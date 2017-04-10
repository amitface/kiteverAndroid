package sms19.inapp.msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.ContactUtil;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.NewContactModelForFlag;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

public class ContactFragment extends Fragment implements OnClickListener {

    private static ContactFragment contactFragment;
    public sms19.inapp.msg.adapter.ContactAdapter chatAdapter = null;
    private ListView mListView;
    private InAppMessageActivity homeActivity = null;
    private Button addGroup;
    public static SharedPreferences chatPrefs;

    /***************
     * xmpp
     **************/

    public static Handler UserupdateHandler;
    public static Handler deleteContactHandler;
    private LinearLayout mInviteBtn, mCreateBtn, mBroadCastBtn;
    private TextView mDataNotFoundText;
    public static ArrayList<Contactmodel> contactlist;
    public static ArrayList<Contactmodel> appUserList = new ArrayList<Contactmodel>();
    public static ArrayList<Contactmodel> appUserList2 = new ArrayList<Contactmodel>();
    public static ArrayList<Contactmodel> sms19AppUsers = new ArrayList<Contactmodel>();
    public static ArrayList<Contactmodel> nonAppUser = new ArrayList<Contactmodel>();
    public static int BROAD_CAST_FLAG = 0;// 0 mean noting, 1 mean both , 2 mean app user, 3 mean non app users

    private boolean sortbynumber_appuser = true;
    private boolean sortbynumber_nonappuser = true;
    private boolean sortbyname_appuser = true;
    private boolean sortbyname_nonappuser = true;
    private ActionMode modeCustom = null;
    private HashMap<String, Contactmodel> selectedItem = new HashMap<String, Contactmodel>();
    private LinearLayout loadmore_progress;
    private ProgressBar progress;
    private boolean sortbyname_both_new = true;
    private String previousrowid = "0";
    public int IS_USER_TYPE = 0;
    private getAppUsersLoadAsyncTask appUsersLoadAsyncTask = null;
    private getLoadMoreNonAppContactAsyncTask loadMoreNonAppContactAsyncTask = null;
    private Handler appuserHandler;
    private Handler nonAppuserHandler;
    private int current_sort = 0;// 0 mean  sort by name , and 1 mean sort by mobile,2 mean appuser,3 mean non app user


    public static ContactFragment newInstance(String titleName) {

        return contactFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customonCreate();
    }

    public void customonCreate() {
        homeActivity = (InAppMessageActivity) getActivity();
        ConstantFlag.TAB_BACK_HANDLE_FRAGMENT = "";
        homeActivity.getLayout_name_status().setVisibility(View.GONE);
        homeActivity.getmActionBarImage().setVisibility(View.GONE);
        homeActivity.getLayoutTab_contact_chat().setVisibility(View.VISIBLE);
        ConstantFields.HIDE_MENU = 4;
        homeActivity.invalidateOptionMenuItem();
        GlobalData.FilterRecentlist = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contactFragment = this;
        ContactFragment.BROAD_CAST_FLAG = 1;
        View view = getActivity().getLayoutInflater().inflate(R.layout.contact_fragment, container, false);
        inItHandlerNonAppUsersForLoadMore();
        inItHandlerAppUsersForLoadMore();
        initiateView(view);

        Utils.saveSelectedItem(getActivity(), new HashMap<String, Contactmodel>());    // selected array save null

        setUserupdateHandler();
        setDeleteContacteHandler();

        contactlist = new ArrayList<Contactmodel>();
        loadmore_progress.setVisibility(View.GONE);


        getLoadMoreParent(0);// 2 mean user load more both user types and one mean only one types


        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void getAppUsersForLoadMore(int isAppuser) {
        callAppUserHandler(isAppuser);
    }


    public void callAppUserHandler(int isAppuser) {
        android.os.Message msg = new android.os.Message();//comment m
        Bundle b = new Bundle();
        b.putInt("isAppuser", isAppuser);
        msg.setData(b);
        if (contactlist != null) {
            contactlist.clear();
        }
        appuserHandler.sendMessage(msg);

    }

    public void callNonAppUserHandler(int isNonAppuser) {
        android.os.Message msg = new android.os.Message();//comment m
        Bundle b = new Bundle();
        b.putInt("isNonAppuser", isNonAppuser);
        msg.setData(b);
        nonAppuserHandler.sendMessage(msg);
    }


    private void inItHandlerAppUsersForLoadMore() {

        appuserHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {

                super.handleMessage(msg);
                int isAppuserSearch = 0;
                ArrayList<Contactmodel> tempchatlist = new ArrayList<Contactmodel>();
                ArrayList<Contactmodel> alreadyGetedList = new ArrayList<Contactmodel>();
                isAppuserSearch = msg.getData().getInt("isAppuser");
                alreadyGetedList.addAll(chatAdapter.getContactArrayList());
                tempchatlist = new ArrayList<Contactmodel>();
                if (previousrowid.equalsIgnoreCase("0")) {
                    tempchatlist.addAll(GlobalData.dbHelper.getContactForLoadMoreFromDBRegister(previousrowid));
                } else {
                    tempchatlist.addAll(GlobalData.dbHelper.getContactForLoadMoreFromDBRegister(previousrowid));
                }

                if (tempchatlist != null) {
                    if (tempchatlist.size() > 0) {
                        Collections.sort(tempchatlist, new Contactmodel().new CustomComparatorSortByName());
                    }
                }

                progress.setVisibility(View.GONE);
                if (tempchatlist != null && tempchatlist.size() > 0) {
                    previousrowid = tempchatlist.get(tempchatlist.size() - 1).getRow_id();
                    contactlist.addAll(contactlist.size(), tempchatlist);
                    loadmore_progress.setTag(previousrowid);
                }
                chatAdapter.setContactArrayList(contactlist);
                chatAdapter.notifyDataSetChanged();
                onResumeUpdate();

                if (isAppuserSearch == 1) {
                    loadmore_progress.setVisibility(View.GONE);
                } else {
                    IS_USER_TYPE = 2;
                    getNonAppUsersForLoadMore(2);
                }
            }
        };
    }


    private void inItHandlerNonAppUsersForLoadMore() {

        nonAppuserHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {

                super.handleMessage(msg);

                ArrayList<Contactmodel> tempchatlist = new ArrayList<Contactmodel>();
                previousrowid = (String) loadmore_progress.getTag();

                try {
                    if (previousrowid.equalsIgnoreCase("0")) {
                        tempchatlist.addAll(GlobalData.dbHelper.getContactfromDBOnlySMS19RowBase2(previousrowid));

                    } else {
                        tempchatlist.addAll(GlobalData.dbHelper.getContactfromDBOnlySMS19RowBase2(previousrowid));
                    }

                    if (tempchatlist != null) {
                        if (tempchatlist.size() > 0) {
                            Collections.sort(tempchatlist, new Contactmodel().new CustomComparatorSortByName());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                progress.setVisibility(View.GONE);

                if (tempchatlist != null && tempchatlist.size() > 0) {
                    previousrowid = tempchatlist.get(tempchatlist.size() - 1).getRow_id();
                    contactlist.addAll(contactlist.size(), tempchatlist);
                    loadmore_progress.setTag(previousrowid);

                }
                chatAdapter.setContactArrayList(contactlist);
                chatAdapter.notifyDataSetChanged();
                onResumeUpdate();


                if (tempchatlist != null && tempchatlist.size() == 0) {
                    loadmore_progress.setTag(R.id.loadmore_progress, false);//
                    loadmore_progress.setVisibility(View.GONE);
                    progress.setTag(R.id.progress, false);//
                } else {
                    loadmore_progress.setTag(R.id.loadmore_progress, false);//
                    loadmore_progress.setVisibility(View.VISIBLE);
                }
                progress.setTag(R.id.progress, false);//


            }
        };
    }


    public void getNonAppUsersForLoadMore(int isNonAppuser) {
        callNonAppUserHandler(isNonAppuser);
    }


    public void getLoadMoreParent(int isusersearchtypre) { // 0 mean default both user have load more, 1 mean only app user and 2 mean nonapp users
        boolean isAppuserLoadMore = true;

        progress.setVisibility(View.VISIBLE);
        loadmore_progress.setVisibility(View.GONE);

        previousrowid = (String) loadmore_progress.getTag();
        isAppuserLoadMore = (Boolean) loadmore_progress.getTag(R.id.loadmore_progress);

        if (contactlist != null) {
            contactlist.clear();
        }

        if (isusersearchtypre != 2) {

            if (isAppuserLoadMore) {
                getAppUsersForLoadMore(isusersearchtypre);
            } else {
                getNonAppUsersForLoadMore(isusersearchtypre);
            }
        } else {
            getNonAppUsersForLoadMore(isusersearchtypre);
        }
    }


    class getAppUsersLoadAsyncTask extends AsyncTask<Integer, Void, Void> {
        ArrayList<Contactmodel> tempchatlist;
        int scrollposition = 0;
        int isAppuserSearch = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tempchatlist = new ArrayList<Contactmodel>();

        }

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                isAppuserSearch = params[0];

                previousrowid = (String) loadmore_progress.getTag();

                if (previousrowid.equalsIgnoreCase("0")) {

                    tempchatlist.addAll(GlobalData.dbHelper.getContactForLoadMoreFromDBRegister(previousrowid));


                } else {
                    tempchatlist.addAll(GlobalData.dbHelper.getContactForLoadMoreFromDBRegister(previousrowid));
                }

                if (tempchatlist != null) {
                    if (tempchatlist.size() > 0) {
                        Collections.sort(tempchatlist, new Contactmodel().new CustomComparatorSortByName());

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                progress.setVisibility(View.GONE);

                if (tempchatlist.size() > 0) {
                    scrollposition = tempchatlist.size();
                    previousrowid = tempchatlist.get(tempchatlist.size() - 1).getRow_id();
                    contactlist.addAll(contactlist.size(), tempchatlist);
                    loadmore_progress.setTag(previousrowid);

                }
                chatAdapter.setContactArrayList(contactlist);
                chatAdapter.notifyDataSetChanged();
                onResumeUpdate();

                if (isAppuserSearch == 1) {
                    loadmore_progress.setVisibility(View.GONE);
                } else {
                    IS_USER_TYPE = 2;
                    getNonAppUsersForLoadMore(2);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }


    class getLoadMoreNonAppContactAsyncTask extends AsyncTask<Void, Void, Void> {
        ArrayList<Contactmodel> tempchatlist;
        int scrollposition = 0;
        ArrayList<Contactmodel> alreadyGetedList = new ArrayList<Contactmodel>();


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            alreadyGetedList.addAll(chatAdapter.getContactArrayList());
            tempchatlist = new ArrayList<Contactmodel>();
        }

        @Override
        protected Void doInBackground(Void... params) {

            previousrowid = (String) loadmore_progress.getTag();

            try {
                if (previousrowid.equalsIgnoreCase("0")) {
                    tempchatlist.addAll(GlobalData.dbHelper.getContactfromDBOnlySMS19RowBase2(previousrowid));
                } else {
                    tempchatlist.addAll(GlobalData.dbHelper.getContactfromDBOnlySMS19RowBase2(previousrowid));
                }

                if (tempchatlist != null) {
                    if (tempchatlist.size() > 0) {
                        Collections.sort(tempchatlist, new Contactmodel().new CustomComparatorSortByName());

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                progress.setVisibility(View.GONE);
                if (tempchatlist.size() > 0) {
                    scrollposition = tempchatlist.size();
                    previousrowid = tempchatlist.get(tempchatlist.size() - 1).getRow_id();
                    contactlist.addAll(contactlist.size(), tempchatlist);
                    loadmore_progress.setTag(previousrowid);
                }
                chatAdapter.setContactArrayList(contactlist);
                chatAdapter.notifyDataSetChanged();
                onResumeUpdate();

                if (tempchatlist.size() == 0) {
                    loadmore_progress.setTag(R.id.loadmore_progress, false);//
                    loadmore_progress.setVisibility(View.GONE);
                    progress.setTag(R.id.progress, false);//
                } else {
                    loadmore_progress.setTag(R.id.loadmore_progress, false);//
                    loadmore_progress.setVisibility(View.VISIBLE);
                }
                progress.setTag(R.id.progress, false);//

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 301
                && resultCode == Activity.RESULT_OK) {
            customonCreate();
            refrsh();

        }
    }

    public void initiateView(View view) {

        mListView = (ListView) view.findViewById(R.id.list_view);
        mDataNotFoundText = (TextView) view.findViewById(R.id.no_data_found);
        mInviteBtn = (LinearLayout) view.findViewById(R.id.invite_btn);
        mCreateBtn = (LinearLayout) view.findViewById(R.id.create_group_btn);
        mBroadCastBtn = (LinearLayout) view.findViewById(R.id.broadcast_btn);
        View view2 = getActivity().getLayoutInflater().inflate(R.layout.contact_bottom_row, null);
        addGroup = (Button) view2.findViewById(R.id.add_group);

        progress = (ProgressBar) view.findViewById(R.id.progress);

        addGroup.setOnClickListener(this);
        mInviteBtn.setOnClickListener(this);
        mCreateBtn.setOnClickListener(this);
        mBroadCastBtn.setOnClickListener(this);

        contactlist = new ArrayList<Contactmodel>();
        ArrayList<Contactmodel> contactmodels = new ArrayList<Contactmodel>();

        chatAdapter = new sms19.inapp.msg.adapter.ContactAdapter(getActivity(), contactmodels);
        chatAdapter.setClickListener(this);
        mListView.setAdapter(chatAdapter);

        if (ContactUtil.isInsertContactRunning) {
            mDataNotFoundText.setText("Loading contacts, please wait...");
        } else {
            mDataNotFoundText.setText("User Contact List Not Found!");
        }

        loadmore_progress = (LinearLayout) view.findViewById(R.id.loadmore_progress);
        loadmore_progress.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        loadmore_progress.setOnClickListener(this);

        loadmore_progress.setTag("0");
        loadmore_progress.setTag(R.id.loadmore_progress, true);//
        progress.setTag(R.id.progress, true);//


        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;
                ArrayList<Contactmodel> arrayList = chatAdapter.getContactArrayList();


                if (arrayList.size() == lastItem) {
                    loadmore_progress.setVisibility(View.VISIBLE);


                    boolean isscroll = (Boolean) progress.getTag(R.id.progress);
                    if (!isscroll) {
                        loadmore_progress.setVisibility(View.GONE);
                    } else {
                        if (IS_USER_TYPE == 1) {
                            loadmore_progress.setVisibility(View.GONE);
                        } else {
                            //	getLoadMoreParent(IS_USER_TYPE);
                        }
                    }

                } else {
                    loadmore_progress.setVisibility(View.GONE);
                }

            }
        });

    }


    @Override
    public void onClick(View v) {

        if (loadmore_progress == v) {
            getLoadMoreParent(IS_USER_TYPE);
        }


        if (mInviteBtn == v) {

            boolean b = false;
            HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();
            hashMap.clear();
            hashMap.putAll(selectedItem);
            boolean isInvite = false;

            ArrayList<String> arrayListnew = new ArrayList<String>();
            for (Object key : selectedItem.keySet()) {
                arrayListnew.add(key.toString());
            }

            for (int j = 0; j < arrayListnew.size(); j++) {
                Contactmodel model = selectedItem.get(arrayListnew.get(j));
                if (model != null) {
                    if (model.getIsRegister() == 0) {
                        isInvite = true;
                        break;
                    }
                }
            }


            for (int i = 0; i < arrayListnew.size(); i++) {

                Contactmodel model = selectedItem.get(arrayListnew.get(i));
                if (model != null) {
                    if (model.getIsRegister() == 1) {
                        if (isInvite) {
                            selectedItem.remove(arrayListnew.get(i));
                        }
                        b = true;
                    }
                }
            }


            if (isInvite) {

                if (b) {
                    Toast.makeText(getActivity(), "The invitation will be send to only non app users", Toast.LENGTH_SHORT).show();
                }

                if (selectedItem.size() > 0) {

                    Utils.saveSelectedItem(getActivity(), selectedItem);
                    homeActivity.actionBarShow();
                    modeCustom.finish();
                    homeActivity.callFragmentWithAddBack(new InviteView(), "InviteView");
                } else {
                    Toast.makeText(getActivity(), "Please select atleast one user for invite!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please select atleast one nonapp users for invite!", Toast.LENGTH_SHORT).show();
            }

        }

        if (mCreateBtn == v) {


            boolean b = false;
            HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();
            hashMap.clear();
            hashMap.putAll(selectedItem);

            ArrayList<String> arrayListnew = new ArrayList<String>();
            for (Object key : selectedItem.keySet()) {
                arrayListnew.add(key.toString());

            }

            boolean isGroupable = false;

            for (int j = 0; j < arrayListnew.size(); j++) {
                Contactmodel model = selectedItem.get(arrayListnew.get(j));
                if (model != null) {
                    if (model.getIsRegister() == 1) {

                        isGroupable = true;
                        break;
                    }
                }
            }

            for (int i = 0; i < arrayListnew.size(); i++) {

                Contactmodel model = selectedItem.get(arrayListnew.get(i));
                if (model != null) {
                    if (model.getIsRegister() == 0) {
                        if (isGroupable) {
                            selectedItem.remove(arrayListnew.get(i));
                        }
                        b = true;
                    }
                }
            }


            if (isGroupable) {

                if (selectedItem.size() > 1) {
                    if (b) {
                        Toast.makeText(getActivity(), "The Group will be created with only app users", Toast.LENGTH_SHORT).show();
                    }
                }

                if (selectedItem.size() > 1) {

                    Utils.saveSelectedItem(getActivity(), selectedItem);

                    homeActivity.actionBarShow();
                    modeCustom.finish();
                    selectedItem = new HashMap<String, Contactmodel>();
                    homeActivity.callFragmentWithAddBack(new CreateGroupChat(), "CreateGroupChat");

                } else {
                    Toast.makeText(getActivity(), "Please select atleast two app users for create group!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please select atleast two app users for create group!", Toast.LENGTH_SHORT).show();
            }

        }

        if (R.id.broadcast == v.getId()) {
            homeActivity.callFragmentWithAddBack( CreateBroadCast.getInstance("contact"), "CreateBroadCast");
        }

        if (R.id.invite == v.getId()) {

            int pos = (Integer) v.getTag();
            ArrayList<Contactmodel> arrayList = chatAdapter.getContactArrayList();
            Contactmodel chatmodel = arrayList.get(pos);
            Bundle data = new Bundle();
            data.putString("remote_jid", chatmodel.getRemote_jid());
            data.putString("remote_jid", chatmodel.getRemote_jid());
            data.putString("remote_name", chatmodel.getName());
            data.putString("custom_status", chatmodel.getCustomStatus());
            data.putByteArray("remote_pic", chatmodel.getAvatar());
            data.putString("status", chatmodel.getStatus());
            data.putInt("isgroup", 0);
            data.putString("mobile_no", chatmodel.getNumber());
            data.putString("total_invite", "1");
            data.putInt("isstranger", chatmodel.getIsStranger());
            data.putInt("isuserblock", chatmodel.getIsUserblock());
            Fragment inviteFrag = new InviteView();
            inviteFrag.setArguments(data);
            homeActivity.callFragmentWithAddBack(inviteFrag, "InviteView");
        }

        if (R.id.group == v.getId()) {
            homeActivity.callFragmentWithAddBack(new CreateGroupChat(), "CreateGroupChat");
        }

        if (v.getId() == R.id.navilayout) {
            int pos = (Integer) v.getTag();
            chatPage(pos);
        }

        if (v.getId() == R.id.profile_image) {
            int pos = (Integer) v.getTag();
            chatPage(pos);
        }

        homeActivity.collapseSearch();
    }


    public void chatPage(int pos) {

        ArrayList<Contactmodel> arrayList = chatAdapter.getContactArrayList();
        Contactmodel chatmodel = arrayList.get(pos);
        Bundle data = new Bundle();
        data.putString("remote_jid", chatmodel.getRemote_jid());
        data.putString("remote_jid", chatmodel.getRemote_jid());
        data.putString("remote_name", chatmodel.getName());
        data.putString("custom_status", chatmodel.getCustomStatus());
        data.putByteArray("remote_pic", chatmodel.getAvatar());
        data.putString("status", chatmodel.getStatus());
        data.putInt("isgroup", 0);
        data.putInt("isstranger", chatmodel.getIsStranger());
        data.putString("phonenumber", chatmodel.getNumber());
        Contactmodel contactmodel = new Contactmodel();
        contactmodel.setRemote_jid(chatmodel.getRemote_jid());
        contactmodel.setName(chatmodel.getName());
        contactmodel.setCustomStatus(chatmodel.getCustomStatus());
        contactmodel.setAvatar(chatmodel.getAvatar());
        contactmodel.setIsGroup(0);
        contactmodel.setIsStranger(chatmodel.getIsStranger());
        Utils.saveContactItem(getActivity(), new NewContactModelForFlag());
        Fragment chatfrag = new SingleChatRoomFrgament();
        chatfrag.setArguments(data);
        homeActivity.callFragmentWithAddBack(chatfrag, ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);
    }


    @Override
    public void onResume() {
        super.onResume();
        GlobalData.OnContactsfrag = true;

    }


    public void onResumeUpdate() {

        GlobalData.OnContactsfrag = true;

        if (chatAdapter.isEmpty()) {
            mDataNotFoundText.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            mDataNotFoundText.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (loadMoreNonAppContactAsyncTask != null) {
            loadMoreNonAppContactAsyncTask.cancel(true);
            loadMoreNonAppContactAsyncTask = null;
        }

        if (appUsersLoadAsyncTask != null) {
            appUsersLoadAsyncTask.cancel(true);
            appUsersLoadAsyncTask = null;
        }
        customDestoryHistory();
    }


    public void customDestoryHistory() {
        Utils.saveSelectedItem(getActivity(), null);
        Utils.printLog("Contacts_frag frag onDestroyView");
        GlobalData.OnContactsfrag = false;
        ConstantFields.HIDE_MENU = 0;
        homeActivity.invalidateOptionMenuItem();
    }

    @SuppressLint("HandlerLeak")
    private void setUserupdateHandler() {
        UserupdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                if (msg.getData().getString("newentry").equalsIgnoreCase("1")) {
                    chatAdapter.notifyDataSetChanged();
                } else {

                    final String remoteid = msg.getData().getString("remoteid");
                    final String updatestatus = msg.getData().getString("updatestatus");
                    final String lastseen = msg.getData().getString("lastseen");

                    for (int i = 0; i < contactlist.size(); i++) {
                        String jid = contactlist.get(i).getRemote_jid();
                        if (jid.equals(remoteid)) {
                            contactlist.get(i).setStatus(updatestatus);
                            contactlist.get(i).setLastseen(lastseen);
                            break;
                        }
                    }
                    chatAdapter.setContactArrayList(contactlist);
                    chatAdapter.notifyDataSetChanged();

                }
            }
        };
    }


    public void setDeleteContacteHandler() {
        deleteContactHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                //final String remoteid = msg.getData().getString("remoteidnew");

                refrsh();
            }
        };
    }


    public void setAddContact() {
        Intent intent = new Intent(getActivity(), sms19.listview.newproject.ContactAdd.class);
        intent.putExtra("froninapp", true);
        startActivityForResult(intent, 301);
    }


    public void appUserList(int isappsuer) {// 2 mean app user only app user
        current_sort = 2;
        contactlist.clear();
        IS_USER_TYPE = 1;
        loadmore_progress.setTag("0");
        progress.setTag(R.id.progress, false);//
        loadmore_progress.setTag(R.id.loadmore_progress, true);//
        getLoadMoreParent(1);

    }

    public void nonAppUserList() {
        current_sort = 2;
        IS_USER_TYPE = 2;
        contactlist.clear();
        loadmore_progress.setTag("0");
        progress.setTag(R.id.progress, true);//
        loadmore_progress.setTag(R.id.loadmore_progress, false);//
        getLoadMoreParent(2);
    }


    public void refrsh() {
        current_sort = 0;
        IS_USER_TYPE = 0;
        contactlist.clear();
        loadmore_progress.setTag("0");
        progress.setTag(R.id.progress, true);//
        loadmore_progress.setTag(R.id.loadmore_progress, true);//
        getLoadMoreParent(0);
        homeActivity.invalidateOptionMenuItem();

    }



    public ArrayList<Contactmodel> sortByNumber(ArrayList<Contactmodel> arrayList1) {


        current_sort = 1;

        if (arrayList1.size() > 0) {
            if (sortbynumber_appuser) {
                sortbynumber_appuser = false;
                Collections.sort(arrayList1, new Contactmodel().new CustomComparatorSortNumber());
            } else {
                sortbynumber_appuser = true;
                Collections.sort(arrayList1, new Contactmodel().new CustomComparatorSortNumberDec());
            }
        }
        return arrayList1;
    }

    public sms19.inapp.msg.adapter.ContactAdapter getChatAdapter() {
        return chatAdapter;
    }


    public static ArrayList<Contactmodel> getContactlist() {
        return contactlist;
    }

    public static void setContactlist(ArrayList<Contactmodel> contactlist1) {
        contactlist.clear();
    }

    public ArrayList<Contactmodel> sortByName(ArrayList<Contactmodel> arrayList1) {
        current_sort = 0;
        if (arrayList1.size() > 0) {

            if (!sortbyname_both_new) {
                sortbyname_both_new = true;
                Collections.sort(arrayList1, new Contactmodel().new CustomComparatorSortByName());
            } else {
                sortbyname_both_new = false;
                Collections.sort(arrayList1, new Contactmodel().new CustomComparatorSortByBeta());
            }
        }
        return arrayList1;

    }

    public void refreshContactAdapter() {
        ContactFragment contactFragment = newInstance("");
        if (contactFragment != null) {
            new Thread(new Runnable() {
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {

                                    refrsh();

                                } catch (Exception exception) {

                                }

                            }
                        });
                    }
                }
            }).start();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Capture ListView item click
        mListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = mListView.getCheckedItemCount();

                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                chatAdapter.toggleSelection(position, null);

                ArrayList<Contactmodel> arrayList = new ArrayList<Contactmodel>();
                arrayList.addAll(chatAdapter.getContactArrayList());

                if (checked) {

                    onListItemSelect(position, null, checked);
                    selectedItem.put(String.valueOf(position), arrayList.get(position));
                } else {

                    onListItemSelect(position, null, checked);
                    selectedItem.remove(String.valueOf(position));


                }

                if (checkedCount == 0) {
                    homeActivity.actionBarShow();
                    homeActivity.getLayoutTab_contact_chat().setVisibility(View.VISIBLE);

                } else {
                    homeActivity.actionBarHide();
                    homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
                }


            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:

                        SparseBooleanArray selected = chatAdapter
                                .getSelectedIds();

                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Contactmodel selecteditem = chatAdapter
                                        .getItem(selected.keyAt(i));

                                chatAdapter.remove(selecteditem);
                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //mode.getMenuInflater().inflate(R.menu.new_selector, menu);
                modeCustom = mode;
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                chatAdapter.unSelectItem();
                homeActivity.actionBarShow();
                homeActivity.getLayoutTab_contact_chat().setVisibility(View.VISIBLE);
                selectedItem = new HashMap<String, Contactmodel>();
                chatAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                return false;
            }
        });


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {


                return true;
            }
        });

    }

    private void onListItemSelect(int position, View view, boolean b) {

        chatAdapter.changeBackgroundColor(view, b, position);

    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GlobalData.OnContactsfrag = false;
    }


    public boolean isSortbyname_both_new() {
        return sortbyname_both_new;
    }


    public int getCurrent_sort() {
        return current_sort;
    }

    public boolean isSortbynumber_appuser() {
        return sortbynumber_appuser;
    }

    public int getIS_USER_TYPE() {
        return IS_USER_TYPE;
    }

}
/*
* public ArrayList<Contactmodel> sortNonAppUser(ArrayList<Contactmodel> arrayList2) {
        current_sort = 2;
        if (arrayList2.size() > 0) {
            if (sortbyname_nonappuser) {
                sortbyname_nonappuser = false;

                Collections.sort(arrayList2, new Contactmodel().new CustomComparatorSortByName());
            } else {
                sortbyname_nonappuser = true;
                Collections.sort(arrayList2, new Contactmodel().new CustomComparatorSortByBeta());
            }
        }
        return arrayList2;

    }


    public ArrayList<Contactmodel> sortByAZBoth(ArrayList<Contactmodel> arrayList1, ArrayList<Contactmodel> arrayList2) {


        ArrayList<Contactmodel> contactlistContactNew = new ArrayList<Contactmodel>();
        if (arrayList1.size() > 0) {

            if (sortbyname_appuser) {
                sortbyname_appuser = false;
                Collections.sort(arrayList1, new Contactmodel().new CustomComparatorSortByName());
            } else {
                sortbyname_appuser = true;
                Collections.sort(arrayList1, new Contactmodel().new CustomComparatorSortByBeta());
            }
        }
        if (arrayList2.size() > 0) {
            if (sortbyname_nonappuser) {
                sortbyname_nonappuser = false;
                Collections.sort(arrayList2, new Contactmodel().new CustomComparatorSortByName());
            } else {
                sortbyname_nonappuser = true;
                Collections.sort(arrayList2, new Contactmodel().new CustomComparatorSortByBeta());
            }
        }
        contactlistContactNew = arrayList1;
        if (contactlistContactNew.size() > 0) {
            if (arrayList1.size() > 0) {
                for (int i = 0; i < arrayList2.size(); i++) {
                    contactlistContactNew.add(arrayList2.get(i));
                }
            } else {
                contactlistContactNew = arrayList2;
            }
        } else {
            contactlistContactNew = arrayList2;
        }


        contactlist = new ArrayList<Contactmodel>();
        contactlist = contactlistContactNew;
        chatAdapter.setContactArrayList(contactlistContactNew);
        chatAdapter.notifyDataSetChanged();

        return contactlist;
    }

    public ArrayList<Contactmodel> sortByZABoth(ArrayList<Contactmodel> arrayList1, ArrayList<Contactmodel> arrayList2) {

        ArrayList<Contactmodel> contactlistContactNew = new ArrayList<Contactmodel>();
        if (arrayList1.size() > 0) {
            Collections.sort(arrayList1, new Contactmodel().new CustomComparatorSortByBeta());
        }
        if (arrayList2.size() > 0) {
            Collections.sort(arrayList2, new Contactmodel().new CustomComparatorSortByBeta());
        }
        contactlistContactNew = arrayList1;
        if (contactlistContactNew.size() > 0) {
            if (arrayList1.size() > 0) {
                for (int i = 0; i < arrayList2.size(); i++) {
                    contactlistContactNew.add(arrayList2.get(i));
                }
            } else {
                contactlistContactNew = arrayList2;
            }
        }
        contactlist = new ArrayList<Contactmodel>();
        contactlist = contactlistContactNew;
        chatAdapter.setContactArrayList(contactlistContactNew);
        chatAdapter.notifyDataSetChanged();
        return contactlist;

    }

    public static ArrayList<Contactmodel> getAppUserList() {
        appUserList = new ArrayList<Contactmodel>();
        appUserList = (GlobalData.dbHelper.getContactfromDBOnlyRegister());
        return appUserList;
    }

    public ArrayList<Contactmodel> getNonAppUser() {

        return nonAppUser;
    }

    public static void setAppUserList(ArrayList<Contactmodel> appUserList) {
        ContactFragment.appUserList = appUserList;
    }

    public static void setNonAppUser(ArrayList<Contactmodel> nonAppUser) {
        ContactFragment.nonAppUser = nonAppUser;
    }

    public void sortByAsenNumber(ArrayList<Contactmodel> arrayList1, ArrayList<Contactmodel> arrayList2) {

        ArrayList<Contactmodel> contactlistContactNew = new ArrayList<Contactmodel>();
        if (arrayList1.size() > 0) {
            if (sortbynumber_appuser) {
                sortbynumber_appuser = false;
                Collections.sort(arrayList1, new Contactmodel().new CustomComparatorSortNumber());
            } else {
                sortbynumber_appuser = true;
                Collections.sort(arrayList1, new Contactmodel().new CustomComparatorSortNumberDec());
            }
        }
        if (arrayList2.size() > 0) {
            if (sortbynumber_nonappuser) {
                sortbynumber_nonappuser = false;
                Collections.sort(arrayList2, new Contactmodel().new CustomComparatorSortNumber());
            } else {
                sortbynumber_nonappuser = true;
                Collections.sort(arrayList2, new Contactmodel().new CustomComparatorSortNumberDec());
            }
        }
        contactlistContactNew = arrayList1;


        if (contactlistContactNew.size() > 0) {
            if (arrayList1.size() > 0) {
                for (int i = 0; i < arrayList2.size(); i++) {
                    contactlistContactNew.add(arrayList2.get(i));
                }
            } else {
                contactlistContactNew = arrayList2;
            }
        } else {
            contactlistContactNew = arrayList2;
        }

        contactlist = new ArrayList<Contactmodel>();
        contactlist = contactlistContactNew;
        chatAdapter.setContactArrayList(contactlistContactNew);// 25jan
        chatAdapter.notifyDataSetChanged();

    }
    public boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
*/