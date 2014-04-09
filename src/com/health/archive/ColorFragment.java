package com.health.archive;

import cn.younext.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ColorFragment extends Fragment {

	private int mColorRes = -1;

	public ColorFragment() {
		setColorRes(R.color.white);
		setRetainInstance(true);
	}

	public static ColorFragment getColorFragment(int colorRes) {
		ColorFragment fragment = new ColorFragment();
		fragment.setColorRes(colorRes);
		return fragment;
	}

	private void setColorRes(int colorRes) {
		mColorRes = colorRes;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			mColorRes = savedInstanceState.getInt("mColorRes");

		int color = getResources().getColor(mColorRes);

		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		v.setBackgroundColor(color);
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", mColorRes);
	}

}
