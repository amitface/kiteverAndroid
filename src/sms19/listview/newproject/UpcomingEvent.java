package sms19.listview.newproject;


import com.kitever.android.R;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;

public class UpcomingEvent extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upcoming_event);
		
		 //changes regards actionbar color
		 ActionBar bar = getSupportActionBar();
		 bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
		 bar.setTitle(Html.fromHtml("<font color='#ffffff'>Upcoming Event</font>"));
	}
	
	@Override
	public void onBackPressed() 
	{
	  finish();
	}
}
