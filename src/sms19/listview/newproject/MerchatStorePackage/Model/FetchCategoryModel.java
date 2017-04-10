
package sms19.listview.newproject.MerchatStorePackage.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class FetchCategoryModel {

    @SerializedName("Category")
    private List<sms19.listview.newproject.MerchatStorePackage.Model.Category> mCategory;
    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Status")
    private String mStatus;

    public List<sms19.listview.newproject.MerchatStorePackage.Model.Category> getCategory() {
        return mCategory;
    }

    public void setCategory(List<sms19.listview.newproject.MerchatStorePackage.Model.Category> Category) {
        mCategory = Category;
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
