package com.kitever.pos.model.data;

public class FetchContactModelData {

	private String contactId, contactName,CompanyName;

	public FetchContactModelData(String contactId, String contactName,String CompanyName) {
		super();
		this.contactId = contactId;
		this.contactName = contactName;
		this.CompanyName=CompanyName;
	}

	public String getContactId() {
		return contactId;
	}

	public String getContactName() {
		return contactName;
	}

	public String getCompanyName() { return CompanyName; }
}
