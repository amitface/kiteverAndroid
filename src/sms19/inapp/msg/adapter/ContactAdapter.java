package sms19.inapp.msg.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.Settings;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.kitever.android.R;
import com.kitever.app.context.BaseApplicationContext;
import com.kitever.app.context.CustomStyle;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.model.Contactmodel;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class ContactAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Contactmodel> contactlist;
    private LayoutInflater inflater;
    private View.OnClickListener clickListener;
    private android.util.SparseBooleanArray mSelectedItemsIds;
    private HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();

    private int counter;

    public ContactAdapter(Activity activity, ArrayList<Contactmodel> contactlist) {

        this.activity = activity;
        this.mSelectedItemsIds = new SparseBooleanArray();
        this.contactlist = contactlist;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {

        if (contactlist == null) {
            contactlist = new ArrayList<Contactmodel>();
        }
        return contactlist.size();
    }

	/*
     * @Override public Object getItem(int position) { return null; }
	 */

    @Override
    public Contactmodel getItem(int position) {
        // TODO Auto-generated method stub
        return contactlist.get(position);
    }

    public void remove(Contactmodel object) {
        contactlist.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position, View view) {
        selectView(position, !mSelectedItemsIds.get(position), view);
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value, View view) {

        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        // changeBackgroundColor(view, value);

        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public void changeBackgroundColor(View view, boolean hasSelectedItem,
                                      int poss) {
        if (hasSelectedItem) {
            contactlist.get(poss).setCheckConact(true);
            hashMap.put(poss, poss);

        } else {
            contactlist.get(poss).setCheckConact(false);
            hashMap.remove(poss);
        }
        notifyDataSetChanged();
    }

    public void unSelectItem() {
        hashMap = new HashMap<Integer, Integer>();
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = null;

        if (view == null) {

            view = inflater.inflate(R.layout.row_contact, null, true);
            holder = new ViewHolder();
            holder.mTopLayout = (LinearLayout) view
                    .findViewById(R.id.navilayout);
            holder.agf_usernametext = (TextView) view.findViewById(R.id.name);
            holder.agf_custom_status = (TextView) view.findViewById(R.id.msg);

            holder.agf_usernametext.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            holder.agf_custom_status.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

            setRobotoThinFont(holder.agf_usernametext,activity);
            setRobotoThinFont(holder.agf_custom_status,activity);

            holder.profile_image = (sms19.inapp.msg.CircularImageView) view
                    .findViewById(R.id.profile_image);
            // holder.profile_image= (ImageView)
            // view.findViewById(R.id.profile_image);

            holder.mBroadCast = (ImageView) view.findViewById(R.id.broadcast);
            holder.mInvite = (ImageView) view.findViewById(R.id.invite);
            holder.mGroup = (ImageView) view.findViewById(R.id.group);
            holder.onlineimage = (ImageView) view
                    .findViewById(R.id.onlineimage);
            holder.check_box = (CheckBox) view.findViewById(R.id.check_box);

            holder.mTopLayout.setOnClickListener(clickListener);
            holder.mBroadCast.setOnClickListener(clickListener);
            holder.mInvite.setOnClickListener(clickListener);
            holder.mGroup.setOnClickListener(clickListener);
            holder.profile_image.setOnClickListener(clickListener);

            // parent.addView(view, position);
            view.setTag(R.layout.row_contact, holder);

        } else {
            holder = (ViewHolder) view.getTag(R.layout.row_contact);
        }

        holder.profile_image.setTag(position);
        holder.mTopLayout.setTag(position);
        /*holder.profile_image.setTag(position);*/
        holder.mBroadCast.setTag(position);
        holder.mInvite.setTag(position);
        holder.mGroup.setTag(position);

        if (contactlist.size() > 0) {

            if (hashMap.containsKey(position)) {
                holder.check_box.setChecked(true);

            } else {
                holder.check_box.setChecked(false);
            }

            if (hashMap.size() > 0) {
                holder.mTopLayout.setClickable(false);
                holder.profile_image.setClickable(false);
                holder.mBroadCast.setClickable(false);
                holder.mInvite.setClickable(false);
                holder.mGroup.setClickable(false);

            }

            if (hashMap.size() <= 0) {
                holder.check_box.setChecked(false);
                holder.mTopLayout.setClickable(true);
                holder.profile_image.setClickable(true);

                holder.mBroadCast.setClickable(true);
                holder.mInvite.setClickable(true);
                holder.mGroup.setClickable(true);
            }

            final Contactmodel contact = contactlist.get(position);

            holder.agf_usernametext.setText(contact.getName());

            holder.agf_usernametext.setText(contact.getName());
            holder.agf_custom_status.setText(contact.getNumber());

            String status = "0";
            if (contact.getStatus() != null) {
                status = contact.getStatus().trim();
            }
            //for image apply
            Bitmap pic = null;
//             if (contact.getAvatar() != null && contact.getAvatar().length >
//             0) {
			if (contact.getImageUrl() != null
					&& !contact.getImageUrl().equalsIgnoreCase("")
					&& !contact.getImageUrl().equalsIgnoreCase("null")) {

                //get Avatar is in byteArray , so value mustbe very long.
                if(contact.getAvatar()!=null && contact.getAvatar().length>15)
                {
                    pic = BitmapFactory.decodeByteArray(contact.getAvatar(), 0,
                            contact.getAvatar().length);

                    holder.profile_image.setImageBitmap(/* pic */Bitmap
                            .createScaledBitmap(pic, 120, 120, false));
                }else
                {
                    final ImageView imageView = holder.profile_image;
                    Picasso.with(activity).load(contact.getImageUrl()).into(holder.profile_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            GlobalData.dbHelper.updateByteArrayInContacts(bytes,contact.getRemote_jid());
                        }

                        @Override
                        public void onError() {
                            imageView.setImageResource(R.drawable.profileimg);
                        }
                    });
//                    getProfileImage(contact.getImageUrl(), holder.profile_image);
                }

            } else {
                // Drawable drawable =
                // activity.getResources().getDrawable(R.drawable.profileimg);
                // pic = Utils.drawableToBitmap(drawable);

                holder.profile_image.setImageResource(R.drawable.profileimg);
            }

            if (status.equals("1")) {

                holder.onlineimage.setImageDrawable(activity.getResources()
                        .getDrawable(R.drawable.online));

            } else {

                holder.onlineimage.setImageDrawable(activity.getResources()
                        .getDrawable(R.drawable.offline));

            }

            if (contact.getIsRegister() == 0) {
                holder.mInvite.setVisibility(View.VISIBLE);
                holder.mGroup.setVisibility(View.GONE);
                holder.onlineimage.setVisibility(View.GONE);
                holder.mTopLayout.setEnabled(false);
                holder.profile_image.setEnabled(false);
            } else {
                holder.mInvite.setVisibility(View.GONE);
                holder.mGroup.setVisibility(View.VISIBLE);
                holder.onlineimage.setVisibility(View.VISIBLE);
                holder.mTopLayout.setEnabled(true);
                holder.profile_image.setEnabled(true);
            }

            if (holder.check_box.isChecked()) {
                int color = Color.GRAY;
                view.setBackgroundColor(color);
            } else {
                int color = Color.WHITE;
                view.setBackgroundColor(color);
            }
        }
        return view;
    }

    private class ViewHolder {

        LinearLayout mTopLayout;
        TextView agf_usernametext, agf_custom_status;
        ImageView mInvite, mBroadCast, mGroup, onlineimage;
        sms19.inapp.msg.CircularImageView profile_image;
        CheckBox check_box;

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

        counter++;
        this.contactlist = clubBeanArrayList;

        generateNoteOnSD("kit_contact_" + counter, clubBeanArrayList.toString());

    }

    public void generateNoteOnSD(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(),
                    "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            // Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getProfileImage(String url,
                                 final sms19.inapp.msg.CircularImageView profile_image) {
        try {

            url = "http://" + url;
            ImageRequest downloadImage = new ImageRequest(url,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            try {
                                profile_image.setImageBitmap(response);

                                // ContextWrapper cw = new
                                // ContextWrapper(activity);
                                // // path to
                                // /data/data/yourapp/app_data/imageDir
                                // File directory = cw.getDir("KiteverImageDir",
                                // Context.MODE_PRIVATE);
                                // // Create imageDir
                                // File mypath = new File(directory,
                                // "profile.jpg");
                                // OutputStream output = new
                                // FileOutputStream(mypath);
                                // output.write(response.);
                            } catch (Exception e) {
                                // TODO: handle exception
                                profile_image
                                        .setImageResource(R.drawable.profileimg);
                            }

                        }
                    }, 0, 0, null, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    profile_image
                            .setImageResource(R.drawable.profileimg);
                }
            });
            RequestQueue requestQueue = Volley
                    .newRequestQueue(BaseApplicationContext.baseContext);
            requestQueue.add(downloadImage);
        } catch (Exception e) {
            // TODO: handle exception
            profile_image
                    .setImageResource(R.drawable.profileimg);
        }
    }
}
