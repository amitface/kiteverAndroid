package sms19.inapp.msg.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Rest;
import android.content.Context;
import android.os.AsyncTask;

public class GetBroadCastGroupList extends AsyncTask<Void, Void, Void> {
	private	String response = "";
	private JSONObject resObj;
	private	String userId="";
	private String mobileNoStr="";


	String users_name="";
	String users_jid="";
	String expiry_time="10";
	String messageStr="hello jack";
	String my_jid="";
	Context mContext;
	private ArrayList<Contactmodel> selectedmemberlist=null;

	public GetBroadCastGroupList(String userId,String my_jid,Context context){

		this.userId=userId;	
		this.my_jid=my_jid;
		mContext=context;
	}


	@Override
	protected Void doInBackground(Void... params) {

		//	String c_code ="+91";
		String postContactNo=mobileNoStr;

		try {
			postContactNo=Utils.removeCountryCode(postContactNo,mContext);
			String stringUrl="";
			stringUrl=Apiurls.KIT19_BASE_URL+"GetBroadcastGroup&user_id="+userId;

			stringUrl=stringUrl.replace(" ","");

			stringUrl=stringUrl.replace(" ","");
			Rest rest= Rest.getInstance();
			response = rest.getBroadcastGroupList(stringUrl.trim());
			/* List<NameValuePair> list= new ArrayList<NameValuePair>();
			 list.add(new BasicNameValuePair("Page","GetBroadcastGroup" ));
			 list.add(new BasicNameValuePair("user_id", userId));
			response = rest.postGetRequestAsGet(Apiurls.KIT19_BASE_URL.replace("?Page=", "").trim(),list);*/ 

			Utils.printLog("Get Broadcast:  " + response);


		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {


		try {

			/*try {
				Utils.getXmppGroupList(userId, my_jid);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/



			if (response != null && response.trim().length() != 0) {
				try {
					resObj = new JSONObject(response);
					if(resObj.has("BroadcastGroups")){


						JSONArray array=resObj.getJSONArray("BroadcastGroups");

						if(array!=null){


							for(int i=0;i<array.length();i++){
								JSONObject jsonObject=array.getJSONObject(i);
								String broadcastlistname="";
								String broadcastid="";
								String username="";
								ArrayList<Contactmodel> selectedmembers=new ArrayList<Contactmodel>();

								if(jsonObject!=null){


									if(jsonObject.has("GroupTitle")){

										broadcastlistname=jsonObject.getString("GroupTitle");
									}
									if(jsonObject.has("GroupJID")){

										broadcastid=jsonObject.getString("GroupJID");
									}
									


									JSONArray array2=jsonObject.getJSONArray("ParticipantList");
									if(array2!=null){
										for(int j=0;j<array2.length();j++){
											Contactmodel contactmodel=new Contactmodel();
											JSONObject jsonObject2=array2.getJSONObject(j);
											if(jsonObject2!=null){

												if(jsonObject2.has("UsersJID")){

													contactmodel.setRemote_jid(jsonObject2.getString("UsersJID"));
												}
												if(jsonObject2.has("UsersNumbers")){

													contactmodel.setNumber(jsonObject2.getString("UsersNumbers"));
												}
												if(jsonObject.has("UsersName")){

													username=jsonObject.getString("UsersName");
													contactmodel.setName(username);
												}
//												Utils.contactmodelGlobalList.add(contactmodel);
											}

											selectedmembers.add(contactmodel);

										}
									}

								}
								if(selectedmembers!=null&&selectedmembers.size()>0){
									if(GlobalData.dbHelper.checkGroupAlreadyexist(broadcastid)){
										GlobalData.dbHelper.deleteGroupRefresh(broadcastid);
									}
									if(!GlobalData.dbHelper.checkGroupAlreadyexist(broadcastid)){
										String msgtime = Utils.currentTimeStap();
										
										GlobalData.dbHelper.addnewBroadcastinDBGettedFromServer(broadcastid,broadcastlistname, selectedmembers,msgtime);

										String sendDefaltmsg = broadcastlistname + "Created...";

										long rowid = GlobalData.dbHelper.addchatToMessagetable(
												broadcastid, sendDefaltmsg, my_jid, msgtime,"","","0");// not set yet msgPacket id for
										// group so blank put
										if (rowid != -1) {
											GlobalData.dbHelper.addorupdateRecentTable(broadcastid,
													String.valueOf(rowid));
											GlobalData.dbHelper.updateContactmsgData(broadcastid,
													sendDefaltmsg, msgtime);
											Utils.printLog("Broadcast completed successfully....");

										}
									}

								}


							}



							//{ "UsersNumbers": " 919509274990", "UsersName": "Test", "UsersJID": " 919509274990@103.25.130.241"}] } ] }



						}
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








