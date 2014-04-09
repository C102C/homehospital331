package com.health.archive;

import cn.younext.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 档案封面
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-4 下午10:07:58
 */
public class ArchiveCover extends Fragment {

	public ArchiveCover() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.health_archivel_cover, null);
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", 4);
	}
}