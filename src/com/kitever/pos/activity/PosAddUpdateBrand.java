package com.kitever.pos.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.POSBrandAddItemAdapter;
import com.kitever.pos.model.BrandModelData;
import com.kitever.pos.model.data.BrandInsertData;
import com.kitever.pos.model.manager.ModelManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class PosAddUpdateBrand extends PosBaseActivity implements
        NetworkManager, OnClickListener {

    private String screenName;
    private final int KEY_ADD_UPDATE_BRAND = 1;
    private EditText brandname;
    private String brandnameVal, brandIdVal;
    private ArrayList<BrandInsertData> arrayBrand;
    private MoonIcon mIcon;
    private ListView lvBrand;
    private POSBrandAddItemAdapter adapter;
    private ArrayList<BrandModelData> arrBrand;
    private LinearLayout inputLayout;
    private String s = "";
    boolean isFromProduct = false;
    private int KEY_DELETE_BRAND = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        arrayBrand = new ArrayList<>();

        mIcon = new MoonIcon(this);
        Intent intent = getIntent();

        screenName = intent.getStringExtra("screen_name");
        isFromProduct = intent.getBooleanExtra("isFromProduct", false);
        setScreenName(screenName);
        if (screenName.equalsIgnoreCase("Update Brand")) {
            brandnameVal = intent.getStringExtra("brand_name");
            brandIdVal = intent.getStringExtra("brand_id");
        }
        setBottomAction(true, true, true, true, true, true, true, true, true,
                true, true, true);
        setLayoutContentView(R.layout.pos_brand_add_update_layout);
        setScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (screenName.equalsIgnoreCase("Update Brand")) {
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
                                        deleteBrand();
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

    private void deleteBrand() {
        if (Utils.isDeviceOnline(this)) {
            try {
                Map map = new HashMap<>();
                map.put("Page", "DeleteBrand");
                map.put("BrandID", brandIdVal);
                map.put("BrandName", brandname.getText().toString());
                map.put("IsActive", "A");
                map.put("userID", getUserId());
                map.put("UserLogin", getUserLogin());
                map.put("Password", getPassWord());

                new RequestManager().sendPostRequest(this, KEY_DELETE_BRAND,
                        map);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            showMessage("Internet connection not found");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//		Intent intent = new Intent(PosAddUpdateBrand.this, POSBrandScreen.class);
        // intent.putExtra("screen_name", "Add Brand");
//		startAcitivityWithEffect(intent);
//		finish();
    }

    private void setScreen() {
        brandname = (EditText) findViewById(R.id.brand_name1);
        inputLayout = (LinearLayout) findViewById(R.id.input_layout);
        lvBrand = (ListView) findViewById(R.id.brand_add_list_view);

        RelativeLayout page_main_layout=(RelativeLayout)findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        setRobotoThinFont(brandname,this);
        brandname.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));

//		lvBrand.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
//		lvBrand.setStackFromBottom(true);


        Button addUpdatebtn = (Button) findViewById(R.id.btn_update_add);
        addUpdatebtn.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        addUpdatebtn.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        if (screenName.equalsIgnoreCase("Add Brand")) {


            addUpdatebtn.setText("Submit");
            inputLayout.setVisibility(View.GONE);
            arrBrand = new ArrayList<>();
            arrBrand.add(new BrandModelData("", "Add"));

            adapter = new POSBrandAddItemAdapter(this, arrBrand);
            lvBrand.setAdapter(adapter);

        } else {
            lvBrand.setVisibility(View.GONE);
            addUpdatebtn.setText("Update");
            brandname.setText(brandnameVal);
        }
        addUpdatebtn.setOnClickListener(this);
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        hideLoading();

        if (response != null && response.length() > 0) {
            if (requestId == KEY_ADD_UPDATE_BRAND) {
                ModelManager.getInstance().setBrandModel(response);
                if (ModelManager.getInstance().getBrandModel() != null
                        && ModelManager.getInstance().getBrandModel()
                        .getMessage() != null
                        && ModelManager.getInstance().getBrandModel()
                        .getMessage().length() > 0) {
                    // showMessage(ModelManager.getInstance().getBrandModel().getMessage());
                    // POSBrandScreen
                    Toast.makeText(
                            getApplicationContext(),
                            ModelManager.getInstance().getBrandModel()
                                    .getMessage(), Toast.LENGTH_LONG).show();
                    if (isFromProduct) {
                        Intent intent = new Intent(this, POSAddUpdateProduct.class);
                        intent.putExtra("screen_name", "Add Product");
                        startAcitivityWithEffect(intent);
                        finish();
                    } else {
//						Intent intent = new Intent(this, POSBrandScreen.class);
//						startAcitivityWithEffect(intent);
                        Intent intent = new Intent();
                        intent.putExtra("response", response);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    showMessage("Please try again.");
                }
            } else if (requestId == KEY_DELETE_BRAND) {
                Intent intent = new Intent();
                intent.putExtra("response", response);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        hideLoading();
        showMessage("Please try again.");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_update_add:

                s = "";
                if (screenName.equalsIgnoreCase("Add Brand")) {
                    arrBrand = null;
                    arrBrand = adapter.getFilteredArrayList();

                    for (int i = 0; i < arrBrand.size(); i++) {
                        if (arrBrand.get(i).getBrandName().length() > 0) {
                            s = s + arrBrand.get(i).getBrandName() + ",";
                        } else
                            arrBrand.remove(i);
                        // arrayBrand.add(new
                        // BrandInsertData(arrBrand.get(i).getBrandName()));
                    }

                    if (s.equals("")) {
                        Toast.makeText(this, "At least add one", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (s.length() == 0) {
                        Toast.makeText(this, "At least add one", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (arrBrand.get(0).getBrandName().length() == 0) {
                        Toast.makeText(this, "At least add one", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    System.out.println("Brand value " + s);
                }
                addUpdateBrand();
                break;

            default:
                break;
        }
    }

    private void addUpdateBrand() {
        if (Utils.isDeviceOnline(this)) {

            Gson gson = new Gson();
            Map map = new HashMap<>();

            gson.toString();
            if (screenName.equalsIgnoreCase("Add Brand")) {
                showLoading();
                map.put("Page", "InsertBrand");
                map.put("BrandCode", "");
                String arr = gson.toJson(arrBrand);
                try {
                    JSONArray array = new JSONArray(arr);
                    JSONObject object = new JSONObject();
                    object.put("Brands", array);
                    map.put("BrandName", object.toString());
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                System.out.println("Array value " + map.toString());
                brandname.setText("");
                brandname.setHint("Brand Name");

            } else {
                if (!com.kitever.utils.Utils.isValidString(brandname.getText()
                        .toString()))
                    Toast.makeText(this, "Enter valid string",
                            Toast.LENGTH_SHORT).show();
                else {
                    showLoading();
                    map.put("Page", "UpdateBrand");
                    map.put("BrandID", brandIdVal);
                    map.put("BrandName", brandname.getText().toString());
                    map.put("IsActive", "A");
                }
            }

            map.put("userID", getUserId());
            map.put("UserLogin", getUserLogin());
            map.put("Password", getPassWord());

            try {
                new RequestManager().sendPostRequest(this,
                        KEY_ADD_UPDATE_BRAND, map);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            showMessage("Internet connection not found");
        }

    }
}
