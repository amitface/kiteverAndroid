package sms19.inapp.msg.asynctask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Rest;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class MessageBroadCastAsyncTask extends AsyncTask<Void, Void, Void> {
    private String response = "";
    private JSONObject resObj;
    private String userId = "";
    private String mobileNoStr = "";
    private InAppMessageActivity activity = null;
    private String grouptitle = "";
    private String users_name = "";
    private String users_jid = "";
    private String expiry_time = "10";
    private String messageStr = "hello jack";
    private String groupJid = "";
    private String message_type = "";
    private ArrayList<Contactmodel> selectedmemberlist = null;
    private String isuniCode = "";
    private String lang = "en";
    private String message_packet_Id = "";
    private String inAppCount = "";
    private String InInboxCount = "";
    private ProgressDialog progressDialog = null;
    private String message_id = "";

    public MessageBroadCastAsyncTask(InAppMessageActivity activity, SharedPreferences chatPrefs, String userId
            , String grouptitle, String groupJid, String message, String iscodetype, String message_type,
                                     String expiry_time, ArrayList<Contactmodel> selectedmemberlist, String message_packet_Id, String inAppCount, String InInboxCount, String msg_id) {

        this.userId = userId;
        this.activity = activity;
        this.grouptitle = grouptitle;
        this.selectedmemberlist = selectedmemberlist;
        this.groupJid = groupJid;
        this.message_type = message_type;
        this.expiry_time = expiry_time;
        this.messageStr = message;
        this.message_packet_Id = message_packet_Id;
        this.inAppCount = inAppCount;
        this.InInboxCount = InInboxCount;
        this.message_id = msg_id;

    }


    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        if (activity != null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Sending broadcast...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }


    @Override
    protected Void doInBackground(Void... params) {

        //	String c_code ="+91";
        String postContactNo = mobileNoStr;
        String postCountryCode = "";

        try {

            if (!messageStr.equalsIgnoreCase("")) {
                try {
                    lang = Utils.LanguageDetect(messageStr);
                    // lang="en";
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (lang.equalsIgnoreCase("en")) {
                isuniCode = "0";
            }/*else if(lang.equalsIgnoreCase("hi")){
                isuniCode="1";
			}*/ else {
                isuniCode = "1";
            }


            try {
                String regexPattern = "[\uD83C-\uDBFF\uDC00-\uDFFF]+";
                byte[] utf8 = messageStr.getBytes("UTF-8");

                String string1 = new String(utf8, "UTF-8");

                Pattern pattern = Pattern.compile(regexPattern);
                Matcher matcher = pattern.matcher(string1);

                boolean isEmoji = false;
                while (matcher.find()) {
                    isEmoji = true;
                    break;
                }
                if (isEmoji) {
                    isuniCode = "1";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            postContactNo = Utils.removeCountryCode(postContactNo, activity);
            postCountryCode = "+91";
            String mobileNoStr = "";
            String messageid = "";
            String messagetime = "";

            String users_jid = "";
//			String email="";
            for (int i = 0; i < selectedmemberlist.size(); i++) {
                if (i == 0) {
                    users_jid = selectedmemberlist.get(i).getRemote_jid();
                    users_name = selectedmemberlist.get(i).getName();
                    mobileNoStr = selectedmemberlist.get(i).getNumber();
                    postCountryCode = selectedmemberlist.get(i).getCountry_code();
//					email=selectedmemberlist.get(i).getEmailId();
                    try {
                        mobileNoStr = postCountryCode.substring(1) + mobileNoStr;
                    } catch (Exception e) {
                        // TODO: handle exception
                        mobileNoStr = postCountryCode.substring(1, postCountryCode.length()) + mobileNoStr;
                    }

                    messageid = selectedmemberlist.get(i).getMessage_id();
                    messagetime = selectedmemberlist.get(i).getMessage_time();
                } else {
                    users_jid = users_jid + "," + selectedmemberlist.get(i).getRemote_jid();
                    users_name = users_name + "," + selectedmemberlist.get(i).getName();
                    postCountryCode = postCountryCode + "," + selectedmemberlist.get(i).getCountry_code();
                    messageid = messageid + "," + selectedmemberlist.get(i).getMessage_id();
//					email=selectedmemberlist.get(i).getEmailId();
//					String code=selectedmemberlist.get(i).getCountry_code().substring(1);
                    try {
                        mobileNoStr = mobileNoStr + "," + selectedmemberlist.get(i).getCountry_code().substring(1) + selectedmemberlist.get(i).getNumber();
                    } catch (Exception e) {
                        // TODO: handle exception
                        mobileNoStr = mobileNoStr + "," + selectedmemberlist.get(i).getCountry_code().substring(1, selectedmemberlist.get(i).getCountry_code().length()) + selectedmemberlist.get(i).getNumber();
                    }
                }
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("group_title", grouptitle));
            nameValuePairs.add(new BasicNameValuePair("group_jid", groupJid));
            nameValuePairs.add(new BasicNameValuePair("users_numbers", mobileNoStr));
            nameValuePairs.add(new BasicNameValuePair("users_name", users_name));
            nameValuePairs.add(new BasicNameValuePair("users_jid", users_jid));
            nameValuePairs.add(new BasicNameValuePair("expiry_time", expiry_time));
            nameValuePairs.add(new BasicNameValuePair("message", messageStr));
            nameValuePairs.add(new BasicNameValuePair("message_type", message_type));
            nameValuePairs.add(new BasicNameValuePair("Page", "SendBroadcastChat"));
            nameValuePairs.add(new BasicNameValuePair("user_id", userId));
            nameValuePairs.add(new BasicNameValuePair("isUnicode", isuniCode));

            nameValuePairs.add(new BasicNameValuePair("message_id", messageid));
            nameValuePairs.add(new BasicNameValuePair("message_time", messagetime));
            nameValuePairs.add(new BasicNameValuePair("country_code", postCountryCode));

            Utils.printLog("SendBroadcastChat Post Data:  " + nameValuePairs.toString());
            Rest rest2 = Rest.getInstance();
            Log.i("SendBroadcastChat", nameValuePairs.toString());
            // String	responseget = rest2.post(Apiurls.KIT19_POST_BASE_URL.replace("?Page=", ""), nameValuePairs);
            response = rest2.post(Apiurls.KIT19_BASE_URL.replace("?Page=", ""), nameValuePairs);


            Utils.printLog("SendBroadcastChat URL:  " + Apiurls.KIT19_BASE_URL.replace("?Page=", ""));
            Utils.printLog("SendBroadcastChat Response1112:  " + response);

            Utils.printLog("SendBroadcastChat:  " + response);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        try {
            if (response != null && response.trim().length() != 0) {
                try {
                    resObj = new JSONObject(response);
                    if (resObj.has("InsertBroadcast")) {
                        JSONArray arrayOb = resObj.getJSONArray("InsertBroadcast");
                        JSONObject jsonObject = arrayOb.getJSONObject(0);
                        sms19.inapp.single.chatroom.SingleChatRoomFrgament singleChat = sms19.inapp.single.chatroom.SingleChatRoomFrgament.newInstance("");
                        if (jsonObject.has("Insufficient Credits") && jsonObject.getInt("Insufficient Credits") == 0) {
                            //	singleChat.sendBroadCastAfterUpdateMsg(messageStr.trim(),message_type,true,message_packet_Id,inAppCount,InInboxCount);
                            //Toast.makeText(activity, jsonObject.getString("Column1"), Toast.LENGTH_SHORT).show();
                            singleChat.sendBroadCastAfterUpdateMsg(messageStr.trim(), "0", false, message_packet_Id, inAppCount, InInboxCount, message_id);

                        } else if (jsonObject.has("Success") && jsonObject.getInt("Success") == 1) {
                            singleChat.sendBroadCastAfterUpdateMsg(messageStr.trim(), message_type, true, message_packet_Id, inAppCount, InInboxCount, message_id);
                        } else {
                            singleChat.sendBroadCastAfterUpdateMsg(messageStr.trim(), "0", false, message_packet_Id, inAppCount, InInboxCount, message_id);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPostExecute(result);
    }
}
