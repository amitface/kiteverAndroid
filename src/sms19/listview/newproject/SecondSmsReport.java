package sms19.listview.newproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import sms19.listview.adapter.TodayAdpter;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.SendSmsReport;
import sms19.listview.newproject.model.SendSmsReport.ReportSms;
import sms19.listview.webservice.Method;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

public class SecondSmsReport extends ActionBarActivity implements
		ServiceHitListener, OnClickListener {
	private ProgressDialog p;
	String UserId = "";
	ListView listSMSREports;
	DataBaseDetails dbObject = new DataBaseDetails(this);
	String Mobile = "";
	ImageView imgDownload, backbutton;

	String ListData[];
	ArrayList<String> listData;

	// file regards
	String dataGood = "";
	String ss;
	boolean dataimage = false;
	Context gContext;
	boolean CheckDownloadStatus = true;
	TextView smsrecords;

	// for responce from server
	String dateT = "";
	String mobile = "";
	String message = "";
	String status = "";
	String dateTime = "";
	TextView dateTxt;
	TextView noRecord;
	TextView pdfView;
	private static final int DIALOG_PICK_DATETIME_ID = 0;
	private int year, month, day;
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_reports);

		listSMSREports = (ListView) findViewById(R.id.SMSReportslistView);
		// imv = (ImageView) findViewById(R.id.ifnoreport);
		imgDownload = (ImageView) findViewById(R.id.imgDownload);
		// backbutton = (ImageView) findViewById(R.id.backbutton);
		noRecord = (TextView) findViewById(R.id.no_record);
		progressBar = (ProgressBar) findViewById(R.id.progressBar_id);
		pdfView = (TextView) findViewById(R.id.pdf_view);
		// backbutton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// onBackPressed();
		// }
		// });
		LinearLayout statusUnderstand = (LinearLayout) findViewById(R.id.status_understand);
		statusUnderstand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openDialog();
			}
		});
		smsrecords = (TextView) findViewById(R.id.smsrecords);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
		bar.setTitle(Html.fromHtml("<font color='#ffffff'>Reports</font>"));
		// bar.setHomeAsUpIndicator(R.drawable.arrow_new);
		bar.setDisplayHomeAsUpEnabled(true);
		// bar.hide();
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		dateTxt = (TextView) findViewById(R.id.txt_date);
		StringBuilder dateVal = new StringBuilder()
				// Month is 0 based, just add 1
				.append(day).append("/").append(month + 1).append("/")
				.append(year).append(" ");
		if (dateVal != null && dateVal.length() > 0) {
			callWebService(dateVal);
		}
		dateTxt.setText(dateVal);
		dateTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_PICK_DATETIME_ID);
			}
		});

		gContext = this;
		try {
			p.setCancelable(true);
		} catch (Exception e) {

		}
		/*
		 * try { Intent i = getIntent(); String value =
		 * i.getStringExtra("dayreport");
		 * 
		 * if (value.equalsIgnoreCase("today")) {
		 * 
		 * p = ProgressDialog .show(this, null,
		 * "Please wait it will take sometime to populate data ....");
		 * fetchMobileandUserId(); new webservice(null,
		 * webservice.SendSmsTodayRpt.geturl( "PELSFT", UserId),
		 * webservice.TYPE_GET, webservice.TYPE_SMS_REPORT, this);
		 * 
		 * }
		 * 
		 * if (value.equalsIgnoreCase("one")) { p = ProgressDialog .show(this,
		 * null, "Please wait it will take sometime to populate data....");
		 * fetchMobileandUserId(); new webservice(null,
		 * webservice.SendSmsOneRpt.geturl("PELSFT", UserId),
		 * webservice.TYPE_GET, webservice.TYPE_SMS_ONEREPORT, this);
		 * 
		 * }
		 * 
		 * if (value.equalsIgnoreCase("two")) { p = ProgressDialog .show(this,
		 * null, "Please wait it will take sometime to populate data....");
		 * fetchMobileandUserId(); new webservice(null,
		 * webservice.SendSmsTwoRpt.geturl("PELSFT", UserId),
		 * webservice.TYPE_GET, webservice.TYPE_SMS_TWOREPORT, this);
		 * 
		 * }
		 * 
		 * if (value.equalsIgnoreCase("three")) { p = ProgressDialog .show(this,
		 * null, "Please wait it will take sometime to populate data....");
		 * fetchMobileandUserId(); new webservice(null,
		 * webservice.SendSmsThreeRpt.geturl( "PELSFT", UserId),
		 * webservice.TYPE_GET, webservice.TYPE_SMS_THREEREPORT, this);
		 * 
		 * }
		 * 
		 * if (value.equalsIgnoreCase("four")) { p = ProgressDialog .show(this,
		 * null, "Please wait it will take sometime to populate data....");
		 * fetchMobileandUserId(); new webservice(null,
		 * webservice.SendSmsFourRpt.geturl("PELSFT", UserId),
		 * webservice.TYPE_GET, webservice.TYPE_SMS_FOURREPORT, this);
		 * 
		 * }
		 * 
		 * if (value.equalsIgnoreCase("five")) { p = ProgressDialog .show(this,
		 * null, "Please wait it will take sometime to populate data....");
		 * fetchMobileandUserId(); new webservice(null,
		 * webservice.SendSmsFiveRpt.geturl("PELSFT", UserId),
		 * webservice.TYPE_GET, webservice.TYPE_SMS_FIVEREPORT, this);
		 * 
		 * }
		 * 
		 * if (value.equalsIgnoreCase("six")) { p = ProgressDialog .show(this,
		 * null, "Please wait it will take sometime to populate data....");
		 * fetchMobileandUserId(); new webservice(null,
		 * webservice.SendSmsSixRpt.geturl("PELSFT", UserId),
		 * webservice.TYPE_GET, webservice.TYPE_SMS_SIXREPORT, this);
		 * 
		 * } } catch (Exception ed) { ed.printStackTrace(); }
		 */

		imgDownload.setOnClickListener(this);
		
		if(android.os.Build.VERSION.SDK_INT >=19) imgDownload.setVisibility(View.VISIBLE);
		else imgDownload.setVisibility(View.GONE);
		
	}

	private void openDialog() {
		final Dialog dialog = new Dialog(SecondSmsReport.this);
		dialog.setContentView(R.layout.status_understand_layout);
		dialog.setCancelable(false);
		dialog.setTitle("Status of Delivery Report");
		TextView ok = (TextView) dialog.findViewById(R.id.ok_txt);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void callWebService(StringBuilder date) {
		progressBar.setVisibility(View.VISIBLE);
		noRecord.setVisibility(View.GONE);
		fetchMobileandUserId();
		new webservice(null, webservice.FetchDeliveryReportApi.geturl(
				date.toString(), UserId), webservice.TYPE_GET,
				webservice.TYPE_SMS_REPORT, this);
	}

	// private void callSmsDownloadWebService(StringBuilder date) {
	// SharedPreferences prfs = getSharedPreferences("profileData",
	// Context.MODE_PRIVATE);
	// String password= prfs.getString("Pwd", "");
	// String username=prfs.getString("user_login", "");
	// if(username.startsWith("+")){
	// username=username.substring(1);
	// }
	// new webservice(null, webservice.SmsDeliverReportDownload.geturl(
	// password, username,"",""), webservice.TYPE_GET,
	// webservice.TYPE_SMS_REPORT, this);
	// }
	@Override
	protected Dialog onCreateDialog(int id) {

		if (id == DIALOG_PICK_DATETIME_ID) {
			return new DatePickerDialog(this, pickerListener, year, month, day);
		}

		return super.onCreateDialog(id);
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			
			// Show selected date
			StringBuilder dateVal = new StringBuilder().append(day).append("/")
					.append(month + 1).append("/").append(year).append(" ");
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date date=null;
			try {
				date = format.parse(dateVal.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			long millisecondsSelectedTime = date.getTime();
			Calendar calendar = Calendar.getInstance();
			long millisecondscurrentTime = calendar.getTimeInMillis();
			if(millisecondsSelectedTime>millisecondscurrentTime){
				Toast.makeText(getApplicationContext(), "You cannot select before date", Toast.LENGTH_LONG).show();
			}else{
				long daysSelected,daysCurrent;
				  daysSelected = TimeUnit.MILLISECONDS.toDays(millisecondsSelectedTime);
				  daysCurrent= TimeUnit.MILLISECONDS.toDays(millisecondscurrentTime);
				 if((daysCurrent-daysSelected)>30){
					 Toast.makeText(getApplicationContext(), "You cannot select more than 30 days", Toast.LENGTH_LONG).show();
				 }else{
					 dateTxt.setText(dateVal);
						if (dateVal != null && dateVal.length() > 0) {
							callWebService(dateVal);
						}
				 }
				
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.sms_report_menu, menu);
		return false;
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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
		// if (id == R.id.transaction) {
		//
		// Intent i = new Intent(SecondSmsReport.this, Transaction.class);
		// startActivity(i);
		// return true;
		// }
		//
		// if (id == R.id.editProfile) {
		// Intent i = new Intent(SecondSmsReport.this, EditProfile.class);
		// startActivity(i);
		// return true;
		// }
		//
		// if (id == R.id.schedulemsgr) {
		// Intent i = new Intent(SecondSmsReport.this, ScheduleList.class);
		// startActivity(i);
		// return true;
		// }
		// if (id == android.R.id.home) {
		// onBackPressed();
		// }

		/*
		 * if(id == R.id.termscondition) { Intent i = new
		 * Intent(SecondSmsReport.this,TermsAndCondition.class);
		 * startActivity(i); return true; }
		 * 
		 * if(id == R.id.HowtoUse) { Intent i = new
		 * Intent(SecondSmsReport.this,HowToUse.class); startActivity(i); return
		 * true; }
		 * 
		 * if(id==R.id.refer) { Intent i = new
		 * Intent(SecondSmsReport.this,Friendsinvite.class); startActivity(i);
		 * finish(); return true; }
		 */

		return super.onOptionsItemSelected(item);
	}

	private void callLogoutMethod() {/*
									 * 
									 * 
									 * 
									 * new AlertDialog.Builder(this)
									 * .setCancelable(false) .setMessage(
									 * "Are you Sure you want to Exit? All your chat data will be deleted."
									 * ) .setPositiveButton("OK", new
									 * DialogInterface.OnClickListener() {
									 * 
									 * @Override public void
									 * onClick(DialogInterface dialog, int
									 * which) {
									 * 
									 * // delete all database
									 * DatabaseCleanState();
									 * 
									 * Toast.makeText(getApplicationContext(),
									 * "Logout Successfully",
									 * Toast.LENGTH_SHORT).show();
									 * 
									 * Intent i = new
									 * Intent(SecondSmsReport.this,SMS19.class);
									 * i
									 * .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									 * | Intent.FLAG_ACTIVITY_NEW_TASK);
									 * startActivity(i); finish();
									 * 
									 * 
									 * } public SQLiteDatabase getDBObject() {
									 * return dbObject.db; }
									 * 
									 * private void DatabaseCleanState() { //
									 * TODO Auto-generated method stub
									 * dbObject.Open();
									 * dbObject.onUpgrade(getDBObject(), 1, 1);
									 * dbObject.close(); }
									 * 
									 * }) .setNegativeButton("CANCEL", new
									 * DialogInterface.OnClickListener(){
									 * 
									 * @Override public void
									 * onClick(DialogInterface dialog, int
									 * which) { dialog.cancel(); }}) .show();
									 */
	}

	public void fetchMobileandUserId() {
		dbObject.Open();

		Cursor c;

		c = dbObject.getLoginDetails();

		while (c.moveToNext()) {

			Mobile = c.getString(1);
			UserId = c.getString(3);

		}

		dbObject.close();
	}

	SendSmsReport modelforPdf;

	@Override
	public void onSuccess(Object Result, int id) {
		progressBar.setVisibility(View.GONE);
		listData = new ArrayList<String>();

		if (id == webservice.TYPE_SMS_REPORT) {
			SendSmsReport model = (SendSmsReport) Result;

			if (model != null) {
				List<ReportSms> gh = model.getSmsTodayReport();

				int len = model.getListSize();

				if (gh.size() > 0) {
					modelforPdf = model;
					noRecord.setVisibility(View.GONE);
					dataimage = false;
					listSMSREports.setAdapter(new TodayAdpter(this,
							R.layout.listsmsreports, gh));
					smsrecords.setText("" + len);

					for (int i = 0; i < len; i++) {

						try {
							mobile = "-------------------------------"
									+ "\n\n"
									+ "Mobile:"
									+ model.getSmsTodayReport().get(i)
											.getMobile().trim();
							message = "Message:"
									+ model.getSmsTodayReport().get(i)
											.getMessage().trim();
							status = "Status:"
									+ model.getSmsTodayReport().get(i)
											.getMessageStatus().trim();
							dateTime = "DateTime:"
									+ model.getSmsTodayReport().get(i)
											.getMessageDeliveredDate().trim()
									+ "\n" + "-------------------------------"
									+ "\n";
							dateT = model.getSmsTodayReport().get(i)
									.getMessageDeliveredDate().trim();

						} catch (Exception e) {
							e.printStackTrace();
						}

						/*
						 * String filestatus; if(!status.equalsIgnoreCase("1"))
						 * { filestatus="Delivered"; } else {
						 * filestatus="Not Delivered"; }
						 */

						listData.add(mobile);
						listData.add(message);
						listData.add(status);
						// listData.add(filestatus);
						listData.add(dateTime);

					}
					/******* List to String Convert Logic *****/

					try {
						// Create one string array till list size
						ListData = new String[listData.size()];
						// convert data list to string arrary by using toArrary
						ListData = listData.toArray(ListData);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					/*************************************/

					/**************** All Data Store in a File *********************/

					try {
						String dateTimeFile = dateT.substring(0,
								dateT.indexOf("T"));
						String dataWithGoodFormat[] = dateTimeFile.split("-");
						dataGood = dataWithGoodFormat[2] + "-"
								+ dataWithGoodFormat[1] + "-"
								+ dataWithGoodFormat[0];

					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					try {
						// create one file to mobile
						// ss = "/" + "SMS19_REPORT:" + dataGood + ".pdf";
						Method.createfile(ss);
						// ******************
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					String abc = "";

					for (int d = 0; d < ListData.length; d++) {
						try {
							abc += ListData[d] + "\n";

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					try {
						Method.storeDataInDeviceStorage(abc);
					} catch (Exception e) {
						e.printStackTrace();
					}

					/*************************************/

				} else {
					dataimage = true;

					// imv.setVisibility(View.VISIBLE);
					noRecord.setVisibility(View.VISIBLE);
				}
			} else {

				noRecord.setVisibility(View.VISIBLE);
			}
			try {

				p.dismiss();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// try {
			// // String EmergencyMessage = "";
			// // if (model != null) {
			// // EmergencyMessage = model.getSmsTodayReport().get(0)
			// // .getEmergencyMessage();
			// }
			//
			// try {
			//
			// Emergency.desAct.finish();
			// } catch (Exception e) {
			// }
			//
			// if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
			// Intent rt = new Intent(SecondSmsReport.this,
			// Emergency.class);
			// rt.putExtra("Emergency", EmergencyMessage);
			// startActivity(rt);
			//
			// }
			// } catch (Exception e) {
			//
			// }
		}

		// if (id == webservice.TYPE_SMS_ONEREPORT) {
		//
		// SendSmsReportOneDayModel model = (SendSmsReportOneDayModel) Result;
		// List<ReportSmsOne> gh1 = model.getBeforeOneDayReport();
		//
		// int len = model.getListSize();
		//
		// if (gh1.size() > 0) {
		// dataimage = false;
		// smsrecords.setText("" + len);
		// listSMSREports.setAdapter(new OneDayReportAdtr(this,
		// R.layout.listsmsreports, gh1));
		//
		// for (int i = 0; i < len; i++) {
		//
		// try {
		// mobile = "-------------------------------"
		// + "\n\n"
		// + "Mobile:"
		// + model.getBeforeOneDayReport().get(i)
		// .getMobile().trim();
		// message = "Message:"
		// + model.getBeforeOneDayReport().get(i)
		// .getMessage().trim();
		// status = "Status:"
		// + model.getBeforeOneDayReport().get(i)
		// .getMessageStatus().trim();
		// dateTime = "DateTime:"
		// + model.getBeforeOneDayReport().get(i)
		// .getMessageDeliveredDate().trim()
		// + "\n" + "-------------------------------"
		// + "\n";
		// dateT = model.getBeforeOneDayReport().get(i)
		// .getMessageDeliveredDate().trim();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// String filestatus;
		// /*
		// * if(!status.equalsIgnoreCase("1")) {
		// * filestatus="Delivered"; } else {
		// * filestatus="Not Delivered"; }
		// */
		//
		// listData.add(mobile);
		// listData.add(message);
		// listData.add(status);
		// listData.add(dateTime);
		//
		// }
		// /******* List to String Convert Logic *****/
		//
		// try {
		// // Create one string array till list size
		// ListData = new String[listData.size()];
		// // convert data list to string array by using toArrary
		// ListData = listData.toArray(ListData);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// /*************************************/
		//
		// /**************** All Data Store in a File *********************/
		//
		// try {
		// String dateTimeFile = dateT
		// .substring(0, dateT.indexOf("T"));
		// String dataWithGoodFormat[] = dateTimeFile.split("-");
		// dataGood = dataWithGoodFormat[2] + "-"
		// + dataWithGoodFormat[1] + "-"
		// + dataWithGoodFormat[0];
		//
		// } catch (Exception e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }
		//
		// try {
		// // create one file to mobile
		// ss = "/" + "SMS19_REPORT:" + dataGood + ".txt";
		// Method.createfile(ss);
		// // ******************
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		//
		// String abc = "";
		//
		// for (int d = 0; d < ListData.length; d++) {
		// try {
		// abc += ListData[d] + "\n";
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		//
		// try {
		// Method.storeDataInDeviceStorage(abc);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// /*************************************/
		//
		// } else {
		// dataimage = true;
		// imv.setVisibility(View.VISIBLE);
		// }
		//
		// try {
		//
		// p.dismiss();
		//
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// String EmergencyMessage = model.getBeforeOneDayReport().get(0)
		// .getEmergencyMessage();
		// try {
		//
		// Emergency.desAct.finish();
		// } catch (Exception e) {
		// }
		//
		// if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
		// Intent rt = new Intent(SecondSmsReport.this,
		// Emergency.class);
		// rt.putExtra("Emergency", EmergencyMessage);
		// startActivity(rt);
		//
		// }
		// } catch (Exception e) {
		//
		// }
		// }
		//
		// if (id == webservice.TYPE_SMS_TWOREPORT) {
		// SendSmsReportTwoDayModel model = (SendSmsReportTwoDayModel) Result;
		// List<ReportSmsTwo> gh2 = model.getBeforeTwoDayReport();
		//
		// int len = model.getListSize();
		//
		// if (gh2.size() > 0) {
		// smsrecords.setText("" + len);
		// dataimage = false;
		// listSMSREports.setAdapter(new TwoDayAdapter(this,
		// R.layout.listsmsreports, gh2));
		//
		// for (int i = 0; i < len; i++) {
		//
		// try {
		// mobile = "-------------------------------"
		// + "\n\n"
		// + "Mobile:"
		// + model.getBeforeTwoDayReport().get(i)
		// .getMobile().trim();
		// message = "Message:"
		// + model.getBeforeTwoDayReport().get(i)
		// .getMessage().trim();
		// status = "Status:"
		// + model.getBeforeTwoDayReport().get(i)
		// .getMessageStatus().trim();
		// dateTime = "DateTime:"
		// + model.getBeforeTwoDayReport().get(i)
		// .getMessageDeliveredDate().trim()
		// + "\n" + "-------------------------------"
		// + "\n";
		// dateT = model.getBeforeTwoDayReport().get(i)
		// .getMessageDeliveredDate().trim();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// String filestatus;
		//
		// /*
		// * if(!status.equalsIgnoreCase("1")) {
		// * filestatus="Delivered"; } else {
		// * filestatus="Not Delivered"; }
		// */
		//
		// listData.add(mobile);
		// listData.add(message);
		// listData.add(status);
		// listData.add(dateTime);
		//
		// }
		// /******* List to String Convert Logic *****/
		//
		// try {
		// // Create one string array till list size
		// ListData = new String[listData.size()];
		// // convert data list to string array by using toArrary
		// ListData = listData.toArray(ListData);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// /*************************************/
		//
		// /**************** All Data Store in a File *********************/
		//
		// try {
		// String dateTimeFile = dateT
		// .substring(0, dateT.indexOf("T"));
		// String dataWithGoodFormat[] = dateTimeFile.split("-");
		// dataGood = dataWithGoodFormat[2] + "-"
		// + dataWithGoodFormat[1] + "-"
		// + dataWithGoodFormat[0];
		//
		// } catch (Exception e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }
		//
		// try {
		// // create one file to mobile
		// ss = "/" + "SMS19_REPORT:" + dataGood + ".txt";
		// Method.createfile(ss);
		// // ******************
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		//
		// String abc = "";
		//
		// for (int d = 0; d < ListData.length; d++) {
		// try {
		// abc += ListData[d] + "\n";
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		//
		// try {
		// Method.storeDataInDeviceStorage(abc);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// } else {
		// dataimage = true;
		// imv.setVisibility(View.VISIBLE);
		// }
		//
		// try {
		//
		// p.dismiss();
		//
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// }
		// try {
		// String EmergencyMessage = model.getBeforeTwoDayReport().get(0)
		// .getEmergencyMessage();
		// try {
		//
		// Emergency.desAct.finish();
		// } catch (Exception e) {
		// }
		//
		// if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
		// Intent rt = new Intent(SecondSmsReport.this,
		// Emergency.class);
		// rt.putExtra("Emergency", EmergencyMessage);
		// startActivity(rt);
		//
		// }
		// } catch (Exception e) {
		//
		// }
		// }
		//
		// if (id == webservice.TYPE_SMS_THREEREPORT) {
		// SendSmsReportThreeDayModel model = (SendSmsReportThreeDayModel)
		// Result;
		// List<ReportSmsThree> gh3 = model.getBeforeThreeDayReport();
		//
		// int len = model.getListSize();
		//
		// if (gh3.size() > 0) {
		// smsrecords.setText("" + len);
		// dataimage = false;
		// listSMSREports.setAdapter(new ThreeDayAdapter(this,
		// R.layout.listsmsreports, gh3));
		//
		// for (int i = 0; i < len; i++) {
		//
		// try {
		// mobile = "-------------------------------"
		// + "\n\n"
		// + "Mobile:"
		// + model.getBeforeThreeDayReport().get(i)
		// .getMobile().trim();
		// message = "Message:"
		// + model.getBeforeThreeDayReport().get(i)
		// .getMessage().trim();
		// status = "Status:"
		// + model.getBeforeThreeDayReport().get(i)
		// .getMessageStatus().trim();
		// dateTime = "DateTime:"
		// + model.getBeforeThreeDayReport().get(i)
		// .getMessageDeliveredDate().trim()
		// + "\n" + "-------------------------------"
		// + "\n";
		// dateT = model.getBeforeThreeDayReport().get(i)
		// .getMessageDeliveredDate().trim();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// String filestatus;
		//
		// /*
		// * if(!status.equalsIgnoreCase("1")) {
		// * filestatus="Delivered"; } else {
		// * filestatus="Not Delivered"; }
		// */
		//
		// listData.add(mobile);
		// listData.add(message);
		// listData.add(status);
		// listData.add(dateTime);
		//
		// }
		// /******* List to String Convert Logic *****/
		//
		// try {
		// // Create one string array till list size
		// ListData = new String[listData.size()];
		// // convert data list to string array by using toArrary
		// ListData = listData.toArray(ListData);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// /*************************************/
		//
		// /**************** All Data Store in a File *********************/
		//
		// try {
		// String dateTimeFile = dateT
		// .substring(0, dateT.indexOf("T"));
		// String dataWithGoodFormat[] = dateTimeFile.split("-");
		// dataGood = dataWithGoodFormat[2] + "-"
		// + dataWithGoodFormat[1] + "-"
		// + dataWithGoodFormat[0];
		//
		// } catch (Exception e2) {
		// }
		//
		// try {
		// // create one file to mobile
		// ss = "/" + "SMS19_REPORT:" + dataGood + ".txt";
		// Method.createfile(ss);
		// // ******************
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		//
		// String abc = "";
		//
		// for (int d = 0; d < ListData.length; d++) {
		// try {
		// abc += ListData[d] + "\n";
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		//
		// try {
		// Method.storeDataInDeviceStorage(abc);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// /*************************************/
		//
		// } else {
		// dataimage = true;
		// imv.setVisibility(View.VISIBLE);
		// }
		//
		// try {
		//
		// p.dismiss();
		//
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// String EmergencyMessage = model.getBeforeThreeDayReport()
		// .get(0).getEmergencyMessage();
		// try {
		//
		// Emergency.desAct.finish();
		// } catch (Exception e) {
		// }
		//
		// if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
		// Intent rt = new Intent(SecondSmsReport.this,
		// Emergency.class);
		// rt.putExtra("Emergency", EmergencyMessage);
		// startActivity(rt);
		//
		// }
		// } catch (Exception e) {
		//
		// }
		// }
		//
		// if (id == webservice.TYPE_SMS_FOURREPORT) {
		// SendSmsReportFourDayModel model = (SendSmsReportFourDayModel) Result;
		// List<ReportSmsFour> gh4 = model.getBeforeFourDayReport();
		//
		// int len = model.getListSize();
		//
		// if (gh4.size() > 0) {
		// smsrecords.setText("" + len);
		// dataimage = false;
		// listSMSREports.setAdapter(new FourDayReportAdptr(this,
		// R.layout.listsmsreports, gh4));
		//
		// for (int i = 0; i < len; i++) {
		//
		// try {
		// mobile = "-------------------------------"
		// + "\n\n"
		// + "Mobile:"
		// + model.getBeforeFourDayReport().get(i)
		// .getMobile().trim();
		// message = "Message:"
		// + model.getBeforeFourDayReport().get(i)
		// .getMessage().trim();
		// status = "Status:"
		// + model.getBeforeFourDayReport().get(i)
		// .getMessageStatus().trim();
		// dateTime = "DateTime:"
		// + model.getBeforeFourDayReport().get(i)
		// .getMessageDeliveredDate().trim()
		// + "\n" + "-------------------------------"
		// + "\n";
		// dateT = model.getBeforeFourDayReport().get(i)
		// .getMessageDeliveredDate().trim();
		//
		// } catch (Exception e) {
		//
		// }
		//
		// String filestatus;
		//
		// /*
		// * if(!status.equalsIgnoreCase("1")) {
		// * filestatus="Delivered"; } else {
		// * filestatus="Not Delivered"; }
		// */
		//
		// listData.add(mobile);
		// listData.add(message);
		// listData.add(status);
		// listData.add(dateTime);
		//
		// }
		// /******* List to String Convert Logic *****/
		//
		// try {
		// // Create one string array till list size
		// ListData = new String[listData.size()];
		// // convert data list to string arrary by using toArrary
		// ListData = listData.toArray(ListData);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// /*************************************/
		//
		// /**************** All Data Store in a File *********************/
		//
		// try {
		// String dateTimeFile = dateT
		// .substring(0, dateT.indexOf("T"));
		// String dataWithGoodFormat[] = dateTimeFile.split("-");
		// dataGood = dataWithGoodFormat[2] + "-"
		// + dataWithGoodFormat[1] + "-"
		// + dataWithGoodFormat[0];
		//
		// } catch (Exception e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }
		//
		// try {
		// // create one file to mobile
		// ss = "/" + "SMS19_REPORT:" + dataGood + ".txt";
		// Method.createfile(ss);
		// // ******************
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		//
		// String abc = "";
		//
		// for (int d = 0; d < ListData.length; d++) {
		// try {
		// abc += ListData[d] + "\n";
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		//
		// try {
		// Method.storeDataInDeviceStorage(abc);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// /*************************************/
		//
		// } else {
		// dataimage = true;
		// imv.setVisibility(View.VISIBLE);
		// }
		//
		// try {
		//
		// p.dismiss();
		//
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// String EmergencyMessage = model.getBeforeFourDayReport().get(0)
		// .getEmergencyMessage();
		// try {
		//
		// Emergency.desAct.finish();
		// } catch (Exception e) {
		// }
		//
		// if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
		// Intent rt = new Intent(SecondSmsReport.this,
		// Emergency.class);
		// rt.putExtra("Emergency", EmergencyMessage);
		// startActivity(rt);
		//
		// }
		// } catch (Exception e) {
		//
		// }
		// }
		//
		// if (id == webservice.TYPE_SMS_FIVEREPORT) {
		// SendSmsReportFiveDayModel model = (SendSmsReportFiveDayModel) Result;
		// List<ReportSmsFive> gh5 = model.getBeforeFiveDayReport();
		// int len = model.getListSize();
		//
		// if (gh5.size() > 0) {
		// smsrecords.setText("" + len);
		// dataimage = false;
		// listSMSREports.setAdapter(new FifthDayReportAdapter(this,
		// R.layout.listsmsreports, gh5));
		//
		// for (int i = 0; i < len; i++) {
		//
		// try {
		// mobile = "-------------------------------"
		// + "\n\n"
		// + "Mobile:"
		// + model.getBeforeFiveDayReport().get(i)
		// .getMobile().trim();
		// message = "Message:"
		// + model.getBeforeFiveDayReport().get(i)
		// .getMessage().trim();
		// status = "Status:"
		// + model.getBeforeFiveDayReport().get(i)
		// .getMessageStatus().trim();
		// dateTime = "DateTime:"
		// + model.getBeforeFiveDayReport().get(i)
		// .getMessageDeliveredDate().trim()
		// + "\n" + "-------------------------------"
		// + "\n";
		// dateT = model.getBeforeFiveDayReport().get(i)
		// .getMessageDeliveredDate().trim();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// String filestatus;
		//
		// /*
		// * if(!status.equalsIgnoreCase("1")) {
		// * filestatus="Delivered"; } else {
		// * filestatus="Not Delivered"; }
		// */
		//
		// listData.add(mobile);
		// listData.add(message);
		// listData.add(status);
		// listData.add(dateTime);
		//
		// }
		// /******* List to String Convert Logic *****/
		//
		// try {
		// // Create one string array till list size
		// ListData = new String[listData.size()];
		// // convert data list to string arrary by using toArrary
		// ListData = listData.toArray(ListData);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// /*************************************/
		//
		// /**************** All Data Store in a File *********************/
		//
		// try {
		// String dateTimeFile = dateT
		// .substring(0, dateT.indexOf("T"));
		// String dataWithGoodFormat[] = dateTimeFile.split("-");
		// dataGood = dataWithGoodFormat[2] + "-"
		// + dataWithGoodFormat[1] + "-"
		// + dataWithGoodFormat[0];
		//
		// } catch (Exception e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }
		//
		// try {
		// // create one file to mobile
		// ss = "/" + "SMS19_REPORT:" + dataGood + ".txt";
		// Method.createfile(ss);
		// // ******************
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		//
		// String abc = "";
		//
		// for (int d = 0; d < ListData.length; d++) {
		// try {
		// abc += ListData[d] + "\n";
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		//
		// try {
		// Method.storeDataInDeviceStorage(abc);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// /*************************************/
		//
		// } else {
		// dataimage = true;
		// imv.setVisibility(View.VISIBLE);
		// }
		//
		// try {
		//
		// p.dismiss();
		//
		// } catch (Exception e) {
		//
		// }
		// try {
		// String EmergencyMessage = model.getBeforeFiveDayReport().get(0)
		// .getEmergencyMessage();
		// try {
		//
		// Emergency.desAct.finish();
		// } catch (Exception e) {
		// }
		//
		// if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
		// Intent rt = new Intent(SecondSmsReport.this,
		// Emergency.class);
		// rt.putExtra("Emergency", EmergencyMessage);
		// startActivity(rt);
		//
		// }
		// } catch (Exception e) {
		//
		// }
		// }
		//
		// if (id == webservice.TYPE_SMS_SIXREPORT) {
		// SendSmsReportSixDayModel model = (SendSmsReportSixDayModel) Result;
		// List<ReportSmsSixth> gh6 = model.getBeforeSixDayReport();
		//
		// int len = model.getListSize();
		//
		// if (gh6.size() > 0) {
		// dataimage = false;
		// smsrecords.setText("" + len);
		// listSMSREports.setAdapter(new SixthDayReportAdapter(this,
		// R.layout.listsmsreports, gh6));
		//
		// for (int i = 0; i < len; i++) {
		//
		// try {
		// mobile = "-------------------------------"
		// + "\n\n"
		// + "Mobile:"
		// + model.getBeforeSixDayReport().get(i)
		// .getMobile().trim();
		// message = "Message:"
		// + model.getBeforeSixDayReport().get(i)
		// .getMessage().trim();
		// status = "Status:"
		// + model.getBeforeSixDayReport().get(i)
		// .getMessageStatus().trim();
		// dateTime = "DateTime:"
		// + model.getBeforeSixDayReport().get(i)
		// .getMessageDeliveredDate().trim()
		// + "\n" + "-------------------------------"
		// + "\n";
		// dateT = model.getBeforeSixDayReport().get(i)
		// .getMessageDeliveredDate().trim();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// String filestatus;
		// /*
		// * if(!status.equalsIgnoreCase("1")) {
		// * filestatus="Delivered"; } else {
		// * filestatus="Not Delivered"; }
		// */
		//
		// listData.add(mobile);
		// listData.add(message);
		// listData.add(status);
		// listData.add(dateTime);
		//
		// }
		// /******* List to String Convert Logic *****/
		//
		// try {
		// // Create one string array till list size
		// ListData = new String[listData.size()];
		// // convert data list to string arrary by using toArrary
		// ListData = listData.toArray(ListData);
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// }
		//
		// /*************************************/
		//
		// /**************** All Data Store in a File *********************/
		//
		// try {
		// String dateTimeFile = dateT
		// .substring(0, dateT.indexOf("T"));
		// String dataWithGoodFormat[] = dateTimeFile.split("-");
		// dataGood = dataWithGoodFormat[2] + "-"
		// + dataWithGoodFormat[1] + "-"
		// + dataWithGoodFormat[0];
		// } catch (Exception e2) {
		//
		// }
		//
		// try {
		// // create one file to mobile
		// ss = "/" + "SMS19_REPORT:" + dataGood + ".txt";
		// Method.createfile(ss);
		// // ******************
		// } catch (Exception e1) {
		// }
		//
		// String abc = "";
		//
		// for (int d = 0; d < ListData.length; d++) {
		// try {
		// abc += ListData[d] + "\n";
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		//
		// try {
		// Method.storeDataInDeviceStorage(abc);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// /*************************************/
		// } else {
		// dataimage = true;
		// imv.setVisibility(View.VISIBLE);
		// }
		//
		// try {
		//
		// p.dismiss();
		//
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// String EmergencyMessage = model.getBeforeSixDayReport().get(0)
		// .getEmergencyMessage();
		// try {
		//
		// Emergency.desAct.finish();
		// } catch (Exception e) {
		// }
		//
		// if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
		// Intent rt = new Intent(SecondSmsReport.this,
		// Emergency.class);
		// rt.putExtra("Emergency", EmergencyMessage);
		// startActivity(rt);
		//
		// }
		// } catch (Exception e) {
		//
		// }

		// }

	}

	@Override
	public void onError(String Error, int id) {
		// TODO Auto-generated method stub

		try {
			progressBar.setVisibility(View.GONE);
			p.dismiss();
		} catch (Exception e) {

		}

		Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onBackPressed() {
		try {
			p.dismiss();
		} catch (Exception e) {
		}
		// try {
		// Intent i = getIntent();
		// String value = i.getStringExtra("DATE_SMS");
		//
		// String dataWithGoodFormat[] = value.split("/");
		// String dataGoodForm = dataWithGoodFormat[0] + "-"
		// + dataWithGoodFormat[1] + "-" + dataWithGoodFormat[2];
		//
		// String fss = "/" + "Kitevre_REPORT:" + dataGoodForm + ".txt";
		//
		// File file = new File(Environment.getExternalStorageDirectory()
		// + fss);
		// if (file.exists()) {
		// if (CheckDownloadStatus) {
		// file.delete();
		// }
		// }
		// } catch (Exception rd) {
		// }
		finish();
		super.onBackPressed();
		 overridePendingTransition(0, R.anim.animation);   
//		 overridePendingTransition(0, 0);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imgDownload: {

			// String fss = ".txt";
			// try {
			// // Intent i = getIntent();
			// // String value = i.getStringExtra("DATE_SMS");
			// //
			// // String dataWithGoodFormat[] = value.split("/");
			// // String dataGoodForm = (dataWithGoodFormat[0] + "-"
			// // + dataWithGoodFormat[1] + "-" + dataWithGoodFormat[2])
			// // .trim().toString();
			// //
			// // fss = "/Kitever/Media" + ".txt";
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			// final File file = new File(Environment
			// .getExternalStorageDirectory().toString() + fss);

			// if (!file.exists()) {
			new AlertDialog.Builder(gContext)
					.setCancelable(false)
					.setMessage("Are you sure to save it to file as SmsReport.pdf.")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									CheckDownloadStatus = false;
									dialog.cancel();

									// Uri fileUri = Uri.fromFile(file);
									// Intent intent = new Intent();
									// intent.setAction(Intent.ACTION_VIEW);
									// intent.setDataAndType(
									// fileUri,
									// URLConnection
									// .guessContentTypeFromName(fileUri
									// .toString()));
									// startActivity(intent);
									createPdf();
									// createPdf();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									CheckDownloadStatus = true;
									dialog.cancel();
								}
							}).show();
			// } else {
			// if (dataimage) {
			// Toast.makeText(gContext, "NO REPORT AVAILABLE",
			// Toast.LENGTH_SHORT).show();
			// dataimage = false;
			// } else {
			// Toast.makeText(gContext, "NO SPACE AVAILABLE",
			// Toast.LENGTH_SHORT).show();
			// }
			// }

		}
			break;

		default:
			break;
		}

	}

	public void createPdf() {
		{
			String text = "";
			if (modelforPdf != null) {
				for (int i = 0; i < modelforPdf.getListSize(); i++) {

					try {
						String mobile = "Mobile : "
								+ modelforPdf.getSmsTodayReport().get(i)
										.getMobile().trim() + "\n";
						text = text + mobile;
						String message = "Message : "
								+ modelforPdf.getSmsTodayReport().get(i)
										.getMessage().trim() + "\n";
						text = text + message;
						String status = modelforPdf.getSmsTodayReport().get(i)
								.getMessageStatus().trim();
						if (status.equalsIgnoreCase("0")) {
							text = text + "Status : Delivered" + "\n";
						} else if (status.equalsIgnoreCase("1")) {
							text = text + "Status : Undelivered" + "\n";
						} else if (status.equalsIgnoreCase("2")) {
							text = text + "Status : Sent for delivery" + "\n";
						}
						String dateTime = modelforPdf.getSmsTodayReport()
								.get(i).getMessageDeliveredDate().trim();

						String dateT = "Date : "
								+ modelforPdf.getSmsTodayReport().get(i)
										.getMessageDeliveredDate().trim()
								+ "\n";
						text = text + dateT + "\n\n";

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				text = "No details available";
			}

			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath();

			File dir = new File(path);
			if (!dir.exists())
				dir.mkdirs();

			File file = new File(dir, "SmsReport.pdf");
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(file);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			PdfDocument document = new PdfDocument();

			// crate a page description
			PageInfo pageInfo = new PageInfo.Builder(150, 800, 1).create();

			// start a page
			Page page = document.startPage(pageInfo);

			if (page != null) {

										// findViewById(R.id.textMessagew);//
										// getContentView();
				pdfView.setText(text);
				pdfView.layout(0, 0, pdfView.getWidth(), pdfView.getHeight());
				pdfView.setPadding(0, 0, 0, 0);
				// Log.i("draw view",
				// " content size: "+view.getWidth()+" / "+view.getHeight());
				pdfView.setTextSize(3);
				pdfView.draw(page.getCanvas());
				// Move the canvas for the next view.
				page.getCanvas().translate(0, pdfView.getHeight());
			}

			// draw something on the page
			// View content = getContentView();
			// content.draw(page.getCanvas());

			// finish the page
			document.finishPage(page);

			// write the document content
			try {
				document.writeTo(fOut);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// close the document
			document.close();
			pdfView.setTextColor(255);
		}

		/*
		 * text = "hello pdf"; Document doc = new Document();
		 * 
		 * try { String path = Environment.getExternalStorageDirectory()
		 * .getAbsolutePath() + "/Dir";
		 * 
		 * File dir = new File(path); if (!dir.exists()) dir.mkdirs();
		 * 
		 * File file = new File(dir, "newFile.pdf"); FileOutputStream fOut = new
		 * FileOutputStream(file); // Document document = new Document(); //
		 * Rectangle pagesize = new Rectangle(216f, 720f); // Document document
		 * = new Document(pagesize, 36f, 72f, 108f, 180f);
		 * PdfWriter.getInstance(doc, fOut);
		 * 
		 * // open the document doc.open(); doc.add(new Paragraph("hello"));
		 * doc.add(new Paragraph("world")); // Paragraph p1 = new
		 * Paragraph(text); // Font paraFont= new Font(Font.COURIER); //
		 * p1.setAlignment(Paragraph.ALIGN_CENTER); // p1.setFont(paraFont);
		 * 
		 * // add paragraph to document // doc.add(p1);
		 * 
		 * } catch (DocumentException de) { // Log.e("PDFCreator",
		 * "DocumentException:" + de); } catch (IOException e) { //
		 * Log.e("PDFCreator", "ioException:" + e); } finally { doc.close(); }
		 */
		// viewPdf("newFile.pdf", "Dir");
	}
	
	
}