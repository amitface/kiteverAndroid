package com.kitever.sendsms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.imoze.EmojiconEditText;
import sms19.inapp.msg.imoze.EmojiconsFragment;
import sms19.inapp.msg.imozemodel.Emojicon;
import sms19.inapp.msg.rest.Rest;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.EmailTemplateActivity;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.newproject.model.FetchGroupDetails.GroupDetails;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.customviews.DateTimePickerDialog;
import com.kitever.models.Balance;
import com.kitever.models.ContactInfo;
import com.kitever.models.LoginInfo;
import com.kitever.utils.DateHelper;
import com.kitever.utils.Utils;

@SuppressWarnings("deprecation")
public class SendSmsActivityWithEmail extends ActionBarActivity implements
		sms19.inapp.msg.imoze.EmojiconGridFragment.OnEmojiconClickedListener,
		EmojiconsFragment.OnEmojiconBackspaceClickedListener {

	private static final int SELECT_CONTACTS_REQUEST_CODE = 101;

	private static final int SELECT_TEMPLATE_REQUEST_CODE = 102;

	private static final int SELECT_EMAIL_TEMPLATE_REQUEST_CODE = 105;

	private static final int SELECT_MANUALLY_REQUEST_CODE = 103;

	private static final int FROM_SEND_SMS_MODULE = 104;

	private DataBaseDetails dbObject = null;

	private LoginInfo loginInfo = null;

	private LinearLayout valueLinLayout, manualNoLinLayout;
	private TextView groupLabelTextView, contactsLabelTextView, timeTextView;

	private ListView mListView;

	// private ArrayList<String> manualArrayList = new ArrayList<String>();
	// private TagView groupTagView;

	private ArrayList<GroupDetails> groupNameTagList = new ArrayList<GroupDetails>();
	private ArrayList<String> broadcastGroupId = new ArrayList<String>();

	private ArrayList<String> groupsArrayList = new ArrayList<String>();

	private ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();

	private AlertDialog.Builder groupBuilder = null;

	private ArrayList<ContactInfo> selectedContactsList = null;

	private long datetime;
	private long smsDateTime;
	private long emailDateTime;

	private static final int DIALOG_PICK_DATETIME_ID = 0;
	private final int DIALOG_PICK_EMAIL_DATETIME_ID = 1;
	private final int DIALOG_PICK_SMS_DATETIME_ID = 2;
	private String templateIdEmail = "";
	private static final String STATE_DATE_STARTED = "SendSmsActivity.datetime";

	protected boolean isCleanStart;
	// Hack - makes any subsequent calls to show the same DateTimePickerDialog
	// to reset dialog's datetime to the current shown in datetimeTextView.
	// Otherwise on the second call to show DateTimePickerDialog for the same
	// Activity instance the dialog will not be reinitialized with the current
	// datetime seen in the datetimeTextView.
	private boolean resetDateTimeDialog;

	private ImageButton setTimeButton;

	private EmojiconEditText editText;

	private ProgressDialog progressDialog = null;

	private SendSMSContactsAdapter adapter = null;
	private String USERID = "";
	private ImageView emailTimeSelect, smsTimeSelect;
	private TextView userEmailCredit, userSmsCreadit;
	private CheckBox emailCheckBox, smsCheckBox;

	private ArrayList<ContactInfo> manualNumArrayList = new ArrayList<>();

	// private TextView balValueTextView;

	private RelativeLayout emojicons;
	private EmojiconsFragment emojiconsFragment = null;
	private ImageView mEmojiBtn, deleteBtn;

	private CheckBox unicodeCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendsms);

		final ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(Utils.setActionBarBackground());
		bar.setTitle(Utils.setActionBarTextAndColor("Send Message"));
		bar.setDisplayHomeAsUpEnabled(true);

		showProgressDialog();
		SharedPreferences chatPrefs = getSharedPreferences("chatPrefs",
				MODE_PRIVATE);
		USERID = chatPrefs.getString("userId", "");
		isCleanStart = (savedInstanceState == null);
		if (isCleanStart) {
			datetime = System.currentTimeMillis();
		} else {
			datetime = savedInstanceState.getLong(STATE_DATE_STARTED);
		}

		// balValueTextView = (TextView) findViewById(R.id.balValueTextView);

		final LinearLayout groupsLinearLayout = (LinearLayout) findViewById(R.id.groupsLinearLayout);
		final LinearLayout manualLinearLayout = (LinearLayout) findViewById(R.id.manualLinearLayout);
		emailTimeSelect = (ImageView) findViewById(R.id.email_time_select);
		smsTimeSelect = (ImageView) findViewById(R.id.time_select);
		userEmailCredit = (TextView) findViewById(R.id.user_email_creadit);
		userSmsCreadit = (TextView) findViewById(R.id.user_sms_creadit);
		emailCheckBox = (CheckBox) findViewById(R.id.email_check_box);
		smsCheckBox = (CheckBox) findViewById(R.id.check_box);
		smsCheckBox.setChecked(false);
		emailCheckBox.setChecked(false);

		userEmailCredit.setText("Use my email credits to send emails");

		userSmsCreadit.setText("Use my sms credits to send sms");

		manualLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SendSmsActivityWithEmail.this,
						AddContactManuallyActivity.class);
				// intent.putStringArrayListExtra("LIST",manualArrayList);
				intent.putExtra("LIST", manualNumArrayList);
				startActivityForResult(intent, SELECT_MANUALLY_REQUEST_CODE);
			}
		});

		TextView topUp = (TextView) findViewById(R.id.top_up);
		topUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder alert = new AlertDialog.Builder(
						SendSmsActivityWithEmail.this);
				alert.setTitle("TopUp");
				WebView wv = new WebViewHelper(SendSmsActivityWithEmail.this);
				wv.clearHistory();
				wv.getSettings().setJavaScriptEnabled(true);
				wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
				String Pass = sms19.inapp.msg.constant.Utils
						.getPassword(SendSmsActivityWithEmail.this);
				String postData = "password=" + Pass;// &id=236";
				wv.postUrl(
						"http://kitever.com/BuyCredit.aspx?tab=topup&userid="
								+ USERID,
						EncodingUtils.getBytes(postData, "BASE64"));

				// wv.loadUrl("http://kitever.com/BuyCredit.aspx?tab=plans&userid="
				// + UserId);
				// wv.setWebViewClient(new WebViewClient());
				WebClientClass webViewClient = new WebClientClass();
				wv.setWebViewClient(webViewClient);

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

		groupLabelTextView = (TextView) findViewById(R.id.groupLabelTextView);
		contactsLabelTextView = (TextView) findViewById(R.id.contactsLabelTextView);
		timeTextView = (TextView) findViewById(R.id.timeTextView);

		groupsLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (groupsArrayList != null) {
					if (groupsArrayList.size() == 0) {
						Toast.makeText(getApplicationContext(),
								"No group(s) exists", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				} else {
					return;
				}

				if (groupBuilder == null) {
					groupBuilder = getGroupDialog();
					groupBuilder.show();
				} else {
					groupBuilder.show();
				}

			}
		});

		// groupTagView = (TagView) findViewById(R.id.tag_group);
		valueLinLayout = (LinearLayout) findViewById(R.id.valueLinLayout);
		valueLinLayout.setVisibility(View.GONE);

		manualNoLinLayout = (LinearLayout) findViewById(R.id.manualNoLinLayout);
		manualNoLinLayout.setVisibility(View.GONE);

		mListView = (ListView) findViewById(R.id.listView);
		mListView.setVisibility(View.GONE);

		final LinearLayout contactLinearLayout = (LinearLayout) findViewById(R.id.contactLinearLayout);

		contactLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (loginInfo != null) {
					if (loginInfo.uid != null) {
						Intent intent = new Intent(
								SendSmsActivityWithEmail.this,
								SelectContactsActivity.class);
						intent.putExtra("FROM", FROM_SEND_SMS_MODULE);
						intent.putExtra("userId", loginInfo.uid);
						startActivityForResult(intent,
								SELECT_CONTACTS_REQUEST_CODE);
					}
				}
			}
		});

		final ImageButton templateButton = (ImageButton) findViewById(R.id.templateButton);

		editText = (EmojiconEditText) findViewById(R.id.editText);
		editText.setUseSystemDefault(false);

		final String text = getIntent().getStringExtra("templateText");

		if (text != null) {
			editText.setText(text);
		}

		final TextView charCountTextView = (TextView) findViewById(R.id.charCountTextView);

		/*
		 * int maxLength = 2000; InputFilter[] fArray = new InputFilter[1];
		 * fArray[0] = new InputFilter.LengthFilter(maxLength);
		 * tv.setFilters(fArray);
		 */

		final InputFilter[] limit2000Array = new InputFilter[] { new InputFilter.LengthFilter(
				2000) };

		final InputFilter[] limit871Array = new InputFilter[] { new InputFilter.LengthFilter(
				871) };

		final TextWatcher mTextEditorWatcher = new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// This sets a textview to the current length
				if (unicodeCheckBox.isChecked()) {
					charCountTextView.setText(String.valueOf(s.length())
							+ "/871");
					editText.setFilters(limit871Array);
				} else {
					charCountTextView.setText(String.valueOf(s.length())
							+ "/2000");
					editText.setFilters(limit2000Array);
				}
			}

			public void afterTextChanged(Editable s) {

			}
		};

		editText.addTextChangedListener(mTextEditorWatcher);

		templateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectEmailSmsTemplate();
				// if (emailCheckBox.isChecked()) {
				// Intent intent = new Intent(SendSmsActivity.this,
				// EmailTemplateActivity.class);
				// startActivityForResult(intent,
				// SELECT_EMAIL_TEMPLATE_REQUEST_CODE);
				// } else {
				// Intent id = new Intent(SendSmsActivity.this,
				// TemplateActivity.class);
				// id.putExtra("taketemplate", "send");
				// startActivityForResult(id, SELECT_TEMPLATE_REQUEST_CODE);
				// }
			}
		});

		setTimeButton = (ImageButton) findViewById(R.id.setTimeButton);

		setTimeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// hack, see details near the field declaration (at the top of
				// the class)
				resetDateTimeDialog = true;

				showDialog(DIALOG_PICK_DATETIME_ID);

			}
		});

		mEmojiBtn = (ImageView) findViewById(R.id.imozebtn);
		deleteBtn = (ImageView) findViewById(R.id.deletebtn);
		deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				templateIdEmail = "";
				editText.setText("");
				editText.setEnabled(true);
				deleteBtn.setVisibility(View.GONE);
				mEmojiBtn.setVisibility(View.VISIBLE);
				smsCheckBox.setEnabled(true);
			}
		});
		mEmojiBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				sms19.inapp.msg.constant.Utils.hideKeyBoardMethod(
						getApplicationContext(), mEmojiBtn);

				setEmojiconFragment(false);
			}
		});

		emojicons = (RelativeLayout) findViewById(R.id.emojicons);

		final ImageView sendButton = (ImageView) findViewById(R.id.sendButton);

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (emailCheckBox.isChecked() || smsCheckBox.isChecked()) {
					showProgressDialog();
				}
				final String txt_message = editText.getText().toString();

				if (txt_message.trim().equals("")) {
					removeProgressDialog();
					Toast.makeText(getApplicationContext(),
							"Please enter some text to send", Toast.LENGTH_LONG)
							.show();
					return;
				} else if (txt_message.trim().length() > 2000) {
					removeProgressDialog();
					Toast.makeText(
							getApplicationContext(),
							"Message length cannot exceed more than 2000 character",
							Toast.LENGTH_LONG).show();
					return;
				}

				final ArrayList<ContactInfo> contactInfoArrayList = new ArrayList<>();

				if (selectedContactsList != null) {
					contactInfoArrayList.addAll(selectedContactsList);
				}

				if (manualNumArrayList != null) {
					contactInfoArrayList.addAll(manualNumArrayList);
				}

				boolean isContactSelected = false;
				boolean isGroupSelected = false;
				String selectedIndividualNumber = "";
				final StringBuffer mobileString = new StringBuffer();

				final StringBuffer groupString = new StringBuffer();
				int count = 0;
				if (contactInfoArrayList != null) {
					for (ContactInfo contactInfo : contactInfoArrayList) {
						if (contactInfo.isSelected) {
							isContactSelected = true;
							if (count == 0) {
								selectedIndividualNumber = selectedIndividualNumber
										+ contactInfo.countryCode
										+ contactInfo.Contact_Mobile
										+ "@email19.in";
							} else {
								selectedIndividualNumber = selectedIndividualNumber
										+ ","
										+ contactInfo.countryCode
										+ contactInfo.Contact_Mobile;
							}
							count++;
							String countryCode = contactInfo.countryCode;
							if (countryCode.startsWith("+")) {
								countryCode = countryCode.substring(1,
										countryCode.length());
							}
							mobileString.append(countryCode
									+ contactInfo.Contact_Mobile + ",");
						}
					}
				}

				String mobiStr = "";

				if (mobileString.length() > 1) {
					mobiStr = mobileString.substring(0,
							mobileString.length() - 1);
				}

				// +919990305371,+919768583806,+919950099500,+919928151819,+919013849991,+919414390319,
				// 99953816162,98744911357,98130993707,99928151819,99783859589
				String broadcastId = "";
				for (int i = 0; i < mSelectedItems.size(); i++) {
					isGroupSelected = true;
					GroupDetails groupDetails = groupNameTagList
							.get(mSelectedItems.get(i));
					if (i > 0) {
						broadcastId = broadcastId + ","
								+ groupDetails.Group_JID;
					} else {
						broadcastId = groupDetails.getGroup_JID();
					}
					groupString.append(groupDetails.group_name + ",");
				}

				// abc,hi,jjjjjjjj,kkkkkkkk,

				String groupStr = "";

				if (groupString.length() > 1) {
					groupStr = groupString.substring(0,
							groupString.length() - 1);
				}

				final String templateID = "";

				if (!isContactSelected && !isGroupSelected) {
					removeProgressDialog();
					Toast.makeText(getApplicationContext(),
							"Please select atleast one contact or group",
							Toast.LENGTH_LONG).show();
					return;
				}

				String date = null, time = null;
				String minInterval = null;
				if (!emailCheckBox.isChecked() && !smsCheckBox.isChecked()) {
					Toast.makeText(SendSmsActivityWithEmail.this,
							"Please select sms/email", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (emailCheckBox.isChecked()) {
					if (!userEmailCredit.getText().equals(
							"Use my email credits to send emails")) {
						long mint = TimeUnit.MILLISECONDS
								.toMinutes(emailDateTime);
						minInterval = mint + "";
					} else {
						// datetime = System.currentTimeMillis();
						// long mint =
						// TimeUnit.MILLISECONDS.toMinutes(datetime);
						minInterval = "0";
					}
					if (groupStr == null || groupStr.equalsIgnoreCase("")) {
						broadcastId = selectedIndividualNumber;
					} else if ((selectedIndividualNumber != null && !selectedIndividualNumber
							.equalsIgnoreCase(""))
							&& (groupStr != null && !groupStr
									.equalsIgnoreCase(""))) {
						broadcastId = broadcastId + ","
								+ selectedIndividualNumber;
					}
					EmailMessageTask asyncTask = new EmailMessageTask(
							SendSmsActivityWithEmail.this, USERID, groupStr,
							broadcastId, txt_message, minInterval,
							templateIdEmail);

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						// check_box.setChecked(false);
						asyncTask
								.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					} else {
						// check_box.setChecked(false);
						asyncTask.execute();
					}
					emailCheckBox.setChecked(false);
					// sendSMSRequest(templateID, txt_message, mobiStr,
					// groupStr,
					// date, time, isUnicode);
				}
				if (smsCheckBox.isChecked()) {
					if (!userSmsCreadit.getText().equals(
							"Use my sms credits to send sms")) {
						// final String dateTime = timeTextView.getText()
						// .toString();
						// if (dateTime.contains(" ")) {
						// final String arrDateTime[] = dateTime.split(" ");
						// date = arrDateTime[0];
						// time = arrDateTime[1] + " " + arrDateTime[2];
						// }
						final String formattedDatetime = DateHelper
								.myFormat2(smsDateTime);
						// log("acceptDatetime: got " + formattedDatetime);
						// SendSmsActivity.this.datetime = datetime;
						// setTimeButton.setText(formattedDatetime);
						if (formattedDatetime.contains(" ")) {
							final String arrDateTime[] = formattedDatetime
									.split(" ");
							date = arrDateTime[0];
							time = arrDateTime[1] + " " + arrDateTime[2];
						}
					} else {
						datetime = System.currentTimeMillis();
						final String formattedDatetime = DateHelper
								.myFormat2(datetime);
						// log("acceptDatetime: got " + formattedDatetime);
						// SendSmsActivity.this.datetime = datetime;
						// setTimeButton.setText(formattedDatetime);
						if (formattedDatetime.contains(" ")) {
							final String arrDateTime[] = formattedDatetime
									.split(" ");
							date = arrDateTime[0];
							time = arrDateTime[1] + " " + arrDateTime[2];
						}
					}

					final int isUnicode = check();

					sendSMSRequest(templateID, txt_message, mobiStr, groupStr,
							date, time, isUnicode);
					smsCheckBox.setChecked(false);
				}

			}
		});

		unicodeCheckBox = (CheckBox) findViewById(R.id.unicodeCheckBox);

		unicodeCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							charCountTextView.setText(String.valueOf(editText
									.getText().length()) + "/871");
							editText.setFilters(limit871Array);
						} else {
							charCountTextView.setText(String.valueOf(editText
									.getText().length()) + "/2000");
							editText.setFilters(limit2000Array);
						}
					}
				});
		emailTimeSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetDateTimeDialog = true;

				showDialog(DIALOG_PICK_EMAIL_DATETIME_ID);
			}
		});
		smsTimeSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetDateTimeDialog = true;

				showDialog(DIALOG_PICK_SMS_DATETIME_ID);
			}
		});
		// setTags();

		initFun();

	}

	private void setEmojiconFragment(boolean useSystemDefault) {

		try {

			if (emojicons != null) {
				emojicons.setVisibility(View.VISIBLE);
				emojicons.removeAllViews();
			}

			emojiconsFragment = EmojiconsFragment.newInstance(useSystemDefault);

			getSupportFragmentManager().beginTransaction()
					.add(R.id.emojicons, emojiconsFragment).commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * private int check(final String str) { int flag = 0; for (int iLetter = 0;
	 * iLetter < str.length(); iLetter++) { char ch = str.charAt(iLetter); if
	 * (Character.UnicodeBlock.of(ch) != Character.UnicodeBlock.BASIC_LATIN) {
	 * 
	 * sms19.inapp.msg.constant.Utils .printLog2("Character.UnicodeBlock.of(" +
	 * ch + ")==" + Character.UnicodeBlock.of(ch) + "");
	 * sms19.inapp.msg.constant.Utils.printLog2(" index== " + iLetter + "");
	 * 
	 * flag = 1; break; } } return flag; }
	 */

	private int check() {
		int flag = 0;
		if (unicodeCheckBox.isChecked()) {
			flag = 1;
		} else {
			flag = 0;
		}
		return flag;
	}

	/*
	 * @Override public void finish() {
	 * 
	 * new Thread(new Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub if
	 * (selectedContactsList != null) { for (ContactInfo info :
	 * selectedContactsList) { info.isSelected = false; } } } }).start();
	 * 
	 * super.finish(); }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(SendSmsActivityWithEmail.this);
			progressDialog.setTitle("Loading");
			progressDialog.setMessage("Please wait...");
			progressDialog.show();
		} else {
			if (!progressDialog.isShowing()) {
				progressDialog.show();
			}
		}
	}

	private void removeProgressDialog() {
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		if (id == DIALOG_PICK_DATETIME_ID) {
			return new DateTimePickerDialog(SendSmsActivityWithEmail.this,
					new DateTimePickerDialog.DateTimeAcceptor() {
						public void accept(long datetime) {
							final String formattedDatetime = DateHelper
									.myFormat2(datetime);
							// log("acceptDatetime: got " + formattedDatetime);
							SendSmsActivityWithEmail.this.datetime = datetime;
							timeTextView.setText(formattedDatetime);
						}
					}, new DateTimePickerDialog.DateTimeDeselector() {

						@Override
						public void deselect() {
							timeTextView.setText("");
						}
					}, datetime);
		} else if (id == DIALOG_PICK_EMAIL_DATETIME_ID) {
			return new DateTimePickerDialog(SendSmsActivityWithEmail.this,
					new DateTimePickerDialog.DateTimeAcceptor() {
						public void accept(long datetime) {
							final String formattedDatetime = DateHelper
									.myFormat2(datetime);
							// log("acceptDatetime: got " + formattedDatetime);
							// SendSmsActivity.this.datetime = datetime;
							emailDateTime = datetime;
							userEmailCredit
									.setText("Use my email credits to send emails on"
											+ " " + formattedDatetime);
						}
					}, new DateTimePickerDialog.DateTimeDeselector() {

						@Override
						public void deselect() {
							// Calendar c = Calendar.getInstance();
							// System.out.println("Current time => " +
							// c.getTime());
							// SimpleDateFormat df = new SimpleDateFormat(
							// "yyyy-MM-dd");
							// String formattedDate = df.format(c.getTime());
							userEmailCredit
									.setText("Use my email credits to send emails");
						}
					}, datetime);
		} else if (id == DIALOG_PICK_SMS_DATETIME_ID) {
			return new DateTimePickerDialog(SendSmsActivityWithEmail.this,
					new DateTimePickerDialog.DateTimeAcceptor() {
						public void accept(long datetime) {
							final String formattedDatetime = DateHelper
									.myFormat2(datetime);
							// log("acceptDatetime: got " + formattedDatetime);
							// SendSmsActivity.this.datetime = datetime;
							smsDateTime = datetime;
							userSmsCreadit
									.setText("Use my sms credits to send sms"
											+ " " + formattedDatetime);
						}
					}, new DateTimePickerDialog.DateTimeDeselector() {

						@Override
						public void deselect() {
							userSmsCreadit
									.setText("Use my sms credits to send sms");
						}
					}, datetime);
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		if (id == DIALOG_PICK_DATETIME_ID) {
			if (resetDateTimeDialog) {
				resetDateTimeDialog = false;
				((DateTimePickerDialog) dialog).resetDatetime(datetime);
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(STATE_DATE_STARTED, datetime);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == SELECT_CONTACTS_REQUEST_CODE) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				// The user picked a contact.
				// The Intent's data Uri identifies which contact was selected.
				setSelectedContacts();
			}
		}

		if (requestCode == SELECT_TEMPLATE_REQUEST_CODE) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				editText.setText(data.getStringExtra("templateText"));
			}
		}
		if (requestCode == SELECT_EMAIL_TEMPLATE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String templateTitle = data.getStringExtra("TemplateTitle");
				templateIdEmail = data.getStringExtra("TemplateId");
				editText.setText("Template: " + templateTitle);
				editText.setEnabled(false);
				deleteBtn.setVisibility(View.VISIBLE);
				mEmojiBtn.setVisibility(View.GONE);
				// if(smsCheckBox.isChecked())
				{
					smsCheckBox.setChecked(false);
					smsCheckBox.setEnabled(false);
					if (smsCheckBox.isChecked()) {
						Toast.makeText(SendSmsActivityWithEmail.this,
								"Selected template can be sent to mail only",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}

		if (requestCode == SELECT_MANUALLY_REQUEST_CODE) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {

				// final ArrayList<String>
				// arrayList=data.getStringArrayListExtra("MANUAL_NUM_LIST");

				@SuppressWarnings("unchecked")
				final ArrayList<ContactInfo> arrayList = (ArrayList<ContactInfo>) data
						.getSerializableExtra("MANUAL_NUM_LIST");

				if (arrayList != null) {
					if (arrayList.size() >= 0) {
						// manualArrayList.clear();
						// manualArrayList.addAll(arrayList);
						manualNumArrayList.clear();
						manualNumArrayList.addAll(arrayList);
						setManualNumbers();
					}
				}

			}
		}

	}

	private void setSelectedContacts() {
		if (SendSmsContactsSingleton.getInstance() == null) {
			// do nothing
		} else {
			if (SendSmsContactsSingleton.getInstance().getContactInfos() == null) {
				// do nothing
			} else {
				new Thread(new Runnable() {
					@Override
					public void run() {

						if (selectedContactsList != null) {
							selectedContactsList.clear();
						} else {
							selectedContactsList = new ArrayList<ContactInfo>();
						}

						final ArrayList<ContactInfo> contactInfos = SendSmsContactsSingleton
								.getInstance().getContactInfos();
						for (ContactInfo contactInfo : contactInfos) {
							if (contactInfo.isSelected) {
								selectedContactsList.add(contactInfo);
							}
						}

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								contactsLabelTextView
										.setText("Select your contacts" + " ("
												+ selectedContactsList.size()
												+ ")");

								if (selectedContactsList.size() == 0) {
									mListView.setVisibility(View.GONE);
								} else {
									mListView.setVisibility(View.VISIBLE);
								}

								adapter = new SendSMSContactsAdapter(
										SendSmsActivityWithEmail.this,
										selectedContactsList);
								mListView.setAdapter(adapter);

							}
						});

					}
				}).start();

			}
		}

	}

	private void initFun() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				dbObject = new DataBaseDetails(SendSmsActivityWithEmail.this);

				fetchLoginDetails();

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						totalavailablebalance();
						getGroupDetails();

					}
				});

			}
		}).start();
	}

	// total balance available in user account fetch from server
	private void totalavailablebalance() {

		final String url = webservice.getCurrentBalance.geturl(loginInfo.uid);

		StringRequest strreq = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String Response) {

						try {
							JSONObject jsonObject = new JSONObject(Response);

							JSONArray array = jsonObject
									.getJSONArray("UserTopTransactionDetails");

							JSONObject jsonObject2 = array.getJSONObject(0);

							Gson gson = new Gson();

							Balance balance = gson.fromJson(
									jsonObject2.toString(), Balance.class);

							// final String balSms_Str =
							// (String)jsonObject2.getString("balance");

							final String balSms_Str = balance.balance;

							if (balSms_Str != null) {
								if (!balSms_Str.equals("")) {
									((TextView) findViewById(R.id.balValueTextView))
											.setText(balSms_Str);
								}
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError e) {
						e.printStackTrace();
					}
				});

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(strreq);

	}

	private void sendSMSRequest(final String templateID,
			final String txt_message, final String mobileString,
			final String groupString, final String date, final String time,
			final int unicode) {

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				Apiurls.getBasePostURL(), new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						// sms19.inapp.msg.constant.Utils.printLog(response);

						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(response);
							if (jsonObject != null) {
								JSONArray array = jsonObject
										.getJSONArray("SheduledSms");
								JSONObject jsonObject2 = array.getJSONObject(0);

								String msg = jsonObject2.getString("Msg");

								resetValues();

								Toast.makeText(getApplicationContext(), msg,
										Toast.LENGTH_LONG).show();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							if (e.getMessage() != null) {
								Toast.makeText(getApplicationContext(),
										e.getMessage(), Toast.LENGTH_LONG)
										.show();
							}
							e.printStackTrace();
						}

						// sms19.inapp.msg.constant.Utils.printLog2(response);

						removeProgressDialog();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(SendSmsActivityWithEmail.this,
								error.toString(), Toast.LENGTH_LONG).show();

						removeProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();

				params.put("Page", "SheduledSms");
				params.put("TempId", templateID);
				params.put("Mobile", loginInfo.userlogin);
				params.put("Password", loginInfo.password);
				// LoginInfo [id=null, mobile=7840002738, status=null, uid=20,
				// userlogin=+917840002738, balance=null, password=49399,
				// LoginType=null]
				params.put("UserId", loginInfo.uid);
				params.put("SenderId", "SMSSMS");
				params.put("recipient", mobileString);
				params.put("msgcontent", txt_message);

				params.put("usermobile", loginInfo.userlogin);
				params.put("GroupName", groupString);
				params.put("RecipientUserID", "");

				params.put("sheduledtime", time);
				params.put("sheduleddate", date);

				params.put("isUnicode", unicode + "");

				return params;
			}

		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}

	private void resetValues() {

		totalavailablebalance();

		editText.setText("");

		valueLinLayout.removeAllViews();

		mSelectedItems.clear();

		manualNumArrayList.clear();

		manualNoLinLayout.removeAllViews();

		mListView.setVisibility(View.GONE);

		groupBuilder = null;

		contactsLabelTextView.setText("Select your contacts" + " (" + 0 + ")");

		groupLabelTextView.setText("Select your groups (0)");

		valueLinLayout.setVisibility(View.GONE);

		if (selectedContactsList != null) {
			for (ContactInfo contactInfo : selectedContactsList) {
				contactInfo.isSelected = false;
			}
		}

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	private void getGroupDetails() {
		new webservice(null,
				webservice.GetAllGroupDetails.geturl(loginInfo.uid),
				webservice.TYPE_GET, webservice.TYPE_GET_GROUP_DETAILS,
				new ServiceHitListener() {

					@Override
					public void onSuccess(Object Result, int id) {

						if (id == webservice.TYPE_GET_GROUP_DETAILS) {

							FetchGroupDetails Model = (FetchGroupDetails) Result;

							final List<GroupDetails> list = Model
									.getGroupDetails();

							groupNameTagList.clear();
							groupsArrayList.clear();

							// groupNameTagList.addAll(list);

							final int size = list.size();

							for (int i = 0; i < size; i++) {

								final String noOfContacts = list.get(i).NoOfContacts;
								String groupId = list.get(i).getGroup_JID();
								if (noOfContacts != null) {
									if (!noOfContacts.trim().equals("0")) {

										groupNameTagList.add(list.get(i));
										broadcastGroupId.add(groupId);
										groupsArrayList.add(list.get(i).group_name
												+ " ("
												+ list.get(i).NoOfContacts
												+ ")");
									}
								}

							}

						}

						removeProgressDialog();

					}

					@Override
					public void onError(String Error, int id) {

						removeProgressDialog();
						Toast.makeText(getApplicationContext(), Error,
								Toast.LENGTH_SHORT).show();

					}
				});
	}

	public void fetchLoginDetails() {
		dbObject.Open();

		Cursor c = dbObject.getLoginDetails();
		loginInfo = new LoginInfo();

		while (c.moveToNext()) {
			loginInfo.userlogin = c.getString(6);
			loginInfo.mobile = c.getString(1);
			loginInfo.uid = c.getString(3);
			loginInfo.password = c.getString(5);
		}

		dbObject.close();

	}

	private AlertDialog.Builder getGroupDialog() {
		AlertDialog.Builder groupBuilder = new AlertDialog.Builder(
				SendSmsActivityWithEmail.this);
		// Set the dialog title

		String[] groupArr = new String[groupsArrayList.size()];
		boolean[] boolArr = new boolean[groupsArrayList.size()];
		groupArr = groupsArrayList.toArray(groupArr);

		groupBuilder
				.setTitle("Displaying groups with 1 or more contacts")
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected
				.setMultiChoiceItems(groupArr, boolArr,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								if (isChecked) {
									// If the user checked the item, add it to
									// the selected items
									mSelectedItems.add(which);
								} else if (mSelectedItems.contains(which)) {
									// Else, if the item is already in the
									// array, remove it
									mSelectedItems.remove(Integer
											.valueOf(which));
								}
							}
						})
				// Set the action buttons
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK, so save the mSelectedItems
						// results somewhere
						// or return them to the component that opened
						// the dialog
						setTags();
					}
				})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

							}
						});

		return groupBuilder;

	}

	private void setManualNumbers() {

		if (manualNumArrayList != null) {
			if (manualNumArrayList.size() > 0) {
				manualNoLinLayout.removeAllViews();
				manualNoLinLayout.setVisibility(View.VISIBLE);
				for (int i = 0; i < manualNumArrayList.size(); i++) {
					final TextView textView = new TextView(
							SendSmsActivityWithEmail.this);
					textView.setId(i);
					textView.setTextColor(Color.parseColor("#006966"));
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							new LayoutParams(LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT));

					params.setMargins(8, 8, 8, 8);
					textView.setPadding(16, 16, 16, 16);
					textView.setLayoutParams(params);

					final int sdk = android.os.Build.VERSION.SDK_INT;

					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						textView.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.roundcorner_mychatbg));
					} else {
						textView.setBackground(getResources().getDrawable(
								R.drawable.roundcorner_mychatbg));
					}

					textView.setText(manualNumArrayList.get(i).countryCode
							+ manualNumArrayList.get(i).Contact_Mobile);

					manualNoLinLayout.addView(textView);

				}
			} else {
				manualNoLinLayout.setVisibility(View.GONE);
			}
		} else {
			manualNoLinLayout.setVisibility(View.GONE);
		}

	}

	private void setTags() {

		valueLinLayout.removeAllViews();

		if (mSelectedItems != null) {
			groupLabelTextView.setText("Select your groups" + " ("
					+ mSelectedItems.size() + ")");
			if (mSelectedItems.size() == 0) {
				valueLinLayout.setVisibility(View.GONE);
			} else {
				valueLinLayout.setVisibility(View.VISIBLE);
			}
		} else {
			groupLabelTextView.setText("Select your groups (0)");
			valueLinLayout.setVisibility(View.GONE);
		}

		for (int i = 0; i < mSelectedItems.size(); i++) {
			final TextView textView = new TextView(
					SendSmsActivityWithEmail.this);
			textView.setId(i);
			textView.setTextColor(Color.parseColor("#006966"));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));

			params.setMargins(8, 8, 8, 8);
			textView.setPadding(16, 16, 16, 16);
			textView.setLayoutParams(params);

			final int sdk = android.os.Build.VERSION.SDK_INT;

			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				textView.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.roundcorner_mychatbg));
			} else {
				textView.setBackground(getResources().getDrawable(
						R.drawable.roundcorner_mychatbg));
			}
			textView.setText(groupsArrayList.get(mSelectedItems.get(i)));

			valueLinLayout.addView(textView);
		}

	}

	@Override
	public void onEmojiconBackspaceClicked(View v) {
		// TODO Auto-generated method stub
		EmojiconsFragment.backspace(editText);
	}

	@Override
	public void onEmojiconClicked(Emojicon emojicon) {
		// TODO Auto-generated method stub
		EmojiconsFragment.input(editText, emojicon);
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
				pd = new ProgressDialog(SendSmsActivityWithEmail.this);
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

	private class EmailMessageTask extends AsyncTask<Void, Void, Void> {
		private String response = "";
		private String userId = "";
		private String grouptitle = "";
		private String expiry_time = "10";
		private String messageStr = "hello jack";
		private String groupJid = "";
		private String templateId = "";
		private Context mContext;

		EmailMessageTask(Context context, String userId, String grouptitle,
				String groupJid, String message, String expiry_timeString,
				String templateId) {
			mContext = context;
			this.userId = userId;
			this.grouptitle = grouptitle;
			this.groupJid = groupJid;
			this.expiry_time = expiry_time;
			this.messageStr = message;
			this.templateId = templateId;

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("Page",
					"EmailSentFromChat"));
			nameValuePairs.add(new BasicNameValuePair("UserId", userId));
			nameValuePairs.add(new BasicNameValuePair("GroupJID", groupJid));
			nameValuePairs.add(new BasicNameValuePair("GroupName", grouptitle));
			nameValuePairs
					.add(new BasicNameValuePair("MessageBody", messageStr));
			nameValuePairs.add(new BasicNameValuePair("Min", expiry_time));
			nameValuePairs
					.add(new BasicNameValuePair("TemplateId", templateId));
			Rest rest2 = Rest.getInstance();
			response = rest2.post(Apiurls.KIT19_BASE_URL.replace("?Page=", ""),
					nameValuePairs);
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				if (deleteBtn != null && deleteBtn.isShown()) {
					deleteBtn.setVisibility(View.GONE);
					mEmojiBtn.setVisibility(View.VISIBLE);
					smsCheckBox.setEnabled(true);
				}
				resetValues();
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			String status = "";
			String message = "";
			if (response != null && response.trim().length() != 0) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.has("MailStatus")) {
						JSONArray jsonArray = jsonObject
								.getJSONArray("MailStatus");
						if (jsonArray != null && jsonArray.length() > 0) {
							JSONObject obj = jsonArray.getJSONObject(0);
							if (obj != null) {
								if (obj.has("Status")) {
									status = obj.getString("Status");
								}
								if (obj.has("Message")) {
									message = obj.getString("Message");
								}
							}
						}
					}
					if (status != null && status.equalsIgnoreCase("false")) {
						Toast.makeText(mContext, message, Toast.LENGTH_LONG)
								.show();
					} else if (status != null
							&& status.equalsIgnoreCase("true")) {
						Toast.makeText(mContext, message, Toast.LENGTH_LONG)
								.show();
					} else {
						Toast.makeText(mContext, "Mail not sent",
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}

	}

	private void selectEmailSmsTemplate() {
		final Dialog dialog = new Dialog(SendSmsActivityWithEmail.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.select_template_popup);
		TextView emailTemplate = (TextView) dialog
				.findViewById(R.id.email_template);
		TextView smsTeplate = (TextView) dialog.findViewById(R.id.sms_template);
		emailTemplate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SendSmsActivityWithEmail.this,
						EmailTemplateActivity.class);
				startActivityForResult(intent,
						SELECT_EMAIL_TEMPLATE_REQUEST_CODE);
				dialog.dismiss();
			}
		});
		smsTeplate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SendSmsActivityWithEmail.this,
						TemplateActivity.class);
				intent.putExtra("taketemplate", "send");
				startActivityForResult(intent, SELECT_TEMPLATE_REQUEST_CODE);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	/*
	 * @Override public void onContactItemClicked(int position) { // TODO
	 * Auto-generated method stub int selected = 0; for (ContactInfo contactInfo
	 * : selectedContactsList) { if (contactInfo.isSelected) { selected++; } }
	 * contactsLabelTextView.setText("Select your contacts" + " (" + selected +
	 * ")"); }
	 */
}
