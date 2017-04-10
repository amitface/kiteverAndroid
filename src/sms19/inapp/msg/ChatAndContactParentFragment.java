package sms19.inapp.msg;


import sms19.inapp.msg.constant.GlobalData;
import com.kitever.android.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatAndContactParentFragment extends Fragment implements OnClickListener{

	private FrameLayout mChatContainer;
	private FrameLayout mContactContainer;
	private TextView mChatTabBtn;
	private TextView mContactTabBtn;
	private TextView mChatTabBtn1;
	private TextView mContactTabBtn1;
	private InAppMessageActivity homeActivity=null;
	private static ChatAndContactParentFragment andContactParentFragment=null;
	private LinearLayout mChatContactHeader;
	
	/*TextView fc_addpreviousmsg;
	ProgressBar fc_loadmsgprogress;
	RelativeLayout fc_loadmsglay;
	*/
	private LinearLayout chat_addfilebuttonlay;
	
	
	
	public static ChatAndContactParentFragment newInstance(){
		
		return andContactParentFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		homeActivity=(InAppMessageActivity)getActivity();
		
		ConstantFlag.FLAG_CHAT_CONTACT=true;
		//ConstantFields.HIDE_MENU=true;
		homeActivity.hideMenuMethod();
		
		//homeActivity.actionBarSetting();
		chat_addfilebuttonlay=homeActivity.getCamera_btn();
		homeActivity.onBothTabPageControlIsGone();
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		andContactParentFragment=this;
		
		 View view = getActivity().getLayoutInflater().inflate(R.layout.chat_contact_parent_view, container, false);
		 
		 mChatContainer=(FrameLayout)view.findViewById(R.id.chat_container);
		 mContactContainer=(FrameLayout)view.findViewById(R.id.contact_container);
		 
		 mChatTabBtn=(TextView)view.findViewById(R.id.tab_chat);
		 mContactTabBtn=(TextView)view.findViewById(R.id.tab_contact);
		 
		 mChatTabBtn1=(TextView)view.findViewById(R.id.tab_chat_1);
		 mContactTabBtn1=(TextView)view.findViewById(R.id.tab_contact_1);
		 
		 mChatContactHeader=(LinearLayout)view.findViewById(R.id.chat_andcontact_header);
		 
		 mChatContactHeader.setVisibility(View.GONE);
		 
			/*fc_addpreviousmsg = (TextView) view.findViewById(R.id.fc_addpreviousmsg);
			fc_loadmsgprogress = (ProgressBar) view.findViewById(R.id.fc_loadmsgprogress);
			fc_loadmsglay = (RelativeLayout) view.findViewById(R.id.fc_loadmsglay);*/
		 
		 chat_addfilebuttonlay.setOnClickListener(this);
		 mChatTabBtn.setOnClickListener(this);
		 mContactTabBtn.setOnClickListener(this);
		 
		 
		 homeActivity.callFragmentWithOutAddBack(new ChatFragment(),ConstantFlag.TAB_CHAT_FRAGMENT);
		
		return view;
	}
	
	
	public void onClick(View v) {
		
		if(mChatTabBtn==v){
					
			mChatTabBtn1.setBackgroundColor(getResources().getColor(R.color.new_actionbar_background_strip));
			mContactTabBtn1.setBackgroundColor(getResources().getColor(R.color.new_actionbar_background));
			//addChildFragment(new ChatFragment(),ConstantFlag.TAB_CHAT_FRAGMENT);
			
		}if(mContactTabBtn==v){
			
			mChatTabBtn1.setBackgroundColor(getResources().getColor(R.color.new_actionbar_background));
			mContactTabBtn1.setBackgroundColor(getResources().getColor(R.color.new_actionbar_background_strip));
			//addChildFragment(new ContactFragment(),ConstantFlag.TAB_CONTACHT_FRAGMENT);
		}
		
		if(v==chat_addfilebuttonlay){
			
			GlobalData.Newgroup = true;
			homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
			homeActivity.callFragmentWithAddBack(new GroupAddName_1(), "groupchat_frag");
			
		}
		
		
	}
	
	
	/* public void addChildFragment(Fragment fragment,String tag) {
	        FragmentManager childFragMan = getChildFragmentManager();
	        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
	        childFragTrans.replace(R.id.chat_container, fragment,tag );
	        childFragTrans.commit();

	    }
	 public void addChildFragmentBackStack(Fragment fragment,String tag) {
	        FragmentManager childFragMan = getChildFragmentManager();

	        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
	        childFragTrans.replace(R.id.chat_container, fragment,tag );
	        childFragTrans.addToBackStack(null);
	        childFragTrans.commit();

	    }*/

	 @Override
	public void onDestroyView() {
		super.onDestroyView();
		//homeActivity.homeContainerIsVisiable();
		ConstantFlag.FLAG_CHAT_CONTACT=false;
		//ConstantFields.HIDE_MENU=false;
		//homeActivity.h
		//homeActivity.showMenuMethod();
		
		//homeActivity.onBothTabPageControlIsGone();
		homeActivity.removeCustomActionBarView();
	}

	public LinearLayout getmChatContactHeader() {
		return mChatContactHeader;
	}
	 
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		 Fragment fragment = getChildFragmentManager().findFragmentByTag(ConstantFlag.TAB_SINGLE_CHAT_ROOM_FRAGMENT);
	      if(fragment != null){
	            fragment.onActivityResult(requestCode, resultCode, data);
	      }
		
	}
	
	 
	
	
}
