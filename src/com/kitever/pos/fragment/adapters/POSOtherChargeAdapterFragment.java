package com.kitever.pos.fragment.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.pos.activity.POSOtherChargeAdd;
import com.kitever.pos.fragment.POSOtherChargeScreenFragment;
import com.kitever.pos.model.data.OTC;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by dev on 17/1/17.
 */

public class POSOtherChargeAdapterFragment extends BaseAdapter implements Filterable {
    private POSOtherChargeAdapterFragment.ItemFilter mItemFilter = new POSOtherChargeAdapterFragment.ItemFilter();
    private String selectedItemType;
    private ArrayList<OTC> filteredArrayList = null;
    private ArrayList<OTC> modelList;
    private POSOtherChargeScreenFragment context;
    private String userId, password, userLogin;
    private POSOtherChargeScreenFragment otherChargeInstance;
    private TotalRows rows;

    public POSOtherChargeAdapterFragment(POSOtherChargeScreenFragment otherChargeInstance,
                                 ArrayList<OTC> list,String userId, String userLogin, String password) {
        // TODO Auto-generated constructor stub
        this.modelList = list;
        filteredArrayList = this.modelList;
        this.otherChargeInstance = otherChargeInstance;
        this.context = otherChargeInstance;
		this.userId = userId;
		this.password = password;
		this.userLogin = userLogin;
        rows = (TotalRows) context;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final POSOtherChargeAdapterFragment.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new POSOtherChargeAdapterFragment.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.pos_other_charge_adapter,
                    parent, false);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.other_name);
            viewHolder.amount = (TextView) convertView
                    .findViewById(R.id.other_amount);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.otc_adapter_layout);


            viewHolder.linearLayout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_ENABLED));

            viewHolder.name.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            viewHolder.amount.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));

            setRobotoThinFont(viewHolder.name,context.getActivity());
            setRobotoThinFont(viewHolder.amount,context.getActivity());


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (POSOtherChargeAdapterFragment.ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(filteredArrayList.get(position).getOTC());
        viewHolder.amount.setText(filteredArrayList.get(position).getAmount());
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getActivity(), POSOtherChargeAdd.class);
                intent.putExtra("screen_name", "Update OTC");
                intent.putExtra("OTCId", filteredArrayList.get(position).getOTCID());
                intent.putExtra("Amount", filteredArrayList.get(position).getAmount());
                intent.putExtra("name", filteredArrayList.get(position).getOTC());
                context.startActivityForResult(intent,1);
            }
        });
        /*viewHolder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage(position);
            }
        });*/

        return convertView;
    }

    private static class ViewHolder {
        TextView name, amount;
        ImageView action;
        LinearLayout linearLayout;
        LinearLayout otc_adapter_layout;
    }

    private void showMessage(final int  position) {
        String arr[] = {"Edit","delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
        builder.setTitle("Action")
                .setItems(arr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if(which == 0)
                        {

                        }
                        else if(which==1)
                        {

                        }
                    }
                }).setNegativeButton("Close",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();

                    }
                }).show();
        builder.create();
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return mItemFilter;
    }

    public void setSelectedItemType(String itemType) {
        selectedItemType = itemType;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            List<OTC> list = modelList;

            int count = list.size();
            final ArrayList<OTC> nlist = new ArrayList<OTC>(
                    count);

            String filterableString = "";

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getOTC();
//                if (selectedItemType != null
//                        && selectedItemType.equalsIgnoreCase("Name")) {
//                    filterableString = list.get(i).getOTC();
//                }
//                else if (selectedItemType != null
//                        && selectedItemType.equalsIgnoreCase("Amount")) {
//                    filterableString = list.get(i).getAmount();
//                }

                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }
            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            filteredArrayList = (ArrayList<OTC>) results.values;
            if (filteredArrayList!=null)
                notifyDataSetChanged();
        }
    }

    public void setModelData(ArrayList<OTC> modelList) {
        if (modelList == null) {
            modelList = new ArrayList<OTC>();
        }
        filteredArrayList = modelList;
    }

    public interface Actionable {
        void deleteOTC(Map map);
    }

    public void changeList(ArrayList<OTC> otc) {
        // TODO Auto-generated method stub
        filteredArrayList = otc;
        notifyDataSetChanged();
    }
}
