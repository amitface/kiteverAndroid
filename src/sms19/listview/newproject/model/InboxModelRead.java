package sms19.listview.newproject.model;


public class InboxModelRead 
{
	String Cname;
	private String inTime;
	private String inMsg;
	private String inMobile;
	private String inStatus;
	private String SenderID;
	private String name;
	private String EmergencyType;
	private String EmergencyMessage;
	
	//private Context con;
	//DataBaseDetails dbObj=new DataBaseDetails(con);
	
	public InboxModelRead(String inboxTime, String inboxMessage, String inboxMobile, String Status, String SenderID,String name)
	{
		this.inTime   = inboxTime;
		this.inMsg    = inboxMessage;
		this.inMobile = inboxMobile;
		this.inStatus = Status;
		this.SenderID = SenderID;
		this.name     = name;
	}
	


	public String getSenderID() {
		return SenderID;
	}



	public void setSenderID(String senderID) {
		SenderID = senderID;
	}



	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getInMsg() {
		return inMsg;
	}

	public void setInMsg(String inMsg) {
		this.inMsg = inMsg;
	}

	public String getInMobile() {
		return inMobile;
	}

	public void setInMobile(String inMobile) {
		this.inMobile = inMobile;
	}

	public String getInStatus() {
		return inStatus;
	}

	public void setInStatus(String inStatus) {
		this.inStatus = inStatus;
	}



	public String getName() 
	{
		
		return name;
	}

	/* public void fetchContactName(String number) 
 	{
 		dbObj.Open();
 		  
 		   Cursor c;
 		   
 		   c= dbObj.SearchIndividuleContact(number);
 		  
 		   Cname="";
 		  				   
 		   while(c.moveToNext())
 		   {
 		  Cname = c.getString(2).trim();
 		Log.w("Contact name", "Contact Name"+Cname);
 		   }
 				   
 		   dbObj.close();
 	}*/

	public void setName(String name) 
	{
		
		this.name=name;	
		
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
