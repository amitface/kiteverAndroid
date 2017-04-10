package com.kitever.pos.activity;

import com.kitever.android.R;
import com.kitever.android.R.id;
import com.kitever.android.R.layout;
import com.kitever.android.R.menu;
import com.kitever.app.context.WebClientClass;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class POSInvoicePdfViewer extends ActionBarActivity {
	
	String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBarSettingWithBack(this,getSupportActionBar(),"PDF Viewer");
		setContentView(R.layout.activity_posinvoice_pdf_viewer);
		setScreen();	
		
	}

	private void setScreen() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		url = intent.getStringExtra("Link");
		WebView web = (WebView) findViewById(R.id.pos_invoice_pdf);
		web.getSettings().getJavaScriptEnabled();
		web.getSettings().setBuiltInZoomControls(true);
		web.setPadding(0, 0, 0, 0);
		web.setInitialScale(getScale());
		web.clearHistory();
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings()
				.setJavaScriptCanOpenWindowsAutomatically(true);
		web.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);
		web.setWebViewClient(new WebViewClient() {        

	        @Override
	        public void onPageFinished(WebView view, String url) {
	            super.onPageFinished(view, url);
//	            pDialog.dismiss();
	        }
	    });


//		WebClientClass webViewClient = new WebClientClass(this, new ProgressBar(this));
//		web.setWebViewClient(webViewClient);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.posinvoice_pdf_viewer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private int getScale(){
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
	    int width = display.getWidth(); 
	    Double val = new Double(width)/new Double(360);
	    val = val * 100d;
	    return val.intValue();
	}
}
