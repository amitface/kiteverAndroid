
package com.kitever.pos.fragment.purchaseList.Vendor;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GetVendorDetail {

    @SerializedName("Combined")
    private String mCombined;
    @SerializedName("Contact_Email")
    private String mContactEmail;
    @SerializedName("Contact_ID")
    private Long mContactID;
    @SerializedName("Contact_Mobile")
    private String mContactMobile;
    @SerializedName("Contact_Name")
    private String mContactName;
    @SerializedName("ImageURL")
    private Object mImageURL;
    @SerializedName("TotalAmount")
    private Double mTotalAmount;
    @SerializedName("TotalCredit")
    private Double mTotalCredit;
    @SerializedName("TotalPayment")
    private Double mTotalPayment;
    @SerializedName("TotalPurchases")
    private Long mTotalPurchases;

    public String getCombined() {
        return mCombined;
    }

    public void setCombined(String Combined) {
        mCombined = Combined;
    }

    public String getContactEmail() {
        return mContactEmail;
    }

    public void setContactEmail(String ContactEmail) {
        mContactEmail = ContactEmail;
    }

    public Long getContactID() {
        return mContactID;
    }

    public void setContactID(Long ContactID) {
        mContactID = ContactID;
    }

    public String getContactMobile() {
        return mContactMobile;
    }

    public void setContactMobile(String ContactMobile) {
        mContactMobile = ContactMobile;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String ContactName) {
        mContactName = ContactName;
    }

    public Object getImageURL() {
        return mImageURL;
    }

    public void setImageURL(Object ImageURL) {
        mImageURL = ImageURL;
    }

    public Double getTotalAmount() {
        return mTotalAmount;
    }

    public void setTotalAmount(Double TotalAmount) {
        mTotalAmount = TotalAmount;
    }

    public Double getTotalCredit() {
        return mTotalCredit;
    }

    public void setTotalCredit(Double TotalCredit) {
        mTotalCredit = TotalCredit;
    }

    public Double getTotalPayment() {
        return mTotalPayment;
    }

    public void setTotalPayment(Double TotalPayment) {
        mTotalPayment = TotalPayment;
    }

    public Long getTotalPurchases() {
        return mTotalPurchases;
    }

    public void setTotalPurchases(Long TotalPurchases) {
        mTotalPurchases = TotalPurchases;
    }

}
