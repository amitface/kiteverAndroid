package sms19.listview.newproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import sms19.listview.adapter.ContactAdaptor;
import sms19.listview.adapter.customAdaptorContactDataBASE;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.FetchAllContact;
import sms19.listview.newproject.model.FetchAllContact.ContactDetails;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.newproject.model.GroupDataModel;
import sms19.listview.newproject.model.contactmodelIndividule;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
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
import com.kitever.sendsms.SendSmsScreen;

public class NewContact extends Activity implements OnClickListener
{

	ListView ContactShowList;
	TextView btncontactshow, btngroupshow, totalContacts, btnForSendMessage,btnContactAddNew,btnGroupAddNew;
	
	TextView textpageLast;
	ImageView vback;

	public static CheckBox cbxselect;
	ImageView imgSearchContact,refresh,imgprevious,imgnext;
	EditText editTextSearchContact;
	int counting;
	String b;
	String Mobile= "",a,Password = "";
	private String UserId="";
	
	public static Activity contactsGroupFlag;
	
	ProgressDialog contact;
	
	Context gContext;
	
	List<contactmodelIndividule> setdbdataInAdapterInd;
	customAdaptorContactDataBASE ccAdpt;
	ContactAdaptor cAdpt;
	
	//flag for contact page
	public static boolean checkAll= false;
	boolean checkStatus= true;
	
	String RecpientNumber="", RecipentName="";	
	int countNUm;
	String dob="", Anni = "";
	String name   = "";
	String number = "";
	String groupAdd;
	
	// paging
	 int changer      = 0;// 0,5,10,15.....(n+5)
	
	public static int counterArrow = 0;
	
	int click = 0;
	int x = 0, y = 10;
	int countingClick = 0;
	int TotalPage  = 0;
	
	//create object of database
	DataBaseDetails dbObject=new DataBaseDetails(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newcontacthomepage);

		editTextSearchContact = (EditText)  findViewById(R.id.editTextSearchContact);
		ContactShowList       = (ListView)  findViewById(R.id.listViewContact);
		btncontactshow        = (TextView)  findViewById(R.id.btncontactshow);
		btngroupshow          = (TextView)  findViewById(R.id.btngroupshow);
		btnContactAddNew      = (TextView)  findViewById(R.id.btnContactAddNew);
		btnGroupAddNew        = (TextView)  findViewById(R.id.btnGroupAddNew);
		cbxselect             = (CheckBox)  findViewById(R.id.cbxContactSelectAll);
		imgSearchContact      = (ImageView) findViewById(R.id.imgSearchContact);
		totalContacts         = (TextView)  findViewById(R.id.totalContacts);
		refresh               = (ImageView) findViewById(R.id.refresh);
		btnForSendMessage     = (TextView)  findViewById(R.id.btnToSendByContact);//btnToSendByContact
		vback                 = (ImageView) findViewById(R.id.backbutton);
		// image mapping for next and previous data
		imgnext               = (ImageView) findViewById(R.id.imgnext);
		imgprevious           = (ImageView) findViewById(R.id.imgprevious);
		
		// for show page number
		textpageLast      = (TextView)  findViewById(R.id.textpageLast);
		
		/***************************INTERNET********************************/
		webservice._context= this;

		gContext=this;
		contactsGroupFlag = this;
		/***************************INTERNET********************************/
		counterArrow = 0;
		
		
		
		// use for know all data length
		getIndividuleDetails();
		
		//Log.w("DB","DB :::::::::(counting)"+counting);
		
		try
		{
		//set textview to total counting number
		totalContacts.setText(""+counting);
		} 
		catch (Exception e)
		{
		e.printStackTrace();
		}
		
		TotalPage = findPageNumber(counting);
		
		try
		{
			//set last page count
			//textpageLast.setText(""+TotalPage);
		} 
		catch (Exception e)
		{
		e.printStackTrace();
		}
		
		//Log.w("TP","TP :: (TotalPage):"+TotalPage);
		
		// hide next button of page.
		if(TotalPage < 2){
			imgnext.setVisibility(View.INVISIBLE);
		}
		
		callContactAdapterwithdatabase();
		
		vback.setOnClickListener(this);
		refresh.setOnClickListener(this);
		btngroupshow.setOnClickListener(this);
		btnContactAddNew.setOnClickListener(this);
		btnGroupAddNew.setOnClickListener(this);
		cbxselect.setOnClickListener(this);
		imgSearchContact.setOnClickListener(this);
		btnForSendMessage.setOnClickListener(this);
		
		// onClick event decide.
		imgnext.setOnClickListener(this);
		imgprevious.setOnClickListener(this);
		
		// onClick event decide for paging.
			
		callFxnForSearchContact();
		
		      Intent i=getIntent();
	          try
	          {
	            String refershadd=i.getStringExtra("adddatacontact");
	          if(refershadd.equalsIgnoreCase("sucessfullyadded"))
		       {
		       callWebServiceForFetchData();  
		       }
	          }
	          catch(Exception w){}
	          
	          	try
	          	{
	                groupAdd = i.getStringExtra("groupname");
		            
					if(!groupAdd.equals("") || groupAdd != null)
					{
						btnForSendMessage.setText("Add to Group");
					}
				} 
	          	catch (Exception e) 
				{
				e.printStackTrace();
				}
	            }


	/**
	 * 
	 */
	

    @Override
	public void onClick(View v) 
    {
		
		switch(v.getId())
		{
		
		case R.id.btnContactAddNew:
		{
				Intent ic=new Intent(NewContact.this,ContactAdd.class);
				startActivity(ic);
	    }
		break;
		
		case R.id.btnGroupAddNew:
		{
			if(cbxselect.isChecked())
			{
			
			String[] values = getIndividuleDetails();
			
			Intent ie=new Intent(NewContact.this,AddToGroup.class);
		    ie.putExtra("allcheckedvalues", values);
			startActivity(ie);
			}
			else
			{
			 Intent ie=new Intent(NewContact.this,AddToGroup.class);
			 startActivity(ie);	
			}
		}  
		break;
		case R.id.refresh:
		{
				InflatecontactfmServer();
		}
			break;
		case R.id.btngroupshow:
		{
			Intent i=new Intent(NewContact.this,GroupNewAdd.class);
			startActivity(i);
			finish();
		}	
		break;
			
		case R.id.cbxContactSelectAll:
		{
			//Log.w("TAG","CheckAll_Click"+checkStatus);
			
			 if(checkStatus)
			   {
				checkAll= true;
			
				callContactAdapterwithdatabase();
				checkStatus= false;
			   }
			  
			  else
			    {
				  checkAll= false;
				
				  callContactAdapterwithdatabase();
				  checkStatus= true;
			    }
		}
		break;
		case R.id.imgSearchContact:
		{
			editTextSearchContact.setVisibility(View.VISIBLE);
			editTextSearchContact.setFocusableInTouchMode(true);
			editTextSearchContact.setCursorVisible(true);
			editTextSearchContact.setBackgroundResource(R.drawable.background_round_white);
			//Toast.makeText(gContext, "Please click Again to Search", Toast.LENGTH_SHORT).show();
			
		}		
			
			break;
	
		case R.id.btnToSendByContact:
		{
			try
			{
			SendSmsScreen.sendSmsFlag.finish();
			}
			catch(Exception e){}
			
		//	 checkAll= false;
			
			if(btnForSendMessage.getText().toString().trim().equalsIgnoreCase("Add to Group")) 
			{
				//Toast.makeText(gContext, "Work in progress", Toast.LENGTH_SHORT).show();

               /*********************ADD CONTACT TO GROUP*************************/	
							
			 try
			 {
			fetchDataFromDbforGroupContact();
			callInsertcontacttogroup();
			}
				catch(Exception e)
				{
					try
					{
					NewContact.contactsGroupFlag.finish();	
					}
					catch(Exception ee)
					{
						
					}
					
					Toast.makeText(gContext, "No contact(s) selected", Toast.LENGTH_SHORT).show();
					
					NewContact.checkAll= false;
					
					Intent i=new Intent(NewContact.this,GroupNewAdd.class);
					//Contacts.switchtoTab(1);
					startActivity(i);
					finish();
					
				}
				
				/***************************************************************/
				
				
			}
			else
			{
				Intent isend=new Intent(NewContact.this,SendSmsScreen.class);
				startActivity(isend);
				finish();
			}
			
		}	
		break;
		
		case R.id.imgnext:
		{
		
			//Log.w("DB","DB :::::(countingClick)"+countingClick+",x:"+x);
			
			String fetchData = textpageLast.getText().toString();
			
			int convertInt = Integer.parseInt(fetchData);
						
			if(TotalPage == convertInt){
				imgnext.setVisibility(View.INVISIBLE);
			}
			else{
				fetchRange(click);
				
				if(countingClick > x)
				{
					click += 1;
												
					callContactAdapterwithdatabase();
			
					
				}
			}
			
			
						
			
		
		}	
		break; 
		
        case R.id.imgprevious:
        {
			
        	if(click != 0)
        	{
        		click -= 1;	
        	}
                    
       	    //Log.w("DB","DB :::::::::::click"+click);
        	
       	    fetchRange(click);
        	callContactAdapterwithdatabase();	
        	
        }	
		break;
        case R.id.backbutton:
        {
     // Intent i=new Intent(NewContact.this,Home.class);
    //  startActivity(i);
      finish();
        }
        break;
        
 		default:
			break;
		
		}
		
	} 
	
	public void InflatecontactfmServer() 
	{
	getIndividuleDetails();
		
		//Log.w("DB","DB :::::::::(counting)"+counting);
		
		try
		{
			//set textview to total counting number
			totalContacts.setText(""+counting);
		} 
		catch (Exception e)
		{
		e.printStackTrace();
		}
				
		fetchMobileandUserId();
		contact=ProgressDialog.show(this, null, "Loading Contacts");
		new webservice(null, webservice.GetAllContact.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_GET_ALL_CONTACT, new ServiceHitListener() {
			
			@Override
			public void onSuccess(Object Result, int id) 
			{
				try {
					contact.dismiss();
				} catch (Exception e1) 
				{
					
					e1.printStackTrace();
				}
				
				FetchAllContact Model  = (FetchAllContact) Result;
				
				try {
					String EmergencyMessage = Model.getContactDetails().get(0).getEmergencyMessage();
					
					  try
					  {
					  Emergency.desAct.finish();
					  } 
					  catch (Exception e)
					  {
					  }
					   
					   if(!(EmergencyMessage.equalsIgnoreCase("NO")))
					   {
					    Intent rt = new Intent(NewContact.this,Emergency.class);
					    rt.putExtra("Emergency", EmergencyMessage);
					    startActivity(rt);

					  }
				} catch (Exception e) {
					e.printStackTrace();
				}
			
				
				List<ContactDetails> list = Model.getContactDetails();
				
				cAdpt = new ContactAdaptor(list, contactsGroupFlag);
				ContactShowList.setAdapter(cAdpt);

		        //Log.w("TAG","VALUE::"+cAdpt.getCount());
		        
		        String valusCount = Integer.toString(cAdpt.getCount());
		        
				  try 
				  {
				//  count.setText(valusCount);
				  }
				  catch (Exception e) 
				  {
				  e.printStackTrace();
				  }
		       
		        if(haveIndContact())
		        {
				 dbObject.Open();
				 dbObject.DeleteContactDataALL();
				 dbObject.close();
		        }
		           
		           
				for(int i=0;i<Model.getContactDetails().size();i++)
				{
					String contactName   = Model.getContactDetails().get(i).getContact_name();
					String contactMobile = Model.getContactDetails().get(i).getContact_mobile();
				  //  String dob           =Model.getContactDetails().get(i).getDOB();
				   // String aniver         =Model.getContactDetails().get(i).getANIVER();
					fetchMobileandUserId();
					
				    dbObject.Open();
				    dbObject.addContactAll(UserId, Mobile, contactName, contactMobile, "0","","");
					dbObject.close();
				}
				
		     }
			
			@Override
			public void onError(String Error, int id) 
			{
				try 
				{
					contact.dismiss();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Toast.makeText(gContext	, Error, Toast.LENGTH_SHORT).show();	
				
			}
		    });
	}


	private void fetchDataFromDbforGroupContact()
	{
		  //Log.w("TAG","%%%%%%%%%%%%%%%%%%%%%%NewContact.checkAll:"+NewContact.checkAll);
		
		if(NewContact.checkAll)
		{
			
			NewContact.checkAll= false;
			
			RecpientNumber = getIndividuleDetailsG();
			
			RecipentName  = getIndividuleDetailsName();
			
			//Log.w("TAG","RecpientNumber"+RecpientNumber+"RecipentName:"+RecipentName);
			
			 // count commas of number
			  countNUm= countNumber(RecpientNumber)+1;
			  
			  //Log.w("TAG","COUNT----->:"+countNUm+"(countNUm-1):"+(countNUm-1));
			 
			  for( int i=0; i<(countNUm-1); i++ ) {
			          dob  += ",";
					  Anni += ",";
			  }
			 
			  //Log.w("TAG","dob--->:"+dob+"Anni---->:"+Anni);
			
			
			  name   = removeBackComma(RecipentName);
			  
			  number = removeBackComma(RecpientNumber);
			  
			  //Log.w("AFTER REMOVE:TAG","VALUE:NAME----->:"+name+"--NUMBER--->:"+number);
		}
		
		else
		{	
			
		String values[] = getAllContactsTOaddIntoGroup();

		//Log.w("TAG","VALUES_ADD_GROUP"+values);
		

		
		  for(int i=0;i<values.length;i++)
		    {
		    	String demo=values[i];
		    	
		    	//Log.w("TAG","VALUES:ARRARY:::"+demo);
		    	
		    	name   += demo.substring(0,demo.indexOf(","))+",";
		    	number += demo.substring(demo.indexOf(",")+1)+",";
		    	
		    	}
		  
		  //Log.w("TAG_____","VALUE:_______NAME:"+name+"_________NUMBER:"+number);
		  
		  // count commas of number
		  countNUm= countNumber(number)+1;
		  
		  //Log.w("TAG_____","COUNT:"+countNUm+"(countNUm-1):"+(countNUm-1));
		 
		  for( int i=0; i<(countNUm-1); i++ ) {
		          dob  += ",";
				  Anni += ",";
		  }
		 
		  //Log.w("TAG_____","dob:"+dob+"Anni:"+Anni);
		  
		  name   = removeBackComma(name);
		  
		  number = removeBackComma(number);
		  
		  //Log.w("AFTER REMOVE:TAG_____","VALUE_______NAME:"+name+"_________NUMBER:"+number);
		  
   }
	}


	private void callInsertcontacttogroup() 
	{
		 fetchMobileandUserId();
			//  String Uid,String contact_person_name,String contactNumber,String conDob,String conAni, String GroupNam
		new webservice(null, webservice.InsertGroupContact.geturl(UserId, name, number, dob, Anni, groupAdd), webservice.TYPE_GET, webservice.TYPE_INSERT_GRP_CONT, new ServiceHitListener() {
			
			@Override
			public void onSuccess(Object Result, int id)
			{
				dbObject.Open();
				dbObject.deleteSelectedContactsFromGroup();
				dbObject.close();
			
				try
				{
				NewContact.contactsGroupFlag.finish();
				}
				catch(Exception e){}
				
				NewContact.checkAll= false;
				GroupDataModel mod=(GroupDataModel) Result;
				
				/*try {
					String EmergencyMessage = mod.getContactGroupRegistration().get(0).getEmergencyMessage();
					
					  try
					  {
					  Emergency.desAct.finish();
					  } 
					  catch (Exception e)
					  {
					  }
					   
					   if(!(EmergencyMessage.equalsIgnoreCase("NO")))
					   {
					    Intent rt = new Intent(NewContact.this,Emergency.class);
					    rt.putExtra("Emergency", EmergencyMessage);
					    startActivity(rt);

					  }
				} catch (Exception e) {
					e.printStackTrace();
				}*/
			
				
				mod.getContactGroupRegistration().get(0).getMsg();
				mod.getContactGroupRegistration().get(0).getError();
				if(mod.getContactGroupRegistration().get(0).getMsg().length()>0)
				{
				Toast.makeText(gContext, "Contact(s) added successfully", Toast.LENGTH_SHORT).show();
				}
				else
				{
				Toast.makeText(gContext, ""+mod.getContactGroupRegistration().get(0).getError(), Toast.LENGTH_SHORT).show();

				}
				FetchContact();
				Intent i=new Intent(NewContact.this,GroupNewAdd.class);
				//Contacts.switchtoTab(1);
				i.putExtra("refreshgp", "sucessfuyllyadd");
				startActivity(i);
				finish();
			}
			
			@Override
			public void onError(String Error, int id)
			{
			Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();	
	       }
		   });
		    }
	
	public String getIndividuleDetailsG()
	{
		dbObject.Open();	
		Cursor c;
		
		c= dbObject.getContactALL();
		
		int count = c.getCount();
		
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
	
	public static int countNumber(String str)
	{
	    if(str == null || str.length()<=0 || str.isEmpty())
	        return 0;

	    int count = 0;
	    for(int e = 0; e < str.length(); e++)
	    {
	        if(str.charAt(e) == ','){
	            count++;
	           
	        }
	    }
	    return count;
	}

	public String removeBackComma(String value){
		
		 return value.substring(0,value.length()-1);
		
	}
	
	public String[] getAllContactsTOaddIntoGroup()
	{
		 dbObject.Open();
		 Cursor c;
		 
		 c=dbObject.getSelectedGroupContacts();
		 
		 int count=c.getCount();
		 
		 String data[] = new String[count];
		 
		 if(count>=1)
		 {
		     int i=0;
			 while(c.moveToNext())
			 {
			 /*contact person name =c.getString(0);
			 contactNumber=c.getString(1);*/
				 
			 data[i]= c.getString(0)+","+c.getString(1);
			 //Log.w("all data","@@@@@@"+data[i]);
			 i++;
			 }
			 
		 }
		 
		 dbObject.close();
		 
		 return data;

	}
	
	public void FetchContact()
	{
		fetchMobileandUserId();
		
	  	new webservice(null, webservice.GetAllGroupDetails.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_GET_GROUP_DETAILS, new ServiceHitListener() {
			
			@Override
			public void onSuccess(Object Result, int id) 
			{
				
			//**********************************GROUP SEARCH***************************************//*
				
				FetchGroupDetails Model  = (FetchGroupDetails) Result;
				//List<GroupDetails> list = Model.getGroupDetails();
				
				try {
					String EmergencyMessage = Model.getGroupDetails().get(0).getEmergencyMessage();
					
					  try
					  {
					  Emergency.desAct.finish();
					  } 
					  catch (Exception e)
					  {
					  }
					   
					   if(!(EmergencyMessage.equalsIgnoreCase("NO")))
					   {
					    Intent rt = new Intent(NewContact.this,Emergency.class);
					    rt.putExtra("Emergency", EmergencyMessage);
					    startActivity(rt);

					  }
				} catch (Exception e) {
					e.printStackTrace();
				}
			
				
				dbObject.Open();
				dbObject.DeleteGroupDataALL();
				dbObject.close();
			
				for(int i=0;i<Model.getGroupDetails().size();i++)
				{
				fetchMobileandUserId();
				String gpname=Model.getGroupDetails().get(i).getGroup_name();
				String gpCount=Model.getGroupDetails().get(i).getNoOfContacts();
				
				dbObject.Open();
				dbObject.addGroupAll(UserId, gpname, gpCount);
				dbObject.close();
				}
			    }
			
			@Override
			public void onError(String Error, int id)
			{
				
				Toast.makeText(gContext, Error, Toast.LENGTH_SHORT).show();	
				
			}
		});
	}
	
	
	public void callFxnForSearchContact()
	{
		
		/**********************************Filter of Contact-CONTACT SEARCH*************************************/
        
		ContactShowList.setTextFilterEnabled(true);
        
        EditText ed = (EditText) findViewById(R.id.editTextSearchContact);
        
        ed.addTextChangedListener(new TextWatcher() 
        {
			
			@Override
			public void onTextChanged(CharSequence ss, int start, int before, int count) 
			{
				////Log.w("TAG","VALUE"+ss);
				////Log.w("TAG","BEFORE"+before+"::"+"START:"+start+"::"+"count:"+count);
				if(count < before) 
				{   
					try
					{
					ccAdpt.resetData();
				    }
                    catch(Exception e)
                    {
                    	
                    }	
				
				    try
				    {
				    cAdpt.resetData();
				    }
				    catch(Exception e)
				    {
				    	
				    }
				}
				
				try
				{
				NewContact.this.ccAdpt.getFilter().filter(ss.toString());
				}
				catch(Exception e)
				{
				}
				
				try
				{
				NewContact.this.cAdpt.getFilter().filter(ss.toString());	
				}
				catch(Exception e)
				{
					
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
        
        /***********************************CONTACT SEARCH*************************************/
	}
	
	
	public void callContactAdapterwithdatabase()
	{
		/*************************************contacts************************************/

		 fetchMobileandUserId();
		
		 //Log.w("DB","DB :::::::::::click"+click);
		 
		 //fetchRange(click);
		 
		 //Log.w("DB","DB :::::::::::x"+x+",y"+y);
		//set contacts in adapter from database
		  String[] values = getIndividuleDetailsFilter(UserId,x,y);
		  
		  //Log.w("DB","DB ::::::::::(counting):"+counting);
		  
		
		  
          if(counting !=0)
		  {	
        	  textpageLast.setText(""+(click+1));
        	  //Log.w("DB","DB :::::ENTER:::::(counting):"+counting);
        	  
		  ArrayList<String> contactlistData = new ArrayList<String>(Arrays.asList(values));
		  setdbdataInAdapterInd=new ArrayList<contactmodelIndividule>();
		  Iterator<String> itre = contactlistData.iterator();
		
		  
		  while(itre.hasNext())
		  {
		   b=itre.next();
		   setdbdataInAdapterInd.add(new contactmodelIndividule(b,"","","",""));
		 // //Log.w("qqqqqqqqqqqq", "?????????????"+b);
		  }
		 
		try
		{
			//set textview to counting number
			//totalContacts.setText(""+counting);
		} 
		catch (Exception e)
		{
		e.printStackTrace();
		}
		  
		ContactShowList.setAdapter(ccAdpt=new customAdaptorContactDataBASE(this, R.layout.customlistforcontacts, setdbdataInAdapterInd));
	}
          else{
        
          }
	}
	
	// Get Contact from database
	public String[] getIndividuleDetails()
	{
		dbObject.Open();	
		Cursor c;
		
		c= dbObject.getContactALL();
		
		counting = c.getCount();
		countingClick = c.getCount();
		
		String icontact[]= new String[counting];
		
	    if(c.getCount() >= 1){
	    
	    	int i=0;
	    	while(c.moveToNext() && i<counting)
	    	{
	    	icontact[i] =c.getString(2)+","+c.getString(3);
	    	i++;
	    	}
	    }
	    
	    dbObject.close();
	   
		return icontact;
	}
	
	// Get Contact from database
		public String[] getIndividuleDetailsFilter(String userID,int startRow, int endRow)
		{
			dbObject.Open();	
			Cursor c;
			
			c= dbObject.getContactALLwithRange(userID, ""+startRow, ""+endRow);
			
			counting = c.getCount();
						
			String icontact[]= new String[counting];
			
		    if(c.getCount() >= 1){
		    
		    	int i=0;
		    	while(c.moveToNext() && i<counting)
		    	{
		    	icontact[i] =c.getString(2)+","+c.getString(3);
		    	i++;
		    	}
		    }
		    
		    dbObject.close();
		   
			return icontact;
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

	public void fetchRange(int click){
		
		x = (click*10);
		y = 10;
	}
	
	public int removelastDigit(int number){
		
		number /= 10;
		
		return number;
	}
	
	private boolean haveIndContact() 
	{
		
	    dbObject.Open();
		Cursor c;
		c= dbObject.getContactALL();
		while (c.moveToNext()== true)
		{
		return true;
		}

		dbObject.close();
		return false;
		
	}
	
	private void callWebServiceForFetchData()
	{
		fetchMobileandUserId();
		
			
		contact=ProgressDialog.show(this, null, "Loading Contacts");
		
		new webservice(null, webservice.GetAllContact.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_GET_ALL_CONTACT, new ServiceHitListener() {
			
			@Override
			public void onSuccess(Object Result, int id) 
			{
				try {
					contact.dismiss();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				FetchAllContact Model  = (FetchAllContact) Result;
				
				/*try {
					String EmergencyMessage = Model.getContactDetails().get(0).getEmergencyMessage();
					
					  try
					  {
					  Emergency.desAct.finish();
					  } 
					  catch (Exception e)
					  {
					  }
					   
					   if(!(EmergencyMessage.equalsIgnoreCase("NO")))
					   {
					    Intent rt = new Intent(NewContact.this,Emergency.class);
					    rt.putExtra("Emergency", EmergencyMessage);
					    startActivity(rt);

					  }
				} catch (Exception e) {
					e.printStackTrace();
				}*/
			
				
				List<ContactDetails> list = Model.getContactDetails();
				
				cAdpt = new ContactAdaptor(list, contactsGroupFlag);
		        ContactShowList.setAdapter(cAdpt);

		        //Log.w("TAG","VALUE::"+cAdpt.getCount());
		        
		        String valusCount = Integer.toString(cAdpt.getCount());
		        
				  try 
				  {
					//  totalContacts.setText(valusCount);
				  }
				  catch (Exception e) 
				  {
				  e.printStackTrace();
				  }
		       
		        if(haveIndContact())
		        {
				 dbObject.Open();
				 dbObject.DeleteContactDataALL();
				 dbObject.close();
		        }
		           
		           
				for(int i=0;i<Model.getContactDetails().size();i++)
				{
					String contactName   = Model.getContactDetails().get(i).getContact_name();
					String contactMobile = Model.getContactDetails().get(i).getContact_mobile();
					 // String dob           =Model.getContactDetails().get(i).getDOB();
					   // String aniver         =Model.getContactDetails().get(i).getANIVER();
					fetchMobileandUserId();
					
				    dbObject.Open();
				    dbObject.addContactAll(UserId, Mobile, contactName, contactMobile, "0","","");
					dbObject.close();
				}
				
				// use for know all data length
				getIndividuleDetails();
				
				//Log.w("DB","DB :::::::::(counting)"+counting);
				
				try
				{
					//set textview to total counting number
					totalContacts.setText(""+counting);
				} 
				catch (Exception e)
				{
				e.printStackTrace();
				}
				
				TotalPage = findPageNumber(counting);
				
				try
				{
					//set last page count
					//textpageLast.setText(""+TotalPage);
				} 
				catch (Exception e)
				{
				e.printStackTrace();
				}
				
		     }
			
			@Override
			public void onError(String Error, int id) 
			{
				try
				{
				contact.dismiss();
				}
				catch (Exception e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Toast.makeText(gContext	, Error, Toast.LENGTH_SHORT).show();	
				
			}
		    });
	}
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
	try 
	{
		checkStatus= false;
		checkAll= false;
		
		dbObject.Open();
		dbObject.deleteselectedReceipent();
	
		dbObject.close();
	} 
	catch (Exception e) 
	{
	e.printStackTrace();
	}
	
	finish();
	}
	
	public int findPageNumber(int totalContact){
		int page = 0;
		
		if((totalContact%10) == 0){
			
			page = totalContact/10;
		}
		
		if((totalContact%10) > 0){
			
			page = totalContact/10;
			page = page+1;
		}
		
		
		return page;
	}
	
	public void findrangepaging(int n){
		
		x = ((n*10)-10);
		y = 10;
		
	}
	

	public void callContactAdapterbyclickpage(int data)
	{
		/*************************************contacts************************************/

		 fetchMobileandUserId();
				 
	     findrangepaging(data);
		 
		 //Log.w("DB","DB :::::::::::x:"+x+",y:"+y+",data:"+data);
		//set contacts in adapter from database
		  String[] values = getIndividuleDetailsFilter(UserId,x,y);
          if(counting !=0)
		  {	
		  
		  ArrayList<String> contactlistData = new ArrayList<String>(Arrays.asList(values));
		  setdbdataInAdapterInd=new ArrayList<contactmodelIndividule>();
		  Iterator<String> itre = contactlistData.iterator();
		
		  
		  while(itre.hasNext())
		  {
		   b=itre.next();
		   setdbdataInAdapterInd.add(new contactmodelIndividule(b,"","","",""));
		 // //Log.w("qqqqqqqqqqqq", "?????????????"+b);
		  }
		 
		try
		{
			//set textview to counting number
			//totalContacts.setText(""+counting);
		} 
		catch (Exception e)
		{
		e.printStackTrace();
		}
		  
		ContactShowList.setAdapter(ccAdpt=new customAdaptorContactDataBASE(this, R.layout.customlistforcontacts, setdbdataInAdapterInd));
	}
	}
}
