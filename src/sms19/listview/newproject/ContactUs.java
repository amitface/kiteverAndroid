package sms19.listview.newproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.app.context.AlertDialogManager;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.CircularImageView;
import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class ContactUs extends AppCompatActivity implements NetworkManager {

    private EditText contact_subject,contact_msg;
    private Button submit;
    private String subject="",msg="";
    AlertDialogManager alert;
    Context context;

    private final int KEY_SEND_EMAIL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        actionBarSettingWithBack(this,getSupportActionBar(),"Support");
        alert=new AlertDialogManager();
        context=this;

        contact_subject = (EditText) findViewById(R.id.contact_subject);
        contact_msg = (EditText) findViewById(R.id.contact_msg);
        submit = (Button) findViewById(R.id.submit);


        setRobotoThinFont(contact_subject,this);
        setRobotoThinFont(contact_msg,this);

        contact_subject.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        contact_msg.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        submit.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        submit.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        setRobotoThinFontButton(submit,this);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject=contact_subject.getText().toString().trim();
                msg=contact_msg.getText().toString().trim();
                if(subject.length()< 1)
                {
                    alert.showAlertDialog(context,"Error !","Enter the Subject",false);
                }
                else if(msg.length()< 1)
                {
                    alert.showAlertDialog(context,"Error !","Enter the Message",false);
                }
                else
                {
                    sendmail(subject,msg);
                }
            }
        });
    }

    public void sendmail(String subject,String description)
    {
        if (Utils.isDeviceOnline(this)) {

            SharedPreferences prfs = getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);
            String userLogin = prfs.getString("user_login", "");
            String passWord = prfs.getString("Pwd", "");
            //showLoading();
            Map map = new HashMap<>();
            map.put("Page", "SupportRequest");
            map.put("userID", Utils.getUserId(context));
            map.put("UserLogin",userLogin);
            map.put("Password", passWord);
            map.put("SupportTitle",subject);
            map.put("SupportDescription",description);

            try {
                new RequestManager().sendPostRequest(this, KEY_SEND_EMAIL, map);
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(this, getResources().getString(R.string.volleyError),Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No internet connection",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onReceiveResponse(int requestId, String response) {

        if (response != null && response.length() > 0) {
            if (requestId == KEY_SEND_EMAIL) {
                Toast.makeText(this, "Support Request Sent",Toast.LENGTH_SHORT).show();
                contact_msg.setText("");
                contact_subject.setText("");
            }
        }else
            Toast.makeText(this, getResources().getString(R.string.volleyError),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(context,getResources().getString(R.string.volleyError),Toast.LENGTH_LONG).show();
    }
}
