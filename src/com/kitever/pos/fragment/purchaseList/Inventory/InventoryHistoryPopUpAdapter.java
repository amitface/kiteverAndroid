package com.kitever.pos.fragment.purchaseList.Inventory;

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
import java.util.TimeZone;

/**
 * Created by android on 10/3/17.
 */

public class InventoryHistoryPopUpAdapter extends RecyclerView.Adapter<InventoryHistoryPopUpAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<GetInventoryTransactionHistory> getInventoryTransactionHistories;

    public InventoryHistoryPopUpAdapter(Context context, ArrayList<GetInventoryTransactionHistory> getInventoryTransactionHistories) {
        this.context = context;
        this.getInventoryTransactionHistories = getInventoryTransactionHistories;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvInventoryHistoryDate, tvInventoryHistoryMonth, tvInventoryHistoryAdded,
                tvInventoryHistoryBalance, tvInventoryHistoryType, tvInventoryHistoryRemarks;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvInventoryHistoryDate = (TextView) itemView.findViewById(R.id.tvInventoryHistoryDate);
            tvInventoryHistoryMonth = (TextView) itemView.findViewById(R.id.tvInventoryHistoryMonth);
            tvInventoryHistoryAdded = (TextView) itemView.findViewById(R.id.tvInventoryHistoryAdded);
            tvInventoryHistoryBalance = (TextView) itemView.findViewById(R.id.tvInventoryHistoryBalance);
            tvInventoryHistoryType = (TextView) itemView.findViewById(R.id.tvInventoryHistoryType);
            tvInventoryHistoryRemarks = (TextView) itemView.findViewById(R.id.tvInventoryHistoryRemarks);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_history_popup_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GetInventoryTransactionHistory temp = getInventoryTransactionHistories.get(position);

        String s[] = new String[2];
        String pattern = "dd-MMM, yy";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = new Date((long)Double.parseDouble(temp.getDateAdded()));
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            s = simpleDateFormat.format(date).split("-");
        } catch (Exception e) {

        }

        holder.tvInventoryHistoryDate.setText(s[0]);
        holder.tvInventoryHistoryMonth.setText(s[1]);
        holder.tvInventoryHistoryAdded.setText(String.valueOf(temp.getAdded()));
        holder.tvInventoryHistoryBalance.setText(String.valueOf(temp.getBalance()));
        holder.tvInventoryHistoryType.setText(String.valueOf(temp.getType()));
        holder.tvInventoryHistoryRemarks.setText(String.valueOf(temp.getRemarks()));
    }

    @Override
    public int getItemCount() {
        return getInventoryTransactionHistories.size();
    }

}
