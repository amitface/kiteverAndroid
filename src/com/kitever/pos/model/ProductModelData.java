package com.kitever.pos.model;

public class ProductModelData {
	private String categoryName;
	private String brandName;
	private String productName;
	private String productID;
	private String perUnitPrice;
	private String units;
	private String productImage;
	private String isActive;

	public ProductModelData(String categoryName, String brandName,
			String productName, String productID, String perUnitPrice,
			String units, String productImage, String isActive) {
		super();
		this.categoryName = categoryName;
		this.brandName = brandName;
		this.productName = productName;
		this.productID = productID;
		this.perUnitPrice = perUnitPrice;
		this.units = units;
		this.productImage = productImage;
		this.isActive = isActive;
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

	public String getProductImage() {
		return productImage;
	}

	public String getIsActive() {
		return isActive;
	}

}
