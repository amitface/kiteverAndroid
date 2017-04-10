package com.kitever.pos.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.customviews.DateTimePickerDialog;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.model.data.FetchSelectedProductModelData;
import com.kitever.pos.model.manager.ModelManager;
import com.kitever.utils.DateHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;

public class POSTaxAddUpdateScreen extends PosBaseActivity implements
        NetworkManager, OnClickListener, DatePickerDialog.OnDateSetListener {

    private String screenName;
    private String taxName, taxPercantageVal, wefFrom, wefTo;
    private EditText taxNameEdit, taxPercantage;
    private TextView wfefDateFromTxt, wfefDateToTxt;
    private final int KEY_ADD_UPDATE_TAX = 1;
    private final int KEY_DELETE_TAX = 2;
    private String taxId;
    private final int DIALOG_PICK_DATE_FROM_ID = 2;
    private final int DIALOG_PICK_DATE_TO_ID = 3;
    private long dateTotime = 0, dateFromtime = 0;
    int year, month, day;
    Calendar cal;
    int dateSelected = 0;
    HashMap<String, FetchSelectedProductModelData> hmap = new HashMap<String, FetchSelectedProductModelData>();
    ArrayList<String> productList = new ArrayList<String>();
    String ContactName, Type;
    private boolean orderpage = false,purchasepage=false;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        screenName = intent.getStringExtra("screen_name");
        setScreenName(screenName);
        if (screenName.equalsIgnoreCase("Update Tax")) {
            taxId = intent.getStringExtra("tax_id");
            taxName = intent.getStringExtra("tax_name");
            wefFrom = intent.getStringExtra("tax_wef_from");
            wefTo = intent.getStringExtra("tax_wef_to");
            taxPercantageVal = intent.getStringExtra("tax_per");
        }

        try {
            if (intent.getExtras() != null) {
                orderpage = intent.getBooleanExtra("orderpage", false);
                purchasepage= intent.getBooleanExtra("purchasepage", false);
                productList = intent.getStringArrayListExtra("productList");
                hmap = (HashMap<String, FetchSelectedProductModelData>) intent
                        .getSerializableExtra("hmap");
                ContactName = intent.getStringExtra("ContactName");
                Type = intent.getStringExtra("Type");
            }

        } catch (Exception e) {
        }

        setBottomAction(true, true, true, true, true, true, true, true, true,
                true, true, true);
        setLayoutContentView(R.layout.pos_tax_add_update_layout);
        setScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (screenName.equalsIgnoreCase("Update Tax")) {
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
                        .setMessage(getString(R.string.delete_brand_alert))
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                        deleteTax();
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

    private void deleteTax() {
        try {
            if (Utils.isDeviceOnline(this)) {
//                showLoading();
                Map map = new HashMap<>();
                map.put("Page", "DeleteTax");
                map.put("TaxId", taxId);
                map.put("userID", getUserId());
                map.put("UserLogin", getUserLogin());
                map.put("Password", getPassWord());
                new RequestManager().sendPostRequest(this, KEY_DELETE_TAX, map);
            } else {
                showMessage("Internet connection not found");
            }
        } catch (Exception e) {
            // TODO: handle exception
            showMessage("Please try again.");
        }

    }


    private void setScreen() {
        taxNameEdit = (EditText) findViewById(R.id.tax_name);
        taxPercantage = (EditText) findViewById(R.id.tax_percantage);
        wfefDateFromTxt = (TextView) findViewById(R.id.wfef_date_from_txt);
        wfefDateToTxt = (TextView) findViewById(R.id.wfef_date_to_txt);
        Button addUpdateBtn = (Button) findViewById(R.id.btn_update_add);

        setRobotoThinFont(taxNameEdit,this);
        setRobotoThinFont(taxPercantage,this);
        setRobotoThinFontButton(addUpdateBtn,this);

        taxNameEdit.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        taxPercantage.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        addUpdateBtn.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        addUpdateBtn.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        if (screenName.equalsIgnoreCase("Add Tax")) {
            addUpdateBtn.setText("Add");
        } else {
            addUpdateBtn.setText("Update");
            taxNameEdit.setText(taxName);
            taxPercantage.setText(taxPercantageVal);
            wfefDateFromTxt.setText(wefFrom);
            wfefDateToTxt.setText(wefTo);

        }
        addUpdateBtn.setOnClickListener(this);
        wfefDateFromTxt.setOnClickListener(this);
        wfefDateToTxt.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        dateFromtime = cal.getTimeInMillis();
    }

    private boolean validation() {
        taxName = taxNameEdit.getText().toString();
        wefFrom = wfefDateFromTxt.getText().toString();
        wefTo = wfefDateToTxt.getText().toString();
        taxPercantageVal = taxPercantage.getText().toString();

        if (taxName == "" || taxName.isEmpty()) {
            showMessage("Please enter name");
            return false;
        }
        if (taxPercantageVal == "" || taxPercantageVal.isEmpty()) {
            showMessage("Please enter tax percent ");
            return false;
        }

        if (taxPercantageVal!=null  && Float.parseFloat(taxPercantageVal) > 100) {
            taxPercantage.setText("100");
            showMessage("Tax percent can not be greater then 100 ");
            return false;
        }

//		if (wefFrom == "" || wefFrom.isEmpty()) {
//			showMessage("Please enter from date");
//			return false;
//		}
//		if (wefTo == "" || wefTo.isEmpty()) {
//			showMessage("Please enter to date");
//			return false;
//		}
//		if (dateFromtime > dateTotime) {
//			showMessage("Start date should be less than end date");
//			return false;
//		}

        return true;

    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_ADD_UPDATE_TAX) {
                Log.i("TaxResponse", "Response" + response);
                ModelManager.getInstance().setTaxModel(response);
                if (ModelManager.getInstance().getTaxModel() != null
                        && ModelManager.getInstance().getTaxModel()
                        .getMessage() != null
                        && ModelManager.getInstance().getTaxModel()
                        .getMessage().length() > 0) {
                    // showMessage(ModelManager.getInstance().getTaxModel().getMessage());
                    Toast.makeText(
                            getApplicationContext(),
                            ModelManager.getInstance().getTaxModel()
                                    .getMessage(), Toast.LENGTH_LONG).show();
                    if (ModelManager.getInstance().getTaxModel().getStatus().equals("false"))
                        return;
                    Intent intent = null;
                    if (orderpage) {
                        intent = new Intent(this, POSAddOrderScreen.class);
                        intent.putExtra("productList", productList);
                        intent.putExtra("hmap", hmap);
                        intent.putExtra("ContactName", ContactName);
                        intent.putExtra("Type", Type);
                        startActivity(intent);
                        finish();
                    }
                    else if(purchasepage)
                    {
                        intent = new Intent();
                        intent.putExtra("response", response);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
//						intent = new Intent(this, POSTaxMasterScreen.class);
                        intent = new Intent();
                        intent.putExtra("response", response);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                } else {
                    showMessage("Please try again.");
                }
            }else if(requestId == KEY_DELETE_TAX){
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
        // super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_update_add:
                if (Utils.isDeviceOnline(this)) {
                    if (validation())
                        addUpdateTax();
                } else {
                    showMessage("Internet connection not found");
                }
                break;
            case R.id.wfef_date_to_txt:
                // showDialog(DIALOG_PICK_DATE_TO_ID);
                dateSelected = 0;

                // Create a new instance of DatePickerDialog and return it
                DatePickerDialog dialogDate = new DatePickerDialog(
                        POSTaxAddUpdateScreen.this, POSTaxAddUpdateScreen.this,
                        year, month, day);
                dialogDate.getDatePicker().setMinDate(dateFromtime);
                dialogDate.show();
                break;
            case R.id.wfef_date_from_txt:
                // showDialog(DIALOG_PICK_DATE_FROM_ID);
                dateSelected = 1;

                // Create a new instance of DatePickerDialog and return it
                DatePickerDialog dialogDatefrom = new DatePickerDialog(
                        POSTaxAddUpdateScreen.this, POSTaxAddUpdateScreen.this,
                        year, month, day);
                // dialogDatefrom.getDatePicker().setMinDate(new Date().getTime());
                dialogDatefrom.show();
                break;
            default:
                break;
        }
    }

    private void addUpdateTax() {
        try {
            showLoading();
            Map map = new HashMap<>();
            if (screenName.equalsIgnoreCase("Add Tax")) {
                map.put("Page", "InsertTax");
            } else {
                map.put("Page", "UpdateTax");
                map.put("TaxId", taxId);
            }
            map.put("TaxName", taxNameEdit.getText().toString());
            map.put("TaxPer", taxPercantage.getText().toString());
            map.put("WefDateFrom", wfefDateFromTxt.getText().toString());
            map.put("WefDateTill", wfefDateToTxt.getText().toString());
            map.put("IsActive", "A");
            map.put("userID", getUserId());
            map.put("UserLogin", getUserLogin());
            map.put("Password", getPassWord());

            Log.i("taxMap", map.toString());
            new RequestManager().sendPostRequest(this, KEY_ADD_UPDATE_TAX, map);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // TODO Auto-generated method stub

        // set selected date into textview
        if (dateSelected == 1) {
            wfefDateFromTxt.setText(new StringBuilder().append(dayOfMonth)
                    .append("/").append(monthOfYear + 1).append("/")
                    .append(year).append(" "));

            dateFromtime = DateHelper.getDate(year, monthOfYear, dayOfMonth);
        } else {
            dateTotime = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            wfefDateToTxt.setText(new StringBuilder().append(dayOfMonth)
                    .append("/").append(monthOfYear + 1).append("/")
                    .append(year).append(" "));
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_PICK_DATE_TO_ID) {
            return new DateTimePickerDialog(POSTaxAddUpdateScreen.this,
                    new DateTimePickerDialog.DateTimeAcceptor() {
                        public void accept(long datetime) {
                            final String formattedDatetime = DateHelper
                                    .formatShortly(datetime);
                            // log("acceptDatetime: got " + formattedDatetime);
                            // POSTaxAddUpdateScreen.this.datetime = datetime;
                            wfefDateToTxt.setText(formattedDatetime);
                        }
                    }, new DateTimePickerDialog.DateTimeDeselector() {

                @Override
                public void deselect() {
                    wfefDateToTxt.setText("");
                }
            }, dateTotime);
        } else if (id == DIALOG_PICK_DATE_FROM_ID) {
            return new DateTimePickerDialog(POSTaxAddUpdateScreen.this,
                    new DateTimePickerDialog.DateTimeAcceptor() {
                        public void accept(long datetime) {
                            final String formattedDatetime = DateHelper
                                    .formatShortly(datetime);
                            // log("acceptDatetime: got " + formattedDatetime);
                            // POSTaxAddUpdateScreen.this.datetime = datetime;
                            wfefDateFromTxt.setText(formattedDatetime);
                        }
                    }, new DateTimePickerDialog.DateTimeDeselector() {

                @Override
                public void deselect() {
                    wfefDateFromTxt.setText("");
                }
            }, dateFromtime);
        }

        return super.onCreateDialog(id);
    }
}
