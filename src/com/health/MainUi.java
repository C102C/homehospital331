package com.health;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

import com.health.alarm.AlarmActivity;
import com.health.archive.ArchiveMain;
import com.health.bluetooth.BluetoothService;
import com.health.healthhelp.HealthHelp;
import com.health.heathedu.HealthEdu;
import com.health.heathtools.HealthTools;
import com.health.remoteask.RemoteHealth;
import com.health.users.UserList;

public class MainUi extends BaseActivity {
	/** Called when the activity is first created. */
	Button main_myhealth;
	Button main_test;
	Button main_teleference;
	Button main_calendar;
	Button main_healthinformation;

	OnClickListener btnClick;

	TextView spinnertext;
	TextView dianliang;
	private static Spinner spinner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_ui);
		spinner = (Spinner) this.findViewById(R.id.user_name_spinner);
		setSinppner();

	}

	public void jumpTo(View view) {
		int id = view.getId();
		Intent intent;
		switch (id) {
		case R.id.users_button:
			intent = new Intent(this, UserList.class);
			break;
		case R.id.my_calendar_button:
			intent = new Intent(this, AlarmActivity.class);
			break;
		case R.id.tools_button:
			intent = new Intent(this, HealthTools.class);
			break;
		case R.id.remote_ask_button:
			intent = new Intent(this, RemoteHealth.class);
			break;
		case R.id.health_help_button:
			intent = new Intent(this, HealthHelp.class);
			break;
		case R.id.health_edu_button:
			intent = new Intent(this, HealthEdu.class);
			break;
		default:
			return;
		}
		startActivity(intent);
	}

	@Override
	public void onStart() {
		super.onStart();
		setSinppner();
	}

	private void setSinppner() {
		String[] spinnerTitle = { BaseActivity.getGroup().getNickName(),
				"切换用户", "退出" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerTitle);

		// 设置下拉列表风格
		adapter.setDropDownViewResource(R.layout.main_spinner_dropdown);
		spinner.setAdapter(adapter);
		// 添加Spinner事件监听
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				parent.setVisibility(View.VISIBLE);
				switch (position) {
				case 1:// 切换用户
					Intent intent = new Intent(MainUi.this, Login.class);
					startActivity(intent);
					break;
				case 2:// 退出登录
					exitLogin();
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
	}

	/***
	 * 退出登录
	 */
	protected void exitLogin() {
		AlertDialog.Builder builder = new Builder(MainUi.this);
		builder.setMessage("确认退出登录？");
		builder.setTitle("提示");

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(MainUi.this, Login.class);
				startActivity(intent);
				finish();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				spinner.setSelection(0);// 还是选中用户名
			}
		});
		builder.show();
	}

	protected void onResume() {
		super.onResume();

	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出！",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				BluetoothService.close();// 关闭蓝牙
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}