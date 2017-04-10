package com.kitever.pos.model;


public class CustomerList {

    private String Contact_ID;

    private String TotalCredit;

    private String ImageURL;

    private String TotalOrder;

    private long LastOrderDate;

    private String TotalAmount;

    private String Contact_Name;

    private String TotalPayment;

    private String Combined;

    public String getCombined() {
        return Combined;
    }

    public void setCombined(String combined) {
        Combined = combined;
    }

    public String getTotalCredit() {
        return TotalCredit;
    }

    public void setTotalCredit(String TotalCredit) {
        this.TotalCredit = TotalCredit;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String ImageURL) {
        this.ImageURL = ImageURL;
    }

    public String getTotalOrder() {
        return TotalOrder;
    }

    public void setTotalOrder(String TotalOrder) {
        this.TotalOrder = TotalOrder;
    }

    public Long getLastOrderDate() {
        return LastOrderDate;
    }

    public void setLastOrderDate(Long LastOrderDate) {
        this.LastOrderDate = LastOrderDate;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public String getContact_Name() {
        return Contact_Name;
    }

    public void setContact_Name(String Contact_Name) {
        this.Contact_Name = Contact_Name;
    }

    public String getTotalPayment() {
        return TotalPayment;
    }

    public void setTotalPayment(String TotalPayment) {
        this.TotalPayment = TotalPayment;
    }

    public String getContact_ID() {
        return Contact_ID;
    }

    public void setContact_ID(String contact_ID) {
        Contact_ID = contact_ID;
    }

    @Override
    public String toString() {
        return "ClassPojo [TotalCredit = " + TotalCredit + ", ImageURL = " + ImageURL + ", TotalOrder = " + TotalOrder + ", LastOrderDate = " + LastOrderDate + ", TotalAmount = " + TotalAmount + ", Contact_Name = " + Contact_Name + ", TotalPayment = " + TotalPayment + "]";
    }
}