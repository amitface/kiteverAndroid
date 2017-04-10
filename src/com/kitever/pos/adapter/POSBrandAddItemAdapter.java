package com.kitever.pos.adapter;

import java.util.ArrayList;

import com.kitever.android.R;

import com.kitever.app.context.CustomStyle;
import com.kitever.pos.model.BrandModelData;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class POSBrandAddItemAdapter extends BaseAdapter{
	
	public ArrayList<BrandModelData> getFilteredArrayList() {
		return filteredArrayList;
	}

	private ArrayList<BrandModelData> filteredArrayList;
	private String brandName; 
	private Context context;
	
	public void setBrandName() {
		
		filteredArrayList.get(filteredArrayList.size()-1).setBrandName(((EditText) getItem(getFilteredArrayList().size()-1)).getText().toString());
	}

	public POSBrandAddItemAdapter(Context context, ArrayList<BrandModelData> listBrand)
	{
		this.context = context;
		filteredArrayList = listBrand;
	}	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(filteredArrayList!=null)
		return filteredArrayList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return  getItem(position);
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
		
		if(convertView == null)
		{
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(
					R.layout.brand_add_list_item, parent, false);
			
			holder.name = (EditText) convertView.findViewById(R.id.brand_name_item_add);
			holder.action = (TextView) convertView.findViewById(R.id.brand_add_item);


			holder.name.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
			setRobotoThinFont(holder.name,context);
			holder.action.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
			setRobotoThinFont(holder.action,context);

			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
//		System.out.println(position+" position "+filteredArrayList.toString());
			
		holder.name.setText(filteredArrayList.get(position).getBrandName());
		
		holder.action.setText(filteredArrayList.get(position).getStatus());		
		holder.name.setTag(position);
		
			
		
		
		final String stat = filteredArrayList.get(position).getStatus();
		if(stat.equals("Remove"))
			holder.name.setEnabled(false);
		else
			holder.name.setEnabled(true);
		
		holder.name.requestFocus();
		
		holder.action.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(position<6)
				{
					
					if(stat.equals("Add"))
					{
						if(holder.name.getText().toString().length()!=0)
						{
							filteredArrayList.add(new BrandModelData("", "Add"));
							filteredArrayList.get(position).setStatus("Remove");
							filteredArrayList.get(position).setBrandName(holder.name.getText().toString());
							notifyDataSetChanged();
						}else
							Toast.makeText(context, "Brand Name cannot be empty", 100).show();

					}
					else 
					{
						filteredArrayList.remove(position);
						notifyDataSetChanged();
					}
				
				}else
					Toast.makeText(context,"Only six item allowed in one time", Toast.LENGTH_SHORT).show();
			}
		});
		
		
			
		
//		holder.name.addTextChangedListener(new GenericTextWatcher(holder.name, position));		
		return convertView;
	}

	private static class ViewHolder {	
		private EditText name;
		private TextView action;
	}
	
//	 private class GenericTextWatcher implements TextWatcher{
//
//	       private int position;
//	       private EditText view;
//	       private GenericTextWatcher(EditText view, int position) {
//	            this.position = position;
//	            this.view = view;
//	        }
//
//	        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//	        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//	        	String text = charSequence.toString();
//	            //save the value for the given tag :
//	            filteredArrayList.get(position).setBrandName(view.getText().toString());
//	           notify();  
//	        }
//
//	     
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				
//			}
//	    }

}
