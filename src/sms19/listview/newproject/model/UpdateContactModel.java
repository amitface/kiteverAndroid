package sms19.listview.newproject.model;

import java.util.List;

public class UpdateContactModel 
{
//"DeleteContactReview":[{"Msg":"Record Deleted Successfully"}]}

	private List<DeleteR> DeleteContactReview;
	
	public List<DeleteR> getDeleteContactReview() {
		return DeleteContactReview;
	}
	public void setDeleteContactReview(List<DeleteR> deleteContactReview) {
		DeleteContactReview = deleteContactReview;
	}
	
	
	public class DeleteR
	{
	private String Msg;
	private String Error;
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
