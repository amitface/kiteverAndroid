package sms19.listview.newproject;

import com.kitever.android.R;
import com.kitever.sendsms.SendSmsActivity;
import com.kitever.sendsms.TemplateActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class EmailSendActivity extends Activity {

	ImageButton templateButton;
	private static final int SELECT_TEMPLATE_REQUEST_CODE = 102;
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.emaile_send_layout);
	 
	 Eventests_Id();
	 }

	private void Eventests_Id() {
		// TODO Auto-generated method stub
		templateButton=(ImageButton)findViewById(R.id.templateButton);
		
		templateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent id = new Intent(EmailSendActivity.this,
						TemplateActivity.class);
				id.putExtra("taketemplate", "send");
				startActivityForResult(id, SELECT_TEMPLATE_REQUEST_CODE);
			}
		});
	}
	
	
	}