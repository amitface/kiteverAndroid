package com.kitever.contacts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.kitever.customviews.FloatingActionButton;
import com.kitever.models.ContactInfo;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSettingWithBack;
import static sms19.inapp.msg.constant.Utils.getUserId;

public class ContactsActivity extends AppCompatActivity implements NetworkManager {

    private ProgressDialog progressDialog = null;
    public static int CHECK_CONTCT_FOR_DELETE = 101;

    protected int currentSelectedIndex = 0;

//    private ContactsFragment contactFragment;
    private GroupsFragment groupsFragment;
    private ContactLoaderFragment contactLoaderFragment;

    private Menu menu;

    private int currentlySelectedContacts = 0;
    private int currentlySelectedGroups = 0;

    private SearchView searchView;
    private TextView sorting_title, mUserNameTitle, actionbar_title;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_contacts);

        final ActionBar bar = getSupportActionBar();

        // actionBarSetting("Contacts");
        actionBarSettingWithBack(ContactsActivity.this, bar, "Contacts");
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        // Set the ViewPagerAdapter into ViewPager
        viewPager.setAdapter(new ContactsViewPagerAdapter(
                getSupportFragmentManager()));

        TabLayout slidingtab = (TabLayout) findViewById(R.id.pager_title_strip);
        slidingtab.setupWithViewPager(viewPager);
        slidingtab.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
        slidingtab.setSelectedTabIndicatorColor(Color.parseColor(CustomStyle.TAB_INDICATOR));
        slidingtab.setSelectedTabIndicatorHeight(8);
        //slidingtab.setTabTextColors(Color.parseColor(CustomStyle.HEADER_FONT_COLOR),Color.parseColor(CustomStyle.HEADER_FONT_COLOR));

        ViewGroup vg = (ViewGroup) slidingtab.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    // ((TextView) tabViewChild).setTypeface(Font.getInstance().getTypeFace(), Typeface.NORMAL);
                    TextView textViewToConvert = (TextView) tabViewChild;
                    textViewToConvert.setAllCaps(false);
                    setRobotoThinFont(textViewToConvert,this);
                    textViewToConvert.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
                }
            }
        }

        FloatingActionButton fabButton = new FloatingActionButton.Builder(
                ContactsActivity.this)
                .withDrawable(
                        getResources()
                                .getDrawable(R.drawable.ic_add_white_36dp))
                .withButtonColor(Color.parseColor(CustomStyle.FLOATING_ACTION_BACKGROUND))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16).create();

        fabButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (currentSelectedIndex == 0) {
                    Intent intent = new Intent(ContactsActivity.this,
                            sms19.listview.newproject.ContactAdd.class);
                    intent.putExtra("froninapp", true);
                    startActivityForResult(intent, 301);
                } else if (currentSelectedIndex == 1) {
                    Intent i = new Intent(ContactsActivity.this,
                            AddBroadcastGroupActivity.class);
                    i.putExtra("FOR", "ADD");
                    final String userId = getIntent().getStringExtra("userId");
                    i.putExtra("userId", userId);
                    startActivityForResult(i, 302);
                } else if (currentSelectedIndex == -1) {
                    Toast.makeText(getApplicationContext(),
                            "Scrolling...hold on", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                currentSelectedIndex = arg0;
                showOption();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                // currentSelectedIndex=-1;
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                // currentSelectedIndex=-1;
            }
        });
    }


    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                if (intent.getAction().equals(
                        com.kitever.utils.Utils.CONTACTS_UPDATE_ACTION)) {

                    final int TYPE = intent.getIntExtra(
                            com.kitever.utils.Utils.CONTACTS_MESSAGE_TYPE, 0);

                    if (TYPE == com.kitever.utils.Utils.CONTACTS_SYNC_UPDATE) {

                        final String message = intent
                                .getStringExtra(com.kitever.utils.Utils.CONTACTS_MESSAGE);

                        Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                .show();
//                        if (contactFragment != null) {
//                            contactFragment.initTask();
//                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(
                        mMessageReceiver,
                        new IntentFilter(
                                com.kitever.utils.Utils.CONTACTS_UPDATE_ACTION));
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contacts_fragment_menu, menu);
        this.menu = menu;
        MenuItem searchItem = this.menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String str) {

                if (currentSelectedIndex == 0) {
                    searchContacts(str);
                } else if (currentSelectedIndex == 1) {
                    searchGroups(str);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String str) {

                if (currentSelectedIndex == 0) {
                    searchContacts(str);
                } else if (currentSelectedIndex == 1) {
                    searchGroups(str);
                }

                return false;
            }
        });

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    private void searchContacts(final String constraint) {
        if (contactLoaderFragment != null) {
            if (contactLoaderFragment.getAdapter() != null) {
                contactLoaderFragment.getAdapter().getFilter().filter(constraint);
            }
        }
    }

    private void searchGroups(final String constraint) {
        if (groupsFragment != null) {
            if (groupsFragment.getAdapter() != null) {
                groupsFragment.getAdapter().getFilter().filter(constraint);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.action_done) {
            //showDialog();
            //check contact is used i
            checkContact();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void showOption() {

        if (currentSelectedIndex == 0) {
            updateMenuTitles(this.currentlySelectedContacts);
        } else if (currentSelectedIndex == 1) {
            updateMenuTitles(this.currentlySelectedGroups);
        }

        if (searchView != null) {
            if (currentSelectedIndex == 0) {
                searchContacts(searchView.getQuery().toString().trim());
            } else if (currentSelectedIndex == 1) {
                searchGroups(searchView.getQuery().toString().trim());
            }
        }

    }



    private void deleteContactRequest(final String str,
                                      final JSONArray jsonArray) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Apiurls.getBasePostURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // sms19.inapp.msg.constant.Utils.printLog2("delete contacts\n"+response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        JSONArray array = jsonObject
                                .getJSONArray("DeleteContacts");
                        JSONObject jsonObject2 = array.getJSONObject(0);

                        final String msg = jsonObject2.getString("Msg");


                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub

                                if (jsonArray != null) {

                                    for (int i = 0; i < jsonArray
                                            .length(); i++) {
                                        try {
                                            if (GlobalData.dbHelper != null) {

                                                JSONObject jsonObject = jsonArray
                                                        .getJSONObject(i);
                                                final String str = jsonObject
                                                        .getString("countryCode")
                                                        + jsonObject
                                                        .getString("contactMobile");
//                                                getContentResolver().delete(ChatContract.ConversationTable.CONTENT_URI, ChatContract.ConversationTable.COLUMN_NAME_NAME + "=?", new String[]{to});
                                                GlobalData.dbHelper
                                                        .deleteContact("phonenumber=?",new String[]{str});
                                            }
                                        } catch (Exception exp) {

                                        }
                                    }
                                }

                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method
                                        // stub

                                        Toast.makeText(
                                                getApplicationContext(),
                                                msg, Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });
                            }
                        }).start();
                        contactLoaderFragment.deleteSelected();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    removeProgressDialog();
                    e.printStackTrace();
                }
                removeProgressDialog();
                updateMenuTitles(0);
                if (contactLoaderFragment != null) {
//                    contactLoaderFragment.initTask();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ContactsActivity.this, error.toString(),
                        Toast.LENGTH_LONG).show();

                removeProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
				/*
				 * Userid:25062 Page: DeleteContacts
				 * contacts:{"contacts":[{"contactMobile"
				 * :"9983186100","countryCode":"+91"}]}
				 */
                params.put("Page", "DeleteContacts");
                final String userId = getIntent().getStringExtra("userId");
                params.put("Userid", userId);
                params.put("contacts", str);

                Log.e("del", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void updateMenuTitles(int selected) {

        if (currentSelectedIndex == 0) {
            this.currentlySelectedContacts = selected;
        } else if (currentSelectedIndex == 1) {
            this.currentlySelectedGroups = selected;
        }

        if (this.menu != null) {

            MenuItem action_doneMenuItem = this.menu.findItem(R.id.action_done);
            if (selected > 0) {
                action_doneMenuItem.setEnabled(true);
                // action_doneMenuItem.setVisible(true);
                action_doneMenuItem.setTitle("(" + selected + ")" + " DELETE");
            } else {
                action_doneMenuItem.setEnabled(false);
                // action_doneMenuItem.setVisible(false);
                action_doneMenuItem.setTitle("DELETE");
            }

        }

    }

    // check contact is used in order or not
    private void checkContact() {
        final ArrayList<ContactInfo> arrayList = (ArrayList<ContactInfo>) contactLoaderFragment.getAdapter().getFilteredArrayList();
        String contctids = "";
        for (ContactInfo contactInfo : arrayList) {
            if (contactInfo.isSelected) {
                //jsonObject2.put("contactID",contactInfo.contact_id);
                if (contctids.trim().length() > 0)
                    contctids += "," + contactInfo.contact_id;
                else contctids += contactInfo.contact_id;
            }
        }

        try {
            showProgressDialog();
            Map map = new HashMap<>();
            map.put("Page", "ContactIDExist");
            map.put("userID", getUserId(this));
            map.put("Contactids", contctids);

            Log.i("map-", map.toString());

            new RequestManager().sendPostRequest(this,
                    CHECK_CONTCT_FOR_DELETE, map);

        } catch (Exception e) {
            removeProgressDialog();
            // TODO: handle exception
        }

    }

    private void showDialog(String str) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        showProgressDialog();

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try {
                                    final JSONObject jsonObject = new JSONObject();

                                    final JSONArray jsonArray = new JSONArray();

                                    final ArrayList<ContactInfo> arrayList = (ArrayList<ContactInfo>) contactLoaderFragment
                                            .getAdapter().getFilteredArrayList();

                                    for (ContactInfo contactInfo : arrayList) {
                                        if (contactInfo.isSelected) {
                                            // {"contactMobile":"9983186100","countryCode":"+91"}
                                            JSONObject jsonObject2 = new JSONObject();
                                            //jsonObject2.put("contactID",contactInfo.contact_id);
                                            jsonObject2.put("contactMobile",
                                                    contactInfo.getContactNumber());
                                            jsonObject2.put("countryCode",
                                                    contactInfo.getCountryCode());

                                            jsonArray.put(jsonObject2);
                                        }
                                    }

                                    jsonObject.put("contacts", jsonArray);

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            deleteContactRequest(
                                                    jsonObject.toString(),
                                                    jsonArray);

                                        }
                                    });

                                } catch (Exception exp) {
                                    removeProgressDialog();
                                }

                            }
                        }).start();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub

                                try {

                                    final ArrayList<ContactInfo> arrayList = ContactsSingleton
                                            .getInstance().getContactInfos();

                                    for (ContactInfo contactInfo : arrayList) {
                                        contactInfo.isSelected = false;
                                    }

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
//                                            contactFragment.initAdapter();
                                            updateMenuTitles(0);
                                        }
                                    });

                                } catch (Exception exp) {

                                }

                            }
                        }).start();
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(
                ContactsActivity.this);
        builder.setTitle("Delete contact(s)?");
        String msg = "";
        if (str.equals("0"))
        {
            msg = "You cannot delete this contact as it will delete all payment and credit history associated with it.";
            builder.setMessage(msg).setNeutralButton("ok",dialogClickListener).show();
        }

        else{
            msg = "Are you sure you want to delete the selected contact(s)?";
            builder.setMessage(msg)
                    .setPositiveButton("Yes (Delete)", dialogClickListener)
                    .setNegativeButton("No (Deselect)", dialogClickListener).show();
        }

    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ContactsActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
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
        super.onActivityResult(requestCode,resultCode,data);
        // Check which request we're responding to
        if (requestCode == 301) {

            if (resultCode == RESULT_OK) {
                if (contactLoaderFragment != null) {
                    contactLoaderFragment.refreshContact();
                }
            }
        }

        if (requestCode == 302) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (groupsFragment != null) {
                    groupsFragment.getGroupDetails();
                }
            }
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        removeProgressDialog();
        if (response != null && response.length() > 0) {
            if (requestId == CHECK_CONTCT_FOR_DELETE) {
                //{"ContactExists":[{"Status":"0"}]}
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        JSONArray array = jsonObject
                                .getJSONArray("ContactExists");
                        JSONObject jsonObject2 = array.getJSONObject(0);

                        String Status = jsonObject2.getString("Status");
                        showDialog(Status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        removeProgressDialog();
    }

    class ContactsViewPagerAdapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 2;
        // Tab Titles
        private String tabtitles[] = new String[]{"Contacts", "Groups", "ContactLoader"};
        Context context;

        public ContactsViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                // Open FragmentTab1.java
                case 0:
//                    contactFragment = new ContactsFragment();
//                    return contactFragment;
                    contactLoaderFragment = ContactLoaderFragment.newInstance("","");
                    return contactLoaderFragment;
                // Open FragmentTab2.java
                case 1:
                    groupsFragment = new GroupsFragment();
                    return groupsFragment;
//                case 2:
//
//                    return ContactLoaderFragment.newInstance("","");
            }
            return null;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabtitles[position];
        }
    }

}
