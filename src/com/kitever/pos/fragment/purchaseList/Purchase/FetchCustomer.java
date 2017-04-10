
package com.kitever.pos.fragment.purchaseList.Purchase;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class FetchCustomer {

    @SerializedName("Contact_ID")
    private Long mContactID;
    @SerializedName("ContactName")
    private String mContactName;

    @SerializedName("CompanyName")
    private String mCompanyName;

    public Long getContactID() {
        return mContactID;
    }

    public void setContactID(Long ContactID) {
        mContactID = ContactID;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String ContactName) {
        mContactName = ContactName;
    }

    public String getmCompanyName() {
        return mCompanyName;
    }

    public void setmCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }
}
