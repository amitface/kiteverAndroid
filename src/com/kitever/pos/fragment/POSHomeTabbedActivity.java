package com.kitever.pos.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.fragment.purchaseList.Inventory.InventoryListFragment;
import com.kitever.pos.fragment.purchaseList.PurchaseListFragment;
import com.kitever.pos.fragment.purchaseList.PurchaseOutStanding.PuchaseOutStandingFragment;
import com.kitever.pos.fragment.purchaseList.PurchasePayment.PurchasePaymentFragment;
import com.kitever.pos.fragment.purchaseList.Vendor.VendorDetailFragment;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

public class POSHomeTabbedActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, android.support.v7.widget.PopupMenu.OnMenuItemClickListener {

    private LinearLayout layoutPosFragmentHolder;
    private RecyclerView recyclerView;
    //private POSHomeTabbedAdapter posHomeTabbedAdapter;
    private String userLogin, passWord, userId;
    private SharedPreferences prfs;


    private ExecuteService executeService;
    private ProgressDialog progressDialog = null;
    private Toolbar toolbar;
    private MoonIcon micon;
    private TabLayout tabLayout;
    private int menuOption = 1;
    private FragmentManager fragmentManager;
    private String screenName="";
    private int FragmentPos=1;
    private int FragmentTabPos=1;

    int res1[] = {R.string.homeicon, R.string.dashboardicon, R.string.category_icon, R.string.ordericon, R.string.customer_icon, R.string.paymenticon,
            R.string.producticon, R.string.setting_icon};

    String str[] = {"Home", "Dashboard", "Categories", "Orders", "Customers", "Purchase",
            "Vendor", "Settings"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBarSettingWithBack(this, getSupportActionBar(), "mCRM DashBoard");
        setContentView(R.layout.activity_poshome_tabbed);
        prfs = getSharedPreferences("profileData",
                Context.MODE_PRIVATE);
        micon = new MoonIcon(this);
        userLogin = prfs.getString("user_login", "");
        passWord = prfs.getString("Pwd", "");
        userId = Utils.getUserId(this);
        try {
            //screenName = getIntent().getStringExtra("FragmentPos");
            FragmentPos=getIntent().getIntExtra("FragmentPos",1);
            FragmentTabPos=getIntent().getIntExtra("TabPos",1);

        }
        catch(Exception e){}

//        getSupportFragmentManager().beginTransaction().add(R.id.pos_fragment_holder, PosHome.newInstance("",""), "PosHome").commit();
        layoutPosFragmentHolder = (LinearLayout) findViewById(R.id.layoutPosFragmentHolder);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.layoutPosFragmentHolder, PosHome.newInstance(getUserId(), getUserLogin(), getPassWord()), "PosHome").commit();


        tabLayout = (TabLayout) findViewById(R.id.posHomeTabs);
        tabLayout.setBackgroundColor(Color.parseColor(CustomStyle.FOOTER_BACKGROUND));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        tabLayout.setSmoothScrollingEnabled(true);

        final TextView leftarrow = (TextView) findViewById(R.id.tvPosLeftArrow);
        final TextView rightarrow = (TextView) findViewById(R.id.tvPosRightArrow);
        leftarrow.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        rightarrow.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        leftarrow.setBackgroundColor(Color.parseColor(CustomStyle.FOOTER_BACKGROUND));
        rightarrow.setBackgroundColor(Color.parseColor(CustomStyle.FOOTER_BACKGROUND));

        micon.setTextfont(leftarrow);
        micon.setTextfont(rightarrow);

//
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

        selectTabFromTabLAyout(FragmentTabPos);


        tabLayout.setOnTabSelectedListener((TabLayout.OnTabSelectedListener) this);

        selectedTab(FragmentPos);


    }

    private void selectTabFromTabLAyout(int pos)
    {
        for (int i = 0; i < res1.length; i++) {
            LinearLayout linearLayout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.custom_tab, null);
            TextView child1 = (TextView) linearLayout.getChildAt(0);
            child1.setText(getResources().getString(res1[i]));
            TextView child2 = (TextView) linearLayout.getChildAt(1);
            child2.setText(str[i]);
            micon.setTextfont(child1);
            setRobotoThinFont(child2, this);
            if (i != pos)
                tabLayout.addTab(tabLayout.newTab().setCustomView(linearLayout));
            else
                tabLayout.addTab(tabLayout.newTab().setCustomView(linearLayout), true);
            if (i == pos) {
                child1.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
                child2.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));

            } else {
                child1.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_UNSELECTED));
                child2.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_UNSELECTED));
            }
        }
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
    public void onTabSelected(Tab tab) {

        PopupMenu popupMenu;

        if (!prfs.contains("isPosSettingEnabled")) {
            if (tab.getPosition() != 1 || tab.getPosition() != 0 || tab.getPosition() != 7) {
                selectedTab(12);
                setTabSelected(tab);
                Toast.makeText(POSHomeTabbedActivity.this, "Please configure your settings first", Toast.LENGTH_LONG).show();
                return;
            }

        } else if (prfs.getString("isPosSettingEnabled", "0").equals("0")) {
            if (tab.getPosition() != 1 || tab.getPosition() != 0 || tab.getPosition() != 12)
            {
                selectedTab(12);
                setTabSelected(tab);
                Toast.makeText(POSHomeTabbedActivity.this, "Please configure your settings first", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (tab.getPosition() == 0)
            finish();
        else if (tab.getPosition() == 1) {
            selectedTab(1);
            setTabSelected(tab);
        } else if (tab.getPosition() == 2) {
            setTabSelected(tab);
            popupMenu = new PopupMenu(POSHomeTabbedActivity.this, tab.getCustomView());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.category_menu);
            popupMenu.show();
        } else if (tab.getPosition() == 3) {
            setTabSelected(tab);
            popupMenu = new PopupMenu(POSHomeTabbedActivity.this, tab.getCustomView());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.order_menu);
            popupMenu.show();
        } else if (tab.getPosition() == 4) {
            setTabSelected(tab);
            selectedTab(5);
        } else if (tab.getPosition() == 5) {
            setTabSelected(tab);
            popupMenu = new PopupMenu(POSHomeTabbedActivity.this, tab.getCustomView());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.purchase_menu);
            popupMenu.show();
        } else if (tab.getPosition() == 6) {
            setTabSelected(tab);
            selectedTab(16);
        } else if (tab.getPosition() == 7) {
            setTabSelected(tab);
            selectedTab(12);
        }



    }

    private void setTabSelected(Tab tab) {
        LinearLayout linearLayout =
                (LinearLayout) tab.getCustomView();
        TextView textView = (TextView) linearLayout.getChildAt(0);
        TextView textView1 = (TextView) linearLayout.getChildAt(1);
        textView.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
        textView1.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_SELECTED));
    }

    private void setTabUnselected(Tab tab) {
        LinearLayout linearLayout =
                (LinearLayout) tab.getCustomView();
        TextView textView = (TextView) linearLayout.getChildAt(0);
        TextView textView1 = (TextView) linearLayout.getChildAt(1);
        textView.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_UNSELECTED));
        textView1.setTextColor(Color.parseColor(CustomStyle.FOOTER_ICON_COLOR_UNSELECTED));
    }

    @Override
    public void onTabUnselected(Tab tab) {
        setTabUnselected(tab);
    }

    @Override
    public void onTabReselected(Tab tab) {
        PopupMenu popupMenu;
        if (tab.getPosition() == 2) {
            popupMenu = new PopupMenu(POSHomeTabbedActivity.this, tab.getCustomView());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.category_menu);
            popupMenu.show();
        } else if (tab.getPosition() == 3) {
            popupMenu = new PopupMenu(POSHomeTabbedActivity.this, tab.getCustomView());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.order_menu);
            popupMenu.show();
        } else if (tab.getPosition() == 5) {
            popupMenu = new PopupMenu(POSHomeTabbedActivity.this, tab.getCustomView());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.purchase_menu);
            popupMenu.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragmentManager.findFragmentByTag("PosHome") != null && fragmentManager.findFragmentByTag("PosHome").isVisible()) {
            finish();
        } else if (fragmentManager.findFragmentByTag("POSTaxMasterScreenFragment") != null && fragmentManager.findFragmentByTag("POSTaxMasterScreenFragment").isVisible()) {
            selectedTab(12);
        } else if (fragmentManager.findFragmentByTag("POSOtherChargeScreenFragment") != null && fragmentManager.findFragmentByTag("POSOtherChargeScreenFragment").isVisible()) {
            selectedTab(12);
        } else selectedTab(1);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                if (fragmentManager.findFragmentByTag("PosHome") != null && fragmentManager.findFragmentByTag("PosHome").isVisible()) {
                    finish();
                } else if (fragmentManager.findFragmentByTag("POSTaxMasterScreenFragment") != null && fragmentManager.findFragmentByTag("POSTaxMasterScreenFragment").isVisible()) {
                    selectedTab(12);
                } else if (fragmentManager.findFragmentByTag("POSOtherChargeScreenFragment") != null && fragmentManager.findFragmentByTag("POSOtherChargeScreenFragment").isVisible()) {

                    selectedTab(12);
                } else
                    // selectedTab(1);
                     tabLayout.getTabAt(1).select();
                break;
        }
        return true;
    }

    private void selectedTab(int position) {
        switch (position) {
            case 0:
                actionBarSettingWithBack(this, getSupportActionBar(), "Home");
                changeFragment(POSHomeViewPager.PlaceholderFragment.newInstance(position + 1), "");
                break;
            case 1:
                actionBarSettingWithBack(this, getSupportActionBar(), "mCRM DashBoard");
                changeFragment(PosHome.newInstance(getUserId(), getUserLogin(), getPassWord()), "PosHome");
                break;
            case 2:
                actionBarSettingWithBack(this, getSupportActionBar(), "Categories");
                changeFragment(POSCategoryScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "POSCategoryScreenFragment");
                setTabSelected(tabLayout.getTabAt(2));
                break;
            case 3:
                actionBarSettingWithBack(this, getSupportActionBar(), "Brands");
                changeFragment(POSBrandScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "POSBrandScreenFragment");
                setTabSelected(tabLayout.getTabAt(2));
                break;
            case 4:
                actionBarSettingWithBack(this, getSupportActionBar(), "Products");
                changeFragment(PosProductScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "PosProductScreenFragment");
                setTabSelected(tabLayout.getTabAt(2));
                break;
            case 5:
                actionBarSettingWithBack(this, getSupportActionBar(), "Customers");
                changeFragment(PosCustomerListFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "PosCustomerListFragment");
                break;
            case 6:
                actionBarSettingWithBack(this, getSupportActionBar(), "Orders");
                changeFragment(POSOrdersScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "POSOrdersScreenFragment");
                setTabSelected(tabLayout.getTabAt(3));
                break;
            case 7:
                actionBarSettingWithBack(this, getSupportActionBar(), "Payments");
                changeFragment(POSPaymentDetailScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "POSPaymentDetailScreenFragment");
                setTabSelected(tabLayout.getTabAt(3));
                break;
            case 8:
                actionBarSettingWithBack(this, getSupportActionBar(), "Credits");
                changeFragment(POSCreditScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "POSCreditScreenFragment");
                setTabSelected(tabLayout.getTabAt(3));
                break;
            case 9:
                actionBarSettingWithBack(this, getSupportActionBar(), "Invoices");
                changeFragment(POSInvoiceScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "POSInvoiceScreenFragment");
                setTabSelected(tabLayout.getTabAt(3));
                break;
            case 10:
                actionBarSettingWithBack(this, getSupportActionBar(), "Taxes");
                changeFragment(POSTaxMasterScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "POSTaxMasterScreenFragment");
                break;
            case 11:
                actionBarSettingWithBack(this, getSupportActionBar(), "Other Charges");
                changeFragment(POSOtherChargeScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "POSOtherChargeScreenFragment");
                break;
            case 12:
                actionBarSettingWithBack(this, getSupportActionBar(), "Settings");
                changeFragment(POSSettingScreenFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "POSSettingScreenFragment");
                break;
            case 13:
                actionBarSettingWithBack(this, getSupportActionBar(), "Purchases");
                changeFragment(PurchaseListFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "PurchaseListFragment");
                setTabSelected(tabLayout.getTabAt(5));
                break;
            case 14:
                actionBarSettingWithBack(this, getSupportActionBar(), "Outstandings");
                changeFragment(PuchaseOutStandingFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "PuchaseOutStandingFragment");
                setTabSelected(tabLayout.getTabAt(5));
                break;
            case 15:
                actionBarSettingWithBack(this, getSupportActionBar(), "Purchase Payments");
                changeFragment(PurchasePaymentFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "PurchasePaymentFragment");
                setTabSelected(tabLayout.getTabAt(5));
                break;
            case 16:
                actionBarSettingWithBack(this, getSupportActionBar(), "Vendors");
                changeFragment(VendorDetailFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "VendorDetailFragment");
                break;
            case 17:
                actionBarSettingWithBack(this, getSupportActionBar(), "Inventory");
                changeFragment(InventoryListFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "InventoryListFragment");
                setTabSelected(tabLayout.getTabAt(2));
                break;

            default:
                actionBarSettingWithBack(this, getSupportActionBar(), "mCRM DashBoard");
                changeFragment(PosHome.newInstance(getUserId(), getUserLogin(), getPassWord()), "PosHome");
                break;
        }
    }

    private void changeFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).replace(R.id.layoutPosFragmentHolder, fragment, tag).commit();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCategories:
                selectedTab(2);
                return true;
            case R.id.menuBrands:
                selectedTab(3);
                return true;
            case R.id.menuProducts:
                selectedTab(4);
                return true;
            case R.id.menuInventory:
                selectedTab(17);
                return true;
            case R.id.menuOrders:
                selectedTab(6);
                return true;
            case R.id.menuPayments:
                selectedTab(7);
                return true;
            case R.id.menuCredits:
                selectedTab(8);
                return true;

            case R.id.menuInvoice:
                selectedTab(9);
                return true;

            case R.id.posPurchaseList:
                selectedTab(13);

                return true;
            case R.id.posPurchaseOustanding:
                selectedTab(14);

                return true;
            case R.id.posPurchasePayments:
                selectedTab(15);
                return true;
        }
        return false;
    }
}

/*
        posHomeTabbedAdapter = new POSHomeTabbedAdapter(new MoonIcon(this));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerPosHome);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(posHomeTabbedAdapter);
    *//*    recyclerView.addOnItemTouchListener(new POSHomeTabbedActivity.RecyclerViewOnTouchListener(this,recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                POSHomeTabbedAdapter.MyViewHolder childViewHolder= (POSHomeTabbedAdapter.MyViewHolder) recyclerView.getChildViewHolder(view);
                Toast.makeText(POSHomeTabbedActivity.this,"selected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public class RecyclerViewOnTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerViewOnTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

            this.clickListener = clickListener;
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null)
                clickListener.onClick(child, recyclerView.getChildAdapterPosition(child));
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    *//*
       */
// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.