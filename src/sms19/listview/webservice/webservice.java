package sms19.listview.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.ContactDetailsNew;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.AllTemplate;
import sms19.listview.newproject.model.BindTemplateModel;
import sms19.listview.newproject.model.ChangePasswordModel;
import sms19.listview.newproject.model.DeleteGroup;
import sms19.listview.newproject.model.EditUmodel;
import sms19.listview.newproject.model.EmailModel;
import sms19.listview.newproject.model.FetchAllContact;
import sms19.listview.newproject.model.FetchContactRegChat;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.newproject.model.FetchGroupDetailsInbox;
import sms19.listview.newproject.model.FetchPackage;
import sms19.listview.newproject.model.FetchSenderIDs;
import sms19.listview.newproject.model.FetchSenderModel;
import sms19.listview.newproject.model.FetchUrl;
import sms19.listview.newproject.model.ForgotPasswordModel;
import sms19.listview.newproject.model.GetFTPCre;
import sms19.listview.newproject.model.GetInfoPayment;
import sms19.listview.newproject.model.GetProfileData;
import sms19.listview.newproject.model.GroupContactDetailModel;
import sms19.listview.newproject.model.GroupDataModel;
import sms19.listview.newproject.model.Imageupload;
import sms19.listview.newproject.model.InboxDeliveredModel;
import sms19.listview.newproject.model.InboxModel;
import sms19.listview.newproject.model.InboxSentModel;
import sms19.listview.newproject.model.InsertContactModel;
import sms19.listview.newproject.model.LoginModel;
import sms19.listview.newproject.model.NotiModel;
import sms19.listview.newproject.model.OPTINmodel;
import sms19.listview.newproject.model.ReferFriendModel;
import sms19.listview.newproject.model.ReportMailModel;
import sms19.listview.newproject.model.SchduleListDetail;
import sms19.listview.newproject.model.ScheduleListDelete;
import sms19.listview.newproject.model.ScheduleSmsSend;
import sms19.listview.newproject.model.ScheduledMailModel;
import sms19.listview.newproject.model.SendSmsModel;
import sms19.listview.newproject.model.SendSmsReport;
import sms19.listview.newproject.model.SendSmsReportFiveDayModel;
import sms19.listview.newproject.model.SendSmsReportFourDayModel;
import sms19.listview.newproject.model.SendSmsReportOneDayModel;
import sms19.listview.newproject.model.SendSmsReportSixDayModel;
import sms19.listview.newproject.model.SendSmsReportThreeDayModel;
import sms19.listview.newproject.model.SendSmsReportTwoDayModel;
import sms19.listview.newproject.model.SplashModel;
import sms19.listview.newproject.model.TemplateListByName;
import sms19.listview.newproject.model.ThreadModel;
import sms19.listview.newproject.model.TransactionDetailModel;
import sms19.listview.newproject.model.TransactionListByDate;
import sms19.listview.newproject.model.UpdateContactModel;
import sms19.listview.newproject.model.UpdateIndividualModel;
import sms19.listview.newproject.model.UserRegistrationModel;
import sms19.listview.newproject.model.getTopBalance;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class webservice {

	// -------------------------------------Global
	// variable-----------------------------
	
	public static final String EQU = "=";
	public static final String AND = "&";
	// public static String BSE_URL = "http://sms19.com/NewService.aspx?Page=";
	// public static String BSE_URL = "http://kit19.com/NewService.aspx?Page=";
	// public static String BSE_URL = "http://sms19.info/NewService.aspx?Page=";

	public static final int TYPE_GET = 1;
	public static final int TYPE_POST = 2;
	// (SEARCH)
	public static final int TYPE_LOGIN = 1;// --01

	public static final int TYPE_GETPROFILE = 3;// --03
	public static final int TYPE_SIGNUP = 4;// Registration --04
	public static final int TYPE_FORGOTPASSWD = 5; // --05
	public static final int TYPE_EDIT = 6;// Edit Profile --06
	public static final int TYPE_TEMPLATE = 7;// All TemplateName list --07
	public static final int TYPE_TRANSACTIONDETL = 8;// Transactional details
														// --08
	public static final int Type_GET_TEM_BY_NM = 9;// Template fetch by
													// TemplateName --09

	public static final int TYPE_TRAN_DETAILS_BY_DATE = 10;// get transaction
															// details by date
															// --10

	public static final int TYPE_SEND_MSG = 11;// send message --11

	public static final int TYPE_SMS_REPORT = 12;// Sms_Report_status --12

	public static final int TYPE_SEND_MSG_SCHEDULE = 13;// send message for
														// schedule time --13

	public static final int TYPE_SMS_ONEREPORT = 14;// BEFORE1DAY_SEND_SMS_REPORT
													// --14
	public static final int TYPE_SMS_TWOREPORT = 15;// BEFORE2DAY_SEND_SMS_REPORT
													// --15
	public static final int TYPE_SMS_THREEREPORT = 16;// BEFORE3DAY_SEND_SMS_REPORT
														// --16
	public static final int TYPE_SMS_FOURREPORT = 17;// BEFORE4DAY_SEND_SMS_REPORT
														// --17
	public static final int TYPE_SMS_FIVEREPORT = 18;// BEFORE5DAY_SEND_SMS_REPORT
														// --18
	public static final int TYPE_SMS_SIXREPORT = 19;// BEFORE6DAY_SEND_SMS_REPORT
													// --19

	public static final int TYPE_INSERT_SEL_PACKAGE = 20; // use for payment
															// gateway --20
	public static final int TYPE_GET_INFO_PAYMENT = 21; // get information
														// regards payment
														// gateway --21

	public static final int TYPE_INSERT_SINGLE_CONTACT = 22; // Insert_Single_Contact
																// (CONTACT)
																// --22
	public static final int TYPE_UPDATE_SINGLE_CONTACT = 23; // UPDATE_Single_Contact
																// (CONTACT)
																// --23
	public static final int TYPE_DELETE_SINGLE_CONTACT = 24; // DELETE_SINGLE_CONTACT
																// (CONTACT)
																// --24

	public static final int TYPE_GET_ALL_CONTACT = 28;// Get all Contact By
														// Userid (CONTACT) --28
	public static final int TYPE_GET_ALL_CONTACT_FOR_CHAT = 288;// Get all
																// Contact By
																// Userid
																// (CONTACT)
																// --28

	public static final int TYPE_INSERT_GRP_CONT = 27;// Insert Group details
														// (GROUP) --27
	public static final int TYPE_UPDATE_GROUP_CONTACT = 29;// Update group
															// contact //
															// (GROUP) --29
	public static final int TYPE_GET_GROUP_DETAILS = 25;/* fetch all details of
														 group name (GROUP)
														 --25*/
	public static final int TYPE_GET_SENDERS_IDS = 26;/* fetch Senders Ids
	 --26*/
	public static final int TYPE_DELETE_GROUP_CONTACT = 30;// Delete group
															// contact (GROUP)
															// --30
	public static final int TYPE_GET_GROUP_CONTACT = 31;// get group contact
														// (GROUP) --31
	public static final int TYPE_DELETE_GROUP = 38;// delete group (GROUP) --38

	public static final int TYPE_GET_ALL_TEMPLATE = 32;// get all template --32

	public static final int TYPE_CHECK_EMAIL_STATUS = 33;// check email status
															// --33
	public static final int TYPE_GET_TOP_BALANCE = 34;// fetch top current
														// balance --34
	public static final int TYPR_GET_TEMPLATE = 35;// Get All Templates --35
	public static final int TYPE_GET_ALL_SCHEDULE = 36;// FETCH ALL SCHEDULE
														// LIST --36
	public static final int TYPE_DELETE_SCHEDULE = 37;// DELETE schedule sms
														// --37
	public static final int TYPE_DELETE_SCHEDULE_MAIL=377;
	
	public static final int TYPE_FETCH_MOBIPACKAGE = 39;// FetchMobilePackage
														// --39

	public static final int TYPE_FETCH_MESSAGE_THREAD = 40;// Fetch_Message for
															// thread --40
	public static final int TYPE_FETCH_MESSAGE_INBOX = 41;// Fetch_Message_for
															// inbox --41
	public static final int TYPE_RECEIVE_MESSAGE_INBOX = 42;// Receive_Message_for
															// inboxread page
															// --42
	public static final int TYPE_SENT_MESSAGE_INBOX = 43;// Sent_Message_for
															// inboxread page
															// --43

	public static final int TYPE_REFER_FRIEND = 44;// Refer a friend --44
	public static final int TYPE_FetchChatContact = 45;// Inbox chat user (Send
														// message inbox) --45
	public static final int TYPE_OPTIN = 46;// OPT_IN_OUT --46

	public static final int TYPE_NOTIFICATION = 47;// broadcast Notification
													// --47
	public static final int TYPE_FetchChatGroup = 48;// Inbox chat user group
														// (send message inbox)
														// --48

	public static final int TYPE_IMAGE_UPLOAD = 49;// Upload Image to server
													// --49
	public static final int TYPE_SPLASH_IMAGE = 50;// Splash screen and name
													// change --50

	public static final int TYPE_FTP_UPLD = 51;// FILE UPLOAD --51
	public static final int TYPE_FECT_URL = 52;// GET URL --52
	public static final int TYPE_PIC_AUD_VID = 53;// AudioVideoPictureMessage
													// --53

	public static final int TYPE_CHANGE_PASSWORD = 54;// change password --54

	public static final int TYPE_MAIL_REPORT = 55;// Fetch Mail Report

	public static final int TYPE_GET_MAIL_SCHEDULE = 56;// Fetch Mail Scheduled

	// Use listener in this class for taking response from server service side
	private static ServiceHitListener listener;
	private static ServiceHitListener listenerTemp;

	// global variable use in this class for http post,url,type and service name
	private HttpPost HTTPPOST;
	private String URL;
	private int TYPE;
	private int SERVICENAME;

	// ----------------------------------------------Global
	// variable--------------------------------

	DataBaseDetails dbd;

	// fcontext for check internet is available or not
	public static Context _context;

	Method checkInternet;

	// constructor for taking value from service.

	public webservice(HttpPost HttpPost, String url, int type, int ServiceName,
			ServiceHitListener listener) {

		// pass value from current class to this handler global variable value.
		webservice.listener = listener;
		this.HTTPPOST = HttpPost;
		this.URL = url;
		this.TYPE = type;
		this.SERVICENAME = ServiceName;
		// listenerTemp = listener;
		// //Log.w("TAG","CONTEXT::::1:::::"+_context);

		try {
			/*************************** INTERNET ********************************/
			checkInternet = new Method(_context);

			// //Log.w("TAG","CHECK_INTERNET"+checkInternet.isConnected());

			if (!checkInternet.isConnected()) {
				listener.onError("No Internet Connection Available",
						ServiceName);
				return;
			}

			/*************************** INTERNET ********************************/
		}

		catch (Exception e) {

		}

		dbd = new DataBaseDetails(_context);

		try {
			dbd.Open();
			Cursor c1;
			c1 = dbd.fetchBaseurl();
			while (c1.moveToNext()) {
				// Apiurls.KIT19_BASE_URL=c1.getString(1); // comment on
				// 18March2016
			}
			dbd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Start AsyncExecuter by which we run service and take response from
		// service
		(new AsyncExecuter()).execute();

	}

	// LOGIN --01 Login.geturl
	public static class Login {

		public static final String SERVICE = "LoginApp";
		public static final String ALREADY_LOGIN = "AlreadyLogin";
		public static final String PARAM_USER_LOGIN = "Mobile";
		public static final String PARAM_PASSWORD = "Password";
		public static final String USER_CHOISE = "UserChoice";
		public static final String DEVICE_ID = "DeviceID";
		public static final String VERSION = "Version";

		// http://sms19.com/NewService.aspx?Page=LoginApp&Mobile=9871981524&Password=1234

		public static String geturl(String username, String password,
				int relogin, String value, String DeviceID, String Version) {
			String URL = Apiurls.KIT19_BASE_URL + SERVICE + AND
					+ PARAM_USER_LOGIN + EQU + username + AND + PARAM_PASSWORD
					+ EQU + password;
			if (relogin == 0) {
				URL = Apiurls.KIT19_BASE_URL + SERVICE + AND + PARAM_USER_LOGIN
						+ EQU + username + AND + PARAM_PASSWORD + EQU
						+ password + AND + DEVICE_ID + EQU + DeviceID + AND
						+ VERSION + EQU + Version;
			} else {
				URL = Apiurls.KIT19_BASE_URL + ALREADY_LOGIN + AND
						+ PARAM_USER_LOGIN + EQU + username + AND
						+ PARAM_PASSWORD + EQU + password + AND + USER_CHOISE
						+ EQU + value + AND + DEVICE_ID + EQU + DeviceID + AND
						+ VERSION + EQU + Version;

			}
			return URL;

		}

	}

	// GET_USER_PROFILE_DETAILS --03
	public static class GetUserProfileDetail {
		// http://sms19.com/NewService.aspx?Page=GetUserProfileDetail&userid=1234
		public static final String SERVICE = "GetUserProfileDetail";
		public static final String PARAM_UID = "userid";

		public static String geturl(String Userid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + PARAM_UID + EQU
					+ Userid;

		}
	}

	// USER_REGISTRATION --04
	public static class UserRegistration {
		// http://sms19.com/NewService.aspx?Page=UserRegistationApp&Name=9871981524&Mobile=1234&Email=1234&LoginType=1234&MerchantCode=1234
		public static final String SERVICE = "UserRegistationApp";
		public static final String UNAME = "Name";
		public static final String MOBLE = "Mobile";
		public static final String EMAIL = "Email";
		public static final String LoginType = "LoginType";
		public static final String MERCHANTCODE = "MerchantCode";

		public static String geturl(String username, String Mobilenum,
				String email, String logintype, String mercode) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + UNAME + EQU
					+ username + AND + MOBLE + EQU + Mobilenum + AND + EMAIL
					+ EQU + email + AND + LoginType + EQU + logintype + AND
					+ MERCHANTCODE + EQU + mercode;

		}

	}

	// FORGOT_PASSWORD --05
	public static class Forgotpassword {
		// http://sms19.com/NewService.aspx?Page=ForgotPasswordApp&Mobile=9871981524
		public static final String SERVICE = "ForgotPasswordApp";
		public static final String MOBLE = "Mobile";

		public static String geturl(String Mobile) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + MOBLE + EQU
					+ Mobile;

		}
	}

	// EDIT_PROFILE --06
	public static class EditUserProfile {
		// http://sms19.com/NewService.aspx?Page=UpdateUserProfileDetail&Mobile=9654469382&Email=rockstarvai@gmail.com&Name=RockStar&Userid=19762&Pincode=110011&Gender=Male&Address=abcd&Merchant_Code=1234&Merchant_Url=abc@xyz.com&User_DOB=16-07-2015
		// http://sms19.com/NewService.aspx?Page=UpdateUserProfileDetail&Mobile=9871981524&Email=1234&Name=1234&Userid=1234&Pincode=1234&Gender=1234&Address=1234
		public static final String SERVICE = "UpdateUserProfileDetail";
		public static final String MOB = "Mobile";
		public static final String EMAIL = "Email";
		public static final String NAME = "Name";
		public static final String PINCODE = "Pincode";
		public static final String GEN = "Gender";
		public static final String UID = "Userid";
		public static final String ADDRESS = "Address";
		public static final String Merchant_CODE = "Merchant_Code";
		public static final String MERCHANT_URL = "Merchant_Url";
		public static final String HOME_URL = "Home_Url";
		public static final String DOB = "User_DOB";

		public static String geturl(String mobile, String email, String Name,
				String Userid, String pincode, String Gender, String Adrs,
				String mercode, String websiteadd, String dob) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + MOB + EQU + mobile
					+ AND + EMAIL + EQU + email + AND + NAME + EQU + Name + AND
					+ UID + EQU + Userid + AND + PINCODE + EQU + pincode + AND
					+ GEN + EQU + Gender + AND + ADDRESS + EQU + Adrs + AND
					+ Merchant_CODE + EQU + mercode + AND + MERCHANT_URL + EQU
					+ websiteadd + AND + DOB + EQU + dob;
		}
	}

	// GET_USER_TEMPLATE_LIST --07
	public static class GetUserTemplateList {
		// http://sms19.com/NewService.aspx?Page=GetUserTemplateList&UserId=1234
		public static final String SERVICE = "GetUserTemplateList";
		// public static final String SERVICE = "GetUserAllTemplateListDetail";
		public static final String UID = "UserId";

		public static String geturl(String userid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + UID + EQU + userid;
		}
	}

	// TRANSACTION_DETAILS --08
	public static class TransactionDetail {
		// http://sms19.com/NewService.aspx?Page=GetUserTransactionDetails&Mobile=9871981524&Userid=1222

		public static final String SERVICE = "GetUserTransactionDetails";
		public static final String MOBILE = "Mobile";
		public static final String UID = "Userid";

		public static String geturl(String mobile, String uid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + MOBILE + EQU
					+ mobile + AND + UID + EQU + uid;
		}
	}

	// GET_TEMPLATE_LIST_BY_NAME --09
	public static class GetUserTemplateListByTempName {

		// http://sms19.com/NewService.aspx?Page=GetUserTemplateListByTempName&Userid=1&TemplateName=HELLO
		public static final String SERVICE = "GetUserTemplateListByTempName";
		public static final String UID = "Userid";
		public static final String TEMNAME = "TemplateName";

		public static String geturl(String uid, String templatename) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + UID + EQU + uid
					+ AND + TEMNAME + EQU + templatename;

		}
	}

	// TRANSACTION_DETAILS_BY_DATE --10
	public static class TransactionDetailsByDate {

		// http://sms19.com/NewService.aspx?Page=GetUserTransactionDetailsByDate&Mobile=9910930090&From=02/07/2014&To=23/03/2015&Userid=1

		public static final String SERVICE = "GetUserTransactionDetailsByDate";
		public static final String MOBILE = "Mobile";
		public static final String FROM_DATE = "From";
		public static final String TO_DATE = "To";
		public static final String USER_ID = "Userid";

		public static String geturl(String Mobile, String FromDate,
				String ToDate, String UserId) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + MOBILE + EQU
					+ Mobile + AND + FROM_DATE + EQU + FromDate + AND + TO_DATE
					+ EQU + ToDate + AND + USER_ID + EQU + UserId;
		}
	}

	// SEND_SMS --11

	public static class SendSms {

		// http://sms19.com/NewService.aspx?Page=SendSms&TempId=&Mobile=9582116782&Password=0123&UserId=20717
		// &SenderId=SMSSMS&recipient=9643853169&msgcontent=hello&usermobile=9582116782
		// sms19.com/NewService.aspx?Page=SendSms&TempId=&Mobile=&Password=&UserId=335&SenderId=&recipient=9654469382,9654469384,9835236908&msgcontent=Ratna..11-08-2015-04:21..Testing..&usermobile=&GroupName=&RecipientUserID=
		public static final String SERVICE = "SendSms";
		public static final String TEMID = "tempid";
		public static final String MOBILE = "mobile";
		public static final String PASSWORD = "Password";
		public static final String USERID = "userid";
		public static final String SENDERID = "senderid";
		public static final String RECIPIENT = "recipient";
		public static final String MESSAGECONTENT = "msgcontent";
		public static final String USERMOBILE = "usermobile";
		public static final String GROUPNAME = "GroupName";
		public static final String RECIPIENT_UID = "RecipientUserID";

		// string tempid, string mobile, string Password, string userid, string
		// senderid, string recipient, string msgcontent, string usermobile,
		// string GroupName, string RecipientUserID)

		public static String geturl(String temid, String mobile,
				String password, String userid, String senderid,
				String recipient, String contentmsg, String usermobile,
				String GroupName, String recid) {
			if (temid.length() > 0) {
				contentmsg = "";
			}

			final String mUrl = Apiurls.KIT19_BASE_URL + SERVICE + AND + TEMID
					+ EQU + temid + AND + MOBILE + EQU + mobile + AND
					+ PASSWORD + EQU + password + AND + USERID + EQU + userid
					+ AND + SENDERID + EQU + "SMSSMS" + AND + RECIPIENT + EQU
					+ recipient + AND + MESSAGECONTENT + EQU + contentmsg + AND
					+ USERMOBILE + EQU + usermobile + AND + GROUPNAME + EQU
					+ GroupName + AND + RECIPIENT_UID + EQU + recid;

			return mUrl;
		}

	}

	// SEND_SMS_REPORT --12
	public static class SendSmsTodayRpt {
		// http://sms19.com/NewService.aspx?Page=GetSmsReportToday&Senderid=SMSSMS&Userid=19762
		public static final String SERVICE = "GetSmsReportToday";
		public static final String SENDERID = "Senderid";
		public static final String UID = "Userid";

		public static String geturl(String senderid, String Userid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + SENDERID + EQU
					+ "SMSSMS" + AND + UID + EQU + Userid;
		}
	}

	public static class FetchDeliveryReportApi {
		// http://kitever.com/NewService.aspx?Page=FetchDeliveryReport&User_Id=10&Date=05/12/1984
		public static final String SERVICE = "FetchDeliveryReport";
		public static final String UID = "User_Id";

		public static String geturl(String date, String Userid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + UID + EQU + Userid
					+ AND + "Date" + EQU + date;
			// http://kitever.com/NewService.aspx?Page="FetchDeliveryReport"&=smssms
		}
	}

	public static class FetchDeliveryReportMailApi {
		// http://kitever.com/NewService.aspx?Page=FetchMailDeliveryReport&User_Id=10&Date=05/12/1984
		public static final String SERVICE = "FetchMailDeliveryReport";
		public static final String UID = "User_Id";

		public static String geturl(String date, String Userid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + UID + EQU + Userid
					+ AND + "Date" + EQU + date;
			// http://kitever.com/NewService.aspx?Page="FetchDeliveryReport"&=smssms
		}
	}

	public static class SmsDeliverReportDownload {
		public static final String SERVICE = "TodayDeliveryReport";
		public static final String UID = "username";

		public static String geturl(String password, String userName,
				String toDate, String fromDate) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + UID + EQU
					+ userName + AND + "password=" + password + AND
					+ "fromdate" + EQU + fromDate + AND + "todate=" + toDate;

			// return
			// "http://www.kitever.com/TodayDeliveryReport.aspx?username=919810398913&password=14742&fromdate=10/07/2016&todate=29/07/2016";

		}
	}

	// SEND_SMS_SCHEDULE_TIME --13
	public static class SendSmsSchedule {
		// http://kitever.com/NewService.aspx?Page=FetchDeliveryReport&Userid=3&Date=05/12/1984
		// http://sms19.com/NewService.aspx?Page=SheduledSms&TempId=18110&Mobile=9582116782&Password=123
		// &UserId=20717&SenderId=SMSSMS&recipient=9643853169,8527805413&sheduleddate=2015-04-14
		// &sheduledtime=02:38 PM&msgcontent=jkh&usermobile=9582116782

		// sms19.com/NewService.aspx?Page=SheduledSms&TempId=&Mobile=sms19&Password=MyFault&UserId=335
		// &SenderId=Demo&recipient=9654469382,9654469384,9835236908&sheduleddate=11/08/2015&sheduledtime=08:00PM
		// &msgcontent=Ratna..11-08-2015-08:00..Testing..&usermobile=8447735671&GroupName=Shalu&RecipientUserID=310,311,312

		public static final String SERVICE = "SheduledSms";
		public static final String TEM_ID = "TempId";
		public static final String SCHEDULE_DATE = "sheduleddate";
		public static final String SCHEDULE_TIME = "sheduledtime";
		public static final String PASSWORD2 = "Password";
		public static final String MOBILE = "Mobile";
		public static final String USERID = "UserId";
		public static final String SENDERID = "SenderId";
		public static final String RECIPENT = "recipient";
		public static final String CONTENTMSG = "msgcontent";
		public static final String USERMOBILE = "usermobile";
		public static final String GROUPNAME = "GroupName";
		public static final String RecUserID = "RecipientUserID";

		public static String geturl(String TempId, String mobile,
				String password, String UserID, String Recipent,
				String ScheduleDate, String ScheduleTime, String msg,
				String usermobile, String GroupName, String recid) {
			if (TempId.length() > 0) {
				msg = "";
			}

			recid = "";

			return Apiurls.KIT19_BASE_URL + SERVICE + AND + TEM_ID + EQU
					+ TempId + AND + MOBILE + EQU + mobile + AND + PASSWORD2
					+ EQU + password + AND + USERID + EQU + UserID + AND
					+ SENDERID + EQU + "SMSSMS" + AND + RECIPENT + EQU
					+ Recipent + AND + SCHEDULE_DATE + EQU + ScheduleDate + AND
					+ SCHEDULE_TIME + EQU + ScheduleTime + AND + CONTENTMSG
					+ EQU + msg + AND + USERMOBILE + EQU + usermobile + AND
					+ GROUPNAME + EQU + GroupName + AND + RecUserID + EQU
					+ recid;
		}

	}

	// BEFORE1DAY_SEND_SMS_REPORT --14
	public static class SendSmsOneRpt {
		// http://sms19.com/NewService.aspx?Page=GetSmsReportBeforeoneday&Senderid=SMSSMS&Userid=19762
		
		public static final String SERVICE = "GetSmsReportBeforeoneday";
		public static final String SENDERID = "Senderid";
		public static final String UID = "Userid";

		public static String geturl(String senderid, String Userid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + SENDERID + EQU
					+ "SMSSMS" + AND + UID + EQU + Userid;
		}
	}

	// BEFORE2DAY_SEND_SMS_REPORT --15

	public static class SendSmsTwoRpt {
		// http://sms19.com/NewService.aspx?Page=GetSmsReportBeforetwoday&Senderid=SMSSMS&Userid=19762

		public static final String SERVICE = "GetSmsReportBeforetwoday";
		public static final String SENDERID = "Senderid";
		public static final String UID = "Userid";

		public static String geturl(String senderid, String Userid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + SENDERID + EQU
					+ "SMSSMS" + AND + UID + EQU + Userid;
		}
	}

	// BEFORE3DAY_SEND_SMS_REPORT --16

	public static class SendSmsThreeRpt {

		// http://sms19.com/NewService.aspx?Page=GetSmsReportBeforethreeday&Senderid=SMSSMS&Userid=19762
		public static final String SERVICE = "GetSmsReportBeforethreeday";
		public static final String SENDERID = "Senderid";
		public static final String UID = "Userid";

		public static String geturl(String senderid, String Userid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + SENDERID + EQU
					+ "SMSSMS" + AND + UID + EQU + Userid;
		}
	}

	// BEFORE4DAY_SEND_SMS_REPORT --17

	public static class SendSmsFourRpt {
		// //http://sms19.com/NewService.aspx?Page=GetSmsReportBeforefourday&Senderid=SMSSMS&Userid=19762
		public static final String SERVICE = "GetSmsReportBeforefourday";
		public static final String SENDERID = "Senderid";
		public static final String UID = "Userid";

		public static String geturl(String senderid, String Userid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + SENDERID + EQU
					+ "SMSSMS" + AND + UID + EQU + Userid;
		}
	}

	// BEFORE5DAY_SEND_SMS_REPORT --18

	public static class SendSmsFiveRpt {
		// http://sms19.com/NewService.aspx?Page=GetSmsReportBeforefiveday&Senderid=SMSSMS&Userid=19762
		public static final String SERVICE = "GetSmsReportBeforefiveday";
		public static final String SENDERID = "Senderid";
		public static final String UID = "Userid";

		public static String geturl(String senderid, String Userid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + SENDERID + EQU
					+ "SMSSMS" + AND + UID + EQU + Userid;
		}
	}

	// //BEFORE6DAY_SEND_SMS_REPORT --19

	public static class SendSmsSixRpt {
		// //http://sms19.com/NewService.aspx?Page=GetSmsReportBeforesixday&Senderid=SMSSMS&Userid=19762
		public static final String SERVICE = "GetSmsReportBeforesixday";
		public static final String SENDERID = "Senderid";
		public static final String UID = "Userid";

		public static String geturl(String senderid, String Userid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + SENDERID + EQU
					+ "SMSSMS" + AND + UID + EQU + Userid;
		}
	}

	// INSERT_USER_SELECTED_PACKAGE --20
	public static class selectPackage {

		// http://sms19.com/NewService.aspx?Page=InsertpackageDetail&Userid=19762&Package=8527805413

		public static final String SERVICE = "InsertpackageDetail";
		public static final String USER_ID = "Userid";
		public static final String PACKAGE = "Package";

		public static String geturl(String userId, int packageInfo) {

			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USER_ID + EQU
					+ userId + AND + PACKAGE + EQU + packageInfo;
		}

	}

	// GET_INFORMATION_FOR_PAYMENT_GATEWAY --21
	public static class getDetailsForPayment {
		// http://sms19.com/NewService.aspx?Page=getpackageDetail&Userid=19762&Mobile=8527805413

		public static final String SERVICE = "getpackageDetail";
		public static final String USERID = "Userid";
		public static final String MOBILE = "Mobile";

		public static String geturl(String UserId, String MobileNumber) {

			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ UserId + AND + MOBILE + EQU + MobileNumber;
		}

	}

	// Insert_Single_Contact --22
	public static class InsertIndividualContact {
		// http://sms19.com/NewService.aspx?Page=InsertIndividualContact&Userid=19762&contactName=raj&contactMobile=43432543&contactDOB=10/12/2015&contactAnniversary=14/12/2015

		public static final String SERVICE = "InsertIndividualContact";
		public static final String USERID = "Userid";
		public static final String CON_NAME = "contactName";
		public static final String CON_MOBILE = "contactMobile";
		public static final String CON_DOB = "contactDOB";
		public static final String CON_ANIVERSARY = "contactAnniversary";
		public static final String COUTRY_CODE = "countryCode";

		public static String geturl(String UserId, String conName,
				String coNum, String DOB, String aniversary, String countryCode) {

			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ UserId + AND + CON_NAME + EQU + conName + AND
					+ CON_MOBILE + EQU + coNum + AND + CON_DOB + EQU + DOB
					+ AND + CON_ANIVERSARY + EQU + aniversary + AND
					+ COUTRY_CODE + EQU + countryCode;
		}

	}

	// UPDATE_Single_Contact --23
	public static class UpdateIndividualContact {

		// sms19.com/NewService.aspx?Page=UpdateIndividualContact&Userid=19767&contactName=home&contactMobile=5598885855&contactDOB=
		// &contactAnniversary= &oldmobile=5598885858

		public static final String SERVICE = "UpdateIndividualContact";
		public static final String USERID = "Userid";
		public static final String CONTACT_NAME = "contactName";
		public static final String CONTACT_MOBILE = "contactMobile";
		public static final String CONTACTDOB = "contactDOB";
		public static final String CONTACT_ANIVERSARY = "contactAnniversary";
		public static final String OLD_NUMBER = "oldmobile";

		public static String geturl(String UserId, String conName,
				String coNum, String DOB, String aniversary, String oldMobile) {

			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ UserId + AND + CONTACT_NAME + EQU + conName + AND
					+ CONTACT_MOBILE + EQU + coNum + AND + CONTACTDOB + EQU
					+ DOB + AND + CONTACT_ANIVERSARY + EQU + aniversary + AND
					+ OLD_NUMBER + EQU + oldMobile;
		}
	}

	// DELETE_SINGLE_CONTACT --24
	// http://sms19.com/NewService.aspx?Page=DeleteIndividualContact&Userid=19762&contactMobile=

	public static class DeleteIndividualContact {
		public static final String SERVICE = "DeleteIndividualContact";
		public static final String USERID = "Userid";
		public static final String CONTACT_MOBILE = "contactMobile";

		public static String geturl(String UserId, String conMob) {

			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ UserId + AND + CONTACT_MOBILE + EQU + conMob;
		}
	}

	// GET_ALL_GROUP_DETAILS --25
	public static class GetAllGroupDetails {
		// http://sms19.com/NewService.aspx?Page=GetGroup&userid=19762
		public static final String SERVICE = "GetGroup";
		public static final String USERID = "userid";

		// public static final String CONTACTDETAILS = "contactMobile";

		public static String geturl(String userId) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ userId;
		}
	}

	// GET_ALL_GROUP_DETAILS --26
	public static class GetSenderId {
		// http://sms19.com/NewService.aspx?Page=FetchSenderIDDetail&userid=19762
		public static final String SERVICE = "FetchSenderIDDetail";
		public static final String USERID = "userid";

		// public static final String CONTACTDETAILS = "contactMobile";

		public static String geturl(String userId) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ userId;
		}
	}
	// InsertGroupContact --27
	// http://sms19.com/NewService.aspx?Page=InsertGroupContact&Userid=19762&contactName=raj&contactMobile=43432543&contactDOB=10/12/2015&contactAnniversary=14/12/2015&GroupName=

	public static class InsertGroupContact {
		public static final String SERVICE = "InsertGroupContact";
		public static final String USERID = "Userid";
		public static final String CONTACT_NAME = "contactName";
		public static final String CONTACT_MOBILE = "contactMobile";
		public static final String CONTACT_DOB = "contactDOB";
		public static final String GROUP_NAME = "GroupName";
		public static final String CONTACT_ANIV = "contactAnniversary";

		public static String geturl(String uid, String contactpersonname,
				String contactNumber, String conDob, String conAni,
				String GroupNam) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU + uid
					+ AND + CONTACT_NAME + EQU + contactpersonname + AND
					+ CONTACT_MOBILE + EQU + contactNumber + AND + CONTACT_DOB
					+ EQU + conDob + AND + CONTACT_ANIV + EQU + conAni + AND
					+ GROUP_NAME + EQU + GroupNam;
		}
	}

	// GET_ALL_CONTACT --28
	public static class GetAllContact {
		// http://sms19.com/NewService.aspx?Page=GetContactByUserid&Userid=19762

		public static final String SERVICE = "GetContactByUserid";
		public static final String USERID = "Userid";

		public static String geturl(String userId) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ userId;
		}

	}

	// UPDATE_GROUP_CONTACT --29
	public static class UpdateGroupContact {
		// http://sms19.com/NewService.aspx?Page=UpdateGroupContact&Userid=19762&contactName=raj&contactMobile=43432543&contactDOB=10/12/2015&contactAnniversary=14/12/2015&GroupName=

		public static final String SERVICE = "UpdateGroupContact";
		public static final String USERID = "Userid";
		public static final String GROUP_NAME = "Contactid";
		public static final String CONTACTNAME = "contactName";
		public static final String CONTACTMOBILE = "contactMobile";
		public static final String CONTACTDOB = "contactDOB";
		public static final String CONTACTANNIVERSRY = "contactAnniversary";

		public static String geturl(String userId, String ContactName,
				String ContactMobile, String ContactDOB, String COntactAnni,
				String GroupNam) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ userId + AND + CONTACTNAME + EQU + ContactName + AND
					+ CONTACTMOBILE + EQU + ContactMobile + AND + CONTACTDOB
					+ EQU + ContactDOB + AND + CONTACTANNIVERSRY + EQU
					+ COntactAnni + AND + GROUP_NAME + EQU + GroupNam;
		}

	}

	// DELETE_GROUP_CONTACT --30
	public static class DeleteGroupContact {
		// http://sms19.com/NewService.aspx?Page=DeleteGroupContact&Userid=19762&GroupName=Demo&contactMobile=1144552255

		public static final String SERVICE = "DeleteGroupContact";
		public static final String USERID = "Userid";
		public static final String GNAME = "GroupName";
		public static final String CONTACTMobile = "contactMobile";

		public static String geturl(String userId, String GroupNam,
				String ContactMobile) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ userId + AND + GNAME + EQU + GroupNam + AND
					+ CONTACTMobile + EQU + ContactMobile;
		}

	}

	// GET_GROUP_CONTACT --31
	public static class GetGroupContact {
		// http://sms19.com/NewService.aspx?Page=GetGroupContact&userid=19762&GroupName=Filmy

		public static final String SERVICE = "GetGroupContact";
		public static final String USERID = "Userid";
		public static final String GROUPNAME = "GroupName";

		public static String geturl(String userId, String Groupname) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ userId + AND + GROUPNAME + EQU + Groupname;
		}

	}

	// GET_ALL_TEMPLATE --32
	public static class getALLTemplate {
		//
		public static final String SERVICE = "getALLTemplate";
		public static final String USERID = "Userid";

		public static String geturl(String userId) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ userId;
		}
	}

	// Check Email Status --33
	public static class checkEmailStatus {
		// http://sms19.com/NewService.aspx?Page=GetUserListUsingEmail&EmailId=abc@gmail.com
		public static final String SERVICE = "GetUserListUsingEmail";
		public static final String EMAIL = "EmailId";

		public static String geturl(String email) {

			return Apiurls.KIT19_BASE_URL + SERVICE + AND + EMAIL + EQU + email;
		}
	}

	// Get Top Current balance --34
	public static class getCurrentBalance {
		// http://test.kitever.com/NewService.aspx?Page=GetUserTopTransaction&Userid=1041
		public static final String SERVICE = "GetUserTopTransaction";
		public static final String UserID = "Userid";

		public static String geturl(String userid) {

			return Apiurls.KIT19_BASE_URL + SERVICE + AND + UserID + EQU
					+ userid;

		}
	}

	// Get All Templates --35
	// http://sms19.com/NewService.aspx?Page=GetUserAllTemplateListDetail&Userid=19762

	public static class GetUserAllTemplateListDetail {
		public static final String SERVICE = "GetUserAllTemplateListDetail";
		public static final String USID = "Userid";

		public static String geturl(String UID) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USID + EQU + UID;
		}
	}

	// FETCH ALL SCHEDULE LIST --36
	// http://sms19.com/NewService.aspx?Page=SheduledSmsList&UserId=17626&FromDate=&ToDate=
	public static class SheduledSmsList {
		public static final String SERVICE = "SheduledSmsList";
		public static final String USID = "UserId";
		public static final String FROM_D = "FromDate";
		public static final String TO_DATE = "ToDate";

		public static String geturl(String UID, String FROM, String TODATE) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USID + EQU + UID
					+ AND + FROM_D + EQU + FROM + AND + TO_DATE + EQU + TODATE;
		}
	}

	public static class SheduledMailList {
		public static final String SERVICE = "SheduledMailList";
		public static final String USID = "UserId";
		public static final String FROM_D = "FromDate";
		public static final String TO_DATE = "ToDate";

		public static String geturl(String UID, String FROM, String TODATE) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USID + EQU + UID
					+ AND + FROM_D + EQU + FROM + AND + TO_DATE + EQU + TODATE;
		}
	}

	// Delete schedule list --37
	// http://sms19.com/NewService.aspx?Page=DeleteSheduledSms&Mobile=9582116782&Password=02101&sheduleddate=2015-04-15&sheduledtime=01:50
	// PM
	public static class DeleteSheduledSms {
		public static final String SERVICE = "DeleteSheduledSms";
		public static final String MOBILE = "Mobile";
		public static final String PASSWORD = "Password";
		public static final String SCHEDULEDATE = "sheduleddate";
		public static final String SCHEDULETIME = "sheduledtime";

		public static String geturl(String mobile, String passwd, String date,
				String time) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + MOBILE + EQU
					+ mobile + AND + PASSWORD + EQU + passwd + AND
					+ SCHEDULEDATE + EQU + date + AND + SCHEDULETIME + EQU
					+ time;
		}
	}
	
	public static class DeleteSheduledMail {
		public static final String SERVICE = "DeleteSheduledMail";
		public static final String MOBILE = "Mobile";
		public static final String PASSWORD = "Password";
		public static final String SCHEDULEDATE = "sheduleddate";
		public static final String SCHEDULETIME = "sheduledtime";

		public static String geturl(String mobile, String passwd, String date,
				String time) {
			Log.i("result","request : "+Apiurls.KIT19_BASE_URL + SERVICE + AND + MOBILE + EQU
					+ mobile + AND + PASSWORD + EQU + passwd + AND
					+ SCHEDULEDATE + EQU + date + AND + SCHEDULETIME + EQU
					+ time);
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + MOBILE + EQU
					+ mobile + AND + PASSWORD + EQU + passwd + AND
					+ SCHEDULEDATE + EQU + date + AND + SCHEDULETIME + EQU
					+ time;
		}
	}

	// delete group by group name --38
	// http://sms19.com/NewService.aspx?Page=DeleteGroup&Userid=19762&GroupName=Cricket
	public static class DeleteGroupAll {
		public static final String SERVICE = "DeleteGroup";
		public static final String USERID = "Userid";
		public static final String GROUPNAM = "GroupName";

		public static String geturl(String userID, String Groupame) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ userID + AND + GROUPNAM + EQU + Groupame;
		}
	}

	// http://sms19.com/NewService.aspx?Page=FetchMobilePackage --39
	public static class FetchMobilePackage {
		public static final String SERVICE = "FetchMobilePackage";

		public static String geturl() {
			return Apiurls.KIT19_BASE_URL + SERVICE;
		}
	}

	// http://sms19.com/NewService.aspx?Page=UndeliveredMessageForRecipientCount&RecipientUserID=20705
	// --40
	public static class UndeliveredMessageForRecipientCount {
		public static final String SERVICE = "UndeliveredMessageForRecipientCount";
		public static final String USERID = "RecipientUserID";

		public static String geturl(String RecipientUserID) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ RecipientUserID;
		}
	}

	// http://sms19.com/NewService.aspx?Page=MessageReceivedToRecipient&RecipientUserID=20705
	// --41

	public static class MessageReceivedToRecipient {
		public static final String SERVICE = "MessageReceivedToRecipient";
		public static final String USERID = "RecipientUserID";

		public static String geturl(String RecipientUserID) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ RecipientUserID;
		}
	}

	// http://sms19.com/NewService.aspx?
	// Page=TotalMessagesOfSingleSerderUser&SenderUserID=20717&RecipientUserID=20705
	// --42
	public static class TotalMessagesOfSingleSenderUser {
		public static final String SERVICE = "TotalMessagesOfSingleSenderUser";
		public static final String USERID = "SenderUserID";
		public static final String RECIPIENTUID = "RecipientUserID";

		public static String geturl(String SenderUserID, String RecipientUserID) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ SenderUserID + AND + RECIPIENTUID + EQU + RecipientUserID;
		}
	}

	// --43
	/*
	 * http://sms19.com/NewService.aspx?Page=MessageSendToRecipient&GroupName=
	 * Abhi12345&TempId=&Mobile=9582116782
	 * &Password=123&UserId=20705&SenderId=SMSSMS&recipient=9643853169
	 * &msgcontent
	 * =kya&usermobile=9582116782&RecipientUserId=20717&CheckUncheck=0
	 */

	public static class MessageSendToRecipient {
		public static final String SERVICE = "MessageSendToRecipient";
		public static final String TemID = "TempId";
		public static final String MOBILE = "Mobile";
		public static final String Password = "Password";
		public static final String UserId = "UserId";
		public static final String SenderId = "SenderId";
		public static final String RECIpient = "recipient";
		public static final String msgcontent = "msgcontent";
		public static final String usermobile = "usermobile";
		public static final String RECIpientUserId = "RecipientUserId";
		public static final String CheckedStatus = "CheckUncheck";
		public static final String GROUPNAME = "GroupName";

		public static String geturl(String temid, String InAppUserlogin,
				String InAppPassword, String InAppUserid, String SenderID,
				String recipient, String msg, String InAppUsermobile,
				String RecipientUserId, String check, String gpname) {

			RecipientUserId = "";

			return Apiurls.KIT19_BASE_URL + SERVICE + AND + GROUPNAME + EQU
					+ gpname + AND + TemID + EQU + temid + AND + MOBILE + EQU
					+ InAppUserlogin + AND + Password + EQU + InAppPassword
					+ AND + UserId + EQU + InAppUserid + AND + SenderId + EQU
					+ SenderID + AND + RECIpient + EQU + recipient + AND
					+ msgcontent + EQU + msg + AND + usermobile + EQU
					+ InAppUsermobile + AND + RECIpientUserId + EQU
					+ RecipientUserId + AND + CheckedStatus + EQU + check;
		}
	}

	// sms19.com/NewService.aspx?Page=ReferFriend&Mobile=9643853169&Password=1234&UserId=20717&SenderId=PELSFT&recipient=9654469382,9654469384,9999999999&usermobile=9910930090&RecipientName=Ratna,Ravish,Ayushi
	// http://sms19.com/NewService.aspx?Page=ReferFriend&Mobile=9910930090&Password=krishnahare2011&UserId=1&SenderId=PELSFT&recipient=9654469382,9654469384,9999999999&usermobile=9910930090&RecipientName=Ratna,Ravish,Ayushi
	public static class ReferFriend {
		public static final String SERVICE = "ReferFriend";
		public static final String TemID = "TempId";
		public static final String MOBILE = "Mobile";
		public static final String Password = "Password";
		public static final String UserId = "UserId";
		public static final String SenderId = "SenderId";
		public static final String RECIpient = "recipient";
		public static final String usermobile = "usermobile";
		public static final String RECIPIENT_NAME = "RecipientName";

		public static String geturl(String InAppUserlogin,
				String InAppPassword, String InAppUserid, String SenderID,
				String recipient, String InAppUsermobile, String RECNAME) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + MOBILE + EQU
					+ InAppUserlogin + AND + Password + EQU + InAppPassword
					+ AND + UserId + EQU + InAppUserid + AND + SenderId + EQU
					+ SenderID + AND + RECIpient + EQU + recipient + AND
					+ usermobile + EQU + InAppUsermobile + AND + RECIPIENT_NAME
					+ EQU + RECNAME;
		}
	}

	// GET_ALL_CONTACT_Chat --45
	public static class GetAllContactchat {
		// http://sms19.com/NewService.aspx?Page=GetContactByUserid&Userid=19762

		public static final String SERVICE = "GetContactByUserid";
		public static final String USERID = "Userid";

		public static String geturl(String userId) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ userId;
		}

	}

	// OPT_IN_OUT ---46
	// http://sms19.com/NewService.aspx?Page=OptInOut&SenderUserID=20717&RecipientUserID=20705&OptOption=0&SenderMobileNo=9643853169
	public static class OptInOut {
		public static final String SERVICE = "OptInOut";
		public static final String SENDER_USERID = "SenderUserID";
		public static final String RECIPIENT_USERID = "RecipientUserID";
		public static final String OPT_IN_OUT = "OptOption";
		public static final String SENDER_MOBILE = "SenderMobileNo";

		public static String geturl(String senuid, String recid, String optout,
				String sendermoblie) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + SENDER_USERID + EQU
					+ recid + AND + RECIPIENT_USERID + EQU + senuid + AND
					+ OPT_IN_OUT + EQU + optout + AND + SENDER_MOBILE + EQU
					+ sendermoblie;

		}

	}

	// Broadcast Notification
	// http://sms19.com/NewService.aspx?Page=GetBroadcastPagetype&RecipientUserID=19762--47
	public static class bNotification {
		public static final String SERVICE = "GetBroadcastPagetype";
		public static final String RECIPIENT_UID = "RecipientUserID";

		public static String geturl(String userId) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + RECIPIENT_UID + EQU
					+ userId;
		}
	}

	// send message group (send message inbox) --48
	public static class getSendMsgGroup {
		// http://sms19.com/NewService.aspx?Page=GetGroup&userid=19762
		public static final String SERVICE = "GetGroup";
		public static final String USERID = "userid";
		public static final String CONTACTDETAILS = "contactMobile";

		public static String geturl(String userId) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USERID + EQU
					+ userId;
		}

	}

	// sms19.com/NewService.aspx?Page=ProfilePicture&SenderUserID=20705&UrlPath=http://sms19.com/AppFolder/ProfilePicture/20705.jpeg
	// --49
	public static class ProfilePic {
		public static final String SERVICE = "ProfilePicture";
		public static final String SENDERUSERID = "SenderUserID";
		public static final String BASESTRING = "UrlPath";

		public static String geturl(String senid, String image) {
			String string = Apiurls.KIT19_BASE_URL + SERVICE + AND
					+ SENDERUSERID + EQU + senid + AND + BASESTRING + EQU
					+ image;
			Utils.printLog("Image_url===" + string);
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + SENDERUSERID + EQU
					+ senid + AND + BASESTRING + EQU + image;

		}
	}

	// http://sms19.com/NewService.aspx?Page=GetMerchantDetails&MerchantCode=1234
	// --50
	public static class SplashScreenImage {
		public static final String SERVICE = "GetMerchantDetails";
		public static final String MERCHANTCODE = "MerchantCode";

		public static String geturl(String merchantCode) {
			try {
				if (merchantCode == null) {
					merchantCode = "";
				}
			} catch (Exception e) {
				merchantCode = "";
			}
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + MERCHANTCODE + EQU
					+ merchantCode;

		}
	}

	// http://sms19.com/NewService.aspx?Page=GetFTPHostDetail&UserID=20705&FileType=image
	// --51
	public static class GetFTPHostDetail {
		public static final String SERVICE = "GetFTPHostDetail";
		public static final String FILE_TYPE = "FileType";

		public static String geturl(String filetype) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + FILE_TYPE + EQU
					+ filetype;

		}
	}

	public static class GetFTPHostDetail2 {
		public static final String SERVICE = "GetFTPHostDetail";
		public static final String FILE_TYPE = "FileType";
		public static final String USER_ID = "UserId";

		public static String geturl(String userId, String filetype) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USER_ID + EQU
					+ userId + AND + FILE_TYPE + EQU + filetype;
		}
	}
		// --52
	public static class Fetchurl {
		// sms19.com/NewService.aspx?Page=AutomatedServicesURL&UserId=20705
		public static final String SERVICE = "AutomatedServicesURL";
		public static final String ID_DATA = "UserId";

		public static String geturl(String uid) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + ID_DATA + EQU + uid;
		}

	}

	// new audio video service --53

	// http://sms19.com/NewService.aspx?Page=AudioVideoPictureMessage&GroupName=Abhi12345,SHALU&tempid=&LoginID=&Password=&userid=310&senderid=&recipient=&msgcontent=ABC&usermobile=&RecipientUserID=312&CheckUncheck=0&base64String=R0lGODlhDwAPAKECAAAAzMzM/////wAAACwAAAAADwAPAAACIISPeQHsrZ5ModrLlN48CXF8m2iQ3YmmKqVlRtW4MLwWACH%2BH09wdGltaXplZCBieSBVbGVhZCBTbWFydFNhdmVyIQAAOw==&FileType=Image&FileName=Robotic
	public static class AudioVideoPictureMessage {
		public static final String SERVICE = "AudioVideoPictureMessage";
		public static final String GROUP_NAME = "GroupName";
		public static final String TEMID = "tempid";
		public static final String LOGIN = "LoginID";
		public static final String PASSWORD = "Password";
		public static final String UID = "userid";
		public static final String SENDER_ID = "senderid";
		public static final String RECIPIENT = "recipient";
		public static final String CHECK_UNCHECK = "CheckUncheck";
		public static final String FILE_TYPE = "FileType";
		public static final String FILE_NAME = "FileName";
		public static final String URL_PATH = "urlPath";
		public static final String RECIPIENT_ID = "RecipientUserID";
		public static final String USER_MOBILE = "usermobile";
		public static final String MESSAGE_CONTENT = "msgcontent";

		// http://sms19.com/NewService.aspx?Page=AudioVideoPictureMessage&GroupName=Abhi12345,SHALU&tempid=&LoginID=&Password=&userid=310&senderid=&recipient=&msgcontent=ABC&usermobile=&RecipientUserID=312&CheckUncheck=0&base64String=R0lGODlhDwAPAKECAAAAzMzM/////wAAACwAAAAADwAPAAACIISPeQHsrZ5ModrLlN48CXF8m2iQ3YmmKqVlRtW4MLwWACH%2BH09wdGltaXplZCBieSBVbGVhZCBTbWFydFNhdmVyIQAAOw==&FileType=Image&FileName=Robotic
		public static String geturl(String GroupName, String temid,
				String loginid, String pwd, String uid, String senderid,
				String recipient, String msgcontent, String usrmobile,
				String recid, String CheckUncheck, String base64String,
				String FileType, String FileName) {
			recid = "";
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + GROUP_NAME + EQU
					+ GroupName + AND + TEMID + EQU + temid + AND + LOGIN + EQU
					+ loginid + AND + PASSWORD + EQU + pwd + AND + UID + EQU
					+ uid + AND + SENDER_ID + EQU + senderid + AND + RECIPIENT
					+ EQU + recipient + AND + MESSAGE_CONTENT + EQU
					+ msgcontent + AND + USER_MOBILE + EQU + usrmobile + AND
					+ RECIPIENT_ID + EQU + recid + AND + CHECK_UNCHECK + EQU
					+ CheckUncheck + AND + URL_PATH + EQU + base64String + AND
					+ FILE_TYPE + EQU + FileType + AND + FILE_NAME + EQU
					+ FileName;

		}
	}

	// --54
	public static class ChangePassword {
		// sms19.com/NewService.aspx?Page=ChangePassword&UserId=20705&OldPassword=20705&NewPassword=20705
		public static final String SERVICE = "ChangePassword";
		public static final String OLD_PWD = "OldPassword";
		public static final String NEW_PWD = "NewPassword";
		public static final String USER_ID = "UserId";

		public static String geturl(String UserId, String oldPassword,
				String NewPassword) {
			return Apiurls.KIT19_BASE_URL + SERVICE + AND + USER_ID + EQU
					+ UserId + AND + OLD_PWD + EQU + oldPassword + AND
					+ NEW_PWD + EQU + NewPassword;
		}

	}

	public class AsyncExecuter extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			// create one string builder by which we can write data from service
			StringBuilder strbuild;

			// ---------------------------------------------------
			// create one object of basic http parameter
			HttpParams httpparams = new BasicHttpParams();

			// time of timeout of service response
			int timeOutConnection = 20000;// 20 sec

			// function by which know about connection timeout
			HttpConnectionParams.setConnectionTimeout(httpparams,
					timeOutConnection);

			// -----------------------------------------------------

			// ----------------------------------------------------
			// time out of socket of device
			int timeOutConncetionSoket = 40000;
			// 20 sec

			// function by which know about socket timeout
			HttpConnectionParams.setSoTimeout(httpparams,
					timeOutConncetionSoket);
			// ----------------------------------------------------

			// making object of defalut httpclient with http parameter
			HttpClient httpclient = new DefaultHttpClient(httpparams);

			HttpResponse httpresponse = null;

			// String responseString = null;

			// check which method is use in service
			try {
				if (TYPE == TYPE_GET) {

					// Log.w("URL", "url::" + "" + URL);

					URL = URL.replaceAll(" ", "+");

					httpresponse = httpclient.execute(new HttpGet(URL));

				} else if (TYPE == TYPE_POST) {

					httpresponse = httpclient.execute(HTTPPOST);

					/*
					 * List<NameValuePair> nameValuePairs =new
					 * ArrayList<NameValuePair>(); nameValuePairs.add(new
					 * BasicNameValuePair("Name","UserRegistationApp" ));
					 * nameValuePairs.add(new
					 * BasicNameValuePair("Mobile",broadcastid ));
					 * nameValuePairs.add(new BasicNameValuePair("Email",userId
					 * )); nameValuePairs.add(new
					 * BasicNameValuePair("LoginType",userId ));
					 * nameValuePairs.add(new
					 * BasicNameValuePair("MerchantCode",userId ));
					 */

				}

				// Log.w("TAG", "Raw Response: " + httpresponse);

				InputStreamReader streamReader = new InputStreamReader(
						httpresponse.getEntity().getContent());
				BufferedReader br = new BufferedReader(streamReader);

				String line;

				strbuild = new StringBuilder();

				while ((line = br.readLine()) != null) {
					strbuild.append(line);
				}
			} catch (Exception e) {

				return null;
			}

			try {
				// Log.w("URL", "Response: " + strbuild.toString());

				return strbuild.toString();
			}

			catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {

			if (null == result) {
				// // when user not find result value..as a

				listener.onError("Please Try Again Later", SERVICENAME);
			} else {
				Gson g = new Gson();

				// LOGIN --01
				if (SERVICENAME == TYPE_LOGIN) {

					try {

						listener.onSuccess(
								g.fromJson(result, LoginModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						// comes due to not getting value by model
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// GET_USER_PROFILE_DETAILS --03
				else if (SERVICENAME == TYPE_GETPROFILE) {

					try {
						listener.onSuccess(
								g.fromJson(result, GetProfileData.class),
								SERVICENAME);
						Utils.printLog(" TYPE_GETPROFILE Start time_logger"
								+ new SimpleDateFormat("hh:mm:ss")
										.format(new Date()) + "");

					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// USER_REGISTRATION --04
				else if (SERVICENAME == TYPE_SIGNUP) {
					try {

						listener.onSuccess(
								g.fromJson(result, UserRegistrationModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// FORGOT_PASSWORD --05
				else if (SERVICENAME == TYPE_FORGOTPASSWD) {

					try {
						listener.onSuccess(
								g.fromJson(result, ForgotPasswordModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// EDIT_PROFILE --06
				else if (SERVICENAME == TYPE_EDIT) {

					try {
						listener.onSuccess(
								g.fromJson(result, EditUmodel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// GET_USER_TEMPLATE_LIST --07
				else if (SERVICENAME == TYPE_TEMPLATE) {

					try {
						listener.onSuccess(
								g.fromJson(result, BindTemplateModel.class),
								SERVICENAME);
						Utils.printLog(" TEMPLETS Start time_logger"
								+ new SimpleDateFormat("hh:mm:ss")
										.format(new Date()) + "");
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// TRANSACTION_DETAILS --08
				else if (SERVICENAME == TYPE_TRANSACTIONDETL) {

					try {
						listener.onSuccess(g.fromJson(result,
								TransactionDetailModel.class), SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// GET_TEMPLATE_LIST_BY_NAME --09
				else if (SERVICENAME == Type_GET_TEM_BY_NM) {

					try {

						listener.onSuccess(
								g.fromJson(result, TemplateListByName.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// TRANSACTION_DETAILS_BY_DATE --10;
				else if (SERVICENAME == TYPE_TRAN_DETAILS_BY_DATE) {
					try {
						listener.onSuccess(
								g.fromJson(result, TransactionListByDate.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// SEND_SMS --11;

				else if (SERVICENAME == TYPE_SEND_MSG) {
					// Log.w("TAG",
					// "::::::::::::::::::::TYPE_SEND_MSG:::::SERVICENAME:"
					// + SERVICENAME);

					try {
						// Log.w("TAG", "::::::::::BEFORE ONSUCCESS:::::result"
						// + result);
						listener.onSuccess(
								g.fromJson(result, SendSmsModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}

				}

				// SEND_SMS_REPORT --12
				else if (SERVICENAME == TYPE_SMS_REPORT) {

					try {
						listener.onSuccess(
								g.fromJson(result, SendSmsReport.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}

				}

				// SEND_SMS_SCHEDULE_TIME --13
				else if (SERVICENAME == TYPE_SEND_MSG_SCHEDULE) {

					try {
						listener.onSuccess(
								g.fromJson(result, ScheduleSmsSend.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// BEFORE1DAY_SEND_SMS_REPORT --14
				else if (SERVICENAME == TYPE_SMS_ONEREPORT) {
					try {
						listener.onSuccess(g.fromJson(result,
								SendSmsReportOneDayModel.class), SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// BEFORE2DAY_SEND_SMS_REPORT --15
				else if (SERVICENAME == TYPE_SMS_TWOREPORT) {
					try {
						listener.onSuccess(g.fromJson(result,
								SendSmsReportTwoDayModel.class), SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// BEFORE3DAY_SEND_SMS_REPORT --16
				else if (SERVICENAME == TYPE_SMS_THREEREPORT) {
					try {
						listener.onSuccess(g.fromJson(result,
								SendSmsReportThreeDayModel.class), SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// BEFORE4DAY_SEND_SMS_REPORT --17
				else if (SERVICENAME == TYPE_SMS_FOURREPORT) {
					try {
						listener.onSuccess(g.fromJson(result,
								SendSmsReportFourDayModel.class), SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// BEFORE5DAY_SEND_SMS_REPORT --18
				else if (SERVICENAME == TYPE_SMS_FIVEREPORT) {
					try {
						listener.onSuccess(g.fromJson(result,
								SendSmsReportFiveDayModel.class), SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// BEFORE6DAY_SEND_SMS_REPORT --19
				else if (SERVICENAME == TYPE_SMS_SIXREPORT) {
					try {
						listener.onSuccess(g.fromJson(result,
								SendSmsReportSixDayModel.class), SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// INSERT_USER_SELECTED_PACKAGE --20
				else if (SERVICENAME == TYPE_INSERT_SEL_PACKAGE) {

					try {
						listener.onSuccess(
								g.fromJson(result, GetInfoPayment.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// GET_INFORMATION_FOR_PAYMENT_GATEWAY --21
				else if (SERVICENAME == TYPE_GET_INFO_PAYMENT) {

					try {
						listener.onSuccess(
								g.fromJson(result, GetInfoPayment.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// Insert_Single_Contact --22
				else if (SERVICENAME == TYPE_INSERT_SINGLE_CONTACT) {

					try {
						listener.onSuccess(
								g.fromJson(result, InsertContactModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// UPDATE_Single_Contact --23
				else if (SERVICENAME == TYPE_UPDATE_SINGLE_CONTACT) {

					try {
						listener.onSuccess(
								g.fromJson(result, UpdateIndividualModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// DELETE_SINGLE_CONTACT --24
				else if (SERVICENAME == TYPE_DELETE_SINGLE_CONTACT) {

					try {
						listener.onSuccess(
								g.fromJson(result, UpdateContactModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// GET_ALL_GROUP_DETAILS --25
				else if (SERVICENAME == TYPE_GET_GROUP_DETAILS) {// TYPE_GET_GROUP_DETAILS
					try {
						Utils.printLog("TYPE_GET_GROUP_DETAILS Start time_logger"
								+ new SimpleDateFormat("hh:mm:ss")
										.format(new Date()) + "");
						listener.onSuccess(
								g.fromJson(result, FetchGroupDetails.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// GET_ALL_SENDER ID_LIST --26
				else if (SERVICENAME == TYPE_GET_SENDERS_IDS) {
					try {
						Utils.printLog(" TYPE_GET_SENDERS_IDS Start time_logger"
								+ new SimpleDateFormat("hh:mm:ss")
										.format(new Date()) + "");
						listener.onSuccess(
								g.fromJson(result, FetchSenderModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// InsertGroupContact --27
				else if (SERVICENAME == TYPE_INSERT_GRP_CONT) {// TYPE_GET_GROUP_DETAILS
					try {
						listener.onSuccess(
								g.fromJson(result, GroupDataModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// GET_ALL_CONTACT --28
				else if (SERVICENAME == TYPE_GET_ALL_CONTACT) {
					try {
						listener.onSuccess(
								g.fromJson(result, FetchAllContact.class),
								SERVICENAME);
						Utils.printLog(" GET ALL CONTACT Start time_logger"
								+ new SimpleDateFormat("hh:mm:ss")
										.format(new Date()) + "");
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
						Utils.printLog("fail GET ALL CONTACT Start time_logger"
								+ new SimpleDateFormat("hh:mm:ss")
										.format(new Date()) + "");
					}
				}
				// GET_ALL_CONTACT --288
				else if (SERVICENAME == TYPE_GET_ALL_CONTACT_FOR_CHAT) {
					try {

						JSONObject jsonObject = new JSONObject(
								result.toString());
						JSONArray array = jsonObject
								.getJSONArray("ContactDetails");

						ArrayList<ContactDetailsNew> arrayList = new ArrayList<ContactDetailsNew>();
						if (array != null) {
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject2 = array.getJSONObject(i);
								if (jsonObject2 != null) {
									ContactDetailsNew obj = new ContactDetailsNew();
									if (jsonObject2.has("contact_name")) {
										obj.setContact_name(jsonObject2
												.getString("contact_name"));
									}
									if (jsonObject2.has("contact_mobile")) {
										obj.setContact_mobile(jsonObject2
												.getString("contact_mobile"));
									}

									arrayList.add(obj);
								}

							}
						}

						listener.onSuccess(arrayList, SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// UPDATE_GROUP_CONTACT --29
				else if (SERVICENAME == TYPE_UPDATE_GROUP_CONTACT) {
					try {
						listener.onSuccess(
								g.fromJson(result, GroupDataModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// DELETE_GROUP_CONTACT --30
				else if (SERVICENAME == TYPE_DELETE_GROUP_CONTACT) {
					try {
						listener.onSuccess(
								g.fromJson(result, DeleteGroup.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// GET_GROUP_CONTACT --31
				else if (SERVICENAME == TYPE_GET_GROUP_CONTACT) {
					try {
						listener.onSuccess(g.fromJson(result,
								GroupContactDetailModel.class), SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// GET_ALL_TEMPLATE --32
				else if (SERVICENAME == TYPE_GET_ALL_TEMPLATE) {
					try {
						listener.onSuccess(
								g.fromJson(result, TemplateListByName.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// Check Email Status --33
				else if (SERVICENAME == TYPE_CHECK_EMAIL_STATUS) {
					try {
						listener.onSuccess(
								g.fromJson(result, EmailModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// Get Top Current balance --34
				else if (SERVICENAME == TYPE_GET_TOP_BALANCE) {
					try {
						listener.onSuccess(
								g.fromJson(result, getTopBalance.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// Get All Templates --35
				else if (SERVICENAME == TYPR_GET_TEMPLATE) {
					try {
						listener.onSuccess(
								g.fromJson(result, AllTemplate.class),
								SERVICENAME);
						Utils.printLog(" GETALLTEMplete DETAILS Start time_logger"
								+ new SimpleDateFormat("hh:mm:ss")
										.format(new Date()) + "");
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
						Utils.printLog("fail GETALLTEMplete Start time_logger"
								+ new SimpleDateFormat("hh:mm:ss")
										.format(new Date()) + "");
					}
				}

				// FETCH ALL SCHEDULE LIST --36
				else if (SERVICENAME == TYPE_GET_ALL_SCHEDULE) {
					try {
						listener.onSuccess(
								g.fromJson(result, SchduleListDetail.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// delete schedule sms --37
				else if (SERVICENAME == TYPE_DELETE_SCHEDULE) {
					try {
						listener.onSuccess(
								g.fromJson(result, ScheduleListDelete.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				//377
				else if (SERVICENAME == TYPE_DELETE_SCHEDULE_MAIL) {
					try {
						Log.i("result","result-"+result);
						listener.onSuccess(
								g.fromJson(result, ScheduleListDelete.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// delete group by group name --38
				else if (SERVICENAME == TYPE_DELETE_GROUP) {
					try {
						listener.onSuccess(
								g.fromJson(result, DeleteGroup.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// fetch package details --39
				else if (SERVICENAME == TYPE_FETCH_MOBIPACKAGE) {
					try {
						listener.onSuccess(
								g.fromJson(result, FetchPackage.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// fetch message thread --40
				else if (SERVICENAME == TYPE_FETCH_MESSAGE_THREAD) {
					try {
						listener.onSuccess(
								g.fromJson(result, ThreadModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// fetch message for inbox --41
				else if (SERVICENAME == TYPE_FETCH_MESSAGE_INBOX) {
					try {
						listener.onSuccess(
								g.fromJson(result, InboxModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// delivered message for inbox --42
				else if (SERVICENAME == TYPE_RECEIVE_MESSAGE_INBOX) {
					try {
						listener.onSuccess(
								g.fromJson(result, InboxDeliveredModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// sent message for inbox --43
				else if (SERVICENAME == TYPE_SENT_MESSAGE_INBOX) {
					try {
						listener.onSuccess(
								g.fromJson(result, InboxSentModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// Refer friend --44
				else if (SERVICENAME == TYPE_REFER_FRIEND) {
					try {
						listener.onSuccess(
								g.fromJson(result, ReferFriendModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// fetchContactchat --45
				else if (SERVICENAME == TYPE_FetchChatContact) {
					try {
						listener.onSuccess(
								g.fromJson(result, FetchContactRegChat.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// OPT_IN_OUT --46
				else if (SERVICENAME == TYPE_OPTIN) {
					try {
						listener.onSuccess(
								g.fromJson(result, OPTINmodel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// broadcast message --47
				else if (SERVICENAME == TYPE_NOTIFICATION) {
					try {
						listener.onSuccess(g.fromJson(result, NotiModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// send message group --48
				else if (SERVICENAME == TYPE_FetchChatGroup) {
					try {
						listener.onSuccess(g.fromJson(result,
								FetchGroupDetailsInbox.class), SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// --49 TYPE_IMAGE_UPLOAD
				else if (SERVICENAME == TYPE_IMAGE_UPLOAD) {
					try {
						listener.onSuccess(
								g.fromJson(result, Imageupload.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// --50 TYPE_SPLASH_IMAGE
				else if (SERVICENAME == TYPE_SPLASH_IMAGE) {
					try {

						listener.onSuccess(
								g.fromJson(result, SplashModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// --51
				else if (SERVICENAME == TYPE_FTP_UPLD) {
					try {
						listener.onSuccess(g.fromJson(result, GetFTPCre.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// --52
				else if (SERVICENAME == TYPE_FECT_URL) {
					try {
						listener.onSuccess(g.fromJson(result, FetchUrl.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				// AUI_VID_IMG --53
				else if (SERVICENAME == TYPE_PIC_AUD_VID) {
					try {
						listener.onSuccess(g.fromJson(result, FetchUrl.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}

				// Change password --54
				else if (SERVICENAME == TYPE_CHANGE_PASSWORD) {
					try {
						listener.onSuccess(
								g.fromJson(result, ChangePasswordModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				} else if (SERVICENAME == TYPE_MAIL_REPORT) {
					try {
						listener.onSuccess(
								g.fromJson(result, ReportMailModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				} else if (SERVICENAME == TYPE_GET_MAIL_SCHEDULE) {
					try {						
						listener.onSuccess(
								g.fromJson(result, ScheduledMailModel.class),
								SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				} else {
					try {
						listener.onSuccess(result, SERVICENAME);
					} catch (Exception e) {
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
			}
		}
	}

	// making interface for which use in all class for service
	public interface ServiceHitListener {

		// use for success result from service
		void onSuccess(Object Result, int id);

		// use for coming error from service
		void onError(String Error, int id);

	}
}
