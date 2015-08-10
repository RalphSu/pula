package com.yuhj.ontheway.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.yuhj.ontheway.R;
import com.yuhj.ontheway.bean.UserInfoData;
import com.yuhj.ontheway.clients.ClientApi;
import com.yuhj.ontheway.utils.StaticStrings;

public class UserInfoActivity extends Activity {
	private SharedPreferences preference;
	private String userName;
	private String passWord;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		
		preference=getSharedPreferences(StaticStrings.PREFS_SETTINGS, MODE_PRIVATE);
		userName=preference.getString("USER_NAME", "");
		passWord=preference.getString("PASSWORD", "");

		Log.i("preference=",""+ userName);

		Log.i("preference=",""+ passWord);
		
		if((userName == null )||(passWord == null))
		{
			Toast.makeText(UserInfoActivity.this,"请重新登录", Toast.LENGTH_LONG).show();
			Intent main =new Intent(UserInfoActivity.this,MainActivity.class);
			startActivity(main);
		}
		
		new Thread(runnable).start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			Bundle data = msg.getData();
			StringBuffer buffer = new StringBuffer();

			buffer.append("用户编号： ").append(data.getString("No")).append("\n");
			buffer.append("用户姓名： ").append(data.getString("Name")).append("\n");
			buffer.append("用户积分： ").append(data.getInt("Points")).append("\n");
			buffer.append("用户生日： ").append(data.getInt("Birthday")).append("\n");
			buffer.append("家长姓名： ").append(data.getString("parentName")).append("\n");
			buffer.append("联系电话： ").append(data.getInt("Phone")).append("\n");

			TextView textView = (TextView) findViewById(R.id.textView);
			textView.setText(buffer.toString());

		}
	};

	Runnable runnable = new Runnable() {
		@Override
		public void run() {

			UserInfoData userInfo = new UserInfoData();
			

			userInfo = ClientApi.getUserInfoData(userName, passWord);
			Message msg = new Message();
			Bundle data = new Bundle();

			data.putString("No", userInfo.getNo());
			data.putString("Name", userInfo.getName());
			data.putInt("Points", userInfo.getPoints());
			data.putInt("Birthday", userInfo.getBirthday());
			data.putString("parentName", userInfo.getParentName());
			data.putInt("Phone", userInfo.getPhone());

			msg.setData(data);
			handler.sendMessage(msg);
		}
	};

}
