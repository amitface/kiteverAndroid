package sms19.listview.newproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import sms19.listview.adapter.customAdaptorContactDataBASE;
import sms19.listview.adapter.custom_Refer_friend;
import sms19.listview.adapter.invitecustomlist;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.ReferFriendModel;
import sms19.listview.newproject.model.ReferListModelDB;
import sms19.listview.newproject.model.contactinvitemodel;
import sms19.listview.newproject.model.contactmodelIndividule;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import com.kitever.android.R;

public class Friendsinvite extends ActionBarActivity implements OnClickListener {

	public static CheckBox selectoinvite;
	ListView inflatecontacts;
	Button simcontact, appcontacts, inviteall, inviteselected, invitedpeople;
	String namedb = "", num = "";
	int ci = 0;
	String Password = "", UserLogin = "", Mobile = "", UserId = "";
	public static boolean invitallvar = false;
	DataBaseDetails db = new DataBaseDetails(this);
	List<contactinvitemodel> ad2;
	String refername = "", refernumber = "", referStatusDBList = "",
			referDateDBList = "";
	boolean cntFlag = true, sms19ContactFlag = true, simContactFlag = true;
	invitecustomlist SimContactAdapter;
	Context context;
	custom_Refer_friend adr;
	private List<ReferListModelDB> DATA;
	String referusername = "", referedusernumbe = "", statusrefer = "";
	ProgressDialog p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitefriends);

		simcontact = (Button) findViewById(R.id.contactbook);
		appcontacts = (Button) findViewById(R.id.sms19);
		selectoinvite = (CheckBox) findViewById(R.id.checkBox1Friends);
		invitedpeople = (Button) findViewById(R.id.invitedpeople);
		inviteall = (Button) findViewById(R.id.inviteall);
		inviteselected = (Button) findViewById(R.id.button6);
		inflatecontacts = (ListView) findViewById(R.id.listView1);

		invitallvar = false;
		context = this;

		// change action bar name and color
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
		bar.setTitle(Html
				.fromHtml("<font color='#ffffff'>Invite Friends</font>"));

		db.Open();
		db.deletereferedpeopleall();
		db.close();

		simcontact.setOnClickListener(this);
		appcontacts.setOnClickListener(this);
		selectoinvite.setOnClickListener(this);
		inviteall.setOnClickListener(this);
		inviteselected.setOnClickListener(this);
		invitedpeople.setOnClickListener(this);

		// show sim contact ###################
		getDetails();
		callFxnForSimContact();
		// show sim contact ###################

		// select all checkbox #############################################
		selectoinvite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				invitallvar = selectoinvite.isChecked();

				// Log.e("TAG",":::"+invitallvar);
				// call adapter from database
				callFxnForSimContact();
			}
		});
		// select all checkbox #############################################

	}

	private void getDetails() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };
		Cursor names = getContentResolver().query(uri, projection, null, null,
				null);
		int indexName = names
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		int indexNumber = names
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

		if (names.getCount() > 0) {
			db.Open();
			while (names.moveToNext()) {

				String name = names.getString(indexName);
				String number = names.getString(indexNumber)
						.replaceAll("\\s+", "").trim();
				// Log.e("name and number","name and number"+name+"number"+number);

				try {

					number = number.replaceAll("-", "");
					number = number.replaceAll("\\(", "");
					number = number.replaceAll("\\)", "");

					// Log.e("SimCon:  After replace",""+number);
				} catch (Exception e) {
					e.printStackTrace();
					number = names.getString(indexNumber)
							.replaceAll("\\s+", "").trim();
				}

				if (number.length() > 10) {
					try {
						number = number.substring(number.length() - 10);
					} catch (Exception e) {
					}
				}

				if (checkduplicate(number)) {

				} else {

					db.addfromphonebook("1111", name, number);
				}
			}
			db.close();
			cur.close();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sms19: {
			Intent i = new Intent(Friendsinvite.this, sms19coninvite.class);
			startActivity(i);
			finish();
		}
			break;
		case R.id.invitedpeople:
			inviteall.setClickable(false);
			inviteselected.setClickable(false);
			selectoinvite.setVisibility(View.INVISIBLE);
			refername = "";

			db.Open();
			Cursor c;
			c = db.getreferedfriend();
			if (c.getCount() > 0) {
				while (c.moveToNext()) {
					refername = c.getString(2);
					referusername = c.getString(3);
					referStatusDBList = c.getString(1);
					referDateDBList = c.getString(4);

					DATA.add(new ReferListModelDB(refername, referusername,
							referStatusDBList, referDateDBList));
				}
			}
			db.close();
			inflatecontacts.setAdapter(new custom_Refer_friend(context,
					R.layout.custom_refer_list, DATA));

			break;
		case R.id.contactbook: {

		}
			break;
		case R.id.inviteall: {
			if (!selectoinvite.isChecked()) {
				Toast.makeText(context, "Please select all before inviting",
						Toast.LENGTH_SHORT).show();
			} else {
				String rnum = fetchselectedreferedpeoplenumber();
				if (rnum.length() > 0) {
					fetchUserId();

					String rname = fetchselectedreferedpeople();
					try {
						rname = rname.substring(0, rname.length() - 1);
						rnum = rnum.substring(0, rnum.length() - 1);
					} catch (Exception e1) {
					}
					p = ProgressDialog
							.show(context, null, "please wait.......");
					new webservice(null, webservice.ReferFriend.geturl(
							UserLogin, Password, UserId, "SMSSMS", rnum,
							Mobile, rname), webservice.TYPE_GET,
							webservice.TYPE_REFER_FRIEND,
							new ServiceHitListener() {

								@Override
								public void onSuccess(Object Result, int id) {
									p.dismiss();
									ReferFriendModel model = (ReferFriendModel) Result;

									if (model.getReferFriend().size() > 0) {
										String reftype = "";
										String Time = "";

										for (int i = 0; i < model
												.getReferFriend().size(); i++) {

											reftype = model.getReferFriend()
													.get(i).getReferredType();

											try {
												if (reftype
														.equalsIgnoreCase("1")) {
													fetchUserId();

													statusrefer = model
															.getReferFriend()
															.get(i)
															.getReferredType();
													referedusernumbe = model
															.getReferFriend()
															.get(i)
															.getRecipientPhoneNo();
													referusername = model
															.getReferFriend()
															.get(i)
															.getRecipientName();

													db.Open();
													db.deletereferedpeopleall();
													db.addintoreferdb(UserId,
															referusername,
															referedusernumbe,
															statusrefer, Time);
													db.close();
												}
											} catch (Exception e) {
											}

										}
									}

								}

								@Override
								public void onError(String Error, int id) {
									p.dismiss();
									// invitallvar=false;
								}
							});
				}
			}

		}
			break;
		case R.id.button6: {
			String rname = "", rnum = "";

			rnum = fetchselectedreferedpeoplenumber();
			if (rnum.length() > 0) {
				// Log.e("rnum","rnum"+rnum.length());
				fetchUserId();
				rname = fetchselectedreferedpeople();

				try {
					rname = rname.substring(0, rname.length() - 1);
					rnum = rnum.substring(0, rnum.length() - 1);
				} catch (Exception e1) {
				}
				p = ProgressDialog.show(context, null, "please wait.......");
				new webservice(null, webservice.ReferFriend.geturl(UserLogin,
						Password, UserId, "SMSSMS", rnum, Mobile, rname),
						webservice.TYPE_GET, webservice.TYPE_REFER_FRIEND,
						new ServiceHitListener() {

							@Override
							public void onSuccess(Object Result, int id) {
								p.dismiss();
								ReferFriendModel model = (ReferFriendModel) Result;

								if (model.getReferFriend().size() > 0) {
									String reftype = "";
									String Time = "";

									for (int i = 0; i < model.getReferFriend()
											.size(); i++) {

										reftype = model.getReferFriend().get(i)
												.getReferredType();

										try {
											if (reftype.equalsIgnoreCase("1")) {
												fetchUserId();

												statusrefer = model
														.getReferFriend()
														.get(i)
														.getReferredType();
												referedusernumbe = model
														.getReferFriend()
														.get(i)
														.getRecipientPhoneNo();
												referusername = model
														.getReferFriend()
														.get(i)
														.getRecipientName();

												db.Open();
												db.deletereferedpeopleall();
												db.addintoreferdb(UserId,
														referusername,
														referedusernumbe,
														statusrefer, Time);
												db.close();
											}
										} catch (Exception e) {
										}

									}
								}

							}

							@Override
							public void onError(String Error, int id) {
								p.dismiss();
								// invitecustomlist.individualselectionphonecon=false;
							}
						});
			} else {
				Toast.makeText(context,
						"Please select atleast one contact before invitation",
						Toast.LENGTH_SHORT).show();
			}
		}
			break;
		}
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		db.Open();
		db.deletereferedpeopleall();
		db.close();
	}

	public void callFxnForSimContact() {

		String[] simNameValue = fetchSimName();
		String[] simNumValue = fetchSimNum();

		ArrayList<String> contactlistName = new ArrayList<String>(
				Arrays.asList(simNameValue));
		ArrayList<String> contactlistNum = new ArrayList<String>(
				Arrays.asList(simNumValue));

		ad2 = new ArrayList<contactinvitemodel>();

		Iterator<String> itreNum = contactlistNum.iterator();
		Iterator<String> itreNam = contactlistName.iterator();

		String sName = "", sNum;

		while (itreNum.hasNext()) {
			sName = itreNam.next();
			sNum = itreNum.next();
			ad2.add(new contactinvitemodel(sName, sNum));
		}

		inflatecontacts.setAdapter(SimContactAdapter = new invitecustomlist(
				context, R.layout.invitecustomlist, ad2));

	}

	public String[] fetchSimName() {

		db.Open();
		Cursor c1;
		c1 = db.getinvitaioncontacts();

		int counting = c1.getCount();

		String simName[] = new String[counting];

		int i = 0;
		while (c1.moveToNext()) {
			simName[i] = c1.getString(1);
			i++;
		}

		db.close();
		return simName;

	}

	public String[] fetchSimNum() {

		db.Open();
		Cursor c1;
		c1 = db.getinvitaioncontacts();

		int counting = c1.getCount();

		String simNum[] = new String[counting];

		int i = 0;
		while (c1.moveToNext()) {
			simNum[i] = c1.getString(2);
			i++;
		}

		db.close();
		return simNum;

	}

	public boolean checkduplicate(String number) {
		Cursor c = db.getinvitaioncontacts();
		String num = "";
		while (c.moveToNext()) {
			num = c.getString(2).trim();
			// Log.e("check number","c.getString(2).equals(number)"+num+"number"+number);
			if (num.equals(number.trim())) {
				// Log.e("check number","c.getString(2).equals(number)"+c.getString(2).equals(number));
				return true;
			}
		}
		return false;
	}

	public void fetchUserId() {
		db.Open();

		Cursor c;

		c = db.getLoginDetails();

		while (c.moveToNext()) {
			Password = c.getString(5);
			UserLogin = c.getString(6);
			Mobile = c.getString(1);
			UserId = c.getString(3);
		}

		db.close();

	}

	public String fetchselectedreferedpeople() {
		refername = "";

		db.Open();
		Cursor c;
		c = db.getreferedcontacts();
		int count = c.getCount();

		int i = 0;
		if (count > 0) {
			while (c.moveToNext()) {
				refername += c.getString(0) + ",";
				i++;
			}
		}

		return refername;
	}

	public String fetchselectedreferedpeoplenumber() {
		refernumber = "";
		db.Open();
		Cursor c;
		c = db.getreferedcontacts();
		int count = c.getCount();

		int i = 0;
		if (count > 0) {
			while (c.moveToNext()) {
				refernumber += c.getString(1) + ",";
				i++;
			}
		}
		return refernumber;
	}
}
