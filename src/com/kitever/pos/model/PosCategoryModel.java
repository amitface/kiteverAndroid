package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kitever.pos.model.data.PosCategoryModelData;

public class PosCategoryModel {

	private String categoryID;
	private String categoryCode;
	private String categoryName;
	private String parentCategory="None";
	private String ParentCategoryID="";
	private String type;
	private String description;
	private String createdDate;
	private String userID;
	private String isActive;
	private String status;
	private String message;


	private ArrayList<PosCategoryModelData> categoryModelDatasList;

	public PosCategoryModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject != null) {
				if (jsonObject.has("Status")) {
					status = jsonObject.getString("Status");
				}
				if (jsonObject.has("Message")) {
					message = jsonObject.getString("Message");
				}
				if (jsonObject.has("Category")) {
					JSONArray jsonArray = jsonObject.getJSONArray("Category");
					if (jsonArray != null && jsonArray.length() > 0) {
						categoryModelDatasList = new ArrayList<PosCategoryModelData>();
						for (int k = 0; k < jsonArray.length(); k++) {
							JSONObject object = jsonArray.getJSONObject(k);
							if (object != null) {
								if (object.has("CategoryID")) {
									categoryID = object.getString("CategoryID");
								}
								if (object.has("CategoryCode")) {
									categoryCode = object
											.getString("CategoryCode");
								}
								if (object.has("CategoryName")) {
									categoryName = object
											.getString("CategoryName");
								}
								if (object.has("ParentCategory")) {
									parentCategory = object
											.getString("ParentCategory");
								}
								if(object.has("ParentCategoryID"))
								{
									ParentCategoryID=object.getString("ParentCategoryID");
								}
								if (object.has("Type")) {
									type = object.getString("Type");
								}
								if (object.has("Description")) {
									description = object
											.getString("Description");
								}
								if (object.has("CreatedDate")) {
									createdDate = object
											.getString("CreatedDate");
								}
								if (object.has("UserID")) {
									userID = object.getString("UserID");
								}
								if (object.has("IsActive")) {
									isActive = object.getString("IsActive");
								}
								categoryModelDatasList
										.add(new PosCategoryModelData(
												categoryID, categoryCode,
												categoryName, parentCategory,ParentCategoryID,
												type, description, createdDate,
												userID, isActive));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public ArrayList<PosCategoryModelData> getCategoryModelDataList() {
		return categoryModelDatasList;
	}

}
