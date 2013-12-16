package com.health;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.younext.R;

import com.health.util.IDCard;
import com.health.util.IDCardFormatException;
import com.health.web.WebService;

/**
 * 注册
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2013-11-29 下午3:33:50
 */
public class Logup extends BaseActivity {
	private static final int MESSAGE_TOAST = 10000;
	private static final String TOAST = "toast";
	private static final int ENABLED = 10001;

	private static Context context;
	private static EditText nameEditText;
	private static EditText cardNoEditText;
	private static EditText moblieEditText;
	// private static DatePicker datePicker;
	private static EditText passwordEditText;
	private static EditText cPasswordEditText;// 确认密码
	private static EditText emailText;
	private static Button logupButton;

	private static String birthday = null;
	private static String sex = null;

	private ExecutorService exec = Executors.newSingleThreadExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logup);
		context = this;
		nameEditText = (EditText) findViewById(R.id.name);
		cardNoEditText = (EditText) findViewById(R.id.cardNo);
		moblieEditText = (EditText) findViewById(R.id.mobile);
		emailText = (EditText) findViewById(R.id.email);
		passwordEditText = (EditText) findViewById(R.id.password);
		cPasswordEditText = (EditText) findViewById(R.id.confirm_password);
		logupButton = (Button) findViewById(R.id.logupButton);
		logupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = nameEditText.getText().toString();
				if (name.length() == 0) {
					Toast.makeText(context, "姓名不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				String cardNo = cardNoEditText.getText().toString();
				if (cardNo.length() == 0) {
					Toast.makeText(context, "身份证号码不能为空", Toast.LENGTH_LONG)
							.show();
					return;
				}
				String moblie = moblieEditText.getText().toString();
				if (moblie.length() == 0) {
					Toast.makeText(context, "电话号码不能为空", Toast.LENGTH_LONG)
							.show();
					return;
				}				
				String email = emailText.getText().toString();
				if (email.length() == 0) {
					Toast.makeText(context, "邮箱用于找回密码，不能为空", Toast.LENGTH_LONG)
							.show();
					return;
				}				
				String password = passwordEditText.getText().toString();
				if (password.length() == 0) {
					Toast.makeText(context, "密码不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				String cPassword = cPasswordEditText.getText().toString();
				if (cPassword.length() == 0) {
					Toast.makeText(context, "确认密码不能为空", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (!cPassword.equals(password)) {
					Toast.makeText(context, "两次密码输入不相同", Toast.LENGTH_LONG)
							.show();
					return;
				}
				try {
					JSONObject info = toJson(name, cardNo, moblie, birthday,
							email, sex, password);
					logupButton.setEnabled(false);
					exec.execute(new Loguper(info));
				} catch (JSONException e) {
					Toast.makeText(context, "网络异常", Toast.LENGTH_LONG).show();
				}
			}
		});
		OnFocusChangeListener focusListenner = new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (!hasFocus) {
					if (nameEditText == view)
						Toast.makeText(context, "姓名输入完", Toast.LENGTH_SHORT)
								.show();
					if (cardNoEditText == view) {
						String cardNo = cardNoEditText.getText().toString();
						try {
							IDCard idcard = new IDCard(cardNo);
							birthday = idcard.getFormatBirthDate("yyyy/MM/dd");
							sex = idcard.isFemal() ? "女" : "男";							
						} catch (IDCardFormatException e) {
							e.printStackTrace();
							Toast.makeText(context, "身份证格式错误!",
									Toast.LENGTH_SHORT).show();
							birthday ="1987-06-06";
							sex ="男";
						}
					}
				}
			}
		};
		nameEditText.setOnFocusChangeListener(focusListenner);
		cardNoEditText.setOnFocusChangeListener(focusListenner);
		moblieEditText.setOnFocusChangeListener(focusListenner);
		emailText.setOnFocusChangeListener(focusListenner);
		passwordEditText.setOnFocusChangeListener(focusListenner);
		cPasswordEditText.setOnFocusChangeListener(focusListenner);
	}

	static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_TOAST:
				Toast.makeText(context, msg.getData().getString(TOAST),
						Toast.LENGTH_SHORT).show();
				break;
			case ENABLED:
				logupButton.setEnabled(true);
			}
		}
	};

	private class Loguper implements Runnable {
		JSONObject info;

		public Loguper(JSONObject info) {
			this.info = info;

		}

		@Override
		public void run() {
			try {
				logup(info);
				handler.obtainMessage(ENABLED, 0, -1).sendToTarget();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private JSONObject toJson(String name, String cardNo, String moblie,
			String birthday, String email, String sex, String password)
			throws JSONException {
		JSONObject info = new JSONObject();
		info.put("name", name);
		info.put("cardNo", cardNo);
		info.put("moblie", moblie);
		info.put("birthday", birthday);
		info.put("email", email);
		info.put("sex", sex);
		info.put("password", password);
		info.put(WebService.PLAT_ID_KEY, WebService.PLAT_ID_VALUE);
		return info;
	}

	private void logup(JSONObject info) throws JSONException {
		JSONObject result = WebService.logup(info);
		String resultMsg;
		if (result == null) {
			resultMsg = "网络异常";
		} else {
			int status = result.getInt(WebService.STATUS_CODE);
			resultMsg = result.getString(WebService.STATUS_MSG);
			if (status == WebService.OK) {
				// 注册成功
			} else {

			}
		}
		Message Message = handler.obtainMessage(MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(TOAST, resultMsg);
		Message.setData(bundle);
		handler.sendMessage(Message);
	}
}
