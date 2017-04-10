package com.kitever.pos.model;

public class TaxModelData {
	private String taxID, code, taxName, taxPer, wefDateFrom, wefDateTill,
			createdDate, isActive;

	public TaxModelData(String taxID, String code, String taxName,
			String taxPer, String wefDateFrom, String wefDateTill,
			String createdDate, String isActive) {
		super();
		this.taxID = taxID;
		this.code = code;
		this.taxName = taxName;
		this.taxPer = taxPer;
		this.wefDateFrom = wefDateFrom;
		this.wefDateTill = wefDateTill;
		this.createdDate = createdDate;
		this.isActive = isActive;
	}

	public String getTaxID() {
		return taxID;
	}

	public String getCode() {
		return code;
	}

	public String getTaxName() {
		return taxName;
	}

	public String getTaxPer() {
		return taxPer;
	}

	public String getWefDateFrom() {
		return wefDateFrom;
	}

	public String getWefDateTill() {
		return wefDateTill;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getIsActive() {
		return isActive;
	}

}
