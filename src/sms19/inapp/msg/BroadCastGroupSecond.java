package sms19.inapp.msg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.Intents.Insert;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import csms19.inapp.msg.customgallery.BucketHomeFragmentActivity;
import sms19.inapp.msg.adapter.BroadCastSecondAdapter;
import sms19.inapp.msg.asynctask.CheckCreditAsyncTask;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Slacktags;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.imoze.EmojiconsFragment;
import sms19.inapp.msg.imozemodel.Emojicon;
import sms19.inapp.msg.model.Chatmodel;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.Recentmodel;
import sms19.inapp.msg.model.Uploadfilemodel;
import sms19.inapp.msg.rest.Rest;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;
import sms19.listview.newproject.Home.ChatFragmentListener;

public class BroadCastGroupSecond extends Fragment implements OnClickListener, OnCompletionListener, sms19.inapp.msg.imoze.EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener,
        sms19.listview.newproject.Home.ChatFragmentListener {


    private static BroadCastGroupSecond chatFragment;
    private int fragment_count = 0;
    private static String title;

    public static ListView mListView;
    private InAppMessageActivity homeActivity = null;
    private ChatAndContactParentFragment andContactParentFragment = null;
    private sms19.inapp.msg.imoze.EmojiconEditText mMessageEdtext;
    private ImageView mSendBtn;
    private ImageView mEmojiBtn;
    private ImageView mCameraBtn;
    private ImageView time_select;
    private TextView user_sms_creadit;
    private CheckBox check_box;

    private int pHour;
    private int pMinute;
    static final int TIME_DIALOG_ID = 0;
    private static HashMap<String, Recentmodel> gmemberhasmap;
    private Handler timeHandler;
    ArrayList<Recentmodel> gmemberlist;


    /***************
     * xmpp code
     *************/

    public static Contactmodel myModel;
    public static byte[] frndpic = null;
    public static String frndname = "";
    public static String remote_jid = "", customStatus = "";
    private String status = "", phonenumber = "";
    public ArrayList<Uploadfilemodel> fileList;
    public static ArrayList<String> broadcastusersid;
    public static String previousrowid = "0";
    public static boolean fromattch = false;
    public static boolean loadmoremsgshow = true;
    private ChatManager chatManager;
    public static int isgroup = 0;
    public static int isstranger = 0;
    public static MultiUserChat mucChat = null;
    public static Handler ChatupdateHandler;
    public static String sendfilefixString = "file__";
    private PopupWindow popupWindow;
    public static int deviceWidth = 0;
    public static int popupwidth = 0;
    private ViewGroup container;
    public static Handler RecordAudioHandler;
    private long rowid = 0;
    private String fileType = "";
    private String fileUrl = "";
    public static String recordfilePath = "";
    public static String locationfilePath = "";
    private String latlng = "";
    private String videoPath = "";
    private String videothmbpath = "";
    public static int RESULT_LOAD_IMAGE = 1;
    public static int VIDEO_REQUEST = 1800;
    public static int CAMERA_REQUEST = 1888;
    public static Uri mCapturedImageURI = null;
    private RelativeLayout emojicons;
    private sms19.inapp.msg.imoze.EmojiconEditText mEditEmojicon;
    private LinearLayout chat_addfilebuttonlay;
    private OnClickListener galleryClick, photoClick, videoclick, audioClick, templateClick;
    private View viewGlobal;
    public static ChatFragmentListener obj;
    private TextView fc_addpreviousmsg;
    private ProgressBar fc_loadmsgprogress;
    private RelativeLayout fc_loadmsglay;
    private TextView fc_addcontact, fc_notnow;
    private LinearLayout addtodevicelay;
    private LinearLayout block_layout;
    private int LAST_HIDE_MENU = 0;
    HashMap<String, Contactmodel> postContactmap = new HashMap<String, Contactmodel>();
    private String mLastTitle = "";
    private CheckCreditAsyncTask asyncTask = null;


    private BroadCastSecondAdapter chatAdapter = null;
    private ArrayList<Contactmodel> contactmodelsArrayList = new ArrayList<Contactmodel>();

    private ArrayList<Contactmodel> contactlistNew = new ArrayList<Contactmodel>();
    private ArrayList<Contactmodel> appUserListNew = new ArrayList<Contactmodel>();
    private ArrayList<Contactmodel> nonAppUserNew = new ArrayList<Contactmodel>();

    public TextView getUser_sms_creadit() {
        return user_sms_creadit;
    }


    public static BroadCastGroupSecond newInstance(String titleName) {
        title = titleName;
        return chatFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        previousrowid = "0";
        fromattch = true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        homeActivity = (InAppMessageActivity) getActivity();

        homeActivity.groupActionBarControlIsVisual();
        homeActivity.getActionbar_title().setVisibility(View.VISIBLE);
        mLastTitle = homeActivity.getActionbar_title().getText().toString();
        homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
        homeActivity.getActionbar_title().setText("Broadcast Group 2");


        LAST_HIDE_MENU = ConstantFields.HIDE_MENU;
        ConstantFields.HIDE_MENU = 3;
        homeActivity.invalidateOptionMenuItem();

        contactlistNew = ContactFragment.contactlist;
        appUserListNew = ContactFragment.appUserList;
        nonAppUserNew = ContactFragment.nonAppUser;
        if (InAppMessageActivity.myModel != null) {
            myModel = InAppMessageActivity.myModel;
            remote_jid = InAppMessageActivity.myModel.getRemote_jid().trim();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        chatFragment = this;
        obj = this;

        if (ContactFragment.BROAD_CAST_FLAG == 1) { // Both
            //contactmodelsArrayList=ContactFragment.contactlist;
            contactmodelsArrayList.addAll(ContactFragment.contactlist);


        } else if (ContactFragment.BROAD_CAST_FLAG == 2) { // App users
            //contactmodelsArrayList=ContactFragment.appUserList;
            contactmodelsArrayList.addAll(ContactFragment.appUserList);


            homeActivity.getActionbar_title().setText("Broadcast Group 2");
        } else if (ContactFragment.BROAD_CAST_FLAG == 3) { // Non app users
            //contactmodelsArrayList=ContactFragment.nonAppUser;
            contactmodelsArrayList.addAll(ContactFragment.nonAppUser);
            homeActivity.getActionbar_title().setText("Broadcast Group 1");
        }


        myModel = GlobalData.dbHelper.getUserDatafromDB();


        View view = getActivity().getLayoutInflater().inflate(R.layout.new_broadcast_view, container, false);
        viewGlobal = view;


        if (Utils.isDeviceOnline(getActivity())) {
            new Thread(new Runnable() {
                public void run() {
                    if (GlobalData.connection != null
                            && GlobalData.connection.isConnected()) {


                        chatManager = GlobalData.connection.getChatManager();


                    } else {
                    }
                }
            }).start();

        } else {
            Toast.makeText(getActivity(), "Check your network connection",
                    Toast.LENGTH_SHORT).show();
        }


        setpopUpclicks();
        initiateView(view);


        chatAdapter = new BroadCastSecondAdapter(getActivity(), contactmodelsArrayList);
        chatAdapter.setClickListener(this);
        mListView.setAdapter(chatAdapter);


        setChatupdateHandler();
        setAudiorecordHandler();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);

        deviceWidth = displaymetrics.widthPixels;
        popupwidth = (int) (deviceWidth * 0.75);

        this.container = container;


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


        return view;
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

        mListView = (ListView) view.findViewById(R.id.listview_single_chat);
        block_layout = (LinearLayout) view.findViewById(R.id.block_layout);
        emojicons = (RelativeLayout) view.findViewById(R.id.emojicons);
        check_box = (CheckBox) view.findViewById(R.id.check_box);
        mMessageEdtext = (sms19.inapp.msg.imoze.EmojiconEditText) view.findViewById(R.id.chat_msg);
        mSendBtn = (ImageView) view.findViewById(R.id.message_send_btn);
        mEmojiBtn = (ImageView) view.findViewById(R.id.imozebtn);
        mCameraBtn = (ImageView) view.findViewById(R.id.camera_new);
        time_select = (ImageView) view.findViewById(R.id.time_select);
        user_sms_creadit = (TextView) view.findViewById(R.id.user_sms_creadit);
        mEditEmojicon = (sms19.inapp.msg.imoze.EmojiconEditText) view.findViewById(R.id.chat_msg);
        mEditEmojicon.setUseSystemDefault(false);

        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listheader = inflater.inflate(R.layout.chathistorylist_header, null, false);

        fc_addcontact = (TextView) listheader.findViewById(R.id.fc_addcontact);
        fc_notnow = (TextView) listheader.findViewById(R.id.fc_notnow);
        addtodevicelay = (LinearLayout) listheader.findViewById(R.id.addtodevicelay);
        fc_addpreviousmsg = (TextView) listheader.findViewById(R.id.fc_addpreviousmsg);
        fc_loadmsgprogress = (ProgressBar) listheader.findViewById(R.id.fc_loadmsgprogress);
        fc_loadmsglay = (RelativeLayout) listheader.findViewById(R.id.fc_loadmsglay);

        mListView.addHeaderView(listheader);

        fc_addcontact.setOnClickListener(this);
        fc_notnow.setOnClickListener(this);
        fc_addpreviousmsg.setOnClickListener(this);
        time_select.setOnClickListener(this);


        homeActivity.getmActionBarImage().setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        mEmojiBtn.setOnClickListener(this);
        //mCameraBtn.setOnClickListener(this);

        //mCameraBtn.setOnClickListener(photoClick);
        mCameraBtn.setOnClickListener(this);
        chatAdapter = new BroadCastSecondAdapter(getActivity(), contactmodelsArrayList);
        chatAdapter.setClickListener(this);
        mListView.setAdapter(chatAdapter);


        mEditEmojicon.setOnClickListener(this);
        homeActivity.getmUserNameTitle().setText(frndname);

        fc_loadmsgprogress.setVisibility(View.GONE);
        fc_loadmsglay.setVisibility(View.GONE);
        addtodevicelay.setVisibility(View.GONE);
        block_layout.setVisibility(View.GONE);

        final Calendar c = Calendar.getInstance();
        pHour = c.get(Calendar.HOUR_OF_DAY);
        pMinute = c.get(Calendar.MINUTE);
        user_sms_creadit.setTag("0:5");
        //.

        mMessageEdtext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    mCameraBtn.setVisibility(View.GONE);
                    mSendBtn.setVisibility(View.VISIBLE);
                } else {
                    mCameraBtn.setVisibility(View.VISIBLE);
                    mSendBtn.setVisibility(View.GONE);
                }

            }
        });


    }


    @Override
    public void onClick(View v) {


        if (v == time_select) {

            show();
        }

        if (v.getId() == R.id.delete_group) {

            int pos = (Integer) v.getTag();
            if (ContactFragment.BROAD_CAST_FLAG == 1) { // Both
                contactlistNew.remove(pos);
                chatAdapter.setContactArrayList(contactlistNew);
                chatAdapter.notifyDataSetChanged();
            } else if (ContactFragment.BROAD_CAST_FLAG == 2) { // App users
                appUserListNew.remove(pos);

                chatAdapter.setContactArrayList(appUserListNew);
                chatAdapter.notifyDataSetChanged();
            } else if (ContactFragment.BROAD_CAST_FLAG == 3) { // Non app users
                nonAppUserNew.remove(pos);
                chatAdapter.setContactArrayList(nonAppUserNew);
                chatAdapter.notifyDataSetChanged();
            }


        }


        if (v.getId() == R.id.fc_addcontact) {
            String usernumber = remote_jid.split("@")[0];
            addContacttoDevice(usernumber);
            addtodevicelay.setVisibility(View.GONE);

        }
        if (v.getId() == R.id.fc_notnow) {
            addtodevicelay.setVisibility(View.GONE);
        }

        if (v == mSendBtn) {

            Utils.hideKeyBoardMethod(getActivity(), mSendBtn);
            if (emojicons.getVisibility() == View.VISIBLE) {
                emojicons.setVisibility(View.GONE);
            }

            if (Utils.isDeviceOnline(getActivity())) {

                if (GlobalData.connection != null && GlobalData.connection.isConnected()) {

                    final String message = mMessageEdtext.getText().toString().trim();


                    if (message != null && message.trim().length() != 0) {


                        if (!check_box.isChecked()) {
                            messageSend(message);
                        } else {
                            String timeValue = (String) user_sms_creadit.getTag();
                            if (!timeValue.equalsIgnoreCase("")) {

                                //if(ContactFragment.BROAD_CAST_FLAG==2){
                                messageSend(message);
                                //}


                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        String userId = Utils.getUserId(getActivity());
                                        postContactmap = new HashMap<String, Contactmodel>();
                                        if (ContactFragment.BROAD_CAST_FLAG == 1) {


                                            if (contactlistNew != null) {
                                                if (contactlistNew.size() > 0) {
                                                    for (int i = 0; i < contactlistNew.size(); i++) {
                                                        Contactmodel contactmodel = new Contactmodel();
                                                        contactmodel.setNumber(Utils.removeCountryCode(contactlistNew.get(i).getNumber(), getActivity()));
                                                        postContactmap.put(String.valueOf(i), contactmodel);
                                                    }

                                                    String contactstring = Utils.makeJsonarrayfromContactmap(postContactmap);
                                                    asyncTask = new CheckCreditAsyncTask(getActivity(), userId, contactstring, message, "0", "0", "0");
                                                    asyncTask.execute();
                                                    Utils.printLog("broadcast send to users through sms complete...");

                                                } else {
                                                    Toast.makeText(getActivity(), "Please select atleast one user",
                                                            Toast.LENGTH_SHORT).show();
                                                }

                                            }


                                        } else if (ContactFragment.BROAD_CAST_FLAG == 2) {
                                            postContactmap = new HashMap<String, Contactmodel>();
                                            if (appUserListNew != null) {
                                                if (appUserListNew.size() > 0) {
                                                    for (int i = 0; i < appUserListNew.size(); i++) {
                                                        Contactmodel contactmodel = new Contactmodel();
                                                        contactmodel.setNumber(Utils.removeCountryCode(appUserListNew.get(i).getNumber(), getActivity()));
                                                        postContactmap.put(String.valueOf(i), contactmodel);
                                                    }

                                                    String contactstring = Utils.makeJsonarrayfromContactmap(postContactmap);
                                                    asyncTask = new CheckCreditAsyncTask(getActivity(), userId, contactstring, message, "0", "0", "0");
                                                    asyncTask.execute();

                                                } else {
                                                    Toast.makeText(getActivity(), "Please select atleast one user",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                            Utils.printLog("broadcast send to users through sms complete...");


                                        } else if (ContactFragment.BROAD_CAST_FLAG == 3) {


                                            postContactmap = new HashMap<String, Contactmodel>();
                                            if (nonAppUserNew != null) {
                                                if (nonAppUserNew.size() > 0) {

                                                    for (int i = 0; i < nonAppUserNew.size(); i++) {
                                                        Contactmodel contactmodel = new Contactmodel();
                                                        contactmodel.setNumber(Utils.removeCountryCode(nonAppUserNew.get(i).getNumber(), getActivity()));
                                                        postContactmap.put(String.valueOf(i), contactmodel);
                                                    }

                                                    String contactstring = Utils.makeJsonarrayfromContactmap(postContactmap);
                                                    asyncTask = new CheckCreditAsyncTask(getActivity(), userId, contactstring, message, "0", "0", "0");
                                                    asyncTask.execute();

                                                } else {
                                                    Toast.makeText(getActivity(), "Please select atleast one user",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            Utils.printLog("broadcast send to users through sms complete...");


                                        }


                                    }
                                });

                            } else {
                                Toast.makeText(getActivity(), "Please choose delivery time!", Toast.LENGTH_SHORT).show();
                            }


                        }

                        mMessageEdtext.setText("");
                        //	messageSend(message);


                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Type message first.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Not connected to server", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), "Check your network connection", Toast.LENGTH_SHORT).show();
            }


        }
        if (v == mEmojiBtn) {

            Utils.hideKeyBoardMethod(getActivity(), mEmojiBtn);
            setEmojiconFragment(false);
        }
        if (v == mCameraBtn) {

			/*if (Utils.isDeviceOnline(getActivity())) {
                if (GlobalData.connection != null
						&& GlobalData.connection.isConnected()) {

					openAddfilePopup();
				} else {
					Toast.makeText(getActivity(), "Not connected to server",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(), "Check your network connection",
						Toast.LENGTH_SHORT).show();

			}*/


        }


        if (homeActivity.getmActionBarImage() == v) {

            callSingleUserProfile();

        }


    }


    public void callSingleUserProfile() {

        Fragment userdetailfrag = null;
        homeActivity.getmUserStatusTitle().setVisibility(View.GONE);
        homeActivity.getmActionBarImage().setVisibility(View.GONE);
        Bundle data = new Bundle();

        data.putString("remote_jid", remote_jid);
        data.putString("remote_name", frndname);
        data.putString("custom_status", customStatus);
        data.putByteArray("remote_pic", frndpic);
        data.putInt("isgroup", isgroup);
        data.putInt("isstranger", isstranger);
        data.putString("status", status);

        if (isgroup == 1) {
            userdetailfrag = new GroupProfile();
            userdetailfrag.setArguments(data);
            homeActivity.callFragmentWithAddBack(userdetailfrag, ConstantFlag.TAB_SINGLE_CHAT_ROOM_PROFILE_FRAGMENT);
        } else if (isgroup == 2) {

        } else {
            userdetailfrag = new SigleUserViewProfile();
            userdetailfrag.setArguments(data);
            homeActivity.callFragmentWithAddBack(userdetailfrag, ConstantFlag.TAB_SINGLE_CHAT_ROOM_PROFILE_FRAGMENT);
        }
    }


    public void openAddfilePopup() {
        recordfilePath = "";
        latlng = "";
        videoPath = "";
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_file_lay, this.container, false);
        popupWindow = new PopupWindow(popupView, popupwidth, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popUpwindowinIt(popupView);
        //popupWindow.showAtLocation(viewGlobal.findViewById(R.id.places), Gravity.CENTER, 0, 0);
        popupWindow.setAnimationStyle(R.anim.popupanim);
        popupWindow.showAsDropDown(viewGlobal.findViewById(R.id.places), 0, 0);

    }

    public void popUpwindowinIt(View view) {
        ImageView gallerybtn = (ImageView) view.findViewById(R.id.gallerybtn);
        ImageView photobtn = (ImageView) view.findViewById(R.id.photobtn);
        ImageView videobtn = (ImageView) view.findViewById(R.id.videobtn);
        ImageView audiobtn = (ImageView) view.findViewById(R.id.audiobtn);
        ImageView templatebtn = (ImageView) view.findViewById(R.id.templatebtn);
        RelativeLayout gallerylay = (RelativeLayout) view
                .findViewById(R.id.gallerylay);
        RelativeLayout photolay = (RelativeLayout) view
                .findViewById(R.id.photolay);
        RelativeLayout videolay = (RelativeLayout) view
                .findViewById(R.id.videolay);
        RelativeLayout audiolay = (RelativeLayout) view
                .findViewById(R.id.audiolay);
        RelativeLayout templatelay = (RelativeLayout) view
                .findViewById(R.id.templatelay);
        TextView gallerytxt = (TextView) view.findViewById(R.id.gallerytxt);
        TextView phototxt = (TextView) view.findViewById(R.id.phototxt);
        TextView videotxt = (TextView) view.findViewById(R.id.videotxt);
        TextView audiotxt = (TextView) view.findViewById(R.id.audiotxt);
        TextView templatetxt = (TextView) view.findViewById(R.id.templatetxt);
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
    }


    public void setpopUpclicks() {
        galleryClick = new OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity().getApplicationContext(), "gallery", Toast.LENGTH_SHORT).show();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                GlobalData.OnFilesendscreen = true;
                Intent i = new Intent(getActivity(), BucketHomeFragmentActivity.class);
                i.putExtra("username", frndname);
                getActivity().startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        };


        photoClick = new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity().getApplicationContext(), "photo", Toast.LENGTH_SHORT).show();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(
                        android.os.Environment.MEDIA_MOUNTED);
                if (isSDPresent) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "Kit19_pic");

                    mCapturedImageURI = getActivity().getContentResolver()
                            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);

                    GlobalData.OnFilesendscreen = true;
                    Intent cameraIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            mCapturedImageURI);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "No SD card sorry!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        videoclick = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity().getApplicationContext(), "video",
                        Toast.LENGTH_SHORT).show();
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
                            "No SD card sorry!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        audioClick = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity().getApplicationContext(), "audio",
                        Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                RecordAudio ru = new RecordAudio(getActivity());
                ru.ReSendalertDialog(getActivity());
            }
        };

        templateClick = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity().getApplicationContext(),
                        "Template", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                latlng = "";
            }
        };
    }


    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        homeActivity.onBothTabPageControlIsGone();
        homeActivity.getActionbar_title().setText(mLastTitle);
        homeActivity.getLayoutTab_contact_chat().setVisibility(View.VISIBLE);
        ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
        homeActivity.invalidateOptionMenuItem();

        ContactFragment.BROAD_CAST_FLAG = 1;

        obj = null;

        //ContactFragment contactFragment=ContactFragment.newInstance("");
        //if(contactFragment!=null){
        ///contactFragment.refreshListAdapter();
        //contactFragment.onResumeUpdate();
        //contactFragment.refreshListAdapter()/*(contactFragment.getAppUserList(), contactFragment.getNonAppUser())*/;
//	}

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE
                && resultCode == getActivity().RESULT_OK) {
            String type = data.getExtras().getString("type");
            String uri = data.getExtras().getString("data");
            if (type.equals("IMG")) {
                String picpath = uri;
                Bitmap originBitmap = null;
                sendimgFile(picpath, originBitmap);
            } else if (type.equals("VID")) {
                sendVideofile(uri);
            }

        } else if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(mCapturedImageURI, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndex(filePathColumn[0]);

            String picpath = cursor.getString(column_index);
            cursor.close();
            Bitmap finalBitmap = Utils.decodeFile(picpath,
                    GlobalData.fileorignalWidth, GlobalData.fileorignalheight);


            try {
                String savedpicpath = Utils.getfilePath(GlobalData.Imagefile);
                FileOutputStream fos = new FileOutputStream(savedpicpath);

                finalBitmap.compress(CompressFormat.JPEG, 80, fos);

                fos.close();
                if (finalBitmap != null) {
                    finalBitmap.recycle();
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

        } else if (requestCode == VIDEO_REQUEST
                && resultCode == getActivity().RESULT_OK) {

            if (videoPath != null && videoPath.trim().length() != 0) {
                sendVideofile(videoPath);
            }

        }
    }


    private void setChatupdateHandler() {
        // TODO Auto-generated method stub
        ChatupdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                String message = msg.getData().getString("message");
                String senderid = msg.getData().getString("senderid");
                String filePath = msg.getData().getString("filePath");
                String rowid = msg.getData().getString("rowid");
                final String msgTime = msg.getData().getString("msgtime");

                // byte[] avatar = msg.getData().getByteArray("picdata");
                Chatmodel model = new Chatmodel();
                if (message.startsWith(SingleChatRoomFrgament.sendfilefixString)) {
                    String[] urls = message.split("__");
                    String type = urls[2];
                    model.setMediatype(type);
                    model.setMediapath(filePath);
                    model.setMediaUrl(urls[1]);
                    if (!type.equals(GlobalData.Audiofile)) {
                        String mediathmb = urls[3];
                        if (mediathmb != null && mediathmb.trim().length() != 0) {
                            byte[] thmb = Utils.decodeToImage(mediathmb);
                            Bitmap bit = BitmapFactory.decodeByteArray(thmb, 0,
                                    thmb.length);

                            model.setThmbbitmap(bit);
                            // model.setMediathmb(thmb);
                        }
                    }
                    if (type.equals(GlobalData.Locationfile)) {
                        String latln = urls[4];
                        if (latln != null && latln.trim().length() != 0) {
                            model.setChatmessage(latln);
                        }

                    }

                } else {
                    model.setChatmessage(message);
                }

                model.setMine(false);
                model.setRemote_userid(senderid);
                model.setMessagerowid(rowid);
                model.setMsgDate(Utils.getmsgDate(msgTime));
                model.setMsgTime(Utils.getmsgTime(msgTime));
                // if (avatar != null) {
                // model.setPic(avatar);
                // }


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

                    fileType = GlobalData.Audiofile;
                    uploadAndSendSelectedfile(recordfilePath, fileType);
                }
            }
        };
    }

    public void messageSend(String msg) {
        try {
            String msgPacketId = sendMessage(msg, remote_jid);
            if (ContactFragment.BROAD_CAST_FLAG == 2 || ContactFragment.BROAD_CAST_FLAG == 1) {
                AddSentMessagetochatandDB(msg, msgPacketId);
            }
            //mEditEmojicon.setText("");
            Utils.printLog("Send message: " + msg);


        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Utils.printLog("Send message: Excption");
        }
    }

    public String sendMessage(final String message, String buddyJID)
            throws XMPPException {
        String msgpacketid = "";
        Utils.printLog("message send to server..and broadcast is" + "first");

        if (ContactFragment.BROAD_CAST_FLAG == 2 || ContactFragment.BROAD_CAST_FLAG == 1) {

            if (chatManager != null) {
                new Thread(new Runnable() {
                    public void run() {
                        Utils.printLog("broadcast send start...");
                        if (appUserListNew.size() > 0) {
                            for (int i = 0; i < appUserListNew.size(); i++) {
                                String jid = appUserListNew.get(i).getRemote_jid();
                                Chat chat = chatManager.createChat(jid, null);
                                try {

                                    String timeStap = Utils.currentTimeStamp();
                                    String userId = InAppMessageActivity.USER_ID;
                                    Slacktags slagTag = new Slacktags(InAppMessageActivity.myModel.getNumber(), userId, GlobalData.MESSAGE_TYPE_2, timeStap, userId + "_" + timeStap);
                                    org.jivesoftware.smack.packet.Message msg2 = new org.jivesoftware.smack.packet.Message();
                                    msg2.setType(org.jivesoftware.smack.packet.Message.Type.chat);
                                    msg2.setBody(message);
                                    msg2.addExtension(slagTag);

                                    chat.sendMessage(msg2);
                                    //chat.sendMessage(message);
                                    Utils.printLog("broadcast send to... "
                                            + jid);
                                } catch (XMPPException e) {
                                    // TODO Auto-generated catch block
                                    Utils.printLog("broadcast send excption...");
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please select atleast one user",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Utils.printLog("broadcast send complete...");
                    }
                }).start();

            }


        }
        return msgpacketid;
    }


    public void AddSentMessagetochatandDB(final String message,
                                          final String message_packetID) {

        new Thread(new Runnable() {
            public void run() {
                final String msgtime = String.valueOf(System
                        .currentTimeMillis());
                rowid = GlobalData.dbHelper.addchatToMessagetable(remote_jid,
                        message, myModel.getRemote_jid(), msgtime,
                        message_packetID, "1", "0");
                if (rowid != -1) {
                    GlobalData.dbHelper.addorupdateRecentTable(remote_jid,
                            String.valueOf(rowid));
                    GlobalData.dbHelper.updateContactmsgData(remote_jid,
                            message, msgtime);
                    if (isgroup == 2) {
                        for (int i = 0; i < broadcastusersid.size(); i++) {
                            String jid = broadcastusersid.get(i);
                            GlobalData.dbHelper.addchatToMessagetable(jid,
                                    message, myModel.getRemote_jid(), msgtime,
                                    "", "", "0");// not set yet msgPacket id for
                            // group so blank put
                            GlobalData.dbHelper.updateContactmsgData(jid,
                                    message, msgtime);
                        }
                    }
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
                        model.setSent_msg_success("1"); // add the sent msg tick
                        // default

                    }
                });
            }
        }).start();
    }


    public void uploadAndSendSelectedfile(String path, String filetype) {

        new UploadtoserverAsynctask().execute(path, filetype);
        // addfiletoUIandDB(path, filetype); //set this method in the above api
        // onPostExecute()
    }

    class UploadtoserverAsynctask extends AsyncTask<String, Void, Void> {
        String filepath = "";
        String filetype = "";
        String base64string = "";

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            filepath = params[0];
            filetype = params[1];
            if (fileType.equals(GlobalData.Videofile)) {
                Bitmap bit = Utils.decodeFile(videothmbpath,
                        GlobalData.filetransferthmb,
                        GlobalData.filetransferthmb);
                base64string = Utils.convertTobase64string(bit);
            } else if (!fileType.equals(GlobalData.Audiofile)) {
                Bitmap bit = Utils.decodeFile(filepath,
                        GlobalData.filetransferthmb,
                        GlobalData.filetransferthmb);
                base64string = Utils.convertTobase64string(bit);
            }
            String response = Rest.getInstance().sendUploadfile_Request(
                    filepath);
            if (response != null && response.trim().length() != 0) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getString("code").equals("0")
                            && resObj.getString("error").equals("false")) {
                        fileList = new ArrayList<Uploadfilemodel>();
                        JSONArray data = resObj.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject modelObj = data.getJSONObject(i);
                            Uploadfilemodel model = new Uploadfilemodel();
                            String url = modelObj.getString("path");
                            String type = modelObj.getString("type");
                            model.setFilepath(filepath);
                            model.setFileurl(url);
                            model.setFiletype(type);
                            fileList.add(model);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (fileList.size() > 0) {
                for (int i = 0; i < fileList.size(); i++) {
                    Uploadfilemodel model = fileList.get(i);
                    fileUrl = model.getFileurl();
                    String msg = "";

                    if (filetype.equals(GlobalData.Audiofile)) {
                        msg = sendfilefixString + fileUrl + "__" + fileType;

                    } else if (filetype.equals(GlobalData.Locationfile)) {
                        msg = sendfilefixString + fileUrl + "__" + fileType
                                + "__" + base64string + "__" + latlng.trim();

                    } else {
                        msg = sendfilefixString + fileUrl + "__" + fileType
                                + "__" + base64string;
                    }
                    try {
                        String msgPacketId = sendMessage(msg, remote_jid);

                        addfiletoUIandDB(filepath, filetype, msgPacketId, "1");

						/*
						 * chathistorylist.get(chathistorylist.size() - 1)
						 * .setStatus("S");
						 * GlobalData.dbHelper.updatestatusOfsendFileinDB();
						 * chathistoyAdapter.notifyDataSetChanged();
						 * chat_lv.setSelection(chathistorylist.size() - 1);
						 */
                    } catch (XMPPException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    public void addfiletoUIandDB(final String filepath, final String filetype,
                                 final String msgPacketId, final String sendfilestaus) {

        new Thread(new Runnable() {
            public void run() {
                final String msgtime = String.valueOf(System
                        .currentTimeMillis());
                if (filetype.equals(GlobalData.Locationfile)) {
                    rowid = GlobalData.dbHelper.addchatFileToMessagetable(
                            remote_jid, filepath, filetype,
                            myModel.getRemote_jid(), "S", "", latlng, "",
                            msgtime, msgPacketId, sendfilestaus, false);
                } else {
                    rowid = GlobalData.dbHelper.addchatFileToMessagetable(
                            remote_jid, filepath, filetype,
                            myModel.getRemote_jid(), "S", "", "", "", msgtime,
                            msgPacketId, sendfilestaus, false);
                }

                if (rowid != -1) {
                    GlobalData.dbHelper.addorupdateRecentTable(remote_jid,
                            String.valueOf(rowid));
                    GlobalData.dbHelper.updateContactmsgData(remote_jid,
                            Utils.updatemessage(filetype) + " sent", msgtime);
                    if (isgroup == 2) {
                        for (int i = 0; i < broadcastusersid.size(); i++) {
                            String jid = broadcastusersid.get(i);
                            if (filetype.equals(GlobalData.Locationfile)) {
                                GlobalData.dbHelper.addchatFileToMessagetable(
                                        jid, filepath, filetype,
                                        myModel.getRemote_jid(), "S", "",
                                        latlng, "", msgtime, "", "", false);// set
                                // blank
                                // msgPacket
                                // id
                                // and
                                // msgsendstatus
                            } else {
                                GlobalData.dbHelper.addchatFileToMessagetable(
                                        jid, filepath, filetype,
                                        myModel.getRemote_jid(), "S", "", "",
                                        "", msgtime, "", "", false);// set blank
                                // msgPacket id
                                // and
                                // msgsendstatus
                            }
                            GlobalData.dbHelper.updateContactmsgData(jid,
                                    Utils.updatemessage(filetype) + " sent",
                                    msgtime);
                        }
                    }
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
                        }
                        if (filetype.equals(GlobalData.Locationfile)) {
                            model.setChatmessage(latlng);
                        }

                        // /////////////////////////////////

						/*chathistorylist.get(chathistorylist.size() - 1)
								.setStatus("S");
						GlobalData.dbHelper.updatestatusOfsendFileinDB();
						chathistoyAdapter.notifyDataSetChanged();
						chat_lv.setSelection(chathistorylist.size() - 1);		*/
                        // ////////////////////////////////
                    }
                });
            }
        }).start();
    }


    public void sendimgFile(String picpath, Bitmap originBitmap) {
        // Bitmap finalBitmap = BitmapFactory.decodeFile(picpath);
        Bitmap finalBitmap = null;
        finalBitmap = Utils.decodeFile(picpath,
                GlobalData.fileorignalWidth, GlobalData.fileorignalheight);

        // finalBitmap = Utils.getResizedBitmap(finalBitmap,
        // GlobalData.fileorignalheight, GlobalData.fileorignalWidth);
        // finalBitmap = Utils.oritRotation(finalBitmap,
        // Uri.parse(picpath));
        try {
            String savedpicpath = Utils.getfilePath(GlobalData.Imagefile);
            FileOutputStream fos = new FileOutputStream(savedpicpath);
            if (originBitmap != null) {
                originBitmap.compress(CompressFormat.JPEG, 80, fos);
            } else {
                finalBitmap.compress(CompressFormat.JPEG, 80, fos);
            }
            fos.close();
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

            finalBitmap.compress(CompressFormat.JPEG, 80, fos);

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
		/*if (mPlayer != null) {
		mPlayer.release();
		mPlayer = null;
		playflag = false;
	}*/

    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        emojicons.setVisibility(View.VISIBLE);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEditEmojicon);

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        stopPlaying();
    }

    @Override
    public void messagePrint(String packetId, String status) {
        Handler mainHandler = new Handler(getActivity().getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {

                    //	chatAdapter.notifyDataSetChanged();
                    homeActivity.backPress();
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        };
        mainHandler.post(myRunnable);

    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEditEmojicon, emojicon);

    }

    public void addContacttoDevice(String number) {
        GlobalData.NumberAddtodevice = number;
        GlobalData.addContactTodevice = true;
        ArrayList<ContentValues> data = new ArrayList<ContentValues>();

        ContentValues row1 = new ContentValues();
        row1.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        row1.put(Phone.NUMBER, number);
        row1.put(Phone.TYPE, Phone.TYPE_MOBILE);
        data.add(row1);

        Intent i = new Intent(Intent.ACTION_INSERT, Contacts.CONTENT_URI);
        i.putParcelableArrayListExtra(Insert.DATA, data);

        startActivity(i);
        // startActivityForResult(i, 1222);
    }


    public void refreshChatAdapter() {
        // TODO Auto-generated method stub
        BroadCastGroupSecond chatRoomFrgament = newInstance("");
        if (chatRoomFrgament != null) {


            new Thread(new Runnable() {
                public void run() {
                    if (getActivity() != null) {

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                try {
                                    //	setChathistory();
                                } catch (Exception exception) {

                                }

                            }
                        });
                    }
                }
            }).start();


        }
    }


    @SuppressLint("NewApi")
    public void show() {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("Time Picker");
        d.setContentView(R.layout.numeric_picker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);
        np.setMaxValue(23); // max value 100
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);

        np2.setMaxValue(59); // max value 100
        np2.setMinValue(0);   // min value 0
        np2.setWrapSelectorWheel(false);


        // np.setOnValueChangedListener(this);
        b1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                user_sms_creadit.setTag(String.valueOf(np.getValue()) + " : " + String.valueOf(np.getValue()));
                user_sms_creadit.setText("Use my sms credits to send messages not delevired in App with in " + String.valueOf(np.getValue()) + ":" + String.valueOf(np2.getValue()) + " Min"); //set the value to textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


    }


}
