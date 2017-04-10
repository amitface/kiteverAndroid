package com.kitever.pos.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.fragment.Interfaces.SetViewPager;
import com.kitever.pos.fragment.purchaseList.Inventory.InventoryListFragment;
import com.kitever.pos.fragment.purchaseList.PurchaseListFragment;
import com.kitever.pos.fragment.purchaseList.PurchaseOutStanding.PuchaseOutStandingFragment;
import com.kitever.pos.fragment.purchaseList.PurchasePayment.PurchasePaymentFragment;
import com.kitever.pos.fragment.purchaseList.Vendor.VendorDetailFragment;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class POSHomeViewPager extends AppCompatActivity implements ViewPager.OnPageChangeListener, SetViewPager, TabLayout.OnTabSelectedListener, android.support.v7.widget.PopupMenu.OnMenuItemClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String userLogin, passWord, userId;
    private ExecuteService executeService;
    private ProgressDialog progressDialog = null;
    private Toolbar toolbar;
    private MoonIcon micon;
    private TabLayout tabLayout;
    private SharedPreferences prfs;
    private int menuOption = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poshome_view_pager);

        prfs = getSharedPreferences("profileData",
                Context.MODE_PRIVATE);
        // userLogin = "+919810398913";
        // passWord = "157013";
        // userId = "400";
        micon = new MoonIcon(this);

        userLogin = prfs.getString("user_login", "");
        passWord = prfs.getString("Pwd", "");
        userId = Utils.getUserId(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextAppearance(this, R.style.viewPagerTitleStrip);
        toolbar.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));


        //actionBarSettingWithBack(this,getSupportActionBar(),"mCRM Dashboard");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setEnabled(false);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(Color.parseColor(CustomStyle.FOOTER_BACKGROUND));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSmoothScrollingEnabled(true);

        int res1[] = {R.string.homeicon, R.string.dashboardicon, R.string.category_icon, R.string.brand_icon, R.string.producticon
                , R.string.customer_icon, R.string.ordericon, R.string.paymenticon, R.string.crediticon,
                R.string.invoice_icon, R.string.tax_icon, R.string.other_charges_icon,
                R.string.setting_icon, R.string.producticon,R.string.producticon,R.string.producticon,R.string.producticon,R.string.producticon};

        String str[] = {"Home", "Dashboard", "Categories", "Brands", "Products", "Customers", "Orders", "Payments", "Credit",
                "Invoice", "Taxes", "Charges", "Settings", "Purchase","Outstanding","PPayments","Vendor","Inventory"};

        final TextView leftarrow = (TextView) findViewById(R.id.bottom_left_arrow);
        final TextView rightarrow = (TextView) findViewById(R.id.bottom_right_arrow);
        leftarrow.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        rightarrow.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        leftarrow.setBackgroundColor(Color.parseColor(CustomStyle.FOOTER_BACKGROUND));
        rightarrow.setBackgroundColor(Color.parseColor(CustomStyle.FOOTER_BACKGROUND));

        micon.setTextfont(leftarrow);
        micon.setTextfont(rightarrow);

        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                tabLayout.arrowScroll(ScrollView.FOCUS_LEFT);
            }
        });

        rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tabLayout.arrowScroll(ScrollView.FOCUS_RIGHT);
            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.custom_tab, null);
            TextView child1 = (TextView) linearLayout.getChildAt(0);
            child1.setText(getResources().getString(res1[i]));
            TextView child2 = (TextView) linearLayout.getChildAt(1);
            child2.setText(str[i]);
            micon.setTextfont(child1);
            setRobotoThinFont(child2,this);
            tabLayout.getTabAt(i).setCustomView(linearLayout);
            if (i == 1) {
                child1.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
                child2.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));

            } else {
                child1.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_UNSELECTED));
                child2.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_UNSELECTED));
            }
        }
        tabLayout.setOnTabSelectedListener((TabLayout.OnTabSelectedListener) this);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (mViewPager.getCurrentItem() == 1)
            super.onBackPressed();
        else
            mViewPager.setCurrentItem(1);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (mViewPager.getCurrentItem() == 1)
                    onBackPressed();
                else
                    mViewPager.setCurrentItem(1);
                break;
        }
        return true;
    }


    String getUserId() {
        return userId;
    }

    String getUserLogin() {
        return userLogin;
    }

    String getPassWord() {
        return passWord;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0.5) {

        }
    }

    @Override
    public void onPageSelected(int position) {

        switch (position) {
            case 0:
                //toolbar.setTitle("Home");
                toolbar.setTitle("mCRM Dashboard");
                finish();
                break;
            case 1:
               //toolbar.setTitle(getString(R.string.title_activity_poshome_view_pager));
                toolbar.setTitle("mCRM Dashboard");
                break;
            case 2:
               toolbar.setTitle("Categories");
                POSCategoryScreenFragment categoryFragment = (POSCategoryScreenFragment) (mSectionsPagerAdapter.getFragment(position));
                executeService = (ExecuteService) categoryFragment;
                executeService.executeService();
                break;
            case 3:
               toolbar.setTitle("Brands");
                POSBrandScreenFragment brandFragment = (POSBrandScreenFragment) (mSectionsPagerAdapter.getFragment(position));
                executeService = (ExecuteService) brandFragment;
                executeService.executeService();
                break;
            case 4:
               toolbar.setTitle("Products");
                PosProductScreenFragment productFragment = (PosProductScreenFragment) (mSectionsPagerAdapter.getFragment(position));
                executeService = (ExecuteService) productFragment;
                executeService.executeService();
                break;

            case 5:
               toolbar.setTitle("Customers");
                PosCustomerListFragment customerFragment = (PosCustomerListFragment) (mSectionsPagerAdapter.getFragment(position));
                executeService = (ExecuteService) customerFragment;
                executeService.executeService();
                break;
            case 6:
               toolbar.setTitle("Orders");
                POSOrdersScreenFragment orderFragment = (POSOrdersScreenFragment) (mSectionsPagerAdapter.getFragment(position));
                executeService = (ExecuteService) orderFragment;
                executeService.executeService();
                break;


            case 7:
               toolbar.setTitle("Payments");
                POSPaymentDetailScreenFragment paymentFragment = (POSPaymentDetailScreenFragment) (mSectionsPagerAdapter.getFragment(position));
                executeService = (ExecuteService) paymentFragment;
                executeService.executeService();
                break;

            case 8:
               toolbar.setTitle("Credits");
                POSCreditScreenFragment creditFragment = (POSCreditScreenFragment) (mSectionsPagerAdapter.getFragment(position));
                executeService = (ExecuteService) creditFragment;
                executeService.executeService();
                break;

            case 9:
               toolbar.setTitle("Invoice");
                POSInvoiceScreenFragment invoiceFragment = (POSInvoiceScreenFragment) (mSectionsPagerAdapter.getFragment(position));
                executeService = (ExecuteService) invoiceFragment;
                executeService.executeService();
                break;

            case 10:
               toolbar.setTitle("Taxes");
                POSTaxMasterScreenFragment taxFragment = (POSTaxMasterScreenFragment) (mSectionsPagerAdapter.getFragment(position));
                executeService = (ExecuteService) taxFragment;
                executeService.executeService();
                break;

            case 11:
               toolbar.setTitle("Charges");
                POSOtherChargeScreenFragment chargesFragment = (POSOtherChargeScreenFragment) (mSectionsPagerAdapter.getFragment(position));
                executeService = (ExecuteService) chargesFragment;
                executeService.executeService();
                break;

            case 12:
               toolbar.setTitle("Settings");
                POSSettingScreenFragment settingFragment = (POSSettingScreenFragment) (mSectionsPagerAdapter.getFragment(position));
                executeService = (ExecuteService) settingFragment;
                executeService.executeService();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void ChangePage(int i) {
        mViewPager.setCurrentItem(i);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        if (!prfs.contains("isPosSettingEnabled")) {
            if (tab.getPosition() == 1 || tab.getPosition() == 0 || tab.getPosition() == 12)
                mViewPager.setCurrentItem(tab.getPosition());
            else {
                mViewPager.setCurrentItem(12);
                Toast.makeText(POSHomeViewPager.this, "Please configure your settings first", Toast.LENGTH_LONG).show();
            }
        } else if (prfs.getString("isPosSettingEnabled", "0").equals("0")) {
            if (tab.getPosition() == 1 || tab.getPosition() == 0 || tab.getPosition() == 12)
                mViewPager.setCurrentItem(tab.getPosition());
            else {
                mViewPager.setCurrentItem(12);
                Toast.makeText(POSHomeViewPager.this, "Please configure your settings first", Toast.LENGTH_LONG).show();
            }
        } else {
            mViewPager.setCurrentItem(tab.getPosition());
            /*if (tab.getPosition() == 13) {
                PopupMenu popupMenu = new PopupMenu(POSHomeViewPager.this, tab.getCustomView());
                popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) POSHomeViewPager.this);
                popupMenu.inflate(R.menu.pos_purchase);
                popupMenu.show();
                return;
            } else {
                mViewPager.setCurrentItem(tab.getPosition());
            }*/
        }
        LinearLayout linearLayout =
                (LinearLayout) tab.getCustomView();
        TextView textView = (TextView) linearLayout.getChildAt(0);
        TextView textView1 = (TextView) linearLayout.getChildAt(1);
        textView.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        textView1.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        /*if (tab.getPosition() == 4)
            return;
        else {}*/
            LinearLayout linearLayout =
                    (LinearLayout) tab.getCustomView();
            TextView textView = (TextView) linearLayout.getChildAt(0);
            TextView textView1 = (TextView) linearLayout.getChildAt(1);
            textView.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_UNSELECTED));
            textView1.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_UNSELECTED));

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

      /*  switch (item.getItemId()) {
            case R.id.posPurchaseList:
                menuOption = 1;
                mViewPager.setCurrentItem(13);
                return true;
            case R.id.posPurchaseOustanding:
                menuOption = 2;
                mViewPager.setCurrentItem(13);
                return true;
            case R.id.posPurchasePayments:
                menuOption = 3;
                mViewPager.setCurrentItem(13);
                return true;
        }*/
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_poshome_view_pager, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private PlaceholderFragment mFragment;
        private PosHome posDashboard;
        private POSCategoryScreenFragment posCategory;
        private PosProductScreenFragment posProduct;
        private PosCustomerListFragment posCustomer;
        private POSOrdersScreenFragment posOrders;
        private POSCreditScreenFragment posCredits;
        private POSPaymentDetailScreenFragment posPayments;
        private POSTaxMasterScreenFragment posTax;
        private POSBrandScreenFragment posBrand;
        private POSOtherChargeScreenFragment posOTC;
        private POSInvoiceScreenFragment posInvoice;
        private POSSettingScreenFragment posSettings;
        private PurchaseListFragment purchaseListFragment;
        private PuchaseOutStandingFragment purchaseOutStandingFragment;
        private PurchasePaymentFragment purchasePaymentFragment;
        private VendorDetailFragment vendorDetailFragment;
        private InventoryListFragment inventoryListFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = PlaceholderFragment.newInstance(position + 1);
                    break;
                case 1:
                    fragment = PosHome.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 2:
                    fragment = POSCategoryScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 3:
                    fragment = POSBrandScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 4:
                    fragment = PosProductScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 5:
                    fragment = PosCustomerListFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 6:
                    fragment = POSOrdersScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 7:
                    fragment = POSPaymentDetailScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 8:
                    fragment = POSCreditScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;

                case 9:
                    fragment = POSInvoiceScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 10:
                    fragment = POSTaxMasterScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 11:
                    fragment = POSOtherChargeScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 12:
                    fragment = POSSettingScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 13:
                    fragment = PurchaseListFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 14:
                    fragment = PuchaseOutStandingFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 15:
                    fragment = PurchasePaymentFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 16:
                    fragment = VendorDetailFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                case 17:
                    fragment = InventoryListFragment.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
                    /*switch (menuOption)
                    {
                        case 1:

                            break;
                        case 2:

                            break;
                    }*/

                default:
                    fragment = PosHome.newInstance(getUserId(), getUserLogin(), getPassWord());
                    break;
            }
            return fragment;

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(
                    container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    mFragment = (PlaceholderFragment) createdFragment;
                    break;
                case 1:
                    posDashboard = (PosHome) createdFragment;
                    break;
                case 2:
                    posCategory = (POSCategoryScreenFragment) createdFragment;
                    break;
                case 3:
                    posBrand = (POSBrandScreenFragment) createdFragment;
                    break;
                case 4:
                    posProduct = (PosProductScreenFragment) createdFragment;
                    break;
                case 5:
                    posCustomer = (PosCustomerListFragment) createdFragment;
                    break;
                case 6:
                    posOrders = (POSOrdersScreenFragment) createdFragment;
                    break;
                case 7:
                    posPayments = (POSPaymentDetailScreenFragment) createdFragment;
                    break;

                case 8:
                    posCredits = (POSCreditScreenFragment) createdFragment;
                    break;

                case 9:
                    posInvoice = (POSInvoiceScreenFragment) createdFragment;
                    break;
                case 10:
                    posTax = (POSTaxMasterScreenFragment) createdFragment;
                    break;
                case 11:
                    posOTC = (POSOtherChargeScreenFragment) createdFragment;
                    break;
                case 12:
                    posSettings = (POSSettingScreenFragment) createdFragment;
                    break;
                case 13:
                    purchaseListFragment = (PurchaseListFragment) createdFragment;
                    break;
                case 14:
                    purchaseOutStandingFragment = (PuchaseOutStandingFragment) createdFragment;
                    break;
                case 15:
                    purchasePaymentFragment = (PurchasePaymentFragment) createdFragment;
                    break;
                case 16:
                    vendorDetailFragment = (VendorDetailFragment) createdFragment;
                    break;
                case 17:
                    inventoryListFragment = (InventoryListFragment) createdFragment;
                    break;
                    /*switch (menuOption)
                    {
                        case 1:
                            purchaseListFragment = (PurchaseListFragment) createdFragment;
                            break;
                        case 2:
                            purchaseOutStandingFragment = (PuchaseOutStandingFragment) createdFragment;
                            break;
                    }*/


                default:
                    posDashboard = (PosHome) createdFragment;
                    break;
            }
            return createdFragment;
        }

        public Fragment getFragment(int position) {
            switch (position) {
                case 0:
                    return mFragment;
                case 1:
                    return posDashboard;
                case 2:
                    return posCategory;
                case 3:
                    return posBrand;
                case 4:
                    return posProduct;
                case 5:
                    return posCustomer;
                case 6:
                    return posOrders;
                case 7:
                    return posPayments;
                case 8:
                    return posCredits;
                case 9:
                    return posInvoice;
                case 10:
                    return posTax;
                case 11:
                    return posOTC;
                case 12:
                    return posSettings;
                case 13:
                    return purchaseListFragment;
                case 14:
                    return purchaseOutStandingFragment;
                case 15:
                    return purchasePaymentFragment;
                case 16:
                    return vendorDetailFragment;
                case 17:
                    return inventoryListFragment;
                    /*switch (menuOption)
                    {
                        case 1:

                        case 2:

                    }*/
                default:
                    return posDashboard;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 18;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Dashboard";
                case 2:
                    return "Categories";
                case 3:
                    return "Brands";
                case 4:
                    return "Products";
                case 5:
                    return "Customers";
                case 6:
                    return "Orders";
                case 7:
                    return "Payments";
                case 8:
                    return "Credit";
                case 9:
                    return "Invoice";
                case 10:
                    return "Taxes";
                case 11:
                    return "Charges";

                case 12:
                    return "Settings";
                case 13:
                    return "Purchase list";
                case 14:
                    return "Purchase OutStanding";
                case 15:
                    return "Purchase Payment";
                case 16:
                    return "Vendor";
                case 17:
                    return "Inventory";
            }
            return null;
        }
    }

    public void showLoading() {

        int[] textSizeAttr = new int[]{android.R.attr.progressBarStyleSmallInverse};
        int indexOfAttrTextSize = 0;
        TypedValue typedValue = new TypedValue();
        TypedArray a = this.obtainStyledAttributes(typedValue.data,
                textSizeAttr);
        getTheme().resolveAttribute(
                android.R.attr.progressBarStyleSmallInverse, typedValue, true);
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this, R.style.MyTheme);
        }
        // progressDialog.setTitle("Please wait");
        // progressDialog.setMessage("Loading...");

        // progressDialog.setS
        progressDialog
                .setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();
    }

    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
