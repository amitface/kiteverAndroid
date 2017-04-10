package sms19.inapp.msg.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kitever.android.BuildConfig;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.ContactUtil;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Chatmodel;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.Groupmodel;
import sms19.inapp.msg.model.InsertContactModel;
import sms19.inapp.msg.model.Recentmodel;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;

@SuppressLint("SimpleDateFormat")
public class DatabaseHelper extends SQLiteOpenHelper {

    public static DatabaseHelper dbhelper;
    public static SQLiteDatabase myDataBase;
    private static final int DATABASE_VERSION = 3;

    private Context mycontext;
    String path;
    String dateSeperatorValue = "";
    // public static String DB_PATH = Environment.getExternalStorageDirectory()
    // + "/Kitever/databases/";
    public static String DB_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";
//	public static String DB_PATH = "/data/data/ + context.getApplicationContext().getPackageName() + /databases";

    // public static String PROFILEPATH =
    // Environment.getExternalStorageDirectory()
    // + "/Kitever/databases/";
    public static String PROFILEPATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";

    // public static String LNAGUAGE_DETECTION_DATA =
    // Environment.getExternalStorageDirectory()
    // + "/Kitever/databases/profiles/";
    public static String LNAGUAGE_DETECTION_DATA = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/profiles/";

    // public static String APP_DIR = Environment.getExternalStorageDirectory()
    // + "/Kitever/";
    public static String APP_DIR = "/data/data/" + BuildConfig.APPLICATION_ID + "/";
    //public static String APP_DIR = "/data/data/com.hoteltheroyalplaza.android/";

    private static String DB_NAME = "SMS19.db";
    static String TableName_Contacts = "Contacts";
    static String TableName_SMS19_Contacts = "Sms19_Contacts";
    static String TableName_Messages = "Messages";
    static String TableName_Offline_Messages = "OfflineMessages";
    static String TableName_Recent = "Recent";
    static String TableName_UserProfile = "UserProfile";
    static String TableName_Groups = "Groups";
    static String Message_Expire = "Message_Expire";
    SharedPreferences preference;
    private boolean checkdb;

    public DatabaseHelper(Context context, boolean newUser) throws IOException {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.mycontext = context;
        Log.i("dbversion", "DATABASE_VERSION-" + DATABASE_VERSION);
        // DB_PATH = Environment.getExternalStorageDirectory()
        // + "/UmnoChat/databases/";

        preference = mycontext.getSharedPreferences("profileData", Context.MODE_PRIVATE);

        if (new File(DB_PATH + DB_NAME).exists()) {
            if (newUser) {
                Utils.deleteDir(new File(APP_DIR));
                GenerateNewDB(context);
            } else {
                if (myDataBase == null || (!myDataBase.isOpen())) {
                    opendatabase();
                }
            }
        } else {
            GenerateNewDB(context);
        }
    }

    private void GenerateNewDB(Context context) {

        Log.i("dbversion", "GenerateNewDB-");
        try {
            File db = new File(DB_PATH + DB_NAME);
            if (!db.getParentFile().exists()) {
                db.getParentFile().mkdirs();
            }
            String str = db.toString();
            // myDataBase =
            // context.openOrCreateDatabase(str,Context.MODE_WORLD_WRITEABLE,
            // null);
            myDataBase = context.openOrCreateDatabase(str,
                    SQLiteDatabase.OPEN_READONLY, null);
            migrate(context);

            preference = mycontext.getSharedPreferences("profileData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preference.edit();
            editor.putString("istableUpdated2", "1");
            editor.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void migrate(Context context) {
        myDataBase.beginTransaction();
        myDataBase.setTransactionSuccessful();
        myDataBase.endTransaction();
        myDataBase.execSQL("VACUUM");
        copyFromAssets();
    }

    public void clearDataBase() {
        myDataBase.delete(TableName_Contacts, null, null);
        myDataBase.delete(TableName_Messages, null, null);
        myDataBase.delete(TableName_Recent, null, null);
        myDataBase.delete(TableName_UserProfile, null, null);
        myDataBase.delete(TableName_Groups, null, null);
        myDataBase.delete(TableName_SMS19_Contacts, null, null);
        myDataBase.delete(Message_Expire, null, null);// new added
    }

    public boolean isDBExist() {
        File dbFile = mycontext.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    public void opendatabase() throws SQLException {
        Log.i("dbversion", "opendatabase-");
        String mypath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mypath, null,
                SQLiteDatabase.OPEN_READWRITE);
        if (!preference.contains("istableUpdated2"))
            alterTable(myDataBase);
    }

    public void alterTable(SQLiteDatabase db) {
        Log.i("dbversion", "altertable");
        try {
            db.execSQL("ALTER TABLE " + TableName_Contacts + " ADD COLUMN contactDOB TEXT");
            db.execSQL("ALTER TABLE " + TableName_Contacts + " ADD COLUMN contactAnniversary TEXT");
            db.execSQL("ALTER TABLE " + TableName_Contacts + " ADD COLUMN contactNumber TEXT");
            db.execSQL("ALTER TABLE " + TableName_Contacts + " ADD COLUMN countryCode TEXT");
            db.execSQL("ALTER TABLE " + TableName_Contacts + " ADD COLUMN companyName TEXT");
            db.execSQL("ALTER TABLE " + TableName_Contacts + " ADD COLUMN address TEXT");
            db.execSQL("ALTER TABLE " + TableName_Contacts + " ADD COLUMN stateId INTEGER NOT NULL DEFAULT -1");
            db.execSQL("ALTER TABLE " + TableName_Contacts + " ADD COLUMN cityId INTEGER NOT NULL DEFAULT -1");
            db.execSQL("ALTER TABLE " + TableName_Contacts + " ADD COLUMN stateName TEXT");
            db.execSQL("ALTER TABLE " + TableName_Contacts + " ADD COLUMN cityName TEXT");

        } catch (Exception e) {

        }
        preference = mycontext.getSharedPreferences("profileData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("istableUpdated2", "1");
        editor.commit();
    }

    public void copyFromAssets() {
        try {

            InputStream myInput = mycontext.getAssets().open("chatapp.sqlite");
            String outFile = DB_PATH + "/" + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        dbhelper = this;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // If you need to add a column
        Log.i("dbversion", "new-" + newVersion + " : old-" + oldVersion);
        if (newVersion > oldVersion) {
            /*db.execSQL("ALTER TABLE "+TableName_Messages+" ADD COLUMN mail_count INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE "+TableName_Messages+" ADD COLUMN isActivity INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE "+TableName_Contacts+" ADD COLUMN contact_email TEXT");*/
        }
    }

    public void insertSinglecontactinDB(String name, String phone) {
        ContentValues valuesFor_Contact = new ContentValues();

        valuesFor_Contact.put("phonenumber", phone);
        valuesFor_Contact.put("display_name", name);
        valuesFor_Contact.put("isregister", 0);
        valuesFor_Contact.put("isstranger", 0);
        valuesFor_Contact.put("issms19_number", 0);
        valuesFor_Contact.put("unread_msg_count", 0);
        String remote_jid = (phone + "@" + GlobalData.HOST).trim();
        valuesFor_Contact.put("remote_jid", remote_jid);
        if (!checkcontactisAlreadyexist(remote_jid.trim())) {// new condition

            myDataBase.insert(TableName_Contacts, null, valuesFor_Contact);
            if (checkcontactisSMS19Alreadyexist(remote_jid.trim())) {
                deleteContactFromSMS19(remote_jid.trim());
            }

        }

    }


    public void insertSinglecontactinSMS19DBWithUserType(int addUpdate, String name,
                                                         String phone, String userType, String contact_email, String companyName,
                                                         String contactDOB, String contactAnniversary, String countryCode,
                                                         String contactNumber, String address, String stateId, String cityId) {
        ContentValues valuesFor_Contact = new ContentValues();

        valuesFor_Contact.put("phonenumber", phone);
        valuesFor_Contact.put("display_name", name);
        /*
         * valuesFor_Contact.put("isregister", 1); String WHERE =
		 * "phonenumber='" + number + "'"; valuesFor_Contact.put("isstranger",
		 * 0); valuesFor_Contact.put("issms19_number", 0);
		 */

        if (userType != null) {
            if (userType.equals("A")) {
                valuesFor_Contact.put("isregister", 1);
                valuesFor_Contact.put("issms19_number", 0);
            } else if (userType.equals("N")) {
                valuesFor_Contact.put("isregister", 0);
                valuesFor_Contact.put("issms19_number", 1);
            }
        }

        valuesFor_Contact.put("isstranger", 0);

        valuesFor_Contact.put("unread_msg_count", 0);
        String remote_jid = (phone + "@" + GlobalData.HOST).trim();
        valuesFor_Contact.put("remote_jid", remote_jid);


        valuesFor_Contact.put("contact_email", contact_email);

        valuesFor_Contact.put("companyName", companyName);


        valuesFor_Contact.put("contactDOB", contactDOB);


        valuesFor_Contact.put("contactAnniversary", contactAnniversary);


        valuesFor_Contact.put("countryCode", countryCode);


        valuesFor_Contact.put("contactNumber", contactNumber);


        valuesFor_Contact.put("address", address);


        valuesFor_Contact.put("stateId", stateId);


        valuesFor_Contact.put("cityId", cityId);


        try {
            if (addUpdate == 0)
                myDataBase.insert(TableName_Contacts, null, valuesFor_Contact);
            else if (addUpdate == 1) ;
                myDataBase.update(TableName_Contacts, valuesFor_Contact,"phonenumber=?",new String[]{phone});
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    // satyadata
    public void updateAfterGetContactfromServer(InsertContactModel insertContactModel) {

        for (int i = 0; i < insertContactModel.getInsertContacts().size(); i++) {
            try {
                InsertContactModel.InsertContacts singleContact = insertContactModel.getInsertContacts().get(i);
                Contactmodel model = new Contactmodel();
                ContentValues valuesFor_Contact = new ContentValues();


                String number2 = singleContact.getContact_Mobile().trim();

                sms19.inapp.msg.model.PhoneValidModel UpdateModel = ContactUtil
                        .validNumberForGetContactApi(number2);

                if (UpdateModel != null
                        && UpdateModel.getPhone_number() != null
                        && UpdateModel.getPhone_number().length() > 0) {

                    String remote_jid = (UpdateModel.getCountry_code()
                            + UpdateModel.getPhone_number() + "@" + GlobalData.HOST);
                    String name = singleContact.getContact_Name();

                    String imageUrl = singleContact.getImageUrl();
                    /*String strImageUrl = null;
                    if (imageUrl != null && !imageUrl.equalsIgnoreCase("null")) {
                        strImageUrl = imageUrl.trim();
                    }*/
                    if (number2.equalsIgnoreCase("8375023977")
                            || number2.equalsIgnoreCase("7840002738")) {
                        String str = null;
                        str = number2;
                    }
                    String UserType = "";
                    String number = UpdateModel.getCountry_code()
                            + UpdateModel.getPhone_number();

                    if (singleContact.getUserType() != null
                            && singleContact.getUserType().length() > 0) {
                        UserType = singleContact.getUserType();
                    }

                    model.setNumber(number);
                    model.setRemote_jid(remote_jid);
                    model.setName(name);
                    model.setImageUrl(imageUrl);
                    valuesFor_Contact.put("phonenumber", number);
                    valuesFor_Contact.put("remote_jid", remote_jid);
                    if (UserType.equalsIgnoreCase("A")) {
                        valuesFor_Contact.put("isregister", 1);
                    } else
                        valuesFor_Contact.put("isregister", 0);

                    valuesFor_Contact.put("display_name", name);

                    String WHERE = "phonenumber='" + number + "'";
                    valuesFor_Contact.put("isstranger", 0);
                    valuesFor_Contact.put("issms19_number", 0);
                    /*valuesFor_Contact.put("profilepic_data",
                            imageUrl.getBytes());*/
                    valuesFor_Contact.put("profilepic_name", imageUrl);

                    String contact_email = singleContact.getContact_email();
                    if (contact_email != null && contact_email.trim().length() > 4)
                        valuesFor_Contact.put("contact_email", contact_email);
                    if (singleContact.getCompanyName() != null)
                        valuesFor_Contact.put("companyName", singleContact.getCompanyName());

                    if (singleContact.getContact_DOB() != null)
                        valuesFor_Contact.put("contactDOB", singleContact.getContact_DOB());

                    if (singleContact.getContact_Anniversary() != null)
                        valuesFor_Contact.put("contactAnniversary", singleContact.getContact_Anniversary());

                    if (singleContact.getCountryCode() != null)
                        valuesFor_Contact.put("countryCode", singleContact.getCountryCode());

                    if (singleContact.getContact_Mobile() != null)
                        valuesFor_Contact.put("contactNumber", singleContact.getContact_Mobile());

                    if (singleContact.getAddress() != null)
                        valuesFor_Contact.put("address", singleContact.getAddress());

                    if (singleContact.getState() != null)
                        valuesFor_Contact.put("stateId", singleContact.getState());

                    if (singleContact.getCity() != null)
                        valuesFor_Contact.put("cityId", singleContact.getCity());

                    if (singleContact.getContact_id() != null)
                        valuesFor_Contact.put("contact_id", singleContact.getContact_id());

                    if (checkcontactisAlreadyexist(remote_jid.trim())) {
                        myDataBase.update(TableName_Contacts,
                                valuesFor_Contact, WHERE, null);
                    } else {
                        myDataBase.insert(TableName_Contacts, null,
                                valuesFor_Contact);
                    }

                    GlobalData.registerContactList.add(model);
                }
//                if (UserType.equalsIgnoreCase("A")) {
//                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

		/*
         * if(GlobalData.registerContactList!=null){ Utils.addingRosterentry();
		 * }
		 */

        ConstantFields.Contactinsertsuccess = true;

    }


    /*Amit*/
    public void updateByteArrayInContacts(byte[] byteArray, String remote_jid) {
        // updation in group
        ContentValues newValues = new ContentValues();
        newValues.put("profilepic_data", byteArray);
        String Where = "remote_jid='" + remote_jid + "'";
        myDataBase.update(TableName_Contacts, newValues, Where, null);
    }



    public synchronized Contactmodel NewentryupdateorAdd(String remote_jid,
                                                         String status, byte[] avatar, String picname, String custom_status) {
        // , String formattedDate
        // TODO Auto-generated method stub
        // String query = "UPDATE Contacts SET status='" + status
        // + "',profilepic_data='" + avatar + "',profilepic_name='"
        // + picname + "' WHERE remote_jid='" + remote_jid + "'";

        // Calendar c = Calendar.getInstance();
        // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // String formattedDate = df.format(c.getTime());

        Contactmodel newcontact = new Contactmodel();
        String lastseen = Utils.currentTimeStap();
        String Where = "remote_jid='" + remote_jid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("status", status);
        newValues.put("status_custom", custom_status);
        // newValues.put("lastseen", lastseen);// 08 may
        newValues.put("isregister", 1);

        if (avatar != null) {
            newValues.put("profilepic_data", avatar);

        }
        newValues.put("profilepic_name", picname);
        int id = myDataBase.update(TableName_Contacts, newValues, Where, null);
        if (id == 0) {
            // testTm+917877475123@conference.103.25.130.241
            if (!checkcontactisAlreadyexist(remote_jid.trim())) {// new
                // condition
                newValues.put("remote_jid", remote_jid);
                String[] hostnumber = remote_jid.split("@");
                String number = hostnumber[0];
                newValues.put("display_name", number);
                newValues.put("phonenumber", number);
                newValues.put("isstranger", 1);

                myDataBase.insert(TableName_Contacts, null, newValues);
                if (checkcontactisSMS19Alreadyexist(remote_jid.trim())) {
                    deleteContactFromSMS19(remote_jid.trim());
                }
            }

        }
        String name = getDisplaynameofNewContact(remote_jid);
        if (name != null && name.trim().length() != 0) {
            newcontact.setName(name);
        } else
            newcontact.setName("");
        newcontact.setRemote_jid(remote_jid);
        newcontact.setStatus(status);

        if (lastseen != null) {
            newcontact.setLastseen(lastseen);
        }

        if (avatar != null) {
            newcontact.setAvatar(avatar);
        }

        return newcontact;

        // Cursor cursor = myDataBase.rawQuery(query, null);

    }

    public String getDisplaynameofNewContact(String remoteid) {
        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + remoteid + "'";
        Cursor cursor = null;
        String displayname = "";
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                displayname = cursor.getString(cursor
                        .getColumnIndex("display_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        return displayname;

    }

    public String getMessageTime(String packetId) {
        String query = "SELECT * FROM " + TableName_Messages
                + " where message_packetid='" + packetId + "'";
        Cursor cursor = null;
        String msgTime = "0";
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                msgTime = (cursor.getString(cursor.getColumnIndex("msg_time")));
                /* msgTime= sms19.inapp.msg.constant.Utils.getmsgTime(msgTime); */
            }
        } catch (Exception e) {
            e.printStackTrace();
            msgTime = "0";

        }
        if (cursor != null) {
            cursor.close();
        }

        return msgTime;

    }

    public boolean getIsStranger(String remoteid) {
        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + remoteid + "'";
        Cursor cursor = null;
        boolean isStrager = false;
        int isstrger = 0;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                isstrger = (cursor.getInt(cursor.getColumnIndex("isstranger")));

                isStrager = isstrger == 1;

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        return isStrager;

    }

    public boolean isSMS19Contact(String remoteid) {
        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + remoteid + "'";
        Cursor cursor = null;
        boolean isStrager = false;
        int issms19_number = 0;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                issms19_number = (cursor.getInt(cursor
                        .getColumnIndex("issms19_number")));

                isStrager = issms19_number == 1;

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        return isStrager;

    }

    public boolean isContactNumber(String remoteid) {
        String query = "SELECT * FROM " + TableName_Contacts
                + " where phonenumber='" + remoteid + "'";
        Cursor cursor = null;
        boolean isStrager = false;
        int issms19_number = 0;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                issms19_number = (cursor.getInt(cursor
                        .getColumnIndex("phonenumber")));

                isStrager = issms19_number == 1;

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        return isStrager;

    }

    public synchronized String updateContactdata(String remote_jid,
                                                 String status, byte[] avatar, String picname, int changelastseen,
                                                 boolean islastSeenUpdate) { // its make synchronized because
        // different thread accessing the
        // database at the same time on thats
        // case through exception

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        String lastseen = Utils.currentTimeStap();

        String Where = "remote_jid='" + remote_jid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("status", status);
        if (changelastseen == 1) {
            if (islastSeenUpdate) {// update 28march
                newValues.put("lastseen", lastseen);
                Utils.printLog(" lastSeenUpdate---" + lastseen + ""
                        + " --remote_jid--   " + remote_jid);

            }
        }
        if (avatar != null) {
            newValues.put("profilepic_data", avatar);
            newValues.put("profilepic_name", picname);
        }

        int id = myDataBase.update(TableName_Contacts, newValues, Where, null);

        return formattedDate + "," + id;

    }

    public String offlineStatusSet(String remote_jid, String status) {

        try {
            String Where = "remote_jid='" + remote_jid + "'";
            ContentValues newValues = new ContentValues();
            newValues.put("status", status);

            int id = myDataBase.update(TableName_Contacts, newValues, Where,
                    null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
        // Cursor cursor = myDataBase.rawQuery(query, null);

    }

    public void updateUserDatainDb(String id, String vcard_name,
                                   byte[] byteArray) {
        String WHERE = "jid='" + id + "'";
        ContentValues valuesFor_userData = new ContentValues();
        String displayname = vcard_name;
        if (byteArray != null) {

            valuesFor_userData.put("profilepic_data", byteArray);

        }
        if (displayname.trim().length() != 0) {
            valuesFor_userData.put("display_name", displayname);
        }

        myDataBase.update(TableName_UserProfile, valuesFor_userData, WHERE,
                null);
        // After_register.mydetail = getUserDatafromDB(); // comment m

    }

    public void reportSpamUpdate(String jid) {
        String WHERE = "remote_jid='" + jid + "'";
        ContentValues valuesFor_userData = new ContentValues();

        valuesFor_userData.put("isReportSpam", 1);

        myDataBase.update(TableName_Contacts, valuesFor_userData, WHERE, null);
        // After_register.mydetail = getUserDatafromDB(); // comment m

    }

    public boolean isReportSpamAlredy(String jid) {
        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + jid + "'" + " AND isReportSpam=1";
        Cursor cursor = null;
        boolean already = false;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            already = true;

        }
        if (cursor != null) {
            cursor.close();
        }
        return already;
    }

    public void saveUserDatainDb(SharedPreferences chatPrefs,
                                 String vcard_name, String pname, byte[] byteArray) {
        // TODO Auto-generated method stub
        try {
            myDataBase.delete(TableName_UserProfile, null, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ContentValues valuesFor_userData = new ContentValues();
        String displayname = vcard_name;
        String userid = chatPrefs.getString("userId", "");
        String user_jid = chatPrefs.getString("user_jid", "");
        String user_number = chatPrefs.getString("userNumber", "");
        String profilepic_name = pname;
        byte[] profilepic_data = byteArray;

        valuesFor_userData.put("user_id", userid);
        valuesFor_userData.put("status", "");
        if (profilepic_name.trim().length() != 0) {
            valuesFor_userData.put("profilepic_name", profilepic_name);
        }
        if (byteArray != null) {
            valuesFor_userData.put("profilepic_data", profilepic_data);

        }
        valuesFor_userData.put("phonenumber", user_number);
        valuesFor_userData.put("jid", user_jid);
        if (displayname.trim().length() != 0) {
            valuesFor_userData.put("display_name", displayname);
        }
        myDataBase.insert(TableName_UserProfile, null, valuesFor_userData);

    }

    public Contactmodel getUserDatafromDB() {
        // TODO Auto-generated method stub
        String query = "SELECT * FROM " + TableName_UserProfile;
        Cursor cursor = null;
        Contactmodel Mymodel = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Mymodel = new Contactmodel();
                Mymodel.setName(cursor.getString(cursor
                        .getColumnIndex("display_name")));
                Mymodel.setAvatar(cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data")));
                Mymodel.setImageUrl(cursor.getString(cursor
                        .getColumnIndex("profilepic_name")));
                Mymodel.setRemote_jid(cursor.getString(cursor
                        .getColumnIndex("jid")));
                Mymodel.setNumber(cursor.getString(cursor
                        .getColumnIndex("phonenumber")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            cursor.close();
        }

        return Mymodel;

    }

    public ArrayList<Contactmodel> getContactfromDBOnlyRegisterAndStrager() {

        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();
        String query = "SELECT rowid,* FROM " + TableName_Contacts
                + " where isregister=1 AND isgroup=0 ";
        String userOwenJid = "";
        if (InAppMessageActivity.myModel != null) {
            userOwenJid = InAppMessageActivity.myModel.getRemote_jid();
        }

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Contactmodel model = new Contactmodel();
                model.setName(cursor.getString(cursor
                        .getColumnIndex("display_name")));
                try {

                    model.setRow_id(String.valueOf(cursor.getLong(cursor
                            .getColumnIndex("rowid"))));

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                model.setRemote_jid(cursor.getString(cursor
                        .getColumnIndex("remote_jid")));
                model.setStatus(cursor.getString(cursor
                        .getColumnIndex("status")));
                model.setNumber(cursor.getString(cursor
                        .getColumnIndex("phonenumber")));
                model.setCustomStatus(cursor.getString(cursor
                        .getColumnIndex("status_custom")));
                String lastseen = cursor.getString(cursor
                        .getColumnIndex("lastseen"));
                model.setIsRegister(cursor.getInt(cursor
                        .getColumnIndex("isregister")));
                if (lastseen != null) {
                    model.setLastseen(lastseen);
                }
                model.setIsUserblock(cursor.getInt(cursor
                        .getColumnIndex("isuserblock")));

                String avatarName = cursor.getString(cursor
                        .getColumnIndex("profilepic_name"));
                byte[] pic = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));
                if (pic != null) {
                    model.setAvatar(pic);
                }
                model.setImageUrl(avatarName);
                model.setIsStranger(cursor.getInt(cursor
                        .getColumnIndex("isstranger")));

                model.setIsUserblock(cursor.getInt(cursor
                        .getColumnIndex("isuserblock")));
                if ((!userOwenJid.trim().equalsIgnoreCase(
                        cursor.getString(cursor.getColumnIndex("remote_jid"))
                                .trim()))) {
                    if (!hashMap.containsKey(model.getRemote_jid())) {
                        hashMap.put(model.getRemote_jid(), model);
                        arrayListOfContacts.add(model);
                    } else {
                        try {
                            // deleteContactDisplayName(model.getName());
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }

            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }
        return arrayListOfContacts;
    }

    public ArrayList<Contactmodel> getContactfromDBOnlyRegister() {

        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();
        String query = "SELECT rowid, * FROM " + TableName_Contacts
                + " where isregister=1 AND isgroup=0 AND isstranger=0";
        String userOwenJid = "";
        if (InAppMessageActivity.myModel != null) {
            userOwenJid = InAppMessageActivity.myModel.getRemote_jid();
        }

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Contactmodel model = new Contactmodel();
                model.setName(cursor.getString(cursor
                        .getColumnIndex("display_name")));
                try {
                    model.setRow_id(String.valueOf(cursor.getLong(cursor
                            .getColumnIndex("rowid"))));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                model.setRemote_jid(cursor.getString(cursor
                        .getColumnIndex("remote_jid")));
                model.setStatus(cursor.getString(cursor
                        .getColumnIndex("status")));
                model.setNumber(cursor.getString(cursor
                        .getColumnIndex("phonenumber")));
                model.setCustomStatus(cursor.getString(cursor
                        .getColumnIndex("status_custom")));
                String lastseen = cursor.getString(cursor
                        .getColumnIndex("lastseen"));
                model.setIsRegister(cursor.getInt(cursor
                        .getColumnIndex("isregister")));
                if (lastseen != null) {
                    model.setLastseen(lastseen);
                }
                model.setIsUserblock(cursor.getInt(cursor
                        .getColumnIndex("isuserblock")));

                String avatarName = cursor.getString(cursor
                        .getColumnIndex("profilepic_name"));
                byte[] pic = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));
                if (pic != null) {
                    model.setAvatar(pic);
                }
                model.setImageUrl(avatarName);
                model.setIsStranger(cursor.getInt(cursor
                        .getColumnIndex("isstranger")));

                model.setIsUserblock(cursor.getInt(cursor
                        .getColumnIndex("isuserblock")));
                if ((!userOwenJid.trim().equalsIgnoreCase(
                        cursor.getString(cursor.getColumnIndex("remote_jid"))
                                .trim()))) {
                    if (!hashMap.containsKey(model.getRemote_jid())) {
                        hashMap.put(model.getRemote_jid(), model);
                        arrayListOfContacts.add(model);
                    } else {
                        try {
                            // deleteContactDisplayName(model.getName());
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }

            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }
        return arrayListOfContacts;
    }

    // satyajit
    public ArrayList<Contactmodel> getContactForLoadMoreFromDBRegister(
            String previousrowid) {

        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();

        String userOwenJid = "";
        if (InAppMessageActivity.myModel != null) {
            userOwenJid = InAppMessageActivity.myModel.getRemote_jid();
        }

        String query = "SELECT rowid, * FROM " + TableName_Contacts
                + " where isregister=1 AND isstranger=0 AND isgroup=0";
        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);

            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    Contactmodel model = new Contactmodel();
                    model.setName(cursor.getString(cursor
                            .getColumnIndex("display_name")));
                    try {
                        model.setRow_id(String.valueOf(cursor.getLong(cursor
                                .getColumnIndex("rowid"))));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    model.setRemote_jid(cursor.getString(cursor
                            .getColumnIndex("remote_jid")));
                    model.setStatus(cursor.getString(cursor
                            .getColumnIndex("status")));
                    model.setNumber(cursor.getString(cursor
                            .getColumnIndex("phonenumber")));
                    model.setCustomStatus(cursor.getString(cursor
                            .getColumnIndex("status_custom")));
                    String lastseen = cursor.getString(cursor
                            .getColumnIndex("lastseen"));
                    model.setIsRegister(cursor.getInt(cursor
                            .getColumnIndex("isregister")));
                    if (lastseen != null) {
                        model.setLastseen(lastseen);
                    }
                    model.setIsUserblock(cursor.getInt(cursor
                            .getColumnIndex("isuserblock")));

                    String avatarName = cursor.getString(cursor
                            .getColumnIndex("profilepic_name"));
                    byte[] pic = cursor.getBlob(cursor
                            .getColumnIndex("profilepic_data"));
                    if (pic != null) {
                        model.setAvatar(pic);
                    }
                    model.setImageUrl(avatarName);
                    model.setIsStranger(cursor.getInt(cursor
                            .getColumnIndex("isstranger")));

                    model.setIsUserblock(cursor.getInt(cursor
                            .getColumnIndex("isuserblock")));
                    if ((!userOwenJid
                            .trim()
                            .equalsIgnoreCase(
                                    cursor.getString(
                                            cursor.getColumnIndex("remote_jid"))
                                            .trim()))) {
                        if (!hashMap.containsKey(model.getRemote_jid())) {
                            hashMap.put(model.getRemote_jid(), model);
                            arrayListOfContacts.add(model);
                        } else {
                            try {
                                // deleteContactDisplayName(model.getName());
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }
        return arrayListOfContacts;
    }

    public ArrayList<Contactmodel> getContactfromDBOnlySMS19RowBase(
            String previousrowid) {
        // TODO Auto-generated method stub
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();

        String userOwenJid = "";
        if (InAppMessageActivity.myModel != null) {
            userOwenJid = InAppMessageActivity.myModel.getRemote_jid();
        }

        String query = "";

        String afterrowid = "";

        if (previousrowid.equals("0")) {

            afterrowid = getMaxRegisteContactRowid("");// getMaxSMS19_ContactsRowid("");

        } else {
            afterrowid = previousrowid;
        }

        // ArrayList<Chatmodel> arrayListOfchats = new ArrayList<Chatmodel>();
        if (afterrowid.trim().length() != 0) {

            if (previousrowid.equals("0")) {
                query = "SELECT rowid,* FROM "
                        + TableName_Contacts
                        + " where  isregister='0' AND isstranger='0' AND isgroup='0' AND  rowid <='"
                        + afterrowid + "' ORDER BY rowid DESC limit 50";
            } else {
                query = "SELECT rowid,* FROM "
                        + TableName_Contacts
                        + " where isregister='0'  AND isstranger='0' AND isgroup='0' AND rowid <'"
                        + afterrowid + "' ORDER BY rowid DESC limit 50";
            }

        }

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Contactmodel model = new Contactmodel();
                model.setName(cursor.getString(cursor
                        .getColumnIndex("display_name")));
                model.setRemote_jid(cursor.getString(cursor
                        .getColumnIndex("remote_jid")));
                model.setStatus(cursor.getString(cursor
                        .getColumnIndex("status")));
                model.setNumber(cursor.getString(cursor
                        .getColumnIndex("phonenumber")));
                model.setCustomStatus(cursor.getString(cursor
                        .getColumnIndex("status_custom")));

                try {
                    model.setRow_id(String.valueOf(cursor.getLong(cursor
                            .getColumnIndex("rowid"))));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String lastseen = cursor.getString(cursor
                        .getColumnIndex("lastseen"));
                model.setIsRegister(cursor.getInt(cursor
                        .getColumnIndex("isregister")));
                if (lastseen != null) {
                    model.setLastseen(lastseen);
                }
                model.setIsUserblock(cursor.getInt(cursor
                        .getColumnIndex("isuserblock")));

                String avatarName = cursor.getString(cursor
                        .getColumnIndex("profilepic_name"));
                byte[] pic = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));
                if (pic != null) {
                    model.setAvatar(pic);
                }
                model.setImageUrl(avatarName);
                model.setIsStranger(cursor.getInt(cursor
                        .getColumnIndex("isstranger")));

                model.setIsUserblock(cursor.getInt(cursor
                        .getColumnIndex("isuserblock")));
                if ((!userOwenJid.trim().equalsIgnoreCase(
                        cursor.getString(cursor.getColumnIndex("remote_jid"))
                                .trim()))) {
                    if (!hashMap.containsKey(model.getRemote_jid())) {
                        hashMap.put(model.getRemote_jid(), model);
                        arrayListOfContacts.add(model);
                    }

                }

            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }
        return arrayListOfContacts;
    }

    public ArrayList<Contactmodel> getContactfromDBOnlySMS19RowBase2(
            String previousrowid) {
        // TODO Auto-generated method stub
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();

        String userOwenJid = "";
        if (InAppMessageActivity.myModel != null) {
            userOwenJid = InAppMessageActivity.myModel.getRemote_jid();
        }

        // String query = "";

        String afterrowid = "";

        if (previousrowid.equals("0")) {

            afterrowid = getMaxRegisteContactRowid("");// getMaxSMS19_ContactsRowid("");

        } else {
            afterrowid = previousrowid;
        }

        // ArrayList<Chatmodel> arrayListOfchats = new ArrayList<Chatmodel>();
		/*
		 * if (afterrowid.trim().length() != 0) {
		 *
		 *
		 *
		 * if (previousrowid.equals("0")) { query = "SELECT rowid,* FROM " +
		 * TableName_Contacts +
		 * " where  isregister='0' AND isstranger='0' AND isgroup='0' AND  rowid <='"
		 * + afterrowid + "' ORDER BY rowid DESC limit 50"; } else { query =
		 * "SELECT rowid,* FROM " + TableName_Contacts +
		 * " where isregister='0'  AND isstranger='0' AND isgroup='0' AND rowid <'"
		 * + afterrowid + "' ORDER BY rowid DESC limit 50"; }
		 *
		 *
		 *
		 * }
		 */

        String query = "SELECT rowid,* FROM " + TableName_Contacts
                + " where isregister=0 AND isstranger=0 AND isgroup=0";

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Contactmodel model = new Contactmodel();
                model.setName(cursor.getString(cursor
                        .getColumnIndex("display_name")));
                model.setRemote_jid(cursor.getString(cursor
                        .getColumnIndex("remote_jid")));
                model.setStatus(cursor.getString(cursor
                        .getColumnIndex("status")));
                model.setNumber(cursor.getString(cursor
                        .getColumnIndex("phonenumber")));
                model.setCustomStatus(cursor.getString(cursor
                        .getColumnIndex("status_custom")));

                try {
                    model.setRow_id(String.valueOf(cursor.getLong(cursor
                            .getColumnIndex("rowid"))));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String lastseen = cursor.getString(cursor
                        .getColumnIndex("lastseen"));
                model.setIsRegister(cursor.getInt(cursor
                        .getColumnIndex("isregister")));
                if (lastseen != null) {
                    model.setLastseen(lastseen);
                }
                model.setIsUserblock(cursor.getInt(cursor
                        .getColumnIndex("isuserblock")));

                String avatarName = cursor.getString(cursor
                        .getColumnIndex("profilepic_name"));
                byte[] pic = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));
                if (pic != null) {
                    model.setAvatar(pic);
                }
                model.setImageUrl(avatarName);
                model.setIsStranger(cursor.getInt(cursor
                        .getColumnIndex("isstranger")));

                model.setIsUserblock(cursor.getInt(cursor
                        .getColumnIndex("isuserblock")));
                if ((!userOwenJid.trim().equalsIgnoreCase(
                        cursor.getString(cursor.getColumnIndex("remote_jid"))
                                .trim()))) {
                    if (!hashMap.containsKey(model.getRemote_jid())) {
                        hashMap.put(model.getRemote_jid(), model);
                        arrayListOfContacts.add(model);
                    }

                }

            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }
        return arrayListOfContacts;
    }

    public ArrayList<Contactmodel> getContactfromDBSMS19All(String previousrowid) {
        // TODO Auto-generated method stub
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();

        String userOwenJid = "";
        if (InAppMessageActivity.myModel != null) {
            userOwenJid = InAppMessageActivity.myModel.getRemote_jid();
        }

        String query = "";

        String afterrowid = "";
        // TODO Auto-generated method stub
        if (previousrowid.equals("0")) {
            afterrowid = getMaxSMS19_ContactsRowid("");
        } else {
            afterrowid = previousrowid;
        }

        ArrayList<Chatmodel> arrayListOfchats = new ArrayList<Chatmodel>();
        if (afterrowid.trim().length() != 0) {

            if (previousrowid.equals("0")) {
                query = "SELECT rowid,* FROM " + TableName_SMS19_Contacts
                        + " where  rowid <='" + afterrowid
                        + "' ORDER BY rowid ASC ";
            } else {
                query = "SELECT rowid,* FROM " + TableName_SMS19_Contacts
                        + " where  rowid >'" + afterrowid
                        + "' ORDER BY rowid ASC limit 10";
            }
        }
        // SELECT rowid,* FROM Sms19_Contacts where rowid <='24' ORDER BY rowid
        // ASC limit 25

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Contactmodel model = new Contactmodel();
                model.setName(cursor.getString(cursor
                        .getColumnIndex("display_name")));
                model.setRemote_jid(cursor.getString(cursor
                        .getColumnIndex("remote_jid")));
                model.setStatus(cursor.getString(cursor
                        .getColumnIndex("status")));
                model.setNumber(cursor.getString(cursor
                        .getColumnIndex("phonenumber")));
                model.setCustomStatus(cursor.getString(cursor
                        .getColumnIndex("status_custom")));

                try {
                    model.setRow_id(String.valueOf(cursor.getLong(cursor
                            .getColumnIndex("rowid"))));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String lastseen = cursor.getString(cursor
                        .getColumnIndex("lastseen"));
                model.setIsRegister(cursor.getInt(cursor
                        .getColumnIndex("isregister")));
                if (lastseen != null) {
                    model.setLastseen(lastseen);
                }
                model.setIsUserblock(cursor.getInt(cursor
                        .getColumnIndex("isuserblock")));

                String avatarName = cursor.getString(cursor
                        .getColumnIndex("profilepic_name"));
                byte[] pic = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));
                if (pic != null) {
                    model.setAvatar(pic);
                }
                model.setImageUrl(avatarName);
                model.setIsStranger(cursor.getInt(cursor
                        .getColumnIndex("isstranger")));

                model.setIsUserblock(cursor.getInt(cursor
                        .getColumnIndex("isuserblock")));
                if ((!userOwenJid.trim().equalsIgnoreCase(
                        cursor.getString(cursor.getColumnIndex("remote_jid"))
                                .trim()))) {
                    if (!hashMap.containsKey(model.getRemote_jid())) {
                        hashMap.put(model.getRemote_jid(), model);
                        arrayListOfContacts.add(model);
                    }

                }

            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }
        return arrayListOfContacts;
    }


    public ArrayList<Contactmodel> getContactfromDBUnregister() {
        // TODO Auto-generated method stub
		/*
		 * ArrayList<Contactmodel> arrayListOfContacts = new
		 * ArrayList<Contactmodel>(); String query = "SELECT * FROM " +
		 * TableName_Contacts +
		 * " where isregister=1 AND isstranger=0 AND isgroup=0";
		 */
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        String query = "SELECT * FROM " + TableName_Contacts
                + " where isregister=0 AND isstranger=0";

        HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();

        String userOwenJid = "";
        if (InAppMessageActivity.myModel != null) {
            userOwenJid = InAppMessageActivity.myModel.getRemote_jid();
        }

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Contactmodel model = new Contactmodel();
                model.setName(cursor.getString(cursor
                        .getColumnIndex("display_name")));
                model.setRemote_jid(cursor.getString(cursor
                        .getColumnIndex("remote_jid")));
                model.setStatus(cursor.getString(cursor
                        .getColumnIndex("status")));
                model.setCustomStatus(cursor.getString(cursor
                        .getColumnIndex("status_custom")));
                model.setNumber(cursor.getString(cursor
                        .getColumnIndex("phonenumber")));
                String lastseen = cursor.getString(cursor
                        .getColumnIndex("lastseen"));
                if (lastseen != null) {
                    model.setLastseen(lastseen);
                }
                model.setIsRegister(cursor.getInt(cursor
                        .getColumnIndex("isregister")));
                String avatarName = cursor.getString(cursor
                        .getColumnIndex("profilepic_name"));
                byte[] pic = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));
                if (pic != null) {
                    model.setAvatar(pic);
                }
                model.setImageUrl(avatarName);
                model.setIsStranger(cursor.getInt(cursor
                        .getColumnIndex("isstranger")));
                model.setIsRegister(cursor.getInt(cursor
                        .getColumnIndex("isregister")));

                if ((!userOwenJid.trim().equalsIgnoreCase(
                        cursor.getString(cursor.getColumnIndex("remote_jid"))
                                .trim()))) {
                    if (!hashMap.containsKey(model.getRemote_jid())) {
                        hashMap.put(model.getRemote_jid(), model);
                        arrayListOfContacts.add(model);
                    }

                }

            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }
        return arrayListOfContacts;
    }

    public ArrayList<Contactmodel> getContactfromDB() {
        // TODO Auto-generated method stub
		/*
		 * ArrayList<Contactmodel> arrayListOfContacts = new
		 * ArrayList<Contactmodel>(); String query = "SELECT * FROM " +
		 * TableName_Contacts +
		 * " where isregister=1 AND isstranger=0 AND isgroup=0";
		 */
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        String query = "SELECT * FROM " + TableName_Contacts
                + " where isregister=1 AND isgroup=0";

        HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Contactmodel model = new Contactmodel();
                model.setName(cursor.getString(cursor
                        .getColumnIndex("display_name")));
                model.setRemote_jid(cursor.getString(cursor
                        .getColumnIndex("remote_jid")));
                model.setStatus(cursor.getString(cursor
                        .getColumnIndex("status")));
                model.setCustomStatus(cursor.getString(cursor
                        .getColumnIndex("status_custom")));
                String lastseen = cursor.getString(cursor
                        .getColumnIndex("lastseen"));
                if (lastseen != null) {
                    model.setLastseen(lastseen);
                }

                String avatarName = cursor.getString(cursor
                        .getColumnIndex("profilepic_name"));
                byte[] pic = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));
                if (pic != null) {
                    model.setAvatar(pic);
                }
                model.setImageUrl(avatarName);
                model.setIsStranger(cursor.getInt(cursor
                        .getColumnIndex("isstranger")));
                model.setIsRegister(cursor.getInt(cursor
                        .getColumnIndex("isregister")));
                if (!model.getRemote_jid().equalsIgnoreCase(
                        InAppMessageActivity.myModel.getRemote_jid())) {

                    if (!hashMap.containsKey(model.getRemote_jid())) {
                        hashMap.put(model.getRemote_jid(), model);
                        arrayListOfContacts.add(model);
                    }

                }

            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }
        return arrayListOfContacts;
    }

    public void deleteSinglemsgrow(String rowid) {
        myDataBase
                .delete(TableName_Messages, "rowid=?", new String[]{rowid});

    }

    public void deleteSingleOfflinemsgrow(String rowid) {
        try {
            myDataBase.delete(TableName_Offline_Messages, "rowid=?",
                    new String[]{rowid});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Utils.printLog("deleteSingleOfflinemsgrow  failed==="
                    + String.valueOf(rowid));
            e.printStackTrace();
        }

    }

    public void deleteGroupParticularrow(String jid) {
        myDataBase
                .delete(TableName_Groups, "group_jid=?", new String[]{jid});

    }

    public int deleteContact(String selection, String[] selectionArgs) {
		/*
		 * myDataBase .delete(TableName_Contacts, "rowid=?", new String[] {
		 * rowid });
		 */

       /* String query = "delete  from " + TableName_Contacts
                + " where phonenumber='" + rowid + "'";*/
//        myDataBase.execSQL(query);
        return myDataBase.delete(TableName_Contacts, "phonenumber=?", selectionArgs);
    }


    public void deleteContactFromSMS19(String rowid) {
		/*
		 * myDataBase .delete(TableName_Contacts, "rowid=?", new String[] {
		 * rowid });
		 */

        String query = "delete  from " + TableName_Contacts
                + " where remote_jid='" + rowid + "'";
        myDataBase.execSQL(query);

    }

    public void deleteContactFromContact(String rowid) {
		/*
		 * myDataBase .delete(TableName_Contacts, "rowid=?", new String[] {
		 * rowid });
		 */

        String query = "delete  from " + TableName_Contacts
                + " where remote_jid='" + rowid + "'";
        myDataBase.execSQL(query);

    }

    public void DeleteContactRemoteIdBase(String rowid) {
        myDataBase.delete(TableName_Contacts, "remote_jid=?",
                new String[]{rowid});
    }

    public void deleteRecentParticularrow(String jid) {
        myDataBase.delete(TableName_Recent, "remote_jid=?",
                new String[]{jid});

    }

	/*
	 * public void updateRecentTableLastMsg(String remote_jid,String
	 * previous_recent_msg,String time) { try{ String query =
	 * "UPDATE Contacts SET last_msg ='" + previous_recent_msg +
	 * "' WHERE remote_jid ='" + remote_jid + "'"; myDataBase.execSQL(query);
	 *
	 * } catch (Exception e) { e.printStackTrace(); } }
	 */

    public String getmaxrowid(String remoteid) {
        String rowid = "";
        // SELECT max(rowid), * FROM Messages where
        // remote_jid='+919358251234@128.199.155.62'
        String query = "SELECT max(rowid),* FROM " + TableName_Messages
                + " where remote_jid='" + remoteid + "'";
        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                rowid = String.valueOf(cursor.getLong(cursor
                        .getColumnIndex("max(rowid)")));
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowid;
    }


    public String getMaxRegisteContactRowid(String remoteid) {
        String rowid = "";

        String query = "SELECT max(rowid),* FROM " + TableName_Contacts;
        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                rowid = String.valueOf(cursor.getLong(cursor
                        .getColumnIndex("max(rowid)")));
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowid;
    }

    public String getMaxSMS19_ContactsRowid(String remoteid) {
        String rowid = "";

        String query = "SELECT max(rowid),* FROM " + TableName_SMS19_Contacts;
        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                rowid = String.valueOf(cursor.getLong(cursor
                        .getColumnIndex("max(rowid)")));
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowid;
    }

    public int getHistorycount(String remoteid) {
        int count = 0;
        String query = "SELECT * FROM " + TableName_Messages
                + " where remote_jid='" + remoteid + "'";
        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                count = cursor.getCount();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    public ArrayList<Chatmodel> getChathistoryfromDBOfMedia(String remoteid,
                                                            String previousrowid) {
        dateSeperatorValue = "";
        String afterrowid = "";
        // TODO Auto-generated method stub
        if (previousrowid.equals("0")) {
            afterrowid = getmaxrowid(remoteid);
        } else {
            afterrowid = previousrowid;
        }

        ArrayList<Chatmodel> arrayListOfchats = new ArrayList<Chatmodel>();
        // if (afterrowid.trim().length() != 0) {

		/*
		 * query = "SELECT rowid,* FROM " + TableName_Messages +
		 * " where remote_jid='" + remoteid + "' AND rowid <'" + afterrowid +
		 * "' ORDER BY rowid DESC limit 25";
		 */

        String query3 = "SELECT rowid,* FROM " + TableName_Messages
                + " where remote_jid='" + previousrowid + "'"
                + " AND media_type='" + "I'" + " OR media_type='" + "V'"
                + " OR media_type='" + "A'";

        String query = "SELECT * FROM " + TableName_Messages
                + " where remote_jid='" + previousrowid + "'AND key_from_me='"
                + remoteid + "' AND media_type='" + "I'" + " OR media_type='"
                + "V'";

        String query2 = "SELECT * FROM " + TableName_Messages
                + " where remote_jid='" + previousrowid + "'AND key_from_me='"
                + previousrowid + "' AND media_type='" + "I'"
                + " OR media_type='" + "V'";

        // String query = "SELECT * FROM Messages where
        // remote_jid='+919509274990@111.93.195.78' AND key_from_me=
        // '+917877475123@111.93.195.78' AND media_type='I'

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query3, null);
            if (cursor != null && cursor.getCount() > 0) {
                // cursor.moveToLast();
                // do {
                while (cursor.moveToNext()) {

                    Chatmodel model = new Chatmodel();

                    String mine = cursor.getString(cursor
                            .getColumnIndex("key_from_me"));
                    String remoteId = cursor.getString(cursor
                            .getColumnIndex("remote_jid"));
                    String message = cursor.getString(cursor
                            .getColumnIndex("message"));
                    String mediatype = cursor.getString(cursor
                            .getColumnIndex("media_type"));
                    String msgTime = cursor.getString(cursor
                            .getColumnIndex("msg_time"));

                    String user_jid = InAppMessageActivity.myModel
                            .getRemote_jid();
                    if (mine.equals(user_jid)) {
                        model.setMine(true);
                    } else {
                        model.setMine(false);

                        model.setRemote_userid(mine);

                    }
                    if (mediatype != null && mediatype.trim().length() != 0) {
                        model.setMediatype(mediatype);
                        String mediapath = cursor.getString(cursor
                                .getColumnIndex("media_path"));
                        if (mediatype
                                .equals(sms19.inapp.msg.constant.GlobalData.Locationfile)) {
                            model.setChatmessage(message);
                        }
                        if (mediapath != null && mediapath.trim().length() != 0) {
                            model.setMediapath(mediapath);

                        }
                        String mediaurl = cursor.getString(cursor
                                .getColumnIndex("media_url"));
                        if (mediaurl != null && mediaurl.trim().length() != 0) {
                            model.setMediaUrl(mediaurl);
                        }

                        if (model.getOrignalbitmap() == null) {
                            byte[] thmb = cursor.getBlob(cursor
                                    .getColumnIndex("media_thmb"));
                            if (thmb != null) {

                                model.setMediathmb(thmb);
                            }
                        }
                    } else {
                        model.setChatmessage(message);
                    }

                    // String rowid =
                    // String.valueOf(cursor.getLong(cursor.getColumnIndex("rowid")));
                    // model.setMessagerowid(rowid);

                    model.setMsg_packetid(cursor.getString(cursor
                            .getColumnIndex("message_packetid")));
                    model.setSent_msg_success(cursor.getString(cursor
                            .getColumnIndex("sent_msg_success")));
                    model.setDeliver_msg_success(cursor.getString(cursor
                            .getColumnIndex("deliver_msg_success")));
                    model.setRead_msg_success(cursor.getString(cursor
                            .getColumnIndex("read_msg_success")));

                    model.setMsgDateTimeCombine(msgTime);
                    model.setSeperator_line(getseperatorlineText(msgTime));
                    if (msgTime != null) {
                        try {
                            model.setMsgDate(sms19.inapp.msg.constant.Utils
                                    .getmsgDate(msgTime));
                            model.setMsgTime(sms19.inapp.msg.constant.Utils
                                    .getmsgTime(msgTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (remoteId.equalsIgnoreCase(previousrowid)) {// new
                        // dependency
                        if (new File(model.getMediapath()).exists()) {// 10March2016
                            arrayListOfchats.add(model);
                        }
                    }

                }// while (cursor.moveToPrevious());
                // while (cursor.moveToNext()) {}

            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }

        Cursor cursor2 = null;
        try {

            cursor2 = myDataBase.rawQuery(query2, null);
            if (cursor2 != null && cursor2.getCount() > 0) {
                // cursor.moveToLast();
                // do {
                while (cursor2.moveToNext()) {

                    Chatmodel model = new Chatmodel();

                    String mine = cursor2.getString(cursor
                            .getColumnIndex("key_from_me"));
                    String message = cursor2.getString(cursor
                            .getColumnIndex("message"));
                    String mediatype = cursor2.getString(cursor
                            .getColumnIndex("media_type"));
                    String msgTime = cursor2.getString(cursor
                            .getColumnIndex("msg_time"));

                    if (mine == null) {
                        mine = "";
                    }

                    String user_jid = InAppMessageActivity.myModel
                            .getRemote_jid();
                    if (mine.equalsIgnoreCase(user_jid)) {
                        model.setMine(true);
                    } else {
                        model.setMine(false);

                        model.setRemote_userid(mine);

                    }
                    if (mediatype != null && mediatype.trim().length() != 0) {
                        model.setMediatype(mediatype);
                        String mediapath = cursor2.getString(cursor
                                .getColumnIndex("media_path"));
                        if (mediatype
                                .equals(sms19.inapp.msg.constant.GlobalData.Locationfile)) {
                            model.setChatmessage(message);
                        }
                        if (mediapath != null && mediapath.trim().length() != 0) {
                            model.setMediapath(mediapath);

                        }
                        String mediaurl = cursor2.getString(cursor
                                .getColumnIndex("media_url"));
                        if (mediaurl != null && mediaurl.trim().length() != 0) {
                            model.setMediaUrl(mediaurl);
                        }

                        if (model.getOrignalbitmap() == null) {
                            byte[] thmb = cursor2.getBlob(cursor
                                    .getColumnIndex("media_thmb"));
                            if (thmb != null) {

                                model.setMediathmb(thmb);
                            }
                        }
                    } else {
                        model.setChatmessage(message);
                    }

                    // String rowid =
                    // String.valueOf(cursor.getLong(cursor.getColumnIndex("rowid")));
                    // model.setMessagerowid(rowid);

					/*
					 * model.setMsg_packetid(cursor2.getString(cursor.getColumnIndex
					 * ("message_packetid")));
					 * model.setSent_msg_success(cursor2.
					 * getString(cursor.getColumnIndex("sent_msg_success")));
					 * model.setDeliver_msg_success(cursor2.getString(cursor.
					 * getColumnIndex("deliver_msg_success")));
					 * model.setRead_msg_success
					 * (cursor2.getString(cursor.getColumnIndex
					 * ("read_msg_success")));
					 */

                    model.setMsg_packetid(cursor2.getString(cursor2
                            .getColumnIndex("message_packetid")));
                    model.setSent_msg_success(cursor2.getString(cursor2
                            .getColumnIndex("sent_msg_success")));
                    model.setDeliver_msg_success(cursor2.getString(cursor2
                            .getColumnIndex("deliver_msg_success")));
                    model.setRead_msg_success(cursor2.getString(cursor2
                            .getColumnIndex("read_msg_success")));

                    model.setMsgDateTimeCombine(msgTime);
                    model.setSeperator_line(getseperatorlineText(msgTime));
                    if (msgTime != null) {
                        try {
                            model.setMsgDate(sms19.inapp.msg.constant.Utils
                                    .getmsgDate(msgTime));
                            model.setMsgTime(sms19.inapp.msg.constant.Utils
                                    .getmsgTime(msgTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (model.getMediathmb() != null) {
                        // arrayListOfchats.add(model);
                    }

                }// while (cursor.moveToPrevious());
                // while (cursor.moveToNext()) {}

            }
            if (cursor2 != null) {
                cursor2.close();
            }

        } catch (Exception e) {
            if (cursor2 != null) {
                cursor2.close();
            }
            e.printStackTrace();
        }
        // }
        return arrayListOfchats;
    }

    // delete the history of particular user
    public void deleteParticularUserHistory(String remoteid) {
        String query = "delete  from " + TableName_Messages
                + " where remote_jid='" + remoteid + "'";
        myDataBase.execSQL(query);
    }

    // delete the recent of particular user
    public void deleteParticularUserRecentMsg(String remoteid) {
        String query = "delete  from " + TableName_Recent
                + " where remote_jid='" + remoteid + "'";
        myDataBase.execSQL(query);
    }

    public ArrayList<Chatmodel> getChathistoryfromDB(String remoteid,
                                                     String previousrowid, String msg_time) {
        dateSeperatorValue = "";

        ArrayList<Chatmodel> arrayListOfchats = new ArrayList<Chatmodel>();

        String query = "";

        query = "SELECT rowid,* FROM " + TableName_Messages
                + " where remote_jid='" + remoteid + "' AND msg_time <'"
                + msg_time + "' ORDER BY msg_time DESC limit 50";

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToLast();
                do {

                    Chatmodel model = new Chatmodel();

                    String mine = cursor.getString(cursor
                            .getColumnIndex("key_from_me"));
                    String message = cursor.getString(cursor
                            .getColumnIndex("message"));
                    String mediatype = cursor.getString(cursor
                            .getColumnIndex("media_type"));
                    String msgTime = cursor.getString(cursor
                            .getColumnIndex("msg_time"));
                    String mggtime_for_sort = cursor.getString(cursor
                            .getColumnIndex("msg_time"));

                    String media_name = cursor.getString(cursor
                            .getColumnIndex("media_name"));

                    try {
                        String expiry_time = cursor.getString(cursor
                                .getColumnIndex("expiry_time"));
                        model.setExpiry_time(expiry_time);

                    } catch (Exception e3) {
                        // TODO Auto-generated catch block
                        e3.printStackTrace();
                    }
                    try {
                        String message_id = cursor.getString(cursor
                                .getColumnIndex("message_id"));
                        model.setMessage_id(message_id);

                    } catch (Exception e3) {
                        // TODO Auto-generated catch block
                        e3.printStackTrace();
                    }

                    try {
                        String MessageStatus = cursor.getString(cursor
                                .getColumnIndex("MessageStatus"));
                        model.setMessageStatus(MessageStatus);

                    } catch (Exception e3) {
                        // TODO Auto-generated catch block
                        e3.printStackTrace();
                    }

                    try {
                        int isBroadCast = cursor.getInt(cursor
                                .getColumnIndex("isBroadCast"));
                        model.setIsBroadCast(isBroadCast);

                        int inAppCount = cursor.getInt(cursor
                                .getColumnIndex("inAppCount"));
                        model.setInApMsgCount(inAppCount);

                        int inInboxCount = cursor.getInt(cursor
                                .getColumnIndex("inInboxCount"));
                        model.setInInboxCount(inInboxCount);

                        int mail_count = cursor.getInt(cursor.getColumnIndex("mail_count"));
                        model.setMail_count(mail_count);

                    } catch (Exception e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }

                    String user_jid = SingleChatRoomFrgament.myModel
                            .getRemote_jid().trim();
                    if (mine.equals(user_jid)) {
                        model.setMine(true);
                        model.setStatus(media_name);

                        try {
                            int istry = cursor.getInt(cursor
                                    .getColumnIndex("isretry"));

                            model.setIsretry(istry);
                        } catch (Exception e2) {
                            // TODO Auto-generated catch block
                            e2.printStackTrace();
                        }

                    } else {
                        model.setMine(false);
                        model.setRemote_userid(mine);
                    }
                    if (mediatype != null && mediatype.trim().length() != 0) {
                        model.setMediatype(mediatype);
                        String mediapath = cursor.getString(cursor
                                .getColumnIndex("media_path"));
                        if (mediatype
                                .equals(sms19.inapp.msg.constant.GlobalData.Locationfile)) {
                            model.setChatmessage(message);
                        }
                        if (mediapath != null && mediapath.trim().length() != 0) {
                            model.setMediapath(mediapath);

                        }
                        String mediaurl = cursor.getString(cursor
                                .getColumnIndex("media_url"));
                        if (mediaurl != null && mediaurl.trim().length() != 0) {
                            model.setMediaUrl(mediaurl);
                        }

                        if (model.getOrignalbitmap() == null) {
                            byte[] thmb = cursor.getBlob(cursor
                                    .getColumnIndex("media_thmb"));
                            if (thmb != null) {

                                model.setMediathmb(thmb);
                            }
                        }
                    } else {
                        model.setChatmessage(message);
                    }

                    String rowid = "";

                    try {
                        rowid = String.valueOf(cursor.getLong(cursor
                                .getColumnIndex("rowid")));
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        rowid = "";
                        e1.printStackTrace();
                    }

                    model.setMessagerowid(rowid);

                    model.setMsg_packetid(cursor.getString(cursor
                            .getColumnIndex("message_packetid")));
                    model.setSent_msg_success(cursor.getString(cursor
                            .getColumnIndex("sent_msg_success")));
                    model.setDeliver_msg_success(cursor.getString(cursor
                            .getColumnIndex("deliver_msg_success")));
                    model.setRead_msg_success(cursor.getString(cursor
                            .getColumnIndex("read_msg_success")));

                    model.setMsgDateTimeCombine(msgTime);
                    model.setSeperator_line(getseperatorlineText(msgTime));
                    if (msgTime != null) {
                        try {
                            model.setMsgDate(sms19.inapp.msg.constant.Utils
                                    .getmsgDate(msgTime));
                            model.setMsgTime(sms19.inapp.msg.constant.Utils
                                    .getmsgTime(msgTime));
                            model.setMsgtime(mggtime_for_sort);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    arrayListOfchats.add(model);

                } while (cursor.moveToPrevious());
                // while (cursor.moveToNext()) {}
            }

            if (arrayListOfchats.size() > 0) {

                Collections.sort(arrayListOfchats,
                        new Chatmodel().new CustomComparatorSortByTime());
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }

        return arrayListOfchats;
    }

    public int getChathistoryTotalCountfromDB(String remoteid) {
        dateSeperatorValue = "";
        String afterrowid = "";
        int count = 0;
        // TODO Auto-generated method stub

        String query = "";

        query = "SELECT rowid,* FROM " + TableName_Messages
                + " where remote_jid='" + remoteid + "' ";

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);

            count = cursor.getCount();

            if (cursor != null && cursor.getCount() > 0) {
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }

        return count;
    }

    public ArrayList<Chatmodel> getChathistoryfromDBOffline(String remoteid,
                                                            String previousrowid) {
        dateSeperatorValue = "";

        ArrayList<Chatmodel> arrayListOfchats = new ArrayList<Chatmodel>();

        String query = "";

        query = "SELECT rowid,* FROM " + TableName_Offline_Messages
                + " where remote_jid_sender='" + remoteid + "'";

		/*
		 * query = "SELECT rowid,* FROM " + TableName_Offline_Messages +
		 * " where remote_jid='" + remoteid + "'";
		 */

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToLast();
                do {

                    Chatmodel model = new Chatmodel();

                    String mine = cursor.getString(cursor
                            .getColumnIndex("key_from_me"));
                    String message = cursor.getString(cursor
                            .getColumnIndex("message"));
                    String mediatype = cursor.getString(cursor
                            .getColumnIndex("media_type"));
                    String msgTime = cursor.getString(cursor
                            .getColumnIndex("msg_time"));
                    String msgTimeNew = cursor.getString(cursor
                            .getColumnIndex("msg_time"));
                    String remoteId = cursor.getString(cursor
                            .getColumnIndex("remote_jid"));
                    model.setRemote_userid(remoteId);

                    try {
                        String row_offlineId = cursor.getString(cursor
                                .getColumnIndex("row_offline_id"));
                        model.setRow_offlineId(row_offlineId);
                    } catch (Exception e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }

                    String user_jid = remoteid;
                    if (mine.equals(user_jid)) {
                        model.setMine(true);
                    } else {
                        model.setMine(false);

                        model.setRemote_userid(mine);

                    }
                    if (mediatype != null && mediatype.trim().length() != 0) {
                        model.setMediatype(mediatype);
                        String mediapath = cursor.getString(cursor
                                .getColumnIndex("media_path"));
                        if (mediatype
                                .equals(sms19.inapp.msg.constant.GlobalData.Locationfile)) {
                            model.setChatmessage(message);
                        }
                        if (mediapath != null && mediapath.trim().length() != 0) {
                            model.setMediapath(mediapath);

                        }
                        String mediaurl = cursor.getString(cursor
                                .getColumnIndex("media_url"));
                        if (mediaurl != null && mediaurl.trim().length() != 0) {
                            model.setMediaUrl(mediaurl);
                        }

                        if (model.getOrignalbitmap() == null) {
                            byte[] thmb = cursor.getBlob(cursor
                                    .getColumnIndex("media_thmb"));
                            if (thmb != null) {

                                model.setMediathmb(thmb);
                            }
                        }
                    } else {
                        model.setChatmessage(message);
                    }

                    String rowid = "";

                    try {
                        rowid = String.valueOf(cursor.getLong(cursor
                                .getColumnIndex("rowid")));
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        rowid = "";
                        e1.printStackTrace();
                    }
                    model.setMessagerowid(rowid);

                    model.setMsg_packetid(cursor.getString(cursor
                            .getColumnIndex("message_packetid")));
                    model.setSent_msg_success(cursor.getString(cursor
                            .getColumnIndex("sent_msg_success")));
                    model.setDeliver_msg_success(cursor.getString(cursor
                            .getColumnIndex("deliver_msg_success")));
                    model.setRead_msg_success(cursor.getString(cursor
                            .getColumnIndex("read_msg_success")));

                    model.setMsgDateTimeCombine(msgTime);
                    model.setSeperator_line(getseperatorlineText(msgTime));
                    if (msgTime != null) {
                        try {
                            model.setMsgDate(sms19.inapp.msg.constant.Utils
                                    .getmsgDate(msgTime));
                            model.setMsgTime(sms19.inapp.msg.constant.Utils
                                    .getmsgTime(msgTime));
                            model.setMsgtime(msgTimeNew);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    arrayListOfchats.add(model);

                } while (cursor.moveToPrevious());
                // while (cursor.moveToNext()) {}

            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }

        return arrayListOfchats;
    }

    public ArrayList<Chatmodel> getReadMessage(String remoteid) {

        ArrayList<Chatmodel> arrayListOfchats = new ArrayList<Chatmodel>();

        String query = "";

        query = "SELECT rowid,* FROM " + TableName_Messages
                + " where remote_jid='" + remoteid + "' AND read_msg_success=0";

        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToLast();
                do {

                    Chatmodel model = new Chatmodel();

                    String mine = cursor.getString(cursor
                            .getColumnIndex("key_from_me"));
                    String message = cursor.getString(cursor
                            .getColumnIndex("message"));
                    String msgTime = cursor.getString(cursor
                            .getColumnIndex("msg_time"));
                    String msgTimeNew = cursor.getString(cursor
                            .getColumnIndex("msg_time"));
                    String remoteId = cursor.getString(cursor
                            .getColumnIndex("remote_jid"));
                    String message_packete_id = cursor.getString(cursor
                            .getColumnIndex("message_packetid"));
                    model.setRemote_userid(remoteId);
                    model.setMsg_packetid(message_packete_id);
                    model.setChatmessage(message);

					/*
					 * try { String row_offlineId= cursor.getString(cursor
					 * .getColumnIndex("row_offline_id"));
					 * model.setRow_offlineId(row_offlineId); } catch (Exception
					 * e2) { // TODO Auto-generated catch block
					 * e2.printStackTrace(); }
					 */

                    String user_jid = SingleChatRoomFrgament.myModel
                            .getRemote_jid().trim();
                    if (mine.equals(user_jid)) {
                        model.setMine(true);
                    } else {
                        model.setMine(false);
                        model.setRead_msg_success(cursor.getString(cursor
                                .getColumnIndex("read_msg_success")));
                        model.setRemote_userid(mine);

                        if (msgTime != null) {
                            try {
                                model.setMsgDate(sms19.inapp.msg.constant.Utils
                                        .getmsgDate(msgTime));
                                model.setMsgTime(sms19.inapp.msg.constant.Utils
                                        .getmsgTime(msgTime));
                                model.setMsgtime(msgTimeNew);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        arrayListOfchats.add(model);
                    }

                } while (cursor.moveToPrevious());
                // while (cursor.moveToNext()) {}

            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }

        return arrayListOfchats;

    }

    // @SuppressLint("NewApi")
    // public ArrayList<Chatmodel> getChathistoryfromDB(String remoteid) {
    // // TODO Auto-generated method stub
    // ArrayList<Chatmodel> arrayListOfchats = new ArrayList<Chatmodel>();
    // String query = "SELECT rowid,* FROM " + TableName_Messages
    // + " where remote_jid='" + remoteid + "'";
    // Cursor cursor = null;
    // try {
    //
    // cursor = myDataBase.rawQuery(query, null);
    // if (cursor != null && cursor.getCount() > 0) {
    // while (cursor.moveToNext()) {
    // Chatmodel model = new Chatmodel();
    //
    // String mine = cursor.getString(cursor
    // .getColumnIndex("key_from_me"));
    // String message = cursor.getString(cursor
    // .getColumnIndex("message"));
    // String mediatype = cursor.getString(cursor
    // .getColumnIndex("media_type"));
    // String msgTime = cursor.getString(cursor
    // .getColumnIndex("msg_time"));
    //
    // String user_jid = Chat_frag_new.myModel.getRemote_jid().trim();
    // if (mine.equals(user_jid)) {
    // model.setMine(true);
    // } else {
    // model.setMine(false);
    //
    // model.setRemote_userid(mine);
    //
    // }
    // if (mediatype != null && mediatype.trim().length() != 0) {
    // model.setMediatype(mediatype);
    // String mediapath = cursor.getString(cursor
    // .getColumnIndex("media_path"));
    // if (mediapath != null && mediapath.trim().length() != 0) {
    // model.setMediapath(mediapath);
    // }
    // String mediaurl = cursor.getString(cursor
    // .getColumnIndex("media_url"));
    // if (mediaurl != null && mediaurl.trim().length() != 0) {
    // model.setMediaUrl(mediaurl);
    // }
    // String status = cursor.getString(cursor
    // .getColumnIndex("media_name"));
    // if (status != null && status.trim().length() != 0) {
    // model.setStatus(status);
    // }
    // byte[] thmb = cursor.getBlob(cursor
    // .getColumnIndex("media_thmb"));
    // if (thmb != null) {
    // model.setMediathmb(thmb);
    // }
    // } else {
    // model.setChatmessage(message);
    // }
    // // int type = cursor
    // // .getType(cursor.getColumnIndex("rowid"));
    // String rowid = String.valueOf(cursor.getLong(cursor
    // .getColumnIndex("rowid")));
    // model.setMessagerowid(rowid);
    // if (msgTime != null) {
    // model.setMsgtime(msgTime);
    // }
    // arrayListOfchats.add(model);
    //
    // }
    //
    // }
    // if (cursor != null) {
    // cursor.close();
    // }
    // } catch (Exception e) {
    // if (cursor != null) {
    // cursor.close();
    // }
    // e.printStackTrace();
    // }
    // return arrayListOfchats;
    // }

    // edit the function for read ,sent,deliver process-->add two new field

    public long addchatToMessagetable(String remote_jid, String message,
                                      String key, String msgTime, String message_packetID,
                                      String sent_msg_success, String isBroadCast) {
        ContentValues valuesFor_Chat = new ContentValues();

        valuesFor_Chat.put("remote_jid", remote_jid);
        valuesFor_Chat.put("message", message);
        valuesFor_Chat.put("key_from_me", key);
        valuesFor_Chat.put("msg_time", msgTime);
        valuesFor_Chat.put("message_packetid", message_packetID);
        valuesFor_Chat.put("sent_msg_success", sent_msg_success);
        valuesFor_Chat.put("read_msg_success", 0);// 27Apl2016

        try {
            valuesFor_Chat.put("isBroadCast", isBroadCast);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return myDataBase.insert(TableName_Messages, null, valuesFor_Chat);

    }

    public void updateBroadCast2InFilecase(String msg_time, String remote_jid,
                                           String isBroadCast) {

        try {

            // String Where = "msg_time='" + msg_time + "'";
            String Where = "msg_time='" + msg_time + "'" + " OR "
                    + "remote_jid='" + remote_jid + "'";
            ContentValues newValues = new ContentValues();
            newValues.put("isBroadCast", isBroadCast);

            int id = myDataBase.update(TableName_Messages, newValues, Where,
                    null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public long downloadedAddchatToMessagetable(String remote_jid,
                                                String message, String key, String msgTime,
                                                String message_packetID, String sent_msg_success, String msg_type) {
        ContentValues valuesFor_Chat = new ContentValues();

        valuesFor_Chat.put("remote_jid", remote_jid);
        valuesFor_Chat.put("message", message);
        valuesFor_Chat.put("key_from_me", key);
        valuesFor_Chat.put("msg_time", msgTime);
        valuesFor_Chat.put("message_packetid", message_packetID);
        valuesFor_Chat.put("sent_msg_success", sent_msg_success);
        valuesFor_Chat.put("deliver_msg_success", sent_msg_success);
        // valuesFor_Chat.put("read_msg_success", sent_msg_success);
        valuesFor_Chat.put("read_msg_success", 0);
        if (msg_type.equals("SMS") || msg_type.equalsIgnoreCase("SMS")) {
            valuesFor_Chat.put("media_type", "SMS");
        }

        return myDataBase.insert(TableName_Messages, null, valuesFor_Chat);

    }

    public long addchatToOfflineMessagetable(String remote_jid, String message,
                                             String key, String msgTime, String message_packetID,
                                             String sent_msg_success, String remote_jid_sender, String filepath,
                                             String filetype, String status, String row_id) {
        ContentValues valuesFor_Chat = new ContentValues();

        valuesFor_Chat.put("remote_jid", remote_jid);
        valuesFor_Chat.put("message", message);
        valuesFor_Chat.put("key_from_me", key);
        valuesFor_Chat.put("msg_time", msgTime);
        valuesFor_Chat.put("message_packetid", message_packetID);
        valuesFor_Chat.put("sent_msg_success", sent_msg_success);
        valuesFor_Chat.put("remote_jid_sender", remote_jid_sender);

        valuesFor_Chat.put("media_type", filetype);
        valuesFor_Chat.put("media_path", filepath);
        valuesFor_Chat.put("media_name", status);

        valuesFor_Chat.put("row_offline_id", row_id);

        valuesFor_Chat.put("isoffline", 1);

        return myDataBase.insert(TableName_Offline_Messages, null,
                valuesFor_Chat);

    }

    public long addchatFileToMessagetable(String remote_jid, String filepath,
                                          String filetype, String key, String status, String fileurl,
                                          String loaction, String mediathmb, String msgTime,
                                          String message_packetID, String sent_msg_success,
                                          boolean fileIsUploaded) {
        ContentValues valuesFor_Chat = new ContentValues();

        valuesFor_Chat.put("remote_jid", remote_jid);
        valuesFor_Chat.put("media_type", filetype);
        valuesFor_Chat.put("media_path", filepath);
        valuesFor_Chat.put("media_name", status);
        valuesFor_Chat.put("key_from_me", key);
        valuesFor_Chat.put("msg_time", msgTime);
        valuesFor_Chat.put("message_packetid", message_packetID);
        valuesFor_Chat.put("sent_msg_success", sent_msg_success);
        valuesFor_Chat.put("deliver_msg_success", sent_msg_success);
        // valuesFor_Chat.put("read_msg_success", sent_msg_success);
        valuesFor_Chat.put("read_msg_success", "0");
        // new add 18jan
        String valueIsUpload = "0";
        if (fileIsUploaded) {
            valueIsUpload = "1";
        } else {
            valueIsUpload = "0";
        }
        valuesFor_Chat.put("file_isupload", valueIsUpload);

        valuesFor_Chat.put("media_url", fileurl);
        if (loaction.trim().length() != 0) {
            valuesFor_Chat.put("message", loaction);
        }
        if (mediathmb.trim().length() != 0) {
            byte[] thmb = sms19.inapp.msg.constant.Utils
                    .decodeToImage(mediathmb);
            valuesFor_Chat.put("media_thmb", thmb);
        }

        return myDataBase.insert(TableName_Messages, null, valuesFor_Chat);

    }

    public long addchatFileToMessagetableNew(String remote_jid,
                                             String filepath, String filetype, String key, String status,
                                             String fileurl, String loaction, String mediathmb, String msgTime,
                                             String message_packetID, String sent_msg_success,
                                             String deliver_msg_success, String read, boolean fileIsUploaded) {
        ContentValues valuesFor_Chat = new ContentValues();

        valuesFor_Chat.put("remote_jid", remote_jid);
        valuesFor_Chat.put("media_type", filetype);
        valuesFor_Chat.put("media_path", filepath);
        valuesFor_Chat.put("media_name", status);
        valuesFor_Chat.put("key_from_me", key);
        valuesFor_Chat.put("msg_time", msgTime);
        valuesFor_Chat.put("message_packetid", message_packetID);
        valuesFor_Chat.put("sent_msg_success", sent_msg_success);
        valuesFor_Chat.put("deliver_msg_success", deliver_msg_success);
        // valuesFor_Chat.put("read_msg_success", read);
        valuesFor_Chat.put("read_msg_success", 0);
        // new add 18jan
        String valueIsUpload = "0";
        if (fileIsUploaded) {
            valueIsUpload = "1";
        } else {
            valueIsUpload = "0";
        }
        valuesFor_Chat.put("file_isupload", valueIsUpload);

        valuesFor_Chat.put("media_url", fileurl);
        if (loaction.trim().length() != 0) {
            valuesFor_Chat.put("message", loaction);
        }
        if (mediathmb.trim().length() != 0) {
            byte[] thmb = sms19.inapp.msg.constant.Utils
                    .decodeToImage(mediathmb);
            valuesFor_Chat.put("media_thmb", thmb);
        }

        return myDataBase.insert(TableName_Messages, null, valuesFor_Chat);

    }


    public void updatestatusOfSentMessage(String remotjid, String msgpacketid,
                                          String status_msg) {
        String query = "";
        if (status_msg.equals("deliver")) {
            query = "UPDATE Messages SET deliver_msg_success='1' where remote_jid='"
                    + remotjid
                    + "'  AND message_packetid='"
                    + msgpacketid
                    + "'";

            // start new update 5May2016
            String message_id = getMsgId(msgpacketid);
            if (message_id != null && message_id.length() > 0) {
                updateBraodCastInAppAndInInboxMsgCount(message_id, msgpacketid);
            }
            // end new update 5May2016

        } else if (status_msg.equals("read")) {
            query = "UPDATE Messages SET read_msg_success='1' where remote_jid='"
                    + remotjid
                    + "'  AND message_packetid='"
                    + msgpacketid
                    + "'";
        }
        myDataBase.execSQL(query);
    }

    public synchronized String updateBraodCastInAppAndInInboxMsgCount(
            String message_id, final String packetid) {

        String query = "SELECT * FROM " + TableName_Messages
                + " where message_packetid='" + message_id + "'";// msg id mean
        // user
        // broadcast
        // packet id
        Cursor cursor = null;
        int inAppCount = 0;
        int inInboxCount = 0;
        int mail_count = 0;
        String newmsgId = "";
        String delivered = "";
        int Isdelivered = 0;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                newmsgId = (cursor.getString(cursor
                        .getColumnIndex("message_id")));

                inAppCount = cursor.getInt(cursor.getColumnIndex("inAppCount"));
                mail_count = cursor.getInt(cursor.getColumnIndex("mail_count"));
                inInboxCount = cursor.getInt(cursor
                        .getColumnIndex("inInboxCount"));

				/*
				 * try {
				 * delivered=cursor.getString(cursor.getColumnIndex("MessageStatus"
				 * )); if(delivered!=null&&delivered.length()>0){ Isdelivered=1;
				 * } } catch (Exception e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */

				/*
				 * newValues.put("inAppCount", inAppCount);
				 * newValues.put("inInboxCount", inInboxCount);
				 */

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        if (inInboxCount > 0) {
            try {
                int InInboxMsgCount = inInboxCount - 1;
                if (mail_count > 0 && InInboxMsgCount < mail_count)
                    mail_count--;
                int InAppMsgCount = inAppCount + 1;
                broadCastInAppAndInboxCountIncrease(message_id, InAppMsgCount,
                        InInboxMsgCount, mail_count);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return message_id;
    }

    public void broadCastInAppAndInboxCountIncrease(String packetId,
                                                    int inAppCount, int inInboxCount, int mail_count) {

        String Where = "message_packetid='" + packetId + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("inAppCount", inAppCount);
        newValues.put("inInboxCount", inInboxCount);
        newValues.put("mail_count", mail_count);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);

    }

    public void broadCastShowCountUpdateForSingleUser(String message_packetid,
                                                      int isBroadCast) {

        String Where = "message_packetid='" + message_packetid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("isBroadCast", isBroadCast);
        int id = myDataBase.update(TableName_Messages, newValues, Where, null);
    }

    public synchronized String getPacketId(String message_id) {

        String query = "SELECT * FROM " + TableName_Messages
                + " where message_id='" + message_id + "'";// msg id mean user
        // broadcast packet
        // id
        Cursor cursor = null;

        String packet_id = "";
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                packet_id = (cursor.getString(cursor
                        .getColumnIndex("message_packetid")));
				/*
				 * newValues.put("inAppCount", inAppCount);
				 * newValues.put("inInboxCount", inInboxCount);
				 */

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        return packet_id;
    }

    public String isMessageDelivered(String message_id) {
        String query = "SELECT * FROM " + TableName_Messages
                + " where message_packetid='" + message_id + "'";
        Cursor cursor = null;

        String isDelivered = "";
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                isDelivered = cursor.getString(cursor
                        .getColumnIndex("deliver_msg_success"));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }
        return isDelivered;
    }

    public String getMsgId(String packetId) {

        String query = "SELECT * FROM " + TableName_Messages
                + " where message_packetid='" + packetId + "'";
        Cursor cursor = null;
        String message_id = "";

        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                message_id = (cursor.getString(cursor
                        .getColumnIndex("message_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        return message_id;
    }

    public void updatestatusOfSentMessageForBlock(String remotjid,
                                                  String msgpacketid, String status_msg) {
        String query = "";

        query = "UPDATE Messages SET sent_msg_success='2' where "
                + "message_packetid='" + msgpacketid + "'";

        myDataBase.execSQL(query);
    }


    public boolean alredyExistMessageTime(String messagetime) {
        // String query ="";
        // if(status_msg.equals("deliver"))

        String query = "SELECT * FROM " + TableName_Messages
                + " where msg_time='" + messagetime + "'";
        Cursor cursor = null;
        boolean ismessagetime = false;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String msgTime = cursor.getString(cursor
                        .getColumnIndex("msg_time"));
                ismessagetime = messagetime.trim().equalsIgnoreCase(msgTime.trim());
            }
            return ismessagetime;

        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        return ismessagetime;

        // int id = myDataBase.update(TableName_Messages, newValues, Where,
        // null);

    }

    public void updateSentMessagePacketIdRowIdBase(String remotjid,
                                                   String msgpacketid, String status_msg, String rowid) {

        String Where = "rowid='" + rowid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("message_packetid", msgpacketid);
        newValues.put("sent_msg_success", status_msg);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);

    }

    public void isBroadCastUpdate(String rowid, String isBroadCast,
                                  String inAppCount, String inInboxCount, String mailCount, String message_id,
                                  String expiry_time) {

        String Where = "rowid='" + rowid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("isBroadCast", isBroadCast);
        newValues.put("inAppCount", inAppCount);
        newValues.put("inInboxCount", inInboxCount);
        newValues.put("mail_count", mailCount);
        newValues.put("message_id", message_id);
        try {
            newValues.put("expiry_time", expiry_time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);

    }

    public boolean isEmailAvailable(String remotejid) {

        String Where = "select contact_email from Contacts where remote_jid='" + remotejid + "'";

//        newValues.put("contact_email", isBroadCast);
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(Where, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                String email = cursor.getString(cursor.getColumnIndex("contact_email"));
                if (email != null && email.length() > 4)
                    return true;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void msgIdUpdateBasePacketId(String message_packetid,
                                        String message_id) {

        String Where = "message_packetid='" + message_packetid + "'";
        ContentValues newValues = new ContentValues();

        newValues.put("message_id", message_id);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);

    }

    public void isBroadCastUpdateBasePacketId(String message_id,
                                              String isBroadCast, String inAppCount, String inInboxCount) {

        String Where = "message_id='" + message_id + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("isBroadCast", isBroadCast);
        newValues.put("inAppCount", inAppCount);
        newValues.put("inInboxCount", inInboxCount);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);

    }


    public void updateMessageId(String packetId, String message_id,
                                String expiry_time) {

        String Where = "message_packetid='" + packetId + "'";
        ContentValues newValues = new ContentValues();

        newValues.put("message_id", message_id);
        try {
            newValues.put("expiry_time", expiry_time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            int id = myDataBase.update(TableName_Messages, newValues, Where,
                    null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void updateMessageId2(String msg_time, String jid, String packetId,
                                 String message_id, String expiry_time) {

        // String Where = "message_packetid='" + packetId + "'";
        String Where = "msg_time='" + msg_time + "'" + " AND " + "remote_jid='"
                + jid + "'";
        ContentValues newValues = new ContentValues();

        newValues.put("message_id", message_id);
        newValues.put("message_packetid", packetId);
        try {
            newValues.put("expiry_time", expiry_time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            int id = myDataBase.update(TableName_Messages, newValues, Where,
                    null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void updateFileIsUploaded(String message_packetid, String media_name) {
        String query = "";
        // if(status_msg.equals("deliver"))

        String Where = "message_packetid='" + message_packetid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("media_name", media_name);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);
        // query=
        // "UPDATE OfflineMessages SET message_packetid='"+msgpacketid+"' where remote_jid='"+remotjid;

        // myDataBase.execSQL(query);
    }


    public void timeUpdate(String message_packetid, String updatedid) {

        // String Where = "message_packetid='" + message_packetid + "'";
        String Where = "msg_time='" + message_packetid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("message_packetid", updatedid);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);

    }

    public void updateMessageStatus(String msg_time, String MessageStatus) {

        // String Where = "message_packetid='" + message_packetid + "'";
        String Where = "msg_time='" + msg_time + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("MessageStatus", MessageStatus);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);

    }

    public void timeUpdateWithRemoteId(String msg_time, String updatedid,
                                       String jid) {

        // String Where = "message_packetid='" + message_packetid + "'";
        // String Where = "msg_time='" + msg_time + "'"+" OR "+"remote_jid='" +
        // jid + "'";
        String Where = "msg_time='" + msg_time + "'" + " OR " + "remote_jid='"
                + jid + "'";// 26May2016
        ContentValues newValues = new ContentValues();
        newValues.put("message_packetid", updatedid);

		/*
		 * String query=
		 * "UPDATE Messages SET message_packetid='"+updatedid+"' where msg_time='"
		 * +msg_time+"'  AND remote_jid='"+jid+"'";
		 *
		 * myDataBase.execSQL(query);
		 */

        try {
            int id = myDataBase.update(TableName_Messages, newValues, Where,
                    null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void updateFileIsUploadedForRetry(String message_packetid, int istry) {
        String query = "";
        // if(status_msg.equals("deliver"))

        String Where = "message_packetid='" + message_packetid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("isretry", istry);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);
        // query=
        // "UPDATE OfflineMessages SET message_packetid='"+msgpacketid+"' where remote_jid='"+remotjid;

        // myDataBase.execSQL(query);
    }

    public void updateRetryUpdateToTime(String time, int istry) {
        String query = "";
        // if(status_msg.equals("deliver"))

        String Where = "msg_time='" + time + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("isretry", istry);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);
        // query=
        // "UPDATE OfflineMessages SET message_packetid='"+msgpacketid+"' where remote_jid='"+remotjid;

        // myDataBase.execSQL(query);
    }

    public void addorupdateRecentTable(String remote_jid, String msgtime) {

        Log.e("Group", "addorupdateRecentTable");

        ContentValues valuesFor_Recent = new ContentValues();
        valuesFor_Recent.put("remote_jid", remote_jid);
        valuesFor_Recent.put("msg_time", msgtime);
        String query = "SELECT * FROM " + TableName_Recent
                + " where remote_jid='" + remote_jid + "'";
        String Where = "remote_jid='" + remote_jid + "'";
        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                int id = myDataBase.update(TableName_Recent, valuesFor_Recent,
                        Where, null);
                Utils.printLog("Recent table update " + id);

            } else {
                long rowid = myDataBase.insert(TableName_Recent, null,
                        valuesFor_Recent);
                Utils.printLog("Recent table new entry add " + rowid);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

    }

    public void updateUnreadmsgCount(String remoteid) {
        String query = "UPDATE Contacts SET unread_msg_count = unread_msg_count+1 WHERE remote_jid ='"
                + remoteid + "'";
        myDataBase.execSQL(query);
    }

    public void resetUnreadmsgCount(String remoteid) {
        String query = "UPDATE Contacts SET unread_msg_count =0 WHERE remote_jid ='"
                + remoteid + "'";
        myDataBase.execSQL(query);
        // Cursor cursor = myDataBase.rawQuery(query, null);
    }

    public Contactmodel getDisplaynameforGroupuser(String remote_id) {
        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + remote_id + "'";
        Utils.printLog2("remote id===" + remote_id);
		/*
		 * String query = "SELECT * FROM " + TableName_Contacts +
		 * " where phonenumber='" + remote_id + "'";
		 */
        Cursor cursor = null;
        Contactmodel model = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                model = new Contactmodel();
                byte[] picdata = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));
                model.setName(cursor.getString(cursor
                        .getColumnIndex("display_name")));
                model.setImageUrl(cursor.getString(cursor
                        .getColumnIndex("profilepic_name")));
                if (picdata != null) {
                    model.setAvatar(picdata);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            cursor.close();
        }

        return model;

    }

    public void updateContactmsgData(String remoteid, String msg, String msgTime) {
        Calendar c = Calendar.getInstance();

        String Where = "remote_jid='" + remoteid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("last_msg_time", msgTime);
        newValues.put("last_msg", msg);

        int id = myDataBase.update(TableName_Contacts, newValues, Where, null);
        Utils.printLog("update contact msg data " + id);
    }

    public void updateContactmsgWithIsRegisterData(String remoteid, String msg,
                                                   String msgTime) {
        Calendar c = Calendar.getInstance();

        String Where = "remote_jid='" + remoteid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("last_msg_time", msgTime);
        newValues.put("last_msg", msg);
        newValues.put("isregister", 1);

        int id = myDataBase.update(TableName_Contacts, newValues, Where, null);
        Utils.printLog("update contact msg data " + id);
    }

    public void updateContactmStranger(String remoteid, String msg,
                                       String msgTime) {
        Calendar c = Calendar.getInstance();

        String Where = "remote_jid='" + remoteid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("last_msg_time", msgTime);
        newValues.put("last_msg", msg);
        newValues.put("isregister", 0);
        newValues.put("display_name", "SMS");
        newValues.put("isstranger", 1);

        int id = myDataBase.update(TableName_Contacts, newValues, Where, null);
        Utils.printLog("update contact msg data " + id);
    }

    public void insertContactDetails(String remoteid, String msg, String msgTime) {

        // TODO Auto-generated method stub

        ContentValues newValues = new ContentValues();
        newValues.put("last_msg_time", msgTime);
        newValues.put("last_msg", msg);
        newValues.put("isregister", 0);
        newValues.put("display_name", "SMS");
        newValues.put("isstranger", 1);
        newValues.put("remote_jid", remoteid);

        newValues.put("isgroup", 0);

        myDataBase.insert(TableName_Contacts, null, newValues);

    }

    public ArrayList<Recentmodel> getRecentHistoryfromDb() {
        ArrayList<Recentmodel> recentlist = new ArrayList<Recentmodel>();
        String query = "SELECT * FROM " + TableName_Recent
                + " ORDER BY msg_time DESC";
        Cursor cursor = null;

        HashMap<String, Recentmodel> hashMap = new HashMap<String, Recentmodel>();

        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String remoteid = cursor.getString(cursor
                            .getColumnIndex("remote_jid"));

                    Recentmodel model = getsingleContactfromDB(remoteid);
                    if (model != null) {
                        if (!hashMap.containsKey(remoteid)) {
                            hashMap.put(remoteid, model);
                            int countmessage = GlobalData.dbHelper
                                    .getChathistoryTotalCountfromDB(remoteid);
                            model.setTotalMessageCount(countmessage);
                            recentlist.add(model);
                        }
                    }
                }

                if (recentlist != null) {
                    Collections
                            .sort(recentlist,
                                    new Recentmodel().new CustomComparatorSortByRecent());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            cursor.close();
        }
        return recentlist;

    }

    public Recentmodel getsingleContactfromDB(String remoteid) {
        // TODO Auto-generated method stub
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + remoteid + "'";

        Cursor cursor = null;
        Recentmodel model = null;
        try {
            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                model = new Recentmodel();
                model.setDisplayname(cursor.getString(cursor
                        .getColumnIndex("display_name")));
                model.setUsernumber(cursor.getString(cursor
                        .getColumnIndex("phonenumber")));
                model.setRemote_jid(cursor.getString(cursor
                        .getColumnIndex("remote_jid")));
                model.setCustomStatus(cursor.getString(cursor
                        .getColumnIndex("status_custom")));
                model.setStatus(cursor.getString(cursor
                        .getColumnIndex("status")));
                model.setLastmsg(cursor.getString(cursor
                        .getColumnIndex("last_msg")));
                model.setLastmsg_t(cursor.getString(cursor
                        .getColumnIndex("last_msg_time")));
                model.setIsgroup(cursor.getInt(cursor.getColumnIndex("isgroup")));
                model.setIsRegister(cursor.getInt(cursor
                        .getColumnIndex("isregister")));
                model.setIsStranger(cursor.getInt(cursor
                        .getColumnIndex("isstranger")));
                int count = 0;
                try {
                    count = cursor.getInt(cursor
                            .getColumnIndex("unread_msg_count"));
                    model.setIsUserblock(cursor.getInt(cursor
                            .getColumnIndex("isuserblock")));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                model.setUnreadcount(count);
                String avatarName = cursor.getString(cursor
                        .getColumnIndex("profilepic_name"));
                byte[] avatar = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));
                if (avatar != null) {
                    model.setUserpic(avatar);
                }
            }

            // ///////////////////
            if (model == null) {
                if (remoteid.equals("superadmin")) {
                    model = new Recentmodel();
                    model.setDisplayname("Admin");
                    model.setRemote_jid("superadmin");
                }
            }
            // ///////////////
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
            return null;
        }
        return model;
    }

    public void singleContactBlockfromDB(String remoteid) {

        ContentValues newValues = new ContentValues();

        newValues.put("isuserblock", 1);
        String Where = "remote_jid='" + remoteid + "'";

        myDataBase.update(TableName_Contacts, newValues, Where, null);

    }

    public void singleContactUnBlockfromDB(String remoteid) {

        ContentValues newValues = new ContentValues();

        newValues.put("isuserblock", 0);
        String Where = "remote_jid='" + remoteid + "'";

        myDataBase.update(TableName_Contacts, newValues, Where, null);

    }

    public boolean userIsBlock(String remoteid) {

        boolean block = false;

        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + remoteid + "'";
        Cursor cursor = null;
        int blockcount = 0;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                blockcount = cursor
                        .getInt(cursor.getColumnIndex("isuserblock"));
            }

            block = blockcount != 0;

        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        return block;

    }

    public boolean groupUserIsBlock(String remoteid) {

        boolean block = false;

        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + remoteid + "'";
        Cursor cursor = null;
        int blockcount = 0;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                blockcount = cursor.getInt(cursor
                        .getColumnIndex("isgroupblock"));
            }

            block = blockcount != 0;

        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        return block;

    }

    // public void checkAndinsertSinglecontactinDB(Contactmodel model) {
    // if (!checkcontactisAlreadyexist(model.getRemote_jid())) {
    // ContentValues valuesFor_Contact = new ContentValues();
    // String remote_jid = model.getRemote_jid();
    // String phone = remote_jid.split("@")[0];
    // valuesFor_Contact.put("phonenumber", phone);
    // valuesFor_Contact.put("display_name", phone);
    // valuesFor_Contact.put("isregister", 1);
    // valuesFor_Contact.put("isstranger", 1);
    // valuesFor_Contact.put("unread_msg_count", 0);
    // valuesFor_Contact.put("remote_jid", remote_jid);
    //
    // myDataBase.insert(TableName_Contacts, null, valuesFor_Contact);
    // DownloadVcardandupdateContact(remote_jid);
    // }
    //
    // }

    public byte[] DownloadVcardandupdateContact(final String jid) {
        byte[] byteArray = null;
        if (GlobalData.connection != null
                && (GlobalData.connection.isAuthenticated())) {
            ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
                    new VCardProvider());
            SmackConfiguration.setPacketReplyTimeout(100000);

            try {
                VCard vCard = new VCard();
                vCard.load(GlobalData.connection, jid);
                Utils.printLog(jid + " User vcard load successfully");

                byteArray = vCard.getAvatar();

            } catch (Exception e) {
                e.printStackTrace();
                Utils.printLog(jid + "  User vcard load excption");
            }
            if (byteArray != null) {
                Updateuservcard(jid, byteArray);
            }
        }
        return byteArray;

    }

    public void Updateuservcard(String jid, byte[] img) {
        ContentValues newValues = new ContentValues();

        newValues.put("profilepic_data", img);
        String Where = "remote_jid='" + jid + "'";
        try {
            myDataBase.update(TableName_Contacts, newValues, Where, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void updateGroupinDB(String groupid,
                                final ArrayList<Contactmodel> selectedmemberlist) {
        String memberlist = "";
        for (int i = 0; i < selectedmemberlist.size(); i++) {
            if (i == 0) {
                memberlist = selectedmemberlist.get(i).getRemote_jid();
            } else {
                memberlist = memberlist + ","
                        + selectedmemberlist.get(i).getRemote_jid();

            }
        }
        try {
            String query = "UPDATE Groups SET group_members ='" + memberlist
                    + "' WHERE group_jid ='" + groupid + "'";
            myDataBase.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addnewBroadcastinDB(String broadcastid, String broadcastname,
                                    ArrayList<Contactmodel> selectedmembers, String time) {

        // TODO Auto-generated method stub

        ContentValues gcontactvalues = new ContentValues();
        gcontactvalues.put("remote_jid", broadcastid);
        gcontactvalues.put("display_name", broadcastname);
        gcontactvalues.put("isregister", 1);
        gcontactvalues.put("isstranger", 1);// add new 3feb
        gcontactvalues.put("isgroup", 2);
        gcontactvalues.put("unread_msg_count", 0);

        myDataBase.insert(TableName_Contacts, null, gcontactvalues);
        String memberlist = "";
        for (int i = 0; i < selectedmembers.size(); i++) {
            if (i == 0) {
                memberlist = selectedmembers.get(i).getRemote_jid();
            } else {
                memberlist = memberlist + ","
                        + selectedmembers.get(i).getRemote_jid();

            }
        }
        ContentValues values = new ContentValues();
        values.put("group_jid", broadcastid);
        values.put("groupname", broadcastname);
        values.put("group_members", memberlist);
        values.put("group_created_time", time);

        values.put("createdbyme", 0);

        myDataBase.insert(TableName_Groups, null, values);

        GlobalData.Newgroup_dbinsert = true;

    }

    public String getBroadCastId(String groupName) {
        String query = "SELECT * FROM " + TableName_Groups
                + " where groupname='" + groupName + "'";
        Cursor cursor = null;
        String name = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                name = cursor.getString(cursor.getColumnIndex("group_jid"));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }
        return name;
    }

    public void addnewBroadcastinDBGettedFromServer(String broadcastid,
                                                    String broadcastname, ArrayList<Contactmodel> selectedmembers,
                                                    String time) {

        // TODO Auto-generated method stub

        ContentValues gcontactvalues = new ContentValues();
        gcontactvalues.put("remote_jid", broadcastid);
        gcontactvalues.put("display_name", broadcastname);
        gcontactvalues.put("isregister", 1);
        gcontactvalues.put("isstranger", 1);// add new 3feb
        gcontactvalues.put("isgroup", 2);
        gcontactvalues.put("unread_msg_count", 0);

        myDataBase.insert(TableName_Contacts, null, gcontactvalues);
        String memberlist = "";
        for (int i = 0; i < selectedmembers.size(); i++) {
            if (i == 0) {
                memberlist = selectedmembers.get(i).getRemote_jid();
            } else {
                memberlist = memberlist + ","
                        + selectedmembers.get(i).getRemote_jid();

            }
        }
        ContentValues values = new ContentValues();
        values.put("group_jid", broadcastid);
        values.put("groupname", broadcastname);
        values.put("group_members", memberlist);
        values.put("group_created_time", time);

        values.put("createdbyme", 0);

        myDataBase.insert(TableName_Groups, null, values);

        GlobalData.Newgroup_dbinsert = true;

    }

    // satyajit
    public int updateBroadcastGroupMember(String broadcastId, String groupMember) {

        String Where = "group_jid='" + broadcastId + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("group_members", "");
        newValues.put("isgroup", 1);

        int id = myDataBase.update(TableName_Groups, newValues, Where, null);

        return 0;
    }

    public int updateGroupNameAndIsgroup(String remotejid) {

        String Where = "remote_jid='" + remotejid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("phonenumber", "");
        newValues.put("isgroup", 1);

        int id = myDataBase.update(TableName_Contacts, newValues, Where, null);

        return 0;
    }

    public void addnewGroupinDB(String groupid, String groupname,
                                byte[] byteArray, ArrayList<Contactmodel> selectedmemberlist,
                                boolean byme, String time) {
        // TODO Auto-generated method stub

        ContentValues gcontactvalues = new ContentValues();
        gcontactvalues.put("remote_jid", groupid);
        gcontactvalues.put("display_name", groupname);
        gcontactvalues.put("isregister", 1);
        gcontactvalues.put("isgroup", 1);
        gcontactvalues.put("unread_msg_count", 0);

        if (byteArray != null) {
            gcontactvalues.put("profilepic_data", byteArray);
        }
        if (!checkcontactisAlreadyexist(groupid.trim())) {

            myDataBase.insert(TableName_Contacts, null, gcontactvalues);
            if (checkcontactisSMS19Alreadyexist(groupid.trim())) {
                deleteContactFromSMS19(groupid.trim());
            }

        } else {

            if (groupid.contains("@conference")) {
                updateGroupNameAndIsgroup(groupid);// change 17March2016
            }
        }

        String memberlist = "";
        for (int i = 0; i < selectedmemberlist.size(); i++) {
            if (i == 0) {
                memberlist = selectedmemberlist.get(i).getRemote_jid();
            } else {
                memberlist = memberlist + ","
                        + selectedmemberlist.get(i).getRemote_jid();
            }
        }
        ContentValues values = new ContentValues();
        values.put("group_jid", groupid);
        values.put("groupname", groupname);
        values.put("group_members", memberlist);
        values.put("group_created_time", time);

        if (byme) {
            values.put("createdbyme", 1);
        } else {
            values.put("createdbyme", 0);
        }

        if (!checkGroupAlreadyexist(groupid)) {
            myDataBase.insert(TableName_Groups, null, values);
        }

        GlobalData.Newgroup_dbinsert = true;

    }

    public void editGroupinDB(String groupid,
                              ArrayList<Contactmodel> selectedmembers) {

        String oldmembers = getAlreadySelectedmemberofgroup(groupid);
        if (oldmembers.trim().length() != 0) {
            for (int i = 0; i < selectedmembers.size(); i++) {

                oldmembers = oldmembers + ","
                        + selectedmembers.get(i).getRemote_jid();
                // add m
				/*
				 * if(selectedmembers.get(i).getIsAdmin()==1){
				 * GlobalData.dbHelper
				 * .groupAddAdminDB(groupid,selectedmembers.get
				 * (i).getRemote_jid()); }
				 */

            }
            String WHERE = "group_jid='" + groupid + "'";
            ContentValues values = new ContentValues();

            values.put("group_members", oldmembers);
            myDataBase.update(TableName_Groups, values, WHERE, null);

        } else {// change m
            if (selectedmembers != null) {

                for (int i = 0; i < selectedmembers.size(); i++) {

                    if (i == 0) {
                        oldmembers = selectedmembers.get(i).getRemote_jid();
                    } else {
                        oldmembers = oldmembers + ","
                                + selectedmembers.get(i).getRemote_jid();
                    }
					/*
					 * //add m if(selectedmembers.get(i).getIsAdmin()==1){
					 * GlobalData
					 * .dbHelper.groupAddAdminDB(groupid,selectedmembers
					 * .get(i).getRemote_jid()); }
					 */

                }
                String WHERE = "group_jid='" + groupid + "'";
                ContentValues values = new ContentValues();

                values.put("group_members", oldmembers);

                if (!oldmembers.equalsIgnoreCase("")) {
                    myDataBase.update(TableName_Groups, values, WHERE, null);
                }
            }
        }

    }

    public void editGroupinDBNew(String groupid,
                                 ArrayList<Contactmodel> selectedmembers) {

        String oldmembers = /* getAlreadySelectedmemberofgroup(groupid) */"";
        if (oldmembers.trim().length() != 0) {
            for (int i = 0; i < selectedmembers.size(); i++) {

                oldmembers = oldmembers + ","
                        + selectedmembers.get(i).getRemote_jid();

                // add m
                if (selectedmembers.get(i).getIsAdmin() == 1) {
                    GlobalData.dbHelper.groupAddAdminDB(groupid,
                            selectedmembers.get(i).getRemote_jid());
                }

            }
            String WHERE = "group_jid='" + groupid + "'";
            ContentValues values = new ContentValues();

            values.put("group_members", oldmembers);
            myDataBase.update(TableName_Groups, values, WHERE, null);

        } else {// change m
            if (selectedmembers != null) {

                for (int i = 0; i < selectedmembers.size(); i++) {

                    if (i == 0) {
                        oldmembers = selectedmembers.get(i).getRemote_jid();
                    } else {
                        oldmembers = oldmembers + ","
                                + selectedmembers.get(i).getRemote_jid();
                    }

                    // add m
                    if (selectedmembers.get(i).getIsAdmin() == 1) {
                        GlobalData.dbHelper.groupAddAdminDB(groupid,
                                selectedmembers.get(i).getRemote_jid());
                    }

                }
                String WHERE = "group_jid='" + groupid + "'";
                ContentValues values = new ContentValues();

                values.put("group_members", oldmembers);
                if (!oldmembers.equalsIgnoreCase("")) {
                    myDataBase.update(TableName_Groups, values, WHERE, null);
                }
            }
        }

    }

    public void editGroupMemberInDBNew(String groupid,
                                       ArrayList<Contactmodel> selectedmembers) {

        String oldmembers = "";

        if (selectedmembers != null) {

            for (int i = 0; i < selectedmembers.size(); i++) {

                if (i == 0) {
                    oldmembers = selectedmembers.get(i).getRemote_jid();
                } else {
                    oldmembers = oldmembers + ","
                            + selectedmembers.get(i).getRemote_jid();
                }

                // add m
                if (selectedmembers.get(i).getIsAdmin() == 1) {
                    GlobalData.dbHelper.groupAddAdminDB(groupid,
                            selectedmembers.get(i).getRemote_jid());
                }

            }
            String WHERE = "group_jid='" + groupid + "'";
            ContentValues values = new ContentValues();

            values.put("group_members", oldmembers);
            if (!oldmembers.equalsIgnoreCase("")) {
                ContentValues values2 = new ContentValues();
                values2.put("group_members", "");
                try {
                    myDataBase.update(TableName_Groups, values2, WHERE, null);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    myDataBase.update(TableName_Groups, values, WHERE, null);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public void singleGroupContactBlockfromDB(String remoteid) {

        ContentValues newValues = new ContentValues();

        newValues.put("isgroupblock", 1);
        String Where = "remote_jid='" + remoteid + "'";

        myDataBase.update(TableName_Contacts, newValues, Where, null);

    }

    public void singleGroupContactUnBlockfromDB(String remoteid) {

        ContentValues newValues = new ContentValues();

        newValues.put("isgroupblock", 0);
        String Where = "remote_jid='" + remoteid + "'";

        myDataBase.update(TableName_Contacts, newValues, Where, null);

    }

    public void updateGroupCreatedByMe(String remoteid) {

        ContentValues newValues = new ContentValues();

        newValues.put("createdbyme", 0);
        String Where = "group_jid='" + remoteid + "'";

        myDataBase.update(TableName_Groups, newValues, Where, null);

    }

    public void groupCreatedByMeUpdateDB(String remoteid) {
        ContentValues newValues = new ContentValues();

        newValues.put("createdbyme", 1);
        String Where = "group_jid='" + remoteid + "'";

        myDataBase.update(TableName_Groups, newValues, Where, null);

    }

    public void groupAddAdminDB(String remoteid, String jid) {
        ContentValues newValues = new ContentValues();

        newValues.put("admin_jid", jid);

        String Where = "group_jid='" + remoteid + "'";

        myDataBase.update(TableName_Groups, newValues, Where, null);

    }

    public String checkGroupAdmin(String gid) {
        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + gid + "'";
        Cursor cursor = null;
        String byme = "";
        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String createdbyme = cursor.getString(cursor
                    .getColumnIndex("admin_jid"));
            if (createdbyme != null) {
                byme = createdbyme;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return byme;
    }

    public boolean groupIsBlockNew(String remoteid) {

        boolean block = false;

        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + remoteid + "'";
        Cursor cursor = null;
        int blockcount = 0;
        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                blockcount = cursor.getInt(cursor.getColumnIndex("isblock"));
            }

            block = blockcount != 0;

        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        return block;

    }

    public void groupBlocknewfromDB(String remoteid) {

        ContentValues newValues = new ContentValues();

        newValues.put("isblock", 1);
        String Where = "group_jid='" + remoteid + "'";

        myDataBase.update(TableName_Groups, newValues, Where, null);

    }

    public void groupUnBlocknewfromDB(String remoteid) {

        ContentValues newValues = new ContentValues();

        newValues.put("isblock", 0);
        String Where = "group_jid='" + remoteid + "'";

        myDataBase.update(TableName_Groups, newValues, Where, null);

    }

    public void addGroupMemberFromDB(String groupid, String jid) {

        String oldmembers = getAlreadySelectedmemberofgroup(groupid);
        // String newMembers="";
        if (oldmembers.trim().length() != 0) {

            String[] selectedmembers = oldmembers.split(",");
            List<String> list = null;
            String updatedArray[] = new String[selectedmembers.length + 1];
            list = new ArrayList<String>();
            Collections.addAll(list, selectedmembers);
            list.add(jid);

			/*
			 * for (int i = 0; i < selectedmembers.length; i++) {
			 *
			 *
			 * if(jid.equalsIgnoreCase(selectedmembers[i])){
			 *
			 * list.add(jid);
			 *
			 *
			 *
			 * }
			 *
			 * }
			 */
            selectedmembers = list.toArray(new String[list.size()]);
            String updated = "";
            for (int j = 0; j < selectedmembers.length; j++) {
                if (j == 0) {
                    updated = selectedmembers[j];
                } else {
                    updated = updated + "," + selectedmembers[j];
                }
            }

            String WHERE = "group_jid='" + groupid + "'";
            ContentValues values = new ContentValues();

            values.put("group_members", updated);
            myDataBase.update(TableName_Groups, values, WHERE, null);

        } else {
            String WHERE = "group_jid='" + groupid + "'";
            ContentValues values = new ContentValues();

            values.put("group_members", jid);
            myDataBase.update(TableName_Groups, values, WHERE, null);
        }

    }

    public void deleteGroupMemberFromDB(String groupid, String jid) {

        String oldmembers = getAlreadySelectedmemberofgroup(groupid);
        // String newMembers="";
        if (oldmembers.trim().length() != 0) {

            String[] selectedmembers = oldmembers.split(",");
            List<String> list = null;
            list = new ArrayList<String>();
            Collections.addAll(list, selectedmembers);

            for (int i = 0; i < selectedmembers.length; i++) {

                if (jid.equalsIgnoreCase(selectedmembers[i])) {
                    singleGroupContactBlockfromDB(jid);
                    list.remove(jid);

                }

            }
            selectedmembers = list.toArray(new String[list.size()]);
            String updated = "";
            for (int j = 0; j < selectedmembers.length; j++) {
                if (j == 0) {
                    updated = selectedmembers[j];
                } else {
                    updated = updated + "," + selectedmembers[j];
                }
            }

            String WHERE = "group_jid='" + groupid + "'";
            ContentValues values = new ContentValues();

            values.put("group_members", updated);
            myDataBase.update(TableName_Groups, values, WHERE, null);

        }

    }

    public ArrayList<Contactmodel> editContactforGroup(
            ArrayList<Contactmodel> selectedmembers) {
        ArrayList<Contactmodel> filtermembers = new ArrayList<Contactmodel>();
        for (Contactmodel contactmodel : selectedmembers) {
            if (!checkcontactisAlreadyexist(contactmodel.getRemote_jid())) {
                ContentValues valuesFor_Contact = new ContentValues();
                String remote_jid = contactmodel.getRemote_jid();
                String phone = remote_jid.split("@")[0];
                valuesFor_Contact.put("phonenumber", phone);
                valuesFor_Contact.put("display_name", phone);
                valuesFor_Contact.put("isregister", 1);
                valuesFor_Contact.put("isstranger", 1);
                valuesFor_Contact.put("unread_msg_count", 0);
                valuesFor_Contact.put("remote_jid", remote_jid);
                //
                String stejid = remote_jid.split("@")[1];
                if (!stejid.startsWith("conference")) {
                    // myDataBase.insert(TableName_Contacts, null,
                    // valuesFor_Contact);//new comment 30Jan
                    filtermembers.add(contactmodel);
                }

            } else {
                // selectedmembers.remove(contactmodel);
            }
        }
        return filtermembers;
    }

    public String getAlreadySelectedmemberofgroup(String groupid) {
        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + groupid + "'";
        Cursor cursor = null;
        String list = "";
        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            list = cursor.getString(cursor.getColumnIndex("group_members"));
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public void setContactasnotStranger(Contactmodel contact) {
        if (contact != null) {
            String jid = contact.getNumber() + "@" + GlobalData.HOST;
            String query = "SELECT * FROM " + TableName_Contacts
                    + " where remote_jid='" + jid
                    + "' AND isstranger='1' AND isregister='1'";
            Cursor cursor = null;

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                resetStrangertoContact(jid, contact.getName());
            }
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    public int updateCustomStatusOfContact(String jid, String cStatus) {

        String statusArray[] = cStatus
                .split(sms19.inapp.msg.constant.GlobalData.status_time_separater);
        String status = statusArray[0];
        String timeString = "";
        try {
            if (statusArray.length > 1) {
                timeString = cStatus.split(GlobalData.status_time_separater)[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!timeString.equals("")) {
            String Where = "remote_jid='" + jid + "'";
            ContentValues newValues = new ContentValues();
            newValues.put("status_custom", status);
            newValues.put("status_custom_time", timeString);
            int id = myDataBase.update(TableName_Contacts, newValues, Where,
                    null);
            return id;
        } else {
            String Where = "remote_jid='" + jid + "'";
            ContentValues newValues = new ContentValues();
            newValues.put("status_custom", status);
            int id = myDataBase.update(TableName_Contacts, newValues, Where,
                    null);

        }
        return 0;
    }

    public Contactmodel getCustomStatus(String jid) {

        Cursor cursor2 = null;
        Contactmodel contactmodel = new Contactmodel();
        // add new
        String status_custom = "0";
        String status = "0";
        String status_custom_time = "";
        String last_seen = "";
        String query2 = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + jid + "'";
        cursor2 = myDataBase.rawQuery(query2, null);

        if (cursor2 != null && cursor2.getCount() > 0) {
            cursor2.moveToFirst();

            byte[] pic = cursor2.getBlob(cursor2
                    .getColumnIndex("profilepic_data"));
            if (pic != null) {
                contactmodel.setAvatar(pic);
            }
            contactmodel.setImageUrl(cursor2.getString(cursor2
                    .getColumnIndex("profilepic_name")));

            try {
                try {
                    status_custom = cursor2.getString(cursor2
                            .getColumnIndex("status_custom"));
                    if (status_custom == null) {
                        status_custom = "0";
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    status_custom = "0";
                }
                try {
                    status = cursor2
                            .getString(cursor2.getColumnIndex("status"));

                    if (status == null) {
                        status = "0";
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    status = "0";
                    e.printStackTrace();
                }
                status_custom_time = cursor2.getString(cursor2
                        .getColumnIndex("status_custom_time"));

                last_seen = cursor2.getString(cursor2
                        .getColumnIndex("lastseen"));

                contactmodel.setName(cursor2.getString(cursor2
                        .getColumnIndex("display_name")));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        contactmodel.setCustomStatus(status_custom);
        contactmodel.setStatus(status);
        contactmodel.setLastseen(last_seen);
        contactmodel.setStatus_custom_time(status_custom_time);

        return contactmodel;

    }

    public int updateContactName(String phone, String name) {

        String Where = "phonenumber='" + phone + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("display_name", name);

        int id = myDataBase.update(TableName_Contacts, newValues, Where, null);

        return 0;
    }

    public void resetStrangertoContact(String remoteid, String name) {
        String WHERE = "remote_jid='" + remoteid + "'";
        ContentValues valuesFor_Contact = new ContentValues();
        valuesFor_Contact.put("isstranger", 0);
        valuesFor_Contact.put("display_name", name);

        myDataBase.update(TableName_Contacts, valuesFor_Contact, WHERE, null);
    }

    public void updateStrager(String remoteid, String name) {
        String WHERE = "remote_jid='" + remoteid + "'";
        ContentValues valuesFor_Contact = new ContentValues();
        valuesFor_Contact.put("isstranger", 0);
        valuesFor_Contact.put("isregister", 1);
        valuesFor_Contact.put("display_name", name);

        myDataBase.update(TableName_Contacts, valuesFor_Contact, WHERE, null);
    }

    public void registerToStrager(String remoteid) {
        String WHERE = "remote_jid='" + remoteid + "'";
        ContentValues valuesFor_Contact = new ContentValues();
        valuesFor_Contact.put("isstranger", 1);
        valuesFor_Contact.put("isregister", 0);
        valuesFor_Contact.put("display_name", "");
        // valuesFor_Contact.put("phonenumber", "");

        myDataBase.update(TableName_Contacts, valuesFor_Contact, WHERE, null);
    }

    public boolean checkcontactisAlreadyexist(String jid) {
        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + jid + "'";
        Cursor cursor = null;
        boolean already = false;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            already = true;

        }
        if (cursor != null) {
            cursor.close();
        }
        return already;
    }

    public boolean isRegister(String jid) {
        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + jid + "'" + " AND isregister=1";
        Cursor cursor = null;
        boolean already = false;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            already = true;

        }
        if (cursor != null) {
            cursor.close();
        }
        return already;
    }

    public boolean isBlockedUserPacketId(String packetid) {
        String query = "SELECT * FROM " + TableName_Messages
                + " where message_packetid='" + packetid + "'"
                + " AND sent_msg_success=2";
        Cursor cursor = null;
        boolean already = false;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            already = true;

        }
        if (cursor != null) {
            cursor.close();
        }
        return already;
    }

    public boolean isContactBlock(String jid) {
        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + jid + "'" + " AND isuserblock=1";
        Cursor cursor = null;
        boolean already = false;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            already = true;

        }
        if (cursor != null) {
            cursor.close();
        }
        return already;
    }

    public boolean checkcontactisSMS19Alreadyexist(String jid) {
        String query = "SELECT * FROM " + TableName_SMS19_Contacts
                + " where remote_jid='" + jid + "'";
        Cursor cursor = null;
        boolean already = false;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            already = true;

        }
        if (cursor != null) {
            cursor.close();
        }
        return already;
    }

    public boolean checkGroupAlreadyexist(String jid) {
        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + jid + "'";
        Cursor cursor = null;
        boolean already = false;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            already = true;

        }
        if (cursor != null) {
            cursor.close();
        }
        return already;
    }

    public void deleteGroupRefresh(String jid) {
        myDataBase.execSQL("delete from " + TableName_Groups
                + " where group_jid='" + jid + "'");
    }


    public boolean checkGroupisAlreadyexsit(String gid) {
        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + gid + "'";
        Cursor cursor = null;
        boolean already = false;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            already = true;
        }
        if (cursor != null) {
            cursor.close();
        }
        return already;
    }

    public ArrayList<String> getMygroupfromDB() {
        ArrayList<String> grouplist = new ArrayList<String>();
        String query = "SELECT * FROM " + TableName_Groups;
        Cursor cursor = null;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String groupid = cursor.getString(cursor
                        .getColumnIndex("group_jid"));
                String domian = groupid.split("@")[1];
                if (domian != null && domian.trim().length() != 0
                        && domian.equalsIgnoreCase("Broadcast")) {

                } else {
                    grouplist.add(groupid);
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return grouplist;
    }

    public boolean checkGroupiscreatedbyme(String gid) {
        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + gid + "'";
        Cursor cursor = null;
        boolean byme = false;
        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int createdbyme = cursor.getInt(cursor
                    .getColumnIndex("createdbyme"));
            if (createdbyme == 1) {
                byme = true;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return byme;
    }

    public ArrayList<String> getBroadcastmembersfromDB(String bid) {
        ArrayList<String> memberlist = new ArrayList<String>();
        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + bid + "'";
        Cursor cursor = null;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String list = cursor.getString(cursor
                    .getColumnIndex("group_members"));

            String lists[] = list.split(",");
            for (int i = 0; i < lists.length; i++) {
                String remotejid = lists[i];

                memberlist.add(remotejid);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return memberlist;
    }


    public boolean isGroup(String name) {

        boolean isgroup = false;
        String query = "SELECT * FROM " + TableName_Groups
                + " where groupname='" + name + "'";
        Cursor cursor = null;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            isgroup = true;

        }
        if (cursor != null) {
            cursor.close();
        }
        return isgroup;

    }

    public ArrayList<Recentmodel> getGroupmemberListfromDB(String gid) {
        ArrayList<Recentmodel> memberlist = new ArrayList<Recentmodel>();

        Log.e("Group", "getGroupmemberListfromDB");

        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + gid + "'";

        Utils.printLog("get group remote jid=" + gid);
        Cursor cursor = null;
        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String list = cursor.getString(cursor
                    .getColumnIndex("group_members"));
            if (list != null && !list.trim().equals("")) {
                String lists[] = list.split(",");
                // if(SingleChatRoomFrgament.testFlag){
                // lists=(String[]) SingleChatRoomFrgament.list.toArray();
                // SingleChatRoomFrgament.testFlag=false;
                // }
                for (int i = 0; i < lists.length; i++) {
                    String remotejid = lists[i];
                    Recentmodel model = getsingleContactfromDB(remotejid);
                    if (model != null) {
                        memberlist.add(model);
                    } else {
                        Recentmodel model2 = new Recentmodel();
                        model2.setRemote_jid(remotejid);
                        model2.setDisplayname(remotejid.split("@")[0]);
                        model2.setUsernumber(remotejid.split("@")[0]);
                        memberlist.add(model2);
                    }
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return memberlist;
    }


    public Groupmodel getSingleGroupFromDB(String gid) {

        Log.e("Group", "getSingleGroupFromDB");

        Groupmodel memberlist = new Groupmodel();

        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + gid + "'";

        Utils.printLog("get group remote jid=" + gid);
        Cursor cursor = null;
        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            // cursor.getString(cursor.getColumnIndex("createdbyme"));
            memberlist.setCreated_me(String.valueOf(cursor.getInt(cursor
                    .getColumnIndex("createdbyme"))));

        }
        if (cursor != null) {
            cursor.close();
        }
        return memberlist;
    }

    public synchronized ArrayList<Groupmodel> getGroupallmemberListfromDB() {
        ArrayList<Groupmodel> memberlist = new ArrayList<Groupmodel>();
        Log.e("Group", "getGroupallmemberListfromDB");
        // String query = "SELECT * FROM " + TableName_Recent
        // + " ORDER BY msg_time DESC";

        Cursor cursor;
        try {
            String query = "SELECT * FROM " + TableName_Groups
                    + " where createdbyme=1  OR createdbyme=0";

            cursor = null;
            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String remoteid = cursor.getString(cursor
                            .getColumnIndex("group_jid"));
                    String createdtime = cursor.getString(cursor
                            .getColumnIndex("group_created_time"));
                    if (createdtime == null) {
                        createdtime = "0";
                    }

                    Groupmodel model = getsingleGroupfromDB(remoteid);
                    if (model != null) {
                        model.setGroup_created_time(createdtime.trim());
//						model.setCreated_me(createdtime.trim());
                        String str[] = null;
                        if (model.getGroup_members() != null)
                            str = model.getGroup_members().toString().split(",");
                        else
                            str = "abc,abe".split(",");

                        if (model.getGroup_jid() != null
                                && model.getGroup_jid().length() > 0 && str.length < 250) {
                            memberlist.add(model);
                        }
                    }
                }

                if (memberlist != null && memberlist.size() > 0) {
                    // Collections.sort(memberlist,new Groupmodel().new
                    // CustomComparatorSortByRecentGroup());
                    Collections.reverse(memberlist);
                }
            }

            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        return memberlist;
    }


    public ArrayList<Groupmodel> getGroupAddWithUserArrayListFromDB(String jid) {
        ArrayList<Groupmodel> memberlist = new ArrayList<Groupmodel>();

        Log.e("Group", "getGroupAddWithUserArrayListFromDB");

        String query = "SELECT * FROM " + TableName_Groups
                + " where createdbyme=1  OR createdbyme=0";

        Cursor cursor = null;
        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String remoteid = cursor.getString(cursor
                        .getColumnIndex("group_jid"));
                if (getGroupListWithUserDB(remoteid, jid)) {
                    Groupmodel model = getsingleGroupfromDBWithUsersArray(remoteid);

                    if (model != null) {
                        memberlist.add(model);
                    }
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return memberlist;
    }

    public boolean getGroupListWithUserDB(String gid, String jid) {
        ArrayList<Recentmodel> memberlist = new ArrayList<Recentmodel>();

        boolean isGroup = false;

        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + gid + "'";

        Utils.printLog("get group remote jid=" + gid);
        Cursor cursor = null;
        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String list = cursor.getString(cursor
                    .getColumnIndex("group_members"));
            if (list != null && !list.trim().equals("")) {
                String lists[] = list.split(",");
                for (int i = 0; i < lists.length; i++) {
                    String remotejid = lists[i];
                    if (jid.trim().equalsIgnoreCase(remotejid)) {
                        Recentmodel model = getsingleContactfromDB(remotejid);
                        memberlist.add(model);
                        isGroup = true;
                        break;
                    } else {
                        isGroup = false;
                    }
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return isGroup;
    }

    public Groupmodel getsingleGroupfromDBWithUsersArray(String remoteid) {
        // TODO Auto-generated method stub

        Groupmodel model = new Groupmodel();
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + remoteid + "'";

        Cursor cursor = null;

        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                model = new Groupmodel();
                model.setGroupname(cursor.getString(cursor
                        .getColumnIndex("groupname")));
                model.setCreated_me(String.valueOf(cursor.getInt(cursor
                        .getColumnIndex("createdbyme"))));
                model.setGroup_members(cursor.getString(cursor
                        .getColumnIndex("group_members")));
                model.setGroup_jid(cursor.getString(cursor
                        .getColumnIndex("group_jid")));

                if (!cursor.getString(cursor.getColumnIndex("group_members"))
                        .equalsIgnoreCase("")) {
                    String string = cursor.getString(cursor
                            .getColumnIndex("group_members"));
                    String nameArray[] = string.split(",");
                    ArrayList<Recentmodel> arrayList = new ArrayList<Recentmodel>();
                    for (int i = 0; i < nameArray.length; i++) {
                        // Contactmodel contactmodel=new Contactmodel();
                        Recentmodel contactmodel = getsingleContactfromDB(nameArray[i]);
                        // contactmodel.setRemote_jid(nameArray[i]);
                        arrayList.add(contactmodel);
                    }
                    model.setContactList(arrayList);
                }

            }
            // ///////////////////

            // ///////////////
            if (cursor != null) {
                cursor.close();
            }

            Cursor cursor2 = null;

            // add new

            String query2 = "SELECT * FROM " + TableName_Contacts
                    + " where remote_jid='" + remoteid + "'";
            cursor2 = myDataBase.rawQuery(query2, null);

            if (cursor2 != null && cursor2.getCount() > 0) {
                cursor2.moveToFirst();

                byte[] pic = cursor2.getBlob(cursor2
                        .getColumnIndex("profilepic_data"));
                if (pic != null) {
                    model.setUser_pic(pic);
                }
                int count = 0;
                try {
                    count = cursor2.getInt(cursor2
                            .getColumnIndex("unread_msg_count"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
            return null;
        }
        return model;
    }

    public Groupmodel getsingleGroupfromDB(String remoteid) {
        // TODO Auto-generated method stub

        Groupmodel model = new Groupmodel();
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + remoteid + "'";

        Cursor cursor = null;

        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                model = new Groupmodel();
                model.setGroupname(cursor.getString(cursor
                        .getColumnIndex("groupname")));
                model.setCreated_me(String.valueOf(cursor.getString(cursor
                        .getColumnIndex("createdbyme"))));
                model.setGroup_members(cursor.getString(cursor
                        .getColumnIndex("group_members")));
                model.setGroup_jid(cursor.getString(cursor
                        .getColumnIndex("group_jid")));

            }
            // ///////////////////

            // ///////////////
            if (cursor != null) {
                cursor.close();
            }

            Cursor cursor2 = null;

            // add new

            String query2 = "SELECT * FROM " + TableName_Contacts
                    + " where remote_jid='" + remoteid + "'";
            cursor2 = myDataBase.rawQuery(query2, null);

            if (cursor2 != null && cursor2.getCount() > 0) {
                cursor2.moveToFirst();

                byte[] pic = cursor2.getBlob(cursor2
                        .getColumnIndex("profilepic_data"));
                if (pic != null) {
                    model.setUser_pic(pic);
                }
                int count = 0;
                try {
                    count = cursor2.getInt(cursor2
                            .getColumnIndex("unread_msg_count"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
            return null;
        }
        return model;
    }


    public HashMap<String, Contactmodel> getUnRegistersavedContactfromNewWithOutSMS19DB() {
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        String query = "SELECT * FROM " + TableName_Contacts
                + " where isregister=0 AND isgroup=0 AND issms19_number=0";
        Cursor cursor = null;
        HashMap<String, Contactmodel> Dbmap = new HashMap<String, Contactmodel>();

        try {
            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Contactmodel model = new Contactmodel();

                    model.setNumber(cursor.getString(cursor
                            .getColumnIndex("phonenumber")));
                    model.setName(cursor.getString(cursor
                            .getColumnIndex("display_name")));

                    Dbmap.put(model.getNumber(), model);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            cursor.close();
        }
        return Dbmap;
    }


    public HashMap<String, Contactmodel> getsavedContactfromDB() {
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        String query = "SELECT * FROM " + TableName_Contacts
                + " where isregister=1 AND isstranger=0 AND isgroup=0";
        Cursor cursor = null;
        HashMap<String, Contactmodel> Dbmap = new HashMap<String, Contactmodel>();

        try {
            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Contactmodel model = new Contactmodel();
                    model.setRemote_jid(cursor.getString(cursor
                            .getColumnIndex("remote_jid")));
                    model.setName(cursor.getString(cursor
                            .getColumnIndex("display_name")));
                    Dbmap.put(model.getRemote_jid(), model);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            cursor.close();
        }
        return Dbmap;
    }

    public Contactmodel getUserDetails(String remote_jid) {
        Contactmodel model = null;
        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + remote_jid.trim() + "'";
        Cursor cursor = null;

        try {
            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    model = new Contactmodel();
                    model.setRemote_jid(cursor.getString(cursor
                            .getColumnIndex("remote_jid")));
                    model.setName(cursor.getString(cursor
                            .getColumnIndex("display_name")));
                    model.setNumber(cursor.getString(cursor
                            .getColumnIndex("phonenumber")));
                    model.setStatus(cursor.getString(cursor
                            .getColumnIndex("status")));

                    model.setIsRegister(cursor.getInt(cursor
                            .getColumnIndex("isregister")));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            cursor.close();
        }
        return model;
    }

    public boolean isOnline(String remote_jid) {
        Contactmodel model = null;
        String query = "SELECT status FROM " + TableName_Contacts
                + " where remote_jid='" + remote_jid.trim() + "'";
        Cursor cursor = null;
        boolean isonline = false;

        try {
            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    model = new Contactmodel();

                    model.setStatus(cursor.getString(cursor
                            .getColumnIndex("status")));
                    String status = cursor.getString(cursor
                            .getColumnIndex("status"));
                    isonline = status != null && (!status.equalsIgnoreCase("null"))
                            && status.equalsIgnoreCase("1");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            cursor.close();
        }
        return isonline;
    }


    public void changeContactTostrangerinDB(HashMap<String, Contactmodel> Dbmap) {
        if (Dbmap != null && Dbmap.size() > 0) {
            Iterator entries = Dbmap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();

                Contactmodel model = (Contactmodel) entry.getValue();
                try {
                    // changeTostranger(model);
                    if (!isSMS19Contact(model.getRemote_jid())) { // add new

                        changeTostranger(model);

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    public void changeTostranger(Contactmodel model) {
        String WHERE = "remote_jid ='" + model.getRemote_jid() + "'";
        ContentValues values = new ContentValues();
        values.put("isstranger", 1);
        values.put("display_name", model.getRemote_jid().split("@")[0]);
        myDataBase.update(TableName_Contacts, values, WHERE, null);
    }

    public String getseperatorlineText(String timemilles) {
        String str[] = Utils.getSeperatorDateorTime(timemilles).split("`");
        if (str != null && str.length > 1)
            if (str[1].equals("old")) {
                if (!dateSeperatorValue.equals(str[0])) {
                    if (str[1] != null) {
                        if (str[1].equals("old")) {
                            dateSeperatorValue = str[0];
                            return dateSeperatorValue;
                        } else {
                            dateSeperatorValue = "today";
                            return "Today";
                        }
                    }
                } else {
                    return "";
                }

            } else {

                // Log.e("dateSeperatorValue.equals(str[1]",
                // dateSeperatorValue+"--"+str[1]);
                if (!dateSeperatorValue.equals(str[1])) {
                    dateSeperatorValue = "today";
                    return "Today";
                } else {
                    return "";
                }
            }

        return "";

    }

    public void updateGroupNameInDB(String groupid, String newName) {

        // updation in group
        String WHERE2 = "group_jid='" + groupid + "'";
        ContentValues values = new ContentValues();

        values.put("groupname", newName);
        myDataBase.update(TableName_Groups, values, WHERE2, null);

        // updation in contacts
        String WHERE = "remote_jid='" + groupid + "'";
        ContentValues valuesFor_Contact = new ContentValues();
        valuesFor_Contact.put("display_name", newName);

        myDataBase.update(TableName_Contacts, valuesFor_Contact, WHERE, null);
    }

    public void updateGroupIconInDB(String groupid, byte[] picarr) {

        // updation in group
        ContentValues newValues = new ContentValues();
        newValues.put("profilepic_data", picarr);
        String Where = "remote_jid='" + groupid + "'";
        myDataBase.update(TableName_Contacts, newValues, Where, null);
    }

    public byte[] getGroupIcon(String remoteid) {
        byte[] picarr = null;
        // updation in group

        String query = "SELECT * FROM " + TableName_Contacts
                + " where remote_jid='" + remoteid + "'";
        Cursor cursor = null;

        try {

            cursor = myDataBase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                picarr = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (cursor != null) {
            cursor.close();
        }

        return picarr;
    }


    /*Methods for Loaders*/
    public Cursor getAllContactCursor(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String query = "SELECT * FROM " + TableName_Contacts
                + " where isregister=1 OR isregister=0";
        try {

//            return myDataBase.rawQuery(query, null);
            return myDataBase.query(TableName_Contacts, projection, selection, selectionArgs, null, null, sortOrder);
        } catch (SQLiteException e) {

        }
        return null;
    }

}
/*
*
* public void addSms19Contact(ArrayList<ContactDetailsNew> Model) {

        for (int i = 0; i < Model.size(); i++) {
            try {
                String contactName = Model.get(i).getContact_name();
                String contactMobile = Model.get(i).getContact_mobile();
                String phoneNo = "";

                PhoneValidModel model = ContactUtil
                        .validNumberForInvite(contactMobile);
                if (model != null && model.getPhone_number() != null
                        && model.getPhone_number().length() > 0) {
                    String remotejid = (model.getCountry_code()
                            + model.getPhone_number() + "@" + GlobalData.HOST)
                            .trim();
                    phoneNo = (model.getCountry_code() + model
                            .getPhone_number().trim());
                    if (!checkcontactisAlreadyexist(remotejid)) {

                        if (contactName.equalsIgnoreCase("")) {
                            contactName = phoneNo;
                        }
                        insertSinglecontactinSMS19DB(contactName, phoneNo);

                    } else {
                        if (getIsStranger(phoneNo)) {
                            if (isSMS19Contact(phoneNo)) {
                                updateStrager(phoneNo, contactName);
                            }
                        }
                    }

                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }


     public void insertExpiryMessageTime(String message_id, String expiry_time,
                                        String msg_add_time, String batch_id) {
        // TODO Auto-generated method stub

        ContentValues valuesFor_userData = new ContentValues();
        valuesFor_userData.put("message_id", message_id);
        valuesFor_userData.put("expiry_time", expiry_time);
        valuesFor_userData.put("msg_add_time", msg_add_time);
        valuesFor_userData.put("batch_id", batch_id);
        myDataBase.insert(Message_Expire, null, valuesFor_userData);
    }

     public HashMap<String, Contactmodel> getsavedContactfromDBAllType() {
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        String query = "SELECT * FROM " + TableName_Contacts
                + " where isgroup=0";
        Cursor cursor = null;
        HashMap<String, Contactmodel> Dbmap = new HashMap<String, Contactmodel>();

        try {
            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Contactmodel model = new Contactmodel();
                    model.setRemote_jid(cursor.getString(cursor
                            .getColumnIndex("remote_jid")));
                    Dbmap.put(model.getRemote_jid(), model);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            cursor.close();
        }
        return Dbmap;
    }

    public HashMap<String, Contactmodel> getsavedContactfromDBAllWitouSms19() {

        String query = "SELECT * FROM " + TableName_Contacts
                + " where isgroup=0";
        Cursor cursor = null;
        HashMap<String, Contactmodel> Dbmap = new HashMap<String, Contactmodel>();

        try {
            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Contactmodel model = new Contactmodel();
                    model.setRemote_jid(cursor.getString(cursor
                            .getColumnIndex("remote_jid")));
                    int issms19_number = (cursor.getInt(cursor
                            .getColumnIndex("issms19_number")));
                    if (issms19_number != 1) {
                        Dbmap.put(model.getRemote_jid(), model);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            cursor.close();
        }
        return Dbmap;
    }

    // satyajit
    public void updateGroupmemberList1(String grpId, String lists) {

        Log.e("Group", "updateGroupmemberList1");

        // String query = "SELECT * FROM " + TableName_Groups
        // + " where group_jid='" + grpId + "'";

        String WHERE2 = "group_jid='" + grpId + "'";
        ContentValues values = new ContentValues();
        // if(lists!=null && lists.length>0){
        // for (int i = 0; i < lists.length; i++) {
        values.put("group_members", lists);
        myDataBase.update(TableName_Groups, values, WHERE2, null);

        // }
        // }

        // updation in contacts
        // String WHERE = "remote_jid='" + groupid + "'";
        // ContentValues valuesFor_Contact = new ContentValues();
        // valuesFor_Contact.put("display_name", newName);
        //
        // myDataBase.update(TableName_Contacts, valuesFor_Contact, WHERE,
        // null);
    }

    public void updateGroupmemberList(String grpId, ArrayList<String> lists) {
        // String query = "SELECT * FROM " + TableName_Groups
        // + " where group_jid='" + grpId + "'";
        Log.e("Group", "updateGroupmemberList");

        String WHERE2 = "group_jid='" + grpId + "'";
        ContentValues values = new ContentValues();
        if (lists != null && lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                values.put("group_members", lists.get(i));
                // myDataBase.update(TableName_Groups, values, WHERE2, null);
                myDataBase.insert(TableName_Groups, null, values);
            }
        }

        // updation in contacts
        // String WHERE = "remote_jid='" + groupid + "'";
        // ContentValues valuesFor_Contact = new ContentValues();
        // valuesFor_Contact.put("display_name", newName);
        //
        // myDataBase.update(TableName_Contacts, valuesFor_Contact, WHERE,
        // null);
    }

    public ArrayList<Contactmodel> getAllContact() {
        // TODO Auto-generated method stub
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        String query = "SELECT * FROM " + TableName_Contacts
                + " where isregister=1 OR isregister=0";
        Cursor cursor = null;
        try {

            cursor = myDataBase.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Contactmodel model = new Contactmodel();
                model.setName(cursor.getString(cursor
                        .getColumnIndex("display_name")));
                model.setRemote_jid(cursor.getString(cursor
                        .getColumnIndex("remote_jid")));
                model.setNumber(cursor.getString(cursor
                        .getColumnIndex("phonenumber")));
                model.setStatus(cursor.getString(cursor
                        .getColumnIndex("status")));
                model.setCustomStatus(cursor.getString(cursor
                        .getColumnIndex("status_custom")));
                String lastseen = cursor.getString(cursor
                        .getColumnIndex("lastseen"));
                model.setIsRegister(cursor.getInt(cursor
                        .getColumnIndex("isregister")));
                if (lastseen != null) {
                    model.setLastseen(lastseen);
                }

                String avatarName = cursor.getString(cursor
                        .getColumnIndex("profilepic_name"));
                byte[] pic = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));
                if (pic != null) {
                    model.setAvatar(pic);
                }
                model.setImageUrl(avatarName);
                model.setIsStranger(cursor.getInt(cursor
                        .getColumnIndex("isstranger")));

                arrayListOfContacts.add(model);
            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }
        return arrayListOfContacts;
    }

    public HashMap<String, Contactmodel> getRegistersavedContactfromDB() {
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        String query = "SELECT * FROM " + TableName_Contacts
                + " where isregister=1";
        Cursor cursor = null;
        HashMap<String, Contactmodel> Dbmap = new HashMap<String, Contactmodel>();

        try {
            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Contactmodel model = new Contactmodel();
					/*
					 * model.setRemote_jid(cursor.getString(cursor
					 * .getColumnIndex("remote_jid")));

model.setNumber(cursor.getString(cursor
        .getColumnIndex("phonenumber")));
        model.setName(cursor.getString(cursor
        .getColumnIndex("display_name")));

        Dbmap.put(model.getNumber(), model);
        }
        }
        } catch (Exception e) {
        e.printStackTrace();
        }
        if (cursor != null) {
        cursor.close();
        }
        return Dbmap;
        }

         public HashMap<String, Contactmodel> getUnRegistersavedContactfromDB() {
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        String query = "SELECT * FROM " + TableName_Contacts
                + " where isregister=0 AND isgroup=0";
        Cursor cursor = null;
        HashMap<String, Contactmodel> Dbmap = new HashMap<String, Contactmodel>();

        try {
            cursor = myDataBase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Contactmodel model = new Contactmodel();
					/*
					 * model.setRemote_jid(cursor.getString(cursor
					 * .getColumnIndex("remote_jid")));

model.setNumber(cursor.getString(cursor
        .getColumnIndex("phonenumber")));
        model.setName(cursor.getString(cursor
        .getColumnIndex("display_name")));

        Dbmap.put(model.getNumber(), model);
        }
        }
        } catch (Exception e) {
        e.printStackTrace();
        }
        if (cursor != null) {
        cursor.close();
        }
        return Dbmap;
        }

         public void insertSinglecontactinSMS19DB(String name, String phone) {
        ContentValues valuesFor_Contact = new ContentValues();
        valuesFor_Contact.put("phonenumber", phone);
        valuesFor_Contact.put("display_name", name);
        valuesFor_Contact.put("isregister", 0);
        valuesFor_Contact.put("isstranger", 0);
        valuesFor_Contact.put("issms19_number", 1);
        valuesFor_Contact.put("unread_msg_count", 0);
        String remote_jid = (phone + "@" + GlobalData.HOST).trim();
        valuesFor_Contact.put("remote_jid", remote_jid);

        try {

            myDataBase.insert(TableName_Contacts, null, valuesFor_Contact);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

     public void deleteContactDisplayName(String rowid) {
		/*
		 * myDataBase .delete(TableName_Contacts, "rowid=?", new String[] {
		 * rowid });


    String query = "delete  from " + TableName_Contacts
            + " where display_name='" + rowid + "'";
myDataBase.execSQL(query);

        }

public ArrayList<Contactmodel> getContactfromDBOnlySMS19() {
        // TODO Auto-generated method stub
        ArrayList<Contactmodel> arrayListOfContacts = new ArrayList<Contactmodel>();
        HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();
        String query = "SELECT * FROM " + TableName_SMS19_Contacts
        + " where isregister=0 AND isgroup=0";
        String userOwenJid = "";
        if (InAppMessageActivity.myModel != null) {
        userOwenJid = InAppMessageActivity.myModel.getRemote_jid();
        }

        Cursor cursor = null;
        try {

        cursor = myDataBase.rawQuery(query, null);
        while (cursor.moveToNext()) {
        Contactmodel model = new Contactmodel();
        model.setName(cursor.getString(cursor
        .getColumnIndex("display_name")));
        model.setRemote_jid(cursor.getString(cursor
        .getColumnIndex("remote_jid")));
        model.setStatus(cursor.getString(cursor
        .getColumnIndex("status")));
        model.setNumber(cursor.getString(cursor
        .getColumnIndex("phonenumber")));
        model.setCustomStatus(cursor.getString(cursor
        .getColumnIndex("status_custom")));
        String lastseen = cursor.getString(cursor
        .getColumnIndex("lastseen"));
        model.setIsRegister(cursor.getInt(cursor
        .getColumnIndex("isregister")));
        if (lastseen != null) {
        model.setLastseen(lastseen);
        }
        model.setIsUserblock(cursor.getInt(cursor
        .getColumnIndex("isuserblock")));

        String avatarName = cursor.getString(cursor
        .getColumnIndex("profilepic_name"));
        byte[] pic = cursor.getBlob(cursor
        .getColumnIndex("profilepic_data"));
        if (pic != null) {
        model.setAvatar(pic);
        }
        model.setImageUrl(avatarName);
        model.setIsStranger(cursor.getInt(cursor
        .getColumnIndex("isstranger")));

        model.setIsUserblock(cursor.getInt(cursor
        .getColumnIndex("isuserblock")));
        if ((!userOwenJid.trim().equalsIgnoreCase(
        cursor.getString(cursor.getColumnIndex("remote_jid"))
        .trim()))) {
        if (!hashMap.containsKey(model.getRemote_jid())) {
        hashMap.put(model.getRemote_jid(), model);
        arrayListOfContacts.add(model);
        }

        }

        }
        if (cursor != null) {
        cursor.close();
        }

        } catch (Exception e) {
        if (cursor != null) {
        cursor.close();
        }
        e.printStackTrace();
        }
        return arrayListOfContacts;
        }


         public void updatestatusOfRecieveMessage(String remotjid,
                                             String msgpacketid, String status_msg) {
        String query = "";

        query = "UPDATE Messages SET sent_msg_success='4' where "
                + "message_packetid='" + msgpacketid + "'";

        myDataBase.execSQL(query);
    }

    public void updateSentMessagePacketId(String remotjid, String msgpacketid,
                                          String status_msg) {
        String query = "";
        // if(status_msg.equals("deliver"))

        String Where = "remote_jid='" + remotjid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("message_packetid", msgpacketid);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);

    }

     public String getMinrowid(String remoteid) {
        String rowid = "";
        // SELECT max(rowid), * FROM Messages where
        // remote_jid='+919358251234@128.199.155.62'
        String query = "SELECT min(rowid),* FROM " + TableName_SMS19_Contacts
		/* + " where remote_jid='" + remoteid + "'" ;
Cursor cursor = null;
        try {

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
        cursor.moveToFirst();
        rowid = String.valueOf(cursor.getLong(cursor
        .getColumnIndex("min(rowid)")));
        }
        if (cursor != null) {
        cursor.close();
        }
        } catch (Exception e) {
        e.printStackTrace();
        }
        return rowid;
        }
public int getHistoryOfMedia(String remoteid) {
        int count = 0;
        // String query = "SELECT * FROM " + TableName_Messages+
        // " where remote_jid='" + remoteid + "'";

        String query = "SELECT * FROM " + TableName_Messages
        + " where remote_jid=1 AND media_type=I";

        Cursor cursor = null;
        try {

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
        count = cursor.getCount();
        }
        if (cursor != null) {
        cursor.close();
        }
        } catch (Exception e) {
        e.printStackTrace();
        }
        return count;
        }

          public void updatestatusOfsendFileinDB() {
        String query = "UPDATE Messages SET media_name='S' where media_name='R'";
        myDataBase.execSQL(query);
    }

    public void isBroadCastUpdateBaseMsgId(String packetId, String isBroadCast,
                                           String inAppCount, String inInboxCount) {

        String Where = "message_packetid='" + packetId + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("isBroadCast", isBroadCast);
        newValues.put("inAppCount", inAppCount);
        newValues.put("inInboxCount", inInboxCount);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);

    }

      public void updateFileIsSent(String message_packetid, String sent) {
        String query = "";
        // if(status_msg.equals("deliver"))

        String Where = "message_packetid='" + message_packetid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("sent_msg_success", sent);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);
        // query=
        // "UPDATE OfflineMessages SET message_packetid='"+msgpacketid+"' where remote_jid='"+remotjid;

        // myDataBase.execSQL(query);
    }

    public void packetIdUpdate(String message_packetid, String updatedid) {

        String Where = "message_packetid='" + message_packetid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put("message_packetid", updatedid);

        int id = myDataBase.update(TableName_Messages, newValues, Where, null);

    }

    public ArrayList<Groupmodel> getGroupAddWithParticularUserDB(String jid) {
        ArrayList<Groupmodel> memberlist = new ArrayList<Groupmodel>();

        Log.e("Group", "getGroupAddWithParticularUserDB");

        String query = "SELECT * FROM " + TableName_Groups
                + " where createdbyme=1  OR createdbyme=0";

        Cursor cursor = null;
        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String remoteid = cursor.getString(cursor
                        .getColumnIndex("group_jid"));
                if (getGroupListWithUserDB(remoteid, jid)) {
                    Groupmodel model = getsingleGroupfromDB(remoteid);

                    if (model != null) {
                        memberlist.add(model);
                    }
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return memberlist;
    }

     public ArrayList<BroadCastSentBean> getBroadcastMembersWithStatusDB(
            String bid) {
        ArrayList<BroadCastSentBean> memberlist = new ArrayList<BroadCastSentBean>();
        String query = "SELECT * FROM " + TableName_Groups
                + " where group_jid='" + bid + "'";
        Cursor cursor = null;

        cursor = myDataBase.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String list = cursor.getString(cursor
                    .getColumnIndex("group_members"));

            String lists[] = list.split(",");
            for (int i = 0; i < lists.length; i++) {
                String remotejid = lists[i];
                BroadCastSentBean bean = new BroadCastSentBean();
                bean.setJid(remotejid);
                if (GlobalData.dbHelper.isRegister(remotejid)) {
                    bean.setIsregister("1");
                } else {
                    bean.setIsregister("1");
                }

                memberlist.add(bean);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return memberlist;
    }

    public void updateContactsinDB(JSONArray data) {
        // TODO Auto-generated method stub
        // myDataBase.delete(TableName_Contacts, null, null);
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject singleContact = data.getJSONObject(i);
                Contactmodel model = new Contactmodel();
                ContentValues valuesFor_Contact = new ContentValues();

				/*
                 * String number = singleContact.getString("username").trim();
				 * String remote_jid = (number + "@" + GlobalData.HOST).trim();
				 * String name = singleContact.getString("contactname").trim();


    String number = "+91"
            + singleContact.getString("Contact_Mobile").trim();
    String remote_jid = (number + "@" + GlobalData.HOST).trim();
    String name = singleContact.getString("Contact_Name").trim();

model.setNumber(number);
        model.setRemote_jid(remote_jid);
        model.setName(name);
        valuesFor_Contact.put("phonenumber", number);

        valuesFor_Contact.put("remote_jid", remote_jid);
        valuesFor_Contact.put("display_name", name);
        valuesFor_Contact.put("isregister", 1);
        String WHERE = "phonenumber='" + number + "'";
        // String Where = "remote_jid='" + remote_jid + "'";
        // insertSinglecontactinDB(name, number);
        myDataBase.update(TableName_Contacts, valuesFor_Contact, WHERE,
        null);
        GlobalData.registerContactList.add(model);
        } catch (Exception e) {
        e.printStackTrace();
        }
        }

		/*
         * if(GlobalData.registerContactList!=null){ Utils.addingRosterentry();
		 * }


        ConstantFields.Contactinsertsuccess = true;

        }
*/