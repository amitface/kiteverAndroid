package com.kitever.pos.fragment.purchaseList;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.fragment.PosComparators.CSVUtils;
import com.kitever.pos.fragment.purchaseList.Purchase.PurchaseAddActivity;

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
 */
public class PurchaseListFragment extends Fragment implements NetworkManager, PurchaseListAdapter.Actionable, TextWatcher, View.OnClickListener{

    private static String ARG_PARAM1 = "param1";
    private static String ARG_PARAM2 = "param2";
    private static String ARG_PARAM3 = "param3";

    private String UserId;
    private String UserLogin;
    private String PassWord;
    private final int KEY_FETCH_PURCHASE_LIST = 1;
    private final int ADD_PURCHASE = 2;
    private final int KEY_FETCH_PURCHASE_POP_UP = 3;

    private RecyclerView recyclerView;
    private ArrayList<GetPurchaseMaster> getPurchaseMasters;
    private PurchaseListAdapter purchaseListAdapter;
    private ImageView imagePurchaseListSearchIcon;
    private EditText etPurchaseListEditSearch;

    private RecyclerView recyclerPurchasePopUp;
    private ArrayList<GetPurchaseDetail> getPurchaseDetails;
    private PurchaseListPopUpAdapter purchaseListPopUpAdapter;
    private Dialog dialog;
    private TextView tvPurchasePopUpVendor, tvPurchasePopUpInvoice, tvPurchasePopUpEmail, tvPurchaseListOrdersTotalRows;
    private Spinner spinnerPurchaseSelectType;
    private TextView dateTitle,orderTitle,customerTitle,amountTitle,paidTitle;
    private ImageView imagePurchaseListOrderExport;

    public PurchaseListFragment() {
        // Required empty public constructor

    }

    public static PurchaseListFragment newInstance(String param1, String param2, String param3) {
        PurchaseListFragment fragment = new PurchaseListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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
        View view = inflater.inflate(R.layout.fragment_purchase_list, container, false);
        setUI(view);
        return view;
    }

    private void setUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewPurchaseList);
        imagePurchaseListSearchIcon = (ImageView) view.findViewById(R.id.imagePurchaseListSearchIcon);
        etPurchaseListEditSearch = (EditText) view.findViewById(R.id.etPurchaseListEditSearch);
        spinnerPurchaseSelectType = (Spinner) view.findViewById(R.id.spinnerPurchaseSelectType);
        tvPurchaseListOrdersTotalRows = (TextView) view.findViewById(R.id.tvPurchaseListOrdersTotalRows);

        recyclerView.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));
        dateTitle = (TextView) view.findViewById(R.id.dateTitle);
        orderTitle = (TextView) view.findViewById(R.id.orderTitle);
        customerTitle = (TextView) view.findViewById(R.id.customerTitle);
        amountTitle = (TextView) view.findViewById(R.id.amountTitle);
        paidTitle = (TextView) view.findViewById(R.id.paidTitle);

        imagePurchaseListOrderExport = (ImageView) view.findViewById(R.id.imagePurchaseListOrderExport);
        imagePurchaseListOrderExport.setOnClickListener(this);

        RelativeLayout page_main_layout=(RelativeLayout)view.findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        tvPurchaseListOrdersTotalRows.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etPurchaseListEditSearch.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        setRobotoThinFont(tvPurchaseListOrdersTotalRows,getActivity());
        setRobotoThinFont(etPurchaseListEditSearch,getActivity());


        dateTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        orderTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        customerTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        amountTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        paidTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        List<String> typeList = new ArrayList<>();
        typeList.add("Last 50 Purchase");
        typeList.add("PO Number");
        typeList.add("Invoice");
        typeList.add("Vendor");
        typeList.add("Bill >");
        typeList.add("Bill <");
        typeList.add("Paid >");
        typeList.add("Paid <");
        typeList.add("Balance >");
        typeList.add("Balance <");

        CustomStyle.MySpinnerAdapter typeAdpater = new CustomStyle.MySpinnerAdapter
                (getActivity(), android.R.layout.simple_spinner_item, typeList);
        typeAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPurchaseSelectType.setAdapter(typeAdpater);
        spinnerPurchaseSelectType.setOnItemSelectedListener(onItemSelectedListener);
        etPurchaseListEditSearch.addTextChangedListener(this);
        android.support.design.widget.FloatingActionButton fabButton = (android.support.design.widget.FloatingActionButton) view.findViewById(R.id.floatBtnPurchaseAdd);
        fabButton.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor(CustomStyle.FLOATING_ACTION_BACKGROUND)));
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PurchaseAddActivity.class);
                intent.putExtra("screen_name", "Add Order");
                startActivityForResult(intent, ADD_PURCHASE);
            }
        });

        getPurchaseMasters = new ArrayList<>();
        getPurchaseDetails = new ArrayList<>();
        purchaseListAdapter = new PurchaseListAdapter(getActivity(), getPurchaseMasters,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(purchaseListAdapter);
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
                fetchPurchase();
            }
        }, 500);
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etPurchaseListEditSearch.setText("");
                purchaseListAdapter.setSelectedItemPosition(position);
                if(position !=0)
                {
                    etPurchaseListEditSearch.setVisibility(View.VISIBLE);
                    imagePurchaseListSearchIcon.setVisibility(View.GONE);
                    if (position==4 || position==5 || position==6 || position==7 || position==8 || position==9)
                        etPurchaseListEditSearch.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    else
                        etPurchaseListEditSearch.setInputType(InputType.TYPE_CLASS_TEXT);

                }else if(position == 0)
                {
                    etPurchaseListEditSearch.setVisibility(View.GONE);
                    imagePurchaseListSearchIcon.setVisibility(View.VISIBLE);

                }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void showDialog(int position) {
        fetchPurchasePopUp(String.valueOf(purchaseListAdapter.getPurchaseList().get(position).getID()));
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.purchase_list_popup);
        dialog.setTitle("Vendor Detail");
        recyclerPurchasePopUp = (RecyclerView) dialog.findViewById(R.id.recyclerPurchasePopUp);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        recyclerPurchasePopUp.setLayoutManager(mLayoutManager);
        recyclerPurchasePopUp.setItemAnimator(new DefaultItemAnimator());
        recyclerPurchasePopUp.setHasFixedSize(true);
        tvPurchasePopUpVendor = (TextView) dialog.findViewById(R.id.tvPurchasePopUpVendor);
        tvPurchasePopUpInvoice = (TextView) dialog.findViewById(R.id.tvPurchasePopUpInvoice);
        tvPurchasePopUpEmail = (TextView) dialog.findViewById(R.id.tvPurchasePopUpEmail);

        tvPurchasePopUpVendor.setText("Vendor : "+getPurchaseMasters.get(position).getmContactName());
        tvPurchasePopUpInvoice.setText("Invoice : "+getPurchaseMasters.get(position).getInvoiceNO());
         /* tvPurchasePopUpEmail.setText(getPurchaseMasters.get(position).getContactEmail());*/

        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_PURCHASE) {
                GetPurchaseListModel getPurchaseListModel = (GetPurchaseListModel) data.getParcelableExtra("GetPurchaseListModel");
                getPurchaseMasters.clear();
                getPurchaseMasters.addAll(getPurchaseListModel.getGetPurchaseMaster());
                purchaseListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void fetchPurchase() {
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                Map map = new HashMap();
                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                map.put("Page", "GetPurchaseMaster");

                new RequestManager().sendPostRequest(this, KEY_FETCH_PURCHASE_LIST, map);
            } catch (Exception e) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    public void fetchPurchasePopUp(String ID) {
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                Map map = new HashMap();
                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                map.put("PurchaseID", ID);
                map.put("Page", "GetPurchaseDetails");
                new RequestManager().sendPostRequest(this, KEY_FETCH_PURCHASE_POP_UP, map);
            } catch (Exception e) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (response != null && response.length() > 0) {
            Gson gson = new Gson();
            if (requestId == KEY_FETCH_PURCHASE_LIST) {
                GetPurchaseListModel getPurchaseListModel = gson.fromJson(response, GetPurchaseListModel.class);
                getPurchaseMasters.addAll(getPurchaseListModel.getGetPurchaseMaster());
                purchaseListAdapter.notifyDataSetChanged();
            } else if (requestId == KEY_FETCH_PURCHASE_POP_UP) {
                PurchaseListPopUpModel purchaseListPopUpModel = gson.fromJson(response, PurchaseListPopUpModel.class);
                if (purchaseListPopUpModel.getGetPurchaseDetails() != null) {
                    getPurchaseDetails.clear();
                    getPurchaseDetails.addAll(purchaseListPopUpModel.getGetPurchaseDetails());
                    purchaseListPopUpAdapter = new PurchaseListPopUpAdapter(getActivity(), getPurchaseDetails);
                    recyclerPurchasePopUp.setAdapter(purchaseListPopUpAdapter);
                }
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void updateRecordCount(int count) {
        tvPurchaseListOrdersTotalRows.setText("Records : "+String.valueOf(count));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(purchaseListAdapter!=null)
                purchaseListAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imagePurchaseListOrderExport:
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
        ArrayList<GetPurchaseMaster> tempPurchaseList = purchaseListAdapter.getPurchaseList();
        String path = Environment.getExternalStorageDirectory().getPath();
        long current_time = System.currentTimeMillis();
        File dir = new File(path + "/" + getString(R.string.app_name) + "/csv/");
        if (!dir.exists()) dir.mkdirs();
        File csvfile = new File(dir + "/PurchaseList_" + current_time + ".csv");

        FileWriter writer = new FileWriter(csvfile);

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Date", "PO Number", "Invoice", "Vendor", "Bill", "Paid", "Balance"));

        for (GetPurchaseMaster d : tempPurchaseList) {
            List<String> list = new ArrayList<>();
            try {
                String pattern = "MM/dd/yyyy";
                Date date = new Date(d.getDateAdded());
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                list.add(format.format(date));
            } catch (Exception c) {
                System.out.println("Date error" + c.getMessage());
            }

            list.add(String.valueOf(d.getPONO()));
            list.add(d.getInvoiceNO());
            list.add(String.valueOf(d.getmContactName()));
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
