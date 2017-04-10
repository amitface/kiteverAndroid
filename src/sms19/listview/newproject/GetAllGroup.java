package sms19.listview.newproject;

import java.util.List;

import sms19.listview.adapter.ContactAdaptorForGroup;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.GroupContactDetailModel;
import sms19.listview.newproject.model.contactmodelIndividule;
import sms19.listview.newproject.model.GroupContactDetailModel.getAllNameAndNumber;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;

import com.kitever.android.R;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class GetAllGroup extends ActionBarActivity {
    public static String gpname;
    ListView alldata;
    String Mobile;
    private String UserId = "";
    public static boolean fromgroup = false;
    ImageButton buttonadd;
    List<contactmodelIndividule> setdbdataInAdapterInd;
    DataBaseDetails dbObject = new DataBaseDetails(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupsuncontact);

        alldata = (ListView) findViewById(R.id.listViewgroupall);
        buttonadd = (ImageButton) findViewById(R.id.buttonadd);

        Intent o = getIntent();
        String fetchnamegroupcont = o.getStringExtra("groupname");
        gpname = fetchnamegroupcont;

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0867A3")));
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>" + fetchnamegroupcont + "</font>"));


        buttonadd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    GroupNewAdd.contactsGroupFlag.finish();
                    //	NewContact.contactsGroupFlag.finish();

                } catch (Exception ee) {
                }


                Intent i = new Intent(GetAllGroup.this, NewContact.class);
                i.putExtra("groupname", gpname);
                startActivity(i);
                finish();
            }
        });

        fetchMobileandUserId();

        new webservice(null, webservice.GetGroupContact.geturl(UserId, fetchnamegroupcont), webservice.TYPE_GET, webservice.TYPE_GET_GROUP_CONTACT, new ServiceHitListener() {

            @Override
            public void onSuccess(Object Result, int id) {
                fromgroup = true;

                GroupContactDetailModel model = (GroupContactDetailModel) Result;
                List<getAllNameAndNumber> list = model.getGroupContactDetail();

                dbObject.Open();
                dbObject.deleteAllvalueEditGroup();
                dbObject.close();

//			Log.w("values","---------------welcome from group_ out of fxn");

//			for (int i=0;i<model.getGroupContactDetail().size();i++)
//		    {
////				Log.w("values","----------------welcome from group_ in of fxn");
//				String ReceipentNum  = model.getGroupContactDetail().get(i).getContact_Mobile();
//				String NameRecipient = model.getGroupContactDetail().get(i).getContact_Name();
////				Log.w("values","------------------ReceipentNum"+ReceipentNum+"NameRecipient"+NameRecipient);
//		   }


                alldata.setAdapter(new ContactAdaptorForGroup(getApplicationContext(), R.layout.custom_list_item_contact, list));

                try {
                    String EmergencyMessage = model.getGroupContactDetail().get(0).getEmergencyMessage();

                    try {
                        Emergency.desAct.finish();
                    } catch (Exception e) {
                    }

                    if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
                        Intent rt = new Intent(GetAllGroup.this, Emergency.class);
                        rt.putExtra("Emergency", EmergencyMessage);
                        startActivity(rt);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String Error, int id) {
                Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();


            }
        });

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


            }
        }
        dbObject.close();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
