package sms19.listview.adapter;

import java.util.List;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.BNotification;
import sms19.listview.newproject.BuyCreditPage;
import sms19.listview.newproject.EditProfile;
import sms19.listview.newproject.Friendsinvite;
import com.kitever.android.R;
import sms19.listview.newproject.TemplateHolder;
import sms19.listview.newproject.model.NotiModel.bNoti;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitever.app.context.CircleImageView;
import com.kitever.app.context.CustomStyle;
import com.kitever.sendsms.SendSmsScreen;
import com.squareup.picasso.Picasso;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class NotificationAdapter extends ArrayAdapter<bNoti>
{
	List<bNoti> defaultdata;
	Context con;
	DataBaseDetails db;
	
	String Link="",finalmessage="",setDate="",fixdate="",time="";
	
	public NotificationAdapter(Context cont, int resource,List<bNoti> objects)
	{
		super(cont, resource, objects);
		this.defaultdata=objects;
		this.con=cont;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final Holder holder;
		final int id=position;
		db=new DataBaseDetails(con);
		
		if(convertView==null)
		{
		   holder = new Holder();
						
		   LayoutInflater inflate = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   convertView=inflate.inflate(R.layout.activity_notification, null);
	    
		   //Log.w("I AM IN ADAPTER","I AM IN ADAPTER");

			holder.notification_image      =(ImageView) convertView.findViewById(R.id.notification_image);
	       holder.delete      =(ImageView) convertView.findViewById(R.id.deleteall);
	    //   holder.textBTitle  =(TextView)  convertView.findViewById(R.id.textBTitle);
	       holder.message     =(TextView)  convertView. findViewById(R.id.Message);
	       holder.date        =(TextView)  convertView. findViewById(R.id.datenotify);

			setRobotoThinFont(holder.message,con);
			setRobotoThinFont(holder.message,con);

			holder.message.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
			holder.date.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
	       
		   convertView.setTag(holder);

	   } 
	  else
	  {
		  holder = (Holder) convertView.getTag();
	  }
		   String displaymessage =defaultdata.get(position).getMessage().replace("~"," ");
		  
		   try 
		   {
		     finalmessage =displaymessage.replace("*"," ");
		   } 
		   catch (Exception e1)
		   {
		     finalmessage =displaymessage;
		   }

			holder.message.setText(finalmessage);

		   try 
		   {
			setDate =defaultdata.get(position).getMessageInsertDateTime();
			holder.date.setText(setDate);
		   } catch (Exception e1) {}

		Link=defaultdata.get(position).getLink();
		if(Link!=null && Link.length()>0)
		{
			Picasso.with(con)
					.load(Link)
					.placeholder(R.drawable.notification_icon)
					.error(R.drawable.ic_launcher)
					.into(holder.notification_image);
		}

		   holder.message.setOnClickListener(new OnClickListener()
		   {
				
				@Override
				public void onClick(View v)
				{
						 
						String landing=defaultdata.get(position).getLandingPage();

					Intent resultIntent = Utils.redirectActivity(con,landing);
					con.startActivity(resultIntent);

				}
			});
		   holder.delete.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				String title=defaultdata.get(position).getBroadcastTitle();

				defaultdata.remove(id);
				notifyDataSetChanged();
			
				db.Open();
				db.DeleteNoti(setDate);
				db.close();
				
				try
				{
				BNotification.HbNoti.finish();
				}
				catch (Exception e) {
				
				}
				
				try
				{
					Intent i=new Intent(con,BNotification.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					con.startActivity(i);
					
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
		
		
		return  convertView;
	}
	
	// create holder class
	public class Holder{
		ImageView delete;
		TextView textBTitle,message,date;
		ImageView notification_image;
	}

}
