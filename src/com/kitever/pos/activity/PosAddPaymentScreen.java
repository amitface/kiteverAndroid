package com.kitever.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import sms19.inapp.msg.constant.Utils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.model.manager.ModelManager;

public class PosAddPaymentScreen extends PosBaseActivity implements
		NetworkManager, OnClickListener {

	private final int KEY_FETCH_MODE_PAYMENT = 1;
	private final int KEY_INSERT_PAYMENT_DETAILS = 2;
	private Spinner modePaymentSpinner;
	private PosModeOfPaymentDetailScreen modeOfPaymentDetailScreen;
	private String modeOfPaymentVal;
	private int modeNo = 0;
	private String orderIdVal;
	private EditText amount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setScreenName("Add Payment");
		setBottomAction(true, true, true,true, true, true,true,true,true,true, true,true);
		setLayoutContentView(R.layout.pos_add_payment_layout);
		setScreen();
		fetchModeOfPayment();
	}

	private void setScreen() {
		Intent intent = getIntent();
		orderIdVal = intent.getStringExtra("order_id");
		String paidAmtVal = intent.getStringExtra("paid_amount");
		String balAmtVal = intent.getStringExtra("balance_amount");
		EditText orderId = (EditText) findViewById(R.id.order_id);
		EditText balance = (EditText) findViewById(R.id.balance_id);
		amount = (EditText) findViewById(R.id.amount_id);
		EditText currentBal = (EditText) findViewById(R.id.current_bal_id);
		modePaymentSpinner = (Spinner) findViewById(R.id.mode_payment);
		Button addPayBtn = (Button) findViewById(R.id.add_pay_btn);
		orderId.setText(orderIdVal);
		balance.setText(balAmtVal);
		amount.setText(paidAmtVal);
		addPayBtn.setOnClickListener(this);
	}

	private void insertPaymentDetail() {
		if (Utils.isDeviceOnline(this)) {
			try {
				showLoading();
				String modeOfPayId = "";
				Map map = new HashMap<>();
				map.put("Page", "InsertpaymentDetail");
				if (ModelManager.getInstance().getFetchModeOfPaymentModel() != null
						&& ModelManager.getInstance()
								.getFetchModeOfPaymentModel()
								.getFetchModeOfPaymentList() != null
						&& ModelManager.getInstance()
								.getFetchModeOfPaymentModel()
								.getFetchModeOfPaymentList().size() > 0) {
					try {
						modeOfPayId = ModelManager.getInstance()
								.getFetchModeOfPaymentModel()
								.getFetchModeOfPaymentList().get(modeNo - 1)
								.getId();
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
				map.put("PayMode", modeOfPayId);
				if (modeOfPaymentDetailScreen != null
						&& modeOfPaymentDetailScreen.getChequeNo() != null) {
					map.put("PayModeNo",
							modeOfPaymentDetailScreen.getChequeNo());
				} else {
					map.put("PayModeNo", "");
				}
				if (modeOfPaymentDetailScreen != null
						&& modeOfPaymentDetailScreen.getDate() != null
						&& !modeOfPaymentDetailScreen.getDate()
								.equalsIgnoreCase("date")) {
					map.put("ChequeDate", modeOfPaymentDetailScreen.getDate());
				} else {
					map.put("ChequeDate", "");
				}
				if (modeOfPaymentDetailScreen != null
						&& modeOfPaymentDetailScreen.getBankNameOrRefNum() != null) {
					map.put("BankName",
							modeOfPaymentDetailScreen.getBankNameOrRefNum());
				} else {
					map.put("BankName", "");
				}
				if (modeOfPaymentDetailScreen != null
						&& modeOfPaymentDetailScreen.getRemarkOrDes() != null) {
					map.put("Remark",
							modeOfPaymentDetailScreen.getRemarkOrDes());
				} else {
					map.put("Remark", "");
				}
				map.put("PaidAmount", amount.getText().toString());
				map.put("OrderID", orderIdVal);
			 map.put("userID", getUserId());
			 map.put("UserLogin", getUserLogin());
			map.put("Password", getPassWord());
				new RequestManager().sendPostRequest(PosAddPaymentScreen.this,
						KEY_INSERT_PAYMENT_DETAILS, map);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			showMessage("Internet connection not found");
		}
	}

	private void fetchModeOfPayment() {
		try {
			Map map = new HashMap<>();
			map.put("Page", "FetchModeOFPayMent");
			new RequestManager().sendPostRequest(PosAddPaymentScreen.this,
					KEY_FETCH_MODE_PAYMENT, map);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.add_pay_btn:
			insertPaymentDetail();
			break;

		default:
			break;
		}
	}

	@Override
	public void onReceiveResponse(int requestId, String response) {
		// TODO Auto-generated method stub
		hideLoading();
		if (response != null && response.length() > 0) {
			if (requestId == KEY_FETCH_MODE_PAYMENT) {
				ModelManager.getInstance().setFetchModeOfPaymentModel(response);
				ArrayList<String> list = new ArrayList<>();
				list.add("Select mode of payment                                          ");
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
				ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
						android.R.layout.simple_spinner_item, list);
				modePaymentSpinner.setAdapter(adapter);
				modePaymentSpinner
						.setOnItemSelectedListener(itemSelectedListener);
			} else if (requestId == KEY_INSERT_PAYMENT_DETAILS) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					String message = "Please try again";
					if (jsonObject != null) {
						if (jsonObject.has("Message")) {
							message = jsonObject.getString("Message");
						}
					}
					showMessage(message);
				} catch (Exception e) {
					// TODO: handle exception
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

	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			Spinner spinModePayment = (Spinner) parent;
			if (spinModePayment.getId() == R.id.mode_payment) {
				modeOfPaymentVal = (String) spinModePayment.getSelectedItem();
				modeNo = position;
				if (position > 0 && !modeOfPaymentVal.equalsIgnoreCase("Cash")) {
					modeOfPaymentDetailScreen = new PosModeOfPaymentDetailScreen(
							PosAddPaymentScreen.this);
					modeOfPaymentDetailScreen.setPaymentType(modeOfPaymentVal);
					modeOfPaymentDetailScreen.show();
				}
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};
}
