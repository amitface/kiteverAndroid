package com.kitever.pos.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSItemChooseAdapter;
import com.kitever.pos.adapter.POSItemChooseAdapter.Actionable;
import com.kitever.pos.model.data.FetchSelectedProductModelData;
import com.kitever.pos.model.manager.ModelManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class POSItemChooseScreen extends Activity implements NetworkManager,
		Actionable {

	private final int KEY_FETCH_PRODUCT_LIST = 1;
	private ProgressDialog progressDialog = null;
	private POSItemChooseAdapter itemAdapter;
	private ListView itemListView;
	Spinner selectType;
	EditText searchBox;
	TextView txt_select, go_cart, check_done;
	HashMap<String, FetchSelectedProductModelData> hmap = new HashMap<String, FetchSelectedProductModelData>();
	ArrayList<String> productList = new ArrayList<String>();
	MoonIcon mIcon;
	String ContactName = "";
	String type = "";
	LinearLayout select_cart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pos_item_choose_layout);
		setScreen();
		fetchProductList();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(this, POSAddOrderScreen.class));
		NavUtils.navigateUpFromSameTask(this);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			startActivity(new Intent(this, POSAddOrderScreen.class));
			NavUtils.navigateUpFromSameTask(this);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (itemAdapter != null) {
			itemAdapter.setModelData(ModelManager.getInstance()
					.getFetchProductModel().getFetchProductList());
			itemAdapter.notifyDataSetChanged();
		}
	}

	private void setScreen() {

		mIcon = new MoonIcon(this);
		itemListView = (ListView) findViewById(R.id.item_list_view);
		go_cart = (TextView) findViewById(R.id.go_cart);
		txt_select = (TextView) findViewById(R.id.txt_select);
		check_done = (TextView) findViewById(R.id.check_done);
		selectType = (Spinner) findViewById(R.id.select_type_spinner);
		searchBox = (EditText) findViewById(R.id.edit_search);

		setRobotoThinFont(txt_select,this);
		setRobotoThinFont(searchBox,this);

		itemListView.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));

		searchBox.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		go_cart.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
		txt_select.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
		check_done.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));

		mIcon.setTextfont(go_cart);
		mIcon.setTextfont(check_done);

		try {
			Intent intent = getIntent();
			if (intent.getExtras() != null) {
				type = intent.getStringExtra("Type");
				productList = intent.getStringArrayListExtra("productList");
				hmap = (HashMap<String, FetchSelectedProductModelData>) intent
						.getSerializableExtra("hmap");
				ContactName = intent.getStringExtra("ContactName");
				updatePrice(hmap, productList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<String> typeList = new ArrayList<String>();
		typeList.add("Product");
		typeList.add("Category");
		typeList.add("Brand");
		typeList.add("Price");
		CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(this,
				android.R.layout.simple_list_item_1, typeList);
		//typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectType.setAdapter(typeAdapter);

		selectType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Spinner spinSelected = (Spinner) parent;
				String selectedItem = (String) spinSelected.getSelectedItem();
				if (itemAdapter != null) {
					itemAdapter.setSelectedItemType(selectedItem);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				itemAdapter.setSelectedItemType(null);
			}
		});

		searchBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				if (itemAdapter != null) {
					POSItemChooseScreen.this.itemAdapter.getFilter().filter(cs);
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

		go_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(POSItemChooseScreen.this,
						PosCart.class);
				intent.putExtra("productList", productList);
				intent.putExtra("hmap", hmap);
				intent.putExtra("Type", type);
				startActivity(intent);
				finish();
			}
		});


		Button doneBtn = (Button) findViewById(R.id.done_btn);
		LinearLayout select_cart= (LinearLayout) findViewById(R.id.select_cart);

		doneBtn.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
		doneBtn.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
		select_cart.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentItem = new Intent(POSItemChooseScreen.this,
						POSAddOrderScreen.class);
				intentItem.putExtra("productList", productList);
				intentItem.putExtra("hmap", hmap);
				intentItem.putExtra("ContactName", ContactName);
				intentItem.putExtra("Type", type);
				startActivity(intentItem);
				finish();
			}
		});

		check_done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentItem = new Intent(POSItemChooseScreen.this,
						POSAddOrderScreen.class);
				intentItem.putExtra("productList", productList);
				intentItem.putExtra("hmap", hmap);
				intentItem.putExtra("ContactName", ContactName);
				intentItem.putExtra("Type", type);
				startActivity(intentItem);
				finish();
			}
		});
	}

	private void fetchProductList() {
		if (Utils.isDeviceOnline(this)) {
			showLoading();
			String userId = Utils.getUserId(this);
			Map map = new HashMap<>();
			map.put("Page", "FetchProductList");
			map.put("userID", userId);
			map.put("Type", type);
			try {
				new RequestManager().sendPostRequest(POSItemChooseScreen.this,
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
		hideLoading();
		if (response != null && response.length() > 0) {
			if (requestId == KEY_FETCH_PRODUCT_LIST) {
				ModelManager.getInstance().setFetchProductModel(response);
				if (ModelManager.getInstance().getFetchProductModel() != null
						&& ModelManager.getInstance().getFetchProductModel()
								.getFetchProductList() != null
						&& ModelManager.getInstance().getFetchProductModel()
								.getFetchProductList().size() > 0) {

					if (hmap != null && hmap.size() > 0) {
						itemAdapter = new POSItemChooseAdapter(
								POSItemChooseScreen.this, ModelManager
										.getInstance().getFetchProductModel()
										.getFetchProductList(), hmap,
								productList);
					} else {
						itemAdapter = new POSItemChooseAdapter(
								POSItemChooseScreen.this, ModelManager
										.getInstance().getFetchProductModel()
										.getFetchProductList());
					}
					itemListView.setAdapter(itemAdapter);
				}
			}
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		hideLoading();
		showMessage("Please try again.");
	}

	public void showMessage(String message) {
		AlertDialog.Builder alert = new AlertDialog.Builder(
				POSItemChooseScreen.this);
		alert.setMessage(message);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		alert.show();
	}

	public void showLoading() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
		}
		progressDialog.setTitle("Please wait");
		progressDialog.setMessage("Loading...");
		progressDialog.show();
	}

	public void hideLoading() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public void updatePrice(
			HashMap<String, FetchSelectedProductModelData> hmap,
			ArrayList<String> productList) {
		this.hmap = hmap;
		this.productList = productList;
		Double tprice = 0.0;
		int totalq = 0;
	
		if (hmap != null && hmap.size() > 0) {
			for (int i = 0; i < hmap.size(); i++) {
				String key = productList.get(i);
				String rate = hmap.get(key).getPerUnitPrice();
				String qty = hmap.get(key).getQuantity();

				int q = Integer.parseInt(qty);
				Double item_rate = Double.parseDouble(rate);
				tprice = tprice + q * item_rate;
				totalq = totalq + q;
			}
			go_cart.setEnabled(true);
			go_cart.setClickable(true);
			check_done.setEnabled(true);
			String display_str = "Items :" + totalq + " Price : " + Utils.doubleToString(tprice);
			txt_select.setText(display_str);
		} else {
			go_cart.setEnabled(false);
			go_cart.setClickable(false);
			check_done.setEnabled(false);
			txt_select.setText("Select Items");

		}
	}

}
