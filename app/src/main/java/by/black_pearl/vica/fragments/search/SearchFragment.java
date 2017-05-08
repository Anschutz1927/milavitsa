package by.black_pearl.vica.fragments.search;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.MainActivity;
import by.black_pearl.vica.adapters.search.FindAdapter;
import by.black_pearl.vica.realm_db.ColorsDb;
import by.black_pearl.vica.realm_db.ConstructionTypesDb;
import by.black_pearl.vica.realm_db.ConstructionsDb;
import by.black_pearl.vica.realm_db.ProductDb;
import by.black_pearl.vica.realm_db.ProductsParamsDb;
import by.black_pearl.vica.realm_db.SizesDb;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SearchFragment extends Fragment {

    private FindAdapter mFindAdapter;
    private Realm mRealm;
    private ArrayList<String> mSizes;
    private ArrayList<String> mColors;
    private ArrayList<String> mConstructions;
    private ArrayList<String> mConstructionTypes;
    private CoordinatorLayout mContainerCl;
    private View mToolbarParentView;

    public SearchFragment() {
    }

    /**
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mFindAdapter = new FindAdapter(getContext());
        mRealm = Realm.getDefaultInstance();
        mSizes = new ArrayList<>();
        mColors = new ArrayList<>();
        mConstructions = new ArrayList<>();
        mConstructionTypes = new ArrayList<>();
        RealmResults<SizesDb> sizesResult = mRealm.where(SizesDb.class).findAll();
        RealmResults<ColorsDb> colorsResult = mRealm.where(ColorsDb.class).findAll();
        RealmResults<ConstructionsDb> constructionsResult = mRealm.where(ConstructionsDb.class).findAll();
        RealmResults<ConstructionTypesDb> constructionTypesResult = mRealm.where(ConstructionTypesDb.class).findAll();
        mSizes.add("Выбрать размер");
        for (SizesDb sizesDb : sizesResult) {
            mSizes.add(sizesDb.getSize());
        }
        mColors.add("Выбрать цвет");
        for (ColorsDb colorsDb : colorsResult) {
            mColors.add(colorsDb.getColor());
        }
        mConstructions.add("Выбрать конструкцию");
        for (ConstructionsDb constructionsDb : constructionsResult) {
            mConstructions.add(constructionsDb.getConstruction());
        }
        mConstructionTypes.add("Выбрать тип конструкции");
        for (ConstructionTypesDb constructionsTypesDb : constructionTypesResult) {
            mConstructionTypes.add(constructionsTypesDb.getConstruction_type());
        }
        setupToolbarTv();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.ToolbarManager.getToolbarManager().setView(mToolbarParentView);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mContainerCl = (CoordinatorLayout) view.findViewById(R.id.cl_container);
        RecyclerView findRv = (RecyclerView) view.findViewById(R.id.rv_find);
        findRv.setLayoutManager(new LinearLayoutManager(getContext()));
        final EditText searchModel = (EditText) view.findViewById(R.id.et_search_model);
        searchModel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    closeKeyboard(getActivity());
                    return true;
                }
                return false;
            }
        });
        final Spinner searchSize = (Spinner) view.findViewById(R.id.spnr_size);
        final Spinner searchColor = (Spinner) view.findViewById(R.id.spnr_color);
        final Spinner searchConstruction = (Spinner) view.findViewById(R.id.spnr_constr);
        final Spinner searchConstrType = (Spinner) view.findViewById(R.id.spnr_constr_type);
        searchSize.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mSizes));
        searchColor.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mColors));
        searchConstruction.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mConstructions));
        searchConstrType.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mConstructionTypes));
        FloatingActionButton fabFind = (FloatingActionButton) view.findViewById(R.id.fab_search);
        fabFind.setOnClickListener(getFabClickListener(searchModel, searchSize, searchColor,
                searchConstruction, searchConstrType));
        mToolbarParentView.findViewById(R.id.iv_toolbar_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchModel.setText("");
                searchSize.setSelection(0);
                searchConstruction.setSelection(0);
                searchColor.setSelection(0);
                searchConstrType.setSelection(0);
                mFindAdapter.removeData();
            }
        });
        findRv.setAdapter(this.mFindAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        MainActivity.ToolbarManager.getToolbarManager().removeView(mToolbarParentView);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private void setupToolbarTv() {
        mToolbarParentView = LayoutInflater.from(getContext())
                .inflate(R.layout.view_toolbar_search, MainActivity.ToolbarManager.getToolbarManager().getToolbarLayout(), false);
        ((TextView) mToolbarParentView.findViewById(R.id.tv_toolbar_txt)).setText(R.string.app_name);
    }

    private void closeKeyboard(Activity activity) {
        if (null != activity && null != activity.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != mInputMethodManager) {
                mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public View.OnClickListener getFabClickListener(final EditText searchModel, final Spinner size,
                                                    final Spinner color, final Spinner construction,
                                                    final Spinner constr_type) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(getActivity());
                if (!checkedFindInfo(searchModel.getText().toString(), size, color, construction, constr_type)) {
                    showErrorMessage(v);
                    return;
                }
                showProgressBar();
                final String modelString = searchModel.getText().toString();
                String sizeString = size.getSelectedItemPosition() == 0 ? "" : size.getSelectedItem().toString();
                String colorString = color.getSelectedItemPosition() == 0 ? "" : color.getSelectedItem().toString();
                String constructionString = construction.getSelectedItemPosition() == 0 ?
                        "" : construction.getSelectedItem().toString();
                String constr_typeString = constr_type.getSelectedItemPosition() == 0 ?
                        "" : constr_type.getSelectedItem().toString();
                RealmQuery<ProductsParamsDb> prodParamsQuery = buildProdParamsQuery(mRealm, sizeString, colorString,
                        constructionString, constr_typeString);
                final RealmResults<ProductsParamsDb> productsParams = prodParamsQuery
                        .distinctAsync(ProductsParamsDb.COLUMN_MODEL);
                productsParams.addChangeListener(new RealmChangeListener<RealmResults<ProductsParamsDb>>() {
                    @Override
                    public void onChange(RealmResults<ProductsParamsDb> element) {
                        String[] irrhStrings = convertRealmResultToStringArray(productsParams);
                        RealmQuery<ProductDb> productQuery = buildProductQuery(mRealm, modelString, irrhStrings);
                        final RealmResults<ProductDb> products = productQuery.findAllAsync();
                        products.addChangeListener(new RealmChangeListener<RealmResults<ProductDb>>() {
                            @Override
                            public void onChange(RealmResults<ProductDb> element) {
                                if(products.size() != 0) {
                                    mFindAdapter.changeData(products.sort(ProductDb.COLUMN_ARTICLE));
                                    showMsgs("Найдено " + String.valueOf(products.size()) + ".");
                                }
                                else {
                                    showMsgs("Ниего не найдено ;(");
                                    mFindAdapter.changeData(null);
                                }
                                products.removeAllChangeListeners();
                            }
                        });
                        productsParams.removeAllChangeListeners();
                    }
                });

            }
        };
    }

    private boolean checkedFindInfo(String searchModel, Spinner size, Spinner color, Spinner construction, Spinner constr_type) {
        if (searchModel.equals("") || searchModel.equals(" ")) {
            if (size.getSelectedItemPosition() == 0 && color.getSelectedItemPosition() == 0 &&
                    construction.getSelectedItemPosition() == 0 && constr_type.getSelectedItemPosition() == 0) {
                return false;
            }
        }
        return true;
    }

    private void showErrorMessage(View v) {
        Snackbar.make(v, "Нет критериев поиска!", Snackbar.LENGTH_SHORT).show();
        mFindAdapter.changeData(null);
    }

    private void showProgressBar() {
        Snackbar snackbar = Snackbar.make(mContainerCl, "Выполняется поиск...", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout sbLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        ProgressBar pb = new ProgressBar(getContext());
        Snackbar.SnackbarLayout.LayoutParams params = new Snackbar.SnackbarLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.END;
        pb.setLayoutParams(params);
        sbLayout.addView(pb);
        snackbar.show();
    }

    private void showMsgs(String msg) {
        Snackbar.make(mContainerCl, msg, Snackbar.LENGTH_SHORT).show();
    }

    private static RealmQuery<ProductsParamsDb> buildProdParamsQuery(Realm realm, String sizeString, String colorSring,
                                                                     String constructionString, String constrTypeString) {
        RealmQuery<ProductsParamsDb> paramsQuery = realm.where(ProductsParamsDb.class);
        if (!sizeString.equals("")) {
            int sizeId = realm.where(SizesDb.class).equalTo(SizesDb.COLUMN_SIZE, sizeString).findFirst().getId();
            paramsQuery = paramsQuery.equalTo(ProductsParamsDb.COLUMN_SIZE_ID, sizeId);
        }
        if (!colorSring.equals("")) {
            int colorId = realm.where(ColorsDb.class).equalTo(ColorsDb.COLUMN_COLOR, colorSring)
                    .findFirst().getId();
            paramsQuery.equalTo(ProductsParamsDb.COLUMN_COLOR_ID, colorId);
        }
        if (!constructionString.equals("")) {
            int constructionId = realm.where(ConstructionsDb.class)
                    .equalTo(ConstructionsDb.COLUMN_CONSTRUCTION, constructionString).findFirst().getId();
            paramsQuery = paramsQuery.equalTo(ProductsParamsDb.COLUMN_CONSTRUCTION_ID, constructionId);
        }
        if (!constrTypeString.equals("")) {
            int constrTypeId = realm.where(ConstructionTypesDb.class)
                    .equalTo(ConstructionTypesDb.COLUMN_CONSTRUCTION_TYPE, constrTypeString).findFirst().getId();
            paramsQuery = paramsQuery.equalTo(ProductsParamsDb.COLUMN_CONSTRUCTION_TYPE_ID, constrTypeId);
        }
        return paramsQuery;
    }

    private static RealmQuery<ProductDb> buildProductQuery(Realm realm, String modelString, String[] irrhStrings) {
        RealmQuery<ProductDb> productQuery = realm.where(ProductDb.class);
        if (!modelString.equals("")) {
            productQuery.contains(ProductDb.COLUMN_ARTICLE, modelString);
        }
        productQuery = productQuery.in(ProductDb.COLUMN_ARTICLE, irrhStrings);
        return productQuery;
    }

    private static String[] convertRealmResultToStringArray(RealmResults<ProductsParamsDb> productsParamsDbs) {
        String[] paramsStrings;
        if (productsParamsDbs.size() != 0) {
            paramsStrings = new String[productsParamsDbs.size()];
            for (int i = 0; i < productsParamsDbs.size(); i++) {
                paramsStrings[i] = String.valueOf(productsParamsDbs.get(i).getModel());
            }
        } else {
            paramsStrings = new String[]{""};
        }
        return paramsStrings;
    }
}
