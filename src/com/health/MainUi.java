package com.health;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.util.EncodingUtils;

import com.health.measurement.Measurement;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import cn.younext.BatteryView;
import cn.younext.DatabaseHelper;
import cn.younext.R;
import cn.younext.calendar_medical;
import cn.younext.healthinformation;
import cn.younext.healthreport_alarm;
import cn.younext.myhealth;
import cn.younext.teleference;
import cn.younext.R.id;
import cn.younext.R.layout;
import cn.younext.R.raw;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class MainUi extends Activity {
	/** Called when the activity is first created. */
	Button main_myhealth;
	Button main_test;
	Button main_teleference;
	Button main_calendar;
	Button main_healthinformation;
	Button main_healthknowledge;
	Button fresh;
	BatteryView myview;

	OnClickListener btnClick;
	Spinner myspinner;
	DatabaseHelper helper;
	SQLiteDatabase db;
	Cursor c;
	int userid = 1;
	final String USERIDFILE = "userid.txt";
	final String FILE_PATH = "/data/data/cn.younext/";
	File useridfile;
	File healthreport_tag;
	FileOutputStream out;
	FileInputStream in;
	String username = null;
	TextView spinnertext;
	TextView dianliang;

	// private static final String[]
	// users={"用户一","用户二","用户三","用户四","用户五"};
	// private ArrayAdapter<String> adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		DigitalClock mDigitalClock = (DigitalClock) findViewById(R.id.digitalclock);
		myview = (BatteryView) findViewById(R.id.batteryview);
		registerReceiver(mIntentReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		try {
			// 创建“tag1”文件
			useridfile = new File(FILE_PATH, USERIDFILE);
			if (!useridfile.exists()) {
				useridfile.createNewFile();
				out = new FileOutputStream(useridfile);
				String text = Integer.toString(userid);
				Log.v("out", "out");
				out.write(text.getBytes());
				out.close();
			}

			in = new FileInputStream(useridfile);
			int length = (int) useridfile.length();
			byte[] temp = new byte[length];
			in.read(temp, 0, length);
			String text = EncodingUtils.getString(temp, "UTF-8");
			userid = Integer.parseInt(text);
			Log.v("userid", String.valueOf(userid));
			in.close();

		} catch (IOException e) {
			// 将出错信息打印到Logcat
			Log.e("I/O", e.toString());
			// this.finish();
		}

		try {
			String DATABASE_DIR_PATH = this
					.getDir("databases", MODE_WORLD_WRITEABLE).getParentFile()
					.getAbsolutePath()
					+ "/databases"; // 得到数据库文件夹目录在系统中的绝对路径
			String databasefilename = DATABASE_DIR_PATH + "/" + "testrecord"; // 得到数据库在系统中的绝对路径

			File databases = new File(DATABASE_DIR_PATH);
			if (!databases.exists())
				databases.mkdir(); // 如果数据库目录不存在，则新建一个
			Log.e("before executing", "before copying");
			if (!(new File(databasefilename)).exists()) {
				InputStream is = getResources().openRawResource(
						R.raw.testrecord);
				FileOutputStream fos = new FileOutputStream(databasefilename);
				byte buffer[] = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
				Log.e("good news", "can copy this file");
			}
		} catch (Exception e) {

		}

		main_myhealth = (Button) findViewById(R.id.main_myhealthid);
		main_test = (Button) findViewById(R.id.main_testid);
		main_teleference = (Button) findViewById(R.id.main_teleferenceid);
		main_calendar = (Button) findViewById(R.id.main_calendarid);
		main_healthinformation = (Button) findViewById(R.id.main_healthinformationid);
		main_healthknowledge = (Button) findViewById(R.id.main_healthknowledgeid);
		fresh = (Button) findViewById(R.id.main_fresh);

		myspinner = (Spinner) findViewById(R.id.main_spinner);
		spinnertext = (TextView) findViewById(R.id.main_spinnertext);
		dianliang = (TextView) findViewById(R.id.dianliang);

		/*
		 * BroadcastReceiver mBatInfoReceiver = new
		 * BroadcastReceiver(){
		 * 
		 * @Override public void onReceive(Context
		 * arg0, Intent intent) { // TODO
		 * Auto-generated method stub int level =
		 * intent.getIntExtra("level", 0);
		 * dianliang.setText(String.valueOf(level) +
		 * "%"); } };
		 */

		// xueyaList=(ListView)findViewById(R.id.xueyarecord);
		helper = new DatabaseHelper(MainUi.this, DatabaseHelper.DATABASE_NAME,
				null, DatabaseHelper.Version);
		db = helper.getWritableDatabase();// 打开数据库

		/*
		 * c=db.query("usermanage",
		 * null,DatabaseHelper.ID+"="+userid,null,
		 * null,null,"_id desc",null); c.moveToNext();
		 * username=c.getString(1);
		 * spinnertext.setText(username);
		 */

		c = db.query("usermanage", null, null, null, null, null, "_id asc",
				null);// 用户名字查询
		// c.moveToNext();
		// Log.v("before executing",c.getString(3));
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainUi.this,
				android.R.layout.simple_spinner_item, c,
				new String[] { DatabaseHelper.USER_NAME },
				new int[] { R.id.text1 });
		adapter.setDropDownViewResource(R.layout.main_spinner_dropdown);

		// adapter.getDropDownView(MODE_APPEND,
		// myspinner,null);

		// xueyaList.setAdapter(adapter);

		// adapter=new
		// ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,users);
		// x

		myspinner.setAdapter(adapter);
		myspinner.setSelection(userid - 1);
		myspinner
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						arg0.setVisibility(View.VISIBLE);
						Log.e("userid", String.valueOf(userid));
						Log.e("userid", String.valueOf(arg2));
						userid = arg2 + 1;
						Log.e("userid", String.valueOf(userid));
						c.moveToPosition(arg2);
						username = c.getString(1);
						Log.e("username", String.valueOf(username));
						spinnertext.setText(username);

						try {
							// 写入“edittext01”文件

							out = new FileOutputStream(useridfile);
							String test = String.valueOf(userid);
							out.write(test.getBytes());
							out.close();
							Log.v("write", "write");

						} catch (IOException e) {
							// 将出错信息打印到Logcat
							Log.e("writefile", e.toString());
							// drug_calendar.this.finish();
						}

						Log.v("userid", String.valueOf(userid));
						Log.v("username", username);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method
						// stub

					}

				}

				);
		db.close();

		btnClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (v == main_myhealth) {
					intent.setClass(MainUi.this, myhealth.class);
					intent.putExtra("userid", userid);
					intent.putExtra("username", username);
					// i.putExtra("userneme",
					// username);
					startActivity(intent);
				}

				else if (v == main_test) {
					intent.setClass(MainUi.this, Measurement.class);
					intent.putExtra("userid", userid);
					intent.putExtra("username", username);
					startActivity(intent);
				} else if (v == main_teleference) {
					intent.setClass(MainUi.this, teleference.class);
					intent.putExtra("userid", userid);
					intent.putExtra("username", username);
					startActivity(intent);
				} else if (v == main_calendar) {
					intent.setClass(MainUi.this, calendar_medical.class);
					intent.putExtra("userid", userid);
					intent.putExtra("username", username);
					startActivity(intent);
				} else if (v == main_healthinformation) {
					intent.setClass(MainUi.this, healthinformation.class);
					intent.putExtra("userid", userid);
					intent.putExtra("username", username);
					startActivity(intent);
				} else if (v == main_healthknowledge) {
					// i.setClass(main.this,
					// healthknowledge.class);
					// startActivity(i);
					intent.putExtra("userid", userid);
					intent.putExtra("username", username);
					startActivityForResult(new Intent(
							android.provider.Settings.ACTION_SETTINGS), 0);

				} else if (v == fresh) {
					Log.v("mac", "this is a good mark!!");
					try {

						BluetoothAdapter device = BluetoothAdapter
								.getDefaultAdapter();
						String mac = device.getAddress();
						// String
						// name=device.getName();
						Log.e("mac", mac);
						// Log.v("name", name);
						// String
						// mac="00:00:00:00:00:15";
						// String mac =
						// getLocalMacAddress();
						// Log.e("mac",mac);
						edu.hitsz.android.Client client = new edu.hitsz.android.Client();
						// 下面演示的是android客户端发送它的mac地址获得家庭成员信息，及7个mac地址，
						// client.family[5],client.devmac1在client中定义并注释请按顺序读取

						client.getFamilyInfo(mac);// android的mac地址
						helper = new DatabaseHelper(MainUi.this,
								DatabaseHelper.DATABASE_NAME, null,
								DatabaseHelper.Version);
						db = helper.getWritableDatabase();// 打开数据库
						for (int k = 0; k < 5; k++) {
							byte[] buffer_img = client.family[k].photo;
							ContentValues values = new ContentValues();

							String name[] = client.family[k].name.split(" ");
							values.put(DatabaseHelper.USER_NAME, name[0]);// key为字段名，value为值

							String cardNum[] = client.family[k].cardNum
									.split(" ");
							values.put(DatabaseHelper.USERPID, cardNum[0]);

							String gender[] = client.family[k].gender
									.split(" ");
							values.put(DatabaseHelper.USER_SEX, gender[0]);

							String age[] = client.family[k].age.split("-");
							final Calendar c = Calendar.getInstance();
							int mage = c.get(Calendar.YEAR)
									- Integer.parseInt(age[0]); // 获取当前年份
							values.put(DatabaseHelper.USER_AGE, mage);

							values.put(DatabaseHelper.USER_PIC, buffer_img);
							db.update(DatabaseHelper.USER_MANAGE, values,
									"_id=?",
									new String[] { String.valueOf(k + 1) });

						}
						ContentValues values = new ContentValues();
						String mmac1[] = client.devMac1.split(" ");
						values.put(DatabaseHelper.BLUETOOTH_MAC, mmac1[0]);
						db.update(DatabaseHelper.BLUETOOTH_MACTABLE, values,
								"_id=?", new String[] { "1" });

						String mmac2[] = client.devMac2.split(" ");
						values.put(DatabaseHelper.BLUETOOTH_MAC, mmac2[0]);
						db.update(DatabaseHelper.BLUETOOTH_MACTABLE, values,
								"_id=?", new String[] { "2" });

						String mmac3[] = client.devMac1.split(" ");
						values.put(DatabaseHelper.BLUETOOTH_MAC, mmac3[0]);
						db.update(DatabaseHelper.BLUETOOTH_MACTABLE, values,
								"_id=?", new String[] { "3" });

						String mmac4[] = client.devMac1.split(" ");
						values.put(DatabaseHelper.BLUETOOTH_MAC, mmac4[0]);
						db.update(DatabaseHelper.BLUETOOTH_MACTABLE, values,
								"_id=?", new String[] { "4" });

						String mmac5[] = client.devMac1.split(" ");
						values.put(DatabaseHelper.BLUETOOTH_MAC, mmac5[0]);
						db.update(DatabaseHelper.BLUETOOTH_MACTABLE, values,
								"_id=?", new String[] { "5" });

						String mmac6[] = client.devMac1.split(" ");
						values.put(DatabaseHelper.BLUETOOTH_MAC, mmac6[0]);
						db.update(DatabaseHelper.BLUETOOTH_MACTABLE, values,
								"_id=?", new String[] { "6" });

						String mmac7[] = client.devMac1.split(" ");
						values.put(DatabaseHelper.BLUETOOTH_MAC, mmac7[0]);
						db.update(DatabaseHelper.BLUETOOTH_MACTABLE, values,
								"_id=?", new String[] { "7" });

						db.close();
						helper = new DatabaseHelper(MainUi.this,
								DatabaseHelper.DATABASE_NAME, null,
								DatabaseHelper.Version);
						db = helper.getWritableDatabase();// 打开数据库

						/*
						 * c=db.query("usermanage",
						 * null
						 * ,DatabaseHelper.ID+"="+userid
						 * ,null,
						 * null,null,"_id desc",null);
						 * c.moveToNext();
						 * username=c.getString(1);
						 * spinnertext
						 * .setText(username);
						 */

						c = db.query("usermanage", null, null, null, null,
								null, "_id asc", null);// 用户名字查询
						// c.moveToNext();
						// Log.v("before executing",c.getString(3));
						SimpleCursorAdapter adapter = new SimpleCursorAdapter(
								MainUi.this,
								android.R.layout.simple_spinner_item, c,
								new String[] { DatabaseHelper.USER_NAME },
								new int[] { R.id.text1 });
						adapter.setDropDownViewResource(R.layout.main_spinner_dropdown);

						// adapter.getDropDownView(MODE_APPEND,
						// myspinner,null);

						// xueyaList.setAdapter(adapter);

						// adapter=new
						// ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,users);
						// x

						myspinner.setAdapter(adapter);
						myspinner.setSelection(userid - 1);
						myspinner
								.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										arg0.setVisibility(View.VISIBLE);
										Log.v("userid", String.valueOf(userid));
										Log.v("userid", String.valueOf(arg2));
										userid = arg2 + 1;
										Log.v("userid", String.valueOf(userid));
										c.moveToPosition(arg2);
										username = c.getString(1);
										Log.v("username",
												String.valueOf(username));
										spinnertext.setText(username);

										try {
											// 写入“edittext01”文件

											out = new FileOutputStream(
													useridfile);
											String test = String
													.valueOf(userid);
											out.write(test.getBytes());
											out.close();
											Log.v("write", "write");

										} catch (IOException e) {
											// 将出错信息打印到Logcat
											Log.e("writefile", e.toString());
											// drug_calendar.this.finish();
										}

										Log.v("userid", String.valueOf(userid));
										Log.v("username", username);

									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {
										// TODO
										// Auto-generated
										// method stub

									}

								}

								);
						db.close();

					} catch (Exception e) {
						// TODO Auto-generated catch
						// block
						// tv.setText(e.getMessage());
					}

				} else {
					Log.e("this ", "fuck!");
				}
			}

		};
		main_myhealth.setOnClickListener(btnClick);
		main_test.setOnClickListener(btnClick);
		main_teleference.setOnClickListener(btnClick);
		main_calendar.setOnClickListener(btnClick);
		main_healthinformation.setOnClickListener(btnClick);
		main_healthknowledge.setOnClickListener(btnClick);
		fresh.setOnClickListener(btnClick);

		int intervalTime = 600000;// 150分钟
		int intervalweek = 86400000 * 7;
		Calendar alarm = Calendar.getInstance();
		Calendar currentTime = Calendar.getInstance();
		currentTime.setTimeInMillis(System.currentTimeMillis());

		// alarm.setTimeInMillis(System.currentTimeMillis());
		Log.v("week", String.valueOf(alarm.get(Calendar.DAY_OF_WEEK)));

		alarm.set(Calendar.HOUR_OF_DAY, 11);
		alarm.set(Calendar.MINUTE, 10);
		alarm.set(Calendar.SECOND, 0);
		alarm.set(Calendar.MILLISECOND, 0);
		Intent intent = new Intent(MainUi.this, healthreport_alarm.class);
		intent.putExtra("userid", userid);
		intent.putExtra("username", "username");
		PendingIntent sender = PendingIntent.getBroadcast(MainUi.this, 11,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am;
		am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(),
				intervalTime, sender);

		Log.v("main", "main set alarm");
	}

	protected void onResume() {
		super.onResume();
		// 注册消息处理器
		registerReceiver(mIntentReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();

			// 要看看是不是我们要处理的消息
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {

				// 充电状态
				if (intent.getIntExtra("status",
						BatteryManager.BATTERY_STATUS_UNKNOWN) == BatteryManager.BATTERY_STATUS_CHARGING) {
					myview.tag = 0;

					Log.v("电量",
							Integer.toString(intent.getIntExtra("level", 0)));
					myview.invalidate();
				} else if (intent.getIntExtra("status",
						BatteryManager.BATTERY_STATUS_UNKNOWN) == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
					myview.tag = 1;
					myview.dianliang = intent.getIntExtra("level", 0);
					myview.invalidate();

				}

			} else {
				myview.tag = 1;
				myview.dianliang = intent.getIntExtra("level", 0);
				myview.invalidate();

			}
		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.exit(0);
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}
	/*
	 * public String getLocalMacAddress() { WifiManager
	 * wifi = (WifiManager)
	 * getSystemService(Context.WIFI_SERVICE); WifiInfo
	 * info = wifi.getConnectionInfo(); return
	 * info.getMacAddress(); }
	 */

}