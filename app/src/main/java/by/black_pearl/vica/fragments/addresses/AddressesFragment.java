package by.black_pearl.vica.fragments.addresses;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import by.black_pearl.vica.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressesFragment extends Fragment {
    public static final String GEO_URL = "https://geocode-maps.yandex.ru/";
    public static final String GEO_REPOS = "1.x/";
    public static final String GEO_FORMAT = "json";
    public static final String GEO_LANG = "ru_RU";

    public AddressesFragment() {
    }

    public static AddressesFragment newInstance() {
        return new AddressesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addresses, container, false);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_addresses);
        viewPager.setAdapter(new AddressesVpAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private class AddressesVpAdapter extends FragmentPagerAdapter {
        private final static String SHOPS_LIST = "Список магазинов";
        private final static String SHOPS_MAP = "Карта";

        private ArrayList<Fragment> fragments = new ArrayList<>();
        private ArrayList<String> titles = new ArrayList<>();

        AddressesVpAdapter(FragmentManager fm) {
            super(fm);
            init();
        }

        private void init() {
            Fragment shopsList = ShopsListFragment.newInstance();
            Fragment shopsMap = ShopsMapFragment.newInstance();
            this.fragments.add(0, shopsList);
            this.fragments.add(1, shopsMap);
            this.titles.add(0, SHOPS_LIST);
            this.titles.add(1, SHOPS_MAP);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
