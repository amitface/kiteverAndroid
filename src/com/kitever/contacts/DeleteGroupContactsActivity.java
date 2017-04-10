package com.kitever.contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.models.ContactInfo;
import com.kitever.sendsms.ContactsAdapter;
import com.kitever.utils.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class DeleteGroupContactsActivity extends AppCompatActivity implements AdapterInterface{


	private ProgressDialog progressDialog = null;

	private Menu menu;
	
	private ArrayList<ContactInfo> arrayList=null;
	private ListView mListView;
	private String groupId;
	private String groupName,userId;
	private TextView delcontact_title;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from activity_main.xml
		setContentView(R.layout.activity_deletegroupcontacts);

		actionBarSettingWithBack(this,getSupportActionBar(),"Delete Group Contacts");
		
		mListView = (ListView) findViewById(R.id.listView);
		delcontact_title=(TextView) findViewById(R.id.delcontact_title);

		setRobotoThinFont(delcontact_title,this);
		delcontact_title.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

		groupName=getIntent().getStringExtra("GROUP_NAME");
		userId =getIntent().getStringExtra("userId");
		groupId =getIntent().getStringExtra("groupId");
		arrayList=(ArrayList<ContactInfo>) getIntent().getSerializableExtra("GROUP_CONTACTS_TO_DELETE");
	
		final ContactsAdapter adapter = new  ContactsAdapter(DeleteGroupContactsActivity.this,arrayList,DeleteGroupContactsActivity.this);
		mListView.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contacts_fragment_menu, menu);
		this.menu = menu;
		updateMenuTitles(0);
		return true;
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == android.R.id.home) {
			finish();
			return true;
		}

		if (id == R.id.action_done) {
			initDelete();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	ArrayList<String> numberJid;
//	int count=0;
	private void initDelete(){
		showProgressDialog();

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try{
					final JSONObject jsonObject = new JSONObject();
					
					JSONArray jsonArray = new JSONArray();
					
				
					if(arrayList!=null){
						numberJid=new ArrayList<>();
						for (ContactInfo contactInfo : arrayList) {
							if (contactInfo.isSelected) {
								//{"contactMobile":"9983186100","countryCode":"+91"}
								JSONObject jsonObject2 = new JSONObject();
								jsonObject2.put("contactMobile",contactInfo.Contact_Mobile );
								jsonObject2.put("countryCode", contactInfo.countryCode);
								jsonArray.put(jsonObject2);
								numberJid.add(contactInfo.countryCode+contactInfo.Contact_Mobile+"@email19.in");
//								if(count==0){
//									numberJid=numberJid+contactInfo.countryCode+contactInfo.Contact_Mobile+"@email19.in";
//								}else{
//									numberJid=numberJid+","+contactInfo.countryCode+contactInfo.Contact_Mobile+"@email19.in";
//								}
//								count++;
							}
						}	
					}else{
						
					}
					
					
					
					jsonObject.put("contacts", jsonArray);
					
					
			
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						deleteGroupContactRequest(jsonObject.toString());
					}
				});
				
				
				}catch(Exception exp){
					removeProgressDialog();
				}
				
			}
		}).start();
	}


	
	private void deleteGroupContactRequest(final String str) {

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				Apiurls.getBasePostURL(), new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						
						sms19.inapp.msg.constant.Utils.printLog2("deleteGroupContactRequest\n"+response);
						
						
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(response);
							if (jsonObject != null) {
								JSONArray array = jsonObject
										.getJSONArray("DeleteContactGroupRegistration");
								JSONObject jsonObject2 = array.getJSONObject(0);

								String msg = jsonObject2.getString("Msg");
								Toast.makeText(getApplicationContext(), msg,
										Toast.LENGTH_LONG).show();
								if(numberJid!=null && numberJid.size()>0){
									for(int k=0;k<numberJid.size();k++){
										GlobalData.dbHelper.deleteGroupMemberFromDB(groupId,numberJid.get(k));
									}
								}
								removeProgressDialog();
								setResult(RESULT_OK);
//								GlobalData.dbHelper.updateGroupmemberList(groupId, numberJid);
								finish();
								
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							removeProgressDialog();
							e.printStackTrace();
						}
						
						
					
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(DeleteGroupContactsActivity.this, error.toString(),
								Toast.LENGTH_LONG).show();

						removeProgressDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				
	        /*  case "DeleteGroupContact":
	                Response.Write(objser.DeleteGroupContact(Request.Form["Userid"], Request.Form["GroupName"], 
	                		Request.Form["contactMobile"], Request.Form["countryCode"]));
	                break;*/
				
				/*Userid:25062
				Page: DeleteContacts
				contacts:{"contacts":[{"contactMobile":"9983186100","countryCode":"+91"}]}*/
				params.put("Page", "DeleteGroupContact");
				params.put("Userid",userId);
				params.put("GroupName",groupName);
				params.put("contacts",str);
			
				return params;
			}

		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}

	public void updateMenuTitles(int selected) {

		if (this.menu != null) {
			
			MenuItem action_doneMenuItem = this.menu.findItem(R.id.action_done);
			if (selected > 0) {
				action_doneMenuItem.setEnabled(true);
				//action_doneMenuItem.setVisible(true);
				action_doneMenuItem.setTitle("(" +selected + ")" + " DELETE");
			} else {
				action_doneMenuItem.setEnabled(false);
				//action_doneMenuItem.setVisible(false);
				action_doneMenuItem.setTitle("DELETE");
			}

		}

	}



	public void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(DeleteGroupContactsActivity.this);
			progressDialog.setTitle("Loading");
			progressDialog.setMessage("Please wait...");
			progressDialog.show();
		} else {
			if (!progressDialog.isShowing()) {
				progressDialog.show();
			}
		}
	}

	public void removeProgressDialog() {
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}


	@Override
	public void onItemClicked(int position, int forWhat) {
		// TODO Auto-generated method stub
		if(arrayList!=null){
			int selected = 0;
			for (ContactInfo contactInfo : arrayList) {
				if (contactInfo.isSelected) {
					selected++;
				}
			}
			updateMenuTitles(selected);
		}
	}

	@Override
	public void onItemsEmpty() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemsAvailable() {
		// TODO Auto-generated method stub
		
	}




}
