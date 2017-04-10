package sms19.listview.adapter;

import java.util.ArrayList;
import java.util.List;

import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.BNotification;
import sms19.listview.newproject.ContactAdd;
import sms19.listview.newproject.GetAllGroup;
import sms19.listview.newproject.Home;
import sms19.listview.newproject.NewContact;
import sms19.listview.newproject.model.UpdateContactModel;
import sms19.listview.newproject.model.contactmodelIndividule;
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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class customAdaptorContactDataBASE extends BaseAdapter implements Filterable
{
	public static CheckBox cbx;
	private Context context;
	private int resource;
	private List<contactmodelIndividule> planetList;
	private List<contactmodelIndividule> origPlanetList;
	private Filter planetFilter;
	String nameGroup;
	String numberGroup;	
/*	String number;*/
	
	String ALLdata ;
	String Namec,number;
	 String mobNumber="";
	 
	 public static boolean statusCheckAll = false;
     private Context con;

	DataBaseDetails dbObj;
	public static boolean deselectselectall=false;
	private String UserId="",ResMObile="",Number_Group="";
	
	
	static List<contactmodelIndividule> gCheckBoxDetails = new ArrayList<contactmodelIndividule>();
	
	public customAdaptorContactDataBASE (Context context, int Resource, List<contactmodelIndividule> data){
		this.context     = context;
		this.planetList = data;
		this.resource    = Resource;
		this.origPlanetList     = data;
	}
	
	@Override
	public int getCount() {
	try
	{
		return planetList.size();
	}
	catch(Exception e)
	{
		return 0;
	}
	}

	@Override
	public contactmodelIndividule getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		
        View v = convertView;
		
		PlanetHolder holder = new PlanetHolder();
		
		if(convertView == null){
			
			// This a new view we inflate the new layout
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.customlistforcontacts, null);
		
			// Now we can fill the layout with the right values
			TextView tv = (TextView) v.findViewById(R.id.textViewName);
			TextView tvMobile = (TextView) v.findViewById(R.id.textViewMObile);
			
			 cbx=(CheckBox) v.findViewById(R.id.checkBoxContact);
			ImageView add=(ImageView)v.findViewById(R.id.editContactImage);
			ImageView delete=(ImageView)v.findViewById(R.id.deleteContactImage);
			if(GetAllGroup.fromgroup==true)
			{
			try
			{
		   // //Log.w("@@@SFromGroup", "boolean value"+Contacts.fromgroup);
			tv.setText(getItem(position).getName());
			tvMobile.setText(getItem(position).getMobile());
			//Contacts.fromgroup=false;
			
			}
			catch(Exception e)
			{	
			}
			}
		
			dbObj = new DataBaseDetails(context);
			try
			{
			ALLdata       = planetList.get(position).getName();
			
			Namec   = ALLdata.substring(0,ALLdata.indexOf(","));
			number  = ALLdata.substring(ALLdata.indexOf(",")+1);
			}
			catch(Exception e){}
			
			if(GetAllGroup.fromgroup)
			{
				mobNumber= fetchGroupContact();
				if(number.equals(mobNumber))
				{
				cbx.setChecked(true);
				GetAllGroup.fromgroup=false;
				}
			}
			
          	try
          	{
			 nameGroup=getItem(position).getName();
			 numberGroup=getItem(position).getMobile();	
          	}
          	catch(Exception e)
          	{
          		
          	}
			add.setOnClickListener(new OnClickListener() 
			{
					
					@Override
					public void onClick(View v)
					{
					
								//Log.w("TAG"," Inside ONCLICK:"+context);
								
								String cname   = planetList.get(position).getName();
								String dob     =planetList.get(position).getDOB();
								String anivr   =planetList.get(position).getAniversary();
								String Name   = cname.substring(0,cname.indexOf(","));
								String Mobile = cname.substring(cname.indexOf(",")+1);
			                  	String emailId=	planetList.get(position).getEmailId();			
								Intent i = new Intent(context,ContactAdd.class);
								
								i.putExtra("editname",Name);
			                    i.putExtra("editNumber",Mobile);
			                    i.putExtra("updateService","updateService");
			                    i.putExtra("emailId",emailId);
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
						.setMessage("Are you sure to Delete Contact?")
						.setPositiveButton("YES", new DialogInterface.OnClickListener() 
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								
								
								fetchMobileandUserId();
								
								String cname   = planetList.get(position).getName();
								//Log.w("@@!@@@", "#####"+cname);
								String Mobile = cname.substring(cname.indexOf(",")+1);
								
								contactmodelIndividule a=planetList.remove(position);
								notifyDataSetChanged();
								try
								{
								dbObj.Open();
								dbObj.DeleteIndividuleContact(number);
								dbObj.close();
								
								}
								catch(Exception fd){}
								new webservice(null, webservice.DeleteIndividualContact.geturl(UserId, Mobile), webservice.TYPE_GET, webservice.TYPE_DELETE_SINGLE_CONTACT, new ServiceHitListener() {
									
									@Override
									public void onSuccess(Object Result, int id)
									{
										UpdateContactModel mod=(UpdateContactModel) Result;
										
										try {
											if(mod.getDeleteContactReview().get(0).getMsg().length()>0)
											{
												Toast.makeText(context, ""+mod.getDeleteContactReview().get(0).getMsg(), Toast.LENGTH_SHORT).show();
											}
											else
											{
											Toast.makeText(context, ""+mod.getDeleteContactReview().get(0).getError(), Toast.LENGTH_SHORT).show();
													
											}
										} catch (Exception e) {
									 		
										}
										
																		
									}
									
									@Override
									public void onError(String Error, int id) {
										
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
			 
			holder.planetNameView    = tv;
			holder.planetContactView = tvMobile;
			
			v.setTag(holder);
					
			try
			{
			if(NewContact.checkAll)
			{
		    cbx.setChecked(true);
		    
		    statusCheckAll= true;
		    
				    
		        }
			else
			{ 
				cbx.setChecked(false);
				statusCheckAll= false;
			}
			}
			catch(Exception gh){}
			
			fetchMobileandUserId();
			
			cbx.setOnClickListener(new OnClickListener()
            {
				
			@Override
			public void onClick(View v)
			{
			
				
				try
				{
				//chechuncheckall();
					//notifyDataSetChanged();
					try
					{
						ALLdata = planetList.get(position).getName();
						Namec   = ALLdata.substring(0,ALLdata.indexOf(","));
						number  = ALLdata.substring(ALLdata.indexOf(",")+1);
					
					}
						catch(Exception e){}
				
					if(checkResnumber(number))
				    {
						/*try {
							NewContact.cbxselect.setChecked(false);
						} catch (Exception e) {
						}
						*/
						dbObj.Open();
				        dbObj.deleteNumberFromRecipient(number);
				        dbObj.close();
					}
					else{
						dbObj.Open();
					    dbObj.addReceipent(UserId, number, Namec);
					    dbObj.close();
					}
							
					if(!checkAllContact(number))
					{
					dbObj.Open();
					dbObj.addSelectedContactsFromGroup(Namec, number,UserId);			
					dbObj.close();
					
					}
					
					else{
						
						dbObj.Open();
					    dbObj.deleteNumberFromSelectedContacts(number);
					    dbObj.close();
					}
				}
				catch(Exception e){}
				//}
				
			}
			});
			
			
		
		}
		
		holder = (PlanetHolder) v.getTag();
		contactmodelIndividule pp = planetList.get(position);
		String DATA=pp.getName();
		try
		{
		String Name   = DATA.substring(0,DATA.indexOf(","));
		String Mobile = DATA.substring(DATA.indexOf(",")+1);
		holder.planetNameView.setText(Name);
		holder.planetContactView.setText(Mobile);
		}
		catch(Exception e){}
		
				
	
		return v;
	}

	
	private static class PlanetHolder
	{
	public TextView planetNameView;
	public TextView planetContactView;
	}
	
	
	public void resetData(){
		planetList = origPlanetList;
		}
	
	@Override
	public Filter getFilter() {
		
//		Log.e("TAG","WELCOME GETFILTER:"+planetFilter);
		
		if (planetFilter == null)
		planetFilter = new PlanetFilter();
		
		return planetFilter;
	}
	
	private class PlanetFilter extends Filter{
		
	@Override
    protected FilterResults performFiltering(CharSequence constraint)
	{
			
//		Log.e("TAG","WELCOME TO SEARCH METHOD:CONSTRAINT"+constraint);
		
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
				List<contactmodelIndividule> nPlanetList = new ArrayList<contactmodelIndividule>();
				
				for (contactmodelIndividule pp : planetList) 
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
				planetList = (List<contactmodelIndividule>) results.values;
				notifyDataSetChanged();
			}
			
		}
		
	}
	
	public void fetchUserId() 
	{
		dbObj.Open();
		  
		   Cursor c;
		   
		   c= dbObj.getLoginDetails();
		   
		  				   
		   while(c.moveToNext()){
			   
			   UserId   = c.getString(3);
			 			   
		   }
				   
		   dbObj.close();
	}

	public boolean checkResnumber(String number) 
	{
		dbObj.Open();
		  
		   Cursor c;
		   
		   c= dbObj.getReceipent();
		   
		  				   
		   while(c.moveToNext()){
			   
			   ResMObile   = c.getString(1).trim();
			   
			   if(number.trim().equals(ResMObile))
			   {
				   dbObj.close();
				   
				   return true;
			   }
			 			   
		   }
		
		   dbObj.close();
		   
		   return false;
	}
	
	public boolean checkAllContact(String number) 
	{
		dbObj.Open();
		  
		   Cursor c;
		   
		   c= dbObj.getSelectedGroupContacts();
		   
		  	if(c.getCount()>=0)		
		  	{
		  		//Log.w("getcoint", "22222"+getCount());
		   while(c.moveToNext()){
			   
			   Number_Group   = c.getString(1).trim();
			   //Log.w("getcoint", "33333"+Number_Group+","+number);
			   if(number.trim().equals(Number_Group))
			   {
				   dbObj.close();
				   
				   return true;
			   }
		   }			   
		   }
		  	/* W/getcoint(11092): 222223
                            W/getcoint(11092): 33333 6555545666,1222233322
                          W/getcoint(11092): 33333 1222233322,1222233322
                        W/T*/
		
		   dbObj.close();
		   
		   return false;
	}
	
	public boolean IsResContactExist(){
		   
		dbObj.Open();
		  
		   Cursor c;
		   
		   c= dbObj.getReceipent();
		   
		  				   
		   while(c.moveToNext()){
			   
			  return true;
		   }
				   
		   dbObj.close();
		   return false;
	}
	
	public boolean IsSelectSelectExist()
	{
		   
		dbObj.Open();
		  
		   Cursor c;
		   
		   c= dbObj.getSelectedGroupContacts();
		   
		  				   
		   while(c.moveToNext()){
			   
			  return true;
		   }
				   
		   dbObj.close();
		   return false;
	}
	
	public void fetchMobileandUserId() 
	{
		dbObj.Open();
		  
		   Cursor c;
		   
		   c= dbObj.getLoginDetails();
		   
		  				   
		   while(c.moveToNext()){
			   
			    UserId = c.getString(3);
			   
		   }
				   
		   dbObj.close();
	}
	
	public String fetchGroupContact()
	{
		
		  dbObj.Open();
		  Cursor c;
		  c= dbObj.getValueEditGroup();
		  while(c.moveToNext())
		  { 
		  mobNumber = c.getString(1);
		  }
		dbObj.close();
		   
		return mobNumber;
	}
	
	public static void chechuncheckall() 
	{
		try {
			if(cbx.isChecked())
			{
			NewContact.cbxselect.setChecked(true);	
			}
			else
			{
			NewContact.cbxselect.setChecked(false);		
			}
		} catch (Exception e) {
			
		}
	}
}
