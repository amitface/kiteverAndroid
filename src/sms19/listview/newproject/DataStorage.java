package sms19.listview.newproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sms19.inapp.msg.CircularImageView;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.rest.Rest;
import sms19.listview.adapter.FtpStorageAdapter;
import sms19.listview.adapter.StorageAdapter;
import sms19.listview.newproject.model.FtpDataListModel;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class DataStorage extends AppCompatActivity {

    ListView storage_list;
    ProgressBar progressBar;
    StorageAdapter storageAdapter;
    RelativeLayout emptyElement;
    TextView no_record,storage_title;
    MoonIcon micon;
    String  total_size="0";
    String allow_size="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_storage);

        actionBarSettingWithBack(this,getSupportActionBar(),"Storage");
        storage_list=(ListView)findViewById(R.id.storage_list);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        emptyElement=(RelativeLayout)findViewById(R.id.tax_emptyElement);
        no_record=(TextView) findViewById(R.id.no_record);
        storage_title=(TextView) findViewById(R.id.storage_title);
        storage_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        setRobotoThinFont(storage_title,this);
        micon=new MoonIcon(this);
        micon.setTextfont(no_record);
        new FetchFtpDataList(Utils.getUserId(this)).execute();
    }



    private class FetchFtpDataList extends AsyncTask<String, String, String> {

        String userId;

        FetchFtpDataList(String userId) {
            this.userId = userId;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Rest rest = Rest.getInstance();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("Page",
                    "FetchFTPDataList"));
            nameValuePairs.add(new BasicNameValuePair("userid", userId));
            String stringUrl = Apiurls.KIT19_BASE_URL.replace("?Page=", "");
            stringUrl = stringUrl.replace(" ", "");
            String response = rest.post(stringUrl, nameValuePairs);
            return response;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            ArrayList<FtpDataListModel> list = new ArrayList<FtpDataListModel>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject != null) {

                    if (jsonObject.has("FileShared")) {

                        JSONArray array = jsonObject.getJSONArray("FileShared");
                        if (array != null && array.length() > 0) {

                            for (int k = 0; k < array.length(); k++) {
                                JSONObject object = array.getJSONObject(k);
                                String name = "";
                                String type = "";
                                String messageBody = "",size="";
                                if (object.has("name")) {
                                    name = object.getString("name");
                                }
                                if (object.has("FileType")) {
                                    type = object.getString("FileType");
                                }
                                if (object.has("MessageBody")) {
                                    messageBody = object
                                            .getString("MessageBody");
                                }
                                if (object.has("AllowedSize")) {
                                    allow_size = object
                                            .getString("AllowedSize");
                                }
                                if (object.has("TotalSize")) {
                                    total_size = object
                                            .getString("TotalSize");
                                }
                                if (object.has("size")) {
                                    size = object.getString("size");

                                }
                                if(Double.parseDouble(total_size)>0)
                                list.add(new FtpDataListModel(name, type, messageBody,size));
                            }
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            storageAdapter = new StorageAdapter(list,DataStorage.this);
            storage_list.setAdapter(storageAdapter);
            storage_list.setEmptyView(emptyElement);
            /*if(list.size()>0)
            {
                emptyElement.setVisibility(View.GONE);
                storage_list.setVisibility(View.VISIBLE);
            }
            else
            {
                emptyElement.setVisibility(View.VISIBLE);
                storage_list.setVisibility(View.GONE);
            }*/
            //storage_list.setEmptyView(emptyElement);
            progressBar.setVisibility(View.GONE);
            storage_title.setText("You have used "+total_size+" KB from "+allow_size+" KB");

        }
    }



}
