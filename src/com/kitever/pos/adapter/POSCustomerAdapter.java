package com.kitever.pos.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kitever.android.R;
import com.kitever.app.context.CircleImageView;

import com.kitever.pos.model.CustomerList;
import com.kitever.pos.model.data.FetchInvoice;
import com.kitever.pos.model.data.PosCategoryModelData;
import com.kitever.utils.DateHelper;
import com.kitever.utils.TotalRows;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class POSCustomerAdapter extends BaseAdapter implements Filterable{

//	TextView name, order, business,
	
	private ArrayList<CustomerList> filteredArrayList;
	private ArrayList<CustomerList> posCustomerList;
	private Context context;
	private String selectedItemType;
	private ItemFilter itemFilter = new ItemFilter();
	private TotalRows rows;
	public POSCustomerAdapter(ArrayList<CustomerList> posCustomerList, Context context)
	{
		this.posCustomerList = posCustomerList;
		this.filteredArrayList = posCustomerList;
		rows = (TotalRows)context; 
		context = this.context;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(filteredArrayList!=null)
		{			
			rows.totalRows(Integer.toString(filteredArrayList.size()));
			return filteredArrayList.size();
		}
		
		else
		{
			rows.totalRows("0");
			return 0;
		}			
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return filteredArrayList.get(position);
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
					R.layout.pos_customer_adapter_layout, parent, false);
			holder.name=(TextView) convertView
					.findViewById(R.id.pos_customer_name);
			holder.order = (TextView) convertView
					.findViewById(R.id.pos_customer_order);
			holder.business = (TextView) convertView
					.findViewById(R.id.pos_customer_business);
			holder.credit = (TextView) convertView
					.findViewById(R.id.pos_customer_credit);
			holder.lastDate = (TextView) convertView
					.findViewById(R.id.pos_customer_date);
			holder.lastMonth = (TextView) convertView.findViewById(R.id.pos_customer_month);
			holder.profile = (CircleImageView) convertView.findViewById(R.id.pos_customer_profile);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name.setText(filteredArrayList.get(position).getContact_Name());
		holder.order.setText(filteredArrayList.get(position).getTotalOrder());
		holder.business.setText(filteredArrayList.get(position).getTotalAmount());
		holder.credit.setText(filteredArrayList.get(position).getTotalCredit());
//		holder.lastDate.setText(filteredArrayList.get(position).getLastOrderDate());
		if(filteredArrayList.get(position).getImageURL()!=null && filteredArrayList.get(position).getImageURL().length()!=0)
		Picasso.with(context)
		   .load(filteredArrayList.get(position).getImageURL())
		   .placeholder(R.drawable.no_image)
		   .error(R.drawable.ic_launcher)
		   .into(holder.profile);		
//		holder.profile.
		
		
		String pattern = "MM/dd/yyyy";
		String pattern1 = "d-MMM ,yy";
		
		String s[] = new String[2];
	    SimpleDateFormat format = new SimpleDateFormat(pattern);
	    try {
	      Date date =  new Date(filteredArrayList.get(position).getLastOrderDate());
	      format = new SimpleDateFormat(pattern1);
	      System.out.println("Date"+date+ " new Date"+format.format(date));
	      s=format.format(date).split("-");
	    }catch(Exception c)
	    {
	    	 System.out.println("Date erroer"+c.getMessage());
	    }
	    
	    holder.lastDate.setText(s[0]);
	    holder.lastMonth.setText(s[1]);
		return convertView;
	}

	private boolean creditRange(Double value, Double orignal)
	{
		return orignal > value;
	}
	
	
	public void dateRangeFilter(int code)
	{
		long orderDate;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();		
		try {
			cal.setTime(dateFormat.parse(dateFormat.format(DateHelper.getDate(DateHelper.getYear(cal.getTimeInMillis()),DateHelper.getMonthOfYear(cal.getTimeInMillis()),DateHelper.getDayOfMonth(cal.getTimeInMillis())))));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		orderDate = DateHelper.getDate(DateHelper.getYear(cal.getTimeInMillis()),DateHelper.getMonthOfYear(cal.getTimeInMillis()),DateHelper.getDayOfMonth(cal.getTimeInMillis()));
		orderDate = cal.getTimeInMillis();
		System.out.println("Current date "+orderDate+" code "+code);
		switch(code)
		{
			case 1:
				dateFilter(orderDate, orderDate+86400000L);
				break;
			case 2:
				dateFilter(orderDate-86400000L,orderDate);
				break;
			case 3:
				cal.set(Calendar.DAY_OF_WEEK,  cal.getFirstDayOfWeek());
				long weekMonth=cal.getTimeInMillis();
				System.out.println("Week date "+weekMonth+" code "+code);
				dateFilter(weekMonth,orderDate+86400000L);
				break;
			case 4:
			
				cal.set(Calendar.DAY_OF_MONTH, 1);
				long firstMonth=cal.getTimeInMillis();
				System.out.println("First date "+firstMonth+" code "+code);
				dateFilter(firstMonth,orderDate+86400000L);
				break;
			case 5:
				cal.set(Calendar.DAY_OF_MONTH, 1);
				long lastMonth=cal.getTimeInMillis();
				System.out.println("Last Month "+lastMonth+" code "+code);
				dateFilter(lastMonth-(86400000L*30L),lastMonth);
				break;
				
				default:
					break;
		}
	}
	
	public void dateFilter(Long startDate, Long endDate)
	{
		Long orderDate;
		List<CustomerList> list = posCustomerList;
		int count = list.size();	
		
//		System.out.println("start "+startDate+ " end "+endDate);
		
		final ArrayList<CustomerList> nlist = new ArrayList<CustomerList>(
				count);
		
			 for (int i = 0; i < count; i++) {
				 orderDate = list.get(i).getLastOrderDate();
//				 orderDate = list.get(i).getLastOrderDate();
				 if(orderDate>=startDate && orderDate<endDate) 
				 nlist.add(list.get(i));
			 }
			// TODO Auto-generated method stub
				filteredArrayList = nlist;
				if (filteredArrayList.size() >= 0)
					notifyDataSetChanged();		
				else 
					clearFilters();
	}
	
	public void clearFilters() {
		// TODO Auto-generated method stub
		filteredArrayList = this.posCustomerList;
    	notifyDataSetChanged();
	}

	public void setSelectedItemType(String itemType) {
		selectedItemType = itemType;
	}
	
	
	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return itemFilter;
	}

	private static class ViewHolder{
		TextView name, order, business, credit, lastDate, lastMonth; 
		CircleImageView profile;
	}
	
	private class ItemFilter extends Filter {
		

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			String filterString = constraint.toString().toLowerCase();

			FilterResults results = new FilterResults();

			List<CustomerList> list = posCustomerList;
			
			if(filterString.length()==0 || selectedItemType.equals("Top 50") || selectedItemType.equals(""))
			{
				results.values = list;
				results.count = list.size();
				return results;
			}

			int count = list.size();
			final ArrayList<CustomerList> nlist = new ArrayList<CustomerList>(
					count);

			String filterableString = "";

			for (int i = 0; i < count; i++) {

				if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Contact")) {
					filterableString = list.get(i).getCombined();
				}
				else if (selectedItemType != null
						&& selectedItemType.equalsIgnoreCase("Credit >")) {
					filterableString = list.get(i).getTotalCredit();
				}
//				else if (selectedItemType != null
//						&& selectedItemType.equalsIgnoreCase("Type")) {
//					filterableString = list.get(i).getType();
//				}
//				else {
//					filterableString = list.get(i).getContact_Name();
//				}
				
				if(selectedItemType != null && selectedItemType.equalsIgnoreCase("Credit >") )
				{
					if(creditRange(Double.parseDouble(filterString),Double.parseDouble(filterableString))==true)
						nlist.add(list.get(i));
				}
				else  if ( filterableString.toLowerCase().contains(filterString)) {
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
			filteredArrayList = (ArrayList<CustomerList>) results.values;
			if (filteredArrayList !=null && filteredArrayList.size() > 0)
			{
				notifyDataSetChanged();				
			}
			else if(filteredArrayList !=null && filteredArrayList.size() == 0)
			{
				notifyDataSetChanged();				
			}
				
			
//			 if (filteredArrayList.size() == 0) {
//			 mClickListener.onItemsEmpty();
//			 } else {
//			 mClickListener.onItemsAvailable();
//			 }
		}

	}

	
	public interface Actionable {
		void deleteCategory(Map map);
	}

//	public void setModelData(ArrayList<PosCategoryModelData> modelList) {
//		if(modelList==null){
//			modelList=new ArrayList<PosCategoryModelData>();
//		}
//		filteredArrayList = modelList;
//	}

}
