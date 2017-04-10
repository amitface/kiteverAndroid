package sms19.listview.newproject.MerchatStorePackage.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kitever.android.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartBuyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartBuyFragment extends Fragment implements TextWatcher{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tvLabelCartBuyName, tvLabelCartBuyEmail, tvLabelCartBuyMobile,
            tvLabelCartBuyState, tvLabelCartBuyCity, tvLabelCartBuyAddress, tvLabelCartBuyRemarks;
    private EditText etCartBuyName, etCartBuyEmail, etCartBuyMobile,
            etCartBuyState, etCartBuyCity, etCartBuyAddress, etCartBuyRemarks;


    public CartBuyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartBuyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartBuyFragment newInstance(String param1, String param2) {
        CartBuyFragment fragment = new CartBuyFragment();
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
        View view =  inflater.inflate(R.layout.fragment_cart_buy, container, false);
        tvLabelCartBuyName = (TextView) view.findViewById(R.id.tvLabelCartBuyName);
        tvLabelCartBuyEmail = (TextView) view.findViewById(R.id.tvLabelCartBuyEmail);
        tvLabelCartBuyMobile = (TextView) view.findViewById(R.id.tvLabelCartBuyMobile);
        tvLabelCartBuyState = (TextView) view.findViewById(R.id.tvLabelCartBuyState);
        tvLabelCartBuyCity = (TextView) view.findViewById(R.id.tvLabelCartBuyCity);
        tvLabelCartBuyAddress = (TextView) view.findViewById(R.id.tvLabelCartBuyAddress);
        tvLabelCartBuyRemarks = (TextView) view.findViewById(R.id.tvLabelCartBuyRemarks);

        etCartBuyName = (EditText) view.findViewById(R.id.etCartBuyName);
        etCartBuyEmail = (EditText) view.findViewById(R.id.etCartBuyEmail);
        etCartBuyMobile = (EditText) view.findViewById(R.id.etCartBuyMobile);
        etCartBuyState = (EditText) view.findViewById(R.id.etCartBuyState);
        etCartBuyCity = (EditText) view.findViewById(R.id.etCartBuyCity);
        etCartBuyAddress = (EditText) view.findViewById(R.id.etCartBuyAddress);
        etCartBuyRemarks = (EditText) view.findViewById(R.id.etCartBuyRemarks);

        etCartBuyName.addTextChangedListener(this);
        etCartBuyEmail.addTextChangedListener(this);
        etCartBuyMobile.addTextChangedListener(this);
        etCartBuyState.addTextChangedListener(this);
        etCartBuyCity.addTextChangedListener(this);
        etCartBuyAddress.addTextChangedListener(this);
        etCartBuyRemarks.addTextChangedListener(this);

        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str = s.toString();
        if(etCartBuyName.getText().hashCode() == s.hashCode())
        {
            if(str.length()>0)
                tvLabelCartBuyName.setText("Name");
            else
                tvLabelCartBuyName.setText("");
        }
        else if(etCartBuyEmail.getText().hashCode()==s.hashCode())
        {
            if(str.length()>0)
                tvLabelCartBuyEmail.setText("Email");
            else
                tvLabelCartBuyEmail.setText("");
        }
        else if(etCartBuyMobile.getText().hashCode() == s.hashCode())
        {
            if(str.length()>0)
                tvLabelCartBuyMobile.setText("City");
            else
                tvLabelCartBuyMobile.setText("");
        }
        else if(etCartBuyState.getText().hashCode() == s.hashCode())
        {
            if(str.length()>0)
                tvLabelCartBuyState.setText("State");
            else
                tvLabelCartBuyState.setText("");
        }
        else if(etCartBuyCity.getText().hashCode()==s.hashCode())
        {
            if(str.length()>0)
                tvLabelCartBuyCity.setText("City");
            else
                tvLabelCartBuyCity.setText("");
        }
        else if(etCartBuyAddress.getText().hashCode()==s.hashCode())
        {
            if(str.length()>0)
                tvLabelCartBuyAddress.setText("Address");
            else
                tvLabelCartBuyAddress.setText("");
        }
        else if(etCartBuyRemarks.getText().hashCode() == s.hashCode())
        {
            if(str.length()>0)
                tvLabelCartBuyRemarks.setText("Remarks");
            else
                tvLabelCartBuyRemarks.setText("");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
