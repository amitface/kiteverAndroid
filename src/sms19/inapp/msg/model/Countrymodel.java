package sms19.inapp.msg.model;

public class Countrymodel {
	String name = "";
	String c_code = "";
	String p_cod = "",country_id="",countrycode="",country_name="";
	boolean selected = false;
	String countryISOCode="";
	
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getC_code() {
		return c_code;
	}

	public void setC_code(String c_code) {
		this.c_code = c_code;
	}

	public String getP_cod() {
		return p_cod;
	}

	public void setP_cod(String p_cod) {
		this.p_cod = p_cod;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getCountry_id() {
		return country_id;
	}

	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getCountryISOCode() {
		return countryISOCode;
	}

	public void setCountryISOCode(String countryISOCode) {
		this.countryISOCode = countryISOCode;
	}
	
	

}
