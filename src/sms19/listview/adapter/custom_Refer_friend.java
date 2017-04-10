package sms19.listview.adapter;

import java.util.List;

import com.kitever.android.R;
import sms19.listview.newproject.model.ReferListModelDB;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class custom_Refer_friend extends BaseAdapter{

	Context CONTEXT;
	private int RESOURCE;
	private List<ReferListModelDB> DATA;
	
	// constructer for fetch data from another activity
	public custom_Refer_friend(Context cnt, int Resource, List<ReferListModelDB> data){
		
		this.DATA     = data;
		this.RESOURCE = Resource;
		this.CONTEXT  = cnt;
		

		
	}
	
	@Override
	public int getCount() {
		try
		{
		return DATA.size();
		}catch(Exception e)
		{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			
			LayoutInflater inflater = (LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(RESOURCE, null);
			
			TextView textName   = (TextView) convertView.findViewById(R.id.textreferListName);
			TextView textNumber = (TextView) convertView.findViewById(R.id.textreferListNumber);
			TextView textStatus = (TextView) convertView.findViewById(R.id.textreferListStatus);
			TextView textDate   = (TextView) convertView.findViewById(R.id.textreferListDate);
			
			textName.setText(DATA.get(position).getNAME());
			textNumber.setText(DATA.get(position).getNUMBER());
			textStatus.setText(DATA.get(position).getSTATUS());
			textDate.setText(DATA.get(position).getDATE());
			
		}
		
		return convertView;
	}

}
