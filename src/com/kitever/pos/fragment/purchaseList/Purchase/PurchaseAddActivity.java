package com.kitever.pos.fragment.purchaseList.Purchase;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.activity.POSTaxAddUpdateScreen;
import com.kitever.pos.activity.PosModeOfPaymentDetailScreen;
import com.kitever.pos.adapter.POSTaxSelectionAdapter;
import com.kitever.pos.fragment.purchaseList.GetPurchaseListModel;
import com.kitever.pos.model.data.TaxModelData;
import com.kitever.pos.model.manager.ModelManager;

import org.lucasr.twowayview.TwoWayView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontSWitch;
import static com.kitever.utils.Utils.actionBarSettingWithBack;


public class PurchaseAddActivity extends AppCompatActivity implements NetworkManager, PurchaseTwoWayViewAdapter.ActionableTwoWay, View.OnClickListener, DatePickerDialog.OnDateSetListener, CompoundButton.OnCheckedChangeListener, TextWatcher {

    private final int KEY_FETCH_CUSTOMER_CONTACT = 1;
    private final int KEY_FETCH_MODE_PAYMENT = 2;
    private final int KEY_FETCH_TAX = 3;
    private final int KEY_INSERT_PURCHASE = 4;
    private final int ADD_TAX=5;

    private String UserId, UserLogin, PassWord;
    private SharedPreferences prfs;
    private AutoCompleteTextView tvPurchaseAddContact;
    private ArrayList<String> contactNameList;
    private TextView tvPurchaseAddPurchaseDate, tvPurchaseAddTotalPrice, tvPurchaseAddItem,
            tvPurchaseAddBalance, tvPurchaseAddSelectTax, tvPurchaseAddActualAmount, tvPurchaseAddBasePrice,
            tvPurchaseAddInclusiveData, tvPurchaseAddTotalPrice2, tvPurchaseAddTotalTax,
            btnPurchaseAdd, imageBiller, imagePurchaseAddInvoice;

    private TwoWayView PurchaseAddTaxItems;
    private TableRow trPurchaseAddExcludeTax, trPurchaseAddIncludeTax;
    private Switch switchPurchaseAddTax, switchPurchaseAddBiller;
    private LinearLayout tvPurchaseAddDateLayout, layoutPurchaseAddTotalBill, layoutPurchaseAddFirst, layoutPurchaseAddThird;
    private RelativeLayout layoutPurchaseAddSecond;
    private DatePickerDialog dialogDate;
    private Spinner spinnerPurchaseAddSelectItem, spinnerPurchaseAddModePayment;
    private PurchaseInsertModel purchaseInsertModel;

    private ArrayList<FetchCustomer> fetchCustomersContact;
    private ArrayList<FetchProduct> fetchProducts;
    private ArrayList<FetchProduct> finalProducts;
    private ArrayList<FetchBrand> fetchModeOfPayment;
    private PurchasedItemsList purchasedItemsLists;
    private ArrayList<PurchasedItem> purchasedItems;
    private ArrayList<String> TaxNameList = new ArrayList<String>();
    private ArrayList<Long> TaxIdList = new ArrayList<Long>();
    private ArrayList<String> Taxdata = new ArrayList<String>();
    private HashMap<Long, FetchProduct> hashCartProducts;

    private ArrayList<Integer> selectedTaxPos;

    private PosModeOfPaymentDetailScreen modeOfPaymentDetailScreen;
    private EditText etPurchaseAddPaidAmt, etPurchaseAddBillingAmt, etPurchaseAddReceipt, etPurchaseAddInvoice,
            etPurchaseAddPoNum;

    private final int REQUEST_ADD_ITEM = 1;
    private final int REQUEST_EDIT_ITEM = 2;
    private Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    private ArrayList<FetchProduct> cartProducts;
    private Double total;
    private String modeOfPaymentVal;
    private int modeNo = 0;
    private boolean isTaxSettingEnable, isInclusive;
    private MoonIcon mIcon;
    private Double TaxPercent = 0.0d, TotalTax = 0.00d, TotalPrice = 0.00d,
            other_total = 0.00d, discount_total = 0.00d, BasePrice;
    private String Orderprice = "", finalAmount = "", totalPriceVal = "",
            paidvalue = "", BalanceValue = "", SelectedTax = "", selectedItem = "", biller = "",
            receiptNum = "", invoiceNum = "", poNum = "", datePurchase = "";

    private TextView roundoffpricetxt,tvroundoffprice,totalpricetxt;
    private String companyName="company",contactName="individual";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_add);
        prfs = getSharedPreferences("profileData",
                Context.MODE_PRIVATE);
        UserId = Utils.getUserId(this);
        UserLogin = prfs.getString("user_login", "");
        PassWord = prfs.getString("Pwd", "");
        actionBarSettingWithBack(this, getSupportActionBar(), "Add Purchase");
        fetchContactForPOS();
        fetchModeOfPayment();
        fetchTaxList();
        setUI();
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

    private void setUI() {
        mIcon = new MoonIcon(this);
        fetchProducts = new ArrayList<>();
        purchaseInsertModel = new PurchaseInsertModel("", fetchProducts);

        RelativeLayout page_main_layout = (RelativeLayout) findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));

        tvPurchaseAddDateLayout = (LinearLayout) findViewById(R.id.tvPurchaseAddDateLayout);
        layoutPurchaseAddTotalBill = (LinearLayout) findViewById(R.id.layoutPurchaseAddTotalBill);
        layoutPurchaseAddFirst = (LinearLayout) findViewById(R.id.layoutPurchaseAddFirst);
        layoutPurchaseAddSecond = (RelativeLayout) findViewById(R.id.layoutPurchaseAddSecond);
        layoutPurchaseAddThird = (LinearLayout) findViewById(R.id.layoutPurchaseAddThird);

        layoutPurchaseAddFirst.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));
        layoutPurchaseAddSecond.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));
        layoutPurchaseAddThird.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        tvPurchaseAddContact = (AutoCompleteTextView) findViewById(R.id.tvPurchaseAddContact);
        tvPurchaseAddPurchaseDate = (TextView) findViewById(R.id.tvPurchaseAddPurchaseDate);
        tvPurchaseAddTotalPrice = (TextView) findViewById(R.id.tvPurchaseAddTotalPrice);
        tvPurchaseAddItem = (TextView) findViewById(R.id.tvPurchaseAddItem);
        tvPurchaseAddBalance = (TextView) findViewById(R.id.tvPurchaseAddBalance);
        tvPurchaseAddSelectTax = (TextView) findViewById(R.id.tvPurchaseAddSelectTax);
        tvPurchaseAddActualAmount = (TextView) findViewById(R.id.tvPurchaseAddActualAmount);
        tvPurchaseAddBasePrice = (TextView) findViewById(R.id.tvPurchaseAddBasePrice);
        tvPurchaseAddInclusiveData = (TextView) findViewById(R.id.tvPurchaseAddInclusiveData);
        tvPurchaseAddTotalTax = (TextView) findViewById(R.id.tvPurchaseAddTotalTax);
        totalpricetxt= (TextView) findViewById(R.id.totalpricetxt);
        mIcon.setTextfont(totalpricetxt);

        imageBiller = (TextView) findViewById(R.id.imageBiller);
        imagePurchaseAddInvoice = (TextView) findViewById(R.id.imagePurchaseAddInvoice);

        etPurchaseAddPaidAmt = (EditText) findViewById(R.id.etPurchaseAddPaidAmt);
        etPurchaseAddBillingAmt = (EditText) findViewById(R.id.etPurchaseAddBillingAmt);
        etPurchaseAddReceipt = (EditText) findViewById(R.id.etPurchaseAddReceipt);
        etPurchaseAddInvoice = (EditText) findViewById(R.id.etPurchaseAddInvoice);
        etPurchaseAddPoNum = (EditText) findViewById(R.id.etPurchaseAddPoNum);
        switchPurchaseAddTax = (Switch) findViewById(R.id.switchPurchaseAddTax);
        switchPurchaseAddBiller = (Switch) findViewById(R.id.switchPurchaseAddBiller);
        tvPurchaseAddTotalPrice2 = (TextView) findViewById(R.id.tvPurchaseAddTotalPrice2);
        btnPurchaseAdd = (TextView) findViewById(R.id.btnPurchaseAdd);


        spinnerPurchaseAddSelectItem = (Spinner) findViewById(R.id.spinnerPurchaseAddSelectItem);
        spinnerPurchaseAddModePayment = (Spinner) findViewById(R.id.spinnerPurchaseAddModePayment);
        trPurchaseAddExcludeTax = (TableRow) findViewById(R.id.trPurchaseAddExcludeTax);
        trPurchaseAddIncludeTax = (TableRow) findViewById(R.id.trPurchaseAddIncludeTax);
        PurchaseAddTaxItems = (TwoWayView) findViewById(R.id.PurchaseAddTaxItems);


        roundoffpricetxt = (TextView) findViewById(R.id.roundoffpricetxt);
        tvroundoffprice = (TextView) findViewById(R.id.tvroundoffprice);

        setRobotoThinFont(roundoffpricetxt, this);
        setRobotoThinFont(tvroundoffprice, this);

        setRobotoThinFont(tvPurchaseAddContact, this);
        setRobotoThinFont(tvPurchaseAddPurchaseDate, this);
        setRobotoThinFont(tvPurchaseAddTotalPrice, this);
        setRobotoThinFont(tvPurchaseAddItem, this);
        setRobotoThinFont(tvPurchaseAddBalance, this);
        setRobotoThinFont(tvPurchaseAddSelectTax, this);
        setRobotoThinFont(tvPurchaseAddActualAmount, this);
        setRobotoThinFont(tvPurchaseAddBasePrice, this);
        setRobotoThinFont(tvPurchaseAddInclusiveData, this);
        setRobotoThinFont(tvPurchaseAddTotalTax, this);
        setRobotoThinFont(etPurchaseAddPaidAmt, this);
        setRobotoThinFont(etPurchaseAddBillingAmt, this);
        setRobotoThinFont(etPurchaseAddReceipt, this);
        setRobotoThinFont(etPurchaseAddInvoice, this);
        setRobotoThinFont(etPurchaseAddPoNum, this);
        setRobotoThinFontSWitch(switchPurchaseAddTax, this);
        setRobotoThinFontSWitch(switchPurchaseAddBiller, this);
        setRobotoThinFont(tvPurchaseAddTotalPrice2, this);
        setRobotoThinFont(btnPurchaseAdd, this);

        roundoffpricetxt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvroundoffprice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        totalpricetxt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchaseAddContact.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchaseAddPurchaseDate.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchaseAddTotalPrice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchaseAddItem.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchaseAddBalance.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchaseAddSelectTax.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchaseAddActualAmount.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchaseAddBasePrice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchaseAddInclusiveData.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchaseAddTotalTax.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etPurchaseAddPaidAmt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etPurchaseAddBillingAmt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etPurchaseAddReceipt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etPurchaseAddInvoice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etPurchaseAddPoNum.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        switchPurchaseAddTax.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        switchPurchaseAddBiller.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchaseAddTotalPrice2.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        btnPurchaseAdd.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        btnPurchaseAdd.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        mIcon.setTextfont(imageBiller);
        mIcon.setTextfont(imagePurchaseAddInvoice);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(c.getTime());
        tvPurchaseAddPurchaseDate.setText(formattedDate);

        spinnerPurchaseAddSelectItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String typeitem = parent.getItemAtPosition(position).toString();
                if (position > 0) {
                    purchaseInsertModel.setType(typeitem);
                    tvPurchaseAddItem.setText(typeitem);
                    sendToCart(typeitem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvPurchaseAddItem.setOnClickListener(this);
        tvPurchaseAddDateLayout.setOnClickListener(this);
        tvPurchaseAddSelectTax.setOnClickListener(this);

        switchPurchaseAddTax.setOnCheckedChangeListener(this);
        switchPurchaseAddBiller.setOnCheckedChangeListener(this);
        etPurchaseAddPaidAmt.addTextChangedListener(this);
        btnPurchaseAdd.setOnClickListener(this);
    }

    private void sendToCart(String typeitem) {
        Intent intent = new Intent(this, PurchaseAddItemActivity.class);
        intent.putExtra("PurchaseInsert", purchaseInsertModel);
        startActivityForResult(intent, REQUEST_ADD_ITEM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_ADD_ITEM || requestCode == REQUEST_EDIT_ITEM) {
//                hashCartProducts = (HashMap<Long, FetchProduct>) data.getSerializableExtra("HashCartProducts");
                cartProducts = ((PurchaseProductModel) data.getParcelableExtra("CartProducts")).getFetchProduct();
                finalProducts = data.getParcelableArrayListExtra("FinalProducts");
                if (cartProducts != null) {
                    tvPurchaseAddItem.setVisibility(View.VISIBLE);
                    spinnerPurchaseAddSelectItem.setVisibility(View.GONE);
                    Iterator iterator = cartProducts.iterator();
                    total = 0.0;
                    double tprice = total;
                    int totalq = 0;

                    purchasedItems = new ArrayList<PurchasedItem>();
                    while (iterator.hasNext()) {
                        FetchProduct fetchProduct = (FetchProduct) iterator.next();
                        total = total + (Double.parseDouble(fetchProduct.getmQuantity()) * Double.parseDouble(fetchProduct.getmUnitPrice()));
                        totalq += Double.parseDouble(fetchProduct.getmQuantity());
                        purchasedItems.add(new PurchasedItem(String.valueOf(fetchProduct.getProductID()).trim(), fetchProduct.getProductName(), fetchProduct.getmQuantity(), fetchProduct.getmUnitPrice()));
                    }
                    purchasedItemsLists = new PurchasedItemsList(purchasedItems);
                    Gson gson = new Gson();
                    selectedItem = gson.toJson(purchasedItemsLists, PurchasedItemsList.class);
                    layoutPurchaseAddTotalBill.setVisibility(View.VISIBLE);
                    tprice = total;
                    try {

                        if (tprice > 0) {
                            totalPriceVal = Utils.doubleToString(tprice);
                            Log.i("totalPriceVal", "--" + totalPriceVal);
                            finalAmount = totalPriceVal;

                            tvPurchaseAddTotalPrice.setText(String.valueOf(total));
                            tvPurchaseAddTotalPrice.setVisibility(View.VISIBLE);
                            etPurchaseAddBillingAmt.setText(totalPriceVal);
                            // Orderprice = billingAmtEdit.getText().toString().trim();

                            layoutPurchaseAddSecond.setVisibility(View.VISIBLE);
                            layoutPurchaseAddTotalBill.setVisibility(View.VISIBLE);
                            calculatePrice();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if(requestCode==ADD_TAX)
        {
            ModelManager.getInstance().setTaxModel(data.getStringExtra("response"));
        }
    }

    private void fetchModeOfPayment() {
        if (Utils.isDeviceOnline(this)) {
            try {
                Map map = new HashMap<>();
                map.put("Page", "FetchModeOFPayMent");
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_MODE_PAYMENT, map);
            } catch (Exception e) {
                Toast.makeText(this, getApplicationContext().getString(R.string.volleyError),
                        Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "No internet connection",
                    Toast.LENGTH_SHORT).show();
    }

    private void fetchContactForPOS() {
        if (Utils.isDeviceOnline(this)) {
            try {
                Map map = new HashMap<>();
                map.put("Page", "FetchContactForPOS");
                map.put("userID", UserId);
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_CUSTOMER_CONTACT, map);
            } catch (Exception e) {
                Toast.makeText(this, getApplicationContext().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    private void fetchTaxList() {
        if (Utils.isDeviceOnline(this)) {
            Map map = new HashMap<>();
            map.put("Page", "FetchTax");
            map.put("IsActive", "A");
            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);
            try {
                new RequestManager().sendPostRequest(this, KEY_FETCH_TAX, map);
            } catch (Exception e) {
                // TODO: handle exception
                showToast(getResources().getString(R.string.volleyError));
            }
        } else {
            showToast("Internet connection not found");
        }
    }

    private void addPurchase() {
        if (Utils.isDeviceOnline(this)) {
            try {
                String modeOfPayId = "";
                Map<String, String> map = new HashMap<String, String>();

                String contactId = "";
                if (contactNameList != null
                        && contactNameList.size() > 0
                        && fetchCustomersContact != null
                        && fetchCustomersContact.size() > 0) {
                    int index = contactNameList.indexOf(tvPurchaseAddContact
                            .getText().toString());
                    if(index == -1)
                    {
                        tvPurchaseAddContact.setError("Select a contact");
                        return;
                    }
                    contactId = fetchCustomersContact.get(index).getContactID().toString();
                }

                if (etPurchaseAddReceipt.getText() != null)
                    receiptNum = etPurchaseAddReceipt.getText().toString();
                if (etPurchaseAddInvoice.getText() != null)
                    invoiceNum = etPurchaseAddInvoice.getText().toString();
                if (etPurchaseAddPoNum.getText() != null)
                    poNum = etPurchaseAddPoNum.getText().toString();
                if (tvPurchaseAddPurchaseDate.getText() != null)
                    datePurchase = tvPurchaseAddPurchaseDate.getText().toString();

                map.put("Page", "AddPurchase");
                map.put("contactID", contactId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                map.put("UserID", UserId);
                map.put("Biller", biller);
                map.put("ReceiptNo", receiptNum);
                map.put("InvoiceNO", invoiceNum);
                map.put("PO_NO", poNum);
                map.put("BaseAmount", totalPriceVal);//finalAmount=""+totalPriceVal;
                map.put("TaxAmount", "" + TotalTax);
                map.put("IsTaxIncluded", "" + isInclusive);
                map.put("PurchaseDate", datePurchase);
                map.put("TaxApplied", SelectedTax);

                paidvalue = etPurchaseAddPaidAmt.getText().toString();
                BalanceValue = tvPurchaseAddBalance.getText().toString();

                if (paidvalue == null || paidvalue.equalsIgnoreCase("")) {
                    paidvalue = "0";
                    BalanceValue = finalAmount;
                }

                map.put("PaidAmount", paidvalue);
                map.put("BalanceAmount", BalanceValue);

                map.put("BillAmount", tvroundoffprice.getText().toString());

               /* String pstatus = getPaidStatus(BalanceValue, finalAmount);
                map.put("Status", pstatus);*/

                if (fetchModeOfPayment != null
                        && fetchModeOfPayment.size() > 0) {
                    try {
                        modeOfPayId = String.valueOf(spinnerPurchaseAddModePayment.getSelectedItemId());
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

                Log.i("postmap", "post:" + map.toString());
                new RequestManager().sendPostRequest(this, KEY_INSERT_PURCHASE, map);
            } catch (Exception e) {
                Log.i("Error", "--" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showToast("Internet connection not found");
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (response != null && response.length() > 0) {
            Gson gson = new Gson();
            if (requestId == KEY_INSERT_PURCHASE) {
                GetPurchaseListModel getPurchaseListModel = gson.fromJson(response, GetPurchaseListModel.class);
                Intent intent = new Intent();
                intent.putExtra("GetPurchaseListModel", getPurchaseListModel);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (requestId == KEY_FETCH_CUSTOMER_CONTACT) {
                ContactModel fetchContactModel = gson.fromJson(response, ContactModel.class);
                fetchCustomersContact = fetchContactModel.getFetchCustomer();
                contactNameList = new ArrayList<String>();
                if (fetchContactModel != null
                        && fetchContactModel.getFetchCustomer() != null
                        && fetchContactModel.getFetchCustomer().size() > 0) {
                    final ArrayList<FetchCustomer> tempVendor = fetchContactModel.getFetchCustomer();
                    for (int k = 0; k < fetchContactModel.getFetchCustomer().size(); k++) {
                        Log.i("contact", "pos-" + k);
                        String contact_name = fetchContactModel.getFetchCustomer()
                                .get(k).getContactName();
                        if(contact_name!=null&& contact_name.length()>3)
                            contact_name=contact_name.substring(3);

                        String company_name=fetchContactModel.getFetchCustomer()
                                .get(k).getmCompanyName();;
                        String concat_string="";
                        if(company_name!=null) concat_string=contact_name+" - "+company_name;
                        else concat_string=contact_name;
                        contactNameList.add(concat_string);
                        //contactNameList.add(contact_name.substring(3));
                    }
                    CustomStyle.MySpinnerAdapter adapter = new CustomStyle.MySpinnerAdapter(
                            this, android.R.layout.simple_list_item_1,
                            contactNameList);
                    tvPurchaseAddContact.setThreshold(1);
                    tvPurchaseAddContact.setAdapter(adapter);

                    tvPurchaseAddContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String str=tvPurchaseAddContact.getText().toString();
                            String[] contactName_ar=str.split(" ");
                            contactName=contactName_ar[1];
                            int array_pos=contactNameList.indexOf(str);
                            switchPurchaseAddBiller.setEnabled(true);
                            companyName=tempVendor.get(array_pos).getmCompanyName();
                            if (tempVendor.get(array_pos).getmCompanyName() != null) {
                                switchPurchaseAddBiller.setChecked(true);
                                biller=companyName;
                            } else {
                                switchPurchaseAddBiller.setChecked(false);
                                switchPurchaseAddBiller.setEnabled(false);
                                biller=contactName;
                            }

                            switchPurchaseAddBiller.setText("Bill to "+biller);
                        }
                    });

                } else {
                    CustomStyle.MySpinnerAdapter adapter = new CustomStyle.MySpinnerAdapter(
                            this, android.R.layout.simple_list_item_1,
                            contactNameList);
                    tvPurchaseAddContact.setAdapter(adapter);
                }
            } else if (requestId == KEY_FETCH_MODE_PAYMENT) {

                ModeOfPayment modeOfPayment = gson.fromJson(response, ModeOfPayment.class);
                fetchModeOfPayment = modeOfPayment.getFetchBrand();
                ArrayList<String> list = new ArrayList<>();
                list.add("Select mode of payment ");
                Iterator iterator = fetchModeOfPayment.iterator();
                while (iterator.hasNext())
                    list.add(((FetchBrand) iterator.next()).getMode());
                Utils.MySpinnerAdapter adapter = new Utils.MySpinnerAdapter(this,
                        android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPurchaseAddModePayment.setAdapter(adapter);
                spinnerPurchaseAddModePayment.setOnItemSelectedListener(itemSelectedListener);

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
                    switchPurchaseAddTax.setChecked(isTaxSettingEnable);

                    list.add("Add New Tax");
                } else {

                }
            }
        } else
            Toast.makeText(this, getApplicationContext().getString(R.string.volleyError),
                    Toast.LENGTH_SHORT).show();
    }

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Spinner spinModePayment = (Spinner) parent;
            if (spinModePayment.getId() == R.id.spinnerPurchaseAddModePayment) {
                modeOfPaymentVal = (String) spinModePayment.getSelectedItem();
                modeNo = position;
                if (position > 0 && !modeOfPaymentVal.equalsIgnoreCase("Cash")) {
                    modeOfPaymentDetailScreen = new PosModeOfPaymentDetailScreen(PurchaseAddActivity.this);
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
        showToast(getResources().getString(R.string.volleyError));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPurchaseAddDateLayout:
                dialogDate = new DatePickerDialog(this, this, year, month, day);
                dialogDate.getDatePicker().setMaxDate((new Date().getTime()));//-(5097600000L)
                dialogDate.show();
                break;
            case R.id.tvPurchaseAddSelectTax:
                setTaxScreen(ModelManager.getInstance().getTaxModel()
                        .getFetchTaxList());
                break;
            case R.id.btnPurchaseAdd:
                addPurchase();
                break;
            case R.id.tvPurchaseAddItem:
                Intent intent = new Intent(this, PurchaseEditItemActivity.class);
                intent.putExtra("PurchaseInsert", purchaseInsertModel);
                intent.putParcelableArrayListExtra("CartProducts", cartProducts);
                intent.putParcelableArrayListExtra("FinalProducts", finalProducts);
                startActivityForResult(intent, REQUEST_EDIT_ITEM);
                break;
        }
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
        mIcon.setTextfont(donetxt);
        add_tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(PurchaseAddActivity.this, POSTaxAddUpdateScreen.class);
                intent.putExtra("screen_name", "Add Tax");
                intent.putExtra("purchasepage", true);
               /* intent.putExtra("productList", productList);
                intent.putExtra("hmap", hmap);
                intent.putExtra("ContactName", ContactName);
                intent.putExtra("Type", Type);*/
                startActivityForResult(intent, ADD_TAX);
            }
        });

        donetxt.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(PurchaseAddActivity.this, "Tax cannot be greater then 100 % ", Toast.LENGTH_SHORT).show();
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
            calculatePrice();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculatePrice() {
        Orderprice = etPurchaseAddBillingAmt.getText().toString().trim();
        Log.i("calculatePrice", "Orderprice-" + Orderprice);

        BasePrice = 0.00d;
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);

        if (Orderprice.length() > 0) {
            Double tprice = 0.00d;

            BasePrice = Double.parseDouble(Orderprice);
            tprice = BasePrice;
            layoutPurchaseAddTotalBill.setVisibility(View.VISIBLE);
            if (TaxPercent > 0 && switchPurchaseAddTax.isChecked()) {
                BasePrice = ((BasePrice) * 100) / (100 + TaxPercent);

                TotalTax = (BasePrice) * TaxPercent / 100;
                TotalPrice = BasePrice + TotalTax + other_total;
            } else {
                TotalTax = (BasePrice) * TaxPercent / 100;
                TotalPrice = BasePrice + TotalTax + other_total;
            }


            Log.i("calculatePrice", "After-BasePrice-" + BasePrice);


            finalAmount = Utils.doubleToString(TotalPrice);
            tvPurchaseAddActualAmount.setText(Orderprice);
            tvPurchaseAddBasePrice.setText(Utils.doubleToString(tprice));

            if (TotalTax > 0) {
                if (isInclusive) {
                    trPurchaseAddIncludeTax.setVisibility(View.VISIBLE);
                    tvPurchaseAddInclusiveData.setVisibility(View.VISIBLE);
                    tvPurchaseAddInclusiveData.setText("[" + Utils.doubleToString(BasePrice) + "(p) + " + Utils.doubleToString(TotalTax) + "(t)]");
                } else {
                    trPurchaseAddIncludeTax.setVisibility(View.GONE);
                    trPurchaseAddExcludeTax.setVisibility(View.VISIBLE);
                    tvPurchaseAddTotalTax.setText(Utils.doubleToString(TotalTax));
                }


            } else {
                trPurchaseAddIncludeTax.setVisibility(View.VISIBLE);
                tvPurchaseAddInclusiveData.setVisibility(View.GONE);
                trPurchaseAddExcludeTax.setVisibility(View.GONE);
            }
            tvPurchaseAddTotalPrice2.setText(finalAmount);
            finalAmount=""+Math.round(TotalPrice);
            tvroundoffprice.setText(""+Math.round(TotalPrice));

        } else {
            //total_bill_layout.setVisibility(View.VISIBLE);
        }


        PurchaseTwoWayViewAdapter staxadapter = new PurchaseTwoWayViewAdapter(PurchaseAddActivity.this, Taxdata, false);
        PurchaseAddTaxItems.setAdapter(staxadapter);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        tvPurchaseAddPurchaseDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteItem(int id) {
        TaxNameList.remove(id);
        TaxIdList.remove(id);
        selectedTax();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchPurchaseAddBiller:
                if (isChecked) {
                    switchPurchaseAddBiller.setChecked(true);
                    switchPurchaseAddBiller.setText("Bill to "+companyName);
                   // biller = "1";
                } else {
                    switchPurchaseAddBiller.setChecked(false);
                    switchPurchaseAddBiller.setText("Bill to "+contactName);
                   // biller = "0";
                }
                break;
            case R.id.switchPurchaseAddTax:
                if (isChecked) {
                    etPurchaseAddBillingAmt.setHint("Price Including Tax");
                    isInclusive = true;
                    tvPurchaseAddInclusiveData.setVisibility(View.VISIBLE);
                    trPurchaseAddExcludeTax.setVisibility(View.GONE);
                    switchPurchaseAddTax.setText("Included");
                } else {
                    etPurchaseAddBillingAmt.setHint("Price Excluding Tax");
                    tvPurchaseAddInclusiveData.setVisibility(View.GONE);
                    trPurchaseAddExcludeTax.setVisibility(View.VISIBLE);
                    isInclusive = false;
                    switchPurchaseAddTax.setText("Excluded");
                }
                calculatePrice();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvPurchaseAddBalance.setText("");
    }

    @Override
    public void afterTextChanged(Editable s) {
        String paidAmt = etPurchaseAddPaidAmt.getText().toString();
        try {
            if (paidAmt != null && !paidAmt.equalsIgnoreCase("")) {
                Double amt = Double.parseDouble(paidAmt);

                if (etPurchaseAddBillingAmt.getText() != null
                        && !etPurchaseAddBillingAmt.getText().toString().trim()
                        .equalsIgnoreCase("")) {

                    Double billingAmt = Double.parseDouble(finalAmount);
                    if (amt > billingAmt) {
                        etPurchaseAddPaidAmt.setText("");
                        etPurchaseAddPaidAmt.setHint("Paid amount");
                    } else {
                        Double bal = billingAmt - amt;
                        DecimalFormat f = new DecimalFormat("##.00");
                        tvPurchaseAddBalance.setText("" + f.format(bal));
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}