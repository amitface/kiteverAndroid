package sms19.listview.newproject.MerchatStorePackage.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.MerchatStorePackage.Fragments.AddOrderAdapters.UserProductListAdapter;
import sms19.listview.newproject.MerchatStorePackage.Model.FetchFeaturedProducts;
import sms19.listview.newproject.MerchatStorePackage.Model.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProductListFragment extends Fragment implements NetworkManager, UserProductListAdapter.Actionable, View.OnClickListener, TextWatcher {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private final int KEY_FETCH_PRODUCT_LIST = 1;

    // TODO: Rename and change types of parameters
    private String userLogin, password, userId;

    private UserProductListAdapter userProductListAdapter;
    private ArrayList<Product> fetchProduct;
    private RecyclerView recyclerUserProductList;
    private HashMap<Long, Product> cartProducts;
    private RequestManager requestManager;
    private Button btnUserProductListItemDone;
    private Spinner spinnerUserProductListType;
    private EditText etUserProductListSearch;

    public UserProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProductListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProductListFragment newInstance(String param1, String param2, String param3) {
        UserProductListFragment fragment = new UserProductListFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_product_list, container, false);

        cartProducts = new HashMap<>();
        fetchProduct = new ArrayList<>();
        recyclerUserProductList = (RecyclerView) view.findViewById(R.id.recyclerUserProductList);
        etUserProductListSearch = (EditText) view.findViewById(R.id.etUserProductListSearch);
        spinnerUserProductListType = (Spinner) view.findViewById(R.id.spinnerUserProductListType);

        etUserProductListSearch.addTextChangedListener(this);
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
        spinnerUserProductListType.setAdapter(typeAdapter);
        //selectType.setSelection(0);
        spinnerUserProductListType.setOnItemSelectedListener(itemSelectedListener);

//        btnUserProductListItemDone = (Button) view.findViewById(R.id.btnUserProductListItemDone);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerUserProductList.setItemAnimator(new DefaultItemAnimator());
        recyclerUserProductList.setLayoutManager(layoutManager);
        fetchFeaturedProductList();

//        btnUserProductListItemDone.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        requestManager.getRequestQueue().cancelAll(this);
        super.onDestroy();
    }

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            etUserProductListSearch.setText("");
            Spinner spinSelected = (Spinner) parent;
            String selectedItem = (String) spinSelected.getSelectedItem();
            if (position == 3 || position == 4)
                etUserProductListSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
            else etUserProductListSearch.setInputType(InputType.TYPE_CLASS_TEXT);
            if (userProductListAdapter != null) {
                userProductListAdapter.setSelectedItemType(position);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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

    private void fetchFeaturedProductList() {
        if (Utils.isDeviceOnline(getActivity())) {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, zProgressFragment.newInstance("", "")).addToBackStack(null).commit();
            Map map = new HashMap<>();
            map.put("Page", "FetchFeaturedProduct");
            map.put("userID", userId);
            map.put("Password", password);
            map.put("UserLogin", userLogin);
            map.put("IsFeatured", "1");

            try {
                requestManager = new RequestManager();
                requestManager.sendPostRequest(this,
                        KEY_FETCH_PRODUCT_LIST, map);
            } catch (Exception e) {
                // TODO: handle exception
//                hideLoading();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        getActivity().getSupportFragmentManager().popBackStack();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_PRODUCT_LIST) {
                Gson gson = new Gson();
                FetchFeaturedProducts fetchFeaturedProducts = gson.fromJson(response, FetchFeaturedProducts.class);

                if (fetchFeaturedProducts != null
                        && fetchFeaturedProducts.getProduct() != null
                        && fetchFeaturedProducts.getProduct().size() > 0) {
                    userProductListAdapter = new UserProductListAdapter(this, fetchFeaturedProducts.getProduct(), cartProducts);
                    recyclerUserProductList.setAdapter(userProductListAdapter);
                }
            }
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
    public void onClick(View v) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (userProductListAdapter!=null)
            userProductListAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
