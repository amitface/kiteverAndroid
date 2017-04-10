package com.kitever.pos.fragment.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.pos.activity.PosAddUpdateBrand;
import com.kitever.pos.fragment.POSBrandScreenFragment;
import com.kitever.pos.model.data.BrandModelData;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by dev on 17/1/17.
 */


public class POSBrandAdapterFragment extends BaseAdapter implements Filterable {
    private POSBrandAdapterFragment.ItemFilter mItemFilter = new POSBrandAdapterFragment.ItemFilter();
    private String selectedItemType, userId, userlogin, password;
    private ArrayList<BrandModelData> filteredArrayList = null;
    private ArrayList<BrandModelData> modelList;
    private POSBrandScreenFragment brandInstance;
    private POSBrandScreenFragment context;
    private TotalRows rows;

    public POSBrandAdapterFragment(POSBrandScreenFragment brandInstance,
                                   ArrayList<BrandModelData> list, String userId, String userLogin,
                                   String password) {
        // TODO Auto-generated constructor stub
        this.userId = userId;
        this.userlogin = userLogin;
        this.password = password;
        modelList = list;
        filteredArrayList = list;
        this.brandInstance = brandInstance;
        this.context = brandInstance;
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
        POSBrandAdapterFragment.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new POSBrandAdapterFragment.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.pos_brand_adapter_layout,
                    parent, false);
            viewHolder.brandName = (TextView) convertView
                    .findViewById(R.id.brand_name_val);
            viewHolder.brandAction = (Switch) convertView
                    .findViewById(R.id.brand_action);
//            viewHolder.deleteBrand = (ImageView) convertView
//                    .findViewById(R.id.delete_img);
            viewHolder.layoutBrand = (LinearLayout) convertView.findViewById(R.id.layout_brand);

            viewHolder.brandName.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            setRobotoThinFont(viewHolder.brandName,brandInstance.getActivity());
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (POSBrandAdapterFragment.ViewHolder) convertView.getTag();
        }

        viewHolder.brandAction.setOnCheckedChangeListener(null);
        viewHolder.layoutBrand.setOnClickListener(null);

        final BrandModelData object = filteredArrayList.get(position);
        if (object.getIsActive().equals("I")) {
            //viewHolder.layoutBrand.setBackgroundResource(R.drawable.border_grey);
            viewHolder.layoutBrand.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_DISABLED));
            viewHolder.brandAction.setChecked(false);
        } else {
            //viewHolder.layoutBrand.setBackgroundResource(R.drawable.border_black);
            viewHolder.layoutBrand.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_ENABLED));
            viewHolder.brandAction.setChecked(true);
        }
        viewHolder.brandName.setText(object.getBrandName());
        if (object.getIsActive().equalsIgnoreCase("A"))
            viewHolder.layoutBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getActivity(), PosAddUpdateBrand.class);
                    intent.putExtra("screen_name", "Update Brand");
                    intent.putExtra("brand_name", filteredArrayList.get(position).getBrandName());
                    intent.putExtra("brand_id", filteredArrayList.get(position).getBrandID());
                    context.startActivityForResult(intent, 2);
                }
            });

        viewHolder.brandAction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String s;
                if (filteredArrayList.get(position).getIsActive().equals("I"))
                    s = "Do you want to Activate it?";
                else
                    s = "Do you want to DeActivate it?";
                new AlertDialog.Builder(context.getActivity())
                        .setCancelable(false)
                        .setMessage(s)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                        Map map = new HashMap<>();
                                        map.put("Page", "UpdateBrand");
                                        map.put("BrandID", filteredArrayList.get(position).getBrandID().toString());
                                        map.put("BrandName", filteredArrayList.get(position).getBrandName().toString());
                                     /*   map.put("Page", "DeleteBrand");
                                        map.put("BrandID", filteredArrayList.get(position).getBrandID());*/
                                        if (filteredArrayList.get(position).getIsActive().equals("A"))
                                            map.put("IsActive", "I");//A
                                        else
                                            map.put("IsActive", "A");//A
                                        map.put("userID", userId);
                                        map.put("UserLogin", userlogin);
                                        map.put("Password", password);
                                        brandInstance.deleteBrand(map);
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        notifyDataSetChanged();
                                        dialog.cancel();

                                    }
                                }).show();
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView brandName;
        //        ImageView brandAction;
        Switch brandAction;
        LinearLayout layoutBrand;
    }

    private void showMessage(final int position) {
        String arr[] = {"Edit", "delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
        builder.setTitle("Action")
                .setItems(arr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if (which == 0) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(context.getActivity(), PosAddUpdateBrand.class);
                            intent.putExtra("screen_name", "Update Brand");
                            intent.putExtra("brand_name", filteredArrayList.get(position).getBrandName());
                            intent.putExtra("brand_id", filteredArrayList.get(position).getBrandID());
                            context.startActivityForResult(intent, 1);
                        } else if (which == 1) {
                            String s;
                            if (filteredArrayList.get(position).getIsActive().equals("I"))
                                s = "Do you want to Activate it?";
                            else
                                s = "Do you want to DeActivate it?";
                            new AlertDialog.Builder(context.getActivity())
                                    .setCancelable(false)
                                    .setMessage(s)
                                    .setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    dialog.cancel();
                                                    Map map = new HashMap<>();
                                                    map.put("Page", "UpdateBrand");
                                                    map.put("BrandID", filteredArrayList.get(position).getBrandID().toString());
                                                    map.put("BrandName", filteredArrayList.get(position).getBrandName().toString());

                                                  /*  map.put("Page", "DeleteBrand");
                                                    map.put("BrandID", filteredArrayList.get(position).getBrandID());*/
                                                    if (filteredArrayList.get(position).getIsActive().equals("A"))
                                                        map.put("IsActive", "I");//A
                                                    else
                                                        map.put("IsActive", "A");//A
                                                    map.put("userID", userId);
                                                    map.put("UserLogin", userlogin);
                                                    map.put("Password", password);
                                                    brandInstance.deleteBrand(map);
                                                }
                                            })
                                    .setNegativeButton("No",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    dialog.cancel();

                                                }
                                            }).show();
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

            List<BrandModelData> list = modelList;

            int count = list.size();
            final ArrayList<BrandModelData> nlist = new ArrayList<BrandModelData>(
                    count);

            String filterableString = "";

            for (int i = 0; i < count; i++) {

                if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Brand")) {
                    filterableString = list.get(i).getBrandName();
                } else {
                    filterableString = list.get(i).getBrandName();
                }

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
            filteredArrayList = (ArrayList<BrandModelData>) results.values;
            if (filteredArrayList != null)
                notifyDataSetChanged();

        }

    }

    public void setModelData(ArrayList<BrandModelData> modelList) {
        if (modelList != null)
            this.modelList = modelList;
        else
            this.modelList = new ArrayList<>();

        filteredArrayList = this.modelList;
    }

    public interface Actionable {
        void deleteBrand(Map map);
    }
}
