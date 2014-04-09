package com.health.users;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.util.L;
import com.health.util.MyProgressDialog;
import com.health.util.T;
import com.health.util.TimeHelper;
import com.health.util.VisitRecordAdapter;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.WebService;

/***
 * 随访记录
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-3-19 下午12:24:57
 */
public class VisitRecord extends BaseActivity {
	private EditText visitRecordEt;
	private static Context context;
	private static LinearLayout visitContentLayout;
	private static MyProgressDialog mDialog;
	private ListView listView;
	private static VisitRecordAdapter adapter;
	private static List<String[]> datas;
	private boolean isShow = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visit_record);
		context = this;
		mDialog = new MyProgressDialog(context);
		datas = new ArrayList<String[]>();
		adapter = new VisitRecordAdapter(context, datas);
		listView = (ListView) findViewById(R.id.visite_record_listview);
		listView.setAdapter(adapter);
		visitRecordEt = (EditText) findViewById(R.id.visit_record_edit_view);
		visitContentLayout = (LinearLayout) findViewById(R.id.visit_content_layout);
		getVisteRecord();
	}

	public static void getVisteRecord() {
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				getVistRecord();
			}
		}).start();
	}

	public void refresh(View view) {
		getVisteRecord();
	}

	private long lastPressTime = 0;

	public void clearVisitRecord(View view) {
		if (visitRecordEt.getText() == null
				|| visitRecordEt.getText().length() == 0)
			return;
		long cTime = System.currentTimeMillis();
		if (cTime - lastPressTime < 3 * 1000)
			visitRecordEt.setText("");
		else {
			lastPressTime = cTime;
			T.showShort(this, "再按一次清空!");
		}
	}

	public final static int UP_OK = 0x2014;
	public final static int GET_OK = 0x2015;
	public final static int UP_ERROE = 0x2016;
	public final static int GET_ERROR = 0x2017;
	private static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mDialog.cancel();
			switch (msg.what) {
			case WebService.OK:
				switch (msg.arg1) {
				case UP_OK:
					T.showLong(context, "随访记录上传成功!");
					visitContentLayout.setVisibility(View.GONE);
					getVisteRecord();
					break;
				case GET_OK:
					adapter.notifyDataSetChanged();
					// 更新界面
					break;
				}
				break;
			default:
				T.showLong(context, "网络错误!");
			}
		}
	};

	/***
	 * 提交随访记录
	 * 
	 * @param view
	 * @throws JSONException
	 */
	public void subVisitRecord(View view) {
		String content = visitRecordEt.getText().toString();
		if (content == null || content.length() < 1) {
			T.showShort(this, "请输入随访记录");
		} else {
			try {
				final JSONObject para = new JSONObject();
				para.put(WebService.CARDNO, BaseActivity.getUser().getCardNo());
				para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
				// para.put(WebService.TOKEN,
				// BaseActivity.getGroup().getToken());
				para.put(WebService.DOCTOR_NAME, BaseActivity.getGroup()
						.getUserName());
				para.put(WebService.PASS_WORD, BaseActivity.getGroup()
						.getPassword());
				JSONObject data = new JSONObject();
				data.put(WebService.CUSTOMERGUID, BaseActivity.getUser()
						.getCustomerGuid());
				data.put(WebService.CONTENT, content);
				data.put(WebService.CHECKTIME, TimeHelper.getCurrentTime());
				para.put(WebService.DATA, data);
				new BackGroundThread(new BackGroundTask() {

					@Override
					public void process() {
						uploadVistRecord(para);
					}
				}).start();
				mDialog.show();
			} catch (JSONException e) {
				T.showShort(this, "上传失败!");
				e.printStackTrace();
			}
		}
	}

	public void addVisitRecord(View view) {
		isShow = !isShow;
		if (isShow)
			visitContentLayout.setVisibility(View.VISIBLE);
		else
			visitContentLayout.setVisibility(View.GONE);
	}

	/***
	 * 上传随访记录,共后台线程调用
	 * 
	 * @param para
	 */
	private void uploadVistRecord(JSONObject para) {
		try {
			L.i("uploadVistRecord", para.toString());
			JSONObject result = WebService
					.httpConenction(para.toString(), WebService.DOMAIN_V2
							+ WebService.PATH_UPLOAD_VISIT_CONTENT);
			int status = result.getInt(WebService.STATUS_CODE);
			mHandler.obtainMessage(status, UP_OK, 0).sendToTarget();
		} catch (Exception e) {
			mHandler.obtainMessage(WebService.ERROE).sendToTarget();
		}
	}

	private static void getVistRecord() {
		final JSONObject para = new JSONObject();
		try {

			para.put(WebService.CARDNO, BaseActivity.getUser().getCardNo());
			para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
			// para.put(WebService.TOKEN,
			// BaseActivity.getGroup().getToken());
			para.put(WebService.DOCTOR_NAME, BaseActivity.getGroup()
					.getUserName());
			para.put(WebService.PASS_WORD, BaseActivity.getGroup()
					.getPassword());
			JSONObject result = WebService
					.httpConenction(WebService.DOMAIN_V2
							+ WebService.PATH_GET_VISIT_CONTENT + "?"
							+ para.toString());
			int status = result.getInt(WebService.STATUS_CODE);
			JSONArray array = result.getJSONArray(WebService.DATA);
			datas.clear();
			for (int i = 0; i < array.length(); i++) {
				JSONObject record = array.getJSONObject(i);
				datas.add(new String[] { record.getString(WebService.CONTENT),
						record.getString(WebService.CHECKTIME) });
			}
			mHandler.obtainMessage(status, GET_OK, 0).sendToTarget();
		} catch (Exception e) {
			L.e("getVistRecord", e.getMessage());
			mHandler.obtainMessage(WebService.ERROE).sendToTarget();
		}

	}
}
