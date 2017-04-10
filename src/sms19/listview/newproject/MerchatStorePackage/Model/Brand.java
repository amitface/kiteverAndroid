
package sms19.listview.newproject.MerchatStorePackage.Model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Brand {

    @SerializedName("BrandID")
    private Long mBrandID;
    @SerializedName("BrandName")
    private String mBrandName;
    @SerializedName("IsActive")
    private Object mIsActive;

    public Long getBrandID() {
        return mBrandID;
    }

    public void setBrandID(Long BrandID) {
        mBrandID = BrandID;
    }

    public String getBrandName() {
        return mBrandName;
    }

    public void setBrandName(String BrandName) {
        mBrandName = BrandName;
    }

    public Object getIsActive() {
        return mIsActive;
    }

    public void setIsActive(Object IsActive) {
        mIsActive = IsActive;
    }

}
