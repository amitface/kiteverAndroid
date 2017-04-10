package com.kitever.pos.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Utils;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSCustomerPaymentDetailAdapter;
import com.kitever.pos.adapter.POSPaymentDetailAdapter;
import com.kitever.pos.model.data.CreditBalnce;
import com.kitever.pos.model.data.POSPaymentDetailData;
import com.kitever.pos.model.data.PosPayment;
import com.kitever.utils.DateHelper;
import com.kitever.utils.SpinnerReselect;
import com.kitever.utils.TotalRows;

public class POSPaymentDetailScreen extends PosBaseActivity implements
		NetworkManager, OnDateSetListener, TotalRows {

	private POSPaymentDetailAdapter paymentDetailsAdapter;
	private POSCustomerPaymentDetailAdapter customerPaymentDetailAdapter;
	private ListView paymentDetailViewList, lvPaymentDetailDailog;
	private ArrayList<CreditBalnce> arrayList;
	private CreditBalnce clickObject;

	private final int KEY_FETCH_CREDIT_BALANCE = 1,
			KEY_FETCHCUSTOMER_ORDER_DETAIL = 2, KEY_SEND_SMS_MAIL = 3;
	private TextView totalCredit, totalRecord, profileName, profileNumber,
			profileItemCode, profileContactName, dateRange, noRecord;
	private Boolean statusEndDate = false, statusStartDate = false,
			statusSms = false, statusMail = false, statusPrint = false;
	private SpinnerReselect dateSearch;
	private EditText searchBox;
	private ImageView dateImage, smsImage, mailImage, printImage;
	private LinearLayout smsLayout, mailLayout, printLayout;
	private long startDatel, endDatel;
	private Button ok;
	private String name, number;
	private SimpleDateFormat format;
	private Calendar c = Calendar.getInstance();
	final int year = c.get(Calendar.YEAR);
	final int month = c.get(Calendar.MONTH);
	final int day = c.get(Calendar.DAY_OF_MONTH);
	Long dateInMillis;
	DatePickerDialog dialogDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setScreenName("Payments");
		dateInMillis = c.getTimeInMillis();
		setBottomAction(true, true, true, true, true, true, false, true, true,
				true, true, true);
		setLayoutContentView(R.layout.pos_payment_detail_layout);
		setScreen();
		fetchPaymentDetails();

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
		Spinner selectType = (Spinner) findViewById(R.id.select_type_spinner);
		searchBox = (EditText) findViewById(R.id.edit_search);
		ImageView adavanceSearch = (ImageView) findViewById(R.id.advance_search);
		paymentDetailViewList = (ListView) findViewById(R.id.payment_view_list);
		totalCredit = (TextView) findViewById(R.id.total_payment);
		totalRecord = (TextView) findViewById(R.id.payment_total_rows);

		dateImage = (ImageView) findViewById(R.id.pos_payment_image_date);
		dateRange = (TextView) findViewById(R.id.pos_payment_date_range);

		noRecord = (TextView) findViewById(R.id.no_record);
		MoonIcon mIcon = new MoonIcon(this);
		mIcon.setTextfont(noRecord);

		dateSearch = (SpinnerReselect) findViewById(R.id.date_payment_search);
		ArrayList<String> typeList = new ArrayList<String>();
		typeList.add("Last 50");
		typeList.add("Invoice");
		typeList.add("Contact");
		// typeList.add("Amount");
		typeList.add("Paid");
		typeList.add("Date");

		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, typeList);
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectType.setAdapter(typeAdapter);
		selectType.setOnItemSelectedListener(itemSelectedListener);
		searchBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				if (paymentDetailsAdapter != null) {
					POSPaymentDetailScreen.this.paymentDetailsAdapter
							.getFilter().filter(cs);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});

		paymentDetailViewList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				name = paymentDetailsAdapter.getName(position);
				number = paymentDetailsAdapter.getNumber(position);
				statusMail = paymentDetailsAdapter.getMailStatus(position);
				clickObject = arrayList.get(position);
				fetchCustomerOrderDetails(arrayList.get(position)
						.getReceiptNo(), arrayList.get(position)
						.getContact_Name(), arrayList.get(position)
						.getInvoiceID());

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
					paymentDetailsAdapter.dateRangeFilter(position);
					dateRange.setText("" + DateHelper.getTodayDate());

				} else if (position == 2) {
					paymentDetailsAdapter.dateRangeFilter(position);
					dateRange.setText(""
							+ DateHelper.getYestardayDate(DateHelper
									.getDateInmillis()));
				} else if (position == 3) {
					paymentDetailsAdapter.dateRangeFilter(position);
					dateRange.setText(DateHelper.getFirstDayOfWeek()
							+ " - "
							+ format.format(new Date(DateHelper
									.getDateInmillis())));
				} else if (position == 4) {
					paymentDetailsAdapter.dateRangeFilter(position);
					dateRange.setText(DateHelper.getFirstDayOfMonth()
							+ " - "
							+ format.format(new Date(DateHelper
									.getDateInmillis())));
				} else if (position == 5) {
					paymentDetailsAdapter.dateRangeFilter(position);

					dateRange.setText(DateHelper.getLastMonth());
				} else if (position == 6) {
					statusStartDate = true;
					dialogDate = new DatePickerDialog(
							POSPaymentDetailScreen.this,
							POSPaymentDetailScreen.this, year, month, day);
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
	}

	protected void showDialog() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.pos_payment_single_detail);
		dialog.setTitle("Payment Details");
		showLoading();
		Button textView = (Button) dialog
				.findViewById(R.id.pos_payment_detail_close);
		lvPaymentDetailDailog = (ListView) dialog
				.findViewById(R.id.pos_payment_detail_listview);
		profileName = (TextView) dialog
				.findViewById(R.id.pos_payment_profile_name);
		profileNumber = (TextView) dialog
				.findViewById(R.id.pos_payment_contact_number);
		profileItemCode = (TextView) dialog
				.findViewById(R.id.pos_payment_item_code);
		profileContactName = (TextView) dialog
				.findViewById(R.id.pos_payment_contact_name);

		profileName.setText("" + name);
		profileNumber.setText("Number: " + number);

		smsLayout = (LinearLayout) dialog
				.findViewById(R.id.pos_payment_dialog_layout_sms);
		mailLayout = (LinearLayout) dialog
				.findViewById(R.id.pos_payment_dialog_layout_mail);
		printLayout = (LinearLayout) dialog
				.findViewById(R.id.pos_payment_dialog_layout_print);

		smsImage = (ImageView) dialog
				.findViewById(R.id.pos_payment_dialog_image_sms);
		mailImage = (ImageView) dialog
				.findViewById(R.id.pos_payment_dialog_image_mail);
		printImage = (ImageView) dialog
				.findViewById(R.id.pos_payment_dialog_image_print);

		if (!statusMail) {
			mailLayout.setEnabled(false);
			mailLayout.getChildAt(1).setEnabled(false);
		}
		printLayout.setEnabled(false);
		printLayout.getChildAt(1).setEnabled(false);

		smsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (statusSms == false) {
					smsImage.setImageDrawable(ContextCompat.getDrawable(
							POSPaymentDetailScreen.this,
							R.drawable.checkbox_settings));
					statusSms = true;
				} else {
					smsImage.setImageDrawable(ContextCompat.getDrawable(
							POSPaymentDetailScreen.this,
							R.drawable.checkbox_blank_settings));
					statusSms = false;
				}
			}
		});

		mailLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (statusMail == false) {
					mailImage.setImageDrawable(ContextCompat.getDrawable(
							POSPaymentDetailScreen.this,
							R.drawable.checkbox_settings));
					statusMail = true;
				} else {
					mailImage.setImageDrawable(ContextCompat.getDrawable(
							POSPaymentDetailScreen.this,
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
							POSPaymentDetailScreen.this,
							R.drawable.checkbox_settings));
					statusPrint = true;
				} else {
					printImage.setImageDrawable(ContextCompat.getDrawable(
							POSPaymentDetailScreen.this,
							R.drawable.checkbox_blank_settings));
					statusPrint = false;
				}
			}
		});

		ok = (Button) dialog.findViewById(R.id.pos_payment_dialog_button_send);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (statusMail == true || statusSms == true)
					sendMailSms();
				else
					Toast.makeText(POSPaymentDetailScreen.this,
							"Select atleast on check box", 100).show();

			}
		});

		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.hide();
			}
		});
		dialog.setCancelable(true);
		dialog.show();
		searchBox.setEnabled(false);
	}

	private void fetchCustomerOrderDetails(String receiptNumber, String Name,
			String InvoiceId) {
		if (Utils.isDeviceOnline(this)) {
			showDialog();
			profileName.setText(Name);
			profileItemCode.setText("Invoice Id: " + InvoiceId);
			profileContactName.setText("Name: " + Name);
			Map map = new HashMap<>();

			map.put("Page", "GetPaymentDetail");
			map.put("ReceiptCode", receiptNumber);
			map.put("userID", getUserId());
			map.put("UserLogin", getUserLogin());
			map.put("Password", getPassWord());
			System.out.println("Map " + map.toString());

			try {
				new RequestManager().sendPostRequest(this,
						KEY_FETCHCUSTOMER_ORDER_DETAIL, map);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			showMessage("Internet connection not found");
		}
	}

	private void fetchPaymentDetails() {
		if (Utils.isDeviceOnline(this)) {
			showLoading();
			Map map = new HashMap<>();
			map.put("Page", "FetchCreditbalance");
			map.put("userID", getUserId());
			map.put("UserLogin", getUserLogin());
			map.put("Password", getPassWord());

			try {
				new RequestManager().sendPostRequest(this,
						KEY_FETCH_CREDIT_BALANCE, map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			showMessage("Internet connection not found");
		}
	}

	private void sendMailSms() {
		if (sms19.inapp.msg.constant.Utils.isDeviceOnline(this)) {
			try {
				showLoading();
				Gson gson = new Gson();
				// Second way to create a Gson object using GsonBuilder

				Map map = new HashMap<>();
				map.put("Page", "MailMsgSendUsingChk");
				map.put("chkSms", String.valueOf(statusSms));
				map.put("chkEmail", String.valueOf(statusMail));
				// map.put("data", gson.toJson(arrayList2));
				if (clickObject.getContact_Email() != null)
					map.put("EmailID", clickObject.getContact_Email());

				if (clickObject.getContact_Mobile() != null)
					map.put("ContactNo", clickObject.getContact_Mobile());

				if (clickObject.getContact_Name() != null)
					map.put("CustomerName", clickObject.getContact_Name());

				map.put("OrderNo", "");

				if (clickObject.getInvoiceID() != null)
					map.put("InvoiceNo", clickObject.getInvoiceID());

				if (clickObject.getInvoiceDate() != null)
					map.put("Invoicedt", clickObject.getInvoiceDate());

				if (clickObject.getBillAmount() != null)
					map.put("BillAmt", clickObject.getBillAmount());

				if (clickObject.getPaidAmount() != null)
					map.put("PaidAmount", clickObject.getPaidAmount());

				if (clickObject.getBalanceAmount() != null)
					map.put("BalanceAmount", clickObject.getBalanceAmount());

				    map.put("FromPage","Payment");
				System.out.println("Order popup" + map.toString());

				new RequestManager().sendPostRequest(
						POSPaymentDetailScreen.this, KEY_SEND_SMS_MAIL, map);
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
			// TODO Auto-generated method stub
			Spinner spinSelected = (Spinner) parent;
			String selectedItem = (String) spinSelected.getSelectedItem();
			searchBox.setText("");

			if (selectedItem.equals("Last 50") || selectedItem.equals(""))
				searchBox.setEnabled(false);
			else
				searchBox.setEnabled(true);

			if (paymentDetailsAdapter != null) {
				paymentDetailsAdapter.setSelectedItemType(selectedItem);
			}
			if (selectedItem.equals("Date") && paymentDetailsAdapter != null) {
				searchBox.setVisibility(View.GONE);
				dateSearch.setVisibility(View.VISIBLE);
				dateImage.setVisibility(View.VISIBLE);
				dateRange.setVisibility(View.VISIBLE);
			} else if (paymentDetailsAdapter != null) {
				paymentDetailsAdapter.clearFilters();
				paymentDetailsAdapter.setSelectedItemType(selectedItem);
				searchBox.setVisibility(View.VISIBLE);
				dateSearch.setVisibility(View.GONE);
				dateImage.setVisibility(View.INVISIBLE);
				dateRange.setVisibility(View.INVISIBLE);

				if (selectedItem.equals("Credit >"))
					searchBox.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL
							| InputType.TYPE_CLASS_NUMBER);
				else
					searchBox.setInputType(InputType.TYPE_CLASS_TEXT);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			paymentDetailsAdapter.setSelectedItemType(null);
		}
	};

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub

		// set selected date into textview
		paymentDetailsAdapter.dateFilter(startDatel, startDatel + 5184000000L);

		if (statusStartDate == true) {
			startDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
			final int yearTemp = year, monthTemp = monthOfYear, dayTemp = dayOfMonth;
			statusEndDate = true;
			statusStartDate = false;
			c= Calendar.getInstance();
			Long maxdate;
			dateInMillis = c.getTimeInMillis();
			maxdate = (startDatel + (5184000000L)) > dateInMillis ? dateInMillis
					: (startDatel + (5184000000L));

			dialogDate = new DatePickerDialog(POSPaymentDetailScreen.this,
					POSPaymentDetailScreen.this, year, month, day);
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
									POSPaymentDetailScreen.this,
									POSPaymentDetailScreen.this, yearTemp,
									monthTemp, dayTemp);
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
			paymentDetailsAdapter
					.dateFilter(startDatel, endDatel + (86400000L));
			String pattern = "d-MMM, yyyy";

			SimpleDateFormat format = new SimpleDateFormat(pattern);
			dateRange.setText(format.format(new Date(startDatel)) + "  -  "
					+ format.format(new Date(endDatel)));
		}
	}

	@Override
	public void onReceiveResponse(int requestId, String response) {
		// TODO Auto-generated method stub
		if (response != null && response.length() > 0) {
			if (requestId == KEY_FETCH_CREDIT_BALANCE) {
				hideLoading();
				System.out.println("Payment " + response);
				Gson gson = new Gson();
				PosPayment posPayment = gson.fromJson(response,
						PosPayment.class);
				arrayList = posPayment.getCreditBalnce();
				paymentDetailsAdapter = new POSPaymentDetailAdapter(this,
						posPayment.getCreditBalnce());
				paymentDetailViewList.setAdapter(paymentDetailsAdapter);
				paymentDetailViewList
						.setEmptyView(findViewById(R.id.payment_emptyElement));
			}

			else if (requestId == KEY_FETCHCUSTOMER_ORDER_DETAIL) {
				hideLoading();
				Gson gson = new Gson();

				POSPaymentDetailData paymentData = gson.fromJson(response,
						POSPaymentDetailData.class);
				if (paymentData != null) {
					customerPaymentDetailAdapter = new POSCustomerPaymentDetailAdapter(
							paymentData.getPaymentDetail());
					lvPaymentDetailDailog
							.setAdapter(customerPaymentDetailAdapter);
				}
			} else if (requestId == KEY_SEND_SMS_MAIL) {
				hideLoading();
				JSONObject obj1;
				try {
					obj1 = new JSONObject(response);
					JSONArray objArray = obj1.getJSONArray("SendInvoice");
					//Toast.makeText(this,objArray.getJSONObject(0).getString("Message"), 100).show();
					Toast.makeText(this,"Send Successfully", Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			// showMessage("Please try again.");
			hideLoading();
		}

	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		hideLoading();
		showMessage("Please try again.");
	}

	@Override
	public void totalRows(String text) {
		// TODO Auto-generated method stub
		totalRecord.setText("Records : " + text);
	}
}
