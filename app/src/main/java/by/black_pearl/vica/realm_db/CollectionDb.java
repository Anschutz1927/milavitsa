package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;

/**
 * Created by BLACK_Pearl.
 */

public class CollectionDb extends RealmObject {
    private String Name;
    private String Image;
    private int MenuId;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public int getMenuId() {
        return MenuId;
    }

    public void setMneuId(int menuId) {
        this.MenuId = menuId;
    }
}
