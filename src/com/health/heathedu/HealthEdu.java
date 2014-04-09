package com.health.heathedu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.younext.R;

import com.health.BaseActivity;

public class HealthEdu extends BaseActivity {
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.health_edu);
		WebView webview = (WebView) findViewById(R.id.health_edu_webview);
		webview.getSettings().setJavaScriptEnabled(true);
		final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progress_bar);
		final Activity activity = this;
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// activity.setProgress(progress *
				// 1000);
				mProgress.setProgress(progress);
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "¡¨Ω” ß∞‹! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});
		webview.loadUrl("http://www.nihe.org.cn/");
	}

}
