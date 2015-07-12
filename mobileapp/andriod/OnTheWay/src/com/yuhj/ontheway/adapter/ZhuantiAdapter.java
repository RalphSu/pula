package com.yuhj.ontheway.adapter;

import java.util.ArrayList;

import com.yuhj.ontheway.R;
import com.yuhj.ontheway.bean.ZhuanTiData;
import com.yuhj.ontheway.utils.ImageCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @name ZhuantiAdapter
 * @Descripation 专题<br>
 * @author 禹慧军
 * @date 2014-10-24
 * @version 1.0
 */
public class ZhuantiAdapter extends BaseAdapter {
	private ArrayList<ZhuanTiData> data;
	private Context context;
	private LruCache<String,Bitmap> lruCache;
	public void BindData(ArrayList<ZhuanTiData> data){
		
		this.data = data;
	}
	
	public ZhuantiAdapter(Context context){
		this.context=context;
		lruCache = ImageCache.GetLruCache(context);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHoleder viewHoleder =null;
		if (view==null) {
			viewHoleder =new ViewHoleder();
			view = LayoutInflater.from(context).inflate(R.layout.zhuanti_item, null);
			viewHoleder.imageView=(ImageView) view.findViewById(R.id.zhuanti_main_image);
			view.setTag(viewHoleder);
		}else {
			viewHoleder = (ViewHoleder) view.getTag();
		}
		ZhuanTiData zhauntiData =data.get(position);
		viewHoleder.imageView.setImageResource(R.drawable.defaultcovers);
		viewHoleder.imageView.setTag(zhauntiData.getIamge());
		new ImageCache(context, lruCache, viewHoleder.imageView, zhauntiData.getIamge(),"OnTheWay",800, 400);
		
		return view;
	}
	
	private class ViewHoleder{
		public ImageView imageView;
		
	}

}
