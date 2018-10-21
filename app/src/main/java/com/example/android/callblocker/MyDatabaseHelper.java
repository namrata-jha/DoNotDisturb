package com.example.android.callblocker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    static final private String DB_NAME = "Contacts Info";
    static final public String DB_TABLE = "Contacts";
    static final private int DB_VER = 1;


    private Context ctx;
    private SQLiteDatabase myDB;

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+DB_TABLE+" (_id integer primary key autoincrement,contact_name text,contact_no text);");
        Log.i("Database", "Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+DB_TABLE);
        onCreate(db);
    }

    void insertData(String name, String number){
        myDB = getWritableDatabase();

        Cursor mCursor = myDB.rawQuery("select* from " + DB_TABLE + " WHERE contact_name=? AND contact_no=?",
                new String[]{name,number});

        if (mCursor.moveToFirst())
            Toast.makeText(ctx, "Contact already added.", Toast.LENGTH_SHORT).show();
        else
            myDB.execSQL("insert into "+DB_TABLE+" (contact_name,contact_no) values('"+name+"','"+number+"');");

    }

    public Cursor getAll(){
        myDB = getReadableDatabase();
        return myDB.rawQuery("select* from "+DB_TABLE+" order by contact_name", null);
    }

    Cursor getNumbers() {
        myDB = getReadableDatabase();
        return myDB.rawQuery("select contact_no from "+DB_TABLE, null);
    }

}
