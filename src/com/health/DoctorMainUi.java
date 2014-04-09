package com.health;

import com.health.heathtools.HealthTools;
import com.health.users.UserList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cn.younext.R;
import cn.younext.MyCalendar;
import cn.younext.RemoteAsk;

/***
 * 医生版主界面
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-3-13 上午9:59:49
 */
public class DoctorMainUi extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_main_ui);
	}

	public void jumpTo(View view) {
		int id = view.getId();
		Intent intent;
		switch (id) {
		case R.id.users_button:
			intent = new Intent(this, UserList.class);
			break;
		case R.id.my_calendar_button:
			intent = new Intent(this, MyCalendar.class);
			break;
		case R.id.tools_button:
			intent = new Intent(this, HealthTools.class);
			break;
		case R.id.remote_ask_button:
			intent = new Intent(this, RemoteAsk.class);
			break;
		default:
			return;
		}
		startActivity(intent);
	}

}
