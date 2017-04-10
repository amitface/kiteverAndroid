package com.kitever.contacts;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.models.ContactInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by android on 23/3/17.
 */

public class ContactLoaderAdapter extends RecyclerView.Adapter<ContactLoaderAdapter.MyViewHolder> implements Filterable {

    private ContactLoaderFragment context;
    private Cursor cursor;
    private List<ContactInfo> contactmodellist;
    private List<ContactInfo> originalArrayList;
    private AdapterInterface mClickListener;
    private ItemFilter mItemFilter;
    public ContactLoaderAdapter(ContactLoaderFragment context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        this.contactmodellist = getAllContact(cursor);
        this.originalArrayList = contactmodellist;
        mClickListener = (AdapterInterface) context;
        mItemFilter = new ItemFilter();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView contact_name, contact_email, contact_phno;
        CheckBox check_box_contact;
        ImageView editImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            contact_email = (TextView) itemView.findViewById(R.id.contact_email);
            contact_phno = (TextView) itemView.findViewById(R.id.contact_phno);

            check_box_contact = (CheckBox) itemView.findViewById(R.id.check_box_contact);
            editImageView = (ImageView) itemView.findViewById(R.id.editImageView);

            setRobotoThinFont(contact_name, context.getContext());
            setRobotoThinFont(contact_email, context.getContext());
            setRobotoThinFont(contact_phno, context.getContext());

            contact_name.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            contact_email.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            contact_phno.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_app2, parent, false);
        return new ContactLoaderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.contact_name.setText(contactmodellist.get(position).getContact_Name());
        holder.contact_email.setText(contactmodellist.get(position).getContact_email());
        holder.contact_phno.setText(contactmodellist.get(position).getContact_Mobile());
        holder.check_box_contact.setOnCheckedChangeListener(null);
        if (contactmodellist.get(position).isSelected())
            holder.check_box_contact.setChecked(true);
        else
            holder.check_box_contact.setChecked(false);

        holder.check_box_contact
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        contactmodellist.get(position).isSelected = isChecked;
                        for(int i=0;i<originalArrayList.size();i++)
                        {
                            if(contactmodellist.get(position).getContact_Mobile().equalsIgnoreCase(originalArrayList.get(i).getContact_Mobile()))
                            {
                                originalArrayList.get(i).setSelected(isChecked);
                                break;
                            }
                        }
                        notifyItemChanged(position);
                        mClickListener.onItemClicked(position, 1);
                    }
                });

        holder.editImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mClickListener.onItemClicked(position, 2);

            }
        });

        holder.check_box_contact.setChecked(contactmodellist.get(position).isSelected);
    }

    public void deleteSelected() {
        Iterator<ContactInfo> iterator = originalArrayList.iterator();
        while(iterator.hasNext())
        {
                if(iterator.next().isSelected())
                    iterator.remove();
        }
        contactmodellist = null;
        contactmodellist = originalArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (contactmodellist != null)
            return contactmodellist.size();
        else
            return 0;
    }

    public List<ContactInfo> getFilteredArrayList() {
        return contactmodellist;
    }

    private ArrayList<ContactInfo> getAllContact(Cursor cursor) {
        // TODO Auto-generated method stub

        ArrayList<ContactInfo> arrayListOfContacts = new ArrayList<>();
        try {

            while (cursor.moveToNext()) {
                ContactInfo model = new ContactInfo();
                model.setContact_Name(cursor.getString(cursor
                        .getColumnIndex("display_name")));

                model.setContact_Mobile(cursor.getString(cursor
                        .getColumnIndex("phonenumber")));

                model.setSelected(false);
                model.setContact_email(cursor.getString(cursor.getColumnIndex("contact_email")));

                String avatarName = cursor.getString(cursor
                        .getColumnIndex("profilepic_name"));
                byte[] pic = cursor.getBlob(cursor
                        .getColumnIndex("profilepic_data"));

                model.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                model.setContact_Anniversary(cursor.getString(cursor.getColumnIndex("contactAnniversary")));
                model.setCountryCode(cursor.getString(cursor.getColumnIndex("countryCode")));
                model.setContact_DOB(cursor.getString(cursor.getColumnIndex("contactDOB")));
                model.setCompanyName(cursor.getString(cursor.getColumnIndex("companyName")));
                model.setContactNumber(cursor.getString(cursor.getColumnIndex("contactNumber")));
                model.setState(cursor.getString(cursor.getColumnIndex("stateId")));
                model.setCity(cursor.getString(cursor.getColumnIndex("cityId")));
                model.setContact_id(cursor.getString(cursor.getColumnIndex("contact_id")));

                /*if (pic != null) {
                    model.setAvatar(pic);
                }
                model.setImageUrl(avatarName);*/


                arrayListOfContacts.add(model);
            }
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            e.printStackTrace();
        }
        return arrayListOfContacts;
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
            contactmodellist = (ArrayList<ContactInfo>) results.values;
            notifyDataSetChanged();
            if (contactmodellist.size() == 0) {
                mClickListener.onItemsEmpty();
            } else {
                mClickListener.onItemsAvailable();
            }
        }
    }
}
