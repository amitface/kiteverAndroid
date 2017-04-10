package sms19.listview.newproject.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.WebClientClass;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.adapter.MicroSiteAdapter;
import sms19.listview.newproject.model.FetchMicroSiteModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MicroSiteCreate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MicroSiteCreate extends Fragment implements NetworkManager, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int KEY_CREATE_MICROSITE = 1;
    private final int KEY_FETCH_MICROSITE = 2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressBar progressBar;
    private CardView cardviewMicroSiteStatics;
    private CardView cardviewMicroSiteCreate;
    private ListView listviewMicroSite;
    private Button btnCreateMicrosite;
    private EditText edtNameMicrosite;
    private TextView tvPageName,tvMicroSiteRefresh, tvEdit;

    private  FetchMicroSiteModel fetchMicroSiteModel;
    public MicroSiteCreate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MicroSiteCreate.
     */
    // TODO: Rename and change types and number of parameters
    public static MicroSiteCreate newInstance(String param1, String param2) {
        MicroSiteCreate fragment = new MicroSiteCreate();
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
        View view = inflater.inflate(R.layout.fragment_micro_site_create, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressMicroSite);
        btnCreateMicrosite = (Button) view.findViewById(R.id.btnCreateMicrosite);
        edtNameMicrosite = (EditText) view.findViewById(R.id.edtNameMicroSite);
        tvPageName = (TextView) view.findViewById(R.id.tvPageName);
        tvEdit = (TextView) view.findViewById(R.id.tvEdit);
        tvMicroSiteRefresh = (TextView) view.findViewById(R.id.tvMicroSiteRefresh);
        cardviewMicroSiteStatics = (CardView)  view.findViewById(R.id.cardviewMicroSiteStatics);
        cardviewMicroSiteCreate = (CardView)  view.findViewById(R.id.cardviewMicroSiteCreate);
        listviewMicroSite =(ListView) view.findViewById(R.id.listviewMicroSite);

        tvPageName.setOnClickListener(this);
        tvMicroSiteRefresh.setOnClickListener(this);
        btnCreateMicrosite.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        requestFetchMicroSite();
        return view;
    }

    private boolean vaildate() {
        if (edtNameMicrosite.length() < 2) {
            Toast.makeText(getActivity(), "Name should be more than two character", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        progressBar.setVisibility(View.GONE);
        if (requestId == KEY_CREATE_MICROSITE) {
            Gson gson = new Gson();
            FetchMicroSiteModel fetchMicroSiteModel = gson.fromJson(response, FetchMicroSiteModel.class);
            if (fetchMicroSiteModel.getMicrositeDetails().get(0).getColumn1() != null)
            {
                Toast.makeText(getActivity(), fetchMicroSiteModel.getMicrositeDetails().get(0).getColumn1().toString(), Toast.LENGTH_LONG).show();
                tvPageName.setText("Page Name : "+fetchMicroSiteModel.getMicrositeDetails().get(0).getPagename());
                cardviewMicroSiteStatics.setVisibility(View.VISIBLE);
                cardviewMicroSiteCreate.setVisibility(View.GONE);
                listviewMicroSite.setAdapter(new MicroSiteAdapter(getActivity(),fetchMicroSiteModel.getMicrositeDetails()));
            }
            else
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
        } else if (requestId == KEY_FETCH_MICROSITE) {
            fetchMicroSiteModel = null;
            Gson gson = new Gson();
             fetchMicroSiteModel = gson.fromJson(response, FetchMicroSiteModel.class);
            if(fetchMicroSiteModel.getMicrositeDetails()!=null && fetchMicroSiteModel.getMicrositeDetails().size()>0)
            {
                tvPageName.setText("Page Name : "+fetchMicroSiteModel.getMicrositeDetails().get(0).getPagename());
                cardviewMicroSiteStatics.setVisibility(View.VISIBLE);
                cardviewMicroSiteCreate.setVisibility(View.GONE);
                listviewMicroSite.setAdapter(new MicroSiteAdapter(getActivity(),fetchMicroSiteModel.getMicrositeDetails()));
            }else
            {
                cardviewMicroSiteStatics.setVisibility(View.GONE);
                cardviewMicroSiteCreate.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateMicrosite:
                if (vaildate()) {
                    requestCreateMicroSite(edtNameMicrosite.getText().toString());
                }
                break;
            case R.id.tvMicroSiteRefresh:
                requestFetchMicroSite();
                break;
            case R.id.tvPageName:
                        showMircoSitePopup(fetchMicroSiteModel.getMicrositeDetails().get(0).getPagename());
                break;
            case R.id.tvEdit:
                Intent intent4 = new Intent(getActivity(), MicroSiteEditor.class);
                intent4.putExtra("fragment", "edit");
                startActivityForResult(intent4,1);
                break;
        }
    }

    private void requestCreateMicroSite(String name) {
        if (Utils.isDeviceOnline(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            Map map = new HashMap<>();
            map.put("Page", "CreateMicrosite");
            map.put("Userid", Utils.getUserId(getActivity()));
            map.put("PageNameMicrosite", name);
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_CREATE_MICROSITE, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestFetchMicroSite() {
        if (Utils.isDeviceOnline(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            Map map = new HashMap<>();
            map.put("Page", "FetchMicrositeDetails");
            map.put("Userid", Utils.getUserId(getActivity()));

            try {
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_MICROSITE, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Internet connection not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMircoSitePopup(String link) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("MicroSite");
        WebView wv = new WebViewHelper(getActivity());
        if (Build.VERSION.SDK_INT >= 19) {
            wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        ProgressBar progressBar = new ProgressBar(getActivity());
        wv.clearHistory();
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.loadUrl(link);

        // wv.loadUrl("http://kitever.com/BuyCredit.aspx?tab=plans&userid="
        // + UserId);
        WebClientClass webViewClient = new WebClientClass(getActivity(),progressBar);
        wv.setWebViewClient(webViewClient);
        // wv.setWebViewClient(new WebViewClient());

        alert.setView(wv);
        alert.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    private static class WebViewHelper extends WebView {
        public WebViewHelper(Context context) {
            super(context);
        }

        // Note this!
        @Override
        public boolean onCheckIsTextEditor() {
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    if (!hasFocus())
                        requestFocus();
                    break;
            }
            return super.onTouchEvent(ev);
        }
    }
}
