package com.kitever.pos.fragment.purchaseList.Purchase;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by android on 7/3/17.
 */

public class PurchaseAddItemAdapter extends RecyclerView.Adapter<PurchaseAddItemAdapter.MyViewHolder> implements Filterable{

    private Context context;
    private ArrayList<FetchProduct> fetchProducts;
    private ArrayList<FetchProduct> finalfetchProducts;
    private HashMap<Long, FetchProduct> cartProducts;
    private MoonIcon mIcon;
    private Actionable actionable;
    private int size = 0;
    private ItemFilter itemFilter;
    private int setSelectedItem = 0;

    public PurchaseAddItemAdapter(Context context, ArrayList<FetchProduct> fetchProducts, HashMap<Long, FetchProduct> cartProducts) {
        this.context = context;
        this.fetchProducts = fetchProducts;
        this.finalfetchProducts = fetchProducts;
        this.cartProducts = cartProducts;
        this.mIcon = new MoonIcon(context);
        actionable = (Actionable) context;
        size = finalfetchProducts.size();
        itemFilter = new ItemFilter();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewPurchaseItemAdapterProduct;
        public TextView tvPurchaseItemAdapterName, tvPurchaseItemAdapterCategory, tvPurchaseItemAdapterBrand,
                tvPurchaseItemAdapterAddToCart;
        public EditText tvPurchaseItemAdapterQty, etPurchaseItemAdapterPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPurchaseItemAdapterName = (TextView) itemView.findViewById(R.id.tvPurchaseItemAdapterName);
            tvPurchaseItemAdapterCategory = (TextView) itemView.findViewById(R.id.tvPurchaseItemAdapterCategory);
            tvPurchaseItemAdapterBrand = (TextView) itemView.findViewById(R.id.tvPurchaseItemAdapterBrand);
            tvPurchaseItemAdapterAddToCart = (TextView) itemView.findViewById(R.id.tvPurchaseItemAdapterAddToCart);
            tvPurchaseItemAdapterQty = (EditText) itemView.findViewById(R.id.tvPurchaseItemAdapterQty);
            etPurchaseItemAdapterPrice = (EditText) itemView.findViewById(R.id.etPurchaseItemAdapterPrice);

            setRobotoThinFont(tvPurchaseItemAdapterName,context);
            setRobotoThinFont(tvPurchaseItemAdapterCategory,context);
            setRobotoThinFont(tvPurchaseItemAdapterBrand,context);
            setRobotoThinFont(tvPurchaseItemAdapterAddToCart,context);
            setRobotoThinFont(tvPurchaseItemAdapterQty,context);
            setRobotoThinFont(etPurchaseItemAdapterPrice,context);


            tvPurchaseItemAdapterName.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseItemAdapterCategory.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseItemAdapterBrand.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseItemAdapterAddToCart.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseItemAdapterQty.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            etPurchaseItemAdapterPrice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.purchase_add_item_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        size = finalfetchProducts.size();
        final FetchProduct temp = fetchProducts.get(position);
        holder.tvPurchaseItemAdapterName.setText(temp.getProductName());
        holder.tvPurchaseItemAdapterCategory.setText(temp.getCategoryName());
        holder.tvPurchaseItemAdapterBrand.setText(temp.getBrandName());
        mIcon.setTextfont(holder.tvPurchaseItemAdapterAddToCart);
        holder.etPurchaseItemAdapterPrice.setEnabled(true);
        holder.etPurchaseItemAdapterPrice.setText("");

        if (fetchProducts.get(position).getmUnitPrice() != null && Double.parseDouble(fetchProducts.get(position).getmUnitPrice()) > 0) {
            holder.etPurchaseItemAdapterPrice.setText(fetchProducts.get(position).getmUnitPrice());
            holder.etPurchaseItemAdapterPrice.setEnabled(false);
        }

        holder.tvPurchaseItemAdapterAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tvPurchaseItemAdapterQty.getText().equals("0") || holder.tvPurchaseItemAdapterQty.getText().toString().trim().length() == 0) {
                    Toast.makeText(context, "Enter correct quantity", Toast.LENGTH_SHORT).show();
                    return;
                }
                int lQty = (int)Double.parseDouble(holder.tvPurchaseItemAdapterQty.getText().toString());
                if (fetchProducts.get(position).getmUnitPrice() != null && Double.parseDouble(fetchProducts.get(position).getmUnitPrice()) >= 0) {
                    lQty = lQty +(int) Double.parseDouble(fetchProducts.get(position).getmQuantity());
                } else {
                    if (holder.etPurchaseItemAdapterPrice.getText().toString().trim().length()==0) {
                        Toast.makeText(context, "Enter correct quantity", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Double l = Double.parseDouble(holder.etPurchaseItemAdapterPrice.getText().toString());
                    fetchProducts.get(position).setmUnitPrice(String.valueOf(l));

                    for(int i = 0; i<finalfetchProducts.size(); i++)
                    {
                        if(fetchProducts.get(position).getProductID()==finalfetchProducts.get(i).getProductID())
                        {
                            finalfetchProducts.get(i).setmUnitPrice(String.valueOf(l));
                            break;
                        }
                    }
                }

                for(int i = 0; i<finalfetchProducts.size(); i++)
                {
                    if(fetchProducts.get(position).getProductID()==finalfetchProducts.get(i).getProductID())
                    {
                        fetchProducts.get(position).setmQuantity(String.valueOf(lQty));
                        finalfetchProducts.get(i).setmQuantity(String.valueOf(lQty));
                        break;
                    }
                }

                cartProducts.put(fetchProducts.get(position).getProductID(), fetchProducts.get(position));
                holder.tvPurchaseItemAdapterQty.setText("");
                actionable.updatePrice(cartProducts);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fetchProducts.size();
    }

    public ArrayList<FetchProduct> getFinalProductList()
    {
        return finalfetchProducts;
    }

    public void setSetSelectedItem(int setSelectedItem) {
        this.setSelectedItem = setSelectedItem;
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    public class ItemFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterable = constraint.toString().toLowerCase();

            if(filterable.length()==0)
            {
                results.values = finalfetchProducts;
                results.count = finalfetchProducts.size();
                return results;
            }

            List<FetchProduct> searchProducts = new ArrayList<>(finalfetchProducts.size());
            int searchSize = finalfetchProducts.size();

            for(int i =0; i<searchSize; i++)
            {
                if(setSelectedItem==0)
                {
                    if(finalfetchProducts.get(i).getProductName().toLowerCase().contains(filterable))
                        searchProducts.add(finalfetchProducts.get(i));
                } else if(setSelectedItem==1)
                {
                    if(finalfetchProducts.get(i).getCategoryName().toLowerCase().contains(filterable))
                        searchProducts.add(finalfetchProducts.get(i));
                } else if( setSelectedItem==2)
                {
                    if(finalfetchProducts.get(i).getBrandName().toLowerCase().contains(filterable))
                        searchProducts.add(finalfetchProducts.get(i));
                }
            }

            results.count = searchProducts.size();
            results.values = searchProducts;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            fetchProducts = (ArrayList<FetchProduct>) results.values;
            if(fetchProducts!=null)
                notifyDataSetChanged();
        }
    }



    public interface Actionable {
        void updatePrice(HashMap<Long, FetchProduct> cartProducts);
    }
}
