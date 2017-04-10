package sms19.inapp.msg.adapter;

import java.util.ArrayList;

import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import com.kitever.android.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BroadCastSecondAdapter extends BaseAdapter {

    private Activity activity;
    ArrayList<Contactmodel> contactlist;
    private LayoutInflater inflater;
    private View.OnClickListener clickListener;
    private int selectedPosition=-1;



    public BroadCastSecondAdapter(Activity activity, ArrayList<Contactmodel> contactlist) {

        this.activity = activity;
        this.contactlist = contactlist;
        inflater = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
    	
    	if(contactlist==null){
    		contactlist=new ArrayList<Contactmodel>();
    	}
    	
        return contactlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.new_broadcast_row, null);
            holder = new ViewHolder();
            holder.mTopLayout=(LinearLayout)view.findViewById(R.id.navilayout);
            holder.agf_usernametext = (TextView) view.findViewById(R.id.name);
			holder.agf_custom_status = (TextView) view.findViewById(R.id.msg);
			holder.profile_image= (sms19.inapp.msg.CircularImageView) view.findViewById(R.id.profile_image);
			
			holder.mBroadCast= (ImageView) view.findViewById(R.id.broadcast);
			holder.mInvite= (ImageView) view.findViewById(R.id.invite);
			holder.closeImg= (ImageView) view.findViewById(R.id.delete_group);
			holder.mGroup= (ImageView) view.findViewById(R.id.group);
			holder.onlineimage= (ImageView) view.findViewById(R.id.onlineimage);
			
			
			holder.closeImg.setOnClickListener(clickListener);
           // holder.mTopLayout.setOnClickListener(clickListener);
           // holder.mBroadCast.setOnClickListener(clickListener);
           // holder.mInvite.setOnClickListener(clickListener);
          //  holder.mGroup.setOnClickListener(clickListener);
         
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.closeImg.setTag(position);
        holder.mTopLayout.setTag(position);
        
        holder.mBroadCast.setVisibility(View.GONE);
        holder.mInvite.setVisibility(View.GONE);
        holder.mGroup.setVisibility(View.GONE);
        holder.closeImg.setVisibility(View.VISIBLE);
        
        
        
        Contactmodel contact = contactlist.get(position);
        
        holder.agf_usernametext.setText(contact.getName());
      
        
        if(contact.getIsRegister()==0){
        
        	holder.onlineimage.setVisibility(View.GONE);
        	holder.mTopLayout.setEnabled(false);
        }else{
        	holder.onlineimage.setVisibility(View.VISIBLE);
        	holder.mTopLayout.setEnabled(true);
        }
       
          
        
    	
        
        holder.agf_usernametext.setText(contact.getName());
		holder.agf_custom_status.setText(contact.getCustomStatus());
		
		String status = "0";
		if (contact.getStatus() != null) {
			status = contact.getStatus().trim();
		}
		
		Bitmap pic = null;
		if (contact.getAvatar() != null) {
			pic = BitmapFactory.decodeByteArray(contact.getAvatar(), 0,
					contact.getAvatar().length);
			pic = Utils.getResizedBitmap(pic, GlobalData.profilepicthmb,
					GlobalData.profilepicthmb);

		} else {
			Drawable drawable = activity.getResources().getDrawable(R.drawable.profileimg);
			pic = Utils.drawableToBitmap(drawable);
			pic = Utils.getResizedBitmap(pic, GlobalData.profilepicthmb,
					GlobalData.profilepicthmb);
		}
		if (status.equals("1")) {
			//pic = Utils.getCircularBitmapWithBorder(pic, 5, Color.GREEN);
			holder.onlineimage.setImageDrawable(activity.getResources().getDrawable(R.drawable.online));

			holder.profile_image.setImageBitmap(pic);

			//holder.agf_userlastseen.setVisibility(View.INVISIBLE);
		} else {
			//pic = Utils.getCircularBitmapWithBorder(pic, 5,Color.TRANSPARENT);
			holder.onlineimage.setImageDrawable(activity.getResources().getDrawable(R.drawable.offline));
			holder.profile_image.setImageBitmap(pic);

			
		}

      
        return view;
    }

    private class ViewHolder {

    	LinearLayout mTopLayout;
    	TextView agf_usernametext, agf_custom_status;
    	ImageView mInvite,mBroadCast,mGroup,onlineimage,closeImg;
    	sms19.inapp.msg.CircularImageView profile_image;
     

    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ArrayList<Contactmodel> getContactArrayList() {
        return contactlist;
    }

    public void setContactArrayList(ArrayList<Contactmodel> clubBeanArrayList) {
        this.contactlist = clubBeanArrayList;

    }

    public void setSelectionPosition(int selectedPosition){
        this.selectedPosition=selectedPosition;
    }
}
