package by.black_pearl.vica.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.backendless.Backendless;

import by.black_pearl.vica.R;
import by.black_pearl.vica.fragments.addresses.AddressesFragment;
import by.black_pearl.vica.fragments.expandable.ExpandableCollectionsFragment;
import by.black_pearl.vica.fragments.search.SearchFragment;
import by.black_pearl.vica.fragments.updater.UpgradeFragment;
import by.black_pearl.vica.realm_db.CollectionsDb;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String sBackendlessAppId = "C68F0999-9946-4158-FF9F-D3E41DCBFD00";
    private static final String sBackendlessKey = "AE240C33-A9D3-AA40-FF5C-ED1B88588700";
    private static final int PERMISSIONS_REQUEST_CODE = 0;

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
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initialization();
        if (savedInstanceState == null) {
            mRealm = Realm.getDefaultInstance();
            if (mRealm.where(CollectionsDb.class).count() > 0) {
                FragmentChanger.getFragmentChanger().addFragmentOnStart(ExpandableCollectionsFragment.newInstance());
            } else {
                FragmentChanger.getFragmentChanger().addFragmentOnStart(UpgradeFragment.newInstance());
            }
        }
    }

    private void initialization() {
        Realm.init(getApplicationContext());
        Backendless.initApp(this, sBackendlessAppId, sBackendlessKey);
        FragmentChanger.init(getSupportFragmentManager());
        Context themedContext = getSupportActionBar().getThemedContext() != null ?
                getSupportActionBar().getThemedContext() : getBaseContext();
        ToolbarManager.init(themedContext, (Toolbar) findViewById(R.id.toolbar));
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (FragmentChanger.getFragmentChanger().isUpgradeFragment()) {
            Toast.makeText(this, "Пожалуйста, подождите окончания загрузки!", Toast.LENGTH_LONG).show();
            return true;
        }
        switch (item.getItemId()) {
            case R.id.nav_start_download:
                FragmentChanger.getFragmentChanger().addFragmentOnStart(UpgradeFragment.newInstance());
                break;
            case R.id.nav_expandable:
                FragmentChanger.getFragmentChanger().addFragmentOnStart(ExpandableCollectionsFragment.newInstance());
                break;
            case R.id.nav_search:
                FragmentChanger.getFragmentChanger().clearBackStack();
                FragmentChanger.getFragmentChanger().changeFragment(SearchFragment.newInstance(), true);
                break;
            case R.id.nav_slide_show:
                startActivity(new Intent(this, SlideshowActivity.class));
                break;
            case R.id.nav_addresses:
                if (!isPremissionsAllowed()) {
                    Toast.makeText(this, "Нет доступа к местоположению или права на использование " +
                            "местоположения не предоставлены!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                FragmentChanger.getFragmentChanger().clearBackStack();
                FragmentChanger.getFragmentChanger().changeFragment(AddressesFragment.newInstance(), true);
                break;
            case R.id.nav_settings:
                //startActivity(new Intent(this, SettingsActivity.class));
                startActivity(new Intent(this, TempSettingsActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isPremissionsAllowed() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    },
                    PERMISSIONS_REQUEST_CODE
            );
            return false;
        }
        return true;
    }

    public static class FragmentChanger {
        private static final String BACKSTACK = "backstack";
        private static FragmentChanger changer;
        private final FragmentManager mManager;
        private Fragment mStartFragment;

        private FragmentChanger(FragmentManager fragmentManager) {
            this.mManager = fragmentManager;
        }

        public void addFragmentOnStart(Fragment startFragment) {
            if(mStartFragment != null) {
                clearBackStack();
                //mManager.beginTransaction().remove(mStartFragment).commit();
                mManager.beginTransaction().remove(mStartFragment).commitAllowingStateLoss();
            }
            this.mStartFragment = startFragment;
            //mManager.beginTransaction().add(R.id.content_main, startFragment).commit();
            mManager.beginTransaction().add(R.id.content_main, startFragment).commitAllowingStateLoss();
        }

        public void changeFragment(Fragment fragment, boolean addToBackStack) {
            FragmentTransaction fragmentTransaction = mManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment);
            if(addToBackStack) {
                fragmentTransaction.addToBackStack(BACKSTACK);
            }
            fragmentTransaction.commit();
        }

        public void clearBackStack() {
            int n = mManager.getBackStackEntryCount();
            for (int i = 0; i < n; i++) {
                mManager.popBackStackImmediate();
            }
        }

        boolean isUpgradeFragment() {
            return mStartFragment.getClass().getName() == UpgradeFragment.class.getName();
        }

        private static void init(FragmentManager supportFragmentManager) {
            changer = new FragmentChanger(supportFragmentManager);
        }

        public static FragmentChanger getFragmentChanger() {
            return changer;
        }
    }

    public static class ToolbarManager {
        private static ToolbarManager toolbarManager;
        private final FrameLayout mToolbarLayout;
        private final Context mContext;

        private ToolbarManager(Context themedContext, Toolbar toolbar) {
            mToolbarLayout = (FrameLayout) toolbar.findViewById(R.id.toolbarLayout);
            mContext = themedContext;
        }

        public void setView(View view) {
            if (mToolbarLayout.getChildCount() != 0) {
                mToolbarLayout.removeAllViews();
            }
            mToolbarLayout.addView(view);
        }

        public void removeView(View view) {
            mToolbarLayout.removeView(view);
        }

        static void init(Context themedContext, Toolbar toolbar) {
            toolbarManager = new ToolbarManager(themedContext, toolbar);
        }

        public static ToolbarManager getToolbarManager() {
            return toolbarManager;
        }

        public Context getThemedContext() {
            return toolbarManager.mContext;
        }

        public void removeViews() {
            mToolbarLayout.removeAllViews();
        }

        public FrameLayout getToolbarLayout() {
            return mToolbarLayout;
        }
    }
}
