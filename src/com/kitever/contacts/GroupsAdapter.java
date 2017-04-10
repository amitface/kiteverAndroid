package com.kitever.contacts;

import java.util.ArrayList;
import java.util.List;

import sms19.listview.newproject.model.FetchGroupDetails.GroupDetails;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class GroupsAdapter extends BaseAdapter implements Filterable {

	private ArrayList<GroupDetails> filteredArrayList = null;
	private ArrayList<GroupDetails> originalArrayList = null;
	// private Context mContext;
	private LayoutInflater inflater;
	private AdapterInterface mClickListener;
	private ItemFilter mItemFilter = new ItemFilter();
	Context context;

	public GroupsAdapter(Context context, ArrayList<GroupDetails> list,
			AdapterInterface clickListener) {
		super();
		// this.mContext = context;
		this.filteredArrayList = list;
		this.originalArrayList = list;
		this.context=context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mClickListener = clickListener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (filteredArrayList != null) {
			return filteredArrayList.size();
		} else {
			return 0;
		}
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

		TextView group_name;
		ImageView editImageView;
		ImageView deleteImageView;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final ViewHolder holder;
		if (convertView == null) {

			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_view_group, null);
			holder.deleteImageView = (ImageView) convertView
					.findViewById(R.id.del_imageView);
			holder.group_name = (TextView) convertView
					.findViewById(R.id.textView);
			holder.editImageView = (ImageView) convertView
					.findViewById(R.id.imageView);

			holder.group_name.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			setRobotoThinFont(holder.group_name,context);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}

		holder.group_name.setText(filteredArrayList.get(position).group_name
				+ " (" + filteredArrayList.get(position).NoOfContacts + ")");

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mClickListener.onItemClicked(position, 3);
			}
		});

		holder.editImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mClickListener.onItemClicked(position, 1);
			}
		});

		holder.deleteImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mClickListener.onItemClicked(position, 2);
			}
		});

		return convertView;
	}

	public ArrayList<GroupDetails> getFilteredArrayList() {
		return filteredArrayList;
	}

	public void setFilteredArrayList(ArrayList<GroupDetails> filteredArrayList) {
		this.filteredArrayList = filteredArrayList;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return mItemFilter;
	}

	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			String filterString = constraint.toString().toLowerCase();

			FilterResults results = new FilterResults();

			final List<GroupDetails> list = originalArrayList;

			int count = list.size();
			final ArrayList<GroupDetails> nlist = new ArrayList<GroupDetails>(
					count);

			String filterableString;

			for (int i = 0; i < count; i++) {
				filterableString = list.get(i).group_name;
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
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			if (filteredArrayList == null)
				mClickListener.onItemsEmpty();
			else if (filteredArrayList.size() == 0)
				mClickListener.onItemsEmpty();
			else
				mClickListener.onItemsAvailable();
			filteredArrayList = (ArrayList<GroupDetails>) results.values;
			if (filteredArrayList != null)
				notifyDataSetChanged();
		}
	}
}