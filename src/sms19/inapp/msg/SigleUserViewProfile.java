package sms19.inapp.msg;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kitever.android.R;

import org.jivesoftware.smack.PrivacyListManager;

import java.util.ArrayList;

import sms19.inapp.msg.adapter.MediaPagerAdapter;
import sms19.inapp.msg.adapter.SingleUserProfileGroupAdapter;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Chatmodel;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.Groupmodel;
import sms19.inapp.msg.model.NewContactModelForFlag;
import sms19.inapp.msg.model.Recentmodel;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;

public class SigleUserViewProfile extends Fragment implements OnClickListener {


    private static SigleUserViewProfile sigleUserViewProfile;
    private SingleUserProfileGroupAdapter groupAdapter = null;
    private ListView mListView;
    private InAppMessageActivity homeActivity = null;
    private ChatAndContactParentFragment andContactParentFragment = null;
    private ViewPager viewPager;
    private MediaPagerAdapter mediaPagerAdapter = null;
    private TextView participant_count;
    private TextView media_count, statusText;
    private String mLastTitle = "";
    private byte[] frndpic = null;
    private String frndname = "";
    private String remote_jid = "";
    private String status = "";
    private int isgroup = 0;
    private int LAST_HIDE_MENU = 0;
    private ImageView imageView;
    private ArrayList<Groupmodel> recentlist;
    private ArrayList<Groupmodel> recentlistVerify;
    public ArrayList<Chatmodel> chathistorylist = new ArrayList<Chatmodel>();
    private Button block_user_btn;
    private Button delete_chat_btn;
    private SingleChatRoomFrgament chatRoomFrgament = null;
    private TextView phone;
    private TextView mTimeLastStatus;
    private Bitmap profilePic = null;
    private int isuserblock = 0;


    public static SigleUserViewProfile newInstance() {


        return sigleUserViewProfile;
    }

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
        chatRoomFrgament = SingleChatRoomFrgament.newInstance("");


    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sigleUserViewProfile = this;

        View view = getActivity().getLayoutInflater().inflate(R.layout.single_user_view_profile, container, false);


        Bundle getData = getArguments();
        frndname = getData.getString("remote_name");
        homeActivity.getActionbar_title().setText(frndname);
        isgroup = getData.getInt("isgroup");
        status = getData.getString("status");

        frndpic = getData.getByteArray("remote_pic");
        remote_jid = getData.getString("remote_jid", "");

        if (GlobalData.dbHelper.isContactBlock(remote_jid)) {
            isuserblock = 1;
        } else {
            isuserblock = 0;
        }


        initiateView(view);


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);


        getDbMedia dbMedia = new getDbMedia();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dbMedia.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            dbMedia.execute();
        }

        if (frndpic != null) {
            Bitmap pic = BitmapFactory.decodeByteArray(frndpic, 0, frndpic.length);
            profilePic = pic;
            imageScaling(pic);
        }

        recentlistVerify = (GlobalData.dbHelper.getGroupAddWithUserArrayListFromDB(remote_jid));
        if (recentlistVerify == null) {
            recentlistVerify = new ArrayList<Groupmodel>();
            recentlist = new ArrayList<Groupmodel>();
            Utils.printLog("" + recentlist.size());

        } else {
            recentlist = new ArrayList<Groupmodel>();
            for (int i = 0; i < recentlistVerify.size(); i++) {
                Groupmodel groupmodel = recentlistVerify.get(i);
                ArrayList<Recentmodel> arrayList = groupmodel.getContactList();
                if (arrayList != null) {
                    for (int j = 0; j < arrayList.size(); j++) {
                        if (arrayList.get(j) != null) {
                            if (arrayList.get(j).getRemote_jid() != null) {
                                if (arrayList.get(j).getRemote_jid().equalsIgnoreCase(remote_jid)) {
                                    recentlist.add(groupmodel);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }


        groupAdapter.setRecentArrayList(recentlist);
        groupAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnItems(mListView);
        participant_count.setText(String.valueOf(recentlist.size()));


    }

    private class getDbMedia extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {


            try {
                ArrayList<Chatmodel> chathistorylist2 = new ArrayList<Chatmodel>();
                chathistorylist.clear();
                chathistorylist2.addAll(GlobalData.dbHelper.getChathistoryfromDBOfMedia(InAppMessageActivity.myModel.getRemote_jid(), remote_jid));
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
                    mediaPagerAdapter = new MediaPagerAdapter(getActivity(), chathistorylist);
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

    public float convertPixelsToDp(float px) {
        Resources resources = homeActivity.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }


    public Integer imageScaling(Bitmap bitMap) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = (displaymetrics.heightPixels * 30) / 100;
        imageView.setImageBitmap(bitMap);
        return height;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (isgroup != 1) {

            Contactmodel contactmodel = null;
            try {
                contactmodel = GlobalData.dbHelper.getCustomStatus(remote_jid);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                contactmodel = null;
                e1.printStackTrace();
            }

            if (contactmodel != null) {
                if (contactmodel.getStatus() != null) {
                    if (contactmodel.getStatus().equalsIgnoreCase("1")) {
                        homeActivity.getmUserStatusTitle().setText("Online");
                    } else {
                        statusText.setText(contactmodel.getCustomStatus());
                    }
                }
                if (contactmodel.getStatus() != null) {
                    if (contactmodel.getStatus().equalsIgnoreCase("1")) {
                        if (contactmodel.getCustomStatus().equalsIgnoreCase("0")) {
                            statusText.setText("Online");
                        } else {
                            statusText.setText(contactmodel.getCustomStatus());
                        }


                    } else {
                        if (contactmodel.getCustomStatus().equalsIgnoreCase("0")) {
                            LastSeenUpdate(contactmodel.getLastseen());


                        } else {
                            statusText.setText(contactmodel.getCustomStatus());

                        }

                    }
                }


                if (statusText.getText().toString().equalsIgnoreCase("0")) {

                    //statusText.setText("");
                    statusText.setText("Offline");
                }

                if (statusText.getText().toString().equalsIgnoreCase("original")) {
                    if (contactmodel.getStatus().toString().equalsIgnoreCase("0")) {
                        statusText.setText("Offline");
                    } else {
                        statusText.setText("Online");
                    }

                }
                //sms19.inapp.msg.constant.Utils.getmsgTime(msgTime)

                try {
                    if (contactmodel.getStatus_custom_time() != null) {
                        mTimeLastStatus.setText(Utils.getFormattedDateFromTimestamp(Long.valueOf(contactmodel.getStatus_custom_time())) + "  " + Utils.getmsgTime(contactmodel.getStatus_custom_time()));
                    }

                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!mTimeLastStatus.getText().toString().equalsIgnoreCase("")) {
                    if (statusText.getText().toString().equalsIgnoreCase("")) {
                        statusText.setText("unavailable");
                    }
                }


            }
        }

    }

    public void LastSeenUpdate(String time) {

        if (time != null) {
            if (!time.equalsIgnoreCase("")) {
                homeActivity.getmUserStatusTitle().setText("Last seen " + Utils.getmsgTime(time));
            } else {
                homeActivity.getmUserStatusTitle().setText("Offline");
            }
        } else {

            homeActivity.getmUserStatusTitle().setText("Offline");
        }

    }

    public void initiateView(View view) {

        mListView = (ListView) view.findViewById(R.id.group_list);
        imageView = (ImageView) view.findViewById(R.id.image);
        statusText = (TextView) view.findViewById(R.id.status);
        participant_count = (TextView) view.findViewById(R.id.participant_count);
        media_count = (TextView) view.findViewById(R.id.media_count);
        phone = (TextView) view.findViewById(R.id.phone);
        block_user_btn = (Button) view.findViewById(R.id.block_user_btn);
        delete_chat_btn = (Button) view.findViewById(R.id.delete_chat_btn);
        mTimeLastStatus = (TextView) view.findViewById(R.id.time);

        imageView.setOnClickListener(this);


        if (isuserblock == 1) {
            block_user_btn.setText("Unblock");
        } else {
            block_user_btn.setText("Block");
        }


        if (status != null && status.equalsIgnoreCase("1")) {
            statusText.setText("Online");
        } else {
            statusText.setText("Offline");
        }


        block_user_btn.setOnClickListener(this);
        delete_chat_btn.setOnClickListener(this);
        if (!remote_jid.equalsIgnoreCase("")) {
            String phoneStr = remote_jid.split("@")[0];
            phone.setText(phoneStr);
        }

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);


        viewPager.setPageMargin(-50);

        if (chathistorylist != null) {
            media_count.setText(String.valueOf(chathistorylist.size()));
        } else {
            chathistorylist = new ArrayList<Chatmodel>();
            media_count.setText("0");
        }
        //mediaPagerAdapter=new MediaPagerAdapter(getActivity(),chathistorylist);
        //viewPager.setAdapter(mediaPagerAdapter);
        groupAdapter = new SingleUserProfileGroupAdapter(homeActivity, new ArrayList<Groupmodel>());
        groupAdapter.setClickListener(this);
        mListView.setAdapter(groupAdapter);


    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {


        if (imageView == v) {


            if (frndpic != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Profile Image");
                alertDialog.setMessage("Are you want to save profile image!");
                //  alertDialog.set
                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions

                        if (profilePic != null) {
                            Utils.saveFileInProfileFolder(profilePic, homeActivity);
                        }
                    }
                });
                alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }
                });

                alertDialog.show();
            }


        }


        if (block_user_btn == v) {

            if (GlobalData.dbHelper.isContactBlock(remote_jid)) {
                isuserblock = 1;
            } else {
                isuserblock = 0;

            }

            if (isuserblock == 0) {
                blockContact(remote_jid);
            } else {
                unblockContact(remote_jid);
            }
        }


        if (v.getId() == R.id.mTopLayout) {
            int pos = (Integer) v.getTag();
            homeActivity.toastMsg(String.valueOf(pos));
            if (andContactParentFragment != null) {
                homeActivity.callFragmentWithAddBack(new SingleChatRoomFrgament(), ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);

            }

        }

        if (v == delete_chat_btn) {
            delete_user_chat_history(remote_jid);

        }

        if (v.getId() == R.id.nav2) {

            int pos = (Integer) v.getTag();
            Groupmodel chatmodel = recentlist.get(pos);
            homeActivity.backPress();
            homeActivity.backPress();
            homeActivity.getGroupInfoAndOpenChatPanel(chatmodel.getGroup_jid(), chatmodel.getGroupname());

			/*int pos=(Integer)v.getTag();
			Groupmodel chatmodel=recentlist.get(pos);
			Recentmodel recentmodel=GlobalData.dbHelper.getsingleContactfromDB(chatmodel.getGroup_jid());
			Bundle data = new Bundle();
			data.putString("remote_jid", chatmodel.getGroup_jid());
			data.putString("remote_name", chatmodel.getGroupname());
			data.putString("custom_status", "");
			data.putByteArray("remote_pic", recentmodel.getUserpic());
			data.putInt("isgroup", recentmodel.getIsgroup());
			data.putInt("isstranger", 0);
			data.putInt("isuserblock", 0);
			NewContactModelForFlag contactmodel=new NewContactModelForFlag();
			contactmodel.setUserFromCommanGroup("1");

			Utils.saveContactItem(getActivity(), contactmodel);
			Fragment chatfrag=new SingleChatRoomFrgament();
			chatfrag.setArguments(data);
			homeActivity.callFragmentWithAddBack(chatfrag, ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);*/

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

        homeActivity.invalidateOptionMenuItem();
        homeActivity.getActionbar_title().setText(mLastTitle);
        ChatFragment chatFragment = ChatFragment.newInstance("");
        if (chatFragment != null) {
            chatFragment.clickEnableDisable();
        }

        if (chatRoomFrgament != null) {
            homeActivity.setOnLineOffileStatus(remote_jid, isgroup, 1);
            chatRoomFrgament.updateBlock();
        }

        if (getActivity() != null) {
            NewContactModelForFlag contactmodel = Utils.getContactItem(getActivity());
            if (!contactmodel.getFromPage().equalsIgnoreCase("")) {
                homeActivity.getActionbar_title().setVisibility(View.VISIBLE);
                homeActivity.getLayoutTab_contact_chat().setVisibility(View.VISIBLE);

                homeActivity.getmActionBarImage().setVisibility(View.GONE);
                homeActivity.getLayout_name_status().setVisibility(View.GONE);
                homeActivity.getmUserStatusTitle().setVisibility(View.GONE);


                Utils.saveContactItem(getActivity(), new NewContactModelForFlag());
            }
        }


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


    public void delete_user_chat_history(final String remotejid) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Chat")
                .setMessage("Are you sure you want to delete chat history?")
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                GlobalData.dbHelper.deleteParticularUserHistory(remotejid);
                                GlobalData.dbHelper.deleteRecentParticularrow(remotejid);
                                homeActivity.backPress();
                                homeActivity.backPress();
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


    public void blockContact(final String groupJId) {
        Log.i("XMPP Chat Client", "User left chat room ");

        new AlertDialog.Builder(getActivity())
                .setTitle("Block Contact")
                .setMessage("Are you sure you want to block this contact?")
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                try {
                                    //GlobalData.dbHelper.singleContactBlockfromDB(groupJId);
                                    //chatRoomFrgament.setIsuserblock(1);
                                    //chatRoomFrgament.getmBlockBtn().setText("Unblock User");
                                    GlobalData.privacyManager = PrivacyListManager.getInstanceFor(GlobalData.connection);
                                    if (GlobalData.privacyManager != null) {
                                        homeActivity.blockUserWithPrivacy(groupJId);
                                    }

                                } catch (Exception e) {
                                    Utils.printLog("Failed leave group..");
                                    e.printStackTrace();
                                }

                            }
                        })
                .show();


    }


    public void unblockContact(final String groupJId) {


        Log.i("XMPP Chat Client", "User left chat room ");
        new AlertDialog.Builder(getActivity())
                .setTitle("Block Contact")
                .setMessage("Unblock " + frndname + "  to send a message")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                            }
                        })
                .setPositiveButton("Unblock",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                try {
                                    //GlobalData.dbHelper.singleContactUnBlockfromDB(groupJId);
                                    //chatRoomFrgament.setIsuserblock(0);
                                    //chatRoomFrgament.getmBlockBtn().setText("Block User");

                                    GlobalData.privacyManager = PrivacyListManager.getInstanceFor(GlobalData.connection);
                                    if (GlobalData.privacyManager != null) {
                                        homeActivity.unBlockUserWithPrivacy(groupJId);
                                    }


                                } catch (Exception e) {
                                    Utils.printLog("Failed leave group..");
                                    e.printStackTrace();
                                }
                            }
                        })
                .show();


    }

    public Button getBlock_user_btn() {
        return block_user_btn;
    }


}
