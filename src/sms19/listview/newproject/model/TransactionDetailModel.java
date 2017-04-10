package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class TransactionDetailModel
{
//{"UserTransactionDetails":[{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":4960235,"balance":307797656,"debits":12,"fromdate":"2014-11-11T00:02:03.883","todate":"2014-11-11T00:02:03.883","datecreated":"2014-11-11T00:02:03.883"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":4959736,"balance":307797668,"debits":4,"fromdate":"2014-11-10T19:42:58.887","todate":"2014-11-10T19:42:58.887","datecreated":"2014-11-10T19:42:58.887"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3662756,"balance":307797672,"debits":2,"fromdate":"2014-07-02T15:30:15.07","todate":"2014-07-02T15:30:15.07","datecreated":"2014-07-02T15:30:15.07"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3657516,"balance":307797674,"debits":4,"fromdate":"2014-07-01T12:09:29.65","todate":"2014-07-01T12:09:29.65","datecreated":"2014-07-01T12:09:29.65"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3647641,"balance":307797678,"debits":2,"fromdate":"2014-06-30T09:46:13.607","todate":"2014-06-30T09:46:13.607","datecreated":"2014-06-30T09:46:13.607"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3631690,"balance":307797680,"debits":2,"fromdate":"2014-06-27T13:12:27.07","todate":"2014-06-27T13:12:27.07","datecreated":"2014-06-27T13:12:27.07"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3626323,"balance":307797682,"debits":2,"fromdate":"2014-06-26T10:48:23.687","todate":"2014-06-26T10:48:23.687","datecreated":"2014-06-26T10:48:23.687"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3623190,"balance":307797684,"debits":4,"fromdate":"2014-06-25T13:15:50.513","todate":"2014-06-25T13:15:50.513","datecreated":"2014-06-25T13:15:50.513"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3610974,"balance":307797688,"debits":8,"fromdate":"2014-06-21T16:06:32.027","todate":"2014-06-21T16:06:32.027","datecreated":"2014-06-21T16:06:32.027"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3599629,"balance":307797696,"debits":2,"fromdate":"2014-06-19T11:39:14.423","todate":"2014-06-19T11:39:14.423","datecreated":"2014-06-19T11:39:14.423"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3596437,"balance":307797698,"debits":2,"fromdate":"2014-06-18T12:43:25.007","todate":"2014-06-18T12:43:25.007","datecreated":"2014-06-18T12:43:25.007"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3588793,"balance":307797700,"debits":2,"fromdate":"2014-06-16T12:52:57.417","todate":"2014-06-16T12:52:57.417","datecreated":"2014-06-16T12:52:57.417"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3588073,"balance":307797702,"debits":4,"fromdate":"2014-06-15T19:35:25.767","todate":"2014-06-15T19:35:25.767","datecreated":"2014-06-15T19:35:25.767"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3584807,"balance":307797706,"debits":2,"fromdate":"2014-06-14T11:10:45.187","todate":"2014-06-14T11:10:45.187","datecreated":"2014-06-14T11:10:45.187"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3579558,"balance":307797708,"debits":4,"fromdate":"2014-06-13T14:17:56.8","todate":"2014-06-13T14:17:56.8","datecreated":"2014-06-13T14:17:56.8"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3576384,"balance":307797712,"debits":4,"fromdate":"2014-06-12T16:12:10.503","todate":"2014-06-12T16:12:10.503","datecreated":"2014-06-12T16:12:10.503"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3562859,"balance":307797716,"debits":4,"fromdate":"2014-06-10T17:54:05.907","todate":"2014-06-10T17:54:05.907","datecreated":"2014-06-10T17:54:05.907"},{"user_id":1,"credits":0,"Mobile":"9910930090","m_id":10000,"credit_id":3537265,"balance":307797720,"debits":6,"fromdate":"2014-06-04T14:57:03.11","todate":"2014-06-04T14:57:03.11","datecreated":"2014-06-04T14:57:03.11"},{"user_id":1,"cred
//{"UserTransactionDetails":[{"DATE":"Apr 24 2015","CREDITS":0,"DEBITS":0,"BALANCE":50},
private List<Tdetail>UserTransactionDetails;

public List<Tdetail> getUserAccountDetails() {
	return UserTransactionDetails;
}
public void setUserAccountDetails(List<Tdetail> userAccountDetails) {
	UserTransactionDetails = userAccountDetails;
}

public int getListSize(){
	return UserTransactionDetails.size();
}

public class Tdetail implements Serializable
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
	
	private String DATE;
	private String CREDITS;
	private String DEBITS;
	private String BALANCE;
	
	private String EmergencyType;
	private String EmergencyMessage;
	
	
	public String getDATE() {
		return DATE;
	}
	public void setDATE(String dATE) {
		DATE = dATE;
	}
	public String getCREDITS() {
		return CREDITS;
	}
	public void setCREDITS(String cREDITS) {
		CREDITS = cREDITS;
	}
	public String getDEBITS() {
		return DEBITS;
	}
	public void setDEBITS(String dEBITS) {
		DEBITS = dEBITS;
	}
	public String getBALANCE() {
		return BALANCE;
	}
	public void setBALANCE(String bALANCE) {
		BALANCE = bALANCE;
	}
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
