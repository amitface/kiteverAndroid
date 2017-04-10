package com.kitever.pos.fragment.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;

/**
 * Created by android on 20/3/17.
 */

public class POSHomeTabbedAdapter  extends RecyclerView.Adapter<POSHomeTabbedAdapter.MyViewHolder>{

    private MoonIcon moonIcon;
    private int res1[] = {R.string.homeicon, R.string.dashboardicon, R.string.category_icon, R.string.brand_icon, R.string.producticon
            , R.string.customer_icon, R.string.ordericon, R.string.paymenticon, R.string.crediticon,
            R.string.invoice_icon, R.string.tax_icon, R.string.other_charges_icon,
            R.string.setting_icon, R.string.producticon,R.string.producticon,R.string.producticon,R.string.producticon,R.string.producticon};

    private String str[] = {"Home", "Dashboard", "Categories", "Brands", "Products", "Customers", "Orders", "Payments", "Credit",
            "Invoice", "Taxes", "Charges", "Settings", "Purchase","Outstanding","PPayments","Vendor","Inventory"};

    public POSHomeTabbedAdapter(MoonIcon moonIcon) {
        this.moonIcon = moonIcon;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvPosHomeText, tvPosHomeIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvPosHomeText = (TextView) itemView.findViewById(R.id.tvPosHomeText);
            tvPosHomeIcon = (TextView) itemView.findViewById(R.id.tvPosHomeIcon);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pos_home_tabbed,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tvPosHomeIcon.setText(res1[position]);
            holder.tvPosHomeText.setText(str[position]);
            moonIcon.setTextfont(holder.tvPosHomeIcon);
    }

    @Override
    public int getItemCount() {
        return res1.length;
    }

}
