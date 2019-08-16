package com.wcxdhr.simpleplayer.VideoDetail;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.wcxdhr.simpleplayer.ExoPlayer.ExoPlayerVideoHandler;
import com.wcxdhr.simpleplayer.R;
import com.wcxdhr.simpleplayer.data.Video;

public class FullScreenActivity extends AppCompatActivity {

    private boolean isDestroyVideo = true;

    private Video video;

    private ImageView ExoFullScreenIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_screen);

        video = (Video) getIntent().getParcelableExtra("video_data");
        ExoFullScreenIcon = findViewById(R.id.exo_fullscreen_enter);
        ExoFullScreenIcon.setImageResource(R.drawable.exo_controls_fullscreen_exit);
    }

    @Override
    public void onResume() {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onResume();

        PlayerView playerView = (PlayerView) findViewById(R.id.player_view);
        ExoPlayerVideoHandler.getInstance()
                .prepareExoPlayerForUri(getApplicationContext(), Uri.parse(video.getPath()), playerView);
        playerView.setPlayer(ExoPlayerVideoHandler.getInstance().getPlayer());
        ExoPlayerVideoHandler.getInstance().goToForeground();
        ExoFullScreenIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDestroyVideo = false;
                finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        ExoPlayerVideoHandler.getInstance().goToBackground();
    }

    @Override
    public void onBackPressed() {
        isDestroyVideo = false;
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isDestroyVideo) {
            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        }
    }
}
