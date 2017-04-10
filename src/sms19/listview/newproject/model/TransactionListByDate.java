package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class TransactionListByDate {


private List<TdetailDate>UserTransactionDetailsByDate;


public List<TdetailDate> getUserTransactionDetailsByDate() {
	return UserTransactionDetailsByDate;
}


public void setUserTransactionDetailsByDate(
		List<TdetailDate> userTransactionDetailsByDate) {
	UserTransactionDetailsByDate = userTransactionDetailsByDate;
}

public int getListSize(){
	return UserTransactionDetailsByDate.size();
}



public class TdetailDate implements Serializable
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
