package com.yuhj.ontheway.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
/*
*import com.zdp.aseo.content.AseoZdpAseo;
*/
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuhj.ontheway.R;
import com.yuhj.ontheway.adapter.JingXuanDetailsAdapter;
import com.yuhj.ontheway.bean.JingXuanData;
import com.yuhj.ontheway.bean.JingxuanDetailData;
import com.yuhj.ontheway.clients.ClientApi;
import com.yuhj.ontheway.utils.ImageCache;
import com.yuhj.ontheway.utils.LoadingAinm;

/**
 * @name JingXuanDetailsActivity
 * @Descripation <br>
 *               1、<br>
 *               2、<br>
 *               3、<br>
 * @author 
 * @date 2014-10-23
 * @version 1.0
 */
public class JingXuanDetailsActivity extends BaseActivity {
	private ImageView mainImage;
	private TextView mainText;
	private ListView listView;
	private JingXuanDetailsAdapter adapter;
	private LruCache<String, Bitmap> lruCache;
	private RelativeLayout loadrRelativeLayout;
	private LinearLayout dataLinearLayout;
	private Button viewCount, commentButton, shareButton;
	private String startId;
	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_jing_xuan_details);
		initViews();
		new DownData().execute();

	}

	private void initViews() {
		View headerView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.jing_xuan_details_header, null);
		mainImage = (ImageView) headerView
				.findViewById(R.id.jingxuan_detail_main_image);
		mainText = (TextView) headerView
				.findViewById(R.id.jingxuan_detail_main_txt);
		listView = (ListView) findViewById(R.id.jingxuan_detail_listview);
		commentButton = (Button) findViewById(R.id.CommentCount);
		shareButton = (Button) findViewById(R.id.share);
		viewCount = (Button) findViewById(R.id.likeCount);
		adapter = new JingXuanDetailsAdapter(getApplicationContext());
		dataLinearLayout = (LinearLayout) findViewById(R.id.list);
		LoadingAinm.ininLoding(JingXuanDetailsActivity.this);
		title = (TextView) findViewById(R.id.detail_main_title);
		loadrRelativeLayout = (RelativeLayout) findViewById(R.id.lodingRelativeLayout);
		Intent intent = getIntent();
		final JingXuanData jingXuanData = (JingXuanData) intent
				.getSerializableExtra("JingXuanData");
		lruCache = ImageCache.GetLruCache(getApplicationContext());
		mainImage.setTag("http://img.117go.com/timg/p308/"
				+ jingXuanData.getImage());
		new ImageCache(getApplicationContext(), lruCache, mainImage,
				"http://img.117go.com/timg/p308/" + jingXuanData.getImage(),
				"OnTheway", 800, 400);
		/*
		 *AseoZdpAseo.initType(this, AseoZdpAseo.INSERT_TYPE);
		 */
		mainText.setText(jingXuanData.getForeword());
		listView.addHeaderView(headerView);
		startId = intent.getStringExtra("id");
		title.setText(jingXuanData.getTitle());
		viewCount.setText(jingXuanData.getViewCount());
		commentButton.setText(jingXuanData.getCmtCount());
		shareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 跳转到第三方分享界面
				Intent intent = new Intent(JingXuanDetailsActivity.this,
						ShareActivity.class);
				intent.putExtra("shareContent", jingXuanData.getForeword());
				startActivity(intent);
			}
		});

	}

	class DownData extends AsyncTask<Void, Void, ArrayList<JingxuanDetailData>> {

		@Override
		protected ArrayList<JingxuanDetailData> doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			return ClientApi.getJingxuanDetailDatas(startId);
		}

		@Override
		protected void onPostExecute(ArrayList<JingxuanDetailData> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result == null) {
				loadrRelativeLayout.setVisibility(View.GONE);
				Toast.makeText(JingXuanDetailsActivity.this, "网络异常,请检查！", 1)
						.show();
			} else {
				adapter.BindData(result);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				loadrRelativeLayout.setVisibility(View.GONE);
				dataLinearLayout.setVisibility(View.VISIBLE);
			}

		}

	}

}
