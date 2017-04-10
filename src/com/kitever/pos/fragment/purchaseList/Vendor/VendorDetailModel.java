
package com.kitever.pos.fragment.purchaseList.Vendor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class VendorDetailModel {

    @SerializedName("GetVendorDetail")
    private ArrayList<GetVendorDetail> mGetVendorDetail;

    public ArrayList<com.kitever.pos.fragment.purchaseList.Vendor.GetVendorDetail> getGetVendorDetail() {
        return mGetVendorDetail;
    }

    public void setGetVendorDetail(ArrayList<com.kitever.pos.fragment.purchaseList.Vendor.GetVendorDetail> GetVendorDetail) {
        mGetVendorDetail = GetVendorDetail;
    }

}
