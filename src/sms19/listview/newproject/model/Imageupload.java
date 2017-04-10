package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class Imageupload
{
	//{"ProfilePicture":[{"Result":"Profile Picture Saved Successfully."}]}
	private List<Profilepic>ProfilePicture ;
	
	public List<Profilepic> getProfilePicture() {
		return ProfilePicture;
	}

	public void setProfilePicture(List<Profilepic> profilePicture) {
		ProfilePicture = profilePicture;
	}
	public class Profilepic implements Serializable
	{
	
	private static final long serialVersionUID = 1L;
	
	private String Result;
	private String EmergencyType;
	private String EmergencyMessage;

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
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
