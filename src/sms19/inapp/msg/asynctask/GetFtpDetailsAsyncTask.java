package sms19.inapp.msg.asynctask;

import org.json.JSONArray;
import org.json.JSONObject;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import android.os.AsyncTask;

public class GetFtpDetailsAsyncTask extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;
	
	private InAppMessageActivity activity=null;

	public GetFtpDetailsAsyncTask(InAppMessageActivity activity){
		
		this.activity=activity;
	}


	@Override
	protected Void doInBackground(Void... params) {

	
		try {
		
			String stringUrl=Apiurls.KIT19_BASE_URL+"GetFTPHostDetail&FileType=image";
			stringUrl=stringUrl.replace(" ","");
			Rest rest= Rest.getInstance();
			response = rest.sendCheckcontactRequest("",stringUrl);   



			Utils.printLog("GetFTPHostDetail Response1112:  " + response);

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

					
					if(resObj.has("GetFTPHostDetail")){

						JSONArray array=resObj.getJSONArray("GetFTPHostDetail");
						if(array!=null){
							JSONObject jsonObject=array.getJSONObject(0);
							if(jsonObject.has("FtpUser")){
								GlobalData.FTP_USER =jsonObject.getString("FtpUser");
								
							}
							if(jsonObject.has("FtpPassword")){
							
								GlobalData.FTP_PASS =jsonObject.getString("FtpPassword");
							}
							if(jsonObject.has("HostName")){
								GlobalData.FTP_HOST =jsonObject.getString("HostName");
							}
							if(jsonObject.has("FtpUrl")){
								GlobalData.FTP_URL =jsonObject.getString("FtpUrl");
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









