package com.yuhj.ontheway.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuhj.ontheway.R;
import com.yuhj.ontheway.utils.StaticStrings;

public class List_3Adapter extends BaseAdapter{
	private List<HashMap<String,Object>> list;
	private Context context;
	private LayoutInflater inflater;
	public List_3Adapter(Context context, List<HashMap<String,Object>> dataList){
		this.context=context;
		this.list=dataList;
		inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.list_user_page, null);
			holder.img_1=(ImageView) convertView.findViewById(R.id.img_1);
			holder.img_2=(ImageView) convertView.findViewById(R.id.img_2);
			holder.tx_name=(TextView) convertView.findViewById(R.id.tx_name);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		//传值
		holder.img_1.setBackgroundResource(StaticStrings.img[position]);
		holder.tx_name.setText(StaticStrings.tx_name[position]);
		holder.img_2.setBackgroundResource(R.drawable.upomp_bypay_icon_jiantou);
		return convertView;
	}
	public class ViewHolder{
		ImageView img_1,img_2;
		TextView tx_name;
	}
}
