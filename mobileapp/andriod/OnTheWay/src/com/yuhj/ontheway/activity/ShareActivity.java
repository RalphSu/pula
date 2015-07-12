package com.yuhj.ontheway.activity;
import java.util.HashMap;

import com.yuhj.ontheway.R;



import com.zdp.aseo.content.AseoZdpAseo;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.qzone.QZoneWebShareAdapter;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.moments.WechatMoments;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ShareActivity extends BaseActivity implements OnClickListener{
	private ImageView Sharekaixin,Shareqq,Shareqzone,
	Sharerenren,Sharesinaweibo,Sharetencentweibo,
	Sharewechat,Sharewechatmoment;
	private String sharecontent;
	private Platform tecentplatform;
	private Platform sinaplatform;
	private Platform renrenplatform;
	private Platform qzoneplatform;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_share);
		ShareSDK.initSDK(getApplicationContext());
		Intent intent =getIntent();
		
		sharecontent=intent.getStringExtra("shareContent");
		initbutton();
		buttonSetonclick();
		
		AseoZdpAseo.initType(this, AseoZdpAseo.INSERT_TYPE);
		tecentplatform = ShareSDK.getPlatform(TencentWeibo.NAME);
		sinaplatform = ShareSDK.getPlatform(SinaWeibo.NAME);
		renrenplatform = ShareSDK.getPlatform(Renren.NAME);
		qzoneplatform = ShareSDK.getPlatform(QZone.NAME);
		setPlatformlistener(tecentplatform, sinaplatform,renrenplatform,qzoneplatform);
		
	}

	private void buttonSetonclick() {
		Sharekaixin.setOnClickListener(this);
		Shareqq.setOnClickListener(this);
		Shareqzone.setOnClickListener(this);
		Sharerenren.setOnClickListener(this);
		Sharesinaweibo.setOnClickListener(this);
		Sharetencentweibo.setOnClickListener(this);
		Sharewechat.setOnClickListener(this);
		Sharewechatmoment.setOnClickListener(this);
	}

	private void setPlatformlistener(final Platform tecentplatform,
			final Platform sinaplatform,
			final Platform renrenPlatform,
			final Platform qzonePlatform) {
		tecentplatform.setPlatformActionListener(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				TencentWeibo.ShareParams shareParams=new TencentWeibo.ShareParams();
				shareParams.setText(sharecontent);
				tecentplatform.share(shareParams);
				Toast.makeText(getApplicationContext(), "分享成功", 1).show();
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		sinaplatform.setPlatformActionListener(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				SinaWeibo.ShareParams shareParams=new SinaWeibo.ShareParams();
				shareParams.setText(sharecontent);
				sinaplatform.share(shareParams);
				Toast.makeText(getApplicationContext(), "分享成功", 1).show();
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		renrenPlatform.setPlatformActionListener(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				Renren.ShareParams shareParams=new Renren.ShareParams();
				shareParams.setText(sharecontent);
				renrenPlatform.share(shareParams);
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		qzonePlatform.setPlatformActionListener(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				QZone.ShareParams shareParams=new QZone.ShareParams();
				shareParams.setText(sharecontent);
				qzonePlatform.share(shareParams);
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initbutton() {
		Sharekaixin=(ImageView)this.findViewById(R.id.share_kaixin);
		Shareqq=(ImageView)this.findViewById(R.id.share_qq);
		Shareqzone=(ImageView)this.findViewById(R.id.share_qzone);
		Sharerenren=(ImageView)this.findViewById(R.id.share_renren);
		Sharesinaweibo=(ImageView)this.findViewById(R.id.share_sina);
		Sharetencentweibo=(ImageView)this.findViewById(R.id.share_tecent);
		Sharewechat=(ImageView)this.findViewById(R.id.share_wechat);
		Sharewechatmoment=(ImageView)this.findViewById(R.id.share_wechatmoment);
	}

	@Override
	public void onClick(View v) {
		// 授权
		switch (v.getId()) {
		case R.id.share_tecent:
			tecentplatform.authorize();
			finish();
			break;

		case R.id.share_sina:
			sinaplatform.authorize();
			finish();
			break;
		case R.id.share_renren:
			renrenplatform.authorize();
			finish();
			break;
		case R.id.share_kaixin:
			Toast.makeText(getApplicationContext(), "本接口暂未实现，敬请期待", 1).show();
			finish();
			break;
		case R.id.share_qq:
			Toast.makeText(getApplicationContext(), "本接口暂未实现，敬请期待", 1).show();
			finish();
			break;
		case R.id.share_qzone:
			qzoneplatform.authorize();
			finish();
			//Toast.makeText(getApplicationContext(), "本接口暂未实现，敬请期待", 1).show();
			break;
		case R.id.share_wechat:
			Toast.makeText(getApplicationContext(), "本接口暂未实现，敬请期待", 1).show();
			finish();
			break;
		case R.id.share_wechatmoment:
			//wechatmomentPlatform.authorize();
			Toast.makeText(getApplicationContext(), "本接口暂未实现，敬请期待", 1).show();
			finish();
			break;
		}
	}





}
