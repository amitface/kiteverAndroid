package sms19.listview.newproject;

import sms19.listview.database.DataBaseDetails;
import com.kitever.android.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class BuyCreditPage extends ActionBarActivity{
	 //WebView dot;
	  DataBaseDetails db= new DataBaseDetails(this);

	String UserId= "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buycreditpage);
	//dot = (WebView)findViewById(R.id.webViewBuycredit);
		
		
		 //changes regards actionbar color
		 ActionBar bar = getSupportActionBar();
		 bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
		 bar.setTitle(Html.fromHtml("<font color='#ffffff'>BuyCredit</font>"));
	 
		 fetchMobileandUserId();
 
		String url = "http://www.pelims.com/mobilepayment.aspx?uid="+UserId;
		
		try {
			
			final WebView dot = (WebView)findViewById(R.id.webViewBuycredit);
		    dot.getSettings().setJavaScriptEnabled(true);
		    dot.setWebViewClient(new WebViewClient());   // this is use for show url at current web view
		    dot.loadUrl(url);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	/*	custom html text on webview -------------
	 * 
	 *   
	 * webView = (WebView) findViewById(R.id.webView1);
		   webView.getSettings().setJavaScriptEnabled(true);
		   //webView.loadUrl("http://www.google.com");
	 
		   String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
		   webView.loadData(customHtml, "text/html", "UTF-8");*/
					
	}
	

	public void fetchMobileandUserId() {
		db.Open();
		  
		   Cursor c;
		   
		   c= db.getLoginDetails();
		   
		  				   
		   while(c.moveToNext()){
			 
			   UserId = c.getString(3);
			   
		   }
				   
		   db.close();
	}
	
	@Override
	public void onBackPressed() {
	
		new AlertDialog.Builder(this)
		.setCancelable(false)
		.setMessage("Are you sure you want to Exit?")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					Home.HomeStatus.finish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent g = new Intent(BuyCreditPage.this, Home.class);
				startActivity(g);				
				finish();
				
			}
		})
		.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.show();
		
	}
}
