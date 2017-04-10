package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class SchduleListDetail {
//	 Response: {"SheduledSmsList":[{"Message":"hi","SenderID":"PELSFT              ","ScheduledDate":"2015-04-14 3:20 PM","ScheduledTime":"2015-04-14 3:20 PM","U_ID":19762,"MessageID":10481863,"TotalMessages":1,"isPrority":0,"isunicode":false},{"Message":"hisirji","SenderID":"PELSFT              ","ScheduledDate":"2015-04-15 12:40 PM","ScheduledTime":"2015-04-15 12:40 PM","U_ID":19762,"MessageID":10481871,"TotalMessages":1,"isPrority":1,"isunicode":false},{"Message":"HELLO","SenderID":"PELSFT              ","ScheduledDate":"2015-04-14 02:38 PM","ScheduledTime":"2015-04-14 02:38 PM","U_ID":19762,"MessageID":10481876,"TotalMessages":1,"isPrority":1,"isunicode":false},{"Message":"HELLO","SenderID":"PELSFT              ","ScheduledDate":"2015-04-15 01:28 PM","ScheduledTime":"2015-04-15 01:28 PM","U_ID":19762,"MessageID":10481882,"TotalMessages":1,"isPrority":1,"isunicode":false},{"Message":"HELLO","SenderID":"PELSFT              ","ScheduledDate":"2015-04-14 02:38 PM","ScheduledTime":"2015-04-14 02:38 PM","U_ID":19762,"MessageID":10481886,"TotalMessages":1,"isPrority":1,"isunicode":false}]}

	
	private List<schedule_list> SheduledSmsList;
	
	public List<schedule_list> getSheduledSmsList() {
		return SheduledSmsList;
	}
	public void setSheduledSmsList(List<schedule_list> sheduledSmsList) {
		SheduledSmsList = sheduledSmsList;
	}
	
public class schedule_list implements Serializable

{
	private static final long serialVersionUID = 1L;
	
	private String Message;
	private String SenderID;
	private String ScheduledTime;
	private String ScheduledDate;
	private String U_ID;
	private String TotalMessages;
	private String MessageID;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getSenderID() {
		return SenderID;
	}
	public void setSenderID(String senderID) {
		SenderID = senderID;
	}
	public String getScheduledTime() {
		return ScheduledTime;
	}
	public void setScheduledTime(String scheduledTime) {
		ScheduledTime = scheduledTime;
	}
	public String getScheduledDate() {
		return ScheduledDate;
	}
	public void setScheduledDate(String scheduledDate) {
		ScheduledDate = scheduledDate;
	}
	public String getU_ID() {
		return U_ID;
	}
	public void setU_ID(String u_ID) {
		U_ID = u_ID;
	}
	public String getTotalMessages() {
		return TotalMessages;
	}
	public void setTotalMessages(String totalMessages) {
		TotalMessages = totalMessages;
	}
	public String getMessageID() {
		return MessageID;
	}
	public void setMessageID(String messageID) {
		MessageID = messageID;
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
