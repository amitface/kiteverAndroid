
package com.kitever.pos.fragment.purchaseList.Purchase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class AddPurchase implements Parcelable {

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
    private Double mDateAdded;
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
    private Double mPurchaseDate;
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

    public Double getDateAdded() {
        return mDateAdded;
    }

    public void setDateAdded(Double DateAdded) {
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

    public Double getPurchaseDate() {
        return mPurchaseDate;
    }

    public void setPurchaseDate(Double PurchaseDate) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mBalance);
        dest.writeValue(this.mBaseAmount);
        dest.writeValue(this.mBillAmount);
        dest.writeValue(this.mBiller);
        dest.writeValue(this.mContactID);
        dest.writeValue(this.mDateAdded);
        dest.writeValue(this.mID);
        dest.writeString(this.mInvoiceNO);
        dest.writeValue(this.mIsTaxIncluded);
        dest.writeString(this.mPONO);
        dest.writeValue(this.mPaidAmount);
        dest.writeValue(this.mPurchaseDate);
        dest.writeValue(this.mTaxAmount);
        dest.writeString(this.mTaxApplied);
        dest.writeValue(this.mUserID);
    }

    public AddPurchase() {
    }

    protected AddPurchase(Parcel in) {
        this.mBalance = (Double) in.readValue(Double.class.getClassLoader());
        this.mBaseAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.mBillAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.mBiller = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mContactID = (Long) in.readValue(Long.class.getClassLoader());
        this.mDateAdded = (Double) in.readValue(Double.class.getClassLoader());
        this.mID = (Long) in.readValue(Long.class.getClassLoader());
        this.mInvoiceNO = in.readString();
        this.mIsTaxIncluded = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mPONO = in.readString();
        this.mPaidAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.mPurchaseDate = (Double) in.readValue(Double.class.getClassLoader());
        this.mTaxAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.mTaxApplied = in.readString();
        this.mUserID = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<AddPurchase> CREATOR = new Parcelable.Creator<AddPurchase>() {
        @Override
        public AddPurchase createFromParcel(Parcel source) {
            return new AddPurchase(source);
        }

        @Override
        public AddPurchase[] newArray(int size) {
            return new AddPurchase[size];
        }
    };
}
