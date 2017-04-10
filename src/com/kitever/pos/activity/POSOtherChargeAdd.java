package com.kitever.pos.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.kitever.android.R;
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

public class POSOtherChargeAdd extends ActionBarActivity implements
        NetworkManager, OnClickListener {

    private TextView name, amount;
    private Button btnAdd;
    private ProgressDialog progressDialog = null;
    // ActionBar
    private String screenName = null;
    private String userId, userLogin, passWord;
    private TextView actionbar_title, mUserNameTitle, sorting_title;
    private ActionBar bar;
    private final int KEY_ADD_OTHER_CHARGE = 1;
    private final int KEY_DELETE_CHARGES = 2;
    private String update, otcId;
    private Boolean statusUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prfs = getSharedPreferences("profileData",
                Context.MODE_PRIVATE);
        // userLogin = "+919810398913";
        // passWord = "157013";
        // userId = "400";

        userLogin = prfs.getString("user_login", "");
        passWord = prfs.getString("Pwd", "");
        userId = Utils.getUserId(this);

        actionBarSettingWithBack(this,getSupportActionBar(),"Add Charge");

        setContentView(R.layout.activity_posother_charge_add);
        name = (TextView) findViewById(R.id.other_add_name);
        amount = (TextView) findViewById(R.id.other_add_amount);
        btnAdd = (Button) findViewById(R.id.other_add_btn);

        setRobotoThinFont(name,this);
        setRobotoThinFont(amount,this);
        setRobotoThinFontButton(btnAdd,this);

        name.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        amount.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        btnAdd.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        btnAdd.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        btnAdd.setOnClickListener(this);
        screenName = getIntent().getStringExtra("screen_name");
        if (getIntent().getStringExtra("screen_name") != null) {
            if (getIntent().getStringExtra("screen_name").equalsIgnoreCase("Update OTC")) {
                statusUpdate = true;
                btnAdd.setText("Update");
            }
            otcId = getIntent().getStringExtra("OTCId");
            amount.setText(getIntent().getStringExtra("Amount"));
            name.setText(getIntent().getStringExtra("name"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (screenName!=null && screenName.equalsIgnoreCase("Update OTC")) {
            getMenuInflater().inflate(R.menu.del_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.del_item:
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Do you want to delete it?")
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                        deleteOTC();
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();

                                    }
                                }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteOTC() {
        try {
            if (Utils.isDeviceOnline(this)) {
                Map map = new HashMap<>();
                map.put("Page", "DeleteOTC");
                map.put("OTCId", otcId);
                map.put("UserID", getUserId());
                map.put("UserLogin", getUserLogin());
                map.put("Password", getPassWord());
                new RequestManager().sendPostRequest(this,
                        KEY_DELETE_CHARGES, map);
            } else {
                Toast.makeText(this,"Internet connection not found",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    String getUserId() {
        return userId;
    }

    String getUserLogin() {
        return userLogin;
    }

    String getPassWord() {
        return passWord;
    }


    private boolean validation() {
        String nametxt = name.getText().toString();
        String amounttxt = amount.getText().toString();

        if (nametxt == "" || nametxt.isEmpty()) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (amounttxt == "" || amounttxt.isEmpty()) {
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;

    }

    private void addUpdateCharges() {
        try {
            Map map = new HashMap<>();
            if (statusUpdate) {
                map.put("Page", "UpdateOTC");
                map.put("OTCId", otcId);
            } else
                map.put("Page", "InsertOTC");

            map.put("OTC", name.getText().toString());
            map.put("Amount", amount.getText().toString());

            map.put("userID", getUserId());
            map.put("UserLogin", getUserLogin());
            map.put("Password", getPassWord());

            Log.i("taxMap", map.toString());
            new RequestManager().sendPostRequest(this, KEY_ADD_OTHER_CHARGE,
                    map);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.putExtra("response", response);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.other_add_btn:
                if (validation())
                    addUpdateCharges();
                break;

            default:
                break;
        }
    }

    public void showLoading() {

        int[] textSizeAttr = new int[]{android.R.attr.progressBarStyleSmallInverse};
        int indexOfAttrTextSize = 0;
        TypedValue typedValue = new TypedValue();
        TypedArray a = this.obtainStyledAttributes(typedValue.data,
                textSizeAttr);
        getTheme().resolveAttribute(
                android.R.attr.progressBarStyleSmallInverse, typedValue, true);
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this, R.style.MyTheme);
        }
        // progressDialog.setTitle("Please wait");
        // progressDialog.setMessage("Loading...");

        // progressDialog.setS
        progressDialog
                .setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();
    }

    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
