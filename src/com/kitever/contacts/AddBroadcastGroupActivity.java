package com.kitever.contacts;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.kitever.sendsms.SelectContactsActivity;
import com.kitever.sendsms.SendSMSContactsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.CreateBroadCast;
import sms19.inapp.msg.constant.Apiurls;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class AddBroadcastGroupActivity extends AppCompatActivity {

    private final String TAG = "AddBroadcastGroupActivity";

    private Button button;

    private EditText editText;

    private ProgressDialog progressDialog = null;

    private static final int SELECT_CONTACTS_REQUEST_CODE = 401;
    private static final int DELETE_CONTACTS_REQUEST_CODE = 402;

    private static final int FROM_BROADCAST_NEW_GROUP_ADD_CONTACT_MODULE = 2;
    private static final int FROM_BROADCAST_EDIT_GROUP_ADD_CONTACT_MODULE = 3;

    private ListView mListView;

    private ArrayList<ContactInfo> selectedContactsList = null;
    private TextView noteTextView, noContactsTextView,manualLabelTextView;

    private SendSMSContactsAdapter adapter = null;

    private LinearLayout recipientsLinearLayout;
    private ImageView addImageView, deleteImageView;
    private String groupId;
    private MenuItem searchViewCollapse;

    private JSONObject jsonObject = null;
    private Menu menu;
    private SearchView searchView;
    // ArrayList<String> numberJid = "";
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_addbroadcastgroup);



        editText = (EditText) findViewById(R.id.editText);

        button = (Button) findViewById(R.id.button);

        noteTextView = (TextView) findViewById(R.id.noteTextView);
        manualLabelTextView= (TextView) findViewById(R.id.manualLabelTextView);
        noContactsTextView = (TextView) findViewById(R.id.noContactsTextView);
        recipientsLinearLayout = (LinearLayout) findViewById(R.id.recipientsLinearLayout);
        addImageView = (ImageView) findViewById(R.id.addImageView);
        deleteImageView = (ImageView) findViewById(R.id.deleteImageView);

        manualLabelTextView.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        editText.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        button.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        button.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        setRobotoThinFont(editText,this);
        setRobotoThinFont(manualLabelTextView,this);
        setRobotoThinFontButton(button,this);

        mListView = (ListView) findViewById(R.id.listView);

        if (getIntent().getStringExtra("FOR").equals("ADD")) {

            //bar.setTitle(Utils.setActionBarTextAndColor("Create Group"));
            actionBarSettingWithBack(AddBroadcastGroupActivity.this,getSupportActionBar(),"Create Group");
            noteTextView.setVisibility(View.VISIBLE);
            addImageView.setVisibility(View.GONE);
            deleteImageView.setVisibility(View.GONE);
            recipientsLinearLayout.setVisibility(View.GONE);

        } else if (getIntent().getStringExtra("FOR").equals("EDIT")) {

           // bar.setTitle(Utils.setActionBarTextAndColor("Update Group"));
            actionBarSettingWithBack(AddBroadcastGroupActivity.this,getSupportActionBar(),"Update Group");

            noteTextView.setVisibility(View.GONE);

            addImageView.setVisibility(View.VISIBLE);
            deleteImageView.setVisibility(View.VISIBLE);

            recipientsLinearLayout.setVisibility(View.VISIBLE);
            // button.setText("UPDATE BROADCAST LIST GROUP");
            button.setVisibility(View.INVISIBLE);
            final String group_name = getIntent().getStringExtra("group_name");
            groupId = getIntent().getStringExtra("groupId");
            editText.setText(group_name);
            editText.setEnabled(true);
            getGroupContacts(group_name);

            addImageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(AddBroadcastGroupActivity.this,
                            SelectContactsActivity.class);
                    intent.putExtra("FROM",
                            FROM_BROADCAST_EDIT_GROUP_ADD_CONTACT_MODULE);
                    final String userId = getIntent().getStringExtra("userId");
                    intent.putExtra("userId", userId);
                    intent.putExtra("GROUP_NAME", editText.getText().toString()
                            .trim());
                    startActivityForResult(intent, SELECT_CONTACTS_REQUEST_CODE);

                }
            });

            deleteImageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(AddBroadcastGroupActivity.this,
                            DeleteGroupContactsActivity.class);
                    final String userId = getIntent().getStringExtra("userId");
                    intent.putExtra("userId", userId);
                    intent.putExtra("GROUP_NAME", editText.getText().toString()
                            .trim());
                    intent.putExtra("GROUP_CONTACTS_TO_DELETE",
                            selectedContactsList);
                    intent.putExtra("groupId", groupId);
                    startActivityForResult(intent, DELETE_CONTACTS_REQUEST_CODE);

                }
            });

        } else if (getIntent().getStringExtra("FOR").equals("VIEW")) {

            //bar.setTitle(Utils.setActionBarTextAndColor("Group Details"));
            actionBarSettingWithBack(AddBroadcastGroupActivity.this,getSupportActionBar(),"Group Details");

            noteTextView.setVisibility(View.GONE);

            addImageView.setVisibility(View.GONE);
            deleteImageView.setVisibility(View.GONE);

            button.setVisibility(View.INVISIBLE);
            recipientsLinearLayout.setVisibility(View.VISIBLE);
            final String group_name = getIntent().getStringExtra("group_name");
            editText.setText(group_name);
            editText.setEnabled(false);
            getGroupContacts(group_name);

        }

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (getIntent().getStringExtra("FOR").equals("EDIT")) {
                    Toast.makeText(getApplicationContext(),
                            "Not Implemented Yet", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String str = editText.getText().toString().trim();
                editText.setText(str);
                editText.setEnabled(false);
                if (!str.equals("")) {
                    addNewGroupRequest(str);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter broadcast group name",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contacts_fragment_menu, menu);
        this.menu = menu;

        searchViewCollapse = this.menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchViewCollapse);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        menu.getItem(1).setVisible(false);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String str) {

//				if (currentSelectedIndex == 0) {
//					searchContacts(str);
//				} else if (currentSelectedIndex == 1) {
//					searchGroups(str);
//				}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String str) {

//				if (currentSelectedIndex == 0) {
//					searchContacts(str);
//				} else if (currentSelectedIndex == 1) {
//					searchGroups(str);
//				}
                if (getSupportFragmentManager().findFragmentByTag("CreateBroadCast") != null && getSupportFragmentManager().findFragmentByTag("CreateBroadCast").isVisible())
                {
                    CreateBroadCast broadcastFragment = (CreateBroadCast) getSupportFragmentManager().findFragmentByTag("CreateBroadCast");
                    if (broadcastFragment != null) {
                        broadcastFragment.filter(str);
                    }
                }
                return false;
            }
        });
        if (getSupportFragmentManager().findFragmentByTag("CreateBroadCast") != null && getSupportFragmentManager().findFragmentByTag("CreateBroadCast").isVisible())
            return true;
        else if (getSupportFragmentManager().findFragmentByTag("CreateBroadCast2") != null && getSupportFragmentManager().findFragmentByTag("CreateBroadCast2").isVisible())
        {
            menu.getItem(0).setVisible(false);
            return false;
        }
        else
        {
            return false;
        }

    }

    public void collapseSearch() {
        if (searchViewCollapse != null) {
            searchViewCollapse.collapseActionView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (getSupportFragmentManager().findFragmentByTag(
                    "CreateBroadCast2") != null
                    && getSupportFragmentManager().findFragmentByTag(
                    "CreateBroadCast2").isResumed()) {
                // CreateBroadCast2 fre = ((CreateBroadCast2)
                // getSupportFragmentManager()
                // .findFragmentByTag("CreateBroadCast2"));
                // getSupportFragmentManager().beginTransaction().remove(fre);
                onBackPressed();
                // Toast.makeText(this, "else if", Toast.LENGTH_SHORT).show();
            } else if (getSupportFragmentManager().findFragmentByTag(
                    "CreateBroadCast") != null
                    && getSupportFragmentManager().findFragmentByTag(
                    "CreateBroadCast").isResumed()) {
                // Toast.makeText(this, "if", Toast.LENGTH_SHORT).show();
                // CreateBroadCast fre = ((CreateBroadCast)
                // getSupportFragmentManager()
                // .findFragmentByTag("CreateBroadCast"));
                onBackPressed();
                // getSupportFragmentManager().beginTransaction().remove(fre);
            } else
                finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

	/*
     * @Override public void finish() {
	 * 
	 * new Thread(new Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub if
	 * (selectedContactsList != null) { for (ContactInfo info :
	 * selectedContactsList) { info.isSelected = false; } } } }).start();
	 * 
	 * super.finish(); }
	 */

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(AddBroadcastGroupActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_CONTACTS_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                setSelectedContacts();
            }
        }

        if (requestCode == DELETE_CONTACTS_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                getGroupContacts(editText.getText().toString().trim());
            }
        }

    }

    private void setSelectedContacts() {

        showProgressDialog();

        if (GroupsContactsSingleton.getInstance() == null) {

        } else {
            if (GroupsContactsSingleton.getInstance().getContactInfos() == null) {

            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (selectedContactsList != null) {
                            for (ContactInfo info : selectedContactsList) {
                                info.isSelected = false;
                                // if (count == 0) {
                                // numberJid = numberJid + info.countryCode
                                // + info.Contact_Mobile
                                // + "@email19.in";
                                // } else {
                                // numberJid = numberJid + ","
                                // + info.countryCode
                                // + info.Contact_Mobile
                                // + "@email19.in";
                                // }
                                // count++;
                            }
                            selectedContactsList.clear();
                        } else {
                            selectedContactsList = new ArrayList<ContactInfo>();
                        }

                        final ArrayList<ContactInfo> contactInfos = GroupsContactsSingleton
                                .getInstance().getContactInfos();
                        for (ContactInfo contactInfo : contactInfos) {
                            if (contactInfo.isSelected) {
                                selectedContactsList.add(contactInfo);
                            }
                        }

                        jsonObject = new JSONObject();

                        try {
                            JSONArray jsonArray = new JSONArray();

                            for (ContactInfo contactInfo : selectedContactsList) {
                                if (contactInfo.isSelected) {

                                    JSONObject jsonObject2 = new JSONObject();

                                    if (contactInfo.Contact_Name != null) {
                                        jsonObject2.put("contactName",
                                                contactInfo.Contact_Name);
                                    } else {
                                        jsonObject2.put("contactName", "");
                                    }

                                    if (contactInfo.countryCode != null) {
                                        jsonObject2.put("countryCode",
                                                contactInfo.countryCode);
                                    } else {
                                        jsonObject2.put("countryCode", "");
                                    }

                                    if (contactInfo.Contact_DOB != null) {
                                        jsonObject2.put("contactDOB",
                                                contactInfo.Contact_DOB);
                                    } else {
                                        jsonObject2.put("contactDOB", "");
                                    }
                                    if (contactInfo.Contact_Anniversary != null) {
                                        jsonObject2
                                                .put("contactAnniversary",
                                                        contactInfo.Contact_Anniversary);
                                    } else {
                                        jsonObject2.put("contactAnniversary",
                                                "");
                                    }

                                    if (contactInfo.Contact_Mobile != null) {
                                        jsonObject2.put("contactMobile",
                                                contactInfo.Contact_Mobile);
                                    } else {
                                        jsonObject2.put("contactMobile", "");
                                    }

                                    jsonArray.put(jsonObject2);
                                }
                            }

                            jsonObject.put("contacts", jsonArray);

                        } catch (Exception exp) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editText.setEnabled(true);
                                    removeProgressDialog();
                                }
                            });
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                insertGroupContactRequest(jsonObject.toString());
                            }
                        });

                    }
                }).start();

            }
        }

    }

    private void setAdapter() {

        if (selectedContactsList != null) {

            if (selectedContactsList.size() == 0) {
                noContactsTextView.setVisibility(View.VISIBLE);
            } else {
                noContactsTextView.setVisibility(View.GONE);
            }

			/*
			 * if(getIntent().getStringExtra("FOR").equals("EDIT")){
			 * recipientsLinearLayout.setVisibility(View.VISIBLE); }else{
			 * 
			 * }
			 */

            adapter = new SendSMSContactsAdapter(
                    AddBroadcastGroupActivity.this, selectedContactsList);
            mListView.setAdapter(adapter);
        }
    }

    private void insertGroupContactRequest(final String contactsStr) {
        final ProgressDialog dialog = new ProgressDialog(
                AddBroadcastGroupActivity.this);
        dialog.setTitle("Please wait");
        dialog.setMessage("Loading...");
        dialog.show();
        System.out.println(" add contact" + contactsStr);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Apiurls.getBasePostURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("InsertGroupContact res",
                        response.toString());
                // sms19.inapp.msg.constant.Utils.printLog2("insertGroupContactRequest\n"+response);
                // 07-21 20:12:00.250: E/KK==(29329):
                // {"ContactGroupRegistration":[{"Msg":"Group created successfully"}]}

                // setAdapter();
                // try {
                // GlobalData.dbHelper.updateGroupmemberList(groupId,
                // numberJid);
                // } catch (Exception e) {
                // // TODO: handle exception
                // }
                button.setVisibility(View.INVISIBLE);
                // removeProgressDialog();
                getGroupContacts(editText.getText().toString().trim());
                setResult(RESULT_OK);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddBroadcastGroupActivity.this,
                        error.toString(), Toast.LENGTH_LONG).show();
                setAdapter();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                // removeProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
				/*
				 * Request.Form["Userid"], Request.Form["GroupName"],
				 * Request.Form["contacts"]
				 */
                params.put("Page", "InsertGroupContact");
                final String userId = getIntent().getStringExtra("userId");
                params.put("Userid", userId);
                params.put("GroupJID", groupId);
                params.put("contacts", contactsStr);
                params.put("GroupName", editText.getText().toString());
                Log.i("InsertGroupContact ", params.toString());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addNewGroupRequest(final String str) {
        CreateBroadCast fragment = CreateBroadCast.getInstance("contact");
        fragment.setGroupName(str);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(android.R.id.content, fragment, "CreateBroadCast");
        transaction.addToBackStack(null);
        transaction.commit();
        // transaction.commitAllowingStateLoss();
    }

    // private void addNewGroupRequest1(final String str) {
    //
    // showProgressDialog();
    //
    // StringRequest stringRequest = new StringRequest(Request.Method.POST,
    // Apiurls.getBasePostURL(), new Response.Listener<String>() {
    // @Override
    // public void onResponse(String response) {
    //
    // //sms19.inapp.msg.constant.Utils.printLog2("addNewGroupRequest\n"+response);
    // removeProgressDialog();
    //
    // JSONObject jsonObject = null;
    // try {
    // jsonObject = new JSONObject(response);
    // if(jsonObject!=null){
    // JSONArray array=jsonObject.getJSONArray("AddGroup");
    // JSONObject jsonObject2=array.getJSONObject(0);
    //
    // try{
    // if(jsonObject2!=null){
    // if(jsonObject2.has("Column1")){
    //
    // editText.setEnabled(false);
    //
    // String msg=jsonObject2.getString("Column1");
    // Toast.makeText(getApplicationContext(), msg,
    // Toast.LENGTH_LONG).show();
    //
    // Intent intent = new Intent(AddBroadcastGroupActivity.this,
    // SelectContactsActivity.class);
    // intent.putExtra("FROM", FROM_BROADCAST_NEW_GROUP_ADD_CONTACT_MODULE);
    // final String userId =getIntent().getStringExtra("userId");
    // intent.putExtra("userId",userId);
    // intent.putExtra("GROUP_NAME",str);
    // startActivityForResult(intent,
    // SELECT_CONTACTS_REQUEST_CODE);
    //
    // }
    // }
    //
    // }catch(Exception exp){
    //
    // }
    //
    // try{
    // if(jsonObject2!=null){
    // if(jsonObject2.has("Error")){
    // editText.setEnabled(true);
    // String msg=jsonObject2.getString("Error");
    // Toast.makeText(getApplicationContext(), msg,
    // Toast.LENGTH_LONG).show();
    // }
    // }
    //
    // }catch(Exception exp){
    //
    // }
    //
    //
    //
    // }
    //
    // } catch (JSONException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // Intent returnIntent = new Intent();
    // setResult(RESULT_OK, returnIntent);
    //
    // }
    // }, new Response.ErrorListener() {
    // @Override
    // public void onErrorResponse(VolleyError error) {
    // editText.setEnabled(true);
    // Toast.makeText(AddBroadcastGroupActivity.this, error.toString(),
    // Toast.LENGTH_LONG).show();
    //
    // removeProgressDialog();
    // }
    // }) {
    // @Override
    // protected Map<String, String> getParams() {
    // Map<String, String> params = new HashMap<String, String>();
    // /*Userid:25062
    // Page: DeleteContacts
    // contacts:{"contacts":[{"contactMobile":"9983186100","countryCode":"+91"}]}*/
    // params.put("Page", "AddGroup");
    // final String userId =getIntent().getStringExtra("userId");
    // params.put("Userid",userId);
    // params.put("GroupName",str);
    //
    // return params;
    // }
    //
    // };
    //
    // RequestQueue requestQueue = Volley.newRequestQueue(this);
    // requestQueue.add(stringRequest);
    // }

    private void getGroupContacts(final String str) {

        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Apiurls.getBasePostURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        try {

                            sms19.inapp.msg.constant.Utils
                                    .printLog2("addNewGroupRequest\n"
                                            + response);

                            JSONObject object = new JSONObject(response);

                            try {

                                JSONArray jsonArray = object
                                        .getJSONArray("GroupContactDetail");
                                final int size = jsonArray.length();

                                if (selectedContactsList != null) {
                                    for (ContactInfo info : selectedContactsList) {
                                        info.isSelected = false;
                                    }
                                    selectedContactsList.clear();
                                } else {
                                    selectedContactsList = new ArrayList<ContactInfo>();
                                }

										/*
										 * "Contact_Name":"Sandeep Sir",
										 * "contact_id":1541,
										 * "Contact_Mobile":"9610072104",
										 * "Contact_DOB":"",
										 * "Contact_Anniversary":"",
										 * "CountryCode":"+91"
										 */
                                for (int i = 0; i < size; i++) {
                                    final JSONObject jsonObject = jsonArray
                                            .getJSONObject(i);
                                    final ContactInfo contactInfo = new ContactInfo();
                                    contactInfo.contact_id = jsonObject
                                            .optString("contact_id");

                                    contactInfo.Contact_Name = jsonObject
                                            .optString("Contact_Name");
                                    contactInfo.Contact_Mobile = jsonObject
                                            .optString("Contact_Mobile");
                                    contactInfo.countryCode = jsonObject
                                            .optString("CountryCode");

                                    contactInfo.Contact_DOB = jsonObject
                                            .optString("Contact_DOB");
                                    contactInfo.Contact_Anniversary = jsonObject
                                            .optString("Contact_Anniversary");

                                    selectedContactsList
                                            .add(contactInfo);

                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                removeProgressDialog();
                                // Log.e(TAG, e.toString());
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            removeProgressDialog();
                            // Log.e(TAG, e.toString());
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                removeProgressDialog();
                                setAdapter();

                            }
                        });
                    }
                }).start();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddBroadcastGroupActivity.this,
                        error.toString(), Toast.LENGTH_LONG).show();

                removeProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Page", "GetGroupContact");
                final String userId = getIntent().getStringExtra("userId");
                params.put("userid", userId);
                params.put("GroupName", str);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
