package com.kitever.pos.model;

public class BrandModelData {

	String brandID, brandCode, brandName, createdDate, isActive, status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandName() {
		return brandName;
	}
	
	public BrandModelData(String brandName,  String status) {
		super();
//		this.brandID = brandID;
//		this.brandCode = brandCode;
		this.brandName = brandName;
//		this.createdDate = createdDate;
//		this.isActive = isActive;
		this.status = status;
	}

	@Override
	public String toString() {
		return "BrandModelData [brandName=" + brandName + ", status=" + status
				+ "]";
	}

//	public String getBrandID() {
//		return brandID;
//	}
//
//	public String getBrandCode() {
//		return brandCode;
//	}

	

//	public String getCreatedDate() {
//		return createdDate;
//	}
//
//	public String getIsActive() {
//		return isActive;
//	}

}
