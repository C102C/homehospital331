package com.health.util;

import java.util.List;

import com.health.web.WebService;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

/**
 * 每一行14个元素的listview
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2013-11-28 上午9:20:40
 */
public class BigListAdapter extends BaseAdapter {
	public static final int TITLE = 0;// 标题栏不显示按钮
	public static final int UPLOADED = 1;// 已经上传，按钮不可点击
	public static final int UNUPLOAD = 2;// 显示按钮
	private List<String[]> datas;
	private List<Boolean> isTitle;// 是否是标题，
	private int dataNum;// datas.get(i)[dataNum-1]一定是上传状态
	private Context mContext;

	public BigListAdapter(Context context, List<String[]> datas,
			List<Boolean> isTitle, int dataNum) {
		this.datas = datas;
		this.mContext = context;
		this.isTitle = isTitle;
		this.dataNum = dataNum;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		String[] line = datas.get(position);
		if (convertView == null) {
			holder = new Holder();
			holder.dataET = new TextView[dataNum];
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_item_big, null);
			holder.dataET[0] = (TextView) convertView
					.findViewById(R.id.list_data_1);
			holder.dataET[1] = (TextView) convertView
					.findViewById(R.id.list_data_2);
			holder.dataET[2] = (TextView) convertView
					.findViewById(R.id.list_data_3);
			holder.dataET[3] = (TextView) convertView
					.findViewById(R.id.list_data_4);
			holder.dataET[4] = (TextView) convertView
					.findViewById(R.id.list_data_5);
			holder.dataET[5] = (TextView) convertView
					.findViewById(R.id.list_data_6);
			holder.dataET[6] = (TextView) convertView
					.findViewById(R.id.list_data_7);
			holder.dataET[7] = (TextView) convertView
					.findViewById(R.id.list_data_8);
			holder.dataET[8] = (TextView) convertView
					.findViewById(R.id.list_data_9);
			holder.dataET[9] = (TextView) convertView
					.findViewById(R.id.list_data_10);
			holder.dataET[10] = (TextView) convertView
					.findViewById(R.id.list_data_11);
			holder.dataET[11] = (TextView) convertView
					.findViewById(R.id.list_data_12);
			holder.dataET[12] = (TextView) convertView
					.findViewById(R.id.list_data_13);
			holder.button = (Button) convertView
					.findViewById(R.id.list_upload_button);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		for (int i = 0; i < dataNum; i++) {
			holder.dataET[i].setText(line[i]);
		}
		if (WebService.UPLOADED.equals(line[dataNum - 1])) {
			holder.button.setEnabled(false);
		} else {
			holder.button.setEnabled(true);
		}
		if (isTitle.get(position))
			holder.button.setVisibility(View.INVISIBLE);

		addListener(convertView);
		return convertView;
	}

	private void addListener(View convertView) {
		((Button) convertView.findViewById(R.id.list_upload_button))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (v.isEnabled() == true)
							v.setEnabled(false);
						Toast.makeText(mContext, "开始上传", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	class Holder {
		TextView[] dataET;
		Button button;
	}
}
