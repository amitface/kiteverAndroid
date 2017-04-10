package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

import android.util.Log;

public class SendSmsReport {
	// {"mobile":"6555545666","Message":"We Wish A Peaceful And Prosperous Navratri\tHaving Full Blessings Of Durga Devi Mata\tAnd We Pray For The Continuation Of Success,\tPower, And Freedom From Evil Thoughts\tAnd Wisdom For You And To All Those Dear\tTo You Throughout This Year.\tHappy Durga Pooja And Navratri 9643853169http://google.com","SenderID":"PELSFT ",
	// "MessageStatus":"Sent",
	// "MessageDeliveredDate":"2015-05-27T20:17:54.33","LastError":null}

	private List<ReportSms> DeliveryReport;

	public List<ReportSms> getSmsTodayReport() {
		return DeliveryReport;
	}

	public void setSmsTodayReport(List<ReportSms> smsTodayReport) {
		DeliveryReport = smsTodayReport;
	}

	public int getListSize() {
		if (DeliveryReport != null) {
			int i = DeliveryReport.size();
			Log.w("kjsd", "djbsd" + i);
			return i;
		} else {
			return 0;
		}

	}

	public class ReportSms implements Serializable {

		private static final long serialVersionUID = 1L;
		private String Mobile;
		private String Message;
		private String MessageStatus;
		private String MessageDeliveredDate;
//		private String EmergencyType;
//		private String EmergencyMessage;

		public String getMessage() {
			return Message;
		}

		public void setMessage(String message) {
			Message = message;
		}

		public String getMessageDeliveredDate() {
			return MessageDeliveredDate;
		}

		public void setMessageDeliveredDate(String messageDeliveredDate) {
			MessageDeliveredDate = messageDeliveredDate;
		}

		public String getMessageStatus() {
			return MessageStatus;
		}

		public void setMessageStatus(String messageStatus) {
			MessageStatus = messageStatus;
		}

		public String getMobile() {
			return Mobile;
		}

		public void setMobile(String mobile) {
			this.Mobile = mobile;
		}

//		public String getEmergencyType() {
//			return EmergencyType;
//		}
//
//		public void setEmergencyType(String emergencyType) {
//			EmergencyType = emergencyType;
//		}
//
//		public String getEmergencyMessage() {
//			return EmergencyMessage;
//		}
//
//		public void setEmergencyMessage(String emergencyMessage) {
//			EmergencyMessage = emergencyMessage;
//		}

	}

}
