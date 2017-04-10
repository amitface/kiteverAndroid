package sms19.inapp.msg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.MultiUserChat;

import sms19.inapp.msg.adapter.GroupListAdapter;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.ContactUtil;
import sms19.inapp.msg.constant.CustomDialogClass;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.constant.myAffiliate;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.Groupmodel;
import sms19.inapp.msg.model.NewContactModelForFlag;
import sms19.inapp.msg.model.Recentmodel;
import sms19.inapp.msg.rest.GroupChatMyStatusListner;
import sms19.inapp.msg.rest.Groupchatparticipentchange;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;
import sms19.listview.adapter.ChatGroupMemberList;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

public class GroupListFrgament extends Fragment implements OnClickListener {

    private static GroupListFrgament contactFragment;
    private GroupListAdapter chatAdapter = null;
    private ListView mListView;
    private InAppMessageActivity homeActivity = null;
    private LinearLayout clickable;
    private MultiUserChat groupChat = null;
    // private Button addGroup;
    // private LinearLayout bottomLayout;

    /***************
     * xmpp
     **************/
    public static ArrayList<Contactmodel> contactlist;
    public static Handler UserupdateHandler;
    private ArrayList<Groupmodel> recentlist;
    // private LinearLayout mInviteBtn,mCreateBtn,mBroadCastBtn;
    private int LAST_HIDE_MENU = 0;
    private TextView mDataNotFoundText;

    private String deleteRemoteId = "";
    private Recentmodel deleteRecentModel = null;
    private int deleteItempos = -1;
    private PopupWindow popupWindow;
    private CustomDialogClass customDialog = null;
    private ArrayList<Recentmodel> gmemberlist;
    private static HashMap<String, Recentmodel> gmemberhasmap;
    private GetGroupListAsyncTask getGroupListAsyncTask = null;
    private Handler handlerRefreshAdapter = null;
    public static Handler onLineOfflineHandler = null;
    private int index = 0;

    public static GroupListFrgament newInstance(String titleName) {
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
        LAST_HIDE_MENU = ConstantFields.HIDE_MENU;
        ConstantFields.HIDE_MENU = 77;
        homeActivity.invalidateOptionMenuItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contactFragment = this;
        gmemberlist = new ArrayList<Recentmodel>();
        gmemberhasmap = new HashMap<String, Recentmodel>();

        View view = getActivity().getLayoutInflater().inflate(
                R.layout.group_list_view, container, false);

        initiateView(view);
        GlobalData.FilterRecentlist = false;
        setOnLineOfflineHandler();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        chatAdapter = new GroupListAdapter(getActivity(),
                new ArrayList<Groupmodel>());
        chatAdapter.setClickListener(this);
        mListView.setAdapter(chatAdapter);
//        mListView.setOnClickListener(this);
        // AdapterRefreshWithAsync();
        callHandler(true);

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    private class GetGroupListAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                adapterSet2();

            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {

                adapterRefresh();

            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    }

    public void clickAbleTrue() {

        clickable.setClickable(false);

    }

    public void callHandler(boolean bFlag) {
        android.os.Message msg = new android.os.Message();// comment m
        Bundle b = new Bundle();
        b.putBoolean("message", bFlag);
        msg.setData(b);
        clickable.setClickable(false);
        handlerRefreshAdapter.sendMessage(msg);
    }

    private void inItHandlerRefreshAdapter() {
        // TODO Auto-generated method stub
        handlerRefreshAdapter = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                boolean message = msg.getData().getBoolean("message");
                adapterSet2();
                adapterRefresh();

            }
        };
    }

    public void adapterSet2() {

        recentlist = new ArrayList<Groupmodel>();

        ArrayList<Groupmodel> groupmodelsNew = new ArrayList<Groupmodel>();
        groupmodelsNew.addAll((GlobalData.dbHelper
                .getGroupallmemberListfromDB()));
        recentlist.addAll(groupmodelsNew);

        if (recentlist == null) {
            recentlist = new ArrayList<Groupmodel>();
            Utils.printLog("" + recentlist.size());
        }
    }

    public void adapterSet() {

        recentlist = new ArrayList<Groupmodel>();

        ArrayList<Groupmodel> groupmodelsNew = new ArrayList<Groupmodel>();
        groupmodelsNew.addAll((GlobalData.dbHelper
                .getGroupallmemberListfromDB()));
        recentlist.addAll(groupmodelsNew);

        if (recentlist == null) {
            recentlist = new ArrayList<Groupmodel>();
            Utils.printLog("" + recentlist.size());
        }

		/*
         * try { if(recentlist.size()>0){ Collections.sort(recentlist,new
		 * Groupmodel().new CustomComparatorSortByRecentGroup()); } } catch
		 * (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

        adapterRefresh();

    }

    public void adapterRefresh() {
        if (chatAdapter != null) {
            chatAdapter.setRecentArrayList(recentlist);
            chatAdapter.notifyDataSetChanged();

            if (chatAdapter.isEmpty()) {
                mDataNotFoundText.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            } else {
                mDataNotFoundText.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void initiateView(View view) {

        mListView = (ListView) view.findViewById(R.id.list_view);

        // addGroup=(Button)view2.findViewById(R.id.add_group);
        mDataNotFoundText = (TextView) view.findViewById(R.id.no_data_found);

        clickable = (LinearLayout) view
                .findViewById(R.id.clickable_grouplist_item);

        inItHandlerRefreshAdapter();
        clickable.setOnClickListener(this);
        clickable.setClickable(true);
        clickable.setEnabled(true);

    }

    @Override
    public void onClick(View v) {

        homeActivity.collapseSearch();
        int pos;
        Bundle data;
        Groupmodel chatmodel;
        Recentmodel recentmodel;
        Fragment chatfrag;
        switch (v.getId()) {
            case R.id.delete_group:
                pos = (Integer) v.getTag();
                String groupId = recentlist.get(pos).getGroup_jid();
                try {
                    deleteGroupChat(GlobalData.connection, groupId);
                } catch (XMPPException e) {
                    Utils.printLog("delete group failed");
                    e.printStackTrace();
                }
                break;
            case R.id.btn_facebook:
                if (customDialog != null) {
                    customDialog.dismiss();
                }

                popupWindow.dismiss();
                if (!deleteRemoteId.equalsIgnoreCase("")) {
                    clickable.setClickable(true);
                    if (deleteRecentModel.getIsgroup() == 0) {

                        navigateFromDialog(deleteItempos);
                    } else {
                        navigateFromDialog(deleteItempos);
                    }

                }
                break;
            case R.id.btn_twitter:
                if (customDialog != null) {
                    customDialog.dismiss();
                }
                popupWindow.dismiss();
                if (GlobalData.dbHelper.checkGroupiscreatedbyme(recentlist.get(deleteItempos)
                        .getGroup_jid()))
                    showAssignAdminList(recentlist.get(deleteItempos)
                            .getGroup_jid(), InAppMessageActivity.myModel.getRemote_jid());
                else
                 /*   exitGroup(recentlist.get(deleteItempos)
                .getGroup_jid(), InAppMessageActivity.myModel.getRemote_jid());*/
                    exitGroup();
//                    Toast.makeText(getActivity(), "exit group chat", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_email:// delete chat and group delete
                if (customDialog != null) {
                    customDialog.dismiss();
                }
                popupWindow.dismiss();
                if (deleteRecentModel.getIsgroup() == 0) {
                    // deleteChat(deleteRemoteId);
                    homeActivity
                            .deleteBroadCastGroupORChatWithDialog(deleteRemoteId);
                    refreshChatAdapter();

                } else if (deleteRecentModel.getIsgroup() == 1) {
                    if (GlobalData.dbHelper.groupIsBlockNew(deleteRemoteId)) {
                        String groupJId = recentlist.get(deleteItempos).getGroup_jid();

                        String ownerJid = "";
                        try {
                            if (GlobalData.dbHelper.checkGroupiscreatedbyme(groupJId)) {
                                if (gmemberlist != null) {
                                    if (gmemberlist.size() > 0) {
                                        ownerJid = gmemberlist.get(0).getRemote_jid();
                                    }
                                }
                            }
                            try {
                                @SuppressWarnings("unused")
                                boolean isExit = homeActivity.exitGroupWithDialog(
                                        groupJId,
                                        InAppMessageActivity.myModel.getRemote_jid(),
                                        ownerJid, GlobalData.DELETE_TITLE,
                                        GlobalData.DELETE_MESSAGE);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            refreshChatAdapter();

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                        // boolean
                        // iscreatedByMe=GlobalData.dbHelper.checkGroupiscreatedbyme(groupJId);

				/*
                 * if(iscreatedByMe){
				 *
				 * try { deleteGroupWithDialog(groupJId); } catch (XMPPException
				 * e) {
				 *
				 * e.printStackTrace(); } }else{ try {
				 * deleteGroupWithDialogForCreatedMeNo(groupJId,
				 * InAppMessageActivity.myModel.getRemote_jid()); } catch
				 * (XMPPException e) { e.printStackTrace(); }
				 *
				 * }
				 */

                    } else {
                        // deleteChat(deleteRemoteId);
                        Toast.makeText(getActivity(), "First exit the group",
                                Toast.LENGTH_SHORT).show();

                    }
                } else {
                    homeActivity
                            .deleteBroadCastGroupORChatWithDialog(deleteRemoteId);
                    refreshChatAdapter();
                }
                break;
            case R.id.nav2:
                pos = (Integer) v.getTag();
                chatmodel = recentlist.get(pos);
                recentmodel = GlobalData.dbHelper
                        .getsingleContactfromDB(chatmodel.getGroup_jid());
                data = new Bundle();
                data.putString("remote_jid", chatmodel.getGroup_jid());
                data.putString("remote_name", chatmodel.getGroupname());
                data.putString("custom_status", "");
                data.putByteArray("remote_pic", recentmodel.getUserpic());
                data.putInt("isgroup", recentmodel.getIsgroup());
                data.putInt("isstranger", 0);
                data.putInt("isuserblock", 0);
                chatfrag = new SingleChatRoomFrgament();
                chatfrag.setArguments(data);
                homeActivity.callFragmentWithAddBack(chatfrag,
                        ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);
                break;
            case R.id.broadcast:
                pos = (Integer) v.getTag();
                if (pos == 2) {
                    homeActivity.callFragmentWithAddBack(new BroadCastGroupFirst(),
                            "BroadCastGroupFirst");
                } else {
                    homeActivity.callFragmentWithAddBack(
                            new BroadCastGroupSecond(), "BroadCastGroupSecond");
                }
                break;
            case R.id.invite:
                homeActivity
                        .callFragmentWithAddBack(new InviteView(), "InviteView");
                break;
            case R.id.nav_page:
                pos = (Integer) v.getTag();
                chatmodel = recentlist.get(pos);
                recentmodel = GlobalData.dbHelper
                        .getsingleContactfromDB(chatmodel.getGroup_jid());
                data = new Bundle();
                data.putString("remote_jid", chatmodel.getGroup_jid());
                data.putString("remote_name", chatmodel.getGroupname());
                data.putString("custom_status", "");
                data.putByteArray("remote_pic", null);
                data.putInt("isgroup", recentmodel.getIsgroup());
                data.putInt("isstranger", 0);
                data.putInt("isuserblock", 0);
                chatfrag = new SingleChatRoomFrgament();
                chatfrag.setArguments(data);
                homeActivity.callFragmentWithAddBack(chatfrag,
                        ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);
                break;
        }
    }

    private void exitGroup() {
        if (deleteItempos >= 0) {
            if (deleteRecentModel.getIsgroup() == 1) {

                String groupJId = recentlist.get(deleteItempos)
                        .getGroup_jid();
                // exitGroup2(groupJId,
                // InAppMessageActivity.myModel.getRemote_jid());

                String ownerJid = "";

                if (GlobalData.dbHelper.checkGroupiscreatedbyme(groupJId)) {
                    if (gmemberlist != null) {
                        if (gmemberlist.size() > 0) {
                            ownerJid = gmemberlist.get(0).getRemote_jid();
                        }
                    }
                }

                try {

                    try {
                        @SuppressWarnings("unused")
                        boolean isExit = homeActivity.exitGroupWithDialog(
                                groupJId, InAppMessageActivity.myModel
                                        .getRemote_jid(), ownerJid,
                                GlobalData.EXIT_TITLE,
                                GlobalData.EXIT_MESSAGE);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    refreshChatAdapter();

                } catch (Exception e) {

                    e.printStackTrace();
                }

            } else {
                // deleteChat(deleteRemoteId);

                homeActivity
                        .deleteBroadCastGroupORChatWithDialog(deleteRemoteId);
                refreshChatAdapter();

            }
        }
    }

    @Override
    public void onResume() {

        super.onResume();

		/*
         * try { if(memberlist.size()>0){ Collections.sort(memberlist,new
		 * Groupmodel().new CustomComparatorSortByRecent()); } } catch
		 * (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        if (getGroupListAsyncTask != null) {
            getGroupListAsyncTask.cancel(true);
            getGroupListAsyncTask = null;
        }

        customDestoryHistory();

    }

    public void customDestoryHistory() {
        Utils.printLog("Contacts_frag frag onDestroyView");
        GlobalData.OnContactsfrag = false;
        // homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);//31Jancomment
        ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
        homeActivity.invalidateOptionMenuItem();

    }

    @SuppressLint("HandlerLeak")
    @SuppressWarnings("unused")
    private void setUserupdateHandler() {

        UserupdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                if (msg.getData().getString("newentry").equalsIgnoreCase("1")) {
                    chatAdapter.notifyDataSetChanged();
                } else {

                    final String remoteid = msg.getData().getString("remoteid");
                    final String updatestatus = msg.getData().getString(
                            "updatestatus");
                    final String lastseen = msg.getData().getString("lastseen");
                    for (int i = 0; i < contactlist.size(); i++) {
                        String jid = contactlist.get(i).getRemote_jid();
                        if (jid.equals(remoteid)) {
                            contactlist.get(i).setStatus(updatestatus);
                            contactlist.get(i).setLastseen(lastseen);
                            break;
                        }
                    }
                    chatAdapter.notifyDataSetChanged();

                }
            }
        };
    }

    public void refreshChatAdapter() {

        GroupListFrgament frgament = newInstance("");
        if (frgament != null) {
            new Thread(new Runnable() {
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    clickable.setClickable(false);
                                    adapterSet();
                                } catch (Exception exception) {
                                }
                            }
                        });
                    }
                }
            }).start();
        }
    }

    private void deleteGroupChat(final Connection conn, final String groupId)
            throws XMPPException {

        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Group")
                .setMessage("Are you sure you want to exit group?")
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                try {
                                    Utils.printLog("Delete group");
                                    GlobalData.dbHelper
                                            .deleteParticularUserHistory(groupId);
                                    GlobalData.dbHelper
                                            .deleteGroupParticularrow(groupId);
                                    GlobalData.dbHelper
                                            .deleteRecentParticularrow(groupId);
                                    Utils.printLog("delete group success");
                                    ChatFragment chatFragment = ChatFragment
                                            .newInstance("");
                                    if (chatFragment != null) {
                                        chatFragment.refreshChatAdapter();
                                    }
                                    GroupListFrgament frgament = GroupListFrgament
                                            .newInstance("");
                                    if (frgament != null) {
                                        frgament.refreshChatAdapter();
                                    }

                                } catch (Exception e) {

                                    Utils.printLog("delete group failed");
                                    e.printStackTrace();
                                }

                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        }).show();

    }

    public GroupListAdapter getChatAdapter() {
        return chatAdapter;
    }

    public static ArrayList<Contactmodel> getContactlist() {
        return contactlist;
    }

    public ArrayList<Groupmodel> getRecentlist() {
        return recentlist;
    }

    public void Show_Dialog(Recentmodel recentmodel, int pos, String jid) {
        deleteItempos = pos;
        deleteRemoteId = jid;
        deleteRecentModel = recentmodel;
        initiatePopupWindow();
    }

    private void initiatePopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(
                    R.layout.delete_view_contactview_view, null);

            Display display = getActivity().getWindowManager()
                    .getDefaultDisplay();
            int width = display.getWidth();

            popupWindow = new PopupWindow(layout, width - 100,
                    LayoutParams.WRAP_CONTENT, true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Button mViewContact = (Button) layout
                    .findViewById(R.id.btn_facebook);
            Button mViewChat = (Button) layout.findViewById(R.id.btn_twitter);
            Button mDeleteChat = (Button) layout.findViewById(R.id.btn_email);

            if (deleteRecentModel.getIsgroup() == 1) {
                mViewContact.setText("View Group");
                mViewChat.setText("Exit Group");

                setGroupmemberlist(true);

                if (GlobalData.dbHelper.groupIsBlockNew(deleteRemoteId)) {

                    mViewChat.setVisibility(View.GONE);
                }
                mDeleteChat.setText("Delete Group");
            } else if (deleteRecentModel.getIsgroup() == 2) {
                mViewContact.setText("View Group");
                mViewChat.setText("Delete");
                mViewChat.setVisibility(View.GONE);
                mDeleteChat.setText("Delete Group");
            }

            mViewContact.setOnClickListener(this);
            mViewChat.setOnClickListener(this);
            mDeleteChat.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void navigateFromDialog(int poss) {

        int pos = poss;

        Groupmodel chatmodel = recentlist.get(pos);

        // Groupmodel chatmodel=recentlist.get(pos);

        Recentmodel recentmodel = GlobalData.dbHelper
                .getsingleContactfromDB(chatmodel.getGroup_jid());
        Bundle data = new Bundle();
        data.putString("remote_jid", chatmodel.getGroup_jid());
        data.putString("remote_name", chatmodel.getGroupname());
        data.putString("custom_status", "");
        data.putByteArray("remote_pic", recentmodel.getUserpic());
        data.putInt("isgroup", recentmodel.getIsgroup());
        data.putInt("isstranger", 0);
        data.putInt("isuserblock", 0);

        // Bundle data = new Bundle();
        /*
		 * data.putString("remote_jid", chatmodel.getRemote_jid());
		 * data.putString("remote_name", chatmodel.getDisplayname());
		 * data.putString("custom_status", chatmodel.getCustomStatus());
		 * data.putString("status", chatmodel.getStatus());
		 * data.putByteArray("remote_pic", chatmodel.getUserpic());
		 * data.putInt("isgroup", chatmodel.getIsgroup());
		 * data.putInt("isstranger", chatmodel.getIsStranger());
		 * data.putString("phonenumber", chatmodel.getUsernumber());
		 * data.putInt("isuserblock", chatmodel.getIsUserblock());
		 */

        NewContactModelForFlag contactmodel = new NewContactModelForFlag();
		/*
		 * contactmodel.setRemote_jid(chatmodel.getRemote_jid());
		 * contactmodel.setName(chatmodel.getDisplayname());
		 * contactmodel.setCustomStatus(chatmodel.getCustomStatus());
		 * contactmodel.setAvatar(chatmodel.getUserpic());
		 * contactmodel.setIsGroup(chatmodel.getIsgroup());
		 * contactmodel.setIsStranger(chatmodel.getIsStranger());
		 * contactmodel.setStatus(chatmodel.getStatus());
		 */
        contactmodel.setFromPage("1");
        Utils.saveContactItem(getActivity(), contactmodel);

        if (recentmodel.getIsgroup() == 0) {
            homeActivity.getActionbar_title().setVisibility(View.GONE);
            homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
            Fragment chatfrag = new SigleUserViewProfile();
            chatfrag.setArguments(data);
            homeActivity.callFragmentWithAddBack(chatfrag,
                    "SigleUserViewProfile");
        } else if (recentmodel.getIsgroup() == 1) {
            Fragment chatfrag = new GroupProfile();
            chatfrag.setArguments(data);
            homeActivity.getActionbar_title().setVisibility(View.GONE);
            homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
            homeActivity.callFragmentWithAddBack(chatfrag, "GroupProfile");
        } else if (recentmodel.getIsgroup() == 2) {
            Fragment chatfrag = new SingleChatRoomFrgament();
            chatfrag.setArguments(data);
            homeActivity.getActionbar_title().setVisibility(View.GONE);
            homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
            homeActivity.callFragmentWithAddBack(chatfrag,
                    ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);
        }

    }


    public MultiUserChat initeGroupChat(String groip_jid) {
        MultiUserChat muc = null;
        if (GlobalData.connection != null
                && GlobalData.connection.isConnected()) {
            muc = new MultiUserChat(GlobalData.connection, groip_jid);
            try {
                muc.join(groip_jid);
            } catch (XMPPException e) {

                e.printStackTrace();
            }
        }

        return muc;

    }

    public void getnewMembers(String remote_jid) {
        if (GlobalData.connection != null
                && GlobalData.connection.isConnected()) {

            // MultiUserChat groupChat=initeGroupChat(remote_jid);

            MultiUserChat groupChat = null;
            if (!ContactUtil.mucChatIs(remote_jid)) {
                groupChat = initeGroupChat(remote_jid);
                GlobalData.globalMucChat.put(remote_jid, groupChat);
            } else {
                groupChat = GlobalData.globalMucChat.get(remote_jid);
                if (!groupChat.isJoined()) {
                    groupChat = initeGroupChat(remote_jid);
                }
            }

            ArrayList<Contactmodel> selectedmemberlist = new ArrayList<Contactmodel>();
            try {
                Collection<Affiliate> roomUsersOwner = groupChat.getOwners();

                Collection<myAffiliate> roomUsers = Utils.getAffiliatesByAdmin(
                        "admin", remote_jid);

                if (roomUsers.size() > 0) {

                    Iterator<myAffiliate> ids = roomUsers.iterator();

                    while (ids.hasNext()) {
                        String id = ids.next().getJid();
                        if (!id.equals(InAppMessageActivity.myModel
                                .getRemote_jid())) {

                            Contactmodel model = new Contactmodel();
                            model.setRemote_jid(id);
                            if (GlobalData.dbHelper.groupUserIsBlock(id)) {

                            } else {
                                selectedmemberlist.add(model);
                            }
                        }
                    }
                }

                if (roomUsersOwner.size() > 0) {
                    Iterator<Affiliate> ids = roomUsersOwner.iterator();
                    while (ids.hasNext()) {
                        String id = ids.next().getJid();
                        if (!id.equals(InAppMessageActivity.myModel
                                .getRemote_jid())) {
                            Contactmodel model = new Contactmodel();
                            model.setRemote_jid(id);
                            model.setIsAdmin(1);
                            selectedmemberlist.add(model);
                            GlobalData.dbHelper
                                    .updateGroupCreatedByMe(remote_jid);

                        } else {
                            GlobalData.dbHelper
                                    .groupCreatedByMeUpdateDB(remote_jid);
                        }
                    }
                }

                if (selectedmemberlist.size() > 0) {
                    Utils.printLog("new member added in group...");
					/*
					 * GlobalData.dbHelper.editGroupinDB(remote_jid,
					 * selectedmemberlist);
					 */
                    GlobalData.dbHelper.editGroupinDBNew(remote_jid,
                            selectedmemberlist);
                    final ArrayList<Contactmodel> filtermembers = GlobalData.dbHelper
                            .editContactforGroup(selectedmemberlist);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Utils.printLog("new member added in group adapter set...");
                            setGroupmemberlist(false);
                        }
                    });
                    new Thread(new Runnable() {
                        public void run() {
                            Utils.printLog("new member vcard in group statrt...");

                            for (int i = 0; i < filtermembers.size(); i++) {
                                GlobalData.dbHelper
                                        .DownloadVcardandupdateContact(filtermembers
                                                .get(i).getRemote_jid());
                            }
                            Utils.printLog("new member vacrd in group complete...");
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setGroupmemberlist(boolean loadmember) {


        Log.i("PageTYpe", "group list - " + GlobalData.PAGE_GROUP_TYPE);
        final String groip_jid = deleteRemoteId;
        try {
            if (gmemberlist.size() > 0) {
                gmemberlist.clear();
            }
            ArrayList<Recentmodel> temp = GlobalData.dbHelper
                    .getGroupmemberListfromDB(groip_jid);

            if (temp != null && temp.size() > 0) {
                gmemberhasmap = new HashMap<String, Recentmodel>();
                gmemberlist.addAll(temp);

                String name = "";
                Utils.printLog("gmemberlist size=" + gmemberlist.size());
                for (int i = 0; i < gmemberlist.size(); i++) {

                    gmemberhasmap.put(gmemberlist.get(i).getRemote_jid(),
                            gmemberlist.get(i));
                }
                if (gmemberlist.size() > 0) {
                    //name = Utils.getUsersNameWithGroupName(gmemberlist);
                    name = "Hello";
                    homeActivity.getmUserStatusTitle().setText(name);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            if (loadmember) {
                new Thread(new Runnable() {
                    public void run() {
                        if (!GlobalData.dbHelper.groupIsBlockNew(groip_jid)) {
                            getnewMembers(groip_jid);
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setOnLineOfflineHandler() {
        onLineOfflineHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {

                super.handleMessage(msg);
                if (getActivity() != null) {
                    String remote_jid = msg.getData().getString("remote_jid");
                    try {
                        sms19.inapp.msg.model.Contactmodel contactmodel = null;
                        try {
                            contactmodel = GlobalData.dbHelper
                                    .getCustomStatus(remote_jid);
                        } catch (Exception e) {
                            contactmodel = null;
                            e.printStackTrace();
                        }
                        if (contactmodel != null) {

                            ArrayList<Groupmodel> arrayList = chatAdapter
                                    .getGroupBeanArrayList();
                            if (arrayList != null) {
                                for (int i = 0; i < arrayList.size(); i++) {
                                    String jid = arrayList.get(i)
                                            .getGroup_jid();
                                    if (jid.equals(remote_jid)) {
                                        arrayList.get(i).setGroupname(
                                                contactmodel.getName());
                                        arrayList.get(i).setUser_pic(
                                                contactmodel.getAvatar());
                                        break;
                                    }
                                }
                                chatAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        };
    }

    private void showAssignAdminList(final String groupJId, final String jid) {

        final Dialog dialog = new Dialog(homeActivity);
        dialog.setContentView(R.layout.exit_chat_grp_popup);
        dialog.setTitle("Group Exit confirmation");

        final Spinner memberListSpinner = (Spinner) dialog
                .findViewById(R.id.member_list);
        Button no = (Button) dialog.findViewById(R.id.no_id);
        Button yes = (Button) dialog.findViewById(R.id.yes_id);
        ChatGroupMemberList adapter = new ChatGroupMemberList(gmemberlist);
        memberListSpinner.setAdapter(adapter);
        memberListSpinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        index = 0;
                        index = arg2;
                        memberListSpinner.setSelection(index);

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });
        no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                exitGroup(groupJId, jid, index);
                groupChat = new MultiUserChat(GlobalData.connection, recentlist.get(deleteItempos)
                        .getGroup_jid());
                try {
                    if (gmemberlist != null) {
                        if (gmemberlist.size() > 0) {
                            groupChat.grantOwnership(gmemberlist.get(index)
                                    .getRemote_jid());
                            groupChat.grantAdmin(gmemberlist.get(index)
                                    .getRemote_jid());
                        }
                    }
                } catch (XMPPException e) {
                    // TODO Auto-generated catch block
                    Utils.printLog("delete group failed");
                    e.printStackTrace();
                }

                if (deleteItempos >= 0) {
                    if (deleteRecentModel.getIsgroup() == 1) {
                        String groupJId = recentlist.get(deleteItempos)
                                .getGroup_jid();
                        // exitGroup2(groupJId,
                        // InAppMessageActivity.myModel.getRemote_jid());

                        String ownerJid = "";

                        if (GlobalData.dbHelper.checkGroupiscreatedbyme(groupJId)) {
                            if (gmemberlist != null) {
                                if (gmemberlist.size() > 0) {
                                    ownerJid = gmemberlist.get(0).getRemote_jid();
                                }
                            }
                        }

                        try {
                            try {
                                @SuppressWarnings("unused")
                                boolean isExit = homeActivity.exitGroupWithDialog(
                                        groupJId, InAppMessageActivity.myModel
                                                .getRemote_jid(), ownerJid,
                                        GlobalData.EXIT_TITLE,
                                        GlobalData.EXIT_MESSAGE);
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                            refreshChatAdapter();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // deleteChat(deleteRemoteId);
                        homeActivity
                                .deleteBroadCastGroupORChatWithDialog(deleteRemoteId);
                        refreshChatAdapter();
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
/*
         * if(mCreateBtn==v){ homeActivity.callFragmentWithAddBack(new
		 * CreateGroupChat(), "CreateGroupChat"); }
		 *
		 *
		 * if(mBroadCastBtn==v){ homeActivity.callFragmentWithAddBack(new
		 * BroadCastGroupSecond(), "BroadCastGroupSecond"); }
		 public void navigateSingleChatUser(int poss) {

        int pos = poss;

        Groupmodel chatmodel = recentlist.get(pos);

        // Groupmodel chatmodel=recentlist.get(pos);
        Recentmodel recentmodel = GlobalData.dbHelper
                .getsingleContactfromDB(chatmodel.getGroup_jid());
        Bundle data = new Bundle();
        data.putString("remote_jid", chatmodel.getGroup_jid());
        data.putString("remote_name", chatmodel.getGroupname());
        data.putString("custom_status", "");
        data.putByteArray("remote_pic", recentmodel.getUserpic());
        data.putInt("isgroup", recentmodel.getIsgroup());
        data.putInt("isstranger", 0);
        data.putInt("isuserblock", 0);

        NewContactModelForFlag contactmodel = new NewContactModelForFlag();
		/*
		 * contactmodel.setRemote_jid(chatmodel.getRemote_jid());
		 * contactmodel.setName(chatmodel.getDisplayname());
		 * contactmodel.setCustomStatus(chatmodel.getCustomStatus());
		 * contactmodel.setAvatar(chatmodel.getUserpic());
		 * contactmodel.setIsGroup(chatmodel.getIsgroup());
		 * contactmodel.setIsStranger(chatmodel.getIsStranger());
		 * contactmodel.setStatus(chatmodel.getStatus());

Utils.saveContactItem(getActivity(), contactmodel);

        Fragment chatfrag = new SingleChatRoomFrgament();
        chatfrag.setArguments(data);
        homeActivity.callFragmentWithAddBack(chatfrag,
        ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);

        }*/

/*
	 * public void exitGroup2(String groupJId,String jid) {
	 * Log.i("XMPP Chat Client", "User left chat room ");
	 *
	 *
	 *
	 * MultiUserChat groupChat=null; if(!ContactUtil.mucChatIs(groupJId)){
	 * groupChat=initeGroupChat(groupJId);
	 * GlobalData.globalMucChat.put(groupJId, groupChat); }else{
	 * groupChat=GlobalData.globalMucChat.get(groupJId); }
	 *
	 *
	 * if (Utils.isDeviceOnline(getActivity())) {
	 *
	 * if(GlobalData.connection!=null&&GlobalData.connection.isConnected()){
	 *
	 * if(groupChat != null && groupChat.isJoined()) { try {
	 *
	 *
	 *
	 * if(GlobalData.dbHelper.checkGroupiscreatedbyme(groupJId)){
	 *
	 * if(gmemberlist!=null){ if(gmemberlist.size()>0){
	 * groupChat.grantOwnership(gmemberlist.get(0).getRemote_jid());
	 * GlobalData.dbHelper.updateGroupCreatedByMe(groupJId); } }
	 *
	 *
	 * }
	 *
	 *
	 * GlobalData.dbHelper.updateGroupCreatedByMe(groupJId);
	 * GlobalData.dbHelper.singleGroupContactBlockfromDB(jid);
	 * GlobalData.dbHelper.groupBlocknewfromDB(groupJId);
	 *
	 * recentlist.clear();
	 *
	 *
	 * ArrayList<Groupmodel> groupmodelsNew=new ArrayList<Groupmodel>();
	 * groupmodelsNew
	 * .addAll((GlobalData.dbHelper.getGroupallmemberListfromDB()));
	 *
	 *
	 * recentlist.addAll(groupmodelsNew);
	 * //chatAdapter.setRecentArrayList(recentlist);
	 * chatAdapter.notifyDataSetChanged();
	 *
	 *
	 * try { groupChat.banUser(jid,""); groupChat.kickParticipant(jid,
	 * "delete group"); groupChat.leave();
	 *
	 *
	 *
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); try { groupChat.leave(); } catch (Exception e1) { //
	 * TODO Auto-generated catch block e1.printStackTrace(); }
	 *
	 *
	 * }
	 *
	 *
	 *
	 * } catch (Exception e) { e.printStackTrace(); } }else{
	 * Toast.makeText(getActivity(),
	 * "Not connected to server",Toast.LENGTH_SHORT).show(); }} else {
	 * Toast.makeText(getActivity(),
	 * "Check your network connection",Toast.LENGTH_SHORT).show(); }
	 *
	 * }
	 *
	 * }
	 */

	/*
	 * public void exitGroup(String groupJId,String jid) {
	 * Log.i("XMPP Chat Client", "User left chat room ");
	 *
	 * // MultiUserChat groupChat=initeGroupChat(groupJId);
	 *
	 * MultiUserChat groupChat=null; if(!ContactUtil.mucChatIs(groupJId)){
	 * groupChat=initeGroupChat(groupJId);
	 * GlobalData.globalMucChat.put(groupJId, groupChat); }else{
	 * groupChat=GlobalData.globalMucChat.get(groupJId);
	 * if(!groupChat.isJoined()){ groupChat=initeGroupChat(groupJId); } }
	 *
	 * if (Utils.isDeviceOnline(getActivity())) {
	 *
	 * if(GlobalData.connection!=null&&GlobalData.connection.isConnected()){
	 *
	 * if(groupChat != null && groupChat.isJoined()) { try {
	 *
	 *
	 *
	 *
	 * if(GlobalData.dbHelper.checkGroupiscreatedbyme(groupJId)){
	 *
	 * if(gmemberlist!=null){ if(gmemberlist.size()>0){
	 * groupChat.grantOwnership(gmemberlist.get(0).getRemote_jid());
	 * GlobalData.dbHelper.updateGroupCreatedByMe(groupJId); } }
	 *
	 *
	 * }
	 *
	 *
	 *
	 * GlobalData.dbHelper.updateGroupCreatedByMe(groupJId);
	 * GlobalData.dbHelper.singleGroupContactBlockfromDB(jid);
	 * GlobalData.dbHelper.deleteParticularUserHistory(groupJId) ;
	 * GlobalData.dbHelper.groupBlocknewfromDB(groupJId);
	 * GlobalData.dbHelper.deleteGroupParticularrow(groupJId);
	 * GlobalData.dbHelper.deleteRecentParticularrow(groupJId);
	 *
	 *
	 *
	 *
	 *
	 * recentlist.clear();
	 *
	 *
	 * ArrayList<Groupmodel> groupmodelsNew=new ArrayList<Groupmodel>();
	 * groupmodelsNew
	 * .addAll((GlobalData.dbHelper.getGroupallmemberListfromDB()));
	 *
	 * recentlist.addAll(groupmodelsNew);
	 *
	 *
	 * chatAdapter.notifyDataSetChanged();
	 *
	 *
	 * groupChat.banUser(jid,""); groupChat.kickParticipant(jid,
	 * "delete group"); groupChat.leave();
	 *
	 *
	 *
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); try { groupChat.leave(); } catch (Exception e1) { //
	 * TODO Auto-generated catch block e1.printStackTrace(); }
	 *
	 *
	 * }
	 *
	 *
	 *
	 * }else{ GlobalData.dbHelper.updateGroupCreatedByMe(groupJId);
	 * GlobalData.dbHelper.singleGroupContactBlockfromDB(jid);
	 * GlobalData.dbHelper.deleteParticularUserHistory(groupJId) ;
	 * GlobalData.dbHelper.deleteGroupParticularrow(groupJId);
	 * GlobalData.dbHelper.deleteRecentParticularrow(groupJId);
	 *
	 *
	 *
	 *
	 *
	 * recentlist.clear(); ArrayList<Groupmodel> groupmodelsNew=new
	 * ArrayList<Groupmodel>();
	 * groupmodelsNew.addAll((GlobalData.dbHelper.getGroupallmemberListfromDB
	 * ()));
	 *
	 * recentlist.addAll(groupmodelsNew);
	 *
	 * chatAdapter.notifyDataSetChanged();
	 *
	 * }
	 *
	 * } else { Toast.makeText(getActivity(),
	 * "Not connected to server",Toast.LENGTH_SHORT).show(); }
	 *
	 * }else { Toast.makeText(getActivity(),
	 * "Check your network connection",Toast.LENGTH_SHORT).show(); }
	 *
	 * }
	 */

	/*
	 * public void deleteGroupWithDialog(final String groupId) throws
	 * XMPPException {
	 *
	 *
	 *
	 * new AlertDialog.Builder(getActivity()) .setTitle("Delete Group")
	 * .setMessage("Confirmation of group delete?")
	 * .setNegativeButton(android.R.string.no"No", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int which) { // do nothing } })
	 * .setPositiveButton(android.R.string.yes"Yes", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int which) { try { GroupDeleteCreatedByMe(groupId);
	 *
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * Utils.printLog("delete group failed"); e.printStackTrace(); }
	 *
	 *
	 * } }) .show();
	 *
	 *
	 *
	 *
	 *
	 * }
	 */

	/*
	 * public void deleteGroupWithDialogForCreatedMeNo(final String
	 * groupId,final String jid) throws XMPPException {
	 *
	 *
	 *
	 * new AlertDialog.Builder(getActivity()) .setTitle("Delete Group")
	 * .setMessage("Confirmation of group delete?")
	 * .setNegativeButton(android.R.string.no"No", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int which) { // do nothing } })
	 * .setPositiveButton(android.R.string.yes"Yes", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int which) { try { exitGroup(groupId, jid);
	 *
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * Utils.printLog("delete group failed"); e.printStackTrace(); }
	 *
	 *
	 * } }) .show();
	 *
	 *
	 *
	 *
	 *
	 * }
	 */

	/*
	 * public void GroupDeleteCreatedByMe(String groupId){
	 *
	 * try {
	 *
	 * if (Utils.isDeviceOnline(getActivity())) { MultiUserChat muc=null;
	 * if(GlobalData.connection!=null&&GlobalData.connection.isConnected()){
	 * Utils.printLog("Delete group"); try {
	 *
	 *
	 * muc = initeGroupChat(groupId);
	 *
	 *
	 * if(GlobalData.dbHelper.checkGroupiscreatedbyme(groupId)){
	 *
	 * if(gmemberlist!=null){ if(gmemberlist.size()>0){
	 * muc.grantOwnership(gmemberlist.get(0).getRemote_jid());
	 * GlobalData.dbHelper.updateGroupCreatedByMe(groupId); } }
	 *
	 *
	 * }
	 *
	 *
	 * GlobalData.dbHelper.deleteParticularUserHistory(groupId) ;
	 * GlobalData.dbHelper.deleteGroupParticularrow(groupId);
	 * GlobalData.dbHelper.deleteRecentParticularrow(groupId);
	 *
	 *
	 *
	 * recentlist.clear(); ArrayList<Groupmodel> groupmodelsNew=new
	 * ArrayList<Groupmodel>();
	 * groupmodelsNew.addAll((GlobalData.dbHelper.getGroupallmemberListfromDB
	 * ()));
	 *
	 * recentlist.addAll(groupmodelsNew);
	 *
	 * chatAdapter.notifyDataSetChanged();
	 *
	 *
	 *
	 *
	 * muc.banUser(InAppMessageActivity.myModel.getRemote_jid(),"");
	 * muc.kickParticipant(InAppMessageActivity.myModel.getRemote_jid(),
	 * "delete group"); muc.leave(); // muc.join(groupId);
	 * //muc.destroy("was group room", null); } catch (Exception e) { // TODO
	 * Auto-generated catch block Utils.printLog("delete group failed");
	 * if(muc!=null){ muc.leave(); } e.printStackTrace(); }
	 *
	 *
	 *
	 *
	 * Utils.printLog("delete group success"); }else{
	 * Toast.makeText(getActivity(),
	 * "Not connected to server",Toast.LENGTH_SHORT).show(); }} else {
	 * Toast.makeText(getActivity(),
	 * "Check your network connection",Toast.LENGTH_SHORT).show(); }
	 *
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * Utils.printLog("delete group failed"); e.printStackTrace(); } }
	 */

	/*
	 * private boolean sortByRecent=false; public void sortByRecent(){
	 *
	 * if(recentlist!=null){ if(recentlist.size()>0){ if(sortByRecent){
	 * sortByRecent=false; Collections.sort(recentlist,new Recentmodel().new
	 * CustomComparatorSortByRecent()); }else{ sortByRecent=true;
	 * Collections.sort(recentlist,new Recentmodel().new
	 * CustomComparatorSortByRecentDec()); }
	 * chatAdapter.setRecentArrayList(recentlist);
	 * chatAdapter.notifyDataSetChanged(); } }
	 *
	 *
	 * }
	 *
	 *  public void deleteChat(String remotejid) {
        GlobalData.dbHelper.deleteParticularUserHistory(remotejid);
        GlobalData.dbHelper.deleteRecentParticularrow(remotejid);
        GlobalData.dbHelper.deleteGroupParticularrow(remotejid);
        GlobalData.dbHelper.updateContactmsgData(remotejid, "", "");
        recentlist.clear();

        ArrayList<Recentmodel> arrayList = new ArrayList<Recentmodel>();
        ArrayList<Groupmodel> groupmodelsNew = new ArrayList<Groupmodel>();
        groupmodelsNew.addAll((GlobalData.dbHelper
                .getGroupallmemberListfromDB()));

        recentlist.addAll(groupmodelsNew);

        // recentlist.addAll(GlobalData.dbHelper.getRecentHistoryfromDb());
        chatAdapter.notifyDataSetChanged();
    }

     public void AdapterRefreshWithAsync() {
        if (getGroupListAsyncTask == null) {
            getGroupListAsyncTask = new GetGroupListAsyncTask();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getGroupListAsyncTask
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                getGroupListAsyncTask.execute();
            }

        } else {
            getGroupListAsyncTask.cancel(true);
            getGroupListAsyncTask = null;
            getGroupListAsyncTask = new GetGroupListAsyncTask();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getGroupListAsyncTask
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                getGroupListAsyncTask.execute();
            }
        }
    }
	 */