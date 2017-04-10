package sms19.listview.adapter;

import java.util.ArrayList;

import sms19.listview.database.DataBaseDetails;
import com.kitever.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomEditContacts extends ArrayAdapter<String> 
{
Context context;
private int resource;

private ArrayList<String> Mobile;
private ArrayList<String> Name;

String[] stockArr;
String[] stocknam;
DataBaseDetails dbobj=new DataBaseDetails(getContext());
	public CustomEditContacts(Context context, int resource, ArrayList<String> objects, ArrayList<String> objectsName) 
	{
		super(context, resource, objects);
		this.resource = resource;
		this.Mobile   = objects;
		this.context  = context;
		this.Name     = objectsName;
		
	}
	@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		final int io = position;
		
		if(convertView == null)
		{
		    convertView   =LayoutInflater.from(context).inflate(resource, null);
		}
		
		ImageView im2 =(ImageView)convertView.findViewById(R.id.delete);
		TextView  tv  =(TextView)convertView.findViewById(R.id.Mobilete);
		TextView  tvv  =(TextView)convertView.findViewById(R.id.textViewNameMobi);
			
		// for fetch number
		stockArr         = new String[Mobile.size()];
		stockArr         = Mobile.toArray(stockArr);
		final String num = stockArr[position].toString();
		
		//for fetch name
		stocknam         = new String[Name.size()];
	    stocknam         = Name.toArray(stocknam);
	    final String nam = stocknam[position].toString();
		
		tv.setText(stockArr[position]);
		tvv.setText(stocknam[position]);
		
		im2.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
			try
			{
			dbobj.Open();
			dbobj.deleteNumberFromRecipient(num);
			dbobj.close();
			}
			catch(Exception es)
			{
				
			}
			Mobile.remove(io);
			notifyDataSetChanged();
			}
		    });
		
		
	   return convertView;
	}
			

}
