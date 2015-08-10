package com.yuhj.ontheway.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.yuhj.ontheway.R;

public class LogoActivity extends Activity {
	private ProgressBar progressBar;
	private Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.logo);

		progressBar = (ProgressBar) findViewById(R.id.pgBar);
		backButton = (Button) findViewById(R.id.btn_back);
		progressBar.setMax(3000);

		Intent intent = new Intent(this, LoginWelcomeAvtivity.class);
		LogoActivity.this.startActivity(intent);

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}
	
}
