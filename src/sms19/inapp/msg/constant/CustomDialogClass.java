package sms19.inapp.msg.constant;

import com.kitever.android.R;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by ubuntu on 17/11/15.
 */
public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button mViewContact, mViewChat,mDeleteChat;
    private Animation mAnimationBottomCentre;
    private View.OnClickListener clickListener;
   // private LinearLayout mCloseBtn;

    public CustomDialogClass(Activity a) {
        super(a, R.style.DialogAnimation);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.delete_view_contactview_view);

//  LinearLayout ll = (LinearLayout) findViewById(R.id.mainlayout);
//  mAnimationBottomCentre = AnimationUtils.loadAnimation(c, R.anim.slide_in_left);
//  ll.startAnimation(mAnimationBottomCentre);
        mViewContact = (Button) findViewById(R.id.btn_facebook);
        mViewChat = (Button) findViewById(R.id.btn_twitter);
        mDeleteChat = (Button) findViewById(R.id.btn_email);
       // mCloseBtn= (LinearLayout) findViewById(R.id.close_btn);
        mViewContact.setOnClickListener(clickListener);
        mViewChat.setOnClickListener(clickListener);
        mDeleteChat.setOnClickListener(clickListener);
       // mCloseBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

	public Button getmViewContact() {
		return mViewContact;
	}

	public Button getmViewChat() {
		return mViewChat;
	}

	public Button getmDeleteChat() {
		return mDeleteChat;
	}

	public void setmViewContact(Button mViewContact) {
		this.mViewContact = mViewContact;
	}

	public void setmViewChat(Button mViewChat) {
		this.mViewChat = mViewChat;
	}

	public void setmDeleteChat(Button mDeleteChat) {
		this.mDeleteChat = mDeleteChat;
	}
    
    
    
    
    
}