package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class FetchAllContact {

	List<ContactDetails> ContactDetails;

	public List<ContactDetails> getContactDetails() {
		return ContactDetails;
	}

	public void setContactDetails(List<ContactDetails> contactDetails) {
		ContactDetails = contactDetails;
	}


public int getListSize(){
	return ContactDetails.size();
}
	
	public class ContactDetails implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		//{"ContactDetails":[{"contact_id":106164747,"contact_name":"ghazipur","contact_mobile":"8527805414","contact_email":null}]}
		
		private String email_id;
		private String contact_name;
		private String contact_mobile;
		private String contact_id;
		private String Error;
		private String Msg;
		private String EmergencyType;
		private String EmergencyMessage;
		private String DOB;
		private String ANIVER;
		
		public String getEmail_id() {
			return email_id;
		}
		public void setEmail_id(String email_id) {
			this.email_id = email_id;
		}
		public String getContact_id() {
			return contact_id;
		}
		public void setContact_id(String contact_id) {
			this.contact_id = contact_id;
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
		public String getDOB() {
			return DOB;
		}
		public void setDOB(String dOB) {
			DOB = dOB;
		}
		public String getANIVER() {
			return ANIVER;
		}
		public void setANIVER(String aNIVER) {
			ANIVER = aNIVER;
		}
		
		
		
	}
	
	

}

