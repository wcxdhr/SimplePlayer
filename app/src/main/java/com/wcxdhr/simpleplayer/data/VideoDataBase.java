package com.wcxdhr.simpleplayer.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Created by wcxdhr on 2019/8/12.
 */

@Database(entities = {Video.class, Comment.class}, version = 1)
public abstract class VideoDataBase extends RoomDatabase {

    public static VideoDataBase INSTANCE;

    public abstract VideoDao videoDao();

    public static VideoDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (VideoDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VideoDataBase.class, "video.db")
                            .build();
                }
            }
        }
        return INSTANCE;

    }

}
