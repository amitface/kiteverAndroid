package com.kitever.pos.model;

import com.kitever.pos.model.data.ProductModelData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import sms19.inapp.msg.constant.Utils;

public class ProductModel {

	private ArrayList<ProductModelData> productDataList;
	private String status ,message;

	public ProductModel(String response) {
		// TODO Auto-generated constructor stub
		parseResponse(response);
	}

	private void parseResponse(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject != null) {
				if(jsonObject.has("Message")){
					message=jsonObject.getString("Message");
				}
				if(jsonObject.has("Status")){
					status=jsonObject.getString("Status");
				}
				if (jsonObject.has("Product")) {
					String categoryName = null, brandName = null, productName = null, productID = null, perUnitPrice = null, units = null, productImage = null, isActive = null;
					String description=null;String TaxApplied=null,PriceWithTax=null, AvailableStock="";
					double ProductPrice=0;

					JSONArray fetchProductArray = jsonObject
							.getJSONArray("Product");
					if (fetchProductArray != null
							&& fetchProductArray.length() > 0) {
						productDataList = new ArrayList<ProductModelData>();
						for (int k = 0; k < fetchProductArray.length(); k++) {
							JSONObject object = fetchProductArray
									.getJSONObject(k);
							if (object.has("CategoryName")) {
								categoryName = object.getString("CategoryName");
							}
							if (object.has("BrandName")) {
								brandName = object.getString("BrandName");
							}
							if (object.has("ProductName")) {
								productName = object.getString("ProductName");
							}
							if (object.has("ProductID")) {
								productID = object.getString("ProductID");
							}
							if (object.has("PerUnitPrice")) {
								ProductPrice=object.getDouble("PerUnitPrice");
								perUnitPrice = Utils.doubleToString(ProductPrice);
							}							
							if (object.has("PriceWithTax")) {
								PriceWithTax = object.getString("PriceWithTax");
							}
							if (object.has("Unit")) {
								units = object.getString("Unit");
							}
							if (object.has("ProductImage")) {
								productImage = object.getString("ProductImage");
							}
							if (object.has("IsActive")) {
								isActive = object.getString("IsActive");
							}
							
							//Description
							if(object.has("Description")) {									
								description = object.getString("Description");								
							}
							
							if(object.has("TaxApplied")) {									
								TaxApplied = object.getString("TaxApplied");								
							}
							if(object.has("AvailableStock")) {
								AvailableStock = object.getString("AvailableStock");
							}
							productDataList.add(new ProductModelData(
									categoryName, brandName, productName,
									productID, perUnitPrice, units,
									productImage, isActive,description,TaxApplied,PriceWithTax,AvailableStock));
						}
					} 
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public ArrayList<ProductModelData> getProductList() {
		return productDataList;
	}
	public String getMessage() {
		return message;
	}
	public String getStatus() {
		return status;
	}
}
