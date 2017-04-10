package sms19.listview.newproject;

import sms19.inapp.msg.CircularImageView;
import sms19.listview.newproject.fragment.ReportSmsMailInterface;
import sms19.listview.newproject.fragment.SendMailReportFragment;
import sms19.listview.newproject.fragment.SendSmsReportFragment;
import android.content.Intent;
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

public class SecondSmsMailReport extends AppCompatActivity implements
		ReportSmsMailInterface {

	ReportPagerAdapter mDemoCollectionPagerAdapter;
	ViewPager mViewPager;
	PagerTabStrip pagerTabStrip;
	private TextView sorting_title, mUserNameTitle, actionbar_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second_sms_mail_report);
		actionBarSettingWithBack(this,getSupportActionBar(),"Reports");
		mDemoCollectionPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.report_pager);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);
		SlidingTabLayout slidingtab = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		slidingtab.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
		slidingtab.setSelectedIndicatorColors(Color.parseColor(CustomStyle.TAB_INDICATOR));
		slidingtab.setViewPager(mViewPager);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public class ReportPagerAdapter extends FragmentStatePagerAdapter {

		private SendSmsReportFragment m1stFragment;
		private SendMailReportFragment m2ndFragment;

		public ReportPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = null;
			switch (i) {
			case 0:
				fragment = SendSmsReportFragment.newIntance();
				break;
			case 1:
				fragment = SendMailReportFragment.newIntance();
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
				m1stFragment = (SendSmsReportFragment) createdFragment;
				break;
			case 1:
				m2ndFragment = (SendMailReportFragment) createdFragment;
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
		SendMailReportFragment fragment = (SendMailReportFragment) mDemoCollectionPagerAdapter
				.getFragment(1);
		ReportSmsMailInterface interface1 = fragment;
		interface1.changeServiceRequest();
	}
}
