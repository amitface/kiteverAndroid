package sms19.inapp.msg.model;

public class PhoneValidModel {

	String phone_number = "", country_code = "";
	boolean isNumber = false;

	public String getPhone_number() {
		return phone_number;
	}

	public String getCountry_code() {
		return country_code;
	}

	public boolean isNumber() {
		return isNumber;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public void setNumber(boolean isNumber) {
		this.isNumber = isNumber;
	}

	@Override
	public String toString() {
		return "PhoneValidModel [phone_number=" + phone_number
				+ ", country_code=" + country_code + ", isNumber=" + isNumber
				+ "]";
	}
	
	
	

}
