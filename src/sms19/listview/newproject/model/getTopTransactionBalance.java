package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class getTopTransactionBalance {
	//{"UserTopTransactionDetails":[{"user_id":19762,"credits":0,"Mobile":"9582116782","m_id":10043,"credit_id":6533922,"balance":25,"debits":1,"fromdate":"2015-04-14T15:23:47.437","todate":"2015-04-14T15:23:47.437","datecreated":"2015-04-14T15:23:47.437"}]}
	//05-30 06:26:17.932: W/TAG(21912): Response: {"UserTopTransactionDetails":[]}

	private List<Topdetail>UserTopTransactionDetails;

	public List<Topdetail> getUserTopTransactionDetails() {
		return UserTopTransactionDetails;
	}


	public void setUserTopTransactionDetails(
			List<Topdetail> userTopTransactionDetails) {
		UserTopTransactionDetails = userTopTransactionDetails;
	}



	public class Topdetail implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		private String user_id;
		private String credits;
		private String Mobile;
		private String m_id;
		private String credit_id;
		private String balance;
		private String debits;
		private String fromdate;
		private String todate;
		private String datecreated;
		private String EmergencyType;
		private String EmergencyMessage;
		
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getCredits() {
			return credits;
		}
		public void setCredits(String credits) {
			this.credits = credits;
		}
		public String getMobile() {
			return Mobile;
		}
		public void setMobile(String mobile) {
			Mobile = mobile;
		}
		public String getM_id() {
			return m_id;
		}
		public void setM_id(String m_id) {
			this.m_id = m_id;
		}
		public String getCredit_id() {
			return credit_id;
		}
		public void setCredit_id(String credit_id) {
			this.credit_id = credit_id;
		}
		public String getBalance() {
			return balance;
		}
		public void setBalance(String balance) {
			this.balance = balance;
		}
		public String getDebits() {
			return debits;
		}
		public void setDebits(String debits) {
			this.debits = debits;
		}
		public String getFromdate() {
			return fromdate;
		}
		public void setFromdate(String fromdate) {
			this.fromdate = fromdate;
		}
		public String getTodate() {
			return todate;
		}
		public void setTodate(String todate) {
			this.todate = todate;
		}
		public String getDatecreated() {
			return datecreated;
		}
		public void setDatecreated(String datecreated) {
			this.datecreated = datecreated;
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
