package com.health.remoteask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.younext.R;

import com.health.BaseActivity;

public class RemoteAddNew extends BaseActivity {

	EditText et_name;
	EditText et_phone;

	Button btn_save;
	Button btn_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_addnew);

		initWidget();

		btn_save.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String name = et_name.getText().toString();
				String phone = et_phone.getText().toString();
				if (name.equals("") || phone.equals("")) {
					Toast.makeText(RemoteAddNew.this, "姓名和电话不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					RemoteUser user = new RemoteUser();
					user.name = name;
					user.phone = phone;

					// 把联系人信息保存到数据库里面
					long success = RemoteDBHelper
							.getInstance(RemoteAddNew.this).save(user);
					if (success != -1) {
						Toast.makeText(RemoteAddNew.this, "添加成功",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(RemoteAddNew.this,
								RemoteHealth.class);
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(RemoteAddNew.this, "添加失败,请重新操作！",
								Toast.LENGTH_LONG).show();

					}
				}

			}

		});

		btn_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RemoteAddNew.this,
						RemoteHealth.class);
				startActivity(intent);
			}

		});
	}

	public void initWidget() {
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_return = (Button) findViewById(R.id.btn_return);
	}

}
