package com.kitever.pos.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.pos.activity.POSCustomerPaymentDetailScreen;
import com.kitever.pos.model.data.CustomerOrderDetail;
import com.kitever.pos.model.data.FetchCustomerOrderDetailModelData;
import com.kitever.pos.model.data.PaymentDetail;

public class POSCustomerPaymentDetailAdapter extends BaseAdapter {
	private ArrayList<PaymentDetail> filteredArrayList;
	private POSCustomerPaymentDetailScreen paymentDetail;
	private String userId, userLogin, password;

//	public POSCustomerPaymentDetailAdapter(
////			POSCustomerPaymentDetailScreen paymentDetail,
//			List<PaymentDetail> list, String userId,
//			String userLogin, String password) {
//		// TODO Auto-generated constructor stub
//		filteredArrayList = (ArrayList<PaymentDetail>) list;
////		this.paymentDetail = paymentDetail;
//		this.userId = userId;
//		this.userLogin = userLogin;
//		this.password = password;
//	}

	public POSCustomerPaymentDetailAdapter(
			
			List<PaymentDetail> list) {
		// TODO Auto-generated constructor stub
		filteredArrayList = (ArrayList<PaymentDetail>) list;
//		this.paymentDetail = paymentDetail;

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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(
					R.layout.pos_customer_paymnet_detail_adapter_layout,
					parent, false);
			
			holder.mop = (TextView) convertView
					.findViewById(R.id.payment_detail_mop);
			holder.paid = (TextView) convertView
					.findViewById(R.id.payment_detail_paid);
			holder.bank = (TextView) convertView
					.findViewById(R.id.payment_detail_bank);
			holder.checkNumber = (TextView) convertView
					.findViewById(R.id.payment_detail_chq_no);
			holder.date = (TextView) convertView
					.findViewById(R.id.payment_detail_date);
			holder.month = (TextView) convertView
					.findViewById(R.id.payment_detail_month);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.paid.setText(filteredArrayList.get(position).getPaidAmount());
		holder.bank.setText(filteredArrayList.get(position).getBankName());
		
		if(filteredArrayList.get(position).getChequeNo()!=null)
		holder.checkNumber.setText(filteredArrayList.get(position).getChequeNo().trim());
		
		holder.mop.setText(filteredArrayList.get(position).getPayMode());

		String pattern = "MM/dd/yyyy";
		String pattern1 = "d-MMM ,yy";
		
		String s[] = new String[2];
	    SimpleDateFormat format = new SimpleDateFormat(pattern);
	    try {
	    	Date date =  new Date(filteredArrayList.get(position).getReceiptDate());
//	      Date date =  format.parse(object.getOrderDate());
	      format = new SimpleDateFormat(pattern1);
	      System.out.println("Date"+date+ " new Date"+format.format(date));
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
		TextView date, month, paid, bank, checkNumber,mop;
		ImageView addPaymentIcon;
	}	
}
