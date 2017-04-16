package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by BLACK_Pearl.
 */

public class SizesDb extends RealmObject {
    @Index
    private int id;
    private String size;

    public SizesDb setSizeParam(String sizeParam) {
        this.size = sizeParam;
        return this;
    }

    public String getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
