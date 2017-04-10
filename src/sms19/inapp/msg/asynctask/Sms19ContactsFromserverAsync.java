package sms19.inapp.msg.asynctask;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.model.ContactDetailsNew;
import sms19.inapp.msg.rest.Rest;
import sms19.listview.newproject.model.FetchAllContact;
import android.os.AsyncTask;

public class Sms19ContactsFromserverAsync extends AsyncTask<FetchAllContact, Void, Void> {
	String response = "";
	JSONObject resObj;
	String userId="";
	String postContactNo="";
	/*Home activity;

	public Sms19ContactsFromserverAsync(Home activity,String userId){

		this.userId=userId;

		this.activity=activity;
	}*/



	ArrayList<ContactDetailsNew> arrayList=new ArrayList<ContactDetailsNew>();
	@Override
	protected Void doInBackground(FetchAllContact... params) {

		try {
			String stringUrl=Apiurls.KIT19_BASE_URL+"GetContactByUserid&Userid="+userId;
			Rest rest= Rest.getInstance();
			response = rest.sendCheckcontactRequest("",stringUrl);

			JSONObject jsonObject=new JSONObject(response.toString());
			//Utils.mCreateAndSaveFile(response.toString(),"kartik");
			JSONArray array=jsonObject.getJSONArray("ContactDetails");


			if(array!=null){
				for(int i=0;i<array.length();i++){
					JSONObject jsonObject2=array.getJSONObject(i);
					if(jsonObject2!=null){
						ContactDetailsNew  obj= new ContactDetailsNew();
						if(jsonObject2.has("contact_name")){
							obj.setContact_name(jsonObject2.getString("contact_name"));
						}
						if(jsonObject2.has("contact_mobile")){
							obj.setContact_mobile(jsonObject2.getString("contact_mobile"));
						}

						arrayList.add(obj);
					}

				}
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

	/*	if(arrayList!=null && arrayList.size()>0){
			if(activity!=null){
				activity.addSms19ContactsInDb(arrayList);
			}else{
				Utils.addSms19ContactsInDb(arrayList);
			}
		}*/


	}

}

