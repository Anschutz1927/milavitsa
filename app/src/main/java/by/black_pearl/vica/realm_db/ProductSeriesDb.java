package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;

/**
 * Created by BLACK_Pearl.
 */

public class ProductSeriesDb extends RealmObject {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_MATHERIAL = "matherial";
    public static final String COLUMN_ID_MENU = "id_menu";
    public static final String COLUMN_SORT = "sort";
    public static final String COLUMN_IMAGE_MENU = "image_menu";
    public static final String COLUMN_IMAGE2 = "image2";
    public static final String COLUMN_IMAGE3 = "image3";

    private int id;
    private String name;
    private String image;
    private String description;
    private String matherial;
    private int id_menu;
    private int sort;
    private String image_menu;
    private String image2;
    private String image3;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMatherial() {
        return matherial;
    }

    public void setMatherial(String matherial) {
        this.matherial = matherial;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getImage_menu() {
        return image_menu;
    }

    public void setImage_menu(String image_menu) {
        this.image_menu = image_menu;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
