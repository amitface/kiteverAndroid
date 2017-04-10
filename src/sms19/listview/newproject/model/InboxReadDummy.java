package sms19.listview.newproject.model;

public class InboxReadDummy {
	private String Date;
	private String message;
	private String time;
	private String comparedate;
	private String status;
	private String gIURL;
	private String recID;
	private int SStatus;
	private String MediaType;
	private String MessageType;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public InboxReadDummy(String datedbitem,String msgboxitem,String timeitem,String typeitem, String IURL, String RecID, int SStatus, String MediaType, String MessageType)
	{
		this.Date        = datedbitem;
		this.message     = msgboxitem;
		this.time        = timeitem;
		this.comparedate = datedbitem;
		this.status      = typeitem;
		this.gIURL       = IURL;
		this.recID       = RecID;
		this.SStatus     = SStatus;
		this.MediaType   = MediaType; 
		this.MessageType = MessageType;
	}



	public String getDate() {
		return Date;
	}



	public void setDate(String date) {
		Date = date;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public String getTime() {
		return time;
	}



	public void setTime(String time) {
		this.time = time;
	}



	public String getComparedate() {
		return comparedate;
	}



	public void setComparedate(String comparedate) {
		this.comparedate = comparedate;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getgIURL() {
		return gIURL;
	}



	public void setgIURL(String gIURL) {
		this.gIURL = gIURL;
	}



	public String getRecID() {
		return recID;
	}



	public void setRecID(String recID) {
		this.recID = recID;
	}



	public int getSStatus() {
		return SStatus;
	}



	public void setSStatus(int sStatus) {
		SStatus = sStatus;
	}



	public String getMediaType() {
		return MediaType;
	}



	public void setMediaType(String mediaType) {
		MediaType = mediaType;
	}



	public String getMessageType() {
		return MessageType;
	}



	public void setMessageType(String messageType) {
		MessageType = messageType;
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

