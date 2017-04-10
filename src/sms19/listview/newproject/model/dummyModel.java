package sms19.listview.newproject.model;

public class dummyModel {
	
	private String Title;
	private String Message;
	private String lendinbg;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public dummyModel(String title, String message,String LendingPage){
		this.Title = title;
		this.Message =message;
		this.lendinbg = LendingPage;
		
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = "this is dummy title";
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = "hello this is message";
	}

	public String getLendinbg() {
		return lendinbg;
	}

	public void setLendinbg(String lendinbg) {
		this.lendinbg = "lending page";
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
	
	

}
