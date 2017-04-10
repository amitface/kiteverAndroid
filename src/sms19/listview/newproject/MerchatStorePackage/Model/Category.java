
package sms19.listview.newproject.MerchatStorePackage.Model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Category {

    @SerializedName("CategoryCode")
    private Object mCategoryCode;
    @SerializedName("CategoryID")
    private Long mCategoryID;
    @SerializedName("CategoryName")
    private String mCategoryName;
    @SerializedName("Description")
    private String mDescription;
    @SerializedName("IsActive")
    private String mIsActive;
    @SerializedName("ParentCategory")
    private Long mParentCategory;
    @SerializedName("ParentCategoryID")
    private String mParentCategoryID;
    @SerializedName("Type")
    private String mType;

    public Object getCategoryCode() {
        return mCategoryCode;
    }

    public void setCategoryCode(Object CategoryCode) {
        mCategoryCode = CategoryCode;
    }

    public Long getCategoryID() {
        return mCategoryID;
    }

    public void setCategoryID(Long CategoryID) {
        mCategoryID = CategoryID;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String CategoryName) {
        mCategoryName = CategoryName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String Description) {
        mDescription = Description;
    }

    public String getIsActive() {
        return mIsActive;
    }

    public void setIsActive(String IsActive) {
        mIsActive = IsActive;
    }

    public Long getParentCategory() {
        return mParentCategory;
    }

    public void setParentCategory(Long ParentCategory) {
        mParentCategory = ParentCategory;
    }

    public String getParentCategoryID() {
        return mParentCategoryID;
    }

    public void setParentCategoryID(String ParentCategoryID) {
        mParentCategoryID = ParentCategoryID;
    }

    public String getType() {
        return mType;
    }

    public void setType(String Type) {
        mType = Type;
    }

}
