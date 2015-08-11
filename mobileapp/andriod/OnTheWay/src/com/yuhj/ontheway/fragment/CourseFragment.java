package com.yuhj.ontheway.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yuhj.ontheway.R;
import com.yuhj.ontheway.activity.CourseDetailH5Activity;
import com.yuhj.ontheway.adapter.CourseAdapter;
import com.yuhj.ontheway.bean.CourseData;
import com.yuhj.ontheway.clients.ClientApi;
import com.yuhj.ontheway.utils.LoadingAinm;

/**
 * @name ZhuanTiFragment
 * @Descripation 这是关于<br>
 * @author
 * @date 2014-10-24
 * @version 1.0
 */
public class CourseFragment extends Fragment {
    private ListView image_listview;
    private CourseAdapter image_adaAdapter;
    private RelativeLayout loadRelativeLayout;
    private LinearLayout dataLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhuanti_fragment, container, false);
        LoadingAinm.ininLodingView(view);
        initViews(view);
        new Downdata().execute();
        return view;
    }

    private void initViews(View view) {
        image_listview = (ListView) view.findViewById(R.id.course_listview);
        image_adaAdapter = new CourseAdapter(getActivity());
        loadRelativeLayout = (RelativeLayout) view.findViewById(R.id.lodingRelativeLayout);
        dataLinearLayout = (LinearLayout) view.findViewById(R.id.courseLinearlayout);
    }

    class Downdata extends AsyncTask<Void, Void, ArrayList<CourseData>> {

        @Override
        protected ArrayList<CourseData> doInBackground(Void... arg0) {
            return ClientApi.getCourseDatas();
        }

        @Override
        protected void onPostExecute(final ArrayList<CourseData> result) {
            super.onPostExecute(result);
            if (result != null) {
                image_adaAdapter.BindData(result);
                image_listview.setAdapter(image_adaAdapter);
                image_adaAdapter.notifyDataSetChanged();
                image_listview.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Intent intent = new Intent(getActivity(), CourseDetailH5Activity.class);
                        intent.putExtra("SearchId", result.get(position).getId());
                        intent.putExtra("name", result.get(position).getName());
                        startActivity(intent);
                    }
                });
                dataLinearLayout.setVisibility(View.VISIBLE);
                loadRelativeLayout.setVisibility(View.GONE);
            } else {
                loadRelativeLayout.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "网络异常", 0).show();
            }
        }

    }

}
