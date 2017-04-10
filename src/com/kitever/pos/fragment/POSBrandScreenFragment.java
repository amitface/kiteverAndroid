package com.kitever.pos.fragment;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.activity.PosAddUpdateBrand;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.fragment.adapters.POSBrandAdapterFragment;
import com.kitever.pos.model.data.BrandModelData;
import com.kitever.pos.model.data.PosCategoryModelData;
import com.kitever.pos.model.manager.ModelManager;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static android.app.Activity.RESULT_OK;
import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link POSBrandScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class POSBrandScreenFragment extends Fragment implements NetworkManager,
        View.OnClickListener, POSBrandAdapterFragment.Actionable, TotalRows, ExecuteService {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final int ADD_BRAND = 11;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String UserId;
    private String UserLogin;
    private String PassWord;

    private ListView brandListView;
    private final int KEY_FETCH_BRAND_LIST = 1;
    private final int KEY_DELETE_BRAND = 2;
    private POSBrandAdapterFragment brandAdapter;
    private TextView noRecordImage, totalRecords;
    private RelativeLayout emptyView;
    private ImageView search_icon;
    private boolean IssearchBoxOpen=false;
    EditText searchBox;

    public POSBrandScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment POSBrandScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static POSBrandScreenFragment newInstance(String param1, String param2, String param3) {
        POSBrandScreenFragment fragment = new POSBrandScreenFragment();
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
        View view = inflater.inflate(R.layout.pos_brand_layout, container, false);
        setScreen(view);
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchBrand();
                searchBox.setText("");
            }
        }, 500);
        return view;
    }

    private void setScreen(View view) {
        Spinner selectType = (Spinner) view.findViewById(R.id.select_type_spinner);
        searchBox = (EditText) view.findViewById(R.id.edit_search);
        ImageView adavanceSearch = (ImageView) view.findViewById(R.id.advance_search);
        brandListView = (ListView) view.findViewById(R.id.brand_list_view);
        RelativeLayout page_main_layout=(RelativeLayout)view.findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        brandListView.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));
        MoonIcon mIcon = new MoonIcon(this);
        noRecordImage = (TextView) view.findViewById(R.id.no_record);
        mIcon.setTextfont(noRecordImage);
        emptyView = (RelativeLayout) view.findViewById(R.id.brand_emptyElement);

        totalRecords = (TextView) view.findViewById(R.id.brand_total_rows);
        setRobotoThinFont(searchBox,getActivity());
        setRobotoThinFont(totalRecords,getActivity());
        totalRecords.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        FloatingActionButton fabButton = (FloatingActionButton) view.findViewById(R.id.brand_add);

        fabButton.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor(CustomStyle.FLOATING_ACTION_BACKGROUND)));
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PosAddUpdateBrand.class);
                intent.putExtra("screen_name", "Add Brand");
                startActivityForResult(intent, ADD_BRAND);
            }
        });

        ArrayList<String> typeList = new ArrayList<String>();

        typeList.add("Brand");

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
                if (brandAdapter != null) {
                    brandAdapter.getFilter().filter(cs);
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

        search_icon= (ImageView) view.findViewById(R.id.search_icon);
        /*search_icon.setOnClickListener(new View.OnClickListener() {
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
        });*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_BRAND) {
            if (resultCode == RESULT_OK) {
                String response = data.getStringExtra("response");
                if (response != null && response.length() > 0) {
                    ModelManager.getInstance().setBrandModel(response);
                    System.out.println("response brand" + response);
                    if (ModelManager.getInstance().getBrandModel().getBrandList() != null
                            && ModelManager.getInstance().getBrandModel()
                            .getBrandList().size() > 0) {
                        brandAdapter = new POSBrandAdapterFragment(this, ModelManager
                                .getInstance().getBrandModel().getBrandList(),
                                UserId, UserLogin, PassWord);
                        brandListView.setAdapter(brandAdapter);
                        brandListView.setEmptyView(emptyView);
                    } else {
                        if (ModelManager.getInstance().getBrandModel().getMessage() != null)
                            showMessage(ModelManager.getInstance().getBrandModel()
                                    .getMessage());
                        brandAdapter = new POSBrandAdapterFragment(this,
                                new ArrayList<BrandModelData>(), UserId,
                                UserLogin, PassWord);
                        brandListView.setAdapter(brandAdapter);
                        brandListView.setEmptyView(emptyView);
                    }
                }
            }
        } else if (requestCode == KEY_DELETE_BRAND) {
            if (resultCode == RESULT_OK) {
                String response = data.getStringExtra("response");
                if (response != null && response.length() > 0) {
                    ModelManager.getInstance().setBrandModel(response);
                    System.out.println("response brand" + response);
                    if (ModelManager.getInstance().getBrandModel().getBrandList() != null
                            && ModelManager.getInstance().getBrandModel()
                            .getBrandList().size() > 0) {
                        brandAdapter.setModelData(ModelManager.getInstance()
                                .getBrandModel().getBrandList());
                        brandAdapter.notifyDataSetChanged();
                    } else {
                        if (ModelManager.getInstance().getBrandModel().getMessage() != null)
                            showMessage(ModelManager.getInstance().getBrandModel()
                                    .getMessage());
                        brandAdapter.setModelData(new ArrayList<BrandModelData>());
                        brandAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (brandAdapter != null
                && ModelManager.getInstance().getBrandModel() != null) {
            if ((ModelManager.getInstance().getBrandModel().getStatus() != null && ModelManager
                    .getInstance().getBrandModel().getStatus()
                    .equalsIgnoreCase("true"))) {
                brandAdapter.setModelData(ModelManager.getInstance()
                        .getBrandModel().getBrandList());
                brandAdapter.notifyDataSetChanged();
            }
        }
    }

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            // TODO Auto-generated method stub
            Spinner spinSelected = (Spinner) parent;
            String selectedItem = (String) spinSelected.getSelectedItem();
            if (brandAdapter != null) {
                brandAdapter.setSelectedItemType(selectedItem);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
            brandAdapter.setSelectedItemType(null);
        }
    };

    private void fetchBrand() {
        if (Utils.isDeviceOnline(getActivity())) {
//            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchBrand");
            map.put("IsActive", "");
            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_BRAND_LIST, map);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
//        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_BRAND_LIST) {
                ModelManager.getInstance().setBrandModel(response);
                System.out.println("response brand" + response);
                if (ModelManager.getInstance().getBrandModel().getBrandList() != null
                        && ModelManager.getInstance().getBrandModel()
                        .getBrandList().size() > 0) {
                    brandAdapter = new POSBrandAdapterFragment(this, ModelManager
                            .getInstance().getBrandModel().getBrandList(),
                            UserId, UserLogin, PassWord);
                    brandListView.setAdapter(brandAdapter);
                    brandListView.setEmptyView(emptyView);
                } else {
                    if (ModelManager.getInstance().getBrandModel().getMessage() != null)
                        showMessage(ModelManager.getInstance().getBrandModel()
                                .getMessage());
                    brandAdapter = new POSBrandAdapterFragment(this,
                            new ArrayList<BrandModelData>(), UserId,
                            UserLogin, PassWord);
                    brandListView.setAdapter(brandAdapter);
                    brandListView.setEmptyView(emptyView);
                }
            } else if (requestId == KEY_DELETE_BRAND) {
                ModelManager.getInstance().setBrandModel(response);
                System.out.println("response delete brand" + response);
                if (brandAdapter != null
                        && ModelManager.getInstance().getBrandModel() != null) {
                    if (ModelManager.getInstance().getBrandModel().getMessage() != null
                            && ModelManager.getInstance().getBrandModel()
                            .getMessage().length() > 0) {
                        showMessage(ModelManager.getInstance().getBrandModel()
                                .getMessage());
                    }
                    brandAdapter.setModelData(ModelManager.getInstance()
                            .getBrandModel().getBrandList());
                    brandAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
//        hideLoading();
        showMessage(getActivity().getResources().getString(R.string.volleyError).toString());
    }

    @Override
    public void deleteBrand(Map map) {
        // TODO Auto-generated method stub
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                new RequestManager().sendPostRequest(this, KEY_DELETE_BRAND,
                        map);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    @Override
    public void totalRows(String text) {
        // TODO Auto-generated method stub
        totalRecords.setText("Records : "+text);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void executeService() {
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchBrand();
                searchBox.setText("");
            }
        }, 500);
    }

    private void showMessage(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    public class NameComparatorAsc implements Comparator {
        @Override
        public int compare(Object  first, Object  sec) {

            PosCategoryModelData s1=(PosCategoryModelData)first;
            PosCategoryModelData s2=(PosCategoryModelData)sec;
            return s1.getCategoryName().compareTo(s2.getCategoryName());
        }
    }

    public class NameComparatorDesc implements Comparator {
        @Override
        public int compare(Object  first, Object  sec) {

            PosCategoryModelData s1=(PosCategoryModelData)first;
            PosCategoryModelData s2=(PosCategoryModelData)sec;
            return s2.getCategoryName().compareTo(s1.getCategoryName());
        }
    }
}
