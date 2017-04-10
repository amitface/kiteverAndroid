
package sms19.listview.newproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class MailTemplateListModel {

    @SerializedName("UserMailTemplateListDetail")
    private List<sms19.listview.newproject.model.UserMailTemplateListDetail> mUserMailTemplateListDetail;

    public List<sms19.listview.newproject.model.UserMailTemplateListDetail> getUserMailTemplateListDetail() {
        return mUserMailTemplateListDetail;
    }

    public void setUserMailTemplateListDetail(List<sms19.listview.newproject.model.UserMailTemplateListDetail> UserMailTemplateListDetail) {
        mUserMailTemplateListDetail = UserMailTemplateListDetail;
    }

}
