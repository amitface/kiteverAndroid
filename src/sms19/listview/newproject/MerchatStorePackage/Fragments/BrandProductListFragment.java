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
import sms19.listview.newproject.MerchatStorePackage.Fragments.AddOrderAdapters.BrandProductListAdapter;
import sms19.listview.newproject.MerchatStorePackage.Model.Brand;
import sms19.listview.newproject.MerchatStorePackage.Model.FetchBrandModel;
import sms19.listview.newproject.MerchatStorePackage.Model.FetchFeaturedProducts;
import sms19.listview.newproject.MerchatStorePackage.Model.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrandProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrandProductListFragment extends Fragment implements NetworkManager, BrandProductListAdapter.Actionable, TextWatcher{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";


    private final int KEY_FETCH_PRODUCT_LIST = 1;
    private final int KEY_FETCH_PRODUCT_CATEGORY_LIST = 2;
    private final int KEY_FETCH_PRODUCT_BRAND_LIST = 3;
    private final int KEY_FETCH_BRAND_LIST = 4;

    // TODO: Rename and change types of parameters
    private String userLogin, password, userId;

    private BrandProductListAdapter brandProductListAdapter;
    private ArrayList<Product> fetchProduct;
    private RecyclerView recyclerView;
    private HashMap<Long, Product> cartProducts;
    private RequestManager requestManager;
    private Spinner spinnerBrandProductListType;
    private EditText etBrandProductListSearch;

    public BrandProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrandProductListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrandProductListFragment newInstance(String param1, String param2, String param3) {
        BrandProductListFragment fragment = new BrandProductListFragment();
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
        requestManager = new RequestManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brand_product_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerBrandProductList);
        spinnerBrandProductListType = (Spinner) view.findViewById(R.id.spinnerBrandProductListType);
        etBrandProductListSearch = (EditText) view.findViewById(R.id.etBrandProductListSearch);

        etBrandProductListSearch.addTextChangedListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fetchFeaturedProductCategoryList();
        fetchBrandist();
        return view;
    }

    private void fetchBrandist() {
        if (Utils.isDeviceOnline(getActivity())) {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, zProgressFragment.newInstance("", "")).addToBackStack(null).commit();
            Map map = new HashMap<>();
            map.put("Page", "FetchCartBrand");
            map.put("userID", userId);
            map.put("Password", password);
            map.put("UserLogin", userLogin);
            map.put("IsPublished", "1");

            try {
                requestManager.sendPostRequest(this,
                        KEY_FETCH_BRAND_LIST, map);
            } catch (Exception e) {
                // TODO: handle exception
//                hideLoading();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchFeaturedProductCategoryList() {
        if (Utils.isDeviceOnline(getActivity())) {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, zProgressFragment.newInstance("", "")).addToBackStack(null).commit();
            Map map = new HashMap<>();
            map.put("Page", "FetchPublishedProductAgainstCategory");
            map.put("userID", userId);
            map.put("Password", password);
            map.put("UserLogin", userLogin);
            map.put("CategoryID", "0");

            try {
                requestManager = new RequestManager();
                requestManager.sendPostRequest(this,
                        KEY_FETCH_PRODUCT_CATEGORY_LIST, map);
            } catch (Exception e) {
                // TODO: handle exception
                // hideLoading();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchFeaturedProductBrandList(String BrandId) {
        if (Utils.isDeviceOnline(getActivity())) {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, zProgressFragment.newInstance("", "")).addToBackStack(null).commit();
            Map map = new HashMap<>();
            map.put("Page", "FetchPublishedProductAgainstBrand");
            map.put("userID", userId);
            map.put("Password", password);
            map.put("UserLogin", userLogin);
            map.put("BrandID", BrandId);

            try {
                requestManager = new RequestManager();
                requestManager.sendPostRequest(this,
                        KEY_FETCH_PRODUCT_BRAND_LIST, map);
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
                if (requestId == KEY_FETCH_PRODUCT_CATEGORY_LIST) {

                    FetchFeaturedProducts fetchFeaturedProducts = gson.fromJson(response, FetchFeaturedProducts.class);

                    if (fetchFeaturedProducts != null
                            && fetchFeaturedProducts.getProduct() != null
                            && fetchFeaturedProducts.getProduct().size() > 0) {
                        brandProductListAdapter = new BrandProductListAdapter(this, fetchFeaturedProducts.getProduct(), cartProducts);
                        recyclerView.setAdapter(brandProductListAdapter);
                    }
                } else if (requestId == KEY_FETCH_PRODUCT_BRAND_LIST) {
                    FetchFeaturedProducts fetchFeaturedProducts = gson.fromJson(response, FetchFeaturedProducts.class);

                    if (fetchFeaturedProducts != null
                            && fetchFeaturedProducts.getProduct() != null
                            && fetchFeaturedProducts.getProduct().size() > 0) {
                        brandProductListAdapter = new BrandProductListAdapter(this, fetchFeaturedProducts.getProduct(), cartProducts);
                        recyclerView.setAdapter(brandProductListAdapter);
                    }else
                    {
                        brandProductListAdapter = new BrandProductListAdapter(this, new ArrayList<sms19.listview.newproject.MerchatStorePackage.Model.Product>(), cartProducts);
                        recyclerView.setAdapter(brandProductListAdapter);
                    }
                } else if (requestId == KEY_FETCH_BRAND_LIST) {
                    FetchBrandModel fetchBrandModel = gson.fromJson(response, FetchBrandModel.class);
                    Iterator<Brand> brandIterator = fetchBrandModel.getBrand().iterator();
                    final List<Brand> brand = fetchBrandModel.getBrand();
                    List<String> listBrand = new ArrayList<>();
                    listBrand.add("Default");
                    while (brandIterator.hasNext()) {
                        listBrand.add(brandIterator.next().getBrandName());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listBrand);
                    spinnerBrandProductListType.setAdapter(arrayAdapter);
                    spinnerBrandProductListType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                fetchFeaturedProductBrandList(String.valueOf(brand.get(position - 1).getBrandID()));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            getActivity().getSupportFragmentManager().popBackStack();
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void onDestroy() {
        requestManager.getRequestQueue().cancelAll(requestManager.getStringPostRequest().getTag());
        super.onDestroy();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

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

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(brandProductListAdapter!=null)
            {
                brandProductListAdapter.getFilter().filter(s);
            }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
