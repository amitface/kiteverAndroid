package sms19.inapp.msg.adapter;

import java.util.ArrayList;

import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.imoze.EmojiconTextView;
import sms19.inapp.msg.model.Recentmodel;
import com.kitever.android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BroadCastFirstAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Recentmodel> recentmodels;
    private LayoutInflater inflater;
    private OnClickListener clickListener;
    private int selectedPosition=-1;



    public BroadCastFirstAdapter(Activity activity,ArrayList<Recentmodel> recentmodels) {

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
			/* holder.mTopLayout=(LinearLayout)convertView.findViewById(R.id.topLayout);
			holder.rc_userlay = (RelativeLayout) convertView.findViewById(R.id.rc_userlay);
			holder.status_layout= (RelativeLayout) convertView.findViewById(R.id.status_layout);
			holder.unreadcount_lay = (RelativeLayout) convertView.findViewById(R.id.unreadcount_lay);
			holder.rc_usernametext = (TextView) convertView.findViewById(R.id.rc_usernametext);
			holder.rc_msg = (EmojiconTextView) convertView.findViewById(R.id.rc_msg);
			holder.unread_count = (TextView) convertView.findViewById(R.id.unread_count);
			holder.rc_msgtime = (TextView) convertView.findViewById(R.id.rc_msgtime);
			holder.rc_userpic = (ImageView) convertView.findViewById(R.id.rc_userpic);
			
			// holder.rc_userpicuppr = (ImageView) convertView
			// .findViewById(R.id.rc_userpicuppr);
			 holder.rc_userlay.setOnClickListener(clickListener);*/
			 
			convertView.setTag(R.layout.recent_chat_adapterlay,holder);
		} else {
			holder = (Recentchatholder) convertView.getTag(R.layout.recent_chat_adapterlay);
		}
		
		
		// holder.rc_userlay.setTag(position);
		
      
        return convertView;
    
    	
    	
        
      
        
    }

    
    public void delete_user_chat_history(final String remotejid) {
		new AlertDialog.Builder(activity)
				.setTitle("Delete Chat")
				.setMessage("Are you sure you want to delete chat history?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								GlobalData.dbHelper
										.deleteParticularUserHistory(remotejid)	;
										GlobalData.dbHelper
										.deleteRecentParticularrow(remotejid);	
							
								GlobalData.dbHelper.updateContactmsgData(
										remotejid, "", "");								
								recentmodels.clear();
								recentmodels.addAll(GlobalData.dbHelper.getRecentHistoryfromDb());
								notifyDataSetChanged();
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).show();
	}
    
    
    class Recentchatholder {
		TextView rc_usernametext, unread_count, rc_msgtime;
		EmojiconTextView rc_msg;
		LinearLayout mTopLayout;
		ImageView rc_userpic, rc_userpicuppr;
		RelativeLayout rc_userlay, unreadcount_lay,status_layout;
	}


    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ArrayList<Recentmodel> getClubBeanArrayList() {
        return recentmodels;
    }

    public void setRecentArrayList(ArrayList<Recentmodel> clubBeanArrayList) {
        this.recentmodels = clubBeanArrayList;

    }

    public void setSelectionPosition(int selectedPosition){
        this.selectedPosition=selectedPosition;
    }
}