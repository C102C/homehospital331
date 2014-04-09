package com.health;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.younext.R;

import com.health.bean.User;
import com.health.bluetooth.BluetoothListActivity;
import com.health.bluetooth.BluetoothService;
import com.health.measurement.Measurement;
import com.identity.Shell;
import com.identity.globalEnum;

public class ReadIDCard extends BaseActivity {
	private static final int DATA_READED = 0x001;
	private static final int PUT_ID_CARD = 0x002;
	private static final int CONNECT_FAILED = 0x003;
	private static final int NOT_FOUND_CARD = 0X004;
	
	private ImageView  iv; 
	private Bitmap bm;
	private Button findIdReaderBtn;
	private Button readBtn;
	private Button goToMeasureBtn;
	private TextView mTVInfo;
	private BluetoothAdapter mAdapter;
	private BluetoothDevice mDevice;
	private static final int REQUEST_ENABLE_BT = 2;
	private Shell shell;
	private boolean bStop = false;
	private boolean bConnected = false;
	private boolean idReaderPaired = false;
	private boolean isConnect = false;

	Runnable findIdReader = new FindIdReader();
	Thread findIdReaderThread = new Thread(findIdReader);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_id_card);
		context = this;

		iv = (ImageView) findViewById(R.id.ivImageview);
		mTVInfo = (TextView) findViewById(R.id.tv_info);
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		readBtn = (Button) findViewById(R.id.btnInit);
		readBtn.setOnClickListener(new ButtonInitOnClick());
		mTVInfo.setText("正在搜索读卡器...");

		if (!isConnect) {
			findIdReaderThread.start();
		}

		findIdReaderBtn = (Button) findViewById(R.id.btn_find_id_reader);
		findIdReaderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent serverIntent = new Intent(ReadIDCard.this,
						BluetoothListActivity.class);
				startActivityForResult(serverIntent,
						BluetoothListActivity.REQUEST_CONNECT_DEVICE);
			}
		});
		
		goToMeasureBtn = (Button) findViewById(R.id.btn_go_to_measure);
		goToMeasureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReadIDCard.this, Measurement.class);
			    startActivity(intent);				
			}
		});
	}
	

	class FindIdReader implements Runnable {
		
		private Message msg;
		@Override
		public void run() {
			try {
				if (mAdapter == null) {
					mTVInfo.setText("mAdapter is null!");
				}
				if (!mAdapter.isEnabled()) {
					Intent enableBtIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				}

				Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();
				if (pairedDevices.size() > 0) {
					for (BluetoothDevice device : pairedDevices) {
						String str;
						str = device.getName().substring(0, 3);
						Log.w("pairedDevices", "device.getName().substring(0, 3) is:"
								+ str);
						if (str.equalsIgnoreCase("SYN") && !idReaderPaired) {
							mDevice = device;
							idReaderPaired = true;
							Log.i("bluetooth", "**************" + mDevice.getName());
							try {
								mAdapter.cancelDiscovery();
								shell = new Shell(ReadIDCard.this.getApplicationContext(), mDevice);
								Log.i("new shell", "**************Socket connect ok**********");
								isConnect = true;
								msg = handler.obtainMessage(PUT_ID_CARD);
								handler.sendMessage(msg);	
								Log.i("putIdCard", "**************  again  **********************");
							} catch (IOException e) {
								e.printStackTrace();
								msg = handler.obtainMessage(CONNECT_FAILED);
								handler.sendMessage(msg);
							}	
						}
					}
					if (!idReaderPaired) {	
						msg = handler.obtainMessage(NOT_FOUND_CARD);
						handler.sendMessage(msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

				connectIdReader(address);
			}
			break;
		}
	}

	private void connectIdReader(String address) {
		mDevice = mAdapter.getRemoteDevice(address);
		Message msg;
		try {
			mAdapter.cancelDiscovery();
			shell = new Shell(ReadIDCard.this.getApplicationContext(), mDevice);
			Log.i("new shell", "**************Socket connect ok**********");
			isConnect = true;
			msg = handler.obtainMessage(PUT_ID_CARD);
			handler.sendMessage(msg);								
		} catch (IOException e) {
			e.printStackTrace();
			msg = handler.obtainMessage(CONNECT_FAILED);
			handler.sendMessage(msg);
		}	
	}
	
	public Handler handler = new Handler() {
		private byte[] data;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PUT_ID_CARD:
				mTVInfo.setText("已连接读卡器\n请点击“读取身份信息”按钮");
				break;
			case CONNECT_FAILED:
				mTVInfo.setText("建立连接失败");
				break;
			case NOT_FOUND_CARD:
				mTVInfo.setText("未找到读卡器，请手动搜索");
				break;
			case DATA_READED:
				data = (byte[]) msg.obj;
				if (data == null) {
					mTVInfo.setText("数据读取中 ...");
				} else {
					try {
						setUser(new User(shell.GetName(data), shell.GetIndentityCard(data)));
						mTVInfo.setText("您好，" + shell.GetName(data));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}	
				}
				break;
			 case 100:
				   bm = (Bitmap) msg.obj;
			       iv.setImageBitmap(bm);			       
			       deleteFile("zp.bmp");
			       goToMeasureBtn.setVisibility(View.VISIBLE);
			       
				   break; 
			  case 101:
					mTVInfo.setText("照片解码授权文件不正确");
				   break; 
			  case 102:
				   mTVInfo.setText("照片原始数据不正确");
				   break; 
			}
		}
	};
	
	private class ButtonInitOnClick implements OnClickListener {
		public void onClick(View v) {
			Log.i("readBtn", "**************readBtn***************");
			globalEnum ge = globalEnum.NONE;
			try {
				if (shell.Register()) {
					
					ge = shell.Init();
					if (ge == globalEnum.INITIAL_SUCCESS) {
						readBtn.setEnabled(false);
						mTVInfo.setText("请把身份证放到读卡器上\n" +
								"如果已经在上面了，请拿起来重放一次");
						
						bConnected = true;
						new Thread(new GetDataThread()).start();
					} else {
						shell.EndCommunication();
						mTVInfo.setText("建立连接失败");
					}
				} else {
					mTVInfo.setText("建立连接失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class GetDataThread implements Runnable {
		private String data = null;
		private byte[] cardInfo = new byte[256];
		private int count = 0;
		private Message msg;
		private boolean bRet = false;
		private String wltPath="";
		private String termBPath="";

		public GetDataThread() {
		}

		public void run() {
			Log.i("Activity", "****************GetDataThread --");
			globalEnum ge = globalEnum.GetIndentiyCardData_GetData_Failed;
			try {
//				Thread.sleep(2000);
				globalEnum gFindCard = globalEnum.NONE;
				while (!bStop) {
					count += 1;
					if (count == 10) {
						System.gc();
						System.runFinalization();
						count = 0;
					}
					data = null;
					bRet = shell.SearchCard();
					if (bRet) {
					
						bRet = shell.SelectCard();
						if (bRet) {
							
							ge = shell.ReadCard();
							if (ge == globalEnum.GetDataSuccess) {
								

								cardInfo = shell.GetCardInfoBytes();
								msg = handler.obtainMessage(DATA_READED, cardInfo);
								handler.sendMessage(msg);
								
								wltPath= "/data/data/cn.younext/files/";
								termBPath="/mnt/sdcard/";
								int nret = shell.GetPic(wltPath,termBPath);
								Log.i("nret", "************" + nret);
								if(nret > 0)
								{
									Bitmap bm = BitmapFactory.decodeFile("/data/data/cn.younext/files/zp.bmp");
									msg = handler.obtainMessage(100, bm);
									handler.sendMessage(msg);

								}else if(nret == -5)
								{
									msg = handler.obtainMessage(101, data);
									handler.sendMessage(msg);
							  	}else if(nret == -1)
							  	{ 
							  		msg = handler.obtainMessage(102, data);
							  		handler.sendMessage(msg);								  
							  	} 
							}
						}
					}
					SystemClock.sleep(50);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bStop = true;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			if (bConnected) {
				Log.w("Activity", "onDestroy bConnected is true");
				if (shell.EndCommunication()) {
					shell.Destroy();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}