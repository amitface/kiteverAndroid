
package sms19.listview.newproject.MerchatStorePackage.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Product implements Parcelable {

    @SerializedName("AvailableStock")
    private Long mAvailableStock;
    @SerializedName("BrandName")
    private String mBrandName;
    @SerializedName("CategoryName")
    private String mCategoryName;
    @SerializedName("Description")
    private String mDescription;
    @SerializedName("Featured")
    private String mFeatured;
    @SerializedName("IsActive")
    private String mIsActive;
    @SerializedName("PerUnitPrice")
    private String mPerUnitPrice;
    @SerializedName("PriceWithTax")
    private Long mPriceWithTax;
    @SerializedName("ProductID")
    private Long mProductID;
    @SerializedName("ProductImage")
    private String mProductImage;
    @SerializedName("ProductName")
    private String mProductName;
    @SerializedName("Published")
    private String mPublished;
    @SerializedName("ReturnRemarks")
    private String mReturnRemarks;
    @SerializedName("ReturnsAccepted")
    private String mReturnsAccepted;
    @SerializedName("ReturnsWithin")
    private String mReturnsWithin;
    @SerializedName("TaxApplied")
    private String mTaxApplied;
    @SerializedName("Unit")
    private String mUnit;
    @SerializedName(("Quantity"))
    private String mQunatity;


    public Long getAvailableStock() {
        return mAvailableStock;
    }

    public void setAvailableStock(Long AvailableStock) {
        mAvailableStock = AvailableStock;
    }

    public String getBrandName() {
        return mBrandName;
    }

    public void setBrandName(String BrandName) {
        mBrandName = BrandName;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String CategoryName) {
        mCategoryName = CategoryName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String Description) {
        mDescription = Description;
    }

    public String getFeatured() {
        return mFeatured;
    }

    public void setFeatured(String Featured) {
        mFeatured = Featured;
    }

    public String getIsActive() {
        return mIsActive;
    }

    public void setIsActive(String IsActive) {
        mIsActive = IsActive;
    }

    public String getPerUnitPrice() {
        return mPerUnitPrice;
    }

    public void setPerUnitPrice(String PerUnitPrice) {
        mPerUnitPrice = PerUnitPrice;
    }

    public Long getPriceWithTax() {
        return mPriceWithTax;
    }

    public void setPriceWithTax(Long PriceWithTax) {
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

    public String getPublished() {
        return mPublished;
    }

    public void setPublished(String Published) {
        mPublished = Published;
    }

    public String getReturnRemarks() {
        return mReturnRemarks;
    }

    public void setReturnRemarks(String ReturnRemarks) {
        mReturnRemarks = ReturnRemarks;
    }

    public String getReturnsAccepted() {
        return mReturnsAccepted;
    }

    public void setReturnsAccepted(String ReturnsAccepted) {
        mReturnsAccepted = ReturnsAccepted;
    }

    public String getReturnsWithin() {
        return mReturnsWithin;
    }

    public void setReturnsWithin(String ReturnsWithin) {
        mReturnsWithin = ReturnsWithin;
    }

    public String getTaxApplied() {
        return mTaxApplied;
    }

    public void setTaxApplied(String TaxApplied) {
        mTaxApplied = TaxApplied;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String Unit) {
        mUnit = Unit;
    }

    public String getmQunatity() {
        return mQunatity;
    }

    public void setmQunatity(String mQunatity) {
        this.mQunatity = mQunatity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mAvailableStock);
        dest.writeString(this.mBrandName);
        dest.writeString(this.mCategoryName);
        dest.writeString(this.mDescription);
        dest.writeString(this.mFeatured);
        dest.writeString(this.mIsActive);
        dest.writeString(this.mPerUnitPrice);
        dest.writeValue(this.mPriceWithTax);
        dest.writeValue(this.mProductID);
        dest.writeString(this.mProductImage);
        dest.writeString(this.mProductName);
        dest.writeString(this.mPublished);
        dest.writeString(this.mReturnRemarks);
        dest.writeString(this.mReturnsAccepted);
        dest.writeString(this.mReturnsWithin);
        dest.writeString(this.mTaxApplied);
        dest.writeString(this.mUnit);
        dest.writeString(this.mQunatity);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.mAvailableStock = (Long) in.readValue(Long.class.getClassLoader());
        this.mBrandName = in.readString();
        this.mCategoryName = in.readString();
        this.mDescription = in.readString();
        this.mFeatured = in.readString();
        this.mIsActive = in.readString();
        this.mPerUnitPrice = in.readString();
        this.mPriceWithTax = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductID = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductImage = in.readString();
        this.mProductName = in.readString();
        this.mPublished = in.readString();
        this.mReturnRemarks = in.readString();
        this.mReturnsAccepted = in.readString();
        this.mReturnsWithin = in.readString();
        this.mTaxApplied = in.readString();
        this.mUnit = in.readString();
        this.mQunatity = in.readString();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
