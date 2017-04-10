package sms19.inapp.msg.adapter;

import java.util.ArrayList;

import sms19.inapp.msg.CircularImageView;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.imoze.EmojiconTextView;
import sms19.inapp.msg.model.Groupmodel;
import com.kitever.android.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SingleUserProfileGroupAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Groupmodel> recentmodels;
    private LayoutInflater inflater;
    private OnClickListener clickListener;
    private int selectedPosition=-1;



    public SingleUserProfileGroupAdapter(Activity activity,ArrayList<Groupmodel> recentmodels) {

        this.activity = activity;
        this.recentmodels = recentmodels;
        inflater = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return recentmodels.size();
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
    public View getView(final int position, View view, ViewGroup parent) {

       
    	
    	 View convertView = view;
     

    	Recentchatholder holder = null;
		if (convertView == null) {
			 
			convertView=	inflater.inflate(R.layout.broadcast_groups_row, null);
			
			holder = new Recentchatholder();
			holder.delete_group=(ImageView)convertView.findViewById(R.id.delete_group);
			holder.profile_image=(CircularImageView)convertView.findViewById(R.id.profile_image);
			
			holder.onlineimage=(ImageView)convertView.findViewById(R.id.onlineimage);
			holder.nav_page=(LinearLayout)convertView.findViewById(R.id.nav2);
			
			holder.rc_usernametext=(TextView)convertView.findViewById(R.id.name);
			holder.msg=(TextView)convertView.findViewById(R.id.msg);
			holder.nav_page.setOnClickListener(clickListener);
			convertView.setTag(R.layout.recent_chat_adapterlay,holder);
		} else {
			holder = (Recentchatholder) convertView.getTag(R.layout.recent_chat_adapterlay);
		}
		holder.onlineimage.setVisibility(View.GONE);
		holder.delete_group.setVisibility(View.GONE);
		holder.nav_page.setTag(position);
		
		
		
		Groupmodel contact = recentmodels.get(position);
		holder.rc_usernametext.setText(contact.getGroupname());
		holder.msg.setText("Group users..");
	
		Bitmap pic = null;
		if (contact.getUser_pic() != null) {
			pic = BitmapFactory.decodeByteArray(contact.getUser_pic(), 0,
					contact.getUser_pic().length);
			

		} else {
			Drawable drawable = activity.getResources().getDrawable(R.drawable.profileimg);
			pic = Utils.drawableToBitmap(drawable);
			
		}
		holder.profile_image.setImageBitmap(pic);
      
		try {
			String users=Utils.getUsersNameWithGroupName(contact.getContactList());
			if(!users.equalsIgnoreCase("")){
				holder.msg.setText(users);
			}else{
				holder.msg.setText("Group users..");
			}
			
			String isBroadCast=contact.getGroup_jid().split("@")[1];
			boolean isBroadCastFlag=false;
			if(isBroadCast.startsWith("Broadcast")){
				isBroadCastFlag=true;
			}
			if (isBroadCastFlag) {
				
				holder.profile_image.setImageResource(R.drawable.icon3);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        return convertView;
            
    }

    
    class Recentchatholder {
		TextView rc_usernametext, unread_count, rc_msgtime,msg;
		EmojiconTextView rc_msg;
		LinearLayout nav_page;
		sms19.inapp.msg.CircularImageView profile_image;
		ImageView rc_userpic, rc_userpicuppr,onlineimage,delete_group;
		RelativeLayout rc_userlay, unreadcount_lay,status_layout;
		
	}


    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ArrayList<Groupmodel> getGroupBeanArrayList() {
        return recentmodels;
    }

    public void setRecentArrayList(ArrayList<Groupmodel> groupBeanArrayList) {
        this.recentmodels = groupBeanArrayList;

    }

    public void setSelectionPosition(int selectedPosition){
        this.selectedPosition=selectedPosition;
    }
}
