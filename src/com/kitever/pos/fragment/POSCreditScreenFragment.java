package com.kitever.pos.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.fragment.PosComparators.CSVUtils;
import com.kitever.pos.fragment.PosComparators.CreditComparator;
import com.kitever.pos.fragment.adapters.POSCreditAdapterFragment;
import com.kitever.pos.model.data.CreditDetails;
import com.kitever.pos.model.data.CreditModelData;
import com.kitever.pos.model.manager.ModelManager;
import com.kitever.utils.DateHelper;
import com.kitever.utils.SpinnerReselect;
import com.kitever.utils.TotalRows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
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

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link POSCreditScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class POSCreditScreenFragment extends Fragment implements NetworkManager,View.OnClickListener, DatePickerDialog.OnDateSetListener, TotalRows, ExecuteService{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String UserId;
    private String UserLogin;
    private String PassWord;

    private POSCreditAdapterFragment creditAdapter;
    private final int KEY_FETCH_CREDIT_BALANCE = 1;
    private final int KEY_FETCH_MODE_PAYMENT=2;
    private final int KEY_ADD_PAYMENT=3;
    private ListView creditListView;
    private TextView totalCredit, totalRecord, dateRange, noRecord;
    private EditText searchBox;
    private SpinnerReselect dateSearch;
    private ImageView dateImage;

    private long startDatel, endDatel,dateInMillis;
    private boolean statusEndDate = false, statusStartDate = false, statusPopup= false;

    private SimpleDateFormat format;
    private Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog dialogDate ;

    private EditText billingAmtEdit, paidAmtEdit, balanceEdit;
    private Spinner modePaymentSpinner;
    private String modeOfPaymentVal="";

    private EditText chequeNo, bankNameOrRefNum, remarkOrDes;
    private TextView date;
    private String modeType="", chequeNoVal="", dateVal="", remarkOrDesVal="",
            bankNameOrRefNumVal="";
    private Context context;
    private FrameLayout inputLayout, calendarLayout;
    private CalendarView calenderView;
    String ref_number="";
    private int positionClick=-1;
    private CreditDetails clickObject=null;
    RelativeLayout payment_mode_layout;
    ArrayAdapter<String> PaymentModeadapter;
    boolean paymentMode_not_cash=false;
    private int modeNo = 0;
    String OrderID="",contactID="";
    private RelativeLayout emptyView;
    private ProgressBar progressBar;
    private ImageView search_icon;
    private boolean IssearchBoxOpen=false;
    Spinner selectType;
    private String InvoiceNo="",InvoiceCode="";
    private TextView dateTitle,orderTitle,customerTitle,billTitle,creditTitle;
    private ImageView credit_export;
    private boolean dateAsc=false,orderAsc=false,CustomerAsc=false,billAsc=false,creditAsc=false;


    public POSCreditScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment POSCreditScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static POSCreditScreenFragment newInstance(String param1, String param2, String param3) {
        POSCreditScreenFragment fragment = new POSCreditScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UserId = getArguments().getString(ARG_PARAM1);
            UserLogin = getArguments().getString(ARG_PARAM2);
            PassWord = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pos_credit_layout,container,false);
        progressBar = (ProgressBar) view.findViewById(R.id.marker_progress);
        progressBar.setVisibility(View.GONE);
        setScreen(view);
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchCreditBalance();
                fetchModeOfPayment();
                selectType.setSelection(0);
                IssearchBoxOpen=false;
                searchBox.setText("");
                searchBox.setVisibility(View.GONE);

            }
        },500);
        return view;
    }

    private void setScreen(View view) {
        selectType = (Spinner) view.findViewById(R.id.select_type_spinner);
        searchBox = (EditText) view.findViewById(R.id.edit_search);
        ImageView adavanceSearch = (ImageView) view.findViewById(R.id.advance_search);
        creditListView = (ListView) view.findViewById(R.id.credit_list_view);

        creditListView.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));

        totalRecord = (TextView) view.findViewById(R.id.credit_total_rows);



        noRecord = (TextView) view.findViewById(R.id.no_record);
        MoonIcon mIcon=new MoonIcon(this);
        mIcon.setTextfont(noRecord);
        emptyView = (RelativeLayout) view.findViewById(R.id.credit_emptyElement);
        creditListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                positionClick= position;
                clickObject = creditAdapter.getClickObject(position);
                contactID=clickObject.getContactID();
                OrderID=clickObject.getOrderID();
                InvoiceCode=clickObject.getInvoiceCode();
                InvoiceNo=clickObject.getInvoiceNo();
                float creditAmount=0.0f;
                creditAmount=Float.parseFloat(clickObject.getBalanceAmount());
                if(creditAmount>0) posPaymentPopup(clickObject.getBalanceAmount());
//                else
//                    showMessage(clickObject.getContact_Name() +" have not any credit amount to pay");

            }
        });


        dateImage = (ImageView) view.findViewById(R.id.pos_credit_image_date);
        dateRange = (TextView) view.findViewById(R.id.pos_credit_date_range);
        dateSearch = (SpinnerReselect) view.findViewById(R.id.date_credit_search);
        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("Last 50 Credits");
        typeList.add("Contact");
        typeList.add("Order Id");
        typeList.add("Total Bill >=");
        typeList.add("Total Bill <=");
        typeList.add("Credit >=");
        typeList.add("Credit <=");
        typeList.add("Date");



        dateTitle = (TextView) view.findViewById(R.id.dateTitle);
        orderTitle = (TextView) view.findViewById(R.id.orderTitle);
        customerTitle = (TextView) view.findViewById(R.id.customerTitle);
        billTitle = (TextView) view.findViewById(R.id.billTitle);
        creditTitle = (TextView) view.findViewById(R.id.creditTitle);
        credit_export= (ImageView) view.findViewById(R.id.credit_export);


        RelativeLayout page_main_layout=(RelativeLayout)view.findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        totalRecord.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        dateRange.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        searchBox.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        setRobotoThinFont(totalRecord,getActivity());
        setRobotoThinFont(dateRange,getActivity());
        setRobotoThinFont(searchBox,getActivity());

        dateTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        orderTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        customerTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        billTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        creditTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        dateTitle.setOnClickListener(this);
        orderTitle.setOnClickListener(this);
        customerTitle.setOnClickListener(this);
        billTitle.setOnClickListener(this);
        creditTitle.setOnClickListener(this);
        credit_export.setOnClickListener(this);

        mIcon.setTextfont(dateTitle);
        mIcon.setTextfont(orderTitle);
        mIcon.setTextfont(customerTitle);
        mIcon.setTextfont(billTitle);
        mIcon.setTextfont(creditTitle);

        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectType.setAdapter(typeAdapter);

        search_icon= (ImageView) view.findViewById(R.id.search_icon);
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
                if (creditAdapter != null) {
                    creditAdapter.getFilter().filter(cs);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        selectType.setOnItemSelectedListener(itemSelectedListener);
        dateSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                String pattern = "d-MMM, yyyy";
                format= new SimpleDateFormat(pattern);

                if(position == 1)
                {
                    creditAdapter.dateRangeFilter(position);
                    dateRange.setText(""+ DateHelper.getTodayDate());

                }	else if(position == 2)
                {
                    creditAdapter.dateRangeFilter(position);
                    dateRange.setText(""+DateHelper.getYestardayDate(DateHelper.getDateInmillis()));
                }
                else if(position == 3)
                {
                    creditAdapter.dateRangeFilter(position);
                    dateRange.setText(DateHelper.getFirstDayOfWeek()+" - "+format.format(new Date(DateHelper.getDateInmillis())));
                }
                else if(position ==4)
                {
                    creditAdapter.dateRangeFilter(position);
                    dateRange.setText(DateHelper.getFirstDayOfMonth()+" - "+format.format(new Date(DateHelper.getDateInmillis())));
                }
                else if(position == 5)
                {
                    creditAdapter.dateRangeFilter(position);

                    dateRange.setText(DateHelper.getLastMonth());
                }
                else if(position==6)
                {
                    statusStartDate = true;
                    dialogDate = new DatePickerDialog(getActivity(), POSCreditScreenFragment.this, year, month, day);
                    if(statusStartDate==true){
                        dialogDate.getDatePicker().setMaxDate((new Date().getTime()));//-(5097600000L)
                        dialogDate.setTitle("Select Start Date");
                        dialogDate.show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // TODO Auto-generated method stub
        creditAdapter.dateFilter(startDatel, startDatel+5184000000L);

        if(statusStartDate == true)
        {
            startDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            final int yearTemp = year, monthTemp=monthOfYear, dayTemp=dayOfMonth;
            statusEndDate = true;
            statusStartDate = false;
            dialogDate = new DatePickerDialog(getActivity(),POSCreditScreenFragment.this, year, month, day);
            c = Calendar.getInstance();
            Long maxdate;
            dateInMillis = c.getTimeInMillis();
            maxdate = (startDatel+(5184000000L))>dateInMillis? dateInMillis:(startDatel+(5184000000L));
            dialogDate.getDatePicker().setMaxDate(maxdate);//startDatel+(5184000000L)
            dialogDate.getDatePicker().setMinDate(startDatel);
            dialogDate.setTitle("End Date (max 60 days)");
            dialogDate.setButton(DialogInterface.BUTTON_NEUTRAL, "Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Your code
                    statusStartDate = true;

                    dialogDate = new DatePickerDialog(getActivity(), POSCreditScreenFragment.this, yearTemp, monthTemp, dayTemp);
                    if(statusStartDate==true){
                        dialogDate.getDatePicker().setMaxDate(new Date().getTime());//-(5097600000L))
                        dialogDate.setTitle("Select Start Date");
                        dialogDate.show();
                    }
                }
            });
            dialogDate.show();
        }
        else if(statusEndDate == true)
        {
            endDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            creditAdapter.dateFilter(startDatel, endDatel+(86400000L));
            String pattern = "d-MMM, yyyy";

            SimpleDateFormat format = new SimpleDateFormat(pattern);
            dateRange.setText(format.format(new Date(startDatel))+"  -  "+format.format(new Date(endDatel)));
        }
        else if(statusPopup == true)
        {
            date.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);
            statusPopup = false;
        }
    }



    AdapterView.OnItemSelectedListener ModeSelectedListner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Spinner spinModePayment = (Spinner) parent;


            if (spinModePayment.getId() == R.id.mode_payment) {
                modeType = (String) spinModePayment.getSelectedItem();
                modeNo = position;
                if (position > 0 && !modeType.equalsIgnoreCase("Cash")) {
                    payment_mode_layout.setVisibility(View.VISIBLE);
                    paymentMode_not_cash=true;
                    if (modeType != null
                            && (modeType.equalsIgnoreCase("RTGS") || (modeType
                            .equalsIgnoreCase("Credit Card")))) {
                        chequeNo.setVisibility(View.GONE);
                        bankNameOrRefNum.setHint("Ref Number");
                        remarkOrDes.setHint("Description");
                        ref_number="Referenece Number";

                    } else {
                        chequeNo.setVisibility(View.VISIBLE);
                        bankNameOrRefNum.setHint("Bank name");
                        remarkOrDes.setHint("Remark");
                        ref_number="Bank Name";
                    }
                }
                else
                {
                    paymentMode_not_cash=false;
                    payment_mode_layout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {


        }
    };



    private boolean  PaymentModeValidation()
    {
        chequeNoVal = chequeNo.getText().toString().trim();
        dateVal = date.getText().toString().trim();
        bankNameOrRefNumVal = bankNameOrRefNum.getText().toString().trim();
        remarkOrDesVal = remarkOrDes.getText().toString().trim();

        if(modeType.equalsIgnoreCase("Cheque"))
        {
            if (chequeNoVal == null || chequeNoVal.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Please enter cheque number",
                        Toast.LENGTH_LONG).show();
                return  false ;
            }
        }

        if (dateVal == null || dateVal.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please enter date",
                    Toast.LENGTH_LONG).show();
            return  false;
        } else if (bankNameOrRefNumVal == null
                || bankNameOrRefNumVal.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please enter "+ref_number,
                    Toast.LENGTH_LONG).show();
            return  false;
        } else {

        }
        return  true ;
    }


    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            // TODO Auto-generated method stub
            Spinner spinSelected = (Spinner) parent;
            String selectedItem = (String) spinSelected.getSelectedItem();
            searchBox.setText("");

            if(selectedItem.equals("Last 50 Credits") || selectedItem.equals(""))
            {
                search_icon.setEnabled(false);
                searchBox.setVisibility(View.GONE);
                dateSearch.setVisibility(View.GONE);
            }
            else
                search_icon.setEnabled(true);

            if(selectedItem.equals("Date") && creditAdapter  != null)
            {
                searchBox.setVisibility(View.GONE);
                search_icon.setVisibility(View.GONE);
                dateSearch.setVisibility(View.VISIBLE);
                dateImage.setVisibility(View.VISIBLE);
                dateRange.setVisibility(View.VISIBLE);
            }
            else if (creditAdapter != null) {
                creditAdapter.clearFilters();
                creditAdapter.setSelectedItemType(selectedItem);
                search_icon.setVisibility(View.VISIBLE);
                dateSearch.setVisibility(View.GONE);
                dateImage.setVisibility(View.INVISIBLE);
                dateRange.setVisibility(View.INVISIBLE);

                if(selectedItem.equals("Credit >"))
                    searchBox.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
                else
                    searchBox.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
            creditAdapter.setSelectedItemType(null);
        }
    };

    private void fetchCreditBalance() {

        if (Utils.isDeviceOnline(getActivity())) {
            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchCreditDetails");
            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);

            try {
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_CREDIT_BALANCE, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            showMessage("Internet connection not found");
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading()
    {
        progressBar.setVisibility(View.GONE);
    }

    @SuppressWarnings({ "unchecked", "unused" })
    private void AddCreditPayemnt(String paidAmount) {

        if (Utils.isDeviceOnline(getActivity())) {
//            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "InsertPartPaymentDetail");
            map.put("InvoiceNo", InvoiceNo);
            map.put("InvoiceCode", InvoiceCode);
            map.put("OrderID", OrderID);
            map.put("contactID", contactID);
            map.put("PaidAmount", paidAmount);
            map.put("PayMode", ""+modeNo);
            map.put("PayModeNo", chequeNoVal);
            map.put("ChequeDate", dateVal);
            map.put("BankName", bankNameOrRefNumVal);
            map.put("Remark", remarkOrDesVal);

            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);


            Log.i("add apyment","map - "+map.toString());

            try {
                new RequestManager().sendPostRequest(this,
                        KEY_ADD_PAYMENT, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    private void fetchModeOfPayment() {
        try {
            Map map = new HashMap<>();
            map.put("Page", "FetchModeOFPayMent");
            new RequestManager().sendPostRequest(this,
                    KEY_FETCH_MODE_PAYMENT, map);
        } catch (Exception e) {

        }
    }

    private void setModelayout(Dialog dialog ) {

        chequeNo = (EditText)dialog.findViewById(R.id.cheque_no_edit);
        bankNameOrRefNum = (EditText)dialog.findViewById(R.id.bank_name_edit);
        remarkOrDes = (EditText)dialog.findViewById(R.id.remark_edit);
        //inputLayout = (FrameLayout)dialog.findViewById(R.id.input_layout);
        //calendarLayout = (FrameLayout)dialog.findViewById(R.id.calendar_layout);
        date = (TextView)dialog.findViewById(R.id.date_id);

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialogDate = new DatePickerDialog(getActivity(), POSCreditScreenFragment.this, year, month, day);
                dialogDate.getDatePicker().setMaxDate((new Date().getTime()));//-(5097600000L)
//				dialogDate.setTitle("Select Start Date");
                statusPopup = true;
                dialogDate.show();

            }
        });
    }

    private void posPaymentPopup(String balanceAmount)
    {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pos_addpaymentpoup);
        dialog.setTitle("Add Payment");
        billingAmtEdit = (EditText)dialog.findViewById(R.id.billing_amt);
        billingAmtEdit.setEnabled(false);
        billingAmtEdit.setText(balanceAmount);
        paidAmtEdit = (EditText)dialog.findViewById(R.id.paid_amt);
        balanceEdit = (EditText) dialog.findViewById(R.id.balance);
        balanceEdit.setEnabled(false);
        modePaymentSpinner = (Spinner)dialog.findViewById(R.id.mode_payment);
        payment_mode_layout= (RelativeLayout)dialog.findViewById(R.id.payment_mode_layout);
        setModelayout(dialog);
        modePaymentSpinner.setAdapter(PaymentModeadapter);
        modePaymentSpinner.setOnItemSelectedListener(ModeSelectedListner);

        paidAmtEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                balanceEdit.setText("");
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {}
            @Override
            public void afterTextChanged(Editable s) {

                String paidAmt = paidAmtEdit.getText().toString();
                try {
                    if (paidAmt != null && !paidAmt.equalsIgnoreCase("")) {
                        float amt = Float.parseFloat(paidAmt);
                        if (billingAmtEdit.getText() != null
                                && !billingAmtEdit.getText().toString().trim()
                                .equalsIgnoreCase("")) {

                            float billingAmt = Float.parseFloat(billingAmtEdit
                                    .getText().toString().trim());
                            if (amt > billingAmt) {
                                paidAmtEdit.setText("");
                                paidAmtEdit.setHint("Paid amount");
                            } else {

                                float bal = billingAmt - amt;
                                DecimalFormat f = new DecimalFormat("##.00");
                                balanceEdit.setText("" + f.format(bal));
                            }
                        }
                    }
                } catch(Exception e) {e.getMessage(); }
            }
        });

        Button addMainBtn = (Button)dialog.findViewById(R.id.btn_add_without_invoice);
        addMainBtn.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        addMainBtn.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        //Button addInvoiceBtn = (Button)dialog.findViewById(R.id.btn_add_invoice);

        addMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(paidAmtEdit.getText().toString().length()>0)
                {
                    if(modeNo==0)
                    {
                        Toast.makeText(getActivity(), "Select Payment Mode", Toast.LENGTH_SHORT).show();
                    }
                    else if(modeNo==1)
                    {
                        AddCreditPayemnt(paidAmtEdit.getText().toString());
                        dialog.cancel();
                    }
                    else
                    {
                        if(PaymentModeValidation())
                        {
                            AddCreditPayemnt(paidAmtEdit.getText().toString());
                            dialog.cancel();
                        }

                    }

                }
                else
                {
                    Toast.makeText(getActivity(), "Enter The Paid Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_CREDIT_BALANCE) {
                Gson gson = new Gson();
                CreditModelData creditData = gson.fromJson(response, CreditModelData.class);
                ArrayList<CreditDetails> creditList = creditData.getCreditDetails();
             /*   Iterator<CreditDetails> i = creditList.iterator();
                while(i.hasNext())
                {

                    if(Double.parseDouble(i.next().getBalanceAmount())< 1)
                    {
//						Log.d("credit ",creditList.get(i).toString());
                        i.remove();
                    }
                }*/
                Log.d("credit ",creditList.toString());
                creditAdapter = new POSCreditAdapterFragment(this,creditList);

                creditListView.setAdapter(creditAdapter);
                creditListView.setEmptyView(emptyView);
            }
            else if (requestId == KEY_FETCH_MODE_PAYMENT) {
                ModelManager.getInstance().setFetchModeOfPaymentModel(response);
                ArrayList<String> list = new ArrayList<>();
                list.add("Select mode of payment");
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
                PaymentModeadapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, list);
                PaymentModeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            }
            else if(requestId==KEY_ADD_PAYMENT)
            {
                Log.i("Response","Response- "+response);
                fetchCreditBalance();
            }
        } else {
            showMessage("Please try again.");
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        hideLoading();
        showMessage(getActivity().getResources().getString(R.string.volleyError).toString());
    }

    @Override
    public void totalRows(String text) {
        // TODO Auto-generated method stub
        totalRecord.setText("Records : "+text);
    }


    @Override
    public void executeService() {
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchCreditBalance();
                fetchModeOfPayment();
                selectType.setSelection(0);
                IssearchBoxOpen=false;
                searchBox.setText("");
                searchBox.setVisibility(View.GONE);

            }
        },500);
    }

    private void showMessage(String str)
    {
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }

    private boolean ChangeStatus(boolean b)
    {
        if(b) return false;
        else return true;
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        switch(id)
        {
            case R.id.dateTitle:
                orderSorting(1,dateAsc);
                setTitleText();
                if(dateAsc) dateTitle.setText(getActivity().getString(R.string.titledateAsc));
                else dateTitle.setText(getActivity().getString(R.string.titledateDesc));
                dateAsc=ChangeStatus(dateAsc);
                break;
            case R.id.orderTitle:
                orderSorting(3,orderAsc);
                setTitleText();
                if(orderAsc) orderTitle.setText(getActivity().getString(R.string.titleorderAsc));
                else orderTitle.setText(getActivity().getString(R.string.titleorderDesc));
                orderAsc=ChangeStatus(orderAsc);
                break;
            case R.id.customerTitle:
                orderSorting(2,CustomerAsc);
                setTitleText();
                if(CustomerAsc) customerTitle.setText(getActivity().getString(R.string.titlecustomerAsc));
                else customerTitle.setText(getActivity().getString(R.string.titlecustomerDesc));
                CustomerAsc=ChangeStatus(CustomerAsc);
                break;
            case R.id.billTitle:
                orderSorting(4,billAsc);
                setTitleText();
                if(billAsc) billTitle.setText(getActivity().getString(R.string.titlebillAsc));
                else billTitle.setText(getActivity().getString(R.string.titlebillDesc));
                billAsc=ChangeStatus(billAsc);
                break;
            case R.id.creditTitle:
                orderSorting(5,creditAsc);
                setTitleText();
                if(creditAsc) creditTitle.setText(getActivity().getString(R.string.titlepaidAsc));
                else creditTitle.setText(getActivity().getString(R.string.titlepaidDesc));
                creditAsc=ChangeStatus(creditAsc);
                break;
            case R.id.credit_export:
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
        customerTitle.setText("Name");
        billTitle.setText("Bill");
        creditTitle.setText("Paid");
    }

    private  void orderSorting(Integer field, boolean status)
    {
        ArrayList<CreditDetails> creditList =creditAdapter.creditList();
        if(creditList.size()>0) {
            Collections.sort(creditList, new CreditComparator(field,status));
            creditAdapter.notifyDataSetChanged();
        }
    }

    private void ExportCSV() throws IOException {
        ArrayList<CreditDetails> creditList =creditAdapter.creditList();
        String path = Environment.getExternalStorageDirectory().getPath();
        long current_time = System.currentTimeMillis();
        File dir = new File(path + "/"+getString(R.string.app_name)+"/csv/");
        if (!dir.exists())  dir.mkdirs();
        File csvfile = new File(dir+"/OrderList_" + current_time + ".csv");

        FileWriter writer = new FileWriter(csvfile);

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Date","Customer", "Order", "Bill","Credit"));

        for (CreditDetails d : creditList) {

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

            list.add(d.getContact_Name());
            list.add(d.getOrderCode());

            list.add(d.getBillAmount());
            list.add(d.getBalanceAmount());

            CSVUtils.writeLine(writer, list);

            //try custom separator and quote.
            //CSVUtils.writeLine(writer, list, '|', '\"');
        }

        writer.flush();
        writer.close();
        Toast.makeText(getActivity(),"csv downloaded",Toast.LENGTH_LONG).show();
        opencsv(csvfile);

    }

    private void opencsv(final File file)
    {
        new AlertDialog.Builder(getActivity())
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
                                Intent csvintent = new Intent(Intent.ACTION_VIEW);
                                csvintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                csvintent.setDataAndType(csvpath, "text/csv");
                                try {
                                    startActivity(csvintent);
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
