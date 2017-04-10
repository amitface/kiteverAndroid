
package com.kitever.pos.fragment.purchaseList.Inventory;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class UpdateCurrentInventory {

    @SerializedName("AddInventory")
    private List<com.kitever.pos.fragment.purchaseList.Inventory.AddInventory> mAddInventory;

    public List<com.kitever.pos.fragment.purchaseList.Inventory.AddInventory> getAddInventory() {
        return mAddInventory;
    }

    public void setAddInventory(List<com.kitever.pos.fragment.purchaseList.Inventory.AddInventory> AddInventory) {
        mAddInventory = AddInventory;
    }

}
