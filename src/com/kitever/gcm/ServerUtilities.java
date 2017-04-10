/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kitever.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gcm.GCMRegistrar;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.rest.Rest;

/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtilities {

    private static final String LOG_TAG = "ServerUtilities";
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();

    /**
     * Register this account/device pair within the server.
     *
     * @return whether the registration succeeded or not.
     */
    static boolean register(final Context context, final String regId) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String msisdn = sp.getString("msisdn", CommonUtilities.MSISDN_NUMBER);
        String serverUrl = "";// SERVER_URL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        params.put("msisdn", msisdn); // TODO: get this
        params.put("deviceClientId", "sdfsftrh12glkj389u423n5n445yhhhskhkjjyh"); // TODO:
        // get
        // this
        params.put("senderId", CommonUtilities.SENDER_ID); // TODO: set this
        // from config
        params.put("appId", "com.example.pushnotifydemo"); // TODO: get the name
        // of the
        // application

        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register it in the
        // demo server. As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            // Logger.debug(TAG, "Attempt #" + i + " to register");
            try {
                CommonUtilities.displayMessage(context, "regiter");
                // postToServer(serverUrl, params, context);
                // pushToKitever(serverUrl, params, context);
                // postToServerNCell(serverUrl, params, context);
                GCMRegistrar.setRegisteredOnServer(context, true);
                String message = "register 2";
                CommonUtilities.displayMessage(context, message);
                return true;
            } catch (Exception e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                // Log.e(TAG, "Failed to register on attempt " + i, e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    // Logger.debug(TAG, "Sleeping for " + backoff
                    // + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    // Logger.debug(TAG,
                    // "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return false;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = "server_register_error";
        CommonUtilities.displayMessage(context, message);
        return false;
    }

    public static void registerToServer(final Context context, final String regId) {
        boolean isRegistered = false;
        SharedPreferences prfs = context.getSharedPreferences("profileData",
                Context.MODE_PRIVATE);
        String userLogin = prfs.getString("user_login", "");
        Rest rest = Rest.getInstance();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("userName", userLogin));
        nameValuePairs.add(new BasicNameValuePair("regId", regId));
        nameValuePairs.add(new BasicNameValuePair("Page",
                "UpdateGCMRegIdToUser"));

        String stringUrl = Apiurls.KIT19_BASE_URL.replace("?Page=", "");
        stringUrl = stringUrl.replace(" ", "");
        String response = rest.post(stringUrl, nameValuePairs);
        JSONObject jsonObj;
        if (response != null && !response.equalsIgnoreCase("")) {
            String status = "";
            try {
                jsonObj = new JSONObject(response);
                JSONArray jsonArray = jsonObj.optJSONArray("UpdateGCMRegIdToUser");
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject obj = jsonArray.getJSONObject(0);
                    if (obj != null && obj.has("Status")) {
                        status = obj.getString("Status");
                    }
                }
                if (status.equalsIgnoreCase("Success")) {
                    isRegistered = true;
                }
                GcmRegistrationHandler.setRegisteredToCD(context, isRegistered);
            } catch (Exception e) {

            }
        }
    }

    /**
     * Unregister this account/device pair within the server.
     */
    static void unregister(final Context context, final String regId) {
        // Logger.debug(TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = "";// SERVER_URL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = "server_unregistered";
            CommonUtilities.displayMessage(context, message);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = "server_unregister_error";
            CommonUtilities.displayMessage(context, message);
        }
    }

    static void unregisterOnServer(final Context context, final String regId) {
        // Logger.debug(TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = "";// SERVER_URL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            post(serverUrl, params);
            String message = "server_unregistered";
            CommonUtilities.displayMessage(context, message);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = "unregister_error";
            CommonUtilities.displayMessage(context, message);
        }
    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params   request parameters.
     * @throws IOException propagated from POST.
     */
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {
        URL url;
        // Logger.debug("endpoint", endpoint);
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }

        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        // Log.v(TAG, "Posting '" + body + "' to " + url);
        StringEntity ent = null;
        try {
            ent = new StringEntity(body);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        HttpPost post = new HttpPost(endpoint);
        post.addHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
        post.setEntity(ent);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
        } catch (Exception e) {
            // Log.e(TAG, "###### ERROR=" + e.getMessage(), e);

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Issue a POST request to the Call Deflection server.
     *
     * @param endpoint POST address.
     * @param params   request parameters.
     * @throws IOException propagated from POST.
     */
    private static void postToServer(String endpoint,
                                     Map<String, String> params, Context context) throws IOException {
        URL url;
        // Logger.debug("endpoint", endpoint);
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }

        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        // Log.v(TAG, "Posting '" + body + "' to " + url);
        StringEntity ent = null;
        try {
            ent = new StringEntity(body);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(endpoint);
        post.addHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
        post.setEntity(ent);
        HttpResponse result = client.execute(post);
        if (result.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new IOException("Post failed with error code "
                    + result.getStatusLine().getStatusCode());
        } else {
            GcmRegistrationHandler.setRegisteredToCD(context, true);
        }

    }

    // private static void pushToKitever(String endpoint,
    // Map<String, String> params, Context context) throws IOException {
    // URL url;
    // // Logger.debug("endpoint", endpoint);
    // try {
    // url = new URL(endpoint);
    // } catch (MalformedURLException e) {
    // throw new IllegalArgumentException("invalid url: " + endpoint);
    // }
    //
    // DefaultHttpClient client = new DefaultHttpClient();
    // // HttpGet get = new HttpGet(endpoint);
    // HttpPost get = new HttpPost(endpoint);
    // String version = "";
    // if (version == null)
    // version = "2.0.0.1";
    // if (version.length() == 0)
    // version = "2.0.0.1";
    // // version="1.0";
    // String AppPlatformVersion = "2.0";
    // // String customerSegment="OTHER";
    //
    // String clientID = BaseActivity.getDEVICEIDFromCache();
    // get.setHeader("X-DeviceClientID", clientID);
    // get.setHeader("X-AppPlatformName", "Android");
    // get.setHeader("X-AppClientVersion", version);
    // get.setHeader("X-AppPlatformVersion", "" + AppPlatformVersion);//
    // Global.BUILD_PLATFORM
    // // if (Global.input_number == null) {
    // // Global.input_number = BaseActivity.getMSISDNFromCache();
    // // }
    // get.setHeader("X-MSISDN", Global.primaryServiceIntance);
    // get.setHeader("X-CustomerSegment", Global.customerSegment);
    // get.setHeader("X-CircleId", Global.circleId);// Global.circleId
    //
    // String regId = params.get("regId");
    // // String appId=params.get("appId");
    // String appId = "com.myairtelapp";
    // get.setHeader("X-AppRegId", regId);
    // get.setHeader("X-AppId", regId);
    // // get.addHeader("Content-Type",
    // // "application/x-www-form-urlencoded;charset=UTF-8");
    // // get.setEntity(ent);
    // // body
    // StringEntity encodedFormEntity = null;
    // String xml = "<?xml version='1.0' encoding='UTF-8'?>"
    // + "<mAppData><userOperationData>" + "</userOperationData>"
    // + "</mAppData>";
    // encodedFormEntity = new StringEntity(xml);
    // encodedFormEntity.setContentType("application/xml");
    // get.setEntity(encodedFormEntity);
    // Logger.debug("ServerUtilities", "xml in push============" + xml);
    // Logger.debug("ServerUtilities", "push notification url:======="
    // + endpoint);
    // Logger.debug("ServerUtilities",
    // "header of push notification:::::X-DeviceClientID=" + clientID);
    // Logger.debug("ServerUtilities",
    // "header of push notification:::::X-AppRegId:=" + regId
    // + "...X-AppId:=" + regId);
    // Logger.debug("ServerUtilities",
    // "header of push notification:::::X-DeviceClientID:=" + clientID);
    // Logger.debug("ServerUtilities",
    // "header of push notification:::::X-AppClientVersion:="
    // + version);
    // Logger.debug("ServerUtilities",
    // "header of push notification:::::X-AppPlatformName:=Android");
    // Logger.debug("ServerUtilities",
    // "header of push notification:::::X-AppPlatformVersion:="
    // + Global.BUILD_PLATFORM);
    // Logger.debug("ServerUtilities",
    // "header of push notification:::::X-MSISDN:="
    // + Global.primaryServiceIntance);
    // Logger.debug("ServerUtilities",
    // "header of push notification:::::X-CustomerSegment:="
    // + Global.customerSegment);
    // Logger.debug("ServerUtilities",
    // "header of push notification:::::X-CircleId:="
    // + Global.circleId);
    // // Logger.debug("ServerUtilities","header of push notification:::::";
    // // Logger.debug("ServerUtilities","header of push notification:::::";
    // // Logger.debug("ServerUtilities","header of push notification:::::";
    //
    // HttpResponse result = client.execute(get);
    // Logger.debug("ServerUtilities",
    // "push notification params=====" + result.getParams()
    // + "...status line==========" + result.getStatusLine());
    // Logger.debug("Http push Response:=========", result.toString());
    // if (result.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
    // throw new IOException("Get failed with error code "
    // + result.getStatusLine().getStatusCode());
    // } else {
    // StringBuilder response = new StringBuilder();
    // InputStream inputStream = result.getEntity().getContent();
    // BufferedReader br = new BufferedReader(new InputStreamReader(
    // inputStream));
    // String line;
    // while ((line = br.readLine()) != null) {
    // response.append(line);
    // }
    // try {
    // Logger.debug("ServerUtilities",
    // "push notification response ===" + response.toString());
    // JSONObject root = XML.toJSONObject(response.toString());
    // JSONObject mAppData = root.getJSONObject("mAppData");
    // JSONObject jsonObj = (JSONObject) mAppData
    // .get("businessOutput");
    // String opStatus = jsonObj.getString("opStatus");
    // if (opStatus != null && opStatus.equalsIgnoreCase("-7")) {
    // clearDataNExit(context);
    // }
    //
    // Logger.debug("ServerUtilities", "statusCode of push=========="
    // + opStatus);
    // } catch (Exception e) {
    // Logger.debug("ServerUtilities", "response exception==" + e);
    // }
    // // String str = Utility.convertStreamToString(inputStream);
    // //
    // Logger.debug("ServerUtilities","str==================================================="+str);
    // // CallDeflection.setRegisteredToCD(context, true);
    //
    // }
    //
    // }

    // private static void postToServerNCell(String endpoint,
    // Map<String, String> params, Context context) throws IOException {
    // URL url;
    // Logger.debug("endpoint", endpoint);
    // try {
    // url = new URL(endpoint);
    // } catch (MalformedURLException e) {
    // throw new IllegalArgumentException("invalid url: " + endpoint);
    // }
    //
    // StringBuilder bodyBuilder = new StringBuilder();
    // Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
    // // constructs the POST body using the parameters
    // while (iterator.hasNext()) {
    // Entry<String, String> param = iterator.next();
    // bodyBuilder.append(param.getKey()).append('=')
    // .append(param.getValue());
    // if (iterator.hasNext()) {
    // bodyBuilder.append('&');
    // }
    // }
    // String body = bodyBuilder.toString();
    // Log.v(TAG, "Posting '" + body + "' to " + url);
    // StringEntity ent = null;
    // try {
    // ent = new StringEntity(body);
    // } catch (UnsupportedEncodingException e1) {
    // e1.printStackTrace();
    // }
    // DefaultHttpClient client = new DefaultHttpClient();
    // HttpPost post = new HttpPost(endpoint);
    // post.addHeader("Content-Type",
    // "application/x-www-form-urlencoded;charset=UTF-8");
    // post.setEntity(ent);
    // HttpResponse result = client.execute(post);
    // if (result.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
    // throw new IOException("Post failed with error code "
    // + result.getStatusLine().getStatusCode());
    // } else {
    // CallDeflection.setRegisteredToCD(context, true);
    // }
    //
    // }

    // public static void clearDataNExit(Context context) {
    // try {
    // DataCacheManager.getInstance().clearCache();
    // SharedPreferences preferences = PreferenceManager
    // .getDefaultSharedPreferences(context);
    // SharedPreferences.Editor editor = preferences.edit();
    // editor.putString("dumpString", "");
    // editor.putBoolean("partialy", false);
    // editor.commit();
    //
    // Intent intent = new Intent(context, SplashActivity.class);
    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    // context.startActivity(intent);
    // Global.input_number = null;
    // Global.deviceClientId = null;
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}
