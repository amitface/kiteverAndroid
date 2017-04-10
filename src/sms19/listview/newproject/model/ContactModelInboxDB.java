
package sms19.listview.newproject.model;

public class ContactModelInboxDB {
	
	private String name;
	private String mobile;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public ContactModelInboxDB(String name, String Mobile)
	{
		this.name=name;
		this.mobile=Mobile;
		
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
	
	
	
}
