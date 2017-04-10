package com.kitever.pos.fragment.purchaseList.Inventory;


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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Use the {@link InventoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryListFragment extends Fragment implements NetworkManager,InventoryListAdapter.Action, TextWatcher, AdapterView.OnItemSelectedListener, View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private static final int KEY_FETCH_INVENTORY_LIST = 1;
    private static final int KEY_FETCH_INVENTORY_POP_UP = 2;
    private static final int KEY_ADD_INVENTORY = 3;

    private String UserId;
    private String UserLogin;
    private String PassWord;

    private RecyclerView recyclerView;
    private ArrayList<GetCurrentInventory> getCurrentInventories;
    private InventoryListAdapter inventoryListAdapter;

    private Dialog dialogAdd;
    private Dialog dialogHistory;

    private RecyclerView recyclerHistory;
    private InventoryHistoryPopUpAdapter inventoryHistoryPopUpAdapter;
    private ArrayList<GetInventoryTransactionHistory> getInventoryTransactionHistories;
    private EditText etInventoryAddQuantity, etInventoryAddRemarks, etInventoryDetailSearch;
    private Spinner spinnerInventoryDetailType;
    private TextView btnInventoryAdd, tvInventoryDetailTotalRows, tvInventoryHistoryQuantity,
            tvInventoryHistoryProduct, tvInventoryfinalQuantity, tvInventoryQuantity;
    private TextView dateTitle,orderTitle,customerTitle,amountTitle,paidTitle;
    private LinearLayout layoutInventoryAddQuantity;
    private ImageView imageInventoryDetailSearch, imageInventoryDetailOrderExport;
    private int lastClick = -1;


    public InventoryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventoryListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventoryListFragment newInstance(String param1, String param2, String param3) {
        InventoryListFragment fragment = new InventoryListFragment();
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
        View view = inflater.inflate(R.layout.fragment_inventory_list, container, false);
        setUI(view);
        return view;
    }

    private void setUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewInventoryDetail);
        imageInventoryDetailSearch = (ImageView) view.findViewById(R.id.imageInventoryDetailSearch);
        imageInventoryDetailOrderExport = (ImageView) view.findViewById(R.id.imageInventoryDetailOrderExport);
        spinnerInventoryDetailType = (Spinner) view.findViewById(R.id.spinnerInventoryDetailType);

        tvInventoryDetailTotalRows = (TextView) view.findViewById(R.id.tvInventoryDetailTotalRows);
        etInventoryDetailSearch = (EditText) view.findViewById(R.id.etInventoryDetailSearch);

        dateTitle = (TextView) view.findViewById(R.id.dateTitle);
        orderTitle = (TextView) view.findViewById(R.id.orderTitle);
        customerTitle = (TextView) view.findViewById(R.id.customerTitle);
        amountTitle = (TextView) view.findViewById(R.id.amountTitle);

        imageInventoryDetailOrderExport.setOnClickListener(this);

        setRobotoThinFont(dateTitle,getActivity());
        setRobotoThinFont(orderTitle,getActivity());
        setRobotoThinFont(customerTitle,getActivity());
        setRobotoThinFont(amountTitle,getActivity());


        dateTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        orderTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        customerTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        amountTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        setRobotoThinFont(tvInventoryDetailTotalRows,getActivity());
        setRobotoThinFont(etInventoryDetailSearch,getActivity());

        tvInventoryDetailTotalRows.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etInventoryDetailSearch.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        List<String> type = new ArrayList<>();
        type.add("Last 50 Inventory");
        type.add("Product");
        type.add("Quantity >");
        type.add("Quantity <");

        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, type);
        spinnerInventoryDetailType.setAdapter(typeAdapter);
        spinnerInventoryDetailType.setOnItemSelectedListener(this);
        etInventoryDetailSearch.addTextChangedListener(this);

        getCurrentInventories = new ArrayList<>();
        getInventoryTransactionHistories = new ArrayList<>();
        inventoryListAdapter = new InventoryListAdapter(getActivity(), getCurrentInventories,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(inventoryListAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchInventoryList();
            }
        },500);
    }

    public void fetchInventoryList() {
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                Map map = new HashMap();
                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                map.put("Page", "GetCurrentInventory");

                new RequestManager().sendPostRequest(this, KEY_FETCH_INVENTORY_LIST, map);
            } catch (Exception e) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    public void fetchInventoryPopUp(String ID) {
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                Map map = new HashMap();
                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                map.put("ProductID", ID);
                map.put("Page", "GetInventoryTransactionHistory");

                new RequestManager().sendPostRequest(this, KEY_FETCH_INVENTORY_POP_UP, map);
            } catch (Exception e) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    public void AddInventory(String ProductId,String Quantity, String Remarks) {
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                Map map = new HashMap();
                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                map.put("ProductID", ProductId);
                map.put("Quantity", Quantity);
                map.put("Remarks", Remarks);
                map.put("Page", "AddInventory");

                new RequestManager().sendPostRequest(this, KEY_ADD_INVENTORY, map);
            } catch (Exception e) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showHistoryPopup(String ProductName, String Quantity, String ID) {

        fetchInventoryPopUp(ID);
        dialogHistory = new Dialog(getActivity());
        dialogHistory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogHistory.setContentView(R.layout.inventory_history_popup);
        recyclerHistory = (RecyclerView) dialogHistory.findViewById(R.id.recyclerInventoryHistory);
        tvInventoryHistoryProduct = (TextView) dialogHistory.findViewById(R.id.tvInventoryHistoryProduct);
        tvInventoryHistoryQuantity = (TextView) dialogHistory.findViewById(R.id.tvInventoryHistoryQuantity);

        tvInventoryHistoryProduct.setText("Product : "+ProductName);
        tvInventoryHistoryQuantity.setText("Quantity : "+Quantity);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerHistory.setLayoutManager(layoutManager);
        recyclerHistory.setItemAnimator(new DefaultItemAnimator());

        dialogHistory.show();
    }

    @Override
    public void showAddPopup(int position, final GetCurrentInventory getInventory) {
        lastClick = position;
        dialogAdd = new Dialog(getActivity());
        dialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAdd.setContentView(R.layout.inventory_add);
        etInventoryAddQuantity = (EditText) dialogAdd.findViewById(R.id.etInventoryAddQuantity);
        etInventoryAddRemarks = (EditText) dialogAdd.findViewById(R.id.etInventoryAddRemarks);
        layoutInventoryAddQuantity = (LinearLayout) dialogAdd.findViewById(R.id.layoutInventoryAddQuantity);
        tvInventoryfinalQuantity = (TextView) dialogAdd.findViewById(R.id.tvInventoryfinalQuantity);
        tvInventoryQuantity = (TextView) dialogAdd.findViewById(R.id.tvInventoryQuantity);
        btnInventoryAdd = (TextView) dialogAdd.findViewById(R.id.btnInventoryAdd);
        TextView poputitle= (TextView) dialogAdd.findViewById(R.id.btnInventoryAdd);

        btnInventoryAdd.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        poputitle.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        tvInventoryQuantity.setText(getInventory.getBalance().toString());

        etInventoryAddQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()>0)
                {
                    if(s.toString().length()==1 && s.toString().equals("-"))
                        return;
                    layoutInventoryAddQuantity.setVisibility(View.VISIBLE);
                    Long temp = getInventory.getBalance()+Long.parseLong(etInventoryAddQuantity.getText().toString());
                    tvInventoryfinalQuantity.setText(String.valueOf(temp));
                }else
                    layoutInventoryAddQuantity.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnInventoryAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddInventory(getInventory.getProductid().toString(),etInventoryAddQuantity.getText().toString(),etInventoryAddRemarks.getText().toString());
            }
        });
        dialogAdd.show();
    }

    @Override
    public void updateRecordCount(int count) {
        tvInventoryDetailTotalRows.setText("Records : "+count);
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (response != null && response.length() > 0) {
            Gson gson = new Gson();
            if(requestId == KEY_FETCH_INVENTORY_LIST)
            {
                InventoryListModel inventoryListModel = gson.fromJson(response,InventoryListModel.class);
                getCurrentInventories.addAll(inventoryListModel.getGetCurrentInventory());
                inventoryListAdapter.notifyDataSetChanged();
            }else if(requestId == KEY_FETCH_INVENTORY_POP_UP)
            {
                InventoryHistoryModel inventoryHistoryModel = gson.fromJson(response,InventoryHistoryModel.class);
                if(inventoryHistoryModel.getGetInventoryTransactionHistory()!=null)
                {
                    getInventoryTransactionHistories.clear();
                    getInventoryTransactionHistories.addAll(inventoryHistoryModel.getGetInventoryTransactionHistory());
                    inventoryHistoryPopUpAdapter = new InventoryHistoryPopUpAdapter(getActivity(),getInventoryTransactionHistories);
                    recyclerHistory.setAdapter(inventoryHistoryPopUpAdapter);
                }

            }else if(requestId == KEY_ADD_INVENTORY)
            {
                Log.i("Add Inventory : ",response);
                Toast.makeText(getActivity(),"Success",Toast.LENGTH_SHORT).show();
                        dialogAdd.dismiss();
                UpdateCurrentInventory updateCurrentInventory =  gson.fromJson(response,UpdateCurrentInventory.class);
                inventoryListAdapter.updateInventory(lastClick,updateCurrentInventory.getAddInventory().get(0));
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
            if(inventoryListAdapter!=null)
                inventoryListAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        etInventoryDetailSearch.setText("");
        inventoryListAdapter.setSelectedItemPosition(position);

        if (position != 0) {
            imageInventoryDetailSearch.setVisibility(View.GONE);
            etInventoryDetailSearch.setVisibility(View.VISIBLE);
            if(position == 2 || position == 3)
                etInventoryDetailSearch.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            else
                etInventoryDetailSearch.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        } else if (position == 0) {
            imageInventoryDetailSearch.setVisibility(View.VISIBLE);
            etInventoryDetailSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageInventoryDetailOrderExport:
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
        ArrayList<GetCurrentInventory> tempInventoryList = inventoryListAdapter.getCurrentInventory();
        String path = Environment.getExternalStorageDirectory().getPath();
        long current_time = System.currentTimeMillis();
        File dir = new File(path + "/" + getString(R.string.app_name) + "/csv/");
        if (!dir.exists()) dir.mkdirs();
        File csvfile = new File(dir + "/InventoryList_" + current_time + ".csv");

        FileWriter writer = new FileWriter(csvfile);

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Product", "Quantity"));

        for (GetCurrentInventory d : tempInventoryList) {
            List<String> list = new ArrayList<>();

            list.add(String.valueOf(d.getmProductName()));
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
