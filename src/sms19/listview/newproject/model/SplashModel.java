package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class SplashModel {
	//{"MerchantDetails":[{"User_ID":19762,"Fname":"RockStar","Lname":null,
	//"Merchant_Url":"abc@xyz.com","Merchant_Picture_Path":"http://leads19.com/AppFolder/ProfilePicture/20717.jpeg"}]}
	
	private List<Splashimg>MerchantDetails ;
	
	public List<Splashimg> getMerchantDetails() {
		return MerchantDetails;
	}

	public void setMerchantDetails(List<Splashimg> merchantDetails) {
		MerchantDetails = merchantDetails;
	}

	public class Splashimg implements Serializable
	{
	
	private static final long serialVersionUID = 1L;
	
	private String User_ID;
	private String FName;
	private String LName;
	private String Merchant_Url;
	private String ProfilePicturePath;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public String getUser_ID() {
		return User_ID;
	}
	public void setUser_ID(String user_ID) {
		User_ID = user_ID;
	}
	public String getFname() {
		return FName;
	}
	public void setFname(String Fname) {
		FName = Fname;
	}
	public String getLname() {
		return LName;
	}
	public void setLname(String Lname) {
		LName = Lname;
	}
	public String getMerchant_Url() {
		return Merchant_Url;
	}
	public void setMerchant_Url(String merchant_Url) {
		Merchant_Url = merchant_Url;
	}
	public String getMerchant_Picture_Path() {
		return ProfilePicturePath;
	}
	public void setMerchant_Picture_Path(String profilePicturePath) {
		ProfilePicturePath = profilePicturePath;
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
