/*package com.kitever.pos.adapter;

import java.util.ArrayList;

import com.kitever.android.R;

import android.support.v7.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class POSHomeScreenAdapter extends RecyclerView.Adapter<POSHomeScreenAdapter.POSHomeScreenViewHolder>{

	public class POSHomeScreenViewHolder extends RecyclerView.ViewHolder {
		private TextView paymentVal, creditVal;
		GridView gridView;
		public POSHomeScreenViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			//paymentVal = (TextView) view.findViewById(R.id.payment_val);
			//creditVal = (TextView) view.findViewById(R.id.credit_val);
			gridView = (GridView) view.findViewById(R.id.grid_view);
		}

	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onBindViewHolder(POSHomeScreenViewHolder holder, int pos) {
		// TODO Auto-generated method stub
		holder.paymentVal.setText("Rs 20Lac");
		holder.creditVal.setText("Rs 10Lac");
		ArrayList<String> gridData = new ArrayList<String>();
		gridData.add("Category");
		gridData.add("Products");
		gridData.add("Orders");
		gridData.add("Setting");

		holder.gridView.setAdapter(new POSHomeCustomAdapter(gridData));
	}

	@Override
	public POSHomeScreenViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.pos_home_grid_layout, parent, false);
		return new POSHomeScreenViewHolder(view);
	}
}
*/