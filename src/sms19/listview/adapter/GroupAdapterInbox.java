
package sms19.listview.adapter;

import java.util.ArrayList;
import java.util.List;

import sms19.listview.database.DataBaseDetails;
import com.kitever.android.R;
import sms19.listview.newproject.model.GroupContactDetailModel;
import sms19.listview.newproject.model.GroupModelInboxDB;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

public class GroupAdapterInbox extends ArrayAdapter<GroupModelInboxDB> implements Filterable 
{

	private List<GroupModelInboxDB> planetList;
	private Context context;
	private Filter planetFilter;
	String Mobile,UserId;
	private List<GroupModelInboxDB> origPlanetList;
	DataBaseDetails db;
	static String gpname;
	public static String gpcount;
	
    private String InAppUserId="",InAppMobile= "",ResMObile="",Number_Group="",InAppPassword = "",InAppUserLogin = "";

	
	public GroupAdapterInbox(List<GroupModelInboxDB> planetList, Context ctx)
	{
		super(ctx, R.layout.listgroupinbox, planetList);
		
		this.planetList = planetList;
		this.context = ctx;
		this.origPlanetList = planetList;
	}
	
	public int getCount() {
		return planetList.size();
	}

	public GroupModelInboxDB getItem(int position)
	{
		return planetList.get(position);
	}

	public long getItemId(int position)
	{
		return planetList.get(position).hashCode();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
	
		PlanetHolder holder = new PlanetHolder();
		
		// First let's verify the convertView is not null
		if (convertView == null) 
		{
	    
		convertView=LayoutInflater.from(context).inflate(R.layout.listgroupinbox, null);
				
		final TextView tv       = (TextView)  convertView.findViewById(R.id.groupsnmcustomgp);
	    final CheckBox cbxGroup = (CheckBox)  convertView.findViewById(R.id.checkBoxGroupgp);
	    final TextView textCount= (TextView) convertView.findViewById(R.id.totalcountgp);
	   
	    db=new DataBaseDetails(context);
	    
	    gpname  =getItem(position).getName();
	    gpcount =getItem(position).getContactcount();
	    
//	    Log.w("TAG","GROUP_NAME_SERVICE:"+gpname+"GroupCount"+gpcount);
	    
	   
	    	try {
				db.Open();
				db.DeleteSelRegCnt();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			    	
	    	    
	    cbxGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
			
					final String ad=getItem(position).getName();
					gpcount =getItem(position).getContactcount();
				    gpcount =getItem(position).getContactcount();
					
				    int a=Integer.parseInt(gpcount);
				    
					int groupCount = 0;
					groupCount = Integer.parseInt(gpcount);
					
					FetchUid();
					
					fetchMobileandUserId();
				
					if(groupCount > 500){
						if(cbxGroup.isChecked())
						{
							
							Toast.makeText(context, "You can not select group more than 500 contact", Toast.LENGTH_SHORT).show();
							cbxGroup.setChecked(false);
						}
						else{
						}
						
					}
					else{

						if(isExistSelectedUserdata(ad)){
							db.DeleteSelRegisterContactChat(ad);

						}
						else{
							new webservice(null, webservice.GetGroupContact.geturl(UserId, ad), webservice.TYPE_GET, webservice.TYPE_GET_GROUP_CONTACT, new ServiceHitListener() {
								
								@Override
								public void onSuccess(Object Result,int id) {
									
							    GroupContactDetailModel model = (GroupContactDetailModel) Result;
								
							fetchMobileandUserId();
							
							String RegNumber ;
							String Regname  ;
							
							db.Open();
							
								for(int i=0 ;i<model.getGroupContactDetail().size();i++)
								{
									RegNumber = model.getGroupContactDetail().get(i).getContact_Mobile();
									Regname   = model.getGroupContactDetail().get(i).getContact_Name();
							
//									Log.w("gp","gp2 ::::::::before db add:::: success:::::(regNum):"+RegNumber+",Regname:"+Regname+",groupName:"+ad);
//									    db.addSelRegisterContactChat(InAppUserId,InAppUserLogin,InAppMobile,InAppPassword,"",RegNumber,ad,Regname);
					    
								}
						    db.close();
							}
								
								@Override
								public void onError(String Error, int id) {
									Toast.makeText(context, Error, Toast.LENGTH_SHORT).show();	
								}
							});
								
						}
					}
						
			}
		});
	    	    
	      
	    
		// Add layout in planetholder
	    holder.planetNameView = tv;
		holder.planetCountView = textCount;
		
		//set value in holder
		convertView.setTag(holder);
		}
		
		holder = (PlanetHolder) convertView.getTag();
		
		// get position for set value
		GroupModelInboxDB pp = planetList.get(position);
		
		// set data into layout
		holder.planetNameView.setText(pp.getName());
		holder.planetCountView.setText("("+pp.getContactcount()+")");		
		
		return convertView;
	}

	public void resetData() {
		planetList = origPlanetList;
	}
	
	
	/* *********************************
	 * We use the holder pattern        
	 * It makes the view faster and avoid finding the component
	 * **********************************/
	
	private static class PlanetHolder 
	{
		public TextView planetNameView;
		public TextView planetCountView;
	}
	
	/*
	 * We create our filter	
	 */
	
	@Override
	public Filter getFilter()
	{
		if (planetFilter == null)
			planetFilter = new PlanetFilter();
		
		return planetFilter;
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
				results.values = origPlanetList;
				results.count  = origPlanetList.size();
			}
			
			else {
				// We perform filtering operation
				List<GroupModelInboxDB> nPlanetList = new ArrayList<GroupModelInboxDB>();
				
				for (GroupModelInboxDB pp : planetList) 
				{
					if (pp.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
						nPlanetList.add(pp);
				}
				
				results.values = nPlanetList;
				results.count = nPlanetList.size();

			}
			
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			
			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();
			
			else {
				planetList = (List<GroupModelInboxDB>) results.values;
				notifyDataSetChanged();
			}
			
		}
		
	}
	public void FetchUid()
	{

		db.Open();
		  
		   Cursor c;
		   
		   c= db.getLoginDetails();
		   
		  				   
		   while(c.moveToNext()){
			   
			   Mobile   = c.getString(1);
			   UserId   = c.getString(3);
			 
			   
		   }
				   
		   db.close();

	}
	public void fetchMobileandUserId()
	{
				
		db.Open();
		  
		   Cursor c;
		   
		   c= db.getLoginDetails();
		   
		  				   
		   while(c.moveToNext())
		   {
			   
			InAppMobile    = c.getString(1);
			InAppUserId    = c.getString(3);
			InAppPassword  = c.getString(5);
			InAppUserLogin = c.getString(6); 
			   
		   }
				   
		   db.close();
	}
	
	public boolean isExistSelectedUserdata(String gpName) 
	{
		db.Open();
		  
		   Cursor c;
		   
		   c= db.getSelRegUserDetails();
		   
		   String sNum="";
		  				   
		   while(c.moveToNext())
		   {
			   
			   sNum = c.getString(6).trim();
			   
			   if(sNum.equalsIgnoreCase(gpName.trim())){
			   			
				  db.close();
				   
				   return true;
			   }
		   }
		
		   db.close();
		   
		   return false;
	}
	
}
