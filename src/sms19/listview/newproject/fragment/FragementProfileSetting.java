package sms19.listview.newproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import sms19.listview.newproject.EditProfilePage;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;
import static sms19.listview.newproject.EditProfilePage.UName;
import static sms19.listview.newproject.EditProfilePage.edit_name_click;

/**
 * Created by Anurag on 1/24/2017.
 */

public class FragementProfileSetting  extends Fragment {

    LinearLayout lyt_setting;
    Button btnSubmit;
    public EditText editMerchantName, editStoreUrl,edit_home_url;
    EditProfieInterface editProfieInterface;
    SharedPreferences prfs;

    public  static FragementProfileSetting newIntance()
    {
        FragementProfileSetting fcontact=new FragementProfileSetting();
        return fcontact;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragement_profile_settings,
                container, false);
        editProfieInterface = (EditProfieInterface) getActivity();

        editMerchantName=(EditText)convertView.findViewById(R.id.editMerchantName);
        editStoreUrl=(EditText)convertView.findViewById(R.id.store_url);
        edit_home_url=(EditText)convertView.findViewById(R.id.edit_home_url);
        btnSubmit=(Button)convertView.findViewById(R.id.btnSubmit);
        lyt_setting=(LinearLayout)convertView.findViewById(R.id.lyt_setting);

        setRobotoThinFont(editMerchantName,getActivity());
        setRobotoThinFont(editStoreUrl,getActivity());
        setRobotoThinFont(edit_home_url,getActivity());


        editMerchantName.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        editStoreUrl.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        edit_home_url.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));



        btnSubmit.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        btnSubmit.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));

        setRobotoThinFontButton(btnSubmit,getActivity());


        setScreen();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validation()) {
                    SharedPreferences.Editor editor = prfs.edit();
                    editor.putString("MerchantName", editMerchantName.getText().toString());
                    editor.putString("Merchant_Url", editStoreUrl.getText().toString());
                    editor.putString("Home_Url", edit_home_url.getText().toString());
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

            }
        });

        return convertView;
    }

    private boolean validation()
    {
        String storeUrl=editStoreUrl.getText().toString().trim();
        String homeUrl=edit_home_url.getText().toString().trim();

       if(storeUrl!=null && storeUrl!="" && storeUrl.length()>0)
       {
           if(!Patterns.WEB_URL.matcher(storeUrl).matches())
           {
               Toast.makeText(getActivity(),"Enter valid store url",Toast.LENGTH_SHORT).show();
               return false;
           }
           else if(checkHttp(storeUrl)==false)
           {
               return false;
           }
           else return true;
       }

        if(homeUrl!=null && homeUrl!="" && homeUrl.length()>0)
        {
            if(!Patterns.WEB_URL.matcher(homeUrl).matches())
            {
                Toast.makeText(getActivity(),"Enter valid home url",Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(checkHttp(storeUrl)==false)
            {
                return false;
            }
            else return true;
        }

        return true;
    }

    private boolean checkHttp(String url)
    {
        if(url.substring(0,7).equals("http://") || url.substring(0,8).equals("https://"))
        {
            return true;
        }
        else
        {
            Toast.makeText(getActivity(),"Enter url with http",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void setScreen()
    {
        try {
            prfs = getActivity().getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);
            String nname = prfs.getString("Name", "");
            String LoginType = prfs.getString("UserCategory", "");
            String merchantNameTxt = prfs.getString("MerchantName", null);
            String merchant_Url = prfs.getString("Merchant_Url", null);
            String home_url = prfs.getString("Home_Url", "");
            String userType = prfs.getString("UserType", "");

            if ((userType.trim()).equalsIgnoreCase("2"))
            {
                lyt_setting.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
            }
            else{
                lyt_setting.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
            }
            editMerchantName.setText(merchantNameTxt);
            editStoreUrl.setText(merchant_Url);
            edit_home_url.setText(home_url);

        } catch(Exception e){e.getMessage();}

    }
}