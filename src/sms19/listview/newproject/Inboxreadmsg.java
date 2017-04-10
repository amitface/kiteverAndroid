

package sms19.listview.newproject;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sms19.listview.adapter.Custom_Inbox_Adapter;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.GetFTPCre;
import sms19.listview.newproject.model.InboxDeliveredModel;
import sms19.listview.newproject.model.InboxReadDummy;
import sms19.listview.newproject.model.InboxSentModel;
import sms19.listview.webservice.InboxReadChat;
import sms19.listview.webservice.InboxReadChat.ServiceHitListenerInboxChat;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.kitever.android.R;

public class Inboxreadmsg extends ActionBarActivity implements OnClickListener
{
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	private static final int SELECT_PICTURE                    = 300;
	
	public static int ctinboxread = 0;
	
	  // for audio recodes ***************
	   private MediaRecorder myAudioRecorder;
	   private String outputFile = null;
	   int cnt = 0;
	   CountDownTimer t;
	   boolean audioRecordFlag = true;
	   // audio record *******************
	
	int checkimageupload;
	boolean checkDuplicate;
	String currentTime = "", currentdate ="";
	int cntb= 0 ;
	 boolean stop = true;
	String msgboxitem="",timeitem="",typeitem="",datedbitem="",UserId, dbURL = "", RecID = "";
	String message="",filetype="",InAppUsermobile="",InAppUserid="",InAppUserlogin="",sendurlpath="",urlpath="",filename="",imagetoupload2="",path="",selectedaudiopath;
  	EditText getmessage;
	ImageView sendmsg,sendimage,Audio,inboxchostemp;
	TextView date;
	ListView lv; 
	
	String InAppMobile= "",InAppPassword = "",InAppUserId="", InAppUserLogin="",MediaType = "",MessageType = "", OPTSTATUS="";
	
	int SendStatus;
	
	String recipientNumber="",recipientName="",recipientid="",choosetemplate="";
	 private String selectedImagePath,selectedVideoPath;
	Handler h;
    String selectedVideo;
	public static int checked= 0;
	
	Custom_Inbox_Adapter ccAdpt;
	
	int taskI = 0;
	Custom_Inbox_Adapter inadapter;
	Runnable checker;
	List<InboxReadDummy> dataset;
	Button optout,addtocontacts;
	DataBaseDetails dbObject=new DataBaseDetails(this);

	 Long nm;
	 int i=0;
	   int checkStatus = 0;
	 CheckBox checkboxsendapp;
	 Context gContext;
	 TextView Count,textView2;
	 String curDate = "";
     String curTime = "";
     int counervariable=1;
     String recNumber = "";
	 String recId     = "";
	 
	 /*******************************************/
	     
     // timer
     String mediagpnm="",medianumber="",dbmessagein="",dbnumberin="",dbgrupname="", dbrecipientId ="",dbtime= "",dbcheckuncheck="";
     String dbpageName ="",dbdiff="",dbmyid="",dbsenderuserid="",dbdate="",dbIurl="",dbsendingtype="",dbSstatus=""; 
 	int dbOutID,dbinboxOut,dbsendStatusRead;
 	
    
     Handler mHandler = new Handler();
     Runnable checkerTime;
     Thread thread;
     private static final int AUDIO_REQUEST =400;
/** Called when the activity is first created. */ 
	 /******************************************/
	 
     public static Activity inboxReadActivity;
 	public static boolean inboxthreadsingleclose= true;

@Override
protected void onCreate(Bundle savedInstanceState) 
{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.messagesublistlayout);
			
	Count                =(TextView) findViewById(R.id.countcharactersall);
	optout               =(Button)   findViewById(R.id.optout);
	addtocontacts        =(Button)   findViewById(R.id.addtocontacts);
	sendimage            =(ImageView)findViewById(R.id.sendimage);
	sendmsg              =(ImageView)findViewById(R.id.sendviachat);
    Audio                =(ImageView)findViewById(R.id.speakaudio);
	getmessage           =(EditText)findViewById(R.id.getMessage);
	checkboxsendapp      =(CheckBox)findViewById(R.id.checkboxsendapp);
	lv                   =(ListView)findViewById(R.id.readmessagelist);
	textView2            =(TextView) findViewById(R.id.hidetext);
	
	inboxchostemp        =(ImageView)findViewById(R.id.inboxchostemp); 
	/****************************GET INTENT DATA*****************************/
	    Intent i        = getIntent();
	    recipientName   = i.getStringExtra("Name");
	    recipientNumber = i.getStringExtra("Number");
	    recipientid     = i.getStringExtra("recipientid");
	   
	    try {
			choosetemplate  = i.getStringExtra("templateID");
			getmessage.setText(choosetemplate);
		} catch (Exception e2) {
			choosetemplate = "";
		}
	/****************************GET INTENT DATA*****************************/
	    
		// close countinuesly running thread
		inboxthreadsingleclose= true;
	    
	    /****************************ACTION BAR*****************************/
	    ActionBar adf = getSupportActionBar();
	    adf.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#04B486")));
	    adf.setTitle(Html.fromHtml("<font color='#ffffff'>"+recipientName+"</font>"));
	    adf.setDisplayHomeAsUpEnabled(true);
	    /****************************ACTION BAR*****************************/
	    
	    inboxReadActivity = this;
	    
	    //if user will not read message he will not be able to get new notification about message
	    Home.countonlyone=0;
	    gContext = this;
	  	 
	    // add onclick listener for take action
	    addtocontacts.setOnClickListener(this);
	    optout.setOnClickListener(this);
	    sendimage.setOnClickListener(this);
	    inboxchostemp.setOnClickListener(this);
	    
	    try {
			Intent i2=getIntent();
			String setdata=i2.getStringExtra("templateID");
			getmessage.setText(setdata);
		} catch (Exception e1) {
			
		}
	    // audio on click recording listener ****************************
	    Audio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent();
		        intent.setType("audio/*");
		        intent.setAction(Intent.ACTION_GET_CONTENT);
		        startActivityForResult(Intent.createChooser(intent,"Select Audio "), AUDIO_REQUEST);
				
							         
			}
		});
	    
	    // audio on click recording listener ****************************************
		  	
	   // optin ouptout functionality ***********************************************
	    try 
	    {
	    Long y = Long.parseLong(recipientName);
		//Log.w("optin /optout parameter","optin /optout parameter"+y);
		
		addtocontacts.setVisibility(View.VISIBLE);
   	    optout.setText("OptOut/Block");	
	 
   	     if(haveOptId()){
   	    	getOptStatus();
   	    	
   	    	if(OPTSTATUS.trim().equalsIgnoreCase("0"))
   	    	{
   	    		optout.setText("OptIn/UnBlock");	
   	    	}
   	    	else if(OPTSTATUS.trim().equalsIgnoreCase("1"))
   	    	{
   	    	    optout.setText("OptOut/Block");	
   	    	}
   	    	
   	     } 
   	    
		}
	    catch (NumberFormatException e) 
	    {
	    	//Log.w("!!!!!!!!!!!!optin /optout parameter","optin /optout parameter"+nm);
			
	    	addtocontacts.setVisibility(View.INVISIBLE);
	    	optout.setVisibility(View.INVISIBLE);
	    	//optout.setText("OptOut");	
    	}
	    // optin ouptout functionality ***********************************************
	    
	
	 // image change after write any text*****************************************
   	getmessage.addTextChangedListener(new TextWatcher()
	{
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			String st = "";
		
			if(s.length() > 138)
			{
				 
		  if(checkboxsendapp.isChecked())
		  {
		Toast.makeText(gContext, "Length don't exceed 138 charecter", Toast.LENGTH_SHORT).show();
		checkboxsendapp.setFocusable(false);
		  }
				
		 }
		else
		{
		checkboxsendapp.setFocusableInTouchMode(true);
		}
			
			try 
			{
			st = getmessage.getText().toString().trim();
			}
			catch (Exception e) {}
			
			if(st.length()>0)
			{
				sendmsg.setVisibility(View.VISIBLE);	
				sendimage.setVisibility(View.INVISIBLE);
			}
			else
			{
				sendmsg.setVisibility(View.INVISIBLE);	
				sendimage.setVisibility(View.VISIBLE);	
			}
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after)
		{
		}
		
		@Override
		public void afterTextChanged(Editable s) 
		{
		}
	});
    // image change after write any text*****************************************
	
    sendmsg.setOnClickListener(new OnClickListener() 
	{
	
    @Override
	public void onClick(View v)
	{
    	    	 	     
       	 message=getmessage.getText().toString().trim();
       	 
    	 //Log.w("JSR","JSR ::::::(call send message to recipient):::::::::::::(message):"+message+",recipentNumber"+recipientNumber+",recipentID"+recipientid);
       	 
    	 int checkStatus = 0;
    	 
    	 if(checkboxsendapp.isChecked())
    	 {
    		 checkStatus = 1 ;
		 }
		 else
		 {
			 checkStatus  = 0;
		 }
    	 
    	 //Log.w("FG","FG ::::::(date):"+curDate+",Time:"+curTime);
    	 
    	 int dataStatus = 0;
    	  
    	 if(haveDatamessageimage())
    	  {
    		
    		  dbObject.Open();
    		  Cursor cs ;
    		  cs = dbObject.getallmessageschat();
    		 
    		 while(cs.moveToNext())
    		 {
    		 dataStatus = cs.getInt(12);
    		 } 
    		  
    		  dbObject.close();
    		  
    		  dataStatus += 1;
    	  }
    	 
    	 fetchMobileandUserId();
   	     fetchCurrentTimeDate();
    	 
    	  dbObject.Open();
    	  dbObject.InboxMessageAll(message, recipientNumber, curDate+","+curTime, InAppUserid, InAppUserid,curDate, "", "S_M", "TXT","1",recipientid,""+checkStatus,dataStatus);
    	  dbObject.close();
    	  
    	  if(checkStatus == 0)
    	  {
    		          	        	   
        	    // check duplicate value and show last recent conversation
        	    checkDuplicate = checkInboxTime(recipientNumber);
				
			    dbObject.Open();
			    
				if(checkDuplicate)
				{
				dbObject.addInboxItem(InAppUserid, curDate+","+curTime, message, "", recipientNumber,recipientNumber, recipientid,"TXT","0");
				}
				
				else
				{
				
					dbObject.deleteInboxItemNumber(recipientNumber);
					dbObject.addInboxItem(InAppUserid, curDate+","+curTime, message, "", recipientNumber,recipientNumber, recipientid,"TXT","0");
				
				}
				dbObject.close();
    		
    		  new webservice(null, webservice.MessageSendToRecipient.geturl("", InAppUserLogin, InAppPassword, InAppUserId, "SMSSMS", recipientNumber, message, InAppMobile, recipientid,"0",""), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() 
				 {	
					 
				 		    @Override
							public void onSuccess(Object Result, int id) 
							{
					 		   	InboxSentModel mod=(InboxSentModel) Result;
					 		   	
					 		  /* try {
									String EmergencyMessage = mod.getMessageSendToRecipients().get(0).getEmergencyMessage();
									
									  try
									  {
									  Emergency.desAct.finish();
									  } 
									  catch (Exception e)
									  {
									  }
									  if(!(EmergencyMessage.equalsIgnoreCase("NO")))
									   {
									    Intent rt = new Intent(Inboxreadmsg.this,Emergency.class);
									    rt.putExtra("Emergency", EmergencyMessage);
									    startActivity(rt);

									  }
								} catch (Exception e) {
									e.printStackTrace();
								}
							*/
					 		  
					 		   	try
								{
								 Toast.makeText(gContext,""+mod.getMessageSendToRecipients().get(0).getResult(), Toast.LENGTH_LONG).show();
								}
								catch(Exception ER)
								{}
							
							}
							
							@Override
							public void onError(String Error, int id) 
							{
								 try 
								 {
									Toast.makeText(gContext,""+Error, Toast.LENGTH_LONG).show();
								} 
								 catch (Exception e) 
								{
									e.printStackTrace();
								}
								
								  int dataStatus = 0;
						    	  
						    	  if(haveDatamessageimage())
						    	  {
						    		  
						    		  dbObject.Open();
						    		  Cursor cw ;
						    		  cw = dbObject.getallmessageschat();
						    		 
						    		 while(cw.moveToNext())
						    		 {
						    			 dataStatus = cw.getInt(12);
						    		 } 
						    	
						    		 dbObject.close();
						    		 
						    		  dataStatus += 1;
						    	  }
						    	  
						    	  dbObject.Open();						 
					    		  dbObject.addOutboxServiceData(message, recipientNumber, curTime, curDate, InAppUserid, InAppUserid, selectedImagePath, "S_M", "TXT", "0", recipientid,"0","Inboxreadmsg",dataStatus,"",0);
					    		  dbObject.close();

							}
						});	
    	  
    		   			    
         }
        	  
    	  if(checkStatus == 1){
    		
    		    	   		  
       	    // check duplicate value and show in inbox page
       	    checkDuplicate = checkInboxTime(recipientNumber);
       	    
		    dbObject.Open();
		    
			if(checkDuplicate)
			{
				dbObject.addInboxItem(InAppUserid, curDate+","+curTime, message, "", recipientNumber,recipientNumber, recipientid,"TXT","1");
			}
			
			else
			{			
				dbObject.deleteInboxItemNumber(recipientNumber);
				dbObject.addInboxItem(InAppUserid,curDate+","+curTime, message, "", recipientNumber,recipientNumber, recipientid,"TXT","1");
			
			}
			dbObject.close();
    		      		  
    		  new webservice(null, webservice.MessageSendToRecipient.geturl("", InAppUserLogin, InAppPassword, InAppUserId, "SMSSMS", recipientNumber, message, InAppMobile, recipientid,"1",""), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() 
				 {	
					 
				 		    @Override
							public void onSuccess(Object Result, int id) 
							{
					 		   	InboxSentModel mod=(InboxSentModel) Result;
					 		  
					 		 /*  try {
									String EmergencyMessage = mod.getMessageSendToRecipients().get(0).getEmergencyMessage();
									
									  try
									  {
									  Emergency.desAct.finish();
									  } 
									  catch (Exception e)
									  {
									  }
									   
									   if(!(EmergencyMessage.equalsIgnoreCase("NO")))
									   {
									    Intent rt = new Intent(Inboxreadmsg.this,Emergency.class);
									    rt.putExtra("Emergency", EmergencyMessage);
									    startActivity(rt);

									  }
								} catch (Exception e) {
									e.printStackTrace();
								}*/
					 		   	try
								{
							    Toast.makeText(gContext,""+mod.getMessageSendToRecipients().get(0).getResult(), Toast.LENGTH_LONG).show();
								}
								catch(Exception ER)
								{}
							
							}
							
							@Override
							public void onError(String Error, int id) 
							{
							    try
								{
								Toast.makeText(gContext,""+Error, Toast.LENGTH_LONG).show();
								} 
							    catch (Exception e) 
							    {
								e.printStackTrace();
								}
							    
							    int dataStatus = 0;
						    	  
						    	  if(haveDatamessageimage())
						    	  {
						    		  
						    		  dbObject.Open();
						    		  Cursor cw ;
						    		  cw = dbObject.getallmessageschat();
						    		 
						    		 while(cw.moveToNext())
						    		 {
						    			 dataStatus = cw.getInt(12);
						    		 } 
						    	
						    		 dbObject.close();
						    		 
						    		  dataStatus += 1;
						    	  }
						    	  						    	 						    	  
						    	  dbObject.Open();						 
					    		  dbObject.addOutboxServiceData(message, recipientNumber, curTime, curDate, InAppUserid, InAppUserid, selectedImagePath, "S_M", "TXT", "0", recipientid,"1","Inboxreadmsg",dataStatus,"",0);
					    		  dbObject.close();


							}
						});	
    		   		 
    		  
    	  }
    	      	 
    	  getmessage.setText("");
    	  
    	  DBvaluesSet();
    	  		
		    }

	
	
	});
    
    /*****************************call database and web service also****************************/
	DBvaluesSet();
    calloutboxservice();
	callReaptedThread();
    /*****************************call database and web service also****************************/
    
}




public void callReaptedThread()
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
  	                    Thread.sleep(5000);// 1000*60= 1 min ;;; 1000 = 1 sec ;;; 1000*60*2= 2 min
  	                    mHandler.post(new Runnable()
  	                    {
                        
  	                        @Override
  	                        public void run() 
  	                        {
  	                        fetchMobileandUserId();
  	                        forthread();
  	                        }

							
  	                    });
  	                } catch (Exception e) {}
  	            }
  	           
  	        }
  	    });
	thread.start();  
}
 
public void forthread()
{
	  //Log.w("TAG","INBOX READ::::::(call service ;fetch data for single user)");
      
	  new InboxReadChat(null, InboxReadChat.TotalMessagesOfSingleSenderUser.geturl( recipientid,InAppUserId), InboxReadChat.TYPE_GET, InboxReadChat.TYPE_RECEIVE_MESSAGE_INBOX, new ServiceHitListenerInboxChat() {
			
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


private void callFxnInSuccess(Object Result) 
{
	
	fetchMobileandUserId();
	 
    dataset = new ArrayList<InboxReadDummy>();
    
    String message="",alldatetime="",time="",date="",type="",I_URL="",fileType="";
           
	InboxDeliveredModel ibx=(InboxDeliveredModel) Result;
	
	if(ibx.getTotalMessagesOfSingleSenderUser().size()>0)
	{	
		int totalSizeList = ibx.getTotalMessagesOfSingleSenderUser().size();
			
		
			
			dbObject.Open();
			
			for(int i=(totalSizeList-1);i>=0;i--)
			{	
				//Log.w("DDF","DDF kk (i)"+i);
								
				    message     =ibx.getTotalMessagesOfSingleSenderUser().get(i).getMessage();
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
				    
				    //Log.w("INBOX READ","INBOX READ::::::::ADD DATA TO DATABASE:"+message+","+InAppUsermobile+","+currentTime+","+InAppUserid+","+type+","+currentdate+",I_URL:"+I_URL);
			    
				    int checkStatus = 0;
		     	    if(checkboxsendapp.isChecked()){
		     		 checkStatus = 1 ;
		 			}
			   		 else{
			   			 checkStatus  = 0;
			   		 }
		     	    
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
		     	   
				    //String message,String number,String time,String uid,String type,String date, String I_URL, String MsgType, String MediaType, String SendStatus	    
				    dbObject.InboxMessageAll(message,recipientNumber,currentdate+","+currentTime,InAppUserid,type,currentdate,I_URL,"R_M",fileType,"0",type,""+checkStatus,dataStatus);
			
				
				
			}
			dbObject.close();
		
		
		
					 	    
		try 
		{
			DBvaluesSet();
		}
		catch (Exception e)
		{
		e.printStackTrace();
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
		    Intent rt = new Intent(Inboxreadmsg.this,Emergency.class);
		    rt.putExtra("Emergency", EmergencyMessage);
		    startActivity(rt);

		  }
	} catch (Exception e) {
		e.printStackTrace();
	}*/

}


private void fetchCurrentTimeDate() 
{
	//for current time
    
     DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
     String date   = df.format(Calendar.getInstance().getTime());
   
     curDate = date.substring(0,date.indexOf("_"));
     curTime = date.substring(date.indexOf("_")+1);
  
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

public boolean haveDatamessageimage()
{
	dbObject.Open();
	
    Cursor c;
    
    c= dbObject.getallmessageschat();
    
    while(c.moveToNext())
    {
    	dbObject.close();
    	return true;
    }
	
	
	dbObject.close();
	return false;
}

public boolean haveDatamessageimage2()
{
		
    Cursor cf;
    
    cf= dbObject.getallmessageschat();
    
    while(cf.moveToNext())
    {
    
    	return true;
    }
	

	return false;
}

public boolean haveOptId()
{
	dbObject.Open();
	
    Cursor c;
    
    c= dbObject.getOptINOUT();
    
    while(c.moveToNext())
    {
    	dbObject.close();
    	return true;
    }
	
	
	dbObject.close();
	return false;
}
public void getOptStatus() 
{
	dbObject.Open();
	  
	   Cursor c;
	   
	   c= dbObject.getOptINOUT();
	   int count=c.getCount();
	   
	   OPTSTATUS = "";
	   
	  if(count>=1)
	  {		
	   while(c.moveToNext()){
		 
		   OPTSTATUS    = c.getString(1);
		  		   
	   }
	  }	   
	   dbObject.close();
}

public void fetchMobileandUserId() 
{
	dbObject.Open();
	  
	   Cursor c;
	   
	   c= dbObject.getLoginDetails();
	   int count=c.getCount();
	   
	  if(count>=1)
	  {		
	   while(c.moveToNext()){
		   
		   InAppUserId    = c.getString(3);
		   InAppMobile    = c.getString(1);
		   InAppPassword  = c.getString(5);
		   InAppUserLogin = c.getString(6);
		   
	   }
	  }	   
	   dbObject.close();
}

public void DBvaluesSet() {
	//fetch from db when no internet connection avialable
	
	dataset = new ArrayList<InboxReadDummy>();
	
	try 
	{
		dbObject.Open();
		
		Cursor c;
	
		c=dbObject.getallmessages(recipientNumber);
		
		int countRow = c.getCount();
		
		//Log.w("JSR","JSR INBOX READ::::::(RECIPIENT_ID:)"+recipientid+":::LENGTH:"+countRow);
		
		//int counter = 0;
		
		while(c.moveToNext())
		{
			// //Log.w("JSR","JSR DB COUNT INBOX READ:::ERROR:::Coming in while loop:(counter:)"+counter);
				
			 msgboxitem  = c.getString(0);
		     // number = c.getString(1);
			 MessageType = c.getString(7);
			 timeitem    = c.getString(2);
			 typeitem    = c.getString(4);
			 datedbitem  = c.getString(5);
			 dbURL       = c.getString(6); 
			 RecID       = c.getString(10);
			 SendStatus  = c.getInt(9);
			 MediaType   = c.getString(8);
			// //Log.w("JSR","JSR INBOX READ::ERROR::::Coming in while loop:(value:)"+"(msgboxitem):"+msgboxitem+"(timeitem):"+timeitem+"(typeitem):"+typeitem+"(datedbitem):"+datedbitem+"(URL):"+dbURL);
			 
			 try
			 {
				dataset.add(new InboxReadDummy(datedbitem, msgboxitem, timeitem, typeitem, dbURL, RecID, SendStatus, MediaType,MessageType));
			} 
			 catch (Exception e)
			{
			    e.printStackTrace();
			}
		 
		   //  counter++;
	
      }
 		dbObject.close();
 		
	lv.setAdapter(ccAdpt=new Custom_Inbox_Adapter( getApplicationContext(),dataset,R.layout.row));
	lv.setSelection(lv.getAdapter().getCount()-1);
	
	}
	catch (Exception e)
	{
	e.printStackTrace();
	}
}
@Override
public void onClick(View v)
{
		
switch(v.getId())
{

case R.id.optout:
	
	getOptStatus();
	
	try {
		if(OPTSTATUS.trim().equalsIgnoreCase("1")){
			fetchuserid();
		optout.setText("OptIn/UnBlock");	
		new webservice(null,webservice.OptInOut.geturl(UserId, recipientid, "0", recipientNumber), webservice.TYPE_GET, webservice.TYPE_OPTIN, new ServiceHitListener() {
			
		@Override
		public void onSuccess(Object Result, int id) 
		{
			if(haveOptId()){
				dbObject.Open();
				dbObject.updateOptinout("1", "0");
				dbObject.close();
			}
			else{
				dbObject.Open();
				dbObject.addOptinout("1", "0");
				dbObject.close();
			}
			
		}
			
		@Override
		public void onError(String Error, int id)
		{
				
				
		}
		});
		}
		else if(OPTSTATUS.trim().equalsIgnoreCase("0")){
			
			fetchuserid();
		    optout.setText("OptOut/Block");	
		new webservice(null,webservice.OptInOut.geturl(UserId, recipientid, "1", recipientNumber), webservice.TYPE_GET, webservice.TYPE_OPTIN, new ServiceHitListener() {
				
			@Override
			public void onSuccess(Object Result, int id) 
			{
				if(haveOptId()){
					dbObject.Open();
					dbObject.updateOptinout("1", "1");
					dbObject.close();
				}
				else{
					dbObject.Open();
					dbObject.addOptinout("1", "1");
					dbObject.close();
				}
					
			}
				
			@Override
			public void onError(String Error, int id)
			{
					
					
			}
			});	
			
		}
		else{

			fetchuserid();
		 
			optout.setText("OptIn/UnBlock");	
		new webservice(null,webservice.OptInOut.geturl(UserId, recipientid, "0", recipientNumber), webservice.TYPE_GET, webservice.TYPE_OPTIN, new ServiceHitListener() {
			
		@Override
		public void onSuccess(Object Result, int id) 
		{
			if(haveOptId()){
				dbObject.Open();
				dbObject.updateOptinout("1", "0");
				dbObject.close();
			}
			else{
				dbObject.Open();
				dbObject.addOptinout("1", "0");
				dbObject.close();
			}
			
		}
			
		@Override
		public void onError(String Error, int id)
		{
				
				
		}
		});
		
		
		}
	} catch (Exception e) {
		
	}


	break;
	case R.id.addtocontacts:
	{
		Intent i1=new Intent(Inboxreadmsg.this,ContactAdd.class);
		i1.putExtra("editNumber",recipientNumber);
		startActivity(i1);
	}
	break;
	
	case R.id.inboxchostemp:{
		Intent i1=new Intent(Inboxreadmsg.this,TemplateHolder.class);
		i1.putExtra("taketemplate","inboxread");
		i1.putExtra("important",recipientName);
		i1.putExtra("important1",recipientNumber);
		i1.putExtra("important2",recipientid);
		startActivity(i1);
	}
	 break;
case R.id.sendimage:
	
{
	AlertDialog alertDialog=new AlertDialog.Builder(this).create();
	
	alertDialog.setTitle("Choose Media ...............");
	
	alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Gallery", new DialogInterface.OnClickListener() {

	      public void onClick(DialogInterface dialog, int id) {

	    	     Intent intent = new Intent();             
	    	     intent.setType("image/*");
	    	     intent.setAction(Intent.ACTION_GET_CONTENT);
	    	     startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);


	    } 
	     }); 

	    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Capture Image", new DialogInterface.OnClickListener() {

	      public void onClick(DialogInterface dialog, int id) {
	    	
	    	  Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
              startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE); 

	    }}); 

	   alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Video", new DialogInterface.OnClickListener() {

	      public void onClick(DialogInterface dialog, int id) {

	    	  Intent intent = new Intent();             
	    	     intent.setType("video/*");
	    	     intent.setAction(Intent.ACTION_GET_CONTENT);
	    	     startActivityForResult(Intent.createChooser(intent,"SelectVideo"), CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
  
	    	  
	           /*Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	           intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	           startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);*/
	      

	    }});
	    alertDialog.show();
}
	break;
}
	
} 

public void onActivityResult(final int requestCode, int resultCode, final Intent data) 
{
    if (resultCode == RESULT_OK)
    {
    

    	new AlertDialog.Builder(gContext)
        .setCancelable(false)
        .setMessage("Are you sure you want to send this media")
        .setPositiveButton("OK", new DialogInterface.OnClickListener()
        {

@Override
public void onClick(DialogInterface dialog, int which)
{
	sendmedaimessage(requestCode, data);    
}
})
.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
{

@Override
public void onClick(DialogInterface dialog, int which) 
{
//TODO Auto-generated method stub

}
})
        .show();
    	
        }
    else if (resultCode == RESULT_CANCELED)
    { 
    	// user cancelled Image capture
       Toast.makeText(getApplicationContext(),"User cancelled image capture", Toast.LENGTH_SHORT).show();
    }
    
    else 
    {
        // failed to capture image
        Toast.makeText(getApplicationContext(), "Sorry! Failed to capture media", Toast.LENGTH_SHORT).show();
    }
    
      
    
}




/**
 * @param requestCode
 * @param data
 */
public void sendmedaimessage(int requestCode, Intent data) {
	//for gallery pick image
	if (requestCode == SELECT_PICTURE)
	{
	    Uri selectedImageUri = data.getData();
	    
	    // fetch data from databse of first selected image.
	    String[] filePath = { MediaStore.Images.Media.DATA };
	    Cursor cursor = getContentResolver().query(selectedImageUri, filePath, null, null, null);
	    cursor.moveToFirst();
	    
	    selectedImagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
	    
	    // imgView.setImageBitmap(bitmap);
	  
	    //Log.w("image slecetd inside media", "hello image"+selectedImagePath);
	    
	    //String message,String number,String time,String uid,String type,String date, String I_URL, String MsgType, String MediaType, String SendStatus
	    fetchMobileandUserId();//InAppUserid
	    
	    int checkStatus = 0;
	    if(checkboxsendapp.isChecked())
	    {
		 checkStatus = 1 ;
		}
	    else
		{
		 checkStatus  = 0;
		}
	 
	    fetchCurrentTimeDate();
	   
	 int dataStatus = 0;
  	  
	if(haveDatamessageimage())
	{
	
	  dbObject.Open();
	  Cursor c ;
	  c = dbObject.getallmessageschat();
	 
	 while(c.moveToNext())
	 {
	 dataStatus = c.getInt(12);
	 } 
	  
	  dbObject.close();
	  
	  dataStatus += 1;
	}
	
	   path=selectedImagePath;
	       checkStatus = 0;
	    if(checkboxsendapp.isChecked())
	    {
		 checkStatus = 1 ;
		}
		 else
		 {
		 checkStatus  = 0;
		 }
	  
	  fetchCurrentTimeDate();
	   
	    dataStatus = 0;
	    dbObject.Open();
	    dbObject.InboxMessageAll("One attachment", recipientNumber,curDate+","+curTime, InAppUserid, InAppUserid, curDate, selectedImagePath, "S_M", "IMAGE","1",recipientid,""+checkStatus,dataStatus);
	    dbObject.close();
      
	 if(haveDatamessageimage())
	 {
	
	  dbObject.Open();
	  Cursor c ;
	  c = dbObject.getallmessageschat();
	 
	 while(c.moveToNext())
	 {
	 dataStatus = c.getInt(12);
	 } 
	  
	  dbObject.close();
	  
	  dataStatus += 1;
	 }
   
   DBvaluesSet();
   cursor.close();
new webservice(null, webservice.GetFTPHostDetail.geturl("image"), webservice.TYPE_GET, webservice.TYPE_FTP_UPLD, new ServiceHitListener() {
	

	public void onSuccess(Object Result, int id)
	{
		 try
		  {
		  imagetoupload2=path.substring(path.lastIndexOf("/"));
		  } 
		 
		    catch (Exception e) {
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

			client.upload(f, new imageTransferListener());

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
	   cursor.close();
	    //DBvaluesSet();
	    
	   
	}
	
	//for image capture from cemera
	if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
	{
		
		  String[] projection = { MediaStore.Images.Media.DATA };
		  fetchMobileandUserId();
	      Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection, null, null, null);
	      
	      int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	      
	      cursor.moveToLast();

	      String imagePath = cursor.getString(column_index_data);
	      
	        path=imagePath;
	      int checkStatus = 0;
	      
	 	 if(checkboxsendapp.isChecked())
	 	 {
	     checkStatus = 1 ;
		 }
		 else
		 {
		 checkStatus  = 0;
		 }
	  	  
	 	 fetchCurrentTimeDate();
	 	   
	 	 int dataStatus = 0;
		 dbObject.Open();
	     dbObject.InboxMessageAll("One attachment", recipientNumber,curDate+","+curTime, InAppUserid, InAppUserid, curDate, imagePath, "S_M", "IMAGE","1",recipientid,""+checkStatus,dataStatus);
	     dbObject.close();
	   
	     if(haveDatamessageimage())
	     {
		
		  dbObject.Open();
		  Cursor c ;
		  c = dbObject.getallmessageschat();
		 
		 while(c.moveToNext())
		 {
		 dataStatus = c.getInt(12);
		 } 
		  
		  dbObject.close();
		  
		  dataStatus += 1;
	     }
	    
	  DBvaluesSet();
      cursor.close();
  	new webservice(null, webservice.GetFTPHostDetail.geturl("image"), webservice.TYPE_GET, webservice.TYPE_FTP_UPLD, new ServiceHitListener() {
	
  	
	public void onSuccess(Object Result, int id)
	{
		 try
		  {
		  imagetoupload2=path.substring(path.lastIndexOf("/"));
		  } 
		 
		    catch (Exception e) {
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

			client.upload(f, new imageTransferListener());

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
	      
	
	
	//video capture 
	if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE)
	{
		
		try
		{
			Uri selectedVideoUri = data.getData();
			
			   
			// fetch data from databse of first selected image.
			//MediaStore.Images.Media.DATA
			String[] filePath = { MediaStore.Video.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedVideoUri, filePath, null, null, null);
			cursor.moveToFirst();
			
			selectedVideoPath = cursor.getString(cursor.getColumnIndex(filePath[0]));
			 path=selectedVideoPath;
     
			fetchMobileandUserId();  
			
			int checkStatus = 0;
			if(checkboxsendapp.isChecked())
			{
			 checkStatus = 1 ;
			}
			 else
			 {
			 checkStatus  = 0;
			 }
	 
			fetchCurrentTimeDate();
			
	    int dataStatus = 0;
  	  
  	   if(haveDatamessageimage())
  	   {
			
			  dbObject.Open();
			  Cursor c ;
			  c = dbObject.getallmessageschat();
			 
			 while(c.moveToNext())
			 {
			 dataStatus = c.getInt(12);
			 } 
			  
			  dbObject.close();
			  
			  dataStatus += 1;
  	   }
  	   
			dbObject.Open();
			dbObject.InboxMessageAll("One attachment", recipientNumber, curDate+","+curTime, InAppUserid,InAppUserid, curDate, selectedVideoPath, "S_M", "VIDEO","1",recipientid,""+checkStatus,dataStatus);
			dbObject.close();
			
			DBvaluesSet();
			new webservice(null, webservice.GetFTPHostDetail.geturl("video"), webservice.TYPE_GET, webservice.TYPE_FTP_UPLD, new ServiceHitListener() {
				
				
				public void onSuccess(Object Result, int id)
				{
					 try
					  {
					  imagetoupload2=path.substring(path.lastIndexOf("/"));
					  } 
					 
					    catch (Exception e) {}
					
						filetype="video";
						filename=path.substring(path.lastIndexOf("/")+1);
						
					GetFTPCre gpmodel=(GetFTPCre) Result;
					String FTP_USER=gpmodel.getGetFTPHostDetail().get(0).getFtpUser();
					String FTP_PASS=gpmodel.getGetFTPHostDetail().get(0).getFtpPassword();
				    String FTP_HOST=gpmodel.getGetFTPHostDetail().get(0).getHostName();
				    urlpath=gpmodel.getGetFTPHostDetail().get(0).getFtpUrl();
				    
				    String selectedVideo=path;
				    
					sendurlpath= urlpath+imagetoupload2;
					//Log.w("sendurlpath", "urlpath"+urlpath+"imagetoupload2"+imagetoupload2+path+"path");		
			        File f = new File(selectedVideo); 			
			
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

						client.upload(f, new imageTransferListener());

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
			       cursor.close();
				    //DBvaluesSet();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		   
	}       
	else if(requestCode==AUDIO_REQUEST)
	{

	    Uri selectedVideoUri = data.getData();
	    	
	     
	        // fetch data from databse of first selected image.
	        String[] filePath = { MediaStore.Audio.Media.DATA };
	        Cursor cursor = getContentResolver().query(selectedVideoUri, filePath, null, null, null);
	        cursor.moveToFirst();
	        
	        selectedaudiopath = cursor.getString(cursor.getColumnIndex(filePath[0]));
	        
	        // add path to show video
	        path=selectedaudiopath;
	        
	        fetchCurrentTimeDate();
	        
	        int checkStatus = 0;
	    	fetchMobileandUserId();
	    	
	   	    if(checkboxsendapp.isChecked())
	   	    {
	   		 checkStatus = 1 ;
			}
	   		 else{
	   			 checkStatus  = 0;
	   		}
	   	   						       	  					     	   
	 	   int dataStatus = 0;
	 	   if(haveDatamessageimage())
	        {
	     		
	     		  dbObject.Open();
	     		  Cursor cs ;
	     		  cs = dbObject.getallmessageschat();
	     		 
	     		 while(cs.moveToNext())
	     		 {
	     		 dataStatus = cs.getInt(12);
	     		 } 
	     		  
	     		  dbObject.close();
	     		  
	     		  dataStatus += 1;
	       }
		    dbObject.Open();
		    dbObject.InboxMessageAll("One attachment", recipientNumber,curDate+","+curTime, InAppUserid, InAppUserid, curDate, selectedaudiopath, "S_M", "AUDIO","1",recipientid,""+checkStatus,dataStatus);
		    dbObject.close();
		    
		    try
		    {
		       //check duplicate value and show last recent conversation
	    	   checkDuplicate = checkInboxTime(recipientNumber);
				
			    dbObject.Open();
			    
				if(checkDuplicate)
				{
					dbObject.addInboxItem(InAppUserid, curDate+","+curTime, message, "", recipientNumber,recipientNumber, recipientid,"AUDIO",""+checkStatus);
				}
				
				else
				{
				
					dbObject.deleteInboxItemNumber(recipientNumber);
					dbObject.addInboxItem(InAppUserid, curDate+","+curTime, message, "", recipientNumber,recipientNumber, recipientid,"AUDIO",""+checkStatus);
				
				}
				dbObject.close();
												
		    DBvaluesSet();
									
		    new webservice(null, webservice.GetFTPHostDetail.geturl("audio"), webservice.TYPE_GET, webservice.TYPE_FTP_UPLD, new ServiceHitListener() {
			
			
			public void onSuccess(Object Result, int id)
			{
				 try
				  {
				  imagetoupload2=path.substring(path.lastIndexOf("/"));
				  } 
				 
				    catch (Exception e) {}
				
					filetype="audio";
					filename=path.substring(path.lastIndexOf("/")+1);
					
				GetFTPCre gpmodel=(GetFTPCre) Result;
				String FTP_USER=gpmodel.getGetFTPHostDetail().get(0).getFtpUser();
				String FTP_PASS=gpmodel.getGetFTPHostDetail().get(0).getFtpPassword();
			    String FTP_HOST=gpmodel.getGetFTPHostDetail().get(0).getHostName();
			    urlpath=gpmodel.getGetFTPHostDetail().get(0).getFtpUrl();
			    
			    String selectedVideo=path;
			    
				sendurlpath= urlpath+imagetoupload2;
				//Log.w("sendurlpath", "urlpath"+urlpath+"imagetoupload2"+imagetoupload2+path+"path");		
		        File f = new File(selectedVideo); 			
		
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

					client.upload(f, new imageTransferListener());

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
	} catch (Exception e) {
		e.printStackTrace();
	}

			  

/**************stop recording and send to server*****************/	
	}
}

public void fetchuserid()
{

	dbObject.Open();
	  
	   Cursor c;
	   
	   c= dbObject.getLoginDetails();
	   
	  				   
	   while(c.moveToNext())
	   {
		   
		  UserId = c.getString(3);
		   
	   }
			   
	   dbObject.close();

}

public class imageTransferListener implements FTPDataTransferListener
{

	@Override
	public void aborted() {
		
	}

	@Override
	public void completed()
	{
		checkimageupload=0;
			
			
	     if(checkboxsendapp.isChecked())
	     {
	       checkStatus = 1 ;
		 }
		 else
		 {
		   checkStatus  = 0;
		 }
	     
	       fetchMobileandUserId();	
		   
	       new webservice(null, webservice.AudioVideoPictureMessage.geturl("", "", InAppUserLogin, InAppPassword, InAppUserId, "SMSMSMS", recipientNumber, "One Attachment", InAppMobile,recipientid,""+checkStatus, sendurlpath, filetype, filename), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() {
			
			@Override
			public void onSuccess(Object Result, int id) 
			{
		
			}
			
			@Override
			public void onError(String Error, int id) 
			{
				
			dbObject.Open();
		    dbObject.addOutboxServiceData("One attachment", recipientNumber, curTime, curDate, InAppUserId, "", selectedImagePath, "S_M", filetype, "0","",""+checkStatus,"SendMessageInbox",0,"",0);
			dbObject.close();  	
			
			}
		});
			
					
	}

	@Override
	public void failed() {}

	@Override
	public void started() {}

	@Override
	public void transferred(int arg0) {}
	
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
	
			new webservice(null, webservice.MessageSendToRecipient.geturl("", InAppUserlogin, InAppPassword,InAppUserid, "SMSSMS",dbnumberin, dbmessagein, InAppMobile,dbrecipientId,dbcheckuncheck,dbgrupname), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() {
					
					@Override
					public void onSuccess(Object Result, int id)
					{
						
						InboxSentModel mod=(InboxSentModel) Result;
				 		  	
					        //Log.w("NEW","NEW::::::::SUCCESS:::::::(OutID:)"+dbOutID+",sendStatusRead:"+dbsendStatusRead+",grupname:"+dbgrupname+",Numberin:"+dbnumberin);
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
					public void onError(String Error, int id) 
					{
						
						
					}
				}); 
	
		}
		else if(dbdiff.equals("IMAGE"))
		{
			if(checkimageupload==0)
			{
			checkimageupload=1;
			dbcheckuncheck =  ce.getString(11);
	
			path=	ce.getString(6);
			//Log.w("path_diff","diff"+dbdiff+"path"+path);
		
			 dbgrupname     =  ce.getString(15);
			 dbcheckuncheck =  ce.getString(11);
			 recipientid  =  ce.getString(10);	
			   
		     mediagpnm    =	dbgrupname;
		     medianumber  =  dbnumberin;
		  
		 new webservice(null, webservice.GetFTPHostDetail.geturl("image"), webservice.TYPE_GET, webservice.TYPE_FTP_UPLD, new ServiceHitListener() {
			
		
			public void onSuccess(Object Result, int id)
			{
				    try
				    {
				    imagetoupload2=path.substring(path.lastIndexOf("/"));
					} 
				 
				    catch (Exception e) {
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
		}
		else if(dbdiff.equals("VIDEO"))
		{
	
			
			 path=	ce.getString(6);
			 
			 //Log.w("VIDEO","VIDEO"+dbdiff+"path"+path);
			 
			 mediagpnm     =ce.getString(15);
			 dbcheckuncheck =  ce.getString(11);
			 recipientid  =ce.getString(10);
			 
		     mediagpnm    =	dbgrupname;
		     medianumber  =dbnumberin;
	
		new webservice(null, webservice.GetFTPHostDetail.geturl("video"), webservice.TYPE_GET, webservice.TYPE_FTP_UPLD, new ServiceHitListener() {
			
		
			public void onSuccess(Object Result, int id)
			{
				 try
				  {
				  imagetoupload2 = path.substring(path.lastIndexOf("/"));
				  } 
				 
				    catch (Exception e) {}
		
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
			 
			 mediagpnm      = ce.getString(15);
			 dbcheckuncheck = ce.getString(11);
			 recipientid    = ce.getString(10);
			 
		     mediagpnm      = dbgrupname;
		     medianumber    = dbnumberin;
			
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
		checkimageupload=0;
								
		new webservice(null, webservice.AudioVideoPictureMessage.geturl(mediagpnm, "", InAppUserLogin, InAppPassword, InAppUserId, "SMSMSMS", medianumber, "One Attachment", InAppMobile,recipientid,dbcheckuncheck, sendurlpath, filetype, filename), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() {
			
			@Override
			public void onSuccess(Object Result, int id) 
			{
				
			}
			
			@Override
			public void onError(String Error, int id) 
			{
				
			}
		});
			
				
	}
	
	
	public void aborted() 
	{
	checkimageupload=1;
	}

	public void failed() 
	{

		checkimageupload=1;
	}

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
	inboxthreadsingleclose= true;
	//Toast.makeText(gContext, "Inbox_onPause", Toast.LENGTH_SHORT).show();
}

@Override
public void onResume() 
{
	stop = true;
	super.onResume();   
	inboxthreadsingleclose= true;
	//Toast.makeText(gContext, "Inbox_onResume", Toast.LENGTH_SHORT).show();
	callReaptedThread();
}

@Override
public void onRestart() 
{
	stop = true;
	super.onRestart(); 
	inboxthreadsingleclose= true;
	//Toast.makeText(gContext, "Inbox_onRestart", Toast.LENGTH_SHORT).show();
	callReaptedThread();
}

@Override
public void onStop() 
{
	stop = false;
	super.onStop();  
	inboxthreadsingleclose= true;
	//Toast.makeText(gContext, "Inbox_onStop", Toast.LENGTH_SHORT).show();
}

@Override
public void onStart() 
{
	stop = true;
	super.onStart();  
	inboxthreadsingleclose= true;
	//Toast.makeText(gContext, "Inbox_onStart", Toast.LENGTH_SHORT).show();
	callReaptedThread();
}

@Override
public void onDestroy() 
{
	stop = false;
	super.onDestroy();  
	//Toast.makeText(gContext, "Inbox_onDestroy", Toast.LENGTH_SHORT).show();
	// close countinuesly running thread
	inboxthreadsingleclose= false;
}
}
