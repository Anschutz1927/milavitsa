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
import by.black_pearl.vica.adapters.expandable.AdapterProductType;
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
    private ViewPager mTopPager;
    private ViewPager mBottomPager;
    private int mId = 0;
    private Spinner mSpinner;
    private Realm mRealm;

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
        mRealm = Realm.getDefaultInstance();
        mMenuId = mRealm.where(ProductSeriesDb.class).equalTo("Id", mId).findFirst().getIdMenu();
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
        mTopPager.addOnPageChangeListener(getOnPageChangeListener());
        loadSpinner();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.ToolbarManager.getToolbarManager().removeView(mSpinner);
    }

    private ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int type = ((AdapterProductType) mTopPager.getAdapter()).getType(position);
                if (type != 1 && mBottomPager.getVisibility() == View.VISIBLE) {
                    mBottomPager.setVisibility(View.GONE);
                }
                else if (type == 1 && mBottomPager.getVisibility() == View.GONE && mProductsType2.size() != 0) {
                    mBottomPager.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
    }

    private void loadSpinner() {
        //this.mSpinner = new Spinner(getContext());
        this.mSpinner = new Spinner(MainActivity.ToolbarManager.getToolbarManager().getThemedContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
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
                }
                else {
                    mTopPager.setVisibility(View.GONE);
                }
                if (mProductsType2.size() != 0) {
                    mBottomPager.setAdapter(new AdapterProductType(getChildFragmentManager(), mProductsType2));
                    if (((AdapterProductType) mTopPager.getAdapter()).getType(mTopPager.getCurrentItem()) == 1) {
                        mBottomPager.setVisibility(View.VISIBLE);
                    }
                    else {
                        mBottomPager.setVisibility(View.GONE);
                    }
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
        ArrayList<ProductDb> productsType3 = new ArrayList<>();
        ArrayList<ProductDb> productsType4 = new ArrayList<>();
        int posType1 = 0;
        int posType3 = 0;
        int posType4 = 0;
        RealmResults<ProductDb> products = mRealm.where(ProductDb.class).equalTo("IdRubric", id).findAll();
        for (ProductDb product : products) {
            switch (product.getType()) {
                case 1:
                    mProductsType1.add(posType1, product);
                    break;
                case 2:
                    mProductsType2.add(product);
                    break;
                case 3:
                    productsType3.add(posType3, product);
                    break;
                case 4:
                    productsType4.add(posType4, product);
                    break;
            }
        }
        mProductsType1.addAll(productsType3);
        mProductsType1.addAll(productsType4);
    }
}
