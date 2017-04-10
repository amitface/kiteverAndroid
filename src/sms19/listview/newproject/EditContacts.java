package sms19.listview.newproject;

import java.util.ArrayList;
import java.util.Arrays;

import sms19.listview.adapter.CustomEditContacts;
import sms19.listview.adapter.customAdaptorContactDataBASE;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.webservice.webservice;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.kitever.android.R;
import com.kitever.sendsms.SendSmsScreen;

public class EditContacts extends ActionBarActivity
{
	ArrayList<String> mob;
	ListView lv;
	String allnumbers[],allnames[];
	DataBaseDetails db=new DataBaseDetails(this);
	
	String Mobile= "",UserId="",Password = "";

	
@Override
protected void onCreate(Bundle savedInstanceState) 
{
	Button ok;
	super.onCreate(savedInstanceState);
	setContentView(R.layout.editcontact);
	lv=(ListView)findViewById(R.id.listView1);
	ok=(Button)findViewById(R.id.OK);
	ActionBar bar = getSupportActionBar();
	bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
	bar.setTitle(Html.fromHtml("<font color='#ffffff'>Edit Contacts</font>"));

	/***************************INTERNET********************************/
	webservice._context= this;

	/***************************INTERNET********************************/
	
	ok.setOnClickListener(new OnClickListener() 
	{
		
		@Override
		public void onClick(View v)
		{
	/*try
	{
		SendSmsScreen.sendSmsFlag.finish();
	}
	catch(Exception e){
		
	}*/
		Intent i=new Intent(EditContacts.this,SendSmsScreen.class);
		//
		startActivity(i);
		finish();
		}
	});
	
	try
	{
		
		if(customAdaptorContactDataBASE.statusCheckAll){
			
			allnumbers = getIndividuleDetails();
			allnames   = getIndividuleDetailsName();
			
			fetchMobileandUserId();
			
			db.Open();
			db.deleteselectedReceipent();
			db.close();
						
			for(int id=0;id<allnumbers.length;id++){
			
			db.Open();
			db.addReceipent(UserId, allnumbers[id], allnames[id]);
			db.close();
			
			}
			customAdaptorContactDataBASE.statusCheckAll= false;
		}
		else{
			
			allnumbers =fetchRecipientList();
			allnames   =fetchRecipientListName();
		}
	
    ArrayList<String> contactGrouplist = new ArrayList<String>(Arrays.asList(allnumbers));
    ArrayList<String> contactGroupName = new ArrayList<String>(Arrays.asList(allnames));
    	  	  
	CustomEditContacts ED=new CustomEditContacts(this, R.layout.editcontactcustomlist, contactGrouplist, contactGroupName);
	if(contactGroupName.size()>0)
	{
    ok.setVisibility(View.VISIBLE);
	}
	lv.setAdapter(ED);
	}
	catch(Exception e)
	{
		
	}
}
public String[] fetchRecipientList()
{
	db.Open();
	Cursor c;
	
	c=db.getReceipent();
	int count =c.getCount();
	
	String containNumber[]=new String[count];
	
	int i=0;
	while(c.moveToNext())
	{
	containNumber[i]=c.getString(1);
	i++;
	}
	return containNumber;
	
}

public String[] fetchRecipientListName()
{
	db.Open();
	Cursor c;
	
	c=db.getReceipent();
	int count =c.getCount();
	
	String containNumber[]=new String[count];
	
	int i=0;
	while(c.moveToNext())
	{
	containNumber[i]=c.getString(2);
	i++;
	}
	return containNumber;
	
}

public String[] getIndividuleDetails()
{
	db.Open();	
	Cursor c;
	
	c= db.getContactALL();
	int count = c.getCount();
	
	String icontact[] = new String[count];
	
    int i=0;
    while(c.moveToNext() && i<count)
    	{
    	icontact [i] = c.getString(3);
    	i++;
    	
    }
    
    db.close();
   
	return icontact;
}

public String[] getIndividuleDetailsName()
{
	db.Open();	
	Cursor c;
	
	c= db.getContactALL();
	int count = c.getCount();
	
	String icontact[] = new String[count];
	
    int i=0;
    while(c.moveToNext() && i<count)
    	{
    	icontact [i] = c.getString(2);
    	i++;
    	
    }
    
    db.close();
   
	return icontact;
}

public void fetchMobileandUserId() 
{
	db.Open();
	  
	   Cursor c;
	   
	   c= db.getLoginDetails();
	   int count=c.getCount();
	   
	  if(count>=1)
	  {		
	   while(c.moveToNext()){
		   
		   Mobile   = c.getString(1);
		   UserId   = c.getString(3);
		   Password = c.getString(5);
		   
	   }
	  }	   
	  db.close();
}

}
