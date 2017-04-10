package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kitever.pos.model.data.FetchUnitModelData;

public class FetchUnitModel {

	private ArrayList<FetchUnitModelData> fetchUnitList;

	public FetchUnitModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject != null) {
				if (jsonObject.has("Fetchunit")) {
					JSONArray root = jsonObject.getJSONArray("Fetchunit");
					if (root != null && root.length() > 0) {
						String iD = null, unitName = null, convertionGroup = null;
						fetchUnitList = new ArrayList<FetchUnitModelData>();
						for (int k = 0; k < root.length(); k++) {
							JSONObject object = root.getJSONObject(k);
							if (object != null) {
								if (object.has("ID")) {
									iD = object.getString("ID");
								}
								if (object.has("UnitName")) {
									unitName = object.getString("UnitName");
								}
								if (object.has("ConvertionGroup")) {
									convertionGroup = object
											.getString("ConvertionGroup");
								}
								fetchUnitList.add(new FetchUnitModelData(iD,
										unitName, convertionGroup));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public ArrayList<FetchUnitModelData> getFetchUnitList() {
		return fetchUnitList;
	}

}
