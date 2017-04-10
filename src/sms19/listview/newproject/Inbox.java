package sms19.listview.newproject;


import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import sms19.listview.adapter.Inbox_DB_Adapter;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.GetFTPCre;
import sms19.listview.newproject.model.InboxModel;
import sms19.listview.newproject.model.InboxModel.InboxList;
import sms19.listview.newproject.model.InboxModelRead;
import sms19.listview.newproject.model.InboxReadDummy;
import sms19.listview.newproject.model.TotalMessageInboxmodel;
import sms19.listview.webservice.InboxContactList;
import sms19.listview.webservice.InboxContactList.ServiceHitListenerInboxCnt;
import sms19.listview.webservice.TotalmessageInbox;
import sms19.listview.webservice.TotalmessageInbox.ServiceHitListenerInbox;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.kitever.android.R;


public class Inbox extends AppCompatActivity implements OnClickListener
{

	ListView inboxList;
	String MsgCut="",Data="";
	String ListData[];
	Context cnt;

	String currentTime = "", currentdate ="";
	
	List<InboxReadDummy> dataset;
	int counervariable=1;
    Runnable checker;
	int taskReapeted = 0;
	private Thread thread;
	
	String Mobile= "",InAppPassword="", UserId="",path;
	DataBaseDetails dbObject=new DataBaseDetails(this);

	// global declare variable for starReapeted thread
	Runnable checkerTime;
	
	Handler mHandler = new Handler();
	
	String filetype,filename, urlpath;
	// variable for fetch data from database
	String [] inboxTime;
	String [] inboxMessage;
	String [] inboxMobile;
	String [] inboxStatus;
	String [] iboxsendername;
	String user = "";
	String [] inboxSenderId;
	
	String imagetoupload2;
	boolean checkDuplicate;
	String mediagpnm="",medianumber="",recipientid="";
	
	ArrayAdapter<String> df; 
	public static String senid="",picpath="";
	
	String setimege;
	String message      = "",InAppUsermobile="",Sendermobile= "",time= "",countmsg= "",InAppUserlogin="",SenderName   = "",sendurlpath="";
    
	// outbox database
	String dbmessagein ="",dbnumberin="",dbgrupname="",dbrecipientId  ="",dbtime= "",dbcheckuncheck="";
    String dbpageName  ="",dbdiff    ="",dbmyid    ="",dbsenderuserid ="",dbdate="", dbIurl        ="",dbsendingtype="",dbSstatus=""; 
	int dbOutID,dbinboxOut,dbsendStatusRead;
	
	ImageView searchimage;
	MyTransferListener mb;
	List<InboxModelRead> setdbdataModel;
	String TimeInbox,MessageInbox,MobileInbox, StatusInbox, SenderID,SendersName;
	
	// Cursor Adapter
	SimpleCursorAdapter adapter;
	
	// custom manually adapter
    Inbox_DB_Adapter ccAdpt;
	
    boolean stop = true;
    EditText search;
    boolean checkData = false;
    
    final Handler handler = new Handler();
	public static Activity sendInboxFlag;
	
	public static boolean inboxthreadclose= true;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
		
		// one flag to stop thread after destroy activity
		stop = true;
		cnt = this;
		
		// close countinuesly running thread
		inboxthreadclose= true;
		
		sendInboxFlag = this;
		
		inboxList   = (ListView) findViewById(R.id.listInbox);
		search      = (EditText)findViewById(R.id.searchinbox);
		searchimage = (ImageView)findViewById(R.id.searchimage);
		
		//***********************ACTION BAR******************************
        ActionBar adf = getSupportActionBar();
        adf.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#04B486")));
        adf.setTitle(Html.fromHtml("<font color='#ffffff'>Inbox</font>"));
        //***********************ACTION BAR******************************
       
         try 
         {
         //set all chat conversation contacts from database 
		 CallToSetAllChatContactInfoDb();
		 }
         catch (Exception e)
         {
		 e.printStackTrace();
		 }
         
         dbObject.Open();
         dbObject.DeleteSelRegCnt();
         dbObject.close();
         
         checkData = false;
         
         // setText filter enable for searching
         inboxList.setTextFilterEnabled(true);
         
         // call fxn for sort data.
         sortdata();
    
        //call webservice after 5 second for new message from any contact(user).
        CallForNewMessage();  
        // call fxn for outbox that send message stored in database when internet connection not there
        calloutboxservice();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.inboxcontact, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
         int id = item.getItemId();
		
		if (id == R.id.sendmessageinbox) 
		{
			try 
			{
			SendMsgChatgroup.sendGroupFlag.finish();
			}
			catch (Exception e) {}
			
			try 
			{
			SendMessageInbox.sendContactFlag.finish();
			} 
			catch (Exception e) 
			{}
			
			//-------------send message invitation to people in contacts and group
		    Intent i = new Intent(Inbox.this,SendMessageInbox.class);
			startActivity(i);
			return true;
		}
		
		if(id == R.id.sortingdesc)
		{
	   //sort in descending order
		callFxnForsorting();
		}
		if(id == R.id.sortingasc)
		{
	   //sort in ascending order
		CallToSetAllChatContactInfoDb();
		}
		
		return super.onOptionsItemSelected(item);
	}
	public void CallForNewMessage()
	{
		thread = new Thread(new Runnable()
		{
	  	        @Override
	  	        public void run()
	  	        {
	  	           
	  	            while (stop)
	  	            {
	  	                try
	  	                {
	  	                    Thread.sleep(5000);
	  	                    mHandler.post(new Runnable()
	  	                    {
                            
	  	                        @Override
	  	                        public void run()
	  	                        {
	  	                        	//Log.w("INBOX", "INBOX:::::::::::::::(thread):"+taskReapeted);
	  	                        //check if any new message and who messaged it with name,message and picture
	  	                        	CallIfNewMessageFromAnyone();
	  	                        	
	  	                          	taskReapeted++;
	  	                        }
	  	                    });
	  	                }
	  	                catch (Exception e) 
	  	                {}
	  	             }
	  	           
	  	        }
	  	  });
		thread.start();  
	}
	 
   private void CallIfNewMessageFromAnyone()
	{
			
		fetchMobileandUserId();
		
		new InboxContactList(null, InboxContactList.MessageReceivedToRecipient.geturl(UserId), InboxContactList.TYPE_GET, InboxContactList.TYPE_FETCH_MESSAGE_INBOX, new ServiceHitListenerInboxCnt() {
			
			@Override
			public void onSuccess(Object Result, int id) {
								
				checkData = true;
			
				InboxModel mod = (InboxModel) Result;
			
				List<InboxList> list = mod.getMessageReceivedToRecipient();
				
				if(mod.getMessageReceivedToRecipient().size()>0)
				{
					
					for(int i =0;i<(mod.getMessageReceivedToRecipient().size());i++)
					{
						if(haveDatainbox())
							{
							    // fetch data with model..
								fetchDataFromModel(mod,i);
								// fetch userid from database
		                        fetchMobileandUserId();
								
		                        //Log.w("TAG"," sendermobile :2::(sendermobile):"+Sendermobile);
		                        
								dbObject.Open();
								dbObject.addInboxItem(UserId, time, message, countmsg, Sendermobile,SenderName,senid,"Media","0");
								dbObject.close();
							}
							
							else
							{
								// fetch data with model
								fetchDataFromModel(mod,i);
								// fetch userid from databse
		                        fetchMobileandUserId();
								
		                        // check duplicate data into table
								checkDuplicate = checkInboxTime(Sendermobile);
								
								if(checkDuplicate)
								{
								    //Log.w("TAG"," sendermobile :3::(sendermobile):"+Sendermobile);
								    
									dbObject.Open();
									dbObject.addInboxItem(UserId, time, message, countmsg, Sendermobile,SenderName,senid,"Media","0");
									dbObject.close();
								}
								
								else
								{
								    //Log.w("TAG"," sendermobile :4::(sendermobile):"+Sendermobile);
									dbObject.Open();
									dbObject.deleteInboxItemNumber(Sendermobile);
									dbObject.addInboxItem(UserId, time, message, countmsg, Sendermobile,SenderName,senid,"Media","0");
									dbObject.close();
								}
					        }
					
						 // fetch data with model
						 fetchDataFromModel(mod,i);
						
						fetchMobileandUserId();
						  
						new TotalmessageInbox(null, TotalmessageInbox.TotalMessagesOfSingleSenderUser.geturl( senid,UserId), TotalmessageInbox.TYPE_GET, TotalmessageInbox.TYPE_TOTAL_MESSAGE, new ServiceHitListenerInbox() {
							
							@Override
							public void onSuccess(Object Result, int id) 
							{
								//Log.w("INBOX READ","INBOX READ:::::SUCCESS:::(call service ;fetch data for single user):");
								
								callFxnInSuccess(Result);
					   						
							}
							
							@Override
							public void onError(String Error, int id) 
							{
							//Log.w("INBOX READ","INBOX READ:::::SUCCESS:::(call service ;fetch data for single user):");
							
						    }
							});
						
					}
					
					// set data from databse after new addition of data
					CallToSetAllChatContactInfoDb();        
					}
				
				else
				{
					
				}
						
			     }
       
			
			@Override
			public void onError(String Error, int id)
			{
				
				//Log.w("INBOX", "INBOX::::::::ERROR:::::::(call webservice):");
				
				checkData = false;
				
				// if internet is not available then data will show into list.
				CallToSetAllChatContactInfoDb();
			}
		});
	
      
	}

    private void fetchDataFromModel(InboxModel mod, int i) 
    {
			
			message       = mod.getMessageReceivedToRecipient().get(i).getMessage();
			countmsg      = mod.getMessageReceivedToRecipient().get(i).getCountMessage();
			time          = mod.getMessageReceivedToRecipient().get(i).getMessageInsertDateTime();
			Sendermobile  = mod.getMessageReceivedToRecipient().get(i).getSenderPhoneNo();
			SenderName    = mod.getMessageReceivedToRecipient().get(i).getSenderName();
			senid         = mod.getMessageReceivedToRecipient().get(i).getSenderUserID();
			picpath       = mod.getMessageReceivedToRecipient().get(i).getProfilePicturePath();
			
		    //Log.w("TAG"," sendermobile :model::(sendermobile):"+Sendermobile);
			
			//"MessageReceivedToRecipient":[{"SenderUserID":1,"SenderPhoneNo":"9910930090","SenderName":"VIJAY","Message":"333333333","MessageInsertDateTime":"2015-07-25T13:15:00","CountMessage":19,"ProfilePictureName":null,"ProfilePicturePath":null},{"SenderUserID":20705,"SenderPhoneNo":"9582116782","SenderName":"vaibhav","Message":"1111111","MessageInsertDateTime":"2015-07-25T13:25:00","CountMessage":283,"ProfilePictureName":"20705","ProfilePicturePath":"
			user=Sendermobile;
			
			//Log.w("check profile picture in class","what path does it receive"+picpath);
			
			// add http before url
			if(picpath == null || picpath.length() < 3){
				
			}
			else
			{
				String checkhttp = picpath.substring(0,7);
				
				if(checkhttp.equalsIgnoreCase("http://"))
				{
					
				}
				else
				{
					picpath = "http://"+picpath;
				}
				
				new DownloadFileFromURL().execute(picpath);
			}
			
			// add http before url
			
			try
			{
				
			if(SenderName.length() <= 0)
			{
			  SenderName = Sendermobile;
			}
			
			}
			catch (Exception e)
			{
			//	  SenderName = Sendermobile;
			}
			
			try 
			{
			int pod = time.indexOf("T");
			
			String adddate = time;
			
			time    = time.substring((pod+1),(pod+9));
			
			adddate = adddate.substring(0,adddate.indexOf("T"));
			
			time = adddate+","+time+" ";
			
			//time = time.replace("T", ":");
			
			}
			catch (Exception e)
			{
			e.printStackTrace();
			}
		 
			
		}
	public void CallToSetAllChatContactInfoDb()
	{
		
		/*************************************contacts************************************/

          // fetching data from database in separate manner		
		  inboxTime       = fetchInboxTime();
		  inboxMessage    = fetchInboxMessage();
		  inboxMobile     = fetchInboxMobile();
		  inboxStatus     = fetchInboxdate();
		  inboxSenderId   = fetchInboxSenderID();
		  iboxsendername  = fetchName();
		  
		  // convert "String Array" to "ArrayList" 
		  ArrayList<String> inboxListTime    = new ArrayList<String>(Arrays.asList(inboxTime));
		  ArrayList<String> inboxListMessage = new ArrayList<String>(Arrays.asList(inboxMessage));
		  ArrayList<String> inboxListMobile  = new ArrayList<String>(Arrays.asList(inboxMobile));
		  ArrayList<String> inboxListStatus  = new ArrayList<String>(Arrays.asList(inboxStatus));
		  ArrayList<String> inboxListSender  = new ArrayList<String>(Arrays.asList(inboxSenderId));
		  ArrayList<String> inboxsendername  = new ArrayList<String>(Arrays.asList(iboxsendername));
		  
		  // create object of "global in list with model" and "local create with ArrayList with Model" 
		  // It's will be used to set value in adapter or we can say that "is it new Blank list"
		  
		  setdbdataModel=new ArrayList<InboxModelRead>();
		  
		  // Initialize iterator for fetch data from list 
		  Iterator<String> itreT      = inboxListTime.iterator();
		  Iterator<String> itreM      = inboxListMessage.iterator();
		  Iterator<String> itreMo     = inboxListMobile.iterator();
		  Iterator<String> itreStatus = inboxListStatus.iterator();
		  Iterator<String> itreSender = inboxListSender.iterator();
		  Iterator<String> itreSendername=inboxsendername.iterator();
		  
		  while(itreT.hasNext())
		  {
		//String TimeInbox,MessageInbox,MobileInbox;
		   TimeInbox    = itreT.next();
		   MessageInbox = itreM.next();
		   MobileInbox  = itreMo.next();
		   StatusInbox  = itreStatus.next();
		   SenderID     = itreSender.next();
		   SendersName  =itreSendername.next();
		   setdbdataModel.add(new InboxModelRead(TimeInbox, MessageInbox, MobileInbox, StatusInbox, SenderID,SendersName));
		   
		  }
				
			inboxList.setAdapter(ccAdpt=new Inbox_DB_Adapter(this, R.layout.inbox_read_list_adapter, setdbdataModel));
			ccAdpt.notifyDataSetChanged();
	}
	
	public void callFxnForsorting()
	{
		//Log.w("INBOX", "INBOX::::::::SET DATA DATABASE:::::::(set data with adapter):");
		
		/*************************************contacts************************************/

          // fetching data from database in separate manner		
		  inboxTime    = fetchInboxTimedesc();
		  inboxMessage = fetchInboxMessagedesc();
		  inboxMobile  = fetchInboxMobiledesc();
		  inboxStatus  = fetchInboxdatedesc();
		  inboxSenderId = fetchInboxSenderIDdesc();
		  
		  iboxsendername  =fetchName();
		  
		  // convert "String Array" to "ArrayList" 
		  ArrayList<String> inboxListTime    = new ArrayList<String>(Arrays.asList(inboxTime));
		  ArrayList<String> inboxListMessage = new ArrayList<String>(Arrays.asList(inboxMessage));
		  ArrayList<String> inboxListMobile  = new ArrayList<String>(Arrays.asList(inboxMobile));
		  ArrayList<String> inboxListStatus  = new ArrayList<String>(Arrays.asList(inboxStatus));
		  ArrayList<String> inboxListSender  = new ArrayList<String>(Arrays.asList(inboxSenderId));
		  ArrayList<String> inboxsendername  =new ArrayList<String>(Arrays.asList(iboxsendername));
		  // create object of "global in list with model" and "local create with ArrayList with Model" 
		  // It's will be used to set value in adapter or we can say that "is it new Blank list"
		  
		  setdbdataModel=new ArrayList<InboxModelRead>();
		  
		  // Initialize iterator for fetch data from list 
		  Iterator<String> itreT      = inboxListTime.iterator();
		  Iterator<String> itreM      = inboxListMessage.iterator();
		  Iterator<String> itreMo     = inboxListMobile.iterator();
		  Iterator<String> itreStatus = inboxListStatus.iterator();
		  Iterator<String> itreSender = inboxListSender.iterator();
		  Iterator<String> itreSendername=inboxsendername.iterator();
		  
		  while(itreT.hasNext())
		  {
		//String TimeInbox,MessageInbox,MobileInbox;
		   TimeInbox    = itreT.next();
		   MessageInbox = itreM.next();
		   MobileInbox  = itreMo.next();
		   StatusInbox  = itreStatus.next();
		   SenderID     = itreSender.next();
		   SendersName  =itreSendername.next();
		   setdbdataModel.add(new InboxModelRead(TimeInbox, MessageInbox, MobileInbox, StatusInbox, SenderID,SendersName));
		   
		  }
				
			inboxList.setAdapter(ccAdpt=new Inbox_DB_Adapter(this, R.layout.inbox_read_list_adapter, setdbdataModel));
			ccAdpt.notifyDataSetChanged();
	}

	public void fetchMobileandUserId() 
	{

		dbObject.Open();
		  
		   Cursor c;
		   
		   c= dbObject.getLoginDetails();
		   
		  				   
		   while(c.moveToNext())
		   {
			   InAppPassword=c.getString(5); 
			   InAppUserlogin = c.getString(6); 
			   Mobile = c.getString(1);
			   UserId = c.getString(3);
		   }
				   
		   dbObject.close();
	
		
	}
	
	public String[] fetchInboxUserID()
	{
		
		dbObject.Open();
		
		Cursor c;
		
		c= dbObject.getInboxDetails();
		
		int countValue = c.getCount();
		
		String fetchData [] =new String [countValue]; 
		
		/*
		 * 0: UserID, 1: Time, 2: Message, 3: Date, 4:Mobile
		 * 
		 * */
		
		int i=0;
		while(c.moveToNext())
		{
			
			fetchData[i] = c.getString(0);
			i++;
		}
		
		dbObject.close();
		
		return fetchData;
		
	}
	
public String[] fetchInboxTime()
{
		
		dbObject.Open();
		
		Cursor c;
		
		c= dbObject.getInboxDetails();
		
		int countValue = c.getCount();
		
		String fetchData [] =new String [countValue]; 
		
		/*
		 * 0: UserID, 1: Time, 2: Message, 3: Date, 4:Mobile
		 * 
		 * */
		
		int i=0;
		while(c.moveToNext())
		{
			
			fetchData[i] = c.getString(1);
			i++;
		}
		
		dbObject.close();
		
		return fetchData;
		
	}

public String[] fetchInboxTimedesc()
{
	
	dbObject.Open();
	
	Cursor c;
	
	c= dbObject.getInboxDetailsDesc();
	
	int countValue = c.getCount();
	
	String fetchData [] =new String [countValue]; 
	
	/*
	 * 0: UserID, 1: Time, 2: Message, 3: Date, 4:Mobile
	 * 
	 * */
	
	int i=0;
	while(c.moveToNext())
	{
		
		fetchData[i] = c.getString(1);
		i++;
	}
	
	dbObject.close();
	
	return fetchData;
	
}

public boolean checkInboxTime(String Time)
{
	
	dbObject.Open();
	
	Cursor c;
	
	c= dbObject.getInboxDetails();
	
	int countValue = c.getCount();
	
	String checkData [] =new String [countValue]; 

	
	int i=0;
	while(c.moveToNext()){
		
		checkData[i] = c.getString(4);
		
		if(Time.trim().equalsIgnoreCase(checkData[i].trim())){
			
			dbObject.close();
			return false;	
		}
			
		i++;
	}
	
	dbObject.close();
	
	return true;
	
}

public String[] fetchInboxMessage(){
	
	dbObject.Open();
	
	Cursor c;
	
	c= dbObject.getInboxDetails();
	
	int countValue = c.getCount();
	
	String fetchData [] =new String [countValue]; 
	
	/*
	 * 0: UserID, 1: Time, 2: Message, 3: Date, 4:Mobile
	 * 
	 * */
	
	int i=0;
	while(c.moveToNext())
	{
		
		fetchData[i] = c.getString(2);
		i++;
	}
	
	dbObject.close();
	
	return fetchData;
	
}

public String[] fetchInboxMessagedesc(){
	
	dbObject.Open();
	
	Cursor c;
	
	c= dbObject.getInboxDetailsDesc();
	
	int countValue = c.getCount();
	
	String fetchData [] =new String [countValue]; 
	
	/*
	 * 0: UserID, 1: Time, 2: Message, 3: Date, 4:Mobile
	 * 
	 * */
	
	int i=0;
	while(c.moveToNext()){
		
		fetchData[i] = c.getString(2);
		i++;
	}
	
	dbObject.close();
	
	return fetchData;
	
}

public String[] fetchInboxdate()
{
	
	dbObject.Open();
	
	Cursor c;
	
	c= dbObject.getInboxDetails();
	
	int countValue = c.getCount();
	
	String fetchData [] =new String [countValue]; 
	
	/*
	 * 0: UserID, 1: Time, 2: Message, 3: Date, 4:Mobile
	 * 
	 * */
	
	int i=0;
	while(c.moveToNext())
	{
		
		fetchData[i] = c.getString(3);
		i++;
	}
	
	dbObject.close();
	
	return fetchData;
	
}
public String[] fetchInboxdatedesc()
{
	
	dbObject.Open();
	
	Cursor c;
	
	c= dbObject.getInboxDetailsDesc();
	
	int countValue = c.getCount();
	
	String fetchData [] =new String [countValue]; 
	
	/*
	 * 0: UserID, 1: Time, 2: Message, 3: Date, 4:Mobile
	 * 
	 * */
	
	int i=0;
	while(c.moveToNext()){
		
		fetchData[i] = c.getString(3);
		i++;
	}
	
	dbObject.close();
	
	return fetchData;
	
}

public String[] fetchInboxSenderID()
{
	
	dbObject.Open();
	
	Cursor c;
	
	c= dbObject.getInboxDetails();
	
	int countValue = c.getCount();
	
	String fetchData [] =new String [countValue]; 
	
	/*
	 * 0: UserID, 1: Time, 2: Message, 3: Date, 4:Mobile
	 * 
	 * */
	
	int i=0;
	while(c.moveToNext()){
		
		fetchData[i] = c.getString(5);
		i++;
	}
	
	dbObject.close();
	
	return fetchData;
	
}

public String[] fetchInboxSenderIDdesc()
{
	
	dbObject.Open();
	
	Cursor c;
	
	c= dbObject.getInboxDetailsDesc();
	
	int countValue = c.getCount();
	
	String fetchData [] =new String [countValue]; 
	
	/*
	 * 0: UserID, 1: Time, 2: Message, 3: Date, 4:Mobile
	 * 
	 * */
	
	int i=0;
	while(c.moveToNext()){
		
		fetchData[i] = c.getString(5);
		i++;
	}
	
	dbObject.close();
	
	return fetchData;
	
}

public String[] fetchInboxMobile(){
	
	dbObject.Open();
	
	Cursor c;
	
	c= dbObject.getInboxDetails();
	
	int countValue = c.getCount();
	
	String fetchData [] =new String [countValue]; 
	
	/*
	 * 0: UserID, 1: Time, 2: Message, 3: Date, 4:Mobile
	 * 
	 * */
	
	int i=0;
	while(c.moveToNext()){
		
		fetchData[i] = c.getString(4);
		i++;
	}
	
	dbObject.close();
	
	return fetchData;
	
}

public String[] fetchInboxMobiledesc(){
	
	dbObject.Open();
	
	Cursor c;
	
	c= dbObject.getInboxDetailsDesc();
	
	int countValue = c.getCount();
	
	String fetchData [] =new String [countValue]; 
	
	/*
	 * 0: UserID, 1: Time, 2: Message, 3: Date, 4:Mobile
	 * 
	 * */
	
	int i=0;
	while(c.moveToNext()){
		
		fetchData[i] = c.getString(4);
		i++;
	}
	
	dbObject.close();
	
	return fetchData;
	
}

public boolean haveDatainbox(){
	
	dbObject.Open();
	
	Cursor c;
	
	c= dbObject.getInboxDetails();

	
	while(c.moveToNext()){
	
		return false;
		
	}
	
	dbObject.close();
	 
	return true;
	
}

class DownloadFileFromURL extends AsyncTask<String, String, String> 
{

	@Override
	protected void onPreExecute() {
		 super.onPreExecute();
       
	}
	
	@Override
	protected String doInBackground(String... f_url) {
		int count;
        try {
        	
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
      
            int lenghtOfFile = conection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream("/sdcard/ChatPeoplePic"+user+".jpg");
            byte data[] = new byte[1024];
            long total = 0;
           while ((count = input.read(data)) != -1) 
           {
                total += count;
               output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            
        } catch (Exception e) {
//        Log.e("Error: ", "Please Try again later");
        }
        
        return null;
	}
		
	@Override
	protected void onPostExecute(String file_url) {
		
		
	}

}

//outbox service will run here
private void calloutboxservice() 
{
	fetchMobileandUserId();
	
	dbObject.Open();
	
	Cursor ce;
	
	ce=dbObject.getOutBox();
		
	while(ce.moveToNext())
	{
	if(ce.getCount()>=counervariable)
	{
		counervariable++;
	
		dbmessagein      =	ce.getString(0); // message
	    dbnumberin       =	ce.getString(1); // number
	    dbtime           =  ce.getString(2);// time
		dbmyid           =  ce.getString(3);//app userid
		dbsenderuserid   =  ce.getString(4);// sender userid
		dbdate           =	ce.getString(5);// date
	    dbIurl           =	ce.getString(6);// url 
	    dbsendingtype    =  ce.getString(7);//s_m 0r r_m
		dbdiff           =  ce.getString(8);// message type: txt, image
		dbSstatus        =  ce.getString(9);// sending status
		dbrecipientId    =  ce.getString(10);// receipent userid
		dbcheckuncheck   =	ce.getString(11);// check, uncheck status
	    dbpageName       =  ce.getString(12);// page name
		dbOutID          =  ce.getInt(13);// out id auto incresase
		dbinboxOut       =  ce.getInt(14);// inbox outbox key
		dbgrupname       =	ce.getString(15);// group name
	    dbsendStatusRead =	ce.getInt(16);// sms read status

	if(dbdiff.equals("TXT"))
	{
			 new webservice(null, webservice.MessageSendToRecipient.geturl("", InAppUserlogin, InAppPassword,UserId, "SMSSMS",dbnumberin, dbmessagein, Mobile,dbrecipientId,dbcheckuncheck,dbgrupname), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() {
				
				@Override
				public void onSuccess(Object Result, int id)
				{
						
				if(dbgrupname.length()>0)
	 		    {
	  		    	    dbObject.DeleteOutboxbyGroupName(dbgrupname);
	  		    }
				
	 		    else
	 		    {
	 		    		dbObject.DeleteOutbox(dbnumberin, dbOutID);
	 		    }
	 	
				}
				
				@Override
				public void onError(String Error, int id) {
									
				}
			}); 

	}
	else if(dbdiff.equals("IMAGE"))
	{
	
		dbcheckuncheck  =  ce.getString(11);

		path            =	ce.getString(6);
		//Log.w("path_diff","diff"+dbdiff+"path"+path);
	
		 dbgrupname     =  ce.getString(15);
		 dbcheckuncheck =  ce.getString(11);
		 recipientid    =  ce.getString(10);	
		   
	     mediagpnm      =  dbgrupname;
	     medianumber    =  dbnumberin;
	  
		
	new webservice(null, webservice.GetFTPHostDetail.geturl("image"), webservice.TYPE_GET, webservice.TYPE_FTP_UPLD, new ServiceHitListener() {
		
	
		public void onSuccess(Object Result, int id)
		{
			    try
			    {
			    imagetoupload2=path.substring(path.lastIndexOf("/"));
				} 
			 
			    catch (Exception e) 
			    {
				e.printStackTrace();
			    }
			
				filetype="image";
				filename=path.substring(path.lastIndexOf("/")+1);
				
			GetFTPCre gpmodel=(GetFTPCre) Result;
			String FTP_USER=gpmodel.getGetFTPHostDetail().get(0).getFtpUser();
			String FTP_PASS=gpmodel.getGetFTPHostDetail().get(0).getFtpPassword();
		    String FTP_HOST=gpmodel.getGetFTPHostDetail().get(0).getHostName();
		    urlpath=gpmodel.getGetFTPHostDetail().get(0).getFtpUrl();
		    
		    String imagePath=path;
		    sendurlpath= urlpath+imagetoupload2;
			//Log.w("sendurlpath", "urlpath"+urlpath+"imagetoupload2"+imagetoupload2+path+"path");		
	        
			File f = new File(imagePath); 			
	        FTPClient client = new FTPClient(); 	
		      try 
		      { 	
		    	  if (android.os.Build.VERSION.SDK_INT > 9) 
		      {
		      
			StrictMode.ThreadPolicy policy =new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
				client.connect(FTP_HOST,21);
				client.login(FTP_USER, FTP_PASS);
				client.setType(FTPClient.TYPE_BINARY);
				//client.changeDirectory("/upload/");

				client.upload(f, new MyTransferListener());

			} 
		      catch (Exception e)
		      {
				e.printStackTrace();
				try 
				{
					client.disconnect(true);
				} 
				catch (Exception e2) {
					e2.printStackTrace();
				}
			}

		
		}
		
		@Override
		public void onError(String Error, int id)
		{
			
			
		}
	});

	
	}
	else if(dbdiff.equals("VIDEO"))
	{

		
		 path=	ce.getString(6);
		 
		 //Log.w("VIDEO","VIDEO"+dbdiff+"path"+path);
		 
		 dbgrupname     =ce.getString(15);
		 dbcheckuncheck =  ce.getString(11);
		 recipientid  =ce.getString(10);
		 
	   
	     mediagpnm    =	dbgrupname;
	     medianumber  =  dbnumberin;
	     
	new webservice(null, webservice.GetFTPHostDetail.geturl("video"), webservice.TYPE_GET, webservice.TYPE_FTP_UPLD, new ServiceHitListener() {
		
	
		public void onSuccess(Object Result, int id)
		{
			

			 try
			    {
			    	imagetoupload2 = path.substring(path.lastIndexOf("/"));
				} 
			 
			    catch (Exception e) {
					e.printStackTrace();
			    }
	
			    filetype="video";
				filename=path.substring(path.lastIndexOf("/")+1);
			 
			GetFTPCre gpmodel=(GetFTPCre) Result;
			String FTP_USER=gpmodel.getGetFTPHostDetail().get(0).getFtpUser();
			String FTP_PASS=gpmodel.getGetFTPHostDetail().get(0).getFtpPassword();
		    String FTP_HOST=gpmodel.getGetFTPHostDetail().get(0).getHostName();
		    
		    urlpath=gpmodel.getGetFTPHostDetail().get(0).getFtpUrl();
		    
		    //Log.w("VIDEO","VIDEO :urlpath"+urlpath);
		    
		    String imagePath = path;
			sendurlpath= urlpath+imagetoupload2;
			
			filetype="video";	
			
			//Log.w("VIDEO","VIDEO :sendurlpath"+sendurlpath);

			//Log.w("VIDEO","VIDEO :imagePath"+imagePath);
			
	        File f = new File(imagePath); 			
	
		    FTPClient client = new FTPClient(); 	
		      try 
		      { 	
		    	  if (android.os.Build.VERSION.SDK_INT > 9) 
		      {
		      
					StrictMode.ThreadPolicy policy =new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
				client.connect(FTP_HOST,21);
				client.login(FTP_USER, FTP_PASS);
				client.setType(FTPClient.TYPE_BINARY);
				//client.changeDirectory("/upload/");

				client.upload(f, new MyTransferListener());

			} 
		      catch (Exception e)
		      {
				e.printStackTrace();
				try 
				{
					client.disconnect(true);
				} 
				catch (Exception e2) {
					e2.printStackTrace();
				}
			}

		
		}
		
		@Override
		public void onError(String Error, int id)
		{
			
			
		}
	});
	}
	
    else if(dbdiff.equals("AUDIO"))
	{
		 path=	ce.getString(6);
		 
		 //Log.w("AUDIO","AUDIO"+dbdiff+"path"+path);
		 
		 dbgrupname     =ce.getString(15);
		 dbcheckuncheck =  ce.getString(11);
		 recipientid  =ce.getString(10);
		 
	   
	     mediagpnm    =	dbgrupname;
	     medianumber  =  dbnumberin;
		
	new webservice(null, webservice.GetFTPHostDetail.geturl("audio"), webservice.TYPE_GET, webservice.TYPE_FTP_UPLD, new ServiceHitListener() {
		
	
		public void onSuccess(Object Result, int id)
		{
			

			 try
			    {
			    	imagetoupload2=path.substring(path.lastIndexOf("/"));
				} 
			 
			    catch (Exception e) {
					e.printStackTrace();
			    }
	
			    filetype="audio";
				filename=path.substring(path.lastIndexOf("/")+1);
			 
			GetFTPCre gpmodel=(GetFTPCre) Result;
			String FTP_USER=gpmodel.getGetFTPHostDetail().get(0).getFtpUser();
			String FTP_PASS=gpmodel.getGetFTPHostDetail().get(0).getFtpPassword();
		    String FTP_HOST=gpmodel.getGetFTPHostDetail().get(0).getHostName();
		    urlpath=gpmodel.getGetFTPHostDetail().get(0).getFtpUrl();
		   
		    String imagePath = path;
		    
			sendurlpath= urlpath+imagetoupload2;
			
			filetype="audio";		
	        File f = new File(imagePath); 			
	
		    FTPClient client = new FTPClient(); 	
		      try 
		      { 	
		    	  if (android.os.Build.VERSION.SDK_INT > 9) 
		      {
		      
					StrictMode.ThreadPolicy policy =new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
				client.connect(FTP_HOST,21);
				client.login(FTP_USER, FTP_PASS);
				client.setType(FTPClient.TYPE_BINARY);
				//client.changeDirectory("/upload/");

				client.upload(f, new MyTransferListener());

			} 
		      catch (Exception e)
		      {
				e.printStackTrace();
				try 
				{
					client.disconnect(true);
				} 
				catch (Exception e2) {
					e2.printStackTrace();
				}
			}

		
		}
		
		@Override
		public void onError(String Error, int id)
		{
			
			
		}
	});
	}
		
	}
	
	else
	{
     dbObject.DeleteOutboxAll();
	}

	}
	dbObject.close();
}

public class MyTransferListener implements FTPDataTransferListener
{

	public void started() 
	{}

	public void transferred(int length) 
	{}

	public void completed() 
	{
	
		fetchMobileandUserId();
				
		new webservice(null, webservice.AudioVideoPictureMessage.geturl(mediagpnm, "", InAppUserlogin, InAppPassword, UserId, "SMSMSMS", medianumber, "One Attachment", Mobile,recipientid,dbcheckuncheck, sendurlpath, filetype, filename), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() {
			
			@Override
			public void onSuccess(Object Result, int id) 
			{
				
 		    		dbObject.Open();
 		 			dbObject.DeleteOutbox(medianumber,dbOutID);
 		 			dbObject.close();
 		    	 	    	
				
			}
			
			@Override
			public void onError(String Error, int id) 
			{
				
			}
		});
			
			
		
	}
	
	
	public void aborted() 
	{

	}

	public void failed() 
	{

	}

}

public void sortdata()
{
	/**********************************Filter of Contact-CONTACT SEARCH*************************************/
  
	search.addTextChangedListener(new TextWatcher() 
    {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(count < before) 
			{   
				
			    try
			    {
			    	ccAdpt.resetData();
			    }
			    catch(Exception e)
			    {
			    	
			    }
			}
			
			try
			{
			ccAdpt.getFilter().filter(s.toString());
			}
			catch(Exception e)
			{
			}
			
		
			}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	});

    /***********************************CONTACT SEARCH*************************************/

}


@Override
public void onClick(View v) 
{
	sortdata();
	
}
public String[] fetchName()
{

dbObject.Open();

Cursor c;

c= dbObject.getInboxDetails();

int countValue = c.getCount();

String fetchData [] =new String [countValue]; 


int i=0;
while(c.moveToNext()){
	
	fetchData[i] = c.getString(5);
	i++;
}

dbObject.close();

return fetchData;

}

private void callFxnInSuccess(Object Result) 
{
	
	fetchMobileandUserId();
	 
    dataset = new ArrayList<InboxReadDummy>();
    
    String message="",alldatetime="",time="",date="",type="",I_URL="",fileType="";
           
	TotalMessageInboxmodel ibx=(TotalMessageInboxmodel) Result;
	
	
	if(ibx.getTotalMessagesOfSingleSenderUser().size()>0)
	{	
		int totalSizeList = ibx.getTotalMessagesOfSingleSenderUser().size();
			
		if(totalSizeList == 1)
		{
			dbObject.Open();
							
			    message        =ibx.getTotalMessagesOfSingleSenderUser().get(0).getMessage().trim();
			    alldatetime    =ibx.getTotalMessagesOfSingleSenderUser().get(0).getMessageInsertDateTime();
			    fileType       =ibx.getTotalMessagesOfSingleSenderUser().get(0).getFileType();
			   
			    try {
			    	
			    	// fetch url and check it have http:// or not if not then add this
			    	I_URL     =ibx.getTotalMessagesOfSingleSenderUser().get(0).getFilePath();
			    			    	
			    	
			    	if(I_URL == null || I_URL.length() < 3)
			    	{
						
					}
					else
					{
						String checkhttp = I_URL.substring(0,7);
						
						//Log.w("RM","RM ::::::::(checkhttp):"+checkhttp);
						
						if(checkhttp.equalsIgnoreCase("http://")){
							
						}
						else{
							I_URL = "http://"+I_URL;
						}
						
						//Log.w("RM","RM ::::::::(logo):"+I_URL);
						
					
					}
				} catch (Exception e) {
					e.printStackTrace();
					I_URL  = "";
				}
			  
			    // fetch url and check it have http:// or not if not then add this
			    
			    //**********************CODE WITH TIME INTERVAL******************************
			    String SHours  = ""; 
			    String SMinuts = ""; 
			    String SSecond = "";
			    
			    String DDay    = "";
			    String DHours  = ""; 
			    String DMinuts = ""; 
			    String DSecond = "";
			    
			    int THours ; 
			    int TMinuts ; 
			    int TSecond ;
			    int TDate= 0;
			    
			    if(alldatetime.length()> 0 && alldatetime !=null)
			    {
			    	    try {
							int pod = alldatetime.indexOf("T");
    						time   = alldatetime.substring((pod+1),(pod+9));
							
    						//Log.w("INBOX READ","INBOX READ::::(server time):"+time);
							
							  //  ********************************* time arrenge as a time interval
						    String[] separated = time.split(":");
						    SHours  = separated[0]; // this will contain "Fruit"
						    SMinuts = separated[1]; // this will contain " they taste good"
						    SSecond = separated[0];
						    
						    // fetch time interval interval from database
							
						} catch (Exception e) {
							e.printStackTrace();
						}
			    	    
			    	    Cursor t;
					    t = dbObject.getTimeInfo();
					    
					    String TimeDay = "";
					    String TimeHours = "";
					    String TimeMinutes = "";
					    String TimeSeconds = "";
					    
					    while(t.moveToNext()){
					    	
					    	TimeDay     = t.getString(1);
					    	TimeHours   = t.getString(2);
					    	TimeMinutes = t.getString(3);
					    	TimeSeconds = t.getString(4);
					    }
					    //Log.w("INBOX READ","INBOX READ::DB::(TimeHours):"+TimeHours+",TimeMinutes"+TimeMinutes+",TimeSeconds"+TimeSeconds);				  
					    
					    THours  = (Integer.parseInt(SHours.trim())  + Integer.parseInt(TimeHours.trim())); 
					    TMinuts = (Integer.parseInt(SMinuts.trim()) + Integer.parseInt(TimeMinutes.trim())); 
					    TSecond = (Integer.parseInt(SSecond.trim()) + Integer.parseInt(TimeSeconds.trim())); 
				
					    //Log.w("INBOX READ","INBOX READ::DB::(THours):"+THours+",TMinuts"+TMinuts+",TSecond"+TSecond);				  
					    
					    TDate = Integer.parseInt(TimeDay.trim());
					    		
					    currentdate = getPriviousdate(TDate);
					    
					    currentTime = ""+THours+":"+TMinuts+":"+TSecond;
			    }
			 
			    //**********************CODE WITH TIME INTERVAL******************************
			   
			    
			    //  ********************************* time arrenge as a time interval
			    
			    date        =alldatetime.substring(0,10);
			    type        =ibx.getTotalMessagesOfSingleSenderUser().get(0).getType();
			    
			    //Log.w("INBOX READ","INBOX READ::::::::ADD DATA TO DATABASE:"+message+","+InAppUsermobile+","+currentTime+","+UserId+","+type+","+currentdate+",I_URL:"+I_URL);
		    
			    int checkStatus = 0;
	     	  
	     	    int dataStatus = 0;
	        	  
	      	    if(haveDatamessage())
	      	    {
	      		
	      		//  dbObject.Open();
	      		  Cursor cc ;
	      		  cc = dbObject.getallmessageschat();
	      		 
	      		 while(cc.moveToNext())
	      		 {
	      		 dataStatus = cc.getInt(12);
	      		 } 
	      		  
	      		//  dbObject.close();
	      		  
	      		  dataStatus += 1;
	      	    }
	     	   
	      	    //Log.w("TAG"," sendermobile :5::(sendermobile):"+Sendermobile);
	      	    
			    //String message,String number,String time,String uid,String type,String date, String I_URL, String MsgType, String MediaType, String SendStatus	    
			    dbObject.InboxMessageAll(message,Sendermobile,currentdate+","+currentTime,UserId,type,currentdate,I_URL,"R_M",fileType,"0",type,""+checkStatus,dataStatus);
			  			
			    dbObject.close();
		}	
		else{
			
			dbObject.Open();
			
			for(int i=(totalSizeList-1);i>=0;i--)
			{	
				//Log.w("DDF","DDF kk (i)"+i);
										
				    message     =ibx.getTotalMessagesOfSingleSenderUser().get(i).getMessage().trim();
				    alldatetime =ibx.getTotalMessagesOfSingleSenderUser().get(i).getMessageInsertDateTime();
				    fileType    =ibx.getTotalMessagesOfSingleSenderUser().get(i).getFileType();
				   
				    try {
				    	
				    	
				    	I_URL           =ibx.getTotalMessagesOfSingleSenderUser().get(i).getFilePath();
				    	
				    	if(I_URL != null)
				    	{
							I_URL       =ibx.getTotalMessagesOfSingleSenderUser().get(i).getFilePath();

				    	}
				    	else
				    	{
				    		I_URL  = "";
				    	}
				    	
				    	if(I_URL == null || I_URL.length() < 3)
				    	{
							
						}
						else
						{
							String checkhttp = I_URL.substring(0,7);
							
							//Log.w("RM","RM ::::::::(checkhttp):"+checkhttp);
							
							if(checkhttp.equalsIgnoreCase("http://")){
								
							}
							else
							{
								I_URL = "http://"+I_URL;
							}
							
							//Log.w("RM","RM ::::::::(logo):"+I_URL);
							
						
						}
					} catch (Exception e) {
						e.printStackTrace();
						I_URL  = "";
					}
				    //**********************CODE WITH TIME INTERVAL******************************
				    String SHours  = ""; 
				    String SMinuts = ""; 
				    String SSecond = "";
				    
				    String DDay    = "";
				    String DHours  = ""; 
				    String DMinuts = ""; 
				    String DSecond = "";
				    
				    int THours ; 
				    int TMinuts ; 
				    int TSecond ;
				    int TDate= 0;
				    
				    if(alldatetime.length()> 0 && alldatetime !=null)
				    {
				    	    try {
								int pod = alldatetime.indexOf("T");
	    						time   = alldatetime.substring((pod+1),(pod+9));
								//Log.w("INBOX READ","INBOX READ::::(time):"+time);
								
								  //  ********************************* time arrenge as a time interval
							    String[] separated = time.split(":");
							    SHours  = separated[0]; // this will contain "Fruit"
							    SMinuts = separated[1]; // this will contain " they taste good"
							    SSecond = separated[0];
							    
							    // fetch time interval interval from database
								
							} catch (Exception e) {
								e.printStackTrace();
							}
				    	    
				    	    Cursor t;
						    t = dbObject.getTimeInfo();
						    
						    String TimeDay = "";
						    String TimeHours = "";
						    String TimeMinutes = "";
						    String TimeSeconds = "";
						    
						    while(t.moveToNext()){
						    	
						    	TimeDay     = t.getString(1);
						    	TimeHours   = t.getString(2);
						    	TimeMinutes = t.getString(3);
						    	TimeSeconds = t.getString(4);
						    }
						    					  
						    
						    THours  = (Integer.parseInt(SHours)  + Integer.parseInt(TimeHours)); 
						    TMinuts = (Integer.parseInt(SMinuts) + Integer.parseInt(TimeMinutes)); 
						    TSecond = (Integer.parseInt(SSecond) + Integer.parseInt(TimeSeconds)); 
					
						    TDate = Integer.parseInt(TimeDay.trim());
						    		
						    currentdate = getPriviousdate(TDate);
						    
						    currentTime = ""+THours+":"+TMinuts+":"+TSecond;
				    }
				 
				    //**********************CODE WITH TIME INTERVAL******************************
				    
				    date        =alldatetime.substring(0,10);
				    type        =ibx.getTotalMessagesOfSingleSenderUser().get(i).getType();
				    
				    //Log.w("INBOX READ","INBOX READ::::::::ADD DATA TO DATABASE:"+message+","+InAppUsermobile+","+currentTime+","+UserId+","+type+","+currentdate+",I_URL:"+I_URL);
			    
				    int checkStatus = 0;
		     	   		     	    
		     	    int dataStatus = 0;
		        	  
		      	    if(haveDatamessage()){
		      		
		      		//  dbObject.Open();
		      		  Cursor cc ;
		      		  cc = dbObject.getallmessageschat();
		      		 
		      		 while(cc.moveToNext()){
		      			 dataStatus = cc.getInt(12);
		      		 } 
		      		  
		      		//  dbObject.close();
		      		  
		      		  dataStatus += 1;
		      	    }
		     	   
		      	    //Log.w("TAG"," sendermobile :1::(sendermobile):"+Sendermobile);
		      	    
				    //String message,String number,String time,String uid,String type,String date, String I_URL, String MsgType, String MediaType, String SendStatus	    
				    dbObject.InboxMessageAll(message,Sendermobile,currentdate+","+currentTime,UserId,type,currentdate,I_URL,"R_M",fileType,"0",type,""+checkStatus,dataStatus);
			
				
				
			}
			dbObject.close();
		}
	
	}
	else
	{
		//Log.w("INBOX READ","INBOX READ::(DATA NOT FETCH FROM SERVER):DATABASE::::(counter:)");
	
	}
	
	/*try {
		String EmergencyMessage = ibx.getTotalMessagesOfSingleSenderUser().get(0).getEmergencyMessage();
		
		  try
		  {
		  Emergency.desAct.finish();
		  } 
		  catch (Exception e)
		  {
		  }
		   
		   if(!(EmergencyMessage.equalsIgnoreCase("NO")))
		   {
		    Intent rt = new Intent(Inbox.this,Emergency.class);
		    rt.putExtra("Emergency", EmergencyMessage);
		    startActivity(rt);

		  }
	} catch (Exception e) {
		e.printStackTrace();
	}*/
}

public boolean haveDatamessage()
{
	//dbObject.Open();
	
    Cursor cs;
    
    cs= dbObject.getallmessageschat();
    
    while(cs.moveToNext())
    {
    	//dbObject.close();
    	return true;
    }
	
	
	//dbObject.close();
	return false;
}

public String getPriviousdate(int finddate)
{
	String date = "";
	try 
	{
	
		  //Log.w("TAG:DATE_2:","*******************2******************");
		    
		    Calendar cal = Calendar.getInstance();
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		  
		    cal.add(Calendar.DAY_OF_YEAR, finddate);
		    Date newDate = cal.getTime();
		    
		    date = dateFormat.format(newDate);
		    
		    //Log.w("TAG:","TIME_STAMP  ::(date)"+date);
			
			/*************************************/
			
		    return date;
	}
	catch (Exception e) {}
	 return date;

	}

@Override
public void onPause() 
{
	stop = false;
	super.onPause();   
	inboxthreadclose= true;
	//Toast.makeText(cnt, "onPause", Toast.LENGTH_SHORT).show();
}

@Override
public void onResume() 
{
	stop = true;
	super.onResume();   
	inboxthreadclose= true;
	//Toast.makeText(cnt, "onResume", Toast.LENGTH_SHORT).show();
	CallForNewMessage();
}

@Override
public void onRestart() 
{
	stop = true;
	super.onRestart(); 
	inboxthreadclose= true;
	//Toast.makeText(cnt, "onRestart", Toast.LENGTH_SHORT).show();
	CallForNewMessage();
}

@Override
public void onStop() 
{
	stop = false;
	super.onStop();  
	inboxthreadclose= true;
	//Toast.makeText(cnt, "onStop", Toast.LENGTH_SHORT).show();
}

@Override
public void onStart() 
{
	stop = true;
	super.onStart();  
	inboxthreadclose= true;
	//Toast.makeText(cnt, "onStart", Toast.LENGTH_SHORT).show();
	CallForNewMessage();
}

@Override
public void onDestroy() 
{
	stop = false;
	super.onDestroy();  
	//Toast.makeText(cnt, "onDestroy", Toast.LENGTH_SHORT).show();
	// close countinuesly running thread
	inboxthreadclose= false;
}

}
