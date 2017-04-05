package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;

/**
 * Created by BLACK_Pearl.
 */

public class SizesDb extends RealmObject {
    private String size;

    public SizesDb setSizeParam(String sizeParam) {
        this.size = sizeParam;
        return this;
    }

    public String getSize() {
        return size;
    }
}
