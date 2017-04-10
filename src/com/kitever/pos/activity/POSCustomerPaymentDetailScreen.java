package com.kitever.pos.activity;

import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSCustomerPaymentDetailAdapter;
import com.kitever.pos.model.manager.ModelManager;

public class POSCustomerPaymentDetailScreen extends PosBaseActivity implements
		NetworkManager {

	private final int KEY_FETCHCUSTOMER_ORDER_DETAIL = 1;
	private POSCustomerPaymentDetailAdapter customerPaymentDetailAdapter;
	private ListView orderPaymentListview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String contactId = intent.getStringExtra("contact_id");
		setScreenName("Payment Details");
		setBottomAction(true, true, true,true, true, true,true,true,true,true, true,true);
		setLayoutContentView(R.layout.pos_customer_payment_detail_layout);
		setScreen();
		fetchCustomerOrderDetails(contactId);
	}

	private void setScreen() {

		orderPaymentListview = (ListView) findViewById(R.id.pay_order_list_view);
	}

	private void fetchCustomerOrderDetails(String contactId) {
		if (Utils.isDeviceOnline(this)) {
			showLoading();
			Map map = new HashMap<>();
			
			map.put("Page", "FetchCustomerOrderDetail");
			map.put("ContactID", contactId);
			map.put("userID", getUserId());
			map.put("UserLogin", getUserLogin());
			map.put("Password", getPassWord());
			
			try {
				new RequestManager().sendPostRequest(this,
						KEY_FETCHCUSTOMER_ORDER_DETAIL, map);
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
			if (requestId == KEY_FETCHCUSTOMER_ORDER_DETAIL) {
				ModelManager.getInstance().setFetchCustomerOrderDetailModel(
						response);
				if (ModelManager.getInstance()
						.getFetchCustomerOrderDetailModel() != null
						&& ModelManager.getInstance()
								.getFetchCustomerOrderDetailModel()
								.getFetchCustomerOrderDetailList() != null
						&& ModelManager.getInstance()
								.getFetchCustomerOrderDetailModel()
								.getFetchCustomerOrderDetailList().size() > 0) {
//					customerPaymentDetailAdapter = new POSCustomerPaymentDetailAdapter(this,
//							ModelManager.getInstance()
//									.getFetchCustomerOrderDetailModel()
//									.getFetchCustomerOrderDetailList());
//					orderPaymentListview
//							.setAdapter(customerPaymentDetailAdapter);

				} else {
					if (ModelManager.getInstance()
							.getFetchCustomerOrderDetailModel().getMessage() != null) {
						showMessage(ModelManager.getInstance()
								.getFetchCustomerOrderDetailModel()
								.getMessage());
					}
				}

			}
		} else {
			showMessage("Please try again.");
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		hideLoading();
		showMessage("Please try again.");
	}

	
}
