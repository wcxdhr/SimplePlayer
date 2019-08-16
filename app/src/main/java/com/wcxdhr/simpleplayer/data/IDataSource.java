package com.wcxdhr.simpleplayer.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 * Created by wcxdhr on 2019/8/16.
 */

public interface IDataSource {

    interface LoadVideosCallback {

        void onVideosLoaded(List<Video> videos);

        void onDataNotAvailable();
    }

    interface GetVideoCallback {

        void onVideoLoaded(Video video);

        void onDataNotAvailable();
    }

    interface LoadCommentsCallback {

        void onCommentsLoaded(List<Comment> comments);

        void onDataNotAvailable();
    }

    MutableLiveData<List<Video>> getVideosByCategory(@NonNull int category);

    MutableLiveData<Video> getVideoById(@NonNull int id);

    MutableLiveData<List<Video>> getVideosByName(@NonNull String name);

    MutableLiveData<List<Video>> getVideosByAuthor(@NonNull String author);

    void saveVideo(@NonNull Video video);

    void deleteVideo(@NonNull int id);

    void updateCount(@NonNull int count, @NonNull int id);

    MutableLiveData<List<Comment>> getCommentsByVideo(@NonNull int id);

    void addComment(@NonNull Comment comment);
}
