package com.kitever.pos.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;
import com.kitever.pos.activity.POSCategoryAddOrUpdateScreen;
import com.kitever.pos.activity.POSCategoryScreen;
import com.kitever.pos.model.data.PosCategoryModelData;
import com.kitever.utils.TotalRows;

public class POSCategoryAdapter extends BaseAdapter implements Filterable {

	private ArrayList<PosCategoryModelData> modelList;
	private Context context;
	private ItemFilter mItemFilter = new ItemFilter();
	private ArrayList<String> parentCategoryList;
	private String selectedItemType;
	private ArrayList<PosCategoryModelData> filteredArrayList = null;
	private POSCategoryScreen categoryInstance;
	private String userId, password, userLogin;
	private TotalRows rows;
	MoonIcon mIcon;
	public POSCategoryAdapter(POSCategoryScreen categoryInstance,
			ArrayList<PosCategoryModelData> list, String userId,
			String password, String userLogin) {
		// TODO Auto-generated constructor stub
		this.modelList = list;
		this.userId = userId;
		this.password = password;
		this.userLogin = userLogin;
		filteredArrayList = this.modelList;
		this.categoryInstance = categoryInstance;
		this.context = categoryInstance;
		parentCategoryList = new ArrayList<String>();
		parentCategoryList.add("Select parent category");
		for (int k = 0; k < list.size(); k++) {
			if (list.get(k).getParentCategory() != null
					&& !list.get(k).getParentCategory().equalsIgnoreCase(""))
				parentCategoryList.add(list.get(k).getParentCategory());
		}
		rows = (TotalRows) context;
		mIcon=new MoonIcon(context);

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
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(
					R.layout.pos_category_adapter_layout, parent, false);
			holder.layout=(LinearLayout) convertView
					.findViewById(R.id.layout);
			holder.categoryName = (TextView) convertView
					.findViewById(R.id.cat_name_val);
			holder.parentCategory = (TextView) convertView
					.findViewById(R.id.parent_cat_val);			
			holder.type = (TextView) convertView.findViewById(R.id.type_val);
			

			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final PosCategoryModelData object = filteredArrayList.get(position);
		
		/*if(object.getIsActive().equals("A"))
			{
			holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));
			holder.activateCategory.setTextColor(Color.parseColor("#006400"));
			}
		else {
			holder.layout.setBackgroundColor(Color.parseColor("#DDDDDD"));
			holder.activateCategory.setTextColor(Color.parseColor("#808080"));
			holder.editCategory.setEnabled(false);
			holder.editCategory.setTextColor(Color.parseColor("#808080"));
		}
		holder.categoryName.setText(object.getCategoryName());
		
		if(object.getParentCategory()=="" || object.getParentCategory().length()==0) holder.parentCategory.setText("None");
		else holder.parentCategory.setText(object.getParentCategory());
		holder.type.setText(object.getType());
		// action
		holder.editCategory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						POSCategoryAddOrUpdateScreen.class);
				intent.putExtra("screen_name", "Update Category");
				intent.putExtra("category_name", object.getCategoryName());				
				intent.putExtra("parent_category", object.getParentCategory());
				intent.putExtra("select_type", object.getType());
				intent.putExtra("description", object.getDescription());
				intent.putExtra("category_id", object.getCategoryID());
				intent.putExtra("is_active", object.getIsActive());
				intent.putStringArrayListExtra("parent_category_list",
						parentCategoryList);
				context.startActivity(intent);
			}
		});
		
		holder.activateCategory.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {

				final String catStatus = object.getIsActive();
				final String isActive = object.getIsActive().equals("A") ? "I" : "A";
				final String isActiveString = object.getIsActive().equals("A") ? "Dea" : "A";

				String Activemsg="All the categories and products under "+object.getCategoryName() +" category also be deactivated. Do you want to deactivate it ?";
				String Inactivemsg="Do You Want to Activate "+object.getCategoryName();
				String msg=object.getIsActive().equals("A") ? Activemsg: Inactivemsg;

					new AlertDialog.Builder(context)
							.setCancelable(false)
							.setMessage(msg)
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
															int which) {
											dialog.cancel();

											object.setIsActive(isActive);
											Toast.makeText(categoryInstance, "Category " + isActiveString + "ctivated", Toast.LENGTH_SHORT).show();
											Map map = new HashMap<>();
											map.put("Page", "SetStatus");
											map.put("id", object.getCategoryID());
											map.put("Status", catStatus);
											map.put("PageType", "Category");
											map.put("UserID", userId);
											map.put("UserLogin", userLogin);
											map.put("Password", password);
											categoryInstance.activateCategory(map);
										}
									}).setNegativeButton("No", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
											int which) {
							dialog.cancel();

						}
					}).show();

			  }
			});

				

		
		holder.deleteCategory.setOnClickListener(new OnClickListener() {

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
										map.put("Page", "DeleteCategory");
										map.put("CategoryID",
												object.getCategoryID());
										map.put("IsActive", object.getIsActive());
										map.put("userID", userId);
										map.put("UserLogin", userLogin);
										map.put("Password", password);
										categoryInstance.deleteCategory(map);
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
		});*/
		return convertView;
	}

	private static class ViewHolder {
		TextView categoryName, parentCategory,type,editCategory,deleteCategory,activateCategory;
		LinearLayout layout;
	}

	public void setSelectedItemType(String itemType) {
		selectedItemType = itemType;
	}
	
	public void setSelectedActiveInactive(String itemType) {
		selectedItemType = itemType;
	}
	
	

	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			String filterString = constraint.toString().toLowerCase();

			FilterResults results = new FilterResults();

			List<PosCategoryModelData> list = modelList;

			int count = list.size();
			
			if(filterString.length()==0 || selectedItemType.equals("Select") || selectedItemType.equals(""))
			{
				results.values = list;
				results.count = list.size();
				return results;
			}
			final ArrayList<PosCategoryModelData> nlist = new ArrayList<PosCategoryModelData>(
					count);
			String filterableString = "";

			for (int i = 0; i < count; i++) {

				if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Category")) {
					filterableString = list.get(i).getCategoryName();
				} else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Parent")) {
					if(list.get(i).getParentCategory().equals(""))	filterableString = "None";
					else filterableString = list.get(i).getParentCategory();
				} else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Type")) {
					filterableString = list.get(i).getType();
				}	else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Active")) {
					filterableString = "A";
				}	else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Inactive")) {
					filterableString = list.get(i).getIsActive();
				}			
				
//				else {
//					filterableString = list.get(i).getCategoryName();
//				}

				Log.i("filterableString","filterable - "+filterableString);
				Log.i("filterString","filterString - "+filterString);
				
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
			filteredArrayList = (ArrayList<PosCategoryModelData>) results.values;
			if (filteredArrayList !=null)
				notifyDataSetChanged();
			
		}

	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return mItemFilter;
	}

	public interface Actionable {
		void deleteCategory(Map map);
		void activateCategory(Map map);
	}

	public void setModelData(ArrayList<PosCategoryModelData> modelList) {
		if(modelList==null){
			modelList=new ArrayList<PosCategoryModelData>();
		}
		filteredArrayList = modelList;
	}

}
