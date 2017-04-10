package sms19.inapp.msg.constant;

import org.jivesoftware.smack.PrivacyListManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.MessageEventManager;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.HashMap;

import it.sauronsoftware.ftp4j.FTPClient;
import sms19.inapp.msg.asynctask.GetContactListAsyncTask;
import sms19.inapp.msg.database.DatabaseHelper;
import sms19.inapp.msg.model.Contactmodel;

public class GlobalData {// smart cab id
    //public static final String GOOGLE_SENDER_ID = "614505744380";
    public static final String GOOGLE_SENDER_ID = "131865367353";

    public static final String Devicetype = "android";
    // public static final String USERLOGIN = "online";
    // public static final String USERLOGOUT = "away";
    public static String VideoPath = "";
    public static String ImagesPath = "";
    public static String profilepicPath = "";
    public static String merchantProfilepicPath = "";
    public static String msgToneuri = "";
    public static String groupmsgToneuri = "";
    public static boolean vibrate = false;
    public static boolean silent = false;
    public static final String Imagefile = "I";
    public static final String Videofile = "V";
    public static final String Audiofile = "A";
    public static final String Locationfile = "L";
    public static final String fileSendingordownloading = "R";
    public static final String fileSendordownloadSuccess = "S";
    public static final String filemessageString = "file_url_type_location";

    public static boolean OnFilesendscreen = false;

    //public static final String HOST = "127.0.0.1";//already host
    // public static final String HOST = "139.162.8.20";
    // public static final String HOST = "111.93.195.78";
//    public static final String HOST = "172.16.4.159";
    public static final String HOST = "email19.in";
    // public static final String HOST_NAME = "email19.in";
    public static final int PORT = 5222;
    // public static final int PORT = 9296;

    //public static final int PORT = 5222;
    // public static final int PORT = 6222;
    //public static final String SERVICE = "uchat.red";
    public static boolean loginSuccess = false;
    public static XMPPConnection connection;
    public static Roster roster;

    public static MessageEventManager msgEventManager;


    public static DatabaseHelper dbHelper;
    public static String ContactStringToSend = "";
    public static ArrayList<Contactmodel> registerContactList;
    public static boolean getnewContact = false;
    public static boolean ContactgetfromRegister = false;
    public static boolean OnContactsfrag = false;
    public static boolean OnChatfrag = false;
    public static boolean OnHomefrag = false;
    public static boolean Newgroup = false;
    public static boolean Newgroup_seninvit = false;
    public static boolean Newgroup_dbinsert = false;
    public static boolean Newgroup_joinandssendmsg = false;
    public static boolean FilterRecentlist = true;
    public static final int profilepicthmb = 200;
    public static final int filetransferthmb = 50;
    public static final int fileorignalWidth = 720;
    public static final int fileorignalheight = 1280;

    public static String shareFilepath = "";
    public static String shareFiletype = "";

    public static boolean addContactTodevice = false;
    public static String NumberAddtodevice = "";

    public static boolean Editgroup_seninvit = false;
    public static boolean Editgroup_dbinsert = false;
    public static String status_time_separater = "__time__";
    public static HashMap<String, MultiUserChat> globalMucChat = new HashMap<String, MultiUserChat>();


    public static GetContactListAsyncTask getContactListAsyncTask = null;
    public static String MESSAGE_TYPE_1 = "BROADCAST";
    public static String MESSAGE_TYPE_2 = "NORMAl";
    public static String MESSAGE_TYPE_IMAGE = "Image";
    public static String MESSAGE_TYPE_AUDIO = "Audio";
    public static String MESSAGE_TYPE_VIDEO = "Video";
    public static boolean CREATE_GROUP_FRAGMENT = false;
    public static boolean REGISTER_CONNECTION_SERVICE_ISON = false;
    public static String subject_gourp_name_changed = "group_name_changed";
    public static String subject_gourp_created_time = "group_created_time";
    public static String subject_gourp_icon_changed = "group_icon_changed";
    public static String subject_gourp_chat_message = "group_chat_message";

    public static String BUSY = "busy";
    public static String AVAILABLE = "available";
    public static String INVISIABLE = "invisable";
    public static String ONLY = "original";

    public static final String EXIT_TITLE = "Exit Member";
    public static final String EXIT_MESSAGE = "Confirmation of member exit?";
    public static final String DELETE_TITLE = "Delete Member";
    public static final String DELETE_MESSAGE = "Confirmation of member delete?";

    //public static ArrayList<Contactmodel> fromdevicepickedcontclist;

    public static ArrayList<Contactmodel> registerAndStragerGlobalArrayList = new ArrayList<Contactmodel>();

    public static String FTP_USER = "";
    public static String FTP_PASS = "";
    public static String FTP_HOST = "";
    public static String FTP_URL = "";
    public static FTPClient clientFtp;
    public static String CountryCode = "";
    public static int GROUP_PARTICIPANT_LIMIT = 50;
    public static int BRAODCAST_GROUP_PARTICIPANT_LIMIT = 50;
    public static int CONTACT_GROUP_PARTICIPANT_LIMIT = 10000;
    public static String PAGE_GROUP_TYPE = "contact";

    public static PrivacyListManager privacyManager;

    public static String romote_jid = "";
    public static String COUNTRY_CODE = "+91";
    public static String MESSAGE_STATUS = "delivered";
    public static HashMap<String, String> blockUserJid = new HashMap<String, String>();


}
