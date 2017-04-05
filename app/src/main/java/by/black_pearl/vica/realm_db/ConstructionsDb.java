package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;

/**
 * Created by BLACK_Pearl.
 */

public class ConstructionsDb extends RealmObject {
    private String construction;

    public ConstructionsDb setConstructionParam(String constructionParam) {
        this.construction = constructionParam;
        return this;
    }

    public String getConstruction() {
        return construction;
    }
}
