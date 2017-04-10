package sms19.inapp.msg.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/*Amit for checking connection*/
public class ConnectionReceiver extends BroadcastReceiver {
    public ConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        /*throw new UnsupportedOperationException("Not yet implemented");*/
        Toast.makeText(context,intent.getStringExtra("message"),Toast.LENGTH_SHORT).show();
    }
}
