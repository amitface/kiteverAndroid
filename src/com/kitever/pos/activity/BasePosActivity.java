package com.kitever.pos.activity;

import sms19.listview.newproject.LoginPage;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitever.android.R;

public class BasePosActivity extends ActionBarActivity implements
		OnClickListener {
	private String screenName = null;
	private boolean isBottomDashbordEnabled = false,
			isBottomProductEnabled = false, isBottomOrderEnabled = false,
			isBottomPaymentEnabled = false, isBottomCreditEnabled = false;
	private ProgressDialog progressDialog = null;
	
	  Typeface moonicon;
	    String[] iconArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	protected void setLayoutContentView(int resLayput) {
		setContentView(resLayput);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
		bar.setTitle(getScreenName());
		bar.setDisplayHomeAsUpEnabled(true);
		
		iconArray=getResources().getStringArray(R.array.posIcons);
		moonicon = Typeface.createFromAsset(getAssets(),  "fonts/icomoon.ttf");
		
		TextView bottomDashbord = (TextView) findViewById(R.id.bottom_dashbord);
		TextView bottomProduct = (TextView) findViewById(R.id.bottom_product);
		TextView bottomOrder = (TextView) findViewById(R.id.bottom_order);
		TextView bottomPayment = (TextView) findViewById(R.id.bottom_payments);
		TextView bottomCredit = (TextView) findViewById(R.id.bottom_credits);
		
		bottomDashbord.setTypeface(moonicon);
		bottomProduct.setTypeface(moonicon);
		bottomOrder.setTypeface(moonicon);
		bottomPayment.setTypeface(moonicon);
		bottomCredit.setTypeface(moonicon);
		
		if (isBottomDashbordEnabled) {
			bottomDashbord.setOnClickListener(this);
			bottomDashbord.setAlpha(1.0f);
		}
		if (isBottomProductEnabled) {
			bottomProduct.setOnClickListener(this);
			bottomProduct.setAlpha(1.0f);
		}
		if (isBottomOrderEnabled) {
			bottomOrder.setOnClickListener(this);
			bottomOrder.setAlpha(1.0f);
		}
		if (isBottomPaymentEnabled) {
			bottomPayment.setOnClickListener(this);
			bottomPayment.setAlpha(1.0f);
		}
		if (isBottomCreditEnabled) {
			bottomCredit.setOnClickListener(this);
			bottomCredit.setAlpha(1.0f);
		}

	}

	private String getScreenName() {
		return screenName;
	}

	protected void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	protected void setBottomAction(boolean isBottomDashbordEnabled,
			boolean isBottomProductEnabled, boolean isBottomOrderEnabled,
			boolean isBottomPaymentEnabled, boolean isBottomCreditEnabled) {
		this.isBottomDashbordEnabled = isBottomDashbordEnabled;
		this.isBottomProductEnabled = isBottomProductEnabled;
		this.isBottomOrderEnabled = isBottomOrderEnabled;
		this.isBottomPaymentEnabled = isBottomPaymentEnabled;
		this.isBottomCreditEnabled = isBottomCreditEnabled;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pos_setting, menu);
		if (getScreenName().equalsIgnoreCase("Setting"))
			menu.getItem(0).setVisible(false);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.bottom_dashbord:
			intent = new Intent(BasePosActivity.this, POSHomeScreen.class);
			break;
		case R.id.bottom_product:
			intent = new Intent(BasePosActivity.this, POSProductsScreen.class);
			break;
		case R.id.bottom_order:
			intent = new Intent(BasePosActivity.this, POSOrdersScreen.class);
			break;
		case R.id.bottom_payments:
			intent = new Intent(BasePosActivity.this, POSPaymentDetailScreen.class);
			break;
		case R.id.bottom_credits:
			intent = new Intent(BasePosActivity.this, POSCreditScreen.class);
			break;
		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	public void showMessageWithTitle(String title, String message) {
		AlertDialog.Builder alert = new AlertDialog.Builder(
				BasePosActivity.this);
		alert.setTitle(title);
		alert.setMessage(message);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		alert.show();
	}

	public void showMessage(String message) {
		AlertDialog.Builder alert = new AlertDialog.Builder(
				BasePosActivity.this);
		alert.setMessage(message);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		alert.show();
	}
	
	public void moonIconfont(TextView txt)
	{
		txt.setTypeface(moonicon);
	}
	
	public void showLoading() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
		}
		progressDialog.setTitle("Please wait");
		progressDialog.setMessage("Loading...");
		progressDialog.show();
	}

	public void hideLoading() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}
