package sms19.inapp.msg.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;


import com.google.gson.GsonBuilder;

public class Rest {

	private static Rest _instance;
	private DefaultHttpClient httpclient;
	private List<NameValuePair> nameValuePairs;
	private HttpPost postRequest;
	private HttpGet getRequest;
	private HttpResponse httpResponse;
	private HttpEntity httpEntity;

	private Rest() {
	}

	public static Rest getInstance() {
		if (_instance == null) {
			_instance = new Rest();
		}

		return _instance;
	}

	private void initializeHttpClient() {
	
		httpclient = new DefaultHttpClient();
		nameValuePairs = new ArrayList<NameValuePair>();
	}
	
	private String postRequest2(String url, List<NameValuePair> list) {
		String response = "";
		
		try {
			
			postRequest = new HttpPost(url);
			DefaultHttpClient	httpclient2 = new DefaultHttpClient();
			postRequest.setEntity(new UrlEncodedFormEntity(list,HTTP.UTF_8));
			httpResponse = httpclient2.execute(postRequest);			 
			httpEntity = httpResponse.getEntity();
         
            
            try {
            	
    			if (httpEntity != null) {
    				response = EntityUtils.toString(httpEntity);
    			}
            	
    		} catch (Exception e) {
    			//Log.e("Buffer Error", "Error converting result " + e.toString()+"  ---response--- "+response);
    		}

            
            return response;
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	  

	private String sendGetRequest2(String url) {
		String response = "";
		try {

			getRequest = new HttpGet(url);
			
			Utils.printLog("Request:  " + url);

			httpResponse = httpclient.execute(getRequest);
			httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				response = EntityUtils.toString(httpEntity,HTTP.UTF_8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	public String sendGetRequest3(String url) {
		String response = "";
		try {

			DefaultHttpClient	httpclient2 = new DefaultHttpClient();
			getRequest = new HttpGet(url);
			
			Utils.printLog("Request:  " + url);

			httpResponse = httpclient2.execute(getRequest);
			httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				response = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	@SuppressWarnings("unused")
	private void setHeaderParam(String username, String password) {
		CredentialsProvider credProvider = new BasicCredentialsProvider();
		credProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST,
				AuthScope.ANY_PORT), new UsernamePasswordCredentials(username,
				password));
		httpclient.setCredentialsProvider(credProvider);
	}

	public String sendCheckcontactRequest(String c_code, String contacts) {
		
		initializeHttpClient();		
		Utils.printLog("check contact==>" + contacts);
		String response = sendGetRequest2(contacts);
	

		return response;
	}

	
	public String sendCheckcontactJsonFormat(String url,String user_id,JSONObject obj) throws JSONException, UnsupportedEncodingException {

		initializeHttpClient();
		
		nameValuePairs.add(new BasicNameValuePair("contacts",obj.toString() ));
	
		nameValuePairs.add(new BasicNameValuePair("Page","InsertContacts"));
		nameValuePairs.add(new BasicNameValuePair("Userid",user_id));
		
		
		//Utils.mCreateAndSaveFile(nameValuePairs.toString(),"AllData");
		
		Utils.printLog("Contact Post data:  " + nameValuePairs.toString());		
		
		//kit19.com/NewService.aspx
		
		Utils.printLog("url url:  " + url);	
		
		Utils.printLog2("nameValuePairs:\n" + nameValuePairs.toString());
		
		
		
		generateNoteOnSD("kit_"+System.currentTimeMillis(), nameValuePairs.toString());
		
		String response = postRequest2(url,nameValuePairs);
		
		generateNoteOnSD("kit_response", response);
		
		System.out.println("InsertContacts - "+nameValuePairs.toString());
		
		//Log.i("InsertContacts","----"+nameValuePairs.toString());
	
		Utils.printLog2("Contact Post data response:  " + response);	

		return response;
	}
	
	public void generateNoteOnSD( String sFileName, String sBody) {
	    try {
	        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
	        if (!root.exists()) {
	            root.mkdirs();
	        }
	        File gpxfile = new File(root, sFileName);
	        FileWriter writer = new FileWriter(gpxfile);
	        writer.append(sBody);
	        writer.flush();
	        writer.close();
	      //  Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	public String getBroadcastGroupList(String group_url) {
		
		initializeHttpClient();
		Utils.printLog("check group_url==>" + group_url);
		String response = sendGetRequest3(group_url);
		

		return response;
	}
	
	
	
	public String getXmppGroupList(String url, List<NameValuePair> list) {
		String response = "";
		  StringEntity se = null;
		try {
			DefaultHttpClient	httpclient2 = new DefaultHttpClient();
			postRequest = new HttpPost(url);			
			postRequest.setEntity(new UrlEncodedFormEntity(list,HTTP.UTF_8));
			httpResponse = httpclient2.execute(postRequest); 
			httpEntity = httpResponse.getEntity();         
			
          try {
  			if (httpEntity != null) {
  				response = EntityUtils.toString(httpEntity);
  			}
          	  		} catch (Exception e) {
  			//Log.e("Buffer Error", "Error converting result " + e.toString());
  		}

		}catch (Exception e) {
			return "";
  		}
          return response;
	}
	
	
	
	public String getSingleUserMessageHistory(String group_url) {
		
		initializeHttpClient();
	
		Utils.printLog("check group_url==>" + group_url);
		String response = sendGetRequest2(group_url);
		

		return response;
	}

	
	public String post(String url,  List<NameValuePair> nameValuePairs) {
		
		//initializeHttpClient();
	
		String response = postRequest2(url,nameValuePairs);

		return response;
	}
	

	public String sendUploadfile_Request(String filepath) {
		String response = "";
		final List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		String url = Apiurls.URL_UPLOADFILE;

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);

		MultipartEntity reqEntity = new MultipartEntity();
		

		try {

			File f = new File(filepath);
			FileBody pbody = new FileBody(f);
			reqEntity.addPart("file[0]", pbody);

		} catch (Exception e2) {
			
			e2.printStackTrace();
		}

		request.setEntity(reqEntity);

		try {

			httpResponse = client.execute(request);
			httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				response = EntityUtils.toString(httpEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

}
/*
* 	public String getGroupListRequest() {
		// TODO Auto-generated method stub
		initializeHttpClient();
		String response = sendGetRequest(Apiurls.URL_GROUPLIST);
		return response;
	}


	public String getCountryList() {
		// TODO Auto-generated method stub
		initializeHttpClient();
		String response = sendGetRequest(Apiurls.KIT19_BASE_URL+"getCountryList");
		return response;
	}


	public String getAdminMessagesRequest(String usernumber) {
		// TODO Auto-generated method stub
		initializeHttpClient();
		nameValuePairs.add(new BasicNameValuePair("username", usernumber));
		String response = sendPostRequest(Apiurls.URL_GETADMINMESSAGE, false);
		return response;
	}



	public String postToAdminMessages(String username, String name,
			String textmsg, String msgtype, String imgmsg) {
		// TODO Auto-generated method stub

		String response = "";
		final List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		String url = Apiurls.URL_SENDTOADMINMESSAGE;

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);

		MultipartEntityBuilder reqEntity =  MultipartEntityBuilder.create();

		try {
			reqEntity.addPart("username", new StringBody(username));
			reqEntity.addPart("name", new StringBody(name));
			reqEntity.addPart("textmsg", new StringBody(textmsg));
			reqEntity.addPart("msgtype", new StringBody(msgtype));

			if (imgmsg != null && !imgmsg.isEmpty()) {
				File f = new File(imgmsg);
				FileBody pbody = new FileBody(f);
				reqEntity.addPart("imgmsg", pbody);
			}

		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		request.setEntity((HttpEntity) reqEntity);

		try {

			httpResponse = client.execute(request);
			httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				response = EntityUtils.toString(httpEntity);
				Utils.printLog("send message response " + response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public String sendGetCodeRequest(String countrycode, String number,
			String token) {
		// TODO Auto-generated method stub
		initializeHttpClient();
		nameValuePairs
				.add(new BasicNameValuePair("countrystdcode", countrycode));
		nameValuePairs.add(new BasicNameValuePair("phone_number", number));
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("device_type",
				GlobalData.Devicetype));
		String response = sendPostRequest(Apiurls.URL_GETCODE, false);

		return response;
	}

	public String sendVerifyCodeRequest(String username, String code) {
		// TODO Auto-generated method stub
		initializeHttpClient();
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("verifycode", code));

		String response = sendPostRequest(Apiurls.URL_VERIFYCODE, false);

		return response;
	}
public String sendNewCheckcontactRequest(String url, String c_code,String contactName,String contactMobile,String contactDOB,String contactAnniversary) {

		initializeHttpClient();

		Utils.printLog("check contact==>" + contactAnniversary);


		String response = sendPostRequestNew(url,nameValuePairs);


		return response;
	}


	public String insertBroadcastGroup(String group_url) {

		initializeHttpClient();

		Utils.printLog("check group_url==>" + group_url);


		String response = sendGetRequest2(group_url);


		return response;
	}

	public  String doPost(JSONObject schObject, String s) {
	      //  HttpClient client = new DefaultHttpClient();

	        HttpPost post = new HttpPost(s);
	        StringEntity se = null;
	        HttpResponse response = null;
	        String jsonString="";

	        try {

	            if(schObject!=null){

	                se = new StringEntity(schObject.toString());
//	                System.out.println("sending" + schObject.toString());
	            }
	            else{
	                se = new StringEntity("");
	            }
	            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
	            post.setEntity(se);
	            response = httpclient.execute(post);
	            if (response != null) {


	                httpEntity = response.getEntity();
	    			if (httpEntity != null) {
	    				jsonString = EntityUtils.toString(httpEntity);

	    			}

	            }


	            try {
	                if (jsonString.startsWith("<pre")) {
	                    jsonString = jsonString.substring(jsonString.indexOf("{\"data\""));
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }



	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return jsonString;
	    }

	      public String PostParams(String url, final List<NameValuePair> params, final Class<String> objectClass) {
	        String paramString = URLEncodedUtils.format(params, "utf-8");
	        url += "?" + paramString;
	        return PostObject(url, null, objectClass);
	    }

	    public String PostObject(final String url, final String object, final Class<String> objectClass) {
	        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
	        HttpPost httpPost = new HttpPost(url);
	        try {

	            StringEntity stringEntity = new StringEntity(new GsonBuilder().create().toJson(object));
	            httpPost.setEntity(stringEntity);
	            httpPost.setHeader("Accept", "application/json");
	            httpPost.setHeader("Content-type", "application/json");
	            httpPost.setHeader("Accept-Encoding", "gzip");

	            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            if (httpEntity != null) {
	                InputStream inputStream = httpEntity.getContent();
	                Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
	                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
	                    inputStream = new GZIPInputStream(inputStream);
	                }

	                String resultString = convertStreamToString(inputStream);
	                inputStream.close();

	                String string =new GsonBuilder().create().fromJson(resultString, objectClass);

	                return new GsonBuilder().create().fromJson(resultString, objectClass)string;

		*//*}

		} catch (UnsupportedEncodingException e) {
		e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (ClientProtocolException e) {
		e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (IOException e) {
		e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return null;
		}

 public static String convertStreamToString(InputStream is) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append((line + ""));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();

	    }



	private String sendPostRequestNew(String url, List<NameValuePair> setheader) {
		String response = "";
		try {
			postRequest = new HttpPost(url);

			postRequest.setEntity(new UrlEncodedFormEntity(setheader,
					HTTP.UTF_8));

			httpResponse = httpclient.execute(postRequest);
			httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				response = EntityUtils.toString(httpEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}






	private String sendGetRequest(String url) {
		String response = "";
		try {

			getRequest = new HttpGet(url);

			Utils.printLog("Request:  " + url);

			httpResponse = httpclient.execute(getRequest);
			httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				response = EntityUtils.toString(httpEntity,HTTP.UTF_8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	public String deleteBroadCast(String url) {
		String response = "";
		try {
			DefaultHttpClient	httpclientNew = new DefaultHttpClient();
			getRequest = new HttpGet(url);

			Utils.printLog("Request:  " + url);

			httpResponse = httpclientNew.execute(getRequest);
			httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				response = EntityUtils.toString(httpEntity,HTTP.UTF_8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	public String sendCheckcontactRequest2(String url, String Userid,Object[] object) throws JSONException, UnsupportedEncodingException {

		initializeHttpClient();
		String nameJsonArray=(String)object[0];
		String numberJsonArray=(String)object[1];
		String postCodeArray=(String)object[2];
		String dateOfBirtharray=(String)object[3];
		String anniversaryArray=(String)object[4];
		String imageArray=(String)object[5];

		nameValuePairs.add(new BasicNameValuePair("contactName",nameJsonArray.toString() ));
		nameValuePairs.add(new BasicNameValuePair("contactMobile", numberJsonArray.toString()));
		//Utils.mCreateAndSaveFilePost(nameValuePairs.toString());
		nameValuePairs.add(new BasicNameValuePair("countryCode", postCodeArray.toString()));
		nameValuePairs.add(new BasicNameValuePair("contactDOB",dateOfBirtharray.toString() ));
		nameValuePairs.add(new BasicNameValuePair("contactAnniversary", anniversaryArray.toString()));
		nameValuePairs.add(new BasicNameValuePair("Page","InsertContacts"));
		nameValuePairs.add(new BasicNameValuePair("Userid",Userid));
		nameValuePairs.add(new BasicNameValuePair("imageUrl",imageArray.toString()));

		//Utils.mCreateAndSaveFile(nameValuePairs.toString(),"AllData");


		String response = postRequest2(url,nameValuePairs);


		return response;
	}

	public String sendStatusRequest(String jid, String status) {

		initializeHttpClient();
		nameValuePairs.add(new BasicNameValuePair("jid", jid));
		nameValuePairs.add(new BasicNameValuePair("status", status));

		String response = sendPostRequest(Apiurls.URL_SETSTATUS, false);

		return response;
	}

	private String sendPostRequest(String url, boolean setheader) {
		String response = "";
		try {
			postRequest = new HttpPost(url);

			if (setheader == true) {
				postRequest.setHeader(
						"Authorization",
						getB64Auth(Apiurls.ADMIN_USERNAME,
								Apiurls.ADMIN_PASSWORD));
				postRequest.setHeader("Content-type", "application/json");
				postRequest.setHeader("Accept", "application/json");
			}
			// /////////////////
			postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));

			httpResponse = httpclient.execute(postRequest);
			httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				response = EntityUtils.toString(httpEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private String getB64Auth(String login, String pass) {
		String source = login + ":" + pass;
		String ret = "Basic "
				+ Base64.encodeToString(source.getBytes(), Base64.URL_SAFE| Base64.NO_WRAP);
		return ret;
	}
		* */