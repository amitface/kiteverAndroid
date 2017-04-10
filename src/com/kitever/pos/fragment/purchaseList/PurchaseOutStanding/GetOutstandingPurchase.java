
package com.kitever.pos.fragment.purchaseList.PurchaseOutStanding;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GetOutstandingPurchase {

    @SerializedName("Balance")
    private Double mBalance;
    @SerializedName("BillAmount")
    private Double mBillAmount;
    @SerializedName("Contact_Email")
    private String mContactEmail;
    @SerializedName("ContactID")
    private Long mContactID;
    @SerializedName("Contact_Mobile")
    private String mContactMobile;
    @SerializedName("Contact_Name")
    private String mContactName;
    @SerializedName("ID")
    private Long mID;
    @SerializedName("MerchantId")
    private Long mMerchantId;
    @SerializedName("NUMBER")
    private Long mNUMBER;
    @SerializedName("PaidAmount")
    private Double mPaidAmount;
    @SerializedName("purchaseDate")
    private Long mPurchaseDate;
    @SerializedName("InvoiceNO")
    private String mInvoiceNo;

    @SerializedName("PO_NO")
    private String PO_NO;

    public Double getBalance() {
        return mBalance;
    }

    public void setBalance(Double Balance) {
        mBalance = Balance;
    }

    public Double getBillAmount() {
        return mBillAmount;
    }

    public void setBillAmount(Double BillAmount) {
        mBillAmount = BillAmount;
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

    public Long getID() {
        return mID;
    }

    public void setID(Long ID) {
        mID = ID;
    }

    public Long getMerchantId() {
        return mMerchantId;
    }

    public void setMerchantId(Long MerchantId) {
        mMerchantId = MerchantId;
    }

    public Long getNUMBER() {
        return mNUMBER;
    }

    public void setNUMBER(Long NUMBER) {
        mNUMBER = NUMBER;
    }

    public Double getPaidAmount() {
        return mPaidAmount;
    }

    public void setPaidAmount(Double PaidAmount) {
        mPaidAmount = PaidAmount;
    }

    public Long getPurchaseDate() {
        return mPurchaseDate;
    }

    public void setPurchaseDate(Long purchaseDate) {
        mPurchaseDate = purchaseDate;
    }

    public String getmInvoiceNo() {
        return mInvoiceNo;
    }

    public void setmInvoiceNo(String mInvoiceNo) {
        this.mInvoiceNo = mInvoiceNo;
    }

    public String getPO_NO() {
        return PO_NO;
    }

    public void setPO_NO(String PO_NO) {
        this.PO_NO = PO_NO;
    }
}
