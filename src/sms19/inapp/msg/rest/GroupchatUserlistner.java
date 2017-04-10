package sms19.inapp.msg.rest;

import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;

import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;

public class GroupchatUserlistner implements SubjectUpdatedListener {

	@Override
	public void subjectUpdated(String subject, String from) {
		// TODO Auto-generated method stub
		Utils.printLog("Subject changed.. : " + subject + " , from " + from);
		MultiUserChat m = new MultiUserChat(GlobalData.connection, from);
	}
}
