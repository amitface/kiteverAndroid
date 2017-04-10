package com.kitever.sendsms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.models.ContactInfo;
import com.kitever.models.LoginInfo;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.activity.PosBaseActivity;
import com.kitever.sendsms.fragments.SendMail;
import com.kitever.sendsms.fragments.SendSms;
import com.kitever.sendsms.fragments.SmsMailInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.newproject.model.FetchGroupDetails.GroupDetails;
import sms19.listview.newproject.model.FetchSenderIDs;
import sms19.listview.newproject.model.FetchSenderModel;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class SendSmsMail extends AppCompatActivity implements NetworkManager {

    public static final int GET_SMS_GROUP=1;
    public static final int GET_SENDER_IDS=2;
    public static final int GET_MAIL_GROUP=3;

    public static final String EQU = "=";
    public static final String AND = "&";

    private DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout pagerTabStrip;
    private TextView sorting_title, mUserNameTitle, actionbar_title;
    private DataBaseDetails dbObject;
    private LoginInfo loginInfo;
    public static String tagPackage[] = new String[2];
    private ProgressDialog progressDialog = null;
    private SmsMailInterface interface1 = null;
    private ArrayList<GroupDetails> groupNameTagList = new ArrayList<GroupDetails>();

    private ArrayList<String> groupsArrayList = new ArrayList<String>();

    private ArrayList<FetchSenderIDs> fetchSenderIDses = new ArrayList<FetchSenderIDs>();

    private ArrayList<GroupDetails> groupMailNameTagList = new ArrayList<GroupDetails>();

    private ArrayList<String> groupsMailArrayList = new ArrayList<String>();
    private String template = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms_mail);
        ActionBar bar = getSupportActionBar();

        actionBarSettingWithBack(this,getSupportActionBar(),"Messages");

        if(getIntent().getStringExtra("template")!=null)
            template = getIntent().getStringExtra("template");

        pagerTabStrip = (TabLayout) findViewById(R.id.pagertitle_smsmail);
        pagerTabStrip.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
        pagerTabStrip.setTabTextColors(Color.parseColor(CustomStyle.TAB_FONT_COLOR),Color.parseColor(CustomStyle.TAB_FONT_COLOR));
        pagerTabStrip.setSelectedTabIndicatorColor(Color.parseColor(CustomStyle.TAB_INDICATOR));


        mViewPager = (ViewPager) findViewById(R.id.viewpager_smsmail);

        initFun();
    }



    @Override
    public void onBackPressed() {
        SendSms fragment = (SendSms) mDemoCollectionPagerAdapter.getFragment(0);
        SmsMailInterface smsMail = (SmsMailInterface) fragment;
        if (fragment.isVisible())
            smsMail.Backpressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        private SendSms m1stFragment;
        private SendMail m2ndFragment;

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                case 0:
                    fragment = SendSms.newIntance(loginInfo,template);
                    break;
                case 1:
                    fragment = SendMail.newIntance(loginInfo);
                    break;
            }
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(
                    container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    m1stFragment = (SendSms) createdFragment;
                    break;
                case 1:
                    m2ndFragment = (SendMail) createdFragment;
                    break;
            }
            return createdFragment;
        }

        public Fragment getFragment(int position) {
            if (position == 0)
                return m1stFragment;
            else
                return m2ndFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "Send Sms";
            else
                return "Send Mail";
        }
    }

    public void initFun() {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                dbObject = new DataBaseDetails(SendSmsMail.this);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        fetchLoginDetails();
                        //getSmsGroupDetails();
                        fetchSmsGroupDetails();
                        fetchMailGroupDetails();
                        fetchSenderIds();
                    }
                });

            }
        }).start();
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
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(
                getSupportFragmentManager());
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        pagerTabStrip.setupWithViewPager(mViewPager);
        if (getIntent() != null) {
            int tab = getIntent().getIntExtra("tab", 0);
            mViewPager.setCurrentItem(tab);
        }

        ViewGroup vg = (ViewGroup) pagerTabStrip.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    TextView textViewToConvert = (TextView) tabViewChild;
                    textViewToConvert.setAllCaps(false);
                    setRobotoThinFont(textViewToConvert,this);
                    textViewToConvert.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
                }
            }
        }


        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                // if (position == 1) {
                // SendMail fragment = (SendMail) mDemoCollectionPagerAdapter
                // .getItem(1);
                //
                // if (fragment != null)
                // fragment.setGroupDetails();
                // else
                // Toast.makeText(SendSmsMail.this, "else ",
                // Toast.LENGTH_SHORT).show();
                // }
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
            }
        });

    }


    private void fetchSmsGroupDetails() {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(this)) {
            try {
                showProgressDialog();
                String Urldata= "GetGroup" + AND + "userid" + EQU + loginInfo.uid ;;
                new RequestManager().sendGetRequest(this,
                        GET_SMS_GROUP, Urldata);
            } catch (Exception e) {

            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    private void fetchMailGroupDetails() {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(this)) {
            try {
                showProgressDialog();
                String Urldata= "GetGroup" + AND + "userid" + EQU + loginInfo.uid+"&type=mail" ;
                new RequestManager().sendGetRequest(this,
                        GET_MAIL_GROUP, Urldata);
            } catch (Exception e) {

            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    private void fetchSenderIds() {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(this)) {
            try {
                showProgressDialog();
                String Urldata= "FetchSenderIDDetail" + AND + "userid" + EQU + loginInfo.uid;
                new RequestManager().sendGetRequest(this,
                        GET_SENDER_IDS, Urldata);
            } catch (Exception e) {

            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    @Override
    public void onReceiveResponse(int requestId, String response) {

        removeProgressDialog();
        if (response != null && response.length() > 0) {
            if (requestId == GET_SMS_GROUP) {
                Gson gson = new Gson();
                FetchGroupDetails Model = gson.fromJson(response, FetchGroupDetails.class);
                final List<GroupDetails> list = Model
                        .getGroupDetails();
                groupNameTagList.clear();
                groupsArrayList.clear();
                System.out.println("Sms group " + list.toString());
                final int size = list.size();
                for (int i = 0; i < size; i++) {
                    final String noOfContacts = list.get(i).NoOfContacts;
                    if (noOfContacts != null) {
                        if (!noOfContacts.trim().equals("0")) {

                            groupNameTagList.add(list.get(i));

                            groupsArrayList.add(list.get(i).group_name
                                    + " ("
                                    + list.get(i).NoOfContacts
                                    + ")");
                        }
                    }
                }

                // removeProgressDialog();
                SendSms fragment = (SendSms) mDemoCollectionPagerAdapter
                        .getFragment(0);
                // .getItem(0);

                if (fragment != null) {
                    // fragment.setSmsGroupDetails(groupNameTagList,groupsArrayList);
                    interface1 = fragment;
                    interface1.ChangeFragment(groupNameTagList,
                            groupsArrayList);
                }


            } else if (requestId == GET_SENDER_IDS) {

                    Gson gson = new Gson();
                    FetchSenderModel Model = gson.fromJson(response, FetchSenderModel.class);
                    final ArrayList<FetchSenderIDs> list = Model
                            .getFetchSenderIDs();
                    fetchSenderIDses = list;

                // removeProgressDialog();
                SendSms fragment = (SendSms) mDemoCollectionPagerAdapter
                        .getFragment(0);
                // .getItem(0);

                if (fragment != null) {
                    // fragment.setSmsGroupDetails(groupNameTagList,groupsArrayList);
                    interface1 = fragment;
                    interface1.setSendersId(fetchSenderIDses);
                }


            } else if(requestId == GET_MAIL_GROUP){

                Gson gson = new Gson();
                FetchGroupDetails Model = gson.fromJson(response, FetchGroupDetails.class);

                   // FetchGroupDetails Model = (FetchGroupDetails) Result;

                    final List<GroupDetails> list = Model
                            .getGroupDetails();
                    System.out.println("Mail group " + list.toString());
                    groupMailNameTagList.clear();
                    groupsMailArrayList.clear();

                    // groupNameTagList.addAll(list);

                    final int size = list.size();

                    for (int i = 0; i < size; i++) {

                        final String noOfContacts = list.get(i).NoOfContacts;

                        if (noOfContacts != null) {
                            if (!noOfContacts.trim().equals("0")) {

                                groupMailNameTagList.add(list.get(i));

                                groupsMailArrayList.add(list.get(i).group_name
                                        + " ("
                                        + list.get(i).NoOfContacts
                                        + ")");
                            }
                        }
                    }

                removeProgressDialog();
                SendMail fragmentMail = (SendMail) mDemoCollectionPagerAdapter
                        .getFragment(1);

                if (fragmentMail != null) {
                    interface1 = fragmentMail;
                    interface1.ChangeFragment(groupMailNameTagList,
                            groupsMailArrayList);
                }

            }
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        removeProgressDialog();
        showMessage("Server Error !!!!");
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
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

    public void showMessage(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                this);
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        SendSmsContactsSingleton des = SendSmsContactsSingleton.getInstance();
        des.setContactInfos(new ArrayList<ContactInfo>());
        des.setEmailInfos(new ArrayList<ContactInfo>());
        des = null;
    }
}
