package com.health.archive;

import com.health.viewUtil.ChoiceEditText;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

/***
 * ���ʱ�Ļ��࣬��װ��һЩ��Ҫ���õļ�������
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-4-24 ����5:17:03
 */
public class VisitBaseActivity extends Activity {
	public static final int REQUEST_FRESH = 0x0010;
	// �кŵ�id��Ϊ-1��ʾ�������½�
	public final static String SYS_ID = "sys_id";
	protected static final String NEW = "-1";
	protected String sysId = NEW;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/***
	 * ��sql�α������ı�����
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
	 * ����id��Ӧ��EditText��stringΪtext
	 * 
	 * @param id
	 * @param text
	 */
	protected void setText(int id, String text) {
		if (text != null)
			((EditText) findViewById(id)).setText(text);
	}

	/***
	 * ��װ�α�����ⷽ��
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
