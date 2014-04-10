package com.health.archive;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import cn.younext.R;

import com.health.util.T;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 档案主界面
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-4 下午5:07:18
 */
public class ArchiveMain extends SlidingFragmentActivity implements
		OnClickListener {

	private Fragment mContent;

	private static ArchiveMain instance;
	private TextView titleTv;
	private Button barButton;

	private boolean lock = false;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		initSlidingMenu(savedInstanceState);
		setActionBar();
		instance = this;
	}

	public static ArchiveMain getInstance() {
		return instance;
	}

	@SuppressLint("NewApi")
	private void setActionBar() {
		ActionBar ab = getActionBar();
		if (ab != null) {
			ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			View view = LayoutInflater.from(this).inflate(
					R.layout.layout_title_bar, null);
			titleTv = (TextView) view.findViewById(R.id.title_bar_name);
			barButton = (Button) view.findViewById(R.id.title_bar_btn);
			barButton.setOnClickListener(this);
			Button cButton = (Button) view
					.findViewById(R.id.title_bar_menu_btn);
			cButton.setOnClickListener(this);
			ab.setCustomView(view);
			ab.setDisplayHomeAsUpEnabled(true);
			// setSlidingActionBarEnabled(false);
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
				.replace(R.id.menu_frame, new MenuFragment()).commit();

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

	// 设置修改button的响应事件
	private ActionBarEditable actionBarEdit;

	public interface ActionBarEditable {
		/***
		 * 点击button时做的工作
		 */
		public void processOnButton();
	}

	public void setActionBarEdit(ActionBarEditable edit) {
		actionBarEdit = edit;
	}

	/***
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		titleTv.setText(title);
	}

	/***
	 * 设置按钮的文字
	 * 
	 * @param text
	 */
	public void setButtonText(String text) {
		barButton.setText(text);
	}
	/**
	 * 设置菜单的可用
	 * @param isLock
	 */
	public void setLock(boolean isLock) {
		lock = isLock;
		getSlidingMenu().setSlidingEnabled(!lock);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_bar_btn:			
			if (actionBarEdit != null)
				actionBarEdit.processOnButton();
			break;
		case R.id.title_bar_menu_btn:
			if (!lock)
				toggle();
			else
				T.showShort(this, "请先保存当前内容");
			break;
		default:
			break;
		}
	}
}
