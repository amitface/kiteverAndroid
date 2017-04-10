package com.kitever.pos.fragment.purchaseList.Purchase;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by android on 7/3/17.
 */

public class PurchaseInsertModel implements Parcelable {

    private String type;

    @SerializedName("FetchProduct")
    private ArrayList<FetchProduct> fetchProducts;

    private ArrayList<FetchProduct> finalProducts;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<FetchProduct> getFetchProducts() {
        return fetchProducts;
    }

    public void setFetchProducts(ArrayList<FetchProduct> fetchProducts) {
        this.fetchProducts = fetchProducts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeList(this.fetchProducts);
    }

    public PurchaseInsertModel(String type, ArrayList<FetchProduct> fetchProducts) {
        this.type = type;
        this.fetchProducts = fetchProducts;
    }

    protected PurchaseInsertModel(Parcel in) {
        this.type = in.readString();
        this.fetchProducts = new ArrayList<FetchProduct>();
        in.readList(this.fetchProducts, FetchProduct.class.getClassLoader());
    }

    public static final Parcelable.Creator<PurchaseInsertModel> CREATOR = new Parcelable.Creator<PurchaseInsertModel>() {
        @Override
        public PurchaseInsertModel createFromParcel(Parcel source) {
            return new PurchaseInsertModel(source);
        }

        @Override
        public PurchaseInsertModel[] newArray(int size) {
            return new PurchaseInsertModel[size];
        }
    };
}
