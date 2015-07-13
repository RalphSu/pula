package com.yuhj.ontheway.adapter;

import java.util.ArrayList;

import com.yuhj.ontheway.R;
import com.yuhj.ontheway.bean.JingXuanData;
import com.yuhj.ontheway.bean.UserInfo;
import com.yuhj.ontheway.utils.ImageCache;

import de.hdodenhof.circleimageview.CircleImageView;
import android.R.string;
import android.content.Context;
import android.content.pm.LabeledIntent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.StaticLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JingXuanAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<JingXuanData> datas;
	private LruCache<String, Bitmap> lruCache;
	private final static String IMAGEHOME = "http://img.117go.com/timg/p308/";
	private final static String AVATAR = "http://img.117go.com/demo27/img/ava66/";

	public JingXuanAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		lruCache = ImageCache.GetLruCache(context);
	}

	public void BindData(ArrayList<JingXuanData> datas) {

		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);

	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;

		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.jingxuan_item,
					null);
			viewHolder.disCitys = (TextView) view
					.findViewById(R.id.jingxuan_txt_address);
			viewHolder.favoriteCount = (TextView) view
					.findViewById(R.id.jingxuan_favoriteCount);
			viewHolder.image = (ImageView) view
					.findViewById(R.id.jingxuan_imageView);
			viewHolder.pictureCount = (TextView) view
					.findViewById(R.id.jingxuan_txt_picCount);
			viewHolder.title = (TextView) view
					.findViewById(R.id.jingxuan_txt_title);
			viewHolder.viewCount = (TextView) view
					.findViewById(R.id.jingxuan_txt_eye);
			viewHolder.userCircleImageView = (CircleImageView) view
					.findViewById(R.id.jingxuan_user_circleImageView);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		JingXuanData jingXuanData = datas.get(arg0);
		viewHolder.favoriteCount.setText(jingXuanData.getFavoriteCount());
		viewHolder.pictureCount.setText(jingXuanData.getPictureCount());
		viewHolder.title.setText(jingXuanData.getTitle());
		viewHolder.viewCount.setText(jingXuanData.getViewCount());
		StringBuffer stringBuffer = new StringBuffer();
		if (jingXuanData.getDispCities().length > 0) {
			for (int i = 0; i < jingXuanData.getDispCities().length; i++) {
				stringBuffer.append(jingXuanData.getDispCities()[i]).append("->");
				if (i ==jingXuanData.getDispCities().length) {
					stringBuffer.append(jingXuanData.getDispCities()[i]);
						
				}

			}
			viewHolder.disCitys.setText(jingXuanData.getDispCities()[0]); // stringBuffer.toString()
		}

		viewHolder.image.setTag(IMAGEHOME + jingXuanData.getImage());
		viewHolder.image.setImageResource(R.drawable.defaultcovers);
		new ImageCache(context, lruCache, viewHolder.image, IMAGEHOME
				+ jingXuanData.getImage(), "OnTheWay", 800, 400);
		UserInfo userInfo = jingXuanData.getUserInfo();
		viewHolder.userCircleImageView.setTag(AVATAR + userInfo.getAvatar());
		new ImageCache(context, lruCache, viewHolder.userCircleImageView,
				AVATAR + userInfo.getAvatar(), "OnTheWay", 800, 400);
		return view;
	}

	private class ViewHolder {

		private CircleImageView userCircleImageView;
		public TextView pictureCount;
		public TextView favoriteCount;
		public TextView disCitys;
		public TextView viewCount;
		public ImageView image;
		public TextView title;

	}
}
