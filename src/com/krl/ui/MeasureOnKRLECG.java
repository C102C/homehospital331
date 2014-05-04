package com.krl.ui;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.bean.User;
import com.health.bluetooth.BluetoothListActivity;
import com.health.database.Cache;
import com.health.database.DatabaseService;
import com.health.util.T;
import com.health.web.WebService;
import com.krl.bluetooth.Bluetooth;
import com.krl.bluetooth.BluetoothConnectThread;
import com.krl.bluetooth.BluetoothConnectThread.BluetoothConnectionState;
import com.krl.bluetooth.BluetoothDataParser;
import com.krl.device.KRLECG;
import com.krl.service.BackService;
import com.krl.service.IBackService;
import com.krl.staticecg.ECGSurfaceView;
import com.krl.staticecg.StaticApp;
import com.krl.tools.Tools;



/**
 * 测量血压
 * 
 */
public class MeasureOnKRLECG extends BaseActivity {

	private static EditText HighBpEditText = null;// 高压文本框
	private static EditText lowBpEditText = null;// 低压文本框
	private static EditText pulseEditText = null;// 脉率
	private static EditText boEditText = null;// 血氧
	private static EditText tempEditText = null;// 体温
	// private static Button measureBpButton = null;// 测试测量血压按钮
	// private static Button stableBoButton = null;// 测试测量血氧按钮
	private static Button measureEcgButton = null;// 测量心电按钮
	private static Button uploadButton;// 上次数据按钮
	private static boolean boStable = false;// 血氧恒定标记
	private static boolean tempStable = false;// 体温恒定标记
	private static Button findButton = null;// 查找设备按钮
	private static TextView statusView = null;// 蓝牙连接状态
	private static ImageView hBpImageView = null;// 收缩压前面图标
	private static ImageView lBpImageView = null;// 舒张压前面图标
	private static ImageView pulseImageView = null;// 脉率前面图标
	private static ImageView boImageView = null;// 血氧前面图标
	private static ImageView tempImageView = null;// 体温前面图标
	private static ImageView ecgImageView = null;// 心电前面图标
	private static final boolean DEBUG = true;
	private static final String TAG = "MeasureOnKRLECG";
	// private static BluetoothService bluetoothService = null;
	// private static KrlEcgHandler handler = null;

	private static String btName = "KRL_TMDA";// 蓝牙名称
	private static String btMac = null;// 蓝牙名称
	private static Context context;
	private static boolean stop = false;
	private static boolean ecg_stop = false;
	// private static LinearLayout graphLayout;// 装血氧图的布局
	private static GraphicalView boWaveView;// 血氧图
	// private static XYSeries xSeries;
	// private static XYMultipleSeriesRenderer mRenderer;
	private static double xAxisMax = 300;
	private static float boWaveIndex = 0;
	private static float ecgFrameNum = 0;
	private static int pulseSource = 4;// 脉率测量来源
	private static final int PULSE_FROM_BP = 1;// 脉率测量来源于血压
	private static final int PULSE_FROM_BO = 2;// 脉率测量来源于血氧
	private static final int PULSE_FROM_ECG = 3;// 脉率测量来源于心电
	private static final int PULSE_FROM_UNKOWN = 4;// 脉率测量来未知
	public static final int UPLOAD_RESULT = 0x10040;

	enum CurvType {
		BO, ECG, NULL
	}

	private static CurvType curvType = CurvType.NULL;

	private static DatabaseService dbService;// 数据库服务

	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothConnectThread mConnectThead = null;
	private BluetoothDataParser mDataParser = null;
	private DisplayMetrics mMetric = null;
	private ECGSurfaceView mEcgView = null;
	private TextView mTimer;
	private boolean mStart = false;
	private IBackService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		regist();
		// 设置当前activity常亮 必须放在setContentView之前
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.measure_on_krlecg);
		findId();
		initECGView();
		context = this;
		dbService = new DatabaseService(context);
		cache = new Cache(context);
		setVisibility();
		// bluetoothService = BluetoothService.getService(handler, true);// 异步方
		// bluetoothService.setSleepTime(20);
		setOnClickListener();
		connectKrlEcg();
		setConnectState();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// xSeries = new XYSeries("");
		// graphLayout.addView(lineView());
	}

	private void initECGView() {
		mTimer = (TextView) findViewById(R.id.textView_timer);
		mDataParser = new BluetoothDataParser(1000 * 2 * 12, 27, 12,
				StaticApp.getinstance().mDataMager.mECG);
		mMetric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mMetric);
		mEcgView = (ECGSurfaceView) findViewById(R.id.surfaceView_Sampling);
		mEcgView.mDataSegment = StaticApp.getinstance().mDataMager.mECG;
		mEcgView.init_canvas_param(mMetric, mEcgView.mDataSegment.mDesc,
				mDataParser.GetShowBuffer());
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mDataParser.mDataSegment.mDataSize = 0;
	}

	/**
	 * 设置监听器
	 */
	private void setOnClickListener() {
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v == findButton)
					startDeviceListActivity();// 开启查找蓝牙activity
				else if (v == measureEcgButton) {
					takeMeasurEcg();
				} else if (v == uploadButton) {
					// T.showShort(context, "后台开始上传");
					// try {
					// // upload();// 上传数据
					// } catch (JSONException e) {
					// T.showLong(context, "网络错误!");
					// e.printStackTrace();
					// }

					try {
						if (null != service)
							service.uploadEcgFileData();
					} catch (RemoteException e) {
						e.printStackTrace();
					}

				}
			}

		};
		// measureBpButton.setOnClickListener(onClickListener);
		// stableBoButton.setOnClickListener(onClickListener);
		measureEcgButton.setOnClickListener(onClickListener);
		uploadButton.setOnClickListener(onClickListener);
		findButton.setOnClickListener(onClickListener);
		// uploadButton.setClickable(true);//
		// 初始化时不可上传数据
	}

	private static void setImageView(ImageView imageview, int status) {
		if (status == WebService.OK)
			imageview.setImageResource(R.drawable.light_greed);
		else if (status == -1)
			imageview.setImageResource(R.drawable.light_blue);
		else
			imageview.setImageResource(R.drawable.light_red);
	}

	/**
	 * 设置几个button的显示与隐藏
	 * 
	 * @param status
	 */
	private void setVisibility() {
		int status;
		if (mConnectThead != null && mConnectThead.isConnected()) {
			status = View.VISIBLE;// 连接时设置可见
		} else {
			status = View.INVISIBLE;// 未连接时设置不可见
		}
		measureEcgButton.setVisibility(status);
	}

	/**
	 * 初始化id
	 */
	private void findId() {
		HighBpEditText = (EditText) findViewById(R.id.hp);
		lowBpEditText = (EditText) findViewById(R.id.lp);
		pulseEditText = (EditText) findViewById(R.id.mb);
		boEditText = (EditText) findViewById(R.id.bo);
		tempEditText = (EditText) findViewById(R.id.temp);
		// measureBpButton = (Button) findViewById(R.id.start_stop_bp_btn);
		// stableBoButton = (Button) findViewById(R.id.bo_stable_button);
		measureEcgButton = (Button) findViewById(R.id.start_stop_ecg_btn);
		findButton = (Button) findViewById(R.id.find_device);
		uploadButton = (Button) findViewById(R.id.upload_button);
		statusView = (TextView) findViewById(R.id.connect_status);
		hBpImageView = (ImageView) findViewById(R.id.hp_image);
		lBpImageView = (ImageView) findViewById(R.id.lp_image);
		pulseImageView = (ImageView) findViewById(R.id.pulse_image);
		boImageView = (ImageView) findViewById(R.id.bo_image);
		tempImageView = (ImageView) findViewById(R.id.temp_image);
		ecgImageView = (ImageView) findViewById(R.id.ecg_image);
		// graphLayout = (LinearLayout) this.findViewById(R.id.bo_image_view);
	}

	/**
	 * 设置连接状态的显示
	 */
	private void setConnectState() {
		if (mConnectThead == null) {
			statusView.setText(R.string.unconnect);
			return;
		}
		int state = mConnectThead.getConnectionState();
		if (BluetoothConnectionState.CONNECTING.ordinal() == state) {
			statusView.setText(R.string.connecting);
		} else if (BluetoothConnectionState.CONNECTED.ordinal() == state) {
			statusView.setText("CONNECTED");
		} else if (BluetoothConnectionState.IOEXCEPTION.ordinal() == state) {
			statusView.setText("IOEXCEPTION");
		} else if (BluetoothConnectionState.DISCONNECTED.ordinal() == state) {
			statusView.setText("DISCONNECTED");
		} else if (BluetoothConnectionState.INTERRUPTEDEXCEPTION.ordinal() == state) {
			statusView.setText("INTERRUPTEDEXCEPTION");
		}
	}



	/**
	 * 进行心电测量、或者停止测量
	 */
	private void takeMeasurEcg() {
		if (mConnectThead.isConnected()) {
			// if (ecg_stop == false) {
			mEcgView.nStartPos = 0;
			mEcgView.nLastPos = 0;
			mStart = true;
			mDataParser.mDataSegment
					.SetSampling(StaticApp.getinstance().mSamplingTime);
			mTimer.setText("");
			mConnectThead.SendStop();
			mEcgView.init_canvas_param(mMetric, mDataParser.mDataSegment.mDesc,
					mDataParser.GetShowBuffer());
			mDataParser.mDataSegment.mStartTime = Tools.GetCurrentTime();
			mDataParser.mStartSampling = true;
			mDataParser.clear();
			mConnectThead.write(KRLECG.ECG);
			measureEcgButton.setText("重新测量");
			uploadButton.setEnabled(false);
			uploadButton.setClickable(false);
			// } else {
			// mConnectThead.write(KRLECG.STOP);
			// measureEcgButton.setText("开始心电测量");
			// }
			// ecg_stop = !ecg_stop;
		}
	}

	/*
	 * 初始化测量显示数据
	 */
	protected void initDataTextEdit() {
		HighBpEditText.setText("");
		lowBpEditText.setText("");
		setPulseText("", PULSE_FROM_UNKOWN);
	}

	/**
	 * 连接到KrlEcg设备
	 */
	private void connectKrlEcg() {
		String address = cache.getDeviceAddress(Cache.KRLECG);// 查找上次綁定的蓝牙
		if (null != address && !"".equals(address)) {
			connectKrlEcg(address);
		} else {
			Toast.makeText(this, "无已经绑定的KRL-ECG设备，试试查找设备...",
					Toast.LENGTH_SHORT).show();
		}
		// saveDeviceAddress
	}

	/**
	 * 连接搜索到的PC300设备
	 * 
	 * @param address
	 */
	private void connectKrlEcg(String address) {
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		if (device != null) {
			if (mConnectThead != null) {
				mConnectThead.cancel();
			}
			mConnectThead = new BluetoothConnectThread(device, mHandler, 0,
					mDataParser, mBluetoothAdapter);
			mConnectThead.start();

			if (null != service) {
				try {
					service.setDeviceInfo(Cache.KRLECG, address);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
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
				cache.saveDeviceAddress(Cache.KRLECG, address);
				connectKrlEcg(address);
			}
			break;
		}
	}


	/***
	 * 设置脉率的值,并标记来源
	 * 
	 * @param value
	 *            脉率值
	 * @param source
	 *            测量来源(血氧\血压\心电\其他)
	 */
	private static void setPulseText(String value, int source) {
		pulseSource = source;
		pulseEditText.setText(value);

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// bluetoothService.stop();//
			// 退出activity后关闭蓝牙连接
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO
			// 自动生成的方法存根
			switch (msg.what) {
			case Bluetooth.MESSAGE_DATA:// 心电数据
//				Log.e(TAG, "MESSAGE_DATA");
				if (mEcgView != null) {
					mDataParser.mScreenWidth = mEcgView.mScreenWidth;
				}
				// mDataParser.push(msg.getData().getByteArray(Bluetooth.MESSAGE_KEY_DATA),
				// msg.getData().getInt(Bluetooth.MESSAGE_KEY_SIZE));
				mDataParser.Parser();
				if (mEcgView != null) {
					mDataParser.mScreenWidth = mEcgView.mScreenWidth;
					mEcgView.nEndPos = mDataParser.GetCurIndex();
					if (mStart == true) {
						if (mTimer != null) {
							mTimer.setText(String.format("还剩:%d秒",
									mDataParser.mDataSegment.RemainTime()));
						}
					}
					if (mDataParser.mDataSegment.IsCompleted()
							&& mStart == true) {
						mStart = false;
						mTimer.setText("");
						// measureEcgButton.setText("开始心电测量");
						// ecg_stop = !ecg_stop;
						Toast.makeText(MeasureOnKRLECG.this, "正在保存数据，请耐心等待...",
								Toast.LENGTH_LONG).show();
						// 停止
						// mConnectThead.write(KRLECG.STOP);
						if (null != service) {
							try {
								User patient = BaseActivity.getUser();
								service.setPatientInfo(patient.getName(),
										patient.getCardNo(),
										patient.getBirthday(),
										patient.getSex(),
										patient.getNickName(),
										patient.getCardNo(),
										patient.getUserGuid(),
										patient.getMobile(), patient.getEmail());
								service.saveECGToFile();
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
					}
				}
				break;
			case Bluetooth.MESSAGE_CONNECTED: {
				int state = msg.getData().getInt(Bluetooth.MESSAGE_KEY_DATA);
				Log.e(TAG, "MESSAGE_CONNECTED:" + state);
				if (BluetoothConnectionState.CONNECTING.ordinal() == state) {
					onDeviceDisConnected();
				} else if (BluetoothConnectionState.CONNECTED.ordinal() == state) {
					onDeviceConnected();
				} else if (BluetoothConnectionState.IOEXCEPTION.ordinal() == state) {
					onDeviceDisConnected();
				} else if (BluetoothConnectionState.DISCONNECTED.ordinal() == state) {
					onDeviceDisConnected();
				} else if (BluetoothConnectionState.INTERRUPTEDEXCEPTION
						.ordinal() == state) {
					onDeviceDisConnected();
				}
				break;
			}
			case Bluetooth.MESSAGE_CLOSED: {
				Toast.makeText(MeasureOnKRLECG.this, "采集失败，请重新采集",
						Toast.LENGTH_LONG).show();
				finish();
				break;
			}
			default:
				break;
			}
		}
	};

	private void onDeviceConnected() {
		statusView.setText("已连接");
		measureEcgButton.setVisibility(View.VISIBLE);
		String address = mBluetoothAdapter.getAddress();
		StaticApp.getinstance().mMAC = address;
		if (null != service) {
			try {
				service.setDeviceInfo(Cache.KRLECG, address);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	private void onDeviceDisConnected() {
		statusView.setText("未连接");
		measureEcgButton.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onDestroy() {
		Log.e(TAG,"onDestroy");
		unbindService(connection);
		unregisterReceiver(uploadResultReceiver);
		// TODO 自动生成的方法存根

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.e(TAG,"onPause");
		if (mConnectThead != null) {
			mConnectThead.write(KRLECG.STOP);
			mConnectThead.cancel();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
	}

	private void regist() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BackService.UPLOAD_RESULT_ACTION);
		intentFilter.addAction(BackService.SAMPLING_RESULT_ACTION);
		registerReceiver(uploadResultReceiver, intentFilter);
		bindService(new Intent(this, BackService.class), connection,
				Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			service = IBackService.Stub.asInterface(arg1);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			service = null;
		}
	};

	// 广播接收者 - 广播的接收
	private BroadcastReceiver uploadResultReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BackService.UPLOAD_RESULT_ACTION.equals(action)) {
				Bundle data = intent.getExtras();
				if (null != data) {
					int status = data.getInt("status");
					String item = data.getString("path");
					if (WebService.PATH_BP.equals(item)) {
						setImageView(hBpImageView, status);
						setImageView(lBpImageView, status);
						setImageView(pulseImageView, status);
					} else if (WebService.PATH_BO.equals(item))
						setImageView(boImageView, status);
					else if (WebService.PATH_TEMP.equals(item))
						setImageView(tempImageView, status);
					else if (WebService.PATH_ECG.equals(item)) {
						setImageView(ecgImageView, status);
						if (WebService.OK == status)
							T.showLong(context, "心电上传成功");
						else
							T.showLong(context, "心电上传失败");
					}
				}
			} else if (BackService.SAMPLING_RESULT_ACTION.equals(action)) {
			
				boolean saveFileSuccess = intent.getBooleanExtra(
						"SaveFileSuccess", false);
				if (saveFileSuccess) {
					uploadButton.setEnabled(true);
					uploadButton.setClickable(true);
				}
			}
		}
	};
}
