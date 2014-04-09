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
 * 档案主界面
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-4 下午5:07:18
 */
public class ArchiveMain extends SlidingFragmentActivity {

	private Fragment mContent;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setTitle("目录");
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
	 * 初始化滑动菜单
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到ColorFragment，否则实例化ColorFragment
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new ArchiveCover();

		// 设置主视图界面
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// 设置滑动菜单视图界面
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new ColorMenuFragment()).commit();

		// 设置滑动菜单的属性值
		SlidingMenu sMenu = getSlidingMenu();
		sMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sMenu.setShadowWidthRes(R.dimen.shadow_width);
		sMenu.setShadowDrawable(R.drawable.shadow);
		sMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sMenu.setFadeDegree(0.35f);
	}

	/**
	 * 切换Fragment，也是切换视图的内容
	 */
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}

	/**
	 * 菜单按钮点击事件，通过点击ActionBar的Home图标按钮来打开滑动菜单
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
	 * 保存Fragment的状态
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
}
