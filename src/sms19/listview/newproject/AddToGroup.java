package sms19.listview.newproject;

import java.util.ArrayList;

import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

public class AddToGroup extends Activity {
	Button btn;
	TextView textViewCreateGroup;
	EditText editTextGroupName;
	/********************/

	ArrayList<String> bindGroup;
	String[] bindGroupString;
	Context currentACtivity;
	/********************/
	String Mobile = "";
	String UserId = "";
	String Password = "";
	String contactNumber, contactpersonname;
	DataBaseDetails dbObject = new DataBaseDetails(this);

	String groupName;

	String RecpientNumber = "", MobiRecpientNumber = "", RecipentName = "",
			MobiRecpientName = "";

	int countNUm;

	String name = "";
	String number = "";

	String dob = "", Anni = "";
	ImageView addgp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newpage);

		btn = (Button) findViewById(R.id.button1);
		textViewCreateGroup = (TextView) findViewById(R.id.textViewCreateGroup);
		editTextGroupName = (EditText) findViewById(R.id.editTextGroupName);// editTextGroupName
		addgp = (ImageView) findViewById(R.id.ingpadd);

		addgp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editTextGroupName.setVisibility(View.VISIBLE);
			}
		});
		/*************************** INTERNET ********************************/
		webservice._context = this;
		currentACtivity = this;
		/*************************** INTERNET ********************************/

		try {
			Intent u = getIntent();
			String a[] = u.getStringArrayExtra("allcheckedvalues");
			// Log.w("getall", "?????????????"+a+","+a.length);
			for (int i = 0; i < a.length; i++) {
				// System.out.println("checked data"+a[i]);
			}
		} catch (Exception e) {

		}
		textViewCreateGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Log.w("TAG","C/L:");

				editTextGroupName.setVisibility(View.VISIBLE);
			}
		});

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				return;

				/*
				 * 
				 * 
				 * 
				 * 
				 * 
				 * try {
				 * 
				 * groupName = editTextGroupName.getText().toString().trim();
				 * 
				 * 
				 * 
				 * if(Validation.hasText(editTextGroupName)) { try {
				 * if(NewContact.checkAll) {
				 * 
				 * NewContact.checkAll= false;
				 * 
				 * RecpientNumber = getIndividuleDetails();
				 * 
				 * RecipentName = getIndividuleDetailsName();
				 * 
				 * //Log.w("TAG","RecpientNumber"+RecpientNumber+"RecipentName:"+
				 * RecipentName);
				 * 
				 * // count commas of number countNUm=
				 * countNumber(RecpientNumber)+1;
				 * 
				 * //Log.w("TAG_____","COUNT:"+countNUm+"(countNUm-1):"+(countNUm
				 * -1));
				 * 
				 * for( int i=0; i<(countNUm-1); i++ ) { dob += ","; Anni +=
				 * ","; }
				 * 
				 * //Log.w("TAG_____","dob:"+dob+"Anni:"+Anni);
				 * 
				 * 
				 * name = removeBackComma(RecipentName);
				 * 
				 * number = removeBackComma(RecpientNumber);
				 * 
				 * //Log.w("AFTER REMOVE:TAG_____","VALUE_______NAME:"+name+
				 * "_________NUMBER:"+number); }
				 * 
				 * else{
				 * 
				 * fetchMobileandUserId();
				 * 
				 * String values[] = getAllContactsTOaddIntoGroup();
				 * 
				 * //Log.w("TAG","VALUES_ADD_GROUP"+values+"UserID:"+UserId+"");
				 * 
				 * 
				 * 
				 * for(int i=0;i<values.length;i++) { String demo=values[i];
				 * 
				 * //Log.w("TAG","VALUES:ARRARY:::"+demo);
				 * 
				 * name += demo.substring(0,demo.indexOf(","))+","; number +=
				 * demo.substring(demo.indexOf(",")+1)+",";
				 * 
				 * }
				 * 
				 * //Log.w("TAG_____","VALUE_______NAME:"+name+"_________NUMBER:"
				 * +number);
				 * 
				 * // count commas of number countNUm= countNumber(number)+1;
				 * 
				 * //Log.w("TAG_____","COUNT:"+countNUm+"(countNUm-1):"+(countNUm
				 * -1));
				 * 
				 * for( int i=0; i<(countNUm-1); i++ ) { dob += ","; Anni +=
				 * ","; }
				 * 
				 * //Log.w("TAG_____","dob:"+dob+"Anni:"+Anni);
				 * 
				 * try { name = removeBackComma(name);
				 * 
				 * number = removeBackComma(number); } catch (Exception e) {
				 * 
				 * e.printStackTrace(); }
				 * 
				 * //Log.w("AFTER REMOVE:TAG_____","VALUE_______NAME:"+name+
				 * "_________NUMBER:"+number);
				 * 
				 * }
				 * 
				 * //Log.w("TAG_____","IF_FXN:::: number.length()"+number.length(
				 * ));
				 * 
				 * if(number.length()>0) {
				 * 
				 * //Log.w("TAG_____","welcome sir/ma'am");
				 * 
				 * // String Uid,String contact_person_name,String
				 * contactNumber,String conDob,String conAni, String GroupNam
				 * fetchMobileandUserId(); new webservice(null,
				 * webservice.InsertGroupContact.geturl(UserId, name, number,
				 * dob, Anni, groupName), webservice.TYPE_GET,
				 * webservice.TYPE_INSERT_GRP_CONT, new ServiceHitListener() {
				 * 
				 * @Override public void onSuccess(Object Result, int id) {
				 * GroupDataModel mod=(GroupDataModel) Result;
				 * 
				 * try { String EmergencyMessage =
				 * mod.getContactGroupRegistration
				 * ().get(0).getEmergencyMessage();
				 * 
				 * try { Emergency.desAct.finish(); } catch (Exception e) { }
				 * 
				 * if(!(EmergencyMessage.equalsIgnoreCase("NO"))) { Intent rt =
				 * new Intent(AddToGroup.this,Emergency.class);
				 * rt.putExtra("Emergency", EmergencyMessage);
				 * startActivity(rt);
				 * 
				 * } } catch (Exception e1) { e1.printStackTrace(); }
				 * 
				 * dbObject.Open(); dbObject.deleteSelectedContactsFromGroup();
				 * dbObject.close();
				 * 
				 * 
				 * try { NewContact.contactsGroupFlag.finish(); }
				 * catch(Exception e){}
				 * 
				 * NewContact.checkAll= false;
				 * 
				 * 
				 * if(mod.getContactGroupRegistration().get(0).getMsg().length()>
				 * 0) { Toast.makeText(getApplicationContext(),
				 * ""+mod.getContactGroupRegistration().get(0).getMsg(),
				 * Toast.LENGTH_SHORT).show(); } else {
				 * Toast.makeText(getApplicationContext(),
				 * ""+mod.getContactGroupRegistration().get(0).getError(),
				 * Toast.LENGTH_SHORT).show(); }
				 * 
				 * 
				 * dbObject.Open(); dbObject.addGroupAll(UserId,
				 * groupName,""+(countNUm-1));
				 * 
				 * dbObject.deleteselectedReceipent();
				 * dbObject.DeleteSELETEDTemplateData(); dbObject.close();
				 * 
				 * Intent i=new Intent(AddToGroup.this,GroupNewAdd.class);
				 * startActivity(i); finish();
				 * 
				 * 
				 * }
				 * 
				 * @Override public void onError(String Error, int id) {
				 * Toast.makeText(getApplicationContext(), Error,
				 * Toast.LENGTH_SHORT).show();
				 * 
				 * } }); }
				 * 
				 * else {
				 * 
				 * //Log.w("TAG_____","welcome back to else sir/ma'am");
				 * 
				 * groupName = editTextGroupName.getText().toString().trim();
				 * //Log.w("TAG","GROUP/ELSE:"+groupName);
				 * 
				 * fetchMobileandUserId(); //Log.w("TAG","UserId:"+UserId);
				 * 
				 * try { NewContact.contactsGroupFlag.finish(); }
				 * catch(Exception e){}
				 * 
				 * dbObject.Open(); dbObject.addGroupAll(UserId, groupName,"0");
				 * 
				 * dbObject.deleteselectedReceipent();
				 * dbObject.DeleteSELETEDTemplateData(); dbObject.close();
				 * 
				 * Intent i=new Intent(AddToGroup.this,GroupNewAdd.class);
				 * //Contacts.switchtoTab(1); //i.putExtra("refreshgp",
				 * "sucessfuyllyadd"); startActivity(i); finish();
				 * 
				 * } } catch(Exception e) { try {
				 * NewContact.contactsGroupFlag.finish(); } catch(Exception
				 * ee){}
				 * 
				 * NewContact.checkAll= false;
				 * 
				 * Intent i=new Intent(AddToGroup.this,GroupNewAdd.class);
				 * //Contacts.switchtoTab(1); //i.putExtra("refreshgp",
				 * "sucessfuyllyadd"); startActivity(i); finish();
				 * 
				 * } } else { Toast.makeText(getApplicationContext(),
				 * "Please enter group name", Toast.LENGTH_SHORT).show(); }
				 * //Log.w("TAG","GROUP/UNDER/2:"+groupName); } catch(Exception
				 * der) { Toast.makeText(getApplicationContext(),
				 * "Please enter group name", Toast.LENGTH_SHORT).show();
				 * 
				 * }
				 */}
		});

	}

	public void fetchMobileandUserId() {
		dbObject.Open();

		Cursor c;

		c = dbObject.getLoginDetails();

		while (c.moveToNext()) {

			Mobile = c.getString(1);
			UserId = c.getString(3);
			Password = c.getString(5);

		}

		dbObject.close();
	}

	public String[] getAllContactsTOaddIntoGroup() {
		dbObject.Open();
		Cursor c;

		c = dbObject.getSelectedGroupContacts();

		int count = c.getCount();

		String data[] = new String[count];

		if (count >= 1) {
			int i = 0;
			while (c.moveToNext()) {
				/*
				 * contact person name =c.getString(0);
				 * contactNumber=c.getString(1);
				 */

				data[i] = c.getString(0) + "," + c.getString(1);
				i++;
			}

		}

		dbObject.close();

		return data;

	}

	public void FetchContact() {
		fetchMobileandUserId();

		new webservice(null, webservice.GetAllGroupDetails.geturl(UserId),
				webservice.TYPE_GET, webservice.TYPE_GET_GROUP_DETAILS,
				new ServiceHitListener() {

					@Override
					public void onSuccess(Object Result, int id) {

						// **********************************GROUP
						// SEARCH***************************************//*

						FetchGroupDetails Model = (FetchGroupDetails) Result;
						// List<GroupDetails> list = Model.getGroupDetails();

						try {
							String EmergencyMessage = Model.getGroupDetails()
									.get(0).getEmergencyMessage();
							try {
								Emergency.desAct.finish();
							}

							catch (Exception e) {
							}

							if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
								Intent rt = new Intent(AddToGroup.this,
										Emergency.class);
								rt.putExtra("Emergency", EmergencyMessage);
								startActivity(rt);

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						dbObject.Open();
						dbObject.DeleteGroupDataALL();
						dbObject.close();

						for (int i = 0; i < Model.getGroupDetails().size(); i++) {
							fetchMobileandUserId();
							String gpname = Model.getGroupDetails().get(i)
									.getGroup_name();
							String gpCount = Model.getGroupDetails().get(i)
									.getNoOfContacts();

							dbObject.Open();
							dbObject.addGroupAll(UserId, gpname, gpCount);
							dbObject.close();
						}
					}

					@Override
					public void onError(String Error, int id) {

						Toast.makeText(getApplicationContext(), Error,
								Toast.LENGTH_SHORT).show();

					}
				});
	}

	public String removeBackComma(String value) {

		return value.substring(0, value.length() - 1);

	}

	public static int countNumber(String str) {
		if (str == null || str.length() <= 0 || str.isEmpty())
			return 0;

		int count = 0;
		for (int e = 0; e < str.length(); e++) {
			if (str.charAt(e) == ',') {
				count++;

			}
		}
		return count;
	}

	public String getIndividuleDetails() {
		dbObject.Open();
		Cursor c;

		c = dbObject.getContactALL();

		int count = c.getCount();

		String icontact = "";

		if (c.getCount() >= 1) {

			int i = 0;
			while (c.moveToNext() && i < count) {
				icontact += c.getString(3) + ",";
				i++;
			}
		}

		dbObject.close();

		return icontact;
	}

	public String getIndividuleDetailsName() {
		dbObject.Open();
		Cursor c;

		c = dbObject.getContactALL();

		int count = c.getCount();

		String icontact = "";

		if (c.getCount() >= 1) {

			int i = 0;
			while (c.moveToNext() && i < count) {
				icontact += c.getString(2) + ",";
				i++;
			}
		}

		dbObject.close();

		return icontact;
	}

	/*
	 * @Override public void onBackPressed() { try { NewContact.checkAll=false;
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * Intent fr = new Intent(AddToGroup.this,NewContact.class);
	 * startActivity(fr); finish(); }
	 */

}
