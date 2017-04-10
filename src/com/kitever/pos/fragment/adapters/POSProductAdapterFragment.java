package com.kitever.pos.fragment.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CircleImageView;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.pos.activity.POSAddUpdateProduct;
import com.kitever.pos.fragment.PosProductScreenFragment;
import com.kitever.pos.model.data.ProductModelData;
import com.kitever.utils.TotalRows;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static sms19.inapp.msg.constant.Utils.imagePopup;

public class POSProductAdapterFragment extends BaseAdapter implements Filterable {
    private static final int ADD_PRODUCT = 1;
    private ArrayList<ProductModelData> modelList;
    private Context context;
    private PosProductScreenFragment contextFragment;
    private POSProductAdapterFragment.ItemFilter mItemFilter = new POSProductAdapterFragment.ItemFilter();
    private String selectedItemType;
    private ArrayList<ProductModelData> filteredArrayList = null;
    private String userId, password, userLogin;
    private PosProductScreenFragment productsScreenfragment;
    MoonIcon mIcon;
    private TotalRows rows;
    private Actionable actionable;
    String isActiveString;

    public POSProductAdapterFragment(PosProductScreenFragment productsScreen,
                                     ArrayList<ProductModelData> list, String userId,
                                     String password, String userLogin) {
        // TODO Auto-generated constructor stub
        this.userId = userId;
        this.password = password;
        this.userLogin = userLogin;
        this.filteredArrayList = list;
        this.modelList = list;
        this.actionable = (Actionable) productsScreen;
        this.productsScreenfragment = productsScreen;
        this.contextFragment = productsScreenfragment;
        mIcon = new MoonIcon(contextFragment);
        rows = (TotalRows) contextFragment;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        rows.totalRows(Integer.toString(filteredArrayList.size()));
        return filteredArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int id = position;
        POSProductAdapterFragment.ViewHolder holder;
        if (convertView == null) {
            holder = new POSProductAdapterFragment.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.pos_product_adapter_layout,
                    parent, false);
            holder.layout = (LinearLayout) convertView
                    .findViewById(R.id.layout);
            holder.productImage = (CircleImageView) convertView
                    .findViewById(R.id.product_image);
            holder.productName = (TextView) convertView
                    .findViewById(R.id.product_name);
            holder.categoryName = (TextView) convertView
                    .findViewById(R.id.product_category);
            holder.brandName = (TextView) convertView
                    .findViewById(R.id.product_brand);
            holder.priceName = (TextView) convertView
                    .findViewById(R.id.product_price);
            holder.price_icon = (TextView) convertView
                    .findViewById(R.id.product_price_icon);
            holder.active_inactive = (ImageView) convertView.findViewById(R.id.active_deactive);

            holder.tvQuantity = (TextView) convertView.findViewById(R.id.tvQuantity);
            holder.tvStock = (TextView) convertView.findViewById(R.id.tvStock);
            holder.tvStockAdd = (TextView) convertView.findViewById(R.id.tvStockAdd);
              mIcon.setTextfont(holder.tvStockAdd);

            setRobotoThinFont(holder.productName, contextFragment.getActivity());
            setRobotoThinFont(holder.categoryName, contextFragment.getActivity());
            setRobotoThinFont(holder.priceName, contextFragment.getActivity());
            setRobotoThinFont(holder.brandName, contextFragment.getActivity());
            setRobotoThinFont(holder.price_icon, contextFragment.getActivity());
            setRobotoThinFont(holder.tvStock, contextFragment.getActivity());
            setRobotoThinFont(holder.tvQuantity, contextFragment.getActivity());

            holder.productName.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.categoryName.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.priceName.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.brandName.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.price_icon.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.tvStock.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.tvQuantity.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.tvStockAdd.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));

            SharedPreferences prfs = contextFragment.getActivity().getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);
            String ccurrency = prfs.getString("Currency", "");
            holder.price_icon.setText(ccurrency);

            convertView.setTag(holder);
        } else {

            holder = (POSProductAdapterFragment.ViewHolder) convertView.getTag();
        }

        final ProductModelData object = filteredArrayList.get(position);


        final SpannableStringBuilder sb = new SpannableStringBuilder(holder.tvStockAdd.getText().toString());

//            // Span to set text color to some RGB value
//            final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(158, 158, 158));

        // Span to make text bold
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        final StyleSpan bab = new StyleSpan(CustomStyle.robotothin.getStyle());
        sb.setSpan(new RelativeSizeSpan(0.9f), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(bab, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the text color for first 4 characters
//            sb.setSpan(fcs, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        // make them also bold
        sb.setSpan(bss, 3, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        holder.tvStockAdd.setText(sb);

        String imagestr = object.getProductImage();
        holder.tvQuantity.setText(object.getAvailableStock());
        if (imagestr != null && imagestr.length() > 0) {
            String image_url = "http://nowconnect.in/" + userId + "/" + object.getProductImage();
            Log.i("image_url", image_url);
            Picasso.with(contextFragment.getActivity())
                    .load(image_url)
                    .placeholder(R.drawable.product_box)
                    .error(R.drawable.product_box)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.product_box);
        }

        final Drawable d = holder.productImage.getDrawable();

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePopup(contextFragment.getActivity(), d);
            }
        });

        if (object.getIsActive().equals("A")) {
            //holder.layout.setBackgroundResource(R.drawable.border_black);
            holder.layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_ENABLED));
            holder.active_inactive.setImageResource(R.drawable.switch_on);
            holder.layout.setClickable(true);
        } else {
            //holder.layout.setBackgroundResource(R.drawable.border_grey);
            holder.layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_DISABLED));
            holder.active_inactive.setImageResource(R.drawable.switch_off);
            holder.layout.setClickable(false);
        }
        holder.productName.setText(object.getProductName());
        //String[] catArray=object.getCategoryName().split("<");
        holder.categoryName.setText(object.getCategoryName());
        holder.brandName.setText(object.getBrandName());
        holder.priceName.setText(object.getPerUnitPrice());

        holder.active_inactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productStatus = object.getIsActive();
                String isActive = object.getIsActive().equals("A") ? "I" : "A";
                isActiveString = object.getIsActive().equals("A") ? "Deactivated" : "Activated";
                // Toast.makeText(contextFragment.getActivity(), "Product " + isActiveString + ", Toast.LENGTH_SHORT).show();
                //object.setIsActive(isActive);
                Map map = new HashMap<>();
                map.put("Page", "SetStatus");
                map.put("id", object.getProductID());
                map.put("Status", productStatus);
                map.put("PageType", "Product");
                map.put("UserID", userId);
                map.put("UserLogin", userLogin);
                map.put("Password", password);
                productsScreenfragment.activateProduct(map, id);

            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (object.getIsActive().equals("A")) {
                    Intent intent = new Intent(contextFragment.getActivity(), POSAddUpdateProduct.class);
                    intent.putExtra("screen_name", "Update Product");
                    intent.putExtra("product_name", object.getProductName());
                    intent.putExtra("product_id", object.getProductID());
                    intent.putExtra("product_img", object.getProductImage());
                    intent.putExtra("product_category", object.getCategoryName());
                    intent.putExtra("product_brand", object.getBrandName());
                    intent.putExtra("product_units", object.getUnits());
                    intent.putExtra("product_price", object.getPerUnitPrice());
                    intent.putExtra("price_withtax", object.getPriceWithTax());
                    intent.putExtra("product_description", object.getDescription());
                    intent.putExtra("taxApplied", object.getTaxApplied());
                    intent.putExtra("is_active", object.getIsActive());
                    contextFragment.startActivityForResult(intent, ADD_PRODUCT);
                }
            }
        });

        holder.tvStockAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionable.update(position, filteredArrayList.get(position));
            }
        });

        return convertView;
    }

    public void toastmsg() {
        Toast.makeText(contextFragment.getActivity(), "Product " + isActiveString, Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ProductModelData> getfilteredArrayList()
    {
        return filteredArrayList;
    }

    public void updateList(int position, String balance) {
        filteredArrayList.get(position).setAvailableStock(balance);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        CircleImageView productImage;
        //ImageView  editProduct,deleteProduct;
        TextView productName, categoryName, brandName, priceName, price_icon, tvQuantity, tvStock,
                tvStockAdd;
        LinearLayout layout;
        ImageView active_inactive;
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return mItemFilter;
    }


    public void setSelectedItemType(String itemType) {
        selectedItemType = itemType;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            List<ProductModelData> list = modelList;

              /*  if(selectedItemType==null ||filterString.length()==0 || selectedItemType.equals(""))
                {
                    results.values = list;
                    results.count = list.size();
                    return results;
                }*/

            int count = list.size();
            final ArrayList<ProductModelData> nlist = new ArrayList<ProductModelData>(
                    count);

            String filterableString = "";

            for (int i = 0; i < count; i++) {

                if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Product")) {
                    filterableString = list.get(i).getProductName();
                    if (filterableString.toLowerCase().contains(filterString))
                        nlist.add(list.get(i));
                } else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Category")) {
                    filterableString = list.get(i).getCategoryName();
                    if (filterableString.toLowerCase().contains(filterString))
                        nlist.add(list.get(i));
                } else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Brand")) {
                    filterableString = list.get(i).getBrandName();
                    if (filterableString.toLowerCase().contains(filterString))
                        nlist.add(list.get(i));
                } else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Price >=")) {
                    String pricestr = list.get(i).getPerUnitPrice();
                    try {
                        Double filterableInt = Double.parseDouble(pricestr.trim());
                        Double filterInt = Double.parseDouble(filterString.trim());
                        if (filterableInt >= filterInt) {
                            nlist.add(list.get(i));
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Price <=")) {

                    String pricestr = list.get(i).getPerUnitPrice();
                    try {
                        Double filterableInt = Double.parseDouble(pricestr.trim());
                        Double filterInt = Double.parseDouble(filterString.trim());
                        Log.i("filterableInt", "AInt -" + filterableInt + " BInt -" + filterInt);
                        if (filterableInt <= filterInt) {
                            // filterableString = list.get(i).getPerUnitPrice();
                            nlist.add(list.get(i));
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (selectedItemType == null || filterString.length() == 0 || selectedItemType.equals("")) {
                    filterableString = list.get(i).getProductName();
                    if (filterableString.toLowerCase().contains(filterString))
                        nlist.add(list.get(i));
                } else if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            filteredArrayList = (ArrayList<ProductModelData>) results.values;
            if (filteredArrayList != null)
                notifyDataSetChanged();
        }
    }

    public void setModelData(ArrayList<ProductModelData> modelList) {
        if (modelList == null) {
            modelList = new ArrayList<ProductModelData>();
        }
        filteredArrayList = modelList;
    }

    public interface Actionable {
        void deleteProduct(Map map);

        void activateProduct(Map map, int position);

        void update(int position, ProductModelData productModelData);
        //void CheckNoResult(int size);
    }

    public void changeList(ArrayList<ProductModelData> product) {
        // TODO Auto-generated method stub
        filteredArrayList = product;
        notifyDataSetChanged();
    }
}
