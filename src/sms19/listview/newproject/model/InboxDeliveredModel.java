package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class InboxDeliveredModel 
{
	/*
	 * {"TotalMessagesOfSingleSenderUser":[{"RecipientUserID":20805,"SenderUserID":20806,"Message":"",
	 * "MessageInsertDateTime":"2015-08-18T12:35:57.783","Type":20806,
	 * "FilePath":"www.manjushaelastic.com/sms19/profileSMS19.jpg","FileType":"image",
	 * "Filename":"profileSMS19.jpg","EmergencyType":"NO","EmergencyMessage":"NO"}]}
	 * 
	 * */
	
private List<allchatdata> TotalMessagesOfSingleSenderUser;

public List<allchatdata> getTotalMessagesOfSingleSenderUser(){
	return TotalMessagesOfSingleSenderUser;
}
public void setTotalMessagesOfSingleSenderUser(
		List<allchatdata> totalMessagesOfSingleSenderUser) 
{
	TotalMessagesOfSingleSenderUser = totalMessagesOfSingleSenderUser;
}

public static class allchatdata implements Serializable
{

private static final long serialVersionUID = 1L;
private String Message;
private String MessageInsertDateTime;
private String Type;
private String FilePath;
private String FileType; 
private String EmergencyType;
private String EmergencyMessage;

public String getMessage() {
	return Message;
}
public void setMessage(String message) {
	Message = message;
}
public String getMessageInsertDateTime() {
	return MessageInsertDateTime;
}
public void setMessageInsertDateTime(String messageInsertDateTime) {
	MessageInsertDateTime = messageInsertDateTime;
}
public String getType() {
	return Type;
}
public void setType(String type) {
	Type = type;
}
public String getFilePath() {
	return FilePath;
}
public void setFilePath(String filePath) {
	FilePath = filePath;
}
public String getFileType() {
	return FileType;
}
public void setFileType(String fileType) {
	FileType = fileType;
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
