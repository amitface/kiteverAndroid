package com.kitever.sendsms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.imoze.EmojiconEditText;
import sms19.inapp.msg.imoze.EmojiconsFragment;
import sms19.inapp.msg.imozemodel.Emojicon;
import sms19.listview.database.DataBaseDetails;
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
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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
public class SendSmsActivity extends ActionBarActivity implements
		sms19.inapp.msg.imoze.EmojiconGridFragment.OnEmojiconClickedListener,
		EmojiconsFragment.OnEmojiconBackspaceClickedListener {

	private static final int SELECT_CONTACTS_REQUEST_CODE = 101;

	private static final int SELECT_TEMPLATE_REQUEST_CODE = 102;

	private static final int SELECT_MANUALLY_REQUEST_CODE = 103;

	private static final int FROM_SEND_SMS_MODULE = 104;

	private DataBaseDetails dbObject = null;

	private LoginInfo loginInfo = null;

	private LinearLayout valueLinLayout, manualNoLinLayout;
	private TextView groupLabelTextView, contactsLabelTextView, timeTextView;
	private boolean makeEmojiVisible = true;

	private ListView mListView;

	// private ArrayList<String> manualArrayList = new ArrayList<String>();
	// private TagView groupTagView;

	private ArrayList<GroupDetails> groupNameTagList = new ArrayList<GroupDetails>();

	private ArrayList<String> groupsArrayList = new ArrayList<String>();

	private ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();

	private AlertDialog.Builder groupBuilder = null;

	private ArrayList<ContactInfo> selectedContactsList = null;

	private long datetime;

	private static final int DIALOG_PICK_DATETIME_ID = 0;

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

	private ArrayList<ContactInfo> manualNumArrayList = new ArrayList<>();

	// private TextView balValueTextView;

	private RelativeLayout emojicons;
	private EmojiconsFragment emojiconsFragment = null;
	private ImageView mEmojiBtn;

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
		/**/

		// final ActionBar bar = getSupportActionBar();
		// bar.hide();
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
		manualLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SendSmsActivity.this,
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
						SendSmsActivity.this);
				alert.setTitle("TopUp");
				WebView wv = new WebViewHelper(SendSmsActivity.this);
				wv.clearHistory();
				wv.getSettings().setJavaScriptEnabled(true);
				wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
				String Pass = sms19.inapp.msg.constant.Utils
						.getPassword(SendSmsActivity.this);
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
						Intent intent = new Intent(SendSmsActivity.this,
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

				Intent id = new Intent(SendSmsActivity.this,
						TemplateActivity.class);
				id.putExtra("taketemplate", "send");
				startActivityForResult(id, SELECT_TEMPLATE_REQUEST_CODE);

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

		mEmojiBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (makeEmojiVisible) {
					sms19.inapp.msg.constant.Utils.hideKeyBoardMethod(
							getApplicationContext(), mEmojiBtn);

					setEmojiconFragment(false);
				} else {
					removeEmoji();
				}

			}
		});

		emojicons = (RelativeLayout) findViewById(R.id.emojicons);

		final ImageView sendButton = (ImageView) findViewById(R.id.sendButton);

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showProgressDialog();

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

				final StringBuffer mobileString = new StringBuffer();

				final StringBuffer groupString = new StringBuffer();

				if (contactInfoArrayList != null) {
					for (ContactInfo contactInfo : contactInfoArrayList) {
						if (contactInfo.isSelected) {
							isContactSelected = true;
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

				for (int i = 0; i < mSelectedItems.size(); i++) {
					isGroupSelected = true;
					GroupDetails groupDetails = groupNameTagList
							.get(mSelectedItems.get(i));
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

				if (!timeTextView.getText().equals("")) {
					final String dateTime = timeTextView.getText().toString();
					if (dateTime.contains(" ")) {
						final String arrDateTime[] = dateTime.split(" ");
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
						// 2016 - 12 - 16
					}
				}

				final int isUnicode = check();

				sendSMSRequest(templateID, txt_message, mobiStr, groupStr,
						date, time, isUnicode);

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

		// setTags();

		initFun();

	}

	private void setEmojiconFragment(boolean useSystemDefault) {

		try {

			if (emojicons != null) {
				makeEmojiVisible = false;
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

	private void removeEmoji() {
		try {
			if (emojicons != null) {
				makeEmojiVisible = true;
				emojicons.setVisibility(View.GONE);
				emojicons.removeAllViews();
			}
		} catch (Exception e) {
			// TODO: handle exception
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
			progressDialog = new ProgressDialog(SendSmsActivity.this);
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
			return new DateTimePickerDialog(SendSmsActivity.this,
					new DateTimePickerDialog.DateTimeAcceptor() {
						public void accept(long datetime) {
							final String formattedDatetime = DateHelper
									.myFormat2(datetime);
							// log("acceptDatetime: got " + formattedDatetime);
							SendSmsActivity.this.datetime = datetime;
							timeTextView.setText(formattedDatetime);
						}
					}, new DateTimePickerDialog.DateTimeDeselector() {

						@Override
						public void deselect() {
							timeTextView.setText("");
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
			Log.i("SendSmsContactsSingleton", "null");
			// do nothing
		} else {
			if (SendSmsContactsSingleton.getInstance().getContactInfos() == null) {
				// do nothing
				Log.i("getcontactsSingleton", "null");
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
						Log.i("contactInfos", "--" + contactInfos.size());
						for (ContactInfo contactInfo : contactInfos) {
							if (contactInfo.isSelected) {
								selectedContactsList.add(contactInfo);
								Log.i("is selected", "--" + contactInfo);
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
										SendSmsActivity.this,
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
				dbObject = new DataBaseDetails(SendSmsActivity.this);

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
						Toast.makeText(SendSmsActivity.this, error.toString(),
								Toast.LENGTH_LONG).show();

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

				Log.i("Url", "url- " + Apiurls.getBasePostURL());
				Log.i("send parameter", "map - " + params.toString());

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

								if (noOfContacts != null) {
									if (!noOfContacts.trim().equals("0")) {

										groupNameTagList.add(list.get(i));

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
				SendSmsActivity.this);
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
					final TextView textView = new TextView(SendSmsActivity.this);
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
			final TextView textView = new TextView(SendSmsActivity.this);
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
				pd = new ProgressDialog(SendSmsActivity.this);
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
	/*
	 * @Override public void onContactItemClicked(int position) { // TODO
	 * Auto-generated method stub int selected = 0; for (ContactInfo contactInfo
	 * : selectedContactsList) { if (contactInfo.isSelected) { selected++; } }
	 * contactsLabelTextView.setText("Select your contacts" + " (" + selected +
	 * ")"); }
	 */
}
