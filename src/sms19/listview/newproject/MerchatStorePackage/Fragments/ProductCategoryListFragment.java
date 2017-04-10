package sms19.listview.newproject.MerchatStorePackage.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.MerchatStorePackage.Fragments.AddOrderAdapters.ProductCategoryListAdapter;
import sms19.listview.newproject.MerchatStorePackage.Model.Category;
import sms19.listview.newproject.MerchatStorePackage.Model.FetchCategoryModel;
import sms19.listview.newproject.MerchatStorePackage.Model.FetchFeaturedProducts;
import sms19.listview.newproject.MerchatStorePackage.Model.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductCategoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductCategoryListFragment extends Fragment implements NetworkManager, ProductCategoryListAdapter.Actionable, TextWatcher {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private static final int KEY_FETCH_PRODUCT_LIST = 1;
    private static final int KEY_FETCH_CATEGORY_LIST = 2;
    private static final int KEY_PRODUCT_AGAINST_CATEGORY = 3;

    // TODO: Rename and change types of parameters
    private String userLogin, password, userId;

    private ProductCategoryListAdapter productCategoryListAdapter;
    private ArrayList<Product> fetchProduct;
    private List<Category> categories;
    private Spinner spinnerProductCategoryListType;
    private EditText etProductCategoryListSearch;
    private RecyclerView recyclerView;
    private HashMap<Long, Product> cartProducts;
    private RequestManager requestManager;


    public ProductCategoryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3
     * @return A new instance of fragment ProductCategoryListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductCategoryListFragment newInstance(String param1, String param2, String param3) {
        ProductCategoryListFragment fragment = new ProductCategoryListFragment();
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
            userLogin = getArguments().getString(ARG_PARAM2);
            password = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_category_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerProductCategoryList);
        spinnerProductCategoryListType = (Spinner) view.findViewById(R.id.spinnerProductCategoryListType);
        etProductCategoryListSearch = (EditText) view.findViewById(R.id.etProductCategoryListSearch);
        etProductCategoryListSearch.addTextChangedListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        FetchCartCategory();
        fetchFeaturedProductList("0");
        return view;
    }

    @Override
    public void onDestroy() {
        requestManager.getRequestQueue().cancelAll(requestManager.getStringPostRequest().getTag());
        super.onDestroy();
    }

    private void fetchProductList(String type) {
        if (Utils.isDeviceOnline(getActivity())) {
            String userId = Utils.getUserId(getActivity());
            Map map = new HashMap<>();
            map.put("Page", "FetchProductList");
            map.put("userID", userId);
            map.put("Type", type);
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_PRODUCT_LIST, map);
            } catch (Exception e) {
                // TODO: handle exception
//                hideLoading();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void FetchCartCategory() {

        if (Utils.isDeviceOnline(getActivity())) {
            spinnerProductCategoryListType.setOnItemSelectedListener(null);
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, zProgressFragment.newInstance("", "")).addToBackStack(null).commit();
            String userId = Utils.getUserId(getActivity());
            Map map = new HashMap<>();
            map.put("Page", "FetchCartCategory");
            map.put("userID", userId);
            map.put("Password", password);
            map.put("UserLogin", userLogin);
            map.put("IsPublished", "1");
            try {
                requestManager = new RequestManager();
                requestManager.sendPostRequest(this,
                        KEY_FETCH_CATEGORY_LIST, map);
            } catch (Exception e) {
                // TODO: handle exception
//                hideLoading();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchFeaturedProductList(String categoryId) {
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, zProgressFragment.newInstance("", "")).addToBackStack(null).commit();
        if (Utils.isDeviceOnline(getActivity())) {
            Map map = new HashMap<>();
            map.put("Page", "FetchPublishedProductAgainstCategory");
            map.put("userID", userId);
            map.put("Password", password);
            map.put("UserLogin", userLogin);
            map.put("CategoryID", categoryId);

            try {
                requestManager = new RequestManager();
                requestManager.sendPostRequest(this,
                        KEY_PRODUCT_AGAINST_CATEGORY, map);
            } catch (Exception e) {
                // TODO: handle exception
                // hideLoading();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        try {
            Gson gson = new Gson();
            if (response != null && response.length() > 0) {
                if (requestId == KEY_PRODUCT_AGAINST_CATEGORY) {
                    FetchFeaturedProducts fetchFeaturedProducts = gson.fromJson(response, FetchFeaturedProducts.class);

                    if (fetchFeaturedProducts != null
                            && fetchFeaturedProducts.getProduct() != null
                            && fetchFeaturedProducts.getProduct().size() > 0) {
                        productCategoryListAdapter = new ProductCategoryListAdapter(this, fetchFeaturedProducts.getProduct(), cartProducts);
                        recyclerView.setAdapter(productCategoryListAdapter);
                    }
                } else if (requestId == KEY_FETCH_CATEGORY_LIST) {
                    FetchCategoryModel fetchCategoryModel = gson.fromJson(response, FetchCategoryModel.class);
                    if (fetchCategoryModel != null && fetchCategoryModel.getStatus().equalsIgnoreCase("true")) {
                        List<String> categoryList = new ArrayList<>();
                        categoryList.add("default");

                        categories = fetchCategoryModel.getCategory();
                        Iterator<Category> iterator = categories.iterator();
                        while (iterator.hasNext())
                            categoryList.add(iterator.next().getCategoryName());
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryList);
                        spinnerProductCategoryListType.setAdapter(arrayAdapter);
                        spinnerProductCategoryListType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position != 0)
                                    fetchFeaturedProductList(String.valueOf(categories.get(position - 1).getCategoryID()));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }
            getActivity().getSupportFragmentManager().popBackStack();
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void updatePrice(HashMap<Long, Product> cartProducts) {

    }

    @Override
    public void onitemClick(Product product) {
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, SingleUserProductFragment.newInstance(userId, userLogin, password, product), "SingleUserProductFragment").addToBackStack(null).commit();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(productCategoryListAdapter != null)
        productCategoryListAdapter.getFilter().filter(s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
