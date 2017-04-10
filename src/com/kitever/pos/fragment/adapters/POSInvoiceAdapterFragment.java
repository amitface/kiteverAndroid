package com.kitever.pos.fragment.adapters;

import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.pos.fragment.POSInvoiceScreenFragment;
import com.kitever.pos.model.data.FetchInvoice;
import com.kitever.utils.DateHelper;
import com.kitever.utils.TotalRows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by dev on 17/1/17.
 */

public class POSInvoiceAdapterFragment extends BaseAdapter implements Filterable {

    private SparseBooleanArray mSelectedItemsIds;
    ArrayList<FetchInvoice> posInvoiceList;
    private String selectedItemType;
    private POSInvoiceAdapterFragment.ItemFilter mItemFilter = new POSInvoiceAdapterFragment.ItemFilter();
    private ArrayList<FetchInvoice> filteredArrayList = null;
    private ArrayList<FetchInvoice> selectedArrayList = null;
    private POSInvoiceScreenFragment context;
    private TotalRows rows;

    public POSInvoiceAdapterFragment(POSInvoiceScreenFragment context, ArrayList<FetchInvoice> posInvoiceList) {
        super();
        mSelectedItemsIds = new SparseBooleanArray();
        this.posInvoiceList = posInvoiceList;
        filteredArrayList = this.posInvoiceList;
        selectedArrayList = new ArrayList<>();
        this.context = context;
        rows = (TotalRows) context;
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

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public void emptyArray() {
        mSelectedItemsIds = null;
        mSelectedItemsIds = new SparseBooleanArray();
        selectedArrayList = null;
        selectedArrayList = new ArrayList<>();
    }

    public void clearFilters() {
        filteredArrayList = this.posInvoiceList;
        notifyDataSetChanged();
    }

    private boolean greaterRange(Double value, Double orignal) {
        return orignal > value;
    }

    private boolean lesserRange(Double value, Double orignal) {
        return orignal < value;
    }

    public ArrayList<FetchInvoice> getArrayList() {

        for (int i = 0; i < mSelectedItemsIds.size(); i++) {
            selectedArrayList.add(filteredArrayList.get(mSelectedItemsIds.keyAt(i)));
        }

        return selectedArrayList;
    }

    public ArrayList<FetchInvoice> getAllArrayList() {
        return filteredArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        POSInvoiceAdapterFragment.ViewHolder holder;
        if (convertView == null) {
            holder = new POSInvoiceAdapterFragment.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.pos_invoice_adapter_layout,
                    parent, false);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.month = (TextView) convertView.findViewById(R.id.month);
            holder.clientName = (TextView) convertView.findViewById(R.id.invoice_client_name);
            holder.billAmit = (TextView) convertView.findViewById(R.id.invoice_billamount);
            holder.invoice = (TextView) convertView.findViewById(R.id.invoice_number);
            holder.amtPaid = (TextView) convertView
                    .findViewById(R.id.invoice_amount_paid);
            holder.balanceAmt = (TextView) convertView.findViewById(R.id.invoice_balance_amount);

            holder.amount_txt = (TextView) convertView.findViewById(R.id.invoice_billamount_txt);
            holder.paid_txt = (TextView) convertView.findViewById(R.id.invoice_amount_paid_txt);
            holder.balance_txt = (TextView) convertView.findViewById(R.id.invoice_balance_amount_txt);


            holder.page_adapter_layout=(LinearLayout) convertView
                    .findViewById(R.id.page_adapter_layout);

            setRobotoThinFont(holder.date,context.getActivity());
            setRobotoThinFont(holder.month,context.getActivity());
            setRobotoThinFont(holder.clientName,context.getActivity());
            setRobotoThinFont(holder.billAmit,context.getActivity());
            setRobotoThinFont(holder.invoice,context.getActivity());
            setRobotoThinFont(holder.amtPaid,context.getActivity());
            setRobotoThinFont(holder.balanceAmt,context.getActivity());

            setRobotoThinFont(holder.amount_txt,context.getActivity());
            setRobotoThinFont(holder.paid_txt,context.getActivity());
            setRobotoThinFont(holder.balance_txt,context.getActivity());

            holder.page_adapter_layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_ENABLED));
            holder.date.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.month.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.clientName.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.billAmit.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.invoice.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.amtPaid.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.balanceAmt.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));

            holder.amount_txt.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.paid_txt.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.balance_txt.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));

            convertView.setTag(holder);

        } else {
            holder = (POSInvoiceAdapterFragment.ViewHolder) convertView.getTag();
        }
        ArrayList<FetchInvoice> object = filteredArrayList;
        holder.clientName.setText(object.get(position).getCustomerName());
        holder.invoice.setText(object.get(position).getInvoiceCode());
        holder.billAmit.setText(object.get(position).getBillAmount());
        holder.amtPaid.setText(object.get(position).getPaidAmount());
        holder.balanceAmt.setText(object.get(position).getBalanceAmount());

        String pattern = "MM/dd/yyyy";
        String pattern1 = "d-MMM ,yy";

        String s[] = new String[2];
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date date = new Date(object.get(position).getOrderDate());
//	      Date date =  format.parse(object.getOrderDate());
            format = new SimpleDateFormat(pattern1);
//	      System.out.println("Date"+date+ " new Date"+format.format(date));
            s = format.format(date).split("-");
        } catch (Exception c) {
            System.out.println("Date erroer" + c.getMessage());
        }

        holder.date.setText(s[0]);
        holder.month.setText(s[1]);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return mItemFilter;
    }

    private static class ViewHolder {
        TextView clientName, billAmit, invoice, amtPaid, balanceAmt, date, month;
        TextView amount_txt,paid_txt,balance_txt;
        LinearLayout page_adapter_layout;
    }

    public void setSelectedItemType(String itemType) {
        selectedItemType = itemType;
    }

    public void dateRangeFilter(int code) {
        long orderDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(dateFormat.format(DateHelper.getDate(DateHelper.getYear(cal.getTimeInMillis()), DateHelper.getMonthOfYear(cal.getTimeInMillis()), DateHelper.getDayOfMonth(cal.getTimeInMillis())))));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		orderDate = DateHelper.getDate(DateHelper.getYear(cal.getTimeInMillis()),DateHelper.getMonthOfYear(cal.getTimeInMillis()),DateHelper.getDayOfMonth(cal.getTimeInMillis()));
        orderDate = cal.getTimeInMillis();
        System.out.println("Current date " + orderDate + " code " + code);
        switch (code) {
            case 1:
                dateFilter(orderDate, orderDate + 86400000L);
                break;
            case 2:
                dateFilter(orderDate - 86400000L, orderDate);
                break;
            case 3:
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                long weekMonth = cal.getTimeInMillis();
                System.out.println("Week date " + weekMonth + " code " + code);
                dateFilter(weekMonth, orderDate + 86400000L);
                break;
            case 4:

                cal.set(Calendar.DAY_OF_MONTH, 1);
                long firstMonth = cal.getTimeInMillis();
//				System.out.println("First date "+firstMonth+" code "+code);
                dateFilter(firstMonth, orderDate + 86400000L);
                break;
            case 5:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                long lastMonth = cal.getTimeInMillis();
                System.out.println("Last Month " + lastMonth + " code " + code);
                dateFilter(lastMonth - (86400000L * 30L), lastMonth);
                break;

            default:
                break;
        }
    }

    public void dateFilter(Long startDate, Long endDate) {
        long orderDate;
        List<FetchInvoice> list = posInvoiceList;
        int count = list.size();

        System.out.println("start " + startDate + " end " + endDate);

        final ArrayList<FetchInvoice> nlist = new ArrayList<FetchInvoice>(
                count);

        for (int i = 0; i < count; i++) {
//				 orderDate = Long.parseLong(list.get(i).getOrderDate());
            orderDate = list.get(i).getOrderDate();
            if (orderDate >= startDate && orderDate < endDate)
                nlist.add(list.get(i));
        }
        // TODO Auto-generated method stub
        filteredArrayList = nlist;
        if (filteredArrayList.size() >= 0)
            notifyDataSetChanged();
        else
            clearFilters();
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            List<FetchInvoice> list = posInvoiceList;

            if (filterString.length() == 0 || selectedItemType.equals("Last 50") || selectedItemType.equals("")) {
                results.values = list;
                results.count = list.size();
                return results;
            }

            int count = list.size();
            final ArrayList<FetchInvoice> nlist = new ArrayList<FetchInvoice>(
                    count);

            String filterableString = "";

            for (int i = 0; i < count; i++) {

                if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Invoice Code")) {
                    filterableString = list.get(i).getInvoiceCode();
                } else if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Contact")) {
                    filterableString = list.get(i).getContactName();
                } else if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Bill Amount >=")) {
                    filterableString = list.get(i).getBillAmount();
                } else if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Bill Amount <=")) {
                    filterableString = list.get(i).getBillAmount();
                } else if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Balance Amount >=")) {
                    filterableString = list.get(i).getBalanceAmount();
                } else if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Balance Amount <=")) {
                    filterableString = list.get(i).getBalanceAmount();
                }

                try {
                    if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Bill Amount >=")) {
                        if (greaterRange(Double.parseDouble(filterString), Double.parseDouble(filterableString)) == true)
                            nlist.add(list.get(i));
                    } else if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Bill Amount <=")) {
                        if (lesserRange(Double.parseDouble(filterString), Double.parseDouble(filterableString)) == true)
                            nlist.add(list.get(i));
                    } else if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Balance Amount >=")) {
                        if (greaterRange(Double.parseDouble(filterString), Double.parseDouble(filterableString)) == true)
                            nlist.add(list.get(i));
                    } else if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Balance Amount <=")) {
                        if (lesserRange(Double.parseDouble(filterString), Double.parseDouble(filterableString)) == true)
                            nlist.add(list.get(i));
                    } else if (filterableString.toLowerCase().contains(filterString)) {
                        nlist.add(list.get(i));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // TODO Auto-generated method stub
            filteredArrayList = (ArrayList<FetchInvoice>) results.values;
            if (filteredArrayList != null)
                notifyDataSetChanged();
        }
    }

    public ArrayList<FetchInvoice> getInvoiceList() {
      return filteredArrayList;
        }
}