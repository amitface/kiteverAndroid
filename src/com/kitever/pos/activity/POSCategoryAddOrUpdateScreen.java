package com.kitever.pos.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.model.data.FetchParentCategoryModelData;
import com.kitever.pos.model.manager.ModelManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;

public class POSCategoryAddOrUpdateScreen extends PosBaseActivity implements
        NetworkManager, OnClickListener {

    private String screenName;
    private EditText categoryNameEdit, descriptionNameEdit;
    private Spinner parentCategorySpinner, typeCategorySpinner;
    private final int KEY_UPDATE_ADD_CATEGORY = 1;
    private final int KEY_FETCH_PARENT_CATEGORY = 2;
    private final int KEY_DELETE_CATEGORY = 3;
    private String selectedType, selectedCategory;
    private String updateCategoryNameVal, updateParentCategory = "none", is_active = "A",
            updateSelectType = "", updateDescription, updateCategoryId = "", parentCategoryId = "";
    int parent_category_position = 0;
    int type_position = 0;
    boolean isFromProduct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        screenName = intent.getStringExtra("screen_name");
        isFromProduct = intent.getBooleanExtra("isFromProduct", false);
        if (screenName != null
                && screenName.equalsIgnoreCase("Update Category")) {
            updateCategoryNameVal = intent.getStringExtra("category_name");
            updateParentCategory = intent.getStringExtra("parent_category");
            updateSelectType = intent.getStringExtra("select_type");
            updateDescription = intent.getStringExtra("description");
            updateCategoryId = intent.getStringExtra("category_id");
            is_active = intent.getStringExtra("is_active");
        }
        setScreenName(screenName);
        setBottomAction(true, true, true, true, true, true, true, true, true,
                true, true, true);
        setLayoutContentView(R.layout.pos_updatecategory_layout);
        setScreen();
        fetchParentCategory();
    }

    private void setScreen() {
        categoryNameEdit = (EditText) findViewById(R.id.category_name);
        descriptionNameEdit = (EditText) findViewById(R.id.description_name);
        parentCategorySpinner = (Spinner) findViewById(R.id.parent_category);
        typeCategorySpinner = (Spinner) findViewById(R.id.type_category);
        Button addUpdateBtn = (Button) findViewById(R.id.btn_update_add_category);

        RelativeLayout page_main_layout=(RelativeLayout)findViewById(R.id.page_main_layout);
        CardView card_view=(CardView)findViewById(R.id.card_view);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));
        card_view.setBackgroundColor(Color.parseColor(CustomStyle.PAGE_BACKGROUND));

        setRobotoThinFont(categoryNameEdit,this);
        setRobotoThinFont(descriptionNameEdit,this);
        setRobotoThinFontButton(addUpdateBtn,this);

        categoryNameEdit.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        descriptionNameEdit.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        addUpdateBtn.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        addUpdateBtn.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        addUpdateBtn.setOnClickListener(this);
        if (screenName != null) {
            if (screenName.equalsIgnoreCase("Add Category")) {
                addUpdateBtn.setText("Add");
            } else {
                addUpdateBtn.setText("Update");
                if (updateCategoryNameVal != null) {
                    categoryNameEdit.setText(updateCategoryNameVal);
                }
                if (descriptionNameEdit != null) {
                    descriptionNameEdit.setText(updateDescription);
                }

            }
        }
        selectTypeList(updateSelectType);
    }

    private void selectTypeList(String str) {

        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("Select type");
        typeList.add("Goods");
        typeList.add("Services");
        typeList.add("Goods & Services");

        if (typeList.contains(str))
            type_position = typeList.indexOf(str);
        CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeCategorySpinner.setAdapter(typeAdapter);
        typeCategorySpinner.setSelection(type_position);
        typeCategorySpinner.setOnItemSelectedListener(itemSelectedListener);
    }

    OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            // TODO Auto-generated method stub
            Spinner spinCategory = (Spinner) parent;
            Spinner spinType = (Spinner) parent;
            if (spinCategory.getId() == R.id.parent_category) {
                selectedCategory = (String) spinCategory.getSelectedItem();
                if (position > 0) {

                    parentCategoryId = ModelManager.getInstance().getFetchParentCategoryModel().getFetchParentCategoryList().get(position - 1).getCategoryID();
                    String typestr = ModelManager.getInstance().getFetchParentCategoryModel().getFetchParentCategoryList().get(position - 1).getType();
                    selectTypeList(typestr);
                    typeCategorySpinner.setEnabled(false);

                } else {
                    parentCategoryId = "";
                    typeCategorySpinner.setEnabled(true);

                }


            }
            if (spinType.getId() == R.id.type_category) {
                selectedType = (String) spinType.getSelectedItem();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (screenName.equalsIgnoreCase("Update Category")) {
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
                        .setMessage(getString(R.string.delete_category_alert))
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                        deleteCategory();
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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        if (v.getId() == R.id.btn_update_add_category) {
            if (validation())
                insertUpdateCategory();
        }
    }

    private void deleteCategory() {
        if (Utils.isDeviceOnline(this)) {
            if (screenName.equalsIgnoreCase("Update Category")) {
                Map map = new HashMap<>();
                map.put("Page", "DeleteCategory");
                map.put("CategoryID", updateCategoryId);
                map.put("IsActive", is_active);
                map.put("userID", getUserId());
                map.put("UserLogin", getUserLogin());
                map.put("Password", getPassWord());


                Log.i("CAtMAp", "map - " + map.toString());
                try {
                    new RequestManager().sendPostRequest(POSCategoryAddOrUpdateScreen.this,
                            KEY_DELETE_CATEGORY, map);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } else {
            showMessage("Internet connection not found");
        }
    }

    private boolean validation() {
        if (categoryNameEdit.getText().toString() == null
                || categoryNameEdit.getText().toString() == ""
                || categoryNameEdit.getText().toString().isEmpty()) {
            showMessage("Please enter category name");
            return false;
        }
        if (selectedType == null
                || selectedType.equalsIgnoreCase("Select type")
                || selectedType.isEmpty()) {
            showMessage("Please Select Type");
            return false;
        }
        return true;
    }

    private void fetchParentCategory() {
        if (Utils.isDeviceOnline(this)) {
            showLoading();
            try {
                Map map = new HashMap<String, String>();
                map.put("Page", "FetchparentCategoryDetail");
                map.put("userID", getUserId());
                map.put("CategoryID", updateCategoryId);
                new RequestManager().sendPostRequest(
                        POSCategoryAddOrUpdateScreen.this,
                        KEY_FETCH_PARENT_CATEGORY, map);
            } catch (Exception e) {
                // TODO: handle exception
                hideLoading();
                showMessage("Please try again.");
            }
        }
    }

    private void insertUpdateCategory() {
        if (Utils.isDeviceOnline(this)) {
            showLoading();
            String categoryName = categoryNameEdit.getText().toString();
            String description = descriptionNameEdit.getText().toString();
            if (selectedCategory == null
                    || selectedCategory
                    .equalsIgnoreCase("Select parent category")) {
                selectedCategory = "";
            }
            if (selectedType == null
                    || selectedType.equalsIgnoreCase("Select type")) {
                selectedType = "";
            }
            Map map = new HashMap<String, String>();
            if (screenName.equalsIgnoreCase("Update Category")) {
                map.put("Page", "UpdateCategory");
                map.put("CategoryID", updateCategoryId);
                map.put("IsActive", is_active);
            } else {
                map.put("Page", "InsertCategory");
                map.put("CategoryCode", "");
                map.put("IsActive", is_active);
            }

            map.put("userID", getUserId());
            map.put("UserLogin", getUserLogin());
            map.put("Password", getPassWord());
            map.put("ParentCategory", parentCategoryId);
            map.put("CategoryName", categoryName);
            map.put("Type", selectedType);
            map.put("Description", description);

            Log.i("PostCategory", "data-" + map.toString());
            try {
                new RequestManager().sendPostRequest(
                        POSCategoryAddOrUpdateScreen.this,
                        KEY_UPDATE_ADD_CATEGORY, map);
            } catch (Exception e) {
                // TODO: handle exception
                hideLoading();
                showMessage("Please try again.");
            }
        } else {
            showMessage("Internet connection not found.");
        }

    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_UPDATE_ADD_CATEGORY) {
                try {
                    ModelManager.getInstance().setCategoryModel(response);
                    if (ModelManager.getInstance().getCategoryModel() != null
                            && ModelManager.getInstance().getCategoryModel()
                            .getMessage() != null
                            && ModelManager.getInstance().getCategoryModel()
                            .getMessage().length() > 0) {
                        if (ModelManager.getInstance().getCategoryModel().getStatus().equals("true")) {
                            Toast.makeText(getApplicationContext(), ModelManager.getInstance().getCategoryModel()
                                    .getMessage(), Toast.LENGTH_LONG).show();
                            Intent res = new Intent();
                            res.putExtra("response", response);
                            setResult(RESULT_OK, res);
                            finish();
                        } else
                            showMessage(ModelManager.getInstance().getCategoryModel().getMessage());

                    } else {
                        showMessage("Please try again.");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            } else if (requestId == KEY_FETCH_PARENT_CATEGORY) {
                ArrayList<String> list = new ArrayList<String>();
                list.add("Select parent category");
                ModelManager.getInstance()
                        .setFetchParentCategoryModel(response);
                if (ModelManager.getInstance().getFetchParentCategoryModel() != null
                        && ModelManager.getInstance()
                        .getFetchParentCategoryModel()
                        .getFetchParentCategoryList() != null
                        && ModelManager.getInstance()
                        .getFetchParentCategoryModel()
                        .getFetchParentCategoryList().size() > 0) {

                    ArrayList<FetchParentCategoryModelData> listModel = ModelManager
                            .getInstance().getFetchParentCategoryModel()
                            .getFetchParentCategoryList();
                    for (int k = 0; k < listModel.size(); k++) {
                        list.add(listModel.get(k).getCategoryName());

                        String catid = listModel.get(k).getCategoryID();
                        if (catid != null && catid.equals(updateParentCategory))
                            parent_category_position = k + 1;
                    }

                    CustomStyle.MySpinnerAdapter typeAdapter = new CustomStyle.MySpinnerAdapter(
                            this, android.R.layout.simple_spinner_item, list);
                    typeAdapter
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    parentCategorySpinner.setAdapter(typeAdapter);
                    parentCategorySpinner.setSelection(parent_category_position);
                    parentCategorySpinner
                            .setOnItemSelectedListener(itemSelectedListener);

                }

            } else if (requestId == KEY_DELETE_CATEGORY) {
                Log.i("response", "response-" + response);
                ModelManager.getInstance().setCategoryModel(response);
                if (ModelManager.getInstance().getCategoryModel() != null
                        && ModelManager.getInstance().getCategoryModel()
                        .getMessage() != null
                        && ModelManager.getInstance().getCategoryModel()
                        .getMessage().length() > 0) {
                    Toast.makeText(POSCategoryAddOrUpdateScreen.this, ModelManager.getInstance().getCategoryModel()
                            .getMessage(), Toast.LENGTH_LONG).show();
                    Intent res = new Intent();
                    res.putExtra("response", response);
                    setResult(RESULT_OK, res);
                    finish();

                } else {
                    showMessage("Please try again.");
                }

            }
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        hideLoading();
        showMessage("Please try again.");
    }

}
