package com.kitever.pos.fragment.purchaseList.Inventory;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;

import java.util.ArrayList;
import java.util.List;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by android on 10/3/17.
 */

public class InventoryListAdapter extends RecyclerView.Adapter<InventoryListAdapter.MyViewHolder>  implements Filterable {
    private Context context;
    private ArrayList<GetCurrentInventory> getCurrentInventories;
    private ArrayList<GetCurrentInventory> finalCurrentInventories;
    private Action action;
    private ItemFilter itemFilter;
    private MoonIcon moonIcon;
    private int selectedItemPosition;

    public InventoryListAdapter(Context context, ArrayList<GetCurrentInventory> getCurrentInventories, Fragment fragmentContext) {
        this.context = context;
        this.getCurrentInventories = getCurrentInventories;
        this.finalCurrentInventories = getCurrentInventories;
        action = (Action)fragmentContext;
        itemFilter = new ItemFilter();
        moonIcon = new MoonIcon(fragmentContext);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvInventoryListName, tvInventoryListStock, tvAddImage;
        FrameLayout tvInventoryListAdd, tvInventoryListHistory;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvInventoryListName = (TextView) itemView.findViewById(R.id.tvInventoryListName);
            tvInventoryListStock = (TextView) itemView.findViewById(R.id.tvInventoryListStock);
            tvAddImage = (TextView) itemView.findViewById(R.id.tvAddImage);
            tvInventoryListAdd = (FrameLayout) itemView.findViewById(R.id.tvInventoryListAdd);
            tvInventoryListHistory = (FrameLayout) itemView.findViewById(R.id.tvInventoryListHistory);

            tvInventoryListName.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            tvInventoryListStock.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            setRobotoThinFont(tvInventoryListName,context);
            setRobotoThinFont(tvInventoryListStock,context);
            moonIcon.setTextfont(tvAddImage);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inventory_list_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
            GetCurrentInventory temp = getCurrentInventories.get(position);
        holder.tvInventoryListName.setText(String.valueOf(temp.getmProductName()));
        holder.tvInventoryListStock.setText(String.valueOf(temp.getBalance()));
        holder.tvInventoryListAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show();
                action.showAddPopup(position,getCurrentInventories.get(position));
            }
        });
        holder.tvInventoryListHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(context,"clicked History",Toast.LENGTH_SHORT).show();
                action.showHistoryPopup(String.valueOf(getCurrentInventories.get(position).getmProductName()),String.valueOf(getCurrentInventories.get(position).getBalance()), String.valueOf(getCurrentInventories.get(position).getProductid()));
            }
        });
    }

    @Override
    public int getItemCount() {
        action.updateRecordCount(getCurrentInventories.size());
        return getCurrentInventories.size();
    }

    public void update(int position, GetCurrentInventory getCurrentInventory)
    {
        getCurrentInventories.get(position).setBalance(getCurrentInventory.getBalance());
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    public void setSelectedItemPosition(int position)
    {
        selectedItemPosition = position;
    }

    public ArrayList<GetCurrentInventory> getCurrentInventory()
    {
        return getCurrentInventories;
    }

    private boolean greaterRange(Double value, Double orignal) {
        return orignal > value;
    }

    private boolean lesserRange(Double value, Double orignal) {
        return orignal < value;
    }

    public class ItemFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterable = constraint.toString().toLowerCase();
            if(filterable.length()==0)
            {
                results.values = finalCurrentInventories;
                results.count = finalCurrentInventories.size();
                return results;
            }

            int count = finalCurrentInventories.size();
            List<GetCurrentInventory> searchList = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                if (selectedItemPosition == 1) {
                    if (finalCurrentInventories.get(i).getmProductName().toLowerCase().contains(filterable))
                        searchList.add(finalCurrentInventories.get(i));
                } else if(selectedItemPosition == 2)
                {
                    if(lesserRange(Double.parseDouble(finalCurrentInventories.get(i).getBalance().toString()),Double.parseDouble(filterable)))
                    {
                        searchList.add(finalCurrentInventories.get(i));
                    }
                } else if(selectedItemPosition == 3)
                {
                    if(greaterRange(Double.parseDouble(finalCurrentInventories.get(i).getBalance().toString()),Double.parseDouble(filterable)))
                    {
                        searchList.add(finalCurrentInventories.get(i));
                    }
                }
            }

            results.values = searchList;
            results.count = searchList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            getCurrentInventories = (ArrayList<GetCurrentInventory>) results.values;
            if(getCurrentInventories!=null)
                notifyDataSetChanged();
        }
    }

    public interface Action{
        void showHistoryPopup(String ProductName, String Quantity, String ProductID);
        void showAddPopup(int position, GetCurrentInventory getInventory);
        void updateRecordCount(int count);
    }

    public void updateInventory(int position, AddInventory temp)
    {
        getCurrentInventories.get(position).setBalance(Long.parseLong(temp.getBalance()));
        for(int i =0;i<finalCurrentInventories.size();i++)
        {
            if(finalCurrentInventories.get(i).getProductid()==temp.getProductid())
            {
                finalCurrentInventories.get(i).setBalance(Long.parseLong(temp.getBalance()));
                break;
            }
        }
        notifyItemChanged(position);
    }
}
