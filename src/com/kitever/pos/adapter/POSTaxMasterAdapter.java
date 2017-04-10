package com.kitever.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.pos.activity.POSTaxMasterScreen;
import com.kitever.pos.model.data.TaxModelData;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class POSTaxMasterAdapter extends BaseAdapter implements Filterable {
	private ItemFilter mItemFilter = new ItemFilter();
	private String selectedItemType;
	private ArrayList<TaxModelData> filteredArrayList = null;
	private ArrayList<TaxModelData> modelList;
	private Context context;
	private String userId, password, userLogin;
	private POSTaxMasterScreen taxMasterInstance;
	private TotalRows rows;

	public POSTaxMasterAdapter(POSTaxMasterScreen taxMasterInstance,
			ArrayList<TaxModelData> list, String userId, String userLogin,
			String password) {
		// TODO Auto-generated constructor stub
		this.modelList = list;
		filteredArrayList = this.modelList;
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
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.pos_tax_master_adapter,
					parent, false);
			viewHolder.taxNameVal = (TextView) convertView
					.findViewById(R.id.tax_name_val);
			viewHolder.taxVal = (TextView) convertView
					.findViewById(R.id.tax_val);
//			viewHolder.monthFrom = (TextView) convertView
//					.findViewById(R.id.from_month);
//			viewHolder.dateFrom = (TextView) convertView
//					.findViewById(R.id.from_date);
//			viewHolder.monthTo = (TextView) convertView
//					.findViewById(R.id.to_month);
//			viewHolder.dateTo = (TextView) convertView
//					.findViewById(R.id.to_date);
			/*viewHolder.editTax = (ImageView) convertView
					.findViewById(R.id.edit_img);
			viewHolder.deleteTax = (ImageView) convertView
					.findViewById(R.id.delete_img);*/
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final TaxModelData object = filteredArrayList.get(position);
		viewHolder.taxNameVal.setText(object.getTaxName());
		viewHolder.taxVal.setText(object.getTaxPer());
//		viewHolder.monthFrom.setText(object.getWefDateFrom());
//		viewHolder.dateFrom.setText(object.getWefDateTill());
//
//
//		String pattern = "MM/dd/yyyy";
//		String pattern1 = "d-MMM ,yy";
//
//		String s[] = new String[2];
//		SimpleDateFormat format = new SimpleDateFormat(pattern);
//		try {
//			Date date =  new Date(Long.parseLong(object.getWefDateFromInMilliSecond()));
//			format = new SimpleDateFormat(pattern1);
//			s=format.format(date).split("-");
//		}catch(Exception c)
//		{
//			System.out.println("Date erroer"+c.getMessage());
//		}
//
//		viewHolder.dateFrom.setText(s[0]);
//		viewHolder.monthFrom.setText(s[1]);
//
//		try {
//			Date date =  new Date(Long.parseLong(object.getWefDateTillInMilliSecond()));
//			format = new SimpleDateFormat(pattern1);
//			s=format.format(date).split("-");
//		}catch(Exception c)
//		{
//			System.out.println("Date erroer"+c.getMessage());
//		}
//
//
//		viewHolder.dateTo.setText(s[0]);
//		viewHolder.monthTo.setText(s[1]);

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

	private static class ViewHolder {
		TextView taxNameVal, taxVal, monthFrom, dateFrom, monthTo, dateTo;
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

			List<TaxModelData> list = modelList;

			
			int count = list.size();
			final ArrayList<TaxModelData> nlist = new ArrayList<TaxModelData>(
					count);

			String filterableString = "";

			for (int i = 0; i < count; i++) {

				if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Tax Name")) {
					filterableString = list.get(i).getTaxName();
				} else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Tax%")) {
					filterableString = list.get(i).getTaxPer();
				} else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("WefDateFrom")) {
					filterableString = list.get(i).getWefDateFrom();
				} else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("WefDateTo")) {
					filterableString = list.get(i).getWefDateTill();
				} 
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
}
