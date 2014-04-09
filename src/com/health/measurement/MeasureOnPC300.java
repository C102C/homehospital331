package com.health.measurement;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.bluetooth.BluetoothListActivity;
import com.health.bluetooth.BluetoothService;
import com.health.database.Cache;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.device.HealthDevice;
import com.health.device.PC300;
import com.health.device.PC300.EcgFrame;

import com.health.util.L;
import com.health.util.T;
import com.health.util.TimeHelper;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.Uploader;
import com.health.web.WebService;

/**
 * 测量血压
 * 
 */
public class MeasureOnPC300 extends BaseActivity {

	private static EditText HighBpEditText = null;// 高压文本框
	private static EditText lowBpEditText = null;// 低压文本框
	private static EditText pulseEditText = null;// 脉率
	private static EditText boEditText = null;// 血氧
	private static EditText tempEditText = null;// 体温
	private static Button measureBpButton = null;// 测试测量血压按钮
	private static Button stableBoButton = null;// 测试测量血氧按钮
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
	private static final String TAG = "MeasureBp";
	private static BluetoothService bluetoothService = null;
	private static PC300Handler handler = null;

	private static String btName = "PC_300SNT";// 蓝牙名称
	private static String btMac = null;// 蓝牙名称
	private static Context context;
	private static boolean stop = false;
	private static boolean ecg_stop = false;
	private static LinearLayout graphLayout;// 装血氧图的布局
	private static GraphicalView boWaveView;// 血氧图
	private static XYSeries xSeries;
	private static XYMultipleSeriesRenderer mRenderer;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置当前activity常亮 必须放在setContentView之前
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.measure_on_pc300);
		findId();
		context = this;
		dbService = new DatabaseService(context);
		cache = new Cache(context);
		setVisibility();
		if (handler == null)
			handler = new PC300Handler();
		bluetoothService = BluetoothService.getService(handler, true);// 异步方
		bluetoothService.setSleepTime(20);
		connectPC300();
		setOnClickListener();
		setConnectState();
	}

	@Override
	protected void onStart() {
		super.onStart();
		xSeries = new XYSeries("");
		graphLayout.addView(lineView());
	}

	/**
	 * 设置监听器
	 */
	private void setOnClickListener() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == measureBpButton)
					takeMeasureBp();// 开始测量
				else if (v == findButton)
					startDeviceListActivity();// 开启查找蓝牙activity
				else if (v == stableBoButton) {
					boStable = !boStable;// 血氧恒定
					if (boStable)
						stableBoButton.setText("取消血氧锁定");
					else
						stableBoButton.setText("锁定血氧");
				} else if (v == measureEcgButton) {
					takeMeasurEcg();
				} else if (v == uploadButton) {
					T.showShort(context, "后台开始上传");
					try {
						upload();// 上传数据
					} catch (JSONException e) {
						T.showLong(context, "网络错误!");
						e.printStackTrace();
					}
				}
			}

		};
		measureBpButton.setOnClickListener(onClickListener);
		stableBoButton.setOnClickListener(onClickListener);
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
	private static void setVisibility() {
		int status;
		if (bluetoothService != null
				&& bluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
			status = View.VISIBLE;// 连接时设置可见
		} else {
			status = View.INVISIBLE;// 未连接时设置不可见
		}
		measureBpButton.setVisibility(status);
		stableBoButton.setVisibility(status);
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
		measureBpButton = (Button) findViewById(R.id.start_stop_bp_btn);
		stableBoButton = (Button) findViewById(R.id.bo_stable_button);
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
		graphLayout = (LinearLayout) this.findViewById(R.id.bo_image_view);
	}

	/**
	 * 设置连接状态的显示
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
	 * 进行血压测量、或者停止测量
	 */
	private void takeMeasureBp() {
		if (bluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
			if (stop == false) {
				bluetoothService.write(PC300.COMMAND_BP_START);
				initDataTextEdit();
				measureBpButton.setText("停止血压测量");
			} else {
				bluetoothService.write(PC300.COMMAND_BP_STOP);
				measureBpButton.setText("开始血压测量");
			}
			stop = !stop;
		}
	}

	/**
	 * 进行心电测量、或者停止测量
	 */
	private void takeMeasurEcg() {
		if (bluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
			if (ecg_stop == false) {
				bluetoothService.write(PC300.COMMAND_ECG_START);				
				measureEcgButton.setText("停止心电测量");
			} else {
				bluetoothService.write(PC300.COMMAND_ECG_STOP);
				measureEcgButton.setText("开始心电测量");
			}
			ecg_stop = !ecg_stop;
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
	 * 连接到PC300设备
	 */
	private void connectPC300() {
		if (bluetoothService.getState() == BluetoothService.STATE_NONE) {// 空闲状态才连接

			String address = cache.getDeviceAddress(Cache.PC300);// 查找上次綁定的蓝牙
			btMac = address;
			BluetoothDevice device = bluetoothService
					.getBondedDeviceByAddress(address);
			if (device != null) {
				connect(device);
			} else {
				device = bluetoothService.getBondedDeviceByPrefix("94:21:97",
						"00:13:EF");// 根据前缀找蓝牙
				if (device != null)
					connect(device);
				else
					T.showLong(context, "无已经绑定的PC300设备，试试查找设备...");// 没有配对过得设备，启动查找
			}
		}
	}

	/**
	 * 连接蓝牙设备
	 */
	private void connect(BluetoothDevice device) {
		bluetoothService.connect(device);
		HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
				bluetoothService, PC300.COMMAND_BETTERY, 3000);
		persistWriter.start();// 持续握手
	}

	/**
	 * 连接搜索到的PC300设备
	 * 
	 * @param address
	 */
	private void connectPC300(String address) {
		btMac = address;
		BluetoothDevice device = bluetoothService
				.getRemoteDeviceByAddress(address);
		bluetoothService.connect(device);
		HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
				bluetoothService, PC300.COMMAND_BETTERY, 3000);
		persistWriter.start();// 持续握手
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
				connectPC300(address);
			}
			break;
		}
	}

	private class PC300Handler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					statusView.setText(btName);
					bluetoothService.write(PC300.COMMAND_TEMP_START);
					uploadButton.setEnabled(true);
					uploadButton.setClickable(true);// 设置可以点击
					T.showLong(context, "已连接到" + btName);
					setVisibility();// 设置测量按钮可见
					break;
				case BluetoothService.STATE_CONNECTING:
					statusView.setText(R.string.connecting);
					break;
				case BluetoothService.STATE_NONE:
					statusView.setText(R.string.unconnect);
					setVisibility();
					break;
				}
				break;

			case BluetoothService.MESSAGE_WRITE:
				// byte[] writeBuf = (byte[]) msg.obj;

				break;
			case BluetoothService.MESSAGE_READ:

				byte[] readBuf = (byte[]) msg.obj;
				Log.i(TAG, Arrays.toString(readBuf));
				processReadData(readBuf);
				if (DEBUG) {
					// Log.d(TAG, readMessage);
					Log.d(TAG, Arrays.toString(readBuf));
				}
				break;
			case BluetoothService.MESSAGE_TOAST:
				T.showShort(context,
						msg.getData().getString(BluetoothService.TOAST));
				break;
			case BluetoothService.MESSAGE_DEVICE:
				btName = msg.getData().getString(BluetoothService.DEVICE_NAME);
				String address = msg.getData().getString(
						BluetoothService.DEVICE_ADDRESS);
				cache.saveDeviceAddress(Cache.PC300, address);// 保存地址,以便下次自带连接
				break;
			case UPLOAD_RESULT:
				int status = msg.arg1;
				String item = (String) msg.obj;
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
				break;
			}

		}
	};

	/**
	 * 处理读入的数据,读入的数据可能包含多条协议数据，根据协议的包头将读入的数据分割，
	 * 对于同一类型的数据只保存最新的且校验正确的一条,最后将每一类型的数据响应到界面
	 * 
	 * @param buffer
	 * 
	 */
	public void processReadData(byte[] buffer) {
		PC300 pc300 = new PC300();
		SparseArray<List<byte[]>> map = pc300
				.getLegalPatternsFromBuffer(buffer);
		int dataSize = map.size();
		for (int i = 0; i < dataSize; i++) {
			int token = map.keyAt(i);// 获取token
			List<byte[]> datas = map.get(token);
			switch (token) {
			case PC300.TOKEN_BP_CURRENT:
				Integer currentBp = pc300
						.getCurrentBp(datas.get(datas.size() - 1));// 获取当前血压值
				HighBpEditText.setText(currentBp.toString());
				break;
			case PC300.TOKEN_BP_RESULT:
				int[] bpResult = pc300.getResultBp(datas.get(datas.size() - 1));
				processBpResult(bpResult);
				break;
			case PC300.TOKEN_BO_WAVE:
				for (byte[] data : datas) {
					Log.i("TOKEN_BO_WAVE", Arrays.toString(data));
					int[] value = pc300.getBoWave(data);
					if (null != value)
						updateWaveImage(value, CurvType.BO);
				}
				break;
			case PC300.TOKEN_BO_PAKAGE:
				for (byte[] each : datas) {
					int[] spO2 = pc300.getSpO2(each);
					if (spO2[2] == PC300.PROBE_OFF)
						T.showShort(context, "探针脱落");
					else {
						if (!boStable)
							boEditText.setText(spO2[0] + "");
						// setPulseText(String.valueOf(spO2[1]),
						// PULSE_FROM_BO);
					}

				}
				break;
			case PC300.TOKEN_ECG_WAVE:// 心电曲线
				for (byte[] each : datas) {
					EcgFrame fram = pc300.getEcgFram(each);
					if (null != fram) {
						pc300.addEcgFrameAndSort(fram);
						if (ecgFrameNum == 0) {
							updateWaveImage(fram.getEcg(), CurvType.ECG);
						} else {
							EcgFrame nextFrame = pc300.getNextFrame(0);
							if (nextFrame != null) {
								ecgFrameNum = nextFrame.getFramNum();
								updateWaveImage(nextFrame.getEcg(),
										CurvType.ECG);
							}
						}

						if (fram.isDeviceOff() == true)
							T.showLong(context, "导联脱落");
					}
				}
				break;
			case PC300.TOKEN_ECG_RESULT:
				if (datas.size() <= 0)
					return;
				int[] ecgResult = pc300.getEcgResult(datas.get(0));
				if (ecgResult[0] < 18) {
					// T.showLong(context,
					// PC300.ECG_RESULT[ecgResult[0]]);
					dialog(PC300.ECG_RESULT[ecgResult[0]]);
				}
				if (ecgResult[1] != 0)
					;
				// setPulseText(String.valueOf(ecgResult[1]),
				// PULSE_FROM_ECG);

				break;
			case PC300.TOKEN_TEMP:
				Float temp = pc300.getTemp(datas.get(datas.size() - 1));
				if (!tempStable)
					tempEditText.setText(temp.toString());
				break;
			case PC300.TOKEN_POWER_OFF:
				bluetoothService.stop();
				T.showLong(context, "设备已关机");
				break;
			}
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

	/**
	 * 更新血氧图
	 * 
	 * @param data
	 */
	private void updateWaveImage(int[] data, CurvType type) {
		if (curvType != type) {
			curvType = type;
			if (curvType == CurvType.ECG) {
				mRenderer.setYAxisMin(50);// 设置y轴最小值
				mRenderer.setYAxisMax(200);
				xAxisMax = 300;
				mRenderer.setXAxisMax(xAxisMax);
				xSeries.setTitle("心电曲线图");
			} else if (curvType == CurvType.BO) {
				mRenderer.setYAxisMin(0);// 设置y轴最小值是0
				mRenderer.setYAxisMax(110);
				xAxisMax = 150;
				mRenderer.setXAxisMax(xAxisMax);
				xSeries.setTitle("血氧曲线图");
			}
		}
		int count = 0;
		int model = 3;
		if (curvType == CurvType.BO)
			model = 2;
		for (int each : data) {
			count++;
			if (count % model != 1)
				continue;
			boWaveIndex += 1;
			if (boWaveIndex > xAxisMax) {
				boWaveIndex -= xAxisMax;
			}
			xSeries.add(boWaveIndex, each);
		}
		if (boWaveView != null)
			boWaveView.repaint();
	}

	/**
	 * 处理血压测量结果的显示，分正常和异常两种情况
	 * 
	 * @param bpResult
	 */
	private static void processBpResult(int[] bpResult) {
		if (bpResult[0] == PC300.ERROR_RESULT) {// 测量结果有误
			Log.i(TAG, "ERROR_RESULT:" + bpResult[1]);
			String text = new String();
			switch (bpResult[1]) {
			case PC300.ILLEGAL_PULSE:
				text = "测量不到有效的脉搏";
				break;
			case PC300.BAD_BOUND:
				text = "气袋没有绑好";
				break;
			case PC300.ERROR_VALUE:
				text = "结果数值有误";
				break;
			default:
				text = "未知错误";
			}
			T.showLong(context, text);
		} else {
			String pulseTag = bpResult[0] == 1 ? "心率不齐" : "心率正常";
			T.showLong(context, pulseTag);
			HighBpEditText.setText(Integer.valueOf(bpResult[1]).toString());
			lowBpEditText.setText(Integer.valueOf(bpResult[2]).toString());
			setPulseText(String.valueOf(bpResult[3]), PULSE_FROM_BP);
		}
		measureBpButton.setText("重新测量");// 测量完毕后，又可以测量
		stop = false;
	}

	/**
	 * 画血氧波形图
	 * 
	 * @return
	 */
	public View lineView() {
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer xRenderer = new XYSeriesRenderer();// (类似于一条线对象)
		mDataset.addSeries(xSeries);
		// 设置图表的X轴的当前方向
		mRenderer
				.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		// mRenderer.setYTitle("血氧值");// 设置y轴的标题
		mRenderer.setAxisTitleTextSize(15);// 设置轴标题文本大小
		// mRenderer.setChartTitle("血氧波形图");// 设置图表标题
		mRenderer.setChartTitleTextSize(15);// 设置图表标题文字的大小
		// mRenderer.setLabelsTextSize(18);// 设置标签的文字大小
		mRenderer.setXLabels(30);//
		mRenderer.setYLabels(20);//
		mRenderer.setLegendTextSize(20);// 设置图例文本大小
		mRenderer.setPointSize(1f);// 设置点的大小
		mRenderer.setXAxisMin(0);// 设置y轴最小值
		mRenderer.setXAxisMax(300);
		mRenderer.setYAxisMin(0);// 设置y轴最小值
		mRenderer.setYAxisMax(110);
		mRenderer.setShowGrid(true);// 显示网格
		mRenderer.setMargins(new int[] { 1, 15, 1, 1 });// 设置视图位置
		xRenderer.setColor(Color.BLUE);// 设置颜色
		xRenderer.setPointStyle(PointStyle.CIRCLE);// 设置点的样式
		xRenderer.setFillPoints(true);// 填充点（显示的点是空心还是实心）
		xRenderer.setLineWidth(2);// 设置线宽
		mRenderer.addSeriesRenderer(xRenderer);
		mRenderer.setMarginsColor(Color.WHITE);// 背景设置为白色
		mRenderer.setPanEnabled(false, false);// 设置不能移动曲线
		mRenderer.setShowLabels(false);
		boWaveView = ChartFactory.getCubeLineChartView(this, mDataset,
				mRenderer, 0.0f);
		boWaveView.setBackgroundColor(Color.WHITE);
		return boWaveView;
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

	protected void dialog(String msg) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("测量结论:" + msg + "\n是否上传？");
		builder.setTitle("心电测量结果");

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					uploadEcg();
				} catch (Exception e) {
					e.printStackTrace();
					handler.obtainMessage(UPLOAD_RESULT, WebService.NETERROE,
							0, WebService.PATH_ECG).sendToTarget();
				}

				T.showShort(context, "后台开始上传");

			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	/***
	 * 上传血压\血氧\体温
	 * 
	 * @throws JSONException
	 */
	protected void upload() throws JSONException {
		String dbp = lowBpEditText.getText().toString();// 舒张压
		if (dbp.length() > 0) {// 舒张压有数据，可以上传血压值
			String sbp = HighBpEditText.getText().toString();// 收缩压
			String pulse = pulseEditText.getText().toString();// 脉率
			JSONObject data = getDefaltAttrs();
			data.put(Tables.SBP, sbp);
			data.put(Tables.DBP, dbp);
			data.put(Tables.PULSE, pulse);
			uploadInBack(data, WebService.PATH_BP);

		}
		String temp = tempEditText.getText().toString();
		if (temp.length() > 0) {// 体温有数据
			JSONObject data = getDefaltAttrs();
			data.put(Tables.TEMP, temp);
			uploadInBack(data, WebService.PATH_TEMP);
		}
		String bo = boEditText.getText().toString();
		if (bo.length() > 0) {// 血氧有数据
			JSONObject data = getDefaltAttrs();
			data.put(Tables.BO, bo);
			uploadInBack(data, WebService.PATH_BO);
		}
	}

	/***
	 * 上传心电图
	 * 
	 * @throws Exception
	 */
	protected void uploadEcg() throws Exception {
		JSONObject data = new JSONObject();
		JSONArray array = new JSONArray();
		for (EcgFrame each : PC300.ecgFrames) {
			if (each.getFramNum() != 0)
				for (int v : each.getEcg())
					array.put(v);
		}
		// originalValue：原始值，这个填0.5
		// originalUnit：原始单位，这个和scaleUnit一样是uV
		// scaleUnit：和原来一样，传uV
		// scaleValue：这个填0.1，刻度值，这样我按5倍放大显示
		data.put("data", array);
		data.put("incrementValue", "0.006666667");
		data.put("incrementUnit", "17");
		data.put("scaleUnit", "28");
		data.put("originalValue", "0.5");
		data.put("originalUnit", "28");
		data.put("scaleValue", "0.05");
		data.put("checkTime",
				TimeHelper.getStringTime(PC300.ecgFrames.get(0).getTime()));
		data.put(
				"checkTimeEnd",
				TimeHelper.getStringTime(PC300.ecgFrames.get(
						PC300.ecgFrames.size() - 1).getTime()));
		uploadInBack(data, WebService.PATH_ECG);

	}

	/***
	 * 获取几个测量项目都有的几个属性
	 * 
	 * @return
	 * @throws JSONException
	 */
	private JSONObject getDefaltAttrs() throws JSONException {
		String time = TimeHelper.getCurrentTime();
		JSONObject data = new JSONObject();
		data.put(WebService.TIME, time);
		data.put(WebService.DEVICENAME, btName);
		data.put(WebService.DEVICEMAC, btMac);
		return data;
	}

	/***
	 * 上传数据,data域由具体项目指定
	 * 
	 * @param data
	 * @param path
	 * @throws JSONException
	 */
	public static void upload(JSONObject data, String path) {
		try {
			JSONObject para = new JSONObject();
			String idCard = BaseActivity.getUser().getCardNo();
			para.put(WebService.CARDNO, idCard);
			para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
			para.put(WebService.DATA, data);
			para.put(WebService.CRC, "");
			JSONObject result = WebService.postConenction(para, path);
			int status = result.getInt(WebService.STATUS_CODE);
			handler.obtainMessage(UPLOAD_RESULT, status, 0, path)
					.sendToTarget();
		} catch (Exception e) {
			handler.obtainMessage(UPLOAD_RESULT, WebService.NETERROE, 0, path)
					.sendToTarget();
		}
	}

	/***
	 * 启用后台线程上传数据
	 * 
	 * @param data
	 * @param path
	 */
	private void uploadInBack(final JSONObject data, final String path) {
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				upload(data, path);
			}
		}).start();
	}

}
