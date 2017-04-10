package sms19.listview.newproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.sendsms.SendSmsScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import sms19.listview.adapter.CustomDbGroupadapter;
import sms19.listview.adapter.GroupAdapter;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.newproject.model.FetchGroupDetails.GroupDetails;
import sms19.listview.newproject.model.contactmodel;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;

public class GroupNewAdd extends Activity implements OnClickListener {

    String Mobile = "", Password = "", UserId = "";
    CheckBox selectagp;
    EditText search;
    TextView sendmsg, gotocontacts;
    ImageView serch;
    ListView gplist;
    public static boolean checkAll = false;
    boolean checkStatus = true;
    List<contactmodel> setdbdataInAdapter;
    List<contactmodel> setdbdataInAdapterNew;
    GroupAdapter aAdpt;
    String gpContact[], a, b, groupContactNew[];
    ImageView refresh;
    int counting, countinggp;
    CustomDbGroupadapter gpadaptaer;
    ProgressDialog contact, group;
    public static Activity contactsGroupFlag;
    DataBaseDetails dbObject = new DataBaseDetails(this);
    ImageView backn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupxmlnew);

        serch = (ImageView) findViewById(R.id.imageSCgp);
        selectagp = (CheckBox) findViewById(R.id.selectagp);
        sendmsg = (TextView) findViewById(R.id.gpsendmsg);
        gplist = (ListView) findViewById(R.id.gplist);
        gotocontacts = (TextView) findViewById(R.id.gotocontacts);
        search = (EditText) findViewById(R.id.searchgptext);
        refresh = (ImageView) findViewById(R.id.grouprefesh);
        backn = (ImageView) findViewById(R.id.backn);
        /***************************INTERNET********************************/
        webservice._context = this;

        contactsGroupFlag = this;
        /***************************INTERNET********************************/


        gplist.setTextFilterEnabled(true);

        //Check if group database is empty if empty fetch group from server
        /*dbObject.Open();
        Cursor c;
	    c=dbObject.getGroupALL();
	    if(c.getCount()<=0)
	    {
	    //InflateGroupfromserver();	
	    }
	    dbObject.close();*/

        callGroupAdapterwithDatabase();

        selectagp.setOnClickListener(this);
        sendmsg.setOnClickListener(this);
        gotocontacts.setOnClickListener(this);
        serch.setOnClickListener(this);
        refresh.setOnClickListener(this);
        backn.setOnClickListener(this);
        FilterGroupSearch();

        gplist.setAdapter(gpadaptaer = new CustomDbGroupadapter(this, R.layout.customlistofgroups, setdbdataInAdapter));

        Intent i = getIntent();
        try {
            String sget = i.getStringExtra("refreshgp");
            if (sget.equalsIgnoreCase("sucessfuyllyadd")) {

                fetchMobileandUserId();

                group = ProgressDialog.show(this, null, "Loading Group Details");
                new webservice(null, webservice.GetAllGroupDetails.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_GET_GROUP_DETAILS, new ServiceHitListener() {

                    @Override
                    public void onSuccess(Object Result, int id) {
                        try {
                            group.dismiss();
                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                        FetchGroupDetails Model = (FetchGroupDetails) Result;

                        try {
                            String EmergencyMessage = Model.getGroupDetails().get(0).getEmergencyMessage();

                            try {
                                Emergency.desAct.finish();
                            } catch (Exception e) {
                            }

                            if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
                                Intent rt = new Intent(GroupNewAdd.this, Emergency.class);
                                rt.putExtra("Emergency", EmergencyMessage);
                                startActivity(rt);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        List<GroupDetails> list = Model.getGroupDetails();

                        dbObject.Open();
                        dbObject.DeleteGroupDataALL();
                        dbObject.close();

                        for (int i = 0; i < Model.getGroupDetails().size(); i++) {
                            fetchMobileandUserId();
                            String gpname = Model.getGroupDetails().get(i).getGroup_name();
                            String gpCount = Model.getGroupDetails().get(i).getNoOfContacts();

                            dbObject.Open();
                            dbObject.addGroupAll(UserId, gpname, gpCount);
                            dbObject.close();
                        }

                        gplist.setAdapter(new GroupAdapter(list, contactsGroupFlag));
                    }

                    @Override
                    public void onError(String Error, int id) {
                        try {
                            group.dismiss();
                        } catch (Exception e) {


                        }
                        Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();

                    }
                });


            }
        } catch (Exception e) {
        }


    }


    private void FilterGroupSearch() {
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence sg, int start, int before, int count) {

                if (count < before) {

                    try {
                        gpadaptaer.resetGroupData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        aAdpt.resetData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                try {
                    gpadaptaer.getFilter().filter(sg.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    aAdpt.getFilter().filter(sg.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void callGroupAdapterwithDatabase() {
        try {
            /*************************************Group************************************/

            //set groups name in adapter from database
            gpContact = groupContacts();
            groupContactNew = groupContactsCount();

            ArrayList<String> contactGrouplist = new ArrayList<String>(Arrays.asList(gpContact));
            ArrayList<String> contactGrouplistNew = new ArrayList<String>(Arrays.asList(groupContactNew));

            setdbdataInAdapter = new ArrayList<contactmodel>();

            Iterator<String> itr = contactGrouplist.iterator();
            Iterator<String> itrq = contactGrouplistNew.iterator();

            while (itr.hasNext()) {
                a = itr.next();
                b = itrq.next();
                setdbdataInAdapter.add(new contactmodel(a, b));
            }
            gplist.setAdapter(gpadaptaer = new CustomDbGroupadapter(this, R.layout.customlistofgroups, setdbdataInAdapter));
        } catch (Exception e) {
            //Toast.makeText(this, "inside group"+e+""+gpContact, Toast.LENGTH_LONG).show();

        }
    }

    public String[] groupContacts() {
        dbObject.Open();
        Cursor c;

        c = dbObject.getGroupALL();

        countinggp = c.getCount();

        String icontact[] = new String[countinggp];

        if (c.getCount() >= 1) {

            int i = 0;
            while (c.moveToNext() && i < countinggp) {

                //Log.w("TAG","VALUES"+c.getString(0)+"::"+c.getString(1));
                icontact[i] = c.getString(1);
                //Log.w("TAG","VALUES"+icontact[i]+"::i="+i);

                i++;
            }
        }

        dbObject.close();

        return icontact;

    }

    public String[] groupContactsCount() {
        dbObject.Open();
        Cursor c;

        c = dbObject.getGroupALL();

        countinggp = c.getCount();

        String icontact[] = new String[countinggp];

        if (c.getCount() >= 1) {

            int i = 0;
            while (c.moveToNext() && i < countinggp) {

                //Log.w("TAG","VALUES"+c.getString(0)+"::"+c.getString(1)+"::"+c.getString(2));
                icontact[i] = c.getString(2);
                //Log.w("TAG","VALUES"+icontact[i]+"::i="+i);

                i++;
            }
        }

        dbObject.close();

        return icontact;

    }

    public void fetchMobileandUserId() {

        dbObject.Open();

        Cursor c;

        c = dbObject.getLoginDetails();
        int count = c.getCount();

        if (count >= 1) {
            while (c.moveToNext()) {

                Mobile = c.getString(1);
                UserId = c.getString(3);
                Password = c.getString(5);

            }
        }
        dbObject.close();
    }

    @Override
    public void onBackPressed() {

        try {
            dbObject.Open();
            dbObject.deleteselectedReceipent();

            dbObject.close();
        } catch (Exception e) {

            e.printStackTrace();
        }

        finish();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.gpsendmsg:

                try {
                    SendSmsScreen.sendSmsFlag.finish();
                } catch (Exception wd) {

                }


                Intent isend = new Intent(GroupNewAdd.this, SendSmsScreen.class);
                startActivity(isend);
                finish();
                break;
            case R.id.gotocontacts:

                Intent send = new Intent(GroupNewAdd.this, NewContact.class);
                startActivity(send);
                finish();

                break;
            case R.id.backn: {
                Intent i = new Intent(GroupNewAdd.this, Home.class);
                startActivity(i);
                finish();
            }
            break;
            case R.id.imageSCgp:

                search.setFocusableInTouchMode(true);
                search.setFocusable(true);
                search.setCursorVisible(true);
                search.setBackgroundResource(R.drawable.background_round_white);

                break;
            case R.id.grouprefesh: {


                InflateGroupfromserver();
            }
            break;
        }

    }

    /**
     *
     */
    public void InflateGroupfromserver() {
        group = ProgressDialog.show(this, null, "Loading...");
        fetchMobileandUserId();

        new webservice(null, webservice.GetAllGroupDetails.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_GET_GROUP_DETAILS, new ServiceHitListener() {

            @Override
            public void onSuccess(Object Result, int id) {
                try {
                    group.dismiss();
                } catch (Exception e) {
                }

                FetchGroupDetails Model = (FetchGroupDetails) Result;

                try {
                    String EmergencyMessage = Model.getGroupDetails().get(0).getEmergencyMessage();

                    try {
                        Emergency.desAct.finish();
                    } catch (Exception e) {
                    }

                    if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
                        Intent rt = new Intent(GroupNewAdd.this, Emergency.class);
                        rt.putExtra("Emergency", EmergencyMessage);
                        startActivity(rt);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                List<GroupDetails> list = Model.getGroupDetails();

                dbObject.Open();
                dbObject.DeleteGroupDataALL();
                dbObject.close();

                for (int i = 0; i < Model.getGroupDetails().size(); i++) {
                    fetchMobileandUserId();
                    String gpname = Model.getGroupDetails().get(i).getGroup_name();
                    String gpCount = Model.getGroupDetails().get(i).getNoOfContacts();

                    dbObject.Open();
                    dbObject.addGroupAll(UserId, gpname, gpCount);
                    dbObject.close();
                }

                gplist.setAdapter(new GroupAdapter(list, contactsGroupFlag));
            }

            @Override
            public void onError(String Error, int id) {
                try {
                    group.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    protected void onStart() {

        super.onStart();
        callGroupAdapterwithDatabase();

    }
}
