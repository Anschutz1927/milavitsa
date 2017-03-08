package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;

/**
 * Created by BLACK_Pearl.
 */

public class ProductDb extends RealmObject {
    private int Id;
    private int IdRubric;
    private String Article;
    private String Image;
    private String ImageBackward;
    private String Description;
    private String Sizes;
    private int Type;
    private int Sort;
    private String CustomMatherial;

    public ProductDb() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdRubric() {
        return IdRubric;
    }

    public void setIdRubric(int idRubric) {
        IdRubric = idRubric;
    }

    public String getArticle() {
        return Article;
    }

    public void setArticle(String article) {
        Article = article;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImageBackward() {
        return ImageBackward;
    }

    public void setImageBackward(String imageBackward) {
        ImageBackward = imageBackward;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSizes() {
        return Sizes;
    }

    public void setSizes(String sizes) {
        Sizes = sizes;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public String getCustomMatherial() {
        return CustomMatherial;
    }

    public void setCustomMatherial(String customMatherial) {
        CustomMatherial = customMatherial;
    }
}
