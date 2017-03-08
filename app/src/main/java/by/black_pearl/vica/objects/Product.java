package by.black_pearl.vica.objects;

/**
 * Created by BLACK_Pearl.
 */

public class Product {
    private int mIdProduct;
    private int mIdRubric;
    private String mArticle;
    private String mImage;
    private String mImageBackward;
    private String mDescription;
    private String mSizes;
    private int mType;
    private int mSort;
    private String mCustomMatherial;

    public int getIdProduct() {
        return mIdProduct;
    }

    public void setIdProduct(int mIdProduct) {
        this.mIdProduct = mIdProduct;
    }

    public int getIdRubric() {
        return mIdRubric;
    }

    public void setIdRubric(int mIdRubric) {
        this.mIdRubric = mIdRubric;
    }

    public String getArticle() {
        return mArticle;
    }

    public void setArticle(String mArticle) {
        this.mArticle = mArticle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getSizes() {
        return mSizes;
    }

    public void setSizes(String mSizes) {
        this.mSizes = mSizes;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public int getSort() {
        return mSort;
    }

    public void setSort(int mSort) {
        this.mSort = mSort;
    }

    public String getCustomMatherial() {
        return mCustomMatherial;
    }

    public void setCustomMatherial(String mCustomMatherial) {
        this.mCustomMatherial = mCustomMatherial;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public String getImageBackward() {
        return mImageBackward;
    }

    public void setImageBackward(String mImageBackward) {
        this.mImageBackward = mImageBackward;
    }
}
