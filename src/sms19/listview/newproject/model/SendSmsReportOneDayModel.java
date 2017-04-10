package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

import android.util.Log;

public class SendSmsReportOneDayModel 
{
	//{"MessageID":19839457,"Mobile":"8527805413          ","Message":"","SenderID":"PELSFT              ","MessageStatus":"Sent","MessageDeliveredDate":"2015-04-09T13:14:24.247","LastError":null}	
	
	private List<ReportSmsOne> BeforeOneDayReport;
	public List<ReportSmsOne> getBeforeOneDayReport() 
	{
		return BeforeOneDayReport;
	}

	public void setBeforeOneDayReport(List<ReportSmsOne> beforeOneDayReport)
	{
		BeforeOneDayReport = beforeOneDayReport;
	}
	
	public int getListSize()
	{
		
		int i=BeforeOneDayReport.size();
		Log.w("kjsd","djbsd"+i);
		return i;
	}
	
	public class ReportSmsOne implements Serializable
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
