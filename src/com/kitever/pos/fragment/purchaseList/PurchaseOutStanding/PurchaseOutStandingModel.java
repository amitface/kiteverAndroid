
package com.kitever.pos.fragment.purchaseList.PurchaseOutStanding;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class PurchaseOutStandingModel {

    @SerializedName("GetOutstandingPurchases")
    private ArrayList<GetOutstandingPurchase> mGetOutstandingPurchases;

    public ArrayList<GetOutstandingPurchase> getGetOutstandingPurchases() {
        return mGetOutstandingPurchases;
    }

    public void setGetOutstandingPurchases(ArrayList<GetOutstandingPurchase> GetOutstandingPurchases) {
        mGetOutstandingPurchases = GetOutstandingPurchases;
    }

}
