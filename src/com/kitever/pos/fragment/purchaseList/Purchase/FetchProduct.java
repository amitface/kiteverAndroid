
package com.kitever.pos.fragment.purchaseList.Purchase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class FetchProduct implements Parcelable {


    @SerializedName("BrandName")
    private String mBrandName;
    @SerializedName("CUG")
    private String mCUG;
    @SerializedName("CategoryName")
    private String mCategoryName;
    @SerializedName("IsActive")
    private String mIsActive;
    @SerializedName("PerUnitPrice")
    private Double mPerUnitPrice;
    @SerializedName("PriceWithTax")
    private Double mPriceWithTax;
    @SerializedName("ProductID")
    private Long mProductID;
    @SerializedName("ProductImage")
    private String mProductImage;
    @SerializedName("ProductName")
    private String mProductName;
    @SerializedName("TaxApplied")
    private String mTaxApplied;
    @SerializedName("Units")
    private String mUnits;
    @SerializedName("UserID")
    private Long mUserID;
    @SerializedName("Quantity")
    private String mQuantity;
    @SerializedName("UnitPrice")
    private String mUnitPrice;

    public FetchProduct(FetchProduct fetchProduct) {
        this.mBrandName = fetchProduct.getBrandName();
        this.mCUG = fetchProduct.getCUG();
        this.mCategoryName = fetchProduct.getCategoryName();
        this.mIsActive = fetchProduct.getIsActive();
        this.mPerUnitPrice = fetchProduct.getPerUnitPrice();
        this.mPriceWithTax = fetchProduct.getPriceWithTax();
        this.mProductID = fetchProduct.getProductID();
        this.mProductImage = fetchProduct.getProductImage();
        this.mProductName = fetchProduct.getProductName();
        this.mTaxApplied = fetchProduct.getTaxApplied();
        this.mUnits = fetchProduct.getUnits();
        this.mUserID = fetchProduct.getUserID();
        this.mQuantity = fetchProduct.getmQuantity();
        this.mUnitPrice = fetchProduct.getmUnitPrice();
    }

    public String getBrandName() {
        return mBrandName;
    }

    public void setBrandName(String BrandName) {
        mBrandName = BrandName;
    }

    public String getCUG() {
        return mCUG;
    }

    public void setCUG(String CUG) {
        mCUG = CUG;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String CategoryName) {
        mCategoryName = CategoryName;
    }

    public String getIsActive() {
        return mIsActive;
    }

    public void setIsActive(String IsActive) {
        mIsActive = IsActive;
    }

    public Double getPerUnitPrice() {
        return mPerUnitPrice;
    }

    public void setPerUnitPrice(Double PerUnitPrice) {
        mPerUnitPrice = PerUnitPrice;
    }

    public Double getPriceWithTax() {
        return mPriceWithTax;
    }

    public void setPriceWithTax(Double PriceWithTax) {
        mPriceWithTax = PriceWithTax;
    }

    public Long getProductID() {
        return mProductID;
    }

    public void setProductID(Long ProductID) {
        mProductID = ProductID;
    }

    public String getProductImage() {
        return mProductImage;
    }

    public void setProductImage(String ProductImage) {
        mProductImage = ProductImage;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String ProductName) {
        mProductName = ProductName;
    }

    public String getTaxApplied() {
        return mTaxApplied;
    }

    public void setTaxApplied(String TaxApplied) {
        mTaxApplied = TaxApplied;
    }

    public String getUnits() {
        return mUnits;
    }

    public void setUnits(String Units) {
        mUnits = Units;
    }

    public Long getUserID() {
        return mUserID;
    }

    public void setUserID(Long UserID) {
        mUserID = UserID;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(String mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getmUnitPrice() {
        return mUnitPrice;
    }

    public void setmUnitPrice(String mUnitPrice) {
        this.mUnitPrice = mUnitPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mBrandName);
        dest.writeString(this.mCUG);
        dest.writeString(this.mCategoryName);
        dest.writeString(this.mIsActive);
        dest.writeValue(this.mPerUnitPrice);
        dest.writeValue(this.mPriceWithTax);
        dest.writeValue(this.mProductID);
        dest.writeString(this.mProductImage);
        dest.writeString(this.mProductName);
        dest.writeString(this.mTaxApplied);
        dest.writeString(this.mUnits);
        dest.writeValue(this.mUserID);
        dest.writeString(this.mQuantity);
        dest.writeString(this.mUnitPrice);
    }

    protected FetchProduct(Parcel in) {
        this.mBrandName = in.readString();
        this.mCUG = in.readString();
        this.mCategoryName = in.readString();
        this.mIsActive = in.readString();
        this.mPerUnitPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.mPriceWithTax = (Double) in.readValue(Double.class.getClassLoader());
        this.mProductID = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductImage = in.readString();
        this.mProductName = in.readString();
        this.mTaxApplied = in.readString();
        this.mUnits = in.readString();
        this.mUserID = (Long) in.readValue(Long.class.getClassLoader());
        this.mQuantity = in.readString();
        this.mUnitPrice = in.readString();
    }

    public static final Parcelable.Creator<FetchProduct> CREATOR = new Parcelable.Creator<FetchProduct>() {
        @Override
        public FetchProduct createFromParcel(Parcel source) {
            return new FetchProduct(source);
        }

        @Override
        public FetchProduct[] newArray(int size) {
            return new FetchProduct[size];
        }
    };
}
