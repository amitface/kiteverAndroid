package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

import android.util.Log;



public class SendSmsReportFourDayModel 
{
	private List<ReportSmsFour> BeforeFourDayReport;
	public List<ReportSmsFour> getBeforeFourDayReport()
	{
		return BeforeFourDayReport;
	}

	public void setBeforeFourDayReport(List<ReportSmsFour> beforeFourDayReport) 
	{
		BeforeFourDayReport = beforeFourDayReport;
	}
	
	public int getListSize()
	{
		
		int i=BeforeFourDayReport.size();
		Log.w("kjsd","djbsd"+i);
		return i;
	}
	
	public class ReportSmsFour implements Serializable
	{

	private static final long serialVersionUID = 1L;
	private String mobile;
	private String Message;
	private String MessageStatus;
	private String MessageDeliveredDate;
	private String EmergencyType;
	private String EmergencyMessage;
	
	public String getMessage() 
	{
		return Message;
	}
	public void setMessage(String message)
	{
		Message = message;
	}
	public String getMessageDeliveredDate() 
	{
		return MessageDeliveredDate;
	}
	public void setMessageDeliveredDate(String messageDeliveredDate) 
	{
		MessageDeliveredDate = messageDeliveredDate;
	}
	public String getMessageStatus() 
	{
		return MessageStatus;
	}
	public void setMessageStatus(String messageStatus)
	{
		MessageStatus = messageStatus;
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
