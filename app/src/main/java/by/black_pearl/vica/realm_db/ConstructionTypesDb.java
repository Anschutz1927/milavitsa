package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;

/**
 * Created by BLACK_Pearl.
 */

public class ConstructionTypesDb extends RealmObject {
    private String constructionType;

    public ConstructionTypesDb setConstructionTypeParam(String constructionTypeParam) {
        this.constructionType = constructionTypeParam;
        return this;
    }

    public String getConstructionType() {
        return constructionType;
    }
}
