package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kitever.pos.model.data.OrderDetailModelData;

public class OrderDetailModel {

	private ArrayList<OrderDetailModelData> orderDetailList;
	private String message, status;

	public OrderDetailModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		if (response != null && response.length() > 0) {
			try {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject != null) {
					if (jsonObject.has("Message")) {
						message = jsonObject.getString("Message");
					}
					if (jsonObject.has("Status")) {
						status = jsonObject.getString("Status");
					}
					if (jsonObject.has("Order")) {
						JSONArray jsonArray = jsonObject.getJSONArray("Order");
						if (jsonArray != null && jsonArray.length() > 0) {
							orderDetailList = new ArrayList<OrderDetailModelData>();
							String orderno = null,orderMasterID = null, orderCode = null, orderDate = null, userName = null, billAmount = null, taxAmount = null, discountAmount = null, paidAmount = null, balanceAmount = null, payMode = null, payModeNo = null, chequeDate = null, bankName = null, contactName = null,Mobile = null,Email = null;
							for (int k = 0; k < jsonArray.length(); k++) {
								JSONObject object = jsonArray.getJSONObject(k);
								if (object != null) {
									if (object.has("orderno"))
									{
										orderno = object.getString("orderno");
									}
									if (object.has("OrderMasterID")) {
										orderMasterID = object
												.getString("OrderMasterID");
									}
									if (object.has("OrderCode")) {
										orderCode = object
												.getString("OrderCode");
									}
									if (object.has("OrderDate")) {
										orderDate = object
												.getString("OrderDate");
									}
									if (object.has("UserName")) {
										userName = object.getString("UserName");
									}
									if (object.has("BillAmount")) {
										billAmount = object
												.getString("BillAmount");
									}
									if (object.has("TaxAmount")) {
										taxAmount = object
												.getString("TaxAmount");
									}
									if (object.has("DiscountAmount")) {
										discountAmount = object
												.getString("DiscountAmount");
									}
									if (object.has("PaidAmount")) {
										paidAmount = object
												.getString("PaidAmount");
									}
									if (object.has("BalanceAmount")) {
										balanceAmount = object
												.getString("BalanceAmount");
									}
									if (object.has("PayMode")) {
										payMode = object.getString("PayMode");
									}
									if (object.has("PayModeNo")) {
										payModeNo = object
												.getString("PayModeNo");
									}
									if (object.has("ChequeDate")) {
										chequeDate = object
												.getString("ChequeDate");
									}
									if (object.has("BankName")) {
										bankName = object.getString("BankName");
									}
									if (object.has("contactName")) {
										contactName = object
												.getString("contactName");
									}
									if(object.has("Mobile")){
										Mobile = object.getString("Moblie");
									}
									if(object.has("Email")){
										Email = object.getString("Email");
									}
									
//									orderDetailList
//											.add(new OrderDetailModelData(orderno,
//													orderMasterID, orderCode,
//													orderDate, userName,
//													billAmount, taxAmount,
//													discountAmount, paidAmount,
//													balanceAmount, payMode,
//													payModeNo, chequeDate,
//													bankName, contactName,Mobile, Email));
								}
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	public ArrayList<OrderDetailModelData> getOrderDetailsList() {
		return orderDetailList;
	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

}
