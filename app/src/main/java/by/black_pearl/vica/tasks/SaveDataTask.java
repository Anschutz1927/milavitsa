package by.black_pearl.vica.tasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import by.black_pearl.vica.R;
import by.black_pearl.vica.objects.Collection;
import by.black_pearl.vica.objects.Product;
import by.black_pearl.vica.objects.ProductSeries;
import by.black_pearl.vica.realm_db.CollectionDb;
import by.black_pearl.vica.realm_db.ProductDb;
import by.black_pearl.vica.realm_db.ProductSeriesDb;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by BLACK_Pearl.
 */

public class SaveDataTask extends AsyncTask<Void, String, Void> {

    private final TextView mTextView;
    private final Resources mResources;
    private SaveModelIrrh mSaveModelIrrh;

    public SaveDataTask(Resources resources, TextView updateTextView) {
        this.mResources = resources;
        this.mTextView = updateTextView;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        final String txt = values[0];
        mTextView.post(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(txt);
            }
        });
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<Collection> collections = loadCollections();
        saveCollections(collections);
        ArrayList<ProductSeries> series = loadSeries();
        if(series.size() > 0) {
            saveSeries(series);
        }
        ArrayList<Product> products = loadProducts();
        if(products.size() > 0) {
            saveProducts(products);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mSaveModelIrrh = new SaveModelIrrh(mResources, mTextView);
        mSaveModelIrrh.execute();
    }

    public void cancel() {
        if(this.mSaveModelIrrh != null && this.mSaveModelIrrh.getStatus() == AsyncTask.Status.RUNNING) {
            this.mSaveModelIrrh.cancel();
        }
        cancel(true);
    }

    private ArrayList<Collection> loadCollections() {
        publishProgress("Loading collections...");
        ArrayList<Collection> collections = new ArrayList<>();
        collections.add(new Collection("Классика", "catalog/classic/t/r_classika_razdel_1_2017_577x386.png", 55));
        collections.add(new Collection("Купальники", "catalog/swimsuit/t/t/r_kartinka_k_razdelu_577x386.png", 56));
        collections.add(new Collection("Модная коллекция", "catalog/moda/t/r_imperiya_chuvstv_785_524_577x386.png", 54));
        collections.add(new Collection("Трикотажная коллекция", "tricotazh/t/r_k_razdelu_577x386.png", 83));
        publishProgress("Complete load collections.");
        return collections;
    }

    private void saveCollections(ArrayList<Collection> collections) {
        publishProgress("Saving collections...");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmList<CollectionDb> dbList = new RealmList<>();
        for (int i = 0; i < collections.size(); i++) {
            publishProgress("Saving collection: " + (i + 1) + " from: " + collections.size() + "...");
            CollectionDb collectionDb = realm.createObject(CollectionDb.class);
            collectionDb.setName(collections.get(i).getName());
            collectionDb.setImage(collections.get(i).getImage());
            collectionDb.setMneuId(collections.get(i).getMneuId());
            dbList.add(collectionDb);
        }
        realm.copyToRealm(dbList);
        realm.commitTransaction();
        realm.close();
        publishProgress("Complete save collections.");
    }

    private ArrayList<ProductSeries> loadSeries() {
        ArrayList<ProductSeries> menus = null;
        int count = 0;
        publishProgress("Loading series...");

        try {
            XmlPullParser parser = mResources.getXml(R.xml.cat_new);
            menus = new ArrayList<>();
            ProductSeries menu = null;
            while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (parser.getEventType()) {
                    case XmlPullParser.START_TAG:
                        menu = parseTag(parser, menu);
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("cat_new") && menu != null) {
                            menus.add(menu);
                            publishProgress(++count + " series loaded...");
                        }
                        parser.next();
                        break;
                    default:
                        parser.next();
                        break;
                }
            }
            publishProgress("Load series complete.");
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            publishProgress("Error load series!");
        }
        return menus;
    }

    private ProductSeries parseTag(XmlPullParser parser, ProductSeries menu) {
        try {
            String parseName = parser.getName();
            parser.next();
            switch (parseName) {
                case "cat_new":
                    menu = new ProductSeries();
                    break;
                case "id":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setId(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "name":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setName(parser.getText());
                    }
                    break;
                case "image":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setImage(parser.getText());
                    }
                    break;
                case "description":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setDescription(parser.getText());
                    }
                    break;
                case "matherial":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setMatherial(parser.getText());
                    }
                    break;
                case "menu_id":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setIdMenu(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "sort":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setSort(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "image_menu":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setImageMenu(parser.getText());
                    }
                    break;
                case "image_2":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setImage2(parser.getText());
                    }
                    break;
                case "image_3":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setImage3(parser.getText());
                    }
                    break;
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return menu;
    }

    private void saveSeries(ArrayList<ProductSeries> series) {
        publishProgress("Saving series...");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmList<ProductSeriesDb> dbList = new RealmList<>();
        for (int i = 0; i < series.size(); ++i) {
            publishProgress("Saving series: " + (i + 1) + " from: " + series.size() + "...");
            ProductSeriesDb seriesDb = realm.createObject(ProductSeriesDb.class);
            ProductSeriesDb productSeriesDb = realm.createObject(ProductSeriesDb.class);
            productSeriesDb.setId(series.get(i).getId());
            productSeriesDb.setName(series.get(i).getName());
            productSeriesDb.setImage(series.get(i).getImage());
            productSeriesDb.setDescription(series.get(i).getDescription());
            productSeriesDb.setMatherial(series.get(i).getMatherial());
            productSeriesDb.setIdMenu(series.get(i).getIdMenu());
            productSeriesDb.setSort(series.get(i).getSort());
            productSeriesDb.setImageMenu(series.get(i).getImageMenu());
            productSeriesDb.setImage2(series.get(i).getImage2());
            productSeriesDb.setImage3(series.get(i).getImage3());
            dbList.add(seriesDb);
        }
        realm.copyToRealm(dbList);
        realm.commitTransaction();
        realm.close();
        publishProgress("Complete save series.");
    }

    private ArrayList<Product> loadProducts() {
        publishProgress("Loading products...");
        ArrayList<Product> products = null;
        try {
            XmlPullParser parser = mResources.getXml(R.xml.cat_new_products);
            products = new ArrayList<>();
            Product product = null;
            int count = 0;
            while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (parser.getEventType()) {
                    case XmlPullParser.START_TAG:
                        product = parseTag(parser, product);
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("cat_new_products") && product != null) {
                            products.add(product);
                            publishProgress(++count + " products loaded...");
                        }
                        parser.next();
                        break;
                    default:
                        parser.next();
                        break;
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        publishProgress("Loaded products.");
        return products;
    }

    private Product parseTag(XmlPullParser parser, Product product) {
        try {
            String parseName = parser.getName();
            parser.next();
            switch (parseName) {
                case "cat_new_products":
                    product = new Product();
                    break;
                case "id":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setIdProduct(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "id_rubric":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setIdRubric(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "article":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setArticle(parser.getText());
                    }
                    break;
                case "image":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setImage(parser.getText());
                    }
                    break;
                case "image_backward":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setImageBackward(parser.getText());
                    }
                    break;
                case "description":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setDescription(parser.getText());
                    }
                    break;
                case "sizes":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setSizes(parser.getText());
                    }
                    break;
                case "type":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setType(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "sort":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setSort(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "custom_matherial":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setCustomMatherial(parser.getText());
                    }
                    break;
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return product;
    }

    private void saveProducts(ArrayList<Product> productes) {
        publishProgress("Saving products...");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmList<ProductDb> dbList = new RealmList<>();
        for (int i = 0; i < productes.size(); ++i) {
            publishProgress("Saving products: " + (i + 1) + " from: " + productes.size() + "...");
            ProductDb productDb = realm.createObject(ProductDb.class);
            productDb.setId(productes.get(i).getIdProduct());
            productDb.setIdRubric(productes.get(i).getIdRubric());
            productDb.setArticle(productes.get(i).getArticle());
            productDb.setImage(productes.get(i).getImage());
            productDb.setImageBackward(productes.get(i).getImageBackward());
            productDb.setDescription(productes.get(i).getDescription());
            productDb.setSizes(productes.get(i).getSizes());
            productDb.setType(productes.get(i).getType());
            productDb.setSort(productes.get(i).getSort());
            productDb.setCustomMatherial(productes.get(i).getCustomMatherial());
            dbList.add(productDb);
        }
        realm.copyToRealm(dbList);
        realm.commitTransaction();
        realm.close();
        publishProgress("Complete save products.");
    }
}
