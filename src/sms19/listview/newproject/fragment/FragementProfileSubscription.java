package sms19.listview.newproject.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.AlertDialogManager;
import com.kitever.app.context.CustomStyle;

import org.apache.http.util.EncodingUtils;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.newproject.EditProfilePage;
import sms19.listview.newproject.Home;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;


public class FragementProfileSubscription extends Fragment {

    TextView planname,expirydate,tvupgrade,bal,topup;

    public  static FragementProfileSubscription newIntance()
    {
        FragementProfileSubscription fcontact=new FragementProfileSubscription();
        return fcontact;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragement_profile_subscription,
                container, false);
        planname=(TextView)convertView.findViewById(R.id.planname);
        expirydate=(TextView)convertView.findViewById(R.id.expirydate);
        tvupgrade=(TextView)convertView.findViewById(R.id.tvupgrade);
        bal=(TextView)convertView.findViewById(R.id.bal);
        topup=(TextView)convertView.findViewById(R.id.topup);

        setRobotoThinFont(planname,getActivity());
        setRobotoThinFont(expirydate,getActivity());
        setRobotoThinFont(tvupgrade,getActivity());
        setRobotoThinFont(bal,getActivity());
        setRobotoThinFont(topup,getActivity());

        planname.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        expirydate.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvupgrade.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        bal.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        topup.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));




        setScreen();
        return convertView;
    }

    public void setScreen()
    {


        try {
            SharedPreferences prfs = getActivity().getSharedPreferences("profileData",
                    Context.MODE_PRIVATE);
            String llogintype = prfs.getString("UserCategory", "");
            String userType = prfs.getString("UserType", "");
            String pplanname = prfs.getString("Plan", "Free Plan");
            String eexpirydate = prfs.getString("ExpiryDate", "forever");
            String balance = prfs.getString("Balance", "forever");
            //  String str = "You are registered in "+pplanname;
            planname.setText("Plan : "+pplanname);
            expirydate.setText("Active till : "+eexpirydate+"  ");
            String str="<u> Upgrade</u>";
            tvupgrade.setText(Html.fromHtml(str));
            String popupstr="<u> Topup</u>";
            topup.setText(Html.fromHtml(popupstr));
            bal.setText(balance +"  ");
            tvupgrade.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialogManager alert=new AlertDialogManager();
                    String Pass = Utils.getPassword(getActivity());
                    String postData = "password=" + Pass;// &id=236";
                    String url="http://kitever.com/BuyCredit.aspx?tab=plans&userid="+ EditProfilePage.UserId;
                    byte[] data= EncodingUtils.getBytes(postData, "BASE64");
                    alert.webviewPoup(getActivity(),"Plans",url,data);
                }
            });

            topup.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialogManager alert=new AlertDialogManager();
                    String Pass = Utils.getPassword(getActivity());
                    String postData = "password=" + Pass;// &id=236";
                    String url="http://kitever.com/BuyCredit.aspx?tab=topup&userid="+ EditProfilePage.UserId;
                    byte[] data= EncodingUtils.getBytes(postData, "BASE64");
                    alert.webviewPoup(getActivity(),"Topup",url,data);
                }
            });
        }catch(Exception e){}

    }
}