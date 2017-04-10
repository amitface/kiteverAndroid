package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class ForgotPasswordModel {

	private List<forgot>ForgotPassword;
	
	public List<forgot> getForgotPassword() {
		return ForgotPassword;
	}
	public void setForgotPassword(List<forgot> forgotPassword) {
		ForgotPassword = forgotPassword;
	}
	public class forgot implements Serializable
	{
	
	private static final long serialVersionUID = 1L;
	
	private	String Error;
	private String Msg;
	private String EmergencyType;
	private String EmergencyMessage;
	
	
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	public String getError() {
		return Error;
	}
	public void setError(String error) {
		Error = error;
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
