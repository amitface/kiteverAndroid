package com.kitever.pos.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;
import com.kitever.customviews.FloatingActionButton;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSProductAdapter;
import com.kitever.pos.adapter.POSProductAdapter.Actionable;
import com.kitever.pos.model.manager.ModelManager;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

public class POSProductsScreen extends PosBaseActivity implements
		NetworkManager, Actionable, TotalRows {

	private final int KEY_FETCH_PRODUCT_LIST = 1;
	private final int KEY_DELETE_PRODUCT = 2;
	private final int KEY_ACTIVATE_PRODUCT = 3;
	private ListView productListView;
	private TextView no_record, no_record_txt, totalRecords,price_header;
	private POSProductAdapter productAdapter;
	private EditText searchBox;
	Spinner selectType;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setScreenName("Products");
		setBottomAction(true, true, false, true, true, true, true, true, true,
				true, true, true);
		setLayoutContentView(R.layout.pos_products_layout);
		setScreen();
		fetchProductList();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startAcitivityWithEffect(new Intent(this, POSHomeScreen.class));
		NavUtils.navigateUpFromSameTask(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (productAdapter != null) {
			productAdapter.setModelData(ModelManager.getInstance()
					.getProductModel().getProductList());
			productAdapter.notifyDataSetChanged();
		}
		searchBox.setText("");
		selectType.setSelection(0);
	}

	private void setScreen() {

		MoonIcon mIcon = new MoonIcon(this);

		// ImageView adavanceSearch = (ImageView)
		// findViewById(R.id.advance_search);
		productListView = (ListView) findViewById(R.id.product_list_view);
		no_record = (TextView) findViewById(R.id.no_record);
		no_record_txt = (TextView) findViewById(R.id.no_record_txt);

		//price_header= (TextView) findViewById(R.id.price_header);


		totalRecords = (TextView) findViewById(R.id.product_total_rows);
		mIcon.setTextfont(no_record);
		//mIcon.setTextfont(price_header);

		FloatingActionButton fabButton = null;
		fabButton = new FloatingActionButton.Builder(POSProductsScreen.this)
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
				Intent intent = new Intent(POSProductsScreen.this,
						POSAddUpdateProduct.class);
				intent.putExtra("screen_name", "Add Product");
				startAcitivityWithEffect(intent);
				finish();
			}
		});

		selectType = (Spinner) findViewById(R.id.select_type_spinner);
		searchBox = (EditText) findViewById(R.id.edit_search);
		ArrayList<String> typeList = new ArrayList<String>();
		// typeList.add("Default");
		typeList.add("Product");
		typeList.add("Brand");
		typeList.add("Price >=");
		typeList.add("Price <=");
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
				if (productAdapter != null) {
					POSProductsScreen.this.productAdapter.getFilter()
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

	}

	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			Spinner spinSelected = (Spinner) parent;
			String selectedItem = (String) spinSelected.getSelectedItem();
			if(position==2 || position==3 ) searchBox.setInputType(InputType.TYPE_CLASS_NUMBER);
			else  searchBox.setInputType(InputType.TYPE_CLASS_TEXT);
			searchBox.setText("");
			if (productAdapter != null) {
				productAdapter.setSelectedItemType(selectedItem);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			productAdapter.setSelectedItemType(null);
		}
	};

	private void fetchProductList() {
		if (Utils.isDeviceOnline(this)) {
			showLoading();
			Map map = new HashMap<>();
			map.put("Page", "FetchProduct");
			map.put("IsActive", "A");
			map.put("userID", getUserId());
			map.put("UserLogin", getUserLogin());
			map.put("Password", getPassWord());
			try {
				new RequestManager().sendPostRequest(POSProductsScreen.this,
						KEY_FETCH_PRODUCT_LIST, map);
			} catch (Exception e) {
				// TODO: handle exception
				hideLoading();
				showMessage("Please try again.");
			}
		} else {
			showMessage("Internet connection not found");
		}

	}

	@Override
	public void onReceiveResponse(int requestId, String response) {
		// TODO Auto-generated method stub
		if (response != null && response.length() > 0) {
			if (requestId == KEY_FETCH_PRODUCT_LIST) {
				Log.i("Product List", "Resoponse - " + response);
				ModelManager.getInstance().setProductModel(response);
				if (ModelManager.getInstance().getProductModel() != null) {
					if (ModelManager.getInstance().getProductModel()
							.getProductList() != null
							&& ModelManager.getInstance().getProductModel()
									.getProductList().size() > 0) {
						productAdapter = new POSProductAdapter(
								POSProductsScreen.this, ModelManager
										.getInstance().getProductModel()
										.getProductList(), getUserId(),
								getPassWord(), getUserLogin());
						productListView.setAdapter(productAdapter);
						productListView
								.setEmptyView(findViewById(R.id.product_emptyElement));
					} else {
						productListView
								.setEmptyView(findViewById(R.id.product_emptyElement));
					}
				}
			} else if (requestId == KEY_DELETE_PRODUCT) {
				ModelManager.getInstance().setProductModel(response);

				if (ModelManager.getInstance().getProductModel() != null
						&& ModelManager.getInstance().getProductModel()
								.getMessage() != null
						&& ModelManager.getInstance().getProductModel()
								.getMessage().length() > 0) {
					showMessage(ModelManager.getInstance().getProductModel()
							.getMessage());
					if (productAdapter != null) {
						productAdapter.setModelData(ModelManager.getInstance()
								.getProductModel().getProductList());
						productAdapter.notifyDataSetChanged();
					}
				} else {
					showMessage("Please try again.");
				}
			}

			else if (requestId == KEY_ACTIVATE_PRODUCT) {
				Log.i("Active", "response- " + response);
				productAdapter.notifyDataSetChanged();

			}

		} else {
			showMessage("Please try again.");
		}
		hideLoading();
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		hideLoading();
		showMessage("Please try again.");
	}

	@Override
	public void deleteProduct(Map map) {
		// TODO Auto-generated method stub
		if (Utils.isDeviceOnline(this)) {
			try {
				new RequestManager().sendPostRequest(POSProductsScreen.this,
						KEY_DELETE_PRODUCT, map);
			} catch (Exception e) {
				// TODO: handle exception
				hideLoading();
				showMessage("Please try again.");
			}
		} else {
			showMessage("Internet connection not found");
		}
	}

	public void CheckNoResult(int size) {
		if (size > 0) {
			no_record.setVisibility(View.GONE);
			productListView.setVisibility(View.VISIBLE);
		} else {
			no_record.setVisibility(View.VISIBLE);
			productListView.setVisibility(View.GONE);
		}
	}

	@Override
	public void activateProduct(Map map,String str) {
		// TODO Auto-generated method stub
		if (Utils.isDeviceOnline(this)) {
			try {
				Log.i("Request ", "--product--" + map.toString());
				new RequestManager().sendPostRequest(POSProductsScreen.this,
						KEY_ACTIVATE_PRODUCT, map);
			} catch (Exception e) {
				Log.i("error", "-Error-" + e.getMessage());
				showMessage("Please try again.");
			}
		} else {
			showMessage("Internet connection not found");

		}

	}

	@Override
	public void totalRows(String text) {
		// TODO Auto-generated method stub
		totalRecords.setText("Records : " + text);
	}
}
