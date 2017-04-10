package com.kitever.pos.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSCustomerAdapter;
import com.kitever.pos.model.CustomerList;
import com.kitever.pos.model.data.CustomerObject;
import com.kitever.utils.DateHelper;
import com.kitever.utils.SpinnerReselect;
import com.kitever.utils.TotalRows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

public class PosCustomerList extends PosBaseActivity implements NetworkManager, DatePickerDialog.OnDateSetListener,TotalRows{

	private final int KEY_FETCH_CUSTOMER_LIST = 1;
	private Spinner spinner;
	private SpinnerReselect dateSearch;
	private EditText etSearch;
	private TextView totalRows, dateRange, noRecord;
	private ImageView dateImage;
	private POSCustomerAdapter customerAdapter ;
	private ListView lvPOSCustomer;
	private ArrayList<CustomerList> customerList;
	private long startDatel, endDatel;
	private boolean statusEndDate = false, statusStartDate = false;
	private LinearLayout dateRangeLayout;
	private DatePickerDialog dialogDate;
	
	private SimpleDateFormat format;
    private Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    
    private Long dateInMillis;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreenName("Customers");
		dateInMillis = c.getTimeInMillis();
		setBottomAction(true, true, true, false,true, true,true,true,true,true, true,true);
		setLayoutContentView(R.layout.activity_pos_customer_list);		
		setScreen();
		fetchCustomerList();		
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
		// TODO Auto-generated method stub
		etSearch = (EditText) findViewById(R.id.customer_edit_search);
		spinner = (Spinner) findViewById(R.id.customer_type_spinner);
		dateSearch = (SpinnerReselect) findViewById(R.id.date_customer_search);
		lvPOSCustomer = (ListView) findViewById(R.id.pos_customer_list_view);
		totalRows = (TextView) findViewById(R.id.customer_total_rows);
		
		dateImage = (ImageView) findViewById(R.id.pos_customer_image_date);
		dateRange = (TextView) findViewById(R.id.pos_customer_date_range);
		dateRangeLayout = (LinearLayout) findViewById(R.id.pos_invoice_date_range_layout);
		
		
		noRecord = (TextView) findViewById(R.id.no_record);
		MoonIcon mIcon=new MoonIcon(this);
		mIcon.setTextfont(noRecord);
		
//		customerList = new ArrayList<>();
//		customerAdapter = new POSCustomerAdapter(customerList, this);
//		lvPOSCustomer.setAdapter(customerAdapter);
		
		ArrayList<String> typeList = new ArrayList<String>();	
		typeList.add("Top 50");
		typeList.add("Contact");
		typeList.add("Credit >");
		typeList.add("Order Date");
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, typeList);	
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(typeAdapter);
		
		spinner.setOnItemSelectedListener(itemSelectedListener);
		
		etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(customerAdapter!=null)
					PosCustomerList.this.customerAdapter.getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});		
		
		
		dateSearch.setOnItemSelectedListener(new OnItemSelectedListener() {			

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String pattern = "d-MMM, yyyy";	
				format= new SimpleDateFormat(pattern);
				
				if(position == 1)
				{
					customerAdapter.dateRangeFilter(position);					
					dateRange.setText(""+DateHelper.getTodayDate());
					
				}	else if(position == 2)
				{
					customerAdapter.dateRangeFilter(position);					
					dateRange.setText(""+DateHelper.getYestardayDate(DateHelper.getDateInmillis()));
				}
				else if(position == 3)
				{
					customerAdapter.dateRangeFilter(position);
					dateRange.setText(DateHelper.getFirstDayOfWeek()+" - "+format.format(new Date(DateHelper.getDateInmillis())));
				}
				else if(position ==4)
				{
					customerAdapter.dateRangeFilter(position);
					dateRange.setText(DateHelper.getFirstDayOfMonth()+" - "+format.format(new Date(DateHelper.getDateInmillis())));
				}
				else if(position == 5)
				{
					customerAdapter.dateRangeFilter(position);
					
					dateRange.setText(DateHelper.getLastMonth());
				}
				else if(position==6)
					{    
					statusStartDate = true;
					dialogDate = new DatePickerDialog(PosCustomerList.this, PosCustomerList.this, year, month, day);
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
		etSearch.setEnabled(false);
	}

	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			Spinner spinSelected = (Spinner) parent;
			String selectedItem = (String) spinSelected.getSelectedItem();

			etSearch.setText("");
			if(selectedItem.equals("Top 50") || selectedItem.equals(""))
				etSearch.setEnabled(false);
			else 
				etSearch.setEnabled(true);
			
			if(selectedItem.equals("Order Date") && customerAdapter  != null)
			{
				
				etSearch.setVisibility(View.GONE);
				dateSearch.setVisibility(View.VISIBLE);
				
				dateImage.setVisibility(View.VISIBLE);
				dateRange.setVisibility(View.VISIBLE);
			}
			
			else if (customerAdapter != null) {
				
				customerAdapter.clearFilters();
				customerAdapter.setSelectedItemType(selectedItem);
				etSearch.setVisibility(View.VISIBLE);
				dateSearch.setVisibility(View.GONE);
				dateImage.setVisibility(View.INVISIBLE);
				dateRange.setVisibility(View.INVISIBLE);
				if(selectedItem.equals("Credit >"))
					etSearch.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
				else 
					etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
			}
		
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
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
			dialogDate = null;
			dialogDate = new DatePickerDialog(PosCustomerList.this,PosCustomerList.this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			c= Calendar.getInstance();
			Long maxdate;
			dateInMillis = c.getTimeInMillis();
			maxdate = (startDatel+(5184000000L))>dateInMillis? dateInMillis:(startDatel+(5184000000L));
			dialogDate.getDatePicker().setMaxDate(maxdate);//startDatel+(5184000000L)
        	dialogDate.getDatePicker().setMinDate(startDatel);

	        dialogDate.setButton(DialogInterface.BUTTON_NEUTRAL, "Back", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	               //Your code
	            	statusStartDate = true;
	            	
					dialogDate = new DatePickerDialog(PosCustomerList.this, PosCustomerList.this, yearTemp, monthTemp, dayTemp);
				        if(statusStartDate==true){		
						        dialogDate.getDatePicker().setMaxDate((new Date().getTime()));//-(5097600000L)
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
			customerAdapter.dateFilter(startDatel, endDatel+(86400000L));			
			String pattern = "d-MMM, yyyy";			
			
		    SimpleDateFormat format = new SimpleDateFormat(pattern);
			dateRange.setText(format.format(new Date(startDatel))+"  -  "+format.format(new Date(endDatel)));
		}
	}

	
	private void fetchCustomerList() {

		if (Utils.isDeviceOnline(this)) {
			showLoading();
			Map map = new HashMap<>();
			map.put("Page", "FetchCustomerList");
			map.put("userID", getUserId());
			map.put("UserLogin", getUserLogin());
			map.put("Password", getPassWord());
			System.out.println("Customer list");
			try {
				new RequestManager().sendPostRequest(this,
						KEY_FETCH_CUSTOMER_LIST, map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			showMessage("Internet connection not found");
		}
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.pos_customer_list, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public void onReceiveResponse(int requestId, String response) {
		// TODO Auto-generated method stub
		hideLoading();
		
		if (response != null && response.length() > 0) {			
			if (requestId == KEY_FETCH_CUSTOMER_LIST) {
				Gson gson = new Gson();
				CustomerObject customerObject =  gson.fromJson(response, CustomerObject.class);
				customerList = (ArrayList<CustomerList>) customerObject.getCustomerList(); 
				customerAdapter = new POSCustomerAdapter(customerList, this);
				lvPOSCustomer.setAdapter(customerAdapter);
				lvPOSCustomer.setEmptyView(findViewById(R.id.customer_emptyElement));
			}			
		}
		 else {
			 	customerList = new ArrayList<>(); 
				customerAdapter = new POSCustomerAdapter(customerList, this);
				lvPOSCustomer.setAdapter(customerAdapter);
				lvPOSCustomer.setEmptyView(findViewById(R.id.customer_emptyElement));
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
		totalRows.setText("Records : "+text);
	}
}
