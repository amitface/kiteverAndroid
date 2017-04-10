package sms19.listview.newproject.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.EditProfile;
import sms19.listview.newproject.Emergency;
import sms19.listview.newproject.Friendsinvite;
import sms19.listview.newproject.HowToUse;
import sms19.listview.newproject.ScheduleList;
import sms19.listview.newproject.TermsAndCondition;
import sms19.listview.newproject.Transaction;
import sms19.listview.newproject.model.BindTemplateModel;
import sms19.listview.newproject.model.SMSTemplateDetailsModel;
import sms19.listview.webservice.webservice;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TemplateSmsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateSmsFragment extends Fragment implements NetworkManager {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView imagetemplate;
    private Button btnCustomGrid;

    private ArrayList<String> listData;
    private String newe[];
    private ArrayAdapter<String> temList;
    private ArrayAdapter<String> temListDATABASE;
    private ArrayAdapter<String> templateicon;
    private String ListData[];
    private String[] values;
    private ProgressDialog progressDialog;
    private String data, data1, UserId;
    private DataBaseDetails dbObject ;
    public static Activity templateHomeFlag;
    private Context gContext;
    private String navigate, important, important1, important2;
    private final int KEY_CREATE_TEMPLATE = 1;
    private EditText tempDetails;


    public TemplateSmsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TemplateSmsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TemplateSmsFragment newInstance(String param1, String param2) {
        TemplateSmsFragment fragment = new TemplateSmsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbObject = new DataBaseDetails(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_template_sms, container, false);
        imagetemplate = (GridView) view.findViewById(R.id.gridTemplateSMS);

        dbObject.Open();
        Cursor c;
        c = dbObject.getTemplates();
        int count = c.getCount();
        if (count <= 0) {
            calltemplateserviceinflater();
        }
        dbObject.close();

//        btnCustomGrid.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(gContext, CustomTemplate.class);
//                startActivity(i);
//                getActivity().finish();
//            }
//        });

        try {
            values = getTemplateTitle();

            imagetemplate.setAdapter(new TemplateSmsFragmentAdapter(getActivity(),
                    R.layout.activity_template_list, values, navigate,
                    important, important1, important2));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        FloatingActionButton fabButton = null;
        fabButton = (FloatingActionButton) view.findViewById(R.id.floatButtonAddSmsTemplate);
        fabButton.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor(CustomStyle.FLOATING_ACTION_BACKGROUND)));
        fabButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.add_template_layout);

                dialog.setCancelable(false);
                dialog.setTitle("Add Template");
                // EditText
                // tempTitle=(EditText)dialog.findViewById((R.id.temp_title));
                tempDetails = (EditText) dialog
                        .findViewById((R.id.temp_details));
                final TextView txtCount = (TextView) dialog
                        .findViewById(R.id.txt_count);
                Button addBtn = (Button) dialog
                        .findViewById(R.id.add_btn);
                Button cancelBtn = (Button) dialog
                        .findViewById(R.id.cancel_btn);
                addBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (tempDetails.getText().toString() != null
                                && !tempDetails.getText()
                                .toString()
                                .equalsIgnoreCase("")) {

                            createTemplate(tempDetails.getText()
                                    .toString());
                            // templatesubAdapter.notifyDataSetChanged();

                        }
                        dialog.dismiss();
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                tempDetails
                        .addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(
                                    CharSequence s, int start,
                                    int before, int count) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void beforeTextChanged(
                                    CharSequence s, int start,
                                    int count, int after) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                // TODO Auto-generated method stub
                                String totalString = tempDetails
                                        .getText().toString();
                                if (totalString != null
                                        && totalString.length() > 0) {
                                    txtCount.setText(totalString
                                            .length() + "/2000");
                                } else {
                                    txtCount.setText("0/2000");
                                }
                            }
                        });
                dialog.show();
                // dbObject.addtoAlltemplate(UserId, data,
                // TemplatesubAdapter.resultData, "Personal");
            }
        });

        return view;
    }

    private void fetchUserId() {
        dbObject.Open();
        Cursor c;
        c = dbObject.getLoginDetails();

        while (c.moveToNext()) {
            UserId = c.getString(3);
        }
        dbObject.close();
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.rfshMenu) {
            calltemplateserviceinflater();

        }
        if (id == R.id.transactionMenu) {

            Intent i = new Intent(gContext, Transaction.class);
            startActivity(i);
            getActivity().finish();
            return true;
        }

        if (id == R.id.editProfileMenu) {
            Intent i = new Intent(gContext, EditProfile.class);
            startActivity(i);
            getActivity().finish();
            return true;
        }

        if (id == R.id.schedulemsgrMenu) {
            Intent i = new Intent(gContext, ScheduleList.class);
            startActivity(i);
            getActivity().finish();
            return true;
        }

        if (id == R.id.termsconditionMenu) {
            Intent i = new Intent(gContext, TermsAndCondition.class);
            startActivity(i);
            getActivity().finish();
            return true;
        }

        if (id == R.id.HowtoUseMenu) {
            Intent i = new Intent(gContext, HowToUse.class);
            startActivity(i);
            getActivity().finish();
            return true;
        }

        if (id == R.id.referMenu) {
            Intent i = new Intent(gContext, Friendsinvite.class);
            startActivity(i);
            getActivity().finish();
            return true;
        }

        if (id == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    private void calltemplateserviceinflater() {
        progressDialog = ProgressDialog.show(gContext, null, "Please wait...");
        fetchUserId();
        new webservice(null, webservice.GetUserTemplateList.geturl(UserId),
                webservice.TYPE_GET, webservice.TYPE_TEMPLATE,
                new webservice.ServiceHitListener() {

                    @Override
                    public void onSuccess(Object Result, int id) {

                        try {
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        BindTemplateModel mod = (BindTemplateModel) Result;
                        listData = new ArrayList<String>();
                        int len = mod.getListSize();

                        if (haveTemplate()) {
                            dbObject.Open();
                            dbObject.DeleteTemplateData();
                            dbObject.close();
                        }

                        for (int i = 0; i < len; i++) {
                            String a = mod.getUserTemplateList().get(i)
                                    .getTemplate_Title();
                            listData.add(a);
                            ListData = new String[listData.size()];
                            ListData = listData.toArray(ListData);

                            imagetemplate
                                    .setAdapter(new TemplateSmsFragmentAdapter(
                                            getActivity(),
                                            R.layout.activity_template_list,
                                            ListData, navigate, important,
                                            important1, important2));

                            fetchUserId();

                            dbObject.Open();
                            dbObject.addTemplate(UserId, a);
                            dbObject.close();
                            try {
                                String EmergencyMessage = mod
                                        .getUserTemplateList().get(0)
                                        .getEmergencyMessage();
                                try {

                                    Emergency.desAct.finish();
                                } catch (Exception e) {
                                }

                                if (!(EmergencyMessage.equalsIgnoreCase("NO"))) {
                                    Intent rt = new Intent(getActivity(),
                                            Emergency.class);
                                    rt.putExtra("Emergency", EmergencyMessage);
                                    startActivity(rt);

                                }
                            } catch (Exception e) {

                            }
                        }
                    }

                    @Override
                    public void onError(String Error, int id) {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Toast.makeText(gContext, Error, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private boolean haveTemplate() {
        dbObject.Open();
        Cursor c;
        c = dbObject.getTemplates();
        while (c.moveToNext() == true) {
            return true;
        }

        dbObject.close();
        return false;
    }

    public String[] getTemplateTitle() {
        dbObject.Open();
        Cursor c;

        c = dbObject.getTemplates();
        int count = c.getCount();
        String Ttitle[] = new String[count];

        if (c.getCount() >= 1) {
            int i = 0;
            while (c.moveToNext() && i < count) {
                Ttitle[i] = c.getString(1);
                i++;
            }
        }
        dbObject.close();
        return Ttitle;
    }

    private  void createTemplate(String content)
    {
        Log.i("send","sendsms");
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(getActivity())) {
            try {
//                showLoading();
                Map map = new HashMap<>();
                map.put("Page", "InsertSMSTemplates");
                map.put("UserID", Utils.getUserId(getActivity()));
                map.put("TemplateName","Personal");
                map.put("TemplateContent",content);

                Log.i("Order","" + map.toString());

                new RequestManager().sendPostRequest(this,
                        KEY_CREATE_TEMPLATE, map);
            } catch (Exception e) {
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.volleyError),Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(),"No Internet connect found",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if(requestId == KEY_CREATE_TEMPLATE)
        {
            Gson gson = new Gson();

            SMSTemplateDetailsModel smsTemplateDetailsModel = gson.fromJson(response,SMSTemplateDetailsModel.class);
            if(smsTemplateDetailsModel!=null)
            {
                if(smsTemplateDetailsModel.getStatus().equals("true"))
                {
                    SMSTemplateDetailsModel.SMSTemplateDetails smsTemplateDetails = smsTemplateDetailsModel.getDetails().get(0);
                    if(smsTemplateDetails!=null)
                    {
                        try
                        {
                            dbObject.Open();
                            dbObject.addtoAlltemplate(UserId, smsTemplateDetails.getTemplate_id(),
                                    smsTemplateDetails.getTemplate(), "Personal");
                            Cursor c = dbObject.getTemplates();
                            int count = c.getCount();
                            if (count <= 0) {
                                calltemplateserviceinflater();
                            }
                        }catch (Exception e)
                        {

                        }finally {
                            dbObject.close();
                        }
                    }else
                        Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.volleyError),Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(getActivity(),smsTemplateDetailsModel.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.volleyError),Toast.LENGTH_LONG).show();
    }

}
