package com.kitever.pos.fragment.purchaseList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitever.android.R;

import java.util.ArrayList;

/**
 * Created by android on 10/3/17.
 */

public class PurchaseListPopUpAdapter extends RecyclerView.Adapter<PurchaseListPopUpAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<GetPurchaseDetail> getPurchaseDetails;

    public PurchaseListPopUpAdapter(Context context, ArrayList<GetPurchaseDetail> getPurchaseDetails) {
        this.context = context;
        this.getPurchaseDetails = getPurchaseDetails;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvPurchasePopUpName,tvPurchasePopUpQuantity,tablerowPurchasePopUp1,
                    tablerowPurchasePopUp2,tablerowPurchasePopUp3;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvPurchasePopUpName = (TextView) itemView.findViewById(R.id.tvPurchasePopUpName);
            tvPurchasePopUpQuantity = (TextView) itemView.findViewById(R.id.tvPurchasePopUpQuantity);
            tablerowPurchasePopUp1 = (TextView) itemView.findViewById(R.id.tablerowPurchasePopUp1);
//            tablerowPurchasePopUp2 = (TextView) itemView.findViewById(R.id.tablerowPurchasePopUp2);
//            tablerowPurchasePopUp3 = (TextView) itemView.findViewById(R.id.tablerowPurchasePopUp3);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.purchase_list_popup_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GetPurchaseDetail temp = getPurchaseDetails.get(position);

        holder.tvPurchasePopUpName.setText(temp.getProductName());
        holder.tvPurchasePopUpQuantity.setText(String.valueOf(temp.getQuantity()));
        holder.tablerowPurchasePopUp1.setText(String.valueOf(temp.getBillAmount()));
        /*holder.tablerowPurchasePopUp2.setText(temp.getProductName());
        holder.tablerowPurchasePopUp3.setText(temp.getProductName());*/
    }

    @Override
    public int getItemCount() {
        return getPurchaseDetails.size();
    }
}
