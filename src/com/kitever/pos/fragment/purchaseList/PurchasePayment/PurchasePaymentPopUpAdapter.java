package com.kitever.pos.fragment.purchaseList.PurchasePayment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitever.android.R;

import java.util.ArrayList;

/**
 * Created by android on 10/3/17.
 */

public class PurchasePaymentPopUpAdapter extends RecyclerView.Adapter<PurchasePaymentPopUpAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<GetPaymentDetailsPopup> getPaymentDetailsPopups;

    public PurchasePaymentPopUpAdapter(Context context, ArrayList<GetPaymentDetailsPopup> getPaymentDetailsPopups) {
        this.context = context;
        this.getPaymentDetailsPopups = getPaymentDetailsPopups;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.purchase_payment_popup_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return getPaymentDetailsPopups.size();
    }
}
