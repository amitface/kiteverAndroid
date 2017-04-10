package sms19.listview.newproject;

import java.util.ArrayList;
import java.util.List;

import sms19.listview.adapter.CustomAdapterSchedule;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.SchduleListDetail;
import sms19.listview.newproject.model.ScheduleListDetails;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.sendsms.SendSmsActivity;
import com.kitever.sendsms.SendSmsScreen;

public class ScheduleList extends ActionBarActivity {
	ListView lv;
	// Button scheduleokbtn;
	String userid = "";

	public static Context cd;

	// Define List with Model by which we can get value from service
	List<ScheduleListDetails> ad;
	DataBaseDetails db = new DataBaseDetails(this);
	TextView tvclickschedule, noMsg;
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedulelist);

		lv = (ListView) findViewById(R.id.schedulelist);
		// scheduleokbtn = (Button) findViewById(R.id.scheduleokbtn);
		tvclickschedule = (TextView) findViewById(R.id.clickto);
		noMsg = (TextView) findViewById(R.id.no_Message);
		progressBar = (ProgressBar) findViewById(R.id.progressBar_id);
		/*************************** INTERNET ********************************/
		webservice._context = this;
		// setTitle("ScheduleList");
		cd = this;
		/*************************** INTERNET ********************************/

		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
		bar.setTitle(Html
				.fromHtml("<font color='#ffffff'>Scheduled SMS</font>"));
		// bar.setHomeAsUpIndicator(R.drawable.arrow_new);

		tvclickschedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(ScheduleList.this, SendSmsActivity.class);
				startActivity(i);

			}
		});

		// create object of list to add value in default mode
		// scheduleokbtn.setOnClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// callfxnatOnbackpress();
		// }
		// });
		//

	}

	private void callScheduledWebService() {
		try {
			fetchMobileandUserId();
			new webservice(null, webservice.SheduledSmsList.geturl(userid, "",
					""), webservice.TYPE_GET, webservice.TYPE_GET_ALL_SCHEDULE,
					new ServiceHitListener() {

						@Override
						public void onSuccess(Object Result, int id) {
							noMsg.setVisibility(View.GONE);
							ad = new ArrayList<ScheduleListDetails>();
							progressBar.setVisibility(View.GONE);

							SchduleListDetail mod = (SchduleListDetail) Result;
//							Log.w("model size", "@@@@@@@"
//									+ mod.getSheduledSmsList().size());

							try {
								if (mod.getSheduledSmsList().size() > 0) {
									// scheduleokbtn.setVisibility(View.VISIBLE);
								} else {
									noMsg.setVisibility(View.VISIBLE);
									// Toast.makeText(getApplicationContext(),
									// "No Message Scheduled",
									// Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							try {
								for (int i = 0; i < mod.getSheduledSmsList()
										.size(); i++) {

									String senderid = mod.getSheduledSmsList()
											.get(i).getScheduledDate();
									String message = mod.getSheduledSmsList()
											.get(i).getMessage();
									String totalcount = mod
											.getSheduledSmsList().get(i)
											.getTotalMessages();
									// Log.w("TAG","VALUE:::"+"senderid"+senderid+"::message"+message);

									ad.add(new ScheduleListDetails(senderid,
											message, totalcount));
								}

								lv.setAdapter(new CustomAdapterSchedule(cd,
										R.layout.schedulecustomlist, ad));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								String EmergencyMessage = mod
										.getSheduledSmsList().get(0)
										.getEmergencyMessage();
								try {

									Emergency.desAct.finish();
								} catch (Exception e) {
								}

								if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
									Intent rt = new Intent(ScheduleList.this,
											Emergency.class);
									rt.putExtra("Emergency", EmergencyMessage);
									startActivity(rt);

								}
							} catch (Exception e) {

							}

						}

						@Override
						public void onError(String Error, int id) {
							noMsg.setVisibility(View.GONE);
							progressBar.setVisibility(View.GONE);
							Toast.makeText(getApplicationContext(), Error,
									Toast.LENGTH_SHORT).show();

						}
					});

		} catch (Exception e) {

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		callScheduledWebService();
	}

	@Override
	public void onBackPressed() {
		// callfxnatOnbackpress();
		super.onBackPressed();
	}

	public void fetchMobileandUserId() {
		db.Open();

		Cursor c;

		c = db.getLoginDetails();

		while (c.moveToNext()) {

			userid = c.getString(3);

		}

		db.close();
	}


}
