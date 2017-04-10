package sms19.listview.adapter;

import sms19.inapp.msg.ChatFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
	
	private static int NUM_ITEMS = 3;
	
    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    
   
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

   
    @Override
    public Fragment getItem(int position) {
        switch (position) {
        case 0: // Fragment # 0 - This will show FirstFragment
            return ChatFragment.newInstance( "Page # 1");
        case 1: // Fragment # 0 - This will show FirstFragment different title
            return ChatFragment.newInstance( "Page # 2");
        
        default:
        	return null;
        }
    }
    
   
    @Override
    public CharSequence getPageTitle(int position) {
    	return "Page " + position;
    }
    


}
