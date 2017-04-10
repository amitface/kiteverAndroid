package com.kitever.pos.fragment.purchaseList.Vendor;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import java.util.ArrayList;
import java.util.List;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by android on 10/3/17.
 */

public class VendorDetailAdapter extends RecyclerView.Adapter<VendorDetailAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private ArrayList<GetVendorDetail> getVendorDetails;
    private ArrayList<GetVendorDetail> finalVendorDetails;
    private Actionable actionable;
    private int selectedItemPosition = -1;
    private ItemFilter itemFilter;

    public VendorDetailAdapter(Context context, ArrayList<GetVendorDetail> getVendorDetails, VendorDetailFragment vendorDetailFragment) {
        this.context = context;
        this.getVendorDetails = getVendorDetails;
        this.actionable = (Actionable) vendorDetailFragment;
        finalVendorDetails = getVendorDetails;
        itemFilter = new ItemFilter();
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvVendorDetailDate, tvVendorDetailMonth, tvVendorDetailInvoice,
                tvVendorDetailName, tvVendorDetailPurchase, tvVendorDetailBilled,
                tvVendorDetailPaid, tvVendorDetailBalance;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvVendorDetailName = (TextView) itemView.findViewById(R.id.tvVendorDetailName);
            tvVendorDetailPurchase = (TextView) itemView.findViewById(R.id.tvVendorDetailPurchase);
            tvVendorDetailBilled = (TextView) itemView.findViewById(R.id.tvVendorDetailBilled);
            tvVendorDetailPaid = (TextView) itemView.findViewById(R.id.tvVendorDetailPaid);
            tvVendorDetailBalance = (TextView) itemView.findViewById(R.id.tvVendorDetailBalance);

            tvVendorDetailName.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvVendorDetailPurchase.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvVendorDetailBilled.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvVendorDetailPaid.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvVendorDetailBalance.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

            setRobotoThinFont(tvVendorDetailName,context);
            setRobotoThinFont(tvVendorDetailPurchase,context);
            setRobotoThinFont(tvVendorDetailBilled,context);
            setRobotoThinFont(tvVendorDetailPaid,context);
            setRobotoThinFont(tvVendorDetailBalance,context);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_detail_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GetVendorDetail temp = getVendorDetails.get(position);
        holder.tvVendorDetailName.setText(temp.getContactName());
        holder.tvVendorDetailPurchase.setText(String.valueOf(temp.getTotalPurchases()));
        holder.tvVendorDetailBilled.setText(String.valueOf(temp.getTotalAmount()));
        holder.tvVendorDetailPaid.setText(String.valueOf(temp.getTotalPayment()));
        holder.tvVendorDetailBalance.setText(String.valueOf(temp.getTotalCredit()));
    }

    public void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
    }

    public ArrayList<GetVendorDetail> getVendorDetail()
    {
        return getVendorDetails;
    }

    @Override
    public int getItemCount() {
        actionable.updateRecordCount(getVendorDetails.size());
        return getVendorDetails.size();
    }

    private boolean greaterRange(Double value, Double orignal) {
        return orignal > value;
    }

    private boolean lesserRange(Double value, Double orignal) {
        return orignal < value;
    }

    public class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterable = constraint.toString().toLowerCase();
            if (filterable.length() == 0) {
                results.values = finalVendorDetails;
                results.count = finalVendorDetails.size();
                return results;
            }

            int count = finalVendorDetails.size();
            List<GetVendorDetail> searchList = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                if (selectedItemPosition == 1) {
                    if (finalVendorDetails.get(i).getContactName().toLowerCase().contains(filterable))
                        searchList.add(finalVendorDetails.get(i));
                } else if (selectedItemPosition == 2) {
                    if (lesserRange(Double.parseDouble(finalVendorDetails.get(i).getTotalPurchases().toString()), Double.parseDouble(filterable))) {
                        searchList.add(finalVendorDetails.get(i));
                    }
                } else if (selectedItemPosition == 3) {
                    if (greaterRange(Double.parseDouble(finalVendorDetails.get(i).getTotalPurchases().toString()), Double.parseDouble(filterable))) {
                        searchList.add(finalVendorDetails.get(i));
                    }
                } else if (selectedItemPosition == 4) {
                    if (lesserRange(Double.parseDouble(finalVendorDetails.get(i).getTotalAmount().toString()), Double.parseDouble(filterable))) {
                        searchList.add(finalVendorDetails.get(i));
                    }
                } else if (selectedItemPosition == 5) {
                    if (greaterRange(Double.parseDouble(finalVendorDetails.get(i).getTotalAmount().toString()), Double.parseDouble(filterable))) {
                        searchList.add(finalVendorDetails.get(i));
                    }
                } else if (selectedItemPosition == 6) {
                    if (lesserRange(Double.parseDouble(finalVendorDetails.get(i).getTotalPayment().toString()), Double.parseDouble(filterable))) {
                        searchList.add(finalVendorDetails.get(i));
                    }
                } else if (selectedItemPosition == 7) {
                    if (greaterRange(Double.parseDouble(finalVendorDetails.get(i).getTotalPayment().toString()), Double.parseDouble(filterable))) {
                        searchList.add(finalVendorDetails.get(i));
                    }
                } else if (selectedItemPosition == 6) {
                    if (lesserRange(Double.parseDouble(finalVendorDetails.get(i).getTotalCredit().toString()), Double.parseDouble(filterable))) {
                        searchList.add(finalVendorDetails.get(i));
                    }
                } else if (selectedItemPosition == 7) {
                    if (greaterRange(Double.parseDouble(finalVendorDetails.get(i).getTotalCredit().toString()), Double.parseDouble(filterable))) {
                        searchList.add(finalVendorDetails.get(i));
                    }
                }
            }

            results.values = searchList;
            results.count = searchList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            getVendorDetails = (ArrayList<GetVendorDetail>) results.values;
            if (getVendorDetails != null)
                notifyDataSetChanged();
        }
    }

    public interface Actionable {
        void updateRecordCount(int count);
    }
}
