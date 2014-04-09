package com.health;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import cn.younext.R;

import com.health.bean.Group;
import com.health.database.Cache;
import com.health.util.MyMD5;
import com.health.util.MyProgressDialog;
import com.health.util.T;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.WebService;
import com.health.web.WebService.StatusCode;

public class Login extends BaseActivity {

	protected static final int MESSAGE_TOAST = 100300;
	private static final int ENABLED = 100301;
	protected static final int LOGIN_STATE = 100302;
	private static final String TOAST = "toast";

	private static AutoCompleteTextView countET;
	private static EditText passwordET;
	private static Button loginButton;
	private static Button logupButton;
	private static Context context;
	private static String count = "";
	private static String passWord = "";
	private static MyProgressDialog dialog;
	private CheckBox saveCb;

	private static final String FILE_NAME = "accountsAndPasswords";
	private Map<String, String> accountPasswordMap = new TreeMap<String, String>();
	private List<String> accounts = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		context = this;
		cache = new Cache(Login.this);
		dialog = new MyProgressDialog(context);
		saveCb = (CheckBox) findViewById(R.id.save_password_cb);
		passwordET = (EditText) this.findViewById(R.id.et_passworld);
		readAccountsAndPasswords();

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, accounts);
		countET = (AutoCompleteTextView) findViewById(R.id.cardNumAuto);
		countET.setAdapter(arrayAdapter);
		countET.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String s = accounts.get(position);

				if (accountPasswordMap.containsKey(s)) {
					passwordET.setText(accountPasswordMap.get(s));
				}
				// 隐藏输入法
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				countET.setCursorVisible(false);
				imm.hideSoftInputFromWindow(countET.getWindowToken(), 0);
			}
		});

		passwordET = (EditText) findViewById(R.id.et_passworld);
		passwordET.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String s = countET.getText().toString();
				if (accountPasswordMap.containsKey(s)) {
					passwordET.setText(accountPasswordMap.get(s));
				}
				return false;
			}
		});

		// countET.setText(COUNT);
		// passwordET.setText(PSWD);
		// countET.clearFocus();
		// passwordET.clearFocus();
		loginButton = (Button) this.findViewById(R.id.loginButton);
		logupButton = (Button) this.findViewById(R.id.btn_read_id);
		OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == loginButton) {
					dialog.show();
					loginButton.setEnabled(false);// 登录时不可再按登录
					loginBackGround();
				} else if (v == logupButton) {
					Intent intent = new Intent(Login.this, ReadIDCard.class);
					startActivity(intent);
				}
			}

		};
		loginButton.setOnClickListener(listener);
		logupButton.setOnClickListener(listener);

	}

	private void jump2MainUi() {
		Intent intent = new Intent(Login.this, MainUi.class);
		startActivity(intent);
		finish();
	}

	private void loginBackGround() {
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				try {
					login();					
				} catch (Exception e) {
					handler.obtainMessage(LOGIN_STATE, WebService.NETERROE, -1).sendToTarget();
				}
				handler.obtainMessage(ENABLED, 0, -1).sendToTarget();
			}
		}).start();
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOGIN_STATE:
				dialog.cancel();
				loginButton.setEnabled(true);
				switch (msg.arg1) {
				case WebService.NETERROE:
					T.showLong(context, "网络错误!");
					break;
				case WebService.OK:
					T.showLong(context, "登录成功!");
					JSONObject json = (JSONObject) msg.obj;
					try {
						JSONObject userInfo = json
								.getJSONObject(WebService.DATA);
						setLoginInfo(userInfo, count, passWord);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					jump2MainUi();
					break;
				case WebService.ERROE:
					T.showLong(context, "用户名或密码错误!");
					break;
				case StatusCode.CAN_NOT_LOGIN:
					T.showLong(context, "该用户不允许登录系统!");
					break;
				case StatusCode.CAN_NOT_IDENTIFY:
					T.showLong(context, "无法识别的用户");
					break;
				case StatusCode.NULL_NAME_PASSWORD:
					T.showLong(context, "用户名或密码为空");
					break;
				case StatusCode.FAILED_LOGIN:
					T.showLong(context, "用户名或密码错误！");
					break;
				default:
					break;
				}
			case ENABLED:
				loginButton.setEnabled(true);
				break;

			}
		}
	};

	private void login() throws Exception {
		count = countET.getText().toString();
		passWord = passwordET.getText().toString();
		JSONObject para = new JSONObject();
		para.put("username", count);
		para.put("password", passWord);
		para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
		String crc = count + passWord + WebService.GUID_VALUE;
		crc = MyMD5.getMD5(crc);
		para.put(WebService.CRC, crc);
		JSONObject result = WebService.postConenction(para,
				WebService.PATH_GROUP_LOGIN);
		if (result == null) {
			handler.obtainMessage(LOGIN_STATE, WebService.NETERROE, 0, null)
					.sendToTarget();
		} else {
			int state = result.getInt(WebService.STATUS_CODE);
			handler.obtainMessage(LOGIN_STATE, state, 0, result).sendToTarget();
		}
	}

	/**
	 * 保存账号,设置登录信息
	 * 
	 * @throws JSONException
	 */
	private void setLoginInfo(JSONObject userInfo, String count, String passWord)
			throws JSONException {
		writeAccountAndPassword();
		String nickName = userInfo.getString(WebService.NICKNAME);
		if (nickName.contains("平安通"))
			nickName = "王医生";
		BaseActivity.setGroup(new Group(nickName, count, userInfo
				.getString(WebService.TOKEN), passWord));
	}

	/**
	 * Read accounts and passwords from file and store
	 * them in a map
	 */
	private void readAccountsAndPasswords() {
		try {
			FileInputStream fis = openFileInput(FILE_NAME);
			byte[] buffer = new byte[1024];
			int count = 0;
			StringBuilder stringBuilder = new StringBuilder("");

			while ((count = fis.read(buffer)) > 0) {
				stringBuilder.append(new String(buffer, 0, count));
			}

			String[] lines = stringBuilder.toString().split("\n");
			for (int i = 0; i < lines.length; i++) {
				String[] accountAndPassword = lines[i].split(",");
				accountPasswordMap.put(accountAndPassword[0],
						accountAndPassword[1]);
			}

			// Get all entries into a set
			Set<Map.Entry<String, String>> entrySet = accountPasswordMap
					.entrySet();

			for (Map.Entry<String, String> entry : entrySet) {
				accounts.add(entry.getKey());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write the account and password into the file in
	 * append way
	 */
	private void writeAccountAndPassword() {
		String account = countET.getText().toString();
		String password = passwordET.getText().toString();
		if (!saveCb.isChecked()) {// 不需要保存密码
			if (accountPasswordMap.containsKey(account))
				accountPasswordMap.remove(account);
		} else {// 保存密碼
			accountPasswordMap.put(account, password);
		}
		try {
			FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
			PrintStream ps = new PrintStream(fos);
			for (Map.Entry<String, String> entry : accountPasswordMap
					.entrySet())
				ps.println(entry.getKey() + "," + entry.getValue());
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
