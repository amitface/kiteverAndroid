package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class ScheduleListDelete 
{
	//{"DeleteSheduledSms":[{"Msg":"Please enter Username or Password.
	
	private List<DeleteSheduledSms> DeleteSheduledSms;
	private List<DeleteSheduledMail> DeleteSheduledMail;

	public List<DeleteSheduledSms> getDeleteSheduledSms() {
		return DeleteSheduledSms;
	}

	public void setDeleteSheduledSms(List<DeleteSheduledSms> deleteSheduledSms) {
		DeleteSheduledSms = deleteSheduledSms;
	}
	
	
	public List<DeleteSheduledMail> getDeleteSheduledMail() {
		return DeleteSheduledMail;
	}

	public void setDeleteSheduledMail(List<DeleteSheduledMail> deleteSheduledMail) {
		DeleteSheduledMail = deleteSheduledMail;
	}
	
	
	public class DeleteSheduledSms implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private String Msg="";
		private String EmergencyType;
		private String EmergencyMessage;

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
	
	public class DeleteSheduledMail implements Serializable{

		
		private String Msg="";

		public String getMsg() {
			return Msg;
		}

		public void setMsg(String msg) {
			Msg = msg;
		}

		
		
				
	}
	

}
