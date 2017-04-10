package com.kitever.pos.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class FetchTaxModel {

	private ArrayList<FetchTaxModelData> fetchTaxList;

	public FetchTaxModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.has("FetchCategory")) {
				JSONArray jsonArray = jsonObject.getJSONArray("FetchCategory");
				if (jsonArray != null && jsonArray.length() > 0) {
					String taxID = null, code = null, taxName = null, taxPer = null, wefDateFrom = null, wefDateTill = null, createdDate = null, isActive = null;
					fetchTaxList = new ArrayList<FetchTaxModelData>();
					for (int k = 0; k < jsonArray.length(); k++) {
						JSONObject object = jsonArray.getJSONObject(k);
						if (object.has("TaxID")) {
							taxID = object.getString("TaxID");
						}
						if (object.has("Code")) {
							code = object.getString("Code");
						}
						if (object.has("TaxName")) {
							taxName = object.getString("TaxName");
						}
						if (object.has("TaxPer")) {
							taxPer = object.getString("TaxPer");
						}
						if (object.has("WefDateFrom")) {
							wefDateFrom = object.getString("WefDateFrom");
						}
						if (object.has("WefDateTill")) {
							wefDateTill = object.getString("WefDateTill");
						}
						if (object.has("CreatedDate")) {
							createdDate = object.getString("CreatedDate");
						}
						if (object.has("IsActive")) {
							isActive = object.getString("IsActive");
						}
						fetchTaxList.add(new FetchTaxModelData(taxID, code,
								taxName, taxPer, wefDateFrom, wefDateTill,
								createdDate, isActive));
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public ArrayList<FetchTaxModelData> getFetchTaxList() {
		return fetchTaxList;
	}
}
