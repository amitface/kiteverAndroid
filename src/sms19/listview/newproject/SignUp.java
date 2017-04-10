package sms19.listview.newproject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPReply;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sms19.inapp.msg.asynctask.RegisterAsynTask;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Countrymodel;
import sms19.inapp.msg.model.RegisterModel;
import sms19.inapp.msg.rest.Rest;
import sms19.inapp.msg.rest.XmmpconnectionListner;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.UserRegistrationModel;
import sms19.listview.validation.Validation;
import sms19.listview.webservice.webservice;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gcm.GCMRegistrar;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.gcm.ServerUtilities;


public class SignUp extends ActionBarActivity implements OnClickListener {

	Button signup;
	EditText username, mobileEditText, email,company_username;
	String cname=""; //company_user_name
	boolean checkLoginType = false;
	boolean checkfirstTime = true;
	String uname, mobilenum, merchant;
	ProgressDialog p;
	// ImageView tc;
	Context alert;
	Bitmap bitmap;

	DataBaseDetails dbojt = new DataBaseDetails(this);
	public static SharedPreferences chatPrefs;

	public static final int progress_bar_type = 0;
	public static final String SENDER_ID  = "134386644306";

	// private String mUserNameXmpp="";
	// private String mUserPassword="123456";

	RegisterModel registerModel = new RegisterModel();
	RegisterModel registerModelPost = new RegisterModel();
	ProgressDialog pDialog;
	public Rest rest;
	private Spinner spinner;
	private ArrayList<Countrymodel> countryArrayList = null;
	private sms19.inapp.msg.adapter.CountryListAdapter adapter = null;
	private int defaultIndex = 0;
	private String countryCodeString = "";
	private TextView name_countryTextView;
	private ImageView individualCompanyIcon;
	// private boolean isPhoneValid=false;
	private String userCategory = "0";
	LinearLayout fname_layout;
	ScrollView scrollLayout;
	//for gcm
	AsyncTask<Void, Void, Void> mRegisterTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signupscreen);
		rest = Rest.getInstance();
		chatPrefs = getSharedPreferences("chatPrefs", MODE_PRIVATE);
		signup = (Button) findViewById(R.id.signupbtn);
		username = (EditText) findViewById(R.id.usersignupname);
		company_username = (EditText) findViewById(R.id.company_username);
		mobileEditText = (EditText) findViewById(R.id.MobileNumber);
		email = (EditText) findViewById(R.id.Email);
		fname_layout = (LinearLayout) findViewById(R.id.fname_layout);
		scrollLayout= (ScrollView) findViewById(R.id.scrollLayout);
		scrollLayout.setBackgroundColor(Color.parseColor(CustomStyle.SIGNUP_BACKGROUND));
		//marchent_code_popupbtn = (ImageView) findViewById(R.id.marchent_code_popupbtn);
		RadioGroup radioGrp = (RadioGroup) findViewById(R.id.radio_grp);
		radioGrp.setOnCheckedChangeListener(checkedChangeListener);
		individualCompanyIcon = (ImageView) findViewById(R.id.individual_company_icon);

		TextView termCond = (TextView) findViewById(R.id.text_privacy);
		termCond.setOnClickListener(tcListner);
		View navigateSignin = findViewById(R.id.navigate_signin);
		navigateSignin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 startActivity(new Intent(SignUp.this,LoginPage.class));
				// finish();
				//onBackPressed();
			}
		});
		// cbx = (CheckBox) findViewById(R.id.checkTermCondition);
		name_countryTextView = (TextView) findViewById(R.id.name_country);
		spinner = (Spinner) findViewById(R.id.country_code);
		ImageView usertTypeHelp = (ImageView) findViewById(R.id.user_type_help);
		usertTypeHelp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Utils.marchantCodeDialog(SignUp.this, "User Type",
						"Choose an option that best describes how you wish to use the app");

			}
		});


		final String iso_code = com.kitever.utils.Utils.getIsoCode(SignUp.this);

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				countryArrayList = Utils.getCountryList(SignUp.this);

				if (countryArrayList != null) {
					if (iso_code != null) {
						if (!iso_code.equals("")) {
							final int size = countryArrayList.size();
							for (int i = 0; i < size; i++) {
								if (countryArrayList.get(i).getCountryISOCode()
										.equals(iso_code)) {
									defaultIndex = i;
									break;
								}
							}
						}
					}
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter = new sms19.inapp.msg.adapter.CountryListAdapter(
								SignUp.this, countryArrayList);
						spinner.setAdapter(adapter);
						spinner.setSelection(defaultIndex);
					}
				});

			}
		}).start();

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				countryCodeString = countryArrayList.get(arg2).getCountrycode()
						.trim();
				name_countryTextView.setText(countryCodeString);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		mobileEditText.addTextChangedListener(new TextWatcher() {
			String phoneNumber;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				phoneNumber = mobileEditText.getText().toString().trim();

				if (phoneNumber.length() > 0 && countryCodeString.length() > 0) {
					if (isValidPhoneNumber(phoneNumber)) {
						// boolean status =
						// validateUsing_libphonenumber(countryCode,
						// phoneNumber);
						boolean status = parseContact(phoneNumber,
								countryCodeString);

						if (!status) {
							mobileEditText.setError("Invalid phone");
						} else {
							mobileEditText.setError(null);
						}

					}

				}

			}
		});

		email.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (Validation.isEmailAddress(email, true)) {
					// isEmailValid = true;
				} else {
					// isEmailValid = false;
				}
			}
		});

		//merchantCode = (EditText) findViewById(R.id.edittextMerchat);

		// tc.setOnClickListener(this);
		/*************************** INTERNET ********************************/
		webservice._context = this;

		/*************************** INTERNET ********************************/

		alert = this;
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
		bar.setTitle(Html.fromHtml("<font color='#ffffff'>Sign Up</font>"));
		// bar.setHomeAsUpIndicator(R.drawable.arrow_new);

		/*
		 * registerModel.setUserNumber("+91"+"9001670667");
		 * registerModel.setUserId("434"); registerModel.setName("sds");
		 * registerModel.setCountryCode("+91"); registerModel.setPwd("89477");
		 * RegisterToServer();
		 */// register on xmpp server

		try {
			Intent i = getIntent();
			String nm = i.getExtras().get("name").toString();
			String em = i.getExtras().getString("email").toString();
			username.setText(nm);
			email.setText(em);
			if (!nm.equals(""))
				checkLoginType = true;
		} catch (Exception e) {

		}

		/*
		 * try { MainActivity.signOutFromGplus(); } catch(Exception e){}
		 */

		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				uname = username.getText().toString().trim();
				cname = company_username.getText().toString().trim();
				if (countryCodeString == null && countryCodeString.equals("")) {

					Toast.makeText(getApplicationContext(),
							"Please input a valid country code",
							Toast.LENGTH_LONG).show();

					return;

				}

				if (Validation.hasText(username) && Validation.hasText(company_username)
						&& Validation.hasText(mobileEditText)
						&& Validation.hasText(email)
						&& Validation.isEmailAddress(email, true)) {

					String emailU = email.getText().toString().trim();
				//	String code = merchantCode.getText().toString().trim();
					mobilenum = mobileEditText.getText().toString().trim();

					// mUserNameXmpp=mobilenum;

					Utils.saveCountryCodeNumber(SignUp.this, countryCodeString
							+ mobilenum.trim());
					// GlobalData.CountryCode="+91";
					String LoginType;
					// LoginType==1 for app user
					if (checkLoginType) {
						LoginType = "1";
						userCategory = "1";
					} else {
						LoginType = "1";
						userCategory = "2";
					}

					registerModelPost = new RegisterModel();
					registerModelPost.setName(uname);
					registerModelPost.setCompanyname(cname);
					registerModelPost.setUserNumber(mobilenum);
					// registerModel.setUserNumber(countryCode+mobilenum);
					registerModelPost.setEmail(emailU);
					registerModelPost.setLogin_type(LoginType);
					registerModelPost.setVerifyCode(getString(R.string.merchant_code));
					registerModelPost.setCountryCode(countryCodeString);
					registerModelPost.setUserLogin(countryCodeString
							+ mobilenum);
					registerModelPost.setUserCategory(userCategory);
					if (Utils.isDeviceOnline(SignUp.this)) {
						p = ProgressDialog.show(alert, null, "Please Wait..");
						sms19.inapp.msg.asynctask.RegisterAsynTask asyncTask = new RegisterAsynTask(
								SignUp.this, registerModelPost);
						asyncTask.execute();
					} else {
						new AlertDialog.Builder(SignUp.this)
								.setCancelable(false)
								.setMessage("Internet connection not found")
								.setPositiveButton("Ok",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();

											}
										}).show();
					}
				}
			}
		});

		//RegisterDevice();

	}

	public void RegisterDevice()
	{
		//register user to server

			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			final String regId = GCMRegistrar.getRegistrationId(this);
			Log.i("register_id",regId );
			final Context context = this;
			if (regId.equals("")) {
				GCMRegistrar.register(this, SENDER_ID);
				Log.i("register status","Blank");
				final String regId123 = GCMRegistrar.getRegistrationId(this);
				Log.i("register_id","--"+regId123 );
			} else {
				// Device is already registered on GCM
				if (GCMRegistrar.isRegisteredOnServer(this)) {
					Log.i("register status","already register");
					ServerUtilities.registerToServer(context, regId);

				} else {
					// Try to register again, but not in the UI thread.
					// It's also necessary to cancel the thread onDestroy(),
					// hence the use of AsyncTask instead of a raw thread.
					Log.i("register status","Register Again");
					mRegisterTask = new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... params) {
							// Register on our server
							// On server creates a new user
							ServerUtilities.registerToServer(context, regId);
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							mRegisterTask = null;
						}

					};
					mRegisterTask.execute(null, null, null);
				}
			}


	}

	OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if (checkedId == R.id.individual_id) {
				checkLoginType = true;
				fname_layout.setVisibility(View.GONE);
				//username.setHint("Name");
				//individualCompanyIcon.setImageDrawable(getResources()	.getDrawable(R.drawable.ic_user));
			} else {
				checkLoginType = false;
				fname_layout.setVisibility(View.VISIBLE);
				//username.setHint("Company Name");
				//individualCompanyIcon.setImageDrawable(getResources().getDrawable(R.drawable.addcon));
			}
		}
	};

	OnClickListener tcListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AlertDialog.Builder alert = new AlertDialog.Builder(SignUp.this);
			alert.setTitle("Terms and Privacy");
			WebView wv = new WebView(SignUp.this);
			wv.clearHistory();
			wv.getSettings().setJavaScriptEnabled(true);
			wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			wv.loadUrl("http://www.kitever.com/termsandprivacy.aspx");
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
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, menu);
		return false;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.howtouse:
			Intent i = new Intent(SignUp.this, HowToUse.class);
			startActivity(i);
			break;
		case R.id.pp:
			Intent i1 = new Intent(SignUp.this, TermsAndCondition.class);
			startActivity(i1);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onClick(View v) {
		new AlertDialog.Builder(alert)
				.setCancelable(false)
				.setMessage(
						"Click OK if You want to read Terms and Conditions  ")
				.setPositiveButton("CANCEL",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						})
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent i = new Intent(SignUp.this,
								TermsAndCondition.class);
						startActivity(i);
					}
				}).show();

	}

	private class WebClientClass extends WebViewClient {
		ProgressDialog pd = null;

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			if (pd == null)
				pd = new ProgressDialog(SignUp.this);
			pd.setTitle("Please wait");
			pd.setMessage("Page is loading..");
			pd.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (pd != null && pd.isShowing())
				pd.dismiss();
		}
	}

	// Download Merchant Logo
	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();

				int lenghtOfFile = conection.getContentLength();

				InputStream input = new BufferedInputStream(url.openStream(),
						8192);
				OutputStream output = new FileOutputStream(
						"/sdcard/MerchantSMS19.jpg");
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();

			} catch (Exception e) {
				// Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {

		}

	}

	public void RegisterToServer() {
		ConnectionConfiguration connConfig = new ConnectionConfiguration(
				GlobalData.HOST, GlobalData.PORT);
		connConfig.setSASLAuthenticationEnabled(false);
		connConfig.setReconnectionAllowed(false);
		connConfig.setCompressionEnabled(false);
		connConfig.setSecurityMode(SecurityMode.disabled);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			connConfig.setTruststoreType("AndroidCAStore");
			connConfig.setTruststorePassword(null);
			connConfig.setTruststorePath(null);
		} else {
			connConfig.setTruststoreType("BKS");
			String path = System.getProperty("javax.net.ssl.trustStore");
			if (path == null)
				path = System.getProperty("java.home") + File.separator + "etc"
						+ File.separator + "security" + File.separator
						+ "cacerts.bks";
			connConfig.setTruststorePath(path);
		}

		GlobalData.connection = new XMPPConnection(connConfig);
		Utils.setUpConnectionProperties();

		new ConnectingToServer().execute();
	}

	class ConnectingToServer extends AsyncTask<Void, Void, Void> {
		boolean allsuccess = true;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Utils.printLog2("connection start...");
				GlobalData.connection.connect();
				GlobalData.connection
						.addConnectionListener(new XmmpconnectionListner());
			} catch (XMPPException ex) {
				allsuccess = false;
				Utils.printLog2("connection exctiption");
				ex.printStackTrace();
				// GlobalData.connection = null;
			}
			// ///////////////dfdfdfddddddddddddddddd
			boolean register = false;
			try {
				if (GlobalData.connection != null
						&& GlobalData.connection.isConnected()) {

					Utils.printLog2("Try to register");
					AccountManager am = new AccountManager(
							GlobalData.connection);
					// Utils.printLog2("Registration Details: UserName = "+
					// registerModel.getUserNumber() + ",  Password =" +
					// mUserPassword);
					saveRegisterData(registerModel.getUserId(),
							registerModel.getUserNumber(),
							registerModel.getCountryCode(),
							registerModel.getCountryCode(),
							registerModel.getCountryCode(),
							registerModel.getName(), registerModel.getPwd());
					// am.createAccount(registerModel.getUserNumber().trim(),
					// mUserPassword);
					// Utils.printLog2("Check registerModel.getUserNumber().trim() = "+
					// registerModel.getUserNumber() +
					// ", registerModel.getPwd() =" + registerModel.getPwd());
					am.createAccount(registerModel.getUserNumber().trim(),
							registerModel.getPwd());

					Utils.printLog2("Register successfully");

					register = true;
				}
			} catch (XMPPException ex) {
				if (ex.getMessage().equals("conflict(409)")) {
					register = true;
					Utils.printLog2("Allready Register");
					// new
					// JoinRoomAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					ex.printStackTrace();
				} else {
					register = false;
					Utils.printLog2("Register excption");
					ex.printStackTrace();
				}
			}
			// new
			// JoinRoomAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			if (GlobalData.connection != null
					&& GlobalData.connection.isConnected() && register) {
				try {

					GlobalData.connection.disconnect();
					GlobalData.connection = null;

				} catch (Exception e) {
					// TODO Auto-generated catch block
					allsuccess = false;
					e.printStackTrace();
					Utils.printLog2("Roster excption");

				}
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// dialog.dismiss();

			if (allsuccess) {

			} else {
				new AlertDialog.Builder(SignUp.this)

						.setMessage("Connection lost try again..")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								}).show();
				// Toast.makeText(getApplicationContext(),"",
				// Toast.LENGTH_SHORT);
			}

		}

	}

	private void saveRegisterData(String userId, String userNumber,
			String countryCode, String verifyCode, String adminroom_id,
			String adminroom_name, String pass) {
		RingtoneManager manager = new RingtoneManager(SignUp.this);
		manager.setType(RingtoneManager.TYPE_NOTIFICATION);
		Uri Duri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		// String pass = "123456";
		Utils.printLog2("USER PASSWORD:  " + pass);

		Editor editor = chatPrefs.edit();
		editor.putString("userId", userId);
		editor.putString("userNumber", userNumber);
		editor.putString("adminroom_id", adminroom_id);
		editor.putString("adminroom_name", adminroom_name);
		editor.putString("user_jid", userNumber + "@" + GlobalData.HOST);
		editor.putString("userPassword", pass);
		editor.putString("msgtone", Duri.toString());
		editor.putString("groupmsgtone", Duri.toString());
		editor.putString("countryCode", countryCode);
		editor.putString("verifyCode", verifyCode);
		editor.putBoolean("firsttime", true);
		editor.putBoolean("vibratebtn", true);
		editor.putBoolean("silentbtn", false);
		editor.putLong("lastleaveTime", 0);
		editor.commit();

	}

	public static void addingRosterentry() {

		final Roster roster = GlobalData.connection.getRoster();
		roster.setSubscriptionMode(SubscriptionMode.accept_all);
		for (int i = 0; i < GlobalData.registerContactList.size(); i++) {
			String remote_jid = GlobalData.registerContactList.get(i)
					.getRemote_jid();
			String remote_name = GlobalData.registerContactList.get(i)
					.getName();
			Utils.printLog2("Entry added start : " + remote_jid + " "
					+ remote_name);

			Presence subscribe = new Presence(Presence.Type.subscribe);
			subscribe.setTo(remote_jid);
			GlobalData.connection.sendPacket(subscribe);

			int retrycount = 0;
			boolean success = createrosterentry(roster, remote_jid,
					remote_name, retrycount);

			while (!success) {
				SystemClock.sleep(500);
				retrycount++;
				success = createrosterentry(roster, remote_jid, remote_name,
						retrycount);
			}
			Utils.printLog2("Entry added success : " + remote_jid + " "
					+ remote_name + ", count==" + retrycount);

		}

	}

	public static boolean createrosterentry(Roster roster, String jid,
			String fname, int retrycount) {
		try {
			roster.createEntry(jid, fname, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return retrycount == 2;
		}
	}

	public void onSuccessNew(Object Result) {

		try {
			p.dismiss();
		} catch (Exception e) {
		}

		UserRegistrationModel m = (UserRegistrationModel) Result;
		if (m.getUserRegistation().get(0).getError() != null) {
			new AlertDialog.Builder(this)

					.setMessage("" + m.getUserRegistation().get(0).getError())
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).show();
			// Toast.makeText(getApplicationContext(),""+
			// m.getUserRegistation().get(0).getError(),
			// Toast.LENGTH_SHORT).show();
		} else {
			String logo = m.getUserRegistation().get(0)
					.getMerchant_Picture_Path();
			String name = m.getUserRegistation().get(0).getMERCHANT_Fname();
			String webadd = m.getUserRegistation().get(0).getMerchant_Url();
			String lname = m.getUserRegistation().get(0).getMERCHANT_Lname();
			String uid = m.getUserRegistation().get(0).getMERCHANT_User_ID();
			String Pwd = m.getUserRegistation().get(0).getPwd();

			// sms19.inapp.msg.model.PhoneValidModel model=
			// sms19.inapp.msg.constant.ContactUtil.validNumberCheckForBroadCast(mobilenum);

			/*
			 * if(model==null){ return; }
			 */

			registerModel.setUserNumber(countryCodeString + mobilenum);
			registerModel.setUserId(uid);
			registerModel.setName(lname);
			registerModel.setCountryCode(countryCodeString);
			registerModel.setPwd(Pwd);

			RegisterToServer();// register on xmpp server

			getFTPDetails(registerModel.getUserId());

			if (logo == null || logo.length() < 3) {

			} else {
				String checkhttp = logo.substring(0, 7);

				// Log.w("RM", "RM ::::::::(checkhttp):" + checkhttp);

				if (checkhttp.equalsIgnoreCase("http://")) {

				} else {
					logo = "http://" + logo;
				}

				// Log.w("RM", "RM ::::::::(logo):" + logo);

				new DownloadFileFromURL().execute(logo);
			}

			// Log.w("sign up data", "find error" + uid + "," + name + ","
			// + webadd + "," + logo);

			dbojt.Open();
			dbojt.addmerchantinformation(uid, name, webadd, logo);
			dbojt.close();

			try {
				Toast.makeText(getApplicationContext(),
						m.getUserRegistation().get(0).getMsg(),
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {

			}
			try {
				clearApplicationData();
				ContextWrapper cw = new ContextWrapper(getApplicationContext());
				File directory = cw.getDir("KiteverImageDir",
						Context.MODE_PRIVATE);
				deleteDir(directory);
			} catch (Exception e) {
				// TODO: handle exception
			}
			new AlertDialog.Builder(alert)
					.setTitle("Registration Successful!!")
					.setCancelable(false)
					.setMessage(
							"Your Username is "
									+ countryCodeString
									+ mobilenum
									+ " "
									+ "Password has been sent to your registered Mobile Number")

					.setNegativeButton("Ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent i = new Intent(SignUp.this,
											LoginPage.class);
									i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
									i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(i);
								}
							}).show();
		}

		try {
			String EmergencyMessage = m.getUserRegistation().get(0)
					.getEmergencyMessage();
			try {

				Emergency.desAct.finish();
			} catch (Exception e) {
			}

			if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
				Intent rt = new Intent(SignUp.this, Emergency.class);
				rt.putExtra("Emergency", EmergencyMessage);
				startActivity(rt);

			}
		} catch (Exception e) {

		}

	}

	public void onErrorNew(String object) {
		try {
			p.dismiss();
		} catch (Exception e) {
		}
		Toast.makeText(getApplicationContext(), object, Toast.LENGTH_SHORT)
				.show();

	}

	private boolean isValidPhoneNumber(CharSequence phoneNumber) {

		if (!TextUtils.isEmpty(phoneNumber)) {
			return Patterns.PHONE.matcher(phoneNumber).matches();
		}

		return false;
	}

	public boolean parseContact(String contact, String countrycode) {
		String code = "";
		PhoneNumber phoneNumber = null;
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		try {
			code = countrycode.substring(1, countrycode.length());
		} catch (Exception e) {
			// TODO: handle exception
			code = countrycode.substring(1, countrycode.length() - 1);
		}

		String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer
				.parseInt(code));
		boolean isValid = false;
		PhoneNumberType isMobile = null;
		try {
			phoneNumber = phoneNumberUtil.parse(contact, isoCode);
			isValid = phoneNumberUtil.isValidNumber(phoneNumber);
			isMobile = phoneNumberUtil.getNumberType(phoneNumber);

		} catch (NumberParseException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return isValid
				&& (PhoneNumberType.MOBILE == isMobile || PhoneNumberType.FIXED_LINE_OR_MOBILE == isMobile);

	}

	private void getFTPDetails(final String userId) {

		// final String url =
		// webservice.getCurrentBalance.geturl(loginInfo.uid);

		final String url = webservice.GetFTPHostDetail2.geturl(userId, "");

		StringRequest strreq = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String Response) {

						try {
							JSONObject jsonObject2 = new JSONObject(Response);

							JSONArray array = jsonObject2
									.getJSONArray("GetFTPHostDetail");

							if (array != null) {

								/*
								 * "HostName": "nowconnect.in", "FtpUser":
								 * "ftp.nowconnect.in|userweb3", "FtpPassword":
								 * "dsT%Mkbbp9ry", "FtpUrl": "nowconnect.in",
								 * "EmergencyType": "NO", "EmergencyMessage":
								 * "NO"
								 */

								JSONObject jsonObject = array.getJSONObject(0);
								if (jsonObject.has("FtpUser")) {
									GlobalData.FTP_USER = jsonObject
											.getString("FtpUser");
								}
								if (jsonObject.has("FtpPassword")) {
									GlobalData.FTP_PASS = jsonObject
											.getString("FtpPassword");
								}
								if (jsonObject.has("HostName")) {
									GlobalData.FTP_HOST = jsonObject
											.getString("HostName");
								}
								if (jsonObject.has("FtpUrl")) {
									GlobalData.FTP_URL = jsonObject
											.getString("FtpUrl");
								}

								new Thread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										ftpMakeDirectory(userId);
									}
								}).start();

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

	private void ftpMakeDirectory(String userId) {

		String folder = "/" + userId;
		String server = GlobalData.FTP_HOST;
		int port = 21;
		String user = GlobalData.FTP_USER;
		String pass = GlobalData.FTP_PASS;

		org.apache.commons.net.ftp.FTPClient ftpClient = new org.apache.commons.net.ftp.FTPClient();
		try {
			try {
				ftpClient.connect(server, port);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				// System.out.println("Operation failed. Server reply code: "
				// + replyCode);
				return;
			}
			boolean success = ftpClient.login(user, pass);
			// showServerReply(ftpClient);
			if (!success) {
				// System.out.println("Could not login to the server");
				return;
			}
			// Creates a directory
			String dirToCreate = folder;
			success = ftpClient.makeDirectory(dirToCreate);
			// showServerReply(ftpClient);
			if (success) {
				Utils.userIsCreateFtpFolder(getApplicationContext(), true);
				// System.out.println("Successfully created directory: "
				// + dirToCreate);
			} else {

				// System.out
				// .println("Failed to create directory. See server's reply.");
			}
			// logs out
			ftpClient.logout();
			ftpClient.disconnect();
		} catch (IOException ex) {
			// System.out.println("Oops! Something wrong happened");
			// ex.printStackTrace();
		}
	}

	public void clearApplicationData() {
		File cache = getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				if (!s.equals("lib")) {
					deleteDir(new File(appDir, s));
				}
			}
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}
}
