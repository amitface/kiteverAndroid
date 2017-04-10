package sms19.inapp.msg.asynctask;

import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import android.os.AsyncTask;

public class MessageDeleteAsyncTask extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;
	private int isGroup=0;


	private String message_time="";

	public MessageDeleteAsyncTask(String message_time,int isGroup){
		this.message_time=message_time;
		this.isGroup=isGroup;

	}


	@Override
	protected Void doInBackground(Void... params) {

		//	String c_code ="+91";


		try {
			String stringUrl=Apiurls.KIT19_BASE_URL+"DeleteGroupChatMessage&Message_Time="+message_time;
			if(isGroup==1){
				stringUrl=Apiurls.KIT19_BASE_URL+"DeleteGroupChatMessage&Message_Time="+message_time;
			}else{
				stringUrl=Apiurls.KIT19_BASE_URL+"DeleteUserChatMessage&Message_Time="+message_time;
			}

			stringUrl=stringUrl.replace(" ","");
			Rest rest= Rest.getInstance();
			response = rest.sendCheckcontactRequest("",stringUrl);   


			Utils.printLog("Delete Message  Url"+stringUrl);
			Utils.printLog("Delete Message Response" + response);

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









