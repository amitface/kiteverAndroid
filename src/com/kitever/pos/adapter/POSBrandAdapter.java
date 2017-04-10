package com.kitever.pos.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.pos.activity.POSBrandScreen;
import com.kitever.pos.activity.PosAddUpdateBrand;
import com.kitever.pos.model.data.BrandModelData;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POSBrandAdapter extends BaseAdapter implements Filterable {
	private ItemFilter mItemFilter = new ItemFilter();
	private String selectedItemType, userId, userlogin, password;
	private ArrayList<BrandModelData> filteredArrayList = null;
	private ArrayList<BrandModelData> modelList;
	private POSBrandScreen brandInstance;
	private Context context;
	private TotalRows rows;
	public POSBrandAdapter(POSBrandScreen brandInstance,
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.pos_brand_adapter_layout,
					parent, false);
			viewHolder.brandName = (TextView) convertView
					.findViewById(R.id.brand_name_val);
//			viewHolder.editBrand = (ImageView) convertView
//					.findViewById(R.id.edit_img);
//			viewHolder.deleteBrand = (ImageView) convertView
//					.findViewById(R.id.delete_img);
			viewHolder.layoutBrand = (LinearLayout) convertView.findViewById(R.id.layout_brand);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final BrandModelData object = filteredArrayList.get(position);
		if(object.getIsActive().equals("I"))
			viewHolder.layoutBrand.setBackgroundColor(Color.parseColor("#DDDDDD")) ;
		else 
			viewHolder.layoutBrand.setBackgroundColor(Color.parseColor("#ffffff"));
		viewHolder.brandName.setText(object.getBrandName());	
		
		viewHolder.editBrand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, PosAddUpdateBrand.class);
				intent.putExtra("screen_name", "Update Brand");
				intent.putExtra("brand_name", object.getBrandName());
				intent.putExtra("brand_id", object.getBrandID());
				context.startActivity(intent);
			}
		});

		viewHolder.deleteBrand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s;
				if(object.getIsActive().equals("I"))
					s="Do you want to Activate it?";
				else 
					s="Do you want to DeActivate it?";
				new AlertDialog.Builder(context)
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
										map.put("BrandID", object.getBrandID().toString());
										map.put("BrandName", object.getBrandName().toString());

//										map.put("Page", "DeleteBrand");
										map.put("BrandID", object.getBrandID());
										if(object.getIsActive().equals("A"))
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
		});
		return convertView;
	}

	private static class ViewHolder {
		TextView brandName;
		ImageView editBrand, deleteBrand;
		LinearLayout layoutBrand;
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
			if (filteredArrayList!= null)
				notifyDataSetChanged();

		}

	}

	public void setModelData(ArrayList<BrandModelData> modelList) {
		if(modelList!=null)
			this.modelList=modelList;
		else 
			this.modelList= new ArrayList<>();
			
		filteredArrayList = this.modelList;
	}

//	public interface Actionable {
//		void deleteBrand(Map map);
////		void deactivateBrand();
//	}
}
