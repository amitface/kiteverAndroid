package sms19.listview.adapter;

import java.util.List;

import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.GetAllGroup;
import com.kitever.android.R;
import sms19.listview.newproject.model.DeleteGroup;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.newproject.model.GroupContactDetailModel.getAllNameAndNumber;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactAdaptorForGroup extends ArrayAdapter<getAllNameAndNumber> 
{
private int resource;
private List<getAllNameAndNumber> data;
private Context con;	
CheckBox cbx;
String number,Namec,UserId,Mobile;
DataBaseDetails dbObject;
public ContactAdaptorForGroup(Context context, int resource,List<getAllNameAndNumber> objects)
{
	
super(context, resource, objects);
this.resource=resource;
this.data=objects;
this.con=context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView             = inflater.inflate(R.layout.customlistforcontacts, null);
		
			// Now we can fill the layout with the right values
			TextView tv       = (TextView) convertView.findViewById(R.id.textViewName);
			TextView tvMobile = (TextView) convertView.findViewById(R.id.textViewMObile);
			
		    cbx              = (CheckBox) convertView.findViewById(R.id.checkBoxContact);
			ImageView add    = (ImageView)convertView.findViewById(R.id.editContactImage);
			ImageView delete = (ImageView)convertView.findViewById(R.id.deleteContactImage);
			
			cbx.setVisibility(View.INVISIBLE);
			add.setVisibility(View.INVISIBLE);
			
			dbObject = new DataBaseDetails(con);
          number = data.get(position).getContact_Mobile();
			
			Namec  = data.get(position).getContact_Name();
			tv.setText(Namec);
			tvMobile.setText(number);
            delete.setOnClickListener(new OnClickListener()
            {
				
				@Override
				public void onClick(View v) 
				{
					data.remove(position);
					notifyDataSetChanged();
					fetchMobileandUserId();
				new webservice(null, webservice.DeleteGroupContact.geturl(UserId, GetAllGroup.gpname, number), webservice.TYPE_GET, webservice.TYPE_DELETE_GROUP, new ServiceHitListener() {
					
					@Override
					public void onSuccess(Object Result, int id)
					{
						DeleteGroup model = (DeleteGroup) Result;
						
						
						
						if(model.getDeleteContactGroupRegistration().get(0).getMsg().length()>0){
							
							Toast.makeText(con, ""+model.getDeleteContactGroupRegistration().get(0).getMsg(), Toast.LENGTH_SHORT).show();

						}
						else{
							Toast.makeText(con, ""+model.getDeleteContactGroupRegistration().get(0).getError(), Toast.LENGTH_SHORT).show();

						}
						refreshchat();
					}

					/**
					 * 
					 */
					
					@Override
					public void onError(String Error, int id) {
						// TODO Auto-generated method stub
						
					}
				});					
				}
			});
			
			
			
		return convertView;
	}
	public void refreshchat() 
	{
//		Log.w("GetAllGroupDetails","Inside GetAllGroupDetails");
		fetchMobileandUserId();
	    
	 
     	new webservice(null, webservice.GetAllGroupDetails.geturl(UserId), webservice.TYPE_GET, webservice.TYPE_GET_GROUP_DETAILS, new ServiceHitListener() {
		
		@Override
		public void onSuccess(Object Result, int id) 
		{
//			Log.w("GetAllGroupDetails","Success GetAllGroupDetails"+UserId);
			FetchGroupDetails Model  = (FetchGroupDetails) Result;
			
			dbObject.Open();
			dbObject.DeleteGroupDataALL();
			dbObject.close();
			dbObject.Open();
			for(int i=0;i<Model.getGroupDetails().size();i++)
			{
//				Log.w("GetAllGroupDetails","Success GetAllGroupDetails"+i);
				//`fetchMobileandUserId();
				String gpname =Model.getGroupDetails().get(i).getGroup_name();
				String gpCount=Model.getGroupDetails().get(i).getNoOfContacts();
				
				
				dbObject.addGroupAll(UserId, gpname , gpCount);
				
			}
			dbObject.close();
		}
		
		@Override
		public void onError(String Error, int id) 
		{
			
		}
	    });
	}
	
public void fetchMobileandUserId() {
		
		dbObject = new DataBaseDetails(con);
		
		dbObject.Open();
		  
		   Cursor c;
		   
		   c= dbObject.getLoginDetails();
		   
		  				   
		   while(c.moveToNext())
		   {
			Mobile   = c.getString(1);
			UserId   = c.getString(3);
			
			   
		   }
				   
		   dbObject.close();
	}
	

}
