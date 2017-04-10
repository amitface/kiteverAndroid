package sms19.listview.newproject.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
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
import sms19.listview.newproject.model.FetchMicroSiteModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MicroSiteView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MicroSiteView extends Fragment implements NetworkManager, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int KEY_FETCH_MICROSITE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressBar progressbarMicroSiteView;
    private WebView webView;
    private String pageName;
    private TextView tvSiteLeads;
    private TextView tvSiteVisits;
    private FetchMicroSiteModel fetchMicroSiteModel = null;
    private Button btnCreateMicroSiteButton;

    public MicroSiteView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MicroSiteView.
     */
    // TODO: Rename and change types and number of parameters
    public static MicroSiteView newInstance(String param1, String param2) {
        MicroSiteView fragment = new MicroSiteView();
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
        View view = inflater.inflate(R.layout.fragment_micro_site_view, container, false);
        tvSiteLeads = (TextView) view.findViewById(R.id.tvSiteLeads);
        tvSiteVisits = (TextView) view.findViewById(R.id.tvSiteVisits);
        webView = (WebView) view.findViewById(R.id.webviewMicroSiteView);
        progressbarMicroSiteView = (ProgressBar) view.findViewById(R.id.progressbarMicroSiteView);

        btnCreateMicroSiteButton = (Button) view.findViewById(R.id.btnCreateMicroSiteButton);

//        String Pass = Utils.getUserId(getActivity());
        requestFetchMicroSite();
        return view;
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (requestId == KEY_FETCH_MICROSITE) {
            Gson gson = new Gson();
            FetchMicroSiteModel fetchMicroSiteModel =  gson.fromJson(response, FetchMicroSiteModel.class);
            if (fetchMicroSiteModel.getMicrositeDetails() != null && fetchMicroSiteModel.getMicrositeDetails().size() > 0) {
                pageName = fetchMicroSiteModel.getMicrositeDetails().get(0).getPagename();
//                pageName.replaceAll(" ","%20");
                setWebView(pageName);
                int visits = 0;
                int leads = 0;
                for (int i = 0; i < fetchMicroSiteModel.getMicrositeDetails().size(); i++) {
                    if (fetchMicroSiteModel.getMicrositeDetails().get(i).getSiteVisits() != null)
                        visits += Integer.parseInt(fetchMicroSiteModel.getMicrositeDetails().get(i).getSiteVisits());
                    if (fetchMicroSiteModel.getMicrositeDetails().get(i).getSiteLeads() != null)
                        leads += Integer.parseInt(fetchMicroSiteModel.getMicrositeDetails().get(i).getSiteLeads());
                }
                tvSiteVisits.setText("Visits : " + visits);
                tvSiteLeads.setText("Leads : " + leads);
            }else if(fetchMicroSiteModel.getMicrositeDetails() == null)
            {
                btnCreateMicroSiteButton.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                btnCreateMicroSiteButton.setOnClickListener(MicroSiteView.this);
            }
        }
    }

    private void setWebView(String pageName) {
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(pageName);

        // wv.loadUrl("http://kitever.com/BuyCredit.aspx?tab=plans&userid="
        // + UserId);
        WebClientClass webViewClient = new WebClientClass(getActivity(), progressbarMicroSiteView);
        webView.setWebViewClient(webViewClient);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
    }

    private void requestFetchMicroSite() {
        if (Utils.isDeviceOnline(getActivity())) {

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



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnCreateMicroSiteButton:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_micro_site, MicroSiteCreate.newInstance("","")).commit();
                break;
        }
    }
}
