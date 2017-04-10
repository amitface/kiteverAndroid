
package com.kitever.pos.fragment.purchaseList.PurchasePayment;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GetPaymentDetailsPopup {

    @SerializedName("Bank")
    private String mBank;
    @SerializedName("ChequeDate")
    private Long mChequeDate;
    @SerializedName("ChequeNo")
    private String mChequeNo;
    @SerializedName("ID")
    private Long mID;
    @SerializedName("PaidAmount")
    private Long mPaidAmount;
    @SerializedName("PayMode")
    private String mPayMode;
    @SerializedName("PaymentDate")
    private Long mPaymentDate;
    @SerializedName("PurchaseID")
    private Long mPurchaseID;
    @SerializedName("Remarks")
    private String mRemarks;
    @SerializedName("UserID")
    private Long mUserID;

    public String getBank() {
        return mBank;
    }

    public void setBank(String Bank) {
        mBank = Bank;
    }

    public Long getChequeDate() {
        return mChequeDate;
    }

    public void setChequeDate(Long ChequeDate) {
        mChequeDate = ChequeDate;
    }

    public String getChequeNo() {
        return mChequeNo;
    }

    public void setChequeNo(String ChequeNo) {
        mChequeNo = ChequeNo;
    }

    public Long getID() {
        return mID;
    }

    public void setID(Long ID) {
        mID = ID;
    }

    public Long getPaidAmount() {
        return mPaidAmount;
    }

    public void setPaidAmount(Long PaidAmount) {
        mPaidAmount = PaidAmount;
    }

    public String getPayMode() {
        return mPayMode;
    }

    public void setPayMode(String PayMode) {
        mPayMode = PayMode;
    }

    public Long getPaymentDate() {
        return mPaymentDate;
    }

    public void setPaymentDate(Long PaymentDate) {
        mPaymentDate = PaymentDate;
    }

    public Long getPurchaseID() {
        return mPurchaseID;
    }

    public void setPurchaseID(Long PurchaseID) {
        mPurchaseID = PurchaseID;
    }

    public String getRemarks() {
        return mRemarks;
    }

    public void setRemarks(String Remarks) {
        mRemarks = Remarks;
    }

    public Long getUserID() {
        return mUserID;
    }

    public void setUserID(Long UserID) {
        mUserID = UserID;
    }

}
