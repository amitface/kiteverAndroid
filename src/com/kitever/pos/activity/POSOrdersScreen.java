package com.kitever.pos.activity;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;
import com.kitever.customviews.FloatingActionButton;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSOrderDetailAdapter;
import com.kitever.pos.adapter.POSOrdersAdapter;
import com.kitever.pos.model.data.FetchOrder;
import com.kitever.pos.model.data.FetchOrderForId;
import com.kitever.pos.model.data.OrderDetailModelData;
import com.kitever.pos.model.data.OrderList;
import com.kitever.utils.DateHelper;
import com.kitever.utils.SpinnerReselect;
import com.kitever.utils.TotalRows;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.Home;

public class POSOrdersScreen extends PosBaseActivity implements NetworkManager,
		OnDateSetListener, TotalRows {

	private final int KEY_FETCH_ORDER_DETAILS = 1;
	private final int KEY_FETCH_ORDER_DETAILS_BY_ID = 2, KEY_SEND_SMS_MAIL = 3;

	private ListView orderListView, orderDetailListView;
	private ArrayList<OrderDetailModelData> orderList = null;
	private OrderDetailModelData clickObject = null;
	private ArrayList<FetchOrder> arrayList = null;
	private POSOrdersAdapter orderAdapter;
	private POSOrderDetailAdapter orderDetailAdapter;
	private TextView profile_name, profile_number, discountTotal, total,
			orderCode, contactName, totalRows, dateRange, noRecord;
	private int positionClick = -1;
	private Boolean statusMail = false, statusSms = false, statusPrint = false,
			statusStartDate = false, statusEndDate = false;
	private Button ok;
	private LinearLayout smsLayout, mailLayout, printLayout;
	private ImageView smsImage, mailImage, printImage, dateImage;
	private Spinner selectTypeSpinner;
	private SpinnerReselect dateSearch, paymentSearch;
	private EditText searchBox;
	private String name, number;
	private DatePickerDialog dialogDate;

	private long startDatel, endDatel, dateInMillis;
	private SimpleDateFormat format;
	private Calendar c = Calendar.getInstance();
	final int year = c.get(Calendar.YEAR);
	final int month = c.get(Calendar.MONTH);
	final int day = c.get(Calendar.DAY_OF_MONTH);
	ImageView pdfview;
	WebView pdfweb;
	LinearLayout pos_order_dialog_layout,pos_order_dialog_layout_price,pos_order_dialog_layout_invoice;
	TextView totaltaxtxt, basePrice, totaltax, otherPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreenName("Orders");
		setBottomAction(true, true, true, true, false, true, true, true, true,
				true, true, true);
		setLayoutContentView(R.layout.pos_orders_layout);
		setScreen();
		fetchOrderDetail();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startAcitivityWithEffect(new Intent(this, POSHomeScreen.class));
		NavUtils.navigateUpFromSameTask(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			startAcitivityWithEffect(new Intent(this, POSHomeScreen.class));
			NavUtils.navigateUpFromSameTask(this);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void setScreen() {

		selectTypeSpinner = (Spinner) findViewById(R.id.select_type_spinner);
		searchBox = (EditText) findViewById(R.id.edit_search);

		dateInMillis = c.getTimeInMillis();
		ImageView advanceSearch = (ImageView) findViewById(R.id.advance_search);
		dateSearch = (SpinnerReselect) findViewById(R.id.date_order_search);
		paymentSearch = (SpinnerReselect) findViewById(R.id.payment_order_search);
		totalRows = (TextView) findViewById(R.id.orders_total_rows);

		noRecord = (TextView) findViewById(R.id.no_record);
		MoonIcon mIcon = new MoonIcon(this);
		mIcon.setTextfont(noRecord);

		dateImage = (ImageView) findViewById(R.id.pos_order_image_date);
		dateRange = (TextView) findViewById(R.id.pos_order_date_range);
		orderListView = (ListView) findViewById(R.id.order_list_view);

		noRecord = (TextView) findViewById(R.id.no_record);

		mIcon.setTextfont(noRecord);

		FloatingActionButton fabButton = null;
		fabButton = new FloatingActionButton.Builder(POSOrdersScreen.this)
				.withDrawable(
						getResources()
								.getDrawable(R.drawable.ic_add_white_36dp))
				.withButtonColor(Color.parseColor("#E46C22"))
				.withGravity(Gravity.BOTTOM | Gravity.RIGHT)
				.withMargins(0, 0, 16,
						com.kitever.utils.Utils.FLOATING_BUTTON_MARGIN)
				.create();

		fabButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(POSOrdersScreen.this,
						POSAddOrderScreen.class);
				intent.putExtra("screen_name", "Add Order");
				startAcitivityWithEffect(intent);
				finish();
			}
		});

		ArrayList<String> typeList = new ArrayList<String>();
		typeList.add("Latest 50 orders");
		typeList.add("Order number");
		typeList.add("Contact");
		typeList.add("Total Amount");
//		typeList.add("Paid");
//		typeList.add("Unpaid");
//		typeList.add("Partially paid");
		typeList.add("Payment Status");
		typeList.add("Date");

		ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_item, typeList);
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectTypeSpinner.setAdapter(typeAdapter);
		selectTypeSpinner.setOnItemSelectedListener(itemSelectedListener);

		searchBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				if (orderAdapter != null) {
					POSOrdersScreen.this.orderAdapter.getFilter().filter(cs);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		orderListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						positionClick = position;
						clickObject = orderAdapter.getClickObject(position);
						fetchOrderDetailById(orderAdapter.getorderId(position));
						name = orderAdapter.getName(position);
						number = orderAdapter.getNumber(position);
						statusMail = orderAdapter.getMailStatus(position);

					}
				});

		dateSearch.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String pattern = "d-MMM, yyyy";
				format = new SimpleDateFormat(pattern);

				if (position == 1) {
					orderAdapter.dateRangeFilter(position);
					dateRange.setText("" + DateHelper.getTodayDate());

				} else if (position == 2) {
					orderAdapter.dateRangeFilter(position);
					dateRange.setText(""
							+ DateHelper.getYestardayDate(DateHelper
									.getDateInmillis()));
				} else if (position == 3) {
					orderAdapter.dateRangeFilter(position);
					dateRange.setText(DateHelper.getFirstDayOfWeek()
							+ " - "
							+ format.format(new Date(DateHelper
									.getDateInmillis())));
				} else if (position == 4) {
					orderAdapter.dateRangeFilter(position);
					dateRange.setText(DateHelper.getFirstDayOfMonth()
							+ " - "
							+ format.format(new Date(DateHelper
									.getDateInmillis())));
				} else if (position == 5) {
					orderAdapter.dateRangeFilter(position);

					dateRange.setText(DateHelper.getLastMonth());
				} else if (position == 6) {
					statusStartDate = true;
					dialogDate = new DatePickerDialog(POSOrdersScreen.this,
							POSOrdersScreen.this, year, month, day);
					if (statusStartDate == true) {
						dialogDate.getDatePicker().setMaxDate(
								(new Date().getTime()));// -(5097600000L)
						dialogDate.setTitle("Select Start Date");
						dialogDate.show();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		paymentSearch.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (position == 1) {

					orderAdapter.setPaidFiltered();
				} else if (position == 2) {
					orderAdapter.setParitalllyPaidFiltered();

				} else if (position == 3) {

					orderAdapter.setUnPaidFiltered();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		searchBox.setEnabled(false);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub

		// set selected date into textview
		// orderAdapter.dateFilter(startDatel, startDatel+5184000000L);

		if (statusStartDate == true) {
			final int yearTemp = year, monthTemp = monthOfYear, dayTemp = dayOfMonth;
			startDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
			statusEndDate = true;
			statusStartDate = false;
			dialogDate = new DatePickerDialog(POSOrdersScreen.this,
					POSOrdersScreen.this, year, month, day);
			c= Calendar.getInstance();
			Long maxdate;
			dateInMillis = c.getTimeInMillis();
			maxdate = (startDatel + (5184000000L)) > dateInMillis ? dateInMillis
					: (startDatel + (5184000000L));
			Log.i("Current date ",""+DateHelper.getDate(year,month,day));
			Log.i("maxdate ",""+maxdate);
			Log.i("startdate ",""+startDatel);

			dialogDate.getDatePicker().setMaxDate(maxdate);// startDatel+(5184000000L)
			dialogDate.getDatePicker().setMinDate(startDatel);
			dialogDate.setTitle("End Date (max 60 days)");
			dialogDate.setButton(DialogInterface.BUTTON_NEUTRAL, "Back",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Your code
							statusStartDate = true;

							dialogDate = new DatePickerDialog(
									POSOrdersScreen.this, POSOrdersScreen.this,
									yearTemp, monthTemp, dayTemp);
							if (statusStartDate == true) {
								dialogDate.getDatePicker().setMaxDate(
										(new Date().getTime()));// -(5097600000L)
								dialogDate.setTitle("Select Start Date");
								dialogDate.show();
							}
						}
					});
			dialogDate.show();
		} else if (statusEndDate == true) {
			endDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
			orderAdapter.dateFilter(startDatel, endDatel + (86400000L));
			String pattern = "d-MMM, yyyy";

			format = new SimpleDateFormat(pattern);
			dateRange.setText(format.format(new Date(startDatel)) + "  -  "
					+ format.format(new Date(endDatel)));
		}

		if (statusStartDate == true) {
			startDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
			final int yearTemp = year, monthTemp = monthOfYear, dayTemp = dayOfMonth;
			statusEndDate = true;
			statusStartDate = false;
			Long maxdate;
			maxdate = (startDatel + (5184000000L)) > dateInMillis ? dateInMillis
					: (startDatel + (5184000000L));

			dialogDate = new DatePickerDialog(POSOrdersScreen.this,
					POSOrdersScreen.this, this.year, month, day);
			dialogDate.getDatePicker().setMaxDate(maxdate);// startDatel+(5184000000L)
			dialogDate.getDatePicker().setMinDate(startDatel);
			dialogDate.setTitle("End Date (max 60 days)");
			dialogDate.setButton(DialogInterface.BUTTON_NEUTRAL, "Back",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Your code
							statusStartDate = true;
							dialogDate = new DatePickerDialog(
									POSOrdersScreen.this, POSOrdersScreen.this,
									yearTemp, monthTemp, dayTemp);
							if (statusStartDate == true) {
								dialogDate.getDatePicker().setMaxDate(
										(new Date().getTime() - (5097600000L)));
								dialogDate.setTitle("Select Start Date");
								dialogDate.show();
							}
						}
					});
			dialogDate.show();
		} else if (statusEndDate == true) {
			endDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
			orderAdapter.dateFilter(startDatel, endDatel + (86400000L));
			String pattern = "d-MMM, yyyy";

			format = new SimpleDateFormat(pattern);
			dateRange.setText(format.format(new Date(startDatel)) + "  -  "
					+ format.format(new Date(endDatel)));
		}
	}

	protected void showDialog() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.pos_order_item_detail);
		dialog.setTitle("Order Detail");

		Button closetv = (Button) dialog
				.findViewById(R.id.pos_order_detail_close);
		orderDetailListView = (ListView) dialog
				.findViewById(R.id.pos_order_detail_listview);

		totaltaxtxt = (TextView) dialog.findViewById(R.id.totaltaxtxt);
		basePrice = (TextView) dialog.findViewById(R.id.basePrice);
		totaltax = (TextView) dialog.findViewById(R.id.totaltax);
		otherPrice = (TextView) dialog.findViewById(R.id.otherPrice);



		pos_order_dialog_layout= (LinearLayout) dialog.findViewById(R.id.pos_order_dialog_layout);
		pos_order_dialog_layout_price= (LinearLayout) dialog.findViewById(R.id.pos_order_dialog_layout_price);


		total = (TextView) dialog.findViewById(R.id.pos_order_item_amt);
		discountTotal = (TextView) dialog
				.findViewById(R.id.pos_order_item_discount);

		contactName = (TextView) dialog
				.findViewById(R.id.pos_order_contact_name);
		orderCode = (TextView) dialog.findViewById(R.id.pos_order_item_code);
		profile_name = (TextView) dialog
				.findViewById(R.id.pos_order_profile_name);
		profile_number = (TextView) dialog
				.findViewById(R.id.pos_order_contact_number);
		smsLayout = (LinearLayout) dialog
				.findViewById(R.id.pos_order_dialog_layout_sms);
		mailLayout = (LinearLayout) dialog
				.findViewById(R.id.pos_order_dialog_layout_mail);
		printLayout = (LinearLayout) dialog
				.findViewById(R.id.pos_order_dialog_layout_print);
		profile_name.setText(name);
		profile_number.setText("Number:" + number);
		smsImage = (ImageView) dialog
				.findViewById(R.id.pos_order_dialog_image_sms);
		mailImage = (ImageView) dialog
				.findViewById(R.id.pos_order_dialog_image_mail);
		printImage = (ImageView) dialog
				.findViewById(R.id.pos_order_dialog_image_print);

		if (!statusMail) {
			mailLayout.setEnabled(false);
			mailLayout.getChildAt(1).setEnabled(false);
		}
		statusMail=false;
		printLayout.setEnabled(false);

		smsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (statusSms == false) {
					smsImage.setImageDrawable(ContextCompat.getDrawable(
							POSOrdersScreen.this, R.drawable.checkbox_settings));
					statusSms = true;
				} else {
					smsImage.setImageDrawable(ContextCompat.getDrawable(
							POSOrdersScreen.this,
							R.drawable.checkbox_blank_settings));
					statusSms = false;
				}
			}
		});

		pdfview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//dialog.hide();
				/*pos_order_dialog_layout_price.setVisibility(View.GONE);
				pos_order_dialog_layout.setVisibility(View.GONE);
				pos_order_dialog_layout_invoice.setVisibility(View.VISIBLE);
				pdfweb.loadUrl("https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf");*/

			}
		});

		mailLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (statusMail == false) {
					mailImage.setImageDrawable(ContextCompat.getDrawable(
							POSOrdersScreen.this, R.drawable.checkbox_settings));
					statusMail = true;
				} else {
					mailImage.setImageDrawable(ContextCompat.getDrawable(
							POSOrdersScreen.this,
							R.drawable.checkbox_blank_settings));
					statusMail = false;
				}
			}
		});

		printLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (statusPrint == false) {
					printImage.setImageDrawable(ContextCompat.getDrawable(
							POSOrdersScreen.this, R.drawable.checkbox_settings));
					statusPrint = true;
				} else {
					printImage.setImageDrawable(ContextCompat.getDrawable(
							POSOrdersScreen.this,
							R.drawable.checkbox_blank_settings));
					statusPrint = false;
				}
			}
		});

		ok = (Button) dialog.findViewById(R.id.pos_order_dialog_button_send);

		contactName.setText("Name: "
				+ orderList.get(positionClick).getContactName());
		orderCode.setText("OrderCode: "
				+ orderList.get(positionClick).getOrderCode());
		closetv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.hide();
				statusPrint = false;
				statusSms = false;
				statusMail = false;
			}
		});
		dialog.setCancelable(true);
		dialog.show();
	}

	private void fetchOrderDetail() {
		if (sms19.inapp.msg.constant.Utils.isDeviceOnline(this)) {
			try {
				showLoading();
				Map map = new HashMap<>();
				map.put("Page", "FetchOrderDetail");
				map.put("IsActive", "A");

				map.put("userID", getUserId());
				map.put("UserLogin", getUserLogin());
				map.put("Password", getPassWord());
				System.out.print("fetch order detail" + map.toString());
				new RequestManager().sendPostRequest(POSOrdersScreen.this,
						KEY_FETCH_ORDER_DETAILS, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			showMessage("Internet connection not found");
		}
	}

	private void fetchOrderDetailById(String id) {
		if (sms19.inapp.msg.constant.Utils.isDeviceOnline(this)) {
			try {
				showLoading();
				Map map = new HashMap<>();
				map.put("Page", "OrderDetails_OnClick");
				map.put("orderID", id);
				System.out.println("order Id " + id);
				// map.put("userID", getUserId());
				// map.put("UserLogin", getUserLogin());
				// map.put("Password", getPassWord());
				/*
				 * map.put("userID", "120"); map.put("UserLogin",
				 * "+918375023977"); map.put("Password", "7406");
				 */
				new RequestManager().sendPostRequest(POSOrdersScreen.this,
						KEY_FETCH_ORDER_DETAILS_BY_ID, map);
			} catch (Exception e) {

			}
		} else {
			showMessage("Internet connection not found");
		}
	}

	@SuppressWarnings("unchecked")
	private void sendMailSms(ArrayList<FetchOrder> arrayList2) {

		Log.i("send","sendsms");
		if (sms19.inapp.msg.constant.Utils.isDeviceOnline(this)) {
			try {
				showLoading();
				Map map = new HashMap<>();
				map.put("Page", "MailMsgSendUsingChk");
				map.put("chkSms", String.valueOf(statusSms));
				map.put("chkEmail", String.valueOf(statusMail));
				map.put("EmailID", clickObject.getEmail());
				map.put("ContactNo", clickObject.getMobile());
				map.put("CustomerName", clickObject.getContactName());
				map.put("OrderNo", clickObject.getOrderno());
				map.put("InvoiceNo", "");
				map.put("Invoicedt", clickObject.getOrderDate());
				map.put("BillAmt", Utils.doubleToString(clickObject.getBillAmount()));
				map.put("PaidAmount", clickObject.getPaidAmount());
				map.put("BalanceAmount", Utils.doubleToString(clickObject.getBalanceAmount()));
				map.put("FromPage","Order");

				Log.i("Order","" + map.toString());

				new RequestManager().sendPostRequest(POSOrdersScreen.this,
						KEY_SEND_SMS_MAIL, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			showMessage("Internet connection not found");
		}
	}

	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {

			Spinner spinSelected = (Spinner) parent;
			String selectedItem = (String) spinSelected.getSelectedItem();
			searchBox.setText("");

			if (selectedItem.equals("Last 50") || selectedItem.equals(""))
				searchBox.setEnabled(false);
			else
				searchBox.setEnabled(true);

			if (orderAdapter != null && selectedItem.equals("Date")) {
				orderAdapter.clearFilters();
				searchBox.setVisibility(View.GONE);
				paymentSearch.setVisibility(View.GONE);
				dateSearch.setVisibility(View.VISIBLE);
				dateImage.setVisibility(View.VISIBLE);
				dateRange.setVisibility(View.VISIBLE);
			}else if(orderAdapter != null && selectedItem.equals("Payment Status")){
				orderAdapter.clearFilters();
				searchBox.setVisibility(View.GONE);
				dateSearch.setVisibility(View.GONE);
				dateImage.setVisibility(View.INVISIBLE);
				dateRange.setVisibility(View.INVISIBLE);
				paymentSearch.setVisibility(View.VISIBLE);
			}
//			else if (orderAdapter != null && selectedItem.equals("Paid")) {
//				orderAdapter.setSelectedItemType(selectedItem);
//				orderAdapter.setPaidFiltered();
//			} else if (orderAdapter != null && selectedItem.equals("Unpaid")) {
//				orderAdapter.setSelectedItemType(selectedItem);
//				orderAdapter.setUnPaidFiltered();
//			} else if (orderAdapter != null
//					&& selectedItem.equals("Partially paid")) {
//				orderAdapter.setSelectedItemType(selectedItem);
//				orderAdapter.setParitalllyPaidFiltered();
//			}
			else if (orderAdapter != null) {
				orderAdapter.clearFilters();
				orderAdapter.setSelectedItemType(selectedItem);
				searchBox.setVisibility(View.VISIBLE);
				dateSearch.setVisibility(View.GONE);
				dateImage.setVisibility(View.INVISIBLE);
				dateRange.setVisibility(View.INVISIBLE);
				paymentSearch.setVisibility(View.GONE);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			orderAdapter.setSelectedItemType(null);
		}
	};

	@Override
	public void onReceiveResponse(int requestId, String response) {

		hideLoading();
		if (response != null && response.length() > 0) {
			if (requestId == KEY_FETCH_ORDER_DETAILS) {
				Gson gson = new Gson();
				OrderList orList = gson.fromJson(response, OrderList.class);
				orderList = orList.getOrder();
				if (orderList != null && orderList.size() > 0) {

					orderAdapter = new POSOrdersAdapter(this, orderList);
					orderListView.setAdapter(orderAdapter);
					// System.out.println("order response"+response);
					orderListView.setEmptyView(findViewById(R.id.order_emptyElement));

				} else {
					orderListView.setEmptyView(findViewById(R.id.order_emptyElement));
				}
			}

			else if (requestId == KEY_FETCH_ORDER_DETAILS_BY_ID) {
				showDialog();
				arrayList = new ArrayList<>();
				Double amt = 0.0, discountAmt = 0.0, OTCAmount = 0.0, TaxAmount = 0.0;
				boolean isInclusiveTax = false;
				orderDetailAdapter = new POSOrderDetailAdapter(arrayList);
				orderDetailListView.setAdapter(orderDetailAdapter);
				Gson gson = new GsonBuilder().create();

				FetchOrderForId arrayId = gson.fromJson(response,
						FetchOrderForId.class);

				// System.out.println("response "+response+
				// " "+" arrayID "+arrayId.getFetchOrder());

				if (arrayId.getFetchOrder() != null) {
					// sSystem.out.println("arraylist "+arrayId.getFetchOrder().get(0).getBillAmount());

					for (int i = 0; i < arrayId.getFetchOrder().size(); i++) {
						arrayList.add(arrayId.getFetchOrder().get(i));
						amt = amt + arrayList.get(i).getBillAmount();
						discountAmt = arrayList.get(i).getDiscountAmount();
						OTCAmount = arrayList.get(i).getOTCAmount();
						TaxAmount = arrayList.get(i).getTaxAmount();
						isInclusiveTax = arrayList.get(i).getIsInclusiveTax();
					}

					if (arrayList != null && arrayList.size() > 0) {
						orderDetailAdapter = new POSOrderDetailAdapter(
								arrayList);
						orderDetailAdapter.notifyDataSetChanged();

						discountTotal
								.setText(Utils.doubleToString(discountAmt));
						if (isInclusiveTax)
						{
							totaltaxtxt.setText("(inc) Total Tax");
							TaxAmount=(double) 0;
						}
						else
							totaltaxtxt.setText("(+) Total Tax");
						basePrice.setText(Utils.doubleToString(amt));
						totaltax.setText(Utils.doubleToString(TaxAmount));
						otherPrice.setText(Utils.doubleToString(OTCAmount));
						total.setText(Utils.doubleToString(amt - discountAmt
								+ TaxAmount + OTCAmount));

					}

				}

				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (statusMail == true || statusSms == true) {
							ok.setClickable(false);
							sendMailSms(arrayList);

						}
					}
				});
			} else if (requestId == KEY_SEND_SMS_MAIL) {
				hideLoading();
				JSONObject obj1;
				try {
					obj1 = new JSONObject(response);
					JSONArray objArray = obj1.getJSONArray("SendInvoice");
					 //Toast.makeText(this, objArray.getJSONObject(0).getString("Message"), 100).show();
					Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}


	private class WebClientClass extends WebViewClient {
		ProgressDialog pd = null;

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if (pd == null) {
				pd = new ProgressDialog(POSOrdersScreen.this);
				pd.show();
			}

			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if (pd != null)
				pd.dismiss();
			super.onPageFinished(view, url);
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		hideLoading();
		Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
		// showMessage("Please try again");
	}

	@Override
	public void totalRows(String text) {
		// TODO Auto-generated method stub
		totalRows.setText("Records : " + text);
	}
}
