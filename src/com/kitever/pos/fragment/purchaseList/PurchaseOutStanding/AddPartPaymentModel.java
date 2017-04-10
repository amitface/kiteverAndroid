
package com.kitever.pos.fragment.purchaseList.PurchaseOutStanding;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class AddPartPaymentModel {

    @SerializedName("AddPaymentToPurchase")
    private List<com.kitever.pos.fragment.purchaseList.PurchaseOutStanding.AddPaymentToPurchase> mAddPaymentToPurchase;

    public List<com.kitever.pos.fragment.purchaseList.PurchaseOutStanding.AddPaymentToPurchase> getAddPaymentToPurchase() {
        return mAddPaymentToPurchase;
    }

    public void setAddPaymentToPurchase(List<com.kitever.pos.fragment.purchaseList.PurchaseOutStanding.AddPaymentToPurchase> AddPaymentToPurchase) {
        mAddPaymentToPurchase = AddPaymentToPurchase;
    }

}
