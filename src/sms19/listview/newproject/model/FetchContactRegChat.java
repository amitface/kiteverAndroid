package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class FetchContactRegChat {

	List<ContactDetailsChat> ContactDetails;

	public List<ContactDetailsChat> getContactDetails() {
		return ContactDetails;
	}

	public void setContactDetails(List<ContactDetailsChat> contactDetails) {
		ContactDetails = contactDetails;
	}


public int getListSize(){
	return ContactDetails.size();
}
	
	public class ContactDetailsChat implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		//{"ContactDetails":[{"contact_id":106164747,"contact_name":"ghazipur","contact_mobile":"8527805414","contact_email":null}]}
		
		private String contact_name;
		private String contact_mobile;
		private String contact_userid;
		private String user_id;
		private String Error;
		private String Msg;
		private String EmergencyType;
		private String EmergencyMessage;
		
		
		public String getContact_userid() {
			return contact_userid;
		}
		public void setContact_userid(String contact_userid) {
			this.contact_userid = contact_userid;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getContact_name() {
			return contact_name;
		}
		public void setContact_name(String contact_name) {
			this.contact_name = contact_name;
		}
		public String getContact_mobile() {
			return contact_mobile;
		}
		public void setContact_mobile(String contact_mobile) {
			this.contact_mobile = contact_mobile;
		}
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
