package com.wcxdhr.simpleplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.wcxdhr.simpleplayer.db.Video;
import com.wcxdhr.simpleplayer.db.VideoDao;

import org.w3c.dom.Text;

import java.io.File;

import javax.sql.DataSource;

public class VideoPlayerActivity extends AppCompatActivity {

    //private VideoView player;

    private Video video;

    //private VideoDao videoDao;

    private TextView videoName;

    private TextView videoAuthor;

    private TextView videoCount;

    private PlayerView playerView;

    private MediaSource videoSource;

    private com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Intent intent = getIntent();
        video = (Video)getIntent().getParcelableExtra("video_data");

        videoName = (TextView) findViewById(R.id.video_name);
        videoAuthor = (TextView) findViewById(R.id.video_author);
        videoCount = (TextView) findViewById(R.id.video_count);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this);
        playerView = (PlayerView) findViewById(R.id.player_view);
        playerView.setPlayer(player);
        dataSourceFactory = new DefaultDataSourceFactory(this,Util.getUserAgent(this,"SimplePlayer"));

        /*player = (VideoView) findViewById(R.id.video_view);
        player.setMediaController(new MediaController(this));*/

        if (ContextCompat.checkSelfPermission(VideoPlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(VideoPlayerActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(video.getPath()));
        }
        setTextView();

        player.prepare(videoSource);

        player.setPlayWhenReady(true);

        //player.start();

    }

    private void setTextView() {
        videoName.setText(video.getName());
        videoAuthor.setText(video.getAuthor());
        videoCount.setText(String.valueOf(video.getCount()));
    }

    /*private void initVideoPath(){

        File file = new File(video.getPath());
        player.setVideoPath(file.getPath());
    }*/

    @Override
    public void onBackPressed() {
        video.setCount(video.getCount()+1);
        VideoDao videoDao = new VideoDao(this);
        videoDao.updateCount(video,video.getCount());
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(video.getPath()));
                }else{
                    Toast.makeText(this, "拒绝了", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

}
