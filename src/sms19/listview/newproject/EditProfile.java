package sms19.listview.newproject;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Rest;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.GetFTPCre;
import sms19.listview.newproject.model.GetProfileData;
import sms19.listview.newproject.model.Imageupload;
import sms19.listview.validation.Validation;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kitever.android.R;
import com.kitever.utils.DateHelper;

import eu.janmuller.android.simplecropimage.CropImage;

public class EditProfile extends ActionBarActivity implements OnClickListener {
	// private static final String UPDATEPROFILE =
	// "http://kit19.com/NewService.aspx";
	private ProgressBar secondBar;
	static Boolean send = false;
	String imagePathh;
	public static EditText dob, editName, editMobile, editEmail, editPinCode,
			editAddress, merchantcode, edit_home_url, webaddress;// dob ;
	RadioButton radioBtnMale, radioBtnFemale;
	Button btnSubmit, btnBrowser;
	public static EditText editCountry, editCurrency, editCompanyName;
	TextView upgrade, tvv, expiryDate, planName;

	String filePath, urlpath, sendurlpath;
	Uri khogenpickedImage;
	String[] khogenfilePath;
	Cursor khogencursor;

	public static ImageView images;
	private int mYear, mMonth, mDay;
	String imagetoupload2 = "";

	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0;

	Bitmap bitmap;

	private static int LOAD_IMAGE_RESULTS = 1;
	String ppath;
	RadioGroup genG;
	String gen, name, mobile, email, zipcode, merchantcodes, webaddressi, dobi,
			country, currency, companyname, merchcode, doe;
	String mobile1;
	String email1;
	String pincode1, setimege = "";
	String last;
	Context gContext;
	// String imagePathh;

	String uname1;
	public static Activity editProfile;
	public static String UserId = "", LoginType = "";
	public static String UserPassword = "";
	CheckBox promptme;
	DataBaseDetails dbObject = new DataBaseDetails(this);
	ImageView dobv;
	String EmergencyMessage;
	TableRow tablerow5, tablerow6, tablerow9;
	LinearLayout lLayoutCompanyname, lLayoutMerchantcode, home_url_layout;
	View viewMcode, viewCompname, home_url_line;
	ProgressDialog p;
	public static boolean checkPrompt = false;

	static Bitmap finalBitmap = null;
	public Contactmodel mydetail = null;
	public SharedPreferences chatPrefs;

	private final String TAG = "MainActivity";

	private final String TEMP_PHOTO_FILE_NAME = "Kitever.jpg";

	private final int REQUEST_CODE_CROP_IMAGE = 0x3;

	String custom_status = "";
	private String merchantCodeVal = "";
	private String userProfilePicturePath = null;
	private String merchant_Url = "", home_url = "";
	private String merchantNameTxt = "";
	private EditText editMerchantName, editStoreUrl;
	private String userType = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);

		custom_status = Utils.getUserCustomStatus(EditProfile.this);

		dobv = (ImageView) findViewById(R.id.dobedit);
		editName = (EditText) findViewById(R.id.editName);
		editMobile = (EditText) findViewById(R.id.editMobile);
		editEmail = (EditText) findViewById(R.id.editEmail);
		editPinCode = (EditText) findViewById(R.id.editPinCode);

		editCountry = (EditText) findViewById(R.id.editCountry);
		editCurrency = (EditText) findViewById(R.id.editCurrency);
		editCompanyName = (EditText) findViewById(R.id.editCompanyName);
		merchantcode = (EditText) findViewById(R.id.editMerchCode);
		promptme = (CheckBox) findViewById(R.id.promptme);
		// merchantcode = (EditText) findViewById(R.id.merchantcode);
		webaddress = (EditText) findViewById(R.id.websiteaddress);
		dob = (EditText) findViewById(R.id.dob);
		btnBrowser = (Button) findViewById(R.id.btnbrowser);
		images = (ImageView) findViewById(R.id.imgViewProfile);
		expiryDate = (TextView) findViewById(R.id.expirydate);
		planName = (TextView) findViewById(R.id.planname);
		View merchantNameLayout = findViewById(R.id.merchant_name_layout);
		View urlLayout = findViewById(R.id.url_layout);
		View urlLine = findViewById(R.id.url_line);
		View nameLine = findViewById(R.id.name_line);
		editMerchantName = (EditText) findViewById(R.id.editMerchantName);
		editStoreUrl = (EditText) findViewById(R.id.store_url);

		tablerow5 = (TableRow) findViewById(R.id.tablerow5);
		tablerow6 = (TableRow) findViewById(R.id.tablerow6);
		// tablerow9 = (TableRow) findViewById(R.id.tablerow9);

		// for company profile

		lLayoutCompanyname = (LinearLayout) findViewById(R.id.lLayoutCompanyName);
		lLayoutMerchantcode = (LinearLayout) findViewById(R.id.lLayoutMerchantCode);
		home_url_layout = (LinearLayout) findViewById(R.id.home_url_layout);
		edit_home_url = (EditText) findViewById(R.id.edit_home_url);
		home_url_line = findViewById(R.id.home_url_line);
		viewCompname = findViewById(R.id.viewCompName);
		viewMcode = findViewById(R.id.viewMCode);
		secondBar = (ProgressBar) findViewById(R.id.secondBar);

		try {
			SharedPreferences prfs = getSharedPreferences("profileData",
					Context.MODE_PRIVATE);
			String nname = prfs.getString("Name", "");
			String mmobile = prfs.getString("Mobile", "");
			String eemail = prfs.getString("EMail", "");
			String ccountry = prfs.getString("Country", "");
			String zzipcode = prfs.getString("pincode", "");
			String ccurrency = prfs.getString("Currency", "");
			String ccompanyname = prfs.getString("CompnayName", "");
			String pplanname = prfs.getString("Plan", "Free Plan");
			String eexpirydate = prfs.getString("ExpiryDate", "forever");
			String llogintype = prfs.getString("UserCategory", "");

			llogintype = llogintype.trim();
			userProfilePicturePath = prfs.getString("ProfilePicturePath", "");
			UserPassword = prfs.getString("Pwd", "");

			merchantCodeVal = prfs.getString("Merchant_Code", "");
			String ddoe = prfs.getString("DOE", "");
			String uuser_dob = prfs.getString("User_DOB", "");

			UserId = Utils.getUserId(EditProfile.this);

			// Toast.makeText(getApplicationContext(),
			// "companynam="+ccompanyname, Toast.LENGTH_SHORT).show();

			editName.setText(nname);
			editMobile.setText(mmobile);
			editEmail.setText(eemail);
			editCountry.setText(ccountry);
			editPinCode.setText(zzipcode);
			editCurrency.setText(ccurrency);
			editCompanyName.setText(ccompanyname);
			expiryDate.setText(eexpirydate);
			// merchantcode.setText(merchantCode);
			merchantCodeVal = merchantCodeVal.trim();
			if (merchantCodeVal == null || merchantCodeVal.equalsIgnoreCase("")) {
				merchantcode.setHint("Merchant Code :  Get it now");

			} else {
				merchantcode.setHint("Merchant Code :  " + merchantCodeVal);
			}

			LoginType = llogintype;
			planName.setText("\"" + pplanname + "\" active till ");

			// if (!uuser_dob.equals("nott")) {
			// dob.setText(uuser_dob);
			// dob.setHint(uuser_dob);
			// if (!ddoe.equals("nott")) {
			// dob.setText(ddoe);
			// dob.setHint(ddoe);
			// }
			// }
			if (userType.trim().equalsIgnoreCase("2")) {
				merchantNameLayout.setVisibility(View.VISIBLE);
				urlLayout.setVisibility(View.VISIBLE);
				urlLine.setVisibility(View.VISIBLE);
				nameLine.setVisibility(View.VISIBLE);
				home_url_layout.setVisibility(View.VISIBLE);
				home_url_line.setVisibility(View.VISIBLE);
				editMerchantName.setText(merchantNameTxt);
				editStoreUrl.setText(merchant_Url);
				edit_home_url.setText(home_url);
			}
			if ((LoginType.trim()).equalsIgnoreCase("2")) {
				dob.setHint("D.O.E");
				if (ddoe != null && ddoe.length() > 0) {
					dob.setHint(ddoe);
				}

			} else {
				dob.setHint("D.O.B");
				if (uuser_dob != null && uuser_dob.length() > 0) {
					dob.setHint(uuser_dob);
				}
			}
		} catch (Exception e) {

		}
		// inflatedetailsfmserver();
		// upgrade
		upgrade = (TextView) findViewById(R.id.tvupgrade);
		upgrade.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder alert = new AlertDialog.Builder(
						EditProfile.this);
				alert.setTitle("Plans");
				WebView wv = new WebView(EditProfile.this);
				String Pass = Utils.getPassword(EditProfile.this);
				String postData = "password=" + Pass;// &id=236";
				wv.postUrl(
						"http://kitever.com/BuyCredit.aspx?tab=plans&userid="
								+ UserId,
						EncodingUtils.getBytes(postData, "BASE64"));
				wv.setWebViewClient(new WebViewClient());

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

		// the bottom line for linnk to company -khogen

		String text = "If you are a company and would like to use this app for branding and your business communication,click here";
		SpannableString ss = new SpannableString(text);
		ClickableSpan clickablespan = new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				// TODO Auto-generated method stub
				// do whatever you want to do
				AlertDialog.Builder alert = new AlertDialog.Builder(
						EditProfile.this);

				WebView wv = new WebViewHelper(EditProfile.this);
				wv.loadUrl("http://www.kitever.com/KiteverPersonalCommercial.aspx?Type=KB");
				wv.setWebViewClient(new WebViewClient());

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
		ss.setSpan(clickablespan, 97, 107, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvv = (TextView) findViewById(R.id.bottomlink);
		tvv.setText(ss);
		tvv.setMovementMethod(LinkMovementMethod.getInstance());
		tvv.setHighlightColor(Color.TRANSPARENT);

		editProfile = this;
		gContext = this;
		chatPrefs = getSharedPreferences("chatPrefs", MODE_PRIVATE);
		ConstantFields.mydetail = GlobalData.dbHelper.getUserDatafromDB();
		mydetail = GlobalData.dbHelper.getUserDatafromDB();
		// changes regards actionbar color
		try {
			ActionBar b = getSupportActionBar();
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			b.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#006966")));
			b.setTitle(Html.fromHtml("<font color='#ffffff'>Profile</font>"));
			// b.setHomeAsUpIndicator(R.drawable.back_new);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dobv.setOnClickListener(this);

		dbObject.Open();
		// Cursor c;
		// c=dbObject.getProfile();
		// //Toast.makeText(getApplicationContext(), "logintype="+LoginType,
		// Toast.LENGTH_LONG).show(); //giving output=1;
		// if(c.getCount()<=0) //why this line?? - khogen
		// {
		//
		// inflatedetailsfmserver();
		// }
		// //Log.w("JSR", "JSR ::::::::(LoginType:)" + LoginType);
		// Toast.makeText(getApplicationContext(), "logintypee="+LoginType,
		// Toast.LENGTH_LONG).show(); output = null. but after sometime it loads
		// with reqd.

		// fetchUserId();
		// Toast.makeText(getApplicationContext(), "userid="+UserId,
		// Toast.LENGTH_LONG).show();

		try {
			if ((LoginType.trim()).equalsIgnoreCase("2")) // should be two here
			{
				/*
				 * tablerow5.setVisibility(View.VISIBLE);
				 * tablerow6.setVisibility(View.VISIBLE);
				 */
				lLayoutCompanyname.setVisibility(View.VISIBLE);
				viewCompname.setVisibility(View.VISIBLE);
				lLayoutMerchantcode.setVisibility(View.VISIBLE);
				viewMcode.setVisibility(View.VISIBLE);
				tvv.setVisibility(View.GONE); // the bottom link of click here?

				merchantcode.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (merchantCodeVal.equalsIgnoreCase("")) {
							AlertDialog.Builder alert = new AlertDialog.Builder(
									EditProfile.this);
							alert.setTitle("Plans");
							String Pass = Utils.getPassword(EditProfile.this);
							WebView wv = new WebViewHelper(EditProfile.this);
							String postData = "password=" + Pass;// &id=236";
							wv.postUrl(
									"http://kitever.com/BuyCredit.aspx?tab=plans&userid="
											+ UserId,
									EncodingUtils.getBytes(postData, "BASE64"));
							wv.setWebViewClient(new WebViewClient());

							alert.setView(wv);
							alert.setNegativeButton("Close",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.dismiss();
										}
									});
							alert.show();

						}
					}
				});

			} else {

			}

		} catch (Exception e2) {

		}

		/*
		 * if(haveProfilePathDB()){
		 * 
		 * filePath = fetchProfilePath();
		 * 
		 * try { final Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		 * image.setImageBitmap(bitmap); } catch (Exception e) {
		 * e.printStackTrace(); }
		 * 
		 * }
		 */

		if (checkPrompt) {
			promptme.setChecked(true);
		} else {
			promptme.setChecked(false);
		}

		btnBrowser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				send = true;
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, LOAD_IMAGE_RESULTS);

			}
		});

		// try
		// {
		// getedit();
		// editName.setText(name);
		// editMobile.setText(mobile);
		// editEmail.setText(email);
		// editPinCode.setText(zipcode);
		// merchantcode.setText(merchcode);
		// dob.setText(dobi);
		// editCompanyName.setText(companyname);
		//
		// }
		// catch (Exception e1)
		// {
		// e1.printStackTrace();
		// }

		promptme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkPrompt = promptme.isChecked();
			}
		});
		btnSubmit = (Button) findViewById(R.id.btnSubmit);

		/*************************** INTERNET ********************************/
		webservice._context = this;
		/*************************** INTERNET ********************************/

		/**************************** set image from device ******************************/
		try {

			ContextWrapper cw = new ContextWrapper(getApplicationContext());
			File directory = cw.getDir("KiteverImageDir", Context.MODE_PRIVATE);
			File f = new File(directory.getPath(), "profile.jpg");

			if (f.exists()) {
				Bitmap bp = BitmapFactory.decodeStream(new FileInputStream(f));
				bp = getRoundedCornerBitmap(bp, 90);
				if (bp == null) {
					bp = BitmapFactory.decodeResource(getResources(),
							R.drawable.profile_propic);
				}
				images.setImageBitmap(bp);
				// images.setImageDrawable(Drawable.createFromPath(imagePath));
			} else {
				if (userProfilePicturePath != null
						&& !userProfilePicturePath.equalsIgnoreCase("")) {
					new DownloadFileFromURL().execute(userProfilePicturePath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**************************** set image from device ******************************/

		// fetchUserId();

		/*
		 * new webservice(null, webservice.GetUserProfileDetail.geturl(UserId),
		 * webservice.TYPE_GET, webservice.TYPE_GETPROFILE, this);
		 */
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// fetchUserId();

				if (send == true && imagePathh != null) {
					// loadimage(imagePathh);
					// loadImageToServer(imagePathh);
					secondBar.setVisibility(View.VISIBLE);
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String str = "";
							str = imagePathh;
							loadImageToServer(imagePathh);
						}
					}).start();

				}

				else {
					secondBar.setVisibility(View.VISIBLE);
					new RegisterUserAsync().execute("");
					// registerUser();
				}

				/*
				 * if(Validation.hasText(editName) &&
				 * Validation.hasText(editMobile) &&
				 * Validation.hasText(editCompanyName)&&
				 * Validation.hasText(editEmail) &&
				 * Validation.hasText(editPinCode) &&
				 * Validation.isEmailAddress(editEmail, true)){
				 * 
				 * mobile1=editMobile.getText().toString().trim();
				 * email1=editEmail.getText().toString().trim();
				 * pincode1=editPinCode.getText().toString().trim();
				 * uname1=editName.getText().toString().trim();
				 * merchantcodes=merchantcode.getText().toString().trim();
				 * webaddressi=webaddress.getText().toString().trim();
				 * dobi=dob.getText().toString().trim(); companyname
				 * =editCompanyName.getText().toString().trim();
				 * 
				 * fetchUserId(); //saving image to database
				 * 
				 * 
				 * //here
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * p=ProgressDialog.show(EditProfile.this,null,"Updating.....");
				 * 
				 * 
				 * new webservice(null,
				 * webservice.EditUserProfile.geturl(mobile1, email1, uname1,
				 * UserId, pincode1, "",
				 * "",merchantcodes,webaddressi,dobi,doe,companyname
				 * ,"pro","passs"), webservice.TYPE_POST, webservice.TYPE_EDIT,
				 * new ServiceHitListener() {
				 * 
				 * @Override public void onSuccess(Object Result, int id) { try
				 * { p.dismiss(); } catch (Exception e) {
				 * 
				 * }
				 * 
				 * EditUmodel mod=(EditUmodel) Result;
				 * if(mod.getUpdateUserProfile().get(0).getMsg().length()>0) {
				 * Toast
				 * .makeText(getApplicationContext(),""+mod.getUpdateUserProfile
				 * ().get(0).getMsg(),Toast.LENGTH_SHORT).show();
				 * dbObject.Open(); dbObject.deletepfofiledata();
				 * dbObject.EditProfile(UserId, uname1, mobile1, email1,
				 * pincode1,merchantcodes,webaddressi,dobi,companyname);
				 * dbObject.updateUserMobile(mobile1); dbObject.close(); }
				 * 
				 * }
				 * 
				 * @Override public void onError(String Error, int id) { try {
				 * p.dismiss(); } catch (Exception e) {
				 * 
				 * }
				 * 
				 * Toast.makeText(getApplicationContext(), Error,
				 * Toast.LENGTH_SHORT).show();
				 * 
				 * } });
				 * 
				 * 
				 * }
				 */
				// loadimage(imagePathh);
			}
		});
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
			super.onPageStarted(view, url, favicon);
			if (pd != null) {
				pd = new ProgressDialog(EditProfile.this);
			}
			pd.setTitle("Please wait");
			pd.setMessage("Page is loading..");
			pd.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (pd != null)
				pd.dismiss();
		}
	}

	String profileMsg = "";

	private class RegisterUserAsync extends AsyncTask<String, String, String> {

		String profilePic, userDOB, companyDOE;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mobile1 = editMobile.getText().toString().trim();
			email1 = editEmail.getText().toString().trim();
			pincode1 = editPinCode.getText().toString().trim();
			uname1 = editName.getText().toString().trim();
			merchantcodes = merchantcode.getText().toString().trim();
			webaddressi = webaddress.getText().toString().trim();
			dobi = dob.getText().toString().trim();
			companyname = editCompanyName.getText().toString().trim();
			merchant_Url = editStoreUrl.getText().toString();
			home_url = edit_home_url.getText().toString();
			merchantNameTxt = editMerchantName.getText().toString();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub


			SharedPreferences prfs = getSharedPreferences("profileData",
					Context.MODE_PRIVATE);
			String nname = prfs.getString("Name", "");
			String zzipcode = prfs.getString("pincode", "");
			String ccompanyname = prfs.getString("CompnayName", "");

			Rest rest = Rest.getInstance();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("Userid", UserId));
			nameValuePairs.add(new BasicNameValuePair("Pwd", UserPassword));
			nameValuePairs
					.add(new BasicNameValuePair("StoreURL", merchant_Url));
			nameValuePairs.add(new BasicNameValuePair("Home_URL", home_url));
			nameValuePairs.add(new BasicNameValuePair("DisplayName",
					merchantNameTxt));
			nameValuePairs.add(new BasicNameValuePair("Page",
					"UpdateUserProfileDetail"));
			nameValuePairs.add(new BasicNameValuePair("Pincode", pincode1));

			if ((LoginType.trim()).equalsIgnoreCase("2")) {
				nameValuePairs.add(new BasicNameValuePair("DOE", dobi));
				nameValuePairs.add(new BasicNameValuePair("User_DOB", ""));
			} else {
				nameValuePairs.add(new BasicNameValuePair("DOE", ""));
				nameValuePairs.add(new BasicNameValuePair("User_DOB", dobi));
			}

			nameValuePairs.add(new BasicNameValuePair("CompanyName",
					companyname));
			nameValuePairs
					.add(new BasicNameValuePair("ProfilePic", sendurlpath));
			nameValuePairs.add(new BasicNameValuePair("Name", uname1));

			String stringUrl = Apiurls.KIT19_BASE_URL.replace("?Page=", "");
			stringUrl = stringUrl.replace(" ", "");
			Log.i("Edit stringUrl", "REsponse-  " + stringUrl);
			String response = rest.post(stringUrl, nameValuePairs);
			Log.i("Edit Response", "REsponse-  " + response);
			// String profilepic = "";
			JSONObject jsonObj;
			if (response != null && !response.equalsIgnoreCase("")) {

				try {
					jsonObj = new JSONObject(response);
					JSONArray update = jsonObj
							.getJSONArray("UpdateUserProfile");
					JSONObject obj = update.getJSONObject(0);

					if (obj != null) {
						Editor editor = prfs.edit();
						if (obj.has("FName")) {
							if (obj.getString("FName") != null) {
								uname1 = obj.getString("FName");
								editor.putString("Name", obj.getString("FName"));
							}
						}
						// if (obj.has("Mobile")) {
						// editMobile.setText(obj.getString("Mobile"));
						// editor.putString("Mobile", obj.getString("Mobile"));
						// }
						if (obj.has("Msg")) {
							profileMsg = (obj.getString("Msg"));

						}
						if (obj.has("DisplayName")) {
							merchantNameTxt = (obj.getString("DisplayName"));
							editor.putString("MerchantName", merchantNameTxt);

						}
						if (obj.has("StoreUrl")) {
							merchant_Url = (obj.getString("StoreUrl"));
							editor.putString("Merchant_Url", merchant_Url);

						}

						if (obj.has("Home_URL")) {
							home_url = (obj.getString("Home_URL"));
							editor.putString("Home_Url", home_url);
						}

						if (obj.has("ProfilePic")) {
							if (obj.getString("ProfilePic") != null) {
								profilePic = obj.getString("ProfilePic");
							}

						}
						if (obj.has("pincode")) {
							if (obj.getString("pincode") != null) {
								pincode1 = obj.getString("pincode");
								editor.putString("pincode",
										obj.getString("pincode"));
							}
						}
						if (obj.has("Merchant_Code")) {
							if (obj.getString("Merchant_Code") != null) {
								merchcode = obj.getString("Merchant_Code");
								editor.putString("Merchant_Code",
										obj.getString("Merchant_Code"));
							}
						}
						if (obj.has("User_DOB")) {
							if (obj.getString("User_DOB") != null) {
								userDOB = obj.getString("User_DOB");
								editor.putString("User_DOB",
										obj.getString("User_DOB"));
							}
						}
						if (obj.has("DOE")) {
							if (obj.getString("DOE") != null) {
								companyDOE = obj.getString("DOE");
								editor.putString("DOE", obj.getString("DOE"));
							}
						}

						if (obj.has("CompanyName")) {
							if (obj.getString("CompanyName") != null) {
								companyname = obj.getString("CompanyName");
								editor.putString("CompnayName",
										obj.getString("CompanyName"));
							}
						}
						editor.commit();
					}
				} catch (Exception e) {
					// showDialogCall("Update Failed! " + e);
				}
			}
			if (profilePic != null && !profilePic.equalsIgnoreCase("")) {
				return profilePic;
			} else {
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute("");
			if (send && imagePathh != null) {
				if (result != null) {
					editName.setText(uname1);
					editPinCode.setText(pincode1);
					merchantcode.setText(merchcode);

					if ((LoginType.trim()).equalsIgnoreCase("2")) {
						dob.setHint("D.O.E");
						if (companyDOE != null && companyDOE.length() > 0) {
							dob.setHint(companyDOE);
						}
					} else {
						dob.setHint("D.O.B");
						if (userDOB != null && userDOB.length() > 0) {
							dob.setHint(userDOB);
						}
					}

					editCompanyName.setText(companyname);
					userProfilePicturePath = null;
					new DownloadFileFromURL().execute(result);
				} else {
					secondBar.setVisibility(View.GONE);
					showDialogCall(profileMsg);
				}
				send = false;
			} else {

				editName.setText(uname1);
				editPinCode.setText(pincode1);
				merchantcode.setText(merchcode);
				if (!companyDOE.equalsIgnoreCase("")) {
					dob.setText(companyDOE);
				} else {
					dob.setText(userDOB);
				}

				editCompanyName.setText(companyname);
				userProfilePicturePath = null;
				secondBar.setVisibility(View.GONE);
				showDialogCall(profileMsg);
			}

			if ((userType.trim()).equalsIgnoreCase("2")) {
				editMerchantName.setText(merchantNameTxt);
				editStoreUrl.setText(merchant_Url);
			}

		}
	}

	String profileImage = "";

	// private void registerUser(){
	// if (Validation.hasText(editName) && Validation.hasText(editMobile)
	// && Validation.hasText(editEmail)/*
	// * &&
	// * Validation.hasText(editPinCode
	// * )
	// */
	// && Validation.isEmailAddress(editEmail, true)) {
	//
	// }
	// }
	private void registerUser() {
		/*
		 * if (imagePathh==null) { imagePathh="image";
		 * 
		 * }
		 */

		if (Validation.hasText(editName) && Validation.hasText(editMobile)
				&& Validation.hasText(editEmail)/*
												 * &&
												 * Validation.hasText(editPinCode
												 * )
												 */
				&& Validation.isEmailAddress(editEmail, true)) {

			mobile1 = editMobile.getText().toString().trim();
			email1 = editEmail.getText().toString().trim();
			pincode1 = editPinCode.getText().toString().trim();
			uname1 = editName.getText().toString().trim();
			merchantcodes = merchantcode.getText().toString().trim();
			webaddressi = webaddress.getText().toString().trim();
			dobi = dob.getText().toString().trim();
			companyname = editCompanyName.getText().toString().trim();

			dbObject.Open();
			dbObject.deletepfofiledata();
			dbObject.EditProfile(UserId, uname1, mobile1, email1, pincode1,
					merchantcodes, webaddressi, dobi, companyname);
			dbObject.updateUserMobile(mobile1);
			dbObject.close();

			StringRequest stringRequest = new StringRequest(
					Request.Method.POST, Apiurls.KIT19_BASE_URL,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							// system.out.println("profile res: " + response);
							Toast.makeText(EditProfile.this,
									"UPDATED PROFILE :  " + response,
									Toast.LENGTH_LONG).show();
							// inflatedetailsfmserver();
							secondBar.setVisibility(View.GONE);
							JSONObject jsonObj;
							try {
								jsonObj = new JSONObject(response);
								JSONArray update = jsonObj
										.getJSONArray("UpdateUserProfile");
								JSONObject obj = update.getJSONObject(0);
								if (obj != null) {
									String name = obj.getString("FName");
									String mobile = obj.getString("Mobile");
									String email = obj.getString("EMail");
									String pincode = obj.getString("pincode");
									String merchantCode = obj
											.getString("Merchant_Code");
									String dob = obj.getString("User_DOB");
									String msg = obj.getString("Msg");
									String profilepic = obj
											.getString("ProfilePic");
									String loginType = obj
											.getString("LoginType");
									String comapnyName = obj
											.getString("CompanyName");
									editName.setText(name);
									editMobile.setText(mobile);
									editPinCode.setText(pincode);
									editEmail.setText(email);
									merchantcode.setText(merchantCode);
									editCompanyName.setText(comapnyName);
									SharedPreferences prfs = getSharedPreferences(
											"profileData", Context.MODE_PRIVATE);
									Editor editor = prfs.edit();
									editor.putString("EMail", email);
									editor.putString("pincode", pincode);
									editor.putString("CompnayName", comapnyName);
									// editor.putString("MerchantProfilePicturePath",
									// model.getLogin().get(0).getMerchantProfilePicturePath());
									editor.putString("User_DOB", dob);
									editor.putString("Mobile", mobile);
									// editor.putString("Balance",
									// model.getLogin().get(0).getBalance());
									// editor.putString("UserCategory",
									// model.getLogin().get(0).getUserCategory());
									editor.putString("ProfilePicturePath",
											profilepic);
									editor.putString("Name", name);
									// editor.putString("Plan",
									// model.getLogin().get(0).getPlan());
									// editor.putString("ExpiryDate",`
									// model.getLogin().get(0).getExpiryDate());
									editor.putString("Merchant_Code",
											merchantCode);
									// editor.putString("User_ID",
									// model.getLogin().get(0).getMerchant_Code());
									// profileImage=profilepic;
									editor.commit();

									showDialogCall(msg);

								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								// e.printStackTrace();
								Toast.makeText(
										EditProfile.this,
										"Failed UPDATED PROFILE :  " + response,
										Toast.LENGTH_LONG).show();
							}

							// Log.e("all data", response.toString());
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Toast.makeText(EditProfile.this,
									"Something wrong : ", Toast.LENGTH_LONG)
									.show();
							secondBar.setVisibility(View.GONE);
						}
					}) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("Page", "UpdateUserProfileDetail");
					// params.put("Mobile", mobile1);
					// params.put("Email", email1);
					params.put("Name", uname1);
					params.put("Userid", UserId);
					params.put("Pincode", editPinCode.getText().toString());
					// params.put("Gender", "");
					// params.put("Address", "");
					// params.put("Merchant_Code", merchantcodes);
					// params.put("Merchant_Url", webaddressi);
					params.put("User_DOB", dobi);
					params.put("DOE", "");
					params.put("CompanyName", companyname);
					if (sendurlpath == null || sendurlpath.equalsIgnoreCase("")) {
						params.put("ProfilePic", "");
					} else {
						params.put("ProfilePic", sendurlpath);
					}
					params.put("Pwd", UserPassword);
					/*
					 * params.put(KEY_EMAIL, email);
					 * 
					 * 
					 * nameValuePairs.add(new BasicNameValuePair("Page",
					 * "UpdateUserProfileDetail")); nameValuePairs.add(new
					 * BasicNameValuePair("Mobile", "9717077554"));
					 * nameValuePairs.add(new BasicNameValuePair("Email",
					 * "mailtoshekhar@gmail.com")); nameValuePairs.add(new
					 * BasicNameValuePair("Name", "Shekhar"));
					 * nameValuePairs.add(new BasicNameValuePair("Userid",
					 * "25062")); nameValuePairs.add(new
					 * BasicNameValuePair("Pincode", "110018"));
					 * nameValuePairs.add(new BasicNameValuePair("Gender",
					 * "Male")); nameValuePairs.add(new
					 * BasicNameValuePair("Address", "ggaggagag"));
					 * nameValuePairs.add(new
					 * BasicNameValuePair("Merchant_Code", "1234"));
					 * 
					 * nameValuePairs.add(new BasicNameValuePair("User_DOB",
					 * "hshh")); nameValuePairs.add(new
					 * BasicNameValuePair("DOE", "hshsh"));
					 * nameValuePairs.add(new BasicNameValuePair("CompanyName",
					 * "hshhs")); nameValuePairs.add(new
					 * BasicNameValuePair("ProfilePic", "hshhsh"));
					 * nameValuePairs.add(new BasicNameValuePair("Pwd",
					 * "19560"));
					 */

					return params;
				}

			};

			RequestQueue requestQueue = Volley.newRequestQueue(this);
			requestQueue.add(stringRequest);
		}

	}

	private void showDialogCall(String msg) {
		new AlertDialog.Builder(EditProfile.this).setCancelable(false)
				.setMessage(msg)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();

					}
				}).show();
	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			secondBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {// http://nowconnect.in/10/2085fb5e8801fcac23e4967062ab6cd1.jpg

				URL url = new URL("http://" + f_url[0]);

				URLConnection conection = url.openConnection();
				conection.connect();
				// getting file length
				int lenghtOfFile = conection.getContentLength();

				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				ContextWrapper cw = new ContextWrapper(getApplicationContext());
				// path to /data/data/yourapp/app_data/imageDir
				File directory = cw.getDir("KiteverImageDir",
						Context.MODE_PRIVATE);
				// Create imageDir
				File mypath = new File(directory, "profile.jpg");

				// FileOutputStream fos = null;
				// try {
				// fos = new FileOutputStream(mypath);
				// // Use the compress method on the BitMap object to write
				// image to the OutputStream
				// bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
				// } catch (Exception e) {
				// e.printStackTrace();
				// } finally {
				// try {
				// fos.close();
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }

				OutputStream output = new FileOutputStream(mypath); // changed
																	// from
				// "/sdcard/SMS19/...

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) // CAN WE DO THIS IN
															// JAVA ? -khogen
															// (o.O)
				{
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				// Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {

			// Displaying downloaded image into image view
			// Reading image path from sdcard
			ContextWrapper cw = new ContextWrapper(getApplicationContext());
			File directory = cw.getDir("KiteverImageDir", Context.MODE_PRIVATE);
			File f = new File(directory.getPath(), "profile.jpg");

			// if (f.exists()) {
			// Bitmap bp = BitmapFactory.decodeStream(new FileInputStream(f));
			// bp = getRoundedCornerBitmap(bp, 90);
			// if(bp==null){
			// bp = BitmapFactory.decodeResource(getResources(),
			// R.drawable.profile_propic);
			// }
			// images.setImageBitmap(bp);
			// // images.setImageDrawable(Drawable.createFromPath(imagePath));
			// }

			String imagePath = f.getPath();
			// setting downloaded into image view
			Bitmap bp = BitmapFactory.decodeFile(imagePath);
			if (bp != null) {
				bp = getRoundedCornerBitmap(bp, 90);
				if (bp == null) {
					bp = BitmapFactory.decodeResource(getResources(),
							R.drawable.profile_propic);
				}
				images.setImageBitmap(bp);
			}
			// images.setImageDrawable(Drawable.createFromPath(imagePath));
			secondBar.setVisibility(View.GONE);
			if (userProfilePicturePath == null)
				new AlertDialog.Builder(EditProfile.this)
						.setCancelable(false)
						.setMessage("Profile Updated Successfully")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();

									}
								}).show();
		}

	}

	/******************************* finish upload from server ***************************/

	// public void fetchUserId()
	// {
	// dbObject.Open();
	//
	// Cursor c;
	//
	// c= dbObject.getLoginDetails();
	//
	//
	// while(c.moveToNext())
	// {
	//
	// UserId = c.getString(3);
	// //LoginType = c.getString(7);
	// UserPassword= c.getString(5);
	//
	//
	// }
	//
	// dbObject.close();
	// }

	public boolean haveProfilePathDB() {

		dbObject.Open();

		Cursor c;
		c = dbObject.getProfilepath();
		while (c.moveToNext()) {

			dbObject.close();
			return true;

		}
		dbObject.close();
		return false;
	}

	public String fetchProfilePath() {

		dbObject.Open();

		Cursor c;

		String datapath = "";
		c = dbObject.getProfilepath();

		while (c.moveToNext()) {
			datapath = c.getString(1);
		}

		dbObject.close();
		return datapath;

	}

	public void getedit() {
		dbObject.Open();

		Cursor c;

		c = dbObject.getProfile();

		while (c.moveToNext()) {
			name = c.getString(1);
			mobile = c.getString(2);
			email = c.getString(3);
			zipcode = c.getString(4);
			merchcode = c.getString(5);
			dobi = c.getString(7);
			companyname = c.getString(8);
		}
		dbObject.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.edit_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		/*
		 * case R.id.rfshMenu: { inflatedetailsfmserver();
		 * 
		 * return true; }
		 */

		case R.id.transactionMenu: {
			Intent i = new Intent(EditProfile.this, Transaction.class);
			startActivity(i);
			return true;
		}

		case R.id.editProfileMenu: {
			Intent i = new Intent(EditProfile.this, EditProfile.class);
			startActivity(i);
			return true;
		}
		case R.id.schedulemsgrMenu: {
			Intent i = new Intent(EditProfile.this, ScheduleList.class);
			startActivity(i);
			return true;
		}

		case R.id.termsconditionMenu: {
			Intent i = new Intent(EditProfile.this, TermsAndCondition.class);
			startActivity(i);
			return true;
		}

		case R.id.HowtoUseMenu: {
			/*
			 * Intent i = new Intent(EditProfile.this,HowToUse.class);
			 * startActivity(i);
			 */
			AlertDialog.Builder alert = new AlertDialog.Builder(
					EditProfile.this);
			alert.setTitle("How to use");

			WebView wv = new WebView(EditProfile.this);
			wv.loadUrl("http://kitever.com/howtouse.aspx");
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

			return true;
		}

		/*
		 * case R.id.logout:{
		 * 
		 * callLogoutMethod();
		 * ////Log.w("LOG OUT! LOG OUT! LOGOUT! LOGOUT! LOGOUT! LOGOUT! ",
		 * "@@@@@@@@@@@@@@@@@@"); try { MainActivity.signOutFromGplus(); }
		 * catch(Exception e){} return true; }
		 */

		default: {
			onBackPressed();
			return true;
		}
		}

	}

	/**
	 * @return
	 */
	public boolean inflatedetailsfmserver() {
		// fetchUserId();
		new webservice(null, webservice.GetUserProfileDetail.geturl(UserId),
				webservice.TYPE_GET, webservice.TYPE_GETPROFILE,
				new ServiceHitListener() {
					@Override
					public void onSuccess(Object Result, int id) {
						GetProfileData model = (GetProfileData) Result;

						String name = model.getProfileDetails().get(0)
								.getFName();
						String moblie = model.getProfileDetails().get(0)
								.getMobile();
						String gmail = model.getProfileDetails().get(0)
								.getEMail();
						String zipcode = model.getProfileDetails().get(0)
								.getPincode();
						String code = model.getProfileDetails().get(0)
								.getMerchant_Code();
						String weburl = model.getProfileDetails().get(0)
								.getAddress();
						String drb = model.getProfileDetails().get(0)
								.getUser_DOB();
						ppath = model.getProfileDetails().get(0).getColumn1();

						currency = model.getProfileDetails().get(0)
								.getCurrency();
						country = model.getProfileDetails().get(0).getCountry();
						String plan = model.getProfileDetails().get(0)
								.getUserCategory();
						String companyname = model.getProfileDetails().get(0)
								.getCompnayName();
						String userdob = model.getProfileDetails().get(0)
								.getUser_DOB();
						String expirydate = model.getProfileDetails().get(0)
								.getExpiryDate();
						String doe = model.getProfileDetails().get(0).getDOE();
						String planname = model.getProfileDetails().get(0)
								.getPlan();

						Toast.makeText(getApplicationContext(),
								"inflating success companyname=" + companyname,
								Toast.LENGTH_LONG).show();
						// expiryDate.setText("1"+ppath);

						if (plan != null && plan.equalsIgnoreCase("2")) {
							LoginType = "2";
						} else {
							LoginType = "1";
						}

						SharedPreferences sharedPref = getSharedPreferences(
								"profileData", Context.MODE_PRIVATE);
						// sharedPref.edit().clear().commit();
						SharedPreferences.Editor editr = sharedPref.edit();
						editr.putString("nam", name);
						editr.putString("mobil", moblie);
						editr.putString("gmai", gmail);
						editr.putString("zipcod", zipcode);
						editr.putString("ppat", ppath);
						editr.putString("currenc", currency);
						editr.putString("countr", country);
						editr.putString("companynam", companyname);
						editr.putString("logintyp", LoginType);
						editr.putString("userdo", userdob);
						editr.putString("do", doe);
						editr.putString("expirydat", expirydate);
						editr.putString("plannam", planname);
						editr.commit();

						editName.setText(name);
						editMobile.setText(moblie);
						editPinCode.setText(zipcode);
						editEmail.setText(gmail);
						merchantcode.setText(code);
						webaddress.setText(weburl);
						dob.setText(drb);
						editCurrency.setText(currency);
						editCountry.setText(country);
						editCompanyName.setText(companyname);
						if (expirydate != null
								&& !expirydate.equalsIgnoreCase(""))
							expiryDate.setText(expirydate);
						else
							expiryDate.setText("forever");
						planName.setText(planname + " active till ");

						secondBar.setVisibility(View.GONE);
						// fetchUserId();

						dbObject.Open();
						dbObject.deletepfofiledata();
						dbObject.EditProfile(UserId, name, moblie, gmail,
								zipcode, code, weburl, drb, companyname);
						dbObject.updateUserMobile(moblie);
						dbObject.close();

						if (ppath == null || ppath.length() < 3) {

						} else {
							String checkhttp = ppath.substring(0, 7);

							// //Log.w("RM", "RM ::::::::(checkhttp):" +
							// checkhttp);

							if (checkhttp.equalsIgnoreCase("http://")) {

							} else {
								ppath = "http://" + ppath;
							}

							// //Log.w("RM", "RM ::::::::(logo):" + ppath);

							new DownloadFileFromURL().execute(ppath);
							/*
							 * try { String EmergencyMessage =
							 * model.getProfileDetails
							 * ().get(0).getEmergencyMessage();
							 * 
							 * try { Emergency.desAct.finish(); } catch
							 * (Exception e) { }
							 * 
							 * if(!(EmergencyMessage.equalsIgnoreCase("NO"))) {
							 * Intent rt = new
							 * Intent(EditProfile.this,Emergency.class);
							 * rt.putExtra("Emergency", EmergencyMessage);
							 * startActivity(rt);
							 * 
							 * } } catch (Exception e) { e.printStackTrace(); }
							 */
						}

					}

					@Override
					public void onError(String Error, int id) {

						Toast.makeText(getApplicationContext(), Error,
								Toast.LENGTH_SHORT).show();

					}
				});

		return true;
	}

	@Override
	public void onBackPressed() {
		try {
			p.dismiss();
		} catch (Exception e) {
		}
		finish();
	}

	private void callLogoutMethod() {

		new AlertDialog.Builder(this)
				.setCancelable(false)
				.setMessage(
						"Are you Sure you want to Exit? All your chat data will be deleted.")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						// delete all database
						DatabaseCleanState();

						Toast.makeText(getApplicationContext(),
								"Logout Successfully", Toast.LENGTH_SHORT)
								.show();

						Intent i = new Intent(EditProfile.this, SMS19.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(i);
						finish();

					}

					public SQLiteDatabase getDBObject() {
						return dbObject.db;
					}

					private void DatabaseCleanState() {
						// TODO Auto-generated method stub
						dbObject.Open();
						dbObject.onUpgrade(getDBObject(), 1, 1);
						dbObject.close();
					}

				})
				.setNegativeButton("CANCEL",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK
				&& data != null) {
			Uri pickedImage = data.getData();
			khogenpickedImage = pickedImage;

			String[] filePath = { MediaStore.Images.Media.DATA };
			khogenfilePath = filePath;
			Cursor cursor = getContentResolver().query(pickedImage, filePath,
					null, null, null);

			cursor.moveToFirst();
			imagePathh = cursor.getString(cursor.getColumnIndex(filePath[0]));

			startCropImage(new File(imagePathh));// for image cropping

			// //Log.w("TAG", "Imagepath:" + imagePathh);
			// fetchUserId();

			if (haveProfilePathDB()) {
				dbObject.Open();
				dbObject.updateProfilePath(imagePathh);
				dbObject.close();
			} else {
				dbObject.Open();
				dbObject.addProfilePicks(UserId, imagePathh);
				dbObject.close();
			}

			if (finalBitmap != null) {
				finalBitmap.recycle();
			}
			if (imagePathh.trim().length() != 0) {
				finalBitmap = Utils.decodeFile(imagePathh,
						GlobalData.profilepicthmb, GlobalData.profilepicthmb);

			}

			// here

			// Toast.makeText(getApplicationContext(),
			// "img frm galley: "+imagePathh, Toast.LENGTH_SHORT).show();
			// loadimage(imagePathh);

			setimege = imagePathh;
			Bitmap bp = BitmapFactory.decodeFile(setimege);
			bp = getRoundedCornerBitmap(bp, 90);
			if (bp == null) {
				bp = BitmapFactory.decodeResource(getResources(),
						R.drawable.profile_propic);
			}
			// images.setImageBitmap(bp);
			// setCircleImage(images,bp);

			cursor.close();
			// Toast.makeText(getApplicationContext(), "cursor closed",
			// Toast.LENGTH_SHORT).show();

		}
		if (REQUEST_CODE_CROP_IMAGE == requestCode) {
			if (data != null && resultCode == RESULT_OK) {
				String path = data.getStringExtra(CropImage.IMAGE_PATH);
				if (path == null) {

					return;
				}
				Bitmap bp = BitmapFactory.decodeFile(path);
				bp = getRoundedCornerBitmap(bp, 90);
				if (bp == null) {
					bp = BitmapFactory.decodeResource(getResources(),
							R.drawable.profile_propic);
				}
				images.setImageBitmap(bp);
			} else {
				Toast.makeText(EditProfile.this, "error in browsing image",
						Toast.LENGTH_LONG).show();
			}
			// bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
			// mImageView.setImageBitmap(bitmap);
		}
	}

	// private void loadImageToServer1(final String imagetoupload) {
	// secondBar.setVisibility(View.VISIBLE);
	//
	// // try {
	// // imagetoupload2 = imagetoupload.substring(imagetoupload
	// // .lastIndexOf("/"));
	// final String[] img=imagetoupload.split("/");
	// String imgname="";
	// imgname=img[img.length-1];
	// String file="";
	// file=imagetoupload;
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // }
	// Bitmap bp = BitmapFactory.decodeFile(setimege);
	// final String str=getStringImage(bp);
	// StringRequest stringRequest = new StringRequest(Request.Method.POST,
	// Apiurls.KIT19_BASE_URL, new Response.Listener<String>() {
	// @Override
	// public void onResponse(String response) {
	// Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
	// }
	// }, new Response.ErrorListener() {
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
	// }
	// }) {
	// @Override
	// protected Map<String, String> getParams() {
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("Page", "UploadFile");
	// params.put("file", imagetoupload);
	// params.put("fileName", str);
	// params.put("user_id", UserId);
	//
	// return params;
	// }
	//
	// };
	//
	// RequestQueue requestQueue = Volley.newRequestQueue(this);
	// requestQueue.add(stringRequest);
	//
	// }
	// public String getStringImage(Bitmap bmp){
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	// byte[] imageBytes = baos.toByteArray();
	// String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
	// return encodedImage;
	// }

	private void loadImageToServer(String imagetoupload) {// /storage/sdcard0/Kitever/Media/Kitever
															// Images/Kitever_1472461770178.jpg
		getImageUrl();
		try {
			imagetoupload2 = imagetoupload.substring(imagetoupload
					.lastIndexOf("/"));// /Kitever_1472461770178.jpg
		} catch (Exception e) {
			e.printStackTrace();
		}
		// sendurlpath = urlpath + "/" + UserId + imagetoupload2;//
		sendurlpath = "nowconnect.in" + "/" + UserId + imagetoupload2; // nowconnect.in/10016/Kitever_1472461770178.jpg
		String charset = "UTF-8";
		File uploadFile = new File(imagetoupload);// /storage/sdcard0/Kitever/Media/Kitever
													// Images/Kitever_1472461770178.jpg
		String requestURL = Apiurls.KIT19_BASE_URL;
		final String[] img = imagetoupload.split("/");// [, storage, sdcard0,
														// Kitever, Media,
														// Kitever Images,
														// Kitever_1472461770178.jpg]
		String imgname = "";
		imgname = img[img.length - 1]; // Kitever_1472461770178.jpg
		try {
			MultipartUtility multipart = new MultipartUtility(requestURL,
					charset);

			multipart.addHeaderField("user_id", UserId);

			multipart.addFormField("Page", "UploadFile");
			multipart.addFormField("fileName", UserId + "/" + imgname);

			multipart.addFilePart("file", uploadFile);

			List<String> response = multipart.finish();
			String res = response.get(0);
			final String[] splitRes = res.split(":");
			runOnUiThread(new Runnable() {
				//
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// secondBar.setVisibility(View.VISIBLE);
					if (splitRes[1].equalsIgnoreCase("Success")) {
						// send=false;
						// registerUser();
						new RegisterUserAsync().execute("");

					} else {
						secondBar.setVisibility(View.GONE);
						new AlertDialog.Builder(EditProfile.this)
								.setCancelable(false)
								.setMessage("update failed")
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
			});

			// if(splitRes[1].equalsIgnoreCase("Success")){
			// registerUser();
			// }
			// else{
			// runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// secondBar.setVisibility(View.VISIBLE);
			// }
			// });
			// }

			// //system.out.println("SERVER REPLIED:");
			//
			// for (String line : response) {
			// //system.out.println(line);
			// }
		} catch (IOException ex) {
			// system.err.println(ex);
		}
	}

	private void getImageUrl() {
		// secondBar.setVisibility(View.VISIBLE);
		// try {
		// imagetoupload2 = imagetoupload.substring(imagetoupload
		// .lastIndexOf("/"));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// setimege = imagetoupload;
		// Toast.makeText(getApplicationContext(),"above webservice loadimage",
		// Toast.LENGTH_SHORT).show();

		new webservice(null, webservice.GetFTPHostDetail.geturl("image"),
				webservice.TYPE_GET, webservice.TYPE_FTP_UPLD,
				new ServiceHitListener() {

					public void onSuccess(Object Result, int id) {

						GetFTPCre gpmodel = (GetFTPCre) Result;

						/*
						 * try { String EmergencyMessage =
						 * gpmodel.getGetFTPHostDetail
						 * ().get(0).getEmergencyMessage();
						 * 
						 * try { Emergency.desAct.finish(); } catch (Exception
						 * e) { }
						 * 
						 * if(!(EmergencyMessage.equalsIgnoreCase("NO"))) {
						 * Intent rt = new
						 * Intent(EditProfile.this,Emergency.class);
						 * rt.putExtra("Emergency", EmergencyMessage);
						 * startActivity(rt);
						 * 
						 * } } catch (Exception e1) { e1.printStackTrace(); }
						 */

						String FTP_USER = gpmodel.getGetFTPHostDetail().get(0)
								.getFtpUser();
						String FTP_PASS = gpmodel.getGetFTPHostDetail().get(0)
								.getFtpPassword();
						String FTP_HOST = gpmodel.getGetFTPHostDetail().get(0)
								.getHostName();
						urlpath = gpmodel.getGetFTPHostDetail().get(0)
								.getFtpUrl();

					}

					@Override
					public void onError(String Error, int id) {

					}
				});

	}

	private void loadimage(final String imagetoupload) {
		secondBar.setVisibility(View.VISIBLE);
		try {
			imagetoupload2 = imagetoupload.substring(imagetoupload
					.lastIndexOf("/"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setimege = imagetoupload;
		// Toast.makeText(getApplicationContext(),"above webservice loadimage",
		// Toast.LENGTH_SHORT).show();

		new webservice(null, webservice.GetFTPHostDetail.geturl("image"),
				webservice.TYPE_GET, webservice.TYPE_FTP_UPLD,
				new ServiceHitListener() {

					public void onSuccess(Object Result, int id) {

						GetFTPCre gpmodel = (GetFTPCre) Result;

						/*
						 * try { String EmergencyMessage =
						 * gpmodel.getGetFTPHostDetail
						 * ().get(0).getEmergencyMessage();
						 * 
						 * try { Emergency.desAct.finish(); } catch (Exception
						 * e) { }
						 * 
						 * if(!(EmergencyMessage.equalsIgnoreCase("NO"))) {
						 * Intent rt = new
						 * Intent(EditProfile.this,Emergency.class);
						 * rt.putExtra("Emergency", EmergencyMessage);
						 * startActivity(rt);
						 * 
						 * } } catch (Exception e1) { e1.printStackTrace(); }
						 */

						String FTP_USER = gpmodel.getGetFTPHostDetail().get(0)
								.getFtpUser();
						String FTP_PASS = gpmodel.getGetFTPHostDetail().get(0)
								.getFtpPassword();
						String FTP_HOST = gpmodel.getGetFTPHostDetail().get(0)
								.getHostName();
						urlpath = gpmodel.getGetFTPHostDetail().get(0)
								.getFtpUrl();
						String path = "";
						sendurlpath = urlpath + "/" + UserId + imagetoupload2;
						path = sendurlpath;
						Toast.makeText(getApplicationContext(), sendurlpath,
								Toast.LENGTH_LONG).show();
						registerUser();
						// Toast.makeText(getApplicationContext(),
						// "success loadimage sendurlpath="+sendurlpath,
						// Toast.LENGTH_LONG).show();

						// //Log.w("PROFILEPICTURE", "PROFILEPICTURE"
						// + imagetoupload);
						// //Log.w("LOAD_IMAGE_URL", "LOAD_IMAGE_URL" +
						// sendurlpath
						// + "IMAGEPATH" + imagetoupload);
						// p.dismiss();

						File f = new File(imagetoupload);

						FTPClient client = new FTPClient();
						try {
							if (android.os.Build.VERSION.SDK_INT > 9) {

								StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
										.permitAll().build();
								StrictMode.setThreadPolicy(policy);
							}
							client.connect(FTP_HOST, 21);
							client.login(FTP_USER, FTP_PASS);
							client.setType(FTPClient.TYPE_AUTO);
							// client.changeDirectory("/upload/");
							// client.upload(f, new MyTransferListener());
							client.upload(f);
							// client.disconnect(true);

						} catch (Exception e) {
							e.printStackTrace();
							try {
								client.disconnect(true);
							} catch (Exception e2) {

							}
						}
						// registerUser();

						new AlertDialog.Builder(EditProfile.this)
								.setCancelable(false)
								.setMessage("update profile successfully")
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

					@Override
					public void onError(String Error, int id) {

					}
				});
	}

	public class MyTransferListener implements FTPDataTransferListener {

		public void started() {
		}

		public void transferred(int length) {
		}

		public void completed() {
			// //Log.w("LOAD_IMAGE_URL", "LOAD_IMAGE_URL" + sendurlpath);
			// fetchUserId();
			// Toast.makeText(getApplicationContext(),
			// "above webservice mytransferlistener",
			// Toast.LENGTH_SHORT).show();
			new webservice(null, webservice.ProfilePic.geturl(UserId,
					sendurlpath), webservice.TYPE_GET,
					webservice.TYPE_IMAGE_UPLOAD, new ServiceHitListener() {
						@Override
						public void onSuccess(Object Result, int id) {
							Imageupload model = (Imageupload) Result;

							/*
							 * try { String EmergencyMessage =
							 * model.getProfilePicture
							 * ().get(0).getEmergencyMessage();
							 * 
							 * try { Emergency.desAct.finish(); } catch
							 * (Exception e) { }
							 * 
							 * if(!(EmergencyMessage.equalsIgnoreCase("NO"))) {
							 * Intent rt = new
							 * Intent(EditProfile.this,Emergency.class);
							 * rt.putExtra("Emergency", EmergencyMessage);
							 * startActivity(rt);
							 * 
							 * } } catch (Exception e) { e.printStackTrace(); }
							 */
							Toast.makeText(getApplicationContext(),
									"image uploaded successfully",
									Toast.LENGTH_LONG).show();

							Bitmap bp = BitmapFactory.decodeFile(setimege);
							images.setImageBitmap(bp);

							// inflatedetailsfmserver();
							new SetuserVcardAsyncTask().execute();

						}

						@Override
						public void onError(String Error, int id) {

						}
					});
		}

		public void aborted() {
		}

		public void failed() {
		}

	}

	@Override
	public void onClick(View v) {

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dpd = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						if (LoginType.equalsIgnoreCase("2")) {
							if (mYear >= (year)) {
								dob.setError(null);
								dob.setText(dayOfMonth + "-"
										+ (monthOfYear + 1) + "-" + year);
							} else if (mYear == (year) && mMonth >= monthOfYear) {
								dob.setError(null);
								dob.setText(dayOfMonth + "-"
										+ (monthOfYear + 1) + "-" + year);
							} else if (mYear == (year)
									&& mMonth == (monthOfYear)
									&& mDay >= dayOfMonth) {
								dob.setError(null);
								dob.setText(dayOfMonth + "-"
										+ (monthOfYear + 1) + "-" + year);
							}
						} else {
							if (mYear >= (year + 13)) {
								dob.setError(null);
								dob.setText(dayOfMonth + "-"
										+ (monthOfYear + 1) + "-" + year);
							} else if (mYear == (year + 13)
									&& mMonth >= monthOfYear) {
								dob.setError(null);
								dob.setText(dayOfMonth + "-"
										+ (monthOfYear + 1) + "-" + year);
							} else if (mYear == (year + 13)
									&& mMonth == (monthOfYear)
									&& mDay >= dayOfMonth) {
								dob.setError(null);
								dob.setText(dayOfMonth + "-"
										+ (monthOfYear + 1) + "-" + year);
							} else
								dob.setError("For above 13 years");
						}

					}
				}, mYear, mMonth, mDay);
		Long startDatel = DateHelper.getDate(mYear - 13, mMonth, mDay);
		dpd.getDatePicker().setMaxDate(startDatel);
		dpd.show();

	}

	class SetuserVcardAsyncTask extends AsyncTask<Void, Void, Void> {
		String name="",companyname="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			name = editName.getText().toString();
			companyname = editCompanyName.getText().toString();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				ProviderManager.getInstance().addIQProvider("vCard",
						"vcard-temp", new VCardProvider());

				SmackConfiguration.setPacketReplyTimeout(300000);
				byte[] byteArray = null;

				if (finalBitmap != null) {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
							stream);
					byteArray = stream.toByteArray();
				}

				String user_jid = chatPrefs.getString("user_jid", "");
				try {
					GlobalData.dbHelper.updateUserDatainDb(user_jid, name,
							byteArray);
				} catch (Exception e) {
					e.printStackTrace();
				}

				VCard vCard = new VCard();

				String str = "";
				str = mydetail.getRemote_jid();
				vCard.load(GlobalData.connection, mydetail.getRemote_jid());
				vCard.setFirstName(name);
				vCard.setOrganization(companyname);
				if (byteArray != null) {
					vCard.setAvatar(byteArray);
				}

				if (GlobalData.connection != null
						&& GlobalData.connection.isAuthenticated()) {
					vCard.save(GlobalData.connection);

					Utils.printLog("Save Vcard Successfully");
					GlobalData.dbHelper.saveUserDatainDb(chatPrefs, name,
							companyname, byteArray);
					Utils.printLog("Save databse Successfully");

					/*
					 * Presence presence = new
					 * Presence(Presence.Type.available);
					 * if(custom_status.equalsIgnoreCase("")){
					 * custom_status=GlobalData.AVAILABLE; }
					 * presence.setStatus(custom_status);
					 */
					Presence presence = null;
					if (custom_status.equalsIgnoreCase("")) {
						custom_status = GlobalData.AVAILABLE;
						presence = new Presence(
								Presence.Type.available,
								custom_status
										+ GlobalData.status_time_separater + "",
								0, Mode.available);
						presence.setStatus(GlobalData.AVAILABLE
								+ GlobalData.status_time_separater + "");
					} else {

						if (GlobalData.AVAILABLE
								.equalsIgnoreCase(custom_status)) {
							presence = new Presence(Presence.Type.available,
									custom_status
											+ GlobalData.status_time_separater
											+ "", 0, Mode.available);
							presence.setStatus(GlobalData.AVAILABLE
									+ GlobalData.status_time_separater + "");
						} else if (GlobalData.BUSY
								.equalsIgnoreCase(custom_status)) {
							presence = new Presence(Presence.Type.available,
									"busy" + GlobalData.status_time_separater
											+ "", 0, Mode.available);
							presence.setStatus(GlobalData.BUSY
									+ GlobalData.status_time_separater + "");
						} else if (GlobalData.INVISIABLE
								.equalsIgnoreCase(custom_status)) {
							presence = new Presence(Presence.Type.available,
									"invisiable"
											+ GlobalData.status_time_separater
											+ "", 0, Mode.available);
							presence.setStatus(GlobalData.INVISIABLE
									+ GlobalData.status_time_separater + "");
						} else if (GlobalData.ONLY
								.equalsIgnoreCase(custom_status)) {
							presence = new Presence(Presence.Type.available,
									"original"
											+ GlobalData.status_time_separater
											+ "", 0, Mode.available);
							presence.setStatus(GlobalData.ONLY
									+ GlobalData.status_time_separater + "");
						}
						// presence = new Presence(Presence.Type.available,
						// custom_status+GlobalData.status_time_separater+"", 1,
						// Mode.available);
					}

					GlobalData.connection.sendPacket(presence);

				} else {
					Utils.printLog("failed data save in vcard");
				}

			} catch (Exception e) {
				Utils.printLog("Excption in saving");
				e.printStackTrace();
			}
			return null;
		}

	}

	private void startCropImage(File file) {

		Intent intent = new Intent(EditProfile.this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
		intent.putExtra(CropImage.SCALE, true);
		// intent.putExtra(CropImage.circleCrop,true);
		intent.putExtra(CropImage.ASPECT_X, 3);
		intent.putExtra(CropImage.ASPECT_Y, 2);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}


	public float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		if (bitmap != null) {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			// final Rect rect = new Rect(0, 0, bitmap.getWidth(),
			// bitmap.getWidth());
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = pixels;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		}
		return null;
	}

	private class MultipartUtility {
		private final String boundary;
		private static final String LINE_FEED = "\r\n";
		private HttpURLConnection httpConn;
		private String charset;
		private OutputStream outputStream;
		private PrintWriter writer;

		/**
		 * This constructor initializes a new HTTP POST request with content
		 * type is set to multipart/form-data
		 * 
		 * @param requestURL
		 * @param charset
		 * @throws IOException
		 */
		public MultipartUtility(String requestURL, String charset)
				throws IOException {
			this.charset = charset;

			// creates a unique boundary based on time stamp
			boundary = "===" + System.currentTimeMillis() + "===";

			URL url = new URL(requestURL);
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setUseCaches(false);
			httpConn.setDoOutput(true); // indicates POST method
			httpConn.setDoInput(true);
			httpConn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);
			httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
			outputStream = httpConn.getOutputStream();
			writer = new PrintWriter(new OutputStreamWriter(outputStream,
					charset), true);
		}

		/**
		 * Adds a form field to the request
		 * 
		 * @param name
		 *            field name
		 * @param value
		 *            field value
		 */
		public void addFormField(String name, String value) {
			writer.append("--" + boundary).append(LINE_FEED);
			writer.append(
					"Content-Disposition: form-data; name=\"" + name + "\"")
					.append(LINE_FEED);
			writer.append("Content-Type: text/plain; charset=" + charset)
					.append(LINE_FEED);
			writer.append(LINE_FEED);
			writer.append(value).append(LINE_FEED);
			writer.flush();
		}

		/**
		 * Adds a upload file section to the request
		 * 
		 * @param fieldName
		 *            name attribute in <input type="file" name="..." />
		 * @param uploadFile
		 *            a File to be uploaded
		 * @throws IOException
		 */
		public void addFilePart(String fieldName, File uploadFile)
				throws IOException {
			String fileName = uploadFile.getName();
			writer.append("--" + boundary).append(LINE_FEED);
			writer.append(
					"Content-Disposition: form-data; name=\"" + fieldName
							+ "\"; filename=\"" + fileName + "\"").append(
					LINE_FEED);
			writer.append(
					"Content-Type: "
							+ URLConnection.guessContentTypeFromName(fileName))
					.append(LINE_FEED);
			writer.append("Content-Transfer-Encoding: binary")
					.append(LINE_FEED);
			writer.append(LINE_FEED);
			writer.flush();

			FileInputStream inputStream = new FileInputStream(uploadFile);
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.flush();
			inputStream.close();

			writer.append(LINE_FEED);
			writer.flush();
		}

		/**
		 * Adds a header field to the request.
		 * 
		 * @param name
		 *            - name of the header field
		 * @param value
		 *            - value of the header field
		 */
		public void addHeaderField(String name, String value) {
			writer.append(name + ": " + value).append(LINE_FEED);
			writer.flush();
		}

		/**
		 * Completes the request and receives response from the server.
		 * 
		 * @return a list of Strings as response in case the server returned
		 *         status OK, otherwise an exception is thrown.
		 * @throws IOException
		 */
		public List<String> finish() throws IOException {
			List<String> response = new ArrayList<String>();

			writer.append(LINE_FEED).flush();
			writer.append("--" + boundary + "--").append(LINE_FEED);
			writer.close();

			// checks server's status code first
			int status = httpConn.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpConn.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					response.add(line);
				}
				reader.close();
				httpConn.disconnect();
			} else {
				throw new IOException("Server returned non-OK status: "
						+ status);
			}

			return response;
		}
	}

}
