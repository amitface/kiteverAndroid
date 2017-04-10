package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class ThreadModel {
	//{"UndeliveredMessageForRecipientCount":[{"GeneralMessageCount":0}]}
	//{"UndeliveredMessageForRecipientCount":[{"GeneralMessageCount":0,"BroadcastGeneralMessageCount":0}]}
	private List<MsgThread> UndeliveredMessageForRecipientCount;
	
	public List<MsgThread> getUndeliveredMessageForRecipientCount() {
		return UndeliveredMessageForRecipientCount;
	}

	public void setUndeliveredMessageForRecipientCount(
			List<MsgThread> undeliveredMessageForRecipientCount) {
		UndeliveredMessageForRecipientCount = undeliveredMessageForRecipientCount;
	}



	public static class MsgThread implements Serializable{

		private static final long serialVersionUID = 1L;
	
		private String GeneralMessageCount;
		private String BroadcastMessageCount;
		private String BroadcastTitle ;
		private String EmergencyType;
		private String EmergencyMessage;
		private String Time;
		
		//{"UndeliveredMessageForRecipientCount":[{"GeneralMessageCount":0,"MessageID":null,"BroadcastMessageCount":0,"BroadcastID":null,
			//"BroadcastTitle":null,"Time":"2015-08-24T15:15:51.577","EmergencyType":"NO","EmergencyMessage":"NO"}]}
		

		public String getGeneralMessageCount() {
			return GeneralMessageCount;
		}

		public void setGeneralMessageCount(String generalMessageCount) {
			GeneralMessageCount = generalMessageCount;
		}

		public String getBroadcastMessageCount() {
			return BroadcastMessageCount;
		}

		public void setBroadcastMessageCount(String broadcastMessageCount) {
			BroadcastMessageCount = broadcastMessageCount;
		}

		public String getBroadcastTitle() {
			return BroadcastTitle;
		}

		public void setBroadcastTitle(String broadcastTitle) {
			BroadcastTitle = broadcastTitle;
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

		public String getTime() {
			return Time;
		}

		public void setTime(String time) {
			Time = time;
		}

		
		
		
	}

}
