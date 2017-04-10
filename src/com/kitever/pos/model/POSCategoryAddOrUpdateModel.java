package com.kitever.pos.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class POSCategoryAddOrUpdateModel {

	private String status, message;

	public POSCategoryAddOrUpdateModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		try {

			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject != null) {
				if (jsonObject.has("CategoryInsert")) {
					JSONArray categoryInsert = jsonObject
							.getJSONArray("CategoryInsert");
					if (categoryInsert != null && categoryInsert.length() > 0) {
						JSONObject object = categoryInsert.getJSONObject(0);
						if (object != null) {
							if (object.has("Status")) {
								status = object.getString("Status");
							}
							if (object.has("Message")) {
								message = object.getString("Message");
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
}
