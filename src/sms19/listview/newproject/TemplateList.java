package sms19.listview.newproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kitever.android.R;
import com.kitever.sendsms.SendSmsMail;
import com.kitever.sendsms.SendSmsScreen;

import java.util.ArrayList;

import sms19.listview.adapter.TemplatesubAdapter;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.AllTemplate;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;

import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class TemplateList extends ActionBarActivity {
    private ProgressDialog p;

    private ListView templateListViewList;
    private Context context;// = this;

    // global value of string
    private String TemName[];
    private String TemId[];
    private String[] values;

    // Global define of ArrayList
    private ArrayList<String> listData;
    private ArrayList<String> listId;
    private ArrayAdapter<String> temListDATABASE;
    private ArrayAdapter<String> listD;

    private String UserId = "";
    private String temname, teminbox, temgoto, inboxStatus, inboxStatus1, inboxStatus2;
    private String data, dataID;
    public static Activity templateListFlag;
    private DataBaseDetails dbObject = new DataBaseDetails(this);
    private TemplatesubAdapter templatesubAdapter;
    private ArrayList<templateModel> templateList;
    // ImageView imageView11;
    private String temp;
    public static Activity templateHomeListFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_list);
        templateListViewList = (ListView) findViewById(R.id.TemplatelistViewList);
        templateList = new ArrayList<>();
        context = this;

        /*************************** INTERNET ********************************/
        webservice._context = this;

        /*************************** INTERNET ********************************/

        templateHomeListFlag = this;
/*
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
        bar.setTitle(Html
                .fromHtml("<font color='#ffffff'>Select Template</font>"));
        // bar.setHomeAsUpIndicator(R.drawable.arrow_new);
        bar.setDisplayHomeAsUpEnabled(true);*/
        actionBarSettingWithBack(this,getSupportActionBar(),"Select Template");
        Intent i = getIntent();
        try {

            temname = i.getStringExtra("Template");
            temgoto = i.getStringExtra("goto");

        } catch (Exception e) {
            temname = "";
            temgoto = "";
        }

        try {
            inboxStatus = i.getStringExtra("gimp");
        } catch (Exception e2) {
            inboxStatus = "";
        }

        try {
            inboxStatus1 = i.getStringExtra("gimp1");
        } catch (Exception e2) {
            inboxStatus1 = "";
        }

        try {
            inboxStatus2 = i.getStringExtra("gimp2");
        } catch (Exception e2) {
            inboxStatus2 = "";
        }
        try {
            temp = i.getStringExtra("TEMNAME").trim();
            // Log.w("TEMPLATE", "TEMPLATE11111" + temp);
        } catch (Exception e1) {
            // Log.w("TEMPLATE", "TEMPLATE222" + temp);
            temp = "";
        }

        // Log.w("CDF", "CDF ::::1::::(temgoto):" + temgoto);

        // to check if database contains value or not if not then fetch template
        // list from server
        dbObject.Open();
        Cursor c;
        c = dbObject.GETALLTEMPLATE();
        if (c.getCount() <= 0) {
            templatelistserviceinflater();
        }
        dbObject.close();

        try {
            templateList = getALLTemplate(temname);
            templatesubAdapter = (new TemplatesubAdapter(TemplateList.this,
                    R.layout.template_subpage_custom_adapter, templateList, temname, dbObject));
            templateListViewList.setAdapter(templatesubAdapter);

        } catch (Exception er) {
        }

        templateListViewList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                try {
                    data = TemName[position];
                    dataID = TemId[position];
                } catch (Exception re) {

                }

                try {
                    String DBDataBase = fetchDataFromdatabase(position, temname);

                    dataID = DBDataBase.substring(0, DBDataBase.indexOf(","));
                    data = DBDataBase.substring(DBDataBase.indexOf(",") + 1);
                    // String s=dataID;
                    // String s1=data;
                    // String s2="";

                } catch (Exception ed) {

                }

                if (IsTemplateExist()) {
                    dbObject.Open();
                    dbObject.DeleteSELETEDTemplateData();
                    dbObject.close();
                }

                fetchUserId();

                dbObject.Open();
                if (temname.equalsIgnoreCase("Personal")) {

                    dbObject.updateTemp("Personal",
                            TemplatesubAdapter.resultData, data);
                } else {
                    dbObject.addSELETEDTemplate("1", dataID, data);
                }

                dbObject.close();


                CustomTemplate.customRemplate = true;


                try {

                    if (temgoto.equalsIgnoreCase("send")) {


                        Intent returnIntent = new Intent();
                        if (temname.equalsIgnoreCase("Personal")) {
                            returnIntent.putExtra("templateID",
                                    TemplatesubAdapter.resultData);
                        } else {
                            returnIntent.putExtra("templateID", data);
                        }
                        setResult(RESULT_OK, returnIntent);
                        finish();

                    } else if (temgoto.equalsIgnoreCase("group")) {
                        // SendMsgChatgroup.sendgroupmsg=0;
                        Intent i = new Intent(TemplateList.this,
                                SendMsgChatgroup.class);
                        i.putExtra("templateID", data);
                        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(i);

                    } else if (temgoto.equalsIgnoreCase("contact")) {
                        // SendMessageInbox.messageinboxread=0;
                        Intent i = new Intent(TemplateList.this,
                                SendMessageInbox.class);
                        i.putExtra("templateID", data);
                        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(i);

                    } else if (temgoto.equalsIgnoreCase("inboxread")
                            && inboxStatus.length() > 0) {
                        Intent i = new Intent(TemplateList.this,
                                Inboxreadmsg.class);
                        i.putExtra("templateID", data);
                        i.putExtra("Name", inboxStatus);
                        i.putExtra("Number", inboxStatus1);
                        i.putExtra("recipientid", inboxStatus2);
                        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(i);
                    } else {
                        // SendMessageInbox.messageinboxread=0;
                        Intent i = new Intent(TemplateList.this,
                                SendSmsScreen.class);
                        i.putExtra("templateID", data);
                        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    Intent i = new Intent(TemplateList.this,
                            SendSmsMail.class);
                     if (temname.equalsIgnoreCase("Personal")) {
                    /*    // boolean flag = true;
                        dbObject.Open();

                        dbObject.updateTemp("Personal",
                                TemplatesubAdapter.resultData, data);

                        dbObject.close();*/
                        i.putExtra("template",
                                TemplatesubAdapter.resultData);
                    } else {
                        i.putExtra("template", data);
                    }

                    startActivity(i);
                    finish();
                }
            }

        });

    }

    private String fetchDataFromdatabase(int pos, String temTitle) {

        dbObject.Open();
        Cursor c;

        c = dbObject.getTemplate(temTitle);
        String DataNameID = "";
        int i = 0;

        while (c.moveToNext()) {
            if (pos == i) {
                DataNameID = c.getString(1) + "," + c.getString(2);
            }
            i++;
        }

        dbObject.close();
        return DataNameID;
    }

    public void fetchUserId() {
        dbObject.Open();
        Cursor c;
        c = dbObject.getLoginDetails();
        while (c.moveToNext()) {
            UserId = c.getString(3);
        }
        dbObject.close();
    }

    public boolean IsTemplateExist() {
        dbObject.Open();
        Cursor c;
        c = dbObject.getSELETEDTemplates();
        while (c.moveToNext()) {
            return true;
        }

        dbObject.close();
        return false;
    }

    public ArrayList<templateModel> getALLTemplate(String templateTitle) {
        dbObject.Open();
        Cursor c;

        c = dbObject.getTemplate(templateTitle);
        int count = c.getCount();
        String Ttitle[] = new String[count];

        if (c.getCount() >= 1) {
            int i = 0;
            while (c.moveToNext() && i < count) {
                templateList.add(i,new templateModel("",c.getString(1),c.getString(2),""));
//                templateList.get(i).setTemname();
                Ttitle[i] = c.getString(2);
                i++;
            }
        }

        dbObject.close();
        return templateList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.edit_profile, menu);
        return false;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.rfshMenu) {

            templatelistserviceinflater();

        }
        if (id == R.id.transactionMenu) {

            Intent i = new Intent(TemplateList.this, Transaction.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.editProfileMenu) {
            Intent i = new Intent(TemplateList.this, EditProfile.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.schedulemsgrMenu) {
            Intent i = new Intent(TemplateList.this, ScheduleList.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.termsconditionMenu) {
            Intent i = new Intent(TemplateList.this, TermsAndCondition.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.HowtoUseMenu) {
            Intent i = new Intent(TemplateList.this, HowToUse.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Service to get
    public void templatelistserviceinflater() {

        fetchUserId();
        p = ProgressDialog.show(context, null, "Please wait...");
        new webservice(null,
                webservice.GetUserAllTemplateListDetail.geturl(UserId),
                webservice.TYPE_GET, webservice.TYPR_GET_TEMPLATE,
                new ServiceHitListener() {

                    @Override
                    public void onSuccess(Object Result, int id) {

                        try {
                            p.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        AllTemplate mid = (AllTemplate) Result;

                        dbObject.Open();
                        dbObject.DeleteALLTemplateDataALL();
                        dbObject.close();

                        for (int i = 0; i < mid.getUserAllTemplateListDetail()
                                .size(); i++) {
                            String templarename = mid
                                    .getUserAllTemplateListDetail().get(i)
                                    .getTemplate();
                            String TemplateId = mid
                                    .getUserAllTemplateListDetail().get(i)
                                    .getTemplate_id();
                            String template_title = mid
                                    .getUserAllTemplateListDetail().get(i)
                                    .getTemplate_title();
                            String UserId = mid.getUserAllTemplateListDetail()
                                    .get(i).getUser_id();

                            dbObject.Open();
                            dbObject.addtoAlltemplate(UserId, TemplateId,
                                    templarename, template_title);
                            dbObject.close();
                        }

                        try {
                            templateList = getALLTemplate(temname);
                            templatesubAdapter = (new TemplatesubAdapter(
                                    TemplateList.this,
                                    R.layout.template_subpage_custom_adapter,
                                    templateList, temname, dbObject));
                            templateListViewList.setAdapter(templatesubAdapter);

                        } catch (Exception er) {
                        }
                        try {
                            String EmergencyMessage = mid
                                    .getUserAllTemplateListDetail().get(0)
                                    .getEmergencyMessage();
                            try {

                                Emergency.desAct.finish();
                            } catch (Exception e) {
                            }

                            if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
                                Intent rt = new Intent(TemplateList.this,
                                        Emergency.class);
                                rt.putExtra("Emergency", EmergencyMessage);
                                startActivity(rt);
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onError(String Error, int id) {
                        try {
                            p.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public class templateModel {
        private String utemid;
        private String temid;
        private String temname;
        private String temtitle;

        public templateModel(String utemid, String temid, String temname, String temtitle) {
            this.utemid = utemid;
            this.temid = temid;
            this.temname = temname;
            this.temtitle = temtitle;
        }

        public String getTemid() {
            return temid;
        }

        public void setTemid(String temid) {
            this.temid = temid;
        }

        public String getTemtitle() {
            return temtitle;
        }

        public void setTemtitle(String temtitle) {
            this.temtitle = temtitle;
        }

        public String getTemname() {
            return temname;
        }

        public void setTemname(String temname) {
            this.temname = temname;
        }

        public String getUtemid() {
            return utemid;
        }

        public void setUtemid(String utemid) {
            this.utemid = utemid;
        }
    }

    // private void callLogoutMethod() {
    // // TODO Auto-generated method stub
    //
    // new AlertDialog.Builder(this)
    // .setCancelable(false)
    // .setMessage(
    // "Are you Sure you want to Exit? All your chat data will be deleted.")
    // .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    //
    // // delete all database
    // DatabaseCleanState();
    //
    // Toast.makeText(getApplicationContext(),
    // "Logout Successfully", Toast.LENGTH_SHORT)
    // .show();
    //
    // Intent i = new Intent(TemplateList.this, SMS19.class);
    // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
    // | Intent.FLAG_ACTIVITY_NEW_TASK);
    // startActivity(i);
    // finish();
    //
    // }
    //
    // public SQLiteDatabase getDBObject() {
    // return dbObject.db;
    // }
    //
    // private void DatabaseCleanState() {
    //
    // dbObject.Open();
    // dbObject.onUpgrade(getDBObject(), 1, 1);
    // dbObject.close();
    // }
    //
    // })
    // .setNegativeButton("CANCEL",
    // new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog,
    // int which) {
    // dialog.cancel();
    // }
    // }).show();
    //
    // }
}

    /*public boolean checkAllTemplate(String templateTitle) {
        dbObject.Open();

        Cursor c;
        c = dbObject.getTemplate(templateTitle);

        while (c.moveToNext()) {
            dbObject.close();
            return true;
        }

        dbObject.close();

        return false;
    }

     FloatingActionButton fabButton = null;
            if (temname.equalsIgnoreCase("Personal")) {
                fabButton = new FloatingActionButton.Builder(TemplateList.this)
                        .withDrawable(
                                getResources().getDrawable(
                                        R.drawable.ic_add_white_36dp))
                        .withButtonColor(Color.parseColor("#E46C22"))
                        .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                        .withMargins(0, 0, 16, 16).create();

                fabButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (temname.equalsIgnoreCase("Personal")) {
                            final Dialog dialog = new Dialog(TemplateList.this);
                            dialog.setContentView(R.layout.add_template_layout);
                            dialog.setCancelable(false);
                            dialog.setTitle("Add Template");
                            // EditText
                            // tempTitle=(EditText)dialog.findViewById((R.id.temp_title));
                            final EditText tempDetails = (EditText) dialog
                                    .findViewById((R.id.temp_details));
                            final TextView txtCount = (TextView) dialog
                                    .findViewById(R.id.txt_count);
                            Button addBtn = (Button) dialog
                                    .findViewById(R.id.add_btn);
                            Button cancelBtn = (Button) dialog
                                    .findViewById(R.id.cancel_btn);
                            addBtn.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (tempDetails.getText().toString() != null
                                            && !tempDetails.getText()
                                            .toString()
                                            .equalsIgnoreCase("")) {
                                        dbObject.Open();
                                        dbObject.addtoAlltemplate(UserId, data,
                                                tempDetails.getText()
                                                        .toString(), "Personal");
                                        // templatesubAdapter.notifyDataSetChanged();
                                        finish();
                                        Intent intent = new Intent(context,
                                                TemplateList.class);
                                        intent.putExtra("Template", "Personal");
                                        startActivity(intent);

                                    }
                                    dialog.dismiss();
                                }
                            });
                            cancelBtn.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            });
                            tempDetails
                                    .addTextChangedListener(new TextWatcher() {

                                        @Override
                                        public void onTextChanged(
                                                CharSequence s, int start,
                                                int before, int count) {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void beforeTextChanged(
                                                CharSequence s, int start,
                                                int count, int after) {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            // TODO Auto-generated method stub
                                            String totalString = tempDetails
                                                    .getText().toString();
                                            if (totalString != null
                                                    && totalString.length() > 0) {
                                                txtCount.setText(totalString
                                                        .length() + "/2000");
                                            } else {
                                                txtCount.setText("0/2000");
                                            }
                                        }
                                    });
                            dialog.show();
                            // dbObject.addtoAlltemplate(UserId, data,
                            // TemplatesubAdapter.resultData, "Personal");
                        }
                    }
                });
                 }*/

