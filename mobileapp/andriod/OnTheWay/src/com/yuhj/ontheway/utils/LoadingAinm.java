package com.yuhj.ontheway.utils;

import com.yuhj.ontheway.R;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
public class LoadingAinm {
	 public static void ininLodingView(View view){
		  ImageView loadingImageView=(ImageView)view.findViewById(R.id.lodding);
		  TextView loadingTextView=(TextView)view.findViewById(R.id.lodiing_text);
	    	loadingImageView.setBackgroundResource(R.anim.lodding);
	        final AnimationDrawable animationDrawable = (AnimationDrawable)loadingImageView.getBackground();
	    	loadingImageView.post(new Runnable() {
	    	    @Override
	    	        public void run()  {
	    	            animationDrawable.start();
	    	        }
	    	});
	    }
	 public static void ininLoding(Activity activity){
		  ImageView loadingImageView=(ImageView)activity.findViewById(R.id.lodding);
		  TextView loadingTextView=(TextView)activity.findViewById(R.id.lodiing_text);
	    	loadingImageView.setBackgroundResource(R.anim.lodding);
	        final AnimationDrawable animationDrawable = (AnimationDrawable)loadingImageView.getBackground();
	    	loadingImageView.post(new Runnable() {
	    	    @Override
	    	        public void run()  {
	    	            animationDrawable.start();
	    	        }
	    	});
	    }
}
