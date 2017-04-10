package sms19.inapp.msg;

import java.util.ArrayList;
import java.util.Collections;

import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.contacts.AddBroadcastGroupActivity;

import static android.R.attr.fragment;
import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontcheckbox;
import static org.jivesoftware.smack.packet.RosterPacket.ItemType.from;

public class CreateBroadCast extends Fragment implements OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private View v;
    private InAppMessageActivity homeActivity;
    private String mLastTitle = "";

    private Button next_btn;
    private ListView gcontact_lv;
    private MamberAdapter memberAdapter;
    public static ArrayList<Contactmodel> memberlist;
    public static ArrayList<Contactmodel> AppUserList = new ArrayList<Contactmodel>();
    public static ArrayList<Contactmodel> selectedmemberlist;
    private int LAST_HIDE_MENU = 3;
    private CheckBox mSelectAll;
    private CheckBox mDeSelectAll;
    public static ArrayList<Contactmodel> sms19AppUsers = new ArrayList<Contactmodel>();
    private String previousrowid = "0";

    private LinearLayout loadmore_progress;
    private ProgressBar progress;
    private getAppUsersLoadAsyncTask appUsersLoadAsyncTask = null;
    private getLoadMoreNonAppContactAsyncTask loadMoreNonAppContactAsyncTask = null;
    private TextView selected_count;

    private Handler appuserHandler;
    private Handler nonAppuserHandler;
    private boolean isGroupCreateFromContacts = false;
    private String param1,groupName;

    public static CreateBroadCast getInstance(String param1) {

        CreateBroadCast fragment = new CreateBroadCast();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
           param1 = getArguments().getString(ARG_PARAM1);
            if(param1.equalsIgnoreCase("contact"))
                isGroupCreateFromContacts = true;

        }
        if (!isGroupCreateFromContacts) {
            homeActivity = (InAppMessageActivity) getActivity();

            homeActivity.groupActionBarControlIsVisual();
            homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
            mLastTitle = homeActivity.getActionbar_title().getText().toString();
            homeActivity.getLayout_name_status().setVisibility(View.GONE);
            homeActivity.getmActionBarImage().setVisibility(View.GONE);
            homeActivity.getActionbar_title().setVisibility(View.VISIBLE);

            homeActivity.getActionbar_title().setText("Create Broadcast");
            LAST_HIDE_MENU = ConstantFields.HIDE_MENU;
            ConstantFields.HIDE_MENU = 84;
            homeActivity.invalidateOptionMenuItem();

        }else
            getActivity().invalidateOptionsMenu();
        memberlist = new ArrayList<Contactmodel>();

        // getConactListFromDb();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        selectedmemberlist = new ArrayList<Contactmodel>();
        v = inflater.inflate(R.layout.create_broadcast, container, false);
        Init(v);
        inItHandlerAppUsersForLoadMore();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        memberlist = new ArrayList<Contactmodel>();
        // getLoadMoreParent(0);// 2 mean user load more both user types and one
        // mean only one types

		/*
         * BothTypeUserAsyncTask asyncTask= new BothTypeUserAsyncTask(); if
		 * (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
		 * asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); }else{
		 * asyncTask.execute(); }
		 */

        callAppUserHandler(0);

    }

    public void callAppUserHandler(int isAppuser) {
        android.os.Message msg = new android.os.Message();// comment m
        Bundle b = new Bundle();
        b.putInt("isAppuser", isAppuser);
        msg.setData(b);
        appuserHandler.sendMessage(msg);
    }

    private void inItHandlerAppUsersForLoadMore() {

        appuserHandler = new Handler() {
            @SuppressWarnings("unused")
            @Override
            public void handleMessage(android.os.Message msg) {

                super.handleMessage(msg);

                ArrayList<Contactmodel> tempchatlist = new ArrayList<Contactmodel>();
                ArrayList<Contactmodel> nonapp = new ArrayList<Contactmodel>();

                tempchatlist = new ArrayList<Contactmodel>();

                tempchatlist.addAll(GlobalData.dbHelper
                        .getContactForLoadMoreFromDBRegister(previousrowid));
                if (tempchatlist != null) {
                    if (tempchatlist.size() > 0) {
                        Collections
                                .sort(tempchatlist,
                                        new Contactmodel().new CustomComparatorSortByName());
                    }
                    nonapp.addAll(GlobalData.dbHelper
                            .getContactfromDBOnlySMS19RowBase2(previousrowid));
                    Collections
                            .sort(nonapp,
                                    new Contactmodel().new CustomComparatorSortByName());
                    tempchatlist.addAll(nonapp);

                } else {
                    nonapp.addAll(GlobalData.dbHelper
                            .getContactfromDBOnlySMS19RowBase2(previousrowid));
                }

                memberlist.addAll(tempchatlist);
                progress.setVisibility(View.GONE);
                memberAdapter = new MamberAdapter();
                gcontact_lv.setAdapter(memberAdapter);
//                memberAdapter.notifyDataSetChanged();
            }
        };
    }

    public void getAppUsersForLoadMore(int isAppuser) {
        progress.setVisibility(View.VISIBLE);
        // new getAppUsersLoadAsyncTask().execute(isAppuser );
        if (appUsersLoadAsyncTask == null) {
            appUsersLoadAsyncTask = new getAppUsersLoadAsyncTask();
            appUsersLoadAsyncTask.execute(isAppuser);
        } else {
            appUsersLoadAsyncTask.cancel(true);
            appUsersLoadAsyncTask = null;
            appUsersLoadAsyncTask = new getAppUsersLoadAsyncTask();
            appUsersLoadAsyncTask.execute(isAppuser);
        }

    }

    public void getNonAppUsersForLoadMore(int isNonAppuser) {
        progress.setVisibility(View.VISIBLE);
        // new getLoadMoreNonAppContactAsyncTask().execute();
        if (loadMoreNonAppContactAsyncTask == null) {
            loadMoreNonAppContactAsyncTask = new getLoadMoreNonAppContactAsyncTask();
            loadMoreNonAppContactAsyncTask.execute();
        } else {
            loadMoreNonAppContactAsyncTask.cancel(true);
            loadMoreNonAppContactAsyncTask = null;
            loadMoreNonAppContactAsyncTask = new getLoadMoreNonAppContactAsyncTask();
            loadMoreNonAppContactAsyncTask.execute();
        }

    }

    class getAppUsersLoadAsyncTask extends AsyncTask<Integer, Void, Void> {
        ArrayList<Contactmodel> tempchatlist;
        int scrollposition = 0;
        ArrayList<Contactmodel> alreadyGetedList = new ArrayList<>();

        int isAppuserSearch = 0;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            alreadyGetedList.addAll(memberlist);
            tempchatlist = new ArrayList<Contactmodel>();

        }

        @Override
        protected Void doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            isAppuserSearch = params[0];

            previousrowid = (String) loadmore_progress.getTag();

            if (previousrowid.equalsIgnoreCase("0")) {

                tempchatlist.addAll(GlobalData.dbHelper
                        .getContactForLoadMoreFromDBRegister(previousrowid));

            } else {
                tempchatlist.addAll(GlobalData.dbHelper
                        .getContactForLoadMoreFromDBRegister(previousrowid));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (tempchatlist.size() > 0) {
                scrollposition = tempchatlist.size();
                previousrowid = tempchatlist.get(tempchatlist.size() - 1)
                        .getRow_id();
                memberlist.addAll(memberlist.size(), tempchatlist);
                loadmore_progress.setTag(previousrowid);

            }
            memberAdapter.notifyDataSetChanged();

			/*
             * if(alreadyGetedList.size()>=15){
			 * loadmore_progress.setVisibility(View.VISIBLE); }else{
			 * loadmore_progress.setVisibility(View.GONE); }
			 */

			/*
			 * if (tempchatlist.size() == 0) { if(isAppuserSearch==0){
			 * loadmore_progress.setTag("0");
			 * loadmore_progress.setTag(R.id.loadmore_progress,false);//
			 * getNonAppUsersForLoadMore(2); }else{
			 * loadmore_progress.setVisibility(View.GONE);
			 * progress.setTag(R.id.progress,false); } } else {
			 * loadmore_progress.setTag(R.id.loadmore_progress,true);// }
			 */

            // IS_USER_TYPE=2;
            getNonAppUsersForLoadMore(2);

            // progress.setVisibility(View.GONE);

        }

    }

    class getLoadMoreNonAppContactAsyncTask extends AsyncTask<Void, Void, Void> {
        ArrayList<Contactmodel> tempchatlist;
        int scrollposition = 0;
        ArrayList<Contactmodel> alreadyGetedList = new ArrayList<Contactmodel>();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            alreadyGetedList.addAll(memberlist);
            tempchatlist = new ArrayList<Contactmodel>();

        }

        @Override
        protected Void doInBackground(Void... params) {

            previousrowid = (String) loadmore_progress.getTag();

            if (previousrowid.equalsIgnoreCase("0")) {

                // tempchatlist.addAll(GlobalData.dbHelper.getContactfromDBOnlySMS19RowBase(previousrowid));
                tempchatlist.addAll(GlobalData.dbHelper
                        .getContactfromDBOnlySMS19RowBase2(previousrowid));

            } else {
                // tempchatlist.addAll(GlobalData.dbHelper.getContactfromDBOnlySMS19RowBase(previousrowid));
                tempchatlist.addAll(GlobalData.dbHelper
                        .getContactfromDBOnlySMS19RowBase2(previousrowid));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                progress.setVisibility(View.GONE);
                if (tempchatlist.size() > 0) {
                    scrollposition = tempchatlist.size();
                    previousrowid = tempchatlist.get(tempchatlist.size() - 1)
                            .getRow_id();
                    memberlist.addAll(memberlist.size(), tempchatlist);
                    loadmore_progress.setTag(previousrowid);

                }

                memberAdapter.notifyDataSetChanged();

				/*
				 * if(alreadyGetedList.size()>=15){
				 * loadmore_progress.setVisibility(View.VISIBLE); }else{
				 * loadmore_progress.setVisibility(View.GONE); }
				 */

                if (tempchatlist.size() == 0) {
                    // loadmore_progress.setTag("0");

                    loadmore_progress.setTag(R.id.loadmore_progress, false);//
                    loadmore_progress.setVisibility(View.GONE);
                    progress.setTag(R.id.progress, false);
                } else {

                    loadmore_progress.setTag(R.id.loadmore_progress, false);//
                    loadmore_progress.setVisibility(View.VISIBLE);
                }

                progress.setTag(R.id.progress, false);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void Init(View view) {

        gcontact_lv = (ListView) view.findViewById(R.id.gcontact_lv);
        next_btn = (Button) view.findViewById(R.id.next_btn);

        mSelectAll = (CheckBox) v.findViewById(R.id.select_all);
        mDeSelectAll = (CheckBox) v.findViewById(R.id.deselect_all);
        mDeSelectAll.setVisibility(View.GONE);

        selected_count = (TextView) v.findViewById(R.id.selected_count_ingroup);


        next_btn.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
        next_btn.setTextColor(Color.parseColor(CustomStyle.TAB_FONT_COLOR));


        mSelectAll = (CheckBox) v.findViewById(R.id.select_all);
        mDeSelectAll = (CheckBox) v.findViewById(R.id.deselect_all);
        mDeSelectAll.setVisibility(View.GONE);
        selected_count = (TextView) v.findViewById(R.id.selected_count_ingroup);

        mSelectAll.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        mDeSelectAll.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        selected_count.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        setRobotoThinFont(selected_count,getActivity());
        setRobotoThinFontcheckbox(mSelectAll,getActivity());
        setRobotoThinFontcheckbox(mDeSelectAll,getActivity());
        setRobotoThinFontButton(next_btn,getActivity());


        next_btn.setOnClickListener(this);

        next_btn.setBackgroundColor(Color.GRAY);

        loadmore_progress = (LinearLayout) view
                .findViewById(R.id.loadmore_progress);
        loadmore_progress.setVisibility(View.GONE);
        loadmore_progress.setOnClickListener(this);
        progress = (ProgressBar) view.findViewById(R.id.progress);

        loadmore_progress.setTag("0");
        loadmore_progress.setTag(R.id.loadmore_progress, true);//
        progress.setTag(R.id.progress, true);

        mSelectAll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                CheckBox isChecked = (CheckBox) v;

                if (isChecked.isChecked()) {
                    // gcontact_lv.setSelection(memberlist.size());

                    selectedmemberlist = new ArrayList<Contactmodel>();
                    for (int i = 0; i < memberlist.size(); i++) {

                        memberlist.get(i).setIsselected(true);
                        selectedmemberlist.add(memberlist.get(i));
                    }

                    mDeSelectAll.setChecked(false);
                    selected_count.setText(selectedmemberlist.size() + "");
                } else {
                    // mSelectAll.setChecked(false);

                    for (int i = 0; i < memberlist.size(); i++) {

                        memberlist.get(i).setIsselected(false);
                    }

                    for (int i = 0; i < selectedmemberlist.size(); i++) {

                        selectedmemberlist.remove(i);

                    }
                    selectedmemberlist = new ArrayList<Contactmodel>();

                }
                memberAdapter.notifyDataSetChanged();

                if (selectedmemberlist.size() <= 1) {
                    next_btn.setBackgroundColor(Color.GRAY);
                } else {
                    next_btn.setBackgroundColor(Color.parseColor("#E46C22"));
                }
                selected_count.setText(selectedmemberlist.size() + "");

            }
        });

        mDeSelectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    // selectedmemberlist=new ArrayList<Contactmodel>();
                    for (int i = 0; i < memberlist.size(); i++) {

                        memberlist.get(i).setIsselected(false);
                    }
                    mSelectAll.setChecked(false);
                } else {
                    // mDeSelectAll.setChecked(false);
                }
                memberAdapter.notifyDataSetChanged();

            }
        });

        gcontact_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;

                if (memberlist != null) {
                    if (lastItem == memberlist.size()) {

                        // loadmore_progress.setVisibility(View.VISIBLE);

                        boolean isscroll = (Boolean) progress
                                .getTag(R.id.progress);
                        if (!isscroll) {
                            loadmore_progress.setVisibility(View.GONE);
                        } else {
                            // getLoadMoreParent(2);
                        }

                    } else {
                        loadmore_progress.setVisibility(View.GONE);
                    }
                }

				/*
				 * boolean isscroll=(Boolean)progress.getTag(R.id.progress);
				 * 
				 * 
				 * if(!isscroll){ loadmore_progress.setVisibility(View.GONE); }
				 */
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.next_btn:
                Addfragment();
                break;

            case R.id.cancel:
                if (isGroupCreateFromContacts) {

                } else {
                    homeActivity.backPress();
                }
                break;

            case R.id.loadmore_progress:
                // new getMoreContactItem().execute();
                // getLoadMoreParent(0);
                break;

            default:
                break;
        }

    }

    public void Addfragment() {

        //CONTACT_GROUP_PARTICIPANT_LIMIT

        Log.i("PageTYpe", "Page - " + GlobalData.PAGE_GROUP_TYPE);
        int participant_limit = (GlobalData.PAGE_GROUP_TYPE.equals("chats")) ? GlobalData.BRAODCAST_GROUP_PARTICIPANT_LIMIT : GlobalData.CONTACT_GROUP_PARTICIPANT_LIMIT;

        if (selectedmemberlist != null && selectedmemberlist.size() > 1) {
            if (participant_limit >= selectedmemberlist.size()) {
                CreateBroadCast2 broadcast_page2 = CreateBroadCast2.getInstance(param1);
                if (isGroupCreateFromContacts) {
//                    setIsGroupCreateFromContacts(false);
//                    isGroupCreateFromContactsLast = true;
                    ((AddBroadcastGroupActivity)getActivity()).collapseSearch();
                    broadcast_page2.setGroupName(groupName);
                    FragmentManager manager = getActivity()
                            .getSupportFragmentManager();
                     manager
                            .beginTransaction().add(android.R.id.content, broadcast_page2,
                            "CreateBroadCast2").addToBackStack(null).commit();

                } else {
                    homeActivity.callFragmentWithAddBack(broadcast_page2,
                            "CreateBroadCast2");
                }
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        "Selected users are "
                                + String.valueOf(selectedmemberlist.size()),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        "Group participant limit should be less then " + String.valueOf(participant_limit), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void filter(CharSequence sequence) {
        if (memberAdapter != null)
            memberAdapter.getFilter().filter(sequence);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if (loadMoreNonAppContactAsyncTask != null) {
            loadMoreNonAppContactAsyncTask.cancel(true);
            loadMoreNonAppContactAsyncTask = null;
        }

        if (appUsersLoadAsyncTask != null) {
            appUsersLoadAsyncTask.cancel(true);
            appUsersLoadAsyncTask = null;
        }

        CreateBroadCast.selectedmemberlist = null;
        if (!isGroupCreateFromContacts) {
            homeActivity.onBothTabPageControlIsGone();
            homeActivity.getActionbar_title().setText(mLastTitle);
            homeActivity.getLayoutTab_contact_chat()
                    .setVisibility(View.VISIBLE);

            homeActivity.getLayout_name_status().setVisibility(View.GONE);

            ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
            homeActivity.invalidateOptionMenuItem();
        }else
        {
            ((AddBroadcastGroupActivity)getActivity()).collapseSearch();
            getActivity().invalidateOptionsMenu();
        }
    }

    private class MamberAdapter extends BaseAdapter implements Filterable {
        public ArrayList<Contactmodel> tempmemberlist;

        private int selected = -1;

        public MamberAdapter() {
            // TODO Auto-generated constructor stub
            tempmemberlist = memberlist;
            selected = -1;
        }

        @Override
        public int getCount() {

            return memberlist.size();
        }

        @Override
        public Object getItem(int position) {

            return memberlist.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

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
                holder.onlineimage = (ImageView) convertView
                        .findViewById(R.id.onlineimage);
                // holder.userpicuppr = (ImageView) convertView
                // .findViewById(R.id.userpicuppr);
                holder.user_checkbox = (CheckBox) convertView
                        .findViewById(R.id.user_checkbox);

                convertView.setTag(holder);
            } else {
                holder = (Mamberholder) convertView.getTag();
            }

            holder.user_checkbox.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    CheckBox box = (CheckBox) v;
                    if (box.isChecked()) {

                        memberlist.get(position).setIsselected(true);
                        selectedmemberlist.add(memberlist.get(position));
                        next_btn.setBackgroundColor(Color.parseColor("#E46C22"));
                        if (memberlist.size() == selectedmemberlist.size()) {
                            mSelectAll.setChecked(true);
                        }

                    } else {
                        String id = memberlist.get(position).getRemote_jid();
                        for (int i = 0; i < selectedmemberlist.size(); i++) {

                            if (id.equals(selectedmemberlist.get(i)
                                    .getRemote_jid())) {
                                memberlist.get(position).setIsselected(false);
                                selectedmemberlist.remove(i);
                                break;
                            }
                        }
                        mSelectAll.setChecked(false);

                    }
                    if (selectedmemberlist.size() <= 1) {
                        next_btn.setBackgroundColor(Color.GRAY);
                    }
                    memberAdapter.notifyDataSetChanged();
                    selected_count.setText(selectedmemberlist.size() + "");

                }
            });

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
                pic = Utils.getResizedBitmap(pic, GlobalData.profilepicthmb,
                        GlobalData.profilepicthmb);
            } else {
                @SuppressWarnings("deprecation")
                Drawable drawable = getResources().getDrawable(
                        R.drawable.profileimg);
                pic = Utils.drawableToBitmap(drawable);
                pic = Utils.getResizedBitmap(pic, GlobalData.profilepicthmb,
                        GlobalData.profilepicthmb);
            }
            if (status.equals("1")) {
                // pic = Utils.getCircularBitmapWithBorder(pic, 5, Color.GREEN);

                holder.onlineimage.setImageDrawable(getActivity()
                        .getResources().getDrawable(R.drawable.online));
                holder.userpic.setImageBitmap(pic);

            } else {
                holder.onlineimage.setImageDrawable(getActivity()
                        .getResources().getDrawable(R.drawable.offline));
                holder.userpic.setImageBitmap(pic);

            }
            if (memberlist.get(position).getIsRegister() == 1) {
                holder.onlineimage.setVisibility(View.VISIBLE);
            } else {
                holder.onlineimage.setVisibility(View.GONE);
            }

            return convertView;

        }

        @Override
        public Filter getFilter() {

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

        public int getSelected() {
            return selected;
        }

        public void setSelected(int selected) {
            this.selected = selected;
        }

    }

    class Mamberholder {
        TextView usernametext;
        ImageView onlineimage;
        sms19.inapp.msg.CircularImageView userpic;
        // ImageView userpicuppr;
        CheckBox user_checkbox;
    }

    @SuppressWarnings("unused")
    public ArrayList<Contactmodel> sortByAZBoth(
            ArrayList<Contactmodel> arrayList1,
            ArrayList<Contactmodel> arrayList2) {

        ArrayList<Contactmodel> contactlistContactNew = new ArrayList<Contactmodel>();

        if (arrayList1.size() > 0) {
            Collections.sort(arrayList1,
                    new Contactmodel().new CustomComparatorSortByName());
        }
        if (arrayList2.size() > 0) {
            Collections.sort(arrayList2,
                    new Contactmodel().new CustomComparatorSortByName());
        }
        contactlistContactNew = arrayList1;

        // //if(contactlistContactNew.size()>0){
        if (arrayList1 != null) {
            if (arrayList1.size() > 0) {

                for (int i = 0; i < arrayList2.size(); i++) {
                    contactlistContactNew.add(arrayList2.get(i));
                }

            } else {
                contactlistContactNew = arrayList2;
            }
        } else {
            contactlistContactNew = arrayList2;
        }

		/*
		 * contactlist=new ArrayList<Contactmodel>();
		 * contactlist=contactlistContactNew;
		 * 
		 * 
		 * chatAdapter.setContactArrayList(contactlistContactNew);
		 * 
		 * chatAdapter.notifyDataSetChanged();
		 */

        return contactlistContactNew;

    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}

/*
* class BothTypeUserAsyncTask extends AsyncTask<Void, Void, Void> {
        ArrayList<Contactmodel> tempchatlist;
        int scrollposition = 0;
        ArrayList<Contactmodel> alreadyGetedList = new ArrayList<Contactmodel>();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            alreadyGetedList.addAll(memberlist);
            tempchatlist = new ArrayList<Contactmodel>();

        }

        @Override
        protected Void doInBackground(Void... params) {

            previousrowid = (String) loadmore_progress.getTag();

            memberlist.clear();

            memberlist = new ArrayList<Contactmodel>();
            ArrayList<Contactmodel> appUserList = (GlobalData.dbHelper
                    .getContactfromDBOnlyRegister());

            if (appUserList != null) {
                if (appUserList.size() > 0) {

                    Collections
                            .sort(appUserList,
                                    new Contactmodel().new CustomComparatorSortByName());
                    tempchatlist.addAll(appUserList);
                }
            }

			/*
             * ArrayList<Contactmodel>
			 * nonAppUserList=(GlobalData.dbHelper.getContactfromDBOnlySMS19RowBase2
			 * ("0"));
			 *
			 * if(nonAppUserList!=null){ if(nonAppUserList.size()>0){
			 * Collections.sort(nonAppUserList,new Contactmodel().new
			 * CustomComparatorSortByName());
			 * tempchatlist.addAll(nonAppUserList); } }

return null;
        }

@Override
protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        try {
        progress.setVisibility(View.GONE);
        progress.setTag(R.id.progress, false);
        if (tempchatlist.size() > 0) {
        scrollposition = tempchatlist.size();
        previousrowid = tempchatlist.get(tempchatlist.size() - 1)
        .getRow_id();
        memberlist.addAll(tempchatlist);
        loadmore_progress.setTag(previousrowid);

        }
        memberAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }

        }

        }

         public void getLoadMoreParent(int isusersearchtypre) { // 0 mean default
        // both user have
        // load more, 1 mean
        // only app user and
        // 2 mean nonapp
        // users
        boolean isAppuserLoadMore = true;
        previousrowid = (String) loadmore_progress.getTag();
        isAppuserLoadMore = (Boolean) loadmore_progress
                .getTag(R.id.loadmore_progress);

        if (isusersearchtypre != 2) {

            if (isAppuserLoadMore) {
                getAppUsersForLoadMore(isusersearchtypre);
            } else {
                getNonAppUsersForLoadMore(isusersearchtypre);
            }
        } else {
            getNonAppUsersForLoadMore(isusersearchtypre);
        }

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

            if (!lastrowid.equalsIgnoreCase(previousrowid)) {
                lastrowid = previousrowid;
                if (previousrowid.equalsIgnoreCase("0")) {
                    try {
                        if (sms19AppUsers.size() > 0) {
                            previousrowid = sms19AppUsers.get(
                                    sms19AppUsers.size() - 1).getRow_id();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    tempchatlist.addAll(GlobalData.dbHelper
                            .getContactfromDBOnlySMS19RowBase(previousrowid));

                } else {
                    tempchatlist.addAll(GlobalData.dbHelper
                            .getContactfromDBOnlySMS19RowBase(alreadyGetedList
                                    .get(alreadyGetedList.size() - 1)
                                    .getRow_id()));
                }

                if (tempchatlist.size() > 0) {
                    scrollposition = tempchatlist.size();

                    previousrowid = tempchatlist.get(tempchatlist.size() - 1)
                            .getRow_id();

                }

                alreadyGetedList.addAll(alreadyGetedList.size(), tempchatlist);

                memberlist.addAll(memberlist.size(), tempchatlist);
                // nonAppUser.addAll(nonAppUser.size(), tempchatlist);

            }
            // }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            if (tempchatlist.size() == 0) {

            } else {

                if (!alreadyGetedList.get(alreadyGetedList.size() - 1)
                        .getRow_id().equalsIgnoreCase(previousrowid)) {
                    memberAdapter.notifyDataSetChanged();
                } else {
                    loadmore_progress.setVisibility(View.GONE);
                }

            }

        }
    }
*/