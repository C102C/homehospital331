package com.health.archive.vaccinate;

import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import cn.younext.R;

import com.health.BaseActivity;

public class DialogVaccEdit extends BaseActivity {
	public static final String ITEM = "item";
	public static ResultTask mTask;
	EditText vaccKindET;
	EditText vaccTimesET;
	DatePicker vaccDateDp;
	EditText vaccLocationET;
	EditText vaccSerialET;
	EditText vaccDoctorlET;
	EditText vaccNoteET;
	static String[] item = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_accinate_edit);
		if (item == null)
			finish();
		vaccKindET = (EditText) findViewById(R.id.vacc_kind);
		vaccTimesET = (EditText) findViewById(R.id.vacc_time);
		if (!Vaccinate.OTHER.equals(item[0])) {//其他疫苗时才不能编辑
			vaccTimesET.setEnabled(false);
			vaccTimesET.setFocusable(false);			
		}
		vaccDateDp = (DatePicker) findViewById(R.id.vacc_date_dp);
		vaccLocationET = (EditText) findViewById(R.id.vacc_location_et);
		vaccSerialET = (EditText) findViewById(R.id.vacc_serial_et);
		vaccDoctorlET = (EditText) findViewById(R.id.vacc_doctor_et);
		vaccNoteET = (EditText) findViewById(R.id.vacc_note_et);
		setText();
		Button saveBtn = (Button) findViewById(R.id.vacc_save_btn);
		Button cancelBtn = (Button) findViewById(R.id.vacc_cancel_btn);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTask.cancel();
				finish();
			}
		});
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int day = vaccDateDp.getDayOfMonth();
				int month = vaccDateDp.getMonth() + 1;
				int year = vaccDateDp.getYear();
				String date = String.format(Locale.CHINA, "%4d年%2d月%2d日", year,
						month, day);
				item[0] = vaccKindET.getText().toString();
				item[1] = vaccTimesET.getText().toString();
				item[2] = date;
				item[3] = vaccLocationET.getText().toString();
				item[4] = vaccSerialET.getText().toString();
				item[5] = vaccDoctorlET.getText().toString();
				item[6] = vaccNoteET.getText().toString();
				mTask.process(item);
				finish();
			}
		});
	}

	@Override
	public void finish() {
		// 隐藏输入法
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		mTask = null;
		super.finish();
	}

	private void setText() {
		setText(vaccKindET, item[0]);
		setText(vaccTimesET, item[1]);
		setText(vaccLocationET, item[3]);
		setText(vaccSerialET, item[4]);
		setText(vaccDoctorlET, item[5]);
		setText(vaccNoteET, item[6]);
		if (item[2] != null) {
			int year = Integer.valueOf(item[2].substring(0, 4).trim());
			int month = Integer.valueOf(item[2].substring(5, 7).trim()) - 1;
			int day = Integer.valueOf(item[2].substring(8, 10).trim());
			vaccDateDp.init(year, month, day, null);
		}
	}

	private void setText(EditText et, String text) {
		if (text != null)
			et.setText(text);
	}

	public static void setResultTask(ResultTask task) {
		mTask = task;
	}

	public interface ResultTask {
		public void process(String[] item);

		public void cancel();
	}

	public static void setEditItem(String[] editItem) {
		item = editItem;
	}

}
