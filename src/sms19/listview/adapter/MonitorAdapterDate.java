package sms19.listview.adapter;

import java.util.List;

import sms19.listview.newproject.model.TransactionListByDate.TdetailDate;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;


public class MonitorAdapterDate extends ArrayAdapter<TdetailDate>
    {

    	// global variable for context and string for which grid view pass value
    	private Context context;
    	private List<TdetailDate> ListData;
    	private int resource;

    	// constructor of put data from gridview from main class, where grid view having grid view data
    	public MonitorAdapterDate(Context context,int resource ,List<TdetailDate> list){
    		// BY which we send context and gridview data in both global variable for use any where
    		super(context, resource, list);
    		
    		this.context  = context;
    		this.ListData = list;
    		this.resource = resource;
    	}
    	
    	@Override
    	public int getCount() 
    	{
    	try
    	{
    	return ListData.size();
    	}
    	catch(Exception e)
    	{
    		return 0;
    	}
    	}

    	@Override
    	public TdetailDate getItem(int position) {
    		
    		return null;
    	}

    	@Override
    	public long getItemId(int position) {
    	
    		return 0;
    	}

    	// this method depend upon gridview data length , and call grid data length time
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		
    		// This is run in first time and don't have any view
    		if(convertView == null){
    		
    			convertView = LayoutInflater.from(context).inflate(resource, null);
    		}
    		
    	        TextView textDate1,textDebited1,textCredit1,textBalance1,textFormTo1;
	    		
	    		textDate1    = (TextView) convertView.findViewById(R.id.textDate1);
	    		textDebited1 = (TextView) convertView.findViewById(R.id.textDebited1);
	    		textCredit1  = (TextView) convertView.findViewById(R.id.textCredit1);
	    		textBalance1 = (TextView) convertView.findViewById(R.id.textBalance1);


			setRobotoThinFont(textDate1,context);
			setRobotoThinFont(textDebited1,context);
			setRobotoThinFont(textCredit1,context);
			setRobotoThinFont(textBalance1,context);

			textDate1.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			textDebited1.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			textCredit1.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
			textBalance1.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


//	    		Log.w("TAG","LIST_SIZE::"+ListData.size());
	    		
	    		String spliteDate = ListData.get(position).getDatecreated().substring(0, ListData.get(position).getDatecreated().indexOf("T"));
	    		String time= ListData.get(position).getDatecreated().substring(ListData.get(position).getDatecreated().indexOf("T")+1,ListData.get(position).getDatecreated().length());
	    		//change format of date
	    		String dataWithGoodFormat[]= spliteDate.split("-");
				String dataGood= dataWithGoodFormat[2]+"-"+dataWithGoodFormat[1]+"-"+dataWithGoodFormat[0];
					    			    		
	    		textDate1.setText(dataGood+"\n"+time);
	    		textDebited1.setText(ListData.get(position).getDebits());
	    		textCredit1.setText(ListData.get(position).getCredits());
	    		textBalance1.setText(ListData.get(position).getBalance());
    		
    		    		
    		
    		return convertView;
    	}

    	
}



