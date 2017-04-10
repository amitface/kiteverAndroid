
package sms19.listview.newproject.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class UserMailTemplateListDetail {

    @SerializedName("m_id")
    private Long mMId;
    @SerializedName("template")
    private String mTemplate;
    @SerializedName("template_id")
    private Long mTemplateId;
    @SerializedName("TemplateSubject")
    private String mTemplateSubject;
    @SerializedName("template_title")
    private String mTemplateTitle;
    @SerializedName("user_id")
    private Long mUserId;

    public Long getMId() {
        return mMId;
    }

    public void setMId(Long mId) {
        mMId = mId;
    }

    public String getTemplate() {
        return mTemplate;
    }

    public void setTemplate(String template) {
        mTemplate = template;
    }

    public Long getTemplateId() {
        return mTemplateId;
    }

    public void setTemplateId(Long templateId) {
        mTemplateId = templateId;
    }

    public String getTemplateSubject() {
        return mTemplateSubject;
    }

    public void setTemplateSubject(String TemplateSubject) {
        mTemplateSubject = TemplateSubject;
    }

    public String getTemplateTitle() {
        return mTemplateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        mTemplateTitle = templateTitle;
    }

    public Long getUserId() {
        return mUserId;
    }

    public void setUserId(Long userId) {
        mUserId = userId;
    }

}
