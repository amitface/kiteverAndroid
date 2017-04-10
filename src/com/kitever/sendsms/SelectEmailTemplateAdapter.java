package com.kitever.sendsms;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kitever.contacts.AdapterInterface;
import com.kitever.models.ContactInfo;


public class SelectEmailTemplateAdapter extends BaseAdapter implements Filterable{

	private List<ContactInfo> filteredArrayList=null;
	private List<ContactInfo> originalArrayList=null;
	// private Context mContext;
	private LayoutInflater inflater;
	
	private AdapterInterface mClickListener;
	
	private ItemFilter mItemFilter = new ItemFilter();

	public SelectEmailTemplateAdapter(Context context, List<ContactInfo> list,
			AdapterInterface clickListener) {
		super();

		// this.mContext = context;
		this.filteredArrayList = list;
		this.originalArrayList = list;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mClickListener = clickListener;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filteredArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		TextView contact_name;
		TextView contact_phno;
		CheckBox check_box;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			String filterString = constraint.toString().toLowerCase();
			
			FilterResults results = new FilterResults();
			
			final List<ContactInfo> list = originalArrayList;

			int count = list.size();
			final ArrayList<ContactInfo> nlist = new ArrayList<ContactInfo>(count);

			String filterableString ;
			
			for (int i = 0; i < count; i++) {
				filterableString = list.get(i).Contact_Name + list.get(i).countryCode + list.get(i).Contact_Mobile;
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
		protected void publishResults(CharSequence constraint, FilterResults results) {
			filteredArrayList = (ArrayList<ContactInfo>) results.values;
			notifyDataSetChanged();
			if(filteredArrayList.size()==0){
				mClickListener.onItemsEmpty();
			}else{
				mClickListener.onItemsAvailable();
			}
		}

	}
}