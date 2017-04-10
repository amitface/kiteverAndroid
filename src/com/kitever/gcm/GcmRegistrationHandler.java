package com.kitever.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class GcmRegistrationHandler {

    public final static String MSG_TAG = "message";

    public GcmRegistrationHandler() {
    }

    /**
     * Register current application to Call server
     *
     * @param context       Application context
     * @param msisdnNo      MSISDN used by application
     * @param SenderID      Google API project id registered to use GCM
     * @param activityTable List of activity will be called by page for desired Action ID
     * @param serverUrl     Server URL to register device
     */
    public static void registerToGcm(Context context, String SenderID) {
        try {
            setActivitiesToCD(context);
            CommonUtilities.SENDER_ID = SenderID;
            GCMRegistrar.checkDevice(context);
            GCMRegistrar.checkManifest(context);
            final String regId = GCMRegistrar.getRegistrationId(context);
            Log.i("regid","regId--"+regId);
            if (regId.equals("") || !isRegisteredToCD(context)) {
                GCMRegistrar.register(context, CommonUtilities.SENDER_ID);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * Set Call server registration status
     */
    static void setRegisteredToCD(Context context, boolean isRegistered) {
        final SharedPreferences prefs = getCDPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(CommonUtilities.CDSTATUS, isRegistered);
        editor.commit();
    }

    /**
     * Set user activities
     */
    static void setActivitiesToCD(Context context) {
        final SharedPreferences prefs = getCDPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CommonUtilities.ACTIVITYLIST, convertListTOString());
        editor.commit();
    }

    /**
     * Check Call server registration status
     *
     * @return Registration status {@code Boolean}
     */
    static boolean isRegisteredToCD(Context context) {
        final SharedPreferences prefs = getCDPreferences(context);
        boolean status = prefs.getBoolean(CommonUtilities.CDSTATUS, false);
        return status;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getCDPreferences(Context context) {
        return context.getSharedPreferences(
                GcmRegistrationHandler.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static String convertListTOString() {
        String list = "";
        if (CommonUtilities.strActivityName != null) {
            if (CommonUtilities.strActivityName.length != 0) {
                for (int i = 0; i < CommonUtilities.strActivityName.length; i++) {
                    list += CommonUtilities.strActivityName[i]
                            + CommonUtilities.ACTIVITY_SEPRATOR;
                }
            }
        }
        return list;
    }

    static String[] getActivityList(Context context) {
        final SharedPreferences prefs = getCDPreferences(context);
        String listString = prefs.getString(CommonUtilities.ACTIVITYLIST, null);
        if (listString == null)
            return null;
        else
            return listString.split(",");
    }
}
