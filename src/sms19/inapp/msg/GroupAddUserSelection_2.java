package sms19.inapp.msg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;

public class GroupAddUserSelection_2 extends Fragment implements OnClickListener {
    private View v;
    private ListView gcontact_lv;
    //	private RelativeLayout agm_backbuttonlay, agm_nextbuttonlay;
    //private TextView agm_backbutton, agm_nextbutton;
    public static ArrayList<Contactmodel> memberlist;
    public static ArrayList<Contactmodel> selectedmemberlist;

    private MamberAdapter memberAdapter;
    private String groupname = "";
    private String grouppicpath = "";
    private InAppMessageActivity homeActivity;
    private String mLastTitle = "";
    private Button mNext_btn;
    private CheckBox mSelectAll;
    private CheckBox mDeSelectAll;
    private HashMap<String, Contactmodel> hashMap = null;
    private TextView selected_count;
    private int LAST_HIDE_MENU = 3;
    private int [] position =null ;

    public static GroupAddUserSelection_2 newInstance(String titleName) {

        GroupAddUserSelection_2 fragment = new GroupAddUserSelection_2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        homeActivity = (InAppMessageActivity) getActivity();

        mLastTitle = homeActivity.getActionbar_title().getText().toString();

        homeActivity.getActionbar_title().setText("Add Group Members");

        homeActivity.getActionbar_title().setVisibility(View.VISIBLE);
        homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
        /*For Searching of contacts*/
        ConstantFields.HIDE_MENU = 83;
        homeActivity.invalidateOptionMenuItem();
        hashMap = Utils.getSelectedItem(getActivity());

        if (GlobalData.Newgroup) {
            memberlist = new ArrayList<Contactmodel>();
            memberlist = GlobalData.dbHelper.getContactForLoadMoreFromDBRegister("0");
            position = new int[memberlist.size()];
            for(int i =0;i<position.length-1;i++)
                position[i]=i;
            //memberlist=(GlobalData.dbHelper.getContactfromDB());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_addgroup_member, container, false);

        Init();

        selectedmemberlist = new ArrayList<Contactmodel>();

        Bundle getData = getArguments();
        groupname = getData.getString("groupname");
        grouppicpath = getData.getString("grouppicpath");

        ArrayList<Contactmodel> arrayListNew = new ArrayList<Contactmodel>();

        if (hashMap != null) {

            if (hashMap.size() > 0) {
                Iterator entries = hashMap.entrySet().iterator();
                //int i=0;
                memberlist = new ArrayList<Contactmodel>();
                while (entries.hasNext()) {


                    Map.Entry entry = (Map.Entry) entries.next();

                    Contactmodel model = (Contactmodel) entry.getValue();

                    memberlist.add(model);
                }

                mSelectAll.setChecked(true);
                selectedmemberlist = new ArrayList<Contactmodel>();
                for (int j = 0; j < memberlist.size(); j++) {
                    memberlist.get(j).setIsselected(true);
                    selectedmemberlist.add(memberlist.get(j));
                }
                mDeSelectAll.setChecked(false);
                selected_count.setText(selectedmemberlist.size() + "");
                if (selectedmemberlist.size() <= 0) {
                    mNext_btn.setBackgroundColor(Color.GRAY);
                } else {
                    mNext_btn.setBackgroundColor(Color.parseColor("#E46C22"));
                }

            } else {

                if (GlobalData.Newgroup) {
                    memberlist = new ArrayList<Contactmodel>();
                    arrayListNew.addAll(GlobalData.dbHelper.getContactForLoadMoreFromDBRegister("0"));
                    memberlist.addAll(arrayListNew);


                }
            }


        } else {
            if (GlobalData.Newgroup) {
                memberlist = new ArrayList<Contactmodel>();
                arrayListNew.addAll(GlobalData.dbHelper.getContactForLoadMoreFromDBRegister("0"));
                memberlist.addAll(arrayListNew);
            }
        }


        if (memberlist != null) {
            if (memberlist.size() > 0) {
                Collections.sort(memberlist, new Contactmodel().new CustomComparatorSortByName());

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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_group_contact, menu);
        return true;
    }*/

    private void Init() {

		/*agm_backbuttonlay = (RelativeLayout) v.findViewById(R.id.agm_backbuttonlay);
        agm_nextbuttonlay = (RelativeLayout) v
				.findViewById(R.id.agm_nextbuttonlay);
		agm_backbutton = (TextView) v.findViewById(R.id.agm_backbutton);
		agm_nextbutton = (TextView) v.findViewById(R.id.agm_nextbutton);*/
        gcontact_lv = (ListView) v.findViewById(R.id.gcontact_lv);
        mNext_btn = (Button) v.findViewById(R.id.next_btn);

        mSelectAll = (CheckBox) v.findViewById(R.id.select_all);
        mDeSelectAll = (CheckBox) v.findViewById(R.id.deselect_all);
        selected_count = (TextView) v.findViewById(R.id.selected_count_ingroup);
        //agm_backbuttonlay.setOnClickListener(this);
        //agm_nextbuttonlay.setOnClickListener(this);
        //agm_backbutton.setOnClickListener(this);
        //agm_nextbutton.setOnClickListener(this);
        mNext_btn.setOnClickListener(this);
        homeActivity.getCamera_btn().setOnClickListener(this);

        mNext_btn.setBackgroundColor(Color.GRAY);


        mSelectAll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckBox isChecked = (CheckBox) v;

                if (isChecked.isChecked()) {
                    selectedmemberlist = new ArrayList<Contactmodel>();
                    for (int i = 0; i < memberlist.size(); i++) {
                        memberlist.get(i).setIsselected(true);
                        selectedmemberlist.add(memberlist.get(i));
                    }
                    mDeSelectAll.setChecked(false);
                    selected_count.setText(selectedmemberlist.size() + "");
                } else {

                    for (int i = 0; i < memberlist.size(); i++) {

                        memberlist.get(i).setIsselected(false);
                    }


                    for (int i = 0; i < selectedmemberlist.size(); i++) {

                        selectedmemberlist.remove(i);

                    }
                    selectedmemberlist = new ArrayList<Contactmodel>();

                }
                memberAdapter.notifyDataSetChanged();

                if (selectedmemberlist.size() <= 0) {
                    mNext_btn.setBackgroundColor(Color.GRAY);
                } else {
                    mNext_btn.setBackgroundColor(Color.parseColor("#E46C22"));
                }
                selected_count.setText(selectedmemberlist.size() + "");


            }
        });


        mDeSelectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {

                    for (int i = 0; i < memberlist.size(); i++) {

                        memberlist.get(i).setIsselected(false);
                    }
                    mSelectAll.setChecked(false);
                }
                memberAdapter.notifyDataSetChanged();


            }
        });


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        /*case R.id.agm_backbuttonlay:
            getActivity().onBackPressed();
			break;
		case R.id.agm_backbutton:
			getActivity().onBackPressed();
			break;*/
            case R.id.next_btn:

                Addfragment();
                break;
			/*case R.id.agm_nextbutton:
			Addfragment();
			break;*/

            default:
                break;
        }
    }

    public void Addfragment() {

        if (selectedmemberlist != null && selectedmemberlist.size() > 0) {

            if (GlobalData.GROUP_PARTICIPANT_LIMIT >= selectedmemberlist.size()) {

                Bundle data = new Bundle();
                data.putString("groupname", groupname);
                data.putString("grouppicpath", grouppicpath);
                GlobalData.Newgroup = false;
                Fragment add_Gfinal_frag = new GroupAddFinal();
                add_Gfinal_frag.setArguments(data);
                homeActivity.callFragmentWithAddBack(add_Gfinal_frag, "agf_frag");
                Toast.makeText(getActivity().getApplicationContext(),
                        "Selected users are " + String.valueOf(selectedmemberlist.size()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Group participant limit should be less then " + String.valueOf(GlobalData.GROUP_PARTICIPANT_LIMIT), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void filter(CharSequence sequence){
        if(memberAdapter!=null)
        memberAdapter.getFilter().filter(sequence);
    }

    class MamberAdapter extends BaseAdapter implements Filterable {
        public ArrayList<Contactmodel> tempmemberlist;

        public MamberAdapter() {
            // TODO Auto-generated constructor stub
            tempmemberlist = memberlist;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return memberlist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return memberlist.get(position);
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
                // holder.userpicuppr = (ImageView) convertView
                // .findViewById(R.id.userpicuppr);
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

                        memberlist.get(position).setIsselected(true);
                        selectedmemberlist.add(memberlist.get(position));

                        mNext_btn.setBackgroundColor(Color.parseColor("#E46C22"));
                        if (memberlist.size() == selectedmemberlist.size()) {
                            mSelectAll.setChecked(true);
                        }

                    } else {
                        String id = memberlist.get(position)
                                .getRemote_jid();

                        mSelectAll.setChecked(false);

                        for (int i = 0; i < selectedmemberlist.size(); i++) {

                            if (id.equals(selectedmemberlist.get(i)
                                    .getRemote_jid())) {
                                memberlist.get(position).setIsselected(
                                        false);
                                selectedmemberlist.remove(i);
                                break;
                            }
                        }
                        if (selectedmemberlist.size() <= 0) {
                            mNext_btn.setBackgroundColor(Color.GRAY);
                        }

                    }
                    memberAdapter.notifyDataSetChanged();
                    selected_count.setText(selectedmemberlist.size() + "");


                }
            });



			/*holder.user_checkbox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {

								memberlist.get(position).setIsselected(true);
								selectedmemberlist.add(memberlist.get(position));

								mNext_btn.setBackgroundColor(Color.parseColor("#E46C22"));

							} else {
								String id = memberlist.get(position)
										.getRemote_jid();
								for (int i = 0; i < selectedmemberlist.size(); i++) {

									if (id.equals(selectedmemberlist.get(i)
											.getRemote_jid())) {
										memberlist.get(position).setIsselected(
												false);
										selectedmemberlist.remove(i);
										break;
									}
								}
								if(selectedmemberlist.size()<=0){
									mNext_btn.setBackgroundColor(Color.GRAY);
								}

							}
							memberAdapter.notifyDataSetChanged();
						}
					});*/

            holder.usernametext.setText(memberlist.get(position).getName());
            if (memberlist.get(position).isIsselected()) {
                holder.user_checkbox.setChecked(true);
            } else {
                holder.user_checkbox.setChecked(false);
            }
            String status = "";
            if (memberlist.get(position).getStatus() != null) {
                status = memberlist.get(position).getStatus().trim();
            }

            Bitmap pic = null;
            if (memberlist.get(position).getAvatar() != null) {
                pic = BitmapFactory.decodeByteArray(memberlist.get(position)
                                .getAvatar(), 0,
                        memberlist.get(position).getAvatar().length);
                //pic = Utils.getResizedBitmap(pic, GlobalData.profilepicthmb,GlobalData.profilepicthmb);
            } else {
                Drawable drawable = getResources().getDrawable(
                        R.drawable.profileimg);
                pic = Utils.drawableToBitmap(drawable);
                //pic = Utils.getResizedBitmap(pic, GlobalData.profilepicthmb,GlobalData.profilepicthmb);
            }
            if (status.equals("1")) {
                //pic = Utils.getCircularBitmapWithBorder(pic, 5, Color.GREEN);

                //holder.userpic.setImageBitmap(pic);

            } else {
                //pic = Utils.getCircularBitmapWithBorder(pic, 5,Color.TRANSPARENT);
                //holder.userpic.setImageBitmap(pic);

            }


            if (status.equals("1")) {

                holder.onlineimage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.online));
                holder.userpic.setImageBitmap(pic);


            } else {

                holder.onlineimage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.offline));
                holder.userpic.setImageBitmap(pic);

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
                        memberlist = tempmemberlist; // has the filtered values
                        notifyDataSetChanged();
                    } else {

                        memberlist = (ArrayList<Contactmodel>) results.values; // has
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

                    if (memberlist == null) {
                        memberlist = new ArrayList<Contactmodel>(tempmemberlist); // saves
                        // the
                        // original
                        // data
                        // in
                        // mOriginalValues
                    }

                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = memberlist.size();
                        results.values = memberlist;
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
        // ImageView userpicuppr;
        CheckBox user_checkbox;
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        homeActivity.getActionbar_title().setText(mLastTitle);
        ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
        homeActivity.invalidateOptionMenuItem();
    }

}
