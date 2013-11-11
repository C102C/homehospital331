package com.health.measurement;

import java.util.Arrays;
import java.util.List;

import com.health.bluetooth.BluetoothListActivity;
import com.health.bluetooth.BluetoothService;
import com.health.device.BeneCheck;
import com.health.device.GmpUa;
import com.health.device.GmpUa.UaRecord;
import com.health.device.HealthDevice;
import com.health.util.MiniDataBase;

import cn.younext.BatteryView;
import cn.younext.R;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 尿液分析仪,可以测量 白细胞 亚硝酸盐 尿胆原 蛋白质 pH值 潜血 比重 酮体 胆红素 葡萄糖
 * 维生素C 12个项目
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2013-10-30 上午10:39:37
 */
public class MeasureUrine extends Activity {
	private static Button homeBottun;// 返回主界面按钮
	private static Button returnBotton;// 返回上一步按钮
	private static Button getDataButton;// 获取最新数据按钮

	private static TextView statusView = null;// 蓝牙连接状态
	private static String btName = "BeneCheck";// 蓝牙名称
	private static UAHandler handler = null;
	private static Context context;
	private static MiniDataBase miniDb;
	private static Button findButton = null;// 查找设备按钮
	private static TextView leuTextView;
	private static TextView nitTextView;
	private static TextView ubgTextView;
	private static TextView proTextView;
	private static TextView phTextView;
	private static TextView sgTextView;
	private static TextView bldTextView;
	private static TextView ketTextView;
	private static TextView bilTextView;
	private static TextView gluTextView;
	private static TextView vcTextView;
	private static TextView dateTextView;

	private static final String TAG = "MeasureUrine";
	private static BluetoothService bluetoothService = null;
	OnClickListener clickListener;
	BatteryView myview;

	TextView user;
	String username;
	int userid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.measure_urine);
		initLayout();

	}

	/**
	 * 初试界面的各种按钮和文本
	 */
	private void initLayout() {
		context = this;
		miniDb = new MiniDataBase(context);
		handler = new UAHandler();
		bluetoothService = BluetoothService.getService(handler, false);
		myview = (BatteryView) findViewById(R.id.batteryview);
		homeBottun = (Button) findViewById(R.id.test_taixin_homeBtn);
		returnBotton = (Button) findViewById(R.id.test_taixin_returnBtn);
		getDataButton = (Button) findViewById(R.id.get_data_button);
		leuTextView = (TextView) findViewById(R.id.leu);
		nitTextView = (TextView) findViewById(R.id.nit);
		ubgTextView = (TextView) findViewById(R.id.ubg);
		proTextView = (TextView) findViewById(R.id.pro);
		phTextView = (TextView) findViewById(R.id.pH);
		sgTextView = (TextView) findViewById(R.id.sg);
		bldTextView = (TextView) findViewById(R.id.bld);
		ketTextView = (TextView) findViewById(R.id.ket);
		bilTextView = (TextView) findViewById(R.id.bil);
		gluTextView = (TextView) findViewById(R.id.glu);
		vcTextView = (TextView) findViewById(R.id.vc);
		dateTextView = (TextView) findViewById(R.id.measure_time);
		statusView = (TextView) findViewById(R.id.connect_status);
		findButton = (Button) findViewById(R.id.find_device);
		clickListener = new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (view == homeBottun) {
					MeasureUrine.this.setResult(RESULT_OK);
					MeasureUrine.this.finish();
				} else if (view == returnBotton) {
					MeasureUrine.this.finish();
				} else if (view == getDataButton) {
					sendCommd(GmpUa.COMMAND_SINGLE_DATA);
				} else if (view == findButton) {
					startDeviceListActivity();// 开启查找蓝牙activity
				}
			}
		};
		homeBottun.setOnClickListener(clickListener);
		returnBotton.setOnClickListener(clickListener);
		getDataButton.setOnClickListener(clickListener);
		findButton.setOnClickListener(clickListener);
		connectGmpUa();
		setConnectState();
	}

	/**
	 * 设置连接状态
	 */
	private void setConnectState() {
		if (bluetoothService == null) {
			statusView.setText(R.string.unconnect);
			return;
		}
		switch (bluetoothService.getState()) {
		case BluetoothService.STATE_CONNECTING:
			statusView.setText(R.string.connecting);
			break;
		case BluetoothService.STATE_CONNECTED:
			statusView.setText(btName);
			break;
		case BluetoothService.STATE_NONE:
			statusView.setText(R.string.unconnect);
			break;
		}
	}

	/**
	 * 开启查找蓝牙设备的activity
	 */
	private void startDeviceListActivity() {
		Intent serverIntent = new Intent(this, BluetoothListActivity.class);
		startActivityForResult(serverIntent,
				BluetoothListActivity.REQUEST_CONNECT_DEVICE);
	}

	/**
	 * 连接尿液分析仪设备
	 */
	private void connectGmpUa() {
		if (bluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
			String address = miniDb.getDeviceAddress(MiniDataBase.GMPUA);
			BluetoothDevice device = bluetoothService
					.getBondedDeviceByAddress(address);
			if (device != null) {
				bluetoothService.connect(device);// 连接设备
				HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
						bluetoothService, GmpUa.CONFIRM, 10000);
				if (!persistWriter.isAlive())
					persistWriter.start();// 持续握手
			} else {
				Toast.makeText(context, "无已经绑定设备，试试查找设备...", Toast.LENGTH_LONG)
						.show();// 没有配对过得设备，启动查找
			}
		}
	}

	/**
	 * 连接搜索到的尿液分析仪设备
	 * 
	 * @param address
	 */
	private void connectGmpUa(String address) {
		BluetoothDevice device = bluetoothService
				.getRemoteDeviceByAddress(address);
		bluetoothService.connect(device);
		HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
				bluetoothService, GmpUa.CONFIRM, 10000);
		persistWriter.start();// 持续握手
	}

	private static class UAHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					statusView.setText(btName);
					Toast.makeText(context, "已连接到" + btName, Toast.LENGTH_LONG)
							.show();
					getDataButton.setVisibility(View.VISIBLE);// 设置测量按钮可见
					break;
				case BluetoothService.STATE_CONNECTING:
					statusView.setText(R.string.connecting);
					getDataButton.setVisibility(View.GONE);
					break;
				case BluetoothService.STATE_NONE:
					statusView.setText(R.string.unconnect);
					getDataButton.setVisibility(View.GONE);
					break;
				}
				break;

			case BluetoothService.MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				new String(writeBuf);
				// Toast.makeText(context,
				// writeMessage,
				// Toast.LENGTH_LONG).show();
				break;
			case BluetoothService.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				Log.i(TAG, "get:" + Arrays.toString(readBuf));
				SparseArray<List<byte[]>> map = GmpUa
						.getLegalPatternsFromBuffer(readBuf);
				int dataSize = map.size();
				for (int i = 0; i < dataSize; i++) {
					int token = map.keyAt(i);// 获取token
					List<byte[]> datas = map.get(token);
					switch (token) {
					case GmpUa.TOKEN_SINGLE_DATA:
						GmpUa.UaRecord record = GmpUa.parseRecord(datas
								.get(datas.size() - 1));
						showRecord(record);// 显示记录
					}
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
				miniDb.saveDeviceAddress(MiniDataBase.GMPUA, address);// 保存地址,以便下次自带连接
				break;
			}
		}

	};

	/**
	 * 发送命令
	 * 
	 * @param command
	 */
	private static void sendCommd(byte[] command) {
		Log.i(TAG, "send:" + Arrays.toString(command));
		if (bluetoothService.getState() == BluetoothService.STATE_CONNECTED
				&& command != null)
			bluetoothService.write(command);
	}

	/**
	 * 显示记录
	 * 
	 * @param record
	 */
	public static void showRecord(UaRecord record) {
		leuTextView.setText(record.leu);
		nitTextView.setText(record.nit);
		ubgTextView.setText(record.ubg);
		proTextView.setText(record.pro);
		phTextView.setText(record.ph);
		sgTextView.setText(record.sg);
		bldTextView.setText(record.bld);
		ketTextView.setText(record.ket);
		bilTextView.setText(record.bil);
		gluTextView.setText(record.glu);
		vcTextView.setText(record.vc);
		dateTextView.setText(record.date);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case BluetoothListActivity.REQUEST_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(
						BluetoothListActivity.EXTRA_DEVICE_ADDRESS);
				connectGmpUa(address);
			}
			break;
		}
	}

	protected void onResume() {
		super.onResume();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			MeasureUrine.this.setResult(RESULT_OK);
			MeasureUrine.this.finish();
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

}
