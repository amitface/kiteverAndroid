
package com.kitever.pos.fragment.purchaseList.Inventory;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class InventoryListModel {

    @SerializedName("GetCurrentInventory")
    private List<com.kitever.pos.fragment.purchaseList.Inventory.GetCurrentInventory> mGetCurrentInventory;

    public List<com.kitever.pos.fragment.purchaseList.Inventory.GetCurrentInventory> getGetCurrentInventory() {
        return mGetCurrentInventory;
    }

    public void setGetCurrentInventory(List<com.kitever.pos.fragment.purchaseList.Inventory.GetCurrentInventory> GetCurrentInventory) {
        mGetCurrentInventory = GetCurrentInventory;
    }

}
