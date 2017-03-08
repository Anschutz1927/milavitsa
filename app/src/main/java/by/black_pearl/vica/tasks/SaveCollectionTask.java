package by.black_pearl.vica.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.util.ArrayList;

import by.black_pearl.vica.fragments.UpgradeFragment;
import by.black_pearl.vica.objects.Collection;
import by.black_pearl.vica.realm_db.CollectionDb;
import io.realm.Realm;

/**
 * Created by BLACK_Pearl.
 */

public class SaveCollectionTask extends AsyncTask<Void, Void, Void> {
    private final Context mContext;
    private final TextView mParentTextView;
    private final UpgradeFragment.TaskListener mListener;

    public SaveCollectionTask(Context context, TextView updateTextView, UpgradeFragment.TaskListener taskListener) {
        this.mContext = context;
        this.mParentTextView = updateTextView;
        this.mListener = taskListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mParentTextView.setText("Сохранение коллекций...");
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<Collection> collections = new ArrayList<>();
        collections.add(new Collection("Классика", "catalog/classic/t/r_classika_razdel_1_2017_577x386.png", 55));
        collections.add(new Collection("Купальники", "catalog/moda/t/r_imperiya_chuvstv_785_524_577x386.png", 56));
        collections.add(new Collection("Модная коллекция", "catalog/swimsuit/t/t/r_kartinka_k_razdelu_577x386.png", 54));
        collections.add(new Collection("Трикотажная коллекция", "tricotazh/t/r_k_razdelu_577x386.png", 83));
        Realm realm = Realm.getDefaultInstance();
        for (int i = 0; i < collections.size(); i++) {
            realm.beginTransaction();
            CollectionDb collectionDb = realm.createObject(CollectionDb.class);
            collectionDb.setName(collections.get(i).getName());
            collectionDb.setImage(collections.get(i).getImage());
            collectionDb.setMneuId(collections.get(i).getMneuId());
            realm.commitTransaction();
        }
        realm.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.mParentTextView.setText("Коллекции сохранены.");
        new UpdateProductTask(mContext, this.mParentTextView, mListener).execute();

    }
}
