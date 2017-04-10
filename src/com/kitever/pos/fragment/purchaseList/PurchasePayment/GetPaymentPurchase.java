
package com.kitever.pos.fragment.purchaseList.PurchasePayment;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GetPaymentPurchase {

    @SerializedName("Bank")
    private String mBank;
    @SerializedName("BillAmount")
    private Double mBillAmount = 0.0D;
    @SerializedName("ChequeDate")
    private Long mChequeDate;
    @SerializedName("ChequeNo")
    private String mChequeNo;
    @SerializedName("ID")
    private Long mID;
    @SerializedName("PaidAmount")
    private Double mPaidAmount;
    @SerializedName("PayMode")
    private String mPayMode;
    @SerializedName("PaymentDate")
    private Long mPaymentDate;
    @SerializedName("PurchaseID")
    private Long mPurchaseID;
    @SerializedName("ReceiptNo")
    private String mReceiptNo;
    @SerializedName("Remarks")
    private String mRemarks;
    @SerializedName("UserID")
    private Long mUserID;
    @SerializedName("PO_NO")
    private String mPoNo;
    @SerializedName("InvoiceNO")
    private String mInvoiceNo;
    @SerializedName("contact_name")
    private String mContactName;

    public String getBank() {
        return mBank;
    }

    public void setBank(String Bank) {
        mBank = Bank;
    }

    public Double getBillAmount() {
        if(mBillAmount==null)
            mBillAmount=0.0D;
        return mBillAmount;
    }

    public void setBillAmount(Double BillAmount) {
        if(BillAmount.toString().equals("null"))return;
        mBillAmount = BillAmount;
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

    public Double getPaidAmount() {
        return mPaidAmount;
    }

    public void setPaidAmount(Double PaidAmount) {
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

    public String getReceiptNo() {
        return mReceiptNo;
    }

    public void setReceiptNo(String ReceiptNo) {
        mReceiptNo = ReceiptNo;
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

    public String getmContactName() {
        return mContactName;
    }

    public void setmContactName(String mContactName) {
        this.mContactName = mContactName;
    }

    public String getmInvoiceNo() {
        return mInvoiceNo;
    }

    public void setmInvoiceNo(String mInvoiceNo) {
        this.mInvoiceNo = mInvoiceNo;
    }

    public String getmPoNo() {
        return mPoNo;
    }

    public void setmPoNo(String mPoNo) {
        this.mPoNo = mPoNo;
    }
}
