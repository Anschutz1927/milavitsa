package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by BLACK_Pearl.
 */

public class ConstructionTypesDb extends RealmObject {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CONSTRUCTION_TYPE = "construction_type";

    @Index
    private int id;
    private String construction_type;

    public ConstructionTypesDb setConstructionTypeParam(String constructionTypeParam) {
        this.construction_type = constructionTypeParam;
        return this;
    }

    public String getConstruction_type() {
        return construction_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
