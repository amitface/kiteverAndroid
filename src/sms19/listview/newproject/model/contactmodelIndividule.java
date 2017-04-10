package sms19.listview.newproject.model;

public class contactmodelIndividule {
	private String name;
	private String mobile;
	private String DOB;
	private String Aniversary;
	private String EmergencyType;
	private String EmergencyMessage;
	private String emailId;
	
	
	public String getEmailId() {
		return emailId;
	}


	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public contactmodelIndividule(String name, String Mobile,String dob,String aniversary,String emailId)
	{
		this.name=name;
		this.mobile=Mobile;
		this.emailId=emailId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getEmergencyType() {
		return EmergencyType;
	}


	public void setEmergencyType(String emergencyType) {
		EmergencyType = emergencyType;
	}


	public String getEmergencyMessage() {
		return EmergencyMessage;
	}


	public void setEmergencyMessage(String emergencyMessage) {
		EmergencyMessage = emergencyMessage;
	}


	public String getAniversary() {
		return Aniversary;
	}


	public void setAniversary(String aniversary) {
		Aniversary = aniversary;
	}


	public String getDOB() {
		return DOB;
	}


	public void setDOB(String dOB) {
		DOB = dOB;
	}
	
	
	
}
