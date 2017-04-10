
package com.kitever.pos.fragment.purchaseList.Purchase;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class FetchBrand {

    @SerializedName("ID")
    private Long mID;
    @SerializedName("Mode")
    private String mMode;

    public Long getID() {
        return mID;
    }

    public void setID(Long ID) {
        mID = ID;
    }

    public String getMode() {
        return mMode;
    }

    public void setMode(String Mode) {
        mMode = Mode;
    }

}
