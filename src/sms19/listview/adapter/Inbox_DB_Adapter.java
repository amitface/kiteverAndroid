package sms19.listview.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sms19.listview.database.BadgeView;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.Inbox;
import sms19.listview.newproject.Inboxreadmsg;
import com.kitever.android.R;
import sms19.listview.newproject.model.InboxModelRead;
import sms19.listview.newproject.model.UpdateContactModel;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Inbox_DB_Adapter extends BaseAdapter implements Filterable {
	Context cnt;
	BadgeView badge1;
	private int RESOURCE;
	private List<InboxModelRead> Data2;
	private List<InboxModelRead> Data;
	String cutmsgDB="",msgboxDB="",timeDB="",numberDB="",numberDBn="",msgStatusDB="", SenderName="",sender="",senid="";
	private Filter planetFilter;
	String UserId = "",name="", Cname="",Mobile="";
	
	DataBaseDetails dbObj; 

	public Inbox_DB_Adapter(Context context, int resource, List<InboxModelRead> data) 
	{

		this.cnt      = context;
		this.RESOURCE = resource;
		this.Data     = data;
		this.Data2    = data; 
		//Log.w("TAG","DATASIZE:::::::::::::::::::::::::Data:"+Data.size());
	}

	public InboxModelRead getItem(int position)
	{
	return null;
	}

	
	@Override
	public int getCount()
	{
	return Data.size();
	}

	public void resetData()
	{
	Data = Data2;
	}
	
	
	public long getItemId(int position) 
	{
	return 0;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		
		PlanetHolder holder = new PlanetHolder();
		
		if(convertView == null)
		{
		
			LayoutInflater inflate = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(RESOURCE, null);
			
			// Now we can fill the layout with the right values
			TextView txtReceiveNumberDB = (TextView) convertView.findViewById(R.id.inboxName);
			TextView txtReceiveMsgDB    = (TextView) convertView.findViewById(R.id.inboxRecipentMsg);
			TextView txtReceiveTimeDB   = (TextView) convertView.findViewById(R.id.inboxTime);
	        TextView textReceiveStatus  = (TextView) convertView.findViewById(R.id.textTotalCount);
			
			LinearLayout lin            = (LinearLayout) convertView.findViewById(R.id.linreadlist);
			
			ImageView img               = (ImageView) convertView.findViewById(R.id.imageProfileAdapter);
		
			ImageView imgDelete         = (ImageView) convertView.findViewById(R.id.imageInboxListDelete);
			
			 try
			 {
				String senprofile=(Data.get(position).getInMobile());
				//Log.w("senprofile picture", "senprofile picture"+senprofile);
				String imagePath = Environment.getExternalStorageDirectory().toString() + "/ChatPeoplePic"+senprofile+".jpg";
				//Log.w("imagePath picture", "imagePath picture"+imagePath);
				File f = new File(imagePath);
					
				if(f.exists())
				{
					//Log.w("profile picture", "profile picture"+f.toString());
					try 
					{
					img.setImageDrawable(Drawable.createFromPath(imagePath));
											
					}
					catch(Exception e){}
					
				}
				else
				{
					//img.setBackgroundResource(R.drawable.ic_launcher);
				}
				} 
			 catch (Exception e) {}
			 
		    msgStatusDB = (Data.get(position).getInStatus().trim());
			numberDB    = (Data.get(position).getInMobile().trim());
		//	img.setD
			try {
				timeDB      = (Data.get(position).getInTime().trim());
				}
			catch (Exception e2) {
				e2.printStackTrace();
			}
			
			msgboxDB    = (Data.get(position).getInMsg().trim());
			SenderName  = (Data.get(position).getSenderID());
			
			holder.ReceiveNumberDB =txtReceiveNumberDB;
			holder.ReceiveMsgDB    =txtReceiveMsgDB;
			holder.ReceiveTimeDB   =txtReceiveTimeDB;
			holder.ReceiveStatus   =textReceiveStatus;
			
			convertView.setTag(holder);
					
			dbObj = new DataBaseDetails(cnt);
		
			try
			{
			cutmsgDB = msgboxDB.substring(0,15)+"...";
			} 
			catch (Exception e)
			{
			cutmsgDB=msgboxDB;
			}
			
			
			int myNum = 0;

			try {
			    myNum = Integer.parseInt(msgStatusDB);
			} catch(NumberFormatException nfe) {
			    myNum = 0;
//			   System.out.println("Could not parse " + nfe);
			} 
			
			if(myNum != 0)
			{
				badge1 = new BadgeView(cnt, textReceiveStatus);
		    	badge1.setText(""+myNum);
		    	badge1.setBadgeBackgroundColor(Color.parseColor("#A4C639"));
		        badge1.show();
		    	
				//textReceiveStatus.setText(""+myNum);
			}
			
			   callFxnToPullNameFromContactList(position);
			   //Log.w("Cname here to set data","Cname"+Cname+"numberDBn"+numberDBn);
				txtReceiveNumberDB.setText(numberDBn);
				txtReceiveMsgDB.setText(cutmsgDB);
				txtReceiveTimeDB.setText(timeDB);
			
			
			lin.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					
					try 
					{
						numberDB    = (Data.get(position).getInMobile().trim());
						
						dbObj.Open();
						dbObj.updateInboxCountmsg(numberDB, "0");
						dbObj.close();
						
					} 
					catch (Exception e1) 
					{
					e1.printStackTrace();
					}
															
					
                    try 
                    {
						numberDB  = (Data.get(position).getInMobile().trim());
						
						String senderID = fetchSenderId(numberDB).trim();


						if(senderID.length()>0 )
						{
							sender = senderID;
						}
					} catch (Exception e) {}
					
						 
						numberDB  =(Data.get(position).getInMobile().trim());
						try
						{
							timeDB    =(Data.get(position).getInTime().trim());
						} 
						catch (Exception e) {
						}
						
						msgboxDB  =(Data.get(position).getInMsg().trim());
				        //name      =(Data.get(position).getName().trim());
					    senid     =(Data.get(position).getSenderID());
					    
					    callFxnToPullNameFromContactList(position);
					    
					  	/*if(!sender.trim().equalsIgnoreCase("NIL")){*/
							Intent i=new Intent(cnt,Inboxreadmsg.class);
							i.putExtra("Name", numberDBn);
							i.putExtra("Number", numberDB);
							i.putExtra("recipientid",sender);
							cnt.startActivity(i);
						
						//}
						/*else{
							Intent i=new Intent(cnt,Inboxreadmsg.class);
							i.putExtra("Name", numberDBn);
							i.putExtra("Number", numberDB);
							i.putExtra("recipientid","");
							cnt.startActivity(i);
						}*/
				}
			});
			
			imgDelete.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) {

					new AlertDialog.Builder(cnt)
					.setCancelable(false)
					.setMessage("Are you sure to Delete Contact?")
					.setPositiveButton("YES", new DialogInterface.OnClickListener() 
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							numberDB    = (Data.get(position).getInMobile().trim());
							
							String senderID = fetchSenderId(numberDB).trim();
							
							try 
							{
							//Log.w("TAG","SENDERID_Lemgth:"+senderID.length());
							//Log.w("TAG","SENDERID:"+senderID);
							}
							catch (Exception e)
							{
							e.printStackTrace();
							}
							
							if(senderID.length()>0)
							{
								
							//Log.w("TAG","SENDERID__111111111111111111(IF)");
							callForRegisterUser(position);
							}
							else
							{
								
								//Log.w("TAG","SENDERID__222222222222222222(ELSE)");
								
								try
								{
								dbObj.Open();
								dbObj.deleteInboxItem(SenderName);
								dbObj.close();
								
								}
								catch(Exception fd){}
								
							}
						
						}

						private void callForRegisterUser(final int position)
						{
							
							fetchMobileandUserId();
							
							numberDB    = (Data.get(position).getInMobile().trim());
							SenderName  = (Data.get(position).getSenderID().trim());
							
							try
							{
							dbObj.Open();
							dbObj.deleteInboxItem(SenderName);
							dbObj.close();
							
							}
							catch(Exception fd){}
							new webservice(null, webservice.DeleteIndividualContact.geturl(UserId, numberDB), webservice.TYPE_GET, webservice.TYPE_DELETE_SINGLE_CONTACT, new ServiceHitListener() {
								
								@Override
								public void onSuccess(Object Result, int id)
								{
									try
									{
									dbObj.Open();
									dbObj.DeleteIndividuleContact(numberDB);
									dbObj.close();
									
									}
									catch(Exception fd){}
									UpdateContactModel mod=(UpdateContactModel) Result;
									if(mod.getDeleteContactReview().get(0).getMsg().length()>0)
									{
										Toast.makeText(cnt, ""+mod.getDeleteContactReview().get(0).getMsg(), Toast.LENGTH_SHORT).show();
									}
									else
									{
									    Toast.makeText(cnt, ""+mod.getDeleteContactReview().get(0).getError(), Toast.LENGTH_SHORT).show();
									}
								}
								
								@Override
								public void onError(String Error, int id) {
									
									Toast.makeText(cnt, ""+Error, Toast.LENGTH_SHORT).show();
								}
							});
						}
					})
					.setNegativeButton("NO", new DialogInterface.OnClickListener() 
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
						dialog.cancel();
						}
					})
					.show();
					
			
					
				}
			});
			
		//to be checked ****************************************************************
			  try 
			  {
				 /*	numberDB    = (Data.get(position).getInMobile().trim());
					
					String senderID = fetchSenderId(numberDB).trim();
			     
					if(senderID.length()>0 )
					{
					//imgNotReg.setBackgroundResource(R.drawable.ic_launcher);
					}
					else 
					{
					imgNotReg.setBackgroundResource(R.drawable.a);
					}*/
			 } 
			  
			  catch (Exception e)
			  {
			  e.printStackTrace();
			  }
			
		    }
		
	
			holder = (PlanetHolder) convertView.getTag();
		   callFxnToPullNameFromContactList(position);
		    InboxModelRead pp = Data.get(position);
			holder.ReceiveNumberDB.setText(numberDBn);
			holder.ReceiveMsgDB.setText(pp.getInMsg());
			holder.ReceiveTimeDB.setText(pp.getInTime());
			//holder.ReceiveStatus.setText(pp.getInStatus());
			
			
		
		
		return convertView;
	}
	public static class PlanetHolder
	{
		TextView ReceiveNumberDB;
		TextView ReceiveMsgDB;
		TextView ReceiveTimeDB;
        TextView ReceiveStatus;
	}
	private void callFxnToPullNameFromContactList(final int position)
	{
		// fetch name from contact list**********************************
		numberDB    = (Data.get(position).getInMobile().trim());
		fetchContactName(numberDB);
		//Log.w("Cname","Cname"+Cname);
		if(Cname.length()>0)
		{
			//Log.w("Cname if numberDBn","Cname"+Cname+"numberDBn"+numberDBn);
			numberDBn = Cname;
		}	
		else
		{
			//Log.w("Cname else numberDBn","Cname"+Cname+"numberDBn"+numberDBn);
			numberDBn    = (Data.get(position).getInMobile().trim());
		}
		//***************************************************************
	}
	
      
      public String fetchSenderId(String mobile)
      {
  		
  		dbObj.Open();
  			
  			Cursor c;
  			
  			c= dbObj.getInboxDetails();
  			
  			String data= "";
  			String fetchSender = "";
  			
  			while(c.moveToNext()){
  				
  			    data = c.getString(4).trim();
  			   
  				if(data.equalsIgnoreCase(mobile.trim())){
  					
  					fetchSender = c.getString(6).trim();
  					
  			    	dbObj.close();
  					return fetchSender;
  				}
  			
  				
  			}
  			
  			dbObj.close();
  			return fetchSender;
  		}
	
      public void fetchMobileandUserId() 
  	{

  		dbObj.Open();
  		  
  		   Cursor c;
  		   
  		   c= dbObj.getLoginDetails();
  		   
  		  				   
  		   while(c.moveToNext())
  		   {
  			   
  			   Mobile = c.getString(1);
  			   UserId = c.getString(3);
  			   
  		   }
  				   
  		 dbObj.close();
  	  
  		
  	}
      
      public void fetchContactName(String number) 
    	{
    		dbObj.Open();
    		  
    		   Cursor c;
    		   
    		   c= dbObj.SearchIndividuleContact(number);
    		  
    		   Cname="";
    		  				   
    		   while(c.moveToNext())
    		   {
    			   
    			   Cname = c.getString(2).trim();
    			   //Log.w("Contact name", "Contact Name"+Cname);
    		   }
    				   
    		   dbObj.close();
    	}

	
      
	@Override
	public Filter getFilter()
	{
	if (planetFilter == null)
			
	planetFilter = new PlanetFilter();
		
	return  planetFilter;
	}
	
	private class PlanetFilter extends Filter
	{
		
	@Override
    protected FilterResults performFiltering(CharSequence constraint)
	{
			
	FilterResults results = new FilterResults();
			
	// We implement here the filter logic
	if (constraint == null || constraint.length() == 0)
	{
	// No filter implemented we return all the list
	results.values = Data2;
	results.count  = Data2.size();
	}
			
			else
			{
				// We perform filtering operation
				List<InboxModelRead> nPlanetList = new ArrayList<InboxModelRead>();
				
				for (InboxModelRead pp : Data)
				{
			
				if (pp.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
				nPlanetList.add(pp);
				}
				for(InboxModelRead p : Data)
				{
				if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
				nPlanetList.add(p);
				}
				results.values = nPlanetList;
				results.count = nPlanetList.size();

			}
			
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,FilterResults results) 
		{
			
			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();
			
			else 
			{
				Data = (List<InboxModelRead>) results.values;
				notifyDataSetChanged();
			}
			
		}
		
	}  
      
      
      
      
      
      
}
