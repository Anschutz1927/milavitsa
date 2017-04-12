package by.black_pearl.vica.fragments.search;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.MainActivity;
import by.black_pearl.vica.adapters.search.FindAdapter;
import by.black_pearl.vica.realm_db.ConstructionTypesDb;
import by.black_pearl.vica.realm_db.ConstructionsDb;
import by.black_pearl.vica.realm_db.ModelIrrhDb;
import by.black_pearl.vica.realm_db.ProductDb;
import by.black_pearl.vica.realm_db.SizesDb;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SearchFragment extends Fragment {

    private FindAdapter mFindAdapter;
    private Realm mRealm;
    private ArrayList<String> mSizes;
    private ArrayList<String> mConstructions;
    private ArrayList<String> mConstructionTypes;
    private CoordinatorLayout mContainerCl;
    private TextView mToolbarTv;

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
        mConstructions = new ArrayList<>();
        mConstructionTypes = new ArrayList<>();
        RealmResults<SizesDb> sizesResult = mRealm.where(SizesDb.class).findAll();
        RealmResults<ConstructionsDb> constructionsResult = mRealm.where(ConstructionsDb.class).findAll();
        RealmResults<ConstructionTypesDb> constructionTypesResult = mRealm.where(ConstructionTypesDb.class).findAll();
        mSizes.add("Выбрать размер");
        for (SizesDb sizesDb : sizesResult) {
            mSizes.add(sizesDb.getSize());
        }
        mConstructions.add("Выбрать конструкцию");
        for (ConstructionsDb constructionsDb : constructionsResult) {
            mConstructions.add(constructionsDb.getConstruction());
        }
        mConstructionTypes.add("Выбрать тип конструкции");
        for (ConstructionTypesDb constructionsTypesDb : constructionTypesResult) {
            mConstructionTypes.add(constructionsTypesDb.getConstructionType());
        }
        setupToolbarTv();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.ToolbarManager.getToolbarManager().addView(mToolbarTv);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mContainerCl = (CoordinatorLayout) view.findViewById(R.id.cl_container);
        RecyclerView findRv = (RecyclerView) view.findViewById(R.id.rv_find);
        findRv.setLayoutManager(new LinearLayoutManager(getContext()));
        EditText searchModel = (EditText) view.findViewById(R.id.et_search_model);
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
        Spinner searchSize = (Spinner) view.findViewById(R.id.spnr_size);
        Spinner searchConstruction = (Spinner) view.findViewById(R.id.spnr_constr);
        Spinner searchConstrType = (Spinner) view.findViewById(R.id.spnr_constr_type);
        searchSize.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mSizes));
        searchConstruction.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mConstructions));
        searchConstrType.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mConstructionTypes));
        FloatingActionButton fabFind = (FloatingActionButton) view.findViewById(R.id.fab_search);
        fabFind.setOnClickListener(getFabClickListener(searchModel, searchSize, searchConstruction, searchConstrType));
        findRv.setAdapter(this.mFindAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.ToolbarManager.getToolbarManager().removeView(mToolbarTv);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private void setupToolbarTv() {
        mToolbarTv = new TextView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        mToolbarTv.setLayoutParams(params);
        mToolbarTv.setText(R.string.app_name);
    }

    private void closeKeyboard(Activity activity) {
        if (null != activity && null != activity.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != mInputMethodManager) {
                mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public View.OnClickListener getFabClickListener(final EditText searchModel,
                                                    final Spinner size, final Spinner construction, final Spinner constr_type) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(getActivity());
                if (!checkedFindInfo(searchModel.getText().toString(), size, construction, constr_type)) {
                    showErrorMessage(v);
                    return;
                }
                showProgressBar();
                final String modelString = searchModel.getText().toString();
                String sizeString = size.getSelectedItemPosition() == 0 ? "" : size.getSelectedItem().toString();
                String constructionString = construction.getSelectedItemPosition() == 0 ?
                        "" : construction.getSelectedItem().toString();
                String constr_typeString = constr_type.getSelectedItemPosition() == 0 ?
                        "" : constr_type.getSelectedItem().toString();
                RealmQuery<ModelIrrhDb> irrhQuery = buidModelIrrhQuery(mRealm, sizeString, constructionString, constr_typeString);
                final RealmResults<ModelIrrhDb> modelIrrhs = irrhQuery.distinctAsync("model");
                modelIrrhs.addChangeListener(new RealmChangeListener<RealmResults<ModelIrrhDb>>() {
                    @Override
                    public void onChange(RealmResults<ModelIrrhDb> element) {
                        String[] irrhStrings = convertRealmResultToStringArray(modelIrrhs);
                        RealmQuery<ProductDb> productQuery = buildProductQuery(mRealm, modelString, irrhStrings);
                        final RealmResults<ProductDb> products = productQuery.findAllAsync();
                        products.addChangeListener(new RealmChangeListener<RealmResults<ProductDb>>() {
                            @Override
                            public void onChange(RealmResults<ProductDb> element) {
                                if(products.size() != 0) {
                                    mFindAdapter.changeData(products.sort("Article"));
                                    showMsgs("Найдено " + String.valueOf(products.size()) + ".");
                                }
                                else {
                                    showMsgs("Ниего не найдено ;(");
                                    mFindAdapter.changeData(null);
                                }
                                products.removeAllChangeListeners();
                            }
                        });
                        modelIrrhs.removeAllChangeListeners();
                    }
                });

            }
        };
    }

    private boolean checkedFindInfo(String searchModel, Spinner size, Spinner construction, Spinner constr_type) {
        if (searchModel.equals("") || searchModel.equals(" ")) {
            if (size.getSelectedItemPosition() == 0 && construction.getSelectedItemPosition() == 0
                    && constr_type.getSelectedItemPosition() == 0) {
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

    private static RealmQuery<ModelIrrhDb> buidModelIrrhQuery(Realm realm,
                                                       String sizeString, String constructionString, String constr_typeString) {
        RealmQuery<ModelIrrhDb> irrhQuery = realm.where(ModelIrrhDb.class);
        if (!sizeString.equals("")) {
            irrhQuery = irrhQuery.equalTo("size", sizeString);
        }
        if (!constructionString.equals("")) {
            irrhQuery = irrhQuery.equalTo("construction", constructionString);
        }
        if (!constr_typeString.equals("")) {
            irrhQuery = irrhQuery.equalTo("construction_type", constr_typeString);
        }
        return irrhQuery;
    }

    private static RealmQuery<ProductDb> buildProductQuery(Realm realm, String modelString, String[] irrhStrings) {
        RealmQuery<ProductDb> productQuery = realm.where(ProductDb.class);
        if (!modelString.equals("")) {
            productQuery.contains("Article", modelString);
        }
        productQuery = productQuery.in("Article", irrhStrings);
        return productQuery;
    }

    private static String[] convertRealmResultToStringArray(RealmResults<ModelIrrhDb> modelIrrhs) {
        String[] irrhStrings ;
        if (modelIrrhs.size() != 0) {
            irrhStrings = new String[modelIrrhs.size()];
            for (int i = 0; i < modelIrrhs.size(); i++) {
                irrhStrings[i] = String.valueOf(modelIrrhs.get(i).getModel());
            }
        }
        else {
            irrhStrings = new String[]{""};
        }
        return irrhStrings;
    }

    private class OnClickAsync extends AsyncTask<Object, Object, RealmList<ProductDb>> {
        private String modelString;
        private String sizeString;
        private String constructionString;
        private String constr_typeString;

        public OnClickAsync(String modelString, String sizeString, String constructionString, String constr_typeString) {
            this.modelString = modelString;
            this.sizeString = sizeString;
            this.constructionString = constructionString;
            this.constr_typeString = constr_typeString;
        }

        @Override
        protected RealmList<ProductDb> doInBackground(Object... params) {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<ModelIrrhDb> irrhQuery =
                    buidModelIrrhQuery(realm, sizeString, constructionString, constr_typeString);
            RealmResults<ModelIrrhDb> modelIrrhs = irrhQuery.distinct("model");
            String[] irrhStrings = convertRealmResultToStringArray(modelIrrhs);
            RealmQuery<ProductDb> productQuery = buildProductQuery(realm, modelString, irrhStrings);
            RealmResults<ProductDb> products = productQuery.findAll();
            RealmList<ProductDb> result = new RealmList<>();
            for (ProductDb productDb : products) {
                result.add(productDb);
            }
            return result;
        }

        @Override
        protected void onPostExecute(RealmList<ProductDb> products) {
            if(products.size() != 0) {
                mFindAdapter.changeData(products.sort("Article"));
                showMsgs("Найдено " + String.valueOf(products.size()) + ".");
            }
            else {
                showMsgs("Ниего не найдено ;(");
                mFindAdapter.changeData(null);
            }
        }
    }
}
