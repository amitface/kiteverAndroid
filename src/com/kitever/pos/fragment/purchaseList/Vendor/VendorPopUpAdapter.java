package com.kitever.pos.fragment.purchaseList.Vendor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitever.android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by android on 10/3/17.
 */

public class VendorPopUpAdapter extends RecyclerView.Adapter<VendorPopUpAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<GetPurchaseDetailAsVendor> getPurchaseDetailAsVendors;

    public VendorPopUpAdapter(Context context, ArrayList<GetPurchaseDetailAsVendor> getPurchaseDetailAsVendors) {
        this.context = context;
        this.getPurchaseDetailAsVendors = getPurchaseDetailAsVendors;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvVendorPopUpDate,tvVendorPopUpPoNum,tvVendorPopUpInvoice,
                tablerowVendorPopUp1,tablerowVendorPopUp2,tablerowVendorPopUp3;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvVendorPopUpDate = (TextView) itemView.findViewById(R.id.tvVendorPopUpDate);
            tvVendorPopUpPoNum = (TextView) itemView.findViewById(R.id.tvVendorPopUpPoNum);
            tvVendorPopUpInvoice = (TextView) itemView.findViewById(R.id.tvVendorPopUpInvoice);
            tablerowVendorPopUp1 = (TextView) itemView.findViewById(R.id.tablerowVendorPopUp1);
            tablerowVendorPopUp2 = (TextView) itemView.findViewById(R.id.tablerowVendorPopUp2);
            tablerowVendorPopUp3 = (TextView) itemView.findViewById(R.id.tablerowVendorPopUp3);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vendor_popup_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GetPurchaseDetailAsVendor temp = getPurchaseDetailAsVendors.get(position);

        String pattern1 = "dd-MMM, yy";
        String s ="";
        if (temp.getPurchaseDate() != null && !temp.getPurchaseDate().equals("")) {
            SimpleDateFormat format;
            try {
                Date date = new Date((long)Double.parseDouble(temp.getPurchaseDate()));
                format = new SimpleDateFormat(pattern1);
                s = format.format(date);
            } catch (Exception c) {

            }
        }

        holder.tvVendorPopUpDate.setText(s);

        holder.tvVendorPopUpPoNum.setText(temp.getPONO());
        holder.tvVendorPopUpInvoice.setText(temp.getInvoiceNO());
        holder.tablerowVendorPopUp1.setText(String.valueOf(temp.getBillAmount()));
        holder.tablerowVendorPopUp2.setText(String.valueOf(temp.getPaidAmount()));
        holder.tablerowVendorPopUp3.setText(String.valueOf(temp.getBalance()));
    }

    @Override
    public int getItemCount() {
        return getPurchaseDetailAsVendors.size();
    }

}
