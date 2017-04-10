
package com.kitever.pos.fragment.purchaseList.Purchase;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class PurchasedItemsList {

    @SerializedName("PurchasedItems")
    private ArrayList<PurchasedItem> mPurchasedItems;

    public PurchasedItemsList(ArrayList<PurchasedItem> mPurchasedItems) {
        this.mPurchasedItems = mPurchasedItems;
    }

    public ArrayList<PurchasedItem> getPurchasedItems() {
        return mPurchasedItems;
    }

    public void setPurchasedItems(ArrayList<PurchasedItem> PurchasedItems) {
        mPurchasedItems = PurchasedItems;
    }

}
