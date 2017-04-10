package com.kitever.pos.adapter;

import java.util.ArrayList;

import com.kitever.android.R;
import com.kitever.pos.activity.POSHomeScreen;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class POSHomeCustomAdapter extends BaseAdapter {

	ArrayList<String> gridData;
	  Typeface moonicon;
	    String[] iconArray;
	    Context context;

	public POSHomeCustomAdapter(ArrayList<String> gridData,Context context) {
		this.gridData = gridData;
		this.context=context;
		iconArray=context.getResources().getStringArray(R.array.posIcons);
		moonicon = Typeface.createFromAsset(context.getAssets(),  "fonts/icomoon.ttf");
	}

	public POSHomeCustomAdapter(ArrayList<String> gridData) {
		this.gridData = gridData;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gridData.size();
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
			convertView = inflater.inflate(R.layout.grid_view_item, parent,
					false);
			//holder.gridItemImage = (ImageView) convertView.findViewById(R.id.grid_icon);
			//item_icon
			holder.item_icon = (TextView) convertView.findViewById(R.id.item_icon);
			holder.gridItemLabel = (TextView) convertView
					.findViewById(R.id.grid_item_label);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item_icon.setText(iconArray[position]);
		holder.item_icon.setTypeface(moonicon);
		holder.gridItemLabel.setText(gridData.get(position));
		/*String value = gridData.get(position);
		if (value.equals("Category")) {
			holder.gridItemImage.setImageResource(R.drawable.category);
		} else if (value.equals("Products")) {
			holder.gridItemImage.setImageResource(R.drawable.product);
		} else if (value.equals("Orders")) {
			holder.gridItemImage.setImageResource(R.drawable.order);
		} else if (value.equals("Tax")) {
			holder.gridItemImage.setImageResource(R.drawable.invoice);
		} else if (value.equals("Brand")) {
			holder.gridItemImage.setImageResource(R.drawable.invoice);
		} else if (value.equals("Setting")) {
			holder.gridItemImage.setImageResource(R.drawable.setting);
		}*/
		return convertView;
	}

	private static class ViewHolder {
		ImageView gridItemImage;
		TextView gridItemLabel,item_icon;
	}
}
