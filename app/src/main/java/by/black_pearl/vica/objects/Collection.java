package by.black_pearl.vica.objects;

/**
 * Created by BLACK_Pearl.
 */

public class Collection {
    private String mName = "";
    private String mImage = "";
    private int mMenuId = 0;

    public Collection(String name, String image_path, int menuId) {
        this.mName = name;
        this.mImage = image_path;
        this.mMenuId = menuId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public int getMneuId() {
        return mMenuId;
    }

    public void setMenuId(int mMneuId) {
        this.mMenuId = mMneuId;
    }
}
