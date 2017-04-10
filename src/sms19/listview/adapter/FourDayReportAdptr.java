package sms19.listview.adapter;

import java.util.List;

import sms19.listview.newproject.model.SendSmsReportFourDayModel.ReportSmsFour;
import com.kitever.android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FourDayReportAdptr extends ArrayAdapter<ReportSmsFour>
{
	private List<ReportSmsFour> details;
	private Context cont;
	private int resource;
	
	    String mobile  = "";
		String message = "";
		String Status  = "";
		String Date    = "";
		String DateT   = "";
		String DateO   = "";
		String DateTime = "";
	
	public FourDayReportAdptr(Context context, int resource, List<ReportSmsFour> objects) 
	{
		super(context, resource, objects);
		this.resource=resource;
		this.details=objects;
		this.cont=context;
	}

	@Override
	public ReportSmsFour getItem(int position)
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
	public int getCount() 
	{
		try
		{
	return details.size();
		}
		catch(Exception e)
		{
			return 0;
		}
		}
	@Override
public View getView(final int position, View convertView, ViewGroup parent) 
{

	if(convertView==null)
       {
		
	convertView=LayoutInflater.from(cont).inflate(resource, null);
       }
	TextView mob,msg,status,date;
	ImageView statisimage;
	
	mob         =(TextView)convertView.findViewById(R.id.textMObile);
	msg         =(TextView)convertView.findViewById(R.id.textMessage);
    status      =(TextView)convertView.findViewById(R.id.textStaus);
	date        =(TextView)convertView.findViewById(R.id.textDate2);
	statisimage =(ImageView)convertView.findViewById(R.id.imagestatus);
	
	try {
		mobile  = details.get(position).getMobile().trim();
	} catch (Exception e2) {
	   e2.printStackTrace();
	}
	try {
		message = details.get(position).getMessage().trim();
	} catch (Exception e2) {
		e2.printStackTrace();
	}
	try {
		Status  = details.get(position).getMessageStatus().trim();
	} catch (Exception e2) {
		e2.printStackTrace();
	}
	try {
		Date    = details.get(position).getMessageDeliveredDate().trim();
	} catch (Exception e2) {
		e2.printStackTrace();
	}
	
	//*****************************************DATE
	try {
	
		int pod = Date.indexOf("T");
		DateT   = Date.substring((pod+1),(pod+9));
		DateO = Date.substring(0,Date.indexOf("T"));

		String dataWithGoodFormat[]= DateO.split("-");
		String dataGood= dataWithGoodFormat[2]+"-"+dataWithGoodFormat[1]+"-"+dataWithGoodFormat[0];
		DateTime = dataGood+"\n"+DateT;
		
	} catch (Exception e1) {

		e1.printStackTrace();
	}

	//*****************************************DATE
	
	//*****************************************SET DATA
			try {
				date.setText(DateTime);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				mob.setText(mobile);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
	
	try {
		if(message.length()>20)
		{
		String mn=message.substring(0,20);
		msg.setText(mn+"...see more");
		}
		else
		{
	    msg.setText(message);	
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	
	if(Status.equalsIgnoreCase("0"))
	{
	   statisimage.setBackgroundResource(R.drawable.right);	
	}
	else
	{
		statisimage.setBackgroundResource(R.drawable.wrong);	
	}
	
	//*****************************************SET DATA
	
		msg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(cont)
				
					 .setMessage(details.get(position).getMessage().trim())
					    .setCancelable(false)
					    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which)
					         { 
					        	dialog.cancel();
					         }
					     })
					    
					    .setIcon(android.R.drawable.ic_dialog_alert).show();
				}
				});
	return convertView;
		}
}
