package sms19.listview.newproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
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
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.PermissionClass;
import com.kitever.utils.SlidingTabLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.janmuller.android.simplecropimage.CropImage;
import it.sauronsoftware.ftp4j.FTPClient;
import sms19.inapp.msg.CircularImageView;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Rest;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.fragment.EditProfieInterface;
import sms19.listview.newproject.fragment.FragementProfileContact;
import sms19.listview.newproject.fragment.FragementProfilePersonal;
import sms19.listview.newproject.fragment.FragementProfileSetting;
import sms19.listview.newproject.fragment.FragementProfileSubscription;
import sms19.listview.newproject.model.GetFTPCre;
import sms19.listview.validation.Validation;
import sms19.listview.webservice.webservice;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class EditProfilePage extends AppCompatActivity implements View.OnClickListener, EditProfieInterface
   {

    ViewPager profile_pager;
    SlidingTabLayout slidingtab;
    public static EditText UName;
    public static ImageView images,edit_name_click;


    public static String imagePathh;
    public static ProgressBar secondBar;
    public static Boolean send = false;

    public static EditText editName,editAddress, merchantcode,  webaddress;
    RadioButton radioBtnMale, radioBtnFemale;

  //  public static EditText editplan,editCurrency, editCompanyName,chatStatus,dob,doe,websiteurl;
    public static EditText editCountry,editMobile, editEmail, editPinCode,editState,editCity;
    public static EditText editMerchantName, editStoreUrl,edit_home_url;

    TextView upgrade, tvv, expiryDate, planName;
    public static String filePath, urlpath, sendurlpath;
    Uri khogenpickedImage;
    String[] khogenfilePath;
    Cursor khogencursor;

    private int mYear, mMonth, mDay;
    public static String imagetoupload2 = "";

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    Bitmap bitmap;

    private static int LOAD_IMAGE_RESULTS = 1;
    String ppath;
    RadioGroup genG;
    String gen, name, mobile, email, zipcode, merchantcodes, webaddressi, dobi,
            country, currency, companyname, merchcode;
    String mobile1;
    String email1;
    String pincode1, setimege = "";
    String last;
    Context gContext;
    // String imagePathh;
    int fragmentcount=3;

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

    private String merchantCodeVal = "";
    private String userProfilePicturePath = null;
    private String merchant_Url = "", home_url = "";
    private String merchantNameTxt = "";
    private String stateid="",cityid="",stateName="",cityName="";;
    private String userType = "";
    String profileMsg = "";
    SharedPreferences prfs;
    PermissionClass pclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        actionBarSettingWithBack(this,getSupportActionBar(),"Profile");

        images=(ImageView) findViewById(R.id.imgViewProfile);
        images.setOnClickListener(this);
        UName=(EditText) findViewById(R.id.Name);

        UName.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        setRobotoThinFont(UName,this);

        edit_name_click=(ImageView) findViewById(R.id.edit_name_click);
        edit_name_click.setOnClickListener(this);
        slidingtab = (SlidingTabLayout) findViewById(R.id.profile_tabs);
        profile_pager=(ViewPager)findViewById(R.id.profile_pager);
        secondBar=(ProgressBar)findViewById(R.id.secondBar);
        pclass=new PermissionClass(this);

        try {
            prfs = getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);
            String nname = prfs.getString("Name", "");
            String mmobile = prfs.getString("Mobile", "");
            String eemail = prfs.getString("EMail", "");
            String ccountry = prfs.getString("Country", "");
            String zzipcode = prfs.getString("pincode", "");
            String ccurrency = prfs.getString("Currency", "");
            String ccompanyname = prfs.getString("CompnayName", "");
            String pplanname = prfs.getString("Plan", "Free Plan");
            String llogintype = prfs.getString("UserCategory", "");

            llogintype = llogintype.trim();
            String eexpirydate = prfs.getString("ExpiryDate", "forever");
            userProfilePicturePath = prfs.getString("ProfilePicturePath", "");
            UserPassword = prfs.getString("Pwd", "");

            merchantCodeVal = prfs.getString("Merchant_Code", "");
            String ddoe = prfs.getString("DOE", "");
            String uuser_dob = prfs.getString("User_DOB", "");

            UserId = Utils.getUserId(EditProfilePage.this);

           // if(LoginType.trim().equals("2")) fragmentcount=4;
           // else fragmentcount=3;
            String userType = prfs.getString("UserType", "");
            LoginType = llogintype;

            if(userType.trim().equals("2") || userType.trim().equals("5"))
            {
                fragmentcount=4;
                slidingtab.setCustomTabView(R.layout.slidingtabcustom,R.id.tabtext);
            }
            else
            {
                fragmentcount=3;
                slidingtab.setCustomTabView(R.layout.tabtextview,R.id.tabtext);
            }

            userProfilePicturePath = prfs.getString("ProfilePicturePath", "");
            UName.setText(nname);
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

        ProfilePager pager=new ProfilePager(getSupportFragmentManager());
        profile_pager.setAdapter(pager);
        slidingtab.setDividerColors(R.color.black);
        slidingtab.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
        slidingtab.setSelectedIndicatorColors(Color.parseColor(CustomStyle.TAB_INDICATOR));

        slidingtab.setViewPager(profile_pager);



    }




    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.imgViewProfile:
                if(pclass.checkPermissionForExternalStorage()) {
                    send = true;
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, LOAD_IMAGE_RESULTS);
                }else pclass.requestPermissionForExternalStorage();
                break;
            case R.id.edit_name_click:
                UName.setEnabled(true);
                UName.requestFocus();
                edit_name_click.setVisibility(View.GONE);
                break;

        }

    }

    @Override
    public void fragement1(String s,boolean b) {
        if(b)
            new RegisterUserAsync().execute("");
        else
            loadImageToServer(imagePathh);
    }


        public class ProfilePager extends FragmentStatePagerAdapter {

        private FragementProfilePersonal fragmentpersonal;
        private FragementProfileContact fragmentcontact;
        private FragementProfileSubscription fragmentsuscription;
        private FragementProfileSetting fragmentsetting;

        public ProfilePager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch(position)
            {
                case 0:
                    fragment = FragementProfilePersonal.newIntance();
                    break;
                case 1:
                    fragment = FragementProfileContact.newIntance();
                    break;
                case 2:
                    fragment = FragementProfileSubscription.newIntance();
                    break;
                case 3: fragment = FragementProfileSetting.newIntance();
                   break;

            }
            return fragment;
        }

        @Override
        public int getCount() {

            return fragmentcount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String str="Persional";
            if (position == 0)
                str ="Personal";
            else  if (position ==1)
                str= "Contact";
            else  if (position ==2)
                str= "Subscription";
            else  if (position ==3)  str= "Settings";
        return  str;
        }
    }


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
            setimege = imagePathh;
            Bitmap bp = BitmapFactory.decodeFile(setimege);
            bp = getRoundedCornerBitmap(bp, 90);
            if (bp == null) {
                bp = BitmapFactory.decodeResource(getResources(),
                        R.drawable.profile_propic);
            }


            cursor.close();

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
                Toast.makeText(EditProfilePage.this, "error in browsing image",
                        Toast.LENGTH_LONG).show();
            }
            // bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
            // mImageView.setImageBitmap(bitmap);
        }
    }


    public void loadImageToServer(String imagetoupload) {
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
            MultipartUtility multipart = new MultipartUtility(requestURL,charset);

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
                        new AlertDialog.Builder(EditProfilePage.this)
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
        } catch (IOException ex) {

        }
    }
    public static void getImageUrl() {

        new webservice(null, webservice.GetFTPHostDetail.geturl("image"),
                webservice.TYPE_GET, webservice.TYPE_FTP_UPLD,
                new webservice.ServiceHitListener() {

                    public void onSuccess(Object Result, int id) {

                        GetFTPCre gpmodel = (GetFTPCre) Result;

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

        new webservice(null, webservice.GetFTPHostDetail.geturl("image"),
                webservice.TYPE_GET, webservice.TYPE_FTP_UPLD,
                new webservice.ServiceHitListener() {

                    public void onSuccess(Object Result, int id) {

                        GetFTPCre gpmodel = (GetFTPCre) Result;

                        String FTP_USER = gpmodel.getGetFTPHostDetail().get(0)
                                .getFtpUser();
                        String FTP_PASS = gpmodel.getGetFTPHostDetail().get(0)
                                .getFtpPassword();
                        String FTP_HOST = gpmodel.getGetFTPHostDetail().get(0)
                                .getHostName();
                        urlpath = gpmodel.getGetFTPHostDetail().get(0)
                                .getFtpUrl();
                        String path = "";
                        Log.i("FTP=","FTP_HOST-"+FTP_HOST);
                        Log.i("FTP=","FTP_USER-"+FTP_USER);
                        Log.i("FTP=","FTP_PASS-"+FTP_PASS);
                        sendurlpath = urlpath + "/" + UserId + imagetoupload2;
                        path = sendurlpath;
                        Toast.makeText(getApplicationContext(), sendurlpath,
                                Toast.LENGTH_LONG).show();
                        //registerUser();
                      
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

                        new AlertDialog.Builder(EditProfilePage.this)
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

    private void startCropImage(File file) {

        Intent intent = new Intent(EditProfilePage.this, CropImage.class);
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


    private class RegisterUserAsync extends AsyncTask<String, String, String> {

        String profilePic, userDOB, companyDOE;

        RegisterUserAsync()
        {
            SharedPreferences prfs = getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);
            uname1 = UName.getText().toString().trim();
            mobile1 = prfs.getString("Mobile", "");
            email1 = prfs.getString("EMail", "");
            pincode1 =  prfs.getString("pincode", "");
            merchantcodes = prfs.getString("Merchant_Code", "");
            webaddressi = prfs.getString("Merchant_Url", null);
            dobi =prfs.getString("User_DOB", "");
            companyname =prfs.getString("CompnayName", "");
            merchant_Url = prfs.getString("Merchant_Url", null);
            home_url = prfs.getString("Home_Url", "");
            merchantNameTxt =  prfs.getString("MerchantName", "");
            stateid =  prfs.getString("stateid", "");
            cityid =  prfs.getString("cityid", "");
            stateName =  prfs.getString("StateName", "");
            cityName =  prfs.getString("CityName", "");

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            nameValuePairs.add(new BasicNameValuePair("State", stateid));
            nameValuePairs.add(new BasicNameValuePair("City", cityid));

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
           // nameValuePairs.add(new BasicNameValuePair("StateName", stateName));
            //nameValuePairs.add(new BasicNameValuePair("CityName", cityName));

            Log.i("Edit profile", "nameValuePairs-  " + nameValuePairs.toString());

            String stringUrl = Apiurls.KIT19_BASE_URL.replace("?Page=", "");
            stringUrl = stringUrl.replace(" ", "");

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

                        SharedPreferences.Editor editor = prfs.edit();
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
                    secondBar.setVisibility(View.GONE);
                    Toast.makeText(EditProfilePage.this,profileMsg,Toast.LENGTH_LONG).show();
                    userProfilePicturePath = null;
                    new DownloadFileFromURL().execute(result);
                } else {
                    secondBar.setVisibility(View.GONE);
                    showDialogCall(profileMsg);
                }
                send = false;
            } else {
                secondBar.setVisibility(View.GONE);
                showDialogCall(profileMsg);
            }
        }
    }



    private void showDialogCall(String msg) {
        new AlertDialog.Builder(EditProfilePage.this).setCancelable(false)
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
                new AlertDialog.Builder(EditProfilePage.this)
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

}
