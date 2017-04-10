package sms19.listview.newproject.model;

public class ReferListModelDB {
	private String NAME;
	private String NUMBER;
	private String STATUS;
	private String DATE;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public ReferListModelDB (String name, String number, String status, String date){
		
		this.NAME   = name;
		this.NUMBER = number;
		this.STATUS = status;
		this.DATE   = date; 
		
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getNUMBER() {
		return NUMBER;
	}

	public void setNUMBER(String nUMBER) {
		NUMBER = nUMBER;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
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
