package sms19.listview.adapter;

import java.util.List;


import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.Friendsinvite;
import com.kitever.android.R;
import sms19.listview.newproject.sms19coninvite;
import sms19.listview.newproject.model.ReferFriendModel;
import sms19.listview.newproject.model.contactinvitemodel;
import sms19.listview.webservice.webservice;
import sms19.listview.webservice.webservice.ServiceHitListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class invitecustomlistapp extends ArrayAdapter<contactinvitemodel>
{
	private int resource;
    private List<contactinvitemodel> data;
    Context con;
    String  referusername,referedusernumbe, statusrefer;
    DataBaseDetails db;
    String  Password="",UserLogin="",Mobile="",UserId="";
    public static boolean individualappcont=false;
    ProgressDialog p;
    
	public invitecustomlistapp(Context context, int resource, List<contactinvitemodel> objects) 
	{
	super(context, resource,objects);
	this.con=context;
	this.resource=resource;
	this.data=objects;
	}
	
@Override
public View getView(final int position, View convertView, ViewGroup parent)
{
	final Holder holder;
	db=new DataBaseDetails(con);
	if(convertView == null)
	{
	holder = new Holder();
	LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	convertView = inflater.inflate(resource, null);
	holder.name=(TextView)convertView.findViewById(R.id.name);
	holder.number=(TextView)convertView.findViewById(R.id.number);
	holder.invite=(Button)convertView.findViewById(R.id.Invite);
	holder.selectcontact=(CheckBox)convertView.findViewById(R.id.selectindividual);
	holder.selectcontact.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) 
		{
			if(holder.selectcontact.isChecked())
			{
				individualappcont=true;
			    db.Open();
			    db.addreferedPeople(data.get(position).getName(),data.get(position).getNumber());
			    db.close();	
			    
			}
			else if(!holder.selectcontact.isChecked())
			{
				try {
					sms19coninvite.selectoinvite.setChecked(false);
				} catch (Exception e) {
				}
				
				individualappcont=false;
				db.Open();
				db.deletereferedpeople(data.get(position).getNumber());
				db.close();
			}
			if(individualappcont==false)
			{
			holder.selectcontact.setChecked(false);
			}
				
		}
	});
	holder.invite.setOnClickListener(new OnClickListener()
	{
		
		@Override
		public void onClick(View v) 
		{ 
			
		String name   =data.get(position).getName();	
		String number =data.get(position).getNumber();
  
		   try
		   {
			   
			 db.Open();
			 Cursor c;
			 c= db.getLoginDetails();
			 
		    if(c.getCount()>0)
		    {
			   while(c.moveToNext())
			   {
			    Mobile    = c.getString(1);
				UserId    = c.getString(3);
				Password  = c.getString(5);
				UserLogin = c.getString(6);    
			   }
		    }
		    
		    db.close();
		   } catch (Exception e) {
				e.printStackTrace();
			}
		   
		   p= ProgressDialog.show(con, null, "please wait...");
		 new webservice(null, webservice.ReferFriend.geturl(UserLogin , Password, UserId, "SMSSMS",number , Mobile,name), webservice.TYPE_GET, webservice.TYPE_REFER_FRIEND,new ServiceHitListener() {
			
			@Override
			public void onSuccess(Object Result, int id)
			{
				try {
					p.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				ReferFriendModel model = (ReferFriendModel) Result;
				
				if(model.getReferFriend().size()>0)
				{
					String reftype = "";
					String  Time="";
				
					for(int i=0;i<model.getReferFriend().size();i++)
				    {
										
						reftype=model.getReferFriend().get(i).getReferredType();
						
						try
						{
							if(reftype.equalsIgnoreCase("1"))	
							{
								statusrefer        = model.getReferFriend().get(i).getReferredType();
								referedusernumbe   = model.getReferFriend().get(i).getRecipientPhoneNo();
								referusername      = model.getReferFriend().get(i).getRecipientName();
								
								db.Open();
								db.deletereferedpeopleall();
								db.addintoreferdb(UserId, referusername,referedusernumbe, statusrefer, Time);
								db.close();
							}
						}
						catch (Exception e) 
						{
						}	
					
					
				
				    }
		}
			
			}
			
			@Override
			public void onError(String Error, int id)
			{
				try {
					p.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
				
				
		}
	});
	
	if(sms19coninvite.invitallvara==true)
	{
	holder.selectcontact.setChecked(true);
	}
	else
	{
	holder.selectcontact.setChecked(false);	
	}
	
	if(holder.selectcontact.isChecked())
    {
    db.Open();
    db.addreferedPeople(data.get(position).getName(),data.get(position).getNumber());
    db.close();
    } 
	else
	{
		db.Open();
		db.deletereferedpeople(data.get(position).getNumber());
		db.close();	
	}
	
	holder.name.setText(data.get(position).getName());
	holder.number.setText(data.get(position).getNumber());
	convertView.setTag(holder);
	}
	else
    {
	holder = (Holder) convertView.getTag();
    }
	
return convertView;
}


public class Holder
{
TextView name;
TextView number;
Button invite;
CheckBox selectcontact;
}
}
