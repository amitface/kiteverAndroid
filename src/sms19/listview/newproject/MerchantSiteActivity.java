package sms19.listview.newproject;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MerchantSiteActivity extends Activity {
	
	ImageView back_icon;
	
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_site_layout);
		String url=getIntent().getStringExtra("url");
		
		back_icon=(ImageView)findViewById(R.id.back_icon);
//		if(url!=null && !url.equalsIgnoreCase("") &&(!url.startsWith("http")||!url.startsWith("https"))){
//			url="http://"+url;
//		}
		RelativeLayout top_layout=(RelativeLayout) findViewById(R.id.top_layout);
		top_layout.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
		WebView webView = (WebView) findViewById(R.id.web_view_id);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		//setLoadWithOverviewMode
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		WebClientClass webViewClient = new WebClientClass();
		webView.setWebViewClient(webViewClient);
		webView.loadUrl(url);
		Button closeBtn = (Button) findViewById(R.id.close_btn);
		closeBtn.setOnClickListener(clickListener);
		closeBtn.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
		back_icon.setOnClickListener(clickListener);
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.close_btn:
				finish();
				break;
			case R.id.back_icon:
				onBackPressed();
				break;
			default:
				break;
			}
		}
	};
	private class WebClientClass extends WebViewClient {
		ProgressDialog pd = null;

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			if(pd==null){
				pd = new ProgressDialog(MerchantSiteActivity.this);
			}
			try {
				pd.setTitle("Please wait");
				pd.setMessage("Page is loading..");
				pd.show();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (pd != null && pd.isShowing()){
					pd.dismiss();
			}
			
		}
	}
}
