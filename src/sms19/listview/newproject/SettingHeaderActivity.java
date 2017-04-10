package sms19.listview.newproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.PermissionClass;

import org.apache.http.util.EncodingUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;

import static com.kitever.utils.Utils.actionBarSettingWithBack;

//import static com.kitever.android.R.id.url;

public class SettingHeaderActivity extends AppCompatActivity {

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private PermissionClass permissionClass;
    private final int CAMERA_REQUEST = 5;
    public static Uri mCapturedImageURI = null;
    private String fileType;
    private final int RESULT_LOAD_IMAGE = 2;
    private String recordfilePath, latlng;
    private String postData, url;
    private ProgressBar progressBar;
    private WebView web;
    private Handler handler;
    private String str;
    private File file;


    @Override
    protected void onStart()
    {
        super.onStart();
        progressBar  = (ProgressBar) findViewById(R.id.progressBarSettingTemplate);
        if (progressBar != null) {
            progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_header);
//        ActionBar bar = getSupportActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
//        // bar.setHomeAsUpIndicator(R.drawable.arrow_new);
//        bar.setDisplayHomeAsUpEnabled(true);
        actionBarSettingWithBack(this,getSupportActionBar(),"Template");
        postData = "PUserID="+ Utils.getUserId(this);
        setScreen();
    }

    private void setScreen() {
        // TODO Auto-generated method stub


        permissionClass = new PermissionClass(this);

        Intent intent = getIntent();
        url = intent.getStringExtra("Link");
        web = (WebView) findViewById(R.id.settingHeaderPdf);
        web.getSettings().setBuiltInZoomControls(true);
        web.setPadding(0, 0, 0, 0);
        web.clearHistory();
        web.clearHistory();
        web.clearCache(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setCacheMode(web.getSettings().LOAD_NO_CACHE);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web.getSettings().setAllowFileAccess(true);


        web.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                web.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"));
                view.clearHistory();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                // timeout = false;
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

        web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {


            }

            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });

        web.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"));

        final MyJavaScriptInterface myJavaScriptInterface
                = new MyJavaScriptInterface(this);
        web.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");

        /*WebClientClass webViewClient = new WebClientClass(this, pbar);
        activityEmailEditor.setWebViewClient(webViewClient);*/
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                web.loadUrl("javascript:getValue()");
                Toast.makeText(getBaseContext(), "Uploading completed ...", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        };

    }
    public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(final String toast) {
            Toast.makeText(SettingHeaderActivity.this, toast, Toast.LENGTH_SHORT).show();
            startGallery();

            /*startImage();*/
           /* startImage();*/

            // webView.loadUrl("javascript:document.getElementById(\"Button3\").innerHTML = \"bye\";");
        }

        @JavascriptInterface
        public void openAndroidDialog() {
            AlertDialog.Builder myDialog
                    = new AlertDialog.Builder(SettingHeaderActivity.this);
            myDialog.setTitle("DANGER!");
            myDialog.setMessage("You can do what you want!");
            myDialog.setPositiveButton("ON", null);
            myDialog.show();
        }

        @JavascriptInterface
        public String receiveValueFromJs(String str) {
        //do something useful with str
        //  Toast.makeText(SettingHeaderActivity.this, "Received Value from JS: " + str,Toast.LENGTH_SHORT).show();         popupWindow.dismiss();
        //  popupWindow.dismiss();
            return "http://nowconnect.in/" + Utils.getUserId(SettingHeaderActivity.this) + "/" + SettingHeaderActivity.this.str;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);

        if (requestCode == CAMERA_REQUEST
                && resultCode == Activity.RESULT_OK) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(
                    mCapturedImageURI, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndex(filePathColumn[0]);

            String picpath = cursor.getString(column_index);
            cursor.close();

            // Bitmap finalBitmap = BitmapFactory.decodeFile(picpath);
            Bitmap finalBitmap = Utils.decodeFile(picpath,
                    GlobalData.fileorignalWidth, GlobalData.fileorignalheight);

            try {
                String savedpicpath = Utils.getfilePath(GlobalData.Imagefile);
                FileOutputStream fos = new FileOutputStream(savedpicpath);
                File f = new File(savedpicpath);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

                fos.close();
                Utils.saveImageInGallery(this, savedpicpath);
                if (finalBitmap != null) {
                    finalBitmap.recycle();
                }
                if (savedpicpath != null && savedpicpath.trim().length() != 0) {
                    fileType = "";
                    fileType = GlobalData.Imagefile;
                }

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == RESULT_LOAD_IMAGE
                && resultCode == Activity.RESULT_OK) {
            Uri uri = intent.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                byte []  bytes =  outStream.toByteArray();
                file = new File(Environment.getExternalStorageDirectory(),Utils.getUserId(this)+""+new Date().getTime()+".jpg");
                if(!file.exists())
                    file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
                fileOutputStream.close();
                progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadFile(file);
                    }
                }).start();

            } catch (Exception e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void startImage() {
        if (permissionClass.checkPermissionForCamera()) {
            if (permissionClass.checkPermissionForExternalStorage()) {
                if (permissionClass.checkPermissionForReadExternalStorage()) {
                    boolean isSDPresent = android.os.Environment
                            .getExternalStorageState().equals(
                                    android.os.Environment.MEDIA_MOUNTED);
                    if (isSDPresent) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "Kit19_pic");
                        mCapturedImageURI = getContentResolver()
                                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values);

                        GlobalData.OnFilesendscreen = true;

                        Intent cameraIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                mCapturedImageURI);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No SD card available", Toast.LENGTH_SHORT).show();
                    }
                } else
                    permissionClass.requestPermissionForReadExternalStorage();
            } else
                permissionClass.requestPermissionForExternalStorage();
        } else
            permissionClass.requestPermissionForCamera();
    }

    private void startGallery() {
        if (permissionClass.checkPermissionForExternalStorage()) {
            if (permissionClass.checkPermissionForReadExternalStorage()) {
                GlobalData.OnFilesendscreen = true;
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
            } else
                permissionClass.requestPermissionForReadExternalStorage();
        } else
            permissionClass.requestPermissionForExternalStorage();
    }


    public void uploadFile(File fileName) {


        FTPClient client = new FTPClient();

        try {

            client.connect(GlobalData.FTP_HOST.trim(), 21);
            client.login(GlobalData.FTP_USER.trim(),
                    GlobalData.FTP_PASS);
            client.setType(FTPClient.TYPE_BINARY);
            client.changeDirectory("/" + Utils.getUserId(this));

            client.upload(fileName, new MyTransferListener(fileName.getName()));

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.logout();
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                client.logout();
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public class MyTransferListener implements FTPDataTransferListener {
        String name;

        public MyTransferListener(String name) {
            this.name = name;
        }

        public void started() {


            // Transfer started
//            Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {
            //            Toast.makeText(getBaseContext(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
        }

        public void completed() {

            sendBackImageName(name);
            // Transfer completed

            /*Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });*/
            //System.out.println(" completed ..." );
        }

        public void aborted() {
            // Transfer aborted
            Toast.makeText(getBaseContext(), " transfer aborted ,please try again...", Toast.LENGTH_SHORT).show();
        }

        public void failed() {
            // Transfer failed
            System.out.println(" failed ...");
        }
    }

    private void sendBackImageName(final String s) {
        str = s;
        handler.sendEmptyMessage(1);
    }
}
