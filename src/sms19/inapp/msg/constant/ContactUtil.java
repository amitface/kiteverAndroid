package sms19.inapp.msg.constant;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.kitever.app.context.BaseApplicationContext;

import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.PhoneValidModel;

public class ContactUtil {

	private static Context context;
	public static SharedPreferences chatPrefs;

	private static Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
	private static String _ID = ContactsContract.Contacts._ID;
	private static String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

	private static String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

	private static Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

	private static String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
	private static String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
	private static String EMAIL_ID=ContactsContract.CommonDataKinds.Email.DATA;
	
	private static Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
	private static String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
	private static String DATA = ContactsContract.CommonDataKinds.Email.DATA;
	static HashMap<String, Contactmodel> contactmap=null ,contactMapReturn = null;
	private static ContentResolver contentResolver;
	private static HashMap<String, Contactmodel> deletedContactList =new HashMap<String, Contactmodel>();
	private static String phoneNumber;
	private static String email;
	private static Contactmodel contact;
	public static boolean isInsertContactRunning=false;

	public static synchronized String getDeviceContact(Context ctx, SharedPreferences prfs) {
		try {
			context = ctx;
			contentResolver = ctx.getContentResolver();
			chatPrefs = prfs;
			Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,null);
			if (contactmap != null && contactmap.size() > 0) {
				contactmap.clear();
			}
			// Loop for every contact in the phone
			if (cursor != null && cursor.getCount() > 0) {
				try {
					contactmap = null;
					contactMapReturn = null;
					contactmap = new HashMap<String, Contactmodel>();
					contactMapReturn = moveToNextCursorMethod(cursor, ctx);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (contactMapReturn != null) {
				String contactstring = Utils.makeJsonarrayfromContactmap(contactMapReturn);
				GlobalData.ContactStringToSend = contactstring;
				return contactstring;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public synchronized static sms19.inapp.msg.model.PhoneValidModel validNumberForMoveToNext(String phone){
		boolean isValid=false;
		boolean isValidCode=true;
		String internationalFormat="";
		PhoneNumberUtil phoneUtil=null;
		String countryCodeName="IN";
		//		String countryCodeName by default should be set to the keypair value of the user's country instead of hardcoding
		// it to "IN"
		if(country_to_indicative!=null && country_to_indicative.size()>0){
		for ( String key : country_to_indicative.keySet() ) {
			if(phone.startsWith(key)){
					countryCodeName=country_to_indicative.get(key);
					isValidCode=false;
					break;
				}
			
			}
		}
		
		if(isValidCode){
			isValidCode=false;
			countryCodeName=country_to_indicative.get(Utils.getCountryCode(BaseApplicationContext.baseContext));
		}
		sms19.inapp.msg.model.PhoneValidModel model=new PhoneValidModel();
		phoneUtil = PhoneNumberUtil.getInstance();
		model.setNumber(false);
		try {
//			PhoneNumber phNumberProto1 = phoneUtil.;
			PhoneNumber phNumberProto = phoneUtil.parse(phone, countryCodeName);
			//int countryCode = phNumberProto.getCountryCode();
			//System.err.println("NumberParseException was thrown: " + countryCode);
			isValid = phoneUtil.isValidNumber(phNumberProto);			
			if (isValid) {
				internationalFormat = phoneUtil.format(phNumberProto,PhoneNumberFormat.NATIONAL).replace(" ", "");
				String code=	 "+"+phNumberProto.getCountryCode()+"".trim();
				 model.setNumber(false);
				
				if (phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).FIXED_LINE||phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).TOLL_FREE) {
					 internationalFormat=""; 
					 model.setNumber(false);
				 }
				 else{
					
					 if(internationalFormat.startsWith("0")){
						 internationalFormat=	 internationalFormat.substring(1, internationalFormat.length());
					 }
					 model.setPhone_number(internationalFormat);
					 model.setCountry_code(code);
					 model.setNumber(isValid);
					
				 }
			}

		} catch (Exception e) {
//			System.err.println("NumberParseException was thrown: " + e.toString());
			model.setNumber(false);
			return model;
		}
		return model;

	}
	
	private static Map<String, String> country_to_indicative = new HashMap<String, String>();
    public  static void  countryToIndicative()
    {
//    	Map<String, String> country_to_indicative = new HashMap<String, String>();
    	country_to_indicative.put( "+93","AF");
    	country_to_indicative.put( "+355","AL");
    	country_to_indicative.put( "+213","DZ");
    	country_to_indicative.put( "+1684","AS");
    	country_to_indicative.put( "+376","AD");
    	country_to_indicative.put( "+244","AO");
    	country_to_indicative.put( "+1264","AI");
    	country_to_indicative.put( "+1268","AG");
    	country_to_indicative.put( "+54","AR");
    	country_to_indicative.put( "+374","AM");
    	country_to_indicative.put( "+61","AU");
    	country_to_indicative.put( "+297","AW");
    	country_to_indicative.put( "+43","AT");
    	country_to_indicative.put( "+994","AZ");
    	country_to_indicative.put( "+1242","BS");
    	country_to_indicative.put( "+973","BH");
    	country_to_indicative.put( "+880","BD");
    	country_to_indicative.put( "+1246","BB");
    	country_to_indicative.put( "+375","BY");
    	country_to_indicative.put( "+32","BE");
    	country_to_indicative.put( "+501","BZ");
    	country_to_indicative.put( "+229","BJ");
    	country_to_indicative.put( "+1441","BM");
    	country_to_indicative.put( "+975","BT");
    	country_to_indicative.put( "+591","BO");
    	country_to_indicative.put( "+387","BA");
    	country_to_indicative.put( "+267","BW");
    	country_to_indicative.put( "+55","BR");
    	country_to_indicative.put( "+673","BN");
    	country_to_indicative.put( "+359","BG");
    	country_to_indicative.put( "+226","BF");
    	country_to_indicative.put( "+257","BI");
    	country_to_indicative.put( "+855","KH");
    	country_to_indicative.put( "+237","CM");
    	country_to_indicative.put( "+1","CA");
    	country_to_indicative.put( "+238","CV");
    	country_to_indicative.put( "+236","CF");
    	country_to_indicative.put( "+235","TD");
    	country_to_indicative.put( "+56","CL");
    	country_to_indicative.put( "+86","CN");
    	country_to_indicative.put( "+57","CO");
    	country_to_indicative.put( "+269","KM");
    	country_to_indicative.put( "+243","CD");
    	country_to_indicative.put( "+242","CG");
    	country_to_indicative.put( "+506","CR");
    	country_to_indicative.put( "+225","CI");
    	country_to_indicative.put( "+385","HR");
    	country_to_indicative.put( "+53","CU");
    	country_to_indicative.put( "+357","CY");
    	country_to_indicative.put( "+420","CZ");
    	country_to_indicative.put( "+45","DK");
    	country_to_indicative.put( "+253","DJ");
    	country_to_indicative.put( "+1767","DM");
    	country_to_indicative.put( "+1829","DO");
    	country_to_indicative.put( "+593","EC");
    	country_to_indicative.put( "+20","EG");
    	country_to_indicative.put( "+503","SV");
    	country_to_indicative.put( "+240","GQ");
    	country_to_indicative.put( "+291","ER");
    	country_to_indicative.put( "+372","EE");
    	country_to_indicative.put( "+251","ET");
    	country_to_indicative.put( "+679","FJ");
    	country_to_indicative.put( "+358","FI");
    	country_to_indicative.put( "+33","FR");
    	country_to_indicative.put( "+241","GA");
    	country_to_indicative.put( "+220","GM");
    	country_to_indicative.put( "+995","GE");
    	country_to_indicative.put( "+49","DE");
    	country_to_indicative.put( "+233","GH");
    	country_to_indicative.put( "+30","GR");
    	country_to_indicative.put( "+1473","GD");
    	country_to_indicative.put( "+502","GT");
    	country_to_indicative.put( "+224","GN");
    	country_to_indicative.put( "+245","GW");
    	country_to_indicative.put( "+592","GY");
    	country_to_indicative.put( "+509","HT");
    	country_to_indicative.put( "+504","HN");
    	country_to_indicative.put( "+36","HU");
    	country_to_indicative.put( "+354","IS");
    	country_to_indicative.put( "+91","IN");
    	country_to_indicative.put( "+62","ID");
    	country_to_indicative.put( "+98","IR");
    	country_to_indicative.put( "+964","IQ");
    	country_to_indicative.put( "+353","IE");
    	country_to_indicative.put( "+972","IL");
    	country_to_indicative.put( "+39","IT");
    	country_to_indicative.put( "+1876","JM");
    	country_to_indicative.put( "+81","JP");
    	country_to_indicative.put( "+962","JO");
    	country_to_indicative.put( "+7","KZ");
    	country_to_indicative.put( "+254","KE");
    	country_to_indicative.put( "+686","KI");
    	country_to_indicative.put( "+850","KP");
    	country_to_indicative.put( "+82","KR");
    	country_to_indicative.put( "+965","KW");
    	country_to_indicative.put( "+996","KG");
    	country_to_indicative.put( "+856","LA");
    	country_to_indicative.put( "+371","LV");
    	country_to_indicative.put( "+961","LB");
    	country_to_indicative.put( "+266","LS");
    	country_to_indicative.put( "+231","LR");
    	country_to_indicative.put( "+218","LY");
    	country_to_indicative.put( "+423","LI");
    	country_to_indicative.put( "+370","LT");
    	country_to_indicative.put( "+352","LU");
    	country_to_indicative.put( "+389","MK");
    	country_to_indicative.put( "+261","MG");
    	country_to_indicative.put( "+265","MW");
    	country_to_indicative.put( "+60","MY");
    	country_to_indicative.put( "+960","MV");
    	country_to_indicative.put( "+223","ML");
    	country_to_indicative.put( "+356","MT");
    	country_to_indicative.put( "+692","MH");
    	country_to_indicative.put( "+222","MR");
    	country_to_indicative.put( "+230","MU");
    	country_to_indicative.put( "+52","MX");
    	country_to_indicative.put( "+691","FM");
    	country_to_indicative.put( "+373","MD");
    	country_to_indicative.put( "+377","MC");
    	country_to_indicative.put( "+976","MN");
    	country_to_indicative.put( "+382","ME");
    	country_to_indicative.put( "+212","MA");
    	country_to_indicative.put( "+258","MZ");
    	country_to_indicative.put( "+95","MM");
    	country_to_indicative.put( "+264","NA");
    	country_to_indicative.put( "+674","NR");
    	country_to_indicative.put( "+977","NP");
    	country_to_indicative.put( "+31","NL");
    	country_to_indicative.put( "+64","NZ");
    	country_to_indicative.put( "+505","NI");
    	country_to_indicative.put( "+227","NE");
    	country_to_indicative.put( "+234","NG");
    	country_to_indicative.put( "+47","NO");
    	country_to_indicative.put( "+968","OM");
    	country_to_indicative.put( "+92","PK");
    	country_to_indicative.put( "+680","PW");
    	country_to_indicative.put( "+507","PA");
    	country_to_indicative.put( "+675","PG");
    	country_to_indicative.put( "+595","PY");
    	country_to_indicative.put( "+51","PE");
    	country_to_indicative.put( "+63","PH");
    	country_to_indicative.put( "+48","PL");
    	country_to_indicative.put( "+351","PT");
    	country_to_indicative.put( "+974","QA");
    	country_to_indicative.put( "+40","RO");
    	country_to_indicative.put( "+7","RU");
    	country_to_indicative.put( "+250","RW");
    	country_to_indicative.put( "+1869","KN");
    	country_to_indicative.put( "+1758","LC");
    	country_to_indicative.put( "+1784","VC");
    	country_to_indicative.put( "+685","WS");
    	country_to_indicative.put( "+378","SM");
    	country_to_indicative.put( "+239","ST");
    	country_to_indicative.put( "+966","SA");
    	country_to_indicative.put( "+221","SN");
    	country_to_indicative.put( "+381","RS");
    	country_to_indicative.put( "+248","SC");
    	country_to_indicative.put( "+232","SL");
    	country_to_indicative.put( "+65","SG");
    	country_to_indicative.put( "+421","SK");
    	country_to_indicative.put( "+386","SI");
    	country_to_indicative.put( "+677","SB");
    	country_to_indicative.put( "+252","SO");
    	country_to_indicative.put( "+27","ZA");
    	country_to_indicative.put( "+34","ES");
    	country_to_indicative.put( "+94","LK");
    	country_to_indicative.put( "+249","SD");
    	country_to_indicative.put( "+597","SR");
    	country_to_indicative.put( "+268","SZ");
    	country_to_indicative.put( "+46","SE");
    	country_to_indicative.put( "+41","CH");
    	country_to_indicative.put( "+963","SY");
    	country_to_indicative.put( "+992","TJ");
    	country_to_indicative.put( "+255","TZ");
    	country_to_indicative.put( "+66","TH");
    	country_to_indicative.put( "+670","TL");
    	country_to_indicative.put( "+228","TG");
    	country_to_indicative.put( "+676","TO");
    	country_to_indicative.put( "+1868","TT");
    	country_to_indicative.put( "+216","TN");
    	country_to_indicative.put( "+90","TR");
    	country_to_indicative.put( "+993","TM");
    	country_to_indicative.put( "+688","TV");
    	country_to_indicative.put( "+256","UG");
    	country_to_indicative.put( "+380","UA");
    	country_to_indicative.put( "+971","AE");
    	country_to_indicative.put( "+44","GB");
    	country_to_indicative.put( "+1","US");
    	country_to_indicative.put( "+598","UY");
    	country_to_indicative.put( "+998","UZ");
    	country_to_indicative.put( "+678","VU");
    	country_to_indicative.put( "+39","VA");
    	country_to_indicative.put( "+58","VE");
    	country_to_indicative.put( "+84","VN");
    	country_to_indicative.put( "+967","YE");
    	country_to_indicative.put( "+260","ZM");
    	country_to_indicative.put( "+263","ZW");
    	country_to_indicative.put( "+995","GE");
    	country_to_indicative.put( "+886","TW");
    	country_to_indicative.put( "+994","AZ");
    	country_to_indicative.put( "+373","MD");
    	country_to_indicative.put( "+252","SO");
    	country_to_indicative.put( "+995","GE");
    	country_to_indicative.put( "+61","AU");
    	country_to_indicative.put( "+61","CX");
    	country_to_indicative.put( "+61","CC");
    	country_to_indicative.put( "+672","NF");
    	country_to_indicative.put( "+687","NC");
    	country_to_indicative.put( "+689","PF");
    	country_to_indicative.put( "+262","YT");
    	country_to_indicative.put( "+590","GP");
    	country_to_indicative.put( "+590","GP");
    	country_to_indicative.put( "+508","PM");
    	country_to_indicative.put( "+681","WF");
    	country_to_indicative.put( "+689","PF");
    	country_to_indicative.put( "+682","CK");
    	country_to_indicative.put( "+683","NU");
    	country_to_indicative.put( "+690","TK");
    	country_to_indicative.put( "+44","GG");
    	country_to_indicative.put( "+44","IM");
    	country_to_indicative.put( "+44","JE");
    	country_to_indicative.put( "+1264","AI");
    	country_to_indicative.put( "+1441","BM");
    	country_to_indicative.put( "+246","IO");
    	country_to_indicative.put( "+1284","VG");
    	country_to_indicative.put( "+1345","KY");
    	country_to_indicative.put( "+500","FK");
    	country_to_indicative.put( "+350","GI");
    	country_to_indicative.put( "+1664","MS");
    	country_to_indicative.put( "+870","PN");
    	country_to_indicative.put( "+290","SH");
    	country_to_indicative.put( "+1649","TC");
    	country_to_indicative.put( "+1670","MP");
    	country_to_indicative.put( "+1","PR");
    	country_to_indicative.put( "+1684","AS");
    	country_to_indicative.put( "+1671","GU");
    	country_to_indicative.put( "+1340","VI");
    	country_to_indicative.put( "+852","HK");
    	country_to_indicative.put( "+853","MO");
    	country_to_indicative.put( "+298","FO");
    	country_to_indicative.put( "+299","GL");
    	country_to_indicative.put( "+594","GF");
    	country_to_indicative.put( "+590","GP");
    	country_to_indicative.put( "+596","MQ");
    	country_to_indicative.put( "+262","RE");
    	country_to_indicative.put( "+35818","AX");
    	country_to_indicative.put( "+297","AW");
    	country_to_indicative.put( "+599","AN");
    	country_to_indicative.put( "+47","SJ");
    	country_to_indicative.put( "+247","AC");
    	country_to_indicative.put( "+290","TA");
    	country_to_indicative.put( "+6721","AQ");
    	country_to_indicative.put( "+381","CS");
    	country_to_indicative.put( "+970","PS");
    	country_to_indicative.put( "+212","EH");
        
    }

	private synchronized   static  HashMap<String, Contactmodel> moveToNextCursorMethod(Cursor cursor, Context ctx) {

		// TODO Auto-generated method stub
		HashMap<String, Contactmodel> Dbmap = GlobalData.dbHelper
				.getsavedContactfromDB();
		HashMap<String, Contactmodel> DbmapUnRegister = GlobalData.dbHelper.getUnRegistersavedContactfromNewWithOutSMS19DB();
		Contactmodel contactAdable =null;

		if (cursor != null && cursor.getCount() != 0) {

			while (cursor.moveToNext()) {

				try {
					String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
					contentResolver = ctx.getContentResolver();
			
					contactAdable =null;
					int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
					
					if (hasPhoneNumber > 0) {

						Cursor cur = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI,null,
								ContactsContract.RawContacts.CONTACT_ID + "="
										+ Integer.valueOf(contact_id), null, null);

					
						Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
								Phone_CONTACT_ID + " = ?", new String[] { contact_id },
								null);


						if (phoneCursor != null && phoneCursor.getCount() != 0) {
							int count=0;
							int c = phoneCursor.getCount();
							while (phoneCursor.moveToNext()) {
								phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
								try {
									
									phoneNumber = phoneNumber.replaceAll("-", "");
									phoneNumber = phoneNumber.replaceAll("\\(", "");
									phoneNumber = phoneNumber.replaceAll("\\)", "");
									phoneNumber=phoneNumber.replaceAll("\\s+", "");
									// Log.e("SimCon:  After replace",""+number);
								} catch (Exception e) {
									
								}
								try {
									if (phoneNumber != null)
										phoneNumber = extractDigits(phoneNumber);
								} catch (Exception e) {
									e.printStackTrace();
								}


								if (phoneNumber != null&& phoneNumber.trim().length() != 0) {

									PhoneValidModel valisNo=validNumberForMoveToNext(phoneNumber);

									if(valisNo!= null&& valisNo.isNumber()){
										contactAdable = new Contactmodel();

										if (!phoneNumber.equals(ConstantFields.user_number)) {
											phoneNumber=valisNo.getPhone_number();
											contactAdable.setNumber(phoneNumber.replace(" ", "").trim().replace("(", "").replace(")", "").replace("-", "").trim());
											String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
//											String emailId = cursor.getString(cursor.getColumnIndex(EMAIL_ID));
											contactAdable.setName(name);
//											String str="";
											contactAdable.setCountry_code(valisNo.getCountry_code());
//											if(emailId!=null){
//												str=emailId;
//											}
//											contactAdable.setEmailId(emailId);
											//Utils.printLog("ContactName  is" + name+" Count "+count);
											//Utils.printLog("ContactName is no" + phoneNumber+" Count "+count);
										}
									}
								}
								count++	;
							}
						}
						if (phoneCursor != null) {
							phoneCursor.close();
						}
						cur.close();
					}
					
					if (contactAdable != null) {
					
						String jidofContact = (contactAdable.getCountry_code()+contactAdable.getNumber()) + "@"
								+ GlobalData.HOST;
						/*Amit */
						/*if(GlobalData.dbHelper.isSMS19Contact(jidofContact)){
							GlobalData.dbHelper.deleteContactFromContact(jidofContact);  // new add 3 feb
						}*/

                         String updateContactWithCode=contactAdable.getCountry_code()+contactAdable.getNumber();// new add 13 Apl2016

						if (!GlobalData.dbHelper.checkcontactisAlreadyexist(jidofContact)) {
							contactmap.put(updateContactWithCode, contactAdable);// new add 13 Apl2016
							String num =updateContactWithCode;
							if(!GlobalData.dbHelper.isContactNumber(num)){
//								GlobalData.dbHelper.insertSinglecontactinDB(
//										contactAdable.getName(), updateContactWithCode);
							}
						} 
						else {
							if (Dbmap.containsKey(jidofContact)) {// this number already registered but not have name 
								Contactmodel value=Dbmap.get(jidofContact);
								if(value.getName().trim().equalsIgnoreCase(contactAdable.getName().trim())){
									Dbmap.remove(jidofContact);
								}
							}
							
							// update contact comment m
							if(contactAdable!=null){
								if(!GlobalData.dbHelper.isRegister(jidofContact)){
									GlobalData.dbHelper.updateContactName(contactAdable.getCountry_code()+contactAdable.getNumber(), contactAdable.getName());
								}else{
									
									// If user already registered 
									
									if(GlobalData.dbHelper.getIsStranger(jidofContact)){
										if(contactAdable.getName()!=null&&(!contactAdable.getName().equalsIgnoreCase(""))){
											GlobalData.dbHelper.updateStrager(jidofContact, contactAdable.getName());
											
											try {
												Dbmap.remove(jidofContact);//remove register and stranger value
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
										}else{
											GlobalData.dbHelper.setContactasnotStranger(contactAdable);
										}
									}else{
										// mean user is registered but not Stranger
										try {
											GlobalData.dbHelper.updateStrager(jidofContact, contactAdable.getName());
											Dbmap.remove(jidofContact);//remove register and stranger value// add on 12Apl2016
										} catch (Exception e) {
											e.printStackTrace();
										}
										GlobalData.dbHelper.setContactasnotStranger(contactAdable);
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
			
				DbmapUnRegister = GlobalData.dbHelper.getUnRegistersavedContactfromNewWithOutSMS19DB();
				if(DbmapUnRegister.size()>0){
					contactmap.putAll(DbmapUnRegister);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (cursor != null) {
			cursor.close();
		}

		try {
			GlobalData.dbHelper.changeContactTostrangerinDB(Dbmap);
		} catch (Exception e) {
			e.printStackTrace();
			return contactmap;
		}
		return contactmap;
	}

	public static  String extractDigits(String src) {
		StringBuilder builder = new StringBuilder();
		String firstchar = src.substring(0, 1);
		for (int i = 0; i < src.length(); i++) {
			char c = src.charAt(i);

			if (Character.isDigit(c) || firstchar.equals("+")) {
				firstchar = "";
				builder.append(c);
			}
		}
		return Checkvalidnumber(builder.toString());
	}

	public static  String Checkvalidnumber(String number) {
		String validnumber = "";
		String Countrycode = chatPrefs.getString("countryCode", "");
		if (number.startsWith("+")) {
			validnumber = number.trim();
		} else if (number.startsWith("0")) {
			validnumber = (Countrycode + number.substring(1, number.length()))
					.trim();
		} else {
			validnumber = (Countrycode + number).trim();
		}

		return validnumber;
	}

	public static boolean  mucChatIs(String groupId){

		boolean isMucChat=false;

		if(GlobalData.globalMucChat!=null){
			isMucChat = GlobalData.globalMucChat.containsKey(groupId);
			return isMucChat;
		}

		return isMucChat;

	}
	

	public synchronized static sms19.inapp.msg.model.PhoneValidModel validNumberForInvite(String phone){
		boolean isValid=false;
		String internationalFormat="";
		PhoneNumberUtil phoneUtil=null;
		String countryCodeName="IN";
		boolean isValidCode=true;
		
		if(country_to_indicative!=null && country_to_indicative.size()>0){
		for ( String key : country_to_indicative.keySet() ) {
			if(phone.startsWith(key)){
					countryCodeName=country_to_indicative.get(key);
					isValidCode=false;
					break;
				}
			
			}
		}
		
		if(isValidCode){
			isValidCode=false;
			countryCodeName=country_to_indicative.get(Utils.getCountryCode(BaseApplicationContext.baseContext));
		}
		sms19.inapp.msg.model.PhoneValidModel model=new PhoneValidModel();
		phoneUtil = PhoneNumberUtil.getInstance();
		model.setNumber(false);
		try {
			PhoneNumber phNumberProto = phoneUtil.parse(phone, countryCodeName);
			int countryCode = phNumberProto.getCountryCode();
//			System.err.println("NumberParseException was thrown: " + countryCode);
			isValid = phoneUtil.isValidNumber(phNumberProto);			
			if (isValid) {
				internationalFormat = phoneUtil.format(phNumberProto,PhoneNumberFormat.NATIONAL).replace(" ", "");
				String code=	 "+"+phNumberProto.getCountryCode()+"".trim();
				 model.setNumber(false);
				
				if (phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).FIXED_LINE||phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).TOLL_FREE) {
					 internationalFormat=""; 
					 model.setNumber(false);
				 }
				 else{
					
					 if(internationalFormat.startsWith("0")){
						 internationalFormat=	 internationalFormat.substring(1, internationalFormat.length());
					 }
					 model.setPhone_number(internationalFormat);
					 model.setCountry_code(code);
					 model.setNumber(isValid);
					
				 }
			}

		} catch (Exception e) {
//			System.err.println("NumberParseException was thrown: " + e.toString());
			model.setNumber(false);
			return model;
		}
		return model;

	}

	public synchronized static sms19.inapp.msg.model.PhoneValidModel validNumberForGetContactApi(String phone){
		boolean isValid=false;
		String internationalFormat="";
		PhoneNumberUtil phoneUtil=null;
		String countryCodeName="IN";
		boolean isValidCode=true;
		
		if(country_to_indicative!=null && country_to_indicative.size()>0){
		for ( String key : country_to_indicative.keySet() ) {
			if(phone.startsWith(key)){
					countryCodeName=country_to_indicative.get(key);
					isValidCode=false;
					break;
				}
			
			}
		}
		
		if(isValidCode){
			isValidCode=false;
			countryCodeName=country_to_indicative.get(Utils.getCountryCode(BaseApplicationContext.baseContext));
		}
		sms19.inapp.msg.model.PhoneValidModel model=new PhoneValidModel();
		phoneUtil = PhoneNumberUtil.getInstance();
		model.setNumber(false);
		try {
			PhoneNumber phNumberProto = phoneUtil.parse(phone, countryCodeName);
			int countryCode = phNumberProto.getCountryCode();
//			System.err.println("NumberParseException was thrown: " + countryCode);
			isValid = phoneUtil.isValidNumber(phNumberProto);			
			if (isValid) {
				internationalFormat = phoneUtil.format(phNumberProto,PhoneNumberFormat.NATIONAL).replace(" ", "");
				String code=	 "+"+phNumberProto.getCountryCode()+"".trim();
				 model.setNumber(false);
				
				if (phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).FIXED_LINE||phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).TOLL_FREE) {
					 internationalFormat=""; 
					 model.setNumber(false);
				 }
				 else{
					
					 if(internationalFormat.startsWith("0")){
						 internationalFormat=	 internationalFormat.substring(1, internationalFormat.length());
					 }
					 model.setPhone_number(internationalFormat);
					 model.setCountry_code(code);
					 model.setNumber(isValid);
					
				 }
			}

		} catch (Exception e) {
//			System.err.println("NumberParseException was thrown: " + e.toString());
			model.setNumber(false);
			return model;
		}
		return model;

	}

}

/*public  static   Contactmodel getDataFromNativeContactsAdd(Cursor cursor,String contact_id, Context ctx) {

		try {
			contentResolver = ctx.getContentResolver();
			boolean hasPhoneNumberFlag = false;

			int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));



			if (hasPhoneNumber > 0) {


				Cursor cur = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI,null,ContactsContract.RawContacts.CONTACT_ID + "="
						+ Integer.valueOf(contact_id), null, null);

				// Query and loop for every phone number of the contact
				Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,Phone_CONTACT_ID + " = ?", new String[] { contact_id },
						null);
				int count = 0;
				if (phoneCursor != null && phoneCursor.getCount() != 0) {

					int c = phoneCursor.getCount();
					while (phoneCursor.moveToNext()) {

						phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
						String name222 = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
						if (name222 == null && name222.trim().length() == 0) {
							name222 = "";
						} else {
							name222 = name222.substring(0, 1).toUpperCase()
									+ name222.substring(1);

						}


						if(name222!=null&&name222.contains("Sir Personal")){
							Utils.printLogSMS("Contact Name   "+phoneNumber+"  count  "+count+"");
							Utils.printLogSMS("Contact Name  name222 "+name222+"  count  "+count+"");

						}

						try {
							if (phoneNumber != null)
								phoneNumber = extractDigits(phoneNumber);
						} catch (Exception e) {
							Utils.printLogSMS("Contact Name exc  "+phoneNumber);
							e.printStackTrace();
						}

						if (phoneNumber != null&& phoneNumber.trim().length() != 0) {

							String valisNo=validNumberCheck(phoneNumber);

							if(valisNo!= null&& valisNo.trim().length() != 0){

								if (!phoneNumber.equals(ConstantFields.user_number)) {
									phoneNumber=valisNo;
									Contactmodel contactAdable = new Contactmodel();
									contactAdable.setNumber(phoneNumber.replace(" ", "").trim().replace("(", "").replace(")", "").replace("-", "").trim());

									String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

									contactAdable.setName(name);
									String no=Utils.remove91(contactAdable.getNumber().trim());
									contactAdable.setNumber(no);

									//	nativeContactList.add(contactAdable);

								} else {
									hasPhoneNumberFlag = true;
									//break;
								}

							} else {
								hasPhoneNumberFlag = true;
								//	break;
							}
							} else {
								hasPhoneNumberFlag = true;
								break;
							}







						}

						count++;
					}

				}

				if (phoneCursor != null) {
					phoneCursor.close();
				}

				if (!hasPhoneNumberFlag) {

					String name = cursor.getString(cursor
							.getColumnIndex(DISPLAY_NAME));
					if (name == null && name.trim().length() == 0) {
						name = "";
					} else {
						name = name.substring(0, 1).toUpperCase()
								+ name.substring(1);

					}
					contact.setName(name);
					if(contact.getName().equalsIgnoreCase("Prashant G")){
						name = name.substring(0, 1).toUpperCase()
								+ name.substring(1);
					}

					if (cur != null) {
						cur.close();
					}
					return contact;

				}
				cur.close();
			}

			return null;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public synchronized static sms19.inapp.msg.model.PhoneValidModel validNumberCheckForBroadCast333(String phone){
		boolean isValid=false;
		String internationalFormat="";
		PhoneNumberUtil phoneUtil=null;
		String countryCodeName="IN";
		boolean isValidCode=true;

		if(country_to_indicative!=null && country_to_indicative.size()>0){
		for ( String key : country_to_indicative.keySet() ) {
			if(phone.startsWith(key)){
					countryCodeName=country_to_indicative.get(key);
					isValidCode=false;
					break;
				}

			}
		}

		if(isValidCode){
			isValidCode=false;
			countryCodeName=country_to_indicative.get(Utils.getCountryCode(BaseApplicationContext.baseContext));
		}
		sms19.inapp.msg.model.PhoneValidModel model=new PhoneValidModel();
		phoneUtil = PhoneNumberUtil.getInstance();
		model.setNumber(false);
		try {
			PhoneNumber phNumberProto = phoneUtil.parse(phone, countryCodeName);
			int countryCode = phNumberProto.getCountryCode();
//			System.err.println("NumberParseException was thrown: " + countryCode);
			isValid = phoneUtil.isValidNumber(phNumberProto);
			if (isValid) {
				internationalFormat = phoneUtil.format(phNumberProto,PhoneNumberFormat.NATIONAL).replace(" ", "");
				String code=	 "+"+phNumberProto.getCountryCode()+"".trim();
				 model.setNumber(false);

				if (phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).FIXED_LINE||phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).TOLL_FREE) {
					 internationalFormat="";
					 model.setNumber(false);
				 }
				 else{

					 if(internationalFormat.startsWith("0")){
						 internationalFormat=	 internationalFormat.substring(1, internationalFormat.length());
					 }
					 model.setPhone_number(internationalFormat);
					 model.setCountry_code(code);
					 model.setNumber(isValid);

				 }
			}

		} catch (Exception e) {
//			System.err.println("NumberParseException was thrown: " + e.toString());
			model.setNumber(false);
			return model;
		}
		return model;

	}
	public static  String getDeletedContact(Context ctx, SharedPreferences prfs) {
		context = ctx;
		contentResolver = ctx.getContentResolver();
		String contactstring ="";
		chatPrefs = prfs;
		Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,null);
		if (cursor != null && cursor.getCount() > 0) {
			HashMap<String,Contactmodel>hashMap=	getDeletedContact(cursor, ctx);
			contactstring = Utils.makeJsonarrayfromContactmap(hashMap);
		}
		return contactstring;

	}
	public static  HashMap<String, Contactmodel> getDeletedContact(Cursor cursor, Context ctx) {

		Contactmodel	contact2=null;
		deletedContactList=new HashMap<String, Contactmodel>();
		ArrayList<Contactmodel>  arrayListFromDb=	GlobalData.dbHelper.getAllContact();

		ArrayList<Contactmodel>  nativeContactList=	new ArrayList<Contactmodel>();


		if (cursor != null && cursor.getCount() != 0) {

			while (cursor.moveToNext()) {

				String contact_id = cursor.getString(cursor.getColumnIndex(_ID));

				contact2 = getDataFromNativeContacts(cursor, contact_id, ctx);
				if (contact2 != null) {

					String no=Utils.removeCountryCode(contact2.getNumber().trim(),ctx);
					contact2.setNumber(no);
					nativeContactList.add(contact2); //7 Apl

				}

			}


			for(int i=0;i<arrayListFromDb.size();i++){
				for(int j=0;j<nativeContactList.size();j++){

					String string1=Utils.removeCountryCode(nativeContactList.get(j).getNumber(),ctx);
					String string2=Utils.removeCountryCode(arrayListFromDb.get(i).getNumber(),ctx);

					if(string1.equalsIgnoreCase(string2)){


						break;

					}else{

						if(!nativeContactList.contains(arrayListFromDb.get(i)))	{
							deletedContactList.put(Utils.removeCountryCode(arrayListFromDb.get(i).getNumber(),ctx), arrayListFromDb.get(i));
							GlobalData.dbHelper.deleteContact(Utils.removeCountryCode(arrayListFromDb.get(i).getNumber(),ctx));
						}


					}


				}

			}



		}




		if (cursor != null) {
			cursor.close();
		}


		if(deletedContactList.size()>0){

		}


		return deletedContactList;

	}


	public static  String fetchDeletedContactFirst(Context ctx, SharedPreferences prfs) {
		context = ctx;
		contentResolver = ctx.getContentResolver();
		String contactstring ="";
		chatPrefs = prfs;


		Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,
				null);

		if (cursor != null && cursor.getCount() > 0) {
			fetchDeletedContact(cursor, ctx);

		}else{}


		return contactstring;

	}

	public static  void fetchDeletedContact(Cursor cursor, Context ctx){

		if(ctx!=null&&cursor!=null){
			HashMap<String, Contactmodel> Dbmap = GlobalData.dbHelper.getsavedContactfromDBAllWitouSms19();

			HashMap<String, Contactmodel> contactNative=new HashMap<String, Contactmodel>();
			Contactmodel contactAdable=null;

			if (cursor != null && cursor.getCount() != 0) {

				while (cursor.moveToNext()) {

					String contact_id = cursor.getString(cursor.getColumnIndex(_ID));

				//	contact = getDataFromNativeContacts(cursor, contact_id, ctx);

					contentResolver = ctx.getContentResolver();

					contactAdable =null;
					int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

					if (hasPhoneNumber > 0) {

						Cursor cur = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI,null,
								ContactsContract.RawContacts.CONTACT_ID + "="
										+ Integer.valueOf(contact_id), null, null);

						// Query and loop for every phone number of the contact
						Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
								Phone_CONTACT_ID + " = ?", new String[] { contact_id },
								null);


						if (phoneCursor != null && phoneCursor.getCount() != 0) {
							int count=0;
							int c = phoneCursor.getCount();



							while (phoneCursor.moveToNext()) {
								phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));



								try {
									if (phoneNumber != null)
										phoneNumber = extractDigits(phoneNumber);
								} catch (Exception e) {

									e.printStackTrace();
								}


								if (phoneNumber != null&& phoneNumber.trim().length() != 0) {

									PhoneValidModel valisNo=validNumberForDelete(phoneNumber);

									if(valisNo!= null&& valisNo.isNumber()){
										contactAdable = new Contactmodel();
										if (!phoneNumber.equals(ConstantFields.user_number)) {
											phoneNumber=valisNo.getPhone_number();
											contactAdable.setNumber(phoneNumber.replace(" ", "").trim().replace("(", "").replace(")", "").replace("-", "").trim());
											String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
											String emailId = cursor.getString(cursor.getColumnIndex(EMAIL_ID));
											contactAdable.setName(name);
											contactAdable.setCountry_code(valisNo.getCountry_code());
											contactAdable.setEmailId(emailId);
											//String no=Utils.remove91(contactAdable.getNumber().trim());

											//contactAdable.setNumber(no);
											//Utils.printLog("ContactName  is ---- fetch updated" + name);
											//Utils.printLog("ContactName is no ---- fetch updated" + phoneNumber);
											String id=(Utils.Add91(contactAdable.getNumber(),ctx)+ "@" + GlobalData.HOST).trim();
											contactNative.put(id, contactAdable);

										}

									}
								}



								count++;
							}

						}

						if (phoneCursor != null) {
							phoneCursor.close();
						}


						cur.close();
					}





				}


			}

			if (cursor != null) {
				cursor.close();
			}


			Set<String> arrayListRemoteId=	Dbmap.keySet();



			for (String newString : arrayListRemoteId) {
				if (!contactNative.containsKey(newString)) {
					if(!GlobalData.dbHelper.getIsStranger(newString)){

						if(!GlobalData.dbHelper.isRegister(newString)){//new
							GlobalData.dbHelper.DeleteContactRemoteIdBase(newString);
						}else{
							GlobalData.dbHelper.registerToStrager(newString);

						}
						//}
					}

				}
			}



		}
	}

	public  static   Contactmodel getDataFromNativeContacts(Cursor cursor,String contact_id, Context ctx) {

		try {
			contentResolver = ctx.getContentResolver();
			boolean hasPhoneNumberFlag = false;

			int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

			if (hasPhoneNumber > 0) {
				Contactmodel contact2 = new Contactmodel();
				String phoneNumber2="";
				Cursor cur = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI,null,
						ContactsContract.RawContacts.CONTACT_ID + "="
								+ Integer.valueOf(contact_id), null, null);

				// Query and loop for every phone number of the contact
				Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
						Phone_CONTACT_ID + " = ?", new String[] { contact_id },
						null);
				int count = 0;
				if (phoneCursor != null && phoneCursor.getCount() != 0) {

					int c = phoneCursor.getCount();
					while (phoneCursor.moveToNext()) {
						phoneNumber2 = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

						try {
							if (phoneNumber2 != null)
								phoneNumber2 = extractDigits(phoneNumber2);
						} catch (Exception e) {

							e.printStackTrace();
						}

						if (count == 0) {
							if (phoneNumber2 != null&& phoneNumber2.trim().length() != 0) {

								PhoneValidModel valisNo=validNumberCheck(phoneNumber2);

								if(valisNo!= null&& valisNo.isNumber()){

									if (!phoneNumber2.equals(ConstantFields.user_number)) {
										phoneNumber2=valisNo.getPhone_number();
										contact2.setNumber(phoneNumber2.replace(" ", "").trim().replace("(", "").replace(")", "").replace("-", "").trim());
									} else {
										hasPhoneNumberFlag = true;
										break;
									}
								} else {
									hasPhoneNumberFlag = true;
									break;
								}
							} else {
								hasPhoneNumberFlag = true;
								break;
							}
						}

						count++;
					}

				}

				if (phoneCursor != null) {
					phoneCursor.close();
				}

				if (!hasPhoneNumberFlag) {

					String name = cursor.getString(cursor
							.getColumnIndex(DISPLAY_NAME));
					if (name == null && name.trim().length() == 0) {
						name = "";
					} else {
						name = name.substring(0, 1).toUpperCase()
								+ name.substring(1);

					}
					contact2.setName(name);

					if (cur != null) {
						cur.close();
					}
					return contact2;

				}
				cur.close();
			}

			return null;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public synchronized static sms19.inapp.msg.model.PhoneValidModel validNumberForDelete(String phone){
		boolean isValid=false;
		String internationalFormat="";
		PhoneNumberUtil phoneUtil=null;
		String countryCodeName="IN";
		boolean isValidCode=true;
		if(country_to_indicative!=null && country_to_indicative.size()>0){
		for ( String key : country_to_indicative.keySet() ) {
			if(phone.startsWith(key)){
					countryCodeName=country_to_indicative.get(key);
					isValidCode=false;
					break;
				}

			}
		}

		if(isValidCode){
			isValidCode=false;
			countryCodeName=country_to_indicative.get(Utils.getCountryCode(BaseApplicationContext.baseContext));
		}
		sms19.inapp.msg.model.PhoneValidModel model=new PhoneValidModel();
		phoneUtil = PhoneNumberUtil.getInstance();
		model.setNumber(false);
		try {
			PhoneNumber phNumberProto = phoneUtil.parse(phone, countryCodeName);
	//		int countryCode = phNumberProto.getCountryCode();
	//		System.err.println("NumberParseException was thrown: " + countryCode);
			isValid = phoneUtil.isValidNumber(phNumberProto);
			if (isValid) {
				internationalFormat = phoneUtil.format(phNumberProto,PhoneNumberFormat.NATIONAL).replace(" ", "");
				String code=	 "+"+phNumberProto.getCountryCode()+"".trim();
				 model.setNumber(false);

				if (phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).FIXED_LINE||phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).TOLL_FREE) {
					 internationalFormat="";
					 model.setNumber(false);
				 }
				 else{

					 if(internationalFormat.startsWith("0")){
						 internationalFormat=	 internationalFormat.substring(1, internationalFormat.length());
					 }
					 model.setPhone_number(internationalFormat);
					 model.setCountry_code(code);
					 model.setNumber(isValid);

				 }
			}

		} catch (Exception e) {
//			System.err.println("NumberParseException was thrown: " + e.toString());
			model.setNumber(false);
			return model;
		}
		return model;

	}

		public synchronized static sms19.inapp.msg.model.PhoneValidModel validNumberCheck(String phone){
		boolean isValid=false;
		String internationalFormat="";
		PhoneNumberUtil phoneUtil=null;
		String countryCodeName="IN";
		boolean isValidCode=true;

		if(country_to_indicative!=null && country_to_indicative.size()>0){
		for ( String key : country_to_indicative.keySet() ) {
			if(phone.startsWith(key)){
					countryCodeName=country_to_indicative.get(key);
					isValidCode=false;
					break;
				}

			}
		}

		if(isValidCode){
			isValidCode=false;
			countryCodeName=country_to_indicative.get(Utils.getCountryCode(BaseApplicationContext.baseContext));
		}
		sms19.inapp.msg.model.PhoneValidModel model=new PhoneValidModel();
		phoneUtil = PhoneNumberUtil.getInstance();
		model.setNumber(false);
		try {
			PhoneNumber phNumberProto = phoneUtil.parse(phone, countryCodeName);
			int countryCode = phNumberProto.getCountryCode();
//			System.err.println("NumberParseException was thrown: " + countryCode);
			isValid = phoneUtil.isValidNumber(phNumberProto);
			if (isValid) {
				internationalFormat = phoneUtil.format(phNumberProto,PhoneNumberFormat.NATIONAL).replace(" ", "");
				String code=	 "+"+phNumberProto.getCountryCode()+"".trim();
				 model.setNumber(false);

				if (phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).FIXED_LINE||phoneUtil.getNumberType(phNumberProto) ==phoneUtil.getNumberType(phNumberProto).TOLL_FREE) {
					 internationalFormat="";
					 model.setNumber(false);
				 }
				 else{

					 if(internationalFormat.startsWith("0")){
						 internationalFormat=	 internationalFormat.substring(1, internationalFormat.length());
					 }
					 model.setPhone_number(internationalFormat);
					 model.setCountry_code(code);
					 model.setNumber(isValid);

				 }
			}

		} catch (Exception e) {
//			System.err.println("NumberParseException was thrown: " + e.toString());
			model.setNumber(false);
			return model;
		}
		return model;

	}
*/
