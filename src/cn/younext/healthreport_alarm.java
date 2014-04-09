package cn.younext;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class healthreport_alarm extends BroadcastReceiver {
	DatabaseHelper helper;
	SQLiteDatabase db;
	Cursor c;
	int myear;
	int mmonth;
	int mday;
	int yestoday_year;
	int yestoday_month;
	int yestoday_day;
	int userid;
	String username;
	String cardNum;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		helper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME,
				null, DatabaseHelper.Version);
		db = helper.getWritableDatabase();// 打开数据库
		c = db.query(DatabaseHelper.HISTORY_REPORT, null, null, null, null,
				null, "_id desc", null);// 用户名字查询
		c.moveToNext();
		myear = c.getInt(10);
		mmonth = c.getInt(11);
		mday = c.getInt(12);
		db.close();

		Bundle extra = intent.getExtras();
		if (extra != null) {
			userid = extra.getInt("userid");
			username = extra.getString(username);
		}

		helper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME,
				null, DatabaseHelper.Version);
		db = helper.getWritableDatabase();// 打开数据库
		c = db.query(DatabaseHelper.USER_MANAGE, null, DatabaseHelper.ID + "="
				+ userid, null, null, null, DatabaseHelper.ID + " desc", null);

		c.moveToNext();
		if (c.isAfterLast()) {
			Log.v("last", "last");
			Log.v("userid", String.valueOf(userid));
		}
		cardNum = c.getString(2);
		db.close();

		Log.v("alarm", "outtry");

		final Calendar c = Calendar.getInstance();
		yestoday_year = c.get(Calendar.YEAR); // 获取当前年份
		yestoday_month = c.get(Calendar.MONTH) + 1;// 获取当前月份
		if (yestoday_month == 13) {
			yestoday_month = 1;
		}
		yestoday_day = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码

		if (yestoday_day != 1) {
			yestoday_day = yestoday_day - 1;
		} else if (yestoday_month == 1) {
			yestoday_month = 12;
			yestoday_day = 31;
			yestoday_year = yestoday_year - 1;
		} else if (yestoday_month == 2 || yestoday_month == 4
				|| yestoday_month == 6 || yestoday_month == 8
				|| yestoday_month == 9 || yestoday_month == 11) {
			yestoday_month = yestoday_month - 1;
			yestoday_day = 31;
		} else if (yestoday_month == 5 || yestoday_month == 7
				|| yestoday_month == 10 || yestoday_month == 12) {
			yestoday_month = yestoday_month - 1;
			yestoday_day = 30;
		} else if (yestoday_year % 4 == 0) {
			yestoday_month = yestoday_month - 1;
			yestoday_day = 29;

		} else {
			yestoday_day = 28;
		}

		if (myear == yestoday_year && mmonth == yestoday_month
				&& mday == yestoday_day) {

		} else {

			boolean k = false;			
			helper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME,
					null, DatabaseHelper.Version);
			db = helper.getWritableDatabase();// 打开数据库
			ContentValues values = new ContentValues();
			try {
				Log.v("alarm", "intry");
				// /如果返回false意味卡号没有在数据库里记录
//				if (client.getHealthRecord(cardNum)) {
//					// 用户的卡号
//					if (client.record.bp == null) {
//						values.put(DatabaseHelper.HISTORY_REPORT_GAOYA, "");
//						values.put(DatabaseHelper.HISTORY_REPORT_DIYA, "");
//						values.put(DatabaseHelper.HISTORY_REPORT_EXPERT_XUEYA,
//								"");
//
//					} else {
//						k = true;
//						String[] bp = client.record.bp.split("/"); // 血压是这样120/80
//						values.put(DatabaseHelper.HISTORY_REPORT_GAOYA, bp[0]);
//						bp = bp[1].split(" ");
//						values.put(DatabaseHelper.HISTORY_REPORT_DIYA, bp[0]);
//						values.put(DatabaseHelper.HISTORY_REPORT_EXPERT_XUEYA,
//								client.record.bpAdvice);
//
//					}
//					if (client.record.bo == null) {
//						values.put(DatabaseHelper.HISTORY_REPORT_XUEYANG, "");
//						values.put(
//								DatabaseHelper.HISTORY_REPORT_EXPERT_XUEYANG,
//								"");
//					} else {
//						k = true;
//						String[] bp = client.record.bo.split(" ");
//						values.put(DatabaseHelper.HISTORY_REPORT_XUEYANG, bp[0]);
//						values.put(
//								DatabaseHelper.HISTORY_REPORT_EXPERT_XUEYANG,
//								client.record.boAdvice);
//					}
//					if (client.record.fat == null) {
//						values.put(DatabaseHelper.HISTORY_REPORT_ZHIFANGLV, "");
//						values.put(
//								DatabaseHelper.HISTORY_REPORT_EXPERT_ZHIFANGLV,
//								"");
//					} else {
//						k = true;
//						String[] bp = client.record.fat.split(" ");
//						values.put(DatabaseHelper.HISTORY_REPORT_ZHIFANGLV,
//								bp[0]);
//						values.put(
//								DatabaseHelper.HISTORY_REPORT_EXPERT_ZHIFANGLV,
//								client.record.fatAdvice);
//
//					}
//					if (client.record.glu == null) {
//						values.put(DatabaseHelper.HISTORY_REPORT_XUETANG, "");
//						values.put(
//								DatabaseHelper.HISTORY_REPORT_EXPERT_XUETANG,
//								"");
//					} else {
//						k = true;
//						String[] bp = client.record.glu.split(" ");
//						values.put(DatabaseHelper.HISTORY_REPORT_XUETANG, bp[0]);
//						values.put(
//								DatabaseHelper.HISTORY_REPORT_EXPERT_XUETANG,
//								client.record.gluAdvice);
//					}
//					if (client.record.heart == null) {
//						values.put(DatabaseHelper.HISTORY_REPORT_TAIXIN, "");
//						values.put(DatabaseHelper.HISTORY_REPORT_EXPERT_TAIXIN,
//								"");
//					} else {
//						k = true;
//						String[] bp = client.record.heart.split(" ");
//						values.put(DatabaseHelper.HISTORY_REPORT_TAIXIN, bp[0]);
//						values.put(DatabaseHelper.HISTORY_REPORT_EXPERT_TAIXIN,
//								client.record.heartAdvice);
//					}
//					if (client.record.pulse == null) {
//						values.put(DatabaseHelper.HISTORY_REPORT_MAILV, "");
//						values.put(DatabaseHelper.HISTORY_REPORT_EXPERT_MAILV,
//								"");
//					} else {
//						k = true;
//						String[] bp = client.record.pulse.split(" ");
//						values.put(DatabaseHelper.HISTORY_REPORT_MAILV, bp[0]);
//						values.put(DatabaseHelper.HISTORY_REPORT_EXPERT_MAILV,
//								client.record.pulseAdvice);
//					}
//					if (client.record.temperature == null) {
//						values.put(DatabaseHelper.HISTORY_REPORT_TIWEN, "");
//						values.put(DatabaseHelper.HISTORY_REPORT_EXPERT_TIWEN,
//								"");
//					} else {
//						k = true;
//						String[] bp = client.record.temperature.split(" ");
//						values.put(DatabaseHelper.HISTORY_REPORT_TIWEN, bp[0]);
//						values.put(DatabaseHelper.HISTORY_REPORT_EXPERT_TIWEN,
//								client.record.temperatureAdvice);
//					}
//					if (client.record.weight == null) {
//						values.put(DatabaseHelper.HISTORY_REPORT_TIZHONG, "");
//						values.put(
//								DatabaseHelper.HISTORY_REPORT_EXPERT_TIZHONG,
//								"");
//					} else {
//						k = true;
//						String[] bp = client.record.weight.split(" ");
//						values.put(DatabaseHelper.HISTORY_REPORT_TIZHONG, bp[0]);
//						values.put(
//								DatabaseHelper.HISTORY_REPORT_EXPERT_TIZHONG,
//								client.record.weightAdvice);
//					}
//					values.put(DatabaseHelper.YEAR, yestoday_year);
//					values.put(DatabaseHelper.MONTH, yestoday_month);
//					values.put(DatabaseHelper.DAY, yestoday_day);
//					values.put(DatabaseHelper.HISTORY_REPORT_USERID, userid);
//					values.put(DatabaseHelper.HISTORY_REPORT_TAG, 0);
//					if (k == true) {
//						db.insert(DatabaseHelper.HISTORY_REPORT, null, values);
//
//						Intent i = new Intent(context,
//								healthreport_alarmalert.class);
//						Log.v("alarmalert", "ktrue");
//						Bundle bundleRet = new Bundle();
//						bundleRet.putString("STR_CALLER", "");
//						i.putExtras(bundleRet);
//						i.putExtra("text", "您有新的健康报告了！");
//						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						context.startActivity(i);
//					}
//
//				}

				

			} catch (Exception e) {
			}

			db.close();

		}

	}

}
