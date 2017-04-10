package sms19.listview.newproject;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.kitever.android.R;

public class Emergency extends ActionBarActivity {
	
	public static Activity desAct ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency);
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff0000")));
		bar.setTitle(Html.fromHtml("<font color='#ffffff'>Emergency </font>"));
	
		desAct =  this;
		
		String data;
		try {
			Intent i = getIntent();
			data = i.getStringExtra("Emergency");
			
			if(data.equalsIgnoreCase("NO")){
				data = "please update Kitever from play store.";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			data = "please update Kitever from play store.";
		}
		
		new AlertDialog.Builder(this)
		.setCancelable(false)
		.setTitle("Alert!")
		.setMessage(data)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				desAct.finish();
			}
		})
		.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				desAct.finish();
			}
		} )
		.show();
		

	}

}
