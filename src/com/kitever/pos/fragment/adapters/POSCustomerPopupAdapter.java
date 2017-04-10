package com.kitever.pos.fragment.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.pos.activity.POSInvoicePdfViewer;
import com.kitever.pos.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.adapter.MircoSiteLeadsAdapter;
import sms19.listview.newproject.model.ClientLeadDetails;

/**
 * Created by android on 28/2/17.
 */

public class POSCustomerPopupAdapter extends RecyclerView.Adapter<POSCustomerPopupAdapter.MyViewHolder> {

    private Activity activity;
    private ArrayList<Order> orderArrayList;

    public POSCustomerPopupAdapter(Activity activity, ArrayList<Order> orderArrayList) {
        this.activity = activity;
        this.orderArrayList = orderArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, order;
        public TextView tablerow1, tablerow2, tablerow3;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.tvCustomerDate);
            order = (TextView) view.findViewById(R.id.tvCustomerOrder);
            tablerow1 = (TextView) view.findViewById(R.id.tablerowCustomer1);
            tablerow2 = (TextView) view.findViewById(R.id.tablerowCustomer2);
            tablerow3 = (TextView) view.findViewById(R.id.tablerowCustomer3);
            imageView = (ImageView) view.findViewById(R.id.imagePdfCustomer);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pos_customer_pop_up, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Order temp = orderArrayList.get(position);
        String pattern1 = "d-MMM";
        String s = "";
        if (temp.getOrderDate() != null && !temp.getOrderDate().equals("")) {
            SimpleDateFormat format = new SimpleDateFormat(pattern1);
            try {
                Date date = new Date(Long.parseLong(temp.getOrderDate()));
                format = new SimpleDateFormat(pattern1);
                s = format.format(date);
            } catch (Exception c) {

            }
        } else
            s = "";
        holder.date.setText(s);
        holder.order.setText(temp.getOrderCode());
        holder.tablerow1.setText(temp.getBillAmount());
        holder.tablerow2.setText(temp.getPaidAmount());
        holder.tablerow3.setText(temp.getBalanceAmount());

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, POSInvoicePdfViewer.class);
                    intent.putExtra("Link", temp.getFullLink());//test.kitever.com/pdf_invoice/4-Invoice0485.pdf
                    activity.startActivity(intent);
                }
            });
        /*if (temp.getFullLink().length() > 4)
        else
            holder.imageView.setVisibility(View.INVISIBLE);*/
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }
}
