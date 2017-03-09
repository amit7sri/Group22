package group22.android.com.group22.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import group22.android.com.group22.model.Accelo;
import group22.android.com.group22.model.DBTable;


/**
 * Created by amitn on 13-02-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Group22.db";

    public DatabaseHandler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENT_TABLE ="CREATE TABLE "+DBTable.getTablename()+"( "+
                DBTable.timestamp+" INTEGER PRIMARY KEY, "+ DBTable.x_values +" REAL, "+
                DBTable.y_values +" REAL, "+ DBTable.z_values +" REAL);";
        db.execSQL(CREATE_PATIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBTable.getTablename());
        // Create tables again
        onCreate(db);

    }

    public void insertDB(SQLiteDatabase db, float[] inputSensorValues) {
        ContentValues values = new ContentValues();
        values.put(DBTable.x_values, inputSensorValues[0]);
        values.put(DBTable.y_values, inputSensorValues[1]);
        values.put(DBTable.z_values, inputSensorValues[2]);
        db.insert(DBTable.tablename, null, values);
    }


    public float[][] get10records(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery("select "+DBTable.x_values+","+DBTable.y_values+","+DBTable.z_values+" from " + DBTable.tablename + " order by timestamp DESC limit 11", null);
        cursor.moveToFirst();
        float values[][] = new float[10][3];
        int i = 0;
        while (cursor.moveToNext() && i!=10) {
            values[i][0] = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DBTable.x_values)));
            values[i][1] = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DBTable.y_values)));
            values[i][2] = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DBTable.z_values)));

            i++;
        }
        return values;
    }

    public List<Accelo> getAllValues() {
        List<Accelo> valList = new ArrayList<Accelo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBTable.getTablename();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Accelo val = new Accelo();
                val.setTp(cursor.getString(0));

                valList.add(val);
            } while (cursor.moveToNext());
        }
        return valList;
    }
}


