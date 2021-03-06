package com.kitever.pos.fragment.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.pos.fragment.POSOrdersScreenFragment;
import com.kitever.pos.model.data.OrderDetailModelData;
import com.kitever.utils.DateHelper;
import com.kitever.utils.TotalRows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class POSOrdersAdapterFragment extends BaseAdapter implements Filterable {

    ArrayList<OrderDetailModelData> orderDetailsList;
    private String selectedItemType;
    private POSOrdersAdapterFragment.ItemFilter mItemFilter = new POSOrdersAdapterFragment.ItemFilter();
    private ArrayList<OrderDetailModelData> filteredArrayList = null;

    private POSOrdersScreenFragment context;
    private TotalRows rows;

    public POSOrdersAdapterFragment(POSOrdersScreenFragment context,
                            ArrayList<OrderDetailModelData> orderDetailsList) {
        // TODO Auto-generated constructor stub
        this.orderDetailsList = orderDetailsList;
        filteredArrayList = this.orderDetailsList;
        this.context = context;
        rows = (TotalRows)context;
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

    public void clearFilters()
    {
        filteredArrayList = this.orderDetailsList;
        notifyDataSetChanged();
    }

    public OrderDetailModelData getClickObject(int position)
    {
        return filteredArrayList.get(position);
    }

    public String getorderId(int position)
    {
        return  filteredArrayList.get(position).getOrderno();
    }

    public String getName(int position)
    {
        return filteredArrayList.get(position).getContactName();
    }

    public String getNumber(int position)
    {
        return filteredArrayList.get(position).getMobile();
    }

    public Boolean getMailStatus(int position)
    {
        return !filteredArrayList.get(position).getEmail().equals("");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        POSOrdersAdapterFragment.ViewHolder holder;
        if (convertView == null) {
            holder = new POSOrdersAdapterFragment.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.pos_order_adapter_layout,
                    parent,false);
            holder.orderNo = (TextView) convertView.findViewById(R.id.order_no);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.month = (TextView) convertView.findViewById(R.id.month);
            holder.customerName = (TextView) convertView
                    .findViewById(R.id.customer_name);
            holder.totalAmt = (TextView) convertView
                    .findViewById(R.id.total_amt);
            holder.paid = (TextView) convertView.findViewById(R.id.paid);

            holder.page_adapter_layout=(RelativeLayout) convertView
                    .findViewById(R.id.page_adapter_layout);

            setRobotoThinFont(holder.date,context.getActivity());
            setRobotoThinFont(holder.month,context.getActivity());
            setRobotoThinFont(holder.customerName,context.getActivity());
            setRobotoThinFont(holder.orderNo,context.getActivity());
            setRobotoThinFont(holder.totalAmt,context.getActivity());
            setRobotoThinFont(holder.paid,context.getActivity());

            holder.page_adapter_layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_ENABLED));
            holder.date.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.month.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.customerName.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.orderNo.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.totalAmt.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.paid.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));

            convertView.setTag(holder);
        } else {
            holder = (POSOrdersAdapterFragment.ViewHolder) convertView.getTag();
        }
        OrderDetailModelData object = filteredArrayList.get(position);
        holder.orderNo.setText(object.getOrderCode());
        holder.customerName.setText(object.getContactName());

        String amount_str= Utils.doubleToString(object.getBillAmount());


        holder.totalAmt.setText(amount_str);
        holder.paid.setText(object.getPaidAmount());

        String pattern = "MM/dd/yyyy";
        String pattern1 = "d-MMM ,yy";

        String s[] = new String[2];
        SimpleDateFormat format ;
        try {
            Date date =  new Date(Long.parseLong(object.getOrderDate()));
//	      Date date =  format.parse(object.getOrderDate());
            format = new SimpleDateFormat(pattern1);
//	      System.out.println("Date"+date+ " new Date"+format.format(date));
            s=format.format(date).split("-");
        }catch(Exception c)
        {
            System.out.println("Date erroer"+c.getMessage());
        }

        holder.date.setText(s[0]);
        holder.month.setText(s[1]);
        return convertView;
    }

    private static class ViewHolder {
        TextView orderNo, date, month, customerName, totalAmt, paid;
        RelativeLayout page_adapter_layout;
    }

    public void setSelectedItemType(String itemType) {
        selectedItemType = itemType;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            List<OrderDetailModelData> list = orderDetailsList;

            if(filterString.length()==0 || selectedItemType.equals("Last 50") || selectedItemType.equals(""))
            {
                results.values = list;
                results.count = list.size();
                return results;
            }

            int count = list.size();
            final ArrayList<OrderDetailModelData> nlist = new ArrayList<OrderDetailModelData>(
                    count);

            String filterableString = "";

            for (int i = 0; i < count; i++) {

                if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Order number")) {
                    filterableString = list.get(i).getOrderCode();
                    if (filterableString.toLowerCase().contains(filterString)) nlist.add(list.get(i));
                } else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Date")) {
                    filterableString = list.get(i).getOrderDate();
                    if (filterableString.toLowerCase().contains(filterString))  nlist.add(list.get(i));
                } else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Contact")) {
                    filterableString = list.get(i).getCombined();
                    if (filterableString.toLowerCase().contains(filterString))  nlist.add(list.get(i));
                } else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Amount >=")) {
                   // filterableString = Double.toString(list.get(i).getBillAmount());

                    try {
                        Double filterableInt =list.get(i).getBillAmount();
                        Double filterInt = Double.parseDouble(filterString.trim());
                        if(filterableInt >= filterInt)
                        {
                            nlist.add(list.get(i));
                        }
                    } catch(NumberFormatException e){e.printStackTrace();}

                }  else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Amount <=")) {
                    //filterableString = Double.toString(list.get(i).getBillAmount());
                    try {
                        Double filterableInt =list.get(i).getBillAmount();
                        Double filterInt = Double.parseDouble(filterString.trim());
                        if(filterableInt <= filterInt)
                        {
                            nlist.add(list.get(i));
                        }
                    } catch(NumberFormatException e){e.printStackTrace();}
                }

                else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Paid")) {
                    filterableString = list.get(i).getPaidAmount().toString();
                    if (filterableString.toLowerCase().contains(filterString)) nlist.add(list.get(i));
                }

//				else {
//					filterableString = list.get(i).getOrderCode();
//				}

               /* if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }*/
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            filteredArrayList = (ArrayList<OrderDetailModelData>) results.values;
            if (filteredArrayList!=null)
                notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return mItemFilter;
    }

    public void dateRangeFilter(int code)
    {
        long orderDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(dateFormat.format(DateHelper.getDate(DateHelper.getYear(cal.getTimeInMillis()),DateHelper.getMonthOfYear(cal.getTimeInMillis()),DateHelper.getDayOfMonth(cal.getTimeInMillis())))));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		orderDate = DateHelper.getDate(DateHelper.getYear(cal.getTimeInMillis()),DateHelper.getMonthOfYear(cal.getTimeInMillis()),DateHelper.getDayOfMonth(cal.getTimeInMillis()));
        orderDate = cal.getTimeInMillis();
        System.out.println("Current date "+orderDate+" code "+code);
        switch(code)
        {
            case 1:
                dateFilter(orderDate, orderDate+86400000L);
                break;
            case 2:
                dateFilter(orderDate-86400000L,orderDate);
                break;
            case 3:
                cal.set(Calendar.DAY_OF_WEEK,  cal.getFirstDayOfWeek());
                long weekMonth=cal.getTimeInMillis();
                System.out.println("Week date "+weekMonth+" code "+code);
                dateFilter(weekMonth,orderDate+86400000L);
                break;
            case 4:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                long firstMonth=cal.getTimeInMillis();
                System.out.println("First date "+firstMonth+" code "+code);
                dateFilter(firstMonth,orderDate+86400000L);
                break;
            case 5:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                long lastMonth=cal.getTimeInMillis();
                System.out.println("Last Month "+lastMonth+" code "+code);
                dateFilter(lastMonth-(86400000L*30L),lastMonth);
                break;

            default:
                break;
        }
    }

    public void dateFilter(Long startDate, Long endDate)
    {
        long orderDate;
        List<OrderDetailModelData> list = orderDetailsList;
        int count = list.size();

        System.out.println("start "+startDate+ " end "+endDate);

        final ArrayList<OrderDetailModelData> nlist = new ArrayList<OrderDetailModelData>(
                count);

        for (int i = 0; i < count; i++) {
            orderDate = Long.parseLong(list.get(i).getOrderDate());
//				 orderDate = list.get(i).getOrderDate();
            if(orderDate>=startDate && orderDate<endDate)
                nlist.add(list.get(i));
        }
        // TODO Auto-generated method stub
        filteredArrayList = nlist;
        if (filteredArrayList.size() >= 0)
            notifyDataSetChanged();
        else
            clearFilters();
    }

    public void setPaidFiltered() {
        // TODO Auto-generated method stub
        filteredArrayList=new ArrayList<>();
        for(int i=0; i< orderDetailsList.size();i++)
        {
            if(Double.parseDouble(orderDetailsList.get(i).getPaidAmount())==orderDetailsList.get(i).getBillAmount())
            {
                filteredArrayList.add(orderDetailsList.get(i));
            }
        }
        if(filteredArrayList!=null)
            notifyDataSetChanged();
    }

    public void setUnPaidFiltered() {
        // TODO Auto-generated method stub
        filteredArrayList=new ArrayList<>();
        for(int i=0; i< orderDetailsList.size();i++)
        {
            if(Double.parseDouble(orderDetailsList.get(i).getPaidAmount())==0.0)
            {
                filteredArrayList.add(orderDetailsList.get(i));
            }
        }
        if(filteredArrayList!=null)
            notifyDataSetChanged();
    }

    public void setParitalllyPaidFiltered() {
        // TODO Auto-generated method stub
        filteredArrayList=new ArrayList<>();
        for(int i=0; i< orderDetailsList.size();i++)
        {
            if(Double.parseDouble(orderDetailsList.get(i).getPaidAmount())> 0.0 )
            {
                filteredArrayList.add(orderDetailsList.get(i));
            }
        }
        if(filteredArrayList!=null)
            notifyDataSetChanged();
    }

    public void changeList(ArrayList<OrderDetailModelData> order) {
        // TODO Auto-generated method stub
        filteredArrayList = order;
        notifyDataSetChanged();
    }

    public ArrayList<OrderDetailModelData> getOrderList()
    {
        return filteredArrayList;
    }

}
