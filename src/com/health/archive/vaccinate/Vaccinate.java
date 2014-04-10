package com.health.archive.vaccinate;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import cn.younext.R;

import com.health.archive.ArchiveMain;
import com.health.archive.ArchiveMain.ActionBarEditable;
import com.health.archive.vaccinate.DialogVaccEdit.ResultTask;
import com.health.util.ListViewForScrollView;
import com.health.util.TimeHelper;

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

	private Button editHelpBtn;
	private ScrollView bodySv;
	private View focusView;

	private boolean lock = false;

	public Vaccinate() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View v = inflater.inflate(R.layout.health_archivel_accinate, null);
		initView(v);
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", 4);
	}

	private void initView(View view) {
		vaccListView = (ListViewForScrollView) view
				.findViewById(R.id.vaccinate_list_view);
		bodySv = (ScrollView) view.findViewById(R.id.table_body_sv);
		datas = initDatas();
		editHelpBtn = (Button) view.findViewById(R.id.edit_help_button);
		adapter = new VaccinateAdapter(getActivity(), datas);
		vaccListView.setAdapter(adapter);
		setListener(vaccListView);
		ArchiveMain.getInstance().setTitle("预防接种卡");
		ArchiveMain.getInstance().setButtonText("修改");
		ArchiveMain.getInstance().setActionBarEdit(new ActionBarEditable() {

			@Override
			public void processOnButton() {
				lock = !lock;
				ArchiveMain.getInstance().setLock(lock);
				setButtonText();
			}
		});
		view.setFocusable(false);

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
				focusView = parent.getChildAt(position);
				parent.setSelection(position);
				setFocus(focusView);
				startEditVacc(item, position);
				return true;
			}
		});

	}

	/***
	 * 弹出对话框，编辑疫苗记录
	 * 
	 * @param item
	 */
	protected void editVaccRecord(final String[] item) {
		AlertDialog.Builder builder = new Builder(getActivity());
		View view = inflater.inflate(R.layout.dialog_accinate_edit, null);
		EditText vaccKindET = (EditText) view.findViewById(R.id.vacc_kind);
		EditText vaccTimesET = (EditText) view.findViewById(R.id.vacc_time);
		final DatePicker vaccDateDp = (DatePicker) view
				.findViewById(R.id.vacc_date_dp);
		final EditText vaccLocationET = (EditText) view
				.findViewById(R.id.vacc_location_et);
		final EditText vaccSerialET = (EditText) view
				.findViewById(R.id.vacc_serial_et);
		final EditText vaccDoctorlET = (EditText) view
				.findViewById(R.id.vacc_doctor_et);
		final EditText vaccNoteET = (EditText) view
				.findViewById(R.id.vacc_note_et);
		vaccKindET.clearFocus();
		vaccKindET.setText(item[0]);
		vaccTimesET.setText(item[1]);
		builder.setView(view);
		builder.setTitle("填写疫苗接种记录");
		builder.setPositiveButton("保存", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				long time = vaccDateDp.getDrawingTime();
				item[2] = TimeHelper.getStringDate(time);
				item[3] = vaccLocationET.getText().toString();
				item[4] = vaccSerialET.getText().toString();
				item[5] = vaccDoctorlET.getText().toString();
				item[6] = vaccNoteET.getText().toString();
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	final ResultTask resultTask = new ResultTask() {

		@Override
		public void process(String[] item) {
			if (item[0].equals("其他疫苗")) {
				String[] other = new String[item.length];
				other[0] = "其他疫苗";
				other[1] = "1";
				datas.add(other);
			}
			adapter.notifyDataSetChanged();
			setFocus(focusView);
			// focusView.clearFocus();
			// vaccListView.setSelection(position);
		}

		@Override
		public void cancel() {
			// focusView.clearFocus();
			setFocus(focusView);
		}

	};

	private void setFocus(View view) {
		if (view != null) {
			// view.setFocusableInTouchMode(true);
			// view.setFocusable(true);
			// boolean state =
			// focusView.requestFocus();
			// L.i("setFocus",
			// "state:"+state+" focusView:"+focusView);
		}
	}

	private void startEditVacc(String[] item, int p) {
		Intent serverIntent = new Intent(getActivity(), DialogVaccEdit.class);
		DialogVaccEdit.setResultTask(resultTask);
		DialogVaccEdit.setEditItem(item);
		getActivity().startActivity(serverIntent);
	}

	private List<String[]> initDatas() {
		String[] vaccs = new String[] { "乙肝疫苗", "卡介苗", "脊灰疫苗", "百白破疫苗", "白破疫苗",
				"麻风疫苗", "麻腮风疫苗", "麻腮疫苗", "麻疹疫苗", "A群流脑疫苗", "A+C群流脑疫苗",
				"乙脑（减毒）活疫苗", "乙脑灭活疫苗", "甲肝减毒活疫苗", "甲肝灭活疫苗", "其他疫苗" };
		Integer[] times = { 3, 1, 4, 4, 1, 1, 2, 1, 2, 2, 2, 2, 4, 1, 2, 1 };
		List<String[]> datas = new ArrayList<String[]>();

		String[] title = { "疫苗", "剂次", "接种日期", "接种部位", "疫苗批号", "接种医生", "备注" };
		datas.add(title);
		for (int i = 0; i < vaccs.length; i++) {
			for (int j = 0; j < times[i]; j++) {
				String[] content = new String[title.length];
				content[0] = vaccs[i];
				content[1] = String.valueOf(j + 1);
				datas.add(content);
			}
		}
		return datas;
	}
}
