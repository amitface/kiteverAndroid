package sms19.listview.newproject.model;

import java.util.List;

public class TemplateListByName
{

private List<TemlistByname> UserTemplate;


public List<TemlistByname> getUserTemplate() {
	return UserTemplate;
}


public void setUserTemplate(List<TemlistByname> userTemplate) {
	UserTemplate = userTemplate;
}



public class TemlistByname
{
private String user_id;
private String Template_ID;
private String Template_Title;
private String EmergencyType;
private String EmergencyMessage;

public String getUser_id() {
	return user_id;
}
public void setUser_id(String user_id) {
	this.user_id = user_id;
}
public String getTemplate_ID() {
	return Template_ID;
}
public void setTemplate_ID(String template_ID) {
	Template_ID = template_ID;
}
public String getTemplate_Title() {
	return Template_Title;
}
public void setTemplate_Title(String template_Title) {
	Template_Title = template_Title;
}
public String getTemplate() {
	return Template;
}
public void setTemplate(String template) {
	Template = template;
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

private String Template;
}
}
