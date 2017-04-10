package com.kitever.pos.fragment.purchaseList.PurchaseOutStanding;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.fragment.PosComparators.CSVUtils;
import com.kitever.pos.model.manager.ModelManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PuchaseOutStandingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class PuchaseOutStandingFragment extends Fragment implements NetworkManager, DatePickerDialog.OnDateSetListener, PurchaseOutStandingAdapter.Actionable, TextWatcher, AdapterView.OnItemSelectedListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters

    private String UserId;
    private String UserLogin;
    private String PassWord;
    private final int KEY_FETCH_PURCHASE_OUTSTANDING = 1;
    private final int KEY_ADD_PURCHASE_PAYMENT = 2;
    private final int KEY_FETCH_MODE_PAYMENT = 3;
    private final int KEY_ADD_PART_PAYMENT = 4;
    private RecyclerView recyclerView;
    private PurchaseOutStandingAdapter purchaseOutStandingAdapter;
    private ArrayList<GetOutstandingPurchase> getOutstandingPurchases;

    private SimpleDateFormat format;
    private Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);

    private EditText billingAmtEdit, paidAmtEdit, balanceEdit, receipt, chequeNo,
            bankNameOrRefNum, remarkOrDes, etPurchaseOutStandingSearch;
    private ImageView imagePurchaseOutStandingSearch, imagePurchaseOutStandingOrderExport;
    private TextView date, tvPurchaseOutStandingTotalRows;
    private Spinner modePaymentSpinner;
    private DatePickerDialog dialogDate;
    private RelativeLayout payment_mode_layout, receipt_layout;
    private ArrayAdapter<String> PaymentModeadapter;
    private int modeNo = 0;
    private boolean paymentMode_not_cash;
    private String chequeNoVal = "", dateVal = "", bankNameOrRefNumVal = "", remarkOrDesVal = "", ref_number = "",
            modeType = "";
    private Spinner spinnerPurchaseOutStandingSelectType;
    private int lastClicked = -1;
    private TextView dateTitle, orderTitle, customerTitle, amountTitle, paidTitle;

    public PuchaseOutStandingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PuchaseOutStandingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PuchaseOutStandingFragment newInstance(String param1, String param2, String param3) {
        PuchaseOutStandingFragment fragment = new PuchaseOutStandingFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_puchase_out_standing, container, false);
        setUI(view);

        return view;
    }

    private void setUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewPurchaseOutStanding);
        spinnerPurchaseOutStandingSelectType = (Spinner) view.findViewById(R.id.spinnerPurchaseOutStandingSelectType);
        etPurchaseOutStandingSearch = (EditText) view.findViewById(R.id.etPurchaseOutStandingSearch);
        tvPurchaseOutStandingTotalRows = (TextView) view.findViewById(R.id.tvPurchaseOutStandingTotalRows);
        imagePurchaseOutStandingSearch = (ImageView) view.findViewById(R.id.imagePurchaseOutStandingSearch);
        imagePurchaseOutStandingOrderExport = (ImageView) view.findViewById(R.id.imagePurchaseOutStandingOrderExport);

        imagePurchaseOutStandingOrderExport.setOnClickListener(this);

        setRobotoThinFont(tvPurchaseOutStandingTotalRows, getActivity());
        setRobotoThinFont(etPurchaseOutStandingSearch, getActivity());

        tvPurchaseOutStandingTotalRows.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etPurchaseOutStandingSearch.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        dateTitle = (TextView) view.findViewById(R.id.dateTitle);
        orderTitle = (TextView) view.findViewById(R.id.orderTitle);
        customerTitle = (TextView) view.findViewById(R.id.customerTitle);
        amountTitle = (TextView) view.findViewById(R.id.amountTitle);
        paidTitle = (TextView) view.findViewById(R.id.paidTitle);


        setRobotoThinFont(dateTitle, getActivity());
        setRobotoThinFont(orderTitle, getActivity());
        setRobotoThinFont(customerTitle, getActivity());
        setRobotoThinFont(amountTitle, getActivity());
        setRobotoThinFont(paidTitle, getActivity());

        dateTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        orderTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        customerTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        amountTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        paidTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        List<String> type = new ArrayList<>();
        type.add("Last 50 Outstanding");
        type.add("Vendor");
        type.add("Po Number");
        type.add("Invoice");
        type.add("Bill >");
        type.add("Bill <");
        type.add("Balance >");
        type.add("Balance <");

        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter
                (getActivity(), android.R.layout.simple_spinner_item, type);
        spinnerPurchaseOutStandingSelectType.setAdapter(typeAdapter);
        spinnerPurchaseOutStandingSelectType.setOnItemSelectedListener(this);
        etPurchaseOutStandingSearch.addTextChangedListener(this);
        getOutstandingPurchases = new ArrayList<>();
        purchaseOutStandingAdapter = new PurchaseOutStandingAdapter(getActivity(), getOutstandingPurchases, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(purchaseOutStandingAdapter);
        recyclerView.addOnItemTouchListener(new PuchaseOutStandingFragment.RecyclerViewTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                posPaymentPopup(String.valueOf(purchaseOutStandingAdapter.getOutstandingPurchaseList().get(position).getBalance()), String.valueOf(purchaseOutStandingAdapter.getOutstandingPurchaseList().get(position).getID()));
                lastClicked = position;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchModeOfPayment();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchOutstanding();
            }
        }, 500);
    }

    private void posPaymentPopup(String balanceAmount, final String purchaseId) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pos_addpaymentpoup);
        dialog.setTitle("Add Payment");
        billingAmtEdit = (EditText) dialog.findViewById(R.id.billing_amt);
        billingAmtEdit.setEnabled(false);
        billingAmtEdit.setText(balanceAmount);
        paidAmtEdit = (EditText) dialog.findViewById(R.id.paid_amt);
        balanceEdit = (EditText) dialog.findViewById(R.id.balance);
        receipt = (EditText) dialog.findViewById(R.id.receipt);
        balanceEdit.setEnabled(false);
        modePaymentSpinner = (Spinner) dialog.findViewById(R.id.mode_payment);
        payment_mode_layout = (RelativeLayout) dialog.findViewById(R.id.payment_mode_layout);
        receipt_layout = (RelativeLayout) dialog.findViewById(R.id.receipt_layout);
        receipt_layout.setVisibility(View.VISIBLE);
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
                                          int after) {
            }

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
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        Button addMainBtn = (Button) dialog.findViewById(R.id.btn_add_without_invoice);
        //Button addInvoiceBtn = (Button)dialog.findViewById(R.id.btn_add_invoice);

        addMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (paidAmtEdit.getText().toString().length() > 0) {
                    if (modeNo == 0) {
                        Toast.makeText(getActivity(), "Select Payment Mode", Toast.LENGTH_SHORT).show();
                    } else if (modeNo == 1) {
                        AddCreditPayemnt(paidAmtEdit.getText().toString(), purchaseId, receipt.getText().toString());
                        dialog.hide();
                    } else if (PaymentModeValidation()) {
                        AddCreditPayemnt(paidAmtEdit.getText().toString(), purchaseId, receipt.getText().toString());
                        dialog.hide();
                    }
                } else {
                    Toast.makeText(getActivity(), "Enter The Paid Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void setModelayout(Dialog dialog) {

        chequeNo = (EditText) dialog.findViewById(R.id.cheque_no_edit);
        bankNameOrRefNum = (EditText) dialog.findViewById(R.id.bank_name_edit);
        remarkOrDes = (EditText) dialog.findViewById(R.id.remark_edit);
        //inputLayout = (FrameLayout)dialog.findViewById(R.id.input_layout);
        //calendarLayout = (FrameLayout)dialog.findViewById(R.id.calendar_layout);
        date = (TextView) dialog.findViewById(R.id.date_id);

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialogDate = new DatePickerDialog(getActivity(), PuchaseOutStandingFragment.this, year, month, day);
                dialogDate.getDatePicker().setMaxDate((new Date().getTime()));//-(5097600000L)
//				dialogDate.setTitle("Select Start Date");
//                statusPopup = true;
                dialogDate.show();

            }
        });
    }

    private boolean PaymentModeValidation() {
        chequeNoVal = chequeNo.getText().toString().trim();
        dateVal = date.getText().toString().trim();
        bankNameOrRefNumVal = bankNameOrRefNum.getText().toString().trim();
        remarkOrDesVal = remarkOrDes.getText().toString().trim();

        if (modeType.equalsIgnoreCase("Cheque")) {
            if (chequeNoVal == null || chequeNoVal.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Please enter cheque number",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (dateVal == null || dateVal.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please enter date",
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (bankNameOrRefNumVal == null
                || bankNameOrRefNumVal.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please enter " + ref_number,
                    Toast.LENGTH_LONG).show();
            return false;
        } else {

        }
        return true;
    }

    private void AddCreditPayemnt(String paidAmount, String purchaseId, String ReceiptNo) {

        if (Utils.isDeviceOnline(getActivity())) {
//            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "AddPaymentToPurchase");
           /* map.put("InvoiceNo", InvoiceNo);
            map.put("InvoiceCode", InvoiceCode);//ReceiptNo:abd457
            map.put("OrderID", OrderID);//PurchaseID:5*/
//            map.put("contactID", contactID);
            map.put("PurchaseID", purchaseId);
            map.put("PaidAmount", paidAmount);
            map.put("PayMode", "" + modeNo);
            map.put("PayModeNo", chequeNoVal);
            map.put("ChequeDate", dateVal);
            map.put("BankName", bankNameOrRefNumVal);
            map.put("Remark", remarkOrDesVal);
            map.put("ReceiptNo", ReceiptNo);

            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);

            Log.i("add apyment", "map - " + map.toString());

            try {
                new RequestManager().sendPostRequest(this,
                        KEY_ADD_PURCHASE_PAYMENT, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
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
                    paymentMode_not_cash = true;
                    if (modeType != null
                            && (modeType.equalsIgnoreCase("RTGS") || (modeType
                            .equalsIgnoreCase("Credit Card")))) {
                        chequeNo.setVisibility(View.GONE);
                        bankNameOrRefNum.setHint("Ref Number");
                        remarkOrDes.setHint("Description");
                        ref_number = "Referenece Number";

                    } else {
                        chequeNo.setVisibility(View.VISIBLE);
                        bankNameOrRefNum.setHint("Bank name");
                        remarkOrDes.setHint("Remark");
                        ref_number = "Bank Name";
                    }
                } else {
                    paymentMode_not_cash = false;
                    payment_mode_layout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {


        }
    };

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // TODO Auto-generated method stub
        date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
//            statusPopup = false;
    }

    public void fetchOutstanding() {
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                Map map = new HashMap();
                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                map.put("Page", "GetOutstandingPurchases");

                new RequestManager().sendPostRequest(this, KEY_FETCH_PURCHASE_OUTSTANDING, map);
            } catch (Exception e) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (response != null && response.length() > 0) {

            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new com.kitever.utils.Utils.NullStringToEmptyAdapterFactory<>()).create();
            if (requestId == KEY_FETCH_PURCHASE_OUTSTANDING) {
                PurchaseOutStandingModel purchaseOutStandingModel = gson.fromJson(response, PurchaseOutStandingModel.class);
                getOutstandingPurchases.addAll(purchaseOutStandingModel.getGetOutstandingPurchases());
                purchaseOutStandingAdapter.notifyDataSetChanged();
            } else if (requestId == KEY_FETCH_MODE_PAYMENT) {
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

            } else if (requestId == KEY_ADD_PURCHASE_PAYMENT) {
                Log.i("Purchase", "Response- " + response);
                AddPartPaymentModel addPartPaymentModel = gson.fromJson(response, AddPartPaymentModel.class);
                purchaseOutStandingAdapter.getOutstandingPurchaseList().get(lastClicked).setBalance(addPartPaymentModel.getAddPaymentToPurchase().get(0).getBalance());
                purchaseOutStandingAdapter.getOutstandingPurchaseList().get(lastClicked).setBillAmount(addPartPaymentModel.getAddPaymentToPurchase().get(0).getBillAmount());
                purchaseOutStandingAdapter.getOutstandingPurchaseList().get(lastClicked).setPaidAmount(addPartPaymentModel.getAddPaymentToPurchase().get(0).getPaidAmount());
                /*Change the main List*/

                purchaseOutStandingAdapter.setFinalOutstandingPurchases(purchaseOutStandingAdapter.getOutstandingPurchaseList().get(lastClicked));
                purchaseOutStandingAdapter.notifyItemChanged(lastClicked);
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void updateCount(int count) {
        tvPurchaseOutStandingTotalRows.setText("Records : " + count);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (purchaseOutStandingAdapter != null)
            purchaseOutStandingAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        purchaseOutStandingAdapter.setSelectedItemPosition(position);
        etPurchaseOutStandingSearch.setText("");
        if (position != 0) {
            etPurchaseOutStandingSearch.setVisibility(View.VISIBLE);
            imagePurchaseOutStandingSearch.setVisibility(View.GONE);
            if (position == 5 || position == 6 || position == 7 || position == 8)
                etPurchaseOutStandingSearch.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            else
                etPurchaseOutStandingSearch.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        } else if (position == 0) {
            etPurchaseOutStandingSearch.setVisibility(View.GONE);
            imagePurchaseOutStandingSearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagePurchaseOutStandingOrderExport:
                try {
                    ExportCSV();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerViewTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, recyclerView.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private void ExportCSV() throws IOException {
        ArrayList<GetOutstandingPurchase> tempVendorList = purchaseOutStandingAdapter.getOutstandingPurchaseList();
        String path = Environment.getExternalStorageDirectory().getPath();
        long current_time = System.currentTimeMillis();
        File dir = new File(path + "/" + getString(R.string.app_name) + "/csv/");
        if (!dir.exists()) dir.mkdirs();
        File csvfile = new File(dir + "/OutStandingList_" + current_time + ".csv");

        FileWriter writer = new FileWriter(csvfile);

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Date", "PO Number", "Invoice", "Vendor", "Bill", "Paid", "Balance"));

        for (GetOutstandingPurchase d : tempVendorList) {
            List<String> list = new ArrayList<>();
            try {
                String pattern = "MM/dd/yyyy";
                Date date = new Date(d.getPurchaseDate());
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                list.add(format.format(date));
            } catch (Exception c) {
                System.out.println("Date error" + c.getMessage());
            }

            list.add(String.valueOf(d.getPO_NO()));
            list.add(String.valueOf(d.getContactName()));
            list.add(d.getmInvoiceNo());
            list.add(String.valueOf(d.getBillAmount()));
            list.add(String.valueOf(d.getPaidAmount()));
            list.add(String.valueOf(d.getBalance()));



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
