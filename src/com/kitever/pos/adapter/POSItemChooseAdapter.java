package com.kitever.pos.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CircleImageView;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.pos.activity.POSItemChooseScreen;
import com.kitever.pos.model.data.FetchProductModelData;
import com.kitever.pos.model.data.FetchSelectedProductModelData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sms19.inapp.msg.constant.Utils;

import static sms19.inapp.msg.constant.Utils.imagePopup;
import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class POSItemChooseAdapter extends BaseAdapter implements Filterable {

	private ArrayList<FetchProductModelData> fetchProductList;
	private ArrayList<FetchSelectedProductModelData> fetchSelectedProductList;
	private boolean[] itemPosState;
	private ArrayList<Integer> selectedPos;
	private ItemFilter mItemFilter = new ItemFilter();
	private String selectedItemType;
	private ArrayList<FetchProductModelData> filteredArrayList = null;
	private POSItemChooseScreen positemchooseScreen;
	private ArrayList<String> productList;
	private HashMap<String, FetchSelectedProductModelData> hmap;
	private String[] arrTemp;
	private MoonIcon mIcon;
	boolean limitstatus = false;

	public POSItemChooseAdapter(POSItemChooseScreen positemchooseScreen,
			ArrayList<FetchProductModelData> fetchProductList) {
		// TODO Auto-generated constructor stub
		this.fetchProductList = fetchProductList;
		filteredArrayList = fetchProductList;
		this.positemchooseScreen = positemchooseScreen;
		itemPosState = new boolean[fetchProductList.size()];
		selectedPos = new ArrayList<Integer>();
		hmap = new HashMap<String, FetchSelectedProductModelData>();
		productList = new ArrayList<String>();
		mIcon = new MoonIcon(positemchooseScreen);
		arrTemp = new String[fetchProductList.size()];
	}

	public POSItemChooseAdapter(POSItemChooseScreen positemchooseScreen,
			ArrayList<FetchProductModelData> fetchProductList,
			HashMap<String, FetchSelectedProductModelData> hmap,
			ArrayList<String> productList) {
		// TODO Auto-generated constructor stub
		this.fetchProductList = fetchProductList;
		filteredArrayList = fetchProductList;
		this.positemchooseScreen = positemchooseScreen;
		itemPosState = new boolean[fetchProductList.size()];
		selectedPos = new ArrayList<Integer>();
		this.hmap = hmap;
		this.productList = productList;
		mIcon = new MoonIcon(positemchooseScreen);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(
					R.layout.pos_item_choose_adapter_layout, parent, false);


			holder.adapter_layout = (LinearLayout) convertView.findViewById(R.id.adapter_layout);

			holder.productimage = (CircleImageView) convertView
					.findViewById(R.id.product_image);
			holder.product = (TextView) convertView.findViewById(R.id.item_val);
			holder.category = (TextView) convertView
					.findViewById(R.id.item_category);
			holder.brand = (TextView) convertView.findViewById(R.id.item_brand);
			holder.quantity = (EditText) convertView.findViewById(R.id.qty_val);
			holder.priceIcon=(TextView)convertView.findViewById(R.id.product_price_icon);
			holder.rate = (TextView) convertView.findViewById(R.id.price_val_choose);
			holder.addtocart = (TextView) convertView
					.findViewById(R.id.addtocart);
			holder.stock = (TextView) convertView.findViewById(R.id.stock);
			holder.stock_title = (TextView) convertView.findViewById(R.id.stock_title);
			mIcon.setTextfont(holder.addtocart);
			holder.addtocart.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

			//mIcon.setTextfont(holder.priceIcon);
			SharedPreferences prfs = positemchooseScreen.getSharedPreferences("profileData",
					Context.MODE_PRIVATE);
			String ccurrency = prfs.getString("Currency", "");
			holder.priceIcon.setText(ccurrency);

			setRobotoThinFont(holder.product,positemchooseScreen);
			setRobotoThinFont(holder.category,positemchooseScreen);
			setRobotoThinFont(holder.brand,positemchooseScreen);
			setRobotoThinFont(holder.quantity,positemchooseScreen);
			setRobotoThinFont(holder.rate,positemchooseScreen);
			setRobotoThinFont(holder.priceIcon,positemchooseScreen);
			setRobotoThinFont(holder.stock,positemchooseScreen);
			setRobotoThinFont(holder.stock_title,positemchooseScreen);

			holder.adapter_layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_ENABLED));

			holder.product.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.category.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.brand.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.quantity.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.rate.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.priceIcon.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.stock.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.stock_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// holder.ref = position;
		holder.quantity.setTag(position);
		holder.rate.setTag(position);
		final FetchProductModelData object = filteredArrayList.get(position);

		String imagestr = object.getproductImage();
		if (imagestr != null && imagestr.length() > 0) {
			String image_url = "http://nowconnect.in/"
					+ Utils.getUserId(positemchooseScreen) + "/" + imagestr;
			Log.i("image_url", image_url);
			Picasso.with(positemchooseScreen).load(image_url)
					.placeholder(R.drawable.product_box)
					.error(R.drawable.ic_launcher)
					.into(holder.productimage);
		}
		else
		{
			holder.productimage.setImageResource(R.drawable.product_box);
		}

		final Drawable d=holder.productimage.getDrawable();
		holder.productimage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imagePopup(positemchooseScreen,d);
			}
		});

		holder.category.setText(object.getCategoryName());
		holder.brand.setText(object.getBrandName());
		holder.product.setText(object.getProductName());
		holder.rate.setText(object.getPerUnitPrice());

		//holder.stock.setText(String.valueOf(avail_stock));

		if (hmap.containsKey(object.getProductID())) {
			String prevQuantity = hmap.get(object.getProductID()).getQuantity();
			int totalQunatity = Integer.parseInt(prevQuantity);
			int avail_stock=object.getAvailableStock()-totalQunatity;
			holder.stock.setText(String.valueOf(avail_stock));
		}
		else
		{
			int avail_stock=object.getAvailableStock();
			holder.stock.setText(String.valueOf(avail_stock));
		}



		holder.addtocart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			 String str = holder.quantity.getText().toString().trim();
				if (str != "" && str != null && !str.isEmpty()) {
					int q = Integer.parseInt(str);
					int totalQunatity=0;
					if (q > 0) {
						if (hmap.containsKey(object.getProductID())) {
							String prevQuantity = hmap.get(
									object.getProductID()).getQuantity();
							 totalQunatity = Integer.parseInt(prevQuantity)
									+ q;
						} else {
							productList.add(object.getProductID());
							totalQunatity=q;
						}

						final String qtystr=""+totalQunatity;
						final int after_stock=object.getAvailableStock()-totalQunatity;
						if(totalQunatity> object.getAvailableStock()) {
							AlertDialog.Builder b = new AlertDialog.Builder(positemchooseScreen)
									.setTitle("Alert")
									.setMessage("You don't have available stock. Do you want to continue ?")
									.setPositiveButton("Yes",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int whichButton) {


													FetchSelectedProductModelData fetchselection = new FetchSelectedProductModelData(
															object.getCategoryName(), object
															.getBrandName(), object
															.getProductName(), object
															.getProductID(), object
															.getPerUnitPrice(), object
															.getUnits(), object.getUserID(),
															qtystr, object.getTaxApplied(), object
															.getpriceWithTax(), object
															.getproductImage(),0);

													hmap.put(object.getProductID(), fetchselection);
													positemchooseScreen.updatePrice(hmap, productList);
													holder.quantity.setText("");
													holder.stock.setText(String.valueOf(after_stock));
													dialog.dismiss();
												}
											}
									)
									.setNegativeButton("No",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int whichButton) {
													dialog.dismiss();

												}
											}
									);

							b.create();
							b.show();
						}
						else
						{

							FetchSelectedProductModelData fetchselection = new FetchSelectedProductModelData(
									object.getCategoryName(), object
									.getBrandName(), object
									.getProductName(), object
									.getProductID(), object
									.getPerUnitPrice(), object
									.getUnits(), object.getUserID(),
									qtystr, object.getTaxApplied(), object
									.getpriceWithTax(), object
									.getproductImage(),0);
							hmap.put(object.getProductID(), fetchselection);
							positemchooseScreen.updatePrice(hmap, productList);
							holder.quantity.setText("");
							holder.stock.setText(String.valueOf(after_stock));
						}


					}
				}
			}
		});

		return convertView;
	}

	private void checkStock(int quantity,int stock)
	{


	}

	private static class ViewHolder {
		private TextView category, product, brand, rate, addtocart,priceIcon, stock,stock_title;
		private EditText quantity;
		private CircleImageView productimage;
		private LinearLayout adapter_layout;
		int ref;
	}

	public ArrayList<Integer> getSelectedPos() {
		return selectedPos;
	}

	@Override
	public Filter getFilter() {
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

			List<FetchProductModelData> list = fetchProductList;

			int count = list.size();
			final ArrayList<FetchProductModelData> nlist = new ArrayList<FetchProductModelData>(
					count);

			String filterableString = "";

			for (int i = 0; i < count; i++) {

				if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Product")) {
					filterableString = list.get(i).getProductName();
				} else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Category")) {
					filterableString = list.get(i).getCategoryName();

				} else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Brand")) {
					filterableString = list.get(i).getBrandName();
				} else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Price")) {
					filterableString = list.get(i).getPerUnitPrice();
				}
				// else {
				// filterableString = list.get(i).getProductName();
				// }

				if (filterableString.toLowerCase().contains(filterString)) {
					nlist.add(list.get(i));
					Log.i("Filter data", " - " + list.get(i));
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
			filteredArrayList = (ArrayList<FetchProductModelData>) results.values;
			if (filteredArrayList != null)
				notifyDataSetChanged();
		}
	}

	public void setModelData(ArrayList<FetchProductModelData> modelList) {
		if (modelList == null) {
			modelList = new ArrayList<FetchProductModelData>();
		}
		filteredArrayList = modelList;
	}

	public interface Actionable {
		void updatePrice(
				HashMap<String, FetchSelectedProductModelData> hmap,
				ArrayList<String> productList);
	}
}
