package com.health.users;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.bean.User;
import com.health.measurement.Measurement;
import com.health.myhealth.TableRecord;
import com.health.util.TwoCloumAdapter;

public class UserDetail extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_detail);
		initView();
	}

	private void initView() {
		ListView list = (ListView) findViewById(R.id.user_detail_list);
		List<String[]> data = new ArrayList<String[]>();
		User user = BaseActivity.getUser();
		if (user == null)// 用户不存在,异常情况.
			finish();
		data.add(new String[] { "姓名", user.getName() });
		data.add(new String[] { "卡号", user.getCardNo() });
		data.add(new String[] { "用户id", user.getCardNo() });
		data.add(new String[] { "出生日期", user.getBirthday() });
		data.add(new String[] { "性别", user.getSex() });
		data.add(new String[] { "昵称", user.getNickName() });
		data.add(new String[] { "编号", user.getCustomerGuid() });
		data.add(new String[] { "机构id", user.getUserGuid() });
		data.add(new String[] { "手机号码", user.getMobile() });
		data.add(new String[] { "邮箱", user.getEmail() });
		for (int i = 0; i < data.size(); i++) {
			String[] tmp = data.get(i);
			for (int j = 0; j < tmp.length; j++) {
				if (tmp[j] == null || tmp[j].length() == 0
						|| "null".equals(tmp[j]))
					data.get(i)[j] = "无";
			}

		}
		TwoCloumAdapter adapter = new TwoCloumAdapter(this, data);
		list.setAdapter(adapter);
		TextView nameTv = (TextView) findViewById(R.id.user_name_tv);
		nameTv.setText(user.getName());
	}

	/***
	 * 跳转进入历史测量记录
	 * 
	 * @param view
	 */
	public void measureHistory(View view) {
		Intent intent = new Intent(this, TableRecord.class);
		startActivity(intent);
	}

	/**
	 * 跳转进入用户测量
	 * 
	 * @param view
	 */

	public void healthMeasuer(View view) {
		Intent intent = new Intent(this, Measurement.class);
		startActivity(intent);
	}

	/**
	 * 跳转进入用户测量
	 * 
	 * @param view
	 */

	public void visitRecord(View view) {
		Intent intent = new Intent(this, VisitRecord.class);
		startActivity(intent);
	}

	/**
	 * 跳转进入用户档案
	 * 
	 * @param view
	 */

	public void healthArchive(View view) {
		Intent intent = new Intent(this, HealthArchive.class);
		startActivity(intent);
	}
}
