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
import com.kitever.pos.activity.POSTaxAddUpdateScreen;
import com.kitever.pos.fragment.POSTaxMasterScreenFragment;
import com.kitever.pos.model.data.TaxModelData;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by dev on 17/1/17.
 */


public class POSTaxMasterAdapterFragment extends BaseAdapter implements Filterable {

    private POSTaxMasterAdapterFragment.ItemFilter mItemFilter = new POSTaxMasterAdapterFragment.ItemFilter();
    private String selectedItemType;
    private ArrayList<TaxModelData> filteredArrayList = null;
    private ArrayList<TaxModelData> modelList;
    private POSTaxMasterScreenFragment context;
    private String userId, password, userLogin;
    private POSTaxMasterScreenFragment taxMasterInstance;
    private TotalRows rows;

    public POSTaxMasterAdapterFragment(POSTaxMasterScreenFragment taxMasterInstance,
                               ArrayList<TaxModelData> list, String userId, String userLogin,
                               String password) {
        // TODO Auto-generated constructor stub
        this.modelList = list;
        filteredArrayList = list;
        this.taxMasterInstance = taxMasterInstance;
        this.context = taxMasterInstance;
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
        final POSTaxMasterAdapterFragment.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new POSTaxMasterAdapterFragment.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.pos_tax_master_adapter,
                    parent, false);
            viewHolder.taxNameVal = (TextView) convertView
                    .findViewById(R.id.tax_name_val);
            viewHolder.taxVal = (TextView) convertView
                    .findViewById(R.id.tax_val);

            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.tax_adapter_layout);

            viewHolder.page_main_layout = (RelativeLayout) convertView.findViewById(R.id.page_main_layout);
            viewHolder.page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_ENABLED));

            viewHolder.taxNameVal.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            viewHolder.taxVal.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));

            setRobotoThinFont(viewHolder.taxNameVal,context.getActivity());
            setRobotoThinFont(viewHolder.taxVal,context.getActivity());

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (POSTaxMasterAdapterFragment.ViewHolder) convertView.getTag();
        }

        final TaxModelData object = filteredArrayList.get(position);
        viewHolder.taxNameVal.setText(object.getTaxName());
        viewHolder.taxVal.setText(object.getTaxPer());
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getActivity(), POSTaxAddUpdateScreen.class);
                intent.putExtra("screen_name", "Update Tax");
                intent.putExtra("tax_id", filteredArrayList.get(position).getTaxID());
                intent.putExtra("tax_name", filteredArrayList.get(position).getTaxName());
                intent.putExtra("tax_wef_from", filteredArrayList.get(position).getWefDateFrom());
                intent.putExtra("tax_wef_to", filteredArrayList.get(position).getWefDateTill());
                intent.putExtra("tax_per", filteredArrayList.get(position).getTaxPer());
                context.startActivityForResult(intent,1);
            }
        });


	/*	viewHolder.deleteTax.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(context)
						.setCancelable(false)
						.setMessage("Do you want to delete it?")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										Map map = new HashMap<>();
										map.put("Page", "DeleteTax");
										map.put("TaxId", object.getTaxID());
										map.put("userID", userId);
										map.put("UserLogin", userLogin);
										map.put("Password", password);
										taxMasterInstance.deleteTax(map);
										modelList.remove(position);
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
		});
		viewHolder.editTax.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(context, POSTaxAddUpdateScreen.class);
				intent.putExtra("screen_name", "Update Tax");
				intent.putExtra("tax_id", object.getTaxID());
				intent.putExtra("tax_name", object.getTaxName());
				intent.putExtra("tax_wef_from", object.getWefDateFrom());
				intent.putExtra("tax_wef_to", object.getWefDateTill());
				intent.putExtra("tax_per", object.getTaxPer());
				context.startActivity(intent);
			}
		});*/
        return convertView;
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
                            Intent intent = new Intent(context.getActivity(), POSTaxAddUpdateScreen.class);
                            intent.putExtra("screen_name", "Update Tax");
                            intent.putExtra("tax_id", filteredArrayList.get(position).getTaxID());
                            intent.putExtra("tax_name", filteredArrayList.get(position).getTaxName());
                            intent.putExtra("tax_wef_from", filteredArrayList.get(position).getWefDateFrom());
                            intent.putExtra("tax_wef_to", filteredArrayList.get(position).getWefDateTill());
                            intent.putExtra("tax_per", filteredArrayList.get(position).getTaxPer());
                            context.startActivityForResult(intent,1);
                        }
                        else if(which==1)
                        {
                            Map map = new HashMap<>();
                            map.put("Page", "DeleteTax");
                            map.put("TaxId", filteredArrayList.get(position).getTaxID());
                            map.put("userID", userId);
                            map.put("UserLogin", userLogin);
                            map.put("Password", password);
                            taxMasterInstance.deleteTax(map);
                            modelList.remove(position);
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

    private static class ViewHolder {
        TextView taxNameVal, taxVal,  monthFrom, dateFrom, monthTo, dateTo;
        LinearLayout linearLayout;
        RelativeLayout page_main_layout;
        ImageView actionTax;
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

            List<TaxModelData> list = modelList;


            int count = list.size();
            final ArrayList<TaxModelData> nlist = new ArrayList<TaxModelData>(
                    count);

            String filterableString = "";

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getTaxName();
//                if (selectedItemType != null
//                        && selectedItemType.equalsIgnoreCase("Tax Name")) {
//                    filterableString = list.get(i).getTaxName();
//                } else if (selectedItemType != null
//                        && selectedItemType.equalsIgnoreCase("Tax%")) {
//                    filterableString = list.get(i).getTaxPer();
//                } else if (selectedItemType != null
//                        && selectedItemType.equalsIgnoreCase("WefDateFrom")) {
//                    filterableString = list.get(i).getWefDateFrom();
//                } else if (selectedItemType != null
//                        && selectedItemType.equalsIgnoreCase("WefDateTo")) {
//                    filterableString = list.get(i).getWefDateTill();
//                }
//				else {
//					filterableString = list.get(i).getTaxName();
//				}

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
            filteredArrayList = (ArrayList<TaxModelData>) results.values;
            if (filteredArrayList!=null)
                notifyDataSetChanged();

        }
    }

    public void setModelData(ArrayList<TaxModelData> modelList) {
        if (modelList == null) {
            modelList = new ArrayList<TaxModelData>();
        }
        filteredArrayList = modelList;
    }

    public interface Actionable {
        void deleteTax(Map map);
    }

    public void changeList(ArrayList<TaxModelData> tax) {
        // TODO Auto-generated method stub
        filteredArrayList = tax;
        notifyDataSetChanged();
    }
}