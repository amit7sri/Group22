package group22.android.com.group22.model;

import java.sql.Timestamp;

/**
 * Created by amitn on 13-02-2017.
 */

public class Accelo {
    String tp;
    float last_x, last_y, last_z;

    public Accelo(){

    }

    public Accelo(String tp, float last_x, float last_y, float last_z) {
        this.tp = tp;
        this.last_x = last_x;
        this.last_y = last_y;
        this.last_z = last_z;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public float getLast_z() {
        return last_z;
    }

    public void setLast_z(float last_z) {
        this.last_z = last_z;
    }

    public float getLast_y() {
        return last_y;
    }

    public void setLast_y(float last_y) {
        this.last_y = last_y;
    }

    public float getLast_x() {
        return last_x;
    }

    public void setLast_x(float last_x) {
        this.last_x = last_x;
    }
}
