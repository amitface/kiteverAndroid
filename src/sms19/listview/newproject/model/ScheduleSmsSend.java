package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class ScheduleSmsSend {
	
	//{"SheduledSms":[{"Msg":"Recipient Can Not be Less Than Two"}]}
	//SheduledSms
	private List<ScheduleSms>  SheduledSms;

	
	


	public static class ScheduleSms implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		private String Msg;
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





	public List<ScheduleSms> getSheduledSms() {
		return SheduledSms;
	}





	public void setSheduledSms(List<ScheduleSms> sheduledSms) {
		SheduledSms = sheduledSms;
	}
	

}
