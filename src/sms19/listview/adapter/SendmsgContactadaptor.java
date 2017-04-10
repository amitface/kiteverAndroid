
package sms19.listview.adapter;

import java.util.List;

import sms19.listview.database.DataBaseDetails;
import com.kitever.android.R;
import sms19.listview.newproject.model.ContactModelInboxDB;
import android.content.Context;
import android.database.Cursor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SendmsgContactadaptor extends ArrayAdapter<ContactModelInboxDB> 
{
	private List<ContactModelInboxDB> planetList;
	private Context context;
    private String InAppUserId="",InAppMobile= "",ResMObile="",Number_Group="",InAppPassword = "",InAppUserLogin = "";
    private String number,Namec;
	
	DataBaseDetails dbObject;
	
	public SendmsgContactadaptor(List<ContactModelInboxDB> planetList, Context ctx)
	{
	
		super(ctx, R.layout.customsendmsgchatcontact, planetList );
		
		this.context        = ctx;
		this.planetList     = planetList;
			
	}
	
	
	public int getCount() 
	{
		if (planetList != null) {
			return planetList.size();
		} else {
			return 0;
		}
	}
	
	public ContactModelInboxDB getItem(int position)
	{
	return planetList.get(position);
	}

	public long getItemId(int position) 
	{
	return planetList.get(position).hashCode();
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		// First let's verify the convertView is not null
		if (convertView == null) {
			
		// This a new view we inflate the new layout
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView             = inflater.inflate(R.layout.customsendmsgchatcontact, null);
		
			// Now we can fill the layout with the right values
			TextView regCntName      = (TextView) convertView.findViewById(R.id.textViewNamechat);
			TextView regCntNumber    = (TextView) convertView.findViewById(R.id.textViewMObilechat);
			CheckBox regCntCheckbox  = (CheckBox) convertView.findViewById(R.id.checkBoxContactchat);
						
			number = planetList.get(position).getMobile();
			Namec  = planetList.get(position).getName();
			
			dbObject = new DataBaseDetails(context);
										
			regCntName.setText(Namec);
			regCntNumber.setText(number);
							
			// checkbox functionality...
			regCntCheckbox.setOnClickListener(new OnClickListener()
            {
				
			@Override
			public void onClick(View v)
			{
			
				try{
			
					String RegNumber   = planetList.get(position).getMobile();
				
					Namec  = planetList.get(position).getName();
					fetchMobileandUserId();
					
					if(isExistSelectedUserdata(RegNumber)){
						dbObject.Open();
						dbObject.DeleteSelRegisterContactChat(RegNumber);
						dbObject.close();
					}
					
					else{
					dbObject.Open();
				    dbObject.addSelRegisterContactChat(InAppUserId,InAppUserLogin,InAppMobile,InAppPassword,"",RegNumber,"NIL",Namec);
					dbObject.close();
					}
													
				}
				catch(Exception e){}
				
			}
			});
			
			
		}
		
		return convertView;
		
	}

	
	
	public void fetchMobileandUserId()
	{
		
		dbObject.Open();
		  
		   Cursor c;
		   
		   c= dbObject.getLoginDetails();
		   
		  				   
		   while(c.moveToNext())
		   {
			   
			InAppMobile    = c.getString(1);
			InAppUserId    = c.getString(3);
			InAppPassword  = c.getString(5);
			InAppUserLogin = c.getString(6); 
			   
		   }
				   
		   dbObject.close();
	}
	

	public boolean isExistSelectedUserdata(String number) 
	{
		dbObject.Open();
		  
		   Cursor c;
		   
		   c= dbObject.getSelRegUserDetails();
		   
		   String sNum="";
		  				   
		   while(c.moveToNext())
		   {
			   
			   sNum = c.getString(5).trim();
			   
			   if(sNum.equalsIgnoreCase(number.trim())){
			   			
				   dbObject.close();
				   
				   return true;
			   }
		   }
		
		   dbObject.close();
		   
		   return false;
	}
			
		
}
