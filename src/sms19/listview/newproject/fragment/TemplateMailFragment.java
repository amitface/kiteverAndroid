package sms19.listview.newproject.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.util.ArrayList;
import java.util.Iterator;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.adapter.EmailTemplateViewAdapter;
import sms19.listview.newproject.EmailEditor;
import sms19.listview.newproject.model.MailTemplateListDetailModel;
import sms19.listview.newproject.model.MailTemplateListModel;
import sms19.listview.newproject.model.UserMailTemplateListDetail;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TemplateMailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateMailFragment extends Fragment implements
        NetworkManager {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressBar progressBar;
    private int TEMPLATE_REQ_ID = 100;
    private ListView templateListView;
    private TextView notAvailableTxt;
    private TextView template_name, template_action;


    public TemplateMailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TemplateMailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TemplateMailFragment newInstance(String param1, String param2) {
        TemplateMailFragment fragment = new TemplateMailFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_template_mail, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_idTemplate);
        if (Utils.isDeviceOnline(getActivity())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callTemplateWebservice();
                }
            }, 500);

        } else {
            try {
                SharedPreferences preferences = getActivity().getSharedPreferences(
                        "EmailTemplatePref", Context.MODE_PRIVATE);
                if (preferences != null) {
                    String response = preferences.getString(
                            "EmailTemplateData", null);
                    onReceiveResponse(TEMPLATE_REQ_ID, response);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        templateListView = (ListView) view.findViewById(R.id.email_template_listviewTemplate);
        notAvailableTxt = (TextView) view.findViewById(R.id.nothing_txtTemplate);

        template_name = (TextView) view.findViewById(R.id.template_name);
        template_action = (TextView) view.findViewById(R.id.template_action);

        setRobotoThinFont(template_name, getActivity());
        setRobotoThinFont(template_action, getActivity());

        template_name.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        template_action.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        FloatingActionButton fabButton = null;
        fabButton = (FloatingActionButton) view.findViewById(R.id.floatButtonAddMailTemplate);
        fabButton.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor(CustomStyle.FLOATING_ACTION_BACKGROUND)));
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmailEditor.class);
                intent.putExtra("choice", "add");
                startActivityForResult(intent, 1);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callTemplateWebservice();
    }


    private void callTemplateWebservice() {
        String userID = Utils.getUserId(getActivity());
        new RequestManager().sendGetRequest(this,
                TEMPLATE_REQ_ID, "GetUserMailTemplateListDetail&UserId="
                        + userID);
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
        if (progressBar != null && progressBar.isShown()) {
            progressBar.setVisibility(View.GONE);
        }
        if (TEMPLATE_REQ_ID == requestId && response != null) {
            final ArrayList<MailTemplateListDetailModel> dataList = new ArrayList<MailTemplateListDetailModel>();
            try {
                Gson gson = new Gson();
                MailTemplateListModel mailTemplateListModel = gson.fromJson(response, MailTemplateListModel.class);


                ArrayList<UserMailTemplateListDetail> userMailTemplateListDetails = (ArrayList<UserMailTemplateListDetail>) mailTemplateListModel.getUserMailTemplateListDetail();
                Iterator<UserMailTemplateListDetail> iterator = userMailTemplateListDetails.iterator();
                while (iterator.hasNext()) {
                    UserMailTemplateListDetail temp = iterator.next();
                    dataList.add(new MailTemplateListDetailModel(
                            String.valueOf(temp.getTemplateId()), String.valueOf(temp.getMId()),
                            temp.getTemplateTitle(), temp.getTemplate(),
                            String.valueOf(temp.getUserId()), temp.getTemplateSubject()));
                }


            } catch (Exception e) {
                // TODO: handle exception
            }
            if (dataList != null && dataList.size() > 0) {
                notAvailableTxt.setVisibility(View.GONE);
                templateListView.setVisibility(View.VISIBLE);
                EmailTemplateViewAdapter adapter = new EmailTemplateViewAdapter(getActivity(),
                        dataList);
                templateListView.setAdapter(adapter);
            } else {
                notAvailableTxt.setVisibility(View.VISIBLE);
                templateListView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        if (progressBar != null && progressBar.isShown()) {
            progressBar.setVisibility(View.GONE);
        }
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError),
                Toast.LENGTH_LONG).show();
    }
}
/*            final ArrayList<MailTemplateListDetailModel> dataList = new ArrayList<MailTemplateListDetailModel>();
                String templateId = null;
                String mId = null;
                String userIdTemplate = null;
                String templateTitle = null;
                String template = null;
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject
                            .getJSONArray("UserMailTemplateListDetail");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject object = jsonArray.getJSONObject(k);
                            if (object != null) {
                                if (object.has("template_id")) {
                                    templateId = object
                                            .getString("template_id");
                                }
                                if (object.has("m_id")) {
                                    mId = object.getString("m_id");
                                }
                                if (object.has("user_id")) {
                                    userIdTemplate = object
                                            .getString("user_id");
                                }
                                if (object.has("template_title")) {
                                    templateTitle = object
                                            .getString("template_title");
                                }
                                if (object.has("template")) {
                                    template = object.getString("template");
                                }
                            }
                            dataList.add(new MailTemplateListDetailModel(
                                    templateId, mId, templateTitle, template,
                                    userIdTemplate));
                        }

                    }
                }*/