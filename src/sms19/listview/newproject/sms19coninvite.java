package sms19.listview.newproject;

import java.util.ArrayList;
import java.util.List;

import sms19.listview.adapter.custom_Refer_friend;
import sms19.listview.adapter.invitecustomlistapp;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.ReferFriendModel;
import sms19.listview.newproject.model.ReferListModelDB;
import sms19.listview.newproject.model.contactinvitemodel;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.kitever.android.R;

public class sms19coninvite extends ActionBarActivity implements
		OnClickListener {
	List<contactinvitemodel> adq;
	public static CheckBox selectoinvite;
	ListView inflatecontacts;
	Button simcontact, appcontacts, inviteall, inviteselected, invitedpeople;
	String namedb = "", num = "";
	int ci = 0;
	String referusername, referedusernumber, statusrefer;
	String Password = "", UserLogin = "", Mobile = "", UserId = "";
	custom_Refer_friend adr;
	private List<ReferListModelDB> DATA;
	DataBaseDetails db = new DataBaseDetails(this);
	invitecustomlistapp AppContactAdapter;
	String refername = "", refernumbe = "", referStatusDBList = "",
			referDateDBList = "", referedusernumbe = "", refernumber = "";
	Context context;

	public static boolean invitallvara = false;
	boolean cntFlag = true, sms19ContactFlag = true, simContactFlag = true;
	ProgressDialog p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newinitationlayout);

		simcontact = (Button) findViewById(R.id.contactbook1);
		appcontacts = (Button) findViewById(R.id.sms191);
		selectoinvite = (CheckBox) findViewById(R.id.checkBox11);
		inviteall = (Button) findViewById(R.id.inviteall1);
		inviteselected = (Button) findViewById(R.id.button61);
		invitedpeople = (Button) findViewById(R.id.invitedpeople1);
		inflatecontacts = (ListView) findViewById(R.id.listView11);
		context = this;
		invitallvara = false;

		// change action bar name and color
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
		bar.setTitle(Html
				.fromHtml("<font color='#ffffff'>Invite Friends</font>"));

		simcontact.setOnClickListener(this);

		selectoinvite.setOnClickListener(this);
		inviteall.setOnClickListener(this);
		inviteselected.setOnClickListener(this);
		invitedpeople.setOnClickListener(this);
		db.Open();
		db.deletereferedpeopleall();
		db.close();
		callappcontactlist();

		// select all checkbox #############################################
		selectoinvite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				invitallvara = selectoinvite.isChecked();

				// call adapter from database
				callappcontactlist();
			}
		});
		// select all checkbox #############################################

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.contactbook1: {
			Intent i = new Intent(sms19coninvite.this, Friendsinvite.class);
			startActivity(i);
			finish();
		}
			break;
		case R.id.invitedpeople1:

			refername = "";
			inviteall.setClickable(false);
			inviteselected.setClickable(false);
			selectoinvite.setVisibility(View.INVISIBLE);
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
		case R.id.inviteall1: {
			if (!selectoinvite.isChecked()) {
				Toast.makeText(context, "Please select all before inviting",
						Toast.LENGTH_SHORT).show();
			} else {
				String rname = "", rnum = "";
				rnum = fetchselectedreferedpeoplenumber();

				if (rnum.length() > 0) {
					fetchUserId();
					rname = fetchselectedreferedpeople();
					try {
						rname = rname.substring(0, rname.length() - 1);
						rnum = rnum.substring(0, rnum.length() - 1);
					} catch (Exception e1) {
					}

					p = ProgressDialog.show(context, null, "please wait...");
					new webservice(null, webservice.ReferFriend.geturl(
							UserLogin, Password, UserId, "SMSSMS", rnum,
							Mobile, rname), webservice.TYPE_GET,
							webservice.TYPE_REFER_FRIEND,
							new ServiceHitListener() {

								@Override
								public void onSuccess(Object Result, int id) {
									try {
										p.dismiss();
									} catch (Exception e) {

									}
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
									try {
										p.dismiss();
									} catch (Exception e) {
										// TODO: handle exception
									}
								}
							});
				}
			}
		}
			break;
		case R.id.button61: {
			String rname = "", rnum = "";

			// Log.w("rnum.length()","rnum.length()"+rnum.length());

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

				p = ProgressDialog.show(context, null, "please wait...");
				new webservice(null, webservice.ReferFriend.geturl(UserLogin,
						Password, UserId, "SMSSMS", rnum, Mobile, rname),
						webservice.TYPE_GET, webservice.TYPE_REFER_FRIEND,
						new ServiceHitListener() {

							@Override
							public void onSuccess(Object Result, int id) {
								try {
									p.dismiss();
								} catch (Exception e) {
									// TODO: handle exception
								}
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
								try {
									p.dismiss();
								} catch (Exception e) {
									// TODO: handle exception
								}
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

	public void callappcontactlist() {
		adq = new ArrayList<contactinvitemodel>();
		db.Open();
		Cursor c;
		c = db.getContactALL();
		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				namedb = c.getString(2);
				num = c.getString(3);
				adq.add(new contactinvitemodel(namedb, num));
			}
		}
		db.close();
		inflatecontacts.setAdapter(AppContactAdapter = new invitecustomlistapp(
				context, R.layout.invitecustomlist, adq));

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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		db.Open();
		db.deletereferedpeopleall();
		db.close();
	}
}
