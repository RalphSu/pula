/**
 * 
 */
package com.yuhj.ontheway.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yuhj.ontheway.R;

/**
 * @author Liangfei
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class CourseDetailH5Activity extends BaseActivity {

    private static final String COURSE_DETIAL_GET = "http://121.40.151.183:8080/pula-sys/app/timecourse/get?id=%s";

    private WebView webView;
    private ProgressBar progressBar;
    private String url;
    private String name;
    
    @Override
    @SuppressLint("InflateParams")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        url = String.format(COURSE_DETIAL_GET, getIntent().getStringExtra("SearchId"));
        name = getIntent().getStringExtra("name");
        LinearLayout rootViewLayout = new LinearLayout(this);
        rootViewLayout.setOrientation(LinearLayout.VERTICAL);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_zhuanti_detail, null);
        // progress bar
        TextView title = (TextView) view.findViewById(R.id.course_main_title);
        
        //AseoZdpAseo.initType(this, AseoZdpAseo.INSERT_TYPE);
        
        // set content data
        title.setText(name);
        rootViewLayout.addView(view);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);

        webView = new WebView(this);
        rootViewLayout.addView(progressBar);
        rootViewLayout.addView(webView);
        setContentView(rootViewLayout);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 页面下载完毕,却不代表页面渲染完毕显示出来
                // WebChromeClient中progress==100时也是一样
                if (webView.getContentHeight() != 0) {
                    // 这个时候网页才显示
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 自身加载新链接,不做外部跳转
                view.loadUrl(url);
                return true;
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // 这里将textView换成你的progress来设置进度
                // if (newProgress == 0) {
                // textView.setVisibility(View.VISIBLE);
                // progressBar.setVisibility(View.VISIBLE);
                // }
                progressBar.setProgress(newProgress);
                progressBar.postInvalidate();
                // if (newProgress == 100) {
                // textView.setVisibility(View.GONE);
                // progressBar.setVisibility(View.GONE);
                // }
            }
        });
    }
}
