package com.kitever.contacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.models.ContactInfo;
import com.kitever.models.ContactList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.ContactUtil;
import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.ContactAdd;

@SuppressWarnings("deprecation")
public class ContactsFragment extends Fragment implements AdapterInterface {

    private ListView mListView;

    private String userId = null;

    private final String TAG = "ContactsFragment";

    private TextView textView;

    private ContactsAdapter adapter = null;

    public static ContactsFragment newInstance(int someInt) {
        ContactsFragment myFragment = new ContactsFragment();

        Bundle args = new Bundle();
        args.putInt("someInt", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts, container,
                false);

//        ((ContactsActivity) getActivity()).showProgressDialog();

        textView = (TextView) view.findViewById(R.id.textView);

        mListView = (ListView) view.findViewById(R.id.listView);
        userId = getActivity().getIntent().getStringExtra("userId");

        if (ContactUtil.isInsertContactRunning) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Loading contacts, please wait...");
        }

//        if (ContactsSingleton.getInstance() == null
//                || ContactsSingleton.getInstance().getContactInfos() == null
//                || ContactsSingleton.getInstance().getContactInfos().size() <= 2) {
//            initTask();
//        } else {
//            initAdapter();
//        }

        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (ContactAdd.isContactsUpdated) {
            ContactAdd.isContactsUpdated = false;
            initTask();
        }
    }

    public void initTask() {
        // TODO Auto-generated method stub
//        final ContactAsyncTask asyncTask = new ContactAsyncTask();
//        asyncTask.execute(userId);
    }

    private void setContactsStatus() {
        if (ContactUtil.isInsertContactRunning) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Loading contacts, please wait...");
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText("No Contacts to display");
        }
    }

    public void initAdapter() {
        // TODO Auto-generated method stub
        if (ContactsSingleton.getInstance().getContactInfos() != null
                && ContactsSingleton.getInstance().getContactInfos().size() > 0) {
            textView.setVisibility(View.GONE);
            adapter = new ContactsAdapter(getActivity(), ContactsSingleton
                    .getInstance().getContactInfos(), this);
            mListView.setAdapter(adapter);
        } else {
            setContactsStatus();
        }

        ((ContactsActivity) getActivity()).removeProgressDialog();
    }

    public ContactsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ContactsAdapter adapter) {
        this.adapter = adapter;
    }

    class ContactAsyncTask extends AsyncTask<String, Void, Void> {

        public ContactAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((ContactsActivity) getActivity()).showProgressDialog();
        }

        @Override
        protected Void doInBackground(String... params) {
            boolean isOnline = Utils.isDeviceOnline(getActivity());

            HttpClient httpclient = new DefaultHttpClient();

            final String stringUrl = Apiurls.getBasePostURL();

            HttpPost httppost = new HttpPost(stringUrl);

            try {
                // Add your data
                String result = "";
                SharedPreferences prfs = getActivity().getSharedPreferences(
                        "ContactsData", Context.MODE_PRIVATE);
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
                    System.out.println("contant data" + result);
                    // CONVERT RESPONSE STRING TO JSON ARRAY
                    try {
                        JSONObject object = new JSONObject(result);

                        try {

                            JSONArray jsonArray = object
                                    .getJSONArray("ContactDetails");
                            final int size = jsonArray.length();

                            final ArrayList<ContactInfo> contactInfos = new ArrayList<ContactInfo>();

                            Gson gson = new Gson();
                            ContactList contact = gson.fromJson(result, ContactList.class);

                            ContactsSingleton.getInstance().setContactInfos(
                                    contact.getContactDetails());

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            // Log.e(TAG, e.toString());
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        // Log.e(TAG, e.toString());
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
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
            if(result!=null)         initAdapter();

        }
    }

    @Override
    public void onItemClicked(int position, int forWhat) {
        // TODO Auto-generated method stub
        if (forWhat == 1) {

            if (((ContactsActivity) getActivity()).currentSelectedIndex == 0) {

                final ArrayList<ContactInfo> arrayList = ContactsSingleton
                        .getInstance().getContactInfos();
                int selected = 0;
                for (ContactInfo contactInfo : arrayList) {
                    if (contactInfo.isSelected) {
                        selected++;
                    }
                }
                ((ContactsActivity) getActivity()).updateMenuTitles(selected);
            }
        } else if (forWhat == 2) {

            if (getActivity() != null) {
                final ArrayList<ContactInfo> arrayList = adapter
                        .getFilteredArrayList();
                if (arrayList != null) {
                    Intent intent = new Intent(getActivity(),
                            sms19.listview.newproject.ContactAdd.class);
                    intent.putExtra("froninapp", false);
                    intent.putExtra("EDIT_CONTACT", arrayList.get(position));
                    startActivityForResult(intent, 301);
                }
            }
        }
    }

    @Override
    public void onItemsEmpty() {
        // TODO Auto-generated method stub
        textView.setVisibility(View.VISIBLE);
        textView.setText("No contact result to display");
    }

    @Override
    public void onItemsAvailable() {
        // TODO Auto-generated method stub
        textView.setVisibility(View.GONE);
        textView.setText("");
    }

}
