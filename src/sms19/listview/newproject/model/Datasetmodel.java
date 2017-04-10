package sms19.listview.newproject.model;

import java.util.List;

public class Datasetmodel 
{
private List<Dataset> fetchuserdata;

public List<Dataset> getFetchuserdata()
{
	return fetchuserdata;
}

public void setFetchuserdata(List<Dataset> fetchuserdata) 
{
	this.fetchuserdata = fetchuserdata;
}
public class Dataset
{
private String Date;
private String message;
private String time;
private String comparedate;
private String status;
private String EmergencyType;
private String EmergencyMessage;

public Dataset(String date,String message,String time,String status)
{
	this.time=time;
	this.message=message;
	this.Date = date;	
	this.status=status;
	
}

public String getDate() {
	return Date;
}
/*public void setDate(String date) {
	Date = date;
}*/
public String getTime() {
	return time;
}
/*public void setTime(String time) {
	this.time = time;
}*/
public String getMessage() {
	return message;
}
/*public void setMessage(String message) {
	this.message = message;
}*/

public void setComparedate(String comparedate) {
	this.comparedate = comparedate;
}

public String getStatus() {
	return status;
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

/*public void setStatus(String status) {
	this.status = status;
}*/


}


}
