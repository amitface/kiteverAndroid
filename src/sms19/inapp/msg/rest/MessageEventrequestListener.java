package sms19.inapp.msg.rest;



import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.DefaultMessageEventRequestListener;
import org.jivesoftware.smackx.MessageEventManager;

import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;

public class MessageEventrequestListener  extends DefaultMessageEventRequestListener{

	@Override
	public void composingNotificationRequested(String arg0, String arg1,
			MessageEventManager arg2) {
		// TODO Auto-generated method stub
		 Utils.printLog("MessageEventrequestListener composingNotificationRequested==> "+arg0+arg1);
	}

	@Override
	public void deliveredNotificationRequested(String arg0, String arg1,
			MessageEventManager arg2) {
		// TODO Auto-generated method stub
		 Utils.printLog("MessageEventrequestListener deliveredNotificationRequested==> "+arg0+arg1);
		 	 super.deliveredNotificationRequested(arg0, arg1, arg2);
	}

	/*@Override
	public void displayedNotificationRequested(String arg0, String arg1,
			MessageEventManager arg2) {
		// TODO Auto-generated method stub
		 Utils.printLog("MessageEventrequestListener displayedNotificationRequested==> "+arg0+arg1);	
		 arg2.sendDisplayedNotification(arg0, arg1);
	}
*/
	@Override
	public void offlineNotificationRequested(String arg0, String arg1,
			MessageEventManager arg2) {
		// TODO Auto-generated method stub
		 Utils.printLog("MessageEventrequestListener offlineNotificationRequested==> "+arg0+arg1);
		 GlobalData.dbHelper.updatestatusOfSentMessageForBlock(StringUtils.parseBareAddress(arg0),arg1,"2");
	}

}
