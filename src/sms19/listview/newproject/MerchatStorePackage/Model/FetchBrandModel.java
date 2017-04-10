
package sms19.listview.newproject.MerchatStorePackage.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class FetchBrandModel {

    @SerializedName("Brand")
    private List<sms19.listview.newproject.MerchatStorePackage.Model.Brand> mBrand;
    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Status")
    private String mStatus;

    public List<sms19.listview.newproject.MerchatStorePackage.Model.Brand> getBrand() {
        return mBrand;
    }

    public void setBrand(List<sms19.listview.newproject.MerchatStorePackage.Model.Brand> Brand) {
        mBrand = Brand;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String Message) {
        mMessage = Message;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String Status) {
        mStatus = Status;
    }

}
