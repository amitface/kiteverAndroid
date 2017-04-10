package sms19.inapp.msg;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kitever.android.R;
import com.kitever.app.context.BaseApplicationContext;
import com.kitever.app.context.CustomStyle;
import com.kitever.sendsms.fragments.SmsMailInterface;


import org.apache.commons.net.ftp.FTPReply;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.PrivacyItem;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import sms19.inapp.msg.asynctask.BroadCastDeleteAsyncTask;
import sms19.inapp.msg.asynctask.GetContactListAsyncTask;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.ContactUtil;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.database.DatabaseHelper;
import sms19.inapp.msg.model.Chatmodel;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.Groupmodel;
import sms19.inapp.msg.model.NewContactModelForFlag;
import sms19.inapp.msg.model.PhoneValidModel;
import sms19.inapp.msg.model.Recentmodel;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.Home;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class InAppMessageActivity extends AppCompatActivity implements
        OnClickListener {
    /***********************************************/

    // private LinearLayout mBackButton;
    private sms19.inapp.msg.CircularImageView mActionBarImage;
    private TextView sorting_title, mUserNameTitle, mUserStatusTitle;
    // private static boolean HIDE_MENU = false;
    private Menu mMenu, mMenu2;
    private MenuItem menuDoneItem;
    private LinearLayout camera_btn;
    private TextView actionbar_title;
    public static Handler Handler;
    private LinearLayout layout_name_status,tab_layout;
    private FrameLayout tab_bottom_container;

    private TextView mChatTabBtn, mContactTabBtn, mGroupTabBtn, mChatTabBtn1,
            mContactTabBtn1, mGroupBtn1;
    private LinearLayout layoutTab_contact_chat;

    public static SharedPreferences chatPrefs;
    public static Contactmodel myModel;

    private SearchView searchView;
    private MenuItem searchViewCollapse;
    public static String USER_ID = "";
    /*
     * private Timer timer; private MyTimerTask myTimerTask;
     */
    private boolean isExitGroupFlag = false;

    private DeleteContactFetch contactFetchAsyn = null;
    private updateregisterAndStragerGlobalArrayListAsyncTask andStragerGlobalArrayListAsyncTask = null;
    private ProgressDialog dialogBlock;
    private Block blockAyncTask = null;
    private UnBlock unBlockAyncTask = null;
    private ProgressBar progress_on_actionbar = null;
    private SmsMailInterface smsMailInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inapp_msg_activity);
        GlobalData.OnHomefrag = true;
        USER_ID = Utils.getUserId(InAppMessageActivity.this);
        SharedPreferences prfs = getSharedPreferences("profileData",
                Context.MODE_PRIVATE);
        String userLogin = prfs.getString("user_login", "");
        try {
            if (Utils.isDeviceOnline(InAppMessageActivity.this)) {
                Utils.GetBroadCastGroupList(USER_ID, userLogin + "@"
                        + GlobalData.HOST, InAppMessageActivity.this);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


        actionBarSetting();

        ErrorReporter errReporter = new ErrorReporter();
        errReporter.Init(this);
        errReporter.CheckErrorAndSendMail(this);

        new DataBaseDetails(this);

        // validNumberCheck("+61430496569");

        if (GlobalData.dbHelper == null) {
            try {
                GlobalData.dbHelper = new DatabaseHelper(
                        InAppMessageActivity.this, false);

            } catch (IOException e1) {

                e1.printStackTrace();
            }
        }

        myModel = GlobalData.dbHelper.getUserDatafromDB();

        ConstantFields.mydetail = myModel;
        chatPrefs = getSharedPreferences("chatPrefs", MODE_PRIVATE);

        tab_bottom_container = (FrameLayout) findViewById(R.id.tab_bottom_container);
        layoutTab_contact_chat = (LinearLayout) findViewById(R.id.layoutTab_contact_chat_inapp);
        tab_layout = (LinearLayout) findViewById(R.id.tab_layout);
        tab_layout.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));

        if (Home.connectionServiceObject != null) {
            Utils.currentTimeStap();

        }

        mChatTabBtn = (TextView) findViewById(R.id.tab_chat);
        mContactTabBtn = (TextView) findViewById(R.id.tab_contact);
        mGroupTabBtn = (TextView) findViewById(R.id.tab_groups);

        mChatTabBtn.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
        mContactTabBtn.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
        mGroupTabBtn.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));

        mChatTabBtn.setTextColor(Color.parseColor(CustomStyle.TAB_FONT_COLOR));
        mContactTabBtn.setTextColor(Color.parseColor(CustomStyle.TAB_FONT_COLOR));
        mGroupTabBtn.setTextColor(Color.parseColor(CustomStyle.TAB_FONT_COLOR));

        setRobotoThinFont(mChatTabBtn,this);
        setRobotoThinFont(mContactTabBtn,this);
        setRobotoThinFont(mGroupTabBtn,this);

        mChatTabBtn1 = (TextView) findViewById(R.id.tab_chat_1);
        mContactTabBtn1 = (TextView) findViewById(R.id.tab_contact_1);
        mGroupBtn1 = (TextView) findViewById(R.id.tab_group_1);

        mChatTabBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_INDICATOR));
        mContactTabBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
        mGroupBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));


        mChatTabBtn.setOnClickListener(this);
        mContactTabBtn.setOnClickListener(this);
        mGroupTabBtn.setOnClickListener(this);

        layoutTab_contact_chat.setVisibility(View.VISIBLE);

        callFragmentWithOutAddBack(new ChatFragment(),
                ConstantFlag.TAB_CHAT_FRAGMENT);
        setHandler();

        handleIntent(getIntent());

		/*
         * timer = new Timer(); myTimerTask = new MyTimerTask();
		 * timer.schedule(myTimerTask, 1000, 10000);
		 */

        boolean isFtpFolder = Utils
                .getIsCreateFtpFolder(InAppMessageActivity.this);
        if (!isFtpFolder) {
            if (Utils.isDeviceOnline(InAppMessageActivity.this)) {

                if (!(GlobalData.FTP_URL.equalsIgnoreCase("")
                        || GlobalData.FTP_USER.equalsIgnoreCase("")
                        || GlobalData.FTP_PASS.equalsIgnoreCase("") || GlobalData.FTP_HOST
                        .equalsIgnoreCase(""))) {

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            ftpMakeDirectory("");
                        }
                    }).start();
                }
            }
        }

        // GetContactstatusandAvatarMethod();

		/*
         * getContentResolver() .registerContentObserver(
		 * ContactsContract.Contacts.CONTENT_URI, true, new
		 * MyCOntentObserver());
		 */
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
                        if (ContactUtil.isInsertContactRunning) {
                            progress_on_actionbar.setVisibility(View.VISIBLE);
                        } else {
                            progress_on_actionbar.setVisibility(View.GONE);
                        }
                    }

                    if (TYPE == com.kitever.utils.Utils.CONTACTS_ADD_UPDATE) {
                        refreshContactList();
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
        Utils.msgCount = 0;
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(
                        mMessageReceiver,
                        new IntentFilter(
                                com.kitever.utils.Utils.CONTACTS_UPDATE_ACTION));
        super.onResume();
        updateregisterAndStragerGlobalArrayListAsyncTaskMethod();
    }

    public void backPress() {

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (SingleChatRoomFrgament.newInstance("") != null && SingleChatRoomFrgament.newInstance("").isEmojiVisible())
            SingleChatRoomFrgament
                    .newInstance("").hideEmoji();
        else {
            try {
                SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                        .newInstance("");
                if (chatRoomFrgament != null) {
                    chatRoomFrgament.killEmoji();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            super.onBackPressed();
        }
    }

    //ActionBar
    public void actionBarSetting() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor(CustomStyle.HEADER_BACKGROUND)));

        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        View view = getLayoutInflater().inflate(R.layout.custon_actionbar_view,
                null, false);
        // mBackButton = (LinearLayout)
        // view.findViewById(R.id.action_bat_back_btn);
        mActionBarImage = (sms19.inapp.msg.CircularImageView) view
                .findViewById(R.id.profile_image);
        sorting_title = (TextView) view.findViewById(R.id.sorting_title);
        mUserNameTitle = (TextView) view.findViewById(R.id.name);
        progress_on_actionbar = (ProgressBar) view
                .findViewById(R.id.progress_on_actionbar);

        if (ContactUtil.isInsertContactRunning) {
            progress_on_actionbar.setVisibility(View.VISIBLE);
        } else {
            progress_on_actionbar.setVisibility(View.GONE);
        }

        camera_btn = (LinearLayout) view
                .findViewById(R.id.chat_addfilebuttonlay);

        mUserStatusTitle = (TextView) view.findViewById(R.id.status);

        actionbar_title = (TextView) view.findViewById(R.id.actionbar_title);

        actionbar_title.setVisibility(View.VISIBLE);
        // mBackButton.setOnClickListener(this);
        layout_name_status = (LinearLayout) view
                .findViewById(R.id.layout_name_status);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.RIGHT);
        params.setMargins(0, 0, 0, 0);

        getSupportActionBar().setCustomView(view, params);

        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        setRobotoThinFont(actionbar_title,this);
        actionbar_title.setText("Chats");
        actionbar_title.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));

        final Drawable drawable = mActionBarImage.getDrawable();
        /*mActionBarImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.imagePopup(InAppMessageActivity.this,drawable);
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        BaseApplicationContext application = (BaseApplicationContext) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName(getString(R.string.app_name)+"- Chat Page");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.inapp_msg_menu, menu);
        mMenu2 = menu;

        searchViewCollapse = mMenu2.findItem(R.id.menu_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) mMenu2.findItem(R.id.menu_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                searchHandle(arg0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                searchHandle(arg0);
                return false;
            }
        });

		/*
         * searchView.setOnQueryTextFocusChangeListener(new
		 * View.OnFocusChangeListener() {
		 * 
		 * @SuppressLint("NewApi")
		 * 
		 * @Override public void onFocusChange(View view, boolean
		 * queryTextFocused) { if(!queryTextFocused) {
		 * if(searchViewCollapse!=null){
		 * searchViewCollapse.collapseActionView(); } } } });
		 */

        MenuItem myMenuItem = menu.findItem(R.id.action_m);
        mMenu = myMenuItem.getSubMenu();

        if (ConstantFields.HIDE_MENU == 0) { // Hide 0 mean show option menu
            // only home page

            for (int i = 0; i < mMenu.size(); i++) {

                MenuItem icon = mMenu.findItem(R.id.sort_by_mostcontact);
                ChatFragment fragment = ChatFragment.newInstance("");

                if (fragment != null) {
                    if (fragment.getCurrent_sort() == 0) {
                        sorting_title.setVisibility(View.VISIBLE);
                        if (fragment.isSortByMostContected()) {
                            sorting_title.setText("Sort Most Contacted Z-A");

                        } else {
                            sorting_title.setText("Sort Most Contacted A-Z");
                        }
                    } else if (fragment.getCurrent_sort() == 1) {

                        sorting_title.setVisibility(View.VISIBLE);
                        if (!fragment.isSortByRecent()) {
                            sorting_title.setText("Sort Recent A-Z");

                        } else {
                            sorting_title.setText("Sort Recent Z-A");
                        }

                    } else if (fragment.getCurrent_sort() == 2) {
                        sorting_title.setVisibility(View.VISIBLE);
                        if (fragment.isSortByName()) {
                            sorting_title.setText("Sort Name Z-A");

                        } else {
                            sorting_title.setText("Sort Name A-Z");
                        }
                    }

                }

                if (mMenu.getItem(i).getItemId() == R.id.contact_search) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_contact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_groups) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_alphabets) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_number) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_app_user) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_nonapp_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.group_info) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.search_chatpage) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.delete_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.exit_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.view_contact_single_user) {// single
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.search_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.block_contact_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                }

            }
        } else if (ConstantFields.HIDE_MENU == 77) {

            for (int i = 0; i < mMenu.size(); i++) {

                if (mMenu.getItem(i).getItemId() == R.id.contact_search) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_recent) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_name) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_mostcontact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_contact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_groups) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_alphabets) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_number) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_app_user) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_nonapp_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.group_info) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.search_chatpage) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.delete_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.exit_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.view_contact_single_user) {// single
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.search_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.block_contact_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                }
            }

        } else if (ConstantFields.HIDE_MENU == 1) {

            if (mMenu2.getItem(0).getItemId() == R.id.menu_search) {// group
                // user chat
                // page menu
                mMenu2.getItem(0).setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this);

            }
            for (int i = 0; i < mMenu.size(); i++) {

                SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                        .newInstance("");
                if (chatRoomFrgament != null) {

                    int isuserblock = 0;
                    if (chatRoomFrgament.getRemote_jid() != null) {
                        if (GlobalData.dbHelper.isContactBlock(chatRoomFrgament
                                .getRemote_jid())) {
                            isuserblock = 1;
                        } else {
                            isuserblock = 0;
                        }
                    }

                    if (isuserblock == 0) {
                        MenuItem bedMenuItem = mMenu
                                .findItem(R.id.block_contact_single_user);
                        bedMenuItem.setTitle("Block Contact");
                    } else {
                        MenuItem bedMenuItem = mMenu
                                .findItem(R.id.block_contact_single_user);
                        bedMenuItem.setTitle("Unblock Contact");
                    }
                }

                if (mMenu.getItem(i).getItemId() == R.id.contact_search) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_contact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_groups) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_alphabets) {

                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_number) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.menu_search) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_app_user) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_nonapp_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.group_info) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.search_chatpage) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.delete_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.exit_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.create_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.create_broadcast) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_recent) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_name) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_mostcontact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.settings) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.refresh) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else {
                    mMenu.getItem(i).setVisible(true);
                    ActivityCompat.invalidateOptionsMenu(this);
                }

            }

        } else if (ConstantFields.HIDE_MENU == 2) {

            for (int i = 0; i < mMenu.size(); i++) {
                if (mMenu.getItem(i).getItemId() == R.id.contact_search) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_contact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_groups) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_alphabets) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_number) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_app_user) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_nonapp_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                }

                if (mMenu.getItem(i).getItemId() == R.id.view_contact_single_user) {// single
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.search_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.block_contact_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.create_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.create_broadcast) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_recent) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_name) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_mostcontact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.settings) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.refresh) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else {
                    mMenu.getItem(i).setVisible(true);
                    ActivityCompat.invalidateOptionsMenu(this);
                }

            }

        } else if (ConstantFields.HIDE_MENU == 3) {

            if (mMenu2.getItem(0).getItemId() == R.id.menu_search) {// group
                // user chat
                // page menu
                mMenu2.getItem(0).setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this);

            }
            if (mMenu2.getItem(1).getItemId() == R.id.action_m) {// group user
                // chat page
                // menu
                mMenu2.getItem(1).setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this);

            }

            for (int i = 0; i < mMenu.size(); i++) {
                mMenu.getItem(i).setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this);
            }
        } else if (ConstantFields.HIDE_MENU == 4) { // contact and groups tab
            // page menu

            for (int i = 0; i < mMenu.size(); i++) {

                MenuItem icon = mMenu.findItem(R.id.contact_sort_by_alphabets);
                ContactFragment fragment = ContactFragment.newInstance("");

                if (fragment != null) {
                    if (fragment.getCurrent_sort() == 0) {
                        sorting_title.setVisibility(View.VISIBLE);
                        if (fragment.isSortbyname_both_new()) {
                            sorting_title.setText("Sort Name A-Z");

                        } else {
                            sorting_title.setText("Sort Name Z-A");
                        }
                    } else if (fragment.getCurrent_sort() == 1) {

                        sorting_title.setVisibility(View.VISIBLE);
                        if (!fragment.isSortbynumber_appuser()) {
                            sorting_title.setText("Sort Number Ase");

                        } else {
                            sorting_title.setText("Sort Number Dse");
                        }

                    } else if (fragment.getCurrent_sort() == 2) {
                        sorting_title.setVisibility(View.VISIBLE);
                        if (fragment.getIS_USER_TYPE() == 1) {
                            sorting_title.setText("App Users");

                        } else {
                            sorting_title.setText("Non App Users");
                        }
                    }

                }

                if (mMenu.getItem(i).getItemId() == R.id.contact_search) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.create_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_recent) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_name) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_mostcontact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.settings) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.group_info) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.search_chatpage) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.delete_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.exit_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.view_contact_single_user) {// single
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.search_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.block_contact_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                }

            }

        } else if (ConstantFields.HIDE_MENU == 6) { // contact and groups tab
            // page menu

            if (mMenu2.getItem(0).getItemId() == R.id.menu_search) {// group
                // user chat
                // page menu
                mMenu2.getItem(0).setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this);

            }
            for (int i = 0; i < mMenu.size(); i++) {

                SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                        .newInstance("");
                if (chatRoomFrgament != null) {
                    if (chatRoomFrgament.getGroupModel().getCreated_me()
                            .equalsIgnoreCase("0")) {

                        if (GlobalData.dbHelper
                                .groupIsBlockNew(chatRoomFrgament
                                        .getRemote_jid())) {
                            if (mMenu.getItem(i).getItemId() == R.id.exit_group) {
                                mMenu.getItem(i).setVisible(false);
                                ActivityCompat.invalidateOptionsMenu(this);
                            }

                        } else {
                            if (mMenu.getItem(i).getItemId() == R.id.delete_group) {
                                mMenu.getItem(i).setVisible(false);
                                ActivityCompat.invalidateOptionsMenu(this);
                            }
                        }

                    }
                }

                if (mMenu.getItem(i).getItemId() == R.id.contact_search) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_contact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_groups) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_alphabets) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_number) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.menu_search) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_app_user) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_nonapp_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.create_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.create_broadcast) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_recent) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_name) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_mostcontact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.settings) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.view_contact_single_user) {// single
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.search_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.block_contact_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.refresh) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                }

            }

        } else if (ConstantFields.HIDE_MENU == 88) { // contact and groups tab
            // page menu

            if (mMenu2.getItem(0).getItemId() == R.id.menu_search) {// group
                // user chat
                // page menu
                mMenu2.getItem(0).setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this);

            }

            for (int i = 0; i < mMenu.size(); i++) {

                SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                        .newInstance("");
                if (chatRoomFrgament != null) {
                    if (chatRoomFrgament.getGroupModel().getCreated_me()
                            .equalsIgnoreCase("0")) {
                        if (mMenu.getItem(i).getItemId() == R.id.delete_group) {
                            mMenu.getItem(i).setVisible(false);
                            ActivityCompat.invalidateOptionsMenu(this);
                        }
                    }
                }

                if (mMenu.getItem(i).getItemId() == R.id.exit_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_search) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_contact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_new_groups) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_alphabets) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.contact_sort_by_number) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.menu_search) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_app_user) {// group
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);

                } else if (mMenu.getItem(i).getItemId() == R.id.contact_and_group_nonapp_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.create_group) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.create_broadcast) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_recent) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_name) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.sort_by_mostcontact) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.settings) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.view_contact_single_user) {// single
                    // user
                    // chat
                    // page
                    // menu
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.search_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.block_contact_single_user) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                } else if (mMenu.getItem(i).getItemId() == R.id.refresh) {
                    mMenu.getItem(i).setVisible(false);
                    ActivityCompat.invalidateOptionsMenu(this);
                }

            }

        } else if (ConstantFields.HIDE_MENU == 83) {
            /*For Search in GroupAdduserSelection_2*/

            if (mMenu2.getItem(1).getItemId() == R.id.action_m) {// group user
                // chat page
                // menu
                mMenu2.getItem(1).setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this);
            }

            for (int i = 0; i < mMenu.size(); i++) {
                mMenu.getItem(i).setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this);
            }
            return true;
        } else if (ConstantFields.HIDE_MENU == 84) {
            /*For Search in CreateBroadCast.java*/

            if (mMenu2.getItem(1).getItemId() == R.id.action_m) {// group user
                // chat page
                // menu
                mMenu2.getItem(1).setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this);
            }

            for (int i = 0; i < mMenu.size(); i++) {
                mMenu.getItem(i).setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this);
            }
            return true;
        } else {
            for (int i = 0; i < mMenu.size(); i++)
                mMenu.getItem(i).setVisible(true);
        }

        return true;
    }

    public void invalidateOptionMenuItem() {
        ActivityCompat.invalidateOptionsMenu(this);
    }

    @SuppressLint("NewApi")
    public void collapseSearch() {
        if (searchViewCollapse != null) {
            searchViewCollapse.collapseActionView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_search) {
            this.menuDoneItem = item;
            menuDoneItem.setVisible(false);
            ActivityCompat.invalidateOptionsMenu(this);
            return true;
        } else {
            collapseSearch();
        }

        if (id == R.id.block_contact_single_user) {
            SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                    .newInstance("");
            if (chatRoomFrgament != null) {
                int isuserblock = 0;
                if (chatRoomFrgament.getRemote_jid() != null) {
                    if (GlobalData.dbHelper.isContactBlock(chatRoomFrgament
                            .getRemote_jid())) {
                        isuserblock = 1;
                    } else {
                        isuserblock = 0;
                    }
                }
                if (!chatRoomFrgament.getRemote_jid().equalsIgnoreCase("")) {
                    if (isuserblock == 0) {

                        chatRoomFrgament.blockContact(chatRoomFrgament
                                .getRemote_jid());
                    } else {
                        chatRoomFrgament.unblockContact(chatRoomFrgament
                                .getRemote_jid());
                    }
                }
            }

            return true;
        } else if (id == R.id.sort_by_mostcontact) {
            ChatFragment chatFragment = ChatFragment.newInstance("");
            if (chatFragment != null) {
                chatFragment.sortByMostContected();
            }

            invalidateOptionMenuItem();
            return true;
        } else if (id == R.id.contact_new_contact) {

            ContactFragment contactFragment = ContactFragment.newInstance("");
            if (contactFragment != null) {
                contactFragment.setAddContact();
            }

            return true;

        } else if (id == R.id.delete_group) {
            SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                    .newInstance("");
            if (chatRoomFrgament != null) {
                if (!chatRoomFrgament.getRemote_jid().equalsIgnoreCase("")) {
                    if (GlobalData.connection != null
                            && GlobalData.connection.isConnected()
                            && GlobalData.connection.isAuthenticated()) {
                        try {
                            if (chatRoomFrgament.getIsgroup() == 1) {
                                chatRoomFrgament.deleteGroupChat(
                                        GlobalData.connection, chatRoomFrgament
                                                .getRemote_jid().trim(),
                                        myModel.getRemote_jid());
                            } else if (chatRoomFrgament.getIsgroup() == 2) {
                                chatRoomFrgament.deleteChat(chatRoomFrgament
                                        .getRemote_jid());
                            }
                        } catch (XMPPException e) {

                            e.printStackTrace();
                        }
                    }
                }
            }

            return true;
        } else if (id == R.id.search_chatpage) {
            SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                    .newInstance("");
            if (chatRoomFrgament != null) {
                actionBarHide();
                chatRoomFrgament.getSearch_header().setVisibility(View.VISIBLE);
            }

            return true;
        } else if (R.id.search_single_user == id) {
            SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                    .newInstance("");
            if (chatRoomFrgament != null) {
                actionBarHide();
                chatRoomFrgament.getSearch_header().setVisibility(View.VISIBLE);

            }
        } else if (id == R.id.exit_group) {
            SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                    .newInstance("");
            if (chatRoomFrgament != null) {
                if (!chatRoomFrgament.getRemote_jid().equalsIgnoreCase("")) {
                    if (GlobalData.connection != null
                            && GlobalData.connection.isConnected()
                            && GlobalData.connection.isAuthenticated()) {
                        try {
                            chatRoomFrgament.exitDialog(
                                    chatRoomFrgament.getRemote_jid(),
                                    myModel.getRemote_jid());
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                }
            }

            return true;
        } else if (id == R.id.sort_by_name) {

            ChatFragment chatRoomFrgament = ChatFragment.newInstance("");
            if (chatRoomFrgament != null) {
                chatRoomFrgament.sortByName();
            }
            invalidateOptionMenuItem();

            return true;
        } else if (id == R.id.refresh) {

            if (ConstantFields.HIDE_MENU == 77) {

                GroupListFrgament frgament = GroupListFrgament.newInstance("");
                if (frgament != null) {
                    // frgament.adapterSet();
                    // frgament.AdapterRefreshWithAsync();
                    frgament.callHandler(true);
                }

            } else {
                if (ConstantFields.HIDE_MENU == 4) {
                    progress_on_actionbar.setVisibility(View.VISIBLE);
                    DeleteContactFetchMethod();// 07apl
                } else if (ConstantFields.HIDE_MENU == 0) {
                    // invalidateOptionMenuItem();
                }
            }
            return true;
        } else if (id == R.id.sort_by_recent) {

            ChatFragment chatRoomFrgament = ChatFragment.newInstance("");
            if (chatRoomFrgament != null) {
                chatRoomFrgament.sortByRecent();
            }
            invalidateOptionMenuItem();

            return true;
        } else if (id == R.id.view_contact_single_user) {
            SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                    .newInstance("");
            if (chatRoomFrgament != null) {
                chatRoomFrgament.callSingleUserProfile();
            }

            return true;
        } else if (id == R.id.group_info) {
            SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                    .newInstance("");
            if (chatRoomFrgament != null) {
                chatRoomFrgament.callSingleUserProfile();
            }

            return true;
        } else if (id == R.id.contact_and_group_app_user) {

            ContactFragment contactFragment = ContactFragment.newInstance("");
            if (contactFragment != null) {
                contactFragment.appUserList(1);
            }
            ContactFragment.BROAD_CAST_FLAG = 2;
            invalidateOptionMenuItem();
            return true;
        } else if (id == R.id.contact_and_group_nonapp_user) {

            ContactFragment contactFragment = ContactFragment.newInstance("");
            if (contactFragment != null) {
                contactFragment.nonAppUserList();
                // contactFragment.refrsh();
            }
            invalidateOptionMenuItem();
            ContactFragment.BROAD_CAST_FLAG = 3;
            return true;
        } else if (id == R.id.contact_and_group_app_user) {

            ContactFragment contactFragment = ContactFragment.newInstance("");
            if (contactFragment != null) {
                contactFragment.appUserList(1);
            }
            invalidateOptionMenuItem();
            return true;
        } else if (id == R.id.contact_sort_by_alphabets) {

            ContactFragment contactFragment = ContactFragment.newInstance("");
            if (contactFragment != null) {
                contactFragment.sortByName(contactFragment.getChatAdapter()
                        .getContactArrayList());
                contactFragment.getChatAdapter().notifyDataSetChanged();
            }

            invalidateOptionMenuItem();

            return true;

        } else if (id == R.id.contact_sort_by_number) {

            ContactFragment contactFragment = ContactFragment.newInstance("");
            if (contactFragment != null) {
                contactFragment.sortByNumber(contactFragment.getChatAdapter()
                        .getContactArrayList());
                contactFragment.getChatAdapter().notifyDataSetChanged();

            }
            invalidateOptionMenuItem();
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.contact_new_groups) {

            callFragmentWithAddBack(new CreateGroupChat(), "CreateGroupChat");

            return true;
        } else if (id == R.id.create_group) {
            callFragmentWithAddBack(new CreateGroupChat(), "CreateGroupChat");
            return true;

        } else if (id == R.id.settings) {
            callFragmentWithAddBack(new Settings(), "Settings");
            return true;
        } else if (id == R.id.create_broadcast) {
            callFragmentWithAddBack(CreateBroadCast.getInstance("broadcast"), "CreateBroadCast");
            return true;

        } else if (id == R.id.group_info) {
            Toast.makeText(getApplicationContext(), "group_info",
                    Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.contact_and_group_app_user) {
            ContactFragment contactFragment = ContactFragment.newInstance("");
            ArrayList<Contactmodel> arrayList = new ArrayList<Contactmodel>();
            if (contactFragment != null) {
                if (contactFragment.contactlist != null) {
                    for (int i = 0; i < contactFragment.contactlist.size(); i++) {
                        if (!contactFragment.contactlist.get(i).getRemote_jid()
                                .equalsIgnoreCase("")) {
                            arrayList.add(contactFragment.contactlist.get(i));
                        }
                    }
                    contactFragment.chatAdapter.setContactArrayList(arrayList);
                    contactFragment.chatAdapter.notifyDataSetChanged();
                }
            }

            return true;

        } else if (id == R.id.contact_and_group_nonapp_user) {
            ContactFragment contactFragment = ContactFragment.newInstance("");
            ArrayList<Contactmodel> arrayList = new ArrayList<Contactmodel>();
            if (contactFragment != null) {
                if (contactFragment.contactlist != null) {
                    for (int i = 0; i < contactFragment.contactlist.size(); i++) {
                        if (contactFragment.contactlist.get(i).getRemote_jid()
                                .equalsIgnoreCase("")) {
                            arrayList.add(contactFragment.contactlist.get(i));
                        }
                    }
                    contactFragment.chatAdapter.setContactArrayList(arrayList);
                    contactFragment.chatAdapter.notifyDataSetChanged();
                }
            }
            return true;
        } else if (id == android.R.id.home) {

            backPress();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {

        collapseSearch();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ChatFragment chatFragment = ChatFragment.newInstance("");
        ContactFragment contactFragment = ContactFragment.newInstance("");
        GroupListFrgament groupListFrgament = GroupListFrgament.newInstance("");

        if (mChatTabBtn == v) {

            // sorting_title.setVisibility(View.GONE);
            // sorting_title.setText("");

            mChatTabBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_INDICATOR));
            mContactTabBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
            mGroupBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));

            if (chatFragment.isAdded()) {
                if (!chatFragment.isVisible()) {
                    chatFragment.refreshChatAdapter();
                    chatFragment.customonCreate();
                    ft.show(chatFragment);
                }
            } else {
                callFragmentWithOutAddBack(new ChatFragment(),
                        ConstantFlag.TAB_CHAT_FRAGMENT);
            }

            // Hide fragment contactFragment
            if (contactFragment != null) {
                if (contactFragment.isAdded()) {
                    ft.hide(contactFragment);

                }
            }
            // Hide fragment groupListFrgament
            if (groupListFrgament != null) {
                if (groupListFrgament.isAdded()) {
                    ft.hide(groupListFrgament);

                }
            }
            // Commit changes
            // ft.commit();
            ft.commitAllowingStateLoss();

        }

        if (mContactTabBtn == v) {

            // sorting_title.setVisibility(View.GONE);
            // sorting_title.setText("");

            mChatTabBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
            mContactTabBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_INDICATOR));
            mGroupBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));

            if (contactFragment != null) {
                if (contactFragment.isAdded()) {
                    if (!contactFragment.isVisible()) {
                        contactFragment.customonCreate();
                        contactFragment.refrsh();
                        ft.show(contactFragment);

                    }
                } else {
                    callFragmentWithOutAddBack(new ContactFragment(),
                            ConstantFlag.TAB_CONTACHT_FRAGMENT);
                }
            } else {
                callFragmentWithOutAddBack(new ContactFragment(),
                        ConstantFlag.TAB_CONTACHT_FRAGMENT);
            }

            // Hide fragment chatFragment
            if (chatFragment != null) {
                if (chatFragment.isAdded()) {

                    ft.hide(chatFragment);

                }
            }
            // Hide fragment groupListFrgament
            if (groupListFrgament != null) {
                if (groupListFrgament.isAdded()) {

                    ft.hide(groupListFrgament);

                }
            }
            // Commit changes
            // ft.commit();
            ft.commitAllowingStateLoss();

        }
        if (mGroupTabBtn == v) {

            sorting_title.setVisibility(View.GONE);
            sorting_title.setText("");

            mChatTabBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
            mContactTabBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
            mGroupBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_INDICATOR));

            if (groupListFrgament != null) {
                if (groupListFrgament.isAdded()) {

                    if (!groupListFrgament.isVisible()) {
                        groupListFrgament.customonCreate();
                        ft.show(groupListFrgament);
                        groupListFrgament.callHandler(true);
                    }

                } else {
                    callFragmentWithOutAddBack(new GroupListFrgament(),
                            "GroupListFrgament");
                }

            } else {
                callFragmentWithOutAddBack(new GroupListFrgament(),
                        "GroupListFrgament");
            }

            // Hide fragment chatFragment
            if (chatFragment != null) {
                if (chatFragment.isAdded()) {

                    ft.hide(chatFragment);

                }
            }
            // Hide fragment groupListFrgament
            if (contactFragment != null) {
                if (contactFragment.isAdded()) {

                    ft.hide(contactFragment);

                }
            }
            // Commit changes
            // ft.commit();
            ft.commitAllowingStateLoss();

        }
        if (android.R.id.home == v.getId()) {
            try {
                SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                        .newInstance("");
                if (chatRoomFrgament != null) {
                    chatRoomFrgament.killEmoji();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            onBackPressed();
        }

    }

    private void refreshContactList() {

        collapseSearch();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ChatFragment chatFragment = ChatFragment.newInstance("");
        ContactFragment contactFragment = ContactFragment.newInstance("");
        GroupListFrgament groupListFrgament = GroupListFrgament.newInstance("");

        mChatTabBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
        mContactTabBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_INDICATOR));
        mGroupBtn1.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));

        if (contactFragment != null) {
            if (contactFragment.isAdded()) {
                if (!contactFragment.isVisible()) {
                    contactFragment.customonCreate();
                    contactFragment.refrsh();
                    ft.show(contactFragment);
                } else {
                    contactFragment.customonCreate();
                    contactFragment.refrsh();
                }
            } else {
                callFragmentWithOutAddBack(new ContactFragment(),
                        ConstantFlag.TAB_CONTACHT_FRAGMENT);
            }
        } else {
            callFragmentWithOutAddBack(new ContactFragment(),
                    ConstantFlag.TAB_CONTACHT_FRAGMENT);
        }

        // Hide fragment chatFragment
        if (chatFragment != null) {
            if (chatFragment.isAdded()) {

                ft.hide(chatFragment);

            }
        }
        // Hide fragment groupListFrgament
        if (groupListFrgament != null) {
            if (groupListFrgament.isAdded()) {

                ft.hide(groupListFrgament);

            }
        }
        // Commit changes
        // ft.commit();
        ft.commitAllowingStateLoss();

    }

    public void callFragmentWithAddBack(Fragment fragment, String tag) {

        getSorting_title().setVisibility(View.GONE);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        if (fragmentManager.findFragmentByTag(tag) == null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.tab_bottom_container, fragment, tag);
            transaction.addToBackStack(null);
            // transaction.commit();
            transaction.commitAllowingStateLoss();
        }
    }

    public void callFragmentWithOutAddBack(Fragment fragment, String tag) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.tab_bottom_container, fragment, tag);
        // transaction.commit();
        transaction.commitAllowingStateLoss();
    }

    public void toastMsg(String msg) {

        Toast.makeText(InAppMessageActivity.this, msg, Toast.LENGTH_SHORT)
                .show();
    }

    public void hideMenuMethod() {

        if (mMenu != null) {
            if (ConstantFields.HIDE_MENU == 0) {
                for (int i = 0; i < mMenu.size(); i++)
                    mMenu.getItem(i).setVisible(false);
                ActivityCompat.invalidateOptionsMenu(this);
            }
        }
    }

    public LinearLayout getLayoutTab_contact_chat() {
        return layoutTab_contact_chat;
    }

    public TextView getActionbar_title() {
        return actionbar_title;
    }

    public LinearLayout getCamera_btn() {
        return camera_btn;
    }

    public LinearLayout getLayout_name_status() {
        return layout_name_status;
    }

    public void groupActionBarControlIsVisual() {
        getLayout_name_status().setVisibility(View.GONE);

    }

    public void onBothTabPageControlIsGone() {

        getLayout_name_status().setVisibility(View.GONE);
        mActionBarImage.setVisibility(View.GONE);
        actionbar_title.setVisibility(View.VISIBLE);
    }

    public sms19.inapp.msg.CircularImageView getmActionBarImage() {
        return mActionBarImage;
    }

    public TextView getmUserNameTitle() {
        return mUserNameTitle;
    }

    public TextView getmUserStatusTitle() {
        return mUserStatusTitle;
    }

    public void removeCustomActionBarView() {

        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setCustomView(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    public void addGroupmember_frag() {

        CreateGroupChat addName_1 = CreateGroupChat.getInstance();
        if (addName_1 != null) {

            try {
                String groupname = addName_1.getGroupname_et().getText()
                        .toString();

                boolean isGroup = GlobalData.dbHelper.isGroup(groupname.trim());

                if (!isGroup) {

                    if (groupname != null && groupname.length() != 0) {
                        String mynumber = chatPrefs.getString("userNumber", "");
                        String gid = groupname + mynumber + "@conference."
                                + GlobalData.HOST;
                        if (GlobalData.dbHelper.checkGroupisAlreadyexsit(gid)) {
                            Toast.makeText(getApplicationContext(),
                                    "Group name already exists",
                                    Toast.LENGTH_SHORT).show();
                        } else if (groupname.contains("@")) {
                            Toast.makeText(getApplicationContext(),
                                    "Please enter the valid group name!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Bundle data = new Bundle();
                            data.putString("groupname", groupname);
                            data.putString("grouppicpath",
                                    addName_1.grouppicPath);
                            GlobalData.Newgroup = true;
                            Fragment add_G_memberfrag = new GroupAddUserSelection_2();
                            add_G_memberfrag.setArguments(data);

                            callFragmentWithAddBack(add_G_memberfrag,
                                    "agm_frag");

                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Enter group name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InAppMessageActivity.this,
                            "Group name already exists", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }

    private void setHandler() {
        // TODO Auto-generated method stub
        Handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                for (int i = 0; i < getSupportFragmentManager()
                        .getBackStackEntryCount(); i++) {
                    getSupportFragmentManager().popBackStack();

                }

                try {

                    int b = msg.what;

                    if (b == 5) {
                        mChatTabBtn.performClick();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

                ChatFragment chatFragment = ChatFragment.newInstance("");
                if (chatFragment != null) {
                    chatFragment.refreshChatAdapter();
                }

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

		/*
         * if (timer!=null){ timer.cancel(); timer = null; }
		 */
    }

    public void updateregisterAndStragerGlobalArrayListAsyncTaskMethod() {
        if (andStragerGlobalArrayListAsyncTask == null) {
            andStragerGlobalArrayListAsyncTask = new updateregisterAndStragerGlobalArrayListAsyncTask();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                andStragerGlobalArrayListAsyncTask
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                andStragerGlobalArrayListAsyncTask.execute();
            }
        } else {
            andStragerGlobalArrayListAsyncTask.cancel(true);
            andStragerGlobalArrayListAsyncTask = null;
            andStragerGlobalArrayListAsyncTask = new updateregisterAndStragerGlobalArrayListAsyncTask();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                andStragerGlobalArrayListAsyncTask
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                andStragerGlobalArrayListAsyncTask.execute();
            }
        }

    }

    public void OfflineStatusThread() {
        Utils.offlineShowUser();
    }

    class updateregisterAndStragerGlobalArrayListAsyncTask extends
            AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<Contactmodel> arrayListNew = new ArrayList<Contactmodel>();
            arrayListNew.addAll((GlobalData.dbHelper
                    .getContactfromDBOnlyRegisterAndStrager()));
            GlobalData.registerAndStragerGlobalArrayList.clear();
            GlobalData.registerAndStragerGlobalArrayList.addAll(arrayListNew);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            if (!Utils.isDeviceOnline(InAppMessageActivity.this)) {

                OfflineStatusThread();

            } else {
                if (!(GlobalData.connection != null
                        && GlobalData.connection.isAuthenticated() && GlobalData.connection
                        .isConnected())) {
                    OfflineStatusThread();
                }
            }

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            @SuppressWarnings("unused")
            String query = intent.getStringExtra(SearchManager.QUERY);
            @SuppressWarnings("unused")
            String query2 = intent.getStringExtra(SearchManager.QUERY);

        }
    }

    // satyajit
    private synchronized void searchHandle(String intent) {

        ArrayList<Recentmodel> arrayListFiltered = new ArrayList<Recentmodel>();
        ArrayList<Contactmodel> arrayListFiltered2 = new ArrayList<Contactmodel>();
        ArrayList<Groupmodel> arrayListFiltered3 = new ArrayList<Groupmodel>();
        ArrayList<Contactmodel> arrayListFiltered4 = new ArrayList<Contactmodel>();// new

        if (ConstantFields.HIDE_MENU == 0) {

            ChatFragment chatFragment = ChatFragment.newInstance("");
            if (chatFragment != null) {

                ArrayList<Recentmodel> arrayList2 = ChatFragment
                        .getRecentlist();
                // arrayListFiltered=new ArrayList<Recentmodel>();

                // ///////////////////
                ArrayList<String> msgList = null;
                ArrayList<String> msgDateList = null;
                ArrayList<Chatmodel> chathistorylist = new ArrayList<Chatmodel>();
                // int historycount = 0;
                String remote_jid = null;
                String previousrowid = "0";
                String timeStap = Utils.currentTimeStap();
                // String str="";

                if (!intent.equalsIgnoreCase("")) {
                    SingleChatRoomFrgament.myModel = GlobalData.dbHelper
                            .getUserDatafromDB();
                    msgList = new ArrayList<String>();
                    msgDateList = new ArrayList<String>();
                    for (int i = 0; i < arrayList2.size(); i++) {
                        remote_jid = arrayList2.get(i).getRemote_jid();
                        if (chathistorylist != null
                                && chathistorylist.size() > 0) {
                            chathistorylist.clear();
                        }
                        chathistorylist.addAll(GlobalData.dbHelper
                                .getChathistoryfromDB(remote_jid,
                                        previousrowid, timeStap));
                        if (chathistorylist != null
                                && chathistorylist.size() > 0) {
                            for (int k = 0; k < chathistorylist.size(); k++) {
                                // str=chathistorylist.get(k).getMsgDate();
                                if (chathistorylist
                                        .get(k)
                                        .getChatmessage()
                                        .toLowerCase()
                                        .startsWith(
                                                (intent.toString())
                                                        .toLowerCase())
                                        || chathistorylist
                                        .get(k)
                                        .getChatmessage()
                                        .toLowerCase()
                                        .contains(
                                                (intent.toString())
                                                        .toLowerCase())) {
                                    // arrayList2.get(i).setLastmsg(chathistorylist.get(k).getChatmessage());
                                    msgList.add(chathistorylist.get(k)
                                            .getChatmessage());
                                    msgDateList.add(chathistorylist.get(k)
                                            .getMsgDate());
                                    arrayListFiltered.add(arrayList2.get(i));
                                }
                            }
                        }
                    }
                }

                // //////////////////

                // if(!intent.equalsIgnoreCase("")){
                // for(int i=0;i<arrayList2.size();i++){
                // if(arrayList2.get(i).getLastmsg()!=null){
                // if(arrayList2.get(i).getLastmsg().toLowerCase().startsWith((intent.toString()).toLowerCase())){
                // arrayListFiltered.add(arrayList2.get(i));
                // }else
                // if(arrayList2.get(i).getLastmsg().toLowerCase().contains((intent.toString()).toLowerCase())){
                // arrayListFiltered.add(arrayList2.get(i));
                // }else
                // if(arrayList2.get(i).getUsernumber()!=null&&Utils.removeCountryCode(arrayList2.get(i).getUsernumber(),InAppMessageActivity.this).contains((intent.toString()))){
                // arrayListFiltered.add(arrayList2.get(i));
                // }
                //
                //
                // }
                // }
                // }
                if (!intent.equalsIgnoreCase("")) {
                    chatFragment.getChatAdapter().setLastMsgAndDateList(
                            msgList, msgDateList);
                    chatFragment.getChatAdapter().setRecentArrayList(
                            arrayListFiltered);
                    chatFragment.getChatAdapter().notifyDataSetChanged();
                } else {
                    chatFragment.getChatAdapter().setLastMsgAndDateList(null,
                            null);
                    chatFragment.getChatAdapter().setRecentArrayList(
                            ChatFragment.getRecentlist());
                    chatFragment.getChatAdapter().notifyDataSetChanged();
                }
            }

        }

        if (ConstantFields.HIDE_MENU == 4) {

            ContactFragment chatFragment = ContactFragment.newInstance("");
            if (chatFragment != null) {

                ArrayList<Contactmodel> arrayList2 = ContactFragment
                        .getContactlist();
                arrayListFiltered2 = new ArrayList<Contactmodel>();
                arrayListFiltered4 = new ArrayList<Contactmodel>();
                if (!intent.equalsIgnoreCase("")) {
                    for (int i = 0; i < arrayList2.size(); i++) {
                        if (!arrayList2.get(i).getName().equalsIgnoreCase("")) {
                            if (arrayList2
                                    .get(i)
                                    .getName()
                                    .toLowerCase()
                                    .startsWith(
                                            (intent.toString()).toLowerCase())) {
                                arrayListFiltered2.add(arrayList2.get(i));
                            } else if (arrayList2
                                    .get(i)
                                    .getName()
                                    .toLowerCase()
                                    .contains/* startsWith */(
                                    (intent.toString()).toLowerCase())) {
                                arrayListFiltered4.add(arrayList2.get(i));
                            } else if ((arrayList2.get(i).getNumber()) != null) {

                                if (Utils.removeCountryCode(
                                        arrayList2.get(i).getNumber(),
                                        InAppMessageActivity.this).startsWith(
                                        (intent.toString()))) {
                                    arrayListFiltered2.add(arrayList2.get(i));
                                } else if (Utils.removeCountryCode(
                                        arrayList2.get(i).getNumber(),
                                        InAppMessageActivity.this).endsWith(
                                        intent.toString())) {
                                    arrayListFiltered4.add(arrayList2.get(i));
                                }
                            }
                        }
                    }

                    if (arrayListFiltered4.size() > 0) {
                        arrayListFiltered2.addAll(arrayListFiltered4);
                    }
                }
                if (!intent.equalsIgnoreCase("")) {

                    chatFragment.getChatAdapter().setContactArrayList(
                            arrayListFiltered2);

                    chatFragment.getChatAdapter().notifyDataSetChanged();

                } else {
                    chatFragment.getChatAdapter().setContactArrayList(
                            ContactFragment.getContactlist());
                    chatFragment.getChatAdapter().notifyDataSetChanged();
                }
            }
        }

        if (ConstantFields.HIDE_MENU == 77) {

            GroupListFrgament chatFragment = GroupListFrgament.newInstance("");
            if (chatFragment != null) {

                ArrayList<Groupmodel> arrayList2 = chatFragment.getRecentlist();
                arrayListFiltered3 = new ArrayList<Groupmodel>();
                if (!intent.equalsIgnoreCase("")) {
                    for (int i = 0; i < arrayList2.size(); i++) {

                        if (!arrayList2.get(i).getGroupname()
                                .equalsIgnoreCase("")) {
                            if (arrayList2
                                    .get(i)
                                    .getGroupname()
                                    .toLowerCase()
                                    ./* contains */startsWith(
                                    (intent.toString()).toLowerCase())) {
                                arrayListFiltered3.add(arrayList2.get(i));
                            }
                        }
                    }
                }
                if (!intent.equalsIgnoreCase("")) {
                    chatFragment.getChatAdapter().setRecentArrayList(
                            arrayListFiltered3);
                    chatFragment.getChatAdapter().notifyDataSetChanged();
                } else {
                    chatFragment.getChatAdapter().setRecentArrayList(
                            chatFragment.getRecentlist());
                    chatFragment.getChatAdapter().notifyDataSetChanged();
                }
            }
        }

        if (ConstantFields.HIDE_MENU == 83) {

            GroupAddUserSelection_2 groupFragment = (GroupAddUserSelection_2) getSupportFragmentManager().findFragmentByTag("agm_frag");
            if (groupFragment != null) {
                groupFragment.filter(intent.toString());
            }
        }

        if (ConstantFields.HIDE_MENU == 84) {

            CreateBroadCast broadcastFragment = (CreateBroadCast) getSupportFragmentManager().findFragmentByTag("CreateBroadCast");
            if (broadcastFragment != null) {
                broadcastFragment.filter(intent.toString());
            }
        }
    }

    public void DeleteContactFetchMethod() {

        if (contactFetchAsyn == null) {
            contactFetchAsyn = new DeleteContactFetch();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                contactFetchAsyn
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                contactFetchAsyn.execute();
            }
        } else {
            contactFetchAsyn.cancel(true);
            contactFetchAsyn = null;
            contactFetchAsyn = new DeleteContactFetch();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                contactFetchAsyn
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                contactFetchAsyn.execute();
            }

        }

    }

    public class DeleteContactFetch extends AsyncTask<Void, Void, Void> {
        String contact = "";

        @Override
        protected Void doInBackground(Void... params) {
            try {
                contact = ContactUtil.getDeviceContact(
                        InAppMessageActivity.this, chatPrefs);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (Utils.isDeviceOnline(InAppMessageActivity.this)) {

                    if (contact!=null && !contact.equalsIgnoreCase("null")) {
                        String userId = Utils
                                .getUserId(InAppMessageActivity.this);
                        if (GlobalData.getContactListAsyncTask == null) {
                            GlobalData.getContactListAsyncTask = new GetContactListAsyncTask(
                                    InAppMessageActivity.this, chatPrefs,
                                    userId, progress_on_actionbar)/*
                                                                 * (
																 * GetContactListAsyncTask
																 * ) new
																 * GetContactListAsyncTask
																 * (chatPrefs,
																 * userId
																 * ).execute()
																 */;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                GlobalData.getContactListAsyncTask
                                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                GlobalData.getContactListAsyncTask.execute();
                            }
                        } else {
                            GlobalData.getContactListAsyncTask.cancel(true);
                            GlobalData.getContactListAsyncTask = null;
                            GlobalData.getContactListAsyncTask = new GetContactListAsyncTask(
                                    InAppMessageActivity.this, chatPrefs,
                                    userId, progress_on_actionbar)/*
                                                                 * (
																 * GetContactListAsyncTask
																 * ) new
																 * GetContactListAsyncTask
																 * (chatPrefs,
																 * userId
																 * ).execute()
																 */;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                GlobalData.getContactListAsyncTask
                                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                GlobalData.getContactListAsyncTask.execute();
                            }
                        }
                    }
                }

                if (ConstantFields.HIDE_MENU == 4) {
                    if (ContactFragment.deleteContactHandler != null) {
                        android.os.Message msg = new android.os.Message();// comment
                        // m
                        Bundle b = new Bundle();
                        b.putString("remoteidnew", "");
                        msg.setData(b);
                        ContactFragment.deleteContactHandler.sendMessage(msg);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void actionBarHide() {

        getSupportActionBar().hide();

    }

    public void actionBarShow() {
        getSupportActionBar().show();

    }

    public boolean exitGroup(String groip_jidN, String user_jidN,
                             String newOwnerId, boolean isDelete) {

        Log.i("XMPP Chat Client", "User left chat room ");
        boolean isExit = true;

        if (Utils.isDeviceOnline(InAppMessageActivity.this)) {
            if (GlobalData.connection != null
                    && GlobalData.connection.isConnected()) {

                MultiUserChat groupChat = null;

                if (!ContactUtil.mucChatIs(groip_jidN)) {
                    groupChat = initeGroupChat(groip_jidN);
                    GlobalData.globalMucChat.put(groip_jidN, groupChat);
                } else {
                    groupChat = GlobalData.globalMucChat.get(groip_jidN);
                    if (!groupChat.isJoined()) {
                        groupChat = initeGroupChat(groip_jidN);
                    }
                }

                if (groupChat != null && groupChat.isJoined()) {
                    try {
                        try {
                            if (GlobalData.dbHelper
                                    .checkGroupiscreatedbyme(groip_jidN)) {

                                if (!newOwnerId.equalsIgnoreCase("")) {
                                    groupChat.grantOwnership(newOwnerId);// assign
                                    // ownership
                                    // to
                                    // other
                                    // user
                                    groupChat.revokeMembership(groip_jidN);// exit
                                    // itself
                                    GlobalData.dbHelper
                                            .updateGroupCreatedByMe(groip_jidN);
                                } else {
                                    // groupChat.revokeMembership(groip_jidN);//exit
                                    // itself
                                    groupChat.destroy("was group room", null);
                                    GlobalData.dbHelper
                                            .updateGroupCreatedByMe(groip_jidN);
                                }
                            }
                            GlobalData.dbHelper
                                    .updateGroupCreatedByMe(groip_jidN);
                            GlobalData.dbHelper
                                    .singleGroupContactBlockfromDB(groip_jidN);
                            GlobalData.dbHelper.groupBlocknewfromDB(groip_jidN);

                            if (isDelete) {

                                GlobalData.dbHelper
                                        .deleteParticularUserHistory(groip_jidN);
                                GlobalData.dbHelper
                                        .groupBlocknewfromDB(groip_jidN);
                                GlobalData.dbHelper
                                        .deleteGroupParticularrow(groip_jidN);
                                GlobalData.dbHelper
                                        .deleteRecentParticularrow(groip_jidN);
                                GlobalData.dbHelper
                                        .DeleteContactRemoteIdBase(groip_jidN);
                            }

                            groupChat.banUser(user_jidN, "");

                            groupChat.leave();

                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                groupChat.leave();
                            } catch (Exception e1) {

                                e1.printStackTrace();
                            }
                            groupChat.revokeMembership(user_jidN);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    GlobalData.dbHelper.updateGroupCreatedByMe(groip_jidN);
                    GlobalData.dbHelper
                            .singleGroupContactBlockfromDB(groip_jidN);
                    GlobalData.dbHelper.groupBlocknewfromDB(groip_jidN);

                    if (isDelete) {

                        GlobalData.dbHelper
                                .deleteParticularUserHistory(groip_jidN);
                        GlobalData.dbHelper.groupBlocknewfromDB(groip_jidN);
                        GlobalData.dbHelper
                                .deleteGroupParticularrow(groip_jidN);
                        GlobalData.dbHelper
                                .deleteRecentParticularrow(groip_jidN);
                        GlobalData.dbHelper
                                .DeleteContactRemoteIdBase(groip_jidN);

                    }

                    isExit = false;
                }

                GroupListFrgament frgament = GroupListFrgament.newInstance("");
                if (frgament != null) {
                    frgament.refreshChatAdapter();
                }

                ChatFragment fChatFragment = ChatFragment.newInstance("");
                if (fChatFragment != null) {
                    fChatFragment.refreshChatAdapter();
                }

            } else {
                isExit = false;
                Toast.makeText(InAppMessageActivity.this,
                        "Not connected to server", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(InAppMessageActivity.this,
                    "Check your network connection", Toast.LENGTH_SHORT).show();
            isExit = false;
        }
        return isExit;

    }

    public MultiUserChat initeGroupChat(String groip_jid) {
        MultiUserChat muc = null;

        if (GlobalData.connection != null
                && GlobalData.connection.isConnected()) {
            muc = new MultiUserChat(GlobalData.connection, groip_jid);
            try {
                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(0);
                try {

                    muc.join(myModel.getRemote_jid(), null, history,
                            SmackConfiguration.getPacketReplyTimeout());
                } catch (XMPPException e) {
                    e.printStackTrace();
                    muc = null;
                }

            } catch (Exception e) {
                muc = null;
                e.printStackTrace();
            }
        }

        return muc;

    }

    public boolean exitGroupWithDialog(final String groupJIdDailog,
                                       final String jidDailog, final String jidOwnerDailog,
                                       final String title, String msg) {

        isExitGroupFlag = false;
        new AlertDialog.Builder(InAppMessageActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isExitGroupFlag = false;
                    }
                })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (title
                                        .equalsIgnoreCase(GlobalData.DELETE_TITLE)) {
                                    isExitGroupFlag = exitGroup(groupJIdDailog,
                                            jidDailog, jidOwnerDailog, true);
                                } else {
                                    isExitGroupFlag = exitGroup(groupJIdDailog,
                                            jidDailog, jidOwnerDailog, false);
                                }
                            }
                        }).show();

        return isExitGroupFlag;

    }

    public boolean deleteBroadCastGroupORChatWithDialog(
            final String groupJIdDailog) {
        new AlertDialog.Builder(InAppMessageActivity.this)
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
                                    deleteChat(groupJIdDailog);

                                } catch (Exception e) {
                                    Utils.printLog("delete group failed");
                                    e.printStackTrace();
                                }

                            }
                        }).show();
        return true;

    }

    public void deleteChat(String remotejid) {

        try {

            if (Utils.isDeviceOnline(this)) {
                GlobalData.dbHelper.deleteParticularUserHistory(remotejid);
                GlobalData.dbHelper.deleteRecentParticularrow(remotejid);
                GlobalData.dbHelper.deleteGroupParticularrow(remotejid);
                GlobalData.dbHelper.updateContactmsgData(remotejid, "", "");
                broadCastDelete(remotejid, 0);

                GroupListFrgament frgament = GroupListFrgament.newInstance("");
                if (frgament != null) {
                    frgament.refreshChatAdapter();
                }

                ChatFragment fChatFragment = ChatFragment.newInstance("");
                if (fChatFragment != null) {
                    fChatFragment.refreshChatAdapter();
                }

            } else {
                Toast.makeText(InAppMessageActivity.this,
                        "Check your network connection", Toast.LENGTH_SHORT)
                        .show();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void blockUserWithPrivacy(String rid) {

        if (blockAyncTask == null) {
            blockAyncTask = new Block();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                blockAyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        rid);
            } else {
                blockAyncTask.execute(rid);
            }

        } else {
            blockAyncTask.cancel(true);
            blockAyncTask = null;

            blockAyncTask = new Block();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                blockAyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        rid);
            } else {
                blockAyncTask.execute(rid);
            }
        }

    }

    public void unBlockUserWithPrivacy(String rid) {

        if (unBlockAyncTask == null) {
            unBlockAyncTask = new UnBlock();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                unBlockAyncTask.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, rid);
            } else {
                unBlockAyncTask.execute(rid);
            }

        } else {
            unBlockAyncTask.cancel(true);
            unBlockAyncTask = null;

            unBlockAyncTask = new UnBlock();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                unBlockAyncTask.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, rid);
            } else {
                unBlockAyncTask.execute(rid);
            }
        }

    }

    class Block extends AsyncTask<String, String, String> {

        String remoteIdBlock = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogBlock = new ProgressDialog(InAppMessageActivity.this);
            dialogBlock.setMessage("blocking...");
            dialogBlock.setCancelable(false);
            dialogBlock.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                if (Utils.isDeviceOnline(InAppMessageActivity.this)) {
                    remoteIdBlock = params[0];
                    String listName = "newList";
                    List<PrivacyItem> privacyItems = new Vector<PrivacyItem>();

                    PrivacyItem itemP = null;
                    if (GlobalData.blockUserJid != null
                            && GlobalData.blockUserJid.size() > 0) {
                        GlobalData.blockUserJid.put(remoteIdBlock,
                                remoteIdBlock);
                    } else {
                        GlobalData.blockUserJid = new HashMap<String, String>();
                        GlobalData.blockUserJid.put(remoteIdBlock,
                                remoteIdBlock);
                    }

                    Iterator<Map.Entry<String, String>> itr1 = GlobalData.blockUserJid
                            .entrySet().iterator();
                    while (itr1.hasNext()) {

                        itemP = new PrivacyItem(
                                PrivacyItem.Type.jid.toString(), false, 8);
                        Map.Entry<String, String> entry = itr1.next();
                        itemP.setValue(entry.getKey());
                        itemP.setFilterIQ(false);
                        itemP.setFilterMessage(false);
                        itemP.setFilterPresence_in(false);
                        privacyItems.add(itemP);
                    }

                    try {

                        GlobalData.privacyManager.createPrivacyList(listName,
                                privacyItems);
                        GlobalData.privacyManager.setDefaultListName(listName);
                        GlobalData.privacyManager.setActiveListName(listName);
                        Utils.printLog("BLOCKED");

                        GlobalData.dbHelper
                                .singleContactBlockfromDB(remoteIdBlock);
                        Utils.printLog("List-"
                                + GlobalData.privacyManager.getActiveList()
                                .getItems().get(0).toXML());
                        GlobalData.dbHelper
                                .offlineStatusSet(remoteIdBlock, "0");

                        return "true";

                    } catch (XMPPException e) {
                        Utils.printLog("PRIVACY_ERROR: " + e);
                        return "false";
                    }
                } else {
                    return "no_internet";

                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return "false";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialogBlock.dismiss();
            try {
                if (result.equals("no_internet")) {
                    Toast.makeText(InAppMessageActivity.this,
                            "No internet connection", Toast.LENGTH_SHORT)
                            .show();
                }
                if (result.equals("true")) {
                    Toast.makeText(InAppMessageActivity.this, "user blocked",
                            Toast.LENGTH_SHORT).show();

                    if (ConstantFields.HIDE_MENU == 1) {
                        SingleChatRoomFrgament chatroom = SingleChatRoomFrgament
                                .newInstance("");
                        if (chatroom != null) {
                            chatroom.getmBlockBtn().setText("Unblock User");
                        }
                        invalidateOptionMenuItem();
                    }

                    if (ConstantFields.HIDE_MENU == 3) {

                        SingleChatRoomFrgament chatroom = SingleChatRoomFrgament
                                .newInstance("");
                        if (chatroom != null) {
                            chatroom.getmBlockBtn().setText("Unblock User");
                        }

                        SigleUserViewProfile sigleUserViewProfile = SigleUserViewProfile
                                .newInstance();
                        if (sigleUserViewProfile != null) {
                            if (sigleUserViewProfile.getBlock_user_btn() != null) {
                                sigleUserViewProfile.getBlock_user_btn()
                                        .setText("Unblock");
                            }
                        }

                    }

                }
                if (result.equals("false")) {
                    Toast.makeText(InAppMessageActivity.this,
                            "error please try later", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    class UnBlock extends AsyncTask<String, String, String> {

        String remoteIdUnBlock = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogBlock = new ProgressDialog(InAppMessageActivity.this);
            dialogBlock.setMessage("unblocking...");
            dialogBlock.setCancelable(false);
            dialogBlock.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                if (Utils.isDeviceOnline(InAppMessageActivity.this)) {

                    remoteIdUnBlock = params[0];

                    String listName = "newList";
                    List<PrivacyItem> privacyItems = new Vector<PrivacyItem>();
                    PrivacyItem itemP = new PrivacyItem(
                            PrivacyItem.Type.jid.toString(), true, 7);
                    itemP.setValue(remoteIdUnBlock);

                    itemP.setFilterIQ(true);
                    itemP.setFilterMessage(true);
                    itemP.setFilterPresence_in(true);
                    privacyItems.add(itemP);

                    if (GlobalData.blockUserJid != null
                            && GlobalData.blockUserJid.size() > 0) {
                        GlobalData.blockUserJid.remove(remoteIdUnBlock);
                    }

                    try {

                        GlobalData.privacyManager.createPrivacyList(listName,
                                privacyItems);
                        GlobalData.privacyManager.setDefaultListName(listName);
                        GlobalData.privacyManager.setActiveListName(listName);
                        Utils.printLog("UN BLOCKED");
                        GlobalData.dbHelper
                                .singleContactUnBlockfromDB(remoteIdUnBlock);
                        Utils.printLog("List-"
                                + GlobalData.privacyManager.getActiveList()
                                .getItems().get(0).toXML());

                        try {
                            blockListUpdate();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        return "true";

                    } catch (XMPPException e) {
                        // System.out.println("PRIVACY_ERROR: " + e);
                        return "false";
                    }
                } else {
                    return "no_internet";

                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return "false";
        }

        void blockListUpdate() {
            String listName = "newList";
            List<PrivacyItem> privacyItems = new Vector<PrivacyItem>();
            PrivacyItem itemP = new PrivacyItem(
                    PrivacyItem.Type.jid.toString(), false, 8);
            if (GlobalData.blockUserJid != null
                    && GlobalData.blockUserJid.size() > 0) {
                Iterator<Map.Entry<String, String>> itr1 = GlobalData.blockUserJid
                        .entrySet().iterator();
                while (itr1.hasNext()) {
                    Map.Entry<String, String> entry = itr1.next();
                    itemP = new PrivacyItem(PrivacyItem.Type.jid.toString(),
                            false, 8);
                    itemP.setValue(entry.getKey());
                    itemP.setFilterIQ(false);
                    itemP.setFilterMessage(false);
                    itemP.setFilterPresence_in(false);
                    privacyItems.add(itemP);
                }

                try {

                    GlobalData.privacyManager.createPrivacyList(listName,
                            privacyItems);
                    GlobalData.privacyManager.setDefaultListName(listName);
                    GlobalData.privacyManager.setActiveListName(listName);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialogBlock.dismiss();
            try {
                if (result.equals("no_internet")) {
                    Toast.makeText(InAppMessageActivity.this,
                            "No internet connection", Toast.LENGTH_SHORT)
                            .show();
                }
                if (result.equals("true")) {
                    Toast.makeText(InAppMessageActivity.this, "user unblocked",
                            Toast.LENGTH_SHORT).show();
                    if (ConstantFields.HIDE_MENU == 1) {
                        SingleChatRoomFrgament chatroom = SingleChatRoomFrgament
                                .newInstance("");
                        if (chatroom != null) {
                            chatroom.getmBlockBtn().setText("Block User");
                        }
                        invalidateOptionMenuItem();
                    }

                    if (ConstantFields.HIDE_MENU == 3) {

                        SingleChatRoomFrgament chatroom = SingleChatRoomFrgament
                                .newInstance("");
                        if (chatroom != null) {
                            chatroom.getmBlockBtn().setText("Block User");
                        }

                        SigleUserViewProfile sigleUserViewProfile = SigleUserViewProfile
                                .newInstance();
                        if (sigleUserViewProfile != null) {
                            if (sigleUserViewProfile.getBlock_user_btn() != null) {
                                sigleUserViewProfile.getBlock_user_btn()
                                        .setText("Block");
                            }
                        }

                    }

                }
                if (result.equals("false")) {
                    Toast.makeText(InAppMessageActivity.this,
                            "error please try later", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void broadCastDelete(String broadcastid, int page) {
        USER_ID = Utils.getUserId(InAppMessageActivity.this);
        BroadCastDeleteAsyncTask asyncTask = new BroadCastDeleteAsyncTask(
                USER_ID, broadcastid, page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asyncTask.execute();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                try {
                    SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
                            .newInstance("");
                    if (chatRoomFrgament != null) {
                        chatRoomFrgament.killEmoji();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onBackPressed();
                return false;
        }
        return super.onKeyDown(keyCode, event);

    }

    public void ftpMakeDirectory(String args) {

        String folder = "/" + USER_ID;
        String server = GlobalData.FTP_HOST;
        int port = 21;
        String user = GlobalData.FTP_USER;
        String pass = GlobalData.FTP_PASS;

        org.apache.commons.net.ftp.FTPClient ftpClient = new org.apache.commons.net.ftp.FTPClient();
        try {
            try {
                ftpClient.connect(server, port);
            } catch (Exception e) {

                e.printStackTrace();
            }

            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                // System.out.println("Operation failed. Server reply code: " +
                // replyCode);
                return;
            }
            boolean success = ftpClient.login(user, pass);

            if (!success) {
                // System.out.println("Could not login to the server");
                // String filePath =
                // "file__http://nowconnect.in/473/Kitever_1474458021281.jpg__I";
                // FTPFile file = ftpClient.mlistFile(filePath);
                // long size = 0;
                // // size = file.getSize();
                // String reply=null;
                // ftpClient.sendCommand("SIZE", filePath);
                // reply = ftpClient.getReplyString();
                return;
            }

            String dirToCreate = folder;
            success = ftpClient.makeDirectory(dirToCreate);

            if (success) {
                Utils.userIsCreateFtpFolder(InAppMessageActivity.this, true);
                // System.out.println("Successfully created directory: " +
                // dirToCreate);
            } else {
                // System.out.println("Failed to create directory. See server's reply.");
            }
            // logs out
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            // System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }

    }

    public void getGroupInfoAndOpenChatPanel(String group_jid, String name) {

        Recentmodel chatmodel = GlobalData.dbHelper
                .getsingleContactfromDB(group_jid);
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
        data.putString("user_pic_url", chatmodel.getUserPicUrl());

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                final NewContactModelForFlag contactmodel2 = new NewContactModelForFlag();
                Utils.saveContactItem(InAppMessageActivity.this, contactmodel2);
            }
        }).start();

        Fragment chatfrag = new SingleChatRoomFrgament();
        chatfrag.setArguments(data);
        callFragmentWithAddBack(chatfrag,
                ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);

    }

    public TextView getSorting_title() {
        return sorting_title;
    }

    public void setSorting_title(TextView sorting_title) {
        this.sorting_title = sorting_title;
    }


    public boolean isUserBlock(String remote_jid) {

        boolean isBlock = GlobalData.dbHelper.isContactBlock(remote_jid);

        try {
            isBlock = GlobalData.dbHelper.isContactBlock(remote_jid);

            return isBlock;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isBlock = false;
        }
        return isBlock;

    }

    public byte[] setOnLineOffileStatus(String remote_jid, int isgroup, int page) {// page
        // 1
        // mean
        // single
        // chat
        // page
        Bitmap pic = null;
        byte[] frndpic = null;
        Contactmodel contactmodel = GlobalData.dbHelper
                .getCustomStatus(remote_jid);
        if (contactmodel != null) {
            if (contactmodel.getCustomStatus() != null
                    && contactmodel.getCustomStatus().length() > 0
                    && contactmodel.getCustomStatus().equalsIgnoreCase(
                    GlobalData.INVISIABLE)) {
                getmUserStatusTitle().setVisibility(View.GONE);
            } else {
                if (page == 1) {
                    getmUserStatusTitle().setVisibility(View.VISIBLE);
                }

                if (contactmodel.getStatus() != null
                        && contactmodel.getStatus().equalsIgnoreCase("1")) {
                    getmUserStatusTitle().setText("Online");
                } else {
                    LastSeenUpdate(contactmodel.getLastseen(), isgroup);
                }
            }
            // for image apply
            if (contactmodel.getAvatar() != null) {
                // if(contactmodel.getImageUrl()!=null &&
                // !contactmodel.getImageUrl().equalsIgnoreCase("")&&
                // !contactmodel.getImageUrl().equalsIgnoreCase("null")){

                pic = BitmapFactory.decodeByteArray(contactmodel.getAvatar(),
                        0, contactmodel.getAvatar().length);
                frndpic = contactmodel.getAvatar();
//                getProfileImage(contactmodel.getImageUrl(),
//                        getmActionBarImage());
                getmActionBarImage().setImageBitmap(pic);
            }
//            else {
//                getmActionBarImage().setImageResource(R.drawable.profileimg);
//            }
        }
        return frndpic;
    }

    public void LastSeenUpdate(String time, int isgroup) {
        if (isgroup == 0) {
            if (time != null && time.length() > 0) {
                getmUserStatusTitle().setText(
                        "Last seen " + Utils.getRecentmsgDateorTime(time));
            } else {
                getmUserStatusTitle().setText("Offline");
            }
        }

    }
}

/*
*  public static String getContactPhoneNumber(Context context, String contactId) {
        int type = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        String phoneNumber = null;

        String[] whereArgs = new String[]{String.valueOf(contactId),
                String.valueOf(type)};

        // Log.d(TAG, "Got contact id: "+contactId);

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone._ID + " = ? and "
                        + ContactsContract.CommonDataKinds.Phone.TYPE + " = ?",
                whereArgs, null);

        int phoneNumberIndex = cursor
                .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

        if (cursor != null) {
            // Log.d(TAG, "Returned contact count: "+cursor.getCount());
            try {
                if (cursor.moveToFirst()) {
                    phoneNumber = cursor.getString(phoneNumberIndex);
                    try {

                        phoneNumber = phoneNumber.replaceAll("-", "");
                        phoneNumber = phoneNumber.replaceAll("\\(", "");
                        phoneNumber = phoneNumber.replaceAll("\\)", "");
                        phoneNumber = phoneNumber.replaceAll("\\s+", "");

                        // Log.e("SimCon:  After replace",""+number);
                    } catch (Exception e) {
                        // phoneNumber=phoneNumber.replaceAll("\\s+", "");
                    }
                }
            } finally {
                cursor.close();
            }
        }

        // Log.d(TAG, "Returning phone number: "+phoneNumber);
        return phoneNumber;
    }

     public ProgressBar getProgress_on_actionbar() {
        return progress_on_actionbar;
    }

    public void setProgress_on_actionbar(ProgressBar progress_on_actionbar) {
        this.progress_on_actionbar = progress_on_actionbar;
    }
     public void FetchContactNumberMethod() {

        if (fetchContactNumberAsyn == null) {
            fetchContactNumberAsyn = new FetchContactNumber();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                fetchContactNumberAsyn
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                fetchContactNumberAsyn.execute();
            }
        } else {
            fetchContactNumberAsyn.cancel(true);
            fetchContactNumberAsyn = null;
            fetchContactNumberAsyn = new FetchContactNumber();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                fetchContactNumberAsyn
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                fetchContactNumberAsyn.execute();
            }

        }

    }

     public void homeContainerIsVisiable() {

    }

    public void homeContainerIsGone() {

    }

    public void showMenuMethod() {

        if (mMenu != null) {
            if (ConstantFields.HIDE_MENU == 0) {
                for (int i = 0; i < mMenu.size(); i++)
                    mMenu.getItem(i).setVisible(true);
                ActivityCompat.invalidateOptionsMenu(this);
            }
        }
    }
    public void callFragmentForRemove(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        // transaction.commit();
        transaction.commitAllowingStateLoss();
    }
     public FrameLayout getTab_bottom_container() {
        return tab_bottom_container;
    }

    public void groupActionBarControlIsGone() {
        getLayout_name_status().setVisibility(View.VISIBLE);

    }

     public void GetContactstatusandAvatarMethod() {

        if (getContactstatusandAvatar == null) {
            getContactstatusandAvatar = new GetContactstatusandAvatar();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getContactstatusandAvatar
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                getContactstatusandAvatar.execute();
            }

        } else {
            getContactstatusandAvatar.cancel(true);
            getContactstatusandAvatar = null;
            getContactstatusandAvatar = new GetContactstatusandAvatar();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getContactstatusandAvatar
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                getContactstatusandAvatar.execute();
            }
        }

    }

      public static String getUSER_ID() {
        return USER_ID;
    }
    /*
	 * class MyTimerTask extends TimerTask {
	 *
	 * @Override public void run() {
	 *
	 *
	 * runOnUiThread(new Runnable(){
	 *
	 * @Override public void run() {
	 *
	 *
	 * if(!GlobalData.ContactStringToSend.equalsIgnoreCase("")){ if
	 * (Utils.isDeviceOnline(InAppMessageActivity.this)) {
	 *
	 * String userId=Utils.getUserId(InAppMessageActivity.this);
	 * if(GlobalData.getContactListAsyncTask==null){
	 * GlobalData.getContactListAsyncTask= new
	 * GetContactListAsyncTask(chatPrefs,
	 * userId,progress_on_actionbar)(GetContactListAsyncTask) new
	 * GetContactListAsyncTask(chatPrefs, userId).execute(); if
	 * (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
	 * GlobalData.getContactListAsyncTask
	 * .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); }else{
	 * GlobalData.getContactListAsyncTask.execute(); } }else{
	 * GlobalData.getContactListAsyncTask.cancel(true);
	 * GlobalData.getContactListAsyncTask=null;
	 * GlobalData.getContactListAsyncTask= new
	 * GetContactListAsyncTask(chatPrefs,
	 * userId,progress_on_actionbar)(GetContactListAsyncTask) new
	 * GetContactListAsyncTask(chatPrefs, userId).execute();
	 *
	 * if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
	 * GlobalData.getContactListAsyncTask
	 * .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); }else{
	 * GlobalData.getContactListAsyncTask.execute(); } }
	 *
	 * if (timer!=null){ timer.cancel(); timer = null; }
	 *
	 *
	 * }
	 *
	 *
	 * }
	 *
	 *
	 *
	 *
	 * }}); }
	 *
	 * }
	 *
	 * /*
	 * boolean isFlagDeleted=false; public class MyCOntentObserver extends
	 * ContentObserver{ public MyCOntentObserver() { super(null); }
	 *
	 * @Override public void onChange(boolean selfChange) {
	 * super.onChange(selfChange); ////Log.e("","~~~~~~"+selfChange);
	 *
	 * if(!isFlagDeleted){ new Thread(new Runnable() {
	 *
	 * @Override // public void run() { // TODO Auto-generated method stub
	 *
	 * try { isFlagDeleted=true;
	 * ContactUtil.fetchDeletedContactFirst(getApplicationContext(), chatPrefs);
	 * isFlagDeleted=false; } catch (Exception e) { e.printStackTrace(); }
	 *
	 * } }).start(); }
	 *
	 *
	 * }
	 *
	 *
	 * @Override public boolean deliverSelfNotifications() { return true; }
	 *
	 * }
	 *
	 *
	 * private FetchContactNumber fetchContactNumberAsyn = null;
	 * private GetContactstatusandAvatar getContactstatusandAvatar = null;
	 *
	 *  class GetContactstatusandAvatar extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            DownloadallUservcard(null);

            return null;
        }

    }

    public void DownloadallUservcard(final Collection<RosterEntry> entries) {
        new Thread(new Runnable() {
            public void run() {

                if (GlobalData.connection != null
                        && GlobalData.connection.isAuthenticated()
                        && GlobalData.connection.isConnected()) {
                    GlobalData.roster = GlobalData.connection.getRoster();
                    Collection<RosterEntry> entries = GlobalData.roster
                            .getEntries();

                    for (RosterEntry r : entries) {

                        String remote_jid = "";
                        remote_jid = r.getUser();
                        if (remote_jid != null && remote_jid.length() > 0) {
                            if (!GlobalData.dbHelper.isContactBlock(remote_jid)) {

                                // ItemType subtype = r.getType();

                                byte[] byteArray = null;
                                String picname = "";
                                String status = "";
                                Presence presence = GlobalData.roster
                                        .getPresence(r.getUser());

                                if (presence.getType().equals(
                                        Presence.Type.available)) {
                                    status = "1";
                                } else {
                                    status = "0";
                                }

                                Utils.printLog(r.getUser() + " : "
                                        + presence.toString());

                                if (chatPrefs.getBoolean("firsttime", false)) {
                                    GlobalData.dbHelper.updateContactdata(
                                            remote_jid, status, byteArray,
                                            picname, 1, false);
                                } else {
                                    GlobalData.dbHelper.updateContactdata(
                                            remote_jid, status, byteArray,
                                            picname, 0, false);
                                }

                                if (GlobalData.dbHelper
                                        .checkcontactisAlreadyexist(remote_jid)) {
                                    Utils.printLog("Entry for load vcard "
                                            + remote_jid);

                                    GlobalData.dbHelper
                                            .DownloadVcardandupdateContact(remote_jid);
                                }
                            }
                        }
                    }
                }
            }
        }).start();
    }
    public class FetchContactNumber extends AsyncTask<Void, Void, Void> {
        String contact = "";

        @Override
        protected Void doInBackground(Void... params) {

            try {
                contact = ContactUtil.getDeviceContact(
                        InAppMessageActivity.this, chatPrefs);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                if (Utils.isDeviceOnline(InAppMessageActivity.this)) {
                    if ((!contact.equalsIgnoreCase("null"))
                            && (!contact.equalsIgnoreCase(""))) {

                        String userId = Utils
                                .getUserId(InAppMessageActivity.this);
                        if (GlobalData.getContactListAsyncTask == null) {
                            GlobalData.getContactListAsyncTask = new GetContactListAsyncTask(
                                    InAppMessageActivity.this, chatPrefs,
                                    userId, null)/*
                                                 * (GetContactListAsyncTask) new
												 * GetContactListAsyncTask
												 * (chatPrefs, userId).execute()
												 ;

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        GlobalData.getContactListAsyncTask
        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
        GlobalData.getContactListAsyncTask.execute();
        }
        } else {
        GlobalData.getContactListAsyncTask.cancel(true);
        GlobalData.getContactListAsyncTask = null;
        GlobalData.getContactListAsyncTask = new GetContactListAsyncTask(
        InAppMessageActivity.this, chatPrefs,
        userId, null)/*
                                                 * (GetContactListAsyncTask) new
												 * GetContactListAsyncTask
												 * (chatPrefs, userId).execute()
												 ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        GlobalData.getContactListAsyncTask
        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
        GlobalData.getContactListAsyncTask.execute();
        }
        }
        }

        }

        if (ConstantFields.HIDE_MENU == 4) {
        if (ContactFragment.deleteContactHandler != null) {
        android.os.Message msg = new android.os.Message();// comment
        // m
        Bundle b = new Bundle();
        b.putString("remoteidnew", "");
        msg.setData(b);
        ContactFragment.deleteContactHandler.sendMessage(msg);
        }
        }

        progress_on_actionbar.setVisibility(View.GONE);

        } catch (Exception e) {

        e.printStackTrace();
        }

        }
        }

        private void getProfileImage(String url,
                                 final sms19.inapp.msg.CircularImageView profile_image) {
        url = "http://" + url;
        ImageRequest downloadImage = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        try {
                            profile_image.setImageBitmap(response);
                        } catch (Exception e) {
                            // TODO: handle exception
                            profile_image
                                    .setImageResource(R.drawable.profileimg);
                        }

                    }
                }, 0, 0, null, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                profile_image.setImageResource(R.drawable.profileimg);
            }
        });
        RequestQueue requestQueue = Volley
                .newRequestQueue(BaseApplicationContext.baseContext);
        requestQueue.add(downloadImage);
    }

       public synchronized static sms19.inapp.msg.model.PhoneValidModel validNumberCheck(
            String phone) {
        boolean isValid = false;
        String internationalFormat = "";
        PhoneNumberUtil phoneUtil = null;

        sms19.inapp.msg.model.PhoneValidModel model = new PhoneValidModel();
        phoneUtil = PhoneNumberUtil.getInstance();
        model.setNumber(false);
        try {
            // if(phone.startsWith("+")){
            // phone=phone.substring(1);
            // }
            PhoneNumber phNumberProto = phoneUtil.parse(phone, "IN");
            int countryCode = phNumberProto.getCountryCode();
            // System.err.println("NumberParseException was thrown: " +
            // countryCode);
            isValid = phoneUtil.isValidNumber(phNumberProto);
            if (isValid) {
                internationalFormat = phoneUtil.format(phNumberProto,
                        PhoneNumberFormat.NATIONAL).replace(" ", "");
                String code = "+" + phNumberProto.getCountryCode() + "".trim();
                model.setNumber(false);

                if (phoneUtil.getNumberType(phNumberProto) == phoneUtil
                        .getNumberType(phNumberProto).FIXED_LINE
                        || phoneUtil.getNumberType(phNumberProto) == phoneUtil
                        .getNumberType(phNumberProto).TOLL_FREE) {
                    internationalFormat = "";
                    model.setNumber(false);
                } else {

                    if (internationalFormat.startsWith("0")) {
                        internationalFormat = internationalFormat.substring(1,
                                internationalFormat.length());
                    }
                    model.setPhone_number(internationalFormat);
                    model.setCountry_code(code);
                    model.setNumber(isValid);

                }
            }

        } catch (Exception e) {
            // System.err.println("NumberParseException was thrown: " +
            // e.toString());
            model.setNumber(false);
            return model;
        }
        return model;

    }
	 */

