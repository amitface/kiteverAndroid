package sms19.inapp.msg;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import sms19.inapp.msg.adapter.ChatAdapter;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.CustomDialogClass;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.constant.myAffiliate;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.NewContactModelForFlag;
import sms19.inapp.msg.model.Recentmodel;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;
import sms19.listview.adapter.ChatGroupMemberList;

public class ChatFragment extends Fragment implements OnClickListener{

    private static ChatFragment chatFragment;
    private ChatAdapter chatAdapter = null;
    private ListView mListView;
    private InAppMessageActivity homeActivity = null;
    private TextView mDataNotFoundText;
    public static Handler RecentupdateHandler;
    public static Handler UpdateUserImageAndName;

    /**************
     * chat
     ****************/

    private static ArrayList<Recentmodel> recentlist;
    private String deleteRemoteId = "";
    private Recentmodel deleteRecentModel = null;
    private int deleteItempos = -1;
    private PopupWindow popupWindow;
    private CustomDialogClass customDialog = null;
    ArrayList<Recentmodel> gmemberlist;
    private static HashMap<String, Recentmodel> gmemberhasmap;
    private int LAST_HIDE_MENU = 0;

    public static Handler UserupStatusHandler;
    public static Handler UserUpdateImageHandler;
    private Handler onLineOfflineHandlerChat;
    private LinearLayout clickable;
    private int current_sort = 1;
    private BroadcastReceiver receiver;
    private int index;
    private MultiUserChat groupChat;

    public static ChatFragment newInstance(String titleName) {

        return chatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        customonCreate();
    }

    public void customonCreate() {
        GlobalData.OnHomefrag = true;
        homeActivity = (InAppMessageActivity) getActivity();
        if (homeActivity.getLayoutTab_contact_chat() != null) {
            homeActivity.getLayoutTab_contact_chat()
                    .setVisibility(View.VISIBLE);
            homeActivity.getLayout_name_status().setVisibility(View.GONE);
            homeActivity.getmActionBarImage().setVisibility(View.GONE);
        }

        ConstantFlag.TAB_BACK_HANDLE_FRAGMENT = "";
        LAST_HIDE_MENU = ConstantFields.HIDE_MENU;
        ConstantFields.HIDE_MENU = 0;
        homeActivity.invalidateOptionMenuItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        chatFragment = this;
        gmemberlist = new ArrayList<Recentmodel>();
        gmemberhasmap = new HashMap<String, Recentmodel>();
        View view = getActivity().getLayoutInflater().inflate(
                R.layout.chat_fragment_view, container, false);

        initiateView(view);
        setRecentupdateHandler();
        UpdateImageRecentupdateHandler();
        setUserupdateHandler();
        setUserUpdateImageHandler();
        return view;
    }

    public void initiateView(View view) {

        mListView = (ListView) view.findViewById(R.id.list_view);
        mDataNotFoundText = (TextView) view.findViewById(R.id.no_data_found);

        clickable = (LinearLayout) view.findViewById(R.id.clickable);
        recentlist = new ArrayList<Recentmodel>();
        chatAdapter = new ChatAdapter(getActivity(), this, recentlist);
//        chatAdapter.setClickListener(this);
        mListView.setAdapter(chatAdapter);

        clickable.setOnClickListener(this);
        clickable.setClickable(false);
        if (isNetworkAvailable()) {
            homeActivity.getmUserStatusTitle()
                    .setText("Online");
        } else
            homeActivity.getmUserStatusTitle()
                    .setText("offline");
    }

    @Override
    public void onClick(View v) {
        int pos;
        switch (v.getId()) {
            case R.id.rc_userpic:
                pos = (Integer) v.getTag();

                navigateSingleChatUser(pos);
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
                        .getRemote_jid()))
                    showAssignAdminList(recentlist.get(deleteItempos)
                            .getRemote_jid(), InAppMessageActivity.myModel.getRemote_jid());
                else
                    exitGroup();
                break;

            case R.id.btn_email:// delete chat and group delete
                if (customDialog != null) {
                    customDialog.dismiss();
                }
                popupWindow.dismiss();
                if (deleteRecentModel.getIsgroup() == 0) {
                    homeActivity
                            .deleteBroadCastGroupORChatWithDialog(deleteRemoteId);

                } else if (deleteRecentModel.getIsgroup() == 1) {
                    if (GlobalData.dbHelper.groupIsBlockNew(deleteRemoteId)) {
                        String groupJId = recentlist.get(deleteItempos).getRemote_jid();

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


                    } else {
                        // deleteChat(deleteRemoteId);
                        Toast.makeText(getActivity(), "First exit the group",
                                Toast.LENGTH_SHORT).show();

                    }
                /*else if (deleteRecentModel.getIsgroup() == 1) {
                    String groupJId = recentlist.get(deleteItempos).getRemote_jid();
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
                        updateChatAdapter();
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }*/
                } else {
                    homeActivity
                            .deleteBroadCastGroupORChatWithDialog(deleteRemoteId);
                    updateChatAdapter();
                }
                break;

            case R.id.rc_msg:
                pos = (Integer) v.getTag();
                navigateSingleChatUser(pos);
                break;
            case R.id.topLayout:
                pos = (Integer) v.getTag();
                navigateSingleChatUser(pos);
                break;
            case R.id.topLayout2:
                pos = (Integer) v.getTag();
                navigateSingleChatUser(pos);
                break;
            case R.id.rc_usernametext:
                pos = (Integer) v.getTag();
                navigateSingleChatUser(pos);
                break;
        }
        homeActivity.collapseSearch();
    }


    public void navigateSingleChatUser(int poss) {

        homeActivity.getSorting_title().setVisibility(View.GONE);
        int pos = poss;

        // Recentmodel chatmodel=recentlist.get(pos);
        Recentmodel chatmodel = chatAdapter.getChatArrayList().get(pos);

        Bundle data = new Bundle();
        data.putString("remote_jid", chatmodel.getRemote_jid());
        data.putString("remote_name", chatmodel.getDisplayname());
        data.putString("custom_status", chatmodel.getCustomStatus());
        data.putString("status", chatmodel.getStatus());
        data.putByteArray("remote_pic", chatmodel.getUserpic());
        data.putInt("isgroup", chatmodel.getIsgroup());
        data.putInt("isstranger", chatmodel.getIsStranger());
        data.putString("phonenumber", chatmodel.getUsernumber());
        data.putInt("isuserblock", chatmodel.getIsUserblock());

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                final NewContactModelForFlag contactmodel2 = new NewContactModelForFlag();
                Utils.saveContactItem(getActivity(), contactmodel2);
            }
        }).start();

        homeActivity.getSorting_title().setVisibility(View.GONE);

        Fragment chatfrag = new SingleChatRoomFrgament();
        chatfrag.setArguments(data);
        homeActivity.callFragmentWithAddBack(chatfrag,
                ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);

    }

    public void navigateFromDialog(int poss) {
        homeActivity.getSorting_title().setVisibility(View.GONE);

        int pos = poss;

        chatAdapter.setIsClickViewProfile(-111);
        chatAdapter.notifyDataSetChanged();

        Recentmodel chatmodel = chatAdapter.getChatArrayList().get(pos);

        Bundle data = new Bundle();
        data.putString("remote_jid", chatmodel.getRemote_jid());
        data.putString("remote_name", chatmodel.getDisplayname());
        data.putString("custom_status", chatmodel.getCustomStatus());
        data.putString("status", chatmodel.getStatus());
        data.putByteArray("remote_pic", chatmodel.getUserpic());
        data.putInt("isgroup", chatmodel.getIsgroup());
        data.putInt("isstranger", chatmodel.getIsStranger());
        data.putString("phonenumber", chatmodel.getUsernumber());
        data.putInt("isuserblock", chatmodel.getIsUserblock());

        new Thread(new Runnable() {

            @Override
            public void run() {
                final NewContactModelForFlag contactmodel2 = new NewContactModelForFlag();
                contactmodel2.setFromPage("1");
                Utils.saveContactItem(getActivity(), contactmodel2);
            }
        }).start();

        if (chatmodel.getIsgroup() == 0) {
            homeActivity.getActionbar_title().setVisibility(View.GONE);
            homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
            Fragment chatfrag = new SigleUserViewProfile();
            chatfrag.setArguments(data);
            homeActivity.callFragmentWithAddBack(chatfrag,
                    "SigleUserViewProfile");
        } else if (chatmodel.getIsgroup() == 1) {
            Fragment chatfrag = new GroupProfile();
            chatfrag.setArguments(data);
            homeActivity.getActionbar_title().setVisibility(View.GONE);
            homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
            homeActivity.callFragmentWithAddBack(chatfrag, "GroupProfile");
        } else if (chatmodel.getIsgroup() == 2) {

            Fragment chatfrag = new BroadCastGroupProfile();
            chatfrag.setArguments(data);
            homeActivity.getActionbar_title().setVisibility(View.GONE);
            homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
            homeActivity.callFragmentWithAddBack(chatfrag,
                    ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customDestoryHistory();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void customDestoryHistory() {
        GlobalData.OnHomefrag = false;
        ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {

        super.onResume();
        GlobalData.OnHomefrag = true;
        updateChatAdapter();
    }

    public void updateChatAdapter() {

		/*
         * current_sort=1; sortByRecent=true;
		 */
        ArrayList<Recentmodel> arrayList = new ArrayList<Recentmodel>();
        arrayList.addAll(GlobalData.dbHelper.getRecentHistoryfromDb());

        recentlist = arrayList;
        if (recentlist != null) {
            if (recentlist != null && recentlist.size() > 0) {
                chatAdapter.setRecentArrayList(recentlist);
//                chatAdapter.notifyDataSetChanged();
                mDataNotFoundText.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            } else {
                mDataNotFoundText.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);

            }
        } else {
            mDataNotFoundText.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getChildFragmentManager()
                .findFragmentByTag(ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void refreshChatAdapter() {

        current_sort = 1;
        sortByRecent = false;

        ChatFragment chatRoomFrgament = newInstance("");
        if (chatRoomFrgament != null) {
            new Thread(new Runnable() {
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {

                                    homeActivity.invalidateOptionMenuItem();

                                    clickable.setClickable(false);
                                    updateChatAdapter();

                                    if (ConstantFields.HIDE_MENU == 0) {
                                        if (homeActivity
                                                .getLayoutTab_contact_chat()
                                                .getVisibility() != View.VISIBLE) {
                                            homeActivity
                                                    .getLayoutTab_contact_chat()
                                                    .setVisibility(View.VISIBLE);
                                        }

                                    }

                                } catch (Exception exception) {
                                }
                            }
                        });
                    }
                }
            }).start();

        }
    }

    public boolean sortByName = true;

    public void sortByName() {
        current_sort = 2;
        if (recentlist != null) {
            if (recentlist.size() > 0) {
                if (sortByName) {
                    sortByName = false;
                    Collections.sort(recentlist,
                            new Recentmodel().new CustomComparatorSortByName());

                } else {
                    sortByName = true;
                    Collections
                            .sort(recentlist,
                                    new Recentmodel().new CustomComparatorSortByNameDec());
                }
                chatAdapter.setRecentArrayList(recentlist);
                chatAdapter.notifyDataSetChanged();
            }
        }

    }

    public boolean isSortByName() {
        return sortByName;
    }

    private boolean sortByRecent = false;

    public void sortByRecent() {
        current_sort = 1;
        if (recentlist != null) {
            if (recentlist.size() > 0) {
                if (sortByRecent) {
                    sortByRecent = false;
                    Collections
                            .sort(recentlist,
                                    new Recentmodel().new CustomComparatorSortByRecent());
                } else {
                    sortByRecent = true;
                    Collections
                            .sort(recentlist,
                                    new Recentmodel().new CustomComparatorSortByRecentDec());
                }
                chatAdapter.setRecentArrayList(recentlist);
                chatAdapter.notifyDataSetChanged();
            }
        }

    }

    public boolean isSortByRecent() {
        return sortByRecent;
    }

    public boolean sortByMostContected = true;

    public void sortByMostContected() {

        current_sort = 0;

        if (recentlist != null) {
            if (recentlist.size() > 0) {
                if (sortByMostContected) {
                    sortByMostContected = false;
                    Collections
                            .sort(recentlist,
                                    new Recentmodel().new CustomComparatorSortByMostContected());
                } else {
                    sortByMostContected = true;
                    Collections
                            .sort(recentlist,
                                    new Recentmodel().new CustomComparatorSortByMostContected());
                    Collections.reverse(recentlist);
                }
                chatAdapter.setRecentArrayList(recentlist);
                chatAdapter.notifyDataSetChanged();
            }
        }

    }

    public int getCurrent_sort() {
        return current_sort;
    }

    public boolean isSortByMostContected() {
        return sortByMostContected;
    }

    @SuppressLint("HandlerLeak")
    private void setRecentupdateHandler() {

        RecentupdateHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                final String remoteid = msg.getData().getString("remoteid");
                final String updatestatus = msg.getData().getString("msg");
                final String msgTime = msg.getData().getString("msgtime");
                boolean already = false;

                for (int i = 0; i < recentlist.size(); i++) {
                    String jid = recentlist.get(i).getRemote_jid();
                    if (jid.equals(remoteid)) {
                        already = true;
                        Recentmodel model = recentlist.get(i);
                        model.setLastmsg(updatestatus);
                        model.setLastmsg_t(msgTime);
                        model.setUnreadcount(model.getUnreadcount() + 1);
                        recentlist.remove(i);
                        recentlist.add(0, model);
                        Collections
                                .sort(recentlist,
                                        new Recentmodel().new CustomComparatorSortByRecent());
                        break;
                    }
                }

                if (!already) {
                    Utils.printLog("remoteid of group==++="
                            + remoteid.toLowerCase() + " " + updatestatus + " "
                            + msgTime);
                    Recentmodel model = GlobalData.dbHelper
                            .getsingleContactfromDB(remoteid.toLowerCase());
                    model.setLastmsg(updatestatus);
                    model.setLastmsg_t(msgTime);
                    model.setUnreadcount(1);
                    recentlist.add(0, model);
                    if (recentlist.size() == 1) {

                        Collections
                                .sort(recentlist,
                                        new Recentmodel().new CustomComparatorSortByRecent());
                        chatAdapter.setRecentArrayList(recentlist);

                        mDataNotFoundText.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }
                }
                if (recentlist.size() > 0) {
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "No recent chat", Toast.LENGTH_SHORT).show();
                }

                chatAdapter.notifyDataSetChanged();

            }
        };
    }

    @SuppressLint("HandlerLeak")
    private void UpdateImageRecentupdateHandler() {

        UpdateUserImageAndName = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                updateChatAdapter();

            }
        };
    }

    public ChatAdapter getChatAdapter() {
        return chatAdapter;
    }

    public static ArrayList<Recentmodel> getRecentlist() {
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

    public void getnewMembers(String remote_jid) {
        if (GlobalData.connection != null
                && GlobalData.connection.isConnected()) {

            MultiUserChat groupChat = homeActivity.initeGroupChat(remote_jid);

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

                    GlobalData.dbHelper.editGroupinDBNew(remote_jid,
                            selectedmemberlist);
                    final ArrayList<Contactmodel> filtermembers = GlobalData.dbHelper
                            .editContactforGroup(selectedmemberlist);// new
                    // comment
                    // 30Jan2015
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

                    name = Utils.getUsersNameWithGroupName(gmemberlist);
                    homeActivity.getmUserStatusTitle().setText(name);

                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        try {
            if (loadmember) {
                new Thread(new Runnable() {
                    public void run() {
                        getnewMembers(groip_jid);
                    }
                }).start();

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void setUserupdateHandler() {
        UserupStatusHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                final String updatestatus = msg.getData().getString(
                        "updatestatus");
                final String remoteid = msg.getData().getString("remoteid");

                for (int i = 0; i < recentlist.size(); i++) {
                    String jid = recentlist.get(i).getRemote_jid();
                    if (jid.equals(remoteid)) {
                        recentlist.get(i).setStatus(updatestatus);
                        break;
                    }
                }

                chatAdapter.setRecentArrayList(recentlist);
                chatAdapter.notifyDataSetChanged();

            }
        };
    }

    private void setUserUpdateImageHandler() {
        UserUpdateImageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                final byte[] byteArray = msg.getData()
                        .getByteArray("imagedata");
                final String remoteid = msg.getData().getString("remoteid");

                for (int i = 0; i < recentlist.size(); i++) {
                    String jid = recentlist.get(i).getRemote_jid();
                    if (jid.equals(remoteid)) {
                        recentlist.get(i).setUserpic(byteArray);
                        break;
                    }
                }

                chatAdapter.setRecentArrayList(recentlist);
                chatAdapter.notifyDataSetChanged();

            }
        };
    }

    public void clickEnableDisable() {

        clickable.setClickable(false);
    }

    private void exitGroup() {
        if (deleteItempos >= 0) {
            if (deleteRecentModel.getIsgroup() == 0) {
                clickable.setClickable(true);
                navigateSingleChatUser(deleteItempos);
            } else if (deleteRecentModel.getIsgroup() == 1) {
                String ownerJid = "";
                String groupJId = recentlist.get(deleteItempos)
                        .getRemote_jid();

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
                    updateChatAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                homeActivity
                        .deleteBroadCastGroupORChatWithDialog(deleteRemoteId);
                updateChatAdapter();
            }
        }
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
                        .getRemote_jid());
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
                                .getRemote_jid();
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
*   public void afterClearResetAdapter() {

        recentlist.clear();
        ArrayList<Recentmodel> arrayList = new ArrayList<Recentmodel>();
        arrayList.addAll(GlobalData.dbHelper.getRecentHistoryfromDb());
        recentlist.addAll(arrayList);
        chatAdapter.notifyDataSetChanged();

    }


    public void navigateSingleUserProfile(int poss) {

        homeActivity.getSorting_title().setVisibility(View.GONE);
        int pos = poss;

        Recentmodel chatmodel = recentlist.get(pos);
        Bundle data = new Bundle();
        data.putString("remote_jid", chatmodel.getRemote_jid());
        data.putString("remote_name", chatmodel.getDisplayname());
        data.putString("custom_status", chatmodel.getCustomStatus());
        data.putString("status", chatmodel.getStatus());
        data.putByteArray("remote_pic", chatmodel.getUserpic());
        data.putInt("isgroup", chatmodel.getIsgroup());
        data.putInt("isstranger", chatmodel.getIsStranger());
        data.putString("phonenumber", chatmodel.getUsernumber());
        data.putInt("isuserblock", chatmodel.getIsUserblock());

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                final NewContactModelForFlag contactmodel2 = new NewContactModelForFlag();
                Utils.saveContactItem(getActivity(), contactmodel2);
            }
        }).start();

        Fragment chatfrag = new SigleUserViewProfile();
        chatfrag.setArguments(data);
        homeActivity.callFragmentWithAddBack(chatfrag,
                ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);

    }
*
*public void startChat(Recentmodel chatmodel) {
        Bundle data = new Bundle();
        data.putString("remote_jid", chatmodel.getRemote_jid());
        data.putString("remote_name", chatmodel.getDisplayname());
        data.putString("custom_status", chatmodel.getCustomStatus());
        data.putByteArray("remote_pic", chatmodel.getUserpic());
        data.putInt("isgroup", chatmodel.getIsgroup());
        data.putInt("isstranger", chatmodel.getIsStranger());
        Fragment chatfrag = new SingleChatRoomFrgament();
        chatfrag.setArguments(data);
        FragmentTransaction agm_ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        agm_ft.addToBackStack(null);
        agm_ft.commit();
    }
     public void sortUnRecent() {

        if (recentlist != null) {
            if (recentlist.size() > 0) {

                Collections.sort(recentlist,
                        new Recentmodel().new CustomComparatorSortByRecent());

                chatAdapter.setRecentArrayList(recentlist);
                chatAdapter.notifyDataSetChanged();
            }
        }

    }
     public void deleteChatWithDialog(final String groupId) throws XMPPException {

        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Chat")
                .setMessage("Confirmation of chat delete?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                try {
                                    deleteChat(groupId);

                                } catch (Exception e) {
                                    Utils.printLog("delete group failed");
                                    e.printStackTrace();
                                }

                            }
                        }).show();

    }

    public void deleteChat(String remotejid) {
        GlobalData.dbHelper.deleteParticularUserHistory(remotejid);
        GlobalData.dbHelper.deleteRecentParticularrow(remotejid);
        GlobalData.dbHelper.deleteGroupParticularrow(remotejid);
        GlobalData.dbHelper.updateContactmsgData(remotejid, "", "");
        recentlist.clear();
        ArrayList<Recentmodel> arrayList = new ArrayList<Recentmodel>();
        arrayList.addAll(GlobalData.dbHelper.getRecentHistoryfromDb());
        recentlist.addAll(arrayList);
        chatAdapter.notifyDataSetChanged();
    }

    public void addContacttoDevice(String number, String name) {

        Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
        startActivity(intent);

    }
* */