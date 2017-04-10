package com.kitever.sendsms;

import java.util.ArrayList;

import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.EditProfile;
import sms19.listview.newproject.Emergency;
import sms19.listview.newproject.Friendsinvite;
import sms19.listview.newproject.HowToUse;

import com.kitever.android.R;
import com.kitever.customviews.FloatingActionButton;

import sms19.listview.newproject.ScheduleList;
import sms19.listview.newproject.TemplateList;
import sms19.listview.newproject.Templaten_new;
import sms19.listview.newproject.TermsAndCondition;
import sms19.listview.newproject.Transaction;
import sms19.listview.newproject.model.BindTemplateModel;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class TemplateActivity extends ActionBarActivity {

    GridView imagetemplate;
    Button btnCustomGrid;

    ArrayList<String> listData;
    String newe[];
    ArrayAdapter<String> temList;
    ArrayAdapter<String> temListDATABASE;
    ArrayAdapter<String> templateicon;
    String ListData[];
    String[] values;
    ProgressDialog p;
    String data, data1, UserId;
    DataBaseDetails dbObject = new DataBaseDetails(this);
    public static Activity templateHomeFlag;
    Context gContext;
    String navigate, important, important1, important2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_template_screen);

        imagetemplate = (GridView) findViewById(R.id.gridTemplate);
        btnCustomGrid = (Button) findViewById(R.id.btnCustomGrid);

        btnCustomGrid.setVisibility(View.GONE);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>Template</font>"));
        bar.setDisplayHomeAsUpEnabled(true);

        templateHomeFlag = this;
        gContext = this;
        webservice._context = this;

        Intent i2 = getIntent();
        try {
            navigate = i2.getStringExtra("taketemplate");
        } catch (Exception e2) {
            navigate = "";
        }

        try {
            important = i2.getStringExtra("important");
        } catch (Exception e2) {
            important = "";
        }

        try {
            important1 = i2.getStringExtra("important1");
        } catch (Exception e2) {
            important1 = "";
        }

        try {
            important2 = i2.getStringExtra("important2");
        } catch (Exception e2) {
            important2 = "";
        }

//		Log.w("CDF", "CDF ::::1::::(navigate):" + navigate);

        // to check if database contains value or not if not then fetch template
        // from server
        dbObject.Open();
        Cursor c;
        c = dbObject.getTemplates();
        int count = c.getCount();
        if (count <= 0) {
            calltemplateserviceinflater();
        }
        dbObject.close();

	/*	btnCustomGrid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(gContext, CustomTemplate.class);
				startActivity(i);
				finish();
			}
		});*/

        try {
            values = getTemplateTitle();
            imagetemplate.setAdapter(new Templaten_new(this,
                    R.layout.activity_template_list, values, navigate,
                    important, important1, important2));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        FloatingActionButton fabButton = null;
        fabButton = new FloatingActionButton.Builder(TemplateActivity.this)
                .withDrawable(
                        getResources().getDrawable(
                                R.drawable.ic_add_white_36dp))
                .withButtonColor(Color.parseColor("#E46C22"))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16).create();

        fabButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                final Dialog dialog = new Dialog(TemplateActivity.this);
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
                addBtn.setOnClickListener(new View.OnClickListener() {

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

                        }
                        dialog.dismiss();
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {

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
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 202) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Intent returnIntent = new Intent();
                returnIntent.putExtra("templateText", data.getStringExtra("templateID"));
                setResult(RESULT_OK, returnIntent);
                finish();

            }
        }
    }

    private void fetchUserId() {

        dbObject.Open();

        Cursor c;

        c = dbObject.getLoginDetails();

        while (c.moveToNext()) {

            UserId = c.getString(3);

        }

        dbObject.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.edit_profile, menu);
        return false;
    }

/*	  @Override
      public boolean onOptionsItemSelected(MenuItem item) {
              switch (item.getItemId()) {
              case android.R.id.home:
                     finish();
                 break;
              }
              return true;
      }*/

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.rfshMenu) {
            calltemplateserviceinflater();
        }

        if (id == R.id.transactionMenu) {
            Intent i = new Intent(gContext, Transaction.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.editProfileMenu) {
            Intent i = new Intent(gContext, EditProfile.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.schedulemsgrMenu) {
            Intent i = new Intent(gContext, ScheduleList.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.termsconditionMenu) {
            Intent i = new Intent(gContext, TermsAndCondition.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.HowtoUseMenu) {
            Intent i = new Intent(gContext, HowToUse.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.referMenu) {
            Intent i = new Intent(gContext, Friendsinvite.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    public void calltemplateserviceinflater() {
        fetchUserId();

        p = ProgressDialog.show(gContext, null, "Please wait...");
        new webservice(null, webservice.GetUserTemplateList.geturl(UserId),
                webservice.TYPE_GET, webservice.TYPE_TEMPLATE,
                new ServiceHitListener() {

                    @Override
                    public void onSuccess(Object Result, int id) {

                        try {
                            p.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        BindTemplateModel mod = (BindTemplateModel) Result;

                        listData = new ArrayList<String>();

                        int len = mod.getListSize();

                        if (haveTemplate()) {
                            dbObject.Open();
                            dbObject.DeleteTemplateData();
                            dbObject.close();

                        }

                        for (int i = 0; i < len; i++) {

                            String a = mod.getUserTemplateList().get(i)
                                    .getTemplate_Title();
                            listData.add(a);
                            ListData = new String[listData.size()];
                            ListData = listData.toArray(ListData);

                            imagetemplate.setAdapter(new Templaten_new(
                                    TemplateActivity.this, R.layout.activity_template_list,
                                    ListData, navigate, important, important1,
                                    important2));

                            fetchUserId();

                            dbObject.Open();
                            dbObject.addTemplate(UserId, a);
                            dbObject.close();
                            try {
                                String EmergencyMessage = mod
                                        .getUserTemplateList().get(0)
                                        .getEmergencyMessage();
                                try {

                                    Emergency.desAct.finish();
                                } catch (Exception e) {
                                }

                                if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
                                    Intent rt = new Intent(
                                            TemplateActivity.this,
                                            Emergency.class);
                                    rt.putExtra("Emergency", EmergencyMessage);
                                    startActivity(rt);

                                }
                            } catch (Exception e) {

                            }
                        }
                    }

                    @Override
                    public void onError(String Error, int id) {
                        try {
                            p.dismiss();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Toast.makeText(gContext, Error, Toast.LENGTH_SHORT)
                                .show();

                    }
                });
    }

    private boolean haveTemplate() {

        dbObject.Open();
        Cursor c;
        c = dbObject.getTemplates();
        while (c.moveToNext() == true) {
            return true;
        }

        dbObject.close();
        return false;
    }

    public String[] getTemplateTitle() {
        dbObject.Open();
        Cursor c;

        c = dbObject.getTemplates();
        int count = c.getCount();
        String Ttitle[] = new String[count];

        if (c.getCount() >= 1) {
            int i = 0;
            while (c.moveToNext() && i < count) {
                Ttitle[i] = c.getString(1);
                i++;
            }
        }
        dbObject.close();
        return Ttitle;
    }
}
