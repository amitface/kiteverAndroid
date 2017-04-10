package com.kitever.pos.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.activity.POSAddUpdateProduct;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.fragment.adapters.POSProductAdapterFragment;
import com.kitever.pos.fragment.purchaseList.Inventory.UpdateCurrentInventory;
import com.kitever.pos.model.data.ProductModelData;
import com.kitever.pos.model.manager.ModelManager;
import com.kitever.utils.TotalRows;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static android.app.Activity.RESULT_OK;
import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

//import com.kitever.customviews.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PosProductScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PosProductScreenFragment extends Fragment implements NetworkManager, POSProductAdapterFragment.Actionable, TotalRows, ExecuteService, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String UserId, UserLogin, PassWord;

    private final int KEY_FETCH_PRODUCT_LIST = 1;
    private final int KEY_DELETE_PRODUCT = 2;
    private final int KEY_ACTIVATE_PRODUCT = 3;
    private final int KEY_ADD_INVENTORY = 6;

    private ListView productListView;
    private TextView no_record, no_record_txt, totalRecords;
    private POSProductAdapterFragment productAdapter;
    private EditText searchBox, etInventoryAddQuantity,
            etInventoryAddRemarks;
    private RelativeLayout emptyView;
    private static final int ADD_PRODUCT = 1;
    private TextView price_header, btnInventoryAdd, tvInventoryfinalQuantity, tvInventoryQuantity;
    private LinearLayout layoutInventoryAddQuantity;
    private int activePosition = 0;
    private String isActive = "A", productID = "", inventory = "", remarks = "";
    private ImageView search_iconLeft, search_icon;
    boolean IssearchBoxOpen = false;
    private Spinner selectType;
    private ProgressBar progressBar;
    private Dialog dialogAdd;
    private int position;

    public PosProductScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3 Parameter 3.
     * @return A new instance of fragment PosProductScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PosProductScreenFragment newInstance(String param1, String param2, String param3) {
        PosProductScreenFragment fragment = new PosProductScreenFragment();
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
        View view = inflater.inflate(R.layout.pos_products_layout, container, false);
        MoonIcon mIcon = new MoonIcon(getActivity());
        // ImageView adavanceSearch = (ImageView)
        // findViewById(R.id.advance_search);
        productListView = (ListView) view.findViewById(R.id.product_list_view);
        productListView.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));
        no_record = (TextView) view.findViewById(R.id.no_record);
        no_record_txt = (TextView) view.findViewById(R.id.no_record_txt);
        //  price_header = (TextView) view.findViewById(R.id.price_header);
        search_icon = (ImageView) view.findViewById(R.id.search_icon);
        // search_iconLeft= (ImageView) view.findViewById(R.id.search_iconLeft);
        progressBar = (ProgressBar) view.findViewById(R.id.marker_progress);
        progressBar.setVisibility(View.GONE);

        totalRecords = (TextView) view.findViewById(R.id.product_total_rows);
        setRobotoThinFont(totalRecords, getActivity());
        totalRecords.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        emptyView = (RelativeLayout) view.findViewById(R.id.product_emptyElement);
        mIcon.setTextfont(no_record);
        // productListView.setEmptyView(emptyView);

        FloatingActionButton fabButton = (FloatingActionButton) view.findViewById(R.id.product_add);
        fabButton.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor(CustomStyle.FLOATING_ACTION_BACKGROUND)));

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        POSAddUpdateProduct.class);
                intent.putExtra("screen_name", "Add Product");
                startActivityForResult(intent, ADD_PRODUCT);
            }
        });

        selectType = (Spinner) view.findViewById(R.id.select_type_spinner);
        searchBox = (EditText) view.findViewById(R.id.edit_search);

        setRobotoThinFont(searchBox, getActivity());
        searchBox.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        ArrayList<String> typeList = new ArrayList<String>();
        // typeList.add("Default");
        typeList.add("Product");
        typeList.add("Category");
        typeList.add("Brand");
        typeList.add("Price >=");
        typeList.add("Price <=");
        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectType.setAdapter(typeAdapter);
        //selectType.setSelection(0);
        selectType.setOnItemSelectedListener(itemSelectedListener);

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (IssearchBoxOpen) {
                    searchBox.setVisibility(View.GONE);
                    IssearchBoxOpen = false;
                } else {
                    searchBox.setVisibility(View.VISIBLE);
                    IssearchBoxOpen = true;
                }
            }
        });


        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                String str = cs.toString();
                if (productAdapter != null) {
                    productAdapter.getFilter()
                            .filter(cs);
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

        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchProductList();
                selectType.setSelection(0);
                IssearchBoxOpen = false;
                searchBox.setText("");
                searchBox.setVisibility(View.GONE);
            }
        }, 500);

        return view;
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PRODUCT) {
            if (resultCode == RESULT_OK) {
                String response = data.getStringExtra("response");
                if (response != null && response.length() > 0) {
                    Gson gson = new Gson();
//                    FetchProductModel fetchProductModel = gson.fromJson(response,)
                    Log.i("Product List", "Response - " + response);
                    ModelManager.getInstance().setProductModel(response);
                    if (ModelManager.getInstance().getProductModel() != null) {
                        if (ModelManager.getInstance().getProductModel()
                                .getProductList() != null
                                && ModelManager.getInstance().getProductModel()
                                .getProductList().size() > 0) {
                            productAdapter.changeList(ModelManager
                                    .getInstance().getProductModel()
                                    .getProductList());
                            productListView.setAdapter(productAdapter);
                            productListView.setEmptyView(emptyView);
                        } else {
                            productAdapter = new POSProductAdapterFragment(
                                    this, new ArrayList<ProductModelData>(), UserId,
                                    PassWord, UserLogin);
                            productListView.setAdapter(productAdapter);
                            productListView.setEmptyView(emptyView);

                        }
                    }
                }
            }
        }
    }

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            searchBox.setText("");
            Spinner spinSelected = (Spinner) parent;
            String selectedItem = (String) spinSelected.getSelectedItem();
            if (position == 3 || position == 4) searchBox.setInputType(InputType.TYPE_CLASS_NUMBER);
            else searchBox.setInputType(InputType.TYPE_CLASS_TEXT);
            if (productAdapter != null) {
                productAdapter.setSelectedItemType(selectedItem);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
            productAdapter.setSelectedItemType("Product");
        }
    };

    private void fetchProductList() {
        if (Utils.isDeviceOnline(getActivity())) {
            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchProduct");
            map.put("IsActive", ""); //S /A/I
            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);
            try {
                new RequestManager().sendPostRequest(PosProductScreenFragment.this,
                        KEY_FETCH_PRODUCT_LIST, map);
            } catch (Exception e) {
                // TODO: handle exception
                hideLoading();
                showMessage("Please try again.");
            }
        } else {
            showMessage("Internet connection not found");
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub

        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_PRODUCT_LIST) {
                Log.i("Product List", "Resoponse - " + response);
                ModelManager.getInstance().setProductModel(response);
                if (ModelManager.getInstance().getProductModel() != null) {
                    if (ModelManager.getInstance().getProductModel()
                            .getProductList() != null
                            && ModelManager.getInstance().getProductModel()
                            .getProductList().size() > 0) {
                        productAdapter = new POSProductAdapterFragment(
                                this, ModelManager
                                .getInstance().getProductModel()
                                .getProductList(), UserId,
                                PassWord, UserLogin);
                        productListView.setAdapter(productAdapter);
                        productListView.setEmptyView(emptyView);
                    } else {
                        productAdapter = new POSProductAdapterFragment(
                                this, new ArrayList<ProductModelData>(), UserId,
                                PassWord, UserLogin);
                        productListView.setAdapter(productAdapter);
                        productListView.setEmptyView(emptyView);

                    }

                }
            } else if (requestId == KEY_DELETE_PRODUCT) {
                ModelManager.getInstance().setProductModel(response);

                if (ModelManager.getInstance().getProductModel() != null
                        && ModelManager.getInstance().getProductModel()
                        .getMessage() != null
                        && ModelManager.getInstance().getProductModel()
                        .getMessage().length() > 0) {
                    showMessage(ModelManager.getInstance().getProductModel()
                            .getMessage());
                    if (productAdapter != null) {
                        productAdapter.setModelData(ModelManager.getInstance()
                                .getProductModel().getProductList());
                        productAdapter.notifyDataSetChanged();
                    }
                } else {
                    showMessage("Please try again.");
                }
            } else if (requestId == KEY_ACTIVATE_PRODUCT) {
                Log.i("Active", "response- " + response);
                if (response.contains("Product cannot be activated")) {
                    try {
                        JSONObject catResponse = new JSONObject(response);
                        JSONArray objArray = catResponse.getJSONArray("SetStatus");
                        Toast.makeText(getActivity(), objArray.getJSONObject(0).getString("Column1"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    isActive = isActive.equals("A") ? "I" : "A";
                    ModelManager.getInstance().getProductModel().getProductList().get(activePosition).setIsActive(isActive);
                    productAdapter.toastmsg();
                    productAdapter.notifyDataSetChanged();
                }
            } else if (requestId == KEY_ADD_INVENTORY) {
                Log.i("Add Inventory : ", response);
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                UpdateCurrentInventory updateCurrentInventory = gson.fromJson(response, UpdateCurrentInventory.class);
                productAdapter.updateList(position, updateCurrentInventory.getAddInventory().get(0).getBalance());
                dialogAdd.dismiss();
            }
        } else {
            showMessage("Please try again.");
        }
        hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        hideLoading();
        showMessage(getActivity().getResources().getString(R.string.volleyError).toString());
    }

    @Override
    public void deleteProduct(Map map) {
        // TODO Auto-generated method stub
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                new RequestManager().sendPostRequest(PosProductScreenFragment.this,
                        KEY_DELETE_PRODUCT, map);
            } catch (Exception e) {
                // TODO: handle exception
//                hideLoading();
                showMessage("Please try again.");
            }
        } else {
            showMessage("Internet connection not found");
        }
    }

    @Override
    public void activateProduct(Map map, int activePosition) {

        this.activePosition = activePosition;
        isActive = map.get("Status").toString();
        // TODO Auto-generated method stub
        if (Utils.isDeviceOnline(getActivity())) {
            try {
                Log.i("Request ", "--product--" + map.toString());
                new RequestManager().sendPostRequest(PosProductScreenFragment.this,
                        KEY_ACTIVATE_PRODUCT, map);
            } catch (Exception e) {
                Log.i("error", "-Error-" + e.getMessage());
                showMessage("Please try again.");
            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    @Override
    public void update(int position, ProductModelData productModelData) {
        showAddInventory(position, productModelData);
    }

    @Override
    public void totalRows(String text) {
        totalRecords.setText("Records : " + text);
    }

    @Override
    public void executeService() {

    }

    @Override
    public void onClick(View v) {

    }

    private void showMessage(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    private void showAddInventory(final int position, final ProductModelData productModelData) {
        this.productID = productModelData.getProductID();
        this.position = position;
        dialogAdd = new Dialog(getActivity());
        dialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAdd.setContentView(R.layout.inventory_add);
        etInventoryAddQuantity = (EditText) dialogAdd.findViewById(R.id.etInventoryAddQuantity);
        etInventoryAddRemarks = (EditText) dialogAdd.findViewById(R.id.etInventoryAddRemarks);
        btnInventoryAdd = (TextView) dialogAdd.findViewById(R.id.btnInventoryAdd);
        layoutInventoryAddQuantity = (LinearLayout) dialogAdd.findViewById(R.id.layoutInventoryAddQuantity);
        tvInventoryQuantity = (TextView) dialogAdd.findViewById(R.id.tvInventoryQuantity);
        tvInventoryfinalQuantity = (TextView) dialogAdd.findViewById(R.id.tvInventoryfinalQuantity);
        tvInventoryQuantity.setText(productModelData.getAvailableStock());
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
                    Double temp = Double.parseDouble(productModelData.getAvailableStock())+Long.parseLong(etInventoryAddQuantity.getText().toString());
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
                if (etInventoryAddQuantity.getText() != null && etInventoryAddQuantity.getText().toString().trim().length() != 0) {
                    inventory = etInventoryAddQuantity.getText().toString();
                    if (etInventoryAddRemarks.getText() != null)
                        remarks = etInventoryAddRemarks.getText().toString();

                    AddInventory(productID, inventory, remarks);
                } else
                    etInventoryAddQuantity.setError("Cannot be empty");
            }
        });
        dialogAdd.show();
    }

    public void AddInventory(String ProductId, String Quantity, String Remarks) {
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
                Toast.makeText(getActivity(), getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getActivity(), getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }
}
