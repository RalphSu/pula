package com.yuhj.ontheway.activity;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.yuhj.ontheway.R;
import com.yuhj.ontheway.adapter.CourseImageDetailsAdapter;
import com.yuhj.ontheway.bean.CourseData;
import com.yuhj.ontheway.clients.ClientApi;
import com.yuhj.ontheway.utils.LoadingAinm;

@Deprecated
public class CourseDetailImageActivity extends BaseActivity {
    private StaggeredGridView gridView;
    private CourseImageDetailsAdapter adapter;
    private String searchId;
    private TextView titletTextView;
    private RelativeLayout loadRelativeLayout;
    private LinearLayout dataLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_zhuanti_detail_image_activity);
        LoadingAinm.ininLoding(CourseDetailImageActivity.this);
        gridView = (StaggeredGridView) findViewById(R.id.gridview);
        adapter = new CourseImageDetailsAdapter(getApplicationContext());
        searchId = getIntent().getStringExtra("SearchId");
        String title = getIntent().getStringExtra("name");
        titletTextView = (TextView) findViewById(R.id.zhuanti_main_title);
        titletTextView.setText(title);
        // AseoZdpAseo.initType(this, AseoZdpAseo.INSERT_TYPE);
        loadRelativeLayout = (RelativeLayout) findViewById(R.id.lodingRelativeLayout);
        dataLinearLayout = (LinearLayout) findViewById(R.id.dataLinearlayout);
        new DownLoad().execute();
    }

    class DownLoad extends AsyncTask<Void, Void, ArrayList<CourseData>> {

        protected ArrayList<CourseData> doInBackground(Void... arg0) {
            return ClientApi.getCouseDetailData(searchId);
        }

        @Override
        protected void onPostExecute(final ArrayList<CourseData> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                adapter.BindData(result);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loadRelativeLayout.setVisibility(View.GONE);
                dataLinearLayout.setVisibility(View.VISIBLE);
                gridView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        // TODO Auto-generated method stub
//                        Intent intent = new Intent(CourseDetailImageActivity.this, JingXuanDetailsActivity.class);
//                        intent.putExtra("JingXuanData", result.get(position));
//                        intent.putExtra("id", result.get(position).getTourId());
//                        startActivity(intent);
                    }
                });
            } else {
                loadRelativeLayout.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "网络异常，请检查", 0).show();
            }
        }

    }

}
