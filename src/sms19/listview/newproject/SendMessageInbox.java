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

import sms19.listview.adapter.SendmsgContactadaptor;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.ContactModelInboxDB;
import sms19.listview.newproject.model.GetFTPCre;
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


public class SendMessageInbox extends ActionBarActivity implements OnClickListener
{

	String InAppMobile= "",InAppPassword = "",InAppUserId="", InAppUserLogin="";
	
	String SEL_INAPP_USERID= "",SEL_INAPP_USERLOGIN = "",SEL_INAPP_USERMOBILE="", SEL_CNT_USERID="",SEL_CNT_NAME="", SEL_CNT_NUMBER="",SEL_INAPP_USERPASSWORD="";

	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	private static final int SELECT_PICTURE                    = 300;
	private static final int AUDIO_REQUEST                     = 400;
	 // for audio recodes ***************
	   private MediaRecorder myAudioRecorder;
	   private String outputFile = null;
	   int cnt = 0;
	   CountDownTimer t;
	   boolean audioRecordFlag = true;
	   // audio record *******************
	
	String selectedVideoPath,selectedImagePath,selectedaudiopath;
	ProgressDialog p;
	String message,mob,UseI ;
	
	String selnumall="";
	
	SendmsgContactadaptor cAdpt;
	
	Context gContext;
	
	boolean checkDuplicate;
		
	ListView ContactShowList;
	ImageView sendviainboxchat,sendmediaviainboxchat,Audio;
	EditText sendMessageinboxedit;
	CheckBox sendMessageinboxChatSend;
	
	TextView rfrdformSendchat,textcount;
	
	public static Activity sendContactFlag;
	
	//create object of database
	DataBaseDetails dbObject=new DataBaseDetails(this);
	
	ProgressDialog contact;
	
	List<ContactModelInboxDB> setdbdataInAdapterInd;
	
	String DbDATA,DbDATA1;
	int counting;
	String checkedFlagforMuliple;
    public static int messageinboxread=0;
	private String curDate;
    ImageView inboxchostemp;
	private String curTime,path,imagetoupload2, filetype,filename,urlpath,sendurlpath;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_message_inbox);
		
		ContactShowList          = (ListView)  findViewById(R.id.listSendSmsChat);
		sendviainboxchat         = (ImageView) findViewById(R.id.sendviainboxchat);
		sendMessageinboxedit     = (EditText)  findViewById(R.id.sendMessageinboxChat);
		rfrdformSendchat         = (TextView) findViewById(R.id.rfrdformSendchat);
		sendMessageinboxChatSend = (CheckBox) findViewById(R.id.sendMessageinboxChatSend);
		sendmediaviainboxchat    = (ImageView) findViewById(R.id.sendmediaviainboxchat);
		textcount                = (TextView) findViewById(R.id.textcount);
		Audio                    = (ImageView)findViewById(R.id.speakaudioinbox);
		inboxchostemp             =(ImageView)findViewById(R.id.inboxchostemp);
		sendContactFlag = this;
		gContext = this;
		
		// ACTION BAR*****************************************
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
		bar.setTitle(Html.fromHtml("<font color='#ffffff'>Send Message Chat Contacts</font>"));
		// ACTION BAR*****************************************
		  try
		  {
				Intent i2=getIntent();
				String setdata=i2.getStringExtra("templateID");
				sendMessageinboxedit.setText(setdata);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  inboxchostemp.setOnClickListener(this);
		// set onclick listener
		sendmediaviainboxchat.setOnClickListener(this);
		
		// go to chat group page
		rfrdformSendchat.setOnClickListener(new OnClickListener() 
		{
			@Override
		    public void onClick(View v) 
	    	{
			Intent i = new Intent(SendMessageInbox.this,SendMsgChatgroup.class);
			startActivity(i);
			finish();
			}
		});
		
		// change image according to text write in edittext*********************************
		sendMessageinboxedit.addTextChangedListener(new TextWatcher() 
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				int lens = (s.length()-1);
				String st = "";
				
				textcount.setText(""+lens+"\n / \n138");
				
				if(s.length() > 138)
				{
					 
					if(sendMessageinboxChatSend.isChecked())
					{
					Toast.makeText(gContext, "You can't enter more than 138 characters", Toast.LENGTH_SHORT).show();
					sendMessageinboxedit.setFocusable(false);
					}
					
				 }
				else
				{
				 sendMessageinboxedit.setFocusableInTouchMode(true);
				}
				
				try 
				{
			    st = sendMessageinboxedit.getText().toString();
				} 
				catch (Exception e)
				{
					
				}
				
				if(st.length()>0)
				{
				sendviainboxchat.setVisibility(View.VISIBLE);	
				sendmediaviainboxchat.setVisibility(View.INVISIBLE);
				}
				else
				{
				sendviainboxchat.setVisibility(View.INVISIBLE);	
				sendmediaviainboxchat.setVisibility(View.VISIBLE);	
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
			
			}
		});
		
		// change image according to text write in edittext*********************************
		
		// after click again user will out of focasable mode*******************************
		sendMessageinboxChatSend.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				
				if(sendMessageinboxChatSend.isChecked())
				{
			
				}
				else
				{
				 sendMessageinboxedit.setFocusableInTouchMode(true);
				}
				
			}
		});
		// after click again user will out of focasable mode******************************* 
		
		// text message send after click msg send icon*************************************
		sendviainboxchat.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				// call fxn to send text message
			callWebServiceForSendmsg();

			}

			
		});
		// text message send after click msg send icon*************************************
		
		
		// audio on click recording listener ****************************
	    Audio.setOnClickListener(new OnClickListener() 
	    {
			
			@Override
			public void onClick(View v)
			{
				
				
				Intent intent = new Intent();
		        intent.setType("audio/*");
		        intent.setAction(Intent.ACTION_GET_CONTENT);
		        startActivityForResult(Intent.createChooser(intent,"Select Audio "), AUDIO_REQUEST);
				/*//AUDIO
				if(audioRecordFlag ){

					  audioRecordFlag =  false;
					  Audio.setBackgroundResource(R.drawable.mice_red);
					
	                *//**************start recording*********************************************************//*
				        try {
		   		            	
			              DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			          	  String date   = df.format(Calendar.getInstance().getTime());
			          	   	                          	                        	
			          	  outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording"+date+".3gp";
			   	       	      
			       	      myAudioRecorder=new MediaRecorder();
			       	      myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			       	      myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			       	      myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
			       	      myAudioRecorder.setOutputFile(outputFile);
			       	      
			              myAudioRecorder.prepare();
			              myAudioRecorder.start();
			              
			              Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
			            }
			          		         			            
			            catch (Exception e) 
			            {
			              
			            }
			        	              
		             

		            t = new CountDownTimer( Long.MAX_VALUE , 1000) {

		                    @Override
		                    public void onTick(long millisUntilFinished) {

		                     
								cnt++;
		                        String time = new Integer(cnt).toString();

		                           long millis = cnt;
		                           int seconds = (int) (millis / 60);
		                           int minutes = seconds / 60;
		                           seconds     = seconds % 60;

		                           getmessage.setText(String.format("%d:%02d:%02d", minutes, seconds,millis));

		                    }

		                    @Override
		                    public void onFinish() {            }
		                };
		                t.start();
			         
			*//**************start recording*******************************************************//*
				}
				else if(!audioRecordFlag){
					
					 audioRecordFlag =  true;
					 Audio.setBackgroundResource(R.drawable.mice);
					
					*//**************stop recording and send to server*********************************//*
							
					        try {
								 myAudioRecorder.stop();
								 myAudioRecorder.release();
								 myAudioRecorder  = null;
								
								Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
								
								 // add audio sending file *************************************
													            
						            int checkStatus = 0;
						            
						       	    if(sendMessageinboxChatSend.isChecked()){
						       		 checkStatus = 1 ;
						   			}
							   		 else{
							   		 checkStatus  = 0;
							   		}
						       	   						       	  					     	   
						     	   int dataStatus = 0;
						     	   
						     	   if(haveDatamessage())
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
						   			   
						   			  if(selnumall.contains(SEL_CNT_NUMBER))
									   {
									   }
									   else
									   {
										   
										   if(selnumall.length()>0)
										   {
										   selnumall =selnumall+","+SEL_CNT_NUMBER; 
										   }
										   else
										   {
										   selnumall = SEL_CNT_NUMBER;
										   }
									   }
						   			  
						   			    checkDuplicate = checkInboxTime(SEL_CNT_NUMBER);
										
										if(checkDuplicate)
										{
								   		dbObject.addInboxItem(SEL_INAPP_USERID, curTime,"One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","AUDIO",checkedFlagforMuliple);
										}
										
										else
										{
											dbObject.deleteInboxItemNumber(SEL_CNT_NUMBER);
								   			dbObject.addInboxItem(SEL_INAPP_USERID, curTime,"One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","AUDIO",checkedFlagforMuliple);
										
										}
						   			   
						   			 dbObject.InboxMessageAll("One attachment", SEL_CNT_NUMBER, curDate+","+curTime, SEL_INAPP_USERID,SEL_INAPP_USERID, curDate, selectedImagePath, "S_M", "AUDIO","1","",checkedFlagforMuliple,dataStatus);
						   	 	     dbObject.DeleteSelRegisterContactChat(SEL_CNT_NUMBER);			
							   			            	
						   		   }
						   		   } 
						   				   		  
						            dbObject.close();
																	
								   														
								    new webservice(null, webservice.GetFTPHostDetail.geturl("audio"), webservice.TYPE_GET, webservice.TYPE_FTP_UPLD, new ServiceHitListener() {
									
									
									public void onSuccess(Object Result, int id)
									{
										

										 try
										    {
										    	imagetoupload2=outputFile.substring(outputFile.lastIndexOf("/"));
											} 
										 
										    catch (Exception e) {
												e.printStackTrace();
										    }
								
										    filetype="audio";
											filename=outputFile.substring(outputFile.lastIndexOf("/")+1);
										 
										GetFTPCre gpmodel=(GetFTPCre) Result;
										
										
										
										String FTP_USER=gpmodel.getGetFTPHostDetail().get(0).getFtpUser();
										String FTP_PASS=gpmodel.getGetFTPHostDetail().get(0).getFtpPassword();
									    String FTP_HOST=gpmodel.getGetFTPHostDetail().get(0).getHostName();
									    urlpath=gpmodel.getGetFTPHostDetail().get(0).getFtpUrl();
									   
									    String imagePath = outputFile;
									    
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
								    
								  
									  
							} catch (Exception e) {
						    	e.printStackTrace();
							}
					
					  *//**************stop recording and send to server*****************//*
				}
				          
				         
			*/}
		});
	    
	    // audio on click recording listener ****************************************
			
		
		// fetch data from database for showing  in list************************************
		callContactAdapterwithdatabase();
	}
	
	public void callContactAdapterwithdatabase()
	{
		/*************************************contacts************************************/

		//set contacts in adapter from database
		  String[] Dbname   = getIndividuleDetailsName();
		  String[] Dbnumber = getIndividuleDetailsNumber();
		  
		  ArrayList<String> contactlistName = new ArrayList<String>(Arrays.asList(Dbname));
		  ArrayList<String> contactlistNumber = new ArrayList<String>(Arrays.asList(Dbnumber));
		 	
		  setdbdataInAdapterInd=new ArrayList<ContactModelInboxDB>();
		  Iterator<String> itreName   = contactlistName.iterator();
		  Iterator<String> itreNumber = contactlistNumber.iterator();
		  
		  while(itreNumber.hasNext())
		  {
			  
		   DbDATA  =itreName.next();
		   DbDATA1 =itreNumber.next();
		   setdbdataInAdapterInd.add(new ContactModelInboxDB(DbDATA,DbDATA1));
		    
		  }
		  ContactShowList.setAdapter(cAdpt=new SendmsgContactadaptor(setdbdataInAdapterInd, gContext));
	}
	
	private void callWebServiceForSendmsg() 
	{
		
    	fetchDataToSelectedUser();
    	
        //Log.w("SEND_MSG","SEND_MSG::::::BEFORE CALL:::::(call webservice):"+SEL_INAPP_USERMOBILE.length());
    	
    	 //need to change condition below later
    	 if(SEL_INAPP_USERMOBILE.length()>=0)
    	 {
    		 
	    	 if(sendMessageinboxChatSend.isChecked())
	    	 {
	    	   checkedFlagforMuliple="1";
	    	 }
	    	 else
	    	 {
	    	   checkedFlagforMuliple= "0"; 
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
		
	         //Log.w("SEND_MSG","SEND_MSG:::::::::::(call webservice)");
	 
	         fetchCurrentTimeDate();
	         
	         dbObject.Open();
			  
			   Cursor c;
			   
			   c= dbObject.getSelRegUserDetails();
			   
			   int count=c.getCount();
			   
			  	
			   while(c.moveToNext())
			   {
				  			   
				   SEL_INAPP_USERID       = c.getString(0);
				   SEL_INAPP_USERLOGIN    = c.getString(1);
				   SEL_INAPP_USERMOBILE   = c.getString(2);
				   SEL_INAPP_USERPASSWORD = c.getString(3);
				   SEL_CNT_USERID         = c.getString(4);
				   SEL_CNT_NUMBER         = c.getString(5);
				   SEL_CNT_NAME           = c.getString(6);
				   
				   message=sendMessageinboxedit.getText().toString();
				   
				   if(selnumall.contains(SEL_CNT_NUMBER))
				   {
				   }
				   else
				   {
					   if(selnumall.length()>0)
					   {
					     selnumall =selnumall+","+SEL_CNT_NUMBER; 
					   }
					   else
					   {
					     selnumall = SEL_CNT_NUMBER;
					   }
				   }
				
				    checkDuplicate = checkInboxTime(SEL_CNT_NUMBER);
					
					if(checkDuplicate)
					{
					  dbObject.addInboxItem(SEL_INAPP_USERID, curTime, message, "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","TXT",checkedFlagforMuliple);
					}
					else
					{
					  dbObject.deleteInboxItemNumber(SEL_CNT_NUMBER);
					  dbObject.addInboxItem(SEL_INAPP_USERID, curTime, message, "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","TXT",checkedFlagforMuliple);
					}
										
						dbObject.InboxMessageAll(message, SEL_CNT_NUMBER, curDate+","+curTime, SEL_INAPP_USERID,SEL_INAPP_USERID, curDate, "", "S_M", "TXT","1","",checkedFlagforMuliple,dataStatus);
						dbObject.DeleteSelRegisterContactChat(SEL_CNT_NUMBER);		
										
			        }
					dbObject.close();
					
			       if(checkedFlagforMuliple.trim().equalsIgnoreCase("0"))
			       {	
			    	   
			       message = sendMessageinboxedit.getText().toString();	 
			       
			      /* String thj ;
					
			        BufferedReader reader = new BufferedReader(new StringReader(message));
					StringBuffer stringBuffer = new StringBuffer();
					
					try {
						while((thj = reader.readLine()) != null)
						{
							 if (thj.length() > 0) 
									 
							      stringBuffer.append(thj.trim());
								  stringBuffer.append("break");//"\\n"
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					String dataString = stringBuffer.toString();
					
					//Log.w("111111111","111111111 ::dataString(before replace)::"+dataString);*/
					
					//String dataStringAfter ;
					//= dataString.replaceAll("\n",  "/*");
					
					//dataStringAfter = dataString.replace("\n", "sss");
					
					////Log.w("111111111","111111111 ::dataString(after replace)::"+dataStringAfter);
			       contact=ProgressDialog.show(SendMessageInbox.this,null,"Please wait....");
				   fetchMobileandUserId();
				   								  			   
				   new webservice(null, webservice.MessageSendToRecipient.geturl("", InAppUserLogin, InAppPassword, InAppUserId, "SMSSMS", selnumall, message, InAppMobile, "",checkedFlagforMuliple,""), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() 
					 {	
						 
					 		    @Override
								public void onSuccess(Object Result, int id) 
								{
					 		    	contact.dismiss();
						 		   	InboxSentModel mod=(InboxSentModel) Result;
						 		  
						 		   	try
									{
									 //  Toast.makeText(gContext,""+mod.getMessageSendToRecipients().get(0).getResult(), Toast.LENGTH_LONG).show();
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
										    Intent rt = new Intent(SendMessageInbox.this,Emergency.class);
										    rt.putExtra("Emergency", EmergencyMessage);
										    startActivity(rt);

										  }
									} catch (Exception e) {
										e.printStackTrace();
									}
						 		   	
						 		   	try
						 		   	{
									  Inbox.sendInboxFlag.finish();
									}
						 		   	catch (Exception e) 
									{
							
									}
						 		   	
						 		   Intent send=new Intent(SendMessageInbox.this,Inbox.class);
						 		   send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								   startActivity(send);
								   finish();
								   
								
								}
								
								@Override
								public void onError(String Error, int id) 
								{
								 try 
								 {
										contact.dismiss();
								  // Toast.makeText(gContext,""+Error, Toast.LENGTH_LONG).show();
								 } 
									 catch (Exception e) 
								    {
									e.printStackTrace();
									}
	                                    dbObject.Open();
										dbObject.addOutboxServiceData(message, selnumall, "", "", SEL_INAPP_USERID, SEL_INAPP_USERID, "", "S_M", "TXT", "0", "",checkedFlagforMuliple,"SendMessageInbox",0,"",0);
	                                    dbObject.close();
								}
							});	
						
		  
			
	      }
	
		   if(checkedFlagforMuliple.trim().equalsIgnoreCase("1"))
		   {
				   message = sendMessageinboxedit.getText().toString();	 
				   fetchMobileandUserId();
				   new webservice(null, webservice.MessageSendToRecipient.geturl("", InAppUserLogin, InAppPassword, InAppUserId, "SMSSMS", selnumall, message, InAppMobile, "",checkedFlagforMuliple,""), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() 
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
						 		   	
						 		   	try {
										Inbox.sendInboxFlag.finish();
									} catch (Exception e) {
							
									}
						 		   	
						 		   Intent send=new Intent(SendMessageInbox.this,Inbox.class);
	                               send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								   startActivity(send);
								   finish();
								   
								
								}
								
								@Override
								public void onError(String Error, int id) 
								{
								 try 
								 {
								 Toast.makeText(gContext,""+Error, Toast.LENGTH_LONG).show();
								 } catch (Exception e) 
								 {
								 e.printStackTrace();
								 }
								 
								 dbObject.Open();
								 dbObject.addOutboxServiceData(message, selnumall, "", "", SEL_INAPP_USERID, SEL_INAPP_USERID, "", "S_M", "TXT", "0", "",checkedFlagforMuliple,"SendMessageInbox",0,"",0);
                                 dbObject.close();
	
								}
							});	
				}	
			  		
    	  }
    	 else
    	  {
    	Toast.makeText(gContext, "Please select one contact for send message.", Toast.LENGTH_SHORT).show();
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
			   
			   InAppMobile    = c.getString(1);
			   InAppUserId    = c.getString(3);
			   InAppPassword  = c.getString(5);
			   InAppUserLogin = c.getString(6);
			   
		   }
		  }	   
		   dbObject.close();
	}
	
	
	public void fetchDataToSelectedUser() 
	{
		dbObject.Open();
		  
		   Cursor c;
		   
		   c= dbObject.getSelRegUserDetails();
		   int count=c.getCount();
		   
		  if(count>=1)
		  {		
		   while(c.moveToNext()){
						 		   
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
	
		
	// Get Contact from database
				public String[] getIndividuleDetailsName()
				{
					dbObject.Open();	
					Cursor c;
					
					c= dbObject.getContactALL();
					
					counting = c.getCount();
					
					String icontact[]= new String[counting];
					
				    if(c.getCount() >= 1){
				    
				    	int i=0;
				    	while(c.moveToNext() && i<counting)
				    	{
				    	icontact[i] =c.getString(2);
				    	i++;
				    	}
				    }
				    
				    dbObject.close();
				   
					return icontact;
				}
		
		
		// Get Contact from database
			public String[] getIndividuleDetailsNumber()
			{
				dbObject.Open();	
				Cursor c;
				
				c= dbObject.getContactALL();
				
				counting = c.getCount();
				
				String icontact[]= new String[counting];
				
			    if(c.getCount() >= 1){
			    
			    	int i=0;
			    	while(c.moveToNext() && i<counting)
			    	{
			    	icontact[i] =c.getString(3);
			    	i++;
			    	}
			    }
			    
			    dbObject.close();
			   
				return icontact;
			}

		@Override
		public void onClick(View v)
		{
		switch(v.getId())
		{
		case R.id.sendmediaviainboxchat:

			AlertDialog alertDialog=new AlertDialog.Builder(this).create();
			
			alertDialog.setTitle("Choose Media ...............");
			
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Gallery", new DialogInterface.OnClickListener() {

			      public void onClick(DialogInterface dialog, int id)
			      {

			    	     Intent intent = new Intent();             
			    	     intent.setType("image/*");
			    	     intent.setAction(Intent.ACTION_GET_CONTENT);
			    	     startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);


			    } 
			     }); 

			    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Capture Image", new DialogInterface.OnClickListener() {

			      public void onClick(DialogInterface dialog, int id)
			      {
			    	
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
		case R.id.inboxchostemp:
		{
		Intent i=new Intent(SendMessageInbox.this,TemplateHolder.class);
		i.putExtra("taketemplate", "contact");
		startActivity(i);
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
		        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			sendmediafuncn(requestCode, data);
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
		public void sendmediafuncn(int requestCode, Intent data) {
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
			    
			    // check status of checkbox
			    if(sendMessageinboxChatSend.isChecked())
			    {
				  checkedFlagforMuliple="1";
				}
				 else
				 {
				  checkedFlagforMuliple= "0"; 
				 }
			    
			    fetchCurrentTimeDate();
			    
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
					   
					  if(selnumall.contains(SEL_CNT_NUMBER))
					   {
					   }
					   else
					   {
						   
						   if(selnumall.length()>0)
						   {
						   selnumall =selnumall+","+SEL_CNT_NUMBER; 
						   }
						   else
						   {
						   selnumall = SEL_CNT_NUMBER;
						   }
					   }
					  
					    checkDuplicate = checkInboxTime(SEL_CNT_NUMBER);
						
						if(checkDuplicate)
						{
				   		dbObject.addInboxItem(SEL_INAPP_USERID, curTime,"One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","IMAGE",checkedFlagforMuliple);
						}
						
						else
						{
							dbObject.deleteInboxItemNumber(SEL_CNT_NUMBER);
				   			dbObject.addInboxItem(SEL_INAPP_USERID, curTime,"One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","IMAGE",checkedFlagforMuliple);
						
						}
					   
					 dbObject.InboxMessageAll("One attachment", SEL_CNT_NUMBER, curDate+","+curTime, SEL_INAPP_USERID,SEL_INAPP_USERID, curDate, selectedImagePath, "S_M", "IMAGE","1","",checkedFlagforMuliple,dataStatus);
			 	     dbObject.DeleteSelRegisterContactChat(SEL_CNT_NUMBER);			
			   			            	
				   }
				   } 
						   		  
			        dbObject.close();

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
			    
			 	  Intent send=new Intent(SendMessageInbox.this,Inbox.class);
			      send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				  startActivity(send);
				  finish();

			}
			
			//for image capture from cemera
			if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
			{
				
				  String[] projection = { MediaStore.Images.Media.DATA };
				  fetchMobileandUserId();
			      Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection, null, null, null);
			      
			      int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			      
			      cursor.moveToLast();
			      selectedImagePath = cursor.getString(column_index_data);
			      
			      // add path to be show in listview
			      path=selectedImagePath;

			       if(sendMessageinboxChatSend.isChecked()){
						 checkedFlagforMuliple="1";
				   }
				   else{
						 checkedFlagforMuliple= "0"; 
				   }
			      
			       fetchCurrentTimeDate();
			       
			       int dataStatus = 0;
			 	  
			   	     if(haveDatamessage()){
  
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
					   
					   if(selnumall.contains(SEL_CNT_NUMBER))
					   {
					   }
					   else
					   {							   
						   if(selnumall.length()>0)
						   {
						   selnumall =selnumall+","+SEL_CNT_NUMBER; 
						   }
						   else
						   {
						   selnumall = SEL_CNT_NUMBER;
						   }
					   }
					   
					   checkDuplicate = checkInboxTime(SEL_CNT_NUMBER);
						
						if(checkDuplicate)
						{
							   dbObject.addInboxItem(SEL_INAPP_USERID, curTime,"One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","IMAGE",checkedFlagforMuliple);
						}
						
						else
						{
							   dbObject.deleteInboxItemNumber(SEL_CNT_NUMBER);
							   dbObject.addInboxItem(SEL_INAPP_USERID, curTime,"One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","IMAGE",checkedFlagforMuliple);
						
						}
					   
			 dbObject.InboxMessageAll("One attachment", SEL_CNT_NUMBER, curDate+","+curTime, SEL_INAPP_USERID,SEL_INAPP_USERID, curDate, selectedImagePath, "S_M", "IMAGE","1","",checkedFlagforMuliple,dataStatus);
			 dbObject.DeleteSelRegisterContactChat(SEL_CNT_NUMBER);			
			  }
			   
			  } 
  	
			   dbObject.close();
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
			   
				  Intent send=new Intent(SendMessageInbox.this,Inbox.class);
			      send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				  startActivity(send);
				  finish();
				        		                   	                   
			} 
			
			//video capture 
			if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
				
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
			    
			    if(sendMessageinboxChatSend.isChecked())
			     {
					 checkedFlagforMuliple="1";
				 }
				 else
				 {
					 checkedFlagforMuliple= "0"; 
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
								   			   
					   SEL_INAPP_USERID       = c.getString(0);
					   SEL_INAPP_USERLOGIN    = c.getString(1);
					   SEL_INAPP_USERMOBILE   = c.getString(2);
					   SEL_INAPP_USERPASSWORD = c.getString(3);
					   SEL_CNT_USERID         = c.getString(4);
					   SEL_CNT_NUMBER         = c.getString(5);
					   
					  if(selnumall.contains(SEL_CNT_NUMBER))
					   {
					   }
					   else
					   {
						   
						   if(selnumall.length()>0)
						   {
						   selnumall =selnumall+","+SEL_CNT_NUMBER; 
						   }
						   else
						   {
						   selnumall = SEL_CNT_NUMBER;
						   }
					   }
					  
					  checkDuplicate = checkInboxTime(SEL_CNT_NUMBER);
						
						if(checkDuplicate)
						{
				   	    dbObject.addInboxItem(InAppUserId,curTime,"One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","VIDEO",checkedFlagforMuliple);
						}
						
						else
						{
					   dbObject.deleteInboxItemNumber(SEL_CNT_NUMBER);
				   	   dbObject.addInboxItem(InAppUserId,curTime,"One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","VIDEO",checkedFlagforMuliple);
						
						}
					   
			   	dbObject.InboxMessageAll("One attachment", SEL_CNT_NUMBER, curDate+","+curTime, SEL_INAPP_USERID,SEL_INAPP_USERID, curDate, selectedVideoPath, "S_M", "VIDEO","1","",checkedFlagforMuliple,dataStatus);
			   	dbObject.DeleteSelRegisterContactChat(SEL_CNT_NUMBER); 
			   	
				   }
				  } 
			 
			    dbObject.close();
			    cursor.close();
			    
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
			    
			 	  Intent send=new Intent(SendMessageInbox.this,Inbox.class);
			      send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				   startActivity(send);
				   finish();
				
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

			    try
			    {
			    if(sendMessageinboxChatSend.isChecked())
			     {
					 checkedFlagforMuliple="1";
				 }
				 else
				 {
					 checkedFlagforMuliple= "0"; 
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
								   			   
					   SEL_INAPP_USERID       = c.getString(0);
					   SEL_INAPP_USERLOGIN    = c.getString(1);
					   SEL_INAPP_USERMOBILE   = c.getString(2);
					   SEL_INAPP_USERPASSWORD = c.getString(3);
					   SEL_CNT_USERID         = c.getString(4);
					   SEL_CNT_NUMBER         = c.getString(5);
					   
					  if(selnumall.contains(SEL_CNT_NUMBER))
					   {
					   }
					   else
					   {
						   
						   if(selnumall.length()>0)
						   {
						   selnumall =selnumall+","+SEL_CNT_NUMBER; 
						   }
						   else
						   {
						   selnumall = SEL_CNT_NUMBER;
						   }
					   }
					  
					  checkDuplicate = checkInboxTime(SEL_CNT_NUMBER);
						
						if(checkDuplicate)
						{
				   	    dbObject.addInboxItem(InAppUserId,curTime,"One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","AUDIO",checkedFlagforMuliple);
						}
						
						else
						{
					   dbObject.deleteInboxItemNumber(SEL_CNT_NUMBER);
				   	   dbObject.addInboxItem(InAppUserId,curTime,"One attachment", "", SEL_CNT_NUMBER,SEL_CNT_NUMBER, "","AUDIO",checkedFlagforMuliple);
						
						}
					   
			   	dbObject.InboxMessageAll("One attachment", SEL_CNT_NUMBER, curDate+","+curTime, SEL_INAPP_USERID,SEL_INAPP_USERID, curDate, selectedaudiopath, "S_M", "AUDIO","1","",checkedFlagforMuliple,dataStatus);
			   	dbObject.DeleteSelRegisterContactChat(SEL_CNT_NUMBER); 
			   	
				   }
				  } 
			 
			    dbObject.close();
			    cursor.close();
												
			   														
			    new webservice(null, webservice.GetFTPHostDetail.geturl("audio"), webservice.TYPE_GET, webservice.TYPE_FTP_UPLD, new ServiceHitListener() {
				
				
				public void onSuccess(Object Result, int id)
				{
					

					 try
					    {
					    	imagetoupload2=path.substring(path.lastIndexOf("/"));
						} 
					 
					    catch (Exception e) {
							
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
			    
			  
				  
} catch (Exception e) {
			e.printStackTrace();
}
			    
			 	try
			 	{
				Inbox.sendInboxFlag.finish();
				} 
			 	catch (Exception e) 
			 	{

				}
			    
			 	  Intent send=new Intent(SendMessageInbox.this,Inbox.class);
			      send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				   startActivity(send);
				   finish();
				
  /**************stop recording and send to server*****************/	
			}
		}
		
		private void fetchCurrentTimeDate()
		{
			
		     DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		     String date   = df.format(Calendar.getInstance().getTime());
		   
		     curDate = date.substring(0,date.indexOf("_"));
		     curTime = date.substring(date.indexOf("_")+1);
				  
		}
		
		public boolean haveDatamessage()
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
		
		public class MyTransferListener implements FTPDataTransferListener
		{

			@Override
			public void aborted() {
								
			}

			@Override
			public void completed() {

				 if(sendMessageinboxChatSend.isChecked())
		    	 {
		    	  checkedFlagforMuliple="1";
		    	 }
		    	else
		    	{
		    	 checkedFlagforMuliple= "0"; 
		    	}
				 
				fetchMobileandUserId();
					
				new webservice(null, webservice.AudioVideoPictureMessage.geturl("", "", InAppUserLogin, InAppPassword, InAppUserId, "SMSMSMS", selnumall, "One Attachment", InAppMobile,"",checkedFlagforMuliple, sendurlpath, filetype, filename), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener() {
					
					@Override
					public void onSuccess(Object Result, int id) 
					{
						  try {
								Inbox.sendInboxFlag.finish();
							} catch (Exception e) {
					
							}
				            
				         	  Intent send=new Intent(SendMessageInbox.this,Inbox.class);
		                      send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							  startActivity(send);
							  finish();				 	    	
						
					}
					
					@Override
					public void onError(String Error, int id) 
					{
						dbObject.Open();
						dbObject.addOutboxServiceData("One attachment", selnumall, curTime, curDate, InAppUserId, "", selectedImagePath, "S_M", filetype, "0","",checkedFlagforMuliple,"SendMessageInbox",0,"",0);
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
