package sms19.listview.newproject.model;

public class DeliveryReport {
	private String Status;

	private String DateTime;

	private String Message;

	private String ReplyTo;

	private String User_Login;

	private String Id;

	private String EmailID;

	private String TemplateMailId;

	public String getTemplateMailId() {
		return TemplateMailId;
	}

	public void setTemplateMailId(String templateMailId) {
		TemplateMailId = templateMailId;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String Status) {
		this.Status = Status;
	}

	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String DateTime) {
		this.DateTime = DateTime;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String Message) {
		this.Message = Message;
	}

	public String getReplyTo() {
		return ReplyTo;
	}

	public void setReplyTo(String ReplyTo) {
		this.ReplyTo = ReplyTo;
	}

	public String getUser_Login() {
		return User_Login;
	}

	public void setUser_Login(String User_Login) {
		this.User_Login = User_Login;
	}

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getEmailID() {
		return EmailID;
	}

	public void setEmailID(String EmailID) {
		this.EmailID = EmailID;
	}

	@Override
	public String toString() {
		return "ClassPojo [Status = " + Status + ", DateTime = " + DateTime
				+ ", Message = " + Message + ", ReplyTo = " + ReplyTo
				+ ", User_Login = " + User_Login + ", Id = " + Id
				+ ", EmailID = " + EmailID + "]";
	}
}
