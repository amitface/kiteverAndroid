
package sms19.listview.newproject.MerchatStorePackage.Model.ModelBuyCart;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Product {

    @SerializedName("Userid")
    private Long mUserId;
    @SerializedName("AvailableStock")
    private Long mAvailableStock;
    @SerializedName("BrandName")
    private String mBrandName;
    @SerializedName("CategoryName")
    private String mCategoryName;
    @SerializedName("PerUnitPrice")
    private Double mPerUnitPrice;
    @SerializedName("ProductDescription")
    private String mProductDescription;
    @SerializedName("ProductImage")
    private String mProductImage;
    @SerializedName("ProductName")
    private String mProductName;
    @SerializedName("Quantity")
    private Long mQuantity;
    @SerializedName("Status")
    private String mStatus;
    @SerializedName("ProductID")
    private String mProductID;
    @SerializedName("tempQuantity")
    private Long mTempQuantity;


    public Product(Product product) {
        this.mUserId = product.mUserId;
        this.mAvailableStock = product.mAvailableStock;
        this.mBrandName = product.mBrandName;
        this.mCategoryName = product.mCategoryName;
        this.mPerUnitPrice = product.mPerUnitPrice;
        this.mProductDescription = product.mProductDescription;
        this.mProductImage = product. mProductImage;
        this.mProductName = product.mProductName;
        this.mQuantity = product.mQuantity;
        this.mStatus = product.mStatus;
        this.mProductID = product.mProductID;
        this.mTempQuantity = product.mTempQuantity;
    }

    public Long getmUserId() {
        return mUserId;
    }

    public void setmUserId(Long mUserId) {
        this.mUserId = mUserId;
    }

    public String getmProductID() {
        return mProductID;
    }

    public void setmProductID(String mProductID) {
        this.mProductID = mProductID;
    }

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

    public Double getPerUnitPrice() {

        return mPerUnitPrice;
    }

    public void setPerUnitPrice(Double PerUnitPrice) {
        mPerUnitPrice = PerUnitPrice;
    }

    public String getProductDescription() {
        return mProductDescription;
    }

    public void setProductDescription(String ProductDescription) {
        mProductDescription = ProductDescription;
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

    public Long getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Long Quantity) {
        mQuantity = Quantity;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String Status) {
        mStatus = Status;
    }

    public Long getmTempQuantity() {
        return mTempQuantity;
    }

    public void setmTempQuantity(Long mTempQuantity) {
        this.mTempQuantity = mTempQuantity;
    }
}
