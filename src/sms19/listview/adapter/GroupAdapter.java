
package sms19.listview.adapter;

import java.util.ArrayList;
import java.util.List;

import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.GetAllGroup;
import sms19.listview.newproject.GroupNewAdd;
import sms19.listview.newproject.NewContact;
import com.kitever.android.R;
import sms19.listview.newproject.model.DeleteGroup;
import sms19.listview.newproject.model.FetchGroupDetails.GroupDetails;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupAdapter extends ArrayAdapter<GroupDetails> implements Filterable 
{

	private List<GroupDetails> planetList;
	private Context context;
	private Filter planetFilter;
	String Mobile,UserId;
	private List<GroupDetails> origPlanetList;
	DataBaseDetails db;
	String gpname, gpcount,totalcont="";
	
	public GroupAdapter(List<GroupDetails> planetList, Context ctx)
	{
		super(ctx, R.layout.customlistofgroups, planetList);
		
		this.planetList = planetList;
		this.context = ctx;
		this.origPlanetList = planetList;
	}
	
	public int getCount() {
		return planetList.size();
	}

	public GroupDetails getItem(int position) {
		return planetList.get(position);
	}

	public long getItemId(int position) {
		return planetList.get(position).hashCode();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
	
		PlanetHolder holder = new PlanetHolder();
		
		// First let's verify the convertView is not null
		if (convertView == null) 
		{
			 convertView=LayoutInflater.from(context).inflate(R.layout.customlistofgroups, null);
				
		final TextView tv = (TextView)  convertView.findViewById(R.id.groupsnmcustom);
	    ImageView add     = (ImageView) convertView.findViewById(R.id.groupcustom);
	    ImageView delete  = (ImageView) convertView.findViewById(R.id.groupcustom2);
	    final CheckBox cbxGroup = (CheckBox)  convertView.findViewById(R.id.checkBoxGroup);
	    ImageView addmore= (ImageView) convertView.findViewById(R.id.addtogpcont);
	    final TextView textCount= (TextView) convertView.findViewById(R.id.totalcountgroups);
	   
	    db=new DataBaseDetails(context);
	    
	    gpname  =getItem(position).getGroup_name();
	    gpcount =getItem(position).getNoOfContacts();
//		Log.e("Tag",getItem(position).getNoOfContacts());
//	    Log.w("TAG","GROUP_NAME_SERVICE:"+gpname+"GroupCount"+gpcount);
	  	    	
	    addmore.setOnClickListener(new OnClickListener()
	    {
			
			@Override
			public void onClick(View v)
			{
				try 
				{
				GroupNewAdd.contactsGroupFlag.finish();
				} 
				catch (Exception e) 
				{
				e.printStackTrace();
				}
		
			Intent i=new Intent(getContext(),NewContact.class);
			i.putExtra("fromaddgp", "groupadd");
			i.putExtra("TotalCountGroup", gpcount);
			Log.e("Tag1111111",getItem(position).getNoOfContacts());
			i.putExtra("groupname",getItem(position).getGroup_name());
			getContext().startActivity(i);
			
			}
		});  
	    
	    cbxGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
					String ad=getItem(position).getGroup_name();
				    gpcount =getItem(position).getNoOfContacts();
					FetchUid();
					  int a=Integer.parseInt(gpcount);
					if(checkResGroup(ad)){
						db.Open();
					  	db.deleteSelectedGroup(ad);
						db.close();
					}
					else{
						db.Open();
					  	db.addSelectedGroupName(ad,UserId,a);
						db.close();
					}
										
			}
		});
	    	    
	    add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				
				Intent i=new Intent(context,GetAllGroup.class);
				i.putExtra("groupname",getItem(position).getGroup_name());
			    context.startActivity(i);	

			}
		});
	    delete.setOnClickListener(new OnClickListener()
	    {
			
			@Override
			public void onClick(View v)
			{
				new AlertDialog.Builder(context)
				.setCancelable(false)
				.setMessage("Are you sure to you want to Delete Group?")
				.setPositiveButton("YES", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					
						String DeletegroupName=getItem(position).getGroup_name();
						
						planetList.remove(position);
						
						notifyDataSetChanged();
						
						FetchUid();
						
						new webservice(null, webservice.DeleteGroupAll.geturl(UserId,DeletegroupName), webservice.TYPE_GET, webservice.TYPE_DELETE_GROUP, new ServiceHitListener() {

							
							@Override
							public void onSuccess(Object Result, int id)
							{
							try{
								db.Open();
								db.DeleteGroupNameBasedOnId(getItem(position).getGroup_name());	
								db.close();
							}
							catch(Exception dr){}
							
							DeleteGroup model = (DeleteGroup) Result;
							
							try {
								if(model.getDeleteContactGroupRegistration().get(0).getMsg().length()>0){
									
									Toast.makeText(context, ""+model.getDeleteContactGroupRegistration().get(0).getMsg(), Toast.LENGTH_SHORT).show();

								}
								else{
									Toast.makeText(context, ""+model.getDeleteContactGroupRegistration().get(0).getError(), Toast.LENGTH_SHORT).show();

								}
							} catch (Exception e) {
							
							}
							
							}
							
							@Override
							public void onError(String Error, int id) 
							{
								
								
							}
						});
					}
				})
				.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.show();
				
			
			
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
		GroupDetails pp = planetList.get(position);
		
		// set data into layout
		holder.planetNameView.setText(pp.getGroup_name());
		holder.planetCountView.setText("("+pp.getNoOfContacts()+")");		
		
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
				List<GroupDetails> nPlanetList = new ArrayList<GroupDetails>();
				
				for (GroupDetails pp : planetList) 
				{
					if (pp.getGroup_name().toUpperCase().startsWith(constraint.toString().toUpperCase()))
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
				planetList = (List<GroupDetails>) results.values;
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
	public boolean checkResGroup(String gpName) 
	{
		db.Open();
		  
		   Cursor c;
		   
		   c= db.getSelectedGroupName();
		   
		   String GROUPNAME;
		  				   
		   while(c.moveToNext()){
			   
			   GROUPNAME   = c.getString(0).trim();
			   
			   if(gpName.trim().equals(GROUPNAME))
			   {
				   db.close();
				   
				   return true;
			   }
			 			   
		   }
		
		   db.close();
		   
		   return false;
	}
	
}
