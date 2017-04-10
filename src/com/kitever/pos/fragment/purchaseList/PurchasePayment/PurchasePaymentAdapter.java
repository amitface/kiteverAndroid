package com.kitever.pos.fragment.purchaseList.PurchasePayment;

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
import java.util.TimeZone;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by android on 9/3/17.
 */

public class PurchasePaymentAdapter extends RecyclerView.Adapter<PurchasePaymentAdapter.MyViewHolder> implements Filterable{

    private Context context;
    private ArrayList<GetPaymentPurchase> getPaymentPurchases;
    private ArrayList<GetPaymentPurchase> finalPaymentPurchases;
    private ItemFilter itemFilter;
    private Actionable actionable;
    private int selectedItemPosition = -1;


    public PurchasePaymentAdapter(Context context, ArrayList<GetPaymentPurchase> getPaymentPurchases, PurchasePaymentFragment purchasePaymentFragment) {
        this.context = context;
        this.getPaymentPurchases = getPaymentPurchases;
        itemFilter = new ItemFilter();
        finalPaymentPurchases = getPaymentPurchases;
        actionable = (Actionable) purchasePaymentFragment;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvPurchasePaymentDate, tvPurchasePaymentMonth, tvPurchasePaymentInvoice,
                tvPurchasePaymentPoNum, tvPurchasePaymentVendor, tvPurchasePaymentBill,
                tvPurchasePaymentPaid, tvPurchasePaymentBalance, tvPurchasePaymentLabelPaid,
                tvPurchasePaymentLabelBill;
        public MyViewHolder(View view) {
            super(view);
            tvPurchasePaymentDate = (TextView) view.findViewById(R.id.tvPurchasePaymentDate);
            tvPurchasePaymentMonth = (TextView) view.findViewById(R.id.tvPurchasePaymentMonth);
            tvPurchasePaymentInvoice = (TextView) view.findViewById(R.id.tvPurchasePaymentInvoice);
            tvPurchasePaymentPoNum = (TextView) view.findViewById(R.id.tvPurchasePaymentPoNum);
            tvPurchasePaymentVendor = (TextView) view.findViewById(R.id.tvPurchasePaymentVendor);
            tvPurchasePaymentBill = (TextView) view.findViewById(R.id.tvPurchasePaymentBill);
            tvPurchasePaymentPaid = (TextView) view.findViewById(R.id.tvPurchasePaymentPaid);
            tvPurchasePaymentLabelPaid = (TextView) view.findViewById(R.id.tvPurchasePaymentLabelPaid);
            tvPurchasePaymentLabelBill = (TextView) view.findViewById(R.id.tvPurchasePaymentLabelBill);



            tvPurchasePaymentDate.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchasePaymentMonth.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchasePaymentInvoice.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchasePaymentPoNum.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchasePaymentVendor.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchasePaymentBill.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchasePaymentPaid.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchasePaymentLabelBill.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvPurchasePaymentLabelPaid.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

            setRobotoThinFont(tvPurchasePaymentDate,context);
            setRobotoThinFont(tvPurchasePaymentMonth,context);
            setRobotoThinFont(tvPurchasePaymentInvoice,context);
            setRobotoThinFont(tvPurchasePaymentPoNum,context);
            setRobotoThinFont(tvPurchasePaymentVendor,context);
            setRobotoThinFont(tvPurchasePaymentBill,context);
            setRobotoThinFont(tvPurchasePaymentPaid,context);
            setRobotoThinFont(tvPurchasePaymentLabelPaid,context);
            setRobotoThinFont(tvPurchasePaymentLabelBill,context);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.purchase_payment_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GetPaymentPurchase temp = getPaymentPurchases.get(position);
        String pattern1 = "d-MMM ,yy";

        String s[] = new String[2];
        if (temp.getPaymentDate() != null && !temp.getPaymentDate().equals("")) {
            SimpleDateFormat format = new SimpleDateFormat(pattern1);
            try {
                Date date = new Date(temp.getPaymentDate());
                format = new SimpleDateFormat(pattern1);
//	      System.out.println("Date"+date+ " new Date"+format.format(date));
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                s = format.format(date).split("-");
            } catch (Exception c) {

            }
        }

        holder.tvPurchasePaymentDate.setText(s[0]);
        holder.tvPurchasePaymentMonth.setText(s[1]);
        holder.tvPurchasePaymentInvoice.setText(temp.getmInvoiceNo());
        holder.tvPurchasePaymentPoNum.setText(String.valueOf(temp.getmPoNo()));
        holder.tvPurchasePaymentVendor.setText(temp.getmContactName());
        holder.tvPurchasePaymentBill.setText(String.valueOf(temp.getBillAmount()));
        holder.tvPurchasePaymentPaid.setText(String.valueOf(temp.getPaidAmount()));
    }

    @Override
    public int getItemCount() {
        actionable.updateRecordCount(getPaymentPurchases.size());
        return getPaymentPurchases.size();
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    public void setSelectedItemPosition(int position)
    {
        selectedItemPosition = position;
    }

    public ArrayList<GetPaymentPurchase> getGetPaymentPurchaseList()
    {
        return getPaymentPurchases;
    }

    private boolean greaterRange(Double value, Double orignal) {
        return orignal > value;
    }

    private boolean lesserRange(Double value, Double orignal) {
        return orignal < value;
    }


    private class ItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterable = constraint.toString().toLowerCase();

            if(filterable.length()==0)
            {
                results.values = finalPaymentPurchases;
                results.count = finalPaymentPurchases.size();
                return results;
            }

            int count = finalPaymentPurchases.size();
            List<GetPaymentPurchase> searchList = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                if (selectedItemPosition == 1) {
                    if (finalPaymentPurchases.get(i).getmContactName().toLowerCase().contains(filterable))
                        searchList.add(finalPaymentPurchases.get(i));
                } else if (selectedItemPosition == 2){
                    if(finalPaymentPurchases.get(i).getmPoNo().toLowerCase().contains(filterable))
                        searchList.add(finalPaymentPurchases.get(i));
                } else if(selectedItemPosition == 3)
                {
                    if(finalPaymentPurchases.get(i).getmInvoiceNo().toLowerCase().contains(filterable))
                        searchList.add(finalPaymentPurchases.get(i));
                } else if(selectedItemPosition == 4)
                {
                    if(lesserRange(finalPaymentPurchases.get(i).getBillAmount(),Double.parseDouble(filterable)))
                    {
                        searchList.add(finalPaymentPurchases.get(i));
                    }
                } else if(selectedItemPosition == 5)
                {
                    if(greaterRange(finalPaymentPurchases.get(i).getBillAmount(),Double.parseDouble(filterable)))
                    {
                        searchList.add(finalPaymentPurchases.get(i));
                    }
                } else if(selectedItemPosition == 6)
                {
                    if(lesserRange(finalPaymentPurchases.get(i).getPaidAmount(),Double.parseDouble(filterable)))
                    {
                        searchList.add(finalPaymentPurchases.get(i));
                    }
                } else if(selectedItemPosition == 7)
                {
                    if(greaterRange(finalPaymentPurchases.get(i).getPaidAmount(),Double.parseDouble(filterable)))
                    {
                        searchList.add(finalPaymentPurchases.get(i));
                    }
                }
            }

            results.values = searchList;
            results.count = searchList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            getPaymentPurchases = (ArrayList<GetPaymentPurchase>) results.values;
            if(getPaymentPurchases!=null)
                notifyDataSetChanged();
        }
    }

    public interface Actionable
    {
        void updateRecordCount(int count);
    }
}
