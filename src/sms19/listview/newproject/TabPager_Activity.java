package sms19.listview.newproject;

import com.kitever.android.R;
import com.kitever.sendsms.SendSmsActivity;
import com.kitever.utils.Utils;


import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")

public class TabPager_Activity  extends TabActivity {

	 TabHost TabHostWindow;
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.tab_host_layout);

	 /*final ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(Utils.setActionBarBackground());
		bar.setTitle(Utils.setActionBarTextAndColor("Send Sms"));
		//bar.setDisplayHomeAsUpEnabled(true);
*/	 //Assign id to Tabhost.
	 TabHostWindow = (TabHost)findViewById(android.R.id.tabhost);
	 TabHostWindow.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#4E4E9C"));
	 //Creating tab menu.
	 
	 TabSpec TabMenu1 = TabHostWindow.newTabSpec("SMS");
	 TabSpec TabMenu2 = TabHostWindow.newTabSpec("EMAILE");
	 /*TabSpec TabMenu3 = TabHostWindow.newTabSpec("Third Tab");*/
	 
	 //Setting up tab 1 name.
	 TabMenu1.setIndicator("SMS");
	 
	 //Set tab 1 activity to tab 1 menu.
	 TabMenu1.setContent(new Intent(this,SendSmsActivity.class));
	 
	 //Setting up tab 2 name.
	 TabMenu2.setIndicator("EMAILE");
	 //Set tab 3 activity to tab 1 menu.
	 
	 TabMenu2.setContent(new Intent(this,EmailSendActivity.class));
	 
	/* //Setting up tab 2 name.
	 TabMenu3.setIndicator("Tab3");
	 //Set tab 3 activity to tab 3 menu.
	 TabMenu3.setContent(new Intent(this,TabActivity_3.class));*/
	 
	 //Adding tab1, tab2, tab3 to tabhost view.
	 
	 TabHostWindow.addTab(TabMenu1);
	 TabHostWindow.addTab(TabMenu2);
	// TabHostWindow.addTab(TabMenu3);
	 
	 }
	}