package com.kitever.pos.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.MultiChoiceModeListener;
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
import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSInvoiceAdapter;
import com.kitever.pos.adapter.POSInvoiceDetailAdapter;
import com.kitever.pos.model.data.FetchInvoice;
import com.kitever.pos.model.data.POSInvoicePopUp;
import com.kitever.pos.model.data.PosInvoiceData;
import com.kitever.pos.model.data.ReceiptDetail;
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

public class POSInvoiceScreen extends PosBaseActivity implements DatePickerDialog.OnDateSetListener, NetworkManager, TotalRows{
	String link = "https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf";
	private int KEY_FETCH_ALL_INVOICE_DETAILS = 1, KEY_SEND_SMS_MAIL = 2, KEY_FETCH_INVOICE_DETAILS = 3;
	private long startDatel, endDatel;
	private LinearLayout searchLayout, actionLayout, dateRangeLayout;
	private ListView invoiceListView, invoiceDetailListView;
	private POSInvoiceAdapter invoiceAdapter;
	private POSInvoiceDetailAdapter invoiceDetailAdapter;
	private ArrayList<FetchInvoice> invoiceList;
	
	private EditText searchBox;
	private TextView startDateLable, startDate, endDateLabel, endDate, totalRows, dateRange, noRecordImage;
	private ImageView advanceSearch, smsSelect, mailSelect, printSelect, dateImage ;
	private SpinnerReselect dateSearch;
	private boolean statusSms = true, statusMail = true, statusPrint = true; 
	private int dateSelected = 1;	
	private Button button;
	private boolean statusStartDate = false, statusEndDate = false;
	
	//Dialog Data
	private TextView contactName, invoiceCode,invoiceContact;
	private ImageView profileImage;
	
	private SimpleDateFormat format;
	
    private DatePickerDialog dialogDate;
    private Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    private Long dateInMillis;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setScreenName("Invoices");
		setBottomAction(true, true, true,true, true, true,true,true,true,true, false,true);
		setLayoutContentView(R.layout.pos_invoice_layout);
		
		setScreen();			
		fetchInvoiceALL();
		dateInMillis = DateHelper.getDate(year, month, day);
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
		
		Spinner selectType = (Spinner) findViewById(R.id.select_type_invoice_spinner);
		searchBox = (EditText) findViewById(R.id.edit_invoice_search);
		
		dateRangeLayout = (LinearLayout) findViewById(R.id.pos_invoice_date_range_layout);
		actionLayout = (LinearLayout) findViewById(R.id.pos_invoice_action_layout);
		searchLayout = (LinearLayout) findViewById(R.id.pos_invoice_search_layout);
		advanceSearch = (ImageView) findViewById(R.id.advance_invoice_search);
		dateSearch = (SpinnerReselect) findViewById(R.id.date_invoice_search);
		noRecordImage = (TextView) findViewById(R.id.no_record);
		
		MoonIcon mIcon=new MoonIcon(this);
		mIcon.setTextfont(noRecordImage);
		
		dateRange = (TextView) findViewById(R.id.pos_invoice_date_range);
		dateImage = (ImageView) findViewById(R.id.invoice_image_date);
		
		invoiceListView = (ListView) findViewById(R.id.pos_invoice_list_view);	
		
		smsSelect = (ImageView) findViewById(R.id.invoice_image_sms);
		mailSelect = (ImageView) findViewById(R.id.invoice_image_mail);
		printSelect = (ImageView) findViewById(R.id.invoice_image_print);
		totalRows = (TextView) findViewById(R.id.invoice_total_rows);
		button = (Button) findViewById(R.id.invoice_button_send);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMailSms( invoiceAdapter.getArrayList());
			}
		});
		
		button.setEnabled(false);
		
		invoiceList = new ArrayList<>();
		invoiceAdapter = new POSInvoiceAdapter(this,invoiceList);	
		invoiceListView.setAdapter(invoiceAdapter);

            	
		ArrayList<String> typeList = new ArrayList<String>();
		typeList.add("Last 50");		
		typeList.add("Invoice Code");
		typeList.add("Contact");		
		typeList.add("Bill Amount");
		typeList.add("Balance Amount");		
		typeList.add("Order Date");
		
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, typeList);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectType.setAdapter(typeAdapter);
		selectType.setOnItemSelectedListener(itemSelectedListener);

		// if (categoryAdapter != null) {
		searchBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				if (invoiceAdapter != null) {
					POSInvoiceScreen.this.invoiceAdapter.getFilter()
							.filter(cs);
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
		
		smsSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(statusSms)
				{
					statusSms=false;
					smsSelect.setImageDrawable(ContextCompat.getDrawable(POSInvoiceScreen.this,R.drawable.checkbox_blank_settings));
				}
				else
				{
					statusSms=true;
					smsSelect.setImageDrawable(ContextCompat.getDrawable(POSInvoiceScreen.this,R.drawable.checkbox_settings));
				}
			}
		});
		
		mailSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(statusMail)
				{
					statusMail=false;
					mailSelect.setImageDrawable(ContextCompat.getDrawable(POSInvoiceScreen.this,R.drawable.checkbox_blank_settings));
				}
				else
				{
					statusMail=true;
					mailSelect.setImageDrawable(ContextCompat.getDrawable(POSInvoiceScreen.this,R.drawable.checkbox_settings));
				}
				
			}
		});
		
		printSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(statusPrint)
				{
					statusPrint=false;
					printSelect.setImageDrawable(ContextCompat.getDrawable(POSInvoiceScreen.this,R.drawable.checkbox_blank_settings));
				}
				else
				{
					statusPrint=true;
					printSelect.setImageDrawable(ContextCompat.getDrawable(POSInvoiceScreen.this,R.drawable.checkbox_settings));
				}
			}
		});
		
		invoiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				showDialogInvoice(position);
			}			
		});
		
										
		invoiceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            	
            	invoiceListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                // Capture ListView item click
            			
            	invoiceListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
					
					@Override
					public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
						// TODO Auto-generated method stub
						return false;
					}
					
					@Override
					public void onDestroyActionMode(ActionMode mode) {
						// TODO Auto-generated method stub
						invoiceAdapter.emptyArray();
						searchLayout.setVisibility(View.VISIBLE);						
						actionLayout.setVisibility(View.GONE);
						totalRows.setVisibility(View.VISIBLE);
						button.setEnabled(false);
					}
					
					@Override
					public boolean onCreateActionMode(ActionMode mode, Menu menu) {
						// TODO Auto-generated method stub
//						  mode.getMenuInflater().inflate(R.menu.pos_invoice_action_setting, menu);
						  searchLayout.setVisibility(View.GONE);
						  actionLayout.setVisibility(View.VISIBLE);
						  totalRows.setVisibility(View.GONE);						  
						  button.setEnabled(true);
	                        return true;
					}
					
					@Override
					public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
						// TODO Auto-generated method stub
						 if (item.getItemId() == R.id.invoice_send){
							 
							 mode.finish();							 
							 return true;
                    }
						 return false;
					}
					
					@Override
					public void onItemCheckedStateChanged(ActionMode mode, int position,
							long id, boolean checked) {
						// TODO Auto-generated method stub
						 mode.setTitle(invoiceListView.getCheckedItemCount() + " Selected");
						 
	                        // Toggle the state of item after every click on it
	                        invoiceAdapter.toggleSelection(position);
					}
				});
            	
				return statusMail;
            }
		});
		
		dateSearch.setOnItemSelectedListener(new OnItemSelectedListener() {
		
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				System.out.println("selected");
				String pattern = "d-MMM, yyyy";	
				format= new SimpleDateFormat(pattern);
				
				if(position == 1)
				{
					invoiceAdapter.dateRangeFilter(position);					
					dateRange.setText(""+DateHelper.getTodayDate());
					
				}	else if(position == 2)
				{
					invoiceAdapter.dateRangeFilter(position);					
					dateRange.setText(""+DateHelper.getYestardayDate(dateInMillis));
				}
				else if(position == 3)
				{
					invoiceAdapter.dateRangeFilter(position);
					dateRange.setText(DateHelper.getFirstDayOfWeek()+" - "+format.format(new Date(dateInMillis)));
				}
				else if(position ==4)
				{
					invoiceAdapter.dateRangeFilter(position);
					dateRange.setText(DateHelper.getFirstDayOfMonth()+" - "+format.format(new Date(dateInMillis)));
				}
				else if(position == 5)
				{
					invoiceAdapter.dateRangeFilter(position);
					
					dateRange.setText(DateHelper.getLastMonth());
				}
				else if(position==6)
					{    
					statusStartDate = true;
					dialogDate = new DatePickerDialog(POSInvoiceScreen.this, POSInvoiceScreen.this, year, month, day);
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

	private OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

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
			
			if( invoiceAdapter  != null && selectedItem.equals("Order Date") )
			{
				searchBox.setVisibility(View.GONE);
				dateSearch.setVisibility(View.VISIBLE);				
				dateRange.setVisibility(View.VISIBLE);
				dateRange.setText("Last 50 Records");				
				dateImage.setVisibility(View.VISIBLE);
			}
			else if (invoiceAdapter != null) {
				invoiceAdapter.clearFilters();
				invoiceAdapter.setSelectedItemType(selectedItem);
				searchBox.setVisibility(View.VISIBLE);
				dateSearch.setVisibility(View.GONE);
				dateImage.setVisibility(View.INVISIBLE);
				dateRange.setVisibility(View.INVISIBLE);				
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

	private void showDialogInvoice(int position) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.pos_invoice_popup);
		dialog.setTitle("Invoice Detail");
		
		Button textView = (Button) dialog.findViewById(R.id.pos_invoice_detail_close);
		invoiceDetailListView = (ListView) dialog.findViewById(R.id.pos_invoice_detail_listview);
		contactName = (TextView) dialog.findViewById(R.id.pos_invoice_contact_name);
		invoiceCode = (TextView) dialog.findViewById(R.id.pos_invoice_item_code);
		profileImage = (ImageView) dialog.findViewById(R.id.pos_invoice_item_image);
		invoiceContact= (TextView) dialog.findViewById(R.id.pos_invoice_contact_number);
		
		invoiceCode.setText("Invoice No:"+invoiceAdapter.getAllArrayList().get(position).getInvoiceCode());
		contactName.setText("Name: "+invoiceAdapter.getAllArrayList().get(position).getCustomerName());
		invoiceContact.setText("Number: "+invoiceAdapter.getAllArrayList().get(position).getContact_Mobile());
		fetchInvoiceDetail(position);

		textView.setOnClickListener(new OnClickListener() {
			
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
	
	private void fetchInvoiceALL() {
		if (sms19.inapp.msg.constant.Utils.isDeviceOnline(this)) {
			try {
				showLoading();
				Map map = new HashMap<>();
				map.put("Page", "FetchInvoice");
//				map.put("userID", "589");
//				map.put("UserLogin","+918512851664");
//				map.put("Password","103641");

				map.put("userID", getUserId());
				map.put("UserLogin",getUserLogin());
				map.put("Password",getPassWord());
				
				new RequestManager().sendPostRequest(POSInvoiceScreen.this,
						KEY_FETCH_ALL_INVOICE_DETAILS, map);
			} catch (Exception e) {
				
			}
		} else {
			showMessage("Internet connection not found");
		}

	}
	
	private void fetchInvoiceDetail(int position) {
		if (sms19.inapp.msg.constant.Utils.isDeviceOnline(this)) {
			try {
				showLoading();
				Map map = new HashMap<>();
				map.put("Page", "GetReceiptDetail");
//				map.put("userID", "589");
//				map.put("UserLogin","+918512851664");
//				map.put("Password","103641");

				map.put("userID", getUserId());
				map.put("UserLogin",getUserLogin());
				map.put("Password",getPassWord());
				map.put("InvoiceNo",invoiceAdapter.getAllArrayList().get(position).getInvoiceCode());				

				new RequestManager().sendPostRequest(POSInvoiceScreen.this,
						KEY_FETCH_INVOICE_DETAILS, map);
			} catch (Exception e) {
				
			}
		} else {
			showMessage("Internet connection not found");
		}

	}
	
	private void sendMailSms(ArrayList<FetchInvoice> arrayList)
	{
		if(sms19.inapp.msg.constant.Utils.isDeviceOnline(this)){
			try
			{
				Gson gson = new Gson();
				
				showLoading();
				Map map = new HashMap<>();
				map.put("Page", "MailMsgSendUsingChk");
				map.put("chkSms",  String.valueOf(statusSms));
				map.put("chkEmail", String.valueOf(statusMail));
				JSONArray array = new JSONArray(gson.toJson(arrayList));
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("SmsMail",array);
				map.put("data", jsonObject.toString());
//				System.out.println("Sms "+arrayList.toString()+" map "+map.toString());
				new RequestManager().sendPostRequest(POSInvoiceScreen.this,
						KEY_SEND_SMS_MAIL, map);
			}catch (Exception e) {
				
			}
		} else {
			showMessage("Internet connection not found");
		}

	}
	
	
	private void setTaxScreen() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.invoice_filter_layout);
		dialog.setTitle("Filters List");
		startDateLable = (TextView) dialog.findViewById(R.id.invoice_start_date);
		startDate = (TextView) dialog.findViewById(R.id.invoice_date);
		endDateLabel = (TextView) dialog.findViewById(R.id.invoice_end_date_lable);
		endDate = (TextView) dialog.findViewById(R.id.invoice_end_date);
		
		final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);


        startDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dateSelected = 1;		        // Create a new instance of DatePickerDialog and return it
		        DatePickerDialog dialogDate = new DatePickerDialog(POSInvoiceScreen.this, POSInvoiceScreen.this, year, month, day);
		        dialogDate.getDatePicker().setMaxDate(new Date().getTime());
		        dialogDate.show();
			}
		});
		
        endDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dateSelected = 0;			
				
		     // Create a new instance of DatePickerDialog and return it
		        DatePickerDialog dialogDate = new DatePickerDialog(POSInvoiceScreen.this, POSInvoiceScreen.this, year, month, day);
		        dialogDate.getDatePicker().setMaxDate(new Date().getTime());
		        
		        dialogDate.show();
//		       
			}
		});
		Button doneBtn = (Button) dialog.findViewById(R.id.invoice_done_btn);
		doneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				invoiceAdapter.dateFilter(startDatel, endDatel+86400000);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
     
	@SuppressWarnings("deprecation")
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		
		if(statusStartDate == true)
		{
			startDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
			final int yearTemp = year, monthTemp=monthOfYear, dayTemp=dayOfMonth;
			statusEndDate = true;
			statusStartDate = false;
			
			dialogDate = new DatePickerDialog(POSInvoiceScreen.this,POSInvoiceScreen.this, year, month, day);
			c= Calendar.getInstance();
			Long maxdate;
			dateInMillis = c.getTimeInMillis();
			maxdate = (startDatel+(5184000000L))>dateInMillis? dateInMillis:(startDatel+(5184000000L));
			dialogDate.getDatePicker().setMaxDate(maxdate);
        	dialogDate.getDatePicker().setMinDate(startDatel);
	        dialogDate.setTitle("End Date (max 60 days)");
	        dialogDate.setButton(DialogInterface.BUTTON_NEUTRAL, "Back", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	               //Your code
	            	statusStartDate = true;
	            	
					dialogDate = new DatePickerDialog(POSInvoiceScreen.this, POSInvoiceScreen.this, yearTemp, monthTemp, dayTemp);
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
			invoiceAdapter.dateFilter(startDatel, endDatel+(86400000L));			
			String pattern = "d-MMM, yyyy";			
			
		    SimpleDateFormat format = new SimpleDateFormat(pattern);
			dateRange.setText(format.format(new Date(startDatel))+"  -  "+format.format(new Date(endDatel)));
		}

	}

	@Override
	public void onReceiveResponse(int requestId, String response) {
		// TODO Auto-generated method stub
		hideLoading();
		if (response != null && response.length() > 0) {
			if (requestId == KEY_FETCH_ALL_INVOICE_DETAILS) {
				
				Gson gson = new Gson();				
				PosInvoiceData vc = gson.fromJson(response, PosInvoiceData.class);				
				for(int i=0;i<vc.getFetchInvoice().size();i++)
				{
					invoiceList.add(vc.getFetchInvoice().get(i));
				}	
				invoiceAdapter.notifyDataSetChanged();
				invoiceListView.setEmptyView(findViewById(R.id.invoice_emptyElement));
			} else if( requestId == KEY_SEND_SMS_MAIL)
			{
				try {
					JSONObject jsonObject = new JSONObject(response);
					JSONArray array = jsonObject.getJSONArray("SendInvoice");
					JSONObject jsonObject2 = array.getJSONObject(0);
					Toast.makeText(this, jsonObject2.getString("Message"), Toast.LENGTH_LONG).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			else if(requestId == KEY_FETCH_INVOICE_DETAILS)
			{				
				Gson gson = new Gson();
				POSInvoicePopUp receiptDetail = gson.fromJson(response, POSInvoicePopUp.class);

				ArrayList<ReceiptDetail>	arr = receiptDetail.getReceiptDetail();
				invoiceDetailAdapter = new POSInvoiceDetailAdapter(this,arr);
				invoiceDetailListView.setAdapter(invoiceDetailAdapter);	
			}
		}
		else 
		{
			
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		hideLoading();
		Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void totalRows(String text) {
		// TODO Auto-generated method stub
		totalRows.setText("Records : "+text);	
	}
}
