
package com.kitever.pos.model;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Contact {

    @SerializedName("Email")
    private String mEmail;
    @SerializedName("Mobile")
    private String mMobile;
    @SerializedName("Name")
    private String mName;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String Email) {
        mEmail = Email;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String Mobile) {
        mMobile = Mobile;
    }

    public String getName() {
        return mName;
    }

    public void setName(String Name) {
        mName = Name;
    }

}
