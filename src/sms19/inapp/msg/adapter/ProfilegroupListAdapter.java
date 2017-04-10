package sms19.inapp.msg.adapter;

import java.util.ArrayList;

import sms19.inapp.msg.GroupProfile;
import sms19.inapp.msg.InAppMessageActivity;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.imoze.EmojiconTextView;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.Recentmodel;
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

public class ProfilegroupListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<Recentmodel> recentmodels;
	private LayoutInflater inflater;
	private OnClickListener clickListener;
	private int selectedPosition=-1;
	private GroupProfile groupProfile=null;
	private Recentmodel recentUserModel=new Recentmodel();
	Recentmodel recentAdminModel=new Recentmodel();
	String admin_jid="";
	private InAppMessageActivity appMessageActivity=null;
	

	public ProfilegroupListAdapter(Activity activity,ArrayList<Recentmodel> recentmodels) {

		this.activity = activity;
		appMessageActivity=(InAppMessageActivity)activity;
		this.recentmodels = recentmodels;
		inflater = activity.getLayoutInflater();
		groupProfile=GroupProfile.newInstance();
		recentAdminModel.setDisplayname("You(Admin)");
		recentAdminModel.setUserpic(InAppMessageActivity.myModel.getAvatar());

		if(groupProfile.getGroupModel().getCreated_me().equalsIgnoreCase("1")){
			recentmodels.add(recentAdminModel);
		}

		try {
			admin_jid=GlobalData.dbHelper.checkGroupAdmin(groupProfile.getRemote_jid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

			convertView=	inflater.inflate(R.layout.group_profile_user_row, null);

			holder = new Recentchatholder();

			holder.delete_layout=(LinearLayout)convertView.findViewById(R.id.delete_layout);
			holder.delete_group=(ImageView)convertView.findViewById(R.id.delete_group1);
			holder.onlineimage=(ImageView)convertView.findViewById(R.id.onlineimage);
			holder.rc_userpic=(sms19.inapp.msg.CircularImageView)convertView.findViewById(R.id.rc_userpic);

			holder.phone_number=(TextView)convertView.findViewById(R.id.phone_number);
			holder.rc_usernametext=(TextView)convertView.findViewById(R.id.name);
			holder.msg=(TextView)convertView.findViewById(R.id.msg);

			holder.delete_group.setOnClickListener(clickListener);
			convertView.setTag(R.layout.recent_chat_adapterlay,holder);
		} else {
			holder = (Recentchatholder) convertView.getTag(R.layout.recent_chat_adapterlay);
		}
		//holder.delete_group.setVisibility(View.GONE);
		holder.delete_group.setTag(position);

		if(groupProfile.getGroupModel().getCreated_me().equalsIgnoreCase("0")){
			holder.delete_group.setVisibility(View.GONE);
		}else{
			holder.delete_group.setVisibility(View.VISIBLE);
		}

		Recentmodel contact = recentmodels.get(position);


		holder.phone_number.setText(contact.getUsernumber());


		holder.rc_usernametext.setText(contact.getDisplayname());


		// SetimageBitmap(contact, holder.agf_userpic);
		if (contact.getIsgroup() == 2) {
			holder.rc_userpic.setImageResource(R.drawable.icon3);

		} else {
			Bitmap pic = null;
			if (contact.getUserpic() != null) {
				pic = BitmapFactory.decodeByteArray(contact.getUserpic(),
						0, contact.getUserpic().length);

			} else {
				Drawable drawable = activity.getResources().getDrawable(R.drawable.profileimg);
				pic = Utils.drawableToBitmap(drawable);

			}

			String status = "0";
			if (contact.getStatus() != null) {
				status = contact.getStatus().trim();
			}
			
			
			
			
			
			if (status.equals("1")) {

				holder.onlineimage.setImageDrawable(activity.getResources().getDrawable(R.drawable.online));


				holder.rc_userpic.setImageBitmap(pic);

				holder.msg.setText("Online");

			} else {

				holder.onlineimage.setImageDrawable(activity.getResources().getDrawable(R.drawable.offline));

				holder.rc_userpic.setImageBitmap(pic);
				holder.msg.setText("Offline");
			}

			
			
			if(contact.getIsgroup()==1){
				holder.onlineimage.setVisibility(View.GONE);
			}

			holder.rc_userpic.setImageBitmap(pic);
			
			Contactmodel contactmodel=	GlobalData.dbHelper.getCustomStatus(contact.getRemote_jid());
			
			if(contactmodel!=null){
				
			if(contactmodel.getStatus().equalsIgnoreCase("0")){
				holder.msg.setText("Offline");
			}else{
				if(contactmodel.getCustomStatus().equalsIgnoreCase("0")){
					if (contactmodel.getStatus().equals("1")) {
						holder.msg.setText("Online");
					}else{
						holder.msg.setText("Offline");
					}
				}else{
					holder.msg.setText(contactmodel.getCustomStatus());
				}
				
			}
			}else{
				holder.msg.setText("Offline");
			}
			
			
			
			
		}

		if(admin_jid!=null){
			if(contact.getRemote_jid().equalsIgnoreCase(admin_jid)){
				holder.rc_usernametext.setText(contact.getDisplayname()+"(Admin)");
				if(recentmodels.size()-1==position){
				holder.msg.setVisibility(View.GONE);
				holder.delete_group.setVisibility(View.GONE);
				holder.onlineimage.setVisibility(View.GONE);
				
				}
			}else{
				
				holder.delete_group.setVisibility(View.VISIBLE);
				holder.onlineimage.setVisibility(View.VISIBLE);
				holder.delete_layout.setVisibility(View.VISIBLE);
				holder.msg.setVisibility(View.VISIBLE);
				
				if(groupProfile.getGroupModel().getCreated_me().equalsIgnoreCase("0")){
					holder.delete_group.setVisibility(View.GONE);
					holder.delete_layout.setVisibility(View.GONE);
					
				}
				
			
				    
					
					
				
				
			}
		}
		if(contact.getDisplayname().trim().equalsIgnoreCase("You(Admin)")){
			holder.rc_usernametext.setText(contact.getDisplayname());
			if(groupProfile.getGroupModel().getCreated_me().equalsIgnoreCase("0")){
				holder.rc_usernametext.setText("You");
			}
			
			holder.delete_group.setVisibility(View.GONE);
			holder.delete_layout.setVisibility(View.GONE);
			holder.onlineimage.setVisibility(View.GONE);
			holder.msg.setVisibility(View.GONE);
		}else if(contact.getDisplayname().trim().equalsIgnoreCase("You")){
			holder.delete_group.setVisibility(View.GONE);
			holder.onlineimage.setVisibility(View.GONE);
			holder.delete_layout.setVisibility(View.GONE);
			holder.msg.setVisibility(View.GONE);
			if(groupProfile.getGroupModel().getCreated_me().equalsIgnoreCase("0")){
				holder.rc_usernametext.setText("You");
			}
		}


		return convertView;






	}


	

	class Recentchatholder {
		TextView rc_usernametext, unread_count, rc_msgtime,msg,phone_number;
		EmojiconTextView rc_msg;
		LinearLayout nav_page,delete_layout;
		ImageView delete_group, rc_userpicuppr,onlineimage;
		sms19.inapp.msg.CircularImageView rc_userpic;
		RelativeLayout rc_userlay, unreadcount_lay,status_layout;

	}


	public View.OnClickListener getClickListener() {
		return clickListener;
	}

	public void setClickListener(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public ArrayList<Recentmodel> getGroupBeanArrayList() {
		return recentmodels;
	}

	public void setRecentArrayList(ArrayList<Recentmodel> groupBeanArrayList) {


		this.recentmodels = groupBeanArrayList;
		/*if(groupProfile.getGroupModel().getCreated_me().equalsIgnoreCase("1")){
    		this.recentmodels.add(recentAdminModel);
    	}*/


	}

	public void setSelectionPosition(int selectedPosition){
		this.selectedPosition=selectedPosition;
	}


	public Recentmodel getRecentUserModel() {
		return recentUserModel;
	}


	public void setRecentUserModel(Recentmodel recentUserModel) {
		this.recentUserModel = recentUserModel;
	}
}