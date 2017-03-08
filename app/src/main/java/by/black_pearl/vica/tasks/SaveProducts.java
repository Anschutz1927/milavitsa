package by.black_pearl.vica.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import by.black_pearl.vica.fragments.UpgradeFragment;
import by.black_pearl.vica.objects.Product;
import by.black_pearl.vica.realm_db.ProductDb;
import io.realm.Realm;

/**
 * Created by BLACK_Pearl.
 */

public class SaveProducts extends AsyncTask<Void, Void, Void> {

    private final Context mContext;
    private final TextView mParentTextView;
    private final ArrayList<Product> mProducts;
    private final UpgradeFragment.TaskListener mTaskListener;

    public SaveProducts(Context context, ArrayList<Product> products, TextView textView, UpgradeFragment.TaskListener taskListener) {
        this.mContext = context;
        this.mProducts = products;
        this.mParentTextView = textView;
        this.mTaskListener = taskListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mParentTextView.setText("Сохранение продуктов...");
    }

    @Override
    protected Void doInBackground(Void... params) {
        Realm realm = Realm.getDefaultInstance();
        for (int i = 0; i < this.mProducts.size(); i++) {
            realm.beginTransaction();
            ProductDb productDb = realm.createObject(ProductDb.class);
            productDb.setId(mProducts.get(i).getIdProduct());
            productDb.setIdRubric(mProducts.get(i).getIdRubric());
            productDb.setArticle(mProducts.get(i).getArticle());
            productDb.setImage(mProducts.get(i).getImage());
            productDb.setImageBackward(mProducts.get(i).getImageBackward());
            productDb.setDescription(mProducts.get(i).getDescription());
            productDb.setSizes(mProducts.get(i).getSizes());
            productDb.setType(mProducts.get(i).getType());
            productDb.setSort(mProducts.get(i).getSort());
            productDb.setCustomMatherial(mProducts.get(i).getCustomMatherial());
            realm.commitTransaction();
            Log.i("Realm", "record #: " + i + " from: " + mProducts.size());
        }
        realm.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.mParentTextView.setText("Продукты сохранены.");
        startUpdateMenu();
    }

    private void startUpdateMenu() {
        new UpdateMenuTask(this.mContext, this.mParentTextView, mTaskListener).execute();
    }
}
