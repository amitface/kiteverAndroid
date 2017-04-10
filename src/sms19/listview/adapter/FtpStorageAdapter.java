package sms19.listview.adapter;

import java.util.ArrayList;

import sms19.listview.newproject.model.FtpDataListModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitever.android.R;

public class FtpStorageAdapter extends BaseAdapter {

	private ArrayList<FtpDataListModel> arrayList;
	private ArrayList<Integer> checkedpositions;
	boolean[] checkBoxState;

	public FtpStorageAdapter(ArrayList<FtpDataListModel> arrayList) {
		// TODO Auto-generated constructor stub
		this.arrayList = arrayList;
		checkedpositions = new ArrayList<>();
		checkBoxState = new boolean[arrayList.size()];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.ftp_list_item, parent,
					false);
			// holder.layout=(RelativeLayout)convertView.findViewById(R.id.list_layout);
			holder.image = (ImageView) convertView.findViewById(R.id.ftp_image);
			holder.dataName = (TextView) convertView
					.findViewById(R.id.image_name);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.check_box_id);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.checkBox.setTag(position);
		FtpDataListModel dataListModel=arrayList.get(position);
		if(dataListModel.getDataType().equalsIgnoreCase("A")){
			holder.image.setImageResource(R.drawable.audio_icon);
		}else if(dataListModel.getDataType().equalsIgnoreCase("V")){
			holder.image.setImageResource(R.drawable.video_icon);
		}else if(dataListModel.getDataType().equalsIgnoreCase("I")){
			holder.image.setImageResource(R.drawable.image_icon);
		}else if(dataListModel.getDataType().equalsIgnoreCase("F")){
			holder.image.setImageResource(R.drawable.file_icon);
		}
		// holder.image.setImageBitmap(null);
		holder.dataName.setText(dataListModel.getDataName());
		

		if (checkBoxState[position])
			holder.checkBox.setChecked(true);
		else
			holder.checkBox.setChecked(false);

		holder.checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Object tag = holder.checkBox.getTag();
				if (holder.checkBox.isChecked()) {
					checkBoxState[position] = true;
					checkedpositions.add((Integer) tag);
				} else {
					checkBoxState[position] = false;
					checkedpositions.remove(tag);
					
				}
			}
		});

		// checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		// {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked)
		// {
		// /*get the tag of the checkbox...in this case this will give the
		// (position of view)*/
		// Object tag=checkBox.getTag();
		// if ( isChecked )
		// {
		// // perform logic
		// count++;
		// checkedpositions.add((Integer) tag);
		// }
		// else
		// {
		// count--;
		// checkedpositions.remove(tag);
		// }
		// }
		// });
		// }
		return convertView;
	}

	public ArrayList<Integer> getcheckeditemcount() {
		return this.checkedpositions;
	}

	private static class ViewHolder {
		ImageView image;
		TextView dataName;
		RelativeLayout layout;
		CheckBox checkBox;
	}
}
