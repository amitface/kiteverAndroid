package sms19.inapp.msg.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sms19.inapp.msg.model.Contactmodel;
import com.kitever.android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewContactAdapter extends ArrayAdapter<Contactmodel> {
	// Declare Variables
	private Context context;
	private LayoutInflater inflater;
	private List<Contactmodel> worldpopulationlist;
	private SparseBooleanArray mSelectedItemsIds;
	private HashMap<Integer, Integer>hashMap =new HashMap<Integer, Integer>();


	private Activity activity;
	private ArrayList<Contactmodel> contactlist;

	private View.OnClickListener clickListener;


	public NewContactAdapter(Activity context1, int resourceId,ArrayList<Contactmodel> worldpopulationlist) {
		super(context1, resourceId, worldpopulationlist);
		this.mSelectedItemsIds = new SparseBooleanArray();
		this.activity=context1;
		this.context = context1;
		this.contactlist=worldpopulationlist;
		this.worldpopulationlist = worldpopulationlist;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		if(worldpopulationlist==null){
			worldpopulationlist=new ArrayList<Contactmodel>();
		}

		return worldpopulationlist.size();
	}


	@Override
	public Contactmodel getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final ViewHolder holder;
		
		
		int pos=-1;
		if(view!=null){

			try {
				pos=(Integer)view.getTag();

				if(position!=pos){
					view=null;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}

		if (view == null) {
			view = inflater.inflate(R.layout.row_contact, null,true);
			holder = new ViewHolder();
			holder.mTopLayout=(LinearLayout)view.findViewById(R.id.navilayout);
			holder.agf_usernametext = (TextView) view.findViewById(R.id.name);
			holder.agf_custom_status = (TextView) view.findViewById(R.id.msg);
			holder.profile_image= (sms19.inapp.msg.CircularImageView) view.findViewById(R.id.profile_image);
			//holder.profile_image= (ImageView) view.findViewById(R.id.profile_image);
  
			holder.mBroadCast= (ImageView) view.findViewById(R.id.broadcast);
			holder.mInvite= (ImageView) view.findViewById(R.id.invite);
			holder.mGroup= (ImageView) view.findViewById(R.id.group);
			holder.onlineimage= (ImageView) view.findViewById(R.id.onlineimage);
			holder.check_box= (CheckBox) view.findViewById(R.id.check_box);




			holder.mTopLayout.setOnClickListener(clickListener);
			holder.mBroadCast.setOnClickListener(clickListener);
			holder.mInvite.setOnClickListener(clickListener);
			holder.mGroup.setOnClickListener(clickListener);
			holder.profile_image.setOnClickListener(clickListener);


			//parent.addView(view, position);
			view.setTag(R.layout.row_contact,holder);

		} else {
			holder = (ViewHolder) view.getTag(R.layout.row_contact);
		}
		
		

		holder.profile_image.setTag(position);
		holder.mTopLayout.setTag(position);

		holder.mBroadCast.setTag(position);
		holder.mInvite.setTag(position);
		holder.mGroup.setTag(position);
		view.setTag(position);


		if(hashMap.containsKey(position)){
			holder.check_box.setChecked(true);
			
		}else{
			holder.check_box.setChecked(false);
		}
		
		if(hashMap.size()>0){
			holder.mTopLayout.setClickable(false);
			holder.profile_image.setClickable(false);
			holder.mBroadCast.setClickable(false);
			holder.mInvite.setClickable(false);
			holder.mGroup.setClickable(false);
			
			
		}
		
		
		if(hashMap.size()<=0){
			holder.check_box.setChecked(false);
			holder.mTopLayout.setClickable(true);
			holder.profile_image.setClickable(true);
			
			holder.mBroadCast.setClickable(true);
			holder.mInvite.setClickable(true);
			holder.mGroup.setClickable(true);
		}

if(contactlist.size()>0){

		Contactmodel contact = contactlist.get(position);

		holder.agf_usernametext.setText(contact.getName());







		holder.agf_usernametext.setText(contact.getName());
		holder.agf_custom_status.setText(contact.getNumber());

		String status = "0";
		if (contact.getStatus() != null) {
			status = contact.getStatus().trim();
		}

		Bitmap pic = null;
		if (contact.getAvatar() != null) {
			pic = BitmapFactory.decodeByteArray(contact.getAvatar(), 0,contact.getAvatar().length);
			
			holder.profile_image.setImageBitmap(/*pic*/Bitmap.createScaledBitmap(pic, 120, 120, false));

		//	Picasso.with(context).load(contact.getAvatar()).resize(50, 50).centerCrop().into(holder.profile_image)
			
		} else {
			//Drawable drawable = activity.getResources().getDrawable(R.drawable.profileimg);
			//pic = Utils.drawableToBitmap(drawable);
			
			holder.profile_image.setImageResource(R.drawable.profileimg);
		}

		if (status.equals("1")) {

			holder.onlineimage.setImageDrawable(activity.getResources().getDrawable(R.drawable.online));




		} else {

			holder.onlineimage.setImageDrawable(activity.getResources().getDrawable(R.drawable.offline));


		}



		if(contact.getIsRegister()==0){
			holder.mInvite.setVisibility(View.VISIBLE);
			holder.mGroup.setVisibility(View.GONE);
			holder.onlineimage.setVisibility(View.GONE);
			holder.mTopLayout.setEnabled(false);
			holder.profile_image.setEnabled(false);
		}else{
			holder.mInvite.setVisibility(View.GONE);
			holder.mGroup.setVisibility(View.VISIBLE);
			holder.onlineimage.setVisibility(View.VISIBLE);
			holder.mTopLayout.setEnabled(true);
			holder.profile_image.setEnabled(true);
		}




		if(holder.check_box.isChecked()){
			int color=Color.GRAY;
			view.setBackgroundColor(color);
		}else{
			int color=Color.WHITE;
			view.setBackgroundColor(color);
		}

}
		return view;
	}

	@Override
	public void remove(Contactmodel object) {
		worldpopulationlist.remove(object);
		notifyDataSetChanged();
	}

	public List<Contactmodel> getWorldPopulation() {
		return worldpopulationlist;
	}

	public void toggleSelection(int position,View view) {
		selectView(position, !mSelectedItemsIds.get(position),view);
	}

	public void removeSelection() {
		mSelectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}

	public void selectView(int position, boolean value,View view) {



		if (value)
			mSelectedItemsIds.put(position, value);
		else
			mSelectedItemsIds.delete(position);

		//changeBackgroundColor(view, value);

		notifyDataSetChanged();
	}

	public int getSelectedCount() {
		return mSelectedItemsIds.size();
	}

	public void changeBackgroundColor(View view,boolean hasSelectedItem,int poss){
		if(hasSelectedItem){

			worldpopulationlist.get(poss).setCheckConact(true);
			hashMap.put(poss, poss);


		}else{
		
			worldpopulationlist.get(poss).setCheckConact(false);
			hashMap.remove(poss);
		
		}
		notifyDataSetChanged();
	}

	public void unSelectItem(){
		hashMap=new HashMap<Integer, Integer>();
		notifyDataSetChanged();
	}


	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}
	private class ViewHolder {

		LinearLayout mTopLayout;
		TextView agf_usernametext, agf_custom_status;
		ImageView mInvite,mBroadCast,mGroup,onlineimage;
		sms19.inapp.msg.CircularImageView profile_image;
		//ImageView profile_image;
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
		this.worldpopulationlist=clubBeanArrayList;
		this.contactlist = clubBeanArrayList;

	}






}
