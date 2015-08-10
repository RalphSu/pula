package com.yuhj.ontheway.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.yuhj.ontheway.activity.JingXuanDetailsActivity;
import com.yuhj.ontheway.adapter.JingXuanAdapter;
import com.yuhj.ontheway.bean.JingXuanData;
import com.yuhj.ontheway.clients.ClientApi;
import com.yuhj.ontheway.utils.LoadingAinm;
import com.yuhj.ontheway.widget.PullToRefreshLayout;
import com.yuhj.ontheway.widget.PullToRefreshLayout.OnRefreshListener;

/**
 * @name JingXuanFragment
 * @Descripation 专题界面的设计<br>
 *		1、<br>
 *		2、<br>
 *      3、<br>
 * @author 
 * @date 2014-10-23
 * @version 1.0
 */
@Deprecated
public class ZhuantiDetailFragment extends Fragment implements OnRefreshListener {
	private ListView listView;
	private JingXuanAdapter adapter;
	private PullToRefreshLayout pullToLoadManager;
	private PullToRefreshLayout pullToRefreshManager;
	private static final int INIT=0;
	private static final int REFRESH = 1;
	private static final int LOAD = 2;
	private int offset = 20;
	private RelativeLayout loadrRelativeLayout;
	private LinearLayout dataLinearLayout;
	private int location=1;
	private ArrayList<JingXuanData> list =new ArrayList<JingXuanData>();
	private final  String REFRESH_URL = "http://app.117go.com/demo27/php/plaza.php?submit=getPlaza4&startId=0&fetchNewer=1&length=20&type=0&isWaterfall=0&token=&v=a5.0.4&vc=anzhi&vd=f2e4ee47505f6fba"; 
  
	private Handler getDateHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg){
			if (msg.obj == null){
			loadrRelativeLayout.setVisibility(View.GONE);
			Toast.makeText(getActivity(), "网络异常,请检查！", 1).show();
			
		} else {
			if (msg.what == INIT){
				loadrRelativeLayout.setVisibility(View.GONE);
				dataLinearLayout.setVisibility(View.VISIBLE);
				list.clear();
				list = (ArrayList<JingXuanData>) msg.obj;
				adapter.BindData(list);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
			if (msg.what == REFRESH) {
				
				list.clear();
				list =(ArrayList<JingXuanData>) msg.obj;
				adapter.BindData(list);
				listView.setAdapter(adapter);
				pullToRefreshManager
				.refreshFinish(PullToRefreshLayout.SUCCEED);
				adapter.notifyDataSetChanged();
			}
			if (msg.what == LOAD) {
				list.addAll((ArrayList<JingXuanData>) msg.obj);
				adapter.BindData(list);
				listView.setAdapter(adapter);
				listView.setSelection(offset);
				pullToLoadManager
				.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				adapter.notifyDataSetChanged();
			}
		}
	}
};
	private String id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		id = getArguments().getString("id");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.jingxuan_fragment, container, false);
		initViews(view);
		((PullToRefreshLayout) view.findViewById(R.id.refresh_view))
		.setOnRefreshListener(this);
		init();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(getActivity(),JingXuanDetailsActivity.class);
				intent.putExtra("JingXuanData", list.get(position));
				intent.putExtra("id",list.get(position).getTourId());
				startActivity(intent);
			}
		});
		return view;
	}
	
	private void init(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				msg.obj = ClientApi.getCouseDetailData(id);
				msg.what = INIT;
				getDateHandler.sendMessage(msg);
			}
		}).start();
	}
	

	private void initViews(View view) {
		listView=(ListView) view.findViewById(R.id.jingxuan_listview);
		adapter=new JingXuanAdapter(getActivity());
		dataLinearLayout=(LinearLayout) view.findViewById(R.id.jingxuanLinearlayout);
		loadrRelativeLayout=(RelativeLayout) view.findViewById(R.id.lodingRelativeLayout);
		LoadingAinm.ininLodingView(view);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		pullToRefreshManager=pullToRefreshLayout;
		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = Message.obtain();
				msg.obj = ClientApi.getCouseDetailData(id);
				msg.what = REFRESH;
				getDateHandler.sendMessage(msg);
			}
		}).start();
		
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		pullToLoadManager=pullToRefreshLayout;
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				String LOAD_URL="http://app.117go.com/demo27/php/plaza.php?submit=getPlaza4&startId="+ClientApi.getStartId()+"&fetchNewer=0&length=20&type=0&isWaterfall=0&token=&v=a5.0.4&vc=anzhi&vd=f2e4ee47505f6fba";
				msg.obj = ClientApi.getJingXuanData(LOAD_URL);
				location+=10;
				msg.what = LOAD;
				getDateHandler.sendMessage(msg);
			}
		}).start();
	}

}
