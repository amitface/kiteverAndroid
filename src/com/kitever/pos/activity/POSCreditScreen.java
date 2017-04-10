package com.kitever.pos.activity;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSCreditAdapter;
import com.kitever.pos.model.data.CreditDetails;
import com.kitever.pos.model.data.CreditModelData;
import com.kitever.pos.model.manager.ModelManager;
import com.kitever.utils.DateHelper;
import com.kitever.utils.SpinnerReselect;
import com.kitever.utils.TotalRows;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

public class POSCreditScreen extends PosBaseActivity implements NetworkManager, OnDateSetListener, TotalRows{

	private POSCreditAdapter creditAdapter;
	private final int KEY_FETCH_CREDIT_BALANCE = 1;
	private final int KEY_FETCH_MODE_PAYMENT=2;
	private final int KEY_ADD_PAYMENT=3;
	private ListView creditListView;
	private TextView totalCredit, totalRecord, dateRange, noRecord;
	private EditText searchBox;
	private SpinnerReselect dateSearch;
	private ImageView dateImage;
	
	private long startDatel, endDatel,dateInMillis;
	private boolean statusEndDate = false, statusStartDate = false, statusPopup= false;
	
	private SimpleDateFormat format;
	private Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
	
    DatePickerDialog dialogDate ;
    
	private EditText billingAmtEdit, paidAmtEdit, balanceEdit;
	private Spinner  modePaymentSpinner;
	private String modeOfPaymentVal="";
    
	private EditText chequeNo, bankNameOrRefNum, remarkOrDes;
	private TextView date;
	private String modeType="", chequeNoVal="", dateVal="", remarkOrDesVal="",
			bankNameOrRefNumVal="";
	private Context context;
	private FrameLayout inputLayout, calendarLayout;
	private CalendarView calenderView;
	String ref_number="";
	private int positionClick=-1;
	private CreditDetails clickObject=null;
	RelativeLayout payment_mode_layout;
	ArrayAdapter<String> PaymentModeadapter;
	boolean paymentMode_not_cash=false;
	private int modeNo = 0;
	String OrderID="",contactID="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setScreenName("Credit");
		dateInMillis = c.getTimeInMillis();
		setBottomAction(true, true, true,true, true, false,true,true,true,true, true,true);
		setLayoutContentView(R.layout.pos_credit_layout);
		setScreen();
		fetchCreditBalance();	
		fetchModeOfPayment();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		startAcitivityWithEffect(new Intent(this,POSHomeScreen.class));
		NavUtils.navigateUpFromSameTask(this);		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			startAcitivityWithEffect(new Intent(this,POSHomeScreen.class));
			NavUtils.navigateUpFromSameTask(this);
			break;
				
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setScreen() {
		Spinner selectType = (Spinner) findViewById(R.id.select_type_spinner);
		searchBox = (EditText) findViewById(R.id.edit_search);
		ImageView adavanceSearch = (ImageView) findViewById(R.id.advance_search);
		creditListView = (ListView) findViewById(R.id.credit_list_view);
		totalRecord = (TextView) findViewById(R.id.credit_total_rows);		
		noRecord = (TextView) findViewById(R.id.no_record);
		MoonIcon mIcon=new MoonIcon(this);
		mIcon.setTextfont(noRecord);
		
		creditListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
		    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				positionClick= position;				
				clickObject = creditAdapter.getClickObject(position);				
				contactID=clickObject.getContactID();
				OrderID=clickObject.getOrderID();
				 float creditAmount=0.0f;
				creditAmount=Float.parseFloat(clickObject.getBalanceAmount());
				if(creditAmount>0) posPaymentPopup(clickObject.getBalanceAmount());
				else showMessage(clickObject.getContact_Name() +" have not any credit amount to pay");
				
		    }
		});
		
		
		dateImage = (ImageView) findViewById(R.id.pos_credit_image_date);
		dateRange = (TextView) findViewById(R.id.pos_credit_date_range);	
		dateSearch = (SpinnerReselect) findViewById(R.id.date_credit_search);
		ArrayList<String> typeList = new ArrayList<String>();
		typeList.add("Last 50");		
		typeList.add("Contact");
		typeList.add("Order Id");
		typeList.add("Total Bill");
		typeList.add("Credit");
		typeList.add("Date");

		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, typeList);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectType.setAdapter(typeAdapter);
		searchBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				if (creditAdapter != null) {
					POSCreditScreen.this.creditAdapter.getFilter().filter(cs);
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
		
		selectType.setOnItemSelectedListener(itemSelectedListener);
		dateSearch.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String pattern = "d-MMM, yyyy";	
				format= new SimpleDateFormat(pattern);
				
				if(position == 1)
				{
					creditAdapter.dateRangeFilter(position);					
					dateRange.setText(""+DateHelper.getTodayDate());
					
				}	else if(position == 2)
				{
					creditAdapter.dateRangeFilter(position);					
					dateRange.setText(""+DateHelper.getYestardayDate(DateHelper.getDateInmillis()));
				}
				else if(position == 3)
				{
					creditAdapter.dateRangeFilter(position);
					dateRange.setText(DateHelper.getFirstDayOfWeek()+" - "+format.format(new Date(DateHelper.getDateInmillis())));
				}
				else if(position ==4)
				{
					creditAdapter.dateRangeFilter(position);
					dateRange.setText(DateHelper.getFirstDayOfMonth()+" - "+format.format(new Date(DateHelper.getDateInmillis())));
				}
				else if(position == 5)
				{
					creditAdapter.dateRangeFilter(position);
					
					dateRange.setText(DateHelper.getLastMonth());
				}
				else if(position==6)
					{    
					statusStartDate = true;
					dialogDate = new DatePickerDialog(POSCreditScreen.this, POSCreditScreen.this, year, month, day);
				        if(statusStartDate==true){		
						        dialogDate.getDatePicker().setMaxDate((new Date().getTime()));//-(5097600000L)
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
		
		searchBox.setEnabled(false);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		creditAdapter.dateFilter(startDatel, startDatel+5184000000L);
		
		if(statusStartDate == true)
		{
			startDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
			final int yearTemp = year, monthTemp=monthOfYear, dayTemp=dayOfMonth;
			statusEndDate = true;
			statusStartDate = false;
			dialogDate = new DatePickerDialog(POSCreditScreen.this,POSCreditScreen.this, year, month, day);
			c = Calendar.getInstance();
			Long maxdate;
			dateInMillis = c.getTimeInMillis();
			maxdate = (startDatel+(5184000000L))>dateInMillis? dateInMillis:(startDatel+(5184000000L));
			dialogDate.getDatePicker().setMaxDate(maxdate);//startDatel+(5184000000L)
        	dialogDate.getDatePicker().setMinDate(startDatel);
	        dialogDate.setTitle("End Date (max 60 days)");
	        dialogDate.setButton(DialogInterface.BUTTON_NEUTRAL, "Back", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	               //Your code
	            	statusStartDate = true;
	            	
					dialogDate = new DatePickerDialog(POSCreditScreen.this, POSCreditScreen.this, yearTemp, monthTemp, dayTemp);
				        if(statusStartDate==true){		
						        dialogDate.getDatePicker().setMaxDate(new Date().getTime());//-(5097600000L))
						        dialogDate.setTitle("Select Start Date");
						        dialogDate.show();			        
					    }	
	            }
	        });
	        dialogDate.show();	
		}
		else if(statusEndDate == true)
		{
			endDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
			creditAdapter.dateFilter(startDatel, endDatel+(86400000L));			
			String pattern = "d-MMM, yyyy";			
			
		    SimpleDateFormat format = new SimpleDateFormat(pattern);
			dateRange.setText(format.format(new Date(startDatel))+"  -  "+format.format(new Date(endDatel)));
		}
		else if(statusPopup == true)
		{
			date.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);
			statusPopup = false;
		}
	}
	
	
	
	OnItemSelectedListener ModeSelectedListner = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
		Spinner spinModePayment = (Spinner) parent;
			
			
			if (spinModePayment.getId() == R.id.mode_payment) {
				modeType = (String) spinModePayment.getSelectedItem();
				modeNo = position;
				if (position > 0 && !modeType.equalsIgnoreCase("Cash")) {
					payment_mode_layout.setVisibility(View.VISIBLE);
					paymentMode_not_cash=true;		
					if (modeType != null
							&& (modeType.equalsIgnoreCase("RTGS") || (modeType
									.equalsIgnoreCase("Credit Card")))) {
						chequeNo.setVisibility(View.GONE);
						bankNameOrRefNum.setHint("Ref Number");
						remarkOrDes.setHint("Description");
						ref_number="Referenece Number";

					} else {
						chequeNo.setVisibility(View.VISIBLE);
						bankNameOrRefNum.setHint("Bank name");
						remarkOrDes.setHint("Remark");
						ref_number="Bank Name";
					}
				}
				else
				{
					paymentMode_not_cash=false;
					payment_mode_layout.setVisibility(View.GONE);
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		

		}
	};
	
	
	
    private boolean  PaymentModeValidation()
    {
    	chequeNoVal = chequeNo.getText().toString().trim();
		dateVal = date.getText().toString().trim();
		bankNameOrRefNumVal = bankNameOrRefNum.getText().toString().trim();
		remarkOrDesVal = remarkOrDes.getText().toString().trim();
		
		if(modeType.equalsIgnoreCase("Cheque"))
		{
			if (chequeNoVal == null || chequeNoVal.equalsIgnoreCase("")) {
				Toast.makeText(this, "Please enter cheque number",
						Toast.LENGTH_LONG).show();
				return  false ;
			}
		}
		
		if (dateVal == null || dateVal.equalsIgnoreCase("")) {
			Toast.makeText(this, "Please enter date",
					Toast.LENGTH_LONG).show();
			return  false;
		} else if (bankNameOrRefNumVal == null
				|| bankNameOrRefNumVal.equalsIgnoreCase("")) {
			Toast.makeText(this, "Please enter "+ref_number,
					Toast.LENGTH_LONG).show();
			return  false;
		} else {
			
		}
    	return  true ;
    }
	
	
	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			Spinner spinSelected = (Spinner) parent;
			String selectedItem = (String) spinSelected.getSelectedItem();
			searchBox.setText("");
					
			if(selectedItem.equals("Last 50") || selectedItem.equals(""))
				searchBox.setEnabled(false);
			else 
				searchBox.setEnabled(true);
			
			if(selectedItem.equals("Date") && creditAdapter  != null)
			{
				searchBox.setVisibility(View.GONE);
				dateSearch.setVisibility(View.VISIBLE);
				dateImage.setVisibility(View.VISIBLE);
				dateRange.setVisibility(View.VISIBLE);
			}			
			else if (creditAdapter != null) {
				creditAdapter.clearFilters();
				creditAdapter.setSelectedItemType(selectedItem);
				searchBox.setVisibility(View.VISIBLE);
				dateSearch.setVisibility(View.GONE);				
				dateImage.setVisibility(View.INVISIBLE);
				dateRange.setVisibility(View.INVISIBLE);
				
				if(selectedItem.equals("Credit >"))
					searchBox.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
				else 
					searchBox.setInputType(InputType.TYPE_CLASS_TEXT);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			creditAdapter.setSelectedItemType(null);
		}
	};

	private void fetchCreditBalance() {

		if (Utils.isDeviceOnline(this)) {
			showLoading();
			Map map = new HashMap<>();
			map.put("Page", "FetchCreditDetails");
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
	
	@SuppressWarnings({ "unchecked", "unused" })
	private void AddCreditPayemnt(String paidAmount) {

		if (Utils.isDeviceOnline(this)) {
			showLoading();
			Map map = new HashMap<>();
			map.put("Page", "InsertPartPaymentDetail");
			map.put("OrderID", OrderID);
			map.put("contactID", contactID);
			map.put("PaidAmount", paidAmount);
			map.put("PayMode", ""+modeNo);
			map.put("PayModeNo", chequeNoVal);
			map.put("ChequeDate", dateVal);
			map.put("BankName", bankNameOrRefNumVal);
			map.put("Remark", remarkOrDesVal);

			map.put("userID", getUserId());
			map.put("UserLogin", getUserLogin());
			map.put("Password", getPassWord());
			
			
			Log.i("add apyment","map - "+map.toString());
			
			try {
				new RequestManager().sendPostRequest(this,
						KEY_ADD_PAYMENT, map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			showMessage("Internet connection not found");
		}
	}
	
	private void fetchModeOfPayment() {
		try {
			Map map = new HashMap<>();
			map.put("Page", "FetchModeOFPayMent");
			new RequestManager().sendPostRequest(POSCreditScreen.this,
					KEY_FETCH_MODE_PAYMENT, map);
		} catch (Exception e) {
			
		}
	}
	
	private void setModelayout(Dialog dialog ) {

		chequeNo = (EditText)dialog.findViewById(R.id.cheque_no_edit);
		bankNameOrRefNum = (EditText)dialog.findViewById(R.id.bank_name_edit);
		remarkOrDes = (EditText)dialog.findViewById(R.id.remark_edit);
		//inputLayout = (FrameLayout)dialog.findViewById(R.id.input_layout);
		//calendarLayout = (FrameLayout)dialog.findViewById(R.id.calendar_layout);
		date = (TextView)dialog.findViewById(R.id.date_id);
		/*calenderView = (CalendarView)dialog.findViewById(R.id.calendarView);
		calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
					@Override
					public void onSelectedDayChange(CalendarView calendarView,
							int i, int i1, int i2) {
						date.setText(i1 + "/" + i2 + "/" + i);
						inputLayout.setVisibility(View.VISIBLE);
						calendarLayout.setVisibility(View.GONE);
					}

				});*/
		
		
		date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				inputLayout.setVisibility(View.GONE);
//				calendarLayout.setVisibility(View.VISIBLE);	
				dialogDate = new DatePickerDialog(POSCreditScreen.this, POSCreditScreen.this, year, month, day);
		        dialogDate.getDatePicker().setMaxDate((new Date().getTime()));//-(5097600000L)
//				dialogDate.setTitle("Select Start Date");	
		        statusPopup = true;
				dialogDate.show();			        
			    
			}
		});
	}
	
	private void posPaymentPopup(String balanceAmount)
	{
		
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.pos_addpaymentpoup);
		dialog.setTitle("Add Payment");		
		billingAmtEdit = (EditText)dialog.findViewById(R.id.billing_amt);
		billingAmtEdit.setEnabled(false);
		billingAmtEdit.setText(balanceAmount);
		paidAmtEdit = (EditText)dialog.findViewById(R.id.paid_amt);
		balanceEdit = (EditText) dialog.findViewById(R.id.balance);
		balanceEdit.setEnabled(false);
		modePaymentSpinner = (Spinner)dialog.findViewById(R.id.mode_payment);
		payment_mode_layout= (RelativeLayout)dialog.findViewById(R.id.payment_mode_layout);
		setModelayout(dialog);
		modePaymentSpinner.setAdapter(PaymentModeadapter);
		modePaymentSpinner.setOnItemSelectedListener(ModeSelectedListner);
		
		paidAmtEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				String paidAmt = paidAmtEdit.getText().toString();
				try {
					if (paidAmt != null && !paidAmt.equalsIgnoreCase("")) {
						float amt = Float.parseFloat(paidAmt);
						balanceEdit.setText("");
						if (billingAmtEdit.getText() != null
								&& !billingAmtEdit.getText().toString().trim()
								.equalsIgnoreCase("")) {

							float billingAmt = Float.parseFloat(billingAmtEdit
									.getText().toString().trim());
							if (amt > billingAmt) {
								paidAmtEdit.setText(""+billingAmt);
								Toast.makeText(POSCreditScreen.this,"Paid Amount is not greater then bill amount",Toast.LENGTH_LONG).show();
								//paidAmtEdit.setHint("Paid amount");
							} else {

								float bal = billingAmt - amt;
								DecimalFormat f = new DecimalFormat("##.00");
								if(bal==0) balanceEdit.setText("0");
								else balanceEdit.setText("" + f.format(bal));
							}
						}
					}
				} catch(Exception e){ e.printStackTrace();}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void afterTextChanged(Editable s) {
			

			}
		});
		
		Button addMainBtn = (Button)dialog.findViewById(R.id.btn_add_without_invoice);
		//Button addInvoiceBtn = (Button)dialog.findViewById(R.id.btn_add_invoice);
		
		addMainBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				if(paidAmtEdit.getText().toString().length()>0)
				{
					if(modeNo==0)
					{
						Toast.makeText(getApplicationContext(), "Select Payment Mode", Toast.LENGTH_SHORT).show();
					}
					else if(modeNo==1)
					{
						AddCreditPayemnt(paidAmtEdit.getText().toString());
						dialog.cancel();
					}
					else
					{
						if(PaymentModeValidation()) 
						{
							AddCreditPayemnt(paidAmtEdit.getText().toString());
							dialog.cancel();
						}						
						
					}
					
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Enter The Paid Amount", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		
		
		dialog.show();
	}

	@Override
	public void onReceiveResponse(int requestId, String response) {
		// TODO Auto-generated method stub
		hideLoading();
		if (response != null && response.length() > 0) {
			if (requestId == KEY_FETCH_CREDIT_BALANCE) {
				Gson gson = new Gson();
				CreditModelData creditData = gson.fromJson(response, CreditModelData.class);
				ArrayList<CreditDetails> creditList = creditData.getCreditDetails();
					Iterator<CreditDetails> i = creditList.iterator();
				while(i.hasNext())
				{
					
					if(Double.parseDouble(i.next().getBalanceAmount())<=1)
					{
//						Log.d("credit ",creditList.get(i).toString());
						i.remove();
					}
				}
				Log.d("credit ",creditList.toString());
				creditAdapter = new POSCreditAdapter(this,creditList);
				
				creditListView.setAdapter(creditAdapter);
				creditListView.setEmptyView(findViewById(R.id.credit_emptyElement));
			}			
			else if (requestId == KEY_FETCH_MODE_PAYMENT) {
				ModelManager.getInstance().setFetchModeOfPaymentModel(response);
				ArrayList<String> list = new ArrayList<>();
				list.add("Select mode of payment");
				if (ModelManager.getInstance().getFetchModeOfPaymentModel() != null
						&& ModelManager.getInstance()
								.getFetchModeOfPaymentModel()
								.getFetchModeOfPaymentList() != null
						&& ModelManager.getInstance()
								.getFetchModeOfPaymentModel()
								.getFetchModeOfPaymentList().size() > 0) {
					for (int k = 0; k < ModelManager.getInstance()
							.getFetchModeOfPaymentModel()
							.getFetchModeOfPaymentList().size(); k++) {
						list.add(ModelManager.getInstance()
								.getFetchModeOfPaymentModel()
								.getFetchModeOfPaymentList().get(k).getMode());
					}
				}
				PaymentModeadapter = new ArrayAdapter<>(this,
						android.R.layout.simple_spinner_item, list);
				PaymentModeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);				
				
			}
			else if(requestId==KEY_ADD_PAYMENT)
			{
				Log.i("Response","Response- "+response);
				fetchCreditBalance();
			}
		} else {
			showMessage("Please try again.");
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
		totalRecord.setText("Records : "+text);
	}
	
}
