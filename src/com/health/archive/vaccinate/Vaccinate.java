package com.health.archive.vaccinate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import cn.younext.R;

import com.health.archive.ArchiveMain;
import com.health.archive.ArchiveMain.ActionBarEditable;
import com.health.archive.vaccinate.DialogVaccEdit.ResultTask;
import com.health.database.DatabaseService;
import com.health.util.L;
import com.health.util.ListViewForScrollView;
import com.health.util.T;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;

/***
 * 疫苗接种卡
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-8 上午10:45:56
 */
public class Vaccinate extends Fragment {
	private ListViewForScrollView vaccListView;
	private VaccinateAdapter adapter;
	private LayoutInflater inflater;
	private List<String[]> datas;

	// 浮在整个fragment上的button，用于控制修改和保存
	private Button editHelpBtn;

	// 整个显示界面，把它提到这里，是在需要时给她换个衣服（颜色）
	private ScrollView bodySv;

	// 布局根界面，把它搞进来是为了在onCreateView中迅速返回，以免卡顿
	private RelativeLayout rootView;
	// 当前布局内容
	private View cView;

	// 控制菜单的锁
	private boolean lock = false;
	// 数据库
	private DatabaseService dbService;
	// 更新界面标志
	private static final int FRESH_UI = 0x10;
	// 确认按钮
	private static final int POSITIVE = 0x11;
	// 保存成功
	private static final int SAVE_OK = 0x12;
	// 保存失败
	private static final int SAVE_ERROE = 0x13;

	public static final String OTHER = "其他疫苗";
	private static final String[] VACCS = new String[] { "乙肝疫苗", "卡介苗", "脊灰疫苗",
			"百白破疫苗", "白破疫苗", "麻风疫苗", "麻腮风疫苗", "麻腮疫苗", "麻疹疫苗", "A群流脑疫苗",
			"A+C群流脑疫苗", "乙脑（减毒）活疫苗", "乙脑灭活疫苗", "甲肝减毒活疫苗", "甲肝灭活疫苗", OTHER };
	// 接种次数
	private static final Integer[] TIMES = { 3, 1, 4, 4, 1, 1, 2, 1, 2, 2, 2,
			2, 4, 1, 2, 1 };

	public Vaccinate() {
		setRetainInstance(true);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_UI:
				initView();
				break;
			case POSITIVE:
				saveToDb();// 保存到数据库
				T.showShort(getActivity(), "保存成功");
				break;
			case SAVE_OK:
				switchLock();
				break;
			case SAVE_ERROE:
				T.showShort(getActivity(), "保存失败");
				break;
			default:
				break;
			}
		}

	};

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("确认保存修改吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.obtainMessage(POSITIVE).sendToTarget();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		rootView = new RelativeLayout(getActivity());
		// 让后台线程告诉界面去更新,这里立即返回一个跟view
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				try {
					TimeUnit.MILLISECONDS.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.obtainMessage(FRESH_UI).sendToTarget();
			}
		}).start();
		// 标题还是先写上,不然一偏空白感觉不好
		ArchiveMain.getInstance().setTitle("预防接种卡");
		ArchiveMain.getInstance().setButtonText("修改");
		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void initView() {
		dbService = new DatabaseService(getActivity());
		cView = inflater.inflate(R.layout.health_archivel_accinate, null);
		rootView.addView(cView);
		findViewById(cView);
		// initDate()里面其实干了大事，但总之是负责加载数据，返回的时疫苗记录表的数据
		datas = initDatas();
		adapter = new VaccinateAdapter(getActivity(), datas);
		vaccListView.setAdapter(adapter);
		setListener(vaccListView);
		ArchiveMain.getInstance().setActionBarEdit(new ActionBarEditable() {

			@Override
			public void processOnButton() {
				if (lock)// 处于修改状态时,弹出对话框后确认才保存修改,并更改状态
					dialog();
				else
					switchLock();

			}
		});

	}

	private void switchLock() {
		lock = !lock;
		ArchiveMain.getInstance().setLock(lock);
		setButtonText();
	}

	/***
	 * 根据id把那一堆的控件初始化
	 * 
	 * @param view
	 */
	private void findViewById(View view) {
		vaccListView = (ListViewForScrollView) view
				.findViewById(R.id.vaccinate_list_view);
		bodySv = (ScrollView) view.findViewById(R.id.table_body_sv);
		editHelpBtn = (Button) view.findViewById(R.id.edit_help_button);
	}

	private void setButtonText() {
		if (lock) {
			ArchiveMain.getInstance().setButtonText("保存");
			editHelpBtn.setVisibility(View.GONE);
			bodySv.setBackgroundColor(getResources().getColor(
					android.R.color.white));
		} else {
			ArchiveMain.getInstance().setButtonText("修改");
			editHelpBtn.setVisibility(View.VISIBLE);
			bodySv.setBackgroundColor(getResources().getColor(
					R.color.shallow_blue));
		}
	}

	private void setListener(ListView listView) {
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				String[] item = (String[]) parent.getItemAtPosition(position);
				startEditVacc(item);
				return true;
			}
		});

	}

	final ResultTask resultTask = new ResultTask() {

		@Override
		public void process(String[] item) {
			if (item[0].equals(OTHER)) {
				String[] other = new String[item.length];
				other[0] = OTHER;
				other[1] = "1";
				datas.add(other);
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		public void cancel() {
		}

	};

	private void startEditVacc(String[] item) {
		Intent serverIntent = new Intent(getActivity(), DialogVaccEdit.class);
		DialogVaccEdit.setResultTask(resultTask);
		DialogVaccEdit.setEditItem(item);
		getActivity().startActivity(serverIntent);
	}

	/***
	 * 从数据库中读取初始化界面，如果数据库中无记录则初始化一些最基本的数据
	 * 
	 * @return
	 */
	private List<String[]> initDatas() {

		List<String[]> datas = new ArrayList<String[]>();

		String[] title = { "疫苗", "剂次", "接种日期", "接种部位", "疫苗批号", "接种医生", "备注" };
		datas.add(title);

		if (!initHeadFromDb()) {// 从数据库中加载表头失败,也说明数据库中没有疫苗记录
			initToDB();
			for (int i = 0; i < VACCS.length; i++) {
				for (int j = 0; j < TIMES[i]; j++) {
					String[] content = new String[title.length];
					content[0] = VACCS[i];
					content[1] = String.valueOf(j + 1);
					datas.add(content);
				}
			}

		} else {// 加载表头成功,说明数据库中也应有了疫苗记录
			datas.addAll(getRecordFromDb());// 加载疫苗记录
		}
		return datas;

	}

	/***
	 * 从数据库中读取表头信息,更新界面
	 * 
	 * @return 当更新界面成功返回true，当数据库中无记录时返回false，
	 */
	private boolean initHeadFromDb() {
		Cursor cursor = dbService.query(VaccTables.vacc_head_table,
				VaccTables.serial_id, VaccTables.getSerialId());
		if (cursor.getCount() == 0) {// 数据库中无记录
			return false;
		}
		cursor.moveToNext();
		setTextFromCursor(cursor, VaccTables.serial_id, R.id.serial_id);
		setTextFromCursor(cursor, VaccTables.guardian, R.id.guardian_et);
		setTextFromCursor(cursor, VaccTables.relation, R.id.relation_et);
		setTextFromCursor(cursor, VaccTables.phone, R.id.guardian_phone_et);
		setTextFromCursor(cursor, VaccTables.addr, R.id.addr_et);
		setTextFromCursor(cursor, VaccTables.register_addr, R.id.r_addr_et);
		setTextFromCursor(cursor, VaccTables.in_time, R.id.intime_det);
		setTextFromCursor(cursor, VaccTables.out_time, R.id.outtime_det);
		setTextFromCursor(cursor, VaccTables.out_reason, R.id.out_reason_det);
		setTextFromCursor(cursor, VaccTables.abnormal_history, R.id.abn_his_et);
		setTextFromCursor(cursor, VaccTables.vacc_taboo, R.id.vacc_taboo_det);
		setTextFromCursor(cursor, VaccTables.infection_history,
				R.id.infection_det);
		setTextFromCursor(cursor, VaccTables.add_date, R.id.add_date_det);
		setTextFromCursor(cursor, VaccTables.add_person, R.id.add_person_et);
		return true;
	}

	/***
	 * 用sql游标设置文本内容
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
	 * 设置id对应的EditText的string为text
	 * 
	 * @param id
	 * @param text
	 */
	private void setText(int id, String text) {
		if (text != null)
			((EditText) cView.findViewById(id)).setText(text);
	}

	/***
	 * 封装游标的奇葩方法
	 * 
	 * @param cursor
	 * @param cloumn
	 * @return
	 */
	private String getCursorString(Cursor cursor, String cloumn) {
		return cursor.getString(cursor.getColumnIndex(cloumn));
	}

	/***
	 * 从数据库中获取疫苗记录
	 * 
	 * @return
	 */
	private List<String[]> getRecordFromDb() {
		List<String[]> result = new ArrayList<String[]>();
		Cursor cursor = dbService.query(VaccTables.vacc_record_table,
				VaccTables.serial_id, VaccTables.getSerialId());
		if (cursor.getCount() == 0) {// 数据库中无记录
			return result;
		}
		while (cursor.moveToNext()) {
			String[] line = new String[7];
			line[0] = getCursorString(cursor, VaccTables.vacc_kind);
			line[1] = getCursorString(cursor, VaccTables.vacc_time);
			line[2] = getCursorString(cursor, VaccTables.vacc_date);
			line[3] = getCursorString(cursor, VaccTables.vacc_part);
			line[4] = getCursorString(cursor, VaccTables.vacc_serial);
			line[5] = getCursorString(cursor, VaccTables.vacc_doctor);
			line[6] = getCursorString(cursor, VaccTables.vacc_note);
			result.add(line);
		}
		return result;
	}

	/***
	 * 将初始化数据插入数据库
	 */
	private void initToDB() {
		// 插入表头基本信息
		ContentValues headContent = new ContentValues();
		headContent.put(VaccTables.serial_id, VaccTables.getSerialId());
		dbService.insert(VaccTables.vacc_head_table, headContent);

		// 插入前先删除,以免重复
		dbService.delete(VaccTables.vacc_record_table, VaccTables.serial_id,
				VaccTables.getSerialId());
		// 插入疫苗记录
		ContentValues recordContent = new ContentValues();
		recordContent.put(VaccTables.serial_id, VaccTables.getSerialId());
		for (int i = 0; i < VACCS.length; i++) {
			for (int j = 0; j < TIMES[i]; j++) {
				recordContent.put(VaccTables.vacc_kind, VACCS[i]);
				recordContent.put(VaccTables.vacc_time, j);
				dbService.insert(VaccTables.vacc_record_table, recordContent);
			}
		}
	}

	/**
	 * 保存数据到数据库
	 */
	private void saveToDb() {
		L.i("saveToDb:", "saveToDb");
		// 更新表头基本信息
		ContentValues headContent = getHeadContent();
		dbService.update(VaccTables.vacc_head_table, VaccTables.serial_id,
				VaccTables.getSerialId(), headContent);
		for (String[] line : datas) {
			ContentValues recoredContent = getRecordContent(line);
			dbService.update(VaccTables.vacc_record_table,
					VaccTables.serial_id, VaccTables.getSerialId(),
					recoredContent);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();
	}

	/***
	 * 将表格行的数据存放在k-v的ContentValues中
	 * 
	 * @param line
	 * @return
	 */
	private ContentValues getRecordContent(String[] line) {
		ContentValues content = new ContentValues();
		content.put(VaccTables.vacc_kind, line[0]);
		content.put(VaccTables.vacc_time, line[1]);
		content.put(VaccTables.vacc_date, line[2]);
		content.put(VaccTables.vacc_part, line[3]);
		content.put(VaccTables.vacc_serial, line[4]);
		content.put(VaccTables.vacc_doctor, line[5]);
		content.put(VaccTables.vacc_note, line[6]);
		return content;
	}

	/***
	 * 获去表头数据
	 * 
	 * @return
	 */
	private ContentValues getHeadContent() {
		ContentValues content = new ContentValues();
		putValue(content, VaccTables.serial_id, R.id.serial_id);
		putValue(content, VaccTables.guardian, R.id.guardian_et);
		putValue(content, VaccTables.relation, R.id.relation_et);
		putValue(content, VaccTables.phone, R.id.guardian_phone_et);
		putValue(content, VaccTables.addr, R.id.addr_et);
		putValue(content, VaccTables.register_addr, R.id.r_addr_et);
		putValue(content, VaccTables.in_time, R.id.intime_det);
		putValue(content, VaccTables.out_time, R.id.outtime_det);
		putValue(content, VaccTables.out_reason, R.id.out_reason_det);
		putValue(content, VaccTables.abnormal_history, R.id.abn_his_et);
		putValue(content, VaccTables.vacc_taboo, R.id.vacc_taboo_det);
		putValue(content, VaccTables.infection_history, R.id.infection_det);
		putValue(content, VaccTables.add_date, R.id.add_date_det);
		putValue(content, VaccTables.add_person, R.id.add_person_et);
		return content;
	}

	/***
	 * 往values加入id对应的EditText的string
	 * 
	 * @param values
	 * @param key
	 * @param id
	 */
	private void putValue(ContentValues values, String key, int id) {
		String text = getEtString(id);
		if (text != null)
			values.put(key, text);
	}

	/***
	 * 获取EditText的文本
	 * 
	 * @param id
	 * @return
	 */
	private String getEtString(int id) {
		return ((EditText) cView.findViewById(id)).getText().toString();
	}
}
