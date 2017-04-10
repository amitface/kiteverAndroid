package sms19.inapp.msg.asynctask;

import org.json.JSONObject;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

public class InviteAsyncTask extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;
	private	String userId="";
	private String mobileNoStr="";
	private InAppMessageActivity activity=null;
	String postCountryCode="";
	public InviteAsyncTask(InAppMessageActivity activity, SharedPreferences chatPrefs,String userId , String mobileNoStr,String postCountryCode){

		this.userId=userId;
		this.mobileNoStr=mobileNoStr;
		this.activity=activity;
		this.postCountryCode=postCountryCode;
	}


	@Override
	protected Void doInBackground(Void... params) {

		//	String c_code ="+91";
		String postContactNo=mobileNoStr;
		
		try {
//			postContactNo=Utils.remove91(postContactNo);
			if(postContactNo.startsWith("+")){
				postContactNo=postContactNo.substring(1);
			}

			String stringUrl=Apiurls.KIT19_BASE_URL+"Invite&User_id="+userId+"&phone_number="+postContactNo.trim()+
					"&country_code="+postCountryCode;
			stringUrl=stringUrl.replace(" ","");
			Rest rest= Rest.getInstance();
			response = rest.sendCheckcontactRequest("",stringUrl);   
			Utils.printLog("Invitation Response1112:  " + response);

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
					if(resObj.has("Invite")){
						Toast.makeText(activity, "Invitation has been sent to user successfully",Toast.LENGTH_SHORT).show();
						activity.backPress();
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








