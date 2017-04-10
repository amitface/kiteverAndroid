package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;


public class UserRegistrationModel
{
//{"UserRegistration":[{"Msg":"User Registered Successfully,Password has been sent on your mobile number","MERCHANT_User_ID":"19762","MERCHANT_Fname":"RockStar","MERCHANT_Lname":null,"Merchant_Url":"abc@xyz.com","Merchant_Picture_Path":"c:\\inetpub\\wwwroot\\sms19.info\\"}]}   	
private List<Uregister> UserRegistration;
public List<Uregister> getUserRegistation() {
	return UserRegistration;
}
public void setUserRegistation(List<Uregister> userRegistation) {
	UserRegistration = userRegistation;
}

public static class Uregister implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String UserRgistration;
	private String Msg;
	private String Error;
	private String Url;
	private String MERCHANT_Fname;
	private String Merchant_Picture_Path;
	private String MERCHANT_Lname;
	private String Merchant_Url;
	private String UserId;
	private String MERCHANT_User_ID;
	private String EmergencyType;
	private String EmergencyMessage;
	private String Pwd;

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public String getError() {
		return Error;
	}

	public String getMERCHANT_Fname() {
		return MERCHANT_Fname;
	}

	public void setMERCHANT_Fname(String mERCHANT_Fname) {
		MERCHANT_Fname = mERCHANT_Fname;
	}

	public String getMerchant_Picture_Path() {
		return Merchant_Picture_Path;
	}

	public void setMerchant_Picture_Path(String merchant_Picture_Path) {
		Merchant_Picture_Path = merchant_Picture_Path;
	}

	public String getMERCHANT_Lname() {
		return MERCHANT_Lname;
	}

	public void setMERCHANT_Lname(String mERCHANT_Lname) {
		MERCHANT_Lname = mERCHANT_Lname;
	}

	public String getMerchant_Url() {
		return Merchant_Url;
	}

	public void setMerchant_Url(String merchant_Url) {
		Merchant_Url = merchant_Url;
	}

	public void setError(String error) {
		Error = error;
	}

	public String getUserRgistration() {
		return UserRgistration;
	}

	public void setUserRgistration(String userRgistration) {
		UserRgistration = userRgistration;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getMERCHANT_User_ID() {
		return MERCHANT_User_ID;
	}

	public void setMERCHANT_User_ID(String mERCHANT_User_ID) {
		MERCHANT_User_ID = mERCHANT_User_ID;
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

	public String getPwd() {
		return Pwd;
	}

	public void setPwd(String pwd) {
		Pwd = pwd;
	}
	
}

}
