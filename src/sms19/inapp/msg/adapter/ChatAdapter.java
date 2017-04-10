package sms19.inapp.msg.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.pos.adapter.POSCreditAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import sms19.inapp.msg.ChatFragment;
import sms19.inapp.msg.CircularImageView;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.imoze.EmojiconTextView;
import sms19.inapp.msg.model.Recentmodel;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;


public class ChatAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Recentmodel> recentmodels;
    private LayoutInflater inflater;
    private OnClickListener clickListener;
    private ChatFragment chatFragment;
    int isClickViewProfile = 0;
    private ArrayList<String> lastMsgList;
    private ArrayList<String> lastMsgDateList;
    private ImageView imageView;

//    public ChatAdapter(Activity activity,ArrayList<Recentmodel> recentmodels) {
//
//        this.activity = activity;
//        this.recentmodels = recentmodels;
//        inflater = activity.getLayoutInflater();
//        chatFragment=ChatFragment.newInstance("");
//    }

    public ChatAdapter(Activity activity, ChatFragment chatFragment, ArrayList<Recentmodel> recentmodels) {

        this.activity = activity;
        this.chatFragment = chatFragment;
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {


        View convertView = view;
        final Recentmodel contact = recentmodels.get(position);

        Recentchatholder holder = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.recent_chat_adapterlay, null);
            holder = new Recentchatholder();
            holder.mTopLayout = (LinearLayout) convertView.findViewById(R.id.topLayout2);
            holder.rc_userlay = (RelativeLayout) convertView.findViewById(R.id.rc_userlay);
            holder.status_layout = (RelativeLayout) convertView.findViewById(R.id.status_layout);
            holder.framelayout = (FrameLayout) convertView.findViewById(R.id.framelayout);
            holder.unreadcount_lay = (RelativeLayout) convertView.findViewById(R.id.unreadcount_lay);
            holder.rc_usernametext = (TextView) convertView.findViewById(R.id.rc_usernametext);
            holder.rc_msg = (EmojiconTextView) convertView.findViewById(R.id.rc_msg);
            holder.unread_count = (TextView) convertView.findViewById(R.id.unread_count);
            holder.rc_msgtime = (TextView) convertView.findViewById(R.id.rc_msgtime);
            holder.rc_userpic = (sms19.inapp.msg.CircularImageView) convertView.findViewById(R.id.rc_userpic);
            holder.onlineimage = (ImageView) convertView.findViewById(R.id.onlineimage);

            setRobotoThinFont(holder.rc_usernametext,activity);
            setRobotoThinFont(holder.unread_count,activity);
            setRobotoThinFont(holder.rc_msgtime,activity);

            holder.rc_usernametext.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            holder.rc_msg.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));




//            holder.mTopLayout.setOnClickListener(clickListener);
            //holder.rc_msg.setOnClickListener(clickListener);
            convertView.setTag(R.layout.recent_chat_adapterlay, holder);
        } else {
            holder = (Recentchatholder) convertView.getTag(R.layout.recent_chat_adapterlay);
        }


        //holder.rc_userpic.setEnabled(false);
        //holder.rc_msg.setEnabled(false);
        //holder.rc_msg.setClickable(true);
        //holder.rc_usernametext.setEnabled(false);

        if (isClickViewProfile == -11) {
            holder.mTopLayout.setEnabled(false);
        } else {
            holder.mTopLayout.setEnabled(true);
        }

        holder.rc_userpic.setTag(position);
        holder.mTopLayout.setTag(position);
        holder.rc_msg.setTag(position);
        holder.rc_usernametext.setTag(position);


        holder.rc_usernametext.setText(contact.getDisplayname());
        String lastMsg = contact.getLastmsg();
        if (lastMsgList != null && lastMsgList.size() > 0 && lastMsgList.size() > position) {
            lastMsg = lastMsgList.get(position);
        }
        String lastMsgTime = contact.getLastmsg_t();
        if (lastMsgDateList != null && lastMsgDateList.size() > 0 && lastMsgDateList.size() > position) {
            lastMsgTime = lastMsgDateList.get(position);
            holder.rc_msgtime.setText(lastMsgTime);
        } else {
            holder.rc_msgtime.setText(Utils.getRecentmsgDateorTime(lastMsgTime));
        }
        holder.rc_msg.setText(lastMsg);

//		holder.rc_msgtime.setText(Utils.getRecentmsgDateorTime(lastMsgTime));

        holder.rc_msg.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int pos = (Integer) v.getTag();
                delete_user_chat_history(contact, pos, contact.getRemote_jid());
                return false;
            }
        });

        holder.mTopLayout.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int pos = (Integer) v.getTag();
                delete_user_chat_history(contact, pos, contact.getRemote_jid());
                return false;
            }
        });

        holder.mTopLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chatFragment.navigateSingleChatUser(position);
            }
        });


        Utils.setdosisboldFont(holder.rc_usernametext, activity);

		/*try {
            int countmessage=GlobalData.dbHelper.getChathistoryTotalCountfromDB(contact.getRemote_jid());
			contact.setTotalMessageCount(countmessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        holder.rc_msg.setText(lastMsg);
        if (holder.rc_msg.getText().toString() != null && holder.rc_msg.getText().toString().length() > 0) {
            String upperString = holder.rc_msg.getText().toString().substring(0, 1).toUpperCase() + holder.rc_msg.getText().toString().substring(1);
            holder.rc_msg.setText(upperString);
        }

        if (contact.getUnreadcount() == 0) {
            holder.unreadcount_lay.setVisibility(View.GONE);
            holder.rc_msg.setTypeface(Typeface.DEFAULT);
        } else {

            holder.rc_msg.setTypeface(Typeface.DEFAULT_BOLD);
            holder.unreadcount_lay.setVisibility(View.VISIBLE);
            holder.unread_count.setText("" + contact.getUnreadcount());
        }

        imageView = holder.rc_userpic;

        if (contact.getIsgroup() == 2) {
            holder.rc_userpic.setImageResource(R.drawable.icon3);

        } else {
            String str = null;
            Bitmap pic = null;
//			if (contact.getUserpic() != null) {
            str = contact.getUserPicUrl();
            if (contact.getUserpic() != null && contact.getUserpic().length > 15) {
                try {
                    pic = BitmapFactory.decodeByteArray(contact.getUserpic(), 0, contact.getUserpic().length);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (pic != null) {
                    pic = Utils.getResizedBitmap(pic, GlobalData.profilepicthmb, GlobalData.profilepicthmb);
                    holder.rc_userpic.setImageBitmap(pic);
                }

            } else if (str != null && str.length() > 4) {

                Picasso.with(activity).load(str).into(holder.rc_userpic, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        GlobalData.dbHelper.updateByteArrayInContacts(bytes, contact.getRemote_jid());
                    }

                    @Override
                    public void onError() {
                        if (contact.getIsgroup() == 1)
                            imageView.setImageResource(R.drawable.group_icon);
                        else
                            imageView.setImageResource(R.drawable.profileimg);
                    }
                });
            } else {
                if (contact.getIsgroup() == 1)
                    holder.rc_userpic.setImageResource(R.drawable.group_icon);
                else
                    holder.rc_userpic.setImageResource(R.drawable.profileimg);
            }

            String status = "0";
            if (contact.getStatus() != null) {
                status = contact.getStatus().trim();
            }
            if (status.equals("1")) {
                holder.onlineimage.setImageDrawable(activity.getResources().getDrawable(R.drawable.online));
            } else {
                holder.onlineimage.setImageDrawable(activity.getResources().getDrawable(R.drawable.offline));
            }


//            holder.rc_userpic.setImageBitmap(pic);
        }

        if (contact.getIsgroup() == 1) {
            holder.onlineimage.setVisibility(View.GONE);
        } else if (contact.getIsgroup() == 2) {
            holder.onlineimage.setVisibility(View.GONE);
        } else if (contact.getIsgroup() == 0) {
            holder.onlineimage.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public void delete_user_chat_history(Recentmodel recentmodel, int pos, final String remotejid) {

        chatFragment.Show_Dialog(recentmodel, pos, remotejid);

		/*new AlertDialog.Builder(activity)
		.setTitle("Delete Chat")
		.setMessage("Are you sure you want to delete chat history?")
		.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int which) {

				GlobalData.dbHelper.deleteParticularUserHistory(remotejid)	;
				GlobalData.dbHelper.deleteRecentParticularrow(remotejid);

				GlobalData.dbHelper.updateContactmsgData(remotejid, "", "");
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
		 */
    }


    class Recentchatholder {
        TextView rc_usernametext, unread_count, rc_msgtime;
        EmojiconTextView rc_msg;
        LinearLayout mTopLayout;
        FrameLayout framelayout;
        ImageView rc_userpicuppr, onlineimage;
        CircularImageView rc_userpic;

        RelativeLayout rc_userlay, unreadcount_lay, status_layout;
    }


    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ArrayList<Recentmodel> getChatArrayList() {
        return recentmodels;
    }

    public void setRecentArrayList(ArrayList<Recentmodel> clubBeanArrayList) {
        this.recentmodels = clubBeanArrayList;

    }

    public void setLastMsgAndDateList(ArrayList<String> msgList, ArrayList<String> msgDateList) {
        lastMsgList = msgList;
        lastMsgDateList = msgDateList;
    }

    public int getIsClickViewProfile() {
        return isClickViewProfile;
    }


    public void setIsClickViewProfile(int isClickViewProfile) {
        this.isClickViewProfile = isClickViewProfile;
    }


}
/*
public class ChatAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Recentmodel> recentmodels;
    private LayoutInflater inflater;
    private OnClickListener clickListener;
    private ChatFragment chatFragment;
    int isClickViewProfile = 0;
    private ArrayList<String> lastMsgList;
    private ArrayList<String> lastMsgDateList;
    private Bitmap pic;


    public ChatAdapter(Activity activity, ChatFragment chatFragment, ArrayList<Recentmodel> recentmodels) {

        this.activity = activity;
        this.chatFragment = chatFragment;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Recentmodel contact = recentmodels.get(position);
        Recentchatholder holder;

        if (convertView == null) {
            holder = new Recentchatholder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.recent_chat_adapterlay,parent, false);

            holder.mTopLayout = (FrameLayout) convertView.findViewById(R.id.topLayout2);
            holder.rc_userlay = (RelativeLayout) convertView.findViewById(R.id.rc_userlay);
            holder.status_layout = (RelativeLayout) convertView.findViewById(R.id.status_layout);
            holder.framelayout = (LinearLayout) convertView.findViewById(R.id.recent_framelayout);
            holder.unreadcount_lay = (FrameLayout) convertView.findViewById(R.id.unreadcount_lay);
            holder.rc_usernametext = (TextView) convertView.findViewById(R.id.rc_usernametext);
            holder.rc_msg = (EmojiconTextView) convertView.findViewById(R.id.rc_msg);
            holder.unread_count = (TextView) convertView.findViewById(R.id.unread_count);
            holder.rc_msgtime = (TextView) convertView.findViewById(R.id.rc_msgtime);
            holder.rc_userpic = (sms19.inapp.msg.CircularImageView) convertView.findViewById(R.id.rc_userpic);
            holder.onlineimage = (ImageView) convertView.findViewById(R.id.onlineimage);


            //Amit
            holder.tick_imgview = (ImageView) convertView.findViewById(R.id.chatfragment_tick_imgview);
            convertView.setTag(R.layout.recent_chat_adapterlay, holder);
//            convertView.setTag(position,holder);
        } else {
            holder = (Recentchatholder) convertView.getTag(R.layout.recent_chat_adapterlay);
//            holder = (Recentchatholder) convertView.getTag(position);
        }


        if (isClickViewProfile == -11) {
            holder.mTopLayout.setEnabled(false);
        } else {
            holder.mTopLayout.setEnabled(true);
        }

        holder.rc_userpic.setTag(position);
        holder.mTopLayout.setTag(position);
        holder.framelayout.setTag(position);
        holder.rc_msg.setTag(position);
        holder.rc_usernametext.setTag(position);
        holder.tick_imgview.setTag(position);


        holder.mTopLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chatFragment.navigateSingleChatUser(position);
            }
        });
        holder.rc_userpic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog(recentmodels.get(position));
            }
        });

        holder.framelayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chatFragment.navigateSingleChatUser(position);
            }
        });

        holder.rc_usernametext.setText(contact.getDisplayname());
        String lastMsg = contact.getLastmsg();
        if (lastMsgList != null && lastMsgList.size() > 0 && lastMsgList.size() > position) {
            lastMsg = lastMsgList.get(position);
        }
        String lastMsgTime = contact.getLastmsg_t();
        if (lastMsgDateList != null && lastMsgDateList.size() > 0 && lastMsgDateList.size() > position) {
            lastMsgTime = lastMsgDateList.get(position);
            holder.rc_msgtime.setText(lastMsgTime);
        } else {
            holder.rc_msgtime.setText(Utils.getRecentmsgDateorTime(lastMsgTime));
        }
        holder.rc_msg.setText(lastMsg);

//		holder.rc_msgtime.setText(Utils.getRecentmsgDateorTime(lastMsgTime));

        */
/*holder.rc_msg.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int pos = (Integer) v.getTag();
                delete_user_chat_history(contact, pos, contact.getRemote_jid());
                return false;
            }
        });*//*


        holder.mTopLayout.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int pos = (Integer) v.getTag();
                delete_user_chat_history(contact, pos, contact.getRemote_jid());
                return false;
            }
        });


        Utils.setdosisboldFont(holder.rc_usernametext, activity);

		*/
/*try {
            int countmessage=GlobalData.dbHelper.getChathistoryTotalCountfromDB(contact.getRemote_jid());
			contact.setTotalMessageCount(countmessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*//*


        holder.rc_msg.setText(lastMsg);
        if (holder.rc_msg.getText().toString() != null && holder.rc_msg.getText().toString().length() > 0) {
            String upperString = holder.rc_msg.getText().toString().substring(0, 1).toUpperCase() + holder.rc_msg.getText().toString().substring(1);
            holder.rc_msg.setText(upperString);
        }

        if (contact.getUnreadcount() == 0) {
            holder.unreadcount_lay.setVisibility(View.GONE);
            holder.rc_msg.setTypeface(Typeface.DEFAULT);
        } else {

            holder.rc_msg.setTypeface(Typeface.DEFAULT_BOLD);
            holder.unreadcount_lay.setVisibility(View.VISIBLE);
            holder.unread_count.setText("" + contact.getUnreadcount());
        }

        if (contact.getIsgroup() == 2) {
            holder.rc_userpic.setImageResource(R.drawable.icon3);

        } else {
            byte[] str = null;
            pic = null;
//			if (contact.getUserpic() != null) {
            str = contact.getUserpic();
//            if (contact.getUserPicUrl() != null && !contact.getUserPicUrl().equalsIgnoreCase("") && !contact.getUserPicUrl().equalsIgnoreCase("null")) {
            if (str != null) {

                try {
                    pic = BitmapFactory.decodeByteArray(contact.getUserpic(), 0, contact.getUserpic().length);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (pic != null) {
                    pic = Utils.getResizedBitmap(pic, GlobalData.profilepicthmb, GlobalData.profilepicthmb);
                    holder.rc_userpic.setImageBitmap(pic);
                }
            }

            if (pic == null) {
                Drawable drawable = activity.getResources().getDrawable(R.drawable.profileimg);
                pic = Utils.drawableToBitmap(drawable);
                holder.rc_userpic.setImageBitmap(pic);
            }

            String status = "0";
            if (contact.getStatus() != null) {
                status = contact.getStatus().trim();
            }
            if (status.equals("1")) {
                holder.onlineimage.setImageDrawable(activity.getResources().getDrawable(R.drawable.online));
            } else {
                holder.onlineimage.setImageDrawable(activity.getResources().getDrawable(R.drawable.offline));
            }

            holder.rc_userpic.setImageBitmap(pic);
        }

        if (contact.getIsgroup() == 1) {
            holder.onlineimage.setVisibility(View.GONE);
        } else if (contact.getIsgroup() == 2) {
            holder.onlineimage.setVisibility(View.GONE);
        } else if (contact.getIsgroup() == 0) {
            holder.onlineimage.setVisibility(View.VISIBLE);
        }

        if (contact.getIsgroup() == 0 && contact.getMine()) {
            holder.tick_imgview.setVisibility(View.VISIBLE);
            if (contact.getSent_msg_success().equals("1")) {
                holder.tick_imgview.setImageResource(R.drawable.sent);
            }
            if (contact.getSent_msg_success().equals("0")) {
                holder.tick_imgview.setImageResource(R.drawable.time_icon);
            }

            if (contact.getDeliver_msg_success().equals("1")) {
                holder.tick_imgview.setImageResource(R.drawable.delivered);
            }
            if (contact.getRead_msg_success().equals("1")) {
                holder.tick_imgview.setImageResource(R.drawable.read);
            }
            if (contact.getSent_msg_success().equals("2")) {
                holder.tick_imgview.setImageResource(R.drawable.sent);
            }
        } else
            holder.tick_imgview.setVisibility(View.GONE);


        return convertView;
    }


    public void delete_user_chat_history(Recentmodel recentmodel, int pos, final String remotejid) {

        chatFragment.Show_Dialog(recentmodel, pos, remotejid);

		*/
/*new AlertDialog.Builder(activity)
		.setTitle("Delete Chat")
		.setMessage("Are you sure you want to delete chat history?")
		.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int which) {

				GlobalData.dbHelper.deleteParticularUserHistory(remotejid)	;
				GlobalData.dbHelper.deleteRecentParticularrow(remotejid);	

				GlobalData.dbHelper.updateContactmsgData(remotejid, "", "");								
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
		 *//*

    }

    private static class Recentchatholder {
        TextView rc_usernametext, unread_count, rc_msgtime;
        EmojiconTextView rc_msg;
        FrameLayout mTopLayout;
        LinearLayout framelayout;
        ImageView rc_userpicuppr, onlineimage, tick_imgview;
        CircularImageView rc_userpic;
        FrameLayout unreadcount_lay;
        RelativeLayout rc_userlay, status_layout;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ArrayList<Recentmodel> getChatArrayList() {
        return recentmodels;
    }

    public void setRecentArrayList(ArrayList<Recentmodel> clubBeanArrayList) {
        this.recentmodels = clubBeanArrayList;
        notifyDataSetChanged();
    }

    public void setLastMsgAndDateList(ArrayList<String> msgList, ArrayList<String> msgDateList) {
        lastMsgList = msgList;
        lastMsgDateList = msgDateList;
    }

    public int getIsClickViewProfile() {
        return isClickViewProfile;
    }


    public void setIsClickViewProfile(int isClickViewProfile) {
        this.isClickViewProfile = isClickViewProfile;
    }

    public void showImageDialog(Recentmodel recentmodel) {
        View view = inflater.inflate(R.layout.image_dialog, null);
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.profile_dialog);
        if (recentmodel.getUserpic() != null)
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(recentmodel.getUserpic(), 0, recentmodel.getUserpic().length));
        else if(recentmodel.getIsgroup()==1)
            imageView.setImageResource(R.drawable.profile_pic);
        else if(recentmodel.getIsgroup()==2)
            imageView.setImageResource(R.drawable.icon3);
        else
            imageView.setImageResource(R.drawable.profile_pic);
        dialog.show();
    }
}*/
