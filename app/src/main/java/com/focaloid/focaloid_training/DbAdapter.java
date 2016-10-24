package com.focaloid.focaloid_training;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dinil on 08-08-2016.
 */
public class DbAdapter  {

    public static final String DATABASE_NAME= "Register.db";
    public static final int DATABASE_VERSION =1;
    public static final int NAME_COLUMN = 1;
    static final String DATABASE_CREATE= "create table "+"LOGIN"+"( " +"ID"+" integer primary key autoincrement,"+ "USERNAME  text,PASSWORD text); ";

    public SQLiteDatabase db;
    private final Context context;
    private DbHelper dbHelper;

    public  DbAdapter(Context _context)
    {
        context= _context;
        dbHelper=new DbHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public  DbAdapter open() throws SQLException
    {
        //dbHelper=new DbHelper(context);
        db=dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {

        db.close();
        dbHelper.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
       return db;
    }

    public void RegsterUser(String username,String password)
    {
        ContentValues newValues=new ContentValues();

        newValues.put("USERNAME",username);
        newValues.put("PASSWORD",password);

        db.insert("LOGIN",null,newValues);
    }

    public String getsingleUser(String username)
    {
        Cursor cursor = db.query("LOGIN", null, " USERNAME=?", new String[]{username}, null, null, null);

        if(cursor.getCount()<1)
        {
            cursor.close();
            return "NOT EXIST";
        }

        cursor.moveToFirst();
        String password=cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();

        return password;
    }


}
