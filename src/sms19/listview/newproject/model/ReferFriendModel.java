package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class ReferFriendModel
{
List <Refer> FriendReferred;
//{"FriendReferred":[{"User_ID":19762,"RecipientName":"RockStar","RecipientPhoneNo":"9654469382","ReferredType":"0"},{"RecipientName":"Ravish","RecipientPhoneNo":"9654469384","InsertedDate":"2015-07-17T17:39:30","ReferredType":"1"},
	
public List<Refer> getReferFriend()
{
		return FriendReferred;
}

public void setReferFriend(List<Refer> referFriend)
{
		FriendReferred = referFriend;
}
	
	public static class Refer implements Serializable{

		private static final long serialVersionUID = 1L;


		private String SenderPhoneNo;
		private String RecipientName;
		private String RecipientPhoneNo;
		private String InsertedDate;
		private String SenderUserID;
		private String ReferredType;
		private String EmergencyType;
		private String EmergencyMessage;
		
		public String getSenderPhoneNo() {
			return SenderPhoneNo;
		}
		public void setSenderPhoneNo(String senderPhoneNo) {
			SenderPhoneNo = senderPhoneNo;
		}
		public String getRecipientName() {
			return RecipientName;
		}
		public void setRecipientName(String recipientName) {
			RecipientName = recipientName;
		}
		public String getRecipientPhoneNo() {
			return RecipientPhoneNo;
		}
		public void setRecipientPhoneNo(String recipientPhoneNo) {
			RecipientPhoneNo = recipientPhoneNo;
		}
		public String getInsertedDate() {
			return InsertedDate;
		}
		public void setInsertedDate(String insertedDate) {
			InsertedDate = insertedDate;
		}
		public String getSenderUserID() {
			return SenderUserID;
		}
		public void setSenderUserID(String senderUserID) {
			SenderUserID = senderUserID;
		}
		
		public String getReferredType() {
			return ReferredType;
		}
		public void setReferredType(String referredType) {
			ReferredType = referredType;
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
