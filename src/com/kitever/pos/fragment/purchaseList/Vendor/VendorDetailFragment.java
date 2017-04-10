package com.kitever.pos.fragment.purchaseList.Vendor;


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
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.fragment.PosComparators.CSVUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorDetailFragment extends Fragment implements NetworkManager,View.OnClickListener,TextWatcher,AdapterView.OnItemSelectedListener, VendorDetailAdapter.Actionable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final int KEY_FETCH_VENDOR_DETAIL = 1;
    private static final int KEY_FETCH_VENDOR_POP_UP = 2;

    // TODO: Rename and change types of parameters
    private String UserId;
    private String UserLogin;
    private String PassWord;

    private RecyclerView recyclerView, recyclerVendorDetail;
    private ArrayList<GetVendorDetail> getVendorDetails;
    private VendorDetailAdapter vendorDetailAdapter;

    private Dialog dialog;
    private TextView tvVendorDetailName, VendorDetailNumber, VendorDetailEmail, tvVendorDetailTotalRows;
    private ArrayList<GetPurchaseDetailAsVendor> getPurchaseDetailAsVendors;
    private Spinner spinnerVendorDetailType;
    private EditText etVendorDetailSearch;
    private ImageView imageVendorDetailSearch, imageVendorDetailOrderExport;
    private VendorPopUpAdapter vendorPopUpAdapter;
    private TextView dateTitle,orderTitle,customerTitle,amountTitle,paidTitle;
    public VendorDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VendorDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VendorDetailFragment newInstance(String param1, String param2, String param3) {
        VendorDetailFragment fragment = new VendorDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_vendor_detail, container, false);
        setUI(view);
        return view;
    }

    private void setUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewVendorDetail);
        tvVendorDetailTotalRows = (TextView) view.findViewById(R.id.tvVendorDetailTotalRows);
        spinnerVendorDetailType = (Spinner) view.findViewById(R.id.spinnerVendorDetailType);
        etVendorDetailSearch = (EditText) view.findViewById(R.id.etVendorDetailSearch);
        imageVendorDetailSearch = (ImageView) view.findViewById(R.id.imageVendorDetailSearch);
        imageVendorDetailOrderExport = (ImageView) view.findViewById(R.id.imageVendorDetailOrderExport);
        imageVendorDetailOrderExport.setOnClickListener(this);
        setRobotoThinFont(etVendorDetailSearch,getActivity());
        setRobotoThinFont(tvVendorDetailTotalRows,getActivity());

        etVendorDetailSearch.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvVendorDetailTotalRows.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


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

        getPurchaseDetailAsVendors = new ArrayList<>();
        List<String> type = new ArrayList<>();
        type.add("Last 50 Vendors");
        type.add("Vendor");
        type.add("Purchases >");
        type.add("Purchases <");
        type.add("Bill >");
        type.add("Bill <");
        type.add("Paid >");
        type.add("Paid <");
        type.add("Balance >");
        type.add("Balance <");

        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, type);
        spinnerVendorDetailType.setAdapter(typeAdapter);
        spinnerVendorDetailType.setOnItemSelectedListener(this);
        etVendorDetailSearch.addTextChangedListener(this);

        getVendorDetails = new ArrayList<>();
        vendorDetailAdapter = new VendorDetailAdapter(getActivity(), getVendorDetails,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(vendorDetailAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(),recyclerView,new VendorDetailFragment.ClickListener(){
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
                fetchVendorDetail();
            }
        }, 500);
    }

    private void showDialog(int position) {

        fetchVendorPopUp(String.valueOf(vendorDetailAdapter.getVendorDetail().get(position).getContactID()));
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pos_vendor_popup);
        dialog.setTitle("Vendor Detail");
        recyclerVendorDetail = (RecyclerView) dialog.findViewById(R.id.recyclerVendorDetail);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        recyclerVendorDetail.setLayoutManager(mLayoutManager);
        recyclerVendorDetail.setItemAnimator(new DefaultItemAnimator());
        recyclerVendorDetail.setHasFixedSize(true);
        tvVendorDetailName = (TextView) dialog.findViewById(R.id.tvVendorDetailName);
        VendorDetailNumber = (TextView) dialog.findViewById(R.id.tvVendorDetailNumber);
        VendorDetailEmail = (TextView) dialog.findViewById(R.id.tvVendorDetailEmail);

        tvVendorDetailName.setText("Vendor : "+vendorDetailAdapter.getVendorDetail().get(position).getContactName());
        VendorDetailNumber.setText("Moblie : "+vendorDetailAdapter.getVendorDetail().get(position).getContactMobile());
        VendorDetailEmail.setText("Email : "+vendorDetailAdapter.getVendorDetail().get(position).getContactEmail());

        dialog.setCancelable(true);
        dialog.show();
    }


    public void fetchVendorDetail() {
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                Map map = new HashMap();
                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                map.put("Page", "GetVendorDetail");
                new RequestManager().sendPostRequest(this, KEY_FETCH_VENDOR_DETAIL, map);
            } catch (Exception e) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    public void fetchVendorPopUp(String contactID) {
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                Map map = new HashMap();
                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                map.put("contactID", contactID);
                map.put("Page", "GetPurchaseDetailAsVendor");

                new RequestManager().sendPostRequest(this, KEY_FETCH_VENDOR_POP_UP, map);
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
            if (requestId == KEY_FETCH_VENDOR_DETAIL) {

                VendorDetailModel vendorDetailModel = gson.fromJson(response, VendorDetailModel.class);
                getVendorDetails.addAll(vendorDetailModel.getGetVendorDetail());
                vendorDetailAdapter.notifyDataSetChanged();
            }
            else if(requestId == KEY_FETCH_VENDOR_POP_UP)
            {
                VendorPopUpModel vendorPopUpModel = gson.fromJson(response,VendorPopUpModel.class);
                getPurchaseDetailAsVendors.clear();
                vendorPopUpAdapter = new VendorPopUpAdapter(getActivity(),getPurchaseDetailAsVendors);
                recyclerVendorDetail.setAdapter(vendorPopUpAdapter);
                getPurchaseDetailAsVendors.addAll(vendorPopUpModel.getGetPurchaseDetailAsVendor());
                vendorPopUpAdapter.notifyDataSetChanged();
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageVendorDetailOrderExport:
                try {
                    ExportCSV();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        vendorDetailAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        etVendorDetailSearch.setText("");
        vendorDetailAdapter.setSelectedItemPosition(position);

        if (position != 0) {
            imageVendorDetailSearch.setVisibility(View.GONE);
            etVendorDetailSearch.setVisibility(View.VISIBLE);
            if(position != 1)
                etVendorDetailSearch.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            else
                etVendorDetailSearch.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (position == 0) {
            imageVendorDetailSearch.setVisibility(View.VISIBLE);
            etVendorDetailSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void updateRecordCount(int count) {
        tvVendorDetailTotalRows.setText("Records : "+count);
    }

    private interface ClickListener {
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
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clickListener!=null)
                    {
                        clickListener.onLongClick(child,recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e))
            {
                clickListener.onClick(child,recyclerView.getChildAdapterPosition(child));
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
        ArrayList<GetVendorDetail> tempVendorList = vendorDetailAdapter.getVendorDetail();
        String path = Environment.getExternalStorageDirectory().getPath();
        long current_time = System.currentTimeMillis();
        File dir = new File(path + "/" + getString(R.string.app_name) + "/csv/");
        if (!dir.exists()) dir.mkdirs();
        File csvfile = new File(dir + "/VendorList_" + current_time + ".csv");

        FileWriter writer = new FileWriter(csvfile);

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Vendor", "Purchases", "Billed", "Paid", "Balance"));

        for (GetVendorDetail d : tempVendorList) {
            List<String> list = new ArrayList<>();

            list.add(String.valueOf(d.getContactName()));
            list.add(String.valueOf(d.getTotalPurchases()));
            list.add(String.valueOf(d.getTotalAmount()));
            list.add(String.valueOf(d.getTotalPayment()));
            list.add(String.valueOf(d.getTotalCredit()));



            CSVUtils.writeLine(writer, list);

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
