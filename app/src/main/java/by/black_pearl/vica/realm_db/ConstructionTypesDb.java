package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by BLACK_Pearl.
 */

public class ConstructionTypesDb extends RealmObject {
    @Index
    private int id;
    private String constructionType;

    public ConstructionTypesDb setConstructionTypeParam(String constructionTypeParam) {
        this.constructionType = constructionTypeParam;
        return this;
    }

    public String getConstructionType() {
        return constructionType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
