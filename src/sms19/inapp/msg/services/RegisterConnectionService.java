package sms19.inapp.msg.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PrivacyListManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.MessageEventManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import sms19.inapp.msg.BroadCastGroupSecond;
import sms19.inapp.msg.ChatFragment;
import sms19.inapp.msg.asynctask.GetFtpDetailsAsyncTask;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Slacktags;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.database.DatabaseHelper;
import sms19.inapp.msg.model.Chatmodel;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Chatlistner;
import sms19.inapp.msg.rest.GroupchatInvitation;
import sms19.inapp.msg.rest.GroupchatListner;
import sms19.inapp.msg.rest.MessageEventnotificationListener;
import sms19.inapp.msg.rest.MessageEventnotificationListener.NotifyAfterMessageDeliverRead;
import sms19.inapp.msg.rest.MessageEventrequestListener;
import sms19.inapp.msg.rest.Rosterlistner;
import sms19.inapp.msg.rest.XmmpconnectionListner;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;
import sms19.listview.newproject.Home;

//---------------------------------------------------------------------------------------
public class RegisterConnectionService extends Service implements
        NotifyAfterMessageDeliverRead {
    // public XMPPConnection connection;
    public final IBinder mBinder = new ServiceBinder();
    public SharedPreferences chatPrefs;
    public MessageEventManager msgEventManager;
    public Activity runonuithread;
    public RegisterConnectionService afterThis;
    private Context context;

    private Handler mHandler = new Handler();

    private ChatManager chatManager;
    private String remote_jid = "", device_token = "", previousrowid = "0";
    private String userId = "", usernumber = "", custom_status = "";
    private AsyncTaskSendOfflineMessage asyncTaskSendOfflineMessage = null;
    private ArrayList<Chatmodel> temp = new ArrayList<Chatmodel>();
    private ArrayList<Chatmodel> buddyJID = new ArrayList<Chatmodel>();

    private AddListnerAsyncTask addListnerAsyncTask = null;
    private JoinAsyncTask joinAsyncTask = null;
    private GetContactstatusandStatus contactstatusandStatus = null;
    private Connect connectXmppMain = null;

    private final Random rndForTorCircuits = new Random();
    private ProxyInfo mProxyInfo = null;
    private static SSLContext sslContext;

    private PacketCollector mPingCollector;

    private Timer timer;
    private MyTimerTask myTimerTask;

    // /public static Handler offlineHadnler;
    // public static Handler joinHadnler;

    // Called by the interface ServiceConnected when calling the service
    public class ServiceBinder extends Binder {
        public RegisterConnectionService getService() {
            return RegisterConnectionService.this;
        }
    }

    @Override
    public IBinder onBind(Intent context) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        afterThis = this;
        GlobalData.REGISTER_CONNECTION_SERVICE_ISON = true;
        this.context = this;
        chatPrefs = getSharedPreferences("chatPrefs", MODE_PRIVATE);
        userId = Utils.getUserId(context);
        if (!(userId != null && userId.length() > 0)) {
            userId = chatPrefs.getString("userId", "");
        }
        custom_status = Utils.getUserCustomStatus(context);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Utils.isDeviceOnline(context)) {
            // try {
            // getFtpClient();
            // } catch (Exception e1) {
            //
            // e1.printStackTrace();
            // }

            GetFtpDetailsAsyncTask sync = new GetFtpDetailsAsyncTask(null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                sync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                sync.execute();
            }
        }

        // setofflineHadnler();
        // setjoinadnler();

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new Timer();

        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 2000, 7000);

        if (chatPrefs != null) {
            String userjid = chatPrefs.getString("user_jid", "");
            usernumber = userjid.split("@")[0];
            remote_jid = userjid;
            GlobalData.romote_jid = remote_jid;
            try {
                device_token = Utils.getDeviceToken(context);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                Random generator = new Random();
                int resource_int = generator.nextInt();
                Utils.saveDeviceToken(context, String.valueOf(resource_int));
                device_token = String.valueOf(resource_int);

            }

        }

        if (GlobalData.connection != null
                && GlobalData.connection.isConnected()
                && GlobalData.connection.isAuthenticated()) {

            // GlobalData.privacyManager =
            // PrivacyListManager.getInstanceFor(GlobalData.connection);

            GlobalData.connection
                    .addConnectionListener(new XmmpconnectionListner());

            try {
                // setMessageNotificationListener();

            } catch (Exception e2) {
                e2.printStackTrace();
            }

            try {
                Chatlistner.addChatlistner(context);
            } catch (Exception e2) {
                e2.printStackTrace();
            }

            AddPresenceForOnline();
            startJoinInLoop2();

            try {
                JoinMethod();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                ContactstatusandStatusMethod();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            if (Utils.isDeviceOnline(context)) {
                try {
                    connectingtoxmppserver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
        return START_STICKY/* START_NOT_STICKY */;
    }

    public void connectingtoxmppserver() {

        if (connectXmppMain == null) {
            connectXmppMain = new Connect();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                connectXmppMain
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                connectXmppMain.execute();
            }
        } else {
            // SystemClock.sleep(15000);// 20apl cmnt
            if ((!GlobalData.connection.isAuthenticated())) {
                connectXmppMain.cancel(true);
                connectXmppMain = null;
                connectXmppMain = new Connect();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    connectXmppMain
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {

                    connectXmppMain.execute();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalData.REGISTER_CONNECTION_SERVICE_ISON = false;
        Home.connectionServiceObject = null;
        if (asyncTaskSendOfflineMessage != null) {
            asyncTaskSendOfflineMessage.cancel(true);
            asyncTaskSendOfflineMessage = null;
        }
        destorAllThread();

    }

    public void destorAllThread() {

        if (addListnerAsyncTask != null) {
            addListnerAsyncTask.cancel(true);
            addListnerAsyncTask = null;
        }
        if (joinAsyncTask != null) {
            joinAsyncTask.cancel(true);
            joinAsyncTask = null;
        }
        if (contactstatusandStatus != null) {
            contactstatusandStatus.cancel(true);
            contactstatusandStatus = null;
        }
        if (connectXmppMain != null) {
            connectXmppMain.cancel(true);
            connectXmppMain = null;
        }

    }

    /*Amit connecting to xmpp server*/
    public void create() {
        try {
            try {
                if (GlobalData.connection == null) {
                    GlobalData.connection = new XMPPConnection(config());
                }

                if (GlobalData.connection != null) {
                    // GlobalData.privacyManager =
                    // PrivacyListManager.getInstanceFor(GlobalData.connection);
                    try {
                        ProviderManager.getInstance().addIQProvider("query",
                                "jabber:iq:privacy", new PrivacyProvider());
                        Class.forName(PrivacyListManager.class.getName());
                        if (!GlobalData.connection.isConnected()) {
                            GlobalData.connection.connect();
                        }
                        // Utils.setUpConnectionProperties();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Utils.setUpConnectionProperties();
                } else {
                    GlobalData.connection = new XMPPConnection(config());
                    GlobalData.connection.connect();
                    Utils.setUpConnectionProperties();
                }
            } catch (XMPPException e) {
                Utils.printLog("connection exctiption");
                Utils.printLog("XMPPClient--" + "Failed to connect to "
                        + GlobalData.connection.getHost());

                e.printStackTrace();
            }

            if (GlobalData.connection.isConnected()) {
                GlobalData.connection
                        .addConnectionListener(new XmmpconnectionListner());
            }

//			String password = chatPrefs.getString("userPassword", "");
            SharedPreferences prfs = getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);
            String password = prfs.getString("Pwd", "");
            String userLogin = prfs.getString("user_login", "Smack_chat");
            try {
                if ((!GlobalData.connection.isAuthenticated())) {
                    try {
                        try {
                            setMessageNotificationListener();
                            Chatlistner.addChatlistner(context);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        try {
                            GroupchatListner.addGroupChatlistner(context);
                            MultiUserChat.addInvitationListener(
                                    GlobalData.connection,
                                    new GroupchatInvitation());

                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }


                        GlobalData.connection.login(remote_jid, password,
                                "Smack_" + "chat" /* device_token */);
//						GlobalData.connection.login(remote_jid, password,
//								userLogin /* device_token */);
                        // GlobalData.connection.login(remote_jid,
                        // password,"Spark 2.7.0");

                        Log.i("xmpp","login sucessfully  - "+  GlobalData.connection.getUser());

                        Utils.printLog("login successfully...");
                    } catch (XMPPException e1) {
                        if (e1.getMessage().equalsIgnoreCase(
                                "No response from the server.")) {
                            Utils.offlineShowUser();
                            connectingtoxmppserver();
                            return;
                        }
                        if (!e1.getMessage()
                                .equalsIgnoreCase(
                                        "SASL authentication failed using mechanism DIGEST-MD5")) {
                            Utils.offlineShowUser();
                            connectingtoxmppserver();
                            return;
                        }
                        e1.printStackTrace();
                    }

                    try {
                        Utils.XMPPAddNewPrivacyList(GlobalData.connection,
                                remote_jid);// get block users list
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    AddPresenceForOnline();
                    Utils.printLog("Roster entry start");
                }
            } catch (Exception e) {
                Utils.printLog("login ");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addListnerWithAsyncTask() {

        if (addListnerAsyncTask == null) {
            addListnerAsyncTask = new AddListnerAsyncTask();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                addListnerAsyncTask
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                addListnerAsyncTask.execute();
            }
        } else {
            addListnerAsyncTask.cancel(true);
            addListnerAsyncTask = null;
            addListnerAsyncTask = new AddListnerAsyncTask();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                addListnerAsyncTask
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                addListnerAsyncTask.execute();
            }
        }
    }

    @Override
    public void notifyMsgStatus(String remot_id, String msgpacket_id,
                                String msg_status) {

        if (GlobalData.dbHelper == null && context != null) {
            try {
                GlobalData.dbHelper = new DatabaseHelper(context, false);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        try {
            if (GlobalData.dbHelper != null) {
                if (!GlobalData.dbHelper.isBlockedUserPacketId(msgpacket_id)) {// if
                    // this
                    // packet
                    // id
                    // have
                    // sent
                    // msg
                    // value
                    // 2
                    // mena
                    // user
                    // is
                    // block
                    GlobalData.dbHelper.updatestatusOfSentMessage(
                            StringUtils.parseBareAddress(remot_id),
                            msgpacket_id, msg_status);

                    if (SingleChatRoomFrgament.obj != null) {
                        SingleChatRoomFrgament.obj.messagePrint(msgpacket_id,
                                msg_status);
                    } else if (msg_status.equalsIgnoreCase("deliver")) {
                        if (BroadCastGroupSecond.obj != null) {
                            BroadCastGroupSecond.obj.messagePrint(msgpacket_id,
                                    msg_status);
                        }
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public ConnectionConfiguration config() {
        // setProxy(TorProxyInfo.PROXY_TYPE, GlobalData.HOST, GlobalData.PORT);
        ConnectionConfiguration connConfig = new ConnectionConfiguration(
                GlobalData.HOST, GlobalData.PORT);
        // ConnectionConfiguration connConfig = new
        // ConnectionConfiguration(GlobalData.HOST, GlobalData.PORT);

        SmackConfiguration.setPacketReplyTimeout(20 * 1000);
        connConfig.setSendPresence(false);

        // PingManager pingManager = PingManager.getInstanceFor(connection);

        connConfig.setSASLAuthenticationEnabled(true);
        // connConfig.setReconnectionAllowed(false);
        connConfig.setReconnectionAllowed(true);// new
        connConfig.setCompressionEnabled(false);
        connConfig.setSecurityMode(SecurityMode.disabled);
        // connConfig.setSecurityMode(SecurityMode.enabled);
        // connConfig.setSocketFactory(SSLSocketFactory.getDefault());// new 5
        // apl

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

        return connConfig;

    }

    public void addChatListner() {

        if (GlobalData.connection != null
                && GlobalData.connection.isConnected()
                && GlobalData.connection.isAuthenticated()) {

            try {
                try {
                    startJoinInLoop();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

                Utils.printLog("register chat listner  added...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setMessageNotificationListener() {

        if (GlobalData.connection != null) {
            msgEventManager = new MessageEventManager(GlobalData.connection);

            Utils.printLog("Set MessageEventRequestListener");
            msgEventManager
                    .addMessageEventRequestListener(new MessageEventrequestListener());
            if (afterThis != null) {
                msgEventManager
                        .addMessageEventNotificationListener(new MessageEventnotificationListener(
                                runonuithread, afterThis));
            }
        }
        // ///////////////////////////////////////
    }

    public synchronized void addingRosterentry() {

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

        }
    }

    public boolean createrosterentry(Roster roster, String jid, String fname,
                                     int retrycount) {

        try {
            roster.createEntry(jid, fname, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return retrycount == 2;
        }
    }



    public void logout() {

        try {
            Presence presence = new Presence(Presence.Type.unavailable);

            try {

                GlobalData.connection.sendPacket(presence);

                GlobalData.loginSuccess = false;
            } catch (Exception exception) {
                exception.printStackTrace();
                GlobalData.connection.disconnect();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            GlobalData.connection.disconnect();
        }

        //Log.w("LogOut", "LogOut");

    }

    private class AddListnerAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                addChatListner();

            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {

                sendOffLineMessageMethodWithAsyncTaskState();

            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    }

    public void sendOffLineMessageMethodWithAsyncTaskState() {
        try {

			/*
             * if(offlineHadnler!=null){ Message msg = new Message(); Bundle b =
			 * new Bundle(); b.putString("sentmsg", "1");
			 * 
			 * msg.setData(b); offlineHadnler.sendMessage(msg);// comment m }
			 */

            if (asyncTaskSendOfflineMessage == null) {
                asyncTaskSendOfflineMessage = new AsyncTaskSendOfflineMessage();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    asyncTaskSendOfflineMessage
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    asyncTaskSendOfflineMessage.execute();
                }
            } else {

                asyncTaskSendOfflineMessage.cancel(true);
                asyncTaskSendOfflineMessage = null;
                asyncTaskSendOfflineMessage = new AsyncTaskSendOfflineMessage();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    asyncTaskSendOfflineMessage
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    asyncTaskSendOfflineMessage.execute();
                }

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void JoinMethod() {

        if (joinAsyncTask == null) {
            joinAsyncTask = new JoinAsyncTask();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                joinAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                joinAsyncTask.execute();
            }
        } else {
            joinAsyncTask.cancel(true);
            joinAsyncTask = null;
            joinAsyncTask = new JoinAsyncTask();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                joinAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                joinAsyncTask.execute();
            }
        }

    }

    private class JoinAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                // runForAddAddJionGroup();
                // startJoinInLoop();

            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

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

    private class AddRoasterAsyncTask extends AsyncTask<String, Void, String> {

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

    private class Connect extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            create();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if (GlobalData.connection.isConnected()
                        && (GlobalData.connection.isAuthenticated())) {
                    //AddPresenceForOnline();
                }

                addListnerWithAsyncTask();

            } catch (Exception e) {

                e.printStackTrace();
            }

            try {
                JoinMethod();

            } catch (Exception e1) {

                e1.printStackTrace();
            }

            try {
                ContactstatusandStatusMethod();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    /*
     * public void getStatusFromService(){
     *
     * new AddListnerAsyncTask().execute(); }
     */
    public void setConnection(XMPPConnection connection) {
        GlobalData.connection = connection;
        if (connection != null) {

        }
    }



    private void startJoinInLoop() {
        try {
            if (GlobalData.connection != null
                    && GlobalData.connection.isConnected()) {
                long savedmillisec = chatPrefs.getLong("lastleaveTime", 0);

                long totalsec = 0;
                if (savedmillisec != 0) {
                    long diffmillsec = Utils.getGroupLeavTime() - savedmillisec;
                    totalsec = TimeUnit.MILLISECONDS.toSeconds(diffmillsec);
                }

                ArrayList<String> grouplist = GlobalData.dbHelper
                        .getMygroupfromDB();
                GlobalData.globalMucChat = new HashMap<String, MultiUserChat>();

                if (grouplist != null && grouplist.size() > 0) {
                    for (int i = 0; i < grouplist.size(); i++) {

                        try {
                            String groupid = grouplist.get(i);
                            int retrycount = 0;
                            boolean success = joingroup(groupid, totalsec,
                                    retrycount);
                            while (!success) {
                                SystemClock.sleep(100);
                                retrycount++;

                                if (!GlobalData.dbHelper
                                        .groupIsBlockNew(groupid)) {
                                    success = joingroup(groupid, totalsec,
                                            retrycount);
                                } else {
                                    success = joingroup(groupid, totalsec,
                                            retrycount);
                                    break;
                                }
                                if (retrycount == 2) {
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                    Home.groupjoiningrunning = false;
                } else {
                    Home.groupjoiningrunning = false;
                }

            } else {
                Home.groupjoiningrunning = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startJoinInLoop2() {
        try {
            if (GlobalData.connection != null
                    && GlobalData.connection.isConnected()) {
                long savedmillisec = chatPrefs.getLong("lastleaveTime", 0);

                long totalsec = 0;
                if (savedmillisec != 0) {
                    long diffmillsec = /* System.currentTimeMillis() */Utils
                            .getGroupLeavTime() - savedmillisec;
                    totalsec = TimeUnit.MILLISECONDS.toSeconds(diffmillsec);
                }

                ArrayList<String> grouplist = GlobalData.dbHelper
                        .getMygroupfromDB();
                if (grouplist != null && grouplist.size() > 0) {
                    for (int i = 0; i < grouplist.size(); i++) {

                        try {
                            String groupid = grouplist.get(i);
                            int retrycount = 0;
                            boolean success = joingroup(groupid, totalsec,
                                    retrycount);
                            while (!success) {
                                SystemClock.sleep(100);
                                retrycount++;
                                if (!GlobalData.dbHelper
                                        .groupIsBlockNew(groupid)) {
                                    success = joingroup(groupid, totalsec,
                                            retrycount);
                                } else {
                                    success = joingroup(groupid, totalsec,
                                            retrycount);
                                    break;
                                }
                                if (retrycount == 2) {
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                    Home.groupjoiningrunning = false;
                } else {
                    Home.groupjoiningrunning = false;
                }

            } else {
                Home.groupjoiningrunning = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean joingroup(String groupid, long totalsec, int retrycount) {
        try {
            MultiUserChat mucChat = new MultiUserChat(GlobalData.connection,
                    groupid);
            if (mucChat != null) {

                // mucChat.addParticipantStatusListener(new
                // Groupchatparticipentchange());
                // mucChat.addUserStatusListener(new
                // GroupChatMyStatusListner());
                DiscussionHistory history = new DiscussionHistory();
                history.setSeconds((int) totalsec);

                mucChat.join(remote_jid, null, history, 5000);
                Utils.printLog("Join group after " + totalsec + " , " + groupid
                        + " successfully.");
                GlobalData.globalMucChat.put(groupid, mucChat);

                // }

            }
            return true;

        } catch (XMPPException e) {
            Utils.printLog("Join group " + groupid + " Excption.");
            e.printStackTrace();
            if (GlobalData.connection != null
                    && GlobalData.connection.isConnected()
                    && GlobalData.connection.isAuthenticated()) {
                if (e.getXMPPError() != null
                        && e.getXMPPError().toString()
                        .equalsIgnoreCase("forbidden(403)")) {
                    GlobalData.dbHelper.groupBlocknewfromDB(groupid);
                }
            }

            return retrycount == 2;
        }
    }

    public MultiUserChat getMuc(String groupid, long totalsec, int retrycount) {
        try {
            MultiUserChat mucChat = null;

            mucChat = new MultiUserChat(GlobalData.connection, groupid);
            DiscussionHistory history = new DiscussionHistory();
            history.setSeconds(0);
            if (!GlobalData.dbHelper.groupUserIsBlock(groupid)) {
                try {
                    mucChat.join(remote_jid, null, history,
                            SmackConfiguration.getPacketReplyTimeout());
                } catch (XMPPException e) {

                    e.printStackTrace();
                    mucChat = null;
                }

                Utils.printLog("Join group after " + totalsec + " , " + groupid
                        + " successfully.");
            }
            return mucChat;

        } catch (Exception e) {
            Utils.printLog("Join group " + groupid + " Excption.");
            e.printStackTrace();
            if (retrycount == 2) {
                return null;
            } else {
                return null;
            }
        }
    }

    public class AsyncTaskSendOfflineMessage extends
            AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (GlobalData.connection != null
                    && GlobalData.connection.isConnected()
                    && GlobalData.connection.isAuthenticated()) {
                // setMessageNotificationListener();
                sendOfflineMessage();
            }
            return null;
        }

    }

    @SuppressWarnings("static-access")
    public synchronized void sendOfflineMessage() {

        Utils.printLog(" offline msg call===");

        try {
            temp = new ArrayList<Chatmodel>();
            temp.clear();
            temp.addAll(GlobalData.dbHelper.getChathistoryfromDBOffline(
                    remote_jid, previousrowid));
        } catch (Exception e) {

            e.printStackTrace();
        }
        try {
            buddyJID = new ArrayList<Chatmodel>();
            buddyJID.clear();
            buddyJID.addAll(temp);
        } catch (Exception e) {

            e.printStackTrace();
        }

        chatManager = GlobalData.connection.getChatManager();

        if (chatManager != null) {
            if (buddyJID != null) {
                if (GlobalData.clientFtp != null) {
                    if (GlobalData.clientFtp.isConnected()) {
                        try {
                            GlobalData.clientFtp = null;
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                }
                for (int i = buddyJID.size() - 1; i >= 0; i--) {

                    if (!buddyJID.get(i).getMediapath().equalsIgnoreCase("")) {

                        String stringCheckIsGroup = buddyJID.get(i)
                                .getRemote_userid().split("@")[1];
                        int isGroup = 0;
                        if (stringCheckIsGroup.startsWith("conference")) {
                            isGroup = 1;
                        } else if (stringCheckIsGroup.startsWith("Broadcast")) {
                            isGroup = 2;
                        }

                        final Object[] iObj = new Object[6];
                        int isGroupNew = isGroup;
                        String path = buddyJID.get(i).getMediapath();
                        String getMediatype = buddyJID.get(i).getMediatype();
                        String getRemote_userid = buddyJID.get(i)
                                .getRemote_userid();
                        Chatmodel chatmodel = buddyJID.get(i);

                        iObj[0] = isGroupNew;
                        iObj[1] = path;
                        iObj[2] = getMediatype;
                        iObj[3] = getRemote_userid;
                        iObj[4] = chatmodel;
                        loadimage(isGroup, buddyJID.get(i).getMediapath(),
                                buddyJID.get(i).getMediatype(), buddyJID.get(i)
                                        .getRemote_userid(), buddyJID.get(i));

                    } else {

                        String stringCheckIsGroup = buddyJID.get(i)
                                .getRemote_userid().split("@")[1];
                        int isGroup = 0;
                        if (stringCheckIsGroup.startsWith("conference")) {
                            isGroup = 1;
                        } else if (stringCheckIsGroup.startsWith("Broadcast")) {
                            isGroup = 2;
                        }

                        if (isGroup == 0) {

                            Chat chat = chatManager.createChat(buddyJID.get(i)
                                    .getRemote_userid(), null);

                            // org.jivesoftware.smack.packet.Message msg =
                            // chat.generateMessage(buddyJID.get(i).getChatmessage());

                            Utils.printLog("setBody==="
                                    + buddyJID.get(i).getChatmessage());

                            String timeStap = Utils.currentTimeStap();

                            timeStap = buddyJID.get(i).getMsgtime();

                            Slacktags slagTag = new Slacktags(usernumber,
                                    userId, GlobalData.MESSAGE_TYPE_2,
                                    timeStap, userId + "_" + timeStap);
                            org.jivesoftware.smack.packet.Message msg2 = new org.jivesoftware.smack.packet.Message();
                            msg2.setType(org.jivesoftware.smack.packet.Message.Type.normal);
                            msg2.setBody(buddyJID.get(i).getChatmessage());
                            msg2.addExtension(slagTag);

                            MessageEventManager eventManager = new MessageEventManager(
                                    GlobalData.connection);
                            eventManager.addNotificationsRequests(msg2, true,
                                    true, true, false);
                            String id = msg2.getPacketID();
                            String remote_jid = "";
                            String offlineid = "";
                            try {
                                if (chat != null) {

                                    try {
                                        String rowId = buddyJID.get(i)
                                                .getMessagerowid();
                                        remote_jid = buddyJID.get(i)
                                                .getRemote_userid();
                                        offlineid = buddyJID.get(i)
                                                .getRow_offlineId();

                                        chat.sendMessage(msg2);
                                        GlobalData.dbHelper
                                                .deleteSingleOfflinemsgrow(/*
                                                                             * buddyJID
																			 * .
																			 * get
																			 * (
																			 * i
																			 * )
																			 * .
																			 * getMessagerowid
																			 * (
																			 * )
																			 */rowId);
                                        GlobalData.dbHelper
                                                .updateSentMessagePacketIdRowIdBase(
                                                        remote_jid, id, "1",
                                                        offlineid);
                                    } catch (XMPPException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    Utils.printLog("sending offline msg==="
                                            + String.valueOf(i) + "---"
                                            + buddyJID.get(i).getChatmessage());

                                }

                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                            // Utils.printLog("setXMLtttt===" + msg.toXML());

                            if (SingleChatRoomFrgament.ON_SINGLE_CHAT_PAGE) {

                                SingleChatRoomFrgament singleChatRoomFrgament = SingleChatRoomFrgament
                                        .newInstance("");
                                if (singleChatRoomFrgament != null) {
                                    if (remote_jid
                                            .equalsIgnoreCase(singleChatRoomFrgament
                                                    .getRemote_jid())) {
                                        singleChatRoomFrgament
                                                .refreshChatAdapter();
                                    }
                                }

                            }

                        } else if (isGroup == 1) {

                            long totalsec = 0;
                            MultiUserChat mucChat = null;

                            int retrycount = 0;
                            mucChat = getMuc(
                                    buddyJID.get(i).getRemote_userid(),
                                    totalsec, retrycount);
                            if (mucChat != null) {

                                // String timeStap= Utils.currentTimeStamp();
                                String timeStap = Utils.currentTimeStap();

                                timeStap = buddyJID.get(i).getMsgtime();

                                Slacktags slagTag = new Slacktags(usernumber,
                                        userId, GlobalData.MESSAGE_TYPE_2,
                                        timeStap, userId + "_" + timeStap);

                                org.jivesoftware.smack.packet.Message msg = mucChat
                                        .createMessage();
                                msg.setBody(buddyJID.get(i).getChatmessage());
                                msg.addExtension(slagTag);
                                msg.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
                                try {
                                    mucChat.sendMessage(msg);

                                    // GlobalData.dbHelper.updateSentMessagePacketId(buddyJID.get(i).getRemote_userid(),
                                    // "", "1");
                                    GlobalData.dbHelper
                                            .updateSentMessagePacketIdRowIdBase(
                                                    buddyJID.get(i)
                                                            .getRemote_userid(),
                                                    "", "1", buddyJID.get(i)
                                                            .getRow_offlineId());
                                    GlobalData.dbHelper
                                            .deleteSingleOfflinemsgrow(buddyJID
                                                    .get(i).getMessagerowid());

                                } catch (XMPPException e) {

                                    e.printStackTrace();
                                }

                            }
                        } else if (isGroup == 2) {

                            final int posnew = i;

                            new Thread(new Runnable() {

                                @Override
                                public void run() {

                                    ArrayList<String> broadcastusersid = GlobalData.dbHelper
                                            .getBroadcastmembersfromDB(buddyJID
                                                    .get(posnew)
                                                    .getRemote_userid());
                                    if (broadcastusersid != null) {
                                        if (broadcastusersid.size() > 0) {

                                            for (int j = 0; j < broadcastusersid
                                                    .size(); j++) {
                                                String jid = broadcastusersid
                                                        .get(j);
                                                Chat chat = chatManager
                                                        .createChat(jid, null);
                                                try {
                                                    if (chat != null) {

                                                        // String timeStap=
                                                        // Utils.currentTimeStamp();
                                                        String timeStap = Utils
                                                                .currentTimeStap();
                                                        Slacktags slagTag = new Slacktags(
                                                                usernumber,
                                                                userId,
                                                                GlobalData.MESSAGE_TYPE_2,
                                                                timeStap,
                                                                userId
                                                                        + "_"
                                                                        + timeStap);
                                                        org.jivesoftware.smack.packet.Message msg = new org.jivesoftware.smack.packet.Message();
                                                        msg.setType(org.jivesoftware.smack.packet.Message.Type.chat);
                                                        msg.setBody(buddyJID
                                                                .get(posnew)
                                                                .getChatmessage());
                                                        msg.addExtension(slagTag);
                                                        chat.sendMessage(msg);
                                                        // GlobalData.dbHelper.updateSentMessagePacketId(buddyJID.get(posnew).getRemote_userid(),
                                                        // "", "1");
                                                        // GlobalData.dbHelper.updateSentMessagePacketIdRowIdBase(buddyJID.get(posnew).getRemote_userid(),
                                                        // "",
                                                        // "1",buddyJID.get(posnew).getRow_offlineId());
                                                        GlobalData.dbHelper
                                                                .deleteSingleOfflinemsgrow(buddyJID
                                                                        .get(posnew)
                                                                        .getMessagerowid());

                                                        Utils.printLog("broadcast send to... "
                                                                + jid);
                                                    }
                                                } catch (Exception e) {

                                                    Utils.printLog("broadcast send excption...");
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }
                            }).start();
                        }
                    }
                    SystemClock.sleep(500);
                }
            }
        }
    }



    private org.jivesoftware.smack.packet.Message messageNew = null;
    private String base64string = "";
    private String imagetoupload2 = "";
    private String setimege = "";
    /*
     * private String FTP_USER =""; private String FTP_PASS =""; private String
     * FTP_HOST=""; private String FTP_URL=""; private FTPClient clientFtp;
     */
    private String sendurlpath = "";
    private String fileType = "";
    public static String sendfilefixString = "file__";
    private long rowid = 0;

    public String sendVideofile(String videoPath) {
        String videothmbpath = "";

        Bitmap finalBitmap = ThumbnailUtils.createVideoThumbnail(videoPath,
                MediaStore.Images.Thumbnails.MINI_KIND);
        try {
            videothmbpath = Utils.getfilePath(GlobalData.Imagefile);
            FileOutputStream fos = new FileOutputStream(videothmbpath);

            finalBitmap.compress(CompressFormat.JPEG, 80, fos);

            fos.close();
            if (finalBitmap != null) {
                finalBitmap.recycle();
            }
            if (videoPath != null && videoPath.trim().length() != 0) {
                if (new File(videoPath).exists()) {
                    fileType = GlobalData.Videofile;

                }
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return videothmbpath;

    }



    private void loadimage(final int isGroup, final String imagetoupload,
                           String filetypeNew, final String user_jid, final Chatmodel chatmodel) {
        try {

            fileType = filetypeNew;
            setimege = "";
            sendurlpath = "";
            imagetoupload2 = "";
            String filepath = "";
            String filetype = "";
            messageNew = null;

            filepath = imagetoupload;
            filetype = filetypeNew;
            base64string = "";
            filetypeNew = "image";

            if (fileType.equals(GlobalData.Videofile)) {

                String videothmbpath = sendVideofile(imagetoupload);
                Bitmap bit = Utils.decodeFile(videothmbpath,
                        GlobalData.filetransferthmb,
                        GlobalData.filetransferthmb);
                filetypeNew = "video";

                base64string = Utils.convertTobase64string(bit);
            } else if (!fileType.equals(GlobalData.Audiofile)) {

                Bitmap bit = Utils.decodeFile(filepath,
                        GlobalData.filetransferthmb,
                        GlobalData.filetransferthmb);
                base64string = Utils.convertTobase64string(bit);

            }

            if (fileType.equals(GlobalData.Audiofile)) {
                filetypeNew = "audio";
            }

            if (fileType.equals(GlobalData.Imagefile)) {
                Bitmap bit = Utils.decodeFile(imagetoupload,
                        GlobalData.profilepicthmb, GlobalData.profilepicthmb);
                base64string = Utils.convertTobase64string(bit);
            }

            imagetoupload2 = imagetoupload.substring(imagetoupload
                    .lastIndexOf("/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setimege = imagetoupload;

        // sendurlpath= GlobalData.FTP_URL+userId+"/"+ imagetoupload2;
        sendurlpath = GlobalData.FTP_HOST + "/" + userId + "/" + imagetoupload2;

        String filepath = "";

        filepath = setimege;

        //Log.w("LOAD_IMAGE_URL", "LOAD_IMAGE_URL" + sendurlpath);

        String msg = "";
        if (fileType.equals(GlobalData.Audiofile)) {
            msg = sendfilefixString + sendurlpath + "__" + fileType;

        } else if (fileType.equals(GlobalData.Locationfile)) {
            msg = sendfilefixString + sendurlpath + "__" + fileType + "__"
                    + base64string + "__" + "";

        } else {
            msg = sendfilefixString + sendurlpath + "__" + fileType + "__"
                    + base64string;
        }

        try {
            messageNew = sendMessageBeforeUpload(user_jid, isGroup, msg,
                    user_jid, filepath, fileType, chatmodel);
        } catch (XMPPException e1) {
            e1.printStackTrace();
        }
        final File f = new File(imagetoupload);
        try {
            clientConnection();
            ExecutorService executor = Executors.newFixedThreadPool(5);
            Object[] iObj = new Object[6];
            iObj[0] = f;
            iObj[1] = GlobalData.clientFtp;
            iObj[2] = messageNew;
            iObj[3] = filepath;
            iObj[4] = user_jid;
            iObj[5] = chatmodel;
            Runnable worker = new UploadThread(iObj);
            executor.execute(worker);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                GlobalData.clientFtp.disconnect(true);
            } catch (Exception e2) {

            }
        }

    }

    public class UploadThread implements Runnable {

        Object object[] = null;
        File file = null;
        org.jivesoftware.smack.packet.Message message = null;
        FTPClient clientFtpNew = null;
        String imagetoupload = "";
        String jid = "";
        Chatmodel chatmodel = null;
        int isGroup = 0;

        public UploadThread(Object[] s) {

            object = s;
            file = (File) object[0];
            clientFtpNew = (FTPClient) object[1];
            message = (org.jivesoftware.smack.packet.Message) object[2];
            imagetoupload = (String) object[3];
            jid = (String) object[4];
            chatmodel = (Chatmodel) object[5];

            try {
                String stringCheckIsGroup = jid.split("@")[1];

                if (stringCheckIsGroup.startsWith("conference")) {
                    isGroup = 1;
                } else if (stringCheckIsGroup.startsWith("Broadcast")) {
                    isGroup = 2;
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        @Override
        public void run() {

            MyTransferListener listener = new MyTransferListener(message, jid,
                    chatmodel, isGroup, chatmodel.getRow_offlineId());
            try {
                clientFtpNew.upload(file, listener);

            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
            } catch (FTPException e) {
                e.printStackTrace();
            } catch (FTPDataTransferException e) {
                e.printStackTrace();
            } catch (FTPAbortedException e) {
                e.printStackTrace();
            }

        }

        @Override
        public String toString() {
            return "";
        }
    }

    public class MyTransferListener implements FTPDataTransferListener {
        org.jivesoftware.smack.packet.Message message;
        String jid = "";
        Chatmodel chatmodel;
        int isgroup = 0;
        String offlineid = "";

        public MyTransferListener(
                org.jivesoftware.smack.packet.Message message, String jid,
                Chatmodel chatmodel, int isgroup, String offlineid) {
            this.message = message;
            this.jid = jid;
            this.chatmodel = chatmodel;
            this.isgroup = isgroup;
            this.offlineid = offlineid;
        }

        public void started() {
        }

        public void transferred(int length) {
            //Log.w("transferred", "transferred=======" + String.valueOf(length));

        }

        public void completed() {

            try {

                if (chatManager != null) {

                    if (isgroup == 0) {
                        Chat chat = chatManager.createChat(jid, null);
                        GlobalData.dbHelper.updateSentMessagePacketIdRowIdBase(
                                jid, message.getPacketID(), "1", offlineid);
                        if (SingleChatRoomFrgament.offlineChatHandler != null) {
                            Message msg = new Message();
                            Bundle b = new Bundle();
                            b.putString("sentoffline", "1");
                            b.putString("time", chatmodel.getMsgtime());
                            b.putString("packetid", message.getPacketID());
                            msg.setData(b);
                            SingleChatRoomFrgament.offlineChatHandler
                                    .sendMessage(msg);// comment m
                        }

                        chat.sendMessage(message);
                        try {
                            GlobalData.dbHelper
                                    .deleteSingleOfflinemsgrow(chatmodel
                                            .getMessagerowid());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

						/*
                         * if(SingleChatRoomFrgament.ON_SINGLE_CHAT_PAGE){
						 * SingleChatRoomFrgament
						 * singleChatRoomFrgament=SingleChatRoomFrgament
						 * .newInstance(""); if(singleChatRoomFrgament!=null){
						 * if(remote_jid.trim().equalsIgnoreCase(
						 * singleChatRoomFrgament.getRemote_jid())){
						 * singleChatRoomFrgament.refreshChatAdapter(); } }
						 * 
						 * }
						 */

                    } else if (isgroup == 1) {

                        long totalsec = 0;
                        int retrycount = 0;
                        MultiUserChat mucChat = getMuc(jid, totalsec,
                                retrycount);
                        if (mucChat != null) {
                            try {
                                mucChat.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            GlobalData.dbHelper
                                    .deleteSingleOfflinemsgrow(chatmodel
                                            .getMessagerowid());

                        }
                    } else if (isgroup == 2) {
                        ArrayList<String> broadcastusersid;
                        Utils.printLog("broadcast send start...");
                        broadcastusersid = GlobalData.dbHelper
                                .getBroadcastmembersfromDB(jid);
                        for (int i = 0; i < broadcastusersid.size(); i++) {
                            String jid = broadcastusersid.get(i);
                            Chat chat = chatManager.createChat(jid, null);
                            if (chat != null) {
                                try {
                                    if (i == 0) {
                                        MessageEventManager eventManager = new MessageEventManager(
                                                GlobalData.connection);
                                        MessageEventManager.addNotificationsRequests(
                                                message, true, true, true,
                                                false);
                                    }
                                    chat.sendMessage(message);
                                    Utils.printLog("broadcast send to... "
                                            + jid);
                                } catch (Exception e) {

                                    Utils.printLog("broadcast send excption...");
                                    e.printStackTrace();
                                }
                            }

                        }
                        GlobalData.dbHelper.deleteSingleOfflinemsgrow(chatmodel
                                .getMessagerowid());
                        GlobalData.dbHelper.updateSentMessagePacketIdRowIdBase(
                                jid, message.getPacketID(), "1", offlineid);
                        Utils.printLog("broadcast send complete...");
                    }

                }

            } catch (XMPPException e) {
                Utils.printLog("Failed send  file==");
                e.printStackTrace();
            }

        }

        public void aborted() {

        }

        public void failed() {

        }

    }

    public void clientConnection() {

        try {
            if (GlobalData.clientFtp == null) {
                GlobalData.clientFtp = new FTPClient();
            }
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            if (!GlobalData.clientFtp.isConnected()) {
                GlobalData.clientFtp.connect(GlobalData.FTP_HOST, 21);
                GlobalData.clientFtp.login(GlobalData.FTP_USER,
                        GlobalData.FTP_PASS);
                GlobalData.clientFtp.setType(FTPClient.TYPE_BINARY);

                try {

                    String test = GlobalData.clientFtp.currentDirectory();
                    GlobalData.clientFtp.changeDirectory(test + userId);
                } catch (IllegalStateException e3) {
                    // TODO Auto-generated catch block
                    e3.printStackTrace();
                } catch (IOException e3) {
                    // TODO Auto-generated catch block
                    e3.printStackTrace();
                } catch (FTPIllegalReplyException e3) {
                    // TODO Auto-generated catch block
                    e3.printStackTrace();
                } catch (FTPException e3) {
                    // TODO Auto-generated catch block
                    e3.printStackTrace();
                }

            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
        } catch (FTPException e) {
            e.printStackTrace();
        }

    }

    public org.jivesoftware.smack.packet.Message sendMessageBeforeUpload(
            String newRemoteId, int isgroup, final String message,
            String buddyJID, String filePath, String fileTy,
            final Chatmodel chatmodel) throws XMPPException {
        org.jivesoftware.smack.packet.Message msgpacketid = null;

        long totalsec = 0;
        Utils.printLog("message send to server..and isgroup is" + isgroup);

        if (isgroup == 1) {

            int retrycount = 0;
            MultiUserChat mucChat = getMuc(newRemoteId, totalsec, retrycount);

            if (mucChat != null) {
                String timeStap = Utils.currentTimeStap();
                if (chatmodel.getMsgtime() != null) {
                    if (chatmodel.getMsgtime() != null) {
                        timeStap = chatmodel.getMsgtime();
                    }
                }

                String phonenumber = "";
                try {
                    phonenumber = remote_jid.split("@")[0];
                } catch (Exception e) {

                    e.printStackTrace();
                }
                Slacktags slagTag = new Slacktags(phonenumber, userId,
                        GlobalData.MESSAGE_TYPE_2, timeStap, userId + "_"
                        + timeStap);
                org.jivesoftware.smack.packet.Message msg = mucChat
                        .createMessage();
                msg.setBody(message);
                msg.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
                msg.addExtension(slagTag);
                msgpacketid = msg;

            }
        } else {

            if (isgroup == 2) {
                if (chatManager != null) {

                    Utils.printLog("broadcast send start...");
                    GlobalData.dbHelper.getBroadcastmembersfromDB(remote_jid);
                    try {
                        String timeStap = Utils.currentTimeStap();
                        String phonenumber = "";
                        try {
                            phonenumber = remote_jid.split("@")[0];
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (chatmodel.getMsgtime() != null) {
                            if (chatmodel.getMsgtime() != null) {
                                timeStap = chatmodel.getMsgtime();
                            }
                        }
                        Slacktags slagTag = new Slacktags(phonenumber, userId,
                                GlobalData.MESSAGE_TYPE_2, timeStap, userId
                                + "_" + timeStap);
                        org.jivesoftware.smack.packet.Message msg = new org.jivesoftware.smack.packet.Message();
                        msg.setType(org.jivesoftware.smack.packet.Message.Type.chat);
                        msg.setBody(message);
                        msg.addExtension(slagTag);
                        msgpacketid = msg;

                    } catch (Exception e) {
                        Utils.printLog("broadcast send excption...");
                        e.printStackTrace();
                    }
                }

            } else {
                if (chatManager != null) {

                    String timeStap = Utils.currentTimeStap();
                    if (chatmodel.getMsgtime() != null) {
                        if (chatmodel.getMsgtime() != null) {
                            timeStap = chatmodel.getMsgtime();
                        }
                    }

                    String phonenumber = "";
                    try {
                        phonenumber = remote_jid.split("@")[0];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Slacktags slagTag = new Slacktags(phonenumber, userId,
                            GlobalData.MESSAGE_TYPE_2, timeStap, userId + "_"
                            + timeStap);
                    org.jivesoftware.smack.packet.Message msg2 = new org.jivesoftware.smack.packet.Message();
                    msg2.setType(org.jivesoftware.smack.packet.Message.Type.normal);
                    msg2.setBody(message);
                    msg2.addExtension(slagTag);
                    MessageEventManager eventManager = new MessageEventManager(
                            GlobalData.connection);
                    MessageEventManager.addNotificationsRequests(msg2, true, true,
                            true, false);

                    if (message != null) {
                        try {
                            GlobalData.dbHelper
                                    .updateSentMessagePacketIdRowIdBase(
                                            chatmodel.getRemote_userid(),
                                            msg2.getPacketID(), "1",
                                            chatmodel.getRow_offlineId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Utils.printLog("setXMLtttt===" + msg2.toXML());
                    msgpacketid = msg2;

                }
            }
        }
        return msgpacketid;
    }



    private void ContactstatusandStatusMethod() {

        if (contactstatusandStatus == null) {
            contactstatusandStatus = new GetContactstatusandStatus();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                contactstatusandStatus
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                contactstatusandStatus.execute();
            }
        } else {
            contactstatusandStatus.cancel(true);
            contactstatusandStatus = null;
            contactstatusandStatus = new GetContactstatusandStatus();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                contactstatusandStatus
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                contactstatusandStatus.execute();
            }
        }
    }

    class GetContactstatusandStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
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

                                if (chatPrefs.getBoolean("firsttime", false)) {

                                    GlobalData.dbHelper.updateContactdata(
                                            remote_jid, status, byteArray,
                                            picname, 1, false);

                                } else {
                                    GlobalData.dbHelper.updateContactdata(
                                            remote_jid, status, byteArray,
                                            picname, 0, false);
                                }

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
            return null;
        }
    }

    public boolean sendsubrequest(String remote_jid, ItemType subtype,
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



    public void AddPresenceForOnline() {

        // Presence presence = new Presence(Presence.Type.available);
        Presence presence = null;
        // presence = new Presence(Presence.Type.available);
        if (custom_status.equalsIgnoreCase("")) {
            custom_status = GlobalData.AVAILABLE;
            presence = new Presence(Presence.Type.available, custom_status
                    + GlobalData.status_time_separater + "", 0, Mode.available);
            presence.setStatus(GlobalData.AVAILABLE
                    + GlobalData.status_time_separater + "");
        } else {

            if (GlobalData.AVAILABLE.equalsIgnoreCase(custom_status)) {
                presence = new Presence(Presence.Type.available, custom_status
                        + GlobalData.status_time_separater + "", 0,
                        Mode.available);
                presence.setStatus(GlobalData.AVAILABLE
                        + GlobalData.status_time_separater + "");
            } else if (GlobalData.BUSY.equalsIgnoreCase(custom_status)) {
                presence = new Presence(Presence.Type.available, "busy"
                        + GlobalData.status_time_separater + "", 0,
                        Mode.available);
                presence.setStatus(GlobalData.BUSY
                        + GlobalData.status_time_separater + "");
            } else if (GlobalData.INVISIABLE.equalsIgnoreCase(custom_status)) {
                presence = new Presence(Presence.Type.available, "invisiable"
                        + GlobalData.status_time_separater + "", 0,
                        Mode.available);
                presence.setStatus(GlobalData.INVISIABLE
                        + GlobalData.status_time_separater + "");
            } else if (GlobalData.ONLY.equalsIgnoreCase(custom_status)) {
                presence = new Presence(Presence.Type.available, "original"
                        + GlobalData.status_time_separater + "", 0,
                        Mode.available);
                presence.setStatus(GlobalData.ONLY
                        + GlobalData.status_time_separater + "");
            }
            // presence = new Presence(Presence.Type.available,
            // custom_status+GlobalData.status_time_separater+"", 1,
            // Mode.available);
        }
        // presence.setStatus(custom_status);
        // GlobalData.connection.sendPacket(presence);

        // presence.setStatus(GlobalData.AVAILABLE+GlobalData.status_time_separater+"");
        // Presence presence = new Presence(Presence.Type.available);
        GlobalData.connection.sendPacket(presence);

    }

    private void sendPing() {

        if (GlobalData.connection != null) {
            if (GlobalData.connection.isConnected()
                    && GlobalData.connection.isAuthenticated()) {
                IQ req = new IQ() {
                    public String getChildElementXML() {
                        return "<ping xmlns='urn:xmpp:ping'/>";
                    }
                };

                req.setType(IQ.Type.GET);
                PacketFilter filter = new AndFilter(new PacketIDFilter(
                        req.getPacketID()), new PacketTypeFilter(IQ.class));

                mPingCollector = GlobalData.connection
                        .createPacketCollector(filter);
                GlobalData.connection.sendPacket(req);
                Utils.printLog("send ping request");
            }
        }
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            sendPing();
        }
    }
}
/*
* public void offlineShowUser() {
        if (GlobalData.dbHelper != null) {

            if (GlobalData.registerAndStragerGlobalArrayList != null) {

                try {
                    for (int i = 0; i < GlobalData.registerAndStragerGlobalArrayList
                            .size(); i++) {
                        GlobalData.dbHelper.offlineStatusSet(
                                GlobalData.registerAndStragerGlobalArrayList
                                        .get(i).getRemote_jid(), "0");
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        }

    }

     public class UploadThreadForMediaPath implements Runnable {

        Object object[] = null;
        int isgroup = 0;
        String path = "";
        String type = "";
        String remote_id = "";
        Chatmodel groupmodel;

        public UploadThreadForMediaPath(Object[] s) {
            object = s;
            this.isgroup = (Integer) object[0];
            path = (String) object[1];
            type = (String) object[2];
            remote_id = (String) object[3];
            groupmodel = (Chatmodel) object[4];
        }

        @Override
        public void run() {
            try {
                loadimage(isgroup, path, type, remote_id, groupmodel);
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        @Override
        public String toString() {
            return "";
        }
    }

    /*
     * private void setofflineHadnler() { offlineHadnler = new Handler() {
	 *
	 * @Override public void handleMessage(Message msg) {
	 *
	 * super.handleMessage(msg); final String sentmsg =
	 * msg.getData().getString("sentmsg"); if(sentmsg.equalsIgnoreCase("1")){
	 * if(
	 * GlobalData.connection!=null&&GlobalData.connection.isConnected()&&GlobalData
	 * .connection.isAuthenticated()){
	 *
	 * sendOfflineMessage(); } }
	 *
	 * } }; } private void setjoinadnler() { joinHadnler = new Handler() {
	 *
	 * @Override public void handleMessage(Message msg) {
	 *
	 * super.handleMessage(msg); final String sentmsg =
	 * msg.getData().getString("join"); if(sentmsg.equalsIgnoreCase("1")){
	 * if(GlobalData
	 * .connection!=null&&GlobalData.connection.isConnected()&&GlobalData
	 * .connection.isAuthenticated()){
	 *
	 * startJoinInLoop(); sendOffLineMessageMethodWithAsyncTaskState(); } }
	 *

	 * } }; }
	 *
	 * public void fetchContact() {

        ContactUtil
                .fetchDeletedContactFirst(getApplicationContext(), chatPrefs);
        ContactUtil.getDeviceContact(context, chatPrefs);

    }

     public void addfiletoUIandDB(final String filepath, final String filetype,
                                 final String msgPacketId, final String sendfilestaus) {

        new Thread(new Runnable() {
            public void run() {

                String getRemote_jid = "";

                // final String msgtime =
                // String.valueOf(System.currentTimeMillis());
                final String msgtime = Utils.currentTimeStap();
                if (filetype.equals(GlobalData.Locationfile)) {
                    rowid = GlobalData.dbHelper.addchatFileToMessagetable(
                            remote_jid, filepath, filetype, getRemote_jid, "S",
                            "", "", "", msgtime, msgPacketId, sendfilestaus,
                            true);
                } else {
                    rowid = GlobalData.dbHelper.addchatFileToMessagetable(
                            remote_jid, filepath, filetype, getRemote_jid, "S",
                            "", "", "", msgtime, msgPacketId, sendfilestaus,
                            true);
                }

                if (rowid != -1) {
                    GlobalData.dbHelper.addorupdateRecentTable(remote_jid,
                            String.valueOf(rowid));
                    GlobalData.dbHelper.updateContactmsgData(remote_jid,
                            Utils.updatemessage(filetype) + " sent", msgtime);

                }

                GlobalData.shareFilepath = "";
                GlobalData.shareFiletype = "";

                Chatmodel model = new Chatmodel();
                model.setMediatype(filetype);
                model.setStatus("S");
                model.setMessagerowid(String.valueOf(rowid));
                model.setMediapath(filepath);
                model.setMine(true);
                model.setMsgDate(Utils.getmsgDate(msgtime));
                model.setMsgTime(Utils.getmsgTime(msgtime));
                model.setMsg_packetid(msgPacketId);
                model.setSent_msg_success("1");
                if (filetype.equals(GlobalData.Imagefile)
                        || filetype.equals(GlobalData.Locationfile)) {
                    model.setOrignalbitmap(BitmapFactory.decodeFile(filepath));
                } else if (filetype.equals(GlobalData.Videofile)) {
                    Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(
                            filepath, MediaStore.Images.Thumbnails.MINI_KIND);
                    if (thumbnail != null) {
                        model.setOrignalbitmap(thumbnail);
                    }
                }

                // }
                // });
            }
        }).start();
    }

    public void getFtpClient() {

        new webservice(null, webservice.GetFTPHostDetail.geturl("image"),
                webservice.TYPE_GET, webservice.TYPE_FTP_UPLD,
                new ServiceHitListener() {

                    public void onSuccess(Object Result, int id) {

                        GetFTPCre gpmodel = (GetFTPCre) Result;

                        GlobalData.FTP_USER = gpmodel.getGetFTPHostDetail()
                                .get(0).getFtpUser();
                        GlobalData.FTP_PASS = gpmodel.getGetFTPHostDetail()
                                .get(0).getFtpPassword();
                        GlobalData.FTP_HOST = gpmodel.getGetFTPHostDetail()
                                .get(0).getHostName();
                        GlobalData.FTP_URL = gpmodel.getGetFTPHostDetail()
                                .get(0).getFtpUrl();

                        // sendurlpath= GlobalData.FTP_URL+userId+"/"+
                        // imagetoupload2;
                        sendurlpath = GlobalData.FTP_HOST + "/" + userId + "/"
                                + imagetoupload2;

                    }

                    @Override
                    public void onError(String Error, int id) {

                        //Log.w("PROFILEPICTURE", "PROFILEPICTURE" + "");
                    }
                });

    }

      public void runForAddRoaster() {
        mHandler.post(new Runnable() {
            public void run() {
                try {
                    addingRosterentry();

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });
    }
	 */
