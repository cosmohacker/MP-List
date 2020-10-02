package com.asocialfingers.mp_list.Backend.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {

    DBHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = DBHelper.getInstance(context);
    }

    public long insertUser(UserDetails userDetails) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.KEY_USERNAME, userDetails.getUsername());
        values.put(dbHelper.KEY_PERMISSION, userDetails.getPermission());
        long id = db.insert(dbHelper.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int deleteUser(long user_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.delete(dbHelper.TABLE_NAME,
                dbHelper.KEY_USER_ID + "= ? ", new String[]{"" + user_id});

        return count;
    }

}
