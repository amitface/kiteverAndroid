package sms19.listview.newproject.MerchatStorePackage.Fragments.AddOrderAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;

import java.util.List;

import sms19.listview.newproject.MerchatStorePackage.Model.ModelBuyCart.Product;
import sms19.listview.newproject.MerchatStorePackage.UserCartActivity;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by android on 4/4/17.
 */

public class CartBuyAdapter extends RecyclerView.Adapter<CartBuyAdapter.MyViewHolder> {

    private UserCartActivity userCartActivity;
    private Context context;
    private List<Product> products;
    private InsertToCart insertItem;
    private MoonIcon moonIcon;
    private String arr[];

    public CartBuyAdapter(UserCartActivity userCartActivity, List<Product> products, String arr[]) {
        this.userCartActivity = userCartActivity;
        this.products = products;
        context = userCartActivity;
        moonIcon = new MoonIcon(userCartActivity);
        insertItem = (InsertToCart) userCartActivity;
        this.arr = arr;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCartBuyAdapterPriceVal, tvCartBuyAdapterProductPriceIcon,
                tvCartBuyAdapterQtyMinus, tvCartBuyAdapterQtyPlus,
                tvCartBuyAdapterRemoveCart, tvCartBuyAdapterCategory, tvCartBuyAdapterBrand,
                tvCartBuyAdapterName, tvCartBuyAdapterQtyStatus;
        EditText tvCartBuyAdapterQty;
        LinearLayout layoutCartBuy;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvCartBuyAdapterRemoveCart = (TextView) itemView.findViewById(R.id.tvCartBuyAdapterRemoveCart);
            tvCartBuyAdapterProductPriceIcon = (TextView) itemView.findViewById(R.id.tvCartBuyAdapterProductPriceIcon);

            tvCartBuyAdapterCategory = (TextView) itemView.findViewById(R.id.tvCartBuyAdapterCategory);
            tvCartBuyAdapterBrand = (TextView) itemView.findViewById(R.id.tvCartBuyAdapterBrand);
            tvCartBuyAdapterName = (TextView) itemView.findViewById(R.id.tvCartBuyAdapterName);
            tvCartBuyAdapterPriceVal = (TextView) itemView.findViewById(R.id.tvCartBuyAdapterPriceVal);
            tvCartBuyAdapterQtyStatus = (TextView) itemView.findViewById(R.id.tvCartBuyAdapterQtyStatus);
            tvCartBuyAdapterQty = (EditText) itemView.findViewById(R.id.tvCartBuyAdapterQty);
            layoutCartBuy = (LinearLayout) itemView.findViewById(R.id.layoutCartBuy);

            tvCartBuyAdapterQtyMinus = (TextView) itemView.findViewById(R.id.tvCartBuyAdapterQtyMinus);
            tvCartBuyAdapterQtyPlus = (TextView) itemView.findViewById(R.id.tvCartBuyAdapterQtyPlus);

            setRobotoThinFont(tvCartBuyAdapterCategory, context);
            setRobotoThinFont(tvCartBuyAdapterBrand, context);
            setRobotoThinFont(tvCartBuyAdapterName, context);
            setRobotoThinFont(tvCartBuyAdapterPriceVal, context);
            setRobotoThinFont(tvCartBuyAdapterQty, context);
            setRobotoThinFont(tvCartBuyAdapterQtyStatus, context);

            tvCartBuyAdapterCategory.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvCartBuyAdapterBrand.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvCartBuyAdapterName.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvCartBuyAdapterPriceVal.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvCartBuyAdapterQty.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvCartBuyAdapterQtyStatus.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

            moonIcon.setTextfont(tvCartBuyAdapterQtyPlus);
            moonIcon.setTextfont(tvCartBuyAdapterQtyMinus);
            moonIcon.setTextfont(tvCartBuyAdapterRemoveCart);
            moonIcon.setTextfont(tvCartBuyAdapterProductPriceIcon);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_buy_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Product temp = products.get(position);
        holder.tvCartBuyAdapterName.setText(temp.getProductName());
        holder.tvCartBuyAdapterCategory.setText(temp.getCategoryName());
        holder.tvCartBuyAdapterBrand.setText(temp.getBrandName());
        holder.tvCartBuyAdapterPriceVal.setText(String.valueOf(temp.getQuantity() * temp.getPerUnitPrice()));
        holder.tvCartBuyAdapterQtyStatus.setText(String.valueOf(temp.getQuantity()));
        holder.tvCartBuyAdapterQty.setText("");

        holder.tvCartBuyAdapterQtyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = holder.tvCartBuyAdapterQty.getText().toString();
                if (s.length() != 0) {
                    if (temp.getAvailableStock()-Long.parseLong(s)  < 0)
                    {
                        Toast.makeText(context, temp.getAvailableStock() + " items avaliable in stock.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else
                    {
                        products.get(position).setQuantity(products.get(position).getQuantity() + Long.parseLong(s));
                        products.get(position).setAvailableStock(products.get(position).getAvailableStock()-Long.parseLong(s));
                    }

                } else
                    return;
                notifyItemChanged(position);
            }
        });

        holder.tvCartBuyAdapterQtyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = holder.tvCartBuyAdapterQty.getText().toString();
                if (s.length() != 0)
                {
                    if (Long.parseLong(s) - temp.getQuantity() > 0)
                    {
                        Toast.makeText(context,"Only "+ temp.getQuantity() + " items in Cart.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        products.get(position).setQuantity(products.get(position).getQuantity() - Long.parseLong(s));
                        products.get(position).setAvailableStock(products.get(position).getAvailableStock() + Long.parseLong(s));
                    }
                }
                else
                    return;
                notifyItemChanged(position);
            }
        });

        holder.tvCartBuyAdapterRemoveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItem.insertCartItem(temp.getmProductID(), (int) (-1 * temp.getQuantity()));
            }
        });

        holder.layoutCartBuy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setSelected(true);
                return false;
            }
        });

        holder.tvCartBuyAdapterQty.setTag(position);

        holder.tvCartBuyAdapterQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && v.isActivated()) {
                    String s = ((EditText) v.findViewWithTag(position)).getText().toString();
                    if (s.length() != 0)
                        products.get(position).setQuantity(Long.parseLong(s));
                    else
                        products.get(position).setQuantity(0L);
                    notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public List<Product> getList() {
        return products;
    }

    public void setList(Product list) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getmProductID().equalsIgnoreCase(list.getmProductID())) {
                if (list.getQuantity() == 0) {
                    {
                        products.remove(i);
                        notifyDataSetChanged();
                    }
                } else {
                    products.get(i).setAvailableStock(list.getAvailableStock());
                    products.get(i).setQuantity(list.getQuantity());
                    notifyItemChanged(i);
                }
            }
        }
    }

    public class TextChange implements TextWatcher {

        int position;

        public TextChange(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
