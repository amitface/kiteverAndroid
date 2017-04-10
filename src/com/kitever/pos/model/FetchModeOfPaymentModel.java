package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kitever.pos.model.data.FetchModeOfPaymentModelData;

public class FetchModeOfPaymentModel {

	ArrayList<FetchModeOfPaymentModelData> list;

	public FetchModeOfPaymentModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);

	}

	private void parseResponse(String response) {
		try {
			if (response != null && response.length() > 0) {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject != null && jsonObject.has("FetchBrand")) {
					JSONArray jsonArray = jsonObject.getJSONArray("FetchBrand");
					if (jsonArray != null && jsonArray.length() > 0) {
						String id = null, mode = null;
						list = new ArrayList<FetchModeOfPaymentModelData>();
						for (int k = 0; k < jsonArray.length(); k++) {
							JSONObject object = jsonArray.getJSONObject(k);
							if (object != null) {
								if (object.has("ID")) {
									id = object.getString("ID");
								}
								if (object.has("Mode")) {
									mode = object.getString("Mode");
								}
								list.add(new FetchModeOfPaymentModelData(id,
										mode));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public ArrayList<FetchModeOfPaymentModelData> getFetchModeOfPaymentList() {
		return list;
	}

}
