package com.kitever.pos.fragment.purchaseList.PurchaseOutStanding;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by android on 9/3/17.
 */

public class PurchaseOutStandingAdapter extends RecyclerView.Adapter<PurchaseOutStandingAdapter.MyViewHolder> implements Filterable {
    private ArrayList<GetOutstandingPurchase> getOutstandingPurchases;
    private ArrayList<GetOutstandingPurchase> finalOutstandingPurchases;
    private Context context;
    private ItemFilter itemFilter;
    private int selectedItemPosition = -1;
    private PurchaseOutStandingAdapter.Actionable actionable;

    public PurchaseOutStandingAdapter(Context context, ArrayList<GetOutstandingPurchase> getOutstandingPurchases, PuchaseOutStandingFragment puchaseOutStandingFragment) {
        this.getOutstandingPurchases = getOutstandingPurchases;
        this.context = context;
        this.finalOutstandingPurchases = getOutstandingPurchases;
        itemFilter = new ItemFilter();
        actionable = (Actionable) puchaseOutStandingFragment;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPurchaseOutStandingDate, tvPurchaseOutStandingMonth, tvPurchaseOutStandingInvoice,
                tvPurchaseOutStandingPoNum, tvPurchaseOutStandingVendor, tvPurchaseOutStandingBill,
                tvPurchaseOutStandingPaid, tvPurchaseOutStandingBalance, tvPurchaseOutStandingLabelBalance,
                tvPurchaseOutStandingLabelBill;

        public MyViewHolder(View view) {
            super(view);
            tvPurchaseOutStandingDate = (TextView) view.findViewById(R.id.tvPurchaseOutStandingDate);
            tvPurchaseOutStandingMonth = (TextView) view.findViewById(R.id.tvPurchaseOutStandingMonth);
            tvPurchaseOutStandingInvoice = (TextView) view.findViewById(R.id.tvPurchaseOutStandingInvoice);
            tvPurchaseOutStandingPoNum = (TextView) view.findViewById(R.id.tvPurchaseOutStandingPoNum);
            tvPurchaseOutStandingVendor = (TextView) view.findViewById(R.id.tvPurchaseOutStandingVendor);
            tvPurchaseOutStandingBill = (TextView) view.findViewById(R.id.tvPurchaseOutStandingBill);
            tvPurchaseOutStandingBalance = (TextView) view.findViewById(R.id.tvPurchaseOutStandingBalance);
            tvPurchaseOutStandingLabelBalance = (TextView) view.findViewById(R.id.tvPurchaseOutStandingLabelBalance);
            tvPurchaseOutStandingLabelBill = (TextView) view.findViewById(R.id.tvPurchaseOutStandingLabelBill);


            tvPurchaseOutStandingDate.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseOutStandingMonth.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseOutStandingInvoice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseOutStandingPoNum.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseOutStandingVendor.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseOutStandingBill.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseOutStandingBalance.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseOutStandingLabelBalance.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchaseOutStandingLabelBill.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

            setRobotoThinFont(tvPurchaseOutStandingDate,context);
            setRobotoThinFont(tvPurchaseOutStandingMonth,context);
            setRobotoThinFont(tvPurchaseOutStandingInvoice,context);
            setRobotoThinFont(tvPurchaseOutStandingPoNum,context);
            setRobotoThinFont(tvPurchaseOutStandingVendor,context);
            setRobotoThinFont(tvPurchaseOutStandingBill,context);
            setRobotoThinFont(tvPurchaseOutStandingBalance,context);
            setRobotoThinFont(tvPurchaseOutStandingLabelBalance,context);
            setRobotoThinFont(tvPurchaseOutStandingLabelBill,context);
        }
    }

    @Override
    public PurchaseOutStandingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.purchase_outstanding_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GetOutstandingPurchase temp = getOutstandingPurchases.get(position);
        String pattern1 = "d-MMM ,yy";

        String s[] = new String[2];
        if (temp.getPurchaseDate() != null && !temp.getPurchaseDate().equals("")) {
            SimpleDateFormat format = new SimpleDateFormat(pattern1);
            try {
                Date date = new Date(temp.getPurchaseDate());
                format = new SimpleDateFormat(pattern1);
                s = format.format(date).split("-");
            } catch (Exception c) {

            }
        }

        holder.tvPurchaseOutStandingDate.setText(s[0]);
        holder.tvPurchaseOutStandingMonth.setText(s[1]);
        holder.tvPurchaseOutStandingInvoice.setText(temp.getmInvoiceNo());
        holder.tvPurchaseOutStandingPoNum.setText(String.valueOf(temp.getPO_NO()));
        holder.tvPurchaseOutStandingVendor.setText(temp.getContactName());
        holder.tvPurchaseOutStandingBill.setText(String.valueOf(temp.getBillAmount()));
        holder.tvPurchaseOutStandingBalance.setText(String.valueOf(temp.getBalance()));
    }

    @Override
    public int getItemCount() {
        actionable.updateCount(getOutstandingPurchases.size());
        return getOutstandingPurchases.size();
    }

    public void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
    }

    public ArrayList<GetOutstandingPurchase> getOutstandingPurchaseList() {
        return getOutstandingPurchases;
    }

    public void setFinalOutstandingPurchases(GetOutstandingPurchase temp) {
        for (int i = 0; i < finalOutstandingPurchases.size(); i++) {
            if (temp.getID() == finalOutstandingPurchases.get(i).getID()) {
                finalOutstandingPurchases.get(i).setBalance(temp.getBalance());
                finalOutstandingPurchases.get(i).setBillAmount(temp.getBillAmount());
                finalOutstandingPurchases.get(i).setPaidAmount(temp.getPaidAmount());
                break;
            }
        }
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private boolean greaterRange(Double value, Double orignal) {
        return orignal > value;
    }

    private boolean lesserRange(Double value, Double orignal) {
        return orignal < value;
    }

    class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterable = constraint.toString().toLowerCase();

            if (filterable.length() == 0) {
                results.values = finalOutstandingPurchases;
                results.count = finalOutstandingPurchases.size();
                return results;
            }

            int count = finalOutstandingPurchases.size();
            List<GetOutstandingPurchase> searchList = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                if (selectedItemPosition == 1) {
                    if (finalOutstandingPurchases.get(i).getContactName().toLowerCase().contains(filterable))
                        searchList.add(finalOutstandingPurchases.get(i));
                } else if (selectedItemPosition == 2) {
                    if (finalOutstandingPurchases.get(i).getPO_NO().toLowerCase().contains(filterable))
                        searchList.add(finalOutstandingPurchases.get(i));
                } else if (selectedItemPosition == 3) {
                    if (finalOutstandingPurchases.get(i).getmInvoiceNo().toLowerCase().contains(filterable))
                        searchList.add(finalOutstandingPurchases.get(i));
                } else if (selectedItemPosition == 4) {
                    if (lesserRange(finalOutstandingPurchases.get(i).getBillAmount(), Double.parseDouble(filterable))) {
                        searchList.add(finalOutstandingPurchases.get(i));
                    }
                } else if (selectedItemPosition == 5) {
                    if (greaterRange(finalOutstandingPurchases.get(i).getBillAmount(), Double.parseDouble(filterable))) {
                        searchList.add(finalOutstandingPurchases.get(i));
                    }
                } else if (selectedItemPosition == 6) {
                    if (lesserRange(finalOutstandingPurchases.get(i).getBalance(), Double.parseDouble(filterable))) {
                        searchList.add(finalOutstandingPurchases.get(i));
                    }
                } else if (selectedItemPosition == 7) {
                    if (greaterRange(finalOutstandingPurchases.get(i).getBalance(), Double.parseDouble(filterable))) {
                        searchList.add(finalOutstandingPurchases.get(i));
                    }
                }
            }

            results.values = searchList;
            results.count = searchList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            getOutstandingPurchases = (ArrayList<GetOutstandingPurchase>) results.values;
            if (getOutstandingPurchases != null)
                notifyDataSetChanged();
        }
    }

    public interface Actionable {
        void updateCount(int count);
    }
}
