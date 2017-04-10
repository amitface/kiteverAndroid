package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;


public class EmailModel {

	private List<Email> userdetailusingemail;

	public List<Email> getUserdetailusingemail() {
		return userdetailusingemail;
	}

	public void setUserdetailusingemail(List<Email> userdetailusingemail) {
		this.userdetailusingemail = userdetailusingemail;
	}


	public static class Email implements Serializable {

		private static final long serialVersionUID = 1L;

		//{"userdetailusingemail":[{"User_ID":455,"Mobile":"8899776655","Pwd":"16854","Status":"0 ","logintype":null,"EmailStatus":"1"}]}
		private String User_ID;
		private String Mobile;
		private String Status;
		private String Msg;
		private String Error;
		private String Balance;
		private String Pwd;
		private String EmailStatus;
		private String user_login;
		private String login_type;
		private String EmergencyType;
		private String EmergencyMessage;
		
		public String getEmailStatus() {
			return EmailStatus;
		}
		public void setEmailStatus(String emailStatus) {
			EmailStatus = emailStatus;
		}
		public String getPwd() {
			return Pwd;
		}
		public void setPwd(String pwd) {
			Pwd = pwd;
		}
		public String getBalance() {
			return Balance;
		}
		public void setBalance(String balance) {
			Balance = balance;
		}
		public String getUser_ID() {
			return User_ID;
		}
		public void setUser_ID(String user_ID) {
			User_ID = user_ID;
		}
		public String getMobile() {
			return Mobile;
		}
		public void setMobile(String mobile) {
			Mobile = mobile;
		}
		public String getStatus() {
			return Status;
		}
		public void setStatus(String status) {
			Status = status;
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
		public void setError(String error) {
			Error = error;
		}
		public String getUser_login() {
			return user_login;
		}
		public void setUser_login(String user_login) {
			this.user_login = user_login;
		}
		public String getLogin_type() {
			return login_type;
		}
		public void setLogin_type(String login_type) {
			this.login_type = login_type;
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
