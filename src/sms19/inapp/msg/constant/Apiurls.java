package sms19.inapp.msg.constant;

public class Apiurls {

	public static final String API_BASE_URL = "http://103.25.130.241/v1/";
	public static final String ADMIN_USERNAME = "admin";
	public static final String ADMIN_PASSWORD = "aj@YBr@chatapp";

	/*
	 * public static final String API_BASE_URL = "http://email19.in/v1/"; public
	 * static final String ADMIN_USERNAME ="admin"; public static final String
	 * ADMIN_PASSWORD ="aj@YBr@2016";
	 */

	// public static String KIT19_BASE_URL
	// ="http://kit19.com/newservice.aspx?Page=";
	/*
	 * public static String KIT19_BASE_URL2
	 * ="http://kit19.com/newservice.aspx?Page=";
	 */
	public static String GET_BASE_URL = "NewService.aspx?Page=";
	public static String POST_BASE_URL = "postservice.aspx?Page=";

	/*
	 * public static String KIT19_BASE_URL ="http://sms19.info/"+GET_BASE_URL;
	 * public static String KIT19_POST_BASE_URL
	 * ="http://sms19.info/"+POST_BASE_URL;
	 */

	public static String KIT19_BASE_URL = "";
	public static String KIT19_POST_BASE_URL = "";

	public static String KIT19_BASE_URL2 = "http://sms19.info/NewService.aspx?Page=";

	/*
	 * Production(Live)
	 */
//	public static String FIRST_BASE_URL = "http://kit19.com/newservice.aspx?Page=AutomatedServicesURL";
	/*
	 * Development === TestServicesURL
	 */
	 public static String FIRST_BASE_URL="http://kit19.com/newservice.aspx?Page=TestServicesURL";

	//public static String FIRST_BASE_URL="http://kit19.com/newservice.aspx?Page=StageServicesURL";

	public static final String URL_GETCODE = API_BASE_URL + "adduser";
	public static final String URL_VERIFYCODE = API_BASE_URL + "verifyuser";
	public static final String URL_CHECKCONTACT = API_BASE_URL + "sendcontacts";
	public static final String URL_UPLOADFILE = API_BASE_URL + "uploadfiles";
	public static final String URL_SETSTATUS = API_BASE_URL + "setstatus";
	public static final String URL_GROUPLIST = API_BASE_URL + "chatroom_list";
	public static final String URL_GETADMINMESSAGE = API_BASE_URL
			+ "getmessage";
	public static final String URL_SENDTOADMINMESSAGE = API_BASE_URL
			+ "sendmessage";

	// public static final String URL_GETCOUNTRY_LIST =
	// "http://sms19.info/NewService.aspx?Page=getCountryList";

	// public static final String URL_JOINROOM =
	// "http://139.162.8.20:9090/plugins/restapi/v1/chatrooms";

	public static String getBasePostURL() {
		final String stringUrl2 = Apiurls.KIT19_BASE_URL.replace("?Page=", "");
		return stringUrl2;
	}

}
