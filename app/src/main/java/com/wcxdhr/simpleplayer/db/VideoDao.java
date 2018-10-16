package com.wcxdhr.simpleplayer.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VideoDao {

    private DataBaseHelper helper;

    private SQLiteDatabase db;

    public VideoDao(Context context){
        helper = new DataBaseHelper(context,"VideoStore.db",null,1);
    }

    public Cursor getCategories(){
        db = helper.getWritableDatabase();
        Cursor cursor = db.query("Category",null,null,null,null,null,null);
        return cursor;
    }

    public Cursor getVideos(int categoryid){
        db = helper.getWritableDatabase();
        Cursor cursor = db.query("Video", new String[]{"category"},"category=?",new String[]{String.valueOf(categoryid)},null,null,null);
        return cursor;
    }

    public void delete(int id){
        db = helper.getWritableDatabase();
        db.delete("Video","id = ?",new String[]{String.valueOf(id)});
    }


}

