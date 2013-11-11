package com.health.measurement;

import java.util.Arrays;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

import com.health.bluetooth.BluetoothListActivity;
import com.health.bluetooth.BluetoothService;
import com.health.device.HealthDevice;
import com.health.device.PC300;
import com.health.util.MiniDataBase;

/**
 * 测量血压
 * 
 */
public class MeasureBp extends Activity {

	static EditText HighBpEditText = null;// 高压文本框
	static EditText lowBpEditText = null;// 低压文本框
	static EditText pulseEditText = null;// 脉率
	static EditText boEditText = null;// 血氧
	static Button measureButton = null;// 测试测量血压按钮
	Button findButton = null;// 查找设备按钮
	static TextView statusView = null;// 蓝牙连接状态

	private static final boolean DEBUG = true;
	private static final String TAG = "MeasureBp";
	private BluetoothService bluetoothService = null;
	private static PC300Handler handler = null;

	private static String btName = "PC_300SNT";// 蓝牙名称
	private static Context context;
	private static MiniDataBase miniDb;// 保存数据工具

	private static boolean stop = false;
	private LinearLayout graphLayout;// 装血氧图的布局
	private static GraphicalView boWaveView;// 血氧图
	static XYSeries xSeries = new XYSeries("血氧");
	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private static float boWaveIndex = 0;
	private static int maxBo = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.measure_bp);

		context = this;
		miniDb = new MiniDataBase(context);
		HighBpEditText = (EditText) findViewById(R.id.hp);
		lowBpEditText = (EditText) findViewById(R.id.lp);
		pulseEditText = (EditText) findViewById(R.id.mb);
		boEditText = (EditText) findViewById(R.id.bo);
		measureButton = (Button) findViewById(R.id.getdata);
		findButton = (Button) findViewById(R.id.find_device);
		statusView = (TextView) findViewById(R.id.connect_status);
		if (handler == null)
			handler = new PC300Handler();
		bluetoothService = BluetoothService.getService(handler, true);// 异步方式
		measureButton.setVisibility(View.GONE);// 开始设置不可见
		graphLayout = (LinearLayout) this.findViewById(R.id.bo_image_view);
		graphLayout.addView(lineView());
		connectPC300();
		measureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				takeMeasureBp();
			}
		});
		findButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startDeviceListActivity();// 开启查找蓝牙activity
			}

		});
		setConnectState();

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
	 * 进行测量、或者停止测量
	 */
	private void takeMeasureBp() {
		if (bluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
			if (stop == false) {
				bluetoothService.write(PC300.COMMAND_BP_START);
				initDataTextEdit();
				measureButton.setText("停止测量");
			} else {
				bluetoothService.write(PC300.COMMAND_BP_STOP);
				measureButton.setText("开始测量");
			}
			stop = !stop;
		}
	}

	/*
	 * 初始化测量显示数据
	 */
	protected void initDataTextEdit() {
		HighBpEditText.setText("");
		lowBpEditText.setText("");
		pulseEditText.setText("");
	}

	/**
	 * 连接到PC300设备
	 */
	private void connectPC300() {
		if (bluetoothService.getState() != BluetoothService.STATE_CONNECTED) {

			String address = miniDb.getDeviceAddress(MiniDataBase.BENECHECK);
			BluetoothDevice device = bluetoothService
					.getBondedDeviceByAddress(address);
			if (device != null) {
				bluetoothService.connect(device);
				HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
						bluetoothService, PC300.COMMAND_BETTERY, 3000);
				persistWriter.start();// 持续握手
			} else {
				Toast.makeText(context, "无已经绑定的PC300设备，试试查找设备...",
						Toast.LENGTH_LONG).show();// 没有配对过得设备，启动查找
			}
		}
	}
	/**
	 * 连接搜索到的PC300设备
	 * @param address
	 */
	private void connectPC300(String address) {
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (DEBUG)
			Log.d(TAG, "onActivityResult " + resultCode);
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

	private static class PC300Handler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					statusView.setText(btName);
					Toast.makeText(context, "已连接到" + btName, Toast.LENGTH_LONG)
							.show();
					measureButton.setVisibility(View.VISIBLE);// 设置测量按钮可见
					break;
				case BluetoothService.STATE_CONNECTING:
					statusView.setText(R.string.connecting);
					measureButton.setVisibility(View.GONE);
					break;
				case BluetoothService.STATE_NONE:
					statusView.setText(R.string.unconnect);
					measureButton.setVisibility(View.GONE);
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
				processReadData(readBuf);
				if (DEBUG) {
					// Log.d(TAG, readMessage);
					Log.d(TAG, Arrays.toString(readBuf));

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

	/**
	 * 处理读入的数据,读入的数据可能包含多条协议数据，根据协议的包头将读入的数据分割，
	 * 对于同一类型的数据只保存最新的且校验正确的一条,最后将每一类型的数据响应到界面
	 * 
	 * @param buffer
	 * 
	 */
	public static void processReadData(byte[] buffer) {
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
					int[] value = pc300.getBoWave(data);
					updateWaveImage(value);
				}
			case PC300.TOKEN_BO_PAKAGE:
				Integer spO2 = pc300.getSpO2(datas.get(datas.size() - 1));
				if (spO2 > maxBo) {
					maxBo = spO2;
					boEditText.setText(spO2.toString());
				}
			}
		}
	}

	/**
	 * 更新血氧图
	 * 
	 * @param data
	 */
	private static void updateWaveImage(int[] data) {
		// Log.i(TAG + ".updateWaveImage",
		// Arrays.toString(data));
		for (int each : data) {
			boWaveIndex += 1;
			if (boWaveIndex > 120) {
				boWaveIndex -= 120;
			}
			xSeries.add(boWaveIndex, each);
		}
		// mRenderer.setXAxisMin(boWaveIndex - 55);
		// mRenderer.setXAxisMax(boWaveIndex + 5);
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
			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
		} else {
			String pulseTag = bpResult[0] == 1 ? "心率不齐" : "心率正常";
			Toast.makeText(context, pulseTag, Toast.LENGTH_LONG).show();
			HighBpEditText.setText(Integer.valueOf(bpResult[1]).toString());
			lowBpEditText.setText(Integer.valueOf(bpResult[2]).toString());
			pulseEditText.setText(Integer.valueOf(bpResult[3]).toString());
		}
		measureButton.setText("重新测量");// 测量完毕后，又可以测量
		stop = false;
	}

	/**
	 * 画血氧波形图
	 * 
	 * @return
	 */
	public View lineView() {
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		XYSeriesRenderer xRenderer = new XYSeriesRenderer();// (类似于一条线对象)

		// for (int i = 0; i < 70; i++) {
		// xSeries.add(i * 1.0 / 10, Math.sin(i * 1.0 /
		// 10));
		// }
		mDataset.addSeries(xSeries);
		// 设置图表的X轴的当前方向
		mRenderer
				.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		mRenderer.setYTitle("血氧值");// 设置y轴的标题
		mRenderer.setAxisTitleTextSize(15);// 设置轴标题文本大小
		mRenderer.setChartTitle("血氧波形图");// 设置图表标题
		mRenderer.setChartTitleTextSize(15);// 设置图表标题文字的大小
		// mRenderer.setLabelsTextSize(18);// 设置标签的文字大小
		mRenderer.setXLabels(0);// 不显示x轴
		mRenderer.setLegendTextSize(20);// 设置图例文本大小
		mRenderer.setPointSize(1f);// 设置点的大小
		mRenderer.setYAxisMin(0);// 设置y轴最小值是0
		mRenderer.setYAxisMax(128);
		mRenderer.setXAxisMax(120);
		mRenderer.setShowGrid(true);// 显示网格
		mRenderer.setMargins(new int[] { 1, 5, 1, 1 });// 设置视图位置

		xRenderer.setColor(Color.BLUE);// 设置颜色
		xRenderer.setPointStyle(PointStyle.CIRCLE);// 设置点的样式
		xRenderer.setFillPoints(true);// 填充点（显示的点是空心还是实心）
		xRenderer.setLineWidth(2);// 设置线宽
		mRenderer.addSeriesRenderer(xRenderer);
		mRenderer.setMarginsColor(Color.TRANSPARENT);// 背景设置为透明
		mRenderer.setZoomEnabled(true, true);// 设置不能移动曲线
		boWaveView = ChartFactory.getCubeLineChartView(this, mDataset,
				mRenderer, 0.6f);
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

}
