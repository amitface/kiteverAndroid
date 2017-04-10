package com.kitever.pos.model;

public class FetchBrandModelData {

	String brandID, brandCode, brandName, createdDate, isActive;

	public FetchBrandModelData(String brandID, String brandCode,
			String brandName, String createdDate, String isActive) {
		super();
		this.brandID = brandID;
		this.brandCode = brandCode;
		this.brandName = brandName;
		this.createdDate = createdDate;
		this.isActive = isActive;
	}

	public String getBrandID() {
		return brandID;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getIsActive() {
		return isActive;
	}

}
