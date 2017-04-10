package sms19.listview.adapter;

import java.util.List;

import sms19.listview.newproject.model.TransactionDetailModel.Tdetail;
import com.kitever.android.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



    public class MonitorAdapter extends ArrayAdapter<Tdetail>
    {

    	// global variable for context and string for which grid view pass value
    	private Context context;
    	private List<Tdetail> ListData;
    	private int resource;

		//int count =0;
		//int c = 0,c1 =0;
    	// constructor of put data from grid view from main class, where grid view having grid view data
    	public MonitorAdapter(Context context,int resource ,List<Tdetail> list){
    		// BY which we send context and grid view data in both global variable for use any where
    		super(context, resource, list);
    		
    		this.context  = context;
    		this.ListData = list;
    		this.resource = resource;
    	}
    	
    	@Override
    	public int getCount() {
    		// use for find grid_data length
    		return ListData.size();
    	}

    	@Override
    	public Tdetail getItem(int position) {
    		
    		return null;
    	}

    	@Override
    	public long getItemId(int position) {
    	
    		return 0;
    	}

    	// this method depend upon gridview data length , and call grid data length time
    	@Override
    	public View getView(final int position, View convertView, ViewGroup parent) {
    		
    		
    		// This is run in first time and don't have any view
    		if(convertView == null){
    		
    			convertView = LayoutInflater.from(context).inflate(resource, null);
    		}
    		
	    		TextView textDate,textDebited,textCredit,textBalance,textFormTo;
	    		
	    		textDate    = (TextView) convertView.findViewById(R.id.textDate);
	    		textDebited = (TextView) convertView.findViewById(R.id.textDebited);
	    		textCredit  = (TextView) convertView.findViewById(R.id.textCredit);
	    		textBalance = (TextView) convertView.findViewById(R.id.textBalance);
	    	
//	    		Log.w("TAG","LIST_SIZE::"+ListData.size());
	    		
	    		try
	    		{
	    		
	    		//String spliteDate     = ListData.get(position).getDatecreated().substring(0, ListData.get(position).getDatecreated().indexOf("T"));
	    		//String spliteDatenext =ListData.get(position+1).getDatecreated().substring(0, ListData.get(position+1).getDatecreated().indexOf("T"));
	    		
	    		//change format of date
	    		//String dataWithGoodFormat[]= spliteDate.split("-");
				//String dataGood= dataWithGoodFormat[2]+"-"+dataWithGoodFormat[1]+"-"+dataWithGoodFormat[0];
				
	    		
    			//Log.w("TAG","@@:spliteDate:"+spliteDate+"@@@:spliteDatenext:"+spliteDatenext+"@@@@@:position:"+position);

	    		/*
	    		
	    		if(spliteDate.equalsIgnoreCase(spliteDatenext))
	    		{
	    			Log.w("TAG","@@@@@:count:"+count);
	    			
	    			
	    			
		    	    if(count == 0){
			    			
				    	int a=Integer.parseInt(ListData.get(position).getDebits());
				    	int a1=Integer.parseInt(ListData.get(position+1).getDebits());
				    	c=a+a1;
				    	textDate.setText(spliteDatenext);
				    	textDebited.setText(""+c);
				    	int b=Integer.parseInt(ListData.get(position).getCredits());
				    	int b1=Integer.parseInt(ListData.get(position+1).getCredits());
				    	c1=b+b1;
				    	textCredit.setText(""+c1);
				    	textBalance.setText(ListData.get(position).getBalance());
				    	
				    	count++;
		    		}
		    		else{
		    			int a=Integer.parseInt(ListData.get(position).getDebits());
				    	int a1=Integer.parseInt(ListData.get(position+1).getDebits());
				    	c+=a1;
				    	textDate.setText(spliteDatenext);
				    	textDebited.setText(""+c);
				    	int b=Integer.parseInt(ListData.get(position).getCredits());
				    	int b1=Integer.parseInt(ListData.get(position+1).getCredits());
				    	c1+=b1;
				    	textCredit.setText(""+c1);
				    	textBalance.setText(ListData.get(position).getBalance());
				    	
		    		}
		    	
	    		}
	    		else
	    		{*/
	    		textDate.setText(ListData.get(position).getDATE());
	    		textDebited.setText(ListData.get(position).getDEBITS());
	    		textCredit.setText(ListData.get(position).getCREDITS());
	    		textBalance.setText(ListData.get(position).getBALANCE());
	    		
	    		
	    		//}
	    		
	    		}
	    		catch(Exception e)
	    		{}
    		
    	
    		    		
    		
    		return convertView;
    	}

    	
}



