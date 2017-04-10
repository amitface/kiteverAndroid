package sms19.listview.newproject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sms19.listview.database.DataBaseDetails;
import sms19.listview.webservice.webservice;
import com.kitever.android.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SmsReports extends ActionBarActivity implements OnClickListener
{

	ListView listSMSREports;
	TextView textFromDateSMS,textTODateSMS;

	ProgressDialog p;
	
	String Mobile= "";
	String UserId= "";
	
	Button today;
	public static Activity SMSREPORT;
	
	DataBaseDetails dbObject= new DataBaseDetails(this);
	Button one,two,three,four,five,six;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smsreports);
	 
	    today=(Button)findViewById(R.id.todayrept);
	    one=(Button)findViewById(R.id.oneday);
	    two=(Button)findViewById(R.id.twoday);
	    three=(Button)findViewById(R.id.threeday);
	    four=(Button)findViewById(R.id.fourday);
	    five =(Button)findViewById(R.id.fiveday);
	    six=(Button)findViewById(R.id.sixday);
	    
		/***************************INTERNET********************************/
		webservice._context= this;
		SMSREPORT = this;
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
		bar.setTitle(Html.fromHtml("<font color='#ffffff'>Sms Reports</font>"));


		/***************************INTERNET********************************/
		
		today.setOnClickListener(this);
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		five.setOnClickListener(this);
		six.setOnClickListener(this);
		
		
		try {
			
		    //Log.w("TAG","Values"+getTodaysDate());
		   
		    today.setText("\n"+getPriviousdate(0));	
		   		   
		    
		  //  if((getTodaysDate().substring(0,getTodaysDate().indexOf("/")).equals("1"))||(getTodaysDate().substring(0,getTodaysDate().indexOf("/")).equals("2"))||(getTodaysDate().substring(0,getTodaysDate().indexOf("/")).equals("3"))||(getTodaysDate().substring(0,getTodaysDate().indexOf("/")).equals("4"))||(getTodaysDate().substring(0,getTodaysDate().indexOf("/")).equals("5"))||(getTodaysDate().substring(0,getTodaysDate().indexOf("/")).equals("6")))
		  //  {
		    one.setText("\n"+ getPriviousdate(-1));
		    two.setText("\n"+ getPriviousdate(-2));
		    three.setText("\n"+ getPriviousdate(-3));	
		    four.setText("\n"+ getPriviousdate(-4));	
		    five.setText("\n"+ getPriviousdate(-5));
		    six.setText("\n"+ getPriviousdate(-6));
		  //  }
		  /*  else
		    {
		    one.setText(getOneDaybefore());	
		    two.setText(getTwoDaybefore());	
		 	three.setText(getThreeDaybefore());	
			four.setText(getFourDaybefore());	
			five.setText(getFiveDaybefore());
			six.setText(getSixDaybefore());
		    }*/
		    
		  
			    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	switch(v.getId())	
	{
	case R.id.todayrept:{
						
		Intent i=new Intent(SmsReports.this,SecondSmsReport.class);
		i.putExtra("dayreport","today");//day report
		i.putExtra("DATE_SMS", today.getText().toString());
		startActivity(i);
	}
	break;
	case R.id.oneday:{
		Intent i1=new Intent(SmsReports.this,SecondSmsReport.class);
		i1.putExtra("dayreport","one");
		i1.putExtra("DATE_SMS", one.getText().toString());
		startActivity(i1);
	}
		break;
	case R.id.twoday:{
		Intent i2=new Intent(SmsReports.this,SecondSmsReport.class);
		i2.putExtra("dayreport","two");
		i2.putExtra("DATE_SMS", two.getText().toString());
		startActivity(i2);
	}
		break;
	case R.id.threeday:{
		Intent i3=new Intent(SmsReports.this,SecondSmsReport.class);
		i3.putExtra("dayreport","three");
		i3.putExtra("DATE_SMS", three.getText().toString());
		startActivity(i3);
	}
		break;
	case R.id.fourday:{
		Intent i4=new Intent(SmsReports.this,SecondSmsReport.class);
		i4.putExtra("dayreport","four");
		i4.putExtra("DATE_SMS", four.getText().toString());
		startActivity(i4);
	}
		break;
	case R.id.fiveday:{
		Intent i5=new Intent(SmsReports.this,SecondSmsReport.class);
		i5.putExtra("dayreport","five");
		i5.putExtra("DATE_SMS", five.getText().toString());
		startActivity(i5);
	}
		break;
	case R.id.sixday:{
		Intent i6=new Intent(SmsReports.this,SecondSmsReport.class);
		i6.putExtra("dayreport","six");
		i6.putExtra("DATE_SMS", six.getText().toString());
		startActivity(i6);
	}
		break;
	default:
		break;
	}
	}
	
	//-----------------------------Date format DD/MM/YYYY---------------------
	private String getTodaysDate() {
	 
	    final Calendar c = Calendar.getInstance();
	 
	    return(new StringBuilder()
	            .append(c.get(Calendar.DAY_OF_MONTH)).append("/")
	            .append(c.get(Calendar.MONTH) + 1).append("/")
	            .append(c.get(Calendar.YEAR)).append(" ")).toString();
	}
	
	public String getPriviousdate(int finddate){
		String date = "";
		try {
		
			  //Log.w("TAG:DATE_2:","*******************2******************");
			    
			    Calendar cal = Calendar.getInstance();
			    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			  
			    cal.add(Calendar.DAY_OF_YEAR, finddate);
			    Date newDate = cal.getTime();
			    
			    date = dateFormat.format(newDate);
			    
			    //Log.w("TAG:DATE_3:",""+date);
				
				/*************************************/
				
			    return date;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 
			e.printStackTrace();
		}
		 return date;
	
		}
	


	private void callLogoutMethod() {
		// TODO Auto-generated method stub
			
		new AlertDialog.Builder(this)
		.setCancelable(false)
		.setMessage("Are you Sure you want to Exit? All your chat data will be deleted.")
		.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
											
	            // delete all database				
	            DatabaseCleanState();
			
			    Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();	
				
			    Intent i = new Intent(SmsReports.this,SMS19.class);
			    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	        	startActivity(i);
	        	finish();
			   
			 
			}
			public SQLiteDatabase getDBObject()
		    {
		    return dbObject.db;
		    }

			private void DatabaseCleanState() {
				// TODO Auto-generated method stub
				dbObject.Open();
				dbObject.onUpgrade(getDBObject(), 1, 1);
				dbObject.close();
			}

			})
		.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}})
		.show();
	
	}
	public void fetchMobileandUserId() 
	{
		dbObject.Open();
		  
		   Cursor c;
		   
		   c= dbObject.getLoginDetails();
		   
		  				   
		   while(c.moveToNext()){
			   
			   Mobile = c.getString(1);
			   UserId = c.getString(3);
			   
		   }
				   
		   dbObject.close();
	}
	
}
