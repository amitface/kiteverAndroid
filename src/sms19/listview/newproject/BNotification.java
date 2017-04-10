package sms19.listview.newproject;

import java.util.ArrayList;
import java.util.List;

import sms19.listview.adapter.NotificationAdapter;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.NotiModel;
import sms19.listview.newproject.model.NotiModel.bNoti;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.kitever.android.R;

import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class BNotification extends AppCompatActivity {

	List<bNoti> broadcastall;
	DataBaseDetails dbObj;
	String Mobile = "", UserId = "";
	ListView lv_broadcast_all;
	String Mess = "";
	String tolink = "";
	String a = "", b = "", c1 = "", d = "", e = "";
	String asendlink = "";
	String title, message, landingpage, time;
	ArrayAdapter<bNoti> broadcastal_adapt;
	TextView noNotification;
	public static Activity HbNoti;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_all_broadcast);
		lv_broadcast_all = (ListView) findViewById(R.id.brodcstallnofn);
		noNotification = (TextView) findViewById(R.id.no_notification);
		dbObj = new DataBaseDetails(this);
		HbNoti = this;
		actionBarSettingWithBack(this,getSupportActionBar(),"Notification");
		int anotify = Home.notifyonlyone = 0;
		// Log.w("One Notify","First Notify"+anotify);
		broadcastall = new ArrayList<bNoti>();
		dbObj.Open();
		Cursor c;
		c = dbObj.getnotify();
		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				a = c.getString(1);
				b = c.getString(2);
				c1 = c.getString(3);
				d = c.getString(4);
				e = c.getString(5);

				broadcastall.add(new bNoti(a, b, c1, d,e));
			}

			try {

				lv_broadcast_all
						.setAdapter(broadcastal_adapt = new NotificationAdapter(
								this, R.layout.activity_notification,broadcastall));
				lv_broadcast_all.setEmptyView(noNotification);
				broadcastal_adapt.notifyDataSetChanged();
			} catch (Exception e) {
			}
		}

		dbObj.close();
		fetchMobileandUserId();

	/*	new webservice(null, webservice.bNotification.geturl(UserId),
				webservice.TYPE_GET, webservice.TYPE_NOTIFICATION,
				new ServiceHitListener() {

					@Override
					public void onSuccess(Object Result, int id) {

						NotiModel notim = (NotiModel) Result;

						*//*
						 * try { String EmergencyMessage =
						 * notim.getGetBroadcastPagetype
						 * ().get(0).getEmergencyMessage();
						 * 
						 * try { Emergency.desAct.finish(); } catch (Exception
						 * e) { }
						 * 
						 * if(!(EmergencyMessage.equalsIgnoreCase("NO"))) {
						 * Intent rt = new
						 * Intent(BNotification.this,Emergency.class);
						 * rt.putExtra("Emergency", EmergencyMessage);
						 * startActivity(rt);
						 * 
						 * } } catch (Exception e1) { e1.printStackTrace(); }
						 *//*
						fetchMobileandUserId();

						dbObj.Open();
						for (int i = 0; i < notim.getGetBroadcastPagetype()
								.size(); i++) {
							title = notim.getGetBroadcastPagetype().get(i)
									.getBroadcastTitle();
							message = notim.getGetBroadcastPagetype().get(i)
									.getMessage();
							landingpage = notim.getGetBroadcastPagetype()
									.get(i).getLandingPage();
							time = notim.getGetBroadcastPagetype().get(i)
									.getMessageInsertDateTime();
							broadcastall.add(new bNoti(title, message,
									landingpage, time));
							// asendlink=message.substring(message.indexOf("~")+1,message.indexOf("*"));
							dbObj.addnotifiaction(UserId, title, message,
									landingpage, time, asendlink);

						}
						dbObj.close();

						try {
							Mess = message.substring(message.indexOf("~") + 1,
									message.indexOf("*"));
						} catch (Exception e) {
							Mess = "";
						}

						// Log.w("!!!!!!!!!!!!!!!!!!!!!!!!","qqqqqqqq"+Mess+","+"@@@"+asendlink);
						// Log.w("!!!!!!!!!!!!!!!!!!!!!!!!","qqqqqqqq"+message);

						lv_broadcast_all
								.setAdapter(broadcastal_adapt = new NotificationAdapter(
										getApplicationContext(),
										R.layout.activity_notification,
										broadcastall, Mess));
						broadcastal_adapt.notifyDataSetChanged();

					}

					@Override
					public void onError(String Error, int id) {
					}
				});*/

	}

	public void fetchMobileandUserId() {
		dbObj.Open();

		Cursor c;
		c = dbObj.getLoginDetails();

		while (c.moveToNext()) {
			Mobile = c.getString(1).trim();
			UserId = c.getString(3).trim();

		}

		dbObj.close();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
