package com.health.archive.baby.oneold;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.younext.R;

import com.health.archive.baby.BabyHomeVistit;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.T;

public class OneOldChilldVistit extends Activity {
	public static final int REQUEST_FRESH = 0x0010;
	// 行号的id，为-1表示来自于新建
	final static String SYS_ID = "sys_id";
	private static final String NEW = "-1";
	private String sysId = NEW;
	private DatabaseService dbService;
	// 更新界面标志
	private static final int FRESH_UI = 0x10;
	// 确认按钮
	private static final int POSITIVE = 0x11;
	// 保存成功
	private static final int SAVE_OK = 0x12;
	// 保存失败
	private static final int SAVE_ERROE = 0x13;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_UI:
				initView();
				break;
			case POSITIVE:
				// saveToDb();// 保存到数据库

				break;
			case SAVE_OK:
				T.showShort(OneOldChilldVistit.this, "保存成功");
				setState(true);
				finish();
				break;
			case SAVE_ERROE:
				// T.showShort(getActivity(), "保存失败");
				break;
			default:
				break;
			}
		}

	};
	private boolean lock = false;
	private Button editHelpBtn;
	private Button saveBtn;
	private View bodySv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 来自于查看还是新建,0表示新建,1表示查看
		sysId = getIntent().getStringExtra(SYS_ID);
		setContentView(R.layout.health_archivel_oneold_visit);
		dbService = new DatabaseService(this);
		//initView();
	}

	private void initView() {

		setChoiceEditText();
		editHelpBtn = (Button) findViewById(R.id.edit_help_button);
		saveBtn = (Button) findViewById(R.id.baby_title_btn);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickButton(v);
			}
		});
		bodySv = findViewById(R.id.table_body_sv);
		setState(!NEW.equals(sysId));// 新建时，进来不锁定，查看时进来默认锁定
		setText(R.id.serial_id, Tables.getSerialId());
		if (!NEW.equals(sysId))
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					initFromDb();
				}

			}, 500);

	}

	private void setChoiceEditText() {
		// TODO Auto-generated method stub
		
	}

	protected void onClickButton(View v) {
		// TODO Auto-generated method stub
		
	}

	protected void initFromDb() {
		// TODO Auto-generated method stub
		
	}

	private void setText(int serialId, String serialId2) {
		// TODO Auto-generated method stub
		
	}

	private void setState(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
