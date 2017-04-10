package sms19.listview.adapter;

import java.util.List;

import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.ScheduleListDelete;
import sms19.listview.newproject.model.ScheduleListDetails;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

public class CustomAdapterSchedule extends ArrayAdapter<ScheduleListDetails> {
	private List<ScheduleListDetails> LISTDATA;
	private Context context;
	private int resource;
	String Mobile = "", date, time;
	String message;
	String Password = "", UserLogin = "";

	DataBaseDetails db = new DataBaseDetails(getContext());

	public CustomAdapterSchedule(Context context, int resource,
			List<ScheduleListDetails> objects) {
		super(context, resource, objects);

		this.LISTDATA = objects;
		this.context = context;
		this.resource = resource;

		// Log.w("TAG","SIZE::"+objects.size());
	}

	public int getCount() {
		try {
			return LISTDATA.size();

		} catch (Exception e) {
			return 0;
		}
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// Log.w("TAG","SIZE:inGetView:"+LISTDATA.size());

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resource, null);

		}
		TextView tv12 = (TextView) convertView.findViewById(R.id.totalrecp);
		TextView tv = (TextView) convertView.findViewById(R.id.textView122);

		TextView tv2 = (TextView) convertView.findViewById(R.id.time);
		ImageView delete = (ImageView) convertView.findViewById(R.id.dlt);
		tv12.setText(getItem(position).getTotalCount());
		message = getItem(position).getMessage();
		tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context)

						.setMessage(getItem(position).getMessage())
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								})

						.setIcon(android.R.drawable.ic_dialog_alert).show();

			}
		});
		String date_time = getItem(position).getDateTime();
		date = date_time.substring(0, 11);
		time = date_time.substring(11);
		// Log.w("time", "@@@@"+time+","+date);

		tv.setText(date + " " + time);
		// tv1.setText(time);
		if (message.length() > 10) {
			String ab;
			try {
				ab = getItem(position).getMessage().substring(0, 15)
						+ "...\nsee more";
				tv2.setText(ab);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			tv2.setText(message);
		}

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/***************************** Alert Dialog ****************************/

				new AlertDialog.Builder(getContext())
						.setCancelable(false)
						.setMessage("Are you sure to Delete Schedule Message?")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										fetchMobileandUserId();
										new webservice(
												null,
												webservice.DeleteSheduledSms
														.geturl(UserLogin,
																Password,
																date.trim(),
																time),
												webservice.TYPE_GET,
												webservice.TYPE_DELETE_SCHEDULE,
												new ServiceHitListener() {

													@Override
													public void onSuccess(
															Object Result,
															int id) {

														ScheduleListDelete model = (ScheduleListDelete) Result;

														// Log.w("TAG","SUCCESS");

														if (model
																.getDeleteSheduledSms()
																.size() > 0
																&& model.getDeleteSheduledSms()
																		.get(0)
																		.getMsg() != null) {
															if (model
																	.getDeleteSheduledSms()
																	.get(0)
																	.getMsg()
																	.contains(
																			"Sucessfully")) {
																try {
																	LISTDATA.remove(position);
																	notifyDataSetChanged();
																} catch (Exception e) {
																}

																// Log.w("TAG","SUCCESS IN FXN");
																new AlertDialog.Builder(
																		context)
																		.setCancelable(
																				false)
																		.setMessage(
																				""
																						+ model.getDeleteSheduledSms()
																								.get(0)
																								.getMsg())
																		.setPositiveButton(
																				"Ok",
																				new DialogInterface.OnClickListener() {

																					@Override
																					public void onClick(
																							DialogInterface dialog,
																							int which) {
																						dialog.cancel();

																					}
																				})
																		.show();
															} else {
																new AlertDialog.Builder(
																		context)
																		.setCancelable(
																				false)
																		.setMessage(
																				model.getDeleteSheduledSms()
																						.get(0)
																						.getMsg())
																		.setPositiveButton(
																				"Ok",
																				new DialogInterface.OnClickListener() {

																					@Override
																					public void onClick(
																							DialogInterface dialog,
																							int which) {
																						dialog.cancel();

																					}
																				})
																		.show();
															}

															// Toast.makeText(getContext(),
															// ""+model.getDeleteSheduledSms().get(0).getMsg().substring(0,model.getDeleteSheduledSms().get(0).getMsg().indexOf("\r")),
															// Toast.LENGTH_SHORT).show();
														} else {
															Toast.makeText(
																	getContext(),
																	"Please try again later",
																	Toast.LENGTH_SHORT)
																	.show();
														}
													}

													@Override
													public void onError(
															String Error, int id) {
														Toast.makeText(
																getContext(),
																"" + Error,
																Toast.LENGTH_SHORT)
																.show();
													}
												});

									}

								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								}).show();
				/***************************** Alert Dialog ****************************/

			}
		});

		return convertView;
	}

	public void fetchMobileandUserId() {
		db.Open();

		Cursor c;

		c = db.getLoginDetails();

		while (c.moveToNext()) {

			Mobile = c.getString(1);
			UserLogin = c.getString(6);
			Password = c.getString(5);

		}

		db.close();
	}

}
