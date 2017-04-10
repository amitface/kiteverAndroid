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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.kitever.pos.fragment.PosComparators.InvoiceComparator;
import com.kitever.pos.fragment.adapters.POSInvoiceAdapterFragment;
import com.kitever.pos.fragment.adapters.POSInvoiceDetailAdapterFragment;
import com.kitever.pos.model.data.FetchInvoice;
import com.kitever.pos.model.data.POSInvoicePopUp;
import com.kitever.pos.model.data.PosInvoiceData;
import com.kitever.pos.model.data.ReceiptDetail;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;

//import com.google.android.gms.ads.formats.NativeAd;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link POSInvoiceScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class POSInvoiceScreenFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener, NetworkManager, TotalRows, ExecuteService {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String UserId;
    private String UserLogin;
    private String PassWord;
    private int oldInvoice;

    String link = "https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf";
    private int KEY_FETCH_ALL_INVOICE_DETAILS = 1, KEY_SEND_SMS_MAIL = 2, KEY_FETCH_INVOICE_DETAILS = 3;
    private long startDatel, endDatel;
    private LinearLayout searchLayout, actionLayout, dateRangeLayout;
    private ListView invoiceListView, invoiceDetailListView;
    private POSInvoiceAdapterFragment invoiceAdapter;
    private POSInvoiceDetailAdapterFragment invoiceDetailAdapter;
    private ArrayList<ReceiptDetail> arr;
    private ArrayList<FetchInvoice> invoiceList;

    private EditText searchBox;
    private TextView startDateLable, startDate, endDateLabel, endDate, totalRows, dateRange, noRecordImage;
    private ImageView advanceSearch, smsSelect, mailSelect, printSelect, dateImage;
    private SpinnerReselect dateSearch;
    private boolean statusSms = true, statusMail = true, statusPrint = true;
    private int dateSelected = 1;
    private Button button;
    private boolean statusStartDate = false, statusEndDate = false;

    //Dialog Data
    private TextView contactName, invoiceCode, invoiceContact;
    private ImageView profileImage, pdfview;

    private SimpleDateFormat format;

    private DatePickerDialog dialogDate;
    private Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    private static Long dateInMillis;
    private RelativeLayout emptyView;
    private Dialog dialog;
    private ImageView search_icon;
    private boolean IssearchBoxOpen = false;
    private ProgressBar progressBar;
    private Spinner selectType;

    private TextView dateTitle, invoiceTitle, customerTitle, billTitle;
    private ImageView invoice_export;
    private boolean dateAsc = false, invoiceAsc = false, CustomerAsc = false, billAsc = false;

    public POSInvoiceScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment POSInvoiceScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static POSInvoiceScreenFragment newInstance(String param1, String param2, String param3) {
        POSInvoiceScreenFragment fragment = new POSInvoiceScreenFragment();
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
        View view = inflater.inflate(R.layout.pos_invoice_layout, container, false);
        setScreen(view);
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchInvoiceALL();
                selectType.setSelection(0);
                IssearchBoxOpen = false;
                searchBox.setText("");
                searchBox.setVisibility(View.GONE);
            }
        }, 500);
        return view;
    }

    private void setScreen(View view) {

        selectType = (Spinner) view.findViewById(R.id.select_type_invoice_spinner);
        searchBox = (EditText) view.findViewById(R.id.edit_invoice_search);

        //dateRangeLayout = (LinearLayout) view.findViewById(R.id.pos_invoice_date_range_layout);
        actionLayout = (LinearLayout) view.findViewById(R.id.pos_invoice_action_layout);
        searchLayout = (LinearLayout) view.findViewById(R.id.pos_invoice_search_layout);
        advanceSearch = (ImageView) view.findViewById(R.id.advance_invoice_search);
        dateSearch = (SpinnerReselect) view.findViewById(R.id.date_invoice_search);
        noRecordImage = (TextView) view.findViewById(R.id.no_record);
        emptyView = (RelativeLayout) view.findViewById(R.id.invoice_emptyElement);

        MoonIcon mIcon = new MoonIcon(this);
        mIcon.setTextfont(noRecordImage);


        dateTitle = (TextView) view.findViewById(R.id.dateTitle);
        invoiceTitle = (TextView) view.findViewById(R.id.invoiceTitle);
        customerTitle = (TextView) view.findViewById(R.id.customerTitle);
        billTitle = (TextView) view.findViewById(R.id.billTitle);

        invoice_export = (ImageView) view.findViewById(R.id.invoice_export);

        dateTitle.setOnClickListener(this);
        invoiceTitle.setOnClickListener(this);
        customerTitle.setOnClickListener(this);
        billTitle.setOnClickListener(this);
        invoice_export.setOnClickListener(this);
        mIcon.setTextfont(dateTitle);
        mIcon.setTextfont(invoiceTitle);
        mIcon.setTextfont(customerTitle);
        mIcon.setTextfont(billTitle);
        dateRange = (TextView) view.findViewById(R.id.pos_invoice_date_range);
        dateImage = (ImageView) view.findViewById(R.id.invoice_image_date);
        invoiceListView = (ListView) view.findViewById(R.id.pos_invoice_list_view);
        invoiceListView.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));
        smsSelect = (ImageView) view.findViewById(R.id.invoice_image_sms);
        mailSelect = (ImageView) view.findViewById(R.id.invoice_image_mail);
        printSelect = (ImageView) view.findViewById(R.id.invoice_image_print);
        totalRows = (TextView) view.findViewById(R.id.invoice_total_rows);
        button = (Button) view.findViewById(R.id.invoice_button_send);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sendMailSms(invoiceAdapter.getArrayList());
            }
        });

        button.setEnabled(false);

       /* invoiceList = new ArrayList<>();
        invoiceAdapter = new POSInvoiceAdapterFragment(this,invoiceList);
        invoiceListView.setAdapter(invoiceAdapter);
*/

        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("Last 50 Invoice");
        typeList.add("Invoice Code");
        typeList.add("Contact");
        typeList.add("Bill Amount <=");
        typeList.add("Bill Amount >=");

        typeList.add("Balance Amount <=");
        typeList.add("Balance Amount >=");
        typeList.add("Order Date");


        RelativeLayout page_main_layout=(RelativeLayout)view.findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        totalRows.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        dateRange.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        searchBox.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        setRobotoThinFont(totalRows,getActivity());
        setRobotoThinFont(dateRange,getActivity());
        setRobotoThinFont(searchBox,getActivity());

        dateTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        invoiceTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        customerTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        billTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        progressBar = (ProgressBar) view.findViewById(R.id.marker_progress);
        progressBar.setVisibility(View.GONE);

        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                if (invoiceAdapter != null) {
                    invoiceAdapter.getFilter()
                            .filter(cs);
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

        smsSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (statusSms) {
                    statusSms = false;
                    smsSelect.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.checkbox_blank_settings));
                } else {
                    statusSms = true;
                    smsSelect.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.checkbox_settings));
                }
            }
        });

        mailSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (statusMail) {
                    statusMail = false;
                    mailSelect.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.checkbox_blank_settings));
                } else {
                    statusMail = true;
                    mailSelect.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.checkbox_settings));
                }

            }
        });

        printSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (statusPrint) {
                    statusPrint = false;
                    printSelect.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.checkbox_blank_settings));
                } else {
                    statusPrint = true;
                    printSelect.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.checkbox_settings));
                }
            }
        });

        invoiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                showDialogInvoice(position);
            }
        });


        invoiceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                invoiceListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                // Capture ListView item click

                invoiceListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        // TODO Auto-generated method stub
                        invoiceAdapter.emptyArray();
                        searchLayout.setVisibility(View.VISIBLE);
                        actionLayout.setVisibility(View.GONE);
                        totalRows.setVisibility(View.VISIBLE);
                        button.setEnabled(false);
                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        // TODO Auto-generated method stub
//						  mode.getMenuInflater().inflate(R.menu.pos_invoice_action_setting, menu);
                        searchLayout.setVisibility(View.GONE);
                        actionLayout.setVisibility(View.VISIBLE);
                        totalRows.setVisibility(View.GONE);
                        button.setEnabled(true);
                        return true;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        // TODO Auto-generated method stub
                        if (item.getItemId() == R.id.invoice_send) {

                            mode.finish();
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                          long id, boolean checked) {
                        // TODO Auto-generated method stub
                        mode.setTitle(invoiceListView.getCheckedItemCount() + " Selected");

                        // Toggle the state of item after every click on it
                        invoiceAdapter.toggleSelection(position);
                    }
                });

                return statusMail;
            }
        });

        dateSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
//				System.out.println("selected");
                String pattern = "d-MMM, yyyy";
                format = new SimpleDateFormat(pattern);

                if (position == 1) {
                    invoiceAdapter.dateRangeFilter(position);
                    dateRange.setText("" + DateHelper.getTodayDate());

                } else if (position == 2) {
                    invoiceAdapter.dateRangeFilter(position);
                    dateRange.setText("" + DateHelper.getYestardayDate(dateInMillis));
                } else if (position == 3) {
                    invoiceAdapter.dateRangeFilter(position);
                    if (dateInMillis != null)
                        dateRange.setText(DateHelper.getFirstDayOfWeek() + " - " + format.format(new Date(dateInMillis)));
                } else if (position == 4) {
                    invoiceAdapter.dateRangeFilter(position);
                    dateRange.setText(DateHelper.getFirstDayOfMonth() + " - " + format.format(new Date(dateInMillis)));
                } else if (position == 5) {
                    invoiceAdapter.dateRangeFilter(position);

                    dateRange.setText(DateHelper.getLastMonth());
                } else if (position == 6) {
                    statusStartDate = true;
                    dialogDate = new DatePickerDialog(getActivity(), POSInvoiceScreenFragment.this, year, month, day);
                    if (statusStartDate == true) {
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

    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            // TODO Auto-generated method stub
            Spinner spinSelected = (Spinner) parent;
            String selectedItem = (String) spinSelected.getSelectedItem();

            searchBox.setText("");

            if (selectedItem.equals("Last 50") || selectedItem.equals("")) {
                search_icon.setEnabled(false);
                searchBox.setVisibility(View.GONE);
                dateSearch.setVisibility(View.GONE);
            } else search_icon.setEnabled(true);

            if (invoiceAdapter != null && selectedItem.equals("Order Date")) {
                searchBox.setVisibility(View.GONE);
                search_icon.setVisibility(View.GONE);
                dateSearch.setVisibility(View.VISIBLE);
                dateRange.setVisibility(View.VISIBLE);
                dateRange.setText("Last 50 Records");
                dateImage.setVisibility(View.VISIBLE);
            } else if (invoiceAdapter != null) {
                invoiceAdapter.clearFilters();
                invoiceAdapter.setSelectedItemType(selectedItem);
                search_icon.setVisibility(View.VISIBLE);
                dateSearch.setVisibility(View.GONE);
                dateImage.setVisibility(View.INVISIBLE);
                dateRange.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }
    };

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showDialogInvoice(final int position) {
        // TODO Auto-generated method stub
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pos_invoice_popup);
        dialog.setTitle("Invoice Detail");
        oldInvoice = position;
        profileImage = (ImageView) dialog.findViewById(R.id.pos_invoice_item_image);
        invoiceDetailListView = (ListView) dialog.findViewById(R.id.pos_invoice_detail_listview);

        Button textView = (Button) dialog.findViewById(R.id.pos_invoice_detail_close);
        contactName = (TextView) dialog.findViewById(R.id.pos_invoice_contact_name);
        invoiceCode = (TextView) dialog.findViewById(R.id.pos_invoice_item_code);

        invoiceContact = (TextView) dialog.findViewById(R.id.pos_invoice_contact_number);

        contactName.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        invoiceCode.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        invoiceContact.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        setRobotoThinFont(contactName,getActivity());
        setRobotoThinFont(invoiceCode,getActivity());
        setRobotoThinFont(invoiceContact,getActivity());
        setRobotoThinFontButton(textView,getActivity());

        textView.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        textView.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        pdfview = (ImageView) dialog.findViewById(R.id.pdfviewInvoice);
        invoiceCode.setText("Invoice No:" + invoiceAdapter.getAllArrayList().get(position).getInvoiceCode());
        contactName.setText("Name: " + invoiceAdapter.getAllArrayList().get(position).getCustomerName());
        invoiceContact.setText("Number: " + invoiceAdapter.getAllArrayList().get(position).getContact_Mobile());
        fetchInvoiceDetail(position);

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.hide();
                statusPrint = false;
                statusSms = false;
                statusMail = false;
            }
        });

        pdfview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), POSInvoicePdfViewer.class);
                /*intent.putExtra("Link","http://test.kitever.com/pdf_invoice/4-Invoice0485.pdf");*/
                intent.putExtra("Link", Utils.getBaseUrlValue(getActivity()).replace("NewService.aspx?Page=", "") + "/pdf_invoice/" + Utils.getUserId(getActivity()) + "-" + invoiceList.get(position).getContactID() + "-Invoice" + invoiceList.get(position).getInvoiceCode().toString() + ".pdf");/*http://test.kitever.com/pdf_invoice/4-Invoice0485.pdf*/
                startActivity(intent);
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private void fetchInvoiceALL() {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(getActivity())) {
            try {
//                showLoading();
                Map map = new HashMap<>();
                map.put("Page", "FetchInvoice");
//				map.put("userID", "589");
//				map.put("UserLogin","+918512851664");
//				map.put("Password","103641");

                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);

                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_ALL_INVOICE_DETAILS, map);
            } catch (Exception e) {

            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    private void fetchInvoiceDetail(int position) {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(getActivity())) {
            try {
//                showLoading();
                Map map = new HashMap<>();
                map.put("Page", "GetReceiptDetail");
//				map.put("userID", "589");
//				map.put("UserLogin","+918512851664");
//				map.put("Password","103641");

                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                map.put("InvoiceNo", invoiceAdapter.getAllArrayList().get(position).getInvoiceCode());

                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_INVOICE_DETAILS, map);
            } catch (Exception e) {

            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    private void sendMailSms(ArrayList<FetchInvoice> arrayList) {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(getActivity())) {
            try {
                Gson gson = new Gson();

                showLoading();
                Map map = new HashMap<>();
                map.put("Page", "MailMsgSendUsingChk");
                map.put("chkSms", String.valueOf(statusSms));
                map.put("chkEmail", String.valueOf(statusMail));
                JSONArray array = new JSONArray(gson.toJson(arrayList));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("SmsMail", array);
                map.put("data", jsonObject.toString());
//				System.out.println("Sms "+arrayList.toString()+" map "+map.toString());
                new RequestManager().sendPostRequest(this,
                        KEY_SEND_SMS_MAIL, map);
            } catch (Exception e) {

            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    private void setTaxScreen() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.invoice_filter_layout);
        dialog.setTitle("Filters List");
        startDateLable = (TextView) dialog.findViewById(R.id.invoice_start_date);
        startDate = (TextView) dialog.findViewById(R.id.invoice_date);
        endDateLabel = (TextView) dialog.findViewById(R.id.invoice_end_date_lable);
        endDate = (TextView) dialog.findViewById(R.id.invoice_end_date);

        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);


        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dateSelected = 1;                // Create a new instance of DatePickerDialog and return it
                DatePickerDialog dialogDate = new DatePickerDialog(getActivity(), POSInvoiceScreenFragment.this, year, month, day);
                dialogDate.getDatePicker().setMaxDate(new Date().getTime());
                dialogDate.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dateSelected = 0;

                // Create a new instance of DatePickerDialog and return it
                DatePickerDialog dialogDate = new DatePickerDialog(getActivity(), POSInvoiceScreenFragment.this, year, month, day);
                dialogDate.getDatePicker().setMaxDate(new Date().getTime());

                dialogDate.show();
//
            }
        });
        Button doneBtn = (Button) dialog.findViewById(R.id.invoice_done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                invoiceAdapter.dateFilter(startDatel, endDatel + 86400000);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // TODO Auto-generated method stub

        if (statusStartDate == true) {
            startDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            final int yearTemp = year, monthTemp = monthOfYear, dayTemp = dayOfMonth;
            statusEndDate = true;
            statusStartDate = false;

            dialogDate = new DatePickerDialog(getActivity(), POSInvoiceScreenFragment.this, year, month, day);
            c = Calendar.getInstance();
            Long maxdate;
            dateInMillis = c.getTimeInMillis();
            maxdate = (startDatel + (5184000000L)) > dateInMillis ? dateInMillis : (startDatel + (5184000000L));
            dialogDate.getDatePicker().setMaxDate(maxdate);
            dialogDate.getDatePicker().setMinDate(startDatel);
            dialogDate.setTitle("End Date (max 60 days)");
            dialogDate.setButton(DialogInterface.BUTTON_NEUTRAL, "Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Your code
                    statusStartDate = true;

                    dialogDate = new DatePickerDialog(getActivity(), POSInvoiceScreenFragment.this, yearTemp, monthTemp, dayTemp);
                    if (statusStartDate == true) {
                        dialogDate.getDatePicker().setMaxDate(new Date().getTime());//-(5097600000L))
                        dialogDate.setTitle("Select Start Date");
                        dialogDate.show();
                    }
                }
            });
            dialogDate.show();
        } else if (statusEndDate == true) {
            endDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            invoiceAdapter.dateFilter(startDatel, endDatel + (86400000L));
            String pattern = "d-MMM, yyyy";

            SimpleDateFormat format = new SimpleDateFormat(pattern);
            dateRange.setText(format.format(new Date(startDatel)) + "  -  " + format.format(new Date(endDatel)));
        }

    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_ALL_INVOICE_DETAILS) {

                Gson gson = new Gson();
                PosInvoiceData vc = gson.fromJson(response, PosInvoiceData.class);
                invoiceList = new ArrayList<>();

                for (int i = 0; i < vc.getFetchInvoice().size(); i++) {
                    invoiceList.add(vc.getFetchInvoice().get(i));
                }

                invoiceAdapter = new POSInvoiceAdapterFragment(this, invoiceList);
                invoiceListView.setAdapter(invoiceAdapter);
                //invoiceAdapter.notifyDataSetChanged();
                invoiceListView.setEmptyView(emptyView);
            } else if (requestId == KEY_SEND_SMS_MAIL) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("SendInvoice");
                    JSONObject jsonObject2 = array.getJSONObject(0);
                    Toast.makeText(getActivity(), jsonObject2.getString("Message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (requestId == KEY_FETCH_INVOICE_DETAILS) {
                Gson gson = new Gson();
                POSInvoicePopUp receiptDetail = gson.fromJson(response, POSInvoicePopUp.class);

                arr = receiptDetail.getReceiptDetail();
                invoiceDetailAdapter = new POSInvoiceDetailAdapterFragment(this, arr);
                invoiceDetailListView.setAdapter(invoiceDetailAdapter);
                invoiceDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), POSInvoicePdfViewer.class);
                /*intent.putExtra("Link","http://test.kitever.com/pdf_invoice/4-Invoice0485.pdf");*/
                        intent.putExtra("Link", Utils.getBaseUrlValue(getActivity()).replace("NewService.aspx?Page=", "") + "/PDF_RECEIPT/" + Utils.getUserId(getActivity()) + "-" + invoiceList.get(oldInvoice).getContactID() + "-Receipt" + arr.get(position).getReceiptNo() + ".pdf");/*http://test.kitever.com/pdf_invoice/4-Invoice0485.pdf*/
                        startActivity(intent);
                    }
                });
            }
        } else {

        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        hideLoading();
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void totalRows(String text) {
        // TODO Auto-generated method stub
        totalRows.setText("Records : " + text);
    }

    @Override
    public void executeService() {
        dateInMillis = DateHelper.getDate(year, month, day);
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchInvoiceALL();
                selectType.setSelection(0);
                IssearchBoxOpen = false;
                searchBox.setText("");
                searchBox.setVisibility(View.GONE);
            }
        }, 500);
    }

    private void showMessage(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    private boolean ChangeStatus(boolean b) {
        if (b) return false;
        else return true;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.dateTitle:
                orderSorting(1, dateAsc);
                setTitleText();
                if (dateAsc) dateTitle.setText(getActivity().getString(R.string.titledateAsc));
                else dateTitle.setText(getActivity().getString(R.string.titledateDesc));
                dateAsc = ChangeStatus(dateAsc);
                break;
            case R.id.invoiceTitle:
                orderSorting(3, invoiceAsc);
                setTitleText();
                if (invoiceAsc)
                    invoiceTitle.setText(getActivity().getString(R.string.titleinvoiceAsc));
                else invoiceTitle.setText(getActivity().getString(R.string.titleinvoiceDesc));
                invoiceAsc = ChangeStatus(invoiceAsc);
                break;
            case R.id.customerTitle:
                orderSorting(2, CustomerAsc);
                setTitleText();
                if (CustomerAsc)
                    customerTitle.setText(getActivity().getString(R.string.titlecustomerAsc));
                else customerTitle.setText(getActivity().getString(R.string.titlecustomerDesc));
                CustomerAsc = ChangeStatus(CustomerAsc);
                break;
            case R.id.billTitle:
                orderSorting(4, billAsc);
                setTitleText();
                if (billAsc) billTitle.setText(getActivity().getString(R.string.titleamtAsc));
                else billTitle.setText(getActivity().getString(R.string.titleamtDesc));
                billAsc = ChangeStatus(billAsc);
                break;

            case R.id.invoice_export:
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
        invoiceTitle.setText("Invoice");
        customerTitle.setText("Name");
        billTitle.setText("Amount");

    }

    private void orderSorting(Integer field, boolean status) {
        ArrayList<FetchInvoice> InvoiceList = invoiceAdapter.getInvoiceList();
        if (InvoiceList.size() > 0) {
            Collections.sort(InvoiceList, new InvoiceComparator(field, status));
            invoiceAdapter.notifyDataSetChanged();
        }
    }

    private void ExportCSV() throws IOException {
        ArrayList<FetchInvoice> InvoiceList = invoiceAdapter.getInvoiceList();
        String path = Environment.getExternalStorageDirectory().getPath();
        long current_time = System.currentTimeMillis();
        File dir = new File(path + "/" + getString(R.string.app_name) + "/csv/");
        if (!dir.exists()) dir.mkdirs();
        File csvfile = new File(dir + "/InvoiceList_" + current_time + ".csv");

        FileWriter writer = new FileWriter(csvfile);

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Date", "Name", "Invoice", "Bill", "Paid", "Balance"));

        for (FetchInvoice d : InvoiceList) {

            List<String> list = new ArrayList<>();
            // list.add(d.getOrderDate());
            try {
                String pattern = "MM/dd/yyyy";
                Date date = new Date(d.getOrderDate());
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                list.add(format.format(date));
            } catch (Exception c) {
                System.out.println("Date erroer" + c.getMessage());
            }

            list.add(d.getContactName());
            list.add(d.getInvoiceCode());
            list.add(d.getBillAmount());
            list.add(d.getBalanceAmount());

            CSVUtils.writeLine(writer, list);

            //try custom separator and quote.
            //CSVUtils.writeLine(writer, list, '|', '\"');
        }

        writer.flush();
        writer.close();
        Toast.makeText(getActivity(), "csv downloaded", Toast.LENGTH_LONG).show();
        opencsv(csvfile);

    }

    private void opencsv(final File file) {
        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setMessage("CSV saved successfully in " + file.toString() + " do you want to open it? ")
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
                                } catch (ActivityNotFoundException e) {

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
