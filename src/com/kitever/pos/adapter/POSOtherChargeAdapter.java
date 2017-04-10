package com.kitever.pos.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.pos.activity.POSOtherChargeScreen;
import com.kitever.pos.model.data.OTC;
import com.kitever.utils.TotalRows;

public class POSOtherChargeAdapter extends BaseAdapter implements Filterable {
	private ItemFilter mItemFilter = new ItemFilter();
	private String selectedItemType;
	private ArrayList<OTC> filteredArrayList = null;
	private ArrayList<OTC> modelList;
	private Context context;
	private String userId, password, userLogin;
	private POSOtherChargeScreen otherChargeInstance;
	private TotalRows rows;

	public POSOtherChargeAdapter(POSOtherChargeScreen otherChargeInstance,
			ArrayList<OTC> list) {
		// TODO Auto-generated constructor stub
		this.modelList = list;
		filteredArrayList = this.modelList;
		this.otherChargeInstance = otherChargeInstance;
		this.context = otherChargeInstance;
//		this.userId = userId;
//		this.password = password;
//		this.userLogin = userLogin;
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
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.pos_other_charge_adapter,
					parent, false);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.other_name);
			viewHolder.amount = (TextView) convertView
					.findViewById(R.id.other_amount);			
			/*viewHolder.editTax = (ImageView) convertView
					.findViewById(R.id.edit_img);
			viewHolder.deleteTax = (ImageView) convertView
					.findViewById(R.id.delete_img);*/
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		
		viewHolder.name.setText(filteredArrayList.get(position).getOTC());
		viewHolder.amount.setText(filteredArrayList.get(position).getAmount());
		
	
		return convertView;
	}

	private static class ViewHolder {
		TextView name, amount;
		//ImageView editTax, deleteTax;
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

				if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Name")) {
					filterableString = list.get(i).getOTC();
				} else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Amount")) {
					filterableString = list.get(i).getAmount();
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
		void deleteTax(Map map);
	}

	public void changeList(ArrayList<OTC> otc) {
		// TODO Auto-generated method stub
		filteredArrayList = otc;
		notifyDataSetChanged();
	}
}
