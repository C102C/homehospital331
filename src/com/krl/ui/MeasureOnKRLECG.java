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
 * ����Ѫѹ
 * 
 */
public class MeasureOnKRLECG extends BaseActivity {

	private static EditText HighBpEditText = null;// ��ѹ�ı���
	private static EditText lowBpEditText = null;// ��ѹ�ı���
	private static EditText pulseEditText = null;// ����
	private static EditText boEditText = null;// Ѫ��
	private static EditText tempEditText = null;// ����
	// private static Button measureBpButton = null;// ���Բ���Ѫѹ��ť
	// private static Button stableBoButton = null;// ���Բ���Ѫ����ť
	private static Button measureEcgButton = null;// �����ĵ簴ť
	private static Button uploadButton;// �ϴ����ݰ�ť
	private static boolean boStable = false;// Ѫ���㶨���
	private static boolean tempStable = false;// ���º㶨���
	private static Button findButton = null;// �����豸��ť
	private static TextView statusView = null;// ��������״̬
	private static ImageView hBpImageView = null;// ����ѹǰ��ͼ��
	private static ImageView lBpImageView = null;// ����ѹǰ��ͼ��
	private static ImageView pulseImageView = null;// ����ǰ��ͼ��
	private static ImageView boImageView = null;// Ѫ��ǰ��ͼ��
	private static ImageView tempImageView = null;// ����ǰ��ͼ��
	private static ImageView ecgImageView = null;// �ĵ�ǰ��ͼ��
	private static final boolean DEBUG = true;
	private static final String TAG = "MeasureOnKRLECG";
	// private static BluetoothService bluetoothService = null;
	// private static KrlEcgHandler handler = null;

	private static String btName = "KRL_TMDA";// ��������
	private static String btMac = null;// ��������
	private static Context context;
	private static boolean stop = false;
	private static boolean ecg_stop = false;
	// private static LinearLayout graphLayout;// װѪ��ͼ�Ĳ���
	private static GraphicalView boWaveView;// Ѫ��ͼ
	// private static XYSeries xSeries;
	// private static XYMultipleSeriesRenderer mRenderer;
	private static double xAxisMax = 300;
	private static float boWaveIndex = 0;
	private static float ecgFrameNum = 0;
	private static int pulseSource = 4;// ���ʲ�����Դ
	private static final int PULSE_FROM_BP = 1;// ���ʲ�����Դ��Ѫѹ
	private static final int PULSE_FROM_BO = 2;// ���ʲ�����Դ��Ѫ��
	private static final int PULSE_FROM_ECG = 3;// ���ʲ�����Դ���ĵ�
	private static final int PULSE_FROM_UNKOWN = 4;// ���ʲ�����δ֪
	public static final int UPLOAD_RESULT = 0x10040;

	enum CurvType {
		BO, ECG, NULL
	}

	private static CurvType curvType = CurvType.NULL;

	private static DatabaseService dbService;// ���ݿ����

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
		// ���õ�ǰactivity���� �������setContentView֮ǰ
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
		// bluetoothService = BluetoothService.getService(handler, true);// �첽��
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
	 * ���ü�����
	 */
	private void setOnClickListener() {
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v == findButton)
					startDeviceListActivity();// ������������activity
				else if (v == measureEcgButton) {
					takeMeasurEcg();
				} else if (v == uploadButton) {
					// T.showShort(context, "��̨��ʼ�ϴ�");
					// try {
					// // upload();// �ϴ�����
					// } catch (JSONException e) {
					// T.showLong(context, "�������!");
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
		// ��ʼ��ʱ�����ϴ�����
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
	 * ���ü���button����ʾ������
	 * 
	 * @param status
	 */
	private void setVisibility() {
		int status;
		if (mConnectThead != null && mConnectThead.isConnected()) {
			status = View.VISIBLE;// ����ʱ���ÿɼ�
		} else {
			status = View.INVISIBLE;// δ����ʱ���ò��ɼ�
		}
		measureEcgButton.setVisibility(status);
	}

	/**
	 * ��ʼ��id
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
	 * ��������״̬����ʾ
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
	 * �����ĵ����������ֹͣ����
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
			measureEcgButton.setText("���²���");
			uploadButton.setEnabled(false);
			uploadButton.setClickable(false);
			// } else {
			// mConnectThead.write(KRLECG.STOP);
			// measureEcgButton.setText("��ʼ�ĵ����");
			// }
			// ecg_stop = !ecg_stop;
		}
	}

	/*
	 * ��ʼ��������ʾ����
	 */
	protected void initDataTextEdit() {
		HighBpEditText.setText("");
		lowBpEditText.setText("");
		setPulseText("", PULSE_FROM_UNKOWN);
	}

	/**
	 * ���ӵ�KrlEcg�豸
	 */
	private void connectKrlEcg() {
		String address = cache.getDeviceAddress(Cache.KRLECG);// �����ϴν���������
		if (null != address && !"".equals(address)) {
			connectKrlEcg(address);
		} else {
			Toast.makeText(this, "���Ѿ��󶨵�KRL-ECG�豸�����Բ����豸...",
					Toast.LENGTH_SHORT).show();
		}
		// saveDeviceAddress
	}

	/**
	 * ������������PC300�豸
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
	 * �������������豸��activity
	 */
	private void startDeviceListActivity() {
		Intent serverIntent = new Intent(this, BluetoothListActivity.class);
		startActivityForResult(serverIntent,
				BluetoothListActivity.REQUEST_CONNECT_DEVICE);
	}

	/**
	 * �����������û�ָ�������豸�����ؽ�������
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
	 * �������ʵ�ֵ,�������Դ
	 * 
	 * @param value
	 *            ����ֵ
	 * @param source
	 *            ������Դ(Ѫ��\Ѫѹ\�ĵ�\����)
	 */
	private static void setPulseText(String value, int source) {
		pulseSource = source;
		pulseEditText.setText(value);

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// bluetoothService.stop();//
			// �˳�activity��ر���������
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO
			// �Զ����ɵķ������
			switch (msg.what) {
			case Bluetooth.MESSAGE_DATA:// �ĵ�����
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
							mTimer.setText(String.format("��ʣ:%d��",
									mDataParser.mDataSegment.RemainTime()));
						}
					}
					if (mDataParser.mDataSegment.IsCompleted()
							&& mStart == true) {
						mStart = false;
						mTimer.setText("");
						// measureEcgButton.setText("��ʼ�ĵ����");
						// ecg_stop = !ecg_stop;
						Toast.makeText(MeasureOnKRLECG.this, "���ڱ������ݣ������ĵȴ�...",
								Toast.LENGTH_LONG).show();
						// ֹͣ
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
				Toast.makeText(MeasureOnKRLECG.this, "�ɼ�ʧ�ܣ������²ɼ�",
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
		statusView.setText("������");
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
		statusView.setText("δ����");
		measureEcgButton.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onDestroy() {
		Log.e(TAG,"onDestroy");
		unbindService(connection);
		unregisterReceiver(uploadResultReceiver);
		// TODO �Զ����ɵķ������

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
		// TODO �Զ����ɵķ������
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

	// �㲥������ - �㲥�Ľ���
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
							T.showLong(context, "�ĵ��ϴ��ɹ�");
						else
							T.showLong(context, "�ĵ��ϴ�ʧ��");
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
