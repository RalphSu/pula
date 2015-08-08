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

import com.yuhj.ontheway.HuodongDetailActivity;
import com.yuhj.ontheway.R;
import com.yuhj.ontheway.adapter.HuoDongAdapter;
import com.yuhj.ontheway.bean.HuoDongData;
import com.yuhj.ontheway.clients.ClientApi;
import com.yuhj.ontheway.utils.LoadingAinm;

//import com.zdp.aseo.content.AseoZdpAseo;

/**
 * @name HuodongFragment
 * @Descripation 这是活动的是实体类<br>
 * @author
 * @date 2014-10-24
 * @version 1.0
 */
public class HuodongFragment extends Fragment {
    private ListView listView;
    private HuoDongAdapter adapter;
    private RelativeLayout loadRelativeLayout;
    private LinearLayout dataLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.huodong_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.hongdong_listview);
        adapter = new HuoDongAdapter(getActivity());
        new DownData().execute();
        loadRelativeLayout = (RelativeLayout) view.findViewById(R.id.lodingRelativeLayout);
        dataLinearLayout = (LinearLayout) view.findViewById(R.id.huodongLinearlayout);
        LoadingAinm.ininLodingView(view);
        return view;
    }

    class DownData extends AsyncTask<Void, Void, ArrayList<HuoDongData>> {

        @Override
        protected ArrayList<HuoDongData> doInBackground(Void... arg0) {
            return ClientApi.getHuoDongList();
        }

        @Override
        protected void onPostExecute(final ArrayList<HuoDongData> result) {

            super.onPostExecute(result);
            if (result != null) {
                adapter.BindData(result);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loadRelativeLayout.setVisibility(View.GONE);
                dataLinearLayout.setVisibility(View.VISIBLE);
                listView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Intent intent = new Intent(getActivity(), HuodongDetailActivity.class);
                        intent.putExtra("id", result.get(position).getId());
                        intent.putExtra("url", result.get(position).getUrlS());
                        intent.putExtra("name", result.get(position).getTitle()); 
                        startActivity(intent);
                    }
                });
            } else {
                loadRelativeLayout.setVisibility(View.GONE);
                Toast.makeText(HuodongFragment.this.getActivity(), "网络异常,请检查", 1).show();
            }
        }

    }

}
