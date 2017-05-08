package by.black_pearl.vica.fragments.addresses;


import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import by.black_pearl.vica.R;
import by.black_pearl.vica.realm_db.ShopsCountriesDb;
import by.black_pearl.vica.realm_db.ShopsParamsDb;
import io.realm.Realm;
import io.realm.RealmResults;

public class ShopsListFragment extends Fragment {
    private static final String USER_COUNTRY_ID = "country";
    private static final String CHOOSE_COUNTRY = "Выберите город";
    private static final String LOG_TAG = "COORDINATES::";

    private ShopListRvAdapter mShopListRvAdapter;
    private CountryListSpnrAdapter mCountryListSpnrAdapter;
    private Realm mRealm;
    private RealmResults<ShopsCountriesDb> mShopsCountriesDbs;
    private RealmResults<ShopsParamsDb> mShopsParamsDbs;
    //private int mSelectedCountryId = -1;

    public ShopsListFragment() {
    }

    public static ShopsListFragment newInstance() {
        return new ShopsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        mShopsCountriesDbs = mRealm.where(ShopsCountriesDb.class).notEqualTo(ShopsCountriesDb.COLUMN_PARENT_ID, 0)
                .findAllSorted(ShopsCountriesDb.COLUMN_COUNTRY_NAME);
        mCountryListSpnrAdapter = new CountryListSpnrAdapter();
        mShopListRvAdapter = new ShopListRvAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shops_list, container, false);
        Spinner countrySpnr = (Spinner) view.findViewById(R.id.spnr_country);
        countrySpnr.setAdapter(mCountryListSpnrAdapter);
        int countryId = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext())
                .getInt(USER_COUNTRY_ID, -1);
        if (countryId != -1) {
            int selection = 0;
            for (ShopsCountriesDb shopsCountriesDb : mShopsCountriesDbs) {
                selection++;
                if (shopsCountriesDb.getId() == countryId) {
                    break;
                }
            }
            countrySpnr.setSelection(selection);
        }
        else {
            countrySpnr.setSelection(0);
        }
        countrySpnr.setOnItemSelectedListener(getItemSelectedListener());
        ImageButton sortBtn = (ImageButton) view.findViewById(R.id.btn_sort);
        sortBtn.setOnClickListener(getSortClickListener());
        RecyclerView shopListRv = (RecyclerView) view.findViewById(R.id.rv_shops);
        shopListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        shopListRv.setAdapter(mShopListRvAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /*public int getSelectedCountryId() {
        return mSelectedCountryId;
    }*/

    public AdapterView.OnItemSelectedListener getItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mShopsParamsDbs = null;
                    //mSelectedCountryId = -1;
                    PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext())
                            .edit().putInt(USER_COUNTRY_ID, -1).apply();
                    return;
                }
                ShopsCountriesDb shopsCountriesDb = mShopsCountriesDbs.where()
                        .equalTo(ShopsCountriesDb.COLUMN_ID, mShopsCountriesDbs.get(position - 1).getId()).findFirst();
                //mSelectedCountryId = shopsCountriesDb.getId();
                if (mShopsParamsDbs != null) {
                    mShopListRvAdapter.notifyItemRangeRemoved(0, mShopsParamsDbs.size());
                }
                mShopsParamsDbs = mRealm.where(ShopsParamsDb.class)
                        .equalTo(ShopsParamsDb.COLUMN_RUBRIC_ID, shopsCountriesDb.getId()).findAll();
                mShopListRvAdapter.notifyItemRangeInserted(0, mShopsParamsDbs.size());
                PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext())
                        .edit().putInt(USER_COUNTRY_ID, shopsCountriesDb.getId()).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    public View.OnClickListener getSortClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    private class ShopListRvAdapter extends RecyclerView.Adapter<ShopListRvAdapter.Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.view_shop_info, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.shopNameTv.setText(mShopsParamsDbs.get(position).getName());
            holder.shopAddressTv.setText(mShopsParamsDbs.get(position).getAddress());
            holder.shopWorkTimeTv.setText(mShopsParamsDbs.get(position).getTimeWork());
            holder.shopTelephoneTv.setText(mShopsParamsDbs.get(position).getPhone());
        }

        @Override
        public int getItemCount() {
            if (mShopsParamsDbs == null) {
                return 0;
            }
            return mShopsParamsDbs.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            private TextView shopNameTv;
            private TextView shopAddressTv;
            private TextView shopWorkTimeTv;
            private TextView shopTelephoneTv;

            Holder(View itemView) {
                super(itemView);
                shopNameTv = (TextView) itemView.findViewById(R.id.tv_shop_name);
                shopAddressTv = (TextView) itemView.findViewById(R.id.tv_address);
                shopWorkTimeTv = (TextView) itemView.findViewById(R.id.tv_work_time);
                shopTelephoneTv = (TextView) itemView.findViewById(R.id.tv_telephone);
            }
        }
    }

    private class CountryListSpnrAdapter implements SpinnerAdapter {

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            }
            if (position == 0) {
                ((TextView) convertView).setText(CHOOSE_COUNTRY);
            }
            else {
                ((TextView) convertView).setText(mShopsCountriesDbs.get(position - 1).getCountryName());
            }
            return convertView;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return mShopsCountriesDbs.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return mShopsCountriesDbs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mShopsCountriesDbs.get(position).getId();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(getContext(), android.R.layout.simple_spinner_item, null);
            if (position == 0) {
                textView.setText(CHOOSE_COUNTRY);
            }
            else {
                textView.setText(mShopsCountriesDbs.get(position - 1).getCountryName());
            }
            return textView;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}
