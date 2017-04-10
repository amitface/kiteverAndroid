package sms19.listview.newproject.fragment;

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

import sms19.listview.adapter.ReportMailAdapter;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.DeliveryReport;
import sms19.listview.newproject.model.ReportMailModel;
import sms19.listview.webservice.Method;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class SendMailReportFragment extends Fragment implements
		ServiceHitListener, OnClickListener, ReportSmsMailInterface {

	private ProgressDialog p;
	private String UserId = "";
	private ListView listSMSREports;
	private DataBaseDetails dbObject = null;
	private String Mobile = "";
	private ImageView imgDownload, backbutton;

	private String ListData[];
	private ArrayList<String> listData;

	// file regards
	private String dataGood = "", ss;
	boolean dataimage = false, CheckDownloadStatus = true;
	private Context gContext;
	private TextView smsrecords;

	// for response from server
	private String dateT = "", mobile = "";
	private String message = "", status = "", dateTime = "";
	private TextView dateTxt, noRecord, pdfView;
	private static final int DIALOG_PICK_DATETIME_ID = 0;
	private int year, month, day;
	private ProgressBar progressBar;
	private StringBuilder dateVal;
	private int statusApi;
	private TextView mail_textMObilew,mail_textMessagew,mail_textStausw,mail_textDate2w,status_understand_mailtxt;
	private ReportMailModel modelforPdf;

	public static SendMailReportFragment newIntance() {
		SendMailReportFragment fragment = new SendMailReportFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View convertView = inflater.inflate(R.layout.fragment_mail_report,
				container, false);
		statusApi = 0;
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		dbObject = new DataBaseDetails(getContext());
		listSMSREports = (ListView) convertView
				.findViewById(R.id.mail_ReportslistView);

		imgDownload = (ImageView) convertView
				.findViewById(R.id.mail_imgDownload);

		noRecord = (TextView) convertView.findViewById(R.id.mail_no_record);

		dateTxt = (TextView) convertView.findViewById(R.id.mail_txt_date);

		mail_textMObilew = (TextView) convertView.findViewById(R.id.mail_textMObilew);
		mail_textMessagew = (TextView) convertView.findViewById(R.id.mail_textMessagew);
		mail_textStausw = (TextView) convertView.findViewById(R.id.mail_textStausw);
		mail_textDate2w = (TextView) convertView.findViewById(R.id.mail_textDate2w);
		status_understand_mailtxt= (TextView) convertView.findViewById(R.id.status_understand_mailtxt);

		setRobotoThinFont(mail_textMObilew,getActivity());
		setRobotoThinFont(mail_textMessagew,getActivity());
		setRobotoThinFont(mail_textStausw,getActivity());
		setRobotoThinFont(mail_textDate2w,getActivity());
		setRobotoThinFont(dateTxt,getActivity());
		setRobotoThinFont(status_understand_mailtxt,getActivity());


		mail_textMObilew.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		mail_textMessagew.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		mail_textStausw.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		mail_textDate2w.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
		dateTxt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


		progressBar = (ProgressBar) convertView
				.findViewById(R.id.mail_progressBar_id);
		pdfView = (TextView) convertView.findViewById(R.id.mail_pdf_view);

		LinearLayout statusUnderstand = (LinearLayout) convertView
				.findViewById(R.id.mail_status_understand);
		statusUnderstand.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
		statusUnderstand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openDialog();
			}
		});
		smsrecords = (TextView) convertView.findViewById(R.id.mail_records);


		dateVal = new StringBuilder().append(day).append("/").append(month + 1)
				.append("/").append(year).append(" ");
		if (dateVal != null && dateVal.length() > 0) {
			// callWebService(dateVal);
		}
		imgDownload.setOnClickListener(this);
		if (android.os.Build.VERSION.SDK_INT >= 19)
			imgDownload.setVisibility(View.VISIBLE);
		else
			imgDownload.setVisibility(View.GONE);
		dateTxt.setText(dateVal);
		dateTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog dpicker=new DatePickerDialog(getActivity(), pickerListener, year,month, day);
				dpicker.getDatePicker().setMaxDate(new Date().getTime());
				dpicker.show();


			}
		});

		gContext = getActivity();
		try {
			p.setCancelable(true);
		} catch (Exception e) {

		}
		return convertView;
	}

	private void openDialog() {
		final Dialog dialog = new Dialog(getActivity());
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

		new webservice(null, webservice.FetchDeliveryReportMailApi.geturl(
				date.toString(), UserId), webservice.TYPE_GET,
				webservice.TYPE_MAIL_REPORT, this);
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
			Date date = null;
			try {
				date = format.parse(dateVal.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long millisecondsSelectedTime = date.getTime();
			Calendar calendar = Calendar.getInstance();
			long millisecondscurrentTime = calendar.getTimeInMillis();
			if (millisecondsSelectedTime > millisecondscurrentTime) {
				Toast.makeText(getActivity(), "You cannot select before date",
						Toast.LENGTH_LONG).show();
			} else {
				long daysSelected, daysCurrent;
				daysSelected = TimeUnit.MILLISECONDS
						.toDays(millisecondsSelectedTime);
				daysCurrent = TimeUnit.MILLISECONDS
						.toDays(millisecondscurrentTime);
				if ((daysCurrent - daysSelected) > 30) {
					Toast.makeText(getActivity(),
							"You cannot select more than 30 days",
							Toast.LENGTH_LONG).show();
				} else {
					dateTxt.setText(dateVal);
					if (dateVal != null && dateVal.length() > 0) {
						callWebService(dateVal);
					}
				}
			}
		}
	};

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
	public void onSuccess(Object Result, int id) {
		progressBar.setVisibility(View.GONE);
		listData = new ArrayList<String>();
		// Toast.makeText(getActivity(), "Mail ", Toast.LENGTH_SHORT).show();
		if (id == webservice.TYPE_MAIL_REPORT) {
			ReportMailModel model = (ReportMailModel) Result;
			System.out.println("" + model.getDeliveryReport().toString());
			if (model != null) {
				List<DeliveryReport> gh = model.getDeliveryReport();

				int len = gh.size();

				if (gh.size() > 0) {
					modelforPdf = model;
					noRecord.setVisibility(View.GONE);
					listSMSREports.setVisibility(View.VISIBLE);
					dataimage = false;

					smsrecords.setText("" + len);
					listSMSREports.setAdapter(new ReportMailAdapter(
							getActivity(), R.layout.listsmsreports, gh));
					for (int i = 0; i < len; i++) {

						try {
							mobile = "-------------------------------" + "\n\n"
									+ "Reply To:"
									+ gh.get(i).getReplyTo().trim();
							message = "Message:"
									+ gh.get(i).getMessage().trim();
							status = "Status:" + gh.get(i).getStatus().trim();
							dateTime = "DateTime:"
									+ gh.get(i).getMessage().trim() + "\n"
									+ "-------------------------------" + "\n";
							dateT = gh.get(i).getDateTime().trim();

						} catch (Exception e) {
							e.printStackTrace();
						}

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
					listSMSREports.setVisibility(View.GONE);
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
		}
	}

	@Override
	public void onError(String Error, int id) {
		// TODO Auto-generated method stub

		try {
			progressBar.setVisibility(View.GONE);

			p.dismiss();
		} catch (Exception e) {

		}

		Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.mail_imgDownload: {

			new AlertDialog.Builder(gContext)
					.setCancelable(false)
					.setMessage(
							"Are you sure to save it to file as pdf.")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									CheckDownloadStatus = false;
									dialog.cancel();
									createPdf();
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
		}
			break;

		default:
			break;
		}

	}

	public void createPdf() {
		{
			String text = "";
			if (modelforPdf.getDeliveryReport() != null) {
				for (int i = 0; i < modelforPdf.getDeliveryReport().size(); i++) {

					try {
						String mobile = "Mobile : "
								+ modelforPdf.getDeliveryReport().get(i)
										.getReplyTo().trim() + "\n";
						text = text + mobile;
						String message = "Message : "
								+ modelforPdf.getDeliveryReport().get(i)
										.getMessage().trim() + "\n";
						text = text + message;
						String status = modelforPdf.getDeliveryReport().get(i)
								.getStatus().trim();
						if (status.equalsIgnoreCase("0")) {
							text = text + "Status : Delivered" + "\n";
						} else if (status.equalsIgnoreCase("1")) {
							text = text + "Status : Undelivered" + "\n";
						} else if (status.equalsIgnoreCase("2")) {
							text = text + "Status : Sent for delivery" + "\n";
						}
						String dateTime = modelforPdf.getDeliveryReport()
								.get(i).getDateTime().trim();

						// // String dateT = "Date : "
						// // + modelforPdf.getDeliveryReport().get(i)
						// // .get().trim()
						// // + "\n";
						// text = text + dateT + "\n\n";

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
			long current_time =System.currentTimeMillis();

			File file = new File(dir, "MailReport_"+current_time+".pdf");
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
		new AlertDialog.Builder(gContext)
				.setCancelable(false)
				.setMessage("Pdf saved successfully in "+file.toString() + " do you want to open it? ")
				.setPositiveButton("Open now",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								CheckDownloadStatus = false;
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
								CheckDownloadStatus = true;
								dialog.cancel();
							}
						}).show();
	}

	@Override
	public void changeServiceRequest() {
		// TODO Auto-generated method stub
		if (dateVal != null && dateVal.length() > 0 && statusApi == 0) {
			statusApi = 1;
			callWebService(dateVal);
		}
	}
}
