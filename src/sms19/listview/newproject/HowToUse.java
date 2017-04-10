package sms19.listview.newproject;

import sms19.listview.database.DataBaseDetails;
import com.kitever.android.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

public class HowToUse extends ActionBarActivity
{
	DataBaseDetails dbObject=new DataBaseDetails(this);
	TextView tve;
	String Mobile,UserId;
	public static Activity howtouse;
@Override
protected void onCreate(Bundle savedInstanceState)
{
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.howtouse);
	tve=(TextView)findViewById(R.id.textView4);
	
	ActionBar bar=getSupportActionBar();
	bar.setDisplayHomeAsUpEnabled(true);
	bar.setTitle((Html.fromHtml("<font color='#ffffff'>How To Use</font>")));
	bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
	howtouse=this;
	tve.setOnClickListener(new OnClickListener() 
	{
		
		@Override
		public void onClick(View v) 
		{
		
			   String recipient="info@sms19.in";
			   String subject="SMS19 suggestion";
			   Intent i = new Intent(Intent.ACTION_SEND);
			   i.setType("message/rfc822");
			  // i.putExtra(Intent.EXTRA_EMAIL,recipient);
			   i.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@sms19.in"});	
			   i.putExtra(Intent.EXTRA_SUBJECT, subject);
		 startActivity(Intent.createChooser(i,"Send mail..."));
		}
		
	});
	}


private void callLogoutMethod()
{
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
			
		    Intent i = new Intent(HowToUse.this,SMS19.class);
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
	   
	  				   
	   while(c.moveToNext())
	   {
		   
		    Mobile = c.getString(1);
		    UserId = c.getString(3);
		   
	   }
			   
	   dbObject.close();
}
}
