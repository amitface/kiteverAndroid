package com.kitever.pos.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.app.context.CircleImageView;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.PermissionClass;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.model.manager.ModelManager;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.janmuller.android.simplecropimage.CropImage;
import sms19.inapp.msg.constant.Apiurls;
import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;
import static com.kitever.utils.Utils.actionBarSettingWithBack;


public class POSAddUpdateProduct extends PosBaseActivity implements
        NetworkManager, OnClickListener, OnItemSelectedListener {


    private String screenName;
    private final int KEY_ADD_UPDATE_PRODUCT = 1;
    private final int KEY_FETCH_CATEGORY = 2;
    private final int KEY_FETCH_BRAND = 3;
    private final int KEY_FETCH_UNIT = 4;
    private final int KEY_DELETE_PRODUCT = 5;

    private final int KEY_ADD_CATEGORY_IN_PRODUCT = 101;
    private final int KEY_ADD_BRAND_IN_PRODUCT = 102;
    private int LOAD_IMAGE_RESULTS = 10;
    private final int REQUEST_CODE_CROP_IMAGE = 11;


    private EditText productNameEdit, priceEdit, descriptionEdit, etAddUpdateProductQuantity;
    private String selectedCategory, selectedBrand, productID, categoryID,
            productImage, brandID, units, perUnitPrice = "",
            selectedProductname;
    private String taxApplied = null, Productprice = "", productDescription = "", Quantity="0";
    private String product_category = "", product_brand = "",
            product_units = "", product_description = "", is_active = "A";
    private String productName = "";
    private Spinner brandSpinner, categorySpinner, unitSpinner;
    private String imagePathh = "", finalAmount, taxId;

    private CircleImageView productImagePic;
    private int categoryPos = -1, brandPos = -1, unitPos = -1;
    private int brandSelectedPos = 0, categorySelectedPos = 0,
            unitsSelectedPos = 0;
    private ArrayList<Integer> selectedTaxPos;
    PermissionClass pclass;
    private Dialog dialogAdd;
    private RelativeLayout Quantity_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setLayoutContentView(R.layout.pos_updateproduct_layout);
        Intent intent = getIntent();
        screenName = intent.getStringExtra("screen_name");
        actionBarSettingWithBack(this,getSupportActionBar(),screenName);
        setScreenName(screenName);
        Quantity_layout = (RelativeLayout) findViewById(R.id.Quantity_layout);

        if (screenName.equalsIgnoreCase("Update Product")) {
            productID = intent.getStringExtra("product_id");
            perUnitPrice = intent.getStringExtra("product_price");
            selectedProductname = intent.getStringExtra("product_name");
            productImage = intent.getStringExtra("product_img");
            product_category = intent.getStringExtra("product_category").replace(" < ", "<");
            product_brand = intent.getStringExtra("product_brand");
            product_units = intent.getStringExtra("product_units");
            product_description = intent.getStringExtra("product_description");
            taxApplied = intent.getStringExtra("taxApplied");
            is_active = intent.getStringExtra("is_active");
            Quantity_layout.setVisibility(View.GONE);
        }
        pclass = new PermissionClass(this);
        setBottomAction(true, true, true, true, true, true, true, true, true,
                true, true, true);

        setScreen();
        fetchCategoryList();
        fetchBrandList();
        fetchUnitList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (screenName.equalsIgnoreCase("Update Product")) {
            getMenuInflater().inflate(R.menu.del_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.del_item:
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Do you want to delete it?")
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                        deleteProduct();
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();

                                    }
                                }).show();


                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteProduct() {
        if (Utils.isDeviceOnline(this)) {
            if (screenName.equalsIgnoreCase("Update Product")) {
                Map map = new HashMap<>();
                map.put("Page", "DeleteProduct");
                map.put("ProductId", productID);
                map.put("IsActive", is_active);
                map.put("userID", getUserId());
                map.put("UserLogin", getUserLogin());
                map.put("Password", getPassWord());
                Log.i("ProductMAp", "map - " + map.toString());
                try {
                    new RequestManager().sendPostRequest(POSAddUpdateProduct.this,
                            KEY_DELETE_PRODUCT, map);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } else {
            showMessage("Internet connection not found");
        }
    }

    private void setScreen() {

        Button addUpdate = (Button) findViewById(R.id.btn_update_add);
        productNameEdit = (EditText) findViewById(R.id.product_name);
        priceEdit = (EditText) findViewById(R.id.price);
        descriptionEdit = (EditText) findViewById(R.id.description);
        etAddUpdateProductQuantity = (EditText) findViewById(R.id.etAddUpdateProductQuantity);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        brandSpinner = (Spinner) findViewById(R.id.brand_spinner);
        unitSpinner = (Spinner) findViewById(R.id.unit_spinner);

        RelativeLayout page_main_layout = (RelativeLayout) findViewById(R.id.page_main_layout);
        CardView card_view = (CardView) findViewById(R.id.card_view);
        CardView customer_pic = (CardView) findViewById(R.id.customer_pic);

        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));
        card_view.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));
        customer_pic.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));


        setRobotoThinFont(productNameEdit, this);
        setRobotoThinFont(priceEdit, this);
        setRobotoThinFont(descriptionEdit, this);
        setRobotoThinFontButton(addUpdate, this);

        addUpdate.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        addUpdate.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));


        productNameEdit.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        priceEdit.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        descriptionEdit.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        ImageView addCategoryBtn = (ImageView) findViewById(R.id.category_plus_btn);
        ImageView addBrandBtn = (ImageView) findViewById(R.id.brand_plus_btn);
        productImagePic = (CircleImageView) findViewById(R.id.product_image);
        productImagePic.setOnClickListener(this);
        addUpdate.setOnClickListener(this);
        addCategoryBtn.setOnClickListener(this);
        addBrandBtn.setOnClickListener(this);

        if (screenName.equalsIgnoreCase("Update Product")) {
            addUpdate.setText("Update");
            priceEdit.setText(perUnitPrice);
            productNameEdit.setText(selectedProductname);
            descriptionEdit.setText(product_description);

            if (productImage != null && productImage.length() > 0) {
                String image_url = "http://nowconnect.in/" + getUserId() + "/"
                        + productImage;
                Log.i("image_url", image_url);
                Picasso.with(this).load(image_url)
                        .placeholder(R.drawable.productimg)
                        .error(R.drawable.ic_launcher).into(productImagePic);

            }
        } else {
            addUpdate.setText("Add");
        }

    }

    private boolean validation() {
        productName = productNameEdit.getText().toString();
        Productprice = priceEdit.getText().toString();
        productDescription = descriptionEdit.getText().toString();

        if (productName == "" || productName.isEmpty()) {
            showMessage("Please enter Product name");
            return false;
        }
        if (selectedCategory.equals("Select category")
                || selectedCategory == "" || selectedCategory.isEmpty()) {
            showMessage("Please select Category");
            return false;
        }
        // selectedBrand units
        if (selectedBrand.equals("Select brand") || selectedBrand == ""
                || selectedBrand.isEmpty()) {
            showMessage("Please Select Brand");
            return false;
        }

        if (units.equals("Select unit") || units == "" || units.isEmpty()) {
            showMessage("Please Select Units");
            return false;
        }

        if (Productprice == "" || Productprice.isEmpty()) {
            showMessage("Please Enter Product Price");
            return false;
        }

        return true;
    }

    private void addUpdateProduct() {
        if (Utils.isDeviceOnline(this)) {

            Map map = new HashMap<>();
            if (screenName.equalsIgnoreCase("Update Product")) {
                map.put("Page", "UpdateProduct");
                map.put("IsActive", is_active);
                map.put("ProductID", productID);
            } else {
                map.put("Page", "InsertProduct");
                if(etAddUpdateProductQuantity.length()>0)
                    Quantity = etAddUpdateProductQuantity.getText().toString();
                map.put("AvailableStock",Quantity);
            }

            map.put("ProductName", productName);
            map.put("ProductDescription", productDescription);
            if (categoryPos > -1
                    && ModelManager.getInstance().getFetchParentCategoryModel() != null
                    && ModelManager.getInstance().getFetchParentCategoryModel()
                    .getFetchParentCategoryList() != null
                    && ModelManager.getInstance().getFetchParentCategoryModel()
                    .getFetchParentCategoryList().size() > 0) {
                categoryID = ModelManager.getInstance()
                        .getFetchParentCategoryModel()
                        .getFetchParentCategoryList().get(categoryPos - 1)
                        .getCategoryID();
            }
            map.put("CategoryID", categoryID);
            if (brandPos > -1
                    && ModelManager.getInstance().getBrandModel() != null
                    && ModelManager.getInstance().getBrandModel()
                    .getBrandList() != null
                    && ModelManager.getInstance().getBrandModel()
                    .getBrandList().size() > 0) {
                brandID = ModelManager.getInstance().getBrandModel()
                        .getBrandList().get(brandPos - 1).getBrandID();
            }
            if (units.equalsIgnoreCase("Select unit")) {
                units = "";
            }
            if (brandID == null || brandID.equalsIgnoreCase("null")) {
                brandID = "";
            }
            map.put("BrandID", brandID);

            map.put("Units", units);

            if (finalAmount == null || finalAmount.equalsIgnoreCase("null")
                    || finalAmount == "") {
                finalAmount = Productprice;
            }

            map.put("PriceWithTax", finalAmount);
            map.put("PerUnitPrice", Productprice);

            if (taxId == null || taxId.equalsIgnoreCase("null")) {
                taxId = "0";
            }
            map.put("TaxApplied", taxId);
            if (productImage == null || productImage.equalsIgnoreCase("null")) {
                productImage = "";
            }
            map.put("ProductImage", productImage);
            // map.put("ProductImage", imagetoupload2);

            map.put("userID", getUserId());
            map.put("UserLogin", getUserLogin());
            map.put("Password", getPassWord());

            Log.i("ProductMAp", "map - " + map.toString());
            try {
                new RequestManager().sendPostRequest(POSAddUpdateProduct.this,
                        KEY_ADD_UPDATE_PRODUCT, map);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            showMessage("Internet connection not found");
        }
    }

    private void fetchCategoryList() {
        if (Utils.isDeviceOnline(this)) {
            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchCategoryForSelect");
            map.put("IsActive", "A");
            map.put("userID", getUserId());
            map.put("UserLogin", getUserLogin());
            map.put("Password", getPassWord());
            try {
                new RequestManager().sendPostRequest(this, KEY_FETCH_CATEGORY,
                        map);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    private void fetchBrandList() {
        if (Utils.isDeviceOnline(this)) {
            showLoading();
            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchBrand");
            map.put("userID", getUserId());
            map.put("UserLogin", getUserLogin());
            map.put("Password", getPassWord());
            map.put("IsActive", "A");
            try {
                new RequestManager()
                        .sendPostRequest(this, KEY_FETCH_BRAND, map);
            } catch (Exception e) {

            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    private void fetchUnitList() {
        if (Utils.isDeviceOnline(this)) {
            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "FetchUnitForSelect");
            map.put("userID", getUserId());
            map.put("UserLogin", getUserLogin());
            map.put("Password", getPassWord());
            try {
                new RequestManager().sendPostRequest(this, KEY_FETCH_UNIT, map);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_update_add:
                if (validation()) {
                    Log.i("onclick", "onclick");
                    // addUpdateProduct
                    if (imagePathh != "" && imagePathh != null) {
                        uploadImage();
                    } else {
                        addUpdateProduct();

                    }
                }
                break;
            case R.id.category_plus_btn:
                Intent intent = new Intent(POSAddUpdateProduct.this,
                        POSCategoryAddOrUpdateScreen.class);
                intent.putExtra("screen_name", "Add Category");
                intent.putExtra("isFromProduct", true);
                startActivityForResult(intent, KEY_ADD_CATEGORY_IN_PRODUCT);
                break;
            case R.id.brand_plus_btn:
                Intent intentBrand = new Intent(POSAddUpdateProduct.this,
                        PosAddUpdateBrand.class);
                intentBrand.putExtra("screen_name", "Add Brand");
                intentBrand.putExtra("isFromProduct", true);
                startActivityForResult(intentBrand, KEY_ADD_BRAND_IN_PRODUCT);
                break;
            case R.id.product_image:
                if (pclass.checkPermissionForExternalStorage()) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, LOAD_IMAGE_RESULTS);
                } else pclass.requestPermissionForExternalStorage();
                break;

            default:
                break;
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        Log.i("Response", "Response" + response);
        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_ADD_UPDATE_PRODUCT) {
                try {
                    ModelManager.getInstance().setProductModel(response);
                    String str = ModelManager.getInstance().getProductModel().getMessage();
                    if (ModelManager.getInstance().getProductModel() != null
                            && ModelManager.getInstance().getProductModel()
                            .getMessage() != null
                            && ModelManager.getInstance().getProductModel()
                            .getMessage().length() > 0) {
                        // showMessage(ModelManager.getInstance().getCategoryModel().getMessage());
                        if (ModelManager.getInstance().getProductModel().getStatus().equals("true")) {

                            Toast.makeText(POSAddUpdateProduct.this, ModelManager.getInstance().getProductModel()
                                    .getMessage(), Toast.LENGTH_LONG)
                                    .show();
//							Intent intent = new Intent(this,
//									POSProductsScreen.class);
//							startActivity(intent);
                            Intent intent = new Intent();
                            intent.putExtra("response", response);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else
                            showMessage(ModelManager.getInstance().getProductModel().getMessage());

                    } else {
                        showMessage("Please try again.");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            } else if (requestId == KEY_DELETE_PRODUCT) {
                ModelManager.getInstance().setProductModel(response);
                if (ModelManager.getInstance().getProductModel() != null
                        && ModelManager.getInstance().getProductModel()
                        .getMessage() != null
                        && ModelManager.getInstance().getProductModel()
                        .getMessage().length() > 0) {
                    //showMessage(ModelManager.getInstance().getProductModel().getMessage());
                    Toast.makeText(POSAddUpdateProduct.this, ModelManager.getInstance().getProductModel()
                            .getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("response", response);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showMessage("Please try again.");
                }
            } else if (requestId == KEY_FETCH_BRAND) {
                ModelManager.getInstance().setBrandModel(response);
                ArrayList<String> brandList = new ArrayList<>();
                brandList.add("Select brand");
                if (ModelManager.getInstance().getBrandModel() != null
                        && ModelManager.getInstance().getBrandModel()
                        .getBrandList() != null
                        && ModelManager.getInstance().getBrandModel()
                        .getBrandList().size() > 0) {

                    for (int k = 0; k < ModelManager.getInstance()
                            .getBrandModel().getBrandList().size(); k++) {
                        String BrandName = ModelManager.getInstance()
                                .getBrandModel().getBrandList().get(k)
                                .getBrandName();
                        brandList.add(BrandName);

                        if (BrandName.equals(product_brand))
                            brandSelectedPos = k + 1;
                    }

                }
                CustomStyle.MySpinnerAdapter brandAdapter = new CustomStyle.MySpinnerAdapter(this,
                        android.R.layout.simple_spinner_item, brandList);
                brandAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (brandAdapter != null) {
                    brandSpinner.setAdapter(brandAdapter);
                    brandSpinner.setSelection(brandSelectedPos);
                    brandSpinner.setOnItemSelectedListener(this);
                }
            } else if (requestId == KEY_FETCH_CATEGORY) {

                ModelManager.getInstance()
                        .setFetchParentCategoryModel(response);
                ArrayList<String> categoryList = new ArrayList<String>();
                categoryList.add("Select category");
                if (ModelManager.getInstance().getFetchParentCategoryModel() != null
                        && ModelManager.getInstance()
                        .getFetchParentCategoryModel()
                        .getFetchParentCategoryList() != null
                        && ModelManager.getInstance()
                        .getFetchParentCategoryModel()
                        .getFetchParentCategoryList().size() > 0) {
                    for (int k = 0; k < ModelManager.getInstance()
                            .getFetchParentCategoryModel()
                            .getFetchParentCategoryList().size(); k++) {
                        String CategoryName = ModelManager.getInstance()
                                .getFetchParentCategoryModel()
                                .getFetchParentCategoryList().get(k)
                                .getCategoryName();
                        categoryList.add(CategoryName);
                        if (CategoryName != null
                                && CategoryName.equals(product_category))
                            categorySelectedPos = k + 1;
                    }
                }
                CustomStyle.MySpinnerAdapter categoryAdapter = new CustomStyle.MySpinnerAdapter(this,
                        android.R.layout.simple_spinner_item, categoryList);
                categoryAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (categoryAdapter != null) {
                    categorySpinner.setAdapter(categoryAdapter);
                    categorySpinner.setSelection(categorySelectedPos);
                    categorySpinner.setOnItemSelectedListener(this);
                }
            } else if (requestId == KEY_FETCH_UNIT) {
                ModelManager.getInstance().setFetchUnitModel(response);
                ArrayList<String> unitList = new ArrayList<String>();
                unitList.add("Select unit");
                if (ModelManager.getInstance().getFetchUnitModel() != null
                        && ModelManager.getInstance().getFetchUnitModel()
                        .getFetchUnitList() != null
                        && ModelManager.getInstance().getFetchUnitModel()
                        .getFetchUnitList().size() > 0) {
                    for (int k = 0; k < ModelManager.getInstance()
                            .getFetchUnitModel().getFetchUnitList().size(); k++) {
                        String UnitsName = ModelManager.getInstance()
                                .getFetchUnitModel().getFetchUnitList().get(k)
                                .getUnitName();
                        unitList.add(UnitsName);
                        if (UnitsName.equals(product_units))
                            unitsSelectedPos = k + 1;
                    }
                }
                CustomStyle.MySpinnerAdapter unitAdapter = new CustomStyle.MySpinnerAdapter(this,
                        android.R.layout.simple_spinner_item, unitList);
                unitAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (unitAdapter != null) {
                    unitSpinner.setAdapter(unitAdapter);
                    unitSpinner.setSelection(unitsSelectedPos);
                    unitSpinner.setOnItemSelectedListener(this);
                }
            }

        } else {
            showMessage("Please try again.");
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        hideLoading();
        showMessage("Please try again.");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub
        Spinner spinCategory = (Spinner) parent;
        Spinner spinBrand = (Spinner) parent;
        Spinner spinunit = (Spinner) parent;
        if (spinCategory.getId() == R.id.category_spinner) {
            selectedCategory = (String) spinCategory.getSelectedItem();
            categoryPos = position;
        }
        if (spinBrand.getId() == R.id.brand_spinner) {
            selectedBrand = (String) spinBrand.getSelectedItem();
            brandPos = position;
        }
        if (spinunit.getId() == R.id.unit_spinner) {
            units = (String) spinunit.getSelectedItem();
            unitPos = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    static Bitmap finalBitmap = null;

    // String setimege = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK
                && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath,
                    null, null, null);
            cursor.moveToFirst();
            imagePathh = cursor.getString(cursor.getColumnIndex(filePath[0]));
            startCropImage(new File(imagePathh));
            if (finalBitmap != null) {
                finalBitmap.recycle();
            }
            if (imagePathh.trim().length() != 0) {
                finalBitmap = Utils.decodeFile(imagePathh, 200, 200);
            }
            // setimege = imagePathh;
            Bitmap bp = BitmapFactory.decodeFile(imagePathh);
            bp = getRoundedCornerBitmap(bp, 90);
            if (bp == null) {
                bp = BitmapFactory.decodeResource(getResources(),
                        R.drawable.profile_propic);
            }
            productImagePic.setImageBitmap(bp);
            cursor.close();
        }
        if (REQUEST_CODE_CROP_IMAGE == requestCode) {
            if (data != null) {
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }
                Bitmap bp = BitmapFactory.decodeFile(path);
                bp = getRoundedCornerBitmap(bp, 90);
                if (bp == null) {
                    bp = BitmapFactory.decodeResource(getResources(),
                            R.drawable.profile_propic);
                }
                productImagePic.setImageBitmap(bp);
            } else {
                Toast.makeText(POSAddUpdateProduct.this,
                        "error in browsing image", Toast.LENGTH_LONG).show();
            }
            // bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
            // mImageView.setImageBitmap(bitmap);
        }

        if (requestCode == KEY_ADD_CATEGORY_IN_PRODUCT && resultCode == RESULT_OK) {
            fetchCategoryList();

        }
        if (requestCode == KEY_ADD_BRAND_IN_PRODUCT && resultCode == RESULT_OK) {
            fetchBrandList();
        }
    }

    private void startCropImage(File file) {

        Intent intent = new Intent(POSAddUpdateProduct.this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 2);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        if (bitmap != null) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            // final Rect rect = new Rect(0, 0, bitmap.getWidth(),
            // bitmap.getWidth());
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        }
        return null;
    }

    private void uploadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadImageToServer(imagePathh);
            }
        }).start();
    }

    String imagetoupload2 = "", ext = "", sendurlpath;

    private void loadImageToServer(String imagetoupload) {// /storage/sdcard0/Kitever/Media/Kitever
        // Images/Kitever_1472461770178.jpg
        try {
            Log.i("Imagepath", "image----" + imagetoupload);
            imagetoupload2 = imagetoupload.substring(imagetoupload
                    .lastIndexOf("/"));// /Kitever_1472461770178.jpg
            ext = imagetoupload2.substring(imagetoupload2.lastIndexOf(".")); // .jpg
            Log.i("imagetoupload2", "imagetoupload2 - " + imagetoupload2);
            Log.i("ext", "ext - " + ext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // sendurlpath = urlpath + "/" + UserId + imagetoupload2;//
        sendurlpath = "nowconnect.in" + "/" + getUserId() + imagetoupload2; // nowconnect.in/10016/Kitever_1472461770178.jpg

        Log.i("sendurlpath", "image----" + sendurlpath);

        String charset = "UTF-8";
        File uploadFile = new File(imagetoupload);// /storage/sdcard0/Kitever/Media/Kitever
        // Images/Kitever_1472461770178.jpg
        String requestURL = Apiurls.KIT19_BASE_URL;
        final String[] img = imagetoupload.split("/");// [, storage, sdcard0,
        // Kitever, Media,
        // Kitever Images,
        // Kitever_1472461770178.jpg]
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        // productImage = img[img.length - 1]; // Kitever_1472461770178.jpg
        productImage = currentTimeStamp + ext;
        Log.i("productImage", "- " + productImage);
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL,
                    charset);
            multipart.addHeaderField("user_id", getUserId());
            multipart.addFormField("Page", "UploadFile");
            multipart
                    .addFormField("fileName", getUserId() + "/" + productImage);
            multipart.addFilePart("file", uploadFile);
            List<String> response = multipart.finish();
            String res = response.get(0);
            final String[] splitRes = res.split(":");
            Log.i("splitRes", "splitRes - " + splitRes);
            runOnUiThread(new Runnable() {
                //
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    // secondBar.setVisibility(View.VISIBLE);
                    /*
                     * if (splitRes[1].equalsIgnoreCase("Success")) {
					 * Toast.makeText(getApplicationContext(), "Image uploaded",
					 * Toast.LENGTH_LONG).show(); } else {
					 * Toast.makeText(getApplicationContext(),
					 * "Image Not uploaded", Toast.LENGTH_LONG).show(); }
					 */
                    addUpdateProduct();
                }
            });

        } catch (IOException ex) {
            // system.err.println(ex);
        }
    }

    private class MultipartUtility {
        private final String boundary;
        private static final String LINE_FEED = "\r\n";
        private HttpURLConnection httpConn;
        private String charset;
        private OutputStream outputStream;
        private PrintWriter writer;

        /**
         * This constructor initializes a new HTTP POST request with content
         * type is set to multipart/form-data
         *
         * @param requestURL
         * @param charset
         * @throws IOException
         */
        public MultipartUtility(String requestURL, String charset)
                throws IOException {
            this.charset = charset;

            // creates a unique boundary based on time stamp
            boundary = "===" + System.currentTimeMillis() + "===";

            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream,
                    charset), true);
        }

        /**
         * Adds a form field to the request
         *
         * @param name  field name
         * @param value field value
         */
        public void addFormField(String name, String value) {
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append(
                    "Content-Disposition: form-data; name=\"" + name + "\"")
                    .append(LINE_FEED);
            writer.append("Content-Type: text/plain; charset=" + charset)
                    .append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.append(value).append(LINE_FEED);
            writer.flush();
        }

        /**
         * Adds a upload file section to the request
         *
         * @param fieldName  name attribute in <input type="file" name="..." />
         * @param uploadFile a File to be uploaded
         * @throws IOException
         */
        public void addFilePart(String fieldName, File uploadFile)
                throws IOException {
            String fileName = uploadFile.getName();
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append(
                    "Content-Disposition: form-data; name=\"" + fieldName
                            + "\"; filename=\"" + fileName + "\"").append(
                    LINE_FEED);
            writer.append(
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(fileName))
                    .append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary")
                    .append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();
        }

        /**
         * Adds a header field to the request.
         *
         * @param name  - name of the header field
         * @param value - value of the header field
         */
        public void addHeaderField(String name, String value) {
            writer.append(name + ": " + value).append(LINE_FEED);
            writer.flush();
        }

        /**
         * Completes the request and receives response from the server.
         *
         * @return a list of Strings as response in case the server returned
         * status OK, otherwise an exception is thrown.
         * @throws IOException
         */
        public List<String> finish() throws IOException {
            List<String> response = new ArrayList<String>();

            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();

            // checks server's status code first
            int status = httpConn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(httpConn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response.add(line);
                }
                reader.close();
                httpConn.disconnect();
            } else {
                throw new IOException("Server returned non-OK status: "
                        + status);
            }

            return response;
        }
    }
}
