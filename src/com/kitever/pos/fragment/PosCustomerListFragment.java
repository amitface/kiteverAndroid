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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.kitever.app.context.PermissionClass;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.fragment.PosComparators.CSVUtils;
import com.kitever.pos.fragment.PosComparators.CustomerComparator;
import com.kitever.pos.fragment.adapters.POSCustomerAdapterFragment;
import com.kitever.pos.fragment.adapters.POSCustomerPopupAdapter;
import com.kitever.pos.model.CustomerList;
import com.kitever.pos.model.Order;
import com.kitever.pos.model.PosCustomerPopup;
import com.kitever.pos.model.data.CustomerObject;
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

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PosCustomerListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PosCustomerListFragment extends Fragment implements NetworkManager, DatePickerDialog.OnDateSetListener, TotalRows, ExecuteService, AdapterView.OnItemClickListener, View.OnClickListener {
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

    private final int KEY_FETCH_CUSTOMER_LIST = 1;
    private final int KEY_FETCH_CUSTOMER_Popup_LIST = 2;
    private Spinner spinner;
    private SpinnerReselect dateSearch;
    private EditText etSearch;
    private TextView totalRows, dateRange, noRecord;
    private ImageView dateImage;
    private POSCustomerAdapterFragment customerAdapter;
    private ListView lvPOSCustomer;
    private ArrayList<CustomerList> customerList;
    private long startDatel, endDatel;
    private boolean statusEndDate = false, statusStartDate = false;
    private LinearLayout dateRangeLayout;
    private DatePickerDialog dialogDate;
    private SimpleDateFormat format;
    private Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    private Long dateInMillis;
    private RelativeLayout emptyView;
    private ImageView search_icon, imageCustomerExport;
    private boolean IssearchBoxOpen = false;
    private ProgressBar progressBar;
    private Dialog dialog;
    private RecyclerView recyclerPosCustomerDetail;
    private ArrayList<Order> orders;
    private TextView posCustomerName, posCustomerNumber, posCustomerEmail;
    private RequestManager requestManager;
    private boolean name = false, order = false, business = false, credit = false, date = false;
    private TextView nameTitle,orderTitle,businessTitle,creditTitle,dateTitle;
    private FrameLayout frameLayoutPosCustomerName, frameLayoutPosCustomerOrder, frameLayoutPosCustomerBusiness, frameLayoutPosCustomerCredit, frameLayoutPosCustomerDate;

    public PosCustomerListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PosCustomerListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PosCustomerListFragment newInstance(String param1, String param2, String param3) {
        PosCustomerListFragment fragment = new PosCustomerListFragment();
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
        View view = inflater.inflate(R.layout.activity_pos_customer_list, container, false);
        setScreen(view);
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchCustomerList();
                spinner.setSelection(0);
                IssearchBoxOpen = false;
            }
        }, 500);
        return view;
    }

    private void setScreen(View view) {
        // TODO Auto-generated method stub
        etSearch = (EditText) view.findViewById(R.id.customer_edit_search);
        spinner = (Spinner) view.findViewById(R.id.customer_type_spinner);
        dateSearch = (SpinnerReselect) view.findViewById(R.id.date_customer_search);
        lvPOSCustomer = (ListView) view.findViewById(R.id.pos_customer_list_view);
        lvPOSCustomer.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));
        lvPOSCustomer.setOnItemClickListener(this);

        RelativeLayout page_main_layout=(RelativeLayout)view.findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        totalRows = (TextView) view.findViewById(R.id.customer_total_rows);

        nameTitle = (TextView) view.findViewById(R.id.nameTitle);
        orderTitle = (TextView) view.findViewById(R.id.orderTitle);
        businessTitle = (TextView) view.findViewById(R.id.businessTitle);
        creditTitle = (TextView) view.findViewById(R.id.creditTitle);
        dateTitle = (TextView) view.findViewById(R.id.dateTitle);

        totalRows.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        nameTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        orderTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        businessTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        creditTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        dateTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etSearch.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        totalRows.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        setRobotoThinFont(totalRows,getActivity());
        setRobotoThinFont(nameTitle,getActivity());
        setRobotoThinFont(orderTitle,getActivity());
        setRobotoThinFont(businessTitle,getActivity());
        setRobotoThinFont(creditTitle,getActivity());
        setRobotoThinFont(dateTitle,getActivity());
        setRobotoThinFont(etSearch,getActivity());
        setRobotoThinFont(totalRows,getActivity());

        imageCustomerExport = (ImageView) view.findViewById(R.id.imageCustomerExport);
        frameLayoutPosCustomerName = (FrameLayout) view.findViewById(R.id.framelayoutPosCustomerName);
        frameLayoutPosCustomerOrder = (FrameLayout) view.findViewById(R.id.framelayoutPosCustomerOrder);
        frameLayoutPosCustomerBusiness = (FrameLayout) view.findViewById(R.id.framelayoutPosCustomerBusiness);
        frameLayoutPosCustomerCredit = (FrameLayout) view.findViewById(R.id.framelayoutPosCustomerCredit);
        frameLayoutPosCustomerDate = (FrameLayout) view.findViewById(R.id.framelayoutPosCustomerDate);

        frameLayoutPosCustomerName.setOnClickListener(this);
        frameLayoutPosCustomerOrder.setOnClickListener(this);
        frameLayoutPosCustomerBusiness.setOnClickListener(this);
        frameLayoutPosCustomerCredit.setOnClickListener(this);
        frameLayoutPosCustomerDate.setOnClickListener(this);
        imageCustomerExport.setOnClickListener(this);

        dateImage = (ImageView) view.findViewById(R.id.pos_customer_image_date);
        dateRange = (TextView) view.findViewById(R.id.pos_customer_date_range);
        dateRange.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        setRobotoThinFont(dateRange,getActivity());
        //  dateRangeLayout = (LinearLayout) view.findViewById(R.id.pos_invoice_date_range_layout);

        emptyView = (RelativeLayout) view.findViewById(R.id.customer_emptyElement);
        noRecord = (TextView) view.findViewById(R.id.no_record);
        MoonIcon mIcon = new MoonIcon(this);
        mIcon.setTextfont(noRecord);

/*		customerList = new ArrayList<>();
		customerAdapter = new POSCustomerAdapter(customerList, this);
		lvPOSCustomer.setAdapter(customerAdapter);*/

        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("Top 50 Customers");
        typeList.add("Contact");
        typeList.add("Credit >");
        typeList.add("Order Date");
        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(typeAdapter);
        spinner.setOnItemSelectedListener(itemSelectedListener);
        progressBar = (ProgressBar) view.findViewById(R.id.marker_progress);
        progressBar.setVisibility(View.GONE);
        search_icon = (ImageView) view.findViewById(R.id.search_icon);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IssearchBoxOpen) {
                    etSearch.setVisibility(View.GONE);
                    IssearchBoxOpen = false;
                } else {
                    etSearch.setVisibility(View.VISIBLE);
                    IssearchBoxOpen = true;
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (customerAdapter != null)
                    customerAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

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
                    customerAdapter.dateRangeFilter(position);
                    dateRange.setText("" + DateHelper.getTodayDate());

                } else if (position == 2) {
                    customerAdapter.dateRangeFilter(position);
                    dateRange.setText("" + DateHelper.getYestardayDate(DateHelper.getDateInmillis()));
                } else if (position == 3) {
                    customerAdapter.dateRangeFilter(position);
                    dateRange.setText(DateHelper.getFirstDayOfWeek() + " - " + format.format(new Date(DateHelper.getDateInmillis())));
                } else if (position == 4) {
                    customerAdapter.dateRangeFilter(position);
                    dateRange.setText(DateHelper.getFirstDayOfMonth() + " - " + format.format(new Date(DateHelper.getDateInmillis())));
                } else if (position == 5) {
                    customerAdapter.dateRangeFilter(position);

                    dateRange.setText(DateHelper.getLastMonth());
                } else if (position == 6) {
                    statusStartDate = true;
                    dialogDate = new DatePickerDialog(getActivity(), PosCustomerListFragment.this, year, month, day);
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
        //etSearch.setEnabled(false);
    }

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            // TODO Auto-generated method stub
            Spinner spinSelected = (Spinner) parent;
            String selectedItem = (String) spinSelected.getSelectedItem();

            etSearch.setText("");
            if (selectedItem.equals("Top 50 Customers") || selectedItem.equals("")) {
                etSearch.setVisibility(View.GONE);
                search_icon.setEnabled(false);
                dateSearch.setVisibility(View.GONE);
            } else search_icon.setEnabled(true);


            if (selectedItem.equals("Order Date") && customerAdapter != null) {

                etSearch.setVisibility(View.GONE);
                search_icon.setVisibility(View.GONE);
                dateSearch.setVisibility(View.VISIBLE);
                dateImage.setVisibility(View.VISIBLE);
                dateRange.setVisibility(View.VISIBLE);
            } else if (customerAdapter != null) {

                customerAdapter.clearFilters();
                customerAdapter.setSelectedItemType(selectedItem);
                search_icon.setVisibility(View.VISIBLE);
                dateSearch.setVisibility(View.GONE);
                dateImage.setVisibility(View.INVISIBLE);
                dateRange.setVisibility(View.INVISIBLE);
                if (selectedItem.equals("Credit >"))
                    etSearch.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                else
                    etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }
    };


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // TODO Auto-generated method stub

        if (statusStartDate == true) {

            startDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            final int yearTemp = year, monthTemp = monthOfYear, dayTemp = dayOfMonth;
            statusEndDate = true;
            statusStartDate = false;
            dialogDate = null;
            dialogDate = new DatePickerDialog(getActivity(), PosCustomerListFragment.this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            c = Calendar.getInstance();
            Long maxdate;
            dateInMillis = c.getTimeInMillis();
            maxdate = (startDatel + (5184000000L)) > dateInMillis ? dateInMillis : (startDatel + (5184000000L));
            dialogDate.getDatePicker().setMaxDate(maxdate);//startDatel+(5184000000L)
            dialogDate.getDatePicker().setMinDate(startDatel);

            dialogDate.setButton(DialogInterface.BUTTON_NEUTRAL, "Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Your code
                    statusStartDate = true;

                    dialogDate = new DatePickerDialog(getActivity(), PosCustomerListFragment.this, yearTemp, monthTemp, dayTemp);
                    if (statusStartDate == true) {
                        dialogDate.getDatePicker().setMaxDate((new Date().getTime()));//-(5097600000L)
                        dialogDate.setTitle("Select Start Date");
                        dialogDate.show();
                    }
                }
            });
            dialogDate.show();
        } else if (statusEndDate == true) {
            endDatel = DateHelper.getDate(year, monthOfYear, dayOfMonth);
            customerAdapter.dateFilter(startDatel, endDatel + (86400000L));
            String pattern = "d-MMM, yyyy";

            SimpleDateFormat format = new SimpleDateFormat(pattern);
            dateRange.setText(format.format(new Date(startDatel)) + "  -  " + format.format(new Date(endDatel)));
        }
    }


    private void fetchCustomerList() {

        if (Utils.isDeviceOnline(getActivity())) {
            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchCustomerList");
            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);
            System.out.println("Customer list");
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_CUSTOMER_LIST, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    private void fetchCustomerListPopup(int position) {
        if (Utils.isDeviceOnline(getActivity())) {
            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchOrderDetailsWithContactID");
            map.put("UserID", UserId);
            map.put("ContactID", customerList.get(position).getContact_ID());
            try {
                requestManager = new RequestManager();
                requestManager.sendPostRequest(this,
                        KEY_FETCH_CUSTOMER_Popup_LIST, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }

    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        hideLoading();

        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_CUSTOMER_LIST) {
                Gson gson = new Gson();
                CustomerObject customerObject = gson.fromJson(response, CustomerObject.class);
                customerList = (ArrayList<CustomerList>) customerObject.getCustomerList();
                customerAdapter = new POSCustomerAdapterFragment(customerList, this);
                lvPOSCustomer.setAdapter(customerAdapter);
                lvPOSCustomer.setEmptyView(emptyView);
            } else if (requestId == KEY_FETCH_CUSTOMER_Popup_LIST) {
                Gson gson = new Gson();
                PosCustomerPopup posCustomerPopup = gson.fromJson(response, PosCustomerPopup.class);
                if (posCustomerPopup.getStatus().equalsIgnoreCase("true")) {
                    posCustomerEmail.setText("Email : " + posCustomerPopup.getEmail());
                    posCustomerNumber.setText("Mobile : " + posCustomerPopup.getMobile());
                    posCustomerName.setText("Name : " + posCustomerPopup.getName());
                    orders = posCustomerPopup.getOrder();
                    recyclerPosCustomerDetail.setAdapter(new POSCustomerPopupAdapter(getActivity(), orders));
                } else if (posCustomerPopup.getStatus().equalsIgnoreCase("false")) {
                    Toast.makeText(getActivity(), posCustomerPopup.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            customerList = new ArrayList<>();
            customerAdapter = new POSCustomerAdapterFragment(customerList, this);
            lvPOSCustomer.setAdapter(customerAdapter);
            lvPOSCustomer.setEmptyView(emptyView);
//            showMessage("Please try again.");
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
        totalRows.setText("Records : " + text);
    }

    @Override
    public void executeService() {
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchCustomerList();
                spinner.setSelection(0);
                IssearchBoxOpen = false;
            }
        }, 500);
    }

    private void showMessage(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fetchCustomerListPopup(position);
        showDialogCustomer(position);
    }

    private void showDialogCustomer(final int position) {
        // TODO Auto-generated method stub
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pos_customer_popup);
        dialog.setTitle("Customer Detail");
        recyclerPosCustomerDetail = (RecyclerView) dialog.findViewById(R.id.recyclerPosCustomerDetail);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        recyclerPosCustomerDetail.setLayoutManager(mLayoutManager);
        recyclerPosCustomerDetail.setItemAnimator(new DefaultItemAnimator());
        recyclerPosCustomerDetail.setHasFixedSize(true);
        posCustomerName = (TextView) dialog.findViewById(R.id.posCustomerName);
        posCustomerNumber = (TextView) dialog.findViewById(R.id.posCustomerNumber);
        posCustomerEmail = (TextView) dialog.findViewById(R.id.posCustomerEmail);


        /*contactName = (TextView) dialog.findViewById(R.id.pos_invoice_contact_name);
        invoiceCode = (TextView) dialog.findViewById(R.id.pos_invoice_item_code);
        profileImage = (ImageView) dialog.findViewById(R.id.pos_invoice_item_image);
        invoiceContact= (TextView) dialog.findViewById(R.id.pos_invoice_contact_number);
        pdfview = (ImageView) dialog.findViewById(R.id.pdfviewInvoice);

        contactName.setText("Name: "+invoiceAdapter.getAllArrayList().get(position).getCustomerName());
        fetchInvoiceDetail(position);*/
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageCustomerExport:
                if (new PermissionClass(getActivity()).checkPermissionForExternalStorage())
                    try {
                        ExportCSV();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(),"File not found",Toast.LENGTH_SHORT).show();
                    }
                else
                    new PermissionClass(getActivity()).requestPermissionForExternalStorage();

                break;
            case R.id.framelayoutPosCustomerName:
                if (name) {
                    if (customerList != null && customerList.size() > 0) {
                        Collections.sort(customerAdapter.getList(), new CustomerComparator(1, name));
                        customerAdapter.notifyDataSetChanged();
                    }
                    name = false;
                } else {
                    if (customerList != null && customerList.size() > 0) {
                        Collections.sort(customerAdapter.getList(), new CustomerComparator(1, name));
                        customerAdapter.notifyDataSetChanged();
                    }
                    name = true;
                }
                break;
            case R.id.framelayoutPosCustomerOrder:
                if (order) {
                    if (customerList != null && customerList.size() > 0) {
                        Collections.sort(customerAdapter.getList(), new CustomerComparator(2, order));
                        customerAdapter.notifyDataSetChanged();
                    }
                    order = false;
                } else {
                    if (customerList != null && customerList.size() > 0) {
                        Collections.sort(customerAdapter.getList(), new CustomerComparator(2, order));
                        customerAdapter.notifyDataSetChanged();
                    }
                    order = true;
                }
                break;

            case R.id.framelayoutPosCustomerBusiness:
                if (business) {
                    if (customerList != null && customerList.size() > 0) {
                        Collections.sort(customerAdapter.getList(), new CustomerComparator(3, business));
                        customerAdapter.notifyDataSetChanged();
                    }
                    business = false;
                } else {
                    if (customerList != null && customerList.size() > 0) {
                        Collections.sort(customerAdapter.getList(), new CustomerComparator(3, business));
                        customerAdapter.notifyDataSetChanged();
                    }
                    business = true;
                }
                break;

            case R.id.framelayoutPosCustomerCredit:
                if (credit) {
                    if (customerList != null && customerList.size() > 0) {
                        Collections.sort(customerAdapter.getList(), new CustomerComparator(4, credit));
                        customerAdapter.notifyDataSetChanged();
                    }
                    credit = false;
                } else {
                    if (customerList != null && customerList.size() > 0) {
                        Collections.sort(customerAdapter.getList(), new CustomerComparator(4, credit));
                        customerAdapter.notifyDataSetChanged();
                    }
                    credit = true;
                }
                break;
            case R.id.framelayoutPosCustomerDate:
                if (date) {
                    if (customerList != null && customerList.size() > 0) {
                        Collections.sort(customerAdapter.getList(), new CustomerComparator(5, date));
                        customerAdapter.notifyDataSetChanged();
                    }
                    date = false;
                } else {
                    if (customerList != null && customerList.size() > 0) {
                        Collections.sort(customerAdapter.getList(), new CustomerComparator(5, date));
                        customerAdapter.notifyDataSetChanged();
                    }
                    date = true;
                }
                break;
        }
    }

    private void ExportCSV() throws IOException {
        ArrayList<CustomerList> tempCustomerList = customerAdapter.getList();
        String path = Environment.getExternalStorageDirectory().getPath();
        long current_time = System.currentTimeMillis();
        File dir = new File(path + "/" + getString(R.string.app_name) + "/csv/");
        if (!dir.exists()) dir.mkdirs();
        File csvfile = new File(dir + "/CustomerList_" + current_time + ".csv");

        FileWriter writer = new FileWriter(csvfile);

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Name", "Order", "Business", "Credit", "Date"));

        for (CustomerList d : tempCustomerList) {

            List<String> list = new ArrayList<>();
            list.add(d.getContact_Name());
            list.add(d.getTotalOrder());
            list.add(String.valueOf(d.getTotalAmount()));
            list.add(d.getTotalCredit());
            try {
                String pattern = "MM/dd/yyyy";
                Date date = new Date(d.getLastOrderDate());
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                list.add(format.format(date));
            } catch (Exception c) {
                System.out.println("Date error" + c.getMessage());
            }


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
                                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                pdfOpenintent.setDataAndType(csvpath, "text/csv");
                                try {
                                    startActivity(pdfOpenintent);
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
