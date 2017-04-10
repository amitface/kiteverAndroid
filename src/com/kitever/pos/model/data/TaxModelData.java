package com.kitever.pos.model.data;

public class TaxModelData {
	private String taxID, code, taxName, taxPer, wefDateFrom, wefDateTill,
			createdDate, isActive, WefDateFromInMilliSecond,
			WefDateTillInMilliSecond;




	public TaxModelData(String taxID, String code, String taxName,
						String taxPer, String wefDateFrom, String wefDateTill,
						String createdDate, String WefDateFromInMilliSecond, String WefDateTillInMilliSecond,String isActive) {
		super();
		this.taxID = taxID;

		this.code = code;
		this.taxName = taxName;

		this.taxPer = taxPer;
		this.wefDateFrom = wefDateFrom;
		this.wefDateTill = wefDateTill;
		this.createdDate = createdDate;
		this.isActive = isActive;
		this.WefDateFromInMilliSecond = WefDateFromInMilliSecond;
		this.WefDateTillInMilliSecond = WefDateTillInMilliSecond;
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

	public String getWefDateFromInMilliSecond() {
		return WefDateFromInMilliSecond;
	}

	public void setTaxID(String taxID) {
		this.taxID = taxID;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public void setTaxPer(String taxPer) {
		this.taxPer = taxPer;
	}

	public void setWefDateFrom(String wefDateFrom) {
		this.wefDateFrom = wefDateFrom;
	}

	public void setWefDateTill(String wefDateTill) {
		this.wefDateTill = wefDateTill;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public void setWefDateFromInMilliSecond(String wefDateFromInMilliSecond) {
		WefDateFromInMilliSecond = wefDateFromInMilliSecond;
	}

	public String getWefDateTillInMilliSecond() {
		return WefDateTillInMilliSecond;
	}

	public void setWefDateTillInMilliSecond(String wefDateTillInMilliSecond) {
		WefDateTillInMilliSecond = wefDateTillInMilliSecond;
	}

}
