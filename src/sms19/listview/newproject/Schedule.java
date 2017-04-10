package sms19.listview.newproject;

import sms19.inapp.msg.CircularImageView;
import sms19.listview.newproject.fragment.ReportSmsMailInterface;
import sms19.listview.newproject.fragment.ScheduleMail;
import sms19.listview.newproject.fragment.ScheduleSms;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.utils.SlidingTabLayout;

import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class Schedule extends AppCompatActivity implements
		ReportSmsMailInterface {

	private SchedularPagerAdapter mDemoCollectionPagerAdapter;
	private ViewPager mViewPager;
	private PagerTabStrip pagerTabStrip;
	private TextView sorting_title, mUserNameTitle, actionbar_title;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		actionBarSettingWithBack(this,getSupportActionBar(),"Scheduled Message");

		mDemoCollectionPagerAdapter = new SchedularPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.schedule_pager);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);

		SlidingTabLayout slidingtab = (SlidingTabLayout) findViewById(R.id.schedule_sliding_tabs);
		slidingtab.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
		slidingtab.setSelectedIndicatorColors(Color.parseColor(CustomStyle.TAB_INDICATOR));
		slidingtab.setViewPager(mViewPager);
	}



	public class SchedularPagerAdapter extends FragmentStatePagerAdapter {
		private ScheduleSms m1stFragment;
		private ScheduleMail m2ndFragment;

		public SchedularPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = null;
			switch (i) {
			case 0:
				fragment = ScheduleSms.newIntance();
				break;
			case 1:
				fragment = ScheduleMail.newIntance();
				break;
			}

			// Bundle args = new Bundle();
			// // Our object is just an integer :-P
			// args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
			// fragment.setArguments(args);
			return fragment;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			Fragment createdFragment = (Fragment) super.instantiateItem(
					container, position);
			// save the appropriate reference depending on position
			switch (position) {
			case 0:
				m1stFragment = (ScheduleSms) createdFragment;
				break;
			case 1:
				m2ndFragment = (ScheduleMail) createdFragment;
				break;
			}
			return createdFragment;
		}

		public Fragment getFragment(int position) {
			if (position == 0)
				return m1stFragment;
			else
				return m2ndFragment;

		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0)
				return "SMS";
			else
				return "Mail";

		}
	}

	@Override
	public void changeServiceRequest() {
		// TODO Auto-generated method stub
		ScheduleMail fragment = (ScheduleMail) mDemoCollectionPagerAdapter
				.getFragment(1);
		ReportSmsMailInterface interface1 = fragment;
		interface1.changeServiceRequest();
	}
}
