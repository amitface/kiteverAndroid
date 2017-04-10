package sms19.listview.webservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Method {

    public static final String BASE_URL_TEST = "http://microlabz.in/IndianRail/Services/";

    static File filename;

    private Context _context;

    public Method(Context context) {
        this._context = context;
    }

    // start check Internet Connection
    public boolean isConnected() {
        ConnectivityManager connec = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connec != null) {
            android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isConnected()) {

                return true;
            } else {
                if (mobile != null) {
                    return mobile.isConnected();
                }
            }
        }
        return false;
    }

    public static void createfile(String a) {

        try {
            // file created and store in device--------------------
            filename = new File(Environment.getExternalStorageDirectory() + a);
            filename.createNewFile();
            //-----------------------------------------------------

        } catch (Exception e) {

        }

    }

    public static File path(String s) {
        return filename = new File(Environment.getExternalStorageDirectory() + s);
    }

    public static void storeDataInDeviceStorage(String msg) {

        try {
            //Log.w("TAG###########","msg"+msg);

            //   String cmd = "vaibhav"+filename.getAbsolutePath();

            String cmd = filename.getAbsolutePath();

            //Log.w("TAG#############","CMD"+cmd);
            // file write in SD CARD------------------------------------------

            try {
                // open myfilename.txt for writing

                //Log.w("TAG############","filename"+filename);
                FileOutputStream out = new FileOutputStream(filename);
                // write the contents on mySettings to the file
                out.write(msg.getBytes());
                // close the file
                out.close();
            } catch (java.io.IOException e) {
                //do something if an IOException occurs.
            } catch (Exception e) {

            }
            //----------------------------------------------------------------

            Runtime.getRuntime().exec(cmd);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public static String readFileDataFromDevice(String name) {

        File filename = new File(Environment.getExternalStorageDirectory() + name);

        String myData = "";

        try {
            FileInputStream fis = new FileInputStream(filename);

            DataInputStream in = new DataInputStream(fis);

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));

            String strLine;

            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }

            // Toast.makeText(, myData, Toast.LENGTH_SHORT).show();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myData;

    }

}
