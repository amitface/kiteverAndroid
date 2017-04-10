package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class InsertContactModel 
{
// Response: {"ContactRegistration":[{"Error":"MobileNo Already Exists"}]}
	//{"ContactRegistration":[{"Msg":"Record(s) inserted successfully"}]}
	private List<ContactListInsert> ContactRegistration;
	public List<ContactListInsert> getContactRegistration() {
		return ContactRegistration;
	}
	public void setContactRegistration(List<ContactListInsert> contactRegistration) {
		ContactRegistration = contactRegistration;
	}
	public class ContactListInsert implements Serializable
	{
		
		private static final long serialVersionUID = 1L;
		private String Error;
		private String Msg;
		private String EmergencyType;
		private String EmergencyMessage;
		
		public String getError() {
			return Error;
		}
		public void setError(String error) {
			Error = error;
		}
		public String getMsg() {
			return Msg;
		}
		public void setMsg(String msg) {
			Msg = msg;
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
