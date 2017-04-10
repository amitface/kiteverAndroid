
package com.kitever.pos.fragment.purchaseList;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class PurchaseListPopUpModel {

    @SerializedName("GetPurchaseDetails")
    private ArrayList<GetPurchaseDetail> mGetPurchaseDetails;

    public ArrayList<GetPurchaseDetail> getGetPurchaseDetails() {
        return mGetPurchaseDetails;
    }

    public void setGetPurchaseDetails(ArrayList<GetPurchaseDetail> GetPurchaseDetails) {
        mGetPurchaseDetails = GetPurchaseDetails;
    }

}
