package com.kitever.pos.activity;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.CircularImageView;
import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.Home;



public class POSHomeScreen extends ActionBarActivity implements NetworkManager, OnClickListener{

	
	TextView creditVal,graph_payment_txt,graph_title;
	private boolean flag=false;
	
	//for graph
	private final int KEY_FETCH_GRAPH_DETAILS= 1;
	private final int KEY_FETCH_TOTAL_CREDIT= 2;
	
	ArrayList<Double> graphbillarray;
	  GraphView graph;
	  DataPoint[] points;
	 BarGraphSeries<DataPoint> barseries;
	 LineGraphSeries<DataPoint> lineseries;
	
	 private String screenName = null;
		private boolean isBottomDashbordEnabled = false,
				isBottomProductEnabled = false, isBottomOrderEnabled = false,
				isBottomPaymentEnabled = false, isBottomCreditEnabled = false,
				isBottomSettingEnabled = false,isBottomCategoryEnabled = false,isBottomBrandEnabled = false,
				isBottomTaxEnabled = false,isBottomOtherChargesEnabled = false, isBottomInvoiceEnabled = false,isBottomCustomerEnabled = false;
		
		private ProgressDialog progressDialog = null;
		private String userId;
		private String userLogin;
		private String passWord;
		HorizontalScrollView hscroll;
		private ActionBar bar;
		
		TextView bottom_right_arrow,bottom_left_arrow ;
		MoonIcon micon;		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences prfs = getSharedPreferences("profileData",
				Context.MODE_PRIVATE);
		//userLogin = "+919810398913";
		//passWord = "157013";
		//userId = "400";
		
		userLogin = prfs.getString("user_login", "");
		passWord =  prfs.getString("Pwd", "");
		userId =   Utils.getUserId(this);
		setBottomAction(false,true, true, true,true, true,true,true,true, true, true,true);
//		setContentView(R.layout.pos_home_grid_layout);
		setScreenName("POS Dashboard");
		setLayoutContentView(R.layout.pos_home_grid_layout);
//		setbarDisabled();
		micon=new MoonIcon(POSHomeScreen.this); 
	
	
		
		creditVal=(TextView)findViewById(R.id.credit_val);
		graph_payment_txt=(TextView)findViewById(R.id.graph_payment_txt);
		graph_title=(TextView)findViewById(R.id.graph_title);
		
		micon.setTextfont(creditVal);
		creditVal.setOnClickListener(this);
		
		graph = (GraphView) findViewById(R.id.graph);	
		graph.setOnClickListener(this);
		fetchGraphicalPayemntDetail(); 
		fetchTotalCredit();

		
		
	}
	
	String getUserId() {
		return userId;
	}

	String getUserLogin() {
		return userLogin;
	}

	String getPassWord() {
		return passWord;
	}

	protected void setLayoutContentView(int resLayput) {
		setContentView(resLayput);
//		bar = getSupportActionBar();
//		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
//		bar.setTitle(getScreenName());
//		bar.setDisplayHomeAsUpEnabled(true);
		actionBarSetting(getScreenName());
		MoonIcon micon = new MoonIcon(this);
		
		hscroll=(HorizontalScrollView) findViewById(R.id.hscroll);
	
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
	    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
	    Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
	    ComponentName componentInfo = taskInfo.get(0).topActivity;
	    componentInfo.getPackageName();
			
		TextView bottom_home = (TextView) findViewById(R.id.bottom_home);
		TextView bottomDashbord = (TextView) this.findViewById(R.id.bottom_dashbord);
		TextView bottomProduct = (TextView) findViewById(R.id.bottom_product);
		TextView bottomOrder = (TextView) findViewById(R.id.bottom_order);
		TextView bottomPayment = (TextView) findViewById(R.id.bottom_payments);
		TextView bottomCredit = (TextView) findViewById(R.id.bottom_credits);
		
		TextView bottom_home_txt = (TextView) findViewById(R.id.bottom_home_txt);
		TextView dashboard_txt = (TextView) findViewById(R.id.dashboard_txt);
		TextView product_txt = (TextView) findViewById(R.id.product_txt);
		TextView orders_txt = (TextView) findViewById(R.id.orders_txt);
		TextView payment_txt = (TextView) findViewById(R.id.payment_txt);
		TextView credits_txt = (TextView) findViewById(R.id.credits_txt);
		
		
		TextView category_txt = (TextView) findViewById(R.id.category_txt);
		TextView customer_txt = (TextView) findViewById(R.id.customer_txt);
		TextView brand_txt = (TextView) findViewById(R.id.brand_txt);
		TextView tax_txt = (TextView) findViewById(R.id.tax_txt);
		TextView other_charges_txt = (TextView) findViewById(R.id.other_charges_txt);
		TextView invoice_txt = (TextView) findViewById(R.id.invoice_txt);
		TextView setting_txt = (TextView) findViewById(R.id.setting_txt);
		
		
		TextView bottom_category = (TextView) findViewById(R.id.bottom_category);
		TextView bottom_customer = (TextView) findViewById(R.id.bottom_customer);
		TextView bottom_brand = (TextView) findViewById(R.id.bottom_brand);
		TextView bottom_tax = (TextView) findViewById(R.id.bottom_tax);
		TextView bottom_other_charges = (TextView) findViewById(R.id.bottom_other_charges);
		TextView bottom_setting = (TextView) findViewById(R.id.bottom_setting);
		TextView bottom_invoice = (TextView) findViewById(R.id.bottom_invoice);
		
		
		 bottom_left_arrow = (TextView) findViewById(R.id.bottom_left_arrow);
		 bottom_right_arrow = (TextView) findViewById(R.id.bottom_right_arrow);
		
		micon.setTextfont(bottom_home);
		micon.setTextfont(bottomDashbord);
		micon.setTextfont(bottomProduct);
		micon.setTextfont(bottomOrder);
		micon.setTextfont(bottomPayment);
		micon.setTextfont(bottomCredit);		
		micon.setTextfont(bottom_left_arrow);
		micon.setTextfont(bottom_right_arrow);
		micon.setTextfont(bottom_category);
		micon.setTextfont(bottom_customer);
		micon.setTextfont(bottom_brand);
		micon.setTextfont(bottom_tax);
		micon.setTextfont(bottom_other_charges);
		micon.setTextfont(bottom_setting);
		micon.setTextfont(bottom_invoice);
		
		
		bottom_home.setOnClickListener(this);
		bottom_home_txt.setOnClickListener(this);
		
	
		
		bottom_left_arrow.setOnClickListener(this);
		bottom_right_arrow.setOnClickListener(this);
		
		if(isBottomDashbordEnabled)
		{
			dashboard_txt.setOnClickListener(this);
			bottomDashbord.setOnClickListener(this);
		}else
		{
			dashboard_txt.setTextColor(getResources().getColor(R.color.bottom_text));
			dashboard_txt.setTypeface(null, Typeface.BOLD);
			bottomDashbord.setTextColor(getResources().getColor(R.color.bottom_text));
		}
		
		if (isBottomCategoryEnabled){
			bottom_category.setOnClickListener(this);
			category_txt.setOnClickListener(this);
		}
		else
		{
			bottom_category.setTextColor(getResources().getColor(R.color.bottom_text));
			category_txt.setTypeface(null, Typeface.BOLD);
			category_txt.setTextColor(getResources().getColor(R.color.bottom_text));
		}
		
		if (isBottomProductEnabled) {
			bottomProduct.setOnClickListener(this);
			product_txt.setOnClickListener(this);
		}
		else
		{
			bottomProduct.setTextColor(getResources().getColor(R.color.bottom_text));
			product_txt.setTypeface(null, Typeface.BOLD);
			product_txt.setTextColor(getResources().getColor(R.color.bottom_text));
		}
		
		if (isBottomOrderEnabled) {
			bottomOrder.setOnClickListener(this);
			orders_txt.setOnClickListener(this);
		}
		else
		{
			bottomOrder.setTextColor(getResources().getColor(R.color.bottom_text));
			orders_txt.setTypeface(null, Typeface.BOLD);
			orders_txt.setTextColor(getResources().getColor(R.color.bottom_text));
			hscroll.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					hscroll.scrollBy(200, 200);		
				}
			});
			
		}
		
		if (isBottomPaymentEnabled) {
			bottomPayment.setOnClickListener(this);
			payment_txt.setOnClickListener(this);
		}
		else
		{
			bottomPayment.setTextColor(getResources().getColor(R.color.bottom_text));
			payment_txt.setTypeface(null, Typeface.BOLD);
			payment_txt.setTextColor(getResources().getColor(R.color.bottom_text));
		}
		if (isBottomCreditEnabled) {
			bottomCredit.setOnClickListener(this);
			credits_txt.setOnClickListener(this);
		}
		else
		{
			bottomCredit.setTextColor(getResources().getColor(R.color.bottom_text));
			credits_txt.setTypeface(null, Typeface.BOLD);
			credits_txt.setTextColor(getResources().getColor(R.color.bottom_text));
			
//			hscroll.post(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					
//					hscroll.fullScroll(ScrollView.FOCUS_RIGHT);		
//				}
//			});
		}
		
		if (isBottomSettingEnabled) {
			bottom_setting.setOnClickListener(this);
			setting_txt.setOnClickListener(this);
		}
		else
		{
			bottom_setting.setTextColor(getResources().getColor(R.color.bottom_text));
			setting_txt.setTypeface(null, Typeface.BOLD);
			setting_txt.setTextColor(getResources().getColor(R.color.bottom_text));
//			hscroll.post(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					hscroll.fullScroll(ScrollView.FOCUS_RIGHT);		
//				}
//			});
		}
		if (isBottomBrandEnabled) {
			bottom_brand.setOnClickListener(this);
			brand_txt.setOnClickListener(this);
			
		}
		else
		{
			bottom_brand.setTextColor(getResources().getColor(R.color.bottom_text));
			brand_txt.setTypeface(null, Typeface.BOLD);
			brand_txt.setTextColor(getResources().getColor(R.color.bottom_text));
//			hscroll.post(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					hscroll.fullScroll(ScrollView.FOCUS_RIGHT);		
//				}
//			});
		}
		if (isBottomTaxEnabled) {
			bottom_tax.setOnClickListener(this);
			tax_txt.setOnClickListener(this);
		}
		else
		{
			bottom_tax.setTextColor(getResources().getColor(R.color.bottom_text));
			tax_txt.setTypeface(null, Typeface.BOLD);
			tax_txt.setTextColor(getResources().getColor(R.color.bottom_text));
		}
		
		if (isBottomOtherChargesEnabled) {
			bottom_other_charges.setOnClickListener(this);
			other_charges_txt.setOnClickListener(this);
		}
		else
		{
			bottom_other_charges.setTextColor(getResources().getColor(R.color.bottom_text));
			other_charges_txt.setTypeface(null, Typeface.BOLD);
			other_charges_txt.setTextColor(getResources().getColor(R.color.bottom_text));
			hscroll.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					hscroll.fullScroll(ScrollView.FOCUS_RIGHT);		
				}
			});
		}
		if (isBottomInvoiceEnabled) {
			bottom_invoice.setOnClickListener(this);
			invoice_txt.setOnClickListener(this);
		}
		else
		{
			bottom_invoice.setTextColor(getResources().getColor(R.color.bottom_text));
			invoice_txt.setTypeface(null, Typeface.BOLD);
			invoice_txt.setTextColor(getResources().getColor(R.color.bottom_text));
		}
		if (isBottomCustomerEnabled) {
			bottom_customer.setOnClickListener(this);
			customer_txt.setOnClickListener(this);
		}
		else
		{
			bottom_customer.setTextColor(getResources().getColor(R.color.bottom_text));
			customer_txt.setTextColor(getResources().getColor(R.color.bottom_text));
			customer_txt.setTypeface(null, Typeface.BOLD);
		}	
	}	
	
	private String getScreenName() {
		return screenName;
	}

	protected void setScreenName(String screenName) {
		this.screenName = screenName;
	}

		
	protected void setBottomAction(boolean isBottomDashbordEnabled,boolean isBottomCategoryEnabled,
			boolean isBottomProductEnabled, boolean isBottomCustomerEnabled, boolean isBottomOrderEnabled,
			 boolean isBottomCreditEnabled, boolean isBottomPaymentEnabled,
			 boolean isBottomTaxEnabled, boolean isBottomOtherChargesEnabled,
			 boolean isBottomBrandEnabled,
			boolean isBottomInvoiceEnabled, boolean isBottomSettingEnabled ) {
		
		this.isBottomDashbordEnabled = isBottomDashbordEnabled;
		this.isBottomProductEnabled = isBottomProductEnabled;
		this.isBottomOrderEnabled = isBottomOrderEnabled;
		this.isBottomPaymentEnabled = isBottomPaymentEnabled;
		this.isBottomCreditEnabled = isBottomCreditEnabled;
		this.isBottomSettingEnabled = isBottomSettingEnabled;
		this.isBottomCategoryEnabled = isBottomCategoryEnabled;
		this.isBottomBrandEnabled = isBottomBrandEnabled;
		this.isBottomTaxEnabled = isBottomTaxEnabled;
		this.isBottomOtherChargesEnabled = isBottomOtherChargesEnabled;
		this.isBottomInvoiceEnabled = isBottomInvoiceEnabled;
		this.isBottomCustomerEnabled = isBottomCustomerEnabled;
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		startAcitivityWithEffect(new Intent(POSHomeScreen.this,Home.class));
		NavUtils.navigateUpFromSameTask(this);		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;		
//		Toast.makeText(this, "onClick", 100).show();
		switch (v.getId()) {
		case R.id.bottom_home_txt:
		case R.id.bottom_home: 
			intent = new Intent(POSHomeScreen.this, Home.class);
			break;
		case R.id.dashboard_txt:	
		case R.id.bottom_dashbord:
//			intent = new Intent(POSHomeScreen.this, .class);
			break;
		case R.id.product_txt:	
		case R.id.bottom_product:
			intent = new Intent(POSHomeScreen.this, POSProductsScreen.class);
			break;
		case R.id.orders_txt:
		case R.id.bottom_order:
			intent = new Intent(POSHomeScreen.this, POSOrdersScreen.class);
			break;
		case R.id.payment_txt:	
		case R.id.bottom_payments:
			intent = new Intent(POSHomeScreen.this, POSPaymentDetailScreen.class);
			break;
		case R.id.credits_txt:
		case R.id.bottom_credits:
			intent = new Intent(POSHomeScreen.this, POSCreditScreen.class);
			break;

		case R.id.category_txt:
		case R.id.bottom_category:
			intent = new Intent(POSHomeScreen.this, POSCategoryScreen.class);
			break;
			
		case R.id.customer_txt:
		case R.id.bottom_customer:
			intent = new Intent(POSHomeScreen.this, PosCustomerList.class);
			break;
		 
		case R.id.brand_txt:
		case R.id.bottom_brand:
			intent = new Intent(POSHomeScreen.this, POSBrandScreen.class);
			break;	
		case R.id.tax_txt:
		case R.id.bottom_tax:
			intent = new Intent(POSHomeScreen.this, POSTaxMasterScreen.class);
			break;
			
		case R.id.other_charges_txt:
		case R.id.bottom_other_charges:
			intent = new Intent(POSHomeScreen.this, POSOtherChargeScreen.class);
			break;	

		 
		case R.id.invoice_txt:
		case R.id.bottom_invoice:
			intent = new Intent(POSHomeScreen.this, POSInvoiceScreen.class);
			break;	
		case R.id.bottom_left_arrow:			
			//hscroll.scrollTo((int)hscroll.getScrollX() - 120, (int)hscroll.getScrollY());			
			hscroll.fullScroll(ScrollView.FOCUS_LEFT);			
			break;
		case R.id.bottom_right_arrow:			
			//hscroll.scrollTo((int)hscroll.getScrollX() + 120, (int)hscroll.getScrollY());
			hscroll.fullScroll(ScrollView.FOCUS_RIGHT);			
			break;
			
		case R.id.credit_val:
			intent = new Intent(POSHomeScreen.this, POSCreditScreen.class);
			startAcitivityWithEffect(intent);
			finish();
			break;
		case R.id.graph:
			intent = new Intent(POSHomeScreen.this, POSPaymentDetailScreen.class);
			startAcitivityWithEffect(intent);
			finish();
			break;
			
	
		default:
			break;
		}
		
		if (intent != null) {			
			startAcitivityWithEffect(intent);
			finish();
		}
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

	/**/	case android.R.id.home:
			onBackPressed();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void actionBarSetting(String str) {

//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		View view = getLayoutInflater().inflate(R.layout.custon_actionbar_view,
				null, false);
		// mBackButton = (LinearLayout)
		// view.findViewById(R.id.action_bat_back_btn);
		CircularImageView	mActionBarImage = (sms19.inapp.msg.CircularImageView) view
				.findViewById(R.id.profile_image);
		mActionBarImage.setVisibility(View.GONE);
		TextView sorting_title = (TextView) view.findViewById(R.id.sorting_title);
		TextView  mUserNameTitle = (TextView) view.findViewById(R.id.name);
//		progress_on_actionbar = (ProgressBar) view
//				.findViewById(R.id.progress_on_actionbar);
//
//		if (ContactUtil.isInsertContactRunning) {
//			progress_on_actionbar.setVisibility(View.VISIBLE);
//		} else {
//			progress_on_actionbar.setVisibility(View.GONE);
//		}
//
//		camera_btn = (LinearLayout) view
//				.findViewById(R.id.chat_addfilebuttonlay);
//
		TextView	mUserStatusTitle = (TextView) view.findViewById(R.id.status);
		mUserStatusTitle.setVisibility(View.GONE);
		TextView  actionbar_title = (TextView) view.findViewById(R.id.actionbar_title);

		actionbar_title.setVisibility(View.VISIBLE);
		// mBackButton.setOnClickListener(this);
		LinearLayout layout_name_status = (LinearLayout) view
				.findViewById(R.id.layout_name_status);
		layout_name_status.setVisibility(View.GONE);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.RIGHT);
		params.setMargins(0, 0, 0, 0);

		getSupportActionBar().setCustomView(view, params);

		Toolbar parent = (Toolbar) view.getParent();
		parent.setContentInsetsAbsolute(0, 0);
		actionbar_title.setText(com.kitever.utils.Utils
				.setActionBarTextAndColor(str));

	}

	
	 
    private void fetchGraphicalPayemntDetail() {
			if (sms19.inapp.msg.constant.Utils.isDeviceOnline(this)) {
				try {
					showLoading();
					Map map = new HashMap<>();
					map.put("Page", "GraphicalPayemntDetail");	
					 map.put("userID", getUserId());
					 map.put("UserLogin", getUserLogin());
					 map.put("Password", getPassWord());
				
					new RequestManager().sendPostRequest(POSHomeScreen.this,
							KEY_FETCH_GRAPH_DETAILS, map);
				} catch (Exception e) {
					
				}
			} else {
				showMessage("Internet connection not found");
			}

		}
    
    private void fetchTotalCredit() {
		if (sms19.inapp.msg.constant.Utils.isDeviceOnline(this)) {
			try {
				showLoading();
				Map map = new HashMap<>();
				map.put("Page", "FetchTotalCredit");
				 map.put("userID", getUserId());
				 map.put("UserLogin", getUserLogin());
				 map.put("Password", getPassWord());			
				new RequestManager().sendPostRequest(POSHomeScreen.this,
						KEY_FETCH_TOTAL_CREDIT, map);
			} catch (Exception e) {
				
			}
		} else {
			showMessage("Internet connection not found");
		}

	}
		
		private void parseGraphResponse(String response) {
			try {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject != null) {
					if (jsonObject.has("GraphicalPaymentDetails")) {
						JSONArray jsonArray = jsonObject
								.getJSONArray("GraphicalPaymentDetails");
						if (jsonArray != null && jsonArray.length() > 0) {
							graphbillarray=new ArrayList<Double>();							
							points = new DataPoint[5];
							Double BillAmount = (double) 0;
							points[0]=new DataPoint(0,0);
							points[1]=new DataPoint(1,0);
							points[2]=new DataPoint(2,0);
							points[3]=new DataPoint(3,0);
							points[4]=new DataPoint(4,0);
							
							int jlength=jsonArray.length()<=4?jsonArray.length():4;
							
							for (int k = 0; k <  jlength; k++) {
								JSONObject object = jsonArray.getJSONObject(k);
								if (object.has("BillAmount")) {
									BillAmount = object.getDouble("BillAmount");									
								}
								//BillAmount=(double) ((k+1)*1000);
								 graphbillarray.add(BillAmount);
								 points[k+1] = new DataPoint(k+1,BillAmount);								
							}
							 graphStyle(false);
						}
						else
						{
							graphStyle(true);	
						}
						
						 
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void parseTotalCredit(String response)
		{
			try {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject != null) {
					if (jsonObject.has("POSTotalCredit")) {
						JSONArray jsonArray = jsonObject.getJSONArray("POSTotalCredit");
						if (jsonArray != null && jsonArray.length() > 0) {
							
								JSONObject object = jsonArray.getJSONObject(0);
								if (object.has("BalanceAmount")) {
									double BalanceAmount = object.getDouble("BalanceAmount");	
									creditVal.setText(""+BalanceAmount+" "+getString(R.string.rsicon));
								    micon.setTextfont(creditVal);
									
								}		
							
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public void graphStyle(boolean isnodata)
		{
			try
			{
			   
				
				 Calendar c = Calendar.getInstance();
				 int year = c.get(Calendar.YEAR);
				 int month = c.get(Calendar.MONTH)+1;
				 
				 if(isnodata)
				 {
				 graph_title.setText("You don't have any payment till now");
				 graph_payment_txt.setText("Week wise graph: "+month+"/"+year);
				 }
				 else
				 {
					 graph.setTitle("Payment This Month");
					 graph_payment_txt.setText("Week wise graph: "+month+"/"+year);
				 }
				
			 graph.setTitle("");	 
			 barseries = new BarGraphSeries<>(points);
			 lineseries = new LineGraphSeries<>(points);			 
			
			 lineseries.setColor(Color.parseColor("#808080"));
			 lineseries.setDrawDataPoints(true);							 
			 lineseries.setDrawDataPoints(true);
			
			 graph.addSeries(barseries);
			 graph.addSeries(lineseries);
			 barseries.setSpacing(50);
			 
			 barseries.setColor(Color.parseColor("#A9A9A9"));					

			// draw values on top
			
			 barseries.setDrawValuesOnTop(true);
			 barseries.setValuesOnTopColor(Color.parseColor("#696969"));	
			 
			 StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
			 staticLabelsFormatter.setHorizontalLabels(new String[] {"","1W", "2W", "3W","4W"});
			 graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);	
			 
			 
			 /*graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
				    @Override
				    public String formatLabel(double value, boolean isValueX) {
				        if (isValueX) {
				        
				            return super.formatLabel(value, isValueX)+" Wk";
				        } else {
				            // show currency for y values
				            return super.formatLabel(value, isValueX);
				        }
				    }
				});*/
			
			
				 
			 GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();			 
			 gridLabelRenderer.setPadding(60);
			 gridLabelRenderer.setLabelsSpace(40);
			 
			 gridLabelRenderer.setHorizontalLabelsColor(Color.parseColor("#808080"));
			 gridLabelRenderer.setVerticalLabelsColor(Color.parseColor("#808080"));			 
			 gridLabelRenderer.setGridColor(Color.parseColor("#808080"));
			 
		 
			// activate horizontal zooming and scrolling
			 
			  
			  graph.getViewport().setScalable(true);
				 // activate horizontal scrolling
			  graph.getViewport().setScrollable(true);
			}
			catch(Exception e){ e.printStackTrace(); }
			
		}

		@Override
		public void onReceiveResponse(int requestId, String response) {
			hideLoading();
			if (response != null && response.length() > 0) {
				if (requestId == KEY_FETCH_GRAPH_DETAILS) {
					parseGraphResponse(response);
				}
				else if (requestId == KEY_FETCH_TOTAL_CREDIT) {
					parseTotalCredit(response);
				}
			}
		}

		@Override
		public void onErrorResponse(VolleyError error) {
			showMessage("Please try again");
		}
		

		public void showMessageWithTitle(String title, String message) {
			AlertDialog.Builder alert = new AlertDialog.Builder(
					POSHomeScreen.this);
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
					POSHomeScreen.this);
			alert.setMessage(message);
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			alert.show();
		}
		
		public void showLoading() {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(this,R.style.MyTheme);
			}
//			progressDialog.setTitle("Please wait");
//			progressDialog.setMessage("Loading...");
			progressDialog.show();
		}

		public void hideLoading() {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
		
		public void startAcitivityWithEffect(Intent slideactivity)
		{
			Bundle bndlanimation = 
					ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
			startActivity(slideactivity, bndlanimation);
		}
}
