package com.kitever.contacts;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.newproject.model.FetchGroupDetails.GroupDetails;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

public class GroupsFragment extends Fragment implements AdapterInterface {

    private String userId = null;
    private ListView mListView;

    // private int selected=0;
    private TextView textView;
    private ArrayList<GroupDetails> list;
    private GroupsAdapter adapter = null;

    public static GroupsFragment newInstance(int someInt) {
        GroupsFragment myFragment = new GroupsFragment();

        Bundle args = new Bundle();
        args.putInt("someInt", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }

    // private ArrayList<Groupmodel> recentlist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab2.xml
        View view = inflater
                .inflate(R.layout.fragment_groups, container, false);

        userId = getActivity().getIntent().getStringExtra("userId");
        ((ContactsActivity) getActivity()).showProgressDialog();

        textView = (TextView) view.findViewById(R.id.textView);
        mListView = (ListView) view.findViewById(R.id.listView);

        getGroupDetails();

        return view;
    }

    private void initAdapter(ArrayList<GroupDetails> list) {
        // TODO Auto-generated method stub

        textView.setText("Loading groups...");
        textView.setVisibility(View.GONE);

        adapter = new GroupsAdapter(getActivity(), list, this);
        mListView.setAdapter(adapter);

        ((ContactsActivity) getActivity()).removeProgressDialog();
    }

    public GroupsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(GroupsAdapter adapter) {
        this.adapter = adapter;
    }

    private void setNoGroupsMsg() {
        textView.setText("No groups");
        textView.setVisibility(View.VISIBLE);
    }

    private void setData() {
        if (list != null) {

            if (list.size() == 0) {
                setNoGroupsMsg();
            } else
                initAdapter(list);
        } else {
            setNoGroupsMsg();
        }
        ((ContactsActivity) getActivity()).removeProgressDialog();
    }

    /*
     * private void getGroupDetails1(){ recentlist=new ArrayList<Groupmodel>();
     *
     * ArrayList<Groupmodel> groupmodelsNew=new ArrayList<Groupmodel>();
     * groupmodelsNew
     * .addAll((GlobalData.dbHelper.getGroupallmemberListfromDB()));
     * recentlist.addAll(groupmodelsNew); }
     */
    protected void getGroupDetails() {

        new webservice(null, webservice.GetAllGroupDetails.geturl(userId),
                webservice.TYPE_GET, webservice.TYPE_GET_GROUP_DETAILS,
                new ServiceHitListener() {

                    @Override
                    public void onSuccess(Object Result, int id) {

                        if (id == webservice.TYPE_GET_GROUP_DETAILS) {

                            FetchGroupDetails Model = (FetchGroupDetails) Result;
                            if (list != null) {
                                list = null;
                            }
                            list = (ArrayList<GroupDetails>) Model
                                    .getGroupDetails();
                            setData();

                        }

                    }

                    @Override
                    public void onError(String Error, int id) {
                        setData();
                        Toast.makeText(getActivity().getApplicationContext(),
                                Error, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    String group_name;

    @Override
    public void onItemClicked(int position, int forWhat) {
        // TODO Auto-generated method stub

        if (adapter != null) {
            group_name = adapter.getFilteredArrayList().get(position).group_name;

            if (forWhat == 1) {

                if (getActivity() != null) {
                    Intent i = new Intent(getActivity(),
                            AddBroadcastGroupActivity.class);
                    i.putExtra("FOR", "EDIT");
                    i.putExtra("userId", userId);
                    i.putExtra("group_name", group_name);
                    i.putExtra("groupId", getRemote_jid());
                    getActivity().startActivityForResult(i, 302);
                }

            } else if (forWhat == 2) {
                if (list != null) {
                    showDeleteDialog(getRemote_jid());
                }

            } else if (forWhat == 3) {
                if (getActivity() != null) {
                    Intent i = new Intent(getActivity(),
                            AddBroadcastGroupActivity.class);
                    i.putExtra("FOR", "VIEW");
                    i.putExtra("userId", userId);
                    i.putExtra("group_name", group_name);
                    getActivity().startActivityForResult(i, 302);
                }
            }

        }

    }

	/*
     * @Override public void onActivityResult(int requestCode, int resultCode,
	 * Intent data) { // TODO Auto-generated method stub
	 * super.onActivityResult(requestCode, resultCode, data);
	 * 
	 * if (requestCode == 311) { // Make sure the request was successful if
	 * (resultCode == FragmentActivity.RESULT_OK) { getGroupDetails();
	 * 
	 * } }
	 * 
	 * }
	 */

    private void showDeleteDialog(final String str) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        // ((ContactsActivity)getActivity()).showProgressDialog();

                        deleteBroadcastlistGroupRequest(str);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete BROADCAST LIST group?");
        builder.setMessage(
                "Are you sure you want to delete the selected BROADCAST LIST group?")
                .setPositiveButton("Yes (Delete)", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    String remote_jid;

    public String getRemote_jid() {
        remote_jid = GlobalData.dbHelper.getBroadCastId(group_name);
        return remote_jid;
    }

    public void deleteBroadcastlistGroupRequest(String remotejid) {

        try {

            if (Utils.isDeviceOnline(getActivity())) {
                GlobalData.dbHelper.deleteParticularUserHistory(remotejid);
                GlobalData.dbHelper.deleteRecentParticularrow(remotejid);
                GlobalData.dbHelper.deleteGroupParticularrow(remotejid);
                GlobalData.dbHelper.updateContactmsgData(remotejid, "", "");
                broadCastDelete(remotejid, 0);

                // GroupListFrgament frgament=GroupListFrgament.newInstance("");
                // if(frgament!=null){
                // frgament.refreshChatAdapter();
                // }
                //
                // ChatFragment fChatFragment=ChatFragment.newInstance("");
                // if(fChatFragment!=null){
                // fChatFragment.refreshChatAdapter();
                // }

            } else {
                Toast.makeText(getActivity(), "Check your network connection",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void broadCastDelete(String broadcastid, int page) {
        userId = Utils.getUserId(getActivity());
        BroadCastDeleteAsyncTask asyncTask = new BroadCastDeleteAsyncTask(
                userId, broadcastid, page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asyncTask.execute();
        }

    }

	/*
	 * private void deleteBroadcastlistGroupRequest1(final String str) {
	 * 
	 * StringRequest stringRequest = new StringRequest(Request.Method.POST,
	 * Apiurls.getBasePostURL(), new Response.Listener<String>() {
	 * 
	 * @Override public void onResponse(String response) {
	 * 
	 * 
	 * //sms19.inapp.msg.constant.Utils.printLog2("delete contacts\n"+response);
	 * // {"DeleteGroup":[{"Msg":"Group deleted successfully"}]} JSONObject
	 * jsonObject = null; try { jsonObject = new JSONObject(response);
	 * if(jsonObject!=null){ JSONArray
	 * array=jsonObject.getJSONArray("DeleteGroup"); JSONObject
	 * jsonObject2=array.getJSONObject(0);
	 * 
	 * try{ if(jsonObject2!=null){ if(jsonObject2.has("Msg")){ String
	 * msg=jsonObject2.getString("Msg");
	 * Toast.makeText(getActivity().getApplicationContext(), msg,
	 * Toast.LENGTH_LONG).show(); getGroupDetails(); }else{
	 * ((ContactsActivity)getActivity()).removeProgressDialog(); } }else{
	 * ((ContactsActivity)getActivity()).removeProgressDialog(); }
	 * 
	 * }catch(Exception exp){
	 * ((ContactsActivity)getActivity()).removeProgressDialog(); }
	 * 
	 * try{ if(jsonObject2!=null){ if(jsonObject2.has("Error")){ String
	 * msg=jsonObject2.getString("Error");
	 * Toast.makeText(getActivity().getApplicationContext(), msg,
	 * Toast.LENGTH_LONG).show(); } }
	 * 
	 * }catch(Exception exp){
	 * ((ContactsActivity)getActivity()).removeProgressDialog(); }
	 * 
	 * 
	 * }
	 * 
	 * } catch (JSONException e) { // TODO Auto-generated catch block
	 * ((ContactsActivity)getActivity()).removeProgressDialog();
	 * e.printStackTrace(); }
	 * 
	 * } }, new Response.ErrorListener() {
	 * 
	 * @Override public void onErrorResponse(VolleyError error) {
	 * ((ContactsActivity)getActivity()).removeProgressDialog();
	 * Toast.makeText(getActivity(), error.toString(),
	 * Toast.LENGTH_LONG).show(); } }) {
	 * 
	 * @Override protected Map<String, String> getParams() { Map<String, String>
	 * params = new HashMap<String, String>();
	 * 
	 * params.put("Page", "DeleteGroup"); final String userId
	 * =getActivity().getIntent().getStringExtra("userId");
	 * params.put("Userid",userId); params.put("GroupName",str);
	 * 
	 * return params; }
	 * 
	 * };
	 * 
	 * RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
	 * requestQueue.add(stringRequest); }
	 */

    @Override
    public void onItemsEmpty() {
        // TODO Auto-generated method stub
        textView.setVisibility(View.VISIBLE);
        textView.setText("No group result to display");
    }

    @Override
    public void onItemsAvailable() {
        // TODO Auto-generated method stub
        textView.setVisibility(View.GONE);
        textView.setText("");
    }

    private class BroadCastDeleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private String response = "";
        private JSONObject resObj;
        private String userId = "";
        private String broadcastid = "";
        private int page = 0;

        private BroadCastDeleteAsyncTask(String userId, String broadcastid,
                                         int page) {
            this.userId = userId;
            this.broadcastid = broadcastid;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String stringUrl = Apiurls.KIT19_BASE_URL
                        + "DeleteBroadCastGroup&UserId=" + userId
                        + "&BraodCastGroups=" + broadcastid;
                stringUrl = stringUrl.replace(" ", "");
                Rest rest = Rest.getInstance();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("Page",
                        "DeleteBroadCastGroup"));
                nameValuePairs.add(new BasicNameValuePair("BraodCastGroups",
                        broadcastid));
                nameValuePairs.add(new BasicNameValuePair("UserId", userId));
                Log.i("delete group", nameValuePairs.toString());
                // response = rest.deleteBroadCast(stringUrl);
                response = rest.post(
                        Apiurls.KIT19_BASE_URL.replace("?Page=", ""),
                        nameValuePairs);
                Utils.printLog("DeleteBroadCastGroup Response1112:  "
                        + response);

            } catch (Exception e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                if (response != null && response.trim().length() != 0) {
                    try {
                        resObj = new JSONObject(response);
                        if (resObj.has("Message")) {
                            getGroupDetails();
                            Toast.makeText(getActivity(),
                                    "Broaod Cast Group Deleted successfully",
                                    Toast.LENGTH_LONG).show();
                        } else {

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

}
