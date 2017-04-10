package sms19.inapp.msg.asynctask;

import org.json.JSONArray;
import org.json.JSONObject;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import android.os.AsyncTask;

public class GetBaseUrlApi extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;

	private InAppMessageActivity activity=null;

	public GetBaseUrlApi(){

		
	
	}


	@Override
	protected Void doInBackground(Void... params) {

	
		try {
			

			String stringUrl=Apiurls.FIRST_BASE_URL;
			stringUrl=stringUrl.replace(" ","");
			Rest rest= Rest.getInstance();
			response = rest.sendCheckcontactRequest("",stringUrl);   



			Utils.printLog("Base URL Response1112:  " + response);

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
					if(resObj.has("AutomatedServicesURL")){
						if(resObj!=null){
						JSONArray array=resObj.getJSONArray("AutomatedServicesURL");
						JSONObject jsonObject=array.getJSONObject(0);
						if(jsonObject!=null){
							if(jsonObject.has("ServicesUrl")){
								Apiurls.KIT19_BASE_URL=jsonObject.getString("ServicesUrl");
							}
						}
						
						
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









