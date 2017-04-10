package sms19.listview.newproject.MerchatStorePackage.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kitever.android.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link zProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class zProgressFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public zProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment zProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static zProgressFragment newInstance(String param1, String param2) {
        zProgressFragment fragment = new zProgressFragment();
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
        View view = inflater.inflate(R.layout.fragment_z_progress, container, false);
        RelativeLayout layoutZProgress = (RelativeLayout) view.findViewById(R.id.layoutZProgress);
        layoutZProgress.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
//        Toast.makeText(getActivity(),"progress clicked",Toast.LENGTH_SHORT).show();
    }
}
