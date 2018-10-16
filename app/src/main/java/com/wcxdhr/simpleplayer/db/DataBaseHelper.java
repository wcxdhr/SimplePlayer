package com.wcxdhr.simpleplayer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_VIDEO = "create table Video ("
            +"id integer primary key autoincrement, "
            +"name text, "
            +"author text, "
            +"path text, "
            +"count integer, "
            +"category text)";

    public static final String CREATE_CATEGORY = "create table Category ("
            +"id integer primary key autoincrement, "
            +"name text)";

    private Context mContext;

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_VIDEO);
        db.execSQL(CREATE_CATEGORY);
        db.execSQL("insert into Category (id, name)values(1,'动画')");
        db.execSQL("insert into Category (id, name)values(2,'电影')");
        db.execSQL("insert into Category (id, name)values(3,'电视剧')");
        db.execSQL("insert into Category (id, name)values(4,'音乐')");
        db.execSQL("insert into Category (id, name)values(5,'生活')");
        db.execSQL("insert into Category (id, name)values(6,'科技')");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
