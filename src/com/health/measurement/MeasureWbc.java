package com.health.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.bean.User;
import com.health.bluetooth.BluetoothListActivity;
import com.health.bluetooth.BluetoothService;
import com.health.database.Cache;
import com.health.device.HealthDevice;
import com.health.device.PC300;
import com.health.util.L;
import com.health.util.MyProgressDialog;
import com.health.util.T;
import com.health.util.TimeHelper;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.WebService;

/***
 * 白细胞测量
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-3-20 上午11:01:19
 */
public class MeasureWbc extends BaseActivity {
	private static MyProgressDialog mDialog;
	private static Context context;
	private static EditText wbcEt;
	private static BluetoothService bluetoothService = null;
	private String btName = "";
	private static ImageView imageview;
	private static TextView statusView;
	private static WbcHandler wbcHandler;
	private static final int UPLOAD = 0x100189;
	private static final int NO_DATA = 0x100190;
	private static final String TAG = "MeasureWbc";
	private static String btMac = "";
	private List<Byte> receiveBytes = new ArrayList<Byte>();
	private static final byte[] WBCSS = { 87, 66, 67, 13, 10 };
	private Float value = null;
	private String unit = null;
	private boolean hasGetData = false;// 是否已经获取到数据

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure_wbc);
		context = this;
		initView();
		wbcHandler = new WbcHandler();
		mDialog = new MyProgressDialog(context);
		bluetoothService = BluetoothService.getService(wbcHandler, true);
	}

	private void initView() {
		imageview = (ImageView) findViewById(R.id.hp_image);
		wbcEt = (EditText) findViewById(R.id.wbc_et);
		statusView = (TextView) findViewById(R.id.connect_status);
	}

	public void uploadWbc(View view) {
		final User user = BaseActivity.getUser();
		if (user == null) {
			T.showLong(context, "用户登录已过期,请退出重新登录");
			return;
		}
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				if (value == null) {
					wbcHandler.obtainMessage(UPLOAD, NO_DATA, 0).sendToTarget();
					return;
				}
				String wbc = String.valueOf(value);
				try {
					JSONObject result = WebService.uploadWbc(wbc,
							user.getCardNo());
					int status = result.getInt(WebService.STATUS_CODE);
					wbcHandler.obtainMessage(UPLOAD, status, 0).sendToTarget();
				} catch (Exception e) {
					L.e(e.getMessage());
					wbcHandler.obtainMessage(UPLOAD, WebService.ERROE, 0)
							.sendToTarget();
				}
			}
		}).start();
		mDialog.show();
	}

	private class WbcHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					statusView.setText(btName);
					Toast.makeText(context, "已连接到" + btName, Toast.LENGTH_LONG)
							.show();
					break;
				case BluetoothService.STATE_CONNECTING:
					statusView.setText(R.string.connecting);
					break;
				case BluetoothService.STATE_NONE:
					statusView.setText(R.string.unconnect);
					break;
				}
				break;

			case BluetoothService.MESSAGE_WRITE:
				//byte[] writeBuf = (byte[]) msg.obj;
				//new String(writeBuf);
				break;
			case BluetoothService.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				if (readBuf == null)
					return;
				if (hasGetData == false) {// 没有获得数据
					hasGetData = check(readBuf);// 检验一下
					if (hasGetData)// 获得了数据即显示
						wbcEt.setText(value + " (" + unit + ")");
				}
				break;
			case BluetoothService.MESSAGE_TOAST:
				Toast.makeText(context,
						msg.getData().getString(BluetoothService.TOAST),
						Toast.LENGTH_SHORT).show();
				break;
			case BluetoothService.MESSAGE_DEVICE:
				btName = msg.getData().getString(BluetoothService.DEVICE_NAME);
				String address = msg.getData().getString(
						BluetoothService.DEVICE_ADDRESS);
				cache.saveDeviceAddress(Cache.GMPUA, address);// 保存地址,以便下次自带连接
				break;
			case UPLOAD:
				mDialog.cancel();
				setImageView(imageview, msg.arg1);
				switch (msg.arg1) {
				case WebService.OK:
					T.showLong(context, "上传成功,收到数据:" + value);
					break;
				case WebService.ERROE:
					T.showLong(context, "上传失败!");
					break;
				case NO_DATA:
					T.showLong(context, "无测量数值!");
					break;
				}
				break;
			}
		}
	};

	private static void setImageView(ImageView imageview, int status) {
		if (status == WebService.OK)
			imageview.setImageResource(R.drawable.light_greed);
		else if (status == -1)
			imageview.setImageResource(R.drawable.light_blue);
		else
			imageview.setImageResource(R.drawable.light_red);
	}

	/***
	 * 检验收到数据是否有测量数值了
	 * 
	 * @param readBuf
	 */
	private boolean check(byte[] readBuf) {
		for (byte b : readBuf) {
			receiveBytes.add(b);
		}
		int index = contains(receiveBytes, WBCSS);
		if (index != -1) {// 包好的wbcss
			if (receiveBytes.size() >= index + 13) {
				List<Byte> sub = receiveBytes.subList(index, index + 13);
				byte[] subArray = new byte[sub.size()];
				for (int i = 0; i < sub.size(); i++)
					subArray[i] = sub.get(i);
				String result = new String(subArray);
				//System.out.println(result);
				String[] results = result.split("\\*");
				if (results.length < 2)
					return false;
				value = Float.valueOf(results[0].trim());
				unit = results[1].trim();
				return true;
			}
		}
		return false;
	}

	private int contains(List<Byte> bytes, byte[] buf) {
		int i = 0;
		int j = 0;
		while (i < bytes.size() && j < buf.length) {
			if (bytes.get(i).equals(Byte.valueOf(buf[j]))) {
				i++;
				j++;
			} else {
				i++;
				j = 0;// 回溯
			}
		}
		if (j == buf.length)
			return i;
		else
			return -1;
	}

	/**
	 * 开启查找蓝牙设备的activity
	 */
	public void startDeviceListActivity(View view) {
		Intent serverIntent = new Intent(this, BluetoothListActivity.class);
		startActivityForResult(serverIntent,
				BluetoothListActivity.REQUEST_CONNECT_DEVICE);
	}

	/**
	 * 查找蓝牙后，用户指定连接设备，返回进行连接
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case BluetoothListActivity.REQUEST_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(
						BluetoothListActivity.EXTRA_DEVICE_ADDRESS);
				connectWbc(address);
			}
			break;
		}
	}

	/**
	 * 连接搜索到的PC300设备
	 * 
	 * @param address
	 */
	private void connectWbc(String address) {
		btMac = address;
		BluetoothDevice device = bluetoothService
				.getRemoteDeviceByAddress(address);
		bluetoothService.connect(device);
		HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
				bluetoothService, PC300.COMMAND_BETTERY, 3000);
		persistWriter.start();// 持续握手
	}

}
