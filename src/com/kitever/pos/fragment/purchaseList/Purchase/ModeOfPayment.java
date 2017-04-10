
package com.kitever.pos.fragment.purchaseList.Purchase;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class ModeOfPayment {

    @SerializedName("FetchBrand")
    private ArrayList<FetchBrand> mFetchBrand;

    public ArrayList<com.kitever.pos.fragment.purchaseList.Purchase.FetchBrand> getFetchBrand() {
        return mFetchBrand;
    }

    public void setFetchBrand(ArrayList<com.kitever.pos.fragment.purchaseList.Purchase.FetchBrand> FetchBrand) {
        mFetchBrand = FetchBrand;
    }
}
