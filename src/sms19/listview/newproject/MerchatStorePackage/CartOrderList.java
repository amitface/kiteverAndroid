package sms19.listview.newproject.MerchatStorePackage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.fragment.PosComparators.CSVUtils;
import com.kitever.pos.fragment.PosComparators.OrderComparator;
import sms19.listview.newproject.MerchatStorePackage.Adapter.CartOrderDetailAdapter;

import com.kitever.pos.model.data.FetchOrder;
import com.kitever.pos.model.data.FetchOrderForId;
import com.kitever.pos.model.data.OrderDetailModelData;
import com.kitever.pos.model.data.OrderList;
import com.kitever.utils.DateHelper;
import com.kitever.utils.SpinnerReselect;
import com.kitever.utils.TotalRows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.MerchatStorePackage.Adapter.CartOrderAdapter;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class CartOrderList extends AppCompatActivity implements NetworkManager,TotalRows,View.OnClickListener,DatePickerDialog.OnDateSetListener {



    private final int KEY_FETCH_ORDER_DETAILS = 1;
    private final int KEY_FETCH_ORDER_DETAILS_BY_ID = 2, KEY_SEND_SMS_MAIL = 3;

    private ListView orderListView, orderDetailListView;
    private ArrayList<OrderDetailModelData> orderList = null;
    private OrderDetailModelData clickObject = null;
    private ArrayList<FetchOrder> arrayList = null;
    private CartOrderAdapter orderAdapter;
    private CartOrderDetailAdapter orderDetailAdapter;
    private TextView profile_name, profile_number, discountTotal, total,
            orderCode, contactName, totalRows, dateRange, noRecord;
    private int positionClick = -1;
    private Boolean statusMail = false, statusSms = false, statusPrint = false,
            statusStartDate = false, statusEndDate = false;
    private Button ok;
    private LinearLayout smsLayout, mailLayout, printLayout;
    private ImageView smsImage, mailImage, printImage, dateImage;
    private Spinner selectTypeSpinner;
    private SpinnerReselect dateSearch, paymentSearch;
    private EditText searchBox;
    private String name, number;
    private DatePickerDialog dialogDate;

    private long startDatel, endDatel, dateInMillis;
    private SimpleDateFormat format;
    private Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    private ImageView pdfview,order_export;

    private LinearLayout pos_order_dialog_layout,pos_order_dialog_layout_price;
    private TextView totaltaxtxt, basePrice, totaltax, otherPrice;
    private RelativeLayout emptyView;
    private ImageView search_icon;
    private boolean IssearchBoxOpen=false;
    private ProgressBar progressBar;
    private int orderClick =-1;
    private TextView dateTitle,orderTitle,amountTitle,paidTitle,round_off_amt,statusTitle;
    private boolean dateAsc=false,orderAsc=false,CustomerAsc=false,amountAsc=false,paidAsc=false;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order_list);
        actionBarSettingWithBack(this,getSupportActionBar(),"Orders");
        context=this;
        setScreen();
        fetchOrderList();
    }

    private void setScreen() {

        selectTypeSpinner = (Spinner) findViewById(R.id.select_type_spinner);
        searchBox = (EditText) findViewById(R.id.edit_search);
        dateInMillis = c.getTimeInMillis();
        ImageView advanceSearch = (ImageView) findViewById(R.id.advance_search);
        dateSearch = (SpinnerReselect) findViewById(R.id.date_order_search);
        paymentSearch = (SpinnerReselect) findViewById(R.id.payment_order_search);
        totalRows = (TextView) findViewById(R.id.orders_total_rows);
        emptyView = (RelativeLayout) findViewById(R.id.order_emptyElement);
        noRecord = (TextView) findViewById(R.id.no_record);
        MoonIcon mIcon = new MoonIcon(this);
        mIcon.setTextfont(noRecord);
        dateImage = (ImageView) findViewById(R.id.pos_order_image_date);
        dateRange = (TextView) findViewById(R.id.pos_order_date_range);
        orderListView = (ListView) findViewById(R.id.order_list_view);

        noRecord = (TextView) findViewById(R.id.no_record);
        progressBar = (ProgressBar) findViewById(R.id.marker_progress);
        progressBar.setVisibility(View.GONE);

        orderListView.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));
        dateTitle = (TextView) findViewById(R.id.dateTitle);
        orderTitle = (TextView) findViewById(R.id.orderTitle);
       // customerTitle = (TextView) findViewById(R.id.customerTitle);
        amountTitle = (TextView) findViewById(R.id.amountTitle);
        paidTitle = (TextView) findViewById(R.id.paidTitle);
        statusTitle= (TextView) findViewById(R.id.statusTitle);
        order_export= (ImageView) findViewById(R.id.order_export);

        dateTitle.setOnClickListener(this);
        orderTitle.setOnClickListener(this);
     //   customerTitle.setOnClickListener(this);
        amountTitle.setOnClickListener(this);
        paidTitle.setOnClickListener(this);
        order_export.setOnClickListener(this);

        mIcon.setTextfont(dateTitle);
        mIcon.setTextfont(orderTitle);
    //    mIcon.setTextfont(customerTitle);
        mIcon.setTextfont(amountTitle);
        mIcon.setTextfont(paidTitle);

        mIcon.setTextfont(noRecord);


        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("Last 50 Orders");
        typeList.add("Order number");
        typeList.add("Contact");
        typeList.add("Amount >=");
        typeList.add("Amount <=");
        typeList.add("Payment Status");
        typeList.add("Date");


        RelativeLayout page_main_layout=(RelativeLayout)findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        totalRows.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        dateRange.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        searchBox.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        setRobotoThinFont(totalRows,context);
        setRobotoThinFont(dateRange,context);
        setRobotoThinFont(searchBox,context);

        dateTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        orderTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
      //  customerTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        amountTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        paidTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        statusTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(context,
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTypeSpinner.setAdapter(typeAdapter);
        selectTypeSpinner.setOnItemSelectedListener(itemSelectedListener);

        search_icon= (ImageView) findViewById(R.id.search_icon);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IssearchBoxOpen)
                {
                    searchBox.setVisibility(View.GONE);
                    IssearchBoxOpen=false;
                }
                else {
                    searchBox.setVisibility(View.VISIBLE);
                    IssearchBoxOpen=true;
                }
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                if (orderAdapter != null) {
                    orderAdapter.getFilter().filter(cs);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        orderListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        positionClick = position;
                        clickObject = orderAdapter.getClickObject(position);
                        fetchOrderDetailById(orderAdapter.getorderId(position));
                        orderClick = position;
                        name = orderAdapter.getName(position);
                        number = orderAdapter.getNumber(position);
                        statusMail = orderAdapter.getMailStatus(position);

                    }
                });

        dateSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                String pattern = "d-MMM, yyyy";
                format = new SimpleDateFormat(pattern);

                if (position == 1) {
                    orderAdapter.dateRangeFilter(position);
                    dateRange.setText("" + DateHelper.getTodayDate());

                } else if (position == 2) {
                    orderAdapter.dateRangeFilter(position);
                    dateRange.setText(""
                            + DateHelper.getYestardayDate(DateHelper
                            .getDateInmillis()));
                } else if (position == 3) {
                    orderAdapter.dateRangeFilter(position);
                    dateRange.setText(DateHelper.getFirstDayOfWeek()
                            + " - "
                            + format.format(new Date(DateHelper
                            .getDateInmillis())));
                } else if (position == 4) {
                    orderAdapter.dateRangeFilter(position);
                    dateRange.setText(DateHelper.getFirstDayOfMonth()
                            + " - "
                            + format.format(new Date(DateHelper
                            .getDateInmillis())));
                } else if (position == 5) {
                    orderAdapter.dateRangeFilter(position);

                    dateRange.setText(DateHelper.getLastMonth());
                } else if (position == 6) {
                   /* statusStartDate = true;
                    dialogDate = new DatePickerDialog(context,year,month,day);
                    if (statusStartDate == true) {
                        dialogDate.getDatePicker().setMaxDate(
                                (new Date().getTime()));// -(5097600000L)
                        dialogDate.setTitle("Select Start Date");
                        dialogDate.show();
                    }*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        paymentSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    orderAdapter.setPaidFiltered();
                } else if (position == 2) {
                    orderAdapter.setParitalllyPaidFiltered();
                } else if (position == 3) {
                    orderAdapter.setUnPaidFiltered();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //searchBox.setEnabled(false);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading()
    {
        progressBar.setVisibility(View.GONE);
    }




    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // TODO Auto-generated method stub

        // set selected date into textview
        // orderAdapter.dateFilter(startDatel, startDatel+5184000000L);

        if (statusStartDate == true) {
            final int yearTemp = year, monthTemp = monthOfYear, dayTemp = dayOfMonth;
            startDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            statusEndDate = true;
            statusStartDate = false;
            dialogDate = new DatePickerDialog(context,
                    this, year, month, day);
            c= Calendar.getInstance();
            Long maxdate;
            dateInMillis = c.getTimeInMillis();
            maxdate = (startDatel + (5184000000L)) > dateInMillis ? dateInMillis
                    : (startDatel + (5184000000L));
            Log.i("Current date ",""+DateHelper.getDate(year,month,day));
            Log.i("maxdate ",""+maxdate);
            Log.i("startdate ",""+startDatel);

            dialogDate.getDatePicker().setMaxDate(maxdate);// startDatel+(5184000000L)
            dialogDate.getDatePicker().setMinDate(startDatel);
            dialogDate.setTitle("End Date (max 60 days)");
            dialogDate.setButton(DialogInterface.BUTTON_NEUTRAL, "Back",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Your code
                            statusStartDate = true;

                          /*  dialogDate = new DatePickerDialog(
                                    context,this,
                                    yearTemp, monthTemp, dayTemp);
                            if (statusStartDate == true) {
                                dialogDate.getDatePicker().setMaxDate(
                                        (new Date().getTime()));// -(5097600000L)
                                dialogDate.setTitle("Select Start Date");
                                dialogDate.show();
                            }*/
                        }
                    });
            dialogDate.show();
        } else if (statusEndDate == true) {
            endDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            orderAdapter.dateFilter(startDatel, endDatel + (86400000L));
            String pattern = "d-MMM, yyyy";

            format = new SimpleDateFormat(pattern);
            dateRange.setText(format.format(new Date(startDatel)) + "  -  "
                    + format.format(new Date(endDatel)));
        }

        if (statusStartDate == true) {
            startDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            final int yearTemp = year, monthTemp = monthOfYear, dayTemp = dayOfMonth;
            statusEndDate = true;
            statusStartDate = false;
            Long maxdate;
            maxdate = (startDatel + (5184000000L)) > dateInMillis ? dateInMillis
                    : (startDatel + (5184000000L));

            dialogDate = new DatePickerDialog(context,
                    this, this.year, month, day);
            dialogDate.getDatePicker().setMaxDate(maxdate);// startDatel+(5184000000L)
            dialogDate.getDatePicker().setMinDate(startDatel);
            dialogDate.setTitle("End Date (max 60 days)");
            dialogDate.setButton(DialogInterface.BUTTON_NEUTRAL, "Back",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Your code
                            statusStartDate = true;
/*
                            dialogDate = new DatePickerDialog(
                                    context, this,
                                    yearTemp, monthTemp, dayTemp);
                            if (statusStartDate == true) {
                                dialogDate.getDatePicker().setMaxDate(
                                        (new Date().getTime() - (5097600000L)));
                                dialogDate.setTitle("Select Start Date");
                                dialogDate.show();
                            }*/
                        }
                    });
            dialogDate.show();
        } else if (statusEndDate == true) {
            endDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            orderAdapter.dateFilter(startDatel, endDatel + (86400000L));
            String pattern = "d-MMM, yyyy";

            format = new SimpleDateFormat(pattern);
            dateRange.setText(format.format(new Date(startDatel)) + "  -  "
                    + format.format(new Date(endDatel)));
        }
    }

    protected void showDialog() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cartorder_item_detail);
        //dialog.setTitle("Order Detail");

        orderDetailListView = (ListView) dialog
                .findViewById(R.id.pos_order_detail_listview);

        totaltaxtxt = (TextView) dialog.findViewById(R.id.totaltaxtxt);
        basePrice = (TextView) dialog.findViewById(R.id.basePrice);
        totaltax = (TextView) dialog.findViewById(R.id.totaltax);
        otherPrice = (TextView) dialog.findViewById(R.id.otherPrice);

        pos_order_dialog_layout= (LinearLayout) dialog.findViewById(R.id.pos_order_dialog_layout);
        pos_order_dialog_layout_price= (LinearLayout) dialog.findViewById(R.id.pos_order_dialog_layout_price);

        total = (TextView) dialog.findViewById(R.id.pos_order_item_amt);
        round_off_amt= (TextView) dialog.findViewById(R.id.round_off_amt);
        discountTotal = (TextView) dialog
                .findViewById(R.id.pos_order_item_discount);

        LinearLayout lyt=(LinearLayout)dialog.findViewById((R.id.item_title_layout));
        lyt.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        TextView item_details_title= (TextView) dialog.findViewById(R.id.item_details_title);
        TextView order_time_track= (TextView) dialog.findViewById(R.id.order_time_track);

        setRobotoThinFont(item_details_title,this);
        setRobotoThinFont(order_time_track,this);


        order_time_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                TrackDialog();

            }
        });


        dialog.show();
    }


    protected void TrackDialog() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.order_track_popup);
        //dialog.setTitle("Order Detail");

        ListView order_track_listview = (ListView) dialog.findViewById(R.id.order_track_listview);
        LinearLayout lyt=(LinearLayout)dialog.findViewById((R.id.track_title_layout));
        lyt.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        TextView item_details_title= (TextView) dialog.findViewById(R.id.item_details_title);
        TextView order_time_track= (TextView) dialog.findViewById(R.id.order_time_track);

        setRobotoThinFont(item_details_title,this);
        setRobotoThinFont(order_time_track,this);


        item_details_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                showDialog();

            }
        });





        dialog.show();
    }


    private void fetchOrderList() {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(context)) {
            try {
                showLoading();
                Map map = new HashMap<>();
                map.put("Page", "FetchOrderDetail");
                map.put("IsActive", "A");

                map.put("userID", Utils.getUserId(this));
                map.put("UserLogin",Utils.getUserLogin(this));
                map.put("Password", Utils.getPassword(this));
                System.out.print("fetch order detail" + map.toString());
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_ORDER_DETAILS, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showMessage("Internet connection not found");
        }
    }

    private void fetchOrderDetailById(String id) {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(context)) {
            try {
//                showLoading();
                Map map = new HashMap<>();
                map.put("Page", "OrderDetails_OnClick");
                map.put("orderID", id);
                map.put("userID", Utils.getUserId(this));
                System.out.println("order Id " + id);


                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_ORDER_DETAILS_BY_ID, map);
            } catch (Exception e) {

            }
        } else {
            showMessage("Internet connection not found");
        }
    }

    @SuppressWarnings("unchecked")
    private void sendMailSms(ArrayList<FetchOrder> arrayList2) {

        Log.i("send","sendsms");
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(context)) {
            try {
//                showLoading();
                Map map = new HashMap<>();
                map.put("Page", "MailMsgSendUsingChk");
                map.put("userID", Utils.getUserId(this));
                map.put("UserLogin",Utils.getUserLogin(this));
                map.put("Password", Utils.getPassword(this));
                map.put("chkSms", String.valueOf(statusSms));
                map.put("chkEmail", String.valueOf(statusMail));
                map.put("EmailID", clickObject.getEmail());
                map.put("ContactNo", clickObject.getMobile());
                map.put("CustomerName", clickObject.getContactName());
                map.put("OrderNo", clickObject.getOrderno());
                map.put("InvoiceNo", "");
                map.put("Invoicedt", clickObject.getOrderDate());
                map.put("BillAmt", Utils.doubleToString(clickObject.getBillAmount()));
                map.put("PaidAmount", clickObject.getPaidAmount());
                map.put("BalanceAmount", Utils.doubleToString(clickObject.getBalanceAmount()));
                map.put("FromPage","Order");
                map.put("UserID",Utils.getUserId(context));
                Log.i("Order","" + map.toString());

                new RequestManager().sendPostRequest(this,
                        KEY_SEND_SMS_MAIL, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showMessage("Internet connection not found");
        }
    }

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            Spinner spinSelected = (Spinner) parent;
            String selectedItem = (String) spinSelected.getSelectedItem();
            searchBox.setText("");
            if (selectedItem.equals("Last 50 Orders") || selectedItem.equals("")) {
                search_icon.setEnabled(false);
                searchBox.setVisibility(View.GONE);
                dateSearch.setVisibility(View.GONE);
            }
            else  search_icon.setEnabled(true);
            if (orderAdapter != null && selectedItem.equals("Date")) {
                orderAdapter.clearFilters();
                searchBox.setVisibility(View.GONE);
                search_icon.setVisibility(View.GONE);
                paymentSearch.setVisibility(View.GONE);
                dateSearch.setVisibility(View.VISIBLE);
                dateImage.setVisibility(View.VISIBLE);
                dateRange.setVisibility(View.VISIBLE);
            }else if(orderAdapter != null && selectedItem.equals("Payment Status")){
                orderAdapter.clearFilters();
                search_icon.setVisibility(View.GONE);
                searchBox.setVisibility(View.GONE);
                dateSearch.setVisibility(View.GONE);
                dateImage.setVisibility(View.INVISIBLE);
                dateRange.setVisibility(View.INVISIBLE);
                paymentSearch.setVisibility(View.VISIBLE);
            }
//			else if (orderAdapter != null && selectedItem.equals("Paid")) {
//				orderAdapter.setSelectedItemType(selectedItem);
//				orderAdapter.setPaidFiltered();
//			} else if (orderAdapter != null && selectedItem.equals("Unpaid")) {
//				orderAdapter.setSelectedItemType(selectedItem);
//				orderAdapter.setUnPaidFiltered();
//			} else if (orderAdapter != null
//					&& selectedItem.equals("Partially paid")) {
//				orderAdapter.setSelectedItemType(selectedItem);
//				orderAdapter.setParitalllyPaidFiltered();
//			}
            else if (orderAdapter != null) {
                orderAdapter.clearFilters();
                orderAdapter.setSelectedItemType(selectedItem);
                search_icon.setVisibility(View.VISIBLE);
                dateSearch.setVisibility(View.GONE);
                dateImage.setVisibility(View.INVISIBLE);
                dateRange.setVisibility(View.INVISIBLE);
                paymentSearch.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            orderAdapter.setSelectedItemType(null);
        }
    };

    @Override
    public void onReceiveResponse(int requestId, String response) {

        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_ORDER_DETAILS) {
                Gson gson = new Gson();
                OrderList orList = gson.fromJson(response, OrderList.class);
                orderList = orList.getOrder();
                if (orderList != null && orderList.size() > 0) {
                    orderAdapter = new CartOrderAdapter(this, orderList);
                      orderListView.setAdapter(orderAdapter);
                    // System.out.println("order response"+response);
                    orderListView.setEmptyView(emptyView);
                } else {
                    orderListView.setEmptyView(emptyView);
                }
            }

            else if (requestId == KEY_FETCH_ORDER_DETAILS_BY_ID) {
                showDialog();
                arrayList = new ArrayList<>();
                Double amt = 0.0, discountAmt = 0.0, OTCAmount = 0.0, TaxAmount = 0.0;
                boolean isInclusiveTax = false;
                orderDetailAdapter = new CartOrderDetailAdapter(arrayList);
                orderDetailListView.setAdapter(orderDetailAdapter);
                Gson gson = new GsonBuilder().create();

                FetchOrderForId arrayId = gson.fromJson(response,
                        FetchOrderForId.class);

                // System.out.println("response "+response+
                // " "+" arrayID "+arrayId.getFetchOrder());

                if (arrayId.getFetchOrder() != null) {

                    for (int i = 0; i < arrayId.getFetchOrder().size(); i++) {
                        arrayList.add(arrayId.getFetchOrder().get(i));
                        amt = amt + arrayList.get(i).getBillAmount();
                        discountAmt = arrayList.get(i).getDiscountAmount();
                        OTCAmount = arrayList.get(i).getOTCAmount();
                        TaxAmount = arrayList.get(i).getTaxAmount();
                        isInclusiveTax = arrayList.get(i).getIsInclusiveTax();
                    }

                    if (arrayList != null && arrayList.size() > 0) {
                        orderDetailAdapter = new CartOrderDetailAdapter(
                                arrayList);
                        orderDetailAdapter.notifyDataSetChanged();

                        discountTotal
                                .setText(Utils.doubleToString(discountAmt));
                        if (isInclusiveTax)
                        {
                            totaltaxtxt.setText("(inc) Total Tax");
                            TaxAmount=(double) 0;
                        }
                        else
                            totaltaxtxt.setText("(+) Total Tax");
                        basePrice.setText(Utils.doubleToString(amt));
                        totaltax.setText(Utils.doubleToString(TaxAmount));
                        otherPrice.setText(Utils.doubleToString(OTCAmount));

                        double total_order_price=amt - discountAmt + TaxAmount + OTCAmount;
                        total.setText(Utils.doubleToString(total_order_price));
                        round_off_amt.setText(""+Math.round(total_order_price));

                    }

                }


            }

        }
    }



    @Override
    public void onErrorResponse(VolleyError error) {
        hideLoading();
        Toast.makeText(context, context.getResources().getString(R.string.volleyError).toString(), Toast.LENGTH_SHORT).show();
        // showMessage("Please try again");
    }

    @Override
    public void totalRows(String text) {
        // TODO Auto-generated method stub
        totalRows.setText("Records : "+text);
    }



    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id)
        {
            case R.id.dateTitle:
                orderSorting(1,dateAsc);
                setTitleText();
                if(dateAsc) dateTitle.setText(context.getString(R.string.titledateAsc));
                else dateTitle.setText(context.getString(R.string.titledateDesc));
                dateAsc=ChangeStatus(dateAsc);
                break;
            case R.id.orderTitle:
                orderSorting(2,orderAsc);
                setTitleText();
                if(orderAsc) orderTitle.setText(context.getString(R.string.titleorderAsc));
                else orderTitle.setText(context.getString(R.string.titleorderDesc));
                orderAsc=ChangeStatus(orderAsc);
                break;
           /* case R.id.customerTitle:
                orderSorting(3,CustomerAsc);
                setTitleText();
                if(CustomerAsc) customerTitle.setText(context.getString(R.string.titlecustomerAsc));
                else customerTitle.setText(context.getString(R.string.titlecustomerDesc));
                CustomerAsc=ChangeStatus(CustomerAsc);
                break;*/
            case R.id.amountTitle:
                orderSorting(4,amountAsc);
                setTitleText();
                if(amountAsc) amountTitle.setText(context.getString(R.string.titleamtAsc));
                else amountTitle.setText(context.getString(R.string.titleamtDesc));
                amountAsc=ChangeStatus(amountAsc);
                break;
            case R.id.paidTitle:
                orderSorting(5,paidAsc);
                setTitleText();
                if(paidAsc) paidTitle.setText(context.getString(R.string.titlepaidAsc));
                else paidTitle.setText(context.getString(R.string.titlepaidDesc));
                paidAsc=ChangeStatus(paidAsc);
                break;
            case R.id.order_export:
                try {
                    ExportCSV();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void setTitleText()
    {
        dateTitle.setText("Date");
        orderTitle.setText("Order");
       // customerTitle.setText("Name");
        amountTitle.setText("Amount");
        paidTitle.setText("Paid");
    }
    private boolean ChangeStatus(boolean b)
    {
        if(b) return false;
        else return true;
    }

    private  void orderSorting(Integer field, boolean status)
    {
        ArrayList<OrderDetailModelData> orderArray=orderAdapter.getOrderList();
        if(orderArray.size()>0) {
            Collections.sort(orderArray, new OrderComparator(field,status));
            orderAdapter.notifyDataSetChanged();
        }
    }

    private void showMessage(String str)
    {
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    private void ExportCSV() throws IOException {
        ArrayList<OrderDetailModelData> orderArray=orderAdapter.getOrderList();
        String path = Environment.getExternalStorageDirectory().getPath();
        long current_time = System.currentTimeMillis();
        File dir = new File(path + "/"+getString(R.string.app_name)+"/csv/");
        if (!dir.exists())  dir.mkdirs();
        File csvfile = new File(dir+"/OrderList_" + current_time + ".csv");

        FileWriter writer = new FileWriter(csvfile);

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Date", "Order", "Customer","Amount","Paid"));

        for (OrderDetailModelData d : orderArray) {

            List<String> list = new ArrayList<>();
            // list.add(d.getOrderDate());
            try {
                String pattern = "MM/dd/yyyy";
                Date date =  new Date(Long.parseLong(d.getOrderDate()));
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                list.add(format.format(date));
            }catch(Exception c)
            {
                System.out.println("Date erroer"+c.getMessage());
            }
            list.add(d.getOrderCode());
            list.add(d.getContactName());
            list.add(String.valueOf(d.getBillAmount()));
            list.add(d.getPaidAmount());

            CSVUtils.writeLine(writer, list);

            //try custom separator and quote.
            //CSVUtils.writeLine(writer, list, '|', '\"');
        }

        writer.flush();
        writer.close();
        Toast.makeText(context,"csv downloaded",Toast.LENGTH_LONG).show();
        opencsv(csvfile);

    }

    private void opencsv(final File file)
    {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage("CSV saved successfully in "+file.toString() + " do you want to open it? ")
                .setPositiveButton("Open now",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // CheckDownloadStatus = false;
                                dialog.cancel();
                                Uri csvpath = Uri.fromFile(file);
                                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                pdfOpenintent.setDataAndType(csvpath, "text/csv");
                                try {
                                    startActivity(pdfOpenintent);
                                }
                                catch (ActivityNotFoundException e) {

                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //CheckDownloadStatus = true;
                                dialog.cancel();
                            }
                        }).show();
    }




}
