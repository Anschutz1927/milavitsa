package by.black_pearl.vica.tasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.black_pearl.vica.R;
import by.black_pearl.vica.fragments.addresses.AddressesFragment;
import by.black_pearl.vica.parsers.CollectionsParser;
import by.black_pearl.vica.parsers.MilavitsaXmlParser;
import by.black_pearl.vica.parsers.ProductsParamsParserCopy;
import by.black_pearl.vica.parsers.ProductsParser;
import by.black_pearl.vica.parsers.SeriesParser;
import by.black_pearl.vica.parsers.ShopsCountryParser;
import by.black_pearl.vica.parsers.ShopsParamsParser;
import by.black_pearl.vica.realm_db.ShopsCoordinatesDb;
import by.black_pearl.vica.realm_db.ShopsCountriesDb;
import by.black_pearl.vica.realm_db.ShopsParamsDb;
import by.black_pearl.vica.retrofit.RetrofitManager;
import by.black_pearl.vica.retrofit.ServerApi;
import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by BLACK_Pearl.
 */

public class SaveMilavitsaDataTask extends AsyncTask<Void, String, Void> {

    private static final String LOG_TAG = "SAVE DATA ->";
    private final TextView mTextView;
    private final Resources mResources;
    private final SaveMilavitsaDataTaskCallback mCallback;

    public SaveMilavitsaDataTask(Resources resources, TextView updateTextView,@Nullable SaveMilavitsaDataTaskCallback callback) {
        this.mResources = resources;
        this.mTextView = updateTextView;
        this.mCallback = callback;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        final String txt = values[0];
        mTextView.setText(txt);
    }

    @Override
    protected Void doInBackground(Void... params) {
        getShopsCoordinates();
        saveCollections();
        saveSeries();
        saveProducts();
        saveProductsParams();
        saveShopCountries();
        saveShopParams();
        //saveShopsCoordinates();
        //downloadShopsCoordinates();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallback != null) {
            mCallback.onPostExecute();
        }
    }

    private void getShopsCoordinates() {
        publishProgress("Началась загрузка мостоположения...");
        RetrofitManager.RetrofitApi api = RetrofitManager.getGsonRetrofit(Backendless.getUrl()).create(RetrofitManager.RetrofitApi.class);
        String shopsCoordinatesDb = "C68F0999-9946-4158-FF9F-D3E41DCBFD00/" +
                "ED8BD84D-BE1A-5766-FFE5-50F738626300/data/shop_coordinates";
        int size = 100;
        int count = 0;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        while (size == 100) {
            Call<RealmList<ShopsCoordinatesDb>> call = api.getRealmListGsonShopsCoordinates(shopsCoordinatesDb, 100, count * 100);
            try {
                Response<RealmList<ShopsCoordinatesDb>> resp = call.execute();
                RealmList<ShopsCoordinatesDb> body = resp.body();
                realm.copyToRealm(body);
                size = body.size();
                count++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        publishProgress("Загрузка мостоположения закончена.");
        realm.commitTransaction();
        realm.close();
    }

    private void saveCollections() {
        publishProgress("Создание коллекций...");
        CollectionsParser.ParserCallback callback = getParserCallback();
        CollectionsParser collectionsParser = new CollectionsParser(callback);
        InputSource source = new InputSource(mResources.openRawResource(R.raw.colections));
        try {
            MilavitsaXmlParser.parseXml(source, collectionsParser);
        }
        catch (Exception e) {
            e.printStackTrace();
            publishProgress("Ошибка обработки коллекций!");
        }
        publishProgress("Коллекции созданы.");
    }

    private void saveSeries() {
        publishProgress("Создание серий...");
        SeriesParser.ParserCallback callback = getParserCallback();
        SeriesParser seriesParser = new SeriesParser(callback);
        InputSource source = new InputSource(mResources.openRawResource(R.raw.cat_new));
        try {
            MilavitsaXmlParser.parseXml(source, seriesParser);
        }
        catch (Exception e) {
            e.printStackTrace();
            publishProgress("Ошибка обработки серий!");
        }
        publishProgress("Серии созданы.");
    }

    private void saveProducts() {
        publishProgress("Создание моделей...");
        ProductsParser.ParserCallback callback = getParserCallback();
        ProductsParser seriesParser = new ProductsParser(callback);
        InputSource source = new InputSource(mResources.openRawResource(R.raw.cat_new_products));
        try {
            MilavitsaXmlParser.parseXml(source, seriesParser);
        }
        catch (Exception e) {
            e.printStackTrace();
            publishProgress("Ошибка обработки моделей!");
        }
        publishProgress("Модели созданы.");
    }

    private void saveProductsParams() {
        publishProgress("Создание параметров моделей...");
        ProductsParamsParserCopy productsParamsParserCopy =
                new ProductsParamsParserCopy(new MilavitsaXmlParser.ParserCallback() {
                    long mLastTime = 0;
                    int mCount;

                    @Override
            public void onStartDocument() {

            }

            @Override
            public void onStartElement(String qName) {

            }

            @Override
            public void onAttributes(Attributes attributes) {

            }

            @Override
            public void onCharacters(String chars) {

            }

            @Override
            public void onEndElement(String qName) {
                if (qName.equals("data")) {
                    mCount++;
                    if(mLastTime == 0) {
                        mLastTime = System.currentTimeMillis();
                        publishProgress("Обработано параметров: " + String.valueOf(mCount));
                    }
                    else {
                        long currentTime = System.currentTimeMillis();
                        if(currentTime - mLastTime >= 100) {
                            mLastTime = currentTime;
                            publishProgress("Обработано параметров: " + String.valueOf(mCount));
                        }
                    }
                }
            }

            @Override
            public void onEndDocument() {

            }
        });
        InputSource source = new InputSource(mResources.openRawResource(R.raw.products_params));
        try {
            MilavitsaXmlParser.parseXml(source, productsParamsParserCopy);
        }
        catch (Exception e) {
            e.printStackTrace();
            publishProgress("Ошибка обработки параметров моделей!");
        }
        publishProgress("Параметры моделей созданы.");
    }

    private void saveShopCountries() {
        publishProgress("Подготовка обработки магазинов...");
        ShopsCountryParser shopsCountryParser = new ShopsCountryParser(getParserCallback());
        InputSource source = new InputSource(mResources.openRawResource(R.raw.shops_countries));
        try {
            MilavitsaXmlParser.parseXml(source, shopsCountryParser);
        }
        catch (Exception e) {
            e.printStackTrace();
            publishProgress("Ошибка обработки магазинов!");
        }
        publishProgress("Обработка магазинов завершена.");
    }

    private void saveShopParams() {
        publishProgress("Обработка параметров магазинов...");
        ShopsParamsParser shopsParamsParser = new ShopsParamsParser(getParserCallback());
        InputSource source = new InputSource(mResources.openRawResource(R.raw.shops_params));
        try {
            MilavitsaXmlParser.parseXml(source, shopsParamsParser);
        }
        catch (Exception e) {
            e.printStackTrace();
            publishProgress("Ошибка обработки магазинов!");
        }
        publishProgress("Обработка магазинов завершена.");
    }

    private void downloadShopsCoordinates() {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(20);
        queryBuilder.setOffset(40);
        List<Map> coordinates = Backendless.Data.of("shop_coordinates").find(queryBuilder);
    }

    private void saveShopsCoordinates() {
        publishProgress("Загрузка местоположения магазинов...");
        Realm realm = Realm.getDefaultInstance();
        ServerApi api = RetrofitManager.getRetrofit(AddressesFragment.GEO_URL).create(ServerApi.class);
        int id = realm.where(ShopsCoordinatesDb.class).max(ShopsCoordinatesDb.COLUMN_ID) != null ?
                (int) realm.where(ShopsCoordinatesDb.class).max(ShopsCoordinatesDb.COLUMN_ID) : 0;
        for (ShopsParamsDb paramsDb : realm.where(ShopsParamsDb.class).findAll()) {
            String address = parseAddress(realm, paramsDb.getRubricId(), paramsDb.getAddress());
            Call<ResponseBody> call = api.getGeoInfo(AddressesFragment.GEO_REPOS, address,
                    AddressesFragment.GEO_FORMAT, 1, 0, AddressesFragment.GEO_LANG);
            if (call(call, id, paramsDb.getId())) {
                id++;
            }
        }
        publishProgress("Загрузка местоположения магазинов завершена.");
    }

    private boolean call(final Call<ResponseBody> call, final int id, final int shopId) {
        Runnable threadRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    if (!response.isSuccessful()) {
                        Log.i(LOG_TAG, "Shop id " + shopId + " was not downloaded.");
                        return; //false;
                    }
                    try {
                        String resp = response.body().string();
                        String coordinates = getCoordinates(resp);
                        if (coordinates.equals("")) {
                            return ;//false;
                        }
                        Log.i(LOG_TAG, coordinates);
                        //HashMap shop_coordinates = createCoordinatesHashMap(shopId, coordinates);
                        //sendDataToServer(shopId, shop_coordinates);
                        saveDataToDatabase(id, shopId, coordinates);
                        return;// true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;// false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;// false;
                }
            }
        };
        new Thread(threadRunnable).start();
        return true;
    }

    private String getCountry(Realm realm, int countryId) {
        ShopsCountriesDb shop = realm.where(ShopsCountriesDb.class).equalTo(ShopsCountriesDb.COLUMN_ID, countryId).findFirst();
        if (shop != null) {
            return shop.getCountryName();
        }
        return "no";
    }

    private String parseAddress(Realm realm, int rubricId, String shopAddress) {
        String address = getCountry(realm, rubricId);
        String[] shopCountryAddress = shopAddress.split(",");
        for (int i = 0; i < shopCountryAddress.length; i++) {
            if (shopCountryAddress[i].contains("ул.") ||
                    shopCountryAddress[i].contains("пр.") ||
                    shopCountryAddress[i].contains("пр-т.") ||
                    shopCountryAddress[i].contains("бул.") ||
                    shopCountryAddress[i].contains("пер.")) {
                if (shopCountryAddress.length > i + 1) {
                    address += "," + shopCountryAddress[i] + ",";// + shopCountryAddress[i + 1];
                    char[] chars = shopCountryAddress[i + 1].toCharArray();
                    for (int j = 0; j < chars.length; j++) {
                        if (chars[j] >= '0' && chars[j] <= '9' || chars[j] == ' ') {
                            address += chars[j];
                        }
                        else {
                            break;
                        }
                    }
                    break;
                }
                address += "," + shopAddress;
            }
            address += "," + shopAddress;
        }
        return address;
    }

    private String getCoordinates(String resp) {
        String[] resps = resp.split("\"pos\":");
        String gpsStr = "";
        if (resps.length > 1) {
            gpsStr = resps[1];
            gpsStr = gpsStr.split("\\}")[0];
            gpsStr = new String(gpsStr.toCharArray(), 1, gpsStr.length() - 2);
        }
        return gpsStr;
    }

    private HashMap createCoordinatesHashMap(int shopId, String coordinates) {
        HashMap shop_coordinates = new HashMap();
        shop_coordinates.put("shop_id", shopId);
        shop_coordinates.put("latitude", Double.valueOf(coordinates.split(" ")[1]));
        shop_coordinates.put("longtitude", Double.valueOf(coordinates.split(" ")[0]));
        return shop_coordinates;
    }

    private void sendDataToServer(final int shopId, HashMap shop_coordinates) {
        try {
            Backendless.Persistence.of("shop_coordinates").save(shop_coordinates, new AsyncCallback<Map>() {
                @Override
                public void handleResponse(Map response) {
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Log.i(LOG_TAG, "fault to save coordinate: " + fault.getCode()+ " -> " + fault.getMessage() + ":");
                    Log.i(LOG_TAG, "Shop id " + shopId + " was not uploaded.");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDataToDatabase(int id, int shopId, String coordinates) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ShopsCoordinatesDb coordinatesDb = realm.createObject(ShopsCoordinatesDb.class);
        coordinatesDb.setId(id);
        coordinatesDb.setShopId(shopId);
        coordinatesDb.setLatitude(Double.parseDouble(coordinates.split(" ")[1]));
        coordinatesDb.setLongtitude(Double.parseDouble(coordinates.split(" ")[0]));
        realm.commitTransaction();
        realm.close();
    }

    private MilavitsaXmlParser.ParserCallback getParserCallback() {
        return new MilavitsaXmlParser.ParserCallback() {
            @Override
            public void onStartDocument() {

            }

            @Override
            public void onStartElement(String qName) {

            }

            @Override
            public void onAttributes(Attributes attributes) {

            }

            @Override
            public void onCharacters(String chars) {

            }

            @Override
            public void onEndElement(String qName) {

            }

            @Override
            public void onEndDocument() {

            }
        };
    }

    public interface SaveMilavitsaDataTaskCallback {
        void onPostExecute();
    }
}
