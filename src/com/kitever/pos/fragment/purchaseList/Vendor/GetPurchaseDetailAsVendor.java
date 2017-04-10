
package com.kitever.pos.fragment.purchaseList.Vendor;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GetPurchaseDetailAsVendor {

    @SerializedName("Balance")
    private Double mBalance;
    @SerializedName("BaseAmount")
    private Double mBaseAmount;
    @SerializedName("BillAmount")
    private Double mBillAmount;
    @SerializedName("Biller")
    private Boolean mBiller;
    @SerializedName("ContactID")
    private Long mContactID;
    @SerializedName("DateAdded")
    private String mDateAdded;
    @SerializedName("ID")
    private Long mID;
    @SerializedName("InvoiceNO")
    private String mInvoiceNO;
    @SerializedName("IsTaxIncluded")
    private Boolean mIsTaxIncluded;
    @SerializedName("PO_NO")
    private String mPONO;
    @SerializedName("PaidAmount")
    private Double mPaidAmount;
    @SerializedName("PurchaseDate")
    private String mPurchaseDate;
    @SerializedName("TaxAmount")
    private Double mTaxAmount;
    @SerializedName("TaxApplied")
    private String mTaxApplied;
    @SerializedName("UserID")
    private Long mUserID;

    public Double getBalance() {
        return mBalance;
    }

    public void setBalance(Double Balance) {
        mBalance = Balance;
    }

    public Double getBaseAmount() {
        return mBaseAmount;
    }

    public void setBaseAmount(Double BaseAmount) {
        mBaseAmount = BaseAmount;
    }

    public Double getBillAmount() {
        return mBillAmount;
    }

    public void setBillAmount(Double BillAmount) {
        mBillAmount = BillAmount;
    }

    public Boolean getBiller() {
        return mBiller;
    }

    public void setBiller(Boolean Biller) {
        mBiller = Biller;
    }

    public Long getContactID() {
        return mContactID;
    }

    public void setContactID(Long ContactID) {
        mContactID = ContactID;
    }

    public String getDateAdded() {
        return mDateAdded;
    }

    public void setDateAdded(String DateAdded) {
        mDateAdded = DateAdded;
    }

    public Long getID() {
        return mID;
    }

    public void setID(Long ID) {
        mID = ID;
    }

    public String getInvoiceNO() {
        return mInvoiceNO;
    }

    public void setInvoiceNO(String InvoiceNO) {
        mInvoiceNO = InvoiceNO;
    }

    public Boolean getIsTaxIncluded() {
        return mIsTaxIncluded;
    }

    public void setIsTaxIncluded(Boolean IsTaxIncluded) {
        mIsTaxIncluded = IsTaxIncluded;
    }

    public String getPONO() {
        return mPONO;
    }

    public void setPONO(String PONO) {
        mPONO = PONO;
    }

    public Double getPaidAmount() {
        return mPaidAmount;
    }

    public void setPaidAmount(Double PaidAmount) {
        mPaidAmount = PaidAmount;
    }

    public String getPurchaseDate() {
        return mPurchaseDate;
    }

    public void setPurchaseDate(String PurchaseDate) {
        mPurchaseDate = PurchaseDate;
    }

    public Double getTaxAmount() {
        return mTaxAmount;
    }

    public void setTaxAmount(Double TaxAmount) {
        mTaxAmount = TaxAmount;
    }

    public String getTaxApplied() {
        return mTaxApplied;
    }

    public void setTaxApplied(String TaxApplied) {
        mTaxApplied = TaxApplied;
    }

    public Long getUserID() {
        return mUserID;
    }

    public void setUserID(Long UserID) {
        mUserID = UserID;
    }

}
