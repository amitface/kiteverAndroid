package com.kitever.pos.activity;

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

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;
import com.kitever.customviews.FloatingActionButton;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSTaxMasterAdapter;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.model.data.TaxModelData;
import com.kitever.pos.model.manager.ModelManager;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

public class POSTaxMasterScreen extends PosBaseActivity implements
		NetworkManager, OnClickListener, POSTaxMasterAdapter.Actionable, TotalRows, ExecuteService {
	private TextView noRecord, totalRecords, dateRange;
	private ImageView dateImage;
	private ListView taxListView;
	private final int KEY_FETCH_TAX_LIST = 1;
	private POSTaxMasterAdapter taxAdapter;
	private final int KEY_DELETE_TAX = 2;
	private EditText searchBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setScreenName("Taxes");
		setBottomAction(true, true, true,true, true, true,true,false,true,true, true,true);
		setLayoutContentView(R.layout.pos_tax_layout);
		setScreen();
		fetchTax();
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
		if (taxAdapter != null) {
			taxAdapter.setModelData(ModelManager.getInstance().getTaxModel()
					.getFetchTaxList());
			taxAdapter.notifyDataSetChanged();
		}
	}

	private void setScreen() {
		Spinner selectType = (Spinner) findViewById(R.id.select_type_spinner);
		searchBox = (EditText) findViewById(R.id.edit_search);
		ImageView adavanceSearch = (ImageView) findViewById(R.id.advance_search);
		taxListView = (ListView) findViewById(R.id.tax_list_view);
		noRecord = (TextView) findViewById(R.id.no_record);
		totalRecords = (TextView) findViewById(R.id.tax_total_rows);
		
		MoonIcon mIcon=new MoonIcon(this);
		mIcon.setTextfont(noRecord);
		

		
		
		FloatingActionButton fabButton = null;
		fabButton = new FloatingActionButton.Builder(POSTaxMasterScreen.this)
				.withDrawable(
						getResources()
								.getDrawable(R.drawable.ic_add_white_36dp))
				.withButtonColor(Color.parseColor("#E46C22"))
				.withGravity(Gravity.BOTTOM | Gravity.RIGHT)
				.withMargins(0, 0, 16, com.kitever.utils.Utils.FLOATING_BUTTON_MARGIN).create();

		fabButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(POSTaxMasterScreen.this,
						POSTaxAddUpdateScreen.class);
				intent.putExtra("screen_name", "Add Tax");
				startActivity(intent);

			}
		});

		ArrayList<String> typeList = new ArrayList<String>();		
		typeList.add("Tax Name");
		typeList.add("Tax%");
//		typeList.add("WefDateFrom");
//		typeList.add("WefDateTo");
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
				if (taxAdapter != null) {
					POSTaxMasterScreen.this.taxAdapter.getFilter().filter(cs);
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

			searchBox.setText("");
			if (taxAdapter != null) {
				taxAdapter.setSelectedItemType(selectedItem);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			taxAdapter.setSelectedItemType(null);
		}
	};

	private void fetchTax() {
		try {
			if (Utils.isDeviceOnline(this)) {
				showLoading();
				Map map = new HashMap<>();
				map.put("Page", "FetchTax");
				map.put("IsActive", "A");
				map.put("userID", getUserId());
				map.put("UserLogin", getUserLogin());
				map.put("Password", getPassWord());
				new RequestManager().sendPostRequest(POSTaxMasterScreen.this,
						KEY_FETCH_TAX_LIST, map);
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
		if (response != null && response.length() > 0) {
			if (requestId == KEY_FETCH_TAX_LIST) {
				ModelManager.getInstance().setTaxModel(response);
				if (ModelManager.getInstance().getTaxModel() != null
						&& ModelManager.getInstance().getTaxModel()
								.getFetchTaxList() != null
						&& ModelManager.getInstance().getTaxModel()
								.getFetchTaxList().size() > 0) {
					taxAdapter = new POSTaxMasterAdapter(
							POSTaxMasterScreen.this, ModelManager.getInstance()
									.getTaxModel().getFetchTaxList(),
							getUserId(), getUserLogin(), getPassWord());
					taxListView.setAdapter(taxAdapter);
					taxListView.setEmptyView(findViewById(R.id.tax_emptyElement));
				} else {
					if (ModelManager.getInstance().getTaxModel() != null
							&& ModelManager.getInstance().getTaxModel()
									.getMessage() != null)
//						showMessage(ModelManager.getInstance().getTaxModel()
//								.getMessage());
					taxAdapter = new POSTaxMasterAdapter(
							POSTaxMasterScreen.this,
							new ArrayList<TaxModelData>(), getUserId(),
							getUserLogin(), getPassWord());
					taxListView.setAdapter(taxAdapter);
					taxListView.setEmptyView(findViewById(R.id.tax_emptyElement));
				}
			} else if (requestId == KEY_DELETE_TAX) {
				ModelManager.getInstance().setTaxModel(response);
				if (ModelManager.getInstance().getTaxModel() != null
						&& ModelManager.getInstance().getTaxModel()
								.getMessage() != null
						&& ModelManager.getInstance().getTaxModel()
								.getMessage().length() > 0) {
					showMessage(ModelManager.getInstance().getTaxModel()
							.getMessage());
					/*if (taxAdapter != null) {
						taxAdapter.setModelData(ModelManager.getInstance()
								.getTaxModel().getFetchTaxList());
						taxAdapter.notifyDataSetChanged();
					}*/
					 taxAdapter.notifyDataSetChanged();
				} else {
//					showMessage("Please try again.");
				}

			}
		} else {
//			showMessage("Please try again.");
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		hideLoading();
		// TODO Auto-generated method stub
		hideLoading();
//		showMessage("Please try again.");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
	}

	@Override
	public void deleteTax(Map map) {
		// TODO Auto-generated method stub
		try {
			if (Utils.isDeviceOnline(this)) {
				showLoading();
				new RequestManager().sendPostRequest(this, KEY_DELETE_TAX, map);
			} else {
				showMessage("Internet connection not found");
			}
		} catch (Exception e) {
			// TODO: handle exception
			showMessage("Please try again.");
		}
	}

	@Override
	public void totalRows(String text) {
		// TODO Auto-generated method stub
		totalRecords.setText("Records : "+text);
	}


	@Override
	public void executeService() {
		fetchTax();
	}
}
