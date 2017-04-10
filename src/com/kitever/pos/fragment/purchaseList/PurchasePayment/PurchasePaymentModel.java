
package com.kitever.pos.fragment.purchaseList.PurchasePayment;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class PurchasePaymentModel {

    @SerializedName("GetPaymentPurchase")
    private List<com.kitever.pos.fragment.purchaseList.PurchasePayment.GetPaymentPurchase> mGetPaymentPurchase;

    public List<com.kitever.pos.fragment.purchaseList.PurchasePayment.GetPaymentPurchase> getGetPaymentPurchase() {
        return mGetPaymentPurchase;
    }

    public void setGetPaymentPurchase(List<com.kitever.pos.fragment.purchaseList.PurchasePayment.GetPaymentPurchase> GetPaymentPurchase) {
        mGetPaymentPurchase = GetPaymentPurchase;
    }

}
