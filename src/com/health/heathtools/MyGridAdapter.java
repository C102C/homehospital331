package com.health.heathtools;
import java.util.ArrayList;

import cn.younext.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private ArrayList<APPinfo> APPinfos;
    public MyGridAdapter(Context context, ArrayList<APPinfo> APPinfos) {
    	super();
        this.inflater = LayoutInflater.from(context);
        this.APPinfos=new ArrayList<APPinfo>();
        if(!APPinfos.isEmpty())
        	this.APPinfos.addAll(APPinfos);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return APPinfos.size();//添加按钮不能算作APP
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

        if (convertView==null) {
            holder=new ViewHolder();
            convertView=this.inflater.inflate(R.layout.grid_item, null);
            holder.iv=(ImageView) convertView.findViewById(R.id.APPicon);
            holder.tv=(TextView) convertView.findViewById(R.id.APPname);
            convertView.setTag(holder);
        }
        else {
           holder=(ViewHolder) convertView.getTag();
        }
        holder.iv.setImageDrawable(APPinfos.get(position).getAPPicon());
        holder.tv.setText(APPinfos.get(position).getAPPname());
        return convertView;
	}
	private class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}
