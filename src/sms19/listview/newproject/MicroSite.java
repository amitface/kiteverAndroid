package sms19.listview.newproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kitever.android.R;

import sms19.inapp.msg.CircularImageView;
import sms19.listview.newproject.fragment.MicroSiteCreate;
import sms19.listview.newproject.fragment.MicroSiteLeads;
import sms19.listview.newproject.fragment.MicroSiteView;

import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class MicroSite extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_site);
        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra("fragment");
        setUi(fragmentName);
    }

    private void setUi(String name) {
        switch (name)
        {
            case "create":
                actionBarSettingWithBack(this,getSupportActionBar(),"MicroSite");
                getSupportFragmentManager().beginTransaction().add(R.id.activity_micro_site, MicroSiteCreate.newInstance("","")).commit();
                break;
            case "view":
                actionBarSettingWithBack(this,getSupportActionBar(),"MicroSite");
                getSupportFragmentManager().beginTransaction().add(R.id.activity_micro_site, MicroSiteView.newInstance("","")).commit();
                break;
            case "leads":
                actionBarSettingWithBack(this,getSupportActionBar(),"MicroSite Leads");
                getSupportFragmentManager().beginTransaction().add(R.id.activity_micro_site, MicroSiteLeads.newInstance("","")).commit();
                break;
        }
    }


}
