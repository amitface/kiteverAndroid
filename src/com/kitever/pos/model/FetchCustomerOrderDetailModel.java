package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kitever.pos.model.data.FetchCustomerOrderDetailModelData;

public class FetchCustomerOrderDetailModel {

	private ArrayList<FetchCustomerOrderDetailModelData> fetchCustomerOrderDetailList;
	private String status, message;

	public FetchCustomerOrderDetailModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		if (response != null && response.length() > 0) {
			try {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject != null) {
					if (jsonObject.has("Status")) {
						status = jsonObject.getString("Status");
					}
					if (jsonObject.has("Message")) {
						message = jsonObject.getString("Message");
					}
					if (jsonObject.has("CustomerOrderDetail")) {
						JSONArray jsonArray = jsonObject
								.getJSONArray("CustomerOrderDetail");
						if (jsonArray != null && jsonArray.length() > 0) {
							fetchCustomerOrderDetailList=new ArrayList<FetchCustomerOrderDetailModelData>();
							String orderMasterID = null, orderCode = null, orderDate = null, userName = null, billAmount = null, taxAmount = null, discountAmount = null, paidAmount = null, balanceAmount = null, payMode = null, payModeNo = null, chequeDate = null, bankName = null, contactName = null;
							for (int k = 0; k < jsonArray.length(); k++) {
								JSONObject object = jsonArray.getJSONObject(k);
								if (object != null) {
									if (object.has("OrderMasterID")) {
										orderMasterID = object.getString("OrderMasterID");
									}
									if (object.has("OrderCode")) {
										orderCode = object.getString("OrderCode");
									}
									if (object.has("OrderDate")) {
										orderDate = object.getString("OrderDate");
									}
									if (object.has("UserName")) {
										userName = object.getString("UserName");
									}
									if (object.has("BillAmount")) {
										billAmount = object.getString("BillAmount");
									}
									if (object.has("TaxAmount")) {
										taxAmount = object.getString("TaxAmount");
									}
									if (object.has("DiscountAmount")) {
										discountAmount = object.getString("DiscountAmount");
									}
									if (object.has("PaidAmount")) {
										paidAmount = object.getString("PaidAmount");
									}
									if (object.has("BalanceAmount")) {
										balanceAmount = object.getString("BalanceAmount");
									}
									if (object.has("PayMode")) {
										payMode = object.getString("PayMode");
									}
									if (object.has("PayModeNo")) {
										payModeNo = object.getString("PayModeNo");
									}
									if (object.has("ChequeDate")) {
										chequeDate = object.getString("ChequeDate");
									}
									if (object.has("BankName")) {
										bankName = object.getString("BankName");
									}
									if (object.has("contactName")) {
										contactName = object.getString("contactName");
									}
									fetchCustomerOrderDetailList
											.add(new FetchCustomerOrderDetailModelData(
													orderMasterID, orderCode,
													orderDate, userName,
													billAmount, taxAmount,
													discountAmount, paidAmount,
													balanceAmount, payMode,
													payModeNo, chequeDate,
													bankName, contactName));
								}
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public ArrayList<FetchCustomerOrderDetailModelData> getFetchCustomerOrderDetailList() {
		return fetchCustomerOrderDetailList;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

}
