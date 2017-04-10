package sms19.inapp.msg.model;

public class RegisterModel  {

	String name="";



	String companyname="";
	String userId="", userNumber="";
	String countryCode="",  verifyCode="",  adminroom_id="",adminroom_name="",email="";
	String Pwd="";
	String login_type="";
	int reLoginType=0;
	String device_id="";
	String version="";
	String user_choise="";
	String UserLogin="";
	String UserCategory;
	
	
	
	
	public String getUserCategory() {
		return UserCategory;
	}
	public void setUserCategory(String userCategory) {
		UserCategory = userCategory;
	}
	public String getUser_choise() {
		return user_choise;
	}
	public void setUser_choise(String user_choise) {
		this.user_choise = user_choise;
	}
	public String getDevice_id() {
		return device_id;
	}
	public String getVersion() {
		return version;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getReLoginType() {
		return reLoginType;
	}
	public void setReLoginType(int reLoginType) {
		this.reLoginType = reLoginType;
	}
	public String getUserId() {
		return userId;
	}
	public String getUserNumber() {
		return userNumber;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public String getAdminroom_id() {
		return adminroom_id;
	}
	public String getAdminroom_name() {
		return adminroom_name;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public void setAdminroom_id(String adminroom_id) {
		this.adminroom_id = adminroom_id;
	}
	public void setAdminroom_name(String adminroom_name) {
		this.adminroom_name = adminroom_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return Pwd;
	}
	public void setPwd(String pwd) {
		Pwd = pwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogin_type() {
		return login_type;
	}
	public void setLogin_type(String login_type) {
		this.login_type = login_type;
	}
	public String getUserLogin() {
		return UserLogin;
	}
	public void setUserLogin(String userLogin) {
		UserLogin = userLogin;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	
}
