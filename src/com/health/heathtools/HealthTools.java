package com.health.heathtools;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import cn.younext.R;

import com.health.BaseActivity;

public class HealthTools extends BaseActivity {

	private GridView gv;
	ArrayList<APPinfo> APPset;
	MyGridAdapter gridapter;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview);
		this.context = this;
		getInstalledApps();
		gridapter = new MyGridAdapter(this, APPset);
		gv = (GridView) findViewById(R.id.mygridview);
		gv.setAdapter(gridapter);
		gv.setOnItemClickListener(new ItemClickListener());

	}

	class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			final int thisposition = position;
			new AlertDialog.Builder(context)
					.setTitle("确认")
					.setMessage("确定打开软件吗？")

					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									APPinfo appinfo = (APPinfo) APPset
											.get(thisposition);
									Intent LaunchIntent = getPackageManager()
											.getLaunchIntentForPackage(
													appinfo.getAPPpackagename());
									startActivity(LaunchIntent); // 按钮事件

								}
							}).setNegativeButton("取消", null).show();

		}
	}

	private void getInstalledApps() {
		List<PackageInfo> packages = getPackageManager()
				.getInstalledPackages(0);
		APPset = new ArrayList<APPinfo>();
		List<String> APPnames = new ArrayList<String>();
		APPnames.add("宝宝树");
		APPnames.add("病历夹");
		APPnames.add("服药提醒");
		APPnames.add("医口袋");
		APPnames.add("医学文献");
		APPnames.add("用药助手");
		for (int j = 0; j < packages.size(); j++) {

			PackageInfo packageInfo = packages.get(j);
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				String APPname = packageInfo.applicationInfo.loadLabel(
						getPackageManager()).toString();
				if (APPnames.contains(APPname)) {

					APPinfo appinfo = new APPinfo(packageInfo.applicationInfo
							.loadLabel(getPackageManager()).toString(),
							packageInfo.applicationInfo.loadIcon(
									getPackageManager()).getCurrent(),
							packageInfo.packageName);
					APPset.add(appinfo);
				}
			}
		}
	}

}