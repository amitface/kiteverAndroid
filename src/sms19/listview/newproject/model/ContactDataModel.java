package sms19.listview.newproject.model;

public class ContactDataModel {
	
	private String cName;
    private String Contactid;
	private String cnum;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public String getcName() {
		return cName;
	}

	/*public void setcName(String cName) {
		this.cName = cName;
	}

*/
	public ContactDataModel(String nameContactData,String num)
	{
		
		this.cName = nameContactData;
		this.cnum =num;
	}

	

	public String getCnum() {
		return cnum;
	}

	/*public void setCnum(String cnum) {
		this.cnum = cnum;
	}
*/
	public String getContactid() {
		return Contactid;
	}

	public void setContactid(String contactid) {
		Contactid = contactid;
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
