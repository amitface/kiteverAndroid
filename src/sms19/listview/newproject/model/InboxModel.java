package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class InboxModel {
	
        //{"MessageReceivedToRecipient":[{"SenderUserID":20807,"SenderPhoneNo":"6767676767","SenderName":"shristi",
	    //"Message":"htr","MessageInsertDateTime":"2015-08-26T12:14:00","CountMessage":4,"ProfilePictureName":null,
	    //"ProfilePicturePath":"www.manjushaelastic.com/sms19/IMG_20150814_115130.jpg","Filename":null,
		//"FileType":null,"FilePath":null,"EmergencyType":"NO","EmergencyMessage":"NO"}]}
	private List<InboxList> MessageReceivedToRecipient;

	public List<InboxList> getMessageReceivedToRecipient() {
		return MessageReceivedToRecipient;
	}

	public void setMessageReceivedToRecipient(
			List<InboxList> messageReceivedToRecipient) {
		MessageReceivedToRecipient = messageReceivedToRecipient;
	}


	public static class InboxList implements Serializable{

		private static final long serialVersionUID = 1L;
	
		private String SenderUserID;
		private String SenderPhoneNo;
		private String Type;
		private String Message;
		private String CountMessage;
		private String MessageInsertDateTime;
		private String SenderName;
		private String ProfilePicturePath;
		private String EmergencyType;
		private String EmergencyMessage;
		
		//{"MessageReceivedToRecipient":[{"SenderUserID":20705,"SenderPhoneNo":"9582116782",
		//"SenderName":"vaibhav","Message":"222222222","MessageInsertDateTime":"2015-07-23T19:04:00","CountMessage":1}]}
		
		public String getSenderName() {
			return SenderName;
		}
		public void setSenderName(String senderName) {
			SenderName = senderName;
		}
		public String getSenderUserID() {
			return SenderUserID;
		}
		public void setSenderUserID(String senderUserID) {
			SenderUserID = senderUserID;
		}
		public String getSenderPhoneNo() {
			return SenderPhoneNo;
		}
		public void setSenderPhoneNo(String senderPhoneNo) {
			SenderPhoneNo = senderPhoneNo;
		}
		public String getType() {
			return Type;
		}
		public void setType(String type) {
			Type = type;
		}
		public String getMessage() {
			return Message;
		}
		public void setMessage(String message) {
			Message = message;
		}
		public String getCountMessage() {
			return CountMessage;
		}
		public void setCountMessage(String countMessage) {
			CountMessage = countMessage;
		}
		public String getMessageInsertDateTime() {
			return MessageInsertDateTime;
		}
		public void setMessageInsertDateTime(String messageInsertDateTime) {
			MessageInsertDateTime = messageInsertDateTime;
		}
		public String getProfilePicturePath() {
			return ProfilePicturePath;
		}
		public void setProfilePicturePath(String profilePicturePath) {
			ProfilePicturePath = profilePicturePath;
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
