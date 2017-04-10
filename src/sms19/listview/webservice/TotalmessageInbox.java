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
import sms19.listview.newproject.model.TotalMessageInboxmodel;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class TotalmessageInbox {

	// -------------------------------------Global variable-----------------------------
	
   public static final String EQU      = "=";
   public static final String AND      = "&";
   //public static final String BSE_URL    ="http://kit19.com/NewService.aspx?Page=";
  // public static String BSE_URL = "http://sms19.info/NewService.aspx?Page=";
	

	public static final int TYPE_GET             = 1;
	public static final int TYPE_POST            = 2;
	                                                                                                   
	public  static final int TYPE_TOTAL_MESSAGE        = 90;//FetchMobilePackage                             --39
	
	// Use listener in this class for taking response from server service side
	private static ServiceHitListenerInbox listener;
	
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
	
	public TotalmessageInbox(HttpPost HttpPost, String url, int type, int ServiceName, ServiceHitListenerInbox listener)
	{
		
		// pass value from current class to this handler global variable value.
		TotalmessageInbox.listener=listener;
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
     
	public class AsyncExecuter extends AsyncTask<Void, Void, String>
	{

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
					
					//Log.w("URL","URL :::url::"+""+URL);
					
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
				//Log.w("URL", "URL ::::Response: " + strbuild.toString());
				
				return strbuild.toString();
			} 
			
			catch (Exception e)
			{
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) 
		{
			
			if(null == result)
			{
				//// when user not find result value..as a 
				
				listener.onError("Please Try Again Later", SERVICENAME);
			}
			else
			{
			Gson g = new Gson();
				
		
				//fetch message thread --40
			if(SERVICENAME == TYPE_TOTAL_MESSAGE)
				 {
					 try{
						 listener.onSuccess(g.fromJson(result,TotalMessageInboxmodel.class), SERVICENAME);
					 }
					 catch(Exception e){
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
	public interface ServiceHitListenerInbox
	{
		
		//use for success result from service 
		void onSuccess(Object Result, int id);
		//use for coming error from service
		void onError(String Error, int id);
		
	}
}
