package com.kitever.sendsms;

import java.util.List;

import com.kitever.android.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitever.app.context.CustomStyle;
import com.kitever.models.ContactInfo;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class SendSMSContactsAdapter extends BaseAdapter {

	private List<ContactInfo> arrayList;
	 private Context mContext;
	private LayoutInflater inflater;




	public SendSMSContactsAdapter(Context context, List<ContactInfo> list) {
		super();

		this.mContext = context;
		this.arrayList = list;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		TextView contact_name;
		TextView contact_phno;
		//CheckBox check_box;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_list_app3, null);

			holder.contact_name = (TextView) convertView
					.findViewById(R.id.contact_name);
			holder.contact_phno = (TextView) convertView
					.findViewById(R.id.contact_phno);
/*			holder.check_box = (CheckBox) convertView
					.findViewById(R.id.check_box_contact);*/

			holder.contact_name.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			holder.contact_phno.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			setRobotoThinFont(holder.contact_name,mContext);

			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
			//holder.check_box.setOnCheckedChangeListener(null);
		}

		holder.contact_name.setText(arrayList.get(position).Contact_Name);
		holder.contact_phno.setText(arrayList.get(position).countryCode
				+ arrayList.get(position).Contact_Mobile);		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//setCheckedItem(holder.check_box, position);

			}
		});

/*		holder.check_box
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						arrayList.get(position).isSelected = isChecked;

						notifyDataSetChanged();
						mClickListener.onContactItemClicked(position);

					}
				});

		holder.check_box.setChecked(arrayList.get(position).isSelected);*/

		return convertView;
	}

/*	private void setCheckedItem(final CheckBox box, final int position) {
		if (box.isChecked()) {
			// box.setChecked(false);
			arrayList.get(position).isSelected = false;
		} else {
			// box.setChecked(true);
			arrayList.get(position).isSelected = true;
		}

		notifyDataSetChanged();
		mClickListener.onContactItemClicked(position);
	}*/

}