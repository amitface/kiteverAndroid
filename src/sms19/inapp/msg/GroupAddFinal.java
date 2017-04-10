package sms19.inapp.msg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Slacktags;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;

public class GroupAddFinal extends Fragment implements OnClickListener {
    View v;
    ListView selectedcontact_lv;
    RelativeLayout agf_backbuttonlay, agf_donebuttonlay;
    TextView agf_backbutton, agf_donebutton;
    SelectdMamberAdapter selectedmemberAdapter;
    public static ArrayList<Contactmodel> selectedmemberlist;

    public static SharedPreferences chatPrefs;
    public static String mynumber = "";
    public static String my_jid = "";
    public static String groupname = "";
    public static String groupdiscription = "";
    public static String grouppicpath = "";
    public static String groupid = "";
    byte[] byteArray = null;
    public String grouppicbase64string = "";
    private InAppMessageActivity homeActivity;
    private String mLastTitle = "";
    private Button mCreateBtn;
    private int LAST_HIDE_MENU = 83;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        homeActivity = (InAppMessageActivity) getActivity();

        mLastTitle = homeActivity.getActionbar_title().getText().toString();

        homeActivity.getActionbar_title().setText("Create new group");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.fragment_addgroupfinal, container, false);
        chatPrefs = getActivity().getSharedPreferences("chatPrefs",
                getActivity().MODE_PRIVATE);

        Bundle getData = getArguments();
        groupname = getData.getString("groupname");
        grouppicpath = getData.getString("grouppicpath");
        my_jid = chatPrefs.getString("user_jid", "");
        mynumber = chatPrefs.getString("userNumber", "");
        groupdiscription = StringUtils.escapeNode(groupname);
        ConstantFields.HIDE_MENU = 3;
        homeActivity.invalidateOptionMenuItem();
		/*groupid = groupdiscription + mynumber + "@conference."
                + GlobalData.HOST;*/
        groupid = (groupdiscription + mynumber + "@conference." + GlobalData.HOST).toLowerCase();

        Init();

        selectedmemberAdapter = new SelectdMamberAdapter();
        selectedcontact_lv.setAdapter(selectedmemberAdapter);

        return v;
    }

    private void Init() {
        // TODO Auto-generated method stub

        ArrayList<Contactmodel> arrayList = new ArrayList<Contactmodel>();
        selectedmemberlist = new ArrayList<Contactmodel>();
        arrayList = (GroupAddUserSelection_2.selectedmemberlist);

        for (int i = 0; i < arrayList.size(); i++) {
            selectedmemberlist.add(arrayList.get(i));
        }


        agf_backbuttonlay = (RelativeLayout) v
                .findViewById(R.id.agf_backbuttonlay);
        agf_donebuttonlay = (RelativeLayout) v
                .findViewById(R.id.agf_donebuttonlay);
        agf_backbutton = (TextView) v.findViewById(R.id.agf_backbutton);
        agf_donebutton = (TextView) v.findViewById(R.id.agf_donebutton);
        selectedcontact_lv = (ListView) v.findViewById(R.id.selectedcontact_lv);
        mCreateBtn = (Button) v.findViewById(R.id.create_group_btn);
/*
        View viewFooter=getActivity().getLayoutInflater().inflate(R.layout.create_group_footer,null);
		mCreateBtn=(Button)viewFooter.findViewById(R.id.create_group_btn);*/
        //selectedcontact_lv.addFooterView(viewFooter);

        agf_backbuttonlay.setOnClickListener(this);
        agf_donebuttonlay.setOnClickListener(this);
        agf_backbutton.setOnClickListener(this);
        mCreateBtn.setOnClickListener(this);
        agf_donebutton.setOnClickListener(this);
        homeActivity.getCamera_btn().setOnClickListener(this);
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
            case R.id.chat_addfilebuttonlay:
                if (Utils.isDeviceOnline(getActivity())) {

                    if (GlobalData.connection != null
                            && GlobalData.connection.isConnected()) {
                        if (selectedmemberlist.size() > 0) {
                            createMucAT asyncTask = new createMucAT(GlobalData.connection, groupid,
                                    groupname);


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                asyncTask.execute();
                            }

						/*if(grouppicpath!=null&&grouppicpath.length()>0){// upload image path
                            try {
								clientConnection(grouppicpath.trim());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    			}*/


                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Select atleast one user in group.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "not connected to server..",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Check your network connection..", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.create_group_btn:
                if (Utils.isDeviceOnline(getActivity())) {

                    if (GlobalData.connection != null
                            && GlobalData.connection.isConnected()) {
                        if (selectedmemberlist.size() > 0) {
                            createMucAT asyncTask = new createMucAT(GlobalData.connection, groupid,
                                    groupname);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                asyncTask.execute();
                            }


                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Select atleast one user in group.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "not connected to server..",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Check your network connection..", Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }

    }

    public class createMucAT extends AsyncTask<Void, Void, MultiUserChat> {

        private Connection conn;
        private String groupId;
        private String groupName;
        private MultiUserChat mucChat;
        ProgressDialog dialog;

        public createMucAT(Connection conn, String groupId, String groupName) {

            this.conn = conn;
            this.groupId = groupId;
            this.groupName = groupName;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Creating group...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected MultiUserChat doInBackground(Void... params) {

            mucChat = null;
            try {
                if (grouppicpath.trim().length() != 0) {
					/*Bitmap grpp = Utils.decodeFile(grouppicpath,
							GlobalData.profilepicthmb,
							GlobalData.profilepicthmb);*/

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(grouppicpath, options);


                    grouppicbase64string = Utils.convertTobase64string(bitmap);


                    byteArray = Utils.decodeToImage(grouppicbase64string);


                }
                mucChat = createGroupChat(conn, groupId, groupName, my_jid);
            } catch (XMPPException e) {
                Utils.printLog("Create group excption");
                //Toast.makeText(getActivity(), "Group creation failed!",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return mucChat;


        }


        public byte[] getByte(String filepath) {


            File imagefile = new File(filepath);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imagefile);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Bitmap bm = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            return b;

        }

        @Override
        protected void onPostExecute(final MultiUserChat result) {
            super.onPostExecute(result);
            if (result != null) {
                GlobalData.Newgroup_seninvit = false;
                GlobalData.Newgroup_dbinsert = false;
                GlobalData.Newgroup_joinandssendmsg = false;
				
	/*			final String roomId=result.getRoom();
				
				try {
					RoomInfo info = MultiUserChat.getRoomInfo(GlobalData.connection,roomId);
					
					
					Utils.printLog2("roomId= "+ roomId);
					
					Utils.printLog2("roomId= "+info.getRoom());
					
					Utils.printLog2("ToString here\n"+info.toString());
					
					Utils.printLog2("description= "+info.getDescription());
					
				} catch (XMPPException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Toast.makeText(getActivity(), "roomId=  " +roomId, Toast.LENGTH_LONG).show();*/

                if (grouppicpath != null && grouppicpath.length() > 0) {// upload image path
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(grouppicpath, options);
                    saveFileInProfileFolderWithGroupName(bitmap, homeActivity, groupname);
                }


                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Utils.printLog("new group invitation start...");
                            for (int i = 0; i < selectedmemberlist.size(); i++) {
                                String remoteid = selectedmemberlist.get(i)
                                        .getRemote_jid();

                                Message msg = new Message();
                                msg.setBody(grouppicbase64string);

                                result.invite(msg, remoteid, groupname);
                                //mucChat.grantModerator(selectedmemberlist.get(i).getRemote_jid());
                                //mucChat.grantMembership(selectedmemberlist.get(i).getRemote_jid());
                                //mucChat.grantOwnership(contactmodels.get(i).getRemote_jid());
                                //	mucChat.grantAdmin(selectedmemberlist.get(i).getRemote_jid());

                            }
                            GlobalData.Newgroup_seninvit = true;
                            Utils.printLog("new group invitation success...");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            GlobalData.Newgroup_seninvit = true;
                            Utils.printLog("new group invitation Exception...");
                            e.printStackTrace();
                        }
                    }
                }).start();
                // new Thread(new Runnable() {
                // public void run() {
                //
                // }
                // }).start();
                new Thread(new Runnable() {
                    public void run() {
                        try {
							
							/*String msgtime1 = String.valueOf(System
									.currentTimeMillis());*/

                            String msgtime1 = Utils.currentTimeStap();
                            GlobalData.dbHelper.addnewGroupinDB(groupid,
                                    groupname, byteArray, selectedmemberlist,
                                    true, msgtime1);
                            GlobalData.Newgroup_dbinsert = true;
                            Utils.printLog("join by me start");
                            result.join(my_jid);
                            Utils.printLog("join by me success");
                            Utils.printLog("new group auto message send");
                            String sendDefaltmsg = "hello...";

                            //String timeStap = Utils.currentTimeStap();
                            String userId = InAppMessageActivity.USER_ID;
                            Slacktags slagTag = new Slacktags(InAppMessageActivity.myModel.getNumber(), userId, GlobalData.MESSAGE_TYPE_2, msgtime1, userId + "_" + msgtime1);

                            org.jivesoftware.smack.packet.Message msg = mucChat.createMessage();
                            msg.setBody(sendDefaltmsg);
                            msg.addExtension(slagTag);

                            //	result.sendMessage(msg);
                            Utils.printLog("new group auto message send success");
                            Utils.printLog("new group auto message added in db start");
							
							/*String msgtime = String.valueOf(System
									.currentTimeMillis()) Utils.currentTimeStap();;*/
                            long rowid = GlobalData.dbHelper
                                    .addchatToMessagetable(groupid,
                                            sendDefaltmsg, my_jid, msgtime1, "", "", "0");// not set yet .sgPacket id for
                            // group so blank put
                            if (rowid != -1) {
                                GlobalData.dbHelper.addorupdateRecentTable(
                                        groupid, String.valueOf(rowid));
                                GlobalData.dbHelper.updateContactmsgData(groupid, sendDefaltmsg, msgtime1);
                                Utils.printLog("new group auto message added in db success");

                            }
                            GlobalData.Newgroup_joinandssendmsg = true;

                        } catch (XMPPException e) {
                            // TODO Auto-generated catch block
                            GlobalData.Newgroup_joinandssendmsg = true;
                            Utils.printLog("join by me excption");
                            e.printStackTrace();
                        }
                    }
                }).start();
                new Thread(new Runnable() {
                    public void run() {
                        while (!GlobalData.Newgroup_joinandssendmsg
                                || !GlobalData.Newgroup_dbinsert
                                || !GlobalData.Newgroup_seninvit) {
                            Utils.printLog("new group creation is in working...");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                                // if (getActivity().getSupportFragmentManager()
                                // .getBackStackEntryCount() > 0) {
                                // getActivity().getSupportFragmentManager()
                                // .popBackStack();
                                // }
                                //
                                // FragmentTransaction home_ft = getActivity()
                                // .getSupportFragmentManager()
                                // .beginTransaction();
                                // home_ft.replace(R.id.frag_containor,
                                // new Home_frag(), "home_frag");
                                //
                                // home_ft.commit();
                            }
                        });

                        //InAppMessageActivity.Handler.sendEmptyMessage(1);
                        InAppMessageActivity.Handler.sendEmptyMessage(5);

                    }
                }).start();
            } else {
                dialog.dismiss();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Group not created try again...", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        private MultiUserChat createGroupChat(Connection conn, String groupId,
                                              String groupName, String nickname) throws XMPPException {
            Utils.printLog("Create group");
            MultiUserChat muc = new MultiUserChat(conn, groupId);
            muc.create(nickname);
            Form form = muc.getConfigurationForm();
            if (form != null) {
                Form submitForm = form.createAnswerForm();
                for (Iterator<FormField> fields = form.getFields(); fields
                        .hasNext(); ) {
                    FormField field = fields.next();
                    if (!FormField.TYPE_HIDDEN.equals(field.getType())
                            && field.getVariable() != null) {
                        submitForm.setDefaultAnswer(field.getVariable());
                    }
                }
                List<String> owners = new ArrayList<String>();
                List<String> owners1 = new ArrayList<String>();
                //owners.add(conn.getUser().toString());
                owners1.add(conn.getUser().toString());
                for (int i = 0; i < selectedmemberlist.size(); i++) {
                    owners.add(selectedmemberlist.get(i).getRemote_jid());
                }
                submitForm.setAnswer("muc#roomconfig_roomowners", owners1);
                submitForm.setAnswer("muc#roomconfig_roomadmins", owners);
                submitForm.setAnswer("muc#roomconfig_changesubject",true);

                submitForm.setAnswer("muc#roomconfig_persistentroom", true);
                submitForm.setAnswer("muc#roomconfig_publicroom", true);
				/*submitForm.setAnswer("muc#roomconfig_moderatedroom", true);*/

                submitForm.setAnswer("muc#roomconfig_roomdesc", groupdiscription);

//                MultiUserChat.addInvitationListener(conn,
//                        new InvitationListener() {
//
//                            @Override
//                            public void invitationReceived(Connection connection,
//                                                           String room, String inviter, String reason,
//                                                           String unKnown, Message message) {
//
//                                //MultiUserChat.decline(mXmppConnection, room, inviter,
//                                //  "Don't bother me right now");
//                                // MultiUserChat.decline(mXmppConnection, room, inviter,
//                                // "Don't bother me right now");
//                                try {
//                                    muc.join("test-nick-name");
//                                    Log.e("abc","join room successfully");
//                                    muc.sendMessage("I joined this room!! Bravo!!");
//                                } catch (XMPPException e) {
//                                    e.printStackTrace();
//                                    Log.e("abc","join room failed!");
//                                }
//                            }
//                        });
                try {
                    muc.sendConfigurationForm(submitForm);


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    muc.join("New group created");
//                    muc.sendMessage("New group created");
                } catch (XMPPException e1) {
                    e1.printStackTrace();
                }
            }

            Utils.printLog("Create group success");
            return muc;
        }
    }

    class SelectdMamberAdapter extends BaseAdapter {

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
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            SelectedMamberholder holder = null;
            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.agf_adapterlay, parent, false);
                holder = new SelectedMamberholder();

                holder.agf_usernametext = (TextView) convertView
                        .findViewById(R.id.agf_usernametext);
                holder.agf_userpic = (ImageView) convertView
                        .findViewById(R.id.agf_userpic);
                holder.onlineimage = (ImageView) convertView
                        .findViewById(R.id.onlineimage);

                // holder.agf_userpicuppr = (ImageView) convertView
                // .findViewById(R.id.agf_userpicuppr);
                holder.agf_user_delete = (ImageView) convertView
                        .findViewById(R.id.agf_user_delete);
                holder.agf_userlastseen = (TextView) convertView
                        .findViewById(R.id.agf_userlastseen);
                holder.agf_custom_status = (TextView) convertView
                        .findViewById(R.id.agf_custom_status);

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
					/*String id = selectedmemberlist.get(position)
							.getRemote_jid();
					for (int i = 0; i < GroupAddUserSelection_2.memberlist.size(); i++) {
						if (id.equals(GroupAddUserSelection_2.memberlist.get(i)
								.getRemote_jid())) {
							GroupAddUserSelection_2.memberlist.get(i)
							.setIsselected(false);
						}
					}*/
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


//commented on 10/10/2016---exception--getAvatar() -gives wrong value
            Bitmap pic = null;
//			if (selectedmemberlist.get(position).getAvatar() != null) {
//				pic = BitmapFactory.decodeByteArray(
//						selectedmemberlist.get(position).getAvatar(), 0,
//						selectedmemberlist.get(position).getAvatar().length);
//				pic = Utils.getResizedBitmap(pic, GlobalData.profilepicthmb,
//						GlobalData.profilepicthmb);
//
//			} else 
            {
                Drawable drawable = getResources().getDrawable(
                        R.drawable.profileimg);
                pic = Utils.drawableToBitmap(drawable);
                pic = Utils.getResizedBitmap(pic, GlobalData.profilepicthmb,
                        GlobalData.profilepicthmb);
            }


            if (status.equals("1")) {
                holder.agf_userpic.setImageBitmap(pic);
                holder.onlineimage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.online));


            } else {
                holder.agf_userpic.setImageBitmap(pic);
                holder.onlineimage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.offline));

            }


            return convertView;

        }

    }

    class SelectedMamberholder {
        TextView agf_usernametext, agf_userlastseen, agf_custom_status;
        ImageView agf_userpic, agf_user_delete, onlineimage;
        // ImageView agf_userpicuppr;

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Utils.hideKeyBoard(getActivity());
        homeActivity.getActionbar_title().setText(mLastTitle);
        ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
        homeActivity.invalidateOptionMenuItem();    }


    public void clientConnection(String path) {


        if (!(GlobalData.FTP_URL.equalsIgnoreCase("")
                || GlobalData.FTP_USER.equalsIgnoreCase("")
                || GlobalData.FTP_PASS.equalsIgnoreCase("") || GlobalData.FTP_HOST
                .equalsIgnoreCase(""))) {

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                if (GlobalData.clientFtp != null) {
                    if (GlobalData.clientFtp.isConnected()
                            && GlobalData.clientFtp.isAuthenticated()) {

                    } else {
                        GlobalData.clientFtp = null;
                        GlobalData.clientFtp = new FTPClient();
                        GlobalData.clientFtp
                                .connect(GlobalData.FTP_HOST.trim(), 21);
                        GlobalData.clientFtp.login(GlobalData.FTP_USER.trim(),
                                GlobalData.FTP_PASS);
                        //GlobalData.clientFtp.setType(FTPClient.TYPE_BINARY);
                    }
                } else {
                    GlobalData.clientFtp = null;
                    GlobalData.clientFtp = new FTPClient();
                    GlobalData.clientFtp.connect(GlobalData.FTP_HOST.trim(), 21);
                    GlobalData.clientFtp.login(GlobalData.FTP_USER.trim(), GlobalData.FTP_PASS);
                    //GlobalData.clientFtp.setType(FTPClient.TYPE_BINARY);


                }


                try {
                    ///String		userId = Utils.getUserId(getActivity());
                    //String test=GlobalData.clientFtp.currentDirectory();
                    //	GlobalData.clientFtp.changeDirectory("/"+userId);


                    ExecutorService executor = Executors.newFixedThreadPool(6);
                    Object[] iObj = new Object[7];
                    final File f = new File(path);


                    iObj[0] = f;


                    Runnable worker = new sms19.inapp.msg.asynctask.UploadThread(iObj);
                    executor.execute(worker);

                } catch (IllegalStateException e3) {
                    // TODO Auto-generated catch block
                    e3.printStackTrace();
                }

            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Ftp server error",
                            Toast.LENGTH_LONG).show();
                }

            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Ftp server error",
                            Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Ftp server error",
                            Toast.LENGTH_LONG).show();
                }
            } catch (FTPIllegalReplyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Ftp server error",
                            Toast.LENGTH_LONG).show();
                }
            } catch (FTPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Ftp server error",
                            Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    public String saveFileInProfileFolderWithGroupName(Bitmap bitmap, Activity homeActivity, String filename) {
        OutputStream output;

        String path = Utils.getProfilePathForGroupUpload(filename);
        File file = new File(path);

        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();

            Utils.saveImageInGallery(homeActivity, path);

            clientConnection(path);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return path;
    }


}