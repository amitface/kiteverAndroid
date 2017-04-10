package sms19.inapp.msg.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream.Activate;
import org.json.JSONObject;

import sms19.inapp.msg.BroadCastGroupProfile;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Rest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

public class BroadCastParticipantDeleteAsyncTask extends AsyncTask<Void, Void, Void> {
    private String response = "";
    private JSONObject resObj;
    private String userId = "";
    //private String userjid="";

    private String grouptitle = "";
    String users_name = "";
    String users_jid = "";
    String expiry_time = "10";
    String messageStr = "hello jack";
    String groupJid = "";
    //private ArrayList<Contactmodel> selectedmemberlist=null;
    private BroadCastGroupProfile broadCastGroupProfile = null;
    private Activity activity;

    public BroadCastParticipantDeleteAsyncTask(Activity activity, SharedPreferences chatPrefs, String userId,
                                               String mobileNoStr, String grouptitle, ArrayList<Contactmodel> selectedmemberlist, String groupJid, BroadCastGroupProfile broadCastGroupProfile) {

        this.userId = userId;
        this.users_jid = mobileNoStr;
        this.activity = activity;

        this.grouptitle = grouptitle;
        //this.selectedmemberlist=selectedmemberlist;
        this.groupJid = groupJid;
        this.broadCastGroupProfile = broadCastGroupProfile;
    }


    @Override
    protected Void doInBackground(Void... params) {

        //	String c_code ="+91";
        //	String postContactNo=mobileNoStr;
        //	String postCountryCode="";
        try {
            /*postContactNo=Utils.remove91(postContactNo);
            postCountryCode="+91";
			String mobileNoStr="";*/
			
			/*String users_jid = "";
			for (int i = 0; i < selectedmemberlist.size(); i++) {
				if (i == 0) {
					users_jid = selectedmemberlist.get(i).getRemote_jid();
					users_name=selectedmemberlist.get(i).getName();
					mobileNoStr=Utils.remove91(selectedmemberlist.get(i).getRemote_jid().split("@")[0]);
					postCountryCode="+91";
				} else {
					users_jid = users_jid + ","+ selectedmemberlist.get(i).getRemote_jid();
					users_name = users_name + ","+ selectedmemberlist.get(i).getName();
					postCountryCode = postCountryCode + ","+ "+91";
					mobileNoStr=mobileNoStr + ","+Utils.remove91(selectedmemberlist.get(i).getRemote_jid().split("@")[0]);

				}
			}
*/


            //response = rest.insertBroadcastGroup(stringUrl);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", userId));
            nameValuePairs.add(new BasicNameValuePair("UserJID", users_jid));
            nameValuePairs.add(new BasicNameValuePair("GroupJID", groupJid));

            nameValuePairs.add(new BasicNameValuePair("Page", "DeleteBroadCastParticipate"));

            Utils.printLog("DeleteBroadCastParticipate POST DATA:  " + nameValuePairs.toString());

            Rest rest2 = Rest.getInstance();

            response = rest2.post(Apiurls.KIT19_BASE_URL.replace("?Page=", ""), nameValuePairs);
            //response = rest2.post(Apiurls.KIT19_POST_BASE_URL.replace("?Page=", ""), nameValuePairs);

            Utils.printLog("DeleteBroadCastParticipate URL:  " + Apiurls.KIT19_POST_BASE_URL.replace("?Page=", ""));
            Utils.printLog("DeleteBroadCastParticipate Response1112:  " + response);


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
                    if (resObj.has("Message")) {

                        String jsonObject = resObj.getString("Message");
                        Toast.makeText(activity, jsonObject, Toast.LENGTH_SHORT).show();
                        if (jsonObject.equalsIgnoreCase("Delete successfully")) {
                            GlobalData.dbHelper.deleteGroupMemberFromDB(groupJid, users_jid);


                            if (broadCastGroupProfile != null) {
                                broadCastGroupProfile.callHandler(true);
                            }
                        }

                    } else {
                        Toast.makeText(activity, "Failed add paticipant!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, "Failed add paticipant!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Failed add paticipant!", Toast.LENGTH_SHORT).show();
        }


        super.onPostExecute(result);
    }


}









