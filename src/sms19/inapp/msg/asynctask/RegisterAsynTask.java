package sms19.inapp.msg.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import sms19.listview.newproject.model.UserRegistrationModel;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class RegisterAsynTask  extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;

	private InAppMessageActivity activity=null;
	private sms19.inapp.msg.model.RegisterModel regiterModel;
	private sms19.listview.newproject.SignUp signUpActivity;

	public RegisterAsynTask(sms19.listview.newproject.SignUp signUpActivity ,sms19.inapp.msg.model.RegisterModel regiterModel){

		this.regiterModel=regiterModel;
		this.signUpActivity=signUpActivity;

	}


	@Override
	protected Void doInBackground(Void... params) {


		try {



			Rest rest= Rest.getInstance();

			List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("Page","UserRegistationApp" ));
			nameValuePairs.add(new BasicNameValuePair("Name",regiterModel.getName()));
			nameValuePairs.add(new BasicNameValuePair("CompanyName",regiterModel.getCompanyname()));
			nameValuePairs.add(new BasicNameValuePair("Mobile",regiterModel.getUserNumber() ));
			nameValuePairs.add(new BasicNameValuePair("Email",regiterModel.getEmail() ));
			nameValuePairs.add(new BasicNameValuePair("LoginType",regiterModel.getLogin_type() ));
			nameValuePairs.add(new BasicNameValuePair("MerchantCode",regiterModel.getVerifyCode() ));
			nameValuePairs.add(new BasicNameValuePair("UserCategory",regiterModel.getUserCategory() ));
			nameValuePairs.add(new BasicNameValuePair("UserLogin",regiterModel.getUserLogin() ));
			nameValuePairs.add(new BasicNameValuePair("CountryCode",regiterModel.getCountryCode() ));

			Log.i("register","namevalue-"+nameValuePairs.toString());
			
			String stringUrl=Apiurls.KIT19_BASE_URL.replace("?Page=", "");
			stringUrl=stringUrl.replace(" ","");
			response = rest.post(stringUrl, nameValuePairs);   



			Utils.printLog("Base URL Regiter:  " + response);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {


		try {
			//if (response != null && response.trim().length() != 0) {
				try {
					try {
						Gson g = new Gson();
						signUpActivity.onSuccessNew(g.fromJson(response, UserRegistrationModel.class));
					} 
					catch (Exception e)
					{
						e.printStackTrace();
						signUpActivity.onErrorNew("Please Try Again Later");
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		} 



		super.onPostExecute(result);
	}


}










