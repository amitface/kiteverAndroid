package com.kitever.models;

import java.util.ArrayList;
import java.util.List;

public class ContactList {

	private List<ContactInfo> ContactDetails;

	public ArrayList<ContactInfo> getContactDetails() {
		return (ArrayList<ContactInfo>) ContactDetails;
	}

	public void setContactDetails(List<ContactInfo> contactDetails) {
		ContactDetails = contactDetails;
	}
}
