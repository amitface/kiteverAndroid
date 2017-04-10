package sms19.listview.adapter;

import java.util.ArrayList;
import java.util.List;

import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.GetAllGroup;
import sms19.listview.newproject.GroupNewAdd;
import sms19.listview.newproject.NewContact;
import sms19.listview.newproject.model.DeleteGroup;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.newproject.model.GroupContactDetailModel;
import sms19.listview.newproject.model.contactmodel;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import com.kitever.android.R;
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



public class CustomDbGroupadapter extends ArrayAdapter<contactmodel> implements Filterable
{
DataBaseDetails db;
private Context con;
private int resource;
private List<contactmodel> data;
private List<contactmodel> orignaldata;
String Mobile,UserId;
private Filter GroupFilter;
static CheckBox cbxGroup;
String groupName;
String gpname, gpcount,totalcont="";

public CustomDbGroupadapter(Context context, int resource, List<contactmodel> objects) 
{
	    super(context, resource, objects);
		this.con=context;
		this.resource=resource;
		this.data=objects;
		this.orignaldata=objects;
		//Log.w("TAG::::::::::::::::::::","objects_size:"+objects.size());
	
	}
    @Override
	public int getCount()
    {
    try
    {
	return data.size();
    }
    catch(Exception e)
    {
    return 0;
    }
	}
    @Override
	public contactmodel getItem(int position)
    {
	return data.get(position);
	}
    @Override
    public long getItemId(int position)
    {
    return super.getItemId(position);
    }
    
@Override
public View getView( final int position, View convertView, ViewGroup parent) 
{
	
	GroupDataHolder holder = new GroupDataHolder();
	if(convertView==null)
	{
	 convertView=LayoutInflater.from(con).inflate(resource, null);
	
	
    db=new DataBaseDetails(con);
	TextView tv        = (TextView) convertView.findViewById(R.id.groupsnmcustom);
	TextView total=(TextView)convertView.findViewById(R.id.totalcount);
    ImageView add      = (ImageView) convertView.findViewById(R.id.groupcustom);
    ImageView delete   = (ImageView) convertView.findViewById(R.id.groupcustom2);
    cbxGroup          = (CheckBox)  convertView.findViewById(R.id.checkBoxGroup);
    ImageView addmore= (ImageView) convertView.findViewById(R.id.addtogpcont);
    TextView textCount= (TextView) convertView.findViewById(R.id.totalcountgroups);
//    Log.e("Tag","getItem(position).getContactcount()"+getItem(position).getContactcount());
	
		
	try
	{
    gpname=getItem(position).getName();
    gpcount=getItem(position).getContactcount();
	}
	catch(Exception ed){}
	
     
    db=new DataBaseDetails(con);
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
	 
		i.putExtra("TotalCountGroup",gpcount);
//		Log.e("Tag","getItem(position).getContactcount()"+getItem(position).getContactcount());
		i.putExtra("groupname",getItem(position).getName());
		getContext().startActivity(i);
		
		}
	});
    
      
    //Log.w("TAG","GROUP_NAME_DB:"+groupName);
    
    cbxGroup.setOnClickListener(new OnClickListener()
    {
		
		@Override
		public void onClick(View v) 
		{
			//Log.w("TAG","****************GP_NAME:"+position+","+data.get(position).getName());
				
	
			String ad=getItem(position).getName();
		    gpcount=getItem(position).getContactcount();
		    int a=Integer.parseInt(gpcount);
			FetchUid();
			
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
	    
    add.setOnClickListener(new OnClickListener() 
    {
		
		@Override
		public void onClick(View v)
		{
							
					Intent i=new Intent(con,GetAllGroup.class);
					i.putExtra("groupname",getItem(position).getName());
					
					con.startActivity(i);	
	
					
		}
	});
    delete.setOnClickListener(new OnClickListener() 
    {
		
		@Override
		public void onClick(View v)
		{
			
			new AlertDialog.Builder(con)
			.setCancelable(false)
			.setMessage("Are you sure you want to Delete Group?")
			.setPositiveButton("YES", new DialogInterface.OnClickListener() 
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					final String DeletegroupName=getItem(position).getName();
					
					data.remove(position);
					notifyDataSetChanged();
			
					
					FetchUid();
					//changes required
					new webservice(null, webservice.DeleteGroupAll.geturl(UserId,DeletegroupName), webservice.TYPE_GET, webservice.TYPE_DELETE_GROUP, new ServiceHitListener() {

						
						@Override
						public void onSuccess(Object Result, int id)
						{
							
							try{
								db.Open();
								db.DeleteGroupNameBasedOnId(DeletegroupName);	
								db.close();
							}
							catch(Exception e3d){}
							DeleteGroup model=(DeleteGroup) Result;
							if(model.getDeleteContactGroupRegistration().get(0).getMsg().length()>0)
							{
							Toast.makeText(getContext(), ""+model.getDeleteContactGroupRegistration().get(0).getMsg(),Toast.LENGTH_SHORT).show();
							}
							else
							{
							Toast.makeText(getContext(), ""+model.getDeleteContactGroupRegistration().get(0).getError(),Toast.LENGTH_SHORT).show();
							}
							
							FetchUid();
						    
							 
					     	new webservice(null, webservice.GetAllGroupDetails.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_GET_GROUP_DETAILS, new ServiceHitListener() {
							
							@Override
							public void onSuccess(Object Result, int id) 
							{
							    
								FetchGroupDetails Model  = (FetchGroupDetails) Result;
								
								db.Open();
								db.DeleteGroupDataALL();
								db.close();
								
								for(int i=0;i<Model.getGroupDetails().size();i++)
								{
									FetchUid();
									String gpname =Model.getGroupDetails().get(i).getGroup_name();
									String gpCount=Model.getGroupDetails().get(i).getNoOfContacts();
									
									db.Open();
									db.addGroupAll(UserId, gpname , gpCount);
									db.close();
								}
							
							}
							
							@Override
							public void onError(String Error, int id) 
							{
								
							}
						    });	
							
							
							
							
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
    
    holder.setTextViewGroup  = tv;
    holder.setTextViewCount  = textCount;
    convertView.setTag(holder);
	
}
	holder = (GroupDataHolder) convertView.getTag();
	
	contactmodel pp = data.get(position);
	holder.setTextViewGroup.setText(pp.getName());
	holder.setTextViewCount.setText("("+pp.getContactcount()+")");
				
	return convertView;
}

public void FetchUid()
{

	db.Open();
	  
	   Cursor c;
	   
	   c= db.getLoginDetails();
	   
	  				   
	   while(c.moveToNext())
	   {
		   
		   Mobile   = c.getString(1);
		   UserId   = c.getString(3);
		  
	   }
			   
	   db.close();

}
public static void CheckedTrueAll()
{
if(NewContact.checkAll==true)
	cbxGroup.setChecked(true); 

else
{
	cbxGroup.setChecked(false); 

}
}
public void resetGroupData()
{
	data = orignaldata;
}

private static class GroupDataHolder 
{
	public TextView setTextViewGroup;
	public TextView setTextViewCount;
}

@Override
public Filter getFilter()
{
	if (GroupFilter == null)
	GroupFilter = new GroupFilter();
	
	return GroupFilter;
}


private class GroupFilter extends Filter 
{
	
@Override
protected FilterResults performFiltering(CharSequence constraint) 
{
	
	FilterResults results = new FilterResults();
	
	// We implement here the filter logic
	if (constraint == null || constraint.length() == 0) 
	{
		// No filter implemented we return all the list
		results.values = orignaldata;
		results.count  = orignaldata.size();
	}
	
	else
	 {
		// We perform filtering operation
		List<contactmodel> nPlanetList = new ArrayList<contactmodel>();
		
		for (contactmodel pp : data)
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
protected void publishResults(CharSequence constraint,FilterResults results) 
{
	
	// Now we have to inform the adapter about the new list filtered
	if (results.count == 0)
		notifyDataSetInvalidated();
	
	else 
	{
		data = (List<contactmodel>) results.values;
		notifyDataSetChanged();
	}
	
}

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
