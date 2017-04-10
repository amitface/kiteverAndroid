package sms19.inapp.msg.adapter;

import java.util.ArrayList;

import com.kitever.android.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CountryListAdapter extends BaseAdapter {

	private ArrayList<sms19.inapp.msg.model.Countrymodel> arrayList;
	private LayoutInflater inflater;

	public CountryListAdapter(Activity activity,
			ArrayList<sms19.inapp.msg.model.Countrymodel> list) {

		arrayList = list;
		inflater = activity.getLayoutInflater();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.country_row, null);

			holder = new ViewHolder();
			holder.cityname = (TextView) view.findViewById(R.id.country_name);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.cityname.setText(arrayList.get(position).getCountry_name()
				+ " (" + arrayList.get(position).getCountrycode().trim() + ")");

		/*
		 * if(!holder.cityname.getText().toString().trim().contains("+")){
		 * holder
		 * .cityname.setText("+"+arrayList.get(position).getCountrycode()); }
		 */

		return view;
	}

	private class ViewHolder {
		TextView cityname;

	}

}
