package sms19.listview.newproject.MerchatStorePackage.Fragments.AddOrderAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sms19.listview.newproject.MerchatStorePackage.Model.Product;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by android on 3/4/17.
 */

public class BrandProductListAdapter extends RecyclerView.Adapter<BrandProductListAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Product> fetchProducts;
    private List<Product> finalfetchProducts;
    private HashMap<Long, Product> cartProducts;
    private MoonIcon mIcon;
    private Actionable actionable;
    private int size = 0;
    private ItemFilter itemFilter;
    private int setSelectedItem = 0;
    private InsertToCart insertToCart;

    public BrandProductListAdapter(Fragment context, List<Product> fetchProducts, HashMap<Long, Product> cartProducts) {

        this.context = context.getContext();
        this.fetchProducts = fetchProducts;
        this.finalfetchProducts = fetchProducts;
        this.cartProducts = cartProducts;
        this.mIcon = new MoonIcon(context);
        actionable = (Actionable) context;
        size = finalfetchProducts.size();
        itemFilter = new ItemFilter();
        insertToCart = (InsertToCart) context.getActivity();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewUserProductAdapterProduct;
        public TextView tvUserProductAdapterName, tvUserProductAdapterCategory, tvUserProductAdapterBrand,
                tvUserProductAdapterAddToCart, etUserProductAdapterPrice, tvUserProductAdapterAddNoItem;
        public EditText tvUserProductAdapterQty;
        public LinearLayout layoutSingleItemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvUserProductAdapterName = (TextView) itemView.findViewById(R.id.tvUserProductAdapterName);
            tvUserProductAdapterCategory = (TextView) itemView.findViewById(R.id.tvUserProductAdapterCategory);
            tvUserProductAdapterBrand = (TextView) itemView.findViewById(R.id.tvUserProductAdapterBrand);
            tvUserProductAdapterAddToCart = (TextView) itemView.findViewById(R.id.tvUserProductAdapterAddToCart);
            tvUserProductAdapterQty = (EditText) itemView.findViewById(R.id.tvUserProductAdapterQty);
            etUserProductAdapterPrice = (TextView) itemView.findViewById(R.id.etUserProductAdapterPrice);
            tvUserProductAdapterAddNoItem = (TextView) itemView.findViewById(R.id.tvUserProductAdapterAddNoItem);
            layoutSingleItemLayout = (LinearLayout) itemView.findViewById(R.id.layoutSingleItemLayout);

            setRobotoThinFont(tvUserProductAdapterName, context);
            setRobotoThinFont(tvUserProductAdapterCategory, context);
            setRobotoThinFont(tvUserProductAdapterBrand, context);
            setRobotoThinFont(tvUserProductAdapterAddToCart, context);
            setRobotoThinFont(tvUserProductAdapterQty, context);
            setRobotoThinFont(etUserProductAdapterPrice, context);
            setRobotoThinFont(tvUserProductAdapterAddNoItem, context);

            tvUserProductAdapterName.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvUserProductAdapterCategory.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvUserProductAdapterBrand.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvUserProductAdapterAddToCart.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvUserProductAdapterQty.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            etUserProductAdapterPrice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_product_list_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        size = finalfetchProducts.size();
        final Product temp = fetchProducts.get(position);
        holder.tvUserProductAdapterName.setText(temp.getProductName());
        holder.tvUserProductAdapterCategory.setText(temp.getCategoryName());
        holder.tvUserProductAdapterBrand.setText(temp.getBrandName());

        holder.tvUserProductAdapterAddToCart.setVisibility(View.VISIBLE);
        holder.tvUserProductAdapterQty.setVisibility(View.VISIBLE);
        holder.tvUserProductAdapterAddNoItem.setVisibility(View.GONE);
        if (temp.getAvailableStock() > 0) {
            mIcon.setTextfont(holder.tvUserProductAdapterAddToCart);
        } else {
            holder.tvUserProductAdapterAddToCart.setVisibility(View.GONE);
            holder.tvUserProductAdapterQty.setVisibility(View.GONE);
            holder.tvUserProductAdapterAddNoItem.setVisibility(View.VISIBLE);
        }


        if (fetchProducts.get(position).getPerUnitPrice() != null && Double.parseDouble(String.valueOf(fetchProducts.get(position).getPerUnitPrice())) > 0) {
            holder.etUserProductAdapterPrice.setText(String.valueOf(fetchProducts.get(position).getPerUnitPrice()));
            holder.etUserProductAdapterPrice.setEnabled(false);
        }

        holder.tvUserProductAdapterAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.tvUserProductAdapterQty.getText() != null && Integer.parseInt(holder.tvUserProductAdapterQty.getText().toString())>temp.getAvailableStock())
                    Toast.makeText(context.getApplicationContext(), "Quantity should be less than Available Stock.", Toast.LENGTH_SHORT).show();
                else if (holder.tvUserProductAdapterQty.getText() != null && holder.tvUserProductAdapterQty.getText().length() != 0 && Integer.parseInt(holder.tvUserProductAdapterQty.getText().toString()) > 0)
                    insertToCart.insertItem(temp,Integer.parseInt(holder.tvUserProductAdapterQty.getText().toString()));
                else
                    Toast.makeText(context.getApplicationContext(), "Quantity should be greater than zero", Toast.LENGTH_SHORT).show();
                holder.tvUserProductAdapterQty.setText("");

            }
        });

        holder.layoutSingleItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionable.onitemClick(temp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fetchProducts.size();
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    public class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterable = constraint.toString().toLowerCase();

            if (filterable.length() == 0) {
                results.values = finalfetchProducts;
                results.count = finalfetchProducts.size();
                return results;
            }

            List<Product> searchProducts = new ArrayList<>(finalfetchProducts.size());
            int searchSize = finalfetchProducts.size();

            for (int i = 0; i < searchSize; i++) {
                if (setSelectedItem == 0) {
                    if (finalfetchProducts.get(i).getProductName().toLowerCase().contains(filterable))
                        searchProducts.add(finalfetchProducts.get(i));
                } else if (setSelectedItem == 1) {
                    if (finalfetchProducts.get(i).getCategoryName().toLowerCase().contains(filterable))
                        searchProducts.add(finalfetchProducts.get(i));
                } else if (setSelectedItem == 2) {
                    if (finalfetchProducts.get(i).getBrandName().toLowerCase().contains(filterable))
                        searchProducts.add(finalfetchProducts.get(i));
                }
            }

            results.count = searchProducts.size();
            results.values = searchProducts;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            fetchProducts = (List<Product>) results.values;
            if (fetchProducts != null)
                notifyDataSetChanged();
        }
    }

    public interface Actionable {
        void updatePrice(HashMap<Long, Product> cartProducts);

        void onitemClick(Product product);
    }
}
