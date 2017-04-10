package com.kitever.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;
import com.kitever.customviews.FloatingActionButton;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSOtherChargeAdapter;
import com.kitever.pos.model.data.OTCList;
import com.kitever.utils.TotalRows;

public class POSOtherChargeScreen extends PosBaseActivity implements
NetworkManager, OnClickListener,  TotalRows {
	private static final int ADD_CHARGES = 1;
	private TextView noRecord, totalRecords, dateRange;
	private ImageView dateImage;
	private ListView chargeListView;
	private final int KEY_FETCH_CHARGES_LIST = 1;
	private POSOtherChargeAdapter chargesAdapter;
	private final int KEY_DELETE_TAX = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setScreenName("Other Charges");
	setBottomAction(true, true, true,true, true, true,true,true,false,true, true,true);
	setLayoutContentView(R.layout.activity_posother_charge_screen);
	setScreen();
	fetchOtherCharges();
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
	
	@Override
	protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	if (chargesAdapter != null) {
//		taxAdapter.setModelData(ModelManager.getInstance().getTaxModel()
//				.getFetchTaxList());
//		taxAdapter.notifyDataSetChanged();
	}
	}
	
	private void setScreen() {
	Spinner selectType = (Spinner) findViewById(R.id.other_select_type_spinner);
	EditText searchBox = (EditText) findViewById(R.id.other_edit_search);
	ImageView adavanceSearch = (ImageView) findViewById(R.id.other_advance_search);
	chargeListView = (ListView) findViewById(R.id.other_tax_list_view);
	noRecord = (TextView) findViewById(R.id.other_no_record);
	totalRecords = (TextView) findViewById(R.id.other_tax_total_rows);
	
	MoonIcon mIcon=new MoonIcon(this);
	mIcon.setTextfont(noRecord);
	

	
	
	FloatingActionButton fabButton = null;
	fabButton = new FloatingActionButton.Builder(POSOtherChargeScreen.this)
			.withDrawable(
					getResources()
							.getDrawable(R.drawable.ic_add_white_36dp))
			.withButtonColor(Color.parseColor("#E46C22"))
			.withGravity(Gravity.BOTTOM | Gravity.RIGHT)
			.withMargins(0, 0, 16, com.kitever.utils.Utils.FLOATING_BUTTON_MARGIN).create();
	
	fabButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(POSOtherChargeScreen.this,
					POSOtherChargeAdd.class);
			intent.putExtra("screen_name", "Add Tax");
			startActivityForResult(intent,ADD_CHARGES);
	
		}
	});
	
	ArrayList<String> typeList = new ArrayList<String>();		
	typeList.add("Name");
	typeList.add("Amount");	
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
			if (chargesAdapter != null) {
				POSOtherChargeScreen.this.chargesAdapter.getFilter().filter(cs);
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
	
	}
	
	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		Spinner spinSelected = (Spinner) parent;
		String selectedItem = (String) spinSelected.getSelectedItem();
		if (chargesAdapter != null) {
			chargesAdapter.setSelectedItemType(selectedItem);
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		chargesAdapter.setSelectedItemType(null);
	}
	};
	
	private void fetchOtherCharges() {
	try {
		if (Utils.isDeviceOnline(this)) {
			showLoading();
			Map map = new HashMap<>();
			map.put("Page", "FetchOTC");
			map.put("IsActive", "A");
			map.put("userID", getUserId());
			map.put("UserLogin", getUserLogin());
			map.put("Password", getPassWord());
			new RequestManager().sendPostRequest(POSOtherChargeScreen.this,
					KEY_FETCH_CHARGES_LIST, map);			
		} else {
			showMessage("Internet connection not found");
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
	}
	
	@Override
	public void onReceiveResponse(int requestId, String response) {
	// TODO Auto-generated method stub
	hideLoading();
	System.out.println("OTC "+response);
	if (response != null && response.length() > 0) {
		if (requestId == KEY_FETCH_CHARGES_LIST) {
			Gson gson = new Gson();
			OTCList otclist = gson.fromJson(response, OTCList.class);
			//System.out.println("OTC "+otclist.getOTC().toString());
			if (otclist.getStatus() != null
					&& otclist.getStatus().equals("true")) {
					chargesAdapter = new POSOtherChargeAdapter(this, otclist.getOTC());
					chargeListView.setAdapter(chargesAdapter);
			} else {
				if (otclist.getStatus() != null
						&& otclist.getMessage()!= null){
					//Toast.makeText(this, otclist.getMessage(), Toast.LENGTH_SHORT).show();
				/*chargesAdapter = new POSOtherChargeAdapter(this, otclist.getOTC());
				chargeListView.setAdapter(chargesAdapter);
				chargeListView.setEmptyView(findViewById(R.id.emptyElement));*/
			}
		}

				// chargesAdapter.notifyDataSetChanged();
			} else {
	//			showMessage("Please try again.");
			}
	
		}
	 else {
	//	showMessage("Please try again.");
	}
}
	
	@Override
	public void onErrorResponse(VolleyError error) {
	hideLoading();
	// TODO Auto-generated method stub
	hideLoading();
	//showMessage("Please try again.");
	}
	
	@Override
	public void onClick(View v) {
	// TODO Auto-generated method stub
	super.onClick(v);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == ADD_CHARGES)
		{
			if(resultCode==RESULT_OK)
			{
				Gson gson = new Gson();
				OTCList otclist = gson.fromJson(data.getStringExtra("response"), OTCList.class);
				System.out.println("OTC "+otclist.getOTC().toString());
				if (otclist.getStatus() != null
						&& otclist.getStatus().equals("true")) {
						chargesAdapter.changeList(otclist.getOTC());
						
				}
			}
		}
	}
	
	@Override
	public void totalRows(String text) {
	// TODO Auto-generated method stub
	totalRecords.setText("Records : "+text);
	}
}