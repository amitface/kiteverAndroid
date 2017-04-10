package sms19.inapp.msg.asynctask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sms19.inapp.msg.ContactFragment;
import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.ContactUtil;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.InsertContactModel;
import sms19.inapp.msg.rest.Rest;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;

public class GetContactListAsyncTask extends AsyncTask<Void, Void, Void> {
    private String response = "";
    private JSONObject resObj;
    private String userId = "";

    private ProgressBar progress_on_actionbar = null;

    private Context mContext;


    public GetContactListAsyncTask(Context context, SharedPreferences chatPrefs, String userId, ProgressBar progress_actionbar) {

        this.mContext = context;
        this.userId = userId;
        this.progress_on_actionbar = progress_actionbar;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        ContactUtil.isInsertContactRunning = true;
        if (progress_on_actionbar != null) {
            progress_on_actionbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        String postContactNo = "";
        String postContactName = "";
        String postCountryCode = "";
        String dob = "";
        String anniversy = "";
        try {

            GlobalData.getnewContact = true;
            JSONArray array = new JSONArray(GlobalData.ContactStringToSend);

            JSONObject finalOb = new JSONObject();

            if (array != null ) {
                if (array.length() > 0) {

                    JSONArray array2 = new JSONArray();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        JSONObject jsonObjectValue = new JSONObject();

                        postContactNo = Utils.removeCountryCode(jsonObject.getString("phonenumber"), mContext);

                        String namecc = jsonObject.getString("name");
                        postContactName = namecc;
                        postCountryCode = GlobalData.COUNTRY_CODE;

                        sms19.inapp.msg.model.PhoneValidModel model = ContactUtil.validNumberForGetContactApi(postContactNo);

                        if (model != null && model.getPhone_number() != null && model.getPhone_number() != null) {
                            String code = model.getCountry_code();
                            if (model.getCountry_code() != null && model.getCountry_code().length() <= 0) {
                                code = Utils.getCountryCode(mContext);
                            }
                            jsonObjectValue.put("contactName", jsonObject.getString("name"));
                            jsonObjectValue.put("contactMobile", model.getPhone_number());
                            jsonObjectValue.put("contactMobile", postContactNo);
                            jsonObjectValue.put("countryCode", code);
//							jsonObjectValue.put("countryCode", "+91");
                            jsonObjectValue.put("contactDOB", "");
                            jsonObjectValue.put("contactAnniversary", "");
                            jsonObjectValue.put("imageUrl", "");
                        }
                        array2.put(jsonObjectValue);
                    }

                    finalOb.put("contacts", array2);
                    Rest rest = Rest.getInstance();
                    final String stringUrl2 = Apiurls.getBasePostURL();
                    response = rest.sendCheckcontactJsonFormat(stringUrl2, userId, finalOb);

                    GlobalData.registerContactList = new ArrayList<Contactmodel>();
                    if (response != null && response.trim().length() != 0) {
                        try {
                            Gson gson = new Gson();
                            InsertContactModel insertContactModel = gson.fromJson(response, InsertContactModel.class);
                            if (insertContactModel.getInsertContacts() != null && insertContactModel.getInsertContacts().size() > 0) {
                                GlobalData.dbHelper.updateAfterGetContactfromServer(insertContactModel);

                                Utils.addRoster();

                                ContactFragment contactFragment = ContactFragment.newInstance("");

                                if (contactFragment != null) {
                                    if (InAppMessageActivity.chatPrefs != null) {
                                        GlobalData.ContactStringToSend = "";// 3 may add

                                    }
                                    if (ConstantFields.HIDE_MENU == 4) {
                                        contactFragment.refreshContactAdapter();
                                    }
                                }
                            }


                            /*resObj = new JSONObject(response);
                            if (resObj.has("InsertContacts")) {
                                Utils.mCreateAndSaveFile(response.toString(), "server");

                                JSONArray dataArray = resObj.getJSONArray("InsertContacts");
                                if (dataArray != null && dataArray.length() > 0) {
                                    GlobalData.dbHelper.updateAfterGetContactfromServer(dataArray);

                                    Utils.addRoster();

                                    ContactFragment contactFragment = ContactFragment.newInstance("");

                                    if (contactFragment != null) {
                                        if (InAppMessageActivity.chatPrefs != null) {
                                            GlobalData.ContactStringToSend = "";// 3 may add

                                        }
                                        if (ConstantFields.HIDE_MENU == 4) {
                                            contactFragment.refreshContactAdapter();
                                        }
                                    }
                                }

                            }*/
                            else {
                                ConstantFields.Contactinsertsuccess = true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        ConstantFields.Contactinsertsuccess = true;
                    }
                }
            }
            //Utils.printLog("Check Contact Response1112:  " + response);

        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {

            e1.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        ContactUtil.isInsertContactRunning = false;
        if (progress_on_actionbar != null) {
            progress_on_actionbar.setVisibility(View.GONE);
        }

        Intent intent = new Intent(com.kitever.utils.Utils.CONTACTS_UPDATE_ACTION);
        // You can also include some extra data.
        intent.putExtra(com.kitever.utils.Utils.CONTACTS_MESSAGE_TYPE, com.kitever.utils.Utils.CONTACTS_SYNC_UPDATE);
        intent.putExtra(com.kitever.utils.Utils.CONTACTS_MESSAGE, "Contacts updated successfully");

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {// http://nowconnect.in/10/2085fb5e8801fcac23e4967062ab6cd1.jpg

                URL url = new URL("http://" + f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                ContextWrapper cw = new ContextWrapper(mContext);
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("KiteverImageDir",
                        Context.MODE_PRIVATE);
                // Create imageDir
                File mypath = new File(directory, "profile.jpg");

                // FileOutputStream fos = null;
                // try {
                // fos = new FileOutputStream(mypath);
                // // Use the compress method on the BitMap object to write
                // image to the OutputStream
                // bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                // } catch (Exception e) {
                // e.printStackTrace();
                // } finally {
                // try {
                // fos.close();
                // } catch (IOException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // }

                OutputStream output = new FileOutputStream(mypath); // changed
                // from
                // "/sdcard/SMS19/...

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) // CAN WE DO THIS IN
                // JAVA ? -khogen
                // (o.O)
                {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
//				Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            ContextWrapper cw = new ContextWrapper(mContext);
            File directory = cw.getDir("KiteverImageDir", Context.MODE_PRIVATE);
            File f = new File(directory.getPath(), "profile.jpg");

            // if (f.exists()) {
            // Bitmap bp = BitmapFactory.decodeStream(new FileInputStream(f));
            // bp = getRoundedCornerBitmap(bp, 90);
            // if(bp==null){
            // bp = BitmapFactory.decodeResource(getResources(),
            // R.drawable.profile_propic);
            // }
            // images.setImageBitmap(bp);
            // // images.setImageDrawable(Drawable.createFromPath(imagePath));
            // }

            String imagePath = f.getPath();
            // setting downloaded into image view
            Bitmap bp = BitmapFactory.decodeFile(imagePath);
//			if (bp != null) {
////				bp = getRoundedCornerBitmap(bp, 90);
//				if (bp == null) {
//					bp = BitmapFactory.decodeResource(getResources(),
//							R.drawable.profile_propic);
//				}
//				images.setImageBitmap(bp);
//			}
            // images.setImageDrawable(Drawable.createFromPath(imagePath));

        }
    }
}






