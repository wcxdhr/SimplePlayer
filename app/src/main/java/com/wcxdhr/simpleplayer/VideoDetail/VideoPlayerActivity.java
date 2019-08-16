package com.wcxdhr.simpleplayer.VideoDetail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.PlayerView;
import com.wcxdhr.simpleplayer.ExoPlayer.ExoPlayerVideoHandler;
import com.wcxdhr.simpleplayer.FloatWindow.FloatWindowService;
import com.wcxdhr.simpleplayer.Log.LogUtil;
import com.wcxdhr.simpleplayer.R;
import com.wcxdhr.simpleplayer.adapter.CommentAdapter;
import com.wcxdhr.simpleplayer.data.Comment;
import com.wcxdhr.simpleplayer.data.Video;
import com.wcxdhr.simpleplayer.db.VideoDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener{

    private Video video;

    private List<Comment> commentList = new ArrayList<>();

    private VideoDao videoDao;

    private TextView videoName;

    private TextView videoAuthor;

    private TextView videoCount;

    private PlayerView playerView;

    private EditText commentText;

    private CommentAdapter adapter;

    private boolean mExoFullScreen;

    private ImageView mFullScreenIcon;

    private com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoDao = new VideoDao(this);
        //Intent intent = getIntent();
        video = (Video)getIntent().getParcelableExtra("video_data");
        initComment();
        mExoFullScreen = false;
        mFullScreenIcon = (ImageView) findViewById(R.id.exo_fullscreen_enter);
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

        /*player = ExoPlayerFactory.newSimpleInstance(this);
        playerView = (PlayerView) findViewById(R.id.player_view);
        playerView.setPlayer(player);*/


        if (ContextCompat.checkSelfPermission(VideoPlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(VideoPlayerActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {

        }
        setTextView();

        sendBtn.setOnClickListener(this);


    }


    @Override
    public void onResume() {
        super.onResume();
        Intent stopIntent = new Intent(this, FloatWindowService.class);
        stopIntent.putExtra("video_data", video);
        stopService(stopIntent);

        playerView = (PlayerView) findViewById(R.id.player_view);
        if (video.getPath() != null && playerView != null) {
            if (ExoPlayerVideoHandler.getInstance()
                    .prepareExoPlayerForUri(this, Uri.parse(video.getPath()), playerView)) {
                video.setCount(video.getCount()+1);
                videoDao.updateCount(video,video.getCount());
            }
            playerView.setPlayer(ExoPlayerVideoHandler.getInstance().getPlayer());
            ExoPlayerVideoHandler.getInstance().goToForeground();

            mFullScreenIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VideoPlayerActivity.this, FullScreenActivity.class);
                    intent.putExtra("video_data", video);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        ExoPlayerVideoHandler.getInstance().goToBackground();
    }

    @Override
    public void onDestroy() {
        //ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        Intent intent = new Intent(this, FloatWindowService.class);
        intent.putExtra("video_data", video);
        startService(intent);
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_btn:
                String content = commentText.getText().toString();
                if (!content.equals("")) {
                    commentText.setText("");
                    Comment comment = new Comment();
                    comment.setContent(content);
                    comment.setVideoId(video.getId());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    comment.setSendTime(simpleDateFormat.format(date));
                    videoDao.addComment(comment);
                    commentList.clear();
                    initComment();
                    adapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
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
                    comment.setVideoId(cursor.getInt(cursor.getColumnIndexOrThrow("video_id")));
                    comment.setSendTime(cursor.getString(cursor.getColumnIndexOrThrow("send_time")));
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(this, "拒绝了", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

   /* @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isShouldHideInput(view, event)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    return imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public boolean isShouldHideInput(View view, MotionEvent event) {
        if (view != null && view instanceof EditText) {
            int[] leftTop = {0 , 0};
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getRawX() > left && event.getRawX() < right && event.getRawY() > top && event.getRawY() < bottom) {
                return false;
            }
            else return true;
        }
        return false;
    }*/


}
