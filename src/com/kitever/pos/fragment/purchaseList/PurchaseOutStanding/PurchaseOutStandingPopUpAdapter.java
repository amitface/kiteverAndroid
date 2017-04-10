package com.kitever.pos.fragment.purchaseList.PurchaseOutStanding;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by android on 10/3/17.
 */

public class PurchaseOutStandingPopUpAdapter extends RecyclerView.Adapter<PurchaseOutStandingPopUpAdapter.MyViewHolder>{

    private Context context;

    public  class  MyViewHolder extends RecyclerView.ViewHolder
    {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
