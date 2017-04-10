package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

// {"GroupContactDetail":[{"Contact_Name":"ghh","contact_id":108661723,"Contact_Mobile":"5155656655",
//"Contact_DOB":"","Contact_Anniversary":""},{"Contact_Name":"rr","contact_id":108661722,
//"Contact_Mobile":"9582116781","Contact_DOB":"","Contact_Anniversary":""}]}

public class GroupContactDetailModel 
{
private List<getAllNameAndNumber> GroupContactDetail;

public List<getAllNameAndNumber> getGroupContactDetail() {
	return GroupContactDetail;
}


public void setGroupContactDetail(List<getAllNameAndNumber> groupContactDetail) {
	GroupContactDetail = groupContactDetail;
}


public int getListSize(){
	return GroupContactDetail.size();
}


public class getAllNameAndNumber implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	private String Contact_Name;

	private String Contact_Mobile;
    private String Msg;
    private String Error;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public String getContact_Name() {
		return Contact_Name;
	}

	public void setContact_Name(String contact_Name) {
		Contact_Name = contact_Name;
	}

	public String getContact_Mobile() {
		return Contact_Mobile;
	}

	public void setContact_Mobile(String contact_Mobile) {
		Contact_Mobile = contact_Mobile;
	}

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
