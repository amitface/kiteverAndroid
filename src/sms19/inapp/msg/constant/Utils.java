package sms19.inapp.msg.constant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kitever.android.R;
import com.kitever.contacts.ContactsActivity;
import com.kitever.pos.fragment.POSHomeTabbedActivity;
import com.kitever.sendsms.SendSmsMail;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PrivacyList;
import org.jivesoftware.smack.PrivacyListManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.PrivacyItem;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.MUCAdmin;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import sms19.inapp.msg.ChatFragment;
import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.database.DatabaseHelper;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.Countrymodel;
import sms19.inapp.msg.model.NewContactModelForFlag;
import sms19.inapp.msg.model.Recentmodel;
import sms19.inapp.msg.rest.Chatlistner;
import sms19.inapp.msg.rest.Downloadfilelistner;
import sms19.inapp.msg.rest.Rosterlistner;
import sms19.inapp.msg.rest.XmmpconnectionListner;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;
import sms19.listview.newproject.DataStorage;
import sms19.listview.newproject.Home;
import sms19.listview.newproject.Schedule;
import sms19.listview.newproject.SecondSmsMailReport;
import sms19.listview.newproject.SettingSmsMail;
import sms19.listview.newproject.SplashScreen;
import sms19.listview.newproject.Transaction;

@SuppressLint("NewApi")
public class Utils {

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    private static final long MONTH = (long) 30 * DAY;
    private static final long YEAR = (long) 12 * MONTH;
    public static boolean forreconnecting = false;
    public static long SERVER_TIME = 00;
    public static long CURRENT_TIME = 00;
    private static SetStatusOfflineAsync async = null;

    // private static int counter = 0;

    public static void printLog(String message) {
        if (message != null && message.trim().length() != 0) {
//            android.util.Log.e("SMS19Chat", message);
        }
    }

    public static void printLog2(String message) {
        // counter++;
        // android.util.Log.e("KK==", counter + " Counter= " + message);
    }

    public static void printLogSMS(String message) {
        if (message != null && message.trim().length() != 0) {
            // android.util.Log.e("SMS19Chat", message);
        }
    }

    public static void MakeDirs(Context ctx) {
        GlobalData.VideoPath = Environment.getExternalStorageDirectory()
                + "/Kitever/Media/Kitever Videos";

        GlobalData.ImagesPath = Environment.getExternalStorageDirectory()
                + "/Kitever/Media/Kitever Images";
        GlobalData.profilepicPath = Environment.getExternalStorageDirectory()
                + "/Kitever/Media/Kitever Profilepics";
        GlobalData.merchantProfilepicPath = Environment
                .getExternalStorageDirectory()
                + "/Kitever/Media/Kitever MerchantPic";
        if (!new File(GlobalData.VideoPath).exists()) {
            new File(GlobalData.VideoPath).mkdirs();
        }
        if (!new File(GlobalData.ImagesPath).exists()) {
            new File(GlobalData.ImagesPath).mkdirs();
        }
        if (!new File(GlobalData.profilepicPath).exists()) {
            new File(GlobalData.profilepicPath).mkdirs();
        }
        if (!new File(GlobalData.merchantProfilepicPath).exists()) {
            new File(GlobalData.merchantProfilepicPath).mkdirs();
        }
    }

    public static String getProfilePath() {

        String filepath = "";

        filepath = GlobalData.profilepicPath + "/Kitever "
                + System.currentTimeMillis() + ".jpg";

        return filepath;

    }

    public static String getProfilePathForGroupUpload(String filename) {

        String filepath = "";

        filepath = GlobalData.profilepicPath + "/Kitever" + filename + ".jpg";

        return filepath;

    }


    public static Bitmap decodeFile(String path, int dstWidth, int dstHeight,
                                    ScalingLogic scalingLogic) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth,
                options.outHeight, dstWidth, dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeFile(path, options);

        return unscaledBitmap;
    }
    public enum ScalingLogic {
        CROP, FIT
    }

    public static int calculateSampleSize(int srcWidth, int srcHeight,
                                          int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcWidth / dstWidth;
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();
        int neww = newWidth;
        int newh = newHeight;
        float scaleWidth;
        float scaleHeight;
        double ratio = 1;
        Bitmap resizedBitmap = null;
        int accw = 1;
        if (width > neww && height > newh) {
            try {

                if (width > height) {
                    ratio = (float) width / height;
                    accw = 1;
                } else if (height > width) {
                    ratio = (float) height / width;
                    accw = 2;
                } else {
                    ratio = 1;
                    accw = 3;
                }
                if (accw == 1) {
                    newh = (int) (neww / ratio);
                } else if (accw == 2) {
                    neww = (int) (newh / ratio);
                } else {
                    neww = newWidth;
                    newh = newWidth;
                }
                if (width > neww) {
                    scaleWidth = ((float) neww) / width;
                } else {
                    scaleWidth = ((float) width) / width;
                }
                if (height > newh) {
                    scaleHeight = ((float) newh) / height;
                } else {
                    scaleHeight = ((float) height) / height;
                }
                //
                // // CREATE A MATRIX FOR THE MANIPULATION
                Matrix matrix = new Matrix();
                // // RESIZE THE BIT MAP
                matrix.postScale(scaleWidth, scaleHeight);

                // "RECREATE" THE NEW BITMAP
                resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                        matrix, false);
                // Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, neww,
                // newh,
                // false);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return resizedBitmap;
        } else {
            return bm;
        }
    }

    public static void hideKeyBoardMethod(Context con, View view) {

        InputMethodManager imm = (InputMethodManager) con
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyBoard(Activity con) {
        con.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

    }

    public static boolean isDeviceOnline(Context context) {
        boolean isConnectionAvail = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConnectionAvail;
    }

    @SuppressLint("NewApi")
    public static void showMessage(final Context context, String message,
                                   String title, final String natbutton) {
        TextView titleText = new TextView(context);
        AlertDialog.Builder alertbox;
        if (android.os.Build.VERSION.SDK_INT >= 11) {

            alertbox = new AlertDialog.Builder(context,
                    AlertDialog.THEME_TRADITIONAL);
        } else {
            alertbox = new AlertDialog.Builder(context);
        }
        TextView myMsg = new TextView(context);
        myMsg.setTextSize(16);
        myMsg.setPadding(5, 10, 5, 20);
        myMsg.setTextColor(Color.parseColor("#ffffff"));
        myMsg.setText(message);
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        alertbox.setView(myMsg);
        titleText.setGravity(Gravity.CENTER);
        titleText.setPadding(0, 8, 0, 8);
        // titleText.setTextAppearance(context, R.style.boldText);
        titleText.setTextColor(Color.parseColor("#ffffff"));
        if (title == null) {

            titleText.setText("Message");

        } else {
            titleText.setText(title);

        }
        alertbox.setCustomTitle(titleText);

        alertbox.setCancelable(false);

        alertbox.setNeutralButton(natbutton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

        alertbox.show();
    }

    public static String makeJsonarrayfromContactmap(
            HashMap<String, Contactmodel> contacts) {

        JSONArray contactarray = new JSONArray();
        Iterator entries = contacts.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();

            Contactmodel model = (Contactmodel) entry.getValue();
            JSONObject singlecontact = new JSONObject();
            try {

                singlecontact.put("phonenumber", model.getNumber());
                singlecontact.put("name", model.getName());

                Contactmodel contactmodel = model;
                contactmodel.setFromNative(true);
                contactarray.put(singlecontact);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        // Log.d("contacts", contactarray.toString());
        // Utils.printLog("ContactName  is json array" +
        // contactarray.toString());

        // mCreateAndSaveFile(contactarray.toString());

        return contactarray.toString();

    }


    public static void LoginToServer(Context ctx) {
        try {
            if (GlobalData.connection != null) {
                // GlobalData.connection.disconnect();
                forreconnecting = true;
                new LoginToServer(ctx).execute();
            } else {
                forreconnecting = false;
                ConnectionConfiguration connConfig = new ConnectionConfiguration(
                        GlobalData.HOST, GlobalData.PORT);
                connConfig.setSASLAuthenticationEnabled(true);
                connConfig.setReconnectionAllowed(false);
                connConfig.setCompressionEnabled(false);
                connConfig.setSecurityMode(SecurityMode.disabled);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    connConfig.setTruststoreType("AndroidCAStore");
                    connConfig.setTruststorePassword(null);
                    connConfig.setTruststorePath(null);
                } else {
                    connConfig.setTruststoreType("BKS");
                    String path = System
                            .getProperty("javax.net.ssl.trustStore");
                    if (path == null)
                        path = System.getProperty("java.home") + File.separator
                                + "etc" + File.separator + "security"
                                + File.separator + "cacerts.bks";
                    connConfig.setTruststorePath(path);
                }

                GlobalData.connection = new XMPPConnection(connConfig);

                if (GlobalData.connection == null) {
                    LoginToServer(ctx);
                }
                setUpConnectionProperties();
                new LoginToServer(ctx).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class LoginToServer extends AsyncTask<Void, Void, Void> {
        String userjid = "";
        String password = "";
        Context ctxt;
        String stringNumber = "";

        public LoginToServer(Context ctx) {
            // TODO Auto-generated constructor stub
            ctxt = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            SharedPreferences chatPrefs = ctxt.getSharedPreferences(
                    "chatPrefs", Context.MODE_PRIVATE);
            userjid = chatPrefs.getString("user_jid", "");
            // password = chatPrefs.getString("userPassword", "");
            SharedPreferences prfs = ctxt.getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);
            password = prfs.getString("Pwd", "");
            stringNumber = chatPrefs.getString("userNumber", "");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                Utils.printLog("connection start...");
                // ServiceDiscoveryManager s = ServiceDiscoveryManager
                // .getInstanceFor(GlobalData.connection);
                //
                // if (s == null) {
                // s = new ServiceDiscoveryManager(GlobalData.connection);
                // }

                GlobalData.connection.connect();
                if (!forreconnecting) {
                    GlobalData.connection
                            .addConnectionListener(new XmmpconnectionListner());
                }
                // if (forreconnecting) {
                if (forreconnecting && GlobalData.connection != null
                        && GlobalData.connection.isConnected()
                        && GlobalData.connection.isAuthenticated()) {
                    ConstantFields.fromPrevconnection = true;
                    GlobalData.loginSuccess = true;
                    // }

                } else {
                    Chatlistner.addChatlistner(ctxt);

					/*
                     * GroupchatListner.addGroupChatlistner(ctxt);
					 * MultiUserChat.
					 * addInvitationListener(GlobalData.connection, new
					 * GroupchatInvitation());
					 */

                    login(ctxt, userjid, password);
                    // login(ctxt, userjid, password);

                }
            } catch (XMPPException ex) {
                GlobalData.loginSuccess = false;
                Utils.printLog("connection exctiption");
                ex.printStackTrace();
                // GlobalData.connection = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (!GlobalData.loginSuccess) {
                LoginToServer(ctxt);
            } else {
                ConstantFields.loginToserver = true;
            }

        }

    }

    // //////////////////////////////////////

    // //////////////////////////////////////////
    public static void login(Context ctxt, String userjid, String password) {
        if (GlobalData.connection != null
                && GlobalData.connection.isConnected()) {

            ConstantFields.fromPrevconnection = false;
            Utils.printLog("chat listner  added...");
            try {
                Utils.printLog("try to  login...");
                // SASLAuthentication.supportSASLMechanism("PLAIN", 0);
                GlobalData.connection.login(userjid, password);
                // GlobalData.connection.login(userjid, password);
                Presence presence = new Presence(Presence.Type.available);
                GlobalData.connection.sendPacket(presence);
                // ///////////////////////////////////
				/*
				 * if (GlobalData.msgEventManager == null) {
				 * GlobalData.msgEventManager = new MessageEventManager(
				 * GlobalData.connection); }
				 * Utils.printLog("Set MessageEventRequestListener");
				 * GlobalData.msgEventManager
				 * .addMessageEventRequestListener(new
				 * MessageEventrequestListener()); GlobalData.msgEventManager
				 * .addMessageEventNotificationListener(new
				 * MessageEventnotificationListener( ctxt,this));
				 */

                // //////////////////////////////////
                // ConstantFields.setMessageNotificationListener(); // comment m
                // //////////////////////////////////
                // /////////////////////////
                Utils.printLog("login successfully...");
                GlobalData.loginSuccess = true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                GlobalData.loginSuccess = false;
                e.printStackTrace();
                Utils.printLog("login excption");
                login(ctxt, userjid, password);

            }

        }
    }



    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

   /* public static void setRobotoThinFont(TextView tv, Context ctx) {

        Typeface robotothin = Typeface.createFromAsset(ctx.getAssets(),"fonts/roboto.regular.ttf");
        tv.setTypeface(robotothin);
    }


    public static void setRobotoThinFontSWitch(Switch tv, Context ctx) {
        Typeface robotothin = Typeface.createFromAsset(ctx.getAssets(),"fonts/roboto.regular.ttf");
        tv.setTypeface(robotothin);
    }

*/
    public static void setdosisboldFont(TextView tv, Context ctx) {

        Typeface DOSIS_BOLD = Typeface.createFromAsset(ctx.getAssets(),
                "Dosis-Bold.otf");
        tv.setTypeface(DOSIS_BOLD);
    }

    public static void setdosisregularFont(TextView tv, Context ctx) {

        Typeface DOSIS_REGULAR = Typeface.createFromAsset(ctx.getAssets(),
                "Dosis-Regular.otf");
        tv.setTypeface(DOSIS_REGULAR);
    }



    public static void setmyriadproFont(TextView tv, Context ctx) {
        Typeface DOSIS_S_BOLD = Typeface.createFromAsset(ctx.getAssets(),
                "MyriadPro-Regular.otf");
        tv.setTypeface(DOSIS_S_BOLD);
    }


    /*
For custom spinner font

*/
    public static class MySpinnerAdapter extends ArrayAdapter<String> {

         Typeface robotothin = Typeface.createFromAsset(getContext().getAssets(),
                  "fonts/roboto.regular.ttf");

        public MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(robotothin);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(robotothin);
            return view;
        }
    }



    public static void setUpConnectionProperties() {
        // Private Data Storage
        ProviderManager pm = null;
        try {

            pm = ProviderManager.getInstance();

            pm.addIQProvider("query", "jabber:iq:private",
                    new PrivateDataManager.PrivateDataIQProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Slack tags(Custom)
        try {
            pm.addExtensionProvider("chattag", "mysl:chattag:chat",
                    new SlacktagsProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Time

        try {

            pm.addIQProvider("query", "jabber:iq:time",
                    Class.forName("org.jivesoftware.smackx.packet.Time"));

        } catch (ClassNotFoundException e) {

            // //Log.w("TestClient",
            // "Can't load class for org.jivesoftware.smackx.packet.Time");

        }

        // Roster Exchange
        try {
            pm.addExtensionProvider("x", "jabber:x:roster",
                    new RosterExchangeProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Message Events
        try {
            pm.addExtensionProvider("x", "jabber:x:event",
                    new MessageEventProvider());

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Chat State
        try {
            pm.addExtensionProvider("active",
                    "http://jabber.org/protocol/chatstates",
                    new ChatStateExtension.Provider());

            pm.addExtensionProvider("composing",
                    "http://jabber.org/protocol/chatstates",
                    new ChatStateExtension.Provider());

            pm.addExtensionProvider("paused",
                    "http://jabber.org/protocol/chatstates",
                    new ChatStateExtension.Provider());

            pm.addExtensionProvider("inactive",
                    "http://jabber.org/protocol/chatstates",
                    new ChatStateExtension.Provider());

            pm.addExtensionProvider("gone",
                    "http://jabber.org/protocol/chatstates",
                    new ChatStateExtension.Provider());

            // XHTML
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pm.addExtensionProvider("html",
                    "http://jabber.org/protocol/xhtml-im",
                    new XHTMLExtensionProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Group Chat Invitations
        try {
            pm.addExtensionProvider("x", "jabber:x:conference",
                    new GroupChatInvitation.Provider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Service Discovery # Items
        try {

            pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
                    new DiscoverItemsProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Service Discovery # Info
        try {
            pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
                    new DiscoverInfoProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Data Forms
        try {
            pm.addExtensionProvider("x", "jabber:x:data",
                    new DataFormProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // MUC User
        try {
            pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
                    new MUCUserProvider());

            // MUC Admin

            pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
                    new MUCAdminProvider());

            // MUC Owner

            pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
                    new MUCOwnerProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // // Delayed Delivery
        // try {
        // pm.addExtensionProvider("x", "jabber:x:delay",
        // new DelayInformationProvider());
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // Version

        try {

            pm.addIQProvider("query", "jabber:iq:version",
                    Class.forName("org.jivesoftware.smackx.packet.Version"));

        } catch (ClassNotFoundException e) {

            // Not sure what's happening here.

        }

        // VCard
        try {
            pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Offline Message Requests
        try {
            pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
                    new OfflineMessageRequest.Provider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Offline Message Indicator
        try {
            pm.addExtensionProvider("offline",
                    "http://jabber.org/protocol/offline",
                    new OfflineMessageInfo.Provider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Last Activity
        try {
            pm.addIQProvider("query", "jabber:iq:last",
                    new LastActivity.Provider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // User Search
        try {
            pm.addIQProvider("query", "jabber:iq:search",
                    new UserSearch.Provider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // SharedGroupsInfo
        try {
            pm.addIQProvider("sharedgroup",
                    "http://www.jivesoftware.org/protocol/sharedgroup",
                    new SharedGroupsInfo.Provider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // JEP-33: Extended Stanza Addressing
        try {
            pm.addExtensionProvider("addresses",
                    "http://jabber.org/protocol/address",
                    new MultipleAddressesProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // FileTransfer

        // pm.addIQProvider("si", "http://jabber.org/protocol/si",
        // new StreamInitiationProvider());
        //
        // pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
        // new BytestreamsProvider());

        // pm.addIQProvider("open", "http://jabber.org/protocol/ibb",
        // new IBBProviders.Open());
        //
        // pm.addIQProvider("close", "http://jabber.org/protocol/ibb",
        // new IBBProviders.Close());
        //
        // pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb",
        // new IBBProviders.Data());

        // Privacy
        try {
            pm.addIQProvider("query", "jabber:iq:privacy",
                    new PrivacyProvider());

			/*
			 * pm.addIQProvider("query", "jabber:iq:privacy", new
			 * PrivacyProvider());
			 * Class.forName(PrivacyListManager.class.getName());
			 */

            pm.addIQProvider("command", "http://jabber.org/protocol/commands",
                    new AdHocCommandDataProvider());

            pm.addExtensionProvider("malformed-action",
                    "http://jabber.org/protocol/commands",
                    new AdHocCommandDataProvider.MalformedActionError());

            pm.addExtensionProvider("bad-locale",
                    "http://jabber.org/protocol/commands",
                    new AdHocCommandDataProvider.BadLocaleError());

            pm.addExtensionProvider("bad-payload",
                    "http://jabber.org/protocol/commands",
                    new AdHocCommandDataProvider.BadPayloadError());

            pm.addExtensionProvider("bad-sessionid",
                    "http://jabber.org/protocol/commands",
                    new AdHocCommandDataProvider.BadSessionIDError());

            pm.addExtensionProvider("session-expired",
                    "http://jabber.org/protocol/commands",
                    new AdHocCommandDataProvider.SessionExpiredError());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void setMediatoChat(final Activity act, final String path,
                                      final String url, final String filetype, final ImageView view,
                                      final ProgressBar progress) {

        new Thread(new Runnable() {
            public void run() {
                if (new File(path).exists()) {
                    act.runOnUiThread(new Runnable() {
                        public void run() {
                            progress.setVisibility(View.GONE);

                            if (filetype.equals(GlobalData.Imagefile)
                                    || filetype.equals(GlobalData.Locationfile)) {
                                view.setImageBitmap(BitmapFactory
                                        .decodeFile(path));
                            } else if (filetype.equals(GlobalData.Audiofile)) {

                            } else {
                                Bitmap thumbnail = ThumbnailUtils
                                        .createVideoThumbnail(
                                                path,
                                                MediaStore.Images.Thumbnails.MINI_KIND);
                                view.setImageBitmap(thumbnail);
                            }

                        }
                    });

                } else {
                    // String newPath = GlobalData.ImagesPath + "/Uchat_"
                    // + System.currentTimeMillis() + ".jpg";
                    // final Bitmap thmb = downloadThumbbitmap(url);
                    // act.runOnUiThread(new Runnable() {
                    // public void run() {
                    // if (thmb != null) {
                    // view.setImageBitmap(thmb);
                    //
                    // }
                    // }
                    // });

                    downloadfileandsave(url, path);
                    setMediatoChat(act, path, url, filetype, view, progress);
                }

            }
        }).start();

    }

    // public static Bitmap downloadThumbbitmap(String url) {
    // try {
    // int index=0;
    // String thumburl=
    // InputStream inputStream = new URL(url).openConnection()
    // .getInputStream();
    // Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
    // if (bitmap != null) {
    // return bitmap;
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // return null;
    // }
    //
    // return null;
    // }



    public synchronized static boolean downloadfileandsave(String url,
                                                           String filepath) {
        try {
            URL fileurl = new URL("http://" + url);
            long startTime = System.currentTimeMillis();

            // Open a connection to that URL.
            URLConnection ucon = fileurl.openConnection();

            // this timeout affects how long it takes for the app to realize
            // there's
            // a connection problem
            // ucon.setReadTimeout(TIMEOUT_CONNECTION);
            // ucon.setConnectTimeout(TIMEOUT_SOCKET);

            // Define InputStreams to read from the URLConnection.
            // uses 3KB download buffer
            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
            FileOutputStream outStream = new FileOutputStream(filepath);
            byte[] buff = new byte[5 * 1024];

            // Read bytes (and store them) until there is nothing more to
            // read(-1)
            int len;
            while ((len = inStream.read(buff)) != -1) {
                outStream.write(buff, 0, len);
            }

            // clean up
            outStream.flush();
            outStream.close();
            inStream.close();
            return true;
        } catch (Exception e) {
            new File(filepath).delete();
            e.printStackTrace();
            return false;
        }
    }

    public static String updatemessage(String type) {
        String msg = "";
        if (type.equals(GlobalData.Imagefile)) {
            msg = "Image";
        } else if (type.equals(GlobalData.Videofile)) {
            msg = "Video";
        } else if (type.equals(GlobalData.Audiofile)) {
            msg = "Audio";
        } else if (type.equals(GlobalData.Locationfile)) {
            msg = "Location";
        }

        return msg;
    }

    public static String getfilePath(String type) {
        String filepath = "";
        if (type.equals(GlobalData.Imagefile)) {
            filepath = GlobalData.ImagesPath + "/Kitever_"
                    + System.currentTimeMillis() + ".jpg";
        } else if (type.equals(GlobalData.Videofile)) {
            filepath = GlobalData.VideoPath + "/Kitever_"
                    + System.currentTimeMillis() + ".mp4";
        } else if (type.equals(GlobalData.Audiofile)) {
            filepath = GlobalData.VideoPath + "/Kitever_"
                    + System.currentTimeMillis() + ".mp3";
        } else if (type.equals(GlobalData.Locationfile)) {
            filepath = GlobalData.ImagesPath + "/Kitever_"
                    + System.currentTimeMillis() + ".jpg";
        }

        return filepath;
    }

    public static Bitmap oritRotation(Bitmap realImage, Uri uri) {
        // Bitmap realImage = bm;

        ExifInterface exif = null;

        try {
            exif = new ExifInterface(uri.toString());
        } catch (IOException e) {

            e.printStackTrace();

        }

        Log.i("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));

        if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase(
                "6")) {
            realImage = rotate(realImage, 90);
        } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                .equalsIgnoreCase("8")) {
            realImage = rotate(realImage, 270);
        } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                .equalsIgnoreCase("3")) {
            realImage = rotate(realImage, 180);
        }

        return realImage;
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // system.out.println("w*h " + w + "*" + h);

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);

    }

    public static String convertTobase64string(Bitmap img) {
        String bse64string = "";

        byte[] byteArray = null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byteArray = stream.toByteArray();

        bse64string = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return bse64string;

    }

    public static byte[] decodeToImage(String imageDataString) {
        return Base64.decode(imageDataString, Base64.DEFAULT);
    }

    public static Bitmap getCircularBitmapWithBorder(Bitmap bitmap,
                                                     int borderWidth, int bordercolor) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
                TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f
                : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(bordercolor);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2,
                paint);
        return canvasBitmap;
    }



    public static void vibrateplay(Context ctx) {
        Vibrator vibr = (Vibrator) ctx
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibr.vibrate(100);
    }

    public static void playselectedTones(Context ctx, String toneUri) {
        AudioManager audioManager = (AudioManager) ctx
                .getSystemService(Context.AUDIO_SERVICE);
        MediaPlayer thePlayer = new MediaPlayer();
        thePlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);

        try {
            thePlayer.setDataSource(ctx.getApplicationContext(),
                    Uri.parse(toneUri));
            // thePlayer.reset();
            thePlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // thePlayer
            // .setVolume(
            // Float.parseFloat(Double.toString(audioManager
            // .getStreamVolume(AudioManager.STREAM_NOTIFICATION) / 7.0)),
            // Float.parseFloat(Double.toString(audioManager
            // .getStreamVolume(AudioManager.STREAM_NOTIFICATION) / 7.0)));
            thePlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    // return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String getRecentmsgDateorTime(String timemillisec) {
        String msgDateorTime = "";
        if (timemillisec != null && timemillisec.trim().length() != 0) {

            try {
                Date msgD = new Date(Long.parseLong(timemillisec));
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                msgDateorTime = dateFormat.format(msgD);
                if (msgDateorTime.equals(getCurrentDate())) {
                    msgDateorTime = getmsgTime(timemillisec);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msgDateorTime;
    }

    public static String getSeperatorDateorTime(String timemillisec) {
        String msgDateorTime = "";
        if (timemillisec != null && timemillisec.trim().length() != 0) {

            try {
                Date msgD = new Date(Long.parseLong(timemillisec));
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                msgDateorTime = dateFormat.format(msgD);
                if (msgDateorTime.equals(getCurrentDate())) {
                    msgDateorTime = getmsgTime(timemillisec) + "`today";
                } else {
                    msgDateorTime = msgDateorTime + "`old";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msgDateorTime;
    }

    public static String getmsgDate(String timemillisec) {
        String msgDate = "";
        if (timemillisec != null && timemillisec.trim().length() != 0) {

            Date msgD = new Date(Long.parseLong(timemillisec));
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            msgDate = dateFormat.format(msgD);
        }
        return msgDate;
    }

    public static String getmsgTime(String timemillisec) {
        String msgTime = "";
        if (timemillisec != null && timemillisec.trim().length() != 0) {
            Date msgT = new Date(Long.parseLong(timemillisec));
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            msgTime = dateFormat.format(msgT);
        }
        return msgTime;
    }

    public static String getCurrentDate() {

        String CurrentDate = "";
        Date currentD = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        CurrentDate = dateFormat.format(currentD);
        return CurrentDate;
    }



    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri,
                new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(PhoneLookup.DISPLAY_NAME));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
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

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
                                                      int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap decodeFile(String f, int neww, int newh) {
        Bitmap image = null;

        Bitmap orignalbit = decodeScaledBitmapFromSdCard(f, neww, newh);

        try {
            int width = orignalbit.getWidth();
            int height = orignalbit.getHeight();
            // int neww = 200;
            // int newh = 200;
            float scaleWidth;
            float scaleHeight;
            double ratio = 1;
            int accw = 1;
            if (width > neww && height > newh) {
                if (width > height) {
                    ratio = (float) width / height;
                    accw = 1;
                } else if (height > width) {
                    ratio = (float) height / width;
                    accw = 2;
                } else {
                    ratio = 1;
                    accw = 3;
                }
                if (accw == 1) {
                    newh = (int) (neww / ratio);
                } else if (accw == 2) {
                    neww = (int) (newh / ratio);
                } else {
                    neww = neww;
                    newh = neww;
                }
                if (width > neww) {
                    scaleWidth = ((float) neww) / width;
                } else {
                    scaleWidth = ((float) width) / width;
                }
                if (height > newh) {
                    scaleHeight = ((float) newh) / height;
                } else {
                    scaleHeight = ((float) height) / height;
                }
                //
                // // CREATE A MATRIX FOR THE MANIPULATION
                Matrix matrix = new Matrix();
                // // RESIZE THE BIT MAP
                matrix.postScale(scaleWidth, scaleHeight);

                // "RECREATE" THE NEW BITMAP
                image = Bitmap.createBitmap(orignalbit, 0, 0, width, height,
                        matrix, false);
                // Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, neww,
                // newh,
                // false);
                return Utils.oritRotation(image, Uri.parse(f));
            } else {
                // return orignalbit;
                return Utils.oritRotation(orignalbit, Uri.parse(f));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Utils.oritRotation(orignalbit, Uri.parse(f));
        }
        // try {
        // if (image != null) {
        //
        // FileOutputStream fis = new FileOutputStream(f);
        // int quality = 100;
        //
        // image.compress(CompressFormat.JPEG, quality, fis);
        // fis.close();
        // // return image;
        // return Utils.oritRotation(image, Uri.parse(f));
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

    }



    public static void saveContactItem(Context context,
                                       NewContactModelForFlag loginUserBean) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            Gson gson = new Gson();
            String json = gson.toJson(loginUserBean);
            prefsEditor.putString("ContactItem", json);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static NewContactModelForFlag getContactItem(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            Gson gson = new Gson();
            String json = mPrefs.getString("ContactItem", "");
            NewContactModelForFlag obj = gson.fromJson(json,
                    NewContactModelForFlag.class);
            prefsEditor.commit();
            return obj;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static void saveSelectedItem(Context context,
                                        HashMap<String, Contactmodel> hashMap) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            Gson gson = new Gson();
            String json = gson.toJson(hashMap);
            prefsEditor.putString("saveSelectedItem", json);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static HashMap<String, Contactmodel> getSelectedItem(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            Gson gson = new Gson();
            String json = mPrefs.getString("saveSelectedItem", null);
            Type typ1ew2 = new TypeToken<HashMap<String, Contactmodel>>() {
            }.getType();
            HashMap<String, Contactmodel> obj = gson.fromJson(json, typ1ew2);
            prefsEditor.commit();
            return obj;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }



    public static ArrayList<Countrymodel> getCountryList(final Context context) {

        final ArrayList<Countrymodel> arrayList = new ArrayList<>();

        if (context != null) {
            try {

                final String strJson = new String(readAsset(
                        context.getAssets(), "countrylist.json"));

                if (strJson != null && strJson.trim().length() != 0) {
                    try {

                        JSONArray jsonArray = new JSONArray(strJson);

                        if (jsonArray != null) {
                            final int size = jsonArray.length();
                            for (int i = 0; i < size; i++) {
                                Countrymodel bean = new Countrymodel();
                                JSONObject jsonObject = jsonArray
                                        .getJSONObject(i);
                                if (jsonObject.has("code")) {
                                    bean.setCountryISOCode(jsonObject
                                            .getString("code"));
                                }
                                if (jsonObject.has("dial_code")) {
                                    bean.setCountrycode(jsonObject
                                            .getString("dial_code"));
                                }
                                if (jsonObject.has("name")) {
                                    bean.setCountry_name(jsonObject
                                            .getString("name"));
                                }
                                arrayList.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {

                e.printStackTrace();
            }

            return arrayList;

        } else {

            return null;

        }

    }

    /*
	 *
	 * Reads the text of an asset. Should not be run on the UI thread.
	 *
	 * @param mgr The {@link AssetManager} obtained via {@link
	 * Context#getAssets()}
	 *
	 * @param path The path to the asset.
	 *
	 * @return The plain text of the asset
	 */
    public static String readAsset(AssetManager mgr, String path) {
        String contents = "";
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = mgr.open(path);
            reader = new BufferedReader(new InputStreamReader(is));
            contents = reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
                contents += '\n' + line;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return contents;
    }

    public static void saveUserType(Context context, String loginUserBean) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            prefsEditor.putString("UserType1", loginUserBean);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getUserType(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            String json = mPrefs.getString("UserType1", "");
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static void saveGroupLimit(Context context, int loginUserBean) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            prefsEditor.putInt("GroupLimit", loginUserBean);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Integer getGroupLimit(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            int json = mPrefs.getInt("GroupLimit", 0);
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static void saveBroadCastGroupLimit(Context context,
                                               int loginUserBean) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            prefsEditor.putInt("BroadCast", loginUserBean);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Integer getBroadCastGroupLimit(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            int json = mPrefs.getInt("BroadCast", 0);
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static void saveUserId(Context context, String loginUserBean) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            prefsEditor.putString("UserId", loginUserBean);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getUserId(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            String json = mPrefs.getString("UserId", "");
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static String getUserLogin(Context context) {

        try {

            SharedPreferences prfs = context.getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);
            String user_login = prfs.getString("user_login", "");
            return user_login;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static void savePhoneNumber(Context context, String loginUserBean) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            prefsEditor.putString("PhoneNumber", loginUserBean);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getPhoneNumber(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            String json = mPrefs.getString("PhoneNumber", "");
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static void savePassword(Context context, String loginUserBean) {
        try {
            SharedPreferences prfs = context.getSharedPreferences(
                    "profileData", Context.MODE_PRIVATE);
            Editor editor = prfs.edit();
            editor.putString("Pwd", loginUserBean);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getPassword(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            // String json = mPrefs.getString("UserPassword", "");
            SharedPreferences prfs = context.getSharedPreferences(
                    "profileData", Context.MODE_PRIVATE);
            String json = prfs.getString("Pwd", "");
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static void userIsCreateFtpFolder(Context context,
                                             Boolean loginUserBean) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            prefsEditor.putBoolean("IsCreateFtpFolder", loginUserBean);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Boolean getIsCreateFtpFolder(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            boolean json = mPrefs.getBoolean("IsCreateFtpFolder", false);
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public static void saveCountryCodeNumber(Context context, String code) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            Gson gson = new Gson();
            // String json = gson.toJson(loginUserBean);
            prefsEditor.putString("CountryCodeNumber", code);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static String getCountryCode(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            String countryCode = mPrefs.getString("countryCode", "");
            // String obj = gson.fromJson(json, String.class);
            prefsEditor.commit();
            return countryCode;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static void saveCountryCode(Context context, String code) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            prefsEditor.putString("countryCode", code);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void saveUserTime(Context context, long loginUserBean) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            // String json = gson.toJson(loginUserBean);
            prefsEditor.putLong("UserTime", loginUserBean);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static long getUserTime(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            long json = mPrefs.getLong("UserTime", 0);

            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    public static void saveUserTimeCurrent(Context context, long loginUserBean) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            // String json = gson.toJson(loginUserBean);
            prefsEditor.putLong("UserTimeCurrent", loginUserBean);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static long getUserTimeCurrent(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            long json = mPrefs.getLong("UserTimeCurrent", 0);

            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    public static void saveExpiryTimeValue(Context context, String value) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            // Gson gson = new Gson();
            // String json = gson.toJson(loginUserBean);
            prefsEditor.putString("ExpiryTimeValue", value);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getExpiryTimeValue(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            // Gson gson = new Gson();
            String json = mPrefs.getString("ExpiryTimeValue", "");
            // String obj = gson.fromJson(json, String.class);
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static void saveExpiryTimeValueMail(Context context, String value) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            // Gson gson = new Gson();
            // String json = gson.toJson(loginUserBean);
            prefsEditor.putString("ExpiryTimeValueMail", value);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void saveBaseUrlValue(Context context, String value) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            // Gson gson = new Gson();
            // String json = gson.toJson(loginUserBean);
            prefsEditor.putString("BaseUrl", value);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getBaseUrlValue(Context context) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            // Gson gson = new Gson();
            String json = mPrefs.getString("BaseUrl", "");
            // String obj = gson.fromJson(json, String.class);
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static void saveUserCustomStatus(Context context,
                                            String loginUserBean) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            Gson gson = new Gson();
            // String json = gson.toJson(loginUserBean);
            prefsEditor.putString("CustomStatus", loginUserBean);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getUserCustomStatus(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            Gson gson = new Gson();
            String json = mPrefs.getString("CustomStatus", "");
            String obj = gson.fromJson(json, String.class);
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public static void saveByDefaultSendMsgYesNo(Context context, boolean yes_no) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            // String json = gson.toJson(loginUserBean);
            prefsEditor.putBoolean("ByDefaultSendMsgYesNo", yes_no);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean getByDefaultSendMsgYesNo(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            boolean json = mPrefs.getBoolean("ByDefaultSendMsgYesNo", false);
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public static void saveByDefaultSendMailYesNo(Context context, boolean yes_no) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            // String json = gson.toJson(loginUserBean);
            prefsEditor.putBoolean("ByDefaultSendMailYesNo", yes_no);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean getByDefaultSendMailYesNo(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            boolean json = mPrefs.getBoolean("ByDefaultSendMailYesNo", false);
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public static String removeCountryCode(String number, Context context) {

        String postContactNo = "";
        try {

            postContactNo = number;

            postContactNo = postContactNo.replace(getCountryCode(context), "");
            if (postContactNo.startsWith("0")) {
                postContactNo.substring(1, postContactNo.length());
            }

            postContactNo.replace(" ", "");
            postContactNo.replace("@", "");
            postContactNo.replace(".", "");
            return postContactNo;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return postContactNo;

    }



    public static String currentTimeStamp() {

        String string = "";

        try {
            int time = (int) (System.currentTimeMillis() / 1000L);
            // Timestamp tsTemp = new Timestamp(time);
            string = String.valueOf(time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return string;

    }

    public static String getUsersNameWithGroupName(
            ArrayList<Recentmodel> arrayList) {

        try {
            String finalName = "";
            String name = "";
            String longName = "";
            if (arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {

                    if (i == 0) {
                        longName = arrayList.get(i).getDisplayname();
                        name = arrayList.get(i).getDisplayname();
                    } else if (i <= 2) {
                        longName = longName + ", "
                                + arrayList.get(i).getDisplayname();
                        name = name + ", " + arrayList.get(i).getDisplayname();
                    } else {

                        name = name + " + "
                                + String.valueOf(arrayList.size() - 2)
                                + " others";
                        break;
                    }

                }
                finalName = name;
            }

            return finalName;
        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }

        return "";

    }

    public static Collection<myAffiliate> getAffiliatesByAdmin(
            String paramString, String roomid) throws XMPPException {
        MUCAdmin localMUCAdmin1 = new MUCAdmin();
        localMUCAdmin1.setTo(roomid);
        localMUCAdmin1.setType(IQ.Type.GET);

        MUCAdmin.Item localItem = new MUCAdmin.Item(paramString, null);
        localMUCAdmin1.addItem(localItem);

        PacketIDFilter localPacketIDFilter = new PacketIDFilter(
                localMUCAdmin1.getPacketID());
        PacketCollector localPacketCollector = GlobalData.connection
                .createPacketCollector(localPacketIDFilter);

        GlobalData.connection.sendPacket(localMUCAdmin1);

        MUCAdmin localMUCAdmin2 = (MUCAdmin) localPacketCollector
                .nextResult(SmackConfiguration.getPacketReplyTimeout());

        localPacketCollector.cancel();
        if (localMUCAdmin2 == null) {
            throw new XMPPException("No response from server.");
        }
        if (localMUCAdmin2.getError() != null) {
            throw new XMPPException(localMUCAdmin2.getError());
        }
        ArrayList localArrayList = new ArrayList();
        for (Iterator localIterator = localMUCAdmin2.getItems(); localIterator
                .hasNext(); ) {

            localArrayList.add(new myAffiliate((MUCAdmin.Item) localIterator
                    .next()));
        }
        return localArrayList;
    }

    public static void downloadfileandsave(String url, String filepath,
                                           String rowid, String DashboardID, Downloadfilelistner listr) {
        new Downloadfileasync(url, filepath, rowid, DashboardID, listr)
                .execute();
    }

    static class Downloadfileasync extends AsyncTask<Void, Void, Void> {
        String fileUrl = "";
        String path = "";
        String dbrowid = "";
        String DashboardID = "";
        String dasboardjson = "";
        boolean success = false;
        Downloadfilelistner listner;

        public Downloadfileasync(String url, String filepath, String rowid,
                                 String Dashboard, Downloadfilelistner listr) {
            // TODO Auto-generated constructor stub
            fileUrl = url;
            path = filepath;
            dbrowid = rowid;
            DashboardID = Dashboard;
            listner = listr;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {

                URL fileurl = new URL("http://" + fileUrl);
                long startTime = System.currentTimeMillis();

                // Open a connection to that URL.
                URLConnection ucon = fileurl.openConnection();

                InputStream is = ucon.getInputStream();
                BufferedInputStream inStream = new BufferedInputStream(is,
                        1024 * 5);
                FileOutputStream outStream = new FileOutputStream(path);
                byte[] buff = new byte[5 * 1024];

                // Read bytes (and store them) until there is nothing more to
                // read(-1)
                int len;
                while ((len = inStream.read(buff)) != -1) {
                    outStream.write(buff, 0, len);
                }

                // clean up
                outStream.flush();
                outStream.close();
                inStream.close();
                success = true;
            } catch (Exception e) {
                new File(path).delete();
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (GlobalData.OnChatfrag) {
                if (listner != null) {
                    if (success) {
                        listner.downloadsuccess(dbrowid, dasboardjson);
                    } else {
                        listner.downloadfail(dbrowid, dasboardjson);
                    }
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

    }



    public static String getFormattedDateFromTimestamp(
            long timestampInMilliSeconds) {
        Date date = new Date();
        date.setTime(timestampInMilliSeconds);
        String formattedDate = new SimpleDateFormat("MMM d, yyyy").format(date);
        return formattedDate;

    }

    public static void saveFileInProfileFolder(Bitmap bitmap,
                                               Activity homeActivity) {
        OutputStream output;

        String path = Utils.getProfilePath();
        File file = new File(path);

        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();

            Utils.saveImageInGallery(homeActivity, path);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void applyGMTOffsetDST(Context context) {
        // Works out adjustments for timezone and daylight saving time

        String TIME_SERVER = "time-a.nist.gov";
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(TIME_SERVER);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TimeInfo timeInfo = null;
        try {
            timeInfo = timeClient.getTime(inetAddress);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // long returnTime = timeInfo.getReturnTime(); //local device time
        long returnTime = timeInfo.getMessage().getTransmitTimeStamp()
                .getTime(); // server time

        String currentTime = getmsgTime(String.valueOf(returnTime));
        SERVER_TIME = returnTime;
        CURRENT_TIME = System.currentTimeMillis();
        if (context != null) {
            Utils.saveUserTime(context, returnTime);
            Utils.saveUserTimeCurrent(context, CURRENT_TIME);
        }

    }

    public static String currentTimeStap() {

        String currentTime = "";
        try {
            long diff = System.currentTimeMillis() - CURRENT_TIME;
            long cirrentTmStap = SERVER_TIME + diff;
            // String newsfsf= Utils.getmsgTime(String.valueOf(cirrentTmStap));
            currentTime = (String.valueOf(cirrentTmStap));

        } catch (Exception e) {
            currentTime = (String.valueOf(System.currentTimeMillis()));

            e.printStackTrace();
        }

        return currentTime;

    }

    public static void expiryTimedifference(String expirytim,
                                            String msgAddtime, String messageid, int isgroup, String packete_id) {

        long currentMint = TimeUnit.MILLISECONDS.toMinutes(Long
                .valueOf(currentTimeStap()));
        long expirytimeMint = Long.valueOf(expirytim);

        long minutesMint = TimeUnit.MILLISECONDS.toMinutes(Long
                .valueOf(msgAddtime));
        long diff = currentMint - minutesMint;

        if (isgroup == 2) {// new add
            Utils.printLog("expiry tm diff " + diff + "");
            GlobalData.dbHelper.broadCastShowCountUpdateForSingleUser(
                    packete_id, 3);
        }

        if (diff >= expirytimeMint) {
            if (isgroup == 2) {
                Utils.printLog("expiry tm diff " + diff + "");
                GlobalData.dbHelper.broadCastShowCountUpdateForSingleUser(
                        packete_id, 3);
            } else {
                GlobalData.dbHelper.broadCastShowCountUpdateForSingleUser(
                        packete_id, 3);

            }
        } else {
            Utils.printLog("expiry tm diff-- " + diff + "");
            String isDelivered = GlobalData.dbHelper
                    .isMessageDelivered(packete_id);
            if (isDelivered != null && isDelivered.length() > 0) {
                if (isDelivered.equalsIgnoreCase("1")) {
                    if (isgroup == 0) {
                        // String
                        // packet_id=GlobalData.dbHelper.getPacketId(messageid);
                        GlobalData.dbHelper
                                .broadCastShowCountUpdateForSingleUser(
                                        packete_id, 0);
                    }
                }
            }

        }

    }

    public static void saveGroupLeaveTm(SharedPreferences chatPrefs) {

        Editor editor = chatPrefs.edit();

        editor.putLong("lastleaveTime", Utils.getGroupLeavTime());
        editor.commit();

    }

    public static long getGroupLeavTime() {

        long cirrentTmStap = 0;
        try {

            long diff = System.currentTimeMillis() - CURRENT_TIME;
            cirrentTmStap = SERVER_TIME + diff;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return cirrentTmStap;

    }

    public static void callForGetTimeAsyncTask(Context context) {
        getCurrentTime currentTime = new getCurrentTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            currentTime.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    context);
        } else {
            currentTime.execute(context);
        }
    }

    public static class getCurrentTime extends AsyncTask<Context, Void, String> {

        @Override
        protected String doInBackground(Context... params) {

            try {
                Context context = params[0];
                applyGMTOffsetDST(context);

            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;
        }

    }

	/*
	 * public static void offlineShowUser(){ if(GlobalData.dbHelper!=null){
	 * //ArrayList<Contactmodel> arrayListNew=new ArrayList<Contactmodel>();
	 * //arrayListNew
	 * .addAll((GlobalData.dbHelper.getContactfromDBOnlyRegister()));
	 * if(GlobalData.registerAndStragerGlobalArrayList!=null){
	 *
	 * try { for(int
	 * i=0;i<GlobalData.registerAndStragerGlobalArrayList.size();i++){
	 * GlobalData
	 * .dbHelper.offlineStatusSet(GlobalData.registerAndStragerGlobalArrayList
	 * .get(i).getRemote_jid(),"0"); } } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 *
	 * } }
	 *
	 * }
	 */

    public static void saveImageInGallery(Activity activity, String path) {
        File file2 = new File(path);
        activity.sendBroadcast(new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file2)));
    }

    public static void addChatHistoryGettedFromServer(String remote_jid,
                                                      String message, String key_from_me, String msgtime,
                                                      String message_packetid, String sent_msg_success, String msg_type) {

        GlobalData.dbHelper.downloadedAddchatToMessagetable(remote_jid,
                message, key_from_me, msgtime, message_packetid, "1", msg_type);// not
        // set
        // packet
        // id
        // because
        // its
        // friend
        // msg

    }

    public static void offlineShowUser() {
        if (async == null) {
            async = new SetStatusOfflineAsync();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                async.execute();
            }
        } else {
            async.cancel(true);
            async = null;
            async = new SetStatusOfflineAsync();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                async.execute();
            }
        }

    }

    private static class SetStatusOfflineAsync extends
            AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                offlineMethod();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (SingleChatRoomFrgament.ON_SINGLE_CHAT_PAGE) {
                SingleChatRoomFrgament singleChatRoomFrgament = SingleChatRoomFrgament
                        .newInstance("");
                if (singleChatRoomFrgament != null) {
                    singleChatRoomFrgament.showOnlineUser();
                }
            }

            if (GlobalData.OnHomefrag) {
                ChatFragment chatFragment = ChatFragment.newInstance("");
                if (chatFragment != null) {
                    chatFragment.refreshChatAdapter();
                }
            }
        }

    }

    public synchronized static void offlineMethod() {

        try {
            if (GlobalData.dbHelper != null) {
                if (GlobalData.registerAndStragerGlobalArrayList != null) {

                    try {
                        for (int i = 0; i < GlobalData.registerAndStragerGlobalArrayList
                                .size(); i++) {
                            GlobalData.dbHelper
                                    .offlineStatusSet(
                                            GlobalData.registerAndStragerGlobalArrayList
                                                    .get(i).getRemote_jid(),
                                            "0");
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void saveDeviceToken(Context context, String loginUserBean) {
        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            // String json = gson.toJson(loginUserBean);
            prefsEditor.putString("DeviceToken", loginUserBean);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getDeviceToken(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();

            String json = mPrefs.getString("DeviceToken", "");

            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return "";
    }



    // public static ArrayList<Contactmodel> contactmodelGlobalList;
    public static void GetBroadCastGroupList(String userId, String user_jid,
                                             Context context) {

        // ContactDetailsNew Model = (ContactDetailsNew) ;
        // new SMS19AddContactAsyncTask().execute(contactDetailsNews);

        sms19.inapp.msg.asynctask.GetBroadCastGroupList broadCastGroupList = new sms19.inapp.msg.asynctask.GetBroadCastGroupList(
                userId, user_jid, context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            broadCastGroupList
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            broadCastGroupList.execute();
        }

    }



    public static void refreshImageAndStatus(String remote_jid) {

        if (ConstantFields.HIDE_MENU == 77) {
            sms19.inapp.msg.GroupListFrgament groupListFrgament = sms19.inapp.msg.GroupListFrgament
                    .newInstance("");
            if (groupListFrgament != null) {
                // if(remote_jid.equalsIgnoreCase(groupListFrgament.getRemote_jid())){
                // singleChatRoomFrgament.showOnlineUser();

                android.os.Message msg = new android.os.Message();
                Bundle b = new Bundle();
                b.putString("remote_jid", remote_jid);
                msg.setData(b);
                sms19.inapp.msg.GroupListFrgament.onLineOfflineHandler
                        .sendMessage(msg);// comment m

                // }
            }

        } else {

            SingleChatRoomFrgament singleChatRoomFrgament = SingleChatRoomFrgament
                    .newInstance("");
            if (singleChatRoomFrgament != null) {
                if (remote_jid.equalsIgnoreCase(singleChatRoomFrgament
                        .getRemote_jid())) {
                    // singleChatRoomFrgament.showOnlineUser();

                    android.os.Message msg = new android.os.Message();
                    Bundle b = new Bundle();
                    b.putString("onlineofline", "1");
                    msg.setData(b);
                    SingleChatRoomFrgament.onLineOfflineHandler
                            .sendMessage(msg);// comment m

                }

            }

            sms19.inapp.msg.GroupProfile groupProfile = sms19.inapp.msg.GroupProfile
                    .newInstance();
            if (groupProfile != null) {
                if (remote_jid.equalsIgnoreCase(groupProfile.getRemote_jid())) {
                    // singleChatRoomFrgament.showOnlineUser();

                    android.os.Message msg = new android.os.Message();
                    Bundle b = new Bundle();
                    b.putString("remote_jid", remote_jid);
                    msg.setData(b);
                    sms19.inapp.msg.GroupProfile.onLineOfflineHandler
                            .sendMessage(msg);// comment m

                }

            }

            sms19.inapp.msg.ProfileEditFragment editFragment = sms19.inapp.msg.ProfileEditFragment
                    .getInstance();
            if (editFragment != null) {
                if (remote_jid.equalsIgnoreCase(groupProfile.getRemote_jid())) {
                    // singleChatRoomFrgament.showOnlineUser();

                    android.os.Message msg = new android.os.Message();
                    Bundle b = new Bundle();
                    b.putString("remote_jid", remote_jid);
                    msg.setData(b);
                    sms19.inapp.msg.ProfileEditFragment.onLineOfflineHandler
                            .sendMessage(msg);// comment m

                }

            }

        }

    }

    public static String LanguageDetect(String text) {
        // Detector detector;
        String langDetected = "";

        try {
			/*
			 * String path = DatabaseHelper.LNAGUAGE_DETECTION_DATA;
			 * DetectorFactory.loadProfile(path); detector =
			 * DetectorFactory.create(); detector.append(text); langDetected =
			 * detector.detect();
			 *
			 * langDetected = detector.detect();
			 */
            String pattern = "^[A-Za-z0-9. ]+$";
            if (text.matches(pattern)) {
                langDetected = "en";
            }
            // is English
            else {
                langDetected = "other";
            }

            Utils.printLogSMS("language---" + langDetected);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return langDetected;
    }

    public static void marchantCodeDialog(Activity activity, String title,
                                          String msg) {

        {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                    activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            final View dialogView = inflater.inflate(
                    R.layout.dialog_info_merchant_code, null);
            dialogBuilder.setView(dialogView);

            dialogBuilder.setCancelable(false);
            TextView textView = (TextView) dialogView.findViewById(R.id.close);
            TextView textView1 = (TextView) dialogView
                    .findViewById(R.id.pop_title);
            TextView textView2 = (TextView) dialogView
                    .findViewById(R.id.pop_msg);
            textView1.setText(title);
            textView2.setText(msg);

			/*
			 * dialogBuilder.setTitle("Custom dialog");
			 * dialogBuilder.setMessage("Enter text below");
			 */
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog alertDialog = (AlertDialog) v.getTag();
                    alertDialog.dismiss();

                }
            });

            AlertDialog b = dialogBuilder.create();
            b.getWindow().setBackgroundDrawableResource(R.color.tran_balck);
            textView.setTag(b);
            b.show();
        }

    }

    public static List<String> XMPPAddNewPrivacyList(XMPPConnection connection,
                                                     String userId) {

        List<String> privacyList = new ArrayList<String>();
        String listName = "newList";
        if (connection != null && connection.isConnected()
                && connection.isAuthenticated()) {

            try {

                if (GlobalData.blockUserJid == null) {
                    GlobalData.blockUserJid = new HashMap<String, String>();
                }

                if (GlobalData.privacyManager == null) {
                    GlobalData.privacyManager = PrivacyListManager
                            .getInstanceFor(GlobalData.connection);
                }

                if (GlobalData.privacyManager == null) {
                    return privacyList;
                }
                // String ser = "@" + XMPPUtils.INSTANCE.XMPPService;
                PrivacyList plist = null;
                try {
                    // plist = GlobalData.privacyManager.getPrivacyLists();
                    plist = GlobalData.privacyManager.getPrivacyList(listName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (plist != null) {// No blacklisted or is not listed, direct
                    // getPrivacyList error
                    List<PrivacyItem> items = plist.getItems();
                    for (PrivacyItem item : items) {

                        String from = item.getValue();
                        int order = item.getOrder();
                        if (order == 8) {
                            GlobalData.blockUserJid.put(from, from);
                            GlobalData.dbHelper.singleContactBlockfromDB(from);
                        } else {
                            if (GlobalData.blockUserJid.containsKey(from)) {
                                GlobalData.blockUserJid.remove(from);
                                GlobalData.dbHelper
                                        .singleContactUnBlockfromDB(from);
                            }

                        }

                    }
                } else {
                    return privacyList;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return privacyList;
    }

    public static void addRoster() {

        if (GlobalData.connection != null
                && GlobalData.connection.isConnected()
                && GlobalData.connection.isAuthenticated()) {

            try {
                AddRoasterAsyncTask addRoasterAsyncTask = new AddRoasterAsyncTask();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    addRoasterAsyncTask
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    addRoasterAsyncTask.execute();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }

    public static class AddRoasterAsyncTask extends
            AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                // runForAddRoaster();
                addingRosterentry();

            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;
        }
    }

    public synchronized static void addingRosterentry() {

        if (GlobalData.connection != null
                && GlobalData.connection.isConnected()
                && GlobalData.connection.isAuthenticated()) {

            Roster roster = GlobalData.connection.getRoster();
            roster.setSubscriptionMode(SubscriptionMode.accept_all);
            ArrayList<Contactmodel> arrayList = null;
            if (GlobalData.dbHelper != null) {
                if (GlobalData.dbHelper.getContactfromDBOnlyRegister() == null) {
                    arrayList = new ArrayList<Contactmodel>();
                } else {
                    arrayList = GlobalData.dbHelper
                            .getContactfromDBOnlyRegister();
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    String remote_jid = arrayList.get(i).getRemote_jid();
                    String remote_name = arrayList.get(i).getName();
                    Utils.printLog("Entry added start : " + remote_jid + " "
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
                        success = createrosterentry(roster, remote_jid,
                                remote_name, retrycount);
                    }
                    Utils.printLog("Entry added success : " + remote_jid + " "
                            + remote_name + ", count==" + retrycount);

                }
            }

            try {
                if (GlobalData.connection != null
                        && GlobalData.connection.isConnected()
                        && GlobalData.connection.isAuthenticated()) {
                    GlobalData.roster = GlobalData.connection.getRoster();

                    if (GlobalData.roster != null) {

                        Utils.printLog("roster not equal null");

                        GlobalData.roster
                                .setSubscriptionMode(SubscriptionMode.accept_all);
                        GlobalData.roster
                                .addRosterListener(new Rosterlistner());// ad
                        // new
                        // 11
                        // may

                        final Collection<RosterEntry> entries = GlobalData.roster
                                .getEntries();

                        for (RosterEntry r : entries) {

                            String remote_jid = "";
                            remote_jid = r.getUser();

                            if (!GlobalData.dbHelper.isContactBlock(remote_jid)) {

                                ItemType subtype = r.getType();
                                int retrycountt = 0;
                                if (subtype == ItemType.both) {

                                } else {

                                    Utils.printLog("sub type of " + remote_jid
                                            + "==" + subtype.toString());
                                    boolean success = sendsubrequest(
                                            remote_jid, subtype, retrycountt);
                                    while (!success) {

                                        retrycountt++;
                                        success = sendsubrequest(remote_jid,
                                                subtype, retrycountt);
                                    }
                                }
                                byte[] byteArray = null;
                                String picname = "";
                                String status = "";

                                Presence presence = GlobalData.roster
                                        .getPresence(r.getUser());

                                if (presence.getType().equals(
                                        Presence.Type.available)) {
                                    status = "1";
                                } else {
                                    status = "0";
                                }

                                Utils.printLog(r.getUser() + " : "
                                        + presence.toString());

								/*
								 * if (chatPrefs.getBoolean("firsttime", false))
								 * {
								 *
								 * GlobalData.dbHelper.updateContactdata(remote_jid
								 * ,status, byteArray, picname, 1,false);
								 *
								 * } else {
								 * GlobalData.dbHelper.updateContactdata
								 * (remote_jid,status, byteArray, picname,
								 * 0,false); }
								 */

                                if (GlobalData.dbHelper
                                        .checkcontactisAlreadyexist(remote_jid)) {
                                    Utils.printLog("Entry for load vcard "
                                            + remote_jid);

                                    GlobalData.dbHelper
                                            .DownloadVcardandupdateContact(remote_jid);
                                }

                                if (GlobalData.OnHomefrag) {

                                    ChatFragment chatFragment = ChatFragment
                                            .newInstance("");
                                    if (chatFragment != null) {
                                        if (ChatFragment.UserupStatusHandler != null) {
                                            Message msg = new Message();
                                            Bundle b = new Bundle();
                                            b.putString("updatestatus", status);
                                            b.putString("remoteid", remote_jid);
                                            msg.setData(b);
                                            ChatFragment.UserupStatusHandler
                                                    .sendMessage(msg);// comment
                                            // m
                                        }
                                    }

                                }

                            }

                        }

                        Utils.printLog("new connection listner added.");
                        if (GlobalData.roster != null) {
                            Utils.printLog("roster not equal null");

                            if (SingleChatRoomFrgament.ON_SINGLE_CHAT_PAGE) {

                                SingleChatRoomFrgament singleChatRoomFrgament = SingleChatRoomFrgament
                                        .newInstance("");
                                if (singleChatRoomFrgament != null) {

                                    Message msg = new Message();
                                    Bundle b = new Bundle();
                                    b.putString("onlineofline", "1");
                                    msg.setData(b);
                                    SingleChatRoomFrgament.onLineOfflineHandler
                                            .sendMessage(msg);// comment m
                                }

                            }

                        }

                    }
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    }

    public static boolean sendsubrequest(String remote_jid, ItemType subtype,
                                         int retrycount) {
        Presence subscribe = null;

        if (subtype.equals(ItemType.none)) {
            subscribe = new Presence(Presence.Type.subscribe);
            // first = true;
        } else if (subtype.equals(ItemType.from)) {
            subscribe = new Presence(Presence.Type.subscribe);
            // first = true;
        }
        if (subscribe == null)
            return true;
        subscribe.setTo(remote_jid);
        try {
            GlobalData.connection.sendPacket(subscribe);
            return true;
        } catch (Exception e) {
            Utils.printLog(e.toString());
            e.printStackTrace();
            return retrycount == 2;
        }
    }

    public static int msgCount = 0;
    public static boolean gcmFlag = false;
    public static ArrayList<String> phList = new ArrayList<String>();

    /*Amit Custom notification*/
    public static void showCustomNotification(Context ctx, String msg,
                                              String senderName, String from) {
        // msg="<message id=\"ytM2g-106\" to=\"+919333333333@email19.in\" from=\"+919910025912@email19.in/Smack_chat\" type=\"chat\"><body>x JG xgux</body><thread>5f43C3</thread><chattag xmlns=\"mysl:chattag:chat\" username=\"+919910025912\" userid=\"30\" msgtype=\"NORMAl\" chattime=\"1469695659592\" chatmessageid=\"30_1469695659592\"/><x xmlns=\"jabber:x:event\"><offline/><delivered/><displayed/></x></message>";
        // msg="<message id=\"fX8Gx-968\" to=\"+919717077554@email19.in\" from=\"+919810398913@email19.in/Smack_chat\" type=\"chat\"><body>jxdjdjd</body><thread>hq2CM2</thread><chattag xmlns=\"mysl:chattag:chat\" username=\"+919810398913\" userid=\"10016\" msgtype=\"NORMAl\" chattime=\"1470895771960\" chatmessageid=\"10016_1470895771960\"/><x xmlns=\"jabber:x:event\"><offline/><delivered/><displayed/></x></message>";
        boolean isNumberExisted = true;
        Notification.Builder builder = new Notification.Builder(ctx);
        builder.setAutoCancel(false);
        String msgTxt = "message received";
        String name = "";
        msgCount++;
        if (msgCount > 1) {
            msgTxt = "messages received";
        }
        // Bitmap bm = BitmapFactory.decodeResource(ctx.getResources(),
        // R.drawable.notification_icon);
        Intent notificationIntent = null;

        if (from.equalsIgnoreCase("gcm")) {
            // SharedPreferences prefs = ctx.getSharedPreferences("SenderList",
            // Context.MODE_PRIVATE);
            notificationIntent = new Intent(ctx, SplashScreen.class);

            try {
                JSONObject root = XML.toJSONObject(msg);
                JSONObject messageObj = root.getJSONObject("message");
                JSONObject bodyObj = root.getJSONObject("body");
                if (messageObj.has("from")) {
                    String fromMsg = messageObj.getString("from");
                    String phNumber = fromMsg.split("@")[0];
                    // Gson gson = new Gson();
                    // String json = prefs.getString("TAG", null);
                    // Type type = new TypeToken<ArrayList<String>>()
                    // {}.getType();
                    // ArrayList<String> arrayList = gson.fromJson(json, type);
                    // phList=arrayList;
                    if (phList != null) {
                        if (phList.size() == 0) {
                            phList.add(phNumber);
                            isNumberExisted = false;
                        } else {
                            for (int k = 0; k < phList.size(); k++) {
                                if ((phList.get(k).equalsIgnoreCase(phNumber))) {
                                    isNumberExisted = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (isNumberExisted && phList != null) {
                        phList.add(phNumber);
                    }

                    // Editor editor = prefs.edit();
                    // // Gson gson = new Gson();
                    // String json1 = gson.toJson(phList);
                    // editor.putString("TAG", json1);
                    // editor.commit();
                    fromMsg = fromMsg.split("/")[0];
                    if (!fromMsg.startsWith("+")) {
                        fromMsg = fromMsg.trim();
                        fromMsg = "+" + fromMsg;
                    }
                    if (GlobalData.dbHelper == null)
                        GlobalData.dbHelper = new DatabaseHelper(ctx, false);
                    // GlobalData.dbHelper.opendatabase();
                    Contactmodel contactmodel = null;
                    contactmodel = GlobalData.dbHelper.getUserDetails(fromMsg);
                    name = contactmodel.getName();
                    GlobalData.dbHelper.close();
                    if (name == null || name.equalsIgnoreCase("")) {
                        name = phNumber;
                    }

                    if (phList != null && phList.size() > 1) {
                        msgTxt = msgTxt + " from " + phList.size() + " chats";
                    } else {
                        msgTxt = msgTxt + " from " + name;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }

            gcmFlag = true;
        } else {
            notificationIntent = new Intent(ctx, InAppMessageActivity.class);
            String phNumber = from.split("@")[0];
            if (phList != null) {
                if (phList.size() == 0) {
                    phList.add(phNumber);
                    isNumberExisted = false;
                } else {
                    for (int k = 0; k < phList.size(); k++) {
                        if ((phList.get(k).equalsIgnoreCase(phNumber))) {
                            isNumberExisted = false;
                            break;
                        }
                    }
                }
            }
            if (isNumberExisted) {
                phList.add(phNumber);
            }
            name = senderName;
            if (phList.size() > 1) {
                msgTxt = msgTxt + " from " + phList.size() + " chats";
            } else {
                msgTxt = msgTxt + " from " + name;
            }

        }

        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

       /* builder.setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(ctx.getString(R.string.app_name))
                .setTicker("message received from " + name)
                // .setContentText(""+" : "+msg)
                .setContentText(msgCount + " " + msgTxt)
                .setContentIntent(pendingIntent);*/

        Notification mynotification = new Notification.Builder(ctx)
                .setContentTitle(ctx.getString(R.string.app_name))
                .setContentText("message received from " + name)
                .setSmallIcon(R.drawable.notification_icon)
                .setStyle(new Notification.BigTextStyle()
                        .bigText("message received from .............................................." + name))
                .build();

        NotificationManager notificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mynotification;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.flags |= Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;
        // Random random = new Random();
        // int count = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(0, notification);
    }

    public static Intent redirectActivity(Context ctx,String activityname)
    {
        Intent resultIntent=null;

        if(activityname.equals("home"))
        {
            resultIntent = new Intent(ctx, Home.class);
        }
        else   if(activityname.equals("chat"))
        {
            resultIntent = new Intent(ctx, InAppMessageActivity.class);
        }

        else   if(activityname.equals("contact"))
        {
            resultIntent = new Intent(ctx, ContactsActivity.class);
        }
        else   if(activityname.equals("datastorage"))
        {
            resultIntent = new Intent(ctx, DataStorage.class);
        }

        else   if(activityname.equals("sendsms"))
        {
            resultIntent = new Intent(ctx, SendSmsMail.class);
        }
        else   if(activityname.equals("schedule"))
        {
            resultIntent = new Intent(ctx, Schedule.class);
        }
        else   if(activityname.equals("smssettings"))
        {
            resultIntent = new Intent(ctx, SettingSmsMail.class);
        }

        else   if(activityname.equals("transaction"))
        {
            resultIntent = new Intent(ctx, Transaction.class);
        }

        else   if(activityname.equals("report"))
        {
            resultIntent = new Intent(ctx, SecondSmsMailReport.class);
            resultIntent.putExtra("dayreport", "today");
            resultIntent.putExtra("DATE_SMS", "");
        }

     /*   case 1:
        PosHome
        case 2:      Categories
        case 3:      Brands
        case 4:      Products
        case 5:      Customers
        case 6:      Orders
        case 7:
        Payments
        case 8:      Credits
        case 9:      Invoices
        case 10:      Taxes
        case 11:      Other Charges
        case 12:      Settings
        case 13:      Purchases
        case 14:      Outstandings
        case 15:      Purchase Payments
        case 16:      Vendors
        case 17:      Inventory
*/
        else   if(activityname.equals("PosHome"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 1);
            resultIntent.putExtra("TabPos", 1);
        }

        else   if(activityname.equals("Categories"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 2);
            resultIntent.putExtra("TabPos", 2);
        }
        else   if(activityname.equals("Brands"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 3);
            resultIntent.putExtra("TabPos", 2);
        }
        else   if(activityname.equals("Products"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 4);
            resultIntent.putExtra("TabPos", 2);
        }
        else   if(activityname.equals("Customers"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 5);
        }
        else   if(activityname.equals("Orders"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 6);
            resultIntent.putExtra("TabPos", 3);
        }
        else   if(activityname.equals("Payments"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 7);
            resultIntent.putExtra("TabPos", 3);
        }
        else   if(activityname.equals("Credits"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 8);
            resultIntent.putExtra("TabPos", 3);
        }
        else   if(activityname.equals("Invoices"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 9);
            resultIntent.putExtra("TabPos", 3);
        }
        else   if(activityname.equals("Taxes"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 10);
        }
        else   if(activityname.equals("Other Charges"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 11);
        }
        else   if(activityname.equals("Settings"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 12);
        }
        else   if(activityname.equals("Purchases"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 13);
            resultIntent.putExtra("TabPos", 5);
        }

        else   if(activityname.equals("Outstandings"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 14);
            resultIntent.putExtra("TabPos", 5);
        }

        else   if(activityname.equals("Purchase Payments"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 15);
            resultIntent.putExtra("TabPos", 5);
        }

        else   if(activityname.equals("Vendors"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 16);
        }

        else   if(activityname.equals("Inventory"))
        {
            resultIntent = new Intent(ctx, POSHomeTabbedActivity.class);
            resultIntent.putExtra("FragmentPos", 17);
            resultIntent.putExtra("TabPos", 2);
        }

        else
        {
            resultIntent = new Intent(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(activityname));
        }

        return resultIntent;
    }

    public static Notification setBigPictureStyleNotification(Context ctx, String msg,
                                                        String imageurl, String page) {
       // String imageurl="https://www.w3schools.com/html/pic_mountain.jpg";
        Bitmap remote_picture = getBitmapFromURL(ctx,imageurl);
        // Create the style object with BigPictureStyle subclass.
        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle(ctx.getString(R.string.app_name));
        notiStyle.setSummaryText(msg);
        //notiStyle.setBigContentTitle("The path of..." /* long text goes here */ )
        notiStyle.bigPicture(remote_picture);


        // Creates an explicit intent for an ResultActivity to receive.
        //Intent resultIntent = new Intent(this, Browser.class);

       // Intent resultIntent = new Intent(ctx, Home.class);

        Intent resultIntent =redirectActivity(ctx,page);
        //resultIntent.setAction(Long.toString(System.currentTimeMillis()));

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(Home.class);
        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(true)
                .setLargeIcon(remote_picture)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(ctx.getString(R.string.app_name))
                .setContentText(msg)
                .setStyle(notiStyle).build();
    }

    public static Bitmap getBitmapFromURL(Context context,String src) {

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.demo);
        if(src.isEmpty() || src.equals("") || src.equals("null"))
        {
            return icon;
        }
        else
        {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                if(myBitmap!=null)  return myBitmap;
                else  return icon;
            } catch (IOException e) {
                e.printStackTrace();
                return icon;
            }
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am
                    .getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static String doubleToString(Double d) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);
        return df.format(d);
    }

    public static void imagePopup(Context ctx, Drawable dimage) {
        Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_dialog);
        ImageView profile_dialog = (ImageView) dialog.findViewById(R.id.profile_dialog);
        profile_dialog.setImageDrawable(dimage);
        dialog.show();
    }


}
/*
*  @SuppressWarnings("unchecked")
    public static void addSms19ContactsInDb(
            ArrayList<ContactDetailsNew> contactDetailsNews) {

        // ContactDetailsNew Model = (ContactDetailsNew) ;
        // new SMS19AddContactAsyncTask().execute(contactDetailsNews);

        SMS19AddContactAsyncTask addContactAsyncTask2 = new SMS19AddContactAsyncTask();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            addContactAsyncTask2.executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR, contactDetailsNews);
        } else {
            addContactAsyncTask2.execute(contactDetailsNews);
        }

    }

     public static String getProfilePathForGroupImagwDownload(String filename) {
        String filepath = "";
        filepath = "/Kitever" + filename + ".jpg";
        return filepath;
    }

    public static Bitmap decodeResource(Resources res, int resId, int dstWidth,
                                        int dstHeight, ScalingLogic scalingLogic) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth,
                options.outHeight, dstWidth, dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeResource(res, resId,
                options);

        return unscaledBitmap;
    }

      public static void mCreateAndSaveFile(String mJsonResponse, String post) {
        try {
            String path = Environment.getExternalStorageDirectory()
                    + "/Kitever/databases/";
            FileWriter file = new FileWriter(path + post + "SMSReasas.json");
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mCreateAndSaveFilePost(String mJsonResponse) {
        try {
            String path = Environment.getExternalStorageDirectory()
                    + "/Kitever/databases/";
            FileWriter file = new FileWriter(path + "SMSResPosLast.json");
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 public static String getLastseenTime(String datetime) {
        String dayhours = "";
        long mils = 0;
        boolean set = false;
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date d = format.parse(datetime);

            mils = d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: this is the value in ms
        long currentms = System.currentTimeMillis();
        long ms = currentms - mils;
        StringBuffer text = new StringBuffer("");
        if (ms > YEAR) {
            int y = (int) (ms / YEAR);
            if (y != 0) {
                if (y == 1) {
                    text.append(ms / YEAR).append(" year ");
                } else {
                    text.append(ms / YEAR).append(" years ");

                }
                set = true;
            }
            ms %= YEAR;
        }
        if (ms > MONTH) {
            int m = (int) (ms / MONTH);
            if (!set) {
                if (m != 0) {
                    if (m == 1) {
                        text.append(ms / MONTH).append(" month ");
                    } else {
                        text.append(ms / MONTH).append(" months ");

                    }
                    set = true;
                }
            }
            ms %= MONTH;
        }
        if (ms > DAY) {
            int d = (int) (ms / DAY);
            if (!set) {
                if (d != 0) {
                    if (d == 1) {
                        text.append(ms / DAY).append(" day ");

                    } else {
                        text.append(ms / DAY).append(" days ");

                    }
                    set = true;
                }
            }
            ms %= DAY;
        }

        if (ms > HOUR) {
            int d = (int) (ms / HOUR);
            if (!set) {
                if (d != 0) {
                    if (d == 1) {
                        text.append(ms / HOUR).append(" hour ");
                    } else {
                        text.append(ms / HOUR).append(" hours ");

                    }
                    set = true;
                }
            }
            ms %= HOUR;

        }
        if (ms > MINUTE) {
            int d = (int) (ms / MINUTE);
            if (!set) {
                if (d != 0) {
                    if (d == 1) {
                        text.append(ms / MINUTE).append(" min ");
                    } else {
                        text.append(ms / MINUTE).append(" mins ");

                    }
                    set = true;
                }
            }
            ms %= MINUTE;

        }
        if (ms > SECOND) {
            int d = (int) (ms / SECOND);
            if (!set) {
                if (d != 0) {
                    if (d == 1) {
                        text.append(ms / SECOND).append(" sec ");
                    } else {
                        text.append(ms / SECOND).append(" secs ");

                    }
                    set = true;
                }
            }
            ms %= SECOND;

        }
        if (!set) {

            text.append("0").append(" sec ");
            set = true;
        }

        // system.out.println(text.toString());
        dayhours = text.toString();
        return dayhours;

    }

    public static String getDayhours(String datetime) {
        String dayhours = "";
        long mils = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");
            Date d = format.parse(datetime);

            mils = d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: this is the value in ms
        long currentms = System.currentTimeMillis();
        long ms = currentms - mils;
        StringBuffer text = new StringBuffer("");
        if (ms > DAY) {
            text.append(ms / DAY).append(" days ");
            ms %= DAY;
        }
        if (ms > HOUR) {
            text.append(ms / HOUR).append(" hours ");
            ms %= HOUR;
        }
        // if (ms > MINUTE) {
        // text.append(ms / MINUTE).append(" minutes ");
        // ms %= MINUTE;
        // }
        // if (ms > SECOND) {
        // text.append(ms / SECOND).append(" seconds ");
        // ms %= SECOND;
        // }
        // text.append(ms + " ms");
        // system.out.println(text.toString());
        dayhours = text.toString();
        return dayhours;

    }

    public static Bitmap getHexagonShape(Bitmap scaleBitmapImage) {
        // TODO Auto-generated method stub
        int targetWidth = 100;
        int targetHeight = 100;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);

        Path path = new Path();
        float stdW = 100;
        float stdH = 100;
        float w3 = stdW / 2;
        float h2 = stdH / 2;
        path.moveTo(0, (float) (h2 * Math.sqrt(3) / 2));
        path.rLineTo(w3 / 2, -(float) (h2 * Math.sqrt(3) / 2));
        path.rLineTo(w3, 0);
        path.rLineTo(w3 / 2, (float) (h2 * Math.sqrt(3) / 2));
        path.rLineTo(-w3 / 2, (float) (h2 * Math.sqrt(3) / 2));
        path.rLineTo(-w3, 0);
        path.rLineTo(-w3 / 2, -(float) (h2 * Math.sqrt(3) / 2));

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
                sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
                targetHeight), null);
        return targetBitmap;
    }

     public static Bitmap createScaledBitmap(Bitmap unscaledBitmap,
                                            int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(),
                unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(),
                unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(),
                dstRect.height(), Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(
                Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

     public static Rect calculateSrcRect(int srcWidth, int srcHeight,
                                        int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int) (srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth,
                        srcHeight);
            } else {
                final int srcRectHeight = (int) (srcWidth / dstAspect);
                final int scrRectTop = (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop
                        + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    public static Rect calculateDstRect(int srcWidth, int srcHeight,
                                        int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

        public static void setdosissemiboldFont(TextView tv, Context ctx) {

        Typeface DOSIS_S_BOLD = Typeface.createFromAsset(ctx.getAssets(),
                "Dosis-SemiBold.otf");
        tv.setTypeface(DOSIS_S_BOLD);
    }

     public static void setAlbaFont(TextView tv, Context ctx) {

        Typeface ALBA = Typeface.createFromAsset(ctx.getAssets(), "ALBA.TTF");
        tv.setTypeface(ALBA);
    }

      public static void setMymediatoChat(final Activity act, final String path,
                                        final String status, final ImageView view, final String filetype,
                                        final ProgressBar progress) {
        new Thread(new Runnable() {
            public void run() {
                if (new File(path).exists()) {
                    act.runOnUiThread(new Runnable() {
                        public void run() {
                            if (status.equalsIgnoreCase("S")) {
                                progress.setVisibility(View.GONE);
                            }
                            if (filetype.equals(GlobalData.Imagefile)
                                    || filetype.equals(GlobalData.Locationfile)) {
                                view.setImageBitmap(BitmapFactory
                                        .decodeFile(path));
                            } else if (filetype.equals(GlobalData.Audiofile)) {

                            } else {

                                Bitmap thumbnail = ThumbnailUtils
                                        .createVideoThumbnail(
                                                path,
                                                MediaStore.Images.Thumbnails.MINI_KIND);
                                if (thumbnail != null) {
                                    view.setImageBitmap(thumbnail);
                                }
                            }
                        }
                    });

                }
            }
        }).start();

    }

    public static boolean downloadBitmapandSave(String url, String picpath) {
        try {

            InputStream inputStream = new URL(url).openConnection()
                    .getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            if (bitmap != null) {
                FileOutputStream f = new FileOutputStream(picpath);
                int quality = 100;

                bitmap.compress(CompressFormat.JPEG, quality, f);
                f.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

    }

     public static ArrayList<Tonesmodel> getlistRingtones(Context ctx) {
        ArrayList<Tonesmodel> toneslist = new ArrayList<Tonesmodel>();
        RingtoneManager manager = new RingtoneManager(ctx);
        manager.setType(RingtoneManager.TYPE_NOTIFICATION);
        Cursor cursor = manager.getCursor();
        Uri Duri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String Dtitle = "Default";
        Tonesmodel Dmodel = new Tonesmodel();
        Dmodel.setTitle(Dtitle);
        Dmodel.setUri(Duri.toString());
        toneslist.add(Dmodel);
        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            int ringtoneID = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri finalSuccessfulUri = Uri.withAppendedPath(Uri.parse(uri), ""
                    + ringtoneID);
            Tonesmodel model = new Tonesmodel();
            model.setTitle(title);
            model.setUri(finalSuccessfulUri.toString());
            toneslist.add(model);
            // Do something with the title and the URI of ringtone
        }

        return toneslist;
    }

     public static boolean checkContactisadded(Context ctx, String number) {
        ContentResolver cr = ctx.getContentResolver();
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        int count = 0;
        Cursor cursor = cr.query(uri,
                new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return false;
        } else {
            count = cursor.getCount();
            cursor.close();
            return count > 0;

        }
    }

    @SuppressWarnings("deprecation")
    public static void showSdcardAlertDialog(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(context.getResources()
                .getString(R.string.app_name));

        // Setting Dialog Message
        alertDialog.setMessage(/*
								 * context.getResources().getString(
								 * R.string.sdcardmsg)
								 "");

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.ic_launcher);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        }
        });

        // Showing Alert Message
        alertDialog.show();
        }

public static long getlastseen(String user_id, int retrycount) {
        long lastseentime = -1;
        try {
        LastActivity lac = LastActivityManager.getLastActivity(
        GlobalData.connection, user_id);
        lastseentime = lac.lastActivity;
        if (lastseentime == -1) {
        lastseentime = 10;
        }
        printLog("lastseentime==" + lastseentime);
        } catch (XMPPException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        printLog("getlastseen excption");
        if (retrycount == 2) {
        lastseentime = 10;
        }
        }
        return lastseentime;
        }

public static String parsedlastseen(String timemillisec) {
        String lastseen = "";
        if (timemillisec != null && timemillisec.trim().length() != 0) {
        long currentmili = System.currentTimeMillis();
        long msgmili = Long.parseLong(timemillisec);
        long diffmili = currentmili - msgmili;
        long hours = TimeUnit.MILLISECONDS.toHours(diffmili);
        long min = TimeUnit.MILLISECONDS.toMinutes(diffmili);
        long sec = TimeUnit.MILLISECONDS.toSeconds(diffmili);
        long day = TimeUnit.MILLISECONDS.toDays(diffmili);
        if (day != 0) {
        lastseen = String.valueOf(day) + " days ago";
        } else if (hours != 0) {
        lastseen = String.valueOf(hours) + " hours ago";
        } else if (min != 0) {
        lastseen = String.valueOf(min) + " mins ago";
        } else if (sec != 0) {
        lastseen = String.valueOf(sec) + " secs ago";
        } else {
        lastseen = "just now";
        }

        }
        return lastseen;
        }

public static void RegisterToServer() {
        ConnectionConfiguration connConfig = new ConnectionConfiguration(
        GlobalData.HOST, GlobalData.PORT);
        connConfig.setSASLAuthenticationEnabled(false);
        // connConfig.setCompressionEnabled(true);
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

        // ConnectionConfiguration connConfig = new ConnectionConfiguration(
        // GlobalData.HOST, GlobalData.PORT);
        // connConfig.setSASLAuthenticationEnabled(false);

        GlobalData.connection = new XMPPConnection(connConfig);
        Utils.setUpConnectionProperties();

        }

        /*
	 * public static void saveCountryList(Context context,
	 * ArrayList<Countrymodel> countryListBeans) { try {
	 * android.content.SharedPreferences mPrefs = PreferenceManager
	 * .getDefaultSharedPreferences(context);
	 * android.content.SharedPreferences.Editor prefsEditor = mPrefs .edit();
	 * Gson gson = new Gson(); String json = gson.toJson(countryListBeans);
	 * prefsEditor.putString("CountryList", json); prefsEditor.commit(); } catch
	 * (Exception e) { e.printStackTrace(); }
	 *
	 * }
	 *
	 * public static ArrayList<Countrymodel> getCountryList(Context context) {
	 *
	 * try { android.content.SharedPreferences mPrefs = PreferenceManager
	 * .getDefaultSharedPreferences(context);
	 * android.content.SharedPreferences.Editor prefsEditor = mPrefs .edit();
	 * Gson gson = new Gson(); String json = mPrefs.getString("CountryList",
	 * null); Type typ1ew2 = new TypeToken<ArrayList<Countrymodel>>() {
	 *
	 * }.getType(); ArrayList<Countrymodel> obj = gson.fromJson(json, typ1ew2);
	 * prefsEditor.commit(); return obj; } catch (Exception e) {
	 *
	 * e.printStackTrace(); }
	 *
	 * return null; }
	 */

	/*
	 * public static ArrayList<Countrymodel> getCountryList(final Context
	 * context) {
	 *
	 * final ArrayList<Countrymodel> arrayList= new ArrayList<>();
	 *
	 * if(context!=null){ try {
	 *
	 * final String strJson = new String(readAsset(context.getAssets(),
	 * "countries.json"));
	 *
	 * JSONObject resObj=null;
	 *
	 * if (strJson != null && strJson.trim().length() != 0) { try { resObj = new
	 * JSONObject(strJson);
	 *
	 * JSONArray jsonArray = resObj.getJSONArray("CountryList");
	 *
	 * if(jsonArray!=null){ for(int i=0;i<jsonArray.length();i++){ Countrymodel
	 * bean=new Countrymodel(); JSONObject
	 * jsonObject=jsonArray.getJSONObject(i); if(jsonObject.has("country_id")){
	 * bean.setCountry_id(jsonObject.getString("country_id")); }
	 * if(jsonObject.has("countrycode")){
	 * bean.setCountrycode(jsonObject.getString("countrycode")); }
	 * if(jsonObject.has("country_name")){
	 * bean.setCountry_name(jsonObject.getString("country_name")); }
	 * arrayList.add(bean); } } } catch (JSONException e) { e.printStackTrace();
	 * }
	 *
	 * } }
	 *
	 * catch (Exception e) {
	 *
	 * e.printStackTrace(); }
	 *
	 *
	 * return arrayList;
	 *
	 *
	 * }else{
	 *
	 * return null;
	 *
	 * }
	 *
	 * }
	 *
	 *  public static void getXmppGroupList(String userId, String user_jid,
                                        Context ctx) {

        GetXmppGroupListAsyncTask broadCastGroupList = new GetXmppGroupListAsyncTask(
                userId, user_jid, ctx);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            broadCastGroupList
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            broadCastGroupList.execute();
        }

    }

    public static void blockUnblock(String from, String jid,
                                    XMPPConnection connection) {

        if (!GlobalData.dbHelper.isContactBlock(jid)) {
            org.jivesoftware.smack.packet.Presence presence = new org.jivesoftware.smack.packet.Presence(
                    org.jivesoftware.smack.packet.Presence.Type.unsubscribe);
            presence.setTo(jid);
            presence.setFrom(from);
            connection.sendPacket(presence);
        } else {
            org.jivesoftware.smack.packet.Presence presence = new org.jivesoftware.smack.packet.Presence(
                    org.jivesoftware.smack.packet.Presence.Type.subscribe);
            presence.setTo(jid);
            connection.sendPacket(presence);
        }
    }

    public static String getCountryCodeNumber(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            // Gson gson = new Gson();
            String json = mPrefs.getString("CountryCodeNumber", "");
            // String obj = gson.fromJson(json, String.class);
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

      public static String removePlus(String number) {

        try {
            number = number.replace("+", "");
            return number;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return number;

    }

    public static String removeCountryCode_2(String number, Context ctx) {

        String postContactNo = "";
        try {

            postContactNo = number;

            postContactNo = postContactNo.replace(getCountryCode(ctx), "");
            if (postContactNo.startsWith("0")) {
                postContactNo.substring(1, postContactNo.length());
            }

            postContactNo.replace(" ", "");
            postContactNo.replace("@", "");
            postContactNo.replace(".", "");
            return postContactNo;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return postContactNo;

    }

    public static String Add91(String number, Context ctx) {

        String postContactNo = "";
        try {

            postContactNo = number;

            postContactNo = postContactNo.replace(getCountryCode(ctx), "");
            postContactNo = postContactNo.replace("+", "");
            if (postContactNo.startsWith("0")) {
                postContactNo.substring(1, postContactNo.length());
            }

            postContactNo = getCountryCode(ctx) + postContactNo;
            postContactNo = postContactNo.replace(" ", "");
            return postContactNo;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return postContactNo;

    }
//
//	public static String getEncodedString(String str) {
//		StringBody ret = null;
//		String ret2 = null;
//		try {
//			Charset chars = Charset.forName("UTF-8");
//			ret2 = URLEncoder.encode(str, "UTF-8");
//			ret = new StringBody(str.toString(), chars);
//			// ret=eMail.toString();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		ret2 = ret2.replace("+", "%20");
//		return ret2;
//	}

 public static String getJSONOfDashboardAndAddToDatabase(String jsonID) {
        JSONObject resObj;
        String response = /* Rest.getInstance().sendJSONRequest(jsonID) "";
        Utils.printLog("DASHBOARD RESPONSE====" + response);
        if (response != null && response.trim().length() != 0) {
        try {
        resObj = new JSONObject(response);
        if (resObj.getString("code").equals("0")
        && resObj.getString("error").equals("false")) {
        JSONObject dataObject = resObj.getJSONObject("data");
        String json = dataObject.getString("json");
        if (!json.equals("")) {
        Utils.printLog(json);
        // GlobalData.dbHelper.updateJsonMessage(jsonID, json);
        // // comment mm
        return json;

        // if (ChatFrag.chathistorylist != null) {
        // for (int i = 0; i < ChatFrag.chathistorylist.size();
        // i++) {
        // if (ChatFrag.chathistorylist.get(i)
        // .getDashboardID() != null)
        // if (ChatFrag.chathistorylist.get(i)
        // .getDashboardID().equals(jsonID)) {
        // ChatFrag.chathistorylist.get(i)
        // .setJSON(json);
        // ChatFrag.ChatupdateHandler
        // .sendEmptyMessage(6);
        // return json;
        // }
        // }
        // }
        } else {
        return null;
        }
        }
        } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
        }
        return null;

        }

public static void timeAccordingToserver() {
        // long timestamp = cursor.getLong(columnIndex);

        long timestamp = Long.valueOf(System.currentTimeMillis());

        // Log.d("Server time: ", timestamp);

		/* log the device timezone
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        // Log.d("Time zone: ", tz.getDisplayName());

		 log the system time
         Log.d("System time: ", System.currentTimeMillis());

        CharSequence relTime = DateUtils.getRelativeTimeSpanString(
        timestamp * 1000, System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS);

        }

         public static ArrayList<Contactmodel> addUnRegisterUsers(
            ArrayList<Contactmodel> contactlistRegistered,
            ArrayList<Contactmodel> contactlistUnRegistered) {

        try {
            ArrayList<Contactmodel> arrayList = new ArrayList<Contactmodel>();
            arrayList = contactlistRegistered;

            if (contactlistRegistered.size() > 0) {
                if (contactlistUnRegistered.size() > 0) {

                    for (int i = 0; i < contactlistUnRegistered.size(); i++) {
                        for (int j = 0; j < contactlistRegistered.size(); j++) {
                            String remoteIdStr = contactlistRegistered.get(j)
                                    .getRemote_jid().split("@")[0];
                            String remoteId2Str = contactlistUnRegistered
                                    .get(i).getNumber().split("@")[0];

                            String remoteId = remoteIdStr.substring(
                                    remoteIdStr.length() - 4,
                                    remoteIdStr.length());
                            String remoteId2 = remoteId2Str.substring(
                                    remoteId2Str.length() - 4,
                                    remoteId2Str.length());

                            if (!(remoteId.equalsIgnoreCase(remoteId2))) {
                                arrayList.add(contactlistUnRegistered.get(i));
                                break;
                            }
                        }

                    }
                }

            }

            return arrayList;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return null;

    }

     public static MultiUserChat joingroup(String groupid, long totalsec,
                                          int retrycount, String remote_jid) {

        MultiUserChat mucChat = null;

        try {
            mucChat = new MultiUserChat(GlobalData.connection, groupid);
            if (mucChat != null) {
                DiscussionHistory history = new DiscussionHistory();
                history.setSeconds((int) totalsec);
                if (!GlobalData.dbHelper.groupUserIsBlock(groupid)) {
                    mucChat.join(remote_jid, null, history, 5000);
                    Utils.printLog("Join group after " + totalsec + " , "
                            + groupid + " successfully.");
                    // mucChat.addParticipantStatusListener(new
                    // Groupchatparticipentchange());
                }

            }
            return mucChat;

        } catch (XMPPException e) {
            Utils.printLog("Join group " + groupid + " Excption.");
            e.printStackTrace();
            if (retrycount == 2) {
                return mucChat;
            } else {
                return null;
            }
        }
    }



    static class SMS19AddContactAsyncTask extends
            AsyncTask<ArrayList<ContactDetailsNew>, Void, Void> {
        String response = "";
        JSONObject resObj;

        @Override
        protected Void doInBackground(ArrayList<ContactDetailsNew>... params) {

            try {
                ArrayList<ContactDetailsNew> allContact = params[0];

                GlobalData.dbHelper.addSms19Contact(allContact);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

    }

    public static String getExpiryTimeValueMail(Context context) {

        try {
            android.content.SharedPreferences mPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            android.content.SharedPreferences.Editor prefsEditor = mPrefs
                    .edit();
            // Gson gson = new Gson();
            String json = mPrefs.getString("ExpiryTimeValueMail", "");
            // String obj = gson.fromJson(json, String.class);
            prefsEditor.commit();
            return json;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
	 */