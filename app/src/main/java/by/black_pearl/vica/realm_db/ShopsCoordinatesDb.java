package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;

/**
 * Created by BLACK_Pearl.
 */

public class ShopsCoordinatesDb extends RealmObject {
    public final static String COLUMN_ID = "id";
    public final static String COLUMN_SHOP_ID = "shop_id";
    public final static String COLUMN_LATITUDE = "latitude";
    public final static String COLUMN_LONGTITUDE = "longtitude";

    private int id;
    private int shop_id;
    private double latitude;
    private double longtitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shop_id;
    }

    public void setShopId(int shop_id) {
        this.shop_id = shop_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
