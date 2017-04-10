package com.kitever.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.kitever.android.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.listview.database.DataBaseDetails;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GcmCustomService extends GCMBaseIntentService {
    public static String pushMsg = "";
    public static boolean pushFlag = false;
    private static boolean notifyFlag = false;
    DataBaseDetails dbObj;
    private static final String TAG = "GCMIntentService";

    public GcmCustomService() {
        super(CommonUtilities.SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        CommonUtilities.displayMessage(context, "gcm_registered");
        ServerUtilities.registerToServer(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        // Logger.debug(TAG, "Device unregistered");
        CommonUtilities.displayMessage(context, "gcm_unregistered");
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, registrationId);
        }
    }



    @Override
    protected void onMessage(Context context, Intent intent) {
        try {

            String gcmMessage = "";
            String msgSource="";
            String content="",pic="",redirectto="",time="";
            gcmMessage = (String) intent.getExtras().get("message");
            msgSource= intent.getStringExtra("Source");
            content= intent.getStringExtra("Content");
            pic= intent.getStringExtra("Pic");
            redirectto= intent.getStringExtra("RedirectTo");
            time= intent.getStringExtra("time");

            if(msgSource.equals("Custom"))
            {
                dbObj = new DataBaseDetails(this);
                dbObj.Open();
                dbObj.addnotifiaction(Utils.getUserId(context), context.getString(R.string.app_name), content,redirectto, time, pic);
                dbObj.close();
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, Utils.setBigPictureStyleNotification(context, content, pic, redirectto));

            }
            else
            {
                if (GlobalData.connection == null) {
                    Utils.showCustomNotification(context, gcmMessage, "", "gcm");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        // Logger.debug(TAG, "Received deleted messages notification");
        String message = "gcm_deleted";
        CommonUtilities.displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        // Logger.debug(TAG, "Received error: " + errorId);
        CommonUtilities.displayMessage(context, "gcm_error, errorId");
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        // Logger.debug(TAG, "Received recoverable error: " + errorId);
        CommonUtilities.displayMessage(context,
                "gcm_recoverable_error, errorId");
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();// Notification(icon,
        // message, when);
        String title = "Gcm";
        CommonUtilities.displayMessage(context, title);
        notificationManager.notify(0, notification);
    }

    /**
     * Fetch action id from the message to map activity
     */
    int getActionID(String message) {
        int actionID = -1;
        if (message != null) {
            if (message.contains("$MSC$%1000%4")) {
                String[] array = message.split("%");
                try {
                    if (array.length >= 3) {
                        String id = array[2];
                        actionID = Integer.parseInt(id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return actionID;
    }

    public static boolean getNotifyShow() {
        return notifyFlag;
    }

    public static void setNotifyShow(boolean flag) {
        notifyFlag = flag;
    }


}
