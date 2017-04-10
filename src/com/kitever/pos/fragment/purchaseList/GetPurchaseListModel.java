
package com.kitever.pos.fragment.purchaseList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class GetPurchaseListModel implements Parcelable {

    @SerializedName("GetPurchaseMaster")
    private ArrayList<GetPurchaseMaster> mGetPurchaseMaster;

    public ArrayList<com.kitever.pos.fragment.purchaseList.GetPurchaseMaster> getGetPurchaseMaster() {
        return mGetPurchaseMaster;
    }

    public void setGetPurchaseMaster(ArrayList<com.kitever.pos.fragment.purchaseList.GetPurchaseMaster> GetPurchaseMaster) {
        mGetPurchaseMaster = GetPurchaseMaster;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mGetPurchaseMaster);
    }

    public GetPurchaseListModel() {
    }

    protected GetPurchaseListModel(Parcel in) {
        this.mGetPurchaseMaster = in.createTypedArrayList(GetPurchaseMaster.CREATOR);
    }

    public static final Parcelable.Creator<GetPurchaseListModel> CREATOR = new Parcelable.Creator<GetPurchaseListModel>() {
        @Override
        public GetPurchaseListModel createFromParcel(Parcel source) {
            return new GetPurchaseListModel(source);
        }

        @Override
        public GetPurchaseListModel[] newArray(int size) {
            return new GetPurchaseListModel[size];
        }
    };
}
