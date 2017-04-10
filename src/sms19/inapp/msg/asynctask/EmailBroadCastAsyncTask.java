package sms19.inapp.msg.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Rest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class EmailBroadCastAsyncTask extends AsyncTask<Void, Void, Void> {

	private	String response = "";
	private JSONObject resObj;
	private	String userId="";
	private String mobileNoStr="";
	private InAppMessageActivity activity=null;
	private String grouptitle="";
	private String users_name="";
	private String users_jid="";
	private String expiry_time="10";
	private String messageStr="hello jack";
	private String groupJid="";
	private String message_type="";
	private ArrayList<Contactmodel> selectedmemberlist=null;
	private String isuniCode="";
	private	String lang="en";
	private String message_packet_Id="";
	private String inAppCount="";
	private String InInboxCount="";
	private ProgressDialog progressDialog=null;
	private String message_id="";
	private boolean isEmailTemplateEnabled=false;
	private String templateId="";
	private String tempMsg=null;

	public EmailBroadCastAsyncTask(InAppMessageActivity activity, SharedPreferences chatPrefs,String userId 
			,String grouptitle,String groupJid,String message,String iscodetype,String message_type,
			String expiry_time,ArrayList<Contactmodel> selectedmemberlist,String message_packet_Id,String inAppCount,String InInboxCount,String msg_id,boolean isEmailTemplateEnabled,String templateId) {

		this.userId=userId;
		this.activity=activity;
		this.grouptitle=grouptitle;
		this.selectedmemberlist=selectedmemberlist;
		this.groupJid=groupJid;
		this.message_type=message_type;
		this.expiry_time=expiry_time;
		this.messageStr=message;
		this.message_packet_Id=message_packet_Id;
		this.inAppCount=inAppCount;
		this.InInboxCount=InInboxCount;
		this.message_id=msg_id;
		this.isEmailTemplateEnabled=isEmailTemplateEnabled;
		this.templateId=templateId;

	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		if(isEmailTemplateEnabled){
			tempMsg="";
		}else{
			tempMsg=messageStr;
		}
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("Page", "EmailSentFromChat"));
		nameValuePairs.add(new BasicNameValuePair("UserId", userId));
		nameValuePairs.add(new BasicNameValuePair("GroupJID", groupJid));
		nameValuePairs.add(new BasicNameValuePair("GroupName", grouptitle));
		nameValuePairs.add(new BasicNameValuePair("MessageBody", tempMsg));
		nameValuePairs.add(new BasicNameValuePair("Min", expiry_time));
		nameValuePairs.add(new BasicNameValuePair("TemplateId", templateId));
		Log.i("EmailSentFromChat",nameValuePairs.toString());
		Rest rest2 = Rest.getInstance();
		response = rest2.post(Apiurls.KIT19_BASE_URL.replace("?Page=", ""),
				nameValuePairs);
		return null;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (activity != null) {
			if (progressDialog == null)
				progressDialog = new ProgressDialog(activity);
			progressDialog.setMessage("Sending email...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
	}

	// {"MailStatus":[{"Status":"true","Message":"Mail Sent"}]}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		String status = "";
		String message = "";
		if (response != null && response.trim().length() != 0) {
			try {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject.has("MailStatus")) {
					JSONArray jsonArray = jsonObject.getJSONArray("MailStatus");
					if (jsonArray != null && jsonArray.length() > 0) {
						JSONObject obj = jsonArray.getJSONObject(0);
						if (obj != null) {
							if (obj.has("Status")) {
								status = obj.getString("Status");
							}
							if (obj.has("Message")) {
								message = obj.getString("Message");
							}
						}
					}
				}
				sms19.inapp.single.chatroom.SingleChatRoomFrgament singleChat = sms19.inapp.single.chatroom.SingleChatRoomFrgament.newInstance("");
				if(status!=null && status.equalsIgnoreCase("false")){
					if(sms19.inapp.single.chatroom.SingleChatRoomFrgament.isSmsCreditSelected){
						
						Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
					}else{
						singleChat.sendBroadCastAfterUpdateMsg(messageStr.trim(),"0",false,message_packet_Id,inAppCount,InInboxCount,message_id);
					}
//					singleChat.sendBroadCastAfterEmailUpdateMsg(messageStr);
				}else if(status!=null && status.equalsIgnoreCase("true")){
					if(!sms19.inapp.single.chatroom.SingleChatRoomFrgament.isSmsCreditSelected){
						singleChat.sendBroadCastAfterUpdateMsg(messageStr.trim(),message_type,true,message_packet_Id,inAppCount,InInboxCount,message_id);
					}
//					singleChat.sendBroadCastAfterEmailUpdateMsg(messageStr);
				}
				else{
					if(!sms19.inapp.single.chatroom.SingleChatRoomFrgament.isSmsCreditSelected){
						singleChat.sendBroadCastAfterUpdateMsg(messageStr.trim(),"0",false,message_packet_Id,inAppCount,InInboxCount,message_id);
					}
				}
				sms19.inapp.single.chatroom.SingleChatRoomFrgament.isSmsCreditSelected=false;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
