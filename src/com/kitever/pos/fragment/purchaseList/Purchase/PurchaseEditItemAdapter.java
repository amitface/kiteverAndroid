package com.kitever.pos.fragment.purchaseList.Purchase;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;

import java.util.ArrayList;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by android on 14/3/17.
 */

public class PurchaseEditItemAdapter extends RecyclerView.Adapter<PurchaseEditItemAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<FetchProduct> cartProducts;
    private ArrayList<FetchProduct> finalProducts;
    private MoonIcon moonIcon;

    public PurchaseEditItemAdapter(Context context, ArrayList<FetchProduct> cartProducts, ArrayList<FetchProduct> finalProducts) {
        this.context = context;
        this.cartProducts = cartProducts;
        this.moonIcon = new MoonIcon(context);
        this.finalProducts = finalProducts;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPurchaseEditItemAdapterPriceVal, tvPurchaseEditItemAdapterProductPriceIcon,
                tvPurchaseEditItemAdapterQty, tvPurchaseEditItemAdapterQtyMinus, tvPurchaseEditItemAdapterQtyPlus,
                tvPurchaseEditItemAdapterRemoveCart, tvPurchaseEditItemAdapterCategory, tvPurchaseEditItemAdapterBrand,
                tvPurchaseEditItemAdapterName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPurchaseEditItemAdapterRemoveCart = (TextView) itemView.findViewById(R.id.tvPurchaseEditItemAdapterRemoveCart);
            tvPurchaseEditItemAdapterProductPriceIcon = (TextView) itemView.findViewById(R.id.tvPurchaseEditItemAdapterProductPriceIcon);

            tvPurchaseEditItemAdapterCategory = (TextView) itemView.findViewById(R.id.tvPurchaseEditItemAdapterCategory);
            tvPurchaseEditItemAdapterBrand = (TextView) itemView.findViewById(R.id.tvPurchaseEditItemAdapterBrand);
            tvPurchaseEditItemAdapterName = (TextView) itemView.findViewById(R.id.tvPurchaseEditItemAdapterName);
            tvPurchaseEditItemAdapterPriceVal = (TextView) itemView.findViewById(R.id.tvPurchaseEditItemAdapterPriceVal);
            tvPurchaseEditItemAdapterQty = (TextView) itemView.findViewById(R.id.tvPurchaseEditItemAdapterQty);

            tvPurchaseEditItemAdapterQtyMinus = (TextView) itemView.findViewById(R.id.tvPurchaseEditItemAdapterQtyMinus);
            tvPurchaseEditItemAdapterQtyPlus = (TextView) itemView.findViewById(R.id.tvPurchaseEditItemAdapterQtyPlus);

            setRobotoThinFont(tvPurchaseEditItemAdapterCategory, context);
            setRobotoThinFont(tvPurchaseEditItemAdapterBrand, context);
            setRobotoThinFont(tvPurchaseEditItemAdapterName, context);
            setRobotoThinFont(tvPurchaseEditItemAdapterPriceVal, context);
            setRobotoThinFont(tvPurchaseEditItemAdapterQty, context);


            tvPurchaseEditItemAdapterCategory.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseEditItemAdapterBrand.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseEditItemAdapterName.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseEditItemAdapterPriceVal.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseEditItemAdapterQty.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

            moonIcon.setTextfont(tvPurchaseEditItemAdapterQtyPlus);
            moonIcon.setTextfont(tvPurchaseEditItemAdapterQtyMinus);
            moonIcon.setTextfont(tvPurchaseEditItemAdapterRemoveCart);
            moonIcon.setTextfont(tvPurchaseEditItemAdapterProductPriceIcon);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchase_edit_item_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final FetchProduct temp = cartProducts.get(position);
        holder.tvPurchaseEditItemAdapterName.setText(temp.getProductName());
        holder.tvPurchaseEditItemAdapterCategory.setText(temp.getCategoryName());
        holder.tvPurchaseEditItemAdapterBrand.setText(temp.getBrandName());
        holder.tvPurchaseEditItemAdapterQty.setText(temp.getmQuantity());

        holder.tvPurchaseEditItemAdapterPriceVal.setText(String.valueOf(Integer.parseInt(temp.getmQuantity()) * Double.parseDouble(temp.getmUnitPrice())));
        holder.tvPurchaseEditItemAdapterQtyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(temp.getmQuantity()) > 0) {


                    cartProducts.get(position).setmQuantity(String.valueOf(Integer.parseInt(temp.getmQuantity()) - 1));
                    for (int i = 0; i < finalProducts.size(); i++) {
                        if (cartProducts.get(position).getProductID() == finalProducts.get(i).getProductID()) {
                            finalProducts.get(i).setmQuantity(String.valueOf(Integer.parseInt(finalProducts.get(position).getmQuantity()) - 1));
                            break;
                        }
                    }
                    notifyItemChanged(position);
                }
            }
        });

        holder.tvPurchaseEditItemAdapterQtyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartProducts.get(position).setmQuantity(String.valueOf(Integer.parseInt(temp.getmQuantity()) + 1));
                for (int i = 0; i < finalProducts.size(); i++) {
                    if (cartProducts.get(position).getProductID() == finalProducts.get(i).getProductID()) {
                        finalProducts.get(position).setmQuantity(String.valueOf(Integer.parseInt(finalProducts.get(position).getmQuantity()) + 1));
                        break;
                    }
                }
                notifyItemChanged(position);
            }
        });

        holder.tvPurchaseEditItemAdapterRemoveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < finalProducts.size(); i++) {
                    if (cartProducts.get(position).getProductID() == finalProducts.get(i).getProductID()) {
                        finalProducts.get(position).setmQuantity("0");
                        break;
                    }
                }
                cartProducts.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    public ArrayList<FetchProduct> getFinalProducts() {
        return finalProducts;
    }

    public ArrayList<FetchProduct> getCart() {
        return cartProducts;
    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }
}
