package com.wcxdhr.simpleplayer;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.wcxdhr.simpleplayer.Log.LogUtil;
import com.wcxdhr.simpleplayer.adapter.CommentAdapter;
import com.wcxdhr.simpleplayer.db.Comment;
import com.wcxdhr.simpleplayer.db.Video;
import com.wcxdhr.simpleplayer.db.VideoDao;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener{

    //private VideoView player;

    private Video video;

    private List<Comment> commentList = new ArrayList<>();

    private VideoDao videoDao;

    private TextView videoName;

    private TextView videoAuthor;

    private TextView videoCount;

    private PlayerView playerView;

    private SimpleExoPlayer player;

    private MediaSource videoSource;

    private EditText commentText;

    private CommentAdapter adapter;

    private boolean mExoFullScreen;

    private Dialog mFullScreenDialog;

    private ImageView mFullScreenIcon;

    private com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoDao = new VideoDao(this);
        Intent intent = getIntent();
        video = (Video)getIntent().getParcelableExtra("video_data");
        initComment();
        mExoFullScreen = false;
        Button sendBtn = (Button) findViewById(R.id.send_btn);
        commentText = (EditText) findViewById(R.id.comment_text);
        RecyclerView commentView = (RecyclerView) findViewById(R.id.video_comment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new CommentAdapter(commentList);
        commentView.setLayoutManager(layoutManager);
        commentView.setAdapter(adapter);
        videoName = (TextView) findViewById(R.id.video_name);
        videoAuthor = (TextView) findViewById(R.id.video_author);
        videoCount = (TextView) findViewById(R.id.video_count);

        player = ExoPlayerFactory.newSimpleInstance(this);
        playerView = (PlayerView) findViewById(R.id.player_view);
        playerView.setPlayer(player);
        initFullScreenDialog();
        initFullScreenImg();
        dataSourceFactory = new DefaultDataSourceFactory(this,Util.getUserAgent(this,"SimplePlayer"));

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

        sendBtn.setOnClickListener(this);

        //player.start();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_btn:
                String content = commentText.getText().toString();
                commentText.setText("");
                Comment comment = new Comment();
                comment.setContent(content);
                comment.setVideo_id(video.getId());
                videoDao.addComment(comment);
                commentList.add(comment);
                adapter.notifyDataSetChanged();
        }

    }

    private void initComment() {
        LogUtil.d("根据视频id获取评论： "+video.getId());
        Cursor cursor =videoDao.getComments(video.getId());
        if (cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                do {
                    Comment comment = new Comment();
                    comment.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    comment.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
                    comment.setVideo_id(cursor.getInt(cursor.getColumnIndexOrThrow("video_id")));
                    commentList.add(comment);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        //adapter.notifyDataSetChanged();
    }

    private void setTextView() {
        videoName.setText(video.getName());
        videoAuthor.setText(video.getAuthor());
        videoCount.setText(String.valueOf(video.getCount()));
    }

    @Override
    public void onBackPressed() {
        video.setCount(video.getCount()+1);
        videoDao.updateCount(video,video.getCount());
        player.release();
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

    private void initFullScreenDialog() {
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoFullScreen) {
                    closeFullScreenDialog();
                }
                super.onBackPressed();
            }
        };
    }

    private void openFullScreenDialog() {
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT ));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(VideoPlayerActivity.this, R.drawable.exo_controls_fullscreen_exit));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mExoFullScreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullScreenDialog() {
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((FrameLayout) findViewById(R.id.play_frameLayout)).addView(playerView);
        mExoFullScreen = false;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(VideoPlayerActivity.this, R.drawable.exo_controls_fullscreen_enter));
    }

    private void initFullScreenImg() {
        mFullScreenIcon = (ImageView) findViewById(R.id.exo_fullscreen_enter);
        mFullScreenIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoFullScreen) {
                    openFullScreenDialog();
                }
                else {
                    closeFullScreenDialog();
                }
            }
        });
    }



}
