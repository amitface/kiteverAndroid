package sms19.inapp.msg.rest;

import android.content.Context;

import org.jivesoftware.smackx.MessageEventNotificationListener;

import sms19.inapp.msg.constant.Utils;

public class MessageEventnotificationListener implements
        MessageEventNotificationListener {

    Context context;

    NotifyAfterMessageDeliverRead notifyMessageDeleverRead;


    public MessageEventnotificationListener(Context ctx, NotifyAfterMessageDeliverRead listener) {
        context = ctx;
        notifyMessageDeleverRead = listener;
    }

    public interface NotifyAfterMessageDeliverRead {

        void notifyMsgStatus(String remot_id, String msgpacket_id, String msg_status);
    }

	/*
     * public static void setMsgStatusListener(NotifyAfterMessageDeliverRead
	 * listener) { notifyMessageDeleverRead = listener; }
	 */

	/*
	 * public static void sendMessageToListener(String msgid, String
	 * type_status) { if (notifyMessageDeleverRead != null) { if
	 * (type_status.equals("deliver"))
	 * notifyMessageDeleverRead.notifyMsgStatus(msgid,type_status); else if
	 * (type_status.equals("display"))
	 * notifyMessageDeleverRead.notifyMsgStatus(msgid,type_status);
	 * 
	 * } }
	 */

    @Override
    public void offlineNotification(String from, String packetID) {
        Utils.printLog("EventNotification offlineNotification==> " + from
                + packetID);
    }

    @Override
    public void displayedNotification(final String from, final String packetID) {
        Utils.printLog("EventNotification displayedNotification==> " + from
                + packetID);
        if (notifyMessageDeleverRead != null) {
            notifyMessageDeleverRead.notifyMsgStatus(from, packetID, "read");
        } else {
            Utils.printLog("EventNotification displayedNotification==> "
                    + "null");
        }

		/*
		 * Handler mainHandler = new Handler(context.getMainLooper()); Runnable
		 * myRunnable = new Runnable() {
		 * 
		 * @Override public void run() { Toast.makeText(context,
		 * "displayedNotification", 1000).show(); notifyMessageDeleverRead
		 * .notifyMsgStatus(from, packetID, "read"); } };
		 * mainHandler.post(myRunnable);
		 */
    }

    @Override
    public void deliveredNotification(final String from, final String packetID) {

        Utils.printLog("EventNotification deliveredNotification==> " + from
                + packetID);

        if (notifyMessageDeleverRead != null) {
            notifyMessageDeleverRead.notifyMsgStatus(from, packetID, "deliver");
        } else {
            Utils.printLog("EventNotification deliveredNotification==> "
                    + "null");
        }

		/*
		 * Handler mainHandler = new Handler(context.getMainLooper()); Runnable
		 * myRunnable = new Runnable() {
		 * 
		 * @Override public void run() { Toast.makeText(context,
		 * "deliveredNotification", 1000).show();
		 * notifyMessageDeleverRead.notifyMsgStatus(from, packetID, "deliver");
		 * } }; mainHandler.post(myRunnable);
		 */

    }

    @Override
    public void composingNotification(String from, String packetID) {
        Utils.printLog("EventNotification composingNotification==> " + from
                + packetID);
    }

    @Override
    public void cancelledNotification(String from, String packetID) {
        Utils.printLog("EventNotification cancelledNotification==> " + from
                + packetID);
    }

}
