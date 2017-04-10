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

public class AddBroadCastParticipateAsyncTask extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;
	private	String userId="";

	private InAppMessageActivity activity=null;
	private String grouptitle="";
	String users_name="";
	String users_jid="";
	String expiry_time="10";
	String messageStr="hello jack";
	String groupJid="";
	private ArrayList<Contactmodel> selectedmemberlist=null;

	public AddBroadCastParticipateAsyncTask(InAppMessageActivity activity, SharedPreferences chatPrefs,String userId , 
			String mobileNoStr,String grouptitle,ArrayList<Contactmodel> selectedmemberlist,String groupJid){

		this.userId=userId;
	
		this.activity=activity;
		this.grouptitle=grouptitle;
		this.selectedmemberlist=selectedmemberlist;
		this.groupJid=groupJid;
	}


	@Override
	protected Void doInBackground(Void... params) {

		//	String c_code ="+91";
		
//		String postCountryCode="";
		try {
		
//			postCountryCode="+91";
			String mobileNoStr="";
			
			String users_jid = "";
			for (int i = 0; i < selectedmemberlist.size(); i++) {
				if (i == 0) {
					users_jid = selectedmemberlist.get(i).getRemote_jid();
					users_name=selectedmemberlist.get(i).getName();
					mobileNoStr=selectedmemberlist.get(i).getRemote_jid().split("@")[0];//Utils.remove91(selectedmemberlist.get(i).getRemote_jid().split("@")[0]);
//					postCountryCode="+91";
				} else {
					users_jid = users_jid + ","+ selectedmemberlist.get(i).getRemote_jid();
					users_name = users_name + ","+ selectedmemberlist.get(i).getName();
//					postCountryCode = postCountryCode + ","+ "+91";
					mobileNoStr=mobileNoStr + ","+selectedmemberlist.get(i).getRemote_jid().split("@")[0];

				}
			}
			
			//response = rest.insertBroadcastGroup(stringUrl);   
			 List<NameValuePair> nameValuePairs= new ArrayList<NameValuePair>();	
			nameValuePairs.add(new BasicNameValuePair("UserId", userId));
			nameValuePairs.add(new BasicNameValuePair("GroupJID",groupJid));
			nameValuePairs.add(new BasicNameValuePair("UserNumber",mobileNoStr ));
			nameValuePairs.add(new BasicNameValuePair("UserName",users_name ));
			nameValuePairs.add(new BasicNameValuePair("UserJID",users_jid ));
			nameValuePairs.add(new BasicNameValuePair("Page","AddBroadCastParticipate" ));
			
			
			
			Rest rest2= Rest.getInstance();
		    String	responseget = rest2.post(Apiurls.KIT19_POST_BASE_URL.replace("?Page=", ""), nameValuePairs);
		  //  String	responseget = rest2.post(Apiurls.KIT19_BASE_URL.replace("?Page=", ""), nameValuePairs);
			
		    
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
					if(resObj.has("InsertBroadcast")){
						
						
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









