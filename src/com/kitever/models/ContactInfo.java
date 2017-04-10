package com.kitever.models;

import java.io.Serializable;


public class ContactInfo implements Serializable {


    /**
     * "contact_id": 10028,
     * "User_ID": 12,
     * "Contact_Name": "Abc Consultant",
     * "Contact_Mobile": "9599688669",
     * "contact_email": null,
     * "Contact_DOB": "",
     * "Contact_Anniversary": "",
     * "countryCode": "+91",
     * "ImageUrl": "",
     * "UserType": "N"
     */
    private static final long serialVersionUID = -7975537618038983849L;

    public String contact_id;

    public String Contact_Name;
    public String Contact_Mobile;

    public String countryCode;

    public String Contact_DOB;

    public String Contact_Anniversary;
    public String contact_email;

    public String CompanyName = "";
    public String Address = "";
    public String State = "0";
    public String City = "0";
    public String contactNumber ;


    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public boolean isSelected;

    public ContactInfo() {
        // TODO Auto-generated constructor stub
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_Name() {
        return Contact_Name;
    }

    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }

    public String getContact_Mobile() {
        return Contact_Mobile;
    }

    public void setContact_Mobile(String contact_Mobile) {
        Contact_Mobile = contact_Mobile;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getContact_DOB() {
        return Contact_DOB;
    }

    public void setContact_DOB(String contact_DOB) {
        Contact_DOB = contact_DOB;
    }

    public String getContact_Anniversary() {
        return Contact_Anniversary;
    }

    public void setContact_Anniversary(String contact_Anniversary) {
        Contact_Anniversary = contact_Anniversary;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        if (city == null) city = "0";
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        if (state == null) state = "0";
        State = state;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public ContactInfo(String contact_id, String contact_Name,
                       String contact_Mobile, String countryCode, String contact_email, boolean isSelected) {
        super();
        this.contact_id = contact_id;
        Contact_Name = contact_Name;
        Contact_Mobile = contact_Mobile;
        this.countryCode = countryCode;
        this.isSelected = isSelected;
        this.contact_email = contact_email;
    }

    @Override
    public String toString() {
        return "ContactInfo [contact_id=" + contact_id + ", Contact_Name="
                + Contact_Name + ", Contact_Mobile=" + Contact_Mobile
                + ", countryCode=" + countryCode + ", Contact_DOB="
                + Contact_DOB + ", Contact_Anniversary=" + Contact_Anniversary + ", Email_Id=" + contact_email
                + ", isSelected=" + isSelected + "]";
    }


}
