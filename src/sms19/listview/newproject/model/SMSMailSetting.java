
package sms19.listview.newproject.model;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class SMSMailSetting {

    @SerializedName("FooterEnabled")
    private Long mFooterEnabled = 0L;
    @SerializedName("HeaderEnabled")
    private Long mHeaderEnabled = 0L;
    @SerializedName("ReplyToMailID")
    private String mReplyToMailID = "";
    @SerializedName("Sender_ID")
    private Long mSenderId;
    @SerializedName("Sender_Name")
    private String mSenderName;

    public SMSMailSetting(Long mSenderId, String mSenderName) {
        this.mSenderId = mSenderId;
        this.mSenderName = mSenderName;
    }

    public Long getFooterEnabled() {
        return mFooterEnabled;
    }

    public void setFooterEnabled(Long FooterEnabled) {
        mFooterEnabled = FooterEnabled;
    }

    public Long getHeaderEnabled() {
        return mHeaderEnabled;
    }

    public void setHeaderEnabled(Long HeaderEnabled) {
        mHeaderEnabled = HeaderEnabled;
    }

    public String getReplyToMailID() {
        return mReplyToMailID;
    }

    public void setReplyToMailID(String ReplyToMailID) {
        mReplyToMailID = ReplyToMailID;
    }

    public Long getSenderId() {
        return mSenderId;
    }

    public void setSenderId(Long senderId) {
        mSenderId = senderId;
    }

    public String getSenderName() {
        return mSenderName;
    }

    public void setSenderName(String senderName) {
        mSenderName = senderName;
    }

}
