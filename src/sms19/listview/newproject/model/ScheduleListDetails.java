package sms19.listview.newproject.model;




public class ScheduleListDetails 
{
	
	private String DateTime;
	private String Message;
	private String TotalCount;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public ScheduleListDetails(String DateTime,String Message,String TotalCount)
	{
		this.DateTime=DateTime;
		this.TotalCount=TotalCount;
		this.Message=Message;
	}

	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getTotalCount() {
		return TotalCount;
	}

	public void setTotalCount(String totalCount) {
		TotalCount = totalCount;
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
