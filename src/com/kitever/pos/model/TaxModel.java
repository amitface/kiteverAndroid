package com.kitever.pos.model;

import com.kitever.pos.model.data.TaxModelData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaxModel {

	private ArrayList<TaxModelData> fetchTaxList;
	private String message, status,taxSettingEnable="false";

	public TaxModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject != null) {
				if (jsonObject.has("Message")) {
					message = jsonObject.getString("Message");
				}
				if (jsonObject.has("Status")) {
					status = jsonObject.getString("Status");
				}				
				
				if (jsonObject.has("Tax")) {
					JSONArray jsonArray = jsonObject
							.getJSONArray("Tax");
					if (jsonArray != null && jsonArray.length() > 0) {
						String taxID = null, code = null, taxName = null, taxPer = null, wefDateFrom = null, wefDateTill = null, createdDate = null, WefDateFromInMilliSecond =null, WefDateTillInMilliSecond = null, isActive = null;
						fetchTaxList = new ArrayList<TaxModelData>();
						for (int k = 0; k < jsonArray.length(); k++) {
							JSONObject object = jsonArray.getJSONObject(k);
							if (object.has("TaxID")) {
								taxID = object.getString("TaxID");
							}							
							if (object.has("TaxSetting")) {
								taxSettingEnable = object.getString("TaxSetting");
							}
							if (object.has("Code")) {
								code = object.getString("Code");
							}
							if (object.has("TaxName")) {
								taxName = object.getString("TaxName");
							}
							if (object.has("TaxPer")) {
								taxPer = object.getString("TaxPer");
							}
							if (object.has("WefDateFrom")) {
								wefDateFrom = object.getString("WefDateFrom");
							}
							if (object.has("WefDateTill")) {
								wefDateTill = object.getString("WefDateTill");
							}
							if (object.has("CreatedDate")) {
								createdDate = object.getString("CreatedDate");
							}
							if (object.has("IsActive")) {
								isActive = object.getString("IsActive");
							}
							if (object.has("WefDateFromInMilliSecond")) {
								WefDateFromInMilliSecond = object.getString("WefDateFromInMilliSecond");
							}
							if (object.has("WefDateTillInMilliSecond")) {
								WefDateTillInMilliSecond = object.getString("WefDateTillInMilliSecond");
							}
							fetchTaxList.add(new TaxModelData(taxID, code,
									taxName, taxPer, wefDateFrom, wefDateTill,
									createdDate, WefDateFromInMilliSecond, WefDateTillInMilliSecond, isActive));
						}
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}
	
	public String getTaxSettingEnable() {
		return taxSettingEnable;
	}

	public ArrayList<TaxModelData> getFetchTaxList() {
		return fetchTaxList;
	}
}
