package sms19.listview.newproject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.AllTemplate;
import sms19.listview.newproject.model.BindTemplateModel;
import sms19.listview.newproject.model.FetchAllContact;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.newproject.model.GetProfileData;
import sms19.listview.newproject.model.TemplateListByName;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitever.android.R;

public class Service_Loading_Page extends ActionBarActivity implements ServiceHitListener {

    ImageView sms, imageAnimation;
    private AnimationDrawable frameAnimation;

    ProgressDialog P;

    TextView textPleaseWait;

    String Mobile, UserId;
    DataBaseDetails db = new DataBaseDetails(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__loading__page);

        textPleaseWait = (TextView) findViewById(R.id.textPleaseWait);
        sms = (ImageView) findViewById(R.id.sms);
        imageAnimation = (ImageView) findViewById(R.id.imageAnimation);
        Utils.printLog(" 10 Start time_logger" + new SimpleDateFormat("hh:mm:ss").format(new Date()) + "");

        //final SharedPreferences chatPrefs = getSharedPreferences("chatPrefs", MODE_PRIVATE);
        /*runOnUiThread(new Runnable() {
			public void run() {
				//ContactUtil.getDeviceContact(getApplicationContext(), chatPrefs);
			}
		});*/

        //changes regards action bar color

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>Loading...</font>"));

        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("KiteverImageDir", Context.MODE_PRIVATE);
            File f = new File(directory.getPath(), "merchantprofile.jpg");

//			String imagePath = Environment.getExternalStorageDirectory().toString() + "/Kitever/Media/Kitever MerchantPic/MerchantKitever.jpg";
//			File f = new File(imagePath);

            if (f.exists()) {
                sms.setImageDrawable(Drawable.createFromPath(f.getPath()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Animation for image rotate
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.newd);
        sms.startAnimation(animation);

        //Animation for text view
        Animation annimate = AnimationUtils.loadAnimation(this, R.anim.textblinking);
        textPleaseWait.startAnimation(annimate);

        /********************Animation************************/


        /***************************INTERNET********************************/
        webservice._context = this;

        /***************************INTERNET********************************/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (hasAnydata()) {
					/*if (Utils.isDeviceOnline(Service_Loading_Page.this)) {
						String userId=Utils.getUserId(Service_Loading_Page.this);
						SharedPreferences chatPrefs = getSharedPreferences("chatPrefs", MODE_PRIVATE);
						//new GetContactListAsyncTask(chatPrefs, userId).execute();
						
						if(!GlobalData.ContactStringToSend.equalsIgnoreCase("")){
							if(GlobalData.getContactListAsyncTask==null){
								GlobalData.getContactListAsyncTask=	new GetContactListAsyncTask(chatPrefs, userId)(GetContactListAsyncTask) new GetContactListAsyncTask(chatPrefs, userId).execute();
								GlobalData.getContactListAsyncTask.execute();
							}else{
								GlobalData.getContactListAsyncTask.cancel(true);
								GlobalData.getContactListAsyncTask=null;
								GlobalData.getContactListAsyncTask=	new GetContactListAsyncTask(chatPrefs, userId)(GetContactListAsyncTask) new GetContactListAsyncTask(chatPrefs, userId).execute();
								GlobalData.getContactListAsyncTask.execute();
							}
							}
					}*/
                    Utils.printLog(" 11 Start time_logger" + new SimpleDateFormat("hh:mm:ss").format(new Date()) + "");
                    Intent i1 = new Intent(Service_Loading_Page.this, Home.class);
                    startActivity(i1);
                    finish();

                } else {

                    //Intent i1=new Intent(Service_Loading_Page.this,MainActivity.class);
                    //startActivity(i1);
                    //finish();
                }


            }
        }, 8000);//12000


        /********************************************IMPLMENTED CODE***********************************/

        /********************************************IMPLMENTED CODE***********************************/

        fetchMobileandUserId();
        new webservice(null, webservice.GetAllGroupDetails.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_GET_GROUP_DETAILS, this);

        fetchMobileandUserId();
        new webservice(null, webservice.GetAllContact.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_GET_ALL_CONTACT, this);

        fetchMobileandUserId();
        new webservice(null, webservice.GetUserTemplateList.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_TEMPLATE, this);
//
        fetchMobileandUserId();
        new webservice(null, webservice.GetUserAllTemplateListDetail.geturl(UserId), webservice.TYPE_GET, webservice.TYPR_GET_TEMPLATE, this);
        fetchMobileandUserId();
        new webservice(null, webservice.GetUserProfileDetail.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_GETPROFILE, this);
    }


    /********************************************
     * IMPLMENTED CODE
     ***********************************/
	/*	



/********************************************IMPLMENTED CODE***********************************/
    @Override
    public void onSuccess(Object Result, int id) {

        //Log.w("TAG", "SUCCESS");
        if (id == webservice.TYPE_GET_ALL_CONTACT) {
            //ALL CONTACT DETAILS
            //Log.w("TAG 1", "TYPE_GET_ALL_CONTACT");

            FetchAllContact model = (FetchAllContact) Result;

            try {
                db.Open();
                db.DeleteContactDataALL();
                db.close();

                fetchMobileandUserId();
                for (int i = 0; i < model.getContactDetails().size(); i++) {
                    //Log.w("TAG", "TYPE_GET_ALL_CONTACT");

                    //Log.w("TAG", "AFTER FETCH USERID"+UserId);

                    String idContact = model.getContactDetails().get(i).getContact_id();
                    String ContactName = model.getContactDetails().get(i).getContact_name();
                    String ContactMobile = model.getContactDetails().get(i).getContact_mobile();

                    ////Log.w("TAG", "AFTER FETCH BY MODEL:"+idContact+"::"+ContactName+"::"+ContactMobile);

                    db.Open();
                    db.addContactAll(UserId, idContact, ContactName, ContactMobile, "0", "", "");
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (id == webservice.TYPE_GET_GROUP_DETAILS) {
            //Log.w("TAG 2", "TYPE_GET_GROUP_DETAILS");
            FetchGroupDetails model = (FetchGroupDetails) Result;
            fetchMobileandUserId();

            // DeleteGroupDataALL

            db.Open();
            db.DeleteGroupDataALL();
            db.close();

            for (int j = 0; j < model.getGroupDetails().size(); j++) {
                String nameGroup = model.getGroupDetails().get(j).getGroup_name();
                String gpCount = model.getGroupDetails().get(j).getNoOfContacts();

                db.Open();
                db.addGroupAll(UserId, nameGroup, gpCount);
                db.close();
            }

        }

        if (id == webservice.TYPE_TEMPLATE) {
            //Log.w("TAG 3", "TYPE_TEMPLATE");
            BindTemplateModel mod = (BindTemplateModel) Result;

            if (haveTemplate()) {
                db.Open();
                db.DeleteTemplateData();
                db.close();

            }

            for (int i = 0; i < mod.getUserTemplateList().size(); i++) {

                String tempname = mod.getUserTemplateList().get(i).getTemplate_Title();

                fetchMobileandUserId();

                db.Open();
                db.addTemplate(UserId, tempname);
                db.close();
            }


        }

        if (id == webservice.TYPE_GET_ALL_TEMPLATE) {
            //ALL TEMPLATE DETAILS

            //Log.w("TAG 4", "TYPE_GET_ALL_TEMPLATE");

            TemplateListByName model = (TemplateListByName) Result;

            db.Open();
            db.DeleteTemplateDataALL();
            db.close();

            for (int i = 0; i < model.getUserTemplate().size(); i++) {

                //Log.w("TAG", "SIZE::TYPE_ALL_TEMPLATE");

                String TemplateTitle = model.getUserTemplate().get(i).getTemplate_Title();
                String TemplateId = model.getUserTemplate().get(i).getTemplate_ID();
                String Template = model.getUserTemplate().get(i).getTemplate();
                String UserId = model.getUserTemplate().get(i).getUser_id();

                db.Open();
                db.addTemplateAll(UserId, TemplateId, TemplateTitle, Template);
                db.close();

            }
        }
        if (id == webservice.TYPE_GETPROFILE) {
            fetchMobileandUserId();
            GetProfileData model = (GetProfileData) Result;
            try {
                String name = model.getProfileDetails().get(0).getFName();
                String moblie = model.getProfileDetails().get(0).getMobile();
                String zipcode = model.getProfileDetails().get(0).getPincode();
                String gmail = model.getProfileDetails().get(0).getEMail();
                String code = model.getProfileDetails().get(0).getMerchant_Code();
                String webaddress = model.getProfileDetails().get(0).getAddress();
                String drb = model.getProfileDetails().get(0).getUser_DOB();


                if (zipcode != null) {
                    db.Open();
                    db.EditProfile(UserId, name, moblie, gmail, zipcode, code, webaddress, drb);
                    db.close();
                } else {
                    db.Open();
                    db.EditProfile(UserId, name, moblie, gmail, "", code, webaddress, drb);
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (id == webservice.TYPR_GET_TEMPLATE) {

            //Log.w("TAG 5", "TYPR_GET_TEMPLATE");

            AllTemplate mid = (AllTemplate) Result;

            db.Open();
            db.DeleteALLTemplateDataALL();
            db.close();

            for (int i = 0; i < mid.getUserAllTemplateListDetail().size(); i++) {

                String templarename = mid.getUserAllTemplateListDetail().get(i).getTemplate();
                String TemplateId = mid.getUserAllTemplateListDetail().get(i).getTemplate_id();
                String template_title = mid.getUserAllTemplateListDetail().get(i).getTemplate_title();
                String UserId = mid.getUserAllTemplateListDetail().get(i).getUser_id();

                db.Open();
                db.addtoAlltemplate(UserId, TemplateId, templarename, template_title);
                db.close();
            }
            if (id == webservice.TYPE_GET_GROUP_DETAILS) {
                FetchGroupDetails Model = (FetchGroupDetails) Result;

                db.Open();
                db.DeleteGroupDataALL();
                db.close();

                for (int i = 0; i < Model.getGroupDetails().size(); i++) {
                    fetchMobileandUserId();
                    String gpname = Model.getGroupDetails().get(i).getGroup_name();
                    String gpCount = Model.getGroupDetails().get(i).getNoOfContacts();

                    db.Open();
                    db.addGroupAll(UserId, gpname, gpCount);
                    db.close();
                }

            }
        }

    }

    private boolean haveTemplate() {

        db.Open();
        Cursor c;

        c = db.getTemplates();

        while (c.moveToNext() == true) {
            return true;

        }

        db.close();

        return false;

    }

    @Override
    public void onError(String Error, int id) {
        try {

            //Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

        }
    }

    public void fetchMobileandUserId() {
        db.Open();

        Cursor c;

        c = db.getLoginDetails();


        while (c.moveToNext()) {

            Mobile = c.getString(1);
            UserId = c.getString(3);

        }

        db.close();
    }

    public boolean hasAnydata() {

        db.Open();

        Cursor c;

        c = db.getLoginDetails();

        while (c.moveToNext()) {

            db.close();
            return true;
        }

        db.close();
        return false;
    }

}
