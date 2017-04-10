package sms19.listview.newproject.model;

public class SheduledMailList {
	private String Message;

	private String DateTime;

	private String BatchID;

	private String Recipient;
	
	private String TemplateMailId;
	
	private String TemplateName;
	

	public SheduledMailList(String message, String dateTime, String batchID,
			String recipient, String templateMailId, String templateName) {
		super();
		Message = message;
		DateTime = dateTime;
		BatchID = batchID;
		Recipient = recipient;
		TemplateMailId = templateMailId;
		TemplateName = templateName;
	}
	
	public SheduledMailList(String message, String dateTime, String batchID,
			String recipient) {
		super();
		Message = message;
		DateTime = dateTime;
		BatchID = batchID;
		Recipient = recipient;
	}

	public String getMessage() {
		return Message;
	}

	public String getTemplateMailId() {
		return TemplateMailId;
	}

	public void setTemplateMailId(String templateMailId) {
		TemplateMailId = templateMailId;
	}

	public String getTemplateName() {
		return TemplateName;
	}

	public void setTemplateName(String templateName) {
		TemplateName = templateName;
	}

	

	public void setMessage(String Message) {
		this.Message = Message;
	}

	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String DateTime) {
		this.DateTime = DateTime;
	}

	public String getBatchID() {
		return BatchID;
	}

	public void setBatchID(String BatchID) {
		this.BatchID = BatchID;
	}

	public String getRecipient() {
		return Recipient;
	}

	public void setRecipient(String Recipient) {
		this.Recipient = Recipient;
	}

	@Override
	public String toString() {
		return "ClassPojo [Message = " + Message + ", DateTime = " + DateTime
				+ ", BatchID = " + BatchID + ", Recipient = " + Recipient + "]";
	}
}