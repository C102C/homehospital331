package com.health.viewUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import cn.younext.R;

import com.health.util.L;
import com.health.util.ListViewForScrollView;

/**
 * 单选框的edittext,当有"其他"项时，且其他项放在最后时，其他项可以编辑,
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-15 下午3:42:32
 */

public class SingleChoiceEditText extends ChoiceEditText {

	public SingleChoiceEditText(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public SingleChoiceEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public SingleChoiceEditText(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (items != null)// item没有填充前不能初始化对话框
			new SelectDialog(context).build().show();
		else
			L.i("MultiChoiceEditText", this + "没有设置items");
		return false;
	}

	private class SelectDialog extends AlertDialog.Builder {
		private ListViewForScrollView listview;
		private TextView otherTv;
		private EditText otherEt;
		private CheckBox otherCb;
		private Context mContext;

		public SelectDialog(Context context) {
			super(context);
			mContext = context;
		}

		public SelectDialog build() {
			setView(initDialogContent());
			setTitle("请选择");
			setPositiveButton("确认", getPositiveListener());
			setNegativeButton("取消", null);
			return this;
		}

		private android.content.DialogInterface.OnClickListener getPositiveListener() {
			return new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String selected = getSelectedString();
					setText(selected);
				}
			};
		}

		/**
		 * 获取选中数据
		 * 
		 * @return
		 */
		private String getSelectedString() {
			SparseBooleanArray array = listview.getCheckedItemPositions();
			for (int i = 0; i < array.size(); i++) {
				int position = array.keyAt(i);
				if (array.get(position)) {// 被选中
					return (String) listview.getItemAtPosition(position);
				}
			}
			if (other != null && otherCb.isChecked()) {// 其他项存在且被选中了
				StringBuilder builder = new StringBuilder();
				builder.append(other);
				Editable otherStr = otherEt.getText();
				if (otherStr != null && otherStr.length() > 0) {
					builder.append("(");
					builder.append(otherStr);
					builder.append(")");
				}
				return builder.toString();
			}
			return "";
		}

		/***
		 * 初始化内容
		 * 
		 * @return
		 */
		private View initDialogContent() {
			View root = LayoutInflater.from(mContext).inflate(
					R.layout.other_item, null);
			listview = (ListViewForScrollView) root
					.findViewById(R.id.select_list_view);
			listview.setAdapter(new ArrayAdapter<String>(mContext,
					android.R.layout.simple_list_item_multiple_choice, items));
			listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 如果不使用这个设置，选项中的radiobutton无法响应选中事件
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (otherCb != null && otherCb.isChecked())// 当其他项选中时
						otherCb.setChecked(false);
					listview.setItemChecked(position, true);
				}
			});

			if (other != null) {// 需要显示其他这一行
				root.findViewById(R.id.other_layout)
						.setVisibility(View.VISIBLE);
				otherTv = (TextView) root.findViewById(R.id.other_tv);
				otherTv.setText(other);
				otherEt = (EditText) root.findViewById(R.id.other_et);
				otherCb = (CheckBox) root.findViewById(R.id.other_cb);
				otherEt.setOnTouchListener(new OnTouchListener() {// 填写其他就默认选中

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						setOtherSelected(true);
						return false;
					}
				});
				otherCb.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						L.i("otherCb.isChecked():", otherCb.isChecked() + "");
						setOtherSelected(otherCb.isChecked());
						L.i("setOtherSelected:", otherCb.isChecked() + "");
					}
				});
			} else
				root.findViewById(R.id.other_layout).setVisibility(
						View.GONE);
			setSelectedItem();
			return root;
		}

		/***
		 * 设置被选中的项
		 */
		private void setSelectedItem() {
			String text = getText().toString();
			String selectedItem = (text == null) ? null : text;
			if (selectedItem != null) {
				if (other != null && selectedItem.contains(other)) {
					otherCb.setChecked(true);
					if (selectedItem.contains("(")// 显示添加的部分
							&& selectedItem.contains(")")) {
						otherEt.setText(selectedItem.split("\\(")[1]
								.split("\\)")[0]);
					}
				} else {
					for (int i = 0; i < listview.getCount(); i++) {
						String item = (String) listview.getItemAtPosition(i);
						if (selectedItem.equals(item)) {
							listview.setItemChecked(i, true);
							return;
						}
					}
				}
			}
		}

		private void setOtherSelected(boolean state) {
			if (listview.getCheckedItemCount() > 0) {// 将listview的选中清除掉
				for (int i = 0; i < listview.getCount(); i++)
					listview.setItemChecked(i, false);
			}
			otherCb.setChecked(state);
		}
	}

}
