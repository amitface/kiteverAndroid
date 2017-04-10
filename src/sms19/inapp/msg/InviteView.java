package sms19.inapp.msg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sms19.inapp.msg.asynctask.InviteAsyncTask;
import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.Utils;
import sms19.inapp.msg.model.Contactmodel;
import com.kitever.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class InviteView extends Fragment implements OnClickListener {

	private static InviteView addName_1;
	private InAppMessageActivity homeActivity;
	private String mLastTitle = "";

	private TextView mGroupNameEdt;
	private Button mInviteBnt;
	private Button mCancel;

	public static byte[] frndpic = null;
	public static String frndname = "";
	public static String remote_jid = "", customStatus = "";
	private String total_count = "", number, postCountryCode = "";
	private TextView textinvitecount;

	private int LAST_HIDE_MENU = 0;
	private InviteAsyncTask asyncTask = null;
	private HashMap<String, Contactmodel> hashMap = new HashMap<String, Contactmodel>();

	public static InviteView getInstance() {

		return addName_1;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		homeActivity = (InAppMessageActivity) getActivity();

		homeActivity.groupActionBarControlIsVisual();
		homeActivity.getActionbar_title().setVisibility(View.VISIBLE);
		homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
		mLastTitle = homeActivity.getActionbar_title().getText().toString();

		homeActivity.getLayout_name_status().setVisibility(View.VISIBLE);
		homeActivity.getmActionBarImage().setVisibility(View.GONE);
		homeActivity.getActionbar_title().setVisibility(View.GONE);
		homeActivity.getmUserNameTitle().setText("Invite");
		homeActivity.getmUserStatusTitle().setText("52 contacts selected");

		LAST_HIDE_MENU = ConstantFields.HIDE_MENU;
		ConstantFields.HIDE_MENU = 3;
		homeActivity.invalidateOptionMenuItem();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		addName_1 = this;
		View v = inflater.inflate(R.layout.invite_view, container, false);

		Bundle getData = getArguments();

		Init(v);

		if (getData != null) {
			frndname = getData.getString("remote_name", "");
			remote_jid = getData.getString("remote_jid", "");
			frndname = getData.getString("remote_name", "");
			total_count = getData.getString("total_invite", "");
			number = getData.getString("mobile_no", "");

			sms19.inapp.msg.model.PhoneValidModel model2 = sms19.inapp.msg.constant.ContactUtil
					.validNumberForInvite(Utils.removeCountryCode(number,getActivity()));

			if (model2 != null) {
				number = model2.getPhone_number();
				postCountryCode = model2.getCountry_code();
			} else {
				postCountryCode = Utils.getCountryCode(getActivity());
			}

			homeActivity.getmUserStatusTitle().setText(
					total_count + " contacts selected");
			textinvitecount.setText("You are about to send and invite to "
					+ total_count + " contacts");
		} else {
			hashMap = Utils.getSelectedItem(getActivity());
			if (hashMap != null) {
				homeActivity.getmUserStatusTitle().setText(
						String.valueOf(hashMap.size()) + " contacts selected");
				textinvitecount.setText("You are about to send and invite to "
						+ String.valueOf(hashMap.size()) + " contacts");
				Iterator entries = hashMap.entrySet().iterator();
				int i = 0;
				while (entries.hasNext()) {
					Map.Entry entry = (Map.Entry) entries.next();

					Contactmodel model = (Contactmodel) entry.getValue();
					sms19.inapp.msg.model.PhoneValidModel model2 = sms19.inapp.msg.constant.ContactUtil
							.validNumberForInvite(Utils.removeCountryCode(model
									.getNumber(),getActivity()));
					if (i == 0) {
						number = model2.getPhone_number();
						postCountryCode = model2.getCountry_code();
					} else {
						number = number + "," + model2.getPhone_number();
						postCountryCode = postCountryCode + ","
								+ model2.getCountry_code();
					}

					i++;

				}

			}
		}

		return v;
	}

	private void Init(View view) {

		mInviteBnt = (Button) view.findViewById(R.id.invite);
		mCancel = (Button) view.findViewById(R.id.cancel);

		mGroupNameEdt = (TextView) view.findViewById(R.id.edt_groupname);
		textinvitecount = (TextView) view.findViewById(R.id.textinvitecount);

		mInviteBnt.setOnClickListener(this);

		mCancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.invite:
			String userId = Utils.getUserId(getActivity());

			asyncTask = new InviteAsyncTask(homeActivity,
					InAppMessageActivity.chatPrefs, userId, number,
					postCountryCode);
			asyncTask.execute();
			break;

		case R.id.cancel:
			homeActivity.backPress();
			break;

		default:
			break;
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Utils.saveSelectedItem(getActivity(),
				new HashMap<String, Contactmodel>()); // selected array save
														// null

		homeActivity.onBothTabPageControlIsGone();
		homeActivity.getActionbar_title().setText(mLastTitle);
		homeActivity.getLayoutTab_contact_chat().setVisibility(View.VISIBLE);

		homeActivity.getLayout_name_status().setVisibility(View.GONE);
		ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
		homeActivity.invalidateOptionMenuItem();

		if (asyncTask != null) {
			asyncTask.cancel(true);
			asyncTask = null;
		}

	}

}
