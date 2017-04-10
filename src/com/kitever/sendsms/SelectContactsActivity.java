package com.kitever.sendsms;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.contacts.AdapterInterface;
import com.kitever.contacts.GroupsContactsSingleton;
import com.kitever.models.ContactInfo;
import com.kitever.models.ContactList;
import com.kitever.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sms19.inapp.msg.constant.Apiurls;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

@SuppressWarnings("deprecation")
public class SelectContactsActivity extends ActionBarActivity implements
		AdapterInterface {

	private ProgressDialog progressDialog = null;

	private String userId = null;

	private final String TAG = "SelectContactsActivity";

	private ListView mListView;

	private TextView textView, msgTextView;

	private static final int FROM_SEND_SMS_MODULE = 104;
	private static final int FROM_BROADCAST_NEW_GROUP_ADD_CONTACT_MODULE = 2;
	private static final int FROM_BROADCAST_EDIT_GROUP_ADD_CONTACT_MODULE = 3;

	private Menu menu;

	private SearchView searchView;

	private ContactsAdapter adapter = null;
	private  ActionBar bar;

	// private boolean isChangesMade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectcontacts);

		bar = getSupportActionBar();

		actionBarSettingWithBack(this,bar,"Select contacts");

		textView = (TextView) findViewById(R.id.textView);
		msgTextView = (TextView) findViewById(R.id.msgTextView);

		textView.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
		setRobotoThinFont(textView,this);

		mListView = (ListView) findViewById(R.id.listView);
		userId = getIntent().getStringExtra("userId");

		if (getIntent().getIntExtra("FROM", 10) == FROM_SEND_SMS_MODULE) {
			textView.setVisibility(View.GONE);
			getContactsForSendSMSModule();
		} else if (getIntent().getIntExtra("FROM", 10) == FROM_BROADCAST_NEW_GROUP_ADD_CONTACT_MODULE) {
			textView.setVisibility(View.VISIBLE);
			final String str = getIntent().getStringExtra("GROUP_NAME");
			textView.setText("Let's add some recipients to BROADCAST LIST:\n"
					+ str);
			getContactsForContactsModule();
		} else if (getIntent().getIntExtra("FROM", 10) == FROM_BROADCAST_EDIT_GROUP_ADD_CONTACT_MODULE) {
			textView.setText("Select contacts to add");
			textView.setVisibility(View.VISIBLE);
			getContactsForContactsModule();
		}
	}

	private void getContactsForSendSMSModule() {

		if (SendSmsContactsSingleton.getInstance() == null
				|| SendSmsContactsSingleton.getInstance().getContactInfos() == null
				|| SendSmsContactsSingleton.getInstance().getContactInfos()
						.size() <= 2) {
			initTask();
		} else {
			initAdapter();
		}

	}

	private void getContactsForContactsModule() {

		if (GroupsContactsSingleton.getInstance() == null
				|| GroupsContactsSingleton.getInstance().getContactInfos() == null
				|| GroupsContactsSingleton.getInstance().getContactInfos()
						.size() <= 2) {
			initTask();
		} else {
			initAdapter();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contacts_fragment_menu, menu);
		this.menu = menu;
		MenuItem action_doneMenuItem = this.menu.findItem(R.id.action_done);
		action_doneMenuItem.setEnabled(true);
		action_doneMenuItem.setTitle("DONE");

		MenuItem searchItem = this.menu.findItem(R.id.action_search);
		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String str) {
				searchContacts(str);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String str) {
				searchContacts(str);
				return false;
			}
		});

		return true;
	}

	private void searchContacts(final String constraint) {
		if (adapter != null) {
			if (adapter.getFilter() != null) {
				adapter.getFilter().filter(constraint);
			}
		}
	}

	/*
	 * private void updateMenuTitles() {
	 * 
	 * if (this.menu != null) {
	 * 
	 * MenuItem action_doneMenuItem = this.menu.findItem(R.id.action_done); if
	 * (isChangesMade) { action_doneMenuItem.setEnabled(true);
	 * //action_doneMenuItem.setVisible(true);
	 * action_doneMenuItem.setTitle("DONE"); } else {
	 * action_doneMenuItem.setEnabled(false);
	 * //action_doneMenuItem.setVisible(false);
	 * action_doneMenuItem.setTitle("DONE"); }
	 * 
	 * }
	 * 
	 * }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == android.R.id.home) {
			finish();
			return true;
		}

		if (id == R.id.action_done) {

			Intent returnIntent = new Intent();

			setResult(RESULT_OK, returnIntent);
			finish();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/*
	 * @Override public void finish() {
	 * 
	 * Intent returnIntent = new Intent(); setResult(RESULT_OK, returnIntent);
	 * 
	 * super.finish(); }
	 */

	private void initTask() {

		final ContactAsyncTask asyncTask = new ContactAsyncTask();
		asyncTask.execute(userId);
	}

	private void initAdapter() {

		ArrayList<ContactInfo> contactInfos = null;

		if (getIntent().getIntExtra("FROM", 10) == FROM_SEND_SMS_MODULE) {

			adapter = new ContactsAdapter(SelectContactsActivity.this,
					contactInfos = SendSmsContactsSingleton.getInstance()
							.getContactInfos(), SelectContactsActivity.this);

		} else if (getIntent().getIntExtra("FROM", 10) == FROM_BROADCAST_NEW_GROUP_ADD_CONTACT_MODULE) {

			adapter = new ContactsAdapter(SelectContactsActivity.this,
					contactInfos = GroupsContactsSingleton.getInstance()
							.getContactInfos(), SelectContactsActivity.this);

		} else if (getIntent().getIntExtra("FROM", 10) == FROM_BROADCAST_EDIT_GROUP_ADD_CONTACT_MODULE) {

			adapter = new ContactsAdapter(SelectContactsActivity.this,
					contactInfos = GroupsContactsSingleton.getInstance()
							.getContactInfos(), SelectContactsActivity.this);
		}

		mListView.setAdapter(adapter);

		if (contactInfos != null) {
			if (contactInfos.size() == 0) {
				displayMsg();
			} else {
				onItemsAvailable();
			}
		} else {
			displayMsg();
		}

	}

	private void setContactInfos(final ArrayList<ContactInfo> contactInfos) {
		if (getIntent().getIntExtra("FROM", 10) == FROM_SEND_SMS_MODULE) {
			SendSmsContactsSingleton.getInstance()
					.setContactInfos(contactInfos);
		} else if (getIntent().getIntExtra("FROM", 10) == FROM_BROADCAST_NEW_GROUP_ADD_CONTACT_MODULE) {
			GroupsContactsSingleton.getInstance().setContactInfos(contactInfos);
		} else if (getIntent().getIntExtra("FROM", 10) == FROM_BROADCAST_EDIT_GROUP_ADD_CONTACT_MODULE) {
			GroupsContactsSingleton.getInstance().setContactInfos(contactInfos);
		}

	}

	private void displayMsg() {
		msgTextView.setText("No contacts to display");
		msgTextView.setVisibility(View.VISIBLE);
	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(SelectContactsActivity.this);
			progressDialog.setTitle("Loading contacts");
			progressDialog.setMessage("Please wait...");
			progressDialog.show();
		} else {
			if (!progressDialog.isShowing()) {
				progressDialog.show();
			}
		}
	}

	private void removeProgressDialog() {
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

	class ContactAsyncTask extends AsyncTask<String, Void, Void> {

		public ContactAsyncTask() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}

		@Override
		protected Void doInBackground(String... params) {
			boolean isOnline = sms19.inapp.msg.constant.Utils
					.isDeviceOnline(SelectContactsActivity.this);
			HttpClient httpclient = new DefaultHttpClient();

			final String stringUrl = Apiurls.getBasePostURL();

			HttpPost httppost = new HttpPost(stringUrl);

			try {
				// Add your data
				String result = "";
				SharedPreferences prfs = getSharedPreferences("ContactsData",
						Context.MODE_PRIVATE);
				Editor editor = prfs.edit();

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("Page",
						"GetContactForSendSMS"));
				nameValuePairs.add(new BasicNameValuePair("Userid", params[0]));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				try {
					HttpResponse response = httpclient.execute(httppost);
					result = EntityUtils.toString(response.getEntity());
				} catch (Exception e) {
					// TODO: handle exception

					if (!isOnline && result != null && result.length() <= 0) {
						result = prfs.getString("ContactsData", "");
					}
				}

				if (result != null && result.length() > 0) {
					editor.putString("ContactsData", result);
					editor.commit();

					// CONVERT RESPONSE STRING TO JSON ARRAY
					final ArrayList<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
					Gson gson = new Gson();
					ContactList contact = gson.fromJson(result,
							ContactList.class);
					setContactInfos(contact.getContactDetails());
				}
			}

			catch (IOException e) {
				// TODO Auto-generated catch block
				// Log.e(TAG, e.toString());
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			initAdapter();
			removeProgressDialog();

		}
	}

	@Override
	public void onItemClicked(int position, int forWhat) {
		// TODO Auto-generated method stub
		if(SendSmsContactsSingleton.getInstance()!=null)
		bar.setSubtitle("selected "+adapter.getCheckedSize());
	}

	@Override
	public void onItemsEmpty() {
		// TODO Auto-generated method stub
		msgTextView.setText("No contact result to display");
		msgTextView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onItemsAvailable() {
		// TODO Auto-generated method stub
		msgTextView.setText("Loading contacts...");
		msgTextView.setVisibility(View.GONE);
	}

	/*
	 * @Override public void onBackPressed() { if (isChangesMade) { new
	 * AlertDialog.Builder(this) .setCancelable(false)
	 * .setMessage("Save changes?") .setPositiveButton("Save", new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) { Intent
	 * returnIntent = new Intent(); setResult(RESULT_OK, returnIntent);
	 * finish(); }
	 * 
	 * }) .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * dialog.cancel(); finish(); } }).show(); }else{ finish(); }
	 * 
	 * }
	 */

}
