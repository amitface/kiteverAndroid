package sms19.listview.newproject.MerchatStorePackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.MerchatStorePackage.Fragments.AddOrderAdapters.InsertToCart;
import sms19.listview.newproject.MerchatStorePackage.Fragments.CartBuyFragment;
import sms19.listview.newproject.MerchatStorePackage.Fragments.ProductStoreFragment;
import sms19.listview.newproject.MerchatStorePackage.Fragments.SingleUserProductFragment;
import sms19.listview.newproject.MerchatStorePackage.Fragments.UserCartFragment;
import sms19.listview.newproject.MerchatStorePackage.Fragments.zProgressFragment;
import sms19.listview.newproject.MerchatStorePackage.Model.ModelBuyCart.CartModel;
import sms19.listview.newproject.MerchatStorePackage.Model.ModelBuyCart.Product;

public class UserCartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetworkManager, SingleUserProductFragment.ChangeToolbar, InsertToCart {

    private final int KEY_INSERT_PRODUCT_LIST = 1;
    private final int KEY_FETCH_PRODUCT_LIST = 2;
    private final int KEY_UPDATE_CART_LIST = 3;
    private String userLogin, passWord, userId;
    private SharedPreferences prfs;
    private MoonIcon micon;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RequestManager requestManager;
    private HashMap<String, Product> cartProduct;
//    public ProgressBar progressUserCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prfs = getSharedPreferences("profileData",
                Context.MODE_PRIVATE);
        micon = new MoonIcon(this);
        userLogin = prfs.getString("user_login", "");
        passWord = prfs.getString("Pwd", "");
        userId = Utils.getUserId(this);
        cartProduct = new HashMap<>();

//        progressUserCart = (ProgressBar) findViewById(R.id.progressUserCart);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, ProductStoreFragment.newInstance(getUserId(), getUserLogin(), getPassWord()), "ProductStoreFragment").commit();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
//                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                invalidateOptionsMenu();
            }
        });
        FetchProductFromCart();
    }

    @Override
    public void onDestroy() {
        requestManager.getRequestQueue().cancelAll(this);

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportFragmentManager().popBackStack();
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.setToolbarNavigationClickListener(null);
            getSupportActionBar().setTitle("Products");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            getMenuInflater().inflate(R.menu.user_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_merchant_site) {
            getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, UserCartFragment.newInstance(userId, userLogin), "UserCartFragment").addToBackStack(null).commit();
            return true;
        } else if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

    public HashMap<String, Product> getCartProduct() {
        return cartProduct;
    }

    public void setCartProduct(HashMap<String, Product> cartProduct) {
        this.cartProduct = cartProduct;
    }

    @Override
    public void setHome(String title) {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void insertProductToCart(String ProductID, String Quantity) {

        if (Utils.isDeviceOnline(this)) {
            getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, zProgressFragment.newInstance("", ""), "zProgressFragment").addToBackStack("zProgressFragment").commit();
//            progressUserCart.setVisibility(View.VISIBLE);

            Map map = new HashMap<>();
            map.put("Page", "InsertItemsInCart");
            map.put("userID", userId);
            map.put("Password", passWord);
            map.put("UserLogin", userLogin);
            map.put("ProductID", ProductID);
            map.put("Quantity", Quantity);

            try {
                requestManager = new RequestManager();
                requestManager.sendPostRequest(this,
                        KEY_INSERT_PRODUCT_LIST, map);
            } catch (Exception e) {
                // TODO: handle exception
                // hideLoading();
//                progressUserCart.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    public void FetchProductFromCart() {

        if (Utils.isDeviceOnline(this)) {
//            progressUserCart.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, zProgressFragment.newInstance("", ""), "zProgressFragment").addToBackStack("zProgressFragment").commit();
            Map map = new HashMap<>();
            map.put("Page", "FetchItemsInCart");
            map.put("userID", userId);
            map.put("Password", passWord);
            map.put("UserLogin", userLogin);

            try {
                requestManager = new RequestManager();
                requestManager.sendPostRequest(this,
                        KEY_FETCH_PRODUCT_LIST, map);
            } catch (Exception e) {
                // TODO: handle exception
                // hideLoading();
//                progressUserCart.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    public void UpdateCartProduct(String json) {
        if (Utils.isDeviceOnline(this)) {
            getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, zProgressFragment.newInstance("", ""), "zProgressFragment").addToBackStack("zProgressFragment").commit();
//            progressUserCart.setVisibility(View.VISIBLE);

            Map map = new HashMap<>();
            map.put("Page", "InsertCartOrderPlace");
            map.put("userID", userId);
            map.put("Password", passWord);
            map.put("UserLogin", userLogin);
            map.put("Items", json);

            try {
                requestManager = new RequestManager();
                requestManager.sendPostRequest(this,
                        KEY_UPDATE_CART_LIST, map);
            } catch (Exception e) {
                // TODO: handle exception
                // hideLoading();
//                progressUserCart.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        getSupportFragmentManager().popBackStack();
//        progressUserCart.setVisibility(View.GONE);
        Gson gson = new Gson();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_PRODUCT_LIST) {

                CartModel cartModel = gson.fromJson(response, CartModel.class);
                if (cartModel != null && cartModel.getProduct() != null && cartModel.getProduct().size() > 0) {
                    List<Product> list = cartModel.getProduct();
                    Iterator<Product> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Product product = iterator.next();
                        cartProduct.put(product.getmProductID(), product);
                    }
                }
            } else if (requestId == KEY_INSERT_PRODUCT_LIST) {
                CartModel cartModel = gson.fromJson(response, CartModel.class);
                if (cartModel != null && cartModel.getProduct() != null && cartModel.getProduct().size() > 0) {
                    cartProduct.put(cartModel.getProduct().get(0).getmProductID(), cartModel.getProduct().get(0));
                    Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show();
                    if (getSupportFragmentManager().findFragmentByTag("UserCartFragment") != null && getSupportFragmentManager().findFragmentByTag("UserCartFragment").isVisible()) {
                        ((UserCartFragment) (getSupportFragmentManager().findFragmentByTag("UserCartFragment"))).updateCartList(cartModel.getProduct().get(0));
                    }
                }
            } else if (requestId == KEY_UPDATE_CART_LIST) {
                /*If true*/
                CartModel cartModel = gson.fromJson(response, CartModel.class);
                if (cartModel != null && cartModel.getStatus() && cartModel.getProduct() != null && cartModel.getProduct().size() > 0) {
                    cartProduct.put(cartModel.getProduct().get(0).getmProductID(), cartModel.getProduct().get(0));
                    Toast.makeText(this, cartModel.getMessage(), Toast.LENGTH_SHORT).show();
                    if (getSupportFragmentManager().findFragmentByTag("UserCartFragment") != null && getSupportFragmentManager().findFragmentByTag("UserCartFragment").isVisible()) {
                        ((UserCartFragment) (getSupportFragmentManager().findFragmentByTag("UserCartFragment"))).updateCartList(cartModel.getProduct().get(0));
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().add(R.id.content_user_cart, CartBuyFragment.newInstance("", ""), "CartBuyFragment").addToBackStack("CartBuyFragment").commit();
                    }
                }else
                {
                    Toast.makeText(this, cartModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        getSupportFragmentManager().popBackStack();
//        progressUserCart.setVisibility(View.GONE);
        Toast.makeText(this, getResources().getString(R.string.volleyError), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void insertItem(sms19.listview.newproject.MerchatStorePackage.Model.Product product, Integer Quantity) {
        if (cartProduct.get(String.valueOf(product.getProductID())) != null)
            if (cartProduct.get(String.valueOf(product.getProductID())).getAvailableStock() < (Quantity)) {
                Toast.makeText(this, "Cart Quantity + Added Quantity is more than avaliable stock.", Toast.LENGTH_SHORT).show();
                return;
            }
        insertProductToCart(String.valueOf(product.getProductID()), String.valueOf(Quantity));
    }

    @Override
    public void insertCartItem(String productId, Integer Quantity) {
        insertProductToCart(String.valueOf(productId),String.valueOf(-1L * cartProduct.get(productId).getQuantity()));
    }

    @Override
    public void updateCartItem(String json) {
        UpdateCartProduct(json);
    }
}
