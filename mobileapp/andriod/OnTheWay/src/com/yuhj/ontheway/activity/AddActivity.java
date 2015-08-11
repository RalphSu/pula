package com.yuhj.ontheway.activity;

import android.content.Intent;
/*
 *import com.zdp.aseo.content.AseoZdpAseo;
 */
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yuhj.ontheway.R;

/**
 * @name AddActivity
 * @Descripation 发布，上传<br>
 * @author 
 * @date 2014-10-24
 * @version 1.0
 */
public class AddActivity extends FragmentActivity implements OnClickListener {
	private Button take_photo, select_picture, publish_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		initViews();
	}

	private void initViews() {
		take_photo = (Button) findViewById(R.id.take_photo);
		select_picture = (Button) findViewById(R.id.select_picture);
		publish_text = (Button) findViewById(R.id.publish_text);
		take_photo.setOnClickListener(this);
		select_picture.setOnClickListener(this);
		publish_text.setOnClickListener(this);
		/*
		 *AseoZdpAseo.initType(this, AseoZdpAseo.SCREEN_TYPE);
		 */
	}  

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		return true;
	}

	@Override
	public void onClick(View view) {
		if (view == take_photo) {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			startActivityForResult(intent, 1001);
		} else if (view == select_picture) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			this.startActivityForResult(intent, 1002);
		} else if (view == publish_text) {
			Intent intent = new Intent(AddActivity.this, MainActivity.class);
			intent.putExtra("FragmentType", 3);
			startActivity(intent);
		}

	}

}
