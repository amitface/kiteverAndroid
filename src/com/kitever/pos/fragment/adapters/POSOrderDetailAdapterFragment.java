package com.kitever.pos.fragment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.pos.model.data.FetchOrder;

import java.util.ArrayList;

import sms19.inapp.msg.constant.Utils;

/**
 * Created by dev on 17/1/17.
 */

public class POSOrderDetailAdapterFragment extends BaseAdapter {

    ArrayList<FetchOrder> orderDetailsList;
    private String selectedItemType;
    private ArrayList<FetchOrder> filteredArrayList = null;
    private Double total, discount;
    public POSOrderDetailAdapterFragment(
            ArrayList<FetchOrder> orderDetailsList) {
        // TODO Auto-generated constructor stub
        this.orderDetailsList = orderDetailsList;
        filteredArrayList = this.orderDetailsList;
        total = 0.0;
        discount = 0.0;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(filteredArrayList!=null)
            return filteredArrayList.size();
        else
            return 0;
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
        POSOrderDetailAdapterFragment.ViewHolder holder;
        if (convertView == null) {
            holder = new POSOrderDetailAdapterFragment.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.pos_orderby_id_adapter_layout,
                    parent,false);
            holder.item = (TextView) convertView.findViewById(R.id.order_product_name);
            holder.quantity = (TextView) convertView.findViewById(R.id.order_product_quantity);
            holder.amt = (TextView) convertView.findViewById(R.id.order_product_bill_amount);
            convertView.setTag(holder);
        } else {
            holder = (POSOrderDetailAdapterFragment.ViewHolder) convertView.getTag();
        }
        holder.item.setText(""+filteredArrayList.get(position).getProductName());

        holder.quantity.setText(""+filteredArrayList.get(position).getQuantity());
        holder.amt.setText(Utils.doubleToString(filteredArrayList.get(position).getBillAmount()));

        return convertView;
    }

    private static class ViewHolder {
        TextView item, quantity, amt;
    }

    public void setSelectedItemType(String itemType) {
        selectedItemType = itemType;
    }
}

