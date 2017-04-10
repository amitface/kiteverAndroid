package sms19.listview.newproject.model;

import java.util.List;

public class NotiModel
{
	
	//{"GetBroadcastPagetype":[{"BroadcastTitle":"Offer from SMS19","Message":"Sendsms","LandingPage":"Sendsms"},{"BroadcastTitle":"Your Contacts","Message":"Contacts","LandingPage":"Contacts"},{"BroadcastTitle":"Template","Message":"Template","LandingPage":"Template"},
	private List<bNoti> GetBroadcastPagetype;
	public List<bNoti> getGetBroadcastPagetype() {
		return GetBroadcastPagetype;
	}

	public void setGetBroadcastPagetype(List<bNoti> getBroadcastPagetype) {
		GetBroadcastPagetype = getBroadcastPagetype;
	}
	
	public static class bNoti
	{
		
		private String BroadcastTitle;
		private String Message;
		private String LandingPage;

		private String link;
		private String MessageInsertDateTime;
		private String EmergencyType;
		private String EmergencyMessage;


		public bNoti(String BroadcastTitle,String Message,String LandingPage,String time,String link)
		{
			this.BroadcastTitle=BroadcastTitle;
			this.Message=Message;
			this.LandingPage=LandingPage;
			this.MessageInsertDateTime=time;
			this.link=link;
		}
		
		public String getBroadcastTitle() {
			return BroadcastTitle;
		}
		/*public void setBroadcastTitle(String broadcastTitle) {
			BroadcastTitle = broadcastTitle;
		}*/
		public String getMessage() {
			return Message;
		}
		/*public void setMessage(String message) {
			Message = message;
		}*/
		public String getLandingPage() {
			return LandingPage;
		}
		/*public void setLandingPage(String landingPage) {
			LandingPage = landingPage;
		}*/
		
      

		
		
		public String getLink() {
			return link;
		}
		public String getMessageInsertDateTime() {
			return MessageInsertDateTime;
		}
		public void setMessageInsertDateTime(String messageInsertDateTime) {
			MessageInsertDateTime = messageInsertDateTime;
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
