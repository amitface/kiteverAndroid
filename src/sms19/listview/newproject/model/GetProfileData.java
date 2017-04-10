package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class GetProfileData 
{
private  List<GetUserProfile> ProfileDetails;


public static class GetUserProfile implements Serializable
{
	//{"ProfileDetails":[{"Column1":"www.manjushaelastic.com/sms19/at6d89-cover.png","FName":"shristi",
	//"Mobile":"6767676767","EMail":"spm@gmail.com","pincode":"110001","Gender":"",
	//"Address":"","Merchant_Code":"      ","Merchant_Url":"","User_DOB":""}]}
	
	private static final long serialVersionUID = 1L;
	private String FName;
	private String Column1;
	private String Mobile;
	private String EMail;
	private String pincode;
	private String Gender;
	private String Address;
	private String EmergencyType;
	private String EmergencyMessage;
	private String User_DOB;
	private String Merchant_Url;
	private String Merchant_Code;
	private String Currency;
	private String Country;
	private String CompnayName;
	private String Plan;
	private String ExpiryDate;
	private String DOE;
	private String UserCategory;
	private String ProfilePicturePath;
	
	public String getEMail() {
		return EMail;
	}
	public void setEMail(String eMail) {
		EMail = eMail;
	}
	public String getFName() {
		return FName;
	}
	public void setFName(String fName) {
		FName = fName;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getGender() {
		return Gender;
	}
	public void setGender(String gender) {
		Gender = gender;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getMerchant_Code() {
		return Merchant_Code;
	}
	public void setMerchant_Code(String merchant_Code) {
		Merchant_Code = merchant_Code;
	}
	public String getMerchant_Url() {
		return Merchant_Url;
	}
	public void setMerchant_Url(String merchant_Url) {
		Merchant_Url = merchant_Url;
	}
	public String getUser_DOB() {
		return User_DOB;
	}
	public void setUser_DOB(String user_DOB) {
		User_DOB = user_DOB;
	}
	public String getColumn1() {
		return Column1;
	}
	public void setColumn1(String column1) {
		Column1 = column1;
	}
	public String getEmergencyMessage() {
		return EmergencyMessage;
	}
	public void setEmergencyMessage(String emergencyMessage) {
		EmergencyMessage = emergencyMessage;
	}
	public String getEmergencyType() {
		return EmergencyType;
	}
	public void setEmergencyType(String emergencyType) {
		EmergencyType = emergencyType;
	}
	public String getPlan(){
		return Plan;
	}
	public void setPlan(String plan){
		Plan = plan;
	}
	public String getCurrency(){
		return Currency;
	}
	public void setCurrency(String currency){
		Currency = currency;
	}
	public String getCountry(){
		return Country;
	}
	public void setCountry(String country){
		Country = country;
	}
	
	public String getUserCategory(){
		return UserCategory;
	}
	public void setUserCategory(String categary){
		UserCategory = categary;
	}
	
	
	public String getCompnayName(){
		return CompnayName;
	}
	public void setCompnayName(String compnayName){
		CompnayName = compnayName;
	}
	public String getExpiryDate(){
		return ExpiryDate;
	}
	public void setExpiryDate(String expirydate){
		ExpiryDate = expirydate;
	}
	
	public String getDOE(){
		return DOE;
	}
	public void setDOE(String doe){
		DOE = doe;
	}
	

	public String getProfilePicturePath(){
		return ProfilePicturePath;
	}
	public void setProfilePicturePath(String ProfilePicturePathuser){
		ProfilePicturePath = ProfilePicturePathuser;
	}
	
	
}


public List<GetUserProfile> getProfileDetails() {
	return ProfileDetails;
}


public void setProfileDetails(List<GetUserProfile> profileDetails) {
	ProfileDetails = profileDetails;
}



}
