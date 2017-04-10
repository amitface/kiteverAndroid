package sms19.listview.newproject;


import sms19.listview.database.DataBaseDetails;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.sendsms.SendSmsScreen;

public class CustomTemplate extends ActionBarActivity {
    DataBaseDetails db = new DataBaseDetails(this);
    String messagecustom;
    String UserId, Mobile;
    DataBaseDetails dbObject = new DataBaseDetails(this);
    public static boolean customRemplate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditText messagetxt;
        Button sendbtn;
        final TextView toatalcountw;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customtemplate);
        messagetxt = (EditText) findViewById(R.id.mssagecustom);
        toatalcountw = (TextView) findViewById(R.id.toatalcountw);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#066966")));
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>Custom Template</font>"));
        bar.setHomeAsUpIndicator(R.drawable.arrow_new);

        sendbtn = (Button) findViewById(R.id.cutommsgsend);
        messagetxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int lengthmsg = messagetxt.length();
                toatalcountw.setText("" + lengthmsg);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fetchMobileandUserId();
                if (messagetxt.length() > 138) {
                    Toast.makeText(getApplicationContext(), "You can not enter more than 138 characters", Toast.LENGTH_SHORT).show();
                } else {
                    messagecustom = messagetxt.getText().toString();
                }
            }
        });

        sendbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                customRemplate = false;

                db.Open();
                db.deleteCustomTemplate();
                db.customtemplate("1", messagecustom);
                db.close();

                Intent i = new Intent(CustomTemplate.this, SendSmsScreen.class);
//		Log.w("message","getmessage"+messagecustom);
                i.putExtra("messagecustom", "okgetcustom");
                startActivity(i);
                finish();
            }
        });
    }

    public void fetchMobileandUserId() {
        dbObject.Open();
        Cursor c;
        c = dbObject.getLoginDetails();

        while (c.moveToNext()) {
            Mobile = c.getString(1);
            UserId = c.getString(3);
        }
        dbObject.close();
    }
}
/*
* private void callLogoutMethod() {
        // TODO Auto-generated method stub

        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Are you Sure you want to Exit? All your chat data will be deleted.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // delete all database
                        DatabaseCleanState();

                        Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(CustomTemplate.this, SMS19.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }

                    public SQLiteDatabase getDBObject() {
                        return dbObject.db;
                    }

                    private void DatabaseCleanState() {
                        // TODO Auto-generated method stub
                        dbObject.Open();
                        dbObject.onUpgrade(getDBObject(), 1, 1);
                        dbObject.close();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }*/