package by.black_pearl.vica.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import by.black_pearl.vica.fragments.UpgradeFragment;
import by.black_pearl.vica.objects.ProductSeries;
import by.black_pearl.vica.realm_db.ProductSeriesDb;
import io.realm.Realm;

/**
 * Created by BLACK_Pearl.
 */

public class SaveProductMenu extends AsyncTask<Void, Void, Void> {

    private final Context mContext;
    private final TextView mParentTextView;
    private final ArrayList<ProductSeries> mProductMenus;
    private final UpgradeFragment.TaskListener mTaskListener;

    public SaveProductMenu(Context context, ArrayList<ProductSeries> menus, TextView textView, UpgradeFragment.TaskListener taskListener) {
        this.mContext = context;
        this.mProductMenus = menus;
        this.mParentTextView = textView;
        this.mTaskListener = taskListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mParentTextView.setText("Сохранение серий...");
    }

    @Override
    protected Void doInBackground(Void... params) {
        Realm realm = Realm.getDefaultInstance();
        for (int i = 0; i < this.mProductMenus.size(); i++) {
            realm.beginTransaction();
            ProductSeriesDb productSeriesDb = realm.createObject(ProductSeriesDb.class);
            productSeriesDb.setId(mProductMenus.get(i).getId());
            productSeriesDb.setName(mProductMenus.get(i).getName());
            productSeriesDb.setImage(mProductMenus.get(i).getImage());
            productSeriesDb.setDescription(mProductMenus.get(i).getDescription());
            productSeriesDb.setMatherial(mProductMenus.get(i).getMatherial());
            productSeriesDb.setIdMenu(mProductMenus.get(i).getIdMenu());
            productSeriesDb.setSort(mProductMenus.get(i).getSort());
            productSeriesDb.setImageMenu(mProductMenus.get(i).getImageMenu());
            productSeriesDb.setImage2(mProductMenus.get(i).getImage2());
            productSeriesDb.setImage3(mProductMenus.get(i).getImage3());
            realm.commitTransaction();
            Log.i("Realm", "record #: " + i + " from: " + mProductMenus.size());
        }
        realm.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.mParentTextView.setText("Серии сохранены.");
        this.mTaskListener.replaceToCatalogFragment();
    }
}
