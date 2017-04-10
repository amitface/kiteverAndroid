package sms19.listview.adapter;

import java.util.List;

import sms19.listview.database.DataBaseDetails;
import com.kitever.android.R;
import sms19.listview.newproject.model.FetchPackage.PackageList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


public class PackageCustom extends ArrayAdapter<PackageList> 
{
	private List<PackageList> packagelist;
	private Context context;
    public static String getdata="";
    int pos;
    DataBaseDetails db;
    String pakageselected;
    int selectedPosition =   -1; 
    Holder hd;
    int count=0,selectedposition;
	public PackageCustom(Context context,List<PackageList> objects) 
	{
	super(context,R.layout.packagedetailscustom,objects);
	this.context     = context;
	this.packagelist = objects;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent)
	{
	
	 
		db=new DataBaseDetails(context);
		
		if(convertView==null)
	    {
			hd=new Holder();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView             = inflater.inflate(R.layout.packagedetailscustom, null);
	     hd.tv             = (TextView) convertView.findViewById(R.id.nosms);
		 hd.tvMobile       = (TextView) convertView.findViewById(R.id.costsms);
		 hd.selctb      =(CheckBox)convertView.findViewById(R.id.selectb);
		 convertView.setTag(hd); 
	    }
		else
		{
			hd = (Holder) convertView.getTag();
		}
		  
		hd.tv.setText(packagelist.get(position).getNoofsms());
		hd.tvMobile.setText(packagelist.get(position).getCostOfSms());
	 

		hd.selctb.setChecked(position == selectedPosition);
          hd.selctb.setTag(position);
          hd.selctb.setOnClickListener(new View.OnClickListener() 
          {
                  @Override
                  public void onClick(View view)
                  {

                      selectedPosition = (Integer) view.getTag();
                      getdata=packagelist.get(position).getNoofsms();
                      notifyDataSetInvalidated();
                      notifyDataSetChanged();
                  }
              });
		return convertView;
	}
public class Holder
{
	 CheckBox selctb;	
	 TextView tv ;
	 TextView tvMobile ;
}
	}
