package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

import android.util.Log;



public class SendSmsReportTwoDayModel
{
	//{"BeforeTwoDayReport":[{"MessageID":19773334,"mobile":"901574****","Message":"gud 9t","SenderID":"PELSFT ","MessageStatus":"Sent","MessageDeliveredDate":"2015-03-24T20:03:17.403","LastError":null},{"MessageID":19765553,"mobile":"901574****","Message":"gud morning","SenderID":"PELSFT ","MessageStatus":"Sent","MessageDeliveredDate":"2015-03-24T09:55:21.507","LastError":null},
	private List<ReportSmsTwo> BeforeTwoDayReport;
    
	public List<ReportSmsTwo> getBeforeTwoDayReport()
	{
		return BeforeTwoDayReport;
	}


	public void setBeforeTwoDayReport(List<ReportSmsTwo> beforeTwoDayReport)
	{
		BeforeTwoDayReport = beforeTwoDayReport;
	}
	
	public int getListSize()
	{
		
		int i=BeforeTwoDayReport.size();
		Log.w("kjsd","djbsd"+i);
		return i;
	}
	
	public class ReportSmsTwo implements Serializable
	{
		
		private static final long serialVersionUID = 1L;
		private String mobile;//mobile
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
		public String getMobile() 
		{
			return mobile;
		}
		public void setMobile(String mobile) 
		{
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
