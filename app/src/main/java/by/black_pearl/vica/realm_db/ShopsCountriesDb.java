package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;

/**
 * Created by BLACK_Pearl.
 */

public class ShopsCountriesDb extends RealmObject {
    public final static String COLUMN_ID = "id";
    public final static String COLUMN_PARENT_ID = "parent_id";
    public final static String COLUMN_COUNTRY_NAME = "country_name";

    private int id;
    private int parent_id;
    private String country_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parent_id;
    }

    public void setParentId(int parentId) {
        this.parent_id = parentId;
    }

    public String getCountryName() {
        return country_name;
    }

    public void setCountryName(String countryName) {
        this.country_name = countryName;
    }
}
