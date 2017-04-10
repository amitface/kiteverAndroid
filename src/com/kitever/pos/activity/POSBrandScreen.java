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
import com.kitever.pos.adapter.POSBrandAdapter;
import com.kitever.pos.fragment.adapters.POSBrandAdapterFragment;
import com.kitever.pos.model.data.BrandModelData;
import com.kitever.pos.model.manager.ModelManager;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

public class POSBrandScreen extends PosBaseActivity implements NetworkManager,
		OnClickListener, POSBrandAdapterFragment.Actionable, TotalRows {
	private ListView brandListView;
	private final int KEY_FETCH_BRAND_LIST = 1;
	private final int KEY_DELETE_BRAND = 2;
	private POSBrandAdapter brandAdapter;
	private TextView noRecordImage, totalRecords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setScreenName("Brands");		
		setBottomAction(true, true, true,true, true, true,true,true,true,false, true,true);
		setLayoutContentView(R.layout.pos_brand_layout);
		setScreen();
		fetchBrand();
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
		EditText searchBox = (EditText) findViewById(R.id.edit_search);
		ImageView adavanceSearch = (ImageView) findViewById(R.id.advance_search);
		brandListView = (ListView) findViewById(R.id.brand_list_view);
		
		MoonIcon mIcon=new MoonIcon(this);
		noRecordImage = (TextView) findViewById(R.id.no_record);
		mIcon.setTextfont(noRecordImage);
		
		
		totalRecords = (TextView) findViewById(R.id.brand_total_rows);
		FloatingActionButton fabButton = null;
		fabButton = new FloatingActionButton.Builder(POSBrandScreen.this)
				.withDrawable(
						getResources()
								.getDrawable(R.drawable.ic_add_white_36dp))
				.withButtonColor(Color.parseColor("#E46C22"))
				.withGravity(Gravity.BOTTOM | Gravity.RIGHT)
				.withMargins(0, 0, 16,com.kitever.utils.Utils.FLOATING_BUTTON_MARGIN).create();

		fabButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(POSBrandScreen.this,
						PosAddUpdateBrand.class);
				intent.putExtra("screen_name", "Add Brand");
				startAcitivityWithEffect(intent);
				finish();
			}
		});

		ArrayList<String> typeList = new ArrayList<String>();
		
		typeList.add("Brand");

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
				if (brandAdapter != null) {
					POSBrandScreen.this.brandAdapter.getFilter().filter(cs);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (brandAdapter != null
				&& ModelManager.getInstance().getBrandModel() != null) {
			if ((ModelManager.getInstance().getBrandModel().getStatus() != null && ModelManager
					.getInstance().getBrandModel().getStatus()
					.equalsIgnoreCase("true"))) {
				brandAdapter.setModelData(ModelManager.getInstance()
						.getBrandModel().getBrandList());
				brandAdapter.notifyDataSetChanged();
			}
		}
	}

	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			Spinner spinSelected = (Spinner) parent;
			String selectedItem = (String) spinSelected.getSelectedItem();
			if (brandAdapter != null) {
				brandAdapter.setSelectedItemType(selectedItem);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			brandAdapter.setSelectedItemType(null);
		}
	};

	private void fetchBrand() {
		if (Utils.isDeviceOnline(this)) {
			showLoading();
			Map map = new HashMap<>();
			map.put("Page", "FetchBrand");
			map.put("IsActive", "A");
			map.put("userID", getUserId());
			map.put("UserLogin", getUserLogin());
			map.put("Password", getPassWord());
			try {
				new RequestManager().sendPostRequest(this,
						KEY_FETCH_BRAND_LIST, map);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			showMessage("Internet connection not found");
		}

	}

	@Override
	public void onReceiveResponse(int requestId, String response) {
		// TODO Auto-generated method stub
		hideLoading();
		if (response != null && response.length() > 0) {
			if (requestId == KEY_FETCH_BRAND_LIST) {
				ModelManager.getInstance().setBrandModel(response);
				System.out.println("response brand"+response);
				if (ModelManager.getInstance().getBrandModel().getBrandList() != null
						&& ModelManager.getInstance().getBrandModel()
								.getBrandList().size() > 0) {
					brandAdapter = new POSBrandAdapter(this, ModelManager
							.getInstance().getBrandModel().getBrandList(),
							getUserId(), getUserLogin(), getPassWord());
					brandListView.setAdapter(brandAdapter);
					brandListView.setEmptyView(findViewById(R.id.brand_emptyElement));
				} else {
					if (ModelManager.getInstance().getBrandModel().getMessage() != null)
//						showMessage(ModelManager.getInstance().getBrandModel()
//								.getMessage());
					brandAdapter = new POSBrandAdapter(this,
							new ArrayList<BrandModelData>(), getUserId(),
							getUserLogin(), getPassWord());
					brandListView.setAdapter(brandAdapter);
					brandListView.setEmptyView(findViewById(R.id.brand_emptyElement));
				}
			} else if (requestId == KEY_DELETE_BRAND) {
				ModelManager.getInstance().setBrandModel(response);
				System.out.println("response delete brand"+response);
				if (brandAdapter != null
						&& ModelManager.getInstance().getBrandModel() != null) {
					if (ModelManager.getInstance().getBrandModel().getMessage() != null
							&& ModelManager.getInstance().getBrandModel()
									.getMessage().length() > 0) {
//						showMessage(ModelManager.getInstance().getBrandModel()
//								.getMessage());
					}
					brandAdapter.setModelData(ModelManager.getInstance()
							.getBrandModel().getBrandList());
					brandAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		hideLoading();
//		showMessage("Please try again.");
	}

	@Override
	public void deleteBrand(Map map) {
		// TODO Auto-generated method stub
		if (Utils.isDeviceOnline(this)) {
			try {
				new RequestManager().sendPostRequest(this, KEY_DELETE_BRAND,
						map);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			showMessage("Internet connection not found");
		}

	}

	@Override
	public void totalRows(String text) {
		// TODO Auto-generated method stub
		totalRecords.setText("Records : "+text);
	}

}
