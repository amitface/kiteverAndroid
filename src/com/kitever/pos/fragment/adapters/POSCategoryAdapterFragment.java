package com.kitever.pos.fragment.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.pos.activity.POSCategoryAddOrUpdateScreen;
import com.kitever.pos.fragment.POSCategoryScreenFragment;
import com.kitever.pos.model.data.PosCategoryModelData;
import com.kitever.utils.TotalRows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static sms19.inapp.msg.constant.Utils.setdosisregularFont;
import static sms19.inapp.msg.constant.Utils.setmyriadproFont;

/**
 * Created by dev on 17/1/17.
 */


public class POSCategoryAdapterFragment extends BaseAdapter implements Filterable {

    private ArrayList<PosCategoryModelData> modelList;
    private POSCategoryScreenFragment context;
    private POSCategoryAdapterFragment.ItemFilter mItemFilter = new POSCategoryAdapterFragment.ItemFilter();
    private ArrayList<String> parentCategoryList;
    private String selectedItemType;
    private ArrayList<PosCategoryModelData> filteredArrayList = null;
    private POSCategoryScreenFragment categoryInstance;
    private String userId, password, userLogin;
    private TotalRows rows;
    private static final int ADD_CATEGORY = 2;
    MoonIcon mIcon;
    public  String isActiveString="";

    public POSCategoryAdapterFragment(POSCategoryScreenFragment categoryInstance,
                              ArrayList<PosCategoryModelData> list, String userId,
                              String password, String userLogin) {
        // TODO Auto-generated constructor stub
        this.modelList = list;
        this.userId = userId;
        this.password = password;
        this.userLogin = userLogin;
        filteredArrayList = this.modelList;
        this.categoryInstance = categoryInstance;
        this.context = categoryInstance;
        parentCategoryList = new ArrayList<String>();
        parentCategoryList.add("Select parent category");
        for (int k = 0; k < list.size(); k++) {
            if (list.get(k).getParentCategory() != null
                    && !list.get(k).getParentCategory().equalsIgnoreCase(""))
                parentCategoryList.add(list.get(k).getParentCategory());
        }
        rows = (TotalRows) context;
        mIcon=new MoonIcon(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        rows.totalRows(""+Integer.toString(filteredArrayList.size()));
        return filteredArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        POSCategoryAdapterFragment.ViewHolder holder;
        if (convertView == null) {
            holder = new POSCategoryAdapterFragment.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(
                    R.layout.pos_category_adapter_layout, parent, false);
            holder.layout=(LinearLayout) convertView
                    .findViewById(R.id.layout);
            holder.categoryName = (TextView) convertView
                    .findViewById(R.id.cat_name_val);
            holder.parentCategory = (TextView) convertView
                    .findViewById(R.id.parent_cat_val);
            holder.type = (TextView) convertView.findViewById(R.id.type_val);
            holder.categoryActivate=(ImageView)convertView.findViewById(R.id.activate_category);

            setRobotoThinFont(holder.categoryName,context.getActivity());
            setRobotoThinFont(holder.parentCategory,context.getActivity());
            setRobotoThinFont(holder.type,context.getActivity());

            holder.categoryName.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.parentCategory.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));
            holder.type.setTextColor(Color.parseColor(CustomStyle.LISTVIEW_FONT_COLOR));

            convertView.setTag(holder);
        } else {
            holder = (POSCategoryAdapterFragment.ViewHolder) convertView.getTag();
        }
        final PosCategoryModelData object = filteredArrayList.get(position);

        if (object.getIsActive().equals("A")) {
            //holder.layout.setBackgroundResource(R.drawable.border_black);
            holder.layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_ENABLED));
            holder.categoryActivate.setImageResource(R.drawable.switch_on);
            holder.layout.setClickable(true);

        } else {
            //holder.layout.setBackgroundResource(R.drawable.border_grey);
            holder.layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND_DISABLED));
            holder.categoryActivate.setImageResource(R.drawable.switch_off);
            holder.layout.setClickable(false);

        }
        holder.categoryName.setText(object.getCategoryName());
        String parentCatStr=object.getParentCategory();
        if(parentCatStr.equals(object.getCategoryName()) || parentCatStr.length()==0) holder.parentCategory.setText("None");
        else
            holder.parentCategory.setText(parentCatStr.replaceFirst(object.getCategoryName()+"<",""));


        holder.type.setText(object.getType());
        // action
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context.getActivity(),
                        POSCategoryAddOrUpdateScreen.class);
                intent.putExtra("screen_name", "Update Category");
                intent.putExtra("category_name", object.getCategoryName());
                intent.putExtra("parent_category", object.getParentCategoryID());
                intent.putExtra("select_type", object.getType());
                intent.putExtra("description", object.getDescription());
                intent.putExtra("category_id", object.getCategoryID());
                intent.putExtra("is_active", object.getIsActive());
                intent.putStringArrayListExtra("parent_category_list",
                        parentCategoryList);
                context.startActivityForResult(intent,ADD_CATEGORY);
            }
        });

        holder.categoryActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String catStatus = object.getIsActive();
                final String isActive = object.getIsActive().equals("A") ? "I" : "A";
                isActiveString = object.getIsActive().equals("A") ? "Deactivated" : "Activated";

                String Activemsg="All the categories and products under "+object.getCategoryName() +" category also be deactivated. Do you want to deactivate it ?";
                String Inactivemsg="Do You Want to Activate "+object.getCategoryName();
                String msg=object.getIsActive().equals("A") ? Activemsg: Inactivemsg;

                new AlertDialog.Builder(categoryInstance.getActivity())
                        .setCancelable(false)
                        .setMessage(msg)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                       // object.setIsActive(isActive);
                                        Map map = new HashMap<>();
                                        map.put("Page", "SetStatus");
                                        map.put("id", object.getCategoryID());
                                        map.put("Status", catStatus);
                                        map.put("PageType", "Category");
                                        map.put("UserID", userId);
                                        map.put("UserLogin", userLogin);
                                        map.put("Password", password);
                                        categoryInstance.activateCategory(map);
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();

                    }
                }).show();


            }
        });


     /*   holder.deleteCategory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new AlertDialog.Builder(context.getActivity())
                        .setCancelable(false)
                        .setMessage("Do you want to delete it?")
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                        Map map = new HashMap<>();
                                        map.put("Page", "DeleteCategory");
                                        map.put("CategoryID",
                                                object.getCategoryID());
                                        map.put("IsActive", object.getIsActive());
                                        map.put("userID", userId);
                                        map.put("UserLogin", userLogin);
                                        map.put("Password", password);
                                        categoryInstance.deleteCategory(map);
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
            }
        });*/
        return convertView;
    }
    public  void toastmsg()
    {
        Toast.makeText(categoryInstance.getActivity(), "Category " + isActiveString, Toast.LENGTH_SHORT).show();
    }

    private static class ViewHolder {
        TextView categoryName, parentCategory,type,editCategory,deleteCategory,activateCategory;
        LinearLayout layout;
        ImageView categoryActivate;
    }

    public void setSelectedItemType(String itemType) {
        selectedItemType = itemType;
    }

    public void setSelectedActiveInactive(String itemType) {
        selectedItemType = itemType;
    }



    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            List<PosCategoryModelData> list = modelList;

            int count = list.size();

           /* if(filterString.length()==0 || selectedItemType.equals("Select") || selectedItemType.equals(""))
            {
                results.values = list;
                results.count = list.size();
                return results;
            }*/
            final ArrayList<PosCategoryModelData> nlist = new ArrayList<PosCategoryModelData>(
                    count);
            String filterableString = "";

            for (int i = 0; i < count; i++) {

                if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Category")) {
                    filterableString = list.get(i).getCategoryName();
                } else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Parent")) {
                    if(list.get(i).getParentCategory().equals(list.get(i).getCategoryName()))	filterableString = "None";
                    else filterableString = list.get(i).getParentCategory();
                } else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Type")) {
                    filterableString = list.get(i).getType();
                }	else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Active")) {
                    filterableString = "A";
                }	else if (selectedItemType != null
                        && selectedItemType.equalsIgnoreCase("Inactive")) {
                    filterableString = list.get(i).getIsActive();
                }
                else
                {
                    filterableString = list.get(i).getCategoryName();
                }

                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }



            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            filteredArrayList = (ArrayList<PosCategoryModelData>) results.values;
            if (filteredArrayList !=null)
                notifyDataSetChanged();
            else
                filteredArrayList=new ArrayList<PosCategoryModelData>();

        }

    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return mItemFilter;
    }

    public interface Actionable {
        void deleteCategory(Map map);
        void activateCategory(Map map);
    }

    public void setModelData(ArrayList<PosCategoryModelData> modelList) {
        if(modelList==null){
            modelList=new ArrayList<PosCategoryModelData>();
        }
        filteredArrayList = modelList;
    }

    public void changeList(ArrayList<PosCategoryModelData> category) {
        // TODO Auto-generated method stub
        filteredArrayList = null;
        filteredArrayList = category;
        notifyDataSetChanged();
    }
    public ArrayList<PosCategoryModelData> getList()
    {
        return  filteredArrayList;
    }
}