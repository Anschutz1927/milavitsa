package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by BLACK_Pearl.
 */

public class ModelIrrhDb extends RealmObject {
    private String size;
    @Index
    private int model;
    private String construction;
    private String construction_type;

    public ModelIrrhDb() {
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }

    public String getConstructionType() {
        return construction_type;
    }

    public void setConstructionType(String constructionType) {
        this.construction_type = constructionType;
    }
}
