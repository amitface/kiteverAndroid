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
import android.widget.TextView;
import android.widget.Toast;

public class TermsAndCondition extends ActionBarActivity 
{
	public static Activity tc;
	String userid;

	public static final String TOU = "<strong>Terms & Conditions: </strong>&nbsp;<br><br> This privacy statement discloses the privacy practices for Service Provider.<br><p>We want to build your trust and confidence in the Internet by promoting the use of fair information practices. We are committed to your privacy and are disclosing our information practices in this company statement.</p><p>This Privacy Policy lets you know how your personal information is processed and used. We promise that we will take steps to use your personal information only in ways that are compatible with this Privacy Policy. The following policies are only in effect for the Web pages, newsletters, discussion lists and opt-in announcement lists owned and operated by Service Provider.</p><strong>Information Request</strong>&nbsp;<br><p>We may also request your e-mail address or mailing address for the purposes of conducting a survey or to provide additional services (for example, subscriptions to e-mail newsletters, announcement lists or information about seminars) Whenever we request the identity of a visitor, we will clearly indicate the purpose of the inquiry before the information is requested. We maintain a strict �No-Spam� policy, that means that we do not intend to sell, rent, or otherwise give your e-mail address to a third-party, without your consent.</p><p>In addition, Service Provider will not send you email that you have not agreed to receive. We may from time to time send e-mail announcing new Service Provider products and services. If you choose to supply your postal address in an online form, you may receive mailings from other reputable, third-party companies.</p><p>If you choose to purchase something online, we need to know your name, e-mail address, mailing address, credit card number, and expiration date. This allows us to process and fulfill your order and to notify you of your order status. This information may also be used by us to notify you of related product and services, but will not be shared or sold to third parties for any purpose.</p><strong>Disclose of Information to Outside Third Parties</strong>&nbsp;<br><p>Service Provider will disclose personal information when required by law or in the good-faith belief that such action is necessary to:1. Conform to the edicts of the law or comply with a legal process served on Service Provider2. Protect and defend the rights or property of Service Provider, or visitors to Service Provider3. Identify persons who may be violating the law, the legal notice, or the rights of third parties,4. Cooperate with the investigations of purported unlawful activities. Service Provider uses reasonable precautions to keep the information disclosed to us secure. However, we are not responsible for any breach of security or for any actions of any third parties which receive the information. Service Provider also links to a wide variety of other sites and contains advertisements of third parties. We are not responsible for their privacy policies or how they treat information about their users.</p><strong>Information Collection and Use</strong>&nbsp;<br><p>Service Provider is the sole owner of the information collected on this site. We will not sell, share, or rent this information to others in ways different from what is disclosed in this statement. Service Provider presently collects information from its users at different points on its website as follows.</p><strong>Sharing Information</strong>&nbsp;<br><p> We will share aggregated demographic information with our partners and advertisers if requested. This is not linked to any personal information that can identify any individual person or computer.</p><p>We may partner with another party to provide specific services. When the user signs up for these services, we will share names, or other contact information that is necessary for the third party to provide these services. If you do not want your personal information shared, please email support@Service Provider with your request.</p><p>The parties listed above are not allowed to use personally identifiable information except for the purpose of providing these services.</p><strong>Your Consent To This Agreement</strong>&nbsp;<br><p> By using our Web site, you consent to the collection and use of information by Service Provider as specified above. If we decide to change our privacy policy, we will post those changes on this page so that you are always aware of what information we collect, how we use it, and under what circumstances we disclose it. Please note that messages will be charged on submission basis.SMS on DND numbers will be counted unless otherwise specified.<br><p><strong>WARNING :  Please register with NDNC if not already done. All promotional messages need to be scrubbed against NDNC registry. Please do not send promotional SMS through the service route else your account will be blocked due to violation of our terms of service. Deactivation of account will be done & no refund will be done and no refund will be provided.</strong></p><p>Fine & Penalties: As per TRAI rules, first violation from your account shall invoke a penalty of Rs. 1000/- per number payable to TRAI.</p><p>We hope you understand the implications of violating the NDNC registry rules and cooperate with us to avoid any complaints.</p><p>If you have any questions, please feel free to contact our Support team</p><p>Assuring you of our best services at all times.</p>";
	DataBaseDetails db=new DataBaseDetails(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_and_condition);
		tc=this;
		((TextView) findViewById(R.id.texttermStatic)).setText(Html.fromHtml(TOU));
	
		 //changes regards actionbar color
		 ActionBar bar = getSupportActionBar();
		 bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
		 bar.setTitle(Html.fromHtml("<font color='#ffffff'>Terms and Privacy</font>"));
	
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) @Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();


		
		if(id == R.id.editProfile)
		{
			Intent i = new Intent(TermsAndCondition.this,EditProfile.class);
			startActivity(i);
			finish();
			return true;	
		}
		

		
		return super.onOptionsItemSelected(item);
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
				
			    Intent i = new Intent(TermsAndCondition.this,SMS19.class);
			    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	        	startActivity(i);
	        	finish();
			   
			 
			}
			public SQLiteDatabase getdb()
		    {
		    return db.db;
		    }

			private void DatabaseCleanState() {
				// TODO Auto-generated method stub
				db.Open();
				db.onUpgrade(getdb(), 1, 1);
				db.close();
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
		db.Open();
		  
		   Cursor c;
		   
		   c= db.getLoginDetails();
		   
		  				   
		   while(c.moveToNext()){
			   
			  
			   userid   = c.getString(3);
			  
			   
		   }
				   
		   db.close();
	}
	@Override
	public void onBackPressed() {
	  finish();
	 
	}
}
