
package sms19.listview.newproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class SMSMailSettingModel {

    @SerializedName("SMSMailSetting")
    private ArrayList<SMSMailSetting> mSMSMailSetting;

    public SMSMailSettingModel(ArrayList<SMSMailSetting> mSMSMailSetting) {
        this.mSMSMailSetting = mSMSMailSetting;
    }

    public List<sms19.listview.newproject.model.SMSMailSetting> getSMSMailSetting() {
        return mSMSMailSetting;

    }

    public void setSMSMailSetting(ArrayList<sms19.listview.newproject.model.SMSMailSetting> SMSMailSetting) {
        mSMSMailSetting = SMSMailSetting;
    }

}
