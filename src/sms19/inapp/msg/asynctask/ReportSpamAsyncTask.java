package sms19.inapp.msg.asynctask;

import org.json.JSONArray;
import org.json.JSONObject;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

public class ReportSpamAsyncTask extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;
	private	String userId="";
	private String mobileNoStr="";
	private InAppMessageActivity activity=null;
	private String remote_jid="";

	public ReportSpamAsyncTask(InAppMessageActivity activity, SharedPreferences chatPrefs,String userId , String mobileNoStr,String remote_jid){

		this.userId=userId;
		this.mobileNoStr=mobileNoStr;
		this.activity=activity;
		this.remote_jid=remote_jid;
	}


	@Override
	protected Void doInBackground(Void... params) {

		//	String c_code ="+91";
		String postContactNo=mobileNoStr;
		String postCountryCode="";
		try {
			postContactNo=Utils.removeCountryCode(postContactNo,activity);
			postCountryCode=Utils.getCountryCode(activity);

			//http://sms19.com/NewService.aspx?Page=ReportSpam&User_id=22338&phone_number=9509274990&country_code=+91

			String stringUrl=Apiurls.KIT19_BASE_URL+"ReportSpam&User_id="+userId+"&phone_number="+postContactNo.trim()+
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
					if(resObj.has("ReportSpam")){

						JSONArray jsonArray=resObj.getJSONArray("ReportSpam");
						if(jsonArray!=null){
							JSONObject jsonObject=jsonArray.getJSONObject(0);
							if(jsonObject!=null){
								if(jsonObject.has("Status")){
									GlobalData.dbHelper.reportSpamUpdate(remote_jid);
									Toast.makeText(activity, jsonObject.getString("Status"),Toast.LENGTH_SHORT).show();
								}
							}
						}

					
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








