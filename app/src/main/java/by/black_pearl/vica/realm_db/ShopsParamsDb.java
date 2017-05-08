package by.black_pearl.vica.realm_db;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by BLACK_Pearl.
 */

public class ShopsParamsDb extends RealmObject {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RUBRIC_ID = "rubric_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_TIME_WORK = "time_work";
    public static final String COLUMN_PHONE = "phone";

    private int id;
    private int rubric_id;
    private String name;
    private String address;
    private String time_work;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTimeWork() {
        return time_work;
    }

    public void setTimeWork(String time_work) {
        this.time_work = time_work;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRubricId() {
        return rubric_id;
    }

    public void setRubricId(int rubric_id) {
        this.rubric_id = rubric_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Integer[] getIdArray(RealmResults<ShopsParamsDb> shopsParamsDbs) {
        Integer[] ids = new Integer[shopsParamsDbs.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = shopsParamsDbs.get(i).getId();
        }
        return ids;
    }
}
