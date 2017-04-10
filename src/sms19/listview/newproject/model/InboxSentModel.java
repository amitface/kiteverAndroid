package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class InboxSentModel
{
private  List<Sentchatmessage> MessageSendToRecipients;


public List<Sentchatmessage> getMessageSendToRecipients() {
	return MessageSendToRecipients;
}


public void setMessageSendToRecipients(
		List<Sentchatmessage> messageSendToRecipients) {
	MessageSendToRecipients = messageSendToRecipients;
}


public static class Sentchatmessage implements Serializable
{

private static final long serialVersionUID = 1L;
private String Result;//Result
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