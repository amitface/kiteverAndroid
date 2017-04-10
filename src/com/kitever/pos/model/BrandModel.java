package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kitever.pos.model.data.BrandModelData;

public class BrandModel {

	private ArrayList<BrandModelData> brandList;
	private String message;
	private String status;

	public BrandModel(String response) {
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
				if (jsonObject.has("Brand")) {
					JSONArray jsonArray = jsonObject.getJSONArray("Brand");
					if (jsonArray != null && jsonArray.length() > 0) {
						brandList = new ArrayList<BrandModelData>();
						String brandID = null, brandCode = null, brandName = null, createdDate = null, isActive = null;
						for (int k = 0; k < jsonArray.length(); k++) {
							JSONObject object = jsonArray.getJSONObject(k);
							if (object.has("BrandID")) {
								brandID = object.getString("BrandID");
							}
							if (object.has("BrandCode")) {
								brandCode = object.getString("BrandCode");
							}
							if (object.has("BrandName")) {
								brandName = object.getString("BrandName");
							}
							if (object.has("CreatedDate")) {
								createdDate = object.getString("CreatedDate");
							}
							if (object.has("IsActive")) {
								isActive = object.getString("IsActive");
							}
							brandList
									.add(new BrandModelData(brandID, brandCode,
											brandName, createdDate, isActive));
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<BrandModelData> getBrandList() {
		return brandList;
	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

}
