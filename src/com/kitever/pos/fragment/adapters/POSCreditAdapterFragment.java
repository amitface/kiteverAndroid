package com.kitever.pos.fragment.adapters;

import android.graphics.Color;
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
import com.kitever.pos.fragment.POSCreditScreenFragment;
import com.kitever.pos.model.data.CreditDetails;
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

public class POSCreditAdapterFragment extends BaseAdapter implements Filterable {

    private ArrayList<CreditDetails> creditList;
    private ArrayList<CreditDetails> filteredArrayList;
    private String selectedItemType;
    private POSCreditAdapterFragment.ItemFilter mItemFilter = new POSCreditAdapterFragment.ItemFilter();
    private POSCreditScreenFragment context;
    private TotalRows rows;

    public POSCreditAdapterFragment(POSCreditScreenFragment context, ArrayList<CreditDetails> creditList) {
        // TODO Auto-generated constructor stub
        this.creditList = creditList;
        filteredArrayList = creditList;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        POSCreditAdapterFragment.ViewHolder holder;
        if (convertView == null) {
            holder = new POSCreditAdapterFragment.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.pos_credit_adapter_layout,
                    parent, false);

            holder.dateTime = (TextView) convertView
                    .findViewById(R.id.credit_date);
            holder.month = (TextView) convertView.findViewById(R.id.credit_month);
            holder.customerName = (TextView) convertView
                    .findViewById(R.id.customer_name);
            holder.orderId = (TextView) convertView.findViewById(R.id.order_id);
            holder.totalBill = (TextView) convertView
                    .findViewById(R.id.total_bill);
            holder.credit = (TextView) convertView.findViewById(R.id.credit);

            holder.page_adapter_layout=(RelativeLayout) convertView
                    .findViewById(R.id.page_adapter_layout);

            setRobotoThinFont(holder.dateTime,context.getActivity());
            setRobotoThinFont(holder.month,context.getActivity());
            setRobotoThinFont(holder.customerName,context.getActivity());
            setRobotoThinFont(holder.orderId,context.getActivity());
            setRobotoThinFont(holder.totalBill,context.getActivity());
            setRobotoThinFont(holder.credit,context.getActivity());

            holder.page_adapter_layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_ENABLED));
            holder.dateTime.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.month.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.customerName.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.orderId.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.totalBill.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.credit.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));

            convertView.setTag(holder);
        } else {
            holder = (POSCreditAdapterFragment.ViewHolder) convertView.getTag();
        }

        CreditDetails objData = filteredArrayList.get(position);


        String pattern = "MM/dd/yyyy";
        String pattern1 = "d-MMM ,yy";

        String s[] = new String[2];
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date date = new Date(Long.parseLong(objData.getOrderDate()));
            format = new SimpleDateFormat(pattern1);
//	      System.out.println("Date"+date+ " new Date"+format.format(date));
            s = format.format(date).split("-");
        } catch (Exception c) {
            System.out.println("Date erroer" + c.getMessage());
        }

        holder.dateTime.setText(s[0]);
        holder.month.setText(s[1]);

//		holder.dateTime.setText(objData.getOrderDate());
//		holder.month.setText("");
        holder.customerName.setText(objData.getContact_Name());
        holder.orderId.setText(objData.getOrderCode());
        holder.totalBill.setText(objData.getBillAmount());
        holder.credit.setText(objData.getBalanceAmount());
        return convertView;
    }

    private static class ViewHolder {
        TextView dateTime, month, customerName, orderId, totalBill, credit;
        RelativeLayout page_adapter_layout;
    }

    public void setSelectedItemType(String itemType) {
        selectedItemType = itemType;
    }

    private boolean greaterRange(Double value, Double orignal) {
        return orignal >= value;
    }

    private boolean lesserRange(Double value, Double orignal) {
        return orignal <= value;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();


            List<CreditDetails> list = creditList;

            if (filterString.length() == 0 || selectedItemType.equals("Last 50") || selectedItemType.equals("")) {
                results.values = list;
                results.count = list.size();
                return results;
            }
            int count = list.size();
            final ArrayList<CreditDetails> nlist = new ArrayList<CreditDetails>(
                    count);

            String filterableString = "";

            for (int i = 0; i < count; i++) {

                if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Date Time")) {
                    filterableString = list.get(i).getOrderDate();
                } else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Contact")) {
                    filterableString = list.get(i).getCombined();
                } else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Order Id")) {
                    filterableString = list.get(i).getOrderID();
                } else if (selectedItemType != null
                        && (selectedItemType.equalsIgnoreCase("Total Bill >=") || selectedItemType.equalsIgnoreCase("Total Bill <="))) {
                    filterableString = list.get(i).getBillAmount();
                } else if (selectedItemType != null
                        && (selectedItemType.equalsIgnoreCase("Credit <=") || selectedItemType.equalsIgnoreCase("Credit >="))) {
                    filterableString = list.get(i).getBalanceAmount();
                }
//				else {
//					filterableString = list.get(i).getContact_Name();
//				}

                try {
                    if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Credit >=")) {
                        if (greaterRange(Double.parseDouble(filterString), Double.parseDouble(filterableString)) == true)
                            nlist.add(list.get(i));
                    } else if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Credit <=")) {
                        if (lesserRange(Double.parseDouble(filterString), Double.parseDouble(filterableString)) == true)
                            nlist.add(list.get(i));
                    } else if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Total Bill >=")) {
                        if (greaterRange(Double.parseDouble(filterString), Double.parseDouble(filterableString)) == true)
                            nlist.add(list.get(i));
                    } else if (selectedItemType != null && selectedItemType.equalsIgnoreCase("Total Bill <=")) {
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

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            filteredArrayList = (ArrayList<CreditDetails>) results.values;
            if (filteredArrayList != null)
                notifyDataSetChanged();
        }

    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return mItemFilter;
    }

    public void clearFilters() {
        filteredArrayList = this.creditList;
        notifyDataSetChanged();
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
                System.out.println("First date " + firstMonth + " code " + code);
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
        List<CreditDetails> list = creditList;
        int count = list.size();

        System.out.println("start " + startDate + " end " + endDate);

        final ArrayList<CreditDetails> nlist = new ArrayList<CreditDetails>(
                count);

        for (int i = 0; i < count; i++) {
            orderDate = Long.parseLong(list.get(i).getOrderDate());
//				 orderDate = list.get(i).getOrderDate();
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

    public CreditDetails getClickObject(int position) {
        return filteredArrayList.get(position);
    }

    public ArrayList<CreditDetails> creditList()
    {
        return filteredArrayList;
    }

}