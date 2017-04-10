package sms19.listview.database;

import sms19.listview.newproject.BNotification;
import sms19.listview.newproject.Inbox;

import com.kitever.android.R;

import sms19.listview.newproject.model.ThreadModel;
import sms19.listview.webservice.ThreadCounterInbox;
import sms19.listview.webservice.ThreadCounterInbox.ServiceHitListenerwhatsup;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class UpdateService extends IntentService {
    public static NotificationManager mNotificationManager;
    public static NotificationManager mNotificationMan;

    public static final int NOTIFICATION_ID1 = 1;
    public static final int NOTIFICATION_ID2 = 2;

    public static final int NOTIFICATION_ID = 20;


    DataBaseDetails dbObject = new DataBaseDetails(this);
    String Mobile = "";
    String UserId = "";

    Handler mHandler = new Handler();
    int Updatevalue = 0;
    public static int notifyonlyone = 0;
    public static int countonlyone = 0;

    public static Boolean notifyna = false;

    Context gContext = this;

    public UpdateService() {
        super("UpdateService");

    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        super.onStartCommand(intent, startId, startId);
        gContext = this;

		/*   Toast toast =null;
           toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
	      toast.cancel();
	    */
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        // 10 sec
                        Thread.sleep(9000);// 1000*60= 1 min ;;; 1000 = 1 sec ;;; 1000*60*2= 2 min
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {

                                Updatevalue = 0;

                                //Log.w("JSR","JSR ::::Sevice is running");

                                try {

                                    //Toast.makeText(gContext, "Service running",Toast.LENGTH_SHORT).show();
                                    //t.cancel();
                                } catch (Exception e1) {

                                }
                                runAsForeground();
                                //	clearNotification();

                                // web service implement for 2 min updating
                                fetchMobileandUserId();

                                new ThreadCounterInbox(null, ThreadCounterInbox.UndeliveredMessageForRecipientCount.geturl(UserId), ThreadCounterInbox.TYPE_GET, ThreadCounterInbox.TYPE_FETCH_MESSAGE_THREAD, new ServiceHitListenerwhatsup() {

                                    @Override
                                    public void onSuccess(Object Result, int id) {

                                        ThreadModel model = (ThreadModel) Result;
                                        //Log.w("1111111111@@##", "1111111111111$$$$$");

                                        if (model.getUndeliveredMessageForRecipientCount().size() > 0) {
                                            //Log.w("22222222222222@@##", "22222222222$$$$$");
                                            try {
                                                Updatevalue = Integer.parseInt(model.getUndeliveredMessageForRecipientCount().get(0).getGeneralMessageCount());
                                            } catch (Exception e) {
                                                //Log.w("1111111111@@##", "1111111111111$$$$$");
                                                e.printStackTrace();
                                                Updatevalue = 0;
                                            }
                                            String count = model.getUndeliveredMessageForRecipientCount().get(0).getBroadcastMessageCount();
                                            //Log.w("333333333331@@##", "333333333333$$$$$"+count);
                                            int broadcastcount = Integer.parseInt(count);
                                            //Log.w("@@##", "$$$$$"+broadcastcount);

                                            if (broadcastcount > 0) {
                                                if (notifyonlyone == 1) {

                                                } else {
                                                    String messagebroadTitle = "You have a new message.";

                                                    try {
                                                        messagebroadTitle = model.getUndeliveredMessageForRecipientCount().get(0).getBroadcastTitle();
                                                    } catch (Exception e) {
                                                        messagebroadTitle = "You have a new message.";
                                                    }


                                                    NotifyBroadCastAll(messagebroadTitle);
                                                }
                                            }

                                            if (Updatevalue > 0) {
                                                if (countonlyone == 1) {

                                                } else

                                                {


                                                    sendNotification("You have a new message");
                                                }
                                            }


                                        }

                                    }

                                    @Override
                                    public void onError(String Error, int id) {

                                    }
                                });

                            }
                        });
                    } catch (Exception e) {

                    }
                }
            }
        }).start();


        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        // startService(new Intent(this, UpdateService.class));
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }


    @SuppressLint("NewApi")
    public void NotifyBroadCastAll(String msg) {
        notifyonlyone = 1;
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //*************************************************
        //Log.w("inside broadcast", "helllo");

        // Prepare intent which is triggered if the // notification is selected
        Intent intent = new Intent(UpdateService.this, BNotification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notiMail = new Notification.Builder(UpdateService.this)

                .setContentTitle("SMS19 Message")
                .setContentText(msg)
                .setSmallIcon(R.drawable.demo)
                .setContentIntent(pendIntent)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setOngoing(true)
                .build();
        mNotificationMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationMan.notify(NOTIFICATION_ID2, notiMail);
        //startActivity(intent);

    }

    public void fetchMobileandUserId() {
        dbObject.Open();

        Cursor c;

        c = dbObject.getLoginDetails();


        while (c.moveToNext()) {

            Mobile = c.getString(1);
            UserId = c.getString(3);

        }

        dbObject.close();
    }

    @SuppressLint("NewApi")
    private void sendNotification(String msg) {

        notifyna = true;
        countonlyone = 1;
        try {

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //*************************************************

            Intent intent = new Intent(UpdateService.this, Inbox.class);
            intent.putExtra("story", "hello");
            intent.putExtra("value", msg);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);

            Notification mNotification = new Notification.Builder(UpdateService.this)

                    .setContentTitle("SMS19 Message")
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.demo)
                    .setContentIntent(pIntent)
                    .setSound(soundUri)
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .build();

            mNotificationManager = (NotificationManager) UpdateService.this.getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID1, mNotification);

        } catch (Exception e) {
        }

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        // Let it continue running until it is stopped.

        gContext = this;

        //Toast.makeText(this, "Service Started in handler", Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        // 10 sec
                        Thread.sleep(5000);// 1000*60= 1 min ;;; 1000 = 1 sec ;;; 1000*60*2= 2 min
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {

                                //Log.w("JSR","JSR ::::Sevice is running in handler");

                                runAsForeground();
                                //clearNotification();
                                try {
                                    //Toast.makeText(gContext, "Service running in handler", Toast.LENGTH_SHORT).cancel();

                                    //t.cancel();
                                } catch (Exception e) {

                                }
                            }
                        });

                    } catch (Exception e) {

                    }
                }
            }
        }).start();


    }

    private void runAsForeground() {
        Intent notificationIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText("a")
                .setContentIntent(pendingIntent).build();

        startForeground(NOTIFICATION_ID, notification);

    }

}
