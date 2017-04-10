package sms19.inapp.msg.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GroupHistoryAsyncTask extends AsyncTask<Object, Void, Void> {
    private String response = "";
    private JSONObject resObj;
    private String userId = "";
    private String mobileNoStr = "";
    private InAppMessageActivity activity = null;
    private String fromJid = "";
    private String toJid = "";
    private String lastMessageTime = "";
    private ProgressBar progress;
    String fromTime = "0";
    private boolean isRefreshAfterDownload = false;
    private TextView loading_text;

    public GroupHistoryAsyncTask(InAppMessageActivity activity, SharedPreferences chatPrefs, String userId, String fromJid, String toJid, String lastMessageTime, ProgressBar progress, TextView loading_text) {

        this.userId = userId;
        this.lastMessageTime = lastMessageTime;
        this.fromJid = fromJid;
        this.toJid = toJid;
        this.activity = activity;
        this.progress = progress;
        this.loading_text = loading_text;
    }


    @Override
    protected Void doInBackground(Object... params) {

        //	String c_code ="+91";
        String postContactNo = mobileNoStr;

        try {


            fromTime = (String) params[0];
            isRefreshAfterDownload = (Boolean) params[1];

            postContactNo = Utils.removeCountryCode(postContactNo, activity);



			/*String stringUrl=Apiurls.KIT19_BASE_URL+"getGroupMessageHistory&GroupJID="+fromJid+"&UserJID="+userId+"&fromTime="+fromTime;

			stringUrl=stringUrl.replace(" ","");
			Rest rest= Rest.getInstance();*/


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserJID", fromJid));
            nameValuePairs.add(new BasicNameValuePair("fromTime", fromTime));
            nameValuePairs.add(new BasicNameValuePair("GroupJID", toJid));
            nameValuePairs.add(new BasicNameValuePair("Page", "getGroupMessageHistory"));


            Rest rest = Rest.getInstance();
            //response = rest.post(Apiurls.KIT19_POST_BASE_URL.replace("?Page=", ""), nameValuePairs);
            response = rest.post(Apiurls.KIT19_BASE_URL.replace("?Page=", ""), nameValuePairs);
            Utils.printLog("Message History URL:  " + Apiurls.KIT19_POST_BASE_URL);
            Utils.printLog("Message History Post Data:  " + nameValuePairs);
            Utils.printLog("Message History Response1112:  " + response);


            try {
                if (response != null && response.trim().length() != 0) {
                    try {

                        resObj = new JSONObject(response);
                        if (resObj.has("GroupMessageHistory")) {
                            JSONArray jsonArray = resObj.getJSONArray("GroupMessageHistory");
                            if (jsonArray != null) {

                                //for(int i=jsonArray.length()-1;i>=0;i--){
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if (jsonObject != null) {


                                        String remote_jid = toJid;
                                        String msgtime = "";
                                        String key_from_me = "";
                                        String message = "";
                                        String getFromjid = "";
                                        String getTojid = "";

                                        String msg_type = "";


                                        if (jsonObject.has("FromJid")) {
                                            getFromjid = jsonObject.getString("FromJid");
                                        }
                                        if (jsonObject.has("ToJid")) {
                                            getTojid = jsonObject.getString("ToJid");
                                        }

                                        if (jsonObject.has("MessageType")) {
                                            msg_type = jsonObject.getString("MessageType");
                                        }
                                        if (jsonObject.has("MessageBody")) {
                                            message = jsonObject.getString("MessageBody");
                                        }
                                        if (jsonObject.has("MessageTime")) {
                                            msgtime = jsonObject.getString("MessageTime");
                                        }
                                        if (jsonObject.has("UserId")) {
                                            //String userIdNew=jsonObject.getString("UserId");
                                            if (fromJid.trim().equalsIgnoreCase(getFromjid)) {
                                                key_from_me = fromJid;
                                            } else {
                                                key_from_me = toJid;
                                            }
                                        }

                                        if (!message.startsWith(SingleChatRoomFrgament.sendfilefixString)) {
                                            if (!msgtime.equalsIgnoreCase("")) {
                                                boolean isMessageTime = GlobalData.dbHelper.alredyExistMessageTime(msgtime);
                                                if (!isMessageTime) {
                                                    Utils.addChatHistoryGettedFromServer(remote_jid, message, key_from_me, msgtime, "", "", msg_type);
                                                }
                                            }

                                        } else {

                                            String messagearray[] = message.split("__");
                                            String filetype = messagearray[2];
                                            String location = "";
                                            String mediathmb = "";
                                            if (!filetype.equals(GlobalData.Audiofile)) {
                                                mediathmb = messagearray[3];
                                            }
                                            if (filetype.equals(GlobalData.Locationfile)) {
                                                location = messagearray[4];

                                            }
                                            String fileurl = messagearray[1];
                                            String filePath = Utils.getfilePath(filetype);
                                            if (!msgtime.equalsIgnoreCase("")) {
                                                boolean isMessageTime = GlobalData.dbHelper.alredyExistMessageTime(msgtime);
                                                if (!isMessageTime) {

                                                    GlobalData.dbHelper.addchatFileToMessagetable(
                                                            remote_jid, filePath,
                                                            filetype, key_from_me, "S",
                                                            fileurl, location,
                                                            mediathmb, msgtime, "", "", false);//set blank msgPacket id and msgsendstatus
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        } else {

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            Utils.printLog("getSingleUserMessageHistory Response1112:  " + response);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        try {
            if (loading_text != null) {
                loading_text.setText("Loading message history...");
            }
            if (progress != null) {
                progress.setVisibility(View.GONE);

                SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament.newInstance("");
                if (chatRoomFrgament != null) {
                    if (fromTime.equalsIgnoreCase("0")) {
                        chatRoomFrgament.refreshChatAdapter();
                    } else if (isRefreshAfterDownload) {
                        chatRoomFrgament.getMoreChatHistoryDownlodedFromServerMethod();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPostExecute(result);
    }
}

