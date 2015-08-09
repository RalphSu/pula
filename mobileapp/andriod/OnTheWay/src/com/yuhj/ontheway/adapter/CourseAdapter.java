package com.yuhj.ontheway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuhj.ontheway.R;
import com.yuhj.ontheway.bean.CourseData;
import com.yuhj.ontheway.utils.ImageCache;

/**
 * @name ZhuantiAdapter
 * @Descripation 专题<br>
 * @author 禹慧军
 * @date 2014-10-24
 * @version 1.0
 */
public class CourseAdapter extends BaseAdapter {
    private ArrayList<CourseData> data;
    private Context context;
    private LruCache<String, Bitmap> lruCache;

    public void BindData(ArrayList<CourseData> data) {

        this.data = data;
    }

    public CourseAdapter(Context context) {
        this.context = context;
        lruCache = ImageCache.GetLruCache(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        return data.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        ViewHoleder viewHoleder = null;
        if (view == null) {
            viewHoleder = new ViewHoleder();
            view = LayoutInflater.from(context).inflate(R.layout.zhuanti_item, null);
            viewHoleder.imageView = (ImageView) view.findViewById(R.id.course_imageView);
            viewHoleder.titleView = (TextView) view.findViewById(R.id.course_item_title);
            viewHoleder.classRoomName = (TextView) view.findViewById(R.id.course_classroomname);
            viewHoleder.branchName = (TextView) view.findViewById(R.id.course_branchName);
            view.setTag(viewHoleder);
        } else {
            viewHoleder = (ViewHoleder) view.getTag();
        }
        CourseData zhuantiData = data.get(position);
        viewHoleder.imageView.setImageResource(R.drawable.defaultcovers);
        viewHoleder.imageView.setTag(zhuantiData.getImage());
        viewHoleder.titleView.setText(zhuantiData.getName());
        viewHoleder.branchName.setText(zhuantiData.getBranchName());
        viewHoleder.classRoomName.setText(zhuantiData.getClassRoomName());
        new ImageCache(context, lruCache, viewHoleder.imageView, zhuantiData.getImage(), "OnTheWay", 800, 400);

        return view;
    }

    private class ViewHoleder {
        public ImageView imageView;
        public TextView titleView;
        public TextView classRoomName;
        public TextView branchName;

    }

}
