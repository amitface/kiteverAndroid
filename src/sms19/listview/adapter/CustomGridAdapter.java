package sms19.listview.adapter;


import sms19.listview.database.BadgeView;
import sms19.listview.newproject.Inbox;
import com.kitever.android.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.HONEYCOMB) 
public class CustomGridAdapter extends BaseAdapter 
{
	BadgeView badge4;
	// global variable for context and string for which grid view pass value
	private Context context;
	private String[] GridData;
	private int UPDATE;
	
	    public static final int NOTIFICATION_ID = 1;
	    public static NotificationManager mNotificationManager;
	    NotificationCompat.Builder builder;
	    String messageReceived;
	    public static Boolean notifyna=false;
	    Typeface moonicon;
	    String[] iconArray;
	    
	    
	// constructor of put data from gridview from main class, where grid view having grid view data
	public CustomGridAdapter(Context context, String[] GridData, int Update)
	{
		// BY which we send context and gridview data in both global variable for use any where
		this.context  = context;
		this.GridData = GridData;
		this.UPDATE   = Update;
		
		//getResources().getStringArray(R.array.fi); .
		iconArray=context.getResources().getStringArray(R.array.homeIcons);
	    moonicon = Typeface.createFromAsset(context.getAssets(),  "fonts/icomoon.ttf");

	}
	
	@Override
	public int getCount()
	{
	return GridData.length;
	}

	@Override
	public Object getItem(int position) 
	{
		
		return null;
	}

	@Override
	public long getItemId(int position) {
	
		return 0;
	}

	// this method depend upon gridview data length , and call grid data length time
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// use layout inflater for use external griditem.xml file
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View gridView;
		
		// This is run in first time and don't have any view
		if(convertView == null){
			
			gridView = new View(context);
			
			// get layout from griditem.xml file
			gridView= inflater.inflate(R.layout.grid_item, null);
		}
		
		else{
			// by which data move to convert view to grid view if exist
			gridView = convertView;
		}
		
		
		// set value in text view
		TextView textView= (TextView)gridView.findViewById(R.id.grid_item_label);
		textView.setText(GridData[position]);
		TextView item_icon= (TextView)gridView.findViewById(R.id.item_icon);
		Log.i("iconarray"," --- "+iconArray[position]);
		item_icon.setText((iconArray[position]));	
		item_icon.setTypeface(moonicon);
		
		String value= GridData[position];
		
	
		 if(value.equals("InAppMsg")){
			//imageView.setImageResource(R.drawable.inapp);
			textView.setText("Chats");
		
			if(UPDATE !=0){
				badge4 = new BadgeView(context);
		    	badge4.setText(""+UPDATE);
		    	badge4.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		    	badge4.setBadgeMargin(15, 10);
		    	badge4.setBadgeBackgroundColor(Color.parseColor("#A4C639"));		    	
		    	TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
		        anim.setInterpolator(new BounceInterpolator());
		        anim.setDuration(1000);
		    	badge4.toggle(anim, null);
			}
			
		}
		else
		{
			//imageView.setImageResource(R.drawable.ic_launcher);
		}
		
		return gridView;
	}

	
	
}
