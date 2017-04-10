package com.kitever.pos.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.kitever.android.R;

import com.kitever.pos.model.data.FetchInvoice;
import com.kitever.pos.model.data.ReceiptDetail;
import com.kitever.utils.DateHelper;
import com.kitever.utils.TotalRows;

public class POSInvoiceDetailAdapter extends BaseAdapter {
		
		private SparseBooleanArray mSelectedItemsIds;
		ArrayList<ReceiptDetail> posInvoiceList;
		private String selectedItemType;

		private ArrayList<ReceiptDetail> filteredArrayList = null;
		private ArrayList<ReceiptDetail> selectedArrayList = null;
		private Context context;
//		private TotalRows rows;
		public POSInvoiceDetailAdapter(Context context, ArrayList<ReceiptDetail> posInvoiceList)
		{
			super();
			mSelectedItemsIds = new SparseBooleanArray();
			this.posInvoiceList = posInvoiceList;
			filteredArrayList = this.posInvoiceList;
			selectedArrayList = new ArrayList<>();
			this.context = context;
//			rows = (TotalRows)context; 
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
//			rows.totalRows(Integer.toString(filteredArrayList.size()));
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
				
		public void toggleSelection(int position) {
	        selectView(position, !mSelectedItemsIds.get(position));
	    }
	 
	    public void selectView(int position, boolean value) {
	        if (value)
	            mSelectedItemsIds.put(position, value);
	        else
	            mSelectedItemsIds.delete(position);
	        notifyDataSetChanged();
	    }
	 
	    public SparseBooleanArray getSelectedIds() {
	        return mSelectedItemsIds;
	    }
	    
	    public void emptyArray()
	    {
	    	mSelectedItemsIds =null;
	    	mSelectedItemsIds = new SparseBooleanArray();
	    	selectedArrayList = null;
	    	selectedArrayList = new ArrayList<>();
	    }

	    public void clearFilters()
	    {
	    	filteredArrayList = this.posInvoiceList;
	    	notifyDataSetChanged();
	    }
	    
	    public ArrayList<ReceiptDetail> getArrayList()
	    {
	    	
	    	for (int i = 0; i < mSelectedItemsIds.size(); i++) {    		
	    				selectedArrayList.add(filteredArrayList.get(mSelectedItemsIds.keyAt(i)));    		
	    	}

	    	return selectedArrayList;
	    }
	    
	    public ArrayList<ReceiptDetail> getAllArrayList()
	    {	
	    	return filteredArrayList;
	    }
	    
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				convertView = inflater.inflate(R.layout.pos_invoice_detail_adapter_layout,
						parent,false);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.month = (TextView) convertView.findViewById(R.id.month);
				holder.receipt = (TextView) convertView.findViewById(R.id.invoice_receipt);
				holder.paid = (TextView) convertView.findViewById(R.id.invoice_paid);
				holder.mop = (TextView) convertView.findViewById(R.id.invoice_mop);
				holder.details = (TextView) convertView
						.findViewById(R.id.invoice_pay_detail);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ArrayList<ReceiptDetail> object = filteredArrayList;
			holder.receipt.setText(object.get(position).getReceiptNo());
			holder.mop.setText(object.get(position).getPayMode());
			holder.paid.setText(object.get(position).getPaidAmount());
			
			String str = "";
			if(object.get(position).getBankName()!=null && !object.get(position).getBankName().equals("null"))
				str = object.get(position).getBankName()+" ";
			if(object.get(position).getChequeNo()!=null && !object.get(position).getChequeNo().equals("null"))
				str = str+object.get(position).getChequeNo();
			 
			holder.details.setText(str);
			
//			holder.balanceAmt.setText(object.get(position).getBalanceAmount());
			
			String pattern = "MM/dd/yyyy";
			String pattern1 = "d-MMM ,yy";
			
			String s[] = new String[2];
		    SimpleDateFormat format = new SimpleDateFormat(pattern);
		    try {
		    	Date date =  new Date(object.get(position).getReceiptDate());		      
		    	format = new SimpleDateFormat(pattern1);
//		    	System.out.println("Date"+date+ " new Date"+format.format(date));
		    	s=format.format(date).split("-");
		    }catch(Exception c)
		    {
		    	 System.out.println("Date erroer"+c.getMessage());
		    }
		    
		    holder.date.setText(s[0]);
		    holder.month.setText(s[1]);
		    
			return convertView;
		}
		
		private static class ViewHolder {
			TextView receipt, paid, mop, details, date, month;
		}
		
		public void setSelectedItemType(String itemType) {
			selectedItemType = itemType;
		}		
		
	}
