package sms19.inapp.msg.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Rest;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class InsertBroadcastGroupAssAynctask  extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;
	private	String userId="";
	private String mobileNoStr="";
	private InAppMessageActivity activity=null;
	private String grouptitle="";
	String users_name="";
	String users_jid="";
	String expiry_time="10";
	String messageStr="hello jack";
	String groupJid="";
	private ArrayList<Contactmodel> selectedmemberlist=null;

	public InsertBroadcastGroupAssAynctask(InAppMessageActivity activity, SharedPreferences chatPrefs,String userId , 
			String mobileNoStr,String grouptitle,ArrayList<Contactmodel> selectedmemberlist,String groupJid){

		this.userId=userId;
		this.mobileNoStr=mobileNoStr;
		this.activity=activity;
		this.grouptitle=grouptitle;
		this.selectedmemberlist=selectedmemberlist;
		this.groupJid=groupJid;
	}


	@Override
	protected Void doInBackground(Void... params) {

		//	String c_code ="+91";
		String postContactNo=mobileNoStr;
//		String postCountryCode="";
		try {
			postContactNo=Utils.removeCountryCode(postContactNo,activity);
//			postCountryCode="+91";
			String mobileNoStr="";
			
			String users_jid = "";
			for (int i = 0; i < selectedmemberlist.size(); i++) {
				if (i == 0) {
					users_jid = selectedmemberlist.get(i).getRemote_jid();
					users_name=selectedmemberlist.get(i).getName();
					mobileNoStr=Utils.removeCountryCode(selectedmemberlist.get(i).getRemote_jid().split("@")[0],activity);
//					postCountryCode="+91";
				} else {
					users_jid = users_jid + ","+ selectedmemberlist.get(i).getRemote_jid();
					users_name = users_name + ","+ selectedmemberlist.get(i).getName();
//					postCountryCode = postCountryCode + ","+ "+91";
					mobileNoStr=mobileNoStr + ","+Utils.removeCountryCode(selectedmemberlist.get(i).getRemote_jid().split("@")[0],activity);

				}
			}

			List<NameValuePair> nameValuePairs= new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", userId));
			nameValuePairs.add(new BasicNameValuePair("group_title",grouptitle));
			nameValuePairs.add(new BasicNameValuePair("group_jid",groupJid));
			nameValuePairs.add(new BasicNameValuePair("users_numbers",mobileNoStr ));
			nameValuePairs.add(new BasicNameValuePair("users_name",users_name ));
			nameValuePairs.add(new BasicNameValuePair("users_jid",users_jid ));
			nameValuePairs.add(new BasicNameValuePair("Page","CreateBroadcastGroupAndContacts" ));
			
			Rest rest2= Rest.getInstance();
			String	responseget = rest2.post(Apiurls.KIT19_BASE_URL.replace("?Page=", ""), nameValuePairs);
			
			Utils.printLog("InsertBroadcast URL:  " + Apiurls.KIT19_BASE_URL.replace("?Page=", ""));
			Utils.printLog("InsertBroadcast Response1112:  " + responseget);
			Utils.printLog("Get Broadcast:  " + responseget);
			
			
			
			


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
					
					if(resObj.has("Message")){
					if(resObj.getString("resObj").equalsIgnoreCase("Partipent Added successfully"))	{
						
					}
						
					}else {

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








