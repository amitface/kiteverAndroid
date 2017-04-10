package sms19.listview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseDetails extends SQLiteOpenHelper {

    // DATABASE NAME
    private static final String databaseName = "Sms19";

    // TABLE NAME
    private static final String LOGIN = "login";
    private static final String TEMPLATE = "Template";
    private static final String TEMPLATE_ALL = "TemplateAll";
    private static final String CONTACT_ALL = "ContactAll";
    private static final String GROUP_ALL = "GroupAll";
    private static final String RECEIPENT_TABLE_NAME = "ResList";
    private static final String SELECTED_TEMPLATE_TABLE = "SelectedTEMP";
    private static final String SELECTED_GROUPS = "Selected_Group";
    private static final String GROUPSCONTACTS = "groupcontact";
    private static final String GROUP_EDIT_CONTACT = "editContactGroup";
    private static final String ALL_TEMPLATE = "getalldetailtemplate";

    private static final String EDIT_PROFILE = "EditProfile";
    private static final String Template_Custom = "Custom";
    private static final String Inbox_Read = "InboxRead";
    private static final String INBOX_WHATSUP = "Newfeauture";
    private static final String REFERFND_DB = "referearn";
    private static final String PROFILE_PICS = "ProfilePics";
    private static final String REG_CONTACT_CHAT_SEND = "Reg_chat_send";
    private static final String SELECTED_REG_CONTACT = "Sel_Reg_cnt";
    private static final String MERCHANT_INFO = "merchant";
    private static final String NOTI_CATION = "notification";
    private static final String INBOX_GROUP = "Groupinbox";
    private static final String RUN_SERVICE = "outboxservice";
    private static final String OPT_INOUT = "OptInOut";
    private static final String SAVE_CONTACTS = "savecontacts";
    private static final String ADD_REFER_PEOPLE = "referedPeople";
    // table for store tempary data

    // table for store time
    private static final String TIME_STORE = "timestore";
    // store base url indatabase
    private static final String BASE_ADDRESS = "URL_BASE";

    // Object of SQLITE_DATABASE
    public SQLiteDatabase db;

    /**********************
     * TABLE KEY OR COLUMN NAME
     ************************************************************/
    // LOGIN TABLE KEY OR COLUMN
    private static final String KEYID = "id";
    private static final String MOBILE = "mobile";
    private static final String STATUS = "status";
    private static final String USERID = "uid";
    private static final String USRLOGIN = "userlogin";
    private static final String BALANCE = "balance";
    private static final String KEYPASSWORD = "password";
    private static final String LOGIN_TYPE = "LoginType";

    // TEMPLATE TABLE KEY OR COLUMN
    // {"UserTemplateList":[{"Template_Title":"morning"}]}
    private static final String KEY_USERID_TEM = "userid";
    private static final String TEMPLATECATEGORY = "TEMPLATENAME";

    // BASE URL
    private static final String KEY_BASE_ID = "id";
    private static final String KEY_BASE_URL = "BASE_URL";

    // TEMPLATE_ALL KEY OR COLUMN
    // {"UserTemplate":[{"user_id":1,"M_ID":10000,"Template_ID":20,"Template_Title":"HELLO","Template":"HELLO"}
    private static final String KEY_USERID_TEM_ALL = "userid";
    private static final String KEY_TEM_ID_ALL = "TemplateId";
    private static final String KEY_TEM_TITLE_ALL = "TemplateTitle";
    private static final String KEY_TEM_ALL = "Template";

    // CONTACT_ALL KEY OR COLUMN
    // {"ContactDetails":[{"contact_id":106210094,"contact_name":"raj singh","contact_mobile":"8527805415","contact_email":null}]}
    private static final String KEY_USERID_CONTACT_ALL = "UserId";
    private static final String KEY_CONTACT_ID_ALL = "ContactId";
    private static final String KEY_CONTACT_NAME_ALL = "ContactName";
    private static final String KEY_CONTACT_MOBILE_ALL = "ContactMobile";
    private static final String KEY_CONTACT_STATUS_ALL = "checkedStatus";
    private static final String KEY_ADD_DOB = "dob";
    private static final String KEY_ADD_ANIV = "Aniversary";
    // GROUP_ALL KEY OR COLUMN

    private static final String KEY_USERID_GROUP_ALL = "UserId";
    private static final String KEY_GROUP_NAME_ALL = "Group_Name";
    private static final String KEY_GROUP_CONTACT_ALL = "Group_Contact";

    // RECEIPENT KEY OR COLUMN
    private static final String KEY_USERID_RES = "UserID";
    private static final String KEY_NUMBER = "Receipent_Number";
    private static final String KEY_NAME = "Receipent_Name";

    // SELECTED TEMPLATE KEY OR COLUMN
    private static final String KEY_ID = "ID";
    private static final String KEY_TEM_ID = "TEM_ID";
    private static final String KEY_TEM_DATA = "TEM_DATA";

    // Selected Group Name
    private static final String KEY_GROUP_SELECTED_NAME = "GroupsName";
    private static final String KEU_USERID = "UID";
    private static final String GROUP_COUNT = "GroupCount";
    private static final String NAME_ALL = "Groupname";
    private static final String CONTACT_ALL_GROUP = "GroupContact";
    private static final String USERIDG = "UserId";

    // GROUP_EDIT_CONTACT

    private static final String KEY_USERID_EDIT_GROUP = "userId";
    private static final String KEY_NAME_EDIT_GROUP = "GName";
    private static final String KEY_MOBILE_EDIT_GROUP = "Gnumber";

    // GET_ALL_TEMPLATE
    private static final String KEY_FOR_ALL = "uid";
    private static final String key_for_temid = "temid";
    private static final String KEY_FOR_TEMPLATE = "temname";
    private static final String KEY_FOR_TEM_TITLE = "temtitle";

    // EditProfile
    private static final String U_PF_ID = "profileid";
    private static final String KEY_PF_NM = "Nameprofile";
    private static final String Key_Mob = "Mobilepr";
    private static final String KEY_EMAIL = "Emailpf";
    private static final String ZIP_CODE = "Zipcode";
    private static final String MERC_CODE = "merchantcode";
    private static final String WEB_URL = "webaddress";
    private static final String DOB = "dob";
    private static final String COMPANY_NAME = "companyname";

    // Custom Template
    private static final String UID = "udd";
    private static final String CUSTOM = "customtemplate";

    // Inbox (Total contact of inApp User )
    private static final String Inbox_User_ID = "Inbox_User_ID";
    private static final String Inbox_Time = "Inbox_Time";
    private static final String Inbox_Message = "Inbox_Message";
    private static final String Inbox_Count_msg = "Inbox_Date";
    private static final String Inbox_Mobile = "Inbox_Mobile";
    private static final String Inbox_Name = "Inbox_Name";
    private static final String Inbox_Sender = "Inbox_Sender_ID";
    private static final String SELECETED_MEDIA_PATH = "SelectedMediapath";
    private static final String CHECKED_UNCHECKED = "checkedunchecked";

    // Inbox Read message (Total msg of single user communication)
    private static final String INBOX_MSG_TYPE = "SenderUserID";
    private static final String INBOX_MESSAGE = "messagein";
    private static final String INBOX_MSG_NUMBER = "messagenumber";
    private static final String INBOX_TIME = "messagetime";
    private static final String USERID_MSG = "AppUserid";
    private static final String INBOX_DATE = "idate";
    private static final String IBNBOX_URL = "Iurl";
    private static final String I_Msg_TYPE = "MType";
    private static final String I_MEDIA_TYPE = "MeType";
    private static final String SEND_STATUS = "SStatus";
    private static final String ALL_STATUS_USERID = "ReceiverUserID";
    private static final String CHECKBOX = "checkboxstatus";
    private static final String InboxOutBoxKey = "InboxOutBoxKey";

    // Refer Friend
    private static final String USERID_REFER = "referid";
    private static final String STATUS_REFERD = "referedstatus";
    private static final String NAME_REFERED = "referedname";
    private static final String NUMBER_REFERED = "referdnumber";
    private static final String DATE = "referingdate";

    // profile picks
    private static final String PICKS_ID = "ID";
    private static final String PICKS_URL = "PicksUrl";

    // REG_CONTACT_CHAT_SEND KEY OR COLUMN
    private static final String KEY_REG_INAPP_USERID = "InAppUserid";
    private static final String KEY_REG_CNT_USERID = "RegCntUserid";
    private static final String KEY_REG_CNT_NAME = "RegCntName";
    private static final String KEY_REG_CNT_NUMBER = "RegCntNumber";

    // SELECTED_REG_CONTACT KEY OR COLUMN
    private static final String KEY_SEL_INAPP_USERID = "SelInAppUserId";
    private static final String KEY_SEL_INAPP_USERLOGIN = "SelInAppUserLogin";
    private static final String KEY_SEL_INAPP_MOBILE = "SelInAppUserMobile";
    private static final String KEY_SEL_INAPP_PASSWORD = "SelInAppUserPassword";
    private static final String KEY_SEL_REG_CNT_USERID = "SelRegCntUserId";
    private static final String KEY_SEL_REG_CNT_NUMBER = "SelRegCntNumber";
    private static final String SELECTED_GROUP_NAME = "Selc_grpname";
    private static final String SELECT_NAME = "Sel_name";

    // MERCHANT INFORMATION

    private static final String KEY_MERCHENT_NM = "merchantname";
    private static final String KEY_WEB_URL = "websiteurl";
    private static final String KEY_MERCT_IMAGEURL = "imageurl";
    private static final String KEY_MER_ID = "muid";

    // Notify To All
    private static final String NOT_UID = "userid";
    private static final String MESSAGE = "message";
    private static final String TITLE = "title";
    private static final String LANDING = "redirecturl";
    private static final String TIME = "time";
    private static final String LINK = "link";

    // selected inbox group
    private static final String SEL_GP_ID = "uidgp";
    private static final String GROUP_SEL_NM = "selgpnmpath";
    private static final String TYPE_MEDIA = "mediatype";

    // optinOptOut
    private static final String Opt_id = "ID";
    private static final String Opt_status = "optinoptoutstatus";

    // OUTBOX SERVICE
    // Inbox Read message (Total msg of single user communication)
    private static final String OUTBOX_MSG_TYP = "SendrUsrID";
    private static final String OUTBOX_MESSAG = "message";
    private static final String OUTBOX_MSG_NUMBER = "number";
    private static final String OUTBOX_TIME = "time";
    private static final String OUTUSERID_MSG = "AppUrid";
    private static final String OUTBOX_DATE = "dte";
    private static final String OUTNBOX_URL = "Imurl";
    private static final String OUTMsg_TYPE = "MiType";
    private static final String OUTMEDIA_TYPE = "MeType";
    private static final String OUT_STATUS = "Status";
    private static final String OUT_STATUS_USERID = "ReceiverUsrID";
    private static final String CHECKED_STATUS_O = "checkedstatus";
    private static final String PageName = "pageName";
    private static final String OUTID = "outid";
    private static final String inboxOutID = "inboxOutID";
    private static final String GROUP_NAME_MSG = "groupnamemsg";
    private static final String SendReadStatus = "sendreadstatus";
    private static final String READCONTACTS = "savecontacts";
    // TIME STORE

    private static final String KEY_TIME_USERID = "TUserid";
    private static final String KEY_TIME_DAY = "TDay";
    private static final String KEY_TIME_HOURS = "THours";
    private static final String KEY_TIME_MINUTES = "TMInutes";
    private static final String KEY_TIME_SECONDS = "TSeconds";

    // save conts from phonebook
    private static final String UINID = "useridlist";
    private static final String LIST_NAME = "clistname";
    private static final String LIST_NUM = "clistnumber";

    // add refered people in db
    private static final String NEW_REFER_NAME = "refername";
    private static final String NEW_REFER_NUM = "refernumber";

    // add package selected details
    private static final String INSERTPACKAGE = "packagename";
    private static final String INSERT_POSITION = "positioninset";
    /********************* TABLE KEY OR COLUMN NAME ************************************************************/

    /********************************
     * TABLE CREATED QUERY
     ************************************************************/

    private static final String logintable = ("create table login(id integer primary key autoincrement, mobile text ,status text,uid text,balance number, password text,userlogin text, LoginType text)");
    private static final String TEMP = ("create table Template(userid text, TEMPLATENAME text)");
    private static final String TEMPALL = ("create table TemplateAll(userid text, TemplateId text, TemplateTitle text, Template text)");
    private static final String CONTACTALL = ("create table ContactAll(UserId text, ContactId text, ContactName text, ContactMobile text, checkedStatus text,dob text,Aniversary text)");
    private static final String GROUPALL = ("create table GroupAll(UserId text, Group_Name text, Group_Contact text)");
    private static final String RECIPIEPENT = ("create table ResList(UserID text, Receipent_Number text, Receipent_Name text)");
    private static final String SELECTEDTEMP = ("create table SelectedTEMP(ID text, TEM_ID text, TEM_DATA text)");
    private static final String SELECTED_Group = ("create table Selected_Group(GroupsName text,UID text,GroupCount integer)");
    private static final String SELECTED_ALL_GROUP = ("create table groupcontact(Groupname text,GroupContact text,UserId text)");
    private static final String EDIT_GROUP_CONTACT = ("create table editContactGroup(userId text, GName text, Gnumber text)");
    private static final String GET_ALL_TEMPLATE = ("create table getalldetailtemplate(uid text,temid text,temname text,temtitle text)");
    private static final String Opt_IN_Out = ("create table OptInOut(ID text,optinoptoutstatus text)");
    private static final String EDIT_PRF = ("create table EditProfile(profileid text,Nameprofile text,Mobilepr text,Emailpf text,Zipcode text,merchantcode text,webaddress text,dob text)");
    private static final String CUSTOM_TEMP = ("create table Custom(udd text,customtemplate text)");
    private static final String InboxTableQuery = ("create table InboxRead(Inbox_User_ID text, Inbox_Time text, Inbox_Message text, Inbox_Date text, Inbox_Mobile text, Inbox_Name text,Inbox_Sender_ID text,SelectedMediapath text,checkedunchecked text)");
    private static final String MESSAGE_READ = ("create table Newfeauture(messagein text,messagenumber text,messagetime text,AppUserid text,SenderUserID text,idate text, Iurl text, MType text, MeType text, SStatus text, ReceiverUserID text, checkboxstatus text,InboxOutBoxKey integer)");
    private static final String REFERFRIEND = ("create table referearn(referid text,referedstatus text,referedname text,referdnumber text,referingdate text)");
    private static final String PROFILE_PIC = ("create table ProfilePics(ID text,PicksUrl text)");
    private static final String REG_CNT_TAB = ("create table Reg_chat_send(InAppUserid text,RegCntUserid text,RegCntName text,RegCntNumber text)");
    private static final String SEL_REG_CNT_TAB = ("create table Sel_Reg_cnt(SelInAppUserId text,SelInAppUserLogin text,SelInAppUserMobile text,SelInAppUserPassword text,SelRegCntUserId text,SelRegCntNumber text,Selc_grpname text,Sel_name text)");
    private static final String SEL_MERCHANT_DATA = ("create table merchant(muid text,merchantname text,websiteurl text,imageurl text)");
    private static final String NOTIFICTN = ("create table notification(userid text,title text,message text,redirecturl text,time text,link text)");
    private static final String INBOX_SELCTET_GP = ("create table Groupinbox(uidgp text,selgpnmpath text,mediatype text)");
    private static final String OUTBOX_SR = ("create table outboxservice(message text,number text,time text,AppUrid text,SendrUsrID text,dte text, Imurl text, MiType text, MeType text, Status text, ReceiverUsrID text,checkedstatus text,pageName text,outid integer primary key autoincrement,inboxOutID integer,groupnamemsg text, sendreadstatus integer)");
    private static final String TEMP_OUTBOX = ("create table tempOutboxservice(message text,number text,time text,AppUrid text,SendrUsrID text,dte text, Imurl text, MiType text, MeType text, Status text, ReceiverUsrID text,checkedstatus text,pageName text,outid integer,inboxOutID integer,groupnamemsg text, sendreadstatus integer)");
    private static final String SAVECONTACTS = ("create table savecontacts(useridlist text,clistname text,clistnumber text)");
    private static final String TIME_INFO = ("create table timestore(TUserid text,TDay text,THours text,TMInutes text,TSeconds text)");
    private static final String REFERD_PEOPLE = ("create table referedPeople(refername text,refernumber text)");
    private static final String BASE_URL_TABLE = ("create table URL_BASE(id text,BASE_URL text)");
    private static final String PACKAGE_SELECT = ("create table getselectedpackage(packagename text,positioninset integer)");

    /********************************
     * TABLE CREATED QUERY
     ************************************************************/

    // Constructor of class with context
    public DataBaseDetails(Context context) {
        super(context, databaseName, null, 1);

    }

    // OnCreate method for create all table

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Use Table created Query
        db.execSQL(logintable);
        db.execSQL(TEMP);
        db.execSQL(TEMPALL);
        db.execSQL(CONTACTALL);
        db.execSQL(GROUPALL);
        db.execSQL(RECIPIEPENT);
        db.execSQL(SELECTEDTEMP);
        db.execSQL(SELECTED_Group);
        db.execSQL(SELECTED_ALL_GROUP);
        db.execSQL(EDIT_GROUP_CONTACT);
        db.execSQL(GET_ALL_TEMPLATE);
        db.execSQL(Opt_IN_Out);
        db.execSQL(EDIT_PRF);
        db.execSQL(CUSTOM_TEMP);
        db.execSQL(InboxTableQuery);
        db.execSQL(MESSAGE_READ);
        db.execSQL(REFERFRIEND);
        db.execSQL(PROFILE_PIC);
        db.execSQL(REG_CNT_TAB);
        db.execSQL(SEL_REG_CNT_TAB);
        db.execSQL(SEL_MERCHANT_DATA);
        db.execSQL(NOTIFICTN);
        db.execSQL(INBOX_SELCTET_GP);
        db.execSQL(OUTBOX_SR);
        db.execSQL(TEMP_OUTBOX);
        db.execSQL(TIME_INFO);
        db.execSQL(BASE_URL_TABLE);
        db.execSQL(REFERD_PEOPLE);
        db.execSQL(SAVECONTACTS);
        db.execSQL(PACKAGE_SELECT);
    }

    // Use for delete all table and recreate database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Use Table Name In Drop Table
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TEMPLATE);
        db.execSQL("DROP TABLE IF EXISTS " + TEMPLATE_ALL);
        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_ALL);
        db.execSQL("DROP TABLE IF EXISTS " + GROUP_ALL);
        db.execSQL("DROP TABLE IF EXISTS " + RECEIPENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SELECTED_TEMPLATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SELECTED_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + GROUPSCONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + GROUP_EDIT_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + ALL_TEMPLATE);

        db.execSQL("DROP TABLE IF EXISTS " + EDIT_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + Template_Custom);
        db.execSQL("DROP TABLE IF EXISTS " + Inbox_Read);
        db.execSQL("DROP TABLE IF EXISTS " + INBOX_WHATSUP);
        db.execSQL("DROP TABLE IF EXISTS " + REFERFND_DB);
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_PICS);
        db.execSQL("DROP TABLE IF EXISTS " + REG_CONTACT_CHAT_SEND);
        db.execSQL("DROP TABLE IF EXISTS " + SELECTED_REG_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + MERCHANT_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + NOTI_CATION);
        db.execSQL("DROP TABLE IF EXISTS " + INBOX_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + RUN_SERVICE);
        db.execSQL("DROP TABLE IF EXISTS " + ADD_REFER_PEOPLE);
        db.execSQL("DROP TABLE IF EXISTS " + TIME_STORE);
        db.execSQL("DROP TABLE IF EXISTS " + BASE_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + OPT_INOUT);
        db.execSQL("DROP TABLE IF EXISTS " + SAVE_CONTACTS);

        onCreate(db);

    }

    // Use for Open Database for do change in database
    public void Open() {
        db = this.getWritableDatabase();
    }

    /*************************************************
     * Base url
     **********************************/
    // ADD BASE URL TO DATABSE
    public long addBase_URl(String id, String B_URL) {

        long values;
        ContentValues cv = new ContentValues();

        cv.put(KEY_BASE_ID, id);
        cv.put(KEY_BASE_URL, B_URL);

        values = db.insert(BASE_ADDRESS, null, cv);
        return values;
    }

    // fetch base url details
    public Cursor fetchBaseurl() {
        Cursor c;
        c = db.query(BASE_ADDRESS, null, null, null, null, null, null);
        return c;

    }

    // UPDATE BASE URL TO DATABSE
    public long updateBase_URl(String id, String B_URL) {

        long values;
        ContentValues cv = new ContentValues();

        cv.put(KEY_BASE_ID, id);
        cv.put(KEY_BASE_URL, B_URL);

        values = db.update(BASE_ADDRESS, cv, null, null);
        return values;
    }

    // DELETE BASE URL DATA
    public void deleteCBaseURL() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + BASE_ADDRESS);
    }

    /************************************************* Base url **********************************/

    /*************************************************
     * Inbox chat
     **********************************/

    // Message Inbox
    public long InboxMessageAll(String message, String number, String time,
                                String AppUserid, String SenderUserID, String date, String I_URL,
                                String MsgType, String MediaType, String SendStatus,
                                String receiverUserId, String checkboxStatus, int inboxKey) {
        long values;
        ContentValues cv = new ContentValues();
        cv.put(INBOX_DATE, date);
        cv.put(INBOX_TIME, time);
        cv.put(INBOX_MESSAGE, message);
        cv.put(INBOX_MSG_NUMBER, number);
        cv.put(USERID_MSG, AppUserid);
        cv.put(INBOX_MSG_TYPE, SenderUserID);
        cv.put(IBNBOX_URL, I_URL);
        cv.put(I_Msg_TYPE, MsgType);
        cv.put(I_MEDIA_TYPE, MediaType);
        cv.put(SEND_STATUS, SendStatus);
        cv.put(ALL_STATUS_USERID, receiverUserId);
        cv.put(CHECKBOX, checkboxStatus);
        cv.put(InboxOutBoxKey, inboxKey);

        values = db.insert(INBOX_WHATSUP, null, cv);
        return values;
    }

    public Cursor getallmessages(String userid) {
        Cursor c;
        c = db.query(INBOX_WHATSUP, null, INBOX_MSG_NUMBER + "=" + "'" + userid
                + "'", null, null, null, null);
        return c;

    }

    public Cursor getallsendmsg(String userid, String status) {
        Cursor c;
        c = db.query(INBOX_WHATSUP, null,
                SEND_STATUS + "=" + "'" + status.trim() + "'", null, null,
                null, null);
        return c;

    }

    public long updateSendDataTable(int id) {
        ContentValues cv = new ContentValues();
        cv.put(SEND_STATUS, "0");

        long genID;
        // Log.w("DELETE CHECK","DELETE CHECK"+INBOX_WHATSUP+","+ cv+","
        // +InboxOutBoxKey+"="+"'"+id+"'");
        genID = db.update(INBOX_WHATSUP, cv, InboxOutBoxKey + "=" + "'" + id
                + "'", null);
        return genID;
    }

    public Cursor getallmessageschat() {
        Cursor c;
        c = db.query(INBOX_WHATSUP, null, null, null, null, null, null);
        return c;

    }

    /*****************************
     * //DELETE ALL DATA FROM TABLE public void deleteAllData(){
     *
     * SQLiteDatabase db = this.getWritableDatabase(); db.execSQL("DELETE FROM "
     * +INBOX_WHATSUP);
     *
     * }
     *
     * /*************************************************Inbox chat
     **********************************/

    /***********************************
     * TEMPLATE
     *******************************************/
    // NEW CUSTOM TEMPLATE
    public long customtemplate(String uid, String msg) {
        long values;
        ContentValues cv = new ContentValues();
        cv.put(UID, uid);
        cv.put(CUSTOM, msg);
        values = db.insert(Template_Custom, null, cv);
        return values;
    }

    // Get Templates
    public Cursor getCustomTemplates() {
        Cursor c;
        c = db.query(Template_Custom, null, null, null, null, null, null);
        return c;
    }

    // DELETE ALL DATA FROM TABLE
    public void deleteCustomTemplate() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Template_Custom);

    }

    /*********************************** TEMPLATE *******************************************/

    /*********************************************
     * OPT_IN_OUT
     ******************************************/
    // NEW CUSTOM TEMPLATE
    public long addOptinout(String id, String optStatus) {
        long values;
        ContentValues cv = new ContentValues();

        cv.put(Opt_id, id);
        cv.put(Opt_status, optStatus);

        values = db.insert(OPT_INOUT, null, cv);
        return values;
    }

    public long updateOptinout(String id, String optStatus) {

        ContentValues cv = new ContentValues();
        cv.put(Opt_id, id);
        cv.put(Opt_status, optStatus);

        long genID;
        genID = db.update(OPT_INOUT, cv, Opt_id + "=" + "'" + id + "'", null);
        return genID;

    }

    // Get Templates
    public Cursor getOptINOUT() {
        Cursor c;
        c = db.query(OPT_INOUT, null, null, null, null, null, null);
        return c;
    }

    // DELETE ALL DATA FROM TABLE
    public void deleteOptINOUT() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + OPT_INOUT);

    }

    /*********************************************
     * OPT_IN_OUT
     ******************************************/

    // ADD TEMPLATE DETAILS
    public long addTemplate(String userid, String tempname) {
        // Log.w("TAG","VALUE:"+"userid:"+userid+":tempname:"+tempname);

        long values;
        ContentValues cv = new ContentValues();

        cv.put(KEY_USERID_TEM, userid);
        cv.put(TEMPLATECATEGORY, tempname);

        values = db.insert(TEMPLATE, null, cv);
        return values;

    }

    // Get Templates
    public Cursor getTemplates() {
        Cursor c;
        c = db.query(TEMPLATE, null, KEY_USERID_TEM, null, null, null, null);
        return c;

    }

    // DELETE TEMPLATE
    public void DeleteTemplateData() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TEMPLATE);
    }

    /*********************************** TEMPLATE *******************************************/

    /***********************************
     * LOGIN
     *******************************************/
    // ADD LOGIN DETAILS
    public long addTologin(String mobile, String status, String userid,
                           String balance, String password, String userlogin, String LoginType) {
        // Log.w("TAG","VALUE:"+"Mobile:"+mobile+":Status:"+status+":UISERID:"+userid+":BALANCE:"+balance+":LOGIN_TYPE:"+LoginType);

        long value;
        ContentValues cv = new ContentValues();

        cv.put(MOBILE, mobile);
        cv.put(STATUS, status);
        cv.put(USERID, userid);
        cv.put(BALANCE, balance);
        cv.put(KEYPASSWORD, password);
        cv.put(USRLOGIN, userlogin);
        cv.put(LOGIN_TYPE, LoginType);

        value = db.insert(LOGIN, null, cv);

        return value;

    }

    // GET LOGIN DETAILS
    public Cursor getLoginDetails() {
        Cursor c = db.query(LOGIN, null, USERID, null, null, null, null);
        return c;
    }

    // UPDATE PASSWORD

    public long updateUserpassword(String pwd) {
        // Cursor c;
        SQLiteDatabase db = this.getWritableDatabase();
        // Log.w("TAG","VALUE:"+"pwd:"+pwd);

        ContentValues cv = new ContentValues();
        cv.put(KEYPASSWORD, pwd);

        long genID;
        genID = db.update(LOGIN, cv, KEYPASSWORD + "=" + "'" + pwd + "'", null);
        return genID;

    }

    // DELETE LOGIN DATA
    public void deleteLoginData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + LOGIN);

    }

    // UPDATE DATA FROM TABLE
    public long updateUserMobile(String userMobile) {
        // Cursor c;
        SQLiteDatabase db = this.getWritableDatabase();
        // Log.w("TAG","VALUE:"+"userMobile:"+userMobile);

        ContentValues cv = new ContentValues();
        cv.put(MOBILE, userMobile);

        long genID;
        genID = db.update(LOGIN, cv, MOBILE + "=" + "'" + userMobile + "'",
                null);
        return genID;

    }

    /*********************************** LOGIN *******************************************/
    /***********************************
     * ALL TEMPLATE
     *******************************************/
    // ADD ALL TEMPLATE
    public long addtoAlltemplate(String uid, String temid, String templatename,
                                 String temtitle) {
        // Log.w("TAG","VALUE:"+"userid:"+uid+"TemplateId:"+temid+"templatename:"+templatename+"temtitle:"+temtitle);

        long value;
        ContentValues cv = new ContentValues();

        cv.put(KEY_FOR_ALL, uid);
        cv.put(key_for_temid, temid);
        cv.put(KEY_FOR_TEMPLATE, templatename);
        cv.put(KEY_FOR_TEM_TITLE, temtitle);

        value = db.insert(ALL_TEMPLATE, null, cv);

        return value;
    }

    public void updateTemp(String tempTitle, String val, String tempId) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_FOR_TEMPLATE, val);
        db.update(ALL_TEMPLATE, cv, key_for_temid + "=" + "'" + tempId + "'", null);
    }

    public void deleteTemp(String tempId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ALL_TEMPLATE + " Where "
                + key_for_temid + "=" + "'" + tempId + "'" );
    }

    /*public void updateTemp(String tempTitle, String val, String previousVal) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_FOR_TEMPLATE, val);
        db.update(ALL_TEMPLATE, cv, KEY_FOR_TEM_TITLE + "=" + "'" + tempTitle
                + "'" + " and " + KEY_FOR_TEMPLATE + "=" + "'" + previousVal
                + "'", null);

    }

    public void deleteTemp(String temtitle, String template) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ALL_TEMPLATE + " Where "
                + KEY_FOR_TEM_TITLE + "=" + "'" + temtitle + "'" + " and "
                + KEY_FOR_TEMPLATE + "=" + "'" + template + "'");
    }*/

    // GET_ALL_TEMPLATE
    public Cursor GETALLTEMPLATE() {
        Cursor c;
        c = db.query(ALL_TEMPLATE, null, null, null, null, null, null);
        return c;
    }

    // get template template title
    public Cursor getTemplate(String temtitle) {
        Cursor c;
        c = db.query(ALL_TEMPLATE, null, KEY_FOR_TEM_TITLE + "=" + "'"
                + temtitle + "'", null, null, null, null);
        return c;
    }

    // DELETE TEMPLATE ALL
    public void DeleteALLTemplateDataALL() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ALL_TEMPLATE);
    }

    /*********************************** ALL TEMPLATE *******************************************/
    /***********************************
     * TEMPLATE ALL
     *******************************************/
    // ADD TEMPLATE ALL DETAILS
    public long addTemplateAll(String userid, String TemplateId,
                               String TemplateTitle, String Template) {
        // Log.w("TAG","VALUE:"+"userid:"+userid+"TemplateId:"+TemplateId+"TemplateTitle:"+TemplateTitle+"Template:"+Template);

        long values;
        ContentValues cv = new ContentValues();

        cv.put(KEY_USERID_TEM_ALL, userid);
        cv.put(KEY_TEM_ID_ALL, TemplateId);
        cv.put(KEY_TEM_TITLE_ALL, TemplateTitle);
        cv.put(KEY_TEM_ALL, Template);

        values = db.insert(TEMPLATE_ALL, null, cv);
        return values;

    }

    // GET TEMPLATE ALL
    public Cursor getTemplatesALL() {
        Cursor c;
        c = db.query(TEMPLATE_ALL, null, KEY_USERID_TEM_ALL, null, null, null,
                null);
        return c;

    }

    // DELETE TEMPLATE ALL
    public void DeleteTemplateDataALL() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TEMPLATE_ALL);
    }

    // GET TEMPLATE ALL
    public Cursor getALLTemplatesRegards_Tem_Title(String templateTitle) {
        Cursor c;
        c = db.query(TEMPLATE_ALL, null, KEY_TEM_TITLE_ALL + "=" + "'"
                + templateTitle + "'", null, null, null, null);
        return c;

    }

    /*********************************** TEMPLATE ALL *******************************************/

    /***********************************
     * CONTACT ALL
     *******************************************/
    // ADD CONTACT ALL DETAILS
    public long addContactAll(String userid, String ContactId,
                              String ContactName, String ContactMobile, String checkedStatus,
                              String dob, String aniver) {
        // //Log.w("TAG","VALUE:"+"userid:"+userid+"ContactId:"+ContactId+"ContactName:"+ContactName+"ContactMobile:"+ContactMobile+"checkedStatus:"+checkedStatus);

        long values;
        ContentValues cv = new ContentValues();

        cv.put(KEY_USERID_CONTACT_ALL, userid);
        cv.put(KEY_CONTACT_ID_ALL, ContactId);
        cv.put(KEY_CONTACT_NAME_ALL, ContactName);
        cv.put(KEY_CONTACT_MOBILE_ALL, ContactMobile);
        cv.put(KEY_CONTACT_STATUS_ALL, checkedStatus);
        cv.put(KEY_ADD_DOB, dob);
        cv.put(KEY_ADD_ANIV, aniver);
        values = db.insert(CONTACT_ALL, null, cv);
        return values;

    }

    // GET CONTACT ALL
    public Cursor getContactALL() {
        Cursor c;
        c = db.query(CONTACT_ALL, null, KEY_USERID_CONTACT_ALL, null, null,
                null, null);
        return c;

    }

    // GET CONTACT ALL as a RANGE
    public Cursor getContactALLwithRange(String userID, String startRow,
                                         String endRow) {
        // Log.w("DB","DB ::::::::::::start"+startRow+","+endRow);

        Cursor c;
        c = db.query(CONTACT_ALL, null, KEY_USERID_CONTACT_ALL + "=" + "'"
                + userID + "'" + " LIMIT" + " " + "'" + startRow + "'" + ","
                + "'" + endRow + "'", null, null, null, null);
        return c;

    }

    // DELETE CONTACT ALL
    public void DeleteContactDataALL() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CONTACT_ALL);
    }

    // DELETE INDIVIDULE CONTACT
    public void DeleteIndividuleContact(String mobile) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CONTACT_ALL + " WHERE "
                + KEY_CONTACT_MOBILE_ALL + "=" + "'" + mobile + "'");
    }

    // DELETE INDIVIDULE CONTACT
    public Cursor SearchIndividuleContact(String mobile) {

        Cursor c;
        c = db.query(CONTACT_ALL, null, KEY_CONTACT_MOBILE_ALL + "=" + "'"
                + mobile + "'", null, null, null, null);
        return c;

    }

    /*********************************** CONTACT ALL *******************************************/

    // private static final String GROUPALL
    // =("create table GroupAll(UserId text, Group_ID text, Group_Name text)");

    /***********************************
     * GROUP ALL
     *******************************************/
    // ADD TEMPLATE ALL DETAILS
    public long addGroupAll(String UserId, String Group_Name,
                            String Group_Contact) {
        // Log.w("TAG","VALUE:"+"userid:"+UserId+"GroupId:"+"Group_Name:"+Group_Name+"Group_Contact:"+Group_Contact);

        long values;
        ContentValues cv = new ContentValues();

        cv.put(KEY_USERID_GROUP_ALL, UserId);
        cv.put(KEY_GROUP_NAME_ALL, Group_Name);
        cv.put(KEY_GROUP_CONTACT_ALL, Group_Contact);
        values = db.insert(GROUP_ALL, null, cv);
        return values;

    }

    // Get Templates ALL
    public Cursor getGroupALL() {
        Cursor c;
        c = db.query(GROUP_ALL, null, KEY_USERID_GROUP_ALL, null, null, null,
                null);
        return c;
    }

    // DELETE TEMPLATE ALL
    public void DeleteGroupDataALL() {
        // Log.w("GetAllGroupDetails", "GetAllGroupDetails database");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + GROUP_ALL);
    }

    // DELETE Specific TEMPLATE with groupName
    public void DeleteGroupNameBasedOnId(String gpname) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + GROUP_ALL + " WHERE " + KEY_GROUP_NAME_ALL
                + "=" + "'" + gpname + "'");
        db.close();
    }

    /*********************************** GROUP ALL *******************************************/

    /***********************************
     * RESPIENT NUMBER
     *******************************************/

    // ADD DATA INTO RECEIPENT TABLE
    public long addReceipent(String userID, String ReceipentNum, String Name) {

        // Log.w("TAG","ADD DATABASE RECEIPENT:"+"userid:"+userID+"ReceipentNum:"+"ReceipentNum:"+Name);

        long values;

        ContentValues cv = new ContentValues();

        cv.put(KEY_USERID_RES, userID);
        cv.put(KEY_NUMBER, ReceipentNum);
        cv.put(KEY_NAME, Name);

        values = db.insert(RECEIPENT_TABLE_NAME, null, cv);

        return values;
    }

    public Cursor getReceipentToUserid(String userid) {
        Cursor c;
        c = db.query(RECEIPENT_TABLE_NAME, null, KEY_USERID_RES + "=" + "'"
                + userid + "'", null, null, null, null);
        return c;
    }

    // GET DATA FROM RECEIPENT TABLE
    public Cursor getReceipent() {
        Cursor c;
        c = db.query(RECEIPENT_TABLE_NAME, null, KEY_USERID_RES, null, null,
                null, null);
        return c;
    }

    // DELETE ALL DATA FROM TABLE
    public void deleteselectedReceipent() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + RECEIPENT_TABLE_NAME);

    }

    public void deleteselectedcahtReceipent(String mobile) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INBOX_WHATSUP + " where "
                + INBOX_MSG_NUMBER + "=" + "'" + mobile + "'");

    }

    // Delete specific data from recipent list
    public void deleteNumberFromRecipient(String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + RECEIPENT_TABLE_NAME + " WHERE "
                + KEY_NUMBER + "=" + "'" + number + "'");
    }

    // UPDATE DATA FROM TABLE
    public long updateReceipent(int userID, String ReceipentNum) {
        // Cursor c;

        // Log.w("TAG","VALUE:"+"userid:"+userID+"ReceipentNum:"+ReceipentNum);

        ContentValues cv = new ContentValues();
        cv.put(KEY_USERID_RES, userID);
        cv.put(KEY_NUMBER, ReceipentNum);

        long genID;
        genID = db.update(RECEIPENT_TABLE_NAME, cv, KEY_USERID_RES + "=" + "'"
                + userID + "'", null);
        return genID;

    }

    /************************************ RECEIPENT ******************************************/

    /***********************************
     * SELECTED TEMPLATE
     *******************************************/
    // ADD selected Templates DETAILS
    public long addSELETEDTemplate(String id, String tempid, String tempname) {
        // Log.w("TAG","VALUE:"+"id:"+id+":tempid:"+tempid+":tempname:"+tempname);

        long values;
        ContentValues cv = new ContentValues();

        cv.put(KEY_ID, id);
        cv.put(KEY_TEM_ID, tempid);
        cv.put(KEY_TEM_DATA, tempname);

        values = db.insert(SELECTED_TEMPLATE_TABLE, null, cv);
        return values;
    }

    /*********************************** TEMPLATE **************************************************/
    /***********************************
     * Edit Profile
     **********************************************/

    // getall
    public long EditProfile(String id, String name, String moblie,
                            String gmail, String zipcode, String mercode, String webaddress,
                            String dobb) {
        Log.i("ann", "inserttodb :" + "id:" + id + ":name:" + name + ":mercode:" + mercode);

        long values;
        ContentValues cv = new ContentValues();
        cv.put(U_PF_ID, id);
        cv.put(KEY_PF_NM, name);
        cv.put(Key_Mob, moblie);
        cv.put(KEY_EMAIL, gmail);
        cv.put(ZIP_CODE, zipcode);
        cv.put(MERC_CODE, mercode);
        cv.put(WEB_URL, webaddress);
        cv.put(DOB, dobb);
        values = db.insert(EDIT_PROFILE, null, cv);
        return values;
    }

    public long EditProfile(String id, String name, String moblie,
                            String gmail, String zipcode, String mercode, String webaddress,
                            String dobb, String companyname) {
        // //Log.w("TAG","VALUE:"+"id:"+id+":tempid:"+tempid+":tempname:"+tempname);

        Log.i("ann", "inserttodb Profile:" + "id:" + id + ":name:" + name + ":mercode:" + mercode);
        long values;
        ContentValues cv = new ContentValues();
        cv.put(U_PF_ID, id);
        cv.put(KEY_PF_NM, name);
        cv.put(Key_Mob, moblie);
        cv.put(KEY_EMAIL, gmail);
        cv.put(ZIP_CODE, zipcode);
        cv.put(MERC_CODE, mercode);
        cv.put(WEB_URL, webaddress);
        cv.put(DOB, dobb);
        cv.put(COMPANY_NAME, companyname);
        values = db.insert(EDIT_PROFILE, null, cv);
        return values;
    }

    // get profile detail
    public Cursor getProfile() {
        Cursor c;
        c = db.query(EDIT_PROFILE, null, U_PF_ID, null, null, null, null);
        return c;

    }

    // Get selected Templates
    public Cursor getSELETEDTemplates() {
        Cursor c;
        c = db.query(SELECTED_TEMPLATE_TABLE, null, KEY_ID, null, null, null,
                null);
        return c;

    }

    // Delete profile information
    public void deletepfofiledata() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + EDIT_PROFILE);
    }

    // DELETE selected Templates
    public void DeleteSELETEDTemplateData() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SELECTED_TEMPLATE_TABLE);
    }

    /*********************************** Edit Profile **************************************************/

    /***********************************
     * SELECTED GROUP
     *******************************************/
    // ADD DATA INTO SELECTED GROUP
    public long addSelectedGroupName(String gp_sel_name, String uid, int gpcount) {
        long values;
        ContentValues cv = new ContentValues();

        cv.put(KEY_GROUP_SELECTED_NAME, gp_sel_name);
        cv.put(KEU_USERID, uid);
        cv.put(GROUP_COUNT, gpcount);
        values = db.insert(SELECTED_GROUPS, null, cv);
        return values;
    }

    // Delete SELECTED GROUP
    public void deleteSelectedGroup(String gpName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SELECTED_GROUPS + " WHERE "
                + KEY_GROUP_SELECTED_NAME + "=" + "'" + gpName + "'");
    }

    // get value from table
    public Cursor getSelectedGroupName() {
        Cursor c;
        c = db.query(SELECTED_GROUPS, null, null, null, null, null, null);
        return c;
    }

    public void SelectedGroupName() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SELECTED_GROUPS);
    }

    /*********************************** SELECTED GROUP *******************************************/

    /***********************************
     * EDIT GROUP CONTACT
     *******************************************/
    // Add value in table
    public long InsertValueEditGroup(String userId, String Gname, String Gmobile) {

        // Log.w("TAG","VALUES:-------------------------"+"NAME:"+Gname+"::GNUM:"+Gmobile);

        long genId;

        ContentValues cv = new ContentValues();
        cv.put(KEY_USERID_EDIT_GROUP, userId);
        cv.put(KEY_NAME_EDIT_GROUP, Gname);
        cv.put(KEY_MOBILE_EDIT_GROUP, Gmobile);

        genId = db.insert(GROUP_EDIT_CONTACT, null, cv);
        return genId;

    }

    // Get All value from table
    public Cursor getValueEditGroup() {
        Cursor c;
        c = db.query(GROUP_EDIT_CONTACT, null, KEY_USERID_EDIT_GROUP, null,
                null, null, null);
        return c;

    }

    // Delete All value from table
    public void deleteAllvalueEditGroup() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + GROUP_EDIT_CONTACT);

    }

    /*********************************** EDIT GROUP CONTACT *******************************************/

    /********************************
     * SelectedGroupContactsNameAndNumber
     ******************************/

    // Add data into selected contact from group
    public long addSelectedContactsFromGroup(String name, String numbers,
                                             String uid) {
        // Log.w("TAG","DATABASE ADD SELECTED:"+"NAME:"+name+"NUMBER:"+numbers+"UID:"+uid);

        long value;

        ContentValues cv = new ContentValues();

        cv.put(NAME_ALL, name);
        cv.put(CONTACT_ALL_GROUP, numbers);
        cv.put(USERIDG, uid);
        value = db.insert(GROUPSCONTACTS, null, cv);

        return value;
    }

    // get data into selectedGroupcontact
    public Cursor getSelectedGroupContacts() {
        Cursor c;
        c = db.query(GROUPSCONTACTS, null, USERIDG, null, null, null, null);
        return c;
    }

    // delete all selected contact from group
    public void deleteSelectedContactsFromGroup() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + GROUPSCONTACTS);
    }

    // Delete specific data from recipent list
    public void deleteNumberFromSelectedContacts(String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + GROUPSCONTACTS + " WHERE "
                + CONTACT_ALL_GROUP + "=" + "'" + number + "'");
    }

    /******************************** SelectedGroupContactsNameAndNumber ******************************/

    /********************************
     * Inbox
     ******************************************/
    // GET LOGIN DETAILS
    public Cursor getInboxDetails() {
        Cursor c;
        c = db.query(Inbox_Read, null, null, null, null, null, Inbox_Name
                + " ASC");
        return c;
    }

    // GET LOGIN DETAILS
    public Cursor getInboxDetailsDesc() {
        Cursor c;
        c = db.query(Inbox_Read, null, null, null, null, null, Inbox_Name
                + " DESC");
        return c;
    }

    // Add inbox item
    public long addInboxItem(String UserId, String time, String message,
                             String countmsg, String Sendermobile, String SenderName,
                             String senid, String mediapath, String checkedstatus) {
        long genId;

        // Log.w("DATABASE_INBOX_CONTACT_TIME","DATABASE_INBOX_CONTACT_TIME::::::::::::::::::(time)"+time+"UserId"+UserId+"MESSAGE"+message+"countmsg"+countmsg+Sendermobile+"Sendermobile"+SenderName+"SenderName"+"senid"+senid+"mediapath"+mediapath);

        ContentValues cvv = new ContentValues();

        cvv.put(Inbox_User_ID, UserId);
        cvv.put(Inbox_Time, time);
        cvv.put(Inbox_Message, message);
        cvv.put(Inbox_Count_msg, countmsg);
        cvv.put(Inbox_Mobile, Sendermobile);
        cvv.put(Inbox_Name, SenderName);
        cvv.put(Inbox_Sender, senid);
        cvv.put(SELECETED_MEDIA_PATH, mediapath);
        cvv.put(CHECKED_UNCHECKED, checkedstatus);

        genId = db.insert(Inbox_Read, null, cvv);

        return genId;
    }

    // Add inbox item
    public long updateInboxItem(String UserId, String time, String message,
                                String countmsg, String Sendermobile, String SenderName,
                                String senid) {
        long genId;

        ContentValues cvv = new ContentValues();

        cvv.put(Inbox_User_ID, UserId);
        cvv.put(Inbox_Time, time);
        cvv.put(Inbox_Message, message);
        cvv.put(Inbox_Count_msg, countmsg);
        cvv.put(Inbox_Mobile, Sendermobile);
        cvv.put(Inbox_Name, SenderName);
        cvv.put(Inbox_Sender, senid);

        genId = db.update(Inbox_Read, cvv, Inbox_Name + "=" + "'" + SenderName
                + "'", null);

        return genId;
    }

    public void updateInboxCountmsg(String Sendermobile, String countmsg) {

        SQLiteDatabase db = this.getWritableDatabase();

        String updateCountmsg = "UPDATE " + Inbox_Read + " SET "
                + Inbox_Count_msg + "=" + "'" + countmsg + "'" + " WHERE "
                + Inbox_Mobile + "=" + "'" + Sendermobile + "'";

        // Log.w("TAG","PATH:::::::"+updateCountmsg);

        db.execSQL(updateCountmsg);

    }

    public void deleteInboxItem(String senderName) {
        // Log.w("TAG","Delete_SENDER_NAME__11111111111111111111(DATA_BASE):"+senderName);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Inbox_Read + " WHERE " + Inbox_Name + "="
                + "'" + senderName + "'");
    }

    public void deleteInboxItemNumber(String senderNumber) {
        // Log.w("TAG","Delete_senderNumber__11111111111111111111(DATA_BASE):"+senderNumber);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Inbox_Read + " WHERE " + Inbox_Mobile + "="
                + "'" + senderNumber + "'");
    }

    /******************************** Inbox ******************************************/
    /********************************
     * ReferFriend
     ************************************/
    public long addintoreferdb(String uid, String name, String number,
                               String status, String date) {
        long gend;
        ContentValues cv = new ContentValues();
        cv.put(USERID_REFER, uid);
        cv.put(STATUS_REFERD, status);
        cv.put(NAME_REFERED, name);
        cv.put(NUMBER_REFERED, number);
        cv.put(DATE, date);
        gend = db.insert(REFERFND_DB, null, cv);
        return gend;

    }

    public Cursor getreferedfriend() {
        Cursor c;
        c = db.query(REFERFND_DB, null, USERID_REFER, null, null, null, null);
        return c;
    }

    /******************************** ReferFriend ************************************/

    /********************************
     * Profile picks
     ************************************/
    public long addProfilePicks(String uid, String filepath) {
        long gend;
        ContentValues cv = new ContentValues();
        cv.put(PICKS_ID, "1");
        cv.put(PICKS_URL, filepath);

        gend = db.insert(PROFILE_PICS, null, cv);
        return gend;

    }

    public Cursor getProfilepath() {
        Cursor c;
        c = db.query(PROFILE_PICS, null, PICKS_ID, null, null, null, null);
        return c;
    }

    // UPDATE Profile path
    public void updateProfilePath(String userPath) {
        SQLiteDatabase db = this.getWritableDatabase();

        String abc = "UPDATE " + PROFILE_PICS + " SET " + PICKS_URL + "=" + "'"
                + userPath + "'" + " WHERE " + PICKS_ID + "=" + "1";

        // Log.w("TAG","PATH:::::::"+abc);
        // update groupAll set Group_Name = "ram" WHERE Group_Contact =0

        db.execSQL(abc);
    }

    /******************************** Profile picks ************************************/

    /*************************************
     * Register Contact details(Send message)
     *******************************/
    public long addRegisterContactChat(String InAppUserId,
                                       String RegisterCntuserId, String RegisterCntname,
                                       String RegisterCntmobile) {

        long genID;

        ContentValues cv = new ContentValues();

        cv.put(KEY_REG_INAPP_USERID, InAppUserId);
        cv.put(KEY_REG_CNT_USERID, RegisterCntuserId);
        cv.put(KEY_REG_CNT_NAME, RegisterCntname);
        cv.put(KEY_REG_CNT_NUMBER, RegisterCntmobile);

        genID = db.insert(REG_CONTACT_CHAT_SEND, null, cv);
        return genID;
    }

    // fetch all data by inAppUserID
    public Cursor getRegUserDetails() {

        Cursor c;
        c = db.query(REG_CONTACT_CHAT_SEND, null, KEY_REG_INAPP_USERID, null,
                null, null, null);
        return c;
    }

    public void DeleteRegisterContactChat() {

        SQLiteDatabase dbDel = this.getWritableDatabase();

        dbDel.execSQL("DELETE FROM " + REG_CONTACT_CHAT_SEND);

    }

    /************************************* Register Contact details(Send message) *******************************/

    /*************************************
     * Selected Register Contact details(Send message)
     *******************************/
    public long addSelRegisterContactChat(String InAppUserId,
                                          String InAppUserLogin, String InAppMobile, String InAppPassword,
                                          String RegUserID, String RegNumber, String gpname, String uname) {

        long genID;

        ContentValues cv = new ContentValues();

        cv.put(KEY_SEL_INAPP_USERID, InAppUserId);
        cv.put(KEY_SEL_INAPP_USERLOGIN, InAppUserLogin);
        cv.put(KEY_SEL_INAPP_MOBILE, InAppMobile);
        cv.put(KEY_SEL_INAPP_PASSWORD, InAppPassword);
        cv.put(KEY_SEL_REG_CNT_USERID, RegUserID);
        cv.put(KEY_SEL_REG_CNT_NUMBER, RegNumber);
        cv.put(SELECTED_GROUP_NAME, gpname);
        cv.put(SELECT_NAME, uname);
        genID = db.insert(SELECTED_REG_CONTACT, null, cv);
        return genID;
    }

    // fetch all data by inAppUserID
    public Cursor getSelRegUserDetails() {

        Cursor c;
        c = db.query(SELECTED_REG_CONTACT, null, null, null, null, null, null);
        return c;
    }

    public void DeleteSelRegisterContactChat(String num) {

        SQLiteDatabase dbDel = this.getWritableDatabase();

        dbDel.execSQL("DELETE FROM " + SELECTED_REG_CONTACT + " WHERE "
                + KEY_SEL_REG_CNT_NUMBER + "=" + "'" + num + "'");

    }

    public void DeleteSelRegisterGroupChat(String gpName) {

        SQLiteDatabase dbDel = this.getWritableDatabase();

        dbDel.execSQL("DELETE FROM " + SELECTED_REG_CONTACT + " WHERE "
                + SELECTED_GROUP_NAME + "=" + "'" + gpName + "'");

    }

    public void DeleteSelRegCnt() {

        SQLiteDatabase dbDel = this.getWritableDatabase();

        dbDel.execSQL("DELETE FROM " + SELECTED_REG_CONTACT);

    }

    // ***************************************************
    // Time store
    public long addTimeInfo(String userId, String diffDay, String diffHours,
                            String diffMin, String diffSecond) {

        long genID;

        ContentValues cv = new ContentValues();
        cv.put(KEY_TIME_USERID, userId);
        cv.put(KEY_TIME_DAY, diffDay);
        cv.put(KEY_TIME_HOURS, diffHours);
        cv.put(KEY_TIME_MINUTES, diffMin);
        cv.put(KEY_TIME_SECONDS, diffSecond);

        genID = db.insert(TIME_STORE, null, cv);
        return genID;
    }

    public long updateTimeInfo(String userId, String diffDay, String diffHours,
                               String diffMin, String diffSecond) {

        long genID;

        ContentValues cv = new ContentValues();
        cv.put(KEY_TIME_DAY, diffDay);
        cv.put(KEY_TIME_HOURS, diffHours);
        cv.put(KEY_TIME_MINUTES, diffMin);
        cv.put(KEY_TIME_SECONDS, diffSecond);

        genID = db.update(TIME_STORE, cv, KEY_TIME_USERID + "=" + "'" + userId
                + "'", null);

        return genID;
    }

    // fetch all data by uid
    public Cursor getTimeInfo() {

        Cursor c;
        c = db.query(TIME_STORE, null, null, null, null, null, null);
        return c;
    }

    /******************************
     * Mercahnt Data
     *************************************/
    public long addmerchantinformation(String uid, String mercname,
                                       String website, String imagepath) {

        long genID;

        ContentValues cv = new ContentValues();
        cv.put(KEY_MER_ID, uid);
        cv.put(KEY_MERCHENT_NM, mercname);
        cv.put(KEY_WEB_URL, website);
        cv.put(KEY_MERCT_IMAGEURL, imagepath);

        genID = db.insert(MERCHANT_INFO, null, cv);
        return genID;
    }

    // fetch all data by uid
    public Cursor getMercahantData() {

        Cursor c;
        c = db.query(MERCHANT_INFO, null, null, null, null, null, null);
        return c;
    }

    // update merchant data

    public long updateMerchantData(String uid, String mercname, String website,
                                   String imagepath) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_MER_ID, uid);
        cv.put(KEY_MERCHENT_NM, mercname);
        cv.put(KEY_WEB_URL, website);
        cv.put(KEY_MERCT_IMAGEURL, imagepath);

        long genID;
        genID = db.update(MERCHANT_INFO, cv,
                KEY_MER_ID + "=" + "'" + uid + "'", null);
        return genID;

    }

    /************************************* Register Contact details(Send message) *******************************/
    /************************************
     * add notification detail
     *******************************************/

    public long addnotifiaction(String uid, String title, String message,
                                String landing, String tmenoti, String link) {
        long genID;
        ContentValues cv = new ContentValues();

        cv.put(NOT_UID, uid);
        cv.put(MESSAGE, message);
        cv.put(TITLE, title);
        cv.put(LANDING, landing);
        cv.put(TIME, tmenoti);
        cv.put(LINK, link);
        genID = db.insert(NOTI_CATION, null, cv);
        return genID;

    }



    public Cursor getnotify() {
        Cursor c;
        c = db.query(NOTI_CATION, null, NOT_UID, null, null, null, null);
        return c;
    }

    public void DeleteNoti(String setDate) {
        SQLiteDatabase dbDel = this.getWritableDatabase();

        dbDel.execSQL("DELETE FROM " + NOTI_CATION + " Where " + TIME + "="
                + "'" + setDate + "'");
        // Log.w("delete from database",""+"DELETE FROM " +NOTI_CATION
        // +" Where "+TITLE+"="+"'"+title+"'");
    }

    public long addSelectedGpNm(String uid, String gpname, String mediatype) {
        long genID;
        ContentValues cv = new ContentValues();

        cv.put(SEL_GP_ID, uid);
        cv.put(GROUP_SEL_NM, gpname);
        cv.put(TYPE_MEDIA, mediatype);

        genID = db.insert(INBOX_GROUP, null, cv);
        return genID;

    }

    public Cursor getselectedinboxgp() {
        Cursor c;
        c = db.query(INBOX_GROUP, null, null, null, null, null, null);
        return c;
    }

    public void DeleteGP() {
        SQLiteDatabase dbDel = this.getWritableDatabase();
        dbDel.execSQL("DELETE FROM " + INBOX_GROUP);

    }

    /******************************************************* TEMP OUTBOX ****************************************************/

    /*********************************************
     * EXPERIMENT
     *****************************************************************/
    // Outbox Data Addition
    public long addOutboxServiceData(String message, String number,
                                     String time, String date, String appuid, String SendrUsrID,
                                     String Imurl, String MiType, String MeType, String Status,
                                     String recpuid, String chedunckstatus, String pageName,
                                     int inoxoutID, String gpname, int sendreadStatus) {
        // attachmentmessage6656363659numbertimeNILdateNILappuid/storage/sdcard0/DCIM/Camera/IMG_20150801_195108.jpgSendrUsrID20717ImurlNILMiTypeS_MMeTypeIMAGEStatus0recpuidNIL
        // Log.w("INSERT INTO DATABASE","@@@@@@@@@ message:"+message+",number:"+number+",time:"+time+",date:"+date+",appuid:"+appuid+",SendrUsrID:"+SendrUsrID+",Imurl:"+Imurl+",MiType:"+MiType+",MeType:"+MeType+",Status:"+Status+",recpuid:"+recpuid+",pageName:"+pageName+",inoxoutID:"+inoxoutID+",sendreadStatus:"+sendreadStatus);

        long genid;

        ContentValues cv = new ContentValues();

        cv.put(OUTBOX_MSG_TYP, "SendrUsrID");
        cv.put(OUTBOX_MESSAG, message);
        cv.put(OUTBOX_MSG_NUMBER, number);
        cv.put(OUTBOX_TIME, time);
        cv.put(OUTUSERID_MSG, appuid);
        cv.put(OUTBOX_DATE, date);
        cv.put(OUTNBOX_URL, Imurl);
        cv.put(OUTMsg_TYPE, MiType);
        cv.put(OUTMEDIA_TYPE, MeType);
        cv.put(OUT_STATUS, Status);
        cv.put(OUT_STATUS_USERID, recpuid);
        cv.put(CHECKED_STATUS_O, chedunckstatus);
        cv.put(PageName, pageName);
        cv.put(inboxOutID, inoxoutID);
        cv.put(GROUP_NAME_MSG, gpname);
        cv.put(SendReadStatus, sendreadStatus);

        genid = db.insert(RUN_SERVICE, null, cv);

        return genid;

    }

    public void updateSendReadStatus(int id) {
        // Log.w("NEW","NEW ::::id"+id);

        int updatestatus = 1;
        SQLiteDatabase dbDel = this.getWritableDatabase();
        // Log.e("@@@@@@@@@@@","##########"+"UPDATE "
        // +RUN_SERVICE+" SET "+SendReadStatus+"="+updatestatus+" WHERE "+OUTID+"="+"'"+id+"'");
        dbDel.execSQL("UPDATE " + RUN_SERVICE + " SET " + SendReadStatus + "="
                + updatestatus + " WHERE " + OUTID + "=" + "'" + id + "'");

    }

    public Cursor getOutBox() {
        Cursor c;
        c = db.query(RUN_SERVICE, null, null, null, null, null, null);
        return c;
    }

    public void DeleteOutbox(String number, int id) {
        // Log.e("NEW","NEW ::::(number):"+number+",id:"+id);

        SQLiteDatabase dbDel = this.getWritableDatabase();
        dbDel.execSQL("DELETE FROM " + RUN_SERVICE + " where "
                + OUTBOX_MSG_NUMBER + "=" + "'" + number + "'" + " and "
                + OUTID + "=" + "'" + id + "'");

    }

    public void DeleteOutboxAll() {

        SQLiteDatabase dbDel = this.getWritableDatabase();
        dbDel.execSQL("DELETE FROM " + RUN_SERVICE);

    }

    public void DeleteOutboxbyGroupName(String gpName) {

        // Log.w("NEW","NEW ::::(groupname):"+gpName);

        SQLiteDatabase dbDel = this.getWritableDatabase();
        dbDel.execSQL("DELETE FROM " + RUN_SERVICE + " where " + GROUP_NAME_MSG
                + "=" + "'" + gpName + "'");
    }

    // ADD CONTACTS FROM PHONEBOPOK TO DATABASE
    public long addfromphonebook(String id, String name, String number) {
        long genid;
        ContentValues cv = new ContentValues();
        cv.put(UINID, id);
        cv.put(LIST_NAME, name);
        cv.put(LIST_NUM, number);
        genid = db.insert(SAVE_CONTACTS, null, cv);
        return genid;

    }

    public Cursor getinvitaioncontacts() {
        Cursor c = db.query(SAVE_CONTACTS, null, null, null, null, null, null);
        return c;

    }

    // add refered people in db
    public long addreferedPeople(String name, String number) {
        long genid;
        ContentValues cv = new ContentValues();
        cv.put(NEW_REFER_NAME, name);
        cv.put(NEW_REFER_NUM, number);
        genid = db.insert(ADD_REFER_PEOPLE, null, cv);
        return genid;

    }

    // delete all refered people from db
    public void deletereferedpeople(String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ADD_REFER_PEOPLE + " where "
                + NEW_REFER_NUM + "=" + "'" + number + "'");
    }

    public void deletereferedpeopleall() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ADD_REFER_PEOPLE);
    }

    public Cursor getreferedcontacts() {
        Cursor c = db.query(ADD_REFER_PEOPLE, null, null, null, null, null,
                null);
        return c;

    }

}