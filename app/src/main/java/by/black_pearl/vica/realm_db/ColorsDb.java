package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by BlackPearl-net.
 */

public class ColorsDb extends RealmObject {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COLOR= "color";

    @Index
    private int id;
    private String color;

    public ColorsDb() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
