package sms19.listview.newproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.utils.SlidingTabLayout;

import sms19.listview.newproject.fragment.TemplateMailFragment;
import sms19.listview.newproject.fragment.TemplateSmsFragment;

import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class TemplateHomeActivity extends AppCompatActivity {

    private TemplatePagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;
    private PagerTabStrip pagerTabStrip;
    private TextView sorting_title, mUserNameTitle, actionbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_home);

        actionBarSettingWithBack(this, getSupportActionBar(), "Templates");
        mDemoCollectionPagerAdapter = new TemplatePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPagerTemplate);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        SlidingTabLayout slidingtab = (SlidingTabLayout) findViewById(R.id.slidingTabsTemplate);
        slidingtab.setBackgroundColor(Color.parseColor(CustomStyle.TAB_BACKGROUND));
        slidingtab.setSelectedIndicatorColors(Color.parseColor(CustomStyle.TAB_INDICATOR));
        slidingtab.setViewPager(mViewPager);
    }


    public class TemplatePagerAdapter extends FragmentStatePagerAdapter {

        private TemplateSmsFragment m1stFragment;
        private TemplateMailFragment m2ndFragment;

        public TemplatePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                case 0:
                    fragment = TemplateSmsFragment.newInstance("", "");
                    break;
                case 1:
                    fragment = TemplateMailFragment.newInstance("", "");
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
                    m1stFragment = (TemplateSmsFragment) createdFragment;
                    break;
                case 1:
                    m2ndFragment = (TemplateMailFragment) createdFragment;
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
                return "SMS Templates";
            else
                return "Mail Templates";

        }
    }
}
