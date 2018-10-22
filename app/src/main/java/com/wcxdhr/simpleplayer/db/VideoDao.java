package com.wcxdhr.simpleplayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.wcxdhr.simpleplayer.Log.LogUtil;

public class VideoDao {

    private DataBaseHelper helper;

    private SQLiteDatabase db;

    private Context context;

    public VideoDao(Context context){
        this.context = context;
        helper = new DataBaseHelper(context,"VideoStore.db",null,1);
    }

    public Cursor getCategories(){
        db = helper.getWritableDatabase();
        Cursor cursor = db.query("Category",null,null,null,null,null,null);
        return cursor;
    }

    public Cursor getVideos(int category){
        db = helper.getWritableDatabase();
        Cursor cursor = db.query("Video", null,"category=?",new String[]{String.valueOf(category)},null,null,null);
        return cursor;
    }

    public Cursor findVideosByName(String content) {
        db = helper.getWritableDatabase();
        Cursor cursor = db.query("Video",null,"name LIKE ?",new String[]{"%"+content+"%"},null,null,null);
        return cursor;
    }

    public Cursor findVideosByAuthor(String content) {
        db = helper.getWritableDatabase();
        Cursor cursor = db.query("Video",null,"author LIKE ?",new String[]{"%"+content+"%"},null,null,null);
        return cursor;
    }

    public boolean addVideo(Video video) {
        db = helper.getWritableDatabase();
        Cursor cursor = db.query("Video",null,"name=?",new String[]{video.getName()},null,null,null);
        if (cursor.getCount() > 0) {
            Toast.makeText(context,"视频已存在，请勿重复添加",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            ContentValues values = new ContentValues();
            values.put("name", video.getName());
            values.put("author", video.getAuthor());
            values.put("path",video.getPath());
            values.put("count",video.getCount());
            values.put("category",video.getCategory());
            LogUtil.d("addVideo: "+String.valueOf(video.getCategory()));
            db.insert("Video", null, values);
            return true;
        }
    }

    public void delete(int id){
        db = helper.getWritableDatabase();
        db.delete("Video","id = ?",new String[]{String.valueOf(id)});
    }

    public void updateCount(Video video, int count) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("count", count);
        db.update("Video",values,"id = ?", new String[]{String.valueOf(video.getId())});
    }

    public void addComment(Comment comment) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", comment.getContent());
        values.put("video_id", comment.getVideo_id());
        db.insert("Comment",null, values );
    }

    public Cursor getComments(int video_id) {
        db = helper.getWritableDatabase();
        Cursor cursor = db.query("Comment", null, "video_id=?",new String[]{String.valueOf(video_id)}, null, null,null);
        return cursor;
    }


}

