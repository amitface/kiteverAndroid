package sms19.inapp.msg.asynctask;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;

public class GetSingleUserMessageHistory extends AsyncTask<Object, Void, Void> {
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

    public GetSingleUserMessageHistory(InAppMessageActivity activity, SharedPreferences chatPrefs, String userId, String fromJid, String toJid, String lastMessageTime, ProgressBar progress, TextView loading_text) {

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


            String stringUrl = Apiurls.KIT19_BASE_URL + "getMessageHistory&from=" + fromJid + "&to=" + toJid + "&fromTime=" + fromTime + "&toTime=0";
            /*String stringUrl="http://SMS19.com/NewService.aspx?Page=getMessageHistory&from="+"vaibhav_9@54.254.193.216"+"&to="+"vaibhav_1@54.254.193.216"+
					"&group="+"";*/
            stringUrl = stringUrl.replace(" ", "");
            Rest rest = Rest.getInstance();
            response = rest.getSingleUserMessageHistory(stringUrl);


            try {
                if (response != null && response.trim().length() != 0) {
                    try {

                        resObj = new JSONObject(response);
                        if (resObj.has("MessageHistory")) {
                            JSONArray jsonArray = resObj.getJSONArray("MessageHistory");
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
                                        String MessageStatus = "";


                                        String message_packetid = null;

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

                                        if (jsonObject.has("MessageStatus")) {
                                            MessageStatus = jsonObject.getString("MessageStatus");
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
                                                if (jsonObject.has("MessageXML") && jsonObject.getString("MessageXML").length() > 0) {

                                                    message_packetid = jsonObject.getString("MessageXML").substring(13, 22).trim();


                                                }

                                            }

                                        }


                                        if (!GlobalData.dbHelper.isContactBlock(getFromjid)) {

                                            if (!message.startsWith(SingleChatRoomFrgament.sendfilefixString)) {
                                                if (!msgtime.equalsIgnoreCase("")) {
                                                    boolean isMessageTime = GlobalData.dbHelper.alredyExistMessageTime(msgtime);
                                                    if (!isMessageTime) {
                                                        Utils.addChatHistoryGettedFromServer(remote_jid, message, key_from_me, msgtime, message_packetid, "", msg_type);
                                                    } else {
                                                        try {
                                                            GlobalData.dbHelper.updateMessageStatus(msgtime, MessageStatus);
                                                        } catch (Exception e) {
                                                            // TODO Auto-generated catch block
                                                            e.printStackTrace();
                                                        }

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
                                                                mediathmb, msgtime, message_packetid, "", false);//set blank msgPacket id and msgsendstatus


                                                    }
                                                    try {
                                                        GlobalData.dbHelper.updateMessageStatus(msgtime, MessageStatus);// add for broadcast message update
                                                    } catch (Exception e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                    }
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

            try {
                if (resObj.has("MessageHistory")) {
                    JSONArray jsonArray = resObj.getJSONArray("MessageHistory");
                    if (jsonArray == null || jsonArray.length() == 0) {
                        if (loading_text != null) {
                            loading_text.setText("Not found message history!");
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (loading_text != null) {
                    loading_text.setText("Not found message history!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPostExecute(result);
    }
}
