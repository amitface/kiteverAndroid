package com.kitever.pos.fragment;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.kitever.pos.activity.POSCategoryAddOrUpdateScreen;
import com.kitever.pos.adapter.POSCategoryAdapter;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.fragment.adapters.POSCategoryAdapterFragment;
import com.kitever.pos.model.data.PosCategoryModelData;
import com.kitever.pos.model.manager.ModelManager;
import com.kitever.utils.TotalRows;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static android.app.Activity.RESULT_OK;
import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link POSCategoryScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class POSCategoryScreenFragment extends Fragment implements NetworkManager, POSCategoryAdapterFragment.Actionable, TotalRows, ExecuteService {
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

    private int KEY_FETCH_CATEGORY_LIST = 1;
    private static final int ADD_CATEGORY = 2;
    private ListView categoryListView;
    private POSCategoryAdapterFragment categoryAdapter;
    //private final int KEY_DELETE_CATEGEORY = 2;
    private final int KEY_ACTIVATE_CATEGORY = 3;
    private TextView no_list_txt, totalRecords, noRecord, active_legend;
    private EditText searchBox;
    private Spinner selectType;
    private RelativeLayout emptyView;
    private ImageView search_icon;
    private boolean IssearchBoxOpen=false;
    private ProgressBar progressBar;
    private TextView catTitle,parentTitle,typeTitle,actionTitle;
    private boolean catnameSort=false;

    public POSCategoryScreenFragment() {
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment POSCategoryScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static POSCategoryScreenFragment newInstance(String param1, String param2, String param3) {
        POSCategoryScreenFragment fragment = new POSCategoryScreenFragment();
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
       View view  = inflater.inflate(R.layout.pos_category_layout,container,false);
       setScreen(view);
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchCategoryList();
                selectType.setSelection(0);
                searchBox.setText("");
                searchBox.setVisibility(View.GONE);
                IssearchBoxOpen=false;
            }
        },500);
       return view;
    }

 /*   @Override
    public void onResume() {
        super.onResume();

    }
*/

    public String getUserId() {
        return UserId;
    }

    public String getUserLogin() {
        return UserLogin;
    }

    public String getPassWord() {
        return PassWord;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CATEGORY)
        {
            if(resultCode==RESULT_OK) {
            selectType.setSelection(0);
            searchBox.setText("");
            searchBox.setVisibility(View.GONE);
            IssearchBoxOpen=false;
            String response = data.getStringExtra("response");
            if (response != null && response.length() > 0) {
                Log.i("Category List", "Resoponse - " + response);
                    ModelManager.getInstance().setCategoryModel(response);
                    if (ModelManager.getInstance().getCategoryModel() != null
                            && (ModelManager.getInstance().getCategoryModel()
                            .getCategoryModelDataList() != null && ModelManager
                            .getInstance().getCategoryModel()
                            .getCategoryModelDataList().size() > 0)) {
                      /*  categoryAdapter.changeList(ModelManager
                                .getInstance().getCategoryModel()
                                .getCategoryModelDataList());*/

                        categoryAdapter = new POSCategoryAdapterFragment(this, ModelManager
                                .getInstance().getCategoryModel()
                                .getCategoryModelDataList(), getUserId(),
                                getPassWord(), getUserLogin());
                        categoryListView.setAdapter(categoryAdapter);

                       // categoryListView.setAdapter(categoryAdapter);
                        categoryListView.setEmptyView(emptyView);
                    } else {
                        categoryAdapter = new POSCategoryAdapterFragment(this, new ArrayList<PosCategoryModelData>(),
                                getUserId(),
                                getPassWord(), getUserLogin());
                        categoryListView.setAdapter(categoryAdapter);
                        categoryListView.setEmptyView(emptyView);
                    }
                }
            }
        }
    }

    private void fetchCategoryList() {
        if (Utils.isDeviceOnline(getActivity())) {
           showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchCategory");
            map.put("IsActive", "");
            map.put("userID", getUserId());
            map.put("UserLogin", getUserLogin());
            map.put("Password", getPassWord());
            Log.i("fetchCategoryList","---"+map.toString());
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_CATEGORY_LIST, map);
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("Please try again.");
            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    private void setScreen(View view) {
        selectType = (Spinner) view.findViewById(R.id.select_type_spinner);
        searchBox = (EditText) view.findViewById(R.id.edit_search);
        ImageView adavanceSearch = (ImageView) view.findViewById(R.id.advance_search);
        categoryListView = (ListView) view.findViewById(R.id.category_list_view);
        no_list_txt = (TextView) view.findViewById(R.id.no_list_txt);
        totalRecords = (TextView) view.findViewById(R.id.category_total_rows);
        search_icon= (ImageView) view.findViewById(R.id.search_icon);
       // active_legend = (TextView) view.findViewById(R.id.active_legend);
        emptyView= (RelativeLayout) view.findViewById(R.id.category_emptyElement);
        noRecord = (TextView) view.findViewById(R.id.no_record);

        RelativeLayout page_main_layout=(RelativeLayout)view.findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        catTitle= (TextView) view.findViewById(R.id.catTitle);
        parentTitle= (TextView) view.findViewById(R.id.parentTitle);
        typeTitle= (TextView) view.findViewById(R.id.typeTitle);
        actionTitle= (TextView) view.findViewById(R.id.actionTitle);

        setRobotoThinFont(catTitle,getActivity());
        setRobotoThinFont(parentTitle,getActivity());
        setRobotoThinFont(typeTitle,getActivity());
        setRobotoThinFont(actionTitle,getActivity());
        setRobotoThinFont(totalRecords,getActivity());
        setRobotoThinFont(searchBox,getActivity());

        catTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        parentTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        typeTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        actionTitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        totalRecords.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        searchBox.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        categoryListView.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));



        MoonIcon mIcon = new MoonIcon(this);
        mIcon.setTextfont(noRecord);
      //  mIcon.setTextfont(active_legend);

       // active_legend.append(" For Active/Inactive");

        no_list_txt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        POSCategoryAddOrUpdateScreen.class);
                intent.putExtra("screen_name", "Add Category");
                startActivity(intent);
            }
        });
        FloatingActionButton fabButton = (FloatingActionButton) view.findViewById(R.id.category_add);

        fabButton.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor(CustomStyle.FLOATING_ACTION_BACKGROUND)));
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        POSCategoryAddOrUpdateScreen.class);
                intent.putExtra("screen_name", "Add Category");
                startActivityForResult(intent,ADD_CATEGORY);
            }
        });
        //9990015887
        ArrayList<String> typeList = new ArrayList<String>();
        // typeList.add("Select");
        typeList.add("Category");
        typeList.add("Parent");
        typeList.add("Type");
        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectType.setAdapter(typeAdapter);
        //selectType.setSelection(0);
        selectType.setOnItemSelectedListener(itemSelectedListener);

        // if (categoryAdapter != null) {
        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                if (categoryAdapter != null) {
                    categoryAdapter.getFilter().filter(
                            cs);
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
        search_icon.setOnClickListener(new View.OnClickListener() {
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
        });

        catTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortingByName(catnameSort);
                if(catnameSort) catnameSort=false;
                else catnameSort=true;
            }
        });
    }



    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            searchBox.setText("");
            Spinner spinSelected = (Spinner) parent;
            String selectedItem = (String) spinSelected.getSelectedItem();
            if (categoryAdapter != null) {
                categoryAdapter.setSelectedItemType(selectedItem);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
            categoryAdapter.setSelectedItemType(null);
        }
    };

    @Override
    public void onReceiveResponse(int requestId, String response) {

        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_CATEGORY_LIST) {
                ModelManager.getInstance().setCategoryModel(response);
                if (ModelManager.getInstance().getCategoryModel() != null
                        && (ModelManager.getInstance().getCategoryModel()
                        .getCategoryModelDataList() != null && ModelManager
                        .getInstance().getCategoryModel()
                        .getCategoryModelDataList().size() > 0)) {
                    categoryAdapter = new POSCategoryAdapterFragment(this, ModelManager
                            .getInstance().getCategoryModel()
                            .getCategoryModelDataList(), getUserId(),
                            getPassWord(), getUserLogin());
                    categoryListView.setAdapter(categoryAdapter);
                    categoryListView
                            .setEmptyView(emptyView);

                } else {
                    categoryAdapter = new POSCategoryAdapterFragment(this, new ArrayList<PosCategoryModelData>(),
                            getUserId(),
                            getPassWord(), getUserLogin());
                    categoryListView.setAdapter(categoryAdapter);
                    categoryListView
                            .setEmptyView(emptyView);

                }
            } else if (requestId == KEY_ACTIVATE_CATEGORY) {
                Log.i("response cat", "response= " + response);
                if(response.contains("Column1"))
                {
                    try {
                        JSONObject catResponse = new JSONObject(response);
                        JSONArray objArray = catResponse.getJSONArray("SetStatus");
                        Toast.makeText(getActivity(), objArray.getJSONObject(0).getString("Column1"), Toast.LENGTH_SHORT).show();
                    }
                    catch(JSONException e){e.printStackTrace();}
                }
                else
                {
                    fetchCategoryList();
                    categoryAdapter.notifyDataSetChanged();
                    categoryAdapter.toastmsg();
                }


            }
        }

    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading()
    {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
       hideLoading();
     showMessage(getActivity().getResources().getString(R.string.volleyError).toString());
    }

    private  void SortingByName(boolean b)
    {
        ArrayList<PosCategoryModelData> categoryModelList=categoryAdapter.getList();
        if(categoryModelList.size()>0) {
            if (b) Collections.sort(categoryModelList, new NameComparatorAsc());
            else Collections.sort(categoryModelList, new NameComparatorDesc());
            categoryAdapter.notifyDataSetChanged();
        }
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




    @Override
    public void deleteCategory(Map map) {
        // TODO Auto-generated method stub
        if (Utils.isDeviceOnline(getActivity())) {
            try {
//                showLoading();


            } catch (Exception e) {
                // TODO: handle exception
//                hideLoading();
//                showMessage("Please try again.");
            }
        } else {
//            hideLoading();
//            showMessage("Internet connection not found");
        }

    }

    @Override
    public void totalRows(String text) {
        totalRecords.setText("Records : "+text);
    }

    @Override
    public void activateCategory(Map map) {

        if (Utils.isDeviceOnline(getActivity())) {
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_ACTIVATE_CATEGORY, map);
            } catch (Exception e) {
                Log.i("category error", "Error -" + e.getMessage());
//                hideLoading();
//                showMessage("Please try again.");
            }
        } else {
//            showMessage("Internet connection not found");
        }

    }

    @Override
    public void executeService() {
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchCategoryList();
                selectType.setSelection(0);
                searchBox.setText("");
                searchBox.setVisibility(View.GONE);
                IssearchBoxOpen=false;
            }
        },500);
    }

    private void showMessage(String str)
    {
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }

}
