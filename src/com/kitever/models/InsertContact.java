package com.kitever.models;

public class InsertContact extends ContactInfo {
	private String contactName;
	private String countryCode;
	private String contactDOB;
	private String contactAnniversary;
	private String imageUrl;
	private String contactMobile;

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getContactDOB() {
		return contactDOB;
	}

	public void setContactDOB(String contactDOB) {
		this.contactDOB = contactDOB;
	}

	public String getContactAnniversary() {
		return contactAnniversary;
	}

	public void setContactAnniversary(String contactAnniversary) {
		this.contactAnniversary = contactAnniversary;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactJID() {
		return contactJID;
	}

	public void setContactJID(String contactJID) {
		this.contactJID = contactJID;
	}

	private String contactJID;
}
