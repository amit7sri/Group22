package group22.android.com.group22.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import group22.android.com.group22.model.Accelo;

/**
 * Created by amitn on 13-02-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    String TAG = "Databaseandler";

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Group22_XXX";

    // Contacts table name
    private static final String TABLE_PATIENT = "Name_ID_Age_Sex_XXX";

    // Contacts Table Columns names
    private static final String TIME_STAMP = "tp";
    private static final String _X = "x";
    private static final String _Y = "y";
    private static final String _Z = "z";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENT_TABLE = "CREATE TABLE " + TABLE_PATIENT + "("
                + TIME_STAMP + " TEXT PRIMARY KEY," + _Y + " REAL,"
                + _Z + "  REAL," + _X + " REAL" + ")";
        db.execSQL(CREATE_PATIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
        // Create tables again
        onCreate(db);

    }

    void addToTable(Accelo value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        Log.d(TAG,"Timestamp is "  + value.getTp());
        Log.d(TAG,"x is  " +  value.getLast_x());
        Log.d(TAG,"y is  " +  value.getLast_y());
        Log.d(TAG, "z is  " + value.getLast_z());


        values.put(_Y, value.getLast_y());
        values.put(TIME_STAMP, value.getTp());
        values.put(_X, value.getLast_x());
        values.put(_Z, value.getLast_z());


        // Inserting Row
        db.insert(TABLE_PATIENT, null, values);
        db.close(); // Closing database connection
    }

    Accelo getSingleValue(String timestamp) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PATIENT, new String[]{TIME_STAMP,
                        _X, _Y, _Z}, TABLE_PATIENT + "=?",
                new String[]{String.valueOf(timestamp)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Accelo value = new Accelo(cursor.getString(0),
                Float.parseFloat(cursor.getString(1)), Float.parseFloat(cursor.getString(2)), Float.parseFloat(cursor.getString(3)));
        // return contact
        return value;

    }

    // Getting All Contacts
    public List<Accelo> getAllValues() {
        List<Accelo> valList = new ArrayList<Accelo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PATIENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Accelo val = new Accelo();
                val.setTp(cursor.getString(0));
                /*val.setLast_x(cursor.getString(1));
                val.setLast_y(cursor.getString(2));
                val.setLast_y(cursor.getString(3));*/

                // Adding contact to list
                valList.add(val);
            } while (cursor.moveToNext());
        }

        // return contact list
        return valList;
    }

}
