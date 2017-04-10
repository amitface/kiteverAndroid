package com.kitever.pos.fragment;


import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.fragment.Interfaces.SetViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.utils.Utils.actionBarSettingWithBack;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PosHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PosHome extends Fragment implements NetworkManager, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;

    TextView creditVal, graph_payment_txt, graph_title;
    private boolean flag = false;

    private String UserId;
    private String UserLogin;
    private String PassWord;

    //for graph
    private final int KEY_FETCH_GRAPH_DETAILS = 1;
    private final int KEY_FETCH_TOTAL_CREDIT = 2;

    private ArrayList<Double> graphbillarray;
    private GraphView graph;
    private DataPoint[] points;
    private SetViewPager setViewPager = null;

    private ProgressDialog progressDialog = null;
    private String userId;
    private String userLogin;
    private String passWord;
    private ActionBar bar;
    private int jlength;
    MoonIcon micon;
    private TextView payment_detail;
    private TextView credit_detail,credit_val_txt;
    ProgressBar progressBar;

    public PosHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PosHome.
     */
    // TODO: Rename and change types and number of parameters
    public static PosHome newInstance(String param1, String param2, String param3) {
        PosHome fragment = new PosHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UserId = getArguments().getString(ARG_PARAM1);
            userLogin = getArguments().getString(ARG_PARAM2);
            PassWord = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences prfs = getActivity().getSharedPreferences("profileData",
                Context.MODE_PRIVATE);

        //layout.setLayoutParams(new LinearLayout.LayoutParams(screenwidth-largesize, largesize));

        userLogin = prfs.getString("user_login", "");
        passWord = prfs.getString("Pwd", "");
        userId = Utils.getUserId(getActivity());
        View view = inflater.inflate(R.layout.pos_home_grid_layout, container, false);
        micon = new MoonIcon(this);

        creditVal = (TextView) view.findViewById(R.id.credit_val);
        graph_payment_txt = (TextView) view.findViewById(R.id.graph_payment_txt);
        graph_title = (TextView) view.findViewById(R.id.graph_title);
        payment_detail = (TextView) view.findViewById(R.id.imgFullscreen);
        credit_detail = (TextView) view.findViewById(R.id.credit_shortcut);
        credit_val_txt= (TextView) view.findViewById(R.id.credit_val_txt);


        setRobotoThinFont(creditVal,getActivity());
        setRobotoThinFont(graph_payment_txt,getActivity());
        setRobotoThinFont(graph_title,getActivity());
        setRobotoThinFont(payment_detail,getActivity());
        setRobotoThinFont(credit_detail,getActivity());
        setRobotoThinFont(credit_val_txt,getActivity());


        creditVal.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        graph_payment_txt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        graph_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        payment_detail.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        credit_detail.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        credit_val_txt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        progressBar = (ProgressBar) view.findViewById(R.id.marker_progress);
        progressBar.setVisibility(View.GONE);

        String htmlstr="<u>View detail</u>";
        payment_detail.setText(Html.fromHtml(htmlstr));
        credit_detail.setText(Html.fromHtml(htmlstr));

        payment_detail.setOnClickListener(this);
        credit_detail.setOnClickListener(this);

        micon.setTextfont(creditVal);
        creditVal.setOnClickListener(this);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenwidth = displaymetrics.widthPixels;
        int  screenheight= displaymetrics.heightPixels;
        int graph_height= (screenheight*40)/100;

        Log.i("Screen","W-"+screenwidth+"H-"+screenheight+"70-"+graph_height);


        graph = (GraphView) view.findViewById(R.id.graph);
        graph.setLayoutParams(new RelativeLayout.LayoutParams(screenwidth, graph_height));
        graph.setOnClickListener(this);

        fetchGraphicalPayemntDetail();
        fetchTotalCredit();
        return view;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getPassWord() {
        return passWord;
    }

    private void fetchGraphicalPayemntDetail() {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(getActivity())) {
            try {
                showLoading();
                Map map = new HashMap<>();

                map.put("Page", "GraphicalPayemntDetail");
                map.put("userID", getUserId());
                map.put("UserLogin", getUserLogin());
                map.put("Password", getPassWord());

                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_GRAPH_DETAILS, map);
            } catch (Exception e) {

            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    private void fetchTotalCredit() {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(getActivity())) {
            try {
                showLoading();
                Map map = new HashMap<>();
                map.put("Page", "FetchTotalCredit");
                map.put("userID", getUserId());
                map.put("UserLogin", getUserLogin());
                map.put("Password", getPassWord());
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_TOTAL_CREDIT, map);
            } catch (Exception e) {

            }
        } else {
            showMessage("Internet connection not found");
        }

    }

    private void parseGraphResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject != null) {
                String isSetting;
                if(jsonObject.has("Setting"))
                {
                    isSetting  = jsonObject.get("Setting").toString();
                    if(isSetting!=null) {
                        try {
                            SharedPreferences preference = getActivity().getSharedPreferences("profileData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preference.edit();
                            if (isSetting.equals("1"))
                                editor.putString("isPosSettingEnabled", "1");
                            else
                                editor.putString("isPosSettingEnabled", "0");

                            editor.commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (jsonObject.has("GraphicalPaymentDetails")) {
                    JSONArray jsonArray = jsonObject
                            .getJSONArray("GraphicalPaymentDetails");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        graphbillarray = new ArrayList<Double>();
                        points = new DataPoint[jsonArray.length() + 1];
                        points[0] = new DataPoint(0, 0);
                        Double BillAmount = 0D;
                        jlength = jsonArray.length();

                        for (int k = 0; k < jlength; k++) {
                            JSONObject object = jsonArray.getJSONObject(k);
                            if (object.has("BillAmount")) {
                                BillAmount = object.getDouble("BillAmount");
                            }
                            graphbillarray.add(BillAmount);
//                            points[k+1] = new DataPoint(k+1,0);
                            int x = k + 1;
                           Double yaxis = (BillAmount / 1000);
//                            points[k] = new DataPoint(x,yaxis);
                            points[k + 1] = new DataPoint(x, yaxis);
                        }

                        initGraph(false, points);
                    } else {
                        initGraph(true, points);
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void initGraph(boolean isnodata, DataPoint[] points) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        String pattern = "MMMM yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);

        if (isnodata) {
            graph_title.setText("You don't have any payment till now");
            graph_payment_txt.setText("Week wise graph: " + format.format(new Date()));
        } else {
            graph.setTitle("Sales This Month");
            graph.setTitleColor(Color.parseColor(CustomStyle.TAB_FONT_COLOR));
            graph_payment_txt.setText("Week wise graph: " + format.format(new Date()));
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);
            series.setSpacing(50); // 50% spacing between bars
            series.setAnimated(true);

          /*  // set manual X bounds
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(200);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(6);
            graph.getViewport().setMaxX(80);*/

            // enable scaling
            graph.getViewport().setScalable(false);

            // enable scrolling
            graph.getViewport().setScrollable(true);

            series.setTitle("Order Amount");


            graph.addSeries(series);

            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            // set the viewport wider than the data, to have a nice view
            graph.getViewport().setMinX(0d);
            graph.getViewport().setMaxX(4d);
            graph.getViewport().setXAxisBoundsManual(true);

            // draw values on top
            // draw values on top
            series.setDrawValuesOnTop(true);
            series.setColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
            series.setValuesOnTopColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

            graph.getGridLabelRenderer().setVerticalAxisTitle("Sales in Thousands");
            graph.getGridLabelRenderer().setHorizontalAxisTitle("In weeks");

            // optional styles
            //graph.setTitleTextSize(40);
            //graph.setTitleColor(Color.BLUE);
            //graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(40);
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
            //graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40);
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        }

    }

    private void parseTotalCredit(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject != null) {
                if (jsonObject.has("POSTotalCredit")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("POSTotalCredit");
                    if (jsonArray != null && jsonArray.length() > 0) {

                        JSONObject object = jsonArray.getJSONObject(0);
                        if (object.has("BalanceAmount")) {
                            try {
                                double BalanceAmount = object.getDouble("BalanceAmount");
                                creditVal.setText("" + Utils.doubleToString(BalanceAmount) + " " + getString(R.string.rsicon));
                                micon.setTextfont(creditVal);
                            }catch(Exception e){}
                        }
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void onReceiveResponse(int requestId, String response) {
        hideLoading();
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_GRAPH_DETAILS) {
                parseGraphResponse(response);
            } else if (requestId == KEY_FETCH_TOTAL_CREDIT) {
                parseTotalCredit(response);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        showMessage(getActivity().getResources().getString(R.string.volleyError).toString());
    }


    public void showMessageWithTitle(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                getActivity());
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void showMessage(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                getActivity());
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoading();
    }

    public void startAcitivityWithEffect(Intent slideactivity) {
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getActivity(), R.anim.animation, R.anim.animation2).toBundle();
        startActivity(slideactivity, bndlanimation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgFullscreen:
                Intent resultIntent = new Intent(getActivity(), POSHomeTabbedActivity.class);
                resultIntent.putExtra("FragmentPos", 7);
                resultIntent.putExtra("TabPos", 3);
                startActivity(resultIntent);
                getActivity().finish();
                break;
            case R.id.credit_shortcut:
                Intent intent = new Intent(getActivity(), POSHomeTabbedActivity.class);
                intent.putExtra("FragmentPos", 8);
                intent.putExtra("TabPos", 3);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }

    public String getUserId() {
        return userId;
    }
}
