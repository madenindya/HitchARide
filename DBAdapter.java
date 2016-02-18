package com.example.madenindya.hitcharide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    private static final String TAG = "DBAdapter"; //used for logging database version changes

    // Field Names:
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NOTRAYEK = "no_trayek";
    public static final String KEY_NAMATRAYEK = "nama_trayek";
    public static final String KEY_JENISANGKUTAN = "jenis_angkutan";
    public static final String KEY_WILAYAH = "wilayah";
    public static final String KEY_RUTEBERANGKAT = "rute_berangkat";
    public static final String KEY_RUTEKEMBALI = "rute_kembali";

    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_NOTRAYEK, KEY_NAMATRAYEK, KEY_JENISANGKUTAN, KEY_WILAYAH, KEY_RUTEBERANGKAT, KEY_RUTEKEMBALI};

    // Column Numbers for each Field Name:
    public static final int COL_ROWID = 0;
    public static final int COL_NOTRAYEK = 1;
    public static final int COL_NAMATRAYEK = 2;
    public static final int COL_JENISANGKUTAN = 3;
    public static final int COL_WILAYAH = 4;
    public static final int COL_RUTEBERANGKAT = 5;
    public static final int COL_RUTEKEMBALI = 6;

    // DataBase info:
    public static final String DATABASE_NAME = "dbAngkutanUmum";
    public static final String DATABASE_TABLE = "mainAngkutanUmum";
    public static final int DATABASE_VERSION = 2; // The version number must be incremented each time a change to DB structure occurs.

    //SQL statement to create database
    private static final String DATABASE_CREATE_SQL =
            "CREATE TABLE " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NOTRAYEK + " TEXT NOT NULL, "
                    + KEY_NAMATRAYEK + " TEXT NOT NULL, "
                    + KEY_JENISANGKUTAN + " TEXT, "
                    + KEY_WILAYAH + " TEXT, "
                    + KEY_RUTEBERANGKAT + " TEXT NOT NULL, "
                    + KEY_RUTEKEMBALI + " TEXT NOT NULL " +
                    ");";

    // Field Names TABLE 2
    public static final String KEY_ROWID_PATH = "_id";
    public static final String KEY_FROM = "path_from";
    public static final String KEY_TO = "path_to";
    public static final String KEY_NOANGKOT = "path_with";

    public static final String[] ALL_KEYS_PATH = new String[]{KEY_ROWID_PATH, KEY_FROM, KEY_TO, KEY_NOANGKOT};

    // Column Numbers for each Field Name:
    public static final int COL_ROWID_PATH = 0;
    public static final int COL_FROM = 1;
    public static final int COL_TO = 2;
    public static final int COL_NOANGKOT = 3;

    // DataBase info:
    public static final String DATABASE_TABLE_PATH = "mainPath";

    //SQL statement to create database
    private static final String DATABASE_CREATE_SQL_PATH =
            "CREATE TABLE " + DATABASE_TABLE_PATH
                    + " (" + KEY_ROWID_PATH + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_FROM + " TEXT NOT NULL, "
                    + KEY_TO + " TEXT NOT NULL, "
                    + KEY_NOANGKOT + " TEXT " +
                    ");";

    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to be inserted into the database.
    public long insertRow(String no_trayek, String nama_trayek, String jenis_angkutan, String wilayah, String rute_berangkat, String rute_kembali) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOTRAYEK, no_trayek);
        initialValues.put(KEY_NAMATRAYEK, nama_trayek);
        initialValues.put(KEY_JENISANGKUTAN, jenis_angkutan);
        initialValues.put(KEY_WILAYAH, wilayah);
        initialValues.put(KEY_RUTEBERANGKAT, rute_berangkat);
        initialValues.put(KEY_RUTEKEMBALI, rute_kembali);

        // Insert the data into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, KEY_NOTRAYEK + " ASC", null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Return all data in the databas with search condition
    public Cursor getAllRows_searchCondition(String str) {
        String where = KEY_NOTRAYEK + " LIKE " + "'%" + str + "%' OR " + KEY_NAMATRAYEK + " LIKE " + "'%" + str + "%'";
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, KEY_NOTRAYEK + " ASC", null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, String no_trayek, String nama_trayek, String jenis_angkutan, String wilayah, String rute_berangkat, String rute_kembali) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NOTRAYEK, no_trayek);
        newValues.put(KEY_NAMATRAYEK, nama_trayek);
        newValues.put(KEY_JENISANGKUTAN, jenis_angkutan);
        newValues.put(KEY_WILAYAH, wilayah);
        newValues.put(KEY_RUTEBERANGKAT, rute_berangkat);
        newValues.put(KEY_RUTEKEMBALI, rute_kembali);
        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
            _db.execSQL(DATABASE_CREATE_SQL_PATH);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }

}

