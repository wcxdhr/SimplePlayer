package com.wcxdhr.simpleplayer.data;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Created by wcxdhr on 2019/8/12.
 */

@Dao
public interface VideoDao {

    @Query("SELECT * FROM videos WHERE category = :category")
    MutableLiveData<List<Video>> getVideosByCategory(int category);

    @Query("SELECT * FROM videos WHERE name = :name")
    MutableLiveData<List<Video>> getVideosByName(String name);

    @Query("SELECT * FROM videos WHERE id = :id")
    MutableLiveData<Video> getVideoById(int id);

    @Query("SELECT * FROM videos WHERE author = :author")
    MutableLiveData<List<Video>> getVideosByAuthor(String author);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addVideo(Video video);

    @Query("DELETE FROM videos WHERE id = :id")
    int deleteVideo(int id);

    @Query("UPDATE videos SET count = :count WHERE id = :id")
    void updateCount(int count, int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addComment(Comment comment);

    @Query("SELECT * FROM comments WHERE videoId = :videoId")
    MutableLiveData<List<Comment>> getCommentsByVideo(int videoId);

}
