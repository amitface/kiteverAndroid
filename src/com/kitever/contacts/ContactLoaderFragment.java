package com.kitever.contacts;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitever.android.BuildConfig;
import com.kitever.android.R;
import com.kitever.models.ContactInfo;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactLoaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactLoaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerContactLoader;
    private ContactLoaderAdapter contactLoaderAdapter;

    public ContactLoaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactLoaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactLoaderFragment newInstance(String param1, String param2) {
        ContactLoaderFragment fragment = new ContactLoaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contact_loader, container, false);
        recyclerContactLoader = (RecyclerView) view.findViewById(R.id.recyclerContactLoader);

        RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager(getActivity());
        recyclerContactLoader.setLayoutManager(layoutManager);
        recyclerContactLoader.setItemAnimator(new DefaultItemAnimator());


        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String projections [] = {"contact_id", "display_name", "phonenumber", "status",
                            "profilepic_name", "profilepic_data", "contact_email","contactDOB",
                            "contactAnniversary", "address", "stateId", "cityId", "companyName",
                            "countryCode", "contactNumber"};
        String selection = "isgroup=? AND contact_id IS NOT ?";
        String selectionArgs [] = {"0","null"};
        return new CursorLoader(getContext(), Uri.parse("content://"+ BuildConfig.APPLICATION_ID+"/Contacts"),projections,selection,selectionArgs,"display_name COLLATE NOCASE ASC");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 301) {

            if (resultCode == RESULT_OK) {
                getLoaderManager().restartLoader(0, null, this);
            }
        }
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        contactLoaderAdapter = new ContactLoaderAdapter(ContactLoaderFragment.this,data);
        recyclerContactLoader.setAdapter(contactLoaderAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        return;
    }

    @Override
    public void onItemClicked(int position, int forWhat) {
        // TODO Auto-generated method stub
        if (forWhat == 1) {

            if (((ContactsActivity) getActivity()).currentSelectedIndex == 0) {

                final ArrayList<ContactInfo> arrayList = (ArrayList<ContactInfo>) contactLoaderAdapter.getFilteredArrayList();
                int selected = 0;
                for (ContactInfo contactInfo : arrayList) {
                    if (contactInfo.isSelected) {
                        selected++;
                    }
                }
                ((ContactsActivity) getActivity()).updateMenuTitles(selected);
            }
        } else if (forWhat == 2) {

            if (getActivity() != null) {
                final List<ContactInfo> arrayList = contactLoaderAdapter
                        .getFilteredArrayList();
                if (arrayList != null) {
                    Intent intent = new Intent(getActivity(),
                            sms19.listview.newproject.ContactAdd.class);
                    intent.putExtra("froninapp", false);
                    intent.putExtra("EDIT_CONTACT", arrayList.get(position));
                    startActivityForResult(intent, 301);
                }
            }
        }
    }

    @Override
    public void onItemsEmpty() {
        // TODO Auto-generated method stub
        /*textView.setVisibility(View.VISIBLE);
        textView.setText("No contact result to display");*/
    }

    @Override
    public void onItemsAvailable() {
        // TODO Auto-generated method stub
        /*textView.setVisibility(View.GONE);
        textView.setText("");*/
    }

    public void refreshContact()
    {
        getLoaderManager().restartLoader(0, null, this);
    }

    public ContactLoaderAdapter getAdapter() {
        return  contactLoaderAdapter;
    }

    public void deleteSelected() {
        contactLoaderAdapter.deleteSelected();
    }

}
