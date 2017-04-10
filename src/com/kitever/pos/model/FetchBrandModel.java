package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchBrandModel {

	private ArrayList<FetchBrandModelData> fetchBrandList;

	public FetchBrandModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject != null) {
				if (jsonObject.has("FetchCategory")) {
					JSONArray jsonArray = jsonObject
							.getJSONArray("FetchCategory");
					if (jsonArray != null && jsonArray.length() > 0) {
						fetchBrandList = new ArrayList<FetchBrandModelData>();
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
							fetchBrandList
									.add(new FetchBrandModelData(brandID,
											brandCode, brandName, createdDate,
											isActive));
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<FetchBrandModelData> getFetchBrandList() {
		return fetchBrandList;
	}

}
