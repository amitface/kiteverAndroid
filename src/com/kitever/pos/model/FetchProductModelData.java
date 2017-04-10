package com.kitever.pos.model;

public class FetchProductModelData {

	private String categoryName, brandName, productName, productID,
			perUnitPrice, units, userID, cUG, taxApplied;

	public FetchProductModelData(String categoryName, String brandName,
			String productName, String productID, String perUnitPrice,
			String units, String userID, String cUG, String taxApplied) {
		super();
		this.categoryName = categoryName;
		this.brandName = brandName;
		this.productName = productName;
		this.productID = productID;
		this.perUnitPrice = perUnitPrice;
		this.units = units;
		this.userID = userID;
		this.cUG = cUG;
		this.taxApplied = taxApplied;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getBrandName() {
		return brandName;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductID() {
		return productID;
	}

	public String getPerUnitPrice() {
		return perUnitPrice;
	}

	public String getUnits() {
		return units;
	}

	public String getUserID() {
		return userID;
	}

	public String getcUG() {
		return cUG;
	}

	public String getTaxApplied() {
		return taxApplied;
	}

}
