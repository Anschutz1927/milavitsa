package by.black_pearl.vica.activities;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import by.black_pearl.vica.R;
import by.black_pearl.vica.fragments.CollectionRecyclerFragment;
import by.black_pearl.vica.fragments.CompactFragment;
import by.black_pearl.vica.fragments.UpgradeFragment;
import by.black_pearl.vica.fragments.expandable.ExpandableCollectionsFragment;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initialization();
        mRealm = Realm.getDefaultInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ExpandableCollectionsFragment.newInstance();
        fragmentTransaction.add(R.id.content_main, fragment);
        //fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();

        //Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //spinner.setAdapter(getSpinnerAdapter());
    }

    private void initialization() {
        Realm.init(this);
        FragmentChanger.init(getSupportFragmentManager());
        ToolbarManager.init(this);
    }

    private SpinnerAdapter getSpinnerAdapter() {
        return new SpinnerAdapter() {

            public final String[] items = new String[] {"Коллекции", "Серии", "Продукты"};

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                }
                ((TextView) convertView).setText(items[position].toString());
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
                return items.length;
            }

            @Override
            public Object getItem(int position) {
                return items[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) View.inflate(MainActivity.this, android.R.layout.simple_spinner_item, null);
                textView.setText(items[position].toString());
                return textView;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //clearBackStack();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.nav_start_download:
                fragmentTransaction.replace(R.id.content_main, UpgradeFragment.newInstance());
                break;
            case R.id.nav_start_catalog:
                fragment = CollectionRecyclerFragment.newInstance();
                fragmentTransaction.replace(R.id.content_main, fragment);
                break;
            case R.id.nav_compact:
                fragment = CompactFragment.newInstance();
                FragmentChanger changer = FragmentChanger.getFragmentChanger();
                changer.changeFragment(fragment, true);
                break;
            case R.id.nav_expandable:
                fragment = ExpandableCollectionsFragment.newInstance();
                FragmentChanger.getFragmentChanger().changeFragment(fragment, true);
                break;
        }
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class FragmentChanger {
        private static FragmentChanger changer;
        private final FragmentManager mManager;

        private FragmentChanger(FragmentManager fragmentManager) {
            this.mManager = fragmentManager;
        }

        public void changeFragment(Fragment fragment, boolean isAddToBackStack) {
            FragmentTransaction fragmentTransaction = mManager.beginTransaction();
            if(isAddToBackStack) {
                fragmentTransaction.addToBackStack(fragment.getClass().getName());
            }
            fragmentTransaction.replace(R.id.content_main, fragment);
            fragmentTransaction.commit();
        }

        private void clearBackStack() {
            for (int i = 0; i < mManager.getBackStackEntryCount(); i++) {
                FragmentManager.BackStackEntry first = mManager.getBackStackEntryAt(0);
                mManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }

        private static void init(FragmentManager fragmentManager) {
            changer = new FragmentChanger(fragmentManager);
        }

        public static FragmentChanger getFragmentChanger() {
            return changer;
        }
    }

    public static class ToolbarManager {
        private static ToolbarManager toolbarManager;
        private final FrameLayout mToolbarLayout;

        private ToolbarManager(Toolbar toolbar) {
            mToolbarLayout = (FrameLayout) toolbar.findViewById(R.id.toolbarLayout);
        }

        public void addView(View view) {
            mToolbarLayout.addView(view);
        }

        public void removeView(View view) {
            mToolbarLayout.removeView(view);
        }

        public static void init(MainActivity activity) {
            toolbarManager = new ToolbarManager((Toolbar) activity.findViewById(R.id.toolbar));
        }

        public static ToolbarManager getToolbarManager() {
            return toolbarManager;
        }
    }
}
