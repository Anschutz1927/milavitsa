package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by BlackPearl-net.
 */

public class ColorModelSizeDb extends RealmObject {
    @Index
    private int model;
    private int colorId;
    private int sizeId;
    private int constructionId;
    private int constructionTypeId;

    public ColorModelSizeDb() {
    }


    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(int constructionId) {
        this.constructionId = constructionId;
    }

    public int getConstructionTypeId() {
        return constructionTypeId;
    }

    public void setConstructionTypeId(int constructionTypeId) {
        this.constructionTypeId = constructionTypeId;
    }
}
