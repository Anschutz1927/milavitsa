package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by BLACK_Pearl.
 */

public class ConstructionsDb extends RealmObject {
    @Index
    private int id;
    private String construction;

    public ConstructionsDb setConstructionParam(String constructionParam) {
        this.construction = constructionParam;
        return this;
    }

    public String getConstruction() {
        return construction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
