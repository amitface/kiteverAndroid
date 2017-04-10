package com.kitever.pos.activity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;

import sms19.inapp.msg.CircularImageView;
import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.Home;

import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class PosBaseActivity extends ActionBarActivity implements
		OnClickListener {
	private String screenName = null;
	private boolean isBottomDashbordEnabled = false,
			isBottomProductEnabled = false, isBottomOrderEnabled = false,
			isBottomPaymentEnabled = false, isBottomCreditEnabled = false,
			isBottomSettingEnabled = false, isBottomCategoryEnabled = false,
			isBottomBrandEnabled = false, isBottomTaxEnabled = false,
			isBottomOtherChargesEnabled = false,
			isBottomInvoiceEnabled = false, isBottomCustomerEnabled = false;

	private ProgressDialog progressDialog = null;
	private String userId, userLogin, passWord;
	private TextView actionbar_title, mUserNameTitle, sorting_title;
	HorizontalScrollView hscroll;
	private ActionBar bar;
	AlertDialog radioDialog = null;
	TextView bottom_right_arrow, bottom_left_arrow;

	private int mTitleOffset;
	private static final int TITLE_OFFSET_DIPS = 24;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences prfs = getSharedPreferences("profileData",
				Context.MODE_PRIVATE);
		// userLogin = "+919810398913";
		// passWord = "157013";
		// userId = "400";
		mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources()
				.getDisplayMetrics().density);
		userLogin = prfs.getString("user_login", "");
		passWord = prfs.getString("Pwd", "");
		userId = Utils.getUserId(this);
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

	protected void setbarDisabled() {
		bar.setDisplayHomeAsUpEnabled(false);
	}

	protected void setLayoutContentView(int resLayput) {
		setContentView(resLayput);

		actionBarSettingWithBack(this,getSupportActionBar(),getScreenName());
		MoonIcon micon = new MoonIcon(this);

		hscroll = (HorizontalScrollView) findViewById(R.id.hscroll);

		TextView bottom_home = (TextView) findViewById(R.id.bottom_home);
		TextView bottomDashbord = (TextView) this
				.findViewById(R.id.bottom_dashbord);
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

		hscroll.setSoundEffectsEnabled(true);

		if (isBottomDashbordEnabled) {
			dashboard_txt.setOnClickListener(this);
			bottomDashbord.setOnClickListener(this);
		} else {
			dashboard_txt.setTextColor(getResources().getColor(
					R.color.bottom_text));
			dashboard_txt.setTypeface(null, Typeface.BOLD);
			bottomDashbord.setTextColor(getResources().getColor(
					R.color.bottom_text));
			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(1, 0);
				}
			});
		}

		if (isBottomCategoryEnabled) {
			bottom_category.setOnClickListener(this);
			category_txt.setOnClickListener(this);
		} else {
			bottom_category.setTextColor(getResources().getColor(
					R.color.bottom_text));
			category_txt.setTypeface(null, Typeface.BOLD);
			category_txt.setTextColor(getResources().getColor(
					R.color.bottom_text));
			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(2, 0);
				}
			});
		}

		if (isBottomProductEnabled) {
			bottomProduct.setOnClickListener(this);
			product_txt.setOnClickListener(this);
		} else {
			bottomProduct.setTextColor(getResources().getColor(
					R.color.bottom_text));
			product_txt.setTypeface(null, Typeface.BOLD);
			product_txt.setTextColor(getResources().getColor(
					R.color.bottom_text));

			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(3, 0);
				}
			});

		}

		if (isBottomOrderEnabled) {
			bottomOrder.setOnClickListener(this);
			orders_txt.setOnClickListener(this);
		} else {
			bottomOrder.setTextColor(getResources().getColor(
					R.color.bottom_text));
			orders_txt.setTypeface(null, Typeface.BOLD);
			orders_txt.setTextColor(getResources()
					.getColor(R.color.bottom_text));
			final int targetScrollX = bottomOrder.getLeft();
			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(5, 0);
					// hscroll.scrollTo(targetScrollX, 0);
				}
			});

		}

		if (isBottomPaymentEnabled) {
			bottomPayment.setOnClickListener(this);
			payment_txt.setOnClickListener(this);
		} else {
			bottomPayment.setTextColor(getResources().getColor(
					R.color.bottom_text));
			payment_txt.setTypeface(null, Typeface.BOLD);
			payment_txt.setTextColor(getResources().getColor(
					R.color.bottom_text));

			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(7, 0);
				}
			});
		}

		if (isBottomCreditEnabled) {
			bottomCredit.setOnClickListener(this);
			credits_txt.setOnClickListener(this);
		} else {
			bottomCredit.setTextColor(getResources().getColor(
					R.color.bottom_text));
			credits_txt.setTypeface(null, Typeface.BOLD);
			credits_txt.setTextColor(getResources().getColor(
					R.color.bottom_text));
			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(6, 0);
				}
			});
		}
		if (isBottomSettingEnabled) {
			bottom_setting.setOnClickListener(this);
			setting_txt.setOnClickListener(this);
		} else {
			bottom_setting.setTextColor(getResources().getColor(
					R.color.bottom_text));
			setting_txt.setTypeface(null, Typeface.BOLD);
			setting_txt.setTextColor(getResources().getColor(
					R.color.bottom_text));
			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(12, 0);
				}
			});
		}
		if (isBottomBrandEnabled) {
			bottom_brand.setOnClickListener(this);
			brand_txt.setOnClickListener(this);

		} else {
			bottom_brand.setTextColor(getResources().getColor(
					R.color.bottom_text));
			brand_txt.setTypeface(null, Typeface.BOLD);
			brand_txt
					.setTextColor(getResources().getColor(R.color.bottom_text));
			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(10, 0);
				}
			});
		}
		if (isBottomTaxEnabled) {
			bottom_tax.setOnClickListener(this);
			tax_txt.setOnClickListener(this);
		} else {
			bottom_tax.setTextColor(getResources()
					.getColor(R.color.bottom_text));
			tax_txt.setTypeface(null, Typeface.BOLD);
			tax_txt.setTextColor(getResources().getColor(R.color.bottom_text));
			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(8, 0);
				}
			});
		}
		if (isBottomOtherChargesEnabled) {
			bottom_other_charges.setOnClickListener(this);
			other_charges_txt.setOnClickListener(this);
		} else {
			bottom_other_charges.setTextColor(getResources().getColor(
					R.color.bottom_text));
			other_charges_txt.setTypeface(null, Typeface.BOLD);
			other_charges_txt.setTextColor(getResources().getColor(
					R.color.bottom_text));
			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(9, 0);
				}
			});
		}

		if (isBottomInvoiceEnabled) {
			bottom_invoice.setOnClickListener(this);
			invoice_txt.setOnClickListener(this);
		} else {
			bottom_invoice.setTextColor(getResources().getColor(
					R.color.bottom_text));
			invoice_txt.setTypeface(null, Typeface.BOLD);
			invoice_txt.setTextColor(getResources().getColor(
					R.color.bottom_text));
			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(11, 0);
				}
			});
		}
		if (isBottomCustomerEnabled) {
			bottom_customer.setOnClickListener(this);
			customer_txt.setOnClickListener(this);
		} else {
			bottom_customer.setTextColor(getResources().getColor(
					R.color.bottom_text));
			customer_txt.setTextColor(getResources().getColor(
					R.color.bottom_text));
			customer_txt.setTypeface(null, Typeface.BOLD);
			hscroll.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollToTab(4, 0);
				}
			});
		}
	}



	private String getScreenName() {
		return screenName;
	}

	protected void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	protected void setBottomAction(boolean isBottomDashbordEnabled,
			boolean isBottomCategoryEnabled, boolean isBottomProductEnabled,
			boolean isBottomCustomerEnabled, boolean isBottomOrderEnabled,
			boolean isBottomCreditEnabled, boolean isBottomPaymentEnabled,
			boolean isBottomTaxEnabled, boolean isBottomOtherChargesEnabled,
			boolean isBottomBrandEnabled, boolean isBottomInvoiceEnabled,
			boolean isBottomSettingEnabled) {

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

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	//
	// switch (item.getItemId()) {
	//
	// case android.R.id.home:
	// onBackPressed();
	// // super.onBackPressed();
	// startAcitivityWithEffect(new Intent(this,POSHomeScreen.class));
	// NavUtils.navigateUpFromSameTask(this);
	// break;
	// case R.id.pos_setting:
	// // if(!isSetting()){
	// Intent intent = new Intent(PosBaseActivity.this,
	// POSSettingScreen.class);
	// startAcitivityWithEffect(intent);
	// finish();
	// // }
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pos_setting, menu);
		return false;
		// if (getScreenName() != null
		// && getScreenName().equalsIgnoreCase("Settings"))
		// menu.getItem(0).setVisible(false);
		// return true;
	}

	public void onSetingClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.bottom_home_txt:
		case R.id.bottom_home:
			intent = new Intent(PosBaseActivity.this, Home.class);
			break;
		case R.id.dashboard_txt:
		case R.id.bottom_dashbord:
			intent = new Intent(PosBaseActivity.this, POSHomeScreen.class);
			// finish();
			break;
		case R.id.product_txt:
		case R.id.bottom_product:
			intent = new Intent(PosBaseActivity.this, POSProductsScreen.class);
			break;
		case R.id.orders_txt:
		case R.id.bottom_order:
			intent = new Intent(PosBaseActivity.this, POSOrdersScreen.class);
			break;
		case R.id.payment_txt:
		case R.id.bottom_payments:
			intent = new Intent(PosBaseActivity.this,
					POSPaymentDetailScreen.class);
			break;
		case R.id.credits_txt:
		case R.id.bottom_credits:
			intent = new Intent(PosBaseActivity.this, POSCreditScreen.class);
			break;

		case R.id.category_txt:
		case R.id.bottom_category:
			intent = new Intent(PosBaseActivity.this, POSCategoryScreen.class);
			break;

		case R.id.customer_txt:
		case R.id.bottom_customer:
			intent = new Intent(PosBaseActivity.this, PosCustomerList.class);
			break;

		case R.id.brand_txt:
		case R.id.bottom_brand:
			intent = new Intent(PosBaseActivity.this, POSBrandScreen.class);
			break;
		case R.id.tax_txt:
		case R.id.bottom_tax:
			intent = new Intent(PosBaseActivity.this, POSTaxMasterScreen.class);
			break;

		case R.id.other_charges_txt:
		case R.id.bottom_other_charges:
			intent = new Intent(PosBaseActivity.this,
					POSOtherChargeScreen.class);
			break;




		case R.id.invoice_txt:
		case R.id.bottom_invoice:
			intent = new Intent(PosBaseActivity.this, POSInvoiceScreen.class);
			break;

		case R.id.bottom_left_arrow:
			// hscroll.scrollTo((int)hscroll.getScrollX() - 120,
			// (int)hscroll.getScrollY());
			hscroll.fullScroll(ScrollView.FOCUS_LEFT);
			break;
		case R.id.bottom_right_arrow:
			// hscroll.scrollTo((int)hscroll.getScrollX() + 120,
			// (int)hscroll.getScrollY());
			hscroll.fullScroll(ScrollView.FOCUS_RIGHT);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		// Toast.makeText(this, "onClick", 100).show();
		switch (v.getId()) {
		case R.id.bottom_home_txt:
		case R.id.bottom_home:
			intent = new Intent(PosBaseActivity.this, Home.class);
			break;
		case R.id.dashboard_txt:
		case R.id.bottom_dashbord:
			intent = new Intent(PosBaseActivity.this, POSHomeScreen.class);
			break;
		case R.id.product_txt:
		case R.id.bottom_product:
			intent = new Intent(PosBaseActivity.this, POSProductsScreen.class);
			break;
		case R.id.orders_txt:
		case R.id.bottom_order:
			intent = new Intent(PosBaseActivity.this, POSOrdersScreen.class);
			break;
		case R.id.payment_txt:
		case R.id.bottom_payments:
			intent = new Intent(PosBaseActivity.this,
					POSPaymentDetailScreen.class);
			break;
		case R.id.credits_txt:
		case R.id.bottom_credits:
			intent = new Intent(PosBaseActivity.this, POSCreditScreen.class);
			break;

		case R.id.category_txt:
		case R.id.bottom_category:
			intent = new Intent(PosBaseActivity.this, POSCategoryScreen.class);
			break;

		case R.id.customer_txt:
		case R.id.bottom_customer:
			intent = new Intent(PosBaseActivity.this, PosCustomerList.class);
			break;

		case R.id.brand_txt:
		case R.id.bottom_brand:
			intent = new Intent(PosBaseActivity.this, POSBrandScreen.class);
			break;
		case R.id.tax_txt:
		case R.id.bottom_tax:
			intent = new Intent(PosBaseActivity.this, POSTaxMasterScreen.class);
			break;

		case R.id.other_charges_txt:
		case R.id.bottom_other_charges:
			intent = new Intent(PosBaseActivity.this,
					POSOtherChargeScreen.class);
			break;

		case R.id.invoice_txt:
		case R.id.bottom_invoice:
			intent = new Intent(PosBaseActivity.this, POSInvoiceScreen.class);
			break;
		case R.id.bottom_left_arrow:
			// hscroll.scrollTo((int)hscroll.getScrollX() - 120,
			// (int)hscroll.getScrollY());
			hscroll.fullScroll(ScrollView.FOCUS_LEFT);
			break;
		case R.id.bottom_right_arrow:
			// hscroll.scrollTo((int)hscroll.getScrollX() + 120,
			// (int)hscroll.getScrollY());
			hscroll.fullScroll(ScrollView.FOCUS_RIGHT);
			break;

		default:
			break;
		}
		if (intent != null) {
			startAcitivityWithEffect(intent);
			finish();
		}
	}

	public void showMessageWithTitle(String title, String message) {
		AlertDialog.Builder alert = new AlertDialog.Builder(
				PosBaseActivity.this);
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
				PosBaseActivity.this);
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

		int[] textSizeAttr = new int[] { android.R.attr.progressBarStyleSmallInverse };
		int indexOfAttrTextSize = 0;
		TypedValue typedValue = new TypedValue();
		TypedArray a = this.obtainStyledAttributes(typedValue.data,
				textSizeAttr);
		getTheme().resolveAttribute(
				android.R.attr.progressBarStyleSmallInverse, typedValue, true);
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this, R.style.MyTheme);
		}
		// progressDialog.setTitle("Please wait");
		// progressDialog.setMessage("Loading...");

		// progressDialog.setS
		progressDialog
				.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
		progressDialog.show();
	}

	public void hideLoading() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public void startAcitivityWithEffect(Intent slideactivity) {
		// R.anim.myanimation
		// Bundle bndlanimation =
		// ActivityOptions.makeCustomAnimation(getApplicationContext(),
		// R.anim.animation,R.anim.animation2).toBundle();
		Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
				getApplicationContext(), R.anim.animation, R.anim.animation2)
				.toBundle();
		startActivity(slideactivity, bndlanimation);
	}

	private void scrollToTab(int tabIndex, int positionOffset) {
		final int tabStripChildCount = 13;
		if (tabStripChildCount == 0 || tabIndex < 0
				|| tabIndex >= tabStripChildCount) {
			return;
		}
		LinearLayout linear_product = (LinearLayout) findViewById(R.id.linear_hscroll);
		// final int targetScrollX = linear_product.get().getLeft();
		// hscroll.scrollTo(targetScrollX, 0);

		View selectedChild = linear_product.getChildAt(tabIndex);
		if (selectedChild != null) {
			int targetScrollX = selectedChild.getLeft();
			// Toast.makeText(PosBaseActivity.this, "" + targetScrollX,
			// Toast.LENGTH_SHORT).show();
			if (tabIndex > 0 || positionOffset > 0) {
				// If we're not at the first child and are mid-scroll, make sure
				// we obey the offset
				targetScrollX -= mTitleOffset;
			}

			hscroll.scrollTo(targetScrollX, 0);
		}
	}
}
