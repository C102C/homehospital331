package com.health.archive.baby.oneold;

import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.younext.R;

import com.health.database.DataOpenHelper;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.T;
import com.health.viewUtil.ChoiceEditText;

public class OneOldChilldVistit extends Activity {
	public static final int REQUEST_FRESH = 0x0010;
	// �кŵ�id��Ϊ-1��ʾ�������½�
	final static String SYS_ID = "sys_id";
	private static final String NEW = "-1";
	private String sysId = NEW;
	private DatabaseService dbService;
	// ���½����־
	private static final int FRESH_UI = 0x10;
	// ȷ�ϰ�ť
	private static final int POSITIVE = 0x11;
	// ����ɹ�
	private static final int SAVE_OK = 0x12;
	// ����ʧ��
	private static final int SAVE_ERROE = 0x13;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_UI:
				initView();
				break;
			case POSITIVE:
				// saveToDb();// ���浽���ݿ�

				break;
			case SAVE_OK:
				T.showShort(OneOldChilldVistit.this, "����ɹ�");
				setState(true);
				finish();
				break;
			case SAVE_ERROE:
				// T.showShort(getActivity(), "����ʧ��");
				break;
			default:
				break;
			}
		}

	};
	private boolean lock = false;
	private Button editHelpBtn;
	private Button saveBtn;
	private View bodySv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// �����ڲ鿴�����½�,0��ʾ�½�,1��ʾ�鿴
		sysId = getIntent().getStringExtra(SYS_ID);
		setContentView(R.layout.health_archivel_oneold_visit);
		dbService = new DatabaseService(this);
		initView();
	}

	private void initView() {

		setChoiceEditText();
		editHelpBtn = (Button) findViewById(R.id.edit_help_button);
		saveBtn = (Button) findViewById(R.id.oneold_title_btn);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickButton(v);
			}
		});
		bodySv = findViewById(R.id.table_body_sv);
		setState(!NEW.equals(sysId));// �½�ʱ���������������鿴ʱ����Ĭ������
		setText(R.id.oneold_serial_id, Tables.getSerialId());
		if (!NEW.equals(sysId))
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					initFromDb();
				}

			}, 500);

	}

	private void setChoiceEditText() {
		// ����
		seChoiceEditText(R.id.oneold_age, new String[] { "����", "3����", "6����",
				"8����" }, null);
		// ��ɫ���
		seChoiceEditText(R.id.oneold_face, new String[] { "1����", "2��Ⱦ" }, "3����");
		// Ƥ��
		seChoiceEditText(R.id.oneold_skin, new String[] { "1δ���쳣 " }, "2�쳣");
		// ǰض״��
		seChoiceEditText(R.id.oneold_bregma_state,
				new String[] { "1�պ�", "2δ��" }, null);
		// ��������
		seChoiceEditText(R.id.oneold_neck_block, new String[] { "��", "��" },
				null);
		// �����
		seChoiceEditText(R.id.oneold_eye, new String[] { "1δ���쳣 " }, "2�쳣");
		// �����
		seChoiceEditText(R.id.oneold_ear, new String[] { "1δ���쳣 " }, "2�쳣");// ��ɫ���
		// ����
		seChoiceEditText(R.id.oneold_hear, new String[] { "1ͨ��", "2δͨ��" }, null);
		// ��ǻ
		seChoiceEditText(R.id.oneold_mouth, new String[] { "1δ���쳣" }, "2�쳣");
		// �ķ�
		seChoiceEditText(R.id.oneold_heart_hear, new String[] { "1δ���쳣" },
				"2�쳣");
		// ����
		seChoiceEditText(R.id.oneold_abdomen_touch, new String[] { "1δ���쳣" },
				"2�쳣");
		// �겿
		seChoiceEditText(R.id.oneold_funicle, new String[] { "1δ���쳣" }, "2�쳣");
		// ��֫
		seChoiceEditText(R.id.oneold_limbs, new String[] { "1δ���쳣" }, "2�쳣");
		// �������Ͳ�֢״
		seChoiceEditText(R.id.oneold_rickets_sign, new String[] { "1��", "2ҹ��",
				"3�ູ", "4����" }, null);

		// �������Ͳ�����
		seChoiceEditText(R.id.oneold_rickets_feature,
				new String[] { "1��", "2­����", "3��­", "4��ͺ", "5�ߴ���", "6���ⷭ",
						"7����ǹ�", "8����", "9������" }, null);
		// ����/����ֳ��
		seChoiceEditText(R.id.oneold_externalia, new String[] { "1δ���쳣" },
				"2�쳣");
		// ��������growth_ assess
		seChoiceEditText(R.id.oneold_growth_assess,
				new String[] { "ͨ��", "δͨ��" }, null);
		// ������ü仼�����
		seChoiceEditText(R.id.oneold_seak_state, new String[] { "1δ����" }, "2����");
		// ת�ｨ��
		seChoiceEditText(R.id.oneold_transfer_advise,
				new String[] { "1��", "2��" }, null);
		// ָ��
		seChoiceEditText(R.id.oneold_guide, new String[] { "1��ѧι��", "2��������",
				"3����Ԥ��", "4Ԥ�������˺�", "5��ǻ����" }, null);
		// ��ҽ��������
		seChoiceEditText(R.id.oneold_TCM, new String[] { "1��ʳ����ָ��", "2��ӵ���ָ��",
				"3Ħ������", "4������������" }, null);
	}

	private void seChoiceEditText(int id, String[] items, String editableItem) {
		ChoiceEditText cet = (ChoiceEditText) findViewById(id);
		cet.setFixItems(items);
		if (editableItem != null)
			cet.setEditableItem(editableItem);
	}

	protected void onClickButton(View v) {
		if (lock)
			setState(false);
		else
			saveToDb();
	}

	/***
	 * ���浽���ݿ�
	 */
	private void saveToDb() {
		ContentValues content = new ContentValues();
		for (Map.Entry<String, Integer> entry : OneOldChildTable.cloumIdmap
				.entrySet()) {
			content.put(entry.getKey(), getEditTextString(entry.getValue()));
		}

		if (NEW.equals(sysId))// �½�
			dbService.insert(OneOldChildTable.oneold_table, content);
		else {// �޸�
			dbService.update(OneOldChildTable.oneold_table,
					DataOpenHelper.SYS_ID, sysId, content);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();

	}

	private String getEditTextString(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

	protected void initFromDb() {
		Cursor cursor = dbService.query(OneOldChildTable.oneold_table,
				DataOpenHelper.SYS_ID, sysId);
		if (cursor.getCount() == 0)
			return;
		cursor.moveToNext();
		Map<String, Integer> map = OneOldChildTable.cloumIdmap;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			setTextFromCursor(cursor, entry.getKey(), entry.getValue());
		}
	}

	/***
	 * ��sql�α������ı�����
	 * 
	 * @param cursor
	 * @param cloumn
	 * @param editTextId
	 */
	private void setTextFromCursor(Cursor cursor, String cloumn, int editTextId) {
		String text = getCursorString(cursor, cloumn);
		setText(editTextId, text);
	}

	/**
	 * ����id��Ӧ��EditText��stringΪtext
	 * 
	 * @param id
	 * @param text
	 */
	private void setText(int id, String text) {
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
	private String getCursorString(Cursor cursor, String cloumn) {
		return cursor.getString(cursor.getColumnIndex(cloumn));
	}

	private void setState(boolean lock) {
		L.i("setState", this.lock + "-->" + lock);
		this.lock = lock;
		if (lock) {
			saveBtn.setText("�޸�");
			editHelpBtn.setVisibility(View.VISIBLE);
			bodySv.setBackgroundColor(getResources().getColor(
					R.color.shallow_blue));

		} else {
			saveBtn.setText("����");
			editHelpBtn.setVisibility(View.GONE);
			bodySv.setBackgroundColor(getResources().getColor(
					android.R.color.white));
		}
	}
}
