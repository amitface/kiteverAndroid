package com.kitever.pos.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kitever.pos.model.data.InsertUpdateFetchInvoiceSettingData;
import com.kitever.pos.model.data.InvoiceSetting;
import com.kitever.pos.model.data.OrderDetailModelData;

public class FetchInvoiceSettingModel {
	private ArrayList<InsertUpdateFetchInvoiceSettingData> insertUpdateFetchInvoiceSettingData;
	private String message, status,header,footer,settingID,orderPrefix,tinNo, panNo, userID,servicetaxNo,invoicePrefix;
	
	public FetchInvoiceSettingModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}
	
	
	private void parseResponse(String response) {
		Gson gson = new Gson();
		insertUpdateFetchInvoiceSettingData = new ArrayList<>();
		if (response != null && response.length() > 0) {
			try {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject != null) {
					if (jsonObject.has("Message")) {
						message = jsonObject.getString("Message");
					}
					if (jsonObject.has("Status")) {
						status = jsonObject.getString("Status");						
//						insertUpdateFetchInvoiceSettingData = gson.fromJson(jsonObject.toString(), new TypeToken<List<InsertUpdateFetchInvoiceSettingData>>(){}.getType());
						JSONArray array = jsonObject.getJSONArray("InvoiceSetting");
						header = array.getJSONObject(0).getString("Header");		
						footer = array.getJSONObject(0).getString("Footer");
//						settingID = array.getJSONObject(0).getString("SettingID");
						orderPrefix = array.getJSONObject(0).getString("OrderPrefix");
						tinNo = array.getJSONObject(0).getString("TinNo");
						userID = array.getJSONObject(0).getString("UserID");
						panNo = array.getJSONObject(0).getString("PanNo");
						servicetaxNo = array.getJSONObject(0).getString("ServicetaxNo");
						invoicePrefix = array.getJSONObject(0).getString("InvoicePrefix");												
						
						insertUpdateFetchInvoiceSettingData.add(new InsertUpdateFetchInvoiceSettingData(message,status,header,footer,orderPrefix,tinNo,userID, panNo, servicetaxNo,invoicePrefix));
//						System.out.println("header "+header+" footer "+footer+" json "+jsonObject.toString());
					}
					
				}
			} catch (JsonSyntaxException e) {
				// TODO: handle exception
				System.out.println("Error"+ e.getMessage().toString());
//				Log.d("this => ", e.printStackTrace().toString());
				
			}
			
				catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	public ArrayList<InsertUpdateFetchInvoiceSettingData> getInvoiceDetailList() {
		return insertUpdateFetchInvoiceSettingData;
	}

	public String getMessage() {
		return insertUpdateFetchInvoiceSettingData.get(0).getMessage();
	}
//
	public String getStatus() {
		return insertUpdateFetchInvoiceSettingData.get(0).getStatus();
	}
}
