package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class GroupDataModel 
{
	
//{"ContactGroupRegistration":[{"Msg":"Group Create Successfully"}]}
	
private List<ContactGroup> ContactGroupRegistration;
public List<ContactGroup> getContactGroupRegistration() {
	return ContactGroupRegistration;
}

public void setContactGroupRegistration(List<ContactGroup> contactGroupRegistration) {
	ContactGroupRegistration = contactGroupRegistration;
}
	
public class ContactGroup implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String Msg;
	private String Error;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	public String getError() {
		return Error;
	}
	public void setError(String error) {
		Error = error;
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


	
	
	
	
}
