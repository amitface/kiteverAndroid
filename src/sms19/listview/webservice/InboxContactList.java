
package sms19.listview.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import sms19.inapp.msg.constant.Apiurls;
import sms19.listview.newproject.model.InboxDeliveredModel;
import sms19.listview.newproject.model.InboxModel;
import sms19.listview.newproject.model.InboxSentModel;
import sms19.listview.newproject.model.ReferFriendModel;
import sms19.listview.newproject.model.ThreadModel;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

/*
* @Developed By: Vaibhav Upadhyay and Pooja Bhatt
* @ DELHI
* 
* This is controller file by which we send and Retrive data from Server using json format
* 
* 
* */


public class InboxContactList 
{

	// -------------------------------------Global variable-----------------------------
	
   public static final String EQU      = "=";
	public static final String AND      = "&";
	
   //public static final String BSE_URL    ="http://kit19.com/NewService.aspx?Page=";
  // public static String BSE_URL = "http://sms19.info/NewService.aspx?Page=";
	
	public static final String SENDERID = "SMSSMS";
	
   public static final String BASE_URL_SEND1 = "&sender="+SENDERID+"&to=";
	
	public static final String BASE_URL_REMAIN = "&priority=1&dnd=1&unicode=0";

 	 
 	public static final String BASE_URL_REMAIN_SCHEDULE = "&priority=1&dnd=1&unicode=0";
 	

	public static final int TYPE_GET             = 1;
	public static final int TYPE_POST            = 2;
	                                                                                                   
	
	public  static final int TYPE_FETCH_MESSAGE_THREAD     = 40;//Fetch_Message  for thread                      --40
	public  static final int TYPE_FETCH_MESSAGE_INBOX      = 41;//Fetch_Message_for inbox                        --41
	public  static final int TYPE_RECEIVE_MESSAGE_INBOX    = 42;//Receive_Message_for inboxread page             --42
	public  static final int TYPE_SENT_MESSAGE_INBOX       = 43;//Sent_Message_for inboxread page                --43
	
	public static final int TYPE_REFER_FRIEND              = 44;// Refer a friend                                --44
	
	// Use listener in this class for taking response from server service side
	private static ServiceHitListenerInboxCnt listener;
	
	// global variable use in this class for http post,url,type and service name
	private HttpPost HTTPPOST;
	private String   URL;
	private int      TYPE;
	private int      SERVICENAME;
	
	//----------------------------------------------Global variable--------------------------------
	
	
	// fcontext for check internet is available or not
	public static Context _context;
	
	Method checkInternet;
	
	
	// constructor for taking value from service.
	
	public InboxContactList(HttpPost HttpPost, String url, int type, int ServiceName, ServiceHitListenerInboxCnt listener)
	{
		
		// pass value from current class to this handler global variable value.
		InboxContactList.listener=listener;
		this.HTTPPOST = HttpPost;
		this.URL      = url;
		this.TYPE     = type;
		this.SERVICENAME = ServiceName;
		
		////Log.w("TAG","CONTEXT::::1:::::"+_context);
		
		try{
			/***************************INTERNET********************************/
			checkInternet = new Method(_context);
				
		    // //Log.w("TAG","CHECK_INTERNET"+checkInternet.isConnected());
			    
			if(!checkInternet.isConnected()){
		    	  listener.onError("No Internet Connection Available", ServiceName);
					return;  
		    	  
		      }
			/***************************INTERNET********************************/
			}
			
		catch(Exception e){
			
		}
		
		
		//AsyncExecuter by which we take response from service 
		(new AsyncExecuter()).execute();
		
	}
	
	
   
  //http://sms19.com/NewService.aspx?Page=UndeliveredMessageForRecipientCount&RecipientUserID=20705  --40
   public static class UndeliveredMessageForRecipientCount
   {
   	public static final String SERVICE       = "UndeliveredMessageForRecipientCount";
   	public static final String USERID        = "RecipientUserID";
   	
   	
   	public static String geturl(String RecipientUserID)
   	{
   		return Apiurls.KIT19_BASE_URL +SERVICE + AND + USERID + EQU + RecipientUserID ;
   	}
   }
   
   //http://sms19.com/NewService.aspx?Page=MessageReceivedToRecipient&RecipientUserID=20705  --41

   public static class MessageReceivedToRecipient
   {
   	public static final String SERVICE       = "MessageReceivedToRecipient";
   	public static final String USERID        = "RecipientUserID";
   	
   	
   	public static String geturl(String RecipientUserID)
   	{
   		return Apiurls.KIT19_BASE_URL +SERVICE + AND +USERID + EQU + RecipientUserID ;
   	}
   }
  
 // http://sms19.com/NewService.aspx? Page=TotalMessagesOfSingleSerderUser&SenderUserID=20717&RecipientUserID=20705 --42
   public static class TotalMessagesOfSingleSenderUser
   {
   	public static final String SERVICE       = "TotalMessagesOfSingleSenderUser";
   	public static final String USERID        = "SenderUserID";
   	public static final String RECIPIENTUID  = "RecipientUserID";
   	
   	public static String geturl(String SenderUserID, String RecipientUserID)
   	{
   		return Apiurls.KIT19_BASE_URL +SERVICE + AND + USERID + EQU + SenderUserID + AND + RECIPIENTUID + EQU + RecipientUserID;
   	}
   }
   
 //http://sms19.com/NewService.aspx?Page=MessageSendToRecipient&SenderUserID=20717&SenderPhoneNo=9643853169&SenderMsg=TestMsg&RecipientUserID=20705&RecipientPhoneNo=9582116782 --43
   public static class MessageSendToRecipient
   {
   	public static final String SERVICE          = "MessageSendToRecipient";
   	public static final String USERID           = "SenderUserID";
       public static final String MESSAGE          = "SenderMsg";
       public static final String SENDERPHONE      = "SenderPhoneNo";
   	public static final String RECIPIENT_UID    = "RecipientUserID";
   	public static final String RECIPIENT_NUMBER = "RecipientPhoneNo";
   	
   	public static String geturl(String UserID, String senderphono,String message,String Recipientuid,String Recipientphonenum)
   	{
   	return Apiurls.KIT19_BASE_URL +SERVICE+ AND + USERID + EQU + UserID +AND + SENDERPHONE + EQU + senderphono + AND + MESSAGE + EQU +message+ AND + RECIPIENT_UID + EQU +Recipientuid + AND + RECIPIENT_NUMBER+ EQU +Recipientphonenum;
   	}
   }
   
 //http://sms19.com/NewService.aspx?Page=ReferFriend&SenderUserID=20705&RecipientName=Ratna&RecipientPhoneNo=9654469382 --44
   public static class ReferFriend{
   	public static final String SERVICE        = "ReferFriend";
   	public static final String Sender_USER_ID = "SenderUserID";
   	public static final String RecipientName  = "RecipientName";
   	public static final String RecipientNum   = "RecipientPhoneNo";
   	
   	public static String geturl(String userID, String NAME, String NUMBER){
   		
   		return Apiurls.KIT19_BASE_URL +SERVICE + AND+ Sender_USER_ID+EQU + userID+AND+RecipientName+EQU+NAME+AND+RecipientNum+EQU+NUMBER;
   		}
   	
   }
  
   
	public class AsyncExecuter extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) 
		{
		
			// create one string builder by which we can write data from service 
			StringBuilder strbuild;
			
			//---------------------------------------------------
			// create one object of basic http parameter
			HttpParams httpparams = new BasicHttpParams();
			
			// time of timeout of service response
			int timeOutConnection = 20000;// 20 sec
			
			// function by which know about connection timeout
			HttpConnectionParams.setConnectionTimeout(httpparams, timeOutConnection);
			
			//-----------------------------------------------------
			
			//----------------------------------------------------
			// time out of socket of device
			int timeOutConncetionSoket= 20000;// 20 sec
			
			// function by which know about socket timeout
			HttpConnectionParams.setSoTimeout(httpparams, timeOutConncetionSoket);
			//----------------------------------------------------
			
			// making object of defalut httpclient with http parameter
			HttpClient httpclient = new DefaultHttpClient(httpparams);
			
			HttpResponse httpresponse=null;
			
			//String responseString = null;
			
			// check which method is use in service
			try{
				if(TYPE == TYPE_GET)
				{
					
					//Log.w("URL","url::"+""+URL);
					
					URL = URL.replaceAll(" ", "+");
					
					httpresponse = httpclient.execute(new HttpGet(URL));
					
				}
				else if(TYPE == TYPE_POST){
					
					httpresponse = httpclient.execute(HTTPPOST);
					
				}
				
				//Log.w("TAG", "Raw Response: " + httpresponse);
				
				InputStreamReader streamReader = new InputStreamReader(httpresponse.getEntity().getContent());
				BufferedReader    br   = new BufferedReader(streamReader);
				
				String line;
				
				strbuild = new StringBuilder();
				
				while((line = br.readLine()) != null)
				{
					
					strbuild.append(line);
				}
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}
			
			try {
				//Log.w("URL", "Response: " + strbuild.toString());
				
				return strbuild.toString();
			} 
			
			catch (Exception e)
			{
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			
			if(null == result)
			{
				//// when user not find result value..as a 
				
				listener.onError("Please Try Again Later", SERVICENAME);
			}
			else
			{
			Gson g = new Gson();
				
		
				//fetch message thread --40
			if(SERVICENAME == TYPE_FETCH_MESSAGE_THREAD)
				 {
					 try{
						 listener.onSuccess(g.fromJson(result,ThreadModel.class), SERVICENAME);
					 }
					 catch(Exception e){
						 e.printStackTrace();
						 listener.onError("Please Try Again Later", SERVICENAME);
					 }	 
				 }
				
				 //fetch message for inbox --41
				 else if(SERVICENAME == TYPE_FETCH_MESSAGE_INBOX)
				 {
					 try{
						 listener.onSuccess(g.fromJson(result,InboxModel.class), SERVICENAME);
					 }
					 catch(Exception e){
						 e.printStackTrace();
						 listener.onError("Please Try Again Later", SERVICENAME);
					 }	 
				 }
				
				 //delivered message for inbox --42
				 else if(SERVICENAME == TYPE_RECEIVE_MESSAGE_INBOX)
				 {
					 try{
						 listener.onSuccess(g.fromJson(result,InboxDeliveredModel.class), SERVICENAME);
					 }
					 catch(Exception e){
						 e.printStackTrace();
						 listener.onError("Please Try Again Later", SERVICENAME);
					 }	 
				 }
			
				 //sent message for inbox --43
				 else if(SERVICENAME == TYPE_SENT_MESSAGE_INBOX)
				 {
					 try
					 {
					listener.onSuccess(g.fromJson(result,InboxSentModel.class), SERVICENAME);
					 }
					 catch(Exception e)
					 {
					 e.printStackTrace();
					listener.onError("Please Try Again Later", SERVICENAME);
					 }	 
				 }
				
				// Refer friend  --44
				 else if(SERVICENAME == TYPE_REFER_FRIEND)
				 {
					 try {
					listener.onSuccess(g.fromJson(result, ReferFriendModel.class), SERVICENAME);	
					} catch (Exception e) {
						 e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				 }
				
			   	else
			   	{
					try 
					{
						listener.onSuccess(result, SERVICENAME);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
						listener.onError("Please Try Again Later", SERVICENAME);
					}
				}
				
			}
			
		} 
		
		
	}
	
	// making interface for which use in all class for service
	public interface ServiceHitListenerInboxCnt
	{
		
		//use for success result from service 
		void onSuccess(Object Result, int id);
		//use for coming error from service
		void onError(String Error, int id);
		
	}
}


