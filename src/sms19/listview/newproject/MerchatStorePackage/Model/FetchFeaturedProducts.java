
package sms19.listview.newproject.MerchatStorePackage.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class FetchFeaturedProducts implements Parcelable {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Product")
    private List<sms19.listview.newproject.MerchatStorePackage.Model.Product> mProduct;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String Message) {
        mMessage = Message;
    }

    public List<sms19.listview.newproject.MerchatStorePackage.Model.Product> getProduct() {
        return mProduct;
    }

    public void setProduct(List<sms19.listview.newproject.MerchatStorePackage.Model.Product> Product) {
        mProduct = Product;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String Status) {
        mStatus = Status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMessage);
        dest.writeList(mProduct);
        dest.writeString(mStatus);
    }

    public FetchFeaturedProducts() {
    }

    protected FetchFeaturedProducts(Parcel in) {
        this.mMessage = in.readString();
        this.mProduct = new ArrayList<Product>();
        in.readList(this.mProduct, Product.class.getClassLoader());
        this.mStatus = in.readString();
    }

    public static final Parcelable.Creator<FetchFeaturedProducts> CREATOR = new Parcelable.Creator<FetchFeaturedProducts>() {
        @Override
        public FetchFeaturedProducts createFromParcel(Parcel source) {
            return new FetchFeaturedProducts(source);
        }

        @Override
        public FetchFeaturedProducts[] newArray(int size) {
            return new FetchFeaturedProducts[size];
        }
    };
}
