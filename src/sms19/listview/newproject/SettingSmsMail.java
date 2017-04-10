package sms19.listview.newproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.model.SMSMailSetting;
import sms19.listview.newproject.model.SMSMailSettingModel;

import static com.kitever.app.context.CustomStyle.MySpinnerAdapter;
import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class SettingSmsMail extends AppCompatActivity implements NetworkManager, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Switch switchHeaderSetting, switchFooterSetting;
    private Spinner spinnerSenderId;
    private TextView spinnerSenderId_title,tvReplyToMail_title,tvHeaderSetting, tvFooterSetting;
    private EditText tvReplyToMail;
    private final int KEY_GET_SMS_MAIL_SETTING = 1;
    private final int KEY_SET_SMS_MAIL_SETTING = 2;
    private int header=0, footer=0;
    private ArrayList<String> typeList ;
    private ArrayList<SMSMailSetting> smsMailSettingArrayList;
    private Button btnSaveSMSMailSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_sms_mail);
        actionBarSettingWithBack(this,getSupportActionBar(),"Setting SMS/Mail");
        setUI();
        setListener();
        getSMSMailSetting();
    }

    private void setUI() {
        switchFooterSetting = (Switch) findViewById(R.id.switchFooterSetting);
        switchHeaderSetting = (Switch) findViewById(R.id.switchHeaderSetting);
        spinnerSenderId = (Spinner) findViewById(R.id.spinnerSenderId);

        spinnerSenderId_title= (TextView) findViewById(R.id.spinnerSenderId_title);
        tvReplyToMail_title= (TextView) findViewById(R.id.tvReplyToMail_title);
        tvHeaderSetting = (TextView) findViewById(R.id.tvHeaderSetting);
        tvFooterSetting = (TextView) findViewById(R.id.tvFooterSetting);
        tvReplyToMail = (EditText) findViewById(R.id.tvReplyToMail);
        btnSaveSMSMailSetting = (Button) findViewById(R.id.btnSaveSMSMailSetting);

        setRobotoThinFont(spinnerSenderId_title,this);
        setRobotoThinFont(tvReplyToMail_title,this);
        setRobotoThinFont(tvHeaderSetting,this);
        setRobotoThinFont(tvFooterSetting,this);
        setRobotoThinFont(tvReplyToMail,this);
        setRobotoThinFontButton(btnSaveSMSMailSetting,this);

        spinnerSenderId_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvReplyToMail_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvHeaderSetting.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvFooterSetting.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvReplyToMail.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        btnSaveSMSMailSetting.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        btnSaveSMSMailSetting.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

    }

    private void setListener() {
        switchFooterSetting.setOnCheckedChangeListener(this);
        switchHeaderSetting.setOnCheckedChangeListener(this);
        tvHeaderSetting.setOnClickListener(this);
        tvFooterSetting.setOnClickListener(this);
        btnSaveSMSMailSetting.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchFooterSetting:
                if (isChecked) {
                    switchFooterSetting.setChecked(true);
                    footer = 1;
                }
                else {
                    switchFooterSetting.setChecked(false);
                    footer = 0;
                }
                break;
            case R.id.switchHeaderSetting:
                if (isChecked) {
                    switchHeaderSetting.setChecked(true);
                    header = 1;
                }
                else {
                    switchHeaderSetting.setChecked(false);
                    header = 0;
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvHeaderSetting:
                Intent intent = new Intent(this, SettingHeaderActivity.class);
                intent.putExtra("Link", Utils.getBaseUrlValue(this).replace("/NewService.aspx?Page=","")+"/settings/HeaderSMSMailSettingAPP.aspx");
                startActivity(intent);
                break;
            case R.id.tvFooterSetting:
                Intent intent1 = new Intent(this, SettingHeaderActivity.class);
                intent1.putExtra("Link", Utils.getBaseUrlValue(this).replace("/NewService.aspx?Page=","")+"/settings/FooterSmsMailSettingAPP.aspx");
                startActivity(intent1);
                break;
            case R.id.btnSaveSMSMailSetting:
                setSMSMailSetting();
                break;
        }
    }

    private void getSMSMailSetting() {
        if (Utils.isDeviceOnline(this)) {
            try {
                Map map = new HashMap<>();
                map.put("Page", "GetSMSMailSetting");
                map.put("UserID", Utils.getUserId(this));

                new RequestManager().sendPostRequest(this, KEY_GET_SMS_MAIL_SETTING, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setSMSMailSetting(){
        if(Utils.isDeviceOnline(this))
        {
            try{

                Map map = new HashMap<>();
                map.put("Page", "SetSMSMailSetting");
                map.put("UserID", Utils.getUserId(this));
                map.put("SenderID",  ""+smsMailSettingArrayList.get(0).getSenderId());
                map.put("SenderName", spinnerSenderId.getSelectedItem().toString());
                map.put("ReplyToMailID", tvReplyToMail.getText().toString());
                map.put("FooterEnabled", ""+footer);
                map.put("HeaderEnabled", ""+header);

                new RequestManager().sendPostRequest(this, KEY_SET_SMS_MAIL_SETTING, map);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (response != null && response.length() > 0) {
            if (requestId == KEY_GET_SMS_MAIL_SETTING) {
                Gson gson = new Gson();
                SMSMailSettingModel smsMailSettingModel = gson.fromJson(response, SMSMailSettingModel.class);
                if (smsMailSettingModel != null && smsMailSettingModel.getSMSMailSetting() != null && smsMailSettingModel.getSMSMailSetting().size()>0) {
                    smsMailSettingArrayList = (ArrayList<SMSMailSetting>) smsMailSettingModel.getSMSMailSetting();
                    tvReplyToMail.setText(smsMailSettingArrayList.get(0).getReplyToMailID());
                    if (smsMailSettingArrayList.get(0).getHeaderEnabled()!=null && smsMailSettingArrayList.get(0).getHeaderEnabled() == 1) {
                        switchHeaderSetting.setChecked(true);
                    } else
                        switchHeaderSetting.setChecked(false);

                    if (smsMailSettingArrayList.get(0).getFooterEnabled()!=null && smsMailSettingArrayList.get(0).getFooterEnabled() == 1) {
                        switchFooterSetting.setChecked(true);
                    } else
                        switchFooterSetting.setChecked(false);

                    if(smsMailSettingArrayList.get(0).getReplyToMailID()!=null)
                        tvReplyToMail.setText(smsMailSettingArrayList.get(0).getReplyToMailID());

                    typeList = new ArrayList<String>();
                    Iterator iterator = smsMailSettingArrayList.iterator();
                    while (iterator.hasNext())
                    {
                        typeList.add(((SMSMailSetting)iterator.next()).getSenderName());
                    }


                    MySpinnerAdapter typeAdapter = new MySpinnerAdapter(this,
                            android.R.layout.simple_spinner_item, typeList);
                    typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSenderId.setAdapter(typeAdapter);

                } else {
                    List<SMSMailSetting> smsMailSettings = new ArrayList<>();
                    smsMailSettings.add(new SMSMailSetting(1104L,"TXTSMS"));
                    smsMailSettingArrayList = new ArrayList<>(smsMailSettings);

                    typeList = new ArrayList<String>();
                    typeList.add("TXTSMS");

                    MySpinnerAdapter typeAdapter = new MySpinnerAdapter(this,
                            android.R.layout.simple_spinner_item, typeList);
                    typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSenderId.setAdapter(typeAdapter);

                }
            }else if(KEY_SET_SMS_MAIL_SETTING == requestId)
            {
                Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();
            }

        } else {

        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this,getResources().getString(R.string.volleyError),Toast.LENGTH_SHORT).show();
    }
}
