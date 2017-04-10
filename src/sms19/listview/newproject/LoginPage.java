package sms19.listview.newproject;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sms19.inapp.msg.asynctask.GetXmppGroupListAsyncTask;
import sms19.inapp.msg.asynctask.LoginAsyncTask;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Countrymodel;
import sms19.inapp.msg.model.PhoneValidModel;
import sms19.inapp.msg.rest.Rest;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.LoginModel;
import sms19.listview.validation.Validation;
import sms19.listview.webservice.webservice;

public class LoginPage extends AppCompatActivity implements OnClickListener /*ServiceHitListener*/

{
    private Button Lsubmitbtn, ButtonReset;
    private EditText usernameid, passwordid;
    private TextView forgot, signup, tvCurrentlyLogin, donthaveaccnt;
    private DataBaseDetails db = new DataBaseDetails(this);
    private ProgressDialog p;
    public SharedPreferences chatPrefs;
    private LinearLayout Llayout;

    private String version = "";
    private String deviceid = "";
    private boolean isRelogin = false;
    private String PasswordCheck = "hey";
    private RelativeLayout rellayout;
    public static Rest rest;
    private Spinner spinner;
    private ArrayList<Countrymodel> countryArrayList = null;
    private sms19.inapp.msg.adapter.CountryListAdapter adapter = null;
    private TextView name_countryTextView;
    private int defaultIndex = 0;
    private String countryCodeString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);
        chatPrefs = getSharedPreferences("chatPrefs", MODE_PRIVATE);

        /***************************INTERNET********************************/
        webservice._context = this;

        /***************************INTERNET********************************/
        rest = Rest.getInstance();
        forgot = (TextView) findViewById(R.id.forgotPassword);
        Lsubmitbtn = (Button) findViewById(R.id.Loginsubmit);
        usernameid = (EditText) findViewById(R.id.username);
        passwordid = (EditText) findViewById(R.id.Password);
        tvCurrentlyLogin = (TextView) findViewById(R.id.tvcurrentlylogin);
        TextView termCondition = (TextView) findViewById(R.id.text_privacy);
        Llayout = (LinearLayout) findViewById(R.id.LlayoutReset);
        donthaveaccnt = (TextView) findViewById(R.id.Tvdonthaveaccount);
        ButtonReset = (Button) findViewById(R.id.breset);
        rellayout = (RelativeLayout) findViewById(R.id.rellayout);
        rellayout.setBackgroundColor(Color.parseColor(CustomStyle.SIGNUP_BACKGROUND));
        Button buttonCancel = (Button) findViewById(R.id.bcancel);
        buttonCancel.setOnClickListener(this);
        termCondition.setOnClickListener(this);
        signup = (TextView) findViewById(R.id.signup);
        signup.setOnClickListener(this);
        forgot.setOnClickListener(this);
        Lsubmitbtn.setOnClickListener(this);


        //validNumberCheck("180030007777");
        //validNumberCheck("18001025600");
        //validNumberCheck("18004192929");
        //validNumberCheck("18001805522");

        name_countryTextView = (TextView) findViewById(R.id.name_country);
        spinner = (Spinner) findViewById(R.id.country_code);

        final String iso_code = com.kitever.utils.Utils.getIsoCode(LoginPage.this);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                countryArrayList = Utils.getCountryList(LoginPage.this);

                if (countryArrayList != null) {
                    if (iso_code != null) {
                        if (!iso_code.equals("")) {
                            final int size = countryArrayList.size();
                            for (int i = 0; i < size; i++) {
                                if (countryArrayList.get(i).getCountryISOCode().equals(iso_code)) {
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
                        adapter = new sms19.inapp.msg.adapter.CountryListAdapter(LoginPage.this, countryArrayList);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(defaultIndex);
                    }
                });

            }
        }).start();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                countryCodeString = countryArrayList.get(arg2).getCountrycode().trim();
                name_countryTextView.setText(countryCodeString);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>Sign In</font>"));

//		bar.setHomeAsUpIndicator(R.drawable.arrow_new);

        try {
            Intent i = getIntent();
            String a = i.getStringExtra("UPDATEPASSWORD");
            String uname = i.getStringExtra("UserLogin");

            try {
                isRelogin = i.getBooleanExtra("relogin", false);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            if (a.equalsIgnoreCase("PasswordUpdate")) {
                usernameid.setText(uname);
                String pwd = passwordid.getText().toString();
                db.Open();
                Cursor c;
                c = db.getLoginDetails();
                if (c.getCount() > 0) {
                    db.updateUserpassword(pwd);
                }
                db.close();
            }
        } catch (Exception e) {

        }

        if (isRelogin) {
            String name = Utils.getPhoneNumber(LoginPage.this);
            usernameid.setText(name);
            usernameid.setEnabled(false);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.text_privacy:
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        LoginPage.this);
                alert.setTitle("Terms and Privacy");

                WebView wv = new WebView(LoginPage.this);
                wv.loadUrl("http://www.kitever.com/termsandprivacy.aspx");
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
                break;
            case R.id.forgotPassword: {

                String userName = usernameid.getText().toString().trim();

                ////Log.w("TAG","VALUES:userName"+userName);

                Intent ig = new Intent(LoginPage.this, ForgotPassword.class);
                ig.putExtra("UserName", userName);
                startActivity(ig);
            }
            break;
            case R.id.signup: {
                Intent signup = new Intent(LoginPage.this, SignUp.class);
                startActivity(signup);

                break;
            }
            case R.id.Loginsubmit: {

                if (Validation.hasText(usernameid) && Validation.hasText(passwordid)) {

                    //Log.w("TAG","::::::VALIDATION SUCCESS::::::");

                    String username = countryCodeString + usernameid.getText().toString().trim();
                    String password = passwordid.getText().toString().trim();
                    PasswordCheck = password;

                    try {
                        deviceid = getIMEI(LoginPage.this);
                        PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        version = pInfo.versionName;
                    } catch (NameNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    sms19.inapp.msg.model.RegisterModel postModel = new sms19.inapp.msg.model.RegisterModel();
                    postModel.setName(username);
                    postModel.setPwd(password);
                    //postModel.setUserNumber(countryCode+username);
                    postModel.setUserNumber(username);
                    postModel.setReLoginType(0);
                    postModel.setVersion(version);
                    postModel.setDevice_id(deviceid);

                    if (Utils.isDeviceOnline(this)) {
                        p = ProgressDialog.show(this, null, "Please wait..");
                        sms19.inapp.msg.asynctask.LoginAsyncTask asyncTask = new LoginAsyncTask(LoginPage.this, null, postModel);
                        asyncTask.execute();
                    } else {
                        new AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setMessage("Internet connection not found")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                }).show();
                    }
//				new webservice(null, webservice.Login.geturl(username, password,0,"",deviceid,version), webservice.TYPE_GET, webservice.TYPE_LOGIN, this);
                }

                Utils.hideKeyBoardMethod(LoginPage.this, Lsubmitbtn);
            }
            break;
            case R.id.breset:
                // Toast.makeText(getApplicationContext(), "ok"+
                // PasswordCheck+" no", Toast.LENGTH_LONG).show();

                if (passwordid.getText().toString().equalsIgnoreCase(PasswordCheck)) {

                    LayoutInflater inflater = this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.multi_signin,
                            null);

                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setView(dialogView)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            String username = countryCodeString + usernameid.getText()
                                                    .toString().trim();
                                            String password = passwordid.getText()
                                                    .toString().trim();


                                            sms19.inapp.msg.model.RegisterModel postModel = new sms19.inapp.msg.model.RegisterModel();
                                            postModel.setName(username);
                                            postModel.setPwd(password);
                                            // postModel.setUserNumber(countryCode+username);
                                            postModel.setUserNumber(username);
                                            postModel.setReLoginType(1);
                                            postModel.setVersion(version);
                                            postModel.setDevice_id(deviceid);
                                            postModel.setUser_choise("Ok");
                                            if (Utils.isDeviceOnline(LoginPage.this)) {
                                                p = ProgressDialog.show(LoginPage.this,
                                                        null, "Please wait..");
                                                sms19.inapp.msg.asynctask.LoginAsyncTask asyncTask = new LoginAsyncTask(
                                                        LoginPage.this, null, postModel);
                                                asyncTask.execute();
                                            } else {
                                                new AlertDialog.Builder(LoginPage.this)
                                                        .setCancelable(false)
                                                        .setMessage("Internet connection not found")
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();

                                                            }
                                                        }).show();
                                            }
                                            // new webservice(null,
                                            // webservice.Login.geturl(username,
                                            // password,1,"Ok",deviceid,version),
                                            // webservice.TYPE_GET,
                                            // webservice.TYPE_LOGIN,
                                            // LoginPage.this);

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
                } else
                    passwordid.setError("Password not matched");

                break;
            default:
                break;
        }
    }

    //@Override
    public void onSuccess(Object Result) {

        Log.w("TAG", "SUCCESS::" + Result);
        final LoginModel model = (LoginModel) Result;

        try {
            p.dismiss();
        } catch (Exception e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        if (model.getLogin().get(0).getMessage() == null) {

            if (model != null && model.getLogin().get(0).getColumn1() == null) {

                Utils.printLog("6 Start time_logger" + new SimpleDateFormat("hh:mm:ss").format(new Date()) + "");


                try {


                    try {
                        String EmergencyMessage = model.getLogin().get(0).getEmergencyMessage();

                        try {
                            Emergency.desAct.finish();
                        } catch (Exception e) {
                        }

                        try {
                            if (model.getLogin().get(0).getQueryStatus() != null && model.getLogin().get(0).getQueryStatus().length() > 0 && model.getLogin().get(0).getQueryStatus().equalsIgnoreCase("False")) {
//							Toast.makeText(getApplicationContext(), model.getLogin().get(0).getUserMessage(), Toast.LENGTH_SHORT).show();
                                String msg = "";
                                if (model.getLogin().get(0).getUserMessage() == null || model.getLogin().get(0).getUserMessage().equalsIgnoreCase("")) {
                                    msg = model.getLogin().get(0).getErrorMessage();
                                } else {
                                    msg = model.getLogin().get(0).getUserMessage();
                                }
                                new AlertDialog.Builder(this)
                                        .setCancelable(false)
                                        .setMessage(model.getLogin().get(0).getUserMessage())
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();

                                            }
                                        }).show();
                                return;
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
                            Intent rt = new Intent(LoginPage.this, Emergency.class);
                            rt.putExtra("Emergency", EmergencyMessage);
                            startActivity(rt);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (model.getLogin().size() > 0) {

                        //Log.w("TAG","SIZE::::::::::");

                        if (model.getLogin().get(0).getError() == null) {
                            //
                            //Log.w("TAG","ERROR SUCCESS::::::::::"+model.getLogin().get(0).getError());
                            //"User_ID":20774,"Pwd":"95645","Mobile":"4855555655","Status":"1         ","Msg":"Login Successfully"}]}
                            final String userid = model.getLogin().get(0).getUser_ID();
                            String mobile = model.getLogin().get(0).getMobile();
                            final String mobile2 = model.getLogin().get(0).getMobile();
                            String Status = model.getLogin().get(0).getStatus();
                            final String pass = model.getLogin().get(0).getPwd();
                            String Mechantcode = model.getLogin().get(0).getMerchantUserID();
                            String Merchanturl = model.getLogin().get(0).getMerchantWebsite();
                            String logo = model.getLogin().get(0).getProfilePicturePath();
                            String MerchantName = model.getLogin().get(0).getMerchantName();
                            final String user_login = model.getLogin().get(0).getUser_login();

                            if (userid != null && userid.length() > 0) {
                                userid.trim();
                            }
                            if (user_login != null && user_login.length() > 0) {
                                user_login.trim();
                            }
                            if (Status != null && Status.length() > 0) {
                                Status.trim();
                            }
                            if (pass != null && pass.length() > 0) {
                                pass.trim();
                            }
                            if (Mechantcode != null && Mechantcode.length() > 0) {
                                Mechantcode.trim();
                            }
                            if (Merchanturl != null && Merchanturl.length() > 0) {
                                Merchanturl.trim();
                            }
                            if (logo != null && logo.length() > 0) {
                                logo.trim();
                            }
                            if (MerchantName != null && MerchantName.length() > 0) {
                                MerchantName.trim();
                            }


//							final String tempMobileNo=mobile;

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.saveUserId(getApplicationContext(), userid);
                                    String user_type = model.getLogin().get(0).getUserType();
                                    Utils.savePassword(getApplicationContext(), pass);
                                    Utils.savePhoneNumber(getApplicationContext(), mobile2);
                                    Utils.saveUserType(getApplicationContext(), user_type);
                                    Utils.saveCountryCode(getApplicationContext(), countryCodeString);

                                    //Utils.printLog(" 88 Start time_logger" + new SimpleDateFormat("hh:mm:ss").format(new Date())+"");
                                    SharedPreferences prfs = getSharedPreferences("profileData", Context.MODE_PRIVATE);
                                    Editor editor = prfs.edit();

                                    editor.putString("countryCode", countryCodeString);

                                    editor.putString("EMail", model.getLogin().get(0).getEMail());

                                    editor.putString("pincode", model.getLogin().get(0).getPincode());
                                    editor.putString("Country", model.getLogin().get(0).getCountry());
                                    editor.putString("CompnayName", model.getLogin().get(0).getCompnayName());
                                    editor.putString("DOE", model.getLogin().get(0).getDOE());
                                    editor.putString("Currency", model.getLogin().get(0).getCurrency());
                                    editor.putString("MerchantProfilePicturePath", model.getLogin().get(0).getMerchantProfilePicturePath());
                                    editor.putString("User_DOB", model.getLogin().get(0).getUser_DOB());
                                    editor.putString("Mobile", model.getLogin().get(0).getMobile());
                                    editor.putString("Balance", model.getLogin().get(0).getBalance());
                                    editor.putString("UserCategory", model.getLogin().get(0).getUserCategory());
                                    editor.putString("UserType", model.getLogin().get(0).getUserType());
                                    editor.putString("ProfilePicturePath", model.getLogin().get(0).getProfilePicturePath());
                                    editor.putString("Name", model.getLogin().get(0).getName());
                                    editor.putString("Plan", model.getLogin().get(0).getPlan());
                                    editor.putString("ExpiryDate", model.getLogin().get(0).getExpiryDate());
                                    editor.putString("Merchant_Code", model.getLogin().get(0).getMerchant_Code());
                                    editor.putString("User_ID", model.getLogin().get(0).getMerchant_Code());
                                    editor.putString("Pwd", model.getLogin().get(0).getPwd());
                                    editor.putString("user_login", model.getLogin().get(0).getUser_login());
                                    editor.putString("MerchantName", model.getLogin().get(0).getMerchantName());
                                    editor.putString("Merchant_Url", model.getLogin().get(0).getMerchant_Url());
                                    editor.putString("Home_Url", model.getLogin().get(0).getHome_Url());
                                    editor.putString("AddOn", model.getLogin().get(0).getAddOn());
                                    editor.commit();
                                    try {
                                        android.content.SharedPreferences mPrefs = PreferenceManager
                                                .getDefaultSharedPreferences(LoginPage.this);
                                        android.content.SharedPreferences.Editor prefsEditor = mPrefs
                                                .edit();

                                        // String json = gson.toJson(loginUserBean);
                                        if (!mPrefs.contains("ExpiryTimeValueMail")) {
                                            prefsEditor.putString("ExpiryTimeValueMail", "0:05");
                                        }
                                        if (!mPrefs.contains("ExpiryTimeValue")) {
                                            prefsEditor.putString("ExpiryTimeValue", "0:05");
                                        }
                                        prefsEditor.commit();
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("Addon", "Add-" + model.getLogin().get(0).getAddOn());

                                }
                            }).start();


                            if (mobile.equalsIgnoreCase("")) {
                                mobile = usernameid.getText().toString().trim();
                            }

                            saveRegisterData(userid, mobile, "", "", "", mobile, pass);

                            if (hasMerchantdata()) {

                                db.Open();
                                db.updateMerchantData(Mechantcode, MerchantName, Merchanturl, logo);
                                db.close();
                            } else {
                                db.Open();
                                db.addmerchantinformation(Mechantcode, MerchantName, Merchanturl, logo);
                                db.close();
                            }
                            String ulogin = "";
                            try {
                                ulogin = model.getLogin().get(0).getUser_login();
                            } catch (Exception e1) {

                                ulogin = mobile;
                            }
                            String filterAccount = "MTIX_TR";

                            String LoginType = "";

                            try {
                                LoginType = model.getLogin().get(0).getUserType();
                            } catch (Exception e) {

                                LoginType = "1";
                            }

                            //Log.w("JSR","JSR ::::::::(LoginType:)"+LoginType);

                            if (filterAccount.equals("MTIX_PRO"))//check account type
                            {
                                new AlertDialog.Builder(this)

                                        .setMessage("Please Login With Transactional Account or Signup with Transactional Account ")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })

                                        .setIcon(android.R.drawable.ic_dialog_alert).show();


                            }//check account type
                            else {

                                if (isHaveData()) {
                                    db.Open();
                                    db.deleteLoginData();
                                    db.close();
                                }

                                db.Open();
                                db.addTologin(mobile, Status, userid, "0", pass, ulogin, LoginType);
                                db.close();


                                if (model.getLogin().get(0).getMsg() != null) {
                                    Toast.makeText(getApplicationContext(), model.getLogin().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                                } else if (model.getLogin().get(0).getUserMessage() != null) {
                                    Toast.makeText(getApplicationContext(), model.getLogin().get(0).getUserMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), model.getLogin().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                                }
                                // Check device Information
                                //    String myVersion = android.os.Build.VERSION.RELEASE;
                                int sdkVersion = android.os.Build.VERSION.SDK_INT;

                                if (sdkVersion > 11) {

                                    Utils.printLog(" 99 Start time_logger" + new SimpleDateFormat("hh:mm:ss").format(new Date()) + "");

                                    Intent i = new Intent(LoginPage.this, Service_Loading_Page.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                } else {

                                    Utils.printLog(" 99 Start time_logger" + new SimpleDateFormat("hh:mm:ss").format(new Date()) + "");
                                    Intent i = new Intent(LoginPage.this, Service_Loading_Page.class);
                                    i.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }


                            }
                        } else {
                            String ErrorRes = model.getLogin().get(0).getError();
                            boolean statustr = (ErrorRes).equalsIgnoreCase("User is Inactive");

                            //Log.w("TAG","status:::::::::::::::::statustr:"+statustr+",ErrorRes:"+ErrorRes);

                            if (statustr) {
                                new AlertDialog.Builder(this)
                                        .setCancelable(false)
                                        .setTitle("Login Failed!")
                                        .setMessage("Your Account is Inactive. Please contact us at info@sms19.in for details")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();

                                            }
                                        }).show();
                            } else {
                                Toast.makeText(getApplicationContext(), model.getLogin().get(0).getError(), Toast.LENGTH_SHORT).show();

                            }
                            //Toast.makeText(this, model.getLogin().get(0).getError(), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                if (model.getLogin().get(0).getColumn1().equalsIgnoreCase("Already Loggedin")) {
                    loginFailedDialog();
                }
            }
        } else {
            if (model.getLogin().get(0).getColumn1() != null && model.getLogin().get(0).getColumn1().equalsIgnoreCase("Already Loggedin")) {
                loginFailedDialog();
            } else if (model.getLogin().get(0).getMessage() != null && model.getLogin().get(0).getMessage().trim().equalsIgnoreCase("Already Loggedin")) {
                loginFailedDialog();
            }
        }


    }

    //@Override
    public void onError(String Error/*, int id*/) {
        //Log.w("TAG","ERROR::"+Error);

        try {
            p.dismiss();
        } catch (Exception e4) {
            e4.printStackTrace();
        }

        try {
            Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();

        } catch (Exception e1) {
            e1.printStackTrace();
        }


    }

    public boolean isHaveData() {

        db.Open();
        Cursor c;

        c = db.getLoginDetails();

        while (c.moveToNext() == true) {
            return true;

        }

        db.close();

        return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.howtouse:
                Intent i = new Intent(LoginPage.this, HowToUse.class);
                startActivity(i);
                break;

            case R.id.pp:
                Intent i1 = new Intent(LoginPage.this, TermsAndCondition.class);
                startActivity(i1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean hasMerchantdata() {

        db.Open();

        Cursor c;

        c = db.getMercahantData();

        while (c.moveToNext()) {

            db.close();
            return true;
        }

        db.close();
        return false;
    }


    private void saveRegisterData(String userId, String userNumber,
                                  String countryCode, String verifyCode, String adminroom_id,
                                  String adminroom_name, String pass) {
        RingtoneManager manager = new RingtoneManager(LoginPage.this);
        manager.setType(RingtoneManager.TYPE_NOTIFICATION);
        Uri Duri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //String pass = "123456";
        //userNumber="+919509274113";
        //	Toast.makeText(LoginPage.this, userNumber, Toast.LENGTH_SHORT).show();
        //	userNumber="+919509274114";
//		Utils.printLog("USER PASSWORD:  " + pass);


        sms19.inapp.msg.model.PhoneValidModel model = validNumberCheckForBroadCast2(userNumber);


        Editor editor = chatPrefs.edit();
        editor.putString("userId", userId);
        editor.putString("userNumber", model.getCountry_code() + model.getPhone_number());
        editor.putString("adminroom_id", adminroom_id);
        editor.putString("adminroom_name", adminroom_name);
        editor.putString("user_jid", model.getCountry_code() + model.getPhone_number() + "@" + GlobalData.HOST);
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

        GlobalData.dbHelper.saveUserDatainDb(chatPrefs, adminroom_name,
                "my", null);


        try {
            //	String userId1=Utils.getUserId(LoginPage.this);
            //String userId1=userId;
            if (!userId.equalsIgnoreCase("")) {

			/*	Sms19ContactsFromserverAsync	addContactAsyncTask=new Sms19ContactsFromserverAsync(null,userId);

				if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
					addContactAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}else{
					addContactAsyncTask.execute();
				}*/

                Utils.GetBroadCastGroupList(userId, userNumber + "@" + GlobalData.HOST, LoginPage.this);
                getXmppGroupList(userNumber + "@" + GlobalData.HOST, userId);
            }
        } catch (Exception e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }


    }


    public void getXmppGroupList(String user_jid, String userId1) {
        //String userId1=Utils.getUserId(LoginPage.this);

        GetXmppGroupListAsyncTask broadCastGroupList = new GetXmppGroupListAsyncTask(userId1, user_jid, LoginPage.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            broadCastGroupList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            broadCastGroupList.execute();
        }

    }

    private void loginFailedDialog() {
        Lsubmitbtn.setVisibility(View.GONE);
        Llayout.setVisibility(View.VISIBLE);
        tvCurrentlyLogin.setVisibility(View.VISIBLE);
        forgot.setVisibility(View.GONE);
        donthaveaccnt.setVisibility(View.GONE);
        signup.setVisibility(View.GONE);
        // usernameid.setEnabled(true);
        usernameid.setHint("Mobile no.");
        passwordid.setHint("Confirm Password");

        ButtonReset.setOnClickListener(LoginPage.this);

    }

//	private void loginFailedDialog() 
//	{
//
//		new AlertDialog.Builder(this)
//		.setCancelable(false)
//		.setMessage("This user is already loggedin, Are you sure you want to login?")
//		.setPositiveButton("OK", new DialogInterface.OnClickListener()
//		{
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) 
//			{
//
//				String username = usernameid.getText().toString().trim();
//				String password = passwordid.getText().toString().trim();			
//				p = ProgressDialog.show(LoginPage.this, null, "Please wait.."); 
//
//
//				sms19.inapp.msg.model.RegisterModel  postModel=new sms19.inapp.msg.model.RegisterModel();
//				postModel.setName(username);
//				postModel.setPwd(password);
//				//postModel.setUserNumber(countryCode+username);
//				postModel.setUserNumber(username);
//				postModel.setReLoginType(1);
//				postModel.setVersion(deviceid);
//				postModel.setDevice_id(deviceid);
//				postModel.setUser_choise("Ok");
//
//				sms19.inapp.msg.asynctask.LoginAsyncTask asyncTask=new LoginAsyncTask(LoginPage.this,null, postModel);
//				asyncTask.execute();
//
//
//				//new webservice(null, webservice.Login.geturl(username, password,1,"Ok",deviceid,version), webservice.TYPE_GET, webservice.TYPE_LOGIN, LoginPage.this);
//
//
//
//			}
//
//		})
//		.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
//		{
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) 
//			{
//
//				dialog.cancel();
//
//
//
//			}})
//			.show();
//	}

    public String getIMEI(Context context) {
        String imei = "";
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            TelephonyManager mngr = (TelephonyManager) context
                    .getSystemService(TELEPHONY_SERVICE);
            imei = mngr.getDeviceId();

        }
        return imei;

    }


    public synchronized static sms19.inapp.msg.model.PhoneValidModel validNumberCheck(String phone) {
        boolean isValid = false;
        String internationalFormat = "";
        PhoneNumberUtil phoneUtil = null;

        sms19.inapp.msg.model.PhoneValidModel model = new PhoneValidModel();
        phoneUtil = PhoneNumberUtil.getInstance();
        model.setNumber(false);
        try {
            PhoneNumber phNumberProto = phoneUtil.parse(phone, "IN");
            int countryCode = phNumberProto.getCountryCode();
            //system.err.println("NumberParseException was thrown: " + countryCode);
            isValid = phoneUtil.isValidNumber(phNumberProto);
            if (isValid) {
                internationalFormat = phoneUtil.format(phNumberProto, PhoneNumberFormat.NATIONAL).replace(" ", "");
                String code = "+" + phNumberProto.getCountryCode() + "".trim();
                model.setNumber(false);

                if (phoneUtil.getNumberType(phNumberProto) == phoneUtil.getNumberType(phNumberProto).FIXED_LINE || phoneUtil.getNumberType(phNumberProto) == phoneUtil.getNumberType(phNumberProto).TOLL_FREE) {
                    internationalFormat = "";
                    model.setNumber(false);
                } else {

                    if (internationalFormat.startsWith("0")) {
                        internationalFormat = internationalFormat.substring(1, internationalFormat.length());
                    }
                    model.setPhone_number(internationalFormat);
                    model.setCountry_code(code);
                    model.setNumber(isValid);

                }
            }

        } catch (Exception e) {
            //system.err.println("NumberParseException was thrown: " + e.toString());
            model.setNumber(false);
            return model;
        }
        return model;

    }


    private synchronized sms19.inapp.msg.model.PhoneValidModel validNumberCheckForBroadCast2(String phone) {
        boolean isValid = false;
        String internationalFormat = "";
        PhoneNumberUtil phoneUtil = null;

        sms19.inapp.msg.model.PhoneValidModel model = new PhoneValidModel();
        phoneUtil = PhoneNumberUtil.getInstance();
        model.setNumber(false);
        try {
            PhoneNumber phNumberProto = phoneUtil.parse(phone, "IN");
            int countryCode = phNumberProto.getCountryCode();
            //system.err.println("NumberParseException was thrown: " + countryCode);
            isValid = phoneUtil.isValidNumber(phNumberProto);
            if (isValid) {
                internationalFormat = phoneUtil.format(phNumberProto, PhoneNumberFormat.NATIONAL).replace(" ", "");
                String code = "+" + phNumberProto.getCountryCode() + "".trim();
                model.setNumber(false);

                if (phoneUtil.getNumberType(phNumberProto) == phoneUtil.getNumberType(phNumberProto).FIXED_LINE || phoneUtil.getNumberType(phNumberProto) == phoneUtil.getNumberType(phNumberProto).TOLL_FREE) {
                    internationalFormat = "";
                    model.setNumber(false);
                } else {

                    if (internationalFormat.startsWith("0")) {
                        internationalFormat = internationalFormat.substring(1, internationalFormat.length());
                    }
                    model.setPhone_number(internationalFormat);
                    model.setCountry_code(code);
                    model.setNumber(isValid);

                }
            }

        } catch (Exception e) {
            //system.err.println("NumberParseException was thrown: " + e.toString());
            model.setNumber(false);
            return model;
        }
        return model;

    }
}

/*	public class CountryListAsyncTask extends AsyncTask<Void, Void, Void>{

		String response="";
		public  ArrayList<Countrymodel> countryListBeans=new ArrayList<Countrymodel>();
		@Override
		protected Void doInBackground(Void... params) {

			response = rest.getCountryList();
			Utils.printLog("getGroupListRequest Response:  " + response);

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			JSONObject resObj=new JSONObject();

			try {
				if (response != null && response.trim().length() != 0) {
					try {
						resObj = new JSONObject(response);

						JSONArray jsonArray = resObj.getJSONArray("CountryList");

						if(jsonArray!=null){
							for(int i=0;i<jsonArray.length();i++){
								Countrymodel bean=new Countrymodel();
								JSONObject jsonObject=jsonArray.getJSONObject(i);
								if(jsonObject.has("country_id")){
									bean.setCountry_id(jsonObject.getString("country_id"));
								}
								if(jsonObject.has("countrycode")){
									bean.setCountrycode(jsonObject.getString("countrycode"));
								}
								if(jsonObject.has("country_name")){
									bean.setCountry_name(jsonObject.getString("country_name"));
								}
								countryListBeans.add(bean);
							}


							Utils.saveCountryList(LoginPage.this, countryListBeans);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}*/
