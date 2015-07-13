package com.yuhj.ontheway.adapter;

import java.util.ArrayList;

import com.yuhj.ontheway.R;
import com.yuhj.ontheway.bean.HuoDongData;
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
public class HuoDongAdapter extends BaseAdapter {
	private ArrayList<HuoDongData> data;
	private Context context;
	private LruCache<String,Bitmap> lruCache;
	public void BindData(ArrayList<HuoDongData> data){
		
		this.data = data;
	}
	
	public HuoDongAdapter(Context context){
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
		HuoDongData huoDongData =data.get(position);
		viewHoleder.imageView.setImageResource(R.drawable.defaultcovers);
		viewHoleder.imageView.setTag(huoDongData.getIamge());
		new ImageCache(context, lruCache, viewHoleder.imageView, huoDongData.getIamge(),"OnTheWay",800, 400);
		
		return view;
	}
	
	private class ViewHoleder{
		public ImageView imageView;
		
	}

}
