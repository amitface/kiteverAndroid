package sms19.listview.newproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kitever.android.R;
import com.kitever.app.context.AlertDialogManager;
import com.kitever.app.context.BaseApplicationContext;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.PermissionClass;
import com.kitever.contacts.ContactsActivity;
import com.kitever.pos.fragment.POSHomeTabbedActivity;
import com.kitever.sendsms.SendSmsMail;

import org.apache.http.util.EncodingUtils;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import sms19.inapp.msg.BroadCastGroupSecond;
import sms19.inapp.msg.ConstantFlag;
import sms19.inapp.msg.ContactFragment;
import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.adapter.SingleChatRoomAdapter;
import sms19.inapp.msg.asynctask.GetContactListAsyncTask;
import sms19.inapp.msg.asynctask.GetXmppGroupListAsyncTask;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.ContactUtil;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.database.DatabaseHelper;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.MessageEventnotificationListener.NotifyAfterMessageDeliverRead;
import sms19.inapp.msg.rest.Rest;
import sms19.inapp.msg.services.ConnectionReceiver;
import sms19.inapp.msg.services.RegisterConnectionService;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.MerchatStorePackage.CartOrderList;
import sms19.listview.newproject.MerchatStorePackage.MerchantCartOrderList;
import sms19.listview.newproject.MerchatStorePackage.UserCartActivity;
import sms19.listview.newproject.model.GetFTPCre;
import sms19.listview.newproject.model.getTopBalance;
import sms19.listview.webservice.ThreadCounterInbox.ServiceHitListenerwhatsup;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;

import static com.kitever.app.context.CustomStyle.Style1;
import static com.kitever.app.context.CustomStyle.Style2;
import static com.kitever.app.context.CustomStyle.Style3;
import static com.kitever.app.context.CustomStyle.defualtStyle;
import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSetting;


public class Home extends AppCompatActivity implements ServiceConnection,
        ServiceHitListener, ServiceHitListenerwhatsup,
        NotifyAfterMessageDeliverRead, OnClickListener, PopupMenu.OnMenuItemClickListener {
    private String Mobile = "", InAppUserlogin = "", InAppPassword = "";
    private static String UserId = "";
    public static final int NOTIFICATION_ID = 2;
    private int Updatevalue = 0;
    public static NotificationManager mNotificationMan;
    // private Button more;
    private TextView txtDate, balanceIcon, txtTopup;
    private Typeface moonicon;
    public static Activity HomeStatus;
    private DataBaseDetails dbObject = new DataBaseDetails(this);
    private Handler mHandler = new Handler();
    public static int notifyonlyone = 0;
    public static int countonlyone = 0;
    public static final int NOTIFICATION_ID_ID = 1;
    public static NotificationManager mNotificationManager;

    private String path = "", imagetoupload2 = "", filename = "",
            filetype = "", urlpath = "", sendurlpath = "";
    public static Boolean notifyna = true;

    private String mediagpnm = "", recipientid = "", medianumber = "",
            dbmessagein = "", dbnumberin = "", dbgrupname = "",
            dbrecipientId = "", dbtime = "", dbcheckuncheck = "";
    private String dbpageName = "", dbdiff = "", dbmyid = "",
            dbsenderuserid = "", dbdate = "", dbIurl = "", dbsendingtype = "",
            dbSstatus = "";
    private int dbOutID, dbinboxOut, dbsendStatusRead;
    private boolean flag = false;

    // we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    private int counervariable = 1;

    private Context HomePart;
    private boolean stop = true;

    /***********************************************/
    private LinearLayout mFrameLayoout, mHomecontainer, mBackButton;

    private LinearLayout layoutMessage, layoutChats, layoutContacts,
            layoutTemplates, layoutMicro, layoutUnlock, layoutReports, layoutPOS;
    private TextView tvMessage, tvChats, tvContacts, tvTemplates, tvStorage, tvMicro,
            tvReports, tvPOS;
    private ImageView mActionBarImage;
    private TextView mUserNameTitle, mUserStatusTitle;
    private CoordinatorLayout coordinatorLayout;
    private WebView homewebView;

    /**************
     * New for xmpp
     *********/
    public static SharedPreferences chatPrefs;
    public static Rest rest;
    public static String user_jid = "";

    public static boolean loginToserver = false, fromPrevconnection = false, groupjoiningrunning = false;

    public static String currentFragment = "";
    public static Activity runonuithread;
    public static Home afterThis;

    public static Handler Handler;

    public static Contactmodel mydetail = null;

    private TextView mChatTabBtn;
    private TextView mContactTabBtn;

    public static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private Timer timer;
    // private MyTimerTask myTimerTask;
    private ChatTimer chatTimer;
    public static String loginUserSmack = "";
    public static ConnectionReceiver connectionReceiver;
    public static boolean mIsBound = false;
    public static RegisterConnectionService connectionServiceObject;
    public static Home homeServiceConnection;
    private PermissionClass permissionClass;
    public static Handler logoutHandler = null;
    private String USERID = "", merchant_Url = "", merchantName = "";
    private boolean isFromShare;
    private String shareDataType, sharePath;
    private static boolean isSetHomeUI = false;
    private ProgressBar homeprogressbar;
    private TextView company_name;
    private ImageView company_image;
    private String Home_Url = "http://kitever.com/";
    private String AddOn = "";
    private ImageView docimage;

    private TextView bottom_leftt_arrow, bottom_right_arrow, bottom_home_contacts, bottom_home_chats, bottom_home_msg, bottom_home_tools, bottom_home_storage, bottom_home_crm, bottom_home_micro;
    private HorizontalScrollView scrollView;
    private RelativeLayout footerLayout;
    private SharedPreferences themePrefs;
    private Tracker mTracker;
    String themeid = "0";
    private Dialog ThemeDilaog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SelectTheme();
        connectionReceiver = new ConnectionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kitever.connection.listener");
        registerReceiver(connectionReceiver, filter);
        try {
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();
            isFromShare = getIntent().getBooleanExtra("from_share", false);
            shareDataType = getIntent().getStringExtra("data_type");
            sharePath = getIntent().getStringExtra("share_path");
            if (Intent.ACTION_SEND.equals(action) && type != null
                    && !isFromShare) {
                if (Utils.isDeviceOnline(Home.this)) {
                    try {
                        doBindService();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (type.startsWith("video/")) {
                    shareDataType = "V";
                } else if (type.startsWith("image/")) {
                    shareDataType = "I";
                }
                Uri uri = null;
                uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                sharePath = getRealPathFromURI(this, uri);
                isFromShare = true;
            }
            getXmppGroupList();

            // else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type !=
            // null && !isFromShare) {
            // isFromShare = getIntent().getBooleanExtra("from_share", false);
            // }
        } catch (Exception e) {
            // TODO: handle exception
        }

        /*Amit this goes to single chat room*/
        if (isFromShare) {
            chatPrefs = getSharedPreferences("chatPrefs", MODE_PRIVATE);
            SingleChatRoomAdapter.isForwardData = true;
            SingleChatRoomAdapter.typeOfData = shareDataType;
            SingleChatRoomAdapter.pathToForwardData = sharePath;
            // SingleChatRoomAdapter.msgToSend=textToShare;
            isFromShare = false;
            Intent intent = new Intent(getApplicationContext(),
                    InAppMessageActivity.class);
            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // startActivity(intent);
        } else {
            isSetHomeUI = true;
            setHomeUI();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        BaseApplicationContext application = (BaseApplicationContext) getApplication();
        Log.i("analytics", "analytics code");
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(getString(R.string.app_name)+"- Home Page");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (!isSetHomeUI) {
            isSetHomeUI = true;
            setHomeUI();
        }
        /*
         * ContextWrapper cw = new ContextWrapper(getApplicationContext()); File
		 * directory = cw.getDir("KiteverImageDir", Context.MODE_PRIVATE); File
		 * f = new File(directory.getPath(), "merchantprofile.jpg"); if
		 * (f.exists()) {
		 * imageViewSplash.setImageDrawable(Drawable.createFromPath
		 * (f.getPath())); }
		 */

        loadTheme();
        SharedPreferences prfs = getSharedPreferences("profileData",
                Context.MODE_PRIVATE);
        merchantName = prfs.getString("MerchantName", null);
        merchant_Url = prfs.getString("Merchant_Url", null);
        refreshCreditBalance();
    }

    private void SelectTheme() {
        themePrefs = getSharedPreferences("themePrefs", MODE_PRIVATE);
        if (themePrefs.contains("themeid"))
            themeid = themePrefs.getString("themeid", "0");
        if (themeid.equalsIgnoreCase("0")) defualtStyle(this);
        else if (themeid.equalsIgnoreCase("1")) Style1(this);
        else if (themeid.equalsIgnoreCase("2")) Style2(this);
        else if (themeid.equalsIgnoreCase("3")) Style3(this);
        else defualtStyle(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectionReceiver);
        /*
         * if (timer != null) { timer.cancel(); timer = null; }
		 */

		/*
         * if (deleteContactAsyncTask != null) {
		 * deleteContactAsyncTask.cancel(true); deleteContactAsyncTask = null; }
		 * if (addContactAsyncTask != null) { addContactAsyncTask.cancel(true);
		 * addContactAsyncTask = null; }
		 */

        // saveGroupLeaveTm();

        if (connectionServiceObject != null) {
            // stopService();
            if (GlobalData.connection != null) {
                if (GlobalData.connection.isConnected()) {
                    try {
                        Presence presence = new Presence(
                                Presence.Type.unavailable);
                        GlobalData.connection.sendPacket(presence);
                        // GlobalData.connection.disconnect();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            stopService();
        }
//        System.gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_with_merchant_site, menu);
        /*if (merchant_Url != null && !merchant_Url.equalsIgnoreCase("")) {
            getMenuInflater().inflate(R.menu.home_with_merchant_site, menu);
        } else {
            getMenuInflater().inflate(R.menu.home, menu);
        }*/
        return true;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            backPress();
            return true;
        }


        if (id == R.id.editProfile) {
            //Intent i = new Intent(Home.this, EditProfile.class);
            Intent i = new Intent(Home.this, EditProfilePage.class);
            startAcitivityWithEffect(i);
            return true;
        }

        if (id == R.id.support) {
            //Intent i = new Intent(Home.this, EditProfile.class);
            Intent i = new Intent(Home.this, ContactUs.class);
            startAcitivityWithEffect(i);
            return true;
        }

		/*if (id == R.id.transaction) {
            Intent i = new Intent(Home.this, Transaction.class);
			startAcitivityWithEffect(i);
			return true;
		}

		if (id == R.id.schedulemsgr) {
			// Intent i = new Intent(Home.this, ScheduleList.class);
			Intent i = new Intent(Home.this, Schedule.class);
			startAcitivityWithEffect(i);
			return true;
		}*/
        /*if (id == R.id.menu_merchant_site) {
            Intent intent = new Intent(Home.this, MerchantSiteActivity.class);
            intent.putExtra("url", merchant_Url);
            startAcitivityWithEffect(intent);
            return true;
        }*/

        if (id == R.id.my_orders) {
            Intent intent = new Intent(Home.this, CartOrderList.class);
            startAcitivityWithEffect(intent);
            return true;
        }

        if (id == R.id.merchnat_orders) {
            Intent intent = new Intent(Home.this,MerchantCartOrderList.class);
            startAcitivityWithEffect(intent);
            return true;
        }

        if (id == R.id.my_cart) {
            Intent intent = new Intent(Home.this, UserCartActivity.class);
            intent.putExtra("url", merchant_Url);
            startAcitivityWithEffect(intent);
            return true;
        }

        /*
		 * if(id == R.id.termscondition) { Intent i = new
		 * Intent(Home.this,TermsAndCondition.class); startActivity(i); return
		 * true; }
		 *
		 * if(id == R.id.HowtoUse) { Intent i = new
		 * Intent(Home.this,HowToUse.class); startActivity(i); return true; }
		 */

        // if(id == R.id.notify){
        //
        //
        // Intent i = new Intent(Home.this,BNotification.class);
        // startActivity(i);
        // return true;
        //
        // }
        if (id == R.id.menu_hotlist) {
            Intent i = new Intent(Home.this, BNotification.class);
            startAcitivityWithEffect(i);
            return true;
        }

		/*
		 * if(id==R.id.refer) { Intent i = new
		 * Intent(Home.this,Friendsinvite.class); startActivity(i);
		 *
		 * return true; }
		 */

        // if (id == R.id.changesPsd) {
        // Intent i = new Intent(Home.this, ChangePassword.class);
        // startActivity(i);
        //
        // return true;
        // }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        // TODO Auto-generated method stub

        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onBackPressed() {
        if (!ConstantFlag.FLAG_CHAT_CONTACT) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Are you Sure you want to Exit?")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    saveGroupLeaveTm();

                                    try {
                                        if (connectionServiceObject != null) {
                                            // stopService();
                                            if (GlobalData.connection != null) {
                                                if (GlobalData.connection
                                                        .isConnected()) {
                                                    try {
                                                        Presence presence = new Presence(
                                                                Presence.Type.unavailable);
                                                        GlobalData.connection
                                                                .sendPacket(presence);
                                                        // GlobalData.connection.disconnect();
                                                    } catch (Exception e) {
                                                        // TODO Auto-generated
                                                        // catch block
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }
                                            stopService();

                                        }
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

									/*
									 * Process.killProcess(Process.myPid());
									 * ActivityManager
									 * .killBackgroundProcesses(PackageName)
									 */
                                    // NavUtils.navigateUpFromSameTask(Home.this);
                                    android.os.Process
                                            .killProcess(android.os.Process
                                                    .myPid());
                                    // Toast.makeText(Home.this,"Hello",Toast.LENGTH_SHORT).show();

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
        } else {

            FragmentManager fm = getSupportFragmentManager();

            for (Fragment frag : fm.getFragments()) {
                if (frag.isVisible()) {

                    FragmentManager childFm = frag.getFragmentManager();
                    if (childFm.getBackStackEntryCount() > 0) {

                        childFm.popBackStack();

                        return;
                    }
                }
            }
        }
    }

    public void backPress() {
        onBackPressed();
    }

    private void callChatScreen() {
        if (Utils.gcmFlag && GlobalData.connection != null
                && GlobalData.connection.isConnected()
                && GlobalData.connection.isAuthenticated()) {
            Utils.gcmFlag = false;
            Utils.msgCount = 0;
            Utils.phList = null;
            Intent intent = new Intent(getApplicationContext(),
                    InAppMessageActivity.class);
            startAcitivityWithEffect(intent);
            return;
        }
        // else if(isFromShare && GlobalData.connection != null
        // && GlobalData.connection.isConnected()
        // && GlobalData.connection.isAuthenticated()){
        // SingleChatRoomAdapter.isForwardData=true;
        // SingleChatRoomAdapter.typeOfData= shareDataType;
        // SingleChatRoomAdapter.pathToForwardData=sharePath;
        // // SingleChatRoomAdapter.msgToSend=textToShare;
        // isFromShare=false;
        // Intent intent = new Intent(getApplicationContext(),
        // InAppMessageActivity.class);
        // // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);
        // }
    }

    /*Amit Function to sync Contacts with server*/
    private void insertContactsMethod() {
        // TODO Auto-generated method stub

        if (chatPrefs != null) {
            if (Utils.isDeviceOnline(Home.this)) {

                int count = chatPrefs.getInt("INSERT_CONTACTS_COUNT", 0);

                if (count == 0) {
                    InsertContactAsyncTask contactAsyncTask = new InsertContactAsyncTask();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        contactAsyncTask
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        contactAsyncTask.execute();
                    }

                    Editor editor = chatPrefs.edit();
                    count++;
                    editor.putInt("INSERT_CONTACTS_COUNT", count);
                    editor.commit();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void refreshCreditBalance() {
        new webservice(null, webservice.getCurrentBalance.geturl(UserId),
                webservice.TYPE_GET, webservice.TYPE_GET_TOP_BALANCE, Home.this);
    }

    private void calloutboxservice() {

        fetchMobileandUserId();

        dbObject.Open();
        Cursor ce;
        ce = dbObject.getOutBox();
        while (ce.moveToNext()) {
            if (ce.getCount() >= counervariable) {
                counervariable++;
                dbmessagein = ce.getString(0); // message
                dbnumberin = ce.getString(1); // number
                dbtime = ce.getString(2);// time
                dbmyid = ce.getString(3);// app userid
                dbsenderuserid = ce.getString(4);// sender userid
                dbdate = ce.getString(5);// date
                dbIurl = ce.getString(6);// url
                dbsendingtype = ce.getString(7);// s_m 0r r_m
                dbdiff = ce.getString(8);// message type: txt, image
                dbSstatus = ce.getString(9);// sending status
                dbrecipientId = ce.getString(10);// receipent userid
                dbcheckuncheck = ce.getString(11);// check, uncheck status
                dbpageName = ce.getString(12);// page name
                dbOutID = ce.getInt(13);// out id auto incresase
                dbinboxOut = ce.getInt(14);// inbox outbox key
                dbgrupname = ce.getString(15);// group name
                dbsendStatusRead = ce.getInt(16);// sms read status

                if (dbdiff.equals("TXT")) {

                    new webservice(null,
                            webservice.MessageSendToRecipient.geturl("",
                                    InAppUserlogin, InAppPassword, UserId,
                                    "SMSSMS", dbnumberin, dbmessagein, Mobile,
                                    dbrecipientId, dbcheckuncheck, dbgrupname),
                            webservice.TYPE_GET,
                            webservice.TYPE_SENT_MESSAGE_INBOX,
                            new ServiceHitListener() {

                                @Override
                                public void onSuccess(Object Result, int id) {

                                    if (dbgrupname.length() > 0) {
                                        dbObject.DeleteOutboxbyGroupName(dbgrupname);
                                    } else {
                                        dbObject.DeleteOutbox(dbnumberin,
                                                dbOutID);
                                    }

                                }

                                @Override
                                public void onError(String Error, int id) {

                                }
                            });

                } else if (dbdiff.equals("IMAGE")) {
                    dbcheckuncheck = ce.getString(11);
                    path = ce.getString(6);
                    dbgrupname = ce.getString(15);
                    dbcheckuncheck = ce.getString(11);
                    recipientid = ce.getString(10);
                    mediagpnm = dbgrupname;
                    medianumber = dbnumberin;

                    new webservice(null,
                            webservice.GetFTPHostDetail.geturl("image"),
                            webservice.TYPE_GET, webservice.TYPE_FTP_UPLD,
                            new ServiceHitListener() {

                                public void onSuccess(Object Result, int id) {
                                    try {
                                        imagetoupload2 = path.substring(path
                                                .lastIndexOf("/"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    filetype = "image";
                                    filename = path.substring(path
                                            .lastIndexOf("/") + 1);

                                    GetFTPCre gpmodel = (GetFTPCre) Result;
                                    String FTP_USER = gpmodel
                                            .getGetFTPHostDetail().get(0)
                                            .getFtpUser();
                                    String FTP_PASS = gpmodel
                                            .getGetFTPHostDetail().get(0)
                                            .getFtpPassword();
                                    String FTP_HOST = gpmodel
                                            .getGetFTPHostDetail().get(0)
                                            .getHostName();
                                    urlpath = gpmodel.getGetFTPHostDetail()
                                            .get(0).getFtpUrl();

                                    String imagePath = path;
                                    sendurlpath = urlpath + imagetoupload2;

                                    File f = new File(imagePath);
                                    FTPClient client = new FTPClient();
                                    try {
                                        if (android.os.Build.VERSION.SDK_INT > 9) {

                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                    .permitAll().build();
                                            StrictMode.setThreadPolicy(policy);
                                        }
                                        client.connect(FTP_HOST, 21);
                                        client.login(FTP_USER, FTP_PASS);
                                        client.setType(FTPClient.TYPE_BINARY);
                                        // client.changeDirectory("/upload/");

                                        client.upload(f,
                                                new MyTransferListener());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        try {
                                            client.disconnect(true);
                                        } catch (Exception e2) {

                                        }
                                    }

                                }

                                @Override
                                public void onError(String Error, int id) {

                                }
                            });

                } else if (dbdiff.equals("VIDEO")) {

                    dbcheckuncheck = ce.getString(11);
                    path = ce.getString(6);
                    dbgrupname = ce.getString(15);
                    dbcheckuncheck = ce.getString(11);
                    recipientid = ce.getString(10);
                    mediagpnm = dbgrupname;
                    medianumber = dbnumberin;

                    new webservice(null,
                            webservice.GetFTPHostDetail.geturl("video"),
                            webservice.TYPE_GET, webservice.TYPE_FTP_UPLD,
                            new ServiceHitListener() {

                                public void onSuccess(Object Result, int id) {

                                    try {
                                        imagetoupload2 = path.substring(path
                                                .lastIndexOf("/"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    filetype = "video";
                                    filename = path.substring(path
                                            .lastIndexOf("/") + 1);

                                    GetFTPCre gpmodel = (GetFTPCre) Result;
                                    String FTP_USER = gpmodel
                                            .getGetFTPHostDetail().get(0)
                                            .getFtpUser();
                                    String FTP_PASS = gpmodel
                                            .getGetFTPHostDetail().get(0)
                                            .getFtpPassword();
                                    String FTP_HOST = gpmodel
                                            .getGetFTPHostDetail().get(0)
                                            .getHostName();

                                    urlpath = gpmodel.getGetFTPHostDetail()
                                            .get(0).getFtpUrl();

                                    // Log.w("VIDEO", "VIDEO :urlpath" +
                                    // urlpath);

                                    String imagePath = path;
                                    sendurlpath = urlpath + imagetoupload2;

                                    filetype = "video";

                                    // Log.w("VIDEO", "VIDEO :sendurlpath"
                                    // + sendurlpath);

                                    // Log.w("VIDEO", "VIDEO :imagePath"
                                    // + imagePath);

                                    File f = new File(imagePath);

                                    FTPClient client = new FTPClient();

                                    try {
                                        if (android.os.Build.VERSION.SDK_INT > 9) {

                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                    .permitAll().build();
                                            StrictMode.setThreadPolicy(policy);
                                        }
                                        client.connect(FTP_HOST, 21);
                                        client.login(FTP_USER, FTP_PASS);
                                        client.setType(FTPClient.TYPE_BINARY);
                                        // client.changeDirectory("/upload/");

                                        client.upload(f,
                                                new MyTransferListener());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        try {
                                            client.disconnect(true);
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }

                                }

                                @Override
                                public void onError(String Error, int id) {

                                }
                            });
                } else if (dbdiff.equals("AUDIO")) {

                    dbcheckuncheck = ce.getString(11);
                    path = ce.getString(6);
                    dbgrupname = ce.getString(15);
                    dbcheckuncheck = ce.getString(11);
                    recipientid = ce.getString(10);
                    mediagpnm = dbgrupname;
                    medianumber = dbnumberin;

                    new webservice(null,
                            webservice.GetFTPHostDetail.geturl("audio"),
                            webservice.TYPE_GET, webservice.TYPE_FTP_UPLD,
                            new ServiceHitListener() {

                                public void onSuccess(Object Result, int id) {

                                    try {
                                        imagetoupload2 = path.substring(path
                                                .lastIndexOf("/"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    filetype = "audio";
                                    filename = path.substring(path
                                            .lastIndexOf("/") + 1);

                                    GetFTPCre gpmodel = (GetFTPCre) Result;
                                    String FTP_USER = gpmodel
                                            .getGetFTPHostDetail().get(0)
                                            .getFtpUser();
                                    String FTP_PASS = gpmodel
                                            .getGetFTPHostDetail().get(0)
                                            .getFtpPassword();
                                    String FTP_HOST = gpmodel
                                            .getGetFTPHostDetail().get(0)
                                            .getHostName();
                                    urlpath = gpmodel.getGetFTPHostDetail()
                                            .get(0).getFtpUrl();

                                    String imagePath = path;

                                    sendurlpath = urlpath + imagetoupload2;

                                    filetype = "audio";
                                    File f = new File(imagePath);

                                    FTPClient client = new FTPClient();
                                    try {
                                        if (android.os.Build.VERSION.SDK_INT > 9) {

                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                    .permitAll().build();
                                            StrictMode.setThreadPolicy(policy);
                                        }
                                        client.connect(FTP_HOST, 21);
                                        client.login(FTP_USER, FTP_PASS);
                                        client.setType(FTPClient.TYPE_BINARY);
                                        // client.changeDirectory("/upload/");

                                        client.upload(f,
                                                new MyTransferListener());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        try {
                                            client.disconnect(true);
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }

                                }

                                @Override
                                public void onError(String Error, int id) {

                                }
                            });

                }

            } else {
                dbObject.DeleteOutboxAll();
            }

        }
        dbObject.close();

    }

    public void fetchMobileandUserId() {
        dbObject.Open();

        Cursor c;

        c = dbObject.getLoginDetails();

        while (c.moveToNext()) {
            InAppPassword = c.getString(5);
            InAppUserlogin = c.getString(6);
            Mobile = c.getString(1);
            UserId = c.getString(3);

        }

        dbObject.close();
    }

    @Override
    public void onSuccess(Object Result, int id) {

        if (id == webservice.TYPE_GET_TOP_BALANCE) {

            // Declare model of that service
            getTopBalance model = (getTopBalance) Result;

            String curBalance = model.getUserTopTransactionDetails().get(0)
                    .getBalance();
            SharedPreferences prfs = getSharedPreferences("profileData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prfs.edit();
            editor.putString("Balance", curBalance);
            editor.commit();

            // create object of create annimation
            Animation annimate = AnimationUtils.loadAnimation(this,
                    R.anim.textblinking);

            if (curBalance != null) {
                txtDate.setText(model.getUserTopTransactionDetails().get(0)
                        .getBalance());
                // stop annimation
                annimate.cancel();
            } else {
                txtDate.setText("...");
                // start animation
                txtDate.startAnimation(annimate);
            }

        }
    }

    @Override
    public void onError(String Error, int id) {
        // Toast.makeText(getApplicationContext(), Error,
        // Toast.LENGTH_SHORT).show();

    }


    public boolean hasAnydata() {

        dbObject.Open();

        Cursor c;

        c = dbObject.getLoginDetails();

        while (c.moveToNext()) {

            dbObject.close();
            return false;
        }

        dbObject.close();
        return true;
    }

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {
        }

        public void transferred(int length) {
        }

        public void completed() {
            fetchMobileandUserId();

            new webservice(null, webservice.AudioVideoPictureMessage.geturl(
                    mediagpnm, "", InAppUserlogin, InAppPassword, UserId,
                    "SMSMSMS", medianumber, "One Attachment", Mobile,
                    recipientid, dbcheckuncheck, sendurlpath, filetype,
                    filename), webservice.TYPE_GET,
                    webservice.TYPE_SENT_MESSAGE_INBOX,
                    new ServiceHitListener() {

                        @Override
                        public void onSuccess(Object Result, int id) {
                            Log.i("pageName", "pageName");

                            dbObject.Open();
                            dbObject.DeleteOutbox(medianumber, dbOutID);
                            dbObject.close();

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

    public void callFragmentWithAddBack(Fragment fragment, String tag) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.tab_bottom_container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public ImageView getmActionBarImage() {
        return mActionBarImage;
    }

    private void saveGroupLeaveTm() {
        Utils.saveGroupLeaveTm(chatPrefs);
    }

    public interface ChatFragmentListener {
        // public void messagePrint();
        void messagePrint(String packetId, String status);
    }

    @Override
    public void notifyMsgStatus(String remot_id, String msgpacket_id,
                                String msg_status) {

        GlobalData.dbHelper.updatestatusOfSentMessage(
                StringUtils.parseBareAddress(remot_id), msgpacket_id,
                msg_status);
        if (SingleChatRoomFrgament.obj != null) {
            SingleChatRoomFrgament.obj.messagePrint(msgpacket_id, msg_status);
        }
        if (msg_status.equalsIgnoreCase("deliver")) {
            if (BroadCastGroupSecond.obj != null) {
                BroadCastGroupSecond.obj.messagePrint(msgpacket_id, msg_status);
            }
        }
    }

    private void setHandler() {
        // TODO Auto-generated method stub
        Handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                for (int i = 0; i < getSupportFragmentManager()
                        .getBackStackEntryCount(); i++) {
                    getSupportFragmentManager().popBackStack();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {

       /* if (mBackButton == v) {
            onBackPressed();
        }*/

        int id=v.getId();
        switch(id)
        {

            case R.id.txtTopup:
                AlertDialogManager alert = new AlertDialogManager();
                String Pass = Utils.getPassword(Home.this);
                String postData = "password=" + Pass;// &id=236";
                String url = "http://kitever.com/BuyCredit.aspx?tab=topup&userid=" + UserId;
                byte[] data = EncodingUtils.getBytes(postData, "BASE64");
                alert.webviewPoup(Home.this, "Topup", url, data);

                break;


            case R.id.lyt1:
                Editor deditor = themePrefs.edit();
                deditor.putString("themeid", "0");
                deditor.commit();
                defualtStyle(Home.this);
                loadTheme();
                ThemeDilaog.hide();
                 break;
            case R.id.lyt2:

                Editor editor = themePrefs.edit();
                editor.putString("themeid", "1");
                editor.commit();
                Style1(Home.this);
                loadTheme();
                ThemeDilaog.hide();
                break;

            case R.id.lyt3:

                Editor editor2 = themePrefs.edit();
                editor2.putString("themeid", "2");
                editor2.commit();
                Style2(Home.this);
                loadTheme();
                ThemeDilaog.hide();
                break;

            case R.id.lyt4:

                Editor editor3 = themePrefs.edit();
                editor3.putString("themeid", "3");
                editor3.commit();
                Style3(Home.this);
                loadTheme();
                ThemeDilaog.hide();
                break;
        }
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

    public class HomewebClass extends WebViewClient {
        ProgressDialog pd = null;
        boolean timeout;
        Context context;
        ProgressBar pbar;

   /* public WebClientClass(Context context)
    {
         timeout = true;
         this.context=context;
    }*/

        public HomewebClass(Context context, ProgressBar pbar) {
            timeout = true;
            this.context = context;
            this.pbar = pbar;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        /*if (pd == null) {
            pd = new ProgressDialog(context);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }*/
            final WebView wview = view;
            pbar.setVisibility(View.VISIBLE);
            Handler myHandler = new Handler();
            Runnable run = new Runnable() {
                public void run() {
                    if (timeout) {
                        pbar.setVisibility(View.GONE);
                        wview.stopLoading();
                        alertmsg();
                    }
                }
            };

            myHandler.postDelayed(run, 12000);  //12sec timeout for loading webview
            super.onPageStarted(view, url, favicon);
            view.clearCache(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            timeout = false;
            pbar.setVisibility(View.GONE);
       /* if (pd != null)
            pd.dismiss();*/
            super.onPageFinished(view, url);
        }
    }


    public void alertmsg() {
        homewebView.setVisibility(View.GONE);
        homeprogressbar.setVisibility(View.GONE);
        company_name.setVisibility(View.VISIBLE);
        company_image.setVisibility(View.VISIBLE);
        company_name.setText(merchantName);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Web page is taking time to load !", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        company_name.setVisibility(View.GONE);
                        company_image.setVisibility(View.GONE);
                        homewebView.loadUrl(Home_Url);
                        homewebView.setVisibility(View.VISIBLE);
                        HomewebClass webViewClient = new HomewebClass(Home.this, homeprogressbar);
                        homewebView.setWebViewClient(webViewClient);
                    }
                });

// Changing message text color
        snackbar.setActionTextColor(Color.RED);

// Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void stopService() {
        try {
            stopService(new Intent(Home.this, RegisterConnectionService.class));
            unbindService(Home.homeServiceConnection);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void doBindService() {
        // activity connects to the service.
        Intent intent1 = new Intent(this, RegisterConnectionService.class);
        startService(intent1);
        Intent intent = new Intent(this, RegisterConnectionService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        mIsBound = true;
    }

    @Override
    public void onServiceConnected(ComponentName arg0, IBinder service) {
        connectionServiceObject = ((RegisterConnectionService.ServiceBinder) service)
                .getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        connectionServiceObject = null;

    }

    public void getGroupLimitFromServer() {

        String user_type = Utils.getUserType(this);
        sms19.inapp.msg.asynctask.GroupParticipantLimitationAsyncTask groupLimitAsync = new sms19.inapp.msg.asynctask.GroupParticipantLimitationAsyncTask(
                this, user_type);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            groupLimitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            groupLimitAsync.execute();
        }
    }

    public void getXmppGroupList() {
        SharedPreferences prfs = getSharedPreferences("profileData",
                Context.MODE_PRIVATE);
        user_jid = prfs.getString("user_login", null);
        user_jid = user_jid + "@email19.in";
        GetXmppGroupListAsyncTask broadCastGroupList = new GetXmppGroupListAsyncTask(
                USERID, user_jid, Home.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            broadCastGroupList
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            broadCastGroupList.execute();
        }
    }

    private void saveRegisterData(String userNumber, String countryCode) {
        RingtoneManager manager = new RingtoneManager(Home.this);
        manager.setType(RingtoneManager.TYPE_NOTIFICATION);
        @SuppressWarnings("static-access")
        Uri Duri = manager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        SharedPreferences prfs = getSharedPreferences("profileData",
                Context.MODE_PRIVATE);
        String pass = prfs.getString("Pwd", "");
        Utils.printLog("USER PASSWORD:  " + pass);
        Editor editor = chatPrefs.edit();
        editor.putString("userNumber", userNumber);
        editor.putString("user_jid", userNumber + "@" + GlobalData.HOST);
        editor.putString("userPassword", pass);
        editor.putString("msgtone", Duri.toString());
        editor.putString("groupmsgtone", Duri.toString());
        editor.putString("countryCode", countryCode);
        editor.putBoolean("firsttime", true);
        editor.putBoolean("vibratebtn", true);
        editor.putBoolean("silentbtn", false);
        editor.putLong("lastleaveTime", 0);
        editor.commit();
        GlobalData.dbHelper.saveUserDatainDb(chatPrefs, userNumber, "my", null);
    }

    /*Fetch contact from phone contacts*/
    public class InsertContactAsyncTask extends AsyncTask<Void, Void, Void> {

        private String contact = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            ContactUtil.isInsertContactRunning = true;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                contact = ContactUtil.getDeviceContact(Home.this, chatPrefs);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if (Utils.isDeviceOnline(Home.this)) {
                    if ((!contact.equalsIgnoreCase("null"))
                            && (!contact.equalsIgnoreCase(""))) {

                        String userId = Utils.getUserId(Home.this);
                        if (GlobalData.getContactListAsyncTask == null) {
                            GlobalData.getContactListAsyncTask = new GetContactListAsyncTask(
                                    Home.this, chatPrefs, userId, null)
                            /*
                             * (
                                                 * GetContactListAsyncTask
                                                 * ) new
                                                 * GetContactListAsyncTask
                                                 * (
                                                 * chatPrefs
                                                 * ,
                                                 * userId
                                                 * )
                                                 * .execute
                                                 * ()
                                                 */;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                GlobalData.getContactListAsyncTask
                                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                GlobalData.getContactListAsyncTask.execute();
                            }
                        } else {
                            GlobalData.getContactListAsyncTask.cancel(true);
                            GlobalData.getContactListAsyncTask = null;
                            GlobalData.getContactListAsyncTask = new GetContactListAsyncTask(
                                    Home.this, chatPrefs, userId, null)
                            /*
                             * (
                             * GetContactListAsyncTask
                             * ) new
                             * GetContactListAsyncTask
                             * (
                             * chatPrefs
                             * ,
                             * userId
                             * )
                             * .execute
                             * ()
                             */;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                GlobalData.getContactListAsyncTask
                                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                GlobalData.getContactListAsyncTask.execute();
                            }
                        }
                    }

                }

                if (ConstantFields.HIDE_MENU == 4) {
                    if (ContactFragment.deleteContactHandler != null) {
                        android.os.Message msg = new android.os.Message();// comment
                        // m
                        Bundle b = new Bundle();
                        b.putString("remoteidnew", "");
                        msg.setData(b);
                        ContactFragment.deleteContactHandler.sendMessage(msg);
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private class ChatTimer extends TimerTask {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            callChatScreen();
        }
    }

    private String getRealPathFromURI(Context mContext, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(mContext, contentUri, proj,
                null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void setHomeUI() {

        actionBarSetting(Home.this, getSupportActionBar(), "Home");
        setContentView(R.layout.newxml);

        // textToShare=getIntent().getStringExtra(Intent.EXTRA_TEXT);
        homeServiceConnection = this;
        permissionClass = new PermissionClass(Home.this);

        scrollView = (HorizontalScrollView) findViewById(R.id.hscroll);
        footerLayout = (RelativeLayout) findViewById(R.id.footerLayout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        layoutMessage = (LinearLayout) findViewById(R.id.layout_message);
        layoutChats = (LinearLayout) findViewById(R.id.layout_chats);
        layoutContacts = (LinearLayout) findViewById(R.id.layout_contacts);
        layoutTemplates = (LinearLayout) findViewById(R.id.layout_templates);
        layoutMicro = (LinearLayout) findViewById(R.id.layout_micro);
        layoutUnlock = (LinearLayout) findViewById(R.id.layout_unlock);
        //layoutReports = (LinearLayout) findViewById(R.id.layout_reports);
        layoutPOS = (LinearLayout) findViewById(R.id.layout_pos);

        tvMessage = (TextView) findViewById(R.id.home_message);
        tvChats = (TextView) findViewById(R.id.home_chats);
        tvContacts = (TextView) findViewById(R.id.home_contacts);
        tvTemplates = (TextView) findViewById(R.id.home_templates);
        tvStorage = (TextView) findViewById(R.id.home_unlock);
        tvMicro = (TextView) findViewById(R.id.home_micro);
        //tvReports = (TextView) findViewById(R.id.home_reports);
        tvPOS = (TextView) findViewById(R.id.home_pos);

        homewebView = (WebView) findViewById(R.id.homewebView);
        homeprogressbar = (ProgressBar) findViewById(R.id.homeprogressbar);
        company_image = (ImageView) findViewById(R.id.company_image);
        company_name = (TextView) findViewById(R.id.company_name);
        docimage = (ImageView) findViewById(R.id.docimage);


        bottom_leftt_arrow = (TextView) findViewById(R.id.bottom_left_arrow);
        bottom_right_arrow = (TextView) findViewById(R.id.bottom_right_arrow);
        bottom_home_contacts = (TextView) findViewById(R.id.bottom_home_contacts);
        bottom_home_chats = (TextView) findViewById(R.id.bottom_home_chats);
        bottom_home_msg = (TextView) findViewById(R.id.bottom_home_msg);
        bottom_home_tools = (TextView) findViewById(R.id.bottom_home_tools);
        bottom_home_storage = (TextView) findViewById(R.id.bottom_home_storage);
        bottom_home_crm = (TextView) findViewById(R.id.bottom_home_crm);
        bottom_home_micro = (TextView) findViewById(R.id.bottom_home_micro);


        homewebView.clearHistory();
        homewebView.getSettings().setJavaScriptEnabled(true);
        homewebView.getSettings()
                .setJavaScriptCanOpenWindowsAutomatically(true);
        String Pass = Utils.getPassword(Home.this);
        String postData = "password=" + Pass;// &id=236";
		/*
		 * homewebView.postUrl("http://kitever.com/BuyCredit.aspx?tab=topup&userid="
		 * + UserId, EncodingUtils.getBytes(postData, "BASE64"));
		 */

        chatPrefs = getSharedPreferences("chatPrefs", MODE_PRIVATE);
        USERID = chatPrefs.getString("userId", "");
        SharedPreferences prfs = getSharedPreferences("profileData",
                Context.MODE_PRIVATE);

        merchantName = prfs.getString("MerchantName", null);
        merchant_Url = prfs.getString("Merchant_Url", null);
        Home_Url = prfs.getString("Home_Url", null);
        AddOn = prfs.getString("AddOn", null);

       /* if (AddOn != null && AddOn.contains("POS"))
            layoutPOS.setVisibility(View.VISIBLE);
        else
            layoutPOS.setVisibility(View.GONE);*/

        String LoginType = prfs.getString("UserCategory", null);

        if ((LoginType.trim()).equalsIgnoreCase("2")) {
            layoutMicro.setVisibility(View.VISIBLE);
            layoutPOS.setVisibility(View.VISIBLE);
        } else {
            layoutMicro.setVisibility(View.GONE);
            layoutPOS.setVisibility(View.GONE);
        }


        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("KiteverImageDir", Context.MODE_PRIVATE);
        File f = new File(directory.getPath(), "merchantprofile.jpg");

        if (Utils.isDeviceOnline(this)) {
            Log.i("isTrue", "isTrue");
            if (Home_Url != null && Home_Url != "" && Home_Url.length() > 0) {
                company_name.setVisibility(View.GONE);
                company_image.setVisibility(View.GONE);
                homewebView.loadUrl(Home_Url);
                HomewebClass webViewClient = new HomewebClass(this, homeprogressbar);
                homewebView.setWebViewClient(webViewClient);
            } else {
                homewebView.setVisibility(View.GONE);
                homeprogressbar.setVisibility(View.GONE);
                company_name.setVisibility(View.VISIBLE);
                company_image.setVisibility(View.VISIBLE);
                company_name.setText(merchantName);
                if (f.exists()) {
                    Log.i("isTrue", "exists");
                    company_image.setImageDrawable(Drawable.createFromPath(f
                            .getPath()));
                }
            }
        } else {
            homewebView.setVisibility(View.GONE);
            homeprogressbar.setVisibility(View.GONE);

            company_name.setVisibility(View.VISIBLE);
            company_image.setVisibility(View.VISIBLE);
            company_name.setText(merchantName);
            if (f.exists()) {
                Log.i("isTrue", "exists");
                company_image.setImageDrawable(Drawable.createFromPath(f
                        .getPath()));
            }
        }

        if (!(USERID != null && USERID.length() > 0)) {
            USERID = Utils.getUserId(Home.this);
        }
        String baseUrl = Utils.getBaseUrlValue(this);// get Base Url

        if (!baseUrl.equalsIgnoreCase("")) {
            Apiurls.KIT19_BASE_URL = baseUrl;
        }

        if (Utils.isDeviceOnline(this)) {
            try {
                GlobalData.GROUP_PARTICIPANT_LIMIT = Utils.getGroupLimit(this);
                GlobalData.BRAODCAST_GROUP_PARTICIPANT_LIMIT = Utils
                        .getBroadCastGroupLimit(this);
                /*Amit get group list from server*/
                getGroupLimitFromServer();

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        runonuithread = this;
        afterThis = Home.this;
        Utils.MakeDirs(this);

        user_jid = chatPrefs.getString("user_jid", "");
        if (GlobalData.dbHelper == null) {
            try {
                GlobalData.dbHelper = new DatabaseHelper(Home.this, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (Utils.isDeviceOnline(Home.this)) {
            Utils.callForGetTimeAsyncTask(Home.this);// get current time;
        } else {
            long valuetime = Utils.getUserTime(this);
            long valuetimeCurrent = Utils.getUserTimeCurrent(this);
            if (valuetime != 0) {
                Utils.SERVER_TIME = valuetime;
                Utils.CURRENT_TIME = valuetimeCurrent;
            }
        }

        try {
            /*Amit Insert Contacts to local db*/
            insertContactsMethod();
            mydetail = GlobalData.dbHelper.getUserDatafromDB();
            if (mydetail.getRemote_jid() == null) {
                String number = (chatPrefs.getString("userNumber", "").trim());
                saveRegisterData(number, Utils.getCountryCode(Home.this));
                mydetail = GlobalData.dbHelper.getUserDatafromDB();
            }
            ConstantFields.mydetail = mydetail;

			/*
			 * timer = new Timer(); myTimerTask = new MyTimerTask();
			 * timer.schedule(myTimerTask, 1000, 5000);
			 */

        } catch (Exception e) {
            e.printStackTrace();
        }

        setHandler();
        rest = Rest.getInstance();

        if (Utils.isDeviceOnline(Home.this)) {

            try {
                doBindService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        moonicon = Typeface.createFromAsset(getAssets(), "fonts/icomoon.ttf");


        tvMessage.setTypeface(moonicon);
        tvChats.setTypeface(moonicon);
        tvContacts.setTypeface(moonicon);
        tvTemplates.setTypeface(moonicon);
        tvStorage.setTypeface(moonicon);
        tvMicro.setTypeface(moonicon);
        tvPOS.setTypeface(moonicon);
        bottom_leftt_arrow.setTypeface(moonicon);
        bottom_right_arrow.setTypeface(moonicon);


        docimage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                themePopup();

               /* PopupMenu popupMenu = new PopupMenu(Home.this, v);
                popupMenu.setOnMenuItemClickListener(Home.this);
                popupMenu.inflate(R.menu.theme_menu);
                if (themePrefs.contains("themeid"))
                    themeid = themePrefs.getString("themeid", "0");
                int id=Integer.parseInt(themeid);
                popupMenu.getMenu().getItem(id).setChecked(true);
                popupMenu.show();*/

            }
        });

        bottom_leftt_arrow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_LEFT);
                    }
                });
            }
        });

        bottom_right_arrow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_RIGHT);
                    }
                });
            }
        });

        // more = (Button) findViewById(R.id.btn_more);
        txtDate = (TextView) findViewById(R.id.txtDate);
        if (!Utils.isDeviceOnline(Home.this)) {
            String balVal = "<i>No Internet Connection Available-</i> <u>Retry!</u>";
            txtDate.setText(Html.fromHtml(balVal));
        }

        txtDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (txtDate.getText().toString()
                        .contains("No Internet Connection Available")) {
                    refreshCreditBalance();
                }

            }
        });
        mFrameLayoout = (LinearLayout) findViewById(R.id.container);
        mHomecontainer = (LinearLayout) findViewById(R.id.home_container);

        balanceIcon = (TextView) findViewById(R.id.balanceIcon);
        txtTopup = (TextView) findViewById(R.id.txtTopup);

        txtTopup.setTypeface(moonicon);
        balanceIcon.setTypeface(moonicon);

        txtTopup.setOnClickListener(this);
        mChatTabBtn = (TextView) findViewById(R.id.tab_chat);
        mContactTabBtn = (TextView) findViewById(R.id.tab_contact);

        mChatTabBtn.setOnClickListener(this);
        mContactTabBtn.setOnClickListener(this);

        /*************************** INTERNET ********************************/
        webservice._context = this;
        HomeStatus = this;
        HomePart = this;
        stop = true;
        /*************************** INTERNET ********************************/

        if (hasAnydata()) {
            Intent i1 = new Intent(Home.this, SplashScreen.class);
            startAcitivityWithEffect(i1);
            finish();
        }

        layoutMessage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(Home.this, SendSmsActivity.class);
                PopupMenu popupMenu = new PopupMenu(Home.this, v);
                popupMenu.setOnMenuItemClickListener(Home.this);
                popupMenu.inflate(R.menu.message_menu);
                popupMenu.show();
            }
        });

        layoutChats.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (permissionClass.checkPermissionForContacts()) {
                    GlobalData.PAGE_GROUP_TYPE = "chats";
                    Intent i = new Intent(getApplicationContext(),
                            InAppMessageActivity.class);
                    startAcitivityWithEffect(i);
                } else {
                    permissionClass.requestPermissionForContacts();
                }
            }
        });

        layoutContacts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (permissionClass.checkPermissionForContacts()) {
                    if (permissionClass.checkPermissionForExternalStorage()) {
                        if (permissionClass.checkPermissionForReadExternalStorage()) {

                            GlobalData.PAGE_GROUP_TYPE = "contacts";
                            Intent contact = new Intent(Home.this, ContactsActivity.class);
                            contact.putExtra("userId", UserId);
                            startAcitivityWithEffect(contact);
                        } else permissionClass.requestPermissionForReadExternalStorage();
                    } else
                        permissionClass.requestPermissionForExternalStorage();
                } else {
                    permissionClass.requestPermissionForContacts();
                }
            }
        });

        layoutTemplates.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                PopupMenu popupMenu = new PopupMenu(Home.this, view);
                popupMenu.setOnMenuItemClickListener(Home.this);
                popupMenu.inflate(R.menu.tools_menu);
                popupMenu.show();
            }
        });

        layoutUnlock.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent storeintent = new Intent(Home.this, DataStorage.class);
                startAcitivityWithEffect(storeintent);
            }
        });

        layoutMicro.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Home.this, view);
                popupMenu.setOnMenuItemClickListener(Home.this);
                popupMenu.inflate(R.menu.micro_site_menu);
                popupMenu.show();
            }
        });


        layoutPOS.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent posIntent = new Intent(Home.this, POSHomeTabbedActivity.class);
                startAcitivityWithEffect(posIntent);
            }
        });

        // call fxn for send message from outbox
        calloutboxservice();
        // callRepeatThread();
        // ********************ACTION BAR**********************************
        // ActionBar bar = getSupportActionBar();
        // bar.setBackgroundDrawable(new
        // ColorDrawable(Color.parseColor("#066966")));
        // if (merchantName == null || merchantName.equalsIgnoreCase("")) {
        // bar.setTitle(Html.fromHtml("<font color='#ffffff'>Kitever</font>"));
        // } else {
        // bar.setTitle(merchantName);
        // }
        /************************* ACTION BAR *********************************/

        fetchMobileandUserId();

        String senderId = "134386644306";
        com.kitever.gcm.GcmRegistrationHandler.registerToGcm(Home.this,
                senderId);

        timer = new Timer();
        chatTimer = new ChatTimer();
        timer.schedule(chatTimer, 1000, 5000);
    }

    private void themePopup()
    {
        ThemeDilaog = new Dialog(this);
        ThemeDilaog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // dialog.setTitle("Alert Setting");
        ThemeDilaog.setContentView(R.layout.themexml);

        Window window=ThemeDilaog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP|Gravity.LEFT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        //wlp.x=10;
        wlp.y=docimage.getBottom()+110;
        Log.i("x-y:","X-"+wlp.x+"   y-"+wlp.y);
        window.setAttributes(wlp);

        LinearLayout lyt1 = (LinearLayout) ThemeDilaog.findViewById(R.id.lyt1);
        LinearLayout lyt2 = (LinearLayout) ThemeDilaog.findViewById(R.id.lyt2);
        LinearLayout lyt3 = (LinearLayout) ThemeDilaog.findViewById(R.id.lyt3);
        LinearLayout lyt4 = (LinearLayout) ThemeDilaog.findViewById(R.id.lyt4);


        lyt1.setOnClickListener(this);
        lyt2.setOnClickListener(this);
        lyt3.setOnClickListener(this);
        lyt4.setOnClickListener(this);

        lyt1.setBackgroundResource(R.drawable.border_black);
        lyt2.setBackgroundResource(R.drawable.border_black);
        lyt3.setBackgroundResource(R.drawable.border_black);
        lyt4.setBackgroundResource(R.drawable.border_black);

        themeid = themePrefs.getString("themeid", "0");
        int id=Integer.parseInt(themeid);

       switch(id)
       {
           case 0:
                //lyt1.setBackgroundResource(R.drawable.border_grey);
                lyt1.setBackgroundColor(Color.parseColor("#B8C4C3"));
            break;
           case 1:
               lyt2.setBackgroundColor(Color.parseColor("#B8C4C3"));
               break;
           case 2:
               lyt3.setBackgroundColor(Color.parseColor("#B8C4C3"));
               break;
           case 3:
               lyt4.setBackgroundColor(Color.parseColor("#B8C4C3"));
               break;
       }

        ThemeDilaog.show();
    }

    private void loadTheme() {

        actionBarSetting(Home.this, getSupportActionBar(), getString(R.string.app_name));

        footerLayout.setBackgroundColor(Color.parseColor(CustomStyle.FOOTER_BACKGROUND));
        tvMessage.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        tvChats.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        tvContacts.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        tvTemplates.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        tvStorage.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        tvMicro.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        tvPOS.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));

        bottom_leftt_arrow.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        bottom_right_arrow.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        bottom_home_contacts.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        bottom_home_chats.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        bottom_home_msg.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        bottom_home_tools.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        bottom_home_storage.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        bottom_home_crm.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        bottom_home_micro.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        txtDate.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        txtTopup.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        setRobotoThinFont(txtDate, this);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send_sms_mail:
                Intent intent = new Intent(getApplicationContext(),
                        SendSmsMail.class);
                startAcitivityWithEffect(intent);
                return true;
            case R.id.scheduled_sms_email:
                Intent i1 = new Intent(Home.this, Schedule.class);
                startAcitivityWithEffect(i1);
                return true;
            case R.id.setting_sms_email:
                Intent i6 = new Intent(Home.this, SettingSmsMail.class);
                startAcitivityWithEffect(i6);
                return true;
            case R.id.transactions:
                // Intent i = new Intent(Home.this, ScheduleList.class);
                Intent i2 = new Intent(Home.this, Transaction.class);
                startAcitivityWithEffect(i2);
                return true;
          /*  case R.id.support:
                // Intent i = new Intent(Home.this, ScheduleList.class);
                Intent sintent = new Intent(Home.this, ContactUs.class);
                startAcitivityWithEffect(sintent);
                return true;*/


            case R.id.reports:
                Intent i3 = new Intent(Home.this, SecondSmsMailReport.class);
                i3.putExtra("dayreport", "today");
                i3.putExtra("DATE_SMS", "");
                startAcitivityWithEffect(i3);
                return true;
            case R.id.templates:
                Intent i5 = new Intent(Home.this, TemplateHomeActivity.class);
                startAcitivityWithEffect(i5);
                return true;
            case R.id.viewMicroSite:
                Intent intent1 = new Intent(Home.this, MicroSite.class);
                intent1.putExtra("fragment", "view");
                startAcitivityWithEffect(intent1);
                return true;

            case R.id.createMicroSite:
                Intent intent2 = new Intent(Home.this, MicroSite.class);
                intent2.putExtra("fragment", "create");
                startAcitivityWithEffect(intent2);
                return true;

            case R.id.leadsMicroSite:
                Intent intent3 = new Intent(Home.this, MicroSite.class);
                intent3.putExtra("fragment", "leads");
                startAcitivityWithEffect(intent3);
                return true;
        }
        return false;
    }

    public void startAcitivityWithEffect(Intent slideactivity) {
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
                getApplicationContext(), R.anim.animation, R.anim.animation2)
                .toBundle();
        startActivityForResult(slideactivity, 0, bndlanimation);
    }

    public void sendConnectionListener(String s) {
        Intent intent = new Intent("com.kitever.connection.listener");
        intent.putExtra("message", s);
        sendBroadcast(intent);
    }
}

/* Amit
    public void homeContainerIsVisiable() {
        mFrameLayoout.setVisibility(View.GONE);
        mHomecontainer.setVisibility(View.VISIBLE);
    }

    public void homeContainerIsGone() {
        mFrameLayoout.setVisibility(View.VISIBLE);
        mHomecontainer.setVisibility(View.GONE);
    }

         // private String textToShare;
    // private Uri uri;

	 * private boolean isDataExist() { dbObject.Open();
	 *
	 * Cursor t; t = dbObject.getTimeInfo();
	 *
	 * while (t.moveToNext()) { dbObject.close(); return true; }
	 *
	 * dbObject.close(); return false; }

     public void DatabaseCleanState() {

        dbObject.Open();
        dbObject.onUpgrade(getDBObject(), 1, 1);
        dbObject.close();

    }

     public SQLiteDatabase getDBObject() {
        return dbObject.db;
    }
    // private updateregisterAndStragerGlobalArrayListAsyncTask updateAsyncTask
    // = null;
    // private DeleteContactAsyncTask deleteContactAsyncTask = null;

    // private Sms19ContactsFromserverAsync addContactAsyncTask = null;
    // private SMS19AddContactAsyncTask addContactAsyncTask2 = null;
    // public static Handler offlineStatusSetHandler = null;
    // public static Handler fetchContactHaldlerObject = null;

      public void callFragmentWithOutAddBack(Fragment fragment, String tag) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.tab_bottom_container, fragment, tag);
        transaction.commit();
    }

    public void callFragmentForRemove(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public void toastMsg(String msg) {

        Toast.makeText(Home.this, msg, Toast.LENGTH_SHORT).show();
    }

     public TextView getmUserNameTitle() {
        return mUserNameTitle;
    }

    public TextView getmUserStatusTitle() {
        return mUserStatusTitle;
    }


	 * private void callLogoutMethod() {
	 *
	 * new AlertDialog.Builder(this) .setCancelable(false) .setMessage(
	 * "Are you Sure you want to Exit? All your chat data will be deleted.")
	 * .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	 *
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 *
	 * // delete all database DatabaseCleanState();
	 *
	 * Toast.makeText(getApplicationContext(), "Logout Successfully",
	 * Toast.LENGTH_SHORT) .show();
	 *
	 * Intent i = new Intent(Home.this, SMS19.class);
	 * i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
	 * Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(i); finish();
	 *
	 * }
	 *
	 * }) .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
	 *
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * dialog.cancel(); } }).show(); }


    @SuppressLint("NewApi")
    public void NotifyBroadCastAll(String msg) {
        notifyonlyone = 1;
        Uri soundUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // *************************************************
        // Log.w("inside broadcast", "helllo");

        // Prepare intent which is triggered if the // notification is selected
        Intent intent = new Intent(Home.this, BNotification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendIntent = PendingIntent.getActivity(Home.this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notiMail = new Notification.Builder(Home.this)

                .setContentTitle("KIT Message").setContentText(msg)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendIntent).setAutoCancel(true)
                .setSound(soundUri).setOngoing(true).build();
        mNotificationMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationMan.notify(NOTIFICATION_ID, notiMail);
        // startActivity(intent);

    }

    @SuppressLint("NewApi")
    private void sendNotification(String msg) {
        // this method
        countonlyone = 1;
        try {

            Uri soundUri = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // *************************************************

            Intent intent = new Intent(Home.this, Inbox.class);
            intent.putExtra("story", "hello");
            intent.putExtra("value", msg);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pIntent = PendingIntent.getActivity(Home.this, 0,
                    intent, 0);

            Notification mNotification = new Notification.Builder(Home.this)

                    .setContentTitle("KIT Message").setContentText(msg)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentIntent(pIntent).setSound(soundUri)
                    .setOngoing(true).setAutoCancel(true).build();

            mNotificationManager = (NotificationManager) Home.this
                    .getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);

        } catch (Exception e) {
        }

    }

	/*
	 * private void fetchCurrentTimeDate() { // for current time
	 *
	 * DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); curDateTime
	 * = df.format(Calendar.getInstance().getTime());
	 *
	 * DateFormat dfs = new SimpleDateFormat("HH:mm:ss"); curTime =
	 * dfs.format(Calendar.getInstance().getTime());
	 *
	 * //Log.w("TAG", "Time_Interval (curDateTime):" + curDateTime + ",curTime:"
	 * + curTime); }


    public void getBroadCastGroupList() {
        // String userId1=Utils.getUserId(Home.this);
        GetBroadCastGroupList broadCastGroupList = new GetBroadCastGroupList(
                USERID, user_jid, Home.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            broadCastGroupList
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            broadCastGroupList.execute();
        }

    }

     public static void setMessageNotificationListener() {
        // //////////////////////////////////
        if (GlobalData.msgEventManager == null) {
            GlobalData.msgEventManager = new MessageEventManager(
                    GlobalData.connection);
        }
        Utils.printLog("Set MessageEventRequestListener");

        GlobalData.msgEventManager
                .addMessageEventRequestListener(new MessageEventrequestListener());
        GlobalData.msgEventManager
                .addMessageEventNotificationListener(new MessageEventnotificationListener(
                        runonuithread, afterThis));
        // ///////////////////////////////////////
    }

    /*
	 * public void updateregisterAndStragerGlobalArrayListAsyncTaskMethod() { if
	 * (updateAsyncTask == null) { updateAsyncTask = new
	 * updateregisterAndStragerGlobalArrayListAsyncTask();
	 *
	 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	 * updateAsyncTask .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); }
	 * else { updateAsyncTask.execute(); } } else {
	 * updateAsyncTask.cancel(true); updateAsyncTask = null; updateAsyncTask =
	 * new updateregisterAndStragerGlobalArrayListAsyncTask(); if
	 * (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	 * updateAsyncTask .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); }
	 * else { updateAsyncTask.execute(); } }
	 *
	 * }
	 */

	/*
	 * class updateregisterAndStragerGlobalArrayListAsyncTask extends
	 * AsyncTask<Void, Void, Void> {
	 *
	 * @Override protected Void doInBackground(Void... params) {
	 *
	 * ArrayList<Contactmodel> arrayListNew = new ArrayList<Contactmodel>();
	 * arrayListNew.addAll((GlobalData.dbHelper
	 * .getContactfromDBOnlyRegisterAndStrager()));
	 * GlobalData.registerAndStragerGlobalArrayList.clear();
	 * GlobalData.registerAndStragerGlobalArrayList.addAll(arrayListNew);
	 *
	 * return null; }
	 *
	 * @Override protected void onPostExecute(Void result) { // TODO
	 * Auto-generated method stub super.onPostExecute(result); if
	 * (!Utils.isDeviceOnline(Home.this)) { OfflineStatusThread(); } else { if
	 * (!(GlobalData.connection != null &&
	 * GlobalData.connection.isAuthenticated() && GlobalData.connection
	 * .isConnected())) { OfflineStatusThread(); } }
	 *
	 * } }


    public void OfflineStatusThread() {

        Utils.offlineShowUser();

    }

// public LinearLayout getCamera_btn() {
// return camera_btn;
// }

    /*
	 * public void DeleteContactAsyncTaskMethod() {
	 *
	 * if (deleteContactAsyncTask == null) { deleteContactAsyncTask = new
	 * DeleteContactAsyncTask();
	 *
	 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
	 * deleteContactAsyncTask
	 * .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); } else {
	 *
	 * deleteContactAsyncTask
	 * .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); } } else {
	 * deleteContactAsyncTask.cancel(true); deleteContactAsyncTask = null;
	 * deleteContactAsyncTask = new DeleteContactAsyncTask(); if
	 * (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
	 * deleteContactAsyncTask
	 * .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); } else {
	 *
	 * deleteContactAsyncTask
	 * .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); } }
	 *
	 * }
	 */

	/*
	 * class DeleteContactAsyncTask extends AsyncTask<Void, Void, Void> { String
	 * response = ""; JSONObject resObj;
	 *
	 * @Override protected Void doInBackground(Void... params) {
	 *
	 * try { ContactUtil.fetchDeletedContactFirst(Home.this, chatPrefs); } catch
	 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 *
	 * return null; }
	 *
	 * }



     public void LoginToServer() {
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
            String path = System.getProperty("javax.net.ssl.trustStore");
            if (path == null)
                path = System.getProperty("java.home") + File.separator + "etc"
                        + File.separator + "security" + File.separator
                        + "cacerts.bks";
            connConfig.setTruststorePath(path);
        }

        GlobalData.connection = new XMPPConnection(connConfig);
        Utils.setUpConnectionProperties();

        // new ConnectingToServer().execute();
    }


	 * class MyTimerTask extends TimerTask {
	 *
	 * @Override public void run() {
	 *
	 * runOnUiThread(new Runnable() {
	 *
	 * @Override public void run() {
	 *
	 * if (!GlobalData.ContactStringToSend.equalsIgnoreCase("")) { if
	 * (Utils.isDeviceOnline(Home.this)) {
	 *
	 * if (timer != null) { timer.cancel(); timer = null; }
	 *
	 * // String userId=Utils.getUserId(Home.this); if
	 * (GlobalData.getContactListAsyncTask == null) {
	 * GlobalData.getContactListAsyncTask = new GetContactListAsyncTask(
	 * chatPrefs, USERID,null) (GetContactListAsyncTask ) new
	 * GetContactListAsyncTask (chatPrefs, userId).execute() ;
	 * GlobalData.getContactListAsyncTask.execute(); } else {
	 * GlobalData.getContactListAsyncTask.cancel(true);
	 * GlobalData.getContactListAsyncTask = null;
	 * GlobalData.getContactListAsyncTask = new GetContactListAsyncTask(
	 * chatPrefs, USERID,null) (GetContactListAsyncTask ) new
	 * GetContactListAsyncTask (chatPrefs, userId).execute() ;
	 * GlobalData.getContactListAsyncTask.execute(); }
	 *
	 * }
	 *
	 * }
	 *
	 * } }); }
	 *
	 * }


    public void doUnbindService() {
        if (mIsBound) {

            unbindService(this);
            mIsBound = false;
        }
    }

	 * @SuppressWarnings("unchecked") public void addSms19ContactsInDb(
	 * ArrayList<ContactDetailsNew> contactDetailsNews) {
	 *
	 * if (addContactAsyncTask2 == null) { addContactAsyncTask2 = new
	 * SMS19AddContactAsyncTask();
	 *
	 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	 * addContactAsyncTask2.executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR,
	 * contactDetailsNews); } else {
	 * addContactAsyncTask2.execute(contactDetailsNews); }
	 *
	 * } else { addContactAsyncTask2.cancel(true); addContactAsyncTask2 = null;
	 * addContactAsyncTask2 = new SMS19AddContactAsyncTask(); if
	 * (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	 * addContactAsyncTask2.executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR,
	 * contactDetailsNews); } else {
	 * addContactAsyncTask2.execute(contactDetailsNews); } }
	 *
	 * }
	 */

	/*
	 * class SMS19AddContactAsyncTask extends
	 * AsyncTask<ArrayList<ContactDetailsNew>, Void, Void> { String response =
	 * ""; JSONObject resObj;
	 *
	 * @Override protected Void doInBackground(ArrayList<ContactDetailsNew>...
	 * params) {
	 *
	 * try { ArrayList<ContactDetailsNew> allContact = params[0];
	 *
	 * GlobalData.dbHelper.addSms19Contact(allContact);
	 *
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 *
	 * return null; }
	 *
	 * }


	 * private void logoutHandlerMethod() { logoutHandler = new Handler() {
	 *
	 * @Override public void handleMessage(Message msg) {
	 *
	 * super.handleMessage(msg);
	 *
	 * final int logoutFlag = msg.getData().getInt("logoutFlag"); if (logoutFlag
	 * == 1) { if (dbObject != null) { // dbObject.deleteLoginData();
	 * Utils.saveUserId(Home.this, "");
	 *
	 * if (GlobalData.connection != null && GlobalData.connection.isConnected())
	 * { try { Presence presence = new Presence( Presence.Type.unavailable);
	 * GlobalData.connection.sendPacket(presence);
	 *
	 * // GlobalData.connection.disconnect(); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 *
	 * try { GlobalData.connection.disconnect();
	 *
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 *
	 * }
	 *
	 * GlobalData.connection = null; Toast.makeText(Home.this,
	 * "User Session Logout!", Toast.LENGTH_LONG).show(); Intent intent = new
	 * Intent(Home.this, LoginPage.class);
	 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 * intent.putExtra("relogin", true);
	 *
	 * startActivity(intent);
	 *
	 * }
	 *
	 * }
	 *
	 * } }; }
        public void getBroadCastGroupList() {
        // String userId1=Utils.getUserId(Home.this);
        GetBroadCastGroupList broadCastGroupList = new GetBroadCastGroupList(
                USERID, user_jid, Home.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            broadCastGroupList
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            broadCastGroupList.execute();
        }

    }
    //            case R.id.unlock:
//                AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
//                alert.setTitle("Plans");
//                WebView wv = new WebViewHelper(Home.this);
//                wv.clearHistory();
//                wv.getSettings().setJavaScriptEnabled(true);
//                wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//                String Pass = Utils.getPassword(Home.this);
//                String postData = "password=" + Pass;// &id=236";
//                wv.postUrl(
//                        "http://kitever.com/BuyCredit.aspx?tab=plans&userid="
//                                + UserId,
//                        EncodingUtils.getBytes(postData, "BASE64"));
//
//                // wv.loadUrl("http://kitever.com/BuyCredit.aspx?tab=plans&userid="
//                // + UserId);
//                WebClientClass webViewClient = new WebClientClass();
//                wv.setWebViewClient(webViewClient);
//                // wv.setWebViewClient(new WebViewClient());
//
//                alert.setView(wv);
//                alert.setNegativeButton("Close",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
//                            }
//                        });
//                alert.show();
//                return true;

    */