package sms19.listview.adapter;

import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.ScheduleListDelete;
import sms19.listview.newproject.model.SheduledMailList;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

import static sms19.inapp.msg.constant.Apiurls.KIT19_BASE_URL;

public class MailAdapterSchedule extends ArrayAdapter<SheduledMailList> {
	private ArrayList<SheduledMailList> LISTDATA;
	private Context context;
	private int resource;
	String Mobile = "", date, time;
	String message;
	String Password = "", UserLogin = "";

	DataBaseDetails db = new DataBaseDetails(getContext());

	public MailAdapterSchedule(Context context, int resource,
			ArrayList<SheduledMailList> objects) {
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
		tv12.setText(getItem(position).getRecipient());
		message = getItem(position).getTemplateName();
		tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setTitle(getItem(position).getTemplateName());
				WebView wv = new WebViewHelper(context);
				wv.clearHistory();
				wv.getSettings().setJavaScriptEnabled(true);
				wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
				String Pass = Utils.getPassword(context);
				String userId = Utils.getUserId(context);
				String tempId = getItem(position).getTemplateMailId()
						.trim();
				System.out.println("temp Id" + tempId);
				String postData = "password=" + Pass.trim();// +"&userid"+userId.trim()+"&templateid"+tempId;
				String base_url=KIT19_BASE_URL.replace("NewService.aspx?Page=","");
				wv.postUrl(base_url+"ViewMailTemplate.aspx?userid="
								+ userId + "&templateid=" + tempId,
						EncodingUtils.getBytes(postData, "BASE64"));

				WebClientClass webViewClient = new WebClientClass();
				wv.setWebViewClient(webViewClient);
				// wv.setWebViewClient(new WebViewClient());

				alert.setView(wv);
				alert.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
				alert.show();

			}
		});

		String date_time = getItem(position).getDateTime();
		if (date_time!=null && date_time.length() > 5) {
			date = date_time.substring(0, 10);
			time = date_time.substring(11);
			// Log.w("time", "@@@@"+time+","+date);

			tv.setText(date + " " + time);
			// tv1.setText(time);
		}

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
									//DeleteSheduledMail
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										fetchMobileandUserId();
										new webservice(
												null,
												webservice.DeleteSheduledMail
														.geturl(UserLogin,
																Password,
																date.trim(),
																time),
												webservice.TYPE_GET,
												webservice.TYPE_DELETE_SCHEDULE_MAIL,
												new ServiceHitListener() {

													@Override
													public void onSuccess(
															Object Result,
															int id) {

														ScheduleListDelete model = (ScheduleListDelete) Result;
														
														Log.i("result","model-"+model.toString());

														 
														 
														

														if (model.getDeleteSheduledMail().size() > 0 && model.getDeleteSheduledMail().get(0).getMsg() != null) {
														
															Toast.makeText(
																	getContext(),
																	model.getDeleteSheduledMail().get(0).getMsg(),
																	Toast.LENGTH_SHORT)
																	.show();
															LISTDATA.remove(position);
															notifyDataSetChanged();

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
	
	private static class WebViewHelper extends WebView {
		public WebViewHelper(Context context) {
			super(context);
		}

		// Note this!
		@Override
		public boolean onCheckIsTextEditor() {
			return true;
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_UP:
				if (!hasFocus())
					requestFocus();
				break;
			}

			return super.onTouchEvent(ev);
		}

	}

	private class WebClientClass extends WebViewClient {
		ProgressDialog pd = null;

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if (pd == null) {
				pd = new ProgressDialog(context);
			}
			pd.setTitle("Please wait");
			pd.setMessage("Page is loading..");
			pd.show();
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if (pd != null)
				pd.dismiss();
			super.onPageFinished(view, url);
		}
	}
}
