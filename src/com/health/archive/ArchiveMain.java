package com.health.archive;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.Window;
import cn.younext.R;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * ����������
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-4-4 ����5:07:18
 */
public class ArchiveMain extends SlidingFragmentActivity {

	private Fragment mContent;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setTitle("Ŀ¼");
		initSlidingMenu(savedInstanceState);
		ActionBar ab = getActionBar();

		if (ab != null) {

			ab.setIcon(R.drawable.ic_top_bar_category);
			ab.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_title_bar));
			// ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			// ab.setCustomView(R.layout.layout_title_bar);

			ab.setDisplayHomeAsUpEnabled(true);
		}
	}

	/**
	 * ��ʼ�������˵�
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// ��������״̬��Ϊ����õ�ColorFragment������ʵ����ColorFragment
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new ArchiveCover();

		// ��������ͼ����
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// ���û����˵���ͼ����
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new ColorMenuFragment()).commit();

		// ���û����˵�������ֵ
		SlidingMenu sMenu = getSlidingMenu();
		sMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sMenu.setShadowWidthRes(R.dimen.shadow_width);
		sMenu.setShadowDrawable(R.drawable.shadow);
		sMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sMenu.setFadeDegree(0.35f);
	}

	/**
	 * �л�Fragment��Ҳ���л���ͼ������
	 */
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}

	/**
	 * �˵���ť����¼���ͨ�����ActionBar��Homeͼ�갴ť���򿪻����˵�
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.title_bar_menu_btn:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ����Fragment��״̬
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
}
