package sms19.listview.newproject;

import java.util.ArrayList;

import sms19.inapp.msg.asynctask.ForgotPasswordAsyncTask;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Countrymodel;
import sms19.listview.newproject.model.ForgotPasswordModel;
import sms19.listview.validation.Validation;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.kitever.android.R;

public class ForgotPassword extends ActionBarActivity {
	Button forgotpswdbtn;
	EditText mob;
	Context con;
	ProgressDialog p;
	
	private Spinner spinner;
	private ArrayList<Countrymodel> countryArrayList=null;
	private sms19.inapp.msg.adapter.CountryListAdapter adapter=null;
	private int defaultIndex=0;
	private String countryCodeString="";
	private TextView name_countryTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgotpassword);

		forgotpswdbtn = (Button) findViewById(R.id.ForgotPassword);
		mob = (EditText) findViewById(R.id.Mobile);
		/*************************** INTERNET ********************************/
		webservice._context = this;
		con = this;
		/*************************** INTERNET ********************************/
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
		bar.setTitle(Html
				.fromHtml("<font color='#ffffff'>Forgot Password </font>"));
		// bar.setHomeAsUpIndicator(R.drawable.arrow_new);
		bar.setDisplayHomeAsUpEnabled(true);
		try {

			Bundle b = getIntent().getExtras();
			String value1 = b.getString("UserName");

			//Log.w("TAG", "VALUE/value1:" + value1);
			mob.setText(value1);

		} catch (Exception er) {

		}
		
		name_countryTextView=(TextView)findViewById(R.id.name_country);
		spinner=(Spinner)findViewById(R.id.country_code);
		
		
	final String iso_code=com.kitever.utils.Utils.getIsoCode(ForgotPassword.this);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				countryArrayList=Utils.getCountryList(ForgotPassword.this);
			
				if(countryArrayList!=null){
					if(iso_code!=null){
						if(!iso_code.equals("")){
							final int size=countryArrayList.size();
							for(int i=0;i<size;i++){
								if(countryArrayList.get(i).getCountryISOCode().equals(iso_code)){
									defaultIndex=i;
									break;
								}
							}
						}
					}
				}
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter =new sms19.inapp.msg.adapter.CountryListAdapter(ForgotPassword.this,countryArrayList);
						spinner.setAdapter(adapter);
						spinner.setSelection(defaultIndex);
					}
				});
				
			}
		}).start();

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            	countryCodeString=countryArrayList.get(arg2).getCountrycode().trim();
            	name_countryTextView.setText(countryCodeString);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

		forgotpswdbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(countryCodeString!=null){
					if(countryCodeString.equals("")){
						Toast.makeText(getApplicationContext(), "Please select your country code",Toast.LENGTH_LONG).show();
						return;
					}
				}else{
					Toast.makeText(getApplicationContext(), "Please select your country code",Toast.LENGTH_LONG).show();
					return;
				}

				final String mobileStr = countryCodeString+mob.getText().toString().trim();

				if (Validation.hasText(mob)) {

					p = ProgressDialog.show(con, null, "Please wait...");

					sms19.inapp.msg.asynctask.ForgotPasswordAsyncTask asyncTask = new ForgotPasswordAsyncTask(
							ForgotPassword.this, mobileStr);
					asyncTask.execute();

				}

			}
		});

	}

	public void onSuccess(Object Result) {
		// TODO Auto-generated method stub

		try {
			p.dismiss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ForgotPasswordModel model = (ForgotPasswordModel) Result;

		try {
			String EmergencyMessage = model.getForgotPassword().get(0)
					.getEmergencyMessage();

			try {
				Emergency.desAct.finish();
			} catch (Exception e) {
			}

			if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
				Intent rt = new Intent(ForgotPassword.this, Emergency.class);
				rt.putExtra("Emergency", EmergencyMessage);
				startActivity(rt);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (model.getForgotPassword().get(0).getError() != null) {
			Toast.makeText(getApplicationContext(),
					"" + model.getForgotPassword().get(0).getError(),
					Toast.LENGTH_SHORT).show();
		} else {
			// Toast.makeText(getApplicationContext(),
			// "Please Check Password has been sent to your mobile",
			// Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(),
					"" + model.getForgotPassword().get(0).getMsg(),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onError(String Error) {

		try {
			p.dismiss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// /MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, menu);
		return false;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.howtouse:
			Intent i = new Intent(ForgotPassword.this, HowToUse.class);
			startActivity(i);
			break;

		case R.id.pp:
			Intent i1 = new Intent(ForgotPassword.this, TermsAndCondition.class);
			startActivity(i1);
			break;
		case android.R.id.home:
			onBackPressed();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
