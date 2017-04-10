package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class FetchCategoryModel {

	private String categoryID;
	private String categoryCode;
	private String categoryName;
	private String parentCategory;
	private String type;
	private String description;
	private String createdDate;
	private String userID;
	private String isActive;
	private String status = "true";
	private ArrayList<FetchCategoryModelData> fetchCategoryModelDatasList;

	public FetchCategoryModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject != null && jsonObject.has("FetchCategory")) {
				JSONArray jsonArray = jsonObject.getJSONArray("FetchCategory");
				if (jsonArray != null && jsonArray.length() > 0) {
					fetchCategoryModelDatasList = new ArrayList<FetchCategoryModelData>();
					for (int k = 0; k < jsonArray.length(); k++) {
						JSONObject object = jsonArray.getJSONObject(k);
						if (object != null) {
							if (object.has("CategoryID")) {
								categoryID = object.getString("CategoryID");
							}
							if (object.has("CategoryCode")) {
								categoryCode = object.getString("CategoryCode");
							}
							if (object.has("CategoryName")) {
								categoryName = object.getString("CategoryName");
							}
							if (object.has("ParentCategory")) {
								parentCategory = object
										.getString("ParentCategory");
							}
							if (object.has("Type")) {
								type = object.getString("Type");
							}
							if (object.has("Description")) {
								description = object.getString("Description");
							}
							if (object.has("CreatedDate")) {
								createdDate = object.getString("CreatedDate");
							}
							if (object.has("UserID")) {
								userID = object.getString("UserID");
							}
							if (object.has("IsActive")) {
								isActive = object.getString("IsActive");
							}
							fetchCategoryModelDatasList
									.add(new FetchCategoryModelData(categoryID,
											categoryCode, categoryName,
											parentCategory, type, description,
											createdDate, userID, isActive));
						}
					}
				} else {
					status = "false";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public String getStatus() {
		return status;
	}

	public ArrayList<FetchCategoryModelData> getFetchCategoryModelDataList() {
		return fetchCategoryModelDatasList;
	}

}
