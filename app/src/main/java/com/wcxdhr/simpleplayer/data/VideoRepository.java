package com.wcxdhr.simpleplayer.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 * Created by wcxdhr on 2019/8/12.
 */

public class VideoRepository implements IDataSource{

    private static VideoRepository INSTANCE;

    private VideoDao videoDao;

    private VideoRepository(Context context) {
        videoDao = VideoDataBase.getInstance(context).videoDao();
    }

    public static VideoRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (VideoRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VideoRepository(context);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public MutableLiveData<List<Video>> getVideosByCategory(@NonNull int category) {
        return videoDao.getVideosByCategory(category);
    }

    @Override
    public MutableLiveData<Video> getVideoById(@NonNull int id) {
        return videoDao.getVideoById(id);
    }

    @Override
    public MutableLiveData<List<Video>> getVideosByName(@NonNull String name) {
        return videoDao.getVideosByName(name);
    }

    @Override
    public MutableLiveData<List<Video>> getVideosByAuthor(@NonNull String author) {
        return videoDao.getVideosByAuthor(author);
    }

    @Override
    public void saveVideo(@NonNull Video video) {
        videoDao.addVideo(video);
    }

    @Override
    public void deleteVideo(@NonNull int id) {
        videoDao.deleteVideo(id);
    }

    @Override
    public void updateCount(@NonNull int count, @NonNull int id) {
        videoDao.updateCount(count, id);
    }

    @Override
    public MutableLiveData<List<Comment>> getCommentsByVideo(@NonNull int id) {
        return videoDao.getCommentsByVideo(id);
    }

    @Override
    public void addComment(@NonNull Comment comment) {
        videoDao.addComment(comment);
    }
}
