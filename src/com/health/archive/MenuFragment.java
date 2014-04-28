package com.health.archive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.younext.DatabaseHelper;
import cn.younext.R;

import com.health.archive.baby.BabyHomeVistitList;
import com.health.archive.baby.oneold.OneOldChildTable;
import com.health.archive.baby.oneold.VistitList;
import com.health.archive.vaccinate.Vaccinate;
import com.health.database.DataOpenHelper;

public class MenuFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] colors = getResources().getStringArray(R.array.color_names);
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, colors);
		setListAdapter(colorAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		Bundle bundle = new Bundle();
		switch (position) {
		case 0:
			newContent = new ArchiveCover();
			break;
		case 1:
			// newContent = new BasicInfoFragment();
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 8:
			newContent = new Vaccinate();
			break;
		case 9:
			newContent = new BabyHomeVistitList();
			break;
		case 10:
			newContent = new VistitList();
			bundle = new Bundle();
			bundle.putString(VistitList.TITLE, "1岁以内儿童健康检查记录表");
			bundle.putString(VistitList.TO_CTIVITY,
					"com.health.archive.baby.oneold.OneOldChilldVistit");
			bundle.putStringArray(VistitList.CLOUMNS, new String[] {
					DataOpenHelper.SYS_ID, OneOldChildTable.visit_date,
					OneOldChildTable.visit_doctor });
			newContent.setArguments(bundle);
			break;
		case 16:
			newContent = new OldPeopleSelfCare();
			break;
		case 21:
			newContent = new SevereMentalIllnessPatientInfoSupplementary();
			break;
		case 22:
			newContent = new SevereMentalIllnessPatientFollowupRecord();
		}
		if (newContent != null) {
			switchFragment(newContent);
		}
	}

	// 切换Fragment视图内ring
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof ArchiveMain) {
			ArchiveMain fca = (ArchiveMain) getActivity();
			fca.switchContent(fragment);
		}
	}

}
