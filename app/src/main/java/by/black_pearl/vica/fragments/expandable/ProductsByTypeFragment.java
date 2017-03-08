package by.black_pearl.vica.fragments.expandable;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.MainActivity;
import by.black_pearl.vica.adapters.AdapterProductType;
import by.black_pearl.vica.adapters.expandable.SeriesSpinnerAdapter;
import by.black_pearl.vica.realm_db.ProductDb;
import by.black_pearl.vica.realm_db.ProductSeriesDb;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsByTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsByTypeFragment extends Fragment {

    private static final String ID = "id";
    private int mMenuId = 0;
    private ArrayList<ProductDb> mProductsType1 = new ArrayList<>();
    private ArrayList<ProductDb> mProductsType2 = new ArrayList<>();
    private ArrayList<ProductDb> mProductsType3 = new ArrayList<>();
    private ArrayList<ProductDb> mProductsType4 = new ArrayList<>();
    private ViewPager mTopPager;
    private ViewPager mBottomPager;
    private int mId = 0;
    private Spinner mSpinner;

    public ProductsByTypeFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProductsByTypeFragment.
     * @param id
     */
    public static ProductsByTypeFragment newInstance(int id) {
        ProductsByTypeFragment fragment = new ProductsByTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mId = getArguments().getInt(ID);
        }
        setRetainInstance(true);
        Realm realm = Realm.getDefaultInstance();
        mMenuId = realm.where(ProductSeriesDb.class).equalTo("Id", mId).findFirst().getIdMenu();
        loadProducts(mId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_by_type, container, false);
        mTopPager = (ViewPager) view.findViewById(R.id.fragment_products_by_type_topPager);
        mBottomPager = (ViewPager) view.findViewById(R.id.fragment_products_by_type_bottomPager);
        mTopPager.setClipToPadding(false);
        mTopPager.setPageMargin(6);
        mBottomPager.setClipToPadding(false);
        mBottomPager.setPageMargin(6);
        loadSpinner();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.ToolbarManager.getToolbarManager().removeView(mSpinner);
    }

    private void loadSpinner() {
        this.mSpinner = new Spinner(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        params.setMargins(0, 0, 0, 10);
        this.mSpinner.setLayoutParams(params);
        MainActivity.ToolbarManager.getToolbarManager().addView(mSpinner);
        SeriesSpinnerAdapter adapter = new SeriesSpinnerAdapter(getContext(), mMenuId);
        this.mSpinner.setAdapter(adapter);
        this.mSpinner.setSelection(adapter.getCurrentPosition(mId));
        this.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadProducts(id);
                if (mProductsType1.size() != 0) {
                    mTopPager.setVisibility(View.VISIBLE);
                    AdapterProductType adapter1 = new AdapterProductType(getChildFragmentManager(), mProductsType1);
                    mTopPager.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                }
                else {
                    mTopPager.setVisibility(View.GONE);
                }
                if (mProductsType2.size() != 0) {
                    mBottomPager.setVisibility(View.VISIBLE);
                    mBottomPager.setAdapter(new AdapterProductType(getChildFragmentManager(), mProductsType2));
                }
                else {
                    mBottomPager.setVisibility(View.GONE);
                }
                if (mTopPager.getVisibility() == View.VISIBLE || mBottomPager.getVisibility() == View.VISIBLE) {
                    return;
                }
                if(mProductsType3.size() != 0) {
                    mTopPager.setVisibility(View.VISIBLE);
                    mTopPager.setAdapter(new AdapterProductType(getChildFragmentManager(), mProductsType3));
                    return;
                }
                if(mProductsType4.size() != 0) {
                    mTopPager.setVisibility(View.VISIBLE);
                    mTopPager.setAdapter(new AdapterProductType(getChildFragmentManager(), mProductsType4));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadProducts(long id) {
        mId = (int) id;
        mProductsType1.clear();
        mProductsType2.clear();
        mProductsType3.clear();
        mProductsType4.clear();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ProductDb> products = realm.where(ProductDb.class).equalTo("IdRubric", id).findAll();
        for (ProductDb product : products) {
            switch (product.getType()) {
                case 1:
                    mProductsType1.add(product);
                    break;
                case 2:
                    mProductsType2.add(product);
                    break;
                case 3:
                    mProductsType3.add(product);
                    break;
                case 4:
                    mProductsType4.add(product);
                    break;
            }
        }
    }
}
