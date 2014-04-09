package com.health.myhealth;

import java.io.ByteArrayInputStream;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.younext.DatabaseHelper;
import cn.younext.R;
import cn.younext.myhealth_healthreport;

import com.health.BaseActivity;

public class Historyreport extends BaseActivity {
	Button homeBtn;
	Button returnBtn;
	OnClickListener btnClick;
	OnClickListener linearlayout;
	ListView list;

	TextView user;
	int userid;
	String username;

	Cursor c;
	DatabaseHelper helper;
	SQLiteDatabase db;

	LinearLayout linear1;
	LinearLayout linear2;
	LinearLayout linear3;
	LinearLayout linear4;
	LinearLayout linear5;
	LinearLayout linear6;
	LinearLayout linear7;
	LinearLayout linear8;
	ImageView icon1;
	ImageView icon2;
	ImageView icon3;
	ImageView icon4;
	ImageView icon5;
	ImageView icon6;
	ImageView icon7;
	ImageView icon8;

	ImageView manicon;

	TextView text1;
	TextView text2;
	TextView text3;
	TextView text4;
	TextView text5;
	TextView text6;
	TextView text7;
	TextView text8;

	TextView text_name;
	TextView text_age;
	TextView text_sex;

	int id1 = 0;
	int id2 = 0;
	int id3 = 0;
	int id4 = 0;
	int id5 = 0;
	int id6 = 0;
	int id7 = 0;
	int id8 = 0;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_report);
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			userid = extra.getInt("userid");
			username = extra.getString("username");
			// Toast.makeText(myhealth.this,username,
			// Toast.LENGTH_LONG).show();
			// Log.v("userid_Myhealth",
			// String.valueOf(userid));
			// Log.v("username_myhealth", username);
		}

		user = (TextView) findViewById(R.id.detail_report_user);
		user.setText(getString(R.string.myhealth_Welcome) + username);

		homeBtn = (Button) findViewById(R.id.detailreport_homebutton);
		returnBtn = (Button) findViewById(R.id.detailreport_returnbutton);

		linear1 = (LinearLayout) findViewById(R.id.history_linearlayout1);
		linear2 = (LinearLayout) findViewById(R.id.history_linearlayout2);
		linear3 = (LinearLayout) findViewById(R.id.history_linearlayout3);
		linear4 = (LinearLayout) findViewById(R.id.history_linearlayout4);
		linear5 = (LinearLayout) findViewById(R.id.history_linearlayout5);
		linear6 = (LinearLayout) findViewById(R.id.history_linearlayout6);
		linear7 = (LinearLayout) findViewById(R.id.history_linearlayout7);
		linear8 = (LinearLayout) findViewById(R.id.history_linearlayout8);

		icon1 = (ImageView) findViewById(R.id.history_icon1);
		icon2 = (ImageView) findViewById(R.id.history_icon2);
		icon3 = (ImageView) findViewById(R.id.history_icon3);
		icon4 = (ImageView) findViewById(R.id.history_icon4);
		icon5 = (ImageView) findViewById(R.id.history_icon5);
		icon6 = (ImageView) findViewById(R.id.history_icon6);
		icon7 = (ImageView) findViewById(R.id.history_icon7);
		icon8 = (ImageView) findViewById(R.id.history_icon8);

		manicon = (ImageView) findViewById(R.id.history_report_manicon);

		text1 = (TextView) findViewById(R.id.history_text1);
		text2 = (TextView) findViewById(R.id.history_text2);
		text3 = (TextView) findViewById(R.id.history_text3);
		text4 = (TextView) findViewById(R.id.history_text4);
		text5 = (TextView) findViewById(R.id.history_text5);
		text6 = (TextView) findViewById(R.id.history_text6);
		text7 = (TextView) findViewById(R.id.history_text7);
		text8 = (TextView) findViewById(R.id.history_text8);

		text_name = (TextView) findViewById(R.id.myhealth_healthreport_name);
		text_age = (TextView) findViewById(R.id.myhealth_healthreport_age);
		text_sex = (TextView) findViewById(R.id.myhealth_healthreport_sex);

		helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null,
				DatabaseHelper.Version);
		db = helper.getWritableDatabase();// 打开数据库
		c = db.query(DatabaseHelper.USER_MANAGE, null, DatabaseHelper.ID + "="
				+ userid, null, null, null, DatabaseHelper.ID + " desc", null);
		c.moveToNext();
		byte[] buffer_img = c.getBlob(5);
		ByteArrayInputStream buf = new ByteArrayInputStream(buffer_img);
		manicon.setImageDrawable(Drawable.createFromStream(buf, "img"));
		text_name.setText(getString(R.string.myhealth_healthreport_Name)
				+ c.getString(1));
		text_age.setText(getString(R.string.myhealth_healthreport_Age)
				+ c.getString(3));
		text_sex.setText(getString(R.string.myhealth_healthreport_Sex)
				+ c.getString(4));
		// list
		// =(ListView)findViewById(R.id.history_reportview);

		Log.v("inqian", "in11");
		db.close();

		/*
		 * String
		 * uriAPI="http://219.223.239.143/HealthInfo.aspx"
		 * ; HttpPost httpRequest = new
		 * HttpPost(uriAPI); try{
		 * httpRequest.setEntity(null); HttpResponse
		 * httpResponse = new
		 * DefaultHttpClient().execute(httpRequest);
		 * if(
		 * httpResponse.getStatusLine().getStatusCode
		 * ()==200){ String strResult =
		 * EntityUtils.toString
		 * (httpResponse.getEntity()); Log.v("wangye",
		 * strResult); // TextView test =
		 * (TextView)findViewById(R.id.testtesttest);
		 * // test.setText(strResult);
		 * ///////////////////////////解析数据存到数据库
		 * 
		 * 
		 * 
		 * 
		 * } else{ Log.v("200","wrong"); } }
		 * catch(Exception e){
		 * 
		 * }
		 */

		Log.v("inqian", "in11");

		helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null,
				DatabaseHelper.Version);
		db = helper.getWritableDatabase();// 打开数据库
		c = db.query(DatabaseHelper.HISTORY_REPORT, null,
				DatabaseHelper.USER_ID + "=" + userid, null, null, null,
				DatabaseHelper.ID + " desc", null);// 查询银行名字
		Log.v("inqian", "in11");
		c.moveToNext();
		if (!c.isAfterLast()) {

			id1 = initialize(this, c, icon1, text1);
			c.moveToNext();
			Log.v("in11", "in11");
		}
		/*
		 * if(c.getInt(14)==0){
		 * icon1.setImageDrawable(getResources
		 * ().getDrawable
		 * (R.drawable.historyreport_weikaiqi)); } else
		 * if(c.getInt(14)==1){
		 * icon1.setImageDrawable(getResources
		 * ().getDrawable
		 * (R.drawable.historyreport_kaiqi)); } else{
		 * 
		 * } id1=c.getInt(0);
		 * 
		 * text1.setText(String.valueOf(c.getInt(11))+"-"
		 * +
		 * String.valueOf(c.getInt(12))+"-"+String.valueOf
		 * (c.getInt(13))); db.close();
		 */
		if (!c.isAfterLast()) {
			id2 = initialize(this, c, icon2, text2);
			c.moveToNext();
		}
		if (!c.isAfterLast()) {
			id3 = initialize(this, c, icon3, text3);
			c.moveToNext();
		}
		if (!c.isAfterLast()) {
			id4 = initialize(this, c, icon4, text4);
			c.moveToNext();
		}
		if (!c.isAfterLast()) {
			id5 = initialize(this, c, icon5, text5);
			c.moveToNext();
		}
		if (!c.isAfterLast()) {
			id6 = initialize(this, c, icon6, text6);
			c.moveToNext();
		}
		if (!c.isAfterLast()) {
			id7 = initialize(this, c, icon7, text7);
			c.moveToNext();
		}
		if (!c.isAfterLast()) {
			id8 = initialize(this, c, icon8, text8);
		}
		for (c.moveToNext(); !c.isAfterLast(); c.moveToNext()) {
			db.delete("xueya", "_id=" + c.getInt(0), null);
		}
		Log.v("ini", "ini");
		db.close();

		linearlayout = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v == linear1) {

					if (id1 != 0) {
						setlinear(Historyreport.this, id1, icon1);

					} else {

					}
				}

				else if (v == linear2) {
					if (id2 != 0)
						setlinear(Historyreport.this, id2, icon2);

				} else if (v == linear3) {
					if (id3 != 0)
						setlinear(Historyreport.this, id2, icon3);

				} else if (v == linear4) {
					if (id4 != 0)
						setlinear(Historyreport.this, id2, icon4);

				} else if (v == linear5) {
					if (id5 != 0)
						setlinear(Historyreport.this, id2, icon5);

				} else if (v == linear6) {
					if (id6 != 0)
						setlinear(Historyreport.this, id2, icon6);

				} else if (v == linear7) {
					if (id7 != 0)
						setlinear(Historyreport.this, id2, icon7);

				} else if (v == linear8) {
					if (id8 != 0)
						setlinear(Historyreport.this, id2, icon8);

				} else {

				}

			}

		};
		linear1.setOnClickListener(linearlayout);
		linear2.setOnClickListener(linearlayout);
		linear3.setOnClickListener(linearlayout);
		linear4.setOnClickListener(linearlayout);
		linear5.setOnClickListener(linearlayout);
		linear6.setOnClickListener(linearlayout);
		linear7.setOnClickListener(linearlayout);
		linear8.setOnClickListener(linearlayout);

		db.close();

		btnClick = new OnClickListener() {
			public void onClick(View v) {
				if (v == returnBtn) {

					Historyreport.this.finish();

				} else if (v == homeBtn) {
					Historyreport.this.setResult(RESULT_OK);
					Historyreport.this.finish();
				}
			}
		};
		homeBtn.setOnClickListener(btnClick);
		returnBtn.setOnClickListener(btnClick);
	}

	public static int initialize(Context context, Cursor c, ImageView icon,
			TextView text) {

		if (c.getInt(13) == 0) {
			icon.setImageDrawable(context.getResources().getDrawable(
					R.drawable.historyreport_weikaiqi));
		} else if (c.getInt(13) == 1) {
			icon.setImageDrawable(context.getResources().getDrawable(
					R.drawable.historyreport_kaiqi));
		} else {

		}
		int id = c.getInt(0);

		text.setText(String.valueOf(c.getInt(10)) + "-"
				+ String.valueOf(c.getInt(11)) + "-"
				+ String.valueOf(c.getInt(12)));
		return id;
	}

	public void setlinear(Context context, int id, ImageView icon) {
		DatabaseHelper helper = new DatabaseHelper(context,
				DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.Version);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.query(DatabaseHelper.HISTORY_REPORT, null,
				DatabaseHelper.ID + "=" + id, null, null, null,
				DatabaseHelper.ID + " desc", null);// 查询银行名字
		c.moveToFirst();
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.HISTORY_REPORT_TAG, 1);
		db.update(DatabaseHelper.HISTORY_REPORT, values, "_id=?",
				new String[] { String.valueOf(id) });

		icon.setImageDrawable(context.getResources().getDrawable(
				R.drawable.historyreport_kaiqi));
		Intent i = new Intent();
		i.setClass(Historyreport.this, myhealth_healthreport.class);
		i.putExtra("userid", userid);
		i.putExtra("username", username);
		// i.putExtra("userneme", username);
		i.putExtra("gaoya", c.getString(1));
		i.putExtra("diya", c.getString(2));
		i.putExtra("mailv", c.getString(3));
		i.putExtra("xuetang", c.getString(4));
		i.putExtra("zhifanglv", c.getString(5));
		i.putExtra("tiwen", c.getString(6));
		i.putExtra("taixin", c.getString(7));
		i.putExtra("tizhong", c.getString(8));
		i.putExtra("xueyang", c.getString(9));
		i.putExtra("expert_xueya", c.getString(15));
		i.putExtra("expert_mailv", c.getString(16));
		i.putExtra("expert_xuetang", c.getString(17));
		i.putExtra("expert_zhifanglv", c.getString(18));
		i.putExtra("expert_tiwen", c.getString(19));
		i.putExtra("expert_taixin", c.getString(20));
		i.putExtra("expert_tizhong", c.getString(21));
		i.putExtra("expert_xueyang", c.getString(22));

		db.close();
		startActivityForResult(i, 1);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				Historyreport.this.setResult(RESULT_OK);
				Historyreport.this.finish();

			}
			break;
		default:
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

			Historyreport.this.setResult(RESULT_OK);
			Historyreport.this.finish();

			return true;

		}
		return super.onKeyDown(keyCode, event);
	}
}