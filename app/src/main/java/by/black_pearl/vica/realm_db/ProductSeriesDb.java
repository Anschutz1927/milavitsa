package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;

/**
 * Created by BLACK_Pearl.
 */

public class ProductSeriesDb extends RealmObject {

    private int Id;
    private String Name;
    private String Image;
    private String Description;
    private String Matherial;
    private int IdMenu;
    private int Sort;
    private String ImageMenu;
    private String Image2;
    private String Image3;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMatherial() {
        return Matherial;
    }

    public void setMatherial(String matherial) {
        Matherial = matherial;
    }

    public int getIdMenu() {
        return IdMenu;
    }

    public void setIdMenu(int idMenu) {
        IdMenu = idMenu;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public String getImageMenu() {
        return ImageMenu;
    }

    public void setImageMenu(String imageMenu) {
        ImageMenu = imageMenu;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String image2) {
        Image2 = image2;
    }

    public String getImage3() {
        return Image3;
    }

    public void setImage3(String image3) {
        Image3 = image3;
    }
}
