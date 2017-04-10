package sms19.inapp.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;

public class InboxSMSGet  extends BroadcastReceiver {
    
   // Get the object of SmsManager
   final SmsManager sms = SmsManager.getDefault();
    
   public void onReceive(Context context, Intent intent) {
    
       // Retrieves a map of extended data from the intent.
       final Bundle bundle = intent.getExtras();

       try {
            
           if (bundle != null) {
                
            //   final Object[] pdusObj = (Object[]) bundle.get("pdus");
                
            //   for (int i = 0; i < pdusObj.length; i++) {
                    
                 //  SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                //   String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    
                 //  String senderNum = phoneNumber;
                  // String message = currentMessage.getDisplayMessageBody();

                  // Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                    

                  // Show Alert
                /*   int duration = Toast.LENGTH_LONG;
                   Toast toast = Toast.makeText(context,
                                "senderNum: "+ senderNum + ", message: " + message, duration);
                   toast.show();*/
                   
                 /*   if(senderNum.equals("DM-TXTSMS")||senderNum.equalsIgnoreCase("DM-TXTSMS")){
                   String jid="+9197820123455@103.25.130.241";
                   if (GlobalData.dbHelper == null) {
           			try {
           				GlobalData.dbHelper = new DatabaseHelper(context, false);
           				long rowid =	  GlobalData.dbHelper.addchatToMessagetable(jid,message,jid, Utils.currentTimeStap(),"","4","0");
           			GlobalData.dbHelper.updateUnreadmsgCount(jid);
           			if (rowid != 0 && rowid != -1) {
						GlobalData.dbHelper
						.addorupdateRecentTable(
								jid,
								String.valueOf(rowid));
						
						GlobalData.dbHelper.insertContactDetails(
								jid,
								message,
								 Utils.currentTimeStap());
						
						
					}
           			// GlobalData.dbHelper.updatestatusOfRecieveMessage(jid,"","4");

           			} catch (IOException e1) {

           				e1.printStackTrace();
           			}
           		}else{
           			GlobalData.dbHelper = new DatabaseHelper(context, false);
           			long rowid =		  GlobalData.dbHelper.addchatToMessagetable(jid,message,jid, Utils.currentTimeStap(),"","4","0");
         			GlobalData.dbHelper.updateUnreadmsgCount(jid);
         			if (rowid != 0 && rowid != -1) {
						GlobalData.dbHelper
						.addorupdateRecentTable(
								jid,
								String.valueOf(rowid));
						GlobalData.dbHelper.insertContactDetails(
								jid,
								message,
								 Utils.currentTimeStap());
						
						
         			}
           		}
                  
                    }*/
                   
                   
               //} // end for loop
             } // bundle is null

       } catch (Exception e) {
//           Log.e("SmsReceiver", "Exception smsReceiver" +e);
            
       }
   }   
}