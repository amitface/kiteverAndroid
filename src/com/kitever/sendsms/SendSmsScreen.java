package com.kitever.sendsms;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sms19.listview.adapter.ContactAdaptor;
import sms19.listview.adapter.customAdaptorContactDataBASE;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.CustomTemplate;
import sms19.listview.newproject.EditContacts;
import sms19.listview.newproject.EditProfile;
import sms19.listview.newproject.Emergency;
import sms19.listview.newproject.GroupNewAdd;
import sms19.listview.newproject.Home;
import sms19.listview.newproject.LoginPage;
import sms19.listview.newproject.NewContact;
import com.kitever.android.R;
import sms19.listview.newproject.ScheduleList;
import sms19.listview.newproject.TemplateHolder;
import sms19.listview.newproject.Transaction;
import sms19.listview.newproject.AddContactManually;
import sms19.listview.newproject.model.ScheduleSmsSend;
import sms19.listview.newproject.model.SendSmsModel;
import sms19.listview.newproject.model.getTopBalance;
import sms19.listview.validation.Validation;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SendSmsScreen extends ActionBarActivity implements OnClickListener, ServiceHitListener
{
	
	EditText choosenTemplatename,Selectedrecepient,textGroupName,currentTime,currentDate;
	TextView numofcont,Sendnowbtn,SendLaterbtn;
	TextView addContacttogroup,addgpcontmsg;
	TextView addmanually;
	ImageView editcontacts,imageViewcustomtem;
	TextView chooseTemplatebtn;
	ImageView imageView2;
	TextView Reset;
	public static String choosedTemplate = " ";
	public static String MobileRecipient="",content = " ", GroupName ="", SelectedDate="",SelectedTime="";
	
	String BalSms ="";
	String curDateTime = "";
	
	// Variable for storing current date and time
	private int mYear, mMonth, mDay, mHour, mMinute;
	
	String DatePickerSEND = "";
	String TimePickerSEND = "";
	
	boolean flgcheck = false;
	boolean flagToToast= true;
	boolean flagToToastTime = true;
	
	ActionBar bar;
	String UserLogin="";
	
	String anu,Template,cont="";
	String Mobile= "";
	String UserId= "";
	String Password = "";
	String tem;
	TextView wordscount/*TOTALCOUNT*/;
	String TemplatesCustom="",TemplateId = "";
	String RecpientNumber="",MobiRecpientNumber="", RecipentName="",MobiRecpientName="" ;
	
	int numberofcontacts;
	
	public static String temid,customtemplate;
	
	public boolean clickedittext = false;
	
	DataBaseDetails dbObject= new DataBaseDetails(this);
	String Templates = null;
	int countTotalcontact = 0;
	int grp=0;
	int countTotalAll = 0;
	Handler handler;
	TextView totalcount;
    Runnable checker;
	public static Activity sendSmsFlag;
	public static ActionBarActivity sendsd;
	
	Context fedt;
	
	
	
@Override
protected void onCreate(Bundle savedInstanceState)
{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.newsendsmsxml);
	Sendnowbtn           =(TextView)findViewById(R.id.SENDNOW);
	SendLaterbtn         =(TextView)findViewById(R.id.SENDLATER);
	choosenTemplatename  =(EditText)findViewById(R.id.ChoosenTemplate);
	Selectedrecepient    =(EditText)findViewById(R.id.EtselectedTemplate);//EtselectedTemplate
	chooseTemplatebtn    =(TextView)findViewById(R.id.chooseTemplatebtn);
	addContacttogroup    =(TextView)findViewById(R.id.addContactsGroupsBtn);//addContactsGroupsBtn
	//schedulelater        =(ImageView)findViewById(R.id.sendMessageLaterOn);
	//sendnow              =(ImageView)findViewById(R.id.SendNowbtn);
	editcontacts         =(ImageView)findViewById(R.id.editcon);//EtselectedTemplate
	numofcont            =(TextView)findViewById(R.id.total);
	//balance            =(TextView)findViewById(R.id.textView4balance);
	addgpcontmsg         =(TextView)findViewById(R.id.addgpcontmsg);
	addmanually          =(TextView) findViewById(R.id.imageVieweditmanu);
	totalcount           =(TextView)findViewById(R.id.totalcount);
	Reset                =(TextView) findViewById(R.id.resetlalldetail);
	textGroupName        =(EditText) findViewById(R.id.textGroupName);
	wordscount           =(TextView)findViewById(R.id.wordscount);
	imageViewcustomtem   =(ImageView) findViewById(R.id.imageViewcustomtem);
	//TOTALCOUNT           =(TextView) findViewById(R.id.textView1);
	// mapping id with current date and time
	currentDate         =(EditText) findViewById(R.id.Editdate);
	currentTime         =(EditText) findViewById(R.id.editFetchTime);
	
   // ActionBar bar = getSupportActionBar();
	/*bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
	bar.setTitle(Html.fromHtml("<font color='#ffffff'>"+BalSms+"</font>"));*/
	
	chooseTemplatebtn.setOnClickListener(this);
	choosenTemplatename.setOnClickListener(this);
	editcontacts.setOnClickListener(this);
	Sendnowbtn.setOnClickListener(this);
	SendLaterbtn.setOnClickListener(this);
	addContacttogroup.setOnClickListener(this);
    addgpcontmsg.setOnClickListener(this);
	addmanually.setOnClickListener(this);
	Reset.setOnClickListener(this);
	imageViewcustomtem.setOnClickListener(this);
	
	currentDate.setOnClickListener(this);
	currentTime.setOnClickListener(this);
	
	/***************************INTERNET********************************/
	webservice._context= this;
	sendSmsFlag = this;
	sendsd = this;
	fedt  = this;
	/***************************INTERNET********************************/
	
	bar = getSupportActionBar();
	bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006966")));
	bar.setTitle(Html.fromHtml("<font color='#ffffff'>Send Sms</font>"));
	//bar.setHomeAsUpIndicator (R.drawable.arrow_new) ;
	
	
	/***********************************************************/
	clickedittext = false;
	
	/***********************************************************/
	
		
	/*******************group set ******************************/
	//group is selected and set in to group name box
	dbObject.Open();
	Cursor c ;
	c = dbObject.getSelectedGroupName();
	String groupName="";
    int gpcount=0;
	while(c.moveToNext())
	{
    groupName += c.getString(0)+","; 
	gpcount += c.getInt(2);
    }
	dbObject.close();
	
	try 
	{
	groupName =  groupName.substring(0,groupName.length()-1);
	textGroupName.setText(""+groupName);
	numofcont.setVisibility(View.VISIBLE);
	numofcont.setText(""+gpcount); 
	}
	catch (Exception e2)
	{
	}
	/*******************group set ******************************/
	
	
	
	//function that check total number of recipients in recipients box and called continiously 
	check();
	startHandler();
	//**********************done******************************

	//check total words in template box
	choosenTemplatename.addTextChangedListener(new TextWatcher() 
	{

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) 
        {
        String templatelength=choosenTemplatename.getText().toString();
        
       
        if(templatelength.length()>0)
     	{
     	//TOTALCOUNT.setVisibility(View.VISIBLE);
     	wordscount.setVisibility(View.VISIBLE);
     	}
       
         int meesagelength=templatelength.length();
         wordscount.setText(""+meesagelength);
         
         if(templatelength.length()>138)
         {
          Toast.makeText(getApplicationContext(),"You can not enter more than 138 characters", Toast.LENGTH_SHORT).show();
          choosenTemplatename.setFocusable(false);
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
	//********************************finish count word of template***********************/
	
	
	
	
	/********************************db leak handled **********************************/

	
	//if user come from send sms click clen all previous value from all text boxes if set due to db leak issue
	Intent i=getIntent();
	
	try 
	{
		String cleanDb = i.getStringExtra("dataclear");
		
		if(cleanDb.equalsIgnoreCase("CLEAN"))
		{
			
			Selectedrecepient.setText("");
			numofcont.setText("");
			choosenTemplatename.setText("");
			textGroupName.setText("");
			TemplateId= "";
			
			dbObject.Open();
			dbObject.deleteselectedReceipent();
			dbObject.DeleteSELETEDTemplateData();
			dbObject.deleteCustomTemplate();
			dbObject.SelectedGroupName();
			dbObject.close();
			
		}
	} catch (Exception e2) {
		
		
	}
	/********************************finish db leak handled **********************************/
	
	/*************set message in template box that has been brought from custom template page*******/
	
	try
	{
		try 
		{
		//set message in template box that has been brought from custom template page
		customtemplate=i.getStringExtra("messagecustom");
	
	     String dfg="dfvdv";
		dfg=getcustom();
        choosenTemplatename.setText(dfg);
		
		} 
		catch (Exception e)
		{
		}
       temid=i.getStringExtra("templateID");
       
      
    }
	catch(Exception e)
	{
    }
	/*************finish set message in template box that has been brought from custom template page*******/

	try
	{
	/*************************************Template set from database***********************/
	// Template set from database	
		if(IsTemplateExist())
		{
		fetchTemplate();
		
		if(CustomTemplate.customRemplate)
		{
		choosenTemplatename.setText(Template);
		}
		
		}
		else
		{
		
		}
	}
	catch(Exception ed){}
	/*************************************finish Template set from database***********************/

	
	//set balance available call service
	try
	{
	totalavailablebalance();
	}
	catch (Exception e1) 
	{}
	/****************************************set data in recipient box and total number of recipients count******************************/
	//On select all check box click in contact page fetch entries from database and calculate total recipients selected
	try
	{
		
		if(customAdaptorContactDataBASE.statusCheckAll || ContactAdaptor.statusCheckAllSer)
		{
			
			customAdaptorContactDataBASE.statusCheckAll= false;
			ContactAdaptor.statusCheckAllSer = false;
	
				RecpientNumber = getIndividuleDetails();
				RecipentName  = getIndividuleDetailsName();
				
				
				
				try
				{
					//calculate number of recipients
				    fetchREspient();
				    anu=Integer.toString(countTotalAll);
				    if(anu.length()>0)
				    {
				    numofcont.setVisibility(View.VISIBLE);
				    numofcont.setText(anu);
				    }
				   
				}
				catch(Exception e)
				{
				}
				
				MobiRecpientNumber = removeLastChar(RecpientNumber);
				MobiRecpientName   = removeLastChar(RecipentName);
				
			
				String[] mob=MobiRecpientNumber.split(",");
				String []num=MobiRecpientName.split(",");
				
                try{
					
						
					fetchMobileandUserId();
					dbObject.Open();
					for(int ii=0;ii<mob.length;ii++)
					{
					String a=mob[ii];
					String b=num[ii];
					dbObject.addReceipent(UserId,  a, b);
					}
					dbObject.close();
													
				}
				catch(Exception e){}
			
                RecpientNumber = fetchREspient();
            	MobiRecpientNumber =removeLastChar(RecpientNumber);
           		Selectedrecepient.setText(MobiRecpientNumber);
			
		}
		
		else
		{
			
		//if user select contact one by one from contact page fetch from db and set in recipient box, calculate total recipients selected
			
			if(IsResContactExist())
			{
			
				RecpientNumber = fetchREspient();
				//Log.w("TAG","VALUE"+IsResContactExist());

				
				try
				{
					//calculate total number of recipients
				    fetchREspient();
				    anu=""+countTotalcontact;
				    if(anu.length()>0)
				    {
				    numofcont.setVisibility(View.VISIBLE);
				    numofcont.setText(anu);
				    }
				   
				}
				catch(Exception e)
				{
				  e.printStackTrace();	
				}
				
				MobiRecpientNumber = removeLastChar(RecpientNumber);
		     	Selectedrecepient.setText(MobiRecpientNumber);
				
				
		     }
		}
	
   }
	catch(Exception e){}
}
/****************************************finish set data in recipient box and total number of recipients count******************************/

//total balance available in user account fetch from server
private void totalavailablebalance() 
{
	
	fetchMobileandUserId();
	new webservice(null, webservice.getCurrentBalance.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_GET_TOP_BALANCE, new ServiceHitListener() {
		
		@Override
		public void onSuccess(Object Result, int id) 
		{
			
			if(id == webservice.TYPE_GET_TOP_BALANCE)
			{
				// Declare model of that service
				getTopBalance model =  (getTopBalance) Result;
			
	
				try
				{
				BalSms = model.getUserTopTransactionDetails().get(0).getBalance().trim();
				totalcount.setText(BalSms);
				} 
				catch (Exception e)
				{
				e.printStackTrace();
				}
				 try {
						String EmergencyMessage = model.getUserTopTransactionDetails().get(0).getEmergencyMessage();
						  try
						  {
							
								
						  Emergency.desAct.finish();
						  } 
						  catch (Exception e)
						  {
						  }
						   
						   if(!(EmergencyMessage.equalsIgnoreCase("NO")))
						   {
						    Intent rt = new Intent(SendSmsScreen.this,Emergency.class);
						    rt.putExtra("Emergency", EmergencyMessage);
						    startActivity(rt);

						  }
					} catch (Exception e) {
						
					} 
			}	
		}
		
		@Override
		public void onError(String Error, int id)
		{
						
		}
	});
}
/**********************************total balance available in user account fetch from server*****************************/
 

//Remove last String
	private static String removeLastChar(String str) 
	{
     return str.substring(0,str.length()-1);
    }

@Override
public void onClick(View v) 
{

	switch(v.getId())
	{
	//Schedule message on click
	case R.id.SENDLATER:
	 {	
		
		   
		    MobileRecipient = Selectedrecepient.getText().toString();
			choosedTemplate = choosenTemplatename.getText().toString();
			GroupName       = textGroupName.getText().toString();
			 SelectedDate    =currentDate.getText().toString();
			 SelectedTime    =currentTime.getText().toString();
			if(MobileRecipient.length()<=0 && GroupName.length()<=0)	
			{
		    Toast.makeText(SendSmsScreen.this, "Please select atleast one Recipient", Toast.LENGTH_SHORT).show();
			}
			else if(choosedTemplate.length()<=0)
			{
		   Toast.makeText(SendSmsScreen.this, "Please select template", Toast.LENGTH_SHORT).show();
						
			}
			else if(SelectedDate.length()<=0)
			{
		   Toast.makeText(SendSmsScreen.this, "Please select Date", Toast.LENGTH_SHORT).show();
						
			}
			else if(SelectedTime.length()<=0)
			{
		     Toast.makeText(SendSmsScreen.this, "Please select Time", Toast.LENGTH_SHORT).show();
						
			}
			else if(choosedTemplate.length()>0 && (MobileRecipient.length()>0 || GroupName.length()>0))
	        	{
	    	  
	    	    //Log.w("Tag","Template ID:"+temid);
	    	
	    		MobileRecipient = Selectedrecepient.getText().toString().trim();
		        GroupName       = textGroupName.getText().toString().trim();
	    		
		        String messageEdit = choosenTemplatename.getText().toString();
				
				if(clickedittext)
				{
					dbObject.Open();
					dbObject.deleteCustomTemplate();
					dbObject.customtemplate("1", messageEdit);
					dbObject.close();
					clickedittext = false;
			    }
		        
				callschedualMessageservice();
	       	
     	    }
	       else
	       {
       	   Toast.makeText(getApplicationContext(), "PLease Select At least One Template and Mobile Number", Toast.LENGTH_SHORT).show();
       	   }
	       
	 }     
		break;
		
		//Add contacts from contact page into recipient box
	case R.id.addContactsGroupsBtn:
	{
		
		NewContact.checkAll= false;
		Intent iadd=new Intent(SendSmsScreen.this,NewContact.class);
		startActivity(iadd);
	}
		break;
		//clear all feilds on reset button
	case R.id.resetlalldetail:
	{
		Selectedrecepient.setText("");
		numofcont.setText("");
		choosenTemplatename.setText("");
		textGroupName.setText("");
		currentTime.setText("");	
		currentDate.setText("");
		TemplateId= "";
		
		dbObject.Open();
		dbObject.deleteselectedReceipent();
		dbObject.DeleteSELETEDTemplateData();
		dbObject.deleteCustomTemplate();
		dbObject.SelectedGroupName();
		dbObject.close();	
	}
		break;
		//choose predefined template button
	case R.id.chooseTemplatebtn:
	{
		clickedittext = false;
		
		choosenTemplatename.setFocusable(false);
		
		Intent id=new Intent(SendSmsScreen.this,TemplateHolder.class);
		id.putExtra("taketemplate", "send");
		startActivity(id);
	}
		break;
	//Edit template	on click button
	case R.id.imageViewcustomtem:
	{
		if(Selectedrecepient.getText().length()<=0 && textGroupName.getText().toString().length()<=0)
        {
    	Toast.makeText(SendSmsScreen.this, "Please select Recipient First", Toast.LENGTH_SHORT).show();
    	choosenTemplatename.setFocusable(false);
        
    	}	
		else
		{
		TemplateId= "";
		choosenTemplatename.setFocusableInTouchMode(true);
		clickedittext = true;
		}
		
	}
		break;
	//Add group into group box grom group page
	case R.id.addgpcontmsg:
	{
		Intent i2=new Intent(SendSmsScreen.this,GroupNewAdd.class);
		startActivity(i2);
	}
		break;
	//Edit contact icon navigate to new screen froim where we can delete selected recipients	
	case R.id.editcon:
	{
		Intent i3=new Intent(SendSmsScreen.this,EditContacts.class);
		startActivity(i3);
	}
		break;
	//select date for scheduling message	
	case R.id.Editdate:
	{
	/* if(choosedTemplate.length()>0 && (MobileRecipient.length()>0 || GroupName.length()>0) && choosenTemplatename.length()>0)
	    	{

*/
		// Process to get Current Date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// Launch Date Picker Dialog
		DatePickerDialog dpd = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener()
		{

					@Override
					public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
						// Display Selected date in text box  Log
						/*currentDate.setText(dayOfMonth + "-"
								+ (monthOfYear + 1) + "-" + year);*///YYYY-MM-DD
						
						if((dayOfMonth>=mDay))
						{
							
							if((monthOfYear+1)>=(mMonth+1))
							{
								
								if((year>=mYear))
								{
									//success
									//changes in code
									//Log.w("TAG","..............................MONTH OF YEAR:+1"+(monthOfYear +1));
									
									if( ((monthOfYear +1) != 10) && ((monthOfYear +1) != 11) && ((monthOfYear +1) != 12))
									{
											
										if(((dayOfMonth) == 1) || ((dayOfMonth) == 2) || ((dayOfMonth) == 3)|| ((dayOfMonth) == 4) || ((dayOfMonth) == 5)|| ((dayOfMonth) == 6) || ((dayOfMonth) == 7)|| ((dayOfMonth) == 8) || ((dayOfMonth) == 9))
					                    {
											
											   currentDate.setText(year + "-"+"0"+(monthOfYear + 1) + "-"+"0"+dayOfMonth);
						                       DatePickerSEND = year + "-"  +"0" + (monthOfYear + 1) + "-"+"0"+dayOfMonth;	
					                    }
					                    else
					                    {
											
					                    	currentDate.setText(year + "-"+"0"+(monthOfYear + 1) + "-" + dayOfMonth);
											DatePickerSEND = year + "-"  +"0" + (monthOfYear + 1) + "-" + dayOfMonth;	
					                    }
					                  
									}
									else
									{
										if(((dayOfMonth) == 1) || ((dayOfMonth) == 2) || ((dayOfMonth) == 3)|| ((dayOfMonth) == 4) || ((dayOfMonth) == 5)|| ((dayOfMonth) == 6) || ((dayOfMonth) == 7)|| ((dayOfMonth) == 8) || ((dayOfMonth) == 9))
					                    {
											currentDate.setText(year + "-"+(monthOfYear + 1) + "-"+"0" + dayOfMonth);
											DatePickerSEND = year + "-" + (monthOfYear + 1) + "-"+"0" + dayOfMonth;
					                    }
										else{
											currentDate.setText(year + "-"+(monthOfYear + 1) + "-" + dayOfMonth);
											DatePickerSEND = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
										}
										
									}
									
									// finish....
									
									
								
								}
								else
								{
									// please enter valid date

									//Log.w("ELSE","dayOfMonth"+dayOfMonth+"monthOfYear"+monthOfYear+"year"+year+"mDate"+mDay+"mMonth"+mMonth+"mYear"+mYear);

									if(flagToToast)
									{
									Toast.makeText(fedt, "Schedule date should be greater than current date", Toast.LENGTH_SHORT).show();
									flagToToast= false;
									}
									else{
										flagToToast= true;
									}
								
								}
							}
							else
							{
								if((year>mYear))
								{
									//success
									//changes in code
									//Log.w("TAG","..............................MONTH OF YEAR:+1"+(monthOfYear +1));
									
									if( ((monthOfYear +1) != 10) && ((monthOfYear +1) != 11) && ((monthOfYear +1) != 12))
									{
											
										if(((dayOfMonth) == 1) || ((dayOfMonth) == 2) || ((dayOfMonth) == 3)|| ((dayOfMonth) == 4) || ((dayOfMonth) == 5)|| ((dayOfMonth) == 6) || ((dayOfMonth) == 7)|| ((dayOfMonth) == 8) || ((dayOfMonth) == 9))
					                    {
											
											   currentDate.setText(year + "-"+"0"+(monthOfYear + 1) + "-"+"0"+dayOfMonth);
						                       DatePickerSEND = year + "-"  +"0" + (monthOfYear + 1) + "-"+"0"+dayOfMonth;	
					                    }
					                    else
					                    {
											
					                    	currentDate.setText(year + "-"+"0"+(monthOfYear + 1) + "-" + dayOfMonth);
											DatePickerSEND = year + "-"  +"0" + (monthOfYear + 1) + "-" + dayOfMonth;	
					                    }
					                  
									}
									else
									{
										if(((dayOfMonth) == 1) || ((dayOfMonth) == 2) || ((dayOfMonth) == 3)|| ((dayOfMonth) == 4) || ((dayOfMonth) == 5)|| ((dayOfMonth) == 6) || ((dayOfMonth) == 7)|| ((dayOfMonth) == 8) || ((dayOfMonth) == 9))
					                    {
											currentDate.setText(year + "-"+(monthOfYear + 1) + "-"+"0" + dayOfMonth);
											DatePickerSEND = year + "-" + (monthOfYear + 1) + "-"+"0" + dayOfMonth;
					                    }
										else{
											currentDate.setText(year + "-"+(monthOfYear + 1) + "-" + dayOfMonth);
											DatePickerSEND = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
										}
										
									}
									
									// finish....
									
									
								
								}
								else{
									//please enter valid date

									//Log.w("ELSE","dayOfMonth"+dayOfMonth+"monthOfYear"+monthOfYear+"year"+year+"mDate"+mDay+"mMonth"+mMonth+"mYear"+mYear);

									if(flagToToast)
									{
									Toast.makeText(fedt, "Please enter valid date", Toast.LENGTH_SHORT).show();
									flagToToast= false;
									}
									else{
										flagToToast= true;
									}
								
									}
							}
							
							}
						else{
							if((monthOfYear+1)>(mMonth+1))
							{
                                if((year>=mYear))
                                {
									//success
									//changes in code
									//Log.w("TAG","..............................MONTH OF YEAR:+1"+(monthOfYear +1));
									
									if( ((monthOfYear +1) != 10) && ((monthOfYear +1) != 11) && ((monthOfYear +1) != 12))
									{
											
										if(((dayOfMonth) == 1) || ((dayOfMonth) == 2) || ((dayOfMonth) == 3)|| ((dayOfMonth) == 4) || ((dayOfMonth) == 5)|| ((dayOfMonth) == 6) || ((dayOfMonth) == 7)|| ((dayOfMonth) == 8) || ((dayOfMonth) == 9))
					                    {
											
											   currentDate.setText(year + "-"+"0"+(monthOfYear + 1) + "-"+"0"+dayOfMonth);
						                       DatePickerSEND = year + "-"  +"0" + (monthOfYear + 1) + "-"+"0"+dayOfMonth;	
					                    }
					                    else
					                    {
											
					                    	currentDate.setText(year + "-"+"0"+(monthOfYear + 1) + "-" + dayOfMonth);
											DatePickerSEND = year + "-"  +"0" + (monthOfYear + 1) + "-" + dayOfMonth;	
					                    }
					                  
									}
									else
									{
										if(((dayOfMonth) == 1) || ((dayOfMonth) == 2) || ((dayOfMonth) == 3)|| ((dayOfMonth) == 4) || ((dayOfMonth) == 5)|| ((dayOfMonth) == 6) || ((dayOfMonth) == 7)|| ((dayOfMonth) == 8) || ((dayOfMonth) == 9))
					                    {
											currentDate.setText(year + "-"+(monthOfYear + 1) + "-"+"0" + dayOfMonth);
											DatePickerSEND = year + "-" + (monthOfYear + 1) + "-"+"0" + dayOfMonth;
					                    }
										else{
											currentDate.setText(year + "-"+(monthOfYear + 1) + "-" + dayOfMonth);
											DatePickerSEND = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
										}
										
									}
									
									// finish....
									
									
								
								}
                                else{
									// please enter valid date

									//Log.w("ELSE","dayOfMonth"+dayOfMonth+"monthOfYear"+monthOfYear+"year"+year+"mDate"+mDay+"mMonth"+mMonth+"mYear"+mYear);

									if(flagToToast)
									{
									Toast.makeText(fedt, "Please enter valid date", Toast.LENGTH_SHORT).show();
									flagToToast= false;
									}
									else{
										flagToToast= true;
									}
								
								}
								}
							else{
								if((year>mYear)){
									//success
									//changes in code
									//Log.w("TAG","..............................MONTH OF YEAR:+1"+(monthOfYear +1));
									
									if( ((monthOfYear +1) != 10) && ((monthOfYear +1) != 11) && ((monthOfYear +1) != 12))
									{
											
										if(((dayOfMonth) == 1) || ((dayOfMonth) == 2) || ((dayOfMonth) == 3)|| ((dayOfMonth) == 4) || ((dayOfMonth) == 5)|| ((dayOfMonth) == 6) || ((dayOfMonth) == 7)|| ((dayOfMonth) == 8) || ((dayOfMonth) == 9))
					                    {
											
											   currentDate.setText(year + "-"+"0"+(monthOfYear + 1) + "-"+"0"+dayOfMonth);
						                       DatePickerSEND = year + "-"  +"0" + (monthOfYear + 1) + "-"+"0"+dayOfMonth;	
					                    }
					                    else
					                    {
											
					                    	currentDate.setText(year + "-"+"0"+(monthOfYear + 1) + "-" + dayOfMonth);
											DatePickerSEND = year + "-"  +"0" + (monthOfYear + 1) + "-" + dayOfMonth;	
					                    }
					                  
									}
									else
									{
										if(((dayOfMonth) == 1) || ((dayOfMonth) == 2) || ((dayOfMonth) == 3)|| ((dayOfMonth) == 4) || ((dayOfMonth) == 5)|| ((dayOfMonth) == 6) || ((dayOfMonth) == 7)|| ((dayOfMonth) == 8) || ((dayOfMonth) == 9))
					                    {
											currentDate.setText(year + "-"+(monthOfYear + 1) + "-"+"0" + dayOfMonth);
											DatePickerSEND = year + "-" + (monthOfYear + 1) + "-"+"0" + dayOfMonth;
					                    }
										else{
											currentDate.setText(year + "-"+(monthOfYear + 1) + "-" + dayOfMonth);
											DatePickerSEND = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
										}
										
									}
									
									// finish....
									
									
								
								}
								else{
									// please enter valid date

									//Log.w("ELSE","dayOfMonth"+dayOfMonth+"monthOfYear"+monthOfYear+"year"+year+"mDate"+mDay+"mMonth"+mMonth+"mYear"+mYear);

									if(flagToToast)
									{
									Toast.makeText(fedt, "Please enter valid date", Toast.LENGTH_SHORT).show();
									flagToToast= false;
									}
									else{
										flagToToast= true;
									}
								
								}
							}
						}
										

					}
				}, mYear, mMonth, mDay);
		dpd.show();
	/*}
	 else
	 {
		 Toast.makeText(SendSmsScreen.this, "Please fill above feilds", Toast.LENGTH_SHORT).show();
	 }*/
	}
		break;
	//select time for scheduling message
	case R.id.editFetchTime:
	{


		// Process to get Current Time
		
	/* if(choosedTemplate.length()>0 && (MobileRecipient.length()>0 || GroupName.length()>0) && choosenTemplatename.length()>0)
    	{
*/
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		// Launch Time Picker Dialog
		TimePickerDialog tpd = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() 
		{

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
														
							String am_pm = "";

						    Calendar datetime = Calendar.getInstance();
						    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
						    datetime.set(Calendar.MINUTE, minute);

						 /*   if (datetime.get(Calendar.AM_PM) == Calendar.AM)
						        am_pm = "AM";
						    else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
						        am_pm = "PM";

						  String strHrsToShow = (datetime.get(Calendar.HOUR) == 00) ?"12":datetime.get(Calendar.HOUR)+""; 
						 */ 
						   
						    String strHrsToShow = ""+datetime.get(Calendar.HOUR_OF_DAY);

							//String HoursDemo = strHrsToShow;
							int MinisDemo = datetime.get(Calendar.MINUTE);
					  
							String minSetPack=""+MinisDemo;
							
							if(MinisDemo <=9)
							{
								minSetPack = "0"+MinisDemo;
							}
							
							currentTime.setText(strHrsToShow+":"+minSetPack+" "+am_pm);
						
							TimePickerSEND =strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm;
							
						   							
					}
				}, mHour, mMinute, false);
		tpd.show();
	
	/*}
	 else
	 {
		 Toast.makeText(SendSmsScreen.this, "Please fill above feilds", Toast.LENGTH_SHORT).show();
	 }*/
	}
		break;
	//send message instantly button on click	
	case R.id.SENDNOW:
	{
		//if promts selected than dialog box appear for confirmation of send message from user
		if(EditProfile.checkPrompt)
		{
			MobileRecipient = Selectedrecepient.getText().toString();
			choosedTemplate = choosenTemplatename.getText().toString();
			GroupName       = textGroupName.getText().toString();
			if(MobileRecipient.length()<=0 && GroupName.length()<=0)	
			{
		    Toast.makeText(SendSmsScreen.this, "Please select atleast one Recipient", Toast.LENGTH_SHORT).show();
			}
			else if(choosedTemplate.length()<=0)
			{
		   Toast.makeText(SendSmsScreen.this, "Please select template", Toast.LENGTH_SHORT).show();
		    }
			else
			{
		new AlertDialog.Builder(this)
		.setCancelable(true)
		.setMessage("Are You sure, You want to send Message?")
		.setPositiveButton("cancel", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
			dialog.cancel();
		        
			}
		})
		.setNegativeButton("ok", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				
				MobileRecipient = Selectedrecepient.getText().toString();
				choosedTemplate = choosenTemplatename.getText().toString();
				GroupName       = textGroupName.getText().toString();
					
						content         = choosenTemplatename.getText().toString().replaceAll("\\s+","").trim();
						MobileRecipient = Selectedrecepient.getText().toString().trim();
						choosedTemplate = choosenTemplatename.getText().toString().trim();
						
						GroupName       = textGroupName.getText().toString().trim();
					
						fetchMobileandUserId();
						fetchMobilenumberEDIT();
						
				           //Log.w("TAG","BBBBBBBBB"+"Mobile"+Mobile);
				           
				           //new webservice(null, webservice.MessageSendToRecipient.geturl(TemplateId, UserLogin, Password, UserId, "SMSSMS", MobileRecipient, content, Mobile, "","1",GroupName), webservice.TYPE_GET, webservice.TYPE_SENT_MESSAGE_INBOX, new ServiceHitListener(){ 
				           Toast.makeText(SendSmsScreen.this,"Wait for somtime while message will be send",Toast.LENGTH_LONG).show();						
							
						   new webservice(null, webservice.SendSms.geturl(TemplateId, UserLogin, Password, UserId, "SMSSMS", MobileRecipient, content, Mobile, GroupName,""), webservice.TYPE_GET, webservice.TYPE_SEND_MSG, new ServiceHitListener() {
							
							@Override
							public void onSuccess(Object Result, int id)
							{
								SendSmsModel mod=(SendSmsModel) Result;
								Selectedrecepient.setText("");
								numofcont.setText("");
								choosenTemplatename.setText("");
								textGroupName.setText("");
								TemplateId= "";
								
								dbObject.Open();
								dbObject.deleteselectedReceipent();
								dbObject.DeleteSELETEDTemplateData();
								dbObject.deleteCustomTemplate();
								dbObject.SelectedGroupName();
								dbObject.close();
								currentTime.setText("");	
								currentDate.setText("");
								String sentnumber=mod.getSendSms().get(0).getMsg();
								try
								{
								if(sentnumber.equalsIgnoreCase("Sent."))
								{
								Toast.makeText(sendSmsFlag, "Sms Sent", Toast.LENGTH_LONG).show();
								}
								else if(sentnumber.equalsIgnoreCase("Authorization Failed"))
								{
								fetchMobileandUserId();
								Intent i=new Intent(SendSmsScreen.this,LoginPage.class);
								i.putExtra("UPDATEPASSWORD","PasswordUpdate");
								i.putExtra("UserLogin", UserLogin);
								startActivity(i);
								finish();
								}
								else
								{
								Toast.makeText(sendSmsFlag,""+mod.getSendSms().get(0).getMsg(), Toast.LENGTH_LONG).show();
									
								}
								}
								catch(Exception ER)
								{
								}
								
								try
								{
								totalavailablebalance();
								} 
								catch (Exception e) 
								{
								}
								 try {
										String EmergencyMessage = mod.getSendSms().get(0).getEmergencyMessage();
										  try
										  {
											
												
										  Emergency.desAct.finish();
										  } 
										  catch (Exception e)
										  {
										  }
										   
										   if(!(EmergencyMessage.equalsIgnoreCase("NO")))
										   {
										    Intent rt = new Intent(SendSmsScreen.this,Emergency.class);
										    rt.putExtra("Emergency", EmergencyMessage);
										    startActivity(rt);

										  }
									} catch (Exception e) {
										
									} 
							
							}
							
							@Override
							public void onError(String Error, int id)
							{
								
								try {
									Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();
								} catch (Exception e1) {
								}	
								
								dbObject.Open();
								dbObject.deleteselectedReceipent();
								dbObject.SelectedGroupName();
								dbObject.DeleteSELETEDTemplateData();
								dbObject.deleteCustomTemplate();
								dbObject.close();	
								
								TemplateId= "";
								try 
								{
								totalavailablebalance();
								} 
								catch (Exception e)
								{
								
								}
							}
						});
				   
		        	
			}
		})
		.show();
		
		}
		}
		else
		{
			//Send Message without conformation
	      
	            MobileRecipient = Selectedrecepient.getText().toString();
				choosedTemplate = choosenTemplatename.getText().toString();
				GroupName       = textGroupName.getText().toString();
				if(MobileRecipient.length()<=0 && GroupName.length()<=0)	
				{
		       Toast.makeText(SendSmsScreen.this, "Please select atleast one Recipient", Toast.LENGTH_SHORT).show();
				}
				else if(choosedTemplate.length()<=0)
				{
				Toast.makeText(SendSmsScreen.this, "Please select template", Toast.LENGTH_SHORT).show();
				}
			  	else 
				{
					 
					    MobileRecipient = Selectedrecepient.getText().toString().trim();
						choosedTemplate = choosenTemplatename.getText().toString().trim();
						content         = choosenTemplatename.getText().toString().replaceAll("\\s+","").trim();
						
						GroupName       = textGroupName.getText().toString().trim();	
				Toast.makeText(SendSmsScreen.this,"Wait for somtime while message will be send",Toast.LENGTH_LONG).show();						
				fetchMobileandUserId();
			new webservice(null, webservice.SendSms.geturl(TemplateId, UserLogin, Password, UserId, "SMSSMS", MobileRecipient, content, Mobile, GroupName,""), webservice.TYPE_GET, webservice.TYPE_SEND_MSG, this);
				   
        	}	
        	
        
        	
					
		}
		
		}
		break;
	 //Add Contact Manually Button navigate to new screen from where we can add contacts
	  case R.id.imageVieweditmanu:
	  {  
		Intent ie=new Intent(SendSmsScreen.this,AddContactManually.class);
		startActivity(ie);
	  }
		break;
	}
}
//on success or error called when url is ping 
@Override
public void onSuccess(Object Result, int id)
{
	if(id==webservice.TYPE_SEND_MSG)
	{
		SendSmsModel mod=(SendSmsModel) Result;
		
		
		Selectedrecepient.setText("");
		numofcont.setText("");
		choosenTemplatename.setText("");
		textGroupName.setText("");
		TemplateId= "";
		currentTime.setText("");	
		currentDate.setText("");
		dbObject.Open();
		dbObject.deleteselectedReceipent();
		dbObject.DeleteSELETEDTemplateData();
		dbObject.deleteCustomTemplate();
		dbObject.SelectedGroupName();
		dbObject.close();
		
		String sentnumber=mod.getSendSms().get(0).getMsg();
		try
		{
		if(sentnumber.equalsIgnoreCase("Sent."))
		{
		Toast.makeText(sendSmsFlag, "Sms Sent", Toast.LENGTH_LONG).show();
		}
		else
		{
		Toast.makeText(sendSmsFlag,""+mod.getSendSms().get(0).getMsg(), Toast.LENGTH_LONG).show();
			
		}
		}
		catch(Exception ER)
		{
		}
		
		
		try
		{
		totalavailablebalance();
		} 
		catch (Exception e) 
		{
		}
		 try {
				String EmergencyMessage = mod.getSendSms().get(0).getEmergencyMessage();
				  try
				  {
					
						
				  Emergency.desAct.finish();
				  } 
				  catch (Exception e)
				  {
				  }
				   
				   if(!(EmergencyMessage.equalsIgnoreCase("NO")))
				   {
				    Intent rt = new Intent(SendSmsScreen.this,Emergency.class);
				    rt.putExtra("Emergency", EmergencyMessage);
				    startActivity(rt);

				  }
			} catch (Exception e) {
				
			} 
	
	}
    }

@Override
public void onError(String Error, int id) {
	
	try {
		Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();
	} catch (Exception e1) {
	}	
	
	dbObject.Open();
	dbObject.deleteselectedReceipent();
	dbObject.DeleteSELETEDTemplateData();
	dbObject.deleteCustomTemplate();
	dbObject.SelectedGroupName();
	dbObject.close();	
	
	TemplateId= "";
	try 
	{
	totalavailablebalance();
	} 
	catch (Exception e)
	{
	
	}
}
//To get custom message from add custom template page
public String getcustom()
{
	
	dbObject.Open();
	  
	   Cursor c;
	   c=dbObject.getCustomTemplates();
	   while(c.moveToNext())
	   {
		   TemplatesCustom=c.getString(1);
		   //Log.w("getcustom", "getallmsg"+TemplatesCustom);
	   }
	   dbObject.close();  
	return TemplatesCustom;
	
}
public void fetchMobileandUserId() 
{
	dbObject.Open();
	  
	   Cursor c;
	   
	   c= dbObject.getLoginDetails();
	   
	  				   
	   while(c.moveToNext())
	   {
          UserLogin=c.getString(6);
		  Mobile   = c.getString(1);
		   UserId   = c.getString(3);
		   Password = c.getString(5);
		   
	   }
			   
	   dbObject.close();
	   
}

public void fetchMobilenumberEDIT() 
{
	dbObject.Open();
	  
	   Cursor c;
	   
	   c= dbObject.getProfile();
	   
	  				   
	   while(c.moveToNext())
	   {
           Mobile   = c.getString(2);
           //Log.w("TAG","AAAAAAAAAA"+"Mobile"+Mobile);
	  }
			   
	   dbObject.close();
	   
}

public void fetchTemplate()
{
	
	   dbObject.Open();
	  
	   Cursor c;
	   
	   c= dbObject.getSELETEDTemplates();
	   
	  				   
	   while(c.moveToNext())
	   {
		   
		   Template   = c.getString(2);
		   TemplateId =c.getString(1);
	   }
			   
	   dbObject.close();
}

public String fetchREspient()
{
	
	int groupcount=0;
	dbObject.Open();	
	Cursor c,c1;
	
	c= dbObject.getReceipent();
	c1 = dbObject.getSelectedGroupName();
    countTotalcontact = c.getCount();
  
	String data="";
	
    if(c.getCount() >= 1)
    {
   while(c.moveToNext() )
    {  
	data += c.getString(1)+",";
    }
    }
  
     //if recipients are numbers and group
    if(c.getCount()>0 && c1.getCount()>0)
    {
   
   	while(c1.moveToNext())
   	{
   	groupcount += c1.getInt(2);
   	}
     int a=groupcount+countTotalcontact;
     numofcont.setText(""+a); 
    }
   
  
    
    dbObject.close();
   
	return data;
	
}


//recipientNumber

public boolean IsTemplateExist(){
	dbObject.Open();
	  
	   Cursor c;
	   
	   c= dbObject.getTemplates();
	   
	  				   
	   while(c.moveToNext())
	   {
		 
		   dbObject.close();
		   return true;
	   }
			   
	   dbObject.close();
	   return false;
}

public boolean IsResContactExist()
{
	   
	   dbObject.Open();
	  
	   Cursor c;
	   
	   c= dbObject.getReceipent();
	   
	  				   
	   while(c.moveToNext())
	   { 
		  dbObject.close();
		  return true;
	   }
			   
	   dbObject.close();
	   return false;
}

public String getIndividuleDetails()
{
	dbObject.Open();	
	Cursor c;
	
	c= dbObject.getContactALL();
	
	int count = c.getCount();
	countTotalAll = c.getCount();
	//Log.w("TAG","count:"+countTotalAll);
	
	String icontact = "";
	
    if(c.getCount() >= 1){
    
    	int i=0;
    	while(c.moveToNext() && i<count)
    	{
    	icontact +=c.getString(3)+",";
    	i++;
    	}
    }
    
    dbObject.close();
   
	return icontact;
}

public String getIndividuleDetailsName()
{
	dbObject.Open();	
	Cursor c;
	
	c= dbObject.getContactALL();
	
	int count = c.getCount();
	countTotalAll = c.getCount();
	//Log.w("TAG","count:"+countTotalAll);
	
	String icontact = "";
	
    if(c.getCount() >= 1){
    
    	int i=0;
    	while(c.moveToNext() && i<count)
    	{
    	icontact +=c.getString(2)+",";
    	i++;
    	}
    }
    
    dbObject.close();
   
	return icontact;
}

@Override
public void onBackPressed() {
	
	callfxnatOnbackpress();
	stopHandler();
}
//when user exit from this activity on back press all the feilds alongwith database we be reset to clear
private void callfxnatOnbackpress() 
{
	choosenTemplatename.setText("");
	Selectedrecepient.setText("");
	numofcont.setText("");
	textGroupName.setText("");
	currentTime.setText("");	
	currentDate.setText("");
	currentTime.setText("");	
	currentDate.setText("");
	CustomTemplate.customRemplate= false;
	
	dbObject.Open();
	dbObject.deleteselectedReceipent();
	dbObject.DeleteSELETEDTemplateData();
	dbObject.deleteCustomTemplate();
	dbObject.SelectedGroupName();
	dbObject.close();
	
	try 
	{
	Home.HomeStatus.finish();
	}
	catch (Exception e) 
	{
	e.printStackTrace();
	}
	
	Intent ifd = new Intent(SendSmsScreen.this, Home.class);
	ifd.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	startActivity(ifd);
	//finish();
}




//startHandler called to check total recipients counts regularly by calling fetch recipient in 1500 micro seconds
private void startHandler()
{
checker.run();
}
public void check()
{
	handler=new Handler();
    checker=new Runnable() {
		
		@Override
		public void run() {
			
			
			 if (Validation.hasTextinrecipient(Selectedrecepient) /*|| Validation.hasText(textGroupName)*/ )
		     {
			 editcontacts.setVisibility(View.VISIBLE);	  
			 fetchREspient();
			 }
			 else
			 {
			
			 }
			handler.postDelayed(checker,1500);
		}
		
	};
    
    }
//stop handler started if exit from activity
public void stopHandler()
{
	try
	{
	handler.removeCallbacksAndMessages(checker);
	
	}
	catch(Exception e)
	{
		Log.v("inside handler","handle"+e);
	}
}

//Schedule message called to schedule message at particular date and time
public void callschedualMessageservice()
{

	//if prompt selected than confirmation dialog appear before sending message
	if(EditProfile.checkPrompt)
	{
		new AlertDialog.Builder(this)
		.setCancelable(false)
		.setMessage("Are You sure, You want to Schedule Message?")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				//String MobileSchedule          = "9582116782";
	        	
				try
				{
	        	if(!SendSmsScreen.choosedTemplate.equals(""))
	        	{
		        	
	        		if(!DatePickerSEND.equals("") && !TimePickerSEND.equals(""))
		        	{
	        		String TemplateMessageSchedule = SendSmsScreen.choosedTemplate;
	        		String MobileScheduleSchedule  = SendSmsScreen.MobileRecipient;
	        		//String TemID                   = SendSmsScreen.temid;
	        		
	        		////Log.w("Tag","Template ID_Before_Service:"+tem);
	        		
	        		fetchMobileandUserId();
	        		//fetchMobilenumberEDIT();
	        		try{
	        			// Template set from database	
	        				if(IsTemplateExist()){
	        					fetchTemplate();
	        					tem=TemplateId;
	        				}
	        			}
	        			catch(Exception ed){}
	        		
	        		try{
	        			//number = MobileRecipient
	        			//gpName = GroupName
	        			
	        	        Log.w("??????????","@@@@@@@"+MobileRecipient);
	        			}
	        			catch(Exception e){
	        				e.printStackTrace();
	        			}
	        		
	        		Log.w("Tag","Template ID_Before_Service:"+tem);
	        		String dfg=getcustom();
	        		
	        		long checkmili = 0;
	        		long d11 = 0;
	        		
	        		try {
						checkmili = fetchCurrentTimeDate();
						
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						Date selDate = df.parse(DatePickerSEND+" "+TimePickerSEND);
								            	
						d11 = selDate.getTime();
							
						Log.w("Time_Mili","Time_milli  ::::(d11):"+d11+"selcteddateTime:"+DatePickerSEND +","+TimePickerSEND);
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}  
	        	
	        		long currentValue = Math.max(checkmili, d11);
	        		
	        		Integer CUR_INT = (int) (long) checkmili;
	        		Integer SEL_INT = (int) (long) d11;
	        		
	        		Log.w("Tag","Time_Mili   (CUR_INT):"+CUR_INT+","+SEL_INT);
	        		
	        		if(CUR_INT > SEL_INT){
						Toast.makeText(fedt, "Schedule dateTime should be greater than current dateTime", Toast.LENGTH_SHORT).show();
	        		}
	        		else if(CUR_INT < SEL_INT){
	
						new webservice(null, webservice.SendSmsSchedule.geturl(tem, UserLogin, Password, UserId, MobileRecipient, DatePickerSEND, TimePickerSEND,dfg,Mobile,GroupName,""), webservice.TYPE_GET, webservice.TYPE_SEND_MSG_SCHEDULE, new ServiceHitListener() {
							
							@Override
							public void onSuccess(Object Result, int id) {
								
								ScheduleSmsSend mod=(ScheduleSmsSend) Result;
								
							
								String fgf = "";
								try {
									fgf = mod.getSheduledSms().get(0).getMsg();
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
								Log.w("TAG::","VALUE::::::::1:::::fgf:"+fgf);
								
								String DataMsg="";
								
								try{
									
									DataMsg = fgf.substring(0,fgf.indexOf("\r"));
									Log.w("TAG::","VALUE::::::::TRY:::::DataMsg:"+DataMsg);
								}
								catch(Exception e){
									flgcheck = true;
									DataMsg = fgf;
									Log.w("TAG::","VALUE::::::::CATCH:::::DataMsg:"+DataMsg);
								}
								
								

								Log.w("TAG::","VALUE::::::::2:::::DataMsg:"+DataMsg);
								
								try
								{
						            if(flgcheck)
						            {
						            	Toast.makeText(fedt, ""+DataMsg, Toast.LENGTH_LONG).show();
						            }
										// {"sheduledsms":[{"Msg":"Recipient Can Not be Less Than Two"}]}

						            else{
									
						            	Toast.makeText(fedt, ""+DataMsg, Toast.LENGTH_LONG).show();
						         
																	
									dbObject.Open();
									dbObject.deleteselectedReceipent();
									dbObject.DeleteSELETEDTemplateData();
									dbObject.deleteCustomTemplate();
									dbObject.close();
									
								    try 
								    {
								    SendSmsScreen.sendSmsFlag.finish();	
								    } 
								    catch (Exception e) 
								    {
									e.printStackTrace();
									}
								    
										Intent i=new Intent(SendSmsScreen.this,ScheduleList.class);
										startActivity(i);
									    finish();
									    
						            }
								}
								catch(Exception ER){
									ER.printStackTrace();
								}
								
							}
							
							@Override
							public void onError(String Error, int id) 
							{
								Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();	
								dbObject.Open();
								dbObject.deleteselectedReceipent();
								dbObject.DeleteSELETEDTemplateData();
								dbObject.deleteCustomTemplate();
								dbObject.close();	
							}
						});	
	        		
	        		}
	        		else{
						Toast.makeText(fedt, "Schedule dateTime should be greater than current dateTime", Toast.LENGTH_SHORT).show();
	        		}
	  	
		        	}
		        	
		        	else{
		        		Toast.makeText(getApplicationContext(), "Please Select Date and Time Both for Schedule Message", Toast.LENGTH_SHORT).show();
		        	}
	        	}

	        	}
				catch(Exception e){
	        		Toast.makeText(getApplicationContext(), "PLease Select At least One Template", Toast.LENGTH_SHORT).show();
					
				}
		
			}
		})
		
		.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.setIcon(R.drawable.ic_launcher)
		.show(); 
		
	}
	
	
    else
	{
	//schedule message without confirmation
	try
	{
	if(!SendSmsScreen.choosedTemplate.equals(""))
	{
    	
		if(!DatePickerSEND.equals("") && !TimePickerSEND.equals(""))
    	{
		
		fetchMobileandUserId();
		fetchMobilenumberEDIT();
		
		try{
			// Template set from database	
				if(IsTemplateExist())
				{
					fetchTemplate();
					tem=TemplateId;
				}
			}
			catch(Exception ed){}
		
		Log.w("Tag","Template ID_Before_Service:"+tem);
		String dfg=getcustom();
		
		
		try{
			
			//number = MobileRecipient
			//gpName = GroupName
			
	        Log.w("??????????","@@@@@@@"+MobileRecipient);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		
		long checkmili = 0;
		long d11 = 0;
		
		try {
			checkmili = fetchCurrentTimeDate();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date selDate = df.parse(DatePickerSEND+" "+TimePickerSEND);
					            	
			d11 = selDate.getTime();
				
			Log.w("Time_Mili","Time_milli  ::::(d11):"+d11+"selcteddateTime:"+DatePickerSEND +","+TimePickerSEND);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}  
	
		long currentValue = Math.max(checkmili, d11);
		
		Integer CUR_INT = (int) (long) checkmili;
		Integer SEL_INT = (int) (long) d11;
		
		Log.w("Tag","Time_Mili   (CUR_INT):"+CUR_INT+","+SEL_INT);
		
		if(CUR_INT > SEL_INT){
			Toast.makeText(fedt, "Schedule dateTime should be greater than current dateTime", Toast.LENGTH_SHORT).show();
		}
		else if(CUR_INT < SEL_INT){
			//Toast.makeText(fedt, "call websevice???", Toast.LENGTH_SHORT).show();
			new webservice(null, webservice.SendSmsSchedule.geturl(tem, UserLogin, Password, UserId, MobileRecipient, DatePickerSEND, TimePickerSEND,dfg,Mobile,GroupName,""), webservice.TYPE_GET, webservice.TYPE_SEND_MSG_SCHEDULE, new ServiceHitListener() {
				
				@Override
				public void onSuccess(Object Result, int id) {

					
					ScheduleSmsSend mod=(ScheduleSmsSend) Result;
			
					
					Log.w("TAG::","VALUE:success:");
					
					String fgf = "";
					try {
						fgf = mod.getSheduledSms().get(0).getMsg();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					Log.w("TAG::","VALUE::::::::1:::::fgf:"+fgf);
					
					String DataMsg="";
					
					try{
						
						DataMsg = fgf.substring(0,fgf.indexOf("\r"));
						Log.w("TAG::","VALUE::::::::TRY:::::DataMsg:"+DataMsg);
					}
					catch(Exception e){
						flgcheck = true;
						DataMsg = fgf;
						Log.w("TAG::","VALUE::::::::CATCH:::::DataMsg:"+DataMsg);
					}
					
					

					Log.w("TAG::","VALUE::::::::2:::::DataMsg:"+DataMsg);
					
					try
					{
			            if(flgcheck)
			            {
			            	Toast.makeText(fedt, ""+DataMsg, Toast.LENGTH_LONG).show();
			            }
							// {"sheduledsms":[{"Msg":"Recipient Can Not be Less Than Two"}]}

			            else{
						
			            	Toast.makeText(fedt, ""+DataMsg, Toast.LENGTH_LONG).show();
			         
														
						dbObject.Open();
						dbObject.deleteselectedReceipent();
						dbObject.DeleteSELETEDTemplateData();
						dbObject.deleteCustomTemplate();
						dbObject.close();
						
					    try 
					    {
					    SendSmsScreen.sendSmsFlag.finish();	
					    } 
					    catch (Exception e) 
					    {
						e.printStackTrace();
						}
					    
							Intent i=new Intent(SendSmsScreen.this,ScheduleList.class);
							startActivity(i);
						    finish();
						    
			            }
					}
					catch(Exception ER){
						ER.printStackTrace();
					}
					
				
					
				}
				
				@Override
				public void onError(String Error, int id) {
					// TODO Auto-generated method stub
					
				}
			});

		}
		else{
			Toast.makeText(fedt, "Schedule dateTime should be greater than current dateTime", Toast.LENGTH_SHORT).show();
		}
		
		Log.w("Time_Mili","Time_Mili  ::(currentValue):"+currentValue);
				
		}
		
    	
    	else
    	{
    	Toast.makeText(getApplicationContext(), "Please Select Date and Time Both for Schedule Message", Toast.LENGTH_SHORT).show();
    	}
	}

	}
	catch(Exception e)
	{
	Toast.makeText(getApplicationContext(), "PLease Select At least One Template", Toast.LENGTH_SHORT).show();
	}
	
	/*Intent i = new Intent(Schedule.this,ScheduleList.class);
	startActivity(i);*/
	
}

}

//fetch current date and time
private long fetchCurrentTimeDate() 
{
     DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
     
     curDateTime  = df.format(Calendar.getInstance().getTime());
     
           long d1 = 0 ;
	
			try {
				d1 = df.parse(curDateTime).getTime();
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
	Log.w("Time_Mili","Time_milli  ::::(d1):"+d1+",curDateTime:"+curDateTime);	
	      
     return d1;
}

}

