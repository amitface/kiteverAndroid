package com.kitever.pos.fragment.purchaseList.PurchasePayment;


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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PurchasePaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchasePaymentFragment extends Fragment implements NetworkManager, TextWatcher, AdapterView.OnItemSelectedListener, PurchasePaymentAdapter.Actionable, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private final int KEY_FETCH_PURCHASE_PAYMENT = 1;
    private static final int KEY_FETCH_PURCHASE_PAYMENT_POPUP = 2;


    private String UserId;
    private String UserLogin;
    private String PassWord;

    private RecyclerView recyclerView;
    private ArrayList<GetPaymentPurchase> getPaymentPurchases;
    private PurchasePaymentAdapter purchasePaymentAdapter;

    private Dialog dialog;
    private RecyclerView recyclerPurchasePaymentPopUp;
    private ArrayList<GetPaymentDetailsPopup> getPaymentDetailsPopups;
    private PurchasePaymentPopUpAdapter purchasePaymentPopUpAdapter;

    private Spinner spinnerPurchasePaymentType;
    private EditText etPurchasePaymentSearch;
    private TextView tvPurchasePaymentPopUpMop, tvPurchasePaymentPopUpPaid, tvPurchasePaymentPopUpDate,
            tvPurchasePaymentPopUpBank, tvPurchasePaymentPopUpChq, tvPurchasePaymentPopUpRemarks,
            tvPurchasePaymentTotalRows, tvPurchasePaymentPopUpName, tvPurchasePaymentPopUpReceipt,
            tvPurchasePaymentPopUpInvoice;
    private ImageView imagePurchasePaymentSearch, imagePurchasePaymentOrderExport;
    private String DateFormate = "dd-MMM, yy";
    private TextView dateTitle,orderTitle,customerTitle,amountTitle,paidTitle;

    public PurchasePaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PurchasePaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PurchasePaymentFragment newInstance(String param1, String param2, String param3) {
        PurchasePaymentFragment fragment = new PurchasePaymentFragment();
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
        View view = inflater.inflate(R.layout.fragment_purchase_payment, container, false);
        setUI(view);
        return view;
    }

    private void setUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewPurchasePayment);
        etPurchasePaymentSearch = (EditText) view.findViewById(R.id.etPurchasePaymentSearch);
        tvPurchasePaymentTotalRows = (TextView) view.findViewById(R.id.tvPurchasePaymentTotalRows);
        imagePurchasePaymentSearch = (ImageView) view.findViewById(R.id.imagePurchasePaymentSearch);
        imagePurchasePaymentOrderExport = (ImageView) view.findViewById(R.id.imagePurchasePaymentOrderExport);
        imagePurchasePaymentOrderExport.setOnClickListener(this);
        spinnerPurchasePaymentType = (Spinner) view.findViewById(R.id.spinnerPurchasePaymentType);

        setRobotoThinFont(etPurchasePaymentSearch,getActivity());
        setRobotoThinFont(tvPurchasePaymentTotalRows,getActivity());

        etPurchasePaymentSearch.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPurchasePaymentTotalRows.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        dateTitle = (TextView) view.findViewById(R.id.dateTitle);
        orderTitle = (TextView) view.findViewById(R.id.orderTitle);
        customerTitle = (TextView) view.findViewById(R.id.customerTitle);
        amountTitle = (TextView) view.findViewById(R.id.amountTitle);
        paidTitle = (TextView) view.findViewById(R.id.paidTitle);


        setRobotoThinFont(dateTitle,getActivity());
        setRobotoThinFont(orderTitle,getActivity());
        setRobotoThinFont(customerTitle,getActivity());
        setRobotoThinFont(amountTitle,getActivity());
        setRobotoThinFont(paidTitle,getActivity());

        dateTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        orderTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        customerTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        amountTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        paidTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));



        List<String> type = new ArrayList<>();
        type.add("Last 50 Payments");
        type.add("Vendor");
        type.add("PO Number");
        type.add("Invoice");
        type.add("Bill >");
        type.add("Bill <");
        type.add("Balance >");
        type.add("Balance <");

        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, type);
        spinnerPurchasePaymentType.setAdapter(typeAdapter);
        spinnerPurchasePaymentType.setOnItemSelectedListener(this);
        etPurchasePaymentSearch.addTextChangedListener(this);

        getPaymentDetailsPopups = new ArrayList<>();
        getPaymentPurchases = new ArrayList<>();
        purchasePaymentAdapter = new PurchasePaymentAdapter(getActivity(), getPaymentPurchases,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(purchasePaymentAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                showDialog(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchPayment();
            }
        }, 500);
    }

    public void fetchPayment() {
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                Map map = new HashMap();
                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                /*map.put("userID", "1199");
                map.put("UserLogin", "+919910197940");
                map.put("Password", "17835");;*/
                map.put("Page", "GetPaymentPurchase");

                new RequestManager().sendPostRequest(this, KEY_FETCH_PURCHASE_PAYMENT, map);
            } catch (Exception e) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    public void fetchPaymentPopup(String PaymentId) {
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                Map map = new HashMap();
                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
              /*  map.put("userID", "1199");
                map.put("UserLogin", "+919910197940");
                map.put("Password", "17835");*/
                map.put("Page", "GetPaymentDetailsPopup");
                map.put("PaymentID", PaymentId);

                new RequestManager().sendPostRequest(this, KEY_FETCH_PURCHASE_PAYMENT_POPUP, map);
            } catch (Exception e) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    private void showDialog(int position) {
        fetchPaymentPopup(String.valueOf(purchasePaymentAdapter.getGetPaymentPurchaseList().get(position).getID()));

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.purchase_payment_popup);
        dialog.setTitle("Payment Detail");

        tvPurchasePaymentPopUpName = (TextView) dialog.findViewById(R.id.tvPurchasePaymentPopUpName);
        tvPurchasePaymentPopUpReceipt = (TextView) dialog.findViewById(R.id.tvPurchasePaymentPopUpReceipt);
        tvPurchasePaymentPopUpInvoice = (TextView) dialog.findViewById(R.id.tvPurchasePaymentPopUpInvoice);
        tvPurchasePaymentPopUpMop = (TextView) dialog.findViewById(R.id.tvPurchasePaymentPopUpMop);
        tvPurchasePaymentPopUpPaid = (TextView) dialog.findViewById(R.id.tvPurchasePaymentPopUpPaid);
        tvPurchasePaymentPopUpDate = (TextView) dialog.findViewById(R.id.tvPurchasePaymentPopUpDate);
        tvPurchasePaymentPopUpBank = (TextView) dialog.findViewById(R.id.tvPurchasePaymentPopUpBank);
        tvPurchasePaymentPopUpChq = (TextView) dialog.findViewById(R.id.tvPurchasePaymentPopUpChq);
        tvPurchasePaymentPopUpRemarks = (TextView) dialog.findViewById(R.id.tvPurchasePaymentPopUpRemarks);

        tvPurchasePaymentPopUpName.setText("Vendor : "+purchasePaymentAdapter.getGetPaymentPurchaseList().get(position).getmContactName());
        tvPurchasePaymentPopUpReceipt.setText("Receipt : "+purchasePaymentAdapter.getGetPaymentPurchaseList().get(position).getReceiptNo());
        tvPurchasePaymentPopUpInvoice.setText("Invoice : "+purchasePaymentAdapter.getGetPaymentPurchaseList().get(position).getmInvoiceNo());
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (response != null && response.length() > 0) {
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new com.kitever.utils.Utils.NullStringToEmptyAdapterFactory<>()).create();
            if (requestId == KEY_FETCH_PURCHASE_PAYMENT) {
                PurchasePaymentModel purchasePaymentModel = gson.fromJson(response, PurchasePaymentModel.class);
                getPaymentPurchases.addAll(purchasePaymentModel.getGetPaymentPurchase());
                purchasePaymentAdapter.notifyDataSetChanged();
            } else if (requestId == KEY_FETCH_PURCHASE_PAYMENT_POPUP) {
                PurchasePaymentPopUpModel purchasePaymentPopUpModel = gson.fromJson(response, PurchasePaymentPopUpModel.class);
                if (purchasePaymentPopUpModel.getGetPaymentDetailsPopup() != null) {
                    getPaymentDetailsPopups.addAll(purchasePaymentPopUpModel.getGetPaymentDetailsPopup());
                    tvPurchasePaymentPopUpMop.setText("MOP : " + getPaymentDetailsPopups.get(0).getPayMode());
                    tvPurchasePaymentPopUpPaid.setText("Paid : " + getPaymentDetailsPopups.get(0).getPaidAmount());
                    tvPurchasePaymentPopUpBank.setText("Bank : " + getPaymentDetailsPopups.get(0).getBank());
                    tvPurchasePaymentPopUpChq.setText("Cheque : " + getPaymentDetailsPopups.get(0).getChequeNo());
                    tvPurchasePaymentPopUpRemarks.setText("Remarks : " + getPaymentDetailsPopups.get(0).getRemarks());

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormate);
                    Date date = new Date(getPaymentDetailsPopups.get(0).getPaymentDate());
                    tvPurchasePaymentPopUpDate.setText("Date : " + simpleDateFormat.format(date));
                }
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (purchasePaymentAdapter != null)
            purchasePaymentAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        etPurchasePaymentSearch.setText("");
        purchasePaymentAdapter.setSelectedItemPosition(position);

        if (position != 0) {
            imagePurchasePaymentSearch.setVisibility(View.GONE);
            etPurchasePaymentSearch.setVisibility(View.VISIBLE);
            if(position == 4 || position == 5 || position == 6 || position == 7)
                etPurchasePaymentSearch.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            else
                etPurchasePaymentSearch.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        } else if (position == 0) {
            imagePurchasePaymentSearch.setVisibility(View.VISIBLE);
            etPurchasePaymentSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void updateRecordCount(int count) {
        tvPurchasePaymentTotalRows.setText("Records : "+count);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
          case R.id.imagePurchasePaymentOrderExport:
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
        ArrayList<GetPaymentPurchase> tempPaymentList = purchasePaymentAdapter.getGetPaymentPurchaseList();
        String path = Environment.getExternalStorageDirectory().getPath();
        long current_time = System.currentTimeMillis();
        File dir = new File(path + "/" + getString(R.string.app_name) + "/csv/");
        if (!dir.exists()) dir.mkdirs();
        File csvfile = new File(dir + "/PurchasePaymentList_" + current_time + ".csv");

        FileWriter writer = new FileWriter(csvfile);

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Date", "Vendor", "PO Number", "Invoice", "Bill", "Balance"));

        for (GetPaymentPurchase d : tempPaymentList) {
            List<String> list = new ArrayList<>();
            try {
                String pattern = "MM/dd/yyyy";
                Date date = new Date(d.getPaymentDate());
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                list.add(format.format(date));
            } catch (Exception c) {
                System.out.println("Date error" + c.getMessage());
            }

            list.add(String.valueOf(d.getmContactName()));
            list.add(String.valueOf(d.getmPoNo()));
            list.add(d.getmInvoiceNo());
            list.add(String.valueOf(d.getBillAmount()));
            list.add(String.valueOf(d.getPaidAmount()));
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
