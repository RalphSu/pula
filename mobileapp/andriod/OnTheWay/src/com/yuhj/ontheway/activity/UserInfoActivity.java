package com.yuhj.ontheway.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yuhj.ontheway.R;
import com.yuhj.ontheway.bean.UserInfoData;
import com.yuhj.ontheway.clients.ClientApi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class UserInfoActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
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
			userInfo = ClientApi.getUserInfoData("JQ000011", "student");
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
