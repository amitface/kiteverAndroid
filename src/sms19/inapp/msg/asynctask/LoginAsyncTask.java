package sms19.inapp.msg.asynctask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import sms19.listview.newproject.model.LoginModel;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class LoginAsyncTask extends AsyncTask<Void, Void, Void> {
	private String response = "";
	private JSONObject resObj;

	private InAppMessageActivity activity = null;
	private sms19.inapp.msg.model.RegisterModel regiterModel;
	private sms19.listview.newproject.LoginPage signUpActivity;
	private sms19.listview.newproject.SplashScreen splashScreen = null;
	private ProgressDialog dialog;

	public LoginAsyncTask(sms19.listview.newproject.LoginPage loginpage,
			sms19.listview.newproject.SplashScreen splashScreen,
			sms19.inapp.msg.model.RegisterModel regiterModel) {

		this.regiterModel = regiterModel;
		this.signUpActivity = loginpage;
		this.splashScreen = splashScreen;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (signUpActivity != null) {

			dialog = new ProgressDialog(signUpActivity);
			dialog.setMessage("Login user...");
			dialog.setCancelable(false);
			dialog.show();
		}

	}

	@Override
	protected Void doInBackground(Void... params) {

		Utils.printLog(" Start time_logger"
				+ new SimpleDateFormat("hh:mm:ss").format(new Date()) + "");
		try {
			Rest rest = Rest.getInstance();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("Mobile", regiterModel
					.getUserNumber()));
			nameValuePairs.add(new BasicNameValuePair("Password", regiterModel
					.getPwd()));
			if (regiterModel.getReLoginType() != 0) {
				nameValuePairs.add(new BasicNameValuePair("UserChoice",
						regiterModel.getUser_choise()));
				nameValuePairs.add(new BasicNameValuePair("Page",
						"AlreadyLogin"));
			} else {
				nameValuePairs.add(new BasicNameValuePair("Page", "LoginApp"));
			}

			nameValuePairs.add(new BasicNameValuePair("DeviceID", regiterModel
					.getDevice_id()));
			nameValuePairs.add(new BasicNameValuePair("Version", regiterModel
					.getVersion()));
			String stringUrl="";
			Log.i("nameValuePairs","nameValuePairs"+nameValuePairs.toString());
			stringUrl = Apiurls.KIT19_BASE_URL.replace("?Page=", "");
			if (stringUrl.equalsIgnoreCase("")) {
				stringUrl = Utils.getBaseUrlValue(splashScreen);
				if (stringUrl.equalsIgnoreCase("")) {
					stringUrl = "http://kitever.com/NewService.aspx?Page=";
				}
			}
			stringUrl = stringUrl.replace(" ", "");
			response = rest.post(stringUrl, nameValuePairs);
			Log.e("login","response"+response);
			Utils.printLog("Base URL Regiter:  " + response);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		Utils.printLog("GSon Start time_logger"
				+ new SimpleDateFormat("hh:mm:ss").format(new Date()) + "");
		try {
			// if (response != null && response.trim().length() != 0) {
			try {
				Gson g = new Gson();
				if (splashScreen == null) {
					signUpActivity.onSuccess(g.fromJson(response,
							LoginModel.class));
				} else {
					splashScreen.onSuccess(g.fromJson(response,
							LoginModel.class));
				}
				Utils.printLog("GSon Start ENDtime_logger"
						+ new SimpleDateFormat("hh:mm:ss").format(new Date())
						+ "");
			} catch (Exception e) {
				e.printStackTrace();
				// comes due to not getting value by model
				signUpActivity.onError("Please Try Again Later");
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (dialog != null) {
			dialog.dismiss();
		}

		Utils.printLog("End Gson time_logger"
				+ new SimpleDateFormat("hh:mm:ss").format(new Date()) + "");
		super.onPostExecute(result);
	}

}
