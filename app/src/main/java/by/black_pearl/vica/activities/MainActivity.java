package by.black_pearl.vica.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

import com.backendless.Backendless;

import java.io.File;
import java.util.Calendar;

import by.black_pearl.vica.R;
import by.black_pearl.vica.WinRarFileWorker;
import by.black_pearl.vica.fragments.expandable.ExpandableCollectionsFragment;
import by.black_pearl.vica.fragments.search.SearchFragment;
import by.black_pearl.vica.fragments.updater.UpgradeFragment;
import by.black_pearl.vica.realm_db.CollectionDb;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Realm mRealm;
    private static final String sBackendlessAppId = "C68F0999-9946-4158-FF9F-D3E41DCBFD00";
    private static final String sBackendlessKey = "AE240C33-A9D3-AA40-FF5C-ED1B88588700";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initialization();
        if (savedInstanceState == null) {
            mRealm = Realm.getDefaultInstance();
            if (mRealm.where(CollectionDb.class).findAll().size() > 0) {
                //if(fileUpdateIsOld()) {
                //    FragmentChanger.getFragmentChanger().addFragmentOnStart(OstDownloaderFragment.newInstance());
                // }
                //else {
                FragmentChanger.getFragmentChanger().addFragmentOnStart(ExpandableCollectionsFragment.newInstance());
                //}
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

    private boolean fileUpdateIsOld() {
        File unraredFile = WinRarFileWorker.getUnraredXlsFile(getCacheDir(), null);
        if(unraredFile != null) {
            int year = 2;
            int month = 1;
            int day = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int[] todayDdMmYyyy = {calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)};
            calendar.setTimeInMillis(unraredFile.lastModified());
            int[] fileDdMmYyyy = {calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)};
            return todayDdMmYyyy[year] > fileDdMmYyyy[year] ||
                    todayDdMmYyyy[month] > fileDdMmYyyy[month] ||
                    todayDdMmYyyy[day] > fileDdMmYyyy[day];
        }
        return true;
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
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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
            case R.id.nav_settings:
                //startActivity(new Intent(this, SettingsActivity.class));
                startActivity(new Intent(this, TempSettingsActivity.class));
                break;
        }
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
                mManager.beginTransaction().remove(mStartFragment).commit();
            }
            this.mStartFragment = startFragment;
            mManager.beginTransaction().add(R.id.content_main, startFragment).commit();
        }

        public void changeFragment(Fragment fragment, boolean addToBackStack) {
            FragmentTransaction fragmentTransaction = mManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment);
            if(addToBackStack) {
                fragmentTransaction.addToBackStack(BACKSTACK);
            }
            fragmentTransaction.commit();
        }

        private void clearBackStack() {
            int n = mManager.getBackStackEntryCount();
            for (int i = 0; i < n; i++) {
                mManager.popBackStackImmediate();
            }
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

        public void addView(View view) {
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
    }
}
