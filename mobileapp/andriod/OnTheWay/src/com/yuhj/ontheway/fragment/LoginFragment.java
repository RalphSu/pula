package com.yuhj.ontheway.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yuhj.ontheway.R;
import com.yuhj.ontheway.activity.LoginWelcomeAvtivity;
import com.yuhj.ontheway.clients.ClientApi;

public class LoginFragment extends Fragment {
    private EditText userName, password;
	private CheckBox rem_pw, auto_login;
	private Button btn_login;
	private ImageButton btnQuit;
    private String userNameValue,passwordValue;
	private SharedPreferences sp;
	private boolean status = false;
	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		
		View view =inflater.inflate(R.layout.login, container, false);
		   //获得实例对象
		sp = this.getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		userName = (EditText) view.findViewById(R.id.et_zh);
		password = (EditText) view.findViewById(R.id.et_mima);
		rem_pw = (CheckBox) view.findViewById(R.id.cb_mima);
		auto_login = (CheckBox) view.findViewById(R.id.cb_auto);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		btnQuit = (ImageButton)view.findViewById(R.id.img_btn);
				
		        
				//判断记住密码多选框的状态
		      if(sp.getBoolean("ISCHECK", true))
		        {
		    	 
		    	  //设置默认是记录密码状态
		          rem_pw.setChecked(true);
		       	  userName.setText(sp.getString("USER_NAME", ""));
		       	  password.setText(sp.getString("PASSWORD", ""));
		       	  
		       	  		       	  
		       	  //判断自动登陆多选框状态
		       	  if(sp.getBoolean("AUTO_ISCHECK", true))
		       	  {
		       		
		       		//设置默认是自动登录状态
		       		auto_login.setChecked(true);
		       		
			    	userNameValue = userName.getText().toString();
					passwordValue = password.getText().toString();
					  
			        if((userNameValue.equals(sp.getString("USER_NAME", "USER_NAME")) == true) && (passwordValue.equals(sp.getString("PASSWORD", "PASSWORD"))==true))
			        {//跳转界面
					Intent intent = new Intent(LoginFragment.this.getActivity(),LoginWelcomeAvtivity.class);
					startActivity(intent);
			        }	
		       	   }
		        }
				
			  
				btn_login.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						userNameValue = userName.getText().toString();
					    passwordValue = password.getText().toString();
					    System.out.println(userNameValue);
					    System.out.println(passwordValue);
					    new loging_action().execute();
					    				  
					}
				});

			    //监听记住密码多选框按钮事件
				rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
						if (rem_pw.isChecked()) {
		                    
							System.out.println("记住密码已选中");
							sp.edit().putBoolean("ISCHECK", true).commit();
							
						}else {
							
							System.out.println("记住密码没有选中");
							sp.edit().putBoolean("ISCHECK", false).commit();
							
						}

					}
				});
				
				//监听自动登录多选框事件
				auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
						if (auto_login.isChecked()) {
							System.out.println("自动登录已选中");
							sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

						} else {
							System.out.println("自动登录没有选中");
							sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
						}
					}
				});
				
				btnQuit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//finish();
					}
				});
       return view;
}

	
	
	 class loging_action extends AsyncTask<Void,Void,Boolean> {

	        @Override
	        protected Boolean doInBackground(Void... arg0) {
	            return ClientApi.getLoginStatus(userNameValue,passwordValue);
	        }

      
	        @Override
	        protected void onPostExecute(final Boolean result) {

	            super.onPostExecute(result);
	            if (result == true) {
	    
	            	//Toast.makeText(LoginFragment.this.getActivity(),"登录成功", Toast.LENGTH_SHORT).show();
					//登录成功和记住密码框为选中状态才保存用户信息
					if(rem_pw.isChecked())
					{
					 //记住用户名、密码、
					  Editor editor = sp.edit();
					  editor.putString("USER_NAME", userNameValue);
					  editor.putString("PASSWORD",passwordValue);
					  editor.putBoolean("ISCHECK",true);
					  editor.putBoolean("AUTO_ISCHECK",true);
					  editor.commit();
					}
					
					Intent intent = new Intent(LoginFragment.this.getActivity(),LoginWelcomeAvtivity.class);

					startActivity(intent);
					
	            } else {
	            	
	            	 Editor editor = sp.edit();
					 editor.putBoolean("ISCHECK",false);
					 editor.putBoolean("AUTO_ISCHECK",false);
					 editor.commit();
					 
	            	Toast.makeText(LoginFragment.this.getActivity(),"用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
	            }
	        }

   } 
}
 