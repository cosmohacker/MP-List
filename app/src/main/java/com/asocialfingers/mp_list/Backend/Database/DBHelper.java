package com.asocialfingers.mp_list.Backend.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = DBHelper.class.getSimpleName();

    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "mp-list";

    public static final String TABLE_NAME = "users";

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PERMISSION = "permission";
    public static final String KEY_CREATED_AT = "created_at";

    public static DBHelper dbHelper;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strCreateTable = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_PERMISSION + " TEXT,"
                + KEY_CREATED_AT + " TEXT)";
        db.execSQL(strCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NAME, null, null);
        db.close();

        Log.d(TAG, "Deleted all users info from sqlite");
    }

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    public String getUsernameData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {dbHelper.KEY_USERNAME};
        Cursor cursor = db.query(dbHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex(dbHelper.KEY_USERNAME));
            buffer.append(username);
        }
        return buffer.toString();
    }

    public String getUserId() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {dbHelper.KEY_USER_ID};
        Cursor cursor = db.query(dbHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex(dbHelper.KEY_USER_ID));
            buffer.append(username);
        }
        return buffer.toString();
    }

    public String getPermissionData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {dbHelper.KEY_PERMISSION};
        Cursor cursor = db.query(dbHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex(dbHelper.KEY_PERMISSION));
            buffer.append(username);
        }
        return buffer.toString();
    }

}
