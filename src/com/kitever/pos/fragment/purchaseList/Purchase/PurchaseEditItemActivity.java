package com.kitever.pos.fragment.purchaseList.Purchase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;

import java.util.ArrayList;

public class PurchaseEditItemActivity extends AppCompatActivity implements View.OnClickListener {

    private final int KEY_ADD_MORE_ITEM = 1;
    private MoonIcon micon;
    private TextView tvEditCartGoCart, tvEditCartCheckDone, tvEditCartAddItems,
            tvEditCartUpdate, tvEditCartTextSelect;
    private LinearLayout tvEditCartSelectCart;
    private RecyclerView recyclerViewEditCart;
    private PurchaseEditItemAdapter purchaseEditItemAdapter;
    private ArrayList<FetchProduct> cartProducts;
    private ArrayList<FetchProduct> finalProducts;
    private PurchaseInsertModel purchaseInsertModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_edit_item);
        getSupportActionBar().hide();
        purchaseInsertModel = getIntent().getParcelableExtra("PurchaseInsert");
        cartProducts = getIntent().getParcelableArrayListExtra("CartProducts");
        finalProducts = getIntent().getParcelableArrayListExtra("FinalProducts");
        setUI();
    }

    private void setUI() {
        micon = new MoonIcon(this);
        tvEditCartGoCart = (TextView) findViewById(R.id.tvEditCartGoCart);
        tvEditCartSelectCart = (LinearLayout) findViewById(R.id.tvEditCartSelectCart);
        tvEditCartCheckDone = (TextView) findViewById(R.id.tvEditCartCheckDone);
        recyclerViewEditCart = (RecyclerView) findViewById(R.id.recyclerViewEditCart);

        tvEditCartAddItems = (TextView) findViewById(R.id.tvEditCartAddItems);
        tvEditCartUpdate = (TextView) findViewById(R.id.tvEditCartUpdate);
        tvEditCartTextSelect = (TextView) findViewById(R.id.tvEditCartUpdate);

        tvEditCartGoCart.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        tvEditCartCheckDone.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        tvEditCartUpdate.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        tvEditCartTextSelect.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));


        tvEditCartCheckDone.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        tvEditCartSelectCart.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        tvEditCartAddItems.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        tvEditCartUpdate.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        micon.setTextfont(tvEditCartGoCart);
        micon.setTextfont(tvEditCartCheckDone);

        purchaseEditItemAdapter = new PurchaseEditItemAdapter(this, cartProducts, finalProducts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewEditCart.setLayoutManager(layoutManager);
        recyclerViewEditCart.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEditCart.setAdapter(purchaseEditItemAdapter);
        tvEditCartUpdate.setOnClickListener(this);
        tvEditCartAddItems.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==Activity.RESULT_OK)
        {
            if(requestCode==KEY_ADD_MORE_ITEM)
            {
                cartProducts.clear();
                finalProducts.clear();
                cartProducts.addAll(((PurchaseProductModel) data.getParcelableExtra("CartProducts")).getFetchProduct());
                ArrayList<FetchProduct> tempProduct = data.getParcelableArrayListExtra("FinalProducts");
                finalProducts.addAll(tempProduct);

                purchaseEditItemAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        PurchaseProductModel purchaseProductModel;
        switch (v.getId()) {
            case R.id.tvEditCartUpdate:
                intent = new Intent();
                purchaseProductModel = new PurchaseProductModel(purchaseEditItemAdapter.getCart());
                intent.putExtra("CartProducts", purchaseProductModel);
                intent.putParcelableArrayListExtra("FinalProducts", purchaseEditItemAdapter.getFinalProducts());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.tvEditCartAddItems:
                intent = new Intent(this,PurchaseAddItemActivity.class);
                purchaseProductModel = new PurchaseProductModel(purchaseEditItemAdapter.getCart());
                intent.putExtra("PurchaseInsert", purchaseInsertModel);
                intent.putExtra("CartProducts", purchaseProductModel);
                intent.putParcelableArrayListExtra("FinalProducts", purchaseEditItemAdapter.getFinalProducts());
                startActivityForResult(intent,KEY_ADD_MORE_ITEM);
                break;
        }
    }
}
