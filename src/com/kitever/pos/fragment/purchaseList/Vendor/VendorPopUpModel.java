
package com.kitever.pos.fragment.purchaseList.Vendor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class VendorPopUpModel {

    @SerializedName("GetPurchaseDetailAsVendor")
    private ArrayList<GetPurchaseDetailAsVendor> mGetPurchaseDetailAsVendor;

    public ArrayList<GetPurchaseDetailAsVendor> getGetPurchaseDetailAsVendor() {
        return mGetPurchaseDetailAsVendor;
    }

    public void setGetPurchaseDetailAsVendor(ArrayList<GetPurchaseDetailAsVendor> GetPurchaseDetailAsVendor) {
        mGetPurchaseDetailAsVendor = GetPurchaseDetailAsVendor;
    }
}
