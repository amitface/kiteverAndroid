package sms19.inapp.msg;

import java.util.ArrayList;
import java.util.HashMap;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import sms19.inapp.msg.adapter.BroadcastProfileAdapter;
import sms19.inapp.msg.adapter.MediaPagerAdapter;
import sms19.inapp.msg.asynctask.BroadCastParticipantDeleteAsyncTask;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Chatmodel;
import sms19.inapp.msg.model.Contactmodel;
import sms19.inapp.msg.model.Groupmodel;
import sms19.inapp.msg.model.NewContactModelForFlag;
import sms19.inapp.msg.model.Recentmodel;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;
import com.kitever.android.R;

public class BroadCastGroupProfile extends Fragment implements OnClickListener {

	private static BroadCastGroupProfile broadCastGroupProfile;

	private ListView mListView;
	private InAppMessageActivity homeActivity = null;
	private ChatAndContactParentFragment andContactParentFragment = null;
	private ViewPager viewPager;
	private MediaPagerAdapter mediaPagerAdapter = null;

	private Button exitGroupBtn;
	private Button deleteGroupBtn;

	private String mLastTitle = "";

	private byte[] frndpic = null;
	private String frndname = "";
	private String remote_jid = "";
	private int isgroup = 0;
	private int LAST_HIDE_MENU = 0;
	public static ArrayList<Recentmodel> gmemberlist;
	public static HashMap<String, Recentmodel> gmemberhasmap;
	private BroadcastProfileAdapter adapter = null;
	private TextView participant_count;
	private TextView media_count;

	private ImageView imageView;
	public ArrayList<Chatmodel> chathistorylist = new ArrayList<Chatmodel>();
	private Groupmodel groupModel = new Groupmodel();
	private Recentmodel recentAdminModel = new Recentmodel();
	private Button mAddGroupBtn;
	private String groupName = "";
	private TextView mEdtBtn;

	private Handler handlerRefreshAdapter = null;

	public static BroadCastGroupProfile newInstance() {

		return broadCastGroupProfile;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homeActivity = (InAppMessageActivity) getActivity();
		andContactParentFragment = ChatAndContactParentFragment.newInstance();
		ConstantFlag.TAB_BACK_HANDLE_FRAGMENT = "2";

		LAST_HIDE_MENU = ConstantFields.HIDE_MENU;

		homeActivity.getLayout_name_status().setVisibility(View.GONE);
		homeActivity.getmActionBarImage().setVisibility(View.GONE);
		ConstantFields.HIDE_MENU = 3;
		homeActivity.invalidateOptionMenuItem();
		homeActivity.getActionbar_title().setVisibility(View.VISIBLE);
		mLastTitle = homeActivity.getActionbar_title().getText().toString();
		homeActivity.getActionbar_title().setText("Settings");
		// homeActivity.getmUserStatusTitle()

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		broadCastGroupProfile = this;

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.broadcast_groupprofile_new, container, false);

		gmemberlist = new ArrayList<Recentmodel>();
		Bundle getData = getArguments();
		frndname = getData.getString("remote_name", "");
		homeActivity.getActionbar_title().setText(frndname);
		groupName = getData.getString("remote_name", "");

		isgroup = getData.getInt("isgroup");

		remote_jid = getData.getString("remote_jid", "");
		frndpic = getData.getByteArray("remote_pic");

		frndpic = getData.getByteArray("remote_pic");

		chathistorylist.addAll(GlobalData.dbHelper.getChathistoryfromDBOfMedia(
				InAppMessageActivity.myModel.getRemote_jid(), remote_jid));

		groupModel = GlobalData.dbHelper.getSingleGroupFromDB(remote_jid);

		if (GlobalData.dbHelper.checkGroupiscreatedbyme(remote_jid)) {
			groupModel.setCreated_me("1");
		} else {
			groupModel.setCreated_me("1");

		}

		inItHandlerRefreshAdapter();
		initiateView(view);

		if (isgroup == 2) {
			imageView.setImageResource(R.drawable.broadcast_profil_new);
		}

		return view;
	}

	public void initiateView(View view) {

		mListView = (ListView) view.findViewById(R.id.group_list);
		imageView = (ImageView) view.findViewById(R.id.image);
		participant_count = (TextView) view
				.findViewById(R.id.participant_count);
		media_count = (TextView) view.findViewById(R.id.media_count);

		mAddGroupBtn = (Button) view.findViewById(R.id.add_group);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager2);
		mEdtBtn = (TextView) view.findViewById(R.id.mEdtBtn);

		viewPager.setPageMargin(-50);
		viewPager.setHorizontalFadingEdgeEnabled(true);
		viewPager.setFadingEdgeLength(30);

		View view2 = getActivity().getLayoutInflater().inflate(
				R.layout.group_bottom_view, null);
		exitGroupBtn = (Button) view2.findViewById(R.id.exit_group);
		deleteGroupBtn = (Button) view2.findViewById(R.id.delete_group);

		mAddGroupBtn.setOnClickListener(this);

		mAddGroupBtn.setVisibility(View.VISIBLE);
		exitGroupBtn.setVisibility(View.VISIBLE);
		mListView.addFooterView(view2);

	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		adapter = new BroadcastProfileAdapter(homeActivity,
				new ArrayList<Recentmodel>());
		adapter.setClickListener(this);
		mListView.setAdapter(adapter);

		exitGroupBtn.setOnClickListener(this);
		deleteGroupBtn.setOnClickListener(this);

		// setGroupmemberlist(false);
		callHandler(false);
		mEdtBtn.setVisibility(View.GONE);
		exitGroupBtn.setVisibility(View.GONE);

		if (chathistorylist != null) {
			media_count.setText(String.valueOf(chathistorylist.size()));
		} else {
			chathistorylist = new ArrayList<Chatmodel>();
			media_count.setText("0");
		}

		mediaPagerAdapter = new MediaPagerAdapter(getActivity(),
				chathistorylist);
		viewPager.setAdapter(mediaPagerAdapter);

		if (groupModel.getCreated_me().equalsIgnoreCase("0")) {
			deleteGroupBtn.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {

		if (mAddGroupBtn == v) {

			homeActivity.callFragmentWithAddBack(
					new BroadcastProfileUserSelection(),
					"BroadcastProfileUserSelection");

		}

		if (deleteGroupBtn == v) {

			String groupId = remote_jid;
			try {
				deleteGroupChat(GlobalData.connection, groupId);
			} catch (XMPPException e) {
				Utils.printLog("delete group failed");
				e.printStackTrace();
			}

		}

		if (R.id.delete_group1 == v.getId()) {
			int pos = (Integer) v.getTag();

			String remoteJidUser = gmemberlist.get(pos).getRemote_jid();
			try {
				// leaveChatRoom(remote_jid, groupId);
				if (gmemberlist.size() > 3) {
					leaveChatRoom(remote_jid, remoteJidUser);
				} else {
					Toast.makeText(
							getActivity(),
							"Atleast two users compulsory for this broadcast group!",
							Toast.LENGTH_SHORT).show();
				}
				// deleteGroupChat(GlobalData.connection, groupId);
			} catch (Exception e) {
				Utils.printLog("delete group failed");
				e.printStackTrace();
			}

		}

		if (v.getId() == R.id.mTopLayout) {
			int pos = (Integer) v.getTag();
			homeActivity.toastMsg(String.valueOf(pos));
			if (andContactParentFragment != null) {
				homeActivity.callFragmentWithAddBack(
						new SingleChatRoomFrgament(),
						ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);
				// andContactParentFragment.addChildFragmentBackStack(new
				// SingleChatRoomFrgament(),
				// ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);

			}

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		homeActivity.getmActionBarImage().setVisibility(View.VISIBLE);
		homeActivity.getLayout_name_status().setVisibility(View.VISIBLE);
		homeActivity.getmUserStatusTitle().setVisibility(View.VISIBLE);

		homeActivity.getActionbar_title().setVisibility(View.GONE);
		ConstantFlag.TAB_BACK_HANDLE_FRAGMENT = "1";
		ConstantFields.HIDE_MENU = LAST_HIDE_MENU;

		ChatFragment chatFragment = ChatFragment.newInstance("");
		if (chatFragment != null) {
			chatFragment.clickEnableDisable();
		}
		GroupListFrgament frgament = GroupListFrgament.newInstance("");
		if (frgament != null) {
			frgament.clickAbleTrue();
		}
		SingleChatRoomFrgament chatRoomFrgament = SingleChatRoomFrgament
				.newInstance("");
		if (chatRoomFrgament != null) {
			if (getActivity() != null) {
				chatRoomFrgament.updateBroadcastList();
				chatRoomFrgament.calljoinhaldler();
			}
		}
		homeActivity.invalidateOptionMenuItem();
		homeActivity.getActionbar_title().setText(mLastTitle);
		if (getActivity() != null) {
			NewContactModelForFlag contactmodel = Utils
					.getContactItem(getActivity());
			if (contactmodel != null) {
				if (!contactmodel.getFromPage().equalsIgnoreCase("")) {
					homeActivity.getActionbar_title().setVisibility(
							View.VISIBLE);
					homeActivity.getLayoutTab_contact_chat().setVisibility(
							View.VISIBLE);

					homeActivity.getmActionBarImage().setVisibility(View.GONE);
					homeActivity.getLayout_name_status().setVisibility(
							View.GONE);
					homeActivity.getmUserStatusTitle().setVisibility(View.GONE);

					Utils.saveContactItem(getActivity(),
							new NewContactModelForFlag());
				}
			}
		}

	}

	public void setGroupmemberlist(boolean loadmember) {
		try {
			if (gmemberlist.size() > 0) {
				gmemberlist.clear();
			}
			ArrayList<Recentmodel> temp = GlobalData.dbHelper
					.getGroupmemberListfromDB(remote_jid);

			if (temp != null && temp.size() > 0) {
				gmemberhasmap = new HashMap<String, Recentmodel>();
				gmemberlist.addAll(temp);

				Utils.printLog("gmemberlist size=" + gmemberlist.size());
				for (int i = 0; i < gmemberlist.size(); i++) {

					gmemberhasmap.put(gmemberlist.get(i).getRemote_jid(),
							gmemberlist.get(i));
				}

				recentAdminModel.setDisplayname("You(Admin)");
				recentAdminModel.setUserpic(InAppMessageActivity.myModel
						.getAvatar());

				if (getGroupModel().getCreated_me().equalsIgnoreCase("1")) {
					gmemberlist.add(recentAdminModel);
				}

				adapter.setRecentArrayList(gmemberlist);
				adapter.notifyDataSetChanged();
				participant_count.setText(String.valueOf(gmemberlist.size()));

			} else {

				recentAdminModel.setDisplayname("You(Admin)");
				recentAdminModel.setUserpic(InAppMessageActivity.myModel
						.getAvatar());

				if (getGroupModel().getCreated_me().equalsIgnoreCase("1")) {
					gmemberlist.add(recentAdminModel);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			setListViewHeightBasedOnItems(mListView);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void delete_user_chat_history(final String remotejid) {
		new AlertDialog.Builder(getActivity())
				.setTitle("Delete Chat")
				.setMessage("Are you sure you want to delete this user?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								GlobalData.dbHelper
										.deleteParticularUserHistory(remotejid);
								GlobalData.dbHelper
										.deleteGroupParticularrow(remotejid);

								// setGroupmemberlist(false);
								callHandler(false);
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

	public boolean setListViewHeightBasedOnItems(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter != null) {

			int numberOfItems = listAdapter.getCount();

			// Get total height of all items.
			int totalItemsHeight = 0;
			for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
				View item = listAdapter.getView(itemPos, null, listView);
				item.measure(0, 0);
				totalItemsHeight += item.getMeasuredHeight();
			}

			// Get total height of all item dividers.
			int totalDividersHeight = listView.getDividerHeight()
					* (numberOfItems - 1);

			// Set list height.
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalItemsHeight + totalDividersHeight;
			listView.setLayoutParams(params);
			listView.requestLayout();

			return true;

		} else {
			return false;
		}

	}

	public void deleteGroupChat(final Connection conn, final String groupId)
			throws XMPPException {

		new AlertDialog.Builder(getActivity())
				.setTitle("Delete Group")
				.setMessage("Confirmation of group delete?")
				.setNegativeButton(/* android.R.string.no */"No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						})
				.setPositiveButton(/* android.R.string.yes */"Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									if (Utils.isDeviceOnline(getActivity())) {

										GlobalData.dbHelper.deleteParticularUserHistory(groupId);
										GlobalData.dbHelper.deleteGroupParticularrow(groupId);
										GlobalData.dbHelper.deleteRecentParticularrow(groupId);
										homeActivity.broadCastDelete(groupId, 3);

										// setGroupmemberlist(false);
										callHandler(false);
										Utils.printLog("delete group success");

										ChatFragment chatFragment = ChatFragment
												.newInstance("");
										if (chatFragment != null) {
											chatFragment.refreshChatAdapter();
										}
										GroupListFrgament frgament = GroupListFrgament
												.newInstance("");
										if (frgament != null) {
											frgament.refreshChatAdapter();
										}

										homeActivity.backPress();
										homeActivity.backPress();
									} else {
										Toast.makeText(
												getActivity(),
												"Check your network connection",
												Toast.LENGTH_SHORT).show();
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									Utils.printLog("delete group failed");
									e.printStackTrace();
								}

							}
						}).show();

	}

	public void leaveChatRoom(final String groupJId, final String jid) {

		Log.i("XMPP Chat Client", "User left chat room ");

		new AlertDialog.Builder(getActivity())
				.setTitle("Delete Member")
				.setMessage("Confirmation of member delete?")
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									boolean isdeleted = true;
									if (isdeleted) {
										/*
										 * GlobalData.dbHelper.
										 * deleteGroupMemberFromDB
										 * (groupJId,jid); callHandler(false);
										 */
										if (Utils.isDeviceOnline(getActivity())) {
											String USER_ID = Utils
													.getUserId(getActivity());
											BroadCastParticipantDeleteAsyncTask asyncTask = new BroadCastParticipantDeleteAsyncTask(
													getActivity(),
													InAppMessageActivity.chatPrefs,
													USER_ID, jid, "", null,
													groupJId,
													broadCastGroupProfile);
											if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
												asyncTask
														.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
											} else {
												asyncTask.execute();
											}
										} else {
											Toast.makeText(
													getActivity(),
													"Check your network connection",
													Toast.LENGTH_SHORT).show();
										}

									}

								} catch (Exception e) {
									Toast.makeText(
											getActivity(),
											"Cannot delete group now",
											Toast.LENGTH_SHORT).show();
									Utils.printLog("Failed leave group..");
									e.printStackTrace();
								}

							}
						}).show();

	}

	public String getRemote_jid() {
		return remote_jid;
	}

	public Groupmodel getGroupModel() {
		return groupModel;
	}

	public boolean addGroupMemberAsync(String jid,
			ArrayList<Contactmodel> contactmodels) {

		boolean isCreated = false;
		try {

			String chatRoomJid = remote_jid;

			for (int i = 0; i < contactmodels.size(); i++) {

				if (!chatRoomJid.equalsIgnoreCase("")) {
					GlobalData.dbHelper.addGroupMemberFromDB(chatRoomJid,
							contactmodels.get(i).getRemote_jid());

				}
			}

			isCreated = true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isCreated = false;
		}
		return isCreated;

	}

	public static ArrayList<Recentmodel> getGmemberlist() {
		return gmemberlist;
	}

	private void inItHandlerRefreshAdapter() {
		// TODO Auto-generated method stub
		handlerRefreshAdapter = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				boolean message = msg.getData().getBoolean("message");
				setGroupmemberlist(message);

			}
		};
	}

	public void callHandler(boolean bFlag) {
		android.os.Message msg = new android.os.Message();// comment m
		Bundle b = new Bundle();
		b.putBoolean("message", bFlag);
		handlerRefreshAdapter.sendMessage(msg);
	}

}
