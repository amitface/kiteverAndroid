package sms19.listview.newproject.model;

import java.util.List;

public class FetchUrl
{

//{"AutomatedServicesURL":[{"ServicesURL":"http://leads19.com/NewService.aspx?Page=","EmergencyType":"NO","EmergencyMessage":"NO"}]}	
private List<automateurl>AutomatedServicesURL;

public List<automateurl> getAutomatedServicesURL() 
{
	return AutomatedServicesURL;
}

public void setAutomatedServicesURL(List<automateurl> automatedServicesURL)
{
	AutomatedServicesURL = automatedServicesURL;
}	

public class automateurl
{
private String AutomatedServicesURL;
private String ServicesURL;
private String EmergencyType;
private String EmergencyMessage;

public String getAutomatedServicesURL() 
{
	return AutomatedServicesURL;
}
public void setAutomatedServicesURL(String automatedServicesURL) 
{
	AutomatedServicesURL = automatedServicesURL;
}
public String getServicesURL()
{
	return ServicesURL;
}
public void setServicesURL(String servicesURL) 
{
	ServicesURL = servicesURL;
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
