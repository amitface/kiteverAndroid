package sms19.listview.newproject.model;


public class contactmodel 
{
private String name;
private String Contactcount;
private String EmergencyType;
private String EmergencyMessage;

public contactmodel(String name,String Contactcount)
{
	this.name=name;
	this.Contactcount=Contactcount;
	
}

public String getName()
{
	return name;
}
public String getContactcount() {
	return Contactcount;
}
public void setContactcount(String contactcount) {
	Contactcount = contactcount;
}
public void setName(String name) {
	this.name = name;
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
