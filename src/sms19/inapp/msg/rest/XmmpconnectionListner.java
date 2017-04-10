package sms19.inapp.msg.rest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.StreamError;

import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.PhoneValidModel;
import sms19.inapp.msg.services.RegisterConnectionService;
import sms19.listview.newproject.Home;

public class XmmpconnectionListner implements ConnectionListener,
        ConnectionCreationListener {

    boolean stringReconnectingFlah = false;
    boolean alreadylogin = false;


    @Override
    public void connectionClosed() {
        // TODO Auto-generated method stub
        ConstantFields.loginfromsplash = false;
//        Utils.printLog("Connection closed successfully.");
        /*if(Home.chatPrefs!=null){
            Utils.saveGroupLeaveTm(Home.chatPrefs);
		}*/
//        Home.homeServiceConnection.sendConnectionListener("Connection closed successfully.");
        Utils.offlineShowUser();
    }

    @Override
    public void connectionClosedOnError(Exception arg0) {
        // TODO Auto-generated method stub
        //org.xmlpull.v1.XmlPullParserException: processing instructions must not start with xml (position:unknown @1:117 in java.io.BufferedReader@42365e20)
        //org.xml.sax.SAXParseException: expected: END_TAG {}BINVAL (position:END_DOCUMENT null@6:39 in java.io.InputStreamReader@42c81f80)
        ConstantFields.loginfromsplash = false;
//        Utils.printLog("Connection closed with error.");
//        Home.homeServiceConnection.sendConnectionListener("Connection closed with error."+arg0.toString());
        stringReconnectingFlah = false;

        if (Home.chatPrefs != null) {
            Utils.saveGroupLeaveTm(Home.chatPrefs);
        }

        if (arg0 instanceof XMPPException) {
            XMPPException xmppException = (XMPPException) arg0;
            StreamError error = xmppException.getStreamError();
            if (error != null && error.getCode() != null && error.getCode().equals("conflict")) {
                Utils.printLog("conflict custom....");
                //GlobalData.connection.disconnect();
                if (Home.logoutHandler != null) {
                    android.os.Message message = new android.os.Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("logoutFlag", 1);// 1 mean logout
                    message.setData(bundle);

                    Home.logoutHandler.sendMessage(message);
                }
                return;
            }
        }

        if (Home.homeServiceConnection != null) {
            if (!Utils.isDeviceOnline(Home.homeServiceConnection)) {
                stopService();
            } else {
                if (Home.connectionServiceObject != null) {
                    try {
                        if ((!GlobalData.connection.isConnected())) {
                            //GlobalData.connection=Home.connectionServiceObject.connection;
                            Utils.offlineShowUser();
                            Home.connectionServiceObject.destorAllThread();

                            Home.connectionServiceObject.connectingtoxmppserver();
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        arg0.printStackTrace();
    }

    @Override
    public void reconnectingIn(int arg0) {
        // TODO Auto-generated method stub
        Utils.printLog("Connection is reconnecting in  : " + arg0);
//        Home.homeServiceConnection.sendConnectionListener("Connection is reconnecting in  : " + arg0);
    }

    @Override
    public void reconnectionFailed(Exception arg0) {
        // TODO Auto-generated method stub
        Utils.printLog("Reconnection is failed.");
//        Home.homeServiceConnection.sendConnectionListener("Reconnection is failed.");
        //GlobalData.connection=null;
    }

    @Override
    public void reconnectionSuccessful() {
        // TODO Auto-generated method stub
        Utils.forreconnecting = true;
        ConstantFields.fromPrevconnection = true;
//        Utils.printLog("Reconnection successfully.");
//        Home.homeServiceConnection.sendConnectionListener("Reconnection successfully.");
        stringReconnectingFlah = true;
        if (Home.connectionServiceObject != null) {
            if (GlobalData.connection.isAuthenticated()) {
                Home.connectionServiceObject.setMessageNotificationListener();
                Home.connectionServiceObject.AddPresenceForOnline();
            }
        }
        //	startService();//new 23
    }

    @Override
    public void connectionCreated(Connection arg0) {
        // TODO Auto-generated method stub
        Utils.printLog("Connection created successfully.");
//        Home.homeServiceConnection.sendConnectionListener("Connection created successfully.");
    }

    public void stopService() {
        if (Home.homeServiceConnection != null) {
            try {
                Home.homeServiceConnection.stopService(new Intent(Home.homeServiceConnection, RegisterConnectionService.class));
                Home.homeServiceConnection.unbindService(Home.homeServiceConnection);
                GlobalData.REGISTER_CONNECTION_SERVICE_ISON = false;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                GlobalData.REGISTER_CONNECTION_SERVICE_ISON = false;
            }
        }
    }

    public void startService() {
        // activity connects to the service.
        if (!GlobalData.REGISTER_CONNECTION_SERVICE_ISON) {
            Intent intent1 = new Intent(Home.homeServiceConnection, RegisterConnectionService.class);
            Home.homeServiceConnection.startService(intent1);
            Intent intent = new Intent(Home.homeServiceConnection, RegisterConnectionService.class);
            Home.homeServiceConnection.bindService(intent, Home.homeServiceConnection, Context.BIND_AUTO_CREATE);
            Home.mIsBound = true;
        } else {

            if (Home.connectionServiceObject != null) {
                try {

                    if (GlobalData.connection != null) {
                        try {
                            Home.connectionServiceObject.connectingtoxmppserver();

                        } catch (Exception e) {
                            if (!e.getMessage().trim().equalsIgnoreCase("Already logged in to server")) {

                            } else if (e.getMessage().trim().equalsIgnoreCase("Already logged in to server")) {

                            }
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public synchronized static sms19.inapp.msg.model.PhoneValidModel validNumberCheck(String phone) {
        boolean isValid = false;
        String internationalFormat = "";
        PhoneNumberUtil phoneUtil = null;

        sms19.inapp.msg.model.PhoneValidModel model = new PhoneValidModel();
        phoneUtil = PhoneNumberUtil.getInstance();
        model.setNumber(false);
        try {
            Phonenumber.PhoneNumber phNumberProto = phoneUtil.parse(phone, "IN");
            int countryCode = phNumberProto.getCountryCode();
//			System.err.println("NumberParseException was thrown: " + countryCode);
            isValid = phoneUtil.isValidNumber(phNumberProto);
            if (isValid) {
                internationalFormat = phoneUtil.format(phNumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL).replace(" ", "");
                String code = "+" + phNumberProto.getCountryCode() + "".trim();
                model.setNumber(false);

                if (phoneUtil.getNumberType(phNumberProto) == phoneUtil.getNumberType(phNumberProto).FIXED_LINE || phoneUtil.getNumberType(phNumberProto) == phoneUtil.getNumberType(phNumberProto).TOLL_FREE) {
                    internationalFormat = "";
                    model.setNumber(false);
                } else {

                    if (internationalFormat.startsWith("0")) {
                        internationalFormat = internationalFormat.substring(1, internationalFormat.length());
                    }
                    model.setPhone_number(internationalFormat);
                    model.setCountry_code(code);
                    model.setNumber(isValid);
                }
            }

        } catch (Exception e) {
//			System.err.println("NumberParseException was thrown: " + e.toString());
            model.setNumber(false);
            return model;
        }
        return model;
    }
}
