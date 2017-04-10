package com.kitever.contacts;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.models.ContactInfo;

import java.util.ArrayList;
import java.util.List;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class ContactsAdapter extends BaseAdapter implements Filterable {

    private ArrayList<ContactInfo> originalArrayList = null;
    private ArrayList<ContactInfo> filteredArrayList = null;

    //private Context mContext;
    private LayoutInflater inflater;

    private AdapterInterface mClickListener;
    Context context;

    private ItemFilter mItemFilter = new ItemFilter();

    public ContactsAdapter(Context context, ArrayList<ContactInfo> list,
                           AdapterInterface clickListener) {
        super();

        //this.mContext = context;
        this.filteredArrayList = list;
        this.originalArrayList = list;
        this.context = context;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mClickListener = clickListener;

    }

    @Override
    public int getCount() {
        if (filteredArrayList != null) {
            return filteredArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return filteredArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView contact_name;
        TextView contact_email;
        TextView contact_phno;
        CheckBox check_box;
        ImageView editImageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_app2, null);

            holder.contact_name = (TextView) convertView
                    .findViewById(R.id.contact_name);
            holder.contact_email = (TextView) convertView
                    .findViewById(R.id.contact_email);
            holder.contact_phno = (TextView) convertView
                    .findViewById(R.id.contact_phno);
            holder.check_box = (CheckBox) convertView
                    .findViewById(R.id.check_box_contact);
            holder.editImageView = (ImageView) convertView.findViewById(R.id.editImageView);

            holder.contact_name.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            holder.contact_email.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            holder.contact_phno.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

            setRobotoThinFont(holder.contact_name, context);
            setRobotoThinFont(holder.contact_email, context);
            setRobotoThinFont(holder.contact_phno, context);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
            holder.check_box.setOnCheckedChangeListener(null);
        }

        if (filteredArrayList.get(position).getContact_email() != null && !filteredArrayList.get(position).getContact_email().equals("null"))
            holder.contact_email.setText(filteredArrayList.get(position).contact_email);
        else
            holder.contact_email.setText("");

        holder.contact_name.setText(filteredArrayList.get(position).Contact_Name);
        holder.contact_phno.setText(filteredArrayList.get(position).countryCode
                + filteredArrayList.get(position).Contact_Mobile);

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                setCheckedItem(holder.check_box, position);

            }
        });

        holder.check_box
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        filteredArrayList.get(position).isSelected = isChecked;

                        notifyDataSetChanged();
                        mClickListener.onItemClicked(position, 1);

                    }
                });

        holder.editImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mClickListener.onItemClicked(position, 2);

            }
        });

        holder.check_box.setChecked(filteredArrayList.get(position).isSelected);

        return convertView;
    }

    private void setCheckedItem(final CheckBox box, final int position) {
        filteredArrayList.get(position).isSelected = !box.isChecked();

        notifyDataSetChanged();
        mClickListener.onItemClicked(position, 1);
    }


    public ArrayList<ContactInfo> getFilteredArrayList() {
        return filteredArrayList;
    }

    public void setFilteredArrayList(ArrayList<ContactInfo> filteredArrayList) {
        this.filteredArrayList = filteredArrayList;
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return mItemFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<ContactInfo> list = originalArrayList;

            int count = list.size();
            final ArrayList<ContactInfo> nlist = new ArrayList<ContactInfo>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).Contact_Name + list.get(i).countryCode + list.get(i).Contact_Mobile;
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
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredArrayList = (ArrayList<ContactInfo>) results.values;
            notifyDataSetChanged();
            if (filteredArrayList.size() == 0) {
                mClickListener.onItemsEmpty();
            } else {
                mClickListener.onItemsAvailable();
            }
        }
    }
}



