package sms19.listview.newproject.model;

import java.util.List;

public class UpdateIndividualModel
{
//"UpdateContactReview":[{"Msg":"Record Updated Successfully"}]}
	private List<UpdateContact> UpdateContactReview;
	public List<UpdateContact> getUpdateContactReview() {
		return UpdateContactReview;
	}

	public void setUpdateContactReview(List<UpdateContact> updateContactReview) {
		UpdateContactReview = updateContactReview;
	}
	public class UpdateContact
	{
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
