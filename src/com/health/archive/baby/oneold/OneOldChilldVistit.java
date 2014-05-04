package com.health.archive.baby.oneold;

import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.archive.VisitBaseActivity;
import com.health.database.DataOpenHelper;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.T;

public class OneOldChilldVistit extends VisitBaseActivity {
	
	private DatabaseService dbService;
	// 更新界面标志
	private static final int FRESH_UI = 0x10;
	// 保存成功
	private static final int SAVE_OK = 0x12;
	// 保存失败
	private static final int SAVE_ERROE = 0x13;

	private boolean lock = false;
	private Button editHelpBtn;
	private Button saveBtn;
	private View bodySv;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_UI:
				initView();
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 来自于查看还是新建,0表示新建,1表示查看
		sysId = getIntent().getStringExtra(SYS_ID);
		setContentView(R.layout.health_archivel_oneold_visit);
		dbService = new DatabaseService(this);
		initView();
	}

	private void initView() {
		setChoiceEditText();
		setText(R.id.oneold_name,BaseActivity.getUser().getName());
		editHelpBtn = (Button) findViewById(R.id.edit_help_button);
		saveBtn = (Button) findViewById(R.id.oneold_title_btn);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickButton(v);
			}
		});
		bodySv = findViewById(R.id.table_body_sv);
		setState(!NEW.equals(sysId));// 新建时，进来不锁定，查看时进来默认锁定
		setText(R.id.oneold_serial_id, Tables.getSerialId());
		if (!NEW.equals(sysId))
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					initFromDb();
				}

			}, 500);

	}

	private void setChoiceEditText() {
		// 月龄
		setChoiceEditText(R.id.oneold_age, new String[] { "满月", "3月龄", "6月龄",
				"8月龄" }, null);
		// 面色情况
		setChoiceEditText(R.id.oneold_face, new String[] { "1红润", "2黄染" }, "3其他");
		// 皮肤
		setChoiceEditText(R.id.oneold_skin, new String[] { "1未见异常 " }, "2异常");
		// 前囟状况
		setChoiceEditText(R.id.oneold_bregma_state,
				new String[] { "1闭合", "2未闭" }, null);
		// 颈部包块
		setChoiceEditText(R.id.oneold_neck_block, new String[] { "有", "无" },
				null);
		// 眼外观
		setChoiceEditText(R.id.oneold_eye, new String[] { "1未见异常 " }, "2异常");
		// 耳外观
		setChoiceEditText(R.id.oneold_ear, new String[] { "1未见异常 " }, "2异常");// 面色情况
		// 听力
		setChoiceEditText(R.id.oneold_hear, new String[] { "1通过", "2未通过" }, null);
		// 口腔
		setChoiceEditText(R.id.oneold_mouth, new String[] { "1未见异常" }, "2异常");
		// 心肺
		setChoiceEditText(R.id.oneold_heart_hear, new String[] { "1未见异常" },
				"2异常");
		// 腹部
		setChoiceEditText(R.id.oneold_abdomen_touch, new String[] { "1未见异常" },
				"2异常");
		// 脐部
		setChoiceEditText(R.id.oneold_funicle, new String[] { "1未见异常" }, "2异常");
		// 四肢
		setChoiceEditText(R.id.oneold_limbs, new String[] { "1未见异常" }, "2异常");
		// 可疑佝偻病症状
		setChoiceEditText(R.id.oneold_rickets_sign, new String[] { "1无", "2夜惊",
				"3多汗", "4烦躁" }, null);

		// 可疑佝偻病体征
		setChoiceEditText(R.id.oneold_rickets_feature,
				new String[] { "1无", "2颅骨软化", "3方颅", "4枕秃", "5肋串珠", "6肋外翻",
						"7肋软骨沟", "8鸡胸", "9手镯征" }, null);
		// 肛门/外生殖器
		setChoiceEditText(R.id.oneold_externalia, new String[] { "1未见异常" },
				"2异常");
		// 发育评估growth_ assess
		setChoiceEditText(R.id.oneold_growth_assess,
				new String[] { "通过", "未通过" }, null);
		// 两次随访间患病情况
		setChoiceEditText(R.id.oneold_seak_state, new String[] { "1未患病" }, "2患病");
		// 转诊建议
		setChoiceEditText(R.id.oneold_transfer_advise,
				new String[] { "1无", "2有" }, null);
		// 指导
		setChoiceEditText(R.id.oneold_guide, new String[] { "1科学喂养", "2生长发育",
				"3疾病预防", "4预防意外伤害", "5口腔保健" }, null);
		// 中医健康管理
		setChoiceEditText(R.id.oneold_TCM, new String[] { "1饮食调养指导", "2起居调摄指导",
				"3摩腹、捏脊", "4健康教育处方" }, null);
	}

	protected void onClickButton(View v) {
		if (lock)
			setState(false);
		else
			saveToDb();
	}

	/***
	 * 保存到数据库
	 */
	private void saveToDb() {
		ContentValues content = new ContentValues();
		for (Map.Entry<String, Integer> entry : OneOldChildTable.cloumIdmap
				.entrySet()) {
			content.put(entry.getKey(), getEditTextString(entry.getValue()));
		}

		if (NEW.equals(sysId))// 新建
			dbService.insert(OneOldChildTable.oneold_table, content);
		else {// 修改
			dbService.update(OneOldChildTable.oneold_table,
					DataOpenHelper.SYS_ID, sysId, content);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();

	}

	protected void initFromDb() {
		Cursor cursor = dbService.query(OneOldChildTable.oneold_table,
				DataOpenHelper.SYS_ID, sysId);
		if (cursor.getCount() == 0)
			return;
		cursor.moveToNext();
		Map<String, Integer> map = OneOldChildTable.cloumIdmap;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			setTextFromCursor(cursor, entry.getKey(), entry.getValue());
		}
	}

	private void setState(boolean lock) {
		L.i("setState", this.lock + "-->" + lock);
		this.lock = lock;
		if (lock) {
			saveBtn.setText("修改");
			editHelpBtn.setVisibility(View.VISIBLE);
			bodySv.setBackgroundColor(getResources().getColor(
					R.color.shallow_blue));

		} else {
			saveBtn.setText("保存");
			editHelpBtn.setVisibility(View.GONE);
			bodySv.setBackgroundColor(getResources().getColor(
					android.R.color.white));
		}
	}
}
