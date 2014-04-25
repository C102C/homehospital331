package com.health.archive;

import com.health.viewUtil.ChoiceEditText;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

/***
 * 访问表的基类，封装了一些需要常用的几个方法
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-24 下午5:17:03
 */
public class VisitBaseActivity extends Activity {
	public static final int REQUEST_FRESH = 0x0010;
	// 行号的id，为-1表示来自于新建
	public final static String SYS_ID = "sys_id";
	protected static final String NEW = "-1";
	protected String sysId = NEW;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/***
	 * 用sql游标设置文本内容
	 * 
	 * @param cursor
	 * @param cloumn
	 * @param editTextId
	 */
	protected void setTextFromCursor(Cursor cursor, String cloumn,
			int editTextId) {
		String text = getCursorString(cursor, cloumn);
		setText(editTextId, text);
	}

	/**
	 * 设置id对应的EditText的string为text
	 * 
	 * @param id
	 * @param text
	 */
	protected void setText(int id, String text) {
		if (text != null)
			((EditText) findViewById(id)).setText(text);
	}

	/***
	 * 封装游标的奇葩方法
	 * 
	 * @param cursor
	 * @param cloumn
	 * @return
	 */
	protected String getCursorString(Cursor cursor, String cloumn) {
		return cursor.getString(cursor.getColumnIndex(cloumn));
	}

	protected void seChoiceEditText(int id, String[] items, String editableItem) {
		ChoiceEditText cet = (ChoiceEditText) findViewById(id);
		cet.setFixItems(items);
		if (editableItem != null)
			cet.setEditableItem(editableItem);
	}

	protected String getEditTextString(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

}
