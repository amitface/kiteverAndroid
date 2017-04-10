package com.kitever.models;

import java.util.ArrayList;
import java.util.List;

public class InsertContactToGroup {
	// private String Page;
	// private String Userid;
	private List<InsertContact> contacts;

	public ArrayList<InsertContact> getContacts() {
		return (ArrayList<InsertContact>) contacts;
	}

	public void setContacts(List<InsertContact> contacts) {
		this.contacts = contacts;
	}

	public InsertContactToGroup(List<InsertContact> contacts) {
		super();
		this.contacts = contacts;
	}

	public void parseContactinto(List<ContactInfo> contacts) {

	}
}
