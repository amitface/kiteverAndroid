package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

import android.util.Log;

public class BindTemplateModel 
{
private List<UserTemplateList> UserTemplateList;

// {"UserTemplateList":[{"Template_Title":"morning"}]}


public List<UserTemplateList> getUserTemplateList() {
	return UserTemplateList;
}
public void setUserTemplateList(List<UserTemplateList> userTemplateList) {
	UserTemplateList = userTemplateList;
}
public class UserTemplateList implements Serializable
{


	private static final long serialVersionUID = 1L;
	private String template_title;
	private String User_ID;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public String getTemplate_Title() 
	{
		return template_title;
	}
	public void setTemplate_Title(String template_Title) {
		template_title = template_Title;
	}
	public String getTemplate_ID() {
		return User_ID;
	}
	public void setTemplate_ID(String template_ID) {
		User_ID = template_ID;
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
public int getListSize()
{
	
	int i=UserTemplateList.size();
	Log.w("kjsd","djbsd"+i);
	return i;
}


}
