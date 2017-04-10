package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kitever.pos.model.data.FetchParentCategoryModelData;

public class FetchParentCategoryModel {

	private ArrayList<FetchParentCategoryModelData> fetchParentCategoryList;

	public ArrayList<FetchParentCategoryModelData> getFetchParentCategoryList() {
		return fetchParentCategoryList;
	}

	public FetchParentCategoryModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		if (response != null && response.length() > 0) {
			try {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject != null && jsonObject.has("FetchCategory")) {
					JSONArray jsonArray = jsonObject
							.getJSONArray("FetchCategory");
					if (jsonArray != null && jsonArray.length() > 0) {
						fetchParentCategoryList = new ArrayList<FetchParentCategoryModelData>();
						String categoryID = null, categoryName = null,type=null;
						for (int k = 0; k < jsonArray.length(); k++) {
							JSONObject object = jsonArray.getJSONObject(k);
							if (object != null) {
								if (object.has("CategoryID")) {
									categoryID = object.getString("CategoryID");
								}
								if (object.has("CategoryName")) {
									categoryName = object
											.getString("CategoryName");
								}
								if (object.has("CategoryType")) {
									type = object
											.getString("CategoryType");
								}
								fetchParentCategoryList
										.add(new FetchParentCategoryModelData(categoryID, categoryName,type));
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}
}
