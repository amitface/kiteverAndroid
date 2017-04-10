package sms19.inapp.msg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.Connection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.rest.Rest;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;

public class CreateBroadCast2 extends Fragment implements OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private View v;
    private ListView selectedcontact_lv;
    private EditText mBroadcastedt;

    private SelectdMamberAdapter selectedmemberAdapter;
    public static ArrayList<Contactmodel> selectedmemberlist;
    private ProgressDialog dialog;
    public SharedPreferences chatPrefs;
    public String mynumber = "";
    public String my_jid = "";
    public String groupname = "";
    public String groupdiscription = "";
    public String grouppicpath = "";
    public String groupid = "";
    byte[] byteArray = null;
    public String grouppicbase64string = "";
    private InAppMessageActivity homeActivity;
    private String mLastTitle = "";
    private Button mCreateBtn;
    private String broadcastid;
    private String broadcastlistname;
    private int LAST_HIDE_MENU = 0;
    private boolean isGroupCreateFromContacts = false;
    private String groupName;

    public static CreateBroadCast2 getInstance(String param1) {

        CreateBroadCast2 fragment = new CreateBroadCast2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String from = getArguments().getString(ARG_PARAM1);
            if(from.equalsIgnoreCase("contact"))
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
            homeActivity.collapseSearch();
            LAST_HIDE_MENU = ConstantFields.HIDE_MENU;
            ConstantFields.HIDE_MENU = 3;
            homeActivity.invalidateOptionMenuItem();
        } else
            getActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.broadcast_2, container, false);
        chatPrefs = getActivity().getSharedPreferences("chatPrefs",
                getActivity().MODE_PRIVATE);
        Bundle getData = getArguments();
        my_jid = chatPrefs.getString("user_jid", "");
        mynumber = chatPrefs.getString("userNumber", "");
        String currenttime = String.valueOf(System.currentTimeMillis());
        broadcastid = currenttime + "@Broadcast";
        Init();
        selectedmemberAdapter = new SelectdMamberAdapter(selectedmemberlist);
        selectedcontact_lv.setAdapter(selectedmemberAdapter);
        return v;
    }

    private void Init() {
        // TODO Auto-generated method stub
        selectedmemberlist = new ArrayList<Contactmodel>();
        ArrayList<Contactmodel> selectedmemberlist2 = CreateBroadCast.selectedmemberlist;

        if (selectedmemberlist2 != null) {

            for (int i = 0; i < selectedmemberlist2.size(); i++) {
                selectedmemberlist.add(selectedmemberlist2.get(i));
            }

        } else {

            HashMap<String, Contactmodel> hashMap = Utils
                    .getSelectedItem(getActivity());
            if (hashMap != null) {
                selectedmemberlist = new ArrayList<Contactmodel>();
                Iterator entries = hashMap.entrySet().iterator();
                int i = 0;
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();

                    Contactmodel model = (Contactmodel) entry.getValue();
                    selectedmemberlist.add(model);
                }
            }
        }

        mCreateBtn = (Button) v.findViewById(R.id.create_group_btn);

        mCreateBtn.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
        mCreateBtn.setTextColor(Color.parseColor(CustomStyle.TAB_FONT_COLOR));
        setRobotoThinFontButton(mCreateBtn, getActivity());


        selectedcontact_lv = (ListView) v.findViewById(R.id.listview);
        mBroadcastedt = (EditText) v.findViewById(R.id.broadcast_name);

        mBroadcastedt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        setRobotoThinFont(mBroadcastedt, getActivity());

        if (!GlobalData.PAGE_GROUP_TYPE.equals("chats") && groupName != null) {
            mBroadcastedt.setText(groupName);
        }
        /*
         * View viewFooter=getActivity().getLayoutInflater().inflate(R.layout.
		 * create_group_footer,null);
		 * mCreateBtn=(Button)viewFooter.findViewById(R.id.create_group_btn);
		 * selectedcontact_lv.addFooterView(viewFooter);
		 */

        mCreateBtn.setText("Create Broadcast");

        mCreateBtn.setOnClickListener(this);
        // if (!isGroupCreateFromContacts()) {
        if (!isGroupCreateFromContacts) {
            homeActivity.getCamera_btn().setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.agf_backbuttonlay:
                getActivity().onBackPressed();
                break;
            case R.id.agf_backbutton:
                getActivity().onBackPressed();
                break;

            case R.id.create_group_btn:
                // if (Utils.isDeviceOnline(getActivity())) {
                try {
                    broadcastlistname = mBroadcastedt.getText().toString();

                    boolean isGroup = GlobalData.dbHelper.isGroup(broadcastlistname
                            .trim());

                    if (!isGroup) {
                        Utils.hideKeyBoardMethod(getActivity(), mCreateBtn);
                        Log.i("PageTYpe", "Page - " + GlobalData.PAGE_GROUP_TYPE);
                        int participant_limit = (GlobalData.PAGE_GROUP_TYPE
                                .equals("chats")) ? GlobalData.BRAODCAST_GROUP_PARTICIPANT_LIMIT
                                : GlobalData.CONTACT_GROUP_PARTICIPANT_LIMIT;
                        Log.i("PageTYpe", "participant_limit - "
                                + participant_limit);
                        if (participant_limit >= selectedmemberlist.size()) {

                            if (broadcastlistname != null
                                    && broadcastlistname.trim().length() != 0) {
                                if (selectedmemberlist.size() > 1) {

                                    if (Utils.isDeviceOnline(getActivity())) {
                                        createBroadcastAT asyncTask = new createBroadcastAT(
                                                GlobalData.connection, broadcastid,
                                                broadcastlistname);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                            asyncTask
                                                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        } else {
                                            asyncTask.execute();
                                        }

                                    } else {
                                        Toast.makeText(getActivity(),
                                                "Check your network connection",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(
                                            getActivity().getApplicationContext(),
                                            "Select atleast two user in broadcast.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(
                                        getActivity().getApplicationContext(),
                                        "Enter broadcast list name.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Group participant limit should be less then "
                                            + String.valueOf(participant_limit),
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(),
                                "This broadcast name is already exist!",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed group creation!",
                            Toast.LENGTH_SHORT).show();
                }

			/*
             * }else { Toast.makeText(getActivity(),
			 * "Check your network connection..", Toast.LENGTH_SHORT); }
			 */

                break;

            default:
                break;
        }

    }

    public class createBroadcastAT extends AsyncTask<Void, Void, Boolean> {

        // private Connection conn;
        private String broadcastid;
        private String broadcastlistname;

        boolean success = false;
        String mag = "null";
        private JSONObject resObj;
        private String responseget = "";

        public createBroadcastAT(Connection conn, String broadcastid,
                                 String broadcastlistname) {

            // this.conn = conn;
            this.broadcastid = broadcastid;
            this.broadcastlistname = broadcastlistname;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Creating broadcast...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                // new InsertBroadcastGroupAssAynctask(homeActivity, chatPrefs,
                // userId, "",
                // broadcastlistname,selectedmemberlist,broadcastid).execute();
                // String postContactNo=broadcastlistname;

                try {
                    String userId = Utils.getUserId(getActivity());
                    String mobileNoStr = "";
                    String users_name = "";

                    String users_jid = "";
                    for (int i = 0; i < selectedmemberlist.size(); i++) {
                        if (i == 0) {
                            users_jid = selectedmemberlist.get(i)
                                    .getRemote_jid();
                            users_name = selectedmemberlist.get(i).getName();
                            // mobileNoStr=Utils.remove91(selectedmemberlist.get(i).getRemote_jid().split("@")[0]);
                            // mobileNoStr=Utils.removePlus(selectedmemberlist.get(i).getNumber());
                            mobileNoStr = selectedmemberlist.get(i).getNumber();
                        } else {
                            users_jid = users_jid + ","
                                    + selectedmemberlist.get(i).getRemote_jid();
                            users_name = users_name + ","
                                    + selectedmemberlist.get(i).getName();
                            // mobileNoStr=mobileNoStr +
                            // ","+Utils.remove91(selectedmemberlist.get(i).getRemote_jid().split("@")[0]);
                            // mobileNoStr=mobileNoStr +
                            // ","+Utils.removePlus(selectedmemberlist.get(i).getNumber());
                            mobileNoStr = mobileNoStr + ","
                                    + selectedmemberlist.get(i).getNumber();

                        }
                    }

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs
                            .add(new BasicNameValuePair("user_id", userId));
                    nameValuePairs.add(new BasicNameValuePair("group_title",
                            broadcastlistname));
                    nameValuePairs.add(new BasicNameValuePair("group_jid",
                            broadcastid));
                    nameValuePairs.add(new BasicNameValuePair("users_numbers",
                            mobileNoStr));
                    nameValuePairs.add(new BasicNameValuePair("users_name",
                            users_name));
                    nameValuePairs.add(new BasicNameValuePair("users_jid",
                            users_jid));
                    nameValuePairs.add(new BasicNameValuePair("Page",
                            "CreateBroadcastGroupAndContacts"));

                    Log.i("broadcast",
                            "nameValuePairs- " + nameValuePairs.toString());

                    Rest rest2 = Rest.getInstance();
                    responseget = rest2.post(Apiurls.getBasePostURL(),
                            nameValuePairs);

                    // Utils.printLog("InsertBroadcast URL:  " +
                    // Apiurls.KIT19_BASE_URL.replace("?Page=", ""));
                    // Utils.printLog("InsertBroadcast Response1112:  " +
                    // responseget);
                    // Utils.printLog("Get Broadcast:  " + responseget);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                try {
                    Log.e("brodcast", "responseget-- " + responseget);
                    if (responseget != null && responseget.trim().length() != 0) {
                        try {
                            resObj = new JSONObject(responseget);
                            if (resObj.has("InsertBroadcast")) {
                                JSONArray broadcastArray = resObj
                                        .getJSONArray("InsertBroadcast");
                                if (broadcastArray != null
                                        && broadcastArray.length() > 1) {

                                    /*if (GlobalData.BRAODCAST_GROUP_PARTICIPANT_LIMIT >= broadcastArray
                                            .length()) { }*/
                                    String msgtime = String.valueOf(System
                                            .currentTimeMillis());
                                    GlobalData.dbHelper
                                            .addnewBroadcastinDB(
                                                    broadcastid,
                                                    broadcastlistname,
                                                    selectedmemberlist,
                                                    msgtime);
                                    if (broadcastArray
                                            .length() < 250) {
                                        String sendDefaltmsg = broadcastlistname
                                                + "Created...";
                                        long rowid = GlobalData.dbHelper
                                                .addchatToMessagetable(
                                                        broadcastid,
                                                        sendDefaltmsg, my_jid,
                                                        msgtime, "", "", "0");// not

                                        // set
                                        // yet
                                        // msgPacket
                                        // id
                                        // for
                                        // group so blank put
                                        if (rowid != -1) {
                                            GlobalData.dbHelper
                                                    .addorupdateRecentTable(
                                                            broadcastid,
                                                            String.valueOf(rowid));
                                            GlobalData.dbHelper
                                                    .updateContactmsgData(
                                                            broadcastid,
                                                            sendDefaltmsg,
                                                            msgtime);
                                        }
                                        Utils.printLog("Broadcast completed successfully....");
                                    }

                                    success = true;
                                    mag = "Broadcast created successfully.";
                                    return success;
                                } else {
                                    mag = "Broadcast should have more than 1 contact";
                                    return true;
                                }

                            } else {
                                mag = "Broadcast not created successfully.";
                                return false;
                            }
                        } catch (Exception e) {
                            mag = "Broadcast not created successfully.";
                            String str = e.getMessage();
                            Log.e("brodcast", "excepttion - " + str);
                            e.printStackTrace();
                            return false;
                        }
                    }
                } catch (Exception e) {
                    mag = "Broadcast not created successfully.";
                    String str = e.getMessage();
                    Log.e("brodcast", "excepttion - " + str);
                    e.printStackTrace();
                    return false;
                }

            } catch (Exception e) {
                Utils.printLog("Create broadcast excption");
                mag = "Broadcast not created successfully.";
                e.printStackTrace();
                return false;
            }

            return false;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            try {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    Utils.hideKeyBoard(getActivity());
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

            if (result) {

                HashMap<String, Contactmodel> hashMap = Utils
                        .getSelectedItem(getActivity());
                if (CreateBroadCast.selectedmemberlist == null) {
                    if (isGroupCreateFromContacts) {
                        FragmentManager fm = getActivity()
                                .getSupportFragmentManager();
                        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                    } else {
                        InAppMessageActivity.Handler.sendEmptyMessage(5);
                    }
                } else {

                    if (isGroupCreateFromContacts) {
                        Toast.makeText(getActivity(), "Group has created.",
                                Toast.LENGTH_SHORT).show();

                        FragmentManager fm = getActivity()
                                .getSupportFragmentManager();
                        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        getActivity().setResult(getActivity().RESULT_OK);
                        getActivity().finish();
                    } else {
                        InAppMessageActivity.Handler.sendEmptyMessage(5);
                    }
                }

                // String userId=Utils.getUserId(getActivity());
                // new InsertBroadcastGroupAssAynctask(homeActivity, chatPrefs,
                // userId, "",
                // broadcastlistname,selectedmemberlist,broadcastid).execute();

            } else {
                try {
                    Toast.makeText(getActivity(), mag, Toast.LENGTH_SHORT)
                            .show();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }

    class SelectdMamberAdapter extends BaseAdapter {

        private ArrayList<Contactmodel> selectedmemberlist;

        public SelectdMamberAdapter(ArrayList<Contactmodel> selectedmemberlist) {
            this.selectedmemberlist = selectedmemberlist;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return selectedmemberlist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return selectedmemberlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            SelectedMamberholder holder = null;
            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.agf_adapterlay, parent,
                        false);
                holder = new SelectedMamberholder();

                holder.agf_usernametext = (TextView) convertView
                        .findViewById(R.id.agf_usernametext);
                holder.agf_userpic = (sms19.inapp.msg.CircularImageView) convertView
                        .findViewById(R.id.agf_userpic);
                // holder.agf_userpicuppr = (ImageView) convertView
                // .findViewById(R.id.agf_userpicuppr);
                holder.agf_user_delete = (ImageView) convertView
                        .findViewById(R.id.agf_user_delete);
                holder.agf_userlastseen = (TextView) convertView
                        .findViewById(R.id.agf_userlastseen);
                holder.agf_custom_status = (TextView) convertView
                        .findViewById(R.id.agf_custom_status);

                holder.onlineimage = (ImageView) convertView
                        .findViewById(R.id.onlineimage);

                setRobotoThinFont(holder.agf_usernametext, getActivity());
                setRobotoThinFont(holder.agf_userlastseen, getActivity());
                setRobotoThinFont(holder.agf_custom_status, getActivity());

                holder.agf_usernametext.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

                convertView.setTag(holder);
            } else {
                holder = (SelectedMamberholder) convertView.getTag();
            }
            holder.agf_custom_status.setText("");
            holder.agf_userlastseen.setVisibility(View.GONE);
            holder.agf_user_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String id = selectedmemberlist.get(position)
                            .getRemote_jid();

					/*
					 * for (int i = 0; i < CreateBroadCast.memberlist.size();
					 * i++) { if
					 * (id.equals(CreateBroadCast.memberlist.get(i).getRemote_jid
					 * ())) {
					 * CreateBroadCast.memberlist.get(i).setIsselected(false); }
					 * }
					 */
                    selectedmemberlist.remove(position);
                    selectedmemberAdapter.notifyDataSetChanged();

                }
            });
            holder.agf_usernametext.setText(selectedmemberlist.get(position)
                    .getName());
            String status = "";
            if (selectedmemberlist.get(position).getStatus() != null) {
                status = selectedmemberlist.get(position).getStatus().trim();
            }

            Bitmap pic = null;
            if (selectedmemberlist.get(position).getAvatar() != null) {
                pic = BitmapFactory.decodeByteArray(
                        selectedmemberlist.get(position).getAvatar(), 0,
                        selectedmemberlist.get(position).getAvatar().length);
                pic = Utils.getResizedBitmap(pic, GlobalData.profilepicthmb,
                        GlobalData.profilepicthmb);

            } else {
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
                holder.agf_userpic.setImageBitmap(pic);

            } else {
                holder.onlineimage.setImageDrawable(getActivity()
                        .getResources().getDrawable(R.drawable.offline));
                holder.agf_userpic.setImageBitmap(pic);

            }
            if (selectedmemberlist.get(position).getIsRegister() == 1) {
                holder.onlineimage.setVisibility(View.VISIBLE);
            } else {
                holder.onlineimage.setVisibility(View.GONE);
            }

            return convertView;

        }

    }

    class SelectedMamberholder {
        TextView agf_usernametext, agf_userlastseen, agf_custom_status;
        ImageView agf_user_delete, onlineimage;
        sms19.inapp.msg.CircularImageView agf_userpic;
        // ImageView agf_userpicuppr;

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        HashMap<String, Contactmodel> hashMap = Utils
                .getSelectedItem(getActivity());
        Utils.saveSelectedItem(getActivity(),
                new HashMap<String, Contactmodel>());
        if (!isGroupCreateFromContacts) {
            if (CreateBroadCast.selectedmemberlist == null) {
                if (!isGroupCreateFromContacts) {
                    homeActivity.getLayoutTab_contact_chat().setVisibility(
                            View.VISIBLE);
                }
            }
            if (!isGroupCreateFromContacts) {
                homeActivity.getActionbar_title().setText(mLastTitle);
                ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
                homeActivity.invalidateOptionMenuItem();
            }
        }
        if (dialog != null)
            dialog.dismiss();
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}