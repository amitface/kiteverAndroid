package sms19.inapp.msg.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import android.os.AsyncTask;

import com.google.gson.Gson;

public class ForgotPasswordAsyncTask extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	
	private sms19.listview.newproject.ForgotPassword activity=null;
	
	private String username=""; 
    
	
	public ForgotPasswordAsyncTask(sms19.listview.newproject.ForgotPassword forgotPassword ,String username){

		this.activity=forgotPassword;
		this.username=username;
		

	}


	@Override
	protected Void doInBackground(Void... params) {
		
		
		try {
			Rest rest= Rest.getInstance();
			List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("Mobile",username));
			nameValuePairs.add(new BasicNameValuePair("Page","ForgotPasswordApp" ));
			
			String stringUrl=Apiurls.KIT19_BASE_URL.replace("?Page=", "");
			if (stringUrl.equalsIgnoreCase("")) {
				stringUrl = Utils.getBaseUrlValue(activity);
				if (stringUrl.equalsIgnoreCase("")) {
					stringUrl = "http://kitever.com/NewService.aspx?Page=";
				}
			}
			stringUrl=stringUrl.replace(" ","");
			response = rest.post(stringUrl, nameValuePairs);   
			Utils.printLog("Base URL Forgot Pass:  " + response);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		
		
		try {
		
			
			try
			{
				Gson g = new Gson();
				activity.onSuccess(g.fromJson(response, sms19.listview.newproject.model.ForgotPasswordModel.class));
			} 
		catch (Exception e)
				{
				e.printStackTrace();
				activity.onError("Please Try Again Later");
				}
			
			//}
		} catch (Exception e) {
			e.printStackTrace();
		} 

		
		
		super.onPostExecute(result);
	}


}










