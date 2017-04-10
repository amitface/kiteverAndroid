package sms19.inapp.msg.asynctask;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Rest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class CheckCreditAsyncTask extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;
	private	String userId="";
	private	String mobile_numbers="";
	private	String message="";
	private	String unString="";
	private Activity activity;
	private ProgressDialog progressDialog=null;
	private sms19.inapp.single.chatroom.SingleChatRoomFrgament singleChat;
	private String msg_type="";
	private String lang="en";
	private String message_packet_Id="";
	
	public CheckCreditAsyncTask(Activity activity ,String userId,String mobile_numbers,String message ,String unString,String msg_type,String message_packet_Id){

		this.userId=userId;
		this.mobile_numbers=mobile_numbers;
		this.message=message;
		this.unString=unString;
		this.activity=activity;
		this.msg_type=msg_type;// mean message is url or not 1 mean url and 0 mean text
		singleChat=sms19.inapp.single.chatroom.SingleChatRoomFrgament.newInstance("");
		this.message_packet_Id=message_packet_Id;


	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(activity!=null){
		progressDialog=new ProgressDialog(activity);
		progressDialog.setMessage("Checking credit...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		}
		
	}


	@Override
	protected Void doInBackground(Void... params) {

		//	String c_code ="+91";
		String postContactNo="";
		String postContactName="";
	//	String postCountryCode="";
		



		try {


			if(!message.equalsIgnoreCase("")){
				 try {
					lang=Utils.LanguageDetect(message);
					// lang="en";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(lang.equalsIgnoreCase("en")){
				unString="0";
			}/*else if(lang.equalsIgnoreCase("hi")){
				unString="1";
			}*/else{
				unString="1";
			}
			
			String regexPattern = "[\uD83C-\uDBFF\uDC00-\uDFFF]+";
			try {
				byte[] utf8 = message.getBytes("UTF-8");

				String string1 = new String(utf8, "UTF-8");

				Pattern pattern = Pattern.compile(regexPattern);
				Matcher matcher = pattern.matcher(string1);
				
				boolean isEmoji=false;
				while (matcher.find()) {
					isEmoji=true;
					break;
				}
				if(isEmoji){
					unString="2";
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			JSONArray array=new JSONArray(mobile_numbers);
			if(array!=null){
				for(int i=0;i<array.length();i++){
					JSONObject jsonObject=array.getJSONObject(i);
					if(postContactNo.equalsIgnoreCase("")){
						postContactNo=Utils.removeCountryCode(jsonObject.getString("phonenumber"),activity);
						postContactName=jsonObject.getString("name").replace(" ", "_");
						
					}else{

						String	Child2	=		Utils.removeCountryCode(jsonObject.getString("phonenumber"),activity);
						postContactNo=postContactNo+","+Child2.trim();
						postContactName=postContactName+","+jsonObject.getString("name").replace(" ", "_");
					
					}
				}

			}
			String stringUrl=Apiurls.KIT19_BASE_URL+"CheckCredit&Userid="+userId+"&mobile="+postContactNo.trim()+
					"&message="+message.trim()+"&IsUnicode="+unString;
			stringUrl=stringUrl.replace(" ","");
			Rest rest= Rest.getInstance();
			response = rest.sendCheckcontactRequest("",stringUrl);   
			GlobalData.registerContactList = new ArrayList<Contactmodel>();


			Utils.printLog("Check CheckCredit Response1112:  " + response);

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		if(progressDialog!=null){
			progressDialog.dismiss();
		}
		

		if (response != null && response.trim().length() != 0) {
			try {
				resObj = new JSONObject(response);
				if(resObj.has("CheckCredit")){

					JSONArray array=resObj.getJSONArray("CheckCredit");
					if(array!=null){

						for(int i=0;i<1;i++ ){
							if(activity!=null){
								JSONObject jsonObject=array.getJSONObject(i);
								if(jsonObject.has("Column1")){
									if(jsonObject.getString("Column1").equalsIgnoreCase("Sent")){
										//singleChat.sendBroadCastThroughApi(message.trim(),msg_type,true,message_packet_Id);
										Toast.makeText(activity, jsonObject.getString("Column1"), Toast.LENGTH_SHORT).show();
									}else{
										//singleChat.sendBroadCastThroughApi(message.trim(),msg_type,false,message_packet_Id);
										//Toast.makeText(activity, jsonObject.getString("Column1"), Toast.LENGTH_SHORT).show();
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
		} else {



		}

		super.onPostExecute(result);
	}


}







