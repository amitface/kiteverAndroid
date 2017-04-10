package sms19.listview.newproject.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.utils.DateHelper;

import java.util.Calendar;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.EditProfilePage;


import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;
import static sms19.listview.newproject.EditProfilePage.UName;
import static sms19.listview.newproject.EditProfilePage.edit_name_click;


public class FragementProfilePersonal extends Fragment {

    Button btnSubmit;
    EditProfieInterface editProfieInterface;
    public EditText editplan,editCurrency, editCompanyName,chatStatus,dob,doe,websiteurl;
    ImageView dobv,websiteicon;
    SharedPreferences prfs;
    private int mYear, mMonth, mDay;
    String LoginType="0";
    LinearLayout websiteLayout;
    View websiteView;


    public static FragementProfilePersonal newIntance() {
        FragementProfilePersonal fragment = new FragementProfilePersonal();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragement_profile_personal,
                container, false);
        editProfieInterface = (EditProfieInterface) getActivity();

        editplan=(EditText)convertView.findViewById(R.id.editplan);
        editCurrency = (EditText)convertView.findViewById(R.id.editCurrency);
        websiteurl = (EditText) convertView.findViewById(R.id.websiteurl);
        dob = (EditText) convertView.findViewById(R.id.dob);
        websiteurl = (EditText) convertView.findViewById(R.id.websiteurl);
        chatStatus= (EditText) convertView.findViewById(R.id.chatStatus);
        dobv = (ImageView) convertView.findViewById(R.id.dobedit);
        websiteLayout=(LinearLayout) convertView.findViewById(R.id.websiteLayout);
        websiteView=(View) convertView.findViewById(R.id.websiteView);
        btnSubmit=(Button)convertView.findViewById(R.id.btnSubmit);


        setRobotoThinFont(editplan,getActivity());
        setRobotoThinFont(editCurrency,getActivity());
        setRobotoThinFont(websiteurl,getActivity());
        setRobotoThinFont(dob,getActivity());
        setRobotoThinFont(chatStatus,getActivity());

        editplan.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        editCurrency.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        websiteurl.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        dob.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        btnSubmit.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        btnSubmit.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));

        setRobotoThinFontButton(btnSubmit,getActivity());




        setScreen();



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = prfs.edit();
                editor.putString("User_DOB",dob.getText().toString());
                editor.putString("CompnayName",editplan.getText().toString());
                //editor.putString("Website",websiteurl.getText().toString());
                editor.commit();
                UName.setEnabled(false);
                edit_name_click.setVisibility(View.VISIBLE);

                if (EditProfilePage.send == true && EditProfilePage.imagePathh != null) {

                    EditProfilePage.secondBar.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            editProfieInterface.fragement1(EditProfilePage.imagePathh,false);
                        }
                    }).start();

                }
                else {
                  EditProfilePage.secondBar.setVisibility(View.VISIBLE);
                    editProfieInterface.fragement1(EditProfilePage.imagePathh,true);

                }

            }
        });

        dobv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if (LoginType.equalsIgnoreCase("2")) {
                                    if (mYear >= (year)) {
                                        dob.setError(null);
                                        dob.setText(dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year);
                                    } else if (mYear == (year) && mMonth >= monthOfYear) {
                                        dob.setError(null);
                                        dob.setText(dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year);
                                    } else if (mYear == (year)
                                            && mMonth == (monthOfYear)
                                            && mDay >= dayOfMonth) {
                                        dob.setError(null);
                                        dob.setText(dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year);
                                    }
                                } else {
                                    if (mYear >= (year + 13)) {
                                        dob.setError(null);
                                        dob.setText(dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year);
                                    } else if (mYear == (year + 13)
                                            && mMonth >= monthOfYear) {
                                        dob.setError(null);
                                        dob.setText(dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year);
                                    } else if (mYear == (year + 13)
                                            && mMonth == (monthOfYear)
                                            && mDay >= dayOfMonth) {
                                        dob.setError(null);
                                        dob.setText(dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year);
                                    } else
                                        dob.setError("For above 13 years");
                                }

                            }
                        }, mYear, mMonth, mDay);
                Long startDatel = DateHelper.getDate(mYear - 13, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(startDatel);
                dpd.show();
            }
        });



        return convertView;
    }



    public void setScreen() {
        try {
            prfs = getActivity().getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);
            String llogintype = prfs.getString("UserCategory", "");
            String userType = prfs.getString("UserType", "");


            String pplanname = prfs.getString("CompnayName", "");
            String ccurrency = prfs.getString("Currency", "");
            String ddoe = prfs.getString("DOE", "");
            String uuser_dob = prfs.getString("User_DOB", "");
            String custom_status = Utils.getUserCustomStatus(getActivity());

            editplan.setText(pplanname);
            websiteurl.setText("");
            editCurrency.setText(ccurrency);
            chatStatus.setText(custom_status);
            LoginType = llogintype;

            websiteLayout.setVisibility(View.GONE);
            websiteView.setVisibility(View.GONE);


            if ( (LoginType.trim().equalsIgnoreCase("2")) || (LoginType.trim().equalsIgnoreCase("5")) ) {
                dob.setHint("D.O.E");
                //websiteLayout.setVisibility(View.VISIBLE);
                //websiteView.setVisibility(View.VISIBLE);
                if (ddoe != null && ddoe.length() > 0) {
                    dob.setHint(ddoe);
                }

            } else {
                dob.setHint("D.O.B");
                if (uuser_dob != null && uuser_dob.length() > 0) {
                    dob.setHint(uuser_dob);
                }
            }

        } catch (Exception e) {
        }
    }


}
