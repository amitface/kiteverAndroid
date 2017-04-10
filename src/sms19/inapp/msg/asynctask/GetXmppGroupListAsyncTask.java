package sms19.inapp.msg.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Rest;

public class GetXmppGroupListAsyncTask extends AsyncTask<Void, Void, Void> {
    private String response = "";
    private JSONObject resObj;
    private String userId = "";
    private String mobileNoStr = "";


    String users_name = "";
    String users_jid = "";
    String expiry_time = "10";
    String messageStr = "hello jack";
    String my_jid = "";
    Context mcontext;

    public GetXmppGroupListAsyncTask(String userId, String my_jid, Context context) {
        mcontext = context;
        this.userId = userId;
        this.my_jid = my_jid;
    }


    @Override
    protected Void doInBackground(Void... params) {

        //	String c_code ="+91";
        String postContactNo = mobileNoStr;

        try {
//			postContactNo=Utils.removeCountryCode(postContactNo,mcontext);


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserJID", my_jid));
            nameValuePairs.add(new BasicNameValuePair("Page", "GetGroupListOfUser"));


            Rest rest = Rest.getInstance();

            response = rest.getXmppGroupList(Apiurls.KIT19_BASE_URL.replace("?Page=", ""), nameValuePairs);

            //response = rest.post(Apiurls.KIT19_BASE_URL.replace("?Page=", ""), nameValuePairs);
            Utils.printLog("Get Xmpp Groups:  " + response);


        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {


        try {
            if (response != null && response.trim().length() != 0) {
                try {
                    resObj = new JSONObject(response);
                    if (resObj.has("GroupListOfUser")) {

                        JSONArray array = resObj.getJSONArray("GroupListOfUser");

                        if (array != null) {


                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                String GroupName = "";
                                String GroupJID = "";
                                String description = "";
                                ArrayList<Contactmodel> selectedmembers = new ArrayList<Contactmodel>();

                                if (jsonObject != null) {


                                    if (jsonObject.has("GroupJID")) {

                                        GroupJID = jsonObject.getString("GroupJID");
                                        GroupJID = StringUtils.unescapeNode(GroupJID).replace(" ", "");
                                    }
                                    if (jsonObject.has("GroupName")) {

                                        GroupName = jsonObject.getString("GroupName");
                                        GroupName = StringUtils.unescapeNode(GroupName);
                                    }

                                    if (jsonObject.has("description")) {

                                        description = jsonObject.getString("description");
                                        description = StringUtils.unescapeNode(description);
                                    }

                                    if (GroupJID != null && GroupJID.length() > 0 && GroupJID.toString().contains("@")) {
                                        if (GlobalData.dbHelper.checkGroupAlreadyexist(GroupJID)) {
                                            GlobalData.dbHelper.deleteGroupRefresh(GroupJID);
                                        }
                                        if (!GlobalData.dbHelper.checkGroupAlreadyexist(GroupJID)) {
                                            String msgtime = Utils.currentTimeStap();
                                            ArrayList<Contactmodel> selectedmemberlist = new ArrayList<Contactmodel>();
                                            GlobalData.dbHelper.addnewGroupinDB(GroupJID, description,
                                                    null, selectedmemberlist, false, msgtime);
                                        }
                                    }
                                }
                            }
                            //{ "UsersNumbers": " 919509274990", "UsersName": "Test", "UsersJID": " 919509274990@103.25.130.241"}] } ] }

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








