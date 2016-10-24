package com.focaloid.focaloid_training;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dinil on 08-08-2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context,name,factory,version);

    }

    @Override
    public void onCreate(SQLiteDatabase _db) {
        _db.execSQL(DbAdapter.DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST"+DbAdapter.DATABASE_CREATE);
        onCreate(db);

    }
}
