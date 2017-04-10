package sms19.inapp.msg.model;

public class ContactDetailsNew {

	private String contact_email;

	private String contact_mobile;

	private String contact_id;

	private String Contact_DOB;

	private String contact_name;

	private String Contact_Anniversary;

	public String getContact_email() {
		return contact_email;
	}

	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}

	public String getContact_mobile() {
		return contact_mobile;
	}

	public void setContact_mobile(String contact_mobile) {
		this.contact_mobile = contact_mobile;
	}

	public String getContact_id() {
		return contact_id;
	}

	public void setContact_id(String contact_id) {
		this.contact_id = contact_id;
	}

	public String getContact_DOB() {
		return Contact_DOB;
	}

	public void setContact_DOB(String Contact_DOB) {
		this.Contact_DOB = Contact_DOB;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_Anniversary() {
		return Contact_Anniversary;
	}

	public void setContact_Anniversary(String Contact_Anniversary) {
		this.Contact_Anniversary = Contact_Anniversary;
	}

	@Override
	public String toString() {
		return "ClassPojo [contact_email = " + contact_email
				+ ", contact_mobile = " + contact_mobile + ", contact_id = "
				+ contact_id + ", Contact_DOB = " + Contact_DOB
				+ ", contact_name = " + contact_name
				+ ", Contact_Anniversary = " + Contact_Anniversary + "]";
	}
}
