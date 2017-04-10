package sms19.inapp.msg;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import sms19.inapp.msg.adapter.MediaPagerAdapter;
import sms19.inapp.msg.adapter.ProfilegroupListAdapter;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.constant.myAffiliate;
import sms19.inapp.msg.model.Chatmodel;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.Groupmodel;
import sms19.inapp.msg.model.NewContactModelForFlag;
import sms19.inapp.msg.model.Recentmodel;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;
import sms19.listview.adapter.ChatGroupMemberList;

public class GroupProfile extends Fragment implements OnClickListener {

    private static GroupProfile sigleUserViewProfile;

    private ListView mListView;
    private InAppMessageActivity homeActivity = null;
    private ChatAndContactParentFragment andContactParentFragment = null;
    private ViewPager viewPager;
    private MediaPagerAdapter mediaPagerAdapter = null;
    private Button exitGroupBtn;
    private Button deleteGroupBtn;

    private String mLastTitle = "";

    private byte[] frndpic = null;
    private String frndname = "";
    private String remote_jid = "";
    private int isgroup = 0;
    private int LAST_HIDE_MENU = 0;
    public static ArrayList<Recentmodel> gmemberlist;
    public static HashMap<String, Recentmodel> gmemberhasmap;
    private ProfilegroupListAdapter adapter = null;
    private TextView participant_count;
    private TextView media_count;
    private MultiUserChat groupChat = null;
    private ImageView imageView;
    public ArrayList<Chatmodel> chathistorylist = new ArrayList<Chatmodel>();
    private Groupmodel groupModel = new Groupmodel();
    private Recentmodel recentAdminModel = new Recentmodel();
    private Button mAddGroupBtn;
    private String groupName = "";
    private TextView mEdtBtn;
    private Bitmap profilePic = null;

    private Handler handlerRefreshAdapter = null;
    public static Handler onLineOfflineHandler = null;

    public static GroupProfile newInstance() {

        return sigleUserViewProfile;
    }

    private GroupUsersShowAsyncTask groupUsersShowAsyncTask = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (InAppMessageActivity) getActivity();
        andContactParentFragment = ChatAndContactParentFragment.newInstance();
        ConstantFlag.TAB_BACK_HANDLE_FRAGMENT = "2";

        LAST_HIDE_MENU = ConstantFields.HIDE_MENU;

        homeActivity.getLayout_name_status().setVisibility(View.GONE);
        homeActivity.getmActionBarImage().setVisibility(View.GONE);
        ConstantFields.HIDE_MENU = 3;
        homeActivity.invalidateOptionMenuItem();
        homeActivity.getActionbar_title().setVisibility(View.VISIBLE);
        mLastTitle = homeActivity.getActionbar_title().getText().toString();
        homeActivity.getActionbar_title().setText("Settings");

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sigleUserViewProfile = this;

        View view = getActivity().getLayoutInflater().inflate(
                R.layout.group_profile, container, false);

        gmemberlist = new ArrayList<Recentmodel>();
        Bundle getData = getArguments();
        frndname = getData.getString("remote_name", "");
        homeActivity.getActionbar_title().setText(frndname);
        groupName = getData.getString("remote_name", "");

        isgroup = getData.getInt("isgroup");

        remote_jid = getData.getString("remote_jid", "");
        frndpic = getData.getByteArray("remote_pic");

        frndpic = getData.getByteArray("remote_pic");

        initiateView(view);

        if (frndpic != null) {

            Bitmap pic = pic = BitmapFactory.decodeByteArray(frndpic, 0,
                    frndpic.length);
            profilePic = pic;
            imageScaling(pic);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        getDbMedia asyncTask = new getDbMedia();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asyncTask.execute();
        }

        groupModel = GlobalData.dbHelper.getSingleGroupFromDB(remote_jid);

        if (GlobalData.dbHelper.checkGroupiscreatedbyme(remote_jid)) {
            groupModel.setCreated_me("1");
            GlobalData.dbHelper.groupAddAdminDB(remote_jid,
                    InAppMessageActivity.myModel.getRemote_jid());

        } else {
            groupModel.setCreated_me("0");

        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (GlobalData.connection != null
                        && GlobalData.connection.isConnected()
                        && GlobalData.connection.isAuthenticated()) {
                    if (!GlobalData.dbHelper.groupIsBlockNew(remote_jid)) {
                        createJoinedGroup(GlobalData.connection, remote_jid);
                    }
                }
            }
        }).start();

        adapter = new ProfilegroupListAdapter(homeActivity,
                new ArrayList<Recentmodel>());
        adapter.setClickListener(this);
        mListView.setAdapter(adapter);

        // showGroupParticipantUsers(true);

        callHandler(true);

        if (chathistorylist != null) {
            media_count.setText(String.valueOf(chathistorylist.size()));
        } else {
            chathistorylist = new ArrayList<Chatmodel>();
            media_count.setText("0");
        }

        if (groupModel.getCreated_me().equalsIgnoreCase("0")) {
            deleteGroupBtn.setVisibility(View.GONE);
        }
    }

    public void initiateView(View view) {

        mListView = (ListView) view.findViewById(R.id.group_list);
        imageView = (ImageView) view.findViewById(R.id.image);
        participant_count = (TextView) view
                .findViewById(R.id.participant_count);
        media_count = (TextView) view.findViewById(R.id.media_count);
        // statusText=(TextView)view.findViewById(R.id.status);
        mAddGroupBtn = (Button) view.findViewById(R.id.add_group);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mEdtBtn = (TextView) view.findViewById(R.id.mEdtBtn);

        viewPager.setPageMargin(0);
        viewPager.setHorizontalFadingEdgeEnabled(true);
        viewPager.setFadingEdgeLength(10);


        View view2 = getActivity().getLayoutInflater().inflate(
                R.layout.group_bottom_view, null);
        exitGroupBtn = (Button) view2.findViewById(R.id.exit_group);
        deleteGroupBtn = (Button) view2.findViewById(R.id.delete_group);

        mEdtBtn.setOnClickListener(this);
        mAddGroupBtn.setOnClickListener(this);
        imageView.setOnClickListener(this);
        if (GlobalData.dbHelper.checkGroupiscreatedbyme(remote_jid)) {
            mAddGroupBtn.setVisibility(View.VISIBLE);
            exitGroupBtn.setVisibility(View.VISIBLE);
            // mEdtBtn.setVisibility(View.VISIBLE);
            mListView.addFooterView(view2);
        } else {
            mAddGroupBtn.setVisibility(View.GONE);
            exitGroupBtn.setVisibility(View.GONE);
            mEdtBtn.setVisibility(View.GONE);
            // mEdtBtn.setVisibility(View.VISIBLE);

        }
        if (GlobalData.dbHelper.groupIsBlockNew(remote_jid)) {
            mEdtBtn.setVisibility(View.VISIBLE);
        }

        if (!GlobalData.dbHelper.checkGroupiscreatedbyme(remote_jid)) {
            mEdtBtn.setVisibility(View.GONE);
        }

        exitGroupBtn.setOnClickListener(this);
        deleteGroupBtn.setOnClickListener(this);

        inItHandlerRefreshAdapter();
        setOnLineOfflineHandler();

    }

    private class getDbMedia extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<Chatmodel> chathistorylist2 = new ArrayList<Chatmodel>();
                chathistorylist.clear();
                chathistorylist2.addAll(GlobalData.dbHelper
                        .getChathistoryfromDBOfMedia(
                                InAppMessageActivity.myModel.getRemote_jid(),
                                remote_jid));
                chathistorylist.addAll(chathistorylist2);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if (chathistorylist != null) {
                    mediaPagerAdapter = new MediaPagerAdapter(getActivity(),
                            chathistorylist);
                    viewPager.setAdapter(mediaPagerAdapter);
                    media_count.setText(String.valueOf(chathistorylist.size()));

                } else {
                    chathistorylist = new ArrayList<Chatmodel>();
                    media_count.setText("0");
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public Integer imageScaling(Bitmap bitMap) {

        imageView.setImageBitmap(bitMap);

        return 0;

    }

    public float convertPixelsToDp(float px) {
        Resources resources = homeActivity.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {

        if (imageView == v) {

            if (frndpic != null) {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .create();
                alertDialog.setTitle("Profile Image");
                alertDialog.setMessage("Are you want to save profile image!");
                // alertDialog.set
                alertDialog.setButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // here you can add functions

                                if (profilePic != null) {
                                    Utils.saveFileInProfileFolder(profilePic,
                                            homeActivity);
                                }
                            }
                        });
                alertDialog.setButton2("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // here you can add functions
                            }
                        });

                alertDialog.show();
            }
        }

        if (mEdtBtn == v) {

            homeActivity.callFragmentWithAddBack(new ProfileEditFragment(),
                    "ProfileEditFragment");

        }

        if (mAddGroupBtn == v) {

            homeActivity.callFragmentWithAddBack(
                    new GroupSelectionPageOnProfile(),
                    "GroupSelectionPageOnProfile");

        }

        if (exitGroupBtn == v) {
            if (Utils.isDeviceOnline(getActivity())) {

                if (GlobalData.connection != null
                        && GlobalData.connection.isConnected()) {
                    // exitDialog(remote_jid,
                    // InAppMessageActivity.myModel.getRemote_jid());
                    showAssignAdminList(remote_jid,
                            InAppMessageActivity.myModel.getRemote_jid());
                    // exitGroup(remote_jid,
                    // InAppMessageActivity.myModel.getRemote_jid());
                } else {
                    Toast.makeText(getActivity(), "Not connected to server",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Check your network connection",
                        Toast.LENGTH_SHORT).show();
            }
        }

        if (deleteGroupBtn == v) {
            if (GlobalData.dbHelper.groupIsBlockNew(remote_jid)) {

                if (GlobalData.connection != null
                        && GlobalData.connection.isConnected()
                        && GlobalData.connection.isAuthenticated()) {

                    String groupId = remote_jid;
                    try {
                        deleteGroupChat(GlobalData.connection, groupId,
                                InAppMessageActivity.myModel.getRemote_jid());
                    } catch (XMPPException e) {
                        Utils.printLog("delete group failed");
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "Not connected to server",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "First exit the group",
                        Toast.LENGTH_SHORT).show();
            }
        }

        if (R.id.delete_group1 == v.getId()) {
            int pos = (Integer) v.getTag();
            if (GlobalData.connection != null
                    && GlobalData.connection.isConnected()
                    && GlobalData.connection.isAuthenticated()) {

                String remoteJidUser = gmemberlist.get(pos).getRemote_jid();
                try {
                    // leaveChatRoom(remote_jid, groupId);
                    if (gmemberlist.size() > 2) {
                        leaveChatRoom(remote_jid, remoteJidUser);
                    } else {
                        Toast.makeText(getActivity(),
                                "Atleast one user compulsory for this group!",
                                Toast.LENGTH_SHORT).show();
                    }
                    // deleteGroupChat(GlobalData.connection, groupId);
                } catch (Exception e) {
                    Utils.printLog("delete group failed");
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getActivity(), "Not connected to server",
                        Toast.LENGTH_SHORT).show();
            }
        }

        if (v.getId() == R.id.mTopLayout) {
            int pos = (Integer) v.getTag();
            homeActivity.toastMsg(String.valueOf(pos));
            if (andContactParentFragment != null) {
                homeActivity.callFragmentWithAddBack(
                        new SingleChatRoomFrgament(),
                        ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);
                // andContactParentFragment.addChildFragmentBackStack(new
                // SingleChatRoomFrgament(),
                // ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        homeActivity.getmActionBarImage().setVisibility(View.VISIBLE);
        homeActivity.getLayout_name_status().setVisibility(View.VISIBLE);
        homeActivity.getmUserStatusTitle().setVisibility(View.VISIBLE);

        homeActivity.getActionbar_title().setVisibility(View.GONE);
        ConstantFlag.TAB_BACK_HANDLE_FRAGMENT = "1";
        ConstantFields.HIDE_MENU = LAST_HIDE_MENU;

        ChatFragment chatFragment = ChatFragment.newInstance("");
        if (chatFragment != null) {
            chatFragment.clickEnableDisable();
        }

        homeActivity.invalidateOptionMenuItem();
        homeActivity.getActionbar_title().setText(mLastTitle);
        if (getActivity() != null) {
            NewContactModelForFlag contactmodel = Utils
                    .getContactItem(getActivity());
            if (contactmodel != null) {
                if (!contactmodel.getFromPage().equalsIgnoreCase("")) {
                    homeActivity.getActionbar_title().setVisibility(
                            View.VISIBLE);
                    homeActivity.getLayoutTab_contact_chat().setVisibility(
                            View.VISIBLE);

                    homeActivity.getmActionBarImage().setVisibility(View.GONE);
                    homeActivity.getLayout_name_status().setVisibility(
                            View.GONE);
                    homeActivity.getmUserStatusTitle().setVisibility(View.GONE);

                    Utils.saveContactItem(getActivity(),
                            new NewContactModelForFlag());

                    GroupListFrgament frgament = GroupListFrgament
                            .newInstance("");
                    if (frgament != null) {
                        frgament.clickAbleTrue();
                    }

                } else {
                    SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                            .newInstance("");
                    if (chatRoomFrgament != null) {
                        if (getActivity() != null) {
                            chatRoomFrgament.checkUserIsJoinGroup();
                        }
                    }
                }
            }
        }
        sigleUserViewProfile = null;

    }

    public void getnewMembers() {
        if (GlobalData.connection != null
                && GlobalData.connection.isConnected() && groupChat != null) {
            if (groupChat.isJoined()) {
                ArrayList<Contactmodel> selectedmemberlist = new ArrayList<Contactmodel>();
                try {
                    Collection<Affiliate> roomUsersOwner = groupChat
                            .getOwners();

                    Collection<myAffiliate> members = Utils
                            .getAffiliatesByAdmin("admin", remote_jid);

                    if (members.size() > 0) {
                        Iterator<myAffiliate> ids = members.iterator();

                        while (ids.hasNext()) {
                            String id = ids.next().getJid();
                            if (!id.equals(InAppMessageActivity.myModel
                                    .getRemote_jid())) {
                                if (gmemberhasmap != null) {
                                    if (!gmemberhasmap.containsKey(id)) {
                                        Contactmodel model = new Contactmodel();
                                        model.setRemote_jid(id);

                                        selectedmemberlist.add(model);

                                    }
                                } else {
                                    Contactmodel model = new Contactmodel();
                                    model.setRemote_jid(id);
                                    selectedmemberlist.add(model);
                                }
                            }
                        }

                        if (gmemberhasmap == null) {
                            GlobalData.dbHelper.editGroupinDB(remote_jid,
                                    selectedmemberlist);
                        }

                    }

					/*
                     * if (roomUsersOwner.size() > 0) {
					 * 
					 * Iterator<Affiliate> ids = roomUsersOwner.iterator();
					 * 
					 * 
					 * while (ids.hasNext()) { String id = ids.next().getJid();
					 * if
					 * (!id.equals(InAppMessageActivity.myModel.getRemote_jid(
					 * ))) { Contactmodel model = new Contactmodel();
					 * model.setRemote_jid(id); model.setIsAdmin(1);
					 * selectedmemberlist.add(model);
					 * 
					 * }else{
					 * GlobalData.dbHelper.groupCreatedByMeUpdateDB(remote_jid);
					 * }
					 * 
					 * } }
					 */

                    if (selectedmemberlist.size() > 0) {
                        Utils.printLog("new member added in group...");
                        GlobalData.dbHelper.editGroupinDB(remote_jid,
                                selectedmemberlist);
                        final ArrayList<Contactmodel> filtermembers = GlobalData.dbHelper
                                .editContactforGroup(selectedmemberlist);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Utils.printLog("new member added in group adapter set...");

                                // setGroupmemberlist(false);
                                // showGroupParticipantUsers(false);
                                callHandler(false);
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
            } else {
                // Toast.makeText(getActivity(),
                // "Not connected to server",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void callHandler(boolean bFlag) {
        android.os.Message msg = new android.os.Message();// comment m
        Bundle b = new Bundle();
        b.putBoolean("message", bFlag);
        msg.setData(b);
        handlerRefreshAdapter.sendMessage(msg);
    }

    public void showGroupParticipantUsers(boolean temp) {

        if (groupUsersShowAsyncTask == null) {
            Object[] objects = new Object[2];
            objects[0] = temp;
            groupUsersShowAsyncTask = new GroupUsersShowAsyncTask();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                groupUsersShowAsyncTask.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, objects);
            } else {
                groupUsersShowAsyncTask.execute(objects);
            }
        } else {

            groupUsersShowAsyncTask.cancel(true);
            groupUsersShowAsyncTask = null;
            Object[] objects = new Object[2];
            objects[0] = temp;
            groupUsersShowAsyncTask = new GroupUsersShowAsyncTask();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                groupUsersShowAsyncTask.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, objects);
            } else {
                groupUsersShowAsyncTask.execute(objects);
            }
        }

    }

    public class GroupUsersShowAsyncTask extends AsyncTask<Object, Void, Void> {

        ArrayList<Recentmodel> temp = null;
        boolean b = false;

        @Override
        protected Void doInBackground(Object... params) {

            try {

                b = (Boolean) params[0];

                if (gmemberlist.size() > 0) {
                    gmemberlist.clear();
                }

                if (temp != null) {
                    temp.clear();
                    temp = null;
                } else {
                    temp = null;
                }

                temp = GlobalData.dbHelper.getGroupmemberListfromDB(remote_jid);

                if (temp != null && temp.size() > 0) {
                    gmemberhasmap = new HashMap<String, Recentmodel>();
                    gmemberlist.addAll(temp);

                    Utils.printLog("gmemberlist size=" + gmemberlist.size());
                    for (int i = 0; i < gmemberlist.size(); i++) {
                        // Utils.printLog("temp.name()...="+gmemberlist.get(i).getDisplayname());
                        gmemberhasmap.put(gmemberlist.get(i).getRemote_jid(),
                                gmemberlist.get(i));
                    }

                    // add admin
                    recentAdminModel.setDisplayname("You(Admin)");
                    recentAdminModel.setUserpic(InAppMessageActivity.myModel
                            .getAvatar());

                    if (getGroupModel().getCreated_me().equalsIgnoreCase("1")) {
                        gmemberlist.add(recentAdminModel);
                    } else {
                        recentAdminModel.setDisplayname("You");

                        if (!GlobalData.dbHelper.groupIsBlockNew(remote_jid)) {
                            gmemberlist.add(recentAdminModel);
                        }

                    }

                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if (temp != null && temp.size() > 0) {
                    adapter.setRecentArrayList(gmemberlist);
                    adapter.notifyDataSetChanged();
                    participant_count
                            .setText(String.valueOf(gmemberlist.size()));

                } else {
                    recentAdminModel.setDisplayname("You(Admin)");
                    recentAdminModel.setUserpic(InAppMessageActivity.myModel
                            .getAvatar());

                    if (getGroupModel().getCreated_me().equalsIgnoreCase("1")) {
                        if (!GlobalData.dbHelper.groupIsBlockNew(remote_jid)) {
                            gmemberlist.add(recentAdminModel);
                        }

                    }
                }

                try {
                    setListViewHeightBasedOnItems(mListView);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    if (b) {
                        new Thread(new Runnable() {
                            public void run() {
                                if (!GlobalData.dbHelper
                                        .groupIsBlockNew(remote_jid)) {
                                    getnewMembers();
                                }
                            }
                        }).start();

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public void setGroupmemberlist(boolean loadmember) {
        try {
            if (gmemberlist.size() > 0) {
                gmemberlist.clear();
            }
            ArrayList<Recentmodel> temp = GlobalData.dbHelper
                    .getGroupmemberListfromDB(remote_jid);

            if (temp != null && temp.size() > 0) {
                gmemberhasmap = new HashMap<String, Recentmodel>();
                gmemberlist.addAll(temp);

                Utils.printLog("gmemberlist size=" + gmemberlist.size());
                for (int i = 0; i < gmemberlist.size(); i++) {
                    // Utils.printLog("temp.name()...="+gmemberlist.get(i).getDisplayname());
                    gmemberhasmap.put(gmemberlist.get(i).getRemote_jid(),
                            gmemberlist.get(i));
                }

                // add admin
                recentAdminModel.setDisplayname("You(Admin)");
                recentAdminModel.setUserpic(InAppMessageActivity.myModel
                        .getAvatar());

                if (getGroupModel().getCreated_me().equalsIgnoreCase("1")) {
                    gmemberlist.add(recentAdminModel);
                } else {
                    recentAdminModel.setDisplayname("You");

                    if (!GlobalData.dbHelper.groupIsBlockNew(remote_jid)) {

                        gmemberlist.add(recentAdminModel);
                    }

                }

                adapter.setRecentArrayList(gmemberlist);
                adapter.notifyDataSetChanged();
                participant_count.setText(String.valueOf(gmemberlist.size()));

            } else {
                // add admin (if user is admin in case add own this list)
                recentAdminModel.setDisplayname("You(Admin)");
                recentAdminModel.setUserpic(InAppMessageActivity.myModel
                        .getAvatar());

                if (getGroupModel().getCreated_me().equalsIgnoreCase("1")) {
                    if (!GlobalData.dbHelper.groupIsBlockNew(remote_jid)) {
                        gmemberlist.add(recentAdminModel);
                    }

                }

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        try {
            if (loadmember) {
                new Thread(new Runnable() {
                    public void run() {
                        getnewMembers();
                    }
                }).start();

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            setListViewHeightBasedOnItems(mListView);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void delete_user_chat_history(final String remotejid) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Chat")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                GlobalData.dbHelper
                                        .deleteParticularUserHistory(remotejid);
                                GlobalData.dbHelper
                                        .deleteGroupParticularrow(remotejid);

                                // setGroupmemberlist(true);
                                // showGroupParticipantUsers(true);
                                callHandler(true);
                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                            }
                        }).show();
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
            int totalDividersHeight = listView.getDividerHeight()
                    * (numberOfItems - 1);

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

    private void deleteGroupChat(final Connection conn, final String groupId,
                                 final String jid) throws XMPPException {

        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Group")
                .setMessage("Confirmation of group delete?")
                .setNegativeButton(/* android.R.string.no */"No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                            }
                        })
                .setPositiveButton(/* android.R.string.yes */"Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                MultiUserChat muc = null;

                                try {

                                    Utils.printLog("Delete group");
                                    muc = new MultiUserChat(conn, groupId);

                                    if (gmemberlist != null) {
                                        if (gmemberlist.size() > 0) {
                                            muc.grantOwnership(gmemberlist.get(
                                                    0).getRemote_jid());
                                            muc.revokeMembership(groupId);
                                            GlobalData.dbHelper
                                                    .updateGroupCreatedByMe(groupId);
                                        }
                                    }
                                    muc.banUser(jid, "delete group");
                                    try {
                                        muc.leave();
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    // muc.join(groupId);
                                    // muc.destroy("was group room", null);

                                    GlobalData.dbHelper
                                            .deleteParticularUserHistory(groupId);
                                    GlobalData.dbHelper
                                            .deleteGroupParticularrow(groupId);
                                    GlobalData.dbHelper
                                            .deleteRecentParticularrow(groupId);
                                    GlobalData.dbHelper
                                            .DeleteContactRemoteIdBase(groupId);
                                    // setGroupmemberlist(true);
                                    // showGroupParticipantUsers(true);
                                    callHandler(true);
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

                                    homeActivity.backPress();

                                } catch (XMPPException e) {
                                    // TODO Auto-generated catch block
                                    Utils.printLog("delete group failed");
                                    try {
                                        if (muc != null) {
                                            muc.leave();
                                        }
                                    } catch (Exception e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    e.printStackTrace();
                                }

                                // setGroupmemberlist(true);
                                // showGroupParticipantUsers(true);
                                callHandler(true);
                            }
                        }).show();

    }

    public void leaveChatRoom(final String groupJId, final String jid) {

        Log.i("XMPP Chat Client", "User left chat room ");

        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Member")
                .setMessage("Confirmation of member delete?")
                .setNegativeButton(/* android.R.string.no */"No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                            }
                        })
                .setPositiveButton(/* android.R.string.yes */"Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                try {
                                    if (groupChat != null
                                            && groupChat.isJoined()) {

                                        // leaveChatRoom2(groupJId, username);

                                        boolean isdeleted = removeGroupMemberAsync(
                                                groupChat, groupJId, jid);
                                        if (isdeleted) {
                                            GlobalData.dbHelper
                                                    .deleteGroupMemberFromDB(
                                                            groupJId, jid);
                                            // setGroupmemberlist(true);
                                            // showGroupParticipantUsers(true);
                                            callHandler(true);
                                        }

                                    } else {
                                        Toast.makeText(getActivity(),
                                                "Not connected to server",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    Utils.printLog("Failed leave group..");
                                    e.printStackTrace();
                                }

                                // setGroupmemberlist(true);
                            }
                        }).show();

    }

    public boolean createJoinedGroup(Connection conn, String groupId) {
        boolean isConnected = false;
        if (groupChat != null && groupChat.isJoined()) {
            isConnected = true;
            Log.i("XMPP Chat Client", "Joined True");
        } else {
            groupChat = new MultiUserChat(conn, groupId);
            try {

                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(0);

                if (InAppMessageActivity.myModel.getRemote_jid() != null
                        && (!InAppMessageActivity.myModel.getRemote_jid()
                        .equalsIgnoreCase(""))) {
                    groupChat
                            .join(InAppMessageActivity.myModel.getRemote_jid(),
                                    null, history,
                                    SmackConfiguration.getPacketReplyTimeout());

                } else {
                    if (GlobalData.dbHelper != null) {
                        InAppMessageActivity.myModel = GlobalData.dbHelper
                                .getUserDatafromDB();
                    }
                }

            } catch (XMPPException e) {
                Utils.printLog("Failed join group");
                e.printStackTrace();
            }
        }
        return isConnected;
    }

    public boolean isJoinedGroup(String username) {
        boolean isConnected = false;
        if (groupChat != null && groupChat.isJoined()) {
            isConnected = true;
            Log.i("XMPP Chat Client", "Joined True");
        }
        return isConnected;
    }

    public String getRemote_jid() {
        return remote_jid;
    }

    public void setFrndpic(byte[] frndpic) {
        this.frndpic = frndpic;
    }

    public Groupmodel getGroupModel() {
        return groupModel;
    }

    public boolean addGroupMemberAsync(MultiUserChat groupChat, String jid,
                                       ArrayList<Contactmodel> contactmodels) {

        boolean isCreated = false;
        try {

            if (groupChat != null) {
                String chatRoomJid = remote_jid;

                String grouppicbase64string = "";
                Bitmap grpp = null;
                byte[] grouppicpath = GlobalData.dbHelper
                        .getGroupIcon(remote_jid);
                if (grouppicpath != null) {
                    grpp = BitmapFactory.decodeByteArray(grouppicpath, 0,
                            grouppicpath.length);
                }

                if (grpp != null) {
                    grouppicbase64string = Utils.convertTobase64string(grpp);
                }

                for (int i = 0; i < contactmodels.size(); i++) {

                    if (!chatRoomJid.equalsIgnoreCase("")) {
                        try {
                            GlobalData.dbHelper
                                    .singleGroupContactUnBlockfromDB(contactmodels
                                            .get(i).getRemote_jid());
                            Message msg = new Message();

                            msg.setBody(grouppicbase64string);

                            groupChat.grantOwnership(contactmodels.get(i)
                                    .getRemote_jid());
                            groupChat.grantMembership(contactmodels.get(i)
                                    .getRemote_jid());
                            groupChat.grantModerator(contactmodels.get(i)
                                    .getRemote_jid());

                            groupChat.invite(msg, contactmodels.get(i)
                                    .getRemote_jid(), groupName);

                            isCreated = true;
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }

                Form form = groupChat.getConfigurationForm();
                if (form != null) {
                    Form submitForm = form.createAnswerForm();
                    for (Iterator<FormField> fields = form.getFields(); fields
                            .hasNext(); ) {
                        FormField field = fields.next();
                        if (!FormField.TYPE_HIDDEN.equals(field.getType())
                                && field.getVariable() != null) {
                            submitForm.setDefaultAnswer(field.getVariable());
                        }
                    }
                    List<String> owners = new ArrayList<String>();

                    for (int j = 0; j < gmemberlist.size(); j++) {
                        if (!InAppMessageActivity.myModel.getRemote_jid()
                                .equalsIgnoreCase(
                                        gmemberlist.get(j).getRemote_jid())) {
                            if (!gmemberlist.get(j).getRemote_jid()
                                    .equalsIgnoreCase("")) {
                                owners.add(gmemberlist.get(j).getRemote_jid());
                            }
                        }

                    }

                    for (int ii = 0; ii < contactmodels.size(); ii++) {
                        owners.add(contactmodels.get(ii).getRemote_jid());
                    }

                    submitForm.setAnswer("muc#roomconfig_roomadmins", owners);
                    submitForm.setAnswer("muc#roomconfig_persistentroom", true);

                    //

                    submitForm.setAnswer("muc#roomconfig_publicroom", true);

                    groupChat.sendConfigurationForm(submitForm);

                }

                isCreated = true;

                if (isCreated) {
                    // setGroupmemberlist(true);
                    // showGroupParticipantUsers(true);
                }

            }/*
             * else { Toast.makeText(getActivity(),
			 * "Not connected to server",Toast.LENGTH_SHORT).show(); }
			 */
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isCreated = false;
        }

        return isCreated;

    }

    protected boolean removeGroupMemberAsync(MultiUserChat groupChat,
                                             String groupJId, String jid) {

        boolean isCreated = false;
        try {
            String chatRoomJid = remote_jid;

            if (!chatRoomJid.equalsIgnoreCase("")) {

                // groupChat.banUser(jid, "exit");

                // groupChat.revokeOwnership(jid);

                groupChat.revokeMembership(jid);
                // groupChat.revokeModerator(jid);
                // groupChat.kickParticipant(jid, "kick from group");
                groupChat.banUser(jid, "exit");
                isCreated = true;
            }
        } catch (XMPPException e) {

            e.printStackTrace();
            isCreated = false;
        }
        return isCreated;

    }

    public MultiUserChat getGroupChat() {
        return groupChat;
    }

    public void setGroupChat(MultiUserChat groupChat) {
        this.groupChat = groupChat;
    }

    public void exitDialog(final String groupJId, final String jid) {

        Log.i("XMPP Chat Client", "User left chat room ");

        new AlertDialog.Builder(getActivity())
                .setTitle("Exit Member")
                .setMessage("Confirmation of member exit?")
                .setNegativeButton(/* android.R.string.no */"No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                            }
                        })
                .setPositiveButton(/* android.R.string.yes */"Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                try {
                                    if (groupChat != null
                                            && groupChat.isJoined()) {

                                        // exitGroup(groupJId, jid);
                                        showAssignAdminList(groupJId, jid);

                                    } else {
                                        Toast.makeText(getActivity(),
                                                "Not connected to server",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    Utils.printLog("Failed leave group..");
                                    e.printStackTrace();
                                }

                                // setGroupmemberlist(true);
                            }
                        }).show();

    }

    private void inItHandlerRefreshAdapter() {
        // TODO Auto-generated method stub
        handlerRefreshAdapter = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                boolean message = msg.getData().getBoolean("message");
                setGroupmemberlist(message);

            }
        };
    }

    private int index = 0;

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
                exitGroup(groupJId, jid, index);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void exitGroup(String groupJId, String jid, int index) {
        Log.i("XMPP Chat Client", "User left chat room ");
        if (groupChat != null && groupChat.isJoined()) {
            try {

                // GlobalData.dbHelper.updateGroupCreatedByMe(groupJId);
                GlobalData.dbHelper.groupBlocknewfromDB(groupJId);
                GlobalData.dbHelper.singleGroupContactBlockfromDB(jid);

				/*
                 * GlobalData.dbHelper.deleteParticularUserHistory(groupJId) ;
				 * GlobalData.dbHelper.deleteGroupParticularrow(groupJId);
				 * GlobalData.dbHelper.deleteRecentParticularrow(groupJId);
				 */

                try {
                    // groupChat.grantMembership(jid);

                    if (gmemberlist != null) {
                        if (gmemberlist.size() > 0) {
                            groupChat.grantOwnership(gmemberlist.get(index)
                                    .getRemote_jid());
                            groupChat.grantAdmin(gmemberlist.get(index)
                                    .getRemote_jid());
                        }
                    }
                    groupChat.revokeMembership(groupJId);
                    groupChat.banUser(jid, "delete group");
                    GlobalData.dbHelper.updateGroupCreatedByMe(groupJId);

                    // groupChat.revokeModerator(jid);
                    // groupChat.kickParticipant(jid, "delete group");

                    groupChat.leave();

                    homeActivity.backPress();
                    // homeActivity.backPress();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    try {
                        groupChat.leave();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    homeActivity.backPress();
                    // homeActivity.backPress();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String getGroupName() {
        return groupName;
    }

    public byte[] getFrndpic() {
        return frndpic;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public static ArrayList<Recentmodel> getGmemberlist() {
        return gmemberlist;
    }

    private void setOnLineOfflineHandler() {
        onLineOfflineHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {

                super.handleMessage(msg);
                if (getActivity() != null) {

                    try {
                        Contactmodel contactmodel = null;
                        try {
                            contactmodel = GlobalData.dbHelper
                                    .getCustomStatus(remote_jid);
                        } catch (Exception e) {
                            contactmodel = null;
                            e.printStackTrace();
                        }
                        if (contactmodel != null) {
                            if (contactmodel.getStatus() != null) {

                            }
                            frndname = contactmodel.getName();
                            groupName = frndname;
                            homeActivity.getActionbar_title().setText(frndname);
                            homeActivity.getmUserNameTitle().setText(frndname);
                            frndpic = contactmodel.getAvatar();
                            if (frndpic != null) {

                                Bitmap pic2 = BitmapFactory.decodeByteArray(
                                        frndpic, 0, frndpic.length);
                                imageScaling(pic2);
                                homeActivity.getmActionBarImage()
                                        .setImageBitmap(pic2);
                            }
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        };
    }
}
