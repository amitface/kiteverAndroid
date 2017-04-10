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
import android.widget.ProgressBar;
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
import com.kitever.pos.activity.POSTaxAddUpdateScreen;
import com.kitever.pos.adapter.POSTaxMasterAdapter;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.fragment.adapters.POSTaxMasterAdapterFragment;
import com.kitever.pos.model.data.TaxModelData;
import com.kitever.pos.model.manager.ModelManager;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static android.app.Activity.RESULT_OK;
import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link POSTaxMasterScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class POSTaxMasterScreenFragment extends Fragment implements
        NetworkManager, POSTaxMasterAdapter.Actionable, TotalRows, ExecuteService, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String userId;
    private String UserLogin;
    private String PassWord;

    private ArrayList<TaxModelData> taxList;
    private static final int ADD_TAX = 1;
    private TextView noRecord, totalRecords, dateRange;
    private ImageView dateImage;
    private ListView taxListView;
    private final int KEY_FETCH_TAX_LIST = 1;
    private POSTaxMasterAdapterFragment taxAdapter;
    private final int KEY_DELETE_TAX = 2;
    private EditText searchBox;
    private RelativeLayout emptyView;
    private ImageView search_icon;
    private boolean IssearchBoxOpen=false;
    private ProgressBar progressBar;

    public POSTaxMasterScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment POSTaxMasterScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static POSTaxMasterScreenFragment newInstance(String param1, String param2, String param3) {
        POSTaxMasterScreenFragment fragment = new POSTaxMasterScreenFragment();
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
            userId = getArguments().getString(ARG_PARAM1);
            UserLogin = getArguments().getString(ARG_PARAM2);
            PassWord = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pos_tax_layout,container,false);
        setScreen(view);
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchTax();
                IssearchBoxOpen=false;
                searchBox.setText("");
            }
        },500);
        return view;
    }

    private void setScreen(View view) {
        Spinner selectType = (Spinner) view.findViewById(R.id.select_type_spinner);
        searchBox = (EditText) view.findViewById(R.id.edit_search);
        ImageView adavanceSearch = (ImageView) view.findViewById(R.id.advance_search);
        taxListView = (ListView) view.findViewById(R.id.tax_list_view);
        noRecord = (TextView) view.findViewById(R.id.no_record);
        totalRecords = (TextView) view.findViewById(R.id.tax_total_rows);
        emptyView = (RelativeLayout) view.findViewById(R.id.tax_emptyElement);
        MoonIcon mIcon=new MoonIcon(this);
        mIcon.setTextfont(noRecord);

        RelativeLayout page_main_layout=(RelativeLayout)view.findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));
        taxListView.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));

        searchBox.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        totalRecords.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        setRobotoThinFont(searchBox,getActivity());
        setRobotoThinFont(totalRecords,getActivity());


        progressBar = (ProgressBar) view.findViewById(R.id.marker_progress);
        progressBar.setVisibility(View.GONE);

        FloatingActionButton fabButton = (FloatingActionButton)view.findViewById(R.id.tax_add);

        fabButton.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor(CustomStyle.FLOATING_ACTION_BACKGROUND)));
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        POSTaxAddUpdateScreen.class);
                intent.putExtra("screen_name", "Add Tax");
                startActivityForResult(intent,ADD_TAX);
            }
        });

        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("Tax Name");
        typeList.add("Tax%");
//		typeList.add("WefDateFrom");
//		typeList.add("WefDateTo");
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
                if (taxAdapter != null) {
                    taxAdapter.getFilter().filter(cs);
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
        IssearchBoxOpen=true;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_TAX)
        {
            if(resultCode==RESULT_OK)
            {
                String response = data.getStringExtra("response");
                if (response != null && response.length() > 0) {
                    ModelManager.getInstance().setTaxModel(response);
                    if (ModelManager.getInstance().getTaxModel() != null
                            && ModelManager.getInstance().getTaxModel()
                            .getFetchTaxList() != null
                            && ModelManager.getInstance().getTaxModel()
                            .getFetchTaxList().size() > 0) {
                        taxList=ModelManager.getInstance()
                                .getTaxModel().getFetchTaxList();
                        taxAdapter.changeList(taxList);
                        taxListView.setAdapter(taxAdapter);
                        taxListView.setEmptyView(emptyView);

                    } else {
                        if (ModelManager.getInstance().getTaxModel() != null
                                && ModelManager.getInstance().getTaxModel()
                                .getMessage() != null)
						showMessage(ModelManager.getInstance().getTaxModel()
								.getMessage());
                            taxAdapter = new POSTaxMasterAdapterFragment(this,
                                    new ArrayList<TaxModelData>(), userId,
                                    UserLogin, PassWord);
                        taxListView.setAdapter(taxAdapter);
                        taxListView.setEmptyView(emptyView);
                    }
                }
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

           searchBox.setText("");
           if (taxAdapter != null) {
               taxAdapter.setSelectedItemType(selectedItem);
           }
       }

       @Override
       public void onNothingSelected(AdapterView<?> parent) {
           // TODO Auto-generated method stub
           taxAdapter.setSelectedItemType(null);
       }
   };

    private void fetchTax() {
        try {
            if (Utils.isDeviceOnline(getActivity())) {
              showLoading();
                Map map = new HashMap<>();
                map.put("Page", "FetchTax");
                map.put("IsActive", "A");
                map.put("userID", userId);
                map.put("UserLogin", UserLogin);
                map.put("Password", PassWord);
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_TAX_LIST, map);
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
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_TAX_LIST) {
                ModelManager.getInstance().setTaxModel(response);
                if (ModelManager.getInstance().getTaxModel() != null
                        && ModelManager.getInstance().getTaxModel()
                        .getFetchTaxList() != null
                        && ModelManager.getInstance().getTaxModel()
                        .getFetchTaxList().size() > 0) {
                    taxList= ModelManager.getInstance()
                            .getTaxModel().getFetchTaxList();
                    taxAdapter = new POSTaxMasterAdapterFragment(
                            this,taxList,
                            userId, UserLogin, PassWord);
                    taxListView.setAdapter(taxAdapter);
                    taxListView.setEmptyView(emptyView);
                } else {
                    if (ModelManager.getInstance().getTaxModel() != null
                            && ModelManager.getInstance().getTaxModel()
                            .getMessage() != null)
						showMessage(ModelManager.getInstance().getTaxModel()
								.getMessage());
                        taxAdapter = new POSTaxMasterAdapterFragment( this,
                                new ArrayList<TaxModelData>(), userId,
                                UserLogin, PassWord);
                    taxListView.setAdapter(taxAdapter);
                    taxListView.setEmptyView(emptyView);
                }
            } else if (requestId == KEY_DELETE_TAX) {
                ModelManager.getInstance().setTaxModel(response);
                if (ModelManager.getInstance().getTaxModel() != null
                        && ModelManager.getInstance().getTaxModel()
                        .getMessage() != null
                        && ModelManager.getInstance().getTaxModel()
                        .getMessage().length() > 0) {
                    showMessage(ModelManager.getInstance().getTaxModel()
                            .getMessage());
					/*if (taxAdapter != null) {
						taxAdapter.setModelData(ModelManager.getInstance()
								.getTaxModel().getFetchTaxList());
						taxAdapter.notifyDataSetChanged();
					}*/
                    taxAdapter.notifyDataSetChanged();
                } else {
					showMessage("Please try again.");
                }
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
    public void deleteTax(Map map) {
        // TODO Auto-generated method stub
        try {
            if (Utils.isDeviceOnline(getActivity())) {
//                showLoading();
                new RequestManager().sendPostRequest(this, KEY_DELETE_TAX, map);
            } else {
//                showMessage("Internet connection not found");
            }
        } catch (Exception e) {
            // TODO: handle exception
            showMessage("Please try again.");
        }
    }

    @Override
    public void totalRows(String text) {
        // TODO Auto-generated method stub
        totalRecords.setText("Record :"+text);
    }

    @Override
    public void executeService() {
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchTax();
                IssearchBoxOpen=false;
                searchBox.setText("");
            }
        },500);
    }

    @Override
    public void onClick(View v) {

    }

    private void showMessage(String str)
    {
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }
}
