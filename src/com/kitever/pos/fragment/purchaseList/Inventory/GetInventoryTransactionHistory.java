
package com.kitever.pos.fragment.purchaseList.Inventory;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GetInventoryTransactionHistory {

    @SerializedName("Added")
    private Long mAdded;
    @SerializedName("Balance")
    private Long mBalance;
    @SerializedName("DateAdded")
    private String mDateAdded;
    @SerializedName("ID")
    private Long mID;
    @SerializedName("Previous")
    private Long mPrevious;
    @SerializedName("ProductID")
    private Long mProductID;
    @SerializedName("Remarks")
    private String mRemarks;
    @SerializedName("Type")
    private String mType;
    @SerializedName("UserID")
    private Long mUserID;

    public Long getAdded() {
        return mAdded;
    }

    public void setAdded(Long Added) {
        mAdded = Added;
    }

    public Long getBalance() {
        return mBalance;
    }

    public void setBalance(Long Balance) {
        mBalance = Balance;
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

    public Long getPrevious() {
        return mPrevious;
    }

    public void setPrevious(Long Previous) {
        mPrevious = Previous;
    }

    public Long getProductID() {
        return mProductID;
    }

    public void setProductID(Long ProductID) {
        mProductID = ProductID;
    }

    public String getRemarks() {
        return mRemarks;
    }

    public void setRemarks(String Remarks) {
        mRemarks = Remarks;
    }

    public String getType() {
        return mType;
    }

    public void setType(String Type) {
        mType = Type;
    }

    public Long getUserID() {
        return mUserID;
    }

    public void setUserID(Long UserID) {
        mUserID = UserID;
    }

}
