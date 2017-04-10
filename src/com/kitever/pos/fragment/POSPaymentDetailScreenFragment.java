package com.kitever.pos.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.activity.POSInvoicePdfViewer;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.fragment.PosComparators.CSVUtils;
import com.kitever.pos.fragment.PosComparators.PaymentComparator;
import com.kitever.pos.fragment.adapters.POSCustomerPaymentDetailAdapterFragment;
import com.kitever.pos.fragment.adapters.POSPaymentDetailAdapterFragment;
import com.kitever.pos.model.data.CreditBalnce;
import com.kitever.pos.model.data.POSPaymentDetailData;
import com.kitever.pos.model.data.PosPayment;
import com.kitever.utils.DateHelper;
import com.kitever.utils.SpinnerReselect;
import com.kitever.utils.TotalRows;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link POSPaymentDetailScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class POSPaymentDetailScreenFragment extends Fragment implements NetworkManager, DatePickerDialog.OnDateSetListener, TotalRows, ExecuteService, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String UserId, UserLogin, PassWord;

    private POSPaymentDetailAdapterFragment paymentDetailsAdapter;
    /*private POSCustomerPaymentDetailAdapterFragment customerPaymentDetailAdapter;*/
    private ListView paymentDetailViewList, lvPaymentDetailDailog;
    private ArrayList<CreditBalnce> arrayList;
    private CreditBalnce clickObject;

    private final int KEY_FETCH_CREDIT_BALANCE = 1,
            KEY_FETCHCUSTOMER_ORDER_DETAIL = 2, KEY_SEND_SMS_MAIL = 3;
    private TextView totalCredit, totalRecord, profileName, profileNumber,
            profileMOP, profilePaid, profileBank, profileChqNo, profileDate,
            profileItemCode, profileContactName, dateRange, pos_payment_remarks, noRecord;
    private Boolean statusEndDate = false, statusStartDate = false,
            statusSms = false, statusMail = false, statusPrint = false;
    private SpinnerReselect dateSearch;
    private EditText searchBox;
    private ImageView dateImage, pdfView, smsImage, mailImage, printImage, search_icon;
    private LinearLayout smsLayout, mailLayout, printLayout;
    private long startDatel, endDatel;
    private Button ok;
    private String name, number;
    private SimpleDateFormat format;
    private Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    private Long dateInMillis;
    private String ReciptNo = "";
    private DatePickerDialog dialogDate;
    private RelativeLayout emptyView;
    private boolean IssearchBoxOpen = false;
    private ProgressBar progressBar;
    private TextView tvPaymentDateLabel,tvPaymentInvoiceLabel, tvPaymentReceiptLabel, tvPaymentCustomerLabel, tvPaymentPaidLabel;
    private boolean date = false, invoice = false, receipt = false, customer = false, paid = false;
    private ImageView payment_export;

    public POSPaymentDetailScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment POSPaymentDetailScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static POSPaymentDetailScreenFragment newInstance(String param1, String param2, String param3) {
        POSPaymentDetailScreenFragment fragment = new POSPaymentDetailScreenFragment();
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
        View view = inflater.inflate(R.layout.pos_payment_detail_layout, container, false);
        setScreen(view);
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchPaymentDetails();
            }
        }, 500);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialogDate != null)
            dialogDate.dismiss();
    }

    private void setScreen(View view) {
        Spinner selectType = (Spinner) view.findViewById(R.id.select_type_spinner);
        searchBox = (EditText) view.findViewById(R.id.edit_search);
        ImageView adavanceSearch = (ImageView) view.findViewById(R.id.advance_search);
        paymentDetailViewList = (ListView) view.findViewById(R.id.payment_view_list);

        paymentDetailViewList.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));

        totalCredit = (TextView) view.findViewById(R.id.total_payment);
        totalRecord = (TextView) view.findViewById(R.id.payment_total_rows);

        tvPaymentDateLabel = (TextView) view.findViewById(R.id.tvPaymentDateLabel);
        tvPaymentInvoiceLabel = (TextView) view.findViewById(R.id.tvPaymentInvoiceLabel);
        tvPaymentReceiptLabel = (TextView) view.findViewById(R.id.tvPaymentReceiptLabel);
        tvPaymentCustomerLabel = (TextView) view.findViewById(R.id.tvPaymentCustomerLabel);
        tvPaymentPaidLabel = (TextView) view.findViewById(R.id.tvPaymentPaidLabel);

        payment_export= (ImageView) view.findViewById(R.id.payment_export);

        dateImage = (ImageView) view.findViewById(R.id.pos_payment_image_date);
        dateRange = (TextView) view.findViewById(R.id.pos_payment_date_range);

        emptyView = (RelativeLayout) view.findViewById(R.id.payment_emptyElement);
        noRecord = (TextView) view.findViewById(R.id.no_record);
        MoonIcon mIcon = new MoonIcon(this);
        mIcon.setTextfont(noRecord);

        mIcon.setTextfont(tvPaymentDateLabel);
        mIcon.setTextfont(tvPaymentInvoiceLabel);
        mIcon.setTextfont(tvPaymentReceiptLabel);
        mIcon.setTextfont(tvPaymentCustomerLabel);
        mIcon.setTextfont(tvPaymentPaidLabel);

        RelativeLayout page_main_layout=(RelativeLayout)view.findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        totalRecord.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        dateRange.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        searchBox.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        setRobotoThinFont(totalRecord,getActivity());
        setRobotoThinFont(dateRange,getActivity());
        setRobotoThinFont(searchBox,getActivity());

        tvPaymentDateLabel.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPaymentInvoiceLabel.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPaymentReceiptLabel.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPaymentCustomerLabel.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPaymentPaidLabel.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        progressBar = (ProgressBar) view.findViewById(R.id.marker_progress);
        progressBar.setVisibility(View.GONE);

        dateSearch = (SpinnerReselect) view.findViewById(R.id.date_payment_search);
        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("Last 50 Payments");
        typeList.add("Invoice");
        typeList.add("Contact");
        typeList.add("Receipt");
        typeList.add("Paid >=");
        typeList.add("Paid <=");
        typeList.add("Date");



        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectType.setAdapter(typeAdapter);
        selectType.setOnItemSelectedListener(itemSelectedListener);

        search_icon = (ImageView) view.findViewById(R.id.search_icon);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IssearchBoxOpen) {
                    searchBox.setVisibility(View.GONE);
                    IssearchBoxOpen = false;
                } else {
                    searchBox.setVisibility(View.VISIBLE);
                    IssearchBoxOpen = true;
                }
            }
        });


        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                if (paymentDetailsAdapter != null) {
                    paymentDetailsAdapter
                            .getFilter().filter(cs);
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

        paymentDetailViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                name = paymentDetailsAdapter.getName(position);
                number = paymentDetailsAdapter.getNumber(position);
                ReciptNo = paymentDetailsAdapter.getReciptNo(position);
                statusMail = paymentDetailsAdapter.getMailStatus(position);
                clickObject = arrayList.get(position);
                fetchCustomerOrderDetails(arrayList.get(position)
                        .getReceiptNo(), arrayList.get(position)
                        .getContact_Name(), arrayList.get(position)
                        .getInvoiceID(), position);

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
                    paymentDetailsAdapter.dateRangeFilter(position);
                    dateRange.setText("" + DateHelper.getTodayDate());

                } else if (position == 2) {
                    paymentDetailsAdapter.dateRangeFilter(position);
                    dateRange.setText(""
                            + DateHelper.getYestardayDate(DateHelper
                            .getDateInmillis()));
                } else if (position == 3) {
                    paymentDetailsAdapter.dateRangeFilter(position);
                    dateRange.setText(DateHelper.getFirstDayOfWeek()
                            + " - "
                            + format.format(new Date(DateHelper
                            .getDateInmillis())));
                } else if (position == 4) {
                    paymentDetailsAdapter.dateRangeFilter(position);
                    dateRange.setText(DateHelper.getFirstDayOfMonth()
                            + " - "
                            + format.format(new Date(DateHelper
                            .getDateInmillis())));
                } else if (position == 5) {
                    paymentDetailsAdapter.dateRangeFilter(position);

                    dateRange.setText(DateHelper.getLastMonth());
                } else if (position == 6) {
                    statusStartDate = true;
                    dialogDate = new DatePickerDialog(
                            getActivity(),
                            POSPaymentDetailScreenFragment.this, year, month, day);
                    if (statusStartDate == true) {
                        dialogDate.getDatePicker().setMaxDate(
                                (new Date().getTime()));// -(5097600000L)
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

        tvPaymentDateLabel.setOnClickListener(this);
        tvPaymentInvoiceLabel.setOnClickListener(this);
        tvPaymentReceiptLabel.setOnClickListener(this);
        tvPaymentCustomerLabel.setOnClickListener(this);
        tvPaymentPaidLabel.setOnClickListener(this);
        payment_export.setOnClickListener(this);
    }

    protected void showDialog(final int position) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pos_payment_single_detail);
        dialog.setTitle("Payment Details");
//        showLoading();
        Button textView = (Button) dialog
                .findViewById(R.id.pos_payment_detail_close);
        lvPaymentDetailDailog = (ListView) dialog
                .findViewById(R.id.pos_payment_detail_listview);
        profileName = (TextView) dialog
                .findViewById(R.id.pos_payment_profile_name);
        profileNumber = (TextView) dialog
                .findViewById(R.id.pos_payment_contact_number);
        profileItemCode = (TextView) dialog
                .findViewById(R.id.pos_payment_item_code);
        profileContactName = (TextView) dialog
                .findViewById(R.id.pos_payment_contact_name);

        profileMOP = (TextView) dialog
                .findViewById(R.id.pos_payment_mop);
        pos_payment_remarks = (TextView) dialog
                .findViewById(R.id.pos_payment_remarks
                );
        profilePaid = (TextView) dialog
                .findViewById(R.id.pos_payment_paid);
        profileBank = (TextView) dialog
                .findViewById(R.id.pos_payment_bank);
        profileChqNo = (TextView) dialog
                .findViewById(R.id.pos_payment_chq);
        profileDate = (TextView) dialog
                .findViewById(R.id.pos_payment_date);

        profileName.setText("" + name);
        profileNumber.setText("Moblie : " + number);

        smsLayout = (LinearLayout) dialog
                .findViewById(R.id.pos_payment_dialog_layout_sms);
        mailLayout = (LinearLayout) dialog
                .findViewById(R.id.pos_payment_dialog_layout_mail);
        printLayout = (LinearLayout) dialog
                .findViewById(R.id.pos_payment_dialog_layout_print);

        smsImage = (ImageView) dialog
                .findViewById(R.id.pos_payment_dialog_image_sms);
        mailImage = (ImageView) dialog
                .findViewById(R.id.pos_payment_dialog_image_mail);
        printImage = (ImageView) dialog
                .findViewById(R.id.pos_payment_dialog_image_print);
        pdfView = (ImageView) dialog
                .findViewById(R.id.pdfviewPayment);

        if (!statusMail) {
            mailLayout.setEnabled(false);
            mailLayout.getChildAt(1).setEnabled(false);
        }
        printLayout.setEnabled(false);
        printLayout.getChildAt(1).setEnabled(false);

        smsLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (statusSms == false) {
                    smsImage.setImageDrawable(ContextCompat.getDrawable(
                            getActivity(),
                            R.drawable.checkbox_settings));
                    statusSms = true;
                } else {
                    smsImage.setImageDrawable(ContextCompat.getDrawable(
                            getActivity(),
                            R.drawable.checkbox_blank_settings));
                    statusSms = false;
                }
            }
        });

        mailLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (statusMail == false) {
                    mailImage.setImageDrawable(ContextCompat.getDrawable(
                            getActivity(),
                            R.drawable.checkbox_settings));
                    statusMail = true;
                } else {
                    mailImage.setImageDrawable(ContextCompat.getDrawable(
                            getActivity(),
                            R.drawable.checkbox_blank_settings));
                    statusMail = false;
                }
            }
        });

        printLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (statusPrint == false) {
                    printImage.setImageDrawable(ContextCompat.getDrawable(
                            getActivity(),
                            R.drawable.checkbox_settings));
                    statusPrint = true;
                } else {
                    printImage.setImageDrawable(ContextCompat.getDrawable(
                            getActivity(),
                            R.drawable.checkbox_blank_settings));
                    statusPrint = false;
                }
            }
        });

        ok = (Button) dialog.findViewById(R.id.pos_payment_dialog_button_send);

        ok.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        ok.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (statusMail == true || statusSms == true) {
                    ok.setText("Sending...");
                    ok.setClickable(false);
                    sendMailSms();
                }
                else
                    Toast.makeText(getActivity(),
                            "Select atleast on check box", Toast.LENGTH_SHORT).show();

            }
        });

        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), POSInvoicePdfViewer.class);
                intent.putExtra("Link", Utils.getBaseUrlValue(getActivity()).replace("NewService.aspx?Page=", "") + "/PDF_RECEIPT/" + Utils.getUserId(getActivity()) + "-" + arrayList.get(position).getContactID() + "-Receipt" + arrayList.get(position).getReceiptNo().toString() + ".pdf");/*http://test.kitever.com/pdf_invoice/4-Invoice0485.pdf*/
                startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.hide();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
        searchBox.setEnabled(false);
    }

    private void fetchCustomerOrderDetails(String receiptNumber, String Name,
                                           String InvoiceId, int position) {
        if (Utils.isDeviceOnline(getActivity())) {
            showDialog(position);
            profileName.setText(Name);
            profileItemCode.setText("Receipt Number : " + receiptNumber);
            profileContactName.setText("Name : " + Name);
            Map map = new HashMap<>();

            map.put("Page", "GetPaymentDetail");
            map.put("ReceiptCode", receiptNumber);
            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);
            System.out.println("Map " + map.toString());

            try {
                new RequestManager().sendPostRequest(this,
                        KEY_FETCHCUSTOMER_ORDER_DETAIL, map);

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

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }


    private void fetchPaymentDetails() {
        if (Utils.isDeviceOnline(getActivity())) {
            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchCreditbalance");
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

    private void sendMailSms() {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(getActivity())) {
            try {
//                showLoading();
                Gson gson = new Gson();
                // Second way to create a Gson object using GsonBuilder

                Map map = new HashMap<>();
                map.put("Page", "MailMsgSendUsingChk");
                map.put("UserID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                map.put("chkSms", String.valueOf(statusSms));
                map.put("chkEmail", String.valueOf(statusMail));
                // map.put("data", gson.toJson(arrayList2));
                if (clickObject.getContact_Email() != null)
                    map.put("EmailID", clickObject.getContact_Email());

                if (clickObject.getContact_Mobile() != null)
                    map.put("ContactNo", clickObject.getContact_Mobile());

                if (clickObject.getContact_Name() != null)
                    map.put("CustomerName", clickObject.getContact_Name());
                map.put("OrderNo", ReciptNo);

                if (clickObject.getInvoiceID() != null)
                    map.put("InvoiceNo", clickObject.getInvoiceID());

                if (clickObject.getInvoiceDate() != null)
                    map.put("Invoicedt", clickObject.getInvoiceDate());

                if (clickObject.getBillAmount() != null)
                    map.put("BillAmt", clickObject.getBillAmount());

                if (clickObject.getPaidAmount() != null)
                    map.put("PaidAmount", clickObject.getPaidAmount());

                if (clickObject.getBalanceAmount() != null)
                    map.put("BalanceAmount", clickObject.getBalanceAmount());

                map.put("FromPage", "Payment");
                System.out.println("Order popup" + map.toString());

                new RequestManager().sendPostRequest(
                        POSPaymentDetailScreenFragment.this, KEY_SEND_SMS_MAIL, map);
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
            // TODO Auto-generated method stub
            Spinner spinSelected = (Spinner) parent;
            String selectedItem = (String) spinSelected.getSelectedItem();
            searchBox.setText("");

            if (selectedItem.equals("Last 50 Payments") || selectedItem.equals("")) {
                search_icon.setEnabled(false);
                searchBox.setVisibility(View.GONE);
                dateSearch.setVisibility(View.GONE);
            } else
                search_icon.setEnabled(true);

            if (paymentDetailsAdapter != null) {
                paymentDetailsAdapter.setSelectedItemType(selectedItem);
            }
            if (selectedItem.equals("Date") && paymentDetailsAdapter != null) {
                searchBox.setVisibility(View.GONE);
                search_icon.setVisibility(View.GONE);
                dateSearch.setVisibility(View.VISIBLE);
                dateImage.setVisibility(View.VISIBLE);
                dateRange.setVisibility(View.VISIBLE);
            } else if (paymentDetailsAdapter != null) {
                paymentDetailsAdapter.clearFilters();
                paymentDetailsAdapter.setSelectedItemType(selectedItem);
                search_icon.setVisibility(View.VISIBLE);
                dateSearch.setVisibility(View.GONE);
                dateImage.setVisibility(View.INVISIBLE);
                dateRange.setVisibility(View.INVISIBLE);

                if (selectedItem.equals("Paid >") || selectedItem.equals("Paid <"))
                    searchBox.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL
                            | InputType.TYPE_CLASS_NUMBER);
                else
                    searchBox.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
            paymentDetailsAdapter.setSelectedItemType(null);
        }
    };

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // TODO Auto-generated method stub

        // set selected date into textview
        paymentDetailsAdapter.dateFilter(startDatel, startDatel + 5184000000L);

        if (statusStartDate == true) {
            startDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            final int yearTemp = year, monthTemp = monthOfYear, dayTemp = dayOfMonth;
            statusEndDate = true;
            statusStartDate = false;
            c = Calendar.getInstance();
            Long maxdate;
            dateInMillis = c.getTimeInMillis();
            maxdate = (startDatel + (5184000000L)) > dateInMillis ? dateInMillis
                    : (startDatel + (5184000000L));

            dialogDate = new DatePickerDialog(getActivity(),
                    POSPaymentDetailScreenFragment.this, year, month, day);
            dialogDate.getDatePicker().setMaxDate(maxdate);// startDatel+(5184000000L)
            dialogDate.getDatePicker().setMinDate(startDatel);
            dialogDate.setTitle("End Date (max 60 days)");
            dialogDate.setButton(DialogInterface.BUTTON_NEUTRAL, "Back",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Your code
                            statusStartDate = true;

                            dialogDate = new DatePickerDialog(
                                    getActivity(),
                                    POSPaymentDetailScreenFragment.this, yearTemp,
                                    monthTemp, dayTemp);
                            if (statusStartDate == true) {
                                dialogDate.getDatePicker().setMaxDate(
                                        (new Date().getTime()));// -(5097600000L)
                                dialogDate.setTitle("Select Start Date");
                                dialogDate.show();
                            }
                        }
                    });
            dialogDate.show();
        } else if (statusEndDate == true) {
            endDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            paymentDetailsAdapter
                    .dateFilter(startDatel, endDatel + (86400000L));
            String pattern = "d-MMM, yyyy";

            SimpleDateFormat format = new SimpleDateFormat(pattern);
            dateRange.setText(format.format(new Date(startDatel)) + "  -  "
                    + format.format(new Date(endDatel)));
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_CREDIT_BALANCE) {
                System.out.println("Payment " + response);
                Gson gson = new Gson();
                PosPayment posPayment = gson.fromJson(response,
                        PosPayment.class);
                arrayList = posPayment.getCreditBalnce();
                paymentDetailsAdapter = new POSPaymentDetailAdapterFragment(this,
                        posPayment.getCreditBalnce());
                paymentDetailViewList.setAdapter(paymentDetailsAdapter);
                paymentDetailViewList
                        .setEmptyView(emptyView);
            } else if (requestId == KEY_FETCHCUSTOMER_ORDER_DETAIL) {
                // hideLoading();
                Gson gson = new Gson();

                POSPaymentDetailData paymentData = gson.fromJson(response,
                        POSPaymentDetailData.class);
                if (paymentData != null && paymentData.toString().length()>0) {
//                    customerPaymentDetailAdapter = new POSCustomerPaymentDetailAdapterFragment(
//                            paymentData.getPaymentDetail());
//                    lvPaymentDetailDailog
//                            .setAdapter(customerPaymentDetailAdapter);

                    String pattern = "MM/dd/yyyy";
                    String pattern1 = "d-MMM ,yyyy";

                    String s = "";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    try {
                        Date date = new Date(paymentData.getPaymentDetail().get(0).getReceiptDate());
                        format = new SimpleDateFormat(pattern1);
                        System.out.println("Date" + date + " new Date" + format.format(date));
                        s = format.format(date);
                    } catch (Exception c) {
                        System.out.println("Date erroer" + c.getMessage());
                    }
                    profileMOP.setText("Mode of Payment : " + paymentData.getPaymentDetail().get(0).getPayMode());
                    profilePaid.setText("Paid : " + paymentData.getPaymentDetail().get(0).getPaidAmount());
                    if (paymentData.getPaymentDetail().get(0).getPayMode().equals("Cash")) {
                        profileBank.setVisibility(View.GONE);
                        profileChqNo.setVisibility(View.GONE);
                    } else if (paymentData.getPaymentDetail().get(0).getPayMode().equals("Cheque")) {
                        profileBank.setText("Bank : " + paymentData.getPaymentDetail().get(0).getBankName());
                        profileChqNo.setText("ChqNo : " + paymentData.getPaymentDetail().get(0).getChequeNo());
                    } else {
                        profileBank.setText("Ref No : " + paymentData.getPaymentDetail().get(0).getBankName());
                        //profileChqNo.setText("Ref No : "+paymentData.getPaymentDetail().get(0).getChequeNo());
                        profileChqNo.setVisibility(View.GONE);
                    }
                    if (paymentData.getPaymentDetail().get(0).getRemarks() != null && paymentData.getPaymentDetail().get(0).getRemarks() != "")
                        pos_payment_remarks.setText("Remarks : " + paymentData.getPaymentDetail().get(0).getRemarks());
                    else pos_payment_remarks.setVisibility(View.GONE);
                    profileDate.setText("Date : " + s);
                }
            } else if (requestId == KEY_SEND_SMS_MAIL) {
                ok.setText("Resend");
                ok.setClickable(true);
                //hideLoading();
                JSONObject obj1;
                try {
                    obj1 = new JSONObject(response);
                    JSONArray objArray = obj1.getJSONArray("SendInvoice");
                    //Toast.makeText(this,objArray.getJSONObject(0).getString("Message"), 100).show();
                    Toast.makeText(getActivity(), "Send Successfully", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            showMessage("Please try again.");
            hideLoading();
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
        totalRecord.setText("Records : " + text);
    }

    @Override
    public void executeService() {
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchPaymentDetails();
            }
        }, 500);
    }

    private void showMessage(String str) {
        Toast.makeText(getActivity(),str, Toast.LENGTH_SHORT).show();
    }

    private boolean ChangeStatus(boolean b)
    {
        if(b) return false;
        else return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvPaymentDateLabel:
                paymentSorting(1,date);
                setTitleText();
                if(date) tvPaymentDateLabel.setText(getActivity().getString(R.string.titledateAsc));
                else tvPaymentDateLabel.setText(getActivity().getString(R.string.titledateDesc));
                date = ChangeStatus(date);
                break;
            case R.id.tvPaymentInvoiceLabel:
                paymentSorting(2,invoice);
                setTitleText();
                if(invoice) tvPaymentInvoiceLabel.setText(getActivity().getString(R.string.titleinvoiceAsc));
                else tvPaymentInvoiceLabel.setText(getActivity().getString(R.string.titleinvoiceDesc));
                invoice = ChangeStatus(date);
                break;
            case R.id.tvPaymentReceiptLabel:
                paymentSorting(3,receipt);
                setTitleText();
                if(receipt) tvPaymentReceiptLabel.setText(getActivity().getString(R.string.titlereceiptAsc));
                else tvPaymentReceiptLabel.setText(getActivity().getString(R.string.titlereceiptDesc));
                receipt = ChangeStatus(receipt);
                break;
            case R.id.tvPaymentCustomerLabel:
                paymentSorting(4,customer);
                setTitleText();
                if(customer) tvPaymentCustomerLabel.setText(getActivity().getString(R.string.titlecustomerAsc));
                else tvPaymentCustomerLabel.setText(getActivity().getString(R.string.titlecustomerDesc));
                customer = ChangeStatus(customer);
                break;
            case R.id.tvPaymentPaidLabel:
                paymentSorting(5,paid);
                setTitleText();
                if(paid) tvPaymentPaidLabel.setText(getActivity().getString(R.string.titlepaidAsc));
                else tvPaymentPaidLabel.setText(getActivity().getString(R.string.titlepaidDesc));
                paid = ChangeStatus(paid);
                break;
            case R.id.payment_export:
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
        tvPaymentDateLabel.setText("Date");
        tvPaymentInvoiceLabel.setText("Invoice");
        tvPaymentReceiptLabel.setText("Receipt");
        tvPaymentCustomerLabel.setText("Name");
        tvPaymentPaidLabel.setText("Paid");
    }

    private void paymentSorting(int n,boolean b)
    {
        if(arrayList!=null && arrayList.size()>0)
        {
            Collections.sort(paymentDetailsAdapter.getList(),new PaymentComparator(n,b));
            paymentDetailsAdapter.notifyDataSetChanged();
        }
    }

    private void ExportCSV() throws IOException {
        ArrayList<CreditBalnce> paymentList=paymentDetailsAdapter.getList();

        String path = Environment.getExternalStorageDirectory().getPath();
        long current_time = System.currentTimeMillis();
        File dir = new File(path + "/"+getString(R.string.app_name)+"/csv/");
        if (!dir.exists())  dir.mkdirs();
        File csvfile = new File(dir+"/PaymentList_" + current_time + ".csv");

        FileWriter writer = new FileWriter(csvfile);

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Date", "Order", "Customer","Amount","Paid"));

        for (CreditBalnce d : paymentList) {

            List<String> list = new ArrayList<>();
            // list.add(d.getOrderDate());
            try {
                String pattern = "MM/dd/yyyy";
                Date date =  new Date(Long.parseLong(d.getPaymentDate()));
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                list.add(format.format(date));
            }catch(Exception c)
            {
                System.out.println("Date erroer"+c.getMessage());
            }
            list.add(d.getInvoiceID());
            list.add(d.getReceiptNo());
            list.add(d.getContact_Name());
            list.add(d.getPaidAmount());

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
