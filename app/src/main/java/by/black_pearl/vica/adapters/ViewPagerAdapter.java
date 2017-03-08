package by.black_pearl.vica.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import by.black_pearl.vica.fragments.CollectionFragment;
import by.black_pearl.vica.realm_db.CollectionDb;
import io.realm.Realm;

/**
 * Created by BLACK_Pearl.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
        return CollectionFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        Realm realm = Realm.getDefaultInstance();
        long count = realm.where(CollectionDb.class).count();
        realm.close();
        return (int) count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }
}
