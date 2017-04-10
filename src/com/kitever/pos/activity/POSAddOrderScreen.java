package com.kitever.pos.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.adapter.CustomGrid;
import com.kitever.pos.adapter.CustomGrid.Actionable;
import com.kitever.pos.adapter.POSOtherChargesSelection;
import com.kitever.pos.adapter.POSTaxSelectionAdapter;
import com.kitever.pos.model.data.FetchSelectedProductModelData;
import com.kitever.pos.model.data.OTC;
import com.kitever.pos.model.data.OTCList;
import com.kitever.pos.model.data.TaxModelData;
import com.kitever.pos.model.manager.ModelManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFontToggleButton;
import static com.kitever.app.context.CustomStyle.MySpinnerAdapter;
import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class POSAddOrderScreen extends PosBaseActivity implements
        NetworkManager, OnClickListener, Actionable, DatePickerDialog.OnDateSetListener {

    private TextView totalPriceEdit, discountPercentageEdit, discountEdit,
            billingAmtEdit, paidAmtEdit, balanceEdit, itemTxt;
    private Spinner itemSpinner, modePaymentSpinner;
    private final int KEY_INSERT_ORDER = 1;
    private final int ITEM_SELECTED = 2;
    private final int KEY_FETCH_MODE_PAYMENT = 3;
    private final int KEY_FETCH_CUSTOMER_CONTACT = 4;
    private final int ADD_CONTACT = 5;
    private final int KEY_FETCH_TAX = 6;
    private final int KEY_FETCH_OTHER_CHARGES = 7;
    private final int ADD_CHARGES = 8;
    private final int ADD_TAX = 9;
    private MoonIcon micon;
    private String selectedItem, totalPriceVal = "", modeOfPaymentVal;
    private int modeNo = 0;
    private AutoCompleteTextView contactNumberEdit;
    private PosModeOfPaymentDetailScreen modeOfPaymentDetailScreen;
    private ArrayList<String> contactNameList;
    private ToggleButton discount_switch;
    private RelativeLayout discount_percentage_layout, discount_layout;
    private String DiscountStr = "";
    private String ContactName = "";
    private HashMap<String, FetchSelectedProductModelData> hmap = new HashMap<String, FetchSelectedProductModelData>();
    private ArrayList<String> productList = new ArrayList<String>();
    private TwoWayView taxitems, othersitem;
    private Spinner select_item;
    private TextView item_txt;
    private String Type = "";
    private RelativeLayout price_right_layout;
    private LinearLayout total_bill_layout;
    private TextView basePrice, otherPrice, dicountedPrice, totalprice;
    private TextView totaltax, totaltaxtxt, actual_amount, actual_amount_txt;
    private TableRow exclude_tax;
    boolean isTaxSettingEnable = false;
    private TextView select_tax_title, select_other_charges;
    // RadioGroup taxradiogroup;
    // RadioButton includetax, excludetax;
    private Switch taxswitch,billswitch;
    private RelativeLayout first_layout, second_layout, third_layout;

    private String Orderprice = "";
    private ArrayList<Integer> selectedTaxPos, selectedOTCPos;

    private ArrayList<String> TaxNameList, otherdata, otherdatatitles, Taxdata = new ArrayList<String>();
    private ArrayList<Long> TaxIdList, otherchargesId = new ArrayList<Long>();

    private Double TaxPercent = 0.0d, TotalTax = 0.00d, TotalPrice = 0.00d, other_total = 0.00d,
            discount_total = 0.00d, BasePrice = 0.00d;

    private String finalAmount = "", DiscountFP = "F", billto="",companyName="",contactName="";
    boolean isInclusive = false;
    private CustomGrid OherDataadapter;
    private String SelectedTax = "", paidvalue = "", BalanceValue = "", OtherChargesdata = "";
    private RelativeLayout billing_amt_layout;
    private ArrayList<OTC> OTClist = new ArrayList<OTC>();
    private Activity currentActivity;
    private RelativeLayout date_layout;
    private ImageView date_icon;
    private EditText orderdate;
    private TextView totalpricetxt, inclusive_data, dicountedPricetxt,basePricetxt,otherPricetxt,
            totalprice_title, round_off_title,roundoffpricetxt,roundoffprice;
    private SimpleDateFormat format;
    private Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    private DatePickerDialog dialogDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenName("Add Order");
        setBottomAction(true, true, true, true, false, true, true, true, true, true, true, true);
        setLayoutContentView(R.layout.pos_addorder_layout);
        currentActivity = this;
        setScreen();
        fetchModeOfPayment();
        fetchContactForPOS();
        fetchTaxList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unchecked")
    private void setScreen() {

        RelativeLayout page_main_layout=(RelativeLayout)findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));

        contactNumberEdit = (AutoCompleteTextView) findViewById(R.id.contact_number);
        totalPriceEdit = (EditText) findViewById(R.id.total_price);
        itemTxt = (TextView) findViewById(R.id.item_txt);
        totalPriceEdit.setEnabled(false);
        RelativeLayout itemLayout = (RelativeLayout) findViewById(R.id.item_layout);
        discount_switch = (ToggleButton) findViewById(R.id.discount_switch);
        discountPercentageEdit = (EditText) findViewById(R.id.discount_percentage);
        discountEdit = (EditText) findViewById(R.id.discount);
        billingAmtEdit = (EditText) findViewById(R.id.billing_amt);
        billingAmtEdit.setEnabled(false);
        paidAmtEdit = (EditText) findViewById(R.id.paid_amt);
        balanceEdit = (EditText) findViewById(R.id.balance);
        balanceEdit.setEnabled(false);

        date_layout = (RelativeLayout) findViewById(R.id.date_layout);
        date_icon = (ImageView) findViewById(R.id.date_icon);
        orderdate = (EditText) findViewById(R.id.orderdate);
        totalpricetxt = (TextView) findViewById(R.id.totalpricetxt);
        inclusive_data = (TextView) findViewById(R.id.inclusive_data);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(c.getTime());
        orderdate.setText(formattedDate);

        setRobotoThinFont(contactNumberEdit, this);
        setRobotoThinFont(totalPriceEdit, this);
        setRobotoThinFont(discountPercentageEdit, this);
        setRobotoThinFont(discountEdit, this);
        setRobotoThinFont(billingAmtEdit, this);
        setRobotoThinFont(paidAmtEdit, this);
        setRobotoThinFont(balanceEdit, this);
        setRobotoThinFont(orderdate, this);
        setRobotoThinFont(inclusive_data, this);
        setRobotoThinFont(itemTxt, this);
        setRobotoThinFontToggleButton(discount_switch,this);

        totalPriceEdit.setVisibility(View.GONE);

        first_layout = (RelativeLayout) findViewById(R.id.first_layout);
        second_layout = (RelativeLayout) findViewById(R.id.second_layout);
        third_layout = (RelativeLayout) findViewById(R.id.third_layout);

        first_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));
        second_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));
        third_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        second_layout.setVisibility(View.GONE);
        third_layout.setVisibility(View.GONE);

        micon = new MoonIcon(this);
        micon.setTextfont(totalpricetxt);


        date_layout.setOnClickListener(this);
        orderdate.setOnClickListener(this);


        billing_amt_layout = (RelativeLayout) findViewById(R.id.billing_amt_layout);

        total_bill_layout = (LinearLayout) findViewById(R.id.total_bill_layout);
        taxswitch = (Switch) findViewById(R.id.taxswitch);
        billswitch= (Switch) findViewById(R.id.billswitch);
        exclude_tax = (TableRow) findViewById(R.id.exclude_tax);

        totaltaxtxt = (TextView) findViewById(R.id.totaltaxtxt);
        totaltax = (TextView) findViewById(R.id.totaltax);


        actual_amount = (TextView) findViewById(R.id.actual_amount);
        actual_amount_txt = (TextView) findViewById(R.id.actual_amount_txt);
        basePrice = (TextView) findViewById(R.id.basePrice);
        otherPrice = (TextView) findViewById(R.id.otherPrice);
        dicountedPrice = (TextView) findViewById(R.id.dicountedPrice);
        dicountedPricetxt = (TextView) findViewById(R.id.dicountedPricetxt);
        totalprice = (TextView) findViewById(R.id.totalprice);


        basePricetxt = (TextView) findViewById(R.id.basePricetxt);
        otherPricetxt = (TextView) findViewById(R.id.otherPricetxt);
        totalprice_title = (TextView) findViewById(R.id.totalprice_title);

        round_off_title = (TextView) findViewById(R.id.round_off_title);
        roundoffpricetxt = (TextView) findViewById(R.id.roundoffpricetxt);
        roundoffprice = (TextView) findViewById(R.id.roundoffprice);


        taxitems = (TwoWayView) findViewById(R.id.taxitems);
        othersitem = (TwoWayView) findViewById(R.id.othersitem);

        //Utils.setdosisregularFont()
        item_txt = (TextView) findViewById(R.id.item_txt);

        setRobotoThinFont(totaltaxtxt, this);
        setRobotoThinFont(basePrice, this);
        setRobotoThinFont(totaltax, this);
        setRobotoThinFont(actual_amount, this);
        setRobotoThinFont(actual_amount_txt, this);
        setRobotoThinFont(otherPrice, this);
        setRobotoThinFont(dicountedPrice, this);
        setRobotoThinFont(totalprice, this);
        setRobotoThinFont(item_txt, this);
        setRobotoThinFont(dicountedPricetxt, this);
        setRobotoThinFont(basePricetxt, this);
        setRobotoThinFont(otherPricetxt, this);
        setRobotoThinFont(totalprice_title, this);

        setRobotoThinFont(round_off_title, this);
        setRobotoThinFont(roundoffprice, this);

        micon.setTextfont(roundoffpricetxt);



        totaltaxtxt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        basePrice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        totaltax.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        actual_amount.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        actual_amount_txt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        otherPrice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        dicountedPrice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        totalprice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        item_txt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        dicountedPricetxt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        basePricetxt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        otherPricetxt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        totalprice_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        round_off_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        roundoffpricetxt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        roundoffprice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        select_item = (Spinner) findViewById(R.id.select_item);
        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("Select Item Type");
        typeList.add("Goods");
        typeList.add("Services");
        typeList.add("Goods & Services");
        MySpinnerAdapter typeAdapter = new MySpinnerAdapter(this, android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_item.setAdapter(typeAdapter);

        select_item.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String typeitem = parent.getItemAtPosition(position).toString();
                if (position > 0) {
                    sendtoCart(typeitem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        taxswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    billingAmtEdit.setHint("Price Including Tax");
                    isInclusive = true;
                    inclusive_data.setVisibility(View.VISIBLE);
                    exclude_tax.setVisibility(View.GONE);
                    taxswitch.setHint("Tax Included");
                } else {
                    billingAmtEdit.setHint("Price Excluding Tax");
                    inclusive_data.setVisibility(View.GONE);
                    exclude_tax.setVisibility(View.VISIBLE);
                    isInclusive = false;
                    taxswitch.setHint("Tax Excluded");
                }
                discountPercentageEdit.setText("");
                discountEdit.setText("");
                calculatePrice();
            }
        });

        billswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    billto=companyName;
                } else {
                    billto=contactName;
                }
                billswitch.setText("Bill to "+billto);
            }
        });

        select_tax_title = (TextView) findViewById(R.id.select_tax_title);
        select_other_charges = (TextView) findViewById(R.id.select_other_charges);

        setRobotoThinFont(select_tax_title, this);
        setRobotoThinFont(select_other_charges, this);

        String taxstr = "<u>Select Tax</u>";
        String otherstr = "<u>Other Charges</u>";
        select_tax_title.setText(Html.fromHtml(taxstr));
        select_other_charges.setText(Html.fromHtml(otherstr));

        modePaymentSpinner = (Spinner) findViewById(R.id.mode_payment);
        ImageView addContact = (ImageView) findViewById(R.id.add_btn);
        addContact.setOnClickListener(this);
        TextView addMainBtn = (TextView) findViewById(R.id.btn_add_without_invoice);
        TextView addInvoiceBtn = (TextView) findViewById(R.id.btn_add_invoice);

        setRobotoThinFont(addMainBtn, this);
        setRobotoThinFont(addInvoiceBtn, this);

        addInvoiceBtn.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        addInvoiceBtn.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));


        select_tax_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        select_other_charges.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        addMainBtn.setOnClickListener(this);
        addInvoiceBtn.setOnClickListener(this);
        itemLayout.setOnClickListener(this);
        select_tax_title.setOnClickListener(this);
        select_other_charges.setOnClickListener(this);

        try {
            Intent intent = getIntent();
            if (intent.getExtras() != null) {
                Type = intent.getStringExtra("Type");
                productList = intent.getStringArrayListExtra("productList");
                hmap = (HashMap<String, FetchSelectedProductModelData>) intent.getSerializableExtra("hmap");
                if (hmap != null && hmap.size() > 0) getCartItem(hmap, productList);
                ContactName = intent.getStringExtra("ContactName");
                if (ContactName.length()>0) {
                    contactNumberEdit.setText(ContactName);
                    billerType();
                }
            }
        } catch (Exception e) {
        }

        //discount_switch.setTextOn("Amt");
        //discount_switch.setTextOff("%");
        discount_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    discountPercentageEdit.setText("");
                    discountPercentageEdit.setVisibility(View.GONE);
                    discountEdit.setVisibility(View.VISIBLE);
                    discount_switch.setBackgroundResource(R.drawable.toggle_amt);
                    discount_total = 0.00d;
                    DiscountFP = "F";
                } else {
                    discountEdit.setText("");
                    discountPercentageEdit.setVisibility(View.VISIBLE);
                    discountEdit.setVisibility(View.GONE);
                    discount_switch.setBackgroundResource(R.drawable.toggle_percent);
                    discount_total = 0.00d;
                    DiscountFP = "P";
                }
                calculatePrice();
            }
        });

        paidAmtEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                balanceEdit.setText("");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String paidAmt = paidAmtEdit.getText().toString();
                try {
                    if (paidAmt != null && !paidAmt.equalsIgnoreCase("")) {
                        Double amt = Double.parseDouble(paidAmt);

                        if (billingAmtEdit.getText() != null
                                && !billingAmtEdit.getText().toString().trim()
                                .equalsIgnoreCase("")) {

                            Double billingAmt = Double.parseDouble(finalAmount);
                            if (amt > billingAmt) {
                                paidAmtEdit.setText("");
                                paidAmtEdit.setHint("Paid amount");
                            } else {
                                Double bal = billingAmt - amt;
                                DecimalFormat f = new DecimalFormat("##.00");
                                balanceEdit.setText("" + f.format(bal));
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        });

        discountPercentageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                try {
                    DiscountStr = s.toString().trim();
                    if (DiscountStr.length() > 0) {
                        discountEdit.clearComposingText();
                        paidAmtEdit.setText("");

                        Double percentage = 0.00d;
                        discount_total = 0.00d;

                        percentage = Double.parseDouble(DiscountStr);
                        if (percentage > 100) {
                            discountPercentageEdit.setText("100");
                            //discountPercentageEdit.setHint("Discount%");
                            Toast.makeText(POSAddOrderScreen.this, "Discount cannot be greater then 100 %", Toast.LENGTH_SHORT).show();
                        } else {
                            Double per = percentage / 100;
                            Double itemPrice = Double.parseDouble(totalPriceVal);
                            discount_total = per * itemPrice;
                        }
                    } else {
                        discount_total = 0.00d;
                    }
                    calculatePrice();
                } catch (Exception e) {

                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        discountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    DiscountStr = s.toString();
                    if (DiscountStr.length() > 0) {
                        paidAmtEdit.setText("");
                        discount_total = 0.00d;
                        discountPercentageEdit.clearComposingText();
                        Double totalPrice = 0.00d;
                        String inputDiscount = null;
                        Double inputDisVal = 0.00d;
                        if (totalPriceVal != null)
                            totalPrice = Double.parseDouble(totalPriceVal);
                        if (s.toString() != null)
                            inputDiscount = discountEdit.getText().toString();
                        if (inputDiscount != null) {
                            billingAmtEdit.setText(totalPriceVal);
                            inputDisVal = Double.parseDouble(inputDiscount);
                            if (inputDisVal > totalPrice) {
                                discountEdit.setText("" + totalPrice);
                                //discountEdit.setHint("Discount");
                                Toast.makeText(POSAddOrderScreen.this, "Discount cannot be greater then total price", Toast.LENGTH_SHORT).show();
                            } else {
                                if (totalPriceVal != null
                                        && !totalPriceVal.trim().equalsIgnoreCase("")) {
                                    Double itemPrice = Double.parseDouble(totalPriceVal);
                                    //Double discountPrice = itemPrice - inputDisVal;
                                    discount_total = inputDisVal;
                                    Log.i("discountper", "amt--" + inputDisVal);
                                    //billingAmtEdit.setText("" + discountPrice);
                                }
                            }
                        }

                    } else {
                        discount_total = 0.00d;
                        //billingAmtEdit.setText(totalPriceVal);
                    }
                    calculatePrice();

                } catch (Exception e) {

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void orderValidation() {
        if (contactNumberEdit.getText().toString() == null
                || contactNumberEdit.getText().toString().trim()
                .equalsIgnoreCase("")) {
            Toast.makeText(POSAddOrderScreen.this, "Please select contact",
                    Toast.LENGTH_LONG).show();
        } else if (selectedItem == null
                || selectedItem.trim().equalsIgnoreCase("")) {
            Toast.makeText(POSAddOrderScreen.this, "Please select item",
                    Toast.LENGTH_LONG).show();

        } else if (orderdate.getText() == null || orderdate.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(POSAddOrderScreen.this, "Please select date",
                    Toast.LENGTH_LONG).show();
        } else if (paidAmtEdit.getText() == null
                || paidAmtEdit.getText().toString().trim()
                .equalsIgnoreCase("")) {
            modeNo = 1;
            addOrder();
            //Toast.makeText(POSAddOrderScreen.this,"Please fill paid amount", Toast.LENGTH_LONG).show();
        } else if (modeNo == 0) {
            Toast.makeText(POSAddOrderScreen.this,
                    "Please select mode of payment", Toast.LENGTH_LONG)
                    .show();
        } else {
            addOrder();
        }
    }

    @Override
    public void onClick(View v) {

//        super.onClick(v);
        switch (v.getId()) {

            case R.id.orderdate:
            case R.id.date_layout:
                dialogDate = new DatePickerDialog(this, this, year, month, day);
                dialogDate.getDatePicker().setMaxDate((new Date().getTime()));//-(5097600000L)
                dialogDate.show();
                break;

            case R.id.btn_add_without_invoice:
                orderValidation();
                break;
            case R.id.btn_add_invoice:
                orderValidation();
                break;
            case R.id.add_btn:
                Intent intent = new Intent(this,
                        sms19.listview.newproject.ContactAdd.class);
                intent.putExtra("froninapp", true);
                startActivityForResult(intent, ADD_CONTACT);

                break;
            case R.id.item_layout:
                if (hmap.size() < 1) select_item.performClick();
                else sendtoCart(Type);
                break;
            case R.id.select_tax_title:

                if (ModelManager.getInstance().getTaxModel()
                        .getFetchTaxList() != null && ModelManager.getInstance().getTaxModel()
                        .getFetchTaxList().size() > 0)
                    setTaxScreen(ModelManager.getInstance().getTaxModel()
                            .getFetchTaxList());

                else AddTaxAlert();

                break;
            case R.id.select_other_charges:
                if (OTClist != null && OTClist.size() > 0)
                    AddOtherCharges(OTClist);
                else AddOtherAlert();
                break;

        }
    }

    @SuppressWarnings("unchecked")
    private void fetchTaxList() {
        if (Utils.isDeviceOnline(this)) {
            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchTax");
            map.put("IsActive", "A");
            map.put("userID", getUserId());
            map.put("UserLogin", getUserLogin());
            map.put("Password", getPassWord());
            try {
                new RequestManager().sendPostRequest(this, KEY_FETCH_TAX, map);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            showMessage("Internet connection not found");
        }
        fetchOtherChargeList();
    }

    @SuppressWarnings("unchecked")
    private void fetchOtherChargeList() {
        if (Utils.isDeviceOnline(this)) {

            Log.i("fetchOtherChargeList", "fetchOtherChargeList");
            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchOTC");
            map.put("IsActive", "A");
            map.put("userID", getUserId());
            map.put("UserLogin", getUserLogin());
            map.put("Password", getPassWord());
            try {
                new RequestManager().sendPostRequest(this, KEY_FETCH_OTHER_CHARGES, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    public void sendtoCart(String Type) {
        ContactName = contactNumberEdit.getText().toString();
        Log.i("select item ", "ContactName - " + ContactName);

        if (hmap != null && hmap.size() > 0) {
            Intent cartintent = new Intent(POSAddOrderScreen.this, PosCart.class);
            cartintent.putExtra("productList", productList);
            cartintent.putExtra("hmap", hmap);
            cartintent.putExtra("ContactName", ContactName);
            cartintent.putExtra("Type", Type);
            startActivity(cartintent);
            finish();
        } else {
            Intent intentItem = new Intent(POSAddOrderScreen.this, POSItemChooseScreen.class);
            intentItem.putExtra("ContactName", ContactName);
            intentItem.putExtra("Type", Type);
            startActivity(intentItem);
            finish();
        }

    }

    public String getPaidStatus(String balAmt, String finalAmt) {
        String paidStatus = "";
        if (Double.parseDouble(balAmt) == 0) paidStatus = "Paid";
        else if (Double.parseDouble(balAmt) == Double.parseDouble(finalAmt)) paidStatus = "Unpaid";
        else paidStatus = "Partialy paid";
        return paidStatus;
    }

    private void addOrder() {
        if (Utils.isDeviceOnline(this)) {
            try {
                showLoading();
                String modeOfPayId = "";
                Map map = new HashMap<>();
                map.put("Page", "InsertOrder");
                map.put("BillAmount", finalAmount);//finalAmount=""+totalPriceVal;
                map.put("TaxAmount", "" + TotalTax);
                map.put("isInclusiveTax", "" + isInclusive);
                map.put("OTCAmount", "" + other_total);
                map.put("OrderDate", orderdate.getText().toString());
                map.put("TaxApplied", SelectedTax);
                map.put("OTCApplied", OtherChargesdata);


                paidvalue = paidAmtEdit.getText().toString();
                BalanceValue = balanceEdit.getText().toString();


                if (paidvalue == null || paidvalue.equalsIgnoreCase("")) {
                    paidvalue = "0";
                    BalanceValue = finalAmount;
                }


                map.put("PaidAmount", paidvalue);
                map.put("BalanceAmount", BalanceValue);
                map.put("billedto", billto);
                map.put("SubTotal", billingAmtEdit.getText().toString());

                String disPerVal = "0";

                if (discountPercentageEdit.getText() != null
                        || discountPercentageEdit.getText().toString().trim().length() > 0) {
                    disPerVal = discountPercentageEdit.getText().toString()
                            .trim();

                }
                if (disPerVal == null || disPerVal.equalsIgnoreCase("")) {
                    disPerVal = "0";
                }
                map.put("DiscountPercent", disPerVal);
                map.put("DiscountAmount", "" + discount_total);
                map.put("DiscountAppliedFP", DiscountFP);

                String pstatus = getPaidStatus(BalanceValue, finalAmount);
                map.put("Status", pstatus);

                if (ModelManager.getInstance().getFetchModeOfPaymentModel() != null
                        && ModelManager.getInstance()
                        .getFetchModeOfPaymentModel()
                        .getFetchModeOfPaymentList() != null
                        && ModelManager.getInstance()
                        .getFetchModeOfPaymentModel()
                        .getFetchModeOfPaymentList().size() > 0) {
                    try {
                        modeOfPayId = ModelManager.getInstance()
                                .getFetchModeOfPaymentModel()
                                .getFetchModeOfPaymentList().get(modeNo - 1)
                                .getId();
                    } catch (Exception e) {

                    }
                }
                map.put("PayMode", modeOfPayId);
                if (modeOfPaymentDetailScreen != null
                        && modeOfPaymentDetailScreen.getChequeNo() != null) {
                    map.put("PayModeNo",
                            modeOfPaymentDetailScreen.getChequeNo());
                } else {
                    map.put("PayModeNo", "");
                }
                if (modeOfPaymentDetailScreen != null
                        && modeOfPaymentDetailScreen.getDate() != null
                        && !modeOfPaymentDetailScreen.getDate()
                        .equalsIgnoreCase("date")) {
                    map.put("ChequeDate", modeOfPaymentDetailScreen.getDate());
                } else {
                    map.put("ChequeDate", "");
                }
                if (modeOfPaymentDetailScreen != null
                        && modeOfPaymentDetailScreen.getBankNameOrRefNum() != null) {
                    map.put("BankName",
                            modeOfPaymentDetailScreen.getBankNameOrRefNum());
                } else {
                    map.put("BankName", "");
                }
                if (modeOfPaymentDetailScreen != null
                        && modeOfPaymentDetailScreen.getRemarkOrDes() != null) {
                    map.put("Remark",
                            modeOfPaymentDetailScreen.getRemarkOrDes());
                } else {
                    map.put("Remark", "");
                }
                map.put("Items", selectedItem);
                String contactId = "";
                if (contactNameList != null
                        && contactNameList.size() > 0
                        && ModelManager.getInstance().getFetchContactModel() != null
                        && ModelManager.getInstance().getFetchContactModel()
                        .getFetchContactList() != null
                        && ModelManager.getInstance().getFetchContactModel()
                        .getFetchContactList().size() > 0) {
                    int index = contactNameList.indexOf(contactNumberEdit
                            .getText().toString());
                    contactId = ModelManager.getInstance()
                            .getFetchContactModel().getFetchContactList()
                            .get(index).getContactId();
                }
                map.put("contactID", contactId);
                map.put("UserLogin", getUserLogin());
                map.put("Password", getPassWord());
                map.put("UserID", getUserId());


                Log.i("postmap", "post:" + map.toString());
                new RequestManager().sendPostRequest(POSAddOrderScreen.this, KEY_INSERT_ORDER, map);
            } catch (Exception e) {
                Log.i("Error", "--" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    private void fetchModeOfPayment() {
        try {
            Map map = new HashMap<>();
            map.put("Page", "FetchModeOFPayMent");
            new RequestManager().sendPostRequest(POSAddOrderScreen.this,
                    KEY_FETCH_MODE_PAYMENT, map);
        } catch (Exception e) {

        }
    }

    private void fetchContactForPOS() {
        try {
            Map map = new HashMap<>();
            map.put("Page", "FetchContactForPOS");
            map.put("userID", getUserId());
            new RequestManager().sendPostRequest(POSAddOrderScreen.this,
                    KEY_FETCH_CUSTOMER_CONTACT, map);
        } catch (Exception e) {

        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {

        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_INSERT_ORDER) {
                ModelManager.getInstance().setOrderDetailModel(response);
                if (ModelManager.getInstance().getOrderDetailModel() != null)
                    //showMessage(ModelManager.getInstance().getOrderDetailModel().getMessage());
                    Toast.makeText(getApplicationContext(), ModelManager.getInstance()
                            .getOrderDetailModel().getMessage(), Toast.LENGTH_LONG).show();
                if (ModelManager.getInstance().getOrderDetailModel().getStatus().equals("true")) {

                    Intent intent = new Intent();
                    intent.putExtra("response", response);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {

                }

            } else if (requestId == KEY_FETCH_MODE_PAYMENT) {
                ModelManager.getInstance().setFetchModeOfPaymentModel(response);
                ArrayList<String> list = new ArrayList<>();
                list.add("Select mode of payment                                          ");
                if (ModelManager.getInstance().getFetchModeOfPaymentModel() != null
                        && ModelManager.getInstance()
                        .getFetchModeOfPaymentModel()
                        .getFetchModeOfPaymentList() != null
                        && ModelManager.getInstance()
                        .getFetchModeOfPaymentModel()
                        .getFetchModeOfPaymentList().size() > 0) {
                    for (int k = 0; k < ModelManager.getInstance()
                            .getFetchModeOfPaymentModel()
                            .getFetchModeOfPaymentList().size(); k++) {
                        list.add(ModelManager.getInstance()
                                .getFetchModeOfPaymentModel()
                                .getFetchModeOfPaymentList().get(k).getMode());
                    }
                }
                MySpinnerAdapter adapter = new MySpinnerAdapter(this,
                        android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                modePaymentSpinner.setAdapter(adapter);
                modePaymentSpinner
                        .setOnItemSelectedListener(itemSelectedListener);
            } else if (requestId == KEY_FETCH_CUSTOMER_CONTACT) {
                ModelManager.getInstance().setFetchContactModel(response);
                contactNameList = new ArrayList<String>();
                if (ModelManager.getInstance().getFetchContactModel() != null
                        && ModelManager.getInstance().getFetchContactModel()
                        .getFetchContactList() != null
                        && ModelManager.getInstance().getFetchContactModel()
                        .getFetchContactList().size() > 0) {
                    for (int k = 0; k < ModelManager.getInstance()
                            .getFetchContactModel().getFetchContactList()
                            .size(); k++) {
                        String contact_name=ModelManager.getInstance().getFetchContactModel().getFetchContactList().get(k).getContactName().substring(3);
                        String company_name=ModelManager.getInstance().getFetchContactModel().getFetchContactList().get(k).getCompanyName();
                        String concat_string="";
                        if(!company_name.equals("null")) concat_string=contact_name+" - "+company_name;
                        else concat_string=contact_name;
                        contactNameList.add(concat_string);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            this, android.R.layout.simple_list_item_1,
                            contactNameList);
                    contactNumberEdit.setThreshold(1);
                    contactNumberEdit.setAdapter(adapter);

                    contactNumberEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            billerType();
                        }
                    });
                } else {
                    MySpinnerAdapter adapter = new MySpinnerAdapter(
                            this, android.R.layout.simple_list_item_1,
                            contactNameList);
                    contactNumberEdit.setAdapter(adapter);
                }

            } else if (requestId == KEY_FETCH_TAX) {

                ArrayList<String> list = new ArrayList<>();
                list.add("Select Tax ");
                ModelManager.getInstance().setTaxModel(response);
                if (ModelManager.getInstance().getTaxModel() != null
                        && ModelManager.getInstance().getTaxModel()
                        .getFetchTaxList() != null
                        && ModelManager.getInstance().getTaxModel()
                        .getFetchTaxList().size() > 0) {

                    for (int k = 0; k < ModelManager.getInstance()
                            .getTaxModel()
                            .getFetchTaxList().size(); k++) {
                        list.add(ModelManager.getInstance().getTaxModel()
                                .getFetchTaxList().get(k).getTaxName() + " - " + ModelManager.getInstance().getTaxModel()
                                .getFetchTaxList().get(k).getTaxPer());

                    }

                    isTaxSettingEnable = Boolean.parseBoolean(ModelManager.getInstance().getTaxModel().getTaxSettingEnable());
                    taxswitch.setChecked(isTaxSettingEnable);

                    list.add("Add New Tax");

				/*ArrayAdapter<String> taxadapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, list);
				taxadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

                } else {

                }
            } else if (requestId == KEY_FETCH_OTHER_CHARGES) {

                Gson gson = new Gson();
                OTCList otclist = gson.fromJson(response, OTCList.class);

                if (otclist.getStatus() != null
                        && otclist.getStatus().equals("true")) {
                    OTClist = otclist.getOTC();

                } else {
                    if (otclist.getStatus() != null
                            && otclist.getMessage() != null) {

                    }
                }
            }
        }
    }

    private void billerType()
    {
        companyName="";
        String str=contactNumberEdit.getText().toString();
        String[] contactName_ar=str.split(" ");
        contactName=contactName_ar[1];
        String[] comapnayName_ar=str.split("-");
        if(comapnayName_ar.length>1)
            companyName=comapnayName_ar[1];
        billswitch.setEnabled(true);
        if (companyName.length()>0) {
            billswitch.setChecked(true);
            billto=companyName;
        } else {
            billswitch.setChecked(false);
            billswitch.setEnabled(false);
            billto=contactName;
        }
      //  billswitch.setText("Bill to "+billto);
    }

    private void AddOtherCharges(final ArrayList<OTC> list) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.othercharge);
        dialog.setTitle("");
        ListView othercharge_list_view = (ListView) dialog.findViewById(R.id.othercharge_list_view);
        final POSOtherChargesSelection otheradapter = new POSOtherChargesSelection(list, otherdatatitles);
        othercharge_list_view.setAdapter(otheradapter);
        TextView add_other_value = (TextView) dialog.findViewById(R.id.add_other_value);
        TextView otherdonetxt = (TextView) dialog.findViewById(R.id.donetxt);
        micon.setTextfont(otherdonetxt);
        add_other_value.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(currentActivity,
                        POSOtherChargeAdd.class);
                startActivityForResult(intent, ADD_CHARGES);
            }
        });

        //Button other_done = (Button) dialog.findViewById(R.id.other_done);
        otherdonetxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otheradapter != null) {
                    other_total = 0.0d;
                    //selected_tax.removeAllViews();
                    otherdatatitles = new ArrayList<String>();
                    otherdata = new ArrayList<String>();
                    otherchargesId = new ArrayList<Long>();
                    selectedOTCPos = otheradapter.getcheckeditemcount();
                    if (selectedOTCPos != null && selectedOTCPos.size() > 0) {
                        for (int k = 0; k < selectedOTCPos.size(); k++) {
                            OTC object = list.get(selectedOTCPos.get(k));
                            otherdatatitles.add(object.getOTC());
                            otherchargesId.add(Long.parseLong(object.getOTCID()));
                            otherdata.add(object.getOTC() + " - " + object.getAmount());
                            other_total = other_total + Double.parseDouble(object.getAmount());
                        }


                    }
                    calculatePrice();

                }
                dialog.dismiss();
            }

        });
        dialog.show();
    }

    public void AddTaxAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add New Other Charegs");
        alert.setMessage("No tax found add new ");
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(currentActivity, POSTaxAddUpdateScreen.class);
                intent.putExtra("screen_name", "Add Tax");
                intent.putExtra("orderpage", true);
                intent.putExtra("productList", productList);
                intent.putExtra("hmap", hmap);
                intent.putExtra("ContactName", ContactName);
                intent.putExtra("Type", Type);
                startActivityForResult(intent, ADD_TAX);
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void AddOtherAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add New Other Charegs");
        alert.setMessage("No other charges found add new ");
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent intent = new Intent(currentActivity,
                        POSOtherChargeAdd.class);
                startActivityForResult(intent, ADD_CHARGES);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void setTaxScreen(final ArrayList<TaxModelData> list) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tax_selection_parent_layout);
        dialog.setTitle("Select Tax");

        //dialog
        ListView taxListView = (ListView) dialog
                .findViewById(R.id.tax_list_view);
        final POSTaxSelectionAdapter adapter = new POSTaxSelectionAdapter(list, TaxNameList);
        taxListView.setAdapter(adapter);
        TextView add_tax = (TextView) dialog.findViewById(R.id.add_tax);
        TextView donetxt = (TextView) dialog.findViewById(R.id.donetxt);
        //  Button doneBtn = (Button) dialog.findViewById(R.id.tax_done_btn);
        micon.setTextfont(donetxt);
        add_tax.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(currentActivity, POSTaxAddUpdateScreen.class);
                intent.putExtra("screen_name", "Add Tax");
                intent.putExtra("orderpage", true);
                intent.putExtra("screen_name", "Add Tax");
                intent.putExtra("productList", productList);
                intent.putExtra("hmap", hmap);
                intent.putExtra("ContactName", ContactName);
                intent.putExtra("Type", Type);
                startActivityForResult(intent, ADD_TAX);
            }
        });

        donetxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (adapter != null) {
                    TaxPercent = 0.0d;
                    //selected_tax.removeAllViews();
                    TaxNameList = new ArrayList<String>();
                    Taxdata = new ArrayList<String>();
                    TaxIdList = new ArrayList<Long>();
                    selectedTaxPos = adapter.getcheckeditemcount();
                    if (selectedTaxPos != null && selectedTaxPos.size() > 0) {
                        for (int k = 0; k < selectedTaxPos.size(); k++) {
                            TaxModelData object = list.get(selectedTaxPos.get(k));
                            TaxNameList.add(object.getTaxName());
                            TaxIdList.add(Long.parseLong(object.getTaxID()));
                            Taxdata.add(object.getTaxName() + " - " + object.getTaxPer() + "%");
                            TaxPercent = TaxPercent + Double.parseDouble(object.getTaxPer());
                        }
                        if (TaxPercent > 100)
                            Toast.makeText(POSAddOrderScreen.this, "Tax cannot be greater then 100 % ", Toast.LENGTH_SHORT).show();
                        else {
                            calculatePrice();
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        selectedTax();

                    }
                }

            }
        });
        dialog.show();
    }

    private void selectedTax() {
        try {
            ArrayList<TaxModelData> list = ModelManager.getInstance().getTaxModel().getFetchTaxList();
            TaxPercent = 0.00d;
            for (int k = 0; k < list.size(); k++) {
                TaxModelData object = list.get(k);
                if (TaxNameList.contains(object.getTaxName())) {
                    TaxPercent = TaxPercent + Double.parseDouble(object.getTaxPer());
                }

            }
            other_total = 0.00d;
            for (int j = 0; j < OTClist.size(); j++) {
                OTC OTCobject = OTClist.get(j);
                if (otherdatatitles.contains(OTCobject.getOTC())) {
                    other_total = other_total + Double.parseDouble(OTCobject.getAmount());
                }

            }

            calculatePrice();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculatePrice() {
        Orderprice = billingAmtEdit.getText().toString().trim();
        Log.i("calculatePrice", "Orderprice-" + Orderprice);

        BasePrice = 0.00d;
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);

        if (Orderprice.length() > 0) {
            Double tprice = 0.00d;

            BasePrice = Double.parseDouble(Orderprice);
            tprice = BasePrice - discount_total;
            total_bill_layout.setVisibility(View.VISIBLE);
            if (TaxPercent > 0 && taxswitch.isChecked()) {
                BasePrice = ((BasePrice - discount_total) * 100) / (100 + TaxPercent);
                // BasePrice = ((BasePrice) * 100) / (100 + TaxPercent);
                TotalTax = (BasePrice) * TaxPercent / 100;
                TotalPrice = BasePrice + TotalTax + other_total;
            } else {
                TotalTax = (BasePrice - discount_total) * TaxPercent / 100;
                TotalPrice = BasePrice + TotalTax + other_total - discount_total;
            }
            Log.i("calculatePrice", "After-BasePrice-" + BasePrice + "discount_total-" + discount_total);
            finalAmount = Utils.doubleToString(TotalPrice);
            actual_amount.setText(Orderprice);
            basePrice.setText(Utils.doubleToString(tprice));
            dicountedPrice.setText(Utils.doubleToString(discount_total));
            if (TotalTax > 0) {
                if (isInclusive) {
                    inclusive_data.setVisibility(View.VISIBLE);
                    inclusive_data.setText("[" + Utils.doubleToString(BasePrice) + "(p) + " + Utils.doubleToString(TotalTax) + "(t)]");
                } else {
                    exclude_tax.setVisibility(View.VISIBLE);
                    totaltax.setText(Utils.doubleToString(TotalTax));
                }
            } else {
                inclusive_data.setVisibility(View.GONE);
                exclude_tax.setVisibility(View.GONE);
            }
            otherPrice.setText(Utils.doubleToString(other_total));
            totalprice.setText(finalAmount);
            finalAmount=""+Math.round(TotalPrice);
            roundoffprice.setText(finalAmount);
        } else {
            //total_bill_layout.setVisibility(View.VISIBLE);
        }

        CustomGrid staxadapter = new CustomGrid(POSAddOrderScreen.this, Taxdata, false);
        taxitems.setAdapter(staxadapter);

        CustomGrid OherDataadapter = new CustomGrid(POSAddOrderScreen.this, otherdata, true);
        othersitem.setAdapter(OherDataadapter);
        paidAmtEdit.setText("");
    }

    OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Spinner spinModePayment = (Spinner) parent;
            if (spinModePayment.getId() == R.id.mode_payment) {
                modeOfPaymentVal = (String) spinModePayment.getSelectedItem();
                modeNo = position;
                if (position > 0 && !modeOfPaymentVal.equalsIgnoreCase("Cash")) {
                    modeOfPaymentDetailScreen = new PosModeOfPaymentDetailScreen(
                            POSAddOrderScreen.this);
                    modeOfPaymentDetailScreen.setPaymentType(modeOfPaymentVal);
                    modeOfPaymentDetailScreen.show();
                }
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onErrorResponse(VolleyError error) {
        hideLoading();
        showMessage("Please try again.");

    }

    private void getCartItem(HashMap<String, FetchSelectedProductModelData> hmap, ArrayList<String> productList) {
        if (hmap.size() > 0) {
            item_txt.setVisibility(View.VISIBLE);
            select_item.setVisibility(View.GONE);
        } else {
            item_txt.setVisibility(View.GONE);
            select_item.setVisibility(View.VISIBLE);
        }
        double tprice = 0.00;
        int totalq = 0;
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < hmap.size(); i++) {
            String key = productList.get(i);
            String rate = hmap.get(key).getpricewithTax();
            String qty = hmap.get(key).getQuantity();
            int q = Integer.parseInt(qty);
            Double item_rate = Double.parseDouble(rate);
            tprice = tprice + q * item_rate;
            totalq = totalq + q;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ProductID", key);
                jsonObject.put("ProductName", hmap.get(key).getProductName());
                jsonObject.put("ProductUnitPrice", hmap.get(key).getPerUnitPrice());
                jsonObject.put("PricewithTax", hmap.get(key).getpricewithTax());
                jsonObject.put("DiscountPercent", "0");
                jsonObject.put("DiscountAmount", "0");
                jsonObject.put("TaxApplied", hmap.get(key).getTaxApplied());
                jsonObject.put("Quantity", hmap.get(key).getQuantity());
                jsonObject.put("ProductUnit", hmap.get(key).getUnits());
                jsonObject.put("Factor", 1);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            object.put("items", jsonArray);
            selectedItem = object.toString();
            Log.i("selectedItem", "--" + selectedItem);
        } catch (JSONException e) {
        }

        try {

            if (tprice > 0) {
                totalPriceVal = Utils.doubleToString(tprice);
                Log.i("totalPriceVal", "--" + totalPriceVal);
                finalAmount = totalPriceVal;
                itemTxt.setText("Total " + Type + " : " + totalq);

                totalPriceEdit.setText(totalPriceVal);
                totalPriceEdit.setVisibility(View.VISIBLE);
                billingAmtEdit.setText(totalPriceVal);
                // Orderprice = billingAmtEdit.getText().toString().trim();

                second_layout.setVisibility(View.VISIBLE);
                third_layout.setVisibility(View.VISIBLE);
                calculatePrice();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_CONTACT) {
                fetchContactForPOS();
            }
        }
        if (requestCode == ADD_CHARGES) {
            if (resultCode == RESULT_OK) {
                Gson gson = new Gson();
                OTCList otclist = gson.fromJson(data.getStringExtra("response"), OTCList.class);

                if (otclist.getStatus() != null
                        && otclist.getStatus().equals("true")) {
                    OTClist = otclist.getOTC();
                } else {
                    if (otclist.getStatus() != null
                            && otclist.getMessage() != null) {
                    }
                }
            }
        }
    }

    @Override
    public void deleteItem(int id, boolean isOthher) {
        if (isOthher) {
            otherdatatitles.remove(id);
            otherchargesId.remove(id);
        } else {
            TaxNameList.remove(id);
            TaxIdList.remove(id);
        }
        selectedTax();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        orderdate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
    }
}
