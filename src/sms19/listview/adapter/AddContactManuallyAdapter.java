package sms19.listview.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.sendsms.fragments.AddContactManuallyInterface;

public class AddContactManuallyAdapter extends BaseAdapter {
	private Context context;
	private int resource;
	private List<String> str;
	private AddContactManuallyInterface addContactManuallyInterface = null;

	public AddContactManuallyAdapter(Context context, int resource,
			List<String> objects) {
		this.context = context;
		this.resource = resource;
		this.str = objects;
		addContactManuallyInterface = (AddContactManuallyInterface) context;
	}

	public int getCount() {
		try {
			return str.size();
		} catch (Exception e) {
			return 0;
		}
	}

	public void addList(List<String> strList) {
		str = strList;
		notifyDataSetChanged();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return getItem(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// View v = convertView;

		Holder holder = new Holder();

		// First let's verify the convertView is not null
		if (convertView == null) {

			// This a new view we inflate the new layout
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(
					R.layout.list_view_add_contact_manually, parent, false);
			holder.NameView = (TextView) convertView
					.findViewById(R.id.addcontactManullay);
			holder.deleteView = (ImageView) convertView
					.findViewById(R.id.addcontactManullay_delete);
			convertView.setTag(holder);
		} else
			holder = (Holder) convertView.getTag();

		holder.NameView.setText(str.get(position));

		holder.NameView.setTag(position);
		// holder.deleteView.setTag(position);

		holder.deleteView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub.

				addContactManuallyInterface.changelist(position);
				// str.remove(position);
				notifyDataSetChanged();
			}
		});

		return convertView;
	}

	public static class Holder {
		public TextView NameView;
		public ImageView deleteView;
	}
}
