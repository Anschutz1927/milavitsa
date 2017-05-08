package by.black_pearl.vica.realm_db;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Index;

/**
 * Created by BlackPearl-net.
 */

public class ProductsParamsDb extends RealmObject {
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_COLOR_ID = "color_id";
    public static final String COLUMN_SIZE_ID = "size_id";
    public static final String COLUMN_CONSTRUCTION_ID = "construction_id";
    public static final String COLUMN_CONSTRUCTION_TYPE_ID = "construction_type_id";

    @Index
    private int model;
    private int color_id;
    private int size_id;
    private int construction_id;
    private int construction_type_id;

    public ProductsParamsDb() {
    }


    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getColor_id() {
        return color_id;
    }

    public void setColorId(int color_id) {
        this.color_id = color_id;
    }

    public int getSize_id() {
        return size_id;
    }

    public void setSizeId(int size_id) {
        this.size_id = size_id;
    }

    public int getConstruction_id() {
        return construction_id;
    }

    public void setConstruction_id(int construction_id) {
        this.construction_id = construction_id;
    }

    public int getConstruction_type_id() {
        return construction_type_id;
    }

    public void setConstructionTypeId(int construction_type_id) {
        this.construction_type_id = construction_type_id;
    }

    public static ArrayList<String> getItemSizesList(Realm realm, int modelArticle) {
        RealmResults<ProductsParamsDb> productRows = realm.where(ProductsParamsDb.class)
                .equalTo(ProductsParamsDb.COLUMN_MODEL, modelArticle).findAll();
        Integer[] sizesIds = new Integer[productRows.size()];
        for (int i = 0; i < productRows.size(); i++) {
            sizesIds[i] = productRows.get(i).getSize_id();
        }
        RealmResults<SizesDb> sizesDbs = realm.where(SizesDb.class).in(SizesDb.COLUMN_ID, sizesIds).findAll();
        ArrayList<String> itemSizes = new ArrayList<>();
        for (SizesDb sizesDb : sizesDbs) {
            itemSizes.add(sizesDb.getSize());
        }
        return itemSizes;
    }

    public static ArrayList<String> getItemColorsList(Realm realm, int modelArticle) {
        RealmResults<ProductsParamsDb> productRows = realm.where(ProductsParamsDb.class)
                .equalTo(ProductsParamsDb.COLUMN_MODEL, modelArticle).findAll();
        Integer[] colorsIds = new Integer[productRows.size()];
        for (int i = 0; i < productRows.size(); i++) {
            colorsIds[i] = productRows.get(i).getColor_id();
        }
        RealmResults<ColorsDb> colorsDbs = realm.where(ColorsDb.class).in(ColorsDb.COLUMN_ID, colorsIds).findAll();
        ArrayList<String> itemColors = new ArrayList<>();
        for (ColorsDb colorsDb : colorsDbs) {
            itemColors.add(colorsDb.getColor());
        }
        return itemColors;
    }
}
