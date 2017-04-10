package com.kitever.sendsms.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.kitever.sendsms.AddContactManuallyActivity;
import com.kitever.sendsms.SelectContactsActivity;
import com.kitever.sendsms.SendSMSContactsAdapter;
import com.kitever.sendsms.SendSmsContactsSingleton;
import com.kitever.sendsms.SendSmsMail;
import com.kitever.sendsms.TemplateActivity;
import com.kitever.utils.DateHelper;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.imoze.EmojiconEditText;
import sms19.inapp.msg.imoze.EmojiconGridFragment;
import sms19.inapp.msg.imoze.EmojiconRecents;
import sms19.inapp.msg.imoze.EmojiconRecentsGridFragment;
import sms19.inapp.msg.imoze.EmojiconRecentsManager;
import sms19.inapp.msg.imoze.EmojiconsFragment;
import sms19.inapp.msg.imozemodel.Emojicon;
import sms19.inapp.msg.imozemodel.Nature;
import sms19.inapp.msg.imozemodel.Objects;
import sms19.inapp.msg.imozemodel.People;
import sms19.inapp.msg.imozemodel.Places;
import sms19.inapp.msg.imozemodel.Symbols;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.FetchGroupDetails.GroupDetails;
import sms19.listview.newproject.model.FetchSenderIDs;
import sms19.listview.webservice.webservice;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class SendSms extends Fragment implements
        sms19.inapp.msg.imoze.EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener,
        ViewPager.OnPageChangeListener, EmojiconRecents, SmsMailInterface {

    private static final int SELECT_CONTACTS_REQUEST_CODE = 101;
    private static final int SELECT_TEMPLATE_REQUEST_CODE = 102;
    private static final int SELECT_MANUALLY_REQUEST_CODE = 103;
    private static final int FROM_SEND_SMS_MODULE = 104;

    private static final String ARG_PARAM1 = "param1";


    private DataBaseDetails dbObject = null;

    private LoginInfo loginInfo = null;

    private LinearLayout valueLinLayout, manualNoLinLayout;
    private TextView groupLabelTextView, contactsLabelTextView, timeTextView;
    private boolean makeEmojiVisible = true;

    private ListView mListView;

    // private ArrayList<String> manualArrayList = new ArrayList<String>();
    // private TagView groupTagView;

    private ArrayList<GroupDetails> groupSmsNameTagList;

    private ArrayList<String> groupsSmsArrayList;

    private ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();

    private AlertDialog.Builder groupBuilder = null;

    private ArrayList<ContactInfo> selectedContactsList = null;

    private long datetime;

    private static final int DIALOG_PICK_DATETIME_ID = 0;

    private static final String STATE_DATE_STARTED = "SendSms.datetime";

    protected boolean isCleanStart;
    // Hack - makes any subsequent calls to show the same DateTimePickerDialog
    // to reset dialog's datetime to the current shown in datetimeTextView.
    // Otherwise on the second call to show DateTimePickerDialog for the same
    // Activity instance the dialog will not be reinitialized with the current
    // datetime seen in the datetimeTextView.
    private boolean resetDateTimeDialog;

    private ImageButton setTimeButton;

    private EmojiconEditText editText;

    private ProgressDialog progressDialog = null;

    private SendSMSContactsAdapter adapter = null;
    private String param1, USERID = "";

    private ArrayList<ContactInfo> manualNumArrayList = new ArrayList<>();

    private TextView balValueTextView;

    private RelativeLayout emojicons;
    private EmojiconsFragment emojiconsFragment = null;
    private ImageView mEmojiBtn;

    private CheckBox unicodeCheckBox;
    private Spinner sendersIdSpinner;
    private ArrayList<FetchSenderIDs> fetchSendersId;
    HashMap<Integer, Dialog> mDialogs = new HashMap<Integer, Dialog>();

    // View pager
    private OnEmojiconBackspaceClickedListener mOnEmojiconBackspaceClickedListener;
    private int mEmojiTabLastSelectedIndex = -1;
    private View[] mEmojiTabs = null;
    private PagerAdapter mEmojisAdapter;
    private EmojiconRecentsManager mRecentsManager;
    private boolean mUseSystemDefault = false;
    private ViewPager emojisPager;
    private Context context;
    private static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";

    private SmsMailInterface smsMailInterface;

    public static SendSms newIntance(LoginInfo loginInfo, String template) {
        SendSms mail = new SendSms();
        Bundle args = new Bundle();
        args.putParcelable("login", loginInfo);
        args.putString(ARG_PARAM1, template);
        mail.setArguments(args);
        return mail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUseSystemDefault = getArguments().getBoolean(
                    USE_SYSTEM_DEFAULT_KEY);

            loginInfo = getArguments().getParcelable("login");
            param1 = getArguments().getString(ARG_PARAM1);

        } else {
            mUseSystemDefault = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_sms, container,
                false);
        SendSmsMail.tagPackage[0] = getTag();
        SharedPreferences chatPrefs = getActivity().getSharedPreferences(
                "chatPrefs", getActivity().MODE_PRIVATE);
        USERID = chatPrefs.getString("userId", "");
        isCleanStart = (savedInstanceState == null);
        if (isCleanStart) {
            datetime = System.currentTimeMillis();
        } else {
            datetime = savedInstanceState.getLong(STATE_DATE_STARTED);
        }

        sendersIdSpinner = (Spinner) convertView.findViewById(R.id.sms_senderId);
        fetchSendersId = new ArrayList<>();
        balValueTextView = (TextView) convertView
                .findViewById(R.id.balValueTextView);

        final LinearLayout groupsLinearLayout = (LinearLayout) convertView
                .findViewById(R.id.groupsLinearLayout);


        final LinearLayout manualLinearLayout = (LinearLayout) convertView
                .findViewById(R.id.manualLinearLayout);

        groupsLinearLayout.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        manualLinearLayout.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        manualLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        AddContactManuallyActivity.class);
                // intent.putStringArrayListExtra("LIST",manualArrayList);
                intent.putExtra("LIST", manualNumArrayList);
                startActivityForResult(intent, SELECT_MANUALLY_REQUEST_CODE);
            }
        });

        TextView topUp = (TextView) convertView.findViewById(R.id.top_up);
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
                .findViewById(R.id.groupLabelTextView);
        contactsLabelTextView = (TextView) convertView
                .findViewById(R.id.contactsLabelTextView);
        TextView manualLabelTextView = (TextView) convertView
                .findViewById(R.id.manualLabelTextView);

        setRobotoThinFont(groupLabelTextView, getActivity());
        setRobotoThinFont(contactsLabelTextView, getActivity());
        setRobotoThinFont(manualLabelTextView, getActivity());

        timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);

        groupsLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println(" group sms "
                        + groupsSmsArrayList.toString());
                if (groupsSmsArrayList != null) {
                    if (groupsSmsArrayList.size() == 0) {
                        Toast.makeText(getActivity(), "No group(s) exists",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    return;
                }

                if (groupBuilder == null) {

                    groupBuilder = getGroupDialog();
                    groupBuilder.show();
                } else {
                    groupBuilder.show();
                }

            }
        });

        // groupTagView = (TagView) findViewById(R.id.tag_group);
        valueLinLayout = (LinearLayout) convertView
                .findViewById(R.id.valueLinLayout);
        valueLinLayout.setVisibility(View.GONE);

        manualNoLinLayout = (LinearLayout) convertView
                .findViewById(R.id.manualNoLinLayout);
        manualNoLinLayout.setVisibility(View.GONE);

        mListView = (ListView) convertView.findViewById(R.id.listView);
        mListView.setVisibility(View.GONE);

        final LinearLayout contactLinearLayout = (LinearLayout) convertView
                .findViewById(R.id.contactLinearLayout);

        contactLinearLayout.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        contactLinearLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (loginInfo != null) {
                    if (loginInfo.uid != null) {
                        Intent intent = new Intent(getActivity(),
                                SelectContactsActivity.class);
                        intent.putExtra("FROM", FROM_SEND_SMS_MODULE);
                        intent.putExtra("userId", loginInfo.uid);
                        startActivityForResult(intent,
                                SELECT_CONTACTS_REQUEST_CODE);
                    }
                }
            }
        });

        final ImageButton templateButton = (ImageButton) convertView
                .findViewById(R.id.templateButton);

        editText = (EmojiconEditText) convertView.findViewById(R.id.editText);
        editText.setUseSystemDefault(false);
        editText.setText(param1);
        final String text = getActivity().getIntent().getStringExtra(
                "templateText");

        if (text != null) {
            editText.setText(text);
        }

        final TextView charCountTextView = (TextView) convertView
                .findViewById(R.id.charCountTextView);

		/*
         * int maxLength = 2000; InputFilter[] fArray = new InputFilter[1];
		 * fArray[0] = new InputFilter.LengthFilter(maxLength);
		 * tv.setFilters(fArray);
		 */

        final InputFilter[] limit2000Array = new InputFilter[]{new InputFilter.LengthFilter(
                2000)};

        final InputFilter[] limit871Array = new InputFilter[]{new InputFilter.LengthFilter(
                871)};

        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // This sets a textview to the current length
                if (unicodeCheckBox.isChecked()) {
                    charCountTextView.setText(String.valueOf(s.length())
                            + "/871");
                    editText.setFilters(limit871Array);
                } else {
                    charCountTextView.setText(String.valueOf(s.length())
                            + "/2000");
                    editText.setFilters(limit2000Array);
                }
            }

            public void afterTextChanged(Editable s) {

            }
        };

        editText.addTextChangedListener(mTextEditorWatcher);

        templateButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent id = new Intent(getActivity(), TemplateActivity.class);
                id.putExtra("taketemplate", "send");
                startActivityForResult(id, SELECT_TEMPLATE_REQUEST_CODE);

            }
        });

        setTimeButton = (ImageButton) convertView
                .findViewById(R.id.setTimeButton);

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

        mEmojiBtn = (ImageView) convertView.findViewById(R.id.imozebtn);

        mEmojiBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (makeEmojiVisible) {
                    sms19.inapp.msg.constant.Utils.hideKeyBoardMethod(
                            getActivity(), mEmojiBtn);

                    // setEmojiconFragment(false);
                    makeEmojiVisible = false;
                    emojisPager.setVisibility(View.VISIBLE);
                } else {
                    // removeEmoji();
                    makeEmojiVisible = true;
                    emojisPager.setVisibility(View.GONE);
                }

            }
        });

        emojicons = (RelativeLayout) convertView.findViewById(R.id.emojicons);

        final ImageView sendButton = (ImageView) convertView
                .findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                showProgressDialog();

                final String txt_message = editText.getText().toString();

                if (txt_message.trim().equals("")) {
                    removeProgressDialog();
                    Toast.makeText(getActivity(),
                            "Please enter some text to send", Toast.LENGTH_LONG)
                            .show();
                    return;
                } else if (txt_message.trim().length() > 2000) {
                    removeProgressDialog();
                    Toast.makeText(
                            getActivity(),
                            "Message length cannot exceed more than 2000 character",
                            Toast.LENGTH_LONG).show();
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

                final StringBuffer mobileString = new StringBuffer();

                final StringBuffer groupString = new StringBuffer();

                if (contactInfoArrayList != null) {
                    for (ContactInfo contactInfo : contactInfoArrayList) {
                        if (contactInfo.isSelected) {
                            isContactSelected = true;
                            String countryCode = contactInfo.countryCode;
                            if (countryCode.startsWith("+")) {
                                countryCode = countryCode.substring(1,
                                        countryCode.length());
                            }
                            mobileString.append(countryCode
                                    + contactInfo.Contact_Mobile + ",");
                        }
                    }
                }

                String mobiStr = "";

                if (mobileString.length() > 1) {
                    mobiStr = mobileString.substring(0,
                            mobileString.length() - 1);
                }

                // +919990305371,+919768583806,+919950099500,+919928151819,+919013849991,+919414390319,
                // 99953816162,98744911357,98130993707,99928151819,99783859589

                for (int i = 0; i < mSelectedItems.size(); i++) {
                    isGroupSelected = true;
                    GroupDetails groupDetails = groupSmsNameTagList
                            .get(mSelectedItems.get(i));
                    groupString.append(groupDetails.group_name + ",");
                }

                // abc,hi,jjjjjjjj,kkkkkkkk,

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
                Boolean IsScheduled = false;

                if (!timeTextView.getText().equals("")) {
                    final String dateTime = timeTextView.getText().toString();
                    if (dateTime.contains(" ")) {
                        final String arrDateTime[] = dateTime.split(" ");
                        date = arrDateTime[0];
                        time = arrDateTime[1] + " " + arrDateTime[2];
                        IsScheduled = true;
                    }
                } else {
                    datetime = System.currentTimeMillis();
                    final String formattedDatetime = DateHelper
                            .myFormat2(datetime);
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

                final int isUnicode = check();

                sendSMSRequest(sendersIdSpinner.getSelectedItem().toString(), templateID, txt_message, mobiStr, groupStr,
                        date, time, isUnicode, IsScheduled);
            }
        });

        unicodeCheckBox = (CheckBox) convertView
                .findViewById(R.id.unicodeCheckBox);

        unicodeCheckBox
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            charCountTextView.setText(String.valueOf(editText
                                    .getText().length()) + "/871");
                            editText.setFilters(limit871Array);
                        } else {
                            charCountTextView.setText(String.valueOf(editText
                                    .getText().length()) + "/2000");
                            editText.setFilters(limit2000Array);
                        }
                    }
                });
        // setTags();

        initFun();
        setViewPager(convertView);
        return convertView;
    }


    protected void showDialog(int id) {
        // TODO Auto-generated method stub

        if (id == DIALOG_PICK_DATETIME_ID) {

            new DateTimePickerDialog(getActivity(),
                    new DateTimePickerDialog.DateTimeAcceptor() {
                        public void accept(long datetime) {
                            final String formattedDatetime = DateHelper
                                    .myFormat2(datetime);
                            // log("acceptDatetime: got " + formattedDatetime);
                            SendSms.this.datetime = datetime;
                            timeTextView.setText(formattedDatetime);
                        }
                    }, new DateTimePickerDialog.DateTimeDeselector() {

                @Override
                public void deselect() {
                    timeTextView.setText("");
                }
            }, datetime).show();
        }
    }


    private int check() {
        int flag = 0;
        if (unicodeCheckBox.isChecked()) {
            flag = 1;
        } else {
            flag = 0;
        }
        return flag;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return true;
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
            if (resultCode == getActivity().RESULT_OK) {
                editText.setText(data.getStringExtra("templateText"));
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

        } else {
            if (SendSmsContactsSingleton.getInstance().getContactInfos() == null) {

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
                                .getInstance().getContactInfos();

                        for (ContactInfo contactInfo : contactInfos) {
                            if (contactInfo.isSelected) {
                                selectedContactsList.add(contactInfo);

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
                                    mListView.setVisibility(View.GONE);
                                } else {
                                    mListView.setVisibility(View.VISIBLE);
                                }

                                adapter = new SendSMSContactsAdapter(
                                        getActivity(), selectedContactsList);
                                mListView.setAdapter(adapter);
                            }
                        });
                    }
                }).start();
            }
        }
    }

    private void initFun() {
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

    private void sendSMSRequest(final String senderId, final String templateID,
                                final String txt_message, final String mobileString,
                                final String groupString, final String date, final String time,
                                final int unicode, final Boolean isScheduled) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Apiurls.getBasePostURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // sms19.inapp.msg.constant.Utils.printLog(response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        JSONArray array = jsonObject
                                .getJSONArray("SheduledSms");
                        JSONObject jsonObject2 = array.getJSONObject(0);

                        String msg = jsonObject2.getString("Msg");

                        resetValues();

                        Toast.makeText(getActivity(), msg,
                                Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    if (e.getMessage() != null) {
                        Toast.makeText(getActivity(), e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                    e.printStackTrace();
                }

                // sms19.inapp.msg.constant.Utils.printLog2(response);
                removeProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(),
                        Toast.LENGTH_LONG).show();

                removeProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Page", "SheduledSms");
                params.put("TempId", templateID);
                params.put("Mobile", loginInfo.userlogin);
                params.put("Password", loginInfo.password);
                // LoginInfo [id=null, mobile=7840002738, status=null, uid=20,
                // userlogin=+917840002738, balance=null, password=49399,
                // LoginType=null]
                params.put("UserId", loginInfo.uid);
                params.put("SenderId", senderId);
                params.put("recipient", mobileString);
                params.put("msgcontent", txt_message);

                params.put("usermobile", loginInfo.userlogin);
                params.put("GroupName", groupString);
                params.put("RecipientUserID", "");

                params.put("sheduledtime", time);
                params.put("sheduleddate", date);
                params.put("isScheduled", String.valueOf(isScheduled));

                params.put("isUnicode", unicode + "");

                Log.i("Url", "url- " + Apiurls.getBasePostURL());
                Log.i("send parameter", "map - " + params.toString());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void resetValues() {

        totalavailablebalance();

        editText.setText("");

        valueLinLayout.removeAllViews();

        mSelectedItems.clear();

        manualNumArrayList.clear();

        manualNoLinLayout.removeAllViews();

        mListView.setVisibility(View.GONE);

        groupBuilder = null;

        contactsLabelTextView.setText("Select your contacts" + " (" + 0 + ")");

        groupLabelTextView.setText("Select your groups (0)");

        valueLinLayout.setVisibility(View.GONE);

        if (selectedContactsList != null) {
            for (ContactInfo contactInfo : selectedContactsList) {
                contactInfo.isSelected = false;
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    // public void setSmsGroupDetails(ArrayList<GroupDetails> TagList,
    // ArrayList<String> ArrayList) {
    // if (ArrayList != null) {
    //
    // groupNameTagList = TagList;
    // groupsArrayList = ArrayList;
    // // Toast.makeText(getActivity(), groupsArrayList.toString(),
    // // Toast.LENGTH_SHORT).show();
    // System.out.println(" group sms " + this.groupsArrayList.toString());
    // } else {
    // groupNameTagList = new ArrayList<GroupDetails>();
    // groupsArrayList = new ArrayList();
    // }
    // }

    @Override
    public void ChangeFragment(ArrayList<GroupDetails> TagList,
                               ArrayList<String> ArrayList) {
        // TODO Auto-generated method stub

        if (ArrayList != null) {

            groupSmsNameTagList = TagList;
            groupsSmsArrayList = ArrayList;
            // System.out.println(" group sms "
            // + this.groupsSmsArrayList.toString());
        } else {
            groupSmsNameTagList = new ArrayList<GroupDetails>();
            groupsSmsArrayList = new ArrayList();
        }
    }

    @Override
    public void setSendersId(ArrayList<FetchSenderIDs> sendersId) {
        ArrayList<String> typeList = new ArrayList<String>();

        if (sendersId != null) fetchSendersId = sendersId;
        for (FetchSenderIDs getchSenderIDs : sendersId) {
            typeList.add(getchSenderIDs.getSender_Name());
        }

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sendersIdSpinner.setAdapter(typeAdapter);
    }

    @Override
    public void Backpressed() {
        if (emojisPager.getVisibility() == View.VISIBLE)
            emojisPager.setVisibility(View.GONE);
        else
            getActivity().finish();
    }


    private AlertDialog.Builder getGroupDialog() {
        AlertDialog.Builder groupBuilder = new AlertDialog.Builder(
                getActivity());
        // Set the dialog title

        String[] groupArr = new String[groupsSmsArrayList.size()];
        boolean[] boolArr = new boolean[groupsSmsArrayList.size()];
        groupArr = groupsSmsArrayList.toArray(groupArr);

        groupBuilder
                .setTitle("Displaying groups with 1 or more contacts")
                .setCancelable(false)
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

                    textView.setText(manualNumArrayList.get(i).countryCode
                            + manualNumArrayList.get(i).Contact_Mobile);

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
            textView.setText(groupsSmsArrayList.get(mSelectedItems.get(i)));

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

    private void setViewPager(View view) {
        emojisPager = (ViewPager) view.findViewById(R.id.emojicons_viewpager);
        emojisPager.setOnPageChangeListener(this);
        // we handle recents
        EmojiconRecents recents = this;
        mEmojisAdapter = new EmojisPagerAdapter(getChildFragmentManager(),
                Arrays.asList(EmojiconRecentsGridFragment
                                .newInstance1(mUseSystemDefault), EmojiconGridFragment
                                .newInstance1(People.DATA, recents, mUseSystemDefault),

                        EmojiconGridFragment.newInstance1(Nature.DATA, recents,
                                mUseSystemDefault), EmojiconGridFragment.newInstance1(
                                Objects.DATA, recents, mUseSystemDefault),
                        EmojiconGridFragment.newInstance1(Places.DATA, recents,
                                mUseSystemDefault), EmojiconGridFragment
                                .newInstance1(Symbols.DATA, recents,
                                        mUseSystemDefault)));

        emojisPager.setAdapter(mEmojisAdapter);

        PagerTabStrip pagerTabStrip = (PagerTabStrip) view
                .findViewById(R.id.mail_pager_title_strip);
        pagerTabStrip.setTabIndicatorColor(Color.parseColor("#80cbc4"));
        ((ViewPager.LayoutParams) pagerTabStrip.getLayoutParams()).isDecor = true;
        // get last selected page
        mRecentsManager = EmojiconRecentsManager.getInstance(view.getContext());
        int page = mRecentsManager.getRecentPage();

        // last page was recents, check if there are recents to use
        // if none was found, go to page 1
        if (page == 0 && mRecentsManager.size() == 0) {
            page = 1;
        }

        if (page == 0) {
            onPageSelected(page);
        } else {
            emojisPager.setCurrentItem(page, false);
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Utils.printLog("Emoji onDestroy");

    }

    public static void input(EditText editText, Emojicon emojicon) {
        if (editText == null || emojicon == null) {
            return;
        }

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            editText.append(emojicon.getEmoji());
        } else {
            editText.getText().replace(Math.min(start, end),
                    Math.max(start, end), emojicon.getEmoji(), 0,
                    emojicon.getEmoji().length());
        }
    }

    @Override
    public void addRecentEmoji(Context context, Emojicon emojicon) {
        final ViewPager emojisPager = (ViewPager) getView().findViewById(
                R.id.emojicons_viewpager);
        EmojiconRecentsGridFragment fragment = (EmojiconRecentsGridFragment) mEmojisAdapter
                .instantiateItem(emojisPager, 0);
        fragment.addRecentEmoji(context, emojicon);
    }


    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        if (mEmojiTabLastSelectedIndex == i) {
            return;
        }
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                // case 5:
                // if (mEmojiTabLastSelectedIndex >= 0
                // && mEmojiTabLastSelectedIndex < mEmojiTabs.length) {
                // mEmojiTabs[mEmojiTabLastSelectedIndex].setSelected(false);
                // }
                // mEmojiTabs[i].setSelected(true);
                // mEmojiTabLastSelectedIndex = i;
                // mRecentsManager.setRecentPage(i);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    private class EmojisPagerAdapter extends FragmentStatePagerAdapter {
        private List<EmojiconGridFragment> fragments;

        int id[] = {R.drawable.ic_emoji_recent_light,
                R.drawable.ic_emoji_people_light,
                R.drawable.ic_emoji_nature_light,
                R.drawable.ic_emoji_objects_light,
                R.drawable.ic_emoji_places_light,
                R.drawable.ic_emoji_symbols_light,
                R.drawable.sym_keyboard_delete_holo_dark};

        public EmojisPagerAdapter(FragmentManager fm,
                                  List<EmojiconGridFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            SpannableStringBuilder sb = new SpannableStringBuilder(""
                    + (position + 1)); // space added before text for
            // convenience

            Drawable drawable = getActivity().getResources().getDrawable(
                    id[position]);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }

    }

    /**
     * A class, that can be used as a TouchListener on any view (e.g. a Button).
     * It cyclically runs a clickListener, emulating keyboard-like behaviour.
     * First click is fired immediately, next before initialInterval, and
     * subsequent before normalInterval.
     * <p/>
     * <p>
     * Interval is scheduled before the onClick completes, so it has to run
     * fast. If it runs slow, it does not generate skipped onClicks.
     */


    public interface OnEmojiconBackspaceClickedListener {
        void onEmojiconBackspaceClicked(View v);
    }




    @Override
    public void onEmojiconBackspaceClicked(View v) {
        // TODO Auto-generated method stub
        EmojiconsFragment.backspace(editText);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        // TODO Auto-generated method stub
        EmojiconsFragment.input(editText, emojicon);
    }

}

    /*private void setEmojiconFragment(boolean useSystemDefault) {

        try {

            if (emojicons != null) {
                makeEmojiVisible = false;
                emojicons.setVisibility(View.VISIBLE);
                emojicons.removeAllViews();
            }

            // emojiconsFragment =
            // EmojiconsFragment.newInstance(useSystemDefault);
            //
            // getChildFragmentManager().beginTransaction()
            // .add(R.id.emojicons, emojiconsFragment).commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
    }*/

	/*
     * private int check(final String str) { int flag = 0; for (int iLetter = 0;
	 * iLetter < str.length(); iLetter++) { char ch = str.charAt(iLetter); if
	 * (Character.UnicodeBlock.of(ch) != Character.UnicodeBlock.BASIC_LATIN) {
	 *
	 * sms19.inapp.msg.constant.Utils .printLog2("Character.UnicodeBlock.of(" +
	 * ch + ")==" + Character.UnicodeBlock.of(ch) + "");
	 * sms19.inapp.msg.constant.Utils.printLog2(" index== " + iLetter + "");
	 *
	 * flag = 1; break; } } return flag; }
	 */

    /*private void getGroupDetails() {
        // showProgressDialog();
        new webservice(null,
                webservice.GetAllGroupDetails.geturl(loginInfo.uid),
                webservice.TYPE_GET, webservice.TYPE_GET_GROUP_DETAILS,
                new webservice.ServiceHitListener() {

                    @Override
                    public void onSuccess(Object Result, int id) {

                        if (id == webservice.TYPE_GET_GROUP_DETAILS) {

                            FetchGroupDetails Model = (FetchGroupDetails) Result;

                            final List<GroupDetails> list = Model
                                    .getGroupDetails();

                            groupSmsNameTagList.clear();
                            groupsSmsArrayList.clear();

                            // groupNameTagList.addAll(list);

                            final int size = list.size();

                            for (int i = 0; i < size; i++) {

                                final String noOfContacts = list.get(i).NoOfContacts;

                                if (noOfContacts != null) {
                                    if (!noOfContacts.trim().equals("0")) {

                                        groupSmsNameTagList.add(list.get(i));

                                        groupsSmsArrayList.add(list.get(i).group_name
                                                + " ("
                                                + list.get(i).NoOfContacts
                                                + ")");
                                    }
                                }
                            }
                        }
                        // removeProgressDialog();

                    }

                    @Override
                    public void onError(String Error, int id) {

                        // removeProgressDialog();

                        Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT)
                                .show();

                    }
                });
    }

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

    public static class RepeatListener implements View.OnTouchListener {

        private Handler handler = new Handler();

        private int initialInterval;
        private final int normalInterval;
        private final View.OnClickListener clickListener;

        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                if (downView == null) {
                    return;
                }
                handler.removeCallbacksAndMessages(downView);
                handler.postAtTime(this, downView, SystemClock.uptimeMillis()
                        + normalInterval);
                clickListener.onClick(downView);
            }
        };

        private View downView;

        /**
         * @param initialInterval The interval before first click event
         * @param normalInterval  The interval before second and subsequent click events
         * @param clickListener   The OnClickListener, that will be called periodically
         */
 /*   public RepeatListener(int initialInterval, int normalInterval,
                          View.OnClickListener clickListener) {
        if (clickListener == null)
            throw new IllegalArgumentException("null runnable");
        if (initialInterval < 0 || normalInterval < 0)
            throw new IllegalArgumentException("negative interval");

        this.initialInterval = initialInterval;
        this.normalInterval = normalInterval;
        this.clickListener = clickListener;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downView = view;
                handler.removeCallbacks(handlerRunnable);
                handler.postAtTime(handlerRunnable, downView,
                        SystemClock.uptimeMillis() + initialInterval);
                clickListener.onClick(view);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                handler.removeCallbacksAndMessages(downView);
                downView = null;
                return true;
        }
        return false;
    }
}

 public static void backspace(EditText editText) {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0,
                0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }

     public View[] getmEmojiTabs() {
        return mEmojiTabs;
    }

    public void setmEmojiTabs(View[] mEmojiTabs) {
        this.mEmojiTabs = mEmojiTabs;
    }

    /*
	 * @Override public void finish() {
	 *
	 * new Thread(new Runnable() {
	 *
	 * @Override public void run() { // TODO Auto-generated method stub if
	 * (selectedContactsList != null) { for (ContactInfo info :
	 * selectedContactsList) { info.isSelected = false; } } } }).start();
	 *
	 * super.finish(); }
	 */

