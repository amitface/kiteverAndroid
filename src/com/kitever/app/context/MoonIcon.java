package com.kitever.app.context;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.widget.TextView;

public class MoonIcon {

	Typeface moonicon;
	
	public MoonIcon(Context context)
	{
		moonicon = Typeface.createFromAsset(context.getAssets(),  "fonts/icomoon.ttf");
	}

	public MoonIcon(Fragment context)
	{
		moonicon = Typeface.createFromAsset(context.getActivity().getAssets(),  "fonts/icomoon.ttf");
	}

	public void setTextfont(TextView tv)
	{
		tv.setTypeface(moonicon);
	}
}
