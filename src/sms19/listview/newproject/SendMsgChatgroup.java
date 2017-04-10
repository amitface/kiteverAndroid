package sms19.listview.newproject;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import sms19.listview.adapter.GroupAdapterInbox;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.GetFTPCre;
import sms19.listview.newproject.model.GroupContactDetailModel;
import sms19.listview.newproject.model.GroupModelInboxDB;
import sms19.listview.newproject.model.InboxSentModel;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.kitever.android.R;

public class SendMsgChatgroup extends ActionBarActivity implements OnClickListener
{

	TextView rfrdformSendchatgp,textcountgp;
	ListView listSendSmsChatgp;
	CheckBox sendMessageinboxChatSendgp;
	ImageView sendviainboxchatgp,sendmediaviaigroupchat,Audio,inboxgrouptemp;
	EditText sendMessageinboxChatgp;
	ProgressDialog p;
	int size;
	String selnumall="";
	public static int sendgroupmsg=0;
	// for audio recodes ***************
	   private MediaRecorder myAudioRecorder;
	   private String outputFile = null;
	   int cnt = 0;
	   CountDownTimer t;
	   boolean audioRecordFlag = true;
	   // audio record *******************
	
	private String path,imagetoupload2, filetype,filename,urlpath,sendurlpath,selectedaudiopath;
	
	boolean checkDuplicate;
	
	DataBaseDetails dbObject =  new DataBaseDetails(this);
	String Mobile   = "",UserId   = "",Password = "";
	   	
	String message;
	String SEL_GP_NAME="",SEL_INAPP_USERID= "",SEL_INAPP_USERLOGIN = "",SEL_INAPP_USERMOBILE="", SEL_CNT_USERID="", SEL_CNT_NUMBER="",SEL_INAPP_USERPASSWORD="";

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	private static final int SELECT_PICTURE                    = 300;
	private static final int AUDIO_REQUEST                     =400;
	String selectedVideoPath,selectedImagePath;
	
	
	String InAppMobile= "",InAppPassword = "",InAppUserId="", InAppUserLogin="";
	
	Context gContext;
	String checkedstatus="";
	int countinggp;
	String gpContact[],a,b,groupContactNew[];
	
	List<GroupModelInboxDB> setdbdataInAdapter;
	GroupAdapterInbox gpadaptaer;
	private String curDate;
	private String curTime;
	public static Activity sendGroupFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_msg_chatgroup);
		
		rfrdformSendchatgp         = (TextView) findViewById(R.id.rfrdformSendchatgp);
		listSendSmsChatgp          = (ListView) findViewById(R.id.listSendSmsChatgp);
		sendMessageinboxChatSendgp = (CheckBox) findViewById(R.id.sendMessageinboxChatSendgp);
		sendviainboxchatgp         = (ImageView) findViewById(R.id.sendviainboxchatgp);
		sendMessageinboxChatgp     = (EditText) findViewById(R.id.sendMessageinboxChatgp);
		sendmediaviaigroupchat     = (ImageView) findViewById(R.id.sendmediaviaigroupchat);
		textcountgp                = (TextView) findViewById(R.id.textcountgp);
		Audio                      = (ImageView)findViewById(R.id.speakaudiogroup);
	
		inboxgrouptemp             = (ImageView) findViewById(R.id.inboxgrouptemp);
		
		// ACTION BAR **********************************************
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
		bar.setTitle(Html.fromHtml("<font color='#ffffff'>Send Message Chat Groups</font>"));
		// ACTION BAR **********************************************
		
		gContext = this;
		sendGroupFlag = this;
		
		try {
				Intent i2=getIntent();
				String setdata=i2.getStringExtra("templateID");
				sendMessageinboxChatgp.setText(setdata);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		
		// add onlick listener to send media
		sendmediaviaigroupchat.setOnClickListener(this);
		
		inboxgrouptemp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SendMsgChatgroup.this,TemplateHolder.class);
				i.putExtra("taketemplate", "group");
				startActivity(i);
			}
		});
		
		// go to contact page ******************************************
		rfrdformSendchatgp.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
				Intent i = new Intent(SendMsgChatgroup.this,SendMessageInbox.class);
				startActivity(i);
				finish();
				
			}
		});
		// go to contact page ******************************************
		
		// change text sending image after write*****************************
		sendMessageinboxChatgp.addTextChangedListener(new TextWatcher() 
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				 
				int lens = (s.length()-1);
				String st="";
				textcountgp.setText(""+lens+"\n / \n138");
			 	     
				if(s.length() > 138)
				{
					 
					if(sendMessageinboxChatSendgp.isChecked())
					{
						Toast.makeText(gContext, "length don't exceed 138 charecter", Toast.LENGTH_SHORT).show();
						sendMessageinboxChatgp.setFocusable(false);
					}
					
				 }
				else
				{
					sendMessageinboxChatgp.setFocusableInTouchMode(true);
				}
			
				if(st.length()>0)
				{
					sendviainboxchatgp.setVisibility(View.VISIBLE);	
					sendmediaviaigroupchat.setVisibility(View.INVISIBLE);
				}
				else
				{
					sendmediaviaigroupchat.setVisibility(View.INVISIBLE);	
					sendviainboxchatgp.setVisibility(View.VISIBLE);	
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
		// change text sending image after write*****************************
		
		
		sendviainboxchatgp.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
			    // call service for send textmessage
				callServiceforSendTxtMsg();
				
			}

		});
	
		// audio on click recording listener ****************************
	    Audio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
		        intent.setType("audio/*");
		        intent.setAction(Intent.ACTION_GET_CONTENT);
		        startActivityForResult(Intent.createChooser(intent,"Select Audio "), AUDIO_REQUEST);
		         
			}
		});
	    
	    // audio on click recording listener ****************************************
		
		
		// call function to fetch data from database..
		callGroupAdapterwithDatabase();
		
		
	}
		

	private void callGroupAdapterwithDatabase()
	{
		try
		{
			/*************************************Group************************************/

		//set groups name in adapter from database
		  gpContact = groupContacts();
		  groupContactNew = groupContactsCount();
		  
		  ArrayList<String> contactGrouplist    = new ArrayList<String>(Arrays.asList(gpContact));
		  ArrayList<String> contactGrouplistNew = new ArrayList<String>(Arrays.asList(groupContactNew));

		  setdbdataInAdapter    =new ArrayList<GroupModelInboxDB>();
				  
		  Iterator<String> itr  = contactGrouplist.iterator(); 
		  Iterator<String> itrq = contactGrouplistNew.iterator(); 
		  
		  while(itr.hasNext())
		  {
		  a=itr.next();
		  b=itrq.next();
		  setdbdataInAdapter.add(new GroupModelInboxDB(a,b));
		  }
		  
		  listSendSmsChatgp.setAdapter(gpadaptaer=new GroupAdapterInbox(setdbdataInAdapter,gContext));
		  
		  }
		catch(Exception e)
		{
			//Toast.makeText(this, "inside group"+e+""+gpContact, Toast.LENGTH_LONG).show();
			
		}
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
			   
			   Mobile   = c.getString(1);
			   UserId   = c.getString(3);
			   Password = c.getString(5);
			   
		   }
		  }	   
		   dbObject.close();
	}

	public  String[] groupContacts()
	{
		dbObject.Open();	
		Cursor c;
		
		c= dbObject.getGroupALL();
		
		countinggp = c.getCount();
		
		String icontact[]= new String[countinggp];
		
	    if(c.getCount() >= 1)
	    {
	    
	    	int i=0;
	        while(c.moveToNext() && i<countinggp)
	    	  {  
	    	 	
	        	icontact[i] =c.getString(1);
	    		i++;
	    	}
	    }
	    
	    dbObject.close();
	   
		return icontact;
		
	}
	
	public  String[] groupContactsCount()
	{
		dbObject.Open();	
		Cursor c;
		
		c= dbObject.getGroupALL();
		
		countinggp = c.getCount();
		
		String icontact[]= new String[countinggp];
		
	    if(c.getCount() >= 1)
	    {
	    
	    	int i=0;
	    	  while(c.moveToNext() && i<countinggp)
	    	  {  
	    		
	    		  //Log.w("TAG","VALUES"+c.getString(0)+"::"+c.getString(1)+"::"+c.getString(2));
	    		  icontact[i] =c.getString(2);
	    		  //Log.w("TAG","VALUES"+icontact[i]+"::i="+i);
	    		  
	    		  i++;
	    	}
	    }
	    
	    dbObject.close();
	   
		return icontact;
		
	}
	
	public void fetchDataToSelectedUser() 
	{
		dbObject.Open();
		  
		   Cursor c;
		   
		   c= dbObject.getSelRegUserDetails();
		   int count=c.getCount();
		   
		  if(count>=1)
		  {		
		   while(c.moveToNext())
		   {
						 		   
			   SEL_INAPP_USERID       = c.getString(0);
			   SEL_INAPP_USERLOGIN    = c.getString(1);
			   SEL_INAPP_USERMOBILE   = c.getString(2);
			   SEL_INAPP_USERPASSWORD = c.getString(3);
			   SEL_CNT_USERID         = c.getString(4);
			   SEL_CNT_NUMBER         = c.getString(5);
			  
			   
		   }
		  }	   
		   dbObject.close();
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.sendmediaviaigroupchat:
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

			           /*Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			           intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			           startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);*/
			       Intent intent = new Intent();             
	    	     intent.setType("video/*");
	    	     intent.setAction(Intent.ACTION_GET_CONTENT);
	    	     startActivityForResult(Intent.createChooser(intent,"SelectVideo"), CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
                  }});
			    
			    alertDialog.show();

			break;
		}
		
	}
	
	

	private void callServiceforSendTxtMsg()
	{
				  
		if(sendMessageinboxChatSendgp.isChecked())
		{
			checkedstatus="1";	
		}
		else
		{
			checkedstatus="0";
		}
				
	    int dataStatus = 0;
     	  
   	     if(haveDatamessage())
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
   	     
   	      fetchCurrentTimeDate();
   	 
		  dbObject.Open();
		  
		   Cursor c;
		 
		   c= dbObject.getSelRegUserDetails();
		   int count=c.getCount();
		   //Log.e("Thanks for calling me", "Thanks calling me"+count);
		   //Log.e("MessageSend", "MessageSend"+"inside database11111"+"INSIDE DB"+count);
		  
			   while(c.moveToNext())
			   {
				   //Log.e("MessageSend", "MessageSend1111"+"inside database22222"+SEL_GP_NAME);
				   message = sendMessageinboxChatgp.getText().toString();	
				   
				   SEL_INAPP_USERID       = c.getString(0);
				   SEL_INAPP_USERLOGIN    = c.getString(1);
				   SEL_INAPP_USERMOBILE   = c.getString(2);
				   SEL_INAPP_USERPASSWORD = c.getString(3);
				   SEL_CNT_USERID         = c.getString(4);
				   SEL_CNT_NUMBER         = c.getString(5);
				   SEL_GP_NAME            = c.getString(6);
				   //Log.e("MessageSend", "MessageSend"+"inside database3333333"+SEL_GP_NAME);
				   if(selnumall.contains(SEL_GP_NAME))
				   {
					   
				   }
				   else
				   {
					   
					   if(selnumall.length()>0)
					   {
					 selnumall =selnumall+","+SEL_GP_NAME; 
					   }
					   else
					   {
					selnumall = SEL_GP_NAME;
					   }
				   }
				  
				   
				   checkDuplicate = checkInboxTime(SEL_CNT_NUMBER);
					
					if(checkDuplicate)
					{
					dbObject.addInboxItem(SEL_INAPP_USERID, curTime, message, "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","TXT",checkedstatus);
					}
					
					else
					{
					 dbObject.deleteInboxItemNumber(SEL_CNT_NUMBER);
					 dbObject.addInboxItem(SEL_INAPP_USERID, curTime, message, "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","TXT",checkedstatus);
					}
					
					dbObject.InboxMessageAll(message, SEL_CNT_NUMBER, curDate+","+curTime, SEL_INAPP_USERID,SEL_INAPP_USERID, curDate, "", "S_M", "TXT","1","",checkedstatus,dataStatus);
					//dbObject.DeleteSelRegisterContactChat(SEL_CNT_NUMBER);  	
			   }
	
	          dbObject.close();

				checkgroupcount(SEL_GP_NAME);
					
			if(size>500)
			{
			Toast.makeText(SendMsgChatgroup.this, "You can not select group more than 500 contacts", Toast.LENGTH_SHORT).show();
			}
			else
			{
				if(checkedstatus.trim().equalsIgnoreCase("0"))
				{
					 //Log.w("MessageSend","SELGROUPNAME sel gp name"+selnumall+","+SEL_GP_NAME);			  
			     	 	
	    		  message = sendMessageinboxChatgp.getText().toString();	    
				   fetchDetaisUser();
				   p=ProgressDialog.show(SendMsgChatgroup.this, null, "Wait.....");
				   new webservice(null, webservice.MessageSendToRecipient.geturl("", InAppUserLogin, InAppPassword, InAppUserId, "SMSSMS", "", message, InAppMobile, "",checkedstatus,selnumall), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() 
					{	
								 
					  @Override
				    public void onSuccess(Object Result, int id) 
					{
						  p.dismiss();
					InboxSentModel mod=(InboxSentModel) Result;
								 		  
					try
					{
					//Toast.makeText(gContext,""+mod.getMessageSendToRecipients().get(0).getResult(), Toast.LENGTH_LONG).show();
					}
					catch(Exception ER)
					{}
								 		    
								 		   try {
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
												    Intent rt = new Intent(SendMsgChatgroup.this,Emergency.class);
												    rt.putExtra("Emergency", EmergencyMessage);
												    startActivity(rt);

												  }
											} catch (Exception e) {
												e.printStackTrace();
											}
								 		   	
								 			try {
												Inbox.sendInboxFlag.finish();
											} catch (Exception e) {
									
											}
								 		   	
								 			   Intent send=new Intent(SendMsgChatgroup.this,Inbox.class);
				                               send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											   startActivity(send);
											   finish();
											   
										
										}
										
										@Override
										public void onError(String Error, int id) 
										{
											 try {
												//Toast.makeText(gContext,""+Error, Toast.LENGTH_LONG).show();
											} catch (Exception e) {
												e.printStackTrace();
											}
											   dbObject.Open();
											   dbObject.addOutboxServiceData(message,"", curTime, curDate, SEL_INAPP_USERID, SEL_INAPP_USERID, "", "S_M", "TXT", "0","",""+checkedstatus,"SendMsgChatgroup",0,selnumall,0);
                                               dbObject.close();

										}
									});	
				
					}		
			   			
		   if(checkedstatus.trim().equalsIgnoreCase("1"))
		   {
			  
			   message = sendMessageinboxChatgp.getText().toString();	    
			  
			   fetchDetaisUser();
				  //Log.w("MessageSend","sel gp name"+selnumall);	 	  
			   new webservice(null, webservice.MessageSendToRecipient.geturl("", InAppUserLogin, InAppPassword, InAppUserId, "SMSSMS", "", message, InAppMobile, "",checkedstatus,selnumall), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() 
				 {	
					 
				 		    @Override
							public void onSuccess(Object Result, int id) 
							{
					 		   	InboxSentModel mod=(InboxSentModel) Result;
					 		  
					 		   	try
								{
								 Toast.makeText(gContext,""+mod.getMessageSendToRecipients().get(0).getResult(), Toast.LENGTH_LONG).show();
								}
								catch(Exception ER)
								{}
					 		   	
					 			try 
					 			{
								Inbox.sendInboxFlag.finish();
								} 
					 			catch (Exception e) 
								{
						
								}
					 		   	
					 			   Intent send=new Intent(SendMsgChatgroup.this,Inbox.class);
	                               send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								   startActivity(send);
								   finish();
								   
							
							}
							
							@Override
							public void onError(String Error, int id) 
							{
								 try {
									Toast.makeText(gContext,""+Error, Toast.LENGTH_LONG).show();
								} catch (Exception e) {
									e.printStackTrace();
								}
								 
								  dbObject.Open();
								  dbObject.addOutboxServiceData(message, "", curTime, curDate, SEL_INAPP_USERID, SEL_INAPP_USERID, "", "S_M", "TXT", "0","",""+checkedstatus,"SendMsgChatgroup",0,selnumall,0);
                                  dbObject.close();

							}
						});	
			}	
	}
	}


	/**
	 * 
	 */
	public void checkgroupcount(String GPNAME) 
	{
		new webservice(null, webservice.GetGroupContact.geturl(UserId,GPNAME), webservice.TYPE_GET, webservice.TYPE_GET_GROUP_CONTACT, new ServiceHitListener() {
			
			@Override
			public void onSuccess(Object Result,int id) {
				
		    GroupContactDetailModel model = (GroupContactDetailModel) Result;
			
		
			size=model.getGroupContactDetail().size();
			
			
	}
			
			@Override
			public void onError(String Error, int id) {
				
			}
		});
	}

			
	public void onActivityResult(final int requestCode, int resultCode, final Intent data) 
	{
		
		
		
		
	    if (resultCode == RESULT_OK)
	    {
	    	new AlertDialog.Builder(gContext)
	        .setCancelable(false)
	        .setMessage("Are you sure you want to send this media")
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

	@Override
	public void onClick(DialogInterface dialog, int which) {
		mediadsendaction(requestCode, data);
	}
	})
	.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

	@Override
	public void onClick(DialogInterface dialog, int which) {
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
	    
	    else {
	        // failed to capture image
	      //  Toast.makeText(getApplicationContext(),"Sorry! Failed to capture media", Toast.LENGTH_SHORT).show();
	    }
	    
	      
	    
	}


	/**
	 * @param requestCode
	 * @param data
	 */
	public void mediadsendaction(int requestCode, Intent data) {
		//for gallery pick image
		if (requestCode == SELECT_PICTURE)
		{
		    Uri selectedImageUri = data.getData();
		    fetchMobileandUserId();
		    
		    // fetch data from databse of first selected image.
		    String[] filePath = { MediaStore.Images.Media.DATA };
		    Cursor cursor = getContentResolver().query(selectedImageUri, filePath, null, null, null);
		    cursor.moveToFirst();
		    selectedImagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
		    
		    // add data to selected path
		    path=selectedImagePath;
		    
		    fetchCurrentTimeDate();
		   
		    if(sendMessageinboxChatSendgp.isChecked())
			{
				checkedstatus="1";	
			}
			else
			{
				checkedstatus="0";
			}
		    
		    int dataStatus = 0;
		 	  
		     if(haveDatamessage())
		     {
  
			  dbObject.Open();
			  Cursor c ;
			  c = dbObject.getallmessageschat();
			 
			 while(c.moveToNext()){
				 dataStatus = c.getInt(12);
			 } 
			  
			  dbObject.close();
			  
			  dataStatus += 1;
		     }
		    
		    dbObject.Open();
		   //image path
		    Cursor c;
			   
			   c= dbObject.getSelRegUserDetails();
			   int count=c.getCount();
			   
			  if(count>=1)
			  {		
			   while(c.moveToNext())
			   {
				   message = sendMessageinboxChatgp.getText().toString();	
				   
				   SEL_INAPP_USERID       = c.getString(0);
				   SEL_INAPP_USERLOGIN    = c.getString(1);
				   SEL_INAPP_USERMOBILE   = c.getString(2);
				   SEL_INAPP_USERPASSWORD = c.getString(3);
				   SEL_CNT_USERID         = c.getString(4);
				   SEL_CNT_NUMBER         = c.getString(5);
				   SEL_GP_NAME            =c.getString(6);
				   
				   checkDuplicate = checkInboxTime(SEL_CNT_NUMBER);
					
					if(checkDuplicate)
					{
			 		dbObject.addInboxItem(SEL_INAPP_USERID, curTime, "One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","IMAGE",checkedstatus);
					}
					else
					{
					dbObject.deleteInboxItemNumber(SEL_CNT_NUMBER);
			 		dbObject.addInboxItem(SEL_INAPP_USERID, curTime, "One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","IMAGE",checkedstatus);
					}
			   
				dbObject.InboxMessageAll("ONE ATTACHMENT", SEL_CNT_NUMBER, curDate+","+curTime, SEL_INAPP_USERID,SEL_INAPP_USERID, curDate, selectedImagePath, "S_M", "IMAGE","1","",checkedstatus,dataStatus);
				dbObject.DeleteSelRegisterContactChat(SEL_CNT_NUMBER);    	
			   }
			   }
			  
			 dbObject.close();
			 cursor.close();
			 

				checkgroupcount(SEL_GP_NAME);
					
			if(size>500)
			{
			Toast.makeText(SendMsgChatgroup.this, "You can not select group more than 500 contacts", Toast.LENGTH_SHORT).show();
			}
			else
			{
			
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
						filename=selectedImagePath.substring(selectedImagePath.lastIndexOf("/")+1);
						
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
			 
		    try {
				Inbox.sendInboxFlag.finish();
			} catch (Exception e) {

			}
		    
		    Intent send=new Intent(SendMsgChatgroup.this,Inbox.class);
		    send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(send);
			finish();
			}  
		  
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
		      
		      // add path to be show in listview
		      path=imagePath;
		      
		       if(sendMessageinboxChatSendgp.isChecked())
				{
					checkedstatus="1";	
				}
				else
				{
					checkedstatus="0";
				}
		       
		       fetchCurrentTimeDate();
		     	
		        int dataStatus = 0;
		 	  
		   	     if(haveDatamessage()){
  
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
		      Cursor c;
			   
			   c= dbObject.getSelRegUserDetails();
			   int count=c.getCount();
			   
			  if(count>=1)
			  {		
			   while(c.moveToNext())
			   {
				   message = sendMessageinboxChatgp.getText().toString();	 		   
				   SEL_INAPP_USERID       = c.getString(0);
				   SEL_INAPP_USERLOGIN    = c.getString(1);
				   SEL_INAPP_USERMOBILE   = c.getString(2);
				   SEL_INAPP_USERPASSWORD = c.getString(3);
				   SEL_CNT_USERID         = c.getString(4);
				   SEL_CNT_NUMBER         = c.getString(5);
				   SEL_GP_NAME            =c.getString(6);
				   
				   checkDuplicate = checkInboxTime(SEL_CNT_NUMBER);
					
					if(checkDuplicate)
					{
			 			 dbObject.addInboxItem(SEL_INAPP_USERID, curTime, "One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","IMAGE",checkedstatus);
					}
					
					else
					{
					
						dbObject.deleteInboxItemNumber(SEL_CNT_NUMBER);
			 			dbObject.addInboxItem(SEL_INAPP_USERID, curTime, "One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","IMAGE",checkedstatus);
					
					}
				 			   
				   	 dbObject.InboxMessageAll("ONE ATTACHMENT", SEL_CNT_NUMBER, curDate+","+curTime, SEL_INAPP_USERID,SEL_INAPP_USERID, curDate, imagePath, "S_M", "IMAGE","1","",checkedstatus,dataStatus);
					 dbObject.DeleteSelRegisterContactChat(SEL_CNT_NUMBER);    	
			   }
				  	   }
		     dbObject.close();
		     cursor.close();

				checkgroupcount(SEL_GP_NAME);
					
			if(size>500)
			{
			Toast.makeText(SendMsgChatgroup.this, "You can not select group more than 500 contacts", Toast.LENGTH_SHORT).show();
			}
			else
			{
			
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
		    				filename=selectedImagePath.substring(selectedImagePath.lastIndexOf("/")+1);
		    				
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
		    			catch (Exception e2)
		    			{
		    			e2.printStackTrace();
		    			}
		    			}

		    		
		    		}
		    		
		    		@Override
		    		public void onError(String Error, int id)
		    		{
		    			
		    			
		    		}
		    	});
		      
		      try 
		      {
			  Inbox.sendInboxFlag.finish();
			  } 
		      catch (Exception e) {}
		      
		      Intent send=new Intent(SendMsgChatgroup.this,Inbox.class);
		      send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			  startActivity(send);
			  finish();
			} 
		           	                   
		} 
		
		//video capture 
		if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE)
		{
			
			Uri selectedVideoUri = data.getData();
			
			Toast.makeText(gContext, "selectedVideoUri:"+selectedVideoUri, Toast.LENGTH_SHORT).show();
			fetchMobileandUserId();
		    // fetch data from databse of first selected image.
		    String[] filePath = { MediaStore.Video.Media.DATA };
		    Cursor cursor = getContentResolver().query(selectedVideoUri, filePath, null, null, null);
		    cursor.moveToFirst();
		    selectedVideoPath = cursor.getString(cursor.getColumnIndex(filePath[0]));
		   
		    // add path to show video
		    path=selectedVideoPath;
		    
		    if(sendMessageinboxChatSendgp.isChecked())
			{
				checkedstatus="1";	
			}
			else
			{
				checkedstatus="0";
			}
		    
		    fetchCurrentTimeDate();
		 	
		    int dataStatus = 0;
		 	  
		     if(haveDatamessage())
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
		    Cursor c;
		   
		   c= dbObject.getSelRegUserDetails();
		   int count=c.getCount();
		   
		  if(count>=1)
		  {		
		   while(c.moveToNext())
		   {
			   message = sendMessageinboxChatgp.getText().toString();	 		   
			   SEL_INAPP_USERID       = c.getString(0);
			   SEL_INAPP_USERLOGIN    = c.getString(1);
			   SEL_INAPP_USERMOBILE   = c.getString(2);
			   SEL_INAPP_USERPASSWORD = c.getString(3);
			   SEL_CNT_USERID         = c.getString(4);
			   SEL_CNT_NUMBER         = c.getString(5);
			   SEL_GP_NAME            =c.getString(6);
			   
			  checkDuplicate = checkInboxTime(SEL_CNT_NUMBER);
				
				if(checkDuplicate)
				{
				     dbObject.addInboxItem(SEL_INAPP_USERID, curTime, "One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","VIDEO",checkedstatus);
				}
				
				else
				{
				
					dbObject.deleteInboxItemNumber(SEL_CNT_NUMBER);
				    dbObject.addInboxItem(SEL_INAPP_USERID, curTime, "One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","VIDEO",checkedstatus);
				
				}
			   
 	  	   dbObject.InboxMessageAll("ONE ATTACHMENT", SEL_CNT_NUMBER, curDate+","+curTime, SEL_INAPP_USERID,SEL_INAPP_USERID, curDate, selectedVideoPath, "S_M", "VIDEO","1","",checkedstatus,dataStatus);
 		   dbObject.DeleteSelRegisterContactChat(SEL_CNT_NUMBER);    	
		   }
		   }
		  
		   dbObject.close();
		   cursor.close();

			checkgroupcount(SEL_GP_NAME);
				
		if(size>500)
		{
		Toast.makeText(SendMsgChatgroup.this, "You can not select group more than 500 contacts", Toast.LENGTH_SHORT).show();
		}
		else
		{
		
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
				    
				    String videoPath = path;
					sendurlpath= urlpath+imagetoupload2;
					
					filetype="video";	
					
					//Log.w("VIDEO","VIDEO :sendurlpath"+sendurlpath);

					//Log.w("VIDEO","VIDEO :imagePath"+videoPath);
					
			        File f = new File(videoPath); 			
			
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
		   
		    try 
		    {
			Inbox.sendInboxFlag.finish();
			}
		    catch (Exception e)
			{

			}
		    
		    Intent send=new Intent(SendMsgChatgroup.this,Inbox.class);
		    send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(send);
			finish();
		
		}
		}
		if(requestCode==AUDIO_REQUEST)
		{

		    Uri selectedVideoUri = data.getData();
		    	
		     	fetchMobileandUserId();
		    	
		        // fetch data from databse of first selected image.
		        String[] filePath = { MediaStore.Audio.Media.DATA };
		        Cursor cursor = getContentResolver().query(selectedVideoUri, filePath, null, null, null);
		        cursor.moveToFirst();
		        
		        selectedaudiopath = cursor.getString(cursor.getColumnIndex(filePath[0]));
		        
		        // add path to show video
		        path=selectedaudiopath;
		      
		        if(sendMessageinboxChatSendgp.isChecked())
		    				{
		    					checkedstatus="1";	
		    				}
		    				else
		    				{
		    					checkedstatus="0";
		    				}
		    	            
		    	            fetchCurrentTimeDate();
		    		     	
		    		        int dataStatus = 0;
		    	         	  
		    	       	     if(haveDatamessage())
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
		    	            Cursor c;
		    	 		   
		    	 		   c= dbObject.getSelRegUserDetails();
		    	 		   int count=c.getCount();
		    	 		   
		    	 		  if(count>=1)
		    	 		  {		
		    	 		   while(c.moveToNext())
		    	 		   {
		    	 			   message = sendMessageinboxChatgp.getText().toString();	 		   
		    	 			   SEL_INAPP_USERID       = c.getString(0);
		    	 			   SEL_INAPP_USERLOGIN    = c.getString(1);
		    	 			   SEL_INAPP_USERMOBILE   = c.getString(2);
		    	 			   SEL_INAPP_USERPASSWORD = c.getString(3);
		    	 			   SEL_CNT_USERID         = c.getString(4);
		    	 			   SEL_CNT_NUMBER         = c.getString(5);
		    	 			   SEL_GP_NAME            =c.getString(6);
		    	 			   
		    	 			  checkDuplicate = checkInboxTime(SEL_CNT_NUMBER);
		    					
		    					if(checkDuplicate)
		    					{
		    					     dbObject.addInboxItem(SEL_INAPP_USERID, curTime, "One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","AUDIO",checkedstatus);
		    					}
		    					
		    					else
		    					{
		    					
		    						dbObject.deleteInboxItemNumber(SEL_CNT_NUMBER);
		    					    dbObject.addInboxItem(SEL_INAPP_USERID, curTime, "One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","AUDIO",checkedstatus);
		    					
		    					}
		    	 			   
		       	  	   dbObject.InboxMessageAll("ONE ATTACHMENT", SEL_CNT_NUMBER, curDate+","+curTime, SEL_INAPP_USERID,SEL_INAPP_USERID, curDate, selectedaudiopath, "S_M", "AUDIO","1","",checkedstatus,dataStatus);
		       		   dbObject.DeleteSelRegisterContactChat(SEL_CNT_NUMBER);    	
		    	 		   }
		    	 		   }
		    	 		  
		    	 		   dbObject.close();
		    	           cursor.close();

		    				checkgroupcount(SEL_GP_NAME);
		    					
		    			if(size>500)
		    			{
		    			Toast.makeText(SendMsgChatgroup.this, "You can not select group more than 500 contacts", Toast.LENGTH_SHORT).show();
		    			}
		    			else
		    			{
		    								
		    				
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
					//Log.w("AUDIO", "AUDIO"+sendurlpath);
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

/**************stop recording and send to server*****************/	
		    	
		}
	}
			
	private void fetchCurrentTimeDate() {
		//for current time
	    
	     DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	     String date   = df.format(Calendar.getInstance().getTime());
	   
	     curDate = date.substring(0,date.indexOf("_"));
	     curTime = date.substring(date.indexOf("_")+1);
	  
	    // curTime = curDate+"::"+curTime+" ";
	     
	}		
	
	public boolean haveDatamessage(){
		dbObject.Open();
		
	    Cursor c;
	    
	    c= dbObject.getallmessageschat();
	    
	    while(c.moveToNext()){
	    	dbObject.close();
	    	return true;
	    }
		
		
		dbObject.close();
		return false;
	}

	public boolean checkInboxTime(String Time){
		
		//dbObject.Open();
		
		Cursor c;
		
		c= dbObject.getInboxDetails();
		
		int countValue = c.getCount();
		
		String checkData [] =new String [countValue]; 

		
		int i=0;
		while(c.moveToNext()){
			
			checkData[i] = c.getString(4);
			
			if(Time.trim().equalsIgnoreCase(checkData[i].trim())){
				
				//dbObject.close();
				return false;	
			}
				
			i++;
		}
		
		//dbObject.close();
		
		return true;
		
	}
	
	public void fetchDetaisUser() 
	{
		dbObject.Open();
		  
		   Cursor c;
		   
		   c= dbObject.getLoginDetails();
		   int count=c.getCount();
		   
		  if(count>=1)
		  {		
		   while(c.moveToNext()){
			   
			   InAppMobile    = c.getString(1);
			   InAppUserId    = c.getString(3);
			   InAppPassword  = c.getString(5);
			   InAppUserLogin = c.getString(6);
			   
		   }
		  }	   
		   dbObject.close();
	}
	
	public void fetchDetaisUser2() 
	{
		
		  
		   Cursor c;
		   
		   c= dbObject.getLoginDetails();
		   int count=c.getCount();
		   
		  if(count>=1)
		  {		
		   while(c.moveToNext())
		   {
			   
			   InAppMobile    = c.getString(1);
			   InAppUserId    = c.getString(3);
			   InAppPassword  = c.getString(5);
			   InAppUserLogin = c.getString(6);
			   
		   }
		  }	   
		  
	}
	
	public class MyTransferListener implements FTPDataTransferListener
	{

		@Override
		public void aborted() {
							
		}

		@Override
		public void completed() {

			 if(sendMessageinboxChatSendgp.isChecked())
	    	 {
				 checkedstatus="1";
	    	 }
	    	 else
	    	 {
	    		checkedstatus= "0"; 
	    	 }
			 
			fetchMobileandUserId();
				
			new webservice(null, webservice.AudioVideoPictureMessage.geturl("", "", InAppUserLogin, InAppPassword, InAppUserId, "SMSMSMS", selnumall, "One Attachment", InAppMobile,"",checkedstatus, sendurlpath, filetype, filename), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() {
				
				@Override
				public void onSuccess(Object Result, int id) 
				{
					try {
						Inbox.sendInboxFlag.finish();
					} catch (Exception e) {
			
					}
		 		   	
		 			   Intent send=new Intent(SendMsgChatgroup.this,Inbox.class);
                       send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					   startActivity(send);
					   finish();				 	    	
					
				}
				
				@Override
				public void onError(String Error, int id) 
				{
					dbObject.Open();
					dbObject.addOutboxServiceData("One attachment","", curTime, curDate, InAppUserId, "", selectedImagePath, "S_M", filetype, "0","",checkedstatus,"SendMessageInbox",0,selnumall,0);
				  	dbObject.close(); 	
				}
			});
		
		}

		@Override
		public void failed() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void started() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void transferred(int arg0) {
			// TODO Auto-generated method stub
			
		}

	}
	
	
}
