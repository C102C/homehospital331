package cn.younext;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.health.BaseActivity;

public class healthinformation extends BaseActivity {
	Button homeBtn;
	Button changshiBtn;
	Button bangbangBtn;
	OnClickListener btnClick;
	String text;
	ListView list;

	TextView user;
	int userid;
	String username;
	TextView textcontent;

	Cursor c;
	DatabaseHelper helper;
	SQLiteDatabase db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.healthinfo);

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			userid = extra.getInt("userid");
			username = extra.getString("username");
			username = username == null ? "" : username;
			// Toast.makeText(myhealth.this,username,
			// Toast.LENGTH_LONG).show();
			// Log.v("userid_Myhealth",
			// String.valueOf(userid));
			// Log.v("username_myhealth", username);
		}

		user = (TextView) findViewById(R.id.healthinformation_user);
		user.setText(getString(R.string.myhealth_Welcome) + username);

		homeBtn = (Button) findViewById(R.id.healthinfo_homebutton);
		changshiBtn = (Button) findViewById(R.id.healthinfo_changshiBtn);
		bangbangBtn = (Button) findViewById(R.id.healthinfo_bangbangBtn);
		list = (ListView) findViewById(R.id.healthinfo_list);
		// textcontent=(TextView)findViewById(R.id.healthinfo_list_textcontent);
		btnClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == homeBtn)
					healthinformation.this.finish();
				else if (v == changshiBtn) {
					// textview.setText(R.string.healthinfo_FoodContent);
					helper = new DatabaseHelper(healthinformation.this,
							DatabaseHelper.DATABASE_NAME, null,
							DatabaseHelper.Version);
					db = helper.getWritableDatabase();
					c = db.query(DatabaseHelper.HEALTHINFO_CHANGSHITABLE, null,
							null, null, null, null, "_id desc", null);// 机场名字查询
					// c.moveToNext();
					// Log.v("before executing",c.getString(3));
					ListAdapter adapter = new SimpleCursorAdapter(
							healthinformation.this,
							R.layout.healthinfo_list,
							c,
							new String[] {
									DatabaseHelper.HEALTHINFOTABLE_TEXTTITLE,
									DatabaseHelper.HEALTHINFOTABLE_TEXTCONTENT },
							new int[] { R.id.healthinfo_list_texttitle,
									R.id.healthinfo_list_textcontent });

					list.setAdapter(adapter);

				} else if (v == bangbangBtn) {
					// textview.setText(R.string.healthinfo_FoodContent);
					helper = new DatabaseHelper(healthinformation.this,
							DatabaseHelper.DATABASE_NAME, null,
							DatabaseHelper.Version);
					db = helper.getWritableDatabase();
					c = db.query(DatabaseHelper.HEALTHINFO_BANGBANGTABLE, null,
							null, null, null, null, "_id desc", null);// 机场名字查询
					// c.moveToNext();
					// Log.v("before executing",c.getString(3));
					ListAdapter adapter = new SimpleCursorAdapter(
							healthinformation.this,
							R.layout.healthinfo_list,
							c,
							new String[] {
									DatabaseHelper.HEALTHINFOTABLE_TEXTTITLE,
									DatabaseHelper.HEALTHINFOTABLE_TEXTCONTENT },
							new int[] { R.id.healthinfo_list_texttitle,
									R.id.healthinfo_list_textcontent });

					list.setAdapter(adapter);

				}

				else {
				}
			}

		};
		homeBtn.setOnClickListener(btnClick);
		changshiBtn.setOnClickListener(btnClick);
		bangbangBtn.setOnClickListener(btnClick);

	}

	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			healthinformation.this.finish();

			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

}
