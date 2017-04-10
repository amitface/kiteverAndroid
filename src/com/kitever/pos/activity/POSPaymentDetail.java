package com.kitever.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.customviews.FloatingActionButton;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSPaymentDetailAdapter;
import com.kitever.pos.model.data.PosPayment;

public class POSPaymentDetail extends PosBaseActivity implements NetworkManager {

	private POSPaymentDetailAdapter paymentDetailsAdapter;
	private ListView paymentDetailViewList;
	private final int KEY_FETCH_CREDIT_BALANCE = 1;
	private TextView totalCredit, totalRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setScreenName("Payment Details");
		setBottomAction(true, true, true,true, true, true,true,true,true,true, true,true);
		setLayoutContentView(R.layout.pos_payment_detail_layout);
		setScreen();
		fetchPaymentDetails();
	}

	private void setScreen() {
		Spinner selectType = (Spinner) findViewById(R.id.select_type_spinner);
		EditText searchBox = (EditText) findViewById(R.id.edit_search);
		ImageView adavanceSearch = (ImageView) findViewById(R.id.advance_search);
		paymentDetailViewList = (ListView) findViewById(R.id.payment_view_list);
		totalCredit = (TextView) findViewById(R.id.total_payment);
		
		FloatingActionButton fabButton = null;
		fabButton = new FloatingActionButton.Builder(POSPaymentDetail.this)
				.withDrawable(
						getResources()
								.getDrawable(R.drawable.ic_add_white_36dp))
				.withButtonColor(Color.parseColor("#E46C22"))
				.withGravity(Gravity.BOTTOM | Gravity.RIGHT)
				.withMargins(0, 0, 16, 16).create();

		fabButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		
		ArrayList<String> typeList = new ArrayList<String>();
		
		typeList.add("Date");
		typeList.add("Invoice");
		typeList.add("Customer no.");
		typeList.add("Amount");
		typeList.add("Paid");

		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, typeList);
		selectType.setAdapter(typeAdapter);
		searchBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				if (paymentDetailsAdapter != null) {
					POSPaymentDetail.this.paymentDetailsAdapter.getFilter()
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			break;
		}

		return super.onOptionsItemSelected(item);
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

	@Override
	public void onReceiveResponse(int requestId, String response) {
		// TODO Auto-generated method stub
		hideLoading();
		if (response != null && response.length() > 0) {
			if (requestId == KEY_FETCH_CREDIT_BALANCE) {
				Gson gson  = new Gson();
				
				PosPayment posPaymentData = gson.fromJson(response,PosPayment.class);
				paymentDetailsAdapter = new POSPaymentDetailAdapter(this,
						posPaymentData.getCreditBalnce());
				paymentDetailViewList.setAdapter(paymentDetailsAdapter);
			} else {
				showMessage("Please try again.");
			}
		
	} else {
		showMessage("Please try again.");
	}

}
						

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		hideLoading();
	}
}
