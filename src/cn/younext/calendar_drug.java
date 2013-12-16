package cn.younext;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.health.BaseActivity;

public class calendar_drug extends BaseActivity {

	Button homeBtn;
	Button medical;

	Button xuanzhongBtn1;
	Button xuanzhongBtn2;
	Button xuanzhongBtn3;
	Button xuanzhongBtn4;
	Button xuanzhongBtn5;

	Button clockBtn1;
	Button clockBtn2;
	Button clockBtn3;
	Button clockBtn4;
	Button clockBtn5;

	EditText drug_edit1;
	EditText drug_edit2;
	EditText drug_edit3;
	EditText drug_edit4;
	EditText drug_edit5;

	TextView drug_spinnertext1;
	TextView drug_spinnertext2;
	TextView drug_spinnertext3;
	TextView drug_spinnertext4;
	TextView drug_spinnertext5;

	Spinner drug_spinner1;
	Spinner drug_spinner2;
	Spinner drug_spinner3;
	Spinner drug_spinner4;
	Spinner drug_spinner5;

	int spinnerid1;
	int spinnerid2;
	int spinnerid3;
	int spinnerid4;
	int spinnerid5;

	DatabaseHelper helper;
	SQLiteDatabase db;
	Cursor c;
	Cursor c1;

	final int intervalTime = 86400000;

	OnClickListener btnClick;

	TextView user;
	String username;
	int userid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.calendar_drug2);

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			userid = extra.getInt("userid");
			username = extra.getString("username");

		}

		user = (TextView) findViewById(R.id.calendar_drug_user);
		user.setText(getString(R.string.myhealth_Welcome) + username);

		homeBtn = (Button) findViewById(R.id.calendar_drug_homeBtn);
		medical = (Button) findViewById(R.id.calendar_medical);

		xuanzhongBtn1 = (Button) findViewById(R.id.calendar_drug_xuanzhongBtn1);
		xuanzhongBtn2 = (Button) findViewById(R.id.calendar_drug_xuanzhongBtn2);
		xuanzhongBtn3 = (Button) findViewById(R.id.calendar_drug_xuanzhongBtn3);
		xuanzhongBtn4 = (Button) findViewById(R.id.calendar_drug_xuanzhongBtn4);
		xuanzhongBtn5 = (Button) findViewById(R.id.calendar_drug_xuanzhongBtn5);

		clockBtn1 = (Button) findViewById(R.id.calendar_drug_clockBtn1);
		clockBtn2 = (Button) findViewById(R.id.calendar_drug_clockBtn2);
		clockBtn3 = (Button) findViewById(R.id.calendar_drug_clockBtn3);
		clockBtn4 = (Button) findViewById(R.id.calendar_drug_clockBtn4);
		clockBtn5 = (Button) findViewById(R.id.calendar_drug_clockBtn5);

		drug_edit1 = (EditText) findViewById(R.id.calendar_drug_edittext1);
		drug_edit2 = (EditText) findViewById(R.id.calendar_drug_edittext2);
		drug_edit3 = (EditText) findViewById(R.id.calendar_drug_edittext3);
		drug_edit4 = (EditText) findViewById(R.id.calendar_drug_edittext4);
		drug_edit5 = (EditText) findViewById(R.id.calendar_drug_edittext5);

		drug_spinnertext1 = (TextView) findViewById(R.id.calendar_drug_spinnertext1);
		drug_spinnertext2 = (TextView) findViewById(R.id.calendar_drug_spinnertext2);
		drug_spinnertext3 = (TextView) findViewById(R.id.calendar_drug_spinnertext3);
		drug_spinnertext4 = (TextView) findViewById(R.id.calendar_drug_spinnertext4);
		drug_spinnertext5 = (TextView) findViewById(R.id.calendar_drug_spinnertext5);

		drug_spinner1 = (Spinner) findViewById(R.id.calendar_drug_spinner1);
		drug_spinner2 = (Spinner) findViewById(R.id.calendar_drug_spinner2);
		drug_spinner3 = (Spinner) findViewById(R.id.calendar_drug_spinner3);
		drug_spinner4 = (Spinner) findViewById(R.id.calendar_drug_spinner4);
		drug_spinner5 = (Spinner) findViewById(R.id.calendar_drug_spinner5);

		helper = new DatabaseHelper(calendar_drug.this,
				DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.Version);
		db = helper.getWritableDatabase();
		c = db.query(DatabaseHelper.ALARMTABLE, null, DatabaseHelper.ID + ">5",
				null, null, null, DatabaseHelper.ID + " asc");

		c.moveToNext();
		initialize(this, c, xuanzhongBtn1, drug_edit1, clockBtn1,
				drug_spinner1, drug_spinnertext1, "6");
		c.moveToNext();
		initialize(this, c, xuanzhongBtn2, drug_edit2, clockBtn2,
				drug_spinner2, drug_spinnertext2, "7");
		c.moveToNext();
		initialize(this, c, xuanzhongBtn3, drug_edit3, clockBtn3,
				drug_spinner3, drug_spinnertext3, "8");
		c.moveToNext();
		initialize(this, c, xuanzhongBtn4, drug_edit4, clockBtn4,
				drug_spinner4, drug_spinnertext4, "9");
		c.moveToNext();
		initialize(this, c, xuanzhongBtn5, drug_edit5, clockBtn5,
				drug_spinner5, drug_spinnertext5, "10");

		db.close();

		btnClick = new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (v == homeBtn) {
					alarmTimeSet(calendar_drug.this, "6", 6, drug_edit1);
					alarmTimeSet(calendar_drug.this, "7", 7, drug_edit2);
					alarmTimeSet(calendar_drug.this, "8", 8, drug_edit3);
					alarmTimeSet(calendar_drug.this, "9", 9, drug_edit4);
					alarmTimeSet(calendar_drug.this, "10", 10, drug_edit5);
					// test_zhifang1.this.setResult(RESULT_OK);
					/*
					 * c=db.query(DatabaseHelper.ALARMTABLE
					 * , null, DatabaseHelper.ID+"=1",
					 * null, null, null,
					 * DatabaseHelper.ID+" asc");
					 * c.moveToNext(); ContentValues
					 * values = new ContentValues();
					 * values.put(DatabaseHelper.TEXT,
					 * medical_edit1
					 * .getText().toString(
					 * ));//key为字段名，value为值
					 * db.update(DatabaseHelper
					 * .ALARMTABLE, values, "_id=?",
					 * new String[]{"1"});
					 */
					calendar_drug.this.setResult(RESULT_OK);
					calendar_drug.this.finish();

					// /////////////////////保存edittext内容
				} else if (v == medical) {
					alarmTimeSet(calendar_drug.this, "6", 7, drug_edit1);
					alarmTimeSet(calendar_drug.this, "7", 7, drug_edit2);
					alarmTimeSet(calendar_drug.this, "8", 8, drug_edit3);
					alarmTimeSet(calendar_drug.this, "9", 9, drug_edit4);
					alarmTimeSet(calendar_drug.this, "10", 10, drug_edit5);
					calendar_drug.this.finish();
					/*
					 * c=db.query(DatabaseHelper.ALARMTABLE
					 * , null, DatabaseHelper.ID+"=1",
					 * null, null, null,
					 * DatabaseHelper.ID+" asc");
					 * c.moveToNext(); ContentValues
					 * values = new ContentValues();
					 * values.put(DatabaseHelper.TEXT,
					 * medical_edit1
					 * .getText().toString(
					 * ));//key为字段名，value为值
					 * db.update(DatabaseHelper
					 * .ALARMTABLE, values, "_id=?",
					 * new String[]{"1"});
					 */

				} else if (v == xuanzhongBtn1) {
					xuanzhongAct(calendar_drug.this, xuanzhongBtn1, "6", 6);
					/*
					 * c=db.query(DatabaseHelper.ALARMTABLE
					 * , null, DatabaseHelper.ID+"=1",
					 * null, null, null,
					 * DatabaseHelper.ID+" asc");
					 * c.moveToNext();
					 * if(c.getInt(1)==0){
					 * ContentValues values = new
					 * ContentValues();
					 * values.put(DatabaseHelper
					 * .XUANZHONG, 1);//key为字段名，value为值
					 * db
					 * .update(DatabaseHelper.ALARMTABLE
					 * , values, "_id=?", new
					 * String[]{"1"});
					 * xuanzhongBtn1.setBackgroundDrawable
					 * (getResources().getDrawable(R.
					 * drawable
					 * .calendar_xuanzhonghoubg));
					 * /////////////////设置闹钟 } else{
					 * ContentValues values = new
					 * ContentValues();
					 * values.put(DatabaseHelper
					 * .XUANZHONG, 0);//key为字段名，value为值
					 * db
					 * .update(DatabaseHelper.ALARMTABLE
					 * , values, "_id=?", new
					 * String[]{"1"});
					 * xuanzhongBtn1.setBackgroundDrawable
					 * (getResources().getDrawable(R.
					 * drawable
					 * .calendar_xuanzhongqianbg));
					 * ////////////////////取消闹钟 }
					 */

				} else if (v == xuanzhongBtn2) {
					xuanzhongAct(calendar_drug.this, xuanzhongBtn2, "7", 7);
				} else if (v == xuanzhongBtn3) {
					xuanzhongAct(calendar_drug.this, xuanzhongBtn3, "8", 8);
				} else if (v == xuanzhongBtn4) {
					xuanzhongAct(calendar_drug.this, xuanzhongBtn4, "9", 9);
				} else if (v == xuanzhongBtn5) {
					xuanzhongAct(calendar_drug.this, xuanzhongBtn5, "10", 10);
				} else if (v == clockBtn1) {
					timeSet(calendar_drug.this, "6", clockBtn1);

					// 获取时间设置闹钟
					/*
					 * final Calendar
					 * alarm=Calendar.getInstance();
					 * alarm.setTimeInMillis(System.
					 * currentTimeMillis()); int mHour
					 * =
					 * alarm.get(Calendar.HOUR_OF_DAY);
					 * int mMinute =
					 * alarm.get(Calendar.MINUTE); new
					 * TimePickerDialog
					 * (calendar_medical.this, new
					 * TimePickerDialog
					 * .OnTimeSetListener() {
					 * 
					 * @Override public void
					 * onTimeSet(TimePicker view, int
					 * hourOfDay, int minute) { // TODO
					 * Auto-generated method stub
					 * 
					 * 
					 * 
					 * c=db.query(DatabaseHelper.ALARMTABLE
					 * , null, DatabaseHelper.ID+"=1",
					 * null, null, null,
					 * DatabaseHelper.ID+" asc");
					 * c.moveToNext(); ContentValues
					 * values = new ContentValues();
					 * values
					 * .put(DatabaseHelper.HOUR,hourOfDay
					 * );//key为字段名，value为值
					 * values.put(DatabaseHelper
					 * .MINUTE,minute);
					 * db.update(DatabaseHelper
					 * .ALARMTABLE, values, "_id=?",
					 * new String[]{"1"});
					 * clockBtn1.setText
					 * (hourOfDay+":"+minute);
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 * alarm.setTimeInMillis(System.
					 * currentTimeMillis());
					 * //alarm.set(Calendar,3);
					 * alarm.set(Calendar.HOUR_OF_DAY,
					 * hourOfDay);
					 * alarm.set(Calendar.MINUTE,
					 * minute);
					 * alarm.set(Calendar.SECOND, 0);
					 * alarm.set(Calendar.MILLISECOND,
					 * 0); Intent intent = new
					 * Intent(calendar_medical
					 * .this,CallAlarm.class);
					 * PendingIntent sender =
					 * PendingIntent
					 * .getBroadcast(calendar_medical
					 * .this, 1, intent, 0);
					 * AlarmManager am; am =
					 * (AlarmManager
					 * )getSystemService(ALARM_SERVICE
					 * ); am.setRepeating(AlarmManager.
					 * RTC_WAKEUP,
					 * alarm.getTimeInMillis(),
					 * intervalTime, sender); };
					 * 
					 * },mHour,mMinute,true).show();
					 */

				} else if (v == clockBtn2) {
					timeSet(calendar_drug.this, "7", clockBtn2);
				} else if (v == clockBtn3) {
					timeSet(calendar_drug.this, "8", clockBtn3);
				} else if (v == clockBtn4) {
					timeSet(calendar_drug.this, "9", clockBtn4);
				} else if (v == clockBtn5) {
					timeSet(calendar_drug.this, "10", clockBtn5);
				}

				else {
				}
			}
		};
		homeBtn.setOnClickListener(btnClick);
		medical.setOnClickListener(btnClick);
		xuanzhongBtn1.setOnClickListener(btnClick);
		xuanzhongBtn2.setOnClickListener(btnClick);
		xuanzhongBtn3.setOnClickListener(btnClick);
		xuanzhongBtn4.setOnClickListener(btnClick);
		xuanzhongBtn5.setOnClickListener(btnClick);
		clockBtn1.setOnClickListener(btnClick);
		clockBtn2.setOnClickListener(btnClick);
		clockBtn3.setOnClickListener(btnClick);
		clockBtn4.setOnClickListener(btnClick);
		clockBtn5.setOnClickListener(btnClick);

	}

	public static void initialize(Context context, Cursor c,
			Button xuanzhongBtn, EditText medical_edit, Button clockBtn,
			Spinner medical_spinner, final TextView medical_spinnertext,
			final String id) {
		if (c.getInt(1) == 0) {
			xuanzhongBtn.setBackgroundDrawable(context.getResources()
					.getDrawable(R.drawable.calendar_xuanzhongqianbg));
		} else {
			xuanzhongBtn.setBackgroundDrawable(context.getResources()
					.getDrawable(R.drawable.calendar_xuanzhonghoubg));
		}
		medical_edit.setText(c.getString(2));
		final int spinnerid = c.getInt(3);

		clockBtn.setText(format(c.getInt(4)) + ":" + format(c.getInt(5)));

		DatabaseHelper helper = new DatabaseHelper(context,
				DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.Version);
		final SQLiteDatabase db = helper.getWritableDatabase();

		final Cursor c1 = db.query(DatabaseHelper.ALARMZHOUQITABLE, null, null,
				null, null, null, "_id asc", null);// 用户名字查询
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
				android.R.layout.simple_spinner_item, c1,
				new String[] { DatabaseHelper.ZHOUQI },
				new int[] { R.id.text1 });
		adapter.setDropDownViewResource(R.layout.main_spinner_dropdown);

		medical_spinner.setAdapter(adapter);
		medical_spinner.setSelection(spinnerid - 1);

		medical_spinner
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						arg0.setVisibility(View.VISIBLE);
						// spinnerid = arg2+1;
						// Log.v("userid",
						// String.valueOf(userid));
						c1.moveToPosition(arg2);
						Log.v("arg2", String.valueOf(arg2));

						medical_spinnertext.setText(c1.getString(1));
						Cursor cursor = db.query(DatabaseHelper.ALARMTABLE,
								null, DatabaseHelper.ID + "=" + id, null, null,
								null, DatabaseHelper.ID + " asc");
						cursor.moveToNext();
						ContentValues values = new ContentValues();
						values.put(DatabaseHelper.SPINNER, arg2 + 1);// key为字段名，value为值
						db.update(DatabaseHelper.ALARMTABLE, values, "_id=?",
								new String[] { id });
						Log.v("shuju", "shuju");

						// /////////////////////设置闹钟

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method
						// stub

					}

				}

				);

	}

	public static void xuanzhongAct(Context context, Button xuanzhongBtn,
			String id, int alarmid) {
		DatabaseHelper helper = new DatabaseHelper(context,
				DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.Version);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.query(DatabaseHelper.ALARMTABLE, null, DatabaseHelper.ID
				+ "=" + id, null, null, null, DatabaseHelper.ID + " asc");
		c.moveToNext();
		if (c.getInt(1) == 0) {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.XUANZHONG, 1);// key为字段名，value为值
			db.update(DatabaseHelper.ALARMTABLE, values, "_id=?",
					new String[] { id });
			xuanzhongBtn.setBackgroundDrawable(context.getResources()
					.getDrawable(R.drawable.calendar_xuanzhonghoubg));
			// ///////////////设置闹钟
		} else {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.XUANZHONG, 0);// key为字段名，value为值
			db.update(DatabaseHelper.ALARMTABLE, values, "_id=?",
					new String[] { id });
			xuanzhongBtn.setBackgroundDrawable(context.getResources()
					.getDrawable(R.drawable.calendar_xuanzhongqianbg));
			// //////////////////取消闹钟
			Intent intent = new Intent(context, CallAlarm.class);
			PendingIntent sender = PendingIntent.getBroadcast(context, alarmid,
					intent, 0);
			AlarmManager am;
			am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
			am.cancel(sender);
		}
		db.close();
	}

	public static void timeSet(final Context context, final String id,
			final Button clockBtn) {
		// 获取时间设置闹钟
		final Calendar alarm = Calendar.getInstance();
		alarm.setTimeInMillis(System.currentTimeMillis());
		int mHour = alarm.get(Calendar.HOUR_OF_DAY);
		int mMinute = alarm.get(Calendar.MINUTE);
		new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub

				DatabaseHelper helper = new DatabaseHelper(context,
						DatabaseHelper.DATABASE_NAME, null,
						DatabaseHelper.Version);
				SQLiteDatabase db = helper.getWritableDatabase();

				Cursor c = db.query(DatabaseHelper.ALARMTABLE, null,
						DatabaseHelper.ID + "=" + id, null, null, null,
						DatabaseHelper.ID + " asc");
				c.moveToNext();
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.HOUR, hourOfDay);// key为字段名，value为值
				values.put(DatabaseHelper.MINUTE, minute);
				db.update(DatabaseHelper.ALARMTABLE, values, "_id=?",
						new String[] { id });
				clockBtn.setText(format(hourOfDay) + ":" + format(minute));
				db.close();
			}
		}, mHour, mMinute, true).show();
	}

	public static void alarmTimeSet(final Context context, String id,
			int alarmid, EditText medical_edit) {
		// TODO Auto-generated method stub

		DatabaseHelper helper = new DatabaseHelper(context,
				DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.Version);
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor c = db.query(DatabaseHelper.ALARMTABLE, null, DatabaseHelper.ID
				+ "=" + id, null, null, null, DatabaseHelper.ID + " asc");
		c.moveToNext();
		if (c.getInt(1) == 0) {

		} else {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.TEXT, medical_edit.getText().toString());// key为字段名，value为值
			db.update(DatabaseHelper.ALARMTABLE, values, "_id=?",
					new String[] { id });
			db.close();
			db = helper.getWritableDatabase();
			c = db.query(DatabaseHelper.ALARMTABLE, null, DatabaseHelper.ID
					+ "=" + id, null, null, null, DatabaseHelper.ID + " asc");
			c.moveToNext();

			int intervalTime = 86400000;
			int intervalweek = 86400000 * 7;
			Calendar alarm = Calendar.getInstance();
			Calendar currentTime = Calendar.getInstance();
			currentTime.setTimeInMillis(System.currentTimeMillis());

			// alarm.setTimeInMillis(System.currentTimeMillis());
			Log.v("week", String.valueOf(alarm.get(Calendar.DAY_OF_WEEK)));

			alarm.set(Calendar.HOUR_OF_DAY, c.getInt(4));
			alarm.set(Calendar.MINUTE, c.getInt(5));
			alarm.set(Calendar.SECOND, 0);
			alarm.set(Calendar.MILLISECOND, 0);
			Intent intent = new Intent(context, CallAlarm.class);
			intent.putExtra("text", c.getString(2));
			intent.putExtra("tishi",
					context.getString(R.string.calendar_drug_tishi));
			PendingIntent sender = PendingIntent.getBroadcast(context, alarmid,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am;
			am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
			if (c.getInt(3) != 1 && c.getInt(3) != 8) {
				alarm.set(Calendar.DAY_OF_WEEK, c.getInt(3));
				if (currentTime.get(Calendar.DAY_OF_WEEK) > c.getInt(3)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else if (currentTime.get(Calendar.DAY_OF_WEEK) == c.getInt(3)
						&& currentTime.get(Calendar.HOUR_OF_DAY) > c.getInt(4)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else if (currentTime.get(Calendar.DAY_OF_WEEK) == c.getInt(3)
						&& currentTime.get(Calendar.HOUR_OF_DAY) == c.getInt(4)
						&& currentTime.get(Calendar.MINUTE) > c.getInt(5)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis(), intervalweek, sender);
				}

			} else if (c.getInt(3) == 1) {
				if (currentTime.get(Calendar.HOUR_OF_DAY) > c.getInt(4)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalTime,
							intervalTime, sender);
				} else if (currentTime.get(Calendar.HOUR_OF_DAY) == c.getInt(4)
						&& currentTime.get(Calendar.MINUTE) > c.getInt(5)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalTime,
							intervalTime, sender);
				} else {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis(), intervalTime, sender);
				}

			} else {
				alarm.set(Calendar.DAY_OF_WEEK, 1);
				if (currentTime.get(Calendar.DAY_OF_WEEK) > 1) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else if (currentTime.get(Calendar.DAY_OF_WEEK) == 1
						&& currentTime.get(Calendar.HOUR_OF_DAY) > c.getInt(4)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else if (currentTime.get(Calendar.DAY_OF_WEEK) == 1
						&& currentTime.get(Calendar.HOUR_OF_DAY) == c.getInt(4)
						&& currentTime.get(Calendar.MINUTE) > c.getInt(5)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis(), intervalweek, sender);
				}

			}

		}
		db.close();
	}

	private static String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}

	protected void onResume() {
		super.onResume();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			alarmTimeSet(calendar_drug.this, "6", 6, drug_edit1);
			alarmTimeSet(calendar_drug.this, "7", 7, drug_edit2);
			alarmTimeSet(calendar_drug.this, "8", 8, drug_edit3);
			alarmTimeSet(calendar_drug.this, "9", 9, drug_edit4);
			alarmTimeSet(calendar_drug.this, "10", 10, drug_edit5);
			// test_zhifang1.this.setResult(RESULT_OK);
			/*
			 * c=db.query(DatabaseHelper.ALARMTABLE,
			 * null, DatabaseHelper.ID+"=1", null,
			 * null, null, DatabaseHelper.ID+" asc");
			 * c.moveToNext(); ContentValues values =
			 * new ContentValues();
			 * values.put(DatabaseHelper.TEXT,
			 * medical_edit1
			 * .getText().toString());//key为字段名，value为值
			 * db.update(DatabaseHelper.ALARMTABLE,
			 * values, "_id=?", new String[]{"1"});
			 */
			calendar_drug.this.setResult(RESULT_OK);
			calendar_drug.this.finish();

			// /////////////////////保存edittext内容

			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

}
