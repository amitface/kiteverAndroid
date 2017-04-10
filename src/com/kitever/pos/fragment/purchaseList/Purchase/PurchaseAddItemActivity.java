package com.kitever.pos.fragment.purchaseList.Purchase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;


public class PurchaseAddItemActivity extends AppCompatActivity implements NetworkManager, PurchaseAddItemAdapter.Actionable, View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {

    private final int KEY_FETCH_PRODUCT_LIST = 1;
    private final int REQUEST_ADD_ITEM = 2;
    private MoonIcon mIcon;
    private RecyclerView recyclerViewPurchaseAddItem;
    private Button btnPurchaseAddItemDone;
    private TextView tvPurchaseAddItemGoCart, tvPurchaseAddItemSelect, tvPurchaseAddItemCheckDone,
            etPurchaseAddItemSearch;
    private Spinner spinnerPurchaseAddItemType;
    private PurchaseAddItemAdapter purchaseAddItemAdapter;
    private ArrayList<FetchProduct> fetchProduct;
    private ArrayList<FetchProduct> unHashedCartProduct;
    private HashMap<Long, FetchProduct> cartProducts;
    private PurchaseInsertModel purchaseInsertModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_purchase_add_item);
        setUI();
        purchaseInsertModel = getIntent().getParcelableExtra("PurchaseInsert");
        if(getIntent().getParcelableArrayListExtra("FinalProducts")!=null)
        {

            unHashedCartProduct =  ((PurchaseProductModel)getIntent().getParcelableExtra("CartProducts")).getFetchProduct();
            Iterator<FetchProduct> iterator = unHashedCartProduct.iterator();
            while(iterator.hasNext())
            {
                FetchProduct temp = iterator.next();
                cartProducts.put(temp.getProductID(),temp);
            }
            fetchProduct = getIntent().getParcelableArrayListExtra("FinalProducts");
            purchaseAddItemAdapter = new PurchaseAddItemAdapter(this,fetchProduct,cartProducts);
            recyclerViewPurchaseAddItem.setAdapter(purchaseAddItemAdapter);
            updatePrice(cartProducts);
        }
        else
        fetchProductList(purchaseInsertModel.getType());
    }

    private void setUI() {
        mIcon = new MoonIcon(this);
        recyclerViewPurchaseAddItem = (RecyclerView) findViewById(R.id.recyclerViewPurchaseAddItem);

        tvPurchaseAddItemGoCart = (TextView) findViewById(R.id.tvPurchaseAddItemGoCart);
        tvPurchaseAddItemSelect = (TextView) findViewById(R.id.tvPurchaseAddItemSelect);
        tvPurchaseAddItemCheckDone = (TextView) findViewById(R.id.tvPurchaseAddItemCheckDone);
        spinnerPurchaseAddItemType = (Spinner) findViewById(R.id.spinnerPurchaseAddItemType);
        etPurchaseAddItemSearch = (EditText) findViewById(R.id.etPurchaseAddItemSearch);
        btnPurchaseAddItemDone = (Button) findViewById(R.id.btnPurchaseAddItemDone);


        setRobotoThinFont(tvPurchaseAddItemGoCart,this);
        setRobotoThinFont(tvPurchaseAddItemSelect,this);
        setRobotoThinFont(etPurchaseAddItemSearch,this);


        tvPurchaseAddItemGoCart.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        tvPurchaseAddItemSelect.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        tvPurchaseAddItemCheckDone.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));

        etPurchaseAddItemSearch.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        LinearLayout select_cart= (LinearLayout) findViewById(R.id.select_cart);
        btnPurchaseAddItemDone.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        btnPurchaseAddItemDone.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        select_cart.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("Product");
        typeList.add("Category");
        typeList.add("Brand");

        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(this,
                android.R.layout.simple_list_item_1, typeList);
        //typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPurchaseAddItemType.setAdapter(typeAdapter);
        spinnerPurchaseAddItemType.setOnItemSelectedListener(this);
        etPurchaseAddItemSearch.addTextChangedListener(this);
        fetchProduct = new ArrayList<FetchProduct>();
        cartProducts = new HashMap<Long, FetchProduct>();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewPurchaseAddItem.setLayoutManager(layoutManager);
        recyclerViewPurchaseAddItem.setItemAnimator(new DefaultItemAnimator());

        setRobotoThinFont(tvPurchaseAddItemSelect, this);
        setRobotoThinFont(etPurchaseAddItemSearch, this);

        mIcon.setTextfont(tvPurchaseAddItemGoCart);
        mIcon.setTextfont(tvPurchaseAddItemCheckDone);
        tvPurchaseAddItemCheckDone.setOnClickListener(this);
        btnPurchaseAddItemDone.setOnClickListener(this);
    }

    private void fetchProductList(String type) {
        if (Utils.isDeviceOnline(this)) {
            String userId = Utils.getUserId(this);
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
            Toast.makeText(this,"Internet connection not found",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_PRODUCT_LIST) {
                Gson gson = new Gson();
                PurchaseProductModel purchaseProductModel = gson.fromJson(response, PurchaseProductModel.class);

                if (purchaseProductModel != null
                        && purchaseProductModel.getFetchProduct() != null
                        && purchaseProductModel.getFetchProduct().size() > 0) {
                    Iterator iterator = purchaseProductModel.getFetchProduct().iterator();
                    while (iterator.hasNext()) {
                        fetchProduct.add((FetchProduct) iterator.next());
                    }
                    purchaseAddItemAdapter = new PurchaseAddItemAdapter(this, fetchProduct, cartProducts);
                    recyclerViewPurchaseAddItem.setAdapter(purchaseAddItemAdapter);
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this,getResources().getString(R.string.volleyError),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updatePrice(HashMap<Long, FetchProduct> cartProducts) {
        this.cartProducts.putAll(cartProducts);
        Iterator iterator = this.cartProducts.entrySet().iterator();
        int itemCount = 0;
        Double total = 0.0;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            total = total + (Double.parseDouble(((FetchProduct) entry.getValue()).getmQuantity()) * Double.parseDouble(((FetchProduct) entry.getValue()).getmUnitPrice()));
            itemCount++;
        }
        tvPurchaseAddItemSelect.setText("Item : " + itemCount + " Total : " + total);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPurchaseAddItemCheckDone:
            case R.id.btnPurchaseAddItemDone:
                ArrayList<FetchProduct> fetchProducts = new ArrayList<>();
                Iterator iterator = cartProducts.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    fetchProducts.add((FetchProduct) entry.getValue());
                }
                PurchaseProductModel purchaseProductModel = new PurchaseProductModel(fetchProducts);
                Intent intent = new Intent();
                intent.putExtra("CartProducts", purchaseProductModel);
                intent.putExtra("HashCartProducts", cartProducts);
                intent.putParcelableArrayListExtra("FinalProducts", purchaseAddItemAdapter.getFinalProductList());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (purchaseAddItemAdapter != null)
            purchaseAddItemAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (purchaseAddItemAdapter != null)
            purchaseAddItemAdapter.setSetSelectedItem(position);
        etPurchaseAddItemSearch.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
