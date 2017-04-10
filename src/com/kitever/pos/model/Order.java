
package com.kitever.pos.model;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Order {

    @SerializedName("BalanceAmount")
    private String mBalanceAmount;
    @SerializedName("BillAmount")
    private String mBillAmount;
    @SerializedName("ContactID")
    private String mContactID;
    @SerializedName("FullLink")
    private String mFullLink;
    @SerializedName("OrderCode")
    private String mOrderCode;
    @SerializedName("OrderDate")
    private String mOrderDate;
    @SerializedName("PaidAmount")
    private String mPaidAmount;
    @SerializedName("UserID")
    private String mUserID;

    public String getBalanceAmount() {
        return mBalanceAmount;
    }

    public void setBalanceAmount(String BalanceAmount) {
        mBalanceAmount = BalanceAmount;
    }

    public String getBillAmount() {
        return mBillAmount;
    }

    public void setBillAmount(String BillAmount) {
        mBillAmount = BillAmount;
    }

    public String getContactID() {
        return mContactID;
    }

    public void setContactID(String ContactID) {
        mContactID = ContactID;
    }

    public String getFullLink() {
        return mFullLink;
    }

    public void setFullLink(String FullLink) {
        mFullLink = FullLink;
    }

    public String getOrderCode() {
        return mOrderCode;
    }

    public void setOrderCode(String OrderCode) {
        mOrderCode = OrderCode;
    }

    public String getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(String OrderDate) {
        mOrderDate = OrderDate;
    }

    public String getPaidAmount() {
        return mPaidAmount;
    }

    public void setPaidAmount(String PaidAmount) {
        mPaidAmount = PaidAmount;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String UserID) {
        mUserID = UserID;
    }

}
