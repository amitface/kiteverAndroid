package sms19.listview.newproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.models.ContactInfo;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Countrymodel;
import sms19.inapp.msg.model.PhoneValidModel;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.model.CityList;
import sms19.listview.newproject.model.ClientLeadDetails;
import sms19.listview.newproject.model.ProfileContact;
import sms19.listview.newproject.model.StateList;
import sms19.listview.validation.Validation;
import sms19.listview.webservice.webservice;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class ContactAdd extends AppCompatActivity implements OnClickListener, NetworkManager, AdapterView.OnItemSelectedListener {


    /************************
     * contacts
     *******************/
    int Pick_Contact = 1;
    private final int FETCH_CITY_LIST = 2;
    private final int FETCH_STATE_LIST = 3;

    ContactRecieveListner l;
    ImageView addfromcontactbk, imageCalend, imageCalend2;
    /************************
     * contacts
     *******************/

    private Button addtocontac, importcsv, editBirthday, editAniversary;
    private EditText editName, editNumber, editTextEmail, etCompanyName,
            etAddress, etState, etCity;

    String service, name, number, cid, emailIdVal;
    private DataBaseDetails db = new DataBaseDetails(this);
    private String Mobile, Userid;

    // Variable for storing current date and time
    private int mYear, mMonth, mDay;
    private ProgressDialog p;
    private Context GAddContact;
    private String mobilenumber = "", username = "", phoneNo = "";

    private TextView countryNameTextView;
    private Spinner countryCodeSpinner, spinnerState, spinnerCity;
    private ArrayList<Countrymodel> countrymodels = null;
    private String countryCode1 = "";
    public static boolean isContactsUpdated = false;

    private boolean isWrongInput;

    private List<StateList> statelist;
    private List<CityList> citylist;

    private String stateid = "", cityid = "", stateName = "", cityName = "", companyName = "", address = "";

    private int addUpdate = 0, UserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adddeletecontacts);

        editName = (EditText) findViewById(R.id.editTextName);
        editNumber = (EditText) findViewById(R.id.editTextNumber);
        editBirthday = (Button) findViewById(R.id.editTextBirthday); // editTextBirthdayeditTextBirthday
        editAniversary = (Button) findViewById(R.id.editTextAniversary); // editTextAniversary
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        etCompanyName = (EditText) findViewById(R.id.etCompanyName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        spinnerState = (Spinner) findViewById(R.id.etState);
        spinnerCity = (Spinner) findViewById(R.id.etCity);

        addtocontac = (Button) findViewById(R.id.addtocontactbtn);// addtocontactbtn
        addtocontac.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        addtocontac.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        setRobotoThinFontButton(addtocontac,this);
        setRobotoThinFontButton(editBirthday,this);
        setRobotoThinFontButton(editAniversary,this);

        setRobotoThinFont(editName,this);
        setRobotoThinFont(editNumber,this);
        setRobotoThinFont(editTextEmail,this);
        setRobotoThinFont(editName,this);
        importcsv = (Button) findViewById(R.id.importcsvbtn);

        /************************ contacts *******************/
        addfromcontactbk = (ImageView) findViewById(R.id.addfromcontactbkbtn);
        /************************ contacts *******************/

        imageCalend = (ImageView) findViewById(R.id.imageCalend);
        imageCalend2 = (ImageView) findViewById(R.id.imageCalend2);



        actionBarSettingWithBack(this,getSupportActionBar(),"Create Contact");

        // bar.setHomeAsUpIndicator (R.drawable.arrow_new) ;

        /*************************** INTERNET ********************************/
        webservice._context = this;
        GAddContact = this;
        /*************************** INTERNET ********************************/

        /************************ contacts *******************/
        addfromcontactbk.setOnClickListener(this);

        /************************ contacts *******************/

        imageCalend2.setOnClickListener(this);
        imageCalend.setOnClickListener(this);

        countryCodeSpinner = (Spinner) findViewById(R.id.countryCodeSpinner);
        countryNameTextView = (TextView) findViewById(R.id.countryNameTextView);

        // countrymodels = new ArrayList<Countrymodel>();

        countrymodels = Utils.getCountryList(ContactAdd.this);

        fetch_uid();

        if (countrymodels != null) {
            int pos = 0;

            final String iso_code = com.kitever.utils.Utils
                    .getIsoCode(ContactAdd.this);

            if (countrymodels != null) {
                if (iso_code != null) {
                    if (!iso_code.equals("")) {
                        final int size = countrymodels.size();
                        for (int i = 0; i < size; i++) {
                            if (countrymodels.get(i).getCountryISOCode()
                                    .equals(iso_code)) {
                                pos = i;
                                break;
                            }
                        }
                    }
                }
            }

            sms19.inapp.msg.adapter.CountryListAdapter adapter1 = new sms19.inapp.msg.adapter.CountryListAdapter(
                    ContactAdd.this, countrymodels);
            countryCodeSpinner.setAdapter(adapter1);
            // countryCodeSpinner.setSelection(position);
            countryCodeSpinner.setSelection(pos);
            countryCodeSpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            countryCode1 = countrymodels.get(arg2)
                                    .getCountrycode().trim();
                            countryNameTextView.setText(countryCode1);

                            String phoneNumber = editNumber.getText()
                                    .toString().trim();

                            if (countryCode1 != null
                                    && phoneNumber.length() > 0
                                    && countryCode1.length() > 0) {
                                if (isValidPhoneNumber(phoneNumber)) {
                                    // boolean status =
                                    // validateUsing_libphonenumber(countryCode,
                                    // phoneNumber);
                                    boolean status = parseContact(phoneNumber,
                                            countryCode1);

                                    if (!status) {
                                        isWrongInput = true;
                                        editNumber.setError("Invalid phone");
                                    } else {
                                        isWrongInput = false;
                                        editNumber.setError(null);
                                    }
                                }
                            }
                        }

                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });

            Intent d = getIntent();
            ClientLeadDetails clientLeadDetails = d.getParcelableExtra("leads");
            if (clientLeadDetails == null) {
                try {

                    name = d.getStringExtra("editname");
                    number = d.getStringExtra("editNumber");
                    cid = d.getStringExtra("CID");
                    service = d.getStringExtra("updateService");
                    emailIdVal = d.getStringExtra("emailId");

                    editName.setText(name);
                    editNumber.setText(number);
                    if (emailIdVal != null && !emailIdVal.equalsIgnoreCase(""))
                        editTextEmail.setText(emailIdVal);

                    if (service.equals("updateService")) {

                        addtocontac.setText("Update Contact");
                        addUpdate =1;
				        actionBarSettingWithBack(this,getSupportActionBar(),"Update Contact");
                    }

                } catch (Exception e) {

                }

                try {
                    phoneNo = d.getStringExtra("phone_number");
                    if (phoneNo != null && phoneNo.length() > 0) {
                        editNumber.setText(phoneNo);
                    }
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else //Activity started from Leads microsite
            {
                editName.setText(clientLeadDetails.getFirstName() + " " + clientLeadDetails.getLastName());
                editNumber.setText(clientLeadDetails.getMobile());
                editTextEmail.setText(clientLeadDetails.getEmail());

            }
        }

        addtocontac.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isWrongInput) {
                    Toast.makeText(getApplicationContext(),
                            "Please input a valid number", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                mobilenumber = "";
                username = "";
                if (etCompanyName.getText() != null)
                    companyName = etCompanyName.getText().toString();
                if (etAddress.getText() != null)
                    address = etAddress.getText().toString();

                fetch_uid();

                try {

                    Intent intent = getIntent();
                    boolean fromInApp = false;
                    if (intent != null) {
                        fromInApp = intent.getBooleanExtra("froninapp", false);
                    }

                    final String name = editName.getText().toString().trim();
                    final String num = editNumber.getText().toString().trim();
                    final String DOB = editBirthday.getText().toString().trim();
                    final String aniversary = editAniversary.getText()
                            .toString().trim();
                    final String emailId = editTextEmail.getText().toString()
                            .trim();

                    if (!isValidEmailID(emailId)) {
                        Toast.makeText(getApplicationContext(),
                                "Please input a valid Email", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }

                    if (fromInApp) {

                        mobilenumber = num;
                        username = name;
                        if (Validation.hasText(editName)
                                && Validation.hasText(editNumber)) {
                            p = ProgressDialog.show(ContactAdd.this, null,
                                    "Adding Contact....");

                            StringRequest stringRequest = new StringRequest(
                                    Request.Method.POST, Apiurls
                                    .getBasePostURL(),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            sms19.inapp.msg.constant.Utils
                                                    .printLog2(response);

                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(
                                                        response);
                                                if (jsonObject != null) {
                                                    JSONArray array = jsonObject
                                                            .getJSONArray("ContactRegistration");
                                                    JSONObject jsonObject2 = array
                                                            .getJSONObject(0);

                                                    try {
                                                        if (jsonObject2 != null) {
                                                            if (jsonObject2
                                                                    .has("Msg")) {
                                                                String msg = jsonObject2
                                                                        .getString("Msg");

                                                                Toast.makeText(
                                                                        getApplicationContext(),
                                                                        msg,
                                                                        Toast.LENGTH_LONG)
                                                                        .show();
                                                                if (jsonObject2
                                                                        .has("UserType")) {
                                                                    String userType = jsonObject2
                                                                            .getString("UserType");
                                                                    addContactinDB(userType);
                                                                }



                                                                sendContactAddActionMessage();
                                                            }
                                                        }
                                                    } catch (Exception exp) {

                                                    }
                                                    try {
                                                        if (jsonObject2 != null) {
                                                            if (jsonObject2
                                                                    .has("Error")) {
                                                                String msg = jsonObject2
                                                                        .getString("Error");
                                                                Toast.makeText(
                                                                        getApplicationContext(),
                                                                        msg,
                                                                        Toast.LENGTH_LONG)
                                                                        .show();
                                                            }
                                                        }
                                                    } catch (Exception exp) {

                                                    }
                                                }
                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch
                                                // block
                                                e.printStackTrace();
                                            }

                                            if (p != null) {
                                                if (p.isShowing()) {
                                                    p.dismiss();
                                                }
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(
                                        VolleyError error) {
                                    if (p != null) {
                                        if (p.isShowing()) {
                                            p.dismiss();
                                        }
                                    }
                                    Toast.makeText(
                                            getApplicationContext(),
                                            error.toString(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();

                                    params.put("Page",
                                            "InsertIndividualContact");
                                    params.put("EmailId", emailId);
                                    params.put("Userid", Userid);
                                    params.put("contactName", name);
                                    params.put("contactMobile", num);

                                    params.put("contactDOB", DOB);
                                    params.put("contactAnniversary", aniversary);
                                    params.put("countryCode", countryCode1);

                                    params.put("CompanyName", companyName);
                                    params.put("Address", address);
                                    params.put("Stateid", stateid);
                                    params.put("Cityid", cityid);

                                    return params;
                                }
                            };

                            RequestQueue requestQueue = Volley
                                    .newRequestQueue(ContactAdd.this);
                            requestQueue.add(stringRequest);

                        } else {
                            Toast.makeText(GAddContact,
                                    "Insert atleast Name or Number",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        isContactsUpdated = true;
                        final ContactInfo contactInfo = (ContactInfo) getIntent()
                                .getSerializableExtra("EDIT_CONTACT");
                        mobilenumber = num;
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST, Apiurls.getBasePostURL(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(
                                                    response);
                                            if (jsonObject != null) {
                                                JSONArray array = jsonObject
                                                        .getJSONArray("UpdateContacts");
                                                JSONObject jsonObject2 = array
                                                        .getJSONObject(0);

                                                try {
                                                    if (jsonObject2 != null) {
                                                        if (jsonObject2
                                                                .has("Msg")) {
                                                            String msg = jsonObject2
                                                                    .getString("Msg");
                                                            Toast.makeText(
                                                                    getApplicationContext(),
                                                                    msg,
                                                                    Toast.LENGTH_LONG)
                                                                    .show();

                                                            setResult(RESULT_OK);
                                                            updateContactinDB("N");

                                                            sendContactAddActionMessage();

                                                            finish();

                                                        } else if (jsonObject2
                                                                .has("ErrorMessage")) {
                                                            String msg = jsonObject2
                                                                    .getString("ErrorMessage");
                                                            Toast.makeText(
                                                                    getApplicationContext(),
                                                                    msg,
                                                                    Toast.LENGTH_LONG)
                                                                    .show();

                                                            setResult(RESULT_OK);

                                                            sendContactAddActionMessage();

                                                            finish();
                                                        }
                                                    }
                                                } catch (Exception exp) {

                                                }
                                                try {
                                                    if (jsonObject2 != null) {
                                                        if (jsonObject2
                                                                .has("Error")) {
                                                            String msg = jsonObject2
                                                                    .getString("Error");
                                                            Toast.makeText(
                                                                    getApplicationContext(),
                                                                    msg,
                                                                    Toast.LENGTH_LONG)
                                                                    .show();
                                                        }
                                                    }

                                                } catch (Exception exp) {

                                                }
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

										/*
                                         * Toast.makeText(
										 * getApplicationContext(
										 * ),"Contact Added Successfully.",
										 * Toast.LENGTH_LONG) .show();
										 */

                                        if (p != null) {
                                            if (p.isShowing()) {
                                                p.dismiss();
                                            }
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(
                                    VolleyError error) {
                                if (p != null) {
                                    if (p.isShowing()) {
                                        p.dismiss();
                                    }
                                }
                                Toast.makeText(getApplicationContext(),
                                        error.toString(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();

                                params.put("Page", "UpdateContacts");
                                /*
                                 * Response.Write(objser.UpdateContacts(Request.Form
								 * ["Userid"], Request.Form["contactName"],
								 * Request.Form["contactMobile"],
								 * Request.Form["contactDOB"],
								 * Request.Form["contactAnniversary"],
								 * Request.Form["oldmobile"],
								 * Request.Form["countryCode"]));
								 */

                                params.put("Userid", Userid);
                                params.put("contactName", name);
                                params.put("contactMobile", num);
                                params.put("EmailId", emailId);
                                params.put("contactDOB", DOB);
                                params.put("contactAnniversary", aniversary);
                                params.put("countryCode", countryCode1);

                                params.put("CompanyName", etCompanyName.getText().toString());
                                params.put("Address", etAddress.getText().toString());
                                params.put("Stateid", stateid);
                                params.put("Cityid", cityid);

                                params.put("oldmobile",
                                        contactInfo.Contact_Mobile);
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley
                                .newRequestQueue(ContactAdd.this);
                        requestQueue.add(stringRequest);
                    }
                } catch (Exception e) {

                }
            }
        });

        editNumber.addTextChangedListener(new TextWatcher() {
            String phoneNumber;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                phoneNumber = editNumber.getText().toString().trim();

                if (phoneNumber.length() > 0 && countryCode1.length() > 0) {
                    if (isValidPhoneNumber(phoneNumber)) {
                        // boolean status =
                        // validateUsing_libphonenumber(countryCode,
                        // phoneNumber);
                        boolean status = parseContact(phoneNumber, countryCode1);

                        if (!status) {
                            isWrongInput = true;
                            editNumber.setError("Invalid phone");
                        } else {
                            isWrongInput = false;
                            editNumber.setError(null);
                        }
                    }
                }
            }
        });

        boolean b = getIntent().getBooleanExtra("froninapp", false);

        if (!b) {
            addtocontac.setText("Update Contact");

			/*
             * editBirthday.setVisibility(View.INVISIBLE);
			 * editAniversary.setVisibility(View.INVISIBLE);
			 * imageCalend.setVisibility(View.INVISIBLE);
			 * imageCalend2.setVisibility(View.INVISIBLE);
			 */

            actionBarSettingWithBack(this,getSupportActionBar(),"Update Contact");

         /*   ActionBar bar1 = getSupportActionBar();
            bar1.setTitle(com.kitever.utils.Utils
                    .setActionBarTextAndColor("Update Contact"));*/

            final ContactInfo contactInfo = (ContactInfo) getIntent()
                    .getSerializableExtra("EDIT_CONTACT");
            if (contactInfo != null) {
                if (contactInfo.Contact_Name == null
                        || contactInfo.Contact_Name.equalsIgnoreCase("null")
                        || contactInfo.Contact_Name.equalsIgnoreCase("")) {
                    editName.setText(contactInfo.getContact_Mobile());
                } else {
                    editName.setText(contactInfo.Contact_Name);
                }

                editNumber.setText(contactInfo.getContactNumber());
                if (contactInfo.Contact_DOB == null
                        || contactInfo.Contact_DOB.equalsIgnoreCase("null")
                        || contactInfo.Contact_DOB.equalsIgnoreCase("")) {
                    contactInfo.Contact_DOB = "";
                    editBirthday.setHint("Date of Birth");
                } else {
                    editBirthday.setText(contactInfo.Contact_DOB);
                }
                if (contactInfo.Contact_Anniversary == null
                        || contactInfo.Contact_Anniversary
                        .equalsIgnoreCase("null")
                        || contactInfo.Contact_Anniversary.equalsIgnoreCase("")) {
                    contactInfo.Contact_Anniversary = "";
                    editAniversary.setHint("Date of Anniversary");
                } else {
                    editAniversary.setText(contactInfo.Contact_Anniversary);
                }
                if (contactInfo.contact_email == null
                        || contactInfo.contact_email.equalsIgnoreCase("null")
                        || contactInfo.contact_email.equalsIgnoreCase("")) {
                    contactInfo.contact_email = "";
                    editTextEmail.setHint("Email Id");
                } else {
                    editTextEmail.setText(contactInfo.getContact_email());
                }

                etCompanyName.setText(contactInfo.getCompanyName());
                etAddress.setText(contactInfo.getAddress());
                stateid = contactInfo.getState();
                cityid = contactInfo.getCity();
            }
        }

        fetchStateData("110");
    }

    private void sendContactAddActionMessage() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(
                com.kitever.utils.Utils.CONTACTS_UPDATE_ACTION);
        // You can also include some extra data.
        intent.putExtra(com.kitever.utils.Utils.CONTACTS_MESSAGE_TYPE,
                com.kitever.utils.Utils.CONTACTS_ADD_UPDATE);
        intent.putExtra(com.kitever.utils.Utils.CONTACTS_MESSAGE,
                "Contact added/updated successfully");

        LocalBroadcastManager.getInstance(ContactAdd.this)
                .sendBroadcast(intent);

    }


    private void updateContactinDB(final String userType)
    {
        final String num = editNumber.getText().toString().trim();
        name = editName.getText().toString().trim();
        final String DOB = editBirthday.getText().toString().trim();
        final String aniversary = editAniversary.getText()
                .toString().trim();
        final String emailId = editTextEmail.getText().toString()
                .trim();
        final String countryCode = countryCode1;

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    if (GlobalData.dbHelper != null) {

                        String remoteid = (countryCode1 + mobilenumber + "@" + GlobalData.HOST)
                                .trim();

                                sms19.inapp.msg.model.PhoneValidModel model = new PhoneValidModel();
                                model.setCountry_code(countryCode1);
                                model.setPhone_number(mobilenumber);

                                if (model != null
                                        && model.getPhone_number() != null
                                        && model.getPhone_number().length() > 0) {
                                    if (name.equalsIgnoreCase("")) {
                                        name = model.getCountry_code()
                                                + model.getPhone_number();
                                    }

                                    /*Zero to add contact*/
                                    GlobalData.dbHelper
                                            .insertSinglecontactinSMS19DBWithUserType(1,
                                                    name,
                                                    model.getCountry_code()
                                                            + model.getPhone_number(),
                                                    userType,emailId, companyName,
                                                    DOB, aniversary, countryCode,
                                                    num,  address, stateid, cityid);

                        } else {
                            // deleteContactFromSMS19
                        }
                    }
                } catch (Exception e) {

                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        setResult(RESULT_OK);

                        finish();
                    }
                });

            }
        }).start();
    }
    private void addContactinDB(final String userType) {

        username = editName.getText().toString().trim();
        final String num = editNumber.getText().toString().trim();
        final String DOB = editBirthday.getText().toString().trim();
        final String aniversary = editAniversary.getText()
                .toString().trim();
        final String emailId = editTextEmail.getText().toString()
                .trim();
        final String countryCode = countryCode1;

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    if (GlobalData.dbHelper != null) {

                        String remoteid = (countryCode1 + mobilenumber + "@" + GlobalData.HOST)
                                .trim();

                        if (!GlobalData.dbHelper
                                .checkcontactisAlreadyexist(remoteid)) {
                            if (!GlobalData.dbHelper
                                    .checkcontactisSMS19Alreadyexist(remoteid)) {
                                // 9851144639
                                // sms19.inapp.msg.model.PhoneValidModel model=
                                // ContactUtil.validNumberForInvite(countryCode1+mobilenumber);
                                sms19.inapp.msg.model.PhoneValidModel model = new PhoneValidModel();
                                model.setCountry_code(countryCode1);
                                model.setPhone_number(mobilenumber);

                                if (model != null
                                        && model.getPhone_number() != null
                                        && model.getPhone_number().length() > 0) {
                                    if (username.equalsIgnoreCase("")) {
                                        username = model.getCountry_code()
                                                + model.getPhone_number();
                                    }

                                    /*Zero to add contact*/
                                    GlobalData.dbHelper
                                            .insertSinglecontactinSMS19DBWithUserType(0,
                                                    username,
                                                    model.getCountry_code()
                                                            + model.getPhone_number(),
                                                    userType,emailId, companyName,
                                            DOB, aniversary, countryCode,
                                            num,  address, stateid, cityid);
                                }
                            }
                        } else {
                            // deleteContactFromSMS19
                        }
                    }
                } catch (Exception e) {

                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        setResult(RESULT_OK);

                        finish();
                    }
                });

            }
        }).start();

    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {

        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }

        return false;
    }

    private boolean isValidEmailID(CharSequence emailid) {

        if (!TextUtils.isEmpty(emailid)) {
            return Patterns.EMAIL_ADDRESS.matcher(emailid).matches();
        }

        return true;
    }

    private boolean parseContact(String contact, String countrycode) {
        if (countrycode != null && countrycode.length() > 0) {
            try {
                countrycode = countrycode.substring(1, countrycode.length());
            } catch (Exception e) {
                // TODO: handle exception
                countrycode = countrycode
                        .substring(1, countrycode.length() - 1);
            }

        }

        PhoneNumber phoneNumber = null;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer
                .parseInt(countrycode));
        boolean isValid = false;
        PhoneNumberType isMobile = null;
        try {
            phoneNumber = phoneNumberUtil.parse(contact, isoCode);
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            isMobile = phoneNumberUtil.getNumberType(phoneNumber);

        } catch (NumberParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        boolean isValid2 = isValid
                && (PhoneNumberType.MOBILE == isMobile || PhoneNumberType.FIXED_LINE_OR_MOBILE == isMobile);

        if (isValid2) {
            Utils.printLog2("isValid2= " + isValid2);
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.addfromcontactbkbtn: {
                /************************ contacts *******************/

                Intent i = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, Pick_Contact);

                /************************ contacts *******************/
            }
            break;
            case R.id.imageCalend: {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                long currentDate = c.getTimeInMillis();
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                editBirthday.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(currentDate);
                dpd.show();

            }
            break;

            case R.id.imageCalend2: {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                long currentDate = c.getTimeInMillis();
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                editAniversary.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(currentDate);
                dpd.show();
            }
            break;

        }
    }

    /************************
     * contacts
     *******************/

    // --------------set listener for contact----------------------
    public void setListner(ContactRecieveListner l) {

        this.l = l;
    }

	/*
	 * By which Getting data from another activity via onActivityResult Or we
	 * can say that This is a Receiver for capturing result from another
	 * activity, In which we use startActivityForResult instead of startActivity
	 */

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);
        try {
            if (reqCode == Pick_Contact) {
                if (resultCode == Activity.RESULT_OK) {

                    Cursor c;
                    ContentResolver cr = getContentResolver();

                    try {
                        Uri contactData = data.getData();

                        c = cr.query(contactData, null, null, null, null);

                        if (c.moveToFirst()) {
                            String id = c
                                    .getString(c
                                            .getColumnIndex(ContactsContract.Contacts._ID));

                            if (id.length() > 0) {

                                c = getContentResolver()
                                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                null,
                                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                        + " = " + id, null,
                                                null);

                                c.moveToFirst();

                                String cNumber = "";
                                cNumber = c
                                        .getString(c
                                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                String nameContact = c
                                        .getString(c
                                                .getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                                try {

                                    cNumber = cNumber.replaceAll("-", "");
                                    cNumber = cNumber.replaceAll("\\(", "");
                                    cNumber = cNumber.replaceAll("\\)", "");
                                    cNumber = cNumber.replaceAll("\\s+", "");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    cNumber = c
                                            .getString(c
                                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                }
                                l.onContactRecieved(nameContact, cNumber);
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(GAddContact,
                                "No mobile contact(s) available",
                                Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    }
                }
            } else {
                // Toast.makeText(GAddContact,
                // "ok::::No mobile contact(s) available",
                // Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
        }
    }

    /************************ contacts *******************/

    /**************************
     * FETCH USERID FROM DATABASE
     ****************************/
    public void fetch_uid() {
        db.Open();

        Cursor c;
        c = db.getLoginDetails();

        while (c.moveToNext()) {
            Userid = c.getString(3);

        }
        db.close();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchStateData(String countryid) {

        if (Utils.isDeviceOnline(this)) {
            Map<String, String> map = new HashMap<>();
            map.put("Page", "GetState");
            map.put("countryID", countryid);
            try {
                new RequestManager().sendPostRequest(this,
                        FETCH_STATE_LIST, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // showMessage("Internet connection not found");
        }
    }

    private void fetchCityData(String stateid) {

        if (Utils.isDeviceOnline(this)) {

            Map<String, String> map = new HashMap<>();
            map.put("Page", "GetCity");
            map.put("stateID", stateid);
            try {
                new RequestManager().sendPostRequest(this,
                        FETCH_CITY_LIST, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // showMessage("Internet connection not found");
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        Gson gson = new Gson();
        if (response != null && response.length() > 0) {
            if (requestId == FETCH_STATE_LIST) {
                statelist = new ArrayList<StateList>();
                ProfileContact stlist = gson.fromJson(response, ProfileContact.class);

                statelist = stlist.getStateList();
                int select_pos = 0;
                if (stlist != null && statelist.size() > 0) {

                    ArrayList<String> stateNameList = new ArrayList<String>();
                    stateNameList.add("Select State");
                    for (int i = 0; i < statelist.size(); i++) {
                        stateNameList.add(statelist.get(i).getStateName());
                        String current_state_id = "" + statelist.get(i).getStateID();
                        if (stateid != null && stateid.equals(current_state_id))
                            select_pos = i + 1;
                    }

                    try {
                        Utils.MySpinnerAdapter typeAdapter = new Utils.MySpinnerAdapter(this, android.R.layout.simple_spinner_item, stateNameList);
                        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerState.setAdapter(typeAdapter);
                        spinnerState.setSelection(select_pos);
                        spinnerState.setOnItemSelectedListener(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestId == FETCH_CITY_LIST) {

                spinnerCity.setEnabled(true);
                citylist = new ArrayList<CityList>();
                ProfileContact ctlist = gson.fromJson(response, ProfileContact.class);
                citylist = ctlist.getCityList();
                if (ctlist != null && citylist.size() > 0) {

                    ArrayList<String> cityNameList = new ArrayList<String>();
                    cityNameList.add("Select City");
                    int select_pos = 0;
                    for (int i = 0; i < citylist.size(); i++) {
                        cityNameList.add(citylist.get(i).getCityName());
                        String current_city_id = "" + citylist.get(i).getCityID();
                        if (cityid != null && cityid.equals(current_city_id)) select_pos = i + 1;
                    }

                    try {
                        Utils.MySpinnerAdapter cityadapter = new Utils.MySpinnerAdapter(this, android.R.layout.simple_spinner_item, cityNameList);
                        cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCity.setAdapter(cityadapter);
                        spinnerCity.setSelection(select_pos);
                        spinnerCity.setOnItemSelectedListener(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.etState:
                if (position > 0) {
                    stateid = "" + statelist.get(position - 1).getStateID();
                    stateName = statelist.get(position - 1).getStateName();
                    fetchCityData(stateid);
                } else spinnerCity.setEnabled(false);
                break;
            case R.id.etCity:
                if (position > 0) {
                    cityid = "" + citylist.get(position - 1).getCityID();
                    cityName = citylist.get(position - 1).getCityName();
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
