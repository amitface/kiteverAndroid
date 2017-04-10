package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

	public class LoginModel
	{

		//{"Login":[{"MerchantUserID":"20717 ","MerchantName":"puja pelsoft","MerchantWebsite":null,"ProfilePicturePath":"www.manjushaelastic.com/sms19/bawkoof.jpeg","User_ID":20717,"Pwd":"1234","Mobile":"9643853169","Status":"1 ","Msg":"Login Successfully","UserType":"0 ","user_login":"9643853169","Userfilter":"MTIX_TR"}]}
		
		
		private List<Login> Login;

		public List<Login> getLogin() 
		{
			return Login;
		}

		public void setLogin(List<Login> login)
		{
			Login = login;
		}

		public static class Login implements Serializable 
		{

			private static final long serialVersionUID = 1L;
		
		
			private String UserType;
			private String MerchantName;
			private String ProfilePicturePath;
			private String MerchantWebsite;
			private String User_ID;
			private String Mobile;
			private String Status;
			private String Msg;
			private String Error;
			private String Balance;
			private String Pwd;
			private String Userfilter;
			private String user_login;
			private String MerchantUserID;
			private String EmergencyType;
			private String EmergencyMessage;
			private String Column1;
			private String Message;
			private String QueryStatus="";
			private String UserMessage="";
			
			private String EMail;
			private String pincode;
			private String Country;
			private String CompnayName;
			private String DOE;
			private String Currency;
			private String MerchantProfilePicturePath;
			private String User_DOB;
			private String Name;
			private String UserCategory;
			private String Plan;
			private String ExpiryDate;
			private String Merchant_Code;
			private String ErrorMessage;
			private String Merchant_Url;
			private String Home_URL;
			private String AddOn;
			private String State;
			private String City;
			private String FileSizeLimit;




			public String getState() {
				return State;
			}

			public void setState(String state) {
				State = state;
			}

			public String getCity() {
				return City;
			}

			public void setCity(String city) {
				City = city;
			}

			public String getFileSizeLimit() {
				return FileSizeLimit;
			}

			public void setFileSizeLimit(String fileSizeLimit) {
				FileSizeLimit = fileSizeLimit;
			}


			public String getMerchant_Url() {
				return Merchant_Url;
			}
			public void setMerchant_Url(String merchant_Url) {
				Merchant_Url = merchant_Url;
			}
			
			public String getHome_Url() {
				return Home_URL;
			}
			public void setHome_Url(String home_URL) {
				Home_URL = home_URL;
			}
			
			public String getAddOn() {
				return AddOn;
			}
			public void setAddOn(String addon) {
				AddOn = addon;
			}
			
			public String getErrorMessage() {
				return ErrorMessage;
			}
			public void setErrorMessage(String errorMessage) {
				ErrorMessage = errorMessage;
			}
			public String getMerchant_Code() {
				return Merchant_Code;
			}
			public void setMerchant_Code(String merchant_Code) {
				Merchant_Code = merchant_Code;
			}
			public String getExpiryDate() {
				return ExpiryDate;
			}
			public void setExpiryDate(String expiryDate) {
				ExpiryDate = expiryDate;
			}
			public String getPlan() {
				return Plan;
			}
			public void setPlan(String plan) {
				Plan = plan;
			}
			public String getUserCategory() {
				return UserCategory;
			}
			public void setUserCategory(String userCategory) {
				UserCategory = userCategory;
			}
			public String getName() {
				return Name;
			}
			public void setName(String name) {
				this.Name = name;
			}
			public String getUser_DOB() {
				return User_DOB;
			}
			public void setUser_DOB(String user_DOB) {
				User_DOB = user_DOB;
			}
			public String getMerchantWebsite() 
			{
				return MerchantWebsite;
			}
			public String getEMail() {
				return EMail;
			}
			public void setEMail(String eMail) {
				EMail = eMail;
			}
			public String getPincode() {
				return pincode;
			}
			public void setPincode(String pincode) {
				this.pincode = pincode;
			}
			public String getCountry() {
				return Country;
			}
			public void setCountry(String country) {
				Country = country;
			}
			public String getCompnayName() {
				return CompnayName;
			}
			public void setCompnayName(String compnayName) {
				CompnayName = compnayName;
			}
			public String getDOE() {
				return DOE;
			}
			public void setDOE(String dOE) {
				DOE = dOE;
			}
			public String getCurrency() {
				return Currency;
			}
			public void setCurrency(String currency) {
				Currency = currency;
			}
			public String getMerchantProfilePicturePath() {
				return MerchantProfilePicturePath;
			}
			public void setMerchantProfilePicturePath(String merchantProfilePicturePath) {
				MerchantProfilePicturePath = merchantProfilePicturePath;
			}
			public void setMerchantWebsite(String merchantWebsite) 
			{
				MerchantWebsite = merchantWebsite;
			}
			public String getMerchantUserID()
			{
				return MerchantUserID;
			}
			public void setMerchantUserID(String merchantUserID)
			{
				MerchantUserID = merchantUserID;
			}
			public String getMerchantName()
			{
				return MerchantName;
			}
			public void setMerchantName(String merchantName) 
			{
				MerchantName = merchantName;
			}
			public String getProfilePicturePath() 
			{
				return ProfilePicturePath;
			}
			public void setProfilePicturePath(String profilePicturePath)
			{
				ProfilePicturePath = profilePicturePath;
			}
		
			public String getPwd()
			{
				return Pwd;
			}
			public void setPwd(String pwd) 
			{
				Pwd = pwd;
			}
			public String getBalance() 
			{
				return Balance;
			}
			public void setBalance(String balance)
			{
				Balance = balance;
			}
			public String getUser_ID() {
				return User_ID;
			}
			public void setUser_ID(String user_ID)
			{
				User_ID = user_ID;
			}
			public String getMobile() 
			{
				return Mobile;
			}
			public void setMobile(String mobile) 
			{
				Mobile = mobile;
			}
			public String getStatus() 
			{
				return Status;
			}
			public void setStatus(String status) 
			{
				Status = status;
			}
			public String getMsg() 
			{
				return Msg;
			}
			public void setMsg(String msg)
			{
				Msg = msg;
			}
			public String getError()
			{
				return Error;
			}
			public void setError(String error) 
			{
				Error = error;
			}
			public String getUserfilter()
			{
				return Userfilter;
			}
			public void setUserfilter(String userfilter) 
			{
				Userfilter = userfilter;
			}
			public String getUser_login() 
			{
				return user_login;
			}
			public void setUser_login(String user_login) 
			{
				this.user_login = user_login;
			}
			public String getUserType() 
			{
				return UserType;
			}
			public void setUserType(String userType) 
			{
				UserType = userType;
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
			public String getColumn1() {
				return Column1;
			}
			public void setColumn1(String column1) {
				Column1 = column1;
			}
			public String getMessage() {
				return Message;
			}
			public void setMessage(String message) {
				Message = message;
			}
			public String getQueryStatus() {
				return QueryStatus;
			}
			public void setQueryStatus(String queryStatus) {
				QueryStatus = queryStatus;
			}
			public String getUserMessage() {
				return UserMessage;
			}
			public void setUserMessage(String userMessage) {
				UserMessage = userMessage;
			}
		
			
			
						
		}
		
		
		
		
	}