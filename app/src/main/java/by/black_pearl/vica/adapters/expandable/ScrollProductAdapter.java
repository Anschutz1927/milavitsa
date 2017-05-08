package by.black_pearl.vica.adapters.expandable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import by.black_pearl.vica.fragments.expandable.ProductFragment;
import by.black_pearl.vica.realm_db.ProductDb;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BLACK_Pearl.
 */
public class ScrollProductAdapter extends FragmentPagerAdapter {
    private RealmResults<ProductDb> mProducts;
    private int mId = 0;

    public ScrollProductAdapter(FragmentManager fragmentManager, int mId) {
        super(fragmentManager);
        this.mId = mId;
        Realm realm = Realm.getDefaultInstance();
        ProductDb mProduct = realm.where(ProductDb.class).equalTo(ProductDb.COLUMN_ID, mId).findFirst();
        if(mProduct == null) {
            return;
        }
        this.mProducts = realm.where(ProductDb.class).equalTo(ProductDb.COLUMN_ID_RUBRIC, mProduct.getId_rubric()).findAll();
    }

    @Override
    public Fragment getItem(int position) {
        return ProductFragment.newInstance(mProducts.get(position).getId());
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    public int getCurrentPos() {
        int i = 0;
        for (ProductDb productDb : mProducts) {
            if(mId == productDb.getId()) {
                break;
            }
            i++;
        }
        return i;
    }

    public String getItemArticle(int position) {
        return mProducts.get(position).getArticle();
    }
}
