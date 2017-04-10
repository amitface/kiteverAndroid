package sms19.inapp.msg.rest;

import org.jivesoftware.smackx.muc.UserStatusListener;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.Home;

public class GroupChatMyStatusListner implements UserStatusListener {

	@Override
	public void adminGranted() {
		// TODO Auto-generated method stub
		Utils.printLog("adminGranted user ");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}

	@Override
	public void adminRevoked() {
		// TODO Auto-generated method stub
		Utils.printLog("adminRevoked user");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}

	@Override
	public void banned(String arg0, String arg1) {
		// TODO Auto-generated method stub
		Utils.printLog("banned from owner");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}

	@Override
	public void kicked(String arg0, String arg1) {
		// TODO Auto-generated method stub
		Utils.printLog("kicked from owner");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}

	@Override
	public void membershipGranted() {
		// TODO Auto-generated method stub
		Utils.printLog("membershipGranted user");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}

	@Override
	public void membershipRevoked() {
		// TODO Auto-generated method stub
		Utils.printLog("membershipRevoked user");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}

	@Override
	public void moderatorGranted() {
		// TODO Auto-generated method stub
		Utils.printLog("moderatorGranted user");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}

	@Override
	public void moderatorRevoked() {
		// TODO Auto-generated method stub
		Utils.printLog("moderatorRevoked user");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}

	@Override
	public void ownershipGranted() {
		// TODO Auto-generated method stub
		Utils.printLog("ownershipGranted user");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}

	@Override
	public void ownershipRevoked() {
		// TODO Auto-generated method stub
		Utils.printLog("ownershipRevoked user");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}

	@Override
	public void voiceGranted() {
		// TODO Auto-generated method stub
		Utils.printLog("voiceGranted user");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}

	@Override
	public void voiceRevoked() {
		// TODO Auto-generated method stub
		Utils.printLog("voiceRevoked user");
		Home.homeServiceConnection.sendConnectionListener("adminGranted user ");
	}
}
