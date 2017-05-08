package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;

/**
 * Created by BLACK_Pearl.
 */

public class ProductDb extends RealmObject {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_RUBRIC = "id_rubric";
    public static final String COLUMN_ARTICLE = "article";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_IMAGE_BACKWARD = "image_backward";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_SIZES = "sizes";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_SORT = "sort";
    public static final String COLUMN_CUSTOM_MATHERIAL = "custom_matherial";

    private int id;
    private int id_rubric;
    private String article;
    private String image;
    private String image_backward;
    private String description;
    private String sizes;
    private int type;
    private int sort;
    private String custom_matherial;

    public ProductDb() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_rubric() {
        return id_rubric;
    }

    public void setId_rubric(int id_rubric) {
        this.id_rubric = id_rubric;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_backward() {
        return image_backward;
    }

    public void setImage_backward(String image_backward) {
        this.image_backward = image_backward;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSizes() {
        return sizes;
    }

    public void setSizes(String sizes) {
        this.sizes = sizes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getCustom_matherial() {
        return custom_matherial;
    }

    public void setCustom_matherial(String custom_matherial) {
        this.custom_matherial = custom_matherial;
    }
}
