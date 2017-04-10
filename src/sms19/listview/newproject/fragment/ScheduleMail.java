package sms19.listview.newproject.fragment;

import java.util.ArrayList;

import sms19.listview.adapter.MailAdapterSchedule;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.Emergency;
import sms19.listview.newproject.model.ScheduledMailModel;
import sms19.listview.newproject.model.SheduledMailList;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.sendsms.SendSmsMail;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class ScheduleMail extends Fragment implements ReportSmsMailInterface {

	private ListView lv;

	private String userid = "";

	public static Context cd;

	// Define List with Model by which we can get value from service
	private ArrayList<SheduledMailList> ad;
	private DataBaseDetails db = null;
	private TextView tvclickschedule, noMsg,textView1,textView2,textView3;
	private ProgressBar progressBar;

	public static Fragment newIntance() {
		// TODO Auto-generated method stub
		ScheduleMail fragment = new ScheduleMail();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View convertView = inflater.inflate(R.layout.fragment_schedule_mail,
				container, false);
		db = new DataBaseDetails(getActivity());

		lv = (ListView) convertView.findViewById(R.id.schedulelist);

		tvclickschedule = (TextView) convertView.findViewById(R.id.clickto);

		textView1= (TextView) convertView.findViewById(R.id.textView1);
		textView2= (TextView) convertView.findViewById(R.id.textView2);
		textView3= (TextView) convertView.findViewById(R.id.textView3);

		setRobotoThinFont(tvclickschedule,getActivity());
		setRobotoThinFont(textView1,getActivity());
		setRobotoThinFont(textView2,getActivity());
		setRobotoThinFont(textView3,getActivity());

		tvclickschedule.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
		tvclickschedule.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));

		textView1.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		textView2.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		textView3.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

		noMsg = (TextView) convertView.findViewById(R.id.no_Message);
		progressBar = (ProgressBar) convertView
				.findViewById(R.id.progressBar_id);

		/*************************** INTERNET ********************************/
		webservice._context = getContext();
		// setTitle("ScheduleList");
		cd = getContext();
		/*************************** INTERNET ********************************/

		tvclickschedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(getActivity(), SendSmsMail.class);
				i.putExtra("tab", 1);
				startActivity(i);

			}
		});

		return convertView;
	}

	private void callScheduledWebService() {
		try {
			fetchMobileandUserId();
			new webservice(null, webservice.SheduledMailList.geturl(userid, "",
					""), webservice.TYPE_GET,
					webservice.TYPE_GET_MAIL_SCHEDULE,
					new ServiceHitListener() {

						@Override
						public void onSuccess(Object Result, int id) {
							if (id == webservice.TYPE_GET_MAIL_SCHEDULE) {
								noMsg.setVisibility(View.GONE);
								// ad = new ArrayList<SheduledMailList>();
								progressBar.setVisibility(View.GONE);

								ScheduledMailModel mod = (ScheduledMailModel) Result;
								Log.w("model size", "@@@@@@@"
										+ mod.getSheduledMailList().toString());

								try {
									if (mod.getSheduledMailList().size() > 0) {
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

									if ( mod != null
											&& mod.getSheduledMailList() != null) {
										ad = mod.getSheduledMailList();
										lv.setAdapter(new MailAdapterSchedule(
												cd,
												R.layout.schedulecustomlist, ad));
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								/*try {
									String EmergencyMessage = mod
											.getSheduledMailList().get(0)
											.getMessage();
									try {

										Emergency.desAct.finish();
									} catch (Exception e) {
									}

									if (!(EmergencyMessage
											.equalsIgnoreCase("NO"))) {
										Intent rt = new Intent(getActivity(),
												Emergency.class);
										rt.putExtra("Emergency",
												EmergencyMessage);
										startActivity(rt);
									}
								} catch (Exception e) {

								}*/
							} else {
								Toast.makeText(getActivity(), "errorMAIL",
										Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onError(String Error, int id) {
							noMsg.setVisibility(View.GONE);
							progressBar.setVisibility(View.GONE);
							Toast.makeText(getActivity(), "Mail" + Error,
									Toast.LENGTH_SHORT).show();
						}
					});

		} catch (Exception e) {

		}
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// callScheduledWebService();
	}

	@Override
	public void changeServiceRequest() {
		// TODO Auto-generated method stub
		callScheduledWebService();
	}
}
