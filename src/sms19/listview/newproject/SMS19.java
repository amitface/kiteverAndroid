package sms19.listview.newproject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import com.kitever.android.R;

public class SMS19 extends ActionBarActivity {

	TextView logout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jugad);
		
		/*logout = (TextView) findViewById(R.id.textLgt);
		
		//Animation for text view
		Animation annimate  = AnimationUtils.loadAnimation(this, R.anim.textblinking);
		logout.startAnimation(annimate);*/
		
		
		finish();
		
		
	}


}
