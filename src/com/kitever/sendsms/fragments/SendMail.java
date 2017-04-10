package com.kitever.sendsms.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.customviews.DateTimePickerDialog;
import com.kitever.models.Balance;
import com.kitever.models.ContactInfo;
import com.kitever.models.LoginInfo;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.sendsms.AddEmailManuallyActivity;
import com.kitever.sendsms.SelectEmailActivity;
import com.kitever.sendsms.SendEmailContactsAdapter;
import com.kitever.sendsms.SendSmsContactsSingleton;
import com.kitever.sendsms.SendSmsMail;
import com.kitever.utils.DateHelper;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.imoze.EmojiconsFragment;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.EmailTemplateActivity;
import sms19.listview.newproject.model.FetchGroupDetails.GroupDetails;
import sms19.listview.newproject.model.FetchSenderIDs;
import sms19.listview.webservice.webservice;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class SendMail extends Fragment implements NetworkManager,
        SmsMailInterface {

    private static final int SELECT_CONTACTS_REQUEST_CODE = 101;

    private static final int SELECT_TEMPLATE_REQUEST_CODE = 102;

    private static final int SELECT_MANUALLY_REQUEST_CODE = 103;

    private static final int KEY_SEND_EMAIL = 105;

    private static final int KEY_REPLY_EMAIL = 106;

    private static final int FROM_SEND_MAIL_MODULE = 104;

    private int tempId = 0;
    private DataBaseDetails dbObject = null;

    private LoginInfo loginInfo = null;

    private LinearLayout valueLinLayout, manualNoLinLayout;
    private TextView groupLabelTextView, contactsLabelTextView, mail_manualLabelTextView,timeTextView;
    private boolean makeEmojiVisible = true;

    private ListView mListView;
    private FrameLayout mailFrame;


    // private ArrayList<String> manualArrayList = new ArrayList<String>();
    // private TagView groupTagView;

    private ArrayList<GroupDetails> groupMailNameTagList = new ArrayList<GroupDetails>();

    private ArrayList<String> groupsMailArrayList = new ArrayList<String>();

    private ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();

    private AlertDialog.Builder groupBuilder = null;

    private ArrayList<ContactInfo> selectedContactsList = null;

    private long datetime;

    private static final int DIALOG_PICK_DATETIME_ID = 0;

    private static final String STATE_DATE_STARTED = "SendSmsActivity.datetime";

    protected boolean isCleanStart;
    // Hack - makes any subsequent calls to show the same DateTimePickerDialog
    // to reset dialog's datetime to the current shown in datetimeTextView.
    // Otherwise on the second call to show DateTimePickerDialog for the same
    // Activity instance the dialog will not be reinitialized with the current
    // datetime seen in the datetimeTextView.
    private boolean resetDateTimeDialog;

    private ImageView setTimeButton;

    private TextView editText;
    private EditText etReceipentMail;
    private ProgressDialog progressDialog = null;

    private SendEmailContactsAdapter adapter = null;
    private String USERID = "";

    private ArrayList<ContactInfo> manualNumArrayList = new ArrayList<>();

    private TextView balValueTextView;

    private RelativeLayout emojicons;
    private EmojiconsFragment emojiconsFragment = null;
    private ImageView mEmojiBtn;

    private CheckBox unicodeCheckBox;

    public static SendMail newIntance(LoginInfo loginInfo) {
        SendMail mail = new SendMail();
        Bundle args = new Bundle();
        args.putParcelable("login", loginInfo);
        mail.setArguments(args);
        return mail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        loginInfo = bundle.getParcelable("login");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_mail, container,
                false);
        SendSmsMail.tagPackage[1] = getTag();

        Log.v("mail", "onCreateView");
        SharedPreferences chatPrefs = getActivity().getSharedPreferences(
                "chatPrefs", getActivity().MODE_PRIVATE);
        USERID = chatPrefs.getString("userId", "");

        SharedPreferences prfs = getActivity().getSharedPreferences("profileData",
                Context.MODE_PRIVATE);
        String emailid = prfs.getString("EMail", "");
        isCleanStart = (savedInstanceState == null);
        if (isCleanStart) {
            datetime = System.currentTimeMillis();
        } else {
            datetime = savedInstanceState.getLong(STATE_DATE_STARTED);
        }

        balValueTextView = (TextView) convertView
                .findViewById(R.id.mail_balValueTextView);
        etReceipentMail = (EditText) convertView
                .findViewById(R.id.mail_ReceipentId);
        setRobotoThinFont(etReceipentMail,getActivity());

        etReceipentMail.setText(emailid);
        GetReplyToMailID();

        final LinearLayout groupsLinearLayout = (LinearLayout) convertView
                .findViewById(R.id.mail_groupsLinearLayout);
        final LinearLayout manualLinearLayout = (LinearLayout) convertView
                .findViewById(R.id.mail_manualLinearLayout);
        final LinearLayout reply_to_layout = (LinearLayout) convertView
                .findViewById(R.id.reply_to_layout);
        reply_to_layout.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        groupsLinearLayout.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        manualLinearLayout.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        manualLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        AddEmailManuallyActivity.class);
                // intent.putStringArrayListExtra("LIST",manualArrayList);
                intent.putExtra("LIST", manualNumArrayList);
                startActivityForResult(intent, SELECT_MANUALLY_REQUEST_CODE);
            }
        });

        TextView topUp = (TextView) convertView.findViewById(R.id.mail_top_up);
        topUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        getActivity());
                alert.setTitle("TopUp");
                WebView wv = new WebViewHelper(getActivity());
                wv.clearHistory();
                wv.getSettings().setJavaScriptEnabled(true);
                wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                String Pass = sms19.inapp.msg.constant.Utils
                        .getPassword(getActivity());
                String postData = "password=" + Pass;// &id=236";
                wv.postUrl(
                        "http://kitever.com/BuyCredit.aspx?tab=topup&userid="
                                + USERID,
                        EncodingUtils.getBytes(postData, "BASE64"));

                // wv.loadUrl("http://kitever.com/BuyCredit.aspx?tab=plans&userid="
                // + UserId);
                // wv.setWebViewClient(new WebViewClient());
                WebClientClass webViewClient = new WebClientClass();
                wv.setWebViewClient(webViewClient);

                alert.setView(wv);
                alert.setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                alert.show();

            }
        });

        groupLabelTextView = (TextView) convertView
                .findViewById(R.id.mail_groupLabelTextView);
        contactsLabelTextView = (TextView) convertView
                .findViewById(R.id.mail_contactsLabelTextView);
        mail_manualLabelTextView= (TextView) convertView
                .findViewById(R.id.mail_manualLabelTextView);

        timeTextView = (TextView) convertView
                .findViewById(R.id.mail_timeTextView);

        setRobotoThinFont(groupLabelTextView,getActivity());
        setRobotoThinFont(contactsLabelTextView,getActivity());
        setRobotoThinFont(mail_manualLabelTextView,getActivity());

        timeTextView.setVisibility(View.INVISIBLE);
        groupsLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println(" group amil "
                        + groupsMailArrayList.toString());
                if (groupsMailArrayList != null) {
                    if (groupsMailArrayList.size() == 0) {
                        Toast.makeText(getActivity(), "No group(s) exists",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    return;
                }

                if (groupBuilder == null) {

                    groupBuilder = getMailGroupDialog();
                    groupBuilder.show();
                } else {
                    groupBuilder.show();
                }

            }
        });

        // groupTagView = (TagView) findViewById(R.id.tag_group);
        valueLinLayout = (LinearLayout) convertView
                .findViewById(R.id.mail_valueLinLayout);
        valueLinLayout.setVisibility(View.GONE);

        manualNoLinLayout = (LinearLayout) convertView
                .findViewById(R.id.mail_manualNoLinLayout);
        manualNoLinLayout.setVisibility(View.GONE);

        mListView = (ListView) convertView.findViewById(R.id.mail_listView);
        mailFrame = (FrameLayout) convertView.findViewById(R.id.mail_frame);
        mailFrame.setVisibility(View.GONE);

        final LinearLayout contactLinearLayout = (LinearLayout) convertView
                .findViewById(R.id.mail_contactLinearLayout);
        contactLinearLayout.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        contactLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (loginInfo != null) {
                    if (loginInfo.uid != null) {
                        Intent intent = new Intent(getActivity(),
                                SelectEmailActivity.class);
                        intent.putExtra("FROM", FROM_SEND_MAIL_MODULE);
                        intent.putExtra("userId", loginInfo.uid);
                        startActivityForResult(intent,
                                SELECT_CONTACTS_REQUEST_CODE);
                    }
                }
            }
        });

        final ImageView templateButton = (ImageView) convertView
                .findViewById(R.id.mail_imozebtn);

        templateButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        EmailTemplateActivity.class);
                intent.putExtra("FROM", FROM_SEND_MAIL_MODULE);
                intent.putExtra("userId", loginInfo.uid);
                startActivityForResult(intent, SELECT_TEMPLATE_REQUEST_CODE);
            }
        });


        editText = (TextView) convertView.findViewById(R.id.mail_template);
        editText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        EmailTemplateActivity.class);
                intent.putExtra("FROM", FROM_SEND_MAIL_MODULE);
                intent.putExtra("userId", loginInfo.uid);
                startActivityForResult(intent, SELECT_TEMPLATE_REQUEST_CODE);
            }
        });

        // editText.setUseSystemDefault(false);

        final String text = getActivity().getIntent().getStringExtra(
                "templateText");

        if (text != null) {
            editText.setText(text);
        }

        final TextView charCountTextView = (TextView) convertView
                .findViewById(R.id.mail_charCountTextView);

        setTimeButton = (ImageView) convertView
                .findViewById(R.id.mail_schedule_time);

        setTimeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // hack, see details near the field declaration (at the top of
                // the class)
                resetDateTimeDialog = true;

                showDialog(DIALOG_PICK_DATETIME_ID);

            }
        });

        final ImageView sendButton = (ImageView) convertView
                .findViewById(R.id.mail_sendButton);

        sendButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                showProgressDialog();

                final String txt_message = etReceipentMail.getText().toString();

                if (tempId == 0) {
                    removeProgressDialog();
                    Toast.makeText(getActivity(), "Please select a template",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (txt_message.trim().length() < 6
                        || !isValidEmail(txt_message)) {
                    removeProgressDialog();
                    Toast.makeText(getActivity(),
                            "Reply to email id not valid", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                final ArrayList<ContactInfo> contactInfoArrayList = new ArrayList<>();

                if (selectedContactsList != null) {
                    contactInfoArrayList.addAll(selectedContactsList);
                }

                if (manualNumArrayList != null) {
                    contactInfoArrayList.addAll(manualNumArrayList);
                }

                boolean isContactSelected = false;
                boolean isGroupSelected = false;

                final StringBuffer emailString = new StringBuffer();

                final StringBuffer groupString = new StringBuffer();

                if (contactInfoArrayList != null) {
                    for (ContactInfo contactInfo : contactInfoArrayList) {
                        if (contactInfo.isSelected) {
                            isContactSelected = true;
                            String countryCode = contactInfo.countryCode;
                            // if (countryCode.startsWith("+")) {
                            // countryCode = countryCode.substring(1,
                            // countryCode.length());
                            // }
                            emailString.append(contactInfo.getContact_email()
                                    + ",");
                        }
                    }
                }

                String emailStr = "";

                if (emailString.length() > 1) {
                    emailStr = emailString.substring(0,
                            emailString.length() - 1).toString();
                }

                for (int i = 0; i < mSelectedItems.size(); i++) {
                    isGroupSelected = true;
                    GroupDetails groupDetails = groupMailNameTagList
                            .get(mSelectedItems.get(i));
                    groupString.append(groupDetails.group_name + ",");
                }

                String groupStr = "";

                if (groupString.length() > 1) {
                    groupStr = groupString.substring(0,
                            groupString.length() - 1);
                }

                final String templateID = "";

                if (!isContactSelected && !isGroupSelected) {
                    removeProgressDialog();
                    Toast.makeText(getActivity(),
                            "Please select atleast one contact or group",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                String date = null, time = null;

                if (!timeTextView.getText().equals("")) {
                    final String dateTime = timeTextView.getText().toString();
                    if (dateTime.contains(" ")) {
                        final String arrDateTime[] = dateTime.split(" ");
                        date = arrDateTime[0];
                        time = arrDateTime[1] + " " + arrDateTime[2];
                    }
                } else {
                    datetime = System.currentTimeMillis();
                    final String formattedDatetime = DateHelper
                            .myFormat3(datetime);
                    // log("acceptDatetime: got " + formattedDatetime);
                    // SendSmsActivity.this.datetime = datetime;
                    // setTimeButton.setText(formattedDatetime);
                    if (formattedDatetime.contains(" ")) {
                        final String arrDateTime[] = formattedDatetime
                                .split(" ");
                        date = arrDateTime[0];
                        time = arrDateTime[1] + " " + arrDateTime[2];
                        // 2016 - 12 - 16
                    }
                }
                SendMailAll(emailStr, date, time, groupStr);
            }
        });

        initFun();

        return convertView;
    }

    protected void showDialog(int id) {
        // TODO Auto-generated method stub
        if (id == DIALOG_PICK_DATETIME_ID) {
            new DateTimePickerDialog(getActivity(),
                    new DateTimePickerDialog.DateTimeAcceptor() {
                        public void accept(long datetime) {
                            final String formattedDatetime = DateHelper
                                    .myFormat3(datetime);
                            // log("acceptDatetime: got " + formattedDatetime);
                            SendMail.this.datetime = datetime;
                            timeTextView.setVisibility(View.VISIBLE);
                            timeTextView.setText(formattedDatetime);
                        }
                    }, new DateTimePickerDialog.DateTimeDeselector() {

                @Override
                public void deselect() {
                    timeTextView.setText("");
                    timeTextView.setVisibility(View.INVISIBLE);
                }
            }, datetime).show();
        }

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        } else {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    private void removeProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        if (requestCode == SELECT_CONTACTS_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == getActivity().RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                setSelectedContacts();
            }
        }

        if (requestCode == SELECT_TEMPLATE_REQUEST_CODE) {
            // Make sure the request was successful
            System.out.println("result code" + resultCode);
            if (resultCode == getActivity().RESULT_OK) {
                editText.setText(data.getStringExtra("TemplateTitle")
                        .toString());
                tempId = Integer.parseInt(data.getStringExtra("TemplateId")
                        .toString());
            }
        }

        if (requestCode == SELECT_MANUALLY_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == getActivity().RESULT_OK) {

                // final ArrayList<String>
                // arrayList=data.getStringArrayListExtra("MANUAL_NUM_LIST");

                @SuppressWarnings("unchecked")
                final ArrayList<ContactInfo> arrayList = (ArrayList<ContactInfo>) data
                        .getSerializableExtra("MANUAL_NUM_LIST");

                if (arrayList != null) {
                    if (arrayList.size() >= 0) {
                        // manualArrayList.clear();
                        // manualArrayList.addAll(arrayList);
                        manualNumArrayList.clear();
                        manualNumArrayList.addAll(arrayList);
                        setManualNumbers();
                    }
                }
            }
        }
    }

    private void setSelectedContacts() {
        if (SendSmsContactsSingleton.getInstance() == null) {
            Log.i("SendSmsContactSingleton", "null");
            // do nothing
        } else {
            if (SendSmsContactsSingleton.getInstance().getEmailInfos() == null) {
                // do nothing
                Log.i("getcontactsSingleton", "null");
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (selectedContactsList != null) {
                            selectedContactsList.clear();
                        } else {
                            selectedContactsList = new ArrayList<ContactInfo>();
                        }
                        final ArrayList<ContactInfo> contactInfos = SendSmsContactsSingleton
                                .getInstance().getEmailInfos();
                        Log.i("contactInfos", "--" + contactInfos.size());
                        for (ContactInfo contactInfo : contactInfos) {
                            if (contactInfo.isSelected) {
                                selectedContactsList.add(contactInfo);
                                Log.i("is selected", "--" + contactInfo);
                            }
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                contactsLabelTextView
                                        .setText("Select your contacts" + " ("
                                                + selectedContactsList.size()
                                                + ")");

                                if (selectedContactsList.size() == 0) {
                                    mailFrame.setVisibility(View.GONE);
                                } else {
                                    mailFrame.setVisibility(View.VISIBLE);
                                }

                                adapter = new SendEmailContactsAdapter(
                                        getActivity(), selectedContactsList);
                                mListView.setAdapter(adapter);

                            }
                        });
                    }
                }).start();
            }
        }
    }

    public void initFun() {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                // dbObject = new DataBaseDetails(getActivity());

                // fetchLoginDetails();

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        totalavailablebalance();
                        // getGroupDetails();
                    }
                });
            }
        }).start();
    }

    // total balance available in user account fetch from server
    private void totalavailablebalance() {

        final String url = webservice.getCurrentBalance.geturl(loginInfo.uid);

        StringRequest strreq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {

                        try {
                            JSONObject jsonObject = new JSONObject(Response);

                            JSONArray array = jsonObject
                                    .getJSONArray("UserTopTransactionDetails");

                            JSONObject jsonObject2 = array.getJSONObject(0);

                            Gson gson = new Gson();

                            Balance balance = gson.fromJson(
                                    jsonObject2.toString(), Balance.class);

                            // final String balSms_Str =
                            // (String)jsonObject2.getString("balance");

                            final String balSms_Str = balance.balance;

                            if (balSms_Str != null) {
                                if (!balSms_Str.equals("")) {
                                    balValueTextView.setText(balSms_Str);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strreq);
    }

    private void resetValues() {

        totalavailablebalance();

        editText.setText("Select Template");

        valueLinLayout.removeAllViews();

        mSelectedItems.clear();

        manualNumArrayList.clear();

        manualNoLinLayout.removeAllViews();

        mListView.setVisibility(View.GONE);

        groupBuilder = null;

        contactsLabelTextView.setText("Select your contacts" + " (" + 0 + ")");

        groupLabelTextView.setText("Select your groups (0)");

        valueLinLayout.setVisibility(View.GONE);

        timeTextView.setText("");
        timeTextView.setVisibility(View.INVISIBLE);

        if (selectedContactsList != null) {
            for (ContactInfo contactInfo : selectedContactsList) {
                contactInfo.isSelected = false;
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    // public void setMailGroupDetails(ArrayList<GroupDetails> groupNameTagList,
    // ArrayList<String> groupsArrayList) {
    // this.groupNameTagList = groupNameTagList;
    // this.groupsArrayList = groupsArrayList;
    // System.out.println(" group " + this.groupsArrayList.toString());
    // }

    @Override
    public void ChangeFragment(ArrayList<GroupDetails> NameTagList,
                               ArrayList<String> Arraylist) {
        if (Arraylist != null) {
            groupMailNameTagList = NameTagList;
            groupsMailArrayList = Arraylist;
            // Toast.makeText(getActivity(), "Mail" + NameTagList.toString(),
            // Toast.LENGTH_SHORT).show();
            // System.out.println(" group mail" +
            // groupsMailArrayList.toString());
        } else {
            groupMailNameTagList = new ArrayList<GroupDetails>();
            groupsMailArrayList = new ArrayList();
        }
    }

    @Override
    public void setSendersId(ArrayList<FetchSenderIDs> sendersId) {
        /*No Use*/
    }

    @Override
    public void Backpressed() {

    }

    // public void getGroupDetails() {
    // // Toast.makeText(getActivity().getApplicationContext(),
    // // "MailgetGroupDetails", Toast.LENGTH_SHORT).show();
    // new webservice(null,
    // webservice.GetAllGroupDetails.geturl(loginInfo.uid)
    // + "&type=Mail", webservice.TYPE_GET,
    // webservice.TYPE_GET_GROUP_DETAILS, new ServiceHitListener() {
    //
    // @Override
    // public void onSuccess(Object Result, int id) {
    //
    // if (id == webservice.TYPE_GET_GROUP_DETAILS) {
    //
    // FetchGroupDetails Model = (FetchGroupDetails) Result;
    //
    // final List<GroupDetails> list = Model
    // .getGroupDetails();
    //
    // groupMailNameTagList.clear();
    // groupsMailArrayList.clear();
    //
    // // groupNameTagList.addAll(list);
    //
    // final int size = list.size();
    //
    // for (int i = 0; i < size; i++) {
    //
    // final String noOfContacts = list.get(i).NoOfContacts;
    //
    // if (noOfContacts != null) {
    // if (!noOfContacts.trim().equals("0")) {
    //
    // groupMailNameTagList.add(list.get(i));
    //
    // groupsMailArrayList.add(list.get(i).group_name
    // + " ("
    // + list.get(i).NoOfContacts
    // + ")");
    // }
    // }
    // }
    // }
    // removeProgressDialog();
    // }
    //
    // @Override
    // public void onError(String Error, int id) {
    //
    // removeProgressDialog();
    // Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT)
    // .show();
    //
    // }
    // });
    // }

    public void fetchLoginDetails() {

        dbObject.Open();

        Cursor c = dbObject.getLoginDetails();
        loginInfo = new LoginInfo();

        while (c.moveToNext()) {
            loginInfo.userlogin = c.getString(6);
            loginInfo.mobile = c.getString(1);
            loginInfo.uid = c.getString(3);
            loginInfo.password = c.getString(5);
        }
        c.close();
        dbObject.close();

    }

    private AlertDialog.Builder getMailGroupDialog() {
        AlertDialog.Builder groupBuilder = new AlertDialog.Builder(
                getActivity());
        // Set the dialog title

        String[] groupArr = new String[groupsMailArrayList.size()];
        boolean[] boolArr = new boolean[groupsMailArrayList.size()];
        groupArr = groupsMailArrayList.toArray(groupArr);

        groupBuilder
                .setTitle("Displaying groups with 1 or more Mail")
                // Specify the list array, the items to be selected by default
                // (null for none),
                // and the listener through which to receive callbacks when
                // items are selected
                .setMultiChoiceItems(groupArr, boolArr,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to
                                    // the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the
                                    // array, remove it
                                    mSelectedItems.remove(Integer
                                            .valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems
                        // results somewhere
                        // or return them to the component that opened
                        // the dialog
                        setTags();
                    }
                })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

        return groupBuilder;

    }

    private void setManualNumbers() {

        if (manualNumArrayList != null) {
            if (manualNumArrayList.size() > 0) {
                manualNoLinLayout.removeAllViews();
                manualNoLinLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < manualNumArrayList.size(); i++) {
                    final TextView textView = new TextView(getActivity());
                    textView.setId(i);
                    textView.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            new LayoutParams(LayoutParams.WRAP_CONTENT,
                                    LayoutParams.WRAP_CONTENT));

                    params.setMargins(8, 8, 8, 8);
                    textView.setPadding(16, 16, 16, 16);
                    textView.setLayoutParams(params);

                    final int sdk = android.os.Build.VERSION.SDK_INT;

                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        textView.setBackgroundDrawable(getResources()
                                .getDrawable(R.drawable.roundcorner_mychatbg));
                    } else {
                        textView.setBackground(getResources().getDrawable(
                                R.drawable.roundcorner_mychatbg));
                    }

                    textView.setText(manualNumArrayList.get(i)
                            .getContact_email());

                    manualNoLinLayout.addView(textView);

                }
            } else {
                manualNoLinLayout.setVisibility(View.GONE);
            }
        } else {
            manualNoLinLayout.setVisibility(View.GONE);
        }

    }

    private void setTags() {

        valueLinLayout.removeAllViews();

        if (mSelectedItems != null) {
            groupLabelTextView.setText("Select your groups" + " ("
                    + mSelectedItems.size() + ")");
            if (mSelectedItems.size() == 0) {
                valueLinLayout.setVisibility(View.GONE);
            } else {
                valueLinLayout.setVisibility(View.VISIBLE);
            }
        } else {
            groupLabelTextView.setText("Select your groups (0)");
            valueLinLayout.setVisibility(View.GONE);
        }

        for (int i = 0; i < mSelectedItems.size(); i++) {
            final TextView textView = new TextView(getActivity());
            textView.setId(i);
            textView.setTextColor(Color.parseColor("#006966"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    new LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));

            params.setMargins(8, 8, 8, 8);
            textView.setPadding(16, 16, 16, 16);
            textView.setLayoutParams(params);

            final int sdk = android.os.Build.VERSION.SDK_INT;

            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                textView.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.roundcorner_mychatbg));
            } else {
                textView.setBackground(getResources().getDrawable(
                        R.drawable.roundcorner_mychatbg));
            }
            textView.setText(groupsMailArrayList.get(mSelectedItems.get(i)));

            valueLinLayout.addView(textView);
        }
    }

    private static class WebViewHelper extends WebView {
        public WebViewHelper(Context context) {
            super(context);
        }

        // Note this!
        @Override
        public boolean onCheckIsTextEditor() {
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    if (!hasFocus())
                        requestFocus();
                    break;
            }

            return super.onTouchEvent(ev);
        }
    }

    private class WebClientClass extends WebViewClient {
        ProgressDialog pd = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (pd == null) {
                pd = new ProgressDialog(getActivity());
            }
            pd.setTitle("Please wait");
            pd.setMessage("Page is loading..");
            pd.show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (pd != null)
                pd.dismiss();
            super.onPageFinished(view, url);
        }
    }

    private void SendMailAll(String emailString, String date, String time,
                             String groupString) {

        String format = time.split(" ")[1];
        String hrMin[] = time.split(" ")[0].split(":");
        if (Utils.isDeviceOnline(getActivity())) {
            showProgressDialog();
            Map map = new HashMap<>();
            map.put("Page", "EmailSent");
            map.put("UserId", Utils.getUserId(getActivity()));
            map.put("mid", "10043");
            map.put("SelectedItem", "Email or Emails");
            map.put("ToMailIDs", emailString);
            map.put("ReplyToID", etReceipentMail.getText().toString());
            map.put("scheduledate", date);
            map.put("scheduleHrs", hrMin[0]);
            map.put("schedulemin", hrMin[1]);
            map.put("scheduleday", format);
            map.put("Mail_Cc", "");
            map.put("Subject", "");
            map.put("MessageBody", "");
            map.put("GroupNames", groupString);
            map.put("TemplateId", "" + tempId);
            // map.put("UserLogin", getUserLogin());
            // map.put("Password", getPassWord());

            Log.i("EmailSent", "map - " + map.toString());

            try {
                new RequestManager().sendPostRequest(this, KEY_SEND_EMAIL, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void GetReplyToMailID() {

        if (Utils.isDeviceOnline(getActivity())) {
            showProgressDialog();
            Map map = new HashMap<>();
            map.put("Page", "GetReplyToMailID");
            map.put("UserId", Utils.getUserId(getActivity()));
            try {
                new RequestManager().sendPostRequest(this, KEY_REPLY_EMAIL, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        removeProgressDialog();
        Log.i("response mail", response);
        if(requestId==KEY_SEND_EMAIL)
        {
            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
            if (true) {
                groupMailNameTagList = new ArrayList<GroupDetails>();
                groupsMailArrayList = new ArrayList<String>();
                mSelectedItems = new ArrayList<Integer>();
                groupBuilder = null;
                selectedContactsList = new ArrayList<ContactInfo>();
                if(adapter!=null) adapter.notifyDataSetChanged();

                resetValues();
            }
        }
        else if(requestId==KEY_REPLY_EMAIL)
        {
            if(response!=null)
            {
                try
                {
                    JSONObject object=new JSONObject(response);
                    if(object.has("ReplyToMailID"))
                    {
                        JSONArray jarray=object.getJSONArray("ReplyToMailID");
                        JSONObject jobject = jarray.getJSONObject(0);
                        String emailid=jobject.getString("MailId");
                        etReceipentMail.setText(emailid);
                    }
                }
                catch(JSONException e){
                    etReceipentMail.setText("");
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        removeProgressDialog();
        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("mail", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("mail", "onPause");
    }

}
