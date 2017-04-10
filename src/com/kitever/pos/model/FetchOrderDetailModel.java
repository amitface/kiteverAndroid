package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class FetchOrderDetailModel {

	private ArrayList<FetchOrderDetailModelData> fetchOrderDetailList;

	public FetchOrderDetailModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject != null) {
				if (jsonObject.has("FetchProduct")) {
					JSONArray root = jsonObject.getJSONArray("FetchProduct");
					if (root != null && root.length() > 0) {
						fetchOrderDetailList = new ArrayList<FetchOrderDetailModelData>();
						String orderMasterID = null, orderDate = null, orderCode = null, contactName = null, billAmount = null, taxAmount = null, discountAmount = null, paidAmount = null, balanceAmount = null, payMode = null, payModeNo = null, chequeDate = null, bankName = null, status = null;
						for (int k = 0; k < root.length(); k++) {
							JSONObject object = root.getJSONObject(k);
							if (object.has("OrderMasterID")) {
								orderMasterID = object
										.getString("OrderMasterID");
							}
							if (object.has("OrderDate")) {
								orderDate = object.getString("OrderDate");
							}
							if (object.has("OrderCode")) {
								orderCode = object.getString("OrderCode");
							}
							if (object.has("contactName")) {
								contactName = object.getString("contactName");
							}
							if (object.has("BillAmount")) {
								billAmount = object.getString("BillAmount");
							}
							if (object.has("TaxAmount")) {
								taxAmount = object.getString("TaxAmount");
							}
							if (object.has("DiscountAmount")) {
								discountAmount = object
										.getString("DiscountAmount");
							}
							if (object.has("PaidAmount")) {
								paidAmount = object.getString("PaidAmount");
							}
							if (object.has("BalanceAmount")) {
								balanceAmount = object
										.getString("BalanceAmount");
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
							if (object.has("Status")) {
								status = object.getString("Status");
							}
							fetchOrderDetailList
									.add(new FetchOrderDetailModelData(
											orderMasterID, orderDate,
											orderCode, contactName, billAmount,
											taxAmount, discountAmount,
											paidAmount, balanceAmount, payMode,
											payModeNo, chequeDate, bankName,
											status));
						}
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public ArrayList<FetchOrderDetailModelData> getFetchOrderDetailList() {
		return fetchOrderDetailList;
	}

}
