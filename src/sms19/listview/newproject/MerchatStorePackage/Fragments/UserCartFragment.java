package sms19.listview.newproject.MerchatStorePackage.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.kitever.android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.listview.newproject.MerchatStorePackage.Fragments.AddOrderAdapters.CartBuyAdapter;
import sms19.listview.newproject.MerchatStorePackage.Fragments.AddOrderAdapters.InsertToCart;
import sms19.listview.newproject.MerchatStorePackage.Model.ModelBuyCart.CartModel;
import sms19.listview.newproject.MerchatStorePackage.Model.ModelBuyCart.Product;
import sms19.listview.newproject.MerchatStorePackage.UserCartActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserCartFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String userId;
    private String mParam2;
    private UserCartActivity userCartActivity;
    private Thread addCart;
    private List<Product> productList;
    private RecyclerView recyclerUserCart;
    private CartBuyAdapter cartBuyAdapter;
    private Button btnUserCart;

    public UserCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserCartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserCartFragment newInstance(String param1, String param2) {
        UserCartFragment fragment = new UserCartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        userCartActivity = (UserCartActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_cart, container, false);
        productList = new ArrayList<>();

        recyclerUserCart = (RecyclerView) view.findViewById(R.id.recyclerUserCart);
        btnUserCart = (Button) view.findViewById(R.id.btnUserCart);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerUserCart.setLayoutManager(layoutManager);
        recyclerUserCart.setItemAnimator(new DefaultItemAnimator());

        addCartList();
        btnUserCart.setOnClickListener(this);
        return view;
    }

    private void addCartList() {
        HashMap<String, Product> temp = userCartActivity.getCartProduct();
        for (Map.Entry<String, Product> productEntry : temp.entrySet()) {
            Product product = productEntry.getValue();
            if (product.getQuantity() > 0)
                productList.add(product);
        }
        String arr[] = new String[productList.size()];
        for(int i=0; i<productList.size();i++)
            arr[i] = String.valueOf(productList.get(i).getQuantity());
        cartBuyAdapter = new CartBuyAdapter(userCartActivity, productList,arr);
        recyclerUserCart.setAdapter(cartBuyAdapter);
    }

    private String getJson()
    {
        List<Product> tempProduct = cartBuyAdapter.getList();
        for(int i=  0;i<tempProduct.size();i++)
            tempProduct.get(i).setmUserId(Long.parseLong(userId));

        Gson gson = new Gson();
        CartModel tempCartModel = new CartModel(tempProduct);
        return gson.toJson(tempCartModel);
    }

    public void updateCartList(Product product) {
        cartBuyAdapter.setList(product);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUserCart:
                ((InsertToCart)getActivity()).updateCartItem(getJson());
                break;
        }
    }
}
