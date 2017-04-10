package com.kitever.pos.fragment;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.kitever.pos.activity.POSOtherChargeAdd;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.fragment.adapters.POSOtherChargeAdapterFragment;
import com.kitever.pos.model.data.OTCList;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static android.app.Activity.RESULT_OK;
import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link POSOtherChargeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class POSOtherChargeScreenFragment extends Fragment implements
        NetworkManager, View.OnClickListener, POSOtherChargeAdapterFragment.Actionable, TotalRows, ExecuteService {
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

    private static final int ADD_CHARGES = 1;
    private TextView noRecord, totalRecords, dateRange;
    private ImageView dateImage;
    private ListView chargeListView;
    private final int KEY_FETCH_CHARGES_LIST = 1;
    private POSOtherChargeAdapterFragment chargesAdapter;
    private final int KEY_DELETE_TAX = 2;
    private EditText searchBox;
    private RelativeLayout emptyView;

    private ProgressBar progressBar;
    private ImageView search_icon;
    private boolean IssearchBoxOpen=false;

    public POSOtherChargeScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment POSOtherChargeScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static POSOtherChargeScreenFragment newInstance(String param1, String param2, String param3) {
        POSOtherChargeScreenFragment fragment = new POSOtherChargeScreenFragment();
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
        View view = inflater.inflate(R.layout.activity_posother_charge_screen, container, false);
        setScreen(view);
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchOtherCharges();
                searchBox.setText("");
            }
        }, 500);
        return view;
    }

    private void setScreen(View view) {
        Spinner selectType = (Spinner) view.findViewById(R.id.other_select_type_spinner);
        searchBox = (EditText) view.findViewById(R.id.other_edit_search);
        ImageView adavanceSearch = (ImageView) view.findViewById(R.id.other_advance_search);
        chargeListView = (ListView) view.findViewById(R.id.other_tax_list_view);
        noRecord = (TextView) view.findViewById(R.id.other_no_record);
        totalRecords = (TextView) view.findViewById(R.id.other_tax_total_rows);
        emptyView = (RelativeLayout) view.findViewById(R.id.other_emptyElement);
        MoonIcon mIcon = new MoonIcon(this);
        mIcon.setTextfont(noRecord);

        RelativeLayout page_main_layout=(RelativeLayout)view.findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));
        chargeListView.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));


        searchBox.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        totalRecords.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        setRobotoThinFont(searchBox,getActivity());
        setRobotoThinFont(totalRecords,getActivity());


        android.support.design.widget.FloatingActionButton fabButton = (android.support.design.widget.FloatingActionButton) view.findViewById(R.id.charges_add);

        fabButton.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor(CustomStyle.FLOATING_ACTION_BACKGROUND)));
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        POSOtherChargeAdd.class);
                intent.putExtra("screen_name", "Add Tax");
                startActivityForResult(intent, ADD_CHARGES);
            }
        });

        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("Name");
//        typeList.add("Amount");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectType.setAdapter(typeAdapter);
        selectType.setOnItemSelectedListener(itemSelectedListener);

        // if (categoryAdapter != null) {
        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                if (chargesAdapter != null) {
                    chargesAdapter.getFilter().filter(cs);
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

        progressBar = (ProgressBar) view.findViewById(R.id.marker_progress);
        progressBar.setVisibility(View.GONE);
        search_icon= (ImageView) view.findViewById(R.id.search_icon);
//        search_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(IssearchBoxOpen)
//                {
//                    searchBox.setVisibility(View.GONE);
//                    IssearchBoxOpen=false;
//                }
//                else {
//                    searchBox.setVisibility(View.VISIBLE);
//                    IssearchBoxOpen=true;
//                }
//            }
//        });


    }


    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading()
    {
        progressBar.setVisibility(View.GONE);
    }

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            // TODO Auto-generated method stub
            Spinner spinSelected = (Spinner) parent;
            String selectedItem = (String) spinSelected.getSelectedItem();
            if (chargesAdapter != null) {
                chargesAdapter.setSelectedItemType(selectedItem);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
            chargesAdapter.setSelectedItemType(null);
        }
    };

    private void fetchOtherCharges() {
        try {
            if (Utils.isDeviceOnline(getActivity())) {
               showLoading();
                Map map = new HashMap<>();
                map.put("Page", "FetchOTC");
                map.put("IsActive", "A");
                map.put("userID", UserId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_CHARGES_LIST, map);
            } else {
                showMessage("Internet connection not found");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        hideLoading();
        System.out.println("OTC " + response);
        if (response != null && response.length() > 0) {
                if (requestId == KEY_FETCH_CHARGES_LIST) {
                Gson gson = new Gson();
                OTCList otclist = gson.fromJson(response, OTCList.class);
                //System.out.println("OTC "+otclist.getOTC().toString());
                if (otclist.getStatus() != null
                        && otclist.getStatus().equals("true")) {
                    chargesAdapter = new POSOtherChargeAdapterFragment(this, otclist.getOTC(), UserId, UserLogin, PassWord);
                    chargeListView.setAdapter(chargesAdapter);
                } else {
                    if (otclist.getStatus() != null
                            && otclist.getMessage() != null) {
                        Toast.makeText(getActivity(), otclist.getMessage(), Toast.LENGTH_SHORT).show();
                        chargesAdapter = new POSOtherChargeAdapterFragment(this, otclist.getOTC(), UserId, UserLogin, PassWord);
                        chargeListView.setAdapter(chargesAdapter);
                        chargeListView.setEmptyView(emptyView);
                    }
                }
                // chargesAdapter.notifyDataSetChanged();
            }
            /*else if (requestId == KEY_DELETE_CHARGES) {
                Gson gson = new Gson();
                OTCList otclist = gson.fromJson(response, OTCList.class);
                //System.out.println("OTC "+otclist.getOTC().toString());
                if (otclist.getStatus() != null
                        && otclist.getStatus().equals("true")) {
                    Toast.makeText(getActivity(), otclist.getMessage(), Toast.LENGTH_SHORT).show();
                    chargesAdapter.changeList(otclist.getOTC());
                } else {
                    if (otclist.getStatus() != null
                            && otclist.getMessage() != null) {
                        Toast.makeText(getActivity(), otclist.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }*/
            else {
                showMessage("Please try again.");
            }
        } else {
            showMessage("Please try again.");
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
      hideLoading();
        // TODO Auto-generated method stub
//        hideLoading();
        showMessage(getActivity().getResources().getString(R.string.volleyError).toString());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
//        super.onClick(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CHARGES) {
            if (resultCode == RESULT_OK) {
                Gson gson = new Gson();
                OTCList otclist = gson.fromJson(data.getStringExtra("response"), OTCList.class);
                System.out.println("OTC " + otclist.getOTC().toString());
                if (otclist.getStatus() != null
                        && otclist.getStatus().equals("true")) {
                    chargesAdapter.changeList(otclist.getOTC());
                }
            }
        }
    }

    @Override
    public void totalRows(String text) {
        // TODO Auto-generated method stub
        totalRecords.setText("Records : "+text);
    }

    @Override
    public void executeService() {
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchOtherCharges();
                searchBox.setText("");
            }
        }, 500);
    }

    private void showMessage(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteOTC(Map map) {
/*        try {
            if (Utils.isDeviceOnline(getActivity())) {
//                showLoading();
                new RequestManager().sendPostRequest(this,
                        KEY_DELETE_CHARGES, map);
            } else {
                showMessage("Internet connection not found");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }*/
    }
}
