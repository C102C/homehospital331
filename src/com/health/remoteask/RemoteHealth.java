package com.health.remoteask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.MainUi;

public class RemoteHealth extends BaseActivity {

	ListView lv;
	ImageButton btn_addContact;
	Button btn_home;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.remote_main);
		// loadUserList();

		btn_addContact = (ImageButton) findViewById(R.id.ib_add_user);
		btn_home = (Button) findViewById(R.id.btn_home);

		btn_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RemoteHealth.this, MainUi.class);
				startActivity(intent);

			}

		});

		btn_addContact.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent(RemoteHealth.this,
						RemoteAddNew.class);
				startActivity(intent);
				finish();
			}

		});
	}

	@Override
	public void onStart() {
		super.onStart();
		loadUserList();
	}

	// 加载联系人列表
	private void loadUserList() {
		lv = (ListView) findViewById(R.id.lv_user_list);
		ArrayList<Map<String, String>> data = RemoteDBHelper.getInstance(this)
				.getUserList();
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.remote_list_item, new String[] { "name", "phone" },
				new int[] { R.id.tv_name, R.id.tv_mobilephone });
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, String> contact = (Map<String, String>) parent
						.getItemAtPosition(position);
				dialog(contact);

			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> arg0,
					View arg1, final int arg2, long arg3) {

				new AlertDialog.Builder(RemoteHealth.this)
						.setTitle("确定要删除选中的记录吗?")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										RemoteDBHelper helper = new RemoteDBHelper(
												RemoteHealth.this);
										HashMap item = (HashMap) arg0
												.getItemAtPosition(arg2);
										String phone = (String) item
												.get("phone");
										helper.delete(phone);

										// 重置视图
										ArrayList<Map<String, String>> list = helper
												.getUserList();
										SimpleAdapter adapter = new SimpleAdapter(
												RemoteHealth.this,
												list,
												R.layout.remote_list_item,
												new String[] { "name", "phone" },
												new int[] { R.id.tv_name,
														R.id.tv_mobilephone });
										adapter.notifyDataSetChanged();
										lv.setAdapter(adapter);

									}
								}).setNegativeButton("取消", null).create()
						.show();

				return true;
			}

		});
	}

	private void dialog(final Map<String, String> map) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("给" + map.get("name") + "  " + map.get("phone")
				+ "拨打电话");
		builder.setTitle("远程医疗");

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri
						.parse("tel://" + map.get("phone")));
				startActivity(intent);
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}

		});
		builder.show();
	}
}
