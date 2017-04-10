package com.kitever.pos.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CircleImageView;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.pos.activity.PosCart;
import com.kitever.pos.model.data.FetchSelectedProductModelData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static sms19.inapp.msg.constant.Utils.imagePopup;

public class PosCartAdapter extends BaseAdapter {

	
	private ArrayList<FetchSelectedProductModelData> fetchSelectedProductList;
	
	private PosCart poscart;
	private ArrayList<String> productList;
	private HashMap<String,FetchSelectedProductModelData> hmap;
	MoonIcon mIcon;
	private Context context;
	
	public PosCartAdapter(PosCart poscart,ArrayList<String> productList,HashMap<String,FetchSelectedProductModelData> hmap) {
		this.poscart = poscart;		
		this.hmap=hmap;
		this.productList=productList;
		mIcon=new MoonIcon(poscart);
		this.context = poscart;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return productList.size();
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
					R.layout.pos_cart_item, parent, false);

			holder.adapter_layout = (LinearLayout ) convertView.findViewById(R.id.adapter_layout);
			holder.productimage = (CircleImageView ) convertView.findViewById(R.id.prodimg);
			holder.product = (TextView) convertView.findViewById(R.id.item_val);
			holder.category = (TextView) convertView.findViewById(R.id.item_category);
			holder.brand=   (TextView) convertView.findViewById(R.id.item_brand);
			holder.quantity = (EditText) convertView.findViewById(R.id.qty);
			holder.rate = (TextView) convertView.findViewById(R.id.price_val);	
			holder.item_plus=(TextView) convertView.findViewById(R.id.qty_plus);
			holder.item_minus=(TextView) convertView.findViewById(R.id.qty_minus);
			holder.remove_cart=(TextView) convertView.findViewById(R.id.remove_cart);
			holder.price_icon=(TextView) convertView.findViewById(R.id.product_price_icon);
			
			mIcon.setTextfont(holder.item_plus);
			mIcon.setTextfont(holder.item_minus);
			mIcon.setTextfont(holder.remove_cart);
			mIcon.setTextfont(holder.price_icon);

			setRobotoThinFont(holder.product,context);
			setRobotoThinFont(holder.category,context);
			setRobotoThinFont(holder.brand,context);
			setRobotoThinFont(holder.quantity,context);
			setRobotoThinFont(holder.rate,context);


			holder.adapter_layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_ENABLED));

			holder.product.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.category.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.brand.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.quantity.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.rate.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Drawable d=holder.productimage.getDrawable();
		holder.productimage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imagePopup(context,d);
			}
		});
		
		
		holder.quantity.setTag(position);
	    holder.rate.setTag(position);
	    holder.productimage.setTag(position);
	    
		final FetchSelectedProductModelData object = hmap.get(productList.get(position));			
		String imagestr=object.getproductImage();
		Log.i("imagestr","imagestr - "+imagestr);
		
		if(imagestr!=null && imagestr.length()>0)
		{
		String image_url= "http://nowconnect.in/" + Utils.getUserId(poscart)+"/" + imagestr;	
		Picasso.with(poscart)
		   .load(image_url)
		   .placeholder(R.drawable.product_box)
			.error(R.drawable.ic_launcher)
		   .into(holder.productimage);	
		}
		else
		{
			holder.productimage.setImageDrawable(context.getResources().getDrawable(R.drawable.product_box));
		}
		
	
		Integer q=Integer.parseInt(object.getQuantity());
		double item_rate=Float.parseFloat(object.getpricewithTax());		
		double tprice=q*item_rate;
		holder.category.setText(object.getCategoryName());
		holder.brand.setText(object.getBrandName());
		holder.product.setText(object.getProductName());
		holder.rate.setText(Utils.doubleToString(tprice));
		holder.quantity.setText(object.getQuantity());
		
		poscart.cartData(hmap, productList);
		
		holder.item_plus.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				Integer q=Integer.parseInt(object.getQuantity());
				
				if(q< 999)
				{		
					double tprice=0.00f;
					q=q+1;
					double item_rate=Float.parseFloat(object.getpricewithTax());
					tprice=q*item_rate;
					String str=""+q;
					
					Log.i("cart plus","quantity : "+q+" total price : "+tprice);
					holder.rate.setText(""+tprice);
				
				FetchSelectedProductModelData fetchselection=new FetchSelectedProductModelData(
						object.getCategoryName(), object.getBrandName(),object.getProductName(),
							object.getProductID(), object.getPerUnitPrice(), object.getUnits(), object.getUserID(),
							str,object.getTaxApplied(),object.getpricewithTax(),object.getproductImage(),0);
			     hmap.put(productList.get(position), fetchselection);	
			     poscart.cartData(hmap, productList);
//			     Toast.makeText(context, "item_rate - "+item_rate+"\n quantity - "+q, 500).show();
			     notifyDataSetChanged();
				}
				
				else
				{	
				Toast.makeText(poscart, "Cannot add more than 999 item", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		holder.item_minus.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Integer q=Integer.parseInt(object.getQuantity());
				if(q>1)
				{
					double tprice=0.00f;
					double item_rate=Float.parseFloat(object.getPerUnitPrice());
					q=q-1;
					String str=""+q;
					tprice=q*item_rate;			
					
					Log.i("cart minus","quantity : "+q+" total price : "+tprice);
					holder.rate.setText(""+tprice);
					holder.quantity.setText(str);
				   FetchSelectedProductModelData fetchselection=new FetchSelectedProductModelData(
						object.getCategoryName(), object.getBrandName(),object.getProductName(),
							object.getProductID(), object.getPerUnitPrice(), object.getUnits(), object.getUserID(),
							str,object.getTaxApplied(),object.getpricewithTax(),object.getproductImage(),0);
			     hmap.put(productList.get(position), fetchselection);
			     poscart.cartData(hmap, productList);
//			     Toast.makeText(context, "item_rate - "+item_rate+"\n quantity - "+q, 500).show();
			     notifyDataSetChanged();
				}
				
				
			}
		});
		
		holder.remove_cart.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				new AlertDialog.Builder(context)
				.setCancelable(false)
				.setMessage("Do you want to delete "+object.getProductName()+ " from cart?")
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();								
								hmap.remove(productList.get(position));
								productList.remove(position);	
								poscart.cartData(hmap, productList);
								notifyDataSetChanged();
				
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
		private TextView category, product, brand, rate,remove_cart,price_icon,item_plus,item_minus;
		EditText quantity;
		LinearLayout adapter_layout;
		CircleImageView  productimage;
	}
	
	public interface  UserCart
	{
		void cartData(HashMap<String, FetchSelectedProductModelData> hmap, ArrayList<String> productList);
	}

}
