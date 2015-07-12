package com.yuhj.ontheway.activity;

import com.zdp.aseo.content.AseoZdpAseo;

import android.app.Activity;

public class BaseActivity extends Activity
{

	@Override
	protected void onPause()
	{
		super.onPause();
		AseoZdpAseo.initType(this, AseoZdpAseo.INSERT_TYPE);
		AseoZdpAseo.initType(this, AseoZdpAseo.SCREEN_TYPE);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

}
