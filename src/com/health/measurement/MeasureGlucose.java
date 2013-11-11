package com.health.measurement;

import java.util.Arrays;
import java.util.List;

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
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.BatteryView;
import cn.younext.R;

import com.health.bluetooth.BluetoothListActivity;
import com.health.bluetooth.BluetoothService;
import com.health.device.BeneCheck;
import com.health.device.HealthDevice;
import com.health.device.PC300;
import com.health.util.MiniDataBase;

/**
 * 血糖测量
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2013-10-25 上午9:15:11
 */
public class MeasureGlucose extends Activity {
	private static EditText resultEditText = null;// 测量结果
	private static TextView statusView = null;// 蓝牙连接状态
	private static Button measureGluButton = null;// 开始测量血糖按钮
	private static Button measureUaButton = null;// 开始测量尿酸按钮
	private static Button measureCholButton = null;// 开始测胆固醇量按钮
	private static String btName = "BeneCheck";// 蓝牙名称
	private static BCHandler handler = null;
	private static Context context;
	private static MiniDataBase miniDb;

	private static final String TAG = "MeasureGlucose";
	private static BluetoothService bluetoothService = null;
	Button findButton = null;// 查找设备按钮
	Button homeButton;
	Button backButton;

	OnClickListener clickListener;
	BatteryView batteryView;
	TextView user;
	String username;
	int userid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.measure_glucose);
		context = this;
		miniDb = new MiniDataBase(context);
		batteryView = (BatteryView) findViewById(R.id.batteryview);
		resultEditText = (EditText) this.findViewById(R.id.gluse_value_et);
		statusView = (TextView) this.findViewById(R.id.connect_status);
		findButton = (Button) this.findViewById(R.id.find_device);
		homeButton = (Button) findViewById(R.id.test_xuetang_homeBtn);
		backButton = (Button) findViewById(R.id.test_xuetang_returnBtn);
		measureGluButton = (Button) findViewById(R.id.measure_glu);
		measureUaButton = (Button) findViewById(R.id.measure_ua);
		measureCholButton = (Button) findViewById(R.id.measure_chol);
		clickListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (view == homeButton) {// 返回主界面
					MeasureGlucose.this.setResult(RESULT_OK);
					MeasureGlucose.this.finish();
				} else if (view == backButton) {// 返回上一个界面
					MeasureGlucose.this.finish();
				} else if (view == findButton) {
					startDeviceListActivity();// 开启查找蓝牙activity
				} else if (view == measureGluButton) {// 测量血糖
					sendCommd(BeneCheck.QUERY_GLU_NUM);// 查询记录数量，再查询最新的记录
				} else if (view == measureUaButton) {// 尿酸
					sendCommd(BeneCheck.QUERY_UA_NUM);
				} else if (view == measureCholButton) {// 测量胆固醇
					sendCommd(BeneCheck.QUERY_CHOL_NUM);
				}
			}
		};
		homeButton.setOnClickListener(clickListener);
		backButton.setOnClickListener(clickListener);
		measureGluButton.setOnClickListener(clickListener);
		measureUaButton.setOnClickListener(clickListener);
		measureCholButton.setOnClickListener(clickListener);
		findButton.setOnClickListener(clickListener);
		if (handler == null)// 只创建一个handler
			handler = new BCHandler();
		bluetoothService = BluetoothService.getService(handler, false);// 同步方式
		connectBeneCheck();// 连接百捷
		setConnectState();
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			userid = extra.getInt("userid");
			username = extra.getString("username");
		}

		user = (TextView) findViewById(R.id.test_xuetang_user);
		user.setText(getString(R.string.myhealth_Welcome) + username);
		

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
	 * 连接百捷
	 */
	private void connectBeneCheck() {
		if (bluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
			String address = miniDb.getDeviceAddress(MiniDataBase.BENECHECK);
			BluetoothDevice device = bluetoothService
					.getBondedDeviceByAddress(address);
			if (device != null) {
				bluetoothService.connect(device);// 连接设备
				HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
						bluetoothService, BeneCheck.ECHO, 1000);
				if (!persistWriter.isAlive())
					persistWriter.start();// 持续握手
			} else {
				Toast.makeText(context, "无已经绑定设备，试试查找设备...", Toast.LENGTH_LONG)
						.show();// 没有配对过得设备，启动查找
			}
		}

	}

	/**
	 * 连接搜索到的百捷设备
	 * 
	 * @param address
	 */
	private void connectBeneCheck(String address) {
		BluetoothDevice device = bluetoothService
				.getRemoteDeviceByAddress(address);
		bluetoothService.connect(device);
		HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
				bluetoothService, BeneCheck.ECHO, 1000);
		persistWriter.start();// 持续握手
	}

	/**
	 * 查找蓝牙后，用户指定连接设备，返回进行连接
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case BluetoothListActivity.REQUEST_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(
						BluetoothListActivity.EXTRA_DEVICE_ADDRESS);
				connectBeneCheck(address);
			}
			break;
		}
	}

	protected void onResume() {
		super.onResume();
		// 注册消息处理器

	}

	private static class BCHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					statusView.setText(btName);
					Toast.makeText(context, "已连接到" + btName, Toast.LENGTH_LONG)
							.show();
					measureGluButton.setVisibility(View.VISIBLE);// 设置测量按钮可见
					break;
				case BluetoothService.STATE_CONNECTING:
					statusView.setText(R.string.connecting);
					measureGluButton.setVisibility(View.GONE);
					break;
				case BluetoothService.STATE_NONE:
					statusView.setText(R.string.unconnect);
					measureGluButton.setVisibility(View.GONE);
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
				SparseArray<List<byte[]>> map = BeneCheck
						.getLegalPatternsFromBuffer(readBuf);
				int dataSize = map.size();
				for (int i = 0; i < dataSize; i++) {
					int token = map.keyAt(i);// 获取token
					List<byte[]> datas = map.get(token);
					switch (token) {
					case BeneCheck.TOKEN_GLU_NUM:// 以前总感觉switch的break怪怪的.break让分支独立
					case BeneCheck.TOKEN_CHOL_NUM:// 现在不加break时，会顺序执行，这在多个case值需要
					case BeneCheck.TOKEN_UA_NUM:// 相同的处理时很有用，避免的代码的重复
						int num = BeneCheck.getNum(datas.get(datas.size() - 1));
						byte[] command = BeneCheck.getLatestRecordCommand(
								token, num);
						sendCommd(command);// 得到了记录数目后,马上发送查询记录命令
						break;
					case BeneCheck.TOKEN_GLU_RECORD:
					case BeneCheck.TOKEN_UA_RECORD:
					case BeneCheck.TOKEN_CHOL_RECORD:
						String name = "总胆固醇";
						if (token == BeneCheck.TOKEN_UA_RECORD)
							name = "尿酸";
						if (token == BeneCheck.TOKEN_GLU_RECORD)
							name = "血糖";
						BeneCheck.Record record = BeneCheck.getRecord(datas
								.get(datas.size() - 1));
						resultEditText.setText(name + "#" + record.date + ":"
								+ record.value+" mmg/L");
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
				miniDb.saveDeviceAddress(MiniDataBase.BENECHECK, address);// 保存地址,以便下次自带连接
				break;
			}
		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			MeasureGlucose.this.setResult(RESULT_OK);
			MeasureGlucose.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

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

}
