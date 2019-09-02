package com.wcxdhr.simpleplayer.videolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wcxdhr.simpleplayer.R;;

public class VideoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, VideoListFragment.newInstance())
                    .commitNow();
        }
    }
}
