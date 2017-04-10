package sms19.inapp.msg.constant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import java.io.File;

import sms19.inapp.msg.services.RegisterConnectionService;
import sms19.listview.newproject.Home;

/**
 * Created by maheshad on 16/7/15.
 */
public class NetReceiver extends BroadcastReceiver {
    Boolean status = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            status = true;
            long valuetime = Utils.getUserTime(Home.homeServiceConnection);
            if (valuetime == 0) {
                Utils.callForGetTimeAsyncTask(Home.homeServiceConnection);// get current time;
            } else {
                Utils.SERVER_TIME = valuetime;
            }
            start();
        } else {
            try {
                if (GlobalData.registerAndStragerGlobalArrayList != null) {
                    if (GlobalData.registerAndStragerGlobalArrayList != null) {

                        try {
                            Utils.offlineShowUser();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (Home.chatPrefs != null) {
                    Utils.saveGroupLeaveTm(Home.chatPrefs);
                }
                status = false;
                stop();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void login() {
        try {
            Presence presence = new Presence(Presence.Type.available);
            try {
                GlobalData.connection.sendPacket(presence);
                //connection.disconnect(presence);
                GlobalData.loginSuccess = false;
            } catch (Exception exception) {
                exception.printStackTrace();
                GlobalData.connection.disconnect();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            GlobalData.connection.disconnect();
        }

//		Log.w("LogOut", "LogOut");
    }


    public void stop() {
        try {
            //RegisterConnectionService connectionService2=	Home.connectionServiceObject;
            if (GlobalData.connection != null) {
                try {
                    GlobalData.connection.disconnect();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            GlobalData.connection = new XMPPConnection(config());
            Home.homeServiceConnection.stopService(new Intent(Home.homeServiceConnection, RegisterConnectionService.class));
            Home.homeServiceConnection.unbindService(Home.homeServiceConnection);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void start() {
        // activity connects to the service.
        try {
            //stop();
            Intent intent1 = new Intent(Home.homeServiceConnection, RegisterConnectionService.class);
            Home.homeServiceConnection.startService(intent1);
            Intent intent = new Intent(Home.homeServiceConnection, RegisterConnectionService.class);
            Home.homeServiceConnection.bindService(intent, Home.homeServiceConnection, Context.BIND_AUTO_CREATE);
            Home.mIsBound = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void logout() {

        try {
            Presence presence = new Presence(Presence.Type.unavailable);
            // presence.setStatus(Home.mydetail.getRemote_jid()/* + "Offline"*/);
            // boolean available=false;
            try {
                //	Presence.Type type = available ? Presence.Type.available : Presence.Type.unavailable;
                //  Presence presence2 = new Presence(type);
                GlobalData.connection.sendPacket(presence);
                //GlobalData.connection.disconnect(presence);
                GlobalData.loginSuccess = false;
            } catch (Exception exception) {
                exception.printStackTrace();
                GlobalData.connection.disconnect();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            GlobalData.connection.disconnect();
        }
//		Log.w("LogOut", "LogOut");
    }

    public ConnectionConfiguration config() {
        //setProxy(TorProxyInfo.PROXY_TYPE, GlobalData.HOST, GlobalData.PORT);
        ConnectionConfiguration connConfig = new ConnectionConfiguration(GlobalData.HOST, GlobalData.PORT);
        //ConnectionConfiguration connConfig = new ConnectionConfiguration(GlobalData.HOST, GlobalData.PORT);

        SmackConfiguration.setPacketReplyTimeout(20 * 1000);
        connConfig.setSendPresence(false);

        //	PingManager pingManager = PingManager.getInstanceFor(connection);

        connConfig.setSASLAuthenticationEnabled(true);
        //connConfig.setReconnectionAllowed(false);
        connConfig.setReconnectionAllowed(true);//new
        connConfig.setCompressionEnabled(false);
        connConfig.setSecurityMode(SecurityMode.disabled);
        //connConfig.setSecurityMode(SecurityMode.enabled);
        //connConfig.setSocketFactory(SSLSocketFactory.getDefault());// new 5 apl


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
}