package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

import android.util.Log;



public class SendSmsReportThreeDayModel 
{
//{"BeforeThreeDayReport":[{"MessageID":19756626,"mobile":"784000****","Message":"Gud morning mam,\n All Service running well after checked i inform u...Have a Gud Day\n Regard\nVinay Kumar Upadhyay","SenderID":"PELSFT ","MessageStatus":"Sent","MessageDeliveredDate":"2015-03-23T09:58:09.987","LastError":null}
private List<ReportSmsThree> BeforeThreeDayReport;
    
public List<ReportSmsThree> getBeforeThreeDayReport() 
{
	return BeforeThreeDayReport;
}

public void setBeforeThreeDayReport(List<ReportSmsThree> beforeThreeDayReport)
{
	BeforeThreeDayReport = beforeThreeDayReport;
}	

public int getListSize()
{
	
	int i=BeforeThreeDayReport.size();
	Log.w("kjsd","djbsd"+i);
	return i;
}
	
public class ReportSmsThree implements Serializable
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
