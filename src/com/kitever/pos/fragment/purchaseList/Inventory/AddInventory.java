
package com.kitever.pos.fragment.purchaseList.Inventory;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class AddInventory {

    @SerializedName("balance")
    private String mBalance;
    @SerializedName("ProductName")
    private String mProductName;
    @SerializedName("productid")
    private Long mProductid;
    @SerializedName("userid")
    private Long mUserid;

    public String getBalance() {
        return mBalance;
    }

    public void setBalance(String balance) {
        mBalance = balance;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String ProductName) {
        mProductName = ProductName;
    }

    public Long getProductid() {
        return mProductid;
    }

    public void setProductid(Long productid) {
        mProductid = productid;
    }

    public Long getUserid() {
        return mUserid;
    }

    public void setUserid(Long userid) {
        mUserid = userid;
    }

}
