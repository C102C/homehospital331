package com.health.archive.baby;

import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import cn.younext.R;

import com.health.bluetooth.BluetoothListActivity;
import com.health.database.DataOpenHelper;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.T;
import com.health.viewUtil.ChoiceEditText;

/***
 * ��������ͥ���Ӽ�¼��
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-4-15 ����10:51:11
 */
public class BabyHomeVistit extends Activity {
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
				T.showShort(BabyHomeVistit.this, "����ɹ�");
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
		setContentView(R.layout.health_archivel_baby_visit);
		dbService = new DatabaseService(this);
		initView();
	}

	private void initView() {

		setChoiceEditText();
		editHelpBtn = (Button) findViewById(R.id.edit_help_button);
		saveBtn = (Button) findViewById(R.id.baby_title_btn);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickButton(v);
			}
		});
		bodySv = findViewById(R.id.table_body_sv);
		setState(!NEW.equals(sysId));// �½�ʱ���������������鿴ʱ����Ĭ������
		setText(R.id.serial_id, Tables.getSerialId());
		if (!NEW.equals(sysId))
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					initFromDb();
				}

			}, 500);

	}

	/***
	 * �����ݿ��г�ʼ������
	 */
	private void initFromDb() {
		Cursor cursor = dbService.query(BabyTable.baby_table,
				DataOpenHelper.SYS_ID, sysId);
		if (cursor.getCount() == 0)
			return;
		cursor.moveToNext();
		Map<String, Integer> map = BabyTable.cloumIdmap;
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

	public void onClickButton(View v) {
		if (lock)
			setState(false);
		else
			saveToDb();

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

	/***
	 * ���浽���ݿ�
	 */
	private void saveToDb() {
		ContentValues content = new ContentValues();
		for (Map.Entry<String, Integer> entry : BabyTable.cloumIdmap.entrySet()) {
			content.put(entry.getKey(), getEditTextString(entry.getValue()));
		}

		if (NEW.equals(sysId))// �½�
			dbService.insert(BabyTable.baby_table, content);
		else {// �޸�
			dbService.update(BabyTable.baby_table, DataOpenHelper.SYS_ID,
					sysId, content);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();

	}

	private String getEditTextString(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

	/***
	 * ����ѡ���������
	 */
	private void setChoiceEditText() {
		// ĸ�������ڻ������
		seChoiceEditText(R.id.baby_m_health,
				new String[] { "1����", "2�����ڸ�Ѫѹ" }, "3�쳣");
		// �������
		seChoiceEditText(R.id.baby_birth_state, new String[] { "1˳��", "2̥ͷ����",
				"3��ǯ", "4�ʹ�", "5˫��̥", "6��λ" }, "7����");
		// ��������Ϣ
		seChoiceEditText(R.id.baby_apnea, new String[] { "1��", "2��" }, null);
		// Apgar����
		seChoiceEditText(R.id.baby_apgar_score, new String[] { "1һ����", " 2�����",
				"3����" }, null);
		// �Ƿ��л���
		seChoiceEditText(R.id.baby_malformation, new String[] { "1��" }, "2��");
		// ����������ɸ��
		seChoiceEditText(R.id.baby_hearing_check, new String[] { "1ͨ��",
				"2δͨ�� ", "3δɸ��", "4����" }, null);
		// ����������ɸ��
		seChoiceEditText(R.id.baby_sick_check,
				new String[] { "1�׵�", "2 ����ͪ��֢" }, "3�����Ŵ���л��");
		// ι����ʽ
		seChoiceEditText(R.id.baby_nurse_pattern, new String[] { "1��ĸ��", "2���",
				"3�˹�" }, null);
		// Ż��
		seChoiceEditText(R.id.baby_emesis, new String[] { "1 ��", "2 ��" }, null);
		// ���
		seChoiceEditText(R.id.baby_excrement, new String[] { "1��״", " 2 ϡ" },
				null);
		// ��ɫ
		seChoiceEditText(R.id.baby_complexion, new String[] { "1���� ", "2��Ⱦ" },
				"3����");
		// ���㲿λ
		seChoiceEditText(R.id.baby_jaundice_part, new String[] { "1�沿 ", "2����",
				"3��֫", "4 ����" }, null);
		// ǰض״��
		seChoiceEditText(R.id.baby_bregma_state, new String[] { "1���� ", "2��¡",
				"3���� " }, "4����");
		// �����
		seChoiceEditText(R.id.baby_eye, new String[] { "1δ���쳣" }, "2�쳣");
		// ��֫���
		seChoiceEditText(R.id.baby_limbs, new String[] { "1δ���쳣" }, "2�쳣");
		// �����
		seChoiceEditText(R.id.baby_ear, new String[] { "1δ���쳣" }, "2�쳣");
		// ��������
		seChoiceEditText(R.id.baby_neck_block, new String[] { "1��" }, "2��");
		// ��
		seChoiceEditText(R.id.baby_nose, new String[] { "1δ���쳣" }, "2�쳣");
		// Ƥ��
		seChoiceEditText(R.id.baby_skin,
				new String[] { "1δ���쳣", "2ʪ��", " 3����" }, " 4����");
		// �� ǻ
		seChoiceEditText(R.id.baby_mouth, new String[] { "1δ���쳣" }, "2�쳣");
		// ����
		seChoiceEditText(R.id.baby_heart_hear, new String[] { "1δ���쳣" }, "2�쳣");
		// �ķ�����
		seChoiceEditText(R.id.baby_anus, new String[] { "1δ���쳣" }, "2�쳣");
		// ����ֳ��
		seChoiceEditText(R.id.baby_externalia, new String[] { "1δ���쳣" }, "2�쳣");
		// ��������
		seChoiceEditText(R.id.baby_abdomen_touch, new String[] { "1δ���쳣" },
				"2�쳣");
		// ����
		seChoiceEditText(R.id.baby_spine, new String[] { "1δ���쳣" }, "2�쳣");
		// ���
		seChoiceEditText(R.id.baby_funicle, new String[] { "1δ��", " 2����",
				" 3�겿������" }, "4����");
		// ת�ｨ��
		seChoiceEditText(R.id.baby_transfer_advise,
				new String[] { "1��", "2��" }, null);
		// ָ��
		seChoiceEditText(R.id.baby_guide, new String[] { "1ι��ָ��", "2����ָ��",
				" 3����ָ��", "4Ԥ���˺�ָ�� ", "5��ǻ����ָ��" }, null);
	}

	private void seChoiceEditText(int id, String[] items, String editableItem) {
		ChoiceEditText cet = (ChoiceEditText) findViewById(id);
		cet.setFixItems(items);
		if (editableItem != null)
			cet.setEditableItem(editableItem);
	}
	
	


}
