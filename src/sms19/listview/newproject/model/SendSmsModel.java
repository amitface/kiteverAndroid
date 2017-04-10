package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;


public class SendSmsModel
{
private List<sendsms> SendSms;
//05-28 16:07:20.502: W/TAG(2172): Response: {"SendSms":[{"Msg":"Sent."}]}

public class sendsms implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	private String Msg;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
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

public List<sendsms> getSendSms() {
	return SendSms;
}

public void setSendSms(List<sendsms> sendSms) {
	SendSms = sendSms;
}


}
