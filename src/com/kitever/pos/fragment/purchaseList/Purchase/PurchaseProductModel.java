
package com.kitever.pos.fragment.purchaseList.Purchase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class PurchaseProductModel implements Parcelable {

    @SerializedName("FetchProduct")
    private ArrayList<FetchProduct> mFetchProduct;

    public PurchaseProductModel(ArrayList<FetchProduct> mFetchProduct) {
        this.mFetchProduct = mFetchProduct;
    }

    public ArrayList<com.kitever.pos.fragment.purchaseList.Purchase.FetchProduct> getFetchProduct() {
        return mFetchProduct;
    }

    public void setFetchProduct(ArrayList<com.kitever.pos.fragment.purchaseList.Purchase.FetchProduct> FetchProduct) {
        mFetchProduct = FetchProduct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mFetchProduct);
    }


    protected PurchaseProductModel(Parcel in) {
        this.mFetchProduct = in.createTypedArrayList(FetchProduct.CREATOR);
    }

    public static final Parcelable.Creator<PurchaseProductModel> CREATOR = new Parcelable.Creator<PurchaseProductModel>() {
        @Override
        public PurchaseProductModel createFromParcel(Parcel source) {
            return new PurchaseProductModel(source);
        }

        @Override
        public PurchaseProductModel[] newArray(int size) {
            return new PurchaseProductModel[size];
        }
    };
}
