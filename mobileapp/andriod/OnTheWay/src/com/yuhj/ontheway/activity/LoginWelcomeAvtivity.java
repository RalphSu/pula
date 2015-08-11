package com.yuhj.ontheway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yuhj.ontheway.R;
import com.yuhj.ontheway.adapter.List_3Adapter;
import com.yuhj.ontheway.utils.StaticStrings;

public class LoginWelcomeAvtivity extends Activity {
	private ListView list_3;
	private List_3Adapter adapter;
	private List<HashMap<String,Object>> dataList;
	private HashMap<String,Object> map;
	SharedPreferences preference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.frame_user_page); 
		list_3=(ListView) findViewById(R.id.list_3);
		
		dataList=new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<4;i++){
			map=new HashMap<String,Object>();
			map.put("img_1", StaticStrings.img[i]);
			map.put("tx_name", StaticStrings.tx_name[i]);
			map.put("img_2", StaticStrings.img2);
			dataList.add(map);
		}
		
		adapter=new List_3Adapter(LoginWelcomeAvtivity.this,dataList);
		//向list內添加數據
		list_3.setAdapter(adapter);
		//判断本地是否保存有用户信息
		preference=getSharedPreferences(StaticStrings.PREFS_SETTINGS, MODE_PRIVATE);
		Log.i("preference=",""+ preference.toString());
		String name=preference.getString("USER_NAME", "");
		String pwd=preference.getString("PASSWORD", "");

		Log.i("preference=",""+ name);

		Log.i("preference=",""+ pwd);
		
		//list的點擊事件
        list_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent_coupon = new Intent(LoginWelcomeAvtivity.this, UserInfoActivity.class);
                    startActivity(intent_coupon);
                } else if (position == 1) {
                    Intent intent_coupon = new Intent(LoginWelcomeAvtivity.this, MainActivity.class);
                    startActivity(intent_coupon);
                } else if (position == 2) {
                    Intent intent_mycollect = new Intent(LoginWelcomeAvtivity.this, MainActivity.class);
                    startActivity(intent_mycollect);
                } else if (position == 3) {
                    Intent intent_mycollect = new Intent(LoginWelcomeAvtivity.this, MainActivity.class);
                    startActivity(intent_mycollect);
                }
            }
        });
		
	}

}
