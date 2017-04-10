package sms19.inapp.msg.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import java.util.ArrayList;
import java.util.HashMap;

import sms19.inapp.msg.GroupListFrgament;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.imoze.EmojiconTextView;
import sms19.inapp.msg.model.Groupmodel;
import sms19.inapp.msg.model.Recentmodel;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class GroupListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Groupmodel> recentmodels;
    private LayoutInflater inflater;
    private OnClickListener clickListener;

    ArrayList<Recentmodel> gmemberlist;
    private static HashMap<String, Recentmodel> gmemberhasmap;
    public static ArrayList<String> broadcastusersid;

    public GroupListAdapter(Activity activity, ArrayList<Groupmodel> recentmodels) {

        this.activity = activity;
        this.recentmodels = recentmodels;
        inflater = activity.getLayoutInflater();
        gmemberlist = new ArrayList<Recentmodel>();
        broadcastusersid = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        if (recentmodels == null) {
            recentmodels = new ArrayList<Groupmodel>();
        }
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

            convertView = inflater.inflate(R.layout.broadcast_groups_row, null);
            holder = new Recentchatholder();

            holder.onlineimage = (ImageView) convertView.findViewById(R.id.onlineimage);
            holder.delete_group = (ImageView) convertView.findViewById(R.id.delete_group);
            holder.profile_image = (ImageView) convertView.findViewById(R.id.profile_image);

            holder.nav_page = (LinearLayout) convertView.findViewById(R.id.nav_page);
            holder.nav_next = (LinearLayout) convertView.findViewById(R.id.nav_next);
            holder.nav2 = (LinearLayout) convertView.findViewById(R.id.nav2);

            holder.unreadcount_lay = (RelativeLayout) convertView.findViewById(R.id.unreadcount_lay);

            holder.unread_count = (TextView) convertView.findViewById(R.id.unread_count);
            holder.rc_usernametext = (TextView) convertView.findViewById(R.id.name);
            holder.msg = (TextView) convertView.findViewById(R.id.msg);


            holder.rc_usernametext.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            holder.msg.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

            setRobotoThinFont(holder.unread_count,activity);
            setRobotoThinFont(holder.unread_count,activity);
            setRobotoThinFont(holder.unread_count,activity);

            holder.nav_page.setOnClickListener(clickListener);
            //holder.delete_group.setOnClickListener(clickListener);
            holder.nav2.setOnClickListener(clickListener);
            convertView.setTag(R.layout.recent_chat_adapterlay, holder);
        } else {
            holder = (Recentchatholder) convertView.getTag(R.layout.recent_chat_adapterlay);
        }
        holder.nav2.setTag(position);
        holder.onlineimage.setVisibility(View.GONE);
        holder.unreadcount_lay.setVisibility(View.VISIBLE);
        holder.delete_group.setVisibility(View.GONE);
        holder.nav_next.setTag(position);
        holder.delete_group.setTag(position);
        holder.nav_page.setTag(position);

        final Groupmodel contact = recentmodels.get(position);
        holder.rc_usernametext.setText(contact.getGroupname());
        holder.msg.setText("Group users..");

        String isBroadCast = contact.getGroup_jid().split("@")[1];
        boolean isBroadCastFlag = false;
        if (isBroadCast.startsWith("Broadcast")) {
            isBroadCastFlag = true;
        }

        Bitmap pic = null;
        if (contact.getUser_pic() != null) {
            pic = BitmapFactory.decodeByteArray(contact.getUser_pic(), 0,
                    contact.getUser_pic().length);
            holder.profile_image.setImageBitmap(pic);

        } else if (isBroadCastFlag) {

            holder.profile_image.setImageResource(R.drawable.icon3);
        } else {
            Drawable drawable = activity.getResources().getDrawable(R.drawable.group_icon);
            try {
                pic = Utils.drawableToBitmap(drawable);
                holder.profile_image.setImageBitmap(pic);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (contact.getGroup_jid() != null) {
            if (!contact.getGroup_jid().equalsIgnoreCase("")) {
                setGroupmemberlist(contact.getGroup_jid(), holder.msg, holder.unread_count);
            }
        }

        holder.unreadcount_lay.setVisibility(View.GONE);

        holder.nav2.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                int pos = (Integer) v.getTag();
                GroupListFrgament recentmodel = GroupListFrgament.newInstance("");
                if (recentmodel != null) {

                    Recentmodel recentmodel2 = new Recentmodel();
                    String isBroadCast = contact.getGroup_jid().split("@")[1];
                    boolean isBroadCastFlag = false;
                    if (isBroadCast.startsWith("Broadcast")) {
                        isBroadCastFlag = true;
                        recentmodel2.setIsgroup(2);
                    } else {
                        recentmodel2.setIsgroup(1);
                    }

                    recentmodel.Show_Dialog(recentmodel2, pos, contact.getGroup_jid());
                    //delete_user_chat_history(contact,pos,contact.getGroup_jid());
                }
                return false;
            }
        });
        return convertView;
    }

    class Recentchatholder {
        TextView rc_usernametext, unread_count, rc_msgtime, msg;
        EmojiconTextView rc_msg;
        LinearLayout nav_page, nav2;
        ImageView delete_group, profile_image, rc_userpicuppr, onlineimage;
        RelativeLayout rc_userlay, unreadcount_lay, status_layout;
        LinearLayout nav_next;
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

    public void setGroupmemberlist(String remote_jid, TextView homeActivity, TextView unread) {
        try {
            if (gmemberlist.size() > 0) {
                gmemberlist.clear();
            }
            ArrayList<Recentmodel> temp = GlobalData.dbHelper.getGroupmemberListfromDB(remote_jid);

            if (temp != null && temp.size() > 0) {
                gmemberhasmap = new HashMap<String, Recentmodel>();
                gmemberlist.addAll(temp);

                Utils.printLog("gmemberlist size=" + gmemberlist.size());
                String name = "";
                for (int i = 0; i < gmemberlist.size(); i++) {

                    gmemberhasmap.put(gmemberlist.get(i).getRemote_jid(),
                            gmemberlist.get(i));
                }
                if (gmemberlist.size() > 0) {
                    name = Utils.getUsersNameWithGroupName(gmemberlist);
                    homeActivity.setText(name);
                }

                String isBroadCast = remote_jid.split("@")[1];
                boolean isBroadCastFlag = false;
                if (isBroadCast.startsWith("Broadcast")) {
                    isBroadCastFlag = true;
                }
            }
            unread.setText(String.valueOf(gmemberlist.size()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            unread.setText(String.valueOf("0"));
            e.printStackTrace();
        }
    }

    public void getBroadCastUsers(String remote_jid, TextView homeActivity, TextView unread) {

        broadcastusersid = GlobalData.dbHelper.getBroadcastmembersfromDB(remote_jid);
        try {
            Recentmodel contactmodel = new Recentmodel();
            for (int i = 0; i < broadcastusersid.size(); i++) {
                String jid = broadcastusersid.get(0);
                contactmodel = GlobalData.dbHelper.getsingleContactfromDB(jid);
            }
            if (contactmodel.getDisplayname().length() > 10) {
                homeActivity.setText(contactmodel.getDisplayname().subSequence(0, 8) + "..");
            } else {
                homeActivity.setText(contactmodel.getDisplayname() + "..");
            }
            unread.setText(String.valueOf(broadcastusersid.size()));
        } catch (Exception e) {

        }
    }
}
