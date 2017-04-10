package sms19.listview.newproject.MerchatStorePackage.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitever.android.R;

import sms19.listview.newproject.MerchatStorePackage.Model.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleUserProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleUserProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;
    private Product product;

    private TextView tvSingleProductName, tvSingleProductDescription, tvSingleProductPrice,
            tvSingleProductCategory;

    public SingleUserProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3
     * @param product
     * @return A new instance of fragment SingleUserProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleUserProductFragment newInstance(String param1, String param2, String param3, Product product) {
        SingleUserProductFragment fragment = new SingleUserProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putParcelable(ARG_PARAM4, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            this.product = getArguments().getParcelable(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_user_product, container, false);
        tvSingleProductName = (TextView) view.findViewById(R.id.tvSingleProductName);
        tvSingleProductDescription = (TextView) view.findViewById(R.id.tvSingleProductDescription);
        tvSingleProductPrice = (TextView) view.findViewById(R.id.tvSingleProductPrice);
        tvSingleProductCategory = (TextView) view.findViewById(R.id.tvSingleProductCategory);

        tvSingleProductName.setText(product.getProductName());
        tvSingleProductCategory.setText(product.getCategoryName());
        tvSingleProductPrice.setText("Rs "+product.getPerUnitPrice());
        tvSingleProductDescription.setText(product.getDescription());

        ChangeToolbar changeToolbar = (ChangeToolbar) getActivity();
        changeToolbar.setHome("Single Product");
        return view;
    }

    public interface ChangeToolbar {
        void setHome(String title);
    }
}
