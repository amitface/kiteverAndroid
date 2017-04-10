package sms19.inapp.msg.asynctask;

import org.json.JSONArray;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import android.app.Activity;
import android.os.AsyncTask;

public class GroupParticipantLimitationAsyncTask extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;
	private	String user_type="";

	private Activity activity=null;

	public GroupParticipantLimitationAsyncTask(Activity activity,String user_type){

		this.user_type=user_type;

		this.activity=activity;
	}


	@Override
	protected Void doInBackground(Void... params) {


		try {

			String stringUrl=Apiurls.KIT19_BASE_URL+"getNoOfParticipant&UserType="+user_type.trim()+"&ChatType=1";
			stringUrl=stringUrl.replace(" ","");
			Rest rest= Rest.getInstance();
			response = rest.sendGetRequest3(stringUrl); 
			Utils.printLog("getNoOfParticipant Response1112:  " + response);

			// Get Group Limit
			try {
				if (response != null && response.trim().length() != 0) {
					try {
						resObj = new JSONObject(response);
						if(resObj.has("NoOfParticipant")){

							JSONArray array=resObj.getJSONArray("NoOfParticipant");
							if(array!=null){

								for(int i=0;i<1;i++ ){
									if(activity!=null){
										JSONObject jsonObject=array.getJSONObject(i);
										if(jsonObject.has("Column1")){
											if(jsonObject.getInt("Column1")>0)
												if(activity!=null){
													GlobalData.GROUP_PARTICIPANT_LIMIT=jsonObject.getInt("Column1");													
													Utils.saveGroupLimit(activity, jsonObject.getInt("Column1"));

												}
										}
										//	Toast.makeText(activity, jsonObject.getString("Column1"), Toast.LENGTH_SHORT).show();
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

// Get BroadCast Limit
			

			stringUrl=Apiurls.KIT19_BASE_URL+"getNoOfParticipant&UserType="+user_type.trim()+"&ChatType=2";
			stringUrl=stringUrl.replace(" ","");

			response = rest.sendGetRequest3(stringUrl); 
			Utils.printLog("getNoOfParticipant Response1112:  " + response);

			try {
				if (response != null && response.trim().length() != 0) {
					try {
						resObj = new JSONObject(response);
						if(resObj.has("NoOfParticipant")){

							JSONArray array=resObj.getJSONArray("NoOfParticipant");
							if(array!=null){

								for(int i=0;i<1;i++ ){
									if(activity!=null){
										JSONObject jsonObject=array.getJSONObject(i);
										if(jsonObject.has("Column1")){
											if(jsonObject.getInt("Column1")>0)
												if(activity!=null){
													GlobalData.BRAODCAST_GROUP_PARTICIPANT_LIMIT=jsonObject.getInt("Column1");
													Utils.saveBroadCastGroupLimit(activity, jsonObject.getInt("Column1"));
												}
										}
										//	Toast.makeText(activity, jsonObject.getString("Column1"), Toast.LENGTH_SHORT).show();
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
					if(resObj.has("NoOfParticipant")){

						JSONArray array=resObj.getJSONArray("NoOfParticipant");
						if(array!=null){

							for(int i=0;i<1;i++ ){
								if(activity!=null){
									JSONObject jsonObject=array.getJSONObject(i);
									if(jsonObject.has("Column1")){
										if(jsonObject.getInt("Column1")>0)
											if(activity!=null){
												//GlobalData.GROUP_PARTICIPANT_LIMIT=jsonObject.getInt("Column1");
											//	GlobalData.BRAODCAST_GROUP_PARTICIPANT_LIMIT=jsonObject.getInt("Column1");
												//Utils.saveGroupLimit(activity, jsonObject.getInt("Column1"));
											}
									}
									//	Toast.makeText(activity, jsonObject.getString("Column1"), Toast.LENGTH_SHORT).show();
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








