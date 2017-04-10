package com.kitever.pos.fragment.purchaseList;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by android on 7/3/17.
 */

public class PurchaseListAdapter extends RecyclerView.Adapter<PurchaseListAdapter.MyViewHolder> implements Filterable {

    private ArrayList<GetPurchaseMaster> getPurchaseMaster;
    private ArrayList<GetPurchaseMaster> finalPurchaseMaster;
    private Context context;
    private Integer selectedItemPosition;
    private PurchaseListAdapter.ItemFilter itemFilter;
    private Actionable actionable;

    public PurchaseListAdapter(Context context, ArrayList<GetPurchaseMaster> getPurchaseMaster, PurchaseListFragment purchaseListFragment) {
        this.context = context;
        this.getPurchaseMaster = getPurchaseMaster;
        itemFilter = new ItemFilter();
        actionable = (Actionable) purchaseListFragment;
        this.finalPurchaseMaster = getPurchaseMaster;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPurchaseListDate, tvPurchaseListMonth, tvPurchaseListInvoice,
                tvPurchaseListPoNum, tvPurchaseListVendor, tvPurchaseListBill,
                tvPurchaseListPaid, tvPurchaseListBalance, tvPurchaseListLabelBill,
                tvPurchaseListLabelPaid, tvPurchaseListLabelBalance;
        public LinearLayout lyt;

        public MyViewHolder(View view) {
            super(view);
            tvPurchaseListDate = (TextView) view.findViewById(R.id.tvPurchaseListDate);
            tvPurchaseListMonth = (TextView) view.findViewById(R.id.tvPurchaseListMonth);
            tvPurchaseListInvoice = (TextView) view.findViewById(R.id.tvPurchaseListInvoice);
            tvPurchaseListPoNum = (TextView) view.findViewById(R.id.tvPurchaseListPoNum);
            tvPurchaseListVendor = (TextView) view.findViewById(R.id.tvPurchaseListVendor);
            tvPurchaseListBill = (TextView) view.findViewById(R.id.tvPurchaseListBill);
            tvPurchaseListPaid = (TextView) view.findViewById(R.id.tvPurchaseListPaid);
            tvPurchaseListBalance = (TextView) view.findViewById(R.id.tvPurchaseListBalance);
            tvPurchaseListLabelBalance = (TextView) view.findViewById(R.id.tvPurchaseListLabelBalance);
            tvPurchaseListLabelBill = (TextView) view.findViewById(R.id.tvPurchaseListLabelBill);
            tvPurchaseListLabelPaid = (TextView) view.findViewById(R.id.tvPurchaseListLabelPaid);
            lyt=(LinearLayout)view.findViewById(R.id.adapter_lyt);

            lyt.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_ENABLED));

            setRobotoThinFont(tvPurchaseListDate,context);
            setRobotoThinFont(tvPurchaseListMonth,context);
            setRobotoThinFont(tvPurchaseListInvoice,context);
            setRobotoThinFont(tvPurchaseListPoNum,context);
            setRobotoThinFont(tvPurchaseListVendor,context);
            setRobotoThinFont(tvPurchaseListBill,context);
            setRobotoThinFont(tvPurchaseListPaid,context);
            setRobotoThinFont(tvPurchaseListBalance,context);
            setRobotoThinFont(tvPurchaseListLabelBill,context);
            setRobotoThinFont(tvPurchaseListLabelPaid,context);
            setRobotoThinFont(tvPurchaseListLabelBalance,context);

            tvPurchaseListDate.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            tvPurchaseListMonth.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            tvPurchaseListInvoice.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            tvPurchaseListPoNum.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            tvPurchaseListVendor.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            tvPurchaseListBill.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            tvPurchaseListPaid.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            tvPurchaseListBalance.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));



        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchase_list_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GetPurchaseMaster temp = getPurchaseMaster.get(position);
        String pattern1 = "dd-MMM, yy";
        String s[] = new String[2];
        if (temp.getDateAdded() != null && !temp.getDateAdded().equals("")) {
            SimpleDateFormat format;
            try {
                Date date = new Date(temp.getDateAdded());

                format = new SimpleDateFormat(pattern1);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                s = format.format(date).split("-");
            } catch (Exception c) {

            }
        } else
            s[0] = "";
        holder.tvPurchaseListDate.setText(s[0]);
        holder.tvPurchaseListMonth.setText(s[1]);
        holder.tvPurchaseListInvoice.setText(temp.getInvoiceNO());
        holder.tvPurchaseListPoNum.setText(temp.getPONO());
        holder.tvPurchaseListVendor.setText(temp.getmContactName());
        holder.tvPurchaseListBill.setText(String.valueOf(temp.getBillAmount()));
        holder.tvPurchaseListPaid.setText(String.valueOf(temp.getPaidAmount()));
        holder.tvPurchaseListBalance.setText(String.valueOf(temp.getBalance()));
    }

    @Override
    public int getItemCount() {
        actionable.updateRecordCount(getPurchaseMaster.size());
        return getPurchaseMaster.size();
    }

    public ArrayList<GetPurchaseMaster> getPurchaseList() {
        return getPurchaseMaster;
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    public void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
    }

    public interface Actionable {
        void updateRecordCount(int count);
    }

    private boolean greaterRange(Double value, Double orignal) {
        return orignal > value;
    }

    private boolean lesserRange(Double value, Double orignal) {
        return orignal < value;
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterString = constraint.toString().toLowerCase();
            List<GetPurchaseMaster> filterPurchaseMaster = finalPurchaseMaster;

            if (filterString.length() == 0) {
                results.values = finalPurchaseMaster;
                results.count = finalPurchaseMaster.size();
                return results;
            }

            int count = filterPurchaseMaster.size();
            List<GetPurchaseMaster> listSearched = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                if (selectedItemPosition == 1) {
                    if ((filterPurchaseMaster.get(i).getPONO().toLowerCase()).contains(filterString))
                        listSearched.add(filterPurchaseMaster.get(i));
                } else if (selectedItemPosition == 2) {
                    if ((filterPurchaseMaster.get(i).getInvoiceNO().toLowerCase()).contains(filterString))
                        listSearched.add(filterPurchaseMaster.get(i));
                } else if (selectedItemPosition == 3) {
                    if ((filterPurchaseMaster.get(i).getmContactName().toLowerCase()).contains(filterString))
                        listSearched.add(filterPurchaseMaster.get(i));
                } else if (selectedItemPosition == 4) {
                    if (lesserRange(filterPurchaseMaster.get(i).getBillAmount(), Double.parseDouble(filterString)))
                        listSearched.add(filterPurchaseMaster.get(i));
                } else if (selectedItemPosition == 5) {
                    if (greaterRange(filterPurchaseMaster.get(i).getBillAmount(), Double.parseDouble(filterString)))
                        listSearched.add(filterPurchaseMaster.get(i));
                } else if (selectedItemPosition == 6) {
                    if (lesserRange(filterPurchaseMaster.get(i).getPaidAmount(), Double.parseDouble(filterString)))
                        listSearched.add(filterPurchaseMaster.get(i));
                } else if (selectedItemPosition == 7) {
                    if (greaterRange(filterPurchaseMaster.get(i).getPaidAmount(), Double.parseDouble(filterString)))
                        listSearched.add(filterPurchaseMaster.get(i));
                } else if (selectedItemPosition == 8) {
                    if (lesserRange(filterPurchaseMaster.get(i).getBalance(), Double.parseDouble(filterString)))
                        listSearched.add(filterPurchaseMaster.get(i));
                } else if (selectedItemPosition == 9) {
                    if (greaterRange(filterPurchaseMaster.get(i).getBalance(), Double.parseDouble(filterString)))
                        listSearched.add(filterPurchaseMaster.get(i));
                }
            }
            results.values = listSearched;
            results.count = listSearched.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            getPurchaseMaster = (ArrayList<GetPurchaseMaster>) results.values;
            if (getPurchaseMaster != null)
                notifyDataSetChanged();
        }
    }
}
