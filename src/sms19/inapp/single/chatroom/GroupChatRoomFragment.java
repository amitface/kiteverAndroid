package sms19.inapp.single.chatroom;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.sendsms.TemplateActivity;
import com.kitever.sendsms.fragments.SmsMailInterface;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PrivacyListManager;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.MessageEventManager;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import csms19.inapp.msg.customgallery.BucketHomeFragmentActivity;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import sms19.inapp.msg.BroadCastGroupProfile;
import sms19.inapp.msg.ChatFragment;
import sms19.inapp.msg.ConstantFlag;
import sms19.inapp.msg.GroupListFrgament;
import sms19.inapp.msg.GroupProfile;
import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.RecordAudio;
import sms19.inapp.msg.SigleUserViewProfile;
import sms19.inapp.msg.adapter.SingleChatRoomAdapter;
import sms19.inapp.msg.asynctask.EmailBroadCastAsyncTask;
import sms19.inapp.msg.asynctask.GetFtpDetailsAsyncTask;
import sms19.inapp.msg.asynctask.GetSingleUserMessageHistory;
import sms19.inapp.msg.asynctask.GroupHistoryAsyncTask;
import sms19.inapp.msg.asynctask.MessageBroadCastAsyncTask;
import sms19.inapp.msg.asynctask.ReportSpamAsyncTask;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.ContactUtil;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Slacktags;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.constant.myAffiliate;
import sms19.inapp.msg.imoze.EmojiconsFragment;
import sms19.inapp.msg.imozemodel.Emojicon;
import sms19.inapp.msg.model.Chatmodel;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.Groupmodel;
import sms19.inapp.msg.model.NewContactModelForFlag;
import sms19.inapp.msg.model.Recentmodel;
import sms19.inapp.msg.model.Uploadfilemodel;
import sms19.inapp.msg.rest.GroupChatMyStatusListner;
import sms19.inapp.msg.rest.Groupchatparticipentchange;
import sms19.inapp.msg.rest.Rest;
import sms19.listview.adapter.FtpStorageAdapter;
import sms19.listview.newproject.EmailTemplateActivity;
import sms19.listview.newproject.Home;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.newproject.model.FetchSenderIDs;
import sms19.listview.newproject.model.FtpDataListModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupChatRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupChatRoomFragment extends Fragment implements
        View.OnClickListener, MediaPlayer.OnCompletionListener,
        sms19.inapp.msg.imoze.EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener,
        View.OnLongClickListener, SmsMailInterface {
    // TODO: Rename parameter arguments, choose names that match

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static SingleChatRoomFrgament chatFragment;
    private SingleChatRoomAdapter chatAdapter = null;
    public static ListView mListView;
    private TextView loading_text;
    private ImageView time_select;
    private TextView email_time_select;
    private InAppMessageActivity inAppMessageActivity = null;
    private sms19.inapp.msg.imoze.EmojiconEditText mMessageEdtext;
    private ImageView mSendBtn, mEmojiBtn, deleteBtn, mAttachBtn, back_on_search, mDelete;
    private TextView user_sms_creadit, user_email_creadit;
    private CheckBox check_box, email_check_box;
    private ProgressBar progress;
    static final int TIME_DIALOG_ID = 0;
    private static HashMap<String, Recentmodel> gmemberhasmap;
    private ArrayList<Recentmodel> gmemberlist;
    private LinearLayout creadit_sms_layout, credit_email_layout, search_header;

    private EditText mSearchEdt;
    public static boolean isSmsCreditSelected = false;

    /***************
     * xmpp code
     *************/

    public static Contactmodel myModel;
    public static byte[] frndpic = null;
    public static String frndname = "";
    public static String remote_jid = "", customStatus = "";
    private String status = "", phonenumber = "";
    private int isuserblock = 0;
    public ArrayList<Uploadfilemodel> fileList;   
   
    private static String previousrowid = "0";
    private static String previousTime = "0";
    public static boolean fromattch = false;
    public static boolean loadmoremsgshow = true;
    private ChatManager chatManager;
    public static int isgroup = 0;
    public static int isstranger = 0;
    public static MultiUserChat mucChat = null;
    public static ArrayList<Chatmodel> chathistorylist;    
    public static Handler groupChatHandler = null;
    public static Handler offlineChatHandler = null;
    public static Handler onLineOfflineHandler = null;
    public static Handler onCheckPresenceHandler = null;
    public static Handler groupJoinAndLoadMembersHandler = null;
    public static Handler handlerRefreshAdapter = null;
    public static String sendfilefixString = "file__";
    private PopupWindow popupWindow;
    public static int deviceWidth = 0;
    public static int popupwidth = 0;
    private ViewGroup container;
    public static Handler RecordAudioHandler;
    private long rowid = 0;
    private String fileType = "";
    public static String recordfilePath = "";
    public static String locationfilePath = "";
    private String latlng = "", videoPath = "", videothmbpath = "";
    public static int RESULT_LOAD_IMAGE = 1;
    public static int VIDEO_REQUEST = 1800;
    public static int CAMERA_REQUEST = 1888;
    private static final int SELECT_TEMPLATE_REQUEST_CODE = 102;
    private final int SELECT_EMAIL_TEMPLATE_REQUEST_CODE = 104;
    public static Uri mCapturedImageURI = null;
    private RelativeLayout emojicons;
    private sms19.inapp.msg.imoze.EmojiconEditText mEditEmojicon;
    private LinearLayout chat_addfilebuttonlay;
    private View.OnClickListener galleryClick, photoClick, videoclick, audioClick,
            templateClick, storageClick;
    private View viewGlobal;
    public static Home.ChatFragmentListener obj;
    private TextView fc_addpreviousmsg;
    private ProgressBar fc_loadmsgprogress;
    private RelativeLayout fc_loadmsglay;
    private TextView fc_addcontact, fc_notnow;
    private LinearLayout addtodevicelay, block_layout;
    private int LAST_HIDE_MENU = 0;
    private HashMap<String, Contactmodel> postContactmap = new HashMap<String, Contactmodel>();
    // private CheckCreditAsyncTask asyncTask = null;
    private String imagetoupload2 = "", setimege = "", sendurlpath;
    private String base64string = "", lastTitle = "";
    private LinearLayout un_block_layout, block_message_layout_bottm;
    private Groupmodel groupModel = new Groupmodel();
    private Button mBlockBtn, mSpamReportBtn;
    private NewContactModelForFlag contactmodelFlag = null;
    private ReportSpamAsyncTask reportSpamAsyncTask = null;
    public static boolean ON_SINGLE_CHAT_PAGE = false;
    private String stringRowId = "", userId = "";
    private EmojiconsFragment emojiconsFragment = null;
    private String templateId = "";
    private boolean makeEmojiVisible = true;
    private Handler ChatupdateHandler;


    public GroupChatRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupChatRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupChatRoomFragment newInstance(String param1, String param2) {
        GroupChatRoomFragment fragment = new GroupChatRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
*/
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_group_chat_room, container, false);
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        previousrowid = "0";
        fromattch = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        inAppMessageActivity = (InAppMessageActivity) getActivity();
        chat_addfilebuttonlay = inAppMessageActivity.getCamera_btn();
        ConstantFlag.TAB_BACK_HANDLE_FRAGMENT = "1";
        inAppMessageActivity.getActionbar_title().setVisibility(View.GONE);
        inAppMessageActivity.getLayoutTab_contact_chat().setVisibility(
                View.GONE);
        inAppMessageActivity.getLayout_name_status()
                .setVisibility(View.VISIBLE);
        inAppMessageActivity.getmActionBarImage().setVisibility(View.VISIBLE);
        LAST_HIDE_MENU = ConstantFields.HIDE_MENU;
        ConstantFields.HIDE_MENU = 1;
        lastTitle = inAppMessageActivity.getmUserNameTitle().getText()
                .toString();
        inAppMessageActivity.invalidateOptionMenuItem();
        GlobalData.OnHomefrag = false;
        GlobalData.CREATE_GROUP_FRAGMENT = false;
    }



    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ON_SINGLE_CHAT_PAGE = false;

        userId = Utils.getUserId(getActivity());
        Utils.getIsCreateFtpFolder(getActivity());

        /***** start make directory on ftp server ****/

        if (Utils.isDeviceOnline(getActivity())) {

            new Thread(new Runnable() {

                @Override
                public void run() {
                    ftpMakeDirectory("");
                }
            }).start();
        }

        /***** end make directory on ftp server ****/

       /* previousrowid = "0";
        chatFragment = this;
        obj = this;*/
        frndpic = null;
        myModel = null;
        frndname = "";
        remote_jid = "";

        fileList = new ArrayList<Uploadfilemodel>();
        
        Bundle getData = getArguments();
        gmemberlist = new ArrayList<Recentmodel>();
        frndname = getData.getString("remote_name", "");
        remote_jid = getData.getString("remote_jid", "");
        customStatus = getData.getString("custom_status", "");
        isgroup = getData.getInt("isgroup");
        isstranger = getData.getInt("isstranger");
        phonenumber = getData.getString("phonenumber");
        status = getData.getString("status", "");
        isuserblock = getData.getInt("isuserblock", 0);

        if (inAppMessageActivity.isUserBlock(remote_jid)) {
            isuserblock = 1;
        } else {
            isuserblock = 0;
        }

        /*if (Utils.isDeviceOnline(getActivity())) {

            if (isgroup == 1) {
                byte[] bs = GlobalData.dbHelper.getGroupIcon(remote_jid);
                if (!(bs != null && bs.length > 0)) {
                    downloadImage(GlobalData.FTP_HOST
                            + Utils.getProfilePathForGroupImagwDownload(frndname
                            .trim()));// download group image from ftp
                }
            }else if(isgroup ==0)
            {
                byte[] bs = GlobalData.dbHelper.getGroupIcon(remote_jid);
                if (!(bs != null && bs.length > 0)) {
                    downloadImage(GlobalData.FTP_HOST
                            + Utils.getProfilePathForGroupImagwDownload(frndname
                            .trim()));// download group image from ftp
                }
            }
        }*/
        // if(remote_jid.endsWith("@Broadcast")){
        // getGroupContacts(frndname);
        // list.add("+919990305371@email19.in");
        // }
        long valuetime = Utils.getUserTime(Home.homeServiceConnection);
        long valuetimeCurrent = Utils
                .getUserTimeCurrent(Home.homeServiceConnection);

        if (valuetime != 0) {
            Utils.SERVER_TIME = valuetime;
            Utils.CURRENT_TIME = valuetimeCurrent;
        }

        frndpic = getData.getByteArray("remote_pic");

        if (isgroup == 0) {

            ON_SINGLE_CHAT_PAGE = true;
            inAppMessageActivity.invalidateOptionMenuItem();
        } else if (isgroup == 1) {

            ConstantFields.HIDE_MENU = 6;
            groupModel = GlobalData.dbHelper.getSingleGroupFromDB(remote_jid);
            if (GlobalData.dbHelper.checkGroupiscreatedbyme(remote_jid)) {
                groupModel.setCreated_me("1");
            } else {
                groupModel.setCreated_me("0");
            }
            inAppMessageActivity.invalidateOptionMenuItem();

        } else if (isgroup == 2) {

            ConstantFields.HIDE_MENU = 88;
            inAppMessageActivity.invalidateOptionMenuItem();

        }

        myModel = GlobalData.dbHelper.getUserDatafromDB();

		/*
         * if (GlobalData.connection != null) { if
		 * (GlobalData.connection.isConnected()) { if (isgroup != 1) { if
		 * (status.equalsIgnoreCase("1")) {
		 * homeActivity.getmUserStatusTitle().setText("Online"); } else {
		 * homeActivity.getmUserStatusTitle().setText("Offline"); } } else {
		 * homeActivity.getmUserStatusTitle().setText("Users.."); }
		 *
		 * } else { homeActivity.getmUserStatusTitle().setText(""); } } else {
		 * homeActivity.getmUserStatusTitle().setText(""); }
		 */



        View view = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_group_chat_room, container, false);
        viewGlobal = view;

        setpopUpclicks();
        initiateView(view);
        inItHandlerRefreshAdapter();
        inItCheckPresenceHandler();
        groupJoinHandler();

         if(isgroup==1){
            Bitmap pic = null;
            if (frndpic != null) {
                pic = BitmapFactory.decodeByteArray(frndpic, 0, frndpic.length);

            } else {
                Drawable drawable = getResources().getDrawable(
                        R.drawable.group_icon);
                pic = Utils.drawableToBitmap(drawable);
            }
            inAppMessageActivity.getmActionBarImage().setImageBitmap(pic);

        }
        return view;
    }
    

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        GlobalData.dbHelper.resetUnreadmsgCount(remote_jid);

        final ArrayList<Chatmodel> arrayList = GlobalData.dbHelper
                .getReadMessage(remote_jid);

        if (arrayList != null && arrayList.size() > 0
                && GlobalData.connection != null
                && GlobalData.connection.isConnected() && isgroup == 0) {

            new Thread(new Runnable() {

                @Override
                public void run() {

                    for (int i = 0; i < arrayList.size(); i++) {

                        MessageEventManager eventManager = new MessageEventManager(
                                GlobalData.connection);
                        if (eventManager != null
                                && arrayList.get(i).getMsg_packetid() != null
                                && arrayList.get(i).getMsg_packetid().trim()
                                .length() > 0) {
                            eventManager.sendDisplayedNotification(remote_jid,
                                    arrayList.get(i).getMsg_packetid().trim());
                            GlobalData.dbHelper.updatestatusOfSentMessage(
                                    StringUtils.parseBareAddress(remote_jid),
                                    arrayList.get(i).getMsg_packetid().trim(),
                                    "read");
                        }
                    }
                }
            }).start();
        }

        checkUserIsJoinGroup();

        if (fromattch) {
            chathistorylist = new ArrayList<Chatmodel>();
            fromattch = false;
            OnStartGetChatHistory(true);

        } else {
            if (loadmoremsgshow) {
                fc_loadmsglay.setVisibility(View.VISIBLE);
            } else {
                fc_loadmsglay.setVisibility(View.GONE);
            }
        }

        chatAdapter = new SingleChatRoomAdapter(getActivity(), chathistorylist);
        chatAdapter.setClickListener(this);
        chatAdapter.setLongClickListener(this);
        mListView.setAdapter(chatAdapter);
        mListView.setSelection(chathistorylist.size() - 1);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        setChatupdateHandler();
                        setGroupChatHandler();
                        setOnLineOfflineHandler();
                        setOfflineMsgFromService();

                        setAudiorecordHandler();
                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay()
                                .getMetrics(displaymetrics);

                        deviceWidth = displaymetrics.widthPixels;
                        popupwidth = (int) (deviceWidth * 0.75);
                    }
                });

            }
        }).start();

        // if (!(GlobalData.connection != null
        // && GlobalData.connection.isConnected() && GlobalData.connection
        // .isAuthenticated())) {
        // Utils.offlineShowUser();
        // }

        GlobalData.dbHelper.resetUnreadmsgCount(remote_jid);
        if (Utils.isDeviceOnline(getActivity())) {
            if (GlobalData.connection != null
                    && GlobalData.connection.isConnected()) {
                if (GlobalData.shareFilepath != null
                        && GlobalData.shareFilepath.trim().length() != 0) {
                    if (GlobalData.shareFiletype.equals(GlobalData.Imagefile)) {
                        sendimgFile(GlobalData.shareFilepath, null);
                    } else if (GlobalData.shareFiletype
                            .equals(GlobalData.Videofile)) {
                        sendVideofile(GlobalData.shareFilepath);
                    } else if (GlobalData.shareFiletype
                            .equals(GlobalData.Audiofile)) {
                        recordfilePath = GlobalData.shareFilepath;
                        RecordAudioHandler.sendEmptyMessage(0);
                    }
                }
            }
        }

        if (isgroup == 2) {
            setGroupmemberlist(false);
        }

        reportSpamButton();

        if (isgroup == 0 || isgroup == 2) {
            boolean isByDefaultSendSmsCheckedYesNo = Utils
                    .getByDefaultSendMsgYesNo(getActivity());
            boolean isByDefaultSendMailCheckedYesNo = Utils
                    .getByDefaultSendMailYesNo(getActivity());
            check_box.setChecked(isByDefaultSendSmsCheckedYesNo);
            email_check_box.setChecked(isByDefaultSendMailCheckedYesNo);
        }

        if (SingleChatRoomAdapter.isForwardData) {
            new AlertDialog.Builder(inAppMessageActivity)
                    .setCancelable(false)
                    // .setTitle("Login Failed!")
                    .setMessage("Do you want to forward/share data.")
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    try {
                                        if (SingleChatRoomAdapter.typeOfData
                                                .equalsIgnoreCase("V")) {
                                            sendVideofile(SingleChatRoomAdapter.pathToForwardData);
                                        } else if (SingleChatRoomAdapter.typeOfData
                                                .equalsIgnoreCase("I")) {
                                            sendimgFile(
                                                    SingleChatRoomAdapter.pathToForwardData,
                                                    null);
                                        } else {
                                            if (GlobalData.connection
                                                    .isAuthenticated()) {
                                                messageSend(SingleChatRoomAdapter.msgToSend);
                                            } else {
                                                messageSendOffline(
                                                        SingleChatRoomAdapter.msgToSend,
                                                        "0");
                                            }
                                        }
                                        dialog.cancel();
                                        SingleChatRoomAdapter.typeOfData = null;
                                        SingleChatRoomAdapter.pathToForwardData = null;
                                        SingleChatRoomAdapter.msgToSend = null;
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    try {
                                        SingleChatRoomAdapter.typeOfData = null;
                                        SingleChatRoomAdapter.pathToForwardData = null;
                                        SingleChatRoomAdapter.msgToSend = null;
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                }
                            }).show();
            SingleChatRoomAdapter.isForwardData = false;
            // SingleChatRoomAdapter.typeOfData=null;
            // SingleChatRoomAdapter.pathToForwardData=null;
        }
    }

    public void checkUserIsJoinGroup() {
        calljoinhaldler();
    }

    public boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void initiateView(View view) {

        Home.currentFragment = "SingleChatRoomFrgament";
        // testProgress=(ProgressBar)view.findViewById(R.id.test);
        mListView = (ListView) view.findViewById(R.id.listview_single_chat);
        /*Amit*/
        mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mListView.setStackFromBottom(true);
        /*Amit*/

        loading_text = (TextView) view.findViewById(R.id.loading_text);
        block_layout = (LinearLayout) view.findViewById(R.id.block_layout);
        block_layout.setVisibility(View.GONE);
        un_block_layout = (LinearLayout) view
                .findViewById(R.id.un_block_layout);
        block_message_layout_bottm = (LinearLayout) view
                .findViewById(R.id.block_message_layout_bottm);
        creadit_sms_layout = (LinearLayout) view
                .findViewById(R.id.creadit_sms_layout);
//        credit_email_layout = (RelativeLayout) view
//                .findViewById(R.id.credit_email_layout);
        mBlockBtn = (Button) view.findViewById(R.id.create);
        mSpamReportBtn = (Button) view.findViewById(R.id.cancel);
        emojicons = (RelativeLayout) view.findViewById(R.id.emojicons);
        check_box = (CheckBox) view.findViewById(R.id.check_box);
        email_check_box = (CheckBox) view.findViewById(R.id.email_check_box);
        mMessageEdtext = (sms19.inapp.msg.imoze.EmojiconEditText) view
                .findViewById(R.id.chat_msg);
        mSendBtn = (ImageView) view.findViewById(R.id.message_send_btn);
        mEmojiBtn = (ImageView) view.findViewById(R.id.imozebtn);
        deleteBtn = (ImageView) view.findViewById(R.id.delete_btn);
        mAttachBtn = (ImageView) view.findViewById(R.id.attachBtn);
        time_select = (ImageView) view.findViewById(R.id.time_select);
        email_time_select = (TextView) view
                .findViewById(R.id.email_time_select);
        user_sms_creadit = (TextView) view.findViewById(R.id.user_sms_creadit);
//        user_email_creadit = (TextView) view
//                .findViewById(R.id.user_email_creadit);
        progress = (ProgressBar) view.findViewById(R.id.progress);

        search_header = (LinearLayout) view.findViewById(R.id.search_header);
        back_on_search = (ImageView) view.findViewById(R.id.back_on_search);
        mDelete = (ImageView) view.findViewById(R.id.delete);
        mSearchEdt = (EditText) view.findViewById(R.id.search_edt);
        search_header.setVisibility(View.GONE);

        mEditEmojicon = (sms19.inapp.msg.imoze.EmojiconEditText) view
                .findViewById(R.id.chat_msg);
        mEditEmojicon.setUseSystemDefault(false);

        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listheader = inflater.inflate(R.layout.chathistorylist_header,
                null, false);

        fc_addcontact = (TextView) listheader.findViewById(R.id.fc_addcontact);
        fc_notnow = (TextView) listheader.findViewById(R.id.fc_notnow);
        addtodevicelay = (LinearLayout) listheader
                .findViewById(R.id.addtodevicelay);
        fc_addpreviousmsg = (TextView) listheader
                .findViewById(R.id.fc_addpreviousmsg);
        fc_loadmsgprogress = (ProgressBar) listheader
                .findViewById(R.id.fc_loadmsgprogress);
        fc_loadmsglay = (RelativeLayout) listheader
                .findViewById(R.id.fc_loadmsglay);

        mListView.addHeaderView(listheader);
        mDelete.setOnClickListener(this);
        back_on_search.setOnClickListener(this);
        fc_addcontact.setOnClickListener(this);
        fc_notnow.setOnClickListener(this);
        fc_addpreviousmsg.setOnClickListener(this);
        time_select.setOnClickListener(this);
        email_time_select.setOnClickListener(this);
        mBlockBtn.setOnClickListener(this);
        mEmojiBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        if (isuserblock == 0) {
            mBlockBtn.setText("Block User");
        } else {
            mBlockBtn.setText("Unblock User");
        }

        block_message_layout_bottm.setVisibility(View.GONE);

        check_box.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!Utils.isDeviceOnline(getActivity())) {
                    Toast.makeText(getActivity(),
                            "No internet connection found.", Toast.LENGTH_LONG)
                            .show();
                    check_box.setChecked(false);
                }
            }
        });


        email_check_box.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!Utils.isDeviceOnline(getActivity())) {
                    Toast.makeText(getActivity(),
                            "No internet connection found.", Toast.LENGTH_LONG)
                            .show();
                    email_check_box.setChecked(false);
                }
            }
        });
        mSearchEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                // searchFilter(s.toString());
                searchFilter();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                searchFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchFilter();

            }
        });

        inAppMessageActivity.getmActionBarImage().setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        mSpamReportBtn.setOnClickListener(this);
        // mAttachBtn.setOnClickListener(this);

        // mAttachBtn.setOnClickListener(photoClick);
        mAttachBtn.setOnClickListener(this);
        chatAdapter = new SingleChatRoomAdapter(getActivity(), chathistorylist);
        chatAdapter.setClickListener(this);
        chatAdapter.setLongClickListener(this);
        mListView.setAdapter(chatAdapter);

        chat_addfilebuttonlay.setOnClickListener(this);
        mEditEmojicon.setOnClickListener(this);
        inAppMessageActivity.getmUserNameTitle().setText(frndname);

        fc_loadmsgprogress.setVisibility(View.GONE);
        fc_loadmsglay.setVisibility(View.GONE);
        addtodevicelay.setVisibility(View.GONE);

        final Calendar c = Calendar.getInstance();

//        user_sms_creadit.setTag("0:05");
//        user_email_creadit.setTag("0:05");
        check_box.setTag("0:05");
        email_check_box.setTag("0:05");
        String timeValue = Utils.getExpiryTimeValue(getActivity());
//        String mailtimeValue = Utils.getExpiryTimeValue(getActivity());
        if (!timeValue.equalsIgnoreCase("")) {
//            user_sms_creadit.setTag(timeValue);
            check_box.setTag(timeValue);
            String newTime[] = timeValue.split(":");
            user_sms_creadit.setText("Send undelivered chats in "+timeValue+" Min");
           /* String mailnewTime[] = mailtimeValue.split(":");*/
/*            user_sms_creadit
                    .setText("Use my sms credits to send messages not delivered in App with in "
                            + String.valueOf(newTime[0])
                            + ":"
                            + newTime[1]
                            + " Min "); // set the value to textview*/
           /* check_box.setText("Sms " + String.valueOf(newTime[0])
                    + ":"
                    + newTime[1]
                    + " Min ");
            email_check_box.setTag(timeValue);*/

//            user_email_creadit.setTag(timeValue);
           /* user_email_creadit
                    .setText("Use my email credits to send emails not delivered in App with in "
                            + String.valueOf(newTime[0])
                            + ":"
                            + newTime[1]
                            + " Min ");*/
            /*email_check_box.setText("Mail " + String.valueOf(newTime[0])
                    + ":"
                    + newTime[1]
                    + " Min ");*/
        }

        // .

        if (isgroup == 1) {
            creadit_sms_layout.setVisibility(View.GONE);
//            credit_email_layout.setVisibility(View.GONE);
        }
        mMessageEdtext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() != 0) {
                    mAttachBtn.setVisibility(View.GONE);
                    mSendBtn.setVisibility(View.VISIBLE);
                } else {
                    mAttachBtn.setVisibility(View.VISIBLE);
                    mSendBtn.setVisibility(View.GONE);
                }

            }
        });

    }

    public void reportSpamButton() {

        if (isgroup == 0) {

            if (!GlobalData.dbHelper.isReportSpamAlredy(remote_jid)) {
                mSpamReportBtn.setVisibility(View.VISIBLE);

                if (GlobalData.dbHelper.getIsStranger(remote_jid)) {

                    addtodevicelay.setVisibility(View.VISIBLE);
                    block_layout.setVisibility(View.VISIBLE);

                    if (GlobalData.dbHelper.getIsStranger(remote_jid)) {

                        if (chathistorylist != null) {

                            if (chathistorylist.size() > 0) {
                                for (int i = 0; i < chathistorylist.size(); i++) {
                                    if (chathistorylist.get(i).isMine()) {
                                        GlobalData.dbHelper
                                                .reportSpamUpdate(remote_jid);
                                        mSpamReportBtn.setVisibility(View.GONE);

                                        break;
                                    }
                                }

                            }
                        }

						/*
                         * if(chathistorylist.size()>1){
						 * mSpamReportBtn.setVisibility(View.GONE); }else{
						 * mSpamReportBtn.setVisibility(View.VISIBLE); }
						 */

                    }
                } else {
                    if (chathistorylist != null && chathistorylist.size() > 3) {
                        // mSpamReportBtn.setVisibility(View.GONE);
                        mBlockBtn.setVisibility(View.GONE);
                    }
                }
            } else {
                mSpamReportBtn.setVisibility(View.GONE);
                mBlockBtn.setVisibility(View.GONE);
                if (GlobalData.dbHelper.getIsStranger(remote_jid)) {
                    addtodevicelay.setVisibility(View.VISIBLE);
                } else {
                    addtodevicelay.setVisibility(View.GONE);
                }
            }
        } else {
            mSpamReportBtn.setVisibility(View.GONE);
            addtodevicelay.setVisibility(View.GONE);
            block_layout.setVisibility(View.GONE);
        }

    }

    public org.jivesoftware.smack.packet.Message onRetryFileUpload(
            String message, Chatmodel chatmodel) throws XMPPException {

        org.jivesoftware.smack.packet.Message msgpacketid = null;

        Utils.printLog("message send to server..and isgroup is" + isgroup);

        // String timeStap= Utils.currentTimeStamp();

        String userId = InAppMessageActivity.USER_ID;
        Slacktags slagTag = new Slacktags(
                InAppMessageActivity.myModel.getNumber(), userId,
                GlobalData.MESSAGE_TYPE_2, chatmodel.getMsgtime(), userId + "_"
                + chatmodel.getMsgtime());

        if (isgroup == 1) {

            if (mucChat == null) {
                mucChat = joinGroup(remote_jid);
            }
            if (mucChat != null) {
                org.jivesoftware.smack.packet.Message msg = mucChat
                        .createMessage();
                msg.setBody(message);
                msg.addExtension(slagTag);
                msg.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
                msgpacketid = msg;

            }
        } else if (isgroup == 2) {
            org.jivesoftware.smack.packet.Message msg2 = new org.jivesoftware.smack.packet.Message();
            msg2.setType(org.jivesoftware.smack.packet.Message.Type.normal);
            msg2.setBody(message);
            msg2.addExtension(slagTag);
            msgpacketid = msg2;
        }
        if (isgroup == 0) {
            org.jivesoftware.smack.packet.Message msg2 = new org.jivesoftware.smack.packet.Message();
            msg2.setType(org.jivesoftware.smack.packet.Message.Type.normal);
            msg2.setBody(message);
            msg2.addExtension(slagTag);
            msgpacketid = msg2;

        }

        if (message != null) {
            // GlobalData.dbHelper.packetIdUpdate(chatmodel.getMsg_packetid(),msgpacketid.getPacketID());
            GlobalData.dbHelper.timeUpdate(chatmodel.getMsgtime(),
                    msgpacketid.getPacketID());

        }

        // msgpacketid = msg;
        /*
         * MessageEventManager eventManager = new MessageEventManager(
		 * GlobalData.connection);
		 */
        MessageEventManager.addNotificationsRequests(msgpacketid, true, true,
                true, false);

        return msgpacketid;
    }

    @SuppressWarnings("unused")
    @Override
    public void onClick(View v) {
        Chatmodel chatmodel = null;
        switch (v.getId()) {
            case R.id.retry_btn_video:
                chatmodel = chathistorylist.get((Integer) v.getTag());
                if (Utils.isDeviceOnline(getActivity())) {
                    if (GlobalData.connection.isConnected()) {
                        v.setVisibility(View.GONE);
                        // chatAdapter.notifyDataSetChanged();
                        loadimage(chatmodel.getMediapath(),
                                chatmodel.getMediatype(), true, chatmodel);
                    }
                }
                break;
            case R.id.retry_btn_audio:
                chatmodel = chathistorylist.get((Integer) v.getTag());
                if (Utils.isDeviceOnline(getActivity())) {
                    if (GlobalData.connection.isConnected()) {

                        v.setVisibility(View.GONE);
                        // chatAdapter.notifyDataSetChanged();
                        loadimage(chatmodel.getMediapath(),
                                chatmodel.getMediatype(), true, chatmodel);
                    }
                }
                break;
            case R.id.retry_btn_image:
                chatmodel = chathistorylist.get((Integer) v.getTag());
                if (Utils.isDeviceOnline(getActivity())) {
                    if (GlobalData.connection.isConnected()) {
                        v.setVisibility(View.GONE);
                        // chatAdapter.notifyDataSetChanged();
                        loadimage(chatmodel.getMediapath(),
                                chatmodel.getMediatype(), true, chatmodel);
                    }
                }
                break;
            case R.id.cancel:
                String userId = Utils.getUserId(getActivity());

                if (phonenumber == null) {
                    phonenumber = remote_jid.split("@")[0];
                }

                if (phonenumber != null) {

                    String phone = Utils.removeCountryCode(phonenumber,
                            getActivity());
                    reportSpamAsyncTask = new ReportSpamAsyncTask(
                            inAppMessageActivity, InAppMessageActivity.chatPrefs,
                            userId, phone, remote_jid);
                    reportSpamAsyncTask.execute();
                    GlobalData.dbHelper.deleteRecentParticularrow(remote_jid);
                    // getActivity().deleteBroadCastGroupORChatWithDialog(deleteRemoteId);
                    // updateChatAdapter();
                }
                break;
            case R.id.back_on_search:
                search_header.setVisibility(View.GONE);
                mSearchEdt.setText("");
                inAppMessageActivity.actionBarShow();
                break;
            case R.id.delete:
                mSearchEdt.setText("");
                break;
            case R.id.time_select:
                show();
                break;
            case R.id.email_time_select:
                showEmailTimeSet();
                break;
            case R.id.create:
                if (GlobalData.dbHelper.isContactBlock(remote_jid)) {
                    isuserblock = 1;
                } else {
                    isuserblock = 0;
                }

                if (isuserblock == 0) {
                    blockContact(getRemote_jid());
                } else {
                    unblockContact(getRemote_jid());
                }
                break;
            case R.id.fc_addpreviousmsg:
                // search_header.setVisibility(View.GONE);
                if (search_header.getVisibility() == View.VISIBLE) {
                    search_header.setVisibility(View.GONE);
                    mSearchEdt.setText("");
                    inAppMessageActivity.actionBarShow();
                }

                getMoreChatHistory chatHistory = new getMoreChatHistory();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    chatHistory.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    chatHistory.execute();
                }
                break;
            case R.id.fc_addcontact:
                String usernumber = remote_jid.split("@")[0];
                addContacttoDevice(usernumber);
                addtodevicelay.setVisibility(View.GONE);
                break;
            case R.id.fc_notnow:
                addtodevicelay.setVisibility(View.GONE);
                break;
            case R.id.message_send_btn:
                sendButtonAction();
                break;
            case R.id.imozebtn:
                if (makeEmojiVisible) {
                    sms19.inapp.msg.constant.Utils.hideKeyBoardMethod(
                            getActivity(), mEmojiBtn);

                    setEmojiconFragment(false);
                    makeEmojiVisible = false;
                } else {
                    removeEmoji();
                }
                break;
            case R.id.attachBtn:
                Utils.hideKeyBoardMethod(getActivity(), mAttachBtn);
                openAddfilePopup();
                break;
            case R.id.delete_btn:
                mEditEmojicon.setEnabled(true);
                mEditEmojicon.setText("");
                deleteBtn.setVisibility(View.GONE);
                mEmojiBtn.setVisibility(View.VISIBLE);
                check_box.setEnabled(true);
                break;
        }

        if (inAppMessageActivity.getmActionBarImage() == v) {

            Utils.hideKeyBoardMethod(getActivity(),
                    inAppMessageActivity.getmActionBarImage());
            /*callSingleUserProfile();*/
            Utils.imagePopup(getActivity(),inAppMessageActivity.getmActionBarImage().getDrawable());
        }
        mEditEmojicon.setEnabled(true);
    }

    private void sendButtonAction() {
        // Utils.hideKeyBoardMethod(getActivity(), mSendBtn);
        if (emojicons.getVisibility() == View.VISIBLE) {
            if (emojiconsFragment != null) {
                emojiconsFragment.getmEmojiTabs()[1].performClick();
            }
            emojicons.setVisibility(View.GONE);

            if (emojicons != null) {
                emojicons.removeAllViews();
            }
        }

        if (Utils.isDeviceOnline(getActivity())) {
            // if (false) {

            if (GlobalData.connection != null
                    && GlobalData.connection.isConnected()) {


                if (isuserblock == 0) {
                    final String message = mMessageEdtext.getText().toString().trim();
                    if (message != null && message.trim().length() != 0) {
                        if (!check_box.isChecked()
                                && !email_check_box.isChecked()) {
                            if (GlobalData.connection.isAuthenticated()) {
                                messageSend(message);
                            } else {
                                messageSendOffline(message, "0");
                            }
                            loading_text.setVisibility(View.GONE);
                            mMessageEdtext.setText("");
                        } else {
                            if (GlobalData.connection.isAuthenticated()) {
                                String timeValue = (String) check_box
                                        .getTag();
                                if (!timeValue.equalsIgnoreCase("")) {

                                    getActivity().runOnUiThread(
                                            new Runnable() {
                                                public void run() {
                                                    String userId = Utils
                                                            .getUserId(getActivity());

                                                    if (isgroup == 1) {
                                                        if (gmemberlist != null) {
                                                            for (int i = 0; i < gmemberlist
                                                                    .size(); i++) {
                                                                Contactmodel contactmodel = new Contactmodel();
                                                                contactmodel
                                                                        .setNumber(Utils
                                                                                .removeCountryCode(
                                                                                        gmemberlist
                                                                                                .get(i)
                                                                                                .getUsernumber(),
                                                                                        getActivity()));
                                                                postContactmap
                                                                        .put(String
                                                                                        .valueOf(i),
                                                                                contactmodel);
                                                            }

                                                            String contactstring = Utils
                                                                    .makeJsonarrayfromContactmap(postContactmap);
                                                           
                                                        }
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(getActivity(),
                                            "Please choose delivery time!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        // loading_text.setVisibility(View.GONE);
                        // mMessageEdtext.setText("");
                        // messageSend(message);

                    } else {
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "Type message first.", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    unblockContact(remote_jid);
                }

                // setChathistory();

            } else {
                if (isuserblock == 0) {
                    final String message = mMessageEdtext.getText()
                            .toString().trim();
                    if (message != null && message.trim().length() != 0) {
                        if (!check_box.isChecked()
                                && !email_check_box.isChecked()) {
                            messageSendOffline(message, "0");
                        } else {
                            messageSendOffline(message, "0");

                        }

                        mMessageEdtext.setText("");
                        // messageSend(message);

                    } else {
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "Type message first.", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    unblockContact(remote_jid);
                }

                // Toast.makeText(getActivity(),
                // "Check your network connection",Toast.LENGTH_SHORT).show();

                // Toast.makeText(getActivity(),
                // "Not connected to server",Toast.LENGTH_SHORT).show();
            }

        } else {

            if (isuserblock == 0) {

                final String message = mMessageEdtext.getText().toString()
                        .trim();
                if (message != null && message.trim().length() != 0) {

                    if (!check_box.isChecked()
                            && !email_check_box.isChecked()) {
                        messageSendOffline(message, "0");
                    } else {

                        messageSendOffline(message, "0");
                    }

                    mMessageEdtext.setText("");
                    // messageSend(message);

                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Type message first.", Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                unblockContact(remote_jid);
            }

        }

        if (chathistorylist != null && chathistorylist.size() > 3) {
            // mSpamReportBtn.setVisibility(View.GONE);
            mBlockBtn.setVisibility(View.GONE);
            // block_layout.setVisibility(View.GONE);
        }
        if (deleteBtn.getVisibility() == View.VISIBLE) {
            deleteBtn.setVisibility(View.GONE);
            mEmojiBtn.setVisibility(View.VISIBLE);
            emojicons.setEnabled(true);
        }
    }

    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);

    }



    public void callSingleUserProfile() {

        if (isgroup != 3) {

            Fragment userdetailfrag = null;
            inAppMessageActivity.getmUserStatusTitle().setVisibility(View.GONE);
            inAppMessageActivity.getmActionBarImage().setVisibility(View.GONE);
            Bundle data = new Bundle();

            data.putString("remote_jid", remote_jid);
            data.putString("remote_name", frndname);
            data.putString("custom_status", customStatus);
            data.putInt("isgroup", isgroup);
            data.putInt("isstranger", isstranger);
            data.putString("status", status);
            data.putByteArray("remote_pic", frndpic);

            if (isgroup == 1) {
                userdetailfrag = new GroupProfile();
                userdetailfrag.setArguments(data);
                inAppMessageActivity.callFragmentWithAddBack(userdetailfrag,
                        ConstantFlag.TAB_SINGLE_CHAT_ROOM_PROFILE_FRAGMENT);
            } else if (isgroup == 2) {
                userdetailfrag = new BroadCastGroupProfile();
                userdetailfrag.setArguments(data);
                inAppMessageActivity.callFragmentWithAddBack(userdetailfrag,
                        "BroadCastGroupProfile");
            } else {
                userdetailfrag = new SigleUserViewProfile();
                userdetailfrag.setArguments(data);
                inAppMessageActivity.callFragmentWithAddBack(userdetailfrag,
                        ConstantFlag.TAB_SINGLE_CHAT_ROOM_PROFILE_FRAGMENT);
            }
        }
    }

    // satya
    public void openAddfilePopup() {
        recordfilePath = "";
        latlng = "";
        videoPath = "";
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_file_lay,
                this.container, false);
        popupWindow = new PopupWindow(popupView, popupwidth,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(popupView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.anim.popupanim);
        // popupWindow.showAsDropDown(viewGlobal.findViewById(R.id.places), 0,
        // 0);
        // RelativeLayout anchorView=(RelativeLayout)
        // viewGlobal.findViewById(R.id.places);
        popupWindow.showAtLocation(mAttachBtn, Gravity.BOTTOM, 0,
                mAttachBtn.getBottom() - 60);
        // popupWindow.showAsDropDown(mAttachBtn);

        ImageView gallerybtn = (ImageView) popupView
                .findViewById(R.id.gallerybtn);
        ImageView photobtn = (ImageView) popupView.findViewById(R.id.photobtn);
        ImageView videobtn = (ImageView) popupView.findViewById(R.id.videobtn);
        ImageView audiobtn = (ImageView) popupView.findViewById(R.id.audiobtn);
        ImageView templatebtn = (ImageView) popupView
                .findViewById(R.id.templatebtn);
        RelativeLayout gallerylay = (RelativeLayout) popupView
                .findViewById(R.id.gallerylay);
        RelativeLayout photolay = (RelativeLayout) popupView
                .findViewById(R.id.photolay);
        RelativeLayout videolay = (RelativeLayout) popupView
                .findViewById(R.id.videolay);
        RelativeLayout audiolay = (RelativeLayout) popupView
                .findViewById(R.id.audiolay);
        RelativeLayout templatelay = (RelativeLayout) popupView
                .findViewById(R.id.templatelay);
        RelativeLayout ftpStorageLay = (RelativeLayout) popupView
                .findViewById(R.id.ftp_storage_layout);
        TextView gallerytxt = (TextView) popupView
                .findViewById(R.id.gallerytxt);
        TextView phototxt = (TextView) popupView.findViewById(R.id.phototxt);
        TextView videotxt = (TextView) popupView.findViewById(R.id.videotxt);
        TextView audiotxt = (TextView) popupView.findViewById(R.id.audiotxt);
        TextView templatetxt = (TextView) popupView
                .findViewById(R.id.templatetxt);

        gallerybtn.setOnClickListener(galleryClick);
        gallerylay.setOnClickListener(galleryClick);
        gallerytxt.setOnClickListener(galleryClick);
        photobtn.setOnClickListener(photoClick);
        photolay.setOnClickListener(photoClick);
        phototxt.setOnClickListener(photoClick);
        videobtn.setOnClickListener(videoclick);
        videolay.setOnClickListener(videoclick);
        videotxt.setOnClickListener(videoclick);
        audiobtn.setOnClickListener(audioClick);
        audiolay.setOnClickListener(audioClick);
        audiotxt.setOnClickListener(audioClick);
        templatebtn.setOnClickListener(templateClick);
        templatelay.setOnClickListener(templateClick);
        templatetxt.setOnClickListener(templateClick);
        ftpStorageLay.setOnClickListener(storageClick);
        if(isgroup==0)
            ftpStorageLay.setVisibility(View.VISIBLE);
    }

    public void setpopUpclicks() {
        galleryClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

				/*
                 * Toast.makeText(getActivity().getApplicationContext(),
				 * "gallery", Toast.LENGTH_SHORT).show();
				 */
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                GlobalData.OnFilesendscreen = true;
                Intent i = new Intent(getActivity(),
                        BucketHomeFragmentActivity.class);
                i.putExtra("username", frndname);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        };

        photoClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

				/*
                 * Toast.makeText(getActivity().getApplicationContext(),
				 * "photo", Toast.LENGTH_SHORT).show();
				 */
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                boolean isSDPresent = android.os.Environment
                        .getExternalStorageState().equals(
                                android.os.Environment.MEDIA_MOUNTED);
                if (isSDPresent) {

                    if (isDeviceSupportCamera()) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "Kit19_pic");
                        mCapturedImageURI = getActivity()
                                .getContentResolver()
                                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values);

                        GlobalData.OnFilesendscreen = true;

                        Intent cameraIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                mCapturedImageURI);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }

					/*
					 * Intent i = new
					 * Intent("android.media.action.IMAGE_CAPTURE"); File f =
					 * new File(Environment.getExternalStorageDirectory(),
					 * "photo.jpg"); i.putExtra(MediaStore.EXTRA_OUTPUT,
					 * Uri.fromFile(f)); String picturePath =
					 * f.getAbsolutePath(); mCapturedImageURI = Uri.fromFile(f);
					 * startActivityForResult(i, CAMERA_REQUEST);
					 */

                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "No SD card available", Toast.LENGTH_SHORT).show();
                }
            }
        };
        videoclick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*
				 * Toast.makeText(getActivity().getApplicationContext(),
				 * "video", Toast.LENGTH_SHORT).show();
				 */
                popupWindow.dismiss();
                Boolean isSDPresent = android.os.Environment
                        .getExternalStorageState().equals(
                                android.os.Environment.MEDIA_MOUNTED);
                if (isSDPresent) {
                    videoPath = Utils.getfilePath(GlobalData.Videofile);
                    File mediaFile = new File(videoPath);
                    GlobalData.OnFilesendscreen = true;
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                    Uri videoUri = Uri.fromFile(mediaFile);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                    startActivityForResult(intent, VIDEO_REQUEST);

                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "No SD card available", Toast.LENGTH_SHORT).show();
                }
            }
        };
        audioClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*
				 * Toast.makeText(getActivity().getApplicationContext(),
				 * "audio", Toast.LENGTH_SHORT).show();
				 */
                popupWindow.dismiss();
                RecordAudio ru = new RecordAudio(getActivity());
                ru.ReSendalertDialog(getActivity());
            }
        };

        templateClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*
				 * Toast.makeText(getActivity().getApplicationContext(),
				 * "Template", Toast.LENGTH_SHORT).show();
				 */
                popupWindow.dismiss();
                latlng = "";
                // if (email_check_box.isChecked()) {
                // Intent id = new Intent(getActivity(),
                // EmailTemplateActivity.class);
                // startActivityForResult(id,
                // SELECT_EMAIL_TEMPLATE_REQUEST_CODE);
                // } else {
                // Intent id = new Intent(getActivity(),
                // TemplateActivity.class);
                // id.putExtra("taketemplate", "send");
                // startActivityForResult(id, SELECT_TEMPLATE_REQUEST_CODE);
                // }
                // if(email_check_box.getVisibility()==View.VISIBLE){
                if (email_check_box.isShown()) {
                    selectEmailSmsTemplate();
                } else {
                    Intent id = new Intent(getActivity(),
                            TemplateActivity.class);
                    id.putExtra("taketemplate", "send");
                    startActivityForResult(id, SELECT_TEMPLATE_REQUEST_CODE);
                }
            }
        };

        storageClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*
				 * Toast.makeText(getActivity().getApplicationContext(),
				 * "Template", Toast.LENGTH_SHORT).show();
				 */
                popupWindow.dismiss();
                latlng = "";
                new FetchFtpDataList(userId).execute("");

            }
        };
    }

    // satyajit
    private void openFtpStorageDataList(
            final ArrayList<FtpDataListModel> ftpDatalist) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("My Storage");
        dialog.setContentView(R.layout.ftp_storage_layout);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button share = (Button) dialog.findViewById(R.id.share);
        TextView noData = (TextView) dialog.findViewById(R.id.no_data);
        // ArrayList<FtpListModel> list=new ArrayList<FtpListModel>();
        //
        // for(int k=0;k<20;k++){
        // list.add(new FtpListModel(null, "name") );
        // }

        final FtpStorageAdapter adapter = new FtpStorageAdapter(ftpDatalist);
        ListView listView = (ListView) dialog.findViewById(R.id.ftp_list);
        if (ftpDatalist.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
        listView.setAdapter(adapter);
        // listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        // {
        // @Override
        // public void onItemClick( AdapterView<?> parent, View item,
        // int position, long id) {
        // planet = ftpDatalist.get( position );
        // planet.toggleChecked();
        // PlanetViewHolder viewHolder = (PlanetViewHolder) item.getTag();
        // viewHolder.getCheckBox().setChecked( planet.isChecked() );
        // }
        // });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String name = "";

                if (adapter.getcheckeditemcount().size() > 0
                        && ftpDatalist.size() > 0) {
                    for (int k = 0; k < adapter.getcheckeditemcount().size(); k++) {
                        FtpDataListModel dataListModel = ftpDatalist
                                .get(adapter.getcheckeditemcount().get(k));
                        if (k > 0) {
                            name = name + " , " + dataListModel.getDataName();
                            try {
                                shareMessageBeforeUpload(
                                        dataListModel.getMessageBody(),
                                        remote_jid, dataListModel.getDataType());
                            } catch (XMPPException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            name = name + dataListModel.getDataName();
                            try {
                                shareMessageBeforeUpload(
                                        dataListModel.getMessageBody(),
                                        remote_jid, dataListModel.getDataType());
                            } catch (XMPPException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }

                    new ShareFtpData(userId, name, remote_jid.split("@")[0])
                            .execute("");
                } else {
                    Toast.makeText(getActivity(), "No data available to share",
                            Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        inAppMessageActivity.getSorting_title().setVisibility(View.GONE);
        GlobalData.OnChatfrag = true;
        if (isgroup == 0) {
            ON_SINGLE_CHAT_PAGE = true;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                contactmodelFlag = Utils.getContactItem(getActivity());// this
                // model
                // use
                // to
                // show
                // hide
                // action
                // bar
            }
        }).start();

        onExitBlockUserUiUpdate();

        if (isgroup == 0) {

            checkPresence();
            byte[] frndpic2 = inAppMessageActivity.setOnLineOffileStatus(
                    remote_jid, isgroup, 1);
            if (frndpic2 != null) {
                frndpic = frndpic2;
            }
			/*
			 * Contactmodel contactmodel = null; try {
			 *
			 * contactmodel = GlobalData.dbHelper.getCustomStatus(remote_jid);
			 * if (contactmodel.getStatus() != null) {
			 * if(contactmodel.getCustomStatus
			 * ()!=null&&contactmodel.getCustomStatus
			 * ().length()>0&&contactmodel.
			 * getCustomStatus().equalsIgnoreCase(GlobalData.INVISIABLE)){
			 *
			 * } if (contactmodel.getStatus().equalsIgnoreCase("1")) {
			 * homeActivity.getmUserStatusTitle().setText("Online"); } else {
			 * LastSeenUpdate(contactmodel.getLastseen()); } } Bitmap pic =
			 * null; if (contactmodel.getAvatar() != null) { pic =
			 * BitmapFactory.decodeByteArray(contactmodel.getAvatar(), 0,
			 * contactmodel.getAvatar().length); frndpic =
			 * contactmodel.getAvatar();
			 * homeActivity.getmActionBarImage().setImageBitmap(pic); } } catch
			 * (Exception e) { contactmodel = null; e.printStackTrace(); }
			 */

        }

    }

    public void LastSeenUpdate(String time) {
        if (isgroup == 0) {
            if (time != null) {
                if (!time.equalsIgnoreCase("")) {

                    inAppMessageActivity.getmUserStatusTitle().setText(
                            "Last seen " + Utils.getRecentmsgDateorTime(time));
                } else {
                    inAppMessageActivity.getmUserStatusTitle().setText(
                            "Offline");
                }
            } else {
                inAppMessageActivity.getmUserStatusTitle().setText("Offline");
            }
        }

    }

    public void showOnlineUser() {

			/*
			 * new Thread(new Runnable() { public void run() {
			 */
            if (getActivity() != null) {

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        try {
                            Contactmodel contactmodel = null;
                            try {
                                contactmodel = GlobalData.dbHelper
                                        .getCustomStatus(remote_jid);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                contactmodel = null;
                                e.printStackTrace();
                            }
                            if (contactmodel != null) {
                                if (contactmodel.getStatus() != null) {
                                    if (contactmodel.getStatus()
                                            .equalsIgnoreCase("1")) {
                                        inAppMessageActivity
                                                .getmUserStatusTitle().setText(
                                                "Online");
                                    } else {
                                        LastSeenUpdate(contactmodel
                                                .getLastseen());
                                    }
                                }
                            }
                        } catch (Exception exception) {

                        }

                    }
                });
            }
            // }
			/* }).start(); */

    }

    public void onExitBlockUserUiUpdate() {
        if (GlobalData.dbHelper.groupIsBlockNew(remote_jid)) {
            un_block_layout.setVisibility(View.GONE);
            block_message_layout_bottm.setVisibility(View.VISIBLE);
        } else {
            un_block_layout.setVisibility(View.VISIBLE);
            block_message_layout_bottm.setVisibility(View.GONE);
            if (chathistorylist.size() <= 5) {

            } else {
                block_layout.setVisibility(View.GONE);
            }
        }
    }

    public void killEmoji() {
        // if (emojicons.getVisibility() == View.VISIBLE) {
        if (emojiconsFragment != null) {
            emojiconsFragment.getmEmojiTabs()[1].performClick();
            // }
        }
    }

    public void hideEmoji() {
        emojicons.setVisibility(View.GONE);
    }

    public Boolean isEmojiVisible() {
        if (emojicons.getVisibility() == View.GONE)
            return false;
        else
            return true;
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();

        ON_SINGLE_CHAT_PAGE = false;
        GlobalData.OnHomefrag = true;

        inAppMessageActivity.actionBarShow();
        GlobalData.OnChatfrag = false;
        ConstantFlag.TAB_BACK_HANDLE_FRAGMENT = "";
        ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
        inAppMessageActivity.invalidateOptionMenuItem();

        obj = null;
        chatFragment = null;
        if (emojicons != null) {
            emojicons.removeAllViews();
        }

        if (getActivity() != null && mSendBtn != null) {
            Utils.hideKeyBoardMethod(getActivity(), mSendBtn);
        }

        if (chatAdapter.getmPlayer() != null) {
            if (chatAdapter.getmPlayer().isPlaying()) {
                chatAdapter.getmPlayer().stop();
            }
        }

        obj = null;
		/*
		 * if (asyncTask != null) { asyncTask.cancel(true); asyncTask = null; }
		 */

        if (reportSpamAsyncTask != null) {
            reportSpamAsyncTask.cancel(true);
            reportSpamAsyncTask = null;
        }

        // Contactmodel contactmodel=Utils.getContactItem(getActivity());

        if (contactmodelFlag != null) {
            if (contactmodelFlag.getUserFromCommanGroup().equalsIgnoreCase("")) {
                inAppMessageActivity.getLayoutTab_contact_chat().setVisibility(
                        View.VISIBLE);
                inAppMessageActivity.onBothTabPageControlIsGone();
            } else {

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        NewContactModelForFlag contactmodel1 = new NewContactModelForFlag();
                        contactmodel1.setUserFromCommanGroup("");
                        Utils.saveContactItem(getActivity(), contactmodel1);
                    }
                }).start();

                inAppMessageActivity.getmUserNameTitle().setText(lastTitle);
                inAppMessageActivity.getLayout_name_status().setVisibility(
                        View.GONE);
                inAppMessageActivity.getmActionBarImage().setVisibility(
                        View.GONE);
                inAppMessageActivity.getActionbar_title().setVisibility(
                        View.VISIBLE);
            }
        } else {
            inAppMessageActivity.getLayoutTab_contact_chat().setVisibility(
                    View.VISIBLE);
            inAppMessageActivity.onBothTabPageControlIsGone();
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                ChatFragment chatFragment = ChatFragment.newInstance("");
                if (chatFragment != null) {
                    GlobalData.dbHelper.resetUnreadmsgCount(remote_jid);
                    chatFragment.refreshChatAdapter();
                }

            }
        }).start();

        try {
            GroupListFrgament frgament = GroupListFrgament.newInstance("");
            if (frgament != null) {
                frgament.clickAbleTrue();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE
                && resultCode == Activity.RESULT_OK) {
            String type = data.getExtras().getString("type");
            String uri = data.getExtras().getString("data");
            if (type.equals("IMG")) {

                String picpath = uri;

                Bitmap originBitmap = null;
                fileType = "";
                sendimgFile(picpath, originBitmap);

                // loadimage(picpath);

            } else if (type.equals("VID")) {
                fileType = "";
                sendVideofile(uri);
            }

        } else if (requestCode == CAMERA_REQUEST
                && resultCode == Activity.RESULT_OK) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(
                    mCapturedImageURI, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndex(filePathColumn[0]);

            String picpath = cursor.getString(column_index);
            cursor.close();

            // Bitmap finalBitmap = BitmapFactory.decodeFile(picpath);
            Bitmap finalBitmap = Utils.decodeFile(picpath,
                    GlobalData.fileorignalWidth, GlobalData.fileorignalheight);
            // finalBitmap = Utils.getResizedBitmap(finalBitmap,
            // GlobalData.fileorignalheight, GlobalData.fileorignalWidth);

            try {
                String savedpicpath = Utils.getfilePath(GlobalData.Imagefile);
                FileOutputStream fos = new FileOutputStream(savedpicpath);

                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

                fos.close();
                Utils.saveImageInGallery(inAppMessageActivity, savedpicpath);
                if (finalBitmap != null) {
                    finalBitmap.recycle();
                }
                if (savedpicpath != null && savedpicpath.trim().length() != 0) {
                    fileType = "";
                    fileType = GlobalData.Imagefile;
                    uploadAndSendSelectedfile(savedpicpath, fileType);
                }

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == VIDEO_REQUEST
                && resultCode == Activity.RESULT_OK) {

            if (videoPath != null && videoPath.trim().length() != 0) {
                sendVideofile(videoPath);
            }

        } else if (requestCode == 301 && resultCode == Activity.RESULT_OK) {
            // refreshChatAdapter();
        } else if (requestCode == SELECT_TEMPLATE_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {

            mEditEmojicon.setText(data.getStringExtra("templateText"));

        } else if (requestCode == SELECT_EMAIL_TEMPLATE_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            mEmojiBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.VISIBLE);
            String templateTitle = data.getStringExtra("TemplateTitle");
            templateId = data.getStringExtra("TemplateId");
            mEditEmojicon.setText("Template: " + templateTitle);
            mEditEmojicon.setEnabled(false);
//            email_check_box.setChecked(true);
            /*Amit*/
            email_check_box.setChecked(true);

            // if(check_box.isChecked())
            {
                check_box.setChecked(false);
                check_box.setEnabled(false);
                if (check_box.isChecked()) {
                    Toast.makeText(getActivity(),
                            "Selected template can be sent to mail only",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public void setChathistory() {

        int historycount = 0;
        chathistorylist.clear();
        previousrowid = "0";
        String timeStap = Utils.currentTimeStap();

        chathistorylist.addAll(GlobalData.dbHelper.getChathistoryfromDB(
                remote_jid, previousrowid, timeStap));
        historycount = GlobalData.dbHelper.getHistorycount(remote_jid);

        if (chathistorylist.size() == 0) {

            if (getActivity() != null) {
                fc_loadmsglay.setVisibility(View.GONE);
                loadmoremsgshow = false;

            }
        } else {

            if (historycount < 50) {
                fc_loadmsglay.setVisibility(View.GONE);
                loadmoremsgshow = false;

            } else {
                fc_loadmsglay.setVisibility(View.VISIBLE);// new commit
                loadmoremsgshow = true;
            }
            chatAdapter.setChatListArrayList(chathistorylist);
            chatAdapter.notifyDataSetChanged();
            mListView.setSelection(chathistorylist.size());

            // if (chathistorylist.size() < 4) {
            // block_layout.setVisibility(View.VISIBLE);
            // } else {
            // block_layout.setVisibility(View.GONE);
            // }

            if (isgroup == 1 || isgroup == 2) {
                block_layout.setVisibility(View.GONE);
            }

            reportSpamButton();// visual hide button

        }

        if (!chatAdapter.isEmpty()) {
            loading_text.setVisibility(View.GONE);
          /*  mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.setSelection(chathistorylist.size());
                }
            });*/
        } else {
            loading_text.setText("No Message history found!");
        }

    }

    public void setChathistory2() {

        int historycount = 0;
        chathistorylist.clear();
        previousrowid = "0";
        String timeStap = Utils.currentTimeStap();

        chathistorylist.addAll(GlobalData.dbHelper.getChathistoryfromDB(
                remote_jid, previousrowid, timeStap));
        historycount = GlobalData.dbHelper.getHistorycount(remote_jid);

        if (chathistorylist.size() == 0) {

            if (getActivity() != null) {
                fc_loadmsglay.setVisibility(View.GONE);
                loadmoremsgshow = false;

            }
        } else {

            if (historycount < 50) {
                fc_loadmsglay.setVisibility(View.GONE);
                loadmoremsgshow = false;

            } else {
                fc_loadmsglay.setVisibility(View.VISIBLE);// new commit
                loadmoremsgshow = true;
            }
            chatAdapter.setChatListArrayList(chathistorylist);
            chatAdapter.notifyDataSetChanged();
            // mListView.setSelection(chathistorylist.size());

            // if (chathistorylist.size() <= 5) {
            // block_layout.setVisibility(View.VISIBLE);
            // } else {
            // block_layout.setVisibility(View.GONE);
            // }

            if (isgroup == 1 || isgroup == 2) {
                block_layout.setVisibility(View.GONE);
            }

            reportSpamButton();// visual hide button

        }

        if (!chatAdapter.isEmpty()) {
            loading_text.setVisibility(View.GONE);
          /*  mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.setSelection(chathistorylist.size());
                }
            });*/

        } else {
            loading_text.setText("No Message history found");

        }

        if (Utils.isDeviceOnline(getActivity())) {
            if (isgroup == 0 || isgroup == 1) {
                getLoadMoreMessageHistoryFromServer(previousTime, false);
            }
        }

    }

    @Override
    public void ChangeFragment(ArrayList<FetchGroupDetails.GroupDetails> groupNameTagList, ArrayList<String> groupsArrayList) {
        /*no use*/
    }

    @Override
    public void setSendersId(ArrayList<FetchSenderIDs> sendersId) {
        /*no use*/
    }

    @Override
    public void Backpressed() {

    }

    class getMoreChatHistory extends AsyncTask<Void, Void, Void> {
        ArrayList<Chatmodel> tempchatlist;
        int scrollposition = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            previousrowid = chathistorylist.get(0).getMsgtime();// previousrowid
            // exchange to
            // time
            previousTime = chathistorylist.get(0).getMsgtime();
            tempchatlist = new ArrayList<Chatmodel>();
        }

        @Override
        protected Void doInBackground(Void... params) {

            tempchatlist.addAll(GlobalData.dbHelper.getChathistoryfromDB(
                    remote_jid, previousrowid, previousrowid));

            if (tempchatlist.size() > 0) {
                scrollposition = tempchatlist.size();
                previousrowid = chathistorylist.get(0).getMsgtime();// previousrowid
                // exchange
                // to time
            }
            chathistorylist.addAll(0, tempchatlist);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (tempchatlist.size() == 0) {
                fc_loadmsgprogress.setVisibility(View.GONE);
                fc_loadmsglay.setVisibility(View.GONE);
                loadmoremsgshow = false;
            } else {
                if (tempchatlist.size() < 50) {
                    fc_loadmsgprogress.setVisibility(View.GONE);
                    fc_loadmsglay.setVisibility(View.GONE);
                    loadmoremsgshow = false;
                } else {
                    loadmoremsgshow = true;

                }
                fc_loadmsgprogress.setVisibility(View.GONE);
                chatAdapter.setChatListArrayList(chathistorylist);
                chatAdapter.notifyDataSetChanged();

                mListView.setSelection(scrollposition);
            }

            if (Utils.isDeviceOnline(getActivity())) {

                if (isgroup == 0 || isgroup == 1) {
                    boolean lomdm = false;
                    lomdm = !loadmoremsgshow;
                    getLoadMoreMessageHistoryFromServer(previousTime, lomdm);
                }
            }

        }
    }

    public void getMoreChatHistoryDownlodedFromServerMethod() {

        if (Utils.isDeviceOnline(getActivity())) {
            ChatHistoryDownlodedFromServerAfterRefresh chatHistoryDownlodedFromServer = new ChatHistoryDownlodedFromServerAfterRefresh();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                chatHistoryDownlodedFromServer
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                chatHistoryDownlodedFromServer.execute();
            }
        }

    }

    class ChatHistoryDownlodedFromServerAfterRefresh extends
            AsyncTask<Void, Void, Void> {
        ArrayList<Chatmodel> tempchatlist = new ArrayList<Chatmodel>();
        int scrollposition = 0;
        String previousrowidNew = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            previousrowidNew = chathistorylist.get(0).getMsgtime();// previousrowid
            // exchange
            // to time

        }

        @Override
        protected Void doInBackground(Void... params) {
            if (previousrowidNew.equalsIgnoreCase(previousrowid)) {
                tempchatlist.addAll(GlobalData.dbHelper.getChathistoryfromDB(
                        remote_jid, previousrowid, previousrowid));
            }

            if (tempchatlist != null) {
                if (tempchatlist.size() > 0) {
                    scrollposition = tempchatlist.size();
                    previousrowid = chathistorylist.get(0).getMsgtime();// previousrowid
                    // exchange
                    // to
                    // time
                }
                chathistorylist.addAll(0, tempchatlist);
            } else {
                tempchatlist = new ArrayList<Chatmodel>();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (tempchatlist.size() == 0) {
                fc_loadmsgprogress.setVisibility(View.GONE);
                fc_loadmsglay.setVisibility(View.GONE);
                loadmoremsgshow = false;
            } else {
                if (tempchatlist.size() < 50) {
                    fc_loadmsgprogress.setVisibility(View.GONE);
                    fc_loadmsglay.setVisibility(View.GONE);
                    // loadmoremsgshow = false;
                } else {
                    // loadmoremsgshow = true;

                }
                fc_loadmsgprogress.setVisibility(View.GONE);
                fc_loadmsglay.setVisibility(View.VISIBLE);// new commit
                chatAdapter.setChatListArrayList(chathistorylist);

                mListView.setSelection(scrollposition);
            }

        }
    }

	/*
	 * public Bitmap getBitmapFromURL(String src) { try {
	 *
	 * //final String encodedUrl = URLEncoder.encode(src, "utf-8"); java.net.URL
	 * url = new java.net.URL(src); HttpURLConnection connection =
	 * (HttpURLConnection) url .openConnection(); connection.setDoInput(true);
	 * connection.connect(); InputStream input = connection.getInputStream();
	 * Bitmap myBitmap = BitmapFactory.decodeStream(input); return myBitmap; }
	 * catch (IOException e) { e.printStackTrace(); return null; } }
	 */

    private void setChatupdateHandler() {
        // TODO Auto-generated method stub
        ChatupdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                if (getActivity() != null) {
                    String message = msg.getData().getString("message");
                    String senderid = msg.getData().getString("senderid");
                    String filePath = msg.getData().getString("filePath");
                    String rowid = msg.getData().getString("rowid");
                    String packetId = msg.getData().getString("packetId");
                    final String msgTime = msg.getData().getString("msgtime");

                    // byte[] avatar = msg.getData().getByteArray("picdata");
                    final Chatmodel model = new Chatmodel();
                    if (message
                            .startsWith(SingleChatRoomFrgament.sendfilefixString)) {
                        String[] urls = message.split("__");
                        String type = urls[2];
                        model.setMediatype(type);
                        model.setMediapath(filePath);
                        model.setMediaUrl(urls[1]);

                        if (!type.equals(GlobalData.Audiofile)) {

                            Picasso.with(getActivity())
                                    .load(model.getMediaUrl())
                                    .into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(
                                                final Bitmap bitmap,
                                                Picasso.LoadedFrom from) {

                                            // Set it in the ImageView
                                            model.setThmbbitmap(bitmap);
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable arg0) {
                                            // TODO Auto-generated method stub
                                            Utils.printLog("onBitmapFailed");
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable arg0) {
                                            // TODO Auto-generated method stub
                                            Utils.printLog("onPrepareLoad ");
                                        }
                                    });

							/*
							 * new Thread(new Runnable() {
							 *
							 * @Override public void run() { // TODO
							 * Auto-generated method stub final Bitmap
							 * bmp=getBitmapFromURL(model.getMediaUrl());
							 *
							 * getActivity().runOnUiThread(new Runnable() {
							 *
							 * @Override public void run() { // TODO
							 * Auto-generated method stub
							 * model.setThmbbitmap(bmp); } });
							 *
							 * } }).start();
							 */

                        }
                        if (type.equals(GlobalData.Locationfile)) {
                            String latln = urls[3];
                            if (latln != null && latln.trim().length() != 0) {
                                model.setChatmessage(latln);
                            }

                        }

                    } else {
                        model.setChatmessage(message);
                    }

                    if (packetId != null && GlobalData.connection != null
                            && GlobalData.connection.isConnected()
                            && isgroup == 0) {
                        MessageEventManager eventManager = new MessageEventManager(
                                GlobalData.connection);
                        if (eventManager != null && packetId != null
                                && packetId.trim().length() > 0) {
                            eventManager.sendDisplayedNotification(remote_jid,
                                    packetId.trim());
                            Utils.printLog("Send read status==> " + packetId);
                            GlobalData.dbHelper.updatestatusOfSentMessage(
                                    StringUtils.parseBareAddress(remote_jid),
                                    packetId.trim(), "read");
                        }
                    }

                    model.setMine(false);
                    model.setRemote_userid(senderid);
                    model.setMessagerowid(rowid);
                    model.setMsgDate(Utils.getmsgDate(msgTime));
                    model.setMsgTime(Utils.getmsgTime(msgTime));
                    model.setMsgtime(msgTime);
                    // if (avatar != null) {
                    // model.setPic(avatar);
                    // }

                    chathistorylist.add(model);

                    try {

                        Collections
                                .sort(chathistorylist,
                                        new Chatmodel().new CustomComparatorSortByTime());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    chatAdapter.notifyDataSetChanged();

                    // mListView.setSelection(chathistorylist.size() - 1);
                 /*   mListView.post(new Runnable() {
                        @Override
                        public void run() {
                            mListView.setSelection(chathistorylist.size());
                        }
                    });*/

                    if (chathistorylist.size() > 0) {
                        loading_text.setVisibility(View.GONE);
                    }

                }
            }
        };
    }

    private void setGroupChatHandler() {
        // TODO Auto-generated method stub
        groupChatHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                if (getActivity() != null) {
                    int idIsblock = msg.getData().getInt("key");
                    if (idIsblock == 2) {// 2 mean block

                        onExitBlockUserUiUpdate();

                    } else {
                        if (mucChat != null) {
                            if (!mucChat.isJoined()) {

                                mucChat = joinGroup(remote_jid);

                            }

                        }
                        onExitBlockUserUiUpdate();
                    }
                }
            }
        };
    }

    private void setOnLineOfflineHandler() {
        onLineOfflineHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

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
                                if (contactmodel.getStatus().equalsIgnoreCase(
                                        "1")) {
                                    inAppMessageActivity.getmUserStatusTitle()
                                            .setText("Online");
                                } else {
                                    if (!(GlobalData.connection != null && GlobalData.connection
                                            .isConnected())) {
                                        chatManager = null;
                                    }
                                    LastSeenUpdate(contactmodel.getLastseen());
                                }
                            }
                            frndname = contactmodel.getName();
                            inAppMessageActivity.getmUserNameTitle().setText(
                                    frndname);

                            Bitmap pic = null;
                            frndpic = contactmodel.getAvatar();
                            if (frndpic != null) {
                                pic = BitmapFactory.decodeByteArray(frndpic, 0,
                                        frndpic.length);
                                inAppMessageActivity.getmActionBarImage()
                                        .setImageBitmap(pic);
                            }
                        }
                    } catch (Exception exception) {

                    }
                }
            }
        };
    }

    public MultiUserChat joinGroup(String groip_jid) {
        MultiUserChat muc = null;

        if (GlobalData.connection != null
                && GlobalData.connection.isConnected()) {
            if (InAppMessageActivity.myModel != null) {
                muc = new MultiUserChat(GlobalData.connection, groip_jid);
                try {
                    DiscussionHistory history = new DiscussionHistory();
                    history.setMaxStanzas(0);
                    try {
                        muc.join(InAppMessageActivity.myModel.getRemote_jid(),
                                null, history,
                                SmackConfiguration.getPacketReplyTimeout());
                        muc.addParticipantStatusListener(new Groupchatparticipentchange());
                        muc.addUserStatusListener(new GroupChatMyStatusListner());
                    } catch (XMPPException e) {
                        e.printStackTrace();
                        muc = null;
                    }

                } catch (Exception e) {
                    muc = null;
                    e.printStackTrace();
                }
            }
        }

        return muc;

    }

    private void inItHandlerRefreshAdapter() {
        // TODO Auto-generated method stub
        handlerRefreshAdapter = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (getActivity() != null) {
                    // setChathistory();
                    setChathistory2();
                }
            }
        };
    }

    public void setAudiorecordHandler() {
        RecordAudioHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                if (recordfilePath.trim().length() != 0) {
                    fileType = "";
                    fileType = GlobalData.Audiofile;
                    uploadAndSendSelectedfile(recordfilePath, fileType);
                }
            }
        };
    }

    public void messageSend(String msg) {
        try {
            String msgPacketId = sendMessage(msg, remote_jid);
            // AddSentMessagetochatandDB(msg, msgPacketId,"1",false);// false
            // mean user is online
            mListView.setSelection(chathistorylist.size() - 1);

			/*
			 * if(chathistorylist!=null){
			 *
			 * if(chathistorylist.size()>0){
			 * if(chathistorylist.get(0).isMine()){
			 * mSpamReportBtn.setVisibility(View.GONE); } }else{
			 * if(!msgPacketId.equalsIgnoreCase("")){
			 * mSpamReportBtn.setVisibility(View.GONE); } } }
			 */
            reportSpamButton();// visual hide button
            // mEditEmojicon.setText("");
            Utils.printLog("Send message: " + msg);
            // if (Utils.isDeviceOnline(getActivity())) {
            // if (isgroup == 0 || isgroup == 1) {
            // getLoadMoreMessageHistoryFromServer(previousTime, false);
            // }
            // }
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Utils.printLog("Send message: Excption");
        }
    }

    public void messageSendOffline(String msg, String sentmessahe_status) {
        try {
            String msgPacketId = sendMessageOffline(msg, remote_jid);
            String msgtime = Utils.currentTimeStap();
            String row_id = AddSentMessagetochatandDB(msg, msgPacketId,
                    sentmessahe_status, true, msgtime);
            // AddSentMessagetochatandDBForOffline(msg,
            // msgPacketId,"","","",row_id);
            mListView.setSelection(chathistorylist.size() - 1);

            // mEditEmojicon.setText("");
            Utils.printLog("Send message: " + msg);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Utils.printLog("Send message: Excption");
        }
    }

    public org.jivesoftware.smack.packet.Message shareMessageBeforeUpload(
            String message, String buddyJID, String type) throws XMPPException {

        org.jivesoftware.smack.packet.Message msgpacketid = null;

        Utils.printLog("message send to server..message" + message);

        Utils.printLog("message send to server..and isgroup is" + isgroup);

        if (isgroup == 1) {
            if (mucChat != null) {

                // String timeStap= Utils.currentTimeStamp();
                String timeStap = Utils.currentTimeStap();
                String userId = InAppMessageActivity.USER_ID;
                Slacktags slagTag = new Slacktags(
                        InAppMessageActivity.myModel.getNumber(), userId,
                        GlobalData.MESSAGE_TYPE_2, timeStap, userId + "_"
                        + timeStap);

                org.jivesoftware.smack.packet.Message msg = mucChat
                        .createMessage();

                // final int lastIndexOf = message.lastIndexOf("__");
                // final String message3 = message.substring(0, lastIndexOf);

                msg.setBody(message);
                msg.addExtension(slagTag);
                msg.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
                // if (message != null) {
                // addfiletoUIandDB(filePath, fileTy, msg.getPacketID(), "1",
                // true, timeStap);
                // }

                msgpacketid = msg;

            }
        } else {
            if (isgroup == 2) {
                if (chatManager != null) {

                    Utils.printLog("broadcast send start...");
                    String timeStap = Utils.currentTimeStap();

                    String userId = InAppMessageActivity.USER_ID;
                    Slacktags slagTag = new Slacktags(
                            InAppMessageActivity.myModel.getNumber(), userId,
                            GlobalData.MESSAGE_TYPE_2, timeStap, userId + "_"
                            + timeStap);
                    org.jivesoftware.smack.packet.Message msg = new org.jivesoftware.smack.packet.Message();
                    msg.setType(org.jivesoftware.smack.packet.Message.Type.chat);
                    // final int lastIndexOf = message.lastIndexOf("__");
                    // final String message3 = message.substring(0,
                    // lastIndexOf);
                    msg.setBody(message);
                    msg.addExtension(slagTag);
                    // if (message != null) {
                    // addfiletoUIandDB(filePath, fileTy, msg.getPacketID(),
                    // "1", true, timeStap);
                    // }
                    msgpacketid = msg;
                    Utils.printLog("broadcast send complete...");

                }

            } else {
                if (chatManager != null) {

                    // Chat chat = chatManager.createChat(buddyJID, null);

					/*
					 * org.jivesoftware.smack.packet.Message msg = chat
					 * .generateMessage(message);
					 */

                    Utils.printLog("setBody===" + message);

                    String timeStap = Utils.currentTimeStap();
                    String userId = InAppMessageActivity.USER_ID;

                    Slacktags slagTag = new Slacktags(
                            InAppMessageActivity.myModel.getNumber(), userId,
                            GlobalData.MESSAGE_TYPE_2, timeStap, userId + "_"
                            + timeStap);
                    org.jivesoftware.smack.packet.Message msg2 = new org.jivesoftware.smack.packet.Message();
                    msg2.setType(org.jivesoftware.smack.packet.Message.Type.normal);

                    // http://nowconnect.in/25398/SMS19_1466686557386.jpg

                    // final String arr[]=message.split("__");

                    // final String message2="http://"+arr[1];

                    // final int lastIndexOf = message.lastIndexOf("__");
                    // final String message3 = message.substring(0,
                    // lastIndexOf);//
                    // file__http://nowconnect.in/121/Kitever_1472532377427.jpg__I
                    // file__http://nowconnect.in/121/VID-20160808-WA0001.mp4__V
                    message = "file__" + message + "__" + type;
                    msg2.setBody(message);
                    msg2.addExtension(slagTag);

					/*
					 * MessageEventManager eventManager = new
					 * MessageEventManager( GlobalData.connection);
					 */
                    MessageEventManager.addNotificationsRequests(msg2, true,
                            true, true, false);

                    // if (message != null) {
                    // addfiletoUIandDB(filePath, fileTy, msg2.getPacketID(),
                    // "1", true, timeStap);
                    // }

                    Utils.printLog("setXMLtttt===" + msg2.toXML());

                    msgpacketid = msg2;

                    // reportSpamButton();// visual hide button

                }
            }
        }
        return msgpacketid;

    }

    public org.jivesoftware.smack.packet.Message sendMessageBeforeUpload(
            final String message, String buddyJID, final String filePath,
            final String fileTy) throws XMPPException {
        org.jivesoftware.smack.packet.Message msgpacketid = null;

        Utils.printLog("message send to server..message" + message);

        Utils.printLog("message send to server..and isgroup is" + isgroup);

        if (isgroup == 1) {
            if (mucChat != null) {

                // String timeStap= Utils.currentTimeStamp();
                String timeStap = Utils.currentTimeStap();
                String userId = InAppMessageActivity.USER_ID;
                Slacktags slagTag = new Slacktags(
                        InAppMessageActivity.myModel.getNumber(), userId,
                        GlobalData.MESSAGE_TYPE_2, timeStap, userId + "_"
                        + timeStap);

                org.jivesoftware.smack.packet.Message msg = mucChat
                        .createMessage();

                final int lastIndexOf = message.lastIndexOf("__");
                final String message3 = message.substring(0, lastIndexOf);

                msg.setBody(message3);
                msg.addExtension(slagTag);
                msg.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
                if (message != null) {
                    addfiletoUIandDB(filePath, fileTy, msg.getPacketID(), "1",
                            true, timeStap);
                }

                msgpacketid = msg;

            }
        }
        return msgpacketid;
    }

    @SuppressWarnings("static-access")
    public String sendMessage(final String message, String buddyJID)
            throws XMPPException {
        String msgpacketid = "";
        Utils.printLog("message send to server..and isgroup is" + isgroup);

        if (isgroup == 1) {
            if (mucChat != null) {

                String timeStap = Utils.currentTimeStap();
                String userId = InAppMessageActivity.USER_ID;
                Slacktags slagTag = new Slacktags(
                        InAppMessageActivity.myModel.getNumber(), userId,
                        GlobalData.MESSAGE_TYPE_2, timeStap, userId + "_"
                        + timeStap);

                org.jivesoftware.smack.packet.Message msg = mucChat
                        .createMessage();
                msg.setBody(message);
                msg.addExtension(slagTag);
                msg.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);

                if (mucChat.isJoined()) {
                    mucChat.sendMessage(msg);
                    String row_id = AddSentMessagetochatandDB(message, "", "1",
                            false, timeStap);
                } else {
                    mucChat.sendMessage(msg);
                    String row_id = AddSentMessagetochatandDB(message, "", "1",
                            true, timeStap);
                }
            }
        } else {
            if (isgroup == 2) {
                if (chatManager != null) {

                    new Thread(new Runnable() {
                        @SuppressWarnings("static-access")
                        public void run() {
                            Utils.printLog("broadcast send start...");
                            String timeStap = Utils.currentTimeStap();
                            String row_id = "";
                            boolean checkBoxIsCheck = check_box.isChecked();
                            String expirytime = "";
                            if (checkBoxIsCheck) {
                                expirytime = timeInMinutes();
                            }

                            Utils.printLog("broadcast send complete...");
                        }
                    }).start();

                }

            } else {
                if (chatManager != null) {

                    Chat chat = chatManager.createChat(buddyJID, null);

					/*
					 * org.jivesoftware.smack.packet.Message msg = chat
					 * .generateMessage(message);
					 */

                    Utils.printLog("setBody===" + message);

                    String timeStap = Utils.currentTimeStap();
                    String userId = InAppMessageActivity.USER_ID;

                    Slacktags slagTag = new Slacktags(
                            InAppMessageActivity.myModel.getNumber(), userId,
                            GlobalData.MESSAGE_TYPE_2, timeStap, userId + "_"
                            + timeStap);
                    org.jivesoftware.smack.packet.Message msg2 = new org.jivesoftware.smack.packet.Message();
                    msg2.setType(org.jivesoftware.smack.packet.Message.Type.normal);
                    msg2.setBody(message);
                    msg2.addExtension(slagTag);

					/*
					 * MessageEventManager eventManager = new
					 * MessageEventManager( GlobalData.connection);
					 */
                    MessageEventManager.addNotificationsRequests(msg2, true,
                            true, true, false);

                    Utils.printLog("setXMLtttt===" + msg2.toXML());
                    msgpacketid = msg2.getPacketID();

                    try {
                        chat.sendMessage(msg2);

                        AddSentMessagetochatandDB(message, msgpacketid, "1",
                                false, timeStap);

                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                        String row_id = AddSentMessagetochatandDB(message,
                                msgpacketid, "1", true, timeStap);

                    }

                    reportSpamButton();// visual hide button

                }
            }
        }
        return msgpacketid;
    }

    public String sendMessageOffline(final String message, String buddyJID)
            throws XMPPException {
        String msgpacketid = "";
        Utils.printLog("message send to server..and isgroup is" + isgroup);

        if (isgroup == 1) {

            String timeStap = Utils.currentTimeStap();
            String userId = InAppMessageActivity.USER_ID;

        }
        return msgpacketid;
    }

    public String AddSentMessagetochatandDB(final String message,
                                            final String message_packetID, final String sent_success,
                                            final boolean isoffline, final String msgtime) {

        stringRowId = "";

        new Thread(new Runnable() {
            public void run() {
                String message_id = "";// use for broadcast to update message
                // counter on get delivery report count
                // update

                rowid = GlobalData.dbHelper.addchatToMessagetable(remote_jid,
                        message, myModel.getRemote_jid(), msgtime,
                        message_packetID, sent_success, "0");
                int mail_count = 0;
                stringRowId = String.valueOf(rowid);               


                if (isoffline) {
                    AddSentMessagetochatandDBForOffline(message,
                            message_packetID, "", "", "", stringRowId, msgtime);
                }

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Chatmodel model = new Chatmodel();
                        model.setChatmessage(message);
                        model.setMine(true);
                        model.setMessagerowid(String.valueOf(rowid));
                        model.setMsgDate(Utils.getmsgDate(msgtime));
                        model.setMsgTime(Utils.getmsgTime(msgtime));
                        model.setMsg_packetid(message_packetID);
                        model.setSent_msg_success(sent_success); // add the sent
                        // msg tick
                        // default

                       
                        chathistorylist.add(model);
                        chatAdapter.notifyDataSetChanged();
                        // mListView.setSelection(chathistorylist.size() - 1);
                        Utils.printLog("add message in ui--" + message);

                      /*  mListView.post(new Runnable() {
                            @Override
                            public void run() {
                                mListView.setSelection(chathistorylist.size());
                            }
                        });*/

                        check_box.setChecked(false);
                        email_check_box.setChecked(false);

                    }
                });

                if (rowid != -1) {

                    GlobalData.dbHelper.addorupdateRecentTable(remote_jid,
                            String.valueOf(rowid));

                    GlobalData.dbHelper.updateContactmsgData(remote_jid,
                            message, msgtime);

                    
                }

            }
        }).start();

        return stringRowId;
    }

    public void AddSentMessagetochatandDBForOffline(final String message,
                                                    final String message_packetID, final String path,
                                                    final String type, final String status, final String row_id,
                                                    final String msgtime) {

		/*
		 * new Thread(new Runnable() { public void run() {
		 */
        // final String msgtime = String.valueOf(System.currentTimeMillis());
        // final String msgtime = Utils.currentTimeStap();

        rowid = GlobalData.dbHelper.addchatToOfflineMessagetable(remote_jid,
                message, myModel.getRemote_jid(), msgtime, message_packetID,
                "0", InAppMessageActivity.myModel.getRemote_jid(), path, type,
                status, row_id);

		/*
		 * } }).start();
		 */
    }


    public void uploadAndSendSelectedfile(final String path,
                                          final String filetype) {
        if (Utils.isDeviceOnline(getActivity())) {
            if (GlobalData.connection.isConnected()) {
                loadimage(path, filetype, false, null);
            } else {
                loadimageOfOffline(path, filetype);
            }
        } else {
            loadimageOfOffline(path, filetype);
        }
        // loadimageOfOffline(path,filetype);
    }

    public String addfiletoUIandOfflineDB(final String filepath,
                                          final String filetype, final String msgPacketId,
                                          final String sendfilestaus, final boolean fileIsupload,
                                          final boolean isOffline, final String msgtime) {
        stringRowId = "";
        new Thread(new Runnable() {
            public void run() {
                // final String msgtime =
                // String.valueOf(System.currentTimeMillis());
                // final String msgtime = Utils.currentTimeStap();

                if (filetype.equals(GlobalData.Locationfile)) {
                    rowid = GlobalData.dbHelper.addchatFileToMessagetable(
                            remote_jid, filepath, filetype,
                            myModel.getRemote_jid(), "S", "", latlng, "",
                            msgtime, msgPacketId, sendfilestaus, fileIsupload);
                } else {
                    rowid = GlobalData.dbHelper.addchatFileToMessagetable(
                            remote_jid, filepath, filetype,
                            myModel.getRemote_jid(), "S", "", "", "", msgtime,
                            msgPacketId, sendfilestaus, fileIsupload);
                }

                if (isOffline) {
                    stringRowId = String.valueOf(rowid);
                    AddSentMessagetochatandDBForOffline("", "", filepath,
                            filetype, "0", stringRowId, msgtime);
                }

                if (rowid != -1) {
                    GlobalData.dbHelper.addorupdateRecentTable(remote_jid,
                            String.valueOf(rowid));
                    GlobalData.dbHelper.updateContactmsgData(remote_jid,
                            Utils.updatemessage(filetype) + " sent", msgtime);                   
                }

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        GlobalData.shareFilepath = "";
                        GlobalData.shareFiletype = "";

                        Chatmodel model = new Chatmodel();
                        model.setMediatype(filetype);
                        model.setStatus("S");
                        model.setMessagerowid(String.valueOf(rowid));
                        model.setMediapath(filepath);
                        model.setMine(true);
                        model.setMsgDate(Utils.getmsgDate(msgtime));
                        model.setMsgTime(Utils.getmsgTime(msgtime));
                        model.setMsg_packetid(msgPacketId);
                        model.setSent_msg_success("0");
                        if (filetype.equals(GlobalData.Imagefile)
                                || filetype.equals(GlobalData.Locationfile)) {
                            model.setOrignalbitmap(BitmapFactory
                                    .decodeFile(filepath));
                        } else if (filetype.equals(GlobalData.Videofile)) {
                            Bitmap thumbnail = ThumbnailUtils
                                    .createVideoThumbnail(
                                            filepath,
                                            MediaStore.Images.Thumbnails.MINI_KIND);
                            if (thumbnail != null) {
                                model.setOrignalbitmap(thumbnail);
                            }
                        }
                        if (filetype.equals(GlobalData.Locationfile)) {
                            model.setChatmessage(latlng);
                        }
                        chathistorylist.add(model);
                        chatAdapter.notifyDataSetChanged();
                        // mListView.setSelection(chathistorylist.size() - 1);
                     /*   mListView.post(new Runnable() {
                            @Override
                            public void run() {
                                mListView.setSelection(chathistorylist.size());
                            }
                        });*/

                    }
                });
            }
        }).start();
        return stringRowId;
    }

    public void addfiletoUIandDB(final String filepath, final String filetype,
                                 final String msgPacketId, final String sendfilestaus,
                                 final boolean fileIsupload, final String msgtime) {

        new Thread(new Runnable() {
            public void run() {

                if (filetype.equals(GlobalData.Locationfile)) {
                    rowid = GlobalData.dbHelper.addchatFileToMessagetable(
                            remote_jid, filepath, filetype,
                            myModel.getRemote_jid(), /* "S" */"R", "", latlng,
                            "", msgtime, msgPacketId, sendfilestaus,
                            fileIsupload);
                } else {

                    rowid = GlobalData.dbHelper.addchatFileToMessagetableNew(
                            remote_jid, filepath, filetype,
                            myModel.getRemote_jid(), /* "S" */"R", "", "", "",
                            msgtime, msgPacketId, "1", "0", "0", fileIsupload);

                }

                if (rowid != -1) {
                    GlobalData.dbHelper.addorupdateRecentTable(remote_jid,
                            String.valueOf(rowid));
                    GlobalData.dbHelper.updateContactmsgData(remote_jid,
                            Utils.updatemessage(filetype) + " sent", msgtime);                    
                }

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        GlobalData.shareFilepath = "";
                        GlobalData.shareFiletype = "";

                        Chatmodel model = new Chatmodel();
                        model.setMediatype(filetype);
                        // model.setStatus("S");
                        model.setStatus("R");
                        model.setMessagerowid(String.valueOf(rowid));
                        model.setMediapath(filepath);
                        model.setMine(true);
                        model.setMsgDate(Utils.getmsgDate(msgtime));
                        model.setMsgTime(Utils.getmsgTime(msgtime));
                        model.setBroadCastTime(msgtime);
                        model.setMsg_packetid(msgPacketId);
                        model.setSent_msg_success("1");

                        if (filetype.equals(GlobalData.Imagefile)
                                || filetype.equals(GlobalData.Locationfile)) {
                            model.setOrignalbitmap(BitmapFactory
                                    .decodeFile(filepath));
                        } else if (filetype.equals(GlobalData.Videofile)) {
                            Bitmap thumbnail = ThumbnailUtils
                                    .createVideoThumbnail(
                                            filepath,
                                            MediaStore.Images.Thumbnails.MINI_KIND);
                            if (thumbnail != null) {
                                model.setOrignalbitmap(thumbnail);
                            }

                            model.setFileIsUploaded(true);

                        }
                        if (filetype.equals(GlobalData.Locationfile)) {
                            model.setChatmessage(latlng);
                        }
                        chathistorylist.add(model);
                        chatAdapter.notifyDataSetChanged();
                        // mListView.setSelection(chathistorylist.size() - 1);
                      /*  mListView.post(new Runnable() {
                            @Override
                            public void run() {
                                mListView.setSelection(chathistorylist.size());
                            }
                        });*/

                    }
                });
            }
        }).start();
    }

    public void sendimgFile(String picpath, Bitmap originBitmap) {
        // Bitmap finalBitmap = BitmapFactory.decodeFile(picpath);
        Bitmap finalBitmap = null;
        finalBitmap = Utils.decodeFile(picpath, GlobalData.fileorignalWidth,
                GlobalData.fileorignalheight);

        // finalBitmap = Utils.getResizedBitmap(finalBitmap,
        // GlobalData.fileorignalheight, GlobalData.fileorignalWidth);
        // finalBitmap = Utils.oritRotation(finalBitmap,
        // Uri.parse(picpath));
        try {
            // String dataSize=calculateFileSize(picpath);
            String savedpicpath = Utils.getfilePath(GlobalData.Imagefile);
            // savedpicpath=dataSize+savedpicpath;
            FileOutputStream fos = new FileOutputStream(savedpicpath);
            if (originBitmap != null) {
                originBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            } else {
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            }
            fos.close();
            Utils.saveImageInGallery(inAppMessageActivity, savedpicpath);
            if (finalBitmap != null) {
                finalBitmap.recycle();
            }
            if (originBitmap != null) {
                originBitmap.recycle();
            }
            if (savedpicpath != null && savedpicpath.trim().length() != 0) {
                fileType = GlobalData.Imagefile;
                uploadAndSendSelectedfile(savedpicpath, fileType);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendVideofile(String videoPath) {
        Bitmap finalBitmap = ThumbnailUtils.createVideoThumbnail(videoPath,
                MediaStore.Images.Thumbnails.MINI_KIND);
        try {
            videothmbpath = Utils.getfilePath(GlobalData.Imagefile);
            FileOutputStream fos = new FileOutputStream(videothmbpath);

            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            Utils.saveImageInGallery(inAppMessageActivity, videoPath);

            fos.close();
            if (finalBitmap != null) {
                finalBitmap.recycle();
            }
            if (videoPath != null && videoPath.trim().length() != 0) {
                if (new File(videoPath).exists()) {
                    fileType = GlobalData.Videofile;
                    uploadAndSendSelectedfile(videoPath, fileType);
                }
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {

    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        emojicons.setVisibility(View.VISIBLE);

        try {

            if (emojicons != null) {
                emojicons.removeAllViews();
            }

            emojiconsFragment = EmojiconsFragment.newInstance(useSystemDefault);

            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.emojicons,
							/* EmojiconsFragment.newInstance(useSystemDefault)) */emojiconsFragment, "emoji")
                    .commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEditEmojicon);

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
        stopPlaying();
    }


    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEditEmojicon, emojicon);

    }

    public void addContacttoDevice(String number) {
		/*
		 * GlobalData.NumberAddtodevice = number; GlobalData.addContactTodevice
		 * = true; ArrayList<ContentValues> data = new
		 * ArrayList<ContentValues>();
		 */

        Intent intent = new Intent(getActivity(),
                sms19.listview.newproject.ContactAdd.class);
        intent.putExtra("froninapp", true);
        intent.putExtra("phone_number", number);
        startActivityForResult(intent, 301);

    }

    public void refreshChatAdapter() {

        GroupChatRoomFragment chatRoomFrgament = newInstance("","");
        if (chatRoomFrgament != null) {

            new Thread(new Runnable() {
                public void run() {
                    if (getActivity() != null) {

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                try {
                                    setChathistory();
                                } catch (Exception exception) {

                                }

                            }
                        });
                    }
                }
            }).start();

        }

        // if(chatRoomFrgament!=null){
		/*
		 * Handler mainHandler = new Handler(getActivity().getMainLooper());
		 * Runnable myRunnable = new Runnable() {
		 *
		 * @Override public void run() { try { setChathistory();
		 *
		 * } catch (Exception exp) { exp.printStackTrace(); } } };
		 * mainHandler.post(myRunnable);
		 */

		/*
		 * try{ setChathistory(); }catch(Exception exception){
		 *
		 * }
		 */

        // }

    }

    @SuppressLint("NewApi")
    public void show() {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("Time Picker");
        d.setContentView(R.layout.numeric_picker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d
                .findViewById(R.id.numberPicker1);
        final NumberPicker np2 = (NumberPicker) d
                .findViewById(R.id.numberPicker2);
        np.setMaxValue(23); // max value 100
        np.setMinValue(0); // min value 0
        np.setWrapSelectorWheel(false);

        np2.setMaxValue(59); // max value 100
        np2.setMinValue(1); // min value 0
        np2.setWrapSelectorWheel(false);

        // np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_box.setTag(String.valueOf(np.getValue()) + " : "
                        + String.valueOf(np2.getValue()));
                email_check_box.setTag(String.valueOf(np.getValue()) + " : "
                        + String.valueOf(np2.getValue()));
                if (np2.getValue() < 10) {
                    user_sms_creadit.setText("Send undelivered chats in "+String.valueOf(np.getValue())+":0"+String.valueOf(np2.getValue())+" Min");
                    /*check_box
                            .setText("Sms "
                                    + String.valueOf(np.getValue())
                                    + ":"
                                    + "0"
                                    + String.valueOf(np2.getValue()) + " Min "); // set
                    email_check_box
                            .setText("Mail "
                                    + String.valueOf(np.getValue())
                                    + ":"
                                    + "0"
                                    + String.valueOf(np2.getValue()) + " Min "); // set*/
                    // Utils.saveExpiryTimeValue(getActivity(),
                    // String.valueOf(np.getValue())+" : "+"0"+String.valueOf(np2.getValue()));
                } else {
                    user_sms_creadit.setText("Send undelivered chats in "+String.valueOf(np.getValue())+":"+String.valueOf(np2.getValue())+ " Min");
                  /*  check_box
                            .setText("Sms "
                                    + String.valueOf(np.getValue())
                                    + ":"
                                    + String.valueOf(np2.getValue()) + " Min "); // set
                    email_check_box
                            .setText("Mail "
                                    + String.valueOf(np.getValue())
                                    + ":"
                                    + String.valueOf(np2.getValue()) + " Min "); // set*/
                    // Utils.saveExpiryTimeValue(getActivity(),
                    // String.valueOf(np.getValue())+" : "+String.valueOf(np2.getValue()));
                }

                // the
                // value
                // to
                // textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();

    }

    @SuppressLint("NewApi")
    public void showEmailTimeSet() {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("Time Picker");
        d.setContentView(R.layout.numeric_picker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d
                .findViewById(R.id.numberPicker1);
        final NumberPicker np2 = (NumberPicker) d
                .findViewById(R.id.numberPicker2);
        np.setMaxValue(23); // max value 100
        np.setMinValue(0); // min value 0
        np.setWrapSelectorWheel(false);

        np2.setMaxValue(59); // max value 100
        np2.setMinValue(1); // min value 0
        np2.setWrapSelectorWheel(false);

        // np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_check_box.setTag(String.valueOf(np.getValue()) + " : "
                        + String.valueOf(np2.getValue()));

                /*if (np2.getValue() < 10) {
                    email_check_box
                            .setText("Mail "
                                    + String.valueOf(np.getValue())
                                    + ":"
                                    + "0"
                                    + String.valueOf(np2.getValue()) + " Min "); // set
                    // Utils.saveExpiryTimeValue(getActivity(),
                    // String.valueOf(np.getValue())+" : "+"0"+String.valueOf(np2.getValue()));
                } else {
                    email_check_box
                            .setText("Mail "
                                    + String.valueOf(np.getValue())
                                    + ":"
                                    + String.valueOf(np2.getValue()) + " Min "); // set
                    // Utils.saveExpiryTimeValue(getActivity(),
                    // String.valueOf(np.getValue())+" : "+String.valueOf(np2.getValue()));
                }*/

                // the
                // value
                // to
                // textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();

    }

    public void getnewMembers() {
        if (GlobalData.connection != null
                && GlobalData.connection.isConnected()
                && SingleChatRoomFrgament.mucChat != null) {

            if (mucChat.isJoined()) {

                ArrayList<Contactmodel> selectedmemberlist = new ArrayList<Contactmodel>();
                try {
                    final Collection<Affiliate> roomUsersOwner = SingleChatRoomFrgament.mucChat
                            .getOwners();

                    Collection<myAffiliate> roomUsers = Utils
                            .getAffiliatesByAdmin("admin", remote_jid);

                    if (roomUsers.size() > 0) {

                        Iterator<myAffiliate> ids = roomUsers.iterator();

                        while (ids.hasNext()) {
                            String id = ids.next().getJid();
                            if (!id.equals(InAppMessageActivity.myModel
                                    .getRemote_jid())) {

                                Contactmodel model = new Contactmodel();
                                model.setRemote_jid(id);
                                selectedmemberlist.add(model);
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

                        // GlobalData.dbHelper.editGroupinDB(remote_jid,selectedmemberlist);
                        GlobalData.dbHelper.editGroupMemberInDBNew(remote_jid,
                                selectedmemberlist);

                        final ArrayList<Contactmodel> filtermembers = GlobalData.dbHelper
                                .editContactforGroup(selectedmemberlist);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Utils.printLog("new member added in group adapter set...");

                                    setGroupmemberlist(false);
                                }
                            });
                        }

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
                    } else {

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Utils.printLog("new member added in group adapter set...");
                                if (roomUsersOwner != null
                                        && roomUsersOwner.size() == 1) {

                                    inAppMessageActivity.getmUserStatusTitle()
                                            .setText("you");
                                    setGroupmemberlist(false);
                                }
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setGroupmemberlist(boolean loadmember) {

        try {

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

            if (gmemberlist.size() > 0) {
                gmemberlist.clear();
            }
            ArrayList<Recentmodel> temp = GlobalData.dbHelper
                    .getGroupmemberListfromDB(remote_jid);

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
                    inAppMessageActivity.getmUserStatusTitle().setVisibility(
                            View.VISIBLE);
                    inAppMessageActivity.getmUserStatusTitle().setText(name);

                } else {
                    if (GlobalData.connection != null
                            && GlobalData.connection.isConnected()
                            && SingleChatRoomFrgament.mucChat != null
                            && SingleChatRoomFrgament.mucChat.isJoined()) {
                        inAppMessageActivity.getmUserStatusTitle().setText(
                                "you");
                    }
                }

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String getRemote_jid() {
        return remote_jid;
    }

    public void deleteGroupChat(final Connection conn, final String groupId1,
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
                                try {

                                    Utils.printLog("Delete group");
                                    MultiUserChat muc = new MultiUserChat(conn,
                                            groupId1);
                                    // if(!GlobalData.dbHelper.groupUserIsBlock(remote_jid)){
                                    if (!GlobalData.dbHelper
                                            .groupIsBlockNew(remote_jid)) {
                                        // muc.join(groupId);
                                        // muc.destroy("was group room", null);

                                        if (mucChat != null) {
                                            if (!mucChat.isJoined()) {
                                                mucChat = joinGroup(remote_jid);
                                            }

                                            if (gmemberlist != null) {
                                                if (gmemberlist.size() > 0) {
                                                    if (GlobalData.dbHelper
                                                            .checkGroupiscreatedbyme(groupId1)) {
                                                        mucChat.grantOwnership(gmemberlist
                                                                .get(0)
                                                                .getRemote_jid());
                                                        mucChat.revokeMembership(groupId1);
                                                        GlobalData.dbHelper
                                                                .updateGroupCreatedByMe(groupId1);

                                                    }
                                                }
                                            }

                                            try {
                                                mucChat.banUser(jid,
                                                        "delete group");
                                                mucChat.leave();

                                            } catch (Exception e) {
                                                // TODO Auto-generated catch
                                                // block
                                                try {
                                                    mucChat.leave();
                                                } catch (Exception e1) {
                                                    // TODO Auto-generated catch
                                                    // block
                                                    e1.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    GlobalData.dbHelper
                                            .deleteParticularUserHistory(groupId1);
                                    GlobalData.dbHelper
                                            .deleteGroupParticularrow(groupId1);
                                    GlobalData.dbHelper
                                            .deleteRecentParticularrow(groupId1);
                                    GlobalData.dbHelper
                                            .DeleteContactRemoteIdBase(groupId1);
                                    setGroupmemberlist(true);
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

                                    inAppMessageActivity.backPress();

                                } catch (XMPPException e) {
                                    // TODO Auto-generated catch block
                                    Utils.printLog("delete group failed");
                                    e.printStackTrace();
                                }

                                setGroupmemberlist(true);
                            }
                        }).show();

    }

    public void clientConnection() {

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            if (GlobalData.clientFtp != null) {
                if (GlobalData.clientFtp.isConnected()
                        && GlobalData.clientFtp.isAuthenticated()) {

                } else {
                    GlobalData.clientFtp = null;
                    GlobalData.clientFtp = new FTPClient();
                    GlobalData.clientFtp
                            .connect(GlobalData.FTP_HOST.trim(), 21);
                    GlobalData.clientFtp.login(GlobalData.FTP_USER.trim(),
                            GlobalData.FTP_PASS);
                    // GlobalData.clientFtp.setType(FTPClient.TYPE_BINARY);
                }
            } else {
                GlobalData.clientFtp = null;
                GlobalData.clientFtp = new FTPClient();
                GlobalData.clientFtp.connect(GlobalData.FTP_HOST.trim(), 21);
                GlobalData.clientFtp.login(GlobalData.FTP_USER.trim(),
                        GlobalData.FTP_PASS);
                // GlobalData.clientFtp.setType(FTPClient.TYPE_BINARY);

            }

            try {

                // String test=GlobalData.clientFtp.currentDirectory();
                GlobalData.clientFtp.changeDirectory("/" + userId);
            } catch (IllegalStateException e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            } catch (IOException e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            } catch (FTPIllegalReplyException e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            } catch (FTPException e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            }

        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Ftp server error",
                        Toast.LENGTH_LONG).show();
            }

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Ftp server error",
                        Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Ftp server error",
                        Toast.LENGTH_LONG).show();
            }
        } catch (FTPIllegalReplyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Ftp server error",
                        Toast.LENGTH_LONG).show();
            }
        } catch (FTPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Ftp server error",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    private void loadimageOfOffline(final String imagetoupload,
                                    String filetypeOffline) {

        try {

            String filetypeNew = "";
            String filepath = "";

            base64string = "";
            filetypeNew = "image";
            if (filetypeOffline.equals(GlobalData.Videofile)) {
                Bitmap bit = Utils.decodeFile(videothmbpath,
                        GlobalData.filetransferthmb,
                        GlobalData.filetransferthmb);
                filetypeNew = "video";
                base64string = Utils.convertTobase64string(bit);
            } else if (!filetypeOffline.equals(GlobalData.Audiofile)) {
                Bitmap bit = Utils.decodeFile(filepath,
                        GlobalData.filetransferthmb,
                        GlobalData.filetransferthmb);
                base64string = Utils.convertTobase64string(bit);

            }

            if (filetypeOffline.equals(GlobalData.Audiofile)) {
                filetypeNew = "audio";
            }

            imagetoupload2 = imagetoupload.substring(imagetoupload
                    .lastIndexOf("/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setimege = imagetoupload;
        Toast.makeText(getActivity(),
                "Wait For Sometime while media file is sharing completed",
                Toast.LENGTH_LONG).show();

        final String msgtime = Utils.currentTimeStap();
        String row_id = addfiletoUIandOfflineDB(imagetoupload, filetypeOffline,
                "", "0", false, true, msgtime);
        // AddSentMessagetochatandDBForOffline("",
        // "",imagetoupload,filetypeOffline,"0",row_id);

    }

    @SuppressWarnings("unused")
    private class FtpConnect extends AsyncTask<Object, Void, String> {

        Object object[] = null;
        File file = null;
        org.jivesoftware.smack.packet.Message message = null;
        FTPClient clientFtpNew = null;
        String imagetoupload = "";
        String timestemp = "";
        boolean b = false, ischecked = false;
        String liveuploaded = "";

        @Override
        protected String doInBackground(Object... object) {

            try {
                // object=s;
                file = (File) object[0];// /storage/sdcard0/Kitever/Media/Kitever
                // Images/Kitever_1472538430365.jpg
                clientFtpNew = (FTPClient) object[1];// it.sauronsoftware.ftp4j.FTPClient
                // [connected=true,
                // host=nowconnect.in,
                // port=21,
                // connector=it.sauronsoftware.ftp4j.connectors.DirectConnector@431050e8,
                // security=SECURITY_FTP,
                // authenticated=true,
                // username=ftp.nowconnect.in|userweb3,
                // password=************,
                // restSupported=true,
                // utf8supported=true,
                // mlsdSupported=false,
                // mode=modezSupportedfalse,
                // mode=modezEnabledfalse,
                // transfer
                // mode=passive,
                // transfer
                // type=TYPE_AUTO,
                // textualExtensionRecognizer=it.sauronsoftware.ftp4j.extrecognizers.DefaultTextualExtensionRecognizer@42203e30,
                // listParsers=it.sauronsoftware.ftp4j.listparsers.UnixListParser@42763ba8,
                // it.sauronsoftware.ftp4j.listparsers.DOSListParser@42763ae8,
                // it.sauronsoftware.ftp4j.listparsers.EPLFListParser@42763a28,
                // it.sauronsoftware.ftp4j.listparsers.NetWareListParser@42763970,
                // it.sauronsoftware.ftp4j.listparsers.MLSDListParser@427638b0,
                // autoNoopTimeout=0]
                message = (org.jivesoftware.smack.packet.Message) object[2];// org.jivesoftware.smack.packet.Message@c3a925ea
                imagetoupload = (String) object[3];// /storage/sdcard0/Kitever/Media/Kitever
                // Images/Kitever_1472538430365.jpg
                timestemp = (String) object[4];// null
                liveuploaded = (String) object[5];// http://nowconnect.in/121/Kitever_1472538430365.jpg
                ischecked = (Boolean) object[6];// false

                try {
                    if (GlobalData.clientFtp != null) {
                        GlobalData.clientFtp.disconnect(true);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    GlobalData.clientFtp = null;
                }

                if (GlobalData.clientFtp != null
                        && GlobalData.clientFtp.isConnected()
                        && GlobalData.clientFtp.isAuthenticated()) {
                    b = true;
                } else {
                    clientConnection();
                    clientFtpNew = GlobalData.clientFtp;
                    b = true;
                }

            } catch (Exception e) {
                GlobalData.dbHelper.updateFileIsUploadedForRetry(
                        message.getPacketID(), 1);
                b = false;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if (b) {
                if (GlobalData.clientFtp != null) {

                    ExecutorService executor = Executors.newFixedThreadPool(6);
                    Object[] iObj = new Object[7];
                    iObj[0] = file;
                    iObj[1] = GlobalData.clientFtp;
                    iObj[2] = message;
                    iObj[3] = imagetoupload;
                    iObj[4] = timestemp;
                    iObj[5] = liveuploaded;
                    iObj[6] = ischecked;

                    Runnable worker = new UploadThread(iObj);
                    executor.execute(worker);

					/*
					 * ExecutorService executor =
					 * Executors.newFixedThreadPool(5); Object[] iObj=new
					 * Object[4]; iObj[0]=file; iObj[1]=GlobalData.clientFtp;
					 * iObj[2]=messageNew; iObj[3]=imagetoupload; Runnable
					 * worker = new UploadThread(iObj);
					 * executor.execute(worker);
					 */

                } else {

                    GlobalData.dbHelper.updateFileIsUploadedForRetry(
                            message.getPacketID(), 1);

                }

            } else {

                GlobalData.dbHelper.updateFileIsUploadedForRetry(
                        message.getPacketID(), 1);

            }

        }

    }

    public String calculateFileSize(String filepath) {
        // String filepathstr=filepath.toString();
        File file = new File(filepath);
        long fileSizeInBytes = file.length();// 51994
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        // long fileSizeInMB = fileSizeInKB / 1024;

        // String calString=Long.toString(fileSizeInMB);
        String calString = Long.toString(fileSizeInKB);
        calString = calString + "KB";
        return calString;
    }

    private synchronized void loadimage(final String imagetoupload,
                                        String filetypeNew, final boolean isRetry, final Chatmodel chatmodel) {// imageoupload=
        // /storage/sdcard0/Kitever/Media/Kitever
        // Images/Kitever_1472479467362.jpg
        // String dataSize="";
        // dataSize=calculateFileSize(imagetoupload);
        boolean ischecked = false;
        if (check_box.isChecked()) {
            ischecked = true;
        }

        String filetypeGetted = "";

        try {

            String filepath = "";

            filepath = imagetoupload;
            filetypeGetted = filetypeNew;
            base64string = "";
            // filetypeNew="image";
            if (filetypeGetted.equals(GlobalData.Videofile)) {
                Bitmap bit = Utils.decodeFile(videothmbpath,
                        GlobalData.filetransferthmb,
                        GlobalData.filetransferthmb);
                // filetypeNew="video";
                base64string = Utils.convertTobase64string(bit);
            } else if (!filetypeGetted.equals(GlobalData.Audiofile)) {
                Bitmap bit = Utils.decodeFile(filepath,
                        GlobalData.filetransferthmb,
                        GlobalData.filetransferthmb);
                base64string = Utils.convertTobase64string(bit);
                // filetypeNew="image";

            }

            if (filetypeGetted.equals(GlobalData.Audiofile)) {
                // filetypeNew="audio";
            }

            imagetoupload2 = imagetoupload.substring(imagetoupload
                    .lastIndexOf("/"));// /Kitever_1472479467362.jpg
            Toast.makeText(getActivity(),
                    "Wait For Sometime while media file is sharing",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setimege = imagetoupload;// /storage/sdcard0/Kitever/Media/Kitever
        // Images/Kitever_1472479467362.jpg
        String setimegenew = imagetoupload2.replace("/", "");// Kitever_1472479467362.jpg
        // setimegenew=dataSize+setimegenew;
        Toast.makeText(getActivity(),
                "Wait For Sometime while media file is sharing",
                Toast.LENGTH_LONG).show();

        // GlobalData.FTP_URL="";
        if (!(GlobalData.FTP_URL.equalsIgnoreCase("")
                || GlobalData.FTP_USER.equalsIgnoreCase("")
                || GlobalData.FTP_PASS.equalsIgnoreCase("") || GlobalData.FTP_HOST
                .equalsIgnoreCase(""))) {
            // if(false){
            sendurlpath = "http://" + GlobalData.FTP_HOST + "/" + userId + "/"
                    + setimegenew;// http://nowconnect.in/10016/Kitever_1472479467362.jpg

            String filepath = "";
            filepath = setimege;
            Log.w("LOAD_IMAGE_URL", "LOAD_IMAGE_URL" + sendurlpath);

            String msg = "";
            if (filetypeGetted.equals(GlobalData.Audiofile)) {
                msg = sendfilefixString + sendurlpath + "__" + filetypeGetted
                        + "__" + base64string;

            } else if (filetypeGetted.equals(GlobalData.Locationfile)) {
                msg = sendfilefixString + sendurlpath + "__" + filetypeGetted
                        + "__" + base64string + "__" + latlng.trim();

            } else {
                msg = sendfilefixString + sendurlpath + "__" + filetypeGetted
                        + "__" + base64string;
            }

            if (filetypeGetted.equals(GlobalData.Imagefile)) {
                Bitmap bit = Utils.decodeFile(imagetoupload,
                        GlobalData.profilepicthmb, GlobalData.profilepicthmb);
                base64string = Utils.convertTobase64string(bit);
            }

            try {
                org.jivesoftware.smack.packet.Message messageNew = null;
                if (isRetry) {
                    messageNew = onRetryFileUpload(msg, chatmodel);
                } else {
                    messageNew = sendMessageBeforeUpload(msg, remote_jid,
                            filepath, filetypeGetted);
                }

                try {

                    // check_box.setChecked(false);

                    final File f = new File(imagetoupload);

                    FtpConnect ftpConnect = new FtpConnect();

                    Object[] iObj = new Object[7];
                    iObj[0] = f;
                    iObj[1] = GlobalData.clientFtp;
                    iObj[2] = messageNew;
                    iObj[3] = filepath;
                    iObj[5] = sendurlpath;
                    iObj[6] = ischecked;

                    if (chatmodel != null) {
                        iObj[4] = chatmodel.getMsgtime();
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        ftpConnect.executeOnExecutor(
                                AsyncTask.THREAD_POOL_EXECUTOR, iObj);
                    } else {
                        ftpConnect.execute(iObj);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        GlobalData.clientFtp.disconnect(true);
                    } catch (Exception e2) {

                    }
                }

            } catch (XMPPException e1) {

                e1.printStackTrace();
            }

        } else {

            if (Utils.isDeviceOnline(getActivity())) {
                GetFtpDetailsAsyncTask sync = new GetFtpDetailsAsyncTask(null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    sync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    sync.execute();
                }

            } else {
                Toast.makeText(getActivity(), "Check your network connection",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }

    public class UploadThread implements Runnable {

        Object object[] = null;
        File file = null;
        org.jivesoftware.smack.packet.Message message = null;
        FTPClient clientFtpNew = null;
        String imagetoupload = "";
        String timestamp = "";
        String liveuploaded = "";
        boolean ischecked = false;

        public UploadThread(Object[] s) {
            object = s;
            file = (File) object[0];
            clientFtpNew = (FTPClient) object[1];
            message = (org.jivesoftware.smack.packet.Message) object[2];
            imagetoupload = (String) object[3];
            timestamp = (String) object[4];
            liveuploaded = (String) object[5];
            ischecked = (Boolean) object[6];

        }

        @Override
        public void run() {

            MyTransferListener listener = new MyTransferListener(message,
                    timestamp, liveuploaded, ischecked);
            // File f = new File(imagetoupload);

            try {

                clientFtpNew.upload(file, listener);

            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                GlobalData.dbHelper.updateFileIsUploadedForRetry(
                        message.getPacketID(), 1);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                GlobalData.dbHelper.updateFileIsUploadedForRetry(
                        message.getPacketID(), 1);
                try {
                    GlobalData.clientFtp.disconnect(true);
                } catch (IllegalStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPIllegalReplyException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                GlobalData.dbHelper.updateFileIsUploadedForRetry(
                        message.getPacketID(), 1);
                try {
                    GlobalData.clientFtp.disconnect(true);
                } catch (IllegalStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPIllegalReplyException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            } catch (FTPIllegalReplyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                GlobalData.dbHelper.updateFileIsUploadedForRetry(
                        message.getPacketID(), 1);
                try {
                    GlobalData.clientFtp.disconnect(true);
                } catch (IllegalStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPIllegalReplyException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            } catch (FTPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                GlobalData.dbHelper.updateFileIsUploadedForRetry(
                        message.getPacketID(), 1);
                try {
                    GlobalData.clientFtp.disconnect(true);
                } catch (IllegalStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPIllegalReplyException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            } catch (FTPDataTransferException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                GlobalData.dbHelper.updateFileIsUploadedForRetry(
                        message.getPacketID(), 1);
                try {
                    GlobalData.clientFtp.disconnect(true);
                } catch (IllegalStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPIllegalReplyException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            } catch (FTPAbortedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                GlobalData.dbHelper.updateFileIsUploadedForRetry(
                        message.getPacketID(), 1);
                try {
                    GlobalData.clientFtp.disconnect(true);
                } catch (IllegalStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPIllegalReplyException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (FTPException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }

        }

        @Override
        public String toString() {
            return "";
        }
    }

    public class MyTransferListener implements FTPDataTransferListener {
        org.jivesoftware.smack.packet.Message message;
        String timeupdate = "";
        String liveuploadedurl = "";
        boolean ischecked = false;

        public MyTransferListener(
                org.jivesoftware.smack.packet.Message message, String time,
                String liveuploaded, boolean ischecked) {
            this.message = message;
            timeupdate = time;
            this.liveuploadedurl = liveuploaded;
            this.ischecked = ischecked;
        }

        public void started() {
        }

        public void transferred(int length) {

            Log.w("transferred", "transferred=======" + String.valueOf(length));

        }

        public void completed() {

            try {

                if (isgroup == 1) {

                    mucChat.sendMessage(message);
                    GlobalData.dbHelper.updateFileIsUploadedForRetry(
                            message.getPacketID(), 2);
                    // GlobalData.dbHelper.updateRetryUpdateToTime(message.getPacketID(),
                    // 2);

                    updateAdapterWithIsUpload(message, timeupdate);

                }

                // addfiletoUIandDB(filepath,fileType, msgPacketId, "1");

            } catch (XMPPException e) {
                Utils.printLog("Failed send  file==");
                e.printStackTrace();
            }
        }

        public void aborted() {

        }

        public void failed() {
            GlobalData.dbHelper.updateFileIsUploadedForRetry(
                    message.getPacketID(), 1);

        }
    }

    public void updateAdapterWithIsUpload(
            final org.jivesoftware.smack.packet.Message message,
            final String time) {
        String packetId = message.getPacketID();
        if (packetId != null) {
            GlobalData.dbHelper.updateFileIsUploaded(packetId, "S");
            // GlobalData.dbHelper.updateFileIsSent(packetId, "1");

            if (newInstance("","") != null) {

                if (chatAdapter != null) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                String packetId = message.getPacketID();

                                for (int i = 0; i < chathistorylist.size(); i++) {
                                    

                                        if (packetId
                                                .equalsIgnoreCase(chathistorylist
                                                        .get(i)
                                                        .getMsg_packetid())) {
                                            chathistorylist.get(i).setStatus(
                                                    "S");
                                            chathistorylist.get(i)
                                                    .setIsretry(2);
                                            break;
                                        } else {
                                            if (time != null) {
                                                if (time.equalsIgnoreCase(chathistorylist
                                                        .get(i).getMsgtime())) {
                                                    chathistorylist.get(i)
                                                            .setStatus("S");
                                                    chathistorylist.get(i)
                                                            .setIsretry(2);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                
                                chatAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        }
    }



    public Groupmodel getGroupModel() {
        return groupModel;
    }

    public void blockContact(final String groupJId) {

        // Log.i("XMPP Chat Client", "User left chat room ");

        new AlertDialog.Builder(getActivity())
                .setTitle("Block Contact")
                .setMessage("Are you sure you want to block this contact?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                try {

                                    // GlobalData.dbHelper.singleContactBlockfromDB(groupJId);
                                    isuserblock = 1;

                                    // mBlockBtn.setText("Unblock User");
                                    // Utils.blockUnblock(remote_jid,groupJId,GlobalData.connection);

                                    // homeActivity.invalidateOptionMenuItem();

                                    GlobalData.privacyManager = PrivacyListManager
                                            .getInstanceFor(GlobalData.connection);
                                    if (GlobalData.privacyManager != null) {
                                        inAppMessageActivity
                                                .blockUserWithPrivacy(groupJId);
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

    public void unblockContact(final String groupJId) {

        Log.i("XMPP Chat Client", "User left chat room ");

        new AlertDialog.Builder(getActivity())
                .setTitle("Block Contact")
                .setMessage("Unblock " + frndname + "  to send a message")
                .setNegativeButton(/* android.R.string.no */"Cancel",
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

                                    // GlobalData.dbHelper.singleContactUnBlockfromDB(groupJId);

                                    isuserblock = 0;
                                    try {
                                        GlobalData.privacyManager = PrivacyListManager
                                                .getInstanceFor(GlobalData.connection);
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    if (GlobalData.privacyManager != null) {
                                        inAppMessageActivity
                                                .unBlockUserWithPrivacy(groupJId);
                                    }

                                    // mBlockBtn.setText("Block User");
                                    // Utils.blockUnblock(remote_jid,groupJId,GlobalData.connection);

                                    // homeActivity.invalidateOptionMenuItem();

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    Utils.printLog("Failed leave group..");
                                    e.printStackTrace();
                                }

                                // setGroupmemberlist(true);
                            }
                        }).show();

    }

    public int getIsuserblock() {
        return isuserblock;
    }

    public void setIsuserblock(int isuserblock) {
        this.isuserblock = isuserblock;
    }

    public Button getmBlockBtn() {
        return mBlockBtn;
    }

    public void exitGroup(String groupJId, String jid) {
        Log.i("XMPP Chat Client", "User left chat room ");

        if (mucChat != null && mucChat.isJoined()) {
            try {

                GlobalData.dbHelper.singleGroupContactBlockfromDB(jid);
                GlobalData.dbHelper.groupBlocknewfromDB(groupJId);

				/*
				 * GlobalData.dbHelper.deleteParticularUserHistory(groupJId) ;
				 * GlobalData.dbHelper.deleteRecentParticularrow(groupJId);
				 * GlobalData.dbHelper.deleteGroupParticularrow(groupJId);
				 */

                try {
                    // mucChat.grantMembership(jid);

                    if (GlobalData.dbHelper.checkGroupiscreatedbyme(groupJId)) {

                        if (gmemberlist != null) {
                            if (gmemberlist.size() > 0) {

                                mucChat.grantOwnership(gmemberlist.get(0)
                                        .getRemote_jid());// assign ownership to
                                // other user
                                mucChat.revokeMembership(groupJId);// exit
                                // itself

                                GlobalData.dbHelper
                                        .updateGroupCreatedByMe(groupJId);
                                // GlobalData.dbHelper.updateGroupCreatedByMe(groupJId);
                            }
                        }

                    }

                    mucChat.banUser(jid, "");
                    mucChat.leave();

                    inAppMessageActivity.backPress();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    try {
                        mucChat.leave();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    inAppMessageActivity.backPress();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static int getIsgroup() {
        return isgroup;
    }

    public void deleteChat(String remotejid) {
        if (Utils.isDeviceOnline(getActivity())) {

            GlobalData.dbHelper.deleteParticularUserHistory(remotejid);
            GlobalData.dbHelper.deleteRecentParticularrow(remotejid);
            GlobalData.dbHelper.deleteGroupParticularrow(remotejid);
            GlobalData.dbHelper.updateContactmsgData(remotejid, "", "");
            inAppMessageActivity.broadCastDelete(remotejid, 2);
            inAppMessageActivity.backPress();
        } else {
            Toast.makeText(getActivity(), "Check your network connection",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void searchFilter() {
        String string = mSearchEdt.getText().toString().trim();
        ArrayList<Chatmodel> arrayListFiltered = new ArrayList<Chatmodel>();

        if (!string.equalsIgnoreCase("")) {
            for (int i = 0; i < chathistorylist.size(); i++) {

                if (chathistorylist.get(i).getChatmessage().toLowerCase()
                        .contains((string.toString()).toLowerCase())) {
                    arrayListFiltered.add(chathistorylist.get(i));
                }

            }
            chatAdapter.setChatListArrayList(arrayListFiltered);
            chatAdapter.notifyDataSetChanged();

        } else {
            chatAdapter.setChatListArrayList(chathistorylist);
            chatAdapter.notifyDataSetChanged();
        }

    }

    public LinearLayout getSearch_header() {
        return search_header;
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

                                    exitGroup(groupJId, jid);

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    Utils.printLog("Failed leave group..");
                                    e.printStackTrace();
                                }

                                // setGroupmemberlist(true);
                            }
                        }).show();

    }

    // @Override
    // public void onStop() {
    // // TODO Auto-generated method stub
    // super.onPause();
    //
    // // ON_SINGLE_CHAT_PAGE=false;
    //
    // }

    public void getLoadMoreMessageHistoryFromServer(String fromTime,
                                                    boolean isRefreshAfterDownload) {
        // progress.setVisibility(View.VISIBLE);

        Object[] time = new Object[2];
        time[0] = fromTime;
        time[1] = isRefreshAfterDownload;
        loading_text.setText("Loading message history...");
        if (isgroup == 1) {
            GroupHistoryAsyncTask getSingleUserMessageHistory = new GroupHistoryAsyncTask(
                    inAppMessageActivity, InAppMessageActivity.chatPrefs,
                    userId, InAppMessageActivity.myModel.getRemote_jid(),
                    remote_jid, "", progress, loading_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getSingleUserMessageHistory.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, time);
            } else {
                getSingleUserMessageHistory.execute(time);
            }
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public boolean onLongClick(View v) {

        return false;
    }

    private void setOfflineMsgFromService() {
        // TODO Auto-generated method stub
        offlineChatHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (getActivity() != null) {
                    String time = msg.getData().getString("time");
                    String msg_packetid = msg.getData().getString("packetid");

                    if (isgroup == 0) {

                        setChathistory();
                    }
                }
            }
        };
    }

    public void OnStartGetChatHistory(boolean bFlag) {
        android.os.Message msg = new android.os.Message();// comment m
        Bundle b = new Bundle();
        b.putBoolean("bFlag", bFlag);
        msg.setData(b);
        handlerRefreshAdapter.sendMessage(msg);
    }

    public void checkPresence() {

        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("onCheckPresenceHandler", "1");
        msg.setData(b);
        SingleChatRoomFrgament.onCheckPresenceHandler.sendMessage(msg);// comment
        // m

    }

    private void inItCheckPresenceHandler() {
        // TODO Auto-generated method stub
        onCheckPresenceHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                if (!GlobalData.dbHelper.isContactBlock(remote_jid)) {
                    if (GlobalData.connection != null
                            && GlobalData.connection.isConnected()
                            && GlobalData.connection.isAuthenticated()) {
                        if (isgroup == 0) {

                            if (GlobalData.roster == null) {
                                GlobalData.roster = GlobalData.connection
                                        .getRoster();
                            }

                            Presence presence = GlobalData.roster
                                    .getPresence(remote_jid);

                            if (presence.getType().equals(
                                    Presence.Type.available)) {
                                status = "1";
                            } else {
                                status = "0";
                            }
                            GlobalData.dbHelper.updateContactdata(remote_jid,
                                    status, null, "", 0, true);
                        }
                    }
                }
            }
        };
    }

    public void groupJoinHandler() {
        groupJoinAndLoadMembersHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                if (getActivity() != null) {

                    if (GlobalData.connection != null
                            && GlobalData.connection.isConnected()) {

                        if (isgroup == 1) {

                            if (!GlobalData.dbHelper
                                    .groupIsBlockNew(remote_jid)) {

                                if (!ContactUtil.mucChatIs(remote_jid)) {
                                    mucChat = joinGroup(remote_jid);
                                    GlobalData.globalMucChat.put(remote_jid,
                                            mucChat);

                                } else {
                                    mucChat = GlobalData.globalMucChat
                                            .get(remote_jid);
                                    if (mucChat == null) {
                                        mucChat = joinGroup(remote_jid);
                                    } else if (!mucChat.isJoined()) {
                                        mucChat = joinGroup(remote_jid);
                                    }
                                }

                                try {

                                    setGroupmemberlist(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                try {

                                    setGroupmemberlist(false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        }  else {
                            chatManager = GlobalData.connection
                                    .getChatManager();
                        }

                    } else {
                        if (isgroup == 1 || isgroup == 2) {
                            setGroupmemberlist(false);
                        }
                    }

                }

            }
        };
    }

    public void calljoinhaldler() {

        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("groupJoinAndLoadMembersHandler", "1");
        msg.setData(b);
        SingleChatRoomFrgament.groupJoinAndLoadMembersHandler.sendMessage(msg);// comment
        // m

    }

   
    private void creditDialog(String msg) {

        new AlertDialog.Builder(getActivity()).setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        check_box.setChecked(false);
                        email_check_box.setChecked(false);
                    }

                })

                .show();
    }

    // private boolean isFtpDirExisted=false;
    public void ftpMakeDirectory(String args) {

        String folder = /* GlobalData.FTP_URL + */"/" + userId;
        String server = GlobalData.FTP_HOST;
        int port = 21;
        String user = GlobalData.FTP_USER;
        String pass = GlobalData.FTP_PASS;

        org.apache.commons.net.ftp.FTPClient ftpClient = new org.apache.commons.net.ftp.FTPClient();
        try {
            try {
                ftpClient.connect(server, port);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                // System.out.println("Operation failed. Server reply code: "
                // + replyCode);
                return;
            }
            boolean success = ftpClient.login(user, pass);
            // showServerReply(ftpClient);
            if (!success) {
                // System.out.println("Could not login to the server");
                return;
            }
            // Creates a directory
            // String dirToCreate = folder;
            // FTPFile[] file=null;
            // String[] name=null;
            // // file=ftpClient.listDirectories(dirToCreate);
            // // file=ftpClient.listFiles(dirToCreate);
            // name=ftpClient.listNames();
            // if(name!=null && name.length>0){
            // for(int k=0;k<name.length;k++){
            // if(name[k].equalsIgnoreCase(userId)){
            // isFtpDirExisted=true;
            // break;
            // }
            // }
            // }
            // if(isFtpDirExisted){
            // ArrayList<String> validFileNames = new ArrayList<String>();
            // try {
            // FTPFile[] ftpFiles = ftpClient.listFiles(userId);
            // int length = ftpFiles.length;
            //
            // for (int i = 0; i < length; i++) {
            // String nameFile = ftpFiles[i].getName();
            // boolean isFile = ftpFiles[i].isFile();
            //
            // if (isFile) {
            // validFileNames.add(nameFile);
            // // Log.i(TAG, "File : " + name);
            // }
            // }
            // }catch (Exception e) {
            // // TODO: handle exception
            // }
            // }
            // dirToCreate="http://nowconnect.in"+dirToCreate;
            // success=makeDirectories(ftpClient, dirToCreate);
            success = ftpClient.makeDirectory(folder);
            // showServerReply(ftpClient);
            if (success) {
                Utils.userIsCreateFtpFolder(getActivity(), true);
                // System.out.println("Successfully created directory: "
                // + dirToCreate);
            } else {

                // System.out
                // .println("Failed to create directory. See server's reply.");
            }
            // logs out
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            // System.out.println("Oops! Something wrong happened");
            // ex.printStackTrace();
        }
    }

    public void updateBlock() {

        if (isgroup == 0) {
            if (inAppMessageActivity.isUserBlock(remote_jid)) {
                isuserblock = 1;
            } else {
                isuserblock = 0;
            }
        }

    }



    private String timeInMinutes() {

        String timeStr = "0";

        try {
            String time = check_box.getTag().toString()
                    .replace(" ", "");
            String timeArr[] = time.split(":");
            final int hour = Integer.parseInt(timeArr[0]) * 60;
            final int min = Integer.parseInt(timeArr[1]);
            final int timeInt = hour + min;
            timeStr = Integer.toString(timeInt);

            return timeStr;

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return timeStr;

    }

    private String timeInMinutesEmail() {

        String timeStr = "0";

        try {
            String time = email_check_box.getTag().toString()
                    .replace(" ", "");
            String timeArr[] = time.split(":");
            final int hour = Integer.parseInt(timeArr[0]) * 60;
            final int min = Integer.parseInt(timeArr[1]);
            final int timeInt = hour + min;
            timeStr = Integer.toString(timeInt);

            return timeStr;

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return timeStr;

    }

    private class FetchFtpDataList extends AsyncTask<String, String, String> {

        String userId;

        FetchFtpDataList(String userId) {
            this.userId = userId;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Rest rest = Rest.getInstance();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("Page",
                    "FetchFTPDataList"));
            nameValuePairs.add(new BasicNameValuePair("userid", userId));
            String stringUrl = Apiurls.KIT19_BASE_URL.replace("?Page=", "");
            stringUrl = stringUrl.replace(" ", "");
            String response = rest.post(stringUrl, nameValuePairs);
            return response;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            ArrayList<FtpDataListModel> list = new ArrayList<FtpDataListModel>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject != null) {
                    if (jsonObject.has("FileShared")) {
                        JSONArray array = jsonObject.getJSONArray("FileShared");
                        if (array != null && array.length() > 0) {
                            for (int k = 0; k < array.length(); k++) {
                                JSONObject object = array.getJSONObject(k);
                                String name = "";
                                String type = "";
                                String messageBody = "";
                                if (object.has("name")) {
                                    name = object.getString("name");
                                }
                                if (object.has("FileType")) {
                                    type = object.getString("FileType");
                                }
                                if (object.has("MessageBody")) {
                                    messageBody = object
                                            .getString("MessageBody");
                                }
                                list.add(new FtpDataListModel(name, type,
                                        messageBody));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            openFtpStorageDataList(list);
        }
    }

    private class ShareFtpData extends AsyncTask<String, String, String> {
        private String userId;
        private String fileName;
        private String mobile;

        ShareFtpData(String userId, String fileName, String mobile) {
            this.mobile = mobile;
            this.userId = userId;
            this.fileName = fileName;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Rest rest = Rest.getInstance();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs
                    .add(new BasicNameValuePair("Page", "MediaShareData"));
            nameValuePairs.add(new BasicNameValuePair("UserId", userId));
            nameValuePairs.add(new BasicNameValuePair("FilePath", fileName));
            nameValuePairs.add(new BasicNameValuePair("Mobile", mobile));
            String stringUrl = Apiurls.KIT19_BASE_URL.replace("?Page=", "");
            stringUrl = stringUrl.replace(" ", "");
            String response = rest.post(stringUrl, nameValuePairs);
            return response;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Toast.makeText(getActivity(), "Data has been shared",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void selectEmailSmsTemplate() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_template_popup);
        TextView emailTemplate = (TextView) dialog
                .findViewById(R.id.email_template);
        TextView smsTeplate = (TextView) dialog.findViewById(R.id.sms_template);
        emailTemplate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        EmailTemplateActivity.class);
                startActivityForResult(intent,
                        SELECT_EMAIL_TEMPLATE_REQUEST_CODE);
                dialog.dismiss();
            }
        });
        smsTeplate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        TemplateActivity.class);
                intent.putExtra("taketemplate", "send");
                startActivityForResult(intent, SELECT_TEMPLATE_REQUEST_CODE);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void removeEmoji() {
        try {
            if (emojicons != null) {
                makeEmojiVisible = true;
                emojicons.setVisibility(View.GONE);
                emojicons.removeAllViews();


            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
