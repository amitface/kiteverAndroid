
package com.kitever.pos.fragment.purchaseList.Inventory;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class InventoryHistoryModel {

    @SerializedName("GetInventoryTransactionHistory")
    private ArrayList<GetInventoryTransactionHistory> mGetInventoryTransactionHistory;

    public ArrayList<com.kitever.pos.fragment.purchaseList.Inventory.GetInventoryTransactionHistory> getGetInventoryTransactionHistory() {
        return mGetInventoryTransactionHistory;
    }

    public void setGetInventoryTransactionHistory(ArrayList<com.kitever.pos.fragment.purchaseList.Inventory.GetInventoryTransactionHistory> GetInventoryTransactionHistory) {
        mGetInventoryTransactionHistory = GetInventoryTransactionHistory;
    }

}
