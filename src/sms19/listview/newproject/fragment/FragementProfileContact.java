package sms19.listview.newproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.EditProfilePage;
import sms19.listview.newproject.model.CityList;
import sms19.listview.newproject.model.ProfileContact;
import sms19.listview.newproject.model.StateList;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;
import static sms19.listview.newproject.EditProfilePage.UName;
import static sms19.listview.newproject.EditProfilePage.edit_name_click;

/**
 * Created by Anurag on 1/24/2017.
 */

public class FragementProfileContact extends Fragment implements NetworkManager {

    public EditText editCountry, editMobile, editEmail, editPinCode, editState, editCity;
    Button btnSubmit;
    EditProfieInterface editProfieInterface;
    SharedPreferences prfs;
    Spinner spinnerState, spinnerCity;
    ProgressBar progressBar;
    private List<StateList> statelist = null;
    private List<CityList> citylist = null;
    private String stateid = "", cityid = "", stateName = "", cityName = "";

    private static final int FETCH_STATE_LIST = 1;
    private static final int FETCH_CITY_LIST = 2;


    public static FragementProfileContact newIntance() {
        FragementProfileContact fcontact = new FragementProfileContact();
        return fcontact;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragement_profile_contact,
                container, false);
        editProfieInterface = (EditProfieInterface) getActivity();

        editCountry = (EditText) convertView.findViewById(R.id.editCountry);
        editMobile = (EditText) convertView.findViewById(R.id.editMobile);
        editEmail = (EditText) convertView.findViewById(R.id.editEmail);
        editPinCode = (EditText) convertView.findViewById(R.id.editPinCode);
        // editState=(EditText)convertView.findViewById(R.id.editState);
        // editCity=(EditText)convertView.findViewById(R.id.editCity);
        btnSubmit = (Button) convertView.findViewById(R.id.btnSubmit);
        progressBar = (ProgressBar) convertView.findViewById(R.id.secondBar);
        spinnerState = (Spinner) convertView.findViewById(R.id.spinnerState);
        spinnerCity = (Spinner) convertView.findViewById(R.id.spinnerCity);


        setRobotoThinFont(editCountry, getActivity());
        setRobotoThinFont(editMobile, getActivity());
        setRobotoThinFont(editEmail, getActivity());
        setRobotoThinFont(editPinCode, getActivity());

        editCountry.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        editMobile.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        editEmail.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        editPinCode.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        btnSubmit.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        btnSubmit.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        setRobotoThinFontButton(btnSubmit, getActivity());


        spinnerCity.setEnabled(false);
        setScreen();
        fetchStateData("110");


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = prfs.edit();
                editor.putString("pincode", editPinCode.getText().toString());
                editor.putString("stateid", stateid);
                editor.putString("cityid", cityid);
                editor.putString("CityName", cityName);
                editor.putString("StateName", stateName);
                editor.commit();
                UName.setEnabled(false);
                edit_name_click.setVisibility(View.VISIBLE);


                if (EditProfilePage.send == true && EditProfilePage.imagePathh != null) {

                    EditProfilePage.secondBar.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            editProfieInterface.fragement1(EditProfilePage.imagePathh, false);
                        }
                    }).start();

                } else {
                    EditProfilePage.secondBar.setVisibility(View.VISIBLE);

                    editProfieInterface.fragement1(EditProfilePage.imagePathh, true);
                    // registerUser();
                }

            }
        });


        return convertView;
    }

    public void setScreen() {
        try {
            prfs = getActivity().getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);
            String nname = prfs.getString("Name", "");
            String mmobile = prfs.getString("Mobile", "");
            String eemail = prfs.getString("EMail", "");
            String ccountry = prfs.getString("Country", "");
            String zzipcode = prfs.getString("pincode", "");
            cityid = prfs.getString("cityid", "");
            stateid = prfs.getString("stateid", "");

            editCountry.setText(ccountry);
            editMobile.setText(mmobile);
            editEmail.setText(eemail);
            editPinCode.setText(zzipcode);
            editState.setText("");
            editCity.setText("");

        } catch (Exception e) {
            e.getMessage();
        }

    }


    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }


    private void fetchStateData(String countryid) {

        if (Utils.isDeviceOnline(getActivity())) {
            showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("Page", "GetState");
            map.put("countryID", countryid);
            try {
                new RequestManager().sendPostRequest(this,
                        FETCH_STATE_LIST, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // showMessage("Internet connection not found");
        }
    }

    private void fetchCityData(String stateid) {

        if (Utils.isDeviceOnline(getActivity())) {
            showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("Page", "GetCity");
            map.put("stateID", stateid);
            try {
                new RequestManager().sendPostRequest(this,
                        FETCH_CITY_LIST, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // showMessage("Internet connection not found");
        }
    }

    AdapterView.OnItemSelectedListener StateSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {


            if (position > 0) {
                stateid = "" + statelist.get(position - 1).getStateID();
                stateName = statelist.get(position - 1).getStateName();
                fetchCityData(stateid);
            } else spinnerCity.setEnabled(false);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener CitySelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            if (position > 0) {
                cityid = "" + citylist.get(position - 1).getCityID();
                cityName = citylist.get(position - 1).getCityName();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onReceiveResponse(int requestId, String response) {

        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == FETCH_STATE_LIST) {
                Gson gson = new Gson();
                statelist = new ArrayList<StateList>();
                ProfileContact stlist = gson.fromJson(response, ProfileContact.class);

                statelist = stlist.getStateList();
                int select_pos = 0;
                if (stlist != null && statelist.size() > 0) {

                    ArrayList<String> stateNameList = new ArrayList<String>();
                    stateNameList.add("Select State");
                    for (int i = 0; i < statelist.size(); i++) {
                        stateNameList.add(statelist.get(i).getStateName());
                        String current_state_id = "" + statelist.get(i).getStateID();
                        if (stateid.equals(current_state_id))
                            select_pos = i + 1;
                    }

                    try {
                        Utils.MySpinnerAdapter typeAdapter = new Utils.MySpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, stateNameList);
                        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerState.setAdapter(typeAdapter);
                        spinnerState.setSelection(select_pos);
                        spinnerState.setOnItemSelectedListener(StateSelectedListener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } else if (requestId == FETCH_CITY_LIST) {

                spinnerCity.setEnabled(true);
                Gson gson = new Gson();
                citylist = new ArrayList<CityList>();
                ProfileContact ctlist = gson.fromJson(response, ProfileContact.class);
                citylist = ctlist.getCityList();
                if (ctlist != null && citylist.size() > 0) {

                    ArrayList<String> cityNameList = new ArrayList<String>();
                    cityNameList.add("Select City");
                    int select_pos = 0;
                    for (int i = 0; i < citylist.size(); i++) {
                        cityNameList.add(citylist.get(i).getCityName());
                        String current_city_id = "" + citylist.get(i).getCityID();
                        if (cityid.equals(current_city_id)) select_pos = i + 1;
                    }

                    try {
                        Utils.MySpinnerAdapter cityadapter = new Utils.MySpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, cityNameList);
                        cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCity.setAdapter(cityadapter);
                        spinnerCity.setSelection(select_pos);
                        spinnerCity.setOnItemSelectedListener(CitySelectedListener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideLoading();
    }
}
