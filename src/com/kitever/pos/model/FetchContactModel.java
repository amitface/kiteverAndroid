package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kitever.pos.model.data.FetchContactModelData;

public class FetchContactModel {

	ArrayList<FetchContactModelData> fetchContactList;

	public FetchContactModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		if (response != null && response.length() > 0) {
			try {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject != null && jsonObject.has("FetchCustomer")) {
					JSONArray jsonArray = jsonObject
							.getJSONArray("FetchCustomer");
					if (jsonArray != null && jsonArray.length() > 0) {
						String contactId = null, contactName = null,CompanyName="";
						fetchContactList = new ArrayList<FetchContactModelData>();
						for (int k = 0; k < jsonArray.length(); k++) {
							JSONObject object = jsonArray.getJSONObject(k);
							if (object != null) {
								if (object.has("Contact_ID")) {
									contactId = object.getString("Contact_ID");
								}
								if (object.has("ContactName")) {
									contactName = object
											.getString("ContactName");
								}
								if (object.has("CompanyName")) {
									CompanyName = object
											.getString("CompanyName");
								}
								fetchContactList.add(new FetchContactModelData(
										contactId, contactName,CompanyName));
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	public ArrayList<FetchContactModelData> getFetchContactList() {
		return fetchContactList;
	}

}
