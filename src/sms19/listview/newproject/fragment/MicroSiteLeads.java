package sms19.listview.newproject.fragment;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.PermissionClass;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.adapter.MircoSiteLeadsAdapter;
import sms19.listview.newproject.ContactAdd;
import sms19.listview.newproject.model.ClientLeadDetails;
import sms19.listview.newproject.model.ClientLeadDetailsModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MicroSiteLeads#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MicroSiteLeads extends Fragment implements View.OnClickListener, NetworkManager {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private TextView tvPagename;
    private MircoSiteLeadsAdapter mAdapter;
    private ArrayList<ClientLeadDetails> micrositeDetailses;
    private ProgressBar progressBar;
    private ImageView imgDownloadMicroSiteLeads;
    private final int FETCH_CLIENT_LEAD_DETAIL = 1;

    public MicroSiteLeads() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MicroSiteLeads.
     */
    // TODO: Rename and change types and number of parameters
    public static MicroSiteLeads newInstance(String param1, String param2) {
        MicroSiteLeads fragment = new MicroSiteLeads();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = new ProgressBar(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_micro_site_leads, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerMicroSiteLeads);
        tvPagename = (TextView) view.findViewById(R.id.tvPageNameMicroSiteLeads);
        imgDownloadMicroSiteLeads = (ImageView) view.findViewById(R.id.imgDownloadMicroSiteLeads);
        imgDownloadMicroSiteLeads.setOnClickListener(this);
        micrositeDetailses = new ArrayList<ClientLeadDetails>();
        mAdapter = new MircoSiteLeadsAdapter(micrositeDetailses);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new MicroSiteLeads.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                addContact(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }

    private void addContact(final int position) {


        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setMessage("Are you sure? You want to add " + micrositeDetailses.get(position).getMobile() + " to your contact")
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                Intent intent = new Intent(getActivity(),ContactAdd.class);
                                intent.putExtra("leads",micrositeDetailses.get(position));
                                intent.putExtra("froninapp", true);
                                startActivityForResult(intent,1);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        }).show();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        requestFetchClientMicroSiteDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgDownloadMicroSiteLeads:
                if (new PermissionClass(getActivity()).checkPermissionForExternalStorage())
                    createCSV();
                else
                    new PermissionClass(getActivity()).requestPermissionForExternalStorage();
                break;
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (response != null && response.length() > 0) {


            if (requestId == FETCH_CLIENT_LEAD_DETAIL) {
                Gson gson = new Gson();
                ClientLeadDetailsModel clientLeadDetailsModel = gson.fromJson(response, ClientLeadDetailsModel.class);
                if (clientLeadDetailsModel.getClientLeadDetails() != null && clientLeadDetailsModel.getClientLeadDetails().size() > 0) {
                    ArrayList<ClientLeadDetails> clientLeadDetails = clientLeadDetailsModel.getClientLeadDetails();
                    for (ClientLeadDetails temp : clientLeadDetails)
                        micrositeDetailses.add(temp);
                    mAdapter.notifyDataSetChanged();
                    tvPagename.setText("Page Name : " + Utils.getBaseUrlValue(getActivity()).replace("NewService.aspx?Page=", "") + clientLeadDetails.get(0).getCname());
                } else
                    Toast.makeText(getActivity(), "Please create microsite first.", Toast.LENGTH_LONG).show();
            }
        } else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
    }

    private void requestFetchClientMicroSiteDetail() {
        if (Utils.isDeviceOnline(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            Map map = new HashMap<>();
            map.put("Page", "GetClientLeadDetails");
            map.put("Userid", Utils.getUserId(getActivity()));

            try {
                new RequestManager().sendPostRequest(this,
                        FETCH_CLIENT_LEAD_DETAIL, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MicroSiteLeads.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MicroSiteLeads.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void createCSV() {
        {
            String text = "";
            if (micrositeDetailses != null) {
                for (int i = 0; i < micrositeDetailses.size(); i++) {

                    try {

                        String pattern1 = "d-MM-yyyy";
                        String s = "";
                        if (micrositeDetailses.get(i).getDate() != null && !micrositeDetailses.get(i).getDate().equals("")) {
                            SimpleDateFormat format = new SimpleDateFormat(pattern1);
                            try {
                                Date date = new Date(micrositeDetailses.get(i).getDate());
                                format = new SimpleDateFormat(pattern1);
                                s = format.format(date);
                            } catch (Exception c) {
                                s = "";
                            }
                        } else
                            s = "";
                        text = text + "Date : " + s + ",";

                        text = text + "Source : "
                                + micrositeDetailses.get(i).getMobile().trim() + ",";

                        text = text + "Email : "
                                + micrositeDetailses.get(i).getEmail().trim() + ",";
                        text = text + "Moblie : " + micrositeDetailses.get(i).getMobile().trim() + ",";
                        text = text + "Name : " + micrositeDetailses.get(i).getFirstName().trim() + " " + micrositeDetailses.get(i).getFirstName().trim() + ",";

                        text = text + "Address : "
                                + micrositeDetailses.get(i).getAddress().trim() + ",";

                        text = text + "City : " + micrositeDetailses.get(i).getCity().trim() + ",";
                        text = text + "Zip : " + micrositeDetailses.get(i).getZip().trim() + ",";
                        text = text + "Remarks : " + micrositeDetailses.get(i).getZip().trim() + "\n";

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                text = "No details available";
            }

            String path = Environment.getExternalStorageDirectory().getPath();
            long current_time = System.currentTimeMillis();
            File dir = new File(path + "/Kitever/doc/");
            if (!dir.exists())
                dir.mkdirs();


            File file = new File(dir + "/MicroSiteDetailReport" + current_time + ".txt");
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file);
                fOut.write(text.getBytes());
                fOut.close();
                openCSV(file);
                Toast.makeText(getActivity(), "MicroSiteDetailReport" + current_time + ".txt" + " saved in kitever folder", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openCSV(final File file) {
        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setMessage("CSV file saved successfully in " + file.toString() + " do you want to open it? ")
                .setPositiveButton("Open now",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                Uri pfdfpath = Uri.fromFile(file);
                                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                pdfOpenintent.setDataAndType(pfdfpath, "text/plain");
                                try {
                                    startActivity(pdfOpenintent);
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        }).show();
    }

}
   /* private void requestAddContact(int position) {
        if (Utils.isDeviceOnline(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            Map map = new HashMap<>();
            map.put("Page", "InsertIndividualContact");
            map.put("UserID", Utils.getUserId(getActivity()));

            if (micrositeDetailses.get(position).getEmail() != null && isValidEmailID(micrositeDetailses.get(position).getEmail()))
                map.put("EmailId", micrositeDetailses.get(position).getEmail());
            else
                map.put("EmailId", "");
            *//*map.put("Userid", Userid);*//*
            map.put("contactName", micrositeDetailses.get(position).getFirstName() + " " + micrositeDetailses.get(position).getLastName());
            map.put("contactMobile", micrositeDetailses.get(position).getMobile());

            map.put("contactDOB", "");
            map.put("contactAnniversary", "");
            map.put("countryCode", "+91");

            try {
                lastPosition = position;
                new RequestManager().sendPostRequest(this,
                        ADD_CONTACT_FROM_LEAD, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate(int position) {
        if (isValidPhoneNumber(micrositeDetailses.get(position).getMobile())) {
            // boolean status =
            // validateUsing_libphonenumber(countryCode,
            // phoneNumber);
            *//*boolean status = parseContact(micrositeDetailses.get(position).getMobile(),
                    "+91");

            if (!status) {
                isWrongInput = true;
                editNumber.setError("Invalid phone");
            } else {
                isWrongInput = false;
                editNumber.setError(null);
            }*//*
        }
        return false;
    }
     private boolean isValidEmailID(CharSequence emailid) {

        if (!TextUtils.isEmpty(emailid)) {
            return Patterns.EMAIL_ADDRESS.matcher(emailid).matches();
        }

        return true;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {

        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }

        return false;
    }

    private boolean parseContact(String contact, String countrycode) {
        if (countrycode != null && countrycode.length() > 0) {
            try {
                countrycode = countrycode.substring(1, countrycode.length());
            } catch (Exception e) {
                // TODO: handle exception
                countrycode = countrycode
                        .substring(1, countrycode.length() - 1);
            }

        }

        Phonenumber.PhoneNumber phoneNumber = null;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer
                .parseInt(countrycode));
        boolean isValid = false;
        PhoneNumberUtil.PhoneNumberType isMobile = null;
        try {
            phoneNumber = phoneNumberUtil.parse(contact, isoCode);
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            isMobile = phoneNumberUtil.getNumberType(phoneNumber);

        } catch (NumberParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        boolean isValid2 = isValid
                && (PhoneNumberUtil.PhoneNumberType.MOBILE == isMobile || PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE == isMobile);

        if (isValid2) {
            Utils.printLog2("isValid2= " + isValid2);
            return true;
        }
        return false;
    }

    else if (requestId == ADD_CONTACT_FROM_LEAD) {
                if (response != null && response.length() > 0) {


                    Gson gson = new Gson();
                    ContactRegistrationModel contactRegistrationModel = gson.fromJson(response, ContactRegistrationModel.class);

                    if (contactRegistrationModel != null ) {

                        try {
                            if (contactRegistrationModel.getContactRegistration() != null && contactRegistrationModel.getContactRegistration().get(0).getError() == null) {

                                String msg = contactRegistrationModel.getContactRegistration().get(0).getMsg();

                                Toast.makeText(
                                        getActivity(),
                                        msg,
                                        Toast.LENGTH_LONG)
                                        .show();


                                addContactinDB(contactRegistrationModel.getContactRegistration().get(0), micrositeDetailses.get(lastPosition));
                            } else {
                                if (contactRegistrationModel.getContactRegistration() != null) {
                                    if (contactRegistrationModel.getContactRegistration().get(0).getError() != null) {

                                        Toast.makeText(
                                                getActivity(),
                                                contactRegistrationModel.getContactRegistration().get(0).getError(),
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            }
                        } catch (Exception exp) {

                        }
                    }else
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
                }
            }

             private void addContactinDB(final ContactRegistration contactRegistration, final ClientLeadDetails clientLeadDetails) {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    if (GlobalData.dbHelper != null) {

                        String remoteid = (countryCode1 + clientLeadDetails.getMobile() + "@" + GlobalData.HOST)
                                .trim();

                        if (!GlobalData.dbHelper
                                .checkcontactisAlreadyexist(remoteid)) {
                            if (!GlobalData.dbHelper
                                    .checkcontactisSMS19Alreadyexist(remoteid)) {
                                // 9851144639
                                // sms19.inapp.msg.model.PhoneValidModel model=
                                // ContactUtil.validNumberForInvite(countryCode1+mobilenumber);
                                sms19.inapp.msg.model.PhoneValidModel model = new PhoneValidModel();
                                model.setCountry_code(countryCode1);
                                model.setPhone_number(clientLeadDetails.getMobile());

                                if (model != null
                                        && model.getPhone_number() != null
                                        && model.getPhone_number().length() > 0) {
                                    if (clientLeadDetails.getFirstName().equalsIgnoreCase("")) {
                                        clientLeadDetails.setFirstName(clientLeadDetails.getMobile());
                                    }

                                    GlobalData.dbHelper
                                            .insertSinglecontactinSMS19DBWithUserType(
                                                    clientLeadDetails.getFirstName() + " " + clientLeadDetails.getLastName(),
                                                    model.getCountry_code()
                                                            + model.getPhone_number(),
                                                    contactRegistration.getUserType());


                                    /*if (phoneNo != null && phoneNo.length() > 0) {
                                        GlobalData
                                                *.dbHelper.updateStrager(username
                                                *, model.getCountry_code
                                                * () + model.getPhone_number());
                                    } else {


                                    }

}
        } else {
        // deleteContactFromSMS19
        }
        }
        }
        } catch (Exception e) {

        }
        }
        }
        ).start();
        }

        private String countryCode1 = "+91";
    */