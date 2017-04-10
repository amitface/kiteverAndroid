package sms19.listview.newproject.MerchatStorePackage.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitever.android.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductStoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductStoreFragment extends Fragment implements TabLayout.OnTabSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String userLogin, password, userId;

    private TabLayout tabUserProduct;

    public ProductStoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3
     * @return A new instance of fragment ProductStoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductStoreFragment newInstance(String param1, String param2, String param3) {
        ProductStoreFragment fragment = new ProductStoreFragment();
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
            userId = getArguments().getString(ARG_PARAM1);
            userLogin = getArguments().getString(ARG_PARAM2);
            password = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_store, container, false);
        tabUserProduct = (TabLayout) view.findViewById(R.id.tabUserProduct);
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragmentHolder,UserProductListFragment.newInstance(userId,userLogin,password),"UserProductListFragment").commit();

        tabUserProduct.addTab(tabUserProduct.newTab().setText("Product"));
        tabUserProduct.addTab(tabUserProduct.newTab().setText("Category"));
        tabUserProduct.addTab(tabUserProduct.newTab().setText("Brand"));
        tabUserProduct.setOnTabSelectedListener(this);
        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition()==0)
        {
            changeFragment(UserProductListFragment.newInstance(userId,userLogin,password),"UserProductListFragment");
        }
        else if(tab.getPosition()==1)
        {
            changeFragment(ProductCategoryListFragment.newInstance(userId,userLogin,password),"BrandProductListFragment");
        }
        else if(tab.getPosition()==2){
            changeFragment(BrandProductListFragment.newInstance(userId,userLogin,password),"BrandProductListFragment");
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void changeFragment(Fragment fragment, String tag)
    {
        getChildFragmentManager().beginTransaction().replace(R.id.fragmentHolder,fragment,tag).commit();
    }
}
