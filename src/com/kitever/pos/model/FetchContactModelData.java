package com.kitever.pos.model;

public class FetchContactModelData {

	private String contactId, contactName;

	public FetchContactModelData(String contactId, String contactName) {
		super();
		this.contactId = contactId;
		this.contactName = contactName;
	}

	public String getContactId() {
		return contactId;
	}

	public String getContactName() {
		return contactName;
	}

}
