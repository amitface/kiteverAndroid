
package com.kitever.pos.fragment.purchaseList.Inventory;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GetCurrentInventory {

    @SerializedName("balance")
    private Long mBalance;
    @SerializedName("productid")
    private Long mProductid;
    @SerializedName("userid")
    private Long mUserid;
    @SerializedName("ProductName")
    private String mProductName;

    public Long getBalance() {
        return mBalance;
    }

    public void setBalance(Long balance) {
        mBalance = balance;
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

    public String getmProductName() {
        return mProductName;
    }

    public void setmProductName(String mProductName) {
        this.mProductName = mProductName;
    }
}
