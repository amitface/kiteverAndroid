package com.kitever.pos.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.kitever.pos.adapter.POSCategoryAdapter;
import com.kitever.pos.model.manager.ModelManager;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

public class POSCategoryScreen extends PosBaseActivity implements
		NetworkManager, POSCategoryAdapter.Actionable, TotalRows {

	private int KEY_FETCH_CATEGORY_LIST = 1;
	private ListView categoryListView;
	private POSCategoryAdapter categoryAdapter;
	private final int KEY_DELETE_CATEGEORY = 2;
	private final int KEY_ACTIVATE_CATEGORY = 3;
	private TextView no_list_txt, totalRecords, noRecord, active_legend;
	EditText searchBox;
	Spinner selectType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setScreenName("Categories");
		setBottomAction(true, false, true, true, true, true, true, true, true,
				true, true, true);
		setLayoutContentView(R.layout.pos_category_layout);
		setScreen();
		fetchCategoryList();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(this, POSHomeScreen.class));
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

	private void fetchCategoryList() {
		if (Utils.isDeviceOnline(this)) {
			showLoading();
			Map map = new HashMap<>();
			map.put("Page", "FetchCategory");
			map.put("IsActive", "A");
			map.put("userID", getUserId());
			map.put("UserLogin", getUserLogin());
			map.put("Password", getPassWord());
			Log.i("fetchCategoryList","---"+map.toString());
			try {
				new RequestManager().sendPostRequest(POSCategoryScreen.this,
						KEY_FETCH_CATEGORY_LIST, map);
			} catch (Exception e) {
				e.printStackTrace();
				hideLoading();
				showMessage("Please try again.");
			}
		} else {
			showMessage("Internet connection not found");
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (categoryAdapter != null) {
			categoryAdapter.setModelData(ModelManager.getInstance()
					.getCategoryModel().getCategoryModelDataList());
			categoryAdapter.notifyDataSetChanged();
			searchBox.setText("");
			selectType.setSelection(0);
		}
	}

	private void setScreen() {
		selectType = (Spinner) findViewById(R.id.select_type_spinner);
		searchBox = (EditText) findViewById(R.id.edit_search);
		ImageView adavanceSearch = (ImageView) findViewById(R.id.advance_search);
		categoryListView = (ListView) findViewById(R.id.category_list_view);
		no_list_txt = (TextView) findViewById(R.id.no_list_txt);
		totalRecords = (TextView) findViewById(R.id.category_total_rows);
		//active_legend = (TextView) findViewById(R.id.active_legend);
		noRecord = (TextView) findViewById(R.id.no_record);
		MoonIcon mIcon = new MoonIcon(this);
		mIcon.setTextfont(noRecord);
		//mIcon.setTextfont(active_legend);

	//	active_legend.append(" For Active/Inactive");

		no_list_txt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(POSCategoryScreen.this,
						POSCategoryAddOrUpdateScreen.class);
				intent.putExtra("screen_name", "Add Category");
				startActivity(intent);
			}
		});
		FloatingActionButton fabButton = null;
		fabButton = new FloatingActionButton.Builder(POSCategoryScreen.this)
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
				Intent intent = new Intent(POSCategoryScreen.this,
						POSCategoryAddOrUpdateScreen.class);
				intent.putExtra("screen_name", "Add Category");
				startActivity(intent);
				finish();
			}
		});

		ArrayList<String> typeList = new ArrayList<String>();
		// typeList.add("Select");
		typeList.add("Category");
		typeList.add("Parent");
		typeList.add("Type");
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, typeList);
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectType.setAdapter(typeAdapter);
		//selectType.setSelection(1);
		selectType.setOnItemSelectedListener(itemSelectedListener);

		// if (categoryAdapter != null) {
		searchBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				if (categoryAdapter != null) {
					POSCategoryScreen.this.categoryAdapter.getFilter().filter(
							cs);
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
		// }
	}

	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			Spinner spinSelected = (Spinner) parent;
			String selectedItem = (String) spinSelected.getSelectedItem();
			if (categoryAdapter != null) {
				categoryAdapter.setSelectedItemType(selectedItem);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			categoryAdapter.setSelectedItemType(null);
		}
	};

	@Override
	public void onReceiveResponse(int requestId, String response) {
		// TODO Auto-generated method stub
		hideLoading();
		if (response != null && response.length() > 0) {
			if (requestId == KEY_FETCH_CATEGORY_LIST) {
				ModelManager.getInstance().setCategoryModel(response);
				if (ModelManager.getInstance().getCategoryModel() != null
						&& (ModelManager.getInstance().getCategoryModel()
								.getCategoryModelDataList() != null && ModelManager
								.getInstance().getCategoryModel()
								.getCategoryModelDataList().size() > 0)) {
					categoryAdapter = new POSCategoryAdapter(this, ModelManager
							.getInstance().getCategoryModel()
							.getCategoryModelDataList(), getUserId(),
							getPassWord(), getUserLogin());
					categoryListView.setAdapter(categoryAdapter);
					categoryListView
							.setEmptyView(findViewById(R.id.category_emptyElement));

				} else {
					categoryListView
							.setEmptyView(findViewById(R.id.category_emptyElement));

				}
			} else if (requestId == KEY_DELETE_CATEGEORY) {
				Log.i("response", "response-" + response);
				ModelManager.getInstance().setCategoryModel(response);
				if (ModelManager.getInstance().getCategoryModel() != null
						&& ModelManager.getInstance().getCategoryModel()
								.getMessage() != null
						&& ModelManager.getInstance().getCategoryModel()
								.getMessage().length() > 0) {
					showMessage(ModelManager.getInstance().getCategoryModel()
							.getMessage());
					if (categoryAdapter != null) {
						categoryAdapter.setModelData(ModelManager.getInstance()
								.getCategoryModel().getCategoryModelDataList());
						categoryAdapter.notifyDataSetChanged();
					}
				} else {
					showMessage("Please try again.");
				}

			} else if (requestId == KEY_ACTIVATE_CATEGORY) {
				Log.i("response cat", "response= " + response);
				categoryAdapter.notifyDataSetChanged();
				// fetchCategoryList();
			}
		}

	}


	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		hideLoading();
		showMessage("Please try again.");
	}

	@Override
	public void deleteCategory(Map map) {
		// TODO Auto-generated method stub
		if (Utils.isDeviceOnline(this)) {
			try {
				showLoading();

				new RequestManager().sendPostRequest(this,
						KEY_DELETE_CATEGEORY, map);
			} catch (Exception e) {
				// TODO: handle exception
				hideLoading();
				showMessage("Please try again.");
			}
		} else {
			hideLoading();
			showMessage("Internet connection not found");
		}

	}

	@Override
	public void totalRows(String text) {
		// TODO Auto-generated method stub
		totalRecords.setText("Records : " + text);
	}

	@Override
	public void activateCategory(Map map) {

		if (Utils.isDeviceOnline(this)) {
			try {
				new RequestManager().sendPostRequest(POSCategoryScreen.this,
						KEY_ACTIVATE_CATEGORY, map);
			} catch (Exception e) {
				Log.i("category error", "Error -" + e.getMessage());
				hideLoading();
				showMessage("Please try again.");
			}
		} else {
			showMessage("Internet connection not found");
		}

	}
}
