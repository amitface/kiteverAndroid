
package com.kitever.pos.fragment.purchaseList.Purchase;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class PurchasedItem {

    @SerializedName("ProductID")
    private String mProductID;
    @SerializedName("ProductName")
    private String mProductName;
    @SerializedName("Quantity")
    private String mQuantity;
    @SerializedName("UnitPrice")
    private String mUnitPrice;

    public PurchasedItem(String mProductID, String mProductName, String mQuantity, String mUnitPrice) {
        this.mProductID = mProductID;
        this.mProductName = mProductName;
        this.mQuantity = mQuantity;
        this.mUnitPrice = mUnitPrice;
    }

    public String getProductID() {
        return mProductID;
    }

    public void setProductID(String ProductID) {
        mProductID = ProductID;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String ProductName) {
        mProductName = ProductName;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String Quantity) {
        mQuantity = Quantity;
    }

    public String getUnitPrice() {
        return mUnitPrice;
    }

    public void setUnitPrice(String UnitPrice) {
        mUnitPrice = UnitPrice;
    }

}
