package com.health.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.younext.R;

public class VisitRecordAdapter extends BaseAdapter {
	private List<String[]> datas;
	private Context context;

	public VisitRecordAdapter(Context context, List<String[]> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		String[] line = datas.get(position);
		// 只有为空时才构造
		if (convertView == null) {
			holder = new Holder();
			holder.dataET = new TextView[line.length];
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.list_item_visit_record, null);
			holder.dataET[0] = (TextView) convertView
					.findViewById(R.id.list_item_1);
			holder.dataET[1] = (TextView) convertView
					.findViewById(R.id.list_item_2);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		for (int i = 0; i < line.length; i++) {
			holder.dataET[i].setText(line[i]);
		}

		return convertView;
	}

	/***
	 * 用于暂存数据
	 * 
	 * @author jiqunpeng
	 * 
	 *         创建时间：2013-12-20 下午3:11:21
	 */
	class Holder {
		TextView[] dataET;// 显示的内容
	}
}
