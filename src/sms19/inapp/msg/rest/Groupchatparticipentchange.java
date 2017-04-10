package sms19.inapp.msg.rest;

import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.Home;


public class Groupchatparticipentchange implements ParticipantStatusListener {

	@Override
	public void adminGranted(String arg0) {  
		// TODO Auto-generated method stub
		Utils.printLog("adminGranted");
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void adminRevoked(String arg0) {
		// TODO Auto-generated method stub
		Utils.printLog("adminRevoked");
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void banned(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		Utils.printLog("banned");
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
		
	}

	@Override
	public void joined(String arg0) {
		// TODO Auto-generated method stub
		Utils.printLog("joined..." + arg0);
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void kicked(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		Utils.printLog("kicked.." + arg0 + ", " + arg1 + ", " + arg2);
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void left(String arg0) {
		// TODO Auto-generated method stub
		Utils.printLog("left.."+arg0);
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void membershipGranted(String arg0) {
		// TODO Auto-generated method stub
		Utils.printLog("membershipGranted");
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void membershipRevoked(String arg0) {
		// TODO Auto-generated method stub
		Utils.printLog("membershipRevoked");
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void moderatorGranted(String arg0) {
		// TODO Auto-generated method stub
		Utils.printLog("moderatorGranted");
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void moderatorRevoked(String arg0) {
		// TODO Auto-generated method stub
		Utils.printLog("moderatorRevoked");
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void nicknameChanged(String arg0, String arg1) {
		// TODO Auto-generated method stub
		Utils.printLog("nicknameChanged");
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void ownershipGranted(String arg0) {
		// TODO Auto-generated method stub
		Utils.printLog("ownershipGranted.." + arg0);
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void ownershipRevoked(String arg0) {
		// TODO Auto-generated method stub
		Utils.printLog("ownershipRevoked..." + arg0);
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void voiceGranted(String arg0) {
		// TODO Auto-generated method stub
		Utils.printLog("voice Granted.. " + arg0);
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}

	@Override
	public void voiceRevoked(String arg0) {
		// TODO Auto-generated method stub
		Utils.printLog("voice revoked.. " + arg0);
		Home.homeServiceConnection.sendConnectionListener("adminGranted");
	}
}
