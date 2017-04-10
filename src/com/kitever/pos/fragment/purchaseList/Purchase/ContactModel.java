
package com.kitever.pos.fragment.purchaseList.Purchase;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class ContactModel {

    @SerializedName("FetchCustomer")
    private ArrayList<FetchCustomer> mFetchCustomer;

    public ArrayList<com.kitever.pos.fragment.purchaseList.Purchase.FetchCustomer> getFetchCustomer() {
        return mFetchCustomer;
    }

    public void setFetchCustomer(ArrayList<com.kitever.pos.fragment.purchaseList.Purchase.FetchCustomer> FetchCustomer) {
        mFetchCustomer = FetchCustomer;
    }

}
