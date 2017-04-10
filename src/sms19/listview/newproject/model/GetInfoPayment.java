package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class GetInfoPayment {
	
	//{"getpackageDetail":[{"FName":"vaibhav","Mobile":9643853169.0,"Quintity":10,"totalPrice":1.0000,"Address":null,"EMail":"1234"},

	private List<FetchPayment> getpackageDetail;

	public List<FetchPayment> getGetpackageDetail() {
		return getpackageDetail;
	}

	public void setGetpackageDetail(List<FetchPayment> getpackageDetail) {
		this.getpackageDetail = getpackageDetail;
	}
	
	
	public static class FetchPayment implements Serializable{
		
		public static final long serialVersionUID = 1L;
		
		private String FName;
		private String Mobile;
		private String Quintity;
		private String totalPrice;
		private String Address;
		private String EMail;
		private String EmergencyType;
		private String EmergencyMessage;
		
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
		public String getQuintity() {
			return Quintity;
		}
		public void setQuintity(String quintity) {
			Quintity = quintity;
		}
		public String getTotalPrice() {
			return totalPrice;
		}
		public void setTotalPrice(String totalPrice) {
			this.totalPrice = totalPrice;
		}
		public String getAddress() {
			return Address;
		}
		public void setAddress(String address) {
			Address = address;
		}
		public String getEMail() {
			return EMail;
		}
		public void setEMail(String eMail) {
			EMail = eMail;
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
