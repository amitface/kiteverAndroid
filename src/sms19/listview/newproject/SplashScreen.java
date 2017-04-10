package sms19.listview.newproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.kitever.android.BuildConfig;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import sms19.inapp.msg.ErrorReporter;
import sms19.inapp.msg.asynctask.LoginAsyncTask;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.ContactUtil;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.database.DatabaseHelper;
import sms19.inapp.msg.rest.Rest;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.FetchUrl;
import sms19.listview.newproject.model.LoginModel;
import sms19.listview.newproject.model.SplashModel;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;

import static com.kitever.app.context.CustomStyle.defualtStyle;
import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class SplashScreen extends Activity {

    ImageView imageViewSplash;
    // Static variable for time expend for splash screen

    private static int TIME_FOR_SPLASH_SHOWING = 3000;// 3000;
    TextView MERCHANTNAME, MERCHANTWEBSITE;

    DataBaseDetails dbObj;
    String MerchantCode = "", MerchantName = "", Merchantwebsite = "",
            MerchantprofileURL = "";

    /**************
     * New
     *********/
    public static SharedPreferences chatPrefs;
    public static Rest rest;
    private String USER_ID = "";
    private ProgressBar progressBar;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isPalyService = checkPlayServices();
        defualtStyle(this);
        if (!isPalyService) {
        } else {
            setContentView(R.layout.activity_splash_screen);
            //createUI();
            new VersionChecker().execute();
        }
    }

    private void createUI() {

        ErrorReporter errReporter = new ErrorReporter();
        //errReporter.Init(this);
        //errReporter.CheckErrorAndSendMail(this);

        MERCHANTNAME = (TextView) findViewById(R.id.merchantName);
        MERCHANTWEBSITE = (TextView) findViewById(R.id.textBottom);
        imageViewSplash = (ImageView) findViewById(R.id.imageViewSplash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_id);

        MERCHANTWEBSITE.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        MERCHANTWEBSITE.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        setRobotoThinFont(MERCHANTWEBSITE,this);



        webservice._context = this;

        Utils.MakeDirs(this);
        ContactUtil.countryToIndicative();
        // Utils.savePhoneNumber(SplashScreen.this,"7877475123");
        // Utils.savePassword(SplashScreen.this,"586362");

        Utils.printLog("1 Start time_logger"
                + new SimpleDateFormat("hh:mm:ss").format(new Date()) + "");

        chatPrefs = getSharedPreferences("chatPrefs", MODE_PRIVATE);
        GlobalData.dbHelper = null;

        USER_ID = Utils.getUserId(SplashScreen.this);

        if (GlobalData.dbHelper == null) {
            try {
                // String userId=Utils.getUserId(SplashScreen.this);
                if (USER_ID.equalsIgnoreCase("")) {

                    // Random generator = new Random();
                    // int resource_int = generator.nextInt();

                    String resource_int = getIMEI(SplashScreen.this);
                    if (resource_int == null) {
                        Random generator = new Random();
                        resource_int = String.valueOf(generator.nextInt());
                    }
                    if (resource_int.equalsIgnoreCase("null")) {
                        Random generator = new Random();
                        resource_int = String.valueOf(generator.nextInt());
                    }
                    if (resource_int.equalsIgnoreCase("")) {
                        Random generator = new Random();
                        resource_int = String.valueOf(generator.nextInt());
                    }

                    Utils.saveDeviceToken(SplashScreen.this,
                            String.valueOf(resource_int));
                    GlobalData.dbHelper = new DatabaseHelper(SplashScreen.this,
                            true);

                } else {
                    GlobalData.dbHelper = new DatabaseHelper(SplashScreen.this,
                            false);
                }

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        rest = Rest.getInstance();
        Utils.printLog("2 Start time_logger"
                + new SimpleDateFormat("hh:mm:ss").format(new Date()) + "");

        getBaseUrl();

        // comment n m end

        dbObj = new DataBaseDetails(this);

        try {

            if (hasMerchantdata()) {
                getMerchantcode();

                if (MerchantName != null) {
                    if (MerchantName.length() > 0 && MerchantName != null) {

                        MERCHANTNAME.setText(MerchantName.trim());
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            SharedPreferences prfs = getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);

            String Merchant_Code = prfs.getString("Merchant_Code", "0");

            Map<String, ?> allPrefs = prfs.getAll(); //your sharedPreference
            Set<String> set = allPrefs.keySet();
            for (String s : set) {
                Log.i("Pref", s + "<" + allPrefs.get(s).getClass().getSimpleName() + "> =  "
                        + allPrefs.get(s).toString());
            }


            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("KiteverImageDir", Context.MODE_PRIVATE);
            File f = new File(directory.getPath(), "merchantprofile.jpg");


            if (f.exists()) {
                imageViewSplash.setImageDrawable(Drawable.createFromPath(f.getPath()));
            } else {

                String mpath = prfs.getString("MerchantProfilePicturePath", "");
                if (mpath != null && !mpath.equalsIgnoreCase("")) {
                    new DownloadFileFromURL().execute("http://" + mpath);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (hasAnydata()) {
                getMerchantcode();
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                try {
                    progressBar.setVisibility(View.GONE);
                    if (hasAnydata()) {

                        if (Utils.isDeviceOnline(SplashScreen.this)) {
                            // String userId=Utils.getUserId(SplashScreen.this);
                            if (USER_ID != null && USER_ID.length() > 0) {
                                Utils.printLog(" 3 Start time_logger"
                                        + new SimpleDateFormat("hh:mm:ss")
                                        .format(new Date()) + "");
                                loginFailedDialog();
                            } else {
                                Intent intent = new Intent(SplashScreen.this,
                                        SignUp.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("relogin", true);
                                // intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);

                            }

                        } else {
                            Intent i = new Intent(SplashScreen.this, Home.class);
                            startActivity(i);
                            finish();
                        }

                    } else {

                        Intent i = new Intent(SplashScreen.this,
                                SignUp.class);
                        startActivity(i);
                        finish();

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        }, TIME_FOR_SPLASH_SHOWING);

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                // Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public boolean hasAnydata() {

        dbObj.Open();
        Cursor c;
        c = dbObj.getLoginDetails();
        while (c.moveToNext()) {
            dbObj.close();
            return true;
        }

        dbObj.close();
        return false;
    }

    public boolean hasMerchantdata() {
        dbObj.Open();
        Cursor c;
        c = dbObj.getMercahantData();
        while (c.moveToNext()) {
            dbObj.close();
            return true;
        }
        dbObj.close();
        return false;
    }

    public boolean hasBaseURL() {
        try {
            dbObj.Open();
            Cursor c;
            c = dbObj.fetchBaseurl();
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    dbObj.close();
                    return true;
                }
            }
            dbObj.close();
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void getMerchantcode() {
        dbObj.Open();
        Cursor c;
        c = dbObj.getMercahantData();
        while (c.moveToNext()) {
            MerchantCode = c.getString(0);
            MerchantName = c.getString(1);
            Merchantwebsite = c.getString(2);
            MerchantprofileURL = c.getString(3);
            Log.i("ann", "splash get - MerchantCode " + MerchantCode + " mname - " + MerchantName);

            dbObj.close();
        }
    }

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
                File mypath = new File(directory, "merchantprofile.jpg");

                // Output stream to write file
                OutputStream output = new FileOutputStream(mypath);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
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
            // dismiss the dialog after the file was downloaded

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("KiteverImageDir", Context.MODE_PRIVATE);
            File f = new File(directory.getPath(), "merchantprofile.jpg");
            String imagePath = f.getPath();
            // setting downloaded into image view
            imageViewSplash
                    .setImageDrawable(Drawable.createFromPath(imagePath));
        }

    }


    // comment m start

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

    public void getBaseUrl() {
        boolean isOnline = Utils.isDeviceOnline(this);
        GetBaseUrlApi api = new GetBaseUrlApi();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            api.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, isOnline);
        } else {
            api.execute(isOnline);
        }

    }

    private class GetBaseUrlApi extends AsyncTask<Boolean, Void, Void> {
        private String response = "";
        private JSONObject resObj;
        boolean isOnline = false;

        @Override
        protected Void doInBackground(Boolean... params) {
            try {
                isOnline = params[0];
                if (isOnline) {
                    String stringUrl = Apiurls.FIRST_BASE_URL;
                    stringUrl = stringUrl.replace(" ", "");
                    Rest rest = Rest.getInstance();
                    response = rest.sendCheckcontactRequest("", stringUrl);
                } else {

                }

                Utils.printLog("Base URL Response1112:  " + response);

            } catch (Exception e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (response != null && response.trim().length() != 0) {
                    try {
                        resObj = new JSONObject(response);
                        if (resObj.has("AutomatedServicesURL")) {
                            if (resObj != null) {
                                JSONArray array = resObj
                                        .getJSONArray("AutomatedServicesURL");
                                JSONObject jsonObject = array.getJSONObject(0);
                                if (jsonObject != null) {
                                    if (jsonObject.has("ServicesUrl")) {
                                        Apiurls.KIT19_BASE_URL = jsonObject
                                                .getString("ServicesUrl");// comment
                                        // for
                                        // testing
                                        // Url
                                        // use
                                        Utils.saveBaseUrlValue(
                                                SplashScreen.this,
                                                Apiurls.KIT19_BASE_URL);
                                    }
                                }

                            }

                        } else {

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                if (!isOnline) {
                    String api = Utils.getBaseUrlValue(SplashScreen.this);
                    Apiurls.KIT19_BASE_URL = api;
                    Utils.saveBaseUrlValue(SplashScreen.this,
                            Apiurls.KIT19_BASE_URL);
                    Apiurls.POST_BASE_URL = Apiurls.KIT19_BASE_URL;
                }

				/*
                 * try { String userId1=Utils.getUserId(SplashScreen.this);
				 * if(!userId1.equalsIgnoreCase("")){
				 * Sms19ContactsFromserverAsync addContactAsyncTask=new
				 * Sms19ContactsFromserverAsync(null,userId1);
				 * addContactAsyncTask.execute(); } } catch (Exception e2) { //
				 * TODO Auto-generated catch block e2.printStackTrace(); }
				 */

                new webservice(null, webservice.Fetchurl.geturl(""),
                        webservice.TYPE_GET, webservice.TYPE_FECT_URL,
                        new ServiceHitListener() {
                            @Override
                            public void onSuccess(Object Result, int id) {

                                FetchUrl model = (FetchUrl) Result;

                                String baseUrl = model
                                        .getAutomatedServicesURL().get(0)
                                        .getServicesURL();

                                try {
                                    if (hasBaseURL()) {
                                        if (baseUrl.length() > 3
                                                || baseUrl != null) {
                                            dbObj.Open();
                                            dbObj.updateBase_URl("1", baseUrl);
                                            dbObj.close();
                                        }
                                    } else {
                                        if (baseUrl.length() > 3
                                                || baseUrl != null) {
                                            dbObj.Open();

                                            dbObj.addBase_URl("1", baseUrl);
                                            dbObj.close();
                                        }
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String Error, int id) {
                                try {

                                    String baseUrl = Apiurls.KIT19_BASE_URL;
                                    if (hasBaseURL()) {

                                        dbObj.Open();
                                        dbObj.updateBase_URl("1", baseUrl);
                                        dbObj.close();

                                    } else {

                                        dbObj.Open();

                                        dbObj.addBase_URl("1", baseUrl);
                                        dbObj.close();

                                    }
                                } catch (Exception e) {
                                }

                            }
                        });

                SharedPreferences prfs = getSharedPreferences("profileData",
                        Context.MODE_PRIVATE);

                String Merchant_Code = prfs.getString("Merchant_Code", "0");
                Log.i("merchantCode", "CODE--" + Merchant_Code);
                new webservice(null,
                        webservice.SplashScreenImage.geturl(Merchant_Code),
                        webservice.TYPE_GET, webservice.TYPE_SPLASH_IMAGE,
                        new ServiceHitListener() {

                            @Override
                            public void onSuccess(Object splashobj, int id) {

                                if (id == webservice.TYPE_SPLASH_IMAGE) {

                                    SplashModel model = (SplashModel) splashobj;

                                    String userID = model.getMerchantDetails()
                                            .get(0).getUser_ID();
                                    String merchantName = model
                                            .getMerchantDetails().get(0).getFname();
                                    String merchantwebsite = model
                                            .getMerchantDetails().get(0)
                                            .getMerchant_Url();
                                    String merchantImgpath = model
                                            .getMerchantDetails().get(0)
                                            .getMerchant_Picture_Path();

                                    if (hasMerchantdata()) {

                                        dbObj.Open();
                                        dbObj.updateMerchantData(userID,
                                                merchantName, merchantwebsite,
                                                merchantImgpath);
                                        dbObj.close();
                                    } else {
                                        dbObj.Open();
                                        dbObj.addmerchantinformation(userID,
                                                merchantName, merchantwebsite,
                                                merchantImgpath);
                                        dbObj.close();
                                    }

                                    if (merchantImgpath == null
                                            || merchantImgpath.length() < 3) {

                                    } else {
                                        String checkhttp = merchantImgpath
                                                .substring(0, 7);

                                        // Log.w("RM", "RM ::::::::(checkhttp):"+
                                        // checkhttp);

                                        if (checkhttp.equalsIgnoreCase("http://")) {

                                        } else {
                                            merchantImgpath = "http://"
                                                    + merchantImgpath;
                                        }

                                        // Log.w("RM", "RM ::::::::(logo):"
                                        // + merchantImgpath);

                                        new DownloadFileFromURL()
                                                .execute(merchantImgpath.trim());

                                    }
                                }

                            }

                            @Override
                            public void onError(String Error, int id) {

                            }
                        });


            } catch (Exception e) {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }

    }

    private void loginFailedDialog() {

        String deviceid = "";
        String version = "";
        String phoneNo = Utils.getCountryCode(SplashScreen.this)
                + Utils.getPhoneNumber(SplashScreen.this);
        // String phoneNo = Utils.getPhoneNumber(SplashScreen.this);
        String Pass = Utils.getPassword(SplashScreen.this);

        try {
            deviceid = getIMEI(SplashScreen.this);
            PackageInfo pInfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            version = pInfo.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        sms19.inapp.msg.model.RegisterModel postModel = new sms19.inapp.msg.model.RegisterModel();
        postModel.setName(phoneNo);
        postModel.setPwd(Pass);
        postModel.setUserNumber(phoneNo);
        postModel.setReLoginType(0);
        postModel.setVersion(version);
        postModel.setDevice_id(deviceid);

        sms19.inapp.msg.asynctask.LoginAsyncTask asyncTask = new LoginAsyncTask(
                null, SplashScreen.this, postModel);
        if (Utils.isDeviceOnline(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                asyncTask.execute();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Internet connection not found")
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

    public void onSuccess(Object Result) {
        // Log.w("TAG", "SUCCESS::" + Result);
        LoginModel model = (LoginModel) Result;
        progressBar.setVisibility(View.GONE);
        if (model != null) {
            if (model.getLogin().get(0).getError() == null) {
                // Toast.makeText(getApplicationContext(),
                // model.getLogin().get(0).getMsg(), Toast.LENGTH_SHORT).show();

                if (model.getLogin().get(0).getMessage() == null) {
                    if (model.getLogin().get(0).getQueryStatus() != null
                            && model.getLogin().get(0).getQueryStatus()
                            .equalsIgnoreCase("false")) {
                        if (model.getLogin().get(0).getColumn1() != null
                                && model.getLogin().get(0).getColumn1()
                                .equalsIgnoreCase("Already Loggedin")) {
                            // String userId=Utils.getUserId(SplashScreen.this);
                            if (USER_ID != null && USER_ID.length() > 0) {
                                Toast.makeText(getApplicationContext(),
                                        model.getLogin().get(0).getColumn1(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            Intent intent = new Intent(SplashScreen.this,
                                    LoginPage.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("relogin", true);
                            // intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            return;
                        } else if (model.getLogin().get(0).getUserMessage() != null
                                && model.getLogin()
                                .get(0)
                                .getUserMessage()
                                .equalsIgnoreCase(
                                        "Mobile and Password does not Match")) {

                            Toast.makeText(getApplicationContext(),
                                    model.getLogin().get(0).getUserMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SplashScreen.this,
                                    SignUp.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // intent.putExtra("relogin", true);

                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SplashScreen.this,
                                    SignUp.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    } else {
                        String mer;
                        mer = model.getLogin().get(0).getMerchantName();
                        SharedPreferences prfs = getSharedPreferences(
                                "profileData", Context.MODE_PRIVATE);
                        Editor editor = prfs.edit();
                        editor.putString("EMail", model.getLogin().get(0)
                                .getEMail());
                        editor.putString("pincode", model.getLogin().get(0)
                                .getPincode());
                        editor.putString("Country", model.getLogin().get(0)
                                .getCountry());
                        editor.putString("CompnayName", model.getLogin().get(0)
                                .getCompnayName());
                        editor.putString("DOE", model.getLogin().get(0)
                                .getDOE());
                        editor.putString("Currency", model.getLogin().get(0)
                                .getCurrency());
                        editor.putString("MerchantProfilePicturePath", model
                                .getLogin().get(0)
                                .getMerchantProfilePicturePath());
                        editor.putString("User_DOB", model.getLogin().get(0)
                                .getUser_DOB());
                        editor.putString("Mobile", model.getLogin().get(0)
                                .getMobile());
                        editor.putString("Balance", model.getLogin().get(0)
                                .getBalance());
                        editor.putString("UserCategory", model.getLogin()
                                .get(0).getUserCategory());
                        editor.putString("UserType", model.getLogin().get(0)
                                .getUserType());
                        editor.putString("ProfilePicturePath", model.getLogin()
                                .get(0).getProfilePicturePath());
                        editor.putString("Name", model.getLogin().get(0)
                                .getName());
                        editor.putString("Plan", model.getLogin().get(0)
                                .getPlan());
                        editor.putString("ExpiryDate", model.getLogin().get(0)
                                .getExpiryDate());
                        editor.putString("Merchant_Code",
                                model.getLogin().get(0).getMerchant_Code());
                        editor.putString("User_ID", model.getLogin().get(0)
                                .getMerchant_Code());
                        editor.putString("Pwd", model.getLogin().get(0)
                                .getPwd());
                        editor.putString("user_login", model.getLogin().get(0)
                                .getUser_login());
                        editor.putString("MerchantName", model.getLogin()
                                .get(0).getMerchantName());
                        editor.putString("Merchant_Url", model.getLogin()
                                .get(0).getMerchant_Url());
                        editor.putString("Home_Url", model.getLogin()
                                .get(0).getHome_Url());
                        editor.putString("AddOn", model.getLogin().get(0).getAddOn());
                        editor.putString("State", model.getLogin().get(0).getState());
                        editor.putString("City", model.getLogin().get(0).getCity());
                        editor.putString("FileSizeLimit", model.getLogin().get(0).getFileSizeLimit());
                        editor.commit();
                        Intent i = new Intent(SplashScreen.this, Home.class);
                        startActivity(i);
                        finish();
                    }

                } else if (model.getLogin().get(0).getMessage() != null
                        && model.getLogin().get(0).getMessage().trim()
                        .equalsIgnoreCase("Already Loggedin")) {
                    // String userId=Utils.getUserId(SplashScreen.this);
                    if (USER_ID != null && USER_ID.length() > 0) {
                        Toast.makeText(getApplicationContext(),
                                model.getLogin().get(0).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(SplashScreen.this,
                            LoginPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("relogin", true);
                    // intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    String mer;
                    mer = model.getLogin().get(0).getMerchantName();
                    SharedPreferences prfs = getSharedPreferences(
                            "profileData", Context.MODE_PRIVATE);
                    Editor editor = prfs.edit();
                    editor.putString("EMail", model.getLogin().get(0)
                            .getEMail());
                    editor.putString("pincode", model.getLogin().get(0)
                            .getPincode());
                    editor.putString("Country", model.getLogin().get(0)
                            .getCountry());
                    editor.putString("CompnayName", model.getLogin().get(0)
                            .getCompnayName());
                    editor.putString("DOE", model.getLogin().get(0).getDOE());
                    editor.putString("Currency", model.getLogin().get(0)
                            .getCurrency());
                    editor.putString("MerchantProfilePicturePath", model
                            .getLogin().get(0).getMerchantProfilePicturePath());
                    editor.putString("User_DOB", model.getLogin().get(0)
                            .getUser_DOB());
                    editor.putString("Mobile", model.getLogin().get(0)
                            .getMobile());
                    editor.putString("Balance", model.getLogin().get(0)
                            .getBalance());
                    editor.putString("UserCategory", model.getLogin().get(0)
                            .getUserCategory());
                    editor.putString("UserType", model.getLogin().get(0)
                            .getUserType());
                    editor.putString("ProfilePicturePath", model.getLogin()
                            .get(0).getProfilePicturePath());
                    editor.putString("Name", model.getLogin().get(0).getName());
                    editor.putString("Plan", model.getLogin().get(0).getPlan());
                    editor.putString("ExpiryDate", model.getLogin().get(0)
                            .getExpiryDate());
                    editor.putString("Merchant_Code", model.getLogin().get(0)
                            .getMerchant_Code());
                    editor.putString("User_ID", model.getLogin().get(0)
                            .getMerchant_Code());
                    editor.putString("Pwd", model.getLogin().get(0).getPwd());
                    editor.putString("user_login", model.getLogin().get(0)
                            .getUser_login());
                    editor.putString("MerchantName", model.getLogin().get(0)
                            .getMerchantName());
                    editor.putString("Merchant_Url", model.getLogin().get(0)
                            .getMerchant_Url());
                    editor.putString("Home_Url", model.getLogin()
                            .get(0).getHome_Url());
                    editor.putString("AddOn", model.getLogin().get(0).getAddOn());
                    editor.putString("State", model.getLogin().get(0).getState());
                    editor.putString("City", model.getLogin().get(0).getCity());
                    editor.putString("FileSizeLimit", model.getLogin().get(0).getFileSizeLimit());
                    editor.commit();
                    Intent i = new Intent(SplashScreen.this, Home.class);
                    startActivity(i);
                    finish();
                }
            } else {

                String ErrorRes = model.getLogin().get(0).getError();
                boolean statustr = (ErrorRes)
                        .equalsIgnoreCase("User is Inactive");

                // Log.w("TAG", "status:::::::::::::::::statustr:" + statustr
                // + ",ErrorRes:" + ErrorRes);

                Toast.makeText(getApplicationContext(), ErrorRes,
                        Toast.LENGTH_SHORT).show();
                // String userId=Utils.getUserId(SplashScreen.this);
                if (USER_ID != null && USER_ID.length() > 0) {
                    Toast.makeText(getApplicationContext(),
                            model.getLogin().get(0).getColumn1(),
                            Toast.LENGTH_SHORT).show();
                }

                if (dbObj != null) {
                    dbObj.deleteLoginData();
                    Utils.saveUserId(SplashScreen.this, "");
                }

                Intent intent = new Intent(SplashScreen.this, SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("relogin", true);
                startActivity(intent);
            }
        } else {
            callPopUp("Please try again later");

        }
    }

    public void onError(String Error) {
        // Log.w("TAG", "ERROR::" + Error);

        try {
            // p.dismiss();
        } catch (Exception e4) {
            e4.printStackTrace();
        }

        try {
            // Toast.makeText(getApplicationContext(),Error,
            // Toast.LENGTH_SHORT).show();
            callPopUp("Please try again later");

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    private void callPopUp(String msg) {
        new AlertDialog.Builder(this).setCancelable(false).setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                }).show();
    }

    private static boolean isShowUpdate = true;

    private class VersionChecker extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String newVersion = null;
            try {
                newVersion = Jsoup
                        .connect(
                                "https://play.google.com/store/apps/details?id="
                                        +BuildConfig.APPLICATION_ID + "&hl=en")
                        .timeout(30000)
                        .userAgent(
                                "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com").get()
                        .select("div[itemprop=softwareVersion]").first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return newVersion;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            // Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)
            super.onPostExecute(result);
            final String packagename = BuildConfig.APPLICATION_ID;
            if (result == null || result.equalsIgnoreCase("")) {
                createUI();
            } else if (Float.valueOf(result) > Float.valueOf(getVersionName())) {
                new AlertDialog.Builder(SplashScreen.this)
                        .setCancelable(true)
                        .setMessage(
                                "This is an outdated version of this App,Install the latest version here")
                        .setPositiveButton("Update Now",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // Intent intent = new Intent(
                                        // Intent.ACTION_VIEW);

                                        try {
                                            Intent intent = new Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://play.google.com/store/apps/details?id="+packagename));
                                            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            Intent intent = new Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://play.google.com/store/apps/details?id="
                                                            + packagename));
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                        // startActivity(intent);
                                       // createUI();
                                    }
                                }).show();
                        /*.setNegativeButton("Update Later",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        createUI();
                                    }
                                }).show();*/
            } else {
                createUI();
            }
        }
    }


    private String getVersionName() {
        PackageManager pm = SplashScreen.this.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(
                    SplashScreen.this.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException ex) {
        }
        return null;
    }

    private int getVersionCode() {
        PackageManager pm = SplashScreen.this.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(
                    SplashScreen.this.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException ex) {
        }
        return 0;
    }

}