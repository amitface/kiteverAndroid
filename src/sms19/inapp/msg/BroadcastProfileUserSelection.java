package sms19.inapp.msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Rest;

import com.kitever.android.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BroadcastProfileUserSelection extends Fragment implements OnClickListener {
    private View v;
    private ListView gcontact_lv;
    public static ArrayList<Contactmodel> memberlist;
    public static ArrayList<Contactmodel> selectedmemberlistOnProfile;

    private MamberAdapter memberAdapter;
    private String groupname = "";
    private String grouppicpath = "";
    private InAppMessageActivity homeActivity;
    private String mLastTitle = "";
    private Button mNext_btn;
    private CheckBox mSelectAll;
    private CheckBox mDeSelectAll;
    private int LAST_HIDE_MENU = 0;
    private ArrayList<Contactmodel> sms19AppUsers = new ArrayList<Contactmodel>();
    private ArrayList<Contactmodel> AppuserList = new ArrayList<Contactmodel>();
    String previousrowid = "0";
    String lastrowid = "-1";
    private LinearLayout loadmore_progress;
    private TextView selected_count;

    private ArrayList<Contactmodel> hashMapAllusers = new ArrayList<Contactmodel>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (InAppMessageActivity) getActivity();

        mLastTitle = homeActivity.getActionbar_title().getText().toString();

        homeActivity.getActionbar_title().setText("Add Group Members");

        homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);


        homeActivity.groupActionBarControlIsVisual();
        homeActivity.getLayout_name_status().setVisibility(View.GONE);
        homeActivity.getmActionBarImage().setVisibility(View.GONE);
        homeActivity.getActionbar_title().setVisibility(View.VISIBLE);

        LAST_HIDE_MENU = ConstantFields.HIDE_MENU;
        ConstantFields.HIDE_MENU = 3;
        homeActivity.invalidateOptionMenuItem();


        if (GlobalData.Newgroup) {
            memberlist = new ArrayList<Contactmodel>();
            memberlist = (GlobalData.dbHelper.getContactfromDB());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_addgroup_member, container, false);

        Init();

        selectedmemberlistOnProfile = new ArrayList<Contactmodel>();
        hashMapAllusers = new ArrayList<Contactmodel>();


        memberlist = new ArrayList<Contactmodel>();
        ArrayList<Contactmodel> appUserList = (GlobalData.dbHelper.getContactfromDBOnlyRegister());
        AppuserList = appUserList;
        ArrayList<Contactmodel> nonAppUserList = (GlobalData.dbHelper.getContactfromDBUnregister());

        sms19AppUsers = new ArrayList<Contactmodel>();
        //sms19AppUsers.addAll(GlobalData.dbHelper.getContactfromDBOnlySMS19RowBase(previousrowid));
        sms19AppUsers.addAll(GlobalData.dbHelper.getContactfromDBSMS19All(previousrowid));

        if (sms19AppUsers != null) {
            if (sms19AppUsers.size() > 0) {
                nonAppUserList.addAll(sms19AppUsers);
            }
        }

        ArrayList<Contactmodel> arrayList = new ArrayList<Contactmodel>();
        arrayList = sortByAZBoth(appUserList, nonAppUserList);
        memberlist.addAll(arrayList);


        for (int i = 0; i < memberlist.size(); i++) {

            if (BroadCastGroupProfile.gmemberhasmap != null) {
                if (!BroadCastGroupProfile.gmemberhasmap.containsKey(memberlist.get(i).getRemote_jid())) {
                    if (!InAppMessageActivity.myModel.getRemote_jid().equalsIgnoreCase(memberlist.get(i).getRemote_jid())) {
                        hashMapAllusers.add(memberlist.get(i));
                    }
                }
            } else {
                hashMapAllusers.add(memberlist.get(i));
            }


        }
        memberAdapter = new MamberAdapter();
        gcontact_lv.setAdapter(memberAdapter);

        homeActivity.getCamera_btn().setOnClickListener(this);

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        memberAdapter.notifyDataSetChanged();
    }

    private void Init() {

        gcontact_lv = (ListView) v.findViewById(R.id.gcontact_lv);
        mNext_btn = (Button) v.findViewById(R.id.next_btn);

        mSelectAll = (CheckBox) v.findViewById(R.id.select_all);
        mDeSelectAll = (CheckBox) v.findViewById(R.id.deselect_all);
        mNext_btn.setOnClickListener(this);
        homeActivity.getCamera_btn().setOnClickListener(this);
        selected_count = (TextView) v.findViewById(R.id.selected_count_ingroup);

        mNext_btn.setBackgroundColor(Color.GRAY);

        loadmore_progress = (LinearLayout) v.findViewById(R.id.loadmore_progress);
        loadmore_progress.setVisibility(View.GONE);
        loadmore_progress.setOnClickListener(this);


        mSelectAll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckBox box = (CheckBox) v;
                if (box.isChecked()) {
                    selectedmemberlistOnProfile = new ArrayList<Contactmodel>();
                    for (int i = 0; i < hashMapAllusers.size(); i++) {
                        hashMapAllusers.get(i).setIsselected(true);
                        selectedmemberlistOnProfile.add(hashMapAllusers.get(i));

                    }
                    mDeSelectAll.setChecked(false);
                    selected_count.setText(selectedmemberlistOnProfile.size() + "");
                } else {

                    for (int i = 0; i < hashMapAllusers.size(); i++) {

                        hashMapAllusers.get(i).setIsselected(false);
                    }


                    for (int i = 0; i < selectedmemberlistOnProfile.size(); i++) {

                        selectedmemberlistOnProfile.remove(i);

                    }

                    selectedmemberlistOnProfile = new ArrayList<Contactmodel>();
                    selected_count.setText(selectedmemberlistOnProfile.size() + "");

                }
                memberAdapter.notifyDataSetChanged();
                if (selectedmemberlistOnProfile.size() <= 0) {
                    mNext_btn.setBackgroundColor(Color.GRAY);
                } else {
                    mNext_btn.setBackgroundColor(Color.parseColor("#E46C22"));
                }

            }
        });


        mDeSelectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {

                    for (int i = 0; i < hashMapAllusers.size(); i++) {

                        hashMapAllusers.get(i).setIsselected(false);
                    }
                    mSelectAll.setChecked(false);
                }
                memberAdapter.notifyDataSetChanged();


            }
        });


/*		gcontact_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				final int lastItem = firstVisibleItem + visibleItemCount;

				if(memberlist!=null){
					if(lastItem==memberlist.size()){

						ArrayList<Contactmodel> arrayListNew=new ArrayList<Contactmodel>();
						arrayListNew.addAll((GlobalData.dbHelper.getContactfromDBOnlyRegister()));


						if(arrayListNew.size()!=AppuserList.size()){
							loadmore_progress.setVisibility(View.VISIBLE);
						}else{
							loadmore_progress.setVisibility(View.GONE);
						}
					}else{
						loadmore_progress.setVisibility(View.GONE);
					}
				}
			}
		});*/


    }

    class getMoreContactItem extends AsyncTask<Void, Void, Void> {
        ArrayList<Contactmodel> tempchatlist;
        int scrollposition = 0;
        ArrayList<Contactmodel> alreadyGetedList = new ArrayList<Contactmodel>();
        String lastrowid = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            alreadyGetedList.addAll(memberlist);
            tempchatlist = new ArrayList<Contactmodel>();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            if (!lastrowid.equalsIgnoreCase(previousrowid)) {
                lastrowid = previousrowid;
                if (previousrowid.equalsIgnoreCase("0")) {
                    try {
                        if (sms19AppUsers.size() > 0) {
                            previousrowid = sms19AppUsers.get(sms19AppUsers.size() - 1).getRow_id();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    tempchatlist.addAll(GlobalData.dbHelper.getContactfromDBOnlySMS19RowBase(previousrowid));

                } else {
                    tempchatlist.addAll(GlobalData.dbHelper.getContactfromDBOnlySMS19RowBase(alreadyGetedList.get(alreadyGetedList.size() - 1).getRow_id()));
                }


                if (tempchatlist.size() > 0) {
                    scrollposition = tempchatlist.size();

                    previousrowid = tempchatlist.get(tempchatlist.size() - 1).getRow_id();


                }


                alreadyGetedList.addAll(alreadyGetedList.size(), tempchatlist);

                memberlist.addAll(memberlist.size(), tempchatlist);


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (tempchatlist.size() == 0) {


            } else {


                if (!alreadyGetedList.get(alreadyGetedList.size() - 1).getRow_id().equalsIgnoreCase(previousrowid)) {
                    memberAdapter.notifyDataSetChanged();
                } else {
                    loadmore_progress.setVisibility(View.GONE);
                }

            }

        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.next_btn:

                BroadCastGroupProfile groupProfile = BroadCastGroupProfile.newInstance();
                if (groupProfile != null) {

                    int totalLength = selectedmemberlistOnProfile.size() + BroadCastGroupProfile.getGmemberlist().size();
                    if (GlobalData.BRAODCAST_GROUP_PARTICIPANT_LIMIT >= totalLength) {

					/*groupProfile.addGroupMemberAsync("", selectedmemberlistOnProfile);
				homeActivity.backPress();*/


                        //	boolean isClick=(Boolean)mNext_btn.getTag();
                        if (Utils.isDeviceOnline(getActivity())) {

                            AddMemberAsyncTask addMemberAsyncTask = new AddMemberAsyncTask();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                addMemberAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {

                                addMemberAsyncTask.execute();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Check your network connection", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Group participant limit should be less then " + String.valueOf(GlobalData.BRAODCAST_GROUP_PARTICIPANT_LIMIT), Toast.LENGTH_SHORT).show();
                    }

                }
                break;


            case R.id.loadmore_progress:
                getMoreContactItem asyncTask = new getMoreContactItem();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    asyncTask.execute();
                }

                break;

            default:
                break;
        }
    }


    private class AddMemberAsyncTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog = null;
        boolean isAdd = false;


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Adding participant...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }


        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {

            String response = "";
            try {
                BroadCastGroupProfile broadcastGroup = BroadCastGroupProfile.newInstance();
                String users_name = "";
                String USER_ID = Utils.getUserId(getActivity());
//				String postCountryCode="";
                try {

//					postCountryCode="+91";
                    String mobileNoStr = "";

                    String users_jid = "";
                    for (int i = 0; i < selectedmemberlistOnProfile.size(); i++) {
                        if (i == 0) {
                            users_jid = selectedmemberlistOnProfile.get(i).getRemote_jid();
                            users_name = selectedmemberlistOnProfile.get(i).getName();
                            mobileNoStr = selectedmemberlistOnProfile.get(i).getRemote_jid().split("@")[0];//Utils.remove91(selectedmemberlistOnProfile.get(i).getRemote_jid().split("@")[0]);
//							postCountryCode="+91";
                        } else {
                            users_jid = users_jid + "," + selectedmemberlistOnProfile.get(i).getRemote_jid();
                            users_name = users_name + "," + selectedmemberlistOnProfile.get(i).getName();
//							postCountryCode = postCountryCode + ","+ "+91";
                            mobileNoStr = mobileNoStr + "," + selectedmemberlistOnProfile.get(i).getRemote_jid().split("@")[0];

                        }
                    }
					
				/*	objser.AddParticipentinBroadcasetGroup(Request.Form["GroupJID"], Request.Form["UserJID"],
							Request.Form["UserId"], Request.Form["UserName"],
							Request.Form["UserNumber"]));*/

                    //response = rest.insertBroadcastGroup(stringUrl);
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("UserId", USER_ID));
                    nameValuePairs.add(new BasicNameValuePair("GroupJID", broadcastGroup.getRemote_jid()));
                    nameValuePairs.add(new BasicNameValuePair("UserNumber", mobileNoStr));
                    nameValuePairs.add(new BasicNameValuePair("UserName", users_name));
                    nameValuePairs.add(new BasicNameValuePair("UserJID", users_jid));
                    nameValuePairs.add(new BasicNameValuePair("Page", "AddBroadCastParticipate"));

                    Utils.printLog("Add BroadCast POST DATA:  " + nameValuePairs.toString());

                    Rest rest2 = Rest.getInstance();
                    //response = rest2.post(Apiurls.KIT19_POST_BASE_URL.replace("?Page=", ""), nameValuePairs);
                    response = rest2.post(Apiurls.KIT19_BASE_URL.replace("?Page=", ""), nameValuePairs);


                    Utils.printLog("Add BroadCast Participant URL:  " + Apiurls.KIT19_POST_BASE_URL.replace("?Page=", ""));
                    Utils.printLog("Add BroadCast Participant Response1112:  " + response);


                } catch (Exception e1) {
                    e1.printStackTrace();
                }


                try {
                    //if (response != null && response.trim().length() != 0) {
                    try {
                        BroadCastGroupProfile groupProfile = BroadCastGroupProfile.newInstance();
                        isAdd = groupProfile.addGroupMemberAsync("", selectedmemberlistOnProfile);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    //}

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {

                e.printStackTrace();
                isAdd = false;
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {

                progressDialog.dismiss();
                if (isAdd) {
                    BroadCastGroupProfile groupProfile = BroadCastGroupProfile.newInstance();
                    groupProfile.callHandler(true);
                } else {
                    Toast.makeText(getActivity(), "Failed Add Participants!", Toast.LENGTH_SHORT).show();
                }
                homeActivity.backPress();

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }


    public void Addfragment() {
        if (selectedmemberlistOnProfile != null && selectedmemberlistOnProfile.size() > 0) {
            Bundle data = new Bundle();
            data.putString("groupname", groupname);
            data.putString("grouppicpath", grouppicpath);
            GlobalData.Newgroup = false;
            Fragment add_Gfinal_frag = new GroupAddFinal();
            add_Gfinal_frag.setArguments(data);
            homeActivity.callFragmentWithAddBack(add_Gfinal_frag, "agf_frag");
            Toast.makeText(getActivity().getApplicationContext(),
                    "Selected users are " + String.valueOf(selectedmemberlistOnProfile.size()), Toast.LENGTH_SHORT).show();
        }

    }

    class MamberAdapter extends BaseAdapter implements Filterable {
        public ArrayList<Contactmodel> tempmemberlist;

        public MamberAdapter() {
            // TODO Auto-generated constructor stub
            tempmemberlist = hashMapAllusers;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return hashMapAllusers.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return hashMapAllusers.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            Mamberholder holder = null;
            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.gcontact_adapter,
                        parent, false);
                holder = new Mamberholder();

                holder.usernametext = (TextView) convertView
                        .findViewById(R.id.usernametext);
                holder.userpic = (sms19.inapp.msg.CircularImageView) convertView
                        .findViewById(R.id.rc_userpic);
                holder.onlineimage = (ImageView) convertView.findViewById(R.id.onlineimage);
                holder.user_checkbox = (CheckBox) convertView.findViewById(R.id.user_checkbox);

                convertView.setTag(holder);
            } else {
                holder = (Mamberholder) convertView.getTag();
            }


            holder.user_checkbox.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox box = (CheckBox) v;

                    if (box.isChecked()) {

                        hashMapAllusers.get(position).setIsselected(true);
                        selectedmemberlistOnProfile.add(hashMapAllusers.get(position));

                        mNext_btn.setBackgroundColor(Color.parseColor("#E46C22"));

                        if (hashMapAllusers.size() == selectedmemberlistOnProfile.size()) {
                            mSelectAll.setChecked(true);
                        }


                    } else {
                        String id = hashMapAllusers.get(position)
                                .getRemote_jid();
                        mSelectAll.setChecked(false);
                        for (int i = 0; i < selectedmemberlistOnProfile.size(); i++) {

                            if (id.equals(selectedmemberlistOnProfile.get(i)
                                    .getRemote_jid())) {
                                hashMapAllusers.get(position).setIsselected(
                                        false);
                                selectedmemberlistOnProfile.remove(i);
                                break;
                            }
                        }
                        if (selectedmemberlistOnProfile.size() <= 0) {
                            mNext_btn.setBackgroundColor(Color.GRAY);
                        }

                    }
                    memberAdapter.notifyDataSetChanged();
                    selected_count.setText(selectedmemberlistOnProfile.size() + "");


                }
            });


			/*holder.user_checkbox
			.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {

						hashMapAllusers.get(position).setIsselected(true);
						selectedmemberlistOnProfile.add(hashMapAllusers.get(position));

						mNext_btn.setBackgroundColor(Color.parseColor("#E46C22"));

					} else {
						String id = hashMapAllusers.get(position)
								.getRemote_jid();
						for (int i = 0; i < selectedmemberlistOnProfile.size(); i++) {

							if (id.equals(selectedmemberlistOnProfile.get(i)
									.getRemote_jid())) {
								hashMapAllusers.get(position).setIsselected(
										false);
								selectedmemberlistOnProfile.remove(i);
								break;
							}
						}
						if(selectedmemberlistOnProfile.size()<=0){
							mNext_btn.setBackgroundColor(Color.GRAY);
						}

					}
					memberAdapter.notifyDataSetChanged();
				}
			});*/

            holder.usernametext.setText(hashMapAllusers.get(position).getName());
            if (hashMapAllusers.get(position).isIsselected()) {
                holder.user_checkbox.setChecked(true);
            } else {
                holder.user_checkbox.setChecked(false);
            }
            String status = "";
            if (hashMapAllusers.get(position).getStatus() != null) {
                status = hashMapAllusers.get(position).getStatus().trim();
            }

            Bitmap pic = null;
            if (hashMapAllusers.get(position).getAvatar() != null) {
                pic = BitmapFactory.decodeByteArray(hashMapAllusers.get(position)
                                .getAvatar(), 0,
                        hashMapAllusers.get(position).getAvatar().length);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.profileimg);
                pic = Utils.drawableToBitmap(drawable);
            }


            if (status.equals("1")) {

                holder.onlineimage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.online));
                holder.userpic.setImageBitmap(pic);


            } else {

                holder.onlineimage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.offline));
                holder.userpic.setImageBitmap(pic);

            }

            if (hashMapAllusers.get(position).getIsRegister() == 0) {
                holder.onlineimage.setVisibility(View.GONE);
            } else {
                holder.onlineimage.setVisibility(View.VISIBLE);
            }


            return convertView;

        }

        @Override
        public Filter getFilter() {
            // TODO Auto-generated method stub
            Filter filter = new Filter() {

                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {

                    ArrayList<Contactmodel> values1 = (ArrayList<Contactmodel>) results.values;
                    if (constraint.equals("")) {
                        hashMapAllusers = tempmemberlist; // has the filtered values
                        notifyDataSetChanged();
                    } else {

                        hashMapAllusers = (ArrayList<Contactmodel>) results.values; // has
                        // the
                        // filtered
                        // values
                        notifyDataSetChanged(); // notifies the data with new
                        // filtered values
                    }
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults(); // Holds the
                    // results
                    // of a
                    // filtering
                    // operation
                    // in values
                    ArrayList<Contactmodel> FilteredArrList = new ArrayList<Contactmodel>();

                    if (hashMapAllusers == null) {
                        hashMapAllusers = new ArrayList<Contactmodel>(tempmemberlist); // saves
                        // the
                        // original
                        // data
                        // in
                        // mOriginalValues
                    }

                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = hashMapAllusers.size();
                        results.values = hashMapAllusers;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < tempmemberlist.size(); i++) {
                            String data = tempmemberlist.get(i).getName();
                            // String data = mDISPLAYEDvalues.get(i).getName();
                            if (data.toLowerCase().startsWith(
                                    constraint.toString())
                                    || data.toLowerCase().contains(
                                    constraint.toString())) {

                                FilteredArrList.add(tempmemberlist.get(i));

                            }
                        }
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }

    }

    class Mamberholder {
        TextView usernametext;
        ImageView onlineimage;
        sms19.inapp.msg.CircularImageView userpic;
        CheckBox user_checkbox;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
        homeActivity.getActionbar_title().setText(mLastTitle);

    }


    public ArrayList<Contactmodel> sortByAZBoth(ArrayList<Contactmodel> arrayList1, ArrayList<Contactmodel> arrayList2) {

        ArrayList<Contactmodel> contactlistContactNew = new ArrayList<Contactmodel>();

        if (arrayList1.size() > 0) {
            Collections.sort(arrayList1, new Contactmodel().new CustomComparatorSortByName());
        }
        if (arrayList2.size() > 0) {
            Collections.sort(arrayList2, new Contactmodel().new CustomComparatorSortByName());
        }
        contactlistContactNew = arrayList1;


        if (contactlistContactNew.size() > 0) {
            if (arrayList1.size() > 0) {

                for (int i = 0; i < arrayList2.size(); i++) {
                    contactlistContactNew.add(arrayList2.get(i));
                }

            } else {
                contactlistContactNew = arrayList2;
            }
        }

		/*contactlist=new  ArrayList<Contactmodel>();
		contactlist=contactlistContactNew;


		chatAdapter.setContactArrayList(contactlistContactNew);

		chatAdapter.notifyDataSetChanged();*/


        return contactlistContactNew;

    }


}

