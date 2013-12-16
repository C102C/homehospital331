package cn.younext;

import java.io.ByteArrayInputStream;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.TextView;

public class myhealth_healthreport extends Activity {

	Button homeBtn;
	Button returnBtn;
	Button detail;
	OnClickListener btnClick;

	TextView user;
	int userid;
	String username;

	TextView text_name;
	TextView text_age;
	TextView text_sex;

	ImageView manicon;

	Cursor c;
	DatabaseHelper helper;
	SQLiteDatabase db;

	TextView xueyaText;
	TextView mailvText;
	TextView xuetangText;
	TextView zhifanglvText;
	TextView tiwenText;
	TextView taixinText;
	TextView tizhongText;
	TextView xueyangText;
	TextView expertText;

	Button xueyabtn;
	Button mailvbtn;
	Button xuetangbtn;
	Button zhifanglvbtn;
	Button tiwenbtn;
	Button taixinbtn;
	Button tizhongbtn;
	Button xueyangbtn;

	String gaoya;
	String diya;
	String mailv;
	String xuetang;
	String zhifanglv;
	String tiwen;
	String taixin;
	String tizhong;
	String xueyang;
	String expert_xueya;
	String expert_mailv;
	String expert_xuetang;
	String expert_zhifanglv;
	String expert_tiwen;
	String expert_taixin;
	String expert_tizhong;
	String expert_xueyang;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.myhealth_healthreport);
		// DigitalClock
		// mDigitalClock=(DigitalClock)findViewById(R.id.myhealth_healthreport_digitalclock);

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			userid = extra.getInt("userid");
			username = extra.getString("username");
			// Toast.makeText(myhealth.this,username,
			// Toast.LENGTH_LONG).show();
			// Log.v("userid_Myhealth",
			// String.valueOf(userid));
			// Log.v("username_myhealth", username);
			gaoya = extra.getString("gaoya");
			diya = extra.getString("diya");
			mailv = extra.getString("mailv");
			xuetang = extra.getString("xuetang");
			zhifanglv = extra.getString("zhifanglv");
			tiwen = extra.getString("tiwen");
			taixin = extra.getString("taixin");
			tizhong = extra.getString("tizhong");
			xueyang = extra.getString("xueyang");
			expert_xueya = extra.getString("expert_xueya");
			expert_mailv = extra.getString("expert_mailv");
			expert_xuetang = extra.getString("expert_xuetang");
			expert_zhifanglv = extra.getString("expert_zhifanglv");
			expert_tiwen = extra.getString("expert_tiwen");
			expert_taixin = extra.getString("expert_taixin");
			expert_tizhong = extra.getString("expert_tizhong");
			expert_xueyang = extra.getString("expert_xueyang");
		}

		user = (TextView) findViewById(R.id.myhealth_healthreport_user);
		user.setText(getString(R.string.myhealth_Welcome) + username);

		xueyaText = (TextView) findViewById(R.id.myhealth_healthreport_BPresult);
		mailvText = (TextView) findViewById(R.id.myhealth_healthreport_PRresult);
		xuetangText = (TextView) findViewById(R.id.myhealth_healthreport_XTresult);
		zhifanglvText = (TextView) findViewById(R.id.myhealth_healthreport_ZFLresult);
		tiwenText = (TextView) findViewById(R.id.myhealth_healthreport_TWresult);
		taixinText = (TextView) findViewById(R.id.myhealth_healthreport_TXresult);
		tizhongText = (TextView) findViewById(R.id.myhealth_healthreport_Wresult);
		xueyangText = (TextView) findViewById(R.id.myhealth_healthreport_BOresult);
		expertText = (TextView) findViewById(R.id.myhealth_healthreport_expertsuggestcontent);

		xueyabtn = (Button) findViewById(R.id.myhealth_healthreport_bloodpressure);
		mailvbtn = (Button) findViewById(R.id.myhealth_healthreport_pulserate);
		xuetangbtn = (Button) findViewById(R.id.myhealth_healthreport_xuetang);
		zhifanglvbtn = (Button) findViewById(R.id.myhealth_healthreport_zhifanglv);
		tiwenbtn = (Button) findViewById(R.id.myhealth_healthreport_tiwen);
		taixinbtn = (Button) findViewById(R.id.myhealth_healthreport_taixin);
		tizhongbtn = (Button) findViewById(R.id.myhealth_healthreport_weight);
		xueyangbtn = (Button) findViewById(R.id.myhealth_healthreport_bloodoxygen);

		manicon = (ImageView) findViewById(R.id.history_report_manicon);

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

		if (gaoya != "" && diya != "") {
			xueyaText.setText(getString(R.string.myhealth_healthreport_gaoya)
					+ String.valueOf(gaoya) + "\n"
					+ getString(R.string.myhealth_healthreport_diya)
					+ String.valueOf(diya));
		} else {
			xueyaText.setText("您这一天没有测量血压");
		}
		if (mailv != "") {
			mailvText.setText(mailv);
		} else {
			mailvText.setText("您这一天没有测量脉率");
		}
		if (xuetang != "") {
			xuetangText.setText(xuetang);
		} else {
			xuetangText.setText("您这一天没有测量脉率");
		}
		if (zhifanglv != "") {
			zhifanglvText.setText(zhifanglv);
		} else {
			zhifanglvText.setText("您这一天没有测量脉率");
		}
		if (tiwen != "") {
			tiwenText.setText(tiwen);
		} else {
			tiwenText.setText("您这一天没有测量脉率");
		}
		if (taixin != "") {
			taixinText.setText(taixin);
		} else {
			taixinText.setText("您这一天没有测量脉率");
		}
		if (tizhong != "") {
			tizhongText.setText(tizhong);
		} else {
			tizhongText.setText("您这一天没有测量脉率");
		}
		if (xueyang != "") {
			xueyangText.setText(xueyang);
		} else {
			xueyangText.setText("您这一天没有测量脉率");
		}

		homeBtn = (Button) findViewById(R.id.healthreport_homebutton);
		returnBtn = (Button) findViewById(R.id.healthreport_returnbutton);
		// detail =
		// (Button)findViewById(R.id.historyBtn);

		btnClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				if (v == homeBtn) {
					// Intent i = new Intent();
					myhealth_healthreport.this.setResult(RESULT_OK);
					myhealth_healthreport.this.finish();
				} else if (v == returnBtn) {
					myhealth_healthreport.this.finish();
				}
				/*
				 * else if(v==detail){
				 * i.setClass(myhealth_healthreport
				 * .this, history_report.class);
				 * i.putExtra("userid", userid);
				 * i.putExtra("username", username);
				 * startActivityForResult(i,1); }
				 */
				else if (v == xueyabtn) {
					if (expert_xueya != null) {
						expertText.setText(expert_xueya);
					} else {
						expertText
								.setText(getString(R.string.myhealth_healthreport_expertempty));
					}
				} else if (v == mailvbtn) {
					if (expert_mailv != null) {
						expertText.setText(expert_mailv);
					} else {
						expertText
								.setText(getString(R.string.myhealth_healthreport_expertempty));

					}
				} else if (v == xuetangbtn) {
					if (expert_xuetang != null) {
						expertText.setText(expert_xuetang);
					} else {
						expertText
								.setText(getString(R.string.myhealth_healthreport_expertempty));
					}
				} else if (v == zhifanglvbtn) {
					if (expert_zhifanglv != null) {
						expertText.setText(expert_zhifanglv);
					} else {
						expertText
								.setText(getString(R.string.myhealth_healthreport_expertempty));
					}
				} else if (v == tiwenbtn) {
					if (expert_tiwen != null) {
						expertText.setText(expert_tiwen);
					} else {
						expertText
								.setText(getString(R.string.myhealth_healthreport_expertempty));
					}
				} else if (v == taixinbtn) {
					if (expert_taixin != null) {
						expertText.setText(expert_taixin);
					} else {
						expertText
								.setText(getString(R.string.myhealth_healthreport_expertempty));
					}
				} else if (v == tizhongbtn) {
					if (expert_tizhong != null) {
						expertText.setText(expert_tizhong);
					} else {
						expertText
								.setText(getString(R.string.myhealth_healthreport_expertempty));
					}
				} else if (v == xueyangbtn) {
					if (expert_xueyang != null) {
						expertText.setText(expert_xueyang);
					} else {
						expertText
								.setText(getString(R.string.myhealth_healthreport_expertempty));
					}
				} else {

				}
			}
		};
		homeBtn.setOnClickListener(btnClick);
		returnBtn.setOnClickListener(btnClick);
		xueyabtn.setOnClickListener(btnClick);
		mailvbtn.setOnClickListener(btnClick);
		xuetangbtn.setOnClickListener(btnClick);
		zhifanglvbtn.setOnClickListener(btnClick);
		tiwenbtn.setOnClickListener(btnClick);
		taixinbtn.setOnClickListener(btnClick);
		tizhongbtn.setOnClickListener(btnClick);
		xueyangbtn.setOnClickListener(btnClick);

		// detail.setOnClickListener(btnClick);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				myhealth_healthreport.this.setResult(RESULT_OK);
				myhealth_healthreport.this.finish();

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

			// Intent i = new Intent();
			myhealth_healthreport.this.setResult(RESULT_OK);
			myhealth_healthreport.this.finish();

			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

}
