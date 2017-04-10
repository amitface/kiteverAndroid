package sms19.listview.newproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import com.kitever.android.R;

public class FullScreen extends Activity 
{
	ImageView imagefull;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullimagescreen);
		imagefull=(ImageView)findViewById(R.id.fullscrenimage);
		try 
		{
			Intent idu=getIntent();
			String a=idu.getStringExtra("image");
			Bitmap bitmap = BitmapFactory.decodeFile(a);
			
			imagefull.setImageBitmap(bitmap);
		} 
		catch (Exception e) {
		
		}
		
		
	
		 /* if(zoomOut) {
		  holder.imageSendChat.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		  holder.imageSendChat.setAdjustViewBounds(true);
              zoomOut =false;
          }else{
          	holder.imageSendChat.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
          	holder.imageSendChat.setScaleType(ImageView.ScaleType.FIT_XY);
              zoomOut = true;
          }*/
	}
}
