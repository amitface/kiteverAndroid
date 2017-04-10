package com.kitever.pos.model;

import com.kitever.pos.model.data.FetchProductModelData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sms19.inapp.msg.constant.Utils;

public class FetchProductModel {

	private ArrayList<FetchProductModelData> fetchProductList;

	public FetchProductModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		if (response != null && response.length() > 0) {
			try {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject != null && jsonObject.has("FetchProduct")) {
					JSONArray jsonArray = jsonObject
							.getJSONArray("FetchProduct");
					if (jsonArray != null && jsonArray.length() > 0) {
						fetchProductList = new ArrayList<FetchProductModelData>();
						String categoryName = null, brandName = null, productName = null, productID = null, perUnitPrice = null, units = null, userID = null, cUG = null,taxApplied=null;
						String ProductImage=null,PriceWithTax=null;
						int AvailableStock=0;
						Double productPrice=0.0;
						for (int k = 0; k < jsonArray.length(); k++) {
							JSONObject object = jsonArray.getJSONObject(k);
							if (object != null) {
								if (object.has("CategoryName")) {
									categoryName = object
											.getString("CategoryName");
								}
								if (object.has("BrandName")) {
									brandName = object.getString("BrandName");
								}
								if (object.has("ProductName")) {
									productName = object
											.getString("ProductName");
								}
								if (object.has("ProductID")) {
									productID = object.getString("ProductID");
								}
								if (object.has("PerUnitPrice")) {
									productPrice=object.getDouble("PerUnitPrice");
									perUnitPrice = Utils.doubleToString(productPrice);
								}
								if (object.has("Units")) {
									units = object.getString("Units");
								}
								if (object.has("UserID")) {
									userID = object.getString("UserID");
								}
								if (object.has("CUG")) {
									cUG = object.getString("CUG");
								}
								if (object.has("TaxApplied")) {
									taxApplied = object.getString("TaxApplied");
								}
								if (object.has("PriceWithTax")) {
									PriceWithTax = object.getString("PriceWithTax");
								}
								if (object.has("ProductImage")) {
									ProductImage = object.getString("ProductImage");
								}
								if (object.has("AvailableStock")) {
									AvailableStock = object.getInt("AvailableStock");
								}
								fetchProductList.add(new FetchProductModelData(
										categoryName, brandName, productName,
										productID, perUnitPrice, units, userID,
										cUG,taxApplied,ProductImage,PriceWithTax,k,AvailableStock));
							}
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ArrayList<FetchProductModelData> getFetchProductList() {
		return fetchProductList;
	}

	public void setFetchProductList(ArrayList<FetchProductModelData> fetchProductList) {
		this.fetchProductList = fetchProductList;
	}
}
