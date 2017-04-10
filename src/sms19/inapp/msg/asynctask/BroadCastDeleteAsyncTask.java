package sms19.inapp.msg.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import android.os.AsyncTask;
import android.widget.Toast;

public class BroadCastDeleteAsyncTask extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;
	private	String userId="";
	private String broadcastid="";
	private int page=0;

	

	public BroadCastDeleteAsyncTask(String userId , String broadcastid,int page){

		this.userId=userId;
		this.broadcastid=broadcastid;
		
	}


	@Override
	protected Void doInBackground(Void... params) {

		//	String c_code ="+91";
		
		
		try {
			

			String stringUrl=Apiurls.KIT19_BASE_URL+"DeleteBroadCastGroup&UserId="+userId+"&BraodCastGroups="+broadcastid;
			stringUrl=stringUrl.replace(" ","");
			Rest rest= Rest.getInstance();
			 List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("Page","DeleteBroadCastGroup" ));
			nameValuePairs.add(new BasicNameValuePair("BraodCastGroups",broadcastid ));
			nameValuePairs.add(new BasicNameValuePair("UserId",userId ));
			
			
			//response = rest.deleteBroadCast(stringUrl);
			response = rest.post(Apiurls.KIT19_BASE_URL.replace("?Page=", ""), nameValuePairs);
			Utils.printLog("DeleteBroadCastGroup Response1112:  " + response);

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
//						Toast.makeText(get, resObj.getString("Message"), Toast.LENGTH_LONG).show();
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








