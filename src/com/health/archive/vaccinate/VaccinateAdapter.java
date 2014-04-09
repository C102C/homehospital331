package com.health.archive.vaccinate;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.younext.R;

/***
 * 疫苗接种表格适配器
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-8 下午6:40:28
 */
public class VaccinateAdapter extends BaseAdapter {
	private List<String[]> datas;
	private Context context;
	LayoutInflater inflater;
	private static int[] ids = new int[] { R.id.list_item_1, R.id.list_item_2,
			R.id.list_item_3, R.id.list_item_4, R.id.list_item_5,
			R.id.list_item_6, R.id.list_item_7 };

	public VaccinateAdapter(Context context, List<String[]> datas) {
		this.context = context;
		this.datas = datas;
		inflater = LayoutInflater.from(this.context);
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
			holder.dataTv = new TextView[line.length];
			convertView = inflater
					.inflate(R.layout.list_item_seven_cloum, null);
			for (int i = 0; i < line.length; i++)
				holder.dataTv[i] = (TextView) convertView.findViewById(ids[i]);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		for (int i = 0; i < line.length; i++) {
			holder.dataTv[i].setText(line[i]);
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
		TextView[] dataTv;// 显示的内容

	}
}
