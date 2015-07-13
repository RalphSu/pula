package com.yuhj.ontheway.fragment;

import com.yuhj.ontheway.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @name LoginFragment
 * @Descripation 登陆的Fragment<br>
 * @author 
 * @date 2014-10-25
 * @version 1.0
 */
public class LoginFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.login, container,false);
	}

}
