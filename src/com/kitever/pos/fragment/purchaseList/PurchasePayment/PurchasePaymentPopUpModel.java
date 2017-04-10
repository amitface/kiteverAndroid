
package com.kitever.pos.fragment.purchaseList.PurchasePayment;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class PurchasePaymentPopUpModel {

    @SerializedName("GetPaymentDetailsPopup")
    private ArrayList<GetPaymentDetailsPopup> mGetPaymentDetailsPopup;

    public ArrayList<com.kitever.pos.fragment.purchaseList.PurchasePayment.GetPaymentDetailsPopup> getGetPaymentDetailsPopup() {
        return mGetPaymentDetailsPopup;
    }

    public void setGetPaymentDetailsPopup(ArrayList<com.kitever.pos.fragment.purchaseList.PurchasePayment.GetPaymentDetailsPopup> GetPaymentDetailsPopup) {
        mGetPaymentDetailsPopup = GetPaymentDetailsPopup;
    }

}
