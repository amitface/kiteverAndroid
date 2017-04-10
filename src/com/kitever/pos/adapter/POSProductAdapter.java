package com.kitever.pos.adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CircleImageView;
import com.kitever.app.context.MoonIcon;
import com.kitever.pos.activity.POSAddUpdateProduct;
import com.kitever.pos.activity.POSProductsScreen;
import com.kitever.pos.model.data.ProductModelData;
import com.kitever.utils.TotalRows;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POSProductAdapter extends BaseAdapter implements Filterable {
	private ArrayList<ProductModelData> modelList;
	private Context context;
	private ItemFilter mItemFilter = new ItemFilter();
	private String selectedItemType;
	private ArrayList<ProductModelData> filteredArrayList = null;
	private String userId, password, userLogin;
	private POSProductsScreen productsScreen;
	MoonIcon mIcon;
	private TotalRows rows;
	
	public POSProductAdapter(POSProductsScreen productsScreen,
			ArrayList<ProductModelData> list, String userId,
			String password, String userLogin) {
		// TODO Auto-generated constructor stub
		this.userId = userId;
		this.password = password;
		this.userLogin = userLogin;
		filteredArrayList = list;
		modelList = list;
		this.productsScreen=productsScreen;
		this.context = productsScreen;
		mIcon=new MoonIcon(context);
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
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.pos_product_adapter_layout,
					parent, false);
			holder.layout=(LinearLayout) convertView
					.findViewById(R.id.layout);			
			holder.productImage = (CircleImageView) convertView
					.findViewById(R.id.product_image);
			holder.productName = (TextView) convertView
					.findViewById(R.id.product_name);
			holder.categoryName = (TextView) convertView
					.findViewById(R.id.product_category);
			holder.brandName = (TextView) convertView
					.findViewById(R.id.product_brand);
			holder.priceName = (TextView) convertView
					.findViewById(R.id.product_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ProductModelData object = filteredArrayList.get(position);

		String imagestr=object.getProductImage();
		if(imagestr!=null && imagestr.length()>0)
		{
		String image_url= "http://nowconnect.in/" + userId+"/" + object.getProductImage();
	//	Log.i("image_url",image_url);
		Picasso.with(context)
		   .load(image_url)
		   .placeholder(R.drawable.productimg)
		   .error(R.drawable.ic_launcher)
		   .into(holder.productImage);
		}

			/*if(object.getIsActive().equals("A"))
			{
			holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));
			holder.activateProduct.setTextColor(Color.parseColor("#006400"));
			}
			else
			{
			holder.layout.setBackgroundColor(Color.parseColor("#DDDDDD"));
			holder.activateProduct.setTextColor(Color.parseColor("#808080"));
			}

		holder.productName.setText(object.getProductName());
		holder.categoryName.setText(object.getCategoryName());
		holder.brandName.setText(object.getBrandName());
		holder.priceName.setText(object.getPriceWithTax());
		holder.editProduct.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, POSAddUpdateProduct.class);
				intent.putExtra("screen_name", "Update Product");
				intent.putExtra("product_name", object.getProductName());
				intent.putExtra("product_id", object.getProductID());
				intent.putExtra("product_img", object.getProductImage());
				intent.putExtra("product_category", object.getCategoryName());
				intent.putExtra("product_brand", object.getBrandName());
				intent.putExtra("product_units", object.getUnits());
				intent.putExtra("product_price", object.getPerUnitPrice());
				intent.putExtra("price_withtax", object.getPriceWithTax());
				intent.putExtra("product_description", object.getDescription());
				intent.putExtra("taxApplied", object.getTaxApplied());
				intent.putExtra("is_active", object.getIsActive());
				context.startActivity(intent);
			}
		});

		holder.activateProduct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String productStatus=object.getIsActive();
				String isActive=object.getIsActive().equals("A")?"I":"A";
				final String isActiveString = object.getIsActive().equals("A") ? "Dea" : "A";
				Toast.makeText(productsScreen, "Product " + isActiveString + "ctivated", Toast.LENGTH_SHORT).show();
				object.setIsActive(isActive);
				Map map=new HashMap<>();
				map.put("Page", "SetStatus");
				map.put("id", object.getProductID());
				map.put("Status", productStatus);
				map.put("PageType", "Product");
				map.put("UserID", userId);
				map.put("UserLogin", userLogin);
				map.put("Password", password);
				productsScreen.activateProduct(map);

			}
		});


		holder.deleteProduct.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context)
				.setCancelable(false)
				.setMessage("Do you want to delete it?")
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
				Map map=new HashMap<>();
				map.put("Page", "DeleteProduct");
				map.put("ProductId", object.getProductID());
				map.put("IsActive", object.getIsActive());
				map.put("UserID", userId);
				map.put("UserLogin", userLogin);
				map.put("Password", password);
				productsScreen.deleteProduct(map);
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
		CircleImageView productImage; 
		//ImageView  editProduct,deleteProduct;
		TextView productName, categoryName,brandName, priceName,editProduct,deleteProduct,activateProduct;
		LinearLayout layout;
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

			List<ProductModelData> list = modelList;

			if(selectedItemType==null ||filterString.length()==0 || selectedItemType.equals("Default") || selectedItemType.equals(""))
			{
				results.values = list;
				results.count = list.size();
				return results;
			}
			int count = list.size();
			final ArrayList<ProductModelData> nlist = new ArrayList<ProductModelData>(
					count);

			String filterableString = "";

			for (int i = 0; i < count; i++) {

				if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Product")) {
					filterableString = list.get(i).getProductName();
					if (filterableString.toLowerCase().contains(filterString)) nlist.add(list.get(i));
				} else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Brand")) {
					filterableString = list.get(i).getBrandName();
					if (filterableString.toLowerCase().contains(filterString)) nlist.add(list.get(i));
				} else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Price >=")) {
					filterableString = list.get(i).getPerUnitPrice();

					Log.i("filterableInt","A-"+filterableString+" B -"+filterString);
					try {
						Double filterableInt = Double.parseDouble(filterableString.trim());
						Double filterInt = Double.parseDouble(filterString.trim());
						Log.i("filterableInt", "AInt -" + filterableInt + " BInt -" + filterInt);
						if(filterableInt >= filterInt)
						{
							Log.i("filter","filter is greater");
							nlist.add(list.get(i));
						}
					} catch(Exception e){e.printStackTrace();}
				}

				else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Price <=")) {
					filterableString = list.get(i).getPerUnitPrice();

					Log.i("filterableInt","A-"+filterableString+" B -"+filterString);
					try {
						Double filterableInt = Double.parseDouble(filterableString.trim());
						Double filterInt = Double.parseDouble(filterString.trim());
						Log.i("filterableInt", "AInt -" + filterableInt + " BInt -" + filterInt);
						if(filterableInt <= filterInt)
						{

							nlist.add(list.get(i));
						}
					} catch(Exception e){e.printStackTrace();}
				}



			}

			results.values = nlist;
			results.count = nlist.size();

			return results;
		}

		
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredArrayList = (ArrayList<ProductModelData>) results.values;			
			notifyDataSetChanged();
			productsScreen.CheckNoResult(filteredArrayList.size());
			//if (filteredArrayList.size() > 0)	notifyDataSetChanged();

		}

	}

	public void setModelData(ArrayList<ProductModelData> modelList) {
		if (modelList == null) {
			modelList = new ArrayList<ProductModelData>();
		}
		filteredArrayList = modelList;
	}

	public interface Actionable {
		void deleteProduct(Map map);
		void activateProduct(Map map,String str);
		void CheckNoResult(int size);
	}
}
