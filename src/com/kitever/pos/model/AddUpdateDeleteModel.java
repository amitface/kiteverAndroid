package com.kitever.pos.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddUpdateDeleteModel {

	private String status, message;

	public AddUpdateDeleteModel(String response, String key) {
		// TODO Auto-generated constructor stub
		parseResponse(response, key);
	}

	private void parseResponse(String response, String key) {
		try {

			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject != null) {
				if (jsonObject.has(key)) {
					JSONArray root = jsonObject.getJSONArray(key);
					if (root != null && root.length() > 0) {
						JSONObject object = root.getJSONObject(0);
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
