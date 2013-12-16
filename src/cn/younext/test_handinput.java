package cn.younext;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.health.BaseActivity;

public class test_handinput extends BaseActivity {

	Button homeBtn;

	Button xueyaBtn;
	Button mailvBtn;
	Button zhifangBtn;
	Button xuetangBtn;
	Button tiwenBtn;
	Button taixinBtn;
	Button tizhongBtn;
	Button xueyangBtn;

	EditText gaoyaEdit;
	EditText diyaEdit;
	EditText mailvEdit;
	EditText zhifangEdit;
	EditText xuetangEdit;
	EditText tiwenEdit;
	EditText taixinEdit;
	EditText tizhongEdit;
	EditText xueyangEdit;

	Button handinput;
	Button returnBtn;
	Button machineinput;
	OnClickListener btnClick;

	Cursor cursor;
	DatabaseHelper helper;
	SQLiteDatabase db;

	private int mHour;
	private int mMinute;
	private int mYear;
	private int mMonth;
	private int mDay;

	TextView user;
	int userid;
	String username;
	String cardNum;	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.test_handinput);

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			userid = extra.getInt("userid");
			username = extra.getString("username");

		}

		user = (TextView) findViewById(R.id.test_handinput_user);
		user.setText(getString(R.string.myhealth_Welcome) + username);

		xueyaBtn = (Button) findViewById(R.id.test_handinput_BPBtn);
		mailvBtn = (Button) findViewById(R.id.test_handinput_HRBtn);
		zhifangBtn = (Button) findViewById(R.id.test_handinput_ZFLBtn);
		xuetangBtn = (Button) findViewById(R.id.test_handinput_XTBtn);
		tiwenBtn = (Button) findViewById(R.id.test_handinput_TWBtn);
		taixinBtn = (Button) findViewById(R.id.test_handinput_TXBtn);
		tizhongBtn = (Button) findViewById(R.id.test_handinput_TZBtn);
		xueyangBtn = (Button) findViewById(R.id.test_handinput_XYBtn);

		gaoyaEdit = (EditText) findViewById(R.id.test_handinput_HBGedittext);
		diyaEdit = (EditText) findViewById(R.id.test_handinput_LBGedittext);
		mailvEdit = (EditText) findViewById(R.id.test_handinput_HRedittext);
		zhifangEdit = (EditText) findViewById(R.id.test_handinput_ZFLedittext);
		xuetangEdit = (EditText) findViewById(R.id.test_handinput_XTedittext);
		tiwenEdit = (EditText) findViewById(R.id.test_handinput_TWedittext);
		taixinEdit = (EditText) findViewById(R.id.test_handinput_TXedittext);
		tizhongEdit = (EditText) findViewById(R.id.test_handinput_TZedittext);
		xueyangEdit = (EditText) findViewById(R.id.test_handinput_XYedittext);

		homeBtn = (Button) findViewById(R.id.test_handinput_homeBtn);
		returnBtn = (Button) findViewById(R.id.test_handinput_returnBtn);
		machineinput = (Button) findViewById(R.id.test_handinput_machinebtn);

		helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null,
				DatabaseHelper.Version);
		db = helper.getWritableDatabase();// 打开数据库
		cursor = db.query(DatabaseHelper.USER_MANAGE, null, DatabaseHelper.ID
				+ "=" + userid, null, null, null, DatabaseHelper.ID + " desc",
				null);
		cursor.moveToNext();
		cardNum = cursor.getString(2);
		db.close();

		btnClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}};
		homeBtn.setOnClickListener(btnClick);
		returnBtn.setOnClickListener(btnClick);
		machineinput.setOnClickListener(btnClick);

		xueyaBtn.setOnClickListener(btnClick);
		mailvBtn.setOnClickListener(btnClick);
		zhifangBtn.setOnClickListener(btnClick);
		xuetangBtn.setOnClickListener(btnClick);
		tiwenBtn.setOnClickListener(btnClick);
		taixinBtn.setOnClickListener(btnClick);
		tizhongBtn.setOnClickListener(btnClick);
		xueyangBtn.setOnClickListener(btnClick);
		// xueya.setOnClickListener(btnClick);
		// xueyang.setOnClickListener(btnClick);
		// xindian.setOnClickListener(btnClick);
		// taixin.setOnClickListener(btnClick);
		// zhifang.setOnClickListener(btnClick);

	}

	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			test_handinput.this.setResult(RESULT_OK);
			test_handinput.this.finish();

			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

}
