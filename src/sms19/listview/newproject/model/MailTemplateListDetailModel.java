package sms19.listview.newproject.model;

public class MailTemplateListDetailModel {
	private String templateId;
	private String mId;
	private String templateTitle;
	private String template;
	private String userIdTemplate;
	private String mTemplateSubject;

	public MailTemplateListDetailModel(String templateId, String mId,
			String templateTitle, String template, String userIdTemplate, String mTemplateSubject) {
		super();
		this.templateId = templateId;
		this.mId = mId;
		this.templateTitle = templateTitle;
		this.template = template;
		this.userIdTemplate = userIdTemplate;
		this.mTemplateSubject = mTemplateSubject;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getTemplateTitle() {
		return templateTitle;
	}

	public void setTemplateTitle(String templateTitle) {
		this.templateTitle = templateTitle;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getUserIdTemplate() {
		return userIdTemplate;
	}

	public void setUserIdTemplate(String userIdTemplate) {
		this.userIdTemplate = userIdTemplate;
	}

	public String getmTemplateSubject() {
		return mTemplateSubject;
	}

	public void setmTemplateSubject(String mTemplateSubject) {
		this.mTemplateSubject = mTemplateSubject;
	}
}
