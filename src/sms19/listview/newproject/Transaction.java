package sms19.listview.newproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sms19.listview.adapter.MonitorAdapter;
import sms19.listview.adapter.MonitorAdapterDate;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.TransactionDetailModel;
import sms19.listview.newproject.model.TransactionListByDate;
import sms19.listview.newproject.model.TransactionDetailModel.Tdetail;
import sms19.listview.newproject.model.TransactionListByDate.TdetailDate;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class Transaction extends ActionBarActivity implements OnClickListener,
		ServiceHitListener {

	ListView listTransaction;
	LinearLayout linLayout;

	TextView textFormDate, textToDate,to, textViewName,transaction_title, noRecord,pdfView;
	ImageButton btnDatePickerFrom, btnDatePickerTo, btnSearchTrans;

	// flag for check status of filter
	boolean filterstatus = true;

	// parameter for pick calendar
	static int mYear, mMonth, mDate;

	String Mobile = "";
	String UserId = "";

	String FromDate = "";
	String ToDate = "";

	Context cntTrans;

	DataBaseDetails dbObject = new DataBaseDetails(this);

	boolean flagToToast = true;
	private int year, month, day;
	ProgressBar progressBar;
	ImageView download;
	TransactionListByDate modelforPdf;
	private TextView DateTITLE,DebitedTitle,CreditTitle,BalanceDATA;

	// static final String[] LIST_DATA
	// ={"Send Sms","Contacts","Template","Sms Reports","Buy Credit","Upcoming Events"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBarSettingWithBack(this,getSupportActionBar(),"Transactions");

		setContentView(R.layout.activity_transaction);
		linLayout = (LinearLayout) findViewById(R.id.linlayoutTrans);
		listTransaction = (ListView) findViewById(R.id.TranslistView);

		download= (ImageView) findViewById(R.id.download);
		progressBar=(ProgressBar)findViewById(R.id.progressBar_id);
		pdfView = (TextView)findViewById(R.id.pdf_view);
		btnDatePickerFrom = (ImageButton) findViewById(R.id.btnDatePickerFrom);
		btnDatePickerTo = (ImageButton) findViewById(R.id.btnDatePickerTo);
		noRecord = (TextView) findViewById(R.id.no_record);

		textFormDate = (TextView) findViewById(R.id.textFormDate);
		to = (TextView) findViewById(R.id.to);
		textToDate = (TextView) findViewById(R.id.textToDate);
		textViewName = (TextView) findViewById(R.id.textViewName);
		transaction_title= (TextView) findViewById(R.id.transaction_title);


		DateTITLE= (TextView) findViewById(R.id.DateTITLE);
		DebitedTitle= (TextView) findViewById(R.id.DebitedTitle);
		CreditTitle= (TextView) findViewById(R.id.CreditTitle);
		BalanceDATA= (TextView) findViewById(R.id.BalanceDATA);


		setRobotoThinFont(textFormDate,this);
		setRobotoThinFont(to,this);
		setRobotoThinFont(textToDate,this);
		setRobotoThinFont(textViewName,this);
		setRobotoThinFont(transaction_title,this);

		setRobotoThinFont(DateTITLE,this);
		setRobotoThinFont(DebitedTitle,this);
		setRobotoThinFont(CreditTitle,this);
		setRobotoThinFont(BalanceDATA,this);

		textFormDate.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		to.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		textToDate.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		textViewName.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		transaction_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

		DateTITLE.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		DebitedTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		CreditTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		BalanceDATA.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));



		// btnSearchTrans = (ImageButton) findViewById(R.id.btnSearchTrans);

		// btnSearchTrans.setOnClickListener(this);
		btnDatePickerFrom.setOnClickListener(this);
		btnDatePickerTo.setOnClickListener(this);
		download.setOnClickListener(this);
		if (android.os.Build.VERSION.SDK_INT >= 19) 	download.setVisibility(View.VISIBLE);
		else download.setVisibility(View.GONE);




		/*************************** INTERNET ********************************/
		webservice._context = this;
		cntTrans = this;
		/*************************** INTERNET ********************************/

		fetchMobileandUserId();
		Calendar fromC = Calendar.getInstance();
		mYear = fromC.get(Calendar.YEAR);
		mMonth = fromC.get(Calendar.MONTH);
		mDate = fromC.get(Calendar.DAY_OF_MONTH);
		StringBuilder dateVal = new StringBuilder()
				// Month is 0 based, just add 1
				.append(mDate).append("/").append(mMonth + 1).append("/")
				.append(mYear).append(" ");
		if (dateVal != null && dateVal.length() > 0) {
			textFormDate.setText(dateVal);
			textToDate.setText(dateVal);
		}

		// new webservice(null, webservice.TransactionDetail.geturl(Mobile,
		// UserId), webservice.TYPE_GET, webservice.TYPE_TRANSACTIONDETL, this);
		new webservice(null, webservice.TransactionDetailsByDate.geturl(Mobile,
				dateVal.toString(), dateVal.toString(), UserId),
				webservice.TYPE_GET, webservice.TYPE_TRAN_DETAILS_BY_DATE, this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.download:
				new AlertDialog.Builder(cntTrans)
						.setCancelable(false)
						.setMessage(
								"Are you sure to save it to file as pdf")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
														int which) {
										//CheckDownloadStatus = false;
										dialog.cancel();
										createPdf();
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
														int which) {
										//CheckDownloadStatus = true;
										dialog.cancel();
									}
								}).show();
				break;

		case R.id.btnDatePickerFrom: {
			// Process to get current date
			Calendar fromC = Calendar.getInstance();
			mYear = fromC.get(Calendar.YEAR);
			mMonth = fromC.get(Calendar.MONTH);
			mDate = fromC.get(Calendar.DAY_OF_MONTH);

			// prompt Dialog of Date picker
			DatePickerDialog datePicDia = new DatePickerDialog(this,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {

							// Log.w("TAG","dayOfMonth"+dayOfMonth+"monthOfYear"+monthOfYear+"year"+year+"mDate"+mDate+"mMonth"+mMonth+"mYear"+mYear);
							if ((dayOfMonth <= mDate)
									&& (monthOfYear + 1) <= (mMonth + 1)
									&& (year <= mYear)) {
								// Log.w("ENTER","dayOfMonth"+dayOfMonth+"monthOfYear"+monthOfYear+"year"+year+"mDate"+mDate+"mMonth"+mMonth+"mYear"+mYear);

								textFormDate.setText(dayOfMonth + "/"
										+ (monthOfYear + 1) + "/" + year);
								FromDate = dayOfMonth + "/" + (monthOfYear + 1)
										+ "/" + year;
							}

							else {
								// //Log.w("ELSE","dayOfMonth"+dayOfMonth+"monthOfYear"+monthOfYear+"year"+year+"mDate"+mDate+"mMonth"+mMonth+"mYear"+mYear);

								if (flagToToast) {
									Toast.makeText(cntTrans,
											"Date cannot be later than today",
											Toast.LENGTH_SHORT).show();
									flagToToast = false;
								} else {
									flagToToast = true;
								}
							}

						}
					}, mYear, mMonth, mDate);

			datePicDia.show();

		}
			break;
		case R.id.btnDatePickerTo: {

			// get current date from device
			Calendar ToC = Calendar.getInstance();
			mYear = ToC.get(Calendar.YEAR);
			mMonth = ToC.get(Calendar.MONTH);
			mDate = ToC.get(Calendar.DAY_OF_MONTH);

			// Show Date Picker dialog

			DatePickerDialog dataPicDialog = new DatePickerDialog(this,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {

							if ((dayOfMonth <= mDate)
									&& (monthOfYear + 1) <= (mMonth + 1)
									&& (year <= mYear)) {

								textToDate.setText(dayOfMonth + "/"
										+ (monthOfYear + 1) + "/" + year);
								ToDate = dayOfMonth + "/" + (monthOfYear + 1)
										+ "/" + year;

								callWebService();

							}

							else {

								if (flagToToast) {
									Toast.makeText(cntTrans,
											"Date cannot be later than today",
											Toast.LENGTH_SHORT).show();
									flagToToast = false;
								} else {
									flagToToast = true;
								}
							}
						}
					}, mYear, mMonth, mDate);

			dataPicDialog.show();

		}
			break;

		default:
			break;
		}

	}

	public void createPdf() {
		{
			String text = "\n\n";
			if (modelforPdf != null) {
				//Log.i("TRANSACTION","---"+modelforPdf.toString());
				for (int i = 0; i < modelforPdf.getListSize(); i++) {

					try {

						String d1date= modelforPdf.getUserTransactionDetailsByDate().get(i).getDatecreated().substring(modelforPdf.getUserTransactionDetailsByDate().get(i).getDatecreated().indexOf("T")+1,modelforPdf.getUserTransactionDetailsByDate().get(i).getDatecreated().length());
						String d1 = "\t Date : "
								+ d1date + "\n";
						text = text + d1;
						String d2 = "\t Debits : "
								+ modelforPdf.getUserTransactionDetailsByDate().get(i).getDebits()
								.trim() + "\n";
						text = text + d2;

						String d3 = "\t Credits : "
								+ modelforPdf.getUserTransactionDetailsByDate().get(i).getCredits()
								.trim() + "\n";
						text = text + d3;

						String d4 = "\t Balance : "
								+ modelforPdf.getUserTransactionDetailsByDate().get(i).getBalance()
								.trim() + "\n";
						text = text + d4;


						text = text +  "\n\n";

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				text = "No details available";
			}

			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath()+"/"+getPackageName()+"/Transactions/";

			File dir = new File(path);
			if (!dir.exists())
				dir.mkdirs();

			long current_time =System.currentTimeMillis();
			File file = new File(dir, "transaction_"+current_time+".pdf");
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(file);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			PdfDocument document = new PdfDocument();

			// crate a page description
			PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(150, 800, 1).create();

			// start a page
			PdfDocument.Page page = document.startPage(pageInfo);

			if (page != null) {
				pdfView.setText(text);
				pdfView.layout(0, 0, pdfView.getWidth(), pdfView.getHeight());
				pdfView.setPadding(0, 0, 0, 0);

				pdfView.setTextSize(3);
				pdfView.draw(page.getCanvas());
				page.getCanvas().translate(0, pdfView.getHeight());
			}
			document.finishPage(page);

			try {
				document.writeTo(fOut);
				openpdf(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// close the document
			document.close();
			pdfView.setTextColor(255);
		}
	}

	private void openpdf(final File file)
	{
		new AlertDialog.Builder(cntTrans)
				.setCancelable(false)
				.setMessage("Pdf saved successfully in "+file.toString() +" do you want to open it? ")
				.setPositiveButton("Open now",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								//CheckDownloadStatus = false;
								dialog.cancel();
								Uri pfdfpath = Uri.fromFile(file);
								Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
								pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								pdfOpenintent.setDataAndType(pfdfpath, "application/pdf");
								try {
									startActivity(pdfOpenintent);
								}
								catch (ActivityNotFoundException e) {

								}
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								//CheckDownloadStatus = true;
								dialog.cancel();
							}
						}).show();
	}

	private void callWebService() {
		fetchMobileandUserId();

		if (!FromDate.equals("") && !ToDate.equals("")) {
			new webservice(null, webservice.TransactionDetailsByDate.geturl(
					Mobile, FromDate, ToDate, UserId), webservice.TYPE_GET,
					webservice.TYPE_TRAN_DETAILS_BY_DATE, this);
		}

		else {
			Toast.makeText(getApplicationContext(),
					"Please select Date for search.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onSuccess(Object Result, int id) {

		try {
			progressBar.setVisibility(View.GONE);
			noRecord.setVisibility(View.GONE);
			if (id == webservice.TYPE_TRANSACTIONDETL) {

				// Declare model of that service
				TransactionDetailModel model = (TransactionDetailModel) Result;


				// create list for pull data from model
				List<Tdetail> list = model.getUserAccountDetails();


				int countTransaction = model.getListSize();

				textViewName.setText("" + countTransaction);

				//Log.e("TAG", "SUCCESS::::::::::::::::::::::::");

				// set custom adaptor (GridAdaptor) for grid view
				listTransaction.setAdapter(new MonitorAdapter(this,
						R.layout.item_user, list));
				String EmergencyMessage = model.getUserAccountDetails().get(0)
						.getEmergencyMessage();

				try {
					Emergency.desAct.finish();
				} catch (Exception e) {
				}

				if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
					Intent rt = new Intent(Transaction.this, Emergency.class);
					rt.putExtra("Emergency", EmergencyMessage);
					startActivity(rt);

				}

			}

			if (id == webservice.TYPE_TRAN_DETAILS_BY_DATE) {

				// Declare model of that service
				TransactionListByDate model1 = (TransactionListByDate) Result;


				// create list for pull data from model
				List<TdetailDate> list = model1
						.getUserTransactionDetailsByDate();

				int countTransactionDat = model1.getListSize();
				if(countTransactionDat>0){
					modelforPdf=model1;

					noRecord.setVisibility(View.GONE);
					textViewName.setText("" + countTransactionDat);
					// //Log.e("TAG","SUCCESS::::::::::::::::::::::::"+list.get(0).getBalance());

					// set custom adaptor (GridAdaptor) for grid view
					listTransaction.setAdapter(new MonitorAdapterDate(this,
							R.layout.item_list_search, list));
					String EmergencyMessage = model1
							.getUserTransactionDetailsByDate().get(0)
							.getEmergencyMessage();

					try {
						Emergency.desAct.finish();
					} catch (Exception e) {
					}

					if (EmergencyMessage != null
							&& !(EmergencyMessage.equalsIgnoreCase("NO"))) {
						Intent rt = new Intent(Transaction.this,
								Emergency.class);
						rt.putExtra("Emergency", EmergencyMessage);
						startActivity(rt);

					}
				}else{
					noRecord.setVisibility(View.VISIBLE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onError(String Error, int id) {
		progressBar.setVisibility(View.GONE);
		Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT)
				.show();
		

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

	@Override
	public void onBackPressed() {
		finish();
	}

	public String getCurrentDate(int finddate) {

		/****************** 1 *******************/
		//Log.w("TAG:DATE_2:", "*******************2******************");

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		cal.add(Calendar.DAY_OF_YEAR, finddate);
		Date newDate = cal.getTime();

		String date = dateFormat.format(newDate);

		//Log.w("TAG:DATE_3:", "" + date);

		/*************************************/

		return date;

	}





}
